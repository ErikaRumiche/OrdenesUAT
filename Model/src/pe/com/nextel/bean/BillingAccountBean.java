package pe.com.nextel.bean;

import java.io.Serializable;

import java.sql.Date;


/**
 * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
 * <br>Fecha: 18/09/2007
 */
public class BillingAccountBean  implements Serializable{
  private static final long serialVersionUID = 1L;
  private String npBillacCName;
    private String npBillacCName2;
    private long npBillaccountNewId;
    private long npBillaccountId;
    //private int npSiteId;
    private String npBscsCustomerId;
    private String npBscsSeq;  
    
    private String npCreatedby;
    private Date npCreateddate;    
    private CustomerBean objCustomerB=new CustomerBean();
    private BillingContactBean objBillingContactB=new BillingContactBean();
    
    private long npOrderId;    
    private long npSiteId;
    
    private String npType;  

    public BillingAccountBean() {
    }

    public void setNpCreatedby(String npCreatedby) {
        this.npCreatedby = npCreatedby;
    }

    public String getNpCreatedby() {
        return npCreatedby;
    }

    public void setNpCreateddate(Date npCreateddate) {
        this.npCreateddate = npCreateddate;
    }

    public Date getNpCreateddate() {
        return npCreateddate;
    }

    public void setObjCustomerB(CustomerBean objCustomerB) {
        this.objCustomerB = objCustomerB;
    }

    public CustomerBean getObjCustomerB() {
        return objCustomerB;
    }


  public void setNpBillaccountNewId(long npBillaccountNewId)
  {
    this.npBillaccountNewId = npBillaccountNewId;
  }


  public long getNpBillaccountNewId()
  {
    return npBillaccountNewId;
  }


  public void setNpOrderId(long npOrderId)
  {
    this.npOrderId = npOrderId;
  }


  public long getNpOrderId()
  {
    return npOrderId;
  }

  public void setNpSiteId(long npSiteId)
  {
    this.npSiteId = npSiteId;
  }


  public long getNpSiteId()
  {
    return npSiteId;
  }


  public void setNpBillaccountId(long npBillaccountId)
  {
    this.npBillaccountId = npBillaccountId;
  }


  public long getNpBillaccountId()
  {
    return npBillaccountId;
  }


  public void setObjBillingContactB(BillingContactBean objBillingContactB)
  {
    this.objBillingContactB = objBillingContactB;
  }


  public BillingContactBean getObjBillingContactB()
  {
    return objBillingContactB;
  }


  public void setNpBscsCustomerId(String npBscsCustomerId)
  {
    this.npBscsCustomerId = npBscsCustomerId;
  }


  public String getNpBscsCustomerId()
  {
    return npBscsCustomerId;
  }


  public void setNpBscsSeq(String npBscsSeq)
  {
    this.npBscsSeq = npBscsSeq;
  }


  public String getNpBscsSeq()
  {
    return npBscsSeq;
  }


  public void setNpBillacCName(String npBillacCName)
  {
    this.npBillacCName = npBillacCName;
  }


  public String getNpBillacCName()
  {
    return npBillacCName;
  }


  public void setNpType(String npType)
  {
    this.npType = npType;
  }


  public String getNpType()
  {
    return npType;
  }


   public void setNpBillacCName2(String npBillacCName2)
   {
      this.npBillacCName2 = npBillacCName2;
   }


   public String getNpBillacCName2()
   {
      return npBillacCName2;
   }

}
