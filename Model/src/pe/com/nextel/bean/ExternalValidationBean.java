package pe.com.nextel.bean;

import java.util.Date;

public class ExternalValidationBean extends ValidationBean {

	private static final long serialVersionUID = 1L;
	
	private String documentNumber;
    private String fullName;
    private String provider;
    private String codeVerificationExt;
    
    public ExternalValidationBean() {
        super();
    }
    
    public ExternalValidationBean(String verificationResult, String source, Date verificationDate, String verificationMotive) {
        super(verificationResult, source, verificationDate, verificationMotive);
    }

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getCodeVerificationExt() {
		return codeVerificationExt;
	}

	public void setCodeVerificationExt(String codeVerificationExt) {
		this.codeVerificationExt = codeVerificationExt;
	}
    
}
