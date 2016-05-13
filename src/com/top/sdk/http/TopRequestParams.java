package com.top.sdk.http;

import com.top.xutils.http.RequestParams;

public class TopRequestParams extends RequestParams {
    @Override
    public void addBodyParameter(String name, String value) {
    	   addBodyParameter("appId","1");
    }
}
