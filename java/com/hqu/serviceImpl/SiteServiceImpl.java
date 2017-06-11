package com.hqu.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.w3c.dom.css.ElementCSSInlineStyle;

import com.hqu.dao.LineDao;
import com.hqu.dao.SiteDao;
import com.hqu.domain.Line;
import com.hqu.domain.Site;
import com.hqu.domain.SiteStatus;
import com.hqu.service.SiteService;

@Service("SiteService")
public class SiteServiceImpl implements SiteService{
	@Resource
	private SiteDao siteDao;
	
	@Resource
	private LineDao lineDao;
	
	public List<Site> selectAll(){
		return siteDao.selectAll();
	}

	@Override
	public Site selectSiteByPK(String ZDDM) {
		return siteDao.selectSiteByPK(ZDDM);
	}
	
	public List<SiteStatus> selectSiteStatus() {
		return siteDao.selectSiteStatus();
	}
	
	@Override
	public List<Site> selectByConditions(Map<String, Object> map) {
		return siteDao.selectByConditions(map);
	}

	@Override
	public Boolean updateSiteStatus(Site site) {
		if(site!=null)
		{
			if(site.getZDZTDM().equals("0")||site.getZDZTDM().equals("1") )
			{
				try {
					if(siteDao.updateSiteStatus(site)>0)
						return true;//update执行成功
					else
						return false;//update执行失败
					}					
				 catch (Exception e) {
					return false;
				 }

			}else
				return false;//输入参数不合法！=
		}else
			return false;//无输入对象！
			
	}

	@Override
	public Boolean updateSite(Site site) {
		if(site!=null)
		{
			if(site.getZDZTDM().equals("0")||site.getZDZTDM().equals("1") )
			{
				try {
					if(siteDao.updateSite(site)>0)
						return true;//update执行成功
					else
						return false;//update执行失败
					}					
				 catch (Exception e) {
					return false;
				 }

			}else
				return false;//输入参数不合法！=
		}else
			return false;//无输入对象！
	}
	
	@Override
	public Boolean deleteSite(Site site) {
		if(site!=null)
		{
			if(site.getZDZTDM().equals("1") )
			{
				try {
					if(siteDao.deleteSite(site)>0)
						return true;//delete执行成功
					else
						return false;
				} catch (Exception e) {
					return false;//外键约束不允许删除
				}
			}else
				return false;//删除前须先停用站点！
		}else
			return false;//无输入对象！
	}

	@Override
	public Boolean insertSite(Site site) {
			try{
				if(siteDao.insertSite(site)>0)
					return true;
				else
					return false;
			}catch (Exception e) {
				return false;
		}
	}

	@Override
	public List<Site> selectSitesAlongLine(String XLDM) {
		List<Site> sitelist = new ArrayList<Site>();
		Site sitetemp = null;
		Line line = null;
		try {
			line = lineDao.findLineByPrimaryKey(XLDM);
		} catch (Exception e) {
			return null;
		}
		
		if(line==null)
			return null;
		else{
				String ZDDMstr;
				if(line.getTJZDDM()!=null&&!"".equals(line.getTJZDDM()))
					ZDDMstr = line.getQDDM()+"#"+line.getTJZDDM()+"#"+line.getZDDM();
				else
					ZDDMstr=line.getQDDM()+"#"+line.getZDDM();
				String [] ZDDMlist = ZDDMstr.split("#");
				for(int i=0;i<ZDDMlist.length;i++){
					try {
						sitetemp = siteDao.selectSiteByPK(ZDDMlist[i]);
						if(sitetemp==null)
							return null;
						else{
							//把返回的站点信息中的城市代码、名称，站点状态代码、名称，站点发布时间设为空，如有需要去掉即可
							sitetemp.setCSDM(null);
							sitetemp.setCSMC(null);
							sitetemp.setZDZTDM(null);
							sitetemp.setZDZTMC(null);
							sitetemp.setFBSJ(null);
							//把图片地址合并到ZDTP数组
							List<String> ZDTP = new ArrayList<String>();
							if(sitetemp.getZDTP1()!=null&&!sitetemp.getZDTP1().isEmpty())
								ZDTP.add(sitetemp.getZDTP1());
							if(sitetemp.getZDTP2()!=null&&!sitetemp.getZDTP2().isEmpty())
								ZDTP.add(sitetemp.getZDTP2());
							if(sitetemp.getZDTP3()!=null&&!sitetemp.getZDTP3().isEmpty())
								ZDTP.add(sitetemp.getZDTP3());
							//ZDTP1 2 3的请求方式封闭,统一放在ZDTP的数组中去
							sitetemp.setZDTP1(null);
							sitetemp.setZDTP2(null);
							sitetemp.setZDTP3(null);
							sitetemp.setZDTP(ZDTP);
							sitelist.add(sitetemp);
							}
						} catch (Exception e) {
								return null;
								}
					}
			}
			return sitelist;
	}

	@Override
	public Site selectSiteByName(String ZDMC) {
		if(ZDMC!=null&&!"".equals(ZDMC)){
			try {
				Site site = siteDao.selectSiteByName(ZDMC);
				if(site!=null)
					return site;
				else
					return null;
			} catch (Exception e) {
				return null;
			}
		}else
			return null;
	}




}
