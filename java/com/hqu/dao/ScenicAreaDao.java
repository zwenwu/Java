package com.hqu.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;

import com.hqu.domain.ScenicArea;


public interface ScenicAreaDao {

    List<ScenicArea> selectAll();
    ScenicArea selectScenicAreaByPK(String JQDM);
    ScenicArea selectScenicAreaByName(String JQMC);

    List<ScenicArea> selectByConditions(Map<String, Object> map);

    int updateScenicArea(ScenicArea scenicArea);
    int deleteScenicArea(ScenicArea scenicArea);
    int insertScenicArea(ScenicArea scenicArea);
}

