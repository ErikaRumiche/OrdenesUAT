package pe.com.nextel.bean;

import java.math.BigDecimal;

/**
 * Created by Administrador on 21/12/2015.
 */
public class DireccionJsonBean {

    private static final long serialVersionUID = -6524220459896257067L;

    private BigDecimal idDireccion;
    private String direccion;
    private String tipoDireccion;
    private String codigoCliente;
    private String usuarioApp;
    private String nombreAplicacion;
    private String codigoUnico;
    private String ubigeoReniec;
    private BigDecimal ubigeoId;
    private String numeroPedido;

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTipoDireccion() {
        return tipoDireccion;
    }

    public void setTipoDireccion(String tipoDireccion) {
        this.tipoDireccion = tipoDireccion;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getUsuarioApp() {
        return usuarioApp;
    }

    public void setUsuarioApp(String usuarioApp) {
        this.usuarioApp = usuarioApp;
    }

    public String getNombreAplicacion() {
        return nombreAplicacion;
    }

    public void setNombreAplicacion(String nombreAplicacion) {
        this.nombreAplicacion = nombreAplicacion;
    }

    public String getCodigoUnico() {
        return codigoUnico;
    }

    public void setCodigoUnico(String codigoUnico) {
        this.codigoUnico = codigoUnico;
    }

    public String getUbigeoReniec() {
        return ubigeoReniec;
    }

    public void setUbigeoReniec(String ubigeoReniec) {
        this.ubigeoReniec = ubigeoReniec;
    }

    public BigDecimal getUbigeoId() {
        return ubigeoId;
    }

    public void setUbigeoId(BigDecimal ubigeoId) {
        this.ubigeoId = ubigeoId;
    }


    public String getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public BigDecimal getIdDireccion() {
        return idDireccion;
    }

    public void setIdDireccion(BigDecimal idDireccion) {
        this.idDireccion = idDireccion;
    }

    @Override
    public String toString() {
        return "DireccionJsonBean [idDireccion=" + idDireccion + ", direccion=" + direccion + ", tipoDireccion="
                + tipoDireccion + ", codigoCliente=" + codigoCliente + ", usuarioApp=" + usuarioApp
                + ", nombreAplicacion=" + nombreAplicacion + ", codigoUnico=" + codigoUnico + ", ubigeoReniec="
                + ubigeoReniec + ", ubigeoId=" + ubigeoId + ", numeroPedido=" + numeroPedido + "]";
    }
}
