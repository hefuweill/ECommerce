package com.electronicBusiness.activity;

import java.util.Date;

import android.content.Intent;
import android.widget.TextView;

import com.electronicBusiness.R;
import com.electronicBusiness.base.BaseActivity;
import com.electronicBusiness.manager.ConfigurationManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ConnectionActivity extends BaseActivity {

	@ViewInject(R.id.tv_date)
	private TextView mTv_date;

	@SuppressWarnings("deprecation")
	@Override
	public void initView() {
		setContentView(R.layout.activity_connection);
		ViewUtils.inject(this);
		mTv_date.setText(new Date().toLocaleString());
	}

	@Override
	protected void initData() {
		startNextActivity();
	}

	private void startNextActivity() {
		Intent intent = new Intent();
//		switch (ConfigurationManager.func) {
//		case 0:
//			intent.setClass(this, MipcaActivityCapture.class);
//			break;
//		case 1:
//			intent.setClass(this, CheckActivity.class);
//			break;
//		case 2:
//			intent.setClass(this, SellActivity.class);
//			break;
//		case 3:
//			intent.setClass(this, UnBindActivity.class);
//			break;
//		}
		intent.setClass(this, HomeActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	protected void initEvent() {
	}
}
