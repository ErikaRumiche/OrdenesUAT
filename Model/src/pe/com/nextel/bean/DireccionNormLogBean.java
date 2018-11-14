package pe.com.nextel.bean;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrador on 21/12/2015.
 */
public class DireccionNormLogBean {

    private static final long serialVersionUID = -8120868614893030475L;

    private String codigoCliente;
    private String tipoDireccion;
    private String direccionAntigua;
    private String request;
    private String response;
    private String usuarioApp;
    private String nombreAplicacion;
    private Date fechaHora;
    private String numeroOcurrencia;
    private String numeroPedido;
    private String tiempoRespOSB;
    private String tiempoRespWS;
    private String flagGeocodificacion;
    private String motivoNoGeocodificacion;
    private BigDecimal idDireccionAntigua;
    private String descExcepcion;

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getTipoDireccion() {
        return tipoDireccion;
    }

    public void setTipoDireccion(String tipoDireccion) {
        this.tipoDireccion = tipoDireccion;
    }

    public String getDireccionAntigua() {
        return direccionAntigua;
    }

    public void setDireccionAntigua(String direccionAntigua) {
        this.direccionAntigua = direccionAntigua;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
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

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getNumeroOcurrencia() {
        return numeroOcurrencia;
    }

    public void setNumeroOcurrencia(String numeroOcurrencia) {
        this.numeroOcurrencia = numeroOcurrencia;
    }

    public String getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public String getTiempoRespOSB() {
        return tiempoRespOSB;
    }

    public void setTiempoRespOSB(String tiempoRespOSB) {
        this.tiempoRespOSB = tiempoRespOSB;
    }

    public String getTiempoRespWS() {
        return tiempoRespWS;
    }

    public void setTiempoRespWS(String tiempoRespWS) {
        this.tiempoRespWS = tiempoRespWS;
    }

    public String getFlagGeocodificacion() {
        return flagGeocodificacion;
    }

    public void setFlagGeocodificacion(String flagGeocodificacion) {
        this.flagGeocodificacion = flagGeocodificacion;
    }

    public String getMotivoNoGeocodificacion() {
        return motivoNoGeocodificacion;
    }

    public void setMotivoNoGeocodificacion(String motivoNoGeocodificacion) {
        this.motivoNoGeocodificacion = motivoNoGeocodificacion;
    }

    public BigDecimal getIdDireccionAntigua() {
        return idDireccionAntigua;
    }

    public void setIdDireccionAntigua(BigDecimal idDireccionAntigua) {
        this.idDireccionAntigua = idDireccionAntigua;
    }

    public String getDescExcepcion() {
        return descExcepcion;
    }

    public void setDescExcepcion(String descExcepcion) {
        this.descExcepcion = descExcepcion;
    }

    @Override
    public String toString() {
        return "DireccionNormLogBean{" +
                "codigoCliente='" + getCodigoCliente() + '\'' +
                ", tipoDireccion='" + getTipoDireccion() + '\'' +
                ", direccionAntigua='" + getDireccionAntigua() + '\'' +
                ", request='" + getRequest() + '\'' +
                ", response='" + getResponse() + '\'' +
                ", usuarioApp='" + getUsuarioApp() + '\'' +
                ", nombreAplicacion='" + getNombreAplicacion() + '\'' +
                ", fechaHora=" + getFechaHora() +
                ", numeroOcurrencia='" + getNumeroOcurrencia() + '\'' +
                ", numeroPedido='" + getNumeroPedido() + '\'' +
                ", tiempoRespOSB='" + getTiempoRespOSB() + '\'' +
                ", tiempoRespWS='" + getTiempoRespWS() + '\'' +
                ", flagGeocodificacion='" + getFlagGeocodificacion() + '\'' +
                ", motivoNoGeocodificacion='" + getMotivoNoGeocodificacion() + '\'' +
                ", idDireccionAntigua=" + getIdDireccionAntigua() +
                ", descExcepcion='" + getDescExcepcion() + '\'' +
                '}';
    }
}
