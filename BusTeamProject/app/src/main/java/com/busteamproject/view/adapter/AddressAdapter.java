package com.busteamproject.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.busteamproject.DTO.AddressInfoDTO;
import com.busteamproject.R;

import java.util.List;

public class AddressAdapter extends ArrayAdapter<AddressInfoDTO> {

    public AddressAdapter(Context context, int resource, List<AddressInfoDTO> addressList) {
        super(context, resource, addressList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        AddressInfoDTO address = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.label);
		TextView textView2 = convertView.findViewById(R.id.label2);

		String mainAddress = "";
		String subAddress = "";
		if(address.getAddress_type().contains("REGION")) {
			mainAddress = address.getAddress_name();
			subAddress = address.getRoadAddressDetailDTO().getAddress_name();
		} else {
			mainAddress = address.getAddress_name();
			subAddress = address.getAddressDetailDTO().getAddress_name();
		}
        textView.setText(mainAddress);
		textView2.setText(subAddress);
        return convertView;
    }
}
