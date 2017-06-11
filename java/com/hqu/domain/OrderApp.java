package com.hqu.domain;

import java.sql.Timestamp;

public class OrderApp {
	private long DDH;			//订单号
	private String QDMC;		//起点名称
	private String ZDMC;		//终点名称
	private Timestamp QCSJ;		//去程时间
	private int RS;				//人数
	private double ZJ;			//总价
	private String DDLXDM;		//订单类型代码
	
	private Timestamp FCSJ;		//返程时间
	private Timestamp JSSJ;		//结束时间
	
	private String APCL;		//安排车辆
	
	private boolean XLSC;		//线路收藏
	
	private String DDZTDM;		//订单状态代码
	
	//应用ID
	private String appid;
	//商户号
	private String mch_id;
	//nonce_str
	private String nonce_str;
	//签名
	private String sign;
	//商品描述
	private String body;
	//商户订单号
	private String out_trade_no;
	//总金额
	private Integer total_fee;
	//终端IP
	private String spbill_create_ip;
	//通知地址
	private String notify_url;
	//交易类型
	private String trade_type;
	
	private String partnerId;//商户号
	
	private String prepayId;//预支付交易会话ID
	private String nonceStr;//随机字符串
	private Timestamp timeStamp;//时间戳
	private String package_;//扩展字段
	
	
	
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getMch_id() {
		return mch_id;
	}
	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}
	public String getNonce_str() {
		return nonce_str;
	}
	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public Integer getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(Integer total_fee) {
		this.total_fee = total_fee;
	}
	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}
	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}
	public String getNotify_url() {
		return notify_url;
	}
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	public String getTrade_type() {
		return trade_type;
	}
	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}
	public String getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	public String getPrepayId() {
		return prepayId;
	}
	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}
	public String getNonceStr() {
		return nonceStr;
	}
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	public Timestamp getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getPackage_() {
		return package_;
	}
	public void setPackage_(String package_) {
		this.package_ = package_;
	}
	public String getDDZTDM() {
		return DDZTDM;
	}
	public void setDDZTDM(String dDZTDM) {
		DDZTDM = dDZTDM;
	}
	public boolean isXLSC() {
		return XLSC;
	}
	public void setXLSC(boolean xLSC) {
		XLSC = xLSC;
	}
	public String getAPCL() {
		return APCL;
	}
	public void setAPCL(String aPCL) {
		APCL = aPCL;
	}
	private String XLDM;
	public Timestamp getFCSJ() {
		return FCSJ;
	}
	public void setFCSJ(Timestamp fCSJ) {
		FCSJ = fCSJ;
	}
	public Timestamp getJSSJ() {
		return JSSJ;
	}
	public void setJSSJ(Timestamp jSSJ) {
		JSSJ = jSSJ;
	}
	
	
	
	public OrderApp() {
		// TODO 自动生成的构造函数存根
	}
	public OrderApp(long DDH, String QDMC, String ZDMC, Timestamp QCSJ, int RS, double ZJ, String DDLXDM, Timestamp FCSJ, Timestamp JSSJ, String XLDM, String APCL, boolean XLSC, String DDZTDM) {
		// TODO 自动生成的构造函数存根
		this.DDH = DDH;
		this.QDMC = QDMC;
		this.ZDMC = ZDMC;
		this.QCSJ = QCSJ;
		this.RS = RS;
		this.ZJ = ZJ;
		this.DDLXDM = DDLXDM;
		this.FCSJ = FCSJ;
		this.JSSJ = JSSJ;
		this.XLDM = XLDM;
		this.APCL = APCL;
		this.XLSC = XLSC;
		this.DDZTDM = DDZTDM;
	}
	public String getXLDM() {
		return XLDM;
	}
	public void setXLDM(String xLDM) {
		XLDM = xLDM;
	}
	public long getDDH() {
		return DDH;
	}
	public void setDDH(long dDH) {
		DDH = dDH;
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
	public Timestamp getQCSJ() {
		return QCSJ;
	}
	public void setQCSJ(Timestamp qCSJ) {
		QCSJ = qCSJ;
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
	public String getDDLXDM() {
		return DDLXDM;
	}
	public void setDDLXDM(String dDLXDM) {
		DDLXDM = dDLXDM;
	}
}
