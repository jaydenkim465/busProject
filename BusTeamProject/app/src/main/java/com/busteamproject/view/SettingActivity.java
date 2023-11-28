package com.busteamproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.busteamproject.AppConst;
import com.busteamproject.databinding.ActivitySettingBinding;
import com.busteamproject.notification.BusAlarmService;
import com.busteamproject.util.SharedPreferenceHelper;

public class SettingActivity extends AppCompatActivity {
	ActivitySettingBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivitySettingBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		initializeUI();
	}

	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferenceHelper sharedPreferenceHelper = SharedPreferenceHelper.getInstance(this);
		String address = sharedPreferenceHelper.getString(AppConst.MY_HOME_ADDRESS);
		if(!address.isEmpty()) {
			binding.buttonAddress.setText(address);
		}
	}

	public void initializeUI() {
		binding.buttonAddress.setOnClickListener(view -> {
			Intent addressSearch = new Intent(this, AddressSearchActivity.class);
			startActivity(addressSearch);
		});

		binding.buttonAlarmTimeSave.setOnClickListener(view -> {
			int time = 0;
			try {
				time = Integer.parseInt(binding.editTextAlarmTime.getText().toString());
				SharedPreferenceHelper helper = SharedPreferenceHelper.getInstance(getApplicationContext());
				helper.putInt(AppConst.ALARM_DEFAULT_TIME, time);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			Toast.makeText(this, String.format("%d 분으로 알림이 설정 되었습니다.", time), Toast.LENGTH_SHORT).show();
		});

		binding.buttonTestServiceStart.setOnClickListener(view -> {
			Intent settingIntent = new Intent(this, BusAlarmService.class);
			settingIntent.setAction(AppConst.SERVICE_START);
			settingIntent.putExtra(AppConst.NOTIFICATION_TITLE, "333번 버스");
			settingIntent.putExtra(AppConst.NOTIFICATION_MSG, "신흥역.종합시장(광역) 정류소");
			settingIntent.putExtra(AppConst.NOTIFICATION_BUS_TIME, 20);
			settingIntent.putExtra(AppConst.NOTIFICATION_WALK_TIME, 10);
			startService(settingIntent);
		});

		binding.buttonTestServiceStop.setOnClickListener(view -> {
			Intent settingIntent = new Intent(this, BusAlarmService.class);
			stopService(settingIntent);
		});
	}
}
