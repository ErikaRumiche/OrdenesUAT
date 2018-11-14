package pe.com.nextel.bean;


import java.sql.Timestamp;

/**
 * Created by HP on 29/03/2017.
 */
public class DocumentGenerationBean {
    private Long id;
    private Long orderId;
    private Long customerId;
    private Long specificationId;
    private Long resolutionId;
    private Integer trxType;
    private Long buildingId;
    private Long assigneeId;
    private String requestNumber;
    private String email;
    private Integer emailNullF;
    private Integer signatureType;
    private String signatureTypeLabel;
    private Integer signatureReason;
    private String signatureReasonLabel;
    private Integer generationStatus;
    private Integer emailStatus;
    private String generatedFiles;
    private String createdBy;
    private Timestamp createdDate;
    private String modificationBy;
    private Timestamp modificationDate;
    private String trxWs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getSpecificationId() {
        return specificationId;
    }

    public void setSpecificationId(Long specificationId) {
        this.specificationId = specificationId;
    }

    public Long getResolutionId() {
        return resolutionId;
    }

    public void setResolutionId(Long resolutionId) {
        this.resolutionId = resolutionId;
    }

    public Integer getTrxType() {
        return trxType;
    }

    public void setTrxType(Integer trxType) {
        this.trxType = trxType;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public Long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(String requestNumber) {
        this.requestNumber = requestNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getEmailNullF() {
        return emailNullF;
    }

    public void setEmailNullF(Integer emailNuloF) {
        this.emailNullF = emailNullF;
    }

    public Integer getSignatureType() {
        return signatureType;
    }

    public void setSignatureType(Integer signatureType) {
        this.signatureType = signatureType;
    }

    public String getSignatureTypeLabel() {
        return signatureTypeLabel;
    }

    public void setSignatureTypeLabel(String signatureTypeLabel) {
        this.signatureTypeLabel = signatureTypeLabel;
    }

    public Integer getSignatureReason() {
        return signatureReason;
    }

    public void setSignatureReason(Integer signatureReason) {
        this.signatureReason = signatureReason;
    }

    public String getSignatureReasonLabel() {
        return signatureReasonLabel;
    }

    public void setSignatureReasonLabel(String signatureReasonLabel) {
        this.signatureReasonLabel = signatureReasonLabel;
    }

    public Integer getGenerationStatus() {
        return generationStatus;
    }

    public void setGenerationStatus(Integer generationStatus) {
        this.generationStatus = generationStatus;
    }

    public Integer getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(Integer emailStatus) {
        this.emailStatus = emailStatus;
    }

    public String getGeneratedFiles() {
        return generatedFiles;
    }

    public void setGeneratedFiles(String generatedFiles) {
        this.generatedFiles = generatedFiles;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getModificationBy() {
        return modificationBy;
    }

    public void setModificationBy(String modificationBy) {
        this.modificationBy = modificationBy;
    }

    public Timestamp getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Timestamp modificationDate) {
        this.modificationDate = modificationDate;
    }

    @Override
    public String toString() {
        return "DocumentGenerationBean{" +
                "orderId=" + orderId +
                ", specificationId=" + specificationId +
                ", trxType=" + trxType +
                ", assigneeId=" + assigneeId +
                ", email='" + email + '\'' +
                ", emailNullF=" + emailNullF +
                ", signatureType=" + signatureType +
                ", signatureTypeLabel='" + signatureTypeLabel + '\'' +
                ", signatureReason=" + signatureReason +
                ", generationStatus=" + generationStatus +
                ", signatureReasonLabel='" + signatureReasonLabel + '\'' +
                '}';
    }

    public String getTrxWs() {
        return trxWs;
    }

    public void setTrxWs(String trxWs) {
        this.trxWs = trxWs;
    }
}