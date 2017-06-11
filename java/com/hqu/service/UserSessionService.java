package com.hqu.service;

import com.hqu.domain.UserSession;

public interface UserSessionService {
	UserSession selectUserSession(String YHZH);
	
	void insertUserSession(UserSession userSession);
	
	void updateUserSession(UserSession userSession);
}
