package com.hqu.dao;

import java.sql.Timestamp;
import java.util.List;

import com.hqu.domain.Interaction;

public interface Interactiondao {

	List<Interaction> selectcity();//城市下拉框
	
	List<Interaction> selectdata(Timestamp start,Timestamp end,Interaction interaction);//按条件查询定制线路
	
	int deleteinteraction(String FQXLDM);//删除定制线路
	
	int checkinteraction(String FQXLDM);//审核定制线路
	
	List<Interaction> selectdatabyFQXLDM(String FQXLDM);//根据定制线路查看线路详情
	
    List<Interaction> selectcomment(String FQXLDM);//查看点赞人
    
    List<Interaction> appselectallDZXL(String CSDM,int start,int pageSize);//app根据城市查询定制的线路
    
    int appcountselectallDZXL(String CSDM);
    
    List<Interaction> appselectDZXLdetail(int FQXLDM);//app根据返回的定制线路代码查询该定制线路的详情
    
    List<Interaction> appselectcomment(int FQXLDM,int start,int pageSize);
    
    int appcountselectcomment(int FQXLDM);
    
    int appFQXL(Interaction interaction);//app定制线路
    
    List<Interaction> selectsupport(Interaction interaction);//查看评论表中该用户是否已对该线路评论
    
    int insertsupport(Interaction interaction);//账号不存在，app点赞评论定制的线路
    
    int  updatesupport(Interaction interaction);//账号存在，app点赞评论定制的线路
    
    List<Interaction> appselectDZXLbyFQXLDM(int FQXLDM);
    
    int updateinteraction(Interaction interaction);
    
    List<Interaction> appselectallcomment(int FQXLDM);
    
    int cancelsupportFQXL(Interaction interaction);//取消点赞
}