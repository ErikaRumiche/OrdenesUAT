package pe.com.nextel.bean;

/**
 * Created by HP on 17/05/2017.
 */
public class PopulateCenterDetBean {
    private String ubigeo;
    private String cpufCode;
    private int isHome;
    private int isCoverage;
    private String createdBy;
    private String department;
    private String province;
    private String district;

    public String getUbigeo() {
        return ubigeo;
    }

    public void setUbigeo(String ubigeo) {
        this.ubigeo = ubigeo;
    }

    public String getCpufCode() {
        return cpufCode;
    }

    public void setCpufCode(String cpufCode) {
        this.cpufCode = cpufCode;
    }

    public int getIsHome() {
        return isHome;
    }

    public void setIsHome(int isHome) {
        this.isHome = isHome;
    }

    public int getIsCoverage() {
        return isCoverage;
    }

    public void setIsCoverage(int isCoverage) {
        this.isCoverage = isCoverage;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Override
    public String toString() {
        return "PopulateCenterDetBean{" +
                "ubigeo='" + ubigeo + '\'' +
                ", cpufCode='" + cpufCode + '\'' +
                ", isHome=" + isHome +
                ", isCoverage=" + isCoverage +
                ", createdBy='" + createdBy + '\'' +
                ", department='" + department + '\'' +
                ", province='" + province + '\'' +
                ", district='" + district + '\'' +
                '}';
    }
}
