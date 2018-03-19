package com.electronicBusiness.fragment;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;
import android.widget.TextView;

import com.electronicBusiness.R;
import com.electronicBusiness.base.BaseFragment;
import com.electronicBusiness.base.BaseHolder;
import com.electronicBusiness.base.MyBaseAdapter;
import com.electronicBusiness.domain.MyPlanBean;
import com.electronicBusiness.holder.CheckPlanHolder;
import com.electronicBusiness.manager.OkHttpClientManager;
import com.electronicBusiness.manager.OkHttpClientManager.ResultCallback;
import com.electronicBusiness.utils.ToastUtils;
import com.electronicBusiness.view.LoadingPager;
import com.electronicBusiness.view.LoadingPager.StateType;
import com.squareup.okhttp.Request;

public class CheckPlanFragment extends BaseFragment{
	private ListView mLv;
	private SwipeRefreshLayout mSr;
	private CheckPlanAdapter mCheckPlanAdapter;
	private List<MyPlanBean> mPlanList;
	private static PlanListener mListener;
	private TextView tv_retry;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	@Override
	public void onStart() {
		super.onStart();
		initData();
	}
	private void initData() {
		OkHttpClientManager.postAsyn("http://"+mIp+":"+mPort+"/"+ com.electronicBusiness.manager.ConfigurationManager.project+"/check/get_plan", new ResultCallback<List<MyPlanBean>>()
		{

					@Override
					public void onError(Request request, Exception e) {
						if(mSr!=null)
						{
							mSr.setRefreshing(false);
						}
						mPager.setState(LoadingPager.STATE_LOAD_ERROR);//设置失败界面
					}
					@Override
					public void onResponse(List<MyPlanBean> response) {
						if(mSr!=null)
						{
							mSr.setRefreshing(false);
						}
						if(mPlanList==null)
						{
							mPlanList = new ArrayList<MyPlanBean>();
						}
						mPlanList.clear();
						mPlanList.addAll(response);
						if(mCheckPlanAdapter!=null)//重新加载数据时刷新界面
						{
							mCheckPlanAdapter.notifyDataSetChanged();
						}
						mPager.setState(LoadingPager.STATE_LOAD_SUCCESS);
					}
			
		});
	}
	@Override
	public StateType loadData() {
		initData();
		return StateType.STATE_LOAD;
	}
	@Override
	public View createSuccessView() {
		View v = null;
		if(mPlanList.size()>0)
		{
			v = View.inflate(getActivity(), R.layout.fragment_check_plan, null);
			mLv = (ListView) v.findViewById(R.id.lv);
			mSr = (SwipeRefreshLayout) v.findViewById(R.id.swiperefreshlayout);
			mLv.setSelector(new ColorDrawable());
			mSr.setColorSchemeResources(R.color.swiperefresh_color1, R.color.swiperefresh_color2,
	                R.color.swiperefresh_color3, R.color.swiperefresh_color4);
			mSr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
	            @Override
	            public void onRefresh() {
	                initData();
	            }
	        });
			mCheckPlanAdapter = new CheckPlanAdapter(mPlanList);
			mLv.setAdapter(mCheckPlanAdapter);
		}
		else{
			v = View.inflate(getActivity(), R.layout.fragment_no_plan, null);
			tv_retry = (TextView) v.findViewById(R.id.tv_retry);
			tv_retry.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mPager.setState(LoadingPager.STATE_LOAD_EMPTY);
					mPager.removeSuccessView();
					mPager.initData();
				}
			});
		}
		return v;
	}
	class CheckPlanAdapter extends MyBaseAdapter<MyPlanBean>
	{

		public CheckPlanAdapter(List<MyPlanBean> data) {
			super(data);
		}

		@Override
		public BaseHolder<?> setHolder() {
			CheckPlanHolder holder = new CheckPlanHolder() {

				@Override
				public void receiveSuccess(View view,final MyPlanBean data) {
					TranslateAnimation animation = new TranslateAnimation(
							Animation.RELATIVE_TO_SELF, 0,
							Animation.RELATIVE_TO_SELF, -1.0f,
							Animation.RELATIVE_TO_SELF, 0,
							Animation.RELATIVE_TO_SELF, 0);
					animation.setDuration(300);
					view.startAnimation(animation);
					new Thread(){
						public void run() {
							SystemClock.sleep(300);
							mPlanList.remove(data);
							if(mCheckPlanAdapter!=null)
							{
								getActivity().runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										mCheckPlanAdapter.notifyDataSetChanged();
										if(mListener!=null)
										{
											mListener.onReceivePlan();
										}
									}
								});
							}
						};
					}.start();
				}
				
			};
			return holder;
		}
		
	}
	interface PlanListener
	{
		void onReceivePlan();
	}
	public static void setPlanListener(PlanListener listener)
	{
		mListener = listener;
	}
}
