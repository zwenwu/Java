package com.hqu.domain;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class Line {
	
	private String CSDM;/*城市代码*/	
	
	private String XLDM;
	
	private String XLMC;
	
	private String QDDM;
	
	private String SQDMC;/*起点名称*/
	
	private List<Site> TJZD;/*途径站点*/
	
	private String ZDDM;
	
	private String EZDMC;/*终点名称*/
	
	private String CSMC;/*城市名称*/
	
	private Timestamp FBSJ;
	
	private String XLZTDM;
	
	private String NOTE;
	
	private String XLZTMC;	
	
	private String TJZDDM;/*途径站点代码*/
	
	private String TJZDMC;/*途径站点名称*/	
	
	private String SCSJ;
	private String SCZT;
	private Integer Page;
	private boolean isKeeped;	
	private String YHZH;
	
	private String XLLX;//是否直通车	
	
	private String QDMC;//我的位置名称
	private double QDJD;//起点经度
	private double QDWD;//起点纬度
	private String ZDMC;//终点名称
	private double ZDJD;//终点经度
	private double ZDWD;//终点纬度
	private double totalDis;
	
	private boolean LocationOrNot ;//是否推荐线路
	
	
	public boolean isLocationOrNot() {
		return LocationOrNot;
	}
	public void setLocationOrNot(boolean locationOrNot) {
		LocationOrNot = locationOrNot;
	}
	public double getTotalDis() {
		return totalDis;
	}
	public void setTotalDis(double totalDis) {
		this.totalDis = totalDis;
	}
	public double getZDJD() {
		return ZDJD;
	}

	public double getZDWD() {
		return ZDWD;
	}

	public String getQDMC() {
		return QDMC;
	}

	public void setQDMC(String qDMC) {
		QDMC = qDMC;
	}

	public double getQDJD() {
		return QDJD;
	}

	public void setQDJD(double qDJD) {
		QDJD = qDJD;
	}

	public double getQDWD() {
		return QDWD;
	}

	public void setQDWD(double qDWD) {
		QDWD = qDWD;
	}

	public void setZDJD(double zDJD) {
		ZDJD = zDJD;
	}

	public void setZDWD(double zDWD) {
		ZDWD = zDWD;
	}

	public String getZDMC() {
		return ZDMC;
	}

	public void setZDMC(String zDMC) {
		ZDMC = zDMC;
	}


	public String getXLLX() {
		return XLLX;
	}

	public void setXLLX(String xLLX) {
		XLLX = xLLX;
	}

	public String getYHZH() {
		return YHZH;
	}

	public void setYHZH(String yHZH) {
		YHZH = yHZH;
	}

	public boolean isKeeped() {
		return isKeeped;
	}

	public void setKeeped(boolean isKeeped) {
		this.isKeeped = isKeeped;
	}

	public Integer getPage() {
		return Page;
	}

	public void setPage(Integer page) {
		Page = page;
	}

	public String getSCZT() {
		return SCZT;
	}

	public void setSCZT(String sCZT) {
		SCZT = sCZT;
	}

	public String getSCSJ() {
		return SCSJ;
	}

	public void setSCSJ(String sCSJ) {
		SCSJ = sCSJ;
	}

	public List<Site> getTJZD() {
		return TJZD;
	}

	public void setTJZD(List<Site> tJZD) {
		TJZD = tJZD;
	}

	public String getCSDM() {
		return CSDM;
	}

	public void setCSDM(String cSDM) {
		CSDM = cSDM;
	}

	public String getCSMC() {
		return CSMC;
	}

	public void setCSMC(String cSMC) {
		CSMC = cSMC;
	}

	public String getTJZDMC() {
		return TJZDMC;
	}

	public void setTJZDMC(String tJZDMC) {
		TJZDMC = tJZDMC;
	}

	public String getTJZDDM() {
		return TJZDDM;
	}

	public void setTJZDDM(String tJZDDM) {
		TJZDDM = tJZDDM;
	}

	public String getXLDM() {
		return XLDM;
	}

	public void setXLDM(String xLDM) {
		XLDM = xLDM;
	}

	public String getXLMC() {
		return XLMC;
	}

	public void setXLMC(String xLMC) {
		XLMC = xLMC;
	}

	public String getQDDM() {
		return QDDM;
	}

	public void setQDDM(String qDDM) {
		QDDM = qDDM;
	}

	public String getZDDM() {
		return ZDDM;
	}

	public void setZDDM(String zDDM) {
		ZDDM = zDDM;
	}
	

	public Timestamp getFBSJ() {
		return FBSJ;
	}

	public void setFBSJ(Timestamp fBSJ) {
		FBSJ = fBSJ;
	}

	public String getXLZTDM() {
		return XLZTDM;
	}

	public void setXLZTDM(String xLZTDM) {
		XLZTDM = xLZTDM;
	}

	public String getNOTE() {
		return NOTE;
	}

	public void setNOTE(String nOTE) {
		NOTE = nOTE;
	}

	public String getXLZTMC() {
		return XLZTMC;
	}

	public void setXLZTMC(String xLZTMC) {
		XLZTMC = xLZTMC;
	}

	public String getSQDMC() {
		return SQDMC;
	}

	public void setSQDMC(String sQDMC) {
		SQDMC = sQDMC;
	}

	public String getEZDMC() {
		return EZDMC;
	}

	public void setEZDMC(String eZDMC) {
		EZDMC = eZDMC;
	}
	
}
