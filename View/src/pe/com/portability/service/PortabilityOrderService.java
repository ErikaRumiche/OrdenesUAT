package pe.com.portability.service;

import java.io.File;
import java.rmi.RemoteException;

import java.sql.SQLException;

import java.util.HashMap;

import javax.naming.Context;

import javax.rmi.PortableRemoteObject;

import pe.com.nextel.bean.ServiciosBean;
import pe.com.nextel.portability.ws.SendMessageBE;
import pe.com.nextel.service.GenericService;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;
import pe.com.portability.bean.PortabilityOrderBean;
import pe.com.portability.ejb.SEJBPortabilityOrderRemote;
import pe.com.portability.ejb.SEJBPortabilityOrderRemoteHome;


public class PortabilityOrderService extends GenericService 
{
  private static String newLine = "<br>";
	private static String tabSpace = "&nbsp;&nbsp;&nbsp;&nbsp;";
  public PortabilityOrderService()
  {
  }
  
  public static SEJBPortabilityOrderRemote getSEJBPortabilityOrderRemote() {
		try {
			final Context context = MiUtil.getInitialContext();


         
      final SEJBPortabilityOrderRemoteHome sEJBPortabilityOrderRemoteHome =          
      (SEJBPortabilityOrderRemoteHome) PortableRemoteObject.narrow( context.lookup("SEJBPortabilityOrder"), SEJBPortabilityOrderRemoteHome.class );

      SEJBPortabilityOrderRemote sEJBPortabilityOrderRemote;
      sEJBPortabilityOrderRemote = sEJBPortabilityOrderRemoteHome.create();

			return sEJBPortabilityOrderRemote;
		}
		catch(Exception e) {
			logger.error(formatException(e));
      System.out.println("Exception : "+ e.getMessage()+"]");
      return null;
		}
	}
  
  
  //------------------------------------BAJA
  
  public HashMap getPortabilityLowByOrder(long lOrderId) {
      HashMap hshData = new HashMap();            
      try {
       return getSEJBPortabilityOrderRemote().getPortabilityLowByOrder(lOrderId);       
      }
		catch(SQLException sqle) {
			logger.error(formatException(sqle));
            hshData.put(Constante.MESSAGE_OUTPUT, formatException(sqle));
        }
		catch(RemoteException re) {
			logger.error(formatException(re));
            hshData.put(Constante.MESSAGE_OUTPUT, formatException(re));
        }
		catch(Exception e) {
			logger.error(formatException(e));
			hshData.put(Constante.MESSAGE_OUTPUT, formatException(e));
		}
		return hshData;
	}

   
  
   public HashMap getDominioList(String dominioTabla) {
      HashMap hshDataMap = new HashMap();
      try {        
         hshDataMap = getSEJBPortabilityOrderRemote().getDominioList(dominioTabla);
      }catch(Throwable t){
         manageCatch(hshDataMap, t);
      }
      return hshDataMap;
   }        
    
   
   public HashMap getPortabilityLowHeader(long lOrderId) {
      HashMap hshData = new HashMap();            
      try {
       return getSEJBPortabilityOrderRemote().getPortabilityLowHeader(lOrderId);       
      }
		catch(SQLException sqle) {
			logger.error(formatException(sqle));
            hshData.put(Constante.MESSAGE_OUTPUT, formatException(sqle));
        }
		catch(RemoteException re) {
			logger.error(formatException(re));
            hshData.put(Constante.MESSAGE_OUTPUT, formatException(re));
        }
		catch(Exception e) {
			logger.error(formatException(e));
			hshData.put(Constante.MESSAGE_OUTPUT, formatException(e));
		}
		return hshData;
	}
   
      
	public HashMap getMotivos(String modContract) {
		HashMap hshDataMap = new HashMap();
		try {			
         hshDataMap = getSEJBPortabilityOrderRemote().getAccesories(modContract);
        } catch(Throwable t){
			manageCatch(hshDataMap, t);
		}
		return hshDataMap;
	}

    public HashMap getMotivosByTypeObjection(String objectionTypeId) {
        HashMap hshDataMap = new HashMap();
        try {
            hshDataMap = getSEJBPortabilityOrderRemote().getMotivosByTypeObjection(objectionTypeId);
        } catch(Throwable t){
            manageCatch(hshDataMap, t);
        }
        return hshDataMap;
    }
              
         
   public HashMap getDocAtatchment() {
		HashMap hshDataMap = new HashMap();
		try {			
         hshDataMap = getSEJBPortabilityOrderRemote().getDocAtatchment();
        } catch(Throwable t){
			manageCatch(hshDataMap, t);
		}
		return hshDataMap;
	}
  
  
   public HashMap getAtatchment_by_mo(String strAtatchment_by_mo) {
		HashMap hshDataMap = new HashMap();
		try {			
         hshDataMap = getSEJBPortabilityOrderRemote().getAtatchment_by_mo(strAtatchment_by_mo);
        } catch(Throwable t){
			manageCatch(hshDataMap, t);
		}
		return hshDataMap;
	}  
  
  public HashMap getConfigFile(String configTabla){
    HashMap hshDataMap = new HashMap();
    try {
      hshDataMap = (HashMap)getSEJBPortabilityOrderRemote().getConfigFile(configTabla);
    }catch(Throwable t){
			manageCatch(hshDataMap, t);
		}
    return hshDataMap;
  }
    
  //--------------------------------------ALTA
  
  public HashMap getPortabilityOrderHighList(long npPortabOrderId) {
    HashMap hshDataMap = new HashMap();
              
    try {
       hshDataMap = (HashMap)getSEJBPortabilityOrderRemote().getPortabilityHighList(npPortabOrderId);       
      }
		catch(SQLException sqle) {
			logger.error(formatException(sqle));
            hshDataMap.put(Constante.MESSAGE_OUTPUT, formatException(sqle));
        }
		catch(RemoteException re) {
			logger.error(formatException(re));
            hshDataMap.put(Constante.MESSAGE_OUTPUT, formatException(re));
        }
		catch(Exception e) {
			logger.error(formatException(e));
			hshDataMap.put(Constante.MESSAGE_OUTPUT, formatException(e));
		}
		return hshDataMap;
	}

  public HashMap getParentCheckOrder(long npPortabOrderId) {
    HashMap hshDataMap = new HashMap();
    try {        
       hshDataMap = getSEJBPortabilityOrderRemote().getParentCheckOrder(npPortabOrderId);
    }catch(Throwable t){
       manageCatch(hshDataMap, t);
    }
    return hshDataMap;
  }
  
  public HashMap getParticipantList(int specificationId, int divisionId){
    HashMap hshDataMap = new HashMap();
    try {        
       hshDataMap = getSEJBPortabilityOrderRemote().getParticipantList(specificationId, divisionId);
    }catch(Throwable t){
       manageCatch(hshDataMap, t);
    }
    return hshDataMap;
  }
  
  public HashMap getStatusOrder(long npOrderId){  //status
    HashMap hshDataMap = new HashMap();
    try {        
       hshDataMap = getSEJBPortabilityOrderRemote().getStatusOrder(npOrderId);
    }catch(Throwable t){
       manageCatch(hshDataMap, t);
    }
    return hshDataMap;
  }
  
  public HashMap getItemsPortabList(long npOrderId){  //1
    HashMap hshDataMap = new HashMap();
    try {        
       hshDataMap = getSEJBPortabilityOrderRemote().getItemsPortabList(npOrderId);
    }catch(Throwable t){
       manageCatch(hshDataMap, t);
    }
    return hshDataMap;
  }
  
  public HashMap getPortabItemDevList1(long npItemid){  //2.1
    HashMap hshDataMap = new HashMap();
    try {        
       hshDataMap = getSEJBPortabilityOrderRemote().getPortabItemDevList1(npItemid);
    }catch(Throwable t){
       manageCatch(hshDataMap, t);
    }
    return hshDataMap;
  }
  
  public HashMap getPortabItemList1(long npItemid){  //2.2
    HashMap hshDataMap = new HashMap();
    try {        
       hshDataMap = getSEJBPortabilityOrderRemote().getPortabItemList1(npItemid);
    }catch(Throwable t){
       manageCatch(hshDataMap, t);
    }
    return hshDataMap;
  }
  
  public HashMap getPortabItemPortabList(long npItemid){  //2.3
    HashMap hshDataMap = new HashMap();
    try {        
       hshDataMap = getSEJBPortabilityOrderRemote().getPortabItemPortabList(npItemid);
    }catch(Throwable t){
       manageCatch(hshDataMap, t);
    }
    return hshDataMap;
  }
  
  public HashMap getPortabOrderList(long lOrderId){
    HashMap hshDataMap = new HashMap();
    try {        
       hshDataMap = getSEJBPortabilityOrderRemote().getPortabOrderList(lOrderId);
    }catch(Throwable t){
       manageCatch(hshDataMap, t);
    }
    return hshDataMap;
  }
  
  
  public HashMap wsPortabilityNumbers(String strOrderId, String strCustomerId, String strLogin, String strMessageType, String strPortabilityType) {
    HashMap hshDataMap = new HashMap();
    try {			
         hshDataMap = getSEJBPortabilityOrderRemote().wsPortabilityNumbers(strOrderId,strCustomerId,strLogin,strMessageType,strPortabilityType);
        } catch(Throwable t){
      manageCatch(hshDataMap, t);
    }
    return hshDataMap;
  }


  public HashMap getValOrdenHija(long lOrderId) {          
    HashMap hshDataMap = new HashMap();
      try {
        long lngValor = getSEJBPortabilityOrderRemote().getValOrdenHija(lOrderId); 
        hshDataMap.put("lngValor",String.valueOf(lngValor));
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
    return hshDataMap; 
  } 
  
  public HashMap updateStatusPortability(long npItemDeviceId,long npItemid,String npStatus, String npModality){
    HashMap hshDataMap = new HashMap();
    try {        
       hshDataMap = getSEJBPortabilityOrderRemote().updateStatusPortability(npItemDeviceId,npItemid,npStatus,npModality);
    }catch(Throwable t){
       manageCatch(hshDataMap, t);
    }
    return hshDataMap;
  }
  
  public HashMap getPhoneNumberItem(long npItemId,long npItemDeviceId){
    HashMap hshDataMap = new HashMap();
    try {        
       hshDataMap = getSEJBPortabilityOrderRemote().getPhoneNumberItem(npItemId,npItemDeviceId);
    }catch(Throwable t){
       manageCatch(hshDataMap, t);
    }
    return hshDataMap;
  }
  
  public HashMap getItemsPortLst(long npOrderId){ //1
    HashMap hshDataMap = new HashMap();
    try {
       hshDataMap = getSEJBPortabilityOrderRemote().getItemsPortLst(npOrderId);
    }catch(Throwable t){
       manageCatch(hshDataMap, t);
    }
    return hshDataMap;
  }
  

   /***********************************************************************************************************************************
   * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
   * <br>Motivo: Obtiene el detall del item de la Orden de Portabilidad
   * <br>Fecha: 21/08/2009      
   /************************************************************************************************************************************/
  public HashMap getItemOrderPortabilityReturn(long lOrderId){   
      HashMap hshMap=new HashMap();
      try{
         hshMap =  getSEJBPortabilityOrderRemote().getItemOrderPortabilityReturn(lOrderId);             
      }catch(RemoteException re){
         System.out.println("PortabilityOrderService getItemOrderPortabilityReturn \nMensaje:" + re.getMessage()+"\n");               
          hshMap.put("strMessage",re.getMessage());
      }catch(Exception ex){
         System.out.println("PortabilityOrderService getItemOrderPortabilityReturn \nMensaje:" + ex.getMessage()+"\n");         
          hshMap.put("strMessage",ex.getMessage());
      } 
       return  hshMap;        
  }
  
  
   /***********************************************************************************************************************************
   * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
   * <br>Motivo: Obtiene el detall del item de la Orden de Portabilidad
   * <br>Fecha: 29/08/2009      
   /************************************************************************************************************************************/
  public HashMap getOrderPortabilityReturn(long lOrderId){   
      HashMap hshMap=new HashMap();
      try{
         hshMap =  getSEJBPortabilityOrderRemote().getOrderPortabilityReturn(lOrderId);             
      }catch(RemoteException re){
         System.out.println("PortabilityOrderService getOrderPortabilityReturn \nMensaje:" + re.getMessage()+"\n");               
          hshMap.put("strMessage",re.getMessage());
      }catch(Exception ex){
         System.out.println("PortabilityOrderService getOrderPortabilityReturn \nMensaje:" + ex.getMessage()+"\n");         
          hshMap.put("strMessage",ex.getMessage());
      } 
       return  hshMap;        
  }
  
   /***********************************************************************************************************************************
   * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
   * <br>Motivo: Obtiene el detall del item de la Orden de Portabilidad de Retorno cunado Nextel es Cedente
   * <br>Fecha: 02/09/2009      
   /************************************************************************************************************************************/
  public HashMap getItemOrderPortabilityReturnHome(long lOrderId){   
      HashMap hshMap=new HashMap();
      try{
         hshMap =  getSEJBPortabilityOrderRemote().getItemOrderPortabilityReturnHome(lOrderId);             
      }catch(RemoteException re){
         System.out.println("PortabilityOrderService getItemOrderPortabilityReturnHome \nMensaje:" + re.getMessage()+"\n");               
          hshMap.put("strMessage",re.getMessage());
      }catch(Exception ex){
         System.out.println("PortabilityOrderService getItemOrderPortabilityReturnHome \nMensaje:" + ex.getMessage()+"\n");         
          hshMap.put("strMessage",ex.getMessage());
      } 
       return  hshMap;        
  }
  
  /***********************************************************************************************************************************
   * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
   * <br>Motivo: Obtiene el detalle de la Orden de Portabilidad de Retorno cunado Nextel es Cedente
   * <br>Fecha: 02/09/2009      
   /************************************************************************************************************************************/
  public HashMap getOrderPortabilityReturnHome(long lOrderId){   
      HashMap hshMap=new HashMap();
      try{
         hshMap =  getSEJBPortabilityOrderRemote().getOrderPortabilityReturnHome(lOrderId);             
      }catch(RemoteException re){
         System.out.println("PortabilityOrderService getOrderPortabilityReturnHome \nMensaje:" + re.getMessage()+"\n");               
          hshMap.put("strMessage",re.getMessage());
      }catch(Exception ex){
         System.out.println("PortabilityOrderService getOrderPortabilityReturnHome \nMensaje:" + ex.getMessage()+"\n");         
          hshMap.put("strMessage",ex.getMessage());
      } 
       return  hshMap;        
  }
  
  /***********************************************************************************************************************************
   * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
   * <br>Motivo: Obtiene el detalle del item de la Orden de Portabilidad de Retorno cuando Nextel es Cedente
   * <br>Fecha: 04/09/2009      
   /************************************************************************************************************************************/
  public HashMap getDetailItemPortabilityReturnHome(String strPhoneNumber){   
      HashMap hshMap=new HashMap();
      try{
         hshMap =  getSEJBPortabilityOrderRemote().getDetailItemPortabilityReturnHome(strPhoneNumber);             
      }catch(RemoteException re){
         System.out.println("PortabilityOrderService getDetailItemPortabilityReturnHome \nMensaje:" + re.getMessage()+"\n");               
          hshMap.put("strMessage",re.getMessage());
      }catch(Exception ex){
         System.out.println("PortabilityOrderService getDetailItemPortabilityReturnHome \nMensaje:" + ex.getMessage()+"\n");         
          hshMap.put("strMessage",ex.getMessage());
      } 
       return  hshMap;        
  }
  
   /***********************************************************************************************************************************
   * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
   * <br>Motivo: Obtiene el detalle del item de la Orden de Portabilidad de Retorno cuando Nextel es Cedente
   * <br>Fecha: 04/09/2009      
   /************************************************************************************************************************************/
  public HashMap getDetailServicePortabilidad(long lngServiceId){ 
      HashMap hshMap=new HashMap();
      ServiciosBean objServiciosBean=new ServiciosBean();
      String strMessage = null;
      try{
         objServiciosBean =  getSEJBPortabilityOrderRemote().getDetailServicePortabilidad(lngServiceId);             
      }catch(RemoteException re){
         System.out.println("PortabilityOrderService getDetailServicePortabilidad \nMensaje:" + re.getMessage()+"\n");               
          hshMap.put("strMessage",re.getMessage());
      }catch(Exception ex){
         System.out.println("PortabilityOrderService getDetailServicePortabilidad \nMensaje:" + ex.getMessage()+"\n");         
          hshMap.put("strMessage",ex.getMessage());
      } 
      
      hshMap.put("objServiciosBean",objServiciosBean);
      hshMap.put("strMessage",strMessage);
      
      return  hshMap;        
  }
  
  public long calculateBalance(long lOrderId){
     HashMap hshMap=new HashMap();
     PortabilityOrderBean objPortabOBean = new PortabilityOrderBean();
     try{
        hshMap = getSEJBPortabilityOrderRemote().calculateBalance(lOrderId);
        objPortabOBean = (PortabilityOrderBean)hshMap.get("objPOBean3");
        
     }catch(RemoteException re){
        System.out.println("EditOrderService calculateBalance \nMensaje:" + re.getMessage()+"\n");
        return -1;
     }catch(SQLException ex){
        System.out.println("EditOrderService calculateBalance \nMensaje:" + ex.getMessage()+"\n");
        return -1;           
     }
     catch(Exception ex){
        System.out.println("EditOrderService calculateBalance \nMensaje:" + ex.getMessage()+"\n");
        return -1;           
     }
    return objPortabOBean.getNpBalance();
  }
  
  
   /***********************************************************************************************************************************
   * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
   * <br>Motivo: Obtiene el título de la Orden (Padre ó Hija) y su Nro de Orden respectivo.
   * <br>Fecha: 23/10/2009      
   /************************************************************************************************************************************/
  public HashMap getParentChildOrder(long getParentChildOrder){   
      HashMap hshMap=new HashMap();
      try{
         hshMap =  getSEJBPortabilityOrderRemote().getParentChildOrder(getParentChildOrder);             
      }catch(RemoteException re){
         System.out.println("PortabilityOrderService getParentChildOrder \nMensaje:" + re.getMessage()+"\n");               
          hshMap.put("strMessage",re.getMessage());
      }catch(Exception ex){
         System.out.println("PortabilityOrderService getParentChildOrder \nMensaje:" + ex.getMessage()+"\n");         
          hshMap.put("strMessage",ex.getMessage());
      } 
       return  hshMap;        
  }
  
  public HashMap getInboxOrder(long strOrderId){
    HashMap hshDataMap = new HashMap();        
    try {
        hshDataMap = getSEJBPortabilityOrderRemote().getInboxOrder(strOrderId);
    } catch (Throwable t) {
        manageCatch(hshDataMap, t);
    }    
    return hshDataMap;
  }
  
  public HashMap checkNumberPorted(String npphoneNumber, long lOrderId){
    HashMap hshDataMap=new HashMap();
      try{
         hshDataMap =  getSEJBPortabilityOrderRemote().checkNumberPorted(npphoneNumber,lOrderId);             
      }catch (Throwable t) {
        manageCatch(hshDataMap, t);
      } 
      return  hshDataMap;
    
  }
  
   /***********************************************************************************************************************************
   * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
   * <br>Motivo:xxxx
   * <br>Fecha: 16/11/2009      
   /************************************************************************************************************************************/
    public HashMap getOrderScreenFieldPorta(long lOrderId,String strPage){     
       try{
          return getSEJBPortabilityOrderRemote().getOrderScreenFieldPorta(lOrderId,strPage);
       }catch(RemoteException re){
          System.out.println("PortabilityOrderService getOrderScreenFieldPorta \nMensaje:" + re.getMessage()+"\n");
          HashMap hshMap=new HashMap();
          hshMap.put("strMessage",re.getMessage());
          return hshMap;
       }catch(SQLException ex){
          System.out.println("PortabilityOrderService getOrderScreenFieldPorta \nMensaje:" + ex.getMessage()+"\n");            
          HashMap hshMap=new HashMap();
          hshMap.put("strMessage",ex.getMessage());
          return hshMap;
       }   
       catch(Exception ex){
          System.out.println("PortabilityOrderService getOrderScreenFieldPorta \nMensaje:" + ex.getMessage()+"\n");            
          HashMap hshMap=new HashMap();
          hshMap.put("strMessage",ex.getMessage());
          return hshMap;
       }
    }     
    
    
    /**
    * Verifica si el usuario pertene a determinado Inbox
    */
   public int getValidInboxContent(int lUserId, String strInboxName, int lInboxType ) {
		int lValidation = 0;
		try {			
         lValidation = getSEJBPortabilityOrderRemote().getValidInboxContent(lUserId,strInboxName,lInboxType);
    }catch (Exception e) {
      logger.error(formatException(e));
    }
		return lValidation;
	}
  
  
  /**
   * @see pe.com.nextel.dao.GeneralDAO#getRolePermissions(String strInbox)
   */   
   public int getValInboxEditableUser(String strInbox, String strLogin){
      int intValidationInboxUser = 0;
    try{
      intValidationInboxUser = getSEJBPortabilityOrderRemote().getValInboxEditableUser(strInbox, strLogin);
    }catch (Exception e) {
      logger.error(formatException(e));
    }
    return intValidationInboxUser;
   }
  
  
  /**
   * @see pe.com.nextel.dao.GeneralDAO#getRolePermissions(String strInbox)
   */   
   public int getValMsgSub(long lOrderId, long npItemId){
      int intValMsgSub = 0;
    try{
      intValMsgSub = getSEJBPortabilityOrderRemote().getValMsgSub(lOrderId, npItemId);
    }catch (Exception e) {
      logger.error(formatException(e));
    }
    return intValMsgSub;
   }
   
   /***********************************************************************************************************************************
   * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
   * <br>Motivo:Obtener el valor y la descripción de la tabla de Portability.Np_Configuration_pkg (Variables)
   * <br>Fecha: 25/10/2010      
   /************************************************************************************************************************************/
    public HashMap getDocumentList(String strNptable){     
       try{
          return getSEJBPortabilityOrderRemote().getDocumentList(strNptable);
       }catch(RemoteException re){
          System.out.println("PortabilityOrderService getDocumentList \nMensaje:" + re.getMessage()+"\n");
          HashMap hshMap=new HashMap();
          hshMap.put("strMessage",re.getMessage());
          return hshMap;
       }catch(SQLException ex){
          System.out.println("PortabilityOrderService getDocumentList \nMensaje:" + ex.getMessage()+"\n");            
          HashMap hshMap=new HashMap();
          hshMap.put("strMessage",ex.getMessage());
          return hshMap;
       }   
       catch(Exception ex){
          System.out.println("PortabilityOrderService getDocumentList \nMensaje:" + ex.getMessage()+"\n");            
          HashMap hshMap=new HashMap();
          hshMap.put("strMessage",ex.getMessage());
          return hshMap;
       }
    }     
    
    
    /***********************************************************************************************************************************
   * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
   * <br>Motivo:Obtener el valor y la descripción de la tabla de Portability.Np_Configuration_pkg (Variables)
   * <br>Fecha: 25/10/2010      
   /************************************************************************************************************************************/
    public HashMap getSectionDocumentValidate(String strNptable, String strNpDescripcion, String strNpvalue) {   
       try{
          return getSEJBPortabilityOrderRemote().getSectionDocumentValidate(strNptable,strNpDescripcion,strNpvalue);
       }catch(RemoteException re){
          System.out.println("PortabilityOrderService getSectionDocumentValidate \nMensaje:" + re.getMessage()+"\n");
          HashMap hshMap=new HashMap();
          hshMap.put("strMessage",re.getMessage());
          return hshMap;
       }catch(SQLException ex){
          System.out.println("PortabilityOrderService getSectionDocumentValidate \nMensaje:" + ex.getMessage()+"\n");            
          HashMap hshMap=new HashMap();
          hshMap.put("strMessage",ex.getMessage());
          return hshMap;
       }   
       catch(Exception ex){
          System.out.println("PortabilityOrderService getSectionDocumentValidate \nMensaje:" + ex.getMessage()+"\n");            
          HashMap hshMap=new HashMap();
          hshMap.put("strMessage",ex.getMessage());
          return hshMap;
       }
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
        try{
            return getSEJBPortabilityOrderRemote().getFlagSendSP(orderId);
        }catch(Exception e){
            logger.error(formatException(e));
            return -2;
        }
    
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
        try{
            return getSEJBPortabilityOrderRemote().getFlagSendPP(orderId);
        }catch(Exception e){
            logger.error(formatException(e));
            return -2;
        }

    }
    /**
     * Motivo: Obtiene tipo de division y expresion regular para validacion de numeros a portar
     * <br>Realizado por: <a href="mailto:anthony.mateo@teamsoft.com.pe">Anthony Mateo</a>
     * <br>Fecha: 19/05/2016
     * @param     orderId
     * @return    HashMap
     */
    public String getExpValidation(long orderId) throws  SQLException, Exception{
        try{
            return getSEJBPortabilityOrderRemote().getExpValidation(orderId);
        }catch (SQLException e){
            logger.error(formatException(e));
            return "ERROR SQL";
        }catch (Exception e){
            logger.error(formatException(e));
            return "OTHER ERROR";
        }

    }
   
    /**
     * Motivo: Envío de subsanación de portabilidad
     * <br>Realizado por: <a href="mailto:gfarfan@soaint.com">Gabriel Farfán</a>
     * <br>Fecha: 04/09/2018
     * @param     sendMessageBE
     * @return    HashMap
     */
    public HashMap wsPortabilitySubSanacion(SendMessageBE sendMessageBE) {
        HashMap hshDataMap = new HashMap();
        try {
            hshDataMap = getSEJBPortabilityOrderRemote().wsPortabilitySubSanacion(sendMessageBE);
        } catch(Throwable t){
            manageCatch(hshDataMap, t);
        }
        return hshDataMap;
    }
}