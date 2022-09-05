package com.yunmai.android.bcr.update;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import cc.ocr.yunapp.idcard.R;

/**
 * 加载中Dialog
 * 
 * @author lzhiwei
 * 
 */
public class LoadingDialog extends AlertDialog {

	private TextView tips_loading_msg;

	private String message = null;

	public LoadingDialog(Context context) {
		super(context);
		message = getContext().getResources().getString(R.string.bcr_file_progressbar_load_title);
	}

	public LoadingDialog(Context context, String message) {
		super(context);
		this.message = message;
		this.setCancelable(true);
		this.setCanceledOnTouchOutside(false);
	}

	public LoadingDialog(Context context, int theme, String message) {
		super(context, theme);
		this.message = message;
		this.setCancelable(true);
		this.setCanceledOnTouchOutside(false);
	}

	public LoadingDialog(Context context, String message, boolean flag) {
		super(context);
		this.message = message;
		this.setCancelable(false);
		this.setCanceledOnTouchOutside(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.view_tips_loading);
		tips_loading_msg = (TextView) findViewById(R.id.tips_loading_msg);
		tips_loading_msg.setText(this.message);
	}

	public void setText(String message) {
		this.message = message;
		tips_loading_msg.setText(this.message);
	}

	public void setText(int resId) {
		setText(getContext().getResources().getString(resId));
	}

}
