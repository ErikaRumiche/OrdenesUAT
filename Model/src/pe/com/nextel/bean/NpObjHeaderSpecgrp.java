package pe.com.nextel.bean;

import java.io.Serializable;

import java.sql.Date;


public class NpObjHeaderSpecgrp implements Serializable{
  private static final long serialVersionUID = 1L;
	private double npobjheaderspecgrpid;
	private double npspecificationgrpid;
	private int npobjitemheaderid;
  private String npobjitemname;
	private String npdisplay;
	private String npcreatedby;
	private Date npcreateddate;
	private String npmodifiedby;
	private Date npmodifieddate;
  private String nphtmlname;


public String getNpcreatedby() {
	return this.npcreatedby;
}

public Date getNpcreateddate() {
	return this.npcreateddate;
}

public String getNpdisplay() {
	return this.npdisplay;
}

public String getNpmodifiedby() {
	return this.npmodifiedby;
}

public Date getNpmodifieddate() {
	return this.npmodifieddate;
}

public double getNpobjheaderspecgrpid() {
	return this.npobjheaderspecgrpid;
}

public int getNpobjitemheaderid() {
	return this.npobjitemheaderid;
}

public double getNpspecificationgrpid() {
	return this.npspecificationgrpid;
}

public void setNpcreatedby(String npcreatedby) {
	this.npcreatedby = npcreatedby;
}

public void setNpcreateddate(Date npcreateddate) {
	this.npcreateddate = npcreateddate;
}

public void setNpdisplay(String npdisplay) {
	this.npdisplay = npdisplay;
}

public void setNpmodifiedby(String npmodifiedby) {
	this.npmodifiedby = npmodifiedby;
}

public void setNpmodifieddate(Date npmodifieddate) {
	this.npmodifieddate = npmodifieddate;
}

public void setNpobjheaderspecgrpid(double npobjheaderspecgrpid) {
	this.npobjheaderspecgrpid = npobjheaderspecgrpid;
}

public void setNpobjitemheaderid(int npobjitemheaderid) {
	this.npobjitemheaderid = npobjitemheaderid;
}

public void setNpspecificationgrpid(double npspecificationgrpid) {
	this.npspecificationgrpid = npspecificationgrpid;
}


  public void setNpobjitemname(String npobjitemname)
  {
    this.npobjitemname = npobjitemname;
  }


  public String getNpobjitemname()
  {
    return npobjitemname;
  }


  public void setNphtmlname(String nphtmlname)
  {
    this.nphtmlname = nphtmlname;
  }


  public String getNphtmlname()
  {
    return nphtmlname;
  }

}
