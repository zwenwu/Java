package com.hqu.domain;
import java.sql.Timestamp;
import java.text.DateFormat;

public class Ticket {

	private String BCDM;
	private String XLDM;
	private String XLMC;
	
	private String CPH;

	private String SJXM;
	private Timestamp FCSJ;
	private String QCPJ;
	private String ZPJG;
	private String YPJG;
	private String ZPS;
	private String SYPS;
	private String YTPS;
	private String BCZTDM;
	private String BCZTMC;
	private String NOTE;
	private String CSDM;/*城市代码*/	
	private String CSMC;/*城市名称*/
	public String getBCDM() {
		return BCDM;
	}
	public void setbCDM(String bCDM) {
		BCDM = bCDM;
	}

	public String getCSDM() {
		return CSDM;
	}

	public void setCSDM(String cSDM) {
		CSDM = cSDM;
	}
	public String getXLDM() {
		return XLDM;
	}
	public void setxLDM(String xLDM) {
		XLDM = xLDM;
	}
	public String getCSMC() {
		return CSMC;
	}

	public void setCSMC(String cSMC) {
		CSMC = cSMC;
	}
	
	public String getXLMC() {
		return XLMC;
	}
	public void setxLMC(String xLMC) {
		XLMC = xLMC;
	}
	
	
	public String getCPH() {
		return CPH;
	}
	public void setcPH(String cPH) {
		CPH = cPH;
	}
	
	
	public String getSJXM() {
		return SJXM;
	}
	public void setsJXM(String sJXM) {
		SJXM = sJXM;
	}
	
	
	public Timestamp getFCSJ() {
		return FCSJ;
	}
	public void setfCSJ(Timestamp fCSJ) {
		FCSJ = fCSJ;
	}
	
	
	public String getQCPJ() {
		return QCPJ;
	}
	public void setqCPJ(String qCPJ) {
		QCPJ = qCPJ;
	}
	
	public String getZPJG() {
		return ZPJG;
	}
	public void setzPJG(String zPJG) {
		ZPJG = zPJG;
	}
	
	public String getYPJG() {
		return YPJG;
	}
	public void setyPJG(String yPJG) {
		YPJG = yPJG;
	}
	
	
	public String getZPS() {
		return ZPS;
	}
	public void setzPS(String zPS) {
		ZPS = zPS;
	}
	
	
	public String getSYPS() {
		return SYPS;
	}
	public void setsYPS(String sYPS) {
		SYPS = sYPS;
	}
	
	
	public String getYTPS() {
		return YTPS;
	}
	public void setyTPS(String yTPS) {
		YTPS = yTPS;
	}
	
	
	public String getBCZTDM() {
		return BCZTDM;
	}
	public void setbCZTDM(String bCZTDM) {
		BCZTDM = bCZTDM;
	}
	
	
	public String getBCZTMC() {
		return BCZTMC;
	}
	public void setbCZTMC(String bCZTMC) {
		BCZTMC = bCZTMC;
	}
	
	
	public String getNOTE() {
		return NOTE;
	}
	public void setnOTE(String nOTE) {
		NOTE = nOTE;
	}
	
	
	private long DDH;			//订单号
	private Timestamp YDRQ;		//预定日期
	private String QDMC;		//起点名称
	private String TJZDMC;		//途经站点名称
	private String ZDMC;		//终点名称
	private Timestamp QCSJ;		//去程时间
	
	private double DJ;			//单价
	private int RS;				//人数
	private double ZJ;			//总价
	private String CLXQ;		//车辆需求
	private String APCL;		//安排车辆
	private int FWPJ;			//服务评价
	private String LXR;			//联系人
	private String YDDH;		//移动电话
	private String YHZH;		//用户账号
	private String DDLXDM;		//订单类型代码
	private String DDZTDM;		//订单状态代码
	
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

	public void setXLMC(String xLMC) {
		XLMC = xLMC;
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
		return TJZDMC;
	}
	public void setTJZDMC(String tJZDMC) {
		TJZDMC = tJZDMC;
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

	public void setFCSJ(Timestamp fCSJ) {
		FCSJ = fCSJ;
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
	
	public void setNOTE(String nOTE) {
		NOTE = nOTE;
	}
}
