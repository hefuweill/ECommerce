package com.electronicBusiness.domain;

import java.io.Serializable;

public class FindMegBean implements Serializable{//用于查询出库记录返回的商品信息

	private String sku;
	private String area;
	private String position;
	private String tyName;
	private String goName;
	private int num;


	public String getGoName() {
		return goName;
	}
	public void setGoName(String goName) {
		this.goName = goName;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getTyName() {
		return tyName;
	}
	public void setTyName(String tyName) {
		this.tyName = tyName;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
}
