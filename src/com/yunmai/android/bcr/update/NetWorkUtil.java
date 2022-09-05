package com.yunmai.android.bcr.update;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkUtil {
	private static final String TAG = "NetWorkUtil";

	/**
	 * 判断是否网络连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnectionActive(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();

		if (networkInfo == null || !networkInfo.isConnected()) {
			return false;
		}
		return true;
	}

}
