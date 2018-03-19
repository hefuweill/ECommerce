package com.electronicBusiness.base;

import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.electronicBusiness.utils.SPUtils;
import com.electronicBusiness.view.LoadingPager;
import com.electronicBusiness.view.LoadingPager.StateType;

public abstract class BaseFragment extends Fragment {
	
	protected LoadingPager mPager;
	protected String mIp;
	protected String mPort;
	public OnKeyListener mKeylistener;
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		mIp = SPUtils.getString("Ip", "");
		mPort = SPUtils.getString("Port", "");
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		init();
		if(mPager==null)
		{
			mPager = new LoadingPager(getActivity())
			{

				@Override
				public StateType loadData() {
					return BaseFragment.this.loadData();
				}

				@Override
				public View createSuccessView() {
					return BaseFragment.this.createSuccessView();
				}
				
			};
		}
		return mPager;
	}
	private void init() {
		mKeylistener = new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK
						|| keyCode == KeyEvent.KEYCODE_SEARCH) {
					return true;
				} else {
					return false;
				}
			}
		};
	}
	public abstract StateType loadData();
	public abstract View createSuccessView();
	
}
