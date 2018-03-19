package com.electronicBusiness.domain;

import java.io.Serializable;
import java.util.List;

public class HistoryDetailsBean implements Serializable{//进行出库操作（接收）

	private String sku;
	private int findNum;
	private int sumNum;
	private String goodName;
	private String tyName;
	private String position;
	private int planId;
	
	
	public int getPlanId() {
		return planId;
	}
	public void setPlanId(int planId) {
		this.planId = planId;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public int getFindNum() {
		return findNum;
	}
	public void setFindNum(int findNum) {
		this.findNum = findNum;
	}
	public int getSumNum() {
		return sumNum;
	}
	public void setSumNum(int sumNum) {
		this.sumNum = sumNum;
	}
	public String getGoodName() {
		return goodName;
	}
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	public String getTyName() {
		return tyName;
	}
	public void setTyName(String tyName) {
		this.tyName = tyName;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	
	
	
	
}
