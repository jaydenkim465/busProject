package com.busteamproject.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.busteamproject.R;

import java.util.List;

public class AddressAdapter extends ArrayAdapter<String> {

    public AddressAdapter(Context context, int resource, List<String> addressList){
        super(context,resource,addressList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String address=getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view , parent, false);
        }

        TextView textView=convertView.findViewById(R.id.label);
        textView.setText(address);

        return convertView;
    }
}
