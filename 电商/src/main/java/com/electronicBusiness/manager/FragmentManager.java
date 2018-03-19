package com.electronicBusiness.manager;

import android.util.SparseArray;

import com.electronicBusiness.base.BaseFragment;
import com.electronicBusiness.fragment.BindFragment;
import com.electronicBusiness.fragment.CheckFragment;
import com.electronicBusiness.fragment.CheckPlanFragment;
import com.electronicBusiness.fragment.CurrentTaskFragment;
import com.electronicBusiness.fragment.HistoryTaskFragment;
import com.electronicBusiness.fragment.MoveFragment;
import com.electronicBusiness.fragment.SaleFragment;
import com.electronicBusiness.fragment.SearchFragment;

public class FragmentManager {
	private static SparseArray<BaseFragment> fragementMap_child = new SparseArray<BaseFragment>();
	private static SparseArray<BaseFragment> fragementMap_father = new SparseArray<BaseFragment>();
	public static BaseFragment getFragment(int position)
	{
		BaseFragment fragment = fragementMap_child.get(position);
		if(fragment==null)
		{
			switch (position) {
			case 0:
				fragment = new CheckPlanFragment();
				break;
			case 1:
				fragment = new CurrentTaskFragment();
				break;
			case 2:
				fragment = new HistoryTaskFragment();
				break;
			}
			fragementMap_child.put(position, fragment);
		}
		return fragment;
	}
	public static BaseFragment getFragment_father(int position)
	{
		BaseFragment fragment = fragementMap_father.get(position);
		if(fragment==null)
		{
			switch (position) {
			case 0:
				fragment = new BindFragment();
				break;
			case 1:
				fragment = new CheckFragment();
				break;
			case 2:
				fragment = new SaleFragment();
				break;
			case 3:
				fragment = new SearchFragment();
				break;
			case 4:
				fragment = new MoveFragment();
				break;
			}
			fragementMap_father.put(position, fragment);
		}
		return fragment;
	}
	public static void clear() {
		fragementMap_child.clear();
		fragementMap_father.clear();
	}
}
