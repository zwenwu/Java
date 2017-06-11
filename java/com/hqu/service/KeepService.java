package com.hqu.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hqu.domain.Keep;
import com.hqu.domain.Line;

public interface KeepService {
	int insertRoute(Keep keep);
	List<Line> showRoute(String yhzh, int startSize, int pageSize);
	int deleteRoute(String yhzh, String xldm);
    int sumPage(String yhzh);
	int selectKey(String yhzh, String xldm);
}
