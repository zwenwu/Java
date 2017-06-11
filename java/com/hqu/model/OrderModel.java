package com.hqu.model;

import java.util.List;

import com.hqu.domain.OrderApp;

public class OrderModel {
    private	List<OrderApp> orders ;
    
	private int totalCount;

	public List<OrderApp> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderApp> orders) {
		this.orders = orders;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}
