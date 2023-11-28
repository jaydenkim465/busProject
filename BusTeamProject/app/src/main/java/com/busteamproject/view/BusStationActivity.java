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
import com.busteamproject.DTO.BusDTO;
import com.busteamproject.DTO.StationBusArrivalInfo;
import com.busteamproject.R;
import com.busteamproject.api.ApiHelper;
import com.busteamproject.databinding.ActivityBusStationBinding;
import com.busteamproject.util.BookMarkHelper;
import com.busteamproject.util.Util;
import com.busteamproject.view.adapter.BusListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusStationActivity extends Activity {
	ActivityBusStationBinding binding;
	private MyHandler myHandler = new MyHandler();
	private List<StationBusArrivalInfo> busList = new ArrayList<>();
	private Map<String, BusDTO> busInfoList = new HashMap<>();
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
		if(busList.isEmpty()) {
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
			if(isBookMark) {
				bookMarkHelper.addBookMarkList("S", "", stationId, stationName, stationNo, stationX, stationY);
			} else {
				bookMarkHelper.removeBookMarkList("S", stationId);
			}
		});
	}

	private void busStationInfo() {
		ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.show();
		if(!stationId.isEmpty()) {
			new Thread(() -> {
				bookMarkHelper.getBookMarkList();
				ApiHelper api = ApiHelper.getInstance();
				String result = api.govStringGet("https://apis.data.go.kr/6410000/busarrivalservice/getBusArrivalList",
						"?serviceKey=ckxCSTx4wV%2FMrdL6AGpQKRuF1AoWEK4E74NmLmE2s0u%2BoETryRg8%2BAwD1S9wDGpoypKr%2BHT8JGRYjJpTRPGvVg%3D%3D" +
								"&stationId=" + stationId);
				busList = Util.parseBusStationArrivalInfo(result, stationName);
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
						busList.get(i).setStationNo(stationNo);
						busList.get(i).setStationX(stationX);
						busList.get(i).setStationY(stationY);
						busInfoList.put(busList.get(i).getRouteId(), busDTO);
					}

					if(bookMarkHelper.findBookMark("B", busList.get(i).getRouteId())) {
						busList.get(i).setBookMark(true);
					}
				}
				myHandler.sendEmptyMessage(0);
				progressDialog.dismiss();
			}).start();
		}
	}

	private void changeBookMarkUI() {
		if(isBookMark) {
			binding.buttonBookMark.setBackground(getDrawable(R.drawable.star_fill_48px));
		} else {
			binding.buttonBookMark.setBackground(getDrawable(R.drawable.star_48px));
		}
	}

	private class MyHandler extends Handler {
		@Override
		public void handleMessage(@NonNull Message msg) {
			if(busList.isEmpty()) {
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
