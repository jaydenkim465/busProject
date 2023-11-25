package com.busteamproject.Service;

import android.util.Log;
import com.busteamproject.DTO.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddressService {

    public List<Document> parseDocument(JSONObject jsonData) {
        try{
            List<Document> documentList = new ArrayList<>();
            JSONArray ja = jsonData.getJSONArray("documents");
            for(int i = 0 ;i<ja.length();i++){
                JSONObject tmp = (JSONObject)ja.get(i);
                documentList.add(new Document(
                        tmp.get("address_name").toString()
                        ,tmp.get("address_type").toString()
                        ,tmp.get("x").toString()
                        ,tmp.get("y").toString()
                        ,(JSONObject) tmp.get("address")
                        ));
                Log.d("document",documentList.get(i).toString());
            }
            return documentList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
