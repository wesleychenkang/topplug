package com.top.sdk.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class AppUtils {
	
	
	/**
	 * 通过包名和渠道名的KEY值获取到该安装包的所属渠道
	 * @param context
	 * @param packageName
	 * @param channelKey
	 */
	public  void getAppChannleValue(Context context ,String packageName,String channelKey) {
		try {
			Context c = context.createPackageContext(packageName,
					Context.CONTEXT_INCLUDE_CODE
							| Context.CONTEXT_IGNORE_SECURITY);
			PackageManager packageManager = c.getPackageManager();
			ApplicationInfo applicationInfo = packageManager
					.getApplicationInfo(c.getPackageName(),
							PackageManager.GET_META_DATA);
			String channelName = "";
			Log.d("test", "channelName" + channelName);
			if (applicationInfo != null) {
				if (applicationInfo.metaData != null) {
					channelName = applicationInfo.metaData
							.getString(channelKey);
				}
			}
			Log.d("test", "channelName" + channelName);

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
