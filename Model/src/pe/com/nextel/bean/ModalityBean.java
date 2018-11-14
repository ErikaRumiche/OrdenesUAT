package pe.com.nextel.bean;

import java.io.Serializable;

public class ModalityBean implements Serializable {
  private static final long serialVersionUID = 1L;
	private Long npspecificationid;
	private String npmodality;
	private String npstatus;


public String getNpmodality() {
	return this.npmodality;
}

public Long getNpspecificationid() {
	return this.npspecificationid;
}

public String getNpstatus() {
	return this.npstatus;
}

public void setNpmodality(String npmodality) {
	this.npmodality = npmodality;
}

public void setNpspecificationid(Long npspecificationid) {
	this.npspecificationid = npspecificationid;
}

public void setNpstatus(String npstatus) {
	this.npstatus = npstatus;
}

}
