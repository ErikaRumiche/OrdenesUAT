package pe.com.nextel.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrador on 21/12/2015.
 */
public class NormalizarDireccionBean implements Serializable {

    private static final long serialVersionUID = 8017252781151988169L;

    private String tipoVia;
    private String nombreVia;
    private String numeroPuerta;
    private String manzana;
    private String nombreUbanizacion;
    private String numeroInterior;
    private String lote;
    private String tipoZona;
    private String nombreZona;
    private String departamento;
    private String provincia;
    private String distrito;
    private String geolocalizacionX;
    private String geolocalizacionY;
    private Date fechaNormalizacion;

    public String getTipoVia() {
        return tipoVia;
    }

    public void setTipoVia(String tipoVia) {
        this.tipoVia = tipoVia;
    }

    public String getNombreVia() {
        return nombreVia;
    }

    public void setNombreVia(String nombreVia) {
        this.nombreVia = nombreVia;
    }

    public String getNumeroPuerta() {
        return numeroPuerta;
    }

    public void setNumeroPuerta(String numeroPuerta) {
        this.numeroPuerta = numeroPuerta;
    }

    public String getManzana() {
        return manzana;
    }

    public void setManzana(String manzana) {
        this.manzana = manzana;
    }

    public String getNombreUbanizacion() {
        return nombreUbanizacion;
    }

    public void setNombreUbanizacion(String nombreUbanizacion) {
        this.nombreUbanizacion = nombreUbanizacion;
    }

    public String getNumeroInterior() {
        return numeroInterior;
    }

    public void setNumeroInterior(String numeroInterior) {
        this.numeroInterior = numeroInterior;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getTipoZona() {
        return tipoZona;
    }

    public void setTipoZona(String tipoZona) {
        this.tipoZona = tipoZona;
    }

    public String getNombreZona() {
        return nombreZona;
    }

    public void setNombreZona(String nombreZona) {
        this.nombreZona = nombreZona;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getGeolocalizacionX() {
        return geolocalizacionX;
    }

    public void setGeolocalizacionX(String geolocalizacionX) {
        this.geolocalizacionX = geolocalizacionX;
    }

    public String getGeolocalizacionY() {
        return geolocalizacionY;
    }

    public void setGeolocalizacionY(String geolocalizacionY) {
        this.geolocalizacionY = geolocalizacionY;
    }

    public Date getFechaNormalizacion() {
        return fechaNormalizacion;
    }

    public void setFechaNormalizacion(Date fechaNormalizacion) {
        this.fechaNormalizacion = fechaNormalizacion;
    }

    @Override
    public String toString() {
        return "NormalizarDireccionBean{" +
                "tipoVia='" + tipoVia + '\'' +
                ", nombreVia='" + nombreVia + '\'' +
                ", numeroPuerta='" + numeroPuerta + '\'' +
                ", manzana='" + manzana + '\'' +
                ", nombreUbanizacion='" + nombreUbanizacion + '\'' +
                ", numeroInterior='" + numeroInterior + '\'' +
                ", lote='" + lote + '\'' +
                ", tipoZona='" + tipoZona + '\'' +
                ", nombreZona='" + nombreZona + '\'' +
                ", departamento='" + departamento + '\'' +
                ", provincia='" + provincia + '\'' +
                ", distrito='" + distrito + '\'' +
                ", geolocalizacionX='" + geolocalizacionX + '\'' +
                ", geolocalizacionY='" + geolocalizacionY + '\'' +
                ", fechaNormalizacion=" + fechaNormalizacion +
                '}';
    }
}