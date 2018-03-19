package com.electronicBusiness.domain;

import java.io.Serializable;
import java.util.List;

public class SelectGoodsMegKeyWord implements Serializable{//用于查询出库记录的条件以及关键字来源

	private String keyWord;
	private String condition;
	private int endTime;
	private int startTime;
	
	
	
	public int getEndTime() {
		return endTime;
	}
	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}
	public int getStartTime() {
		return startTime;
	}
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
}
