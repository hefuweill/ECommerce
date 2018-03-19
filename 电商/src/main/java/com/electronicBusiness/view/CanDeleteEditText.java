package com.electronicBusiness.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.electronicBusiness.R;

public class CanDeleteEditText extends RelativeLayout{

	private TextView mTv_tip;
	private EditText mEt_data;
	private ImageView mIv_delete;

	public CanDeleteEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
	}

	private void init(AttributeSet attrs) {
		TypedArray array = getContext().obtainStyledAttributes(attrs,R.styleable.CanDeleteEditText);
		String tip = array.getString(R.styleable.CanDeleteEditText_tip);
		View.inflate(getContext(), R.layout.layout_et, this);
		mTv_tip = (TextView) findViewById(R.id.tv_tip);
		mEt_data = (EditText) findViewById(R.id.et_data);
		mIv_delete = (ImageView) findViewById(R.id.iv_delete);
		try{
			mTv_tip.setText(tip);
			mEt_data.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if(TextUtils.isEmpty(s))
					{
						mIv_delete.setVisibility(View.INVISIBLE);
					}
					else
					{
						mIv_delete.setVisibility(View.VISIBLE);
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

			mIv_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mEt_data.setText("");
				}
			});
		}catch (NullPointerException e)
		{

		}

		array.recycle();
	}
	public String getETText()
	{
		return mEt_data.getText().toString();
	}
	public CanDeleteEditText(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CanDeleteEditText(Context context) {
		super(context);
	}
	
}
