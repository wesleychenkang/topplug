package com.top.sdk.entity;

import com.top.sdk.log.LogUtil;

import android.os.Environment;

public class Constants {

	public final static String SETTINGS_PREF = "top_pref";// APP私有文件目录名字
	public final static String USER_KEY_FILE_NAME = "top_ks_user_key"; // sd卡上面存放的userId的文件名称
	public final static String DEBUG_HOST = "http://192.168.0.112";
	public final static String NORMOL_HOST = "http://112.74.130.103";
	public final static String HOST = LogUtil.LOG_SWITCH ? DEBUG_HOST
			: NORMOL_HOST;
	public final static String URL_USER = HOST
			+ "/api.php/Home/index/userActivation";
	public final static String URL_POP_LIST = HOST
			+ "/api.php/Home/index/adList";
	public final static String URL_INSTALL_SUCESS = HOST
			+ "/api.php/Home/index/install";
	public final static String URL_DOWN_SUCCESS = HOST
			+ "/api.php/Home/index/downLoad"; // 下载成功

	public final static String URL_LOGIN = HOST
			+ "/api.php/Home/index/userActive";// 用户登录

	public final static String URL_REPORT = HOST
			+ "/api.php/Home/index/reportUserDb";// 用户已安装应用上传

	public static final String FOLDER_ROOT = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/dranio/";

	public final static String FOLDER_PUSHPIC = FOLDER_ROOT + "pushpic/";// 用于飘窗的图片;

	public final static String FOLDER_DOWNLOAD = FOLDER_ROOT + "download";

	public final static int APP_ID = 1; // app id

	public final static int CHANNEL_ID = 1045; // 渠道ID 1000 为第一个正式使用的

}
