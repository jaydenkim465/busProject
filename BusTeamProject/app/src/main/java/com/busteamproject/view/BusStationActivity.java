package com.busteamproject.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.busteamproject.AppConst;
import com.busteamproject.DTO.BusArrivalInfoDTO;
import com.busteamproject.DTO.BusRouteDTO;
import com.busteamproject.DTO.gyeonggi.busroute.BusRouteInfo;
import com.busteamproject.R;
import com.busteamproject.api.ApiHelper;
import com.busteamproject.databinding.ActivityBusStationBinding;
import com.busteamproject.util.BookMarkHelper;
import com.busteamproject.util.Util;
import com.busteamproject.view.adapter.BusListAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusStationActivity extends Activity {
	private final MyHandler myHandler = new MyHandler();
	private final Map<String, BusRouteInfo> busInfoList = new HashMap<>();
	ActivityBusStationBinding binding;
	private List<BusArrivalInfoDTO> busList = new ArrayList<>();
	private BookMarkHelper bookMarkHelper;

	private String stationId = "";
	private String stationX = "";
	private String stationY = "";
	private String stationNo = "";
	private String stationName = "정류소 이름 없음";

	private boolean isBookMark = false;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityBusStationBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		bookMarkHelper = BookMarkHelper.getInstance(this);
		initializeUI(getIntent());
		busStationInfo();

		isBookMark = bookMarkHelper.findBookMark("S", stationId);
		changeBookMarkUI();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (busList.isEmpty()) {
			binding.listViewBusList.setVisibility(View.GONE);
			binding.textViewEmptyNotice.setVisibility(View.VISIBLE);
		} else {
			binding.listViewBusList.setVisibility(View.VISIBLE);
			binding.textViewEmptyNotice.setVisibility(View.GONE);
		}
	}

	private void initializeUI(Intent intent) {
		String stationInfo = "정류소 정보 없음";
		if (intent != null) {
			stationName = intent.getExtras().getString(AppConst.INTENT_STATION_NAME, stationName);
			stationNo = intent.getExtras().getString(AppConst.INTENT_STATION_NO, stationName);
			stationId = intent.getExtras().getString(AppConst.INTENT_STATION_ID, "");
			stationX = intent.getExtras().getString(AppConst.INTENT_STATION_X, "");
			stationY = intent.getExtras().getString(AppConst.INTENT_STATION_Y, "");
		}
		binding.textViewStationName.setText(String.format("%s (%s)", stationName, stationNo));
		binding.buttonRefresh.setOnClickListener(view -> busStationInfo());
		binding.buttonBookMark.setOnClickListener(view -> {
			isBookMark = !isBookMark;
			changeBookMarkUI();
			if (isBookMark) {
				bookMarkHelper.addBookMarkList("S", "", stationId, stationName, stationNo, stationX, stationY);
			} else {
				bookMarkHelper.removeBookMarkList("S", stationId);
			}
		});
	}

	private void busStationInfo() {
		ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.show();
		if (!stationId.isEmpty()) {
			new Thread(() -> {
				bookMarkHelper.getBookMarkList();

				String apiKey = Util.getApiKey(this, "busArrivalInfoKey");
				String url = "https://apis.data.go.kr/6410000/busarrivalservice/getBusArrivalList";
				String parameter = String.format("?serviceKey=%s&stationId=%s", apiKey, stationId);

				ApiHelper api = ApiHelper.getInstance();
				String result = api.govStringGet(url, parameter);
				busList = Util.parseBusStationArrivalInfo(result, stationName);
				for (int i = 0; i < busList.size(); i++) {
					BusRouteInfo busRouteInfo = busInfoList.get(busList.get(i).getRouteId());
					if (busRouteInfo != null) {
						busList.get(i).setBusRouteInfo(busRouteInfo);
					} else {
						apiKey = Util.getApiKey(this, "busRouteInfoKey");
						url = "https://apis.data.go.kr/6410000/busrouteservice/getBusRouteInfoItem";
						parameter = String.format("?serviceKey=%s&routeId=%s", apiKey, busList.get(i).getRouteId());

						result = Util.convertXmlToJson(api.govStringGet(url, parameter)).toString();
						BusRouteDTO busRouteDTO = new Gson().fromJson(result, BusRouteDTO.class);
						busRouteInfo = busRouteDTO.getResponse().getMsgBody().getBusRouteInfoItem();

						busList.get(i).setBusRouteInfo(busRouteInfo);
						busList.get(i).setStationNo(stationNo);
						busList.get(i).setStationX(stationX);
						busList.get(i).setStationY(stationY);
						busInfoList.put(busList.get(i).getRouteId(), busRouteInfo);
					}

					if (bookMarkHelper.findBookMark("B", busList.get(i).getRouteId())) {
						busList.get(i).setBookMark(true);
					}
				}
				myHandler.sendEmptyMessage(0);
				progressDialog.dismiss();
			}).start();
		}
	}

	private void changeBookMarkUI() {
		if (isBookMark) {
			binding.buttonBookMark.setBackground(getDrawable(R.drawable.star_fill_48px));
		} else {
			binding.buttonBookMark.setBackground(getDrawable(R.drawable.star_48px));
		}
	}

	private class MyHandler extends Handler {
		@Override
		public void handleMessage(@NonNull Message msg) {
			if (busList.isEmpty()) {
				binding.listViewBusList.setVisibility(View.GONE);
				binding.textViewEmptyNotice.setVisibility(View.VISIBLE);
			} else {
				binding.listViewBusList.setVisibility(View.VISIBLE);
				binding.textViewEmptyNotice.setVisibility(View.GONE);
				BusListAdapter busListAdapter = new BusListAdapter(BusStationActivity.this, 0, busList);
				binding.listViewBusList.setAdapter(busListAdapter);
				busListAdapter.notifyDataSetChanged();
			}
		}
	}
}
