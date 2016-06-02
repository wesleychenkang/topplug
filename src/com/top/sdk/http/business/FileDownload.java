package com.top.sdk.http.business;

import java.io.File;

import android.content.Context;
import android.net.Network;
import android.os.Environment;

import com.top.sdk.apputils.PackageUtils;
import com.top.sdk.apputils.RootPermission;
import com.top.sdk.db.impservice.ImpFileInfoDbService;
import com.top.sdk.db.service.FileInfoDbService;
import com.top.sdk.entity.Constants;
import com.top.sdk.entity.FileInfo;
import com.top.sdk.entity.PopData;
import com.top.sdk.http.reqentity.DownReqPragam;
import com.top.sdk.log.LogUtil;
import com.top.sdk.utils.NetWorkUtils;
import com.top.sdk.utils.SharedPrefUtil;
import com.top.xutils.HttpUtils;
import com.top.xutils.exception.HttpException;
import com.top.xutils.http.ResponseInfo;
import com.top.xutils.http.callback.RequestCallBack;

public class FileDownload {
	private Context context;

	public FileDownload(Context context) {
		this.context = context;
	}

	public void download(PopData pop) {
		if (NetWorkUtils.isWifiNetWork(context)) { // wifi的条件下去下载APP
			String target = String.valueOf(pop.getPopUrl().hashCode());
			File file = new File(Constants.FOLDER_DOWNLOAD + File.separatorChar
					+ target);
			HttpUtils http = new HttpUtils();
			http.download(pop.getPopUrl(), file.getAbsolutePath(),
					new FileRequestCallBack(pop));
			SharedPrefUtil.setAdDownLoading(context, 1);
		}
	}

	class FileRequestCallBack extends RequestCallBack<File> {

		private PopData pop;

		public FileRequestCallBack(PopData pop) {
			this.pop = pop;
		}

		@Override
		public void onSuccess(ResponseInfo<File> responseInfo) {
			FileInfo info = new FileInfo();
			info.setFileId(pop.getPopId());
			info.setFileName(String.valueOf(pop.getPopUrl().hashCode()));
			info.setFileUrl(pop.getPopUrl());
			info.setIsSuccess(0); // 更新下载成功的应用
			FileInfoDbService service = new ImpFileInfoDbService(context);
			service.updateFileInfo(info);
			SharedPrefUtil.setAdDownLoading(context, 0);
			
			InterfaceHttpbusiness business = new DownHttpBusiness();
			DownReqPragam req = new DownReqPragam(context);
			req.setIDs(""+pop.getPopId());
			business.httpBusiness(new RequestCallBack<String>() {
				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					// TODO Auto-generated method stub
					LogUtil.d("成功发送已下载"+responseInfo.result);
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					// TODO Auto-generated method stub
					
				}
			}, req);
           
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			// TODO Auto-generated method stub
			// 下载失败也无需统计
			if (error != null && error.getExceptionCode() == 416) {
				FileInfo info = new FileInfo();
				info.setFileId(pop.getPopId());
				info.setFileName(String.valueOf(pop.getPopUrl().hashCode()));
				info.setFileUrl(pop.getPopUrl());
				info.setIsSuccess(0); // 默认都没有下载成功；
				FileInfoDbService service = new ImpFileInfoDbService(context);
				service.updateFileInfo(info);
			}

			SharedPrefUtil.setAdDownLoading(context, 0);
			LogUtil.d("下载失败----->"+error.getExceptionCode()+"---> message--->"+msg);
		}

		@Override
		public void onLoading(long total, long current, boolean isUploading) {
               // LogUtil.d("total--->"+total+"===current===="+current);
		}

	}
}
