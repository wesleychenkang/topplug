package com.top.sdk.actvity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.top.sdk.entity.PopData;
import com.top.sdk.log.LogUtil;
import com.top.sdk.plugservice.PopSevice;
@SuppressLint("NewApi")
public class PopActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		PopSevice popShow = new PopSevice();
		View view = popShow.showView(this);
		if (view != null) {
			setContentView(view);
			popShow.setViewOnClick(view, this, getApplicationContext());
		} else {
			LogUtil.d("直接填充失败");
			finish();
		}
	}
}
