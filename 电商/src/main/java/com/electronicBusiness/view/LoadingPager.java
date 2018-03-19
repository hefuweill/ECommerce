package com.electronicBusiness.view;

import com.electronicBusiness.R;
import com.electronicBusiness.utils.UIUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;


public abstract class LoadingPager extends FrameLayout {

    public static final int STATE_LOAD_UNDO = 1;
    public static final int STATE_LOAD_LOADING = 2;
    public static final int STATE_LOAD_ERROR = 3;
    public static final int STATE_LOAD_EMPTY = 4;
    public static final int STATE_LOAD_SUCCESS = 5;
    private int STATE_CURRENT = 1;
    private View mLoadView;
    private View mEmptyView;
    private View mErrorView;
    private View mSuccessView;

    public LoadingPager(Context context) {
        this(context, null);
    }

    public LoadingPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    public void setState(int state)
    {
        STATE_CURRENT = state;
        showRightPager();
    }

    private void init() {
        if (mLoadView == null) {
            mLoadView = UIUtils.inflate(R.layout.pager_loading);
            this.addView(mLoadView);
        }
        if (mEmptyView == null) {
            mEmptyView = UIUtils.inflate(R.layout.pager_empty);
            this.addView(mEmptyView);
        }
        if (mErrorView == null) {
            mErrorView = UIUtils.inflate(R.layout.pager_error);
            Button btn_retry = (Button) mErrorView.findViewById(R.id.error_btn_retry);
            btn_retry.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    initData();
                }
            });
            this.addView(mErrorView);
        }
        showRightPager();
    }

    private void showRightPager() {
        mLoadView.setVisibility(STATE_CURRENT == STATE_LOAD_UNDO
                || STATE_CURRENT == STATE_LOAD_LOADING ? View.VISIBLE
                : View.INVISIBLE);
        mEmptyView
                .setVisibility(STATE_CURRENT == STATE_LOAD_EMPTY ? View.VISIBLE
                        : View.INVISIBLE);
        mErrorView
                .setVisibility(STATE_CURRENT == STATE_LOAD_ERROR ? View.VISIBLE
                        : View.INVISIBLE);
        if (STATE_CURRENT == STATE_LOAD_SUCCESS && mSuccessView == null) {
            mSuccessView = createSuccessView();
            if (mSuccessView != null)
                this.addView(mSuccessView);
        }
        if (mSuccessView != null) {
            mSuccessView
                    .setVisibility(STATE_CURRENT == STATE_LOAD_SUCCESS ? View.VISIBLE
                            : View.INVISIBLE);
        }
    }

    public void initData() {
        Log.e("error", "当前" + STATE_CURRENT);//加上成功不刷新完美解决bug
        if (STATE_CURRENT != STATE_LOAD_LOADING && STATE_CURRENT != STATE_LOAD_SUCCESS) {
            STATE_CURRENT = STATE_LOAD_LOADING;
            StateType type = loadData();//如果不使用asncyhttpclient必须加子线程
            if (type != null) {
                STATE_CURRENT = type.state;
            }
            UIUtils.runOnSafeThread(new Runnable() {

                @Override
                public void run() {
                    showRightPager();
                }
            });
        }

    }
    public void removeSuccessView()
	{
		removeView(mSuccessView);
		mSuccessView = null;
	}
    public enum StateType {
        STATE_ERROR(STATE_LOAD_ERROR), STATE_EMPTY(STATE_LOAD_EMPTY), STATE_SUCCESS(
                STATE_LOAD_SUCCESS),
        STATE_LOAD(STATE_LOAD_LOADING);
        int state;

        private StateType(int state) {
            this.state = state;
        }

        public int getState() {
            return state;
        }
    }

    public abstract StateType loadData();

    public abstract View createSuccessView();
}
