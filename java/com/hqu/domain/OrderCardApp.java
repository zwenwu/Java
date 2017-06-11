package com.hqu.domain;

import java.sql.Timestamp;

public class OrderCardApp {
	private Timestamp QCSJ;
	private Timestamp KSSJ;
	private Timestamp JSSJ;
	private String QDMC;
	private String ZDMC;
	private String DDLXDM;
	public String getDDLXDM() {
		return DDLXDM;
	}

	public void setDDLXDM(String dDLXDM) {
		DDLXDM = dDLXDM;
	}

	public OrderCardApp() {
		// TODO 自动生成的构造函数存根
	}
	
	public OrderCardApp(Timestamp QCSJ, Timestamp KSSJ, Timestamp JSSJ, String QDMC, String ZDMC, String DDLXDM) {
		// TODO 自动生成的构造函数存根
		this.QCSJ = QCSJ;
		this.KSSJ = KSSJ;
		this.JSSJ = JSSJ;
		this.QDMC = QDMC;
		this.ZDMC = ZDMC;
		this.DDLXDM = DDLXDM;
	}
	public Timestamp getQCSJ() {
		return QCSJ;
	}
	public void setQCSJ(Timestamp qCSJ) {
		QCSJ = qCSJ;
	}
	public Timestamp getKSSJ() {
		return KSSJ;
	}
	public void setKSSJ(Timestamp kSSJ) {
		KSSJ = kSSJ;
	}
	public Timestamp getJSSJ() {
		return JSSJ;
	}
	public void setJSSJ(Timestamp jSSJ) {
		JSSJ = jSSJ;
	}
	public String getQDMC() {
		return QDMC;
	}
	public void setQDMC(String qDMC) {
		QDMC = qDMC;
	}
	public String getZDMC() {
		return ZDMC;
	}
	public void setZDMC(String zDMC) {
		ZDMC = zDMC;
	}
}
