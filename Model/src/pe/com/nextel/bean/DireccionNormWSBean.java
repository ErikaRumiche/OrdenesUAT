package pe.com.nextel.bean;

import java.math.BigDecimal;

/**
 * Created by Administrador on 21/12/2015.
 */
public class DireccionNormWSBean {

    private static final long serialVersionUID = 8017252781151988169L;

    private String direccionNormalizada;
    private String tipoVia;
    private String nombreVia;
    private String numeroPuerta;
    private String manzana;
    private String nombreUbanizacion;
    private String tipoInterior;
    private String numeroInterior;
    private String lote;
    private String tipoZona;
    private String nombreZona;
    private String referencia;
    private BigDecimal geolocalizacionX;
    private BigDecimal geolocalizacionY;
    private String ubigeoInei;

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getTipoInterior() {
        return tipoInterior;
    }

    public void setTipoInterior(String tipoInterior) {
        this.tipoInterior = tipoInterior;
    }

    public String getDireccionNormalizada() {
        return direccionNormalizada;
    }

    public void setDireccionNormalizada(String direccionNormalizada) {
        this.direccionNormalizada = direccionNormalizada;
    }

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

    public BigDecimal getGeolocalizacionX() {
        return geolocalizacionX;
    }

    public void setGeolocalizacionX(BigDecimal geolocalizacionX) {
        this.geolocalizacionX = geolocalizacionX;
    }

    public BigDecimal getGeolocalizacionY() {
        return geolocalizacionY;
    }

    public void setGeolocalizacionY(BigDecimal geolocalizacionY) {
        this.geolocalizacionY = geolocalizacionY;
    }

    public String getUbigeoInei() {
        return ubigeoInei;
    }

    public void setUbigeoInei(String ubigeoInei) {
        this.ubigeoInei = ubigeoInei;
    }

    @Override
    public String toString() {
        return "DireccionNormWSBean [direccionNormalizada=" + direccionNormalizada + ", tipoVia=" + tipoVia
                + ", nombreVia=" + nombreVia + ", numeroPuerta=" + numeroPuerta + ", manzana=" + manzana
                + ", nombreUbanizacion=" + nombreUbanizacion + ", tipoInterior=" + tipoInterior + ", numeroInterior="
                + numeroInterior + ", lote=" + lote + ", tipoZona=" + tipoZona + ", nombreZona=" + nombreZona
                + ", referencia=" + referencia + ", geolocalizacionX=" + geolocalizacionX + ", geolocalizacionY="
                + geolocalizacionY + ", ubigeoInei=" + ubigeoInei + "]";
    }
}
