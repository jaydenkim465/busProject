package com.busteamproject.notification;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.os.Process;

import androidx.annotation.Nullable;

import com.busteamproject.AppConst;
import com.busteamproject.view.MainActivity;

public class BusAlarmService extends Service {
	private Looper serviceLooper;
	private ServiceHandler serviceHandler;
	private String title = "";
	private String message = "";
	private int busTime = 0;
	private int alramTime = 0;
	private int walkTime = 0;
	private int defaultTime = 0;
	private boolean accessRunning = true;
	private boolean isProccessEnd = false;
	String infoString = "";

	private final class ServiceHandler extends Handler {
		public ServiceHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			isProccessEnd = false;
			while (alramTime > 0 && accessRunning) {
				try {
					Thread.sleep(1000);
					busTime = busTime - 1;
					alramTime = alramTime - 1;
					updateForegroundNotification(false);
				} catch (InterruptedException e) {
					// Restore interrupt status.
					Thread.currentThread().interrupt();
				}
			}
			completeNotification(!accessRunning, 0);

			if(!accessRunning) {
				return;
			}

			try {
				Thread.sleep(3000);
				busTime = busTime - 3;
			} catch (InterruptedException e) {
				// Restore interrupt status.
				Thread.currentThread().interrupt();
			}

			while (busTime > 0 && accessRunning) {
				try {
					Thread.sleep(1000);
					busTime = busTime - 1;
					updateForegroundNotification(true);
				} catch (InterruptedException e) {
					// Restore interrupt status.
					Thread.currentThread().interrupt();
				}
			}
			isProccessEnd = true;
			stopSelf();
		}
	}

	@Override
	public void onCreate() {
		HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();
		serviceLooper = thread.getLooper();
		serviceHandler = new ServiceHandler(serviceLooper);
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		title = intent.getExtras().getString(AppConst.NOTIFICATION_TITLE);
		message = intent.getExtras().getString(AppConst.NOTIFICATION_MSG);
		busTime = intent.getExtras().getInt(AppConst.NOTIFICATION_BUS_TIME);
		walkTime = intent.getExtras().getInt(AppConst.NOTIFICATION_WALK_TIME);
		defaultTime = intent.getExtras().getInt(AppConst.ALARM_DEFAULT_TIME);

		alramTime = busTime - (defaultTime + walkTime);

		infoString = String.format("도보거리 : %d초 / 기본 시간 : %d초", walkTime, defaultTime);

		startForegroundNotification(title, message);
		Message msg = serviceHandler.obtainMessage();
		msg.arg1 = startId;
		serviceHandler.sendMessage(msg);

		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		accessRunning = false;
		completeNotification(!accessRunning, 1);
	}

	private void startForegroundNotification(String title, String message) {
		Intent mainLanding = new Intent(this, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mainLanding, PendingIntent.FLAG_IMMUTABLE);
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationHelper.createVibrateChannel(notificationManager);

		String busTimeString;
		if(busTime / 60 > 0) {
			busTimeString = String.format("%d분 %d초", busTime / 60, busTime % 60);
		} else {
			busTimeString = String.format("%d초", busTime % 60);
		}

		String walkTimeString;
		if(alramTime / 60 > 0) {
			walkTimeString = String.format("%d분 %d초", alramTime / 60, alramTime % 60);
		} else {
			walkTimeString = String.format("%d초", alramTime % 60);
		}

		String msg = String.format("%s (%s 후 도착)\n%s 후 출발 알림 발생\n%s", message, busTimeString, walkTimeString, infoString);
		Notification notification = NotificationHelper.getNotification(
				this, title, msg, pendingIntent, false);
		startForeground(NotificationHelper.notificationId, notification);
		notificationManager.notify(NotificationHelper.notificationId, notification);
	}

	private void completeNotification(boolean isForceStop, int type) {
		Intent mainLanding = new Intent(this, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mainLanding, PendingIntent.FLAG_IMMUTABLE);
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationHelper.createVibrateChannel(notificationManager);

		String busTimeString;
		if(busTime / 60 > 0) {
			busTimeString = String.format("%d분 %d초", busTime / 60, busTime % 60);
		} else {
			busTimeString = String.format("%d초", busTime % 60);
		}

		String msg = String.format("%s (%s 후 도착)", message, busTimeString);
		String notificationMsg;
		if(isForceStop && !isProccessEnd) {
			notificationMsg = "알림이 수동으로 종료되었습니다.\n" + infoString;
		} else {
			if(type == 0) {
				notificationMsg = String.format("%s\n정류소로 출발하세요!\n%s", msg, infoString);
			} else {
				notificationMsg = String.format("%s\n버스가 도착 예정입니다!\n%s", msg, infoString);
			}
		}

		Notification notification = NotificationHelper.getNotification(
				this, title, notificationMsg, pendingIntent, false);
		notificationManager.notify(NotificationHelper.notificationId, notification);
	}

	private void updateForegroundNotification(boolean isComplete) {
		Intent mainLanding = new Intent(this, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mainLanding, PendingIntent.FLAG_IMMUTABLE);
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationHelper.createSilentChannel(notificationManager);

		String busTimeString;
		if(busTime / 60 > 0) {
			busTimeString = String.format("%d분 %d초", busTime / 60, busTime % 60);
		} else {
			busTimeString = String.format("%d초", busTime % 60);
		}

		String walkTimeString;
		if(alramTime / 60 > 0) {
			walkTimeString = String.format("%d분 %d초", alramTime / 60, alramTime % 60);
		} else {
			walkTimeString = String.format("%d초", alramTime % 60);
		}

		String notificationMsg;
		if(!isComplete) {
			notificationMsg = String.format("%s (%s 후 도착)\n%s 후 출발 알림 발생\n%s", message, busTimeString, walkTimeString, infoString);
		} else {
			notificationMsg = String.format("%s (%s 후 도착)\n정류소로 출발하세요!\n%s", message, busTimeString, infoString);
		}

		Notification notification = NotificationHelper.getNotification(
				this, title,notificationMsg, pendingIntent, true);
		notificationManager.notify(NotificationHelper.notificationId, notification);
	}
}
