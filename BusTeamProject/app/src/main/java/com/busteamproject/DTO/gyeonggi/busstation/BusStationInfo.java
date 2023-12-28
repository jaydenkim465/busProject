package com.busteamproject.DTO.gyeonggi.busstation;

public class BusStationInfo {
	//노선 관할지역코드 (1:서울, 2:경기, 3:인천)
	private int districtCd;
	//정류소 위치 지역명
	private String regionName;

	private double x;
	private double y;
	private String stationName;

	//중앙차로 여부 (N:일반, Y:중앙차로) 옵션 값임
	private String centerYn;

	//정류소 고유모바일번호. 고유모바일번호가 없는 정류소인 경우 표출되지 않을 수 있음.
	private int mobileNo;

	private int stationId;
	private String distance;

	public String getStationId() {
		return String.valueOf(stationId);
	}

	public String getRegionName() {
		return regionName;
	}

	public int getDistrictCd() {
		return districtCd;
	}

	public String getStationName() {
		return stationName;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public String getDistance() {
		return distance;
	}

	public String getMobileNo() {
		return String.valueOf(mobileNo);
	}

	public String getInfo() {
		String result = String.format("관할지역코드: %d, 지역명 : %s, 정류장 아이디: %s, 정류장 이름: %s, 좌표(%f, %f)",
				this.getDistrictCd(),
				this.getRegionName(),
				this.getStationId(),
				this.getStationName(),
				this.getX(),
				this.getY());
		return result;
	}
}
