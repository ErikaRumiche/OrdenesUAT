package pe.com.nextel.bean;

import java.io.Serializable;

public class SolutionGroupBean implements Serializable{
  private static final long serialVersionUID = 1L;
  private long npsolutiongroupid;
  private String npname;
  private String npprefix;
  private long npdivisionid;


  public void setNpsolutiongroupid(long npsolutiongroupid)
  {
    this.npsolutiongroupid = npsolutiongroupid;
  }


  public long getNpsolutiongroupid()
  {
    return npsolutiongroupid;
  }


  public void setNpname(String npname)
  {
    this.npname = npname;
  }


  public String getNpname()
  {
    return npname;
  }


  public void setNpprefix(String npprefix)
  {
    this.npprefix = npprefix;
  }


  public String getNpprefix()
  {
    return npprefix;
  }


  public void setNpdivisionid(long npdivisionid)
  {
    this.npdivisionid = npdivisionid;
  }


  public long getNpdivisionid()
  {
    return npdivisionid;
  }
  
}