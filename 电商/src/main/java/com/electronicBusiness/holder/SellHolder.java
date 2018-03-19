package com.electronicBusiness.holder;

import android.view.View;
import android.widget.TextView;

import com.electronicBusiness.R;
import com.electronicBusiness.base.BaseHolder;
import com.electronicBusiness.utils.UIUtils;

public class SellHolder extends BaseHolder<String> {

	private TextView mTv;

	@Override
	public void changeViewStyle(String data) {
		mTv.setText(data);
	}

	@Override
	public View initView() {
		View v = View.inflate(UIUtils.getContext(), R.layout.holder_sell, null);
		mTv = (TextView) v.findViewById(R.id.tv);
		return v;
	}

}
