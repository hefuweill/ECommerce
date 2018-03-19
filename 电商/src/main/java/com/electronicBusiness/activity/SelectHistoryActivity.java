package com.electronicBusiness.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;

import com.electronicBusiness.R;
import com.electronicBusiness.base.BaseActivity;
import com.electronicBusiness.base.BaseHolder;
import com.electronicBusiness.base.MyBaseAdapter;
import com.electronicBusiness.domain.MyHistoryUpBean;
import com.electronicBusiness.domain.ScanResultBean;
import com.electronicBusiness.holder.HistoryHolder;
import com.electronicBusiness.manager.OkHttpClientManager;
import com.electronicBusiness.manager.OkHttpClientManager.Param;
import com.electronicBusiness.manager.OkHttpClientManager.ResultCallback;
import com.electronicBusiness.utils.ToastUtils;
import com.electronicBusiness.utils.UIUtils;
import com.electronicBusiness.view.MyListView;
import com.electronicBusiness.view.MyListView.onRefreshListener;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Request;

public class SelectHistoryActivity extends BaseActivity {

	@ViewInject(R.id.pb)
	private ProgressBar pb;
	@ViewInject(R.id.historic_task_listview)
	private MyListView lv;
	private int currPage = 0;
	private boolean hasNext = true;
	private HistoryAdapter mAdapter;
	private List<ScanResultBean> data = new ArrayList<ScanResultBean>();
	@Override
	public void initView() {
		setContentView(R.layout.activity_select_history);
		ViewUtils.inject(this);
	}
	@Override
	protected void initData() {
		super.initData();
		String json = CreateJson();
		Param param = new Param("historyPlan",json);
		OkHttpClientManager.postAsyn("http://"+mIp+":"+mPort+"/"+ com.electronicBusiness.manager.ConfigurationManager.project+"/check/get_history_plan",new ResultCallback<List<ScanResultBean>>(){


			@Override
			public void onError(Request request, Exception e) {
				ToastUtils.showToast("网络通讯失败");
			}

			@Override
			public void onResponse(List<ScanResultBean> list) {
				currPage++;
				data.addAll(list);
				mAdapter = new HistoryAdapter(data);
				lv.setAdapter(mAdapter);
				pb.setVisibility(View.INVISIBLE);
				lv.setVisibility(View.VISIBLE);
			}
			
		},param);
	}
	@Override
	protected void initEvent() {
		super.initEvent();
		lv.setOnRefershListener(new onRefreshListener() {
			
			@Override
			public void onRefresh() {
				lv.afterFresh();
			}
			
			@Override
			public void onFootFresh() {
				if(hasNext)
				{
					getDataFromNet();
				}
				lv.afterLoad();
			}
		});
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(UIUtils.getContext(),HistoryDetailActivity.class);
				intent.putExtra("planId", data.get(position-1).getId());
				startActivity(intent);
			}
		});
	}
	protected void getDataFromNet() {
		String json = CreateJson();
		Param param = new Param("historyPlan",json);
		OkHttpClientManager.postAsyn("http://"+mIp+":"+mPort+"/"+ com.electronicBusiness.manager.ConfigurationManager.project+"/check/get_history_plan",new ResultCallback<List<ScanResultBean>>(){

			@Override
			public void onError(Request request, Exception e) {
				ToastUtils.showToast("网络通讯失败");
			}

			@Override
			public void onResponse(List<ScanResultBean> list) {
				if(list.size()<10)
				{
					hasNext = false;
				}
				currPage++;
				data.addAll(list);
				mAdapter.notifyDataSetChanged();
			}
			
		},param);
	}
	private String CreateJson() {
		MyHistoryUpBean bean = new MyHistoryUpBean();
		bean.setBigTime(getIntent().getLongExtra("endTime", 0)/1000);
		bean.setSmallTime(getIntent().getLongExtra("startTime", 0)/1000);
		bean.setPageNum(currPage);
		return new Gson().toJson(bean);
	}
	class HistoryAdapter extends MyBaseAdapter<ScanResultBean>
	{

		public HistoryAdapter(List<ScanResultBean> data) {
			super(data);
		}

		@Override
		public BaseHolder<?> setHolder() {
			return new HistoryHolder();
		}
		
	}
}
