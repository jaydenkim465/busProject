package com.busteamproject.DTO.gyeonggi.busroute;

/*
결과 코드 및 메시지
-  0: 정상적으로 처리되었습니다.
-  1: 시스템 에러가 발생하였습니다.
-  2: 필수 요청 Parameter 가 존재하지 않습니다.
-  3: 필수 요청 Parameter 가 잘못되었습니다.
-  4: 결과가 존재하지 않습니다.
-  5: 필수 요청 Parameter(인증키) 가 존재하지 않습니다.
-  6: 등록되지 않은 키입니다.
-  7: 사용할 수 없는(등록은 되었으나, 일시적으로 사용 중지된) 키입니다.
-  8: 요청 제한을 초과하였습니다.
-  20: 잘못된 위치로 요청하였습니다. 위경도 좌표값이 정확한지 확인하십시오.
-  21: 노선번호는 1자리 이상 입력하세요.
-  22: 정류소명/번호는 1자리 이상 입력하세요.
-  23: 버스 도착 정보가 존재하지 않습니다.
-  31: 존재하지 않는 출발 정류소 아이디(ID)/번호입니다.
-  32: 존재하지 않는 도착 정류소 아이디(ID)/번호입니다.
-  99: API 서비스 준비중입니다.
 */

public class Response {
	private String comMsgHeader;
	private MsgHeader msgHeader;
	private MsgBody msgBody;

	public String getComMsgHeader() {
		return comMsgHeader;
	}

	public MsgHeader getMsgHeader() {
		return msgHeader;
	}

	public MsgBody getMsgBody() {
		return msgBody;
	}
}
