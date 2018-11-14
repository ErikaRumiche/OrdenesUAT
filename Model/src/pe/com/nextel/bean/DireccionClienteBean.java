package pe.com.nextel.bean;

import java.math.BigDecimal;

/**
 * Created by Administrador on 21/12/2015.
 */
public class DireccionClienteBean {
    private static final long serialVersionUID = -5862362277495533907L;

    private BigDecimal idDireccion;
    private String direccion;
    private String distrito;
    private String provincia;
    private String departamento;
    private String tipoDireccion;
    private String codTipoDireccion;
    private String distritoId;
    private String provinciaId;
    private String departamentoId;
    private String flagDireccion;
    private String addressSales;
    private String ubigeo;
    private String estado;
    private String referencia;

    public BigDecimal getIdDireccion() {
        return idDireccion;
    }
    public void setIdDireccion(BigDecimal idDireccion) {
        this.idDireccion = idDireccion;
    }
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public String getDistrito() {
        return distrito;
    }
    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }
    public String getProvincia() {
        return provincia;
    }
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }
    public String getDepartamento() {
        return departamento;
    }
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
    public String getTipoDireccion() {
        return tipoDireccion;
    }
    public void setTipoDireccion(String tipoDireccion) {
        this.tipoDireccion = tipoDireccion;
    }
    public String getCodTipoDireccion() {
        return codTipoDireccion;
    }
    public void setCodTipoDireccion(String codTipoDireccion) {
        this.codTipoDireccion = codTipoDireccion;
    }
    public String getDistritoId() {
        return distritoId;
    }
    public void setDistritoId(String distritoId) {
        this.distritoId = distritoId;
    }
    public String getProvinciaId() {
        return provinciaId;
    }
    public void setProvinciaId(String provinciaId) {
        this.provinciaId = provinciaId;
    }
    public String getDepartamentoId() {
        return departamentoId;
    }
    public void setDepartamentoId(String departamentoId) {
        this.departamentoId = departamentoId;
    }
    public String getFlagDireccion() {
        return flagDireccion;
    }
    public void setFlagDireccion(String flagDireccion) {
        this.flagDireccion = flagDireccion;
    }
    public String getReferencia() {
        return referencia;
    }
    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
    public String getAddressSales() {
        return addressSales;
    }
    public void setAddressSales(String addressSales) {
        this.addressSales = addressSales;
    }
    public String getUbigeo() {
        return ubigeo;
    }
    public void setUbigeo(String ubigeo) {
        this.ubigeo = ubigeo;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "DireccionClienteBean [idDireccion=" + idDireccion + ", direccion=" + direccion + ", distrito="
                + distrito + ", provincia=" + provincia + ", departamento=" + departamento + ", tipoDireccion="
                + tipoDireccion + ", codTipoDireccion=" + codTipoDireccion + ", distritoId=" + distritoId
                + ", provinciaId=" + provinciaId + ", departamentoId=" + departamentoId + ", flagDireccion="
                + flagDireccion + ", addressSales=" + addressSales + ", ubigeo=" + ubigeo + ", estado=" + estado
                + ", referencia=" + referencia + "]";
    }
}
