package pe.com.nextel.bean;

import java.io.Serializable;

import java.sql.Date;


public class NpObjItemHeader implements  Serializable{
  private static final long serialVersionUID = 1L;
	private double npobjitemheaderid;
	private String npobjitemname;
	private String npobjitemdesc;
	private String npdefaultvalue;
	private String npstatus;
	private String npcreatedby;
	private Date npcreateddate;
	private String npmodifiedby;
	private Date npmodifieddate;


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

public String getNpobjitemdesc() {
	return this.npobjitemdesc;
}

public double getNpobjitemheaderid() {
	return this.npobjitemheaderid;
}

public String getNpobjitemname() {
	return this.npobjitemname;
}

public String getNpstatus() {
	return this.npstatus;
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

public void setNpobjitemdesc(String npobjitemdesc) {
	this.npobjitemdesc = npobjitemdesc;
}

public void setNpobjitemheaderid(double npobjitemheaderid) {
	this.npobjitemheaderid = npobjitemheaderid;
}

public void setNpobjitemname(String npobjitemname) {
	this.npobjitemname = npobjitemname;
}

public void setNpstatus(String npstatus) {
	this.npstatus = npstatus;
}

}
