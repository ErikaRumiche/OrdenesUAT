package pe.com.nextel.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import pe.com.nextel.bean.*;

import pe.com.nextel.bean.CreditEvaluationBean;
import pe.com.nextel.bean.ItemBean;
import pe.com.nextel.bean.ItemServiceBean;
import pe.com.nextel.bean.OrderBean;
import pe.com.nextel.bean.PortalSessionBean;
import pe.com.nextel.bean.ServiciosBean;
import pe.com.nextel.bean.SpecificationBean;
import pe.com.nextel.exception.BpelException;
import pe.com.nextel.exception.SessionException;
import pe.com.nextel.exception.UserException;
import pe.com.nextel.service.*;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;
import pe.com.nextel.util.MyUtil;
import pe.com.nextel.util.RequestHashMap;
import pe.com.nextel.service.IsolatedVerificationService;

/**
 * Motivo:  Servlet que gestiona las peticiones de Órdenes
 * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
 * <br>Fecha: 22/10/2007
 *
 * @author     Lee Rosales
 */
public class OrderServlet extends GenericServlet{

    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
    private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";


    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        // Variables estandar.
        String strMyaction = null;
        String strMessage  = null;
        out.println("<html>");
        out.println("<head><title>OrderServlet</title></head>");
        out.println("<script >");

        try{

            //Captura la acción a realizar.
            strMyaction = request.getParameter("myaction");

            // Bucle estandar de seleccion de acciones.
            if ( strMyaction != null ) {

                if(strMyaction.equals("grabarOrden") ) {             //Acción: GrabarOrden

                    //Si no existe vep para la orden entonces hace lo siguiente:
                    if (doValidateExistVep(request,response).equals("N")){
                        if (MiUtil.parseInt((String)request.getParameter("chkVepFlag")) == 1){
                            //EFLORES PM0011359, se almacena temporalmente las operaciones de VEP
                            request.setAttribute(Constante.VEP_OPERATIONS,Constante.VEP_OPERATIONS_ENUM.DELETE_AND_SAVE_VEP);
                            logger.info("CHECK VEP 1 | BEFORE doGrabarOrden "+Constante.VEP_OPERATIONS+" value: "+request.getAttribute(Constante.VEP_OPERATIONS));
                        }else{
                            //EFLORES PM0011359, se almacena temporalmente las operaciones de VEP
                            request.setAttribute(Constante.VEP_OPERATIONS,Constante.VEP_OPERATIONS_ENUM.DELETE_VEP);
                            logger.info("CHECK VEP NOT 1 | BEFORE doGrabarOrden "+Constante.VEP_OPERATIONS+" value: "+request.getAttribute(Constante.VEP_OPERATIONS));
                        }
                    }
                    doGrabarOrden(request, response, out);
                }

                else if(strMyaction.equals("limpiarOrden") ) {       //Acción: LimpiarOrden
                    clearOrder(request, out);
                }

                else if(strMyaction.equals("cmbCategoria") ) {       //Acción: cmbCategoria
                    displayCategory(request, out);
                }

                else if(strMyaction.equals("cmbSubCategoria") ) {    //Acción: cmbSubCategoria
                    displaySubCategory(request, out);
                }
                else if(strMyaction.equals("loadAssignmentBillingAccount")){
                    loadAssignmentBillingAccount(request, response, out);
                }
                else if(strMyaction.equals("loadSpecificationsSections")){
                    validateSpecificationOrder(request, response, out);
                    loadSpecifications(request, response, out);
                }
                else if(strMyaction.equals("getDealerNameBySalesman")){
                    getDealerNameBySalesman(request, response, out);
                }
                //MSOTO 03-06-2014 SAR N_O000022816 txtCmbVendedor reemplaza al cmbVendedor
                else if(strMyaction.equals("getDealerNameByVendor")){
                    getDealerNameByVendor(request, response, out);
                }

                else if(strMyaction.equals("evaluarOrden")){

                    if (MiUtil.parseInt((String)request.getParameter("chkVepFlag")) == 1){
                        //EFLORES PM0011359, se almacena temporalmente las operaciones de VEP
                        request.setAttribute(Constante.VEP_OPERATIONS,Constante.VEP_OPERATIONS_ENUM.DELETE_AND_SAVE_VEP);
                        logger.info("CHECK VEP 1 | BEFORE evaluarOrden "+Constante.VEP_OPERATIONS+" value: "+request.getAttribute(Constante.VEP_OPERATIONS));
                    }else{
                        //EFLORES PM0011359, se almacena temporalmente las operaciones de VEP
                        request.setAttribute(Constante.VEP_OPERATIONS,Constante.VEP_OPERATIONS_ENUM.DELETE_VEP);
                        logger.info("CHECK VEP NOT 1 | BEFORE evaluarOrden "+Constante.VEP_OPERATIONS+" value: "+request.getAttribute(Constante.VEP_OPERATIONS));
                    }

                    doEvaluarOrden(request, response, out);
                }
                else if(strMyaction.equals("ValidationProposedOrder")){
                    doValidateProposedOrder(request,response,out);
                }
                else if(strMyaction.equals("validateSalesExclusivity")){
                    validateSalesExclusivity(request,response,out);
                }

            }

        }catch(UserException e){
            strMessage = e.getMessage();
            String variable = MiUtil.getMessageClean(strMessage);
            out.println("alert('"+variable+"')");
            out.println("parent.mainFrame.frmdatos.btnSaveOrder.disabled = false;");
            out.println("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html');");
        }catch(BpelException e){
            strMessage = e.getMessage();
            String variable = MiUtil.getMessageClean(strMessage);
            out.println("alert('Se produjo un error con BPEL. La Orden fue registrada')");
            out.println("alert('"+variable+"')");
            out.println("parent.mainFrame.frmdatos.btnSaveOrder.disabled = true;");
            out.println("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html');");
        }catch(Exception e){
            e.printStackTrace();
            strMessage = e.getMessage() + " - Caused By : " + e.getCause();
            String variable = MiUtil.getMessageClean(strMessage);
            out.println("alert('"+strMessage+"')");
            out.println("parent.mainFrame.frmdatos.btnSaveOrder.disabled = false;");
            out.println("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html');");
        }
        //out.println("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html');");
        out.println("</script>");
        out.close();
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        response.setContentType(CONTENT_TYPE);

        doGet(request, response);
    }

    public IsolatedVerificationBean getValidVia(RequestHashMap objHashMap, String hdnFlgViaType, Integer hdnVerifId, Integer hdnSpecificationId){
        Integer customerId=(objHashMap.get("txtCompanyId")==null || objHashMap.get("txtCompanyId").equals(""))
                ?null:Integer.parseInt(objHashMap.get("txtCompanyId").toString());

        IsolatedVerificationService service = new IsolatedVerificationService();
        List<IsolatedVerificationBean> verificationBeanList=new ArrayList<IsolatedVerificationBean>();
        if(hdnFlgViaType!=null && customerId!=null){
            if(hdnFlgViaType.equals("0")){
                //validacion de que exista al menos una via
                verificationBeanList=service.getViaCustomer(customerId, null, null, hdnSpecificationId);
            }else if(hdnFlgViaType.equals("1")){
                //validacion que la verificacion ingresada siga disponible y vigente
                verificationBeanList=service.getViaCustomer(customerId, hdnVerifId, null, hdnSpecificationId);
            }
            if(verificationBeanList.size()>0)
                return verificationBeanList.get(0);
        }
        return null;
    }

    public void updateVia(Integer npverificationid, String nptypetransaction, Integer nptransaction, String npmodifiedby){
        IsolatedVerificationService service = new IsolatedVerificationService();
        service.updViaCustomer(npverificationid, "ORDEN", nptransaction, npmodifiedby);
    }

    /**
     * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
     * <br>Fecha: 04/08/2007
     * <br>Lee Rosales     04/08/2007  Creación
     * <br>Carmen Gremios  17/01/2008  Se agregó código para invocar a BPEL
     * <br>Hugo Tenorio    04/08/2008  Se cambio el metodo para invocar a BPEL cuando se crea la orden
     * @see pe.com.nextel.dao.OrderDAO#doGrabarOrden(String,String,String)
     */
    public void doGrabarOrden(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws Exception,ServletException,IOException {
        logger.info("*************** INICIO OrderSevlet > doGrabarOrden ***************");
        //Variable que controla la Transaction de la inserción de la Orden
        long    norderid = 0;
        String  strMessage = null;
        String  strOrderId = null;
        String  strEstatusOrder = null;
        String  strNextInboxName = null;
        java.sql.Timestamp fechaFirma = null;
        java.sql.Timestamp fechaFirma2 = null;
        PortalSessionBean objPortalSessionBean = new PortalSessionBean();
        OrderBean         objOrderBean = new OrderBean();
        SpecificationBean objSpecificationBean=null;
        NewOrderService   objOrderServiceTransaction = new NewOrderService();
        WorkflowService   objWorkflowService = new WorkflowService();
        BiometricaService objBiometricaService= new BiometricaService();
        DigitalDocumentService objDigitalizationService = new DigitalDocumentService();
        PopulateCenterService populateCenterService = new PopulateCenterService();
        //DigitalizationService objDigitalizationService = new DigitalizationService();
        String login =request.getParameter("hdnSessionLogin");
        logger.info("[loginBio]"+login);

        String          strSessionId=(request.getParameter("hdnUserId")==null?"0":request.getParameter("hdnUserId"));
        objPortalSessionBean = (new SessionService()).getUserSession(strSessionId);
        logger.info("[OrderServlet]OrderServlet/doGrabarOrden/getUserSession/strSessionId->"+strSessionId);
        if( objPortalSessionBean == null )
            throw new SessionException("Su sesión ha finalizado. Vuelva a ingresar");

        HashMap hshResultSaveOrder = new HashMap();
        HashMap hashData   = null;
        HashMap hshRetorno = null;
        HashMap hshValidateBlack  = null;

        RequestHashMap objHashMap =  new RequestHashMap();
        HashMap objHasvalidate = new HashMap();

        objHashMap = getParameterNames(request);
        logger.info("objHashMap: "+objHashMap.toString());
        objHashMap.put("hdnSessionLogin",objPortalSessionBean.getLogin());
        objHashMap.put("hdnSessionUserid",objPortalSessionBean.getUserid()+"");
        objHashMap.put("hdnSessionAppid",objPortalSessionBean.getAppId()+"");
        norderid = MiUtil.parseLong(objHashMap.getParameter("hdnNumeroOrder"));

        //Inicio PPNN
        CreditEvaluationService creditEvaluationService = new CreditEvaluationService();
        logger.info("Antes obtener customerscoreid para la orden: " + norderid);
        HashMap hashCustomerScore = creditEvaluationService.getLastCustomerScore(norderid);
        logger.info("Despues de obtener customerscoreid: " + hashCustomerScore.get("customerscoreid"));
        objHashMap.put("customerscoreid", hashCustomerScore.get("customerscoreid"));
        //Fin PPNN

        //INICIO PORTEGA
        //obtener flg de verificacion identidad fase 2 y valor del verif id, opcional

        logger.info("[OrderServlet]validacion VERIFICACION IDENTIDAD: objHashMap: "+objHashMap);

        String hdnFlgViaType=objHashMap.get("hdnFlgViaType")==null || objHashMap.get("hdnFlgViaType").equals("")
                ?null:objHashMap.get("hdnFlgViaType").toString();
        Integer hdnVerifId=(objHashMap.get("hdnVerifId")==null || objHashMap.get("hdnVerifId").equals(""))
                ?null:Integer.parseInt(objHashMap.get("hdnVerifId").toString());
        Integer hdnSpecificationId=(objHashMap.get("cmbSubCategoria")==null || objHashMap.get("cmbSubCategoria").equals(""))
                ?null:Integer.parseInt(objHashMap.get("cmbSubCategoria").toString());

        boolean hasValidVia=false;
        IsolatedVerificationBean verificationBean=null;

        logger.info("[OrderServlet]validacion VERIFICACION IDENTIDAD: hdnVerifId: "+hdnVerifId+", hdnFlgViaType: "+hdnFlgViaType+ ", hdnSpecificationId: "+hdnSpecificationId);

        if(hdnFlgViaType!=null && hdnSpecificationId!=null){
            if(hdnFlgViaType.equals("1")){
                logger.info("[OrderServlet]validacion para empresa, hdnVerifId: "+hdnVerifId);
                if(hdnVerifId==null || hdnVerifId.equals("")){
                    Integer hdnRowCount=(objHashMap.get("hdnRowCount")==null || objHashMap.get("hdnRowCount").equals(""))
                            ?null:Integer.parseInt(objHashMap.get("hdnRowCount").toString());
                    logger.info("[OrderServlet]hdnRowCount: "+hdnRowCount);
                    if(hdnRowCount==null || hdnRowCount.intValue()<1)
                        strMessage="Se requiere por lo menos una verificación de identidad aislada para generar la orden.";
                    else
                        strMessage="Se requiere seleccionar un RRLL o apoderado de la lista de verificación de identidad aislada para generar la orden.";

                    logger.info("[OrderServlet]verificationBean is null, se retorna error: "+strMessage);
                    throw new UserException(strMessage);
                }
            }
            logger.info("[OrderServlet]validacion de via, getValidVia()");
            String customer=""+objHashMap.get("hdnCustomerName");
            //validacion de via
            verificationBean=getValidVia(objHashMap, hdnFlgViaType, hdnVerifId, hdnSpecificationId);
            logger.info("[OrderServlet]validacion de via, verificationBean: "+verificationBean);
            if(verificationBean!=null){
                hasValidVia=true;
            }else{
                strMessage="El cliente: "+customer+", no cuenta con una verificación de " +
                        "identidad aislada disponible para este proceso.";
                logger.info("[OrderServlet]verificationBean is null, se retorna error: "+strMessage);
                throw new UserException(strMessage);
            }

        }
        //FIN PORTEGA

	//Ini: [TDECONV003] KOTINIANO
        String strFlagMigracion = null;
        if(request.getParameter("hdnFlagMigration") != null) {
            strFlagMigracion = String.valueOf( request.getParameter("hdnFlagMigration")).trim();
        }
        logger.info("[OrderServlet]OrderServlet/doGrabarOrden hdnFlagMigration->"+strFlagMigracion);
        //Fin: [TDECONV003] KOTINIANO

        //EFLORES PM0011359 se almacena el status adicional para registrar vep
        objHashMap.put(Constante.VEP_OPERATIONS,request.getAttribute(Constante.VEP_OPERATIONS));
        logger.info(Constante.VEP_OPERATIONS+" FIRST :  "+request.getAttribute(Constante.VEP_OPERATIONS));
        //INICIO JVERGARA
        //Se obtiene los flags de VIA, Conctacto autorizado y Tipo de Firma
        //Si el Contacto autorizado esta activo debe ser el mismo que el de VIA

        logger.info("[OrderServlet]validacion VERIFICACION IDENTIDAD AISLADA, APODERADO Y TIPO FIRMA: objHashMap: "+objHashMap);



        Integer hdnChkAuthContac=(objHashMap.get("hdnChkAssignee")==null || objHashMap.get("hdnChkAssignee").equals(""))
                ?null:Integer.parseInt(objHashMap.get("hdnChkAssignee").toString());
        Integer cmbSignature=(objHashMap.get("cmbSignature")==null || objHashMap.get("cmbSignature").equals(""))
                ?null:Integer.parseInt(objHashMap.get("cmbSignature").toString());

        String txtDocNumAuthContac=objHashMap.get("txtDocNumAssignee")==null || objHashMap.get("txtDocNumAssignee").equals("")
                ?null:objHashMap.get("txtDocNumAssignee").toString();

        String DocTypeAssigneeText =  MiUtil.emptyValObjTrim(objHashMap.get("hdnCmbDocTypeAssigneeText"));

        String strClientDocNum = objHashMap.get("hdnDocument")==null || objHashMap.get("hdnDocument").equals("")
                ?null:objHashMap.get("hdnDocument").toString();

        String strClientDocType = objHashMap.get("hdnClientDocType")==null || objHashMap.get("hdnClientDocType").equals("")
                ?null:objHashMap.get("hdnClientDocType").toString();

        if(hdnFlgViaType!=null&&hdnChkAuthContac!=null&&cmbSignature!=null){
            if(hdnFlgViaType.equals("1")&&cmbSignature==1){
                if(hdnChkAuthContac==1){
                    if(verificationBean!=null&&txtDocNumAuthContac!=null){
                        if(verificationBean.getNpnrodocument().equals(txtDocNumAuthContac.trim()) &&
                                verificationBean.getNptipodocument().equals(DocTypeAssigneeText)){
                        logger.info("[OrderServlet] Apoderado y Verificacion de Identidad Aislada validados correctamente ");

                        } else{
                            if(!verificationBean.getNpnrodocument().equals(txtDocNumAuthContac.trim())){
                                strMessage="El numero de documento del Apoderado no coincide con la verificacion de " +
                                        "identidad aislada seleccionada.";
                            logger.info("[OrderServlet] Apoderado y VIA no coinciden, se retorna error: "+strMessage);
                                throw new UserException(strMessage);
                            }else{
                                strMessage="El tipo de documento del Apoderado no coincide con la verificacion de " +
                                        "identidad aislada seleccionada.";
                            logger.info("[OrderServlet] Apoderado y VIA no coinciden, se retorna error: "+strMessage);
                                throw new UserException(strMessage);
                            }

                        }
                    }
                }else{
                    System.out.println("[OrderServlet] NO SE HA SELECCIONADO EL CHECK DEL APODERADO - strClientDocNum:"+strClientDocNum+"||txtDocNumAuthContac: "+txtDocNumAuthContac+"||strClientDocType: "+strClientDocType);
                    if(strClientDocNum!=null&&txtDocNumAuthContac!=null){
                        if(verificationBean.getNpnrodocument().equals(strClientDocNum) &&
                                verificationBean.getNptipodocument().equals(strClientDocType)){
                            logger.info("[OrderServlet] Cliente y Verificacion de Identidad Aislada validados correctamente ");

                        } else{
                            if(!verificationBean.getNpnrodocument().equals(strClientDocNum)){
                                strMessage="Los datos no coinciden con la Verificacion de Identidad Aislada, ingrese los datos del apoderado";
                                logger.info("[OrderServlet] Cliente y VIA no coinciden, se retorna error: "+strMessage);
                                throw new UserException(strMessage);
                            }else{
                                strMessage="Los datos no coinciden con la Verificacion de Identidad Aislada, ingrese los datos del apoderado";
                                logger.info("[OrderServlet] Cliente y VIA no coinciden, se retorna error: "+strMessage);
                                throw new UserException(strMessage);
                            }

                        }
                    }

                }

            }
        }


        //FIN JVERGARA

        //Metodo Guardar Orden
        hshResultSaveOrder = objOrderServiceTransaction.doSaveOrder(objHashMap);

        strMessage         =  (String)hshResultSaveOrder.get("strMessage");
        logger.info("[OrderServlet]OrderServlet/doGrabarOrden/doSaveOrder->"+strMessage);

        if (strMessage!=null)
            throw new UserException(strMessage);

        hashData = new HashMap();

        String strCustomerId=null;
        String strDivisionId=null;

        objOrderBean       = (OrderBean)hshResultSaveOrder.get("objOrderBean");
        strNextInboxName   = objOrderBean.getNpStatus();

        logger.info("[OrderServlet]OrderServlet/doGrabarOrden/strNextInboxName->"+strNextInboxName);

        //INICIO PORTEGA
        logger.info("[OrderServlet]validacion de via, hasValidVia(update): "+hasValidVia);
        if(hasValidVia){
            logger.info("[OrderServlet]actualizando via, updateVia() getNpOrderId: "+objOrderBean.getNpOrderId()+
                    ", getNpModificationBy: "+objOrderBean.getNpModificationBy());
            updateVia(verificationBean.getNpverificationid(), "ORDEN", (int)(objOrderBean.getNpOrderId()), ""+objHashMap.get("hdnSessionLogin"));
        }
        //FIN PORTEGA

        objSpecificationBean=(SpecificationBean)hshResultSaveOrder.get("objSpecificationBean");
        strOrderId=((String)hshResultSaveOrder.get("strOrderId")==null?"0":(String)hshResultSaveOrder.get("strOrderId"));
        strCustomerId=(String)hshResultSaveOrder.get("strCustomerId");

        strDivisionId=(String)hshResultSaveOrder.get("strDivisionId");
        logger.info("strDivisionId: "+strDivisionId);

        hashData.put("objSpecificationBean",objSpecificationBean);
        hashData.put("strOrderId",strOrderId);
        hashData.put("strCustomerId",strCustomerId);
        hashData.put("strDivisionId",strDivisionId);
        hashData.put("strNextInboxName",strNextInboxName);

        //Inicio  : RPASCACIO N_SD000246338

        java.util.Date fechaDataOrder = new java.util.Date();
        logger.info("[LOG_PROCESS_INFO] [INSERT_ORDER] [N_SD000246338]:  " +
                " \n- Orden : " + strOrderId +
                " \n- Lugar de Despacho : " + request.getParameter("cmbLugarAtencion") +
                " \n- Forma de Pago : " + request.getParameter("cmbFormaPago") +
                " \n- Fecha y Hora de Proceso  : " + fechaDataOrder +
                " \n- Nombre del Servlet :OrderServlet");

        //Fin : RPASCACIO

        //Validaciones del Blacklist
        //---------------------------
        hshValidateBlack =  new HashMap();
        int iReturnBlack = 0;
        hshValidateBlack = objOrderServiceTransaction.getValidateBlacklist(norderid);
        iReturnBlack=MiUtil.parseInt((String)hshValidateBlack.get("iResult"));
        String strMessageBlack = (String)hshValidateBlack.get("strMessage");
        if (iReturnBlack>=0){
            if (iReturnBlack>0){
                strMessageBlack = MiUtil.getMessageClean(strMessageBlack);
                out.println("alert('"+strMessageBlack+"')");
            }
        }else{
            if (strMessage!=null)
                throw new UserException(strMessageBlack);
        }

        String strValidAdm = objOrderServiceTransaction.getValidateAdministrator(norderid);
        logger.info("[OrderServlet][doGrabarOrden][norderid]"+norderid);
        logger.info("[OrderServlet][doGrabarOrden][strValidAdm]"+strValidAdm);
        if (strValidAdm != null){
            String variable = MiUtil.getMessageClean(strValidAdm);
            out.println("alert('"+variable+"')");
        }

        hshRetorno = objWorkflowService.doInvokeBPELCreateWorkflow(hashData, objPortalSessionBean);
        strMessage = (String)hshRetorno.get("strMessage");
        logger.info("OrderServlet/doGrabarOrden/doInvokeBPELCreateWorkflow->"+strMessage);

        if (strMessage!=null)
            throw new BpelException(strMessage);

        objOrderBean       = (OrderBean)hshResultSaveOrder.get("objOrderBean");
        strEstatusOrder    = objOrderBean.getNpStatus();
        /*Se está comentando esta parte de este codigo temporalmente por motivo de que
         *falta definir funcionalmente como va ser la creación de ordenes desde campañas (phidalgo)
          String pedidos = Constante.NAME_ORIGEN_FFPEDIDOS;
          */

        //PRY-0762 - JQUISPE
        OrderBean objOrderRentaAdelantada = (OrderBean) hshResultSaveOrder.get("objOrderRentaAdelantada");
                
        if(objOrderRentaAdelantada != null){
        	HashMap hashDataRentaAdelantada = new HashMap();
        	SpecificationBean objSpecificationRentaAdelantada = (SpecificationBean) hshResultSaveOrder.get("objSpecificationRentaAdelantada");

            logger.info("BPEL_RA objSpecificationRentaAdelantada:"+objSpecificationRentaAdelantada);
            logger.info("BPEL_RA objOrderRentaAdelantada.getNpOrderId() :"+objOrderRentaAdelantada.getNpOrderId());
            logger.info("BPEL_RA objOrderRentaAdelantada.getNpCustomerId():"+objOrderRentaAdelantada.getNpCustomerId());
            logger.info("BPEL_RA objOrderRentaAdelantada.getNpDivisionId():"+objOrderRentaAdelantada.getNpDivisionId());
            logger.info("BPEL_RA objOrderRentaAdelantada.getNpStatus():"+objOrderRentaAdelantada.getNpStatus());
        	
        	hashDataRentaAdelantada.put("objSpecificationBean",objSpecificationRentaAdelantada);
        	hashDataRentaAdelantada.put("strOrderId",objOrderRentaAdelantada.getNpOrderId() + "");
        	hashDataRentaAdelantada.put("strCustomerId",objOrderRentaAdelantada.getNpCustomerId()+"");
        	hashDataRentaAdelantada.put("strDivisionId",objOrderRentaAdelantada.getNpDivisionId() + "");
        	hashDataRentaAdelantada.put("strNextInboxName",objOrderRentaAdelantada.getNpStatus());
        	
            HashMap hshRetornoRentaAdelantada = objWorkflowService.doInvokeBPELCreateWorkflow(hashDataRentaAdelantada, objPortalSessionBean);
            strMessage = (String)hshRetornoRentaAdelantada.get("strMessage");
            logger.info("OrderServlet/doGrabarOrden/RentaAdelantada/doInvokeBPELCreateWorkflow->"+strMessage);

            if (strMessage!=null)
                throw new BpelException(strMessage);
        }    
               
        
        
        if(strMessage==null){

       /* INICIO ADT-BCL-083 - LHUAPAYA */
            String msj = null;
            msj = objOrderServiceTransaction.doValidatePostVentaBolCel(Long.parseLong(strOrderId));
            if(msj != null){
                out.println("alert(\""+ msj +"\")");
            }
       /* FIN ADT-BCL-083 - LHUAPAYA */

          //JQUISPE PRY-0762 Muestra mensaje de la Orden de Renta Adelantada
            ItemBean objItemRentaAdelantada = (ItemBean) hshResultSaveOrder.get("objItemRentaAdelantada");
            
            //JCURI PRY-0890 Mensaje de la Orden de Prorrateo(Pago Anticipado).
            ItemBean objItemProrrateo = (ItemBean)hshResultSaveOrder.get("objItemProrrateo");
            logger.info("[successOrderProrrateo] " + objItemProrrateo);
                        
            String messaje = "La orden " + strOrderId + " se ingresó y se envió al INBOX "+strNextInboxName;
            
            if(objItemRentaAdelantada!=null) {
            	messaje = messaje.concat("\\nLa orden de Renta Adelantada "+ objItemRentaAdelantada.getNporderid() +" tiene un monto de "+objItemRentaAdelantada.getNpprice()); 
            } 
            if(objItemProrrateo!=null) {
            	messaje = messaje.concat("\\nLa orden de Pago Anticipado "+ objItemProrrateo.getNporderid() +" se ha creado y tiene un monto de "+objItemProrrateo.getNpprice());
            }
            out.println("alert(\"" + messaje +"\")");
            
            //out.println("parent.mainFrame.location.replace('EditPagina.jsp?an_nporderid="+norderid+"');");
       /*Se está comentando esta parte de este codigo temporalmente por motivo de que
        *falta definir funcionalmente como va ser la creación de ordenes desde campañas (phidalgo)
       if(Constante.NAME_CAMP_DET.equals(objOrderBean.getNpOrigen())){
          out.println("try{parent.opener.fx_CreateOrderDetail('"+objOrderBean.getNpGeneratorId()+"','"+pedidos+"','"+objOrderBean.getNpOrderId()+"');}catch(e){}");
       }
       */

            logger.info("Despues de crear la orden");

            int    specification=0;
            int    order=Integer.valueOf(strOrderId);
            int    resultado=0;
            HashMap hashMap = new HashMap();
            String authorizedUser="";
            String authorizedDni="";
            String authorizedPass="";
            HashMap ValidActivacion=null;
            String itemSoluciones="";
            specification=Integer.valueOf(request.getParameter("cmbSubCategoria"));
            String finNormDivision = MiUtil.emptyValObjTrim(objHashMap.get("cmbDivision"));
         String finNormCategoria = MiUtil.emptyValObjTrim(objHashMap.get("cmbCategoria"));
         String finNormSubCategoria = MiUtil.emptyValObjTrim(objHashMap.get("cmbSubCategoria"));
         String finNormCodCliente = MiUtil.emptyValObjTrim(objHashMap.get("txtCompanyId"));
         String finNormCodPromotor = MiUtil.emptyValObjTrim(objHashMap.get("cmbVendedor"));

            //JVERGARA
            String channel = request.getParameter("hdnChannelClient");
            //

         String datosNorm = finNormDivision+"-"+finNormCategoria+"-"+finNormSubCategoria+"-"+finNormCodCliente+"-"+finNormCodPromotor+"-"+norderid;

            HashMap hashMapDigi = new HashMap();

            logger.info("Antes de Inserta en la Document Gneration");

            //INICIO PRY-0787 JCASTILLO INSERTA EN LA DOCUMENT GENERATION
            if(Constante.FLAG_SECTION_ACTIVE == MiUtil.getInt(objHashMap.get("hdnFlgSDD"))) {
                DocumentGenerationBean documentGenerationBean = objDigitalDocumentService.getDocumentGenerationBean(objOrderBean, objHashMap);
                //INICIO PRY-0787 JRIOS
                if(Constante.FLAG_SECTION_ACTIVE == MiUtil.getInt(objHashMap.get("hdnFlgSAC")) && "1".equals(MiUtil.getString(objHashMap.get("hdnChkAssignee")))){
                    DocAssigneeBean docAssigneeBean = objDigitalDocumentService.getDocAssigneeBean(objOrderBean,objHashMap);
                    hashMapDigi = objDigitalDocumentService.saveDocAssignee(docAssigneeBean);
                    documentGenerationBean.setAssigneeId(MiUtil.getLong(hashMapDigi.get("idAssignee")));
                }

                objDigitalDocumentService.saveDocumentGeneration(documentGenerationBean);
            }else{
                if(Constante.FLAG_SECTION_ACTIVE == MiUtil.getInt(objHashMap.get("hdnFlgSAC")) && "1".equals(MiUtil.getString(objHashMap.get("hdnChkAssignee")))){
                    DocAssigneeBean docAssigneeBean = objDigitalDocumentService.getDocAssigneeBean(objOrderBean,objHashMap);
                    objDigitalDocumentService.saveDocAssignee(docAssigneeBean);
                }
                //FIN JRIOS
            }
            //SECTION POPULATE CENTER
            if(Constante.FLAG_SECTION_ACTIVE==MiUtil.getInt(objHashMap.get("hdnFlgCPUF"))){
                int cpufResponse=MiUtil.getInt(request.getParameter("hdnCpufResponse"));
                if(cpufResponse!=2){
                    PopulateCenterBean populateCenterBean=new PopulateCenterBean();
                    populateCenterBean.setResponse(cpufResponse);
                    populateCenterBean.setOrderId(norderid);
                    populateCenterService.savePopulateCenter(populateCenterBean,objPortalSessionBean.getLogin());

                }
            }
            //FIN JCASTILLO


           /* INICIO PRY-0787 - JVERGARA */

            if(hasValidVia && (Constante.FLAG_SECTION_ACTIVE == MiUtil.getInt(objHashMap.get("hdnFlgSDD")))){
                HashMap objhash;
                objhash = objDigitalizationService.updateGenerationVI(order,request.getParameter("hdnSessionLogin"));
                if (StringUtils.isBlank((String) objhash.get(Constante.MESSAGE_OUTPUT))){
                    logger.info("[OrderServlet] Actualizando Generacion de documento si existe OrderId: "+((DocumentGenerationBean)objhash.get("documentGenerationBean")).getOrderId());
                }else{
                    logger.info("[OrderServlet] Actualizando Generacion de documento si existe "+objhash.get(Constante.MESSAGE_OUTPUT));

                }
            }

            //Obtener el Tipo de transaccion de la Orden
            int trxType = -1;
            int signType = -1;
            HashMap objhashDoc;
            objhashDoc = objDigitalDocumentService.getDocumentGeneration(strOrderId);
            String message = (String)objhashDoc.get(Constante.MESSAGE_OUTPUT);
            if(StringUtils.isBlank(message)){
                trxType = ((DocumentGenerationBean)objhashDoc.get("documentGenerationBean")).getTrxType();
                signType = ((DocumentGenerationBean)objhashDoc.get("documentGenerationBean")).getSignatureType();
            }
            logger.info("Tipo de Transacción 1:Todos, 2:Venta, 3:Portabilidad, 4:PostVenta : "+trxType);

            /* JVERGARA: Se agregó una validación para VIA deshabilitada en Proyecto VIDD */
            if((hdnFlgViaType==null) && (trxType==Constante.TRX_TYPE_POSTVENTA_ID) &&(signType==Constante.SIGNATURE_TYPE_DIGITAL)){
                HashMap objhash;
                objhash = objDigitalizationService.updateGenerationSIGN(order,Constante.SIGNATURE_REASON_DISABLED_VIA, login);
                out.println("alert(\"" + Constante.MESSAGE_REASON_70 + "\")");
                if(StringUtils.isBlank((String)objhash.get(Constante.MESSAGE_OUTPUT))){
                    logger.info("[OrderServlet] Actualizando Generacion de documento si existe OrderId: "+((DocumentGenerationBean)objhash.get("documentGenerationBean")).getOrderId());
                }else{
                    logger.info("[OrderServlet] Actualizando Generacion de documento si existe "+objhash.get(Constante.MESSAGE_OUTPUT));
                }
            }
            /* JVERGARA: Se agregó una validación para el proyecto de digitalización */
           String categoriaResultado;
            String usecase=Constante.Source_CRM;

            //VALIDAR SOLO EN ORDENES DE VENTA Y PORTABILIDAD
            logger.info("Antes de Validar Venta y Portabilidad "+hdnChkAuthContac);

            if (hdnChkAuthContac!=null){
                if(hdnChkAuthContac==1) { //Obtner hdnflgVIDD
                    usecase = Constante.Source_DIGIT;
                }
            }

            logger.info("Use Case: " + usecase);

            hashMap = objBiometricaService.getValidaCategoriaSolucion(order);
            categoriaResultado = (String) hashMap.get("strResultado");

            logger.info("Resultado: categoriaResultado"+categoriaResultado);

             /* FIN PRY-0787 - JVERGARA */


            if(categoriaResultado.equals("1")){
                logger.info("-subcategoria"+ request.getParameter("cmbSubCategoria")+
                        " -DNI:"+request.getParameter("txtCampoOtro")+
                        " -Login: "+request.getParameter("hdnSessionLogin"));

                    ValidActivacion=objBiometricaService.getValidActivation(order, specification, login, usecase);
                strMessage=(String) ValidActivacion.get("strMessage");
                logger.info("resultadoServletMessage="+strMessage);

                resultado=(Integer)ValidActivacion.get("resultado");
                authorizedUser = (String) ValidActivacion.get("authorizedUser");
                authorizedDni  = (String) ValidActivacion.get("authorizedDni");
                authorizedPass = (String) ValidActivacion.get("authorizedPass");
                logger.info("resultadoServlet="+resultado);

                if(resultado==1){

                    String document=objBiometricaService.getDocumento(order);
                    if(StringUtils.isBlank(document))
                    { document=""; }

                        String typeDocument = request.getParameter("txtCampoOtro");

                        //JVERGARA [PRY 0787]

                        boolean hdnChkAuthContac_flag =  hdnChkAuthContac == 1;
                        String CmbDocTypeAssigneeText = "";
                        String CmbDocTypeAssigneeVal = "";
                        String itxtDocNumAuthContac = "";

                        if(hdnChkAuthContac_flag){
                            CmbDocTypeAssigneeText =  MiUtil.emptyValObjTrim(objHashMap.get("hdnCmbDocTypeAssigneeText"));
                            CmbDocTypeAssigneeVal = MiUtil.emptyValObjTrim(objHashMap.get("hdnCmbDocTypeAssigneeVal"));
                            itxtDocNumAuthContac =  MiUtil.emptyValObjTrim(txtDocNumAuthContac);
                        }

                    logger.info(MiUtil.getDate(DATE_FORMAT)+"[PRY-0787: "+getClass().getSimpleName()+"] " + "????????????? "+strOrderId );


                        if(validateClientExoneration(strOrderId)){ //CLIENTES EMPRESAS AGREGAN LA OPCION DE VERIF EXONERADA

                            if(hdnChkAuthContac_flag){ //CONTACTO AUTORIZADO MARCADO

                                if(CmbDocTypeAssigneeVal.equals(Constante.DNI_ASSIGNEE)){//DNI de Apoderado

                                    document=itxtDocNumAuthContac;

                                    logger.info("Apoderado de Empresa con Documento = "+document);
                        String datos= strOrderId+"_"+authorizedUser+"_"+login+"_"+authorizedDni+"_"+authorizedPass+"_"+strSessionId+"_"+document;
                                    String url="Verificacion.jsp?datos="+datos;
                                    String winUrl = "POPUPBIOMETRICA/PopUpVerifFrame.jsp?idOrderPopup="+strOrderId+"&action=new"+"&av_url="+url;
                                    out.println("window.open(\""+winUrl+"\", \"Verificacion\",\"toolbar = no, location = no, directories = no, status = no, menubar = no, scrollbars = yes, resizable = yes, screenX = 60, top = 40, left = 60, screenY = 40, width = 620,height=400\"); ");


                                }else{ // CE o PASS de Apoderado
                                    //Enviar datos de contacto autorizado  Enviar tipo de documento
                                    document=itxtDocNumAuthContac;
                                    typeDocument= CmbDocTypeAssigneeText;
                                    if (typeDocument.equals("3")){typeDocument="1";} //Apoderado: 1: DNI, 2:CE, 3:PAS pero en PopUpVerificacion 1:PAS, 2:CE, 3:DNI

                                    logger.info("Exoneracion para el apoderado con tipo de documento : " + typeDocument + "  numero: " + document );
                                    String datos= strOrderId+"_"+authorizedUser+"_"+login+"_"+authorizedDni+"_"+authorizedPass+"_"+strSessionId+"_"+document+"_"+CmbDocTypeAssigneeVal+"_"+typeDocument;
                        String url = "VerificationExoneration.jsp?datos="+datos;
                        String winUrl = "POPUPBIOMETRICA/PopUpVerifFrame.jsp?idOrderPopup="+strOrderId+"&action=new"+"&av_url="+url;
                        out.println("window.open(\""+winUrl+"\", \"Verificacion\",\"toolbar = no, location = no, directories = no, status = no, menubar = no, scrollbars = yes, resizable = yes, screenX = 60, top = 40, left = 60, screenY = 40, width = 620,height=400\"); ");

                                }


                    } else{
                                System.out.println("Exoneracion para el cliente con tipo de documento : " + typeDocument + " y numero: " + document);
                        String datos= strOrderId+"_"+authorizedUser+"_"+login+"_"+authorizedDni+"_"+authorizedPass+"_"+strSessionId+"_"+document;
                                String url = "VerificationExoneration.jsp?datos="+datos;
                                String winUrl = "POPUPBIOMETRICA/PopUpVerifFrame.jsp?idOrderPopup="+strOrderId+"&action=new"+"&av_url="+url;
                                out.println("window.open(\""+winUrl+"\", \"Verificacion\",\"toolbar = no, location = no, directories = no, status = no, menubar = no, scrollbars = yes, resizable = yes, screenX = 60, top = 40, left = 60, screenY = 40, width = 620,height=400\"); ");

                            }

                        } else{  //PERSONAS

                            if(hdnChkAuthContac_flag){ //CONTACTO AUTORIZADO MARCADO


                                if(CmbDocTypeAssigneeVal.equals(Constante.DNI_ASSIGNEE)){//DNI Apoderado
                                    document=itxtDocNumAuthContac;
                                    logger.info("Apoderado de Persona resultado= "+document);
                                    String datos= strOrderId+"_"+authorizedUser+"_"+login+"_"+authorizedDni+"_"+authorizedPass+"_"+strSessionId+"_"+document;
                        String url="Verificacion.jsp?datos="+datos;
                        String winUrl = "POPUPBIOMETRICA/PopUpVerifFrame.jsp?idOrderPopup="+strOrderId+"&action=new"+"&av_url="+url;
                        out.println("window.open(\""+winUrl+"\", \"Verificacion\",\"toolbar = no, location = no, directories = no, status = no, menubar = no, scrollbars = yes, resizable = yes, screenX = 60, top = 40, left = 60, screenY = 40, width = 620,height=400\"); ");

                                }else{
                                    logger.info("Para apoderado con documento de identidad distinto de DNI y cliente Persona no se mostrará la VI ");
                                    //JVERGARA
                                    if(Constante.TRX_TYPE_PORTABILIDAD_ID==trxType){
                                        out.println("alert(\"" + Constante.MESSAGE_EXONERATE_PORTAB + "\")");
                                        HashMap objhash;
                                        objhash = objDigitalizationService.updateGenerationSIGN(order,Constante.SIGNATURE_REASON_FOREIGN, login);
                                        if(StringUtils.isBlank((String)objhash.get(Constante.MESSAGE_OUTPUT))){
                                        logger.info("[OrderServlet] Actualizando Generacion de documento si existe OrderId: "+((DocumentGenerationBean)objhash.get("documentGenerationBean")).getOrderId());
                                        }else{
                                        logger.info("[OrderServlet] Actualizando Generacion de documento si existe "+objhash.get(Constante.MESSAGE_OUTPUT));
                    }
                                    }


                                    validarCargarPopNormalizacion(out, datosNorm, norderid);

                                }


                            }else{
                                logger.info("resultado="+document);
                                String datos= strOrderId+"_"+authorizedUser+"_"+login+"_"+authorizedDni+"_"+authorizedPass+"_"+strSessionId+"_"+document;
                                String url="Verificacion.jsp?datos="+datos;
                                String winUrl = "POPUPBIOMETRICA/PopUpVerifFrame.jsp?idOrderPopup="+strOrderId+"&action=new"+"&av_url="+url;
                                out.println("window.open(\""+winUrl+"\", \"Verificacion\",\"toolbar = no, location = no, directories = no, status = no, menubar = no, scrollbars = yes, resizable = yes, screenX = 60, top = 40, left = 60, screenY = 40, width = 620,height=400\"); ");

                            }

                        }


                }else if(resultado==2){
                    out.println("alert(\"" + strMessage.trim() + "\")");
                    //2: usuario cuenta con rol pero no esta registrado en el mantenimiento
                    ValidActivacion=objBiometricaService.getInsertUserNotConfig(order,"BIOMETRIC",login);
                    strMessage=(String) ValidActivacion.get("strmessage");
                    logger.info("InsertUserNotConfig:strMessage =" + strMessage);

                        //JVERGARA
                        if(Constante.TRX_TYPE_PORTABILIDAD_ID==trxType){
                            out.println("alert(\"" + Constante.MESSAGE_REASON_60 + "\")");
                            HashMap objhash;
                            objhash = objDigitalizationService.updateGenerationVI(order,login);
                            if(StringUtils.isBlank((String)objhash.get(Constante.MESSAGE_OUTPUT))){
                                logger.info("[OrderServlet] Actualizando Generacion de documento si existe OrderId: "+((DocumentGenerationBean)objhash.get("documentGenerationBean")).getOrderId());
                            }else{
                                logger.info("[OrderServlet] Actualizando Generacion de documento si existe "+objhash.get(Constante.MESSAGE_OUTPUT));
                            }
                        }

             validarCargarPopNormalizacion(out, datosNorm, norderid);
             //out.println("parent.bottomFrame.location.replace('/portal/pls/portal/ORDERS.PLI_ORDER_REDIRECT?an_nporderid="+norderid+"&av_execframe=BOTTOMFRAME');");
         }else{
                        //JVERGARA
                        if(Constante.TRX_TYPE_PORTABILIDAD_ID==trxType){
                            out.println("alert(\"" + Constante.MESSAGE_REASON_60 + "\")");
                            HashMap objhash;
                            objhash = objDigitalizationService.updateGenerationVI(order,login);
                            if(StringUtils.isBlank((String)objhash.get(Constante.MESSAGE_OUTPUT))){
                                logger.info("[OrderServlet] Actualizando Generacion de documento si existe OrderId: "+((DocumentGenerationBean)objhash.get("documentGenerationBean")).getOrderId());
                            }else{
                                logger.info("[OrderServlet] Actualizando Generacion de documento si existe "+objhash.get(Constante.MESSAGE_OUTPUT));
                            }
                        }

             validarCargarPopNormalizacion(out, datosNorm, norderid);
             logger.info("hash=" + ValidActivacion);
             //out.println("parent.bottomFrame.location.replace('/portal/pls/portal/ORDERS.PLI_ORDER_REDIRECT?an_nporderid="+norderid+"&av_execframe=BOTTOMFRAME');");
         }
            }else{
                validarCargarPopNormalizacion(out, datosNorm, norderid);
                //out.println("parent.bottomFrame.location.replace('/portal/pls/portal/ORDERS.PLI_ORDER_REDIRECT?an_nporderid="+norderid+"&av_execframe=BOTTOMFRAME');");
            }

     }
     logger.info("*************** FIN OrderSevlet > doGrabarOrden ***************");
  }



    private void validarCargarPopNormalizacion(PrintWriter out, String datosNorm, long norderid){
        NormalizarDireccionService objNormService = new NormalizarDireccionService();
        try {
            if(objNormService.normalizacionIsActive()) {
                System.out.println("[OrderServlet][validarCargarPopNomalizacion] - orderId: " + norderid + "; datosNorm: " + datosNorm);
                if (objNormService.validarDatos(datosNorm)) {
                    String url = "PopUpNormalizar.jsp?datosNorm=" + datosNorm;
                    String winUrl = "POPUPNORMALIZACION/PopUpFrameNormalizar.jsp?av_url=" + url;
                    out.println("window.open(\"" + winUrl + "\", \"Normalizacion\",\"toolbar = no, location = no, directories = no, status = no, menubar = no, scrollbars = yes, resizable = yes, screenX = 60, top = 40, left = 60, screenY = 40, width = 710,height=410\"); ");
                } else {
                    out.println("parent.bottomFrame.location.replace('/portal/pls/portal/ORDERS.PLI_ORDER_REDIRECT?an_nporderid="+norderid+"&av_execframe=BOTTOMFRAME');");
                }
            }else{
                out.println("parent.bottomFrame.location.replace('/portal/pls/portal/ORDERS.PLI_ORDER_REDIRECT?an_nporderid="+norderid+"&av_execframe=BOTTOMFRAME');");
            }
        }catch (Exception e){
            System.out.println("[NormalizarDireccionServlet][cargarPopupNormalizacion] - Error" + e.getMessage());
            out.println("parent.bottomFrame.location.replace('/portal/pls/portal/ORDERS.PLI_ORDER_REDIRECT?an_nporderid="+norderid+"&av_execframe=BOTTOMFRAME');");
        }
    }
  
    /**
     Method : displayCategory
     Purpose: Muestra las categorias
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     David Coca     20/07/2007  Creación
     */
    public void displayCategory(HttpServletRequest request, PrintWriter out) {

        int intSolutionId = 0;

        out.println("form = parent.mainFrame.document.frmdatos;");

        //1.1 Sección de Captura de parametros.
        //------------------------------------
        intSolutionId = Integer.parseInt(request.getParameter("cmbDivision"));

        //1.2 Sección de proceso y captura de datos.
        //-----------------------------------------
        // borrar previamente como Categoria
        out.println("if( form.cmbCategoria.length > 0 ) {");
        out.println("  var nLongitud  =  form.cmbCategoria.length;");
        out.println("  for(j=0;j< nLongitud;j++) { ");
        out.println("     form.cmbCategoria.remove(0);");
        out.println("  };");

        out.println("};");

        // borrar previamente como SubCategoria
        out.println("if( form.cmbSubCategoria.length > 0 ) {");
        out.println("  var nLongitud_S  =  form.cmbSubCategoria.length;");
        out.println("  for(z=0;z< nLongitud_S;z++) { ");
        out.println("     form.cmbSubCategoria.remove(0);");
        out.println("  };");

        out.println("};");

        ArrayList l = null;

        try{
            l = NewOrderService.OrderDAOgetCategoryList(intSolutionId);

        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }

        if ( l != null && l.size() > 0 ){

            for( int i=1; i<=l.size();i++ ){
                Hashtable h = (Hashtable)l.get(i-1);
                out.println("opcion=new Option('" + h.get("wv_npType")+ "','" +  h.get("wv_npType") + "');");
                out.println("form.cmbCategoria.options[" + i + "]=opcion;");
            }

        }

        //1.3 Sección de visualización de datos.
        //-------------------------------------
        // Repetir similares
        out.println("location.replace('"+Constante.PATH_APPORDER_LOCAL+"/Bottom.html');");

    }

    /**
     Method : displaySubCategory
     Purpose: Muestra las Subcategorias
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     David Coca     20/07/2007  Creación
     */
    public void displaySubCategory(HttpServletRequest request, PrintWriter out) {

        String strCategoria = null;

        out.println("form = parent.mainFrame.document.frmdatos;");

        //1.1 Sección de Captura de parametros.
        //------------------------------------
        strCategoria = request.getParameter("cmbCategoria");

        //1.2 Sección de proceso y captura de datos.
        //-----------------------------------------
        // borrar previamente
        out.println("if( form.cmbSubCategoria.length > 0 ) {");
        out.println("  var nLongitud  =  form.cmbSubCategoria.length;");
        out.println("  for(j=0;j< nLongitud;j++) { ");
        out.println("     form.cmbSubCategoria.remove(0);");
        out.println("  };");

        out.println("};");

        ArrayList l = NewOrderService.OrderDAOgetSubCategoryList(strCategoria);

        if ( l!=null && l.size() > 0 ){
            for( int i=1; i<=l.size();i++ ){
                Hashtable h = (Hashtable)l.get(i-1);
                out.println("opcion=new Option('" + h.get("wv_npSpecification")+ "','" +  h.get("wn_npSpecificationId") + "');");
                out.println("form.cmbSubCategoria.options[" + i + "]=opcion;");
            }
        }
        //1.3 Sección de visualización de datos.
        //-------------------------------------
        // Repetir similares
        out.println("location.replace('"+Constante.PATH_APPORDER_LOCAL+"/Bottom.html');");

    }

    /**
     Method : clearOrder
     Purpose: Limpia las ordenes
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     David Coca     20/07/2007  Creación
     */
    public void clearOrder(HttpServletRequest request, PrintWriter out) {

        out.println("parent.mainFrame.frmdatos.txtNumSolicitud.value ='';");
        out.println("parent.mainFrame.frmdatos.cmbDivision.value ='';");
        out.println("parent.mainFrame.frmdatos.cmbCategoria.value ='';");
        out.println("parent.mainFrame.frmdatos.cmbSubCategoria.value ='';");
        out.println("parent.mainFrame.frmdatos.txtDealer.value ='';");

        //1.3 Sección de visualización de datos.
        //-------------------------------------
        // Repetir similares
        out.println("location.replace('"+Constante.PATH_APPORDER_LOCAL+"/Bottom.html');");

    }


    private void loadAssignmentBillingAccount(HttpServletRequest request, HttpServletResponse response,PrintWriter out) {
        try{
            //System.out.println("Entramos mameta");
            HashMap   objHashMap                 = new HashMap();
            String    resultTransaction          = "";
            long      lngOrderId                 = MiUtil.parseLong((String)request.getParameter("strOrderId"));
            String    pv_servicios_item          = request.getParameter("servicios_item");
            String    strCodigoCliente           = request.getParameter("txtCompanyId");
            String    strResponsablePagoValue    = request.getParameter("responsablePagoValue");
            String    strResponsablePagoDesc     = request.getParameter("responsablePagoDesc");
            String    strItemBillingAccount      = request.getParameter("item_billingAccount");
            String    strEquipoDescription       = request.getParameter("equipoDesc");
            String    strEquipoId                = request.getParameter("equipoId");

            ItemServiceBean itemServiceBean      = null;
            NewOrderService obNewOrderService    = new NewOrderService();
            BillingAccountService objBillingAccountService = new BillingAccountService();
            ArrayList objArrayBillingAccount     = new ArrayList();
            ArrayList objArraySiteExistsList     = new ArrayList();
            ArrayList objArraySiteSolicitedList  = new ArrayList();
            String    strCustomerStruct          = "";

            Vector    la_services                = new Vector();
            Vector    la_servicesid              = new Vector();
            Vector    la_excepserviceid          = new Vector();
            Vector    la_items_billing           = new Vector();
            int       ln_indexid;
            String    lv_freeservice;
            String    wv_npflagservicio_ant      = "";
            String    wv_npflagservicio_act      = "";
            String    resultFlafservice          = "";
            int       index                      = MiUtil.parseInt(request.getParameter("rowIndex"));

            StringTokenizer tokens          = new StringTokenizer(pv_servicios_item,"|");
            StringTokenizer tokensBilling   = new StringTokenizer(strItemBillingAccount,"|");

            //objHashMap             	= objBillingAccountService.BillingAccountDAOgetBillingAccountList(MiUtil.parseLong(strCodigoCliente));
  /*
        if( objHashMap.get("strMessage") != null ) {
          out.println("<script>");
          out.println("alert('" + objHashMap.get("strMessage") + "')");
          out.println("</script>");
          out.close();
          return;
        }
        */
            //objArrayBillingAccount    = (ArrayList)objHashMap.get("objArrayList");


            /**Obtenemos los Sites existentes**/
            objHashMap = (new NewOrderService()).SiteDAOgetSiteExistsList(MiUtil.parseLong(strCodigoCliente),Constante.CUSTOMERTYPE_CUSTOMER);
            if( objHashMap.get("strMessage") != null )
                throw new Exception((String)objHashMap.get("strMessage"));
            objArraySiteExistsList = (ArrayList)objHashMap.get("objArrayList");

            if( objArraySiteExistsList!=null ) strCustomerStruct = objArraySiteExistsList.size()==0?"FLAT":"LARGE";

            /**Obtenemos los Sites solicitados**/
            objHashMap = (new NewOrderService()).SiteDAOgetSiteSolicitedList(lngOrderId);
            if( objHashMap.get("strMessage") != null )
                throw new Exception((String)objHashMap.get("strMessage"));
            objArraySiteSolicitedList = (ArrayList)objHashMap.get("objArrayList");

            /**Guardamos los servicios adicionales**/
            if ( pv_servicios_item != null ){

                while(tokens.hasMoreTokens()){
                    String aux = tokens.nextToken();
                    la_services.addElement((String)aux);
                }

                for( int n = 0,j = 1;  j <= (la_services.size()/3); n=n+3,j++ ) {
                    String nomAux = "";
                    if( obNewOrderService.ServiceDAOgetServiceDescription(MiUtil.parseInt((String)la_services.elementAt(n)),"") != null)
                        nomAux = (obNewOrderService.ServiceDAOgetServiceDescription(MiUtil.parseInt((String)la_services.elementAt(n)),"")).getNpnomserv();
                    ServiciosBean objServiciosBean = new ServiciosBean();
                    objServiciosBean.setNpservicioid(MiUtil.parseLong(""+la_services.elementAt(n)));
                    objServiciosBean.setNpnomserv(nomAux);
                    la_servicesid.addElement(objServiciosBean);
                }

                while(tokensBilling.hasMoreTokens()){
                    String aux = tokensBilling.nextToken();
                    la_items_billing.addElement((String)aux);
                }


                request.setAttribute("objArraySiteExistsList",objArraySiteExistsList);
                request.setAttribute("objArraySiteSolicitedList",objArraySiteSolicitedList);

                request.setAttribute("la_servicesid",la_servicesid);
                request.setAttribute("strCodigoCliente",strCodigoCliente);
                request.setAttribute("objArrayBillingAccount",objArrayBillingAccount);
                request.setAttribute("strResponsablePagoValue",strResponsablePagoValue);
                request.setAttribute("strResponsablePagoDesc",strResponsablePagoDesc);
                request.setAttribute("vctItemBillingAccount",la_items_billing);
                request.setAttribute("strEquipoDescription",strEquipoDescription);
                request.setAttribute("strEquipoId",strEquipoId);
                request.setAttribute("index",""+index);
                request.setAttribute("strOrderId",""+lngOrderId);

                RequestDispatcher rd=request.getRequestDispatcher("htdocs/jsp/Order_AsignacionResponsables.jsp");
                rd.forward(request,response);
            }

        }catch(Exception ex){
            //return Constante.NPERROR + ex.getMessage();
        }
    }

    private void getDealerNameBySalesman(HttpServletRequest request,
                                         HttpServletResponse response,
                                         PrintWriter out) throws Exception {

        GeneralService objGeneralService = new GeneralService();
        HashMap objHashMap = null;
        HashMap objHashMap2 = null;
        HashMap hshValidationSaleMan=null;
        String  strSalesmanId ="",strDealerName,strCompanyId="",strSiteId="",strVendedor="",strDealer="";
        Long lngSiteId = null;
        strSalesmanId              =   request.getParameter("an_vendedorid");
        strCompanyId               =   request.getParameter("an_CompanyId");
        strSiteId                  =   request.getParameter("an_SiteId");
        strVendedor                =   request.getParameter("av_Vendedor");
        strDealer                  =   request.getParameter("av_Dealer");
        int iUserId =(request.getParameter("iUserId")==null?0:Integer.parseInt(request.getParameter("iUserId")));
        int iAppId =(request.getParameter("iAppId")==null?0:Integer.parseInt(request.getParameter("iAppId")));
        String strUserId=(String)request.getParameter("iUserId");


        if(strSiteId.equals(""))
            lngSiteId = new Long(0);
        else
            lngSiteId = new Long(strSiteId);

        objHashMap2 = objGeneralService.getValidateSalesman(MiUtil.parseLong(strCompanyId),lngSiteId.longValue(),MiUtil.parseLong(strSalesmanId),strVendedor,iUserId,iAppId);
        if( objHashMap2.get("strMessage2") != null ) {
            int iAccion=4;
            RequestDispatcher rd=request.getRequestDispatcher("PAGEEDIT/ValidateSalesMan.jsp?nAction="+iAccion+"&sMsgError="+objHashMap2.get("strMessage2"));
            rd.forward(request,response);
        }
        // Validación propuestas
        hshValidationSaleMan= objGeneralService.getValidationSalesManProposed(MiUtil.parseLong(strUserId),MiUtil.parseLong(strSalesmanId));
        if( hshValidationSaleMan.get(Constante.MESSAGE_OUTPUT) != null ) {
            int iAccion=4;
            RequestDispatcher rd=request.getRequestDispatcher("PAGEEDIT/ValidateSalesMan.jsp?nAction="+iAccion+"&sMsgError="+hshValidationSaleMan.get(Constante.MESSAGE_OUTPUT));
            rd.forward(request,response);
        }

        objHashMap =  objGeneralService.getDealerBySalesman(MiUtil.parseLong(strSalesmanId));
        if( objHashMap.get("strMessage") != null )
            throw new  Exception((String)objHashMap.get("strMessage"));

        strDealerName = (String)objHashMap.get("strDealerName");

        out.println("parent.mainFrame.frmdatos.txtDealer.value ='"+MiUtil.getString(strDealerName)+"';");
        out.println("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html');");

    }

    /**
     Method : loadSpecifications
     Purpose: Se invoca cuando ocurre se cambia de Subcategoria
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     Lee Rosales     04/08/2007  Creación
     Carmen Gremios  17/01/2008  Se la invocación al método loadPayForm
     */
    private void loadSpecifications(HttpServletRequest request,
                                    HttpServletResponse response,
                                    PrintWriter out) throws Exception{
        logger.info("************************** INICIO OrderServlet > loadSpecifications**************************");
        //Cargar el lugar de despacho
        loadDispatchPlaceList(request,response,out);

        //Cargar la fecha de Proceso Automatico
        loadSpecificationDate(request,response,out);

        //Cargar el combo: Forma de Pago
        loadPayForm(request,response,out);

        //Carga el combo de Vendedor Data o modifica el de Vendedor Voz de acuerdo a reglas
        loadDataSalesmen(request,response,out);

        //Cargar las secciones dinámicas
        loadSpecificationsSections(request,response,out);
        logger.info("************************** FIN OrderServlet > loadSpecifications**************************");
    }

    /**
     Method : loadPayForm
     Purpose: Obtiene los  datos de la forma de pago de acuerdo a un specificationId
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     Carmen Gremios  17/01/2008  Se la invocación al método loadPayForm

     */
    private void loadPayForm(HttpServletRequest request,
                             HttpServletResponse response,
                             PrintWriter out) throws Exception{
        logger.info("************************** INICIO OrderServlet > loadPayForm**************************");
        ArrayList arrListado=null;
        String hdnSpecification  =   request.getParameter("cmbSubCategoria");
        String strCustomerId  =  request.getParameter("txtCompanyId");
        logger.info("hdnSpecification  : "+hdnSpecification);
        logger.info("strCustomerId     : "+strCustomerId);
        HashMap hshData = objOrderService.getPayFormList(MiUtil.parseInt(hdnSpecification),MiUtil.parseInt(strCustomerId));
        logger.info("hshData     : "+hshData);
        String strMessage=(String)hshData.get("strMessage");
        logger.info("strMessage: "+strMessage);
        if (strMessage!=null)
            throw new Exception(strMessage);

        arrListado=(ArrayList)hshData.get("objListado");

        request.removeAttribute("arrListadoPayForm");
        request.setAttribute("arrListadoPayForm",arrListado);
        logger.info("************************** FIN OrderServlet > loadPayForm**************************");
    }

    private void loadSpecificationDate(HttpServletRequest request,
                                       HttpServletResponse response,
                                       PrintWriter out) throws Exception{
        logger.info("************************** INICIO OrderServlet > loadSpecificationDate**************************");
        String hdnSpecification   = "",
                strFlgEnabled      = "",
                strActivationDate  = "",
                wn_billcycle = null;
        String strMessage = null;
        hdnSpecification        =   MiUtil.getString(request.getParameter("cmbSubCategoria"));
        wn_billcycle      =   request.getParameter("hndBillcycle");
        NewOrderService objNewOrderService = new NewOrderService();

        HashMap objHashMap = objNewOrderService.getSpecificationDate(MiUtil.parseLong(hdnSpecification),wn_billcycle);

        if( objHashMap==null )
            throw new Exception("Hubieron errores al obtener la fecha por defecto del proceso automático");

        strMessage = (String)objHashMap.get("strMessage");
        logger.info("strMessage: "+strMessage);
        if( strMessage!=null )
            throw new Exception(strMessage);

        strFlgEnabled       = (String)objHashMap.get("strFlgEnabled");
        strActivationDate   = (String)objHashMap.get("strActivationDate");

        request.removeAttribute("strFlgEnabled");
        request.removeAttribute("strActivationDate");

        request.setAttribute("strFlgEnabled",strFlgEnabled);
        request.setAttribute("strActivationDate",strActivationDate);
        logger.info("************************** FIN OrderServlet > loadSpecificationDate**************************");
    }

    private String loadFlagEmail(String hdnSpecification,
                                 String strHdnIUserId) throws Exception{
        //String hdnSpecification   = "",
        //    strHdnIUserId      = "";
        String strMessage = null;
        String strFlgEnabledEmail = "";
        //hdnSpecification  =   MiUtil.getString(request.getParameter("cmbSubCategoria"));
        //strHdnIUserId = MiUtil.getString(request.getParameter("hdnIUserId"));
        System.out.println("jtorrescA strSpecification"+hdnSpecification);
        System.out.println("jtorrescB strHdnIUserId"+strHdnIUserId);
        NewOrderService objNewOrderService = new NewOrderService();
        HashMap objHashMap = objNewOrderService.getFlagEmail(MiUtil.parseLong(hdnSpecification), MiUtil.parseLong(strHdnIUserId));
        if( objHashMap==null )
            throw new Exception("Hubieron errores al obtener el flag de email");
        strMessage = (String)objHashMap.get("strMessage");
        if( strMessage!=null )
            throw new Exception(strMessage);
        strFlgEnabledEmail = (String)objHashMap.get("lngFlgEnabledEmail");
        //request.removeAttribute("strFlgEnabledEmail");
        //request.setAttribute("strFlgEnabledEmail", strFlgEnabledEmail);
        System.out.println("FINNNN jtorerss"+strFlgEnabledEmail);
        return strFlgEnabledEmail;
    }
    private void loadDispatchPlaceList(HttpServletRequest request,
                                       HttpServletResponse response,
                                       PrintWriter out) throws Exception{
        logger.info("************************** INICIO OrderServlet > loadDispatchPlaceList**************************");
        SessionService objSessionService = new SessionService();
        String strSessionId=(request.getParameter("hdnUserId")==null?"0":request.getParameter("hdnUserId"));
        PortalSessionBean objPortalSessionBean = objSessionService.getUserSession(strSessionId);
        String strGeneratorType = MiUtil.getString(request.getParameter("hdnGeneratorType")).toUpperCase();
        String strBuildingId=null;

        strBuildingId  =objPortalSessionBean.getBuildingid()+"";

        String hdnSpecification = "";
        String strMessage = null;
        hdnSpecification        =   MiUtil.getString(request.getParameter("cmbSubCategoria"));

        ArrayList arr = null;
        logger.info("strSessionId     : "+strSessionId);
        logger.info("strGeneratorType : "+strGeneratorType);
        logger.info("strBuildingId    : "+strBuildingId);
        logger.info("hdnSpecification : "+hdnSpecification);
        HashMap objHashMap = NewOrderService.OrderDAOgetDispatchPlaceList(MiUtil.parseInt(hdnSpecification));

        if( objHashMap==null )
            throw new Exception("Hubieron errores al traer información del Lugar de Despacho");

        strMessage = (String)objHashMap.get("strMessage");
        logger.info("strMessage: "+strMessage);
        if( strMessage!=null )
            throw new Exception(strMessage);

        arr = (ArrayList)objHashMap.get("arrData");

        request.removeAttribute("strBuildingId");
        request.removeAttribute("arrDataDispatchList");
        request.setAttribute("strBuildingId",strBuildingId);
        request.setAttribute("arrDataDispatchList",arr);
        logger.info("************************** FIN OrderServlet > loadDispatchPlaceList**************************");

    }
    /*
    * JOYOLAR - 01/08/2008
    * Valida si se puede crear otra orden para un prospect en base al specificationid
    * */
    private void validateSpecificationOrder(HttpServletRequest request,
                                            HttpServletResponse response,
                                            PrintWriter out) throws Exception{

        NewOrderService objOrderServiceSpec = new NewOrderService();
        HashMap objHashMap = null;

        String  strCodigoCliente = "",
                strSpecification = "";

        strCodigoCliente = request.getParameter("txtCompanyId");
        strSpecification = request.getParameter("cmbSubCategoria");

        long an_customerid = MiUtil.parseLong(strCodigoCliente);
        long an_specification = MiUtil.parseLong(strSpecification);

        objHashMap =  objOrderServiceSpec.getAllowedSpecification(an_specification, an_customerid);

        if( objHashMap.get("strMessage") != null ){
            throw new UserException((String)objHashMap.get("strMessage"));
        }

    }

    private void loadSpecificationsSections(HttpServletRequest request,
                                            HttpServletResponse response,
                                            PrintWriter out) throws Exception{
        logger.info("************************** INICIO OrderServlet > loadSpecificationsSections**************************");
        NewOrderService objOrderServiceSections = new NewOrderService();
        HashMap objHashMap = null;
        String  strOrderId ="",
                strCreatedby = "",
                strCodigoCliente= "",
                strProviderGrpId = "",
                strnpSite ="",
                strCodBSCS = "",
                hdnSpecification = "",
                strTypeCompany = "",
                strSessionId = "",
                strunknwnSiteId = "",
                strGeneratorType = "",
                strSalesTeamId = "",
                strDivision = "",
                strDocument = "",
                strTypeDocument = "",
                strSalesStuctOrigenId = "",
                strnpnumcuotas = "",
                strflagvep=""
                //INICIO: AMENDEZ | PRY-1141
                ,strhdnIUserId=""
                //FIN: AMENDEZ | PRY-1141
                //INICIO: AMENDEZ | PRY-1049
                ,strHdnCobertura=""
                //FIN: AMENDEZ | PRY-1049
                  ;


        strOrderId              =   request.getParameter("hdnNumeroOrder");
        strCreatedby            =   request.getParameter("hdnSessionCreatedby");
        strCodigoCliente        =   request.getParameter("txtCompanyId");
        strProviderGrpId        =   request.getParameter("hdnSalesTeamOppId");
        strSalesTeamId          =   request.getParameter("cmbVendedor");
        strnpSite               =   request.getParameter("hdnSite");
        strunknwnSiteId         =   request.getParameter("hdnunknwnSiteId");
        strCodBSCS              =   request.getParameter("txtCodBSCS");
        hdnSpecification        =   request.getParameter("cmbSubCategoria");
        strDivision             =   request.getParameter("cmbDivision");
        strTypeCompany          =   request.getParameter("txtTipoCompania");
        strSessionId            =   request.getParameter("hdnUserId");
        strGeneratorType        =   request.getParameter("hdnGeneratorType");
        strDocument             =   request.getParameter("hdnDocument");
        strTypeDocument         =   request.getParameter("hdnTypeDocument");
        strSalesStuctOrigenId   =   request.getParameter("hdnSalesStructOrigenId");
        //INICIO: AMENDEZ | PRY-1141
        strhdnIUserId           =   request.getParameter("hdnIUserId");
        logger.info("strhdnIUserId            : "+strhdnIUserId);
         //FIN: AMENDEZ | PRY-1141
        //INICIO: AMENDEZ | PRY-1049
        strHdnCobertura         =   request.getParameter("hdnCobertura");
        logger.info("strOrderId            : "+strOrderId);
        logger.info("strCreatedby          : "+strCreatedby);
        logger.info("strCodigoCliente      : "+strCodigoCliente);
        logger.info("strProviderGrpId      : "+strProviderGrpId);
        logger.info("strSalesTeamId        : "+strSalesTeamId);
        logger.info("strnpSite             : "+strnpSite);
        logger.info("strunknwnSiteId       : "+strunknwnSiteId);
        logger.info("strCodBSCS            : "+strCodBSCS);
        logger.info("hdnSpecification      : "+hdnSpecification);
        logger.info("strDivision           : "+strDivision);
        logger.info("strTypeCompany        : "+strTypeCompany);
        logger.info("strSessionId          : "+strSessionId);
        logger.info("strGeneratorType      : "+strGeneratorType);
        logger.info("strDocument           : "+strDocument);
        logger.info("strTypeDocument       : "+strTypeDocument);
        logger.info("strSalesStuctOrigenId : "+strSalesStuctOrigenId);
        logger.info("strHdnCobertura       : "+strHdnCobertura);
        //FIN: AMENDEZ | PRY-1049
    
        //Parametros q vienen desde Incidentes
        long lSiteId           = request.getParameter("hdnSiteOppId")==null?0:MiUtil.parseLong(request.getParameter("hdnSiteOppId"));
        long lGeneratorId      = request.getParameter("hdnGeneratorId")==null?0:MiUtil.parseLong(request.getParameter("hdnGeneratorId"));
        logger.info("lSiteId: "+lSiteId);
        logger.info("lGeneratorId: "+lGeneratorId);


        int idEspecification = MiUtil.parseInt(hdnSpecification);

        objHashMap =  objOrderServiceSections.CategoryDAOgetSpecificationData(idEspecification, strGeneratorType);

        if( objHashMap.get("strMessage") != null )
            throw new Exception((String)objHashMap.get("strMessage"));
        //MiUtil.printBottomReplaceMessage((String)request.getContextPath(),(String)objHashMap.get("strMessage"),out);

        ArrayList objDataSpecification = (ArrayList)objHashMap.get("objArrayList");
        request.setAttribute("objDataSpecification",objDataSpecification);

        //Agregar HashTable de inputs - Customer, ...

        Hashtable hshtInputNewSection = new Hashtable();
        request.removeAttribute("hshtInputNewSection");


        hshtInputNewSection.put("strCustomerId",strCodigoCliente);
        hshtInputNewSection.put("strSiteId",strnpSite);
        hshtInputNewSection.put("strCodBSCS",strCodBSCS);
        hshtInputNewSection.put("strSpecificationId",hdnSpecification);
        hshtInputNewSection.put("strDivisionId",strDivision);
        hshtInputNewSection.put("strTypeCompany",strTypeCompany);
        hshtInputNewSection.put("strSessionId",strSessionId);
        hshtInputNewSection.put("strOrderId",strOrderId);
        hshtInputNewSection.put("strSiteOppId",lSiteId+"");
        hshtInputNewSection.put("strGeneratorId",lGeneratorId+"");
        hshtInputNewSection.put("strUnknwnSiteId",strunknwnSiteId+"");
        hshtInputNewSection.put("strGeneratorType",strGeneratorType+"");
        hshtInputNewSection.put("strGeneratorType",strGeneratorType+"");
        hshtInputNewSection.put("strProviderGrpId",strProviderGrpId+"");
        hshtInputNewSection.put("strSalesTeamId",strSalesTeamId+"");
        hshtInputNewSection.put("strDocument",strDocument+"");
        hshtInputNewSection.put("strTypeDocument",strTypeDocument+"");
        hshtInputNewSection.put("strSalesStuctOrigenId",strSalesStuctOrigenId+"");


        //INICIO: AMENDEZ | PRY-1141
        hshtInputNewSection.put("strhdnIUserId",strhdnIUserId);
        //FIN: AMENDEZ | PRY-1141
        //INICIO: AMENDEZ | PRY-1049
        hshtInputNewSection.put("strHdnCobertura",strHdnCobertura);
        //FIN: AMENDEZ | PRY-1049

        request.removeAttribute("hshtInputNewSection");
        request.setAttribute("hshtInputNewSection",hshtInputNewSection);
        //ini jtorresc
        hdnSpecification   = "";
        String strHdnIUserId = "";

        hdnSpecification = MiUtil.getString(request.getParameter("cmbSubCategoria"));
        strHdnIUserId = MiUtil.getString(request.getParameter("hdnIUserId"));
        String strFlgEnabledEmail = loadFlagEmail(hdnSpecification, strHdnIUserId);
        request.removeAttribute("strFlgEnabledEmail");
        request.setAttribute("strFlgEnabledEmail", strFlgEnabledEmail);
        //fin jtorresc

        RequestDispatcher dispatcher = null;
        String messageError = null;
        try {
            dispatcher = getServletContext().getRequestDispatcher("/htdocs/jsp/JSP_FORWARD.jsp");
            dispatcher.forward(request,response);
        } catch (ServletException e) {
            e.printStackTrace();
            logger.error(e);
            messageError  = "[ServletException][OrderServlet][loadSpecificationsSections]["+e.getClass()+" "+e.getMessage()+"]";
            throw new Exception(messageError);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e);
            messageError  = "[ServletException][IOException][loadSpecificationsSections]["+e.getClass()+" "+e.getMessage()+"]";
            throw new Exception(messageError);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            messageError  = "[ServletException][Exception][loadSpecificationsSections]["+e.getClass()+" "+e.getMessage()+"]";
            throw new Exception(messageError);
        }
        logger.info("************************** FIN OrderServlet > loadSpecificationsSections**************************");
    }

    /**
     * @see GenericServlet#executeDefault(HttpServletRequest, HttpServletResponse)
     */
    public void executeDefault(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        try {
            logger.debug("Antes de hacer el sendRedirect");
            response.sendRedirect("/portal/page/portal/orders/RETAIL_NEW");
        } catch (Exception e) {
            logger.error(formatException(e));
        }
    }

    /**
     * <br>Realizado por: <a href="mailto:jose.casas@nextel.com.pe">José Casas</a>
     * <br>Fecha: 12/06/2009
     * <br>Lee Rosales     12/06/2009  Creación
     * @see pe.com.nextel.dao.OrderDAO#doEvaluarOrden(String,String,String)
     */
    public void doEvaluarOrden(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception, ServletException, IOException {
        logger.info("****************** INICIO OrderServlet > doEvaluarOrden******************");

        //Variable que controla la Transaction de la inserción de la Orden
        String  strMessage = null;
        PortalSessionBean objPortalSessionBean = new PortalSessionBean();
        CreditEvaluationService objCreditEvaluationService = new CreditEvaluationService();

        String strSessionId = (request.getParameter("hdnUserId")==null?"0":request.getParameter("hdnUserId"));
        objPortalSessionBean = (new SessionService()).getUserSession(strSessionId);
        logger.info("OrderServlet/doEvaluarOrden/getUserSession/strSessionId->"+strSessionId);
        if( objPortalSessionBean == null ) throw new SessionException("Su sesión ha finalizado. Vuelva a ingresar");

        HashMap hshCreditEvaluation = new HashMap();
        HashMap hshValidateCredit = new HashMap();
        RequestHashMap objHashMap =  new RequestHashMap();

        CreditEvaluationBean objCreditEvaluationBean = new CreditEvaluationBean();
        ArrayList arrRuleResult = null;

        String strFlagValidateCredit = "0";
        long lOrderId=MiUtil.parseLong(request.getParameter("hdnNumeroOrder"));

        logger.info("lOrderId: "+lOrderId);
        hshValidateCredit = objCreditEvaluationService.doValidateCredit(lOrderId,"ORDER");
        if((String)hshValidateCredit.get("strMessage")!=null){
            throw new Exception(strMessage);
        }
        String strValidateCredit = (String)hshValidateCredit.get("flagValidateCredit");
        if( MiUtil.getString(strValidateCredit).equals("1")) {
            strFlagValidateCredit = "1";
            objHashMap = getParameterNames(request);
            objHashMap.put("hdnSessionLogin",objPortalSessionBean.getLogin());
            objHashMap.put("hdnSessionUserid",objPortalSessionBean.getUserid()+"");
            objHashMap.put("hdnSessionAppid",objPortalSessionBean.getAppId()+"");

            //EFLORES PM0011359 se almacena el status adicional para registrar vep
            objHashMap.put(Constante.VEP_OPERATIONS,request.getAttribute(Constante.VEP_OPERATIONS));

            logger.info("objHashMap.toString(): "+objHashMap.toString());
            hshCreditEvaluation = objCreditEvaluationService.doEvaluateOrderCreate(objHashMap); //EFLORES PM0011359
            strMessage         =  (String)hshCreditEvaluation.get("strMessage");
            logger.info("OrderServlet/doEvaluarOrden/doEvaluateOrder->"+strMessage);
            if (strMessage!=null) throw new UserException(strMessage);

            String strOrderId = (String)objHashMap.getParameter("hdnNumeroOrder");
            HashMap hshData = new HashMap();
            hshData = (HashMap)objCreditEvaluationService.getCreditEvaluationData(lOrderId,"ORDER");
            strMessage=(String)hshData.get("strMessage");
            if (strMessage!=null)
                throw new Exception(strMessage);
            objCreditEvaluationBean = (CreditEvaluationBean)hshData.get("objCreditEvaluationBean");

            logger.info("objCreditEvaluationBean.getnpCreditEvaluationId() "+objCreditEvaluationBean.getnpCreditEvaluationId());

            hshData = (HashMap)objCreditEvaluationService.getRuleResult(objCreditEvaluationBean.getnpCreditEvaluationId(),"20");
            strMessage=(String)hshData.get("strMessage");
            if (strMessage!=null)
                throw new Exception(strMessage);
            arrRuleResult=(ArrayList)hshData.get("arrRuleResult");

        }
        request.removeAttribute("objCreditEvaluationBean");
        request.setAttribute("objCreditEvaluationBean",objCreditEvaluationBean);
        request.removeAttribute("arrRuleResult");
        request.setAttribute("arrRuleResult",arrRuleResult);
        request.removeAttribute("strFlagValidateCredit");
        request.setAttribute("strFlagValidateCredit",strFlagValidateCredit);

        RequestDispatcher dispatcher = null;
        String messageError = null;
        try {
            dispatcher = getServletContext().getRequestDispatcher("/htdocs/jsp/Order_CreditEvaluation.jsp");
            dispatcher.forward(request,response);
        } catch (ServletException e) {
            messageError  = "[ServletException][OrderServlet][doEvaluarOrden]["+e.getClass()+" "+e.getMessage()+"]";
            logger.error("[ServletException][OrderServlet][doEvaluarOrden]",e);
            throw new Exception(messageError);
        } catch (IOException e) {
            messageError  = "[ServletException][IOException][doEvaluarOrden]["+e.getClass()+" "+e.getMessage()+"]";
            logger.error("[IOException][OrderServlet][doEvaluarOrden]",e);
            throw new Exception(messageError);
        } catch (Exception e) {
            messageError  = "[ServletException][Exception][doEvaluarOrden]["+e.getClass()+" "+e.getMessage()+"]";
            logger.error("[Exception][OrderServlet][doEvaluarOrden]",e);
            throw new Exception(messageError);
        }
        logger.info("****************** FIN OrderServlet > doEvaluarOrden******************");
    }
    /**
     * <br>Realizado por: <a href="mailto:cesar.barzola@nextel.com.pe">Cesar Barzola</a>
     * <br>Cesar Barzola     07/08/2009  Creación
     * @see pe.com.nextel.dao.ProposedDAO#doValidateProposedOrder(long,long,long,long,String)
     */
    public void doValidateProposedOrder(HttpServletRequest request, HttpServletResponse response, PrintWriter out)throws Exception, ServletException, IOException{

        String cadena=(request.getParameter("hdnTrama")==null?"":request.getParameter("hdnTrama"));
        String strCustomerId =(request.getParameter("txtCompanyId")==null?"":request.getParameter("txtCompanyId"));
        String strProposedId =(request.getParameter("txtPropuesta")==null?"":request.getParameter("txtPropuesta"));
        String strSpecificationId=(request.getParameter("cmbSubCategoria")==null?"":request.getParameter("cmbSubCategoria"));
        String strSellerId=(request.getParameter("cmbVendedor")==null?"":request.getParameter("cmbVendedor"));
        String strOrderId=(request.getParameter("hdnOrderId")==null?"":request.getParameter("hdnOrderId"));

        NewOrderService objNewOrderService= new NewOrderService();
        HashMap hshValidationProp= objNewOrderService.getValidationProposed(MiUtil.parseLong(strOrderId),MiUtil.parseLong(strProposedId),MiUtil.parseLong(strCustomerId),MiUtil.parseLong(strSpecificationId),MiUtil.parseLong(strSellerId),cadena);
        if(hshValidationProp.get(Constante.MESSAGE_OUTPUT)!=null)
        {
            request.setAttribute(Constante.MESSAGE_OUTPUT,(String)hshValidationProp.get(Constante.MESSAGE_OUTPUT));
        }
        request.getRequestDispatcher("pages/validateProposed.jsp").forward(request, response);

    }

    /****
     Method : loadDataSalesmen
     Purpose: Obtiene los  vendedores de Data y las reglas para los combos
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     Jorge Pérez     03/03/2010  Creación
     Karen Salvador  22/06/2010  Se obtiene el valor del delear para setearlo en la Orden.
     * */

    private void loadDataSalesmen(HttpServletRequest request,
                                  HttpServletResponse response,
                                  PrintWriter out) throws Exception{

        GeneralService objGenServ = new GeneralService();
        PortalSessionBean objPortalSessionBean = new PortalSessionBean();
        String strMessage = null;
        String strSpecificationType = null;
        ArrayList arrSalesDataList=null;
        String strUserId          = (request.getParameter("hdnIUserId")==null?"0":request.getParameter("hdnIUserId"));
        String strAppId           = (request.getParameter("hdnIAppId")==null?"0":request.getParameter("hdnIAppId"));
        String strShowDataFields = "0";
        String strLoadSellerData = "0";
        String strDataSalesProvId = "0";
        String strResultValidate = "0";
        String strFlagexclusivity = null;
        String strLoadDealer      = null;
        String hdnSpecification   =  MiUtil.getString(request.getParameter("cmbSubCategoria"));
        String strCustomerId      =  request.getParameter("txtCompanyId");
        String  strSiteId         =   request.getParameter("an_SiteId");
        String strGeneratorType   = MiUtil.getString(request.getParameter("hdnGeneratorType")).toUpperCase();
        long   lGeneratorId       = MiUtil.parseLong(request.getParameter("hdnGeneratorId"));
        String strSalesTeamId     = MiUtil.getString(request.getParameter("cmbVendedor"));
        String strSessionId       = (request.getParameter("hdnUserId")==null?"0":request.getParameter("hdnUserId"));
        objPortalSessionBean      = (new SessionService()).getUserSession(strSessionId);

        long lngSalesStructId     = objPortalSessionBean.getSalesStructId();
        long lngSalesmanId        = objPortalSessionBean.getProviderGrpId();

        if (strSalesTeamId.equalsIgnoreCase(""))
            strSalesTeamId = MiUtil.getString(request.getParameter("hdncmbVendedorVoz"));

        System.out.println("hdnSpecification: "+MiUtil.parseLong(hdnSpecification));
        System.out.println("strGeneratorType: "+strGeneratorType);
        System.out.println("lGeneratorId: "+lGeneratorId);
        System.out.println("strCustomerId: "+strCustomerId);
        System.out.println("strSiteId: "+strSiteId);
        // Valida si la orden se creo desde un incidente u oportunidad de data
        HashMap hshDataValidate = new HashMap();
        hshDataValidate = objGenServ.getSalesDataShow(MiUtil.parseLong(hdnSpecification), strGeneratorType, lGeneratorId,
                MiUtil.parseLong(strCustomerId), MiUtil.parseLong(strSiteId));

        strMessage = (String)hshDataValidate.get("strMessage");
        if (strMessage!=null)
            throw new Exception(strMessage);
        strShowDataFields = (String)hshDataValidate.get("strShowDataFields");
        strLoadSellerData = (String)hshDataValidate.get("strLoadSellerData");
        strDataSalesProvId = (String)hshDataValidate.get("strDataSalesProvId");
        strFlagexclusivity = (String)hshDataValidate.get("strFlagExclusivity");
        strLoadDealer     = (String)hshDataValidate.get("strLoadDealer");
        request.removeAttribute("strLoadSellerData");
        request.setAttribute("strLoadSellerData",strLoadSellerData);
        request.removeAttribute("strShowDataFields");
        request.setAttribute("strShowDataFields",strShowDataFields);
        request.removeAttribute("strDataSalesProvId");
        request.setAttribute("strDataSalesProvId",strDataSalesProvId);
        request.removeAttribute("strFlagexclusivity");
        request.setAttribute("strFlagexclusivity",strFlagexclusivity);
        request.removeAttribute("strLoadDealer");
        request.setAttribute("strLoadDealer",strLoadDealer);

        System.out.println("strShowDataFields: "+strShowDataFields);// Indica si se debe mostrar el combo de data
        System.out.println("strLoadSellerData: "+strLoadSellerData);// Indica si se deben cargar los vendedores de data en el combo de voz
        System.out.println("strDataSalesProvId: "+strDataSalesProvId);// Indica el ganado por de la compañia
        System.out.println("strFlagexclusivity: "+strFlagexclusivity);// Indica si no tiene exlcusividad (N)
        System.out.println("strLoadDealer: "+strLoadDealer);// Indica el dealer que se debe mostrar

        if ( strShowDataFields.equalsIgnoreCase("1") ||  strLoadSellerData.equalsIgnoreCase("1") ){
            //Se debe mostrar el combo de vendedor data o se deben cargar los vendedores data en el combo de vendedor.
            //Se obtiene el listado de vendedores data
            HashMap hshVendedorData = new HashMap();
            hshVendedorData= objGenServ.getSalesDataList( Constante.SALES_RULE_ID_DATA);
            strMessage=(String)hshVendedorData.get("strMessage");
            if (strMessage!=null)
                throw new Exception(MiUtil.getMessageClean(strMessage));

            arrSalesDataList=(ArrayList)hshVendedorData.get("arrSalesDataList");

            request.removeAttribute("arrSalesDataList");
            request.setAttribute("arrSalesDataList",arrSalesDataList);
        }else{
            request.removeAttribute("arrSalesDataList");
        }

    }


    private void validateSalesExclusivity(HttpServletRequest request,
                                          HttpServletResponse response,
                                          PrintWriter out) throws Exception{


        GeneralService objGenServ = new GeneralService();
        String strMessage = null;
        HashMap objHashMap = null;
        String  strSalesmanId ="",strDealerName,strCompanyId="",strSiteId="",strVendedor="",strDealer="";
        long lngSiteId = 0;
        long lngWinnerTypeId = Constante.SALES_DATA_WINNER_TYPE;

        strSalesmanId              =   request.getParameter("an_vendedorid");
        strCompanyId               =   request.getParameter("an_CompanyId");
        strSiteId                  =   request.getParameter("an_SiteId");

        if(strSiteId.equals(""))
            lngSiteId = 0;
        else
            lngSiteId = MiUtil.parseLong(strSiteId);

        System.out.println("strSalesmanId===="+strSalesmanId);

        objHashMap = objGenServ.validateSalesExclusivity(MiUtil.parseLong(strCompanyId), lngSiteId, lngWinnerTypeId, MiUtil.parseLong(strSalesmanId) );
    /*
    if ( strMessage != null )
      throw new Exception(strMessage);
    */
        objHashMap.put("strSalesmanId", strSalesmanId);
        objHashMap.put("strCompanyId", strCompanyId);
        objHashMap.put("strSiteId", strSiteId);
        request.setAttribute("objHashMap",objHashMap);
        RequestDispatcher rd=request.getRequestDispatcher("PAGEEDIT/ResultValidateSalesExclusivity.jsp");
        rd.forward(request,response);

    }

    /**
     * @author GGUANILO
     * @throws java.lang.Exception
     * @param response
     * @param request
     */
    public void doGrabarVep(HttpServletRequest request, HttpServletResponse response) throws Exception,ServletException
    {
        RequestHashMap objHashMap =  new RequestHashMap();
        objHashMap = getParameterNames(request);

        PortalSessionBean objPortalSessionBean = new PortalSessionBean();
        String       strSessionId=(request.getParameter("hdnUserId")==null?"0":request.getParameter("hdnUserId"));
        String strCodBSCS   =   request.getParameter("txtCodBSCS");

        try
        {
            objPortalSessionBean = (new SessionService()).getUserSession(strSessionId);
            System.out.println("OrderServlet/doGrabarVep/getUserSession/strSessionId->"+strSessionId);
            if( objPortalSessionBean == null )
                throw new SessionException("Su sesión ha finalizado. Vuelva a ingresar");

            objHashMap.put("hdnSessionLogin",objPortalSessionBean.getLogin());
            objHashMap.put("strCodBSCS",strCodBSCS);

            HashMap objReturn = new NewOrderService().doSaveVep(objHashMap);

            String strError = (String)objReturn.get("strMessage");
            if (strError!=null)
            {
                throw new Exception(strError);
            }
        }catch(Exception e)
        {
            throw e;
        }
    }

    /**
     * @author RMARTINEZ
     * @throws java.lang.Exception
     * @param response
     * @param request
     */
    public void doDeleteVep(HttpServletRequest request, HttpServletResponse response) throws Exception,ServletException
    {
        RequestHashMap objHashMap =  new RequestHashMap();
        objHashMap = getParameterNames(request);

        PortalSessionBean objPortalSessionBean = new PortalSessionBean();
        String       strSessionId=(request.getParameter("hdnUserId")==null?"0":request.getParameter("hdnUserId"));

        try
        {
            objPortalSessionBean = (new SessionService()).getUserSession(strSessionId);
            System.out.println("OrderServlet/doDeleteVep/getUserSession/strSessionId->"+strSessionId);
            if( objPortalSessionBean == null )
                throw new SessionException("Su sesión ha finalizado. Vuelva a ingresar");

            objHashMap.put("hdnSessionLogin",objPortalSessionBean.getLogin());

            HashMap objReturn = new NewOrderService().doDeleteVep(objHashMap);

            String strError = (String)objReturn.get("strMessage");
            if (strError!=null)
            {
                throw new Exception(strError);
            }
        }catch(Exception e)
        {
            throw e;
        }
    }


    /**
     * @author RMARTINEZ
     * @throws java.lang.Exception
     * @param response
     * @param request
     */
    public String doValidateExistVep(HttpServletRequest request, HttpServletResponse response) throws Exception,ServletException {
        logger.info("************************** INICIO OrderServlet > doValidateExistVep**************************");
        RequestHashMap objHashMap =  new RequestHashMap();
        objHashMap = getParameterNames(request);

        PortalSessionBean objPortalSessionBean = new PortalSessionBean();
        String       strSessionId=(request.getParameter("hdnUserId")==null?"0":request.getParameter("hdnUserId"));
        String strIsVep= null;
        try
        {
            objPortalSessionBean = (new SessionService()).getUserSession(strSessionId);
            logger.info("OrderServlet/doValidateExistVep/getUserSession/strSessionId->"+strSessionId);
            if( objPortalSessionBean == null )
                throw new SessionException("Su sesión ha finalizado. Vuelva a ingresar");

            objHashMap.put("hdnSessionLogin",objPortalSessionBean.getLogin());

            HashMap objReturn = new NewOrderService().doValidateExistVep(objHashMap);

            String strError = (String)objReturn.get("strMessage");
            strIsVep = (String)objReturn.get("strIsVep");
            logger.info("strError: "+strError);
            logger.info("strIsVep: "+strIsVep);
            if (strError!=null)
            {
                throw new Exception(strError);
            }


        }catch(Exception e)
        {
            throw e;
        }
        logger.info("************************** FIN OrderServlet > doValidateExistVep**************************");
        return strIsVep;
    }

    /**
     * @author MSOTO
     * @throws java.lang.Exception
     * @param response
     * @param request
     */
    private void getDealerNameByVendor(HttpServletRequest request,
                                       HttpServletResponse response,
                                       PrintWriter out) throws Exception {

        GeneralService objGeneralService = new GeneralService();
        HashMap objHashMap = null;
        HashMap objHashMap2 = null;
        HashMap hshValidationSaleMan=null;
        String  strSalesmanId ="",strDealerName,strCompanyId="",strSiteId="",strVendedor="",strDealer="";
        Long lngSiteId = null;
        strSalesmanId              =   request.getParameter("an_vendedorid");
        strCompanyId               =   request.getParameter("an_CompanyId");
        strSiteId                  =   request.getParameter("an_SiteId");
        strVendedor                =   request.getParameter("av_Vendedor");
        strDealer                  =   request.getParameter("av_Dealer");
        int iUserId =(request.getParameter("iUserId")==null?0:Integer.parseInt(request.getParameter("iUserId")));
        int iAppId =(request.getParameter("iAppId")==null?0:Integer.parseInt(request.getParameter("iAppId")));
        String strUserId=(String)request.getParameter("iUserId");


        if(strSiteId.equals(""))
            lngSiteId = new Long(0);
        else
            lngSiteId = new Long(strSiteId);

        objHashMap2 = objGeneralService.getValidateSalesman(MiUtil.parseLong(strCompanyId),lngSiteId.longValue(),MiUtil.parseLong(strSalesmanId),strVendedor,iUserId,iAppId);
        if( objHashMap2.get("strMessage2") != null ) {
            out.println(objHashMap2.get("strMessage2"));
        }
    }

    /*Method : validateClientExoneration
  Purpose: Valida si un cliente puede realizar una exoneracion
  Developer       Fecha       Comentario
  =============   ==========  ======================================================================
  Luis Huapaya    05/08/2018  Creación
  */
    public boolean validateClientExoneration(String idOrder){
        boolean isExoneration = false;
        int intNumberValidate = 0;

        BiometricaService biometricaService = new BiometricaService();
        intNumberValidate = biometricaService.validateClientExonerate (Integer.parseInt(idOrder));

        if(intNumberValidate != 0) isExoneration = true;

        return isExoneration;
    }

}