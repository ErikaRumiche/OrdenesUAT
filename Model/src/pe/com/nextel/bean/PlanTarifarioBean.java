package pe.com.nextel.bean;

import java.io.Serializable;

import java.sql.Date;

public class PlanTarifarioBean implements Serializable {
  private static final long serialVersionUID = 1L;

	private long npplantarifarioid;
	private double npplancode;
	private String npdescripcion;
	private String npcreatedby;
	private Date npdatecreated;
	private String timestamp;
	private char nptipo1;
	private String nptipo2;
	private char npplanescomercial;
	private int npstatus;
	private char nppooling;
	private String nppdsubscriptionfee;
	private String npemployeeavailable;
	private String nphowtodisplay;
	private double npgrupoplanid;
	private int nprequestapprove;
	private int npsolutionid;
  private int npspecificationid;
  private long npprodlineval; //EZUBIAURR 14/03/11

	//PRY-1049| INICIO: AMENDEZ
	private int flagCoverage;
	private long npproductlineid;
	private long npproductid;
	private long npsolutionidbafi;
    private String npmodality;
	//PRY-1049 | FIN: AMENDEZ


public String getNpcreatedby() {
	return this.npcreatedby;
}

public Date getNpdatecreated() {
	return this.npdatecreated;
}

public String getNpdescripcion() {
	return this.npdescripcion;
}

public String getNpemployeeavailable() {
	return this.npemployeeavailable;
}

public double getNpgrupoplanid() {
	return this.npgrupoplanid;
}

public String getNphowtodisplay() {
	return this.nphowtodisplay;
}

public String getNppdsubscriptionfee() {
	return this.nppdsubscriptionfee;
}

public double getNpplancode() {
	return this.npplancode;
}

public char getNpplanescomercial() {
	return this.npplanescomercial;
}

public long getNpplantarifarioid() {
	return this.npplantarifarioid;
}

public char getNppooling() {
	return this.nppooling;
}

public int getNprequestapprove() {
	return this.nprequestapprove;
}

public int getNpsolutionid() {
	return this.npsolutionid;
}

public int getNpstatus() {
	return this.npstatus;
}

public char getNptipo1() {
	return this.nptipo1;
}

public String getNptipo2() {
	return this.nptipo2;
}

public String getTimestamp() {
	return this.timestamp;
}

public void setNpcreatedby(String npcreatedby) {
	this.npcreatedby = npcreatedby;
}

public void setNpdatecreated(Date npdatecreated) {
	this.npdatecreated = npdatecreated;
}

public void setNpdescripcion(String npdescripcion) {
	this.npdescripcion = npdescripcion;
}

public void setNpemployeeavailable(String npemployeeavailable) {
	this.npemployeeavailable = npemployeeavailable;
}

public void setNpgrupoplanid(double npgrupoplanid) {
	this.npgrupoplanid = npgrupoplanid;
}

public void setNphowtodisplay(String nphowtodisplay) {
	this.nphowtodisplay = nphowtodisplay;
}

public void setNppdsubscriptionfee(String nppdsubscriptionfee) {
	this.nppdsubscriptionfee = nppdsubscriptionfee;
}

public void setNpplancode(double npplancode) {
	this.npplancode = npplancode;
}

public void setNpplanescomercial(char npplanescomercial) {
	this.npplanescomercial = npplanescomercial;
}

public void setNpplantarifarioid(long npplantarifarioid) {
	this.npplantarifarioid = npplantarifarioid;
}

public void setNppooling(char nppooling) {
	this.nppooling = nppooling;
}

public void setNprequestapprove(int nprequestapprove) {
	this.nprequestapprove = nprequestapprove;
}

public void setNpsolutionid(int npsolutionid) {
	this.npsolutionid = npsolutionid;
}

public void setNpstatus(int npstatus) {
	this.npstatus = npstatus;
}

public void setNptipo1(char nptipo1) {
	this.nptipo1 = nptipo1;
}

public void setNptipo2(String nptipo2) {
	this.nptipo2 = nptipo2;
}

public void setTimestamp(String timestamp) {
	this.timestamp = timestamp;
}


  public void setNpspecificationid(int npspecificationid)
  {
    this.npspecificationid = npspecificationid;
  }


  public int getNpspecificationid()
  {
    return npspecificationid;
  }


  public void setNpprodlineval(long npprodlineval)
  {
    this.npprodlineval = npprodlineval;
  }


  public long getNpprodlineval()
  {
    return npprodlineval;
  }

	//PRY-1049| INICIO: AMENDEZ
	public int getFlagCoverage() {
		return flagCoverage;
	}

	public void setFlagCoverage(int flagCoverage) {
		this.flagCoverage = flagCoverage;
	}

	public long getNpproductlineid() {
		return npproductlineid;
	}

	public void setNpproductlineid(long npproductlineid) {
		this.npproductlineid = npproductlineid;
	}

	public long getNpsolutionidbafi() {
		return npsolutionidbafi;
	}

	public void setNpsolutionidbafi(long npsolutionidbafi) {
		this.npsolutionidbafi = npsolutionidbafi;
	}

    public String getNpmodality() {
        return npmodality;
    }

    public void setNpmodality(String npmodality) {
        this.npmodality = npmodality;
    }

	public long getNpproductid() {
		return npproductid;
	}

	public void setNpproductid(long npproductid) {
		this.npproductid = npproductid;
	}

	@Override
	public String toString() {
		return "PlanTarifarioBean{" +
				"npplantarifarioid=" + npplantarifarioid +
				", npplancode=" + npplancode +
				", npdescripcion='" + npdescripcion + '\'' +
				", npcreatedby='" + npcreatedby + '\'' +
				", npdatecreated=" + npdatecreated +
				", timestamp='" + timestamp + '\'' +
				", nptipo1=" + nptipo1 +
				", nptipo2='" + nptipo2 + '\'' +
				", npplanescomercial=" + npplanescomercial +
				", npstatus=" + npstatus +
				", nppooling=" + nppooling +
				", nppdsubscriptionfee='" + nppdsubscriptionfee + '\'' +
				", npemployeeavailable='" + npemployeeavailable + '\'' +
				", nphowtodisplay='" + nphowtodisplay + '\'' +
				", npgrupoplanid=" + npgrupoplanid +
				", nprequestapprove=" + nprequestapprove +
				", npsolutionid=" + npsolutionid +
				", npspecificationid=" + npspecificationid +
				", npprodlineval=" + npprodlineval +
				", flagCoverage=" + flagCoverage +
				", npproductlineid=" + npproductlineid +
				", npproductid=" + npproductid +
				", npsolutionidbafi=" + npsolutionidbafi +
				", npmodality='" + npmodality + '\'' +
				'}';
	}
	//PRY-1049| FIN: AMENDEZ
}
