package pe.com.nextel.bean;

import java.io.Serializable;

/**
 * Created by huapaya on 02/09/2016.
 */
public class LegalRepresentativeBean implements Serializable {

    private String nomLegalRep;
    private String typeDocument;
    private int valTypeDoc;
    private String numberDocument;

    public String getTypeDocument() {
        return typeDocument;
    }

    public void setTypeDocument(String typeDocument) {
        this.typeDocument = typeDocument;
    }

    public int getValTypeDoc() {
        return valTypeDoc;
    }

    public void setValTypeDoc(int valTypeDoc) {
        this.valTypeDoc = valTypeDoc;
    }

    public String getNumberDocument() {
        return numberDocument;
    }

    public void setNumberDocument(String numberDocument) {
        this.numberDocument = numberDocument;
    }

    public String getNomLegalRep() {
        return nomLegalRep;
    }

    public void setNomLegalRep(String nomLegalRep) {
        this.nomLegalRep = nomLegalRep;
    }
}