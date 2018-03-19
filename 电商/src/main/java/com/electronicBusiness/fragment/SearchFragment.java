package com.electronicBusiness.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.electronicBusiness.R;
import com.electronicBusiness.activity.OrderActivity;
import com.electronicBusiness.activity.SelectHistoryActivity;
import com.electronicBusiness.base.BaseFragment;
import com.electronicBusiness.base.BaseHolder;
import com.electronicBusiness.base.MyBaseAdapter;
import com.electronicBusiness.domain.FindMegBean;
import com.electronicBusiness.domain.SalesOrderBean;
import com.electronicBusiness.domain.SelectGoodsMegKeyWord;
import com.electronicBusiness.holder.QueryHoler;
import com.electronicBusiness.manager.OkHttpClientManager;
import com.electronicBusiness.utils.ToastUtils;
import com.electronicBusiness.utils.UIUtils;
import com.electronicBusiness.view.LoadingPager;
import com.electronicBusiness.view.LoadingPager.StateType;
import com.electronicBusiness.view.MyView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SearchFragment extends BaseFragment {

	private MyView view;
	private Button bt_search;
	private LinearLayout ll;
	private EditText et_content;
	private long start_time;
	private long end_time;
	private ProgressBar pb;
	private ListView lv;
	private List<FindMegBean> findList;
	private DrawerLayout dr;
	private TextView tv_tip;
	private boolean isOpen = false;
	private QueryAdapter adapter;

	@Override
	public StateType loadData() {
		return null;
	}
	@Override
	public View createSuccessView() {
		View v = View.inflate(getActivity(),R.layout.fragment_search,null);
		dr = (DrawerLayout) v.findViewById(R.id.dr);
		ImageView iv_menu = (ImageView) v.findViewById(R.id.iv_menu);
		iv_menu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isOpen)
				{
					dr.closeDrawer(GravityCompat.START);
				}
				else
				{
					dr.openDrawer(GravityCompat.START);
				}
				isOpen = !isOpen;
			}
		});
		pb = (ProgressBar) v.findViewById(R.id.pb);
		lv = (ListView) v.findViewById(R.id.lv);
		view = (MyView) v.findViewById(R.id.mView);
		bt_search = (Button) v.findViewById(R.id.bt_search);
		tv_tip = (TextView) v.findViewById(R.id.tv_tip);
		ll = (LinearLayout) v.findViewById(R.id.ll);
		et_content = (EditText) v.findViewById(R.id.et_content);
		final TextView tv_start = (TextView) v.findViewById(R.id.tv_start);
		final TextView tv_end = (TextView) v.findViewById(R.id.tv_end);
		setListViewAdapter();
		tv_start.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				View view = View.inflate(getActivity(),
						R.layout.dialog_change_day, null);
				final DatePicker dp_day = (DatePicker) view
						.findViewById(R.id.dp_day);
				builder.setView(view);
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								start_time = 0;
								String time = dp_day.getYear()
										+ "-"
										+ (dp_day.getMonth() > 9 ? (dp_day
										.getMonth() + 1)
										: ("0" + (dp_day.getMonth() + 1)))
										+ "-"
										+ (dp_day.getDayOfMonth() > 9 ? (dp_day
										.getDayOfMonth())
										: ("0" + dp_day.getDayOfMonth()));
								tv_start.setText(time);
								Date date = new Date(dp_day.getYear() - 1900,
										dp_day.getMonth(), dp_day
										.getDayOfMonth());
								start_time = date.getTime()/1000;
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
							}
						});
				builder.show();
			}
		});
		tv_end.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				View view = View.inflate(getActivity(),
						R.layout.dialog_change_day, null);
				final DatePicker dp_day = (DatePicker) view
						.findViewById(R.id.dp_day);
				builder.setView(view);
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								end_time = 0;
								String time = dp_day.getYear()
										+ "-"
										+ (dp_day.getMonth() > 9 ? (dp_day
										.getMonth() + 1)
										: ("0" + (dp_day.getMonth() + 1)))
										+ "-"
										+ (dp_day.getDayOfMonth() >= 10 ? (dp_day
										.getDayOfMonth())
										: ("0" + dp_day.getDayOfMonth()));
								tv_end.setText(time);
								Date date = new Date(dp_day.getYear() - 1900,
										dp_day.getMonth(), dp_day
										.getDayOfMonth());
								end_time = date.getTime()/1000;
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
							}
						});
				builder.show();
			}
		});
		bt_search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pb.setVisibility(View.VISIBLE);
				tv_tip.setVisibility(View.INVISIBLE);
				SelectGoodsMegKeyWord bean = new SelectGoodsMegKeyWord();
				bean.setCondition(view.getSelectString());
				if(view.getSelectString().equals("入库时间"))
				{
					if (end_time > start_time) {
						bean.setKeyWord(et_content.getText().toString());
						bean.setStartTime((int) start_time);
						bean.setEndTime((int) end_time);
					} else {
						tv_start.setText("");
						tv_end.setText("");
						ToastUtils.showToast("查询时间有误，请重新输入");
					}
				}
				else
				{
					bean.setKeyWord(et_content.getText().toString());
					bean.setStartTime(0);
					bean.setEndTime(0);
				}
				connInternet(bean);
				dr.closeDrawer(GravityCompat.START);
			}
		});
		String[] queryType = getActivity().getResources().getStringArray(R.array.queryType);
		List<String> queryList = Arrays.asList(queryType);
		view.setData(queryList);
		view.setMyOnClickListener(new MyView.MyOnClickListener() {
			@Override
			public void onClick(String name) {
				Toast.makeText(getActivity(), name, Toast.LENGTH_SHORT).show();
				if(name.equals("入库时间")){
					et_content.setVisibility(View.INVISIBLE);
					ll.setVisibility(View.VISIBLE);
				}
				else
				{
					et_content.setVisibility(View.VISIBLE);
					ll.setVisibility(View.INVISIBLE);
				}
			}
		});
		return v;
	}

	private void setListViewAdapter() {
		findList = new ArrayList<>();
		adapter = new QueryAdapter(findList);
		lv.setAdapter(adapter);
	}

	class QueryAdapter extends MyBaseAdapter<FindMegBean>
	{

		public QueryAdapter(List<FindMegBean> data) {
			super(data);
		}

		@Override
		public BaseHolder<?> setHolder() {
			return new QueryHoler();
		}
	}
	private void connInternet(SelectGoodsMegKeyWord bean) {
		OkHttpClientManager.Param param1 = new OkHttpClientManager.Param("KeyWord", new Gson().toJson(bean));
		OkHttpClientManager.postAsyn("http://" + mIp + ":" + mPort
						+ "/"+ com.electronicBusiness.manager.ConfigurationManager.project+"/supply/select_goods_meg",
				new OkHttpClientManager.ResultCallback<List<FindMegBean>>() {

					@Override
					public void onError(Request request, Exception e) {
						ToastUtils.showToast("网络通讯失败");
					}

					@Override
					public void onResponse(List<FindMegBean> response) {
						pb.setVisibility(View.INVISIBLE);
						lv.setVisibility(View.VISIBLE);
						findList.clear();
						findList.addAll(response);
						adapter.notifyDataSetChanged();
					}

				}, param1);
	}

	@Override
	public void onStart() {
		super.onStart();
		mPager.setState(LoadingPager.STATE_LOAD_SUCCESS);
	}
}
