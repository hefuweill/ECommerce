package com.electronicBusiness.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.electronicBusiness.R;
import com.electronicBusiness.base.BaseActivity;
import com.electronicBusiness.manager.ConfigurationManager;
import com.electronicBusiness.view.NoScrollViewPager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class HomeActivity extends BaseActivity {

	@ViewInject(R.id.vp)
	private NoScrollViewPager vp;
	@ViewInject(R.id.rg_tab)
	private RadioGroup rg_tab;
	@Override
	public void initView() {
		setContentView(R.layout.activity_home);
		ViewUtils.inject(this);
		vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		vp.setKeepScreenOn(true);
		vp.setOffscreenPageLimit(0);
	}
	@Override
	protected void initEvent() {
		super.initEvent();
		rg_tab.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				ConfigurationManager.stopEquip();
				switch (checkedId) {
				case R.id.rb1:
					vp.setCurrentItem(0);
					break;
				case R.id.rb2:
					vp.setCurrentItem(1);
					break;
				case R.id.rb3:
					vp.setCurrentItem(2);
					break;
				case R.id.rb4:
					vp.setCurrentItem(3);
					break;
				case R.id.rb5:
					vp.setCurrentItem(4);
					break;
				}
			}
		});
	}
	class MyPagerAdapter extends FragmentPagerAdapter
	{

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return com.electronicBusiness.manager.FragmentManager.getFragment_father(arg0);
		}

		@Override
		public int getCount() {
			return 5;
		}
		
	}
}
