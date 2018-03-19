package com.electronicBusiness.fragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.electronicBusiness.R;
import com.electronicBusiness.base.BaseFragment;
import com.electronicBusiness.base.BaseHolder;
import com.electronicBusiness.base.MyBaseAdapter;
import com.electronicBusiness.domain.MoveBean;
import com.electronicBusiness.domain.PositionBean;
import com.electronicBusiness.holder.BindEpcHolder;
import com.electronicBusiness.manager.ConfigurationManager;
import com.electronicBusiness.manager.OkHttpClientManager;
import com.electronicBusiness.manager.OkHttpClientManager.Param;
import com.electronicBusiness.manager.OkHttpClientManager.ResultCallback;
import com.electronicBusiness.utils.ToastUtils;
import com.electronicBusiness.view.LoadingPager;
import com.electronicBusiness.view.LoadingPager.StateType;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.hardware.uhf.magic.reader;

public class MoveFragment extends BaseFragment {
    @ViewInject(R.id.pb)
    private ProgressBar pb;
    @ViewInject(R.id.sp)
    private Spinner spinner;
    @ViewInject(R.id.fab)
    private FloatingActionButton fab;
    @ViewInject(R.id.tv_start)
    private TextView tv_start;
    @ViewInject(R.id.tv_submit)
    private TextView tv_submit;
    @ViewInject(R.id.tv_clear)
    private TextView tv_clear;
    @ViewInject(R.id.lv)
    private ListView lv;
    @ViewInject(R.id.ll)
    private LinearLayout ll;
    private List<String> data = new ArrayList<String>();
    public final int CHANGE_START = 2;
    public final int CHANGE_STOP = 3;
    private boolean isReading = false;
    private List<PositionBean> list;
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case reader.msgreadepc:
                    if(isReading){
                        String epc = (String) msg.obj;
                        reFreshData(epc);
                    }
                    break;
                case CHANGE_START:
                    tv_start.setText("开始盘点");
                    isReading = false;
                    break;
                case CHANGE_STOP:
                    tv_start.setText("暂停盘点");
                    isReading = true;
                    break;
            }
        }

        ;
    };
    private MyEpcAdapter mAdapter;
    private AlertDialog mDialog;

    @Override
    public StateType loadData() {
        return null;
    }

    @Override
    public View createSuccessView() {
        View view = View.inflate(getContext(), R.layout.fragment_move, null);
        ViewUtils.inject(this, view);
        mAdapter = new MyEpcAdapter(data);
        lv.setAdapter(mAdapter);
        connectInternet();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        initEvent();
    }

    protected void initEvent() {
        tv_clear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                data.clear();
                mAdapter.notifyDataSetChanged();
            }
        });
        tv_start.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isReading){
                    reader.StopLoop();
                    handler.sendEmptyMessage(CHANGE_START);
                }else{
                    reader.ReadtidLablesLoop(12);
                    handler.sendEmptyMessage(CHANGE_STOP);
                }
            }
        });
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll.getVisibility() == View.INVISIBLE) {
                    ll.setVisibility(View.VISIBLE);
                } else {
                    ll.setVisibility(View.INVISIBLE);
                }
            }
        });
        tv_submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                stop();
                if (data.size() == 0) {
                    ToastUtils.showToast("请至少扫描一个epc");
                }
                if (data.size() > 0) {
                    connInternet();
                }
            }
        });
    }

    protected void reFreshData(List<String> epcs) {
        for (String epc : epcs) {
            if (!data.contains(epc)) {
                data.add(epc);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    protected void reFreshData(String epc) {
        if (!data.contains(epc)) {
            data.add(epc);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void stop() {
        if (ConfigurationManager.stopEquip() == 0)// 关闭成功
        {
            handler.sendEmptyMessage(CHANGE_START);
        }
    }

    /**
     * 获取位置信息
     */
    private void connectInternet() {
        OkHttpClientManager.postAsyn("http://" + mIp + ":" + mPort + "/" + ConfigurationManager.project + "/supply/get_position", new ResultCallback<List<PositionBean>>() {

            @Override
            public void onError(Request request, Exception e) {
                ToastUtils.showToast("网络通讯失败");
            }

            @Override
            public void onResponse(List<PositionBean> list) {
                MoveFragment.this.list = list;
                List<PositionBean> data = new ArrayList();
                Iterator<PositionBean> iterator = list.iterator();
                while (iterator.hasNext()) {
                    PositionBean next = iterator.next();
                    PositionBean bean = new PositionBean();
                    bean.setPosition(next.getArea() + "-" + next.getPosition());
                    bean.setId(next.getId());
                    data.add(bean);
                }
                List<String> stringList = new ArrayList<>();
                Iterator<PositionBean> iterator1 = data.iterator();
                while (iterator1.hasNext()) {
                    PositionBean next = iterator1.next();
                    stringList.add(next.getPosition());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, stringList);
                adapter.setDropDownViewResource(R.layout.drop_down_item);
                spinner.setAdapter(adapter);
                pb.setVisibility(View.INVISIBLE);
                ll.setVisibility(View.VISIBLE);
            }

        });
    }


    /**
     * 访问网络
     *
     *
     */
    private void connInternet() {
		showWaitNetDialog();
		final Param param1 = createSubmitParam();
		// 访问网络绑定回到扫描sku界面
		OkHttpClientManager.postAsyn("http://" + mIp + ":" + mPort
				+ "/"+ ConfigurationManager.project+"/supply/do_move", new ResultCallback<String>() {

			@Override
			public void onError(Request request, Exception e) {
				ToastUtils.showToast("访问网络失败" + e.getMessage());
				mDialog.dismiss();
			}

			@Override
			public void onResponse(String response) {
				mDialog.dismiss();
				System.out.println("response" + response);
				if (response.equals("200")) {
					ToastUtils.showToast("移位成功");
				} else {
					ToastUtils.showToast("移位失败");
				}
			}

		}, param1);
    }

    private Param createSubmitParam() {
        MoveBean bean = new MoveBean();
        bean.setEpcList(data);
        bean.setTo(list.get(spinner.getSelectedItemPosition()).getId());
        return new Param("MoveContent",new Gson().toJson(bean));
    }

    protected void showWaitNetDialog() {
		Builder builder = new Builder(getActivity());
		builder.setCancelable(false);
		builder.setOnKeyListener(mKeylistener);
		builder.setMessage("正在上传请稍后......");
        mDialog = builder.create();
		mDialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        mPager.setState(LoadingPager.STATE_LOAD_SUCCESS);
    }

    class MyEpcAdapter extends MyBaseAdapter<String> {

        public MyEpcAdapter(List<String> data) {
            super(data);
        }

        @Override
        public BaseHolder<?> setHolder() {
            return new BindEpcHolder();
        }
    }

}
