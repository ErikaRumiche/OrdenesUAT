package pe.com.nextel.bean;

import java.io.Serializable;

import java.sql.Date;


public class NpSpecgrpXSpec  implements Serializable{
  private static final long serialVersionUID = 1L;
	private double npspecgrpxspecid;
	private double npspecificationgrpid;
	private double npspecificationid;
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

public String getNpmodifiedby() {
	return this.npmodifiedby;
}

public Date getNpmodifieddate() {
	return this.npmodifieddate;
}

public double getNpspecgrpxspecid() {
	return this.npspecgrpxspecid;
}

public double getNpspecificationgrpid() {
	return this.npspecificationgrpid;
}

public double getNpspecificationid() {
	return this.npspecificationid;
}

public void setNpcreatedby(String npcreatedby) {
	this.npcreatedby = npcreatedby;
}

public void setNpcreateddate(Date npcreateddate) {
	this.npcreateddate = npcreateddate;
}

public void setNpmodifiedby(String npmodifiedby) {
	this.npmodifiedby = npmodifiedby;
}

public void setNpmodifieddate(Date npmodifieddate) {
	this.npmodifieddate = npmodifieddate;
}

public void setNpspecgrpxspecid(double npspecgrpxspecid) {
	this.npspecgrpxspecid = npspecgrpxspecid;
}

public void setNpspecificationgrpid(double npspecificationgrpid) {
	this.npspecificationgrpid = npspecificationgrpid;
}

public void setNpspecificationid(double npspecificationid) {
	this.npspecificationid = npspecificationid;
}

}
