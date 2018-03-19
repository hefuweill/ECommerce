package com.electronicBusiness.domain;

import java.util.List;

public class MySaleSubmit {
	private List<MySkuNumBean> skuNum;
	
	private List<String> epcList;
	
	private double sumPrice;

	public List<String> getEpcList() {
		return epcList;
	}

	public void setEpcList(List<String> epcList) {
		this.epcList = epcList;
	}

	public List<MySkuNumBean> getSkuNum() {
		return skuNum;
	}

	public double getSumPrice() {
		return sumPrice;
	}

	public void setSumPrice(double sumPrice) {
		this.sumPrice = sumPrice;
	}

	public void setSkuNum(List<MySkuNumBean> skuNum) {
		this.skuNum = skuNum;
	}
	
	
}
