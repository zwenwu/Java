package com.hqu.domain;

import java.sql.Timestamp;

/**
 * @author WangWeiWei
 * @email 1345352429@qq.com
 * @time 2016年9月26日 上午9:55:06
 */
public class Schedule {
	/**
	 * 班次代码
	 */
	private String BCDM;
	/**
	 * 线路代码
	 */
	private String XLDM;
	/**
	 * 线路名称
	 */
	private String XLMC;
	/**
	 * 车牌号
	 */
	private String CPH;
	/**
	 * 司机姓名
	 */
	private String SJXM;
	/**
	 * 司机用户账号
	 */
	private String SJYHZH;
	/**
	 * 发车时间
	 */
	private Timestamp FCSJ;
	/**
	 * 全程票价
	 */
	private Double QCPJ;
	/**
	 * 月票价格
	 */
	private Double YPJG;
	/**
	 * 周票价格
	 */
	private Double ZPJG;
	/**
	 * 总票数
	 */
	private int ZPS;
	/**
	 * 剩余票数
	 */
	private int SYPS;
	/**
	 * 已退票数
	 */
	private int YTPS;
	/**
	 * 班次状态代码
	 */
	private String BCZTDM;
	/**
	 * 班次状态名称
	 */
	private String BCZTMC;
	/**
	 * 班次类型代码
	 */
	private String BCLXDM;
	/**
	 * 班次类型名称
	 */
	private String BCLXMC;
	/**
	 * 备注
	 */
	private String NOTE;
	/**
	 * 移动电话
	 */
	private String YDDH;
	/**
	 * 能否停用
	 */
	private int BTY;

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

	public String getXLMC() {
		return XLMC;
	}

	public void setXLMC(String xLMC) {
		XLMC = xLMC;
	}

	public String getCPH() {
		return CPH;
	}

	public void setCPH(String cPH) {
		CPH = cPH;
	}

	public String getSJXM() {
		return SJXM;
	}

	public void setSJXM(String sJXM) {
		SJXM = sJXM;
	}

	public String getSJYHZH() {
		return SJYHZH;
	}

	public void setSJYHZH(String sJYHZH) {
		SJYHZH = sJYHZH;
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

	public int getZPS() {
		return ZPS;
	}

	public void setZPS(int zPS) {
		ZPS = zPS;
	}

	public int getSYPS() {
		return SYPS;
	}

	public void setSYPS(int sYPS) {
		SYPS = sYPS;
	}

	public int getYTPS() {
		return YTPS;
	}

	public void setYTPS(int yTPS) {
		YTPS = yTPS;
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

	public int getBTY() {
		return BTY;
	}

	public void setBTY(int bTY) {
		BTY = bTY;
	}

	public String getNOTE() {
		return NOTE;
	}

	public void setNOTE(String nOTE) {
		NOTE = nOTE;
	}

	public String getYDDH() {
		return YDDH;
	}

	public void setYDDH(String yDDH) {
		YDDH = yDDH;
	}

}
