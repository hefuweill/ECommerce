package com.electronicBusiness.holder;

import android.view.View;
import android.widget.TextView;

import com.electronicBusiness.R;
import com.electronicBusiness.base.BaseHolder;
import com.electronicBusiness.domain.HistoryDetailsBean;
import com.electronicBusiness.domain.MyHistoryDetailsBean;
import com.electronicBusiness.utils.UIUtils;

public class HistoryDetailHolder extends BaseHolder<HistoryDetailsBean> {

	private TextView mTv_name;
	private TextView mTv_sku;
	private TextView mTv_num;
	private TextView mTv_pan;
	private TextView mTv_type;
	private TextView mTv_position;

	@Override
	public void changeViewStyle(HistoryDetailsBean data) {
		mTv_name.setText(data.getGoodName());
		mTv_num.setText(data.getSumNum()+"");
		mTv_pan.setText(data.getFindNum()+"");
		mTv_sku.setText(data.getSku());
		mTv_type.setText(data.getTyName());
		mTv_position.setText(data.getPosition());
	}

	@Override
	public View initView() {
		View v = View.inflate(UIUtils.getContext(), R.layout.holder_history_detail, null);
		mTv_name = (TextView) v.findViewById(R.id.tv_name);
		mTv_sku = (TextView) v.findViewById(R.id.tv_sku);
		mTv_num = (TextView) v.findViewById(R.id.tv_num);
		mTv_pan = (TextView) v.findViewById(R.id.tv_pan);
		mTv_type = (TextView) v.findViewById(R.id.tv_type);
		mTv_position = (TextView) v.findViewById(R.id.tv_position);
		return v; 
	}

}
