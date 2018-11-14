package pe.com.nextel.bean;

import java.io.Serializable;

import java.sql.Date;


public class RejectBean implements Serializable{

  private static final long serialVersionUID = 1L;
    private int npRejectId;
    private long npOrderId;
    private String npReason;
    private String npComment;
    private String npStatus;
    private String npInbox;
    private Date npModifiedDate;
    private String npModifiedBy;
    private Date npCreatedDate;
    private String npCreatedBy;
    

    public RejectBean() {
    }

    public void setNpRejectId(int npRejectId) {
        this.npRejectId = npRejectId;
    }

    public int getNpRejectId() {
        return this.npRejectId;
    }

    public void setNpOrderId(long npOrderId) {
        this.npOrderId = npOrderId;
    }

    public long getNpOrderId() {
        return this.npOrderId;
    }

    public void setNpReason(String npReason) {
        this.npReason = npReason;
    }

    public String getNpReason() {
        return this.npReason;
    }

    public void setNpComment(String npComment) {
        this.npComment = npComment;
    }

    public String getNpComment() {
        return this.npComment;
    }

    public void setNpStatus(String npStatus) {
        this.npStatus = npStatus;
    }

    public String getNpStatus() {
        return this.npStatus;
    }
    
    public String getNpStatusDesc() {
        if ("S".equals(npStatus))
           return "Subsanado";
        else if ("N".equals(npStatus) )
           return "Pendiente";
        else
            return "";        
    }

    public void setNpInbox(String npInbox) {
        this.npInbox = npInbox;
    }

    public String getNpInbox() {
        return this.npInbox;
    }

    public void setNpModifiedDate(Date npModifiedDate) {
        this.npModifiedDate = npModifiedDate;
    }

    public Date getNpModifiedDate() {
        return this.npModifiedDate;
    }

    public void setNpModifiedBy(String npModifiedBy) {
        this.npModifiedBy = npModifiedBy;
    }

    public String getNpModifiedBy() {
        return this.npModifiedBy;
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
