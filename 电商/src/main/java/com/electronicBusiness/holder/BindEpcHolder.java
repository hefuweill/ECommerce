package com.electronicBusiness.holder;

import android.view.View;
import android.widget.TextView;

import com.electronicBusiness.R;
import com.electronicBusiness.base.BaseHolder;
import com.electronicBusiness.utils.UIUtils;

public class BindEpcHolder extends BaseHolder<String>{

	private TextView mTv_no;
	private TextView mTv_epc;

	@Override
	public void changeViewStyle(String data) {
		mTv_epc.setText(data);
	}
	@Override
	public void changeViewStyle(String data,int position) {
		changeViewStyle(data);
		mTv_no.setText((position+1)+"");
	}
	@Override
	public View initView() {
		View v = View.inflate(UIUtils.getContext(), R.layout.item_lv, null);
		mTv_no = (TextView) v.findViewById(R.id.tv_no);
		mTv_epc = (TextView) v.findViewById(R.id.tv_epc);
		return v;
	}

}
