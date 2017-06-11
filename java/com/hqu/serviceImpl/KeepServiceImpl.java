package com.hqu.serviceImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hqu.dao.KeepDao;
import com.hqu.domain.Keep;
import com.hqu.domain.Line;
import com.hqu.domain.Order;
import com.hqu.service.KeepService;

@Service("KeepService")
public class KeepServiceImpl implements KeepService{
	@Resource
	private KeepDao keepDao;

	@Override
	public int insertRoute(Keep keep) {
		// TODO Auto-generated method stub
		Date now = new Date(); 
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		keep.setSCSJ(dateFormat.format(now));
		keep.setSCZT("0");
		return keepDao.insertRoute(keep);
	}

	@Override
	public List<Line> showRoute(String yhzh,int startSize, int pageSize) {
		// TODO Auto-generated method stub
		return keepDao.showRoute(yhzh,startSize,pageSize);
	}

	@Override
	public int deleteRoute(String yhzh, String xldm) {
		// TODO Auto-generated method stub
		return keepDao.deleteRoute(yhzh,xldm);
	}

	@Override
	public int sumPage(String yhzh) {
		// TODO Auto-generated method stub
		return keepDao.sumPage(yhzh);
	}

	@Override
	public int selectKey(String yhzh, String xldm) {
		// TODO Auto-generated method stub
		return keepDao.selectKey(yhzh,xldm);
	}

	

}
