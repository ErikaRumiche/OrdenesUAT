/*
 * Created on 10/09/2007
 * Copyright (c) Nextel del Peru S.A.
 */
package pe.com.nextel.ejb;

import java.io.FileInputStream;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.sql.DataSource;

import pe.com.nextel.bean.*;
import pe.com.nextel.dao.CategoryDAO;
import pe.com.nextel.dao.ConfigurationDAO;
import pe.com.nextel.dao.ExceptionDAO;
import pe.com.nextel.dao.GeneralDAO;
import pe.com.nextel.dao.InstallmentSalesDAO;
import pe.com.nextel.dao.PortalSessionDAO;
import pe.com.nextel.dao.ProductDAO;
import pe.com.nextel.dao.ProposedDAO;
import pe.com.nextel.dao.RepairDAO;
import pe.com.nextel.dao.ServiceDAO;
import pe.com.nextel.dao.UsuarioDAO;
import pe.com.nextel.exception.CustomException;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;
import pe.com.nextel.util.RequestHashMap;
import pe.com.nextel.util.StaticProperties;

/**
 * Motivo:    Clase EJB que obtiene la lógiga de negocio de procesos generales o
 *            comunes. <br>
 * Realizado  por <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>:<br>
 * Fecha 	    12/02/2008 <br>
 * @version 	1.00
 * @author 	  Nextel del Peru S.A – Lee Rosales
 * @see 	    SessionBean
 */ 

public class SEJBGeneralBean implements SessionBean 
{
  private SessionContext _context;
  DataSource        ds                    = null;
   GeneralDAO    objGeneralDAO   = null;
   UsuarioDAO    objUsuarioDAO   = null;  
   CategoryDAO   objCategoryDAO  = null;
   ExceptionDAO  objExceptionDAO = null;
   ServiceDAO     objServiceDAO = null;
   RepairDAO     objRepairDAO    = null;
   ProposedDAO   objProposedDAO  =null;
   ConfigurationDAO objConfigurationDAO = null;
   ProductDAO    objProductDAO   = null;
  InstallmentSalesDAO objInstallmentSalesDAO = null;

  public void ejbCreate() {
      objGeneralDAO   =   new GeneralDAO();
      objUsuarioDAO   =   new UsuarioDAO();      
      objCategoryDAO  =   new CategoryDAO();
      objExceptionDAO =   new ExceptionDAO();
      objRepairDAO    =   new RepairDAO();
      objServiceDAO   =   new ServiceDAO();
      objProposedDAO  =   new ProposedDAO();
      objConfigurationDAO = new ConfigurationDAO();
      objProductDAO   =   new ProductDAO();  
      objInstallmentSalesDAO = new InstallmentSalesDAO();
      //Generar una referencia para el DataSource
       try {
          Context context = new InitialContext();       

           StaticProperties singleton = StaticProperties.instance();
           Properties properties = singleton.props;
          ds = (DataSource)context.lookup(properties.getProperty("JNDI.DATASOURCE"));          
       }catch(Exception ex){
            ex.printStackTrace();
       }
      
  }

  public void ejbActivate(){}

  public void ejbPassivate(){}

  public void ejbRemove(){}
  
  public void setSessionContext(SessionContext ctx){}
  
  /***********************************************************************     
   ***********************************************************************
   *  ORDENES - INICIO
   *  REALIZADO POR: Lee Rosales Crispin
   *  FECHA: 30/10/2007
   ***********************************************************************     
   ***********************************************************************/ 
   

  public HashMap GeneralDAOgetComboList(String av_datatable) throws Exception,SQLException{
    return objGeneralDAO.getComboList(av_datatable);
  }
  
  public String GeneralDAOgetWorldNumber(String strPhone,String strType) throws Exception,SQLException{
    return objGeneralDAO.getWorldNumber(strPhone,strType);
  }
  
  public HashMap getDealerBySalesman(long lngSalesmanId) throws Exception, SQLException{
    return objGeneralDAO.getDealerBySalesman(lngSalesmanId);
  }
  
  /***********************************************************************     
   ***********************************************************************
   *  ORDENES - FIN
   *  REALIZADO POR: Lee Rosales Crispin
   *  FECHA: 30/10/2007
   ***********************************************************************     
   ***********************************************************************/ 
   
  
    /***********************************************************************
   ***********************************************************************
   ***********************************************************************
   *  INTEGRACION DE ORDENES Y RETAIL - INICIO
   *  REALIZADO POR: Carmen Gremios
   *  FECHA: 13/09/2007
   ***********************************************************************
   ***********************************************************************
   ***********************************************************************/  

   public HashMap getTableList(String strParamName, String strParamStatus) 
   throws Exception {
      return objGeneralDAO.getTableList(strParamName,strParamStatus);
   }
   
   public HashMap getSalesList(long iUserId, int iAppId) 
   throws Exception {
      return objGeneralDAO.getSalesList(iUserId,iAppId);
   }
   
   public int getRol(int iScreenoptionid, long iUserid, int iAppid) 
   throws Exception {
      return objUsuarioDAO.getRol(iScreenoptionid, iUserid, iAppid);
   }
   
   public int getRol(int iScreenoptionid, int iLevel, String strCode) 
   throws Exception {
      return objUsuarioDAO.getRol(iScreenoptionid, iLevel, strCode);
   }   
   
  /* public int getCheckPermission(long lCustomerId,long lSiteId,long lUserId) 
   throws Exception {
      return objUsuarioDAO.getCheckPermission(lCustomerId, lSiteId, lUserId);
   }      */
   
   public HashMap getRepresentantesCCList() 
   throws SQLException,Exception {
      return objGeneralDAO.getRepresentantesCCList();
   }
   
   public HashMap getDistDeparProvList() throws Exception {
      return objGeneralDAO.getDistDeparProvList();   
   }
   
   public HashMap getTitleList() throws Exception {
      return objGeneralDAO.getTitleList();
   }
   
   public HashMap getUbigeoList(String sDptoId, String sProvId, 
                        String sFlag) 
   throws Exception {
      return objGeneralDAO.getUbigeoList(sDptoId,sProvId,sFlag);
   }
   
   public HashMap getAreaCodeList(String strAreaName, String strAreaCode) 
   throws Exception {
      return objGeneralDAO.getAreaCodeList(strAreaName,strAreaCode);
   }
   
   
   public HashMap getSpecificationDetail(long lSolutionId) 
   throws Exception {
      return objCategoryDAO.getSpecificationDetail(lSolutionId);
   }
   
   public HashMap getRegionList() 
   throws Exception {
      return objGeneralDAO.getRegionList();
   }
   
   public String getDepartmentName(String strCode) throws Exception
   {
      return objGeneralDAO.getDepartmentName(strCode);
   }
   
   public String getRegionName(long lRegionId) throws Exception
   {
      return objGeneralDAO.getRegionName(lRegionId);
   }  
   
   public  HashMap getUbigeoList(int iDptoId,int iProvId,String sFlag)throws Exception
   {
      return objGeneralDAO.getUbigeoList(iDptoId,iProvId,sFlag);
   } 
   
   public int getCustomerValue(long lCustomerId) 
   throws Exception {
      return objGeneralDAO.getCustomerValue(lCustomerId);
   }  
    
   public int getCountCases(String strType, String strValue)
   throws Exception {
      return objGeneralDAO.getCountCases(strType,strValue);
   }  

   public HashMap generateOrderExterna(RepairBean objRepairBean,String strLogin)
   throws Exception {
      return objRepairDAO.generateOrderExterna(objRepairBean,strLogin);
   }     

   public HashMap generateOrderInterna(RepairBean objRepairBean,String strLogin)
   throws Exception {
      return objRepairDAO.generateOrderInterna(objRepairBean,strLogin);
   }        
   
   public  HashMap getImeiDetail(RepairBean objRepairBean)
   throws Exception {
      return objRepairDAO.getImeiDetail(objRepairBean);
   }           

   public  HashMap getImeiDetailTab(RepairBean objRepairBean)
   throws Exception {
      return objRepairDAO.getImeiDetailTab(objRepairBean);
   }  
   
   public String getFechaEmision(String strImei) 
   throws Exception {
      return objRepairDAO.getFechaEmision(strImei);
   }      
   
   public HashMap getGeneralOptionList(String strGeneralOption,long lValue) 
   throws Exception {
      return objRepairDAO.getGeneralOptionList(strGeneralOption,lValue);
   }      

	public HashMap getRepuestoDetail(long lObjectId,String strObjectType,String strLogin)
   throws Exception {
      return objRepairDAO.getRepuestoDetail(lObjectId,strObjectType,strLogin);
   }         

	public String getBuildingName(int iBuildingid) 
   throws Exception {
      return objGeneralDAO.getBuildingName(iBuildingid);
   }

    /* OCRUCES - N_O000046759 Control de Tipo de Operación vs Tipo de Orden*/
   public String getValidateTypOpe(String npconfigurationtype)
        throws Exception {
        return objGeneralDAO.getValidateTypOpe(npconfigurationtype);
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
   *  ITEMS DE SECCIONES DINANICAS - INICIO
   *  REALIZADO POR: ISRAEL RONDON
   *  FECHA: 30/10/2007
   *  MODIFICADO POR:EVELYN OCAMPO
   *  FECHA: 04/02/2008
   *  SE INCLUYE COMO PARÁMETRO DE SALIDA LA DIRECCIÓN DE INSTALACIÓN 
   *  COMPARTIDA EN getSharedInstalation.
   ***********************************************************************
   ***********************************************************************
   ***********************************************************************/

   public  HashMap getSharedInstalation(AddressObjectBean objAddressObjB)throws SQLException,Exception{
    return objGeneralDAO.getSharedInstalation(objAddressObjB);
   } 

   
   public  HashMap getAddress(int iswObjectId,String sTipoDirCustomer,String sSwObjectType,long lngSpecificationId)throws Exception
  {
    return objGeneralDAO.getAddress(iswObjectId,sTipoDirCustomer,sSwObjectType,lngSpecificationId);
  }
   
   public  HashMap getAddressPuntual(int iswAddressId,String sAddresstype)throws Exception
  {
    return objGeneralDAO.getAddressPuntual(iswAddressId,sAddresstype);
  }  

   public  HashMap getValidateContract(long lngCustomerID, long lngContractId , long lngSpecificationId,long lngSolutionId, long lngInstallAddressId )throws Exception{
    return objGeneralDAO.getValidateContract(lngCustomerID,lngContractId,lngSpecificationId,lngSolutionId,lngInstallAddressId);
  }
  
  /**
   * INICIO - DLAZO - SUSCRIPCIONES
   */
 //cmbjohn inicio
   public  HashMap getServiceList(int intSolutionId, int intPlanId, int intProductId, String strSSAAType, String strType) throws Exception
  {
    return objGeneralDAO.getServiceList(intSolutionId, intPlanId, intProductId, strSSAAType, strType);
  } 
//cmbjohn final
  
  public HashMap getOrderDeact(long intOrderId, String strPhone, long intServiceId) throws Exception
  {
    return objGeneralDAO.getOrderDeact(intOrderId, strPhone, intServiceId);
  }

  public HashMap getServiceActive(String strPhone, long lCustomerId, long lSpecificationId, long lSiteId) throws Exception
  {
    return objGeneralDAO.getServiceActive(strPhone, lCustomerId, lSpecificationId, lSiteId);
  }
  
  public HashMap getSubscriptionList(String strprocessing, String strgroup) throws Exception
  {
    return objGeneralDAO.getSubscriptionList(strprocessing,strgroup);
  }
  
  public HashMap getItem(long lServicioId, String strPhone) throws Exception
  {
    return objGeneralDAO.getItem(lServicioId,strPhone);
  }
  
  public HashMap getProcessTypeByOrderId(long lOrderId) throws Exception
  {
    return objGeneralDAO.getProcessTypeByOrderId(lOrderId);
  }
  /**
   * FIN - DLAZO - SUSCRIPCIONES
   */
  
   /***********************************************************************
   ***********************************************************************
   ***********************************************************************
   *  ITEMS DE SECCIONES DINANICAS - FIN
   *  REALIZADO POR: ISRAEL RONDON
   *  FECHA: 30/10/2007
   ***********************************************************************
   ***********************************************************************
   ***********************************************************************/

  /**********************************************************************
   * EXCEPCIONES - INICIO
   * REALIZADO POR: Jorge Pérez
   * FECHA: 24/10/2007
   * ********************************************************************/
   public HashMap getCustomerBillCycle(int piCustomerId, int piOrderId) throws SQLException, Exception
   {
      return objExceptionDAO.getCustomerBillCycle(piCustomerId, piOrderId);   
   }
   
   public HashMap getServiceList(String[] pastrItemServIds,  boolean pbOrdenFromSalesRep) throws SQLException, Exception
   {
      /* Los servicios llegan con el formato: {"123|N|S|345|N|N|", "432|S|S|879|S|N|", etc, etc}
       * "123|N|S|345|N|N|" indica que el primer ítem tiene 2 servicios (123 y 345)
       **/      
      String strServices = "";      
      
      if (pbOrdenFromSalesRep)
      {
         for (int i = 0; i < pastrItemServIds.length; i++){
            if (pastrItemServIds[i].compareTo("|")!=0 )
               //strServices = strServices +","+pastrItemServIds[i];
               strServices = strServices + (strServices.compareTo("")==0?pastrItemServIds[i]:","+pastrItemServIds[i]);               
        }
      }else {
         for (int i = 0; i < pastrItemServIds.length; i++){
            StringTokenizer tokens    = new StringTokenizer(pastrItemServIds[i],"|");           
            while (tokens.hasMoreTokens()) {
               //Los Id de servicio siempre llegan seguido de dos valores que indican Disponible y Solicitado
               String strServiceId =  tokens.nextToken();
               String strAvaiable  =  tokens.nextToken();
               String strRequested =  tokens.nextToken();
               
               //sólo se consideran los servicios que tienen N y S
               if ( (strAvaiable.compareTo("N")==0) && (strRequested.compareTo("S")==0 )  )
                  //Se concatenan los servicios para ser enviados  al query del SP
                  //strServices = strServices +","+strServiceId;
                  strServices = strServices + (strServices.compareTo("")==0?strServiceId:","+strServiceId);
            }            
            
         }
      }
      if (strServices.length()>0 )
         return objGeneralDAO.getServiceList(strServices);
      else
         return null;   
   }
   
   public HashMap getAccessFee(int piAppId, String[] pastrItemPlanIds, String[] pastrItemServIds, boolean pbOrdenFromSalesRep) throws SQLException, Exception {
      /* Los servicios llegan con el formato: {"123|N|S|345|N|N|", "432|S|S|879|S|N|", etc, etc}
       * "123|N|S|345|N|N|" indica que el primer ítem tiene 2 servicios (123 y 345)
       **/      
      System.out.println("pastrItemServIds "+pastrItemServIds.length);
      ArrayList astrServicesTemp = new ArrayList();
      String strAux;
      if (pbOrdenFromSalesRep){
        //Para este caso los servicios se envía: "123|346|233","545|474", etc
        for (int i = 0; i < pastrItemServIds.length; i++)
          astrServicesTemp.add(pastrItemServIds[i]);              
      }else{
        for (int i = 0; i < pastrItemServIds.length; i++){          
          strAux = "";
          StringTokenizer tokens    = new StringTokenizer(pastrItemServIds[i],"|");           
          while (tokens.hasMoreTokens()) {
            //Los Id de servicio siempre llegan seguido de dos valores que indican Disponible y Solicitado
            String strServiceId =  tokens.nextToken();
            String strAvaiable  =  tokens.nextToken();
            String strRequested =  tokens.nextToken();
            //sólo se consideran los servicios que tienen N y S            
            if ( (strAvaiable.compareTo("N")==0) && (strRequested.compareTo("S")==0 )  ){
              //Se concatenan los servicios para ser enviados  al query del SP              
              strAux = strAux + "|" + strServiceId;              
            }               
          }           
          astrServicesTemp.add(strAux);
        }
      }      
      return objExceptionDAO.getAccessFee(piAppId, pastrItemPlanIds, astrServicesTemp);     
   }
   public HashMap getRentFee(String[] pastrItemProductIds, int iSpecId) throws SQLException, Exception {            
      return objExceptionDAO.getRentFee(pastrItemProductIds, iSpecId);     
   }
   public HashMap getServiceCost(int piPlanId, int piServiceId) throws Exception, SQLException {
      return objExceptionDAO.getServiceCost(piPlanId, piServiceId);
   }
   public HashMap updateExceptionApprove(RequestHashMap objHashMap) 
   {   
      HashMap hshResult   = new HashMap();  
      String strMessage = null;
      try{
        Connection conn = ds.getConnection();
        conn.setAutoCommit(false);
        
        try {
          String  strStatus = "",
                  strNote   = "", 
                  strLogin  = "",
                  strExceptionid = "",
                  strOrderId = "",
                  strExceptionUnitId = "", 
                  strExceptionUserId = "",
                  strTimeStamp  = "";
                  
          
          int iExceptionid, iOrderId, iExceptionUnitId, iExceptionUserId;
          strExceptionid      = objHashMap.getParameter("hndnpexceptionpedidoid");      
          strOrderId          = objHashMap.getParameter("hndnppedidoid");      
          strStatus           = objHashMap.getParameter("hdnexceptionnew");                
          strExceptionUnitId  = objHashMap.getParameter("hndunitId");      
          strExceptionUserId  = objHashMap.getParameter("hndexceptionuserid");            
          strNote             = objHashMap.getParameter("hdnNota");            
          strLogin            = objHashMap.getParameter("hdnSessionLogin");
          long lTimeStamp     = MiUtil.parseLong(objHashMap.getParameter("hdntimestamp"));
          
          iExceptionid        = MiUtil.parseInt(strExceptionid);
          iOrderId            = MiUtil.parseInt(strOrderId);
          iExceptionUnitId    = MiUtil.parseInt(strExceptionUnitId);
          iExceptionUserId    = MiUtil.parseInt(strExceptionUserId);
          
          hshResult = objExceptionDAO.updateExceptionApprove(iExceptionid, iOrderId, strStatus, iExceptionUnitId, iExceptionUserId, strNote, strLogin, lTimeStamp, conn);
          if( ((String)hshResult.get("strMessage"))!=null){
            if (conn != null) conn.rollback();
              return hshResult;
          }
          //Confirmar la transaccion en la BD
          conn.commit();        
          return hshResult;
        } catch (Exception e) {
            //Si existe error, deshacer los cambios en la BD
            if (conn != null) conn.rollback();          
            e.printStackTrace();
            strMessage  = MiUtil.getMessageClean("[Exception][updateExceptionApprove]["+e.getClass() + " " + e.getMessage()+ " - Caused by " + e.getCause() + "]");
            hshResult.put("strMessage",strMessage);
            return hshResult;
        }finally {
            //Finalmente, pase lo que pase, cerrar la conexion
            if (conn != null) conn.close();
        } 
      
      }catch (Exception ex) {
        ex.printStackTrace();
        strMessage  = MiUtil.getMessageClean("[Exception][doSaveOrder]["+ex.getClass() + " " + ex.getMessage()+"]");
        hshResult.put("strMessage",strMessage);
        return hshResult;
      } 
      
   }
   
   /**********************************************************************
   * EXCEPCIONES - FIN
   * REALIZADO POR: Jorge Pérez
   * FECHA: 24/10/2007
   * ********************************************************************/

	/***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES - INICIO
     *  REALIZADO POR: Richard De los Reyes
     *  FECHA: 07/11/2007
     ***********************************************************************
     ***********************************************************************
     **********************************************************************/
	public HashMap getCodigosJerarquiaVentasMap(String strLevel, String strCode, String strBusUnitId, long lVendedorId, int iFlagVendedor) throws SQLException, Exception {
		return objGeneralDAO.getCodigosJerarquiaVentasMap(strLevel, strCode, strBusUnitId, lVendedorId, iFlagVendedor);
	}

	public HashMap getBuildingList(String strTipo) throws SQLException, Exception {
		return objGeneralDAO.getBuildingList(strTipo);
	}

	public HashMap getKitDetail(long lKitId, String strModalidad,long lngSalesStructOrigenId) throws SQLException, Exception {
		return objGeneralDAO.getKitDetail(lKitId, strModalidad, lngSalesStructOrigenId);
	}
  
	public HashMap getPackDetail(String strSim, String strPin) throws SQLException, Exception {
		 return objGeneralDAO.getPackDetail(strSim, strPin);
	}  

	public HashMap getPlanTarifarioNombre(long lPlanId) throws SQLException, Exception {
		return objGeneralDAO.getPlanTarifarioNombre(lPlanId);
	}
	
	public HashMap getModelList() throws SQLException, Exception {
		return objGeneralDAO.getModelList();
	}
	
	public HashMap getCodeSetList(String strCodeSet) throws SQLException, Exception {
		return objGeneralDAO.getCodeSetList(strCodeSet);
	}
	
	public HashMap getGeneralOptionList(String strGeneralOption) throws SQLException, Exception {
		return objGeneralDAO.getGeneralOptionList(strGeneralOption);
	}
	
	public HashMap getProcessList(String strEquipment) throws SQLException, Exception {
		return objGeneralDAO.getProcessList(strEquipment);
	}
	
	public HashMap getDetalleReposicionByTelefono(String strTelefono) throws SQLException, Exception {
		return objGeneralDAO.getDetalleReposicionByTelefono(strTelefono);
	}
	
	public HashMap getRetailList() throws SQLException, Exception {
		return objGeneralDAO.getRetailList();
	}
	
	public HashMap getInfoImei(String strImei) throws SQLException, Exception {
		return objGeneralDAO.getInfoImei(strImei);
	}
	
	/***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES - FIN
     *  REALIZADO POR: Richard De los Reyes
     *  FECHA: 07/11/2007
     ***********************************************************************
     ***********************************************************************
     **********************************************************************/

    // Inicio CEM COR0303
	public HashMap getDataNpTable(String strTableName,String strValueDesc) throws SQLException, Exception {
		return objGeneralDAO.getDataNpTable(strTableName,strValueDesc);
	}
  
  public HashMap getValidateSalesman(long lngCustomerId, long lngSiteId, long lngSalesmanId, String strVendedor,int iUserId,int iAppId) throws Exception, SQLException{
    return objGeneralDAO.getValidateSalesman(lngCustomerId,lngSiteId,lngSalesmanId,strVendedor,iUserId,iAppId);
  }
  
  public String getValue(String strTable, String strValueDesc) throws  SQLException, Exception{
    return objGeneralDAO.getValue(strTable,strValueDesc);
  }
  
  public HashMap getCheckPermission(long lngSellerId, long lngCustomerId, long lngSiteId) throws Exception, SQLException{
    return objGeneralDAO.getCheckPermission(lngSellerId,lngCustomerId,lngSiteId);
  }
	// Fin CEM COR0303   
   
  public HashMap getNpopportunitytypeid(long lOppontunityId) 
   throws Exception {
      return objGeneralDAO.getNpopportunitytypeid(lOppontunityId);
   } 
   
   public int getValidateAuthorization(long lOppontunityId, int iOppType, int iSalesID) 
   throws Exception {
      return objGeneralDAO.getValidateAuthorization(lOppontunityId, iOppType, iSalesID);
   } 
	
	public HashMap selectResponsablePago(long lSpecificationId, String strOrigenOrden,long lValor,String strOpcion,long lngCustomerId,String strTypeCustomer) throws Exception, SQLException{
		return objGeneralDAO.selectResponsablePago(lSpecificationId,strOrigenOrden,lValor,strOpcion,lngCustomerId,strTypeCustomer);
   } 
   
   /*JPEREZ*/
  public HashMap getObjectTypeUrl(String strObjectType)throws Exception, SQLException{
    return objGeneralDAO.getObjectTypeUrl(strObjectType);    
  }
  
  public HashMap getRolMult(String strScreenOption, long iUserid, int iAppid)throws Exception, SQLException{
    return objUsuarioDAO.getRolMult(strScreenOption, iUserid, iAppid);    
  }
  
	public HashMap getUserInboxByLogin(String strLogin) throws Exception, SQLException{  
		return objGeneralDAO.getUserInboxByLogin(strLogin);
	}
  
	public HashMap getNameServerReport(String strCategory, String strParameterName) throws Exception, SQLException{
		return objGeneralDAO.getNameServerReport(strCategory,strParameterName);
   }
   
	public HashMap getSubGirosByIndustry(long lGiroId) throws SQLException, Exception {
		return objGeneralDAO.getSubGirosByIndustry(lGiroId);
	}

  public HashMap getDescriptionByValue(String strValue, String strTable) throws  SQLException, Exception{
    return objGeneralDAO.getDescriptionByValue(strValue,strTable);
  }

  public HashMap getDominioList(String dominioTabla) throws  SQLException, Exception{
    return objGeneralDAO.getDominioList(dominioTabla);
  }

  public HashMap getTypeOpeSpecifications(String strCustomerId) throws  SQLException, Exception{
        return objGeneralDAO.getTypeOpeSpecifications(strCustomerId);
  }

  public HashMap getRepairConfiguration(String av_param) throws  SQLException, Exception{
    return objGeneralDAO.getRepairConfiguration(av_param);
  }

  public HashMap getRatePlanList() throws  SQLException, Exception{
    return objGeneralDAO.getRatePlanList();
  }

  public HashMap getUbigeoList(UbigeoBean objUbigeoBean) throws  SQLException, Exception{
    return objGeneralDAO.getUbigeoList(objUbigeoBean);
  }

  public HashMap getGiroList() throws  SQLException, Exception{
    return objGeneralDAO.getGiroList();
  }

  public HashMap getSubGirosByGiroList(long lGiroId) throws  SQLException, Exception{
    return objGeneralDAO.getSubGirosByGiroList(lGiroId);
  }

  public HashMap getKitsList(String strTiendaRetail) throws SQLException, Exception{
    return objGeneralDAO.getKitsList(strTiendaRetail);
  }

  public HashMap getComboReparacionList(String strNombreOpcion) throws  SQLException, Exception{
    return objGeneralDAO.getComboReparacionList(strNombreOpcion);
  }

  public HashMap getEstadoOrdenList() throws  SQLException, Exception{
    return objGeneralDAO.getEstadoOrdenList();
  }

  public HashMap getDivisionList() throws  SQLException, Exception{
    return objGeneralDAO.getDivisionList();
  }

  public HashMap getSolucionList(long lDivisionId) throws  SQLException, Exception{
    return objGeneralDAO.getSolucionList(lDivisionId);
  }

  public HashMap getCategoryList(long lSolutionId) throws  SQLException, Exception{
    return objGeneralDAO.getCategoryList(lSolutionId);
  }

  public HashMap getSubCategoryList(String strCategoria, long lSolutionId) throws  Exception,  SQLException{
    return objGeneralDAO.getSubCategoryList(strCategoria, lSolutionId);
  }

  public HashMap getZoneList(long lBizUnitId) throws  SQLException, Exception{
    return objGeneralDAO.getZoneList(lBizUnitId);
  }
  
  public HashMap getComboRegionList() throws  Exception,  SQLException{
    return objGeneralDAO.getComboRegionList();
  }
  
  public int getPermissionDetail(long lOrderId, long iUserid, int iAppid) 
   throws Exception {
      return objUsuarioDAO.getPermissionDetail(lOrderId, iUserid, iAppid);
   }

	public HashMap getValueNpTable(String strTableName) throws SQLException, Exception {
		return objGeneralDAO.getValueNpTable(strTableName);
	}

  public HashMap getFormatBySpecification(long lSpecificationId) throws SQLException, Exception {
		return objGeneralDAO.getFormatBySpecification(lSpecificationId);
	}
  
  public HashMap getApplicationList(String strLogin) throws  Exception, SQLException{
    return (new PortalSessionDAO()).getApplicationList(strLogin);
  }
  public HashMap getSalesmanName(long lSalesmanId) throws SQLException, Exception {
		return objGeneralDAO.getSalesmanName(lSalesmanId);
	}
  public HashMap getListaEquipPendRecojo(long customerId, long siteId) throws SQLException, Exception {
		return objGeneralDAO.getListaEquipPendRecojo(customerId,siteId);
	}
	/*public String insertFailedInstances(String strInstanceList, String npOrderId) throws SQLException, Exception {
		return objGeneralDAO.insertFailedInstance(strInstanceList, npOrderId);
	}   
  */
   public HashMap getResolution(String strFabricatorId) throws Exception, SQLException{
      return objRepairDAO.getResolution(strFabricatorId);
  }
  
   public HashMap getDiagnosis(String intProviderId ) throws Exception, SQLException{
      return objRepairDAO.getDiagnosis(intProviderId);
  }

 
   public HashMap getFailureList(int intValue, int intRepairId, String strRepairTypeId) throws SQLException, Exception{
      return objRepairDAO.getFailureList(intValue, intRepairId, strRepairTypeId);
  }
  
  
   public String getDiagnosisDescription(int intValue) throws Exception, SQLException{
      return objRepairDAO.getDiagnosisDescription(intValue);
  }

   /*public HashMap getSparePartsListByModel(String strModelo) throws SQLException, Exception{
      return objRepairDAO.getSparePartsListByModel(strModelo);
  }*/
  
   public HashMap getSparePartsListByModel(String strModelo, int intRepairId, String strRepairTypeId) throws SQLException, Exception{
   return objRepairDAO.getSparePartsListByModel(strModelo, intRepairId, strRepairTypeId);
  }

   public HashMap getSelectedFailureList(int intValue, String strRepairTypeId) throws Exception, SQLException{
    return objRepairDAO.getSelectedFailureList(intValue, strRepairTypeId);
  }

 	public HashMap getTypeServices() throws SQLException, Exception {
		return objRepairDAO.getTypeServices();
	}
   
   
   public HashMap getloadServices(String strTipoReparacion) throws SQLException, Exception {
		return objRepairDAO.getloadServices(strTipoReparacion);
	}
          
   public HashMap newOrderReparation(HashMap objHashMap) throws Exception, SQLException{
      return objRepairDAO.newOrderReparation(objHashMap);
   }
  
   public HashMap getSelectedSpareList(int intValue, String strRepairTypeId) throws Exception, SQLException {
      return objRepairDAO.getSelectedSpareList(intValue, strRepairTypeId);
   }
 
 
   public HashMap/*String*/ validateTrueBounce(String strImei,long intFallaid, String strBounce) throws Exception,SQLException {
      return objRepairDAO.validateTrueBounce(strImei,intFallaid,strBounce);
   } 
  
   public HashMap getRepairCount(String strValue) throws SQLException,  Exception {
      return objRepairDAO.getRepairCount(strValue);
   }
   
   public HashMap getSmallRepairDetail(String strValue) throws SQLException,  Exception {
      return objRepairDAO.getSmallRepairDetail(strValue);
   }
   
   public HashMap getNpRepairBOF(int intValue, String strLogin) throws SQLException,  Exception {
    return objRepairDAO.getNpRepairBOF(intValue, strLogin);
   }
  
   public HashMap activateEquipment(String strImei,String strImeiNuevo, String strSim, String strSimNuevo, String strReplaceType) throws Exception,SQLException {
    return objRepairDAO.activateEquipment(strImei, strImeiNuevo, strSim, strSimNuevo, strReplaceType);
   } 
  
   public HashMap getRepairid(String strImei) throws Exception {
      return objRepairDAO.getRepairid(strImei);
   }
      
   public HashMap GenerateInvDoc(HashMap hshParameters) throws SQLException,  Exception {
      return objRepairDAO.GenerateInvDoc(hshParameters);    
   }
   
 	public HashMap getServices() throws SQLException, Exception {
		return objRepairDAO.getServices();
	}

 	public HashMap getAccesories() throws SQLException, Exception {
		return objRepairDAO.getAccesories();
	}
   
   public HashMap getAccesoryModels(String strAccesoryType) throws SQLException, Exception {
		return objRepairDAO.getAccesoryModels(strAccesoryType);
	}
   
   
   public HashMap getDocGenerate(String strImei, long strRepairId) throws Exception {
      return objRepairDAO.getDocGenerate(strImei, strRepairId);
   }
 
   
   public HashMap getDiagnosisLevel(String strProviderId, int intLevel) throws Exception, SQLException{
      return objRepairDAO.getDiagnosisLevel(strProviderId,intLevel);
   }
   
   public HashMap getDocGenClose(String strImei, long strRepairId) throws Exception {
      return objRepairDAO.getDocGenClose(strImei, strRepairId);
   }
    
    
   public HashMap validateStock(long intFallaid, long  intSpecification , String strLogin) throws Exception,SQLException {
      return objRepairDAO.validateStock(intFallaid,intSpecification,strLogin);
   } 
   
   
   public HashMap getValueTag1(String strValue, String strTable) throws SQLException, Exception {
		return objGeneralDAO.getValueTag1(strValue,strTable);
	}
     
  public HashMap getValidatePhoneVoIp(long lngCustomerID, String strPhoneNumber , long lngSpecificationId,long lngSolutionId, long lngInstallAddressId )throws Exception{
    return objGeneralDAO.getValidatePhoneVoIp(lngCustomerID,strPhoneNumber,lngSpecificationId,lngSolutionId,lngInstallAddressId);     
   }    
  
  //Modificado: Daniel Gutierrez Tagle  --- Se agrego el parametro strServiceMsjId para obtener el id del servicio de mensajeria -- 23/09/2010
  public HashMap getPermissionServiceDefault(long lspecificationid, String strObjectItem, String strModality, int iModelId, int iProductId,String strServiceMsjId) throws SQLException, Exception {
		return objGeneralDAO.getPermissionServiceDefault(lspecificationid,strObjectItem,strModality,iModelId,iProductId,strServiceMsjId);
  }
   
 public ServiciosBean getDetailService(long lserviceid) throws SQLException, Exception {
      return objServiceDAO.getDetailService(lserviceid);
   }
/**CBARZOLA: Cambio de Modelo - Distintas tecnologias **/
/**fecha:19/07/2009**/
 public HashMap getStatusByTable(String strTable, String strValue) throws SQLException,Exception{
    return objGeneralDAO.getStatusByTable(strTable,strValue);
  }
 
 /* Inicio Data*/ 
 public HashMap getSalesDataList(long lngRule) 
   throws SQLException, Exception {
      return objGeneralDAO.getSalesDataList(lngRule);
   } 
   
  public HashMap getSalesDataShow(long lngSpecificationId, String strObjectType, long lngObjectId) 
   throws SQLException, Exception {
      return objGeneralDAO.getSalesDataShow(lngSpecificationId, strObjectType, lngObjectId);
  } 
  public HashMap validateSalesExclusivity(long lngCustomerId, long lngSiteId, long lngWinnerTypeId, long lngSalesmanId) 
   throws SQLException, Exception {
      return objGeneralDAO.validateSalesExclusivity(lngCustomerId, lngSiteId, lngWinnerTypeId, lngSalesmanId);  
  }
  public HashMap getOrderSellersList(long lNpOrderid) 
    throws SQLException, Exception {
      return objGeneralDAO.getOrderSellersList(lNpOrderid);
   }
   
  public HashMap getOrderSellerByType(long lNpOrderid, long lngType) 
   throws SQLException, Exception {
      return objGeneralDAO.getOrderSellerByType(lNpOrderid, lngType);
  }
  
  public HashMap validateStructRule(long lngSalesRuleId, long lngSalesStrucId)
   throws SQLException, Exception {
      return objGeneralDAO.validateStructRule(lngSalesRuleId, lngSalesStrucId);
  } 
  
   /**MWONG - Lista de stock en tiendas 31/08/2009**/
  public HashMap getStockPorModelo(String strModel) throws SQLException, Exception{
      return objGeneralDAO.getStockPorModelo(strModel);
  }
  
  
   public HashMap validateIncident(long lngGeneratorId, String strGeneratorType, long lngSpecification, long lngUserId, int iAppId, long lngCustomerId, long lngSiteId, long lngWinnerTypeId)
      throws SQLException, Exception {
      return objGeneralDAO.validateIncident(lngGeneratorId, strGeneratorType, lngSpecification, lngUserId, iAppId,lngCustomerId,lngSiteId, lngWinnerTypeId);
  }
  public HashMap getValidationSalesManProposed(long lUserId,long lSalesManId)throws SQLException, Exception{
    return  objProposedDAO.getValidationSalesManProposed(lUserId,lSalesManId); 
  }
 /* Fin Data*/ 

  public HashMap validateSim(String strSim) throws Exception, SQLException{
       return objRepairDAO.validateSim(strSim);
     }
   public String getReplaceType(String orderId) throws Exception, SQLException{
      return objRepairDAO.getReplaceType(orderId);
   }
   public String getValidateActiveEquipment(String imei, String sim, String replaceType) throws Exception, SQLException{
      return objRepairDAO.getValidateActiveEquipment(imei, sim, replaceType);
   }

  public HashMap GeneralDAOgetConsultor(long lUserId) throws  SQLException, Exception
  {
    return objGeneralDAO.getConsultor(lUserId);
  }

  public HashMap GeneralDAOgetAmbitSalesList(String codType, int regionId) throws  SQLException, Exception
  {
    return objGeneralDAO.getAmbitSalesList(codType,regionId);
  }
  public  HashMap getSalesDataShow(long lngSpecificationId, String strObjectType, long lngObjectId, long lngCustomerId, long lngSiteId)throws SQLException,Exception
  {
    return objGeneralDAO.getSalesDataShow(lngSpecificationId,strObjectType, lngObjectId, lngCustomerId, lngSiteId);
  }
  public  String getValidInternetNextel(long orderId)throws SQLException,Exception
  {
    return objRepairDAO.getValidInternetNextel(orderId);
  }
  
  public  HashMap getFinalEstateList()throws SQLException,Exception
  {
    return objRepairDAO.getFinalEstateList();
  }
  
  public  HashMap getEquipSOList()throws SQLException,Exception
  {
    return objRepairDAO.getEquipSOList();
  }
  
  public  int validateChangeProcess(long orderId)throws SQLException,Exception
  {
    return objRepairDAO.validateChangeProcess(orderId);
  }
  
   public HashMap validateSpecOrders(HashMap hshParameters)throws SQLException,Exception
  {
    return objRepairDAO.validateSpecOrders(hshParameters);
  }
  
  public String getTechnologyByImei(String imei) throws SQLException, Exception
  {
    return objRepairDAO.getTechnologyByImei(imei);
  }
  
   /**
      Method : getValueByConfiguration
      Purpose: 
      Developer       		Fecha       Comentario
      =============   		==========  ======================================================================
      DGUTIERREZ     	   11/08/2010  Creación 
       */
       
  public HashMap getValueByConfiguration(String configuration) throws SQLException, Exception {
    
    return objConfigurationDAO.getValueByConfiguration(configuration);
    
  }

  public HashMap GeneralDAOgetNpConfValues(String strNpConfiguration, String strValue, String strValueDesc, String strNpStatus, String strNpTag1, String strNpTag2, String strNpTag3) throws  SQLException, Exception
  {
    return objConfigurationDAO.getNpConfigurationValues(strNpConfiguration,strValue,strValueDesc,strNpStatus,strNpTag1,strNpTag2,strNpTag3);
  }

  public HashMap ProductDAOgetValidateSimImei(String strNumber) throws  SQLException, Exception
  {
    return objProductDAO.getValidateSimImei(strNumber);
  }
//johncmb inicio
public HashMap getServiceGroupLst() throws Exception
  {
  System.out.println("[SEJBGeneralBean][getServiceGroupLst-->]");
   return  objServiceDAO.getServiceGroupLst();
}
//johncmb fin

   // INICIO - HP Argentina - TIPOS DISPOSITIVO -  PROYECTO HP-PTT
   /**
   * Motivo: getTipoEquipoList
   * <br>Realizado por: <a href="mailto:marcelo.cejas-echeverria@hp.com">Marcelo Cejas Echeverria</a>
   * <br>Fecha: 30/09/2010 
   */  
    public HashMap getTipoEquipoList() throws SQLException, Exception {    
     return objGeneralDAO.getTipoEquipoList();
    }
  
  
   /**
   * Motivo: getModelsByTypeOfEquipment
   * <br>Realizado por: <a href="mailto:marcelo.cejas-echeverria@hp.com">Marcelo Cejas Echeverria</a>
   * <br>Fecha: 30/09/2010 
   */  
    public HashMap getModelsByTypeOfEquipment(int deviceTypeId) throws SQLException, Exception {
       return objGeneralDAO.getModelsByTypeOfEquipment(deviceTypeId);
    }
  // FIN - HP Argentina - TIPOS DISPOSITIVO -  PROYECTO HP-PTT

  public int get_AttChannel_Struct(int intSalesstructid) throws Exception
  {
   return objGeneralDAO.get_AttChannel_Struct(intSalesstructid);
  }
  
  public HashMap getSalesStructOrigen(long lSalesStructId, String strCanal) throws  SQLException, Exception{
    return objGeneralDAO.getSalesStructOrigen(lSalesStructId,strCanal);
  }
  
   public HashMap getSalesStructOrigenxRetail(long lRetailId) throws  SQLException, Exception{
    return objGeneralDAO.getSalesStructOrigenxRetail(lRetailId);
  }
  
  /**
   * Motivo: doSaveLogItem
   * <br>Realizado por: <a href="mailto:frank.picoy@hp.com">Frank Picoy</a>
   * <br>Fecha: 09/01/2012 
   */  
  public String doSaveLogItem(HashMap objLogItem) throws SQLException, Exception {    
     return objGeneralDAO.doSaveLogItem(objLogItem);
  }
  
 public HashMap getConfigurationInstallment(String npvaluedesc)  throws  SQLException, Exception{
    return objInstallmentSalesDAO.getConfigurationInstallment(npvaluedesc);
  }


    public String getURLRedirect(int appId, int userId, int orderId,String tab) throws SQLException,CustomException, Exception {
        Connection conn = ds.getConnection();
        String page = null;
        String url = null;
        int valueEdit=0;
        int valueRol = 0;
        String valueView = null;
        try{
            valueEdit = objUsuarioDAO.getPermissionEdit(conn,userId, orderId);
            valueRol  = objUsuarioDAO.getRol(conn,Constante.SCRN_OPTTO_ORDEREDIT, userId, orderId);
            valueView = objUsuarioDAO.getPermissionViewOrder(conn, userId, orderId, appId);
        }catch(Exception e){
            e.printStackTrace();
            throw new Exception(e);
        }finally{
            if (conn!=null)
                conn.close();
        }
        if (valueEdit == 1 && valueRol == 1) {
            page = Constante.PAGE_ORDER_EDIT;
        } else {
            page = Constante.PAGE_ORDER_DETAIL;
        }
        
        if (valueView.equals(Constante.ANSWER_YES)){
            tab = (tab!=null)?"/"+tab:"";
            url = "/portal/page/portal/orders/" + page + tab + "?an_nporderid="+orderId;    
        }
        else
        {
            throw new CustomException("Ud. no tiene permiso para visualizar la Orden");
        }
            
        return url;
    }

    public HashMap getConfigurations(String strParam) throws Exception {
       return objRepairDAO.getConfigurations(strParam);
    }
    
    public HashMap getComboTiendaList() throws Exception {
       return objRepairDAO.getComboTiendaList();
    }
   
   public HashMap getDataLoanForReport(String repairId) throws SQLException, Exception {
		return objRepairDAO.getDataLoanForReport(repairId);
	}
   
   public HashMap getDamageList(String repairListId, String model) throws SQLException, Exception {
		return objRepairDAO.getDamageList(repairListId, model);
	}
   
   //JRAMIREZ 21/07/2014 Tienda Express
    public int getOrderExist(int nporderid) throws SQLException, Exception {
                 return objGeneralDAO.getOrderExist(nporderid);
         }
    
    //JRAMIREZ 21/07/2014 Tienda Express
    public int getBuildingidByOrderid(int nporderid) throws SQLException, Exception {
                 return objGeneralDAO.getBuildingidByOrderid(nporderid);
         }
    //EFLORES Requerimiento PM0010274
    public String verifyUserCanSeeCustomer(String userId,String objectType,String objectId,String typeMessage)
            throws Exception{
        return objGeneralDAO.verifyUserCanSeeCustomer(userId, objectType,objectId,typeMessage);
    }
    //EPENA 18/06/2015  Validación de Cantidad de Items Registrados por Ordenes EPV
    public HashMap getValueLimitModel(String strTable,String strSubCategoria) throws  SQLException, Exception{
        return objGeneralDAO.getValueLimitModel(strTable, strSubCategoria);
    }
 
    //ADT-BCL-083 numeros en blacklist
    public HashMap getPhoneBlackList(int strnpSite,int strCustomerId, int type) throws  SQLException, Exception{
        return objGeneralDAO.getPhoneBlacklist(strnpSite,strCustomerId,type);
    }

    //ADT-BCL-083 numero de productos de tipo bolsa de celulares
    public HashMap getValidateCountUnitsBolCel(int specification,int productOrigen, int productDestino) throws  SQLException, Exception{
        return objGeneralDAO.getValidateCountUnitsBolCel(specification,productOrigen,productDestino);
    }

    //EPENA 23/06/2015
    public ArrayList getTipoDocumentoList()  throws SQLException, Exception {
        return objGeneralDAO.getTipoDocumentoList();
    }

    //EPENA 23/07/2015
    public ArrayList getConditionsReport(String npTable)  throws SQLException, Exception {
        return objGeneralDAO.getConditionsReport(npTable);
    }
  
    //FBERNALES Requerimiento PM0010503
    public Boolean validateNumSolicitudRetail(String sNumeroSolicitud,int iFlagColumnOrderNumber)
            throws Exception{
        return objGeneralDAO.validateNumSolicitudRetail(sNumeroSolicitud,iFlagColumnOrderNumber);
    }

    //JQUISPE PRY-0762 Obtiene la cantidad de Renta Adelantada
    public HashMap getCantidadRentaAdelantada(int codigoProducto,int codigoPlan)throws  SQLException, Exception{
        return objProductDAO.getCantidadRentaAdelantada(codigoProducto, codigoPlan);
    }
    
    //JQUISPE PRY-0762 Obtiene el precio del plan
    public HashMap getPrecioPlan(int piAppId, String strItemPlanId) throws SQLException, Exception {    	
    	String[] pastrItemPlanIds = {strItemPlanId};    	
    	ArrayList astrServicesTemp = new ArrayList();    	
    	astrServicesTemp.add(null);
    	
    	return objExceptionDAO.getAccessFee(piAppId, pastrItemPlanIds, astrServicesTemp);
    }

    //[PRY-0710] EFLORES Determina si existe de la tabla swbapps.np_table un registro para el campo npvalue con la combinacion  nptable,npvaluedesc
    //0: No encontro
    //1: Encontro
    public int validateIfNpvalueIsInNptable(String npTable,String npValueDesc,String npValue) throws Exception{
        HashMap rows = objGeneralDAO.getDataNpTable(npTable,npValueDesc);
        String strMessage = String.valueOf(rows.get("strMessage"));
        if(strMessage != null && !strMessage.equals("")){
            ArrayList<NpTableBean> arrNpTable = (ArrayList<NpTableBean>)rows.get("objArrayList");
            for (NpTableBean t: arrNpTable) {
                if(t.getNpvalue().equals(npValue)){
                    return 1;
                }
            }
        }
        return 0;
    }

    //DERAZO PRY-0721 Obtiene la lista de regiones habilitadas por producto
    public HashMap getEnabledRegions(String strProductId) throws SQLException, Exception{
        return objProductDAO.getEnabledRegions(strProductId);
    }

    //EFLORES BAFI2
    public HashMap getListProvinceBAFI(String strProductId,String strNpProvinceZoneId) throws SQLException, Exception{
        return objProductDAO.getListProvinceBAFI(strProductId,strNpProvinceZoneId);
    }
    public HashMap getListDistrictBAFI(String strProductId,String strNpDistrictZoneId) throws SQLException, Exception{
        return objProductDAO.getListDistrictBAFI(strProductId,strNpDistrictZoneId);
    }

    //DERAZO REQ-0940
    public HashMap getTraceabilityConfigurations() throws SQLException, Exception {
        return objConfigurationDAO.getTraceabilityConfigurations();
    }

    //DERAZO REQ-0940
    public HashMap getValidateShowContacts(long orderid) throws SQLException, Exception {
        return objConfigurationDAO.getValidateShowContacts(orderid);
    }
    //EFLORES PRY-1112
    public HashMap getExcludingAditionalServices() throws SQLException, Exception{
      return objGeneralDAO.getExcludingAditionalServices();
    }
    
    //JCURI PRY-1093
    public HashMap lugarDespachoDeliveryList() throws SQLException, Exception{
      return objGeneralDAO.lugarDespachoDeliveryList();
    }
} 