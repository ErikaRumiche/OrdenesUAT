package pe.com.nextel.service;

import java.rmi.RemoteException;

import java.sql.SQLException;

import java.util.HashMap;

import javax.naming.Context;

import javax.rmi.PortableRemoteObject;

import pe.com.nextel.ejb.SEJBAutomatizacionRemote;
import pe.com.nextel.ejb.SEJBAutomatizacionRemoteHome;
import pe.com.nextel.util.MiUtil;


public class AutomatizacionService extends GenericService {

      /**
      * Motivo: Obtiene la instancia del EJB remoto
      * <br>Realizado por: <a href="mailto:fanny.najarro@nextel.com.pe">Fanny Najarro</a>
      * <br>Fecha: 02/06/2009
      * @return    SEJBAutomatizacionRemote
      */    
      public static SEJBAutomatizacionRemote getSEJBAutomatizacionRemote() {
         try{
            final Context context = MiUtil.getInitialContext();
            final SEJBAutomatizacionRemoteHome sEJBAutomatizacionRemoteHome = 
               (SEJBAutomatizacionRemoteHome) PortableRemoteObject.narrow( context.lookup( "SEJBAutomatizacion" ), SEJBAutomatizacionRemoteHome.class );
            SEJBAutomatizacionRemote sEJBAutomatizacionRemote;
            sEJBAutomatizacionRemote = sEJBAutomatizacionRemoteHome.create();             
            return sEJBAutomatizacionRemote;
         }catch(Exception ex) {
            System.out.println("Exception : [AutomatizacionService][getSEJBAutomatizacionRemote]["+ex.getMessage()+"]");
            return null;
         }
      }
      
      /**
      * Motivo: Clase Service, muestra el boton Reemplazar Equipos.
      * <br>Realizado por: <a href="mailto:fanny.najarro@nextel.com.pe">Fanny Najarro</a>
      * <br>Fecha: 02/06/2009
      * @see pe.com.nextel.dao.AutomatizacionDAO#doShowButtonReplaceHandset(long lOrderId,String strLogin)
      * 
      */    
       public HashMap doShowButtonReplaceHandset(long lOrderId,String strLogin)      
       throws Exception {      
          try{
             return getSEJBAutomatizacionRemote().doShowButtonReplaceHandset(lOrderId,strLogin);
          }catch(RemoteException re){
             System.out.println("AutomatizacionService doShowButtonReplaceHandset \nMensaje:" + re.getMessage()+"\n");
             HashMap shMap=new HashMap();
             shMap.put("strMessage",re.getMessage());
             return shMap;                    
          }catch(SQLException ex){
             System.out.println("AutomatizacionService doShowButtonReplaceHandset \nMensaje:" + ex.getMessage()+"\n");
             HashMap shMap=new HashMap();
             shMap.put("strMessage",ex.getMessage());
             return shMap;         
          }
          catch(Exception ex){
             System.out.println("AutomatizacionService doShowButtonReplaceHandset \nMensaje:" + ex.getMessage()+"\n");
             HashMap shMap=new HashMap();
             shMap.put("strMessage",ex.getMessage());
             return shMap;         
          }         
       }
       
      /**
      * Motivo: Clase Service, muestra la ventana de Error de Operación del ImieSim.
      * <br>Realizado por: <a href="mailto:fanny.najarro@nextel.com.pe">Fanny Najarro</a>
      * <br>Fecha: 04/06/2009
      * @see pe.com.nextel.dao.AutomatizacionDAO#doErrorOperationImeiSim(long lOrderId, long lItemId, long lItemDeviceId)
      * 
      */    
       public HashMap doErrorOperationImeiSim(long lOrderId, long lItemId, long lItemDeviceId)     
       throws Exception {      
          try{
             return getSEJBAutomatizacionRemote().doErrorOperationImeiSim(lOrderId,lItemId,lItemDeviceId);
          }catch(RemoteException re){
             System.out.println("AutomatizacionService doErrorOperationImeiSim \nMensaje:" + re.getMessage()+"\n");
             HashMap shMap=new HashMap();
             shMap.put("strMessage",re.getMessage());
             return shMap;                    
          }catch(SQLException ex){
             System.out.println("AutomatizacionService doErrorOperationImeiSim \nMensaje:" + ex.getMessage()+"\n");
             HashMap shMap=new HashMap();
             shMap.put("strMessage",ex.getMessage());
             return shMap;         
          }
          catch(Exception ex){
             System.out.println("AutomatizacionService doErrorOperationImeiSim \nMensaje:" + ex.getMessage()+"\n");
             HashMap shMap=new HashMap();
             shMap.put("strMessage",ex.getMessage());
             return shMap;         
          }         
       }
       
      /**
      * Motivo: Clase Service, el Mensaje de la ejecucion Reemplaza Equipo.
      * <br>Realizado por: <a href="mailto:fanny.najarro@nextel.com.pe">Fanny Najarro</a>
      * <br>Fecha: 08/06/2009
      * @see pe.com.nextel.dao.AutomatizacionDAO#doReplaceHandset(long lOrderId, String strLogin, String strCodeAplic)
      * 
      */
       public HashMap doReplaceHandset(long lOrderId, String strLogin, String strCodeAplic)     
       throws Exception {      
          try{
             return getSEJBAutomatizacionRemote().doReplaceHandset(lOrderId,strLogin,strCodeAplic);
          }catch(RemoteException re){
             System.out.println("AutomatizacionService doReplaceHandset \nMensaje:" + re.getMessage()+"\n");
             HashMap shMap=new HashMap();
             shMap.put("strMessage",re.getMessage());
             return shMap;                    
          }catch(SQLException ex){
             System.out.println("AutomatizacionService doReplaceHandset \nMensaje:" + ex.getMessage()+"\n");
             HashMap shMap=new HashMap();
             shMap.put("strMessage",ex.getMessage());
             return shMap;         
          }
          catch(Exception ex){
             System.out.println("AutomatizacionService doReplaceHandset \nMensaje:" + ex.getMessage()+"\n");
             HashMap shMap=new HashMap();
             shMap.put("strMessage",ex.getMessage());
             return shMap;         
          }         
       }
       
      /**
      * Motivo: Clase Service, Obtiene las categorias e inbox de la orden y de la np_table.
      * <br>Realizado por: <a href="mailto:fanny.najarro@nextel.com.pe">Fanny Najarro</a>
      * <br>Fecha: 08/06/2009
      * @see pe.com.nextel.dao.AutomatizacionDAO#getOrderSpecificationInbox(long lOrderId)
      * 
      */    
       public HashMap getOrderSpecificationInbox(long lOrderId)     
       throws Exception {      
          try{
             return getSEJBAutomatizacionRemote().getOrderSpecificationInbox(lOrderId);
          }catch(RemoteException re){
             System.out.println("AutomatizacionService getOrderSpecificationInbox \nMensaje:" + re.getMessage()+"\n");
             HashMap shMap=new HashMap();
             shMap.put("strMessage",re.getMessage());
             return shMap;                    
          }catch(SQLException ex){
             System.out.println("AutomatizacionService getOrderSpecificationInbox \nMensaje:" + ex.getMessage()+"\n");
             HashMap shMap=new HashMap();
             shMap.put("strMessage",ex.getMessage());
             return shMap;         
          }
          catch(Exception ex){
             System.out.println("AutomatizacionService getOrderSpecificationInbox \nMensaje:" + ex.getMessage()+"\n");
             HashMap shMap=new HashMap();
             shMap.put("strMessage",ex.getMessage());
             return shMap;         
          }         
       }
       
       
      /**
      * Motivo: Clase Service, Obtiene el estado del numero de Telefono
      * <br>Realizado por: <a href="mailto:fanny.najarro@nextel.com.pe">Fanny Najarro</a>
      * <br>Fecha: 09/07/2009
      * @see pe.com.nextel.dao.AutomatizacionDAO#getStatusNumber(String strCodApp, String strPhoneNumber)   
      * 
      */    
       public HashMap getStatusNumber(String strCodApp, String strPhoneNumber)     
       throws Exception {      
          try{
             return getSEJBAutomatizacionRemote().getStatusNumber(strCodApp, strPhoneNumber);
          }catch(RemoteException re){
             System.out.println("AutomatizacionService getStatusNumber \nMensaje:" + re.getMessage()+"\n");
             HashMap shMap=new HashMap();
             shMap.put("strMessage",re.getMessage());
             return shMap;                    
          }catch(SQLException ex){
             System.out.println("AutomatizacionService getStatusNumber \nMensaje:" + ex.getMessage()+"\n");
             HashMap shMap=new HashMap();
             shMap.put("strMessage",ex.getMessage());
             return shMap;         
          }
          catch(Exception ex){
             System.out.println("AutomatizacionService getStatusNumber \nMensaje:" + ex.getMessage()+"\n");
             HashMap shMap=new HashMap();
             shMap.put("strMessage",ex.getMessage());
             return shMap;         
          }         
       }
       
      /**
      * Motivo: Clase Service, actualiza el numero de Telefono en la np_item_device
      * <br>Realizado por: <a href="mailto:fanny.najarro@nextel.com.pe">Fanny Najarro</a>
      * <br>Fecha: 19/07/2009
      * @see pe.com.nextel.dao.AutomatizacionDAO#doUpdatePhoneItemDevice(String strCodApp, String strPhoneNumber)   
      * 
      */
       public static String doUpdatePhoneItemDevice(long lOrderId, long lItemId, long lItemDeviceId, String phoneNumber){
       
         try {
              
                  return getSEJBAutomatizacionRemote().doUpdatePhoneItemDevice(lOrderId,lItemId,lItemDeviceId,phoneNumber);
          }catch (Exception e) {
                  System.out.println("Error [SQLException][AutomatizacionService][doUpdatePhoneItemDevice][" + e.getMessage() + "]["+e.getClass()+"]");
                  return "Error [SQLException][AutomatizacionService][doUpdatePhoneItemDevice][" + e.getMessage() + "]["+e.getClass()+"]";
          }
    
       }  
       
       
}