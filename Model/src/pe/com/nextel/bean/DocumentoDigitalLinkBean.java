package pe.com.nextel.bean;

import java.io.Serializable;

/**
 * Created by LROQUE on 25/10/2016.
 * [PM0011173] LROQUE
 */
public class DocumentoDigitalLinkBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idDocAcepta;
    private String nombreDoc;

    public String getIdDocAcepta() {
        return idDocAcepta;
    }

    public void setIdDocAcepta(String idDocAcepta) {
        this.idDocAcepta = idDocAcepta;
    }

    public String getNombreDoc() {
        return nombreDoc;
    }

    public void setNombreDoc(String nombreDoc) {
        this.nombreDoc = nombreDoc;
    }

    @Override
    public String toString() {
        return "DocumentoDigitalLinkBean{" +
                "idDocAcepta='" + idDocAcepta + '\'' +
                ", nombreDoc='" + nombreDoc + '\'' +
                '}';
    }
}
