package pe.com.nextel.bean;

import java.io.Serializable;

/**
 * Created by Practia on 31/07/2015.
 */
public class BiometricBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer biometricId;
    private Integer verificationId;
    private Integer attempbiometric;
    private String status;
    private String observacion;
    private String auditoria;
    private String renicCode;
    private String reniecResponse;
    private String valityDni;
    private String name;
    private String father;//apellidopaterno
    private String mother;
    private String createby;


    public Integer getBiometricId() {
        return biometricId;
    }

    public void setBiometricId(Integer biometricId) {
        this.biometricId = biometricId;
    }

    public Integer getVerificationId() {
        return verificationId;
    }

    public void setVerificationId(Integer verificationId) {
        this.verificationId = verificationId;
    }

    public Integer getAttempbiometric() {
        return attempbiometric;
    }

    public void setAttempbiometric(Integer attempbiometric) {
        this.attempbiometric = attempbiometric;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(String auditoria) {
        this.auditoria = auditoria;
    }

    public String getRenicCode() {
        return renicCode;
    }

    public void setRenicCode(String renicCode) {
        this.renicCode = renicCode;
    }

    public String getReniecResponse() {
        return reniecResponse;
    }

    public void setReniecResponse(String reniecResponse) {
        this.reniecResponse = reniecResponse;
    }

    public String getValityDni() {
        return valityDni;
    }

    public void setValityDni(String valityDni) {
        this.valityDni = valityDni;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getCreateby() {
        return createby;
    }

    public void setCreateby(String createby) {
        this.createby = createby;
    }

    @Override
    public String toString() {
        return "BiometricBean{" +
                "biometricId=" + biometricId +
                ", verificationId=" + verificationId +
                ", attempbiometric=" + attempbiometric +
                ", status='" + status + '\'' +
                ", observacion='" + observacion + '\'' +
                ", auditoria='" + auditoria + '\'' +
                ", renicCode='" + renicCode + '\'' +
                ", reniecResponse='" + reniecResponse + '\'' +
                ", valityDni='" + valityDni + '\'' +
                ", name='" + name + '\'' +
                ", father='" + father + '\'' +
                ", mother='" + mother + '\'' +
                ", createby='" + createby + '\'' +
                '}';
    }
}
