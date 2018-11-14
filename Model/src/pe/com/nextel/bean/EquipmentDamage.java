package pe.com.nextel.bean;

import java.io.Serializable;

public class EquipmentDamage  implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer npequipmentdamageid;
    private Integer npreplistid;
    private String nprow;
    private String npcolumn;
    private String npdamagecode;

    public void setNpequipmentdamageid(Integer npequipmentdamageid) {
        this.npequipmentdamageid = npequipmentdamageid;
    }

    public Integer getNpequipmentdamageid() {
        return npequipmentdamageid;
    }

    public void setNpreplistid(Integer npreplistid) {
        this.npreplistid = npreplistid;
    }

    public Integer getNpreplistid() {
        return npreplistid;
    }

    public void setNprow(String nprow) {
        this.nprow = nprow;
    }

    public String getNprow() {
        return nprow;
    }

    public void setNpcolumn(String npcolumn) {
        this.npcolumn = npcolumn;
    }

    public String getNpcolumn() {
        return npcolumn;
    }

    public void setNpdamagecode(String npdamagecode) {
        this.npdamagecode = npdamagecode;
    }

    public String getNpdamagecode() {
        return npdamagecode;
    }
}
