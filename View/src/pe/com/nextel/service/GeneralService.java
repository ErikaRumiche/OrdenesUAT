/*
 * Created on 10/09/2007
 * Copyright (c) Nextel del Peru S.A.
 */
package pe.com.nextel.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.*;

import javax.naming.Context;
import javax.rmi.PortableRemoteObject;

import pe.com.nextel.bean.*;
import pe.com.nextel.ejb.SEJBGeneralRemote;
import pe.com.nextel.ejb.SEJBGeneralRemoteHome;
import pe.com.nextel.exception.CustomException;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;
import pe.com.nextel.util.RequestHashMap;


/**
 * Motivo:  Clase Service que contendrá lógica de negocio común,
 *          así como el acceso a los DAOs compartidos.
 * <br>
 * Log de Cambios:<br>
 * 12/02/2008	lrosales     Creación<br>*
 * @version 	1.00
 * @author 	Nextel del Peru S.A – Lee Rosales
 * @see 	pe.com.nextel.service.GenericService
 */ 

public class GeneralService extends GenericService {

  public static SEJBGeneralRemote getSEJBGeneralRemote() {
    try{
       final Context context = MiUtil.getInitialContext();
       final SEJBGeneralRemoteHome sEJBGeneralRemoteHome = 
           (SEJBGeneralRemoteHome) PortableRemoteObject.narrow( context.lookup( "SEJBGeneral" ), SEJBGeneralRemoteHome.class );
       SEJBGeneralRemote sEJBGeneralRemote;
       sEJBGeneralRemote = sEJBGeneralRemoteHome.create();
       
       return sEJBGeneralRemote;
    }catch(Exception ex) {
       System.out.println("Exception : [GeneralService][getSEJBGeneralRemote]["+ex.getMessage()+"]");
       return null;
    }    
  }
  
                           
	public HashMap getComboRegionList() {
    HashMap hshDataMap = new HashMap();
      try {
        //hshDataMap = objGeneralDAO.getComboRegionList();
        hshDataMap = getSEJBGeneralRemote().getComboRegionList();
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
	}
  
  public HashMap GeneralDAOgetComboList(String av_datatable){
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().GeneralDAOgetComboList(av_datatable);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }
/**
 * Incluir aqui la descripción de la clase. Puede hacer uso de tags HTML<br>
 * <br>
 * Log de Cambios:<br>
 * 05/03/2006	lrosales     Creación<br>*   
 * @version 	1.00
 * @author 	  Nextel del Peru S.A – Lee Rosales
 * @see 	    pe.com.nextel.dao.GeneralDAO#getDealerBySalesman() HashMap
 */ 

  public HashMap getDealerBySalesman(long lngSalesmanId){
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getDealerBySalesman(lngSalesmanId);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }   
  
  public String GeneralDAOgetWorldNumber(String strPhone,String strType){
    try {
      return getSEJBGeneralRemote().GeneralDAOgetWorldNumber(strPhone,strType);
    }catch(SQLException e){
      System.out.println("[GeneralService][GeneralDAOgetWorldNumber][" + e.getClass() + " " + e.getMessage()+"]");
      return "[GeneralService][GeneralDAOgetWorldNumber][" + e.getClass() + " " + e.getMessage()+"]"; 
    }catch(RemoteException e){
      System.out.println("[GeneralService][GeneralDAOgetWorldNumber][" + e.getClass() + " " + e.getMessage()+"]");
      return "[GeneralService][GeneralDAOgetWorldNumber][" + e.getClass() + " " + e.getMessage()+"]"; 
    }catch(Exception e){
      System.out.println("[GeneralService][GeneralDAOgetWorldNumber][" + e.getClass() + " " + e.getMessage()+"]");
      return "[GeneralService][GeneralDAOgetWorldNumber][" + e.getClass() + " " + e.getMessage()+"]"; 
    }
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
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 25/07/2007
   * @see pe.com.nextel.dao.GeneralDAO#getTableList(String,String,String)      
   */      
  public HashMap getTableList(String strParamName, String strParamStatus) { //DETALLE ORDEN
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getTableList(strParamName,strParamStatus);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }
   
   
   /**
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 11/07/2007
   * @see pe.com.nextel.dao.GeneralDAO#getSalesList(int,int,String)      
   */        
  public HashMap getSalesList(long iUserId, int iAppId){ //DETALLE ORDEN        
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getSalesList(iUserId,iAppId);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
   }
   
   /**
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 11/07/2007
   * @see pe.com.nextel.dao.UsuarioDAO#getRol(int,int,String)      
   */    
   public HashMap getRol(int iScreenoptionid, long iUserid, int iAppid) {    // CABERA - RESUME, DETALLE ORDEN
    HashMap hshDataMap = new HashMap();
      try {
        int i=getSEJBGeneralRemote().getRol(iScreenoptionid, iUserid, iAppid);
        hshDataMap.put("iRetorno",i+"");
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
   }     
       
   /**
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 11/07/2007
   * @see pe.com.nextel.dao.UsuarioDAO#getRol(int,int,String)      
   */        
  public HashMap getRol2(int iScreenoptionid, int iLevel, String strCode) {    // CABERA - RESUME, DETALLE ORDEN
    HashMap hshDataMap = new HashMap();
      try {
        int i =getSEJBGeneralRemote().getRol(iScreenoptionid, iLevel, strCode); 
        hshDataMap.put("iRetorno",i+"");
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }
       
   /**
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 16/09/2007
   * @see pe.com.nextel.dao.GeneralDAO#getRepresentantesCCList(String)      
   */        
  public HashMap getRepresentantesCCList() {  
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getRepresentantesCCList(); 
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap; 
  }
       
   /**
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 18/09/2007
   * @see pe.com.nextel.dao.GeneralDAO#getDistDeparProvList(String)      
   */        
  public HashMap getDistDeparProvList() {           
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getDistDeparProvList();
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap; 
  }  
       
   /**
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 18/09/2007
   * @see pe.com.nextel.dao.GeneralDAO#getTitleList(String)      
   */        
  public HashMap getTitleList( ) {           
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getTitleList();
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap; 
  }
       
 /**
  * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
  * <br>Fecha: 19/09/2007
  * @see pe.com.nextel.dao.GeneralDAO#getUbigeoList(String,String,String,String)      
  */
  public HashMap getUbigeoList(String strDptoId,String strProvId,String strFlag ){
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getUbigeoList(strDptoId,strProvId,strFlag);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap; 
  }
       
  /**
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 20/09/2007
   * @see pe.com.nextel.dao.GeneralDAO#getAreaCodeList(String,String,int,String)      
   */        
  public HashMap getAreaCodeList(String strAreaName,String strAreaCode){           
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getAreaCodeList(strAreaName,strAreaCode);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap; 
  }   
    
  /**
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 24/09/2007
   * @see pe.com.nextel.dao.CategoryDAO#getSpecificationData(long,String)      
   */        
  public HashMap getSpecificationDetail(long lSolutionId) {           
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getSpecificationDetail(lSolutionId);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap; 
   }   
       
  /**
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 28/09/2007
   * @see pe.com.nextel.dao.GeneralDAO#getRegionList(long,String)      
   */        
  public HashMap getRegionList() {
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getRegionList();
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap; 
  }   
       
  /**
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 01/10/2007
   * @see pe.com.nextel.dao.GeneralDAO#getDepartmentName(long)      
   */        
   public String getDepartmentName(String strCode){          
      String strMsgError=null;
      try{            
         return getSEJBGeneralRemote().getDepartmentName(strCode);
      }catch(RemoteException re){
         System.out.println("GeneralService getDepartmentName \nMensaje:" + re.getMessage()+"\n");
         strMsgError =  re.getMessage();    
         return strMsgError; 
      }catch(Exception ex){
         System.out.println("GeneralService getDepartmentName \nMensaje:" + ex.getMessage()+"\n");            
         strMsgError =  ex.getMessage();             
         return strMsgError; 
      }
   }   
       
   /**
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 01/10/2007
   * @see pe.com.nextel.dao.GeneralDAO#getDepartmentName(long)      
   */        
   public String getRegionName(long lRegionId){          
      String strMsgError=null;
      try{            
         return getSEJBGeneralRemote().getRegionName(lRegionId);
      }catch(RemoteException re){
         System.out.println("GeneralService getRegionName \nMensaje:" + re.getMessage()+"\n");
         strMsgError =  re.getMessage();    
         return strMsgError; 
      }catch(Exception ex){
         System.out.println("GeneralService getRegionName \nMensaje:" + ex.getMessage()+"\n");            
         strMsgError =  ex.getMessage();             
         return strMsgError; 
      }
   }
   
   /**
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 19/10/2007
   * @see pe.com.nextel.dao.GeneralDAO#getUbigeoList(int,int,String)      
   */        
  public HashMap getUbigeoList(int iDptoId,int iProvId,String strFlag){           
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getUbigeoList(iDptoId,iProvId,strFlag);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
    return hshDataMap; 
  }    
   
  /**
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 08/11/2007
   * @see pe.com.nextel.dao.UsuarioDAO#getRol(long)      
   */        
  public HashMap getCustomerValue(long lCustomerId) {    // CABERA - RESUME, DETALLE ORDEN
    HashMap hshDataMap = new HashMap();
      try {
        int i =getSEJBGeneralRemote().getCustomerValue(lCustomerId); 
        hshDataMap.put("iRetorno",i+"");
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
    return hshDataMap; 
  }

  /**
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 24/11/2007
   * @see pe.com.nextel.dao.GeneralDAO#getCountCases(String,String)      
   */        
  public HashMap getCountCases(String strType, String strValue)  {    // CABERA - RESUME, DETALLE ORDEN
    HashMap hshDataMap = new HashMap();
      try {
        int i =getSEJBGeneralRemote().getCountCases(strType,strValue); 
        hshDataMap.put("iRetorno",i+"");
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
    return hshDataMap; 
  }


 /**
  * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
  * <br>Fecha: 26/11/2007
  * @see pe.com.nextel.dao.GeneralDAO#generateOrderExterna(RepairBean)      
  */        
  public HashMap generateOrderExterna(RepairBean objRepairBean,String strLogin) {          
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().generateOrderExterna(objRepairBean,strLogin);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
    return hshDataMap; 
  }   

  /**
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 26/11/2007
   * @see pe.com.nextel.dao.GeneralDAO#generateOrderExterna(RepairBean)      
   */        
  public HashMap generateOrderInterna(RepairBean objRepairBean,String strLogin) {          
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().generateOrderInterna(objRepairBean,strLogin); 
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
    return hshDataMap; 
  } 

  /**
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 26/11/2007
   * @see pe.com.nextel.dao.GeneralDAO#getImeiDetail(RepairBean)      
   */
   public HashMap getImeiDetail(RepairBean objRepairBean) {          
      HashMap shMap=new HashMap();       
      try{
         return getSEJBGeneralRemote().getImeiDetail(objRepairBean); 
      }catch(SQLException sql){
         System.out.println("GeneralService getImeiDetail SQL Exception \nMensaje:" + sql.getMessage()+"\n Numero ORA: "+sql.getErrorCode()+"\n");
         String strMessage=null;
         if (sql.getErrorCode()==0) shMap.put("strMessage",strMessage);
         else  shMap.put("strMessage",sql.getMessage());
         return shMap;                            
      }catch(RemoteException re){
         System.out.println("GeneralService getImeiDetail Remote \nMensaje:" + re.getMessage()+"\n");
         shMap.put("strMessage",re.getMessage());
         return shMap; 
      }catch(Exception ex){
         System.out.println("GeneralService getImeiDetail Exception \nMensaje:" + ex.getMessage()+"\n");            
         shMap.put("strMessage",ex.getMessage());
         return shMap;   
      }
   }    

  /**
   * <br>Realizado por: <a href="mailto:oliver.dubock@nextel.com.pe">Oliver Dubock</a>
   * <br>Fecha: 14/05/2008
   * @see pe.com.nextel.dao.GeneralDAO#getImeiDetailTab(RepairBean)      
   */        
  public HashMap getImeiDetailTab(RepairBean objRepairBean) {          
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getImeiDetailTab(objRepairBean);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
    return hshDataMap; 
  } 

   /**
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 26/11/2007
   * @see pe.com.nextel.dao.GeneralDAO#getImeiDetail(RepairBean)      
   */        
  public HashMap getFechaEmision(String strImei) {          
    HashMap hshDataMap = new HashMap();
      try {
        String strValor = getSEJBGeneralRemote().getFechaEmision(strImei); 
        hshDataMap.put("strFechaEmision",strValor);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
    return hshDataMap; 
  }

	/**
   * @see pe.com.nextel.dao.GeneralDAO#getGeneralOptionList(String)
   */
  public HashMap getGeneralOptionList(String strGeneralOption,long lValue){ 
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getGeneralOptionList(strGeneralOption,lValue);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
    return hshDataMap; 
  } 
	
   /**
   * Motivo:  Obtiene el listado de Repuestos de una Reparacion
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 27/11/2007
     * @param     long
     * @param     String     
     * @param     String     
     * @return		ArrayList    
   */   
  public HashMap getRepuestoDetail(long lObjectId,String strObjectType,String strLogin){ 
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getRepuestoDetail(lObjectId,strObjectType,strLogin);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
    return hshDataMap; 
  }

   /**
   * Motivo: Obtiene el nombre de la tienda dado el buildingid
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 07/02/2008
     * @param     int iBuildingid
     * @return		String    
   */   
  public HashMap getBuildingName(int iBuildingid){
    HashMap hshDataMap = new HashMap();
      try {
        String strBuildingName=getSEJBGeneralRemote().getBuildingName(iBuildingid);
        hshDataMap.put("strBuldingName",strBuildingName);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
    return hshDataMap;
  }   


    /**
     * Motivo: Devuelve parametro para validar Tipo de Operación
     * <br>Realizado por: <a href="mailto:orlando.cruces@hp.com">Orlando Cruces</a>
     * <br>Fecha: 23/03/2015
     * @param     String npconfigurationtype
     * @return	  String
     */
    public HashMap getValidateTypOpe(String npconfigurationtype){
        HashMap hshDataMap = new HashMap();
        try {
            String strValidTypOpe=getSEJBGeneralRemote().getValidateTypOpe(npconfigurationtype);
            hshDataMap.put("strValidTypOpe",strValidTypOpe);
        }catch(Throwable t){
            manageCatch(hshDataMap, t);
        }
        return hshDataMap;
    }

    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - FIN
     *  REALIZADO POR: Carmen Gremios
     *  FECHA: 13/09/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/
  
  /***********************************************************************
   ***********************************************************************
   ***********************************************************************
   *  INTEGRACION DE ORDENES Y RETAIL - INICIO
   *  REALIZADO POR: Richard De los Reyes
   *  FECHA: 28/09/2007
   ***********************************************************************
   ***********************************************************************
   ***********************************************************************/
  /**
   * @see pe.com.nextel.dao.GeneralDAO#getDescriptionByValue(String, String)
   */
  public HashMap getDescriptionByValue(String strValue, String strTable) {
    HashMap hshDataMap = new HashMap();
      try {
        //hshDataMap = objGeneralDAO.getDescriptionByValue(strValue, strTable);
        hshDataMap = getSEJBGeneralRemote().getDescriptionByValue(strValue, strTable);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
      return hshDataMap;
  }
  
  /**
   * @see pe.com.nextel.dao.GeneralDAO#getDominioList(String)
   */
  public HashMap getDominioList(String dominioTabla) {
    HashMap hshDataMap = new HashMap();
      try {
        //hshDataMap = objGeneralDAO.getDominioList(dominioTabla);
        hshDataMap = getSEJBGeneralRemote().getDominioList(dominioTabla);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
    return hshDataMap;
  }
  
  /**
   * @see pe.com.nextel.dao.GeneralDAO#getRepairConfiguration(String)
   */
  public HashMap getRepairConfiguration(String av_param) {
    HashMap hshDataMap = new HashMap();
      try {
        //hshDataMap = objGeneralDAO.getDominioList(dominioTabla);
        hshDataMap = getSEJBGeneralRemote().getRepairConfiguration(av_param);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
    return hshDataMap;
  }

  /**
   * @see pe.com.nextel.dao.GeneralDAO#getRatesPlans()
   */
  public HashMap getRatePlanList() {
    HashMap hshDataMap = new HashMap();
      try {
        //hshDataMap = objGeneralDAO.getRatePlanList();
        hshDataMap = getSEJBGeneralRemote().getRatePlanList();
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
    return hshDataMap;
  }

  /**
   * @see pe.com.nextel.dao.GeneralDAO#getUbigeoList(UbigeoBean)
   */
  public HashMap getUbigeoList(UbigeoBean objUbigeoBean) {
    HashMap hshDataMap = new HashMap();
      try {
        //hshDataMap = objGeneralDAO.getUbigeoList(objUbigeoBean);
        hshDataMap = getSEJBGeneralRemote().getUbigeoList(objUbigeoBean);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }

  /**
   * @see pe.com.nextel.dao.GeneralDAO#getGirosList()
   */
  public HashMap getGiroList() {
    HashMap hshDataMap = new HashMap();
      try {
        //hshDataMap = objGeneralDAO.getGiroList();
        hshDataMap = getSEJBGeneralRemote().getGiroList();
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
    return hshDataMap;
  }

 /**
  * @see pe.com.nextel.dao.GeneralDAO#getSubGirosByGiroList(long)
  */
  public HashMap getSubGirosByGiroList(long lGiroId) {
    HashMap hshDataMap = new HashMap();
      try{
        //hshDataMap = objGeneralDAO.getSubGirosByGiroList(lGiroId);
        hshDataMap = getSEJBGeneralRemote().getSubGirosByGiroList(lGiroId);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
    return hshDataMap;
  }

  /**
   * @see pe.com.nextel.dao.GeneralDAO#getKitsList()
   */
  public HashMap getKitsList(String strTiendaRetail) {
    HashMap hshDataMap = new HashMap();
      try{
        //hshDataMap = objGeneralDAO.getKitsList();
        hshDataMap = getSEJBGeneralRemote().getKitsList(strTiendaRetail);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
    return hshDataMap;
  }

	/**
     * @see pe.com.nextel.dao.GeneralDAO#getComboReparacionList(String)
     */
  public HashMap getComboReparacionList(String strNombreOpcion) {
    HashMap hshDataMap = new HashMap();
      try {
        //hshDataMap = objGeneralDAO.getComboReparacionList(strNombreOpcion);
        hshDataMap = getSEJBGeneralRemote().getComboReparacionList(strNombreOpcion);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
    return hshDataMap;
  }
	
	/**
   * @see pe.com.nextel.dao.GeneralDAO#getEstadoOrdenList()
   */
  public HashMap getEstadoOrdenList() {
    HashMap hshDataMap = new HashMap();
      try {
        //hshDataMap = objGeneralDAO.getEstadoOrdenList();
        hshDataMap = getSEJBGeneralRemote().getEstadoOrdenList();
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
    return hshDataMap;
  }
	
	/**
   * @see pe.com.nextel.dao.GeneralDAO#getDivisionList()
   */
  public HashMap getDivisionList() {
    HashMap hshDataMap = new HashMap();
      try {
        //hshDataMap = objGeneralDAO.getDivisionList();
        hshDataMap = getSEJBGeneralRemote().getDivisionList();
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
    return hshDataMap;
  }
	
	/**
   * @see pe.com.nextel.dao.GeneralDAO#getSolucionList()
   */
  public HashMap getSolucionList(long lDivisionId) {
    HashMap hshDataMap = new HashMap();
      try{
        //hshDataMap = objGeneralDAO.getSolucionList(lDivisionId);
        hshDataMap = getSEJBGeneralRemote().getSolucionList(lDivisionId);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
    return hshDataMap;
  }
	
	/**
   * @see pe.com.nextel.dao.GeneralDAO#getCategoryList()
   */
  public HashMap getCategoryList(long lSolutionId) {
    HashMap hshDataMap = new HashMap();
      try{
        //hshDataMap = objGeneralDAO.getCategoryList(lSolutionId);
        hshDataMap = getSEJBGeneralRemote().getCategoryList(lSolutionId);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
    return hshDataMap;
  }
	
	/**
   * @see pe.com.nextel.dao.GeneralDAO#getSubCategoryList()
   */
  public HashMap getSubCategoryList(String strCategoria, long lSolutionId) {
    HashMap hshDataMap = new HashMap();
      try{
        //hshDataMap = objGeneralDAO.getSubCategoryList(strCategoria);
        hshDataMap = getSEJBGeneralRemote().getSubCategoryList(strCategoria, lSolutionId);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
    return hshDataMap;
  }
	
	/**
   * @see pe.com.nextel.dao.GeneralDAO#getZoneList(long)
   */
  public HashMap getZoneList(long lBizUnitId) {
    HashMap hshDataMap = new HashMap();
      try {
        //hshDataMap = objGeneralDAO.getZoneList(lBizUnitId);
        hshDataMap = getSEJBGeneralRemote().getZoneList(lBizUnitId);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
    return hshDataMap;
  }
    
  /**
   * @see pe.com.nextel.dao.GeneralDAO#getSubGirosByGiroList(long)
   */
  public HashMap getSubGirosByIndustry(long lGiroId) {
    HashMap hshDataMap = new HashMap();
      try{
        hshDataMap = getSEJBGeneralRemote().getSubGirosByIndustry(lGiroId);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
    return hshDataMap;
  }    

	/**
     * @see pe.com.nextel.dao.GeneralDAO#getKitDetail(long, String,long)
     */
	public HashMap getKitDetail(long lKitId, String strModalidad, long lngSalesStructOrigenId) {
		if(logger.isDebugEnabled())
			logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
        try {
            hshDataMap = getSEJBGeneralRemote().getKitDetail(lKitId, strModalidad, lngSalesStructOrigenId);
        } catch(Throwable t){
			manageCatch(hshDataMap, t);
		}
		return hshDataMap;
	}
	
		/**
     * modificación de la llamada a nptable - mlimaylla 
     */
		public HashMap getKitInfo(HashMap hshKitDetail) {
		HashMap hshKitInfo = new HashMap();
		hshKitInfo.put("valorKit", "");
		hshKitInfo.put("valorEquipo", "");
		hshKitInfo.put("valorServicio", "");
		hshKitInfo.put("planTarifarioId", "");
		hshKitInfo.put("planTarifarioNombre", "");
		try {
			HashMap hshKitMap = (HashMap) hshKitDetail.get("hshKitMap");
			hshKitInfo.put("valorKit", MiUtil.defaultString(hshKitMap.get("kitPrice"),""));
			//////////////////////////////////////////////////////
			ArrayList arrProductDetailList = (ArrayList) hshKitDetail.get("arrProductDetailList");
			Iterator iterator = arrProductDetailList.iterator();
      
			HashMap hshValueNpTable;
			hshValueNpTable = getSEJBGeneralRemote().getValueNpTable("PRODUCT_LINE_EQUIP_MOVIL");
			ArrayList arrNpTable = new ArrayList();
			arrNpTable = (ArrayList)hshValueNpTable.get("objArrayList");
			
			/*PCS MLIMAYLLA*/
			HashMap hshValueNpTableSerTel;
			hshValueNpTableSerTel = getSEJBGeneralRemote().getValueNpTable("PRODUCT_LINE_SER_TEL");
            ArrayList arrNpTableSerTel = new ArrayList();
			arrNpTableSerTel = (ArrayList)hshValueNpTableSerTel.get("objArrayList");
			/*FIN PCS MLIMAYLLA*/
			
			while (iterator.hasNext()) {
				HashMap hshProductMap = (HashMap) iterator.next();
				//if (hshProductMap.get("productLineId").equals(new BigDecimal(Constante.PRODUCT_LINE_EQUIPMENT))) {
				//if( arrNpTable.contains(hshProductMap.get("productLineId").toString()) ) {                              
				//PCS MLIMAYLLA if(MiUtil.contentStringInArrayList(arrNpTable,hshProductMap.get("productLineId").toString())) {                               
				if(MiUtil.contentStringInArrayListOfNpTableBean(arrNpTable,hshProductMap.get("productLineId").toString())) {                              
					hshKitInfo.put("valorEquipo", MiUtil.defaultString(hshProductMap.get("priceOneTime"),""));
				/*PCS MLIMAYLLA*/
				/*} else if (hshProductMap.get("productLineId").equals(new BigDecimal(Constante.PRODUCT_LINE_SERVICE))) {*/
				} else if (MiUtil.contentStringInArrayListOfNpTableBean(arrNpTableSerTel,hshProductMap.get("productLineId").toString())) {
				/*FIN PCS MLIMAYLLA*/
					hshKitInfo.put("valorServicio", MiUtil.defaultString(hshProductMap.get("priceOneTime"),""));
					hshKitInfo.put("planTarifarioId", MiUtil.defaultString(hshProductMap.get("planId"),""));
					HashMap hshPlanNombreMap = getPlanTarifarioNombre(((BigDecimal)hshProductMap.get("planId")).longValue());
					hshKitInfo.put("planTarifarioNombre", MiUtil.defaultString(hshPlanNombreMap.get("strPlanTarifarioNombre"),""));
				}
			}
      
      return hshKitInfo;
       
		}catch(RemoteException ex){
        System.out.println("[GeneralService][getKitInfo][RemoteException][" + ex.getMessage()+"]");
        hshKitInfo.put("strMessage",ex.getMessage());     
        return hshKitInfo;   
      }catch(SQLException ex){
        System.out.println("[GeneralService][getKitInfo][SQLException][" + ex.getMessage()+"]");            
        hshKitInfo.put("strMessage",ex.getMessage());     
        return hshKitInfo;    
      }catch(Exception ex){
        System.out.println("[GeneralService][getKitInfo][Exception][" + ex.getMessage()+"]");            
        hshKitInfo.put("strMessage",ex.getMessage());     
        return hshKitInfo;   
      }		
	}
  
  	/**
     * @see pe.com.nextel.dao.GeneralDAO#getRetailList()
     */
	public HashMap getRetailList() {
		if(logger.isDebugEnabled())
			logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
        try {
            hshDataMap = getSEJBGeneralRemote().getRetailList();
        } catch(Throwable t){
			manageCatch(hshDataMap, t);
		}
		return hshDataMap;
	}	
	
	
	/**
     * @see pe.com.nextel.dao.GeneralDAO#getPlanTarifarioNombre(long)
     */
	public HashMap getPlanTarifarioNombre(long lPlanId) {
		if(logger.isDebugEnabled())
			logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
        try {
            hshDataMap = getSEJBGeneralRemote().getPlanTarifarioNombre(lPlanId);
        } catch(Throwable t){
			manageCatch(hshDataMap, t);
		}
		return hshDataMap;
	}
	
	
	/**
     * @see pe.com.nextel.dao.GeneralDAO#getCodigosJerarquiaVentasMap(String, String, String, long, int)
     */
	public HashMap getCodigosJerarquiaVentasMap(String strLevel, String strCode, String strBusUnitId, long lVendedorId, int iFlagVendedor) {
		HashMap hshDataMap = new HashMap();
        try {
            hshDataMap = getSEJBGeneralRemote().getCodigosJerarquiaVentasMap(strLevel, strCode, strBusUnitId, lVendedorId, iFlagVendedor);
        } catch(Throwable t){
			manageCatch(hshDataMap, t);
		}
		return hshDataMap;
	}
	
	/**
     * @see pe.com.nextel.dao.GeneralDAO#getBuildingList(String)
     */
	public HashMap getBuildingList(String strTipo) {
		HashMap hshDataMap = new HashMap();
        try {
            hshDataMap = getSEJBGeneralRemote().getBuildingList(strTipo);
        } catch(Throwable t){
			manageCatch(hshDataMap, t);
		}
		return hshDataMap;
	}
	
	/**
     * @see pe.com.nextel.dao.GeneralDAO#getModelList()
     */
	public HashMap getModelList() {
		HashMap hshDataMap = new HashMap();
		try {
			hshDataMap = getSEJBGeneralRemote().getModelList();            
            if (((String) hshDataMap.get(Constante.MESSAGE_OUTPUT)).startsWith(Constante.SUCCESS_ORA_RESULT)) {             
				hshDataMap.put(Constante.MESSAGE_OUTPUT,null);
            }
		} catch(SQLException sqle) {
			logger.error(formatException(sqle));         
			if (sqle.getErrorCode()==0) {
				hshDataMap.put(Constante.MESSAGE_OUTPUT, null);
			} else {
				hshDataMap.put(Constante.MESSAGE_OUTPUT, sqle.getMessage());
			}
        } catch(Throwable t){
			manageCatch(hshDataMap, t);
		}
		return hshDataMap;
	}
	
	/**
     * @see pe.com.nextel.dao.GeneralDAO#getCodeSetList(String)
     */
	public HashMap getCodeSetList(String strCodeSet) {
		HashMap hshDataMap = new HashMap();
        try {            
            hshDataMap = getSEJBGeneralRemote().getCodeSetList(strCodeSet);
            if (((String) hshDataMap.get(Constante.MESSAGE_OUTPUT)).startsWith(Constante.SUCCESS_ORA_RESULT)) {             
				hshDataMap.put(Constante.MESSAGE_OUTPUT, null);
            }
        } catch(Throwable t){
			manageCatch(hshDataMap, t);
		}
		return hshDataMap;
	}
	
	/**
     * @see pe.com.nextel.dao.GeneralDAO#getGeneralOptionList(String)
     */
	public HashMap getGeneralOptionList(String strGeneralOption) {
		HashMap hshDataMap = new HashMap();
        try {            
            hshDataMap = getSEJBGeneralRemote().getGeneralOptionList(strGeneralOption);
            if (((String) hshDataMap.get(Constante.MESSAGE_OUTPUT)).startsWith(Constante.SUCCESS_ORA_RESULT)) {             
               hshDataMap.put(Constante.MESSAGE_OUTPUT, null);
            }
        } catch(Throwable t){
			manageCatch(hshDataMap, t);
		}
		return hshDataMap;
	}
	
	/**
     * @see pe.com.nextel.dao.GeneralDAO#getProcessList()
     */
	public HashMap getProcessList(String strEquipment) {
		HashMap hshDataMap = new HashMap();
        try {
            hshDataMap = getSEJBGeneralRemote().getProcessList(strEquipment);                
        } catch(Throwable t){
			manageCatch(hshDataMap, t);
		}
		return hshDataMap;
	}
	
	/**
     * @see pe.com.nextel.dao.GeneralDAO#getProcessList()
     */
	public HashMap getDetalleReposicionByTelefono(String strTelefono) {
		HashMap hshDataMap = new HashMap();
        try {            
            hshDataMap = getSEJBGeneralRemote().getDetalleReposicionByTelefono(strTelefono);
        }
		catch(Throwable t) {
			manageCatch(hshDataMap, t);
		}
		return hshDataMap;
	}
	
	/**
	 * @see pe.com.nextel.dao.GeneralDAO#getInfoImei(String)
	 */
	public HashMap getInfoImei(String strImei) {
		HashMap hshDataMap = new HashMap();
        try {            
            hshDataMap = getSEJBGeneralRemote().getInfoImei(strImei);
        }
		catch(Throwable t) {
			manageCatch(hshDataMap, t);
		}
		return hshDataMap;
	} 

    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - FIN
     *  REALIZADO POR: Richard De los Reyes
     *  FECHA: 28/09/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/
	 
    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  ITEMS DE SECCIONES DINAMICAS - INICIO
     *  REALIZADO POR: ISRAEL RONDON
     *  FECHA: 25/10/2007
     *  MODIFICADO POR: EVELYN OCAMPO
     *  FECHA: 04/02/2008
     *  SE INCLUYE COMO PARÁMETRO DE SALIDA LA DIRECCIÓN DE INSTALACIÓN 
     *  COMPARTIDA EN getSharedInstalation.
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/    
    /**
       * <br>Realizado por: <a href="mailto:jose.rondon@nextel.com.pe">Israel Rondón</a>
       * <br>Fecha: 19/10/2007
       * <br>Modificado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
       * <br>Fecha: 14/04/2008
       * @see pe.com.nextel.dao.GeneralDAO#getSharedInstalation(int,String,String,String,String,String)      
    */        
  public HashMap getSharedInstalation( AddressObjectBean objAddressObjB){           
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getSharedInstalation(objAddressObjB);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }
    /**
       * <br>Realizado por: <a href="mailto:jose.rondon@nextel.com.pe">Israel Rondón</a>
       * <br>Fecha: 23/10/2007
       * @see pe.com.nextel.dao.GeneralDAO#getAddress(int,String,String)      
    */        
  public HashMap getAddress(int iswObjectId,String sTipoDirCustomer,String sSwObjectType,long lngSpecificationId){           
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getAddress(iswObjectId,sTipoDirCustomer,sSwObjectType,lngSpecificationId);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }
  
  /**
   * <br>Realizado por: <a href="mailto:jose.rondon@nextel.com.pe">Israel Rondón</a>
   * <br>Fecha: 23/10/2007
   * @see pe.com.nextel.dao.GeneralDAO#getAddressPuntual(int,String)      
   */        
  public HashMap getAddressPuntual(int iswAddressId,String sAddresstype){
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getAddressPuntual(iswAddressId,sAddresstype);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }

 /**
  * <br>Realizado por: <a href="mailto:jose.rondon@nextel.com.pe">Israel Rondón</a>
  * <br>Fecha: 29/10/2007
  * @see pe.com.nextel.dao.GeneralDAO#getValidateContract(int,String)      
  */        
  public HashMap getValidateContract(long lngCustomerID, long lngContractId , long lngSpecificationId,long lngSolutionId, long lngInstallAddressId ){           
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getValidateContract(lngCustomerID,lngContractId,lngSpecificationId, lngSolutionId, lngInstallAddressId);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }

/**
  * <br>Realizado por: <a href="mailto:jose.rondon@nextel.com.pe">Israel Rondón</a>
  * <br>Fecha: 06/11/2007
  * @see pe.com.nextel.dao.GeneralDAO#getServiceList(int)      
  */        
  //johncmb inicio
  public HashMap getServiceList(int intSolutionId, int intPlanId, int intProductId, String strSSAAType,String strType){           
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getServiceList(intSolutionId, intPlanId, intProductId, strSSAAType, strType);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }
  //johncmb fin
  
  /**
  * <br>Realizado por: <a href="mailto:david.lazodelavega@hp.com">David Lazo de la Vega</a>
  * <br>Fecha: 23/09/2010
  * @see pe.com.nextel.dao.GeneralDAO#getOrderDeact(int,String,int)      
  */        
  public HashMap getOrderDeact(long intOrderId, String strPhone, long intServiceId){           
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getOrderDeact(intOrderId, strPhone, intServiceId);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }

/**
  * <br>Realizado por: <a href="mailto:david.lazodelavega@hp.com">David Lazo de la Vega</a>
  * <br>Fecha: 01/10/2010
  * @see pe.com.nextel.dao.GeneralDAO#getServiceActive(String,long,long,long,long)      
  */
  
  public HashMap getServiceActive(String strPhone, long lCustomerId, long lSpecificationId, long lSiteId, long lOrderId){           
    HashMap hshDataMap = new HashMap();
    HashMap hshDataMapServ = new HashMap();
    HashMap hshResult = new HashMap();
    HashMap hshDataMapItem = new HashMap();
    ArrayList listService = null;
    ArrayList listSubscrip = null;
    String message1 = "";
    String message2 = "";
    String strSSAAContrato = "";
    Vector strSSAAActive = new Vector();
    ServiciosBean serviceBean = null;
    ServiciosBean serviceBean2 = null;
      try {
        listService = new ArrayList();
        hshDataMap = (HashMap)getSEJBGeneralRemote().getServiceActive(strPhone, lCustomerId, lSpecificationId, lSiteId);
        //hshDataMap = (HashMap)getSEJBGeneralRemote().getServiceActive(strPhone, lCustomerId, 2024, 15927);
        message1 = (String)hshDataMap.get("strMessage");
        if(message1!=null){ 
          hshResult.put("strmessage",message1);
        }else{
          listSubscrip = new ArrayList();
          strSSAAContrato = (String)hshDataMap.get("strSSAAContrato");
          if(strSSAAContrato != null && !strSSAAContrato.equals("")){
            if(String.valueOf(lSpecificationId).equals(String.valueOf(Constante.SPEC_SSAA_SUSCRIPCIONES))){
              hshDataMapServ = (HashMap)getSEJBGeneralRemote().getSubscriptionList(Constante.SERVICE_PROCESSING,Constante.SERVICE_GROUP_SUBSC);
            }else{
              hshDataMapServ = (HashMap)getSEJBGeneralRemote().getSubscriptionList(Constante.SERVICE_PROCESSING,Constante.SERVICE_GROUP_PROM);
            }
            message2 = (String)hshDataMapServ.get("strMessage");
            if(message2!=null){
              hshResult.put("strmessage",message2);
            }else{
              listService = (ArrayList)hshDataMapServ.get("objSubscriptionList");
              StringTokenizer tokens  = new StringTokenizer(strSSAAContrato,"|");
              while(tokens.hasMoreTokens()){
                  String aux = tokens.nextToken();
                  strSSAAActive.addElement((String)aux);
              }              
              for(int n=0,j = 1;  j <= (strSSAAActive.size()/3); n=n+3,j++){
                
                for( int z = 0; z < listService.size(); z++ ){
                  serviceBean = new ServiciosBean();
                  serviceBean = (ServiciosBean)listService.get(z);
                  if(serviceBean.getNpservicioid() == Long.parseLong((String)strSSAAActive.elementAt(n))){
                    //Obtengo la lista de suscripciones activas
                    serviceBean2 = new ServiciosBean();
                    hshDataMapItem = (HashMap)getSEJBGeneralRemote().getItem(serviceBean.getNpservicioid(),strPhone);
                    ItemBean itemBean = (ItemBean)hshDataMapItem.get("itemBean");
                    serviceBean2.setNpservicioid(serviceBean.getNpservicioid());
                    serviceBean2.setNpnomserv(serviceBean.getNpnomserv());
                    serviceBean2.setNpnomcorserv(serviceBean.getNpnomcorserv());
                    serviceBean2.setNpduration(serviceBean.getNpduration());
                    serviceBean2.setNpmissingdays(itemBean==null?"":itemBean.getNpmissingdays());
                    serviceBean2.setNpsalesstartdate(itemBean==null?null:itemBean.getNpactivationdate());
                    serviceBean2.setNpsalesenddate(itemBean==null?null:itemBean.getNpdeactivationdate());
                    listSubscrip.add(serviceBean2);
                    break;
                  }
                }                
              }
              hshResult.put("strmessage",null);
            }
          }else{
            hshResult.put("strmessage","No se encontraron servicios activos");
          }
        }
        hshResult.put("listSubscrip",listSubscrip);
        
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
        logger.error(formatException(t));
      }
		return hshResult;
  }
  
  /**
  * <br>Realizado por: <a href="mailto:david.lazodelavega@hp.com">David Lazo de la Vega</a>
  * <br>Fecha: 28/12/2010
  * @see pe.com.nextel.dao.GeneralDAO#getProcessTypeByOrderId(long)
  */  
  public HashMap getProcessTypeByOrderId(long lOrderId){
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getProcessTypeByOrderId(lOrderId);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }
  
  
    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  ITEMS DE SECCIONES DINAMICAS - FIN
     *  REALIZADO POR: ISRAEL RONDON
     *  FECHA: 25/10/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/
    
	/***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  Excepciones - INICIO
     *  REALIZADO POR: Jorge Pérez
     *  FECHA: 24/10/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/
    
    /**
       * <br>Realizado por: <a href="mailto:jorge.perez@nextel.com.pe">Jorge Pérez</a>
       * <br>Fecha: 24/10/2007
       * @see pe.com.nextel.dao.ExceptionDAO#getCustomerBillCycle(int,int)
    */
  public HashMap getCustomerBillCycle(int piCustomerId, int piOrderId){   
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getCustomerBillCycle(piCustomerId, piOrderId);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }
  
/**
  * <br>Realizado por: <a href="mailto:jorge.perez@nextel.com.pe">Jorge Pérez</a>
  * <br>Fecha: 24/10/2007
  * @see pe.com.nextel.dao.GeneralDAO
  */
  public HashMap getServiceList(String[] pastrItemServIds, boolean pbOrdenFromSalesRep){   
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getServiceList(pastrItemServIds, pbOrdenFromSalesRep);   
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }
   
 /**
  * <br>Realizado por: <a href="mailto:jorge.perez@nextel.com.pe">Jorge Pérez</a>
  * <br>Fecha: 29/10/2007
  * @see pe.com.nextel.dao.ExceptionDAO
  */
  public HashMap getAccessFee(int piAppId, String[] pastrItemPlanIds, String[] pastrItemServIds, boolean pbOrdenFromSalesRep) {
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getAccessFee(piAppId, pastrItemPlanIds, pastrItemServIds, pbOrdenFromSalesRep);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }
   
   /**
       * <br>Realizado por: <a href="mailto:jorge.perez@nextel.com.pe">Jorge Pérez</a>
       * <br>Fecha: 30/10/2007
       * @see pe.com.nextel.dao.ExceptionDAO
    */
  public HashMap getRentFee(String[] pastrItemProductIds, int iSpecId) {
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getRentFee(pastrItemProductIds, iSpecId);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;  
  }
   
 /**
  * <br>Realizado por: <a href="mailto:jorge.perez@nextel.com.pe">Jorge Pérez</a>
  * <br>Fecha: 02/11/2007
  * @see pe.com.nextel.dao.ExceptionDAO
  */
  public HashMap getServiceCost(int piPlanId, int piServiceId){   
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getServiceCost(piPlanId, piServiceId);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;  
  }
   
  /**
   * <br>Realizado por: <a href="mailto:jorge.perez@nextel.com.pe">Jorge Pérez</a>
   * <br>Fecha: 02/11/2007
   * @see pe.com.nextel.dao.ExceptionDAO
   */
  public HashMap updateExceptionApprove(RequestHashMap objHashMap){
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().updateExceptionApprove(objHashMap);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }

   /***********************************************************************
    ***********************************************************************
    ***********************************************************************
    *  Excepciones - FIN
    *  REALIZADO POR: Jorge Pérez
    *  FECHA: 24/10/2007
    ***********************************************************************
    ***********************************************************************
    ***********************************************************************/	 

	//Inicio CEM COR0303
   /**
       * <br>Realizado por: <a href="mailto:cristian.espinoza@nextel.com.pe">Cristian Espinoza</a>
       * <br>Fecha: 20/02/2008
       * @see pe.com.nextel.dao.ExceptionDAO
    */
  public HashMap getDataNpTable(String strTableName, String strValueDesc) {
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getDataNpTable(strTableName,strValueDesc);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }
	//Fin CEM COR0303

 /**
  * <br>Realizado por: <a href="mailto:oliver.dubock@nextel.com.pe">Odubock</a>
  * <br>Fecha: 28/02/2008
  * @see pe.com.nextel.dao.ExceptionDAO
  */
  public HashMap getValidateSalesman(long lngCustomerId,long lngSiteId,long lngSalesmanId,String strVendedor,int iUserId,int iAppId) {
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getValidateSalesman(lngCustomerId,lngSiteId,lngSalesmanId,strVendedor,iUserId,iAppId);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  } 
  
  /**
   * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
   * <br>Fecha: 14/03/2008
   * @see pe.com.nextel.dao.GeneralDAO
   */
  public String getValue(String strTable, String strValueDesc){
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getValue(strTable,strValueDesc);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return null;
  }
    
  public HashMap getCheckPermission(long lngSellerId, long lngCustomerId, long lngSiteId){
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getCheckPermission(lngSellerId,lngCustomerId,lngSiteId);  
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }

   /**
       * <br>Realizado por: <a href="mailto:hugo.moreno@nextel.com.pe">Hugo Moreno</a>
       * <br>Fecha: 20/05/2008
       * @see pe.com.nextel.dao.GeneralDAO
    */
  public HashMap getNpopportunitytypeid(long lOppontunityId){
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getNpopportunitytypeid(lOppontunityId);        
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }
 
    
    /**
       * <br>Realizado por: <a href="mailto:hugo.moreno@nextel.com.pe">Hugo Moreno</a>
       * <br>Fecha: 20/05/2008
       * @see pe.com.nextel.dao.GeneralDAO
    */
    public int getValidateAuthorization(long lOppontunityId, int iOppType, int iSalesID){
    
      try {
         return getSEJBGeneralRemote().getValidateAuthorization(lOppontunityId, iOppType, iSalesID);        
      }  catch (RemoteException re) {             
            System.out.println("[RemoteException][GeneralService][getValue][" + re.getMessage() + "]["+re.getClass()+"]");                     
            return -1;
      } catch (SQLException ex) {
            System.out.println("[SQLException][GeneralService][getValue][" + ex.getMessage() + "]["+ex.getClass()+"]");                     
            return -1;
      } catch (Exception ex) {
            System.out.println("[Exception][GeneralService][getValue][" + ex.getMessage() + "]["+ex.getClass()+"]");                     
            return -1;
      }	  
    }

  public HashMap selectResponsablePago(long lSpecificationId, String strOrigenOrden,long lValor,String strOpcion,long lngCustomerId,String strTypeCustomer){
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().selectResponsablePago(lSpecificationId,strOrigenOrden,lValor,strOpcion,lngCustomerId,strTypeCustomer);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }
  
  /*JPEREZ*/
  public HashMap getObjectTypeUrl(String strObjectType){
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getObjectTypeUrl(strObjectType);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }

  public HashMap getRolMult(String strScreenOption, long iUserid, int iAppid){
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getRolMult(strScreenOption, iUserid, iAppid);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }
 
  public HashMap getUserInboxByLogin (String strLogin){
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getUserInboxByLogin(strLogin);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }
  
  public HashMap getNameServerReport(String strCategory, String strParameterName){
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getNameServerReport(strCategory, strParameterName);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }
  
   /**
   * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
   * <br>Fecha:25/07/2008
   * @see pe.com.nextel.dao.UsuarioDAO#getPermissionDetail(long,long,int)      
   */    
   public HashMap getPermissionDetail(long lOrderid, long iUserid, int iAppid) {  
    HashMap hshDataMap = new HashMap();
      try {
        int i=getSEJBGeneralRemote().getPermissionDetail(lOrderid, iUserid, iAppid);
        hshDataMap.put("iRetorno",i+"");
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
   }   
  
	/**
     * Motivo: Se obtiene el npvalue de la tabla de item de la orda.
     * <br>Realizado por: <a href="mailto:cristian.espinoza@nextel.com.pe">Cristian Espinoza</a>
     * <br>Fecha: 28/07/2008
	 * 
	 * @param strTableName Nombre del nptable
	 */	
	public HashMap getValueNpTable(String strTableName) {
		
        String strMessage =  null;
        try{
            return getSEJBGeneralRemote().getValueNpTable(strTableName);
        }catch(RemoteException ex){
             System.out.println("[GeneralService][getValueNpTable][RemoteException][" + ex.getMessage()+"]");
             HashMap hshData=new HashMap();
             hshData.put("strMessage",ex.getMessage());     
             return hshData;   
         }catch(SQLException ex){
             System.out.println("[GeneralService][getValueNpTable][SQLException][" + ex.getMessage()+"]");            
             HashMap hshData=new HashMap();
             hshData.put("strMessage",ex.getMessage());     
             return hshData;    
         }catch(Exception ex){
             System.out.println("[GeneralService][getValueNpTable][Exception][" + ex.getMessage()+"]");            
             HashMap hshData=new HashMap();
             hshData.put("strMessage",ex.getMessage());     
             return hshData;   
         }
	}

  /**
     * Motivo: Obtiene los formatos de la categoria
     * <br>Realizado por: <a href="mailto:martin.mverae@nextel.com.pe">Martin Vera Espinoza</a>
     * <br>Fecha: 20/08/2008
     * 
     * @param  lSpecificationId
     */
	public HashMap getFormatBySpecification(long lSpecificationId) {
		
    String strMessage =  null;
    try{
        return getSEJBGeneralRemote().getFormatBySpecification(lSpecificationId);
    }catch(RemoteException ex){
         System.out.println("[GeneralService][getFormatBySpecification][RemoteException][" + ex.getMessage()+"]");
         HashMap hshData=new HashMap();
         hshData.put("strMessage",ex.getMessage());     
         return hshData;   
     }catch(SQLException ex){
         System.out.println("[GeneralService][getFormatBySpecification][SQLException][" + ex.getMessage()+"]");            
         HashMap hshData=new HashMap();
         hshData.put("strMessage",ex.getMessage());     
         return hshData;    
     }catch(Exception ex){
         System.out.println("[GeneralService][getFormatBySpecification][Exception][" + ex.getMessage()+"]");            
         HashMap hshData=new HashMap();
         hshData.put("strMessage",ex.getMessage());     
         return hshData;   
     }
	}
  
  /**
   * <br>Realizado por: <a href="mailto:jorge.perez@nextel.com.pe">Jorge Pérez</a>
   * <br>Fecha:6/10/2008
   * @see pe.com.nextel.dao.GeneralDAO#getSalesmanName(long)      
   */    
   public HashMap getSalesmanName(long lSalesmanId) {      
      try {
        return getSEJBGeneralRemote().getSalesmanName(lSalesmanId);
      }catch(RemoteException ex){
        System.out.println("[GeneralService][getSalesmanName][RemoteException][" + ex.getMessage()+"]");
        HashMap hshData=new HashMap();
        hshData.put("strMessage",ex.getMessage());     
        return hshData;   
      }catch(SQLException ex){
        System.out.println("[GeneralService][getSalesmanName][SQLException][" + ex.getMessage()+"]");            
        HashMap hshData=new HashMap();
        hshData.put("strMessage",ex.getMessage());     
        return hshData;    
      }catch(Exception ex){
        System.out.println("[GeneralService][getSalesmanName][Exception][" + ex.getMessage()+"]");            
        HashMap hshData=new HashMap();
        hshData.put("strMessage",ex.getMessage());     
        return hshData;   
      }		
   }
   
     /**
   * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
   * <br>Fecha:16/09/2008
   * @see pe.com.nextel.dao.UsuarioDAO#getPermissionDetail(long,long,int)      
   */    
   public HashMap getApplicationList(String strLogin) {  
    HashMap hshDataMap = new HashMap();
      try {
        hshDataMap =getSEJBGeneralRemote().getApplicationList(strLogin);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
   }   

  /**
     * Motivo: Lista de equipos pendiente de recojo
     * <br>Realizado por: <a href="mailto:cristian.espinoza@nextel.com.pe">Cristian Espinoza</a>
     * <br>Fecha: 20/08/2008
     * 
     * @param  lSpecificationId
     */
	public HashMap getListaEquipPendRecojo(long customerId,long siteId) {
		
    String strMessage =  null;
    try{
        return getSEJBGeneralRemote().getListaEquipPendRecojo(customerId,siteId);
    }catch(RemoteException ex){
         System.out.println("[GeneralService][getListaEquipPendRecojo][RemoteException][" + ex.getMessage()+"]");
         HashMap hshData=new HashMap();
         hshData.put("strMessage",ex.getMessage());     
         return hshData;   
     }catch(SQLException ex){
         System.out.println("[GeneralService][getListaEquipPendRecojo][SQLException][" + ex.getMessage()+"]");            
         HashMap hshData=new HashMap();
         hshData.put("strMessage",ex.getMessage());     
         return hshData;    
     }catch(Exception ex){
         System.out.println("[GeneralService][getListaEquipPendRecojo][Exception][" + ex.getMessage()+"]");            
         HashMap hshData=new HashMap();
         hshData.put("strMessage",ex.getMessage());     
         return hshData;   
     }
	}   
   
  /**
     * Motivo: 
     * <br>Realizado por: <a href="mailto:edgar.jara@nextel.com.pe">Edgar Jara</a>
     * <br>Fecha: 23/12/2008
     * 
     * @param  strSim
     *         strPin
     */
	public HashMap getPackDetail(String strSim, String strPin) {
		if(logger.isDebugEnabled())
			logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
        try {
            hshDataMap = getSEJBGeneralRemote().getPackDetail(strSim, strPin);
        } catch(Throwable t){
			manageCatch(hshDataMap, t);
		}
		return hshDataMap;
	} 
  
    
  /**
   * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
   * <br>Fecha:09/06/2009
   * @see pe.com.nextel.dao.GeneralDAO#getValueTag1(String, String)      
   */    
   public HashMap getValueTag1(String strValue, String strTable) {      
      try {
        return getSEJBGeneralRemote().getValueTag1(strValue,strTable);
      }catch(RemoteException ex){
        System.out.println("[GeneralService][getValueTag1][RemoteException][" + ex.getMessage()+"]");
        HashMap hshData=new HashMap();
        hshData.put("strMessage",ex.getMessage());     
        return hshData;   
      }catch(SQLException ex){
        System.out.println("[GeneralService][getValueTag1][SQLException][" + ex.getMessage()+"]");            
        HashMap hshData=new HashMap();
        hshData.put("strMessage",ex.getMessage());     
        return hshData;    
      }catch(Exception ex){
        System.out.println("[GeneralService][getValueTag1][Exception][" + ex.getMessage()+"]");            
        HashMap hshData=new HashMap();
        hshData.put("strMessage",ex.getMessage());     
        return hshData;   
      }		
   }

/**
  * <br/>Realizado por: <a href="mailto:jorge.perez@nextel.com.pe">Jorge Pérez</a>
  * <br/>Fecha: 27/05/2009
  * @see pe.com.nextel.dao.GeneralDAO#getValidatePhoneVoIp      
  */        
  public HashMap getValidatePhoneVoIp(long lngCustomerID, String strPhoneNumber , long lngSpecificationId, long lngSolutionId, long lngInstallAddressId ){           
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getValidatePhoneVoIp(lngCustomerID,strPhoneNumber,lngSpecificationId, lngSolutionId, lngInstallAddressId);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }
  
   /**
   * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
   * <br>Fecha:01/07/2009
   * @see pe.com.nextel.dao.GeneralDAO#getPermissionServiceDefault(long,String, String,int,int)      
   */    
   public HashMap getPermissionServiceDefault(long lspecificationid, String strObjectItem, String strModality, int iModelId, int iProductId,String strServiceMsjId) {      
      try {
        return getSEJBGeneralRemote().getPermissionServiceDefault(lspecificationid,strObjectItem,strModality,iModelId,iProductId,strServiceMsjId);
      }catch(RemoteException ex){
        System.out.println("[GeneralService][getPermissionServiceDefault][RemoteException][" + ex.getMessage()+"]");
        HashMap hshData=new HashMap();
        hshData.put("strMessage",ex.getMessage());     
        return hshData;   
      }catch(SQLException ex){
        System.out.println("[GeneralService][getPermissionServiceDefault][SQLException][" + ex.getMessage()+"]");            
        HashMap hshData=new HashMap();
        hshData.put("strMessage",ex.getMessage());     
        return hshData;    
      }catch(Exception ex){
        System.out.println("[GeneralService][getPermissionServiceDefault][Exception][" + ex.getMessage()+"]");            
        HashMap hshData=new HashMap();
        hshData.put("strMessage",ex.getMessage());     
        return hshData;   
      }		
   }
   
   /**
   * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
   * <br>Fecha:09/08/2009
   * @see pe.com.nextel.dao.ServiceDAO#getDetailService(long)      
   */    
   public ServiciosBean getDetailService(long lserviceid) {      
      try {
        return getSEJBGeneralRemote().getDetailService(lserviceid);
      }catch(RemoteException ex){
        System.out.println("[GeneralService][getDetailService][RemoteException][" + ex.getMessage()+"]");
         return null;
      }catch(SQLException ex){
        System.out.println("[GeneralService][getDetailService][SQLException][" + ex.getMessage()+"]");            
        return null;
      }catch(Exception ex){
        System.out.println("[GeneralService][getDetailService][Exception][" + ex.getMessage()+"]");            
        return null;
      }		
   }
   /**
   * <br>Realizado por: <a href="mailto:cesar.barzola@nextel.com.pe">Cesar Barzola</a>
   * <br>Fecha:19/07/2009
   * @see pe.com.nextel.dao.GeneralDAO#getStatusByTable(String,String)      
   */
   public HashMap getStatusByTable(String strTable,String strValue)
   {
     try {
        return getSEJBGeneralRemote().getStatusByTable(strTable,strValue);
      }catch(RemoteException ex){
        System.out.println("[GeneralService][getStatusByTable][RemoteException][" + ex.getMessage()+"]");
        HashMap hshData=new HashMap();
        hshData.put("strMessage",ex.getMessage());     
        return hshData;   
      }catch(SQLException ex){
        System.out.println("[GeneralService][getStatusByTable][SQLException][" + ex.getMessage()+"]");            
        HashMap hshData=new HashMap();
        hshData.put("strMessage",ex.getMessage());     
        return hshData;    
      }catch(Exception ex){
        System.out.println("[GeneralService][getStatusByTable][Exception][" + ex.getMessage()+"]");            
        HashMap hshData=new HashMap();
        hshData.put("strMessage",ex.getMessage());     
        return hshData;   
      }
   }
   
   /*Inicio Data*/
   /**
   * <br>Realizado por: Jorge Pérez
   * <br>Fecha: 26/08/2009
   * @see pe.com.nextel.dao.GeneralDAO#getSalesDataList(int,int,String)      
   */        
  public HashMap getSalesDataList(long lngRule){         
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getSalesDataList(lngRule);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
   }
   
   /**
   * <br>Realizado por: Jorge Pérez
   * <br>Fecha: 26/08/2009
   * @see pe.com.nextel.dao.GeneralDAO#getSalesDataShow(long lngSpecificationId, String strObjectType, String strObjectId)
   */        
  public HashMap getSalesDataShow(long lngSpecificationId, String strObjectType, long lngObjectId){     
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getSalesDataShow(lngSpecificationId, strObjectType, lngObjectId);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
   }
   
   /**
   * <br>Realizado por: Jorge Pérez
   * <br>Fecha: 26/08/2009
   * @see pe.com.nextel.dao.GeneralDAO#getSalesDataShow(long lngSpecificationId, String strObjectType, String strObjectId)
   */        
  public HashMap validateSalesExclusivity(long lngCustomerId, long lngSiteId, long lngWinnerTypeId, long lngSalesmanId){     
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().validateSalesExclusivity(lngCustomerId, lngSiteId, lngWinnerTypeId, lngSalesmanId);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
   }
   
   /**
   * <br>Realizado por: Jorge Pérez
   * <br>Fecha: 26/08/2009
   * @see pe.com.nextel.dao.GeneralDAO#getSalesDataList(int,int,String)      
   */        
  public HashMap getOrderSellersList(long lNpOrderid){         
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getOrderSellersList(lNpOrderid);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
   }
   
   /**
   * <br>Realizado por: Jorge Pérez
   * <br>Fecha: 28/08/2009
   * @see pe.com.nextel.dao.GeneralDAO#getOrderSellerByType(long lNpOrderid, long lngType)
   */        
  public HashMap getOrderSellerByType(long lNpOrderid, long lngType) {     
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getOrderSellerByType(lNpOrderid, lngType);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
   }
   
   /**
   * <br>Realizado por: Jorge Pérez
   * <br>Fecha: 01/09/2009
   * @see pe.com.nextel.dao.GeneralDAO#validateStructRule(long lngSalesRuleId, long lngUserId)
   */        
  public HashMap validateStructRule(long lngSalesRuleId, long lngSalesStrucId) {     
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().validateStructRule(lngSalesRuleId, lngSalesStrucId);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
   }
   
/**
    * <br>Realizado por: <a href="mailto:mauricio.wong@nextel.com.pe">Mauricio Wong</a>
    * <br>Fecha:18/08/2009    
    */
     public HashMap getStockModel(String strModel) {
             HashMap hshDataMap = new HashMap();
     try {            
         hshDataMap = getSEJBGeneralRemote().getStockPorModelo(strModel);
     }
             catch(Throwable t) {
                     manageCatch(hshDataMap, t);
             }
             return hshDataMap;
     }
	 
	   /**
	   * <br>Realizado por: Fanny Najarro
	   * <br>Fecha: 24/09/2009
	   * @see pe.com.nextel.dao.GeneralDAO#validateIncident(long lngGeneratorId, long lngSpecification,
	   * long lngSessionId, int iAppId, long lngCustomerId, long lngSiteId, long lngWinnerTypeId)
	   */        
	  public HashMap validateIncident(long lngGeneratorId, String strGeneratorType, long lngSpecification, long lngUserId, int iAppId, long lngCustomerId, long lngSiteId, long lngWinnerTypeId){     
		HashMap hshDataMap = new HashMap();
		  try {
			return getSEJBGeneralRemote().validateIncident(lngGeneratorId, strGeneratorType, lngSpecification, lngUserId, iAppId,lngCustomerId,lngSiteId,lngWinnerTypeId);
		  }catch(Throwable t){
			manageCatch(hshDataMap, t);
		  }
			return hshDataMap;
	   }
    public HashMap getValidationSalesManProposed(long lUserId,long lSalesManId){
      
      try{
			return getSEJBGeneralRemote().getValidationSalesManProposed(lUserId,lSalesManId);
	  }catch(SQLException sql){
      System.out.println("GeneralService ProposedDAOgetValidationSalesManProposed \nMensaje:" + sql.getMessage()+"\n");
      HashMap hshData=new HashMap();
      hshData.put("strMessage",sql.getMessage());             
      return hshData;           
   }catch(RemoteException re){
      System.out.println("GeneralService ProposedDAOgetValidationSalesManProposed \nMensaje:" + re.getMessage()+"\n");
      HashMap hshData=new HashMap();
      hshData.put("strMessage",re.getMessage());             
      return hshData;      
   }catch(Exception ex){
      System.out.println("GeneralService ProposedDAOgetValidationSalesManProposed \nMensaje:" + ex.getMessage()+"\n");            
      HashMap hshData=new HashMap();
      hshData.put("strMessage",ex.getMessage());             
      return hshData;    
   }
    }
	 
  public HashMap GeneralDAOgetConsultor(long lUserId) {     
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().GeneralDAOgetConsultor(lUserId);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
   }	 

  public HashMap GeneralDAOgetAmbitSalesList(String codType, int regionId) {     
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().GeneralDAOgetAmbitSalesList(codType,regionId);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
   }	 
   
    public HashMap getSalesDataShow(long lngSpecificationId, String strObjectType, long lngObjectId, long lngCustomerId, long lngSiteId){     
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getSalesDataShow(lngSpecificationId, strObjectType, lngObjectId, lngCustomerId, lngSiteId);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
   }
  
    /**
   * <br>Realizado por: Daniel Gutierrez
   * <br>Fecha: 10/08/2010
   * @see pe.com.nextel.dao.GeneralDAO#getValueByConfiguration(String)      
   */   
  public HashMap getValueByConfiguration(String configuration)  {
  
   HashMap hshDataMap = new HashMap();
  
      try {
  
        return getSEJBGeneralRemote().getValueByConfiguration(configuration);
  
      }catch(Throwable t){
  
        manageCatch(hshDataMap, t);
  
      }
	
  	return hshDataMap;
  
  }

    public HashMap GeneralDAOgetNpConfValues(String strNpConfiguration, String strValue, String strValueDesc, String strNpStatus, String strNpTag1, String strNpTag2, String strNpTag3){
      HashMap objHashMap = new HashMap();
      String  strMessage = new String();
      try{
        return getSEJBGeneralRemote().GeneralDAOgetNpConfValues(strNpConfiguration, strValue, strValueDesc, strNpStatus, strNpTag1, strNpTag2, strNpTag3);
      }catch (SQLException e) {
            strMessage = "[SQLException][GeneralService][GeneralDAOgetNpConfValues][" + e.getMessage() + "]["+e.getClass()+"]";
            System.out.println(strMessage);
            objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage = "[RemoteException][GeneralService][GeneralDAOgetNpConfValues][" + e.getMessage() + "]["+e.getClass()+"]";
            System.out.println(strMessage);
            objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][GeneralService][GeneralDAOgetNpConfValues][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
            return objHashMap;
        }
    }  

    public HashMap ProductDAOgetValidateSimImei(String strNumber){
      HashMap objHashMap = new HashMap();
      String strMessage  = new String();
      try{
        return getSEJBGeneralRemote().ProductDAOgetValidateSimImei(strNumber);          
      }catch(SQLException e){
        logger.error(formatException(e));
        strMessage = "[SQLException][GeneralService][ProductDAOgetValidateSimImei][" + e.getMessage() + "]["+e.getClass()+"]";
        objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
        return objHashMap;
      }catch(RemoteException e){
        logger.error(formatException(e));
        strMessage = "[RemoteException][GeneralService][ProductDAOgetValidateSimImei][" + e.getMessage() + "]["+e.getClass()+"]";
        objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
        return objHashMap;  
      }catch(Exception e){
        logger.error(formatException(e));
        strMessage  = "[Exception][GeneralService][ProductDAOgetValidateSimImei][" + e.getClass() + " " + e.getMessage()+"]";
        objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
        return objHashMap;        
      }
    }


   /************************************************************************************************************************
   * <br>Realizado por: <a href="mailto:karen.salvador@hp.com">Karen Salvador</a>
   * <br>Fecha:09/11/2010
   * @see pe.com.nextel.dao.GeneralDAO#getSalesStructOrigen(long,String)      
   **************************************************************************************************************************/    
   public HashMap getSalesStructOrigen(long lSalesStructId, String strCanal) {      
      try {
       return getSEJBGeneralRemote().getSalesStructOrigen(lSalesStructId,strCanal);
      }catch(RemoteException ex){
        System.out.println("[GeneralService][getSalesStructOrigen][RemoteException][" + ex.getMessage()+"]");
        HashMap hshData=new HashMap();
        hshData.put("strMessage",ex.getMessage());     
        return hshData;   
      }catch(SQLException ex){
        System.out.println("[GeneralService][getSalesStructOrigen][SQLException][" + ex.getMessage()+"]");            
        HashMap hshData=new HashMap();
        hshData.put("strMessage",ex.getMessage());     
        return hshData;    
      }catch(Exception ex){
        System.out.println("[GeneralService][getSalesStructOrigen][Exception][" + ex.getMessage()+"]");            
        HashMap hshData=new HashMap();
        hshData.put("strMessage",ex.getMessage());     
        return hshData;   
      }		
   }
   
   
    /************************************************************************************************************************
   * <br>Realizado por: <a href="mailto:karen.salvador@hp.com">Karen Salvador</a>
   * <br>Fecha:09/11/2010
   * @see pe.com.nextel.dao.GeneralDAO#getSalesStructOrigenxRetail(long)      
   **************************************************************************************************************************/    
   public HashMap getSalesStructOrigenxRetail(long lRetailId) {      
      try {
       return getSEJBGeneralRemote().getSalesStructOrigenxRetail(lRetailId);
      }catch(RemoteException ex){
        System.out.println("[GeneralService][getSalesStructOrigenxRetail][RemoteException][" + ex.getMessage()+"]");
        HashMap hshData=new HashMap();
        hshData.put("strMessage",ex.getMessage());     
        return hshData;   
      }catch(SQLException ex){
        System.out.println("[GeneralService][getSalesStructOrigenxRetail][SQLException][" + ex.getMessage()+"]");            
        HashMap hshData=new HashMap();
        hshData.put("strMessage",ex.getMessage());     
        return hshData;    
      }catch(Exception ex){
        System.out.println("[GeneralService][getSalesStructOrigenxRetail][Exception][" + ex.getMessage()+"]");            
        HashMap hshData=new HashMap();
        hshData.put("strMessage",ex.getMessage());     
        return hshData;   
      }		
   }

  /**
  * <br>Realizado por: <a href="mailto:john.salazar@hp.com">john salazar</a>
  * <br>Fecha: 06/11/2010
  * @see pe.com.nextel.dao.ServiceDAO#getServiceGroupLst()      
  */        
  //johncmb inicio
  public HashMap getServiceGroupLst(){           
    HashMap hshDataMap = new HashMap();
      try {
        return getSEJBGeneralRemote().getServiceGroupLst();
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
		return hshDataMap;
  }
  //johncmb fin



  // INICIO - HP Argentina - TIPOS DISPOSITIVO -  PROYECTO HP-PTT
   /**
   * Motivo: getTipoEquipoList
   * <br>Realizado por: <a href="mailto:marcelo.cejas-echeverria@hp.com">Marcelo Cejas Echeverria</a>
   * <br>Fecha: 30/09/2010 
   */  
  public HashMap getTipoEquipoList(){
  
      HashMap hshDataMap = new HashMap();
  
      try {
  
       hshDataMap = getSEJBGeneralRemote().getTipoEquipoList();
   
      }catch(Throwable t){
  
        manageCatch(hshDataMap, t);
  
      }
	
  	return hshDataMap;
  }
  
   /**
   * Motivo: getModelsByTypeOfEquipment
   * <br>Realizado por: <a href="mailto:marcelo.cejas-echeverria@hp.com">Marcelo Cejas Echeverria</a>
   * <br>Fecha: 30/09/2010 
   */  
  public HashMap getModelsByTypeOfEquipment(int deviceTypeId){
  
      HashMap hshDataMap = new HashMap();
  
      try {
  
       hshDataMap = getSEJBGeneralRemote().getModelsByTypeOfEquipment(deviceTypeId);
   
      }catch(Throwable t){
  
        manageCatch(hshDataMap, t);
  
      }
	
  	return hshDataMap;
  }
  // FIN - HP Argentina - TIPOS DISPOSITIVO -  PROYECTO HP-PTT
  

  /**
   * <br>Realizado por: <a href="mailto:mario.mendoza-ludena@hp.com">Antonio Mendoza</a>
   * <br>Fecha: 05/10/2010
   * @see pe.com.nextel.dao.GeneralDAO#get_AttChannel_Struct(int)
   */          
  public HashMap get_AttChannel_Struct(int intSalesstructid){
    HashMap hshDataMap = new HashMap();
      try {
        int i = getSEJBGeneralRemote().get_AttChannel_Struct(intSalesstructid);        
        hshDataMap.put("iRetorno",i+"");
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
    return hshDataMap; 
  }


  /*Fin Data*/ 
  
  /**
   * <br>Realizado por: <a href="mailto:frank.picoy@hp.com">Frank Picoy</a>
   * <br>Fecha: 09/01/2012
   * @see pe.com.nextel.dao.GeneralDAO#doSaveLogItem(objLogItem)
   */          
  public String doSaveLogItem(HashMap objLogItem){
     String strResult="";
      try {
        strResult = getSEJBGeneralRemote().doSaveLogItem(objLogItem);
      }catch(RemoteException ex){
        System.out.println("[GeneralService][doSaveLogItem][RemoteException][" + ex.getMessage()+"]");
      }catch(SQLException ex){
        System.out.println("[GeneralService][doSaveLogItem][SQLException][" + ex.getMessage()+"]");            
      }catch(Exception ex){
        System.out.println("[GeneralService][doSaveLogItem][Exception][" + ex.getMessage()+"]");            
      }		
      return strResult;
  }
  
   public HashMap getConfigurationInstallment(String npvaluedesc) {
      HashMap hshDataMap = new HashMap();
      try {
        hshDataMap = getSEJBGeneralRemote().getConfigurationInstallment(npvaluedesc);
      }catch(Throwable t){
        manageCatch(hshDataMap, t);
      }
      return hshDataMap;
   }
   public String getURLRedirect(int appId, int userId, int orderId,String tab) throws SQLException,CustomException, Exception{
     return getSEJBGeneralRemote().getURLRedirect(appId, userId, orderId, tab);
      
  }
   
    //JRAMIREZ 21/07/2014 Tienda Express 
    public int getOrderExist(int nporderid){   
       try {
            return getSEJBGeneralRemote().getOrderExist(nporderid);
        } catch (Exception e) {
            return 0;
        }
    }
    
    //JRAMIREZ 21/07/2014 Tienda Express 
    public int getBuildingidByOrderid(int nporderid){   
       try {
            return getSEJBGeneralRemote().getBuildingidByOrderid(nporderid);
        } catch (Exception e) {
            return 0;
        }
    }


    /**
     *  ocruces N_O000046759 Control de Tipo de Operación vs Tipo de Orden
     */
    public HashMap getTypeOpeSpecifications(String strCustomerId) {
        HashMap hshDataMap = new HashMap();
        try {
            hshDataMap = getSEJBGeneralRemote().getTypeOpeSpecifications(strCustomerId);
        }catch(Throwable t){
            manageCatch(hshDataMap, t);
        }
        return hshDataMap;
    }

    //EFLORES Requerimiento PM0010274 Verificacion de acceso a datos de cliente
    public String verifyUserCanSeeCustomer(String userId,String objectType,String objectId,String typeMessage){
        String resp = "";
        try{
            resp = getSEJBGeneralRemote().verifyUserCanSeeCustomer(userId,objectType,objectId,typeMessage);
        }catch(Exception e){
            System.out.println(e);
        }
        return resp;
    }

    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  OBTENER MAXIMA CANTIDAD DE MODELOS A REGISTRAR POR CATEGORIA
     *  REALIZADO POR: Eduardo Peña Vilca EPV
     *  FECHA: 18/06/2015
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/
    /**
     * @see pe.com.nextel.dao.GeneralDAO#getDescriptionByValue(String, String)
     */
    public HashMap getValueLimitModel(String strTable,String strSubCategoria) {
        HashMap hshDataMap = new HashMap();
        try{
            hshDataMap = getSEJBGeneralRemote().getValueLimitModel(strTable, strSubCategoria);
        }catch(Throwable t){
            manageCatch(hshDataMap, t);
        }
        return hshDataMap;
    }


    /** INICIO ADT-BLC-083 --LHUAPAYA
      * Numeros que pertenecen al blacklist
      */
    public String getPhoneBlackList(int strnpSite,int strCustomerId, int type){
        HashMap hshDataMap = new HashMap();
        String resp = null;
        try{
            hshDataMap = getSEJBGeneralRemote().getPhoneBlackList(strnpSite,strCustomerId,type);
            if (hshDataMap.get("message") == null){
                resp = "Existen " + hshDataMap.get("count") + " numeros en blacklist";
            }else{
                resp = (String)hshDataMap.get("message");
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return resp;
    }

    /**INICIO ADT-BLC-083 --LHUAPAYA
     * Validacion de cantidad para los productos de tipo bolsa de celulares
     */
    public String getValidateCountUnitsBolCel(int specification,int productOrigen, int productDestino){
        String resp = null;
        HashMap hshDataMap = new HashMap();
        try{
            hshDataMap = getSEJBGeneralRemote().getValidateCountUnitsBolCel(specification,productOrigen,productDestino);
            if (hshDataMap.get("srtMessage") != null){
                resp = (String)hshDataMap.get("srtMessage");
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return resp;
    }


    public ArrayList getTipoDocumentoList() {
        ArrayList arrDocumentType = new ArrayList();
        try {
            arrDocumentType = getSEJBGeneralRemote().getTipoDocumentoList();
        }catch(Throwable t){
            System.out.println();
        }
        return arrDocumentType;
    }

    /**
     * Obtiene las Condiciones de los Reportes de Ordenes de <Reparación
     */
    public ArrayList getConditionsReport(String npTable) {
        ArrayList arrDocumentType = new ArrayList();
        try {
            arrDocumentType = getSEJBGeneralRemote().getConditionsReport(npTable);
        }catch(Throwable t){
            System.out.println();
        }
        return arrDocumentType;
    }
    
     //FBERNALES Requerimiento PM0010503 Validacion del numero de solicitud en Retail
    public Boolean validateNumSolicitudRetail(String sNumeroSolicitud,int iFlagColumnOrderNumber){
        Boolean resp = false;
        try{
            resp = getSEJBGeneralRemote().validateNumSolicitudRetail(sNumeroSolicitud,iFlagColumnOrderNumber);
        }catch(Exception e){
            System.out.println(e);
        }
        return resp;
    }

    //JQUISPE PRY-0762 Obtiene la cantidad de Renta Adelantada
    public HashMap getCantidadRentaAdelantada(int codigoProducto, int codigoPlan){
    	String  strMessage = null;
    	HashMap objHashMap = new HashMap();
    	try{
    		objHashMap = getSEJBGeneralRemote().getCantidadRentaAdelantada(codigoProducto, codigoPlan);
          }catch (SQLException e) {
                strMessage = "[SQLException][GeneralService][getCantidadRentaAdelantada][" + e.getMessage() + "]["+e.getClass()+"]";
                System.out.println(strMessage);
                objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
                return objHashMap;
            }catch (RemoteException e) {
                strMessage = "[RemoteException][GeneralService][getCantidadRentaAdelantada][" + e.getMessage() + "]["+e.getClass()+"]";
                System.out.println(strMessage);
                objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
                return objHashMap;
            }catch (Exception e) {
                strMessage  = "[Exception][GeneralService][getCantidadRentaAdelantada][" + e.getClass() + " " + e.getMessage()+"]";
                objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
                return objHashMap;
            }
    	
    	return objHashMap;
    }

    //JQUISPE PRY-0762 Obtiene el precio del plan
    public HashMap getPrecioPlan(int piAppId, String strItemPlanId){
    	String  strMessage = null;
        HashMap objHashMap = new HashMap();
        try {
    		objHashMap = getSEJBGeneralRemote().getPrecioPlan(piAppId, strItemPlanId);
        }catch (SQLException e) {
                strMessage = "[SQLException][GeneralService][getPrecioPlan][" + e.getMessage() + "]["+e.getClass()+"]";
            System.out.println(strMessage);
                objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
            return objHashMap;
        }catch (RemoteException e) {
                strMessage = "[RemoteException][GeneralService][getPrecioPlan][" + e.getMessage() + "]["+e.getClass()+"]";
            System.out.println(strMessage);
                objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
            return objHashMap;
        }catch (Exception e) {
                strMessage  = "[Exception][GeneralService][getPrecioPlan][" + e.getClass() + " " + e.getMessage()+"]";
                objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
            return objHashMap;
        }
    	
    	return objHashMap;
    }

    //[PRY-0710] EFLORES Determina si existe de la tabla swbapps.np_table un registro para el campo npvalue con la combinacion  nptable,npvaluedesc
    //0: No encontro
    //1: Encontro
    public int validateIfNpvalueIsInNptable(String npTable,String npValueDesc,String npValue){
        int permiso = 0;
        try{
            permiso = getSEJBGeneralRemote().validateIfNpvalueIsInNptable(npTable,npValueDesc,npValue);
        }catch (Exception e){
            System.out.print(e);
        }
        return permiso;
    }

    //DERAZO PRY-0721 Obtiene la lista de regiones habilitadas por producto
    public HashMap getEnabledRegions(String strProductId){
        HashMap objHashMap = new HashMap();
        String  strMessage = new String();

        try {
            return getSEJBGeneralRemote().getEnabledRegions(strProductId);
        }catch (SQLException e) {
            strMessage  = "[SQLException][NewOrderService][getProductType][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][NewOrderService][getProductType][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][NewOrderService][getProductType][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }

    //EFLORES BAFI2 Obtiene la lista de provincias habilitadas por region
    public HashMap getListProvinceBAFI(String strProductId,String strZonaCoberturaId){
        HashMap objHashMap = new HashMap();
        String  strMessage = new String();

        try {
            return getSEJBGeneralRemote().getListProvinceBAFI(strProductId,strZonaCoberturaId);
        }catch (SQLException e) {
            strMessage  = "[SQLException][GeneralService][getListProvinceBAFI][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][GeneralService][getListProvinceBAFI][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][GeneralService][getListProvinceBAFI][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
}
    }

    //EFLORES BAFI2 Obtiene la lista de provincias habilitadas por region
    public HashMap getListDistrictBAFI(String strProductId,String strProvinceZoneId){
        HashMap objHashMap = new HashMap();
        String  strMessage = new String();

        try {
            return getSEJBGeneralRemote().getListDistrictBAFI(strProductId,strProvinceZoneId);
        }catch (SQLException e) {
            strMessage  = "[SQLException][GeneralService][getListDistrictBAFI][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (RemoteException e) {
            strMessage  = "[RemoteException][GeneralService][getListDistrictBAFI][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }catch (Exception e) {
            strMessage  = "[Exception][GeneralService][getListDistrictBAFI][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println(strMessage);
            objHashMap.put("strMessage",strMessage);
            return objHashMap;
        }
    }

    /**
     * @see pe.com.nextel.dao.ConfigurationDAO#getTraceabilityConfigurations()
     * DERAZO REQ-0940 Obtiene las configuraciones para la funcionalidad de Trazabilidad de Pedidos
     */
    public HashMap getTraceabilityConfigurations(){
        HashMap hshResultMap = new HashMap();
        int flagTraceability = 0;
        try {
            HashMap hshDataMap = getSEJBGeneralRemote().getTraceabilityConfigurations();
            String strMessage = (String)hshDataMap.get(Constante.MESSAGE_OUTPUT);

            if(!MiUtil.isNotNull(strMessage)){
                List<ConfigurationBean> listConfiguration = (ArrayList<ConfigurationBean>)hshDataMap.get("listConfiguration");

                List<ConfigurationBean> listTracSpecifications = new ArrayList<ConfigurationBean>();
                List<ConfigurationBean> listTracDispatchPlaces = new ArrayList<ConfigurationBean>();
                List<ConfigurationBean> listTracTypeDocuments = new ArrayList<ConfigurationBean>();
                List<ConfigurationBean> listTracTypeCustomersRUC = new ArrayList<ConfigurationBean>();

                if(listConfiguration.size() > 0){
                    //Separamos las configuraciones
                    for(int i=0; i<listConfiguration.size(); i++){
                        ConfigurationBean configuration = listConfiguration.get(i);

                        if(configuration.getNpConfiguration().equalsIgnoreCase(Constante.CONF_SPEC_TRACEABILITY)){
                            listTracSpecifications.add(configuration);
                        }
                        if(configuration.getNpConfiguration().equalsIgnoreCase(Constante.CONF_DISP_PLACE_TRACEABILITY)){
                            listTracDispatchPlaces.add(configuration);
                        }
                        if(configuration.getNpConfiguration().equalsIgnoreCase(Constante.CONF_TYPE_DOCUMENT_TRACEABILITY)){
                            listTracTypeDocuments.add(configuration);
                        }
                        if(configuration.getNpConfiguration().equalsIgnoreCase(Constante.CONF_TYPE_CUSTOMER_RUC_TRACEABILITY)){
                            listTracTypeCustomersRUC.add(configuration);
                        }

                        if(configuration.getNpConfiguration().equalsIgnoreCase(Constante.CONF_FLAG_TRACEABILITY)){
                            flagTraceability = Integer.parseInt(configuration.getNpValue());
                        }
                    }

                    hshResultMap.put("listTracSpecifications", listTracSpecifications);
                    hshResultMap.put("listTracDispatchPlaces", listTracDispatchPlaces);
                    hshResultMap.put("listTracTypeDocuments", listTracTypeDocuments);
                    hshResultMap.put("listTracTypeCustomersRUC", listTracTypeCustomersRUC);
                    hshResultMap.put("flagTraceabilityFunct", flagTraceability);
                }
            }

            hshResultMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        }
        catch(Throwable t){
            manageCatch(hshResultMap, t);
            System.out.println("Error [SQLException][GeneralService][getTraceabilityConfigurations]["+t.getMessage()+"]");
        }

        return hshResultMap;
    }

    /**
     * @see pe.com.nextel.dao.ConfigurationDAO#getValidateShowContacts()
     * DERAZO REQ-0940 Se valida si la orden va a mostrar o no la seccion de contactos
     */
    public HashMap getValidateShowContacts(long orderid){
        HashMap hshResultMap = new HashMap();
        int resultValidation = 0;
        try {
            HashMap hshDataMap = getSEJBGeneralRemote().getValidateShowContacts(orderid);
            String strMessage = (String)hshDataMap.get(Constante.MESSAGE_OUTPUT);
            resultValidation = (Integer)hshDataMap.get("resultValidation");

            hshResultMap.put("resultValidation",resultValidation);
            hshResultMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        }
        catch(Throwable t){
            manageCatch(hshResultMap, t);
            System.out.println("Error [SQLException][GeneralService][getTraceabilityConfigurations]["+t.getMessage()+"]");
        }

        return hshResultMap;
    }

     /* @see pe.com.nextel.dao.GeneralDAO#getBlockServiceListByUser()
     * EFLORES PRY-1112 Obtiene lista de tuplas de servicios adicionales
     */
    public HashMap getExcludingAditionalServices(){
        HashMap hshResultMap = new HashMap();
        try {
            return getSEJBGeneralRemote().getExcludingAditionalServices();
        }
        catch(Throwable t){
            manageCatch(hshResultMap, t);
            System.out.println("Error [SQLException][GeneralService][getExcludingAditionalServices]["+t.getMessage()+"]");
        }

        return hshResultMap;
    }
    
    /**
     * Motivo: Listado de lugar de despacho delivery.
     * <br>Realizado por: PRY-1093 JCURI</a>
     * <br>Fecha: 01/06/2018
	 * 
	 * @param 
	 */	
	public HashMap lugarDespachoDeliveryList() {
		
        String strMessage =  null;
        try{
            return getSEJBGeneralRemote().lugarDespachoDeliveryList();
        }catch(RemoteException ex){
             System.out.println("[GeneralService][lugarDespachoDeliveryList][RemoteException][" + ex.getMessage()+"]");
             HashMap hshData=new HashMap();
             hshData.put("strMessage",ex.getMessage());     
             return hshData;   
         }catch(SQLException ex){
             System.out.println("[GeneralService][lugarDespachoDeliveryList][SQLException][" + ex.getMessage()+"]");            
             HashMap hshData=new HashMap();
             hshData.put("strMessage",ex.getMessage());     
             return hshData;    
         }catch(Exception ex){
             System.out.println("[GeneralService][lugarDespachoDeliveryList][Exception][" + ex.getMessage()+"]");            
             HashMap hshData=new HashMap();
             hshData.put("strMessage",ex.getMessage());     
             return hshData;   
         }
	}
}
