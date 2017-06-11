package com.hqu.domain;

import java.sql.Timestamp;

public class Passenger {
	private String YHZH;
	
	private String CKXM;
	
	private String YDDH;
	
	private String XBDM;
	
	private String XBMC;
	
	private String CSDM;
	
	private String CSMC;
	
	private String YX;
	
	private String DQJF;
	
	private String QBJF;
	
	private String CKZTDM;
	
	private String CKZTMC;

	private String CKJBDM;

	private String CKJBMC;	
	
	private String NOTE;
	
	private Timestamp ZCSJ;
	
	private Timestamp startTime;
	
	private Timestamp endTime;
	
    private String likeYHZH;
    
    private String likeYDDH;
    
    private String likeCKXM;
    
    private String isSex;
    
    private String isCity;
    
    private String likeYX;
    
    private String moreorlessDQJF;
    
    private String moreorlessQBJF;
    
    private String isCKJB;
    
    private String isCKZT;
    
    private String isZCSJ;
    
    private String SQLText;
	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public Timestamp getZCSJ() {
		return ZCSJ;
	}

	public void setZCSJ(Timestamp zCSJ) {
		ZCSJ = zCSJ;
	}

	public String getXBMC() {
		return XBMC;
	}

	public void setXBMC(String xBMC) {
		XBMC = xBMC;
	}

	public String getCSMC() {
		return CSMC;
	}

	public void setCSMC(String cSMC) {
		CSMC = cSMC;
	}

	public String getCKJBMC() {
		return CKJBMC;
	}

	public void setCKJBMC(String cKJBMC) {
		CKJBMC = cKJBMC;
	}

	public String getCKZTMC() {
		return CKZTMC;
	}

	public void setCKZTMC(String cKZTMC) {
		CKZTMC = cKZTMC;
	}

	public String getYHZH() {
		return YHZH;
	}

	public void setYHZH(String yHZH) {
		YHZH = yHZH;
	}

	public String getCKXM() {
		return CKXM;
	}

	public void setCKXM(String cKXM) {
		CKXM = cKXM;
	}

	public String getYDDH() {
		return YDDH;
	}

	public void setYDDH(String yDDH) {
		YDDH = yDDH;
	}

	public String getXBDM() {
		return XBDM;
	}

	public void setXBDM(String xBDM) {
		XBDM = xBDM;
	}

	public String getCSDM() {
		return CSDM;
	}

	public void setCSDM(String cSDM) {
		CSDM = cSDM;
	}

	public String getYX() {
		return YX;
	}

	public void setYX(String yX) {
		YX = yX;
	}

	public String getDQJF() {
		return DQJF;
	}

	public void setDQJF(String dQJF) {
		DQJF = dQJF;
	}

	public String getQBJF() {
		return QBJF;
	}

	public void setQBJF(String qBJF) {
		QBJF = qBJF;
	}

	public String getCKZTDM() {
		return CKZTDM;
	}

	public void setCKZTDM(String cKZTDM) {
		CKZTDM = cKZTDM;
	}

	public String getCKJBDM() {
		return CKJBDM;
	}

	public void setCKJBDM(String cKJBDM) {
		CKJBDM = cKJBDM;
	}

	public String getNOTE() {
		return NOTE;
	}

	public void setNOTE(String nOTE) {
		NOTE = nOTE;
	}

	public String getLikeYHZH() {
		return likeYHZH;
	}

	public void setLikeYHZH(String likeYHZH) {
		this.likeYHZH = likeYHZH;
	}

	public String getLikeYDDH() {
		return likeYDDH;
	}

	public void setLikeYDDH(String likeYDDH) {
		this.likeYDDH = likeYDDH;
	}

	public String getLikeCKXM() {
		return likeCKXM;
	}

	public void setLikeCKXM(String likeCKXM) {
		this.likeCKXM = likeCKXM;
	}

	public String getIsSex() {
		return isSex;
	}

	public void setIsSex(String isSex) {
		this.isSex = isSex;
	}

	public String getIsCity() {
		return isCity;
	}

	public void setIsCity(String isCity) {
		this.isCity = isCity;
	}

	public String getLikeYX() {
		return likeYX;
	}

	public void setLikeYX(String likeYX) {
		this.likeYX = likeYX;
	}

	public String getMoreorlessDQJF() {
		return moreorlessDQJF;
	}

	public void setMoreorlessDQJF(String moreorlessDQJF) {
		this.moreorlessDQJF = moreorlessDQJF;
	}

	public String getMoreorlessQBJF() {
		return moreorlessQBJF;
	}

	public void setMoreorlessQBJF(String moreorlessQBJF) {
		this.moreorlessQBJF = moreorlessQBJF;
	}

	public String getIsCKJB() {
		return isCKJB;
	}

	public void setIsCKJB(String isCKJB) {
		this.isCKJB = isCKJB;
	}

	public String getIsCKZT() {
		return isCKZT;
	}

	public void setIsCKZT(String isCKZT) {
		this.isCKZT = isCKZT;
	}

	public String getIsZCSJ() {
		return isZCSJ;
	}

	public void setIsZCSJ(String isZCSJ) {
		this.isZCSJ = isZCSJ;
	}

	public String getSQLText() {
		return SQLText;
	}

	public void setSQLText(String sQLText) {
		SQLText = sQLText;
	}
     
	
	
}
