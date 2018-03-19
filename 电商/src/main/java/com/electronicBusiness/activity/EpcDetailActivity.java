package com.electronicBusiness.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.electronicBusiness.R;
import com.electronicBusiness.base.BaseActivity;
import com.electronicBusiness.domain.ExecutePlanDetailsBean;
import com.electronicBusiness.domain.MyGoodsMegBean;
import com.electronicBusiness.manager.OkHttpClientManager;
import com.electronicBusiness.manager.OkHttpClientManager.Param;
import com.electronicBusiness.manager.OkHttpClientManager.ResultCallback;
import com.electronicBusiness.utils.ToastUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Request;

public class EpcDetailActivity extends BaseActivity {

	@ViewInject(R.id.tv_name)
	private TextView tv_name;
	@ViewInject(R.id.tv_type)
	private TextView tv_type;
	@ViewInject(R.id.tv_epc)
	private TextView tv_epc;
	@ViewInject(R.id.tv_sku)
	private TextView tv_sku;
	@ViewInject(R.id.tv_position)
	private TextView tv_position;
	@ViewInject(R.id.pb)
	private ProgressBar pb;
	@ViewInject(R.id.ll)
	private LinearLayout ll;
	@Override
	public void initView() {
		setContentView(R.layout.activity_epc_detail);
		ViewUtils.inject(this);
	}
	@Override
	protected void initData() {
		super.initData();
		ExecutePlanDetailsBean bean = (ExecutePlanDetailsBean) getIntent().getSerializableExtra("epc");
		tv_epc.setText("epc:"+bean.getEpc());
		tv_sku.setText("sku:"+bean.getSku());
		tv_name.setText("商品名称:"+bean.getName());
		tv_type.setText("商品类型:"+bean.getType());
		tv_position.setText("商品位置:"+bean.getPosition());
	}
}
