package pe.com.nextel.servlet;

import org.apache.commons.lang.StringUtils;
import pe.com.nextel.bean.DocAssigneeBean;
import pe.com.nextel.bean.VerificationCustomerBean;
import pe.com.nextel.service.BiometricaService;
import pe.com.nextel.service.DigitalDocumentService;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;

public class BiometricaEditServlet extends GenericServlet {

    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
    BiometricaService biometricaService= new BiometricaService();
    Constante constante= new Constante();

    MiUtil miUtil= new MiUtil();
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        System.out.println("BiometricaEditPost");
        doGet(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("BiometricaGet");
        response.setContentType(CONTENT_TYPE);

        String myaction=request.getParameter("myaction");
        if(myaction!=null) {

            if (myaction.equals("anularOrden")) {
                anularOrden(request, response);
            }else if(myaction.equals("listMotivo")){
                listMotivo(request, response);
            }else if (myaction.equals("getValidActivation")) {
                getValidActivation(request, response);
            }else if(myaction.equals("verifPendiente")){
                verifPendiente(request, response);
            }else if(myaction.equals("verifCategoria")){
                verifCategoria(request, response);
            }else if(myaction.equals("listMotivoAislada")){
                listMotivoAislada(request, response); //IOZ12092016I
            }else if(myaction.equals("listAccion")){
                listAccion(request, response); //IOZ12092016I
            }else if(myaction.equals("insertRegistrar")){
                insertRegistrar(request, response); //IOZ12092016I
            }
            }
    }

    private String verifCategoria(HttpServletRequest request, HttpServletResponse response) throws IOException{
       System.out.println("---verifCategoria---");
       String categoria= request.getParameter("strCategoria");
       HashMap hashMap = new HashMap();
       String resultado="";

       try {

           System.out.println("categoria:"+categoria);
           hashMap=biometricaService.getValidarCategoria(categoria);
           System.out.println("[Datos Hash :]" + hashMap);
           resultado=(String)hashMap.get("strResultado");


       }catch (Exception e){
           System.out.print("[verifCategoria :] "+e.getMessage());
       }

        PrintWriter out = response.getWriter();
        out.print(resultado);
        return resultado;
    }

    private String verifPendiente(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("verifPendiente");
        String strmessage="";
        String strmessgaEror="";
        int order=miUtil.parseInt(request.getParameter("order"));
        String selectAccion=request.getParameter("selectAccion");
        String document=request.getParameter("documento");
        String accionNoBiometrica = request.getParameter("accionNoBiometrica");
        HashMap hashMap= new HashMap();


        try {
            System.out.println("[Request verif Pendiente : ] [Order] "+
                    order+" [selectAccion] "+selectAccion+" [document] "+document+" [accionNoBiometrica] "+accionNoBiometrica);
            hashMap=biometricaService.getVerifPendiente(order,document,selectAccion);
            strmessage=(String)hashMap.get("strmessage");
            strmessgaEror=(String)hashMap.get("strMessageError");
            System.out.println("[strmessage:]"+strmessage+"[strMessageError]"+strmessage);

            if(StringUtils.isBlank(strmessgaEror) && StringUtils.isBlank(strmessage) ) {
                strmessage="1";
            }
        }catch ( Exception e ){
            strmessage=e.getMessage();
            System.out.print("[strMessageError:]"+e.getMessage());
        }

        PrintWriter out = response.getWriter();
        out.print(strmessage);
        return strmessage;
    }


    public String  anularOrden(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        System.out.println("ANULAR ORDEN");
        String resultado="";
        String  strMessage = "";
        VerificationCustomerBean verificationCustomerBean= new VerificationCustomerBean();


        int idOrder=miUtil.parseInt(request.getParameter("idOrder"));
        String source=constante.Source_CRM;
        String authorizeduser=request.getParameter("authorizedUser");
        String documento=request.getParameter("documento");
        String type_transaction=constante.type_transaction;
        int status=constante.status_cancelar;
        int action= miUtil.parseInt(request.getParameter("cmbAccion"));
        int motivo= miUtil.parseInt(request.getParameter("cmbmotivo"));
        String login =request.getParameter("login");


        System.out.print("action:"+action+"motivo:"+motivo);


        verificationCustomerBean.setTransaction(idOrder);
        verificationCustomerBean.setSource(source);
        verificationCustomerBean.setAuthorizedUser(authorizeduser);
        verificationCustomerBean.setDocumento(documento);
        verificationCustomerBean.setTypeTransaction(type_transaction);
        verificationCustomerBean.setStatus(status);

        try {
            strMessage=biometricaService.getAnularOrden(verificationCustomerBean,action,motivo,login);
            if(StringUtils.isBlank(strMessage)) {
                resultado="1";
            }
        }catch(Exception e) {
            System.out.println("[BiometricaEditServlet][AnularOrden]"+e.getMessage());
        }

        System.out.println("resultado: "+resultado);

        PrintWriter out = response.getWriter();
        out.print(resultado);
        return resultado;

    }

    public String getValidActivation(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
        try {
              Date fechaInicio = new Date();
              System.out.println("[BiometricaEditServlet] [getValidActivation] [PM0010354] INICIO DEL METODO getValidActivation");
              System.out.println("[BiometricaEditServlet] [getValidActivation] [PM0010354] Fecha y Hora Inicio --> " + fechaInicio);
        }catch(Exception e) {
              System.out.println("[BiometricaEditServlet] [getValidActivation] [PM0010354] Error al pintar Log de Inicio");
        }
        String resultado="";
        System.out.println(" solution: "+request.getParameter("solution"));
        Integer specification=Integer.valueOf(request.getParameter("subCategoria"));
        String idSolution=request.getParameter("solution");
        String login =request.getParameter("login");
        int    order=miUtil.parseInt(request.getParameter("orderId"));
        System.out.println("[specification:]"+specification+"[idSolution]:"+idSolution+"[login:]"+login);
        int int_resultado=0;
        /*Inicio [PRY-0787] Proyecto VIDD
        * Author: Jefferson Vergara
        * Objective: Para el caso que la Orden tenga Apoderado
        * */
        String usecase = Constante.Source_CRM;
        DigitalDocumentService digitalDocumentService = new DigitalDocumentService();
        System.out.println("----- Antes de getDocAssignee, verificar si la Orden tiene Assignee -----");
        HashMap hashAssigne = digitalDocumentService.getDocAssignee(String.valueOf(order));
        String strMessage = (String)hashAssigne.get(Constante.MESSAGE_OUTPUT);
        if(StringUtils.isBlank(strMessage)){
            usecase = Constante.Source_DIGIT;
        }
        System.out.println("Use Case: " + usecase);
        try {
            int resultValidacion=biometricaService.validarSoluciones(idSolution);

            if(resultValidacion>0){

                HashMap   hashResult=biometricaService.getValidActivation(order, specification, login, usecase);
                System.out.println("[resultado:Edit]"+resultado);
                int_resultado= (Integer) hashResult.get("resultado");
            }

            resultado= String.valueOf(int_resultado);

        }catch(Exception e) {
            System.out.println("[BiometricaEditServlet][getValidActivation]"+e.getMessage());
        }

        PrintWriter out = response.getWriter();
        out.print(resultado);

        try {
            Date fechaFin = new Date();
            System.out.println("[BiometricaEditServlet] [getValidActivation] [PM0010354] Fecha y Hora Fin --> " + fechaFin);
            System.out.println("[BiometricaEditServlet] [getValidActivation] [PM0010354] FIN DEL METODO getValidActivation");
        }catch(Exception e) {
            System.out.println("[BiometricaEditServlet] [getValidActivation] [PM0010354] Error al pintar Log Fin");
        }

        return  resultado;
    }


    public String listMotivo(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
        String option= "<option value='0'></option>";
        ArrayList comboAction= new ArrayList();
        String strMessage="";
        HashMap hashMap= new HashMap();
        String domain=request.getParameter("domain");
        String solution = request.getParameter("solution");
        int nbPend = Integer.parseInt(request.getParameter("nbPend"));
        int intDomain=miUtil.parseInt(domain);

        try {
            hashMap=biometricaService.getListReason(intDomain, solution, nbPend);
            strMessage=(String)hashMap.get("strMessage");
            if(StringUtils.isBlank(strMessage)){
                comboAction=(ArrayList)hashMap.get("objArrayList");
                for(int i=0;i<comboAction.size();i++ ){
                    hashMap=(HashMap)comboAction.get(i);
                    String parameterid=(String)hashMap.get("wn_parameterid");
                    String descripcion=(String)hashMap.get("wv_npdescription");
                    option+="<option value="+parameterid+">"+descripcion+"</option>";
                }
            }
        }catch(Exception e) {
            System.out.println("[BiometricaEditServlet][ListMotivo]"+e.getMessage());
        }

        PrintWriter out = response.getWriter();
        out.print(option);
        return  option;
    }





    @Override
    protected void executeDefault(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            logger.debug("Antes de hacer el sendRedirect");
            response.sendRedirect("/portal/page/portal/orders/RETAIL_NEW");
        } catch (Exception e) {
            logger.error(formatException(e));
        }
    }
    /**
     * Motivo: Obtiene los motivos en base a la accion
     * <br>IOZ01092016I</a>
     * <br>Fecha: 12/09/2016
     * @return    HashMap
     * */
    public String listMotivoAislada(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
        String option= "<option value='0'></option>";
        ArrayList comboMotivo= new ArrayList();
        String strMessage="";
        HashMap hashMap= new HashMap();
        String action=request.getParameter("action");
        System.out.print("valor de action: " + action);

        try {
            hashMap=biometricaService.getListReasonAislada(action);
            strMessage=(String)hashMap.get("strMessage");
            if(StringUtils.isBlank(strMessage)){
                comboMotivo=(ArrayList)hashMap.get("objArrayList");
                for(int i=0;i<comboMotivo.size();i++ ){
                    hashMap=(HashMap)comboMotivo.get(i);
                    String parameterid=(String)hashMap.get("wn_parameterid");
                    String descripcion=(String)hashMap.get("wv_npdescription");
                    option+="<option value="+parameterid+">"+descripcion+"</option>";
                }
            }
        }catch(Exception e) {
            System.out.println("[BiometricaServlet][ListMotivoAislada]"+e.getMessage());
        }

        System.out.println(option);

        PrintWriter out = response.getWriter();
        out.print(option);
        return  option;
    }
    /**
     * Motivo: Obtiene las acciones en base al tipo de documento
     * <br>IOZ01092016I</a>
     * <br>Fecha: 12/09/2016
     * @return    HashMap
     * */
    public String listAccion(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
        String option= "<option value='0'></option>";
        ArrayList comboAction= new ArrayList();
        String strMessage="";
        HashMap hashMap= new HashMap();
        String av_type_document=request.getParameter("av_type_document");
        System.out.print("valor de av_type_document: " + av_type_document);

        try {
            hashMap=biometricaService.getListAccion(av_type_document);
            strMessage=(String)hashMap.get("strMessage");
            if(StringUtils.isBlank(strMessage)){
                comboAction=(ArrayList)hashMap.get("objArrayList");
                for(int i=0;i<comboAction.size();i++ ){
                    hashMap=(HashMap)comboAction.get(i);
                    String parameterid=(String)hashMap.get("wn_parameterid");
                    String descripcion=(String)hashMap.get("wv_npdescription");
                    option+="<option value="+parameterid+">"+descripcion+"</option>";
                }
            }
        }catch(Exception e) {
            System.out.println("[BiometricaServlet][listAccion]"+e.getMessage());
        }
        System.out.print("option: "+option);

        PrintWriter out = response.getWriter();
        out.print(option);
        return  option;
    }

    /**
     * Motivo: inserta el registrar
     * <br>IOZ15092016I</a>
     * <br>Fecha: 12/09/2016
     * @return    String
     * */
    private String insertRegistrar(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        System.out.println("insertRegistrar");
        String login= request.getParameter("login");
        String tipoDoc=request.getParameter("tipoDoc");
        String numDoc=request.getParameter("numDoc");
        String accion=request.getParameter("accion");
        String customerId=request.getParameter("customerId");
        String source=constante.Source_CRMVIA;
        String strmessage="";
        try{
            strmessage=biometricaService.insertRegistrar(login, tipoDoc, numDoc, "E", customerId, source);
        }catch (Exception e){
            strmessage=e.getMessage();
            System.out.println("[insertRegistrar:]Message="+e.getMessage());
        }

        PrintWriter out = response.getWriter();
        out.print(strmessage);
        return  strmessage;
    }
}