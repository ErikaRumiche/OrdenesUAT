package pe.com.nextel.ejb;

import java.rmi.RemoteException;

import java.util.HashMap;

import javax.ejb.EJBObject;

import pe.com.nextel.bean.AddressObjectBean;
import pe.com.nextel.bean.PortalSessionBean;
import pe.com.nextel.util.RequestHashMap;


public interface SEJBSiteRemote extends EJBObject 
{

/***********************************************************************
***********************************************************************
***********************************************************************
*  INTEGRACION DE ORDENES Y RETAIL - FIN
*  REALIZADO POR: Carmen Gremios
*  FECHA: 27/09/2007
***********************************************************************
***********************************************************************
***********************************************************************/  
  /*ArrayList getNoChangeContacts(long lSiteId) 
  throws RemoteException , Exception;  */
  
 /* HashMap getNoChangeContacts(long lCustomerId,long lSiteId) 
  throws RemoteException , Exception;   */
    
  
  HashMap getSiteData(long iSiteid) throws RemoteException , Exception;
  
  HashMap getSiteDetail(long intCustomerId,long lSiteId) throws RemoteException , Exception;
  
  HashMap getSiteSolutionGroup(long lSiteId) throws RemoteException , Exception;

  HashMap getAddressList(String strObjectType,long lObjectId,int iResultado) throws RemoteException , Exception;
  
  HashMap getCheckMandatoryAddress(long lCustomerId,long lSiteId) 
  throws RemoteException , Exception;
  
  HashMap getSiteType(long lSiteId)
  throws RemoteException , Exception;
  
  HashMap getAddressOrContactList(String strObjectType,long lObjectId,String strSearchType) 
  throws RemoteException , Exception;  
  
  String doAddrContValidation(long lCustomerId,long lUnknwnSiteId)
  throws RemoteException , Exception; 
 
  HashMap getNoChangeAddress(long lCustomerId,long lSiteId) 
  throws RemoteException , Exception;   
  
  HashMap getCheckNoChangeContacts(long lCustomerId,long lSiteId)
  throws RemoteException , Exception;    
  
  HashMap getTypeContacts(long lCustomerId,long lSiteId) 
  throws RemoteException , Exception;    
  
  HashMap getCheckUniqueContacts(long lCustomerId,long lSiteId)
  throws RemoteException , Exception;    
  
  HashMap getCheckMandatoryContacts(long lCustomerId,long lSiteId)    
  throws RemoteException , Exception;  
  
 /* HashMap getUniqueContacts(long lCustomerId,long lSiteId) 
  throws RemoteException , Exception;  */

  HashMap getCheckUniqueAddress(long lCustomerId,long lSiteId) 
  throws RemoteException, Exception;  
  
  HashMap getTypeAddresses(long lCustomerId,long lSiteId) 
  throws RemoteException, Exception;  
  
/*  HashMap getUniqueAddresses(String strObjectId,String strObjectType)     
  throws RemoteException, Exception;   */

  HashMap validateRegionDelivAddr(AddressObjectBean objAddressBean)     
  throws RemoteException, Exception;      
  
  //long insSites(SiteBean objSiteBean, long lOrderId, String strLogin, int iAppId, String strMsgError) throws RemoteException , Exception;
  HashMap insSites(RequestHashMap request,PortalSessionBean objPortalSesBean)throws RemoteException , Exception;

  HashMap insAddress(RequestHashMap request,PortalSessionBean objPortalSesBean) throws RemoteException , Exception;
  
  HashMap updAddress(RequestHashMap request,PortalSessionBean objPortalSesBean)throws RemoteException, Exception;   

  //String delAddress(long lAddressId ,long lObjectId,String strObjectType,String strUser,long lCustomerId, long lSiteId) 
  //throws RemoteException , Exception;   
   HashMap delAddress(RequestHashMap request,PortalSessionBean objPortalSesBean)  throws RemoteException , Exception;   

 /* int updSites(SiteBean objSiteBean,String strLogin,int iAppId,String strMsgError) 
  throws RemoteException , Exception; */
  HashMap updSites(RequestHashMap request,PortalSessionBean objPortalSesBean)  throws RemoteException , Exception;    
  
  /*int delSites(long lUnkwSiteId ,String  strMsgError) 
  throws RemoteException , Exception;   */
  HashMap delSites(RequestHashMap request)  throws RemoteException , Exception; 
  HashMap delSitesAssign(RequestHashMap request)  throws RemoteException , Exception; 
  
  /*HashMap insContact(ContactObjectBean objContactBean,String strTrama) 
  throws RemoteException , Exception;  */
  HashMap insContact(RequestHashMap request,PortalSessionBean objPortalSesBean)throws RemoteException , Exception;    

  /*HashMap updContact(ContactObjectBean objContactBean,int iEdicion,String strTramaContactType,int iIndice)     
  throws RemoteException, Exception;   */  
 HashMap updContact(RequestHashMap request,PortalSessionBean objPortalSesBean)throws RemoteException, Exception;     
 
 /*HashMap delContact(ContactObjectBean objContactBean) 
  throws RemoteException , Exception;   */
  HashMap delContact(RequestHashMap request,PortalSessionBean objPortalSesBean) throws RemoteException , Exception;   
/***********************************************************************
***********************************************************************
***********************************************************************
*  INTEGRACION DE ORDENES Y RETAIL - FIN
*  REALIZADO POR: Carmen Gremios
*  FECHA: 27/09/2007
***********************************************************************
***********************************************************************
***********************************************************************/  

  /*String para traer longitud maxima permitida para el nombre del site
  throws RemoteException , Exception; */
  HashMap  getLongMaxSite()  throws RemoteException, Exception;  
 
  int getSourceSite(long lSiteId) throws RemoteException , Exception; 
  
  public HashMap getSpecSolutionGroup(long lSpecificationId) throws RemoteException, Exception;

  /*PBI000000042016*/
  public Long  getUnknownSite(long lSiteId)throws RemoteException;


}