package com.busteamproject.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.busteamproject.DTO.BusLocationDTO;
import com.busteamproject.R;

import java.util.List;

public class BusListAdapter extends ArrayAdapter<BusLocationDTO> {
    public BusListAdapter(Context context, int resource, List<BusLocationDTO> busList) {
        super(context, resource, busList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        BusLocationDTO bus = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view, parent, false);
        }
        TextView tv = convertView.findViewById(R.id.label);
        tv.setText(String.format("%s %s", bus.getRouteno(), bus.getArrtime()));
        return convertView;
    }
}
