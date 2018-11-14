package pe.com.nextel.bean;

import java.io.Serializable;

import java.sql.Date;


public class ItemDeviceBean implements Serializable {

  private static final long serialVersionUID = 1L;
	private long npitemdeviceid;
	private long npitemid;
	private long nporderid;
	private String npserialnumber;
	private String npimeinumber;
	private String npsimnumber;
	private String npcheckimei;
	private String npbadimei;
	private String npinventorycode;
	private double npcontractnumber;
	private String npcontractstatus;
	private Date npcreateddate;
	private String npcreatedby;
  
	private int    npcantidad;
	private String npplanname;
	private long npproductid;
	private String npproductname;
  
	private String npmodality;
	private String npwarrant;
  private String strImeiChange;
  private String strSimChange;
  /*Agrego Nuevos Parametros al Bean*/
  private String npphone;
  private long   nprequestid;
  private String nperrorautom;
  
  
	public String getNpbadimei() {
		return this.npbadimei;
	}

	public String getNpcheckimei() {
		return this.npcheckimei;
	}

	public double getNpcontractnumber() {
		return this.npcontractnumber;
	}
	
public String getNpcontractstatus() {
	return this.npcontractstatus;
}

public String getNpcreatedby() {
	return this.npcreatedby;
}

public Date getNpcreateddate() {
	return this.npcreateddate;
}

public String getNpimeinumber() {
	return this.npimeinumber;
}

public String getNpinventorycode() {
	return this.npinventorycode;
}

public long getNpitemdeviceid() {
	return this.npitemdeviceid;
}

public long getNpitemid() {
	return this.npitemid;
}

public long getNporderid() {
	return this.nporderid;
}

public String getNpserialnumber() {
	return this.npserialnumber;
}

public String getNpsimnumber() {
	return this.npsimnumber;
}

public void setNpbadimei(String npbadimei) {
	this.npbadimei = npbadimei;
}

public void setNpcheckimei(String npcheckimei) {
	this.npcheckimei = npcheckimei;
}

public void setNpcontractnumber(double npcontractnumber) {
	this.npcontractnumber = npcontractnumber;
}

public void setNpcontractstatus(String npcontractstatus) {
	this.npcontractstatus = npcontractstatus;
}

public void setNpcreatedby(String npcreatedby) {
	this.npcreatedby = npcreatedby;
}

public void setNpcreateddate(Date npcreateddate) {
	this.npcreateddate = npcreateddate;
}

public void setNpimeinumber(String npimeinumber) {
	this.npimeinumber = npimeinumber;
}

public void setNpinventorycode(String npinventorycode) {
	this.npinventorycode = npinventorycode;
}

public void setNpitemdeviceid(long npitemdeviceid) {
	this.npitemdeviceid = npitemdeviceid;
}

public void setNpitemid(long npitemid) {
	this.npitemid = npitemid;
}

public void setNporderid(long nporderid) {
	this.nporderid = nporderid;
}

public void setNpserialnumber(String npserialnumber) {
	this.npserialnumber = npserialnumber;
}

public void setNpsimnumber(String npsimnumber) {
	this.npsimnumber = npsimnumber;
}


  public void setNpcantidad(int npcantidad)
  {
    this.npcantidad = npcantidad;
  }


  public int getNpcantidad()
  {
    return npcantidad;
  }


  public void setNpplanname(String npplanname)
  {
    this.npplanname = npplanname;
  }


  public String getNpplanname()
  {
    return npplanname;
  }


  public void setNpproductid(long npproductid)
  {
    this.npproductid = npproductid;
  }


  public long getNpproductid()
  {
    return npproductid;
  }


  public void setNpproductname(String npproductname)
  {
    this.npproductname = npproductname;
  }


  public String getNpproductname()
  {
    return npproductname;
  }


  public void setNpmodality(String npmodality)
  {
    this.npmodality = npmodality;
  }


  public String getNpmodality()
  {
    return npmodality;
  }


	public void setNpwarrant(String npwarrant)
	{
		this.npwarrant = npwarrant;
	}


	public String getNpwarrant()
	{
		return npwarrant;
	}


  public void setStrImeiChange(String strImeiChange)
  {
    this.strImeiChange = strImeiChange;
  }


  public String getStrImeiChange()
  {
    return strImeiChange;
  }


  public void setStrSimChange(String strSimChange)
  {
    this.strSimChange = strSimChange;
  }


  public String getStrSimChange()
  {
    return strSimChange;
  }


  public void setNpphone(String npphone)
  {
    this.npphone = npphone;
  }


  public String getNpphone()
  {
    return npphone;
  }


  public void setNprequestid(long nprequestid)
  {
    this.nprequestid = nprequestid;
  }


  public long getNprequestid()
  {
    return nprequestid;
  }


  public void setNperrorautom(String nperrorautom)
  {
    this.nperrorautom = nperrorautom;
  }


  public String getNperrorautom()
  {
    return nperrorautom;
  }

}
