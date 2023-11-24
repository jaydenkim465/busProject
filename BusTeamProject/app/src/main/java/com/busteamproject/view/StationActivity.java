package com.busteamproject.view;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import com.busteamproject.R;

import java.util.ArrayList;

public class StationActivity extends AppCompatActivity {

        //ArrayList busList로 생성
        public static ArrayList<String> busList=new ArrayList<String>();
        ListView listView;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //만들어진 class명들 값넣어줄자리 생성
            //ctivity_main을 레이아웃에 넣어주겠다.
            setContentView(R.layout.activity_station_search);

            setUpDate();
            setUpList();

            //이벤트등록
            searchBus();
            setUpOnClickListener();
        }
        private void searchBus(){
            //bus_search_view와 일치하는 뷰를 가져와 searchView에 저장하겠다
            SearchView searchView=findViewById(R.id.bus_search_view);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                //
                @Override//검색버튼을 누르는곳이지만 밑에 바로 반영되는게 있어서 false처리됨= 예전은 TextWatcher사용
                public boolean onQueryTextSubmit(String query) {
                    ArrayList<String> filterBus = new ArrayList<String>();
                    for(int i =0; i< busList.size(); i++){
                        String bus =busList.get(i);
                        if(bus.toLowerCase().contains(query.toLowerCase())) {
                            filterBus.add(bus);
                        }
                    }
                    //resource = R.layout.list_view
                    //불러온값 넣어주기 listview에 출력
//                    busAdapter adapter=new busAdapter(getApplicationContext(),0,filterBus);
//                    listView.setAdapter(adapter);

                    return false;
                }

                @Override// 글자가 변경될때 검색되는부분
                public boolean onQueryTextChange(String newText) {
                ArrayList<String> filterBus = new ArrayList<String>();
//                for(int i =0; i< busList.size(); i++){
//                    String bus =busList.get(i);
//                    if(bus.toLowerCase().contains(newText.toLowerCase())) {
//                        filterBus.add(bus);
//                    }
//                }
//                //resource = R.layout.list_view
//                //불러온값 넣어주기 listview에 출력
//                busAdapter adapter=new busAdapter(getApplicationContext(),0,filterBus);
//                listView.setAdapter(adapter);

                    return false;
                }
            });

        }
        //
        private void setUpDate(){
            busList.add("4850");
            busList.add("480");
            busList.add("49");
            busList.add("480");
            busList.add("48");
            busList.add("850");
            busList.add("50");
            busList.add("477");
            busList.add("60");
            busList.add("63");
            busList.add("64");
            busList.add("68");
            busList.add("4880");
            busList.add("고양이");
            busList.add("신흥동");
            busList.add("수진동");
        }
        //list로 저장된 목록출력
        private void setUpList(){
            //seatch_view와 일치하는거 가져와서 listview 저장
            listView=findViewById(R.id.search_view);
            //값을 adapter로 가져와서 출력하는부분
            busAdapter adapter=new busAdapter(getApplicationContext(),0,busList);
            listView.setAdapter(adapter);

        }
        //클릭시 발생하는값
        private void setUpOnClickListener(){
            int listViewId = listView.getId();
            //나열된 목록중 하나를 클릭할때 저장해서 new값
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                //목록중하나 view <이목록 묶는거 parent<listview  @@position=>몇번재값인지
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int parentId = parent.getId();
                    //position값을 String값으로 저장 selectBus
                    String selectBus=(String) listView.getItemAtPosition(position);


                }
            });
        }
    }

