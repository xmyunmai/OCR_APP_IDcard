package com.yunmai.android.bcr.receiver;

import java.io.File;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import cc.ocr.yunapp.idcard.R;

/**
 * 下载接收器
 * 
 * @author Иεtwμ
 * 
 */
public class DownloadReceiver extends BroadcastReceiver {

	public static final String ACTION_DOWNLOAD = "com.yunmai.android.ocrIdcard.UPGRADE_DOWNLOAD";

	@Override
	public void onReceive(Context context, Intent intent) {

		String action = intent.getAction();
		Log.i("yexiaoli", "action=" + action);
		if (action != null && action.equals(ACTION_DOWNLOAD)) {
			Log.i("yexiaoli", "action");
			boolean exit = intent.getBooleanExtra("exit", false);
			long max = intent.getLongExtra("max", 1);
			long progress = intent.getLongExtra("progress", 0);
			String apkPath = intent.getStringExtra("apkPath");
			String appName = intent.getStringExtra("appName");
			String packageName = intent.getStringExtra("packageName");
			int id = intent.getIntExtra("id", 1001);

			NotificationManager notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			Notification notification = new Notification(R.drawable.ic_launcher,
					context.getString(R.string.download_ing), System.currentTimeMillis());
			notification.flags = Notification.FLAG_NO_CLEAR;
			Intent mIntent = new Intent();
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.bcr_notification_remoteview_download);
			remoteViews.setTextViewText(R.id.notification_download_title, appName);
			if (progress == max) {

				notification.flags = Notification.FLAG_AUTO_CANCEL;
				notificationManager.cancel(packageName, id);
				Intent in = new Intent();
				in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				in.setAction(android.content.Intent.ACTION_VIEW);
				in.setDataAndType(Uri.fromFile(new File(apkPath)), "application/vnd.android.package-archive");
				context.startActivity(in);
				return;
			} else {
				Log.i("yexiaoli", "progresselse");
				remoteViews.setTextViewText(R.id.notification_download_progress_percent, (progress * 100) / max + "%");

				double a = progress / (double) max;
				int hasUploadLength = (int) (a * 100);

				remoteViews.setProgressBar(R.id.notification_download_progress, 100, hasUploadLength, false);

				Log.e("test", "progress->" + hasUploadLength + "\n");

			}
			PendingIntent contentIntent = PendingIntent.getActivity(context, 0, mIntent,
					PendingIntent.FLAG_CANCEL_CURRENT);
			notification.setLatestEventInfo(context, "", "", contentIntent);

			notification.contentView = remoteViews;
			notification.when = 15880238873L;
			Log.i("yexiaoli", "exit=" + exit);
			if (!exit) {
				notificationManager.notify(packageName, id, notification);
			} else {
				notificationManager.cancel(packageName, id);
				Toast.makeText(context, appName + "  " + context.getString(R.string.download_receiver_error_msg),
						Toast.LENGTH_SHORT).show();
				// System.exit(0);
				// Intent mintent = new Intent();
				// mintent.setAction(ConnectionBroadCastReceiver.MOVE_TO_BACKGROUND_ACTION);
				// context.sendBroadcast(mintent);
			}
		}
	}

}
