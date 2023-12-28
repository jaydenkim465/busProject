package com.busteamproject.DTO;


//http://apis.data.go.kr/6410000/busrouteservice/getBusRouteInfoItem
// ?serviceKey=&routeId=200000085
public class BusDTO {
    private String routeId;
    private String routeName;
    private String regionName; //지역 이름
    private String districtCd; //지역코드
    private String peekAlloc; // 평일 최소 배차시간
    private String nPeekAlloc; //평일 최대 배차시간
    private String endStationName; //종점정류소명

    public BusDTO() {
    }

    public BusDTO(String routeId, String routeName) {
        this.routeId = routeId;
        this.routeName = routeName;
    }

    public BusDTO(String routeId, String routeName, String regionName, String districtCd, String peekAlloc, String nPeekAlloc, String endStationName) {
        this.routeId = routeId;
        this.routeName = routeName;
        this.regionName = regionName;
        this.districtCd = districtCd;
        this.peekAlloc = peekAlloc;
        this.nPeekAlloc = nPeekAlloc;
        this.endStationName = endStationName;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getDistrictCd() {
        return districtCd;
    }

    public void setDistrictCd(String districtCd) {
        this.districtCd = districtCd;
    }

    public String getPeekAlloc() {
        return peekAlloc;
    }

    public void setPeekAlloc(String peekAlloc) {
        this.peekAlloc = peekAlloc;
    }

    public String getNPeekAlloc() {
        return nPeekAlloc;
    }

    public void setNPeekAlloc(String npeekAlloc) {
        this.nPeekAlloc = npeekAlloc;
    }

    public String getEndStationName() {
        return endStationName;
    }

    public void setEndStationName(String endStationName) {
        this.endStationName = endStationName;
    }

	@Override
    public String toString() {
        return "BusDTO{" +
                "routeId='" + routeId + '\'' +
                ", routeName='" + routeName + '\'' +
                ", regionName='" + regionName + '\'' +
                ", districtCd='" + districtCd + '\'' +
                ", peekAlloc='" + peekAlloc + '\'' +
                ", npeekAlloc='" + nPeekAlloc + '\'' +
                ", endStationName='" + endStationName + '\'' +
                '}';
    }
}