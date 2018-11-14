package pe.com.nextel.bean;

import java.io.Serializable;

import java.sql.Date;
import java.sql.Timestamp;


public class ContactObjectBean  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private long swjobtitleid;      
  private String swjobtitle;
  private long swcustomerid;
  private long swsiteid;
  private String swemailaddress;
  private String swfirstname;
  private String swlastname;
  private String swmiddlename;
  private String swcreatedby;
  private String swtitle;
  private String swofficephonearea;
  private String swofficephone;
  private String swfaxarea;
  private String swfax;
  private String swofficephoneext;
  private long swpersonid;
  private String npcreatedby;
  private String npmodifiedby;
  private Timestamp npcreateddate;
  private Date npmodifieddate;
  private String npcontacttype;
  private String npaction;
  private int npitemid;
  
  
  private String nptitlenew;  
  private String npfirstnamenew;
  private String nplastnamenew;
  private String npmiddlenamenew;
  private String npjobtitlenew;
  private String npemailnew;
  
  
  public ContactObjectBean()
  {
  }


  public void setSwjobtitleid(long swjobtitleid)
  {
    this.swjobtitleid = swjobtitleid;
  }


  public long getSwjobtitleid()
  {
    return swjobtitleid;
  }


  public void setSwjobtitle(String swjobtitle)
  {
    this.swjobtitle = swjobtitle;
  }


  public String getSwjobtitle()
  {
    return swjobtitle;
  }


  public void setSwcustomerid(long swcustomerid)
  {
    this.swcustomerid = swcustomerid;
  }


  public long getSwcustomerid()
  {
    return swcustomerid;
  }


  public void setSwsiteid(long swsiteid)
  {
    this.swsiteid = swsiteid;
  }


  public long getSwsiteid()
  {
    return swsiteid;
  }


  public void setSwemailaddress(String swemailaddress)
  {
    this.swemailaddress = swemailaddress;
  }


  public String getSwemailaddress()
  {
    return swemailaddress;
  }


  public void setSwfirstname(String swfirstname)
  {
    this.swfirstname = swfirstname;
  }


  public String getSwfirstname()
  {
    return swfirstname;
  }


  public void setSwlastname(String swlastname)
  {
    this.swlastname = swlastname;
  }


  public String getSwlastname()
  {
    return swlastname;
  }


  public void setSwmiddlename(String swmiddlename)
  {
    this.swmiddlename = swmiddlename;
  }


  public String getSwmiddlename()
  {
    return swmiddlename;
  }


  public void setSwcreatedby(String swcreatedby)
  {
    this.swcreatedby = swcreatedby;
  }


  public String getSwcreatedby()
  {
    return swcreatedby;
  }


  public void setSwtitle(String swtitle)
  {
    this.swtitle = swtitle;
  }


  public String getSwtitle()
  {
    return swtitle;
  }


  public void setSwofficephonearea(String swofficephonearea)
  {
    this.swofficephonearea = swofficephonearea;
  }


  public String getSwofficephonearea()
  {
    return swofficephonearea;
  }


  public void setSwofficephone(String swofficephone)
  {
    this.swofficephone = swofficephone;
  }


  public String getSwofficephone()
  {
    return swofficephone;
  }


  public void setSwfaxarea(String swfaxarea)
  {
    this.swfaxarea = swfaxarea;
  }


  public String getSwfaxarea()
  {
    return swfaxarea;
  }


  public void setSwfax(String swfax)
  {
    this.swfax = swfax;
  }


  public String getSwfax()
  {
    return swfax;
  }


  public void setSwofficephoneext(String swofficephoneext)
  {
    this.swofficephoneext = swofficephoneext;
  }


  public String getSwofficephoneext()
  {
    return swofficephoneext;
  }


  public void setSwpersonid(long swpersonid)
  {
    this.swpersonid = swpersonid;
  }


  public long getSwpersonid()
  {
    return swpersonid;
  }


  public void setNpcreatedby(String npcreatedby)
  {
    this.npcreatedby = npcreatedby;
  }


  public String getNpcreatedby()
  {
    return npcreatedby;
  }


  public void setNpmodifiedby(String npmodifiedby)
  {
    this.npmodifiedby = npmodifiedby;
  }


  public String getNpmodifiedby()
  {
    return npmodifiedby;
  }


  public void setNpcreateddate(Timestamp npcreateddate)
  {
    this.npcreateddate = npcreateddate;
  }


  public Timestamp getNpcreateddate()
  {
    return npcreateddate;
  }


  public void setNpmodifieddate(Date npmodifieddate)
  {
    this.npmodifieddate = npmodifieddate;
  }


  public Date getNpmodifieddate()
  {
    return npmodifieddate;
  }


  public void setNpcontacttype(String npcontacttype)
  {
    this.npcontacttype = npcontacttype;
  }


  public String getNpcontacttype()
  {
    return npcontacttype;
  }


  public void setNpaction(String npaction)
  {
    this.npaction = npaction;
  }


  public String getNpaction()
  {
    return npaction;
  }


  public void setNpitemid(int npitemid)
  {
    this.npitemid = npitemid;
  }


  public int getNpitemid()
  {
    return npitemid;
  }


  public void setNptitlenew(String nptitlenew)
  {
    this.nptitlenew = nptitlenew;
  }


  public String getNptitlenew()
  {
    return nptitlenew;
  }


  public void setNpfirstnamenew(String npfirstnamenew)
  {
    this.npfirstnamenew = npfirstnamenew;
  }


  public String getNpfirstnamenew()
  {
    return npfirstnamenew;
  }


  public void setNplastnamenew(String nplastnamenew)
  {
    this.nplastnamenew = nplastnamenew;
  }


  public String getNplastnamenew()
  {
    return nplastnamenew;
  }


  public void setNpmiddlenamenew(String npmiddlenamenew)
  {
    this.npmiddlenamenew = npmiddlenamenew;
  }


  public String getNpmiddlenamenew()
  {
    return npmiddlenamenew;
  }


  public void setNpjobtitlenew(String npjobtitlenew)
  {
    this.npjobtitlenew = npjobtitlenew;
  }


  public String getNpjobtitlenew()
  {
    return npjobtitlenew;
  }


  public void setNpemailnew(String npemailnew)
  {
    this.npemailnew = npemailnew;
  }


  public String getNpemailnew()
  {
    return npemailnew;
  }


}