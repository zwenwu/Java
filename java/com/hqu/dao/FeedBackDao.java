package com.hqu.dao;

import java.util.List;
import java.util.Map;

import com.hqu.domain.FeedBack;
import com.hqu.domain.FeedBackType;

public interface FeedBackDao {
	List<FeedBack> selectByConditions(Map<String, Object> map);
	FeedBack selectFeedBackByPK(String FKYJLSH);
	List<FeedBackType> selectFeedBackType();
	int replyFeedBack(FeedBack feedback);
	int insertFeedBack(FeedBack feedBack);
	List<FeedBack> selectFeedBackByFSR(String FSR,int startSize, int pageSize);
	int selectFeedBackCount(String FSR);
}
