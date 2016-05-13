package com.top.sdk.entity;

import com.top.sdk.utils.DeviceUtil;
import com.top.sdk.utils.LogUtil;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

public class DeviceInfo {
	private String imsi;
	private String imei;
	private String phType;
	private String deviceId;
	private String androidVersion; 

	
	private Context context;
	private TelephonyManager tm ;
    public DeviceInfo(Context context){
     this.context = context;
     tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
    }
	public String getDeviceId() {
		deviceId = DeviceUtil.readUserId(context);
		LogUtil.d("deviceId-->"+deviceId);
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		
		this.deviceId = deviceId;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		imsi = tm.getSubscriberId();
		if(TextUtils.isEmpty(imsi)){
			imsi = "";
		}
		this.imsi = imsi;
	}

	public String getImei() {
		imei = tm.getDeviceId();
		if(TextUtils.isEmpty(imei)){
			imei="";  
		}
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getPhType() {
		if(TextUtils.isEmpty(phType))
		phType = Build.MODEL;
		return phType;
	}

	public void setPhType(String phType) {
		this.phType = phType;
	}
	public String getAndroidVersion() {
		androidVersion = Build.VERSION.RELEASE;
		return androidVersion;
	}
	@Override
	public String toString() {
		return "DeviceInfo [imsi=" + imsi + ", imei=" + imei + ", phType="
				+ phType + ", deviceId=" + deviceId + "]";
	}
	

}
