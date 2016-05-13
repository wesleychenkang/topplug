package com.top.sdk.db.service;

import java.util.List;

import com.top.sdk.entity.PopInstallData;

public interface InstallDbService {

	public List<PopInstallData> getListInstallData();

	public  int getInstallCountByMonth(String month);

	public boolean addInstallData(PopInstallData data);

	public boolean deleteInstallDataByMonth(String month);

	public PopInstallData getPopInstallById(int popId);
}
