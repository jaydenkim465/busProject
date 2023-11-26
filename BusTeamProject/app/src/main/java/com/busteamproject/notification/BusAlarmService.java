package com.busteamproject.notification;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.os.Process;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.busteamproject.AppConst;
import com.busteamproject.R;
import com.busteamproject.view.MainActivity;

public class BusAlarmService extends Service {
	private Looper serviceLooper;
	private ServiceHandler serviceHandler;
	private String title = "";
	private String message = "";
	private int seconds = 0;
	private boolean accessRunning = true;

	// Handler that receives messages from the thread
	private final class ServiceHandler extends Handler {
		public ServiceHandler(Looper looper) {
			super(looper);
		}
		@Override
		public void handleMessage(Message msg) {
			while (seconds > 0 && accessRunning) {
				try {
					Thread.sleep(1000);
					seconds = seconds - 1;
					updateForegroundNotification();
				} catch (InterruptedException e) {
					// Restore interrupt status.
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	@Override
	public void onCreate() {
		HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();
		serviceLooper = thread.getLooper();
		serviceHandler = new ServiceHandler(serviceLooper);
	}

	private final String channelName = "TEST_CHANNEL";
	private final String channelId = "test_notification_channel";

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(intent.getAction().equals(AppConst.SERVICE_START)) {
			title = intent.getExtras().getString("title");
			message = intent.getExtras().getString("message");
			seconds = intent.getExtras().getInt("time");

			startForegroundNotification(title, message);

			Message msg = serviceHandler.obtainMessage();
			msg.arg1 = startId;
			serviceHandler.sendMessage(msg);
		} else if(intent.getAction().equals(AppConst.SERVICE_UPDATE)) {
			title = intent.getExtras().getString("title");
			message = intent.getExtras().getString("message");
			seconds = intent.getExtras().getInt("time");
			updateForegroundNotification();

			Message msg = serviceHandler.obtainMessage();
			msg.arg1 = startId;
			serviceHandler.sendMessage(msg);
		}

		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		accessRunning = false;
	}

	private Notification notification;
	private NotificationManager notificationManager;
	private int notificationId = 48206475;

	private void startForegroundNotification(String title, String message) {
		Intent mainLanding = new Intent(this, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mainLanding, PendingIntent.FLAG_IMMUTABLE);
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
		notificationChannel.enableLights(true);
		notificationChannel.enableVibration(true);
		notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
		notificationManager.createNotificationChannel(notificationChannel);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
		builder.setContentTitle(title)
				.setContentText(String.format("%s\n%d초", message, seconds))
				.setPriority(NotificationCompat.PRIORITY_MAX)
				.setWhen(0)
				.setContentIntent(pendingIntent)
				.setOngoing(true)
				.setSmallIcon(R.drawable.ic_launcher_foreground);

		notification = builder.build();
		startForeground(notificationId, notification);
		notificationManager.notify(notificationId, notification);
		Log.i(this.getClass().getName(), String.format("Notification Start : %s\n%d초", message, seconds));
	}

	private void updateForegroundNotification() {
		Intent mainLanding = new Intent(this, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mainLanding, PendingIntent.FLAG_IMMUTABLE);
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationChannel notificationChannel = new NotificationChannel(channelId + "_2", channelName, NotificationManager.IMPORTANCE_HIGH);
		notificationChannel.enableLights(false);
		notificationChannel.enableVibration(false);
		notificationManager.createNotificationChannel(notificationChannel);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId + "_2");
		builder.setContentTitle(title)
				.setContentText(String.format("%s\n%d초", message, seconds))
				.setPriority(NotificationCompat.PRIORITY_MAX)
				.setWhen(0)
				.setSilent(true)
				.setContentIntent(pendingIntent)
				.setSmallIcon(R.drawable.ic_launcher_foreground);

		notification = builder.build();
		notificationManager.notify(notificationId, notification);
		Log.i(this.getClass().getName(), String.format("Notification Update : %s\n%d초", message, seconds));
	}
}
