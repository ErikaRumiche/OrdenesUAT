package pe.com.nextel.bean;

import java.io.Serializable;

import pe.com.nextel.util.GenericObject;


public class CheckpointBean extends GenericObject implements Serializable {
  private static final long serialVersionUID = 1L;
	private long operationId;
	private int customerId;
	private String numberGuide;
	private int[] codeCheckpoint;
	private String[] descriptionCheckpoint;
	
	public long getOperationId() {
		return operationId;
	}

	public void setOperationId(long operationId) {
		this.operationId = operationId;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getNumberGuide() {
		return numberGuide;
	}

	public void setNumberGuide(String numberGuide) {
		this.numberGuide = numberGuide;
	}

	public int[] getCodeCheckpoint() {
		return codeCheckpoint;
	}

	public void setCodeCheckpoint(int[] codeCheckpoint) {
		this.codeCheckpoint = codeCheckpoint;
	}

	public String[] getDescriptionCheckpoint() {
		return descriptionCheckpoint;
	}

	public void setDescriptionCheckpoint(String[] descriptionCheckpoint) {
		this.descriptionCheckpoint = descriptionCheckpoint;
	}

	public boolean validateCheckpoint() {
		boolean flag = false;
		if (codeCheckpoint != null && descriptionCheckpoint != null) {
			flag = codeCheckpoint.length == descriptionCheckpoint.length;
		}
		return flag;
	}
}
