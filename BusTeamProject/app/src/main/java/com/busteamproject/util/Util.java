package com.busteamproject.util;

import com.busteamproject.DTO.BusStationInfo;
import org.json.android.JSONArray;
import org.json.android.JSONException;
import org.json.android.JSONObject;
import org.json.android.XML;

import java.util.ArrayList;
import java.util.List;

public class Util {

    public static JSONObject convertXmlToJson(String xmlStr) {

        JSONObject jsonResult = null;

        try {
            jsonResult = XML.toJSONObject(xmlStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonResult;
    }
    public static List<BusStationInfo> parseBusStationAroundResult(String result) {
        List<BusStationInfo> busStationList = new ArrayList<>();

        try {
            JSONObject jsonResult = new JSONObject(result);
            JSONObject msgBody = jsonResult.getJSONObject("msgBody");
            JSONArray busStationAroundList = msgBody.getJSONArray("busStationList");

            for (int i = 0; i < busStationAroundList.length(); i++) {
                JSONObject station = busStationAroundList.getJSONObject(i);
                int districtCd = station.getInt("districtCd");
                String regionName = station.getString("regionName");
                String stationId = station.getString("stationId");
                String stationName = station.getString("stationName");
                double x = station.getDouble("x");
                double y = station.getDouble("y");

                BusStationInfo busStation = new BusStationInfo(districtCd, regionName, stationId, stationName, x, y);
                busStationList.add(busStation);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return busStationList;
    }

    public static List<BusStationInfo> parseBusStationAroundResult(JSONObject jsonResult) {
        List<BusStationInfo> busStationList = new ArrayList<>();

        try {
            if (jsonResult != null) {
                JSONObject response=jsonResult.getJSONObject("response");
                JSONObject msgBody = response.getJSONObject("msgBody");
                JSONArray busStationAroundList = msgBody.getJSONArray("busStationList");

                for (int i = 0; i < busStationAroundList.length(); i++) {
                    JSONObject station = busStationAroundList.getJSONObject(i);
                    int districtCd = station.getInt("districtCd");
                    String regionName = station.getString("regionName");
                    String stationId = String.valueOf(station.getInt("stationId"));
                    String stationName = station.getString("stationName");
                    double x = station.getDouble("x");
                    double y = station.getDouble("y");


                    BusStationInfo busStation = new BusStationInfo(districtCd,regionName, stationId, stationName, x, y);
                    busStationList.add(busStation);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return busStationList;
    }

}
