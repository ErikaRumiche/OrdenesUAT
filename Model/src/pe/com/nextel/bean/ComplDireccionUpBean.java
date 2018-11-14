package pe.com.nextel.bean;

/**
 * Created by Administrador on 21/12/2015.
 */
public class ComplDireccionUpBean {

    private static final long serialVersionUID = -6933334394787286592L;

    private String direccionNorm;
    private String estado;
    private String motivo;
    private String ubigeo;
    private String descDep;
    private String descProv;
    private String descDist;
    private String idDep;
    private String idProv;
    private String idDist;
    private String addressSales;
    private String operacion;
    private String tipoDireccion;

    public String getDireccionNorm() {
        return direccionNorm;
    }

    public void setDireccionNorm(String direccionNorm) {
        this.direccionNorm = direccionNorm;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getUbigeo() {
        return ubigeo;
    }

    public void setUbigeo(String ubigeo) {
        this.ubigeo = ubigeo;
    }

    public String getDescDep() {
        return descDep;
    }

    public void setDescDep(String descDep) {
        this.descDep = descDep;
    }

    public String getDescProv() {
        return descProv;
    }

    public void setDescProv(String descProv) {
        this.descProv = descProv;
    }

    public String getDescDist() {
        return descDist;
    }

    public void setDescDist(String descDist) {
        this.descDist = descDist;
    }

    public String getIdDep() {
        return idDep;
    }

    public void setIdDep(String idDep) {
        this.idDep = idDep;
    }

    public String getIdProv() {
        return idProv;
    }

    public void setIdProv(String idProv) {
        this.idProv = idProv;
    }

    public String getIdDist() {
        return idDist;
    }

    public void setIdDist(String idDist) {
        this.idDist = idDist;
    }

    public String getAddressSales() {
        return addressSales;
    }

    public void setAddressSales(String addressSales) {
        this.addressSales = addressSales;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public String getTipoDireccion() {
        return tipoDireccion;
    }

    public void setTipoDireccion(String tipoDireccion) {
        this.tipoDireccion = tipoDireccion;
    }

    @Override
    public String toString() {
        return "ComplDireccionUpBean [direccionNorm=" + direccionNorm + ", estado=" + estado + ", motivo=" + motivo
                + ", ubigeo=" + ubigeo + ", descDep=" + descDep + ", descProv=" + descProv + ", descDist=" + descDist
                + ", idDep=" + idDep + ", idProv=" + idProv + ", idDist=" + idDist + ", addressSales=" + addressSales
                + ", operacion=" + operacion + ", tipoDireccion=" + tipoDireccion + "]";
    }
}
