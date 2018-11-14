package pe.com.nextel.bean;

import pe.com.nextel.util.GenericObject;

import java.io.Serializable;

public class IsolatedVerifConfigBean extends GenericObject implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer spec;
    private Integer isCompany;

    public IsolatedVerifConfigBean(){

    }

    public Integer getSpec() {
        return spec;
    }

    public void setSpec(Integer spec) {
        this.spec = spec;
    }

    public Integer getIsCompany() {
        return isCompany;
    }

    public void setIsCompany(Integer isCompany) {
        this.isCompany = isCompany;
    }

}
