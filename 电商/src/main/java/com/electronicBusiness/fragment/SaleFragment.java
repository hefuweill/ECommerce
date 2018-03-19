package com.electronicBusiness.fragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.electronicBusiness.R;
import com.electronicBusiness.activity.OrderActivity;
import com.electronicBusiness.base.BaseFragment;
import com.electronicBusiness.base.BaseHolder;
import com.electronicBusiness.base.MyBaseAdapter;
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

import java.util.ArrayList;
import java.util.List;

import android.hardware.uhf.magic.reader;

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
	public final int CHANGE_START = 2;
	public final int CHANGE_STOP = 3;
	private boolean isReading = false;
	private ArrayList<String> data = new ArrayList<String>();
	private SellAdapter mAdapter;
	private AlertDialog mDialog;
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
		}

		;
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

		tv_clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				data.clear();
				mAdapter.notifyDataSetChanged();
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
	protected void reFreshData(String epc) {
		if (!data.contains(epc)) {
			data.add(epc);
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
