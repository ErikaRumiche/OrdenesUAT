package pe.com.nextel.bean;

import java.io.Serializable;

import java.sql.Date;


public class BaAssignmentBean implements Serializable{

  private static final long serialVersionUID = 1L;
  private long npbaassignmentid;
	private long nporderid;
	private long npdeviceid;
	private String npserviceid;
	private String npchargetype;
	private String billingAccountId;
	private String npbillaccnewid;
  private String npactivesiteid;
	private String npunknowsiteid;
	private Date npcreateddate;
	private String npcreatedby;

public String getBillingAccountId() {
	return this.billingAccountId;
}

public long getNpbaassignmentid() {
	return this.npbaassignmentid;
}

public String getNpbillaccnewid() {
	return this.npbillaccnewid;
}

public String getNpchargetype() {
	return this.npchargetype;
}

public String getNpcreatedby() {
	return this.npcreatedby;
}

public Date getNpcreateddate() {
	return this.npcreateddate;
}

public long getNpdeviceid() {
	return this.npdeviceid;
}

public long getNporderid() {
	return this.nporderid;
}

public String getNpserviceid() {
	return this.npserviceid;
}

public void setBillingAccountId(String billingAccountId) {
	this.billingAccountId = billingAccountId;
}

public void setNpbaassignmentid(long npbaassignmentid) {
	this.npbaassignmentid = npbaassignmentid;
}

public void setNpbillaccnewid(String npbillaccnewid) {
	this.npbillaccnewid = npbillaccnewid;
}

public void setNpchargetype(String npchargetype) {
	this.npchargetype = npchargetype;
}

public void setNpcreatedby(String npcreatedby) {
	this.npcreatedby = npcreatedby;
}

public void setNpcreateddate(Date npcreateddate) {
	this.npcreateddate = npcreateddate;
}

public void setNpdeviceid(long npdeviceid) {
	this.npdeviceid = npdeviceid;
}

public void setNporderid(long nporderid) {
	this.nporderid = nporderid;
}

public void setNpserviceid(String npserviceid) {
	this.npserviceid = npserviceid;
}


  public void setNpactivesiteid(String npactivesiteid)
  {
    this.npactivesiteid = npactivesiteid;
  }


  public String getNpactivesiteid()
  {
    return npactivesiteid;
  }


  public void setNpunknowsiteid(String npunknowsiteid)
  {
    this.npunknowsiteid = npunknowsiteid;
  }


  public String getNpunknowsiteid()
  {
    return npunknowsiteid;
  }

}
