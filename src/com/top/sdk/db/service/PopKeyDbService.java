package com.top.sdk.db.service;

import java.util.List;

import com.top.sdk.entity.PopKey;

public interface PopKeyDbService {

	public PopKey getPopKey(String packageName, String version);

	public boolean insertListPopKey(List<PopKey> list);

	public boolean deletePopKey(String adKey);
}
