package com.top.sdk.http.reqentity;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.top.sdk.log.LogUtil;
import com.top.sdk.utils.DESCoder;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

public class ReportReqPragam extends BaseReqPragam {
	private Context context;

	public ReportReqPragam(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public String toJson() {
		JSONObject obj = getBaseJson();
		try {
			JSONArray list = new JSONArray();
			List<PackageInfo> packages = context.getPackageManager()
					.getInstalledPackages(0);
			JSONObject o = null;
			for (int i = 0; i < packages.size(); i++) {

				PackageInfo packageInfo = packages.get(i);

				String appName = packageInfo.applicationInfo.loadLabel(
						context.getPackageManager()).toString();
				String packageName = packageInfo.packageName;
				String versionName = packageInfo.versionName;
				// 如果属于非系统程序，则添加到列表显示
				if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
					o = new JSONObject();
					o.put("packageName", packageName);
					o.put("name", appName);
					o.put("versionName", versionName);
					list.put(o);
				}
			}
			
			obj.put("dbList", list);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return DESCoder.ebotongEncrypto(obj.toString());
	}

}
