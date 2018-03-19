package com.electronicBusiness.holder;

import android.view.View;
import android.widget.TextView;

import com.electronicBusiness.R;
import com.electronicBusiness.base.BaseHolder;
import com.electronicBusiness.domain.FindMegBean;
import com.electronicBusiness.utils.UIUtils;

/**
 * Created by FuWei on 2017/4/19.
 */
public class QueryHoler extends BaseHolder<FindMegBean>{
    private TextView mTv_name;
    private TextView mTv_goods_position;
    private TextView mTv_sku;
    private TextView mTv_num;
    private TextView mTv_goods_type;

    @Override
    public void changeViewStyle(FindMegBean data) {
        mTv_name.setText(data.getGoName());
        mTv_goods_position.setText(data.getArea()+"-"+data.getPosition());
        mTv_goods_type.setText(data.getTyName());
        mTv_num.setText(data.getNum()+"个");
        mTv_sku.setText(data.getSku());
    }

    @Override
    public View initView() {
        View v = View.inflate(UIUtils.getContext(), R.layout.holder_query,null);
        mTv_name = (TextView) v.findViewById(R.id.tv_goods_name);
        mTv_goods_position = (TextView) v.findViewById(R.id.tv_goods_position);
        mTv_sku = (TextView) v.findViewById(R.id.tv_goods_sku);
        mTv_num = (TextView) v.findViewById(R.id.tv_num);
        mTv_goods_type = (TextView) v.findViewById(R.id.tv_goods_type);
        return v;
    }
}
