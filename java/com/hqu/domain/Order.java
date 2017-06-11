package com.hqu.domain;

import java.sql.Timestamp;

public class Order {
	
	private long DDH;			//订单号
	private Timestamp YDRQ;		//预定日期
	private String XLMC;		//线路名称
	private String BCDM;		//班次代码
	private String QDMC;		//起点名称
	private String QDDM;		//起点代码
	private String TJZDDM;		//途经站点代码
	private String ZDMC;		//终点名称
	private String ZDDM;		//终点代码
	private Timestamp QCSJ;		//去程时间
	private Timestamp FCSJ;		//返程时间
	private double DJ;			//单价
	private int RS;				//人数
	private double ZJ;			//总价
	private String CLXQ;		//车辆需求
	private String APCL;		//安排车辆
	private String APSJ;		//安排司机
	private int FWPJ;			//服务评价
	private String LXR;			//联系人
	private String YDDH;		//移动电话
	private String YHZH;		//用户账号
	private String DDLXDM;		//订单类型代码
	private String DDZTDM;		//订单状态代码
	private String NOTE;		//备注
	
	private double QDJD;		//起点经度
	private double QDWD;		//终点纬度
	private double ZDJD;		//终点经度
	private double ZDWD;		//终点纬度
	
	private Timestamp KSSJ;		//开始时间
	private Timestamp JSSJ;		//结束时间
	private String XLDM;		//线路代码
	
	private int TS;				//天数
	
	private Timestamp TKSJ;		//退款时间
	private String TKYY;		//退款原因
	
	private String SCZT;		//上车状态
	
	
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
	public double getZDJD() {
		return ZDJD;
	}
	public void setZDJD(double zDJD) {
		ZDJD = zDJD;
	}
	public double getZDWD() {
		return ZDWD;
	}
	public void setZDWD(double zDWD) {
		ZDWD = zDWD;
	}
	
	public String getTJZDDM() {
		return TJZDDM;
	}
	public void setTJZDDM(String tJZDDM) {
		TJZDDM = tJZDDM;
	}
	public String getSCZT() {
		return SCZT;
	}
	public void setSCZT(String sCZT) {
		SCZT = sCZT;
	}
	public String getAPSJ() {
		return APSJ;
	}
	public void setAPSJ(String aPSJ) {
		APSJ = aPSJ;
	}
	public Timestamp getTKSJ() {
		return TKSJ;
	}
	public void setTKSJ(Timestamp tKSJ) {
		TKSJ = tKSJ;
	}
	public String getTKYY() {
		return TKYY;
	}
	public void setTKYY(String tKYY) {
		TKYY = tKYY;
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
	public void setJSSJ(String jSSJ) {
		if (jSSJ == "") {
			JSSJ = null;
		}
		else {
			JSSJ = new Timestamp(Integer.parseInt(jSSJ));
		}
	}
	public String getXLDM() {
		return XLDM;
	}
	public void setXLDM(String xLDM) {
		XLDM = xLDM;
	}
	
	public int getTS() {
		return TS;
	}
	public void setTS(int tS) {
		TS = tS;
	}
	private int Page;
	public int getPage() {
		return Page - 1;
	}
	public void setPage(int page) {
		this.Page = page;
	}
	public long getDDH() {
		return DDH;
	}
	public void setDDH(long dDH) {
		DDH = dDH;
	}
	public Timestamp getYDRQ() {
		return YDRQ;
	}
	public void setYDRQ(Timestamp yDRQ) {
		YDRQ = yDRQ;
	}
	public String getXLMC() {
		return XLMC;
	}
	public void setXLMC(String xLMC) {
		XLMC = xLMC;
	}
	public String getBCDM() {
		return BCDM;
	}
	public void setBCDM(String bCDM) {
		BCDM = bCDM;
	}
	public String getQDMC() {
		return QDMC;
	}
	public void setQDMC(String qDMC) {
		QDMC = qDMC;
	}
	public String getTJZDMC() {
		return TJZDDM;
	}
	public void setTJZDMC(String tJZDMC) {
		TJZDDM = tJZDMC;
	}
	public String getZDMC() {
		return ZDMC;
	}
	public void setZDMC(String zDMC) {
		ZDMC = zDMC;
	}
	public Timestamp getQCSJ() {
		return QCSJ;
	}
	public void setQCSJ(Timestamp qCSJ) {
		QCSJ = qCSJ;
	}
	public Timestamp getFCSJ() {
		return FCSJ;
	}
	public void setFCSJ(Timestamp fCSJ) {
		FCSJ = fCSJ;
	}
	public void setFCSJ(String fCSJ) {
		if (fCSJ == "") {
			FCSJ = null;
		}
		else {
			FCSJ = new Timestamp(Integer.parseInt(fCSJ));
		}
	}
	public double getDJ() {
		return DJ;
	}
	public void setDJ(double dJ) {
		DJ = dJ;
	}
	public int getRS() {
		return RS;
	}
	public void setRS(int rS) {
		RS = rS;
	}
	public double getZJ() {
		return ZJ;
	}
	public void setZJ(double zJ) {
		ZJ = zJ;
	}
	public String getCLXQ() {
		return CLXQ;
	}
	public void setCLXQ(String cLXQ) {
		CLXQ = cLXQ;
	}
	public String getAPCL() {
		return APCL;
	}
	public void setAPCL(String aPCL) {
		APCL = aPCL;
	}
	public int getFWPJ() {
		return FWPJ;
	}
	public void setFWPJ(int fWPJ) {
		FWPJ = fWPJ;
	}
	public String getLXR() {
		return LXR;
	}
	public void setLXR(String lXR) {
		LXR = lXR;
	}
	public String getYDDH() {
		return YDDH;
	}
	public void setYDDH(String yDDH) {
		YDDH = yDDH;
	}
	public String getYHZH() {
		return YHZH;
	}
	public void setYHZH(String yHZH) {
		YHZH = yHZH;
	}
	public String getDDLXDM() {
		return DDLXDM;
	}
	public void setDDLXDM(String dDLXDM) {
		DDLXDM = dDLXDM;
	}
	public String getDDZTDM() {
		return DDZTDM;
	}
	public void setDDZTDM(String dDZTDM) {
		DDZTDM = dDZTDM;
	}
	public String getNOTE() {
		return NOTE;
	}
	public void setNOTE(String nOTE) {
		NOTE = nOTE;
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
}
