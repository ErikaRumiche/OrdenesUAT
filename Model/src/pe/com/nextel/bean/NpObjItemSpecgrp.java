package pe.com.nextel.bean;

import java.io.Serializable;

import java.sql.Date;


public class NpObjItemSpecgrp  implements Serializable{
  private static final long serialVersionUID = 1L;
  private int npobjitemheaderid;
	private double npobjspecgrpid;
	private double npobjitemid;
  private String npobjitemname;
  private String npobjitemcontroltype;
  private String npdefaultvalue;
  private String npsourceprogram;
  private String npnamehtmlheader;
  private String npnamehtmlitem;
	private double npspecificationgrpid;
	private String npdisplay;
	private String npobjreadonly;
	private String npcreatedby;
	private Date npcreateddate;
	private String npmodifiedby;
	private Date npmodifieddate;
  private String npdatatype;
  private String npvalidateflg;
  private int    nplength;
    

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

public double getNpobjitemid() {
	return this.npobjitemid;
}

public String getNpobjreadonly() {
	return this.npobjreadonly;
}

public double getNpobjspecgrpid() {
	return this.npobjspecgrpid;
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

public void setNpobjitemid(double npobjitemid) {
	this.npobjitemid = npobjitemid;
}

public void setNpobjreadonly(String npobjreadonly) {
	this.npobjreadonly = npobjreadonly;
}

public void setNpobjspecgrpid(double npobjspecgrpid) {
	this.npobjspecgrpid = npobjspecgrpid;
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


  public void setNpobjitemcontroltype(String npobjitemcontroltype)
  {
    this.npobjitemcontroltype = npobjitemcontroltype;
  }


  public String getNpobjitemcontroltype()
  {
    return npobjitemcontroltype;
  }


  public void setNpdefaultvalue(String npdefaultvalue)
  {
    this.npdefaultvalue = npdefaultvalue;
  }


  public String getNpdefaultvalue()
  {
    return npdefaultvalue;
  }


  public void setNpsourceprogram(String npsourceprogram)
  {
    this.npsourceprogram = npsourceprogram;
  }


  public String getNpsourceprogram()
  {
    return npsourceprogram;
  }


  public void setNpnamehtmlheader(String npnamehtmlheader)
  {
    this.npnamehtmlheader = npnamehtmlheader;
  }


  public String getNpnamehtmlheader()
  {
    return npnamehtmlheader;
  }


  public void setNpnamehtmlitem(String npnamehtmlitem)
  {
    this.npnamehtmlitem = npnamehtmlitem;
  }


  public String getNpnamehtmlitem()
  {
    return npnamehtmlitem;
  }


  public void setNpobjitemheaderid(int npobjitemheaderid)
  {
    this.npobjitemheaderid = npobjitemheaderid;
  }


  public int getNpobjitemheaderid()
  {
    return npobjitemheaderid;
  }


  public void setNpdatatype(String npdatatype)
  {
    this.npdatatype = npdatatype;
  }


  public String getNpdatatype()
  {
    return npdatatype;
  }


  public void setNpvalidateflg(String npvalidateflg)
  {
    this.npvalidateflg = npvalidateflg;
  }


  public String getNpvalidateflg()
  {
    return npvalidateflg;
  }


  public void setNplength(int nplength)
  {
    this.nplength = nplength;
  }


  public int getNplength()
  {
    return nplength;
  }

}
