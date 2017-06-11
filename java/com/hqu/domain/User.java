package com.hqu.domain;

public class User implements java.io.Serializable{

	/**
	 * 用户账号
	 */
	private String YHZH;

	/**
	 * 电话号码
	 */
	private String YDDH;

	/**
	 * 用户类别
	 */
	private String YHLBDM;

	/**
	 * 用户密码
	 */
	private String YHMM;

	/**
	 * 角色代码
	 */
	private String JSDM;
	
	private String JSMC;

	/**
	 * 加密盐
	 */
	private String Salt;
	/**
	 * 新密码
	 */
	private String xPassword;
	/**
	 * 用户头像
	 */
	private String YHTX;
	/**
	 * 邮箱
	 */
	private String YX;
	/**
	 * 姓名
	 */
	private String XM;
	/**
	 * 性别代码
	 */
	private String XBDM;
	
	private String XBMC;
	
	
	private String NOTE;
	/**
	 * 短信验证码
	 */
	private Integer YZM;

	private String CSDM;

	public String getCSDM() {
		return CSDM;
	}

	public void setCSDM(String cSDM) {
		CSDM = cSDM;
	}

	public Integer getYZM() {
		return YZM;
	}

	public void setYZM(Integer yZM) {
		YZM = yZM;
	}

	public String getXBMC() {
		return XBMC;
	}

	public void setXBMC(String xBMC) {
		XBMC = xBMC;
	}

	public String getJSMC() {
		return JSMC;
	}

	public void setJSMC(String jSMC) {
		JSMC = jSMC;
	}

	public String getNOTE() {
		return NOTE;
	}

	public void setNOTE(String nOTE) {
		NOTE = nOTE;
	}

	public String getYX() {
		return YX;
	}

	public void setYX(String yX) {
		YX = yX;
	}

	public String getXM() {
		return XM;
	}

	public void setXM(String xM) {
		XM = xM;
	}

	public String getXBDM() {
		return XBDM;
	}

	public void setXBDM(String xBDM) {
		XBDM = xBDM;
	}

	public String getYHTX() {
		return YHTX;
	}

	public void setYHTX(String yHTX) {
		YHTX = yHTX;
	}

	public String getxPassword() {
		return xPassword;
	}

	public void setxPassword(String xPassword) {
		this.xPassword = xPassword;
	}

	public String getSalt() {
		return Salt;
	}

	public void setSalt(String salt) {
		Salt = salt;
	}

	public String getYHZH() {
		return YHZH;
	}

	public void setYHZH(String yHZH) {
		YHZH = yHZH;
	}

	public String getYDDH() {
		return YDDH;
	}

	public void setYDDH(String yDDH) {
		YDDH = yDDH;
	}

	public String getYHLBDM() {
		return YHLBDM;
	}

	public void setYHLBDM(String yHLBDM) {
		YHLBDM = yHLBDM;
	}

	public String getYHMM() {
		return YHMM;
	}

	public void setYHMM(String yHMM) {
		this.YHMM = yHMM == null ? null : yHMM.trim();
	}

	public String getJSDM() {
		return JSDM;
	}

	public void setJSDM(String jSDM) {
		JSDM = jSDM;
	}

}