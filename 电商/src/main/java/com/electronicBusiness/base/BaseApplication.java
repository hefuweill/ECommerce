package com.electronicBusiness.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import android.hardware.uhf.magic.DevBeep;
import android.hardware.uhf.magic.reader;

public class BaseApplication extends Application{
	private static Context mContext;
	public static boolean isConn = false;
	private static long mThreadId;
	private static Handler mHandler;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		mThreadId = Thread.currentThread().getId();
		mHandler = new Handler();
		String C5U = "/dev/ttyMT1";
		InitUHF(C5U);
	}

	public void InitUHF(String type) {
		reader.init(type);
		reader.Open(type);
		reader.SetTransmissionPower(1950);
		DevBeep.init(this);
	}

	public static Context getContext() {
		return mContext;
	}

	public static Handler getHandler() {
		return mHandler;
	}

	public static long getMainThreadId() {
		return mThreadId;
	}


}
