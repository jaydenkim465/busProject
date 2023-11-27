package com.busteamproject.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import com.busteamproject.AppConst;
import com.busteamproject.DTO.AddressInfoDTO;
import com.busteamproject.api.ApiHelper;
import com.busteamproject.databinding.ActivityAddressSearchBinding;
import com.busteamproject.util.SharedPreferenceHelper;
import com.busteamproject.util.Util;
import com.busteamproject.view.adapter.AddressAdapter;

import java.util.ArrayList;
import java.util.List;

public class AddressSearchActivity extends AppCompatActivity {
    public static List<AddressInfoDTO> addressList = new ArrayList<>();
    private ActivityAddressSearchBinding binding;
    private MyHandler myHandler = new MyHandler();
	private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddressSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeUI();
    }

	@Override
	protected void onResume() {
		super.onResume();
		if(dialog == null) {
			dialog = new ProgressDialog(this);
		}
	}

    //주소검색하는 부분
    private void initializeUI() {
        binding.searchViewAddress.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
				dialog.show();
                ApiHelper apiHelper = ApiHelper.getInstance();
                apiHelper.kakaoGet("https://dapi.kakao.com/v2/local/search/address",
                        "?query=" + query,
                        result -> {
                            addressList = Util.parseAddressDocument(result);
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

        binding.listViewAddress.setOnItemClickListener((parent, view, position, id) -> {
            SharedPreferenceHelper sharedPreferenceHelper = SharedPreferenceHelper.getInstance(this);
            sharedPreferenceHelper.putString(AppConst.MY_HOME_ADDRESS, addressList.get(position).getAddress_name());
            sharedPreferenceHelper.putString(AppConst.MY_HOME_GPS_X, addressList.get(position).getX());
            sharedPreferenceHelper.putString(AppConst.MY_HOME_GPS_Y, addressList.get(position).getY());
            finish();
        });
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(addressList.isEmpty()) {
                binding.textViewEmptyNotice.setVisibility(View.VISIBLE);
                binding.listViewAddress.setVisibility(View.GONE);
            } else {
                binding.textViewEmptyNotice.setVisibility(View.GONE);
                binding.listViewAddress.setVisibility(View.VISIBLE);
                AddressAdapter adapter = new AddressAdapter(getApplicationContext(), 0, addressList);
                binding.listViewAddress.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
