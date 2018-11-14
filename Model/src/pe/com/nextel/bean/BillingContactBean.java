package pe.com.nextel.bean;

import java.io.Serializable;

public class BillingContactBean implements Serializable
{   
  private static final long serialVersionUID = 1L;
    private String npTypeContact;
    private String npTitle;
    private String npfname;
    private String nplname;
    private String npjobtitle;
    private String npphonearea;
    private String npphone;
    private String npaddress1;
    private String npaddress2;
    private String npcity;
    private String npstate;
    private String npdepartment;
    private String npzipcode;
    private String npcreatedby;
    private long npbillaccnewid;
    
  public BillingContactBean()
  {
  }

  public void setNpTypeContact(String npTypeContact)
  {
    this.npTypeContact = npTypeContact;
  }


  public String getNpTypeContact()
  {
    return npTypeContact;
  }


  public void setNpTitle(String npTitle)
  {
    this.npTitle = npTitle;
  }


  public String getNpTitle()
  {
    return npTitle;
  }


  public void setNpfname(String npfname)
  {
    this.npfname = npfname;
  }


  public String getNpfname()
  {
    return npfname;
  }


  public void setNplname(String nplname)
  {
    this.nplname = nplname;
  }


  public String getNplname()
  {
    return nplname;
  }


  public void setNpjobtitle(String npjobtitle)
  {
    this.npjobtitle = npjobtitle;
  }


  public String getNpjobtitle()
  {
    return npjobtitle;
  }


  public void setNpphonearea(String npphonearea)
  {
    this.npphonearea = npphonearea;
  }


  public String getNpphonearea()
  {
    return npphonearea;
  }


  public void setNpphone(String npphone)
  {
    this.npphone = npphone;
  }


  public String getNpphone()
  {
    return npphone;
  }


  public void setNpaddress1(String npaddress1)
  {
    this.npaddress1 = npaddress1;
  }


  public String getNpaddress1()
  {
    return npaddress1;
  }


  public void setNpaddress2(String npaddress2)
  {
    this.npaddress2 = npaddress2;
  }


  public String getNpaddress2()
  {
    return npaddress2;
  }


  public void setNpcity(String npcity)
  {
    this.npcity = npcity;
  }


  public String getNpcity()
  {
    return npcity;
  }


  public void setNpstate(String npstate)
  {
    this.npstate = npstate;
  }


  public String getNpstate()
  {
    return npstate;
  }


  public void setNpdepartment(String npdepartment)
  {
    this.npdepartment = npdepartment;
  }


  public String getNpdepartment()
  {
    return npdepartment;
  }


  public void setNpzipcode(String npzipcode)
  {
    this.npzipcode = npzipcode;
  }


  public String getNpzipcode()
  {
    return npzipcode;
  }


  public void setNpcreatedby(String npcreatedby)
  {
    this.npcreatedby = npcreatedby;
  }


  public String getNpcreatedby()
  {
    return npcreatedby;
  }

  public void setNpbillaccnewid(long npbillaccnewid)
  {
    this.npbillaccnewid = npbillaccnewid;
  }


  public long getNpbillaccnewid()
  {
    return npbillaccnewid;
  }
}