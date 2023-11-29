package com.busteamproject.notification;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.busteamproject.AppConst;
import com.busteamproject.R;

import java.util.List;

public class NotificationHelper {
	public static final String alarmChannelName = "진동 알림";
	public static final String silentChannelName = "진행 알림";
	public static final String alarmChannelId = "alarm_channel_id";
	public static final String silentChannelId = "silent_channel_id";
	public static final int notificationId = 48206475;

	private String title;
	private String message;
	private int busTime;
	private int walkTime;
	private int defaultTime;

	public NotificationHelper(){}
	public NotificationHelper(String title, String message, int busTime, int walkTime, int defaultTime) {
		this.title = title;
		this.message = message;
		this.busTime = busTime;
		this.walkTime = walkTime;
		this.defaultTime = defaultTime;
	}

	public void start(Context context) {
		Intent intent = new Intent(context, BusAlarmService.class);
		if(isServiceRunning(context)) {
			return;
		}
		intent.setAction(AppConst.SERVICE_START);
		intent.putExtra(AppConst.NOTIFICATION_TITLE, title);
		intent.putExtra(AppConst.NOTIFICATION_MSG, message);
		intent.putExtra(AppConst.NOTIFICATION_BUS_TIME, busTime);
		intent.putExtra(AppConst.NOTIFICATION_WALK_TIME, walkTime);
		intent.putExtra(AppConst.NOTIFICATION_DEFAULT_TIME, defaultTime);
		context.startForegroundService(intent);
	}

	public void stop(Context context) {
		Intent intent = new Intent(context, BusAlarmService.class);
		if(isServiceRunning(context)) {
			context.stopService(intent);
		}
	}

	public boolean isServiceRunning(Context context) {
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

	public static void createVibrateChannel(NotificationManager notificationManager) {
		NotificationChannel notificationChannel = notificationManager.getNotificationChannel(alarmChannelId);
		if(notificationChannel == null) {
			notificationChannel = new NotificationChannel(alarmChannelId, alarmChannelName, NotificationManager.IMPORTANCE_HIGH);
			notificationChannel.enableLights(true);
			notificationChannel.enableVibration(true);
			notificationChannel.setShowBadge(false);
			notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
			notificationManager.createNotificationChannel(notificationChannel);
		}
	}

	public static void createSilentChannel(NotificationManager notificationManager) {
		NotificationChannel notificationChannel = notificationManager.getNotificationChannel(silentChannelId);
		if(notificationChannel == null) {
			notificationChannel = new NotificationChannel(silentChannelId, silentChannelName, NotificationManager.IMPORTANCE_HIGH);
			notificationChannel.enableLights(false);
			notificationChannel.enableVibration(false);
			notificationChannel.setShowBadge(false);
			notificationManager.createNotificationChannel(notificationChannel);
		}
	}

	public static Notification getNotification(Context context, String title, String message, PendingIntent pendingIntent, boolean isSilent) {
		NotificationCompat.Builder builder;
		if(isSilent) {
			builder = new NotificationCompat.Builder(context, silentChannelId);
		} else {
			builder = new NotificationCompat.Builder(context, alarmChannelId);
		}

		builder.setContentTitle(title)
//				.setContentText(message)
				.setPriority(NotificationCompat.PRIORITY_HIGH)
				.setWhen(0)
				.setContentIntent(pendingIntent)
				.setStyle(new NotificationCompat.BigTextStyle().bigText(message))
				.setSmallIcon(R.drawable.bus_alert_48px);

		if(isSilent) {
			builder.setSilent(true);
		}

		return builder.build();
	}
}
