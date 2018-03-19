package com.electronicBusiness.manager;


import android.hardware.uhf.magic.reader;

public class ConfigurationManager {
	public static String project = "istore";
	public static int func = 0;
	public static final int MANGPAN = 1;
	public static final int QUANPAN = 2;
	public static final int ANSHANGPINGPAN = 3;
	public static int stopEquip()
	{
		reader.StopLoop();
		return 0;
	}
	public static int startEquip()
	{
		return 0;
	}
}
