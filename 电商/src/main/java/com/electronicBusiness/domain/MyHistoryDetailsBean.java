package com.electronicBusiness.domain;

import java.io.Serializable;

public class MyHistoryDetailsBean implements Serializable{
	
	private Integer planId;
	private String sku;
	private String goodName;
	private String typeName;
	private Integer findNum;//找到的数量
	private Integer sumNum;//计划找到的数量
	public Integer getPlanId() {
		return planId;
	}
	public void setPlanId(Integer planId) {
		this.planId = planId;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	
	public String getGoodName() {
		return goodName;
	}
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public Integer getFindNum() {
		return findNum;
	}
	public void setFindNum(Integer findNum) {
		this.findNum = findNum;
	}
	public Integer getSumNum() {
		return sumNum;
	}
	public void setSumNum(Integer sumNum) {
		this.sumNum = sumNum;
	}
}
