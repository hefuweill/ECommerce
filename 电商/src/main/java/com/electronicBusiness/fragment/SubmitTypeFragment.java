package com.electronicBusiness.fragment;

import java.util.ArrayList;
import java.util.List;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.electronicBusiness.R;
import com.electronicBusiness.domain.MyTypeBean;
import com.electronicBusiness.manager.OkHttpClientManager;
import com.electronicBusiness.manager.OkHttpClientManager.Param;
import com.electronicBusiness.manager.OkHttpClientManager.ResultCallback;
import com.electronicBusiness.utils.SPUtils;
import com.electronicBusiness.utils.ToastUtils;
import com.squareup.okhttp.Request;

public abstract class SubmitTypeFragment extends Fragment{
	
	private String mIp;
	private String mPort;
	private ProgressBar mBar;
	private ListView mLv;
	private final String PARAM = "param";
	private final String PARAM_LEVEL = "param_level";
	private String typeFId = "0";
	private List<MyTypeBean> typeList = new ArrayList<MyTypeBean>(); 
	private int level = 1;
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("oncreate");
		mIp = SPUtils.getString("Ip", "");
		mPort = SPUtils.getString("Port", "");
		Bundle bundle = getArguments();
		if(bundle!=null)
		{
			typeFId = bundle.getString(PARAM);
			level = bundle.getInt(PARAM_LEVEL);
		}
	}
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		System.out.println("oncreateView");
		View v = View.inflate(getActivity(), R.layout.fragment_submit_type, null);
		mBar = (ProgressBar) v.findViewById(R.id.pb);
		mLv = (ListView) v.findViewById(R.id.lv);
		mLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SubmitTypeFragment.this.onItemClick(level,typeList.get(position));
			}
		});
		return v;
	}
	@Override
	public void onStart() {
		super.onStart();
		System.out.println("onStart");
		Param param = new Param("typeFId", typeFId);
		OkHttpClientManager.postAsyn("http://"+mIp+":"+mPort+"/"+ com.electronicBusiness.manager.ConfigurationManager.project+"/supply/get_type",new ResultCallback<List<MyTypeBean>>(){

			@Override
			public void onError(Request request, Exception e) {
				ToastUtils.showToast("网络异常,请检查网络!");
			}

			@Override
			public void onResponse(List<MyTypeBean> response) {
				typeList = response;
				TypeAdapter adapter = new TypeAdapter();
				mLv.setAdapter(adapter);
				mBar.setVisibility(View.INVISIBLE);
				mLv.setVisibility(View.VISIBLE);
			}
			
		},param);
	}
	class TypeAdapter extends BaseAdapter {
	
	    private int lastPosition;	
	
	    @Override
	    public int getCount() {
	        return typeList.size();
	    }
	
	    @Override
	    public Object getItem(int position) {
	        return typeList.get(position);
	    }
	
	    @Override
	    public long getItemId(int position) {
	        return 0;
	    }
	
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        ViewHolder viewHolder = null;
	        if (convertView==null){
	            convertView =  View.inflate(getActivity(), R.layout.item_type, null);
	            viewHolder = new ViewHolder();
	            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv);
	            convertView.setTag(viewHolder);
	        }
	        viewHolder = (ViewHolder) convertView.getTag();
	        MyTypeBean bean = typeList.get(position);
	        viewHolder.textView.setText(bean.getName());
	        if (lastPosition<position&&lastPosition!=0){
	            ObjectAnimator.ofFloat(convertView,"translationY",convertView.getHeight()*2,0).setDuration(500).start();
	
	        }
	        lastPosition = position;
	        return convertView;
	    }
	
	    class ViewHolder{
	        TextView textView;
	    }
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("onDestory");
	}
	
	public abstract void onItemClick(int level, MyTypeBean myTypeBean);
}
	
