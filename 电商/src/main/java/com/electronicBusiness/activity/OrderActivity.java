package com.electronicBusiness.activity;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.electronicBusiness.R;
import com.electronicBusiness.base.BaseActivity;
import com.electronicBusiness.base.BaseHolder;
import com.electronicBusiness.base.MyBaseAdapter;
import com.electronicBusiness.domain.DoSalesBean;
import com.electronicBusiness.domain.SalesOrderBean;
import com.electronicBusiness.holder.SellEpcHolder;
import com.electronicBusiness.holder.SellSkuHolder;
import com.electronicBusiness.manager.OkHttpClientManager;
import com.electronicBusiness.manager.OkHttpClientManager.Param;
import com.electronicBusiness.manager.OkHttpClientManager.ResultCallback;
import com.electronicBusiness.utils.ToastUtils;
import com.electronicBusiness.utils.UIUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Request;

public class OrderActivity extends BaseActivity{

	@ViewInject(R.id.lv1)
	private ListView lv1;
	@ViewInject(R.id.lv2)
	private ListView lv2;
	@ViewInject(R.id.iv_arrow1)
	private ImageView iv_arrow1; 
	@ViewInject(R.id.iv_arrow2)
	private ImageView iv_arrow2;
	@ViewInject(R.id.tv_submit)
	private TextView tv_submit;
	@ViewInject(R.id.tv_totalprice)
	private TextView tv_totalprice;
	private List<SalesOrderBean> mEpcs;
	private List<SalesOrderBean> mSkus;
	private List<SalesOrderBean> mInfoList;
	private MyEpcAdapter mEpcAdapter;
	private MySkuAdapter mSkuAdapter;
	private boolean isEpcOpen = true;
	private boolean isSkuOpen = true;
	@ViewInject(R.id.ll_root)
	private LinearLayout ll_root;
	@ViewInject(R.id.tv_delete)
	private TextView tv_delete;
	private int mLv1Height;
	private int mLv2Height;
	private AlertDialog mInputDialog;
	@Override
	public void initView() {
		setContentView(R.layout.activity_order);
		ViewUtils.inject(this);
		lv1.setDivider(new ColorDrawable(Color.TRANSPARENT));
		lv2.setDivider(new ColorDrawable(Color.TRANSPARENT));
	}
	@SuppressWarnings("unchecked")
	@Override
	protected void initData() {
		super.initData();
		Intent intent = getIntent();
		mInfoList = (List<SalesOrderBean>) intent.getSerializableExtra("infoList");
		mSkus = new ArrayList<>();
		mEpcs = new ArrayList<>();
		for(SalesOrderBean bean : mInfoList)
		{
			mEpcs.add(bean);
		}

		out:for(SalesOrderBean bean : mEpcs)
		{
			for(SalesOrderBean skuBean : mSkus)
			{
				if(bean.getSku().equals(skuBean.getSku()))
				{
					continue out;
				}
			}
			mSkus.add(bean);
		}
		mSkuAdapter = new MySkuAdapter(mSkus);
		mEpcAdapter = new MyEpcAdapter(mEpcs);
		lv1.setAdapter(mEpcAdapter);
		lv2.setAdapter(mSkuAdapter);
	}
	//epc 和 sku 的对应关系   在你查询sku的时候把不重复的sku保存在一个列表中 然后创建一个类里面两个字段 sku 和 epc 也存入一个List然后你返回数据的时候遍历skulist再遍历
	@Override
	protected void initEvent() {
		iv_arrow1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toggle_Epc();
			}
		});
		iv_arrow2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toggle_Sku();
			}
		});
		tv_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//访问网络
				showInputDialog();
			}
		});
		tv_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showConfirmDialog();
			}
		});
	}

	private void showConfirmDialog() {
		AlertDialog dialog = new AlertDialog.Builder(this).setMessage("确定将选中记录从出库清单中删除吗？")
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						for(int i=0;i<mEpcs.size();i++)
						{
							if(mEpcs.get(i).isSelect())
							{
								for (SalesOrderBean bean : mSkus)
								{
									if(mEpcs.get(i).getSku().equals(bean.getSku()))
									{
										bean.setNum(bean.getNum()-1);
										break;
									}
								}
								mEpcs.remove(i);
								i = -1;
							}
						}
						Toast.makeText(OrderActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
						lv1.setAdapter(mEpcAdapter);
						mSkuAdapter.notifyDataSetChanged();
					}
				}).create();
		dialog.show();

	}

	private void showInputDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(false);
		builder.setOnKeyListener(mKeylistener);
		View v = View.inflate(UIUtils.getContext(), R.layout.dialog_input_to,
				null);
		TextView tv_confirm = (TextView) v.findViewById(R.id.tv_confirm);
		TextView tv_cancal = (TextView) v.findViewById(R.id.tv_cancal);
		final EditText et_goods_source = (EditText) v
				.findViewById(R.id.et_goods_source);
		tv_cancal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mInputDialog != null) {
					mInputDialog.dismiss();
				}
			}
		});
		tv_confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(et_goods_source.getText().toString())) {
					if (mInputDialog != null) {
						mInputDialog.dismiss();
					}
					connInternet(et_goods_source.getText().toString());
				} else {
					ToastUtils.showToast("请输入商品去向");
				}
			}
		});
		builder.setView(v);
		mInputDialog = builder.create();
		mInputDialog.show();
	}

	private void connInternet(String s) {
		String json = createSubmitBean(s);
		Param param = new Param("doSalesContent",json);
		OkHttpClientManager.postAsyn("http://"+mIp+":"+mPort+"/"+ com.electronicBusiness.manager.ConfigurationManager.project+"/supply/do_sales",new ResultCallback<String>(){

			@Override
			public void onError(Request request, Exception e) {
				ToastUtils.showToast("网络通讯失败");
			}

			@Override
			public void onResponse(String response) {
				if(response.equals("200"))
				{
					ToastUtils.showToast("提交成功");
					setResult(2);
					finish();
				}
				else
				{
					ToastUtils.showToast("提交失败");
				}
			}

		},param);
	}

	protected String createSubmitBean(String s) {
		DoSalesBean doSaleBean = new DoSalesBean();
		Gson gson = new Gson();
		List<String> epcs = new ArrayList<String>();
		for(SalesOrderBean bean:mEpcs)
		{
			epcs.add(bean.getEpc());
		}
		doSaleBean.setEpcList(epcs);
		doSaleBean.setTo(s);
		return gson.toJson(doSaleBean);
	}
	class MyEpcAdapter extends MyBaseAdapter<SalesOrderBean>
	{

		public MyEpcAdapter(List<SalesOrderBean> data) {
			super(data);
		}

		@Override
		public BaseHolder<?> setHolder() {
			return new SellEpcHolder();
		}
		
	}
	class MySkuAdapter extends MyBaseAdapter<SalesOrderBean>
	{

		public MySkuAdapter(List<SalesOrderBean> data) {
			super(data);
		}

		@Override
		public BaseHolder<?> setHolder() {
			return new SellSkuHolder();
		}
	}
	protected void toggle_Epc() {
		if(mLv1Height==0)
		mLv1Height = lv1.getMeasuredHeight();
		ValueAnimator animator = null;//属性动画
		if (isEpcOpen) {
			// 关闭
			isEpcOpen = false;
			// 属性动画
			animator = ValueAnimator.ofInt(mLv1Height, 0);// 从某个值变化到某个值
		} else {
			// 开启
			isEpcOpen = true;
			// 属性动画
			animator = ValueAnimator.ofInt(0, mLv1Height);
		}

		// 动画更新的监听
		animator.addUpdateListener(new AnimatorUpdateListener() {

			// 启动动画之后, 会不断回调此方法来获取最新的值
			@Override
			public void onAnimationUpdate(ValueAnimator animator) {
				// 获取最新的高度值
				Integer height = (Integer) animator.getAnimatedValue();

				System.out.println("最新高度:" + height);

				// 重新修改布局高度
				LayoutParams params = lv1.getLayoutParams();
				params.height = height;
				lv1.setLayoutParams(params);
			}
		});

		animator.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator arg0) {

			}

			@Override
			public void onAnimationRepeat(Animator arg0) {

			}

			@Override
			public void onAnimationEnd(Animator arg0) {
				// 动画结束的事件
				// 更新小箭头的方向
				if (isEpcOpen) {
					iv_arrow1.setImageResource(R.drawable.arrow_up);
				} else {
					iv_arrow1.setImageResource(R.drawable.arrow_down);
				}
			}

			@Override
			public void onAnimationCancel(Animator arg0) {

			}
		});

		animator.setDuration(200);// 动画时间
		animator.start();// 启动动画
	}
	protected void toggle_Sku() {
		if(mLv2Height==0)
		mLv2Height = lv2.getMeasuredHeight();
		ValueAnimator animator = null;//属性动画
		if (isSkuOpen) {
			// 关闭
			isSkuOpen = false;
			// 属性动画
			animator = ValueAnimator.ofInt(mLv2Height, 0);// 从某个值变化到某个值
		} else {
			// 开启
			isSkuOpen = true;
			// 属性动画
			animator = ValueAnimator.ofInt(0, mLv2Height);
		}

		// 动画更新的监听
		animator.addUpdateListener(new AnimatorUpdateListener() {

			// 启动动画之后, 会不断回调此方法来获取最新的值
			@Override
			public void onAnimationUpdate(ValueAnimator animator) {
				// 获取最新的高度值
				Integer height = (Integer) animator.getAnimatedValue();

				System.out.println("最新高度:" + height);

				// 重新修改布局高度
				LayoutParams params = lv2.getLayoutParams();
				params.height = height;
				lv2.setLayoutParams(params);
			}
		});

		animator.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator arg0) {

			}

			@Override
			public void onAnimationRepeat(Animator arg0) {

			}

			@Override
			public void onAnimationEnd(Animator arg0) {
				// 动画结束的事件
				// 更新小箭头的方向
				if (isSkuOpen) {
					iv_arrow2.setImageResource(R.drawable.arrow_up);
				} else {
					iv_arrow2.setImageResource(R.drawable.arrow_down);
				}
			}

			@Override
			public void onAnimationCancel(Animator arg0) {

			}
		});

		animator.setDuration(200);// 动画时间
		animator.start();// 启动动画
	}
}
