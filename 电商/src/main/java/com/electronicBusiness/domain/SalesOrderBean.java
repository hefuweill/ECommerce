package com.electronicBusiness.domain;

import java.io.Serializable;

public class SalesOrderBean implements Serializable{//出库订单bean

	private String goodName;
	private String tyName;
	private String sku;
	private String epc;
	private int num;
	private boolean isSelect;

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean select) {
		isSelect = select;
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
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getEpc() {
		return epc;
	}
	public void setEpc(String epc) {
		this.epc = epc;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
}
