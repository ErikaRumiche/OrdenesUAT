package pe.com.nextel.service;

import java.io.Serializable;

import java.rmi.RemoteException;

import java.sql.SQLException;

import java.util.HashMap;

import javax.naming.Context;

import javax.rmi.PortableRemoteObject;

import pe.com.nextel.ejb.SEJBMassiveOrderRemote;
import pe.com.nextel.ejb.SEJBMassiveOrderRemoteHome;
import pe.com.nextel.util.MiUtil;


public class MassiveOrderService extends GenericService implements Serializable{

    public static SEJBMassiveOrderRemote getSEJBMassiveOrderRemote() {
         try{
             final Context context = MiUtil.getInitialContext();
             final SEJBMassiveOrderRemoteHome sEJBMassiveOrderRemoteHome = 
                 (SEJBMassiveOrderRemoteHome) PortableRemoteObject.narrow( context.lookup( "SEJBMassiveOrder" ), SEJBMassiveOrderRemoteHome.class );
             SEJBMassiveOrderRemote sEJBMassiveOrderRemote;
             sEJBMassiveOrderRemote = sEJBMassiveOrderRemoteHome.create();
             
             return sEJBMassiveOrderRemote;
         }catch(Exception ex) {
             System.out.println("Exception : [MassiveOrderService][getSEJBMassiveOrderRemote]["+ex.getMessage()+"]");
             return null;
         }
         
    }
     
   public HashMap MassiveOrderDAOgetItemOrder(long npcustomerid,long npsiteid, long npspecification, long npcustomeridAcept){
      HashMap objHashMap = new HashMap();
      String  strMessage = new String();
    
      try {
          return getSEJBMassiveOrderRemote().MassiveOrderDAOgetItemOrder(npcustomerid,npsiteid,npspecification,npcustomeridAcept);
     
           }
        catch (SQLException e) {
                  strMessage  = "[SQLException][NewOrderService][ItemDAOgetItemOrder][" + e.getClass() + " " + e.getMessage()+"]";
                   System.out.println(strMessage);
                   objHashMap.put("strMessage",strMessage);
                   return objHashMap;
                   }
          catch (RemoteException e) {
                strMessage  = "[RemoteException][NewOrderService][ItemDAOgetItemOrder][" + e.getClass() + " " + e.getMessage()+"]";
                System.out.println(strMessage);
                objHashMap.put("strMessage",strMessage);
                return objHashMap;
                }
        catch (Exception e) {
               strMessage  = "[Exception][MassiveOrderService][ItemDAOgetItemOrder][" + e.getClass() + " " + e.getMessage()+"]";
               System.out.println(strMessage);
               objHashMap.put("strMessage",strMessage);
               return objHashMap;
    }
  }
  
   /**
   * <br>Realizado por: <a href="mailto:evelyn.ocampo@nextel.com.pe">Evelyn Ocampo</a>
   * <br>Fecha: 14/07/2009
   * @see pe.com.nextel.dao.MassiveOrderDAO#getServiceList(int)      
   */        
   public HashMap getServiceList(int iDivisionId, int iPlanId){           
      HashMap hshDataMap = new HashMap();
        try {
          return getSEJBMassiveOrderRemote().getServiceList(iDivisionId,iPlanId);
        }catch(Throwable t){
          manageCatch(hshDataMap, t);
        }
      return hshDataMap;
   }
   
  /**
   * <br>Realizado por: <a href="mailto:evelyn.ocampo@nextel.com.pe">Evelyn Ocampo</a>
   * <br>Fecha: 17/07/2009
   * @see pe.com.nextel.dao.MassiveOrderDAO#getServiceListBySolution(int)      
   */        
   public HashMap getServiceListBySolution(int iDivisionId){           
      HashMap hshDataMap = new HashMap();
        try {
          return getSEJBMassiveOrderRemote().getServiceListBySolution(iDivisionId);
        }catch(Throwable t){
          manageCatch(hshDataMap, t);
        }
      return hshDataMap;
   }
   
     /**
   * <br>Realizado por: <a href="mailto:evelyn.ocampo@nextel.com.pe">Evelyn Ocampo</a>
   * <br>Fecha: 21/07/2009
   * @see pe.com.nextel.dao.MassiveOrderDAO#getServiceItemListBySolution(int)      
   */        
   public HashMap getServiceItemListBySolution(int iSolutionId){           
      HashMap hshDataMap = new HashMap();
        try {
          return getSEJBMassiveOrderRemote().getServiceItemListBySolution(iSolutionId);
        }catch(Throwable t){
          manageCatch(hshDataMap, t);
        }
      return hshDataMap;
   }
  
   /**
   * <br>Realizado por: <a href="mailto:henry.rengifo@nextel.com.pe">Henry Rengifo</a>
   * <br>Fecha: 05/08/2009
   * @see pe.com.nextel.dao.MassiveOrderDAO#getCommercialService(int)      
   */   
    public HashMap getCommercialService(long numContract){           
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBMassiveOrderRemote().getCommercialService(numContract);      
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }
  
   /**
   * <br>Realizado por: <a href="mailto:henry.rengifo@nextel.com.pe">Henry Rengifo</a>
   * <br>Fecha: 20/08/2009
   * @see pe.com.nextel.dao.MassiveOrderDAO#getValidateByPhone(long,long,long)      
   */   
    public HashMap getValidateByPhone(long npphone, long intSpecification, long nOrderid){           
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBMassiveOrderRemote().getValidateByPhone(npphone, intSpecification, nOrderid);      
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }
}