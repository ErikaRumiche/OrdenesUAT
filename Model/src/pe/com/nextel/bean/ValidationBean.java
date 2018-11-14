package pe.com.nextel.bean;

import pe.com.nextel.util.GenericObject;

import java.io.Serializable;
import java.util.Date;

public class ValidationBean extends GenericObject implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer verificationNumber;
    private String verificationResult;
    private String source;
    private Date verificationDate;
    private String verificationMotive;
    
    public ValidationBean() {
    }

    public ValidationBean(String verificationResult, String source, Date verificationDate, String verificationMotive) {
        this.verificationResult = verificationResult;
        this.source = source;
        this.verificationDate = verificationDate;
        this.verificationMotive = verificationMotive;
    }

    public Integer getVerificationNumber() {
        return verificationNumber;
    }

    public void setVerificationNumber(Integer verificationNumber) {
        this.verificationNumber = verificationNumber;
    }

    public String getVerificationResult() {
        return verificationResult;
    }

    public void setVerificationResult(String verificationResult) {
        this.verificationResult = verificationResult;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getVerificationDate() {
        return verificationDate;
    }

    public void setVerificationDate(Date verificationDate) {
        this.verificationDate = verificationDate;
    }

    public String getVerificationMotive() {
        return verificationMotive;
    }

    public void setVerificationMotive(String verificationMotive) {
        this.verificationMotive = verificationMotive;
    }

}
