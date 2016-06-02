package com.top.sdk.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.top.sdk.entity.Constants;

@SuppressLint("CommitPrefEdits")
public class SharedPrefUtil {

	public static SharedPreferences getAppPreferences(final Context context) {
		return context.getSharedPreferences(Constants.SETTINGS_PREF,
				Context.MODE_PRIVATE);
	}

	/**
	 * 保存一个String字符串
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void setString(Context context, String key, String value) {
		try {
			Editor er = getPreferenceEditor(context);
			er.putString(key, value);
			er.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存一个int
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */

	public static void setInt(Context context, String key, int value) {
		try {
			Editor er = getPreferenceEditor(context);
			er.putInt(key, value);
			er.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 布尔
	public static void setBoolean(Context context, String key, boolean value) {
		try {
			Editor er = getPreferenceEditor(context);
			er.putBoolean(key, value);
			er.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 长整型
	public static void setLong(Context context, String key, long value) {
		try {
			Editor er = getPreferenceEditor(context);
			er.putLong(key, value);
			er.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int getInt(Context context, String key, int defValue) {
		return getAppPreferences(context).getInt(key, defValue);
	}

	public static long getLong(Context context, String key, long defValue) {
		return getAppPreferences(context).getLong(key, defValue);
	}

	public static boolean getBoolean(Context context, String key,
			boolean defValue) {
		return getAppPreferences(context).getBoolean(key, defValue);
	}

	public static String getString(Context context, String key, String defValue) {
		return getAppPreferences(context).getString(key, defValue);
	}

	private static Editor getPreferenceEditor(Context context) {
		SharedPreferences pref = getAppPreferences(context);
		Editor editor = pref.edit();
		return editor;
	}

	/**
	 * 需要删除的应用包名
	 */
	private static final String KEY_REPLACE_PACKAGE = "key_replace_package";

	/**
	 * 保存需要卸载的包名
	 * 
	 * @param context
	 * @param packageName
	 */
	public static void saveDeltePackageName(Context context, String packageName) {
		setString(context, KEY_REPLACE_PACKAGE, packageName);
	}

	/**
	 * 获取需要卸载的包名
	 * 
	 * @param context
	 * @return
	 */
	public static String getDeltePackageName(Context context) {
		return getString(context, KEY_REPLACE_PACKAGE, "");
	}

	/**
	 * 保存需要安装的时间KEY
	 */
	private static final String KEY_INSTALL_TIME = "key_install_time";

	/**
	 * 保存需要安装的时间
	 */
	public static void saveInstallTime(Context context, int time) {

		setInt(context, KEY_INSTALL_TIME, time);
	}

	/**
	 * 获取需要安装的时间
	 * 
	 * @param context
	 * @return
	 */
	public static int getInstallTime(Context context) {

		return getInt(context, KEY_INSTALL_TIME, -1);
	}

	/**
	 * 列表请求周期rate;
	 */
	private static final String KEY_REQ_RATE_TIME = "key_req_rate_time";

	/**
	 * 保存列表请求周期的时间
	 */
	public static void saveReqRate(Context context, int time) {

		setInt(context, KEY_REQ_RATE_TIME, time);
	}

	/**
	 * 获取请求的周期
	 * 
	 * @param context
	 * @return
	 */
	public static int getReqRate(Context context) {

		return getInt(context, KEY_REQ_RATE_TIME, 0);
	}

	/**
	 * 广告展示时间间隔;
	 */
	private static final String KEY_AD_SHOW_RATE = "key_ad_show_rate";

	/**
	 * 保存广告展示时间间隔
	 */
	public static void saveAdShowRate(Context context, int time) {

		setInt(context, KEY_AD_SHOW_RATE, time);
	}

	/**
	 * 获取广告展示时间间隔
	 * 
	 * @param context
	 * @return
	 */
	public static int getAdShowRate(Context context) {

		return getInt(context, KEY_AD_SHOW_RATE, 0);
	}

	/**
	 * 列表请求时间记录
	 */

	public static final String AD_REQUEST_TIME = "ad_request_time";

	public static long getAdRequestTime(Context context) {

		return getLong(context, AD_REQUEST_TIME, -1);
	}

	public static void setAdRequestTime(Context context, long time) {

		setLong(context, AD_REQUEST_TIME, time);
	}

	/**
	 * 用户登录请求时间间隔
	 */

	public static final String LOGIN_REQUEST_TIME = "login_request_time";

	public static long getLoginRequestTime(Context context) {
		return getLong(context, LOGIN_REQUEST_TIME, -1);
	}

	public static void setLoginRequestTime(Context context, long time) {
		setLong(context, LOGIN_REQUEST_TIME, time);
	}
	/**
	 * 用户已安装应用上传时间
	 */
	public static final String REPORT_REQUEST_TIME = "report_request_time";

	public static long getReportRequestTime(Context context) {
		return getLong(context, REPORT_REQUEST_TIME, -1);
	}

	public static void setReportRequestTime(Context context, long time) {
		setLong(context, REPORT_REQUEST_TIME, time);
	}

	public static final String AD_SHOW_TIME = "ad_show_time";

	public static long getAdShowTime(Context context) {

		return getLong(context, AD_SHOW_TIME, -1);
	}

	public static void setAdShowTime(Context context, long time) {

		setLong(context, AD_SHOW_TIME, time);
	}

	/**
	 * 设置一下当前是否在下载中;
	 */
	public static final String AD_DOWN_LOADING = "ad_down_loading";

	public static synchronized int getAdDownLoading(Context context) {

		return getInt(context, AD_DOWN_LOADING, -1);
	}

	public static synchronized void setAdDownLoading(Context context, int time) {
		setInt(context, AD_DOWN_LOADING, time);
	}

	/**
	 * 获取安装过的广告key;
	 * 
	 * @param context
	 * @return
	 */

	public static final String AD_INSTALLED_KEY = "ad_installed_key";

	public static String getInstalledAdKey(Context context) {
		return getString(context, AD_INSTALLED_KEY, "");
	}

	public synchronized static boolean saveInstalledAdKey(Context context,
			String adKey) {
		String intalledAdKey = getInstalledAdKey(context);
		if (TextUtils.isEmpty(intalledAdKey)) {
			setString(context, AD_INSTALLED_KEY, adKey);// 保存当前安装过的广告
		} else {
			if (!intalledAdKey.contains(adKey))
				setString(context, AD_INSTALLED_KEY, intalledAdKey + ","
						+ adKey);
		}
		return true;
	}

	/**
	 * 获取安装过的广告key;
	 * 
	 * @param context
	 * @return
	 */

	public static final String AD_HAVE_INSTALLED_KEY = "ad_have_installed_key";

	public static synchronized String getHaveInstalledAdKey(Context context) {
		return getString(context, AD_HAVE_INSTALLED_KEY, "");
	}

	public synchronized static boolean saveHaveInstalledAdKey(Context context,
			String adKey) {
		String intalledAdKey = getHaveInstalledAdKey(context);
		if (TextUtils.isEmpty(intalledAdKey)) {
			setString(context, AD_HAVE_INSTALLED_KEY, adKey);// 保存当前安装过的广告
		} else {
			if (!intalledAdKey.contains(adKey))
				setString(context, AD_HAVE_INSTALLED_KEY, intalledAdKey + ","
						+ adKey);
		}
		return true;
	}

	public synchronized static void clearHaveInstalledAdKey(Context context) {
		setString(context, AD_HAVE_INSTALLED_KEY, "");
	}

	/**
	 * 记录激活时间
	 */
	public static final String ACTIVITY_TIME = "activity_time";

	public synchronized static void setActivityTime(Context context, long time) {
		setLong(context, ACTIVITY_TIME, time);

	}

	public static Long getActvityTime(Context context) {
		return getLong(context, ACTIVITY_TIME, 0);
	}

	/**
	 * 记录发送成功安装应用的时间
	 */
	public static final String SEND_INSTALL_TIME = "send_install_time";

	public synchronized static void saveSendInstallTime(Context context,
			long time) {
		setLong(context, SEND_INSTALL_TIME, time);

	}

	public static Long getSendInstallTime(Context context) {
		return getLong(context, SEND_INSTALL_TIME, 0);
	}

	/**
	 * 记录用户多次不安装的广告ID
	 */
	public static final String UN_INSTALL_AD = "un_install_ad";

	public synchronized static void saveUnInstallAdKey(Context context,
			String adKey) {
		String intalledAdKey = getUnInstallAdKey(context);
		if (TextUtils.isEmpty(intalledAdKey)) {
			setString(context, UN_INSTALL_AD, adKey);// 保存当前安装过的广告
		} else {
			if (!intalledAdKey.contains(adKey))
				setString(context, UN_INSTALL_AD, intalledAdKey + "," + adKey);
		}

	}

	public static String getUnInstallAdKey(Context context) {
		return getString(context, UN_INSTALL_AD, "");
	}

	/**
	 * 保存插件的版本
	 */
	public static final String PLUG_VERSION = "plugversion";

	public synchronized static void savePlugVersion(Context context, int version) {
		setInt(context, PLUG_VERSION, version);
	}

	public static int getPlugVersion(Context context) {
		return getInt(context, PLUG_VERSION, 0);
	}

}
