package com.hqu.domain;

import java.sql.Timestamp;

public class FeedBack {

	private int FKYJLSH;//反馈意见流水号
	private String FKLXDM;//反馈类型代码
	private String FKLXMC;//反馈类型名称
	private Timestamp FSSJ;//发送时间
	private String FSNR;//发送内容
	private String FSR;//发送人
	private Timestamp HFSJ;//回复时间
	private String HFNR;//回复内容
	private String HFR;//回复人

	private Integer Page;//页数
	public int getFKYJLSH() {
		return FKYJLSH;
	}
	public void setFKYJLSH(int fKYJLSH) {
		FKYJLSH = fKYJLSH;
	}
	public String getFKLXDM() {
		return FKLXDM;
	}
	public void setFKLXDM(String fKLXDM) {
		FKLXDM = fKLXDM;
	}
	public String getFKLXMC() {
		return FKLXMC;
	}
	public void setFKLXMC(String fKLXMC) {
		FKLXMC = fKLXMC;
	}
	public Timestamp getFSSJ() {
		return FSSJ;
	}
	public void setFSSJ(Timestamp fSSJ) {
		FSSJ = fSSJ;
	}
	public String getFSNR() {
		return FSNR;
	}
	public void setFSNR(String fSNR) {
		FSNR = fSNR;
	}
	public String getFSR() {
		return FSR;
	}
	public void setFSR(String fSR) {
		FSR = fSR;
	}
	public Timestamp getHFSJ() {
		return HFSJ;
	}
	public void setHFSJ(Timestamp hFSJ) {
		HFSJ = hFSJ;
	}
	public String getHFNR() {
		return HFNR;
	}
	public void setHFNR(String hFNR) {
		HFNR = hFNR;
	}
	public String getHFR() {
		return HFR;
	}
	public void setHFR(String hFR) {
		HFR = hFR;
	}
	public Integer getPage() {
		return Page;
	}
	public void setPage(Integer page) {
		Page = page;
	}
	
	
}
