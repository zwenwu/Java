package com.hqu.model;

import java.util.List;

import com.hqu.domain.FeedBack;

public class FeedBackModel {
    private	List<FeedBack> feedBacks ;    
	
	private int totalCount;

	public List<FeedBack> getFeedBacks() {
		return feedBacks;
	}

	public void setFeedBacks(List<FeedBack> feedBacks) {
		this.feedBacks = feedBacks;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
}
