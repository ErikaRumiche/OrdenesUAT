package pe.com.nextel.bean;

import java.io.Serializable;

public class EquipmentBean implements Serializable{
  private static final long serialVersionUID = 1L;
  private String     npequipmentid;
  private String  npequipmentdesc;


  public void setNpequipmentid(String npequipmentid)
  {
    this.npequipmentid = npequipmentid;
  }


  public String getNpequipmentid()
  {
    return npequipmentid;
  }


  public void setNpequipmentdesc(String npequipmentdesc)
  {
    this.npequipmentdesc = npequipmentdesc;
  }


  public String getNpequipmentdesc()
  {
    return npequipmentdesc;
  }
}