package pe.com.nextel.bean;

import java.util.Date;

public class NoBiometricValidationBean extends ValidationBean {

    private String nroPreguntaAcertada;
    
    public NoBiometricValidationBean() {
        super();
    }
    
    public NoBiometricValidationBean(String verificationResult, String source, Date verificationDate, String verificationMotive, String nroPreguntaAcertada) {
        super(verificationResult, source, verificationDate, verificationMotive);
        this.nroPreguntaAcertada = nroPreguntaAcertada;
    }
    
    public String getNroPreguntaAcertada() {
        return nroPreguntaAcertada;
    }

    public void setNroPreguntaAcertada(String nroPreguntaAcertada) {
        this.nroPreguntaAcertada = nroPreguntaAcertada;
    }
}
