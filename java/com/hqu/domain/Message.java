package com.hqu.domain;

import java.sql.Timestamp;
import java.util.List;

import javax.management.loading.PrivateClassLoader;

/** 
 * @author WangWeiWei 
 * @email  1345352429@qq.com
 * @time   2016年9月26日 上午9:34:54 
 */
public class Message {
    /** 
     * 消息流水号
     * */
    private int XXLSH;
    /**
     *消息类型代码
     */
    private String XXLXDM;
    /**
     *消息类型名称
     */
    private String XXLXMC;
    /**
     *发送时间
     */
    private Timestamp FSSJ;
    /**
     *发送人
     */
    private String FSR;
    /**
     *接收人
     */
    private String JSR;
    /**
     *消息内容
     */
    private String XXNR;
    
    /**
     * 消息状态
     */
    private String XXZT;
    
    private String CKJBDM;//乘客类型代码
    
    private String CKJBMC;//乘客类型名称
    
    private String CKXM;//乘客姓名
    
   private  String YHZH; 
	public String getYHZH() {
	return YHZH;
}
public void setYHZH(String yHZH) {
	YHZH = yHZH;
}
	public String getCKXM() {
		return CKXM;
	}
	public void setCKXM(String cKXM) {
		CKXM = cKXM;
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
	private int Page;
    
    public int getPage() {
		return Page;
	}
	public void setPage(int page) {
		Page = page;
	}
	public String getXXZT() {
		return XXZT;
	}
	public void setXXZT(String xXZT) {
		XXZT = xXZT;
	}
	public int getXXLSH() {
        return XXLSH;
    }
    public void setXXLSH(int xXLSH) {
        XXLSH = xXLSH;
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
    public Timestamp getFSSJ() {
        return FSSJ;
    }
    public void setFSSJ(Timestamp fSSJ) {
        FSSJ = fSSJ;
    }
    public String getFSR() {
        return FSR;
    }
    public void setFSR(String fSR) {
        FSR = fSR;
    }
    public String getJSR() {
        return JSR;
    }
    public void setJSR(String jSR) {
        JSR = jSR;
    }
    public String getXXNR() {
        return XXNR;
    }
    public void setXXNR(String xXNR) {
        XXNR = xXNR;
    }

}
