package com.busteamproject.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import com.busteamproject.DTO.BusStationInfo;
import com.busteamproject.api.ApiHelper;
import com.busteamproject.databinding.ActivityStationSearchBinding;
import com.busteamproject.util.Util;

import java.util.ArrayList;
import java.util.List;

public class StationActivity extends AppCompatActivity {
    ActivityStationSearchBinding binding;
    private List<BusStationInfo> stationResult;
    private MyHandler myHandler = new MyHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStationSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpList();

        searchBus();
        setUpOnClickListener();
    }

    private void searchBus() {
        binding.searchViewSatation.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ApiHelper apiHelper = ApiHelper.getInstance();
                apiHelper.govStringGet("https://apis.data.go.kr/6410000/busstationservice/getBusStationList",
                        "?serviceKey=ckxCSTx4wV%2FMrdL6AGpQKRuF1AoWEK4E74NmLmE2s0u%2BoETryRg8%2BAwD1S9wDGpoypKr%2BHT8JGRYjJpTRPGvVg%3D%3D" +
                                "&keyword=" + query,
                        result -> {
                            stationResult = Util.parseBusStationSearchResult(result);
                            new Thread(() -> myHandler.sendEmptyMessage(0)).start();
                        });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    private void setUpList() {
        BusStationAdapter adapter = new BusStationAdapter(getApplicationContext(), 0, new ArrayList<>());
        binding.listViewSearchResult.setAdapter(adapter);
    }

    private void setUpOnClickListener() {
        binding.listViewSearchResult.setOnItemClickListener((parent, view, position, id) -> {
		});
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            BusStationAdapter adapter = new BusStationAdapter(getApplicationContext(), 0, stationResult);
            binding.listViewSearchResult.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}