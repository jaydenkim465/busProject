package com.busteamproject.DTO.gyeonggi.busroute;

// 참고 Link : https://www.data.go.kr/data/15080662/openapi.do
/*
노선 유형 코드
11	직행좌석형시내버스	23	일반형농어촌버스
12	좌석형시내버스	30	마을버스
13	일반형시내버스	41	고속형시외버스
14	광역급행형시내버스	42	좌석형시외버스
15	따복형 시내버스	43	일반형시외버스
16	경기순환버스	51	리무진공항버스
21	직행좌석형농어촌버스	52	좌석형공항버스
22	좌석형농어촌버스	53	일반형공항버스

노선운영ID
01	가평군	12	성남시	23	용인시
02	고양시	13	수원시	24	의왕시
03	과천시	14	시흥시	25	의정부시
04	광명시	15	안산시	26	이천시
05	광주시	16	안성시	27	파주시
06	구리시	17	안양시	28	평택시
07	군포시	18	양주시	29	포천시
08	김포시	19	양평군	30	하남시
09	남양주시	20	여주군	31	화성시
10	동두천시	21	연천군	32	서울특별시
11	부천시	22	오산시	33	인천광역시
 */

public class BusRouteInfo {
	private String routeId; // 노선ID
	private String routeName;   // 노선번호
	private String routeTypeCd; // 노선 유형 코드
	private String routeTypeName;   // 노선유형명
	private String startStationId;  // 기점정류소ID
	private String startStationName;    // 기점정류소명칭
	private String startMobileNo;   // 기점정류소 고유모바일번호. 고유모바일번호가 없는 정류소인 경우 표출되지 않음
	private String endStationId;    // 종점
	private String endStationName;  // 종점 정류소명
	private String endMobileNo; //종점

	private String regionName; // 노선 운행지역
	private String districtCd; // 노선 관할지역코드

	private String upFirstTime;
	private String upLastTime;
	private String downFirstTime;
	private String downLastTime;

	private String peekAlloc; // 평일 최소 배차시간
	private String nPeekAlloc; //평일 최대 배차시간

	private String companyId;
	private String companyName;
	private String companyTel;

	public String getRouteId() {
		return routeId;
	}

	public String getRouteName() {
		return routeName;
	}

	public String getRouteTypeCd() {
		return routeTypeCd;
	}

	public String getRouteTypeName() {
		return routeTypeName;
	}

	public String getStartStationId() {
		return startStationId;
	}

	public String getStartStationName() {
		return startStationName;
	}

	public String getStartMobileNo() {
		return startMobileNo;
	}

	public String getEndStationId() {
		return endStationId;
	}

	public String getEndStationName() {
		return endStationName;
	}

	public String getEndMobileNo() {
		return endMobileNo;
	}

	public String getRegionName() {
		return regionName;
	}

	public String getDistrictCd() {
		return districtCd;
	}

	public String getUpFirstTime() {
		return upFirstTime;
	}

	public String getUpLastTime() {
		return upLastTime;
	}

	public String getDownFirstTime() {
		return downFirstTime;
	}

	public String getDownLastTime() {
		return downLastTime;
	}

	public String getPeekAlloc() {
		return peekAlloc;
	}

	public String getnPeekAlloc() {
		return nPeekAlloc;
	}

	public String getCompanyId() {
		return companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getCompanyTel() {
		return companyTel;
	}
}