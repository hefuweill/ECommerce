package com.electronicBusiness.holder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.electronicBusiness.R;
import com.electronicBusiness.base.BaseHolder;
import com.electronicBusiness.domain.MySaleBean;
import com.electronicBusiness.domain.SalesOrderBean;
import com.electronicBusiness.utils.ToastUtils;
import com.electronicBusiness.utils.UIUtils;

public class SellEpcHolder extends BaseHolder<SalesOrderBean> {

	private TextView mTv_name;
	private TextView mTv_epc;
	private TextView mTv_sku;
	private CheckBox cb;

	@Override
	public void changeViewStyle(final SalesOrderBean data) {
		mTv_name.setText(data.getGoodName());
		mTv_sku.setText(data.getSku());
		mTv_epc.setText(data.getEpc());
		cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				data.setSelect(isChecked);
			}
		});
	}

	@Override
	public View initView() {
		View v = View.inflate(UIUtils.getContext(), R.layout.holder_sell_epc,
				null);
		mTv_name = (TextView) v.findViewById(R.id.tv_goods_name);
		mTv_epc = (TextView) v.findViewById(R.id.tv_goods_epc);
		mTv_sku = (TextView) v.findViewById(R.id.tv_goods_sku);
		cb = (CheckBox) v.findViewById(R.id.cb);

		return v;
	}

}
