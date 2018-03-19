package com.electronicBusiness.base;

import java.util.List;

import com.electronicBusiness.holder.BindEpcHolder;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class MyBaseAdapter<T> extends BaseAdapter {

	private List<T> data;
	public MyBaseAdapter(List<T> data) {
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	public abstract BaseHolder<?> setHolder();
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.e("aaa",position+"");
		BaseHolder holder = null;
		if(convertView==null)
		{
			holder = setHolder();
			//查找View
			//设置tag
		}
		else
		{
			holder = (BaseHolder) convertView.getTag();
		}
		if(holder instanceof BindEpcHolder)
		{
			holder.changeViewStyle(data.get(position),position);
		}
		else{
			holder.changeViewStyle(data.get(position));
		}
		return holder.mRootView;
	}

}
