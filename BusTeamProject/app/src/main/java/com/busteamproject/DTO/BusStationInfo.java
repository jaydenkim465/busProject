package com.busteamproject.DTO;


public class BusStationInfo {
	/*
    "https://apis.data.go.kr/6410000/busstationservice/getBusStationList",
    "?serviceKey=ckxCSTx4wV%2FMrdL6AGpQKRuF1AoWEK4E74NmLmE2s0u%2BoETryRg8%2BAwD1S9wDGpoypKr%2BHT8JGRYjJpTRPGvVg%3D%3D" +
    "&keyword="
     */
	private int districtCd; //노선 관할지역코드 (1:서울, 2:경기, 3:인천)
	private String regionName; //정류소 위치 지역명
//        private char centerYn; //중앙차로 여부 (N:일반, Y:중앙차로) 옵션값임.
//        private int mobileNo; //정류소 고유모바일번호. 고유모바일번호가 없는 정류소인 경우 표출되지 않을수 있음.

	private String stationId;
	private String stationName;
	private double x;
	private double y;
	private String distance;

	// 생성자, getter 및 setter 메서드 추가
	public BusStationInfo(int districtCd, String regionName, String stationId, String stationName, double x, double y, String distance) {
		this.districtCd = districtCd; //노선 관할지역코드
		this.regionName = regionName; //정류소 위치 지역명
		this.stationId = stationId;
		this.stationName = stationName;
		this.x = x;
		this.y = y;
		this.distance = distance;
	}

	public String getInfo() {
		String result = String.format("관할지역코드: %d, 지역명 : %s, 정류장 아이디: %s, 정류장 이름: %s, 좌표(%f, %f) \n", this.getDistrictCd(), this.getRegionName(), this.getStationId(), this.getStationName(), this.getX(), this.getY());
		return result;
	}

	public String getStationId() {
		return stationId;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
	}


	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public int getDistrictCd() {
		return districtCd;
	}

	public void setDistrictCd(int districtCd) {
		this.districtCd = districtCd;
	}


	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public String getDistance() {
		return distance;
	}
}

