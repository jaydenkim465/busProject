package com.busteamproject.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import com.busteamproject.DTO.AddressInfoDTO;
import com.busteamproject.Service.AddressService;
import com.busteamproject.api.ApiHelper;
import com.busteamproject.databinding.ActivityAddressSearchBinding;

import java.util.List;

public class AddressSearchActivity extends Activity {
	ActivityAddressSearchBinding binding;
	Context context = this;
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
				api.kakaoGet("https://dapi.kakao.com/v2/local/search/address",
						"?query=" + binding.editTextAddress.getText().toString(),
						result -> {
							AddressService as = new AddressService();
							List<AddressInfoDTO> addressInfoDTOList = as.parseDocument(result);
							binding.recyclerViewAddressList.setText(addressInfoDTOList.toString());
						});
			}
		});
	}
}
