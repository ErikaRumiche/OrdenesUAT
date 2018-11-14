package pe.com.nextel.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by LROQUE on 28/10/2016.
 * [PM0011173] LROQUE
 */
public class OrdenDocDigitalBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long orderId;
    private Long generatorId;
    private String type;
    private String specification;
    private String parameterName;
    private String createBy;
    private Date orderDate;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getGeneratorId() {
        return generatorId;
    }

    public void setGeneratorId(Long generatorId) {
        this.generatorId = generatorId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        return "OrdenDocDigitalBean{" +
                "orderId=" + orderId +
                ", generatorId=" + generatorId +
                ", type='" + type + '\'' +
                ", specification='" + specification + '\'' +
                ", parameterName='" + parameterName + '\'' +
                ", createBy='" + createBy + '\'' +
                ", orderDate=" + orderDate +
                '}';
    }
}
