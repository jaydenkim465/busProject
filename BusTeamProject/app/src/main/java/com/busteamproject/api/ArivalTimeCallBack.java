package com.busteamproject.api;

import com.busteamproject.DTO.BusLocationDTO;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ArivalTimeCallBack implements CallBack{
    public void getArivalTimeOfBus(String cityCode, String nodeId){
        ApiHelper api=ApiHelper.getInstance();
        String requestUrl="http://apis.data.go.kr/1613000/ArvlInfoInqireService/getSttnAcctoArvlPrearngeInfoList";
        String key="0y0iJ9SX92FFc%2FLnxp9IAOfbJvBpcvjdwbkJY6cxdJupuPuryYpGB%2B37hnA5%2Fn21dOdPvcwW2%2Bsj7i%2F6A8Y7iQ%3D%3D";

        api.get(requestUrl
                ,"?serviceKey="+key+"&cityCode="+cityCode+ "&nodeId="+nodeId+"&_type=json"
                , this);
    }
    @Override
    public void ApiResult(JSONObject result) {
        if(result.length()==0)//1. NULL값 처리
            return;
        //2. JSON Parse 준비
        JsonParser parser=new JsonParser();
        JsonObject jsonObject=(JsonObject)parser.parse(result.toString());//JSONObject를 JsonObject로
        JsonArray jsonArray = null;
        try{//3. JSON parse 시작
            jsonObject= (JsonObject) jsonObject.get("response");
            jsonObject=(JsonObject) jsonObject.get("body");
            jsonObject=(JsonObject) jsonObject.get("items");
            jsonArray=jsonObject.get("item").getAsJsonArray();

        } catch (Exception e){//4. 예외 처리(키값이 json에 없을 경우==get에 실패한경우)
            e.printStackTrace();
            return;
        }
        if(jsonArray==null){//5.null값 처리(실제 아무런 버스 정보가 없는 경우)
            return;
        }
        //6. 결과값 jsonArray를 DTO로 변환
        Type typeList=new TypeToken<ArrayList<BusLocationDTO>>(){}.getType();
        List<BusLocationDTO> dtoList=new Gson().fromJson(jsonArray.toString(), typeList);
        //7. 변환된 결과 확인
        for(BusLocationDTO dto : dtoList){
            System.out.println(dto.getArrtime());
        }
    }
}
