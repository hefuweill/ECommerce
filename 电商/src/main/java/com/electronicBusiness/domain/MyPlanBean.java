package com.electronicBusiness.domain;

public class MyPlanBean {

	private Integer id;
	private String name;
	private String descript;
	private Integer addTime;
	private Integer finishTime;
	private Integer fromId;
	private Integer toId;
	private Integer status;
	private Integer isBlind;
	private Integer storeId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public Integer getAddTime() {
		return addTime;
	}

	public void setAddTime(Integer addTime) {
		this.addTime = addTime;
	}

	public Integer getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Integer finishTime) {
		this.finishTime = finishTime;
	}

	public Integer getFromId() {
		return fromId;
	}

	public void setFromId(Integer fromId) {
		this.fromId = fromId;
	}

	public Integer getToId() {
		return toId;
	}

	public void setToId(Integer toId) {
		this.toId = toId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsBlind() {//1-盲盘 2-全盘  3-按商品盘
		return isBlind;
	}

	public void setIsBlind(Integer isBlind) {
		this.isBlind = isBlind;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

}
