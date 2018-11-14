package pe.com.nextel.bean;

import java.io.Serializable;

public class SiteBean implements Serializable{
  private static final long serialVersionUID = 1L;
    private long swSiteId;
    private String swSiteName;
    private String npCodBscs;
    private long swRegionId;
    private String swRegionName;
    private String npStatus;
    private String swCreatedBy;
    private long npLineaCredito;       
    
    
    private long swcustomerid;
    private String swofficecode;
    private String swofficephonearea;
    private String swofficephone;
    private String swfaxarea; 
    private String swfax;
    private long npsolutiongroupid;
    private String strSolutionGroup;


    public SiteBean() {
    }


    public void setSwSiteName(String swSiteName) {
        this.swSiteName = swSiteName;
    }

    public String getSwSiteName() {
        return this.swSiteName;
    }

    public void setNpCodBscs(String npCodBscs) {
        this.npCodBscs = npCodBscs;
    }

    public String getNpCodBscs() {
        return this.npCodBscs;
    }

    public void setSwRegionName(String swRegionName) {
        this.swRegionName = swRegionName;
    }

    public String getSwRegionName() {
        return this.swRegionName;
    }

    public void setNpStatus(String npStatus) {
        this.npStatus = npStatus;
    }

    public String getNpStatus() {
        return this.npStatus;
    }

    public void setSwCreatedBy(String swCreatedBy) {
        this.swCreatedBy = swCreatedBy;
    }

    public String getSwCreatedBy() {
        return this.swCreatedBy;
    }


    public void setNpLineaCredito(long npLineaCredito) {
        this.npLineaCredito = npLineaCredito;
    }

    public long getNpLineaCredito() {
        return this.npLineaCredito;
    }


  public void setSwRegionId(long swRegionId)
  {
    this.swRegionId = swRegionId;
  }


  public long getSwRegionId()
  {
    return swRegionId;
  }


  public void setSwcustomerid(long swcustomerid)
  {
    this.swcustomerid = swcustomerid;
  }


  public long getSwcustomerid()
  {
    return swcustomerid;
  }


  public void setSwofficecode(String swofficecode)
  {
    this.swofficecode = swofficecode;
  }


  public String getSwofficecode()
  {
    return swofficecode;
  }


  public void setSwofficephonearea(String swofficephonearea)
  {
    this.swofficephonearea = swofficephonearea;
  }


  public String getSwofficephonearea()
  {
    return swofficephonearea;
  }


  public void setSwofficephone(String swofficephone)
  {
    this.swofficephone = swofficephone;
  }


  public String getSwofficephone()
  {
    return swofficephone;
  }


  public void setSwfaxarea(String swfaxarea)
  {
    this.swfaxarea = swfaxarea;
  }


  public String getSwfaxarea()
  {
    return swfaxarea;
  }


  public void setSwfax(String swfax)
  {
    this.swfax = swfax;
  }


  public String getSwfax()
  {
    return swfax;
  }
  
  /*public ArrayList getNoChangeAddress(long lCustomerId,long lSiteId,String strCustomerType,String strSiteType){
      ArrayList arrListaAddress=new ArrayList();
      if (lSiteId ==0 ){
         if ("Customer".equals(strCustomerType)){           
            arrListaAddress.add("DOMICILIO");
            arrListaAddress.add("FACTURACION");
            arrListaAddress.add("ENTREGA");                     
         }
      }else{      
         if ("Customer".equals(strSiteType)){         
            arrListaAddress.add("FACTURACION");
            arrListaAddress.add("ENTREGA");                                 
         }
      }
      return arrListaAddress;
  }*/

  /*public ArrayList getMandatoryContact(String strType){
      ArrayList arrListaAddress=new ArrayList();
      HashMap hshData = new HashMap();      
      if ( "Customer".equals(strType)){ 
           hshData.put("contact","VENTAS");            
      }else if ("Prospect".equals(strType)){     
           hshData.put("contact","VENTAS");           
      }
      arrListaAddress.add(hshData);                                    
      return arrListaAddress;
  }*/

  public void setSwSiteId(long swSiteId)
  {
    this.swSiteId = swSiteId;
  }


  public long getSwSiteId()
  {
    return swSiteId;
  }

  

  public void setStrSolutionGroup(String strSolutionGroup)
  {
    this.strSolutionGroup = strSolutionGroup;
  }


  public String getStrSolutionGroup()
  {
    return strSolutionGroup;
  }


  public void setNpsolutiongroupid(long npsolutiongroupid)
  {
    this.npsolutiongroupid = npsolutiongroupid;
  }


  public long getNpsolutiongroupid()
  {
    return npsolutiongroupid;
  }


}
