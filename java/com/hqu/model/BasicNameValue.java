package com.hqu.model;

import java.sql.Timestamp;

public class BasicNameValue {
	private String Name;
	
	private String Value;
	
	private Timestamp startTime;
	
	private Timestamp endTime;
	
	private int Count;
	
	public int getCount() {
		return Count;
	}

	public void setCount(int count) {
		Count = count;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getValue() {
		return Value;
	}

	public void setValue(String value) {
		Value = value;
	}
	private int countRS;//统计人数
	
	private int countDS;//统计单数
	
	private double countJG;//统计月票

	public int getCountRS() {
		return countRS;
	}

	public void setCountRS(int countRS) {
		this.countRS = countRS;
	}

	public int getCountDS() {
		return countDS;
	}

	public void setCountDS(int countDS) {
		this.countDS = countDS;
	}

	public double getCountJG() {
		return countJG;
	}

	public void setCountJG(double countJG) {
		this.countJG = countJG;
	}	
	
}
