package pe.com.portability.ejb;

import java.io.File;
import java.rmi.RemoteException;

import java.sql.SQLException;

import java.util.HashMap;

import javax.ejb.EJBObject;

import pe.com.nextel.bean.ServiciosBean;
import pe.com.nextel.portability.ws.SendMessageBE;


public interface SEJBPortabilityOrderRemote extends EJBObject 
{

  //-------------------------------BAJA
  
   HashMap getPortabilityLowByOrder(long lOrderId) throws  Exception, RemoteException, SQLException;
   
   HashMap getDominioList(String dominioTabla) throws  SQLException, Exception, RemoteException;
   
   HashMap getPortabilityLowHeader(long lOrderId) throws  Exception, RemoteException, SQLException;
   
   //HashMap getAccesories() throws SQLException, RemoteException, Exception;
   HashMap getAccesories(String modContract) throws SQLException, RemoteException, Exception;

   HashMap getMotivosByTypeObjection(String objectionTypeId) throws SQLException, Exception;
   
   HashMap getDocAtatchment() throws SQLException, RemoteException, Exception;
   
   HashMap getAtatchment_by_mo(String strAtatchment_by_mo) throws SQLException, RemoteException, Exception;
   
   HashMap getConfigFile(String configTabla) throws SQLException, RemoteException, Exception;
   
  //-------------------------------ALTA

   HashMap getPortabilityHighList(long npPortabOrderId) throws RemoteException, Exception, SQLException;
  
   HashMap getParentCheckOrder(long npPortabOrderId) throws SQLException, Exception, RemoteException;
  
   HashMap getParticipantList(int specificationId, int divisionId) throws SQLException, Exception, RemoteException;
   
   HashMap getStatusOrder(long npOrderId) throws SQLException, Exception, RemoteException;
   
   HashMap getItemsPortabList(long npOrderId) throws SQLException, Exception, RemoteException; //1
   
   HashMap getItemsPortLst(long npOrderId) throws SQLException, Exception, RemoteException; //1
  
   HashMap getPortabItemDevList1(long npItemId) throws SQLException, Exception, RemoteException; //2.1
   
   HashMap getPortabItemList1(long npItemId) throws SQLException, Exception, RemoteException; //2.2
   
   HashMap getPortabItemPortabList(long npItemId) throws SQLException, Exception, RemoteException; //2.3
     
   HashMap getPortabOrderList(long lOrderId) throws SQLException, Exception, RemoteException;
   
   HashMap wsPortabilityNumbers(String strOrderId, String strCustomerId, String strLogin, String strMessageType, String strPortabilityType) throws RemoteException, Exception, SQLException;
     
   long getValOrdenHija(long lOrderId) throws RemoteException, Exception;
   
   HashMap updateStatusPortability(long npitemdeviceid,long npitemid,String npstatus,String npModality) throws RemoteException, Exception;
   
   HashMap getPhoneNumberItem(long npitemid,long npitemdeviceid) throws RemoteException, Exception;
   
   HashMap getItemOrderPortabilityReturn(long lOrderId) throws SQLException, RemoteException, Exception;
   
   HashMap getOrderPortabilityReturn(long lOrderId) throws SQLException, RemoteException, Exception;
   
   HashMap getItemOrderPortabilityReturnHome(long lOrderId) throws SQLException, RemoteException, Exception;
   
   HashMap getOrderPortabilityReturnHome(long lOrderId) throws SQLException, RemoteException, Exception;
   
   HashMap getDetailItemPortabilityReturnHome(String strPhoneNumber) throws SQLException, RemoteException, Exception;
   
   ServiciosBean getDetailServicePortabilidad(long lngServiceId) throws SQLException, RemoteException, Exception;
   
   HashMap calculateBalance(long lOrderId) throws SQLException, RemoteException, Exception;
   
   HashMap getParentChildOrder(long lOrderId) throws SQLException, RemoteException, Exception;
   
   HashMap getInboxOrder(long strOrderId) throws Exception, SQLException, RemoteException; 
   
   HashMap checkNumberPorted(String npphoneNumber, long lOrderId) throws Exception, SQLException, RemoteException;
   
   HashMap getOrderScreenFieldPorta(long lOrderId,String strPage) throws Exception, SQLException, RemoteException;
   
   int getValidInboxContent(int lUserId, String strInboxName, int lInboxType ) throws SQLException, RemoteException, Exception;
   
   int getValInboxEditableUser(String strInbox, String strLogin) throws SQLException, RemoteException, Exception;      
   
   int getValMsgSub(long lOrderId, long npItemId) throws SQLException, RemoteException, Exception;     
   
   HashMap getDocumentList(String strNptable) throws Exception, SQLException, RemoteException;
   
   HashMap getSectionDocumentValidate(String strNptable, String strNpDescripcion, String strNpvalue) throws  Exception, RemoteException, SQLException;
   
    /**
    * Motivo: Obtiene el resultado de éxito o error del envío del SP
    * <br>Realizado por: <a href="mailto:lee.rosales@hp.com">Lee Rosales</a>
    * <br>Fecha: 29/05/2014
    * @see pe.com.portability.ejb.SEJBPortabilityOrderBean(long)      
    * @param     orderId        
    * @return    HashMap 
    */
    int getFlagSendSP(long orderId) throws SQLException, Exception;

    /**
     * Motivo: Obtiene el resultado de éxito o error del envío del PP
     * <br>Realizado por: <a href="mailto:lee.rosales@hp.com">Lee Rosales</a>
     * <br>Fecha: 29/05/2014
     * @see pe.com.portability.ejb.SEJBPortabilityOrderBean(long)
     * @param     orderId
     * @return    HashMap
     */
    int getFlagSendPP(long orderId) throws SQLException, Exception;
   
   /**
    * Motivo: Obtiene tipo de division y expresion regular para validacion de numeros a portar
    * <br>Realizado por: <a href="mailto:anthony.mateo@teamsoft.com.pe">Anthony Mateo</a>
    * <br>Fecha: 19/05/2016
    * @param     orderId
    * @return    HashMap
    */
    String getExpValidation(long orderId) throws  SQLException, Exception;

    /**
     * Motivo: Envío de subsanación de portabilidad
     * <br>Realizado por: <a href="mailto:gfarfan@soaint.com">Gabriel Farfán</a>
     * <br>Fecha: 04/09/2018
     * @param     sendMessageBE
     * @return    HashMap
     */
    HashMap wsPortabilitySubSanacion(SendMessageBE sendMessageBE) throws RemoteException,
            Exception, SQLException;

}
