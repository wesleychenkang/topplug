package com.top.sdk.http.business;

import com.top.sdk.entity.Constants;
import com.top.sdk.http.reqentity.BaseReqPragam;
import com.top.xutils.HttpUtils;
import com.top.xutils.http.RequestParams;
import com.top.xutils.http.callback.RequestCallBack;
import com.top.xutils.http.client.HttpRequest.HttpMethod;

public class LoginHttpBusiness implements InterfaceHttpbusiness {

	@Override
	public void httpBusiness(RequestCallBack<String> call, BaseReqPragam pargam) {
		HttpUtils http = new HttpUtils();
		RequestParams requst = new RequestParams();
		requst.addBodyParameter("appId",""+Constants.APP_ID);
		requst.addBodyParameter("channelId",""+Constants.CHANNEL_ID);
		requst.addBodyParameter("header", pargam.toJson());
		http.send(HttpMethod.POST, Constants.URL_LOGIN, requst, call);
	}
}
