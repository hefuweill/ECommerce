package com.electronicBusiness.domain;

import java.io.Serializable;
import java.util.List;

public class DoSalesBean implements Serializable{//用于移位和出库的bean

	private List<String> epcList;
	private String to;
	public List<String> getEpcList() {
		return epcList;
	}
	public void setEpcList(List<String> epcList) {
		this.epcList = epcList;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	
	
	
}
