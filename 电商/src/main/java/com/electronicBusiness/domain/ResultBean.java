package com.electronicBusiness.domain;

import java.util.List;

public class ResultBean {
	private int isBlind;
	private int plan_id;
	private List<String> epc;
	private List<String> sku;
	public int getIsBlind() {
		return isBlind;
	}
	public void setIsBlind(int isBlind) {
		this.isBlind = isBlind;
	}
	public int getPlan_id() {
		return plan_id;
	}
	public void setPlan_id(int plan_id) {
		this.plan_id = plan_id;
	}
	public List<String> getEpc() {
		return epc;
	}
	public void setEpc(List<String> epc) {
		this.epc = epc;
	}
	public List<String> getSku() {
		return sku;
	}
	public void setSku(List<String> sku) {
		this.sku = sku;
	}
	
}
