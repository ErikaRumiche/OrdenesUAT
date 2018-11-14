package pe.com.nextel.bean;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;

import pe.com.nextel.util.Constante;
import pe.com.nextel.util.GenericObject;


public class CustomerBean extends GenericObject implements Serializable{
  
    private static final long serialVersionUID = 1L;
    private long swCustomerId;
    private long swParentId;
    private long swChnlPartnerId;
    private long swAcctMgrId;
    private long swRegionId;

    private int swActiveFfk;
    private int swPublishall;
    private long swIndustryId;
    private String swCreditRating;
    private String swDefaultReplyVia;
    private String swRegion;
    private String swRegionName;
    private String swStatus;
    private String swType;
    private String swMainFax;
    private String swMainFaxArea;
    private String depMainFaxArea; 
    private String swMainFaxCntry;
    private String swMainPhone;
    private String swMainPhoneArea;
    private String swMainPhoneCntry;
    private String swUrl;
    private String swLocType;
    private String swAcctType;
    private String swBusiness;
    private String swIndustry;
    private String swTerritory;
    private String swRevenue;
    private int swEmpTotal;
    private String swOwnerShip;
    private String swSic;
    private String swGeoCode;
    private String swDuns;
    private String swName;
    private int swCarrier;
    private int swTpm;
    private String swNote;
    private String swSupportNote;
    private String swMasterVer;
    private String swCreatedBy;
    private Date swDateCreated;
    private String timeStamp;
    private String swCanEmpl;
    private String swBeepErqt;
    private String swCelularQt;
    private String swRadiosQt;
    private String swRuc;
    private String swPhone2;
    private String swPhone3;
    private String ubicacion;
    private String top5000;
    private int swTpmswCelularQt1;
    private int swTpmswRadiosQt1;
    private int swTpmswBeeperQt1;
    private String swGanadoP;
    private String swEstado;
    private String swNameCom;
    private String swTipoPerson;
    private String swRating;
    private String swCodBscs;
    private long swRereridoId;
    private String swClienteDesact;
    private int nxReferidoCustid;
    private long npPersonId;
    private String npCorporativo;
    private String npNacional;
    private String npPartnerCodBscs;
    private long npPrePartnerId;
    private int npLineaCredito;
    private String npPublish;
    private String npPublishPhone;
    private String npEmail;
    private long npGiroId;
    private String npGiroName;
    private Date npFechaUpdcali;
    private int npHolding;
    private int npRanking;
    private int npIngresoAnual;
    private int npUtilidadNeta;
    private String npFlgStrategic;
    private String npTipoDoc;
    private int npCiiuId;
    private String npRatingProspect;
    private int npDepositoGarantia;
    private int npReminder;
    private String npFlagAuto;
    private long npCorporateId;
    private int npPlus;
    private String npWapUrl;
    private String swMainphonearea;
    private String depMainphonearea;                
    private String npPhone2areacode;
    private String depPhone2areacode;                
    private String npPhone3areacode;                
    private String depPhone3areacode;                
    private ArrayList listSites = new ArrayList();
    private String npCustomerRelationType;
    private int npCustomerRelationid;
    private String npCustomerRelationName;
    private String npFirstName;
    private String npLastName;
    private String strMessage;
    private String strExclusivity;
    private String strRucValid; // retorna el num de doc. si existe uno similar
    private String strNameValid; // retorna el cliente si existe uno similar 
    private String npSegmento;
    private String npRangoCuenta;
    private String npAutoLpdp;
    private String npFechaLpdp;
        
    
    public void setStrRucValid(String strRucValid) {
        this.strRucValid = strRucValid;
    }

    public String getStrRucValid() {
        return strRucValid;
    }
    
    public void setStrNameValid(String strNameValid) {
        this.strNameValid = strNameValid;
    }

    public String getStrNameValid() {
        return strNameValid;
    }
    
    public void setStrMessage(String strMessage) {
        this.strMessage = strMessage;
    }

    public String getStrMessage() {
        return strMessage;
    }
    
    
    /***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - INICIO
     *  REALIZADO POR: Richard De los Reyes
     *  FECHA: 24/09/2007
     ***********************************************************************/
    private String tituloPersona; //Sr. Sra. Srta.
    private int status; //Usado por el getValidateCustomer: 1: Customer, 0; Nuevo; cualquier otro valor inválido.
    private ArrayList cuentaList = new ArrayList();
    private ArrayList direccionList = new ArrayList();
    /***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - FIN
     *  REALIZADO POR: Richard De los Reyes
     *  FECHA: 24/09/2007
     *  
     */
    
    public void setStrExclusivity(String strExclusivity) {
        this.strExclusivity = strExclusivity;
    }

    public String getStrExclusivity() {
        return strExclusivity;
    }
    
    public void setNpFirstName(String npFirstName) {
        this.npFirstName = npFirstName;
    }

    public String getNpFirstName() {
        return npFirstName;
    }
    
    public void setNpLastName(String npLastName) {
        this.npLastName = npLastName;
    }

    public String getNpLastName() {
        return npLastName;
    }
    public void setSwActiveFfk(int swActiveFfk) {
        this.swActiveFfk = swActiveFfk;
    }

    public int getSwActiveFfk() {
        return swActiveFfk;
    }

    public void setSwPublishall(int swPublishall) {
        this.swPublishall = swPublishall;
    }

    public int getSwPublishall() {
        return swPublishall;
    }

    public void setSwIndustryId(long swIndustryId) {
        this.swIndustryId = swIndustryId;
    }

    public long getSwIndustryId() {
        return swIndustryId;
    }

    public void setSwCreditRating(String swCreditRating) {
        this.swCreditRating = swCreditRating;
    }

    public String getSwCreditRating() {
        return swCreditRating;
    }

    public void setSwDefaultReplyVia(String swDefaultReplyVia) {
        this.swDefaultReplyVia = swDefaultReplyVia;
    }

    public String getSwDefaultReplyVia() {
        return swDefaultReplyVia;
    }

    public void setSwRegion(String swRegion) {
        this.swRegion = swRegion;
    }

    public String getSwRegion() {
        return swRegion;
    }

    public void setSwStatus(String swStatus) {
        this.swStatus = swStatus;
    }

    public String getSwStatus() {
        return swStatus;
    }

    public void setSwType(String swType) {
        this.swType = swType;
    }

    public String getSwType() {
        return swType;
    }

    public void setSwMainFax(String swMainFax) {
        this.swMainFax = swMainFax;
    }

    public String getSwMainFax() {
        return swMainFax;
    }

    public void setSwMainFaxArea(String swMainFaxArea) {
        this.swMainFaxArea = swMainFaxArea;
    }

    public String getSwMainFaxArea() {
        return swMainFaxArea;
    }

    public void setSwMainFaxCntry(String swMainFaxCntry) {
        this.swMainFaxCntry = swMainFaxCntry;
    }

    public String getSwMainFaxCntry() {
        return swMainFaxCntry;
    }

    public void setSwMainPhone(String swMainPhone) {
        this.swMainPhone = swMainPhone;
    }

    public String getSwMainPhone() {
        return swMainPhone;
    }

    public void setSwMainPhoneArea(String swMainPhoneArea) {
        this.swMainPhoneArea = swMainPhoneArea;
    }

    public String getSwMainPhoneArea() {
        return swMainPhoneArea;
    }

    public void setSwMainPhoneCntry(String swMainPhoneCntry) {
        this.swMainPhoneCntry = swMainPhoneCntry;
    }

    public String getSwMainPhoneCntry() {
        return swMainPhoneCntry;
    }

    public void setSwUrl(String swUrl) {
        this.swUrl = swUrl;
    }

    public String getSwUrl() {
        return swUrl;
    }

    public void setSwLocType(String swLocType) {
        this.swLocType = swLocType;
    }

    public String getSwLocType() {
        return swLocType;
    }

    public void setSwAcctType(String swAcctType) {
        this.swAcctType = swAcctType;
    }

    public String getSwAcctType() {
        return swAcctType;
    }

    public void setSwBusiness(String swBusiness) {
        this.swBusiness = swBusiness;
    }

    public String getSwBusiness() {
        return swBusiness;
    }

    public void setSwIndustry(String swIndustry) {
        this.swIndustry = swIndustry;
    }

    public String getSwIndustry() {
        return swIndustry;
    }

    public void setSwTerritory(String swTerritory) {
        this.swTerritory = swTerritory;
    }

    public String getSwTerritory() {
        return swTerritory;
    }

    public void setSwRevenue(String swRevenue) {
        this.swRevenue = swRevenue;
    }

    public String getSwRevenue() {
        return swRevenue;
    }

    public void setSwEmpTotal(int swEmpTotal) {
        this.swEmpTotal = swEmpTotal;
    }

    public int getSwEmpTotal() {
        return swEmpTotal;
    }

    public void setSwOwnerShip(String swOwnerShip) {
        this.swOwnerShip = swOwnerShip;
    }

    public String getSwOwnerShip() {
        return swOwnerShip;
    }

    public void setSwSic(String swSic) {
        this.swSic = swSic;
    }

    public String getSwSic() {
        return swSic;
    }

    public void setSwGeoCode(String swGeoCode) {
        this.swGeoCode = swGeoCode;
    }

    public String getSwGeoCode() {
        return swGeoCode;
    }

    public void setSwDuns(String swDuns) {
        this.swDuns = swDuns;
    }

    public String getSwDuns() {
        return swDuns;
    }

    public void setSwName(String swName) {
        this.swName = swName;
    }

    public String getSwName() {
        return swName;
    }

    public void setSwCarrier(int swCarrier) {
        this.swCarrier = swCarrier;
    }

    public int getSwCarrier() {
        return swCarrier;
    }

    public void setSwTpm(int swTpm) {
        this.swTpm = swTpm;
    }

    public int getSwTpm() {
        return swTpm;
    }

    public void setSwNote(String swNote) {
        this.swNote = swNote;
    }

    public String getSwNote() {
        return swNote;
    }

    public void setSwSupportNote(String swSupportNote) {
        this.swSupportNote = swSupportNote;
    }

    public String getSwSupportNote() {
        return swSupportNote;
    }

    public void setSwMasterVer(String swMasterVer) {
        this.swMasterVer = swMasterVer;
    }

    public String getSwMasterVer() {
        return swMasterVer;
    }

    public void setSwCreatedBy(String swCreatedBy) {
        this.swCreatedBy = swCreatedBy;
    }

    public String getSwCreatedBy() {
        return swCreatedBy;
    }

    public void setSwDateCreated(Date swDateCreated) {
        this.swDateCreated = swDateCreated;
    }

    public Date getSwDateCreated() {
        return swDateCreated;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setSwCanEmpl(String swCanEmpl) {
        this.swCanEmpl = swCanEmpl;
    }

    public String getSwCanEmpl() {
        return swCanEmpl;
    }

    public void setSwBeepErqt(String swBeepErqt) {
        this.swBeepErqt = swBeepErqt;
    }

    public String getSwBeepErqt() {
        return swBeepErqt;
    }

    public void setSwCelularQt(String swCelularQt) {
        this.swCelularQt = swCelularQt;
    }

    public String getSwCelularQt() {
        return swCelularQt;
    }

    public void setSwRadiosQt(String swRadiosQt) {
        this.swRadiosQt = swRadiosQt;
    }

    public String getSwRadiosQt() {
        return swRadiosQt;
    }

    public void setSwRuc(String swRuc) {
        this.swRuc = swRuc;
    }

    public String getSwRuc() {
        return swRuc;
    }

    public void setSwPhone2(String swPhone2) {
        this.swPhone2 = swPhone2;
    }

    public String getSwPhone2() {
        return swPhone2;
    }

    public void setSwPhone3(String swPhone3) {
        this.swPhone3 = swPhone3;
    }

    public String getSwPhone3() {
        return swPhone3;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setTop5000(String top5000) {
        this.top5000 = top5000;
    }

    public String getTop5000() {
        return top5000;
    }

    public void setSwTpmswCelularQt1(int swTpmswCelularQt1) {
        this.swTpmswCelularQt1 = swTpmswCelularQt1;
    }

    public int getSwTpmswCelularQt1() {
        return swTpmswCelularQt1;
    }

    public void setSwTpmswRadiosQt1(int swTpmswRadiosQt1) {
        this.swTpmswRadiosQt1 = swTpmswRadiosQt1;
    }

    public int getSwTpmswRadiosQt1() {
        return swTpmswRadiosQt1;
    }

    public void setSwTpmswBeeperQt1(int swTpmswBeeperQt1) {
        this.swTpmswBeeperQt1 = swTpmswBeeperQt1;
    }

    public int getSwTpmswBeeperQt1() {
        return swTpmswBeeperQt1;
    }

    public void setSwGanadoP(String swGanadoP) {
        this.swGanadoP = swGanadoP;
    }

    public String getSwGanadoP() {
        return swGanadoP;
    }

    public void setSwEstado(String swEstado) {
        this.swEstado = swEstado;
    }

    public String getSwEstado() {
        return swEstado;
    }

    public void setSwNameCom(String swNameCom) {
        this.swNameCom = swNameCom;
    }

    public String getSwNameCom() {
        return swNameCom;
    }

    public void setSwTipoPerson(String swTipoPerson) {
        this.swTipoPerson = swTipoPerson;
    }

    public String getSwTipoPerson() {
        return swTipoPerson;
    }

    public void setSwRating(String swRating) {
        this.swRating = swRating;
    }

    public String getSwRating() {
        return swRating;
    }

    public void setSwCodBscs(String swCodBscs) {
        this.swCodBscs = swCodBscs;
    }

    public String getSwCodBscs() {
        return swCodBscs;
    }

    public void setSwRereridoId(int swRereridoId) {
        this.swRereridoId = swRereridoId;
    }

    public long getSwRereridoId() {
        return swRereridoId;
    }

    public void setSwClienteDesact(String swClienteDesact) {
        this.swClienteDesact = swClienteDesact;
    }

    public String getSwClienteDesact() {
        return swClienteDesact;
    }

    public void setNxReferidoCustid(int nxReferidoCustid) {
        this.nxReferidoCustid = nxReferidoCustid;
    }

    public int getNxReferidoCustid() {
        return nxReferidoCustid;
    }

    public void setNpPersonId(int npPersonId) {
        this.npPersonId = npPersonId;
    }

    public long getNpPersonId() {
        return npPersonId;
    }

    public void setNpCorporativo(String npCorporativo) {
        this.npCorporativo = npCorporativo;
    }

    public String getNpCorporativo() {
        return npCorporativo;
    }

    public void setNpNacional(String npNacional) {
        this.npNacional = npNacional;
    }

    public String getNpNacional() {
        return npNacional;
    }

    public void setNpPartnerCodBscs(String npPartnerCodBscs) {
        this.npPartnerCodBscs = npPartnerCodBscs;
    }

    public String getNpPartnerCodBscs() {
        return npPartnerCodBscs;
    }

    public void setNpPrePartnerId(int npPrePartnerId) {
        this.npPrePartnerId = npPrePartnerId;
    }

    public long getNpPrePartnerId() {
        return npPrePartnerId;
    }

    public void setNpLineaCredito(int npLineaCredito) {
        this.npLineaCredito = npLineaCredito;
    }

    public int getNpLineaCredito() {
        return npLineaCredito;
    }

    public void setNpPublish(String npPublish) {
        this.npPublish = npPublish;
    }

    public String getNpPublish() {
        return npPublish;
    }

    public void setNpPublishPhone(String npPublishPhone) {
        this.npPublishPhone = npPublishPhone;
    }

    public String getNpPublishPhone() {
        return npPublishPhone;
    }

    public void setNpEmail(String npEmail) {
        this.npEmail = npEmail;
    }

    public String getNpEmail() {
        return npEmail;
    }

    public void setNpGiroId(int npGiroId) {
        this.npGiroId = npGiroId;
    }

    public long getNpGiroId() {
        return npGiroId;
    }

    public void setNpFechaUpdcali(Date npFechaUpdcali) {
        this.npFechaUpdcali = npFechaUpdcali;
    }

    public Date getNpFechaUpdcali() {
        return npFechaUpdcali;
    }

    public void setNpHolding(int npHolding) {
        this.npHolding = npHolding;
    }

    public int getNpHolding() {
        return npHolding;
    }

    public void setNpRanking(int npRanking) {
        this.npRanking = npRanking;
    }

    public int getNpRanking() {
        return npRanking;
    }

    public void setNpIngresoAnual(int npIngresoAnual) {
        this.npIngresoAnual = npIngresoAnual;
    }

    public int getNpIngresoAnual() {
        return npIngresoAnual;
    }

    public void setNpUtilidadNeta(int npUtilidadNeta) {
        this.npUtilidadNeta = npUtilidadNeta;
    }

    public int getNpUtilidadNeta() {
        return npUtilidadNeta;
    }

    public void setNpFlgStrategic(String npFlgStrategic) {
        this.npFlgStrategic = npFlgStrategic;
    }

    public String getNpFlgStrategic() {
        return npFlgStrategic;
    }

    public void setNpTipoDoc(String npTipoDoc) {
        this.npTipoDoc = npTipoDoc;
    }

    public String getNpTipoDoc() {
        return npTipoDoc;
    }

    public void setNpCiiuId(int npCiiuId) {
        this.npCiiuId = npCiiuId;
    }

    public int getNpCiiuId() {
        return npCiiuId;
    }

    public void setNpRatingProspect(String npRatingProspect) {
        this.npRatingProspect = npRatingProspect;
    }

    public String getNpRatingProspect() {
        return npRatingProspect;
    }

    public void setNpDepositoGarantia(int npDepositoGarantia) {
        this.npDepositoGarantia = npDepositoGarantia;
    }

    public int getNpDepositoGarantia() {
        return npDepositoGarantia;
    }

    public void setNpReminder(int npReminder) {
        this.npReminder = npReminder;
    }

    public int getNpReminder() {
        return npReminder;
    }

    public void setNpFlagAuto(String npFlagAuto) {
        this.npFlagAuto = npFlagAuto;
    }

    public String getNpFlagAuto() {
        return npFlagAuto;
    }

    public void setNpCorporateId(int npCorporateId) {
        this.npCorporateId = npCorporateId;
    }

    public long getNpCorporateId() {
        return npCorporateId;
    }

    public void setNpPlus(int npPlus) {
        this.npPlus = npPlus;
    }

    public int getNpPlus() {
        return npPlus;
    }

    public void setNpWapUrl(String npWapUrl) {
        this.npWapUrl = npWapUrl;
    }

    public String getNpWapUrl() {
        return npWapUrl;
    }

    public void setListSites(ArrayList listSites) {
        this.listSites = listSites;
    }

    public ArrayList getListSites() {
        return listSites;
    }

    public void setNpCustomerRelationType(String npCustomerRelationType) {
        this.npCustomerRelationType = npCustomerRelationType;
    }

    public String getNpCustomerRelationType() {
        return npCustomerRelationType;
    }

    public void setNpGiroName(String npGiroName) {
        this.npGiroName = npGiroName;
    }

    public String getNpGiroName() {
        return npGiroName;
    }
    
     public void setNpRangoCuenta(String npRangoCuenta) {
        this.npRangoCuenta = npRangoCuenta;
    }
    public String getNpRangoCuenta() {
        return npRangoCuenta;
    }

    public void setSwRegionName(String swRegionName) {
        this.swRegionName = swRegionName;
    }

    public void setNpAutoLpdp (String npAutoLpdp){
        this.npAutoLpdp = npAutoLpdp;
    }
    public String getNpAutoLpdp(){
        return npAutoLpdp;
    }

    public void setNpFechaLpdp (String npFechaLpdp){
        this.npFechaLpdp = npFechaLpdp;
    }
    public String getNpFechaLpdp(){
        return npFechaLpdp;
    }

    public String getSwRegionName() {
        return swRegionName;
    }
    
    public void setNpCustomerRelationName(String npCustomerRelationName) {
        this.npCustomerRelationName = npCustomerRelationName;
    }

    public String getNpCustomerRelationName() {
        return npCustomerRelationName;
    }

    public void setNpCustomerRelationid(int npCustomerRelationid) {
        this.npCustomerRelationid = npCustomerRelationid;
    }

    public int getNpCustomerRelationid() {
        return npCustomerRelationid;
    }

  public void setSwCustomerId(long swCustomerId)
  {
    this.swCustomerId = swCustomerId;
  }


  public long getSwCustomerId()
  {
    return swCustomerId;
  }


  public void setSwParentId(long swParentId)
  {
    this.swParentId = swParentId;
  }


  public long getSwParentId()
  {
    return swParentId;
  }


  public void setSwChnlPartnerId(long swChnlPartnerId)
  {
    this.swChnlPartnerId = swChnlPartnerId;
  }


  public long getSwChnlPartnerId()
  {
    return swChnlPartnerId;
  }


  public void setSwAcctMgrId(long swAcctMgrId)
  {
    this.swAcctMgrId = swAcctMgrId;
  }


  public long getSwAcctMgrId()
  {
    return swAcctMgrId;
  }


  public void setSwRegionId(long swRegionId)
  {
    this.swRegionId = swRegionId;
  }


  public long getSwRegionId()
  {
    return swRegionId;
  }


  


  public void setSwRereridoId(long swRereridoId)
  {
    this.swRereridoId = swRereridoId;
  }



  public void setNpPersonId(long npPersonId)
  {
    this.npPersonId = npPersonId;
  }


  public void setNpPrePartnerId(long npPrePartnerId)
  {
    this.npPrePartnerId = npPrePartnerId;
  }


  public void setNpGiroId(long npGiroId)
  {
    this.npGiroId = npGiroId;
  }


  public void setNpCorporateId(long npCorporateId)
  {
    this.npCorporateId = npCorporateId;
  }


    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - INICIO
     *  REALIZADO POR: Richard De los Reyes
     *  FECHA: 24/09/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/
     
    /**
     * Motivo:  Inicializa los valores de un Cliente como Prospecto.
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 26/08/2007
     */
    public void createProspect() {
        swCustomerId = 0;
        npTipoDoc = "";
        swRuc = "";
        swCodBscs = "";
        swName = "";
        swMainPhone = "";
        npGiroId = 0;
        swIndustryId = 0;
        tituloPersona = "";
        swType = Constante.CUSTOMER_CONDICION_PROSPECT;
        swTipoPerson = "";
        status = 0;
    }

    /**
     * Motivo:  Verifica que el Cliente exista, de acuerdo a 3 criterios:
     * <ul>* Status = 1 </ul>
     * <ul>* Tipo = "Customer"</ul>
     * <ul>* Id > 0</ul>
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 26/08/2007
     * @return    boolean.
     */
    public boolean isValidCustomer() {
        return isStatusValid() &&  isIdValid();
    }

    /**
     * Motivo:  Verifica que el Status sea válido.
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 26/08/2007
     * @return    boolean.
     */
    private boolean isStatusValid() {
        return status == Constante.CUSTOMER_STATUS_VALIDO;
    }

    /**
     * Motivo:  Verifica que sea "Customer"
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 26/08/2007
     * @return    boolean.
     */
    private boolean isTipoCustomer() {
        return swType.equals(Constante.CUSTOMER_CONDICION_CUSTOMER);
    }

    /**
     * Motivo:  Verifica que sea un Id válida.
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 26/08/2007
     * @return    boolean.
     */
    private boolean isIdValid() {
        return swCustomerId > 0;
    }

  public void setTituloPersona(String tituloPersona)
  {
    this.tituloPersona = tituloPersona;
  }


  public String getTituloPersona()
  {
    return tituloPersona;
  }


  public void setStatus(int status)
  {
    this.status = status;
  }


  public int getStatus()
  {
    return status;
  }


  public void setCuentaList(ArrayList cuentaList)
  {
    this.cuentaList = cuentaList;
  }


  public ArrayList getCuentaList()
  {
    return cuentaList;
  }


  public void setDireccionList(ArrayList direccionList)
  {
    this.direccionList = direccionList;
  }


  public ArrayList getDireccionList()
  {
    return direccionList;
  }


   public void setSwMainphonearea(String swMainphonearea)
   {
      this.swMainphonearea = swMainphonearea;
   }


   public String getSwMainphonearea()
   {
      return swMainphonearea;
   }


   public void setNpPhone2areacode(String npPhone2areacode)
   {
      this.npPhone2areacode = npPhone2areacode;
   }


   public String getNpPhone2areacode()
   {
      return npPhone2areacode;
   }


   public void setNpPhone3areacode(String npPhone3areacode)
   {
      this.npPhone3areacode = npPhone3areacode;
   }


   public String getNpPhone3areacode()
   {
      return npPhone3areacode;
   }


   public void setDepMainFaxArea(String depMainFaxArea)
   {
      this.depMainFaxArea = depMainFaxArea;
   }


   public String getDepMainFaxArea()
   {
      return depMainFaxArea;
   }


   public void setDepMainphonearea(String depMainphonearea)
   {
      this.depMainphonearea = depMainphonearea;
   }


   public String getDepMainphonearea()
   {
      return depMainphonearea;
   }


   public void setDepPhone2areacode(String depPhone2areacode)
   {
      this.depPhone2areacode = depPhone2areacode;
   }


   public String getDepPhone2areacode()
   {
      return depPhone2areacode;
   }


   public void setDepPhone3areacode(String depPhone3areacode)
   {
      this.depPhone3areacode = depPhone3areacode;
   }


   public String getDepPhone3areacode()
   {
      return depPhone3areacode;
   }


  public void setNpSegmento(String npSegmento)
  {
    this.npSegmento = npSegmento;
  }


  public String getNpSegmento()
  {
    return npSegmento;
  }

    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - FIN
     *  REALIZADO POR: Richard De los Reyes
     *  FECHA: 24/09/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/

}
