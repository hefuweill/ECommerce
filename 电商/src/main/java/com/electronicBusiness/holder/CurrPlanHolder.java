package com.electronicBusiness.holder;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.electronicBusiness.R;
import com.electronicBusiness.activity.BlindActivity;
import com.electronicBusiness.activity.ExeCheckActivity;
import com.electronicBusiness.activity.TaskDetailActivity;
import com.electronicBusiness.base.BaseHolder;
import com.electronicBusiness.domain.MyPlanBean;
import com.electronicBusiness.manager.ConfigurationManager;
import com.electronicBusiness.utils.ToastUtils;
import com.electronicBusiness.utils.UIUtils;

public class CurrPlanHolder extends BaseHolder<MyPlanBean>{

	private TextView mTv_plan_name;
	private TextView mTv_plan_type;
	private TextView mTv_plan_time;
	private TextView mTv_start_plan;
	private TextView mTv_see_detail;
	private Fragment mFragment;
	@Override
	public void changeViewStyle(final MyPlanBean data) {
		mTv_plan_name.setText(data.getName());
		mTv_plan_type.setText(UIUtils.getType(data.getIsBlind()));
		mTv_plan_time.setText(UIUtils.getFormatterTime(data.getAddTime()));
		mTv_start_plan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(data.getIsBlind()==ConfigurationManager.MANGPAN)
				{
					Intent intent = new Intent(UIUtils.getContext(),BlindActivity.class);
					intent.putExtra("planId", data.getId());
					intent.putExtra("isBlind", data.getIsBlind());
					mFragment.startActivityForResult(intent, 1);
				}
				else
				{
					Intent intent = new Intent(UIUtils.getContext(),ExeCheckActivity.class);
					intent.putExtra("planId", data.getId());
					intent.putExtra("isBlind", data.getIsBlind());
					mFragment.startActivityForResult(intent, 1);
				}
			}
		});
		mTv_see_detail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(data.getIsBlind()==1)
				{
					ToastUtils.showToast("盲盘无详情");
					return ;
				}
				Intent intent = new Intent(UIUtils.getContext(),TaskDetailActivity.class);
				intent.putExtra("planId", data.getId());
				mFragment.startActivity(intent);
					
			}
		});
	}
	public CurrPlanHolder(Fragment fragment) {
		super();
		mFragment = fragment;
	}
	@Override
	public View initView() {
		View v = View.inflate(UIUtils.getContext(),
				R.layout.item_currplan_list, null);
		mTv_plan_name = (TextView) v.findViewById(R.id.tv_plan_name);
		mTv_plan_type = (TextView) v.findViewById(R.id.tv_plan_type);
		mTv_plan_time = (TextView) v.findViewById(R.id.tv_plan_time);
		mTv_start_plan = (TextView) v.findViewById(R.id.tv_start_plan);
		mTv_see_detail = (TextView) v.findViewById(R.id.tv_see_detail);
		return v;
	}

}
