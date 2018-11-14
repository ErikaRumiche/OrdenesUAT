package pe.com.nextel.bean;

import java.io.Serializable;

public class ModePaymentBean   implements Serializable{
  private static final long serialVersionUID = 1L;
    private String npValue;
    private String npValueDesc;
    private String npTag1;
    private String npTag2;
    private int npOrder;
            
    public ModePaymentBean() {
    }
    
    public void setNpValue(String npValue) {
           this.npValue = npValue;
       }
    public String getNpValue() {
           return this.npValue;
       }  
    public void setNpValueDesc(String npValueDesc) {
           this.npValueDesc = npValueDesc;
       }
    public String getNpValueDesc() {
           return this.npValueDesc;
       }         
    public void setNpTag1(String npTag1) {
           this.npTag1 = npTag1;
       }
    public String getNpTag1() {
           return this.npTag1;
       }         
    public void setNpTag2(String npTag2) {
           this.npTag2 = npTag2;
       }
    public String getNpTag2() {
           return this.npTag2;
       }    
    public void setNpOrder(int npOrder) {
           this.npOrder = npOrder;
       }
    public int getNpOrder() {
           return this.npOrder;
       }         
}
