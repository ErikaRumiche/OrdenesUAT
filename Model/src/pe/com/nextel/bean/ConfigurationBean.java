package pe.com.nextel.bean;


import java.io.Serializable;

import java.util.Date;


public class ConfigurationBean implements Serializable{
  private static final long serialVersionUID = 1L;
    private String npConfiguration;
    private String npValue;
    private String npValueDesc;
    private String npStatus;
    private String npTag1;
    private String npTag2;
    private String npTag3;
    private Long npOrder;
    private String npCreatedBy;
    private Date npCreatedDate;
    private String npModificationBy;
    private Date npModificationDate;

    public void setNpConfiguration(String npConfiguration) {
        this.npConfiguration = npConfiguration;
    }

    public String getNpConfiguration() {
        return npConfiguration;
    }

    public void setNpValue(String npValue) {
        this.npValue = npValue;
    }

    public String getNpValue() {
        return npValue;
    }

    public void setNpValueDesc(String npValueDesc) {
        this.npValueDesc = npValueDesc;
    }

    public String getNpValueDesc() {
        return npValueDesc;
    }

    public void setNpStatus(String npStatus) {
        this.npStatus = npStatus;
    }

    public String getNpStatus() {
        return npStatus;
    }

    public void setNpTag1(String npTag1) {
        this.npTag1 = npTag1;
    }

    public String getNpTag1() {
        return npTag1;
    }

    public void setNpTag2(String npTag2) {
        this.npTag2 = npTag2;
    }

    public String getNpTag2() {
        return npTag2;
    }

    public void setNpTag3(String npTag3) {
        this.npTag3 = npTag3;
    }

    public String getNpTag3() {
        return npTag3;
    }

    public void setNpOrder(Long npOrder) {
        this.npOrder = npOrder;
    }

    public Long getNpOrder() {
        return npOrder;
    }

    public void setNpCreatedBy(String npCreatedBy) {
        this.npCreatedBy = npCreatedBy;
    }

    public String getNpCreatedBy() {
        return npCreatedBy;
    }

    public void setNpCreatedDate(Date npCreatedDate) {
        this.npCreatedDate = npCreatedDate;
    }

    public Date getNpCreatedDate() {
        return npCreatedDate;
    }

    public void setNpModificationBy(String npModificationBy) {
        this.npModificationBy = npModificationBy;
    }

    public String getNpModificationBy() {
        return npModificationBy;
    }

    public void setNpModificationDate(Date npModificationDate) {
        this.npModificationDate = npModificationDate;
    }

    public Date getNpModificationDate() {
        return npModificationDate;
    }
}
