package com.busteamproject.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class SharedPreferenceHelper {
	private static SharedPreferences sharedPreferences = null;
	private static SharedPreferenceHelper instance = null;

	private static final String fileName = "busApp";

	private SharedPreferenceHelper(Context context) {
		sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
	}

	public static SharedPreferenceHelper getInstance(Context context) {
		if(instance == null) {
			instance = new SharedPreferenceHelper(context);
		}
		return instance;
	}

	public String getString(String key) {
		return sharedPreferences.getString(key, "");
	}

	public Set<String> getStringSet(String key) {
		return sharedPreferences.getStringSet(key, new HashSet<>());
	}

	public void putString(String key, String value) {
		sharedPreferences.edit().putString(key, value).apply();
	}

	public void putStringSet(String key, Set<String> values) {
		sharedPreferences.edit().putStringSet(key, values).apply();
	}
}
