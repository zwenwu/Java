package com.hqu.domain;


import java.sql.Timestamp;
import java.util.List;

public class Site {
	private String ZDDM;
	private String ZDMC;
	private String ZDDZ;
	private String ZDTP1;
	private String ZDTP2;
	private String ZDTP3;
	private List<String> ZDTP;
	private String CSDM;
	private String CSMC;
	private double JD;
	private double WD;
	private Timestamp FBSJ;
	private String ZDZTDM;
	private String ZDZTMC;
	//youcai新加
	private double startDis;
	private double endDis;
	
	public double getStartDis() {
		return startDis;
	}
	public void setStartDis(double startDis) {
		this.startDis = startDis;
	}
	public double getEndDis() {
		return endDis;
	}
	public void setEndDis(double endDis) {
		this.endDis = endDis;
	}
	private String XLDM;
	public String getZDDM() {
		return ZDDM;
	}
	public void setZDDM(String zDDM) {
		ZDDM = zDDM;
	}
	public String getXLDM() {
		return XLDM;
	}
	public void setXLDM(String xLDM) {
		XLDM = xLDM;
	}
	
	public String getZDMC() {
		return ZDMC;
	}
	public void setZDMC(String zDMC) {
		ZDMC = zDMC;
	}
	public String getZDDZ() {
		return ZDDZ;
	}
	public void setZDDZ(String zDDZ) {
		ZDDZ = zDDZ;
	}
	public String getZDTP1() {
		return ZDTP1;
	}
	public void setZDTP1(String zDTP1) {
		ZDTP1 = zDTP1;
	}
	public String getZDTP2() {
		return ZDTP2;
	}
	public void setZDTP2(String zDTP2) {
		ZDTP2 = zDTP2;
	}
	public String getZDTP3() {
		return ZDTP3;
	}
	public void setZDTP3(String zDTP3) {
		ZDTP3 = zDTP3;
	}
	public List<String> getZDTP() {
		return ZDTP;
	}
	public void setZDTP(List<String> zDTP) {
		ZDTP = zDTP;
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
	public double getJD() {
		return JD;
	}
	public void setJD(double jD) {
		JD = jD;
	}
	public double getWD() {
		return WD;
	}
	public void setWD(double wD) {
		WD = wD;
	}
	public Timestamp getFBSJ() {
		return FBSJ;
	}
	public void setFBSJ(Timestamp fBSJ) {
		FBSJ = fBSJ;
	}
	public String getZDZTDM() {
		return ZDZTDM;
	}
	public void setZDZTDM(String zDZTDM) {
		ZDZTDM = zDZTDM;
	}
	public String getZDZTMC() {
		return ZDZTMC;
	}
	public void setZDZTMC(String zDZTMC) {
		ZDZTMC = zDZTMC;
	}

	
}
