package com.electronicBusiness.activity;

import java.util.List;

import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.electronicBusiness.R;
import com.electronicBusiness.base.BaseActivity;
import com.electronicBusiness.base.BaseHolder;
import com.electronicBusiness.base.MyBaseAdapter;
import com.electronicBusiness.domain.MyPlanBean;
import com.electronicBusiness.domain.MyPlanDetailsBean;
import com.electronicBusiness.domain.PlanDetailsBean;
import com.electronicBusiness.holder.PlanDetailHolder;
import com.electronicBusiness.manager.OkHttpClientManager;
import com.electronicBusiness.manager.OkHttpClientManager.Param;
import com.electronicBusiness.manager.OkHttpClientManager.ResultCallback;
import com.electronicBusiness.utils.ToastUtils;
import com.squareup.okhttp.Request;

public class TaskDetailActivity extends BaseActivity {

	private ListView mLv;
	private ProgressBar mPb;
	@Override
	public void initView() {
		setContentView(R.layout.activity_plan_detail);
		mLv = (ListView) findViewById(R.id.lv);
		mPb = (ProgressBar) findViewById(R.id.pb);
	}
	@Override
	protected void initData() {
		super.initData();
		Param param = new Param("planId",getIntent().getIntExtra("planId", 0)+"");
		
		OkHttpClientManager.postAsyn("http://"+mIp+":"+mPort+"/"+ com.electronicBusiness.manager.ConfigurationManager.project+"/check/get_plan_detail", new ResultCallback<List<PlanDetailsBean>>()
		{

					@Override
					public void onError(Request request, Exception e) {
						ToastUtils.showToast("网络通讯失败");
						mPb.setVisibility(View.INVISIBLE);
						mLv.setVisibility(View.VISIBLE);
					}
					@Override
					public void onResponse(List<PlanDetailsBean> list) {
						if(list!=null&&list.size()>0)
						{
							mLv.setAdapter(new PlanDetailAdapter(list));
							mPb.setVisibility(View.INVISIBLE);
							mLv.setVisibility(View.VISIBLE);
						}
					}
			
		},param);
	}
	class PlanDetailAdapter extends MyBaseAdapter<PlanDetailsBean>
	{

		public PlanDetailAdapter(List<PlanDetailsBean> data) {
			super(data);
		}

		@Override
		public BaseHolder<?> setHolder() {
			return new PlanDetailHolder();
		}
	}
}
