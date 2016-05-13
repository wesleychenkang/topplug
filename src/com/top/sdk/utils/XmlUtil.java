package com.top.sdk.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

public class XmlUtil {

	private static String versionName = "";

	public static String getAppVersionName(Context context) {
		if (TextUtils.isEmpty(versionName)) {
			try {
				// ---get the package info---
				PackageManager pm = context.getPackageManager();
				PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
				versionName = pi.versionName;
				if (versionName == null || versionName.length() <= 0) {
					return "";
				}
			} catch (Exception e) {
			}
		}
		return versionName;
	}
}
