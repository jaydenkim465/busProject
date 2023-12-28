package com.busteamproject.DTO.gyeonggi.busstation;

public class MsgHeader {
	private String queryTime;
	private int resultCode;
	private String resultMessage;

	public String getQueryTime() {
		return queryTime;
	}

	public int getResultCode() {
		return resultCode;
	}

	public String getResultMessage() {
		return resultMessage;
	}
}
