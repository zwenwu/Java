package com.hqu.serviceImpl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hqu.dao.SitePassengerDao;
import com.hqu.domain.Line;
import com.hqu.domain.Schedule;
import com.hqu.domain.SitePassenger;
import com.hqu.service.SitePassengerService;

@Service("SitePassengerService")
public class SitePassengerServiceImpl implements SitePassengerService{
	
	@Resource
	private SitePassengerDao sitePassengerDao;
	
	@Resource
	private LineServiceImpl lineServiceImpl;
	
	public int insert(String BCDM, String ZDDM){
		return this.sitePassengerDao.insert(BCDM, ZDDM);
	}
	
	public int add(SitePassenger sitePassenger){
		return this.sitePassengerDao.add(sitePassenger);
	}
	
	public int reduce(SitePassenger sitePassenger){
		return this.sitePassengerDao.reduce(sitePassenger);
	}
	
	public void insert(Schedule schedule){
		Line line = lineServiceImpl.findLineByPrimaryKey(schedule.getXLDM());
		String XL = line.getQDDM() + "#" + line.getTJZDDM() + "#" + line.getZDDM();
		String[] XLs = XL.split("#");
		for (int i = 0; i < XLs.length; i++) {
			if (!XLs[i].equals("")) {
				this.sitePassengerDao.insert(schedule.getBCDM(), XLs[i]);
			}
		}
	}
}
