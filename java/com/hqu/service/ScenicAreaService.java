package com.hqu.service;

import java.util.List;
import java.util.Map;

import com.hqu.domain.ScenicArea;
import com.hqu.domain.Site;
import com.hqu.domain.SiteStatus;

public interface ScenicAreaService {
	
	public List<ScenicArea> selectAll();
	/*public List<ScenicArea> selectSitesAlongLine(String XLDM);*/
	public ScenicArea selectScenicAreaByPK(String JQDM);
	public ScenicArea selectScenicAreaByName(String JQMC);
	
	public List<ScenicArea> selectByConditions(Map<String, Object> map);
	
	public Boolean updateScenicArea(ScenicArea scenicArea);
	public Boolean deleteScenicArea(ScenicArea scenicArea);
	public Boolean  insertScenicArea(ScenicArea scenicArea);
	
	public List<ScenicArea> selectScenicAreasAlongLine(String XLDM);
	
}
