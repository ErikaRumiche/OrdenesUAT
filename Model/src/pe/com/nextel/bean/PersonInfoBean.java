package pe.com.nextel.bean;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

/**
 * Created by HHuaraca on 31/07/2015.
 */
public class PersonInfoBean {

    private String documentNumber;
    private BigInteger attempts;
    private BigInteger minAccepted;
    private byte[] photo;
    private String lengthPhoto;
    private String result;
    private List<QuizBean> lstQuizBean;
    private BigInteger verificationid;
    private String  fechaEmision;//ricardo.quispe PRY-0438

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }


    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getLengthPhoto() {
        return lengthPhoto;
    }

    public void setLengthPhoto(String lengthPhoto) {
        this.lengthPhoto = lengthPhoto;
    }

    public List<QuizBean> getLstQuizBean() {
        return lstQuizBean;
    }

    public void setLstQuizBean(List<QuizBean> lstQuizBean) {
        this.lstQuizBean = lstQuizBean;
    }

    public BigInteger getAttempts() {
        return attempts;
    }

    public void setAttempts(BigInteger attempts) {
        this.attempts = attempts;
    }

    public BigInteger getMinAccepted() {
        return minAccepted;
    }

    public void setMinAccepted(BigInteger minAccepted) {
        this.minAccepted = minAccepted;
    }

    public BigInteger getVerificationid() {
        return verificationid;
    }

    public void setVerificationid(BigInteger verificationid) {
        this.verificationid = verificationid;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    @Override
    public String toString() {
        return "PersonInfoBean{" +
                "documentNumber='" + documentNumber + '\'' +
                ", attempts=" + attempts +
                ", minAccepted=" + minAccepted +
                ", lengthPhoto='" + lengthPhoto + '\'' +
                ", result='" + result + '\'' +
                ", lstQuizBean=" + lstQuizBean +
                ", verificationid=" + verificationid +
                '}';
    }
}
