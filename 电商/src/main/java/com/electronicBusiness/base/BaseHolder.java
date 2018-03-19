package com.electronicBusiness.base;

import com.electronicBusiness.utils.SPUtils;

import android.view.View;

public abstract class BaseHolder<T> {
	private T data;
	public View mRootView;
	protected String mIp;
	protected String mPort;

	public BaseHolder() {
		mRootView = initView();
		mRootView.setTag(this);
		mIp = SPUtils.getString("Ip", "");
		mPort = SPUtils.getString("Port", "");
	}

	public void setData(T data) {
		this.data = data;
		changeViewStyle(data);
	}

	public T getData() {
		return data;
	}

	public abstract void changeViewStyle(T data);

	public void changeViewStyle(T data, int position) {
	}

	public abstract View initView();
}
