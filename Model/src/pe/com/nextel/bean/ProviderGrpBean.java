package pe.com.nextel.bean;

import java.io.Serializable;


public class ProviderGrpBean   implements Serializable{

  private static final long serialVersionUID = 1L;
    private int swProviderGrpId;
    private String swName;
    
    public ProviderGrpBean() {
    }
    
    public void setSwProviderGrpId(int swProviderGrpId) {
           this.swProviderGrpId = swProviderGrpId;
       }
    public int getSwProviderGrpId() {
           return this.swProviderGrpId;
       }

    public void setSwName(String swName) {
           this.swName = swName;
       }
    public String getSwName() {
           return this.swName;
       }
       
    
}
