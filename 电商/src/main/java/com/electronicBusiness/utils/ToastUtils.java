package com.electronicBusiness.utils;

import android.widget.Toast;

public class ToastUtils {
	private static Toast mToast = null;
	public static void showToast(String content)
	 
	{
		if(mToast!=null)
		{
			mToast.setText(content);
		}
		else
		{
			mToast = Toast.makeText(UIUtils.getContext(), content, Toast.LENGTH_SHORT);
		}
		mToast.show();
	}
}
