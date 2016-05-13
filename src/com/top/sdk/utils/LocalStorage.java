package com.top.sdk.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class LocalStorage {
	/**
	 * SharedPreferences 本地存储
	 */

	public static final String STORAGE_NAME = "TopSdk";

	private SharedPreferences sharedPreferences;

	private LocalStorage(Context context) {
		sharedPreferences = context.getSharedPreferences(STORAGE_NAME,
				Context.MODE_PRIVATE);
	}

	public static LocalStorage getInstance(Context context) {
		return new LocalStorage(context);
	}

	public boolean hasKey(String key) {
		return !TextUtils.isEmpty(key) && sharedPreferences.contains(key);
	}

	public void putString(String key, String value) {

		if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value))
			return;
		sharedPreferences.edit().putString(key, value).commit();

	}

	public String getString(String key, String... defaultValues) {
		if (TextUtils.isEmpty(key))
			return null;
		String defaultValue = defaultValues.length >= 1 ? defaultValues[0] : "";
		return sharedPreferences.getString(key, defaultValue);

	}

	public void putInt(String key, int value) {
		sharedPreferences.edit().putInt(key, value).commit();
	}

	public int getInt(String key, int... defaultValues) {
		if (TextUtils.isEmpty(key))
			return 0;
		int defaultValue = defaultValues.length >= 1 ? defaultValues[0] : 0;
		return sharedPreferences.getInt(key, defaultValue);

	}

	public void putLong(String key, long value) {
		sharedPreferences.edit().putLong(key, value).commit();
	}

	public long getLong(String key, long... defaultValues) {
		if (TextUtils.isEmpty(key))
			return 0;
		long defaultValue = defaultValues.length >= 1 ? defaultValues[0] : 0;
		return sharedPreferences.getLong(key, defaultValue);

	}

}
