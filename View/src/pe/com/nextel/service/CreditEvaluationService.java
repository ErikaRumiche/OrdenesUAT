package pe.com.nextel.service;

import java.rmi.RemoteException;

import java.sql.SQLException;

import java.util.HashMap;

import javax.naming.Context;

import javax.rmi.PortableRemoteObject;

import pe.com.nextel.ejb.SEJBCreditEvaluationRemote;
import pe.com.nextel.ejb.SEJBCreditEvaluationRemoteHome;
import pe.com.nextel.util.MiUtil;
import pe.com.nextel.util.RequestHashMap;


public class CreditEvaluationService extends GenericService
{
  public static SEJBCreditEvaluationRemote getSEJBCreditEvaluationRemote() {
    try{
      final Context context = MiUtil.getInitialContext();
      final SEJBCreditEvaluationRemoteHome sEJBCreditEvaluationRemoteHome = 
        (SEJBCreditEvaluationRemoteHome) PortableRemoteObject.narrow( context.lookup( "SEJBCreditEvaluation" ), SEJBCreditEvaluationRemoteHome.class );
      SEJBCreditEvaluationRemote sEJBCreditEvaluationRemote;
      sEJBCreditEvaluationRemote = sEJBCreditEvaluationRemoteHome.create();      
      return sEJBCreditEvaluationRemote;
    }catch(Exception ex) {
      System.out.println("Exception : [CustomerService][getSEJBCustomerRemote]["+ex.getMessage()+"]");
      return null;
    }
  }

  /**
  * <br>Realizado por: <a href="mailto:jose.casas@nextel.com.pe">José Casas</a>
  * @see pe.com.nextel.dao.CreditEvaluationDAO#getCreditEvaluationData(int,String)      
  */ 
  public HashMap getCreditEvaluationData(long lSourceId, String strSource) throws Exception, SQLException {
    try{
      return getSEJBCreditEvaluationRemote().getCreditEvaluationData(lSourceId, strSource);
    } catch(SQLException sql){
      System.out.println("CreditEvaluationService getCreditEvaluationData \nMensaje:" + sql.getMessage()+"\n");
      HashMap hshData=new HashMap();
      hshData.put("strMessage",sql.getMessage());
      return hshData;
    } catch(RemoteException re){
      System.out.println("CreditEvaluationService getCreditEvaluationData \nMensaje:" + re.getMessage()+"\n");
      HashMap hshData=new HashMap();
      hshData.put("strMessage",re.getMessage());             
      return hshData;  
    }catch(Exception ex){
      System.out.println("CreditEvaluationService getCreditEvaluationData \nMensaje:" + ex.getMessage()+"\n");            
      HashMap hshData=new HashMap();
      hshData.put("strMessage",ex.getMessage());             
      return hshData;  
    }
  }
  
  
  /**
  * <br>Realizado por: <a href="mailto:jose.casas@nextel.com.pe">José Casas</a>
  * <br>Fecha: 05/06/2009
  * @see pe.com.nextel.dao.CreditEvaluationDAO#getRuleResult(long,String)      
  */      
  public HashMap getRuleResult(long lCreditEvaluationId,String strSource) throws Exception, SQLException{        
    try{        
      return getSEJBCreditEvaluationRemote().getRuleResult(lCreditEvaluationId, strSource);
    }catch(SQLException sql){
      System.out.println("CreditEvaluationService getRuleResult \nMensaje:" + sql.getMessage()+"\n");
      HashMap hshData=new HashMap();
      hshData.put("strMessage",sql.getMessage());
      return hshData;
    }catch(RemoteException re){
      System.out.println("CreditEvaluationService getRuleResult \nMensaje:" + re.getMessage()+"\n");
      HashMap hshData=new HashMap();
      hshData.put("strMessage",re.getMessage());
      return hshData;
    }catch(Exception ex){
      System.out.println("CreditEvaluationService getRuleResult \nMensaje:" + ex.getMessage()+"\n");
      HashMap hshData=new HashMap();
      hshData.put("strMessage",ex.getMessage());
      return hshData;
    }
  }

  /**
  * <br>Realizado por: <a href="mailto:jose.casas@nextel.com.pe">José Casas</a>
  * <br>Fecha: 05/06/2009
  * @see pe.com.nextel.dao.CreditEvaluationDAO#getRuleResult(long,String)      
  */      
  public HashMap doEvaluateOrder(RequestHashMap objHashMap) throws Exception, SQLException{
    logger.info("******************* INICIO CreditEvaluationService > doEvaluateOrder *******************");
    HashMap hshResultCreditEvaluation = new HashMap();
    try{
      hshResultCreditEvaluation= getSEJBCreditEvaluationRemote().doEvaluateOrder(objHashMap);
    }catch(SQLException sql){
      logger.error("[CreditEvaluationService][doEvaluateOrder][SQLException]", sql);
      HashMap hshData=new HashMap();
      hshData.put("strMessage",sql.getMessage());
      return hshData;
    }catch(RemoteException re){
      logger.error("[CreditEvaluationService][doEvaluateOrder][RemoteException]", re);
      HashMap hshData=new HashMap();
      hshData.put("strMessage",re.getMessage());
      return hshData;
    }catch(Exception ex){
      logger.error("[CreditEvaluationService][doEvaluateOrder][Exception]", ex);
      HashMap hshData=new HashMap();
      hshData.put("strMessage",ex.getMessage());
      return hshData;
    }
    logger.info("******************* FIN CreditEvaluationService > doEvaluateOrder *******************");
    return hshResultCreditEvaluation;
  }

  /**
   * EFLORES PM0011359 Se copia el procedimiento para la evaluacion crediticia para conectar con la creacion de la cuota VEP
   * @param objHashMap
   * @return
   * @throws Exception
   * @throws SQLException
   */
  public HashMap doEvaluateOrderCreate(RequestHashMap objHashMap) throws Exception, SQLException{
    try{
      return getSEJBCreditEvaluationRemote().doEvaluateOrder(objHashMap);
    }catch(SQLException sql){
      System.out.println("CreditEvaluationService doEvaluateOrder \nMensaje:" + sql.getMessage()+"\n");
      HashMap hshData=new HashMap();
      hshData.put("strMessage",sql.getMessage());
      return hshData;
    }catch(RemoteException re){
      System.out.println("CreditEvaluationService doEvaluateOrder \nMensaje:" + re.getMessage()+"\n");
      HashMap hshData=new HashMap();
      hshData.put("strMessage",re.getMessage());
      return hshData;
    }catch(Exception ex){
      System.out.println("CreditEvaluationService doEvaluateOrder \nMensaje:" + ex.getMessage()+"\n");
      HashMap hshData=new HashMap();
      hshData.put("strMessage",ex.getMessage());
      return hshData;
    }
  }

  /**
  * <br>Realizado por: <a href="mailto:jose.casas@nextel.com.pe">José Casas</a>
  * <br>Fecha: 30/06/2009
  * @see pe.com.nextel.dao.CreditEvaluationDAO#getRuleResult(long,String)      
  */            
  public HashMap doReasonReject(long lSourceId, String strSource, int iflag, String strCreatedby) throws Exception, SQLException{
    HashMap objHashMap = new HashMap();
    String  strMessage = new String();
    try {
      return getSEJBCreditEvaluationRemote().doReasonReject(lSourceId, strSource, iflag, strCreatedby);
    } catch (SQLException e) {      
      strMessage  = "[SQLException][CreditEvaluationService][doReasonReject][" + e.getClass() + " " + e.getMessage()+"]";      
      objHashMap.put("strMessage",strMessage);
      return objHashMap;
    } catch (RemoteException e) {
      strMessage  = "[RemoteException][CreditEvaluationService][doReasonReject][" + e.getClass() + " " + e.getMessage()+"]";      
      objHashMap.put("strMessage",strMessage);
      return objHashMap;
    } catch (Exception e) {
      strMessage  = "[Exception][CreditEvaluationService][doReasonReject][" + e.getClass() + " " + e.getMessage()+"]";      
      objHashMap.put("strMessage",strMessage);
      return objHashMap;
    }
  }
  
  /**
  * <br>Realizado por: <a href="mailto:jose.casas@nextel.com.pe">José Casas</a>
  * <br>Fecha: 21/07/2009    
  */      
  public HashMap doValidateCredit(long lSourceId,String strSource) throws Exception, SQLException{        
    try{        
      return getSEJBCreditEvaluationRemote().doValidateCredit(lSourceId, strSource);
    }catch(SQLException sql){
      System.out.println("CreditEvaluationService doValidateCredit \nMensaje:" + sql.getMessage()+"\n");
      HashMap hshData=new HashMap();
      hshData.put("strMessage",sql.getMessage());
      return hshData;
    }catch(RemoteException re){
      System.out.println("CreditEvaluationService doValidateCredit \nMensaje:" + re.getMessage()+"\n");
      HashMap hshData=new HashMap();
      hshData.put("strMessage",re.getMessage());
      return hshData;
    }catch(Exception ex){
      System.out.println("CreditEvaluationService doValidateCredit \nMensaje:" + ex.getMessage()+"\n");
      HashMap hshData=new HashMap();
      hshData.put("strMessage",ex.getMessage());
      return hshData;
    }
  }
  
    /**
    * <br>Realizado por:  <a href="rensso.martinez@hp.com">Rensso Martínez</a>
    * <br>Fecha: 10/04/2014    
    */      
    public HashMap getLastCustomerScore(long lnorderid) throws Exception, SQLException{
      try{        
        return getSEJBCreditEvaluationRemote().getLastCustomerScore(lnorderid);
      }catch(SQLException sql){
        System.out.println("CreditEvaluationService getLastCustomerScore \nMensaje:" + sql.getMessage()+"\n");
        HashMap hshData=new HashMap();
        hshData.put("strMessage",sql.getMessage());
        return hshData;
      }catch(RemoteException re){
        System.out.println("CreditEvaluationService getLastCustomerScore \nMensaje:" + re.getMessage()+"\n");
        HashMap hshData=new HashMap();
        hshData.put("strMessage",re.getMessage());
        return hshData;
      }catch(Exception ex){
        System.out.println("CreditEvaluationService getLastCustomerScore \nMensaje:" + ex.getMessage()+"\n");
        HashMap hshData=new HashMap();
        hshData.put("strMessage",ex.getMessage());
        return hshData;
      }
    }
}