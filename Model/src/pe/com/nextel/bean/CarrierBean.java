package pe.com.nextel.bean;

import java.io.Serializable;

import java.util.Date;


public class CarrierBean  implements Serializable{
  private static final long serialVersionUID = 1L;
   
    private Long npCarrierId;
    private String npType;
    private String npName;
    private String npDocumentNumber;
    private String npAddress;
    private String npStatus;
    private Date npCreatedDate;
    private String npCreatedBy;

    
    public CarrierBean() {
    }
    
    public void setNpCarrierId(Long npCarrierId) {
           this.npCarrierId = npCarrierId;
       }
    public Long getNpCarrierId() {
           return this.npCarrierId;
       }

    public void setNpType(String npType) {
           this.npType = npType;
       }
    public String getNpType() {
           return this.npType;
       }       
    public void setNpName(String npName) {
           this.npName =npName;
       }
    public String getNpName() {
           return this.npName;
       }           

    public void setNpDocumentNumber(String npDocumentNumber) {
           this.npDocumentNumber = npDocumentNumber;
       }
    public String getNpDocumentNumber() {
           return this.npDocumentNumber;
       }   
       
    public void setNpAddress(String npAddress) {
           this.npAddress = npAddress;
       }
    public String getNpAddress() {
           return this.npAddress;
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
