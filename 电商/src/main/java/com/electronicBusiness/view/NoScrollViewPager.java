package com.electronicBusiness.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoScrollViewPager extends NoCacheViewPager {

	public NoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return true;
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;//true拦截  只有当return true或默认时才会调用ontouchevent
	}
}
