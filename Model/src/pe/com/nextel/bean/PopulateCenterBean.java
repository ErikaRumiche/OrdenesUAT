package pe.com.nextel.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 17/05/2017.
 */
public class PopulateCenterBean {
    private Integer cpufId;
    private Integer response;
    private Integer acceptterms;
    private Long orderId;
    private List<PopulateCenterDetBean> populateCenterDetBeanList = new ArrayList<PopulateCenterDetBean>();

    public Integer getCpufId() {
        return cpufId;
    }

    public void setCpufId(Integer cpufId) {
        this.cpufId = cpufId;
    }

    public Integer getResponse() {
        return response;
    }

    public void setResponse(Integer response) {
        this.response = response;
    }

    public Integer getAcceptterms() {
        return acceptterms;
    }

    public void setAcceptterms(Integer acceptterms) {
        this.acceptterms = acceptterms;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public List<PopulateCenterDetBean> getPopulateCenterDetBeanList() {
        return populateCenterDetBeanList;
    }

    public void setPopulateCenterDetBeanList(List<PopulateCenterDetBean> populateCenterDetBeanList) {
        this.populateCenterDetBeanList = populateCenterDetBeanList;
    }

    @Override
    public String toString() {
        return "PopulateCenterBean{" +
                "cpufId=" + cpufId +
                ", response=" + response +
                ", acceptterms=" + acceptterms +
                ", orderId=" + orderId +
                ", populateCenterDetBeanList=" + populateCenterDetBeanList +
                '}';
    }
}
