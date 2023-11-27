package com.busteamproject.api;

import com.busteamproject.util.Util;
import org.json.android.JSONObject;

public class NearStationAPI implements JSONCallBack{

    public void getNearStation(){
        double x=UserGPS.getX(), y=UserGPS.getY();
        ApiHelper api=ApiHelper.getInstance();
        String serviceUrl="http://apis.data.go.kr/6410000/busstationservice";
        String key="0y0iJ9SX92FFc%2FLnxp9IAOfbJvBpcvjdwbkJY6cxdJupuPuryYpGB%2B37hnA5%2Fn21dOdPvcwW2%2Bsj7i%2F6A8Y7iQ%3D%3D";

        api.govJsonGet(serviceUrl
                , "?serviceKey="+key+"&x="+String.valueOf(x)+"&y="+String.valueOf(y)
                , this);
    }

    @Override
    public void ApiResult(JSONObject result) {
        Util.parseBusStationSearchResult(result.toString());
    }
}
