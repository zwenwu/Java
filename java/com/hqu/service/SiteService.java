package com.hqu.service;

import java.util.List;
import java.util.Map;

import com.hqu.domain.Site;
import com.hqu.domain.SiteStatus;

public interface SiteService {
	
	public List<Site> selectAll();
	public List<Site> selectSitesAlongLine(String XLDM);
	public Site selectSiteByPK(String ZDDM);
	public Site selectSiteByName(String ZDMC);
	public List<SiteStatus> selectSiteStatus();
	public List<Site> selectByConditions(Map<String, Object> map);
	public Boolean updateSiteStatus(Site site);
	public Boolean updateSite(Site site);
	public Boolean deleteSite(Site site);
	public Boolean  insertSite(Site site);
}
