package com.busteamproject.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.busteamproject.DTO.address.Documents;
import com.busteamproject.R;

import java.util.List;

public class AddressAdapter extends ArrayAdapter<Documents> {

    public AddressAdapter(Context context, int resource, List<Documents> addressList) {
        super(context, resource, addressList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		Documents address = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.label);
		TextView textView2 = convertView.findViewById(R.id.label2);

		String mainAddress = "";
		String subAddress = "";
		if(address.getAddressType().contains("REGION_ADDR")) {
			mainAddress = address.getAddressName();
			subAddress = address.getRoadAddress().getAddressName();
		} else if(address.getAddressType().contains("ROAD_ADDR")) {
			mainAddress = address.getAddressName();
			subAddress = address.getAddress().getAddressName();
		} else {
			mainAddress = address.getAddressName();
			subAddress = "";
		}
        textView.setText(mainAddress);
		textView2.setText(subAddress);
        return convertView;
    }
}
