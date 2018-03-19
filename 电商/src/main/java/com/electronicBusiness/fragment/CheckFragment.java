package com.electronicBusiness.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;
import com.electronicBusiness.R;
import com.electronicBusiness.base.BaseFragment;
import com.electronicBusiness.view.LoadingPager;
import com.electronicBusiness.view.LoadingPager.StateType;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class CheckFragment extends BaseFragment {

	@ViewInject(R.id.tab)
	private PagerSlidingTabStrip tab;
	@ViewInject(R.id.vp)
	private ViewPager vp;
	private String mItems[] = { "盘点计划", "当前任务", "历史任务" };
	@Override
	public StateType loadData() {
		return null;
	}

	@Override
	public View createSuccessView() {
		View view = View.inflate(getActivity(), R.layout.activity_check, null);
		ViewUtils.inject(this,view);
		vp.setAdapter(new BindAdapter(getChildFragmentManager()));
		tab.setViewPager(vp);
		return view;
	}
	@Override
	public void onStart() {
		super.onStart();
		mPager.setState(LoadingPager.STATE_LOAD_SUCCESS);
	}
	class BindAdapter extends FragmentPagerAdapter {
		@Override
		public CharSequence getPageTitle(int position) {
			return mItems[position];
		}

		public BindAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			System.out.println(com.electronicBusiness.manager.FragmentManager
					.getFragment(arg0));
			return com.electronicBusiness.manager.FragmentManager
					.getFragment(arg0);
		}

		@Override
		public int getCount() {
			return mItems.length;
		}

	}
}
