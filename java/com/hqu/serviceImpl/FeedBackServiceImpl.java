package com.hqu.serviceImpl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hqu.dao.FeedBackDao;
import com.hqu.domain.FeedBack;
import com.hqu.domain.FeedBackType;
import com.hqu.service.FeedBackService;
@Service("FeedBackService")
public class FeedBackServiceImpl implements FeedBackService {
	
	@Resource
	private FeedBackDao feedbackDao;
	
	@Override
	public List<FeedBack> selectByConditions(Map<String, Object> map) {
		return feedbackDao.selectByConditions(map);
	}

	@Override
	public List<FeedBackType> selectFeedBackType() {
		return feedbackDao.selectFeedBackType();
	}

	@Override
	public FeedBack selectFeedBackByPK(String FKYJLSH) {
		return feedbackDao.selectFeedBackByPK(FKYJLSH);
	}

	@Override
	public Boolean replyFeedBack(FeedBack feedback) {
		if(feedback!=null)
		{
			try {
				if(feedbackDao.replyFeedBack(feedback)>0)
					return true;//update执行成功
				else
					return false;//update执行失败
				}	
			catch (Exception e) {
				return false;
				}
		}else
			return false;//无输入对象！
	}
	@Override
	public int insertFeedBack(FeedBack feedBack) {
		// TODO 自动生成的方法存根
		if(feedBack!=null)
		{
			feedBack.setFSSJ(new Timestamp(System.currentTimeMillis()));
			try {
				if(feedbackDao.insertFeedBack(feedBack)>0)
				{
					return 1;//插入成功
				}
				else
				{
					return 0;//插入不成功
				}
				}	
			catch (Exception e) {
				return 0;//错误
				}
		}else{
			return 0;//无输入对象！
		}
	}
	@Override
	public List<FeedBack> selectFeedBackByFSR(String FSR,int startSize, int pageSize) {
		// TODO 自动生成的方法存根
		return feedbackDao.selectFeedBackByFSR(FSR,startSize,pageSize);
	}
	@Override
	public int selectFeedBackCount(String FSR) {
		// TODO 自动生成的方法存根
		return feedbackDao.selectFeedBackCount(FSR);
	}

}
