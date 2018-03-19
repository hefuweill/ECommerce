package com.electronicBusiness.holder;

import android.renderscript.Sampler.Value;
import android.view.View;
import android.widget.TextView;

import com.electronicBusiness.R;
import com.electronicBusiness.base.BaseHolder;
import com.electronicBusiness.domain.MySaleBean;
import com.electronicBusiness.domain.SalesOrderBean;
import com.electronicBusiness.utils.UIUtils;
import com.mining.app.zxing.view.NumberAddSubView;
import com.mining.app.zxing.view.NumberAddSubView.OnButtonClickListenter;

public class SellSkuHolder extends BaseHolder<SalesOrderBean> {

	private TextView mTv_name;
	private TextView mTv_sku;
	private TextView mTv_type;
	private TextView mTv_num;

	@Override
	public void changeViewStyle(final SalesOrderBean data) {
		mTv_name.setText(data.getGoodName());
		mTv_sku.setText(data.getSku());
		mTv_type.setText(data.getTyName());
		mTv_num.setText(data.getNum()+"个");
	}

	@Override
	public View initView() {
		View v = View.inflate(UIUtils.getContext(), R.layout.holder_sell_sku,
				null);
		mTv_name = (TextView) v.findViewById(R.id.tv_goods_name);
		mTv_sku = (TextView) v.findViewById(R.id.tv_goods_sku);
		mTv_type = (TextView) v.findViewById(R.id.tv_goods_type);
		mTv_num = (TextView) v.findViewById(R.id.tv_goods_num);
		return v;
	}
	private void changeCount(MySaleBean bean,int value)
	{
		bean.setCount(value);
	}
}
