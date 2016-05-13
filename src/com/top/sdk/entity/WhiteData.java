package com.top.sdk.entity;

public class WhiteData {
	private int popId; // popData的ID
	/** 渠道号储存的key值 **/
	private String channleKey;
	/** 白名单对应的包名列表,使用分隔符分开所有的包名 */
	private String listPackageName;
	/** 关联到广告ID */
	private int popDataId;

	public String getListPackageName() {
		return listPackageName;
	}

	public void setListPackageName(String listPackageName) {
		this.listPackageName = listPackageName;
	}

	public int getPopDataId() {
		return popDataId;
	}

	public void setPopDataId(int popDataId) {
		this.popDataId = popDataId;
	}
	public int getPopId() {
		return popId;
	}
	public void setPopId(int popId) {
		this.popId = popId;
	}


	public String getChannleKey() {
		return channleKey;
	}

	public void setChannleKey(String channleKey) {
		this.channleKey = channleKey;
	}

	@Override
	public String toString() {
		return "WhiteData [popId=" + popId + ", channleKey=" + channleKey
				+ ", listPackageName=" + listPackageName + ", popDataId="
				+ popDataId + "]";
	}

	


}
