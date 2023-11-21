package com.busteamproject.view;

import android.app.Activity;
import android.os.Bundle;
import com.busteamproject.api.ApiHelper;
import com.busteamproject.databinding.ActivityAddressSearchBinding;

public class AddressSearchActivity extends Activity {
	ActivityAddressSearchBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityAddressSearchBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		initializeUI();
	}

	public void initializeUI() {
		ApiHelper api = ApiHelper.getInstance();
		binding.buttonSearch.setOnClickListener(view -> {
			if(!binding.editTextAddress.getText().toString().isEmpty()) {
				api.get("https://dapi.kakao.com/v2/local/search/address",
						"?query=" + binding.editTextAddress.getText().toString(), result -> {
							binding.recyclerViewAddressList.setText(result.toString());
							result.toString();
						});
			}
		});
	}
}
