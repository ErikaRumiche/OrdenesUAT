package pe.com.nextel.service;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.List;
import java.util.Map;
import javax.naming.Context;
import javax.rmi.PortableRemoteObject;

import org.apache.log4j.Logger;
import pe.com.nextel.bean.BudgetBean;
import pe.com.nextel.bean.OrderBean;
import pe.com.nextel.bean.PortalSessionBean;
import pe.com.nextel.ejb.SEJBOrderEditBean;
import pe.com.nextel.ejb.SEJBOrderEditRemote;
import pe.com.nextel.ejb.SEJBOrderEditRemoteHome;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;
import pe.com.nextel.util.RequestHashMap;
import pe.com.nextel.bean.DominioBean;



/**
 * Developer : Carmen Gremios
 * Objetivo  : Interface que provee los servicios del EJB
 *             para ser consumidos por la capa Controller
 */

public class EditOrderService extends GenericService {
  
    /* INICIO: DOLANO-0001 | PRY-1049 */
    private static final Logger logger = Logger.getLogger(EditOrderService.class);
    /* FIN: DOLANO-0001 | PRY-1049 */
  
/***********************************************************************
***********************************************************************
***********************************************************************
*  INTEGRACION DE ORDENES Y RETAIL - INICIO
*  REALIZADO POR: Carmen Gremios
*  FECHA: 13/09/2007
***********************************************************************
***********************************************************************
***********************************************************************/    

/**
* Motivo: Obtiene la instancia del EJB remoto
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 13/09/2007
* @return    SEJBOrderEditRemote
*/     
public static SEJBOrderEditRemote getSEJBOrderEditRemote() {
   try{
      final Context context = MiUtil.getInitialContext();
      final SEJBOrderEditRemoteHome sEJBOrderEditRemoteHome = 
         (SEJBOrderEditRemoteHome) PortableRemoteObject.narrow( context.lookup( "SEJBOrderEdit" ), SEJBOrderEditRemoteHome.class );
      SEJBOrderEditRemote sEJBOrderEditRemote;
      sEJBOrderEditRemote = sEJBOrderEditRemoteHome.create();             
      return sEJBOrderEditRemote;
   }catch(Exception ex) {
      System.out.println("Exception : [EditOrderService][getSEJBOrderEditRemote]["+ex.getMessage()+"]");
      return null;
   }
}      
    
/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 14/09/2007
* @see pe.com.nextel.dao.OrderDAO#getTimeStamp(long)      
*/   
public int getTimeStamp(long lOrderId) {
   try{
      return getSEJBOrderEditRemote().getTimeStamp(lOrderId);    
   }catch(RemoteException re){
      System.out.println("EditOrderService getTimeStamp \nMensaje:" + re.getMessage()+"\n");
      return -1;
   }catch(SQLException ex){
      System.out.println("EditOrderService getTimeStamp \nMensaje:" + ex.getMessage()+"\n");
      return -1;           
   }
   catch(Exception ex){
      System.out.println("EditOrderService getTimeStamp \nMensaje:" + ex.getMessage()+"\n");
      return -1;           
   }
}
    
/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 15/09/2007
* @see pe.com.nextel.dao.OrderDAO#getRichExclusivity(long)      
*/   
public String getRichExclusivity(long lCustomerId) {
   try{       
      return getSEJBOrderEditRemote().getRichExclusivity(lCustomerId);     
   }catch(RemoteException re){
      System.out.println("EditOrderService getRichExclusivity \nMensaje:" + re.getMessage()+"\n");
      return null;
   }catch(SQLException ex){
      System.out.println("EditOrderService getRichExclusivity \nMensaje:" + ex.getMessage()+"\n");
      return null;           
   }
   catch(Exception ex){
      System.out.println("EditOrderService getRichExclusivity \nMensaje:" + ex.getMessage()+"\n");
      return null;           
   }
}    
   
/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 16/09/2007
* @see pe.com.nextel.dao.OrderDAO#getVendedorRegionId(int,String)      
*/   
public HashMap getVendedorRegionId(int iVendedorId) {
   try{
      return getSEJBOrderEditRemote().getVendedorRegionId(iVendedorId);    
   }catch(RemoteException re){
      System.out.println("EditOrderService getVendedorRegionId \nMensaje:" + re.getMessage()+"\n");
      HashMap hshMap=new HashMap();
      hshMap.put("strMessage",re.getMessage());
      return hshMap;  
   }catch(SQLException ex){
      System.out.println("EditOrderService getVendedorRegionId \nMensaje:" + ex.getMessage()+"\n");
      HashMap hshMap=new HashMap();
      hshMap.put("strMessage",ex.getMessage());
      return hshMap;          
   }
   catch(Exception ex){
      System.out.println("EditOrderService getVendedorRegionId \nMensaje:" + ex.getMessage()+"\n");
      HashMap hshMap=new HashMap();
      hshMap.put("strMessage",ex.getMessage());
      return hshMap;          
   }
}   
     
/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 16/09/2007
* @see pe.com.nextel.dao.OrderDAO#getVendedorRegionId(int,String)      
*/   
public String doValidateSalesName(long lCustomerId,int iSiteId,int iSpecialtyId,String strLogin,int iVendedorId,String strVendedorName, int iUserId, int iAppId) {
   try{
      return getSEJBOrderEditRemote().doValidateSalesName(lCustomerId,iSiteId,iSpecialtyId,strLogin,iVendedorId,strVendedorName,iUserId,iAppId);    
   }catch(RemoteException re){
      System.out.println("EditOrderService doValidateSalesName \nMensaje:" + re.getMessage()+"\n");
      return null;
   }catch(SQLException ex){
      System.out.println("EditOrderService doValidateSalesName \nMensaje:" + ex.getMessage()+"\n");
      return null;           
   }
   catch(Exception ex){
      System.out.println("EditOrderService doValidateSalesName \nMensaje:" + ex.getMessage()+"\n");
      return null;           
   }
}     

/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 16/09/2007
* @see pe.com.nextel.dao.OrderDAO#getVendedorRegionId(int,String)      
*/   
public HashMap getOppOwnershipChngFlag(long lCustomerId,int iSiteId,String strAccMngmnt,int iVendedorId) {
   try{
      return getSEJBOrderEditRemote().getOppOwnershipChngFlag(lCustomerId,iSiteId,strAccMngmnt,iVendedorId);        
   }catch(RemoteException re){
      System.out.println("EditOrderService getOppOwnershipChngFlag \nMensaje:" + re.getMessage()+"\n");
      HashMap hshMap=new HashMap();
      hshMap.put("strMessage",re.getMessage());
      return hshMap;     
   }catch(SQLException ex){
      System.out.println("EditOrderService getOppOwnershipChngFlag \nMensaje:" + ex.getMessage()+"\n");
      HashMap hshMap=new HashMap();
      hshMap.put("strMessage",ex.getMessage());
      return hshMap;
   }
   catch(Exception ex){
      System.out.println("EditOrderService getOppOwnershipChngFlag \nMensaje:" + ex.getMessage()+"\n");
      HashMap hshMap=new HashMap();
      hshMap.put("strMessage",ex.getMessage());
      return hshMap;
   }
}    
  
/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 16/09/2007
* @see pe.com.nextel.dao.OrderDAO#getDealer(int)      
*/   
public String getDealer(int iVendedorId) {
   try{      
      return getSEJBOrderEditRemote().getDealer(iVendedorId);   
   }catch(RemoteException re){
      System.out.println("EditOrderService getDealer \nMensaje:" + re.getMessage()+"\n");
      return null;
   }catch(SQLException ex){      
      System.out.println("EditOrderService getDealer \nMensaje:" + ex.getMessage()+"\n");
      return null;           
   }
   catch(Exception ex){      
      System.out.println("EditOrderService getDealer \nMensaje:" + ex.getMessage()+"\n");
      return null;           
   }
}     

/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 11/07/2007
* @see pe.com.nextel.dao.OrderDAO#getOrder(long,String)      
*/ 

public HashMap getOrder(long lNpOrderid){ // CABERA - RESUME, DETALLE ORDEN, EDIT CUSTOMER        
   try{   
      return getSEJBOrderEditRemote().getOrder(lNpOrderid);   
   }catch(RemoteException re){
      System.out.println("EditOrderService getOrder \nMensaje:" + re.getMessage()+"\n");
      HashMap hshMap=new HashMap();
      hshMap.put("strMessage",re.getMessage());
      return hshMap;
   }catch(SQLException ex){
      System.out.println("EditOrderService getOrder \nMensaje:" + ex.getMessage()+"\n");            
      HashMap hshMap=new HashMap();
      hshMap.put("strMessage",ex.getMessage());
      return hshMap;
   }
   catch(Exception ex){
      System.out.println("EditOrderService getOrder \nMensaje:" + ex.getMessage()+"\n");            
      HashMap hshMap=new HashMap();
      hshMap.put("strMessage",ex.getMessage());
      return hshMap;
   }
}

/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 12/08/2007
* @see pe.com.nextel.dao.OrderDAO#getBuildingName(int,String,String)      
*/      
public HashMap getBuildingName(int intBuildingid, String strLogin)      //DETALLE ORDEN         
{
   try{        
      return getSEJBOrderEditRemote().getBuildingName(intBuildingid,strLogin);     
   }catch(RemoteException re){
      HashMap hshMap=new HashMap();
      hshMap.put("strMessage",re.getMessage());
      return hshMap;
   }catch(Exception ex){
      System.out.println("EditOrderService getBuildingName \nMensaje:" + ex.getMessage()+"\n");
      HashMap hshMap=new HashMap();
      hshMap.put("strMessage",ex.getMessage());
      return hshMap;
   }
}  

/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 27/08/2007
* @see pe.com.nextel.dao.OrderDAO#getDispatchPlaceList(int,String)      
*/        
public HashMap getDispatchPlaceList(int intSpecialtyId) {  // DETALLE ORDEN         
   try{        
      return getSEJBOrderEditRemote().getDispatchPlaceList(intSpecialtyId);     
   }catch(RemoteException re){
      System.out.println("EditOrderService getDispatchPlaceList \nMensaje:" + re.getMessage()+"\n");
      HashMap hshMap=new HashMap();
      hshMap.put("strMessage",re.getMessage());
      return hshMap;
   }catch(Exception ex){
      System.out.println("EditOrderService getDispatchPlaceList \nMensaje:" + ex.getMessage()+"\n");
      HashMap hshMap=new HashMap();
      hshMap.put("strMessage",ex.getMessage());
      return hshMap;
   }
}
     
/**
* Motivo: Convierte una trama de Inbox en un listado de Inbox
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 10/09/2007
* @param     strNpBpelbackinboxs    Trama de Inbox
* @return    ArrayList
*/    
public ArrayList getInboxList(String strNpBpelbackinboxs, String strSpecification){
   /*
   String strInbox="";
   ArrayList arrLista =new ArrayList();
   HashMap hshMap=new HashMap();
   StringTokenizer stkInbox = new StringTokenizer(strNpBpelbackinboxs, Constante.DIVISOR);  
   while(stkInbox.hasMoreTokens()) {            
      hshMap=new HashMap();
      strInbox =  stkInbox.nextToken().toString();                   
      hshMap.put("datoBPel",strInbox);            
      arrLista.add(hshMap);
   }   
   */
    try {
        return getSEJBOrderEditRemote().getInboxList(strNpBpelbackinboxs, strSpecification);
    }catch (SQLException e) {
        System.out.println("Error [EditOrderService][getInboxList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
    }catch (RemoteException e) {
        return null;
    }
    catch (Exception e) {
        System.out.println("Error [EditOrderService][getInboxList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
    }
}      
 
/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 18/10/2007
* @see pe.com.nextel.dao.RejectDAO#getBankPaymentDet(int,String,String,String,String)      
*/      
public HashMap getBankPaymentDet(int iCodeBank,String strCodeService, String strRuc,
                       String strOperationNumber,String strDateVoucher)  
{  
   HashMap hshMap=new HashMap();
   try{                 
      hshMap=getSEJBOrderEditRemote().getBankPaymentDet(iCodeBank,strCodeService,strRuc,strOperationNumber,strDateVoucher);
      if (((String) hshMap.get(Constante.MESSAGE_OUTPUT)).startsWith(Constante.SUCCESS_ORA_RESULT)) {             
         hshMap.put(Constante.MESSAGE_OUTPUT,null);
      }      
      return hshMap;
   }catch(SQLException sql){
      System.out.println("EditOrderService getBankPaymentDet SQL Exception \nMensaje:" + sql.getMessage()+"\n Numero ORA: "+sql.getErrorCode()+"\n");
      String strMessage=null;
      if (sql.getErrorCode()==0) hshMap.put("strMessage",strMessage);
      else  hshMap.put("strMessage",sql.getMessage());
      return hshMap;      
   }catch(RemoteException re){
      System.out.println("EditOrderService getBankPaymentDet \nMensaje:" + re.getMessage()+"\n");
      hshMap.put("strMessage",re.getMessage());
      return hshMap;
   }catch(Exception ex){
      System.out.println("EditOrderService getBankPaymentDet \nMensaje:" + ex.getMessage()+"\n");            
      hshMap.put("strMessage",ex.getMessage());
      return hshMap;
   }
}

/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 04/09/2007
* @see pe.com.nextel.dao.OrderDAO#getOrderScreenField(long,String)      
*/  
public HashMap getOrderScreenField(long lOrderId,String strPage){     
   try{
      return getSEJBOrderEditRemote().getOrderScreenField(lOrderId,strPage);
   }catch(RemoteException re){
      System.out.println("EditOrderService getOrderScreenField \nMensaje:" + re.getMessage()+"\n");
      HashMap hshMap=new HashMap();
      hshMap.put("strMessage",re.getMessage());
      return hshMap;
   }catch(SQLException ex){
      System.out.println("EditOrderService getOrderScreenField \nMensaje:" + ex.getMessage()+"\n");            
      HashMap hshMap=new HashMap();
      hshMap.put("strMessage",ex.getMessage());
      return hshMap;
   }   
   catch(Exception ex){
      System.out.println("EditOrderService getOrderScreenField \nMensaje:" + ex.getMessage()+"\n");            
      HashMap hshMap=new HashMap();
      hshMap.put("strMessage",ex.getMessage());
      return hshMap;
   }
}     

/**
 * CPUENTE6
* <br>Realizado por: <a href="mailto:carlos.puente@nextel.com.pe">CPUENTE</a>
* <br>Fecha: 19/10/2009
* @see pe.com.nextel.dao.OrderDAO#doValidateEquipmentReplacement(String)      
*/   
public String doValidateEquipmentReplacement(long lOrderId) {
   try{
      return getSEJBOrderEditRemote().doValidateEquipmentReplacement(lOrderId);    
   }catch(RemoteException re){
      System.out.println("EditOrderService doValidateEquipmentReplacement \nMensaje:" + re.getMessage()+"\n");
      return null;
   }catch(SQLException ex){
      System.out.println("EditOrderService doValidateEquipmentReplacement \nMensaje:" + ex.getMessage()+"\n");
      return null;           
   }
   catch(Exception ex){
      System.out.println("EditOrderService doValidateEquipmentReplacement \nMensaje:" + ex.getMessage()+"\n");
      return null;           
   }
}   

/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 25/10/2007
* @see pe.com.nextel.ejb.SEJBOrderEditBean#updOrder(RequestHashMap,PortalSessionBean)      
*/
  public HashMap updOrder(RequestHashMap objHashMap,PortalSessionBean objPortalSesBean) throws Exception{
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBOrderEditRemote().updOrder(objHashMap,objPortalSesBean);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }     


/**
* <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
* <br>Fecha: 01/02/2008
* @see pe.com.nextel.ejb.SEJBOrderEditBean#updOrder(RequestHashMap,PortalSessionBean)      
*/  
  public HashMap updOrderDetail(RequestHashMap objHashMap,PortalSessionBean objPortalSesBean) throws Exception{               
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBOrderEditRemote().updOrderDetail(objHashMap,objPortalSesBean); 
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }     


/**
* <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
* <br>Fecha: 01/08/2007
* @see pe.com.nextel.dao.RejectDAO#getRejectList(long,String)      
*/       
public HashMap getRejectList(long lNpOrderid ){
   try{
      return getSEJBOrderEditRemote().getRejectList(lNpOrderid);
   }catch(RemoteException re){
      System.out.println("EditOrderService getRejectList \nMensaje:" + re.getMessage()+"\n");
      HashMap shMap=new HashMap();
      shMap.put("strMessage",re.getMessage());
      return shMap;                    
   }catch(Exception ex){
      System.out.println("EditOrderService getRejectList \nMensaje:" + ex.getMessage()+"\n");
      HashMap shMap=new HashMap();
      shMap.put("strMessage",ex.getMessage());
      return shMap;         
   }
}

  
  /**
    * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
    * <br>Fecha: 20/11/2007
    * @see pe.com.nextel.dao.OrderDAO#getPaymentListBySource(String,long)      
  */        
   public HashMap getPaymentListBySource(String strSource,long lOrderId)      
   throws Exception {      
      try{
         return getSEJBOrderEditRemote().getPaymentListBySource(strSource,lOrderId);
      }catch(RemoteException re){
         System.out.println("EditOrderService getPaymentListBySource \nMensaje:" + re.getMessage()+"\n");
         HashMap shMap=new HashMap();
         shMap.put("strMessage",re.getMessage());
         return shMap;                    
      }catch(SQLException ex){
         System.out.println("EditOrderService getPaymentListBySource \nMensaje:" + ex.getMessage()+"\n");
         HashMap shMap=new HashMap();
         shMap.put("strMessage",ex.getMessage());
         return shMap;         
      } 
      catch(Exception ex){
         System.out.println("EditOrderService getPaymentListBySource \nMensaje:" + ex.getMessage()+"\n");
         HashMap shMap=new HashMap();
         shMap.put("strMessage",ex.getMessage());
         return shMap;         
      }         
   } 
   
  /**
    * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
    * <br>Fecha: 20/11/2007
    * @see pe.com.nextel.dao.OrderDAO#generateDocumentInvBill(long,String,String,int)      
  */      
   public HashMap generateDocumentInvBill(long lOrderId,String strOrigen,String strLogin,int iBuilding)      
   throws Exception {      
      try{
         return getSEJBOrderEditRemote().generateDocumentInvBill(lOrderId,strOrigen,strLogin,iBuilding);
      }catch(RemoteException re){
         System.out.println("EditOrderService generateDocumentInvBill \nMensaje:" + re.getMessage()+"\n");
         HashMap shMap=new HashMap();
         shMap.put("strMessage",re.getMessage());
         return shMap;                    
      }catch(SQLException ex){
         System.out.println("EditOrderService generateDocumentInvBill \nMensaje:" + ex.getMessage()+"\n");
         HashMap shMap=new HashMap();
         shMap.put("strMessage",ex.getMessage());
         return shMap;         
      }
      catch(Exception ex){
         System.out.println("EditOrderService generateDocumentInvBill \nMensaje:" + ex.getMessage()+"\n");
         HashMap shMap=new HashMap();
         shMap.put("strMessage",ex.getMessage());
         return shMap;         
      }         
   }    
   
   
   /**
    * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
    * <br>Fecha: 25/06/2008
    * @see pe.com.nextel.dao.OrderDAO#generateDocumentInvBillDetail(long,String,String,int)      
  */      
   public HashMap generateDocumentInvBillDetail(long lOrderId,String strOrigen,String strLogin,int iBuilding)      
   throws Exception {      
      try{
         return getSEJBOrderEditRemote().generateDocumentInvBillDetail(lOrderId,strOrigen,strLogin,iBuilding);
      }catch(RemoteException re){
         System.out.println("EditOrderService generateDocumentInvBillDetail \nMensaje:" + re.getMessage()+"\n");
         HashMap shMap=new HashMap();
         shMap.put("strMessage",re.getMessage());
         return shMap;                    
      }catch(SQLException ex){
         System.out.println("EditOrderService generateDocumentInvBillDetail \nMensaje:" + ex.getMessage()+"\n");
         HashMap shMap=new HashMap();
         shMap.put("strMessage",ex.getMessage());
         return shMap;         
      }
      catch(Exception ex){
         System.out.println("EditOrderService generateDocumentInvBillDetail \nMensaje:" + ex.getMessage()+"\n");
         HashMap shMap=new HashMap();
         shMap.put("strMessage",ex.getMessage());
         return shMap;         
      }         
   }    

   public HashMap generatePayamentOrder(long lOrderId,String strLogin,int iBuilding)      
   throws Exception {      
      try{
         return getSEJBOrderEditRemote().generatePayamentOrder(lOrderId,strLogin,iBuilding);
      }catch(RemoteException re){
         System.out.println("EditOrderService generatePayamentOrder \nMensaje:" + re.getMessage()+"\n");
         HashMap shMap=new HashMap();
         shMap.put("strMessage",re.getMessage());
         return shMap;                    
      }catch(SQLException ex){
         System.out.println("EditOrderService generatePayamentOrder \nMensaje:" + ex.getMessage()+"\n");
         HashMap shMap=new HashMap();
         shMap.put("strMessage",ex.getMessage());
         return shMap;         
      } 
      catch(Exception ex){
         System.out.println("EditOrderService generatePayamentOrder \nMensaje:" + ex.getMessage()+"\n");
         HashMap shMap=new HashMap();
         shMap.put("strMessage",ex.getMessage());
         return shMap;         
      }         
   }     

   public String updSinchronizeActiv(long lOrderId, String strLogin)
   throws Exception {   
      String strMessage=null;
      try{
         return getSEJBOrderEditRemote().updSinchronizeActiv(lOrderId, strLogin);
      }catch(RemoteException re){
         System.out.println("EditOrderService updSinchronizeActiv \nMensaje:" + re.getMessage()+"\n");         
         strMessage=re.getMessage();
         return strMessage;                    
      }catch(SQLException ex){
         System.out.println("EditOrderService updSinchronizeActiv \nMensaje:" + ex.getMessage()+"\n");         
         strMessage=ex.getMessage();
         return strMessage;         
      }
      catch(Exception ex){
         System.out.println("EditOrderService updSinchronizeActiv \nMensaje:" + ex.getMessage()+"\n");         
         strMessage=ex.getMessage();
         return strMessage;         
      }         
   }     

   /**
   * Motivo: actualiza las notificaciones de acciónes 
   * <br>Realizado por: <a href="mailto:oliver.dubock@nextel.com.pe">Oliver Dubock</a>
   * <br>Fecha: 21/05/2008
   * @param     long  
   * @return    String
   * @return    String 
   */ 
   public String insNotificationAction(long lOrderId, String strStatus, String strLogin)
   throws Exception {   
      String strMessage=null;
      try{
         return getSEJBOrderEditRemote().insNotificationAction(lOrderId, strStatus, strLogin);
      }catch(RemoteException re){
         System.out.println("EditOrderService insNotificationAction \nMensaje:" + re.getMessage()+"\n");         
         strMessage=re.getMessage();
         return strMessage;                    
      }catch(SQLException ex){
         System.out.println("EditOrderService insNotificationAction \nMensaje:" + ex.getMessage()+"\n");         
         strMessage=ex.getMessage();
         return strMessage;         
      }
      catch(Exception ex){
         System.out.println("EditOrderService insNotificationAction \nMensaje:" + ex.getMessage()+"\n");         
         strMessage=ex.getMessage();
         return strMessage;         
      }         
   } 

   /**
   * Motivo: Verifica si el boton se puede mostrar para el speficitationid 
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 27/11/2007
   * @param     long  
   * @return    String 
   */   
   public String getShowButtom(long lSpecificationId)    
   throws Exception {   
      String strMessage=null;
      try{
         return getSEJBOrderEditRemote().getShowButtom(lSpecificationId);
      }catch(RemoteException re){
         System.out.println("EditOrderService getShowButtom \nMensaje:" + re.getMessage()+"\n");         
         strMessage=re.getMessage();
         return strMessage;                    
      }catch(SQLException ex){
         System.out.println("EditOrderService getShowButtom \nMensaje:" + ex.getMessage()+"\n");         
         strMessage=ex.getMessage();
         return strMessage;         
      }
      catch(Exception ex){
         System.out.println("EditOrderService getShowButtom \nMensaje:" + ex.getMessage()+"\n");         
         strMessage=ex.getMessage();
         return strMessage;         
      }         
   }      
   
   /**
   * Motivo: executeActionFromOrder
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 28/11/2007
   * @param     long  
   * @param     String  
   * @param     String  
   * @param     String  
   * @param     long  
   * @return    String 
   */   
   public HashMap doExecuteActionFromOrder(long lOrderId,String strStatusOld,String strStatusNew,String strLogin,long lLoginBuilding)
   throws Exception {     
      try{
         return getSEJBOrderEditRemote().doExecuteActionFromOrder(lOrderId,strStatusOld,strStatusNew,strLogin,lLoginBuilding );
      }catch(RemoteException re){
         System.out.println("EditOrderService executeActionFromOrder \nMensaje:" + re.getMessage()+"\n");         
         HashMap shMap=new HashMap();
         shMap.put("strMessage",re.getMessage());
         return shMap;                     
      }catch(SQLException ex){
         System.out.println("EditOrderService executeActionFromOrder \nMensaje:" + ex.getMessage()+"\n");         
         HashMap shMap=new HashMap();
         shMap.put("strMessage",ex.getMessage());
         return shMap;           
      }
      catch(Exception ex){
         System.out.println("EditOrderService executeActionFromOrder \nMensaje:" + ex.getMessage()+"\n");         
         HashMap shMap=new HashMap();
         shMap.put("strMessage",ex.getMessage());
         return shMap;           
      }         
   }      
   
   /**
   * Motivo: executeActionFromOrder
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 28/11/2007
   * @param     long  
   * @param     String  
   * @param     String  
   * @param     String  
   * @param     long  
   * @return    String 
   */   
   public HashMap doGenerarParteIngreso(long lOrderId,String strOrigen,String strLogin,long lLoginBuilding,String strPIType)  
   throws Exception {   
      try{
         return getSEJBOrderEditRemote().doGenerarParteIngreso(lOrderId,strOrigen,strLogin,lLoginBuilding,strPIType);
      }catch(RemoteException re){
         System.out.println("EditOrderService doGenerarParteIngreso \nMensaje:" + re.getMessage()+"\n");         
         HashMap shMap=new HashMap();
         shMap.put("strMessage",re.getMessage());
         return shMap;                    
      }catch(SQLException ex){
         System.out.println("EditOrderService doGenerarParteIngreso \nMensaje:" + ex.getMessage()+"\n");         
         HashMap shMap=new HashMap();
         shMap.put("strMessage",ex.getMessage());
         return shMap;        
      }
      catch(Exception ex){
         System.out.println("EditOrderService doGenerarParteIngreso \nMensaje:" + ex.getMessage()+"\n");         
         HashMap shMap=new HashMap();
         shMap.put("strMessage",ex.getMessage());
         return shMap;        
      }         
   }    
   
      /**
   * Motivo: 
   * <br>Realizado por: <a href="mailto:edgar.jara@nextel.com.pe">Edgar Jara</a>
   * <br>Fecha: 28/11/2007
   * @param     long  
   * @return    String 
   */   
   public HashMap doAutorizacionDevolucion(long lOrderId)  
   throws Exception {   
      try{
         return getSEJBOrderEditRemote().doAutorizacionDevolucion(lOrderId);
      }catch(RemoteException re){
         System.out.println("EditOrderService AutorizacionDevolucion \nMensaje:" + re.getMessage()+"\n");         
         HashMap shMap=new HashMap();
         shMap.put("strMessage",re.getMessage());
         return shMap;                    
      }catch(SQLException ex){
         System.out.println("EditOrderService AutorizacionDevolucion \nMensaje:" + ex.getMessage()+"\n");         
         HashMap shMap=new HashMap();
         shMap.put("strMessage",ex.getMessage());
         return shMap;        
      }
      catch(Exception ex){
         System.out.println("EditOrderService AutorizacionDevolucion \nMensaje:" + ex.getMessage()+"\n");         
         HashMap shMap=new HashMap();
         shMap.put("strMessage",ex.getMessage());
         return shMap;        
      }         
   }    

   /**
   * Motivo: 
   * <br>Realizado por: <a href="mailto:edgar.jara@nextel.com.pe">Edgar Jara</a>
   * <br>Fecha: 29/08/2008
   * @param     long  
   * @return    String 
   */   
   public String getAutorizacionDevolucion(long lOrderId,int iUserid,int iAppId )    
   throws Exception {   
      try{
         System.out.println("EditOrderService getAutorizacionDevolucion 111");         
         return getSEJBOrderEditRemote().getAutorizacionDevolucion(lOrderId,iUserid,iAppId);
      }catch(RemoteException re){
         System.out.println("EditOrderService getAutorizacionDevolucion \nMensaje:" + re.getMessage()+"\n");         
         HashMap shMap=new HashMap();
         shMap.put("strMessage",re.getMessage());
         return re.getMessage();                    
      }catch(Exception ex){
         System.out.println("EditOrderService getAutorizacionDevolucion \nMensaje:" + ex.getMessage()+"\n");         
         HashMap shMap=new HashMap();
         shMap.put("strMessage",ex.getMessage());
         return ex.getMessage();        
      }         
   }       
   
   /**
   * Motivo: 
   * <br>Realizado por: <a href="mailto:edgar.jara@nextel.com.pe">Edgar Jara</a>
   * <br>Fecha: 29/08/2008
   * @param     int  
   * @return    float 
   */   
   public float getKitEquipmentPrice(long npProduct,String npModality, long lSalesStructOrigenId)    
  {
      try{
         System.out.println("EditOrderService getKitEquipmentPrice");         
         return getSEJBOrderEditRemote().getKitEquipmentPrice(npProduct,npModality,lSalesStructOrigenId);
      }catch(RemoteException re){
         System.out.println("EditOrderService getKitEquipmentPrice \nMensaje:" + re.getMessage()+"\n");         
         HashMap shMap=new HashMap();
         shMap.put("strMessage",re.getMessage());
         return 0;
      }catch(Exception ex){
         System.out.println("EditOrderService getKitEquipmentPrice \nMensaje:" + ex.getMessage()+"\n");         
         HashMap shMap=new HashMap();
         shMap.put("strMessage",ex.getMessage());
         return 0;    
      }         
   }          
   
   /**
   * Motivo: Obtiene un listado de opciones a mostrar en el combo accion de la página de edición de Ordenes
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 24/01/2008
   * @param     iSpecificationId  
   * @param     strAction  
   * @return    HashMap 
   */   
   public HashMap getActionList(int iSpecificationId,String strAction,String strBpelbackinboxs,long lOrderId)
   throws Exception {   
      try{
         return getSEJBOrderEditRemote().getActionList(iSpecificationId,strAction,strBpelbackinboxs,lOrderId);
      }catch(RemoteException re){
         System.out.println("EditOrderService getActionList \nMensaje:" + re.getMessage()+"\n");         
         HashMap shMap=new HashMap();
         shMap.put("strMessage",re.getMessage());
         return shMap;                    
      }catch(SQLException ex){
         System.out.println("EditOrderService getActionList \nMensaje:" + ex.getMessage()+"\n");         
         HashMap shMap=new HashMap();
         shMap.put("strMessage",ex.getMessage());
         return shMap;        
      }
      catch(Exception ex){
         System.out.println("EditOrderService getActionList \nMensaje:" + ex.getMessage()+"\n");         
         HashMap shMap=new HashMap();
         shMap.put("strMessage",ex.getMessage());
         return shMap;        
      }         
   }      



   /**
   * Motivo: Obtiene el listado de Acciones.
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 28/11/2007
   * @param     ArrayList  
   * @param     long  
   * @param     String  
   * @param     String     
   * @return    HashMap 
   */   
   public HashMap getListaAccion(ArrayList objLista,int lSpecificationId,String strBPElConversationId,String strStatus)  
   throws Exception {   
      try{
         return getSEJBOrderEditRemote().getListaAccion(objLista,lSpecificationId,strBPElConversationId,strStatus);
      }catch(RemoteException re){
         System.out.println("EditOrderService getListaAccion \nMensaje:" + re.getMessage()+"\n");         
         HashMap shMap=new HashMap();
         shMap.put("strMessage",re.getMessage());
         return shMap;                    
      }catch(Exception ex){
         System.out.println("EditOrderService getListaAccion \nMensaje:" + ex.getMessage()+"\n");         
         HashMap shMap=new HashMap();
         shMap.put("strMessage",ex.getMessage());
         return shMap;        
      }         
   }        

   /**
   * Motivo: Obtiene un listado de Inboxs a los que se puede avanzar 
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 07/12/2007
   * @param     int  
   * @param     String  
   * @return    HashMap 
   */   
   public HashMap getSpecificationStatus(int iSpecificationId,String strCurrentInbox)    
   throws Exception {   
      try{
         return getSEJBOrderEditRemote().getSpecificationStatus(iSpecificationId,strCurrentInbox);
      }catch(RemoteException re){
         System.out.println("EditOrderService getSpecificationStatus \nMensaje:" + re.getMessage()+"\n");         
         HashMap shMap=new HashMap();
         shMap.put("strMessage",re.getMessage());
         return shMap;                    
      }catch(Exception ex){
         System.out.println("EditOrderService getSpecificationStatus \nMensaje:" + ex.getMessage()+"\n");         
         HashMap shMap=new HashMap();
         shMap.put("strMessage",ex.getMessage());
         return shMap;        
      }         
   } 
 

   /**
   * Motivo: Valida que Vendedor de la orden se pueda modificar 
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 18/12/2007
   * @param     int  
   * @param     int
   * @return    int
   */   
   public HashMap getFlagModifiySalesName(int iSpecificationId,int iVendedorId,int iUserId,int iAppId)    
   throws Exception {   
      HashMap shMap=new HashMap();
      try{         
         int i=getSEJBOrderEditRemote().getFlagModifiySalesName(iSpecificationId,iVendedorId,iUserId,iAppId);
         shMap.put("iRetorno",i+"");
         shMap.put("strMessage",null);
         return shMap;
      }catch(RemoteException re){
         System.out.println("EditOrderService getFlagModifiySalesName \nMensaje:" + re.getMessage()+"\n");               
         shMap.put("strMessage",re.getMessage());
         return shMap;                    
      }catch(SQLException ex){
         System.out.println("EditOrderService getFlagModifiySalesName \nMensaje:" + ex.getMessage()+"\n");         
         shMap.put("strMessage",ex.getMessage());
         return shMap;        
      }
      catch(Exception ex){
         System.out.println("EditOrderService getFlagModifiySalesName \nMensaje:" + ex.getMessage()+"\n");         
         shMap.put("strMessage",ex.getMessage());
         return shMap;        
      }         
   }    
           

   public  HashMap getCarrierList(String strParamName, String strParamStatus) 
   throws Exception {   
      HashMap shMap=new HashMap();
      try{         
         return getSEJBOrderEditRemote().getCarrierList(strParamName,strParamStatus);         
      }catch(RemoteException re){
         System.out.println("EditOrderService getCarrierList \nMensaje:" + re.getMessage()+"\n");               
         shMap.put("strMessage",re.getMessage());
         return shMap;                    
      }catch(SQLException ex){
         System.out.println("EditOrderService getCarrierList \nMensaje:" + ex.getMessage()+"\n");         
         shMap.put("strMessage",ex.getMessage());
         return shMap;        
      }  
      catch(Exception ex){
         System.out.println("EditOrderService getCarrierList \nMensaje:" + ex.getMessage()+"\n");         
         shMap.put("strMessage",ex.getMessage());
         return shMap;        
      }         
   } 
   
   public HashMap updTimeStamp(long lOrderId)      
   throws Exception {   
      HashMap shMap=new HashMap();
      try{         
         return getSEJBOrderEditRemote().updTimeStamp(lOrderId);         
      }catch(RemoteException re){
         System.out.println("EditOrderService updTimeStamp \nMensaje:" + re.getMessage()+"\n");               
         shMap.put("strMessage",re.getMessage());
         return shMap;                    
      }catch(SQLException ex){
         System.out.println("EditOrderService updTimeStamp \nMensaje:" + ex.getMessage()+"\n");         
         shMap.put("strMessage",ex.getMessage());
         return shMap;        
      }
      catch(Exception ex){
         System.out.println("EditOrderService updTimeStamp \nMensaje:" + ex.getMessage()+"\n");         
         shMap.put("strMessage",ex.getMessage());
         return shMap;        
      }         
   }    
      
   public HashMap getPayFormList(int iSpecificationId,long lCustomerId)      
   throws Exception {   
      HashMap shMap=new HashMap();
      try{         
         return getSEJBOrderEditRemote().getPayFormList(iSpecificationId,lCustomerId);         
      }catch(RemoteException re){
         System.out.println("EditOrderService getPayFormList \nMensaje:" + re.getMessage()+"\n");               
         shMap.put("strMessage",re.getMessage());
         return shMap;                    
      }catch(SQLException ex){
         System.out.println("EditOrderService getPayFormList \nMensaje:" + ex.getMessage()+"\n");         
         shMap.put("strMessage",ex.getMessage());
         return shMap;        
      }
      catch(Exception ex){
         System.out.println("EditOrderService getPayFormList \nMensaje:" + ex.getMessage()+"\n");         
         shMap.put("strMessage",ex.getMessage());
         return shMap;        
      }         
   }    
   
   
     /**
  * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
  * <br>Fecha: 06/08/2008
  * @see pe.com.nextel.dao.OrderDAO#getIsFirstInbox(int,String)      
  */   
  public HashMap getIsFirstInbox(long lOrderId) {
   try{
      return getSEJBOrderEditRemote().getIsFirstInbox(lOrderId);        
   }catch(RemoteException re){
      System.out.println("EditOrderService getIsFirstInbox \nMensaje:" + re.getMessage()+"\n");
      HashMap hshMap=new HashMap();
      hshMap.put("strMessage",re.getMessage());
      return hshMap;     
   }catch(SQLException ex){
      System.out.println("EditOrderService getIsFirstInbox \nMensaje:" + ex.getMessage()+"\n");
      HashMap hshMap=new HashMap();
      hshMap.put("strMessage",ex.getMessage());
      return hshMap;
   }
   catch(Exception ex){
      System.out.println("EditOrderService getIsFirstInbox \nMensaje:" + ex.getMessage()+"\n");
      HashMap hshMap=new HashMap();
      hshMap.put("strMessage",ex.getMessage());
      return hshMap;
   }
  }    

   
/***********************************************************************
***********************************************************************
***********************************************************************
*  INTEGRACION DE ORDENES Y RETAIL - FIN
*  REALIZADO POR: Carmen Gremios
*  FECHA: 13/09/2007
***********************************************************************
***********************************************************************
***********************************************************************/   

	public HashMap OrderPassForInventory(long lOrderId){        
	   try{        
		  return getSEJBOrderEditRemote().OrderPassForInventory(lOrderId);           
	   }catch(SQLException sql){
		  System.out.println("EditOrderService OrderPassForInventory \nMensaje:" + sql.getMessage()+"\n");
		  HashMap hshData=new HashMap();
		  hshData.put("strMessage",sql.getMessage());             
		  return hshData;           
	   }catch(RemoteException re){
		  System.out.println("EditOrderService OrderPassForInventory \nMensaje:" + re.getMessage()+"\n");
		  HashMap hshData=new HashMap();
		  hshData.put("strMessage",re.getMessage());             
		  return hshData;      
	   }catch(Exception ex){
		  System.out.println("EditOrderService OrderPassForInventory \nMensaje:" + ex.getMessage()+"\n");            
		  HashMap hshData=new HashMap();
		  hshData.put("strMessage",ex.getMessage());             
		  return hshData;    
	   }
	}   

   public HashMap getOrderDetailFlow(long lOrderId,String strLogin)      
   throws Exception {   
      HashMap shMap=new HashMap();
      try{         
         //return getSEJBOrderEditRemote().getOrderDetailFlow(lOrderId,strLogin);      
         return getSEJBOrderEditRemote().getOrder(lOrderId); 
      }catch(RemoteException re){
         System.out.println("EditOrderService getOrderDetailFlow \nMensaje:" + re.getMessage()+"\n");               
         shMap.put("strMessage",re.getMessage());
         return shMap;                    
      }catch(SQLException ex){
         System.out.println("EditOrderService getOrderDetailFlow \nMensaje:" + ex.getMessage()+"\n");         
         shMap.put("strMessage",ex.getMessage());
         return shMap;        
      } 
      catch(Exception ex){
         System.out.println("EditOrderService getOrderDetailFlow \nMensaje:" + ex.getMessage()+"\n");         
         shMap.put("strMessage",ex.getMessage());
         return shMap;        
      }         
   }
   
  public HashMap getActionItem(long lOrderId,String sOperacionItem,String estadoPagoActual) {
   try{
      return getSEJBOrderEditRemote().getActionItem(lOrderId,sOperacionItem,estadoPagoActual);  
   }catch(RemoteException re){
      System.out.println("EditOrderService getActionItem \nMensaje:" + re.getMessage()+"\n");
      HashMap hshMap=new HashMap();
      hshMap.put("strMessage",re.getMessage());
      return hshMap;     
   }catch(SQLException ex){
      System.out.println("EditOrderService getActionItem \nMensaje:" + ex.getMessage()+"\n");
      HashMap hshMap=new HashMap();
      hshMap.put("strMessage",ex.getMessage());
      return hshMap;
   }
   catch(Exception ex){
      System.out.println("EditOrderService getActionItem \nMensaje:" + ex.getMessage()+"\n");
      HashMap hshMap=new HashMap();
      hshMap.put("strMessage",ex.getMessage());
      return hshMap;
   }
  }      
  

      /**
   * Motivo: 
   * <br>Realizado por: <a href="mailto:patricia.castillo@nextel.com.pe">Edgar Jara</a>
   * <br>Fecha: 28/11/2007
   * @param     long  
   * @return    String 
   */   
   public HashMap doSetOrderPayPend(String av_constOrder, long lOrderId)  
   throws Exception {   
      try{
         return getSEJBOrderEditRemote().doSetOrderPayPend(av_constOrder, lOrderId);
      }catch(RemoteException re){
          System.out.println("EditOrderService doSetOrderPayPend \nMensaje:" + re.getMessage()+"\n");
          HashMap hshMap=new HashMap();
          hshMap.put("strMessage",re.getMessage());
          return hshMap;          
      }catch(SQLException ex){
         System.out.println("EditOrderService doSetOrderPayPend \nMensaje:" + ex.getMessage()+"\n");
         HashMap hshMap=new HashMap();
         hshMap.put("strMessage",ex.getMessage());
         return hshMap;                    
      }
      catch(Exception ex){
         System.out.println("EditOrderService doSetOrderPayPend \nMensaje:" + ex.getMessage()+"\n");
         HashMap hshMap=new HashMap();
         hshMap.put("strMessage",ex.getMessage());
         return hshMap;             
      }         
   }    
      /**
   * Motivo: 
   * <br>Realizado por: <a href="mailto:patricia.castillo@nextel.com.pe">Patricia Castillo</a>
   * <br>Fecha: 06/02/2009
   * @param     long  
   * @return    String 
   */   
   public String doSetOrderPayCancel(String av_constOrder, long lOrderId)  
   throws Exception {   
      System.out.println("Inicio-----------------------OrderEditService.doSetOrderPayCancel");
      try{
         return getSEJBOrderEditRemote().doSetOrderPayCancel(av_constOrder, lOrderId);
      }catch(RemoteException re){
         System.out.println("EditOrderService doSetOrderPayCancel \nMensaje:" + re.getMessage()+"\n");         
         String strMessage= re.getMessage();
         return strMessage;                    
      }catch(SQLException ex){
         System.out.println("EditOrderService doSetOrderPayCancel \nMensaje:" + ex.getMessage()+"\n");         
         String strMessage= ex.getMessage();
         return strMessage;           
      }
      catch(Exception ex){
         System.out.println("EditOrderService doSetOrderPayCancel \nMensaje:" + ex.getMessage()+"\n");         
         String strMessage= ex.getMessage();
         return strMessage;            
      }         
   }    
   
  /**
  * <br>Realizado por: <a href="mailto:jose.casas@nextel.com.pe">José Casas</a>
  * <br>Fecha: 04/06/2009
  * @see pe.com.nextel.Service.EditOrderService#getGuarantee(long,String)      
  */       
  public HashMap getGuarantee(long lSourceid, String strSource, long lConceptid ) throws Exception, SQLException{
    HashMap objHashMap = new HashMap();
    String  strMessage = new String();  
    try{
      return getSEJBOrderEditRemote().getGuarantee(lSourceid, strSource, lConceptid);
    } catch (SQLException e) {
      strMessage  = "[SQLException][EditOrderService][getGuarantee][" + e.getClass() + " " + e.getMessage()+"]";      
      objHashMap.put("strMessage",strMessage);
      return objHashMap;
    } catch (RemoteException e) {
      strMessage  = "[RemoteException][EditOrderService][getGuarantee][" + e.getClass() + " " + e.getMessage()+"]";      
      objHashMap.put("strMessage",strMessage);
      return objHashMap;
    } catch (Exception e) {
      strMessage  = "[Exception][EditOrderService][getGuarantee][" + e.getClass() + " " + e.getMessage()+"]";      
      objHashMap.put("strMessage",strMessage);
      return objHashMap;
    }
  } 
  
/***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:rensso.martinez@nextel.com.pe">Rensso Martinezr</a>
 * <br>Motivo: Valida que los contratos de una orden no esten suspendidos mas de 60 días.
 * <br>Fecha: 24/06/2009
 * @see pe.com.nextel.dao.OrderDAO#OrderDAOvalidaDiasSuspensionEdit(String, String, String)      
/*************************************************************************************************************************************/    
    public HashMap OrderDAOvalidaDiasSuspensionEdit(String strNporderId, String strNpScheduleDate, String strNpScheduleDate2){
       HashMap objHashMap = new HashMap();
       String  strMessage = new String();
     
        try {
            return getSEJBOrderEditRemote().OrderDAOvalidaDiasSuspensionEdit(MiUtil.parseInt(strNporderId) , strNpScheduleDate,strNpScheduleDate2);
        }catch (SQLException e) {
            strMessage  = "[SQLException][EditOrderService][OrderDAOvalidaDiasSuspensionEdit][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][EditOrderService][OrderDAOvalidaDiasSuspensionEdit][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][EditOrderService][OrderDAOvalidaDiasSuspensionEdit][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }
 /***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:cesar.barzola@nextel.com.pe">Cesar Barzola</a>
 * <br>Motivo: Valida la Propuesta
 * <br>Fecha: 27/07/2009      
/ ************************************************************************************************************************************/
  public  HashMap  getValidationProposed(long lOrderId,long lProposedId,long lCustomerId,long lSpecification,long lSellerId,String strTrama)
   {
    HashMap objHashMap = new HashMap();
    String  strMessage = new String();
     try{
          return getSEJBOrderEditRemote().getValidationProposed(lOrderId,lProposedId,lCustomerId,lSpecification,lSellerId,strTrama);
        }
        catch (SQLException e) {
            strMessage="Error [EditOrderService][getValidationProposed][" + e.getMessage() + "]["+e.getClass()+"]";
             objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
                return objHashMap;
        }catch (RemoteException e) {
           strMessage="Error [EditOrderService][getValidationProposed][" + e.getMessage() + "]["+e.getClass()+"]";
             objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
             return objHashMap;
        }
        catch (Exception e) {
           strMessage="Error [EditOrderService][getValidationProposed][" + e.getMessage() + "]["+e.getClass()+"]";
             objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
                return objHashMap;
        }
   } 
   
      
/***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:julio.herrera@nextel.com.pe">Julio Herrera</a>
 * <br>Motivo: Devuelve la cantidad de días calendario entre una fecha dada y el parametro
 * <br>Fecha: 03/11/2009      
/ ************************************************************************************************************************************/

   public int getAmountCalendarDays(String npCreateDate, int plazo){
       try {
          return getSEJBOrderEditRemote().getAmountCalendarDays(npCreateDate,plazo);
      }catch (SQLException e) {
          return 0;
      }catch (RemoteException e) {
          return 0;
      }catch(Exception e){
          return 0;
      }
  }
  
/***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:julio.herrera@nextel.com.pe">Julio Herrera</a>
 * <br>Motivo: Devuelve la fecha final de un intervalo de periodo de 5 dias
 * <br>Fecha: 03/11/2009      
/ ************************************************************************************************************************************/

   public String getFechaFinalIntervalo(String npCreateDate, int plazo){
       try {
          return getSEJBOrderEditRemote().getFechaFinalIntervalo(npCreateDate,plazo);
      }catch (SQLException e) {
          return "";
      }catch (RemoteException e) {
          return "";
      }catch(Exception e){
          return "";
      }
  }

  /***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:michael.valle@hp.com">Michael Valle</a>
 * <br>Motivo: Generar Guia de Remisión
 * <br>Fecha: 12/11/2010      
/ ************************************************************************************************************************************/
  public  HashMap  doGenerateGuiaRemision(long lngOrderId, String strLogin)
   {
    HashMap objHashMap = new HashMap();
    String  strMessage = new String();
     try{
          return getSEJBOrderEditRemote().OrderDAOdoGenerateGuiaRemision(lngOrderId, strLogin);
        }
        catch (SQLException e) {
            strMessage="Error [EditOrderService][doGenerateGuiaRemision][" + e.getMessage() + "]["+e.getClass()+"]";
             objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
                return objHashMap;
        }catch (RemoteException e) {
           strMessage="Error [EditOrderService][doGenerateGuiaRemision][" + e.getMessage() + "]["+e.getClass()+"]";
             objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
             return objHashMap;
        }
        catch (Exception e) {
           strMessage="Error [EditOrderService][doGenerateGuiaRemision][" + e.getMessage() + "]["+e.getClass()+"]";
             objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
                return objHashMap;
        }
   } 

/**
 * PHIDALGO
* <br>Fecha: 24/11/2010
* @see pe.com.nextel.dao.OrderDAO#doValidateBudget(String)      
*/   
  public String doValidateBudget(OrderBean orderBean, PortalSessionBean portalBean, ArrayList itemOrderList, HashMap objItemDeviceMap) {
     try{       
        return getSEJBOrderEditRemote().doValidateBudget(orderBean,portalBean,itemOrderList,objItemDeviceMap);    
     }catch(RemoteException re){
        logger.error("EditOrderService doValidateBudget \nMensaje:" + re.getMessage()+"\n");
        return re.getMessage();
     }catch(SQLException ex){
        logger.error("EditOrderService doValidateBudget \nMensaje:" + ex.getMessage()+"\n");
        return ex.getMessage();           
     }
     catch(Exception ex){
        logger.error("EditOrderService doValidateBudget \nMensaje:" + ex.getMessage()+"\n");
        return ex.getMessage();           
     }
  }
/**
 * PHIDALGO
* <br>Fecha: 24/11/2010
* @see pe.com.nextel.dao.OrderDAO#budgetsAvailableChannels(String)      
*/ 
  public Map budgetsAvailableChannels(String channelType, BudgetBean budgetBean){
      List budgetsList = null;
      Map budgetsMap = null;
      String  strMessage = null;
      try {
          budgetsMap = (Map)getSEJBOrderEditRemote().budgetsAvailableChannels(channelType,budgetBean);
      } catch(RemoteException re){
          budgetsMap = new HashMap();
          strMessage = "EditOrderService budgetsAvailableChannels \nMensaje:" + re.getMessage()+"\n";
          logger.error(strMessage);
          budgetsMap.put(Constante.MESSAGE_OUTPUT,strMessage);
      } catch(SQLException ex){
          budgetsMap = new HashMap();
          strMessage = "EditOrderService budgetsAvailableChannels \nMensaje:" + ex.getMessage()+"\n";
          logger.error(strMessage);
          budgetsMap.put(Constante.MESSAGE_OUTPUT,strMessage);      
      } catch(Exception ex){
          budgetsMap = new HashMap();
          strMessage = "EditOrderService budgetsAvailableChannels \nMensaje:" + ex.getMessage()+"\n";
          logger.error(strMessage);
          budgetsMap.put(Constante.MESSAGE_OUTPUT,strMessage);           
     }
      return budgetsMap;
  }

   /***********************************************************************************************************************************
   * <br>Realizado por: <a href="mailto:joseph.nino-de-guzman-diaz@hp.com">Joseph Niño de Guzmán</a>
   * <br>Motivo: Suspender Equipos
   * <br>Fecha: 30/11/2010
   / ************************************************************************************************************************************/
   public HashMap doGenerarSuspenderEquipos(long lOrderId)  
   throws Exception {   
      try{
         return getSEJBOrderEditRemote().doGenerarSuspenderEquipos(lOrderId);
      }catch(RemoteException re){
         System.out.println("EditOrderService doGenerarSuspenderEquipos \nMensaje:" + re.getMessage()+"\n");         
         HashMap shMap=new HashMap();
         shMap.put("strMessage",re.getMessage());
         return shMap;                    
      }catch(SQLException ex){
         System.out.println("EditOrderService doGenerarSuspenderEquipos \nMensaje:" + ex.getMessage()+"\n");         
         HashMap shMap=new HashMap();
         shMap.put("strMessage",ex.getMessage());
         return shMap;        
      }
      catch(Exception ex){
         System.out.println("EditOrderService doGenerarSuspenderEquipos \nMensaje:" + ex.getMessage()+"\n");         
         HashMap shMap=new HashMap();
         shMap.put("strMessage",ex.getMessage());
         return shMap;        
      }         
   }          

   /***********************************************************************************************************************************
   * <br>Realizado por: <a href="mailto:joseph.nino-de-guzman-diaz@hp.com">Joseph Niño de Guzmán</a>
   * <br>Motivo: Obtener Status de Equipos de Orden
   * <br>Fecha: 30/11/2010      
   / ************************************************************************************************************************************/   
   public HashMap doGetEquipmentStatus(long lOrderId,String strUserId)
   throws Exception {
      try{
         return getSEJBOrderEditRemote().doGetEquipmentStatus(lOrderId,strUserId);
      }catch(RemoteException re){
         System.out.println("EditOrderService doGetEquipmentStatus \nMensaje:" + re.getMessage()+"\n");
         HashMap shMap=new HashMap();
         shMap.put("strMessage",re.getMessage());
         return shMap;                    
      }catch(SQLException ex){
         System.out.println("EditOrderService doGetEquipmentStatus \nMensaje:" + ex.getMessage()+"\n");         
         HashMap shMap=new HashMap();
         shMap.put("strMessage",ex.getMessage());
         return shMap;        
      }
      catch(Exception ex){
         System.out.println("EditOrderService doGetEquipmentStatus \nMensaje:" + ex.getMessage()+"\n");         
         HashMap shMap=new HashMap();
         shMap.put("strMessage",ex.getMessage());
         return shMap;        
      }
   }
   /***********************************************************************************************************************************
   *PHIDALGO
   *<br>Motivo: Obtiene los motivos de rechazo del inbox de Presupuesto
   * <br>Fecha: 20/12/2010      
   / ************************************************************************************************************************************/   
   public Map doGetBudgetReasons(){
      Map budgetReasonMap = null;
      try{
        budgetReasonMap = getSEJBOrderEditRemote().doGetBudgetReasons();
        return budgetReasonMap;
      }catch(RemoteException re){
        budgetReasonMap=new HashMap();
        budgetReasonMap.put(Constante.MESSAGE_OUTPUT,re.getMessage());
        logger.error("EditOrderService doGetBudgetReasons \nMensaje:" + re.getMessage()+"\n");
        return budgetReasonMap;
     }catch(SQLException ex){
        budgetReasonMap=new HashMap();
        budgetReasonMap.put(Constante.MESSAGE_OUTPUT,ex.getMessage());
        logger.error("EditOrderService doGetBudgetReasons \nMensaje:" + ex.getMessage()+"\n");
        return budgetReasonMap;           
     }
     catch(Exception ex){
        budgetReasonMap=new HashMap();
        budgetReasonMap.put(Constante.MESSAGE_OUTPUT,ex.getMessage());
        logger.error("EditOrderService doGetBudgetReasons \nMensaje:" + ex.getMessage()+"\n");
        return budgetReasonMap;           
     }
     
   }
   
   /***********************************************************************************************************************************
   *PHIDALGO
   *<br>Motivo: obtiene la ultima descripción del rechazo del inbox de Presupuesto
   * <br>Fecha: 20/12/2010      
   / ************************************************************************************************************************************/   
   public Map doGetLastReasonDescription(BudgetBean budgetBean){
      Map budgetReasonMap = null;
      try{
        budgetReasonMap = getSEJBOrderEditRemote().doGetLastReasonDescription(budgetBean);
        return budgetReasonMap;
      }catch(RemoteException re){
        budgetReasonMap=new HashMap();
        budgetReasonMap.put(Constante.MESSAGE_OUTPUT,re.getMessage());
        logger.error("EditOrderService doGetLastReasonDescription \nMensaje:" + re.getMessage()+"\n");
        return budgetReasonMap;
     }catch(SQLException ex){
        budgetReasonMap=new HashMap();
        budgetReasonMap.put(Constante.MESSAGE_OUTPUT,ex.getMessage());
        logger.error("EditOrderService doGetLastReasonDescription \nMensaje:" + ex.getMessage()+"\n");
        return budgetReasonMap;           
     }
     catch(Exception ex){
        budgetReasonMap=new HashMap();
        budgetReasonMap.put(Constante.MESSAGE_OUTPUT,ex.getMessage());
        logger.error("EditOrderService doGetLastReasonDescription \nMensaje:" + ex.getMessage()+"\n");
        return budgetReasonMap;           
     }
   }
  
  
  /*************************************************************************************************************
   * <br>Realizado por: <a href="mailto:cesar.lozza@hp.com">César Lozza</a>
   * <br>Motivo: Método que obtiene la cantidad de items a los cuales se aplicó promoción por volumen de orden
   * <br>Fecha: 20/12/2010
  / ************************************************************************************************************/
  public int getOrderVolumeCount(int orderId){
    
    int orderVolumeCount = -1;
    
    try {
      orderVolumeCount = getSEJBOrderEditRemote().getOrderVolumeCount(orderId);
    }catch (SQLException e) {
      orderVolumeCount =  0;
    }catch (RemoteException e) {
      orderVolumeCount =  0;
    }catch(Exception e){
      orderVolumeCount =  0;
    }
    
    return orderVolumeCount;
  } 
    
   public int getEnabledCourier(long npOrderId){
       try {
          return getSEJBOrderEditRemote().getEnabledCourier(npOrderId);
      }catch (SQLException e) {
          return 1;
      }catch (RemoteException e) {
          return 1;
      }catch(Exception e){
          return 1;
      }
  }

   /**
     * <br>Realizado por: <a href="mailto:miguel.montoya@hp.com">Miguel Ángel Montoya</a>
     * <br>Fecha: 08/04/2013
     */
  public boolean showCourier(int npSpecificationId) {
    GeneralService generalService = new GeneralService();    
    HashMap hashMap = generalService.getDominioList("DESPACHO_COURIER");
    ArrayList arrDominioList = (ArrayList) hashMap.get("arrDominioList");
    
    for (int i = 0; i < arrDominioList.size(); i++) {
        DominioBean dominio = (DominioBean) arrDominioList.get(i);
        
        if (dominio.getValor().equals(String.valueOf(npSpecificationId))) {
            return true;        
        }                
    }
    
    return false;       
  }

    /**
     * <br>Realizado por: <a href="mailto:miguel.montoya@hp.com">Miguel Ángel Montoya</a>
     * <br>Fecha: 07/11/2013
     */
    public boolean requiresDocumentVerification(long npOrderId) {
        try {
            return getSEJBOrderEditRemote().requiresDocumentVerification(npOrderId);
        } catch (Exception e) {
            return false;
        }
    }

    //EFLORES 29/12/2015 Requerimiento REQ-0204 JBALCAZAR PRY-1055
    public String doValidateUltimaPreevaluacion(String customerid,String categoryId,String cadenaNumeros,String cadenaModalidad, String userLogin){
        try {
            return getSEJBOrderEditRemote().doValidateUltimaPreevaluacion(customerid,categoryId,cadenaNumeros,cadenaModalidad,userLogin);
        } catch (Exception e) {
            return "0|"+e.getMessage();
        }
    }
    
    /**
     * <br>PRY-0890 JCURI</a>
     * <br>Fecha: 17/08/2017
     */
    public boolean isClientPostPago(long npOrderId) throws Exception, SQLException{
    	try {
            return getSEJBOrderEditRemote().isClientPostPago(npOrderId);
        }catch (Exception e) {
        	System.out.println("[Exception][EditOrderService][isClientPostPago][" + e.getClass() + " " + e.getMessage()+"]");
            
            return false;
        }
    }
    
    /**
     * <br>PRY-0890 JCURI</a>
     * <br>Fecha: 17/08/2017
     */
    public HashMap generateOrderPAExtorno(long npOrderId, long customerId, String strSpecificationId, String strCurrentInbox, String strUser) throws Exception, SQLException{
    	HashMap resultMap = new HashMap();
    	try {
    		resultMap = getSEJBOrderEditRemote().generateOrderPAExtorno(npOrderId, customerId, strSpecificationId, strCurrentInbox, strUser);
        }catch (Exception e) {
        	System.out.println("[Exception][EditOrderService][generateOrderPAExtorno][" + e.getClass() + " - " + e.getMessage()+"]");           
        }
    	return resultMap;
    }


    /*
    Method :    loadUseAddressInOrder
    Project:    PRY-1049
    Purpose:    Carga la dirección de uso de la pre evaluación realizada al cliente en la tabla de direcciones
    de la orden
    Developer       Fecha       Comentario
    =============   ==========  ======================================================================
    Daniel Olano    22/03/2018  Creación
    */
    public HashMap loadUseAddressInOrder(long buildingId, long customerId){
        HashMap resultMap = new HashMap();
        try {
            resultMap = getSEJBOrderEditRemote().loadUseAddressInOrder(buildingId, customerId);
        } catch (Exception e) {
            /* INICIO: DOLANO-0001 | PRY-1049 */
            //System.out.println("[Exception][EditOrderService][verifyEvalLocation][" + e.getClass() + " - " + e.getMessage()+"]");
            logger.error("[Exception][EditOrderService][verifyEvalLocation][\" + e.getClass() + \" - \" + e.getMessage()+\"]", e);
            /* FIN: DOLANO-0001 | PRY-1049 */
        }
        return resultMap;

    }
	/**
    * <br>Realizado por PRY-1081 CDIAZ</a>
    * <br>Fecha: 09/04/2018
    */  
    public HashMap getEditOrderScreenField(long lOrderId,String strPage, int iSpecificationId, String strInbox, int iUserId,int iAppId){     
       try{
          return getSEJBOrderEditRemote().getEditOrderScreenField(lOrderId,strPage, iSpecificationId, strInbox, iUserId, iAppId);
       }catch(RemoteException re){
          System.out.println("EditOrderService getEditOrderScreenField \nMensaje:" + re.getMessage()+"\n");
          HashMap hshMap=new HashMap();
          hshMap.put("strMessage",re.getMessage());
          return hshMap;
       }catch(SQLException ex){
          System.out.println("EditOrderService getEditOrderScreenField \nMensaje:" + ex.getMessage()+"\n");            
          HashMap hshMap=new HashMap();
          hshMap.put("strMessage",ex.getMessage());
          return hshMap;
       }   
       catch(Exception ex){
          System.out.println("EditOrderService getEditOrderScreenField \nMensaje:" + ex.getMessage()+"\n");            
          HashMap hshMap=new HashMap();
          hshMap.put("strMessage",ex.getMessage());
          return hshMap;
       }
    }
	
	/***********************************************************************************************************************************
     * <br>Autor  : JCURI
     * <br>project: PRY-1093
     * <br>Motivo : Obtiene lista de transportistas
     * <br>Fecha  : 09/04/2018      
     / ************************************************************************************************************************************/
    public HashMap getCarrierPlaceOfficeList(int intParamDispatch, String strParamName, String strParamStatus){     
        try{
           return getSEJBOrderEditRemote().getCarrierPlaceOfficeList(intParamDispatch, strParamName, strParamStatus);
        }catch(RemoteException re){
           System.out.println("EditOrderService getCarrierPlaceOfficeList \nMensaje:" + re.getMessage()+"\n");
           HashMap hshMap=new HashMap();
           hshMap.put("strMessage",re.getMessage());
           return hshMap;
        }catch(SQLException ex){
           System.out.println("EditOrderService getCarrierPlaceOfficeList \nMensaje:" + ex.getMessage()+"\n");            
           HashMap hshMap=new HashMap();
           hshMap.put("strMessage",ex.getMessage());
           return hshMap;
        }   
        catch(Exception ex){
           System.out.println("EditOrderService getCarrierPlaceOfficeList \nMensaje:" + ex.getMessage()+"\n");            
           HashMap hshMap=new HashMap();
           hshMap.put("strMessage",ex.getMessage());
           return hshMap;
        }
     }

    /***********************************************************************************************************************************
     * <br>Autor  : JCURI
     * <br>project: PRY-1093
     * <br>Motivo : Valida orden delivery courier
     * <br>Fecha  : 01/06/2018      
     / ************************************************************************************************************************************/
    public boolean validateStoreRegion(long npOrderId) {   
    	try{
           return getSEJBOrderEditRemote().validateStoreRegion(npOrderId);           
   	 	}catch(Exception ex){
            System.out.println("EditOrderService validateStoreRegion \nMensaje:" + ex.getMessage()+"\n");            
            HashMap hshMap=new HashMap();
            hshMap.put("strMessage",ex.getMessage());
            return false;
         }
    }
}
