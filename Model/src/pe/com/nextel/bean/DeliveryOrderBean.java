package pe.com.nextel.bean;

import java.io.Serializable;

import pe.com.nextel.util.GenericObject;


public class DeliveryOrderBean extends GenericObject implements Serializable {
  private static final long serialVersionUID = 1L;
	private String deliveryOrderId;
	private String orderId;
	private String quantityDocument;
	private String quantityReceipt;
	private String status;
	private String error;
	private String errorMessage;
	private String createdBy;
	private String createdDate;
	private String modifiedBy;
	private String modifiedDate;
	private String notificado;
	private String operationId;

	public String getDeliveryOrderId() {
		return deliveryOrderId;
	}

	public void setDeliveryOrderId(String deliveryOrderId) {
		this.deliveryOrderId = deliveryOrderId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getQuantityDocument() {
		return quantityDocument;
	}

	public void setQuantityDocument(String quantityDocument) {
		this.quantityDocument = quantityDocument;
	}

	public String getQuantityReceipt() {
		return quantityReceipt;
	}

	public void setQuantityReceipt(String quantityReceipt) {
		this.quantityReceipt = quantityReceipt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getNotificado() {
		return notificado;
	}

	public void setNotificado(String notificado) {
		this.notificado = notificado;
	}

	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

}