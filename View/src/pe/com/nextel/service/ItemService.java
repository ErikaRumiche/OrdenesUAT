package pe.com.nextel.service;

import java.io.Serializable;

import java.rmi.RemoteException;

import java.sql.SQLException;

import java.util.HashMap;
import java.util.List;

import javax.naming.Context;

import javax.rmi.PortableRemoteObject;

import pe.com.nextel.ejb.SEJBOrderEditRemote;
import pe.com.nextel.ejb.SEJBOrderEditRemoteHome;
import pe.com.nextel.ejb.SEJBOrderNewRemote;
import pe.com.nextel.ejb.SEJBOrderNewRemoteHome;
import pe.com.nextel.ejb.SEJBOrderSearchRemote;
import pe.com.nextel.ejb.SEJBOrderSearchRemoteHome;
import pe.com.nextel.ejb.SEJBCustomerAccountFSRemote;
import pe.com.nextel.ejb.SEJBCustomerAccountFSRemoteHome;
import pe.com.nextel.ejb.SEJBContractFSRemote;
import pe.com.nextel.ejb.SEJBContractFSRemoteHome;
import pe.com.nextel.bean.RequestContractFSBean;
import pe.com.nextel.bean.RequestCustomerAccountFSBean;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;


/**
 * Developer : José María Espinoza Bueno.
 * Objetivo  : Interface que provee los servicios del EJB
 *             para ser consumidos por la capa Controller
 */

public class ItemService extends GenericService implements Serializable{

  public static SEJBOrderNewRemote getSEJBOrderNewRemote() {
    try{
      final Context context = MiUtil.getInitialContext();
      final SEJBOrderNewRemoteHome sEJBOrderNewRemoteHome = 
            (SEJBOrderNewRemoteHome) PortableRemoteObject.narrow( context.lookup( "SEJBOrderNew" ), SEJBOrderNewRemoteHome.class );
      SEJBOrderNewRemote sEJBOrderNewRemote;
      sEJBOrderNewRemote = sEJBOrderNewRemoteHome.create();
        
      return sEJBOrderNewRemote;
    }catch(Exception ex) {
      System.out.println("Exception : [NewOrderService][getSEJBOrderNewRemote]["+ex.getMessage()+"]");
      return null;
    } 
  }
  
  /**
   * Developer : Lee Rosales
   * Objetivo  : Permite crear el ejb stateless local
  */

  /*public static SEJBOrderNewRemote getSEJBOrderNewRemote() {
    try{
      final Context context = MiUtil.getInitialContext();
      final SEJBOrderNewRemoteHome sEJBOrderNewRemoteHome = 
            (SEJBOrderNewRemoteHome) PortableRemoteObject.narrow( context.lookup( "SEJBOrderNew" ), SEJBOrderNewRemoteHome.class );
      SEJBOrderNewRemote sEJBOrderNewRemote;
      sEJBOrderNewRemote = sEJBOrderNewRemoteHome.create();
        
      return sEJBOrderNewRemote;
    }catch(Exception ex) {
      System.out.println("Exception : [NewOrderService][getSEJBOrderNewRemote]["+ex.getMessage()+"]");
      return null;
    } 
  }*/
  
  public static SEJBOrderEditRemote getSEJBOrderEditRemote() {
    try{
      final Context context = MiUtil.getInitialContext();
      final SEJBOrderEditRemoteHome sEJBOrderEditRemoteHome = 
            (SEJBOrderEditRemoteHome) PortableRemoteObject.narrow( context.lookup( "SEJBOrderEdit" ), SEJBOrderEditRemoteHome.class );
      SEJBOrderEditRemote sEJBOrderEditRemote;
      sEJBOrderEditRemote = sEJBOrderEditRemoteHome.create();
       
      return sEJBOrderEditRemote;
    }catch(Exception ex) {
      System.out.println("Exception : [ItemService][getSEJBOrderEditRemote]["+ex.getMessage()+"]");
      return null;
    } 
  }
     
     
     public static HashMap OrderDAOgetAddendasList(int id_prom, int id_plan, int id_specification, int id_kit){
       HashMap shMap = new HashMap();  
        
       try {
            return getSEJBOrderNewRemote().OrderDAOgetAddendasList(id_prom,id_plan,id_specification, id_kit);
        }catch (SQLException e) {
            shMap.put("strMessage",e.getMessage());
            return shMap;
        }catch (RemoteException e) {
            shMap.put("strMessage",e.getMessage());
            return shMap;
        }catch (Exception e) {
            shMap.put("strMessage",e.getMessage());
            return shMap;
        }
    }
    
     public static String OrderDAOgetNpAllowAdenda(int id_specification){
      
       try {
            return getSEJBOrderNewRemote().OrderDAOgetNpAllowAdenda(id_specification);
        }catch (SQLException e) {
            System.out.println("Error [SQLException][ItemService][OrderDAOgetNpAllowAdenda][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (RemoteException e) {
            System.out.println("Error [RemoteException][ItemService][OrderDAOgetAddendasList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (Exception e) {
            System.out.println("Error [Exception][ItemService][OrderDAOgetAddendasList][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
    }

    /**
     * Developer : Javier Villanueva Rangel
     * Objetivo  : Obtener un flag para ver si se la seccion de adenda para una especificacion se bloquea o no.
     */
    public static String OrderDAOgetValidationAdenda(int id_specification){

        try {
            return getSEJBOrderNewRemote().OrderDAOgetValidationAdenda(id_specification);
        }catch (SQLException e) {
            System.out.println("Error [SQLException][ItemService][OrderDAOgetValidationAdenda][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (RemoteException e) {
            System.out.println("Error [RemoteException][ItemService][OrderDAOgetValidationAdenda][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
        catch (Exception e) {
            System.out.println("Error [Exception][ItemService][OrderDAOgetValidationAdenda][" + e.getMessage() + "]["+e.getClass()+"]");
            return null;
        }
    }
    
     public static HashMap OrderDAOgetTemplateOrder(int id_order, int id_item){
       HashMap shMap = new HashMap();  
        
       try {
            return getSEJBOrderNewRemote().OrderDAOgetTemplateOrder(id_order,id_item);
        }catch (SQLException e) {
            shMap.put("strMessage",e.getMessage());
            return shMap;
        }catch (RemoteException e) {
            shMap.put("strMessage",e.getMessage());
            return shMap;
        }catch (Exception e) {
            shMap.put("strMessage",e.getMessage());
            return shMap;
        }
     }    
    
     public static HashMap OrderDAOgetNumAddendumAct(int id_customer, String id_num_nextel){
       
        HashMap shMap = new HashMap();  
        
        try {
            return getSEJBOrderNewRemote().OrderDAOgetNumAddendumAct(id_customer,id_num_nextel);
        }catch (SQLException e) {
            shMap.put("strMessage",e.getMessage());
            return shMap;
        }catch (RemoteException e) {
            shMap.put("strMessage",e.getMessage());
            return shMap;
        }catch (Exception e) {
            shMap.put("strMessage",e.getMessage());
            return shMap;
        }
        
    }
    //EIORTIZ 16-06-2016
    public static HashMap OrderDAOgetNumAddendumActSpec(int id_customer, String id_num_nextel, String id_specification){
        System.out.println("[ItemService.java.OrderDAOgetNumAddendumActSpec]: Ejecutando este metodo");
        HashMap shMap = new HashMap();

        try {
            return getSEJBOrderNewRemote().OrderDAOgetNumAddendumActSpec(id_customer, id_num_nextel,id_specification);
        }catch (SQLException e) {
            shMap.put("strMessage",e.getMessage());
            return shMap;
        }catch (RemoteException e) {
            shMap.put("strMessage",e.getMessage());
            return shMap;
        }catch (Exception e) {
            shMap.put("strMessage",e.getMessage());
            return shMap;
        }
    }
	
    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - FIN
     *  REALIZADO POR: Richard De los Reyes
     *  FECHA: 28/08/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/

  	/**
	 * @see pe.com.nextel.dao.ItemDAO#doValidateIMEI(HashMap, Connection)
	 */
	public HashMap doValidateIMEI(HashMap hshItemDeviceMap) {
		HashMap hshDataMap = new HashMap();
		try {
			hshDataMap.put(Constante.MESSAGE_OUTPUT, getSEJBOrderNewRemote().doValidateIMEI(hshItemDeviceMap));
		} catch(Throwable t) {
			manageCatch(hshDataMap, t);
		}
		return hshDataMap;
	}
	
	/**
	 * @see pe.com.nextel.dao.ItemDAO#doValidateIMEI(HashMap, Connection)
	 */
	public HashMap getInboxGenerateGuide() {
		HashMap hshDataMap = new HashMap();
		try {
			hshDataMap = getSEJBOrderNewRemote().getInboxGenerateGuide();
		} catch(Throwable t) {
			manageCatch(hshDataMap, t);
		}
		return hshDataMap;
	}
   
    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - FIN
     *  REALIZADO POR: Richard De los Reyes
     *  FECHA: 28/08/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/


    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - INICIO
     *  REALIZADO POR: Carmen Gremios
     *  FECHA: 13/02/2008
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/
     
   public HashMap doValidateIMEI(String strIMEI){   
      HashMap shMap=new HashMap();
      try{         
         return getSEJBOrderNewRemote().doValidateIMEI(strIMEI);             
      }catch(RemoteException re){
         System.out.println("ItemService doValidateIMEI \nMensaje:" + re.getMessage()+"\n");               
         shMap.put("strMessage",re.getMessage());
         return shMap;                    
      }catch(Exception ex){
         System.out.println("ItemService doValidateIMEI \nMensaje:" + ex.getMessage()+"\n");         
         shMap.put("strMessage",ex.getMessage());
         return shMap;        
      }         
   }       
   
    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - FIN
     *  REALIZADO POR: Carmen Gremios
     *  FECHA: 13/02/2008
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/   
    
     /***********************************************************************
      ***********************************************************************
      ***********************************************************************
      *  CAP & CAL INICIO
      *  REALIZADO POR: CPUENTE
      *  FECHA: 28/08/2009
      ***********************************************************************
      ***********************************************************************
      ***********************************************************************/
      public HashMap doValidateOwnEquipment(String strIMEI) {
        HashMap shMap = new HashMap();
        try {
            return getSEJBOrderNewRemote().doValidateOwnEquipment(strIMEI);
        } catch (RemoteException re) {
            System.out.println("ItemService doValidateOwnEquipment \nMensaje:" + 
                               re.getMessage() + "\n");
            shMap.put("strMessage", re.getMessage());
            return shMap;
        } catch (Exception ex) {
            System.out.println("ItemService doValidateOwnEquipment \nMensaje:" + 
                               ex.getMessage() + "\n");
            shMap.put("strMessage", ex.getMessage());
            return shMap;
        }
    }

    /***********************************************************************
      ***********************************************************************
      ***********************************************************************
      *  CAP & CAL FIN
      *  REALIZADO POR: CPUENTE
      *  FECHA: 28/08/2009
      ***********************************************************************
      ***********************************************************************
      ***********************************************************************/
         
     /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  VALIDACION DE SIM - INICIO
     *  REALIZADO POR: Jorge Pérez
     *  FECHA: 08/03/2008
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/    
    /**
    * @see pe.com.nextel.dao.ItemDAO#doValidateSIM(String strSIM)
    */     
     public HashMap doValidateSIM(String strSIM){
       HashMap objHashMap=new HashMap();
       String  strMessage = new String();
       try{
         return getSEJBOrderNewRemote().doValidateSIM(strSIM);
       }catch (SQLException e) {
            System.out.println("[SQLException][ItemService][doValidateSIM][" + e.getMessage() + "]["+e.getClass()+"]");
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            System.out.println("[RemoteException][ItemService][doValidateSIM][" + e.getMessage() + "]["+e.getClass()+"]");
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][ItemService][doValidateSIM][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }            
     }
             
     /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  VALIDACION DE SIM - FIN
     *  REALIZADO POR: Jorge Pérez
     *  FECHA: 08/03/2008
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/   
  /**
   * @autor Odubock
   * @return 1 = Nueva, 2 = Renovación
   * @param iTemplateId
   * @param strNumeroNextel
   */
     public static HashMap getTipoPlantillaAdenda(String strNumeroNextel, int iTemplateId){
       HashMap shMap = new HashMap();  
        
       try {
            return getSEJBOrderNewRemote().getTipoPlantillaAdenda(strNumeroNextel,iTemplateId);
        }catch (SQLException e) {
            shMap.put("strMessage",e.getMessage());
            return shMap;
        }catch (RemoteException e) {
            shMap.put("strMessage",e.getMessage());
            return shMap;
        }catch (Exception e) {
            shMap.put("strMessage",e.getMessage());
            return shMap;
        }
    }
    
    public HashMap getImeiSimMatch(long lSpecificationId ,String strImei, String strSim){
      HashMap objHashMap=new HashMap();
      String  strMessage = new String();
      try{
        return getSEJBOrderEditRemote().getImeiSimMatch(lSpecificationId, strImei, strSim);
      }catch (SQLException e) {
            strMessage = "[SQLException][ItemService][doValidateSIM][" + e.getMessage() + "]["+e.getClass()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage = "[RemoteException][ItemService][doValidateSIM][" + e.getMessage() + "]["+e.getClass()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][ItemService][doValidateSIM][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }
    
    
  /**
	 * @see pe.com.nextel.dao.ItemDAO#doValidateMassiveSim(long lngCustomerId, 
                                                         long lngSpecificationId, 
                                                         String strModality,
                                                         String[] strSim)
	 */
	public HashMap doValidateMassiveSim(long lngCustomerId, 
                                      long lngSpecificationId, 
                                      String strModality,
                                      String[] strSim) {
		HashMap hshDataMap = new HashMap();
		try {
			hshDataMap = getSEJBOrderNewRemote().doValidateMassiveSim(lngCustomerId, 
                                                                lngSpecificationId, 
                                                                strModality,
                                                                strSim);
		} catch(Throwable t) {
			manageCatch(hshDataMap, t);
		}
		return hshDataMap;
	}
  
  /*jsalazar - modif hpptt # 1 - 29/09/2010 - inicio*/   

  public static SEJBOrderSearchRemote getSEJBOrderSearchRemote() {
  
    Context context = null;
    SEJBOrderSearchRemoteHome sEJBOrderSearchRemoteHome = null;
    SEJBOrderSearchRemote aux = null;
    try{
      context = MiUtil.getInitialContext();
      sEJBOrderSearchRemoteHome = 
            (SEJBOrderSearchRemoteHome) PortableRemoteObject.narrow( context.lookup( "SEJBOrderSearch" ), SEJBOrderSearchRemoteHome.class );
      aux = sEJBOrderSearchRemoteHome.create();
    }catch(Exception ex) {
      System.out.println("Exception : [NewOrderService][getSEJBOrderSearchRemote]["+ex.getMessage()+"]");
    } 
    return aux;
  }
     
  public  HashMap getItemServicioPendingList(long lOrderId, long lItemId) throws SQLException{
       HashMap shMap = new HashMap();  
       try {
            shMap = (HashMap)getSEJBOrderSearchRemote().getItemServicePendingList(lOrderId, lItemId);
        }catch(Throwable t){
        manageCatch(shMap, t);
       }
       return shMap;
    }
/*jsalazar - modif hpptt # 1 - 29/09/2010 - fin*/
    
  /**
   * Metodo que busca precios de promoción de volumen de orden para cada items de la lista
   * @autor César Lozza
   * @return Lista de items con precios de volumen de orden
   * @param itemsList
   * @param typeWindow
   * @param specificationId
   * @param customerId
   */
    public static HashMap evaluateOrderVolume(int customerId, int specificationId, String typeWindow, List itemsList){
       HashMap shMap = new HashMap();  
        
       try {
            shMap = getSEJBOrderNewRemote().evaluateOrderVolume(customerId, specificationId, typeWindow, itemsList);
        }catch (SQLException e) {
            shMap.put("strMessage",e.getMessage());
        }catch (RemoteException e) {
            shMap.put("strMessage",e.getMessage());
        }catch (Exception e) {
            shMap.put("strMessage",e.getMessage());
        }
        
        return shMap;
    }
    
    public HashMap ItemDAOdoValidateIMEICustomer(String strIMEI){
      HashMap objHashMap = new HashMap();
      String  strMessage = new String();
      try{
        return getSEJBOrderEditRemote().ItemDAOdoValidateIMEICustomer(strIMEI);
      }catch (SQLException e) {
            strMessage = "[SQLException][ItemService][ItemDAOdoValidateIMEICustomer][" + e.getMessage() + "]["+e.getClass()+"]";
            System.out.println(strMessage);
            objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage = "[RemoteException][ItemService][ItemDAOdoValidateIMEICustomer][" + e.getMessage() + "]["+e.getClass()+"]";
            System.out.println(strMessage);
            objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][ItemService][ItemDAOdoValidateIMEICustomer][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
            return objHashMap;
        }
    } 
  
    //[TDECONV003-8] INI PZACARIAS
    public static SEJBCustomerAccountFSRemote getSEJBCustomerAccountFSRemote() {
        try{
            final Context context = MiUtil.getInitialContext();
            final SEJBCustomerAccountFSRemoteHome sEJBCustomerAccountFSRemoteHome =
                    (SEJBCustomerAccountFSRemoteHome) PortableRemoteObject.narrow( context.lookup( "SEJBCustomerAccountFS" ), SEJBCustomerAccountFSRemoteHome.class );
            SEJBCustomerAccountFSRemote sEJBCustomerAccountFSRemote;
            sEJBCustomerAccountFSRemote = sEJBCustomerAccountFSRemoteHome.create();

            return sEJBCustomerAccountFSRemote;
        }catch(Exception ex) {
            logger.error("Error [Exception][ItemService][getSEJBCustomerAccountFSRemote][" + ex.getMessage() + "]");
            return null;
        }
    }

    public static SEJBContractFSRemote getSEJBContractFSRemote() {
        try{
            final Context context = MiUtil.getInitialContext();
            final SEJBContractFSRemoteHome sEJBContractFSRemoteHome =
                    (SEJBContractFSRemoteHome) PortableRemoteObject.narrow( context.lookup( "SEJBContractFS" ), SEJBContractFSRemoteHome.class );
            SEJBContractFSRemote sEJBContractFSRemote;
            sEJBContractFSRemote = sEJBContractFSRemoteHome.create();

            return sEJBContractFSRemote;
        }catch(Exception ex) {
            logger.error("Error [Exception][ItemService][getSEJBContractFSRemote][" + ex.getMessage() + "]");
            return null;
        }
    }

    public HashMap getTipDocFS(RequestCustomerAccountFSBean request) {
        HashMap hshDataMap = new HashMap();
        try {
            hshDataMap = getSEJBCustomerAccountFSRemote().getTipDocFS(request);
        }
        catch(Throwable t) {
            manageCatch(hshDataMap, t);
            logger.error("Error [Exception][ItemService][getTipDocFS][" + t.getMessage() + "]");
        }
        return hshDataMap;
    }

    public HashMap getSIM_MSISDN_FS(RequestContractFSBean request) {
        HashMap hshDataMap = new HashMap();
        try {
            hshDataMap = getSEJBContractFSRemote().getSIM_MSISDN_FS(request);
        }
        catch(Throwable t) {
            manageCatch(hshDataMap, t);
            logger.error("Error [Exception][ItemService][getSIM_MSISDN_FS][" + t.getMessage() + "]");
        }
        return hshDataMap;
    }
    //[TDECONV003-8] FIN
  
}