package com.hqu.serviceImpl;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hqu.service.InteractionService;
import com.hqu.dao.Interactiondao;
import com.hqu.domain.Interaction;
@Service("InteractionService")

public class InteractionServiceImpl implements InteractionService {

	@Autowired

	private Interactiondao Interactiondao;

	public List<Interaction> selectcity(){
		return Interactiondao.selectcity();
	}
	
	public List<Interaction> selectdata(Timestamp start,Timestamp end,Interaction interaction){
		return Interactiondao.selectdata(start, end, interaction);
	}
	public int deleteinteraction(String FQXLDM){
		return Interactiondao.deleteinteraction(FQXLDM);
	}
	public int checkinteraction(String FQXLDM){
		return Interactiondao.checkinteraction(FQXLDM);
	}
	
	public List<Interaction> selectdatabyFQXLDM(String FQXLDM){
		return Interactiondao.selectdatabyFQXLDM( FQXLDM);
	}
	
	public List<Interaction> selectcomment(String FQXLDM){
		return Interactiondao.selectcomment(FQXLDM);
	}
	public List<Interaction> appselectallDZXL(String CSDM,String PLR,int start,int pageSize){
		List<Interaction> list=Interactiondao.appselectallDZXL(CSDM,start,pageSize);
		for(Interaction pojo: list){
			Interaction interaction=new Interaction();
			interaction.setFQXLDM(pojo.getFQXLDM());
			interaction.setPLR(PLR);
			List<Interaction> listcomment=Interactiondao.selectsupport(interaction);
			if(listcomment.size()==0){
				pojo.setWhethersupport(false);
				pojo.setWhethercomment(false);
			}
			else{
				for(Interaction pojo2:listcomment){
					if(pojo2.getPLLX().equals("3")){
						pojo.setWhethersupport(false);
					}
					else{
						pojo.setWhethersupport(true);
					}
					if(pojo2.getPLNR()==null||pojo2.getPLNR()==""){
						pojo.setWhethercomment(false);
					}
					else{
						pojo.setWhethercomment(true);
					}
				}
				
			}
		}
		return list;
	}
	public int appcountselectallDZXL(String CSDM){
		return Interactiondao.appcountselectallDZXL(CSDM);
	}
	public List<Interaction> appselectDZXLdetail(int FQXLDM,String PLR){
		List<Interaction> list=Interactiondao.appselectDZXLdetail(FQXLDM);
		for(Interaction pojo: list){
			Interaction interaction=new Interaction();
			interaction.setFQXLDM(pojo.getFQXLDM());
			interaction.setPLR(PLR);
			List<Interaction> listcomment=Interactiondao.selectsupport(interaction);
			if(listcomment.size()==0){
				pojo.setWhethersupport(false);
				pojo.setWhethercomment(false);
			}
			else{
				for(Interaction pojo2:listcomment){
					if(pojo2.getPLLX().equals("3")){
						pojo.setWhethersupport(false);
					}
					else{
						pojo.setWhethersupport(true);
					}
					if(pojo2.getPLNR()==null||pojo2.getPLNR()==""){
						pojo.setWhethercomment(false);
					}
					else{
						pojo.setWhethercomment(true);
					}
				}
				
			}
		}
		return list;
	}
	public List<Interaction> appselectcomment(int FQXLDM,int start,int pageSize){
		return Interactiondao.appselectcomment(FQXLDM, start, pageSize);
	}
	public int appcountselectcomment(int FQXLDM){
		return Interactiondao.appcountselectcomment(FQXLDM);
	}
	public int appFQXL(Interaction interaction){//app定制线路
	    return Interactiondao.appFQXL(interaction);
	
	}
	public  String whethersupport(int FQXLDM,String PLR){//是否已点赞
		String result="";
		Interaction interaction=new Interaction();
		interaction.setFQXLDM(FQXLDM);
		interaction.setPLR(PLR);
		List<Interaction> listcomment=Interactiondao.selectsupport(interaction);
		if(listcomment.size()==0){
			result="未点赞";
		}
		else{
			for(Interaction pojo2:listcomment){
				if(pojo2.getPLLX().equals("3")){
					result=pojo2.getPLNR()+"";
				}
				else{
					result="已点赞";
				}
				
			}
			
		}
		return result;
	}
	public void insertsupport(Interaction interaction){//点赞和评论
		Interactiondao.insertsupport(interaction);
		Interaction inter=new Interaction();
		inter.setFQXLDM(interaction.getFQXLDM());
		List<Interaction> list=Interactiondao.appselectallcomment(interaction.getFQXLDM());
		int zcrs=0;
		int plrs=0;
		for(Interaction pojo:list){
			if(pojo.getPLLX().equals("0")){
				zcrs++;
			}
			if(pojo.getPLNR()!=null){
				plrs++;
			}
		}
		inter.setZCRS(zcrs);
		inter.setPLRS(plrs);
		Interactiondao.updateinteraction(inter);
	}
	public List<Interaction> appselectDZXLbyFQXLDM(int FQXLDM,String PLR){
		List<Interaction> list= Interactiondao.appselectDZXLbyFQXLDM(FQXLDM);
		for(Interaction pojo: list){
			Interaction interaction=new Interaction();
			interaction.setFQXLDM(pojo.getFQXLDM());
			interaction.setPLR(PLR);
			List<Interaction> listcomment=Interactiondao.selectsupport(interaction);
			if(listcomment.size()==0){
				pojo.setWhethersupport(false);
				pojo.setWhethercomment(false);
			}
			else{
				for(Interaction pojo2:listcomment){
					if(pojo2.getPLLX().equals("3")){
						pojo.setWhethersupport(false);
					}
					else{
						pojo.setWhethersupport(true);
					}
					if(pojo2.getPLNR()==null||pojo2.getPLNR()==""){
						pojo.setWhethercomment(false);
					}
					else{
						pojo.setWhethercomment(true);
					}
				}
				
			}
		}
		return list;
	}
	public void  updatesupport(Interaction interaction){//用户存在时，点赞
		Interactiondao.updatesupport(interaction);
		Interaction inter=new Interaction();
		inter.setFQXLDM(interaction.getFQXLDM());
		List<Interaction> list=Interactiondao.appselectallcomment(interaction.getFQXLDM());
		int zcrs=0;
		int plrs=0;
		for(Interaction pojo:list){
			if(pojo.getPLLX().equals("0")){
				zcrs++;
			}
			if(pojo.getPLNR()!=null){
				plrs++;
			}
		}
		inter.setZCRS(zcrs);
		inter.setPLRS(plrs);
		Interactiondao.updateinteraction(inter);
	}
	public  String whethercomment(int FQXLDM,String PLR){//是否已评论
		String result="";
		Interaction interaction=new Interaction();
		interaction.setFQXLDM(FQXLDM);
		interaction.setPLR(PLR);
		List<Interaction> listcomment=Interactiondao.selectsupport(interaction);
		if(listcomment.size()==0){
			result="未评论";
		}
		else{
			for(Interaction pojo2:listcomment){
				if(pojo2.getPLNR()==null||pojo2.getPLNR()==""){
					result=pojo2.getPLLX()+"";
				}
				else{
					result="已评论";
				}
				
			}
			
		}
		return result;
	}
	 public void cancelsupportFQXL(Interaction interaction){//取消点赞
		 Interactiondao.cancelsupportFQXL(interaction);
		 Interaction inter=new Interaction();
			inter.setFQXLDM(interaction.getFQXLDM());
			List<Interaction> list=Interactiondao.appselectallcomment(interaction.getFQXLDM());
			int zcrs=0;
			int plrs=0;
			for(Interaction pojo:list){
				if(pojo.getPLLX().equals("0")){
					zcrs++;
				}
				if(pojo.getPLNR()!=null){
					plrs++;
				}
			}
			inter.setZCRS(zcrs);
			inter.setPLRS(plrs);
			Interactiondao.updateinteraction(inter);
	 }
}
