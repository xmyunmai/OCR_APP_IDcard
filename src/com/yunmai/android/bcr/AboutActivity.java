package com.yunmai.android.bcr;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cc.ocr.yunapp.idcard.R;

import com.yunmai.android.bcr.update.LoadingDialog;
import com.yunmai.android.bcr.update.NetWorkUtil;
import com.yunmai.android.bcr.update.UpdateManager;
import com.yunmai.android.bcr.update.VersionEntity;

public class AboutActivity extends Activity {

	private LoadingDialog checkUpdateDialog;
	private VersionEntity versionEntity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		initUI();

	}

	private void initUI() {
		LinearLayout mTvNew = (LinearLayout) findViewById(R.id.layout_about_new);
		mTvNew.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				checkUpdate();

			}
		});
		TextView mTvVersion = (TextView) findViewById(R.id.tv_about_version);
		String version = getVersion();
		version = version.substring(1, 13);
		mTvVersion.setText(version);

	}

	/**
	 * 检查更新
	 */
	public void checkUpdate() {
		if (!NetWorkUtil.isNetworkConnectionActive(this)) {
			Toast.makeText(this, this.getResources().getString(R.string.setting_activity_no_network),
					Toast.LENGTH_SHORT).show();
			return;
		}
		showCheckUpdateDialog();
		new Thread() {
			@Override
			public void run() {

				versionEntity = UpdateManager.getOnlineVersion();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (versionEntity != null) {
							PackageInfo info;
							try {
								String packageName = getPackageName();
								PackageManager manager = getPackageManager();
								info = manager.getPackageInfo(packageName, 0);
								int version = info.versionCode;
								Log.i("yexiaoli", "version=" + version);
								if (version < Integer.valueOf(versionEntity.getVersionCode())) {

									UpdateManager.showUpdataDialog(AboutActivity.this, versionEntity);
								} else {
									Toast.makeText(getApplicationContext(),
											getResources().getString(R.string.last_version), Toast.LENGTH_SHORT).show();
								}
								dismissCheckUpdateDialog();
							} catch (NameNotFoundException e) {
								e.printStackTrace();
								dismissCheckUpdateDialog();
							}
						} else {
							dismissCheckUpdateDialog();
							Toast.makeText(AboutActivity.this,
									AboutActivity.this.getResources().getString(R.string.err_http_error),
									Toast.LENGTH_SHORT).show();
						}
					}
				});

			};
		}.start();
	}

	/**
	 * 获取版本号
	 * 
	 * @return 当前应用的版本号
	 */
	public String getVersion() {
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return getString(R.string.not_find_version_name);
		}
	}

	/**
	 * 显示检查更新加载框
	 */
	private void showCheckUpdateDialog() {
		if (checkUpdateDialog == null || !checkUpdateDialog.isShowing()) {
			checkUpdateDialog = new LoadingDialog(this, getResources().getString(R.string.check_updating));
			checkUpdateDialog.setCancelable(true);
			checkUpdateDialog.show();
		}
	}

	/**
	 * 关闭检查更新加载框
	 */
	private void dismissCheckUpdateDialog() {
		if (checkUpdateDialog != null && checkUpdateDialog.isShowing()) {
			checkUpdateDialog.dismiss();
			checkUpdateDialog = null;
		}
	}

}
