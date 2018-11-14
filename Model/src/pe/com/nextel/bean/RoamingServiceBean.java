package pe.com.nextel.bean;

import java.math.BigDecimal;

/**
 * Created by montoymi on 14/08/2015.
 */
public class RoamingServiceBean extends ServiciosBean {
    private String bagCode;
    private String bagType;
    private int planType;
    private BigDecimal price;
    private int validity;

    public String getBagCode() {
        return bagCode;
    }

    public void setBagCode(String bagCode) {
        this.bagCode = bagCode;
    }

    public String getBagType() {
        return bagType;
    }

    public void setBagType(String bagType) {
        this.bagType = bagType;
    }

    public int getPlanType() {
        return planType;
    }

    public void setPlanType(int planType) {
        this.planType = planType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getValidity() {
        return validity;
    }

    public void setValidity(int validity) {
        this.validity = validity;
    }
}
