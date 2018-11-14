package pe.com.nextel.bean;

import java.io.Serializable;

public class FormatBean implements Serializable
{
  private static final long serialVersionUID = 1L;
  private long npformid;
  private String npformname;
  private String npprogramname;
  private String nptemplatename;


  public void setNpformid(long npformid)
  {
    this.npformid = npformid;
  }


  public long getNpformid()
  {
    return npformid;
  }


  public void setNpformname(String npformname)
  {
    this.npformname = npformname;
  }


  public String getNpformname()
  {
    return npformname;
  }


  public void setNpprogramname(String npprogramname)
  {
    this.npprogramname = npprogramname;
  }


  public String getNpprogramname()
  {
    return npprogramname;
  }


  public void setNptemplatename(String nptemplatename)
  {
    this.nptemplatename = nptemplatename;
  }


  public String getNptemplatename()
  {
    return nptemplatename;
  }
  
}