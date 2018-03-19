package com.electronicBusiness.domain;

import java.io.Serializable;

public class MyPlanDetailsBean implements Serializable{
	
	private String sku;
	
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public int getStockNum() {
		return stockNum;
	}

	public void setStockNum(int stockNum) {
		this.stockNum = stockNum;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	private int stockNum;
	
	private String goodsName;
	
	

}