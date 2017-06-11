package com.hqu.service;

import java.util.List;
import java.util.Map;

import com.hqu.domain.FeedBack;
import com.hqu.domain.FeedBackType;

public interface FeedBackService {
	public List<FeedBack> selectByConditions(Map<String, Object> map);
	public FeedBack selectFeedBackByPK(String FKYJLSH);
	public List<FeedBackType> selectFeedBackType();
	public Boolean replyFeedBack(FeedBack feedback);
	public int insertFeedBack(FeedBack feedBack);//插入一条反馈
	public List<FeedBack> selectFeedBackByFSR(String FSR,int startSize, int pageSize);
	public int selectFeedBackCount(String FSR);
}
