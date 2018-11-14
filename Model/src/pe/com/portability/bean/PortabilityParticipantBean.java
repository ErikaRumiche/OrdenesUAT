package pe.com.portability.bean;

import java.io.Serializable;

import pe.com.nextel.util.GenericObject;


public class PortabilityParticipantBean extends GenericObject implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String npParticipantId;
  private String npDescripcion;
  private String npTag1;
  private String npStatus;
  
  public PortabilityParticipantBean()
  {
  }


  public void setNpParticipantId(String npParticipantId)
  {
    this.npParticipantId = npParticipantId;
  }


  public String getNpParticipantId()
  {
    return npParticipantId;
  }


  public void setNpDescripcion(String npDescripcion)
  {
    this.npDescripcion = npDescripcion;
  }


  public String getNpDescripcion()
  {
    return npDescripcion;
  }


  public void setNpTag1(String npTag1)
  {
    this.npTag1 = npTag1;
  }


  public String getNpTag1()
  {
    return npTag1;
  }


  public void setNpStatus(String npStatus)
  {
    this.npStatus = npStatus;
  }


  public String getNpStatus()
  {
    return npStatus;
  }
  
  
}