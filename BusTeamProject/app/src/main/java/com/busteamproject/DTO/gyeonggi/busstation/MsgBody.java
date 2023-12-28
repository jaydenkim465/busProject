package com.busteamproject.DTO.gyeonggi.busstation;

import java.util.List;

public class MsgBody {
	private List<BusStationInfo> busStationList;
	private List<BusStationInfo> busStationAroundList;

	public List<BusStationInfo> getBusStationList() {
		return busStationList;
	}

	public List<BusStationInfo> getBusStationAroundList() {
		return busStationAroundList;
	}
}
