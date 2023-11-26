package com.busteamproject.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.busteamproject.AppConst;
import com.busteamproject.DTO.BusLocationDTO;
import com.busteamproject.api.ArivalTimeApi;
import com.busteamproject.databinding.ActivityBusStationBinding;
import com.busteamproject.util.Util;

import java.util.List;

public class BusStationActivity extends Activity {
	ActivityBusStationBinding binding;
	private String stationId = "";
	private MyHandler myHandler = new MyHandler();
	private List<BusLocationDTO> busList;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityBusStationBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		initializeUI(getIntent());
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(!stationId.isEmpty()) {
			ArivalTimeApi arivalTimeApi = new ArivalTimeApi();
			// 성남시, 성남시 의료원
			arivalTimeApi.getArivalTimeOfBus("31020", "GGB204000105", result -> {
				busList = Util.parseBusLocationResult(result);
				myHandler.sendEmptyMessage(0);
				new Thread(() -> myHandler.sendEmptyMessage(0)).start();
			});
		}
	}

	private void initializeUI(Intent intent) {
		String stationName = "정류소 이름 없음";
		String stationInfo = "정류소 정보 없음";
		if(intent != null) {
			stationName = intent.getExtras().getString(AppConst.INTENT_STATION_NAME, stationName);
			stationId = intent.getExtras().getString(AppConst.INTENT_STATION_ID, "");
		}
		binding.textViewStationName.setText(stationName);
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
