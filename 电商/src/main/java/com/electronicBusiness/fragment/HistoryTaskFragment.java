package com.electronicBusiness.fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.electronicBusiness.MyListener;
import com.electronicBusiness.R;
import com.electronicBusiness.activity.HistoryDetailActivity;
import com.electronicBusiness.activity.SelectHistoryActivity;
import com.electronicBusiness.base.BaseFragment;
import com.electronicBusiness.base.BaseHolder;
import com.electronicBusiness.base.MyBaseAdapter;
import com.electronicBusiness.domain.MyHistoryUpBean;
import com.electronicBusiness.domain.ScanResultBean;
import com.electronicBusiness.holder.HistoryHolder;
import com.electronicBusiness.manager.OkHttpClientManager;
import com.electronicBusiness.manager.OkHttpClientManager.Param;
import com.electronicBusiness.manager.OkHttpClientManager.ResultCallback;
import com.electronicBusiness.utils.ToastUtils;
import com.electronicBusiness.utils.UIUtils;
import com.electronicBusiness.view.LoadingPager;
import com.electronicBusiness.view.LoadingPager.StateType;
import com.electronicBusiness.view.MyListView;
import com.electronicBusiness.view.MyListView.onRefreshListener;
import com.electronicBusiness.view.PullToRefreshLayout;
import com.electronicBusiness.view.PullableListView;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Request;

public class HistoryTaskFragment extends BaseFragment {

    @ViewInject(R.id.tv_start_time)
    private TextView mTv_start_time;
    @ViewInject(R.id.tv_end_time)
    private TextView mTv_end_time;
    private long start_time;
    private long end_time;
    private boolean hasNext = true;
    @ViewInject(R.id.select_historictask_button)
    private Button bt_select;
    private List<ScanResultBean> data = new ArrayList<ScanResultBean>();
    @ViewInject(R.id.content_view)
    private PullableListView lv;
    @ViewInject(R.id.refresh_view)
    private PullToRefreshLayout refreshLayout;
    private int currPage = 0;
    private HistoryAdapter mAdapter;
    private boolean isFresh = false;

    @Override
    public StateType loadData() {
        String json = CreateJson();
        Param param = new Param("historyPlan", json);
        OkHttpClientManager.postAsyn("http://" + mIp + ":" + mPort + "/" + com.electronicBusiness.manager.ConfigurationManager.project + "/check/get_history_plan", new ResultCallback<List<ScanResultBean>>() {

            @Override
            public void onError(Request request, Exception e) {
                ToastUtils.showToast("网络通讯失败");
                mPager.setState(LoadingPager.STATE_LOAD_ERROR);
            }

            @Override
            public void onResponse(List<ScanResultBean> list) {
                if (list == null) {
                    mPager.setState(LoadingPager.STATE_LOAD_ERROR);
                    return;
                }
                currPage++;
                if (isFresh) {
                    data.clear();
                }
                data.addAll(list);
                mPager.setState(LoadingPager.STATE_LOAD_SUCCESS);
                if (isFresh) {
                    mAdapter.notifyDataSetChanged();
                    new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            // 千万别忘了告诉控件刷新完毕了哦！
                            refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                        }
                    }.sendEmptyMessage(0);
                }
            }

        }, param);
        return StateType.STATE_LOAD;
    }

    private String CreateJson() {
        MyHistoryUpBean bean = new MyHistoryUpBean();
        bean.setBigTime(Integer.MAX_VALUE);
        bean.setSmallTime(0);
        bean.setPageNum(currPage);
        return new Gson().toJson(bean);
    }

    @Override
    public View createSuccessView() {
        View v = View.inflate(getActivity(), R.layout.fragment_history_task, null);
        ViewUtils.inject(this, v);
        mAdapter = new HistoryAdapter(data);
        lv.setAdapter(mAdapter);
        initEvent();
        return v;
    }

    private void initEvent() {
        refreshLayout.setOnRefreshListener(new MyListener() {
            @Override
            public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
                isFresh = true;
                currPage = 0;
                loadData();
                // 下拉刷新操作

            }

            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
                if (hasNext) {
                    getDataFormNet();
                }
                // 加载操作
                else
                {
                    new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            // 千万别忘了告诉控件加载完毕了哦！
                            refreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                        }
                    }.sendEmptyMessage(0);
                }
            }
        });
//		lv.setOnRefershListener(new onRefreshListener() {
//
//			@Override
//			public void onRefresh() {
//				isFresh = true;
//				currPage = 0;
//				data.clear();
//				loadData();
//
//			}
//
//			@Override
//			public void onFootFresh() {
//				if(hasNext)
//				{
//					getDataFormNet();
//				}
//				lv.afterLoad();
//			}
//		});
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(UIUtils.getContext(), HistoryDetailActivity.class);
                intent.putExtra("planId", data.get(position).getId());
                startActivity(intent);
            }
        });
        bt_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (end_time > start_time) {
                    Intent intent = new Intent(getActivity(),
                            SelectHistoryActivity.class);

                    intent.putExtra("startTime", start_time);
                    intent.putExtra("endTime", end_time);
                    startActivity(intent);
                } else {
                    mTv_start_time.setText("");
                    mTv_end_time.setText("");
                    ToastUtils.showToast("查询时间有误，请重新输入");
                }
            }
        });
        mTv_start_time.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new Builder(getActivity());
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
                                mTv_start_time.setText(time);
                                Date date = new Date(dp_day.getYear() - 1900,
                                        dp_day.getMonth(), dp_day
                                        .getDayOfMonth());
                                start_time = date.getTime();
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
        mTv_end_time.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new Builder(getActivity());
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
                                        + (dp_day.getDayOfMonth() > 10 ? (dp_day
                                        .getDayOfMonth())
                                        : ("0" + dp_day.getDayOfMonth()));
                                mTv_end_time.setText(time);
                                Date date = new Date(dp_day.getYear() - 1900,
                                        dp_day.getMonth(), dp_day
                                        .getDayOfMonth());
                                end_time = date.getTime();
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
    }

    protected void getDataFormNet() {
        String json = CreateJson();
        Param param = new Param("historyPlan", json);
        OkHttpClientManager.postAsyn("http://" + mIp + ":" + mPort + "/" + com.electronicBusiness.manager.ConfigurationManager.project + "/check/get_history_plan", new ResultCallback<List<ScanResultBean>>() {

            @Override
            public void onError(Request request, Exception e) {
                ToastUtils.showToast("网络通讯失败");
            }

            @Override
            public void onResponse(List<ScanResultBean> list) {
                if (list == null) {
                    ToastUtils.showToast("未知错误");
                    return;
                }
                if (list.size() < 10) {
                    hasNext = false;
                }
                currPage++;
                data.addAll(list);
                mAdapter.notifyDataSetChanged();
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // 千万别忘了告诉控件加载完毕了哦！
                        refreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                }.sendEmptyMessage(0);
            }

        }, param);
    }

    @Override
    public void onStart() {
        mPager.initData();
        super.onStart();
    }

    class HistoryAdapter extends MyBaseAdapter<ScanResultBean> {

        public HistoryAdapter(List<ScanResultBean> data) {
            super(data);
        }

        @Override
        public BaseHolder<?> setHolder() {
            return new HistoryHolder();
        }

    }
}
