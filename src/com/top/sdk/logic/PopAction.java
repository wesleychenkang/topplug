package com.top.sdk.logic;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.top.sdk.db.impservice.ImpPopDbService;
import com.top.sdk.db.service.PopDbService;
import com.top.sdk.entity.PopData;

/**
 * 广告相关
 * 
 * @author Administrator
 * 
 */
public class PopAction {

	public static String whiteData = "com.android.vending,com.huawei.gamebox,com.oppo.market,com.pp.assistant,com.letv.letvshop,com.gionee.aora.market,com.lenovo.leos.appstore,com.mappn.gfan,com.meitu.appmarket,com.letv.app.appstore,com.huawei.appmarket,com.meizu.mstore,com.qihoo.gameunion,com.aspire.mmui,com.aspire.mmservice,com.qihoo.appstore,com.xiaomi.gamecenter,com.xiaomi.market,com.tencent.android.qqdownloader,com.bbk.appstore,com.egame,com.wandoujia.phoenix2,com.infinit.wostore.u,cn.emagsoftware.gamehall,com.aspire.mm,com.muzhiwan.market,com.vivo.game,com.sogou.androidtool,cn.goapk.market,com.qihoo.secstore";

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
	public static boolean checkPackageName(String packageName) {
		boolean r = checkWhitePackageName(packageName);
		// 检查当前的包名是否在所包含的应用商店里面
		return r;

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

	public static boolean checkChannelName(String channnelKey,
			String channelName) {

		return true;
	}

	public static List<PopData> getShowPopDataFromCache(Context context) {
		// 首先先从缓存中取数据
		if (null!=listCache&&listCache.size() > 0) {
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
		if(listData != null && listData.size()==0){
			service.deleteAllPopData();
		}
	}

}
