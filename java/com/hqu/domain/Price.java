package com.hqu.domain;

import java.sql.Timestamp;

/** 
 * @author WangWeiWei 
 * @email  1345352429@qq.com
 * @time   2016年9月26日 上午9:55:06 
 */
public class Price {
    /**
     *线路代码
     */
    private String XLDM;
    /**
     *线路名称
     */
    private String XLMC;
    /**
     *全程票价
     */
    private Double CPPJ;

    /**
     *班次状态代码
     */
    private String QDDM;
    /**
     *班次状态名称
     */
    private String ZDDM;
 
    public String getXLDM() {
        return XLDM;
    }
    public void setXLDM(String xLDM) {
        XLDM = xLDM;
    }
    public String getXLMC() {
        return XLMC;
    }
    public void setXLMC(String xLMC) {
        XLMC = xLMC;
    }

    public Double getCPPJ() {
        return CPPJ;
    }
    public void setCPPJ(Double cPPJ) {
    	CPPJ = cPPJ;
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
