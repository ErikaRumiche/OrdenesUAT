package pe.com.nextel.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Bean que contiene informacion de una Verificacion de Identidad Aislada
 */
public class VerificationBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long verificationId;
    private Integer biometricId;
    private String tipoVerificacion;
    private String origen;
    private String numero; //Numero de transaccion
    private Date fechaInicioVigencia;
    private Date fechaFinVIgencia;
    private Date fechaUtilizacion;
    private Date fechaCreacion;
    private String tipoDocumento;
    private String numeroDocumento;
    private String nombres;
    private String apellidos;
    private String estado;
    private String registradoPor;
    private String modificadoPor;
    private List<VerificationBean> historicoBiometrica;
    private List<VerificationBean> historicoNoBiometrica;
    private Integer numPreguntaAcertada;
    private String motivo;

    public Integer getBiometricId() {
        return biometricId;
    }

    public void setBiometricId(Integer biometricId) {
        this.biometricId = biometricId;
    }

    public VerificationBean(){}

    public Long getVerificationId() {
        return verificationId;
    }

    public void setVerificationId(Long verificationId) {
        this.verificationId = verificationId;
    }

    public String getTipoVerificacion() {
        return tipoVerificacion;
    }

    public void setTipoVerificacion(String tipoVerificacion) {
        this.tipoVerificacion = tipoVerificacion;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
    
    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public Date getFechaFinVIgencia() {
        return fechaFinVIgencia;
    }

    public void setFechaFinVIgencia(Date fechaFinVIgencia) {
        this.fechaFinVIgencia = fechaFinVIgencia;
    }

    public Date getFechaUtilizacion() {
        return fechaUtilizacion;
    }

    public void setFechaUtilizacion(Date fechaUtilizacion) {
        this.fechaUtilizacion = fechaUtilizacion;
    }

    public Date getFechaInicioVigencia() {
        return fechaInicioVigencia;
    }

    public void setFechaInicioVigencia(Date fechaInicioVigencia) {
        this.fechaInicioVigencia = fechaInicioVigencia;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
    
    public String getRegistradoPor() {
        return registradoPor;
    }

    public void setRegistradoPor(String registradoPor) {
        this.registradoPor = registradoPor;
    }

    public String getModificadoPor() {
        return modificadoPor;
    }

    public void setModificadoPor(String modificadoPor) {
        this.modificadoPor = modificadoPor;
    }

    public List<VerificationBean> getHistoricoNoBiometrica() {
        if (historicoNoBiometrica == null) return new ArrayList<VerificationBean>();
        return historicoNoBiometrica;
    }

    public void setHistoricoNoBiometrica(List<VerificationBean> historicoNoBiometrica) {
        this.historicoNoBiometrica = historicoNoBiometrica;
    }

    public List<VerificationBean> getHistoricoBiometrica() {
        if (historicoBiometrica == null) return new ArrayList<VerificationBean>();
        return historicoBiometrica;
    }

    public void setHistoricoBiometrica(List<VerificationBean> historicoBiometrica) {
        this.historicoBiometrica = historicoBiometrica;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getNumPreguntaAcertada() {
        return numPreguntaAcertada;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setNumPreguntaAcertada(Integer numPreguntaAcertada) {
        this.numPreguntaAcertada = numPreguntaAcertada;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
