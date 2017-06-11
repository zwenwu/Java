package com.hqu.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hqu.domain.Keep;
import com.hqu.domain.Line;

public interface KeepDao {
	int insertRoute(Keep keep);
	List<Line> showRoute(@Param("YHZH")String yhzh,int startSize, int pageSize);
	int deleteRoute(@Param("YHZH")String yhzh,@Param("XLDM")String xldm);
	int sumPage(@Param("YHZH")String yhzh);
	int selectKey(@Param("YHZH")String yhzh, @Param("XLDM")String xldm);
	
}
