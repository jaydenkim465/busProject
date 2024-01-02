package com.busteamproject.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.busteamproject.AppConst;
import com.busteamproject.DTO.BookMarkDTO;
import com.busteamproject.DTO.BusArrivalInfoDTO;
import com.busteamproject.DTO.BusRouteDTO;
import com.busteamproject.DTO.CityCodeDTO;
import com.busteamproject.DTO.citycode.Data;
import com.busteamproject.DTO.gyeonggi.busroute.BusRouteInfo;
import com.busteamproject.api.ApiHelper;
import com.busteamproject.databinding.ActivityMainBinding;
import com.busteamproject.util.BookMarkHelper;
import com.busteamproject.util.SharedPreferenceHelper;
import com.busteamproject.util.Util;
import com.busteamproject.view.adapter.BookMarkListAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
	private final ActivityResultLauncher<String> requestPermissionLauncher =
			registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
			});
	private final MyHandler myHandler = new MyHandler();
	private final Map<String, BusRouteInfo> busInfoList = new HashMap<>();
	private ActivityMainBinding binding;
	private BookMarkHelper helper;
	private List<BusArrivalInfoDTO> bookMarkList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		initializeUI();
		helper = BookMarkHelper.getInstance(this);
		checkBookMarkInfo();
	}

	@Override
	protected void onResume() {
		super.onResume();
		requestPermission();
	}

	public void initializeUI() {
		binding.buttonSetting.setOnClickListener(view -> {
			Intent settingIntent = new Intent(this, SettingActivity.class);
			startActivity(settingIntent);
		});

		binding.searchBarStationSearch.setOnClickListener(view -> {
			Intent stationSearchIntent = new Intent(this, StationSearchActivity.class);
			startActivity(stationSearchIntent);
		});

		binding.buttonRefresh.setOnClickListener(view -> {
			checkBookMarkInfo();
		});

		binding.listViewBookMark.setOnItemClickListener((parent, view, position, id) -> {
			Intent busStation = new Intent(this, BusStationActivity.class);
			busStation.putExtra(AppConst.INTENT_STATION_ID, bookMarkList.get(position).getStationId());
			busStation.putExtra(AppConst.INTENT_STATION_NAME, bookMarkList.get(position).getStationName());
			busStation.putExtra(AppConst.INTENT_STATION_NO, helper.getBookMarkList().get(position).getStationNo());
			busStation.putExtra(AppConst.INTENT_STATION_X, helper.getBookMarkList().get(position).getStationX());
			busStation.putExtra(AppConst.INTENT_STATION_Y, helper.getBookMarkList().get(position).getStationY());

			startActivity(busStation);
		});
	}

	private void requestPermission() {
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
		} else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS)) {
		} else {
			requestPermissionLauncher.launch(
					Manifest.permission.POST_NOTIFICATIONS
			);
		}

		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
		} else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
		} else {
			requestPermissionLauncher.launch(
					Manifest.permission.ACCESS_FINE_LOCATION
			);
		}
	}

	private void checkBookMarkInfo() {
		List<BookMarkDTO> dataList = helper.getBookMarkList();
		if (!dataList.isEmpty()) {
			ProgressDialog progressDialog = new ProgressDialog(this);
			progressDialog.show();

			new Thread(() -> {
				bookMarkList = new ArrayList<>();
				for (BookMarkDTO bookMark : dataList) {
					if (bookMark.getType().equals("B")) {
						String apiKey = Util.getApiKey(this, "busArrivalInfoKey");
						String url = "https://apis.data.go.kr/6410000/busarrivalservice/getBusArrivalList";
						String parameter = String.format("?serviceKey=%s&stationId=%s&routeId=%s", apiKey, bookMark.getStationId(), bookMark.getRouteId());

						ApiHelper api = ApiHelper.getInstance();
						String result = api.govStringGet(url, parameter);
						List<BusArrivalInfoDTO> tempList = Util.parseBusStationArrivalInfo(result, bookMark.getStationName());
						for (int i = 0; i < tempList.size(); i++) {
							if (!bookMark.getRouteId().equals(tempList.get(i).getRouteId())) {
								continue;
							}

							BusRouteInfo busRouteInfo = busInfoList.get(tempList.get(i).getRouteId());
							if (busRouteInfo != null) {
								tempList.get(i).setBusRouteInfo(busRouteInfo);
							} else {
								apiKey = Util.getApiKey(this, "busRouteInfoKey");
								url = "https://apis.data.go.kr/6410000/busrouteservice/getBusRouteInfoItem";
								parameter = String.format("?serviceKey=%s&routeId=%s", apiKey, tempList.get(i).getRouteId());

								result = Util.convertXmlToJson(api.govStringGet(url, parameter)).toString();
								BusRouteDTO busRouteDTO = new Gson().fromJson(result, BusRouteDTO.class);
								busRouteInfo = busRouteDTO.getResponse().getMsgBody().getBusRouteInfoItem();

								tempList.get(i).setBusRouteInfo(busRouteInfo);
								tempList.get(i).setStationX(bookMark.getStationX());
								tempList.get(i).setStationY(bookMark.getStationY());
								busInfoList.put(tempList.get(i).getRouteId(), busRouteInfo);
							}
							bookMarkList.add(tempList.get(i));
						}
					} else if (bookMark.getType().equals("S")) {
						BusArrivalInfoDTO tempData = new BusArrivalInfoDTO(bookMark.getStationId(), bookMark.getStationName());
						tempData.setStationNo(bookMark.getStationNo());
						tempData.setStationX(bookMark.getStationX());
						tempData.setStationY(bookMark.getStationY());
						bookMarkList.add(tempData);
					}
				}
				myHandler.sendEmptyMessage(0);
				progressDialog.dismiss();
			}).start();
		}
	}

	private void checkCityCode() {
		SharedPreferenceHelper sharedData = SharedPreferenceHelper.getInstance(this);
		Set<String> cityCodeList = sharedData.getStringSet(AppConst.CITY_CODE_LIST);

		if (cityCodeList.isEmpty()) {
			ApiHelper api = ApiHelper.getInstance();
			api.govStringGet("https://apis.data.go.kr/1613000/ArvlInfoInqireService/getCtyCodeList",
					"?serviceKey=" + Util.getApiKey(this, "cityCodeKey") +
							"&_type=json",
					result -> {
						CityCodeDTO cityCodeDTO = new Gson().fromJson(result, CityCodeDTO.class);
						Set<String> citySet = new HashSet<>();
						for (Data data : cityCodeDTO.getResponse().getBody().getItems().getItem()) {
							citySet.add(String.format("%s|%s", data.getCitycode(), data.getCityname()));
						}
						sharedData.putStringSet(AppConst.CITY_CODE_LIST, citySet);
					});
		}
	}

	private class MyHandler extends Handler {
		@Override
		public void handleMessage(@NonNull Message msg) {
			if (bookMarkList.isEmpty()) {
				binding.listViewBookMark.setVisibility(View.GONE);
				binding.textViewEmptyNotice.setVisibility(View.VISIBLE);
			} else {
				binding.listViewBookMark.setVisibility(View.VISIBLE);
				binding.textViewEmptyNotice.setVisibility(View.GONE);
				BookMarkListAdapter bookMarkListAdapter = new BookMarkListAdapter(MainActivity.this, 0, bookMarkList);
				binding.listViewBookMark.setAdapter(bookMarkListAdapter);
				bookMarkListAdapter.notifyDataSetChanged();
			}
		}
	}
}