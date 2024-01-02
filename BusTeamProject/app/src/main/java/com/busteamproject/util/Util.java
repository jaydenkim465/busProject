package com.busteamproject.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import com.busteamproject.DTO.BusArrivalInfoDTO;
import com.busteamproject.DTO.BusLocationDTO;
import com.busteamproject.DTO.WalkingTimeDTO;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.android.JSONArray;
import org.json.android.JSONException;
import org.json.android.JSONObject;
import org.json.android.XML;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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

	public static List<BusLocationDTO> parseBusLocationResult(String input) {
		return parseBusLocationResult(convertXmlToJson(input));
	}

	public static List<BusLocationDTO> parseBusLocationResult(JSONObject input) {
		List<BusLocationDTO> result = new ArrayList<>();
		if (input.length() == 0)//1. NULL값 처리
			return result;
		//2. JSON Parse 준비
		JsonParser parser = new JsonParser();
		JsonObject jsonObject = (JsonObject) parser.parse(input.toString());//JSONObject를 JsonObject로
		JsonArray jsonArray = null;
		try {//3. JSON parse 시작
			jsonObject = (JsonObject) jsonObject.get("response");
			jsonObject = (JsonObject) jsonObject.get("body");
			jsonObject = (JsonObject) jsonObject.get("items");
			jsonArray = jsonObject.get("item").getAsJsonArray();

		} catch (Exception e) {//4. 예외 처리(키값이 json에 없을 경우==get에 실패한경우)
			e.printStackTrace();
			return result;
		}
		if (jsonArray == null) {//5.null값 처리(실제 아무런 버스 정보가 없는 경우)
			return result;
		}
		//6. 결과값 jsonArray를 DTO로 변환
		Type typeList = new TypeToken<ArrayList<BusLocationDTO>>() {
		}.getType();
		return new Gson().fromJson(jsonArray.toString(), typeList);
	}

	public static List<BusArrivalInfoDTO> parseBusStationArrivalInfo(String jsonResult) {
		return parseBusStationArrivalInfo(convertXmlToJson(jsonResult));
	}

	public static List<BusArrivalInfoDTO> parseBusStationArrivalInfo(String jsonResult, String stationName) {
		return parseBusStationArrivalInfo(convertXmlToJson(jsonResult), stationName);
	}

	public static List<BusArrivalInfoDTO> parseBusStationArrivalInfo(JSONObject jsonResult) {
		return parseBusStationArrivalInfo(jsonResult, "");
	}

	public static List<BusArrivalInfoDTO> parseBusStationArrivalInfo(JSONObject jsonResult, String stationName) {
		List<BusArrivalInfoDTO> busStationList = new ArrayList<>();

		try {
			if (jsonResult != null) {
				JSONObject response = jsonResult.getJSONObject("response");
				JSONObject msgBody = response.getJSONObject("msgBody");
				JSONArray busStationAroundList = msgBody.getJSONArray("busArrivalList");

				for (int i = 0; i < busStationAroundList.length(); i++) {
					JSONObject station = busStationAroundList.getJSONObject(i);
					String stationId = String.valueOf(station.getInt("stationId"));
					String routeId = String.valueOf(station.getInt("routeId"));
					String locationNo1 = String.valueOf(station.getInt("locationNo1"));
					String predictTime1 = String.valueOf(station.getInt("predictTime1"));
					String locationNo2 = "";
					String predictTime2 = "";
					try {
						locationNo2 = String.valueOf(station.getInt("locationNo2"));
						predictTime2 = String.valueOf(station.getInt("predictTime2"));
					} catch (Exception ex) {
					}
					String staOrder = String.valueOf(station.getInt("staOrder"));
					String flag = station.getString("flag");
					String plateNo1 = station.getString("plateNo1");
					String plateNo2 = station.getString("plateNo2");

					BusArrivalInfoDTO busStation = new BusArrivalInfoDTO(stationId, routeId, locationNo1, predictTime1,
							plateNo1, locationNo2, predictTime2, plateNo2, staOrder, flag);
					busStation.setStationName(stationName);
					busStationList.add(busStation);
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return busStationList;
	}

	public static WalkingTimeDTO parseWalkingTime(JSONObject jsonResult) {
		WalkingTimeDTO time = new WalkingTimeDTO();
		try {
			if (jsonResult != null) {
				JSONArray features = jsonResult.getJSONArray("features");
				JSONObject properties = features.getJSONObject(0).getJSONObject("properties");

				String totalDistance = String.valueOf(properties.getInt("totalDistance"));
				String totalTime = String.valueOf(properties.getInt("totalTime"));
				String description = properties.getString("description");

				time.setTotalDistance(totalDistance);
				time.setTotalTime(totalTime);
				time.setDescription(description);
			}
			return time;

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getApiKey(Context context, String key) {
		Resources resources = context.getResources();
		AssetManager assetManager = resources.getAssets();

		try {
			Properties properties = new Properties();

			InputStream inputStream = assetManager.open("apikey.properties");
			properties.load(inputStream);

			return properties.getProperty(key, "");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}