package com.hqu.model;

import java.util.List;

import com.hqu.domain.OrderCardApp;

public class OrderCardModel {
	private List<OrderCardApp> orderCards;
	
	private int totalCount;

	public List<OrderCardApp> getOrderCards() {
		return orderCards;
	}

	public void setOrderCards(List<OrderCardApp> orderCards) {
		this.orderCards = orderCards;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}
