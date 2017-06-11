package com.hqu.dao;

import org.apache.ibatis.annotations.Param;

import com.hqu.domain.SitePassenger;

public interface SitePassengerDao {
	int insert(@Param("BCDM")String BCDM, @Param("ZDDM") String ZDDM);
	
	int add(SitePassenger sitePassenger);
	
	int reduce(SitePassenger sitePassenger);
}
