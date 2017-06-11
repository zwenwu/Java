package com.hqu.dao;


import com.hqu.domain.UserSession;

public interface UserSessionDao {
	UserSession selectUserSession(String YHZH);
	
	void insertUserSession(UserSession userSession);
	
	void updateUserSession(UserSession userSession);
}
