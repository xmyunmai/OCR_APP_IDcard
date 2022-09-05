package com.yunmai.android.bcr.update;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;

public class MyDialog extends Dialog{

	public MyDialog(Context context) {
		super(context);
		
	}

	public MyDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public MyDialog(Context context, int theme) {
		super(context, theme);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowManager m = getWindow().getWindowManager();  
		Display d = m.getDefaultDisplay();  //为获取屏幕宽、高  
		//设定大小
        LayoutParams params = getWindow().getAttributes();   
        params.height = LayoutParams.WRAP_CONTENT;   //高度设置为屏幕的0.6  
        params.width = (int) (d.getWidth() * 0.9);    //宽度设置为屏幕的0.95  
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params); 
	}
}
