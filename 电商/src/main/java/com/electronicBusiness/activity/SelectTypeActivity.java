package com.electronicBusiness.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.electronicBusiness.R;
import com.electronicBusiness.base.BaseActivity;
import com.electronicBusiness.domain.MyTypeBean;
import com.electronicBusiness.fragment.SubmitTypeFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class SelectTypeActivity extends BaseActivity{

	private static FragmentManager mManager;
	public static SubmitTypeFragment mFragment1 = null;
	private static String type = "";
	private static SelectTypeActivity activity;
	@ViewInject(R.id.tv)
	private TextView tv;
	static Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			activity.setResult(1, (Intent) msg.obj);
			activity.finish();
		}
	};
	@Override
	public void initView() {
		activity = this;
		setContentView(R.layout.activity_select_type);
		ViewUtils.inject(this);
	}
	@Override
	protected void initData() {
		super.initData();
		mManager = getSupportFragmentManager();
		mFragment1 = new MyFragment();
		mManager.beginTransaction().replace(R.id.fl, mFragment1).commit();
	}
	public static class MyFragment extends SubmitTypeFragment
	{
		@Override
		public void onItemClick(int level,MyTypeBean myTypeBean) {
			type = myTypeBean.getName();
			Intent intent = new Intent();
			intent.putExtra("type", type);
			intent.putExtra("id", myTypeBean.getId());
			Message message = Message.obtain();
			message.obj = intent;
			handler.sendMessage(message);
		}

	};
//	public static class MyFragment2 extends SubmitTypeFragment
//	{
//		@Override
//		public void onItemClick(int level,MyTypeBean myTypeBean) {
//			SubmitTypeFragment fragment3 = new MyFragment3();
//			type1 = type + " " +myTypeBean.getName();
//			Bundle bundle = new Bundle();
//			bundle.putInt(PARAM_LEVEL, 3);
//			bundle.putString(PARAM, myTypeBean.getId()+"");
//			fragment3.setArguments(bundle);
//			FragmentTransaction transaction = mManager.beginTransaction();
//			transaction.hide(mFragment2)
//					.add(R.id.fl, fragment3)
//					.addToBackStack(null).commit();
//		}
//	}
//	public static class MyFragment3 extends SubmitTypeFragment
//	{
//		@Override
//		public void onItemClick(int level, MyTypeBean myTypeBean) {
//			type = type1 + " " +myTypeBean.getName();
//			Intent intent = new Intent();
//			intent.putExtra("type", type);
//			intent.putExtra("id", myTypeBean.getId());
//			Message message = Message.obtain();
//			message.obj = intent;
//			handler.sendMessage(message);
//		}
//	}
	@Override
	protected void initEvent() {
		super.initEvent();
		tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mManager.getBackStackEntryCount()>0)
				{
					mManager.popBackStack();
				}
				else
				{
					finish();
				}
			}
		});
	}
}
