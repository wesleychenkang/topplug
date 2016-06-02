package com.top.sdk.http.reqentity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.animation.IntArrayEvaluator;
import android.content.Context;
import android.util.Log;

import com.top.sdk.db.impservice.ImpInstallDataService;
import com.top.sdk.db.service.InstallDbService;
import com.top.sdk.log.LogUtil;
import com.top.sdk.utils.DESCoder;
import com.top.sdk.utils.SharedPrefUtil;

public class PopReqPragam extends BaseReqPragam {
	private Context context;

	public PopReqPragam(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toJson() {
		JSONObject obj = getBaseJson();
		try {
			obj.put("IDs", SharedPrefUtil.getInstalledAdKey(context));
			obj.put("activeTime", SharedPrefUtil.getActvityTime(context));
			InstallDbService db = new ImpInstallDataService(context);
			DateFormat format = new SimpleDateFormat("MM");
			String month = format.format(new Date());
			int count = db.getInstallCountByMonth(month);
			obj.put("number", count);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LogUtil.d("请求的json-->"+obj.toString());
		String json = DESCoder.ebotongEncrypto(obj.toString());
		return json;
	}

}
