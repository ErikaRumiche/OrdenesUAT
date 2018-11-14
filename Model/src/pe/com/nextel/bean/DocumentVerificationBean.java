package pe.com.nextel.bean;

import java.io.Serializable;

import java.util.Date;

import pe.com.nextel.util.GenericObject;


public class DocumentVerificationBean extends GenericObject implements Serializable {
    private static final long serialVersionUID = 6366041025461088330L;

    private long documentVerificationId;
    private long orderId;
    private int result; // 0: Rechazado. 1: Aprobado. 2: Pendiente. 3: Subsanado
    private String comments;
    private String status;
    private String createdBy;
    private Date createdDate;
    private String modifiedBy;
    private Date modifiedDate;
    private String receivedBy;
    private Date receivedDate;
    private int receivedStatus;

    private String resultDesc;

    public long getDocumentVerificationId() {
        return documentVerificationId;
    }

    public void setDocumentVerificationId(long documentVerificationId) {
        this.documentVerificationId = documentVerificationId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public int getReceivedStatus() {
        return receivedStatus;
    }

    public void setReceivedStatus(int receivedStatus) {
        this.receivedStatus = receivedStatus;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }
}
