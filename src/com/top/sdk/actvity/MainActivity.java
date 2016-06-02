package com.top.sdk.actvity;

import java.io.File;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import com.top.sdk.R;
import com.top.sdk.apputils.PackageUtils;
import com.top.sdk.apputils.RootPermission;
import com.top.sdk.db.DBHelper;
import com.top.sdk.db.impservice.ImpPopDbService;
import com.top.sdk.db.impservice.ImpWhiteDbService;
import com.top.sdk.db.service.PopDbService;
import com.top.sdk.db.service.WhiteDbService;
import com.top.sdk.entity.Constants;
import com.top.sdk.entity.DeviceInfo;
import com.top.sdk.entity.PopData;
import com.top.sdk.entity.WhiteData;
import com.top.sdk.http.business.InterfaceHttpbusiness;
import com.top.sdk.http.business.PopHttpBusiness;
import com.top.sdk.http.business.UserHttpBusiness;
import com.top.sdk.http.reqentity.PopReqPragam;
import com.top.sdk.http.reqentity.UserReqPragam;
import com.top.sdk.http.respone.entity.PopResult;
import com.top.sdk.http.respone.parser.PopResultParser;
import com.top.sdk.log.LogUtil;
import com.top.sdk.logic.UserAction;
import com.top.xutils.HttpUtils;
import com.top.xutils.exception.HttpException;
import com.top.xutils.http.RequestParams;
import com.top.xutils.http.ResponseInfo;
import com.top.xutils.http.callback.RequestCallBack;
import com.top.xutils.http.client.HttpRequest.HttpMethod;

public class MainActivity extends Activity {
	private DBHelper openHelper;
	private SQLiteDatabase dataBase;
	private FrameLayout ly = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		ly = (FrameLayout)findViewById(R.id.container);
		ly.getBackground().setAlpha(0);
		ly.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
//		if (savedInstanceState == null) {
//			getSupportFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
//		}
		
		openHelper = DBHelper.getInstance(getApplicationContext());
		dataBase = openHelper.getWritableDatabase();

		DeviceInfo d = new DeviceInfo(getApplicationContext());
	}


	/**
	 * A placeholder fragment containing a simple view.
	 */
	@SuppressLint("ValidFragment")
	public class PlaceholderFragment extends Fragment implements
			OnClickListener {

		private Button btnAddPop;
		private Button btnAddWhite;
		private Button btnSelectAllPop;
		private Button btnSelectAllWhite;
		private Button btnUnInstall;
		private Button btnInstall;
		private Button btnInstallUn;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			btnAddPop = (Button) rootView.findViewById(R.id.addPop);
			btnAddPop.setOnClickListener(this);

			btnAddWhite = (Button) rootView.findViewById(R.id.addWhite);
			btnAddWhite.setOnClickListener(this);

			btnSelectAllPop = (Button) rootView.findViewById(R.id.selectAllPop);
			btnSelectAllPop.setOnClickListener(this);

			btnSelectAllWhite = (Button) rootView
					.findViewById(R.id.selectAllWhite);
			btnSelectAllWhite.setOnClickListener(this);

			btnUnInstall = (Button) rootView.findViewById(R.id.uninstall);
			btnUnInstall.setOnClickListener(this);

			btnInstall = (Button) rootView.findViewById(R.id.install);
			btnInstall.setOnClickListener(this);

			btnInstallUn = (Button) rootView.findViewById(R.id.install_un);
			btnInstallUn.setOnClickListener(this);
			return rootView;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.addPop:
			   // addPop();
				//popList();
				select(9);
				break;
			case R.id.addWhite:
				addWhite();
				break;
			case R.id.selectAllPop:
				selectAllPop();
				break;
			case R.id.selectAllWhite:
				selectAllWhite();
				break;
			case R.id.uninstall:
				uninstall("com.example.first");

				break;

			case R.id.install:
				install("First.apk");
				break;

			case R.id.install_un:
				install_uninstall("First.apk", "com.example.first");
				break;

			}
		}
	}

	private void addPop() {
		ContentValues values = new ContentValues();
		values.put("popType", 1);
		values.put(
				"popUrl",
				"https://raw.githubusercontent.com/LitePalFramework/LitePal/master/downloads/litepal-1.3.1.jar");
		values.put(
				"imgUrl",
				"http://e.hiphotos.baidu.com/image/pic/item/14ce36d3d539b600be63e95eed50352ac75cb7ae.jpg");
		values.put("channelName", "top25");
		values.put("packageName", "com.top.sdk");
		values.put("showCount", 1);
		values.put("showRate", 5);
		dataBase.insert("popdata", null, values);
		PopData popData = new PopData();
		popData.setPopType(2);
		popData.setPopUrl("https://raw.githubusercontent.com/LitePalFramework/LitePal/master/downloads/litepal-1.3.1.jar");
		popData.setImgUrl("http://e.hiphotos.baidu.com/image/pic/item/14ce36d3d539b600be63e95eed50352ac75cb7ae.jpg");
		popData.setChannelKey("top25");
		popData.setPackageName("com.top.sdk");
		popData.setShowRate(5);
		popData.setShowCount(1);
		popData.setVersion("2.0");
		PopDbService popService = new ImpPopDbService(getApplicationContext());
		popService.insertPopData(popData);

	}

	private void addWhite() {
		ContentValues values = new ContentValues();
		values.put("popId", 1);
		values.put("version", "2.0");
		values.put("channleKey", "channlekey");
		values.put("listPackageName", "com.top.sdk;com.top.sdk2;com.top.sdk3");
		dataBase.insert("whitedata", null, values);
		WhiteData data = new WhiteData();
		data.setPopId(2);
		data.setChannleKey("channlekey");
		data.setListPackageName("com.top.sdk;com.top.sdk2;com.top.sdk3");
		WhiteDbService whiteDb = new ImpWhiteDbService(getApplicationContext());
		whiteDb.insertWhiteData(data);
	}

	private void selectAllPop() {
		PopDbService popService = new ImpPopDbService(getApplicationContext());
		List<PopData> all = popService.getPopDataList();
		if (all != null) {
			for (int i = 0; i < all.size(); i++) {
				LogUtil.d(all.get(i).toString());
			}
		}

	}

	private void selectAllWhite() {
		WhiteDbService whiteDb = new ImpWhiteDbService(getApplicationContext());
		List<WhiteData> all = whiteDb.getWhiteDataList();
		if (all != null) {

			for (int i = 0; i < all.size(); i++) {
				LogUtil.d(all.get(i).toString());
			}
		}

	}

	private void install(final String apk) {
		if (!TextUtils.isEmpty(apk)) {
			new Thread() {

				public void run() {

					RootPermission.upgradeRootPermission(getPackageCodePath());
					String path = Environment.getExternalStorageDirectory()
							.getPath() + File.separator + apk;
					PackageUtils.install(getApplicationContext(), path);
				};

			}.start();

		}

	}

	private void uninstall(final String packageName) {
		new Thread() {
			public void run() {
				RootPermission.upgradeRootPermission(getPackageCodePath());
				PackageUtils.uninstall(getApplicationContext(), packageName);

			};

		}.start();

	}

	private void install_uninstall(final String apk, final String packageName) {
		if (!TextUtils.isEmpty(apk)) {
			new Thread() {

				public void run() {

					RootPermission.upgradeRootPermission(getPackageCodePath());
					String path = Environment.getExternalStorageDirectory()
							.getPath() + File.separator + apk;
					int reslt = PackageUtils
							.uninstall(getApplicationContext(), packageName);
					LogUtil.d("执行了卸载结果"+reslt);
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					int r = PackageUtils.install(getApplicationContext(), path);
					LogUtil.d("执行了安装"+r);

				};

			}.start();

		}

	}

	private void select(int id) {
      ImpPopDbService service = new ImpPopDbService(getApplicationContext());
      PopData data = service.getPopDataFromId(id);
      LogUtil.d("根据ID查询的结果为"+data);
	}

	private void register() {
     
		Log.d("test", "==================");
		InterfaceHttpbusiness business = new UserHttpBusiness();
		business.httpBusiness(new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// TODO Auto-generated method stub
                    Log.d("test", "成功了====="+responseInfo.result.toString());
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				Log.d("test", "连接失败------》"+msg);
			}

		}, new UserReqPragam(getApplicationContext()));

	}
	
	
	
	public void test(){
		HttpUtils http = new HttpUtils();
		RequestParams requst = new RequestParams();
		UserReqPragam user= new UserReqPragam(getApplicationContext());
		requst.addBodyParameter("appId","1");
		requst.addBodyParameter("header", user.toJson());
		// requst.addBodyParameter();
		http.send(HttpMethod.POST, Constants.URL_USER, requst,new RequestCallBack<String>(){

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				
				// TODO Auto-generated method stub
				Log.d("test", "成功了====="+responseInfo.result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				Log.d("test", "连接失败------》"+msg);
			}
			
		});
	}
	
	
	public void testRegister(){
		
		UserAction action = new UserAction();
		action.userActivation(getApplicationContext());
	}
	
	public void popList(){
		
		InterfaceHttpbusiness business = new PopHttpBusiness();
		business.httpBusiness(new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// TODO Auto-generated method stub
				PopResultParser parser = new PopResultParser();
				PopResult result = parser.parserJson(responseInfo.result);
				LogUtil.d("返回成功的数据"+result.toString());
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				LogUtil.d("访问失败--》"+msg);
			}
		}, new PopReqPragam(getApplicationContext()));
	}

}
