package com.busteamproject.view;

import android.content.Context;
import android.content.DialogInterface;
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
import com.busteamproject.DTO.BusStationInfo;
import com.busteamproject.api.ApiHelper;
import com.busteamproject.databinding.ActivityStationSearchBinding;
import com.busteamproject.util.SharedPreferenceHelper;
import com.busteamproject.util.Util;
import com.busteamproject.view.adapter.BusStationAdapter;

import java.util.List;

public class StationSearchActivity extends AppCompatActivity {
	ActivityStationSearchBinding binding;
	private List<BusStationInfo> stationResult;
	private MyHandler myHandler = new MyHandler();
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
				dialog.show();
				ApiHelper apiHelper = ApiHelper.getInstance();
				apiHelper.govStringGet("https://apis.data.go.kr/6410000/busstationservice/getBusStationList",
						"?serviceKey=ckxCSTx4wV%2FMrdL6AGpQKRuF1AoWEK4E74NmLmE2s0u%2BoETryRg8%2BAwD1S9wDGpoypKr%2BHT8JGRYjJpTRPGvVg%3D%3D" +
								"&keyword=" + query,
						result -> {
							stationResult = Util.parseBusStationSearchResult(result);
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

			startActivity(busStation);
		});
	}

	private void GpsSearch() {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location lastKnownLocation = null;
		try {
			lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
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
		if(xString.isEmpty() || yString.isEmpty()) {
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

		if(valueSuitable) {
			coordinateSearch(x, y);
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("경고")
					.setMessage("주소가 등록되지 않거나, 잘못된 주소 값이 입력되었습니다.")
					.setCancelable(false)
					.setPositiveButton("확인", (dialog, which) -> {})
					.create();
			builder.show();
		}
	}

	private void coordinateSearch(double x, double y) {
		ProgressDialog dialog = new ProgressDialog(this);
		dialog.show();
		ApiHelper api = ApiHelper.getInstance();
		String serviceUrl = "https://apis.data.go.kr/6410000/busstationservice/getBusStationAroundList";
		String key = "0y0iJ9SX92FFc%2FLnxp9IAOfbJvBpcvjdwbkJY6cxdJupuPuryYpGB%2B37hnA5%2Fn21dOdPvcwW2%2Bsj7i%2F6A8Y7iQ%3D%3D";
		api.govStringGet(serviceUrl
				, String.format("?serviceKey=%s&x=%f&y=%f", key, x, y)
				, result -> {
					stationResult = Util.parseBusStationSearchResult(result);
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