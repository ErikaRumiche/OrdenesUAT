package pe.com.nextel.bean;

import java.io.Serializable;

import java.sql.Date;


public class ItemErrorBean implements Serializable{
  private static final long serialVersionUID = 1L;
    private long nporderid;
    private long npitemid;
    private long npitemdeviceid;
    private long npcoid;
    private int npphone;
    private String npaction;
    private String npdescription;
    private String npcreatedby;
    private Date   npcreateddate;


  public void setNporderid(long nporderid)
  {
    this.nporderid = nporderid;
  }


  public long getNporderid()
  {
    return nporderid;
  }


  public void setNpitemid(long npitemid)
  {
    this.npitemid = npitemid;
  }


  public long getNpitemid()
  {
    return npitemid;
  }


  public void setNpitemdeviceid(long npitemdeviceid)
  {
    this.npitemdeviceid = npitemdeviceid;
  }


  public long getNpitemdeviceid()
  {
    return npitemdeviceid;
  }


  public void setNpcoid(long npcoid)
  {
    this.npcoid = npcoid;
  }


  public long getNpcoid()
  {
    return npcoid;
  }


  public void setNpphone(int npphone)
  {
    this.npphone = npphone;
  }


  public int getNpphone()
  {
    return npphone;
  }


  public void setNpaction(String npaction)
  {
    this.npaction = npaction;
  }


  public String getNpaction()
  {
    return npaction;
  }


  public void setNpdescription(String npdescription)
  {
    this.npdescription = npdescription;
  }


  public String getNpdescription()
  {
    return npdescription;
  }


  public void setNpcreatedby(String npcreatedby)
  {
    this.npcreatedby = npcreatedby;
  }


  public String getNpcreatedby()
  {
    return npcreatedby;
  }


  public void setNpcreateddate(Date npcreateddate)
  {
    this.npcreateddate = npcreateddate;
  }


  public Date getNpcreateddate()
  {
    return npcreateddate;
  } 
  
  
}