package com.busteamproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.busteamproject.AppConst;
import com.busteamproject.databinding.ActivitySettingBinding;
import com.busteamproject.notification.BusAlarmService;
import com.busteamproject.notification.NotificationHelper;
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
		if (!address.isEmpty()) {
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
			NotificationHelper helper = new NotificationHelper("333번 버스", "신흥역.종합시장(광역) 정류소", 20, 10, 0);
			if (!helper.isServiceRunning(this)) {
				helper.start(this);
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("안내")
						.setMessage("알림이 작동 중입니다. 중지하고 다시 시도 해주세요.")
						.setCancelable(false)
						.setPositiveButton("확인", (dialog, which) -> {})
						.create();
				builder.show();
			}
		});

		binding.buttonTestServiceStop.setOnClickListener(view -> {
			NotificationHelper notificationHelper = new NotificationHelper();
			notificationHelper.stop(this);
		});

		SharedPreferenceHelper helper = SharedPreferenceHelper.getInstance(this);
		binding.editTextAlarmTime.setText(String.valueOf(helper.getInt(AppConst.ALARM_DEFAULT_TIME)));
	}
}
