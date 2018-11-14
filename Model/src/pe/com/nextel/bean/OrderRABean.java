package pe.com.nextel.bean;

import java.io.Serializable;

import pe.com.nextel.util.GenericObject;


public class OrderRABean extends GenericObject implements Serializable{	
    private static final long serialVersionUID = 1L;
    
    private String npCodProd;
    private String npCodPlan;
    private String npDscProd;
    private String npDscPlan;
    private String npCantRA;
    private String npEstado;
    private String npIndExiste;
    private String npUserId;
    private String npInvisible;
    
    public String getNpCodProd() {
        return npCodProd;
    }
    
    public void setNpCodProd(String npCodProd) {
        this.npCodProd = npCodProd;
    }

    public String getNpCodPlan() {
        return npCodPlan;
    }
    
    public void setNpCodPlan(String npCodPlan) {
        this.npCodPlan = npCodPlan;
    }	
    
    public String getNpDscProd() {
        return npDscProd;
    }
    
    public void setNpDscProd(String npDscProd) {
        this.npDscProd = npDscProd;
    }

    public String getNpDscPlan() {
        return npDscPlan;
    }
    
    public void setNpDscPlan(String npDscPlan) {
        this.npDscPlan = npDscPlan;
    }
    
    public String getNpCantRA() {
        return npCantRA;
    }
    
    public void setNpCantRA(String npCantRA) {
        this.npCantRA = npCantRA;
    }
    
    public String getNpEstado() {
        return npEstado;
    }
    
    public void setNpEstado(String npEstado) {
        this.npEstado = npEstado;
    }
    
    public String getNpIndExiste() {
        return npIndExiste;
    }
    
    public void setNpIndExiste(String npIndExiste) {
        this.npIndExiste = npIndExiste;
    }

	public String getNpUserId() {
		return npUserId;
	}

	public void setNpUserId(String npUserId) {
		this.npUserId = npUserId;
	}

	public String getNpInvisible() {
		return npInvisible;
	}

	public void setNpInvisible(String npInvisible) {
		this.npInvisible = npInvisible;
	}    

}
