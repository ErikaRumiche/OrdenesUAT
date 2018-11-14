package pe.com.nextel.bean;

import java.io.Serializable;

public class PlanBean   implements Serializable{
  private static final long serialVersionUID = 1L;
    private int     npPlanId;
    private String  npPlanDesc;
    
    public PlanBean() {
    }

    public void setNpPlanId(int npPlanId) {
        this.npPlanId = npPlanId;
    }

    public int getNpPlanId() {
        return npPlanId;
    }

    public void setNpPlanDesc(String npPlanDesc) {
        this.npPlanDesc = npPlanDesc;
    }

    public String getNpPlanDesc() {
        return npPlanDesc;
    }
}
