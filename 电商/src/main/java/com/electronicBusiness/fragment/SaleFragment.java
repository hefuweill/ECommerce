package com.electronicBusiness.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rfid.ivrjacku1.IvrJackStatus;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.electronicBusiness.R;
import com.electronicBusiness.activity.MipcaActivityCapture;
import com.electronicBusiness.activity.OrderActivity;
import com.electronicBusiness.base.BaseApplication;
import com.electronicBusiness.base.BaseApplication.onConnectListener;
import com.electronicBusiness.base.BaseFragment;
import com.electronicBusiness.base.BaseHolder;
import com.electronicBusiness.base.MyBaseAdapter;
import com.electronicBusiness.domain.MySaleBean;
import com.electronicBusiness.domain.SalesOrderBean;
import com.electronicBusiness.holder.SellHolder;
import com.electronicBusiness.manager.ConfigurationManager;
import com.electronicBusiness.manager.OkHttpClientManager;
import com.electronicBusiness.manager.OkHttpClientManager.Param;
import com.electronicBusiness.manager.OkHttpClientManager.ResultCallback;
import com.electronicBusiness.utils.ToastUtils;
import com.electronicBusiness.utils.UIUtils;
import com.electronicBusiness.view.LoadingPager;
import com.electronicBusiness.view.LoadingPager.StateType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Request;

public class SaleFragment extends BaseFragment {
	@ViewInject(R.id.tv_start)
	private TextView tv_start;
	@ViewInject(R.id.tv_orders)
	private TextView tv_orders;
	@ViewInject(R.id.tv_clear)
	private TextView tv_clear;
	@ViewInject(R.id.lv)
	private ListView lv;
	@ViewInject(R.id.ll)
	private LinearLayout ll;
	@ViewInject(R.id.fab)
	private FloatingActionButton fab;
	private boolean isReading = true;
	public final int CHANGE_START = 1;
	public final int CHANGE_STOP = 2;
	private ArrayList<String> data = new ArrayList<String>();
	private SellAdapter mAdapter;
	private AlertDialog mDialog;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CHANGE_START:
				tv_start.setText("开始扫描");
				isReading = true;
				break;
			case CHANGE_STOP:
				tv_start.setText("暂停扫描");
				isReading = false;
				break;
			}
		};
	};
	@Override
	public StateType loadData() {
		return null;
	}
	@Override
	public void onResume() {
		super.onResume();
		initEvent();
	}
	@Override
	public View createSuccessView() {
		View view = View.inflate(getActivity(), R.layout.fragment_sell, null);
		ViewUtils.inject(this,view);
		mAdapter = new SellAdapter(data);
		lv.setAdapter(mAdapter);
		return view;
	}
	@Override
	public void onPause() {
		super.onPause();
		stop();
	}
	private void stop() {
		if (ConfigurationManager.stopEquip() == 0)// 关闭成功
		{
			handler.sendEmptyMessage(CHANGE_START);
		}		
	}

	protected void initEvent() {
		fab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(ll.getVisibility()==View.INVISIBLE)
				{
					ll.setVisibility(View.VISIBLE);
				}
				else
				{
					ll.setVisibility(View.INVISIBLE);
				}
			}
		});
		BaseApplication.setOnConnectListener(new onConnectListener() {

			@Override
			public void onStatusChange(IvrJackStatus arg0) {
			}

			@Override
			public void onInventory(String arg0) {
				System.out.println(arg0);
				List<String> epcs = Arrays.asList(arg0.split(";"));
				reFreshData(epcs);
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
				data.clear();
				mAdapter.notifyDataSetChanged();
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
							Vibrator vibrator = (Vibrator) getActivity().getSystemService(Service.VIBRATOR_SERVICE);
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
		tv_orders.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (data.size()>0) {
					stop();
					showWaitNetDialog();
					String json = new Gson().toJson(data);
					Param param1 = new Param("epcList", json);
					OkHttpClientManager.postAsyn("http://" + mIp + ":" + mPort
							+ "/"+ com.electronicBusiness.manager.ConfigurationManager.project+"/supply/get_sale_meglist",
							new ResultCallback<String>() {

								@Override
								public void onError(Request request, Exception e) {
									ToastUtils.showToast("网络通讯失败");
									mDialog.dismiss();
								}

								@Override
								public void onResponse(String response) {
									System.out.println("response" + response);
									if (response.equals("400")) {
										ToastUtils.showToast("获取信息失败");
									} else {
										ArrayList<SalesOrderBean> infoList = new Gson()
												.fromJson(
														response,
														new TypeToken<ArrayList<SalesOrderBean>>() {
														}.getType());
										Intent intent = new Intent(UIUtils
												.getContext(), OrderActivity.class);
										intent.putExtra("infoList", infoList);
										startActivityForResult(intent,2);
									}
									mDialog.dismiss();
								}

							}, param1);
				}
				else
				{
					ToastUtils.showToast("商品列表为空");
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
	@Override
	public void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == 1 && arg2 != null && arg0 == 1) {
			List<String> skus = arg2.getStringArrayListExtra("skus");
			for (String sku : skus) {
				data.add(sku);
			}
			mAdapter.notifyDataSetChanged();
		}
		else if(arg0 == 2)
		{
			data.clear();
			mAdapter.notifyDataSetChanged();
		}
	}
	class SellAdapter extends MyBaseAdapter<String> {

		public SellAdapter(List<String> data) {
			super(data);
		}

		@Override
		public BaseHolder<?> setHolder() {
			return new SellHolder();
		}

	}
	@Override
	public void onStart() {
		super.onStart();
		mPager.setState(LoadingPager.STATE_LOAD_SUCCESS);
	}
	protected void showWaitNetDialog() {
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setCancelable(false);
		builder.setOnKeyListener(mKeylistener);
		builder.setMessage("正在获取商品信息请稍后......");
		mDialog = builder.create();
		mDialog.show();
	}
}
