package com.top.sdk.entity;

public class PopKey {

	private int adKey;
	private String packageName;
	private String version;
	private String channelKey;
	private String channelValue;

	
	public String getChannelKey() {
		return channelKey;
	}

	public void setChannelKey(String channelKey) {
		this.channelKey = channelKey;
	}

	public String getChannelValue() {
		return channelValue;
	}

	public void setChannelValue(String channelValue) {
		this.channelValue = channelValue;
	}

	public int getAdKey() {
		return adKey;
	}

	public void setAdKey(int adKey) {
		this.adKey = adKey;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "PopKey [adKey=" + adKey + ", packageName=" + packageName
				+ ", version=" + version + ", channelKey=" + channelKey
				+ ", channelValue=" + channelValue + "]";
	}

}
