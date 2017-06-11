package com.hqu.serviceImpl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hqu.dao.UserSessionDao;
import com.hqu.domain.UserSession;
import com.hqu.service.UserSessionService;
@Service("UserSessionService")
public class UserSessionServiceImpl implements UserSessionService{
	@Resource
	private UserSessionDao UserSessionDao;
	@Override
	public UserSession selectUserSession(String YHZH) {
		// TODO Auto-generated method stub
		return UserSessionDao.selectUserSession(YHZH);
	}

	@Override
	public void insertUserSession(UserSession userSession) {
		// TODO Auto-generated method stub
		UserSessionDao.insertUserSession(userSession);
	}

	@Override
	public void updateUserSession(UserSession userSession) {
		// TODO Auto-generated method stub
		UserSessionDao.updateUserSession(userSession);
	}
	
}
