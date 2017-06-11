package com.hqu.domain;

public class Type {
	/*
	 * T_UserType_C（用户类别代码表）1
	 * */
	/**
	 * 用户类别代码
	 */
	private String YHLBDM;
	/**
	 * 用户类别名称
	 */
	private String YHLBMC;
	
	/*
	 * T_Role_C（角色代码表）2
	 * */
	/**
	 * 角色代码
	 */
	private String JSDM;
	/**
	 * 角色名称
	 */
	private String JSMC;
	
	/*
	 * T_Sex_C（性别代码表）3
	 * */
	/**
	 * 性别代码
	 */
	private String XBDM;
	/**
	 * 性别名称
	 */
	private String XBMC;

	/*
	 * T_Province_C（省代码表）4
	 * */
	/**
	 * 省代码
	 */
	private String SDM;
	/**
	 * 省名称
	 */
	private String SMC; 
	
	/*
	 * T_City_C（城市代码表）5
	 * */
	/**
	 * 城市代码
	 */
	private String CSDM;
	//private String SDM;//外键：省代码
	/**
	 * 城市名称
	 */
	private String CSMC;
	
	/*
	 * T_Area_C（区代码表）6
	 * */
	/**
	 * 区代码
	 */
	private String QDM;
	//private String CSDM;//外键：城市代码
	/**
	 * 区名称
	 */
	private String QMC;
	
	/*
	 * T_PassengerStatus_C（乘客状态代码表）7
	 * */
	/**
	 * 乘客状态代码
	 */
	private String CKZTDM;
	/**
	 * 乘客状态名称
	 */
	private String CKZTMC;
	
	/*
	 * T_PassengerLevel_C（乘客级别代码表）8
	 * */
	/**
	 * 乘客级别代码
	 */
	private String CKJBDM;
	/**
	 * 乘客级别名称
	 */
	private String CKJBMC;
	/**
	 * 乘客级别积分
	 */
	private Double CKJBJF;
	
	/*
	 * T_Brand_C（品牌代码表）9
	 * */
	/**
	 * 品牌代码
	 */
	private String PPDM;
	/**
	 * 品牌名称
	 */
	private String PPMC;
	
	/*
	 * T_ModelNo_C（型号代码表）10
	 * */
	/**
	 * 型号代码
	 */
	private String XHDM;
	/**
	 * 型号名称
	 */
	private String XHMC;
	
	/*
	 * T_GearBoxType_C（变速箱类型代码表）11
	 * */
	/**
	 * 变速箱类型代码
	 */
	private String BSXLXDM; 
	/**
	 * 变速箱类型名称
	 */
	private String BSXLXMC;	
	
	/*
	 * T_Company_C（公司代码表）12
	 * */
	/**
	 * 公司代码
	 */
	private String SSGSDM;
	/**
	 * 公司名称
	 */
	private String SSGSMC;
	
	/*
	 * T_MotorcycleType_C（车型代码表）13
	 * */
	/**
	 * 车型代码
	 */
	private String CXDM;
	/**
	 * 车型名称
	 */
	private String CXMC;
	
	/*
	 * T_Color_C（颜色代码表）14
	 * */
	/**
	 * 颜色代码
	 */
	private String CLYSDM;
	/**
	 * 颜色名称
	 */
	private String CLYSMC;
	
	/*
	 * T_VehicleStatus_C（车辆状态代码表）15
	 * */
	/**
	 * 车辆状态代码
	 */
	private String CLZTDM;
	/**
	 * 车辆状态名称
	 */
	private String CLZTMC;
	
	/*
	 * T_DriverType_C（司机类型代码表）16
	 * */
	/**
	 * 司机类型代码
	 */
	private String SJLXDM;
	/**
	 * 司机类型名称
	 */
	private String SJLXMC;
	
	/*
	 * T_DriverStatus_C（司机状态代码表）17
	 * */
	/**
	 * 司机状态代码
	 */
	private String SJZTDM;
	/**
	 * 司机状态名称
	 */
	private String SJZTMC;
	
	/*
	 * T_SiteStatus_C（站点状态代码表）18
	 * */
	/**
	 * 站点状态代码
	 */
	private String ZDZTDM;
	/**
	 * 站点状态名称
	 */
	private String ZDZTMC;
	
	/*
	 * T_RouteStatus_C（线路状态代码表）19
	 * */
	/**
	 * 线路状态代码
	 */
	private String XLZTDM;
	/**
	 * 线路状态名称
	 */
	private String XLZTMC;
	
	/*
	 * T_ScheduleStatus_C（班次状态代码表）20
	 * */
	/**
	 * 班次状态代码
	 */
	private String BCZTDM;
	/**
	 * 班次状态名称
	 */
	private String BCZTMC;
	
	/*
	 * T_OrderType_C（订单类型代码表）21
	 * */
	/**
	 * 订单类型代码
	 */
	private String DDLXDM;
	/**
	 * 订单类型名称
	 */
	private String DDLXMC;
	
	/*
	 * T_OrderStatus_C（订单状态代码表）22
	 * */
	/**
	 * 订单状态代码
	 */
	private String DDZTDM;
	/**
	 * 订单状态名称
	 */
	private String DDZTMC;
	
	/*
	 * T_MessageType_C（活动类型代码表）23
	 * */
	/**
	 * 消息类型代码
	 */
	private String XXLXDM;
	/**
	 * 消息类型名称
	 */
	private String XXLXMC;
	/**
	 * 卡片状态代码
	 */
	private String KPZTDM;
	/**
	 * 卡片状态名称
	 */
	private String KPZTMC;
	/**
	 * 卡片类型代码
	 */
	private String KPLXDM;
	/**
	 * 卡片类型名称
	 */
	private String KPLXMC;
	/*
	 * 班次类型代码表
	 * */
	/**
	 * 班次类型代码
	 */
	private String BCLXDM;
	/**
	 * 班次类型名称
	 */
	private String BCLXMC;
	
	public String getKPZTDM() {
		return KPZTDM;
	}
	public void setKPZTDM(String kPZTDM) {
		KPZTDM = kPZTDM;
	}
	public String getKPZTMC() {
		return KPZTMC;
	}
	public void setKPZTMC(String kPZTMC) {
		KPZTMC = kPZTMC;
	}
	public String getKPLXDM() {
		return KPLXDM;
	}
	public void setKPLXDM(String kPLXDM) {
		KPLXDM = kPLXDM;
	}
	public String getKPLXMC() {
		return KPLXMC;
	}
	public void setKPLXMC(String kPLXMC) {
		KPLXMC = kPLXMC;
	}
	public String getYHLBDM() {
		return YHLBDM;
	}
	public void setYHLBDM(String yHLBDM) {
		YHLBDM = yHLBDM;
	}
	public String getYHLBMC() {
		return YHLBMC;
	}
	public void setYHLBMC(String yHLBMC) {
		YHLBMC = yHLBMC;
	}
	public String getJSDM() {
		return JSDM;
	}
	public void setJSDM(String jSDM) {
		JSDM = jSDM;
	}
	public String getJSMC() {
		return JSMC;
	}
	public void setJSMC(String jSMC) {
		JSMC = jSMC;
	}
	public String getXBDM() {
		return XBDM;
	}
	public void setXBDM(String xBDM) {
		XBDM = xBDM;
	}
	public String getXBMC() {
		return XBMC;
	}
	public void setXBMC(String xBMC) {
		XBMC = xBMC;
	}
	public String getSDM() {
		return SDM;
	}
	public void setSDM(String sDM) {
		SDM = sDM;
	}
	public String getSMC() {
		return SMC;
	}
	public void setSMC(String sMC) {
		SMC = sMC;
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
	public String getQDM() {
		return QDM;
	}
	public void setQDM(String qDM) {
		QDM = qDM;
	}
	public String getQMC() {
		return QMC;
	}
	public void setQMC(String qMC) {
		QMC = qMC;
	}
	public String getCKZTDM() {
		return CKZTDM;
	}
	public void setCKZTDM(String cKZTDM) {
		CKZTDM = cKZTDM;
	}
	public String getCKZTMC() {
		return CKZTMC;
	}
	public void setCKZTMC(String cKZTMC) {
		CKZTMC = cKZTMC;
	}
	public String getCKJBDM() {
		return CKJBDM;
	}
	public void setCKJBDM(String cKJBDM) {
		CKJBDM = cKJBDM;
	}
	public String getCKJBMC() {
		return CKJBMC;
	}
	public void setCKJBMC(String cKJBMC) {
		CKJBMC = cKJBMC;
	}
	public Double getCKJBJF() {
		return CKJBJF;
	}
	public void setCKJBJF(Double cKJBJF) {
		CKJBJF = cKJBJF;
	}
	public String getPPDM() {
		return PPDM;
	}
	public void setPPDM(String pPDM) {
		PPDM = pPDM;
	}
	public String getPPMC() {
		return PPMC;
	}
	public void setPPMC(String pPMC) {
		PPMC = pPMC;
	}
	public String getXHDM() {
		return XHDM;
	}
	public void setXHDM(String xHDM) {
		XHDM = xHDM;
	}
	public String getXHMC() {
		return XHMC;
	}
	public void setXHMC(String xHMC) {
		XHMC = xHMC;
	}
	public String getBSXLXDM() {
		return BSXLXDM;
	}
	public void setBSXLXDM(String bSXLXDM) {
		BSXLXDM = bSXLXDM;
	}
	public String getBSXLXMC() {
		return BSXLXMC;
	}
	public void setBSXLXMC(String bSXLXMC) {
		BSXLXMC = bSXLXMC;
	}
	public String getSSGSDM() {
		return SSGSDM;
	}
	public void setSSGSDM(String sSGSDM) {
		SSGSDM = sSGSDM;
	}
	public String getSSGSMC() {
		return SSGSMC;
	}
	public void setSSGSMC(String sSGSMC) {
		SSGSMC = sSGSMC;
	}
	public String getCXDM() {
		return CXDM;
	}
	public void setCXDM(String cXDM) {
		CXDM = cXDM;
	}
	public String getCXMC() {
		return CXMC;
	}
	public void setCXMC(String cXMC) {
		CXMC = cXMC;
	}
	public String getCLYSDM() {
		return CLYSDM;
	}
	public void setCLYSDM(String cLYSDM) {
		CLYSDM = cLYSDM;
	}
	public String getCLYSMC() {
		return CLYSMC;
	}
	public void setCLYSMC(String cLYSMC) {
		CLYSMC = cLYSMC;
	}
	public String getCLZTDM() {
		return CLZTDM;
	}
	public void setCLZTDM(String cLZTDM) {
		CLZTDM = cLZTDM;
	}
	public String getCLZTMC() {
		return CLZTMC;
	}
	public void setCLZTMC(String cLZTMC) {
		CLZTMC = cLZTMC;
	}
	public String getSJLXDM() {
		return SJLXDM;
	}
	public void setSJLXDM(String sJLXDM) {
		SJLXDM = sJLXDM;
	}
	public String getSJLXMC() {
		return SJLXMC;
	}
	public void setSJLXMC(String sJLXMC) {
		SJLXMC = sJLXMC;
	}
	public String getSJZTDM() {
		return SJZTDM;
	}
	public void setSJZTDM(String sJZTDM) {
		SJZTDM = sJZTDM;
	}
	public String getSJZTMC() {
		return SJZTMC;
	}
	public void setSJZTMC(String sJZTMC) {
		SJZTMC = sJZTMC;
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
	public String getXLZTDM() {
		return XLZTDM;
	}
	public void setXLZTDM(String xLZTDM) {
		XLZTDM = xLZTDM;
	}
	public String getXLZTMC() {
		return XLZTMC;
	}
	public void setXLZTMC(String xLZTMC) {
		XLZTMC = xLZTMC;
	}
	public String getBCZTDM() {
		return BCZTDM;
	}
	public void setBCZTDM(String bCZTDM) {
		BCZTDM = bCZTDM;
	}
	public String getBCZTMC() {
		return BCZTMC;
	}
	public void setBCZTMC(String bCZTMC) {
		BCZTMC = bCZTMC;
	}
	public String getDDLXDM() {
		return DDLXDM;
	}
	public void setDDLXDM(String dDLXDM) {
		DDLXDM = dDLXDM;
	}
	public String getDDLXMC() {
		return DDLXMC;
	}
	public void setDDLXMC(String dDLXMC) {
		DDLXMC = dDLXMC;
	}
	public String getDDZTDM() {
		return DDZTDM;
	}
	public void setDDZTDM(String dDZTDM) {
		DDZTDM = dDZTDM;
	}
	public String getDDZTMC() {
		return DDZTMC;
	}
	public void setDDZTMC(String dDZTMC) {
		DDZTMC = dDZTMC;
	}
	public String getXXLXDM() {
		return XXLXDM;
	}
	public void setXXLXDM(String xXLXDM) {
		XXLXDM = xXLXDM;
	}
	public String getXXLXMC() {
		return XXLXMC;
	}
	public void setXXLXMC(String xXLXMC) {
		XXLXMC = xXLXMC;
	}
	public String getBCLXDM() {
		return BCLXDM;
	}
	public void setBCLXDM(String bCLXDM) {
		BCLXDM = bCLXDM;
	}
	public String getBCLXMC() {
		return BCLXMC;
	}
	public void setBCLXMC(String bCLXMC) {
		BCLXMC = bCLXMC;
	}
	
	
}
