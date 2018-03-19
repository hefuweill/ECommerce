package com.electronicBusiness.utils;

import android.content.Context;

public class SPUtils {

	public static boolean putBoolean(String key, Boolean value)
	{
		return UIUtils.getContext().getSharedPreferences("config", Context.MODE_PRIVATE).edit()
		.putBoolean(key, value).commit();
	}
	public static boolean getBoolean(String key,boolean defValue)
	{
		return UIUtils.getContext().getSharedPreferences("config", Context.MODE_PRIVATE).getBoolean(key, defValue);
	}
	public static boolean putString(String key, String value)
	{
		return UIUtils.getContext().getSharedPreferences("config", Context.MODE_PRIVATE).edit()
		.putString(key, value).commit();
	}
	public static String getString(String key,String defValue)
	{
		return UIUtils.getContext().getSharedPreferences("config", Context.MODE_PRIVATE).getString(key, defValue);
	}
	public static boolean putInt(String key, int value)
	{
		return UIUtils.getContext().getSharedPreferences("config", Context.MODE_PRIVATE).edit()
		.putInt(key, value).commit();
	}
	public static int getInt(String key,int defValue)
	{
		return UIUtils.getContext().getSharedPreferences("config", Context.MODE_PRIVATE).getInt(key, defValue);
	}
}