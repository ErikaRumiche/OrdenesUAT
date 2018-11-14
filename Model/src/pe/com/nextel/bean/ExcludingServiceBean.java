package pe.com.nextel.bean;

public class ExcludingServiceBean {
    private String servicePrincipal;
    private String serviceExcluded;
    private String serviceMessage;


    public String getServicePrincipal() {
        return servicePrincipal;
    }

    public void setServicePrincipal(String servicePrincipal) {
        this.servicePrincipal = servicePrincipal;
    }

    public String getServiceExcluded() {
        return serviceExcluded;
    }

    public void setServiceExcluded(String serviceExcluded) {
        this.serviceExcluded = serviceExcluded;
    }

    public String getServiceMessage() {
        return serviceMessage;
    }

    public void setServiceMessage(String serviceMessage) {
        this.serviceMessage = serviceMessage;
    }

    @Override
    public String toString() {
        return "ExcludingServiceBean{" +
                "servicePrincipal='" + servicePrincipal + '\'' +
                ", serviceExcluded='" + serviceExcluded + '\'' +
                ", serviceMessage='" + serviceMessage + '\'' +
                '}';
    }
}
