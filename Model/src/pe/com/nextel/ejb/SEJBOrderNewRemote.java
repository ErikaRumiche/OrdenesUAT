package pe.com.nextel.ejb;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import javax.ejb.EJBObject;
import javax.servlet.GenericServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pe.com.nextel.bean.ItemBean;
import java.util.HashMap;
import pe.com.nextel.bean.ItemDeviceBean;
import pe.com.nextel.bean.ItemServiceBean;
import pe.com.nextel.bean.OrderBean;
import pe.com.nextel.bean.PlanTarifarioBean;
import pe.com.nextel.bean.ProductBean;
import pe.com.nextel.bean.RepairBean;
import pe.com.nextel.bean.ServiciosBean;
import pe.com.nextel.util.RequestHashMap;

public interface SEJBOrderNewRemote extends EJBObject {

    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - INICIO
     *  REALIZADO POR: Rosales Lee
     *  FECHA: 28/08/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/

    ArrayList OrderDAOgetSolutionList(String idSolution) throws Exception, SQLException,RemoteException;

    ArrayList OrderDAOgetCategoryList(int idSolution) throws Exception, SQLException,RemoteException;

    HashMap CategoryDAOgetSpecificationData(int idSpecification, String strGeneratorType) throws RemoteException, Exception, SQLException;

    ArrayList ItemDAOgetHeaderSpecGrp(int idSpecificationId) throws RemoteException, SQLException, Exception;

    ArrayList ItemDAOgetItemHeaderSpecGrp(int idSpecificationId) throws RemoteException, SQLException, Exception;

    Hashtable OrderDAOgetOrderIdNew() throws Exception, RemoteException, SQLException;

    ArrayList OrderDAOgetSalesList(int intUserId, int intAppId) throws Exception, RemoteException, SQLException;

    HashMap  OrderDAOgetDispatchPlaceList(int intSpecialtyId) throws RemoteException, SQLException, Exception;

    HashMap OrderDAOgetBuildingName(int intBuildingid, String strLogin) throws RemoteException, SQLException, Exception;

    //JLIMAYMANTA,RETORNA EL IDENTIFICADOR DE TIEMDA EXPRESS
    HashMap OrderDAOgetBuildingTS(int intBuildingid, String strLogin) throws RemoteException, SQLException, Exception;

    ArrayList OrderDAOgetModePaymentList(String strParamName, String strParamStatus) throws Exception, RemoteException, SQLException;

    HashMap OrderDAOgetModalityList(long intSpecificationId, String strEquipment, String strWarrant, String strEquipmentReturn) throws RemoteException, Exception, SQLException;

    ArrayList ProductLineDAOgetProductLineValueList(int iProductLineId, String strMessage) throws RemoteException, SQLException, Exception;

    ArrayList ProductDAOgetProductList(ProductBean productBean, String strMessage) throws RemoteException, SQLException, Exception;

    //ArrayList PlanDAOgetPlanList(PlanTarifarioBean planTarifarioBean, String type, String strMessage) throws RemoteException, SQLException;
    HashMap PlanDAOgetPlanList(PlanTarifarioBean planTarifarioBean, String type) throws RemoteException, SQLException, Exception;

    ArrayList EquipmentDAOgetProductList(String ownhandset, String consignmen, String strMessage) throws RemoteException, SQLException, Exception;

    String SpecificationDAOgetConsigmentValue(int intSpecificationID, String strMessage) throws RemoteException, SQLException, Exception;

    ArrayList ServiceDAOgetServiceAllList(int intSolutionId, String strMessage) throws RemoteException, SQLException, Exception;

    ArrayList ServiceDAOgetServiceDefaultList(String strObjectType, int intSpecificationId, String strMessage) throws RemoteException, SQLException, Exception;

    ArrayList OrderDAOgetSubCategoryList(String strCategory) throws RemoteException, SQLException, Exception;

    HashMap OrderDAOgetOrderInsertar(OrderBean orderBean,Connection conn) throws Exception,RemoteException, SQLException;

    String ItemDAOgetItemInsertar(ItemBean itemBean,Connection conn) throws Exception,RemoteException, SQLException;

    String ItemDAOgetItemServiceInsertar(ItemServiceBean itemServiceBean,Connection conn) throws Exception,RemoteException, SQLException;

    HashMap ItemDAOgetItemOrder(long nporderid) throws Exception,SQLException, RemoteException;

    String ItemDAOgetItemOrderDelete(ItemBean itemBean,Connection conn) throws Exception,RemoteException, SQLException;

    String ItemDAOgetItemUpdate(ItemBean itemBean,Connection conn) throws Exception,RemoteException, SQLException;

    String ItemDAOgetItemInsertDevices(ItemDeviceBean itemDeviceBean,String strNextInbox,Connection conn) throws Exception,RemoteException, SQLException;

    ArrayList  ItemDAOgetItemDeviceOrder(long intOrderId) throws RemoteException, SQLException;

    ServiciosBean ServiceDAOgetServiceDescription(int intServicioId, String strMessage) throws RemoteException, SQLException, Exception;

    String ItemDAOgetItemImeiAssignementBADelete(ItemBean itemBean,Connection conn) throws Exception, RemoteException, SQLException;

    HashMap doSaveOrder(RequestHashMap objHashMap) throws Exception, RemoteException;

    HashMap SiteDAOgetSiteSolicitedList(long longNpOrderId) throws Exception,SQLException,RemoteException;

    HashMap SiteDAOgetSiteExistsList(long longNpCustomerId,String strObjectType) throws Exception,SQLException,RemoteException;

    HashMap SpecificationDAOgetSpecificationUserList(long customerId, String strLogin, String strGeneratorType, String strOpportunityTypeId, String strFlagGenerator, long lngDivisionId, long lngSpecificationId, long lngGeneratorId ) throws  SQLException, Exception, RemoteException;

    HashMap SpecificationDAOgetSpecificationUserList(long userId, long lngDivisionId, long lngSpecificationId, String strObjectType ,String strFlagGenerator ) throws  SQLException, Exception, RemoteException;

    HashMap OrderDAOgetAddendasList(int id_prom, int id_plan, int id_specification, int id_kit) throws RemoteException,SQLException,Exception;

    String OrderDAOgetNpAllowAdenda(int id_specification) throws RemoteException,SQLException,Exception;

    String OrderDAOgetValidationAdenda(int id_specification) throws RemoteException,SQLException,Exception;

    HashMap OrderDAOgetTemplateOrder(int id_order, int id_item) throws RemoteException,SQLException,Exception;

    HashMap OrderDAOgetProductPriceType(ProductBean objProductBean) throws RemoteException,SQLException,Exception;

    HashMap OrderDAOgetNumAddendumAct(int id_customer, String id_num_nextel) throws RemoteException,SQLException,Exception;

    HashMap OrderDAOgetNumAddendumActSpec(int id_customer, String id_num_nextel, String id_specification) throws RemoteException,SQLException,Exception;

    HashMap ProductLineDAOgetProductLineSpecList(long longSolutionId, long longSpecificationId, String strObjectType, long longProductLineId) throws RemoteException, Exception, SQLException;

    HashMap ProductDAOgetDetailByPhone(String strPhoneNumber,long longCustomerId,long longSiteId,long lSepecificationId) throws RemoteException, Exception, SQLException;

    HashMap ProductDAOgetDetailByPhoneBySpecification(String strPhoneNumber,long longCustomerId,long longSiteId,long lSpecificationId,long lOrderId) throws RemoteException, Exception, SQLException;

    HashMap ProductDAOgetProductDetailImei(String strImei, long longCustomerId,long lSpecificationId) throws Exception, RemoteException, SQLException;

    HashMap ProductDAOgetDetailByImeiBySpecification(String strImei,long longCustomerId,long lSpecificationId,String strModalitySell, String strFlagMigration) throws RemoteException, Exception, SQLException; //[TDECONV003-1] EFLORES Agrega parametro

    HashMap getResponsibleAreaList() throws Exception, RemoteException, SQLException;

    HashMap ItemDAOhasPaymentOrderId (long nporderid) throws SQLException, RemoteException, Exception;
    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - FIN
     *  REALIZADO POR: Rosales Lee
     *  FECHA: 03/12/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/

    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - INICIO
     *  REALIZADO POR: Richard De los Reyes
     *  FECHA: 28/08/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/

    HashMap getRepairReplaceList(String strRepairId) throws SQLException, RemoteException, Exception;

    HashMap generateDocument(HashMap hshParametrosMap) throws SQLException, RemoteException, Exception;

    HashMap reportRepair(String strReportName, long lRepairId) throws SQLException, RemoteException, Exception;

    HashMap getLastImeiRepair(long lRepairId) throws SQLException, RemoteException, Exception;

    HashMap getResponsibleDevList() throws Exception, RemoteException, SQLException;

    HashMap getProductType(ProductBean objProductBean) throws  Exception, RemoteException, SQLException;

    HashMap getProductBolsa(long lngCustomerId,long lngSiteId) throws Exception, RemoteException, SQLException;

    HashMap getBolsaCreacionN2(long lngCustomerId,long lngSiteId) throws Exception, RemoteException, SQLException;

    HashMap getProductDetail(long longProductId) throws Exception, RemoteException, SQLException;

    HashMap getSpecificationDate(long lngSpecificationId, String strBillcycle) throws Exception, RemoteException, SQLException;
    HashMap getFlagEmail(long lngSpecificationId, long lngHdnIUserId) throws Exception, RemoteException, SQLException;//jtorresc 09/12/2011

    HashMap getTableValue(String strNameTable) throws Exception, RemoteException, SQLException;

    /**
     * @see pe.com.nextel.dao.ItemDAO#doValidateIMEI(HashMap, Connection)
     */
    String doValidateIMEI(HashMap hshItemDeviceMap) throws SQLException, RemoteException, Exception;

    /**
     * @see pe.com.nextel.dao.ItemDAO#doValidateIMEI(HashMap, Connection)
     */
    HashMap getInboxGenerateGuide() throws SQLException, RemoteException, Exception;

    HashMap getServiceRentList(long lngPlanId) throws Exception, RemoteException, SQLException;

    HashMap getModelList() throws RemoteException,Exception,SQLException;


    HashMap getModelListByCategory(int specId) throws RemoteException,Exception,SQLException;

    HashMap getProductLineDetail(long lngProductLineId) throws Exception, RemoteException, SQLException;

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

    HashMap doValidateIMEI(String strIMEI)
            throws RemoteException,Exception;

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

    public HashMap doValidateOwnEquipment(String strIMEI)
            throws RemoteException,Exception;

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
     *  VALIDACION DE STOCK - INICIO
     *  REALIZADO POR: Jorge Pérez
     *  FECHA: 03/03/2008
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/
    /**
     * @see pe.com.nextel.dao.ProductDAO#getValidateStock(int idSpecification)
     */
    HashMap getValidateStock(int idSpecification, int iDispatchPlace)throws Exception, RemoteException, SQLException;
    /**
     * @see pe.com.nextel.dao.ProductDAO#getStockMessage(int idSpecification, long lProductId, int iBuildingid, String strSaleModality,  long lSalesStructOrigenId, String strTipo)
     */
    HashMap getStockMessage(int idSpecification, long lProductId, int iBuildingid, String strSaleModality, long lSalesStructOrigenId, String strTipo)throws Exception, RemoteException, SQLException;


    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  VALIDACION DE STOCK - FIN
     *  REALIZADO POR: Jorge Pérez
     *  FECHA: 03/03/2008
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/

    /**
     * @see pe.com.nextel.dao.ItemDAO#doValidateSIM(String strSIM)
     */
    HashMap doValidateSIM(String strSIM) throws Exception, RemoteException, SQLException;

    long getUnkownSiteIdByOportunity(long lngOportunityId) throws Exception, RemoteException, SQLException;

    HashMap getCustSiteIdByOportunity(long lngOportunityId) throws Exception, RemoteException, SQLException;

    /**
     * @autor Opdubock
     * @throws java.sql.SQLException
     * @throws java.rmi.RemoteException
     * @throws java.lang.Exception
     * @return retorna mensaje con la validación de la fecha firma
     * @param tstmFechaFirma
     * @param lngOrderId
     */

    HashMap getValidateFechaFirma(long lngOrderId, String strFechaFirma)throws Exception, RemoteException, SQLException;

    HashMap generateDocumentRepair(HashMap hshParametrosMap) throws SQLException, RemoteException, Exception;

    HashMap getTipoPlantillaAdenda(String strNumeroNextel, int iTemplateId) throws Exception, RemoteException, SQLException;

    String getValidateAdministrator(long lOrderId) throws Exception, RemoteException, SQLException;

    HashMap getRepairByOrder(long lOrderId) throws  Exception, RemoteException, SQLException;

    HashMap getCodEquipFromImei(String strImei) throws  Exception, RemoteException, SQLException;

    HashMap getAllowedSpecification(long lspecificationId, long lcustomerid) throws  Exception, RemoteException, SQLException;

    String valImeiPrestamoCambio(RepairBean objRepairBean,HashMap hshParametrosMap) throws  Exception, RemoteException, SQLException;

    // PCASTILLO - Despacho en Tienda - Validación de Stock
    HashMap valStockPrestCambio(RepairBean objRepairBean,HashMap hshParametrosMap) throws  Exception, RemoteException, SQLException;

    HashMap getNoteCount(long lOrderId) throws  Exception, RemoteException, SQLException;

    HashMap doValidateMassiveSim(long lngCustomerId, long lngSpecificationId, String strModality, String[] strSim)  throws  Exception, SQLException, RemoteException;

    /**
     * Motivo : VALIDACION DE COMISSION
     * @see pe.com.nextel.dao.ProductDAO#getComissionMessage(int intServicioId)
     * REALIZADO POR: Ruth Polo
     * FECHA: 13/01/2009
     */
    HashMap getComissionMessage(int intServicioId)throws Exception, RemoteException, SQLException;

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
     * <br>Fecha: 22/04/2009
     * @see pe.com.nextel.dao.SpecificationDAO#getSolutionSpecificationList(long,long)
     ************************************************************************************************************************************/
    HashMap getSolutionSpecificationList(long lngSpecificationId,long lngDivisionId, long lngSiteId) throws  Exception, RemoteException,SQLException;

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
     * <br>Fecha: 22/04/2009
     * @see pe.com.nextel.dao.SpecificationDAO#getSolutionType()
     ************************************************************************************************************************************/
    HashMap getSolutionType() throws  Exception, RemoteException,SQLException;

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:jose.casas@nextel.com.pe">José Casas</a>
     * <br>Fecha: 16/06/2009
     * @see pe.com.nextel.ejb.SEJBOrderNewRemote#getFistStatus()
     ************************************************************************************************************************************/

    HashMap getFistStatus(RequestHashMap objHashMap) throws Exception, RemoteException, SQLException;

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:rensso.martinez@nextel.com.pe">Rensso Martinezr</a>
     * <br>Motivo: Valida los que un contrato no este suspendido mas de 60 días.
     * <br>Fecha: 24/06/2009
     * @see pe.com.nextel.dao.ProductDAO#validaDiasSuspension(String, String, String)
     ************************************************************************************************************************************/
    HashMap ProductDAOvalidaDiasSuspension(String strPhoneNumber, long lSpecificationId, String strNpScheduleDate, String strNpScheduleDate2) throws  Exception, RemoteException,SQLException;

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:rensso.martinez@nextel.com.pe">Rensso Martinez</a>
     * <br>Motivo: Obtiene el listado de los Estados del Item para las Suspensiones definitivas.
     * <br>Fecha: 28/06/2009
     * @see pe.com.nextel.dao.ItemDAO#getStatusItemList(String)
     ************************************************************************************************************************************/
    HashMap  ItemDAOgetStatusItemList(String nameTable, String nptag1, String nptag2) throws RemoteException, SQLException, Exception;

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
     * <br>Fecha: 22/04/2009
     * @see pe.com.nextel.dao.SpecificationDAO#getProductServiceDefaultList()
     ************************************************************************************************************************************/
    HashMap getProductServiceDefaultList (long lspecificationid,int iProductId,int iplanId,int iPermission_alq, int iPermission_msj) throws  Exception, RemoteException,SQLException;
    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:cesar.barzola@nextel.com.pe">Cesar Barzola</a>
     * <br>Fecha: 19/07/2009
     * @see pe.com.nextel.dao.OrderDAO#getOthersSolutionsbySubMarket()
     ************************************************************************************************************************************/
    HashMap getOthersSolutionsbySubMarket(long lspecificationId, long lsolutionId) throws SQLException,RemoteException,Exception ;

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:reinhard.arana@nextel.com.pe">Reinhard Arana</a>
     * <br>Fecha: 21/07/2009
     * @see pe.com.nextel.dao.ProductDAO#getProductBolsa(long,long,long)
     ************************************************************************************************************************************/
    HashMap getProductBolsa(long lngCustomerId,long lngSiteId, long lngSolutionId) throws Exception, RemoteException, SQLException;

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:cesar.barzola@nextel.com.pe">Cesar Barzola</a>
     * <br>Fecha: 21/07/2009
     * @see pe.com.nextel.dao.ProposedDAO#getProposedList(Long,Long,Long,Long)
     ************************************************************************************************************************************/
    HashMap  getProposedList(long lCustomerId,long lSite,long lSpecificationId,long lSellerId) throws SQLException,RemoteException,Exception ;
    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:cesar.barzola@nextel.com.pe">Cesar Barzola</a>
     * <br>Fecha: 06/08/2009
     * @see pe.com.nextel.dao.ProposedDAO#getValidationProposed(String,String)
     ************************************************************************************************************************************/
    HashMap   getValidationProposed(long lOrderId, long lProposedId,long lCustomerId,long lSpecification,long lSellerId,String strTrama) throws SQLException,RemoteException,Exception ;

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:cesar.barzola@nextel.com.pe">Cesar Barzola</a>
     * <br>Fecha: 12/07/2009
     * @see pe.com.nextel.dao.ServiceDAO#getCoreService_by_Plan(long)
     ************************************************************************************************************************************/
    HashMap ServiceDAOgetCoreServicebyPlan(long lplanId) throws Exception, RemoteException,SQLException ;

    HashMap getOriginalPlan(PlanTarifarioBean planTarifarioBean) throws RemoteException, SQLException, Exception;


    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
     * <br>Fecha: 02/11/2010
     * @see pe.com.nextel.dao.OrderDAO#getValidateBlacklist()
     ************************************************************************************************************************************/
    HashMap getValidateBlacklist (long lOrderId) throws  Exception, RemoteException,SQLException;



    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:frank.picoy@hp.com">Frank Picoy</a>
     * <br>Fecha: 30/09/2010
     * @see pe.com.nextel.dao.ProductDAO#getProductModelList(ProductBean)
     ***********************************************************************************************************************************/
    HashMap getProductModelList(ProductBean objProductBean) throws  SQLException, Exception, RemoteException;

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:frank.picoy@hp.com">Frank Picoy</a>
     * <br>Fecha: 08/10/2010
     * @see pe.com.nextel.dao.ProductDAO#getProductPlanList(ProductBean)
     ***********************************************************************************************************************************/
    HashMap getProductPlanList(ProductBean objProductBean) throws  SQLException, Exception, RemoteException;

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:frank.picoy@hp.com">Frank Picoy</a>
     * <br>Fecha: 08/10/2010
     ************************************************************************************************************************************/
    HashMap getValidateServSelectedList(String strServices, String strServicesDesc, String strPlanId, String strProductId) throws  Exception, RemoteException, SQLException;

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:ronald.huacani@asistp.com">Ronald Huacani</a>
     * <br>Fecha: 15/10/2010
     ************************************************************************************************************************************/
    HashMap getNumberGolden(String sCodApp, long lDnType, long lNpcode, String sDnNum, long lTmCode, String sExcluded, String sQuantity, String sPortabilidad) throws   SQLException, Exception, RemoteException;

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:frank.picoy@hp.com">Frank Picoy</a>
     * <br>Fecha: 15/11/2010
     ************************************************************************************************************************************/
    HashMap transferExtendedGuarantee(String strImei,String strImeiNuevo) throws  Exception, RemoteException, SQLException;

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:cesar.lozza@hp.com">César Lozza</a>
     * <br>Fecha: 09/11/2010
     ************************************************************************************************************************************/
    HashMap evaluateOrderVolume(int customerId, int specificationId, String typeWindow, List itemsList) throws RemoteException, Exception, SQLException;

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:frank.picoy@hp.com">Frank Picoy</a>
     * <br>Fecha: 23/11/2011
     ************************************************************************************************************************************/
    HashMap getServiceDefaultListByPlan(long lPlanId) throws  Exception, RemoteException, SQLException;

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:frank.picoy@hp.com">Frank Picoy</a>
     * <br>Fecha: 05/12/2011
     ************************************************************************************************************************************/
    String getTypePlan(long lPlanId) throws  Exception, RemoteException, SQLException;


    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:gguanilo@hp.com">Juan Guanilo</a>
     * <br>Fecha: 19/11/2012
     ************************************************************************************************************************************/
    HashMap doSaveVep(RequestHashMap objHashMap)throws Exception;

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:rensso.martinez@hp.com">Rensso Martinez</a>
     * <br>Fecha: 28/11/2012
     ************************************************************************************************************************************/
    HashMap doDeleteVep(RequestHashMap objHashMap)throws Exception;

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:rensso.martinez@hp.com">Rensso Martinez</a>
     * <br>Fecha: 28/11/2012
     ************************************************************************************************************************************/
    HashMap doValidateExistVep(RequestHashMap objHashMap)throws Exception;

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:frank.picoy@hp.com">Frank Picoy</a>
     * <br>Fecha: 20/11/2013
     ************************************************************************************************************************************/
    HashMap doValidateChangePlanToEmployee(RequestHashMap objHashMap)throws Exception;

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:lhuapaya@practiaconsulting.com">Luis Huapaya</a>
     * <br>Fecha: 30/07/2015
     ************************************************************************************************************************************/
    HashMap getBolsaCelulares(long idSite,long customerId ,long productLine)throws  SQLException, Exception, RemoteException;

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:lhuapaya@practiaconsulting.com">Luis Huapaya</a>
     * <br>Fecha: 30/07/2015
     ************************************************************************************************************************************/
    HashMap getAllProductBCL(long idSite,long idCustomer)throws  SQLException, Exception, RemoteException;

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:lhuapaya@practiaconsulting.com">Luis Huapaya</a>
     * <br>Fecha: 30/07/2015
     ************************************************************************************************************************************/
    String doValidatePostVentaBolCel(long orderId)throws  SQLException, Exception, RemoteException;
    
    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:jhonny.quispe@tcs.com">Jhonny Quispe</a>
     * <br>Fecha: 21/02/2017
     ************************************************************************************************************************************/
    boolean validarRentaAdelantada(String hdnSpecification, long lngCustomerId, String tipoDocumento, String numeroDocumento, HashMap objOrdenRA ) throws SQLException, Exception, RemoteException;
    
    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:jhonny.quispe@tcs.com">Jhonny Quispe</a>
     * <br>Fecha: 07/04/2017
     ************************************************************************************************************************************/
    HashMap getOrdenRentaAdelantada(long orderId) throws SQLException, Exception, RemoteException;

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:jorge.curi@tcs.com">JCURI</a>
     * <br>Fecha: 11/07/2017
	 * <br>Modificado por: JBALCAZAR PRY-1002
	 * <br>Fecha: 22/01/2018
     ************************************************************************************************************************************/
    boolean obtenerFlagActivoProrrateo(String hdnSpecification, String tipoDocumento , String numeroDocumento,String strGeneratorType, String strUser) throws SQLException, Exception;

    /**
     * @author AMENDEZ
     * @project PRY-0864
     * Metodo   Evalua mostrar campo precio excepcion en popupitem
     *          flag vep activo y cliente es ruc20(Juridico): 1 --> Se muestra campo de precio excepcion
     *          flag vep inactivo y cliente no es ruc20(Natural): 0 --> No se muestra campo de precio excepcion
     *          errores generales -1
     * @return
     */
    int evaluateExceptionPriceVep(int npvep,long sw_customerid) throws SQLException, Exception;

    /**
     * @author AMENDEZ
     * @project PRY-0864
     * Metodo   Evalua el monto inicial en casos de persona natural y persona juridica
     *          flag vep activo, cliente es ruc20(Juridico) y monto inicial no obligatorio: 1
     *          flag vep activo y cliente no es ruc20(Natural) y monto inicial es obligatorio: 2
     *          errores generales -1
     * @return
     */
    String validateOrderVepCI(long nporderid,int npvepquantityquota,double npinitialquota,int npspecificationid,long swcustomerid,double totalsalesprice,int npvep,String nptype,int nppaymenttermsiq) throws SQLException, Exception;

    /**
     * @author AMENDEZ
     * @project PRY-0980
     * Metodo   Retorna valor para colocar el check por defecto de forma de pago cuota inicial
     * @return
     */
    int validatePaymentTermsCI(long swcustomerid,long userid,int npvep)  throws SQLException, Exception;
    /**
     * @author CMONETEROS
     * @project PRY-1062
     * Metodo   Valida la preevaluacion para Cambio de Modelo.
     * @return
     */
    HashMap doValidatePreevaluationCDM(long customerid) throws SQLException, Exception;

    /**
     * @author KPEREZ
     * @project PRY-1037
     * Metodo   Valida  .
     * @return
     */
    HashMap doValidateSimManager(String[] item_Product_Val) throws SQLException, Exception;

    /**
     * @author CMONETEROS
     * @project PRY-1062
     * Metodo   Valida que el numero de telefono no esté asociado a una cuota VEP
     * @return
     */
    HashMap doValidateVEPItem(long customerid, String strPhoneNumber) throws SQLException, Exception;


    /**
     * Purpose: Valida que exista una configuracion para bafi 2300 segun modalidad, solucion y linea producto
     * Developer       Fecha       Comentario
     * =============   ==========  ======================================================================
     * AMENDEZ         22/03/2018  [EST-1098]Creacion
     */
    int validateConfigBafi2300(String av_npmodality,String av_npsolutionid,String av_npproductlineid) throws Exception;
	
	/**
     * @author JCURI
     * @project PRY-1093
     * Metodo   Valida la activacion del check de courier
     * @return
     */
    boolean activeChkCourier(int iUserId, int iAppId) throws SQLException, Exception;
    
    /**
     * @author AMENDEZ
     * @project PRY-1200
     * Metodo   Valida si la especificacion esta configurada para VEP
     *          flag 1, Aplica
     *          flag 0, No aplica
     *          flag -1, Errores
     * @return
     */
    String validateSpecificationVep(int anum_npspecificationid) throws SQLException, Exception;

    /**
     * @author AMENDEZ
     * @project PRY-1200
     * Metodo   Obtiene valor de configuracion de tablas VEP
     * @return
     */
    public String getConfigValueVEP(String avch_npvaluedesc) throws SQLException, Exception;
} 
     

