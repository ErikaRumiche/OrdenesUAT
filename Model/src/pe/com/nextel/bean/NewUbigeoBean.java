package pe.com.nextel.bean;

import java.math.BigDecimal;

/**
 * Created by Administrador on 21/12/2015.
 */
public class NewUbigeoBean {

    private static final long serialVersionUID = -7633278167857288458L;

    private String nombre;
    private String dep;
    private String prov;
    private String dist;
    private BigDecimal npubigeoid;
    private String codigoinei;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDep() {
        return dep;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public BigDecimal getNpubigeoid() {
        return npubigeoid;
    }

    public void setNpubigeoid(BigDecimal npubigeoid) {
        this.npubigeoid = npubigeoid;
    }

    public String getCodigoinei() {
        return codigoinei;
    }

    public void setCodigoinei(String codigoinei) {
        this.codigoinei = codigoinei;
    }

    @Override
    public String toString() {
        return "NewUbigeoBean [nombre=" + nombre + ", dep=" + dep + ", prov=" + prov + ", dist=" + dist
                + ", npubigeoid=" + npubigeoid + ", codigoinei=" + codigoinei + "]";
    }

}

