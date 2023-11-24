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

//String-> 이름
//어뎁터값을 String
public class busAdapter extends ArrayAdapter<String> {
    //busAdapter의 값을 context=>main에 getApplicationContext(),를 자료 이어주는곳
    //  resource=R.id와 같음 밑에서 써서main에서는 0으로 넣어줌   String 리스트 저장
    public busAdapter(Context context, int resource, List<String> busList){
        super(context,resource,busList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //값        busAdapter값을 (Stirng)getItem에 넣어 bus저장
        String bus=getItem(position);
        //convertView 가 null일때 아닐때 값
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view, parent, false);
        }
        //label 에 담긴값 tv에 저장
        TextView tv=convertView.findViewById(R.id.label);
//추가정보 tv의 내용중 bus의 정보
        tv.setText(bus);
//

        return convertView;
    }
}
