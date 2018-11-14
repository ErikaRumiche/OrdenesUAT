package pe.com.nextel.ejb;

import pe.com.nextel.bean.OrderBean;
import pe.com.nextel.bean.OrderContactBean;
import pe.com.nextel.bean.PenaltyBean;
import pe.com.nextel.bean.SpecificationBean;
import pe.com.nextel.dao.*;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;
import pe.com.nextel.util.RequestHashMap;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Jorge Gabriel on 19/05/2016.
 */
public class SEJBPenaltyBean implements SessionBean {
    private SessionContext _context;
    PenaltyDAO   objPenaltyDAO   = null;
    OrderDAO     objOrderDAO     = null;
    CategoryDAO  objCategoryDAO  = null;
    ExceptionDAO objExceptionDAO = null;
    GeneralDAO   objGeneralDAO   = null;

    public void ejbCreate() {
        objPenaltyDAO   = new PenaltyDAO();
        objOrderDAO     = new OrderDAO();
        objCategoryDAO  = new CategoryDAO();
        objExceptionDAO = new ExceptionDAO();
        objGeneralDAO   = new GeneralDAO();
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void ejbRemove() {
    }

    public void setSessionContext(SessionContext ctx) {
        _context = ctx;
    }

    //DERAZO Requerimiento REQ-0428
    public HashMap getPenaltySimulator(String strTelefono, String strCustomerId) throws SQLException, Exception {
        return objPenaltyDAO.getPenaltySimulator(strTelefono, strCustomerId);
    }

    //DERAZO Requerimiento REQ-0428
    public HashMap getPenaltyListByPhone(String strObjectType,String strObject, String strCustomerId) throws SQLException, Exception {
        return objPenaltyDAO.getPenaltyListByPhone(strObjectType, strObject, strCustomerId);
    }

    //DERAZO Requerimiento REQ-0428
    public HashMap getFlagShowPenaltySimulatorEdit(String strCustomerId, String strPhone) throws SQLException, Exception {
        return objPenaltyDAO.getFlagShowPenaltySimulatorEdit(strCustomerId, strPhone);
    }

    //DERAZO Requerimiento REQ-0428
    public HashMap verifAddendumPenalty(String strOption, String strOrderId, String strUser) throws SQLException, Exception {
        return objPenaltyDAO.verifAddendumPenalty(strOption, strOrderId, strUser);
    }

    //DERAZO Requerimiento REQ-0428
    public HashMap getFlagShowPenaltyFunctionality(String strOrderId, String strSpecificationId, String strStatus, String strUser) throws SQLException, Exception {
        return objPenaltyDAO.getFlagShowPenaltyFunctionality(strOrderId, strSpecificationId, strStatus, strUser);
    }

    //DERAZO Requerimiento REQ-0428
    public HashMap getConfigurationList(String strConfiguration) throws SQLException, Exception {
        return objPenaltyDAO.getConfigurationList(strConfiguration);
    }

    /**
     * Motivo : Registra la orden y las secciones relacionadas a la categor�a
     *          seleccionada
     * @return HashMap
     * @param objHashMap
     */
    public HashMap doSaveOrder(HashMap objHashMap){
        System.out.println("************** INI SEJBPenaltyBean->doSaveOrder **************");
        String     strMessage = null;
        HashMap    hshResultSaveOrder = new HashMap();
        int        intSpecificationId;

        try {
            //Obtener la conexion del pool de conexiones (DataSource)
            Connection conn = Proveedor.getConnection();

            //Desactivar el commit automatico de la conexion obtenida
            conn.setAutoCommit(false);

            OrderBean orderBean = null;
            try {
                SpecificationBean objSpecificationBean=null;
                HashMap hshData=null;
                orderBean = getOrderData(objHashMap);

                hshResultSaveOrder=OrderValidations(MiUtil.parseLong(objHashMap.get("hdnSessionUserid").toString()) ,orderBean,conn);
                if( ((String)hshResultSaveOrder.get("strMessage"))!=null){
                    return hshResultSaveOrder;
                }

                //Se graba la orden
                System.out.println("===============REGISTRO DE LA ORDEN ==> "+orderBean.getNpOrderId() +" === USUARIO ==> "+orderBean.getNpCreatedBy() +" =======================================");

                hshResultSaveOrder = objOrderDAO.getOrderInsertar(orderBean,conn);
                System.out.println("SEJBOrderNewBean/doSaveOrder/getOrderInsertar/->"+(String)hshResultSaveOrder.get("strMessage"));
                if( ((String)hshResultSaveOrder.get("strMessage"))!=null){
                    if (conn != null) conn.rollback();
                    return hshResultSaveOrder;
                }

                //graba los periodos de excepciones
                System.out.println("SEJBOrderNewBean/doSaveOrder/Eliminación de los peridodos de excepciones");
                strMessage = doDeleteOrderPeriod(objHashMap, conn);
                System.out.println("SEJBOrderNewBean/doSaveOrder/doDeleteOrderPeriod/->strMessage: "+strMessage);
                if( strMessage!=null){
                    if (conn != null) conn.rollback();
                    hshResultSaveOrder.put("strMessage",strMessage);
                    return hshResultSaveOrder;
                }

                System.out.println("SEJBOrderNewBean/doSaveOrder/Registro de los peridodos de excepciones");
                strMessage = doSaveOrderPeriod(objHashMap, conn);
                System.out.println("SEJBOrderNewBean/doSaveOrder/doSaveOrderPeriod/->strMessage: "+strMessage);
                if( strMessage!=null){
                    if (conn != null) conn.rollback();
                    hshResultSaveOrder.put("strMessage",strMessage);
                    return hshResultSaveOrder;
                }

                String strSpecificationId   = (objHashMap.get("hdnSpecification")==null?"":(String)objHashMap.get("hdnSpecification"));
                long   longSpecificationId  = MiUtil.parseLong(strSpecificationId);


                //Inicio Mod CGC  - Datos para BPEL
                hshData=objCategoryDAO.getSpecificationDetail(longSpecificationId,conn);
                strMessage=(String)hshData.get("strMessage");
                if( strMessage!=null){
                    if (conn != null) conn.rollback();
                    hshResultSaveOrder.put("strMessage",strMessage);
                    return hshResultSaveOrder;
                }

                //JCASAS Valida si debe contar con Evaluación de Créditos
                HashMap hshValidateCredit = new HashMap();
                CreditEvaluationDAO objCreditEvaluationDAO = new CreditEvaluationDAO();
                long lOrderId = orderBean.getNpOrderId(); //MiUtil.parseLong(MiUtil.defaultString(objHashMap.get("hdnNumeroOrder"),null));
                System.out.println("Antest de objCreditEvaluationDAO.doValidateCredit : "+lOrderId);
                hshValidateCredit = objCreditEvaluationDAO.doValidateCredit(lOrderId,"ORDER");
                if((String)hshValidateCredit.get("strMessage")!=null){
                    throw new Exception(strMessage);
                }
                String strValidateCredit = (String)hshValidateCredit.get("flagValidateCredit");
                if(strValidateCredit.equals("1")) {
                    HashMap hshResultCustomerScore = objOrderDAO.validateCustomerScore(orderBean.getNpOrderId(), orderBean.getNpCustomerId(),orderBean.getNpSpecificationId(),orderBean.getNpCreatedBy(), conn);
                    if((String)hshResultCustomerScore.get("strMessage")!=null){
                        if(conn !=null) conn.rollback();
                        hshResultSaveOrder.put("strMessage",(String)hshResultCustomerScore.get("strMessage"));
                        return hshResultSaveOrder;
                    } else {
                        String respCustomerScore=(String)hshResultCustomerScore.get("flagCustomerScore");
                        System.out.println("SEJBOrderNewBean/doSaveOrder/validateCustomerScore/respCustomerScore-> "+respCustomerScore);
                        if(respCustomerScore.equals("0")) {
                            if(conn !=null)
                                conn.rollback();
                            hshResultSaveOrder.put("strMessage","El cliente requiere evaluación");
                            return hshResultSaveOrder;
                        }
                    }
                }

                objSpecificationBean=(SpecificationBean)hshData.get("objSpecifBean");

                hshResultSaveOrder.put("objSpecificationBean",objSpecificationBean);
                hshResultSaveOrder.put("strOrderId",orderBean.getNpOrderId()+"");
                hshResultSaveOrder.put("strCustomerId",orderBean.getNpCustomerId()+"");
                //hshResultSaveOrder.put("strSolutionId",orderBean.getNpSolutionId()+"");
                hshResultSaveOrder.put("strDivisionId",orderBean.getNpDivisionId()+"");

                //Confirmar la transaccion en la BD
                conn.commit();
                System.out.println("===============FIN DEL REGISTRO DE LA ORDEN ==> "+orderBean.getNpOrderId() +" === USUARIO ==> "+orderBean.getNpCreatedBy() +" =======================================");
                // Fin Mod CGC  - Datos para BPEL
                return hshResultSaveOrder;
            } catch (Exception e) {
                //Si existe error, deshacer los cambios en la BD
                if (conn != null) conn.rollback();

                System.out.println("===============ERROR AL CREAR LA ORDEN ==> " + orderBean.getNpOrderId() + " === USUARIO ==> " + orderBean.getNpCreatedBy() + " =======================================");
                e.printStackTrace();
                strMessage  = MiUtil.getMessageClean("[Exception][doSaveOrder]["+e.getClass() + " " + e.getMessage()+ " - Caused by " + e.getCause() + "]");
                hshResultSaveOrder.put("strMessage",strMessage);
                return hshResultSaveOrder;
            }
            finally {
                //Finalmente, pase lo que pase, cerrar la conexion
                try{
                    if (conn != null){
                        conn.close();
                        conn = null;
                    }
                }catch (Exception exConn) {
                    exConn.printStackTrace();
                    try{
                        if (conn != null){
                            conn.close();
                            conn = null;
                        }
                    }catch (Exception exConnAux) {
                        exConnAux.printStackTrace();
                        try{
                            if (conn != null){
                                conn.close();
                                conn = null;
                            }
                        }catch(Exception exConnAuxiliar){
                            exConnAuxiliar.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            strMessage  = MiUtil.getMessageClean("[Exception][doSaveOrder]["+ex.getClass() + " " + ex.getMessage()+"]");
            hshResultSaveOrder.put("strMessage",strMessage);
        }
        System.out.println("************** FIN SEJBPenaltyBean->doSaveOrder **************");
        return hshResultSaveOrder;
    }

    public OrderBean getOrderData(HashMap request) throws Exception{
        System.out.println("************** INI SEJBPenaltyBean->getOrderData **************");
        HashMap    hshResultSaveOrder = new HashMap();
        OrderBean orderBean = new OrderBean();
        // Variables para funcionalidad de grabaci�n

        String  strOrderId ="",
                strCreatedby = "",
                strDato= "",
                strCodigoCliente= "",
                strMessage = "",
                strTipo= "",
                strStatus= "",
                strnpUser  = "",
                strnpsiteid = "",
                strnpprovidergrpid = "",
                strnpsalesmanname = "",
                strnpdealername = "",
                strnpsolutionid = "",
                strnpspecificationid = "",
                strnpbuildingid = "",
                strnpregionid = "",
                strnpSite ="",
                strnpdescription = "",
                strnpcategoria = "",
                strnphdnRegionId = "",
                strnpnumSolicutd = "",
                strtxtEstadoPago = "",
                strnpcmbFormaPago = "",
                strnpdispatchet = "",
                hdncmbCategoria = "",
                hdncmbSubCategoria = "",
                hdncmbVendedor = "",
                hdnExceptionInstallation = "",
                hdnExceptionPrice = "",
                hdnExceptionPlan = "",
                hdnExceptionWarrant = "",
                hdnExceptionRevenue = "",
                hdnExceptionRevenueAmount = "",
                hdnExceptionBillCycle = "",
                strDeliveryAddress = "",
                strDeliveryCity = "",
                strDeliveryProvince = "",
                strDeliveryState = "",
                strGeneratorType = "",
                strOrigenType = "",
                strGeneratorId = "",
                strnpdateprocess ="",
                strnpfuturedate ="",
                strnpsigndate ="",
                strhdnnpdispatchet="",
                strnpPaymentRespId = "",
                strnpdivisionid = "",
                strnpdatereconex ="",
                strnpproposedid="", //CBARZOLA
                strnpprovidergrpiddata = "",
                hdnSalesStructOrigenId = "",
                strnpdateFinProg="",
                strnpnumcuotas="",
                strTotalSalesPriceVEP = "",
                strCarpetaDigital = "";

        Date df = null;

        try{
            System.out.println("SEJBPenaltyBean->getOrderData->request: "+request);
            /**1.1 Secci�n de Captura de parametros.
             //------------------------------------
             */
            strOrderId              =   MiUtil.defaultString(request.get("hdnNumeroOrder"),null);
            strCreatedby            =   request.get("hdnSessionLogin").toString();
            strCodigoCliente        =   MiUtil.defaultString(request.get("txtCompanyId"),null);

            //strnpsolutionid         =   request.getParameter("cmbSolucion");
            strnpdivisionid         =   request.get("cmbDivision").toString();
            strnpspecificationid    =   request.get("cmbSubCategoria").toString();
            strnpcategoria          =   request.get("cmbCategoria").toString();
            strnpprovidergrpid      =   request.get("hdnVendedorId").toString();
            strnpSite               =   request.get("hdnSite").toString();
            strnpUser               =   request.get("hdnSessionUserid").toString();
            strnpspecificationid    =   request.get("hdnSpecification").toString();
            strnphdnRegionId        =   request.get("hdnRegionId").toString();
            strnpdescription        =   request.get("txtDetalle").toString();
            strnpnumSolicutd        =   request.get("txtNumSolicitud").toString();
            strnpcmbFormaPago       =   request.get("cmbFormaPago").toString();
            strnpdispatchet         =   request.get("cmbLugarAtencion").toString();
            hdncmbCategoria         =   request.get("hdncmbCategoria").toString();
            hdncmbSubCategoria      =   request.get("hdncmbSubCategoria").toString();
            hdncmbVendedor          =   request.get("hdncmbVendedor").toString();
            strnpdealername         =   request.get("txtDealer").toString();
            strtxtEstadoPago        =   request.get("txtEstadoPago").toString();
            strnpbuildingid         =   request.get("hdnBuildingId").toString();
            strnpdateprocess        =   MiUtil.defaultString(request.get("txtFechaProceso"),"");
            strnpfuturedate         =   MiUtil.defaultString(request.get("txtFechaProbablePago"),"");
            strnpsigndate           =   request.get("txtFechaHoraFirma").toString();
            strhdnnpdispatchet      =   request.get("hdnLugarDespacho").toString();
            strnpdatereconex        =   MiUtil.defaultString(request.get("txtFechaReconexion"),""); //rmartinez 15-06-2009
            strnpdateFinProg        =   MiUtil.defaultString(request.get("txtFechaFinProg"),""); //*jsalazar - modif hpptt # 1 - 27/09/2010 */

            //Para la dirección de entrega
            strDeliveryAddress      =   request.get("hdnDeliveryAddress").toString();
            strDeliveryCity         =   request.get("hdnDeliveryCity").toString();
            strDeliveryProvince     =   request.get("hdnDeliveryProvince").toString();
            strDeliveryState        =   request.get("hdnDeliveryState").toString();

            strGeneratorType        =   request.get("hdnGeneratorType").toString();

            strOrigenType           =   request.get("hdnOrigenType").toString();
            strGeneratorId          =   request.get("hdnGeneratorId").toString();

            if(request.get("cmbVepNumCuotas") != null) {
                strnpnumcuotas          =   request.get("cmbVepNumCuotas").toString();
            }

            /*if( MiUtil.parseInt(request.get("chkVepFlag")) == 1 && request.get("hdnTotalSalesPriceVEP") != null) {
                strTotalSalesPriceVEP   =   request.get("hdnTotalSalesPriceVEP");
            }*/

            /*Campos de Excepciones. */
            hdnExceptionInstallation =   request.get("hdnExceptionInstallation").toString();
            hdnExceptionPrice        =   request.get("hdnExceptionPrice").toString();
            hdnExceptionPlan         =   request.get("hdnExceptionPlan").toString();
            hdnExceptionWarrant      =   request.get("hdnExceptionWarrant").toString();
            hdnExceptionRevenue      =   request.get("hdnExceptionRevenue").toString();
            hdnExceptionRevenueAmount=   request.get("hdnExceptionRevenueAmount").toString();
            hdnExceptionBillCycle    =   request.get("hdnExceptionBillCycle").toString();
            strnpproposedid          =   request.get("txtPropuesta").toString();//CBARZOLA
            strnpprovidergrpiddata   =   request.get("hdnVendedorDataId").toString();
            hdnSalesStructOrigenId   =   request.get("hdnSalesStructOrigenId").toString();

            //EFH Carpeta digital
            strCarpetaDigital = MiUtil.defaultString(request.get("hdnCarpetaDigital"),"");

            String strReference = MiUtil.defaultString(request.get("txtReference"),""); // [N_O000017567] MMONTOYA

            // [N_O000017567] MMONTOYA
            // Inicio datos del contacto
            String strContactFirstName = MiUtil.defaultString(request.get("txtContactFirstName"),"");
            String strContactLastName = MiUtil.defaultString(request.get("txtContactLastName"),"");
            String strContactDocumentType = MiUtil.defaultString(request.get("cmbContactDocumentType"),"");
            String strContactDocumentNumber = MiUtil.defaultString(request.get("txtContactDocumentNumber"),"");
            String strContactPhoneNumber = MiUtil.defaultString(request.get("txtContactPhoneNumber"),"");
            // Fin datos del contacto


            orderBean = new OrderBean();

            orderBean.setNpOrderId(MiUtil.parseInt(strOrderId));
            orderBean.setNpCustomerId(MiUtil.parseLong(strCodigoCliente));
            orderBean.setNpCreatedDate(MiUtil.getTimeStampBD("dd/MM/yyyy"));
            orderBean.setNpCreatedBy(strCreatedby);

            orderBean.setNpBuildingId(MiUtil.parseLong(strnpbuildingid));
            orderBean.setNpSiteId(MiUtil.parseLong(strnpSite));
            orderBean.setNpSpecificationId(MiUtil.parseInt(strnpspecificationid));
            //orderBean.setNpSolutionId(MiUtil.parseInt(strnpsolutionid));
            orderBean.setNpDivisionId(MiUtil.parseInt(strnpdivisionid));
            orderBean.setNpSalesmanName(hdncmbVendedor);
            orderBean.setNpDealerName(strnpdealername);
            orderBean.setNpType(hdncmbCategoria);
            orderBean.setNpSpecification(hdncmbSubCategoria);
            orderBean.setNpProviderGrpId(MiUtil.parseInt(strnpprovidergrpid));
            orderBean.setNpDescription(strnpdescription);
            orderBean.setNpRegionId(MiUtil.parseInt(strnphdnRegionId));
            orderBean.setNpOrderCode(strnpnumSolicutd);
            orderBean.setNpPaymentTerms(strnpcmbFormaPago);
            orderBean.setNpPaymentStatus(strtxtEstadoPago);
            if(strnpdispatchet==null)
                orderBean.setNpDispatchPlaceId(MiUtil.parseInt(strhdnnpdispatchet));
            else{
                orderBean.setNpDispatchPlaceId(MiUtil.parseInt(strnpdispatchet));
            }
            orderBean.setNpPaymentFutureDate(MiUtil.toFecha(strnpfuturedate,"dd/MM/yyyy"));

        /*jsalazar - modif hpptt # 1 - 27/09/2010 - inicio*/
            if(!("".equals(strnpdateprocess.trim()))){
                orderBean.setNpScheduleDate(MiUtil.toFecha(strnpdateprocess,"dd/MM/yyyy"));
            }
        /*jsalazar - modif hpptt # 1 - 27/09/2010 - fin*/
            orderBean.setNpSignDate(MiUtil.toFechaHora(strnpsigndate,"dd/MM/yyyy HH:mm"));
        /*jsalazar - modif hpptt # 1 - 27/09/2010 - inicio*/
            if(Constante.SPEC_SERVICIOS_ADICIONALES[0]== MiUtil.parseInt(strnpspecificationid)){
                orderBean.setNpScheduleDate2(MiUtil.toFecha(strnpdateFinProg, "dd/MM/yyyy"));
            }else{
                orderBean.setNpScheduleDate2(MiUtil.toFecha(strnpdatereconex, "dd/MM/yyyy"));
            }
        /*jsalazar - modif hpptt # 1 - 27/09/2010 - Fin*/


            orderBean.setNpShipToAddress1(strDeliveryAddress);
            orderBean.setNpShipToCity(strDeliveryCity);
            orderBean.setNpShipToProvince(strDeliveryProvince);
            orderBean.setNpShipToState(strDeliveryState);

            orderBean.setNpGeneratorType(strGeneratorType);
            orderBean.setNpGeneratorId(MiUtil.parseLong(strGeneratorId));
            orderBean.setNpOrigen(strOrigenType);

            orderBean.setNpExceptionInstallation(MiUtil.parseInt(hdnExceptionInstallation));
            orderBean.setNpExcepcionBillCycle(hdnExceptionBillCycle);
            orderBean.setNpExceptionPlan(MiUtil.parseInt(hdnExceptionPlan));
            orderBean.setNpExceptionPrice(MiUtil.parseInt(hdnExceptionPrice));
            orderBean.setNpExcepcionBillCycle(hdnExceptionBillCycle);
            orderBean.setNpExceptionRevenueAmount(MiUtil.parseDouble(hdnExceptionRevenueAmount));
            orderBean.setNpExceptionRevenue(MiUtil.parseInt(hdnExceptionRevenue));
            orderBean.setNpproposedid(MiUtil.parseLong(strnpproposedid));
            orderBean.setNpProviderGrpIdData(MiUtil.parseLong(strnpprovidergrpiddata));
            orderBean.setSalesStructureOriginalId(MiUtil.parseLong(hdnSalesStructOrigenId));

            orderBean.setNpFlagVep(MiUtil.parseInt(MiUtil.defaultString(request.get("chkVepFlag"), "0")));
            orderBean.setNpNumCuotas(MiUtil.parseInt(strnpnumcuotas));
            orderBean.setNpAmountVep(MiUtil.parseDouble(strTotalSalesPriceVEP));
            orderBean.setNpFlagCourier(MiUtil.parseInt(MiUtil.defaultString(request.get("hdnChkCourier"), "0")));
            orderBean.setNpCustomerScoreId(MiUtil.parseLong(MiUtil.defaultString(request.get("customerscoreid"), "0")));

            orderBean.setNpShipToReference(strReference); // [N_O000017567] MMONTOYA
            //EFH Carpeta digital
            orderBean.setNpCarpetaDigital(strCarpetaDigital);

            // [N_O000017567] MMONTOYA
            // Inicio datos del contacto
            OrderContactBean orderContactBean = new OrderContactBean();
            orderContactBean.setFirstName(strContactFirstName);
            orderContactBean.setLastName(strContactLastName);
            orderContactBean.setDocumentType(strContactDocumentType);
            orderContactBean.setDocumentNumber(strContactDocumentNumber);
            orderContactBean.setPhoneNumber(strContactPhoneNumber);
            orderBean.setOrderContactBean(orderContactBean);
            // Fin datos del contacto

        } catch (Exception ex) {
            ex.printStackTrace();
            strMessage  = MiUtil.getMessageClean("[Exception][doSaveOrder]["+ex.getClass() + " " + ex.getMessage()+"]");
            hshResultSaveOrder.put("strMessage",strMessage);
            System.out.println(strMessage);
        }

        System.out.println("************** INI SEJBPenaltyBean->getOrderData **************");
        return orderBean;
    }


    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:cesar.barzola@nextel.com.pe">Cesar Barzola</a>
     * <br>Fecha: 01/07/2009
     ************************************************************************************************************************************/
    public HashMap OrderValidations(long lUserId, OrderBean objOrder, Connection conn)throws Exception, SQLException
    {
        System.out.println("************** INI SEJBPenaltyBean->OrderValidations **************");
        // lUserId=218198;
        HashMap hshResultValidation= new HashMap();
        String strMessage = null;
        try{

            //CBARZOLA- validacion que el usuario coordinador o supervisor tenga  necesariamente una propuesta
            HashMap hshResultCoord=objGeneralDAO.validateUserRol(lUserId,6,"I");
            if (hshResultCoord.get(Constante.MESSAGE_OUTPUT)!=null){
                hshResultValidation.put("strMessage",(String)hshResultCoord.get("strMessage"));
            }
            HashMap hshResultSupervisor=objGeneralDAO.validateUserRol(lUserId,7,"I");
            if (hshResultSupervisor.get(Constante.MESSAGE_OUTPUT)!=null){
                hshResultValidation.put("strMessage",(String)hshResultSupervisor.get("strMessage"));
            }
            int intresprolcoord= MiUtil.parseInt((String)hshResultCoord.get("respuesta"));
            System.out.println("intresprolcoord"+intresprolcoord);
            int intresprolsuperv= MiUtil.parseInt((String)hshResultSupervisor.get("respuesta"));
            System.out.println("intresprolsuperv"+intresprolsuperv);
            System.out.println("objOrder.getNpproposedid()"+objOrder.getNpproposedid());

            String strGeneratorType= objOrder.getNpGeneratorType();
            if(  (!strGeneratorType.equals(Constante.GENERATOR_TYPE_OPP)) && (intresprolcoord > 0 || intresprolsuperv > 0) )
            {
                if  (objOrder.getNpproposedid()==0)
                {
                    hshResultValidation.put("strMessage","Debe seleccionar una propuesta");
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            strMessage  = MiUtil.getMessageClean("[Exception][doSaveOrder]["+ex.getClass() + " " + ex.getMessage()+"]");
            hshResultValidation.put("strMessage",strMessage);
            System.out.println(strMessage);
        }
        System.out.println("************** INI SEJBPenaltyBean->OrderValidations **************");
        return hshResultValidation;
    }

    public String doDeleteOrderPeriod(HashMap request, Connection conn) throws SQLException,Exception{
        System.out.println("************** INI SEJBPenaltyBean->doDeleteOrderPeriod **************");
        String strOrderId      = "";
        String strMessage      = null;
        try{

            strOrderId       =   request.get("hdnNumeroOrder").toString();
            strMessage = objExceptionDAO.deleteOrderPeriod(Long.parseLong(strOrderId), conn);

        } catch (Exception ex) {
            ex.printStackTrace();
            strMessage  = MiUtil.getMessageClean("[Exception][doDeleteOrderPeriod]["+ex.getClass() + " " + ex.getMessage()+"]");
            System.out.println(strMessage);
        }
        System.out.println("************** INI SEJBPenaltyBean->doDeleteOrderPeriod **************");
        return strMessage;
    }

    public String doSaveOrderPeriod(HashMap request, Connection conn) throws SQLException,Exception{
        System.out.println("************** INI SEJBPenaltyBean->doSaveOrderPeriod **************");
        String strBeginPeriods = "",
                strEndPeriods   = "",
                strOrderId      = "",
                strCreatedBy    = "",
                strMessage      = null;

        try{

            strBeginPeriods  =   MiUtil.defaultString(request.get("hdnPeriodoIni"),null);
            strEndPeriods    =   MiUtil.defaultString(request.get("hdnPeriodoFin"),null);
            strOrderId       =   MiUtil.defaultString(request.get("hdnNumeroOrder"),null);
            strCreatedBy     =   MiUtil.defaultString(request.get("hdnSessionLogin"),null);

            if (strBeginPeriods!=null){
                System.out.println("strBeginPeriods === "+strBeginPeriods);
                StringTokenizer tkBeginPeriod   = new StringTokenizer(strBeginPeriods,"|");
                StringTokenizer tkEndPeriod      = new StringTokenizer(strEndPeriods,"|");

                while (tkBeginPeriod.hasMoreTokens()) {
                    String strBeginPeriod = tkBeginPeriod.nextToken();
                    String strEndPeriod   = tkEndPeriod.nextToken();
                    System.out.println("strBeginPeriod === "+strBeginPeriod);
                    strMessage = objExceptionDAO.insertOrderPeriod(Long.parseLong(strOrderId), strBeginPeriod, strEndPeriod, strCreatedBy, conn );
                    if (strMessage!=null){
                        System.out.println("===========strMessage "+strMessage);
                        break;
                    }

                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            strMessage  = MiUtil.getMessageClean("[Exception][doSaveOrderPeriod]["+ex.getClass() + " " + ex.getMessage()+"]");
            System.out.println(strMessage);
        }

        System.out.println("************** INI SEJBPenaltyBean->doSaveOrderPeriod **************");
        return strMessage;
    }

    /**
     * @see pe.com.nextel.dao.PenaltyDAO#getOrderPenaltyByParentOrderId(long)
     */
    public HashMap getOrderPenaltyByParentOrderId(long npparentorderid) throws SQLException, Exception{
        HashMap hshData = new HashMap();
        try{
            hshData = objPenaltyDAO.getOrderPenaltyByParentOrderId(npparentorderid);
        }catch(Exception ex){
            hshData.put(Constante.MESSAGE_OUTPUT,ex.getMessage());
            ex.printStackTrace();
        }
        return hshData;
    }

    /**
     * @see pe.com.nextel.dao.PenaltyDAO#updateFastOrderIdPenalty(long, long, String)
     */
    public String updateFastOrderIdPenalty(long lOrderId, long lFastOrderId, String strLogin) throws SQLException, Exception{
        try{
            return objPenaltyDAO.updateFastOrderIdPenalty(lOrderId,  lFastOrderId, strLogin);
        }catch(Exception ex){
            ex.printStackTrace();
            return MiUtil.getMessageClean("[Exception][doSaveOrder][" + ex.getClass() + " " + ex.getMessage() + "]");
        }
    }

    /**
     * @see pe.com.nextel.dao.PenaltyDAO#saveOrderPenalty(long, String, String, List)
     */
    public HashMap saveOrderPenalty(long lOrderId, String strPaymentterms, String strLogin,List<PenaltyBean> penaltyList) throws SQLException, Exception{
        HashMap hshData = new HashMap();
        HashMap hshResult = new HashMap();
        try{
            hshData = objPenaltyDAO.saveOrderPenalty(lOrderId, strPaymentterms, strLogin,penaltyList);

            //Inicio Mod CGC  - Datos para BPEL
            /*hshResult=objCategoryDAO.getSpecificationDetail(longSpecificationId,conn);
            strMessage=(String)hshData.get("strMessage");
            if( strMessage!=null){
                if (conn != null) conn.rollback();
                hshResultSaveOrder.put("strMessage",strMessage);
                return hshResultSaveOrder;
            }
            */
        }catch(Exception ex){
            hshData.put(Constante.MESSAGE_OUTPUT, ex.getMessage());
            ex.printStackTrace();
        }
        return hshData;
    }

    /**
     * @see pe.com.nextel.dao.PenaltyDAO#getPageOrderById(long)
     */
    public HashMap getPageOrderById(int userId,int appId,long lOrderId) throws SQLException, Exception{
        HashMap hshData = new HashMap();
        try{
            hshData = objPenaltyDAO.getPageOrderById(userId,appId,lOrderId);
        }catch(Exception ex){
            hshData.put(Constante.MESSAGE_OUTPUT, ex.getMessage());
            ex.printStackTrace();
        }
        return hshData;
    }


    /**
     * Este metodo quita la referencia que existe entre la adenda y la orden rapida creada, limpiando la orden rapida para la orden padre
     * @see pe.com.nextel.dao.PenaltyDAO#updatePageOrderById(long,long ,String)
     */
    public String updateStatusFastOrder(long lOrderId,long lFastOrderId, String strLogin) throws  Exception{

        HashMap hshRetorno= null;
        String strMessage = null;
        OrderBean orbResume = null;

        try{
            System.out.println("---------------INICIO VERIFICACION DE ANULACION DE LA ORDEN ANTES DE EJECUTAR ACCION------------------");
            System.out.println("lOrderId : "+lOrderId);
            System.out.println("lFastOrderId : "+lFastOrderId);
            System.out.println("strLogin : "+strLogin);

            //Solo ejecutara si viene la orden padre
            if(lOrderId > 0) {

                if(lFastOrderId > 0){
                    //Si viene la orden hijo
                    System.out.println("El parametro lFastOrderId llega con valor : "+lFastOrderId);
                }else{
                    //Si no viene la orden hijo hay que verificar que realmente no existe una relacion
                    System.out.println("El parametro lFastOrderId NO llega con valor : "+lFastOrderId+" sin embargo se verifica si realmente no se tiene la relacion");
                    hshRetorno = objPenaltyDAO.getOrderPenaltyByParentOrderId(lOrderId);
                    strMessage = (String) hshRetorno.get("strMessage");
                    if (strMessage != null) {
                        return strMessage;
                    }
                    lFastOrderId = (Long)hshRetorno.get("nporderid");
                    System.out.println("Se obtiene la orden de penalidad asociada :"+lFastOrderId);

                }
                if(lFastOrderId > 0){
                    hshRetorno = objOrderDAO.getOrder(lFastOrderId);
                    strMessage = (String) hshRetorno.get("strMessage");
                    if (strMessage != null) {
                        return strMessage;
                    }
                    orbResume = (OrderBean) hshRetorno.get("objResume");
                    System.out.println("orbResume.getNpStatus() : " + orbResume.getNpStatus());
                    if (Constante.ORDER_STATUS_ANULADO.equals(orbResume.getNpStatus())) {
                        objPenaltyDAO.updateFastOrderIdPenalty(lOrderId, 0, strLogin);
                    }
                }

            }
        }catch(Exception ex){
            strMessage=ex.getMessage();
            ex.printStackTrace();
        }
        return strMessage;
    }
    
    /**
     * @see pe.com.nextel.dao.PenaltyDAO#getFlagShowPenaltyPayFunctionality(long)
     */ 
    public HashMap getFlagShowPenaltyPayFunctionality(long lOrderId) throws SQLException, Exception { 
    	System.out.println("## getFlagPenaltyPayFunctionality ## lOrderId : " + lOrderId);
    	return objPenaltyDAO.getFlagShowPenaltyPayFunctionality(lOrderId);
    }
    
    /**
     * @see pe.com.nextel.dao.PenaltyDAO#getConfigurationRolList(String, long)
     */ 
    public HashMap getConfigurationListBoxList(String strNroRuc, long lUserId, int iAppId) throws SQLException, Exception { 
    	System.out.println("## getConfigurationListBoxList ## strNroRuc : " + strNroRuc + " | lUserId : " + lUserId + " | iAppId : " + iAppId);
    	try {
        	strNroRuc = strNroRuc.substring(0,2);        	
        	return objPenaltyDAO.getConfigurationRolList(lUserId,iAppId,strNroRuc);        	
		} catch (Exception e) {
			throw new Exception(e);
		}    	
    }
}
