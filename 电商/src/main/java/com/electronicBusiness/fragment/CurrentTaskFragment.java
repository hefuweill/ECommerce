package com.electronicBusiness.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.electronicBusiness.R;
import com.electronicBusiness.base.BaseFragment;
import com.electronicBusiness.base.BaseHolder;
import com.electronicBusiness.base.MyBaseAdapter;
import com.electronicBusiness.domain.MyPlanBean;
import com.electronicBusiness.fragment.CheckPlanFragment.PlanListener;
import com.electronicBusiness.holder.CurrPlanHolder;
import com.electronicBusiness.manager.OkHttpClientManager;
import com.electronicBusiness.manager.OkHttpClientManager.ResultCallback;
import com.electronicBusiness.utils.ToastUtils;
import com.electronicBusiness.view.LoadingPager;
import com.electronicBusiness.view.LoadingPager.StateType;
import com.squareup.okhttp.Request;


public class CurrentTaskFragment extends BaseFragment{

	private List<MyPlanBean> data = new ArrayList<MyPlanBean>();
	private ListView lv;
	private SwipeRefreshLayout mSr;
	private CurrPlanAdapter mAdapter;
	@Override
	public StateType loadData() {
		CheckPlanFragment.setPlanListener(new PlanListener() {
			
			@Override
			public void onReceivePlan() {
				mPager.removeSuccessView();
				initData();
			}
		});
		initData();
		return StateType.STATE_LOAD;
	}
	private void initData() {
		OkHttpClientManager.postAsyn("http://" + mIp + ":" + mPort
				+ "/"+ com.electronicBusiness.manager.ConfigurationManager.project+"/check/current_plan",
				new ResultCallback<List<MyPlanBean>>() {

					@Override
					public void onError(Request request, Exception e) {
						ToastUtils.showToast("网络通讯失败");
						if(mSr!=null)
						{
							mSr.setRefreshing(false);
						}
						mPager.setState(LoadingPager.STATE_LOAD_ERROR);
					}

					@Override
					public void onResponse(List<MyPlanBean> data) {
						if(mSr!=null)
						{
							mSr.setRefreshing(false);
						}
						if(data!=null)
						{
							CurrentTaskFragment.this.data.clear();
							CurrentTaskFragment.this.data.addAll(data);
							if(mAdapter!=null)//重新加载数据时刷新界面
							{
								mAdapter.notifyDataSetChanged();
							}
							if(data.size()>0)
							{
//								ToastUtils.showToast(data.size()+"");
//								System.out.println(data.size()+"条");
							}
							mPager.setState(LoadingPager.STATE_LOAD_SUCCESS);
							return ;
						}
						mPager.setState(LoadingPager.STATE_LOAD_ERROR);
					}

				});
	}

	@Override
	public View createSuccessView() {
		View v = null;
		if(data.size()>0)
		{
			v = View.inflate(getActivity(), R.layout.fragment_curr_plan, null);
			lv = (ListView) v.findViewById(R.id.lv);
			mSr = (SwipeRefreshLayout) v.findViewById(R.id.swiperefreshlayout);
			mSr.setColorSchemeResources(R.color.swiperefresh_color1, R.color.swiperefresh_color2,
	                R.color.swiperefresh_color3, R.color.swiperefresh_color4);
			mSr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
	            @Override
	            public void onRefresh() {
	                initData();
	            }
	        });
			mAdapter = new CurrPlanAdapter(data);
			lv.setAdapter(mAdapter);
		}
		else
		{
			v = View.inflate(getActivity(), R.layout.fragment_no_curr_plan, null);
			
		}
		return v;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		mPager.initData();
	}
	class CurrPlanAdapter extends MyBaseAdapter<MyPlanBean>
	{

		public CurrPlanAdapter(List<MyPlanBean> data) {
			super(data);
		}

		@Override
		public BaseHolder<?> setHolder() {
			CurrPlanHolder holder = new CurrPlanHolder(CurrentTaskFragment.this); 
			return holder;
		}
		
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		initData();
		super.onActivityResult(requestCode, resultCode, data);
	}
}
