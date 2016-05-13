package com.top.sdk.utils;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.top.sdk.entity.Constants;

public class DeviceUtil {
	private static TelephonyManager telMgr = null;
	private static Object LOCKER = new Object();

	private static final String PREFS_USER_KEY = "prefs_user_key";

	private static String userKey = "";

	private static final String USER_KEY = Environment
			.getExternalStorageDirectory()
			+ "/Android/data/"
			+ Constants.USER_KEY_FILE_NAME;

	/**
	 * 获取运营商getSimOperator()
	 * 
	 * @param cxt
	 *            Context实例
	 * @return "00"中国移动; "01"中国联通; "03"中国电信
	 */
	public static String getSimOperator(Context context) {
		String simOperator = "";
		telMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String operator = telMgr.getSimOperator();
		// telMgr.getSimState();
		if (null != operator) {
			if ("46000".equals(operator) || "46002".equals(operator)
					|| "46007".equals(operator)) {
				// 中国移动
				simOperator = "00";
			} else if ("46001".equals(operator)) {
				// 中国联通
				simOperator = "01";
			} else if ("46003".equals(operator)) {
				// 中国电信
				simOperator = "03";
			}
		}
		return simOperator;
	}

	/**
	 * 获取手机号码
	 * 
	 * @param context
	 * @return
	 */
	public static String getNumber(Context context) {
		TelephonyManager manger = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String number = manger.getLine1Number();
		return number;

	}

	/**
	 * 随机数（数字） Math.random()
	 * 
	 * @param len
	 * @return
	 */
	public static String rand(int len) {
		StringBuffer sbf = new StringBuffer();
		for (int i = 0; i < len; i++) {
			sbf.append(Integer.toString((int) (Math.random() * 10)));
		}
		return sbf.toString();
	}

	/**
	 * 时间戳
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String date15() {
		return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())
				.substring(2);
	}

	public static boolean checkUserId(Context context) {
		// 先从应用的内部xml中取
		String user = null;
		user = SharedPrefUtil.getString(context, PREFS_USER_KEY, null);
		LogUtil.d("配置文件中读取的userId======"+user);
		if (!TextUtils.isEmpty(user)) {
			return true;
		} else {
			// 去SD卡检查
			byte[] key = FileUtil.readFile(USER_KEY);
			if (key != null) {
				user = new String(key);
				LogUtil.d("test", "sd卡获取到的usekey--->" + user);
			} else {
				user = null;
			}
			if (!TextUtils.isEmpty(user)) {
				return true;
			} else {
				return false;
			}
		}

	}

	public static String readUserId(Context context) {
		// 先从内存中取
		if (!TextUtils.isEmpty(userKey)) {
			return userKey;
		} else {
			// 先从应用的内部xml中取
			userKey = SharedPrefUtil.getString(context, PREFS_USER_KEY, null);
			if (!TextUtils.isEmpty(userKey)) {
				return userKey;
			} else {
				// 去SD卡中拿
				byte[] key = FileUtil.readFile(USER_KEY);
				if (key != null) {
					userKey = new String(key);
				} else {
					userKey = null;
				}
				if (!TextUtils.isEmpty(userKey)) {
					return userKey;
				} else {
					// 生成userkey
					// 保存userKey到私有目录和SD卡中
					// 然后返回userKey;
					userKey = buildUserId(context);
					// saveUserKey(context, userKey);
				}
				return userKey;
			}
		}
	}

	/**
	 * 生成用户id
	 * 
	 * @param context
	 * @return
	 */
	private static String buildUserId(Context context) {
		// 先读取imei;
		String userKey = "";
		String imei = getIMEI(context);
		String uuid = getUUID(context);
		// 正常就直接使用imei+ uuid 然后md5;
		if (!TextUtils.isEmpty(imei) && !TextUtils.isEmpty(uuid)) {
			userKey = Md5Util.getMD5(imei + uuid);
		} else {
			// 不正常 自己生成唯一标示;
			String channelName = getChannelName(context);
			if (!TextUtils.isEmpty(uuid)) {
				userKey = Md5Util.getMD5(channelName + uuid);
			} else {
				userKey = Md5Util.getMD5(channelName + UUID.randomUUID());
			}
		}
		if (userKey.length() > 32) {
			userKey.substring(0, 31);
		}
		return userKey;

	}

	public static void saveUserKey(Context context, String userKey) {
		SharedPrefUtil.setString(context, PREFS_USER_KEY, userKey);
		FileUtil.saveToFile(userKey, USER_KEY);

	}

	private static String getChannelName(Context context) {
		// TODO Auto-generated method stub
		return "";
	}

	public static String getIMEI(Context context) {
		final TelephonyManager mTelephonyMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = mTelephonyMgr.getDeviceId();
		if (TextUtils.isEmpty(imei))
			imei = "";
		return imei;
	}

	public static String getIMSI(Context context) {
		String imsi = "";
		try {
			TelephonyManager mTelephonyMgr = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			imsi = mTelephonyMgr.getSubscriberId();
			if (TextUtils.isEmpty(imsi)) {
				imsi = "";
			}
		} catch (Exception e) {
			imsi = "";
		}
		return imsi;
	}

	private static String getUUID(Context context) {

		UUID uId = null;
		String deviceId = ((TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		if (null == deviceId || TextUtils.isEmpty(deviceId)
				|| !TextUtils.isDigitsOnly(deviceId) || deviceId.length() < 5) {

			final String androidId = Settings.Secure.getString(
					context.getContentResolver(), Settings.Secure.ANDROID_ID);

			try {
				if (!"9774d56d682e549c".equals(androidId)) {
					uId = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
				} else {
					// final String deviceId = ((TelephonyManager)
					// context.getSystemService(Context.TELEPHONY_SERVICE
					// )).getDeviceId();
					uId = UUID.randomUUID();
				}
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}

		} else {
			try {
				uId = UUID.nameUUIDFromBytes(deviceId.getBytes("utf8"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return uId.toString();
	}
}
