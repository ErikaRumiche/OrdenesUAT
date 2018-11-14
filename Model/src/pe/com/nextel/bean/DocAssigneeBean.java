package pe.com.nextel.bean;

import java.sql.Timestamp;

/**
 * Created by HP on 11/04/2017.
 */
public class DocAssigneeBean {
    private Long docAssigneeId;
    private Long orderId;
    private String firstName;
    private String lastName;
    private String familyName;
    private String typeDoc;
    private String numDoc;
    private String createdBy;
    private Timestamp createdDate;
    private String modificationBy;
    private Timestamp modificationDate;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getTypeDoc() {
        return typeDoc;
    }

    public void setTypeDoc(String typeDoc) {
        this.typeDoc = typeDoc;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getDocAssigneeId() {
        return docAssigneeId;
    }

    public void setDocAssigneeId(Long docAssigneeId) {
        this.docAssigneeId = docAssigneeId;
    }
}
