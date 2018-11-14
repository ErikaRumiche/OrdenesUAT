package pe.com.nextel.bean;

import java.io.Serializable;

/**
 * Created by Practia on 31/07/2015.
 */
public class VerificationCustomerBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private  String  verificationType;
    private  String  source;
    private  String  motive;
    private  String  authorizedUser;
    private  int     transaction;//idOrder
    private  String  typeTransaction;
    private  String  documento;
    private  String  docType;
    private  String  phoneNumber;
    private  int     status;
    private  String  createby;
    private  String  modifiedby;


    public String getVerificationType() {
        return verificationType;
    }

    public void setVerificationType(String verificationType) {
        this.verificationType = verificationType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMotive() {
        return motive;
    }

    public void setMotive(String motive) {
        this.motive = motive;
    }

    public String getAuthorizedUser() {
        return authorizedUser;
    }

    public void setAuthorizedUser(String authorizedUser) {
        this.authorizedUser = authorizedUser;
    }

    public int getTransaction() {
        return transaction;
    }

    public void setTransaction(int transaction) {
        this.transaction = transaction;
    }

    public String getTypeTransaction() {
        return typeTransaction;
    }

    public void setTypeTransaction(String typeTransaction) {
        this.typeTransaction = typeTransaction;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreateby() {
        return createby;
    }

    public void setCreateby(String createby) {
        this.createby = createby;
    }

    public String getModifiedby() {
        return modifiedby;
    }

    public void setModifiedby(String modifiedby) {
        this.modifiedby = modifiedby;
    }

    public String getDocType() { return docType; }

    public void setDocType(String docType) { this.docType = docType; }

    @Override
    public String toString() {
        return "VerificationCustomerBean{" +
                "verificationType='" + verificationType + '\'' +
                ", source='" + source + '\'' +
                ", motive='" + motive + '\'' +
                ", authorizedUser='" + authorizedUser + '\'' +
                ", transaction=" + transaction +
                ", typeTransaction='" + typeTransaction + '\'' +
                ", documento='" + documento + '\'' +
                ", docType='" + docType + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", status=" + status +
                ", createby='" + createby + '\'' +
                ", modifiedby='" + modifiedby + '\'' +
                '}';
    }
}
