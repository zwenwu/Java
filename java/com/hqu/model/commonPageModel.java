package com.hqu.model;

import java.util.List;

public class commonPageModel<T> {	
	
	private List<T> dataobjet;
	
	private int totalCount;

	/*public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}*/

	public List<T> getDataobjet() {
		return dataobjet;
	}

	public void setDataobjet(List<T> dataobjet) {
		this.dataobjet = dataobjet;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
}
