package pe.com.nextel.bean;

import pe.com.nextel.util.GenericObject;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;


/**
 * Motivo: Mapeo de los campos de la tabla ORDERS.NP_ITEM
 * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
 * <br>Fecha: 30/10/2007
 */
public class ItemBean extends GenericObject implements Serializable {
    private static final long serialVersionUID = 1L;
    private long npitemid;
    private long nporderid;
    private long npplanid;
    private long npsharedinstalationid;
    private long npconceptid;
    private long npproductlineid;
    private long npproductid;
    private long nparearespdev;
    private long npprovidergrpiddev;
    private long nppromotionid;
    private long npaddendumid;
    private long nporiginalplanid;
    private long nporiginalproductlineid;
    private long nporiginalproductid;
    private long npinstallationaddressid;
    private long nppayorderid;
    private long npnewaddress;
    private long npsolutionid;
    private String npsolutionname;
    private long nporiginalsolutionid;
    private String nporiginalsolutionname;

    private long nporigmainservice;
    private long npnewmainservice;

    private int npoccurrence;
    private int npquantity;
    private int nptimestamp;
    private int npcontractnumber;
    private int npnetworkhosttype;

    private double npminuteprice;
    private double npexceptionrevenuediscount;
    private double npexceptionrentdiscount;

    private String nprent;
    private String npdiscount;
    private String npprice;

    private String npinstalationprice;
    private String npinstalationexception;
    private String npreferencephone;


    private Date npequipmentreturndate;
    private Timestamp npfeasibilityprogdate;
    private Timestamp npinstalationprogdate;
    private Date npmodificationdate;
    private Date npcreateddate;

    private Date npendservicedate;


    private String npfeasibility;
    private String npinstalation;
    private String nporigmainservicedesc;
    private String npnewmainservicedesc;
    private String npcurrency;
    private String npwarrant;
    private String npwarrantdesc;
    private String nporiginalphone;
    private String nporiginalplanname;
    private String nporiginalproductname;
    private String npownimeinumber;
    private String npinventorycode;
    private String npequipmentreturn;
    private String npequipmentreturndesc;
    private String npequipmentnotyetgiveback;
    private String npexception;
    private String npexceptionrevenue;
    private String npexceptionrent;
    private String npexceptionminadidispatch;
    private String npexceptionminaditelephony;
    private String npkit;
    private String npinvoicenumber;
    private String npguidenumber;
    private String nppaymentconcept;
    private String npcontractstatus;
    private String npdescription;
    private String nplinktype;
    private String npphonenumber1;
    private String npphonenumber2;
    private String npcontactname;
    private String npmodificationby;
    private String npcreatedby;
    private String npproductname;
    private String npitemservices;
    private String npitembillingaccount;
    private String npitemfreeservices;
    private String npitemservicescost;
    private String npplanname;
    private String npsharedinstal;
    private String npsharedinstaldesc;
    private String npfirstcontract;
    private String npfirstcontractdesc;
    private String npnewphone;
    private String npmodel;
    private String npmodelid; //jsalazar servicios adicionales 06/12/2010
    private String npminutesrate;
    private String npmodalitysell;
    private String npequipment;
    private String npphone;
    private String npserialnumber;
    private String npimeinumber;
    private String npsiminumber;
    private String npproductlinename;
    private String nppriceexception;
    private String nparearespdevname;
    private String npprovidergrpiddevname;
    private String npnetworkhosttypedesc;
    private String nplinktypedesc;
    private String npaditionalcost;
    private String npfeasibilitydesc;
    private String npitemaddendumtemplates;

    private String nporiginalprice;
    private String nppricetype;
    private long nppricetypeid;
    private long nppricetypeitemid;

    private String npfixedphone;
    private String nplocution;
    private String npitemoperationstatus;
    private long npsiteid;
    private String npsitename;
    private long npinternetrefcontract;
    private long npdatosrefcontract;
    private String nptfrefphonenumber;

    private String npestadoitemId;
    private String npestadoitemDesc;
    private String npestadoproceso;
    private String npcobro;

    private long npcontractref;

    private int nptypeip;
    private String nptypeipdesc;
    private String npradio;
    private String nppayrespname;
    private String npcustcode;
    private String npssaacontratado;
    private long npbscspaymntrespcustomeridId;

    //CPUENTE CAP & CAL
    private int npequipmenttime;
    private int npownequipment;
    private String npreasonchange;

    //CPUENTE2 CAP & CAL
    private String npestadoContrato;
    private String npmotivoEstado;
    private String npfechaCambioEstado;

    //CLOZZA
    private ArrayList npServiceItemList;

    private long npcountreasonId;

    //PHIDALGO
    private int npLevel;
    private String npLevelDesc;

    private String npstatusaplication;
    private long npbusinesssolutionid;

    private String npimeicustomer;
    private String npownimeisim;

    //DLAZO
    private Date npactivationdate;
    private Date npdeactivationdate;
    private String npmissingdays;

    //FPICOY
    private long npproductmodelid;
    private String cadNumberReserve;

    //CLOZZA
    private String npAplicarVO;
    private int npIndice;

    private int npVepItem;
    private double npVepTotalPrice;

    // MMONTOYA - Despacho en tienda
    private String npproductstatus;  // Nuevos/Usados
    private String npchanged;        // Si/No
    private String npflagaccessory;  // Si/No

    // MMONTOYA - ADT-RCT-092
    private String npserviceROA; // Es solo para visualizar la descripción del servicio en la grilla. Para grabar se usa el mismo de servicios adicionales: npitemservices.
    private int npservplantype;
    private String npservbagcode;
    private String npservbagtype;
    private Date npservvalidactivationdate;
    private int npservvalidity;

    private int nptypeproductBC; // ADT-BCL-083

	private int npkeepSIM; //CDM+CDP EFLORES Parametro que indica si se mantiene el SIM para una activacion ordenes CMT
    private int npzonacoberturaid; //PRY-0721 DERAZO Id de la region(PRODUCT.NP_ZONA_COBERTURA) para productos configurados
    private String npnombrezona; ///PRY-0721 DERAZO Nombre de la region

    private int npProvinceZoneId; //EFLORES - BAFI2
    private String npNameProvinceZone;//EFLORES - BAFI2
    private int npDistrictZoneId; //EFLORES - BAFI2
    private String npNameDistrictZone; //EFLORES - BAFI2

    private Integer npcantidadRentaAdelantada; // PRY-0762 JQUISPE cantidad de Renta Adelantada
    private String nptotalRentaAdelantada; // PRY-0762 JQUISPE total de Renta Adelantada

    private String npprorrateoPrice; // PRY-0890 JBALCAZAR Monto de Prorrateo
    private String nphndIndice; // PRY-0890 JBALCAZAR Indice 
        
    private String npOwnImeiFS; //DERAZO TDECONV003-2


    public void setNpitemid(long npitemid) {
        this.npitemid = npitemid;
    }

    public long getNpitemid() {
        return npitemid;
    }

    public void setNporderid(long nporderid) {
        this.nporderid = nporderid;
    }

    public long getNporderid() {
        return nporderid;
    }

    public void setNpmodalitysell(String npmodalitysell) {
        this.npmodalitysell = npmodalitysell;
    }

    public String getNpmodalitysell() {
        return npmodalitysell;
    }

    public void setNpsolutionid(long npsolutionid) {
        this.npsolutionid = npsolutionid;
    }

    public long getNpsolutionid() {
        return this.npsolutionid;
    }

    public void setNpsolutionname(String npsolutionname) {
        this.npsolutionname = npsolutionname;
    }

    public String getNpsolutionname() {
        return this.npsolutionname;
    }

    public void setNpequipment(String npequipment) {
        this.npequipment = npequipment;
    }

    public String getNpequipment() {
        return npequipment;
    }

    public void setNpphone(String npphone) {
        this.npphone = npphone;
    }

    public String getNpphone() {
        return npphone;
    }

    public void setNpserialnumber(String npserialnumber) {
        this.npserialnumber = npserialnumber;
    }

    public String getNpserialnumber() {
        return npserialnumber;
    }

    public void setNpimeinumber(String npimeinumber) {
        this.npimeinumber = npimeinumber;
    }

    public String getNpimeinumber() {
        return npimeinumber;
    }

    public void setNpsiminumber(String npsiminumber) {
        this.npsiminumber = npsiminumber;
    }

    public String getNpsiminumber() {
        return npsiminumber;
    }

    public void setNpplanid(long npplanid) {
        this.npplanid = npplanid;
    }

    public long getNpplanid() {
        return npplanid;
    }

    public void setNpplanname(String npplanname) {
        this.npplanname = npplanname;
    }

    public String getNpplanname() {
        return npplanname;
    }

    public void setNpproductlineid(long npproductlineid) {
        this.npproductlineid = npproductlineid;
    }

    public long getNpproductlineid() {
        return npproductlineid;
    }

    public void setNpproductid(long npproductid) {
        this.npproductid = npproductid;
    }

    public long getNpproductid() {
        return npproductid;
    }

    public void setNpquantity(int npquantity) {
        this.npquantity = npquantity;
    }

    public int getNpquantity() {
        return npquantity;
    }

    public void setNpprice(String npprice) {
        this.npprice = npprice;
    }

    public String getNpprice() {
        return npprice;
    }

    public void setNppriceexception(String nppriceexception) {
        this.nppriceexception = nppriceexception;
    }

    public String getNppriceexception() {
        return nppriceexception;
    }

    public void setNprent(String nprent) {
        this.nprent = nprent;
    }

    public String getNprent() {
        return nprent;
    }

    public void setNpdiscount(String npdiscount) {
        this.npdiscount = npdiscount;
    }

    public String getNpdiscount() {
        return npdiscount;
    }

    public void setNpcurrency(String npcurrency) {
        this.npcurrency = npcurrency;
    }

    public String getNpcurrency() {
        return npcurrency;
    }

    public void setNpwarrant(String npwarrant) {
        this.npwarrant = npwarrant;
    }

    public String getNpwarrant() {
        return npwarrant;
    }

    public void setNpoccurrence(int npoccurrence) {
        this.npoccurrence = npoccurrence;
    }

    public int getNpoccurrence() {
        return npoccurrence;
    }

    public void setNppromotionid(long nppromotionid) {
        this.nppromotionid = nppromotionid;
    }

    public long getNppromotionid() {
        return nppromotionid;
    }

    public void setNpaddendumid(long npaddendumid) {
        this.npaddendumid = npaddendumid;
    }

    public long getNpaddendumid() {
        return npaddendumid;
    }

    public void setNporiginalphone(String nporiginalphone) {
        this.nporiginalphone = nporiginalphone;
    }

    public String getNporiginalphone() {
        return nporiginalphone;
    }

    public void setNporiginalplanid(long nporiginalplanid) {
        this.nporiginalplanid = nporiginalplanid;
    }

    public long getNporiginalplanid() {
        return nporiginalplanid;
    }

    public void setNporiginalplanname(String nporiginalplanname) {
        this.nporiginalplanname = nporiginalplanname;
    }

    public String getNporiginalplanname() {
        return nporiginalplanname;
    }

    public void setNporiginalproductlineid(long nporiginalproductlineid) {
        this.nporiginalproductlineid = nporiginalproductlineid;
    }

    public long getNporiginalproductlineid() {
        return nporiginalproductlineid;
    }

    public void setNporiginalproductid(long nporiginalproductid) {
        this.nporiginalproductid = nporiginalproductid;
    }

    public long getNporiginalproductid() {
        return nporiginalproductid;
    }

    public void setNpownimeinumber(String npownimeinumber) {
        this.npownimeinumber = npownimeinumber;
    }

    public String getNpownimeinumber() {
        return npownimeinumber;
    }

    public void setNpinventorycode(String npinventorycode) {
        this.npinventorycode = npinventorycode;
    }

    public String getNpinventorycode() {
        return npinventorycode;
    }

    public void setNpequipmentreturn(String npequipmentreturn) {
        this.npequipmentreturn = npequipmentreturn;
    }

    public String getNpequipmentreturn() {
        return npequipmentreturn;
    }

    public void setNpequipmentnotyetgiveback(String npequipmentnotyetgiveback) {
        this.npequipmentnotyetgiveback = npequipmentnotyetgiveback;
    }

    public String getNpequipmentnotyetgiveback() {
        return npequipmentnotyetgiveback;
    }

    public void setNpequipmentreturndate(Date npequipmentreturndate) {
        this.npequipmentreturndate = npequipmentreturndate;
    }

    public Date getNpequipmentreturndate() {
        return npequipmentreturndate;
    }

    public void setNpexception(String npexception) {
        this.npexception = npexception;
    }

    public String getNpexception() {
        return npexception;
    }

    public void setNpexceptionrevenuediscount(double npexceptionrevenuediscount) {
        this.npexceptionrevenuediscount = npexceptionrevenuediscount;
    }

    public double getNpexceptionrevenuediscount() {
        return npexceptionrevenuediscount;
    }

    public void setNpexceptionrentdiscount(double npexceptionrentdiscount) {
        this.npexceptionrentdiscount = npexceptionrentdiscount;
    }

    public double getNpexceptionrentdiscount() {
        return npexceptionrentdiscount;
    }

    public void setNpexceptionminadidispatch(String npexceptionminadidispatch) {
        this.npexceptionminadidispatch = npexceptionminadidispatch;
    }

    public String getNpexceptionminadidispatch() {
        return npexceptionminadidispatch;
    }

    public void setNpexceptionminaditelephony(String npexceptionminaditelephony) {
        this.npexceptionminaditelephony = npexceptionminaditelephony;
    }

    public String getNpexceptionminaditelephony() {
        return npexceptionminaditelephony;
    }

    public void setNpinstallationaddressid(long npinstallationaddressid) {
        this.npinstallationaddressid = npinstallationaddressid;
    }

    public long getNpinstallationaddressid() {
        return npinstallationaddressid;
    }

    public void setNpkit(String npkit) {
        this.npkit = npkit;
    }

    public String getNpkit() {
        return npkit;
    }

    public void setNpinvoicenumber(String npinvoicenumber) {
        this.npinvoicenumber = npinvoicenumber;
    }

    public String getNpinvoicenumber() {
        return npinvoicenumber;
    }

    public void setNpguidenumber(String npguidenumber) {
        this.npguidenumber = npguidenumber;
    }

    public String getNpguidenumber() {
        return npguidenumber;
    }

    public void setNppaymentconcept(String nppaymentconcept) {
        this.nppaymentconcept = nppaymentconcept;
    }

    public String getNppaymentconcept() {
        return nppaymentconcept;
    }

    public void setNppayorderid(long nppayorderid) {
        this.nppayorderid = nppayorderid;
    }

    public long getNppayorderid() {
        return nppayorderid;
    }

    public void setNpinstalationprice(String npinstalationprice) {
        this.npinstalationprice = npinstalationprice;
    }

    public String getNpinstalationprice() {
        return npinstalationprice;
    }

    public void setNpinstalationexception(String npinstalationexception) {
        this.npinstalationexception = npinstalationexception;
    }

    public String getNpinstalationexception() {
        return npinstalationexception;
    }

    public void setNpfeasibility(String npfeasibility) {
        this.npfeasibility = npfeasibility;
    }

    public String getNpfeasibility() {
        return npfeasibility;
    }

    public void setNpinstalation(String npinstalation) {
        this.npinstalation = npinstalation;
    }

    public String getNpinstalation() {
        return npinstalation;
    }

    /*
    public void setNpfeasibilityprogdate(Date npfeasibilityprogdate) {
        this.npfeasibilityprogdate = npfeasibilityprogdate;
    }

    public Date getNpfeasibilityprogdate() {
        return npfeasibilityprogdate;
    }
   */
    public void setNpfeasibilityprogdate(Timestamp npfeasibilityprogdate) {
        this.npfeasibilityprogdate = npfeasibilityprogdate;
    }

    public Timestamp getNpfeasibilityprogdate() {
        return npfeasibilityprogdate;
    }

    public void setNpinstalationprogdate(Timestamp npinstalationprogdate) {
        this.npinstalationprogdate = npinstalationprogdate;
    }

    public Timestamp getNpinstalationprogdate() {
        return npinstalationprogdate;
    }

    public void setNpcontractnumber(int npcontractnumber) {
        this.npcontractnumber = npcontractnumber;
    }

    public int getNpcontractnumber() {
        return npcontractnumber;
    }

    public void setNpcontractstatus(String npcontractstatus) {
        this.npcontractstatus = npcontractstatus;
    }

    public String getNpcontractstatus() {
        return npcontractstatus;
    }

    public void setNpaditionalcost(String npaditionalcost) {
        this.npaditionalcost = npaditionalcost;
    }

    public String getNpaditionalcost() {
        return npaditionalcost;
    }

    public void setNpnetworkhosttype(int npnetworkhosttype) {
        this.npnetworkhosttype = npnetworkhosttype;
    }

    public int getNpnetworkhosttype() {
        return npnetworkhosttype;
    }

    public void setNpdescription(String npdescription) {
        this.npdescription = npdescription;
    }

    public String getNpdescription() {
        return npdescription;
    }

    public void setNplinktype(String nplinktype) {
        this.nplinktype = nplinktype;
    }

    public String getNplinktype() {
        return nplinktype;
    }

    public void setNpnewaddress(long npnewaddress) {
        this.npnewaddress = npnewaddress;
    }

    public long getNpnewaddress() {
        return npnewaddress;
    }

    public void setNpphonenumber1(String npphonenumber1) {
        this.npphonenumber1 = npphonenumber1;
    }

    public String getNpphonenumber1() {
        return npphonenumber1;
    }

    public void setNpphonenumber2(String npphonenumber2) {
        this.npphonenumber2 = npphonenumber2;
    }

    public String getNpphonenumber2() {
        return npphonenumber2;
    }

    public void setNpcontactname(String npcontactname) {
        this.npcontactname = npcontactname;
    }

    public String getNpcontactname() {
        return npcontactname;
    }

    public void setNporigmainservice(long nporigmainservice) {
        this.nporigmainservice = nporigmainservice;
    }

    public long getNporigmainservice() {
        return nporigmainservice;
    }

    public void setNpnewmainservice(long npnewmainservice) {
        this.npnewmainservice = npnewmainservice;
    }

    public long getNpnewmainservice() {
        return npnewmainservice;
    }

    public void setNpmodificationdate(Date npmodificationdate) {
        this.npmodificationdate = npmodificationdate;
    }

    public Date getNpmodificationdate() {
        return npmodificationdate;
    }

    public void setNpmodificationby(String npmodificationby) {
        this.npmodificationby = npmodificationby;
    }

    public String getNpmodificationby() {
        return npmodificationby;
    }

    public void setNpcreateddate(Date npcreateddate) {
        this.npcreateddate = npcreateddate;
    }

    public Date getNpcreateddate() {
        return npcreateddate;
    }

    public void setNpcreatedby(String npcreatedby) {
        this.npcreatedby = npcreatedby;
    }

    public String getNpcreatedby() {
        return npcreatedby;
    }

    public void setNptimestamp(int nptimestamp) {
        this.nptimestamp = nptimestamp;
    }

    public int getNptimestamp() {
        return nptimestamp;
    }


    public void setNpconceptid(long npconceptid) {
        this.npconceptid = npconceptid;
    }


    public long getNpconceptid() {
        return npconceptid;
    }


    public void setNpitemservices(String npitemservices) {
        this.npitemservices = npitemservices;
    }


    public String getNpitemservices() {
        return npitemservices;
    }


    public void setNpitembillingaccount(String npitembillingaccount) {
        this.npitembillingaccount = npitembillingaccount;
    }


    public String getNpitembillingaccount() {
        return npitembillingaccount;
    }


    public void setNpitemfreeservices(String npitemfreeservices) {
        this.npitemfreeservices = npitemfreeservices;
    }


    public String getNpitemfreeservices() {
        return npitemfreeservices;
    }


    public void setNpitemservicescost(String npitemservicescost) {
        this.npitemservicescost = npitemservicescost;
    }


    public String getNpitemservicescost() {
        return npitemservicescost;
    }


    public void setNpsharedinstalationid(long npsharedinstalationid) {
        this.npsharedinstalationid = npsharedinstalationid;
    }


    public long getNpsharedinstalationid() {
        return npsharedinstalationid;
    }


    public void setNpsharedinstal(String npsharedinstal) {
        this.npsharedinstal = npsharedinstal;
    }


    public String getNpsharedinstal() {
        return npsharedinstal;
    }


    public void setNpfirstcontract(String npfirstcontract) {
        this.npfirstcontract = npfirstcontract;
    }


    public String getNpfirstcontract() {
        return npfirstcontract;
    }


    public void setNpproductname(String npproductname) {
        this.npproductname = npproductname;
    }


    public String getNpproductname() {
        return npproductname;
    }


    public void setNpproductlinename(String npproductlinename) {
        this.npproductlinename = npproductlinename;
    }


    public String getNpproductlinename() {
        return npproductlinename;
    }


    public void setNpnewphone(String npnewphone) {
        this.npnewphone = npnewphone;
    }


    public String getNpnewphone() {
        return npnewphone;
    }


    public void setNpmodel(String npmodel) {
        this.npmodel = npmodel;
    }


    public String getNpmodel() {
        return npmodel;
    }


    public void setNpexceptionrevenue(String npexceptionrevenue) {
        this.npexceptionrevenue = npexceptionrevenue;
    }


    public String getNpexceptionrevenue() {
        return npexceptionrevenue;
    }


    public void setNpexceptionrent(String npexceptionrent) {
        this.npexceptionrent = npexceptionrent;
    }


    public String getNpexceptionrent() {
        return npexceptionrent;
    }


    public void setNparearespdev(long nparearespdev) {
        this.nparearespdev = nparearespdev;
    }


    public long getNparearespdev() {
        return nparearespdev;
    }


    public void setNpprovidergrpiddev(long npprovidergrpiddev) {
        this.npprovidergrpiddev = npprovidergrpiddev;
    }


    public long getNpprovidergrpiddev() {
        return npprovidergrpiddev;
    }


    public void setNparearespdevname(String nparearespdevname) {
        this.nparearespdevname = nparearespdevname;
    }


    public String getNparearespdevname() {
        return nparearespdevname;
    }


    public void setNpprovidergrpiddevname(String npprovidergrpiddevname) {
        this.npprovidergrpiddevname = npprovidergrpiddevname;
    }


    public String getNpprovidergrpiddevname() {
        return npprovidergrpiddevname;
    }


    public void setNporiginalproductname(String nporiginalproductname) {
        this.nporiginalproductname = nporiginalproductname;
    }


    public String getNporiginalproductname() {
        return nporiginalproductname;
    }


    public void setNpminutesrate(String npminutesrate) {
        this.npminutesrate = npminutesrate;
    }


    public String getNpminutesrate() {
        return npminutesrate;
    }


    public void setNpsharedinstaldesc(String npsharedinstaldesc) {
        this.npsharedinstaldesc = npsharedinstaldesc;
    }


    public String getNpsharedinstaldesc() {
        return npsharedinstaldesc;
    }


    public void setNpfirstcontractdesc(String npfirstcontractdesc) {
        this.npfirstcontractdesc = npfirstcontractdesc;
    }


    public String getNpfirstcontractdesc() {
        return npfirstcontractdesc;
    }


    public void setNpnetworkhosttypedesc(String npnetworkhosttypedesc) {
        this.npnetworkhosttypedesc = npnetworkhosttypedesc;
    }


    public String getNpnetworkhosttypedesc() {
        return npnetworkhosttypedesc;
    }


    public void setNplinktypedesc(String nplinktypedesc) {
        this.nplinktypedesc = nplinktypedesc;
    }


    public String getNplinktypedesc() {
        return nplinktypedesc;
    }


    public void setNpfeasibilitydesc(String npfeasibilitydesc) {
        this.npfeasibilitydesc = npfeasibilitydesc;
    }


    public String getNpfeasibilitydesc() {
        return npfeasibilitydesc;
    }

    public void setNporigmainservicedesc(String nporigmainservicedesc) {
        this.nporigmainservicedesc = nporigmainservicedesc;
    }


    public String getNporigmainservicedesc() {
        return nporigmainservicedesc;
    }


    public void setNpnewmainservicedesc(String npnewmainservicedesc) {
        this.npnewmainservicedesc = npnewmainservicedesc;
    }


    public String getNpnewmainservicedesc() {
        return npnewmainservicedesc;
    }


    public void setNpminuteprice(double npminuteprice) {
        this.npminuteprice = npminuteprice;
    }


    public double getNpminuteprice() {
        return npminuteprice;
    }


    public void setNpreferencephone(String npreferencephone) {
        this.npreferencephone = npreferencephone;
    }


    public String getNpreferencephone() {
        return npreferencephone;
    }


    public void setNpendservicedate(Date npendservicedate) {
        this.npendservicedate = npendservicedate;
    }


    public Date getNpendservicedate() {
        return npendservicedate;
    }


    public void setNpequipmentreturndesc(String npequipmentreturndesc) {
        this.npequipmentreturndesc = npequipmentreturndesc;
    }


    public String getNpequipmentreturndesc() {
        return npequipmentreturndesc;
    }


    public void setNpwarrantdesc(String npwarrantdesc) {
        this.npwarrantdesc = npwarrantdesc;
    }


    public String getNpwarrantdesc() {
        return npwarrantdesc;
    }

    public void setNpitemaddendumtemplates(String npitemaddendumtemplates) {
        this.npitemaddendumtemplates = npitemaddendumtemplates;
    }


    public String getNpitemaddendumtemplates() {
        return npitemaddendumtemplates;
    }


    public void setNporiginalprice(String nporiginalprice) {
        this.nporiginalprice = nporiginalprice;
    }


    public String getNporiginalprice() {
        return nporiginalprice;
    }


    public void setNppricetype(String nppricetype) {
        this.nppricetype = nppricetype;
    }


    public String getNppricetype() {
        return nppricetype;
    }


    public void setNppricetypeid(long nppricetypeid) {
        this.nppricetypeid = nppricetypeid;
    }


    public long getNppricetypeid() {
        return nppricetypeid;
    }


    public void setNppricetypeitemid(long nppricetypeitemid) {
        this.nppricetypeitemid = nppricetypeitemid;
    }


    public long getNppricetypeitemid() {
        return nppricetypeitemid;
    }


    public void setNpfixedphone(String npfixedphone) {
        this.npfixedphone = npfixedphone;
    }


    public String getNpfixedphone() {
        return npfixedphone;
    }


    public void setNplocution(String nplocution) {
        this.nplocution = nplocution;
    }


    public String getNplocution() {
        return nplocution;
    }

    public void setNpitemoperationstatus(String npitemoperationstatus) {
        this.npitemoperationstatus = npitemoperationstatus;
    }

    public String getNpitemoperationstatus() {
        return npitemoperationstatus;
    }


    public void setNpsiteid(long npsiteid) {
        this.npsiteid = npsiteid;
    }


    public long getNpsiteid() {
        return npsiteid;
    }


    public void setNpsitename(String npsitename) {
        this.npsitename = npsitename;
    }


    public String getNpsitename() {
        return npsitename;
    }


    public void setNpinternetrefcontract(long npinternetrefcontract) {
        this.npinternetrefcontract = npinternetrefcontract;
    }


    public long getNpinternetrefcontract() {
        return npinternetrefcontract;
    }


    public void setNpdatosrefcontract(long npdatosrefcontract) {
        this.npdatosrefcontract = npdatosrefcontract;
    }


    public long getNpdatosrefcontract() {
        return npdatosrefcontract;
    }


    public void setNptfrefphonenumber(String nptfrefphonenumber) {
        this.nptfrefphonenumber = nptfrefphonenumber;
    }


    public String getNptfrefphonenumber() {
        return nptfrefphonenumber;
    }


    public void setNpestadoitemId(String npestadoitemId) {
        this.npestadoitemId = npestadoitemId;
    }


    public String getNpestadoitemId() {
        return npestadoitemId;
    }


    public void setNpestadoitemDesc(String npestadoitemDesc) {
        this.npestadoitemDesc = npestadoitemDesc;
    }


    public String getNpestadoitemDesc() {
        return npestadoitemDesc;
    }


    public void setNpestadoproceso(String npestadoproceso) {
        this.npestadoproceso = npestadoproceso;
    }


    public String getNpestadoproceso() {
        return npestadoproceso;
    }


    public void setNpcobro(String npcobro) {
        this.npcobro = npcobro;
    }


    public String getNpcobro() {
        return npcobro;
    }


    public void setNporiginalsolutionid(long nporiginalsolutionid) {
        this.nporiginalsolutionid = nporiginalsolutionid;
    }


    public long getNporiginalsolutionid() {
        return nporiginalsolutionid;
    }


    public void setNporiginalsolutionname(String nporiginalsolutionname) {
        this.nporiginalsolutionname = nporiginalsolutionname;
    }


    public String getNporiginalsolutionname() {
        return nporiginalsolutionname;
    }


    public void setNpcontractref(long npcontractref) {
        this.npcontractref = npcontractref;
    }


    public long getNpcontractref() {
        return npcontractref;
    }


    public void setNptypeip(int nptypeip) {
        this.nptypeip = nptypeip;
    }


    public int getNptypeip() {
        return nptypeip;
    }

    public void setNptypeipdesc(String nptypeipdesc) {
        this.nptypeipdesc = nptypeipdesc;
    }


    public String getNptypeipdesc() {
        return nptypeipdesc;
    }


    public void setNpradio(String npradio) {
        this.npradio = npradio;
    }


    public String getNpradio() {
        return npradio;
    }

    public void setNppayrespname(String nppayrespname) {
        this.nppayrespname = nppayrespname;
    }


    public String getNppayrespname() {
        return nppayrespname;
    }

    public void setNpcustcode(String npcustcode) {
        this.npcustcode = npcustcode;
    }


    public String getNpcustcode() {
        return npcustcode;
    }


    public void setNpssaacontratado(String npssaacontratado) {
        this.npssaacontratado = npssaacontratado;
    }


    public String getNpssaacontratado() {
        return npssaacontratado;
    }


    public void setNpbscspaymntrespcustomeridId(long npbscspaymntrespcustomeridId) {
        this.npbscspaymntrespcustomeridId = npbscspaymntrespcustomeridId;
    }


    public long getNpbscspaymntrespcustomeridId() {
        return npbscspaymntrespcustomeridId;
    }


    //CPUENTE CAP & CAL
    public int getNpequipmenttime() {
        return npequipmenttime;
    }

    public void setNpequipmenttime(int npequipmenttime) {
        this.npequipmenttime = npequipmenttime;
    }

    public int getNpownequipment() {
        return npownequipment;
    }

    public void setNpownequipment(int npownequipment) {
        this.npownequipment = npownequipment;
    }

    public String getNpreasonchange() {
        return npreasonchange;
    }

    public void setNpreasonchange(String npreasonchange) {
        this.npreasonchange = npreasonchange;
    }


    public void setNpestadoContrato(String npestadoContrato) {
        this.npestadoContrato = npestadoContrato;
    }


    public String getNpestadoContrato() {
        return npestadoContrato;
    }


    public void setNpmotivoEstado(String npmotivoEstado) {
        this.npmotivoEstado = npmotivoEstado;
    }


    public String getNpmotivoEstado() {
        return npmotivoEstado;
    }


    public void setNpfechaCambioEstado(String npfechaCambioEstado) {
        this.npfechaCambioEstado = npfechaCambioEstado;
    }


    public String getNpfechaCambioEstado() {
        return npfechaCambioEstado;
    }

    public void setNpServiceItemList(ArrayList npServiceItemList) {
        this.npServiceItemList = npServiceItemList;
    }


    public ArrayList getNpServiceItemList() {
        return npServiceItemList;
    }


    public void setNpcountreasonId(long npcountreasonId) {
        this.npcountreasonId = npcountreasonId;
    }


    public long getNpcountreasonId() {
        return npcountreasonId;
    }


    public void setNpLevel(int npLevel) {
        this.npLevel = npLevel;
    }


    public int getNpLevel() {
        return npLevel;
    }


    public void setNpLevelDesc(String npLevelDesc) {
        this.npLevelDesc = npLevelDesc;
    }


    public String getNpLevelDesc() {
        return npLevelDesc;
    }

    public void setNpstatusaplication(String npstatusaplication) {
        this.npstatusaplication = npstatusaplication;
    }


    public String getNpstatusaplication() {
        return npstatusaplication;
    }


    public void setNpbusinesssolutionid(long npbusinesssolutionid) {
        this.npbusinesssolutionid = npbusinesssolutionid;
    }


    public long getNpbusinesssolutionid() {
        return npbusinesssolutionid;
    }


    public void setNpactivationdate(Date npactivationdate) {
        this.npactivationdate = npactivationdate;
    }


    public Date getNpactivationdate() {
        return npactivationdate;
    }


    public void setNpdeactivationdate(Date npdeactivationdate) {
        this.npdeactivationdate = npdeactivationdate;
    }


    public Date getNpdeactivationdate() {
        return npdeactivationdate;
    }


    public void setNpmissingdays(String npmissingdays) {
        this.npmissingdays = npmissingdays;
    }


    public String getNpmissingdays() {
        return npmissingdays;
    }


    public void setNpproductmodelid(long npproductmodelid) {
        this.npproductmodelid = npproductmodelid;
    }


    public long getNpproductmodelid() {
        return npproductmodelid;
    }

    public void setNpAplicarVO(String npAplicarVO) {
        this.npAplicarVO = npAplicarVO;
    }


    public String getNpAplicarVO() {
        return npAplicarVO;
    }

    public void setNpIndice(int npIndice) {
        this.npIndice = npIndice;
    }


    public int getNpIndice() {
        return npIndice;
    }

    //jsalazar -- servicios adicionales 06/12/2010 inicio
    public void setNpmodelid(String npmodelid) {
        this.npmodelid = npmodelid;
    }


    public String getNpmodelid() {
        return npmodelid;
    }
//jsalazar -- servicios adicionales 06/12/2010 fin  

    public void setNpimeicustomer(String npimeicustomer) {
        this.npimeicustomer = npimeicustomer;
    }


    public String getNpimeicustomer() {
        return npimeicustomer;
    }


    public void setNpownimeisim(String npownimeisim) {
        this.npownimeisim = npownimeisim;
    }


    public String getNpownimeisim() {
        return npownimeisim;
    }


    public void setCadNumberReserve(String cadNumberReserve) {
        this.cadNumberReserve = cadNumberReserve;
    }


    public String getCadNumberReserve() {
        return cadNumberReserve;
    }


    public void setNpVepItem(int npVepItem) {
        this.npVepItem = npVepItem;
    }


    public int getNpVepItem() {
        return npVepItem;
    }


    public void setNpVepTotalPrice(double npVepTotalPrice) {
        this.npVepTotalPrice = npVepTotalPrice;
    }


    public double getNpVepTotalPrice() {
        return npVepTotalPrice;
    }

    public String getNpproductstatus() {
        return npproductstatus;
    }

    public void setNpproductstatus(String npproductstatus) {
        this.npproductstatus = npproductstatus;
    }

    public String getNpchanged() {
        return npchanged;
    }

    public void setNpchanged(String npchanged) {
        this.npchanged = npchanged;
    }

    public String getNpflagaccessory() {
        return npflagaccessory;
    }

    public void setNpflagaccessory(String npflagaccessory) {
        this.npflagaccessory = npflagaccessory;
    }

    public String getNpserviceROA() {
        return npserviceROA;
    }

    public void setNpserviceROA(String npserviceROA) {
        this.npserviceROA = npserviceROA;
    }

    public int getNpservplantype() {
        return npservplantype;
    }

    public void setNpservplantype(int npservplantype) {
        this.npservplantype = npservplantype;
    }

    public String getNpservbagcode() {
        return npservbagcode;
    }

    public void setNpservbagcode(String npservbagcode) {
        this.npservbagcode = npservbagcode;
    }

    public String getNpservbagtype() {
        return npservbagtype;
    }

    public void setNpservbagtype(String npservbagtype) {
        this.npservbagtype = npservbagtype;
    }

    public Date getNpservvalidactivationdate() {
        return npservvalidactivationdate;
    }

    public void setNpservvalidactivationdate(Date npservvalidactivationdate) {
        this.npservvalidactivationdate = npservvalidactivationdate;
    }

    public int getNpservvalidity() {
        return npservvalidity;
    }

    public void setNpservvalidity(int npservvalidity) {
        this.npservvalidity = npservvalidity;
    }

    public int getNptypeproductBC() {
        return nptypeproductBC;
    }

    public void setNptypeproductBC(int nptypeproductBC) {
        this.nptypeproductBC = nptypeproductBC;
	}
	//CDM+CDP PRY-0817 EFLORES
	public int getNpkeepSIM() {
        return npkeepSIM;
    }

    //PRY-0721 DERAZO
    public int getNpzonacoberturaid() {
        return npzonacoberturaid;
    }

    public void setNpkeepSIM(int npkeepSIM) {
        this.npkeepSIM = npkeepSIM;
    }

    //PRY-0721 DERAZO
    public void setNpzonacoberturaid(int npzonacoberturaid) {
        this.npzonacoberturaid = npzonacoberturaid;
    }

    //PRY-0721 DERAZO
    public String getNpnombrezona() {
        return npnombrezona;
    }

    //PRY-0721 DERAZO
    public void setNpnombrezona(String npnombrezona) {
        this.npnombrezona = npnombrezona;
    }

    //EFLORES BAFI2
    public int getNpProvinceZoneId() {
        return npProvinceZoneId;
    }
    
  //PRY-0762 JQUISPE
  	public Integer getNpcantidadRentaAdelantada() {
  		return npcantidadRentaAdelantada;
  	}

    public void setNpProvinceZoneId(int npProvinceZoneId) {
        this.npProvinceZoneId = npProvinceZoneId;
    }

    public String getNpNameProvinceZone() {
        return npNameProvinceZone;
    }

    public void setNpNameProvinceZone(String npNameProvinceZone) {
        this.npNameProvinceZone = npNameProvinceZone;
    }

    public int getNpDistrictZoneId() {
        return npDistrictZoneId;
    }

    public void setNpDistrictZoneId(int npDistrictZoneId) {
        this.npDistrictZoneId = npDistrictZoneId;
    }

    public String getNpNameDistrictZone() {
        return npNameDistrictZone;
    }

    public void setNpNameDistrictZone(String npNameDistrictZone) {
        this.npNameDistrictZone = npNameDistrictZone;
    }
    //FIN EFLORES BAFI2


  	//PRY-0762 JQUISPE
  	public void setNpcantidadRentaAdelantada(Integer npcantidadRentaAdelantada) {
  		this.npcantidadRentaAdelantada = npcantidadRentaAdelantada;
  	}

  	//PRY-0762 JQUISPE
  	public String getNptotalRentaAdelantada() {
  		return nptotalRentaAdelantada;
  	}

  	//PRY-0762 JQUISPE
  	public void setNptotalRentaAdelantada(String nptotalRentaAdelantada) {
  		this.nptotalRentaAdelantada = nptotalRentaAdelantada;
  	}

	public String getNpprorrateoPrice() {
		return npprorrateoPrice;
	}

	public void setNpprorrateoPrice(String npprorrateoPrice) {
		this.npprorrateoPrice = npprorrateoPrice;
	}

	public String getNphndIndice() {
		return nphndIndice;
	}

	public void setNphndIndice(String nphndIndice) {
		this.nphndIndice = nphndIndice;
	}

    //INICIO DERAZO TDECONV003-2
    public String getNpOwnImeiFS() {
        return npOwnImeiFS;
    }

    public void setNpOwnImeiFS(String npOwnImeiFS) {
        this.npOwnImeiFS = npOwnImeiFS;
    }
    //FIN DERAZO
}
