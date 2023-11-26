package com.busteamproject.view;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import com.busteamproject.R;

import java.util.ArrayList;

public class AddressSearchActivity extends AppCompatActivity {
//집주소찾기
        public static ArrayList<String> addressList=new ArrayList<String>();
        ListView listView;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_address_search);

            setUpDate();
            setUpList();

            searchAddress();
            setUpOnClickListener();
        }


        //주소검색하는 부분
        private void searchAddress(){
            //검색창부분
            SearchView searchView=findViewById(R.id.address_search);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    ArrayList<String> filterAddress = new ArrayList<String>();
                    for(int i=0; i< addressList.size(); i++){
                        String address = addressList.get(i);
                        if(address.toLowerCase().contains(query.toLowerCase())) {
                            filterAddress.add(address);
                        }
                    }
//
                    AddressAdapter addressAdapter= new AddressAdapter(getApplicationContext(), 0, filterAddress);
                    listView.setAdapter(addressAdapter);

                    return false;
                }
                @Override
                public boolean onQueryTextChange(String newText) {
                    ArrayList<String> filterAddress=new ArrayList<String>();
//                    for (int i = 0; i < addressList.size(); i++) {
//                        String address = addressList.get(i);
//                        if (address.toLowerCase().contains(newText.toLowerCase())) {
//                            filterAddress.add(address);
//                        }
//                    }
//                    AddressAdapter addressAdapter= new AddressAdapter(getApplicationContext(), 0, filterAddress);
//                    listView.setAdapter(addressAdapter);

                    return false;
                }
            });

        }
        //클릭시 발생이벤트 생기는곳

    private void setUpDate(){
        addressList.add("수정구 신흥동");
        addressList.add("수정구 수진동");
        addressList.add("수정구 신흥2동");
        addressList.add("수정구 신흥4동");
        addressList.add("수정구 신흥1동");
        addressList.add("수정구 흥3동");
        addressList.add("수정구 수진1동");
        addressList.add("중원구 금광동");
        addressList.add("중원구 은행동");
        addressList.add("중원구 금금동");
        addressList.add("중원구 덩덩동");
        addressList.add("중원구 동동동");
        addressList.add("중원구 신흥동");
        addressList.add("중원구 신흥동");

    }
    //리스트 저장하는곳
    private void setUpList(){
        listView = findViewById(R.id.address_search_view);

//        AddressAdapter addressAdapter= new AddressAdapter(getApplicationContext(), 0, addressList);
//        listView.setAdapter((ListAdapter) addressAdapter);
    }
    private void setUpOnClickListener(){
        int listViewId=listView.getId();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent,View view,int position, long id) {
                int parentId = parent.getId();
                String selectAddress=(String) listView.getItemAtPosition(position);

            }
        });

    }
}
