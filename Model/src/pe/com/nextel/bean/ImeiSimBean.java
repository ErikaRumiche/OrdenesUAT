package pe.com.nextel.bean;

import java.io.Serializable;

import pe.com.nextel.util.GenericObject;

public class ImeiSimBean extends GenericObject implements Serializable {

    private static final long serialVersionUID = 1L;
	private String imei;
	private String sim;
	private String message;

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getSim() {
		return sim;
	}

	public void setSim(String sim) {
		this.sim = sim;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
