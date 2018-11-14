package pe.com.nextel.bean;

import pe.com.nextel.util.GenericObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IdentityVerificationDetailBean extends GenericObject implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombres;
    private String apellidos;
    private String tipoDocumento;
    private String nroDocumento;
    private String tipoVerificacionExitosa;
    private String registradoPor;
    private String modificadoPor;

    private List<BiometricValidationBean> listBiometricValidation;
    private List<NoBiometricValidationBean> listNoBiometricValidation;
    private List<ExternalValidationBean> listExternalValidation;

    public IdentityVerificationDetailBean() {
        listBiometricValidation = new ArrayList<BiometricValidationBean>();
        listNoBiometricValidation = new ArrayList<NoBiometricValidationBean>();
        listExternalValidation = new ArrayList<ExternalValidationBean>();
    }

    public IdentityVerificationDetailBean(String nombres, String apellidos, String tipoDocumento, String nroDocumento, String tipoVerificacionExitosa, String registradoPor, String modificadoPor) {
        this.setNombres(nombres);
        this.setApellidos(apellidos);
        this.setTipoDocumento(tipoDocumento);
        this.setNroDocumento(nroDocumento);
        this.setTipoVerificacionExitosa(tipoVerificacionExitosa);
        this.setRegistradoPor(registradoPor);
        this.setModificadoPor(modificadoPor);
        listBiometricValidation = new ArrayList<BiometricValidationBean>();
        listNoBiometricValidation = new ArrayList<NoBiometricValidationBean>();
        listExternalValidation = new ArrayList<ExternalValidationBean>();
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNroDocumento() {
        return nroDocumento;
    }

    public void setNroDocumento(String nroDocumento) {
        this.nroDocumento = nroDocumento;
    }

    public String getTipoVerificacionExitosa() {
        return tipoVerificacionExitosa;
    }

    public void setTipoVerificacionExitosa(String tipoVerificacionExitosa) {
        this.tipoVerificacionExitosa = tipoVerificacionExitosa;
    }

    public String getRegistradoPor() {
        return registradoPor;
    }

    public void setRegistradoPor(String registradoPor) {
        this.registradoPor = registradoPor;
    }

    public String getModificadoPor() {
        return modificadoPor;
    }

    public void setModificadoPor(String modificadoPor) {
        this.modificadoPor = modificadoPor;
    }

    public List<BiometricValidationBean> getListBiometricValidation() {
        return listBiometricValidation;
    }

    public void setListBiometricValidation(List<BiometricValidationBean> listBiometricValidation) {
        this.listBiometricValidation = listBiometricValidation;
    }

    public List<NoBiometricValidationBean> getListNoBiometricValidation() {
        return listNoBiometricValidation;
    }

    public void setListNoBiometricValidation(List<NoBiometricValidationBean> listNoBiometricValidation) {
        this.listNoBiometricValidation = listNoBiometricValidation;
    }

	public List<ExternalValidationBean> getListExternalValidation() {
		return listExternalValidation;
	}

	public void setListExternalValidation(
			List<ExternalValidationBean> listExternalValidation) {
		this.listExternalValidation = listExternalValidation;
	}
}
