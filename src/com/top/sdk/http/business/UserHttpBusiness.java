package com.top.sdk.http.business;

import com.top.sdk.entity.Constants;
import com.top.sdk.http.TopRequestParams;
import com.top.sdk.http.reqentity.BaseReqPragam;
import com.top.sdk.http.respone.entity.UserResult;
import com.top.sdk.utils.LogUtil;
import com.top.xutils.HttpUtils;
import com.top.xutils.http.RequestParams;
import com.top.xutils.http.callback.RequestCallBack;
import com.top.xutils.http.client.HttpRequest.HttpMethod;

public class UserHttpBusiness implements InterfaceHttpbusiness {
	@Override
	public void httpBusiness(RequestCallBack<String> call, BaseReqPragam pargam) {
		HttpUtils http = new HttpUtils();
		RequestParams requst = new RequestParams();
		requst.addBodyParameter("appId",""+Constants.APP_ID);
		requst.addBodyParameter("channelId",""+Constants.CHANNEL_ID);
		requst.addBodyParameter("header", pargam.toJson());
		http.send(HttpMethod.POST, Constants.URL_USER, requst, call);
	}

}
