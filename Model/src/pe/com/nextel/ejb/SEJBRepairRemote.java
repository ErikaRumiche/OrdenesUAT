package pe.com.nextel.ejb;

import java.rmi.RemoteException;

import java.sql.SQLException;

import java.util.HashMap;

import javax.ejb.EJBObject;


public interface SEJBRepairRemote extends EJBObject 
{

	/***********************************************************************
	 *  REPARACIONES - INICIO
	 *  REALIZADO POR: Julio Herrera
	 *  FECHA: 12/11/2010
	 **********************************************************************/ 
  //HashMap getOrderHistory(String strOrderID) throws SQLException, Exception, RemoteException;
  //HashMap getImeiModelTab(RepairBean objRepairBean) throws RemoteException, Exception;
  HashMap generateNacionalizacion(HashMap hshParameters) throws SQLException, Exception, RemoteException;
  HashMap generateRAFile(HashMap hshParameters) throws SQLException, Exception, RemoteException;
  HashMap getBscsModelId(String modelName) throws SQLException, Exception, RemoteException;
  HashMap existsImei(String imei) throws SQLException, Exception, RemoteException;  
  HashMap registerEquipmentNew(String imei, String numSerie, String modelId) throws Exception, RemoteException;
  // MMONTOYA - Despacho en tienda
  HashMap getModels(long orderId) throws SQLException, Exception, RemoteException;
  // YRUIZ - Generación de Orden de Soporte SSTT
  String getPlanType(String strPhone, String strImei) throws SQLException, Exception;
    
     /**
      * Motivo: Obtiene una lista de los centros de Reparaciones
      * <br>Realizado por: <a href="mailto:paolo.ortega@teamsoft.com.pe">Paolo Ortega Ramirez</a>
      * <br>Fecha: 20/05/2014
      * @return    HashMap
      */
    public HashMap getCentroReparacion() throws SQLException, Exception;
    
    /**
     * Motivo: Obtiene la cantidad de reparaciones
     * <br>Realizado por: <a href="mailto:carlos.delossantos@teamsoft.com.pe">Carlos De los santos</a>
     * <br>Fecha: 27/05/2014
     * @return    Int
     */
    public int getCantReparaciones( String strIMEI) throws SQLException, Exception;
    
    public HashMap getOtherFailureList(int intRepairId, String strRepairTypeId) throws SQLException, RemoteException, Exception;
    
    HashMap getSelectedOtherFailureList(int intRepairId, String strRepairTypeId) throws SQLException, RemoteException, Exception;
    
}