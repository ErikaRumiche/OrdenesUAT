package pe.com.portability.ejb;

import java.io.File;
import java.sql.SQLException;

import java.util.HashMap;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import pe.com.nextel.bean.ServiciosBean;
import pe.com.nextel.portability.ws.SendMessageBE;
import pe.com.portability.bean.PortabilityOrderBean;
import pe.com.portability.dao.PortabilityGeneralDAO;
import pe.com.portability.dao.PortabilityItemDAO;
import pe.com.portability.dao.PortabilityOrderDAO;
import pe.com.portability.util.PortabilityUtil;


public class SEJBPortabilityOrderBean implements SessionBean 
{
  PortabilityOrderDAO objPortabilityDAO = null;
  PortabilityItemDAO objPortabilityItemDAO = null;
  PortabilityGeneralDAO objPortabilityGeneralDAO = null;
  public void ejbCreate()
  {
    objPortabilityDAO	= new PortabilityOrderDAO();
    objPortabilityItemDAO = new PortabilityItemDAO();
    objPortabilityGeneralDAO = new PortabilityGeneralDAO();
  }

  public void ejbActivate()
  {
  }

  public void ejbPassivate()
  {
  }




  public void ejbRemove()
  {
  }

  public void setSessionContext(SessionContext ctx)
  {
  }
  
  //-----------------------------------BAJA
  
  public HashMap getPortabilityLowByOrder(long lOrderId) throws  Exception, SQLException{

      return objPortabilityDAO.getPortabilityLowByOrder(lOrderId);
   }
  
   public HashMap getDominioList(String dominioTabla) throws  SQLException, Exception{
      return objPortabilityDAO.getDominioList(dominioTabla);
   } 
      
   public HashMap getPortabilityLowHeader(long lOrderId) throws  Exception, SQLException{
      return objPortabilityDAO.getPortabilityLowHeader(lOrderId);
   }   
   
   /*public HashMap getAccesories() throws SQLException, Exception {
		return objPortabilityDAO.getMotivos();
	 }*/
  
  public HashMap getAccesories(String modContract) throws SQLException, Exception {
		return objPortabilityDAO.getMotivos(modContract);
	}

    public HashMap getMotivosByTypeObjection(String objectionTypeId) throws SQLException, Exception {
        return objPortabilityDAO.getMotivosByTypeObjection(objectionTypeId);
    }
   
   public HashMap getDocAtatchment() throws SQLException, Exception {
		return objPortabilityDAO.getDocAtatchment();
	}
  
   public HashMap getAtatchment_by_mo(String strAtatchment_by_mo) throws SQLException, Exception {
      return objPortabilityDAO.getAtatchment_by_mo(strAtatchment_by_mo); 
   }
   
   public HashMap getConfigFile(String configTabla) throws SQLException, Exception{
     return objPortabilityDAO.getConfigFile(configTabla);
   }
  
  //----------------------------------ALTA
  
  public HashMap getPortabilityHighList(long npPortabOrderId) throws Exception, SQLException {
    return null;// objPortabilityDAO.getPortabilityHighList(npPortabOrderId);
  }
  
  public HashMap getParentCheckOrder(long npPortabOrderId) throws  SQLException, Exception{
    return objPortabilityDAO.getParentCheckOrder(npPortabOrderId);
  }
  
  public HashMap getParticipantList(int specificationId, int divisionId) throws  SQLException, Exception{
    return objPortabilityDAO.getParticipantList(specificationId, divisionId);
  }
  
  public HashMap getStatusOrder(long npOrderId) throws  SQLException, Exception{  //status
    return objPortabilityDAO.getStatusOrder(npOrderId);
  }
  
  public HashMap getItemsPortabList(long npOrderId) throws  SQLException, Exception{  //1
    return objPortabilityDAO.getItemsPortabList(npOrderId);
  }
  
  public HashMap getItemsPortLst(long npOrderId) throws  SQLException, Exception{  //1
    return objPortabilityDAO.getItemsPortLst(npOrderId);
  }
  
  public HashMap getPortabItemDevList1(long npItemId)throws SQLException, Exception{  //2.1
    return objPortabilityDAO.getPortabItemDevList1(npItemId);
  }
  
  public HashMap getPortabItemList1(long npItemId)throws SQLException, Exception{  //2.2
    return objPortabilityDAO.getPortabItemList1(npItemId);
  }
  
  public HashMap getPortabItemPortabList(long npItemId)throws SQLException, Exception{  //2.3
    return objPortabilityDAO.getPortabItemPortabList(npItemId);
  }
  
  public HashMap getPortabOrderList(long lOrderId)throws SQLException, Exception{
    return objPortabilityDAO.getPortabOrderList(lOrderId);
  }
  
  
   public HashMap wsPortabilityNumbers(String strOrderId, String strCustomerId, String strLogin, String strMessageType, String strPortabilityType) throws Exception, SQLException
  {
    PortabilityUtil pu = new PortabilityUtil();
    return pu.wsPortabilityNumbers(strOrderId,strCustomerId,strLogin,strMessageType,strPortabilityType);
  }
  
  public long getValOrdenHija(long lOrderId) throws Exception {
      return objPortabilityDAO.getValOrdenHija(lOrderId);
  } 
  
  public HashMap updateStatusPortability(long npitemdeviceid,long npitemid,String npstatus,String npModality) throws Exception {
      return objPortabilityDAO.updateStatusPortability(npitemdeviceid,npitemid,npstatus,npModality);
  }
  
  public HashMap getPhoneNumberItem(long npitemid,long npitemdeviceid) throws Exception {
      return objPortabilityDAO.getPhoneNumberItem(npitemid,npitemdeviceid);
  }
  
  /***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
 * <br>Fecha: 28/08/2009
 * @see pe.com.nextel.dao.PortabilityItemDAO#getItemOrderPortabilityReturn(long)      
 ************************************************************************************************************************************/ 
  public HashMap getItemOrderPortabilityReturn(long lOrderId) throws SQLException, Exception {
      return objPortabilityItemDAO.getItemOrderPortabilityReturn(lOrderId); 
  }
  
 /***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
 * <br>Fecha: 28/08/2009
 * @see pe.com.nextel.dao.PortabilityOrderDAO#getOrderPortabilityReturn(long)      
 ************************************************************************************************************************************/
   public HashMap getOrderPortabilityReturn(long lOrderId) throws SQLException, Exception {
      return objPortabilityDAO.getOrderPortabilityReturn(lOrderId); 
  }
  
  /***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
 * <br>Fecha: 28/08/2009
 * @see pe.com.nextel.dao.PortabilityOrderDAO#getItemOrderPortabilityReturnHome(long)      
 ************************************************************************************************************************************/
  public HashMap getItemOrderPortabilityReturnHome(long lOrderId) throws SQLException, Exception {
      return objPortabilityItemDAO.getItemOrderPortabilityReturnHome(lOrderId); 
  }
  
 /***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
 * <br>Fecha: 03/09/2009
 * @see pe.com.nextel.dao.PortabilityOrderDAO#getOrderPortabilityReturnHome(long)      
 ************************************************************************************************************************************/
  public HashMap getOrderPortabilityReturnHome(long lOrderId) throws SQLException, Exception {
      return objPortabilityDAO.getOrderPortabilityReturnHome(lOrderId); 
  }
  
 /***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
 * <br>Fecha: 04/09/2009
 * @see pe.com.nextel.dao.PortabilityItemDAO#getDetailItemPortabilityReturnHome(long)      
 ************************************************************************************************************************************/
  public HashMap getDetailItemPortabilityReturnHome(String strPhoneNumber) throws SQLException, Exception {
      return objPortabilityItemDAO.getDetailItemPortabilityReturnHome(strPhoneNumber); 
  }
  
 /***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
 * <br>Fecha: 04/09/2009
 * @see pe.com.nextel.dao.PortabilityItemDAO#getDetailServicePortabilidad(long)      
 ************************************************************************************************************************************/
  public ServiciosBean getDetailServicePortabilidad(long lngServiceId) throws SQLException, Exception {
      return objPortabilityItemDAO.getDetailServicePortabilidad(lngServiceId); 
  }
  
  
  public HashMap calculateBalance(long lOrderId) throws SQLException, Exception {
    //Verificamos si la orden ingresada tiene un orden padre.
    HashMap hshMap = objPortabilityDAO.getParentOrder(lOrderId);
    PortabilityOrderBean objPortabOBean = new PortabilityOrderBean();
    objPortabOBean = (PortabilityOrderBean)hshMap.get("objPOBean");
    
    if(objPortabOBean.getNpParentOrderId() == 0){
      //Obtenemos la id de la orden hija.
      HashMap hshMap2 = objPortabilityDAO.getChildOrder(lOrderId);
      PortabilityOrderBean objPortabOBean2 = new PortabilityOrderBean();
      objPortabOBean2 = (PortabilityOrderBean)hshMap2.get("objPOBean2");
      if(objPortabOBean2 != null){
        return objPortabilityDAO.calculateBalanceTotal(objPortabOBean.getNpOrderid(),objPortabOBean2.getNpOrderid());
      }else{
        return objPortabilityDAO.calculateBalanceTotal(objPortabOBean.getNpOrderid(),0);
      }
    }else{
      return objPortabilityDAO.calculateBalanceTotal(objPortabOBean.getNpParentOrderId(),objPortabOBean.getNpOrderid());
    }

  } 
  
  /***********************************************************************************************************************************
 * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
 * <br>Fecha: 04/09/2009
 * @see pe.com.nextel.dao.PortabilityOrderDAO#getParentChildOrder(long)      
 ************************************************************************************************************************************/
  public HashMap getParentChildOrder(long npOrderId) throws SQLException, Exception {
      return objPortabilityDAO.getParentChildOrder(npOrderId);
  }
  
  public HashMap getInboxOrder(long strOrderId) throws Exception, SQLException{
    HashMap hshDataMap = objPortabilityDAO.getInboxOrder(strOrderId);
    return hshDataMap;
  }
  
  public HashMap checkNumberPorted(String npphoneNumber, long lOrderId) throws Exception, SQLException{
    HashMap hshDataMap = objPortabilityDAO.checkNumberPorted(npphoneNumber,lOrderId);
    return hshDataMap;
  }
  
/***********************************************************************************************************
 * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
 * <br>Fecha: 16/11/2009
 * @see pe.com.nextel.dao.PortabilityOrderDAO#getOrderScreenFieldPorta(long,String)      
 ***************************************************************************************************************/
   public HashMap getOrderScreenFieldPorta(long lOrderId,String strPage) throws Exception, SQLException {
      return objPortabilityDAO.getOrderScreenFieldPorta(lOrderId,strPage);
   }    
   
   public int getValidInboxContent(int lUserId, String strInboxName, int lInboxType ) throws SQLException, Exception{
    return objPortabilityDAO.getValidInboxContent(lUserId,strInboxName,lInboxType);
  }
  
  
   public int getValInboxEditableUser(String strInbox, String strLogin) throws SQLException, Exception{
    return objPortabilityDAO.getValInboxEditableUser(strInbox,strLogin);
  }
  
    
  public int getValMsgSub(long lOrderId, long npItemId) throws SQLException, Exception{
    return objPortabilityDAO.getValMsgSub(lOrderId,npItemId);
  }
  
 /***********************************************************************************************************
 * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
 * <br>Fecha: 28/11/2010
 * @see pe.com.nextel.dao.PortabilityOrderDAO#getDocumentList(String)      
 ***************************************************************************************************************/
 public HashMap getDocumentList(String strNptable) throws Exception, SQLException {
     return objPortabilityDAO.getDocumentList(strNptable);
 } 
 
 
 public HashMap getSectionDocumentValidate(String strNptable, String strNpDescripcion, String strNpvalue) throws  Exception, SQLException{
    return objPortabilityGeneralDAO.getSectionDocumentValidate(strNptable,strNpDescripcion,strNpvalue);
   }

    /**
    * Motivo: Obtiene el resultado de éxito o error del envío del SP
    * <br>Realizado por: <a href="mailto:lee.rosales@hp.com">Lee Rosales</a>
    * <br>Fecha: 29/05/2014
    * @see pe.com.portability.ejb.SEJBPortabilityOrderBean(long)      
    * @param     orderId        
    * @return    HashMap 
    */
    public int getFlagSendSP(long orderId) throws SQLException, Exception {
        return objPortabilityDAO.getFlagSendSP(orderId);
    }

    /**
     * Motivo: Obtiene el resultado de éxito o error del envío del PP
     * <br>Realizado por: <a href="mailto:lee.rosales@hp.com">Lee Rosales</a>
     * <br>Fecha: 29/05/2014
     * @see pe.com.portability.ejb.SEJBPortabilityOrderBean(long)
     * @param     orderId
     * @return    HashMap
     */
    public int getFlagSendPP(long orderId) throws SQLException, Exception {
        return objPortabilityDAO.getFlagSendPP(orderId);
    }
    
    /**
     * Motivo: Obtiene tipo de division y expresion regular para validacion de numeros a portar
     * <br>Realizado por: <a href="mailto:anthony.mateo@teamsoft.com.pe">Anthony Mateo</a>
     * <br>Fecha: 19/05/2016
     * @param     orderId
     * @return    HashMap
     */
    public String getExpValidation(long orderId) throws  SQLException, Exception{
        return objPortabilityDAO.getExpValidation(orderId);
    }

    /**
     * Motivo: Envío de subsanación de portabilidad
     * <br>Realizado por: <a href="mailto:gfarfan@soaint.com">Gabriel Farfán</a>
     * <br>Fecha: 04/09/2018
     * @param     sendMessageBE
     * @return    HashMap
     */
    public HashMap wsPortabilitySubSanacion(SendMessageBE sendMessageBE) throws Exception, SQLException
    {
        PortabilityUtil pu = new PortabilityUtil();
        return pu.wsPortabilitySubSanacion(sendMessageBE);
    }
}

