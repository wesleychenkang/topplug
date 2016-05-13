package com.top.sdk;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.top.sdk.logic.UserAction;
import com.top.sdk.plugin.DownLoadDex;
import com.top.sdk.service.LTService;
import com.top.sdk.utils.DESCoder;
import com.top.sdk.utils.LogUtil;
import com.top.sdk.utils.SharedPrefUtil;

public class App extends Application {
	private final String url = "http://112.74.100.106/api.php/Home/index/versionUpdate";

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		LogUtil.d("app", "appplicat  oncreate");
		userAction(); // 用户激活
		CrashHandler handler = CrashHandler.getInstance();
		handler.init(getApplicationContext());
		SharedPrefUtil.setAdDownLoading(getApplicationContext(), -1);// 清空处理
		 loadService();

//		new Thread() {
//
//			public void run() {
//
//				checkVersion(getApplicationContext(), url);
//
//			};
//
//		}.start();
	}

	public void userAction() {
		UserAction user = new UserAction();
		user.userActivation(getApplicationContext());
	}

	private void checkVersion(Context context, String url) {
		// 通过网络请求，版本匹配
		int version = SharedPrefUtil.getPlugVersion(context);
		// 必然需要从网络拉取
		try {
			HttpPost request = new HttpPost(url);
			List<NameValuePair> listParmas = new ArrayList<NameValuePair>();
			listParmas.add(new BasicNameValuePair("channelId", "" + 1046));
			listParmas.add(new BasicNameValuePair("appId", "" + 1));
			listParmas.add(new BasicNameValuePair("sdkVer", "" + 1.0));
			listParmas.add(new BasicNameValuePair("version", "" + version));
			request.setEntity(new UrlEncodedFormEntity(listParmas, HTTP.UTF_8));
			// 发送请求
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(request);
			// 得到应答的字符串，这也是一个 JSON 格式保存的数据
			InputStream input = httpResponse.getEntity().getContent();
			StringBuffer builder = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					input));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
			JSONObject obj = new JSONObject(builder.toString());
			int code = obj.getInt("code");
			if (code == 0) {
				String json = DESCoder.ebotongDecrypto(obj.getString("data"));
				LogUtil.d("版本接口返回--》" + json);
				JSONObject j = new JSONObject(json);
				String loadUrl = j.getString("loadUrl");
				String md = j.getString("md");
				int v = j.getInt("version");
				downLoadDex(context, loadUrl, md, v);
			}

			// 生成 JSON 对象
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 检查本地dex文件是否存在
	private boolean checkFileIsExits() {

		return true;

	}

	// 下载dex文件
	private void downLoadDex(Context context, final String url, String md,
			int version) {
		if (checkFileIsExits()
				&& SharedPrefUtil.getPlugVersion(context) == version) {
			// 如果文件存在并且版本号一致 就可以直接加载本地有缓存
			return;
		}

		if ((checkFileIsExits() && SharedPrefUtil.getPlugVersion(context) != version)
				|| !checkFileIsExits()) {
			// 如果文件存在，并且版本号不一致,或者文件不存在
			new Thread() {
				public void run() {
					DownLoadDex load = new DownLoadDex();
					load.downLoadFile(url);

				};
			}.start();

		}

	}

	// 启动service
	private void loadService() {
		Intent intent = new Intent(this, LTService.class);
		startService(intent);
	}
}
