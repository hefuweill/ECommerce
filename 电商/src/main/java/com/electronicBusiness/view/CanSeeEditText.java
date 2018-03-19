package com.electronicBusiness.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.electronicBusiness.R;

public class CanSeeEditText extends RelativeLayout{

	private TextView mTv_tip;
	private EditText mEt_data;
	private ImageView mIv_see;
	private boolean isSee = false;

	public CanSeeEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
	}

	private void init(AttributeSet attrs) {
		TypedArray array = getContext().obtainStyledAttributes(attrs,R.styleable.CanSeeEditText);
		String tip = array.getString(R.styleable.CanSeeEditText_tip_see);
		View.inflate(getContext(), R.layout.layout_et_see, this);
		mTv_tip = (TextView) findViewById(R.id.tv_tip);
		mEt_data = (EditText) findViewById(R.id.et_data);
		mIv_see = (ImageView) findViewById(R.id.iv_see);
		mTv_tip.setText(tip);
		mIv_see.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!isSee)
				mEt_data.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				else
					mEt_data.setTransformationMethod(PasswordTransformationMethod.getInstance());
				isSee = !isSee;
			}
		});
		array.recycle();
		mEt_data.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(TextUtils.isEmpty(s))
				{
					mIv_see.setVisibility(View.INVISIBLE);
				}
				else
				{
					mIv_see.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
	}
	public String getETText()
	{
		return mEt_data.getText().toString();
	}
	public CanSeeEditText(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CanSeeEditText(Context context) {
		super(context);
	}
	
}
