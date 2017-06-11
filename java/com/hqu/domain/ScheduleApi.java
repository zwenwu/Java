package com.hqu.domain;

import java.sql.Timestamp;

public class ScheduleApi {
	   /**
     *班次代码
     */
    private String BCDM;
    /**
     *线路代码
     */
    private String XLDM;
    /**
     *发车时间
     */
    private Timestamp FCSJ;
    /**
     *全程票价
     */
    private Double QCPJ;
    /**
     *剩余票数
     */
    private int SYPS;
    /**
     *月票价格
     */
    private Double YPJG;
    /**
     *周票价格
     */
    private Double ZPJG;
    /**
     *司机姓名
     */
    private String SJXM;
    /**
     *移动电话
     */
    private String YDDH;
    /**
     *移动电话
     */
    private String CPH;
    /**
     * 查询页数
     */
    private int Page;
    
    public int getPage() {
		return Page;
	}
	public void setPage(int page) {
		Page = page;
	}
	public String getBCDM() {
        return BCDM;
    }
    public void setBCDM(String bCDM) {
        BCDM = bCDM;
    }
    public String getXLDM() {
        return XLDM;
    }
    public void setXLDM(String xLDM) {
        XLDM = xLDM;
    }
    public Timestamp getFCSJ() {
        return FCSJ;
    }
    public void setFCSJ(Timestamp fCSJ) {
        FCSJ = fCSJ;
    }
    public Double getQCPJ() {
        return QCPJ;
    }
    public void setQCPJ(Double qCPJ) {
        QCPJ = qCPJ;
    }
    public int getSYPS() {
        return SYPS;
    }
    public void setSYPS(int sYPS) {
        SYPS = sYPS;
    }
    public Double getYPJG() {
        return YPJG;
    }
    public void setYPJG(Double yPJG) {
    	YPJG = yPJG;
    }
    public Double getZPJG() {
        return ZPJG;
    }
    public void setZPJG(Double zPJG) {
    	ZPJG = zPJG;
    }
    public String getSJXM() {
        return SJXM;
    }
    public void setSJXM(String sJXM) {
        SJXM = sJXM;
    }
    public String getYDDH() {
        return YDDH;
    }
    public void setYDDH(String yDDH) {
    	YDDH = yDDH;
    }
    public String getCPH() {
        return CPH;
    }
    public void setCPH(String cPH) {
    	CPH = cPH;
    }
}
