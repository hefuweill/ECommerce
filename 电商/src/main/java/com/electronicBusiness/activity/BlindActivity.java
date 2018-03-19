package com.electronicBusiness.activity;

import android.app.AlertDialog;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.electronicBusiness.R;
import com.electronicBusiness.base.BaseActivity;
import com.electronicBusiness.domain.ResultBean;
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

public class BlindActivity extends BaseActivity{

	@ViewInject(R.id.tv_start)
	private TextView tv_start;
	@ViewInject(R.id.tv_submit)
	private TextView tv_submit;
	@ViewInject(R.id.tv_clear)
	private TextView tv_clear;
	@ViewInject(R.id.tv_count)
	private TextView tv_count;
	@ViewInject(R.id.lv)
	private ListView lv;
	private List<String> data = new ArrayList<String>();
	private int count = 0;
	private AlertDialog mDialog;
	public final int CHANGE_START = 2;
	public final int CHANGE_STOP = 3;
	private boolean isReading = false;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case reader.msgreadepc:
					if(isReading){
						String epc = (String) msg.obj;
						reFreshData(epc);
					}
					break;
				case CHANGE_START:
					tv_start.setText("开始盘点");
					isReading = false;
					break;
				case CHANGE_STOP:
					tv_start.setText("暂停盘点");
					isReading = true;
					break;
			}
		};
	};
	private ArrayAdapter<String> mAdapter;
	@Override
	public void initView() {
		setContentView(R.layout.activity_blind);
		ViewUtils.inject(this);
	}
	@Override
	protected void initData() {
		super.initData();
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
		lv.setAdapter(mAdapter);
	}
	private String createJson()
	{
		ResultBean bean = new ResultBean();
		bean.setPlan_id(getIntent().getIntExtra("planId", 0));
		bean.setIsBlind(getIntent().getIntExtra("isBlind", 1));
		bean.setEpc(data);
		return new Gson().toJson(bean);
	}
	@Override
	protected void initEvent() {
		tv_clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				data.clear();
				mAdapter.notifyDataSetChanged();
				tv_count.setText("已扫到:0");
				count = 0;
			}
		});
		tv_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(data.size()<=0)
				{
					ToastUtils.showToast("请先扫描!");
					return ;
				}
				if(ConfigurationManager.stopEquip()==0)
				{
					handler.sendEmptyMessage(CHANGE_START);
				}
				showSubmitDialog();
				Param param = new Param("result",createJson());
				OkHttpClientManager.postAsyn("http://"+mIp+":"+mPort+"/"+ com.electronicBusiness.manager.ConfigurationManager.project+"/check/upload_scan_result",new ResultCallback<String>(){

					@Override
					public void onError(Request request, Exception e) {
						ToastUtils.showToast("网络通讯失败");
						mDialog.dismiss();
					}

					@Override
					public void onResponse(String response) {
						if(response.equals("200"))
						{
							ToastUtils.showToast("提交成功");
							setResult(1);
							finish();
						}
						else
						{
							ToastUtils.showToast("提交失败");
						}
						mDialog.dismiss();
					}
					
				},param);
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
	protected void reFreshData(List<String> data) {
		for (String epc : data) {
			if (!BlindActivity.this.data.contains(epc)) {
				BlindActivity.this.data.add(epc);
				count++;
				tv_count.setText("已扫到:"+count);
			}
		}
		mAdapter.notifyDataSetChanged();
	}
	protected void showSubmitDialog() {
		mDialog = new AlertDialog.Builder(this)
		.setView(View.inflate(this, R.layout.dialog_submit, null))
		.setOnKeyListener(mKeylistener)
		.create();
		mDialog.show();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ConfigurationManager.stopEquip();
	}

	protected void reFreshData(String data) {
		if (!BlindActivity.this.data.contains(data)) {
			BlindActivity.this.data.add(data);
			count++;
			tv_count.setText("已扫到:"+count);
		}
		mAdapter.notifyDataSetChanged();
	}
}
