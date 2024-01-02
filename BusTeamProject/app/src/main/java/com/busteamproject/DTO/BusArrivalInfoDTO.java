package com.busteamproject.DTO;

// 참고 Link : https://www.data.go.kr/data/15080346/openapi.do

import com.busteamproject.DTO.gyeonggi.busroute.BusRouteInfo;

public class BusArrivalInfoDTO {
	private boolean isBookMark = false;
	private String stationId; //정류장 아이디
	private String stationName;
	private String stationNo;
	private String stationX;
	private String stationY;
	private String routeId; //노선 아이디
	private String locationNo1; //첫번째 차량 위치 정보
	private String predictTime1; //첫번째 차량 도착 예정 시간
	private String plateNo1; //첫번째 차량 차량번호
	private String locationNo2; //두번째 차량 위치 정보
	private String predictTime2; //두번째 차량 도착 예정 시간
	private String plateNo2; //두번째 차량 차량번호
	private String staOrder; //정류소 순번
	private String flag; //상태구분

	private BusRouteInfo busRouteInfo;

	public BusArrivalInfoDTO(String stationId, String stationName) {
		this.stationId = stationId;
		this.stationName = stationName;
	}

	// 생성자, getter 및 setter 메서드 추가
	public BusArrivalInfoDTO(String stationId, String routeId, String locationNo1, String predictTime1, String plateNo1,
	                         String locationNo2, String predictTime2, String plateNo2, String staOrder, String flag) {
		this.stationId = stationId;
		this.routeId = routeId;
		this.locationNo1 = locationNo1;
		this.predictTime1 = predictTime1;
		this.plateNo1 = plateNo1;
		this.locationNo2 = locationNo2;
		this.predictTime2 = predictTime2;
		this.plateNo2 = plateNo2;
		this.staOrder = staOrder;
		this.flag = flag;
	}

	public String getInfo() {
		String result = String.format("정류장 아이디: %s, 노선 아이디: %s, 차량 위치 정보: %s , 도착 예정 시간 : %s, 차량번호 : %s, 두번째 차량 위치 정보 : %s, 도착 예정 시간 : %s, 차량번호 : %s, 정류소 순번: %s, 상태 : %s \n",
				this.getStationId(), this.getRouteId(), this.getLocationNo1(), this.getPredictTime1(),
				this.getPlateNo1(), this.getLocationNo2(), this.getPredictTime2(), this.getPlateNo2(),
				this.getStaOrder(), this.getFlag());
		return result;
	}

	public String getStationId() {
		return stationId;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
	}


	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public String getLocationNo1() {
		return locationNo1;
	}

	public void setLocationNo1(String locationNo1) {
		this.locationNo1 = locationNo1;
	}

	public String getPredictTime1() {
		return predictTime1;
	}

	public void setPredictTime1(String predictTime1) {
		this.predictTime1 = predictTime1;
	}

	public String getPlateNo1() {
		return plateNo1;
	}

	public void setPlateNo1(String plateNo1) {
		this.plateNo1 = plateNo1;
	}

	public String getLocationNo2() {
		return locationNo2;
	}

	public void setLocationNo2(String locationNo2) {
		this.locationNo2 = locationNo2;
	}

	public String getPredictTime2() {
		return predictTime2;
	}

	public void setPredictTime2(String predictTime2) {
		this.predictTime2 = predictTime2;
	}

	public String getPlateNo2() {
		return plateNo2;
	}

	public void setPlateNo2(String plateNo2) {
		this.plateNo2 = plateNo2;
	}

	public String getStaOrder() {
		return staOrder;
	}

	public void setStaOrder(String staOrder) {
		this.staOrder = staOrder;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public BusRouteInfo getBusRouteInfo() {
		return busRouteInfo;
	}

	public void setBusRouteInfo(BusRouteInfo busRouteInfo) {
		this.busRouteInfo = busRouteInfo;
	}

	public boolean isBookMark() {
		return isBookMark;
	}

	public void setBookMark(boolean bookMark) {
		isBookMark = bookMark;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getStationNo() {
		return stationNo;
	}

	public void setStationNo(String stationNo) {
		this.stationNo = stationNo;
	}

	public String getStationX() {
		return stationX;
	}

	public void setStationX(String stationX) {
		this.stationX = stationX;
	}

	public String getStationY() {
		return stationY;
	}

	public void setStationY(String stationY) {
		this.stationY = stationY;
	}
}