package com.top.sdk.plugservice;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public interface InterPopService {
	public View showView(Context context);

	public void setViewOnClick(View v, Activity activity, Context context);

	public void requestPop(Context context);

	public void startListenting(Context context);

	public void login(Context context);

	public Runnable getReciveRunnable(Context context, String packageName,
			String version);

	public void screenChange(Context context, boolean screenOpen);
	
	public void viewCallBack(Context context);
	
	public Runnable getRemoveRunnable(Context context,String packageName);
	

}
