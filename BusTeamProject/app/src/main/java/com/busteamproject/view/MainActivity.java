package com.busteamproject.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.busteamproject.AppConst;
import com.busteamproject.DTO.CityCodeDTO;
import com.busteamproject.DTO.citycode.Data;
import com.busteamproject.api.ApiHelper;
import com.busteamproject.databinding.ActivityMainBinding;
import com.busteamproject.util.SharedPreferenceHelper;
import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
	private ActivityMainBinding binding;
	private ActivityResultLauncher<String> requestPermissionLauncher =
			registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {});


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		initializeUI();
	}

	@Override
	protected void onResume() {
		super.onResume();
		requestPermission();
		checkCityCode();
	}

	public void initializeUI() {
		binding.buttonSetting.setOnClickListener(view -> {
			Intent settingIntent = new Intent(this, SettingActivity.class);
			startActivity(settingIntent);
		});

		binding.editTextSearch.setOnClickListener(view -> {
			Intent stationSearchIntent = new Intent(this, StationActivity.class);
			startActivity(stationSearchIntent);
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
	}

	private void checkCityCode() {
		SharedPreferenceHelper sharedData = SharedPreferenceHelper.getInstance(this);
		Set<String> cityCodeList = sharedData.getStringSet(AppConst.CITY_CODE_LIST);

		if(cityCodeList.isEmpty()) {
			ApiHelper api = ApiHelper.getInstance();
			api.govStringGet("https://apis.data.go.kr/1613000/ArvlInfoInqireService/getCtyCodeList",
					"?serviceKey=0y0iJ9SX92FFc%2FLnxp9IAOfbJvBpcvjdwbkJY6cxdJupuPuryYpGB%2B37hnA5%2Fn21dOdPvcwW2%2Bsj7i%2F6A8Y7iQ%3D%3D" +
							"&_type=json",
					result -> {
						CityCodeDTO cityCodeDTO = new Gson().fromJson(result, CityCodeDTO.class);
						Set<String> citySet = new HashSet<>();
						for(Data data : cityCodeDTO.getResponse().getBody().getItems().getItem()) {
							citySet.add(String.format("%s|%s", data.getCitycode(), data.getCityname()));
						}
						sharedData.putStringSet(AppConst.CITY_CODE_LIST, citySet);
					});
		}
	}
}