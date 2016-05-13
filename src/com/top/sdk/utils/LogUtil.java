package com.top.sdk.utils;

import android.util.Log;

public class LogUtil {

	public static boolean LOG_SWITCH = true;
	public static String TAG = "test";

	public static void i(String tag, String msg) {
		if (LOG_SWITCH) {
			Log.i(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (LOG_SWITCH) {
			Log.e(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (LOG_SWITCH) {
			Log.d(tag, msg);
		}

	}

	public static void d(String msg) {
		if (LOG_SWITCH) {
			Log.d(TAG, msg);
		}

	}
}