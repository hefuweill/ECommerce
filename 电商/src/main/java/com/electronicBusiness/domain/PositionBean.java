package com.electronicBusiness.domain;

import java.io.Serializable;
import java.util.List;

public class PositionBean implements Serializable{

	private int id;
	private String area;
	private String position;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	

}
