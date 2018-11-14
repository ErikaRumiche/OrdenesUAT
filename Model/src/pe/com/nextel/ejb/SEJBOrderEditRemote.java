package pe.com.nextel.ejb;

import java.rmi.RemoteException;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJBObject;

import pe.com.nextel.bean.BudgetBean;
import pe.com.nextel.bean.LoadMassiveItemBean;
import pe.com.nextel.bean.OrderBean;
import pe.com.nextel.bean.PortalSessionBean;
import pe.com.nextel.bean.RejectBean;
import pe.com.nextel.util.RequestHashMap;

public interface SEJBOrderEditRemote extends EJBObject {
/***********************************************************************
***********************************************************************
***********************************************************************
*  INTEGRACION DE ORDENES Y RETAIL - INICIO
*  REALIZADO POR: Carmen Gremios
*  FECHA: 13/09/2007
***********************************************************************
***********************************************************************
***********************************************************************/

  /* int getOrderPermEdit(int iUserId, long iOrderId)
   throws RemoteException,Exception;    */
   
   int getTimeStamp(long lOrderId)
   throws RemoteException,Exception, SQLException;    
   
   String getRichExclusivity(long lCustomerId)
   throws RemoteException,Exception, SQLException;    
   
   HashMap getVendedorRegionId(int iVendedorId) 
   throws RemoteException,Exception, SQLException;    
   
   String doValidateSalesName(long lCustomerId,int iSiteId,int iSpecialtyId,String strLogin,int iVendedorId,String strVendedorName,int iUserId, int iAppId)
   throws RemoteException,Exception, SQLException;   
      
  //CPUENTE6
   String doValidateEquipmentReplacement(long lOrderId)
   throws RemoteException,Exception, SQLException;
   
   HashMap getOppOwnershipChngFlag(long lCustomerId,int iSiteId,String strAccMngmnt,int iVendedorId)
   throws RemoteException,Exception, SQLException;    
   
   String getDealer(int iVendedorId) throws RemoteException,Exception, SQLException;    
   
   HashMap getOrder(long lNpOrderid)  throws RemoteException,Exception, SQLException;        
   
   HashMap getBuildingName(long intBuildingid, String strLogin) throws RemoteException,Exception;
   
   HashMap getDispatchPlaceList(int intSpecialtyId)throws RemoteException,Exception;
   
   HashMap getOrderScreenField(long lOrderId,String strPage)throws RemoteException,Exception, SQLException;     
   
   HashMap updOrder(RequestHashMap request,PortalSessionBean objPortalSesBean) throws RemoteException,Exception, SQLException;
   
   HashMap updOrderDetail(RequestHashMap request,PortalSessionBean objPortalSesBean) throws RemoteException,Exception;
   
   HashMap getRejectList(long lNpOrderid) throws RemoteException,Exception;
   
   HashMap getBankPaymentDet(int iCodeBank,String strCodeService, String strRuc, String strOperationNumber,String strDateVoucher)
   throws RemoteException,Exception, SQLException;

/* String insReject(RejectBean objReject) throws RemoteException,Exception;

String updReject(RejectBean objRejectB) throws RemoteException,Exception;   
*/
/*  HashMap updPaymentBank(int iCodeBank,String strCodeService,String strRuc,String strNroVoucher,String strFechaPago)
throws RemoteException,Exception;   */
                                 
/*   HashMap updBankPayment(String strOperationNumber, int iCodeBank,String strDatePayment,
                     long lMontoTotal,String strCodeService)
throws RemoteException,Exception;*/

   HashMap getPaymentListBySource(String strSource,long lSourceId) throws RemoteException,Exception, SQLException;     
   
   HashMap generateDocumentInvBill(long lOrderId,String strOrigen,String strLogin,int iBuilding) throws RemoteException,Exception,SQLException;
   
   HashMap generateDocumentInvBillDetail(long lOrderId,String strOrigen,String strLogin,int iBuilding) throws RemoteException,Exception,SQLException;
   
   HashMap generatePayamentOrder(long lOrderId,String strLogin,int iBuilding) throws RemoteException,Exception,SQLException;   
   
   String updSinchronizeActiv(long lOrderId, String strLogin)  throws RemoteException,Exception,SQLException;   
   
   String insNotificationAction(long lOrderId, String strStatus, String strLogin)  throws RemoteException,Exception,SQLException;
   
   String getShowButtom(long lSpecificationId) throws RemoteException,Exception, SQLException;   

   HashMap doExecuteActionFromOrder(long lOrderId,String strStatusOld,String strStatusNew,String strLogin,long lLoginBuilding)           
   throws RemoteException,Exception,SQLException;   
   
   HashMap doGenerarParteIngreso(long lOrderId,String strOrigen,String strLogin,long lLoginBuilding, String strPIType)     
   throws RemoteException,Exception,SQLException;   

   String getAutorizacionDevolucion(long lOrderId,int iUserId,int iAppId)       
   throws RemoteException,Exception,SQLException;   
   
   float getKitEquipmentPrice(long npProduct,String npModality,long lSalesStructOrigenId)    
   throws RemoteException,Exception,SQLException;   


   HashMap doAutorizacionDevolucion(long lOrderId)     
   throws RemoteException,Exception,SQLException; 
 
   String doSetOrderPayCancel(String av_constOrder, long lOrderId)     
   throws RemoteException,Exception,SQLException; 
   
   HashMap doSetOrderPayPend(String av_constOrder, long lOrderId)     
   throws RemoteException,Exception,SQLException; 
   
   public ArrayList getInboxList(String strNpBpelbackinboxs, String strSpecification)throws RemoteException,Exception,SQLException;   
   
   HashMap getActionList(int iSpecificationId,String strAction,String strBpelbackinboxs,long lOrderId)
   throws RemoteException,Exception,SQLException;   

   HashMap getListaAccion(ArrayList objLista,int lSpecificationId,String strBPElConversationId,String strStatus)        
   throws RemoteException,Exception;   
   
   HashMap getSpecificationStatus(int iSpecificationId,String strCurrentInbox)       
   throws RemoteException,Exception;        
   
   int getFlagModifiySalesName(int iSpecificationId,int iVendedorId,int iUserId,int iAppId)     
   throws RemoteException,Exception, SQLException;        
   
   HashMap getCarrierList(String strParamName, String strParamStatus) 
   throws RemoteException,Exception,SQLException;    

   HashMap updTimeStamp(long lOrderId)   
   throws RemoteException,Exception,SQLException;    
   
   HashMap getPayFormList(int iSpecificationId,long lCustomerId)
   throws RemoteException,Exception,SQLException;
          
   HashMap getOrderDetailFlow(long lOrderId,String strLogin)           
   throws RemoteException,Exception,SQLException;         
	
	HashMap getIsFirstInbox(long lOrderId)
	throws RemoteException,Exception,SQLException;
		  
/***********************************************************************
***********************************************************************
***********************************************************************
*  INTEGRACION DE ORDENES Y RETAIL - FIN
*  REALIZADO POR: Carmen Gremios
*  FECHA: 13/09/2007
***********************************************************************
***********************************************************************
***********************************************************************/
    
   HashMap OrderPassForInventory(long lOrderId) throws Exception ,SQLException , RemoteException;
   
   public HashMap getImeiSimMatch(long lSpecificationId ,String strImei, String strSim)throws SQLException, Exception, RemoteException;
 
   public HashMap getActionItem (long lOrderId,String sOperacionItem,String estadoPagoActual)  throws SQLException, Exception, RemoteException;
   
   HashMap validateMassiveImeiSim (LoadMassiveItemBean loadMassiveItemBean) throws SQLException, RemoteException, Exception;

  HashMap getGuarantee(long lSourceid, String strSource, long lConceptid) throws SQLException, RemoteException, Exception;    
  
   /***********************************************************************************************************************************
  * <br>Realizado por: <a href="mailto:rensso.martinez@nextel.com.pe">Rensso Martinezr</a>
  * <br>Motivo: Valida que los contratos de una orden no esten suspendidos mas de 60 días.
  * <br>Fecha: 24/06/2009
  * @see pe.com.nextel.dao.OrderDAO#validaDiasSuspensionEdit(String, String, String)      
  ************************************************************************************************************************************/      
   HashMap OrderDAOvalidaDiasSuspensionEdit(int iNporderId,String strNpScheduleDate, String strNpScheduleDate2) throws  Exception, RemoteException,SQLException;
/***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:cesar.barzola@nextel.com.pe">Cesar Barzola</a>
 * <br>Fecha: 06/08/2009
 * @see pe.com.nextel.dao.ProposedDAO#getValidationProposed(String,String)      
/ ************************************************************************************************************************************/
 HashMap   getValidationProposed(long lOrderId, long lProposedId,long lCustomerId,long lSpecification,long lSellerId,String strTrama) throws SQLException,RemoteException,Exception ;
 
/***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:julio.herrera@nextel.com.pe">Julio Herrera</a>
 * <br>Motivo: Devuelve la cantidad de días calendario entre una fecha dada y el parametro
 * <br>Fecha: 03/11/2009      
/ ************************************************************************************************************************************/
 
 int getAmountCalendarDays(String npCreateDate, int plazo)
 throws RemoteException,Exception,SQLException;

/***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:julio.herrera@nextel.com.pe">Julio Herrera</a>
 * <br>Motivo: Devuelve la fecha final de un intervalo de periodo de 5 dias
 * <br>Fecha: 03/11/2009      
/ ************************************************************************************************************************************/
 
 String getFechaFinalIntervalo(String npCreateDate, int plazo)
 throws RemoteException,Exception,SQLException; 

  int getOrderVolumeCount(int orderId) throws RemoteException, Exception, SQLException;

 HashMap ItemDAOdoValidateIMEICustomer(String strIMEI) throws RemoteException, Exception, SQLException;

 /***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:michael.valle@hp.com">Michael Valle</a>
 * <br>Motivo: Genera Guia de Remision
 * <br>Fecha: 17/11/2010
/ ************************************************************************************************************************************/
 
 HashMap OrderDAOdoGenerateGuiaRemision(long lngOrderId, String strLogin) throws RemoteException, Exception, SQLException;


 /***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:joseph.nino-de-guzman-diaz@hp.com">Joseph Niño de Guzmán</a>
 * <br>Motivo: Suspender Equipos
 * <br>Fecha: 24/11/2010
/ ************************************************************************************************************************************/
 
  HashMap doGenerarSuspenderEquipos(long lOrderId)     
  throws RemoteException,Exception,SQLException;   


 /***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:joseph.nino-de-guzman-diaz@hp.com">Joseph Niño de Guzmán</a>
 * <br>Motivo: Validar Equipos
 * <br>Fecha: 24/11/2010
/ ************************************************************************************************************************************/
 
  HashMap doGetEquipmentStatus(long lOrderId,String strUserId)
  throws RemoteException,Exception,SQLException;   

  
 public String doValidateBudget(OrderBean orderBean, PortalSessionBean portalBean, ArrayList itemOrderList, HashMap objItemDeviceMap) throws RemoteException,Exception, SQLException;
 
 public Map budgetsAvailableChannels(String channelType, BudgetBean budgetBean) throws RemoteException, Exception, SQLException;
 
 public Map doGetBudgetReasons() throws RemoteException, Exception, SQLException;
 
 public Map doGetLastReasonDescription(BudgetBean budgetBean) throws RemoteException, Exception, SQLException;
 
 public int getEnabledCourier(long npOrderId) throws RemoteException, Exception, SQLException;
 
 public boolean requiresDocumentVerification(long npOrderId) throws Exception, SQLException; // [PPNN] MMONTOYA
 
 public String doValidateUltimaPreevaluacion(String customerid,String categoryId,String cadenaNumeros,String cadenaModalidad, String userLogin) throws Exception, SQLException; //EFLORES 29/12/2015 // JBALCAZAR PRY-1055
 
 public boolean isClientPostPago(long npOrderId) throws Exception, SQLException;
 
 public HashMap generateOrderPAExtorno(long orderId, long customerId, String specificationId, String strCurrentInbox, String user) throws Exception, SQLException; //PRY-0890 JCURI

 public HashMap loadUseAddressInOrder(long buildingId, long cusotomerId) throws Exception, SQLException; // PRY-1049 DOLANO
 
 public HashMap getEditOrderScreenField(long lOrderId,String strPage, int iSpecificationId, String strInbox, int iUserId,int iAppId)throws RemoteException,Exception, SQLException; //PRY-1081
 
 /***********************************************************************************************************************************
  * <br>Realizado por: PRY-1093 JCURI</a>
  * <br>Motivo: Obtiene lista de transportistas
  * <br>Fecha: 09/04/2018
 / ************************************************************************************************************************************/
 public HashMap getCarrierPlaceOfficeList(int intParamDispatch, String strParamName, String strParamStatus) throws Exception, SQLException;
 
 /***********************************************************************************************************************************
  * <br>Realizado por: PRY-1093 JCURI</a>
  * <br>Motivo: Valida si la orden es delivery region
  * <br>Fecha: 01/06/2018
 / ************************************************************************************************************************************/
 public boolean validateStoreRegion(long npOrderId) throws Exception;
}
