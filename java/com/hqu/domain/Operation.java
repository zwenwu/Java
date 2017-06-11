package com.hqu.domain;

import java.sql.Timestamp;

public class Operation {
	private int CZLSH;

	private Timestamp CZSJ;

	private String CZYM;

	private String CZLX;
	
	private String GNDM;

	private String YHZH;

	private String CZXQ;

	public Operation() {
		// TODO Auto-generated constructor stub
	}

	public Operation(Timestamp CZSJ, String GNDM, String YHZH) {
		this.CZSJ = CZSJ;
		this.GNDM = GNDM;
		this.YHZH = YHZH;
	}

	public int getCZLSH() {
		return CZLSH;
	}

	public void setCZLSH(int cZLSH) {
		CZLSH = cZLSH;
	}

	public Timestamp getCZSJ() {
		return CZSJ;
	}

	public void setCZSJ(Timestamp cZSJ) {
		CZSJ = cZSJ;
	}

	public String getCZYM() {
		return CZYM;
	}

	public void setCZYM(String cZYM) {
		CZYM = cZYM;
	}

	public String getCZLX() {
		return CZLX;
	}

	public void setCZLX(String cZLX) {
		CZLX = cZLX;
	}

	public String getGNDM() {
		return GNDM;
	}

	public void setGNDM(String gNDM) {
		GNDM = gNDM;
	}

	public String getYHZH() {
		return YHZH;
	}

	public void setYHZH(String yHZH) {
		YHZH = yHZH;
	}

	public String getCZXQ() {
		return CZXQ;
	}

	public void setCZXQ(String cZXQ) {
		CZXQ = cZXQ;
	}

	@Override
	public String toString() {
		return "还没重写呢";
	}
}
