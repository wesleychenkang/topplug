package com.top.sdk.logic;

import android.content.Context;

import com.top.sdk.http.business.InterfaceHttpbusiness;
import com.top.sdk.http.business.UserHttpBusiness;
import com.top.sdk.http.reqentity.UserReqPragam;
import com.top.sdk.http.respone.entity.BaseResult;
import com.top.sdk.http.respone.parser.UserResultParser;
import com.top.sdk.utils.DeviceUtil;
import com.top.sdk.utils.LogUtil;
import com.top.sdk.utils.SharedPrefUtil;
import com.top.xutils.exception.HttpException;
import com.top.xutils.http.ResponseInfo;
import com.top.xutils.http.callback.RequestCallBack;

public class UserAction {
	public static boolean ISENTER = false;
	public static String userKey = "";

	public void userActivation(final Context context) {

		if (!getISEnter()) { // 先控制重复访问的问题存在
			ISENTER = true;
		}
		// 检查本地是否有保存过userId,没有就执行用户注册
		if (!checkUser(context)) {

			InterfaceHttpbusiness business = new UserHttpBusiness();
			business.httpBusiness(new RequestCallBack<String>() {
				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					LogUtil.d("test", "成功激活用户" + responseInfo.result.toString());
					String json = responseInfo.result.toString();
					// 激活成功，将用户ID保存在本地
					UserResultParser parser = new UserResultParser();
					BaseResult result = parser.parserJson(json);
					int code = result.getCode();
					if (code == 0 || code == 3) {
						String userId = DeviceUtil.readUserId(context);
						DeviceUtil.saveUserKey(context, userId);
						SharedPrefUtil.setActivityTime(context,
								System.currentTimeMillis());
					}

					ISENTER = false;
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					// TODO Auto-generated method stub
					LogUtil.d("test", "连接失败------》" + msg);
					ISENTER = false;
				}

			}, new UserReqPragam(context));

		}

	}

	private boolean checkUser(Context context) {
		boolean result = DeviceUtil.checkUserId(context);
		LogUtil.d("用户是否已注册====" + result);
		return result;
	}

	private static synchronized boolean getISEnter() {
		return ISENTER;

	}
}
