package com.electronicBusiness.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.electronicBusiness.R;
import com.electronicBusiness.base.BaseActivity;
import com.electronicBusiness.manager.ConfigurationManager;
import com.electronicBusiness.manager.FragmentManager;
import com.electronicBusiness.manager.OkHttpClientManager;
import com.electronicBusiness.manager.OkHttpClientManager.Param;
import com.electronicBusiness.manager.OkHttpClientManager.ResultCallback;
import com.electronicBusiness.utils.SPUtils;
import com.electronicBusiness.utils.ToastUtils;
import com.electronicBusiness.utils.UIUtils;
import com.electronicBusiness.view.CanDeleteEditText;
import com.electronicBusiness.view.CanSeeEditText;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Request;


public class LoginActivity extends BaseActivity {

	@ViewInject(R.id.bt_login)
	private Button  mBt_login;
	@ViewInject(R.id.et_user)
	private CanDeleteEditText mEt_user;
	@ViewInject(R.id.et_pass)
	private CanSeeEditText mEt_pass;
	public static final String[] funcs = {"绑定","盘点","销售","解绑"};
	private AlertDialog mDialog;

	public void initView() {
		setContentView(R.layout.activity_login);
		ViewUtils.inject(this);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FragmentManager.clear();
	}
	@Override
	protected void initData() {
	}
	@Override
	protected void initEvent() {
		mBt_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String username = mEt_user.getETText();
				String password = mEt_pass.getETText();
				if(!TextUtils.isEmpty(username)&&!TextUtils.isEmpty(password))
				{
					if(username.equals(password)&&username.equals("admin"))
					{
						Intent intent = new Intent(UIUtils.getContext(),SettingActivity.class);
						startActivity(intent);
					}
					else
					{
						showLoginingDialog();
						String ip = SPUtils.getString("Ip", "");
						String port = SPUtils.getString("Port", "");
						if(TextUtils.isEmpty(ip)||TextUtils.isEmpty(port))
						{
							ToastUtils.showToast("请先设置IP地址和端口号");
							mDialog.dismiss();
							return ;
						}
						Param param1 = new Param("username",username);
						Param param2 = new Param("password",password);
						//访问服务器
						OkHttpClientManager.postAsyn("http://"+ip+":"+port+"/"+ com.electronicBusiness.manager.ConfigurationManager.project+"/check/login",new ResultCallback<String>(){

							@Override
							public void onError(Request request, Exception e) {
								ToastUtils.showToast("网络通讯失败");
								dismissDialog();
								//先进去
//								ToastUtils.showToast("登录成功");
//								Intent intent = new Intent(UIUtils.getContext(),ConnectionActivity.class);
//								startActivity(intent);
							}

							@Override
							public void onResponse(String response) {
								if(response.equals("200"))
								{
									ToastUtils.showToast("登录成功");
									Intent intent = new Intent(UIUtils.getContext(),ConnectionActivity.class);
									startActivity(intent);
									finish();
								}
								else if(response.equals("400"))
								{
									ToastUtils.showToast("登录失败请检查用户名和密码是否正确");
								}
								else
								{
									ToastUtils.showToast("网络通讯失败");
								}
//								ToastUtils.showToast("登录成功");
//								Intent intent = new Intent(UIUtils.getContext(),ConnectionActivity.class);
//								startActivity(intent);
								dismissDialog();
							}
							
						},param1,param2);
					}
				}
			}
		});

	}
	protected void showLoginingDialog() {
		mDialog = new AlertDialog.Builder(this)
		.setView(View.inflate(this, R.layout.dialog_login, null))
		.setOnKeyListener(mKeylistener)
		.create();
		mDialog.show();
	}
	private void dismissDialog()
	{
		if(mDialog!=null)
		{
			mDialog.dismiss();
		}
	}
}
