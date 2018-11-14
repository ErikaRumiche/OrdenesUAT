package pe.com.nextel.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Administrador on 21/12/2015.
 */
public class DataDireccionBean implements Serializable {

    private static final long serialVersionUID = -6524220459896257067L;

    private BigDecimal idDireccion;
    private String direccion;
    private String tipoDireccion;
    private String codigoCliente;
    private String usuarioApp;
    private String nombreAplicacion;
    private String codigoUnico;
    private String ubigeo;
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

    public String getUbigeo() {
        return ubigeo;
    }

    public void setUbigeo(String ubigeo) {
        this.ubigeo = ubigeo;
    }

    public String getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    @Override
    public String toString() {
        return "DataDireccionBean{" +
                "idDireccion='" + getIdDireccion() + '\'' +
                ", direccion='" + direccion + '\'' +
                ", tipoDireccion='" + tipoDireccion + '\'' +
                ", codigoCliente='" + codigoCliente + '\'' +
                ", usuarioApp='" + usuarioApp + '\'' +
                ", nombreAplicacion='" + nombreAplicacion + '\'' +
                ", codigoUnico='" + codigoUnico + '\'' +
                ", ubigeo='" + ubigeo + '\'' +
                ", numeroPedido='" + numeroPedido + '\'' +
                '}';
    }

    public BigDecimal getIdDireccion() {
        return idDireccion;
    }

    public void setIdDireccion(BigDecimal idDireccion) {
        this.idDireccion = idDireccion;
    }
}
