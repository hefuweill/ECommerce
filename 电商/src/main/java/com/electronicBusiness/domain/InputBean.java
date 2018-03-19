package com.electronicBusiness.domain;

import java.io.Serializable;
import java.util.List;

public class InputBean implements Serializable{

	private String sku;
	private List<String> epc;
	private int positionId;
	private String from;
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public List<String> getEpc() {
		return epc;
	}
	public void setEpc(List<String> epc) {
		this.epc = epc;
	}
	public int getPositionId() {
		return positionId;
	}
	public void setPositionId(int positionId) {
		this.positionId = positionId;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	
	
	
}
