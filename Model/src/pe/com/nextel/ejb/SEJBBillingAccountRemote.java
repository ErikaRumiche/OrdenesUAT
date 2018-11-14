package pe.com.nextel.ejb;

import java.rmi.RemoteException;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.HashMap;

import javax.ejb.EJBObject;

import pe.com.nextel.bean.BaAssignmentBean;
import pe.com.nextel.bean.PortalSessionBean;
import pe.com.nextel.util.RequestHashMap;


public interface SEJBBillingAccountRemote extends EJBObject {    
                                                        
/***********************************************************************
***********************************************************************
***********************************************************************
*  INTEGRACION DE ORDENES - INICIO
*  REALIZADO POR: Carmen Gremios
*  FECHA: 27/10/2007
***********************************************************************
***********************************************************************
***********************************************************************/   
   HashMap getAccountList(String strObjectType,long lObjectId,long lOrderId)
   throws SQLException,RemoteException, Exception;
                                                             
   HashMap getContactBillCreateList(long lNpcustomerid,long lNpSiteId)
   throws SQLException,RemoteException,Exception ;                                                                  
   
   HashMap getNewContactBilling(long lNpbillaccnewid)  
   throws RemoteException, Exception;                                                                      
   
   long getNewBillAccId() throws RemoteException, Exception;  
   
   HashMap insBillAccount(RequestHashMap request,PortalSessionBean objPortalSesBean) throws RemoteException, Exception;
   
   HashMap updBillAccount(RequestHashMap request,PortalSessionBean objPortalSesBean) throws RemoteException, Exception;
   
   HashMap delBillAccount(RequestHashMap request,PortalSessionBean objPortalSesBean) throws RemoteException, Exception;
   
   HashMap delBillAccountAssign(RequestHashMap request,PortalSessionBean objPortalSesBean) throws RemoteException, Exception;

/***********************************************************************
***********************************************************************
***********************************************************************
*  INTEGRACION DE ORDENES - FIN
*  REALIZADO POR: Carmen Gremios
*  FECHA: 27/10/2007
***********************************************************************
***********************************************************************
***********************************************************************/    


  /***********************************************************************
  ***********************************************************************
  ***********************************************************************
  *  INTEGRACION DE ORDENES - INICIO
  *  REALIZADO POR: Lee Rosales Crispin
  *  FECHA: 29/10/2007
  ***********************************************************************
  ***********************************************************************
  ***********************************************************************/   

  HashMap  BillingAccountDAOgetBillingAccountList(long iNpcustomerid) throws Exception,RemoteException, SQLException;

  String BillingAccountDAOinsertarAssignementAccount(BaAssignmentBean baAssignmentBean,Connection conn) throws  Exception, RemoteException, SQLException;

  HashMap BillingAccountDAOgetAccountList(long longSiteId, long longCustomerId, long longOrderId) throws  SQLException, Exception, RemoteException;

  HashMap BillingAccountDAOgetCoAssignmentSiteOrig(String strPhone) throws  SQLException, Exception, RemoteException;
  
  HashMap BillingAccountDAOgetCoAssignmentList(long longOrderId)  throws Exception, SQLException, RemoteException;

  HashMap  BillingAccountDAOgetCoAssignmentBillingByContract(long longContractId) throws  SQLException, Exception, RemoteException;
  
  /***********************************************************************
  ***********************************************************************
  ***********************************************************************
  *  INTEGRACION DE ORDENES - FIN
  *  REALIZADO POR: Lee Rosales Crispin
  *  FECHA: 29/10/2007
  ***********************************************************************
  ***********************************************************************
  ***********************************************************************/   
  
  /*
  *  REALIZADO POR: Karen Salvador
  *  FECHA: 09/12/2007
  */
   HashMap  BillingAccountDAOgetBillingAccountListNew(long iNpcustomerid,long iNpsiteid) throws Exception,RemoteException, SQLException;

}
