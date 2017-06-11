package com.hqu.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;

import com.hqu.domain.Site;
import com.hqu.domain.SiteStatus;

public interface SiteDao {

    List<Site> selectAll();
    Site selectSiteByPK(String ZDDM);
    Site selectSiteByName(String ZDMC);
    List<SiteStatus> selectSiteStatus();
    List<Site> selectByConditions(Map<String, Object> map);
    int updateSiteStatus(Site site);
    int updateSite(Site site);
    int deleteSite(Site site);
    int insertSite(Site site);
}

