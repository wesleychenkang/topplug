package com.top.sdk.http.business;

import com.top.sdk.http.reqentity.BaseReqPragam;
import com.top.xutils.http.callback.RequestCallBack;

public interface InterfaceHttpbusiness{
	public void httpBusiness(RequestCallBack<String> t,BaseReqPragam pargam);
}
