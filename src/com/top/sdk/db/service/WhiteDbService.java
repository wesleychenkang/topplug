package com.top.sdk.db.service;

import java.util.List;

import com.top.sdk.entity.WhiteData;

public interface WhiteDbService {
	
	public List<WhiteData> getWhiteDataList(); // 得到所有的有效广告列表数据

	public WhiteData getWhiteDataFromId(int id);
	
	public boolean insertWhiteData(WhiteData data);

	public boolean deleteWhiteData(int id);

	public boolean deleteAllWhiteData();
	
	public boolean updateWhiteData(WhiteData data);
	
	public int getAdIdByPackageName(String packageName);
   
	public boolean saveWhiteDataList(List<WhiteData> list);
}
