package pe.com.nextel.bean;

import java.io.Serializable;

import pe.com.nextel.util.GenericObject;


public class RepairBean extends GenericObject implements Serializable {
  private static final long serialVersionUID = 1L;
	private long nprepairid;
	private long nporderid;
	private long npcodeosiptel;
	private String npreception;
	private String npimei;
	private String npsim;
	private String npimeisn;
	private String npmodel;
	private String npphone;
	private String npequipment;
	private String npwarrantnextel;
	private String npwarrantfactory;
	private String npimeichange;
	private String npimeiloan;
	private String npstatus;
	private long npfailureid;
	private long npresolutionid;
	private String npservicetype;
   
   private String npservicecode;
   
   
	private String npendsituation;
	private String nprepairtype;
	private String npaccessorytype;
	private String npaccessorymodel;
	private String npcollectcontact;
	private String npcollectaddress;
	private String npcontactfirstname;
	private String npcontactlastname;
	private String npdescriptionnextel;
	private String npdescriptionfactory;
	private String npfrequency;
   
   private String npfechaActivacion;   
   
   private String npprovider;   
   private String npgarantia_extendida;   
   private String npgarantia_bounce;   
   private String npgarantia_Truebounce;
   private String npgarantia_refurbish;   
   private String npgarantia_fabricante;
   private long npmesesAdicional;   
   private String npfallas_seleccionadas;
   private String nprepuestos_seleccionados;   
   private String nprepuestos_n_u;
   private String npservicio;
   private String npFnc;
   private String nprRepSinCosto;
   private long npproviderid;      
   private long npitemid;
   private String npgarantia_reparacion;
   private String npgarantia_truebounce;   
   
   private String npfechaActOracle;   
   
   private String npdiagncode;   
   private String nprecepcion;   
   private String npbuildingid;   
   private String npdescservicecode;   
   private String npdesctypeservicecode;
   
   
   private String npselldateacc;   
      
   private String nptimervalue;
         
   private String nporiginal;
   
   private String npinventorycode;
   
   //private long npreplacetype;
   
   private String nptechnology;
   
   private String NPEQUIPGAMA;
   
   private String NPFABRICATOR;
   private String NPREPAIRCENTER;
   private String NPWARRANSER;
   private String NPWARRANOCUR;  
   
   private String NPFABRICATORID;
   private String NPREPAIRCENTERID;
  
   private String NPFINALSTATE;
   private String NPEQUIPSO;
   
   private String NPINDRETAIL;
   
   private String npguaranteeExtFact;//HPPTT+ Garantia Extendida - 12/10/2010 - FPICOY
   private String npmodelnew;
   private String npmarca;
   
   //CDELOSSANTOS PARA EL PROYECTO RAE
   private String npemailcontact;
   private String npphonecontact;
   private String npwarrantdap;
   private String npstorepickup;
   private String npcloseddate;
   private String npnotifpref;

   private Long nprepairid_bof;
   private String npotras_fallas_seleccionadas;
   private String NPDIAGNOSTICDETAIL;
   private String NPASSESSOROBSERVATION;   

    //portega
    private String readOnlyFromRepairCenter;
  
	private OrderBean orderBean = new OrderBean();

	public void setNprepairid(long nprepairid) {
		this.nprepairid = nprepairid;
	}

	public long getNprepairid() {
		return nprepairid;
	}

	public void setNporderid(long nporderid) {
		this.nporderid = nporderid;
	}

	public long getNporderid() {
		return nporderid;
	}

	public void setNpcodeosiptel(long npcodeosiptel) {
		this.npcodeosiptel = npcodeosiptel;
	}

	public long getNpcodeosiptel() {
		return npcodeosiptel;
	}

	public void setNpreception(String npreception) {
		this.npreception = npreception;
	}

	public String getNpreception() {
		return npreception;
	}

	public void setNpimei(String npimei) {
		this.npimei = npimei;
	}

	public String getNpimei() {
		return npimei;
	}

	public void setNpsim(String npsim) {
		this.npsim = npsim;
	}

	public String getNpsim() {
		return npsim;
	}

	public void setNpimeisn(String npimeisn) {
		this.npimeisn = npimeisn;
	}

	public String getNpimeisn() {
		return npimeisn;
	}

	public void setNpmodel(String npmodel) {
		this.npmodel = npmodel;
	}

	public String getNpmodel() {
		return npmodel;
	}

	public void setNpphone(String npphone) {
		this.npphone = npphone;
	}

	public String getNpphone() {
		return npphone;
	}

	public void setNpequipment(String npequipment) {
		this.npequipment = npequipment;
	}

	public String getNpequipment() {
		return npequipment;
	}

	public void setNpwarrantnextel(String npwarrantnextel) {
		this.npwarrantnextel = npwarrantnextel;
	}

	public String getNpwarrantnextel() {
		return npwarrantnextel;
	}

	public void setNpwarrantfactory(String npwarrantfactory) {
		this.npwarrantfactory = npwarrantfactory;
	}

	public String getNpwarrantfactory() {
		return npwarrantfactory;
	}

	public void setNpimeichange(String npimeichange) {
		this.npimeichange = npimeichange;
	}

	public String getNpimeichange() {
		return npimeichange;
	}

	public void setNpimeiloan(String npimeiloan) {
		this.npimeiloan = npimeiloan;
	}

	public String getNpimeiloan() {
		return npimeiloan;
	}

	public void setNpstatus(String npstatus) {
		this.npstatus = npstatus;
	}

	public String getNpstatus() {
		return npstatus;
	}

	public void setNpfailureid(long npfailureid) {
		this.npfailureid = npfailureid;
	}

	public long getNpfailureid() {
		return npfailureid;
	}

	public void setNpresolutionid(long npresolutionid) {
		this.npresolutionid = npresolutionid;
	}

	public long getNpresolutionid() {
		return npresolutionid;
	}

	public void setNpservicetype(String npservicetype) {
		this.npservicetype = npservicetype;
	}

	public String getNpservicetype() {
		return npservicetype;
	}

	public void setNpendsituation(String npendsituation) {
		this.npendsituation = npendsituation;
	}

	public String getNpendsituation() {
		return npendsituation;
	}

	public void setNprepairtype(String nprepairtype) {
		this.nprepairtype = nprepairtype;
	}

	public String getNprepairtype() {
		return nprepairtype;
	}

	public void setNpaccessorytype(String npaccessorytype) {
		this.npaccessorytype = npaccessorytype;
	}

	public String getNpaccessorytype() {
		return npaccessorytype;
	}

	public void setNpaccessorymodel(String npaccessorymodel) {
		this.npaccessorymodel = npaccessorymodel;
	}

	public String getNpaccessorymodel() {
		return npaccessorymodel;
	}

	public void setNpcollectcontact(String npcollectcontact) {
		this.npcollectcontact = npcollectcontact;
	}

	public String getNpcollectcontact() {
		return npcollectcontact;
	}

	public void setNpcollectaddress(String npcollectaddress) {
		this.npcollectaddress = npcollectaddress;
	}

	public String getNpcollectaddress() {
		return npcollectaddress;
	}

	public void setNpcontactfirstname(String npcontactfirstname) {
		this.npcontactfirstname = npcontactfirstname;
	}

	public String getNpcontactfirstname() {
		return npcontactfirstname;
	}

	public void setNpcontactlastname(String npcontactlastname) {
		this.npcontactlastname = npcontactlastname;
	}

	public String getNpcontactlastname() {
		return npcontactlastname;
	}

	public void setNpdescriptionnextel(String npdescriptionnextel) {
		this.npdescriptionnextel = npdescriptionnextel;
	}

	public String getNpdescriptionnextel() {
		return npdescriptionnextel;
	}

	public void setNpdescriptionfactory(String npdescriptionfactory) {
		this.npdescriptionfactory = npdescriptionfactory;
	}

	public String getNpdescriptionfactory() {
		return npdescriptionfactory;
	}
	
	public void setNpfrequency(String npfrequency) {
		this.npfrequency = npfrequency;
	}

	public String getNpfrequency() {
		return npfrequency;
	}
	
	public void setOrderBean(OrderBean orderBean) {
		this.orderBean = orderBean;
	}

	public OrderBean getOrderBean() {
		return orderBean;
	}	


	/*public void setFechaActivacion(Timestamp npfechaActivacion ) {
	   this.npfechaActivacion = npfechaActivacion;
	}

   public Timestamp getFechaActivacion() {
	   return this.npfechaActivacion;
	}*/



   
   public void setNpprovider(String npprovider)
   {
      this.npprovider = npprovider;
   }   

   public String getNpprovider() {
	   return this.npprovider;
	}
 
 
   public void setNpgarantia_extendida(String npgarantia_extendida) {
		this.npgarantia_extendida = npgarantia_extendida;
	}

	public String getNpgarantia_extendida() {
		return npgarantia_extendida;
	}


   public void setNpgarantia_bounce(String npgarantia_bounce) {
		this.npgarantia_bounce = npgarantia_bounce;
	}

	public String getNpgarantia_bounce() {
		return npgarantia_bounce;
	}
 
  
   public void setNpgarantia_refurbish(String npgarantia_refurbish) {
		this.npgarantia_refurbish = npgarantia_refurbish;
	}

	public String getNpgarantia_refurbish() {
		return npgarantia_refurbish;
	}
   
            
   public void setNpgarantia_fabricante(String npgarantia_fabricante) {
		this.npgarantia_fabricante = npgarantia_fabricante;
	}

	public String getNpgarantia_fabricante() {
		return npgarantia_fabricante;
	}
      
   
   public void setNpmesesAdicional(long npmesesAdicional) {
		this.npmesesAdicional = npmesesAdicional;
	}

	public long getNpmesesAdicional() {
		return npmesesAdicional;
	}
                        




         
   public void setNpfallas_seleccionadas(String npfallas_seleccionadas) {
      this.npfallas_seleccionadas = npfallas_seleccionadas;
	}

	public String getNpfallas_seleccionadas() {
		return npfallas_seleccionadas;
	}
   
   public void setNprepuestos_seleccionados (String nprepuestos_seleccionados) {
      this.nprepuestos_seleccionados = nprepuestos_seleccionados;
	}

	public String getNprepuestos_seleccionados() {
		return nprepuestos_seleccionados;
	}
   
   public void setNprepuestos_n_u(String nprepuestos_n_u) {
      this.nprepuestos_n_u = nprepuestos_n_u;
	}

	public String getNprepuestos_n_u() {
		return nprepuestos_n_u;
	}
   
   
      
   public void setNpservicio(String npservicio) {
      this.npservicio = npservicio;
	}

	public String getNpservicio() {
		return npservicio;
	}


   public void setNpFnc(String npFnc)
   {
      this.npFnc = npFnc;
   }


   public String getNpFnc()
   {
      return npFnc;
   }


   public void setNprRepSinCosto(String nprRepSinCosto)
   {
      this.nprRepSinCosto = nprRepSinCosto;
   }


   public String getNprRepSinCosto()
   {
      return nprRepSinCosto;
   }


   public void setNpfechaActivacion(String npfechaActivacion)
   {
      this.npfechaActivacion = npfechaActivacion;
   }


   public String getNpfechaActivacion()
   {
      return npfechaActivacion;
   }


   public void setNpproviderid(long npproviderid)
   {
      this.npproviderid = npproviderid;
   }


   public long getNpproviderid()
   {
      return npproviderid;
   }


   public void setNpitemid(long npitemid)
   {
      this.npitemid = npitemid;
   }


   public long getNpitemid()
   {
      return npitemid;
   }


   public void setNpgarantia_reparacion(String npgarantia_reparacion)
   {
      this.npgarantia_reparacion = npgarantia_reparacion;
   }


   public String getNpgarantia_reparacion()
   {
      return npgarantia_reparacion;
   }


   public void setNpgarantia_truebounce(String npgarantia_truebounce)
   {
      this.npgarantia_truebounce = npgarantia_truebounce;
   }


   public String getNpgarantia_truebounce()
   {
      return npgarantia_truebounce;
   }


   public void setNpgarantia_Truebounce(String npgarantia_Truebounce)
   {
      this.npgarantia_Truebounce = npgarantia_Truebounce;
   }


   public String getNpgarantia_Truebounce()
   {
      return npgarantia_Truebounce;
   }


   public void setNpservicecode(String npservicecode)
   {
      this.npservicecode = npservicecode;
   }


   public String getNpservicecode()
   {
      return npservicecode;
   }


   public void setNpfechaActOracle(String npfechaActOracle)
   {
      this.npfechaActOracle = npfechaActOracle;
   }


   public String getNpfechaActOracle()
   {
      return npfechaActOracle;
   }


   public void setNpdiagncode(String npdiagncode)
   {
      this.npdiagncode = npdiagncode;
   }


   public String getNpdiagncode()
   {
      return npdiagncode;
   }


   public void setNprecepcion(String nprecepcion)
   {
      this.nprecepcion = nprecepcion;
   }


   public String getNprecepcion()
   {
      return nprecepcion;
   }


   public void setNpbuildingid(String npbuildingid)
   {
      this.npbuildingid = npbuildingid;
   }


   public String getNpbuildingid()
   {
      return npbuildingid;
   }


   public void setNpdescservicecode(String npdescservicecode)
   {
      this.npdescservicecode = npdescservicecode;
   }


   public String getNpdescservicecode()
   {
      return npdescservicecode;
   }


   public void setNpdesctypeservicecode(String npdesctypeservicecode)
   {
      this.npdesctypeservicecode = npdesctypeservicecode;
   }


   public String getNpdesctypeservicecode()
   {
      return npdesctypeservicecode;
   }


   public void setNporiginal(String nporiginal)
   {
      this.nporiginal = nporiginal;
   }


   public String getNporiginal()
   {
      return nporiginal;
   }


   public void setNptimervalue(String nptimervalue)
   {
      this.nptimervalue = nptimervalue;
   }


   public String getNptimervalue()
   {
      return nptimervalue;
   }


   public void setNpselldateacc(String npselldateacc)
   {
      this.npselldateacc = npselldateacc;
   }


   public String getNpselldateacc()
   {
      return npselldateacc;
   }


   public void setNpinventorycode(String npinventorycode)
   {
      this.npinventorycode = npinventorycode;
   }


   public String getNpinventorycode()
   {
      return npinventorycode;
   }


  /*public void setNpreplacetype(long npreplacetype)
  {
    this.npreplacetype = npreplacetype;
  }


  public long getNpreplacetype()
  {
    return npreplacetype;
  }*/
 
   public void setNptechnology(String nptechnology)
   {
      this.nptechnology = nptechnology;
   }


   public String getNptechnology()
   {
      return nptechnology;
   }


  public void setNPEQUIPGAMA(String NPEQUIPGAMA)
  {
    this.NPEQUIPGAMA = NPEQUIPGAMA;
  }


  public String getNPEQUIPGAMA()
  {
    return NPEQUIPGAMA;
  }



  public void setNPFABRICATOR(String NPFABRICATOR)
  {
    this.NPFABRICATOR = NPFABRICATOR;
  }


  public String getNPFABRICATOR()
  {
    return NPFABRICATOR;
  }


  public void setNPREPAIRCENTER(String NPREPAIRCENTER)
  {
    this.NPREPAIRCENTER = NPREPAIRCENTER;
  }


  public String getNPREPAIRCENTER()
  {
    return NPREPAIRCENTER;
  }


  public void setNPWARRANSER(String NPWARRANSER)
  {
    this.NPWARRANSER = NPWARRANSER;
  }


  public String getNPWARRANSER()
  {
    return NPWARRANSER;
  }


  public void setNPWARRANOCUR(String NPWARRANOCUR)
  {
    this.NPWARRANOCUR = NPWARRANOCUR;
  }


  public String getNPWARRANOCUR()
  {
    return NPWARRANOCUR;
  }


  public void setNPFABRICATORID(String NPFABRICATORID)
  {
    this.NPFABRICATORID = NPFABRICATORID;
  }


  public String getNPFABRICATORID()
  {
    return NPFABRICATORID;
  }


  public void setNPREPAIRCENTERID(String NPREPAIRCENTERID)
  {
    this.NPREPAIRCENTERID = NPREPAIRCENTERID;
  }


  public String getNPREPAIRCENTERID()
  {
    return NPREPAIRCENTERID;
  }


  public void setNPFINALSTATE(String NPFINALSTATE)
  {
    this.NPFINALSTATE = NPFINALSTATE;
  }


  public String getNPFINALSTATE()
  {
    return NPFINALSTATE;
  }


  public void setNPEQUIPSO(String NPEQUIPSO)
  {
    this.NPEQUIPSO = NPEQUIPSO;
  }


  public String getNPEQUIPSO()
  {
    return NPEQUIPSO;
  }


  public void setNPINDRETAIL(String NPINDRETAIL)
  {
    this.NPINDRETAIL = NPINDRETAIL;
  }


  public String getNPINDRETAIL()
  {
    return NPINDRETAIL;
  }


  public void setNpguaranteeExtFact(String npguaranteeExtFact)
  {
    this.npguaranteeExtFact = npguaranteeExtFact;
  }


  public String getNpguaranteeExtFact()
  {
    return npguaranteeExtFact;
  }

  public void setNpmodelnew(String npmodelnew)
  {
    this.npmodelnew = npmodelnew;
  }


  public String getNpmodelnew()
  {
    return npmodelnew;
  }


  public void setNpmarca(String npmarca)
  {
    this.npmarca = npmarca;
  }


  public String getNpmarca()
  {
    return npmarca;
  }
  
    public void setNpemailcontact(String npemailcontact)
    {
      this.npemailcontact = npemailcontact;
    }
    public String getNpemailcontact()
    {
      return npemailcontact;
    }
    
    public void setNpphonecontact(String npphonecontact)
    {
      this.npphonecontact = npphonecontact;
    }
    public String getNpphonecontact()
    {
      return npphonecontact;
    }
    
    public void setNpwarrantdap(String npwarrantdap)
    {
      this.npwarrantdap = npwarrantdap;
    }
    public String getNpwarrantdap()
    {
      return npwarrantdap;
    }
    
    public void setNpstorepickup(String npstorepickup)
    {
      this.npstorepickup = npstorepickup;
    }
    public String getNpstorepickup()
    {
      return npstorepickup;
    }
    
    public void setNpcloseddate(String npcloseddate)
    {
      this.npcloseddate = npcloseddate;
    }
    public String getNpcloseddate()
    {
      return npcloseddate;
    }

    public void setNpnotifpref(String npnotifpref) {
        this.npnotifpref = npnotifpref;
    }

    public String getNpnotifpref() {
        return npnotifpref;
    }

    public void setNprepairid_bof(Long nprepairid_bof) {
        this.nprepairid_bof = nprepairid_bof;
    }

    public Long getNprepairid_bof() {
        return nprepairid_bof;
    }

    public void setNpotras_fallas_seleccionadas(String npotras_fallas_seleccionadas) {
        this.npotras_fallas_seleccionadas = npotras_fallas_seleccionadas;
    }

    public String getNpotras_fallas_seleccionadas() {
        return npotras_fallas_seleccionadas;
    }

    public void setNPDIAGNOSTICDETAIL(String NPDIAGNOSTICDETAIL) {
        this.NPDIAGNOSTICDETAIL = NPDIAGNOSTICDETAIL;
    }

    public String getNPDIAGNOSTICDETAIL() {
        return NPDIAGNOSTICDETAIL;
    }

    public void setNPASSESSOROBSERVATION(String NPASSESSOROBSERVATION) {
        this.NPASSESSOROBSERVATION = NPASSESSOROBSERVATION;
    }

    public String getNPASSESSOROBSERVATION() {
        return NPASSESSOROBSERVATION;
    }

    public void setReadOnlyFromRepairCenter(String readOnlyFromRepairCenter) {
        this.readOnlyFromRepairCenter = readOnlyFromRepairCenter;
    }

    public String getReadOnlyFromRepairCenter() {
        return readOnlyFromRepairCenter;
    }
}
