package pe.com.nextel.bean;

import java.util.Date;

public class BiometricValidationBean extends ValidationBean {

    public BiometricValidationBean() {
        super();
    }
    
    public BiometricValidationBean(String verificationResult, String source, Date verificationDate, String verificationMotive) {
        super(verificationResult, source, verificationDate, verificationMotive);
    }
}
