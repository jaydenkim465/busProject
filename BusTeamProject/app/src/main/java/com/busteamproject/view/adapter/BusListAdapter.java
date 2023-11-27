package com.busteamproject.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.busteamproject.DTO.StationBusArrivalInfo;
import com.busteamproject.R;

import java.util.List;

public class BusListAdapter extends ArrayAdapter<StationBusArrivalInfo> {
    public BusListAdapter(Context context, int resource, List<StationBusArrivalInfo> busList) {
        super(context, resource, busList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        StationBusArrivalInfo bus = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view, parent, false);
        }

        TextView tv = convertView.findViewById(R.id.label);
		TextView tv2 = convertView.findViewById(R.id.label2);

		String tv1String = String.format("%s번 버스 (%s행)", bus.getBusInfo().getRouteName(), bus.getBusInfo().getEndStationName());
		String tv2String = String.format("첫번째 버스 : %s 전 정류소(%s분 후 도착)\n두번째 버스 : %s 전 정류소(%s분 후 도착)",
				bus.getLocationNo1(), bus.getPredictTime1(), bus.getLocationNo2(), bus.getPredictTime2());
		tv.setText(tv1String);
        tv2.setText(tv2String);
        return convertView;
    }
}
