package pe.com.nextel.ejb;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJBObject;

import pe.com.nextel.bean.AddressObjectBean;
import pe.com.nextel.bean.EquipmentDamage;
import pe.com.nextel.bean.RepairBean;
import pe.com.nextel.bean.ServiciosBean;
import pe.com.nextel.bean.UbigeoBean;
import pe.com.nextel.exception.CustomException;
import pe.com.nextel.util.RequestHashMap;


public interface SEJBGeneralRemote extends EJBObject {

	/***********************************************************************
	 ***********************************************************************
	 ***********************************************************************
	 *  ORDENES - INICIO
	 *  REALIZADO POR: Lee Rosales Crispin
	 *  FECHA: 30/10/2007
	 ***********************************************************************
	 ***********************************************************************
	 **********************************************************************/ 
	HashMap GeneralDAOgetComboList(String av_datatable) throws Exception, RemoteException, SQLException;
	
	String GeneralDAOgetWorldNumber(String strPhone,String strType) throws Exception, SQLException, RemoteException;
	
	HashMap getDealerBySalesman(long lngSalesmanId) throws Exception, RemoteException, SQLException;	
	/***********************************************************************
	 ***********************************************************************
	 ***********************************************************************
	 *  ORDENES - FIN
	 *  REALIZADO POR: Lee Rosales Crispin
	 *  FECHA: 30/10/2007
	 ***********************************************************************
	 ***********************************************************************
	 **********************************************************************/ 
  
    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - INICIO
     *  REALIZADO POR: Carmen Gremios
     *  FECHA: 13/09/2007
     ***********************************************************************
     ***********************************************************************
     **********************************************************************/
   HashMap getTableList(String strParamName, String strParamStatus) throws RemoteException, Exception;
   
   HashMap getSalesList(long iUserId, int iAppId) throws RemoteException, Exception;
   
   int getRol(int iScreenoptionid, long iUserid, int iAppid) throws RemoteException, Exception;
   
   int getRol(int iScreenoptionid, int iLevel, String strCode) throws RemoteException, Exception;
   
   /*int getCheckPermission(long lCustomerId,long lSiteId,long lUserId) throws RemoteException, Exception;*/

   HashMap getRepresentantesCCList() throws SQLException,RemoteException, Exception;
   
   HashMap getDistDeparProvList() throws RemoteException, Exception;
   
   HashMap getTitleList() throws RemoteException, Exception;
   
   HashMap getUbigeoList(String sDptoId, String sProvId, String sFlag) throws RemoteException, Exception;
   
   HashMap getAreaCodeList(String strAreaName, String strAreaCode) throws RemoteException, Exception;
   
   HashMap getSpecificationDetail(long lSolutionId) throws RemoteException, Exception;
   
   HashMap getRegionList() throws RemoteException, Exception;
   
   String getDepartmentName(String strCode) throws RemoteException, Exception;
   
   String getRegionName(long lRegionId) throws RemoteException, Exception;
   
   HashMap getUbigeoList(int iDptoId,int iProvId,String sFlag)throws RemoteException, Exception;
   
   
   int getCustomerValue(long lCustomerId) throws RemoteException, Exception;
         
   int getCountCases(String strType, String strValue) throws RemoteException, Exception;   
   
   HashMap generateOrderExterna(RepairBean objRepairBean,String strLogin)throws RemoteException, Exception;   
   
   HashMap generateOrderInterna(RepairBean objRepairBean,String strLogin)throws RemoteException, Exception;   

   HashMap getImeiDetail(RepairBean objRepairBean) throws RemoteException, Exception;   
   
   HashMap getImeiDetailTab(RepairBean objRepairBean) throws RemoteException, Exception;

   String getFechaEmision(String strImei) throws RemoteException, Exception;       
    
   HashMap getGeneralOptionList(String strGeneralOption,long lValue) throws RemoteException, Exception; 
   
   HashMap getRepuestoDetail(long lObjectId,String strObjectType,String strLogin)throws RemoteException, Exception; 

   String getBuildingName(int iBuildingid)throws RemoteException, Exception;

   /* OCRUCES - N_O000046759 Control de Tipo de Operación vs Tipo de Orden*/

   public String getValidateTypOpe(String npconfigurationtype) throws RemoteException,Exception;

    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - FIN
     *  REALIZADO POR: Carmen Gremios
     *  FECHA: 13/09/2007
     ***********************************************************************
     ***********************************************************************
     **********************************************************************/
     
	/***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  EXCEPCIONES - INICIO
     *  REALIZADO POR: Jorge Pérez
     *  FECHA: 20/10/2007
     ***********************************************************************
     ***********************************************************************
     **********************************************************************/ 
    HashMap getServiceList(String[] pastrItemServIds,  boolean pbOrdenFromSalesRep)throws RemoteException, SQLException, Exception;
	
    HashMap getCustomerBillCycle(int piCustomerId, int piOrderId)throws RemoteException, SQLException, Exception;
	
    HashMap getAccessFee(int piAppId, String[] pastrItemPlanIds, String[] pastrItemServIds, boolean pbOrdenFromSalesRep)throws RemoteException, SQLException, Exception;
	
    HashMap getRentFee(String[] pastrItemProductIds, int iSpecId)throws RemoteException, SQLException, Exception;
	
    HashMap getServiceCost(int piPlanId, int piServiceId)throws RemoteException, Exception, SQLException;
    
    HashMap updateExceptionApprove(RequestHashMap objHashMap) throws RemoteException, Exception, SQLException;
    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  EXCEPCIONES - FIN
     *  REALIZADO POR: Jorge Pérez
     *  FECHA: 20/10/2007
     ***********************************************************************
     ***********************************************************************
     **********************************************************************/ 
  
	/***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  ITEMS DE SECCIONES DINAMICAS - INICIO
     *  REALIZADO POR: ISRAEL RONDON
     *  FECHA: 30/10/2007
     *  MODIFICADO POR:EVELYN OCAMPO
     *  FECHA: 04/02/2008
     *  SE INCLUYE COMO PARÁMETRO DE SALIDA LA DIRECCIÓN DE INSTALACIÓN 
     *  COMPARTIDA EN getSharedInstalation.
     ***********************************************************************
     ***********************************************************************
     **********************************************************************/  
	HashMap getSharedInstalation(AddressObjectBean objAddressObjB)throws RemoteException, SQLException,Exception;
  
	HashMap getAddress(int iswObjectId,String sTipoDirCustomer,String sSwObjectType,long lngSpecificationId)throws RemoteException, Exception;
  
	HashMap getAddressPuntual(int iswAddressId,String sAddresstype)throws RemoteException, Exception;  
  
	HashMap getValidateContract(long lngCustomerID, long lngContractId , long lngSpecificationId,long lngSolutionId, long lngInstallAddressId )throws RemoteException, Exception; 
   
  /***********************************************************************
	 ***********************************************************************
	 *  SUSCRIPCIONES - INICIO
	 *  REALIZADO POR: David Lazo de la Vega
	 *  FECHA: 01/10/2010
	 ***********************************************************************
	 **********************************************************************/
   
  //johncmb inicio  -- 
  HashMap getServiceList(int intSolutionId, int intPlanId, int intProductId, String strSSAAType, String strType) throws RemoteException, Exception;
  //johncmb fin--
  
  HashMap getOrderDeact(long intOrderId, String strPhone, long intServiceId)throws RemoteException, Exception; 
  
  HashMap getServiceActive(String strPhone, long lCustomerId, long lSpecificationId, long lSiteId) throws RemoteException, Exception;
  
  HashMap getSubscriptionList(String strprocessing, String strgroup) throws RemoteException, Exception;
  
  HashMap getItem(long lServicioId, String strPhone) throws RemoteException, Exception;
  
  HashMap getProcessTypeByOrderId(long lOrderId) throws RemoteException, Exception;
  
  /***********************************************************************
	 ***********************************************************************
	 *  SUSCRIPCIONES - FIN
	 *  REALIZADO POR: David Lazo de la Vega
	 *  FECHA: 01/10/2010
	 ***********************************************************************
	 **********************************************************************/

	/***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  ITEMS DE SECCIONES DINAMICAS - INICIO
     *  REALIZADO POR: ISRAEL RONDON
     *  FECHA: 30/10/2007
     ***********************************************************************
     ***********************************************************************
     **********************************************************************/
	 
	/***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES - INICIO
     *  REALIZADO POR: Richard De los Reyes
     *  FECHA: 07/11/2007
     ***********************************************************************
     ***********************************************************************
     **********************************************************************/
	public HashMap getCodigosJerarquiaVentasMap(String strLevel, String strCode, String strBusUnitId, long lVendedorId, int iFlagVendedor) throws SQLException, RemoteException, Exception;

	public HashMap getBuildingList(String strTipo) throws SQLException, RemoteException, Exception;

	public HashMap getKitDetail(long lKitId, String strModalidad, long lngSalesStructOrigenId) throws SQLException, RemoteException, Exception;

	public HashMap getPlanTarifarioNombre(long lPlanId) throws SQLException, RemoteException, Exception;
	
	public HashMap getModelList() throws SQLException, RemoteException, Exception;
	
	public HashMap getCodeSetList(String strCodeSet) throws SQLException, RemoteException, Exception;

  public HashMap getPackDetail(String strSim, String strPin) throws SQLException, RemoteException, Exception;
	
	public HashMap getGeneralOptionList(String strGeneralOption) throws SQLException, RemoteException, Exception;
	
	public HashMap getProcessList(String strEquipment) throws SQLException, RemoteException, Exception;
	
	public HashMap getDetalleReposicionByTelefono(String strTelefono) throws SQLException, RemoteException, Exception;
	
	public HashMap getRetailList() throws Exception, RemoteException, SQLException;
	
	public HashMap getInfoImei(String strImei) throws Exception, RemoteException, SQLException;
	/***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES - FIN
     *  REALIZADO POR: Richard De los Reyes
     *  FECHA: 07/11/2007
     ***********************************************************************
     ***********************************************************************
     **********************************************************************/
	 
	HashMap getDataNpTable(String strTableName,String strValueDesc) throws Exception, RemoteException, SQLException; // CEM COR0303

  HashMap getValidateSalesman(long lngCustomerId, long lngSiteId, long lngSalesmanId, String strVendedor,int iUserId,int iAppId) throws Exception, RemoteException, SQLException;

  String getValue(String strTable, String strValueDesc) throws  SQLException, Exception, RemoteException;

  HashMap getCheckPermission(long lngSellerId, long lngCustomerId, long lngSiteId) throws Exception, RemoteException, SQLException;
  
  HashMap getNpopportunitytypeid(long lOppontunityId) throws RemoteException, Exception;
  
  int getValidateAuthorization(long lOppontunityId, int iOppType, int iSalesID) throws RemoteException, Exception;
  
	HashMap selectResponsablePago(long lSpecificationId, String strOrigenOrden,long lValor,String strOpcion,long lngCustomerId,String strTypeCustomer) throws RemoteException,SQLException, Exception;
  
  HashMap getObjectTypeUrl(String strObjectType)throws RemoteException, Exception, SQLException;
  
  HashMap getRolMult(String strScreenOption, long iUserid, int iAppid)throws RemoteException, Exception, SQLException;
  
	HashMap getUserInboxByLogin(String strLogin) throws RemoteException,SQLException, Exception;  
  
  HashMap getNameServerReport(String strCategory, String strParameterName) throws RemoteException,SQLException, Exception;
  
  HashMap getSubGirosByIndustry(long lGiroId) throws RemoteException,SQLException, Exception;

  HashMap getDescriptionByValue(String strValue, String strTable) throws  SQLException, Exception, RemoteException;

  HashMap getDominioList(String dominioTabla) throws  SQLException, Exception, RemoteException;

  HashMap getRepairConfiguration(String av_param) throws  SQLException, Exception, RemoteException;

  HashMap getRatePlanList() throws  SQLException, Exception, RemoteException;

  HashMap getUbigeoList(UbigeoBean objUbigeoBean) throws  SQLException, Exception, RemoteException;

  HashMap getGiroList() throws  SQLException, Exception, RemoteException;

  HashMap getSubGirosByGiroList(long lGiroId) throws  SQLException, Exception, RemoteException;

  HashMap getKitsList(String strTiendaRetail) throws SQLException, Exception, RemoteException;

  HashMap getComboReparacionList(String strNombreOpcion) throws  SQLException, Exception, RemoteException;

  HashMap getEstadoOrdenList() throws  SQLException, Exception, RemoteException;

  HashMap getDivisionList() throws  SQLException, Exception, RemoteException;

  HashMap getSolucionList(long lDivisionId) throws  SQLException, Exception, RemoteException;

  HashMap getCategoryList(long lSolutionId) throws  SQLException, Exception, RemoteException;

  HashMap getSubCategoryList(String strCategoria, long lSolutionId) throws  Exception,  SQLException, RemoteException;

  HashMap getZoneList(long lBizUnitId) throws  SQLException, Exception, RemoteException;

  HashMap getComboRegionList() throws  Exception,  SQLException, RemoteException;
  
  int getPermissionDetail(long lOrderId, long iUserid, int iAppid) throws RemoteException, Exception;
  
  HashMap getValueNpTable(String strTableName) throws RemoteException,SQLException, Exception;
  
  HashMap getFormatBySpecification(long lSpecificationId) throws RemoteException,SQLException, Exception;

  HashMap getApplicationList(String strLogin) throws  Exception, RemoteException, SQLException;
  
  HashMap getSalesmanName(long lSpecificationId) throws RemoteException,SQLException, Exception;

  HashMap getListaEquipPendRecojo(long customerId, long siteId) throws RemoteException,SQLException, Exception;  
  
  HashMap getResolution(String strFabricatorId) throws Exception, RemoteException, SQLException;
  
  HashMap getDiagnosis(String intProviderId) throws Exception, RemoteException, SQLException;
  
  //HashMap getFailureList(int intValue) throws Exception, RemoteException, SQLException;
  
  HashMap getFailureList(int intValue, int intRepairId, String strRepairTypeId) throws Exception, RemoteException, SQLException;
 
  String getDiagnosisDescription(int intValue) throws Exception, RemoteException, SQLException; 
     
  //HashMap getSparePartsListByModel(String strValue) throws Exception, RemoteException, SQLException; 
  
  HashMap getSparePartsListByModel(String strValue, int intRepairId, String strRepairTypeId) throws Exception, RemoteException, SQLException;
  
  HashMap getSelectedFailureList(int intValue, String strRepairTypeId) throws Exception, RemoteException, SQLException;
  
  public HashMap getTypeServices() throws SQLException, RemoteException, Exception;
  
  public HashMap getloadServices(String strTipoReparacion) throws SQLException, RemoteException, Exception;
  
  HashMap newOrderReparation(HashMap objHashMap) throws Exception, RemoteException, SQLException;  
  
  HashMap getSelectedSpareList(int intValue, String strRepairTypeId) throws Exception, RemoteException, SQLException;
 
  HashMap/*String*/ validateTrueBounce(String strImei,long intFallaid, String strBounce) throws Exception, RemoteException, SQLException;

  HashMap getRepairCount(String strValue) throws SQLException, RemoteException, Exception;    
  
  HashMap getSmallRepairDetail(String strValue) throws SQLException, RemoteException, Exception;
  
  HashMap getNpRepairBOF(int intValue, String strLogin) throws SQLException, RemoteException, Exception;
  
  HashMap activateEquipment(String strImei,String strImeiNuevo, String strSim, String strSimNuevo, String strReplaceType) throws SQLException, RemoteException, Exception;
    
  HashMap getRepairid(String strImei) throws RemoteException, Exception;

  HashMap GenerateInvDoc(HashMap hshParameters) throws SQLException, RemoteException, Exception; 
  
  public HashMap getServices() throws SQLException, RemoteException, Exception;
  
  HashMap getAccesories() throws SQLException, RemoteException, Exception;
  
  HashMap getAccesoryModels(String strAccesoryType) throws SQLException, RemoteException, Exception;
  
  HashMap getDocGenerate(String strImei, long strRepairId ) throws RemoteException, Exception;        
  
  HashMap getDiagnosisLevel(String strProviderId, int intLevel) throws Exception, RemoteException, SQLException;
 
  HashMap getDocGenClose(String strImei, long strRepairId ) throws RemoteException, Exception;        
  
  HashMap validateStock(long intFallaid, long  intSpecification , String strLogin) throws Exception, RemoteException, SQLException;
  
  HashMap getValueTag1(String strValue, String strTable) throws SQLException, RemoteException, Exception;
    

  HashMap getValidatePhoneVoIp(long lngCustomerID, String strPhoneNumber , long lngSpecificationId,long lngSolutionId, long lngInstallAddressId )throws SQLException,RemoteException, Exception;     
  
  //Modificado: Daniel Gutierrez Tagle  --- Se agrego el parametro strServiceMsjId para obtener el id del servicio de mensajeria -- 23/09/2010
   HashMap getPermissionServiceDefault(long lspecificationid, String strObjectItem, String strModality, int iModelId, int iProductId,String strServiceMsjId) throws SQLException, RemoteException, Exception;
  
  ServiciosBean getDetailService(long lserviceid) throws SQLException, RemoteException, Exception;
  /**CBARZOLA - Cambio de Modelo - Distintas Tecnologias 19/07/2009**/
  HashMap getStatusByTable(String strTable, String strValue) throws SQLException,RemoteException,Exception;
  
  /*Inicio Data*/
  HashMap getSalesDataList(long lngRule) throws SQLException, RemoteException, Exception;
  HashMap getSalesDataShow(long lngSpecificationId, String strObjectType, long lngObjectId)throws SQLException, RemoteException, Exception;
  HashMap validateSalesExclusivity(long lngCustomerId, long lngSiteId, long lngWinnerTypeId, long lngSalesmanId)throws SQLException, RemoteException, Exception;
  HashMap getOrderSellersList(long lNpOrderid) throws SQLException, RemoteException, Exception;
  HashMap getOrderSellerByType(long lNpOrderid, long lngType)throws SQLException, RemoteException, Exception;
  HashMap validateStructRule(long lngSalesRuleId, long lngSalesStrucId)throws SQLException, RemoteException, Exception;
  HashMap validateIncident(long lngGeneratorId, String strGeneratorType, long lngSpecification, long lngUserId, int iAppId, long lngCustomerId, long lngSiteId, long lngWinnerTypeId)throws SQLException, RemoteException, Exception;
  /*Fin Data*/

/**MWONG - Lista de stock en tiendas 31/08/2009**/
  HashMap getStockPorModelo(String strModel) throws SQLException, RemoteException, Exception;
  HashMap getValidationSalesManProposed(long lUserId,long lSalesManId)throws SQLException, RemoteException, Exception;
  
  HashMap validateSim(String strSim) throws SQLException, RemoteException, Exception;
  String getReplaceType(String orderId) throws SQLException, RemoteException, Exception;
  String getValidateActiveEquipment(String imei, String sim, String replaceType) throws SQLException, RemoteException, Exception;

  HashMap GeneralDAOgetConsultor(long lUserId) throws  SQLException, Exception, RemoteException;

  HashMap GeneralDAOgetAmbitSalesList(String codType, int regionId) throws  SQLException, Exception, RemoteException;
  
  HashMap getSalesDataShow(long lngSpecificationId, String strObjectType, long lngObjectId, long lngCustomerId, long lngSiteId)throws SQLException, Exception, RemoteException;
  
  String getValidInternetNextel(long orderId) throws SQLException, Exception, RemoteException;
  
  HashMap getFinalEstateList() throws SQLException, Exception, RemoteException;
  
  HashMap getEquipSOList() throws SQLException, Exception, RemoteException;
  
  int validateChangeProcess(long orderId) throws SQLException, Exception, RemoteException;
  
  HashMap validateSpecOrders(HashMap hshParameters) throws SQLException, Exception, RemoteException;
    
  String getTechnologyByImei(String imei) throws SQLException, Exception, RemoteException;
  
  /**
      Method : getValueByConfiguration
      Purpose: 
      Developer       		Fecha       Comentario
      =============   		==========  ======================================================================
      DGUTIERREZ     	   11/08/2010  Creación 
       */
  
  HashMap getValueByConfiguration(String configuration) throws SQLException, Exception, RemoteException;
   
  HashMap GeneralDAOgetNpConfValues(String strNpConfiguration, String strValue, String strValueDesc, String strNpStatus, String strNpTag1, String strNpTag2, String strNpTag3) throws  SQLException, Exception, RemoteException;

  HashMap ProductDAOgetValidateSimImei(String strNumber) throws  SQLException, Exception, RemoteException;

  //johncmb inicio  -- 

  HashMap getServiceGroupLst() throws RemoteException, Exception;
  //johncmb fin



   // INICIO - HP Argentina - TIPOS DISPOSITIVO -  PROYECTO HP-PTT
   /**
   * Motivo: getTipoEquipoList
   * <br>Realizado por: <a href="mailto:marcelo.cejas-echeverria@hp.com">Marcelo Cejas Echeverria</a>
   * <br>Fecha: 30/09/2010 
   */  
    HashMap getTipoEquipoList() throws SQLException, Exception, RemoteException;
  
  
   /**
   * Motivo: getModelsByTypeOfEquipment
   * <br>Realizado por: <a href="mailto:marcelo.cejas-echeverria@hp.com">Marcelo Cejas Echeverria</a>
   * <br>Fecha: 30/09/2010 
   */  
    HashMap getModelsByTypeOfEquipment(int deviceTypeId) throws SQLException, Exception, RemoteException;
  // FIN - HP Argentina - TIPOS DISPOSITIVO -  PROYECTO HP-PTT

   HashMap getSalesStructOrigen(long lSalesStructId, String strCanal) throws  SQLException, Exception, RemoteException;
  
   HashMap getSalesStructOrigenxRetail(long lRetailId) throws  SQLException, Exception, RemoteException;

   int get_AttChannel_Struct(int intSalesstructid) throws Exception, RemoteException;
   
   /**
   * Motivo: doSaveLogItem
   * <br>Realizado por: <a href="mailto:frank.picoy@hp.com">Frank Picoy</a>
   * <br>Fecha: 09/01/2012 
   */  
   String doSaveLogItem(HashMap objLogItem) throws SQLException, Exception, RemoteException;

   HashMap getConfigurationInstallment(String npvaluedesc)  throws  SQLException, Exception, RemoteException;
   
   String getURLRedirect(int appId, int userId, int orderId,String tab) throws SQLException,CustomException, Exception;
   
   HashMap getConfigurations(String strParam) throws Exception;
   
   HashMap getComboTiendaList() throws Exception;
  
  HashMap getDataLoanForReport(String repairId) throws SQLException, RemoteException, Exception;
  
  HashMap getDamageList(String repairListId, String model) throws SQLException, RemoteException, Exception;
  
   //JRAMIREZ 21/07/2014 Tienda Express
   public int getOrderExist(int nporderid) throws SQLException, Exception;
   
   //JRAMIREZ 21/07/2014 Tienda Express
   public int getBuildingidByOrderid(int nporderid) throws SQLException, Exception;

  //  ocruces N_O000046759 Control de Tipo de Operación vs Tipo de Orden
  HashMap getTypeOpeSpecifications(String strCustomerId) throws  SQLException, Exception, RemoteException;
  //EFLORES Requerimiento PM0010274
  String verifyUserCanSeeCustomer(String userId,String objectType,String objectId,String typeMessage) throws  SQLException, Exception, RemoteException;


    /*
    Motivo: Validación de Cantidad de Items Registrados por Ordenes
    Realizado Por: Eduardo Peña Vilca EPV
    Fecha: 18/06/2015

    */
    HashMap getValueLimitModel(String strTable,String strSubCategoria) throws SQLException, Exception;


    // ADT-BCL-083 Numeros en blacklist
    HashMap getPhoneBlackList(int strnpSite,int strCustomerId,int type) throws  SQLException, Exception, RemoteException;

    //ADT-BCL-083 Validacion de cantidad de productos
    HashMap getValidateCountUnitsBolCel(int specification,int productOrigen, int productDestino) throws  SQLException, Exception;

    //  ocruces N_O000046759 Control de Tipo de Operación vs Tipo de Orden
  ArrayList getTipoDocumentoList() throws  SQLException, Exception, RemoteException;

   // epena
   ArrayList getConditionsReport(String npTable) throws  SQLException, Exception, RemoteException;

   //FBERNALES Requerimiento PM0010503 Validacion del numero de solicitud en Retail
    public Boolean validateNumSolicitudRetail(String sNumeroSolicitud,int iFlagColumnOrderNumber) throws  SQLException, Exception, RemoteException;

    //JQUISPE PRY-0762 Obtiene la cantidad de Renta Adelantada
    public HashMap getCantidadRentaAdelantada(int codigoProducto,int codigoPlan)throws  SQLException, Exception, RemoteException;
    
    //JQUISPE PRY-0762 Obtiene el precio del plan
    public HashMap getPrecioPlan(int piAppId, String strItemPlanId) throws  SQLException, Exception, RemoteException;


    //[PRY-0710] EFLORES Determina si existe de la tabla swbapps.np_table un registro para el campo npvalue con la combinacion  nptable,npvaluedesc
    //0: No encontro
    //1: Encontro
    public int validateIfNpvalueIsInNptable(String npTable,String npValueDesc,String npValue) throws SQLException,Exception;

   //DERAZO PRY-0721 Obtiene la lista de regiones habilitadas por producto
   public HashMap getEnabledRegions(String strProductId) throws SQLException, Exception, RemoteException;

    //EFLORES BAFI2
    public HashMap getListProvinceBAFI(String strProductId,String strNpProvinceId) throws SQLException, Exception, RemoteException;
    public HashMap getListDistrictBAFI(String strProductId,String strNpDistrictId) throws SQLException, Exception, RemoteException;

    //DERAZO REQ-0940
    public HashMap getTraceabilityConfigurations() throws SQLException, Exception, RemoteException;

    //DERAZO REQ-0940
    public HashMap getValidateShowContacts(long orderid) throws SQLException, Exception, RemoteException;

    //EFLORES PRY-1112
    public HashMap getExcludingAditionalServices() throws SQLException, Exception;
    
    //JCURI PRY-1093
    public HashMap lugarDespachoDeliveryList() throws SQLException, Exception;
}
