package pe.com.nextel.bean;

import pe.com.nextel.util.GenericObject;

import java.io.Serializable;
import java.util.Date;

public class IsolatedVerificationBean extends GenericObject implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer npverificationid;
    private String nptipodocument;
    private String npnrodocument;
    private String customer_name;
    private Date np_date_end_validity;
    private Date np_date_of_use;
    private Integer nptransaction;
    private String nptypetransaction;
    private String npverificationtype;

    private Integer isSelected;
    private String date_end_validity;
    private String date_of_use;

    public IsolatedVerificationBean() {

    }

    public Integer getNpverificationid() {
        return npverificationid;
    }

    public void setNpverificationid(Integer npverificationid) {
        this.npverificationid = npverificationid;
    }

    public String getNptipodocument() {
        return nptipodocument;
    }

    public void setNptipodocument(String nptipodocument) {
        this.nptipodocument = nptipodocument;
    }

    public String getNpnrodocument() {
        return npnrodocument;
    }

    public void setNpnrodocument(String npnrodocument) {
        this.npnrodocument = npnrodocument;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public Date getNp_date_end_validity() {
        return np_date_end_validity;
    }

    public void setNp_date_end_validity(Date np_date_end_validity) {
        this.np_date_end_validity = np_date_end_validity;
    }

    public Date getNp_date_of_use() {
        return np_date_of_use;
    }

    public void setNp_date_of_use(Date np_date_of_use) {
        this.np_date_of_use = np_date_of_use;
    }

    public Integer getNptransaction() {
        return nptransaction;
    }

    public void setNptransaction(Integer nptransaction) {
        this.nptransaction = nptransaction;
    }

    public String getNptypetransaction() {
        return nptypetransaction;
    }

    public void setNptypetransaction(String nptypetransaction) {
        this.nptypetransaction = nptypetransaction;
    }

    public Integer getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Integer isSelected) {
        this.isSelected = isSelected;
    }

    public String getDate_end_validity() {
        return date_end_validity;
    }

    public void setDate_end_validity(String date_end_validity) {
        this.date_end_validity = date_end_validity;
    }

    public String getDate_of_use() {
        return date_of_use;
    }

    public void setDate_of_use(String date_of_use) {
        this.date_of_use = date_of_use;
    }

    public String getNpverificationtype() {
        return npverificationtype;
    }

    public void setNpverificationtype(String npverificationtype) {
        this.npverificationtype = npverificationtype;
    }

    @Override
    public String toString() {
        return "IsolatedVerificationBean{" +
                "npverificationid=" + npverificationid +
                ", nptipodocument='" + nptipodocument + '\'' +
                ", npnrodocument='" + npnrodocument + '\'' +
                ", customer_name='" + customer_name + '\'' +
                ", np_date_end_validity=" + np_date_end_validity +
                ", np_date_of_use=" + np_date_of_use +
                ", nptransaction=" + nptransaction +
                ", nptypetransaction='" + nptypetransaction + '\'' +
                ", npverificationtype=" +npverificationtype+ '\'' +
                ", isSelected=" + isSelected +
                ", date_end_validity='" + date_end_validity + '\'' +
                ", date_of_use='" + date_of_use + '\'' +
                '}';
    }
}
