package com.electronicBusiness.holder;

import android.view.View;
import android.widget.TextView;

import com.electronicBusiness.R;
import com.electronicBusiness.base.BaseHolder;
import com.electronicBusiness.domain.MyPlanDetailsBean;
import com.electronicBusiness.domain.PlanDetailsBean;
import com.electronicBusiness.utils.UIUtils;

public class PlanDetailHolder extends BaseHolder<PlanDetailsBean>{

	private TextView tv_name;
	private TextView tv_sku;
	private TextView tv_num;
	private TextView tv_position;
	@Override
	public void changeViewStyle(final PlanDetailsBean data) {
		tv_name.setText(data.getName());
		tv_sku.setText(data.getSku());
		tv_num.setText(data.getNum()+"");
		tv_position.setText(data.getPosition());
	}

	@Override
	public View initView() {
		View v = View.inflate(UIUtils.getContext(),
				R.layout.item_task_detail, null);
		tv_name = (TextView)v.findViewById(R.id.tv_goods_name);
		tv_sku = (TextView)v.findViewById(R.id.tv_goods_sku);
		tv_num = (TextView)v.findViewById(R.id.tv_num);
		tv_position = (TextView) v.findViewById(R.id.tv_goods_position);
		return v;
	}

}
