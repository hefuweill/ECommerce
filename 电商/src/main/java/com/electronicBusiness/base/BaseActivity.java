package com.electronicBusiness.base;

import com.electronicBusiness.utils.SPUtils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

public abstract class BaseActivity extends AppCompatActivity {


	public OnKeyListener mKeylistener;
	public String mIp;
	public String mPort;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		init();
		initView();
		initData();
		initEvent();
	}

	private void init() {
		mKeylistener = new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK
						|| keyCode == KeyEvent.KEYCODE_SEARCH) {
					return true;
				} else {
					return false;
				}
			}
		};
		mIp = SPUtils.getString("Ip", "");
		mPort = SPUtils.getString("Port", "");
	}

	protected void initEvent() {
		
	}

	protected void initData() {
		
	}

	public abstract void initView();
}
