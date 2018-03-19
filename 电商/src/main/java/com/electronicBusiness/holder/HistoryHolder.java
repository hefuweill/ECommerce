package com.electronicBusiness.holder;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.view.View;
import android.widget.TextView;

import com.electronicBusiness.R;
import com.electronicBusiness.base.BaseHolder;
import com.electronicBusiness.domain.ScanResultBean;
import com.electronicBusiness.utils.UIUtils;

public class HistoryHolder extends BaseHolder<ScanResultBean> {

	private TextView mTv_status;
	private TextView mTv_tasktime;
	private TextView mTv_taskname;

	@SuppressWarnings("deprecation")
	@Override
	public void changeViewStyle(ScanResultBean data) {
		mTv_status.setText(data.getStatus()==1?"清算完整":"存在缺失");
		mTv_taskname.setText(data.getPlanName());
		System.out.println(data.getFinishTime()+"finish");
		mTv_tasktime.setText(gettTime(data.getFinishTime()));
	}

	@Override
	public View initView() {
		View v = View.inflate(UIUtils.getContext(), R.layout.holder_history, null);
		mTv_status = (TextView) v
				.findViewById(R.id.tv_taskstatus);
		mTv_tasktime = (TextView) v
				.findViewById(R.id.tv_tasktime);
		mTv_taskname = (TextView) v
				.findViewById(R.id.tv_taskname);
		return v;
	}
	private String gettTime(int time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date(time*1000L));
	}
}
