package pe.com.nextel.bean;

import java.io.Serializable;


public class SubCategoryBean   implements Serializable{
  private static final long serialVersionUID = 1L;
    private int npSpecificationId;
    private String npSpecification;
   
    public void setNpSpecificationId(int npSpecificationId) {
           this.npSpecificationId = npSpecificationId;
       }
    public int getNpSpecificationId() {
           return this.npSpecificationId;
       }
    public void setNpSpecification(String npSpecification) {
           this.npSpecification = npSpecification;
       }
    public String getNpSpecification() {
           return this.npSpecification;
       }  

}
