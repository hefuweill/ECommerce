package com.electronicBusiness.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.electronicBusiness.R;

public class MyListView extends ListView {

	private int startY = 0;
	private int height;
	private View v;
	private final int PULLFRESH = 0;
	private final int UPFRESH = 1;
	private final int FRESHING = 2;
	private int currentState = 0;
	private TextView tv_text;
	private ProgressBar pb;
	private ImageView iv_arrow;
	private TextView tv_time;
	private boolean isLoading = false;

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	private void initView() {
		v = View.inflate(getContext(), R.layout.header, null);
		foot = View.inflate(getContext(), R.layout.footer, null);
		pb = (ProgressBar) v.findViewById(R.id.pb);
		tv_text = (TextView) v.findViewById(R.id.tv_text);
		iv_arrow = (ImageView) v.findViewById(R.id.iv_arrow);
		tv_time = (TextView) v.findViewById(R.id.tv_time);
		tv_time.setText("刷新时间:" + getCurrentTime());
		foot.measure(0, 0);
		fHeight = foot.getMeasuredHeight();
		foot.setPadding(0, -fHeight, 0, 0);
		addFooterView(foot);
		v.measure(0, 0);// 通知系统测量
		height = v.getMeasuredHeight();
		v.setPadding(0, -height, 0, 0);// 可以为负
		addHeaderView(v);
		setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
						&& getLastVisiblePosition() == getCount() - 1
						&& !isLoading) {
					isLoading = true;
					foot.setPadding(0, 0, 0, 0);
					setSelection(getCount() - 1);
					if (listener != null) {
						listener.onFootFresh();
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}
		});
	}

	public MyListView(Context context) {
		super(context);
		initView();
	}

	// @Override
	// public boolean onInterceptTouchEvent(MotionEvent ev) {
	// // TODO Auto-generated method stub
	// int action = ev.getAction();
	// if(action==MotionEvent.ACTION_DOWN)//使点击正常
	// {
	// System.out.println("调用down");
	// return true;
	// }
	// return super.onInterceptTouchEvent(ev);
	// }
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			if (currentState == FRESHING) {
				break;
			}
			int deltaY = (int) (ev.getY() - startY);
			int padding = -height + deltaY;
			if (padding > -height && getFirstVisiblePosition() == 0) {
				v.setPadding(0, padding, 0, 0);
				if (padding >= 0 && currentState == PULLFRESH) {
					currentState = UPFRESH;
					changeContent();
				} else if (padding < 0 && currentState == UPFRESH) {
					currentState = PULLFRESH;
					changeContent();
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (currentState == PULLFRESH) {
				v.setPadding(0, -height, 0, 0);
			} else if (currentState == UPFRESH) {
				currentState = FRESHING;
				changeContent();
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	private void changeContent() {
		switch (currentState) {
		case PULLFRESH:
			tv_text.setText("下拉刷新");
			toDownAnim();
			break;
		case UPFRESH:
			tv_text.setText("松开刷新");
			toUpAnim();
			break;
		case FRESHING:
			tv_text.setText("正在刷新...");
			iv_arrow.clearAnimation();
			pb.setVisibility(View.VISIBLE);
			iv_arrow.setVisibility(View.INVISIBLE);
			v.setPadding(0, 0, 0, 0);
			if (listener != null) {
				listener.onRefresh();
			}
			/*
			 * new Handler().postDelayed(new Runnable() {
			 * 
			 * @Override public void run() { // TODO Auto-generated method stub
			 * afterFresh(); } }, 2000);
			 */
			break;
		}
	}

	private void toUpAnim() {
		RotateAnimation animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(300);
		animation.setFillAfter(true);
		iv_arrow.startAnimation(animation);
	}

	private void toDownAnim() {
		RotateAnimation animation = new RotateAnimation(-180, -360,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(300);
		animation.setFillAfter(true);
		iv_arrow.startAnimation(animation);
	}

	public void afterFresh() {
		currentState = PULLFRESH;
		v.setPadding(0, -height, 0, 0);
		iv_arrow.setVisibility(View.VISIBLE);
		pb.setVisibility(View.INVISIBLE);
		tv_text.setText("下拉刷新");
		tv_time.setText("刷新时间:" + getCurrentTime());
	}

	private String getCurrentTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date());
	}

	private onRefreshListener listener;
	private View foot;
	private int fHeight;

	public void setOnRefershListener(onRefreshListener listener) {
		this.listener = listener;
	}

	public interface onRefreshListener {
		void onRefresh();

		void onFootFresh();
	}

	public void afterLoad() {
		foot.setPadding(0, -fHeight, 0, 0);
		isLoading = false;
	}
}