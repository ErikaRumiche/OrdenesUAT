package pe.com.nextel.bean;

import java.io.Serializable;

import java.util.Date;


public class SpecificationBean implements Serializable{
  private static final long serialVersionUID = 1L;
    
    private long nppagesectionid;
    private long npSpecificationId;
    private long npSolutionId;
	  private long npDivisionId;
    private String npsolutionname;
	 private String npdivisionname;
    private String npcategorynname;
    private String npSpecification;
    private String npType;
    private String npStatus;
    private String npAllowUpdateOwnerBy;
    private String npAllowOwnEquipment;
    private String npAllowAdditionalService;
    private String npAllowConsignment;
    private String npAllowMinuteSooling;
    private int npExceptionvalidation;
    private String npExceptionType;
    private String npExceptionDetail;
    private String npSitedataInputFlag;
    private Date npCreatedDate;
    private String npCreatedBy;
    
    private String nppagenpname;
    private String npname;
    private String npeventname;
    private String npeventhandler;
    private String nptypeobject;
    private String npobjectname;
    private String npbusinessobject;
    
    private String npAllowbagpooling;
    private String npAllowInputDatasite;
    private String npRasprocess;
    private String npGrossprocess;
    private String npUpdatesalesbscs;
    private int npInvoice;
    private int npPicking;
    private String npTelecomunication;
    private String npBpelflowgroup;
    private String npPricetype;
    
    private int npValiditem;
    
   
    private long npSolutiongroupid;
    private String npSolutiontype;

    private String npEstadoItemId;
    private String npEstadoItemName;
    
    
    public void setNpSpecificationId(long npSpecificationId) {
           this.npSpecificationId = npSpecificationId;
       }
    public long getNpSpecificationId() {
           return this.npSpecificationId;
       }
    public void setNpSolutionId(long npSolutionId) {
           this.npSolutionId = npSolutionId;
       }
    public long getNpSolutionId() {
           return this.npSolutionId;
       }

    public void setNpSpecification(String npSpecification) {
           this.npSpecification = npSpecification;
       }
    public String getNpSpecification() {
           return this.npSpecification;
       }    
    
    public void setNpType(String npType) {
           this.npType = npType;
       }
    public String getNpType() {
           return this.npType;
       }      
    
    public void setNpStatus(String npStatus) {
           this.npStatus = npStatus;
       }
    public String getNpStatus() {
           return this.npStatus;
       }     
       
    public void setNpAllowUpdateOwnerBy(String npAllowUpdateOwnerBy) {
           this.npAllowUpdateOwnerBy = npAllowUpdateOwnerBy;
       }
    public String getNpAllowUpdateOwnerBy() {
           return this.npAllowUpdateOwnerBy;
       }         
    
    public void setNpAllowOwnEquipment(String npAllowOwnEquipment) {
           this.npAllowOwnEquipment = npAllowOwnEquipment;
       }
    public String getNpAllowOwnEquipment() {
           return this.npAllowOwnEquipment;
       }         
    
    public void setNpAllowAdditionalService(String npAllowAdditionalService) {
           this.npAllowAdditionalService = npAllowAdditionalService;
       }
    public String getNpAllowAdditionalService() {
           return this.npAllowAdditionalService;
       }         
    
    public void setNpAllowConsignment(String npAllowConsignment) {
           this.npAllowConsignment = npAllowConsignment;
       }
    public String getNpAllowConsignment() {
           return this.npAllowConsignment;
       }         
    
    public void setNpAllowMinuteSooling(String npAllowMinuteSooling) {
           this.npAllowMinuteSooling = npAllowMinuteSooling;
       }
    public String getNpAllowMinuteSooling() {
           return this.npAllowMinuteSooling;
       }         
    
    public void setNpExceptionvalidation(int npExceptionvalidation) {
           this.npExceptionvalidation = npExceptionvalidation;
       }
    public int getNpExceptionvalidation() {
           return this.npExceptionvalidation;
       }     
    
    public void setNpExceptionType(String npExceptionType) {
           this.npExceptionType = npExceptionType;
       }
    public String getNpExceptionType() {
           return this.npExceptionType;
       }     
    
    public void setNpExceptionDetail(String npExceptionDetail) {
           this.npExceptionDetail = npExceptionDetail;
       }
    public String getNpExceptionDetail() {
           return this.npExceptionDetail;
       } 
 
    public void setNpSitedataInputFlag(String npSitedataInputFlag) {
           this.npSitedataInputFlag = npSitedataInputFlag;
       }
    public String getNpSitedataInputFlag() {
           return this.npSitedataInputFlag;
       } 

    public void setNpCreatedDate(Date npCreatedDate) {
           this.npCreatedDate = npCreatedDate;
       }
    public Date getNpCreatedDate() {
           return this.npCreatedDate;
       }
    public void setNpCreatedBy(String npCreatedBy) {
           this.npCreatedBy = npCreatedBy;
       }
    public String getNpCreatedBy() {
           return this.npCreatedBy;
       }

    public void setNpname(String npname) {
        this.npname = npname;
    }

    public String getNpname() {
        return npname;
    }

    public void setNpeventname(String npeventname) {
        this.npeventname = npeventname;
    }

    public String getNpeventname() {
        return npeventname;
    }

    public void setNpeventhandler(String npeventhandler) {
        this.npeventhandler = npeventhandler;
    }

    public String getNpeventhandler() {
        return npeventhandler;
    }

    public void setNptypeobject(String nptypeobject) {
        this.nptypeobject = nptypeobject;
    }

    public String getNptypeobject() {
        return nptypeobject;
    }

    public void setNpobjectname(String npobjectname) {
        this.npobjectname = npobjectname;
    }

    public String getNpobjectname() {
        return npobjectname;
    }

    public void setNpbusinessobject(String npbusinessobject) {
        this.npbusinessobject = npbusinessobject;
    }

    public String getNpbusinessobject() {
        return npbusinessobject;
    }

    public void setNppagesectionid(long nppagesectionid) {
        this.nppagesectionid = nppagesectionid;
    }

    public long getNppagesectionid() {
        return nppagesectionid;
    }

    public void setNppagenpname(String nppagenpname) {
        this.nppagenpname = nppagenpname;
    }

    public String getNppagenpname() {
        return nppagenpname;
    }


  public void setNpAllowbagpooling(String npAllowbagpooling)
  {
    this.npAllowbagpooling = npAllowbagpooling;
  }


  public String getNpAllowbagpooling()
  {
    return npAllowbagpooling;
  }


  public void setNpAllowInputDatasite(String npAllowInputDatasite)
  {
    this.npAllowInputDatasite = npAllowInputDatasite;
  }


  public String getNpAllowInputDatasite()
  {
    return npAllowInputDatasite;
  }


  public void setNpRasprocess(String npRasprocess)
  {
    this.npRasprocess = npRasprocess;
  }


  public String getNpRasprocess()
  {
    return npRasprocess;
  }


  public void setNpGrossprocess(String npGrossprocess)
  {
    this.npGrossprocess = npGrossprocess;
  }


  public String getNpGrossprocess()
  {
    return npGrossprocess;
  }


  public void setNpUpdatesalesbscs(String npUpdatesalesbscs)
  {
    this.npUpdatesalesbscs = npUpdatesalesbscs;
  }


  public String getNpUpdatesalesbscs()
  {
    return npUpdatesalesbscs;
  }


  public void setNpInvoice(int npInvoice)
  {
    this.npInvoice = npInvoice;
  }


  public int getNpInvoice()
  {
    return npInvoice;
  }


  public void setNpPicking(int npPicking)
  {
    this.npPicking = npPicking;
  }


  public int getNpPicking()
  {
    return npPicking;
  }


  public void setNpTelecomunication(String npTelecomunication)
  {
    this.npTelecomunication = npTelecomunication;
  }


  public String getNpTelecomunication()
  {
    return npTelecomunication;
  }


  public void setNpBpelflowgroup(String npBpelflowgroup)
  {
    this.npBpelflowgroup = npBpelflowgroup;
  }


  public String getNpBpelflowgroup()
  {
    return npBpelflowgroup;
  }


  public void setNpPricetype(String npPricetype)
  {
    this.npPricetype = npPricetype;
  }


  public String getNpPricetype()
  {
    return npPricetype;
  }


  public void setNpsolutionname(String npsolutionname)
  {
    this.npsolutionname = npsolutionname;
  }


  public String getNpsolutionname()
  {
    return npsolutionname;
  }


  public void setNpcategorynname(String npcategorynname)
  {
    this.npcategorynname = npcategorynname;
  }


  public String getNpcategorynname()
  {
    return npcategorynname;
  }
  
  
  public void setNpValiditem(int npValiditem)
  {
    this.npValiditem = npValiditem;
  }


  public int getNpValiditem()
  {
    return npValiditem;
  }


	public void setNpDivisionId(long npDivisionId)
	{
		this.npDivisionId = npDivisionId;
	}


	public long getNpDivisionId()
	{
		return npDivisionId;
	}


	public void setNpdivisionname(String npdivisionname)
	{
		this.npdivisionname = npdivisionname;
	}


	public String getNpdivisionname()
	{
		return npdivisionname;
	}
  
 
  
  public void setNpSolutiongroupid(long npSolutiongroupid)
	{
		this.npSolutiongroupid = npSolutiongroupid;
	}


	public long getNpSolutiongroupid()
	{
		return npSolutiongroupid;
	}
  
  
  
  public void setNpSolutiontype(String npSolutiontype)
	{
		this.npSolutiontype = npSolutiontype;
	}


	public String getNpSolutiontype()
	{
		return npSolutiontype;
	} 


  public void setNpEstadoItemId(String npEstadoItemId)
  {
    this.npEstadoItemId = npEstadoItemId;
  }


  public String getNpEstadoItemId()
  {
    return npEstadoItemId;
  }


  public void setNpEstadoItemName(String npEstadoItemName)
  {
    this.npEstadoItemName = npEstadoItemName;
  }


  public String getNpEstadoItemName()
  {
    return npEstadoItemName;
  }
   
}
