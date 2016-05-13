package com.top.sdk.http.respone.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.top.sdk.entity.Constants;
import com.top.sdk.http.respone.entity.BaseResult;
import com.top.sdk.utils.DESCoder;

public class BaseResultParser implements ParserInterface<BaseResult> {

	@Override
	public BaseResult parserJson(String json) {
		BaseResult result = new BaseResult();
		try {
			JSONObject obj = new JSONObject(json);
			int code = obj.getInt("code");
			result.setCode(code);
			result.setMsg(obj.getString("msg"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}
