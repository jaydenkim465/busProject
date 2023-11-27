package com.busteamproject.util;

import android.util.Log;
import com.busteamproject.DTO.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.json.android.JSONArray;
import org.json.android.JSONException;
import org.json.android.JSONObject;
import org.json.android.XML;

import java.lang.reflect.Type;
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

	public static List<BusStationInfo> parseBusStationSearchResult(String result) {
		return parseBusStationSearchResult(convertXmlToJson(result));
	}

	public static List<BusStationInfo> parseBusStationSearchResult(JSONObject jsonResult) {
		List<BusStationInfo> busStationSearchResultList = new ArrayList<>();
		try {
			if (jsonResult != null) {
				JSONObject response = jsonResult.getJSONObject("response");
				JSONObject msgBody = response.getJSONObject("msgBody");

				JSONArray busStationList;

				if (msgBody.has("busStationList")) {
					busStationList = msgBody.getJSONArray("busStationList");
				} else if (msgBody.has("busStationAroundList")) {
					busStationList = msgBody.getJSONArray("busStationAroundList");
				} else {
					busStationList = new JSONArray();
				}

				for (int i = 0; i < busStationList.length(); i++) {
					JSONObject station = busStationList.getJSONObject(i);
					int districtCd = station.getInt("districtCd");
					String regionName = station.getString("regionName");
					String stationId = String.valueOf(station.getInt("stationId"));
					String stationName = station.getString("stationName");
					double x = station.getDouble("x");
					double y = station.getDouble("y");

					String distance = "";
					if(station.has("distance")) {
						distance = String.valueOf(station.getInt("distance"));
					}

					BusStationInfo busStation = new BusStationInfo(districtCd, regionName, stationId, stationName, x, y, distance);
					busStationSearchResultList.add(busStation);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return busStationSearchResultList;
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

	public static List<AddressInfoDTO> parseAddressDocument(JSONObject jsonData) {
		try {
			List<AddressInfoDTO> addressInfoDTOList = new ArrayList<>();
			JSONArray ja = jsonData.getJSONArray("documents");
			for (int i = 0; i < ja.length(); i++) {
				JSONObject tmp = (JSONObject) ja.get(i);
				addressInfoDTOList.add(new AddressInfoDTO(
						tmp.get("address_name").toString()
						, tmp.get("address_type").toString()
						, tmp.get("x").toString()
						, tmp.get("y").toString()
						, (JSONObject) tmp.get("address")
				));
				Log.d("document", addressInfoDTOList.get(i).toString());
			}
			return addressInfoDTOList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<BusStationSearchList> parseBusStationArrivalInfo(String jsonResult) {
		return parseBusStationArrivalInfo(convertXmlToJson(jsonResult));
	}

	public static List<BusStationSearchList> parseBusStationArrivalInfo(JSONObject jsonResult) {
		List<BusStationSearchList> busStationList = new ArrayList<>();

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
					String locationNo2 = String.valueOf(station.getInt("locationNo2"));
					String predictTime2 = String.valueOf(station.getInt("predictTime2"));
					String staOrder = String.valueOf(station.getInt("staOrder"));
					String flag = station.getString("flag");
					String plateNo1 = station.getString("plateNo1");
					String plateNo2 = station.getString("plateNo2");

					BusStationSearchList busStation = new BusStationSearchList(stationId, routeId, locationNo1, predictTime1,
							plateNo1, locationNo2, predictTime2, plateNo2, staOrder, flag);
					busStationList.add(busStation);
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return busStationList;
	}


	public static BusDTO parseBusInfo(String xmlStr) {
		return parseBusInfo(convertXmlToJson(xmlStr));
	}

	public static BusDTO parseBusInfo(JSONObject jsonResult) {
		BusDTO busDTO = new BusDTO();
		try {
			if (jsonResult != null) {
				JSONObject response = jsonResult.getJSONObject("response");
				JSONObject msgBody = response.getJSONObject("msgBody");
				JSONObject busRouteInfoItem = msgBody.getJSONObject("busRouteInfoItem");

				String routeId = String.valueOf(busRouteInfoItem.getInt("routeId"));
				String routeName = String.valueOf(busRouteInfoItem.getInt("routeName"));
				String regionName = busRouteInfoItem.getString("regionName");
				String districtCd = String.valueOf(busRouteInfoItem.getInt("districtCd"));
				String peekAlloc = String.valueOf(busRouteInfoItem.getInt("peekAlloc"));
				String nPeekAlloc = String.valueOf(busRouteInfoItem.getInt("nPeekAlloc"));
				String endStationName = busRouteInfoItem.getString("endStationName");

				busDTO.setRouteId(routeId); //노선아이디
				busDTO.setRouteName(routeName); //버스이름
				busDTO.setRegionName(regionName); //지역이름
				busDTO.setDistrictCd(districtCd); //지역코드
				busDTO.setPeekAlloc(peekAlloc); //평일최소배차시간
				busDTO.setNPeekAlloc(nPeekAlloc); //평일최대배차시간
				busDTO.setEndStationName(endStationName); //종점정류소이름
			}
			return busDTO;

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return busDTO;
	}
}