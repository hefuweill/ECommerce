package com.electronicBusiness.domain;

import java.io.Serializable;
import java.util.List;

public class ExecutePlanDetailsBean implements Serializable{//进行出库操作（接收）

	private String sku;
	private String name;
	private String type;
	private String position;
	private String epc;
	
	
	
	
	
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getEpc() {
		return epc;
	}
	public void setEpc(String epc) {
		this.epc = epc;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
	
}
