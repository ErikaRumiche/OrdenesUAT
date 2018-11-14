package pe.com.nextel.bean;

import java.io.Serializable;

import java.util.Date;


public class TemplateAdendumBean implements Serializable
{
  private static final long serialVersionUID = 1L;
    private int nptemplateid;
    private String nptemplatedesc;
    private int npaddendumterm;
    private Date npcreateddate;
    private String nptemplatedefa;
    private String npaddendtype;
    private String npbenefittype;
    private String type_addend;
    private String type_benefit;
    
    
  public TemplateAdendumBean()
  {

    nptemplateid = 0;
    nptemplatedesc = "";
    npaddendumterm = 0;
    npcreateddate = null;
    nptemplatedefa = "";
    npaddendtype = "";
    npbenefittype = "";
    //type_addend = "";
    //type_benefit = "";    
  }


  public void setNptemplateid(int nptemplateid)
  {
    this.nptemplateid = nptemplateid;
  }


  public int getNptemplateid()
  {
    return nptemplateid;
  }


  public void setNptemplatedesc(String nptemplatedesc)
  {
    this.nptemplatedesc = nptemplatedesc;
  }


  public String getNptemplatedesc()
  {
    return nptemplatedesc;
  }


  public void setNpaddendumterm(int npaddendumterm)
  {
    this.npaddendumterm = npaddendumterm;
  }


  public int getNpaddendumterm()
  {
    return npaddendumterm;
  }


  public void setNpcreateddate(Date npcreateddate)
  {
    this.npcreateddate = npcreateddate;
  }


  public Date getNpcreateddate()
  {
    return npcreateddate;
  }


  public void setNptemplatedefa(String nptemplatedefa)
  {
    this.nptemplatedefa = nptemplatedefa;
  }


  public String getNptemplatedefa()
  {
    return nptemplatedefa;
  }


  public void setNpaddendtype(String npaddendtype)
  {
    this.npaddendtype = npaddendtype;
  }


  public String getNpaddendtype()
  {
    return npaddendtype;
  }


  public void setNpbenefittype(String npbenefittype)
  {
    this.npbenefittype = npbenefittype;
  }


  public String getNpbenefittype()
  {
    return npbenefittype;
  }

/*
  public void setType_addend(String type_addend)
  {
    this.type_addend = type_addend;
  }


  public String getType_addend()
  {
    return type_addend;
  }


  public void setType_benefit(String type_benefit)
  {
    this.type_benefit = type_benefit;
  }


  public String getType_benefit()
  {
    return type_benefit;
  }*/
}