package com.hqu.service;

import java.sql.Timestamp;
import java.util.List;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;

import com.hqu.domain.Interaction;

public interface InteractionService {

	public List<Interaction> selectcity();
	
	public List<Interaction> selectdata(Timestamp start,Timestamp end,Interaction interaction);
	
	public int deleteinteraction(String FQXLDM);
	
	public int checkinteraction(String FQXLDM);
	
	public List<Interaction> selectdatabyFQXLDM(String FQXLDM);
	
	public List<Interaction> selectcomment(String FQXLDM);
	
	public List<Interaction> appselectallDZXL(String CSDM,String PLR,int start,int pageSize);
	
	public int appcountselectallDZXL(String CSDM);
	
	public List<Interaction> appselectDZXLdetail(int FQXLDM,String PLR);
	
	public List<Interaction> appselectcomment(int FQXLDM,int start,int pageSize);
	
	public int appcountselectcomment(int FQXLDM);
	
	public int appFQXL(Interaction interaction);//app定制线路
	
	public String whethersupport(int FQXLDM,String PLR);//是否已点赞
	
	public void insertsupport(Interaction interaction);//用户不存在时，点赞
	
	public List<Interaction> appselectDZXLbyFQXLDM(int FQXLDM,String PLR);
	
	public void  updatesupport(Interaction interaction);//用户存在时，点赞
	
     public  String whethercomment(int FQXLDM,String PLR);//是否已评论
     
     public void cancelsupportFQXL(Interaction interaction);//取消点赞
}
