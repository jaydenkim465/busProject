package com.busteamproject.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.busteamproject.DTO.BusStationSearchList;
import com.busteamproject.R;

import java.util.List;

public class BusListAdapter extends ArrayAdapter<BusStationSearchList> {
    public BusListAdapter(Context context, int resource, List<BusStationSearchList> busList) {
        super(context, resource, busList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        BusStationSearchList bus = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view, parent, false);
        }
        TextView tv = convertView.findViewById(R.id.label);
        tv.setText(String.format("%s", bus.getInfo()));
        return convertView;
    }
}
