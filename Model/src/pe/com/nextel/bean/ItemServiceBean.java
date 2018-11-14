package pe.com.nextel.bean;

import java.io.Serializable;

public class ItemServiceBean implements Serializable{
  private static final long serialVersionUID = 1L;
	private long npitemserviceid;
	private long npitemid;
	private long npserviceid;
	private String npservicetype;
	private double npserviceprice;
	private String npservicefree;
	private String npaction;


public String getNpaction() {
	return this.npaction;
}

public long getNpitemid() {
	return this.npitemid;
}

public long getNpitemserviceid() {
	return this.npitemserviceid;
}

public String getNpservicefree() {
	return this.npservicefree;
}

public long getNpserviceid() {
	return this.npserviceid;
}

public double getNpserviceprice() {
	return this.npserviceprice;
}

public String getNpservicetype() {
	return this.npservicetype;
}

public void setNpaction(String npaction) {
	this.npaction = npaction;
}

public void setNpitemid(long npitemid) {
	this.npitemid = npitemid;
}

public void setNpitemserviceid(long npitemserviceid) {
	this.npitemserviceid = npitemserviceid;
}

public void setNpservicefree(String npservicefree) {
	this.npservicefree = npservicefree;
}

public void setNpserviceid(long npserviceid) {
	this.npserviceid = npserviceid;
}

public void setNpserviceprice(double npserviceprice) {
	this.npserviceprice = npserviceprice;
}

public void setNpservicetype(String npservicetype) {
	this.npservicetype = npservicetype;
}

}
