package com.hqu.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.w3c.dom.css.ElementCSSInlineStyle;

import com.hqu.dao.LineDao;
import com.hqu.dao.ScenicAreaDao;
import com.hqu.domain.Line;
import com.hqu.domain.ScenicArea;
import com.hqu.domain.SiteStatus;
import com.hqu.service.ScenicAreaService;

@Service("ScenicAreaService")
public class ScenicAreaServiceImpl implements ScenicAreaService{
	@Resource
	private ScenicAreaDao scenicAreaDao;
	
	@Resource
	private LineDao lineDao;
	
	public List<ScenicArea> selectAll(){
		return scenicAreaDao.selectAll();
	}

	@Override
	public ScenicArea selectScenicAreaByPK(String JQDM) {
		return scenicAreaDao.selectScenicAreaByPK(JQDM);
	}
	
	
	
	@Override
	public List<ScenicArea> selectByConditions(Map<String, Object> map) {
		return scenicAreaDao.selectByConditions(map);
	}



	@Override
	public Boolean updateScenicArea(ScenicArea scenicArea) {
		if(scenicArea!=null)
		{
			
				try {
					if(scenicAreaDao.updateScenicArea(scenicArea)>0)
						return true;//update执行成功
					else
						return false;//update执行失败
					}					
				 catch (Exception e) {
					return false;
				 }

			
		}else
			return false;//无输入对象！
	}
	
	@Override
	public Boolean deleteScenicArea(ScenicArea scenicArea) {
		if(scenicArea!=null)
		{
			
				try {
					if(scenicAreaDao.deleteScenicArea(scenicArea)>0)
						return true;//delete执行成功
					else
						return false;
				} catch (Exception e) {
					return false;//外键约束不允许删除
				}
			
		}else
			return false;//无输入对象！
	}

	@Override
	public Boolean insertScenicArea(ScenicArea scenicArea) {
			try{
				if(scenicAreaDao.insertScenicArea(scenicArea)>0)
					return true;
				else
					return false;
			}catch (Exception e) {
				return false;
		}
	}

	@Override
	public List<ScenicArea> selectScenicAreasAlongLine(String XLDM) {
		List<ScenicArea> scenicArealist = new ArrayList<ScenicArea>();
		ScenicArea scenicAreatemp = null;
		Line line = null;
		try {
			line = lineDao.findLineByPrimaryKey(XLDM);
		} catch (Exception e) {
			return null;
		}
		
		if(line==null)
			return null;
		else{
				String JQDMstr;
				if(line.getTJZDDM()!=null&&!"".equals(line.getTJZDDM()))
					JQDMstr = line.getQDDM()+"#"+line.getTJZDDM()+"#"+line.getZDDM();
				else
					JQDMstr=line.getQDDM()+"#"+line.getZDDM();
				String [] JQDMlist = JQDMstr.split("#");
				for(int i=0;i<JQDMlist.length;i++){
					try {
						scenicAreatemp = scenicAreaDao.selectScenicAreaByPK(JQDMlist[i]);
						if(scenicAreatemp==null)
							return null;
						else{
							//把返回的站点信息中的城市代码、名称，景区名称、票价，介绍，级别，发布时间设为空，如有需要去掉即可
							scenicAreatemp.setCSDM(null);
							scenicAreatemp.setCSMC(null);
							scenicAreatemp.setJQJB(null);
							scenicAreatemp.setJQPJ(null);
							scenicAreatemp.setJQJS(null);
							scenicAreatemp.setJQMC(null);
							scenicAreatemp.setFBSJ(null);
							//把图片地址合并到JQTP数组
							List<String> JQTP = new ArrayList<String>();
							if(scenicAreatemp.getJQTP1()!=null&&!scenicAreatemp.getJQTP1().isEmpty())
								JQTP.add(scenicAreatemp.getJQTP1());
							if(scenicAreatemp.getJQTP2()!=null&&!scenicAreatemp.getJQTP2().isEmpty())
								JQTP.add(scenicAreatemp.getJQTP2());
							if(scenicAreatemp.getJQTP3()!=null&&!scenicAreatemp.getJQTP3().isEmpty())
								JQTP.add(scenicAreatemp.getJQTP3());
							if(scenicAreatemp.getJQTP4()!=null&&!scenicAreatemp.getJQTP4().isEmpty())
								JQTP.add(scenicAreatemp.getJQTP4());
							if(scenicAreatemp.getJQTP5()!=null&&!scenicAreatemp.getJQTP5().isEmpty())
								JQTP.add(scenicAreatemp.getJQTP5());
							if(scenicAreatemp.getJQTP6()!=null&&!scenicAreatemp.getJQTP6().isEmpty())
								JQTP.add(scenicAreatemp.getJQTP6());
							if(scenicAreatemp.getJQTP7()!=null&&!scenicAreatemp.getJQTP7().isEmpty())
								JQTP.add(scenicAreatemp.getJQTP7());
							if(scenicAreatemp.getJQTP8()!=null&&!scenicAreatemp.getJQTP8().isEmpty())
								JQTP.add(scenicAreatemp.getJQTP8());
							if(scenicAreatemp.getJQTP9()!=null&&!scenicAreatemp.getJQTP9().isEmpty())
								JQTP.add(scenicAreatemp.getJQTP9());
							if(scenicAreatemp.getJQTP10()!=null&&!scenicAreatemp.getJQTP10().isEmpty())
								JQTP.add(scenicAreatemp.getJQTP10());
							
							
							
							
							
							
							
							
							//ZDTP1 2 3的请求方式封闭,统一放在ZDTP的数组中去
							scenicAreatemp.setJQTP1(null);
							scenicAreatemp.setJQTP2(null);
							scenicAreatemp.setJQTP3(null);
							scenicAreatemp.setJQTP4(null);
							scenicAreatemp.setJQTP5(null);
							scenicAreatemp.setJQTP6(null);
							scenicAreatemp.setJQTP7(null);
							scenicAreatemp.setJQTP8(null);
							scenicAreatemp.setJQTP9(null);
							scenicAreatemp.setJQTP10(null);
							
							
							scenicAreatemp.setJQTP(JQTP);
							scenicArealist.add(scenicAreatemp);
							}
						} catch (Exception e) {
								return null;
								}
					}
			}
			return scenicArealist;
	}

	@Override
	public ScenicArea selectScenicAreaByName(String JQMC) {
		if(JQMC!=null&&!"".equals(JQMC)){
			try {
				ScenicArea scenicArea = scenicAreaDao.selectScenicAreaByName(JQMC);
				if(scenicArea!=null)
					return scenicArea;
				else
					return null;
			} catch (Exception e) {
				return null;
			}
		}else
			return null;
	}




}
