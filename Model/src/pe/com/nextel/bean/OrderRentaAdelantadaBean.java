package pe.com.nextel.bean;

import java.io.Serializable;
import java.sql.Date;

import pe.com.nextel.util.GenericObject;

public class OrderRentaAdelantadaBean extends GenericObject implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private long npOrderRentaAdelantadaId;
	private long npOrderId;
	private long npOrderRefRentaAdelantadaId;
	private Date npCreatedDate;
    private String npCreatedBy;
    private Date npModificationDate;
    private String npModificationBy;
    
	public long getNpOrderRentaAdelantadaId() {
		return npOrderRentaAdelantadaId;
	}
	public void setNpOrderRentaAdelantadaId(long npOrderRentaAdelantadaId) {
		this.npOrderRentaAdelantadaId = npOrderRentaAdelantadaId;
	}
	public long getNpOrderId() {
		return npOrderId;
	}
	public void setNpOrderId(long npOrderId) {
		this.npOrderId = npOrderId;
	}
	public long getNpOrderRefRentaAdelantadaId() {
		return npOrderRefRentaAdelantadaId;
	}
	public void setNpOrderRefRentaAdelantadaId(long npOrderRefRentaAdelantadaId) {
		this.npOrderRefRentaAdelantadaId = npOrderRefRentaAdelantadaId;
	}
	public Date getNpCreatedDate() {
		return npCreatedDate;
	}
	public void setNpCreatedDate(Date npCreatedDate) {
		this.npCreatedDate = npCreatedDate;
	}
	public String getNpCreatedBy() {
		return npCreatedBy;
	}
	public void setNpCreatedBy(String npCreatedBy) {
		this.npCreatedBy = npCreatedBy;
	}
	public Date getNpModificationDate() {
		return npModificationDate;
	}
	public void setNpModificationDate(Date npModificationDate) {
		this.npModificationDate = npModificationDate;
	}
	public String getNpModificationBy() {
		return npModificationBy;
	}
	public void setNpModificationBy(String npModificationBy) {
		this.npModificationBy = npModificationBy;
	}
		
}
