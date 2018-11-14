package pe.com.nextel.bean;

import java.io.Serializable;

import pe.com.nextel.util.GenericObject;


public class ProposedBean extends GenericObject  implements Serializable
{
  private static final long serialVersionUID = 1L;
 private long npproposedid;
 private String npproposedtype;
 private long npindice;


  public void setNpproposedid(long npproposedid)
  {
    this.npproposedid = npproposedid;
  }


  public long getNpproposedid()
  {
    return npproposedid;
  }


  public void setNpproposedtype(String npproposedtype)
  {
    this.npproposedtype = npproposedtype;
  }


  public String getNpproposedtype()
  {
    return npproposedtype;
  }


  public void setNpindice(long npindice)
  {
    this.npindice = npindice;
  }


  public long getNpindice()
  {
    return npindice;
  }
}