package pe.com.nextel.bean;

import java.io.Serializable;

import java.util.Date;


public class DefaultServiceBean  implements Serializable{
  private static final long serialVersionUID = 1L;
    private String npObjectType;
    private Long npSpecificationId;
    private Long npServiceId;
    private String npStatus;
    private Date npCreatedDate;
    private String npCreatedBy;


    public DefaultServiceBean() {
    }

    public void setNpObjectType(String npObjectType) {
           this.npObjectType = npObjectType;
       }
    public String getNpObjectType() {
           return this.npObjectType;
       } 

    public void setSpecificationId(Long npSpecificationId) {
           this.npSpecificationId = npSpecificationId;
       }
    public Long getNpSpecificationId() {
           return this.npSpecificationId;
       } 

    public void setNpServiceId(Long npServiceId) {
           this.npServiceId = npServiceId;
       }
    public Long getNpServiceId() {
           return this.npServiceId;
       }

    public void setNpStatus(String npStatus) {
           this.npStatus = npStatus;
       }
    public String getNpStatus() {
           return this.npStatus;
       } 
  
    public void setNpCreatedDate(Date npCreatedDate) {
           this.npCreatedDate = npCreatedDate;
       }
    public Date getNpCreatedDate() {
           return this.npCreatedDate;
       }     
    public void setNpCreatedBy(String npCreatedBy) {
           this.npCreatedBy = npCreatedBy;
       }
    public String getNpCreatedBy() {
           return this.npCreatedBy;
       }        


}
