package pe.com.nextel.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RequestApportionmentBean implements Serializable {

	/**
	 * 
	 */
	private String trxId;
	private String nroDocument;
	private String typeDocument; //JBALCAZAR PRY-1002	
	private String customerId;
	private String siteId; //JBALCAZAR PRY-1002	
	private String billCycle;
	private String user;
	private String accion;
	private String orderId;
	private static final long serialVersionUID = 1L;
	private List<ApportionmentBean> items = new ArrayList<ApportionmentBean>();

	public List<ApportionmentBean> getItems() {
		return items;
	}

	public void setItems(List<ApportionmentBean> items) {
		this.items = items;
	}

	public String getTrxId() {
		return trxId;
	}

	public void setTrxId(String trxId) {
		this.trxId = trxId;
	}

	public String getNroDocument() {
		return nroDocument;
	}

	public void setNroDocument(String nroDocument) {
		this.nroDocument = nroDocument;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getBillCycle() {
		return billCycle;
	}

	public void setBillCycle(String billCycle) {
		this.billCycle = billCycle;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}
	
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getTypeDocument() {
		return typeDocument;
	}

	public void setTypeDocument(String typeDocument) {
		this.typeDocument = typeDocument;
	}

	@Override
	public String toString() {
		return "RequestApportionmentBean [trxId=" + trxId + ", nroDocument=" + nroDocument + ", typeDocument="
				+ typeDocument + ", customerId=" + customerId + ", siteId=" + siteId + ", billCycle=" + billCycle
				+ ", user=" + user + ", accion=" + accion + ", orderId=" + orderId + ", items=" + items + "]";
	}

}
