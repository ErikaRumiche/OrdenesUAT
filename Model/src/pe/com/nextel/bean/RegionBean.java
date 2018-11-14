package pe.com.nextel.bean;

import pe.com.nextel.util.GenericObject;

import java.io.Serializable;

public class RegionBean extends GenericObject implements Serializable {
    private static final long serialVersionUID = 1L;
    private int regionId;
    private String regionName;

    public RegionBean(){

    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
}

