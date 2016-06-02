package com.top.sdk.entity;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginInfo {
	private String userId;
	private String loingTime;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@SuppressLint("SimpleDateFormat")
	public String getLoginTime() {
		SimpleDateFormat foramat = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		loingTime = foramat.format(today);
		return loingTime;
	}

	public void setLoingTime(String loingTime) {
		this.loingTime = loingTime;
	}
}
