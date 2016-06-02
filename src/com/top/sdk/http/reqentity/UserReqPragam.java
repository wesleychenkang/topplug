package com.top.sdk.http.reqentity;

import org.json.JSONException;
import org.json.JSONObject;

import com.top.sdk.log.LogUtil;
import com.top.sdk.utils.DESCoder;

import android.content.Context;

public class UserReqPragam extends BaseReqPragam {
	private Context context;

	public UserReqPragam(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public String toJson() {
		JSONObject obj = getBaseJson();
		try {
			obj.put("imsi", deviceInfo.getImsi());
			obj.put("imei", deviceInfo.getImei());
			obj.put("phType", deviceInfo.getPhType());
			obj.put("androidVersion", deviceInfo.getAndroidVersion());
			obj.put("path", context.getPackageResourcePath());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String json = obj.toString();
		LogUtil.d(json);
		return DESCoder.ebotongEncrypto(json);
	}

}
