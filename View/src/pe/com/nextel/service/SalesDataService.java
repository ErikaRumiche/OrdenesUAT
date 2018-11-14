package pe.com.nextel.service;

import java.rmi.RemoteException;

import java.sql.SQLException;

import java.util.HashMap;

import javax.naming.Context;

import javax.rmi.PortableRemoteObject;

import pe.com.nextel.bean.PortalSessionBean;
import pe.com.nextel.ejb.SEJBSalesDataRemote;
import pe.com.nextel.ejb.SEJBSalesDataRemoteHome;
import pe.com.nextel.util.MiUtil;
import pe.com.nextel.util.RequestHashMap;


public class SalesDataService {

    /**
      * Motivo: Obtiene la instancia del EJB remoto
      * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
      * <br>Fecha: 07/05/2010
      * @return    SEJBSalesDataRemote
     */    
      public static SEJBSalesDataRemote getSEJBSalesDataRemote() {
         try{
             final Context context = MiUtil.getInitialContext();
             final SEJBSalesDataRemoteHome sEJBSalesDataRemoteHome = 
                 (SEJBSalesDataRemoteHome) PortableRemoteObject.narrow( context.lookup( "SEJBSalesData" ), SEJBSalesDataRemoteHome.class );
             SEJBSalesDataRemote sEJBSalesDataRemote;
             sEJBSalesDataRemote = sEJBSalesDataRemoteHome.create();
             
             return sEJBSalesDataRemote;
         }catch(Exception ex) {
             System.out.println("Exception : [SalesDataService][getSEJBSalesDataRemote]["+ex.getMessage()+"]");
             return null;
         }
         
     }
     
       
  /***********************************************************************************************************************************
  * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
  * <br>Motivo: Obtiene los registros de las aplicaciones disponibles para el cliente.
  * <br>Fecha: 06/05/2010
  * @see pe.com.nextel.dao.DataDAO#getAplicationDataList(long,long,long,long)      
  ************************************************************************************************************************************/       
    public HashMap getAplicationDataList (long lngOrderId,long lngDivisionId,long lngCustomerId,long lngSpecificationId) throws Exception,SQLException{
      HashMap objHashMap = new HashMap();    
      try {
            return getSEJBSalesDataRemote().getAplicationDataList(lngOrderId,lngDivisionId,lngCustomerId,lngSpecificationId);
        }catch (SQLException e) {
            System.out.println("Error [SQLException][SalesDataService][getAplicationDataList][" + e.getMessage() + "]["+e.getClass()+"]");
            objHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objHashMap; 
        }
        catch (RemoteException e) {
            System.out.println("Error [RemoteException][SalesDataService][getAplicationDataList][" + e.getMessage() + "]["+e.getClass()+"]");
            objHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objHashMap; 
        }
        catch (Exception e) {
            System.out.println("Error [SQLException][SalesDataService][getAplicationDataList][" + e.getMessage() + "]["+e.getClass()+"]");
             objHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objHashMap;
        }
    }
    
  /***********************************************************************************************************************************
  * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
  * <br>Motivo: Inserta la Aplicación de Ventas Data.
  * <br>Fecha: 06/05/2010
  * @see pe.com.nextel.dao.DataDAO#insSalesData(request,objPortalSesBean)      
  ************************************************************************************************************************************/  
    public HashMap setSalesData(RequestHashMap request,PortalSessionBean objPortalSesBean){
       HashMap objHashMap = new HashMap();
       String  strMessage = new String();
     
        try {
            return getSEJBSalesDataRemote().setSalesData(request, objPortalSesBean);
        }catch (SQLException e) {
            strMessage  = "[SQLException][SalesDataService][setSalesData][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][SalesDataService][setSalesData][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][SalesDataService][setSalesData][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }
    
    
  /***********************************************************************************************************************************
  * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
  * <br>Motivo: Obtiene los registros de las aplicaciones de un cliente.
  * <br>Fecha: 06/05/2010
  * @see pe.com.nextel.dao.DataDAO#getAplicationCustomer(lomg,long,long,String)      
  ************************************************************************************************************************************/       
    public HashMap getAplicationCustomer (long lOrderId,long lngDivisionId,long lngCustomerId,String strStatus) throws Exception,SQLException{
      HashMap objHashMap = new HashMap();    
      try {
            return getSEJBSalesDataRemote().getAplicationCustomer(lOrderId,lngDivisionId,lngCustomerId,strStatus);
        }catch (SQLException e) {
            System.out.println("Error [SQLException][SalesDataService][getAplicationCustomer][" + e.getMessage() + "]["+e.getClass()+"]");
            objHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objHashMap; 
        }
        catch (RemoteException e) {
            System.out.println("Error [RemoteException][SalesDataService][getAplicationCustomer][" + e.getMessage() + "]["+e.getClass()+"]");
            objHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objHashMap; 
        }
        catch (Exception e) {
            System.out.println("Error [SQLException][SalesDataService][getAplicationCustomer][" + e.getMessage() + "]["+e.getClass()+"]");
             objHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objHashMap;
        }
    }
    
    
  /***********************************************************************************************************************************
  * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
  * <br>Motivo: Obtiene el detalle de la aplicación.
  * <br>Fecha: 06/05/2010
  * @see pe.com.nextel.dao.DataDAO#getAplicationDetail(long)      
  ************************************************************************************************************************************/       
    public HashMap getAplicationDetail (long getAplicationDetail) throws Exception,SQLException{
      HashMap objHashMap = new HashMap();    
      try {
            return getSEJBSalesDataRemote().getAplicationDetail(getAplicationDetail);
        }catch (SQLException e) {
            System.out.println("Error [SQLException][SalesDataService][getAplicationDetail][" + e.getMessage() + "]["+e.getClass()+"]");
            objHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objHashMap; 
        }
        catch (RemoteException e) {
            System.out.println("Error [RemoteException][SalesDataService][getAplicationDetail][" + e.getMessage() + "]["+e.getClass()+"]");
            objHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objHashMap; 
        }
        catch (Exception e) {
            System.out.println("Error [SQLException][SalesDataService][getAplicationDetail][" + e.getMessage() + "]["+e.getClass()+"]");
             objHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objHashMap;
        }
    }
    
    
   /***********************************************************************************************************************************
  * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
  * <br>Motivo: Obtiene el detalle de la aplicación.
  * <br>Fecha: 06/05/2010
  * @see pe.com.nextel.dao.DataDAO#getAplicationDetail(long)      
  ************************************************************************************************************************************/ 
  public HashMap delSalesData(RequestHashMap request)throws Exception{         
       HashMap objHashMap = new HashMap();    
      try {
            return getSEJBSalesDataRemote().delSalesData(request);
        }catch (SQLException e) {
            System.out.println("Error [SQLException][SalesDataService][delSalesData][" + e.getMessage() + "]["+e.getClass()+"]");
            objHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objHashMap; 
        }
        catch (RemoteException e) {
            System.out.println("Error [RemoteException][SalesDataService][delSalesData][" + e.getMessage() + "]["+e.getClass()+"]");
            objHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objHashMap; 
        }
        catch (Exception e) {
            System.out.println("Error [SQLException][SalesDataService][delSalesData][" + e.getMessage() + "]["+e.getClass()+"]");
             objHashMap.put("wv_message",e.getClass()+" "+e.getMessage());
            return objHashMap;
        }
    
    
  }  




}