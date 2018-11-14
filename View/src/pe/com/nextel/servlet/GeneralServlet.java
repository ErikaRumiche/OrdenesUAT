package pe.com.nextel.servlet;

import java.io.IOException;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import pe.com.nextel.bean.DominioBean;
import pe.com.nextel.bean.IsolatedVerificationBean;
import pe.com.nextel.bean.OrderBean;
import pe.com.nextel.bean.PortalSessionBean;
import pe.com.nextel.service.*;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;


public class GeneralServlet extends HttpServlet
{
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";

    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        //header params
        response.setHeader("Pragma", "No-cache");
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/html; charset=iso-8859-1");

        String metodo = request.getParameter("metodo");
        System.out.println("[GeneralServlet]Inicio - doGet, metodo: " + metodo);
        if ((metodo == null) || ! metodo.startsWith("request")) {
            return;
        }

        String resul;
        Method method = null;
        try {
            method = this.getClass().getMethod(metodo, new Class[] { HttpServletRequest.class, HttpServletResponse.class });
            resul = "OK" + (String) method.invoke(this, new Object[] { request, response });
            System.out.println("Resultado-->"+resul);
        }catch(InvocationTargetException ite){
            String strMessage = ite.getTargetException().getMessage();
            resul = "Error [GeneralServlet][doGet][" + ite.getMessage() + "]";
        }catch (Exception ex) {
            resul = "Error [GeneralServlet][doGet][" + ex.getMessage() + "]";
        }

        if(metodo.equals(Constante.REQUEST_GET_VIA_CUSTOMER)){
            response.getWriter().write("");
        } else {
            response.getWriter().write(resul);
        }

        System.out.println("[GeneralServlet]Fin - doGet");
    }


    public String requestGetNewBillAccId(HttpServletRequest request,HttpServletResponse response)
            throws ServletException,
            IOException
    {
        BillingAccountService objBillAccS=new BillingAccountService();
        HashMap hshResult=null;
        hshResult=(HashMap)objBillAccS.getNewBillAccId();
        String strMessage=(String)hshResult.get("strMessage");
        String strNewBillAccId=null;
        if (strMessage==null)
            strNewBillAccId=(String)hshResult.get("strNewBillAcc");
        else
            return strMessage;

        System.out.println("NUEVO CODIGO GENERADO lNewBillAccId-->"+strNewBillAccId);
        return strNewBillAccId;
    }

    public String requestRespPago(HttpServletRequest request,HttpServletResponse response)
            throws ServletException, IOException {

        long lSpecificationId = (request.getParameter("category")==null?0:Long.parseLong(request.getParameter("category")));
        String strOrigenOrden=(request.getParameter("origen")==null?"":(String)request.getParameter("origen"));

        String strTypeComp = MiUtil.getString((String)request.getParameter("strTypeCompany"));
        long lngCustomerId = MiUtil.parseLong((String)request.getParameter("strCustomerId"));

        long lValor = (request.getParameter("campo1")==null?0:Long.parseLong(request.getParameter("campo1")));
        String strOpcion=(request.getParameter("opcion")==null?"":(String)request.getParameter("opcion"));


        GeneralService	objGeneralService	=	new GeneralService();
        HashMap hshResult=null;
        hshResult=(HashMap)objGeneralService.selectResponsablePago(lSpecificationId,strOrigenOrden,lValor,strOpcion,lngCustomerId,strTypeComp);
        String strMessage=(String)hshResult.get("strMessage");
        String strSelectRP=(String)hshResult.get("strSelectRP");
        if (strMessage!=null){
            System.out.println("[requestRespPago] strMessage: "+strMessage);
            strSelectRP="-1";
        }
        System.out.println("[requestRespPago] lSpecificationId:" + lSpecificationId);
        System.out.println("[requestRespPago] strOrigenOrden:" + strOrigenOrden);
        System.out.println("[requestRespPago] lValor:" + lValor);
        System.out.println("[requestRespPago] strOpcion:" + strOpcion);
        System.out.println("[requestRespPago] lngCustomerId:" + lngCustomerId);
        System.out.println("[requestRespPago] strTypeComp:" + strTypeComp);
        System.out.println("[requestRespPago] strSelectRP:" + strSelectRP);
        return strSelectRP;
    }

    public String requestOrdenCancelada(HttpServletRequest request,HttpServletResponse response)
            throws ServletException, IOException {

        OrderBean objOrderBean=null;
        String strOrdenCancelada=null;

        long lOrderId = (request.getParameter("hdnNumeroOrder")==null?0:Long.parseLong(request.getParameter("hdnNumeroOrder")));
        EditOrderService	objEditOrderService	=	new EditOrderService();
        HashMap hshOrder=null;
        String strMessage=null;
        String strPaymentStatus=null;
        hshOrder=(HashMap)objEditOrderService.getOrder(lOrderId);
        strMessage=(String)hshOrder.get("strMessage");
        if (strMessage!=null){
            System.out.println("[requestOrdenCancelada] strMessage: "+strMessage);
            strOrdenCancelada="-1";
        }
        else{
            objOrderBean=(OrderBean)hshOrder.get("objResume");
            strPaymentStatus=objOrderBean.getNpPaymentStatus();
            System.out.println("[requestOrdenCancelada] strStatusOrden: "+strPaymentStatus);
            if (strPaymentStatus.equalsIgnoreCase("CANCELADO")){
                strOrdenCancelada="1";
            }
            else{
                strOrdenCancelada="0";
            }
        }
        System.out.println("[requestOrdenCancelada] strOrdenCancelada: "+strOrdenCancelada);
        System.out.println("[requestOrdenCancelada] lOrderId:" + lOrderId);
        strOrdenCancelada=strOrdenCancelada+"*"+strPaymentStatus;
        System.out.println("[requestOrdenCancelada] CadenaRetorno:" + strOrdenCancelada);
        return strOrdenCancelada;
    }

    public String requestActionItem (HttpServletRequest request,HttpServletResponse response)
            throws ServletException, IOException {

        OrderBean objOrderBean=null;
        String strOrdenCancelada=null;

        long    lOrderId          = (request.getParameter("hdnNumeroOrder")==null?0:Long.parseLong(request.getParameter("hdnNumeroOrder")));
        String  sOperacionItem    = (request.getParameter("operacion")==null?"":request.getParameter("operacion"));
        String  estadoPagoActual  = (request.getParameter("estadoPagoActual")==null?"":request.getParameter("estadoPagoActual"));

        System.out.println("[requestActionItem] lOrderId:"+lOrderId);
        System.out.println("[requestActionItem] sOperacionItem:"+sOperacionItem);
        System.out.println("[requestActionItem] estadoPagoActual:"+estadoPagoActual);

        EditOrderService	objEditOrderService	=	new EditOrderService();
        HashMap hshOrder=null;
        String strMessage=null;
        String strParametros=null;
        hshOrder=(HashMap)objEditOrderService.getActionItem(lOrderId,sOperacionItem,estadoPagoActual);
        strMessage=(String)hshOrder.get("strMessage");
        strParametros=(String)hshOrder.get("strPermission")+"*"+strMessage;
        System.out.println("[requestActionItem] strParametros:"+strParametros);
        return strParametros;
    }

    /**
     * Motivo: AnulPayOrderPend
     * <br>Realizado por: <a href="mailto:patricia.castillo@nextel.com.pe">Patricia Castillo</a>
     * <br>Fecha: 06/02/2009
     * @param     request
     * @param     response
     */
    public String requestDoSetOrderPayPend(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{

        HashMap hshResult=new HashMap();
        String strMessage = null;
        String srtResult=null;
        String strParametros = null;
        String strUserId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));
        long lOrderId=(request.getParameter("hdnOrderId")==null?0:MiUtil.parseLong(request.getParameter("hdnOrderId")));
        EditOrderService	objEditOrderService	=	new EditOrderService();
        int lPaymentCount=0;
        try{
            hshResult = objEditOrderService.doSetOrderPayPend("10",lOrderId);
            strMessage=(String)hshResult.get("strMessage");

            if (strMessage==null){
                lPaymentCount=MiUtil.parseInt((String)hshResult.get("strCount"));
                System.out.println("Cantidad de Ordenes de Pago Pendientes � Canceladas:"+lPaymentCount);
                if (lPaymentCount > 0){ srtResult="1"; }
                else { srtResult="0";}
            }else{
                srtResult="0";
            }
            strParametros=srtResult+"*"+strMessage;
            System.out.println("[requestDoSetOrderPayPend] srtResult:"+strParametros);

        }catch(Exception e){
            //e.printStackTrace();
            strMessage = e.getMessage();
            srtResult="0";
            strParametros=srtResult+"*"+strMessage;
            System.out.println("catch" + e.getMessage() );
        }
        return strParametros;
    }


    /**
     * Motivo: requestDoSetOrderPayCancel
     * <br>Realizado por: <a href="mailto:patricia.castillo@nextel.com.pe">Patricia Castillo</a>
     * <br>Fecha: 06/02/2009
     * @param     request
     * @param     response
     */
    public String requestDoSetOrderPayCancel(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{


        String strMessage = null;
        String srtResult=null;
        long lOrderId=(request.getParameter("hdnOrderId")==null?0:MiUtil.parseLong(request.getParameter("hdnOrderId")));
        EditOrderService	objEditOrderService	=	new EditOrderService();
        int lPaymentCount=0;
        System.out.println("-----------------Inicio GeneralServlet.java / requestDoSetOrderPayCancel-------------------------");
        try{
            strMessage = objEditOrderService.doSetOrderPayCancel("10",lOrderId);
            lPaymentCount=MiUtil.parseInt((String)strMessage);
            if (lPaymentCount > 0){
                srtResult="1";
            }
            else
            {
                srtResult="0";
            }

        }catch(Exception e){
            strMessage = e.getMessage();
            System.out.println("catch" + e.getMessage() );
        }

        System.out.println("lOrderId-->"+lOrderId);
        System.out.println("-----------------Fin GeneralServlet.java / requestDoSetOrderPayCancel-------------------------");
        System.out.println("strmessage : " + strMessage);

        return srtResult;
    }


    //INICIO - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 30/09/2010
    /**
     * Motivo: loadModel
     * <br>Realizado por: <a href="mailto:marcelo.cejas-echeverria@hp.com">Marcelo Cejas Echeverria</a>
     * <br>Fecha: 30/09/2010
     * @param     request
     * @param     response
     */
    public String requestLoadModel (HttpServletRequest request,HttpServletResponse response){

        System.out.println("-----------------Inicio GeneralServlet.java / requestLoadModel-------------------------");

        String strMessage = null;
        String srtResult="";
        int  deviceTypeId=0;
        HashMap hshResult=new HashMap();
        GeneralService	objService	=	new GeneralService();
        ArrayList arrComboModelsByTypeOfEquipmentList = new ArrayList();

        try{

            deviceTypeId=(request.getParameter("cmbTipoEquipo")==null?0:MiUtil.parseInt(request.getParameter("cmbTipoEquipo")));

            hshResult = objService.getModelsByTypeOfEquipment(deviceTypeId);

            strMessage=(String)hshResult.get("strMessage");

            if (strMessage==null) {
                arrComboModelsByTypeOfEquipmentList = (ArrayList) hshResult.get("arrComboModelsByTypeOfEquipmentList");

                for(int i=0; i <= arrComboModelsByTypeOfEquipmentList.size(); i++){
                    DominioBean dominio = (DominioBean) arrComboModelsByTypeOfEquipmentList.get(i);
                    // srtResult += "|value"+i+"*"+dominio.getDescripcion();
                    srtResult += "|"+dominio.getDescripcion()+"*"+dominio.getDescripcion();
                }
                srtResult +="|";
                // debo formar siempre esta cadena para luego hacer split  "|value1*modelo1|value2*modelo2|...";
            }
        }catch(Exception e){
            strMessage = e.getMessage();
            System.out.println("catch" + e.getMessage() );
        }

        System.out.println("deviceTypeId es -->"+deviceTypeId);
        System.out.println("-----------------Fin GeneralServlet.java / requestLoadModel-------------------------");
        System.out.println("strmessage : " + strMessage);

        return srtResult;
    }
    //FIN - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 30/09/2010

    /**
     * Motivo: Grabar log en la tabla NP_LOG_ITEM
     * <br>Realizado por: <a href="mailto:frank.picoy@hp.com">Frank Picoy</a>
     * <br>Fecha: 10/01/2012
     * @param     request
     * @param     response
     */
    public String requestDoSetLogItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{

        HashMap hshResult=new HashMap();
        String strMessage = null;
        String srtResult=null;
        String strParametros = "*"; //null;
        String    type_window  =  MiUtil.getString((String)request.getParameter("strTypeWindow"));
        String    strSessionId =  MiUtil.getString((String)request.getParameter("strSessionId"));
        PortalSessionBean objSessionBean2 = (PortalSessionBean)SessionService.getUserSession(strSessionId);
        System.out.println("****************************************************INICIO LOG ITEM***************************************************");

        System.out.println("El texto a imprimir y grabar en BD es-->" + request.getParameter("strTexto").toString());

        System.out.println("****************************************************FIN LOG ITEM***************************************************");
        GeneralService	objGeneralService	=	new GeneralService();
        try {
            HashMap objLogItem = new HashMap();
            objLogItem.put("nporderid",request.getParameter("strOrderId").toString());
            objLogItem.put("npinbox",type_window);
            objLogItem.put("npdescription",request.getParameter("strTexto").toString().length()<2000?request.getParameter("strTexto").toString():request.getParameter("strTexto").toString().substring(0,2000));
            objLogItem.put("npcreatedby",objSessionBean2==null?"MWONG2":objSessionBean2.getLogin());
            srtResult = objGeneralService.doSaveLogItem(objLogItem);

        } catch(Exception e){

        }
        return strParametros;
    }
    //EFLORES PM0010274
    public String requestVerifyUserCanSeeCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        String userId = MiUtil.getString((String)request.getParameter("strUserId"));
        String objectType = MiUtil.getString((String) request.getParameter("strObjectType"));
        String objectId = MiUtil.getString((String) request.getParameter("strObjectId"));
        String typeMessage = MiUtil.getString((String) request.getParameter("strTypeMessage"));
        System.out.println("***************************************************************************");
        System.out.println("---             INICIO GENERALSERVLET.JAVA       ---");
        System.out.println("Metodo requestVerifyUserCanSeeCustomer");
        System.out.println("Parametros de entrada : ");
        System.out.println("UserId = "+userId);
        System.out.println("ObjectType = "+objectType);
        System.out.println("ObjectId = "+objectId);
        System.out.println("TypeMessage = " + typeMessage);
        System.out.println("***************************************************************************");
        GeneralService	objGeneralService	=	new GeneralService();
        String valor = "";
        try {
            valor = objGeneralService.verifyUserCanSeeCustomer(userId,objectType,objectId,typeMessage);
        } catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Valor devuelto = "+valor);
        System.out.println("***************************************************************************");
        return valor;
    }

    /*Method : requestGetViaCustomer
  Purpose: Obtiene las verificaciones aisladas en base a un customerId, verificationId y rowNum
  Developer       Fecha       Comentario
  =============   ==========  ======================================================================
  Paul Zacarias   27/09/2016  Creación
  */
    public void requestGetViaCustomer(HttpServletRequest request,
                                      HttpServletResponse response) throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String resp = "";

        Integer customerId = null;
        String orderId = null;
        Integer specificationId = null;
        Integer selected = null;

        IsolatedVerificationService service = new IsolatedVerificationService();

        try {
            customerId = (request.getParameter("customerId")==null)? null: Integer.parseInt(request.getParameter("customerId"));
            System.out.println("PZF: customerId = " + customerId);
            orderId = (request.getParameter("orderId")==null)? null: String.valueOf(request.getParameter("orderId"));
            System.out.println("PZF: orderId = " + orderId);
            specificationId = (request.getParameter("hdncmbSubCategoria")==null)? null: Integer.parseInt(request.getParameter("hdncmbSubCategoria"));
            System.out.println("PZF: specificationId = " + specificationId);
            selected = (request.getParameter("selected")==null)? null: Integer.parseInt(request.getParameter("selected"));
            System.out.println("PZF: selected = " + selected);

            List<IsolatedVerificationBean> listIsolatedVerification = null;
            System.out.println("requestGetViaCustomer(), listIsolatedVerification1: "+listIsolatedVerification);

            if(selected!=null && selected.intValue() == 1){
                listIsolatedVerification = service.getViaCustomer(customerId, null, Integer.parseInt(orderId), specificationId);
                //listIsolatedVerification=obtenerSoloSeleccionado(listIsolatedVerification, orderId);
            } else {
                listIsolatedVerification = service.getViaCustomer(customerId, null, null, specificationId);
                //listIsolatedVerification=obtenerNoSeleccionados(listIsolatedVerification);
            }
            System.out.println("obtenerNoSeleccionados(), listIsolatedVerification2: "+listIsolatedVerification);

            Map<String, Object> respJson = new HashMap<String, Object>();
            respJson.put("listIsolatedVerification", listIsolatedVerification);

            Gson gson = new Gson();
            resp = gson.toJson(respJson);

            out.write(resp);
        } catch(Exception e){
            e.printStackTrace();
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            response.getWriter().write(e.getMessage());
            response.flushBuffer();
        } finally {
            out.close();
        }

    }

    private List<IsolatedVerificationBean> obtenerSoloSeleccionado(List<IsolatedVerificationBean> listIsolatedVerification, String orderId){
        System.out.println("obtenerSoloSeleccionado()");
        List<IsolatedVerificationBean> listTemp = new ArrayList<IsolatedVerificationBean>();
        for(IsolatedVerificationBean isolatedVerificationBean: listIsolatedVerification){
            String nptypetransaction = isolatedVerificationBean.getNptypetransaction();
            Integer nptransaction = isolatedVerificationBean.getNptransaction();
            if((nptypetransaction.equals(Constante.type_transaction)) && nptransaction == Integer.parseInt(orderId)){
                listTemp.add(isolatedVerificationBean);
                break;
            }
        }
        System.out.println("obtenerNoSeleccionados(), listTemp: "+listTemp);

        return listTemp;
    }

    private List<IsolatedVerificationBean> obtenerNoSeleccionados(List<IsolatedVerificationBean> listIsolatedVerification){
        System.out.println("obtenerNoSeleccionados()");
        List<IsolatedVerificationBean> listTemp = new ArrayList<IsolatedVerificationBean>();
        for(IsolatedVerificationBean isolatedVerificationBean: listIsolatedVerification){
            Integer nptransaction = isolatedVerificationBean.getNptransaction();
            System.out.println("obtenerNoSeleccionados(), nptransaction: "+nptransaction);
            if(nptransaction == null || nptransaction.equals("") || nptransaction.intValue()==0){
                listTemp.add(isolatedVerificationBean);
            }
        }
        System.out.println("obtenerNoSeleccionados(), listTemp: "+listTemp);
        return listTemp;
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        response.setContentType(CONTENT_TYPE);
        System.out.println("[GeneralServlet]Inicio - doPost");
        doGet(request, response);
    }

    /*PBI000000042016*/
    public void validarNuevoRespPago(HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {
        System.out.println("[GeneralServlet]Inicio - validarNuevoRespPago");
        SiteService siteService = new SiteService();
        Long orderId = Long.parseLong((String)request.getParameter("ordenId"));
        Long siteId = null;
        siteId = siteService.getUnknownSite(orderId);
        if(siteId == null){
            siteId = 0L;
        }

        response.getWriter().print(siteId);
    }

    /*PBI000000042016*/
    public void validarEspecResPago(HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {

        System.out.println("[GeneralServlet]Inicio - validarEspecResPago");
        String especificacionId = (String)request.getParameter("especificacionId");
        
        System.out.println("especificacionId: " + especificacionId);
        GeneralService objGeneralService = new GeneralService();
        HashMap mapEspecificacionResPago = objGeneralService.getTableList("SINC_RESP_SPEC", "a");
        ArrayList <HashMap> arrEspecificacionResPago = (ArrayList <HashMap>)mapEspecificacionResPago.get("arrTableList");
        int contador=0;
        if (arrEspecificacionResPago!=null){
            for(HashMap config: arrEspecificacionResPago){
                System.out.println("especificacion configurada: " + (String)config.get("wv_npValue"));
                if(especificacionId.equals((String)config.get("wv_npValue"))){
                    contador++;
                }
            }
        }
        System.out.println("contador " + contador);
        response.setContentType("text/xml");
        response.getWriter().print(contador);
    }
}