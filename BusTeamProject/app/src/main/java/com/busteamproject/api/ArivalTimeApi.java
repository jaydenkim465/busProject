package com.busteamproject.api;

public class ArivalTimeApi {
    public void getArivalTimeOfBus(String cityCode, String nodeId, JSONCallBack callBack) {
        ApiHelper api = ApiHelper.getInstance();
        String requestUrl = "https://apis.data.go.kr/1613000/ArvlInfoInqireService/getSttnAcctoArvlPrearngeInfoList";
        String key = "0y0iJ9SX92FFc%2FLnxp9IAOfbJvBpcvjdwbkJY6cxdJupuPuryYpGB%2B37hnA5%2Fn21dOdPvcwW2%2Bsj7i%2F6A8Y7iQ%3D%3D";

        api.govJsonGet(requestUrl
                , "?serviceKey=" + key + "&cityCode=" + cityCode + "&nodeId=" + nodeId + "&_type=json"
                , callBack);
    }
}
