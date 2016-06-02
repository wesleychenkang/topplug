package com.top.sdk.http.reqentity;

import org.json.JSONException;
import org.json.JSONObject;

import com.top.sdk.utils.DESCoder;
import com.top.sdk.utils.SharedPrefUtil;

import android.content.Context;

public class LoginReqPragam extends BaseReqPragam {
    private Context context;
	public LoginReqPragam(Context context) {
		super(context);
		this.context = context;
	}
	@Override
	public String toJson() {
		JSONObject obj= getBaseJson();
		try {
			obj.put("plugVersion", SharedPrefUtil.getPlugVersion(context));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return DESCoder.ebotongEncrypto(obj.toString());
	}
}
