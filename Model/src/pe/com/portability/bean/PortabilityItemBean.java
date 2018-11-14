package pe.com.portability.bean;

import java.io.Serializable;

import java.sql.Date;

import pe.com.nextel.util.GenericObject;


public class PortabilityItemBean extends GenericObject implements Serializable
{
  private static final long serialVersionUID = 1L;



    public PortabilityItemBean()
  {
  }
  
  private String npportaborderid;
  private String npportabitemid;
  private String messagetype;
  private String npphonenumber;
  private long npPortabItemId;
  private long npPortabOrderId;
  private String npStatusPhoneBSCS;
  private String npexecutiondatereturn;
  private String npScheduleDelayBSCS;
  private String npComment;
  private String npRejectType;
  private String npmodificationby;
  private Date npmodificationdate;
  private String npReasonType;
    private String npReasonDesc;
  private String npPlanTarifarioname;
  private String npServiceContract;
  private String npServiceContractStatus;
  private String npLimitTimerDate;
  private long npSequenceId;
  
  private String npPhoneNumberWN;
  


  public void setNpportaborderid(String npportaborderid)
  {
    this.npportaborderid = npportaborderid;
  }


  public String getNpportaborderid()
  {
    return npportaborderid;
  }


  public void setNpportabitemid(String npportabitemid)
  {
    this.npportabitemid = npportabitemid;
  }


  public String getNpportabitemid()
  {
    return npportabitemid;
  }


  public void setMessagetype(String messagetype)
  {
    this.messagetype = messagetype;
  }


  public String getMessagetype()
  {
    return messagetype;
  }


  public void setNpphonenumber(String npphonenumber)
  {
    this.npphonenumber = npphonenumber;
  }


  public String getNpphonenumber()
  {
    return npphonenumber;
  }
  
  
   public void setNpPortabItemId(long npPortabItemId)
  {
    this.npPortabItemId = npPortabItemId;
  }


  public long getNpPortabItemId()
  {
    return npPortabItemId;
  }
  
   public void setNpPortabOrderId(long npPortabOrderId)
  {
    this.npPortabOrderId = npPortabOrderId;
  }


  public long getNpPortabOrderId()
  {
    return npPortabOrderId;
  }
  
   public void setnNpStatusPhoneBSCS(String npStatusPhoneBSCS) 
  {
    this.npStatusPhoneBSCS = npStatusPhoneBSCS;
  }
  
  public String getNpStatusPhoneBSCS()
  {
    return npStatusPhoneBSCS;
  }
 
 
  public void setNpexecutiondatereturn(String npexecutiondatereturn) {
      this.npexecutiondatereturn = npexecutiondatereturn;
  }

  public String getNpexecutiondatereturn() {
      return npexecutiondatereturn;
 }
 
  public void setNpScheduleDelayBSCS(String npScheduleDelayBSCS) 
  {
    this.npScheduleDelayBSCS = npScheduleDelayBSCS;
  }
  
  public String getNpScheduleDelayBSCS()
  {
    return npScheduleDelayBSCS;
  }
  
  public void setNpComment(String npComment) 
  {
    this.npComment = npComment;
  }
  
  public String getNpComment()
  {
    return npComment;
  }
  
   public void setNpRejectType(String npRejectType) 
  {
    this.npRejectType = npRejectType;
  }
  
  public String getNpRejectType()
  {
    return npRejectType;
  }
  
    public void setNpmodificationby(String npmodificationby) {
        this.npmodificationby = npmodificationby;
    }

    public String getNpmodificationby() {
        return npmodificationby;
    }
    
    public void setNpmodificationdate(Date npmodificationdate) {
        this.npmodificationdate = npmodificationdate;
    }

    public Date getNpmodificationdate() {
        return npmodificationdate;
    }
    
    public void setNpPlanTarifarioname(String npPlanTarifarioname) {
        this.npPlanTarifarioname = npPlanTarifarioname;
    }

    public String getNpPlanTarifarioname() {
        return npPlanTarifarioname;
    }
    
    public void setNpServiceContract(String npServiceContract) {
        this.npServiceContract = npServiceContract;
    }

    public String getNpServiceContract() {
        return npServiceContract;
    }
    
    
    public void setNpServiceContractStatus(String npServiceContractStatus) {
        this.npServiceContract = npServiceContractStatus;
    }

    public String getNpServiceContractStatus() {
        return npServiceContractStatus;
    }
    
    
    
    public void setNpReasonType(String npReasonType) {
        this.npReasonType = npReasonType;
    }

    public String getNpReasonType() {
        return npReasonType;
    }
    
    public void setNpLimitTimerDate(String npLimitTimerDate) {
        this.npLimitTimerDate = npLimitTimerDate;
    }

    public String getNpLimitTimerDate() {
        return npLimitTimerDate;
    }
    
    
    public void setNpSequenceId(long npSequenceId)
    {
    this.npSequenceId = npSequenceId;
    }

    public long getNpSequenceId()
    {
      return npSequenceId;
    }


   public void setNpPhoneNumberWN(String npPhoneNumberWN)
   {
      this.npPhoneNumberWN = npPhoneNumberWN;
   }


   public String getNpPhoneNumberWN()
   {
      return npPhoneNumberWN;
   }


    public String getNpReasonDesc() {
        return npReasonDesc;
    }

    public void setNpReasonDesc(String npReasonDesc) {
        this.npReasonDesc = npReasonDesc;
    }

}