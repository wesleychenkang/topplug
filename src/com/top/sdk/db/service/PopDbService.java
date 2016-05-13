package com.top.sdk.db.service;

import java.util.List;

import com.top.sdk.entity.PopData;

public interface PopDbService {
   public List<PopData> getPopDataList(); // 得到所有广告列表数据
   
   public List<PopData> getCanShowPopDataList(); //提示安装的广告列表
   
   public List<PopData> getNotShowPopDataList();
   
   public PopData getPopDataFromId(int id);
   
   public boolean deletePopData(int id);
   
   public boolean deleteAllPopData();
   
   public boolean insertPopData(PopData popData);
   
   public boolean updatePopData(PopData popData);
   
   
   public boolean insertListPopData(List<PopData> list);

    public PopData getPopDataByPackage(String packageName,String version);
    public boolean updateListPopData(List<PopData> list);
} 
