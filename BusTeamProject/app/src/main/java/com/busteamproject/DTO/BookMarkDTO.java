package com.busteamproject.DTO;

public class BookMarkDTO implements Comparable<BookMarkDTO> {
	private int index;
	private String routeId;
	private String stationId;
	private String stationName;
	private String stationNo;
	private String stationX;
	private String stationY;
	private String type;

	public BookMarkDTO(int index, String routId, String stationId, String type, String stationName, String stationNo, String stationX, String stationY) {
		this.index = index;
		this.routeId = routId;
		this.stationId = stationId;
		this.type = type;
		this.stationName = stationName;
		this.stationNo = stationNo;
		this.stationX = stationX;
		this.stationY = stationY;
	}

	public int getIndex() {
		return index;
	}

	public String getRouteId() {
		return routeId;
	}

	public String getType() {
		return type;
	}

	public String getStationId() {
		return stationId;
	}

	public String getStationName() {
		return stationName;
	}

	public String getStationNo() {
		return stationNo;
	}

	public String getStationY() {
		return stationY;
	}

	public String getStationX() {
		return stationX;
	}

	public String getSaveData() {
		return String.format("%d|%s|%s|%s|%s|%s|%s|%s", index, routeId, stationId, type, stationName, stationNo, stationX, stationY);
	}

	@Override
	public int compareTo(BookMarkDTO input) {
		if(input.getIndex() < index) {
			return 1;
		} else if(index < input.getIndex()) {
			return -1;
		} else {
			return 0;
		}
	}
}
