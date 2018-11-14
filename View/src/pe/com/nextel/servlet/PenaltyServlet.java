package pe.com.nextel.servlet;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import pe.com.nextel.bean.*;
import pe.com.nextel.exception.BpelException;
import pe.com.nextel.exception.ExceptionBpel;
import pe.com.nextel.exception.SessionException;
import pe.com.nextel.exception.UserException;
import pe.com.nextel.service.*;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;
import pe.com.nextel.util.MyUtil;
import pe.com.nextel.util.RequestHashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.*;
import java.util.List;


public class PenaltyServlet extends GenericServlet {

    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
    PenaltyService penaltyService=new PenaltyService();
    WorkflowService   objWorkflowService = new WorkflowService();
    MiUtil miUtil = new MiUtil();

    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
    }

    @Override
    protected void executeDefault(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("************** INICIO PenaltyServlet->executeDefault **************");
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        HashMap h = new HashMap();

        // Variables para estandar.
        String  strMyaction         = null;

        try{

            //Captura la accion a realizar.
            strMyaction         = request.getParameter("method");

            //Captura de acciones
            System.out.println("method: "+strMyaction);
            if ( strMyaction != null ) {
                if(strMyaction.equals("showPenaltySimulatorEdit")){
                    showPenaltySimulatorEdit(request, response);
                }else if(strMyaction.equals("searchPreliquidation")){
                    doGeneratePayOrder(request, response);
                }else{
                    System.out.println("Metodo no especificado ");
                }
            }

        }catch(Exception ex){
            System.out.println("Penalty Servlet : " + ex.getClass() + " - "+ ex.getMessage());
            String variable = ""+ex.getMessage()+"";
            variable  = (MiUtil.getMessageClean(variable)).replaceAll("\\u000a"," ");
            out.println("alert(\""+variable+"\");");
        }
        System.out.println("************** FIN PenaltyServlet->executeDefault **************");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

        String metodo = request.getParameter("method");

        String resul;
        Method method = null;
        try {
            System.out.println("[PenaltyServlet]doGet  return  metodo");
            method = this.getClass().getMethod(metodo, new Class[] { HttpServletRequest.class, HttpServletResponse.class });
            resul = "" + (String) method.invoke(this, new Object[] { request, response });
        }catch(InvocationTargetException ite){
            String strMessage = ite.getTargetException().getMessage();
            resul = "Error [PenaltyServlet][doGet][" + ite.getMessage() + "]";
        }catch (Exception ex) {
            resul = "Error [PenaltyServlet][doGet][" + ex.getMessage() + "]";
        }
        String cad = resul.substring(0,resul.length() - 4);
        response.getWriter().write(cad);
        System.out.println("[PenaltyServlet]Fin - doGet");
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        response.setContentType(CONTENT_TYPE);
        System.out.println("[PenaltyServlet]Inicio - doPost");
        doGet(request, response);
        System.out.println("[PenaltyServlet]Fin - doPost");
    }

    /**
     * Motivo:  Valida si se va a mostrar el link de ver penalidad simulada
     * <br>Realizado por: <a href="mailto:daniel.erazo@hpe.com">Daniel Erazo</a>
     * <br>Fecha: 22/05/2016
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    public void showPenaltySimulatorEdit(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try {
            String strCustomerId = request.getParameter("customerId");
            String strPhone = request.getParameter("phone");

            HashMap hshDataMap = penaltyService.getFlagShowPenaltySimulatorEdit(strCustomerId, strPhone);
            String strMessage = (String)hshDataMap.get(Constante.MESSAGE_OUTPUT);
            int flagPenaltySim = (Integer)hshDataMap.get("flag");

            Gson gson;
            gson = new Gson();

            if(StringUtils.isNotBlank(strMessage)){
                strMessage = gson.toJson(strMessage);
                out.write("{\"strMessage\":"+strMessage+"}");
            }
            else{
                String flag = gson.toJson(flagPenaltySim);
                out.write("{\"flagShowPenaltySim\":"+flag+"}");
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            String msg = "[PenaltyServlet.showPenaltySimulatorEdit]: "+e.getMessage();
            out.write(msg);
            out.close();
        }
    }

    /**
     * Motivo:  Metodo que se invoca al hacer click en el Boton Anular Orden de pago.
     * No tomar como referencia para cualquier tipo de orden de pago!!!
     * <br>Realizado por: <a href="mailto:eddy.flores@hpe.com">Eddy Flores</a>
     * <br>Fecha: 07/08/2015
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    public void anulatePaymentOrder(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        HashMap hashResultAnulateOrder = new HashMap();

        EditOrderService editOrderService= new EditOrderService();
        GeneralService generalService= new GeneralService();
        PortalSessionBean objPortalSesBean = null;

        String strLogin = null;
        int lLoginBuilding = 0;
        String strMessage = null;
        HashMap hashData= new HashMap();
        SpecificationBean objSpecificationBean = new SpecificationBean();

        HashMap hashOrderPenalty = new HashMap();
        Gson gson = new Gson();
        long nporderid = 0;
        try{
            System.out.println("--------------------INICIO ANULACION DE ORDEN DE PAGO DE PENALIDAD DE ADENDA-------------------");
            long npOrderPadreId = miUtil.parseLong(request.getParameter("strOrderId"));
            //Se detecta el switch de apagado universal, si esta apagado mostrara alerta de que se apago
            HashMap hshSwitch = penaltyService.getFlagShowPenaltyPayFunctionality(npOrderPadreId);
            if ((Integer) hshSwitch.get("flag") != 1 ) {
				throw new Exception("Funcionalidad apagada, no se permite ejecutar esta acción");
			}
            hashOrderPenalty = penaltyService.getOrderPenaltyByParentOrderId(npOrderPadreId);
            nporderid = (Long)hashOrderPenalty.get("nporderid");
            strMessage = (String)hashOrderPenalty.get(Constante.MESSAGE_OUTPUT);
            if (strMessage!=null)
                throw new Exception(strMessage);

            String strSessionId = request.getParameter("strSessionId");
            HashMap hshOrderDetail  = editOrderService.getOrder(nporderid);
            OrderBean orderDetail = (OrderBean)hshOrderDetail.get("objResume");

            System.out.println("Specification: " + orderDetail.getNpSpecificationId());
            System.out.println("CustomerId: " + orderDetail.getCsbCustomer().getSwCustomerId());
            System.out.println("DivisionId: " + orderDetail.getNpDivisionId());
            System.out.println("NpStatus: " + orderDetail.getNpStatus());
            System.out.println("NpPaymentTerms: " + orderDetail.getNpPaymentTerms());
            System.out.println("NpPaymentStatus: " + orderDetail.getNpPaymentStatus());
            System.out.println("Order: " + nporderid);

            if(orderDetail.getNpStatus() != null){
                if(orderDetail.getNpStatus().equals(Constante.ORDER_STATUS_CERRADO)){
                    throw new Exception("La orden  ya ha sido cerrada");
                }
                if(orderDetail.getNpStatus().equals(Constante.ORDER_STATUS_ANULADO)){
                    throw new Exception("La orden  ya ha sido anulada");
                }
            }

            hashData=generalService.getSpecificationDetail(orderDetail.getNpSpecificationId());
            System.out.println("metodo getSpecificationDetail "+hashData);
            strMessage=(String)hashData.get("strMessage");
            if (strMessage!=null)
                throw new Exception(strMessage);

            objSpecificationBean=(SpecificationBean)hashData.get("objSpecifBean");
            System.out.println("Bpelflowgroup" + objSpecificationBean.getNpBpelflowgroup());

            objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);

            System.out.println("Code" + objPortalSesBean.getCode());
            System.out.println("Login" + objPortalSesBean.getLogin());

            if( objPortalSesBean == null || objPortalSesBean.getUserid() == 0 )
                throw new Exception("La sesión del usuario ha expirado por favor vuelva a registrarse. Inicie Login");

            hashData.put("objSpecificationBean",objSpecificationBean);
            hashData.put("strOrderId" ,String.valueOf(nporderid) );
            hashData.put("strCustomerId",String.valueOf(orderDetail.getCsbCustomer().getSwCustomerId()) );
            hashData.put("strDivisionId",String.valueOf(orderDetail.getNpDivisionId())  );
            hashData.put("strNpBpelconversationid",orderDetail.getNpBpelconversationid() );
            hashData.put("strActionName",Constante.ACTION_INBOX_ANULAR);
            hashData.put("strNextInboxName","");
            hashData.put("strOldInboxName",  orderDetail.getNpStatus());
            hashData.put("strCurrentInbox",orderDetail.getNpStatus());

            System.out.println("HashParaBPEL:" + hashData);
            WorkflowService objWorkflowService = new WorkflowService();

            hashData= objWorkflowService.doInvokeBPELProcess(hashData, objPortalSesBean);
            String strNextStatus=(String)hashData.get("strNextInbox");
            strMessage=(String)hashData.get("strMessage");

            System.out.println("Siguiente Inbox-->"+strNextStatus);
            System.out.println("Mensaje de Error del BPEL-->"+strMessage);

            hashData= objOrderService.updTimeStamp(nporderid);
            strMessage=(String)hashData.get("strMessage");

            if (strMessage!=null)
                throw new Exception(strMessage);

            //Se quita la relacion de la orden hija.
            System.out.println("[PenaltyServlet.anulatePaymentOrder] Se elimina la orden de pago asignada en los items");
            strMessage = penaltyService.doUpdFastOrderIdPenalty(npOrderPadreId,0,objPortalSesBean.getLogin());

            if (strMessage!=null)
                throw new Exception(strMessage);
            System.out.println("--------------------FIN ANULACION DE ORDEN DE PAGO DE PENALIDAD DE ADENDA-------------------");
            String msg = gson.toJson("Orden "+nporderid+" Anulada");
            out.write("{\"strMessage\":"+msg+"}");
            out.close();
        } catch (Exception ex) {
            String msg = gson.toJson("No se completó la acción : "+nporderid+" : "+ex.getMessage());
            out.write("{\"strMessage\":"+msg+"}");
            out.close();
        }
    }

    /**
     * Motivo:  Metodo que llama al servicio que crear el incidente de penalidad.
     * <br>Realizado por: <a href="mailto:jorge.gabriel@teamsoft.com.pe">Jorge Gabriel</a>
     * <br>Fecha: 26/05/2015
     * @param request
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    public String doUpdFastOrderIdPenalty(HashMap request) throws ServletException, IOException {
        System.out.println("************** INICIO PenaltyServlet->doUpdFastOrderIdPenalty **************");
        String strMessage = null;
        try {

            long lOrderId = MiUtil.parseLong(MiUtil.defaultString(request.get("hdnOrdenId"),"0"));
            long lFastOrderId = MiUtil.parseLong(MiUtil.defaultString(request.get("hdnFastOrdenId"),"0"));
            String strLogin= MiUtil.defaultString(request.get("hdnSessionLogin"),"");

            //atualiza la orden de pago para los items de penalidad.
            System.out.println("Antes de doUpdFastOrderIdPenalty : lOrderId" + lOrderId+", lFastOrderId"+lFastOrderId + " , strLogin " +strLogin);
            strMessage = penaltyService.doUpdFastOrderIdPenalty(lOrderId, lFastOrderId,strLogin);
            System.out.println("Despues de doUpdFastOrderIdPenalty : " + strMessage);
            if (strMessage!=null){
                throw new UserException(strMessage);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            strMessage  = MiUtil.getMessageClean("[Exception][doUpdFastOrderIdPenalty]["+ex.getClass() + " " + ex.getMessage()+"]");
        }
        System.out.println("************** FIN PenaltyServlet->doUpdFastOrderIdPenalty **************");
        return strMessage;
    }

    /**
     * Motivo:  Metodo que se invoca al hacer click en el Boton Generar Pago.
     * <br>Realizado por: <a href="mailto:jorge.gabriel@teamsoft.com.pe">Jorge Gabriel</a>
     * <br>Fecha: 07/08/2015
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    public void doGeneratePayOrder(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        System.out.println("************** INICIO PenaltyServlet->doGeneratePayOrder **************");
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();
        PortalSessionBean objPortalSessionBean = new PortalSessionBean();

        HashMap hashData   = null;
        HashMap hashResult = null;

        String strMessage  = "", msgAlert = "";

        long lOrderId    = 0;

        int flagAmountZero = 0;

        String  strPaymentterms,
                strLogin,
                lOrderPenaltyId ,
                strOrderPenaltyStatus,
                strDivisionId,
                strCustomerId ;

        List<PenaltyBean> penaltyList = null;
        SpecificationBean objSpecificationBean=null;

        String          strSessionId=(request.getParameter("hdnUserId")==null?"0":request.getParameter("hdnUserId"));

        objPortalSessionBean = (new SessionService()).getUserSession(strSessionId);

        if( objPortalSessionBean == null )
            throw new SessionException("Su sesión ha finalizado. Vuelva a ingresar");

        Date df = null;

        try {
        	lOrderId = MiUtil.parseLong(request.getParameter("hdnOrdenId"));
        	System.out.println("lOrderId : "+lOrderId);
            //Se detecta el switch de apagado universal, si esta apagado mostrara alerta de que se apago
            HashMap hshSwitch = penaltyService.getFlagShowPenaltyPayFunctionality(lOrderId);
            if ((Integer) hshSwitch.get("flag") != 1 ) {
				throw new Exception("Funcionalidad apagada, no se permite ejecutar esta acción");
			}
            System.out.println("new request.getParameter(hdnUserId) :"+request.getParameter("hdnUserId"));

            HashMap params = new HashMap(); // <String, String[]>(request.getParameterMap());
            System.out.println("------------------------------------------------");

            /**1.1 Sección de Captura de parametros.
             //------------------------------------
             */
            
            strLogin = objPortalSessionBean.getLogin();
            params.put("hdnSessionAppid", objPortalSessionBean.getAppId());
            params.put("hdnSessionUserid",objPortalSessionBean.getUserid());//falta

            strPaymentterms = request.getParameter("paymentMethodSelect");
            strCustomerId   = request.getParameter("hdnCustomerId");
           
            System.out.println("strLogin : "+strLogin);
            System.out.println("strPaymentterms : "+strPaymentterms);
            System.out.println("strCustomerId : "+strCustomerId);

            String motivos[] = request.getParameterValues("motivoSelect");
            String montoFinals[] = request.getParameterValues("montoFinal");
            String hndItemPenaltyId[] = request.getParameterValues("hndItemPenaltyId");

            penaltyList = new ArrayList<PenaltyBean>();
            PenaltyBean pojoPenaltyBean;
            int i = 0;
            for(String m : motivos){
                pojoPenaltyBean = new PenaltyBean();

                System.out.println("motivo"+(i+1)+" "+m);
                System.out.println("montoFinal"+(i+1)+" "+montoFinals[i]);
                System.out.println("hndItemPenaltyId"+(i+1)+" "+hndItemPenaltyId[i]);

                pojoPenaltyBean.setMontoFinal(MiUtil.parseDouble(montoFinals[i]));
                pojoPenaltyBean.setMotivo(MiUtil.parseInt(m));
                pojoPenaltyBean.setNumItemPenaltyId(hndItemPenaltyId[i]);

                penaltyList.add(pojoPenaltyBean);
                i++;
            }

            //crear la orden de pago
            hashResult = penaltyService.saveOrderPenalty(lOrderId, strPaymentterms, strLogin, penaltyList);
            System.out.println("Despues de saveOrderPenalty : " + hashResult);
            strMessage = MiUtil.defaultString(hashResult.get("strMessage"),null);

            if (strMessage!=null)
                throw new UserException(strMessage);

            flagAmountZero = (Integer)hashResult.get("flagAmountZero");
            System.out.println("flagAmountZero :"+flagAmountZero);

            if(flagAmountZero == 0){
                lOrderPenaltyId = MiUtil.defaultString(hashResult.get("lOrderPenaltyId"),null);
                strOrderPenaltyStatus = MiUtil.defaultString(hashResult.get("strOrderPenaltyStatus"),null);
                strDivisionId = MiUtil.defaultString(hashResult.get("strDivisionId"),null);
                objSpecificationBean = (SpecificationBean) hashResult.get("objSpecificationBean");

                System.out.println("lOrderPenaltyId :"+lOrderPenaltyId);
                System.out.println("strOrderPenaltyStatus :"+strOrderPenaltyStatus);
                System.out.println("strDivisionId :"+strDivisionId);
                System.out.println("objSpecificationBean :"+objSpecificationBean);

                //Invoca a Bpel
                hashData = new HashMap();
                hashData.put("strOrderId", lOrderPenaltyId);
                hashData.put("strNextInboxName",strOrderPenaltyStatus);

                hashData.put("objSpecificationBean", objSpecificationBean);
                hashData.put("strCustomerId",strCustomerId);
                hashData.put("strDivisionId",strDivisionId);

                hashResult = objWorkflowService.doInvokeBPELCreateWorkflow(hashData, objPortalSessionBean);
                strMessage = MiUtil.defaultString(hashResult.get("strMessage"),null);
                System.out.println("OrderServlet/doGrabarOrden/doInvokeBPELCreateWorkflow->"+strMessage);

                if (strMessage!=null)
                    throw new BpelException(strMessage);

                //id de la orden de pago generada
                out.write("1|"+lOrderPenaltyId);
                out.close();
            }
            else{
                msgAlert =  MiUtil.defaultString(hashResult.get("msgFlagAmountZero"),null);
                out.write("2|"+msgAlert);
                out.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            strMessage  = MiUtil.getMessageClean("[Exception][doGeneratePayOrder]["+ex.getClass() + " " + ex.getMessage()+"]");
            out.println(strMessage);
            out.write("2|"+strMessage);
            out.close();
        }
        System.out.println("************** FIN PenaltyServlet-> doGeneratePayOrder **************");
    }

    /**
     * Motivo:  Metodo que se invoca al hacer click en el Boton Cerrar Orden de pago.
     * Se valida previamente que la orden haya sido pagada, debido a algunos problemas en bpel
     * Si la orden ya fue cerrada y el boton aun esta habilitado ya no se hara nada
     * No tomar como referencia para cualquier tipo de orden de pago!!!
     * <br>Realizado por: <a href="mailto:eddy.flores@hpe.com">Eddy Flores</a>
     * <br>Fecha: 16/06/2016
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    public void closePaymentOrder(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        HashMap hashResultCloseOrder = new HashMap();

        EditOrderService editOrderService= new EditOrderService();
        GeneralService generalService= new GeneralService();
        PortalSessionBean objPortalSesBean = null;

        String strLogin = null;
        int lLoginBuilding = 0;
        String strMessage = null;
        HashMap hashData= new HashMap();
        SpecificationBean objSpecificationBean = new SpecificationBean();

        HashMap hashOrderPenalty = new HashMap();
        Gson gson = new Gson();
        long nporderid = 0;
        try{
            System.out.println("--------------------INICIO CERRADO DE ORDEN DE PAGO DE PENALIDAD DE ADENDA-------------------");
            long npOrderPadreId = miUtil.parseLong(request.getParameter("strOrderId"));
            //Se detecta el switch de apagado universal, si esta apagado mostrara alerta de que se apago. Separacion de funcionalidad Cambio de Modelo y Cambio de Plan
            HashMap hshSwitch = penaltyService.getFlagShowPenaltyPayFunctionality(npOrderPadreId);
            
            if ((Integer) hshSwitch.get("flag") != 1 ) {
				throw new Exception("Funcionalidad apagada, no se permite ejecutar esta acción");
			}
            hashOrderPenalty = penaltyService.getOrderPenaltyByParentOrderId(npOrderPadreId);
            nporderid = (Long)hashOrderPenalty.get("nporderid");
            strMessage = (String)hashOrderPenalty.get(Constante.MESSAGE_OUTPUT);
            if (strMessage!=null)
                throw new Exception(strMessage);

            String strSessionId = request.getParameter("strSessionId");
            HashMap hshOrderDetail  = editOrderService.getOrder(nporderid);
            OrderBean orderDetail = (OrderBean)hshOrderDetail.get("objResume");

            System.out.println("Specification: " + orderDetail.getNpSpecificationId());
            System.out.println("CustomerId: " + orderDetail.getCsbCustomer().getSwCustomerId());
            System.out.println("DivisionId: " + orderDetail.getNpDivisionId());
            System.out.println("NpStatus: " + orderDetail.getNpStatus());
            System.out.println("NpPaymentTerms: " + orderDetail.getNpPaymentTerms());
            System.out.println("NpPaymentStatus: " + orderDetail.getNpPaymentStatus());
            System.out.println("Order: " + nporderid);

            if(orderDetail.getNpStatus() != null){
                if(orderDetail.getNpStatus().equals(Constante.ORDER_STATUS_CERRADO)){
                    throw new Exception("La orden  ya ha sido cerrada");
                }
                if(orderDetail.getNpStatus().equals(Constante.ORDER_STATUS_ANULADO)){
                    throw new Exception("La orden  ya ha sido anulada");
                }
            }

            if(orderDetail.getNpPaymentTerms() != null && orderDetail.getNpPaymentStatus() != null){
                if(orderDetail.getNpPaymentTerms().equals(Constante.PAYFORM_CONTADO) && orderDetail.getNpPaymentStatus().equals(Constante.ESTADO_DE_PAGO_ORDEN_PENDIENTE)){
                    throw new Exception("El pago de la orden no esta cancelado");
                }
            }

            hashData=generalService.getSpecificationDetail(orderDetail.getNpSpecificationId());
            System.out.println("metodo getSpecificationDetail "+hashData);
            strMessage=(String)hashData.get("strMessage");
            if (strMessage!=null)
                throw new Exception(strMessage);

            objSpecificationBean=(SpecificationBean)hashData.get("objSpecifBean");
            System.out.println("Bpelflowgroup" + objSpecificationBean.getNpBpelflowgroup());

            objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);

            System.out.println("Code" + objPortalSesBean.getCode());
            System.out.println("Login" + objPortalSesBean.getLogin());

            if( objPortalSesBean == null || objPortalSesBean.getUserid() == 0 )
                throw new Exception("La sesión del usuario ha expirado por favor vuelva a registrarse. Inicie Login");

            hashData.put("objSpecificationBean",objSpecificationBean);
            hashData.put("strOrderId" ,String.valueOf(nporderid) );
            hashData.put("strCustomerId",String.valueOf(orderDetail.getCsbCustomer().getSwCustomerId()) );
            hashData.put("strDivisionId",String.valueOf(orderDetail.getNpDivisionId())  );
            hashData.put("strNpBpelconversationid",orderDetail.getNpBpelconversationid() );
            hashData.put("strActionName",Constante.ACTION_INBOX_AVANZAR);
            hashData.put("strNextInboxName","");
            hashData.put("strOldInboxName",  orderDetail.getNpStatus());
            hashData.put("strCurrentInbox", "");

            System.out.println("HashParaBPEL:" + hashData);
            WorkflowService objWorkflowService = new WorkflowService();

            hashData= objWorkflowService.doInvokeBPELProcess(hashData, objPortalSesBean);
            String strNextStatus=(String)hashData.get("strNextInbox");
            strMessage=(String)hashData.get("strMessage");

            System.out.println("Siguiente Inbox-->"+strNextStatus);
            System.out.println("Mensaje de Error del BPEL-->"+strMessage);

            if (strMessage!=null)
                throw new Exception(strMessage);

            hashData= objOrderService.updTimeStamp(nporderid);
            strMessage=(String)hashData.get("strMessage");

            if (strMessage!=null)
                throw new Exception(strMessage);

            System.out.println("--------------------FIN CERRADO DE ORDEN DE PAGO DE PENALIDAD DE ADENDA-------------------");
            String msg = gson.toJson("Orden "+nporderid+" Cerrada");
            out.write("{\"strMessage\":"+msg+"}");
            out.close();
        } catch (Exception ex) {
            String msg = gson.toJson("No se completó la transacción : "+nporderid+" : "+ex.getMessage());
            out.write("{\"strMessage\":"+msg+"}");
            out.close();
        }
    }

}