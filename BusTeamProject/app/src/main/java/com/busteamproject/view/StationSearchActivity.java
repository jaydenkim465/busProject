package com.busteamproject.view;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.busteamproject.AppConst;
import com.busteamproject.DTO.BusStationSearchDTO;
import com.busteamproject.DTO.gyeonggi.busstation.BusStationInfo;
import com.busteamproject.api.ApiHelper;
import com.busteamproject.databinding.ActivityStationSearchBinding;
import com.busteamproject.util.SharedPreferenceHelper;
import com.busteamproject.util.Util;
import com.busteamproject.view.adapter.BusStationAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class StationSearchActivity extends AppCompatActivity {
	private final MyHandler myHandler = new MyHandler();
	ActivityStationSearchBinding binding;
	private List<BusStationInfo> stationResult;
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityStationSearchBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		initializeUI();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (dialog == null) {
			dialog = new ProgressDialog(this);
		}
	}

	private void initializeUI() {
		binding.searchViewSatation.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				String url = "https://apis.data.go.kr/6410000/busstationservice/getBusStationList";
				String apikey = Util.getApiKey(getApplicationContext(), "stationSearchKey");
				String parameter = String.format("?serviceKey=%s&keyword=%s", apikey, query);

				dialog.show();
				ApiHelper apiHelper = ApiHelper.getInstance();
				apiHelper.govStringGet(url, parameter, result -> {
					try {
						BusStationSearchDTO busStationSearchDTO = new Gson().fromJson(Util.convertXmlToJson(result).toString(), BusStationSearchDTO.class);
						if (busStationSearchDTO.getResponse().getMsgHeader().getResultCode() == 0) {
							stationResult = busStationSearchDTO.getResponse().getMsgBody().getBusStationList();
						} else {
							stationResult = new ArrayList<>();
						}
					} catch (Exception e) {
						e.printStackTrace();
						stationResult = new ArrayList<>();
					}
					myHandler.sendEmptyMessage(0);
					dialog.dismiss();
				});
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});

		binding.buttonGpsSearchStation.setOnClickListener(view -> GpsSearch());

		binding.buttonAddressSearchStation.setOnClickListener(view -> AddressSearch());

		binding.listViewSearchResult.setOnItemClickListener((parent, view, position, id) -> {
			Intent busStation = new Intent(this, BusStationActivity.class);
			busStation.putExtra(AppConst.INTENT_STATION_ID, stationResult.get(position).getStationId());
			busStation.putExtra(AppConst.INTENT_STATION_NAME, stationResult.get(position).getStationName());
			busStation.putExtra(AppConst.INTENT_STATION_NO, stationResult.get(position).getMobileNo());
			busStation.putExtra(AppConst.INTENT_STATION_X, String.valueOf(stationResult.get(position).getX()));
			busStation.putExtra(AppConst.INTENT_STATION_Y, String.valueOf(stationResult.get(position).getY()));

			startActivity(busStation);
		});
	}

	private void GpsSearch() {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location lastKnownLocation = null;
		try {
			lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (lastKnownLocation == null) {
				lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}
		} catch (SecurityException e) {//권한오류 GPS
			e.printStackTrace();
		}

		if (lastKnownLocation != null) {
			coordinateSearch(lastKnownLocation.getLongitude(), lastKnownLocation.getLatitude());
		}
	}

	private void AddressSearch() {
		SharedPreferenceHelper sharedPreferenceHelper = SharedPreferenceHelper.getInstance(this);
		String xString = sharedPreferenceHelper.getString(AppConst.MY_HOME_GPS_X);
		String yString = sharedPreferenceHelper.getString(AppConst.MY_HOME_GPS_Y);
		double x = 0d;
		double y = 0d;

		boolean valueSuitable = true;
		if (xString.isEmpty() || yString.isEmpty()) {
			valueSuitable = false;
		} else {
			try {
				x = Double.parseDouble(xString);
				y = Double.parseDouble(yString);
			} catch (Exception ex) {
				ex.printStackTrace();
				valueSuitable = false;
			}
		}

		if (valueSuitable) {
			coordinateSearch(x, y);
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			String title = "경고";
			String message = "주소가 등록되지 않거나, 잘못된 주소 값이 입력되었습니다.";

			builder.setTitle(title)
					.setMessage(message)
					.setCancelable(false)
					.setPositiveButton("확인", (dialog, which) -> {
					}).create();
			builder.show();
		}
	}

	private void coordinateSearch(double x, double y) {
		ProgressDialog dialog = new ProgressDialog(this);
		dialog.show();
		ApiHelper api = ApiHelper.getInstance();
		String serviceUrl = "https://apis.data.go.kr/6410000/busstationservice/getBusStationAroundList";
		String key = Util.getApiKey(this, "coordinateSearchKey");
		String parameter = String.format("?serviceKey=%s&x=%f&y=%f", key, x, y);
		api.govStringGet(serviceUrl, parameter, result -> {
			try {
				String json = Util.convertXmlToJson(result).toString();
				BusStationSearchDTO busStationSearchDTO = new Gson().fromJson(json, BusStationSearchDTO.class);
				if (busStationSearchDTO.getResponse().getMsgHeader().getResultCode() == 0) {
					stationResult = busStationSearchDTO.getResponse().getMsgBody().getBusStationAroundList();
				} else {
					stationResult = new ArrayList<>();
				}
			} catch (Exception e) {
				e.printStackTrace();
				stationResult = new ArrayList<>();
			}
			myHandler.sendEmptyMessage(0);
			dialog.dismiss();
		});
	}

	private class MyHandler extends Handler {
		@Override
		public void handleMessage(@NonNull Message msg) {
			if (stationResult.isEmpty()) {
				binding.textViewEmptyNotice.setVisibility(View.VISIBLE);
				binding.listViewSearchResult.setVisibility(View.GONE);
			} else {
				binding.textViewEmptyNotice.setVisibility(View.GONE);
				binding.listViewSearchResult.setVisibility(View.VISIBLE);
				BusStationAdapter adapter = new BusStationAdapter(getApplicationContext(), 0, stationResult);
				binding.listViewSearchResult.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}
		}
	}
}