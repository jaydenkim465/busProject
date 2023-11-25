package com.busteamproject.api;

import android.util.Log;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiHelper {
	private ApiHelper() {}

	public static ApiHelper getInstance() {
		return LazyHodler.instance;
	}

	private static class LazyHodler {
		private static final ApiHelper instance = new ApiHelper();
	}

	public void kakaoGet(String stringURL, String param, JSONCallBack callBack) {
		new Thread(() -> {
			try {
				URL url = new URL(stringURL + param);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setConnectTimeout(5000); //서버에 연결되는 Timeout 시간 설정
				con.setReadTimeout(5000); // InputStream 읽어 오는 Timeout 시간 설정
				con.setRequestMethod("GET");
				con.setRequestProperty("Authorization", "KakaoAK 42f354b6876c3ffd4076ea7317ef14c7");
				con.setRequestProperty("FORMAT", "JSON");
				con.setDoOutput(false);
				callGet(con, callBack);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}).start();
	}

	public void govJsonGet(String stringURL, String param, JSONCallBack callBack) {
		new Thread(() -> {
			try {
				URL url = new URL(stringURL + param);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setConnectTimeout(5000); //서버에 연결되는 Timeout 시간 설정
				con.setReadTimeout(5000); // InputStream 읽어 오는 Timeout 시간 설정
				con.setRequestMethod("GET");
				con.setDoOutput(false);
				callGet(con, callBack);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}).start();
	}

	public void govXMLGet(String stringURL, String param, JSONCallBack callBack) {
		new Thread(() -> {
			try {
				URL url = new URL(stringURL + param);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setConnectTimeout(5000); //서버에 연결되는 Timeout 시간 설정
				con.setReadTimeout(5000); // InputStream 읽어 오는 Timeout 시간 설정
				con.setRequestMethod("GET");
				con.setDoOutput(false);
				callGet(con, callBack);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}).start();
	}

	private void callGet(HttpURLConnection con, JSONCallBack callBack) {
		try {
			StringBuilder sb = new StringBuilder();
			if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(
						new InputStreamReader(con.getInputStream(), "utf-8"));
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line).append("\n");
				}
				br.close();
				JSONObject responseData = new JSONObject(sb.toString());
				Log.d(this.getClass().getName(), responseData.toString());
				callBack.ApiResult(responseData);
				return;
			} else {
				Log.d(this.getClass().getName(), con.getResponseMessage());
			}
		} catch (Exception e) {
			Log.d(this.getClass().getName(), e.toString());
		}
		callBack.ApiResult(new JSONObject());
	}
}
