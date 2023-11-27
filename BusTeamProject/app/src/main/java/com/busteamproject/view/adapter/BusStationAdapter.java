package com.busteamproject.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.busteamproject.DTO.BusStationInfo;
import com.busteamproject.R;

import java.util.List;

public class BusStationAdapter extends ArrayAdapter<BusStationInfo> {
    public BusStationAdapter(Context context, int resource, List<BusStationInfo> busList) {
        super(context, resource, busList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        BusStationInfo bus = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view, parent, false);
        }
        TextView tv = convertView.findViewById(R.id.label);
		TextView tv2 = convertView.findViewById(R.id.label2);
        tv.setText(String.format("%s (%s)", bus.getStationName(), bus.getMobileNo()));

		String distanceString = "";
		if(!bus.getDistance().isEmpty()) {
			distanceString = String.format("거리: %sm", bus.getDistance());
		}
		tv2.setText(distanceString);
        return convertView;
    }
}
