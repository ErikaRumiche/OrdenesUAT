package pe.com.nextel.bean;

import java.io.Serializable;

import java.sql.Date;


public class NpObjItem  implements Serializable{
  private static final long serialVersionUID = 1L;
	private double npobjitemid;
	private String npobjitemname;
	private String npobjitemdesc;
	private String npcontroltype;
	private String npdefaultvalue;
	private String npsourceprogram;
	private double npobjecheaderid;
	private String npstatus;
	private String npcreatedby;
	private Date npcreateddate;
	private String npmodifiedby;
	private Date npmodifieddate;

public String getNpcontroltype() {
	return this.npcontroltype;
}

public String getNpcreatedby() {
	return this.npcreatedby;
}

public Date getNpcreateddate() {
	return this.npcreateddate;
}

public String getNpdefaultvalue() {
	return this.npdefaultvalue;
}

public String getNpmodifiedby() {
	return this.npmodifiedby;
}

public Date getNpmodifieddate() {
	return this.npmodifieddate;
}

public double getNpobjecheaderid() {
	return this.npobjecheaderid;
}

public String getNpobjitemdesc() {
	return this.npobjitemdesc;
}

public double getNpobjitemid() {
	return this.npobjitemid;
}

public String getNpobjitemname() {
	return this.npobjitemname;
}

public String getNpsourceprogram() {
	return this.npsourceprogram;
}

public String getNpstatus() {
	return this.npstatus;
}

public void setNpcontroltype(String npcontroltype) {
	this.npcontroltype = npcontroltype;
}

public void setNpcreatedby(String npcreatedby) {
	this.npcreatedby = npcreatedby;
}

public void setNpcreateddate(Date npcreateddate) {
	this.npcreateddate = npcreateddate;
}

public void setNpdefaultvalue(String npdefaultvalue) {
	this.npdefaultvalue = npdefaultvalue;
}

public void setNpmodifiedby(String npmodifiedby) {
	this.npmodifiedby = npmodifiedby;
}

public void setNpmodifieddate(Date npmodifieddate) {
	this.npmodifieddate = npmodifieddate;
}

public void setNpobjecheaderid(double npobjecheaderid) {
	this.npobjecheaderid = npobjecheaderid;
}

public void setNpobjitemdesc(String npobjitemdesc) {
	this.npobjitemdesc = npobjitemdesc;
}

public void setNpobjitemid(double npobjitemid) {
	this.npobjitemid = npobjitemid;
}

public void setNpobjitemname(String npobjitemname) {
	this.npobjitemname = npobjitemname;
}

public void setNpsourceprogram(String npsourceprogram) {
	this.npsourceprogram = npsourceprogram;
}

public void setNpstatus(String npstatus) {
	this.npstatus = npstatus;
}

}
