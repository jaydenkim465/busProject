package com.busteamproject.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import com.busteamproject.DTO.Document;
import com.busteamproject.Service.AddressService;
import com.busteamproject.api.ApiHelper;
import com.busteamproject.api.CallBack;
import com.busteamproject.databinding.ActivityAddressSearchBinding;
import org.json.JSONObject;

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
				api.get("https://dapi.kakao.com/v2/local/search/address",
						"?query=" + binding.editTextAddress.getText().toString(),
						new CallBack() {
							@Override
							public void ApiResult(JSONObject result){
								AddressService as = new AddressService();
								List<Document> documentList=as.parseDocument(result);
								binding.recyclerViewAddressList.setText(documentList.toString());

								// 리싸이클러뷰 하려다가 실패 ㅜ
//								LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
//								binding.recyclerView.setLayoutManager(linearLayoutManager);
//								binding.recyclerView.setAdapter(new CustomAdapter(documentList));
//								RecyclerView recyclerView = findViewById(R.id.recyclerView);
//								recyclerView.setLayoutManager(linearLayoutManager);  // LayoutManager 설정
//								CustomAdapter customAdapter = new CustomAdapter(documentList);
//								binding.recyclerView.setAdapter(customAdapter);
//								recyclerView.setAdapter(customAdapter); // 어댑터 설정

							}
						});
			}
		});
	}
}
