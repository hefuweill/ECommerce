package com.electronicBusiness.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by fuwei on 2017/3/9.
 */
public class MyView extends LinearLayout{
    List<String> data;
    LinearLayout ll;
    Context context;
    MyOnClickListener listener;
    private int beforePosition = -1;
    private List<TextView> tvList;

    public MyView(Context context) {
        this(context,null);
    }

    public MyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        setOrientation(LinearLayout.VERTICAL);
    }
    public void setData(List<String> data)
    {
        this.data = data;
        showView();
    }
    public void setSelected(int position)
    {
        if(beforePosition==position)
        {
            return ;
        }
        else if(beforePosition!=-1)
        {

            tvList.get(beforePosition).setTextColor(Color.BLACK);
            GradientDrawable beforeBackground = (GradientDrawable)tvList.get(beforePosition).getBackground();
            beforeBackground.setColor(Color.WHITE);
            tvList.get(beforePosition).setBackground(beforeBackground);
        }
        GradientDrawable background = (GradientDrawable) tvList.get(position).getBackground();
        background.setColor(Color.argb(255,148,148,148));
        tvList.get(position).setTextColor(Color.argb(255,252,252,252));
        tvList.get(position).setBackground(background);
        beforePosition = position;
    }
    public String getSelectString()
    {
        return data.get(beforePosition);
    }
    private void showView() {
        tvList = new ArrayList<>();
        int count = 0;
        for(final String str:data)
        {
            if(count%3==0)
            {
                ll = new LinearLayout(context);
                ll.setWeightSum(3);
                this.addView(ll);

            }
            GradientDrawable shape = new GradientDrawable();
            shape.setCornerRadius(40);
            shape.setSize(200, 100);
            shape.setStroke(1,Color.GRAY);
            TextView tv = new TextView(context);
            tvList.add(tv);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(14);
            tv.setTextColor(Color.BLACK);
            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                        if(listener!=null)
                        {
                            Log.e("???",str);
                            setSelected(data.indexOf(str));
                            listener.onClick(str);
                        }

                }
            });
            LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            tv.setText(str);
            params.leftMargin = 10;
            params.rightMargin = 10;
            params.topMargin = 30;
            tv.setLayoutParams(params);
            tv.setBackground(shape);
            ll.addView(tv);
            count++;
        }
        setSelected(0);
    }
    public void setMyOnClickListener(MyOnClickListener listener)
    {
        this.listener = listener;
    }
    public interface MyOnClickListener
    {
        void onClick(String name);
    }
}
