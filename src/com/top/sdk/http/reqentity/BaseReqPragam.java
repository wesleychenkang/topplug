package com.top.sdk.http.reqentity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.top.sdk.entity.DeviceInfo;
import com.top.sdk.entity.Sdk;
import com.top.sdk.utils.DeviceUtil;

public abstract class BaseReqPragam {
	protected DeviceInfo deviceInfo;
	protected Sdk sdk;

	public BaseReqPragam(Context context) {
		deviceInfo = new DeviceInfo(context);
		sdk = new Sdk(context);
	}

	public String getSdkVer() {
		return sdk.getSdkVersion();
	}

	protected JSONObject getBaseJson() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("sdkVer", sdk.getSdkVersion());
			obj.put("userId", deviceInfo.getDeviceId());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}

	public abstract String toJson();

}
