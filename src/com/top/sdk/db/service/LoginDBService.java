package com.top.sdk.db.service;

import com.top.sdk.entity.LoginInfo;

public interface LoginDBService {

	public boolean saveLoginInfo(LoginInfo infor);

	public LoginInfo getLoginInfo();
}
