package com.electronicBusiness.domain;

import java.io.Serializable;
import java.util.List;

public class MoveBean implements Serializable{//用于移位的bean

	private List<String> epcList;
	private int to;
	
	public List<String> getEpcList() {
		return epcList;
	}
	public void setEpcList(List<String> epcList) {
		this.epcList = epcList;
	}
	public int getTo() {
		return to;
	}
	public void setTo(int to) {
		this.to = to;
	}
	
}
