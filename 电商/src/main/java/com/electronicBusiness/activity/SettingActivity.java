package com.electronicBusiness.activity;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.electronicBusiness.R;
import com.electronicBusiness.base.BaseActivity;
import com.electronicBusiness.manager.ConfigurationManager;
import com.electronicBusiness.utils.SPUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class SettingActivity extends BaseActivity {

	@ViewInject(R.id.iv_back)
	private TextView mIv_back;
	@ViewInject(R.id.et_ip)
	private EditText mEt_ip;
	@ViewInject(R.id.et_port)
	private EditText mEt_port;
	@ViewInject(R.id.bt_save)
	private Button mBt_save;
	@ViewInject(R.id.sp_app_name)
	private Spinner mSp_app_name;
	public static final String Path[] = {"","myIstore","istore"};
	@Override
	public void initView() {
		setContentView(R.layout.activity_setting);
		ViewUtils.inject(this);
	}
	@Override
	protected void initEvent() {
		mIv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mBt_save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(mEt_ip.getText().toString())
						&& !TextUtils.isEmpty(mEt_port.getText().toString())) {
					long id = mSp_app_name.getSelectedItemId();
					boolean isSelected = id != 0?true:false;
					boolean isSave = SPUtils.putInt("id", (int)id);
					boolean isSuccess = SPUtils.putBoolean("IsIpConfiger", true)
							&& SPUtils.putString("Ip",
							mEt_ip.getText().toString())
							&& SPUtils.putString("Port",
							mEt_port.getText().toString())
							&& isSelected && isSave;
					if (isSuccess) {
						ConfigurationManager.project = Path[(int)id];
						Toast.makeText(SettingActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
						finish();
					} else {
						Toast.makeText(SettingActivity.this, "保存失败,请检查是否设置了门店", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(SettingActivity.this, "请正确输入", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	@Override
	protected void initData() {
		String ip = SPUtils.getString("Ip", "");
		String port = SPUtils.getString("Port", "");
		mEt_ip.setText(ip);
		mEt_port.setText(port);
		mSp_app_name.setSelection(SPUtils.getInt("id", 0), true);
	}
}
