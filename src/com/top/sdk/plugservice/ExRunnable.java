package com.top.sdk.plugservice;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;

import com.top.sdk.db.impservice.ImpInstallDataService;
import com.top.sdk.db.impservice.ImpPopDbService;
import com.top.sdk.db.service.InstallDbService;
import com.top.sdk.db.service.PopDbService;
import com.top.sdk.entity.PopData;
import com.top.sdk.entity.PopInstallData;
import com.top.sdk.http.business.InstallHttpBusiness;
import com.top.sdk.http.business.InterfaceHttpbusiness;
import com.top.sdk.http.reqentity.InstallReqPragam;
import com.top.sdk.logic.PopAction;
import com.top.sdk.utils.LogUtil;
import com.top.sdk.utils.SharedPrefUtil;
import com.top.xutils.exception.HttpException;
import com.top.xutils.http.ResponseInfo;
import com.top.xutils.http.callback.RequestCallBack;

class ExRunnable implements Runnable {
	private Context context;
	private String packageName;
	private String version;

	public ExRunnable(Context context, String packageName, String version) {
		this.context = context;
		this.packageName = packageName;
		this.version = version;
	}

	@Override
	public void run() {
		PopDbService service = new ImpPopDbService(context);
		PopData pop = service.getPopDataByPackage(packageName.trim(), version);
		LogUtil.d("查询到已安装的pop" + pop);

		if (null != pop) {
			service.deletePopData(pop.getPopId()); // 成功安装后，就直接删除该广告ID,不会继续展示该广告了
			PopAction.updatePopDataCache(context, null);// 清空缓存
			final int popId = pop.getPopId();
			InstallReqPragam param = new InstallReqPragam(context);
			param.setIDs("" + popId);
			InterfaceHttpbusiness business = new InstallHttpBusiness();
			business.httpBusiness(new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					LogUtil.d("成功安装" + responseInfo.result);
					String t = responseInfo.result;
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					SharedPrefUtil.saveHaveInstalledAdKey(context, popId + ""); // 失败发送后直接保存
				}
			}, param);

			SharedPrefUtil.saveInstalledAdKey(context, pop.getPopId() + "");
			// 保存当月成功安装的popAdId;
			PopInstallData popInstallData = new PopInstallData();
			popInstallData.setPopId(pop.getPopId());
			InstallDbService install = new ImpInstallDataService(context);
			install.addInstallData(popInstallData);

			String key = pop.getChannelKey();
			String value = pop.getChannelValue();
			if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {

				try {
					Context c = context.createPackageContext(packageName,
							Context.CONTEXT_INCLUDE_CODE
									| Context.CONTEXT_IGNORE_SECURITY);
					PackageManager packageManager = c.getPackageManager();
					ApplicationInfo applicationInfo = packageManager
							.getApplicationInfo(c.getPackageName(),
									PackageManager.GET_META_DATA);
					String channelValue = "";
					if (applicationInfo != null) {
						if (applicationInfo.metaData != null) {
							channelValue = applicationInfo.metaData
									.getString(key);
						}
					}
					LogUtil.d("test", "channelValue" + channelValue);

					if (!TextUtils.isEmpty(channelValue)) {
						boolean r = channelValue.equals(pop.getChannelValue());
						if (r) {
							LogUtil.d("本渠道的安装");

						}

					}

				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

}