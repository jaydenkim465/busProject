package com.busteamproject.notification;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import com.busteamproject.AppConst;

import java.util.List;

public class NotificationHelper {
	private String title;
	private String message;
	private int time;

	public NotificationHelper(String title, String message, int time) {
		this.title = title;
		this.message = message;
		this.time = time;
	}

	public void start(Context context) {
		Intent intent = new Intent(context, BusAlarmService.class);
		if(isServiceRunning(context)) {
			context.stopService(intent);
		}
		intent.setAction(AppConst.SERVICE_START);
		intent.putExtra("title", title);
		intent.putExtra("message", message);
		intent.putExtra("time",20);
		context.startForegroundService(intent);
	}

	public void update(Context context) {
		Intent intent = new Intent(context, BusAlarmService.class);
		if(isServiceRunning(context)) {
			context.stopService(intent);
		}
		intent.setAction(AppConst.SERVICE_UPDATE);
		intent.putExtra("title", title);
		intent.putExtra("message", message);
		intent.putExtra("time",time);
		context.startForegroundService(intent);
	}

	private boolean isServiceRunning(Context context) {
		try {
			ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			List<ActivityManager.RunningServiceInfo> serviceList = manager.getRunningServices(Integer.MAX_VALUE);
			for (ActivityManager.RunningServiceInfo serviceInfo : serviceList) {
				if(serviceInfo.service.getClassName().equals(BusAlarmService.class.getName())) {
					return true;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
}
