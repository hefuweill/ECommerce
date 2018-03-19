package com.electronicBusiness.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.electronicBusiness.R;
import com.electronicBusiness.base.BaseActivity;
import com.electronicBusiness.base.BaseHolder;
import com.electronicBusiness.base.MyBaseAdapter;
import com.electronicBusiness.holder.BindEpcHolder;
import com.electronicBusiness.manager.ConfigurationManager;
import com.electronicBusiness.manager.OkHttpClientManager;
import com.electronicBusiness.manager.OkHttpClientManager.Param;
import com.electronicBusiness.manager.OkHttpClientManager.ResultCallback;
import com.electronicBusiness.utils.ToastUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import android.hardware.uhf.magic.reader;

public class UnBindActivity extends BaseActivity {

	@ViewInject(R.id.lv)
	private ListView lv;
	private List<String> data = new ArrayList<String>();
	@ViewInject(R.id.tv_start)
	private TextView tv_start;
	@ViewInject(R.id.tv_unbind)
	private TextView tv_unbind;
	@ViewInject(R.id.tv_clear)
	private TextView tv_clear;
	private MyEpcAdapter mAdapter;
	private AlertDialog mDialog;
	public final int CHANGE_START = 2;
	public final int CHANGE_STOP = 3;
	private boolean isReading = false;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case CHANGE_START:
					tv_start.setText("Start");
					isReading = true;
					break;
				case CHANGE_STOP:
					tv_start.setText("Stop");
					isReading = false;
					break;
			}
		};
	};

	@Override
	public void initView() {
		setContentView(R.layout.activity_unbind);
		ViewUtils.inject(this);
		mAdapter = new MyEpcAdapter(data);
		lv.setAdapter(mAdapter);
	}

	@Override
	protected void initEvent() {
		super.initEvent();
		tv_clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				data.clear();
				mAdapter.notifyDataSetChanged();
			}
		});
		tv_unbind.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (data.size() > 0) {
					showWaitNetDialog();
					Param param = createUnBindParam();
					// 访问网络绑定回到扫描sku界面
					OkHttpClientManager.postAsyn("http://" + mIp + ":" + mPort
							+ "/"+ com.electronicBusiness.manager.ConfigurationManager.project+"/supply/unbind",
							new ResultCallback<String>() {

								@Override
								public void onError(Request request, Exception e) {
									ToastUtils.showToast("访问网络失败"
											+ e.getMessage());
									mDialog.dismiss();
								}

								@Override
								public void onResponse(String response) {
									System.out.println("response" + response);
									if (response.equals("200")) {
										ToastUtils.showToast("解绑成功");
									} else if (response.equals("400")) {
										ToastUtils.showToast("解绑失败");
									}
									mDialog.dismiss();
								}

							}, param);

				} else {
					ToastUtils.showToast("EPC列表为空");
				}
			}
		});
		tv_start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(isReading){
					reader.StopLoop();
					handler.sendEmptyMessage(CHANGE_START);
				}else{
					reader.ReadtidLablesLoop(12);
					handler.sendEmptyMessage(CHANGE_STOP);
				}
			}
		});
	}

	protected void reFreshData(List<String> epcs) {
		for (String epc : epcs) {
			if (!data.contains(epc)) {
				data.add(epc);
			}
		}
		mAdapter.notifyDataSetChanged();
	}

	protected Param createUnBindParam() {
		Gson gson = new Gson();
		String json = gson.toJson(data);
		return new Param("epcList", json);
	}

	protected void showWaitNetDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setCancelable(false);
		builder.setOnKeyListener(mKeylistener);
		builder.setMessage("正在上传请稍后......");
		mDialog = builder.create();
		mDialog.show();
	}

	class MyEpcAdapter extends MyBaseAdapter<String> {

		public MyEpcAdapter(List<String> data) {
			super(data);
		}

		@Override
		public BaseHolder<?> setHolder() {
			return new BindEpcHolder();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ConfigurationManager.stopEquip();
	}
}
