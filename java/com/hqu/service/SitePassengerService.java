package com.hqu.service;

import com.hqu.domain.Schedule;
import com.hqu.domain.SitePassenger;

public interface SitePassengerService {
	public int insert(String BCDM, String ZDDM);
	
	public int add(SitePassenger sitePassenger);
	
	public int reduce(SitePassenger sitePassenger);
	
	public void insert(Schedule schedule);
}
