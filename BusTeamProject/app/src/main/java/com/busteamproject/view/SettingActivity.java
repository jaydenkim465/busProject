package com.busteamproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.busteamproject.databinding.ActivitySettingBinding;

public class SettingActivity extends AppCompatActivity {
	ActivitySettingBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivitySettingBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		initializeUI();
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
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			Toast.makeText(this, String.format("%d 분으로 알림이 설정 되었습니다.", time), Toast.LENGTH_SHORT).show();
		});
	}
}
