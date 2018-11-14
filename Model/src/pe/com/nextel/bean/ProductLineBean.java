package pe.com.nextel.bean;

import java.io.Serializable;

import pe.com.nextel.util.GenericObject;


public class ProductLineBean extends GenericObject implements Serializable{
  private static final long serialVersionUID = 1L;
    private int     npProductLineId;
    private String  npName;
    private int     nptype;

    public void setNpProductLineId(int npProductLineId) {
        this.npProductLineId = npProductLineId;
    }

    public int getNpProductLineId() {
        return npProductLineId;
    }

    public void setNpName(String npName) {
        this.npName = npName;
    }

    public String getNpName() {
        return npName;
    }


  public void setNptype(int nptype)
  {
    this.nptype = nptype;
  }


  public int getNptype()
  {
    return nptype;
  }
}
