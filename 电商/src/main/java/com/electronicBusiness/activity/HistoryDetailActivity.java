package com.electronicBusiness.activity;

import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.electronicBusiness.R;
import com.electronicBusiness.base.BaseActivity;
import com.electronicBusiness.base.BaseHolder;
import com.electronicBusiness.base.MyBaseAdapter;
import com.electronicBusiness.domain.HistoryDetailsBean;
import com.electronicBusiness.domain.MyHistoryDetailsBean;
import com.electronicBusiness.holder.HistoryDetailHolder;
import com.electronicBusiness.manager.ConfigurationManager;
import com.electronicBusiness.manager.OkHttpClientManager;
import com.electronicBusiness.manager.OkHttpClientManager.Param;
import com.electronicBusiness.manager.OkHttpClientManager.ResultCallback;
import com.electronicBusiness.utils.ToastUtils;
import com.electronicBusiness.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Request;

public class HistoryDetailActivity extends BaseActivity {

	@ViewInject(R.id.pb)
	private ProgressBar pb;
	@ViewInject(R.id.lv)
	private ListView lv;
	@ViewInject(R.id.tv_total)
	private TextView tv_total;
	@Override
	public void initView() {
		setContentView(R.layout.activity_history_detail);
		ViewUtils.inject(this);
	}
	@Override
	protected void initData() {
		super.initData();
		Param param = new Param("planId",getIntent().getIntExtra("planId", 0)+"");
		OkHttpClientManager.postAsyn("http://"+mIp+":"+mPort+"/"+ com.electronicBusiness.manager.ConfigurationManager.project+"/check/get_history_plan_detail",new ResultCallback<List<HistoryDetailsBean>>(){

			private HistoryDetailAdapter mAdapter;

			@Override
			public void onError(Request request, Exception e) {
				ToastUtils.showToast("网络通讯失败");
			}

			@Override
			public void onResponse(List<HistoryDetailsBean> list) {
				pb.setVisibility(View.INVISIBLE);
				lv.setVisibility(View.VISIBLE);
				mAdapter = new HistoryDetailAdapter(list);
				lv.setAdapter(mAdapter);
				int ysum = 0;
				int snum = 0;
				for(HistoryDetailsBean bean:list)
				{
					ysum+=bean.getSumNum();
					snum+=bean.getFindNum();
				}
				tv_total.setText("应盘："+ysum+"  实盘："+snum+"  差额："+(ysum-snum));
			}
			
		},param);
	}
	class HistoryDetailAdapter extends MyBaseAdapter<HistoryDetailsBean>
	{

		public HistoryDetailAdapter(List<HistoryDetailsBean> data) {
			super(data);
		}

		@Override
		public BaseHolder<?> setHolder() {
			return new HistoryDetailHolder();
		}
		
	}
}
