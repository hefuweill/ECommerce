package com.electronicBusiness.base;

import rfid.ivrjacku1.IvrJackAdapter;
import rfid.ivrjacku1.IvrJackService;
import rfid.ivrjacku1.IvrJackStatus;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.electronicBusiness.utils.ToastUtils;

import java.util.Timer;
import java.util.TimerTask;

public class BaseApplication extends Application implements IvrJackAdapter {
	private static Context mContext;
	public static boolean isConn = false;
	private static onConnectListener mListener;
	private static IvrJackService mService;
	private static long mThreadId;
	private static Handler mHandler;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		mService = new IvrJackService();
		mService.open(this, this);
		mThreadId = Thread.currentThread().getId();
		mHandler = new Handler();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if(mListener!=null)
				Log.e("Base",mListener.toString());
			}
		},1000,2000);
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

	public static IvrJackService getService() {
		return mService;
	}

	@Override
	public void onConnect(String arg0) {
		ToastUtils.showToast("设备已经连接");
		if (mListener != null) {
			mListener.onConnect();
		}
		isConn = true;
	}

	@Override
	public void onDisconnect() {
		System.out.println("onDisconnect");
		isConn = false;
		if (mListener != null) {
			mListener.onDisconnect();
		}
	}

	@Override
	public void onInventory(String arg0) {
		System.out.println("onInventory"+arg0);
		if (mListener != null) {
			mListener.onInventory(arg0);
		}
	}

	@Override
	public void onStatusChange(IvrJackStatus arg0) {
		switch (arg0) {
		case ijsRecognized:// 已经识别
			System.out.println("ijsRecognized");
			break;
		case ijsDetecting:// 正在识别
			System.out.println("ijsDetecting");
			break;
		case ijsPlugout:// 设备已拔出
			System.out.println("ijsPlugout");
			break;
		case ijsUnRecognized:// 设备未识别
			System.out.println("ijsUnRecognized");
			break;
		}
	}

	public interface onConnectListener {
		void onConnect();

		void onDisconnect();

		void onInventory(String arg0);

		void onStatusChange(IvrJackStatus arg0);
	}

	public static void setOnConnectListener(onConnectListener listener) {
		mListener = listener;
		System.out.println("listener"+mListener.toString());
	}
}
