package com.electronicBusiness.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rfid.ivrjacku1.IvrJackStatus;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.electronicBusiness.R;
import com.electronicBusiness.base.BaseActivity;
import com.electronicBusiness.base.BaseApplication;
import com.electronicBusiness.base.BaseApplication.onConnectListener;
import com.electronicBusiness.domain.ExecutePlanDetailsBean;
import com.electronicBusiness.domain.ResultBean;
import com.electronicBusiness.manager.ConfigurationManager;
import com.electronicBusiness.manager.OkHttpClientManager;
import com.electronicBusiness.manager.OkHttpClientManager.Param;
import com.electronicBusiness.manager.OkHttpClientManager.ResultCallback;
import com.electronicBusiness.utils.ToastUtils;
import com.electronicBusiness.utils.UIUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Request;

public class ExeCheckActivity extends BaseActivity {

	@ViewInject(R.id.tv)
	private TextView tv_progress;
	@ViewInject(R.id.pb)
	private ProgressBar pb;
	private BaseAdapter mAdapter;
	@ViewInject(R.id.pb_load)
	private ProgressBar pb_load;
	@ViewInject(R.id.lv)
	private ListView lv;
	@ViewInject(R.id.ll)
	private LinearLayout ll;
	private List<String> epcList = new ArrayList<>();
	private List<String> epcListCopy;
	private List<ExecutePlanDetailsBean> list;
	public final int CHANGE_START = 1;
	public final int CHANGE_STOP = 2;
	private boolean isReading = true;
	@ViewInject(R.id.tv_start)
	private TextView tv_start;
	@ViewInject(R.id.tv_submit)
	private TextView tv_submit;
	@ViewInject(R.id.tv_clear)
	private TextView tv_clear;
	private AlertDialog mDialog;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CHANGE_START:
				tv_start.setText("开始盘点");
				isReading = true;
				break;
			case CHANGE_STOP:
				tv_start.setText("暂停盘点");
				isReading = false;
				break;
			}
		};
	};
	@Override
	public void initView() {
		setContentView(R.layout.activity_exe_check);
		ViewUtils.inject(this);
	}
	protected void reFreshData(List<String> data) {
		for (String epc : data) {
			if (epcList.contains(epc)) {
				epcList.remove(epc);
				pb.setProgress(epcListCopy.size()-epcList.size());
				tv_progress.setText((epcListCopy.size()-epcList.size())+"/"+epcListCopy.size());
			}
		}
		mAdapter.notifyDataSetChanged();
	}
	@Override
	protected void initEvent() {
		super.initEvent();
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				for(ExecutePlanDetailsBean bean : list)
				{
					if(bean.getEpc().equals(epcList.get(position)))
					{
						Intent intent = new Intent(UIUtils.getContext(),EpcDetailActivity.class);
						intent.putExtra("epc", bean);
						startActivity(intent);
						break;
					}
				}

			}
		});
		BaseApplication.setOnConnectListener(new onConnectListener() {

			@Override
			public void onStatusChange(IvrJackStatus arg0) {
			}

			@Override
			public void onInventory(String arg0) {
				List<String> data = Arrays.asList(arg0.split(";"));
				reFreshData(data);
			}
				
			@Override
			public void onDisconnect() {
				isReading = true;
				handler.sendEmptyMessage(CHANGE_START);
			}

			@Override
			public void onConnect() {
			}
		});
		tv_clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				epcList.clear();
				epcList.addAll(epcListCopy);
				mAdapter.notifyDataSetChanged();
				pb.setProgress(0);
				tv_progress.setText((epcListCopy.size()-epcList.size())+"/"+epcListCopy.size());
			}
		});
		tv_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
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

			private String mMessage;

			@Override
			public void onClick(View v) {
				mMessage = "设备未连接,请先连接设备";
				new Thread() {
					public void run() {
						if (BaseApplication.isConn) {
							int result = BaseApplication.getService().readEPC(
									isReading);// true为开启
							switch (result) {
							case -1:
								mMessage = "电池电量低";
								break;
							case -2:
								mMessage = "通讯失败,设备未连接";
								break;
							case 0:
								if (isReading) {
									mMessage = "开启成功";
									handler.sendEmptyMessage(CHANGE_STOP);
								} else {
									mMessage = "暂停成功";
									handler.sendEmptyMessage(CHANGE_START);
								}
								break;
							case 1:
								mMessage = "通讯失败";
								break;
							case 2:
								mMessage = "未知错误";
								break;
							default:

							}
						} else {
							Vibrator vibrator = (Vibrator) ExeCheckActivity.this.getSystemService(Service.VIBRATOR_SERVICE);
							vibrator.vibrate(1500);
						}
						UIUtils.runOnSafeThread(new Runnable() {

							@Override
							public void run() {
								ToastUtils.showToast(mMessage);
							}
						});
					}
				}.start();
			}
		});
	}
	@Override
	protected void initData() {
		super.initData();
		int id = getIntent().getIntExtra("planId",0);
		Param param = new Param("planId",id+"");
		OkHttpClientManager.postAsyn("http://"+mIp+":"+mPort+"/"+ com.electronicBusiness.manager.ConfigurationManager.project+"/check/execute_plan_detail",new ResultCallback<List<ExecutePlanDetailsBean>>(){


			@Override
			public void onError(Request request, Exception e) {
				ToastUtils.showToast("网络通讯失败");
				
			}

			@Override
			public void onResponse(List<ExecutePlanDetailsBean> list) {
				if(list!=null&&list.size()>0)
				{
					ExeCheckActivity.this.list = list;
					for(ExecutePlanDetailsBean bean : list)
					{
						epcList.add(bean.getEpc());
					}
					epcListCopy = new ArrayList<String>(epcList);
					mAdapter = new ArrayAdapter<String>(UIUtils.getContext(), R.layout.simple_list_item, epcList);
					lv.setAdapter(mAdapter);
					ll.setVisibility(View.VISIBLE);
					pb_load.setVisibility(View.INVISIBLE);
					pb.setMax(list.size());
					tv_progress.setText((epcListCopy.size()-epcList.size())+"/"+epcListCopy.size());
				}
			}
			
		},param);
	}
	protected void showSubmitDialog() {
		mDialog = new AlertDialog.Builder(this)
		.setView(View.inflate(this, R.layout.dialog_submit, null))
		.setOnKeyListener(mKeylistener)
		.create();
		mDialog.show();
	}
	private String createJson()
	{
		ResultBean bean = new ResultBean();
		bean.setPlan_id(getIntent().getIntExtra("planId", 0));
		bean.setIsBlind(getIntent().getIntExtra("isBlind", 1));
		bean.setEpc(epcList);
		return new Gson().toJson(bean);
	}
}
