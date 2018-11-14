package pe.com.portability.bean;

import java.io.Serializable;

import pe.com.nextel.util.GenericObject;


public class PortabilityContactBean extends GenericObject implements Serializable{

  private static final long serialVersionUID = 1L;

  public PortabilityContactBean(){}
  
  private String npreceptorname;
  private long npreceptorid;
  private String npconctactname;
  private String npcontactemail;
  private String npcontactphone;
  private String npcontactfax;
  
  public void setNpreceptorname(String npreceptorname){
    this.npreceptorname = npreceptorname;
  }
  public String getNpreceptorname(){
    return npreceptorname;
  }
  
  public void setNpreceptorid(long npreceptorid){
    this.npreceptorid = npreceptorid;
  }
  public long getNpreceptorid(){
    return npreceptorid;
  }
  
  public void setNpconctactname(String npconctactname){
    this.npconctactname = npconctactname;
  }
  public String getNpconctactname(){
    return npconctactname;
  }
  
  public void setNpcontactemail(String npcontactemail){
    this.npcontactemail = npcontactemail;
  }
  public String getNpcontactemail(){
    return npcontactemail;
  }
  
  public void setNpcontactphone(String npcontactphone){
    this.npcontactphone = npcontactphone;
  }
  public String getNpcontactphone(){
    return npcontactphone;
  }
  
  public void setNpcontactfax(String npcontactfax){
    this.npcontactfax = npcontactfax;
  }
  public String getNpcontactfax(){
    return npcontactfax;
  }
  
}