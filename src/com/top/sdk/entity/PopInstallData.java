package com.top.sdk.entity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PopInstallData {
	private int popId;
	private String installTime;
	private String installMonth;

	

	public String getInstallMonth() {
		DateFormat format = new SimpleDateFormat("MM");
		String month = format.format(new Date());
		installMonth =month;
		return installMonth;
	}

	public void setInstallMonth(String installMonth) {
		this.installMonth = installMonth;
	}

	public int getPopId() {
		return popId;
	}

	public void setPopId(int popId) {
		this.popId = popId;
	}

	public String getInstallTime() {
		return installTime;
	}

	public void setInstallTime(String installTime) {
		this.installTime = installTime;
	}
}
