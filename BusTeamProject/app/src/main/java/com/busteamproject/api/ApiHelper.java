package com.busteamproject.api;

import android.content.Context;
import android.util.Log;

import com.busteamproject.util.Util;

import org.json.android.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiHelper {
	private ApiHelper() {
	}

	public static ApiHelper getInstance() {
		return LazyHodler.instance;
	}

	private static class LazyHodler {
		private static final ApiHelper instance = new ApiHelper();
	}

	public void kakaoJsonGet(Context context, String stringURL, String param, JSONCallBack callBack) {
		new Thread(() -> {
			try {
				URL url = new URL(stringURL + param);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setConnectTimeout(5000); //서버에 연결되는 Timeout 시간 설정
				con.setReadTimeout(5000); // InputStream 읽어 오는 Timeout 시간 설정
				con.setRequestMethod("GET");
				con.setRequestProperty("Authorization", Util.getApiKey(context, "kakaoAddressKey"));
				con.setRequestProperty("FORMAT", "JSON");
				con.setDoOutput(false);
				callJSONGet(con, callBack);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}).start();
	}

	public void kakaoStringGet(Context context, String stringURL, String param, StringCallback callBack) {
		new Thread(() -> {
			try {
				URL url = new URL(stringURL + param);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setConnectTimeout(5000); //서버에 연결되는 Timeout 시간 설정
				con.setReadTimeout(5000); // InputStream 읽어 오는 Timeout 시간 설정
				con.setRequestMethod("GET");
				con.setRequestProperty("Authorization", Util.getApiKey(context, "kakaoAddressKey"));
				con.setRequestProperty("FORMAT", "JSON");
				con.setDoOutput(false);
				callStringGet(con, callBack);
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
				callJSONGet(con, callBack);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}).start();
	}

	public void govStringGet(String stringURL, String param, StringCallback callBack) {
		new Thread(() -> {
			try {
				URL url = new URL(stringURL + param);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setConnectTimeout(5000); //서버에 연결되는 Timeout 시간 설정
				con.setReadTimeout(5000); // InputStream 읽어 오는 Timeout 시간 설정
				con.setRequestMethod("GET");
				con.setDoOutput(false);
				callStringGet(con, callBack);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}).start();
	}

	public String govStringGet(String stringURL, String param) {
		try {
			URL url = new URL(stringURL + param);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(5000); //서버에 연결되는 Timeout 시간 설정
			con.setReadTimeout(5000); // InputStream 읽어 오는 Timeout 시간 설정
			con.setRequestMethod("GET");
			con.setDoOutput(false);
			return callStringGet(con, null);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	private JSONObject callJSONGet(HttpURLConnection con, JSONCallBack callBack) {
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
				int sbLength = sb.length();
				if (sbLength > 3600) {
					for (int i = 0; i < (sbLength / 3600) + 1; i++) {
						if (i == (sbLength / 3600)) {
							Log.d(this.getClass().getName(), sb.substring(i * 3600, (i * 3600) + (sbLength % 3600)));
						} else {
							Log.d(this.getClass().getCanonicalName(), sb.substring(i * 3600, (i + 1) * 3600));
						}
					}
				}
				JSONObject responseData = new JSONObject(sb.toString());
				if(callBack != null) {
					callBack.ApiResult(responseData);
				}
				return responseData;
			} else {
				Log.d(this.getClass().getName(), con.getResponseMessage());
			}
		} catch (Exception e) {
			Log.d(this.getClass().getName(), e.toString());
		}
		if(callBack != null) {
			callBack.ApiResult(new JSONObject());
		}
		return new JSONObject();
	}

	private String callStringGet(HttpURLConnection con, StringCallback callBack) {
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
				int sbLength = sb.length();
				if (sbLength > 3600) {
					for (int i = 0; i < (sbLength / 3600) + 1; i++) {
						if (i == (sbLength / 3600)) {
							Log.d(this.getClass().getName(), sb.substring(i * 3600, (i * 3600) + (sbLength % 3600)));
						} else {
							Log.d(this.getClass().getCanonicalName(), sb.substring(i * 3600, (i + 1) * 3600));
						}
					}
				}
				if(callBack != null) {
					callBack.ApiResult(sb.toString());
				}
				return sb.toString();
			} else {
				Log.d(this.getClass().getName(), con.getResponseMessage());
			}
		} catch (Exception e) {
			Log.d(this.getClass().getName(), e.toString());
		}

		if(callBack != null) {
			callBack.ApiResult("");
		}
		return "";
	}

	public void tmapGet(Context context, String stringURL, JSONObject jsonData, JSONCallBack callBack){
		new Thread(() -> {
			try {
				URL url = new URL(stringURL);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setConnectTimeout(5000); //서버에 연결되는 Timeout 시간 설정
				con.setReadTimeout(5000); // InputStream 읽어 오는 Timeout 시간 설정
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type","application/json");
				con.setRequestProperty("Accept","application/json");
				con.setRequestProperty("appKey",Util.getApiKey(context, "tmapKey"));
				con.setDoOutput(true);

				OutputStream os = con.getOutputStream();
				os.write(jsonData.toString().getBytes("utf-8"));
				os.flush();
				os.close();
				callJSONGet(con, callBack);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}).start();
	}

	public JSONObject tmapGet(Context context, String stringURL, JSONObject jsonData) {
		try {
			URL url = new URL(stringURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(5000); //서버에 연결되는 Timeout 시간 설정
			con.setReadTimeout(5000); // InputStream 읽어 오는 Timeout 시간 설정
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type","application/json");
			con.setRequestProperty("Accept","application/json");
			con.setRequestProperty("appKey", Util.getApiKey(context, "tmapKey"));
			con.setDoOutput(true);

			OutputStream os = con.getOutputStream();
			os.write(jsonData.toString().getBytes("utf-8"));
			os.flush();
			os.close();
			return callJSONGet(con, null);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new JSONObject();
	}
}
