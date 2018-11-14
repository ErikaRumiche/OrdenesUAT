package pe.com.nextel.bean;

import java.io.Serializable;

public class ApportionmentBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String trxId;
	private long itemId;
	private String cicloOrigen;
	private String cicloDestino;
	private String tmCode;
	private String templateId;
	private String price;
	private String priceIgv;
	private String roundedPrice;
	private String planId;
	private double igv;
	private int quantity;
	 
	public String getTrxId() {
		return trxId;
	}

	public void setTrxId(String trxId) {
		this.trxId = trxId;
	}

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	public String getCicloOrigen() {
		return cicloOrigen;
	}

	public void setCicloOrigen(String cicloOrigen) {
		this.cicloOrigen = cicloOrigen;
	}

	public String getCicloDestino() {
		return cicloDestino;
	}

	public void setCicloDestino(String cicloDestino) {
		this.cicloDestino = cicloDestino;
	}

	public String getTmCode() {
		return tmCode;
	}

	public void setTmCode(String tmCode) {
		this.tmCode = tmCode;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getRoundedPrice() {
		return roundedPrice;
	}

	public void setRoundedPrice(String roundedPrice) {
		this.roundedPrice = roundedPrice;
	}

	public String getPriceIgv() {
		return priceIgv;
	}

	public void setPriceIgv(String priceIgv) {
		this.priceIgv = priceIgv;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public double getIgv() {
		return igv;
	}

	public void setIgv(double igv) {
		this.igv = igv;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
		
}