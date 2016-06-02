package com.top.sdk.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v4.widget.SlidingPaneLayout.PanelSlideListener;
import android.text.TextUtils;

import com.top.sdk.db.impservice.ImpPopDbService;
import com.top.sdk.db.service.PopDbService;
import com.top.sdk.entity.PopData;
import com.top.sdk.log.LogUtil;

/**
 * 广告相关
 * 
 * @author Administrator
 * 
 */
public class PopAction {
     
	public static String WHITE = 
		"com.ijinshan.duba,com.cleanmaster.security_cn,com.tencent.token,com.kms.free,project.rising,"+
    	"com.tencent.qqpimsecure,com.lbe.security,com.anguanjia.safe,cn.opda.a.phonoalbumshoushou,"+
    	"com.lenovo.safecenter,com.qihoo360.mobilesafe,com.cleanmaster.mguard_cn,com.ijinshan.mguard,"+
    	"com.qihoo.browser,com.qihoo.haosou,com.tencent.mobileqq,com.tencent.mtt,com.tencent.mm,"+
    	"com.tencent.android.qqdownloader,com.tencent.news,com.tencent.map,com.tencent.androidqqmail,"+
    	"com.qzone,com.tencent.qqgame,com.qq.reader,com.tencent.hd.qq,com.tencent.Wblog,com.tencent.qqlive,"+
    	"com.tencent.QQLottery,com.tencent.qqpim,com.tencent.qqmusic,com.baidu.searchbox,com.baidu.browser.apps,"+
    	"com.baidu.appsearch,com.baidu.tieba,com.baidu.video,com.baidu.BaiduMap,com.baidu.netdisk,"+
    	"com.ting.mp3.android,bdmobile.android.app,com.baidu.iknow,com.baidu.yuedu,com.baidu.wenku,"+
    	"com.baidu.baidutranslate,cn.jingling.motu.photowonder,com.cmbchina.ccd.pluto.cmbActivity";

	public static String whiteData = "com.android.vending,com.huawei.gamebox,com.oppo.market,com.pp.assistant,com.letv.letvshop,com.gionee.aora.market,com.lenovo.leos.appstore,com.mappn.gfan,com.meitu.appmarket,com.letv.app.appstore,com.huawei.appmarket,com.meizu.mstore,com.qihoo.gameunion,com.aspire.mmui,com.aspire.mmservice,com.qihoo.appstore,com.xiaomi.gamecenter,com.xiaomi.market,com.tencent.android.qqdownloader,com.bbk.appstore,com.egame,com.wandoujia.phoenix2,com.infinit.wostore.u,cn.emagsoftware.gamehall,com.aspire.mm,com.muzhiwan.market,com.vivo.game,com.sogou.androidtool,cn.goapk.market,com.qihoo.secstore,com.xiaomi.shop";
    public static String  allSystem = "";
	/**
	 * 定义一个内存缓存数据
	 */
	public static List<PopData> listCache = new ArrayList<PopData>();

	/**
	 * 包名是否符合
	 * 
	 * @param packageName
	 * @return
	 */
	public static boolean checkPackageName(Context context, String packageName) {
		boolean result = canShowAD(context, packageName);
		if (result) {
			return true;
		} else {
			return checkWhitePackageName(packageName);
		}
	}

	public static boolean canShowAD(Context context, String pName) {
		checkSystemPakcageName(context);
		// 过滤掉一些系统应用
		if (pName.contains("phone") || pName.contains("camera")
				|| pName.contains("clock") || pName.contains("dialer") || pName.contains("android.process.acore") || pName.contains("com.miui.home")||pName.contains("android.process.media")) {
			return false;
		}
		if(allSystem.contains(pName)){
			return false;
		}
		// 过滤掉桌面
		if (isLauncherRunnig(context)) {
			return false;
		}
		// 过滤掉systemui和自己
		if (isSystemUiOrMyself(pName, context)) {
			return false;
		}
		if(pName.contains(":")){
			// 监测到当前是进程类的
			return false;
		}
		if(WHITE.contains(pName)){
			return false;
		}
		// 过滤白名单,暂时没有列入

		return true;
	}

	private static boolean isSystemUiOrMyself(String pName, Context context) {
		if (pName.equalsIgnoreCase(context.getPackageName())
				|| pName.equalsIgnoreCase("com.android.systemui"))
			return true;
		else
			return false;
	}

	private static boolean isLauncherRunnig(Context context) {
		boolean result = false;
		List<String> names = getAllTheLauncher(context);
		ActivityManager mActivityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> appList = mActivityManager
				.getRunningAppProcesses();
		for (RunningAppProcessInfo running : appList) {
			if (running.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				for (int i = 0; i < names.size(); i++) {
					if (names.get(i).equals(running.processName)) {
						result = true;
						break;
					}
				}
			}
		}
		return result;
	}

	private static List<String> getAllTheLauncher(Context context) {
		List<String> names = null;
		PackageManager pkgMgt = context.getPackageManager();
		Intent it = new Intent(Intent.ACTION_MAIN);
		it.addCategory(Intent.CATEGORY_HOME);
		List<ResolveInfo> ra = pkgMgt.queryIntentActivities(it, 0);
		if (ra.size() != 0) {
			names = new ArrayList<String>();
		}
		for (int i = 0; i < ra.size(); i++) {
			String packageName = ra.get(i).activityInfo.packageName;
			names.add(packageName);
		}
		return names;
	}

	/**
	 * 检查包名是否在白名单(应用商店列表);
	 * 
	 * @param packageName
	 * @return
	 */
	public static boolean checkWhitePackageName(String packageName) {
		if (whiteData.contains(packageName)) {
			return true;
		}
		return false;
	}
	
	private static void checkSystemPakcageName(Context context){
		if(TextUtils.isEmpty(allSystem)){
		List<PackageInfo> packages = context.getPackageManager()
				.getInstalledPackages(0);
		if(packages!=null){
		StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < packages.size(); i++) {

			PackageInfo packageInfo = packages.get(i);
			String packageName = packageInfo.packageName;
			// 系统程序
			if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
				builder.append(packageName);
				builder.append(",");
			}
		}
		allSystem = builder.toString();
		LogUtil.d("系统级别的应用"+allSystem);
		}
	}
	}

	public static boolean checkChannelName(String channnelKey,
			String channelName) {

		return true;
	}

	public static List<PopData> getShowPopDataFromCache(Context context) {
		// 首先先从缓存中取数据
		if (null != listCache && listCache.size() > 0) {
			return listCache;
		}
		PopDbService service = new ImpPopDbService(context);
		listCache = service.getCanShowPopDataList();
		// 再从数据库中取数据
		return listCache;
	}

	public static void updatePopDataCache(Context context,
			List<PopData> listData) {
		listCache = listData;
		PopDbService service = new ImpPopDbService(context);
		if (listData != null && listData.size() > 0) {
			service.updateListPopData(listData);
		}
		if (listData != null && listData.size() == 0) {
			service.deleteAllPopData();
		}
	}

}
