package com.top.sdk.actvity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;

import com.top.sdk.log.LogUtil;
import com.top.sdk.plugservice.PopSevice;

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
		registerComponent();

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

	private void registerComponent() {
		// 灭屏和亮屏的广播注册
		IntentFilter mScreenOnOrOffFilter = new IntentFilter();
		mScreenOnOrOffFilter.addAction("android.intent.action.SCREEN_ON");
		mScreenOnOrOffFilter.addAction("android.intent.action.SCREEN_OFF");
		LTService.this.registerReceiver(mScreenOnOrOffReceiver,
				mScreenOnOrOffFilter);
	}

	private BroadcastReceiver mScreenOnOrOffReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 屏幕开屏需要做什么
			if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
				// 屏幕开启需要做什么
				if (null != servie) {
					servie.screenChange(context, true);
				}

			} else if (intent.getAction().equals(
					"android.intent.action.SCREEN_OFF")) {
				// 屏幕关闭
				if (null != servie) {
					servie.screenChange(context, false);
				}
			}
		}

	};

	public void onDestroy() {
		unregisterReceiver(mScreenOnOrOffReceiver);
		
	};
}
