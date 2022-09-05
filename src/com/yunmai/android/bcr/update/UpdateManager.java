package com.yunmai.android.bcr.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import cc.ocr.yunapp.idcard.R;

import com.yunmai.android.bcr.receiver.DownloadReceiver;

/**
 * 更新升级管理
 * 
 * @author Иεtwμ
 * 
 */
public class UpdateManager {

	private static String UPDATA_URL = "http://www.aipim.cn/publicfiles/ymUpgrade/androidOcr/OcrIdcard/ymupgrade.xml";
	public static Boolean isUpdating = false;

	public static VersionEntity getOnlineVersion() {
		VersionEntity versionEntity = null;
		PullVersionParser parser = null;
		InputStream is = null;
		try {

			URL url = new URL(UPDATA_URL);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("GET");
			httpConn.setConnectTimeout(30000);
			httpConn.setReadTimeout(30 * 1000);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			is = http.getInputStream();

			parser = new PullVersionParser();
			versionEntity = parser.parse(is);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
			// Toast.makeText(this, "网络不可用", Toast.LENGTH_SHORT).show();
		} finally {
			try {
				if (is != null) {

					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return versionEntity;
	}

	public static void showUpdataDialog(final Activity activity, final VersionEntity versionEntity) {

		final MyDialog versionUpdataDialog = new MyDialog(activity, R.style.myDialogTheme);
		LayoutInflater inflater = LayoutInflater.from(activity);
		final View view = inflater.inflate(R.layout.m_version_updata_dialog, null);

		TextView updata_content = (TextView) view.findViewById(R.id.updata_content);

		final TextView content_title = (TextView) view.findViewById(R.id.content_title);
		StringBuffer sb = new StringBuffer();

		Locale l = Locale.getDefault();

		if ("zh-CN".equals(String.format("%s-%s", l.getLanguage(), l.getCountry()))) {
			for (int i = 0; i < versionEntity.getChineseSimplifiedContent().size(); i++) {
				sb.append(versionEntity.getChineseSimplifiedContent().get(i).trim()).append("\n");
			}
		} else if ("en".equals(String.format("%s", l.getLanguage())))// 在系统语言为英文的情况下，选择英文升级版本
		{
			for (int i = 0; i < versionEntity.getEnglishContent().size(); i++) {
				sb.append(versionEntity.getEnglishContent().get(i).trim()).append("\n");
			}
		} else if ("zh-TW".equals(String.format("%s-%s", l.getLanguage(), l.getCountry()))) {
			for (int i = 0; i < versionEntity.getEnglishContent().size(); i++) {
				sb.append(versionEntity.getChineseTraditionalContent().get(i).trim()).append("\n");
			}
		} else if ("zh-HK".equals(String.format("%s-%s", l.getLanguage(), l.getCountry()))) {
			for (int i = 0; i < versionEntity.getEnglishContent().size(); i++) {
				sb.append(versionEntity.getChineseTraditionalContent().get(i).trim()).append("\n");
			}
		} else {
			for (int i = 0; i < versionEntity.getEnglishContent().size(); i++) {
				sb.append(versionEntity.getEnglishContent().get(i).trim()).append("\n");
			}
		}

		updata_content.setText(sb.toString());

		final Button updata_cancel = (Button) view.findViewById(R.id.updata_cancel);
		final Button updata_btn = (Button) view.findViewById(R.id.updata_btn);
		if ("1".equals(versionEntity.getMandatory())) {
			updata_cancel.setVisibility(View.GONE);
			// RelativeLayout.LayoutParams lay = new
			// RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
			// RelativeLayout.LayoutParams.WRAP_CONTENT);
			// lay.gravity = Gravity.CENTER;
			// updata_btn.setLayoutParams(lay);
			// versionUpdataDialog.setCancelable(true);
			versionUpdataDialog.setCanceledOnTouchOutside(false);
			versionUpdataDialog.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
					if (arg1 == KeyEvent.KEYCODE_BACK) {
						// activity.finish();
						activity.moveTaskToBack(true);
					}
					return false;
				}
			});
		}
		updata_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				versionUpdataDialog.dismiss();

			}
		});
		updata_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!NetWorkUtil.isNetworkConnectionActive(activity)) {

					Toast.makeText(activity, R.string.upgrade_No_network, Toast.LENGTH_SHORT).show();
					return;
				}
				if (isUpdating) {
					// ToastUtil.showLollipopToast(activity.getString(R.string.d_updata),activity);
					Toast.makeText(activity, R.string.d_updata, Toast.LENGTH_SHORT).show();
					if (versionUpdataDialog != null) {
						versionUpdataDialog.dismiss();
					}
					return;
				}

				if ("1".equals(versionEntity.getMandatory())) {
					content_title.setText(activity.getResources().getString(R.string.d_updata));
					updata_cancel.setClickable(false);
					updata_btn.setClickable(false);
				} else {
					updata_cancel.setClickable(true);
					updata_btn.setClickable(true);
					versionUpdataDialog.dismiss();
				}

				new Thread(new Runnable() {

					@Override
					public void run() {
						File outFile = null;
						Intent notify = null;
						try {
							outFile = new File(activity.getCacheDir(), activity.getPackageName() + ".apk");
							String downloadUrl = versionEntity.getDownloadUrl();
							Log.i("yexiaoli", "downloadUrl=" + downloadUrl);
							notify = new Intent();
							notify.setAction(DownloadReceiver.ACTION_DOWNLOAD);
							notify.putExtra("apkPath", outFile.getPath());
							notify.putExtra("appName", activity.getString(R.string.app_name));
							notify.putExtra("packageName", activity.getPackageName());
							notify.putExtra("id", 1001);
							activity.sendBroadcast(notify);

							URL url = new URL(downloadUrl);
							HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
							long apkSize = 0;
							long progress = 0;
							String contentLen = urlConn.getHeaderField("content-Length");
							Log.i("yexiaoli", "contentLen=" + contentLen);
							if (contentLen != null && contentLen.length() != 0) {
								apkSize = Integer.valueOf(contentLen);
							}
							// Debug.println("apkSize = " + apkSize);
							notify.putExtra("max", apkSize);
							// 获取数据
							InputStream input = urlConn.getInputStream();
							// 下载数
							int respondCode = urlConn.getResponseCode();
							// Debug.println("respondCode = " + respondCode);
							// HttpURLConnection.HTTP_PARTIAL
							FileOutputStream output = new FileOutputStream(outFile);
							byte[] buffer = new byte[1024];
							//
							int n = 0;
							isUpdating = true;

							// int counter = 0;
							long startTime = System.currentTimeMillis();
							while (-1 != (n = input.read(buffer))) {
								output.write(buffer, 0, n);
								progress += n;
								// Debug.println("progress = " + progress);
								Log.i("yexiaoli", "progress=" + progress);
								notify.putExtra("progress", progress);

								// counter++;
								// int shit = counter % 99;
								long temp = System.currentTimeMillis();
								if (temp - startTime > 500 || apkSize == progress) {
									activity.sendBroadcast(notify);
									startTime = temp;
								}
							}
							// notify.putExtra("progress", progress);
							// activity.sendBroadcast(notify);

							input.close();
							output.close();
							Process p = Runtime.getRuntime().exec("chmod 604 " + outFile.getPath());
							int status = p.waitFor();

							// Intent intent = new Intent();
							// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							// intent.setAction(android.content.Intent.ACTION_VIEW);
							// intent.setDataAndType(Uri.fromFile(outFile),
							// "application/vnd.android.package-archive");
							// activity.startActivity(intent);
							isUpdating = false;
							updata_btn.setClickable(true);
						} catch (MalformedURLException e) {
							isUpdating = false;
							// Debug.e("upgrade", e);
							updata_btn.setClickable(true);
							notify.putExtra("exit", true);
							activity.sendBroadcast(notify);
						} catch (IOException e) {
							isUpdating = false;
							// Debug.e("upgrade", e);
							updata_btn.setClickable(true);
							notify.putExtra("exit", true);
							activity.sendBroadcast(notify);
						} catch (InterruptedException e) {
							isUpdating = false;
							// Debug.e("upgrade", e);
							updata_btn.setClickable(true);
							notify.putExtra("exit", true);
							activity.sendBroadcast(notify);
						}
					}
				}).start();

			}
		});

		versionUpdataDialog.setContentView(view);

		if (!activity.isFinishing()) {

			versionUpdataDialog.show();
		}

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				TextView updata_content1 = (TextView) view.findViewById(R.id.updata_content);
				int Linenum = updata_content1.getLineCount();
				// Log.e("Linenum",Linenum+"");
				if (Linenum >= 12) {
					ScrollView sv = (ScrollView) view.findViewById(R.id.sv_update);
					ViewGroup.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, DensityUtil
							.dip2px(activity, 200));
					sv.setLayoutParams(params);
				}
			}
		}, 100);

	}

	public static void showRecogUpdataDialog(final Activity activity, final VersionEntity versionEntity) {

		final MyDialog versionUpdataDialog = new MyDialog(activity, R.style.myDialogTheme);
		LayoutInflater inflater = LayoutInflater.from(activity);
		View view = inflater.inflate(R.layout.m_version_updata_dialog, null);

		TextView updata_content = (TextView) view.findViewById(R.id.updata_content);

		final TextView content_title = (TextView) view.findViewById(R.id.content_title);
		content_title.setText(activity.getResources().getString(R.string.d_engine_updata));
		updata_content.setText(activity.getResources().getString(R.string.d_engine_expire));

		final Button updata_cancel = (Button) view.findViewById(R.id.updata_cancel);
		final Button updata_btn = (Button) view.findViewById(R.id.updata_btn);

		versionUpdataDialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
				if (arg1 == KeyEvent.KEYCODE_BACK) {
					activity.finish();
				}
				return false;
			}
		});

		updata_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				versionUpdataDialog.dismiss();

			}
		});
		updata_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				new Thread(new Runnable() {

					@Override
					public void run() {
						versionUpdataDialog.dismiss();
						File outFile = new File(activity.getCacheDir(), activity.getPackageName() + ".apk");
						String downloadUrl = versionEntity.getDownloadUrl();
						Log.i("yexiaoli", "downloadUrl2=" + downloadUrl);
						Intent notify = new Intent();
						notify.setAction(DownloadReceiver.ACTION_DOWNLOAD);
						notify.putExtra("apkPath", outFile.getPath());
						notify.putExtra("appName", activity.getString(R.string.app_name));
						notify.putExtra("packageName", activity.getPackageName());
						notify.putExtra("id", 1001);
						activity.sendBroadcast(notify);

						try {
							URL url = new URL(downloadUrl);
							HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
							int apkSize = 0;
							int progress = 0;
							String contentLen = urlConn.getHeaderField("content-Length");
							if (contentLen != null && contentLen.length() != 0) {
								apkSize = Integer.valueOf(contentLen);
							}
							// Debug.println("apkSize = " + apkSize);
							notify.putExtra("max", apkSize);
							// 获取数据
							InputStream input = urlConn.getInputStream();
							// 下载数
							int respondCode = urlConn.getResponseCode();
							// Debug.println("respondCode = " + respondCode);
							// HttpURLConnection.HTTP_PARTIAL
							FileOutputStream output = new FileOutputStream(outFile);
							byte[] buffer = new byte[10240];
							//
							int n = 0;
							while (-1 != (n = input.read(buffer))) {
								output.write(buffer, 0, n);
								progress += n;
								// Debug.println("progress = " + progress);
								notify.putExtra("progress", progress);
								activity.sendBroadcast(notify);
							}
							input.close();
							output.close();
							Process p = Runtime.getRuntime().exec("chmod 604 " + outFile.getPath());
							int status = p.waitFor();
							// Log.e("love", "status-------------" + "   "
							// + status);

							Intent intent = new Intent();
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.setAction(android.content.Intent.ACTION_VIEW);
							intent.setDataAndType(Uri.fromFile(outFile), "application/vnd.android.package-archive");
							activity.startActivity(intent);

							// openFile(activity,outFile);
							if (status == 0) {
								// chmod succeed
							} else {
								// chmod failed
							}
						} catch (MalformedURLException e) {
							// Debug.e("upgrade", e);
							notify.putExtra("exit", true);
							activity.sendBroadcast(notify);
						} catch (IOException e) {
							// Debug.e("upgrade", e);
							notify.putExtra("exit", true);
							activity.sendBroadcast(notify);
						} catch (InterruptedException e) {
							// Debug.e("upgrade", e);
							notify.putExtra("exit", true);
							activity.sendBroadcast(notify);
						}
					}
				}).start();

			}
		});

		versionUpdataDialog.setContentView(view);
		versionUpdataDialog.show();

	}

}
