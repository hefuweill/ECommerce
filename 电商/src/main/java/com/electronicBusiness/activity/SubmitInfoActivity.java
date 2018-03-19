package com.electronicBusiness.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.electronicBusiness.R;
import com.electronicBusiness.base.BaseActivity;
import com.electronicBusiness.domain.InputBean;
import com.electronicBusiness.domain.GoodMegBean;
import com.electronicBusiness.manager.OkHttpClientManager;
import com.electronicBusiness.manager.OkHttpClientManager.Param;
import com.electronicBusiness.manager.OkHttpClientManager.ResultCallback;
import com.electronicBusiness.utils.ToastUtils;
import com.electronicBusiness.utils.UIUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Request;

public class SubmitInfoActivity extends BaseActivity {

	@ViewInject(R.id.ll_type)
	private LinearLayout ll_type;
	@ViewInject(R.id.tv_type)
	private TextView tv_type;
	private final String defaultType = "点击选择类别";
	@ViewInject(R.id.et_goods_name)
	private EditText et_goods_name;
	@ViewInject(R.id.bt_submit)
	private Button bt_submit;
	private int mTypeId;
	private AlertDialog mDialog;
	@ViewInject(R.id.tv_bar)
	private TextView tv_bar;

	@Override
	public void initView() {
		setContentView(R.layout.activity_submitinfo);
		ViewUtils.inject(this);
	}

	@Override
	protected void initData() {
		super.initData();
	}

	@Override
	protected void initEvent() {
		super.initEvent();
		ll_type.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(UIUtils.getContext(),
						SelectTypeActivity.class), 1);
			}
		});
		bt_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String type = tv_type.getText().toString();
				String name = et_goods_name.getText().toString();
				if (!tv_type.getText().toString().equals(defaultType)
						&& !TextUtils.isEmpty(type) && !TextUtils.isEmpty(name)
						&& mTypeId != 0) {
					GoodMegBean bean = new GoodMegBean();
					bean.setGoodName(name);
					String sku = getIntent().getStringExtra("sku");
					bean.setSku(sku);
					bean.setTypeId(mTypeId);
					showSubmitDialog();
					Gson gson = new Gson();
					String stockContent = gson.toJson(bean);
					Param param1 = new Param("goodMegContent", stockContent);
					OkHttpClientManager.postAsyn("http://" + mIp + ":" + mPort
							+ "/"+ com.electronicBusiness.manager.ConfigurationManager.project+"/supply/upload_stock_content",
							new ResultCallback<String>() {

								@Override
								public void onError(Request request, Exception e) {
									ToastUtils.showToast("网络通讯失败");
									dismissDialog();
								}

								@Override
								public void onResponse(String response) {
									if (response.equals("200")) {
										ToastUtils.showToast("提交成功");

										connInternet(0);
									} else {
										ToastUtils.showToast("提交失败");
									}
									dismissDialog();
								}

							}, param1);
				} else {
					ToastUtils.showToast("请填写完整");
				}
			}
		});
		tv_bar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	protected void showSubmitDialog() {
		mDialog = new AlertDialog.Builder(this).setView(
				View.inflate(this, R.layout.dialog_submit, null)).create();
		mDialog.show();
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == 1 && arg2 != null) {
			String type = arg2.getStringExtra("type");
			mTypeId = arg2.getIntExtra("id", 0);
			tv_type.setText(type);
		}
	}

	private void dismissDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
		}
	}
	private void connInternet(final int num) {
		Param param1 = new Param("bindBean",new Gson().toJson(getIntent().getSerializableExtra("bean")));
		// 访问网络绑定回到扫描sku界面
		OkHttpClientManager.postAsyn("http://" + mIp + ":" + mPort
				+ "/"+ com.electronicBusiness.manager.ConfigurationManager.project+"/supply/input_bind", new ResultCallback<String>() {

			@Override
			public void onError(Request request, Exception e) {
				ToastUtils.showToast("访问网络失败" + e.getMessage());
				dismissDialog();
			}

			@Override
			public void onResponse(String response) {
				System.out.println("response" + response);
				if (response.equals("200")) {
					ToastUtils.showToast("绑定成功");
					setResult(2);
					finish();
				} else if (response.equals("400")) {
					ToastUtils.showToast("绑定失败");
				} else if (response.equals("500")) {

				}
				else if(response.startsWith("200")&&response.length()>3)
				{
					ToastUtils.showToast("商品信息录入成功，但由于上述epc中有"+response.substring(3)+"个已经绑定，因此本次入库失败");
					//已经有？？个被绑定‘
					finish();
				}
				dismissDialog();
			}

		}, param1);
	}
}
