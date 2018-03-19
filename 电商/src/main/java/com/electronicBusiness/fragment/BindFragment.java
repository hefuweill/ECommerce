package com.electronicBusiness.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import rfid.ivrjacku1.IvrJackStatus;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.electronicBusiness.R;
import com.electronicBusiness.activity.ConnectionActivity;
import com.electronicBusiness.activity.MipcaActivityCapture;
import com.electronicBusiness.activity.SubmitInfoActivity;
import com.electronicBusiness.activity.UnBindActivity;
import com.electronicBusiness.base.BaseApplication;
import com.electronicBusiness.base.BaseApplication.onConnectListener;
import com.electronicBusiness.base.BaseFragment;
import com.electronicBusiness.base.BaseHolder;
import com.electronicBusiness.base.MyBaseAdapter;
import com.electronicBusiness.domain.InputBean;
import com.electronicBusiness.domain.PositionBean;
import com.electronicBusiness.holder.BindEpcHolder;
import com.electronicBusiness.manager.ConfigurationManager;
import com.electronicBusiness.manager.OkHttpClientManager;
import com.electronicBusiness.manager.OkHttpClientManager.Param;
import com.electronicBusiness.manager.OkHttpClientManager.ResultCallback;
import com.electronicBusiness.utils.ToastUtils;
import com.electronicBusiness.utils.UIUtils;
import com.electronicBusiness.view.LoadingPager;
import com.electronicBusiness.view.LoadingPager.StateType;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Request;

public class BindFragment extends BaseFragment {

	@ViewInject(R.id.tv_barcode)
	private TextView tv_barcode;
	@ViewInject(R.id.tv_scan)
	private TextView tv_scan;
	@ViewInject(R.id.tv_start)
	private TextView tv_start;
	@ViewInject(R.id.tv_bind)
	private TextView tv_bind;
	@ViewInject(R.id.tv_clear)
	private TextView tv_clear;
	@ViewInject(R.id.lv)
	private ListView lv;
	@ViewInject(R.id.tv_unbind)
	private TextView tv_unbind;
	@ViewInject(R.id.ivbtn1)
	private ImageView ivbtn1;
	@ViewInject(R.id.fab)
	private FloatingActionButton fab;
	@ViewInject(R.id.ll)
	private LinearLayout ll;
	private boolean isReading = true;
	private List<String> data = new ArrayList<String>();
	private MyEpcAdapter mAdapter;
	private AlertDialog mDialog;
	private String mSku;
	public final int CHANGE_START = 1;
	public final int CHANGE_STOP = 2;
	private AlertDialog mInputDialog;
	private List<PositionBean> list;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CHANGE_START:
				tv_start.setText("开始");
				isReading = true;
				break;
			case CHANGE_STOP:
				tv_start.setText("暂停");
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
	public View createSuccessView() {
		View view = View.inflate(getContext(), R.layout.fragment_bind, null);
		ViewUtils.inject(this,view);
		mAdapter = new MyEpcAdapter(data);
		lv.setAdapter(mAdapter);
		return view;
	}


	@Override
	public void onResume() {
		super.onResume();
		initEvent();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data!=null&&resultCode==1)
		{
			mSku = data.getStringExtra("result");
			tv_barcode.setText(mSku);
			tv_scan.setVisibility(View.INVISIBLE);
			tv_barcode.setVisibility(View.VISIBLE);
		}
		if(resultCode==2)
		{
			//从绑定界面回来
			afterBind();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	private void afterBind()
	{
		stop();
		data.clear();
		tv_barcode.setText("");
		tv_barcode.setVisibility(View.INVISIBLE);
		tv_scan.setVisibility(View.VISIBLE);
		mAdapter.notifyDataSetChanged();
	}
	protected void initEvent() {
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
		ivbtn1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivityForResult((new Intent(getActivity(),MipcaActivityCapture.class)),0);
			}
		});
		tv_unbind.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(),UnBindActivity.class));
			}
		});
		tv_scan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivityForResult((new Intent(getActivity(),MipcaActivityCapture.class)),0);
			}
		});
		tv_bind.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!TextUtils.isEmpty(tv_barcode.getText().toString()))
				{
					stop();
					if (data.size() == 0) {
						ToastUtils.showToast("请至少扫描一个epc");
					}
					if (data.size() > 0) {
						showInputDialog();
					}
				}
				else
				{
					ToastUtils.showToast("请先扫描SKU再点击绑定");
				}
			}
		});
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
	}

	private void showInputDialog() {
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setCancelable(false);
		builder.setOnKeyListener(mKeylistener);
		View v = View.inflate(UIUtils.getContext(), R.layout.dialog_show_input,
				null);
		LinearLayout ll = (LinearLayout) v.findViewById(R.id.ll);
		ProgressBar pb = (ProgressBar) v.findViewById(R.id.pb);
		final Spinner spinner = (Spinner) v.findViewById(R.id.sp_save);
		connectInternet(spinner,ll,pb);
		TextView tv_confirm = (TextView) v.findViewById(R.id.tv_confirm);
		TextView tv_cancal = (TextView) v.findViewById(R.id.tv_cancal);
		final EditText et_goods_source = (EditText) v
				.findViewById(R.id.et_goods_source);
		tv_cancal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mInputDialog != null) {
					mInputDialog.dismiss();
				}
			}
		});
		tv_confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(et_goods_source.getText().toString())) {
					connInternet(spinner.getSelectedItemPosition(),et_goods_source.getText().toString());
					if (mInputDialog != null) {
						mInputDialog.dismiss();
					}
				} else {
					ToastUtils.showToast("请输入商品来源");
				}
			}
		});
		builder.setView(v);
		mInputDialog = builder.create();
		mInputDialog.show();
	}

	/**
	 * 获取位置信息
	 * @param spinner
	 * @param ll
     * @param pb
     */
	private void connectInternet(final Spinner spinner, final LinearLayout ll, final ProgressBar pb) {
		OkHttpClientManager.postAsyn("http://"+mIp+":"+mPort+"/"+ com.electronicBusiness.manager.ConfigurationManager.project+"/supply/get_position",new ResultCallback<List<PositionBean>>(){

			@Override
			public void onError(Request request, Exception e) {
				ToastUtils.showToast("网络通讯失败");
				mInputDialog.dismiss();
			}

			@Override
			public void onResponse(List<PositionBean> list) {
				BindFragment.this.list = list;
				List<PositionBean> data =  new ArrayList();
				Iterator<PositionBean> iterator = list.iterator();
				while (iterator.hasNext())
				{
					PositionBean next = iterator.next();
					PositionBean bean = new PositionBean();
					bean.setPosition(next.getArea()+"-"+next.getPosition());
					bean.setId(next.getId());
					data.add(bean);
				}
				List<String> stringList = new ArrayList<>();
				Iterator<PositionBean> iterator1 = data.iterator();
				while (iterator1.hasNext())
				{
					PositionBean next = iterator1.next();
					stringList.add(next.getPosition());
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,stringList);
				adapter.setDropDownViewResource(R.layout.drop_down_item);
				spinner.setAdapter(adapter);
				pb.setVisibility(View.INVISIBLE);
				ll.setVisibility(View.VISIBLE);
			}

		});
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
//	private void start() {
//		if (ConfigurationManager.startEquip() == 0)// 关闭成功
//		{
//			handler.sendEmptyMessage(CHANGE_STOP);
//		}
//	}
	protected void reFreshData(List<String> epcs) {
		for (String epc : epcs) {
			if (!data.contains(epc)) {
				data.add(epc);
			}
		}
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * bind访问网络

	 * @param s
	 */
	private void connInternet(final int position, final String s) {
		showWaitNetDialog();
		final Param param1 = createBindParam(position,s);
		// 访问网络绑定回到扫描sku界面
		OkHttpClientManager.postAsyn("http://" + mIp + ":" + mPort
				+ "/"+ com.electronicBusiness.manager.ConfigurationManager.project+"/supply/input_bind", new ResultCallback<String>() {

			@Override
			public void onError(Request request, Exception e) {
				ToastUtils.showToast("访问网络失败" + e.getMessage());
				mDialog.dismiss();
			}

			@Override
			public void onResponse(String response) {
				mDialog.dismiss();
				System.out.println("response" + response);
				if (response.equals("200")) {
					ToastUtils.showToast("绑定成功");
					afterBind();
				} else if (response.equals("400")) {
					ToastUtils.showToast("绑定失败");
				} else if (response.equals("500")) {
					// 跳转至填写商品信息的页面
					Intent intent = new Intent(UIUtils.getContext(),
							SubmitInfoActivity.class);
					intent.putExtra("sku", mSku);
					intent.putExtra("bean",createBindBean(position,s));
					startActivityForResult(intent,1);
				}
				else if(response.startsWith("200")&&response.length()>3)
				{
					ToastUtils.showToast("由于上述epc中有"+response.substring(3)+"个已经绑定，因此本次入库失败");
					//已经有？？个被绑定‘
					afterBind();
				}
			}

		}, param1);
	}
	protected Param createBindParam(int position, String s) {
		InputBean bean = createBindBean(position,s);
		Gson gson = new Gson();
		String json = gson.toJson(bean);
		return new Param("bindBean", json);
	}

	private InputBean createBindBean(int position, String s) {
//		mSku = "6933266607697";//需要删除
		InputBean bean = new InputBean();
		bean.setEpc(data);
		bean.setSku(mSku);
		bean.setPositionId(list.get(position).getId());
		bean.setFrom(s);
		return bean;
	}

	protected void showWaitNetDialog() {
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setCancelable(false);
		builder.setOnKeyListener(mKeylistener);
		builder.setMessage("正在上传请稍后......");
		mDialog = builder.create();
		mDialog.show();
	}
	@Override
	public void onStart() {
		super.onStart();
		mPager.setState(LoadingPager.STATE_LOAD_SUCCESS);
	}
	
	public class MyEpcAdapter extends MyBaseAdapter<String> {

		public MyEpcAdapter(List<String> data) {
			super(data);
		}

		@Override
		public BaseHolder<?> setHolder() {
			return new BindEpcHolder();
		}
	}

}
