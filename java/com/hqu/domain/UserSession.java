package com.hqu.domain;
import java.sql.Timestamp;

public class UserSession {
	private String sessionid;
	private String YHZH;
	private Timestamp expire;
	public String getSessionid() {
		return sessionid;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	public String getYHZH() {
		return YHZH;
	}
	public void setYHZH(String yHZH) {
		YHZH = yHZH;
	}
	public Timestamp getExpire() {
		return expire;
	}
	public void setExpire(Timestamp expire) {
		this.expire = expire;
	}	
}
