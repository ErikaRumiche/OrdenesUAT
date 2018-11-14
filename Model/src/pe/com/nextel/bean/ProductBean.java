package pe.com.nextel.bean;

import java.io.Serializable;

import java.sql.Date;

import pe.com.nextel.util.GenericObject;

/**
 * Prueba
 */
public class ProductBean extends GenericObject implements Serializable {
  private static final long serialVersionUID = 1L;
	private long npproductid;
	private long npproductlineid;
	private long npsolutionid;
	private String npname;
  private String npmodel;
	private String npnote;
	private String npinventorycode;
	private char npinventoryflag;
	private String npsubtype;
	private char npkit;
	private char npflginstall;
	private char npflgrepairable;
	private double npcost;
	private String npcurrency;
	private String npcreatedby;
	private Date npcreateddate;
	private String npmodifiedby;
	private Date npmodifieddate;
	private String npstatus;
	private long npplanid;
  
  private long npcustomerid;
  private long npsiteid;
  private long npproductid_new;
  private long npproductid_old;
  private String npmodality_new;
  private String npmodality_old;
  private String npflg_return;
  private String npflg_garanty;
  private int npoccurrence;
  private String npsolution;
  private String npplan;
  private long npcategoryid;
  private int npamount;
  private String npflg_option;
  
  private long npproductpriceid;
  private int npproductidkit;
  private String nppricetype;
  private long nptransactionid;
  private String npmodality;
  private double nppriceonetime;
  private double nppricerecurring;
  private long npkitid;
  private String npproductname;
  private String npcostcurrency;
  private long nppromoheaderid;
  private String nppromoname;
  private String npquantity;
  
  private long npcontractid;
  private String npcostatus;
  private String npproductmodel;
  private String npequipment;
  private int npnum_ocurrence;
  private String npssaa_contratado;
  private long npcustomerid_subscriber;
  private long npcustomerid_paymntresp;
  private String npcustcode_paymntresp;
  private String npcd_sim;
  
  private String npequipmentimei;
  private String npwarranty;
  
  private double npminuteprice;
  private int npminute;
  
  private Date npstatusdate;
  
  private String npequipmentimeistatus;  
  
  private String npphonenumber;
  //CPUENTE CAP & CAL
  private int nptiempoequipo;  
  private String npestadoContrato;
  private String npmotivoEstado;
  private String npfechaCambioEstado;
  //PHIDALGO
  private int npLevel;
    
  private String strRealModel;
  private long lRealModelId;
  private String strRealSim;
  private String strRealImei;
  private long lRealProductLineId;
  private long lSalesStructureOriginalId;
  //Por Garantia Extendida - FPICOY 12/10/2010
  private String npguaranteeExtFact;
  private int npflagvep;
  private int npnumcuotas;
  // fin nbravo
  private String npoffertDif;
  
  /* INICIO ADT-BCL-083 */
  private int npproduct_type;

  //Correctivo INICIO PM0011405
  private String orderId;
  //Correctivo FIN PM0011405

  //PRY-1049| INICIO: AMENDEZ
  private int flagCoverage;
  //PRY-1049 | FIN: AMENDEZ

  //PRY-1200| INICIO: AMENDEZ
  private Integer npclassid;
  //PRY-1200 | FIN: AMENDEZ

  public int getNpproduct_type() {
        return npproduct_type;
    }

  public void setNpproduct_type(int npproduct_type) {
        this.npproduct_type = npproduct_type;
    }

 /*  FIN ADT-BCL-083  */
  
  public int getNpflagvep() {
    return this.npflagvep;
  }
  
  public int getNpnumcuotas() {
    return this.npnumcuotas;
  }
  public String getOffertDif() {
        return this.npoffertDif;
  }

  public double getNpcost() {
    return this.npcost;
  }
  
  public String getNpcreatedby() {
    return this.npcreatedby;
  }
  
  public Date getNpcreateddate() {
    return this.npcreateddate;
  }
  
  public String getNpcurrency() {
    return this.npcurrency;
  }
  
  public char getNpflginstall() {
    return this.npflginstall;
  }
  
  public char getNpflgrepairable() {
    return this.npflgrepairable;
  }
  
  public String getNpinventorycode() {
    return this.npinventorycode;
  }
  
  public char getNpinventoryflag() {
    return this.npinventoryflag;
  }
  
  public char getNpkit() {
    return this.npkit;
  }
  
  public String getNpmodifiedby() {
    return this.npmodifiedby;
  }
  
  public Date getNpmodifieddate() {
    return this.npmodifieddate;
  }
  
  public String getNpname() {
    return this.npname;
  }
  
  public String getNpnote() {
    return this.npnote;
  }
  
  public long getNpplanid() {
    return this.npplanid;
  }
  
  public long getNpproductid() {
    return this.npproductid;
  }
  
  public long getNpproductlineid() {
    return this.npproductlineid;
  }
  
  public String getNpstatus() {
    return this.npstatus;
  }
  
  public String getNpsubtype() {
    return this.npsubtype;
  }
  
  public void setNpcost(double npcost) {
    this.npcost = npcost;
  }
  
  public void setNpcreatedby(String npcreatedby) {
    this.npcreatedby = npcreatedby;
  }
  
  public void setNpcreateddate(Date npcreateddate) {
    this.npcreateddate = npcreateddate;
  }
  
  public void setNpcurrency(String npcurrency) {
    this.npcurrency = npcurrency;
  }
  
  public void setNpflginstall(char npflginstall) {
    this.npflginstall = npflginstall;
  }
  
  public void setNpflgrepairable(char npflgrepairable) {
    this.npflgrepairable = npflgrepairable;
  }
  
  public void setNpinventorycode(String npinventorycode) {
    this.npinventorycode = npinventorycode;
  }
  
  public void setNpinventoryflag(char npinventoryflag) {
    this.npinventoryflag = npinventoryflag;
  }
  
  public void setNpkit(char npkit) {
    this.npkit = npkit;
  }
  
  public void setNpmodifiedby(String npmodifiedby) {
    this.npmodifiedby = npmodifiedby;
  }
  
  public void setNpmodifieddate(Date npmodifieddate) {
    this.npmodifieddate = npmodifieddate;
  }
  
  public void setNpname(String npname) {
    this.npname = npname;
  }
  
  public void setNpnote(String npnote) {
    this.npnote = npnote;
  }
  
  public void setNpplanid(long npplanid) {
    this.npplanid = npplanid;
  }
  
  public void setNpproductid(long npproductid) {
    this.npproductid = npproductid;
  }
  
  public void setNpproductlineid(long npproductlineid) {
    this.npproductlineid = npproductlineid;
  }
  
  public void setNpstatus(String npstatus) {
    this.npstatus = npstatus;
  }
  
  public void setNpsubtype(String npsubtype) {
    this.npsubtype = npsubtype;
  }

  public void setNpcustomerid(long npcustomerid)  {
    this.npcustomerid = npcustomerid;
  }

  public long getNpcustomerid()  {
    return npcustomerid;
  }

  public void setNpproductid_new(long npproductid_new)  {
    this.npproductid_new = npproductid_new;
  }

  public long getNpproductid_new()  {
    return npproductid_new;
  }

  public void setNpproductid_old(long npproductid_old)  {
    this.npproductid_old = npproductid_old;
  }

  public long getNpproductid_old()  {
    return npproductid_old;
  }

  public void setNpmodality_new(String npmodality_new)  {
    this.npmodality_new = npmodality_new;
  }

  public String getNpmodality_new()  {
    return npmodality_new;
  }

  public void setNpmodality_old(String npmodality_old)  {
    this.npmodality_old = npmodality_old;
  }

  public String getNpmodality_old()  {
    return npmodality_old;
  }

  public void setNpflg_return(String npflg_return)  {
    this.npflg_return = npflg_return;
  }

  public String getNpflg_return()  {
    return npflg_return;
  }

  public void setNpflg_garanty(String npflg_garanty)  {
    this.npflg_garanty = npflg_garanty;
  }

  public String getNpflg_garanty()  {
    return npflg_garanty;
  }

  public void setNpoccurrence(int npoccurrence)  {
    this.npoccurrence = npoccurrence;
  }

  public int getNpoccurrence()  {
    return npoccurrence;
  }

  public void setNpsolution(String npsolution)  {
    this.npsolution = npsolution;
  }

  public String getNpsolution()  {
    return npsolution;
  }

  public void setNpplan(String npplan)  {
    this.npplan = npplan;
  }

  public String getNpplan()  {
    return npplan;
  }

  public void setNpcategoryid(long npcategoryid)  {
    this.npcategoryid = npcategoryid;
  }

  public long getNpcategoryid()  {
    return npcategoryid;
  }

  public void setNpamount(int npamount)  {
    this.npamount = npamount;
  }

  public int getNpamount()  {
    return npamount;
  }

  public void setNpflg_option(String npflg_option)  {
    this.npflg_option = npflg_option;
  }

  public String getNpflg_option()  {
    return npflg_option;
  }


  public void setNpproductpriceid(long npproductpriceid)
  {
    this.npproductpriceid = npproductpriceid;
  }


  public long getNpproductpriceid()
  {
    return npproductpriceid;
  }


  public void setNpproductidkit(int npproductidkit)
  {
    this.npproductidkit = npproductidkit;
  }


  public int getNpproductidkit()
  {
    return npproductidkit;
  }


  public void setNppricetype(String nppricetype)
  {
    this.nppricetype = nppricetype;
  }


  public String getNppricetype()
  {
    return nppricetype;
  }


  public void setNptransactionid(long nptransactionid)
  {
    this.nptransactionid = nptransactionid;
  }


  public long getNptransactionid()
  {
    return nptransactionid;
  }


  public void setNpmodality(String npmodality)
  {
    this.npmodality = npmodality;
  }


  public String getNpmodality()
  {
    return npmodality;
  }


  public void setNppriceonetime(double nppriceonetime)
  {
    this.nppriceonetime = nppriceonetime;
  }


  public double getNppriceonetime()
  {
    return nppriceonetime;
  }


  public void setNppricerecurring(double nppricerecurring)
  {
    this.nppricerecurring = nppricerecurring;
  }


  public double getNppricerecurring()
  {
    return nppricerecurring;
  }


  public void setNpkitid(long npkitid)
  {
    this.npkitid = npkitid;
  }


  public long getNpkitid()
  {
    return npkitid;
  }


  public void setNpproductname(String npproductname)
  {
    this.npproductname = npproductname;
  }


  public String getNpproductname()
  {
    return npproductname;
  }


  public void setNpcostcurrency(String npcostcurrency)
  {
    this.npcostcurrency = npcostcurrency;
  }


  public String getNpcostcurrency()
  {
    return npcostcurrency;
  }


  public void setNppromoheaderid(long nppromoheaderid)
  {
    this.nppromoheaderid = nppromoheaderid;
  }


  public long getNppromoheaderid()
  {
    return nppromoheaderid;
  }


  public void setNppromoname(String nppromoname)
  {
    this.nppromoname = nppromoname;
  }


  public String getNppromoname()
  {
    return nppromoname;
  }


  public void setNpsolutionid(long npsolutionid)
  {
    this.npsolutionid = npsolutionid;
  }


  public long getNpsolutionid()
  {
    return npsolutionid;
  }


  public void setNpquantity(String npquantity)
  {
    this.npquantity = npquantity;
  }


  public String getNpquantity()
  {
    return npquantity;
  }


  public void setNpcontractid(long npcontractid)
  {
    this.npcontractid = npcontractid;
  }


  public long getNpcontractid()
  {
    return npcontractid;
  }


  public void setNpcostatus(String npcostatus)
  {
    this.npcostatus = npcostatus;
  }


  public String getNpcostatus()
  {
    return npcostatus;
  }


  public void setNpproductmodel(String npproductmodel)
  {
    this.npproductmodel = npproductmodel;
  }


  public String getNpproductmodel()
  {
    return npproductmodel;
  }


  public void setNpequipment(String npequipment)
  {
    this.npequipment = npequipment;
  }


  public String getNpequipment()
  {
    return npequipment;
  }


  public void setNpnum_ocurrence(int npnum_ocurrence)
  {
    this.npnum_ocurrence = npnum_ocurrence;
  }


  public int getNpnum_ocurrence()
  {
    return npnum_ocurrence;
  }


  public void setNpssaa_contratado(String npssaa_contratado)
  {
    this.npssaa_contratado = npssaa_contratado;
  }


  public String getNpssaa_contratado()
  {
    return npssaa_contratado;
  }


  public void setNpcustomerid_subscriber(long npcustomerid_subscriber)
  {
    this.npcustomerid_subscriber = npcustomerid_subscriber;
  }


  public long getNpcustomerid_subscriber()
  {
    return npcustomerid_subscriber;
  }


  public void setNpcustomerid_paymntresp(long npcustomerid_paymntresp)
  {
    this.npcustomerid_paymntresp = npcustomerid_paymntresp;
  }


  public long getNpcustomerid_paymntresp()
  {
    return npcustomerid_paymntresp;
  }


  public void setNpcustcode_paymntresp(String npcustcode_paymntresp)
  {
    this.npcustcode_paymntresp = npcustcode_paymntresp;
  }


  public String getNpcustcode_paymntresp()
  {
    return npcustcode_paymntresp;
  }


  public void setNpcd_sim(String npcd_sim)
  {
    this.npcd_sim = npcd_sim;
  }


  public String getNpcd_sim()
  {
    return npcd_sim;
  }


  public void setNpequipmentimei(String npequipmentimei)
  {
    this.npequipmentimei = npequipmentimei;
  }


  public String getNpequipmentimei()
  {
    return npequipmentimei;
  }


  public void setNpwarranty(String npwarranty)
  {
    this.npwarranty = npwarranty;
  }


  public String getNpwarranty()
  {
    return npwarranty;
  }


  public void setNpminuteprice(double npminuteprice)
  {
    this.npminuteprice = npminuteprice;
  }


  public double getNpminuteprice()
  {
    return npminuteprice;
  }


  public void setNpminute(int npminute)
  {
    this.npminute = npminute;
  }


  public int getNpminute()
  {
    return npminute;
  }
  
  public void setNpstatusdate(Date npstatusdate) {
	this.npstatusdate = npstatusdate;
  }
  
  public Date getNpstatusdate() {
	 return this.npstatusdate;
  }


  public void setNpmodel(String npmodel)
  {
    this.npmodel = npmodel;
  }


  public String getNpmodel()
  {
    return npmodel;
  }
  /***Modificaciones Lee ROSALES*/

  public void setNpequipmentimeistatus(String npequipmentimeistatus)
  {
    this.npequipmentimeistatus = npequipmentimeistatus;
  }


  public String getNpequipmentimeistatus()
  {
    return npequipmentimeistatus;
  }


  public void setNpsiteid(long npsiteid)
  {
    this.npsiteid = npsiteid;
  }


  public long getNpsiteid()
  {
    return npsiteid;
  }


  public void setNpphonenumber(String npphonenumber)
  {
    this.npphonenumber = npphonenumber;
  }


  public String getNpphonenumber()
  {
    return npphonenumber;
  }

//CPUENTE CAP & CAL
    public void setNptiempoequipo(int nptiempoequipo) {
        this.nptiempoequipo= nptiempoequipo;
    }
    
    public int  getNptiempoequipo()
    {return nptiempoequipo;}

    public String getNpestadoContrato() {
        return npestadoContrato;
    }

    public void setNpestadoContrato(String npestadoContrato) {
        this.npestadoContrato = npestadoContrato;
    }

    public String getNpmotivoEstado() {
        return npmotivoEstado;
    }

    public void setNpmotivoEstado(String npmotivoEstado) {
        this.npmotivoEstado = npmotivoEstado;
    }

    public String getNpfechaCambioEstado() {
        return npfechaCambioEstado;
    }

    public void setNpfechaCambioEstado(String npfechaCambioEstado) {
        this.npfechaCambioEstado = npfechaCambioEstado;
    }


  public void setNpLevel(int npLevel)
  {
    this.npLevel = npLevel;
  }


  public int getNpLevel()
  {
    return npLevel;
  }

  public void setStrRealModel(String strRealModel)
  {
    this.strRealModel = strRealModel;
  }


  public String getStrRealModel()
  {
    return strRealModel;
  }


  public void setLRealModelId(long lRealModelId)
  {
    this.lRealModelId = lRealModelId;
  }


  public long getLRealModelId()
  {
    return lRealModelId;
  }


  public void setStrRealSim(String strRealSim)
  {
    this.strRealSim = strRealSim;
  }


  public String getStrRealSim()
  {
    return strRealSim;
  }


  public void setStrRealImei(String strRealImei)
  {
    this.strRealImei = strRealImei;
  }


  public String getStrRealImei()
  {
    return strRealImei;
  }


  public void setLRealProductLineId(long lRealProductLineId)
  {
    this.lRealProductLineId = lRealProductLineId;
  }


  public long getLRealProductLineId()
  {
    return lRealProductLineId;
  }

  
  public void setSalesStructureOriginalId(long lSalesStructureOriginalId)
  {
    this.lSalesStructureOriginalId = lSalesStructureOriginalId;
  }


  public long getSalesStructureOriginalId()
  {
    return lSalesStructureOriginalId;
  }

  public void setNpguaranteeExtFact(String npguaranteeExtFact)
  {
    this.npguaranteeExtFact = npguaranteeExtFact;
  }


  public String getNpguaranteeExtFact()
  {
    return npguaranteeExtFact;
  }


  public void setNpflagvep(int npflagvep)
  {
    this.npflagvep = npflagvep;
  }


  public void setNpnumcuotas(int npnumcuotas)
  {
    this.npnumcuotas = npnumcuotas;
  }

  public void setOffertDif(String npoffertDif)
    {
        this.npoffertDif = npoffertDif;
    }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  //PRY-1049 | INICIO: AMENDEZ
  public int getFlagCoverage() {
    return flagCoverage;
  }

  public void setFlagCoverage(int flagCoverage) {
    this.flagCoverage = flagCoverage;
  }
  //PRY-1049 | FIN: AMENDEZ

    //PRY-1200 | INICIO: AMENDEZ
    public Integer getNpclassid() {
        return npclassid;
    }

    public void setNpclassid(Integer npclassid) {
        this.npclassid = npclassid;
    }

  @Override
  public String toString() {
    return "ProductBean{" +
            "npproductid=" + npproductid +
            ", npproductlineid=" + npproductlineid +
            ", npsolutionid=" + npsolutionid +
            ", npname='" + npname + '\'' +
            ", npmodel='" + npmodel + '\'' +
            ", npnote='" + npnote + '\'' +
            ", npinventorycode='" + npinventorycode + '\'' +
            ", npinventoryflag=" + npinventoryflag +
            ", npsubtype='" + npsubtype + '\'' +
            ", npkit=" + npkit +
            ", npflginstall=" + npflginstall +
            ", npflgrepairable=" + npflgrepairable +
            ", npcost=" + npcost +
            ", npcurrency='" + npcurrency + '\'' +
            ", npcreatedby='" + npcreatedby + '\'' +
            ", npcreateddate=" + npcreateddate +
            ", npmodifiedby='" + npmodifiedby + '\'' +
            ", npmodifieddate=" + npmodifieddate +
            ", npstatus='" + npstatus + '\'' +
            ", npplanid=" + npplanid +
            ", npcustomerid=" + npcustomerid +
            ", npsiteid=" + npsiteid +
            ", npproductid_new=" + npproductid_new +
            ", npproductid_old=" + npproductid_old +
            ", npmodality_new='" + npmodality_new + '\'' +
            ", npmodality_old='" + npmodality_old + '\'' +
            ", npflg_return='" + npflg_return + '\'' +
            ", npflg_garanty='" + npflg_garanty + '\'' +
            ", npoccurrence=" + npoccurrence +
            ", npsolution='" + npsolution + '\'' +
            ", npplan='" + npplan + '\'' +
            ", npcategoryid=" + npcategoryid +
            ", npamount=" + npamount +
            ", npflg_option='" + npflg_option + '\'' +
            ", npproductpriceid=" + npproductpriceid +
            ", npproductidkit=" + npproductidkit +
            ", nppricetype='" + nppricetype + '\'' +
            ", nptransactionid=" + nptransactionid +
            ", npmodality='" + npmodality + '\'' +
            ", nppriceonetime=" + nppriceonetime +
            ", nppricerecurring=" + nppricerecurring +
            ", npkitid=" + npkitid +
            ", npproductname='" + npproductname + '\'' +
            ", npcostcurrency='" + npcostcurrency + '\'' +
            ", nppromoheaderid=" + nppromoheaderid +
            ", nppromoname='" + nppromoname + '\'' +
            ", npquantity='" + npquantity + '\'' +
            ", npcontractid=" + npcontractid +
            ", npcostatus='" + npcostatus + '\'' +
            ", npproductmodel='" + npproductmodel + '\'' +
            ", npequipment='" + npequipment + '\'' +
            ", npnum_ocurrence=" + npnum_ocurrence +
            ", npssaa_contratado='" + npssaa_contratado + '\'' +
            ", npcustomerid_subscriber=" + npcustomerid_subscriber +
            ", npcustomerid_paymntresp=" + npcustomerid_paymntresp +
            ", npcustcode_paymntresp='" + npcustcode_paymntresp + '\'' +
            ", npcd_sim='" + npcd_sim + '\'' +
            ", npequipmentimei='" + npequipmentimei + '\'' +
            ", npwarranty='" + npwarranty + '\'' +
            ", npminuteprice=" + npminuteprice +
            ", npminute=" + npminute +
            ", npstatusdate=" + npstatusdate +
            ", npequipmentimeistatus='" + npequipmentimeistatus + '\'' +
            ", npphonenumber='" + npphonenumber + '\'' +
            ", nptiempoequipo=" + nptiempoequipo +
            ", npestadoContrato='" + npestadoContrato + '\'' +
            ", npmotivoEstado='" + npmotivoEstado + '\'' +
            ", npfechaCambioEstado='" + npfechaCambioEstado + '\'' +
            ", npLevel=" + npLevel +
            ", strRealModel='" + strRealModel + '\'' +
            ", lRealModelId=" + lRealModelId +
            ", strRealSim='" + strRealSim + '\'' +
            ", strRealImei='" + strRealImei + '\'' +
            ", lRealProductLineId=" + lRealProductLineId +
            ", lSalesStructureOriginalId=" + lSalesStructureOriginalId +
            ", npguaranteeExtFact='" + npguaranteeExtFact + '\'' +
            ", npflagvep=" + npflagvep +
            ", npnumcuotas=" + npnumcuotas +
            ", npoffertDif='" + npoffertDif + '\'' +
            ", npproduct_type=" + npproduct_type +
            ", orderId='" + orderId + '\'' +
            ", flagCoverage=" + flagCoverage +
                ", npclassid=" + npclassid +
            '}';
  }

    //PRY-1200 | FIN: AMENDEZ
}