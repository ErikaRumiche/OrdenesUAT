package pe.com.nextel.service;

import java.rmi.RemoteException;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.HashMap;

import javax.naming.Context;

import javax.rmi.PortableRemoteObject;

import pe.com.nextel.bean.BaAssignmentBean;
import pe.com.nextel.bean.PortalSessionBean;
import pe.com.nextel.ejb.SEJBBillingAccountRemote;
import pe.com.nextel.ejb.SEJBBillingAccountRemoteHome;
import pe.com.nextel.util.MiUtil;
import pe.com.nextel.util.RequestHashMap;


//import pe.com.nextel.dao.BillingAccountDAO;

public class BillingAccountService {
   public BillingAccountService() {
   }
   
   /***********************************************************************
   ***********************************************************************
   ***********************************************************************
   *  INTEGRACION DE ORDENES Y RETAIL - INICIO
   *  REALIZADO POR: Carmen Gremios
   *  FECHA: 13/09/2007
   ***********************************************************************
   ***********************************************************************
   ***********************************************************************/
   
   /**
   * Motivo: Obtiene la instancia del EJB remoto
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 13/09/2007
   * @return    SEJBBillingAccountRemote
   */    
   public static SEJBBillingAccountRemote getSEJBBillingAccountRemote() {
      try{
         final Context context = MiUtil.getInitialContext();
         final SEJBBillingAccountRemoteHome sEJBBillingAccountRemoteHome = 
         (SEJBBillingAccountRemoteHome) PortableRemoteObject.narrow( context.lookup( "SEJBBillingAccount" ), SEJBBillingAccountRemoteHome.class );
         SEJBBillingAccountRemote sEJBBillingAccountRemote;
         sEJBBillingAccountRemote = sEJBBillingAccountRemoteHome.create();             
         return sEJBBillingAccountRemote;
      }catch(Exception ex) {
         System.out.println("Exception : [BillingAccountService][getSEJBBillingAccountRemote]["+ex.getMessage()+"]");
         return null;
      }   
   }      
   
   
   /**
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * @see pe.com.nextel.dao.BillingAccountDAO#getAccountList(int,String)      
   */     
   public HashMap getAccountList(String strObjectType,long lObjectId,long lOrderId) {          
      HashMap hshMap=new HashMap();
      try{      
         return getSEJBBillingAccountRemote().getAccountList(strObjectType,lObjectId,lOrderId);
      }catch(SQLException sql){         
         System.out.println("GeneralService getRepresentantesCCList \nMensaje:" + sql.getMessage()+"\n Numero ORA: "+sql.getErrorCode()+"\n");
         String strMessage=null;
         if (sql.getErrorCode()==0) hshMap.put("strMessage",strMessage);
         else  hshMap.put("strMessage",sql.getMessage());
         return hshMap;    
      }catch(RemoteException re){
         System.out.println("CustomerService getAccountList \nMensaje:" + re.getMessage()+"\n");         
         hshMap.put("strMessage",re.getMessage());
         return hshMap;        
      }catch(Exception ex){
         System.out.println("CustomerService getAccountList \nMensaje:" + ex.getMessage()+"\n");                     
         hshMap.put("strMessage",ex.getMessage());
         return hshMap;    
      }
   }  
   
   /**
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 20/09/2007
   * @see pe.com.nextel.dao.BillingAccountDAO#insBillAccount(BillingAccountBean)      
   */   
   public HashMap insBillAccount(RequestHashMap objHashMap,PortalSessionBean objPortalSesBean)throws Exception{
      return getSEJBBillingAccountRemote().insBillAccount(objHashMap,objPortalSesBean);
   } 
   
   
   /**
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 21/09/2007
   * @see pe.com.nextel.dao.BillingAccountDAO#updBillAccount(BillingAccountBean)      
   */   
   public HashMap updBillAccount(RequestHashMap objHashMap,PortalSessionBean objPortalSesBean)throws Exception{ 
      return getSEJBBillingAccountRemote().updBillAccount(objHashMap,objPortalSesBean);
   } 
   
   /**
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 21/09/2007
   * @see pe.com.nextel.dao.BillingAccountDAO#delBillAccount(long)      
   */   
   public HashMap delBillAccount(RequestHashMap objHashMap,PortalSessionBean objPortalSesBean)throws Exception{    
      return getSEJBBillingAccountRemote().delBillAccount(objHashMap,objPortalSesBean);
   } 
   
   /**
   * <br>Realizado por: <a href="mailto:hugo.moreno@nextel.com.pe">Hugo Moreno</a>
   * <br>Fecha: 03/04/2008
   * @see pe.com.nextel.dao.BillingAccountDAO#delBillAccountAssign(long)      
   */   
   public HashMap delBillAccountAssign(RequestHashMap objHashMap,PortalSessionBean objPortalSesBean)throws Exception{    
      return getSEJBBillingAccountRemote().delBillAccountAssign(objHashMap,objPortalSesBean);
   } 
   
   /**
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 22/09/2007
   * @see pe.com.nextel.dao.BillingAccountDAO#getContactBillCreateList(long,long)      
   */   
   public HashMap getContactBillCreateList(long lNpcustomerid,long lNpSiteId){ 
      HashMap hshMap=new HashMap();
      try{      
         return getSEJBBillingAccountRemote().getContactBillCreateList(lNpcustomerid,lNpSiteId);
      }catch(SQLException sql){
         System.out.println("BillingAccountService getContactBillCreateList \nMensaje:" + sql.getMessage()+"\n Numero ORA: "+sql.getErrorCode()+"\n");
         String strMessage=null;
         if (sql.getErrorCode()==0) hshMap.put("strMessage",strMessage);
         else  hshMap.put("strMessage",sql.getMessage());
         return hshMap;             
      }catch(RemoteException re){
         System.out.println("BillingAccountService getContactBillCreateList \nMensaje:" + re.getMessage()+"\n");
         hshMap.put("strMessage",re.getMessage());
         return hshMap;            
      }catch(Exception ex){
         System.out.println("BillingAccountService getContactBillCreateList \nMensaje:" + ex.getMessage()+"\n");            
         hshMap.put("strMessage",ex.getMessage());
         return hshMap;        
      }
   } 
   
   /**
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 22/09/2007
   * @see pe.com.nextel.dao.BillingAccountDAO#getNewContactBilling(long,String)      
   */   
   public HashMap getNewContactBilling(long lNpbillaccnewid){ 
      HashMap hshMap=new HashMap();
      try{      
         return getSEJBBillingAccountRemote().getNewContactBilling(lNpbillaccnewid);
      }catch(SQLException sql){
         System.out.println("BillingAccountService getContactBillCreateList \nMensaje:" + sql.getMessage()+"\n Numero ORA: "+sql.getErrorCode()+"\n");
         String strMessage=null;
         if (sql.getErrorCode()==0) hshMap.put("strMessage",strMessage);
         else  hshMap.put("strMessage",sql.getMessage());
         return hshMap;           
      }catch(RemoteException re){
         System.out.println("BillingAccountService getNewContactBilling \nMensaje:" + re.getMessage()+"\n");         
         hshMap.put("strMessage",re.getMessage());
         return hshMap;                 
      }catch(Exception ex){
         System.out.println("BillingAccountService getNewContactBilling \nMensaje:" + ex.getMessage()+"\n");            
         hshMap.put("strMessage",ex.getMessage());
         return hshMap;          
      }
   } 
   
   /**
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 17/10/2007
   * @see pe.com.nextel.dao.BillingAccountDAO#getNewBillAccId()      
   */ 
   public HashMap getNewBillAccId(){
      HashMap hshNewBillAcc=new HashMap();       
      try {   
         String strNewBillAcc=getSEJBBillingAccountRemote().getNewBillAccId()+"";
         hshNewBillAcc.put("strNewBillAcc",strNewBillAcc);
         return hshNewBillAcc;
      }catch (SQLException e) {
         System.out.println("Error [SQLException][BillingAccountService][getNewBillAccId][" + e.getMessage() + "]["+e.getClass()+"]");
         hshNewBillAcc.put("strMessage","Error [SQLException][BillingAccountService][getNewBillAccId][" + e.getMessage() + "]["+e.getClass()+"]");
         return hshNewBillAcc;
      }catch (RemoteException e) {
         System.out.println("Error [RemoteException][BillingAccountService][getNewBillAccId][" + e.getMessage() + "]["+e.getClass()+"]");
         hshNewBillAcc.put("strMessage","Error [RemoteException][BillingAccountService][getNewBillAccId][" + e.getMessage() + "]["+e.getClass()+"]");
         return hshNewBillAcc;
      }catch (Exception e){
         System.out.println("Error [Exception][BillingAccountService][getNewBillAccId][" + e.getMessage() + "]["+e.getClass()+"]");                
         hshNewBillAcc.put("strMessage","Error [Exception][BillingAccountService][getNewBillAccId][" + e.getMessage() + "]["+e.getClass()+"]");
         return hshNewBillAcc;
      }   
   }
   /***********************************************************************
   ***********************************************************************
   ***********************************************************************
   *  INTEGRACION DE ORDENES  - FIN
   *  REALIZADO POR: Carmen Gremios
   *  FECHA: 13/09/2007
   ***********************************************************************
   ***********************************************************************
   ***********************************************************************/   
   
   /**
   * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales Crispin</a>
   * <br>Fecha: 22/09/2007
   * @see pe.com.nextel.dao.BillingAccountDAO#getNewContactBilling(long,String)      
   */   
   public HashMap BillingAccountDAOgetBillingAccountList(long iNpcustomerid){ 
      HashMap objHashMap  = new HashMap();    
      try {   
         return getSEJBBillingAccountRemote().BillingAccountDAOgetBillingAccountList(iNpcustomerid); 
      }catch (SQLException e) {
         System.out.println("[SQLException][BillingAccountService][BillingAccountDAOgetBillingAccountList][" + e.getClass() + " "+e.getMessage()+"]");
         objHashMap.put("strMessage","[SQLException][BillingAccountService][BillingAccountDAOgetBillingAccountList][" + e.getClass() + " "+e.getMessage()+"]");
         return objHashMap;
      }catch (RemoteException e) {
         System.out.println("[RemoteException][BillingAccountService][BillingAccountDAOgetBillingAccountList][" + e.getClass() + " "+e.getMessage()+"]");
         objHashMap.put("strMessage","[RemoteException][BillingAccountService][BillingAccountDAOgetBillingAccountList][" + e.getClass() + " "+e.getMessage()+"]");
         return objHashMap;
      }catch (Exception e){
         System.out.println("[Exception][BillingAccountService][BillingAccountDAOgetBillingAccountList][" + e.getClass() + " "+e.getMessage()+"]");
         objHashMap.put("strMessage","[Exception][BillingAccountService][BillingAccountDAOgetBillingAccountList][" + e.getClass() + " "+e.getMessage()+"]");
         return objHashMap;
      }
   } 
   
 
   
   /**
   * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
   * <br>Fecha: 09/12/2007
   * @see pe.com.nextel.dao.BillingAccountDAO#getBillingAccountListNew(long,long)      
   */   
   public HashMap BillingAccountDAOgetBillingAccountListNew(long iNpcustomerid,long iNpsiteid){ 
      HashMap objHashMap  = new HashMap();    
      try {   
         return getSEJBBillingAccountRemote().BillingAccountDAOgetBillingAccountListNew(iNpcustomerid,iNpsiteid); 
      }catch (SQLException e) {
         System.out.println("[SQLException][BillingAccountService][BillingAccountDAOgetBillingAccountListNew][" + e.getClass() + " "+e.getMessage()+"]");
         objHashMap.put("strMessage","[SQLException][BillingAccountService][BillingAccountDAOgetBillingAccountListNew][" + e.getClass() + " "+e.getMessage()+"]");
         return objHashMap;
      }catch (RemoteException e) {
         System.out.println("[RemoteException][BillingAccountService][BillingAccountDAOgetBillingAccountListNew][" + e.getClass() + " "+e.getMessage()+"]");
         objHashMap.put("strMessage","[RemoteException][BillingAccountService][BillingAccountDAOgetBillingAccountListNew][" + e.getClass() + " "+e.getMessage()+"]");
         return objHashMap;
      }catch (Exception e){
         System.out.println("[Exception][BillingAccountService][BillingAccountDAOgetBillingAccountListNew][" + e.getClass() + " "+e.getMessage()+"]");
         objHashMap.put("strMessage","[Exception][BillingAccountService][BillingAccountDAOgetBillingAccountListNew][" + e.getClass() + " "+e.getMessage()+"]");
         return objHashMap;
      }
   } 
   
   public static String BillingAccountDAOinsertarAssignementAccount(BaAssignmentBean baAssignmentBean,Connection conn){
   
     try {
         //Insertar los Items de Ordenes
             return getSEJBBillingAccountRemote().BillingAccountDAOinsertarAssignementAccount(baAssignmentBean,conn);
     }catch (SQLException e) {
             System.out.println("Error [SQLException][BillingAccountService][BillingAccountDAOinsertarAssignementAccount][" + e.getMessage() + "]["+e.getClass()+"]");
             return "Error [SQLException][BillingAccountService][BillingAccountDAOinsertarAssignementAccount][" + e.getMessage() + "]["+e.getClass()+"]";
     }catch (RemoteException e) {
             System.out.println("Error [RemoteException][BillingAccountService][BillingAccountDAOinsertarAssignementAccount][" + e.getMessage() + "]["+e.getClass()+"]");
             return "Error [RemoteException][BillingAccountService][BillingAccountDAOinsertarAssignementAccount][" + e.getMessage() + "]["+e.getClass()+"]";
     }catch (Exception e){
             System.out.println("Error [Exception][BillingAccountService][BillingAccountDAOinsertarAssignementAccount][" + e.getMessage() + "]["+e.getClass()+"]");
             return "Error [Exception][BillingAccountService][BillingAccountDAOinsertarAssignementAccount][" + e.getMessage() + "]["+e.getClass()+"]";
     }
   
   }
   
  
   public HashMap BillingAccountDAOgetAccountList(long longSiteId, long longCustomerId, long longOrderId){
     HashMap hshNewBillAcc=new HashMap();    
   
     try {   
             return getSEJBBillingAccountRemote().BillingAccountDAOgetAccountList(longSiteId,longCustomerId,longOrderId);
     }catch (SQLException e) {
             System.out.println("Error [SQLException][BillingAccountService][BillingAccountListDAOgetAccountList][" + e.getMessage() + "]["+e.getClass()+"]");
             hshNewBillAcc.put("strMessage","Error [SQLException][BillingAccountService][BillingAccountListDAOgetAccountList][" + e.getMessage() + "]["+e.getClass()+"]");
             return hshNewBillAcc;
     }catch (RemoteException e) {
             System.out.println("Error [RemoteException][BillingAccountService][BillingAccountListDAOgetAccountList][" + e.getMessage() + "]["+e.getClass()+"]");
             hshNewBillAcc.put("strMessage","Error [RemoteException][BillingAccountService][BillingAccountListDAOgetAccountList][" + e.getMessage() + "]["+e.getClass()+"]");
             return hshNewBillAcc;
     }catch (Exception e){
             System.out.println("Error [Exception][BillingAccountService][getNewBillAccId][" + e.getMessage() + "]["+e.getClass()+"]");                
             hshNewBillAcc.put("strMessage","Error [Exception][BillingAccountService][getNewBillAccId][" + e.getMessage() + "]["+e.getClass()+"]");
             return hshNewBillAcc;
     }
   
   }
   
   public HashMap  BillingAccountDAOgetCoAssignmentSiteOrig(String strPhone){
     try {
           return getSEJBBillingAccountRemote().BillingAccountDAOgetCoAssignmentSiteOrig(strPhone);
     }catch (SQLException e) {
           System.out.println("Error [SQLException][BillingAccountService][BillingAccountListDAOgetAccountList][" + e.getMessage() + "]["+e.getClass()+"]");
             
           return (HashMap)(new HashMap()).put("strMessage","Error [SQLException][BillingAccountService][BillingAccountDAOgetCoAssignmentSiteOrig]["+e.getClass() + " " + e.getMessage() + "]");  
     }catch (RemoteException e) {               
           System.out.println("Error [SQLException][BillingAccountService][BillingAccountListDAOgetAccountList][" + e.getMessage() + "]["+e.getClass()+"]");
             
           return (HashMap)(new HashMap()).put("strMessage","Error [RemoteException][BillingAccountService][BillingAccountDAOgetCoAssignmentSiteOrig]["+e.getClass() + " " + e.getMessage() + "]");  
     }catch (Exception e) {
           System.out.println("Error [SQLException][BillingAccountService][BillingAccountListDAOgetAccountList][" + e.getMessage() + "]["+e.getClass()+"]");
             
           return (HashMap)(new HashMap()).put("strMessage","Error [Exception][BillingAccountService][BillingAccountDAOgetCoAssignmentSiteOrig]["+e.getClass() + " " + e.getMessage() + "]");  
     }
   }
   
   
   public HashMap BillingAccountDAOgetCoAssignmentList(long longOrderId)  {
     HashMap hshNewBillAcc=new HashMap();    
     try {   
             return getSEJBBillingAccountRemote().BillingAccountDAOgetCoAssignmentList(longOrderId);
     }catch (SQLException e) {
             System.out.println("Error [SQLException][BillingAccountService][BillingAccountDAOgetCoAssignmentList][" + e.getMessage() + "]["+e.getClass()+"]");
             hshNewBillAcc.put("strMessage","Error [SQLException][BillingAccountService][BillingAccountDAOgetCoAssignmentList][" + e.getMessage() + "]["+e.getClass()+"]");
             return hshNewBillAcc;
     }catch (RemoteException e) {
             System.out.println("Error [RemoteException][BillingAccountService][BillingAccountDAOgetCoAssignmentList][" + e.getMessage() + "]["+e.getClass()+"]");
             hshNewBillAcc.put("strMessage","Error [RemoteException][BillingAccountService][BillingAccountDAOgetCoAssignmentList][" + e.getMessage() + "]["+e.getClass()+"]");
             return hshNewBillAcc;
     }catch (Exception e){
             System.out.println("Error [Exception][BillingAccountService][BillingAccountDAOgetCoAssignmentList][" + e.getMessage() + "]["+e.getClass()+"]");                
             hshNewBillAcc.put("strMessage","Error [Exception][BillingAccountService][BillingAccountDAOgetCoAssignmentList][" + e.getMessage() + "]["+e.getClass()+"]");
             return hshNewBillAcc;
     }
   
   }
   
   public HashMap BillingAccountDAOgetCoAssignmentBillingByContract(long longContractId)  {
     HashMap hshNewBillAcc=new HashMap();    
     try {   
             return getSEJBBillingAccountRemote().BillingAccountDAOgetCoAssignmentBillingByContract(longContractId);
     }catch (SQLException e) {
             System.out.println("[SQLException][BillingAccountService][BillingAccountDAOgetCoAssignmentBillingByContract][" + e.getMessage() + "]["+e.getClass()+"]");
             hshNewBillAcc.put("strMessage","[SQLException][BillingAccountService][BillingAccountDAOgetCoAssignmentBillingByContract][" + e.getMessage() + "]["+e.getClass()+"]");
             return hshNewBillAcc;
     }catch (RemoteException e) {
             System.out.println("[RemoteException][BillingAccountService][BillingAccountDAOgetCoAssignmentBillingByContract][" + e.getMessage() + "]["+e.getClass()+"]");
             hshNewBillAcc.put("strMessage","[RemoteException][BillingAccountService][BillingAccountDAOgetCoAssignmentBillingByContract][" + e.getMessage() + "]["+e.getClass()+"]");
             return hshNewBillAcc;
     }catch (Exception e){
             System.out.println("[Exception][BillingAccountService][BillingAccountDAOgetCoAssignmentBillingByContract][" + e.getMessage() + "]["+e.getClass()+"]");                
             hshNewBillAcc.put("strMessage","[Exception][BillingAccountService][BillingAccountDAOgetCoAssignmentBillingByContract][" + e.getMessage() + "]["+e.getClass()+"]");
             return hshNewBillAcc;
     }
   
   }
  
}
