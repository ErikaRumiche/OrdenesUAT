package pe.com.nextel.bean;

import java.io.Serializable;

import java.util.Date;


public class ServiciosBean implements Serializable{
  private static final long serialVersionUID = 1L;
	private long npservicioid;
  private long npsncode;
	private String npnomserv;
	private String npnomcorserv;
	private String npstatus;
	private int npordenamiento;
	private String npexcludingind;
	private String npexcludingflg;
	private String nprasprocess;
	private int nprequestapprove;
	private String npservicetype;
	private int npsolutionid;
	private String npvalue;
	private String nptype;
  private int npduration;
  private Date npsalesstartdate;
  private Date npsalesenddate;
  private String npmissingdays;
  private String npgroup; //johncmb


public String getNpexcludingflg() {
	return this.npexcludingflg;
}

public String getNpexcludingind() {
	return this.npexcludingind;
}

public String getNpnomcorserv() {
	return this.npnomcorserv;
}

public String getNpnomserv() {
	return this.npnomserv;
}

public int getNpordenamiento() {
	return this.npordenamiento;
}

public String getNprasprocess() {
	return this.nprasprocess;
}

public int getNprequestapprove() {
	return this.nprequestapprove;
}

public String getNpservicetype() {
	return this.npservicetype;
}

public long getNpservicioid() {
	return this.npservicioid;
}

public int getNpsolutionid() {
	return this.npsolutionid;
}

public String getNpstatus() {
	return this.npstatus;
}

public String getNptype() {
	return this.nptype;
}

public String getNpvalue() {
	return this.npvalue;
}

public void setNpexcludingflg(String npexcludingflg) {
	this.npexcludingflg = npexcludingflg;
}

public void setNpexcludingind(String npexcludingind) {
	this.npexcludingind = npexcludingind;
}

public void setNpnomcorserv(String npnomcorserv) {
	this.npnomcorserv = npnomcorserv;
}

public void setNpnomserv(String npnomserv) {
	this.npnomserv = npnomserv;
}

public void setNpordenamiento(int npordenamiento) {
	this.npordenamiento = npordenamiento;
}

public void setNprasprocess(String nprasprocess) {
	this.nprasprocess = nprasprocess;
}

public void setNprequestapprove(int nprequestapprove) {
	this.nprequestapprove = nprequestapprove;
}

public void setNpservicetype(String npservicetype) {
	this.npservicetype = npservicetype;
}

public void setNpservicioid(long npservicioid) {
	this.npservicioid = npservicioid;
}

public void setNpsolutionid(int npsolutionid) {
	this.npsolutionid = npsolutionid;
}

public void setNpstatus(String npstatus) {
	this.npstatus = npstatus;
}

public void setNptype(String nptype) {
	this.nptype = nptype;
}

public void setNpvalue(String npvalue) {
	this.npvalue = npvalue;
}


  public void setNpsncode(long npsncode)
  {
    this.npsncode = npsncode;
  }


  public long getNpsncode()
  {
    return npsncode;
  }

//johncmb inicio
  public void setNpgroup(String npgroup)
  {
    this.npgroup = npgroup;
  }

  public String getNpgroup()
  {
    return npgroup;
  }
//johncmb fin

  public void setNpduration(int npduration)
  {
    this.npduration = npduration;
  }


  public int getNpduration()
  {
    return npduration;
  }

  public void setNpsalesstartdate(Date npsalesstartdate)
  {
    this.npsalesstartdate = npsalesstartdate;
  }


  public Date getNpsalesstartdate()
  {
    return npsalesstartdate;
  }


  public void setNpmissingdays(String npmissingdays)
  {
    this.npmissingdays = npmissingdays;
  }


  public String getNpmissingdays()
  {
    return npmissingdays;
  }


  public void setNpsalesenddate(Date npsalesenddate)
  {
    this.npsalesenddate = npsalesenddate;
  }


  public Date getNpsalesenddate()
  {
    return npsalesenddate;
  }


}