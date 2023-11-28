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
	private int walkTime = 0;
	private boolean accessRunning = true;

	private final class ServiceHandler extends Handler {
		public ServiceHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			while (walkTime > 0 && accessRunning) {
				try {
					Thread.sleep(1000);
					busTime = busTime - 1;
					walkTime = walkTime - 1;
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
			completeNotification(!accessRunning, 1);
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
		if(walkTime / 60 > 0) {
			walkTimeString = String.format("%d분 %d초", walkTime / 60, walkTime % 60);
		} else {
			walkTimeString = String.format("%d초", walkTime % 60);
		}

		String msg = String.format("%s (%s 후 도착)\n%s 후 출발 알림 발생", message, busTimeString, walkTimeString);
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
		if(!isForceStop) {
			if(type == 0) {
				notificationMsg = String.format("%s\n정류소로 출발하세요!", msg, busTime);
			} else {
				notificationMsg = String.format("%s\n버스가 도착 예정입니다!", msg, busTime);
			}
		} else {
			notificationMsg = "알림이 수동으로 종료되었습니다.";
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
		if(walkTime / 60 > 0) {
			walkTimeString = String.format("%d분 %d초", walkTime / 60, walkTime % 60);
		} else {
			walkTimeString = String.format("%d초", walkTime % 60);
		}

		String notificationMsg;
		if(!isComplete) {
			notificationMsg = String.format("%s (%s 후 도착)\n%s 후 출발 알림 발생", message, busTimeString, walkTimeString);
		} else {
			notificationMsg = String.format("%s (%s 후 도착)\n정류소로 출발하세요!", message, busTimeString);
		}

		Notification notification = NotificationHelper.getNotification(
				this, title,notificationMsg, pendingIntent, true);
		notificationManager.notify(NotificationHelper.notificationId, notification);
	}
}
