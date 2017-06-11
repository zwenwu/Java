package com.hqu.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hqu.dao.LineDao;
import com.hqu.dao.SiteDao;
import com.hqu.domain.Line;
import com.hqu.domain.Site;
import com.hqu.service.PriceService;
@Service("PriceService")
public class PriceServicelmpl implements PriceService{
	@Resource
	private SiteDao siteDao;
	
	@Resource
	private LineDao lineDao;
	@Override
	public List<Site> getSitesByLine(String XLDM) {
		List<Site> sitelist = new ArrayList<Site>();
		Site sitetemp = null;
		Line line = null;
		line = lineDao.findLineByPrimaryKey(XLDM);
		System.out.println("线路：开始 "+line.getSQDMC()+" 途径 "+line.getTJZDDM()+" 终点 "+line.getEZDMC());
		if(line==null)
			return null;
		//添加起点站点
		if(line.getQDDM()==null||line.getSQDMC()==null||line.getQDDM().equals("")||line.getSQDMC().equals(""))
			return null;
		sitetemp = siteDao.selectSiteByPK(line.getQDDM());
		sitelist.add(sitetemp);
		//添加途径站点
		if (line.getTJZDDM()!=null&&!line.getTJZDDM().equals("")) {
			String [] ZDDMlist = line.getTJZDDM().split("#");
			System.out.println("分割："+ZDDMlist.length);
			for(int i=0;i<ZDDMlist.length;i++)
			{
				System.out.println("途径站点代码"+i+"："+ZDDMlist[i]);
				sitetemp = siteDao.selectSiteByPK(ZDDMlist[i]);
				if(sitetemp==null)
					return null;
				sitelist.add(sitetemp);
			}			
		}

		//添加终点站点
		if(line.getZDDM()==null||line.getEZDMC()==null||line.getZDDM().equals("")||line.getEZDMC().equals(""))
			return null;
		sitetemp = siteDao.selectSiteByPK(line.getZDDM());
		sitelist.add(sitetemp);
		for (int i = 0; i < sitelist.size(); i++) {

			System.out.println("站点"+i+"："+sitelist.get(i).getZDMC());
		}
		return sitelist;

	}

}
