package pe.com.nextel.bean;

import java.io.Serializable;

import pe.com.nextel.util.GenericObject;


public class FinalSuspensionBean extends GenericObject implements Serializable {
    
    // inicio - campos para el reporte de suspensiones
    private static final long serialVersionUID = 1L;
    private String npBscsId;
    private String npName;
    private String npCycle;
    private String npCreateSupDate;
    private String npSuspensionDate;
    private String npScoringCust;
    private String npCustomerType;
    private String npCustomerState;
    private String npSuspensionPhone;
    private String npSuspensionState;
    private String npGiro;
    private String npRegion;
    private long npCustomerId;
    private String npCreatedBy;
    private String SWSTATUS;
    private String ST_SUSPENSION;
    private String TOTAL_ITEMS;
    private String NPCLOSEDDATE;
    private String CO_ID;
    private String npCategoriaOrden;
    private String npStatusBSCS;
    private String npMotivoBSCS;
    
    // fin - campos para el reporte de suspensiones
        
    public FinalSuspensionBean() {        
    }
    
   public String getNpCategoriaOrden(){
      return npCategoriaOrden;
   }   
   public void setNpCategoriaOrden(String npCategoriaOrden){
      this.npCategoriaOrden = npCategoriaOrden;
   }
    
   public String getStatusBSCS(){
      return npStatusBSCS;
   }   
   public void setNpStatusBSCS(String npStatusBSCS){
      this.npStatusBSCS = npStatusBSCS;
   }
   
   public String getNpMotivoBSCS(){
      return npMotivoBSCS;
   }   
   public void setNpMotivoBSCS(String npMotivoBSCS){
      this.npMotivoBSCS = npMotivoBSCS;
   } 
    
    public String getNpBscsId()
   {
      return npBscsId;
   }
   
     public void setNpBscsId(String npBscsId)
   {
      this.npBscsId = npBscsId;
   }

   public void setNpName(String npName)
   {
      this.npName = npName;
   }
   
   public String getNpName()
   {
      return npName;
   }
   
   public void setNpCustomerId(long npCustomerId)
   {
    this.npCustomerId = npCustomerId;
   }

   public long getNpCustomerId()
   {
    return npCustomerId;
   }


      

   public void setNpCycle(String npCycle)
   {
      this.npCycle = npCycle;
   }

   public String getNpCycle()
   {
      return npCycle;
   }

   public void setNpCreateSupDate(String npCreateSupDate)
   {
      this.npCreateSupDate = npCreateSupDate;
   }

   public String getNpCreateSupDate()
   {
      return npCreateSupDate;
   }

   public void setNpSuspensionDate(String npSuspensionDate)
   {
      this.npSuspensionDate = npSuspensionDate;
   }

   public String getNpSuspensionDate()
   {
      return npSuspensionDate;
   }

   public void setNpScoringCust(String npScoringCust)
   {
      this.npScoringCust = npScoringCust;
   }

   public String getNpScoringCust()
   {
      return npScoringCust;
   }

   public void setNpCustomerType(String npCustomerType)
   {
      this.npCustomerType = npCustomerType;
   }

   public String getNpCustomerType()
   {
      return npCustomerType;
   }


   public void setNpCustomerState(String npCustomerState)
   {
      this.npCustomerState = npCustomerState;
   }


   public String getNpCustomerState()
   {
      return npCustomerState;
   }

   public void setNpSuspensionPhone(String npSuspensionPhone)
   {
      this.npSuspensionPhone = npSuspensionPhone;
   }

   public String getNpSuspensionPhone()
   {
      return npSuspensionPhone;
   }

   public void setNpSuspensionState(String npSuspensionState)
   {
      this.npSuspensionState = npSuspensionState;
   }

   public String getNpSuspensionState()
   {
      return npSuspensionState;
   }

   public void setNpGiro(String npGiro)
   {
      this.npGiro = npGiro;
   }

   public String getNpGiro()
   {
      return npGiro;
   }

   public void setNpRegion(String npRegion)
   {
      this.npRegion = npRegion;
   }

   public String getNpRegion()
   {
      return npRegion;
   }
   
   public void setNpCreatedBy(String npCreatedBy) {
           this.npCreatedBy = npCreatedBy;
   }
    public String getNpCreatedBy() {
           return this.npCreatedBy;
   }   
            
   public void setST_SUSPENSION(String ST_SUSPENSION) {
           this.ST_SUSPENSION = ST_SUSPENSION;
   }
    public String getST_SUSPENSION() {
           return this.ST_SUSPENSION;
   }         
   public void setTOTAL_ITEMS(String TOTAL_ITEMS) {
           this.TOTAL_ITEMS = TOTAL_ITEMS;
   }
    public String getTOTAL_ITEMS() {
           return this.TOTAL_ITEMS;
   }
   
   public void setNPCLOSEDDATE(String NPCLOSEDDATE) {
           this.NPCLOSEDDATE = NPCLOSEDDATE;
   }
    public String getNPCLOSEDDATE() {
           return this.NPCLOSEDDATE;
   }  


  public void setCO_ID(String CO_ID)
  {    this.CO_ID = CO_ID;
  }


  public String getCO_ID()
  {    return CO_ID;
  }
   
}