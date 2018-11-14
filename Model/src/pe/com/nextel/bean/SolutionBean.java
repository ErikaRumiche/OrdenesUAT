package pe.com.nextel.bean;

import java.io.Serializable;

import pe.com.nextel.util.GenericObject;


public class SolutionBean extends GenericObject implements Serializable 
{
  private static final long serialVersionUID = 1L;
  private long npsolutionid;
	private String npsolutionname;


  public void setNpsolutionid(long npsolutionid)
  {
    this.npsolutionid = npsolutionid;
  }


  public long getNpsolutionid()
  {
    return npsolutionid;
  }


  public void setNpsolutionname(String npsolutionname)
  {
    this.npsolutionname = npsolutionname;
  }


  public String getNpsolutionname()
  {
    return npsolutionname;
  }

 
}