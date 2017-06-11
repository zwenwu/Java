package com.hqu.domain;
import java.sql.Timestamp;
import java.text.DateFormat;

public class Card {

	private String KPDM;
	private String XLDM;
	private String KPLXDM;
	private Timestamp KSSJ;
	private Timestamp JSSJ;
	private String KPJG;
	private String KPZTDM;
	private String KPLXMC;
	private String KPZTMC;
	private String XLMC;
	private String NOTE;
	
	public String getKPDM() {return KPDM;}
	public void setkPDM(String kPDM) {KPDM = kPDM;}

	public String getXLDM() {return XLDM;}
	public void setxLDM(String xLDM) {XLDM = xLDM;}

	public String getXLMC() {return XLMC;}
	public void setxLMC(String xLMC) {XLMC = xLMC;}
	
	public String getKPLXDM() {return KPLXDM;}
	public void setkPLXDM(String kPLXDM) {KPLXDM = kPLXDM;}

	public Timestamp getKSSJ() {return KSSJ;}
	public void setkSSJ(Timestamp kSSJ) {KSSJ = kSSJ;}
	
	public Timestamp getJSSJ() {return JSSJ;}
	public void setjSSJ(Timestamp jSSJ) {JSSJ = jSSJ;}
	
	public String getKPJG() {return KPJG;}
	public void setkPJG(String kPJG) {KPJG = kPJG;}
	
	public String getKPZTDM() {return KPZTDM;}
	public void setkPZTDM(String kPZTDM) {KPZTDM = kPZTDM;}
	
	public String getKPLXMC() {return KPLXMC;}
	public void setkPLXMC(String kPLXMC) {KPLXMC = kPLXMC;}
	
	public String getKPZTMC() {return KPZTMC;}
	public void setkPZTMC(String kPZTMC) {KPZTMC = kPZTMC;}
}
