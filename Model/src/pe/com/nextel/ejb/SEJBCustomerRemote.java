package pe.com.nextel.ejb;

import java.rmi.RemoteException;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import java.util.Map;

import javax.ejb.EJBObject;

import pe.com.nextel.bean.AddressWriteStruct;
import pe.com.nextel.bean.BillingAccountWriteStruct;
import pe.com.nextel.bean.CustomerBean;
import pe.com.nextel.bean.MessageVO;


import wsp.customerread.proxy.be.CustomerReadBeanOut;


//import wsp.contractcreate.proxy.ContractCreateOut;
//import wsp.customerhierarchywrite.proxy.types.customerhierarchywrite.types.CustomerHierarchyWriteOut;

public interface SEJBCustomerRemote extends EJBObject {

   /***********************************************************************
    **********************************************************************
    ***********************************************************************
    *  INTEGRACION DE ORDENES Y RETAIL - INICIO
    *  REALIZADO POR: Lee Rosales
    *  FECHA: 29/09/2007
    ***********************************************************************
    ***********************************************************************
    ***********************************************************************/
   
   
   Hashtable CustomerDAOgetCompanyInfo(long codCliente) throws Exception ,SQLException , RemoteException;

   HashMap CustomerDAOgetCustomerAddress(long intObjectId, long longRegionId, String strObjectType,String strGeneratortype) throws Exception ,SQLException , RemoteException;   
   
   HashMap CustomerDAOgetAddressDelivery(long idObjectId, String strObjectType, String strGeneratorType, String strRegionId) throws Exception ,SQLException , RemoteException;
   
   int CustomerDAOgetUnknowSite(long intObjectId, String strObjectType) throws Exception ,SQLException , RemoteException;
   
   HashMap CustomerDAOgetCustomerData(long iCustomerId, String strSwcreatedBy, long iRegionid,int iUserId,int iAppId,String strGeneratorId,String strGeneratorType)  throws Exception ,SQLException , RemoteException;
   
   Hashtable CustomerDAOgetCustomerIdCrmByBSCS(long intCodigoCliente) throws Exception ,SQLException , RemoteException;
   
   Hashtable CustomerDAOgetCustPatternQty(String strNombreCompania) throws Exception ,SQLException , RemoteException;
   
   Hashtable CustomerDAOgetCustRucQty(String strRuc) throws Exception ,SQLException , RemoteException;
   
   Hashtable CustomerDAOgetSiteData(long intSiteid, String strCreatedby, long intUserid, long intAppid, long intRegionid,
                                    String strGeneratortype, String strGeneratorId) throws Exception ,SQLException , RemoteException;
   
   HashMap getCustomerSites(long intCustomerId,String strEstadoSite) throws Exception ,SQLException , RemoteException;
   
   HashMap CustomerDAOgetCustomerJava(long longAppId, String strPhoneNumber, long longContractId, String strIMEI, String strSIM,String strNumeroRadio) throws  SQLException, Exception, RemoteException;

   long getCustomerIdBSCS(long lCustomerId, String strObjectType) throws  SQLException, Exception, RemoteException;
   
	
    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - FIN
     *  REALIZADO POR: Lee Rosales
     *  FECHA: 29/09/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/ 
    

/***********************************************************************
***********************************************************************
***********************************************************************
*  INTEGRACION DE ORDENES Y RETAIL - INICIO
*  REALIZADO POR: Carmen Gremios
*  FECHA: 13/09/2007
***********************************************************************
***********************************************************************
***********************************************************************/
     
   HashMap getCustomerData(long iNpCustomerId) throws RemoteException, Exception;
   
   HashMap getCustomerData2(long iCustomerId, String strSwcreatedBy, long iRegionid) throws RemoteException, Exception;
   
   HashMap getCustomerContacts2(long intObjectId, String strObjectType)  throws RemoteException, Exception;
   
   HashMap getCustomerAddress2(long intObjectId,long longRegionId, String strObjectType)  throws RemoteException, Exception;
      
   HashMap getCustomerSitesList(long lCustomerId,long lOrderId,long lOportunityId,String strEstadoSite)  throws SQLException,RemoteException, Exception;                             
   
   String getCustomerType(long lCustomerId) throws SQLException,RemoteException, Exception;                             
   
   ArrayList getCustomerContactAll(long lCustomerId, int iResultado) throws SQLException,RemoteException, Exception;                             
   
   ArrayList getSiteContactAll(long lCustomerId,long lSiteId, int iResultado) 
   throws SQLException,RemoteException, Exception;                             
   
   HashMap getDeliveryAddress(long lObjetId,String strObjectType) 
   throws SQLException,RemoteException, Exception;                             
   
   HashMap getAddressByRegion(long lObjectId,String strObjectType,long lSellerRegId) 
   throws SQLException,RemoteException, Exception;   

   HashMap getAddress(long lObjectTypeId, String strObjectType,String strType) throws RemoteException, SQLException;
   
   HashMap getContact(long lObjectTypeId, String strObjectType,String strType)throws RemoteException, SQLException;

   HashMap getAddressChange(long lOrderId, int iTypeObject,long lObjectId)throws RemoteException, SQLException;
   
   HashMap getContactChange(long lOrderId, int iTypeObject,long lObjectId)throws RemoteException, SQLException;
   
   HashMap getHeaderChange(long lOrderId, int iTypeObject,long lObjectId)throws RemoteException, SQLException;
   
   HashMap getCustomerSearch(String strTipoDocumento, 
                                               String strNumeroDocumento,
                                               String strRazonSocial,
                                               String strNombreCliente,
                                               String strTipoCliente,
                                               long intCustomerId,
                                               long intRegionId,
                                               String strSessionCode,
                                               int intSessionLevel,
                                               int lPageSelected,
                                               int iProGrpId,
                                               String strLogin,
                                               int intSalesStructId,
                                               int iUserId1,
                                               int iAppId1)  throws Exception, RemoteException, SQLException;
											   
/***********************************************************************
***********************************************************************
***********************************************************************
*  INTEGRACION DE ORDENES Y RETAIL - FIN
*  REALIZADO POR: Carmen Gremios
*  FECHA: 13/09/2007
***********************************************************************
***********************************************************************
***********************************************************************/

	/***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES - INICIO
     *  REALIZADO POR: Richard De los Reyes
     *  FECHA: 07/11/2007
     ***********************************************************************
     ***********************************************************************
     **********************************************************************/
	MessageVO generateCustomer(HashMap hshParametrosMap) throws RemoteException, Exception;
	 
	AddressWriteStruct generateAddress(HashMap hshParametrosMap, Long lCustomerId, String strCsId) throws RemoteException, Exception;
	 
	BillingAccountWriteStruct generateBillingAccount(HashMap hshParametrosMap, Long lContactSeqNo, String strCsId) throws RemoteException, Exception;

	String synchronizeCustomerBSCS(HashMap hshCustomerMap) throws SQLException, RemoteException, Exception;
	
	String synchronizeBillingAccountBSCS(HashMap hshBillingAccountMap) throws SQLException, RemoteException, Exception;
	
	//CustomerHierarchyWriteOut joinCustomersByHierarchy(String strCsIdHight, String strCsId) throws SQLException, RemoteException, Exception;
	
	CustomerReadBeanOut customerRead(String strCsid) throws SQLException, RemoteException, Exception;
	
	//ContractCreateOut createContract(HashMap hshParametrosMap) throws SQLException, RemoteException, Exception;

  HashMap getSourceAddress(long lngCustomerId, long lngItemId) throws  SQLException, Exception, RemoteException;

  HashMap getDestinyAddress(long lngCustomerId) throws  SQLException, Exception, RemoteException;

  HashMap getCustomerContactsByType(long lngCustomerId, String strType) throws  SQLException, Exception, RemoteException;

  HashMap CustomerDAOgetInfoCustomer(long lOrderId) throws  SQLException, Exception,RemoteException ;
  
  //HashMap createCustomerBSCS(long lOrderId) throws  SQLException, Exception,RemoteException ;
  
  HashMap CustomerDAOgetValidateDniRucEquals(String strNumDoc,String strTipoDoc) throws  SQLException, Exception,RemoteException ;

  ArrayList getSitesByCustomer(long customerId) throws  Exception, RemoteException, SQLException;

  CustomerBean getCustomerInfo(long customerId) throws  Exception, RemoteException, SQLException;

  HashMap getAddressesByCustomer(long customerId) throws  Exception, RemoteException, SQLException;

  HashMap getExisteOrdenProspect(long customerId) throws  Exception, RemoteException, SQLException;

  CustomerBean getValidateCustomer(String tipoDocumento, String nroDocumento) throws  Exception, RemoteException, SQLException;

  HashMap getCreateContract(HashMap hshParametrosMap) throws  SQLException, Exception, RemoteException;

  HashMap getCustomerDataDetail(long iCustomerId, String strSwcreatedBy, long iRegionid, long lOrderId) throws RemoteException, Exception;

  HashMap getNumAddressId (String strTipoObjeto, long lCodigo, String strTipoDireccion, long lRegionId) throws RemoteException, Exception;  
  
  
  HashMap CustomerDAOgetValidateDocument(String strNumDoc,String strTipoDoc) throws  SQLException, Exception,RemoteException ;

  byte getIsSiteBA(long lngCustomerId) throws Exception, RemoteException, SQLException;
	/***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES - FIN
     *  REALIZADO POR: Richard De los Reyes
     *  FECHA: 07/11/2007
     ***********************************************************************
     ***********************************************************************
     **********************************************************************/


    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  ORDENES SSAA - INICIO
     *  REALIZADO POR: Ruth Polo
     *  FECHA: 09/01/2009
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/ 
     
     public long  CustomerDAOgetRol(long intScreenoptionid, long intUserid, long intAppid)  throws SQLException, RemoteException, Exception;

  HashMap getValidationFraudCustomer(String strDocCustomer) throws  SQLException, Exception, RemoteException;
     
    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  ORDENES SSAA - FIN 
     *  REALIZADO POR: Ruth Polo
     *  FECHA: 09/01/2009
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/ 
     
     /*Inicio Responsable de Pago*/
      public HashMap CustomerDAOgetCustomerSitesBySolution(long intCustomerId, int iSolution, int iSpecification) throws SQLException, RemoteException, Exception;
     /*Fin Responsable de Pago*/


    /**
      Method : getContactList
      Purpose: Obtener el detalle del contacto de un tipo determinado del customer o site
      Developer       		Fecha       Comentario
      =============   		==========  ======================================================================
      Evelyn Ocampo     	11/06/2009  Creación
      */  
      HashMap getContactList(long lObjectTypeId, String strObjectType, String strType, long lSiteId) throws RemoteException, Exception ;
    
      /**
      Method : getContactList
      Purpose: Obtener el detalle del contacto de un tipo determinado del customer o site
      Developer       		Fecha       Comentario
      =============   		==========  ======================================================================
      Evelyn Ocampo     	11/06/2009  Creación
      */  
      HashMap getContactChangeList(long lOrderId,int iItemid,int iObjectType,long lObjectId, String strContactType) throws  SQLException, Exception,RemoteException ;
      /**
      Method : getValidationCustomer
      Purpose: 
      Developer       		Fecha       Comentario
      =============   		==========  ======================================================================
      CBARZOLA     	      09/11/2009  Creación 
       */
       HashMap getValidationCustomer(long lUserId,long lCustomerId)throws  SQLException, Exception,RemoteException ;
       
       
       HashMap getSubRegOrder(long lOrderId) throws SQLException, Exception,RemoteException ;
       HashMap getPhoneSubReg(long lCustomerId,long lIncidentId) throws SQLException, Exception,RemoteException ;
          /**
      Method : validateUbigeo
      Purpose: 
      Developer       		Fecha       Comentario
      =============   		==========  ======================================================================
      DGUTIERREZ     	   21/07/2010  Creación 
       */

      public String validateUbigeo(String state, String province, String city) throws SQLException, RemoteException, Exception;
          
        /**
        Method : getValidateAddress
        Purpose:
        Developer                 Fecha       Comentario
        =============             ==========  ======================================================================
        FBERNALES                22/12/2015  Creación
        */  
      public Map<Double,Boolean> getValidateAddress(String sAddress,String sUbigeo, String sAplicacion )throws Exception;
    public void insLogValidateAddress(String sIdApp,Double dCorrelacion, String sCreatedBy,String sDireccion,Integer lIdCliente, String sNumDoc, Integer lNumOrder) throws Exception;
}
