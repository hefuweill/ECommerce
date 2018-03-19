package com.electronicBusiness.manager;

import com.electronicBusiness.base.BaseApplication;

public class ConfigurationManager {
	public static String project = "istore";
	public static int func = 0;
	public static final int MANGPAN = 1;
	public static final int QUANPAN = 2;
	public static final int ANSHANGPINGPAN = 3;
	public static int stopEquip()
	{
		return BaseApplication.getService().readEPC(false);
	}
	public static int startEquip()
	{
		return BaseApplication.getService().readEPC(true);
	}
}
