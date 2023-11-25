package com.busteamproject.view;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.busteamproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
	private ActivityMainBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		initializeUI();
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
}