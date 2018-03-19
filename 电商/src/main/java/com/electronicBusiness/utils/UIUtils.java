package com.electronicBusiness.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import com.electronicBusiness.base.BaseApplication;
import com.electronicBusiness.manager.ConfigurationManager;

public class UIUtils {
	public static Context getContext()
	{
		return BaseApplication.getContext();
	}
	public static View inflate(int resId)
	{
		return View.inflate(getContext(), resId, null);
	}
	public static void runOnSafeThread(Runnable runnable) {
		if(Thread.currentThread().getId()==BaseApplication.getMainThreadId())
		{
			runnable.run();
		}
		else
		{
			BaseApplication.getHandler().post(runnable);
		}
	}
	public static String getType(int type)
	{
		switch (type) {
		case ConfigurationManager.MANGPAN:
			return "盲盘";
		case ConfigurationManager.QUANPAN:
			return "全盘";
		case ConfigurationManager.ANSHANGPINGPAN:
			return "按商品盘";
		default:
			return "";
		}
	}
	@SuppressLint("SimpleDateFormat") public static String getFormatterTime(int time)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd ahh:mm");
		return format.format(new Date(time*1000L));
	}
}
