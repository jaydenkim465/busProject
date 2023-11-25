package com.busteamproject.Service;

import android.util.Log;
import com.busteamproject.DTO.AddressInfoDTO;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddressService {

    public List<AddressInfoDTO> parseDocument(JSONObject jsonData) {
        try{
            List<AddressInfoDTO> addressInfoDTOList = new ArrayList<>();
            JSONArray ja = jsonData.getJSONArray("documents");
            for(int i = 0 ;i<ja.length();i++){
                JSONObject tmp = (JSONObject)ja.get(i);
                addressInfoDTOList.add(new AddressInfoDTO(
                        tmp.get("address_name").toString()
                        ,tmp.get("address_type").toString()
                        ,tmp.get("x").toString()
                        ,tmp.get("y").toString()
                        ,(JSONObject) tmp.get("address")
                        ));
                Log.d("document", addressInfoDTOList.get(i).toString());
            }
            return addressInfoDTOList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
