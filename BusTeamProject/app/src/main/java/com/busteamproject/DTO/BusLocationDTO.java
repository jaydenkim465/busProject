package com.busteamproject.DTO;

public class BusLocationDTO {
    private String resultCode, resultMsg, numOfRows, pageNo, totalCount, nodeid, nodenm, routeid, routeno, routetp, arrprevstationcnt, vehicletp, arrtime;

    public BusLocationDTO(String resultCode, String resultMsg, String numOfRows, String pageNo, String totalCount, String nodeid, String nodenm, String routeid, String routeno, String routetp, String arrprevstationcnt, String vehicletp, String arrtime) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
        this.numOfRows = numOfRows;
        this.pageNo = pageNo;
        this.totalCount = totalCount;
        this.nodeid = nodeid;
        this.nodenm = nodenm;
        this.routeid = routeid;
        this.routeno = routeno;
        this.routetp = routetp;
        this.arrprevstationcnt = arrprevstationcnt;
        this.vehicletp = vehicletp;
        this.arrtime = arrtime;
    }

    public String getArrtime() {
        return arrtime;
    }
}
