package pe.com.nextel.bean;

import java.io.Serializable;

/**
 * JCURI PRY-0890
 */
public class ResponseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private long status;
	private String message;
	private Object data;
    
	public long getStatus() {
		return status;
	}

	public void setStatus(long status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
