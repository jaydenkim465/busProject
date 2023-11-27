package com.busteamproject.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.busteamproject.AppConst;
import com.busteamproject.DTO.BusDTO;
import com.busteamproject.DTO.StationBusArrivalInfo;
import com.busteamproject.api.ApiHelper;
import com.busteamproject.databinding.ActivityBusStationBinding;
import com.busteamproject.util.Util;
import com.busteamproject.view.adapter.BusListAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusStationActivity extends Activity {
	ActivityBusStationBinding binding;
	private String stationId = "";
	private MyHandler myHandler = new MyHandler();
	private List<StationBusArrivalInfo> busList;
	private Map<String, BusDTO> busInfoList = new HashMap<>();

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityBusStationBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		initializeUI(getIntent());
		busStationInfo();
	}

	private void initializeUI(Intent intent) {
		String stationName = "정류소 이름 없음";
		String stationInfo = "정류소 정보 없음";
		if (intent != null) {
			stationName = intent.getExtras().getString(AppConst.INTENT_STATION_NAME, stationName);
			stationId = intent.getExtras().getString(AppConst.INTENT_STATION_ID, "");
		}
		binding.textViewStationName.setText(stationName);
		binding.buttonRefresh.setOnClickListener(view -> {
			busStationInfo();
		});
	}

	private void busStationInfo() {
		ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.show();
		if(!stationId.isEmpty()) {
			new Thread(() -> {
				ApiHelper api = ApiHelper.getInstance();
				String result = api.govStringGet("https://apis.data.go.kr/6410000/busarrivalservice/getBusArrivalList",
						"?serviceKey=ckxCSTx4wV%2FMrdL6AGpQKRuF1AoWEK4E74NmLmE2s0u%2BoETryRg8%2BAwD1S9wDGpoypKr%2BHT8JGRYjJpTRPGvVg%3D%3D" +
								"&stationId=" + stationId);
				busList = Util.parseBusStationArrivalInfo(result);
				for(int i = 0; i < busList.size(); i++) {
					BusDTO busDTO = busInfoList.get(busList.get(i).getRouteId());
					if(busDTO != null) {
						busList.get(i).setBusInfo(busDTO);
					} else {
						result = api.govStringGet("https://apis.data.go.kr/6410000/busrouteservice/getBusRouteInfoItem",
								"?serviceKey=qd8%2BoFaqwR%2B16s53dhTsjIhyXxGaHAwaZ5VOSL0yJPnjy%2FbPsZXkQvf7KLJLKfxdoP5i5jV1yKO4UQgmBPTlPQ%3D%3D" +
										"&routeId=" + busList.get(i).getRouteId());
						busDTO = Util.parseBusInfo(result);
						busList.get(i).setBusInfo(busDTO);
						busInfoList.put(busList.get(i).getRouteId(), busDTO);
					}
				}
				myHandler.sendEmptyMessage(0);
				progressDialog.dismiss();
			}).start();
		}
	}

	private class MyHandler extends Handler {
		@Override
		public void handleMessage(@NonNull Message msg) {
			BusListAdapter busListAdapter = new BusListAdapter(getApplicationContext(), 0, busList);
			binding.listViewBusList.setAdapter(busListAdapter);
			busListAdapter.notifyDataSetChanged();
		}
	}
}
