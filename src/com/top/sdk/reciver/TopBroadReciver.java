package com.top.sdk.reciver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.text.TextUtils;

import com.top.sdk.db.impservice.ImpInstallDataService;
import com.top.sdk.db.impservice.ImpPopDbService;
import com.top.sdk.db.impservice.ImpPopKeyDbService;
import com.top.sdk.db.service.InstallDbService;
import com.top.sdk.db.service.PopDbService;
import com.top.sdk.db.service.PopKeyDbService;
import com.top.sdk.entity.PopInstallData;
import com.top.sdk.entity.PopKey;
import com.top.sdk.http.business.InstallHttpBusiness;
import com.top.sdk.http.business.InterfaceHttpbusiness;
import com.top.sdk.http.reqentity.InstallReqPragam;
import com.top.sdk.log.LogUtil;
import com.top.sdk.logic.PopAction;
import com.top.sdk.utils.SharedPrefUtil;
import com.top.xutils.exception.HttpException;
import com.top.xutils.http.ResponseInfo;
import com.top.xutils.http.callback.RequestCallBack;

public class TopBroadReciver extends BroadcastReceiver {
	public Handler handler = new Handler();
	@Override
	public void onReceive(Context context, Intent intent) {
		final Context c = context;
		// 接收安装广播
		if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
			final String packageName = intent.getData().getSchemeSpecificPart();
			String versionName = "";
			try {
				versionName = context.getPackageManager().getPackageInfo(
						packageName, 0).versionName;
			} catch (NameNotFoundException e1) {
				e1.printStackTrace();
			}
			if (!TextUtils.isEmpty(packageName)) {
				LogUtil.d("监听到了安装应用====" + packageName);
				if (!packageName.equals("com.top.sdk")) {
					handler.post(new ExRunnable(context, packageName,versionName));
				}

			}

		}
		// 接收卸载广播
		if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
		}
		// 监听开机广播
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			// 在此需要启动服务
		}

	}

	class ExRunnable implements Runnable {
		private Context context;
		private String packageName;
        private String version;
		public ExRunnable(Context context, String packageName,String version) {
			this.context = context;
			this.packageName = packageName;
			this.version = version;
		}

		@Override
		public void run() {
			PopKeyDbService service = new ImpPopKeyDbService(context);
			PopKey pop = service.getPopKey(packageName,version);
			LogUtil.d("查询到已安装的pop" +"version=="+version+ pop);

			if (null != pop) {
				service.deletePopKey(""+pop.getAdKey()); 
				
				PopDbService popService = new ImpPopDbService(context);  //成功安装后，就直接删除该广告ID,不会继续展示该广告了
				popService.deletePopData(pop.getAdKey());
				
				PopAction.updatePopDataCache(context, null);//清空缓存
				final int popId = pop.getAdKey();
				InstallReqPragam param = new InstallReqPragam(context);
				param.setIDs(""+popId);
				InterfaceHttpbusiness business = new InstallHttpBusiness();
				business.httpBusiness(new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtil.d("成功安装" + responseInfo.result);
						String t = responseInfo.result;
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						SharedPrefUtil.saveHaveInstalledAdKey(context,popId + ""); //失败发送后直接保存
					}
				}, param);
                
				SharedPrefUtil.saveInstalledAdKey(context, pop.getAdKey() + "");
				//保存当月成功安装的popAdId;
				PopInstallData popInstallData = new PopInstallData();
				popInstallData.setPopId(pop.getAdKey());
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
							boolean r = channelValue.equals(pop
									.getChannelValue());
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

}
