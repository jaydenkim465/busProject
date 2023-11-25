package com.busteamproject.api;

import org.json.JSONException;
import org.json.JSONObject;

public interface CallBack {
	void ApiResult(JSONObject result) throws JSONException;
}
