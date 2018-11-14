package pe.com.nextel.bean;

import java.io.Serializable;

public class SectionDinamicBean implements Serializable{
  private static final long serialVersionUID = 1L;
    private long nppagesectionid;   
    private String nppagenpname;
    private String npname;
    private String npeventname;
    private String npeventhandler;
    private String nptypeobject;
    private String npobjectname;
    private String npbusinessobject;
    
    private long npSpecificationId;
    
  public SectionDinamicBean()
  {
  }


  public void setNppagenpname(String nppagenpname)
  {
    this.nppagenpname = nppagenpname;
  }


  public String getNppagenpname()
  {
    return nppagenpname;
  }


  public void setNpname(String npname)
  {
    this.npname = npname;
  }


  public String getNpname()
  {
    return npname;
  }


  public void setNpeventname(String npeventname)
  {
    this.npeventname = npeventname;
  }


  public String getNpeventname()
  {
    return npeventname;
  }


  public void setNpeventhandler(String npeventhandler)
  {
    this.npeventhandler = npeventhandler;
  }


  public String getNpeventhandler()
  {
    return npeventhandler;
  }


  public void setNptypeobject(String nptypeobject)
  {
    this.nptypeobject = nptypeobject;
  }


  public String getNptypeobject()
  {
    return nptypeobject;
  }


  public void setNpobjectname(String npobjectname)
  {
    this.npobjectname = npobjectname;
  }


  public String getNpobjectname()
  {
    return npobjectname;
  }


  public void setNpbusinessobject(String npbusinessobject)
  {
    this.npbusinessobject = npbusinessobject;
  }


  public String getNpbusinessobject()
  {
    return npbusinessobject;
  }
  
  
  
    public void setNppagesectionid(long nppagesectionid) {
        this.nppagesectionid = nppagesectionid;
    }

    public long getNppagesectionid() {
        return nppagesectionid;
    }


  public void setNpSpecificationId(long npSpecificationId)
  {
    this.npSpecificationId = npSpecificationId;
  }


  public long getNpSpecificationId()
  {
    return npSpecificationId;
  }

  @Override
  public String toString() {
    return "SectionDinamicBean{" +
            "nppagesectionid=" + nppagesectionid +
            ", nppagenpname='" + nppagenpname + '\'' +
            ", npname='" + npname + '\'' +
            ", npeventname='" + npeventname + '\'' +
            ", npeventhandler='" + npeventhandler + '\'' +
            ", nptypeobject='" + nptypeobject + '\'' +
            ", npobjectname='" + npobjectname + '\'' +
            ", npbusinessobject='" + npbusinessobject + '\'' +
            ", npSpecificationId=" + npSpecificationId +
            '}';
  }
}