package pe.com.nextel.bean;

import java.io.Serializable;
import pe.com.nextel.util.GenericObject;

public class OrderDetailBean extends GenericObject implements Serializable {
      private static final long serialVersionUID = 1L;
      private long npOrderId;
      private long npCustomerId;
      private long npSiteId;      
      private long npBuildingId;     
      private String npDescription; //descripcion de la orden   
      private String swName;//razon social
      private String npShortName;//nombre de la Tienda
      private String npTypeService;//nombre de la Tienda
      private long n_isSamebuilding;
      
    //INICIO: PRY-0864_2 | AMENDEZ
    private String message;
    private int npvep;
    private double npvepaymenttotal;
    private double npinitialquota;
    private int npvepquantityquota;
    private double npamountfinanced;
    //FIN: PRY-0864_2 | AMENDEZ

    //INICIO: AMENDEZ | PRY-1141
    private double npqa;
    private long nppaymentorderquotaid;
    //FIN: AMENDEZ | PRY-1141

    public long getNpOrderId() {
        return npOrderId;
    }

    public void setNpOrderId(long npOrderId) {
        this.npOrderId = npOrderId;
    }

    public long getNpCustomerId() {
        return npCustomerId;
    }

    public void setNpCustomerId(long npCustomerId) {
        this.npCustomerId = npCustomerId;
    }

    public long getNpSiteId() {
        return npSiteId;
    }

    public void setNpSiteId(long npSiteId) {
        this.npSiteId = npSiteId;
    }

    public long getNpBuildingId() {
        return npBuildingId;
    }

    public void setNpBuildingId(long npBuildingId) {
        this.npBuildingId = npBuildingId;
    }

    public String getNpDescription() {
        return npDescription;
    }

    public void setNpDescription(String npDescription) {
        this.npDescription = npDescription;
    }

    public String getSwName() {
        return swName;
    }

    public void setSwName(String swName) {
        this.swName = swName;
    }


    public String getNpShortName() {
        return npShortName;
    }

    public void setNpShortName(String npShortName) {
        this.npShortName = npShortName;
    }

    public void setNpTypeService(String npTypeService) {
        this.npTypeService = npTypeService;
    }
    
    public String getNpTypeService() {
        return npTypeService;
    }

    public long getN_isSamebuilding() {
        return n_isSamebuilding;
    }

    public void setN_isSamebuilding(long n_isSamebuilding) {
        this.n_isSamebuilding = n_isSamebuilding;
    }

    //INICIO: PRY-0864_2 | AMENDEZ
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getNpvep() {
        return npvep;
    }

    public void setNpvep(int npvep) {
        this.npvep = npvep;
    }

    public double getNpvepaymenttotal() {
        return npvepaymenttotal;
    }

    public void setNpvepaymenttotal(double npvepaymenttotal) {
        this.npvepaymenttotal = npvepaymenttotal;
    }

    public double getNpinitialquota() {
        return npinitialquota;
    }

    public void setNpinitialquota(double npinitialquota) {
        this.npinitialquota = npinitialquota;
    }

    public int getNpvepquantityquota() {
        return npvepquantityquota;
    }

    public void setNpvepquantityquota(int npvepquantityquota) {
        this.npvepquantityquota = npvepquantityquota;
    }

    public double getNpamountfinanced() {
        return npamountfinanced;
    }

    public void setNpamountfinanced(double npamountfinanced) {
        this.npamountfinanced = npamountfinanced;
    }
    //FIN: PRY-0864_2 | AMENDEZ

    //INICIO: AMENDEZ | PRY-1141
    public double getNpqa() {
        return npqa;
    }

    public void setNpqa(double npqa) {
        this.npqa = npqa;
    }

    public long getNppaymentorderquotaid() {
        return nppaymentorderquotaid;
    }

    public void setNppaymentorderquotaid(long nppaymentorderquotaid) {
        this.nppaymentorderquotaid = nppaymentorderquotaid;
    }
    //FIN: AMENDEZ | PRY-1141

}
