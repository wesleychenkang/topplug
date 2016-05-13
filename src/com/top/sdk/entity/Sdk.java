package com.top.sdk.entity;

import com.top.sdk.utils.XmlUtil;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

public class Sdk {
	private String sdkId;

	private String sdkVersion;
	private String channelId;
	private Context context;

	public Sdk(Context context) {
		this.context = context;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getSdkVersion() {
			sdkVersion = XmlUtil.getAppVersionName(context);
		return sdkVersion;
	}

	public void setSdkVersion(String sdkVersion) {
		this.sdkVersion = sdkVersion;
	}

	public String getSdkId() {
		return sdkId;
	}

	public void setSdkId(String sdkId) {
		this.sdkId = sdkId;
	}

}
