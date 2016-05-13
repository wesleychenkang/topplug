package com.top.sdk.http.reqentity;

import org.json.JSONException;
import org.json.JSONObject;

import com.top.sdk.utils.DESCoder;

import android.content.Context;

public class InstallReqPragam extends BaseReqPragam {
	public InstallReqPragam(Context context) {
		super(context);
	}

	private String IDs;

	public String getIDs() {
		return IDs;
	}

	public void setIDs(String iDs) {
		IDs = iDs;
	}

	@Override
	public String toJson() {
		JSONObject obj = getBaseJson();
		try {
			obj.put("IDs", getIDs());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		String json = DESCoder.ebotongEncrypto(obj.toString());
		return json;
	}

}
