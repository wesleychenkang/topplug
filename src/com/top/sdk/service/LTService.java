package com.top.sdk.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

import com.top.sdk.plugservice.PopSevice;
import com.top.sdk.utils.LogUtil;

public class LTService extends Service {
	private static long popShowTime;
	private long startAlarm = 20000;
	private long verTime = 10 * 1000 * 60; // 时间间隔
	private PopSevice servie;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		LogUtil.d("app", "----oncreate-----");
		servie = new PopSevice();
		servie.startListenting(getApplicationContext());
		setAlarmManager();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LogUtil.d("app", "----onstartCommd-----");
		servie.requestPop(getApplicationContext());
		return super.onStartCommand(intent, flags, startId);

	}
	private void setAlarmManager() {
		Context context = getApplicationContext();
		Intent intent = new Intent(LTService.this, LTService.class);
		PendingIntent pend = PendingIntent.getService(context, 0, intent, 0);
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		long firstTime = SystemClock.currentThreadTimeMillis();
		am.setRepeating(AlarmManager.RTC_WAKEUP, firstTime, startAlarm, pend);
	}

}
