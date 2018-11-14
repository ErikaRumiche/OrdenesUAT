package pe.com.nextel.servlet;

import org.apache.commons.lang.StringUtils;
import pe.com.nextel.bean.*;
import pe.com.nextel.exception.ExceptionBpel;
import pe.com.nextel.service.*;
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


public class BiometricaServlet extends GenericServlet {
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
    BiometricaService biometricaService= new BiometricaService();
    Constante constante= new Constante();

    MiUtil miUtil= new MiUtil();

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        System.out.println("BiometricaPost");
        doGet(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("BiometricaGet");
        response.setContentType(CONTENT_TYPE);

        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>BIOMETRICA</title></head>");
        out.println("<script >");

        String myaction=request.getParameter("myaction");
        System.out.println("myaction:"+myaction);
        if(myaction!=null) {

            if (myaction.equals("validOrderPendientes")) {
                validOrderPendientes(request, response);
            } else if(myaction.equals("verificacionBiometricaNew")){
                verificacionBiometricaNew(request, response, out);
            }else if(myaction.equals("openBiometricOrderEdit")){
                openBiometricOrderEdit(request, response, out);
            }else if(myaction.equals("verificacionBiometricaEdit")){
                verificacionBiometricaEdit(request, response, out);
            }else if(myaction.equals("verificacionBiometricaAislada")){
                verificacionBiometricaAislada(request, response, out);				
            }else if(myaction.equals("anularVerificacion")){
                anularVerificacion(request, response, out);
            }else if(myaction.equals("verificacionNobiometrica")){
                verificacionNobiometrica(request, response, out);
            }else if(myaction.equals("validdaNoBiometrica")){
                validdaNoBiometrica(request, response, out);
            } else if (myaction.equals("validdaNoBiometricaAislada")) {
                validdaNoBiometricaAislada(request, response, out);
            }else if(myaction.equals("cancelNoBiometrico")){
                cancelNoBiometrico(request, response, out);
            }else if(myaction.equals("anularOrden")){
                anularOrden(request, response, out);
            }else if(myaction.equals("registerExonerate")){
                registerExonerate(request, response, out);
            }
        }
        out.println("</script>");
        out.close();
    }


    private void anularOrden(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        System.out.println("------anularOrdenEdit-----");

        String  resultado="";
        String  strMessage = "";
        VerificationCustomerBean verificationCustomerBean= new VerificationCustomerBean();

        Integer idOrder= miUtil.parseInt(request.getParameter("idOrder"));
        String  source=constante.Source_CRM;
        String  type_transaction=constante.type_transaction;
        int     status=constante.status_cancelar;

        Integer action=miUtil.parseInt(request.getParameter("cmbAccion"));
        Integer motivo=miUtil.parseInt(request.getParameter("cmbMotivo"));
        String  authorizeduser= request.getParameter("hdnAuthorizedUser");
        String  login= request.getParameter("hdnSessionLogin");
        String  documento= request.getParameter("Rut");
        String  origen= request.getParameter("origen");
        String idsesion=request.getParameter("hdUserId");

        verificationCustomerBean.setTransaction(idOrder);
        verificationCustomerBean.setSource(source);
        verificationCustomerBean.setAuthorizedUser(authorizeduser);
        verificationCustomerBean.setDocumento(documento);
        verificationCustomerBean.setTypeTransaction(type_transaction);
        verificationCustomerBean.setStatus(status);

        try {
            strMessage=biometricaService.getAnularOrden(verificationCustomerBean,action,motivo,login);
            if(StringUtils.isBlank(strMessage)) {
                if(origen.equals("edit")) {
                    out.println("parent.opener.parent.mainFrame.editOrderBiometric('ANULAR')");
                    out.println("parent.close()");
                } else if (origen.equals("new")) {
                    this.anularOrdenNewBiometrica(idOrder,idsesion);
                    out.println("parent.opener.parent.mainFrame.redirectOrder("+idOrder+")");
                    out.println("parent.close()");
                }
            }
        }catch(Exception e) {
            System.out.println("[BiometricaEditServlet][AnularOrden]" + e.getMessage());
        }
    }

    /*Method : anularVerificacion
    Purpose: Anula la orden , segun la accion el motivo
    Developer       Fecha       Comentario
    =============   ==========  ======================================================================
    Ricardo Quispe  28/08/2015  Creacion
    */
    private void anularVerificacion(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        System.out.println("anularVerificacion");
        int   idOrder=miUtil.parseInt(request.getParameter("idOrder"));
        String login= request.getParameter("hdnSessionLogin");
        String authorizedUser=request.getParameter("hdnAuthorizedUser");
        String source=constante.Source_CRM;
        String strmessage="";
        DigitalDocumentService objDigitalizationService = new DigitalDocumentService();

        try{
            strmessage=biometricaService.getAnularVerificacion(idOrder,login,authorizedUser,source);

            if(!StringUtils.isBlank(strmessage)) {
                out.println("alert(\"" + strmessage+"\")");
                //JVERGARA PRY 0787
                HashMap objhash;
                //Obtener el Tipo de transaccion de la Orden
                int trxType = -1;
                HashMap objhashDoc;
                objhashDoc = objDigitalDocumentService.getDocumentGeneration(String.valueOf(idOrder));
                String message = (String)objhashDoc.get(Constante.MESSAGE_OUTPUT);
                if(StringUtils.isBlank(message)){
                    trxType = ((DocumentGenerationBean)objhashDoc.get("documentGenerationBean")).getTrxType();
                }
                System.out.println("Tipo de TransacciÛn 1:Todos, 2:Venta, 3:Portabilidad, 4:PostVenta : "+trxType);

                if(Constante.TRX_TYPE_PORTABILIDAD_ID==trxType) {
                    out.println("alert(\"" + Constante.MESSAGE_NO_BIOMETRIC_PORTAB + "\")");
                    objhash = objDigitalizationService.updateGenerationVI(idOrder, login);
                    if (StringUtils.isBlank((String) objhash.get(Constante.MESSAGE_OUTPUT))) {
                        System.out.println("[OrderServlet] Actualizando Generacion de documento si existe OrderId: " + ((DocumentGenerationBean) objhash.get("documentGenerationBean")).getOrderId());
                    } else {
                        System.out.println("[OrderServlet] Actualizando Generacion de documento si existe " + objhash.get(Constante.MESSAGE_OUTPUT));

                    }

                }
            }else{
                out.println("parent.opener.parent.mainFrame.redirectOrder("+idOrder+")");
                out.println("parent.close()");
            }
        } catch (Exception e) {
            System.out.println("[anularVerificacion:]Message="+e.getMessage());
        }
    }

    /*Method : validOrderPendientes
    Purpose: Valida antes de ingresar a una verificacion biometrica o viceversa, no tenga pendiente de verificacion
    Developer       Fecha       Comentario
    =============   ==========  ======================================================================
    Ricardo Quispe  28/08/2015  CreaciÛn
    */
    private void validOrderPendientes(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        System.out.println("----------- validOrderPendientes ----------------");
        PrintWriter out = response.getWriter();
        HashMap hasMap= new HashMap();
        int  idOrder=Integer.valueOf(request.getParameter("idOrder"));

        hasMap=biometricaService.validOrderPendientes(idOrder);
        String resultado= hasMap.get("resultado").toString();
        System.out.println("Resultado :" + resultado);
        out.println(resultado);
        out.close();

    }


    /*Method : verificacionBiometricaNew
    Purpose: Valida una veridicacion cuando la orden es creada.
    Developer       Fecha       Comentario
    =============   ==========  ======================================================================
    Ricardo Quispe  28/08/2015  CreaciÛn
    LROSALES        22/09/2016  Se agrega un parametro useCase para obtener identificar la aplicacion que requiere
                                una verificacion de identidad
    */
    private void verificacionBiometricaNew(HttpServletRequest request, HttpServletResponse response,PrintWriter out) {
        System.out.println("verificacionBiometricaNew");
        VerificationCustomerBean verificationCustomerBean= new VerificationCustomerBean();

        DigitalDocumentService objDigitalizationService = new DigitalDocumentService();
        BiometricBean  biometricBean= new BiometricBean();

        int   idOrder=miUtil.parseInt(request.getParameter("idOrder"));
        String document=request.getParameter("Rut");
        String ercDesc=request.getParameter("ErcDesc");
        String nombres=request.getParameter("Nombres");
        String apPaterno=request.getParameter("ApPaterno");
        String apMaterno=request.getParameter("ApMaterno");
        String vigencia=request.getParameter("Vigencia");
        String nroAudit=request.getParameter("NroAudit");
        String restriccion=request.getParameter("Restriccion");
        String login =request.getParameter("hdnSessionLogin");
        int flagDni=miUtil.parseInt(request.getParameter("hdflagDni"));
        String codError=request.getParameter("codError");
        String athorizedUser=request.getParameter("hdnAuthorizedUser");
        String ercAcepta=request.getParameter("hdErcAcepta");
        String errorAcepta=request.getParameter("hdErrorAcepta");

        String athorizedDni=request.getParameter("hdnAuthorizedDni");
        String athorizedPass=request.getParameter("hdnAuthorizedPass");
        String idSession=request.getParameter("hdUserId");

        String useCase = request.getParameter("hdSource");
        HashMap responseRule= new HashMap();

        System.out.println("document:"+document+"codError:"+codError+"restriccion:"+restriccion+" ercAcepta:"+ ercAcepta+" Pass:"+athorizedPass+
                "ErrorAcepta:"+errorAcepta+" -idSession: "+idSession);

        String strMessage="";
        String strmotivo="";
        String straccion="";
        String strMessageError="";

        try {
            responseRule=biometricaService.getResponseRule( idOrder,document,codError,restriccion,
                    constante.Tipo_Biometrica,constante.Source_CRM,flagDni,0,ercAcepta);


            //consultar si es registro nuevo qcountRegistro  qcountRegistro
            straccion=(String)responseRule.get("straccion");
            strmotivo=(String)responseRule.get("strmotivo");
            strMessage=(String)responseRule.get("strMessage");
            strMessageError=(String)responseRule.get("strMessageError");
            System.out.println("[straccion:]"+straccion+" [strmotivo:]"+strmotivo+" [strMessageError:]" + strMessageError+
                    "[ercDesc:]"+ercDesc);


            verificationCustomerBean.setVerificationType(constante.Tipo_Biometrica);
            verificationCustomerBean.setSource(constante.Source_CRM);
            verificationCustomerBean.setMotive(strmotivo);
            verificationCustomerBean.setAuthorizedUser(athorizedUser);
            verificationCustomerBean.setDocumento(document);
            verificationCustomerBean.setTransaction(idOrder);
            verificationCustomerBean.setTypeTransaction(constante.type_transaction);


            biometricBean.setObservacion(restriccion);
            biometricBean.setAuditoria(nroAudit);
            biometricBean.setRenicCode(codError);
            biometricBean.setReniecResponse(ercDesc);
            biometricBean.setValityDni(document);
            biometricBean.setName(nombres);
            biometricBean.setFather(apPaterno);
            biometricBean.setMother(apMaterno);
            biometricBean.setStatus(straccion);


            String strMessageRule=strMessage;
            if (!StringUtils.isBlank(strMessageError)) {
                out.println("alert(\"" + strMessageError+"\");");
                //Muestra mensaje parametriazdo en np_rule
            } else {

                //Si es Exitoso no mostrar mensaje
                if (!straccion.equals(constante.action_C)) {
                    out.println("alert(\"" + strMessage + "\");");
                }
                System.out.println("MessageRule:[" + strMessage + "]");
                strMessage = biometricaService.getActionBiometric(straccion, verificationCustomerBean, biometricBean, login);
                System.out.println("MessageAction:[" + strMessage + "]");

                if (StringUtils.isBlank(strMessage)) {

                    if (straccion.equals(constante.action_R)) {
                        // MMONTOYA
                        // Se habilita el botÛn en caso de reintento.
                        out.println("parent.mainFrame.document.getElementById('btnVerificacion').disabled=false;");
                    } else if (straccion.equals(constante.action_A)) {
                        this.anularOrdenNewBiometrica(idOrder,idSession);
                        out.println("parent.opener.parent.mainFrame.redirectOrder(" + idOrder + ")");
                        out.println("parent.close()");
                    } else if (straccion.equals(constante.action_C)) {
                        System.out.println("Mensaje:" + strMessageRule);
                        String strvalores = "idOrder=" + idOrder + "&strDocument=" + document + "&strName=" + nombres +
                                "&strApePat=" + apPaterno + "&strApemat=" + apMaterno + "&strVigencia=" + vigencia +
                                "&strRestriccion=" + restriccion + "&message=" + strMessageRule;

                        //JVERGARA PRY 0787
                        HashMap objhash;
                        //Obtener el Tipo de transaccion de la Orden
                        int trxType = -1;
                        HashMap objhashDoc;
                        objhashDoc = objDigitalDocumentService.getDocumentGeneration(String.valueOf(idOrder));
                        String message = (String)objhashDoc.get(Constante.MESSAGE_OUTPUT);
                        if(StringUtils.isBlank(message)){
                            trxType = ((DocumentGenerationBean)objhashDoc.get("documentGenerationBean")).getTrxType();
                        }
                        System.out.println("Tipo de TransacciÛn 1:Todos, 2:Venta, 3:Portabilidad, 4:PostVenta : "+trxType);
                        if(Constante.TRX_TYPE_PORTABILIDAD_ID==trxType) {
                            objhash = objDigitalizationService.updateGenerationVI(idOrder, login);
                            if (StringUtils.isBlank((String) objhash.get(Constante.MESSAGE_OUTPUT))) {
                                System.out.println("[OrderServlet] Actualizando Generacion de documento si existe OrderId: " + ((DocumentGenerationBean) objhash.get("documentGenerationBean")).getOrderId());
                            } else {
                                System.out.println("[OrderServlet] Actualizando Generacion de documento si existe " + objhash.get(Constante.MESSAGE_OUTPUT));
                            }
                        }


                        out.println("parent.mainFrame.location.replace('POPUPBIOMETRICA/VerificacionBiometrica.jsp?" + strvalores + "');");

                    } else if (straccion.equals(constante.action_NB) || straccion.equals(constante.action_D)) {
                        this.OpenNoBiometrico(out, "new", strmotivo, idOrder, athorizedUser, document, athorizedPass, idSession, useCase, null, null, login);
                    }

                } else {
                    out.println("alert(\"" + strMessage + "\");");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("FIN");
    }


    /*Method : verificacionBiometricaEdit
    Purpose: Valida una veridicacion cuando la orden es editada.
    Developer       Fecha       Comentario
    =============   ==========  ======================================================================
    Ricardo Quispe  28/08/2015  Creacion
    LROSALES        22/09/2016  Se agrega un parametro useCase para obtener identificar la aplicacion que requiere
                                una verificacion de identidad
    LHUAPAYA        28/09/2016  Se agrega condicionales para poder manejar la verificacion de identidad aislada
    */
    private void verificacionBiometricaEdit(HttpServletRequest request, HttpServletResponse response,PrintWriter out) {
        System.out.println("verificacionBiometricaEdit");
        VerificationCustomerBean verificationCustomerBean= new VerificationCustomerBean();
        BiometricBean  biometricBean= new BiometricBean();

        int   idOrder=miUtil.parseInt(request.getParameter("idOrder"));
        String document=request.getParameter("Rut");
        String ercDesc=request.getParameter("ErcDesc");
        String nombres=request.getParameter("Nombres");
        String apPaterno=request.getParameter("ApPaterno");
        String apMaterno=request.getParameter("ApMaterno");
        String vigencia=request.getParameter("Vigencia");
        String nroAudit=request.getParameter("NroAudit");
        String restriccion=request.getParameter("Restriccion");
        String login =request.getParameter("hdnSessionLogin");
        int    flagDni=miUtil.parseInt(request.getParameter("hdflagDni"));
        String codError=request.getParameter("codError");
        String athorizedUser=request.getParameter("hdnAuthorizedUser");
        String athorizedDni=request.getParameter("hdnAuthorizedDni");
        String athorizedPass=request.getParameter("hdnAuthorizedPass");
        String ercAcepta=request.getParameter("hdErcAcepta");
        String errorAcepta=request.getParameter("hdErrorAcepta");
        String docType = request.getParameter("hdnTipodoc");

        //String useCase = request.getParameter("hdSource");

        String source = request.getParameter("hdSource");
        System.out.println("SOURCE EN verificacionBiometricaEdit: " + source);
        int customerid = 0;
        if (request.getParameter("hdnCustomerId") != null)
            customerid = Integer.parseInt(request.getParameter("hdnCustomerId"));

        HashMap responseRule= new HashMap();

        System.out.println("document:"+document+"codError:"+codError+"restriccion:"+restriccion+" pass:"+athorizedPass+
                "ErrorAcepta:"+errorAcepta);

        String strMessage="";
        String strmotivo="";
        String straccion="";
        String strMessageError="";
        String strSource = "";

        try {
            if (constante.SOURCE_VIA.equals(source)) {
                strSource = constante.SOURCE_VIA;
                responseRule = biometricaService.getResponseRuleVIA(document, codError, restriccion, constante.Tipo_Biometrica, constante.SOURCE_VIA, flagDni, 0, ercAcepta,customerid);
            } else{
                strSource = constante.Source_CRM;
                responseRule = biometricaService.getResponseRule(idOrder, document, codError, restriccion, constante.Tipo_Biometrica, constante.Source_CRM, flagDni, 0, ercAcepta);
            }



            //consultar si es registro nuevo qcountRegistro  qcountRegistro
            straccion=(String)responseRule.get("straccion");
            strmotivo=(String)responseRule.get("strmotivo");
            strMessage=(String)responseRule.get("strMessage");
            strMessageError=(String)responseRule.get("strMessageError");
            System.out.println("[straccion:]"+straccion+" [strmotivo:]"+strmotivo+" [strMessageError:]" + strMessageError);


            verificationCustomerBean.setVerificationType(constante.Tipo_Biometrica);
            verificationCustomerBean.setSource(strSource);
            verificationCustomerBean.setMotive(strmotivo);
            verificationCustomerBean.setAuthorizedUser(athorizedUser);
            verificationCustomerBean.setDocumento(document);
            verificationCustomerBean.setTransaction(idOrder);
            verificationCustomerBean.setTypeTransaction(constante.type_transaction);
            verificationCustomerBean.setDocType(docType);


            biometricBean.setObservacion(restriccion);
            biometricBean.setAuditoria(nroAudit);
            biometricBean.setRenicCode(codError);
            biometricBean.setReniecResponse(ercDesc);
            biometricBean.setValityDni(document);
            biometricBean.setName(nombres);
            biometricBean.setFather(apPaterno);
            biometricBean.setMother(apMaterno);
            biometricBean.setStatus(straccion);
            //vigencia??

            String strMessageRule=strMessage;
            if (!StringUtils.isBlank(strMessageError)) {
                out.println("alert(\"" + strMessageError+"\");");
                //se muestra mensaje parametrizado en np_rule
            } else {

                //Si es Exitoso no mostrar mensaje
                if (!straccion.equals(constante.action_C)) {
                    out.println("alert(\"" + strMessage.trim() + "\");");
                }

                System.out.println("MessageRule:[" + strMessage + "]");
                if (constante.SOURCE_VIA.equals(source))
                    strMessage = biometricaService.getActionBiometricVIA(straccion, verificationCustomerBean, biometricBean, login, customerid);
                else
                strMessage = biometricaService.getActionBiometric(straccion, verificationCustomerBean, biometricBean, login);
                System.out.println("MessageAction:[" + strMessage + "]");

                if (StringUtils.isBlank(strMessage)) {

                    String url = "";
                    String datos = "";
                    if (straccion.equals(constante.action_R)) {
                        // MMONTOYA
                        // Se habilita el boton en caso de reintento.
                        out.println("parent.mainFrame.document.getElementById('btnVerificacion').disabled=false;");
                    } else if (straccion.equals(constante.action_A)) {
                        if(constante.SOURCE_VIA.equals(source)){
                            out.println("parent.mainFrame.document.getElementById('btnRefreshForm').click();");
                        }else{
                        out.println("parent.opener.parent.mainFrame.editOrderBiometric('ANULAR')");
                        out.println("parent.close()");
                        }
                    } else if (straccion.equals(constante.action_C)) {

                        System.out.println("Mensaje:" + strMessageRule);
                        datos = "idOrder=" + idOrder + "&strDocument=" + document + "&strName=" + nombres +
                                "&strApePat=" + apPaterno + "&strApemat=" + apMaterno + "&strVigencia=" + vigencia +
                                "&strRestriccion=" + restriccion + "&message=" + strMessageRule;
                        url = "POPUPBIOMETRICA/VerificacionBiometricaEdit.jsp?" + datos;

                        if (constante.SOURCE_VIA.equals(source)) {
                            out.println("parent.mainFrame.document.getElementById('btnRefreshForm').click();");
                            out.println("window.open(\"" + url + "&strSource=" + constante.SOURCE_VIA + "\", \"Verificacion\",\"toolbar = no, location = no, directories = no, status = no, menubar = no, scrollbars = yes, resizable = yes, screenX = 60, top = 40, left = 60, screenY = 40, width = 620,height=400\"); ");
                        } else out.println("parent.mainFrame.location.replace('" + url + "');");

                    } else if (straccion.equals(constante.action_NB) || straccion.equals(constante.action_D)) {
                        this.OpenNoBiometrico(out, "edit", strmotivo, idOrder, athorizedUser, document, athorizedPass, "", source, docType, String.valueOf(customerid), login);

                    }
                }else{
                    out.println("alert(\"" + strMessage.trim() + "\");");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception:verificacionBiometricaEdit"+e.getMessage());
        }

        System.out.println("FIN");
    }

    /*
    Method : openBiometricOrderEdit
    Purpose:   Abre el popup de verificacion biometrica/no biometrica
    Developer       Fecha       Comentario
    =============   ==========  ======================================================================
    Ricardo Quispe  28/08/2015  Creaci√≥n
    MMONTOYA        04/08/2016 Se modifica por el proyecto Verificaci√≥n Biom√©trica - Fase 2

    Este m√©todo se invoca:
        Al crear la orden desde AppOrders
        Al realizar la activaci√≥n PackSim aislada desde AppPackSim
    */
    private void openBiometricOrderEdit(HttpServletRequest request, HttpServletResponse response,PrintWriter out){
        System.out.println("----- openBiometricOrderEdit -----");

        /*
         * Inicio: Obtiene los par√°metros.
         */

        String useCase = "";
        Integer order = 0;
        String document = "";
        Integer specification = 0;

        DigitalDocumentService digitalDocumentService = new DigitalDocumentService();



        if (request.getParameter("useCase") == null) { // Invocaci√≥n desde AppOrders.
            useCase = Constante.Source_CRM; // Ver procedimiento almacenado de acceso a verificaci√≥n de identidad.
            order = miUtil.parseInt(request.getParameter("idOrder"));
            document = biometricaService.getDocumento(order);
            specification = Integer.valueOf(request.getParameter("hdnSubCategoria"));
        } else { // Invocaci√≥n desde AppPackSim.
            useCase = request.getParameter("useCase");
            document = request.getParameter("documentNumber");
        }

        String login=request.getParameter("hdnSessionLogin");

        /*
         * Fin: Obtiene los par√°metros.
         */

         /*
        * Inicio [PRY-0787] Proyecto VIDD
        * Author: Jefferson Vergara
        * Objective: Para el caso que la Orden tenga Apoderado
        *
        * */
        System.out.println("----- Antes de getDocAssignee, verificar si la Orden tiene Assignee -----");
        DocAssigneeBean docAssigneeBean;
        String assigneeDocNum = "";
        String assigneeDocType = "";
        String assigneeValueDoc = "";
        HashMap hashAssigne = digitalDocumentService.getDocAssignee(String.valueOf(order));
        HashMap hashDoctypeExo = biometricaService.getTypesDocumentsExoneration();
        String strMessage = (String)hashAssigne.get(Constante.MESSAGE_OUTPUT);
        String strMessage2 = (String)hashDoctypeExo.get(Constante.MESSAGE_OUTPUT);
        if(StringUtils.isBlank(strMessage) && StringUtils.isBlank(strMessage2)){
            docAssigneeBean = (DocAssigneeBean)hashAssigne.get("docAssigneeBean");
            assigneeDocNum=docAssigneeBean.getNumDoc();
            assigneeDocType=docAssigneeBean.getTypeDoc();

            ArrayList comboTypeDoc=(ArrayList)hashDoctypeExo.get("objArrayList");
            for (Object aux:comboTypeDoc){
                if(assigneeDocType.equals(((HashMap)aux).get("wv_descTypeDoc"))){
                    assigneeValueDoc = (String)(((HashMap)aux).get("wv_valueTypeDoc"));
                }
            }

            document = assigneeDocNum; // El numero de documento del apoderado toma lugar
            useCase = Constante.Source_DIGIT;
        }else{
            System.out.println("----- Message OUTPUT: "+strMessage + " "+strMessage2);
            strMessage=strMessage+strMessage2;
        }

        System.out.println("[order:]" + specification + "[order]:" + specification + "[login:]" + login + "[useCase]:" + useCase);

        try{

            HashMap hashMap = biometricaService.getValidActivation(order, specification, login, useCase);

            String authorizedUser = (String) hashMap.get("authorizedUser");
            String authorizedDni = (String) hashMap.get("authorizedDni");
            String authorizedPass = (String) hashMap.get("authorizedPass");

            String datos= order+"_"+authorizedUser+"_"+login+"_"+authorizedDni+"_"+authorizedPass+"_"+document+"_"+assigneeValueDoc+"_"+assigneeDocType;

            if(validateClientExoneration(order+"")){ // PARA LA EXONERACION DEL CLIENTE
                if(StringUtils.isBlank(strMessage)){ // Existe Apoderado para la Orden
                    if(assigneeDocType.equals(constante.TIPO_DOC_DNI)){ //Apoderado DNI
                        System.out.println("Para apoderado con documento de identidad DNI se mostrar· la VI " +datos);
                        String url="VerificacionEdit.jsp?datos="+datos;
                        String winUrl = "POPUPBIOMETRICA/PopUpVerifFrame.jsp?idOrderPopup="+order+"&action=edit"+"&authorizedDni="+authorizedDni+"&login="+login+"&av_url="+url;
                        out.println("window.open(\""+winUrl+"\", \"Verificacion\",\"toolbar = no, location = no, directories = no, status = no, menubar = no, scrollbars = yes, resizable = yes, screenX = 60, top = 40, left = 60, screenY = 40, width = 620,height=400\"); ");
                    }else{
                        System.out.println("Para apoderado con documento de identidad distinto de DNI se mostrar· la VI " +datos);
                        String url = "VerificationExonerationEdit.jsp?datos="+datos;
                        String winUrl = "POPUPBIOMETRICA/PopUpVerifFrame.jsp?idOrderPopup="+order+"&action=new"+"&av_url="+url;
                        out.println("window.open(\""+winUrl+"\", \"Verificacion\",\"toolbar = no, location = no, directories = no, status = no, menubar = no, scrollbars = yes, resizable = yes, screenX = 60, top = 40, left = 60, screenY = 40, width = 620,height=400\"); ");
                    }
                }else{
                    System.out.println("Para cliente sin apoderado mostrar· la VI " +datos);
                    String url = "VerificationExonerationEdit.jsp?datos="+datos;
                    String winUrl = "POPUPBIOMETRICA/PopUpVerifFrame.jsp?idOrderPopup="+order+"&action=new"+"&av_url="+url;
                    out.println("window.open(\""+winUrl+"\", \"Verificacion\",\"toolbar = no, location = no, directories = no, status = no, menubar = no, scrollbars = yes, resizable = yes, screenX = 60, top = 40, left = 60, screenY = 40, width = 620,height=400\"); ");
                }
            }else{// Es Persona
                if(StringUtils.isBlank(strMessage)) { // Existe Apoderado para la Orden
                    if(assigneeDocType.equals(constante.TIPO_DOC_DNI)) { //Apoderado DNI
                        System.out.println("Para apoderado con documento de identidad DNI en Persona se mostrar· la VI " +datos);
                        String url="VerificacionEdit.jsp?datos="+datos;
                        String winUrl = "POPUPBIOMETRICA/PopUpVerifFrame.jsp?idOrderPopup="+order+"&action=edit"+"&authorizedDni="+authorizedDni+"&login="+login+"&av_url="+url;
                        out.println("window.open(\""+winUrl+"\", \"Verificacion\",\"toolbar = no, location = no, directories = no, status = no, menubar = no, scrollbars = yes, resizable = yes, screenX = 60, top = 40, left = 60, screenY = 40, width = 620,height=400\"); ");
                    }else{
                        System.out.println("Para apoderado con documento de identidad distinto de DNI no se mostrar· la VI ");
                        //JVERGARA
                        HashMap objhash;
                        //Obtener el Tipo de transaccion de la Orden
                        int trxType = -1;
                        HashMap objhashDoc;
                        objhashDoc = objDigitalDocumentService.getDocumentGeneration(String.valueOf(String.valueOf(order)));
                        String message = (String)objhashDoc.get(Constante.MESSAGE_OUTPUT);
                        if(StringUtils.isBlank(message)){
                            trxType = ((DocumentGenerationBean)objhashDoc.get("documentGenerationBean")).getTrxType();
                        }
                        System.out.println("Tipo de TransacciÛn 1:Todos, 2:Venta, 3:Portabilidad, 4:PostVenta : "+trxType);
                        if(Constante.TRX_TYPE_PORTABILIDAD_ID==trxType) {
                            out.println("alert(\"" + Constante.MESSAGE_EXONERATE_PORTAB + "\")");
                            objhash = digitalDocumentService.updateGenerationSIGN(order, Constante.SIGNATURE_REASON_FOREIGN, login);
                            if (StringUtils.isBlank((String) objhash.get(Constante.MESSAGE_OUTPUT))) {
                                System.out.println("[OrderServlet] Actualizando Generacion de documento si existe OrderId: " + ((DocumentGenerationBean) objhash.get("documentGenerationBean")).getOrderId());
                            } else {
                                System.out.println("[OrderServlet] Actualizando Generacion de documento si existe " + objhash.get(Constante.MESSAGE_OUTPUT));
                            }
                        }
                        System.out.println("[OrderServlet] Por regla de validacion no se abre verificacion y se ingresa a Normalizacion OrderId: ");
                        validarCargarPopNormalizacionEdit(out, request.getParameter("hdorder"));
                    }
                }else{
                    System.out.println("Para cliente Persona con DNI se mostrar· la VI " +datos);
                    String url="VerificacionEdit.jsp?datos="+datos;
                    String winUrl = "POPUPBIOMETRICA/PopUpVerifFrame.jsp?idOrderPopup="+order+"&action=edit"+"&authorizedDni="+authorizedDni+"&login="+login+"&av_url="+url;
                    out.println("window.open(\""+winUrl+"\", \"Verificacion\",\"toolbar = no, location = no, directories = no, status = no, menubar = no, scrollbars = yes, resizable = yes, screenX = 60, top = 40, left = 60, screenY = 40, width = 620,height=400\"); ");
                }
            }

         /*
        * Fin [PRY-0787] Proyecto VIDD
        * Author: Jefferson Vergara
        * Objective: Para el caso que la Orden tenga Apoderado
        * */
        }catch (Exception e){
            System.out.print("StrMessage:"+e.getMessage());
        }
    }

    /*Method : verificacionNobiometrica
    Purpose: Redirecciona a la verificacion no biometrica , cuando se selecciona en el popup de verificacion
    Developer       Fecha       Comentario
    =============   ==========  ======================================================================
    Ricardo Quispe  28/08/2015  Creacion
    LROSALES        22/09/2016  Se agrega el parametro useCase, el cual se utiliza para conocer el origen
                                de la aplicacion que solicita la verificacion de identidad.
    Ivan Ortiz      27/09/2016  Se hardcodea la opcion No biometrica
    */
    private void verificacionNobiometrica(HttpServletRequest request, HttpServletResponse response,PrintWriter out){
        System.out.println("[BiometricaServlet][verificacionNobiometrica]:");
        int accion;
        if (request.getParameter("cmbAccion").equals("NB")) {
            accion = 7;
        } else {
            accion = miUtil.parseInt(request.getParameter("cmbAccion"));

        }
        int motivo=miUtil.parseInt(request.getParameter("cmbMotivo"));
        int idOrder=miUtil.parseInt(request.getParameter("idOrder"));
        String authorizer=request.getParameter("hdnAuthorizedUser");
        String authorizerPass=request.getParameter("hdnAuthorizedPass");
        String idsesion=request.getParameter("idsesion");
        String login = request.getParameter("hdnSessionLogin");
        String useCase = request.getParameter("hdSource");
        System.out.println("ESTE ES EL USECASE : " + useCase);

        String docType = request.getParameter("hdnTipodoc");
        String customerId = request.getParameter("hdnCustomerId");

        System.out.println("[BiometricaServlet][verificacionNobiometrica]: accion:" + accion + "motivo:" + motivo + "idOrder" + idOrder + "authorizer:" + authorizer + " authorizerPass:" + authorizerPass + " -idsesion: " + idsesion + "-useCase:" + useCase);
        System.out.println("******* [BiometricaServlet][verificacionNobiometrica]: accion:" + accion + "motivo:" + motivo + "idOrder" + idOrder + "authorizer:" + authorizer + " authorizerPass:" + authorizerPass + " -idsesion: " + idsesion + "-useCase:" + useCase);

        String document=request.getParameter("Rut");
        String strmotivo=request.getParameter("motivo");
        String origen=request.getParameter("origen");
        try{
            this.OpenNoBiometrico(out, origen, strmotivo, idOrder, authorizer, document, authorizerPass, idsesion, useCase, docType, customerId, login);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    /*Method : validdaNoBiometrica
    Purpose: Valida las respuestas que vienen de la verificacion no biometrica
    Developer       Fecha       Comentario
    =============   ==========  ======================================================================
    Ricardo Quispe  28/08/2015  Creacion
    */
    private void validdaNoBiometrica(HttpServletRequest request, HttpServletResponse response,PrintWriter out){
        System.out.println("[validdaNoBiometrica]:");
        int       qQuestion=miUtil.parseInt(request.getParameter("hdquestionario"));
        int       minAceptados=miUtil.parseInt(request.getParameter("hdminAceptado"));
        String    authorizer=request.getParameter("hdauthorizer");
        int       idOrder=miUtil.parseInt(request.getParameter("hdorder"));
        int       verifCustomer=miUtil.parseInt(request.getParameter("hdverification"));//----
        String    motivo=request.getParameter("hdmotivo");
        String    documento=request.getParameter("hddocumento");
        String    origen=request.getParameter("hdorigen");
        int       preguntas=miUtil.parseInt(request.getParameter("hdqpreguntas"));
        int       intentos=miUtil.parseInt(request.getParameter("hdintentos"));
        String    authorizerPass=request.getParameter("hdauthorizerPass");
        String    idSesion=request.getParameter("hdUserId");
        //JVERGARA
        DigitalDocumentService objDigitalizationService = new DigitalDocumentService();

        System.out.println("preguntas:"+preguntas+" origen:"+origen+" documento:"+documento+" motivo:"+motivo);
        System.out.println("question:"+qQuestion+" idOrder:"+idOrder+"verifCustomer"+verifCustomer);
        System.out.println("[minAceptados:]" + minAceptados);
        int qCorrectos=0;
        String action="";
        String strMessage="";
        HashMap hashMap=new HashMap();
        int idquestionBean=0 ;

        try {
            for(int i=1;i<=preguntas;i++){
                String succes=request.getParameter("hdSucces_"+i);
                String option=request.getParameter("rbOption_"+i);

                if (succes.equals(option)) {
                    qCorrectos++;
            }
            }

            if(qCorrectos>=minAceptados){
//PRY-0892 - HHUARACA
                if(intentos==1){
                    out.println("alert(\"" + "IdentificaciÛn Correcta" + "\")");
                action=constante.action_C;
                hashMap = biometricaService.getActionNoBiometrica(idOrder, verifCustomer, qQuestion, authorizer, action, constante.Source_CRM, "");
                //JVERGARA PRY 0787
                HashMap objhash;
                //Obtener el Tipo de transaccion de la Orden
                int trxType = -1;
                HashMap objhashDoc;
                objhashDoc = objDigitalDocumentService.getDocumentGeneration(String.valueOf(idOrder));
                String message = (String)objhashDoc.get(Constante.MESSAGE_OUTPUT);
                DocumentGenerationBean documentGenerationBean=new DocumentGenerationBean();
                if(StringUtils.isBlank(message)){
                    documentGenerationBean=(DocumentGenerationBean)objhashDoc.get("documentGenerationBean");
                    trxType = documentGenerationBean.getTrxType();

                }
                PortalSessionBean objPortalSesBean = null;
                objPortalSesBean = (PortalSessionBean) SessionService.getUserSession(idSesion);
                String login;
                if(objPortalSesBean==null){
                    login=documentGenerationBean.getCreatedBy();
                }else{
                    login=objPortalSesBean.getLogin();
                }
                System.out.println("Tipo de TransacciÛn 1:Todos, 2:Venta, 3:Portabilidad, 4:PostVenta : "+trxType);

                if(Constante.TRX_TYPE_PORTABILIDAD_ID==trxType) {
                    out.println("alert(\"" + Constante.MESSAGE_NO_BIOMETRIC_PORTAB + "\")");
                    objhash = objDigitalizationService.updateGenerationVI(idOrder, login);
                    if (StringUtils.isBlank((String) objhash.get(Constante.MESSAGE_OUTPUT))) {
                        System.out.println("[OrderServlet] Actualizando Generacion de documento si existe OrderId: " + ((DocumentGenerationBean) objhash.get("documentGenerationBean")).getOrderId());
                    } else {
                        System.out.println("[OrderServlet] Actualizando Generacion de documento si existe " + objhash.get(Constante.MESSAGE_OUTPUT));

                    }

                }
                if(origen.equals("new")){
                   validarCargarPopNormalizacion(out, request.getParameter("hdorder"));
                   //out.println("parent.opener.parent.mainFrame.redirectOrder("+idOrder+")");
                   //out.println("parent.close()");
               }else if(origen.equals("edit")){
                   //out.println("parent.opener.parent.mainFrame.editOrderBiometric('ok')");
                   //out.println("parent.close()");
                   validarCargarPopNormalizacionEdit(out, request.getParameter("hdorder"));
                }

            }else{

				action = constante.action_R;
                    hashMap = biometricaService.getActionNoBiometrica(idOrder, verifCustomer, qQuestion, authorizer, action, constante.Source_CRM, "");
                    System.out.println("Order:" + idOrder + " -verifCustomer:" + verifCustomer + " qQuestion:" + qQuestion +
                            " -authorizer:" + authorizer + " -action:" + action);

                    System.out.println("question:" + idquestionBean);
                    String url = "idorder=" + idOrder + "&authorizer=" + authorizer +
                            "&motivo=" + motivo + "&documento=" + documento + "&origen=" + origen + "&authorizerPass=" + authorizerPass + "&idsesion=" + idSesion +
                            "&verificationId=" + verifCustomer;

                    out.println("alert(\"" + "Respuesta Correcta. Siguiente Pregunta" + "\")");
                    out.println("parent.mainFrame.location.replace('POPUPBIOMETRICA/VerificacionNoBiometrica.jsp?" + url + "');");
//PRY-0892 - HHUARACA
					}


            }else{

//PRY-0892 - HHUARACA

                out.println("alert(\"" + "Respuesta Incorrecta. Se anulara la Orden" + "\")");
                    action=constante.action_A;
                    hashMap = biometricaService.getActionNoBiometrica(idOrder, verifCustomer, qQuestion, authorizer, action, constante.Source_CRM, "");
                    System.out.println("Order:" + idOrder+" -verifCustomer:"+verifCustomer+" qQuestion:"+qQuestion+
                            " -authorizer:"+authorizer+" -action:"+action);

                    if(origen.equals("new")){
                        this.anularOrdenNewBiometrica(idOrder,idSesion);
                        out.println("parent.opener.parent.mainFrame.redirectOrder("+idOrder+")");
                        out.println("parent.close()");
                    }else if(origen.equals("edit")){
                        out.println("parent.opener.parent.mainFrame.editOrderBiometric('ANULAR')");
                        out.println("parent.close()");
                    }

// PRY-0892 - HHUARACA


                }
        } catch (Exception e) {
            strMessage = e.getMessage();
        }


        if (!StringUtils.isBlank(strMessage)) {
            out.println("alert(\"" + strMessage + "\")");
        }

    }

    /**
     * Method : validdaNoBiometricaAislada
     * Purpose: Valida las respuestas que vienen de la verificacion no biometrica aislada
     * Developer       Fecha       Comentario
     * =============   ==========  ======================================================================
     * PORTEGA         04/10/2016  Creacion
     **/
    private void validdaNoBiometricaAislada(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        System.out.println("[validdaNoBiometricaAislada]:");
        int qQuestion = miUtil.parseInt(request.getParameter("hdquestionario"));
        int minAceptados = miUtil.parseInt(request.getParameter("hdminAceptado"));
        String authorizer = request.getParameter("hdauthorizer");
        int idOrder = miUtil.parseInt(request.getParameter("hdorder"));
        int verifCustomer = miUtil.parseInt(request.getParameter("hdverification"));//----
        String motivo = request.getParameter("hdmotivo");
        String documento = request.getParameter("hddocumento");
        String origen = request.getParameter("hdorigen");
        int preguntas = miUtil.parseInt(request.getParameter("hdqpreguntas"));
        int intentos = miUtil.parseInt(request.getParameter("hdintentos"));
        String authorizerPass = request.getParameter("hdauthorizerPass");
        String idSesion = request.getParameter("hdUserId");
        String login = request.getParameter("hdnSessionLogin");

        System.out.println("[validdaNoBiometricaAislada]preguntas:" + preguntas + " origen:" + origen + " documento:" + documento + " motivo:" + motivo);
        System.out.println("[validdaNoBiometricaAislada]question:" + qQuestion + ",idOrder:" + idOrder + ",verifCustomer" + verifCustomer
                + ",intentos :" + intentos+", login: "+login);
        System.out.println("[validdaNoBiometricaAislada]minAceptados:" + minAceptados);
        int qCorrectos = 0;
        String action = "";
        String strMessage = "";
        HashMap hashMap = new HashMap();
        int idquestionBean = 0;

        try {
            for (int i = 1; i <= preguntas; i++) {
                String succes = request.getParameter("hdSucces_" + i);
                String option = request.getParameter("rbOption_" + i);

                if (succes.equals(option)) {
                    qCorrectos++;
                }
            }
            System.out.println("[validdaNoBiometricaAislada]qCorrectos:" + qCorrectos);

            if (qCorrectos >= minAceptados) {

// PRY-0892 - HHUARACA
                if (intentos == 1) {

                System.out.println("[validdaNoBiometricaAislada]constante.action_C:");
                action = constante.action_C;
                System.out.println("[validdaNoBiometricaAislada]Order:" + idOrder + " -verifCustomer:" + verifCustomer + " qQuestion:" + qQuestion +
                        " -authorizer:" + authorizer + " -action:" + action);
                hashMap = biometricaService.getActionNoBiometrica(idOrder, verifCustomer, qQuestion, authorizer, action, constante.Source_CRMVIA, login);

                    out.println("alert(\"" + "IdentificaciÛn Correcta" + "\")");
                out.println("parent.opener.parent.mainFrame.fxCleanForm();");
                out.println("parent.close()");

            } else {
                    System.out.println("[validdaNoBiometricaAislada]constante.action_R:");
                    action = constante.action_R;
                    System.out.println("[validdaNoBiometricaAislada]Order:" + idOrder + " -verifCustomer:" + verifCustomer + " qQuestion:" + qQuestion +
                            " -authorizer:" + authorizer + " -action:" + action);
                    hashMap = biometricaService.getActionNoBiometrica(idOrder, verifCustomer, qQuestion, authorizer, action, constante.Source_CRMVIA, login);
                    out.println("alert(\"Respuesta Correcta. Siguiente Pregunta\")");
                    out.println("parent.opener.parent.mainFrame.document.frmdatos.cmbAccion.value = \"NB\";");
                    out.println("parent.opener.parent.mainFrame.verifPendiente();");
                    out.println("parent.close()");
                }
// PRY-0892 - HHUARACA

                } else {

// PRY-0892 - HHUARACA
                System.out.println("[validdaNoBiometricaAislada]constante.action_A:");
                action = constante.action_A;
                    System.out.println("[validdaNoBiometricaAislada]Order:" + idOrder + " -verifCustomer:" + verifCustomer + " qQuestion:" + qQuestion +
                            " -authorizer:" + authorizer + " -action:" + action);
                    hashMap = biometricaService.getActionNoBiometrica(idOrder, verifCustomer, qQuestion, authorizer, action, constante.Source_CRMVIA, login);

                out.println("alert(\"Respuesta Incorrecta. Se anular?la Orden\")");
                out.println("parent.opener.parent.mainFrame.fxCleanForm();");
                    out.println("parent.close()");
// PRY-0892 - HHUARACA

            }
        } catch (Exception e) {
            strMessage = e.getMessage();
        }


        if (!StringUtils.isBlank(strMessage)) {
            out.println("alert(\"" + strMessage + "\")");
        }

    }

    private void validarCargarPopNormalizacion(PrintWriter out, String orderId){
        NormalizarDireccionService objNormService = new NormalizarDireccionService();
        try {
            if(objNormService.normalizacionIsActive()){
                String datosNorm = objNormService.datosOrdenId(Long.parseLong(orderId));
                System.out.println("[BiometricaServlet][validarCargarPopNomalizacion] - orderId: "+orderId+"; datosNorm: " + datosNorm);
                if(datosNorm != null) {
                    if (objNormService.validarDatos(datosNorm)) {
                        String url = "PopUpNormalizar.jsp?datosNorm=" + datosNorm;
                        String winUrl = "POPUPNORMALIZACION/PopUpFrameNormalizar.jsp?av_url=" + url;
                        out.println("parent.opener.open(\"" + winUrl + "\", \"Normalizacion\",\"toolbar = no, location = no, directories = no, status = no, menubar = no, scrollbars = yes, resizable = yes, screenX = 60, top = 40, left = 60, screenY = 40, width = 710,height=410\"); ");
                    } else {
                        out.println("parent.opener.parent.mainFrame.redirectOrder(" + orderId + ")");
                    }
                }else{
                    out.println("parent.opener.parent.mainFrame.redirectOrder(" + orderId + ")");
                }
            }else{
                out.println("parent.opener.parent.mainFrame.redirectOrder(" + orderId + ")");
            }
        }catch (Exception e){
            System.out.println("[BiometricaServlet][validarCargarPopNomalizacion] - Error" + e.getMessage());
            out.println("parent.opener.parent.mainFrame.redirectOrder("+orderId+")");
        }finally{
            out.println("parent.close()");
        }
    }

    private void validarCargarPopNormalizacionEdit(PrintWriter out, String orderId){
        NormalizarDireccionService objNormService = new NormalizarDireccionService();
        try {
            if(objNormService.normalizacionIsActive()) {
                String datosNorm = objNormService.datosOrdenId(Long.parseLong(orderId));
                System.out.println("[BiometricaServlet][validarCargarPopNomalizacionEdit] - orderId: " + orderId + "; datosNorm: " + datosNorm);
                if (datosNorm != null) {
                    if (objNormService.validarDatos(datosNorm)) {
                        String url = "PopUpNormalizarEdit.jsp?datosNorm=" + datosNorm;
                        String winUrl = "POPUPNORMALIZACION/PopUpFrameNormalizar.jsp?av_url=" + url;
                        out.println("parent.opener.open(\"" + winUrl + "\", \"Normalizacion\",\"toolbar = no, location = no, directories = no, status = no, menubar = no, scrollbars = yes, resizable = yes, screenX = 60, top = 40, left = 60, screenY = 40, width = 710,height=410\"); ");
                    } else {
                        out.println("parent.opener.parent.mainFrame.editOrderBiometric('ok')");
                    }
                } else {
                    out.println("parent.opener.parent.mainFrame.editOrderBiometric('ok')");
                }
            }else{
                out.println("parent.opener.parent.mainFrame.editOrderBiometric('ok')");
            }
        }catch (Exception e){
            System.out.println("[BiometricaServlet][validarCargarPopNomalizacionEdit] - Error" + e.getMessage());
            out.println("parent.opener.parent.mainFrame.editOrderBiometric('ok')");
        }finally{
            out.println("parent.close()");
        }
    }

    /*Method : validdaNoBiometrica
   Purpose: Accion cancelar en la verificacion no biometrica
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Ricardo Quispe  28/08/2015  Creacion
   */
    public void cancelNoBiometrico(HttpServletRequest request, HttpServletResponse response,PrintWriter out){
        System.out.println("[cancelNoBiometrico]:");
        HashMap hashMap= new HashMap();
        Integer idOrder=miUtil.parseInt(request.getParameter("hdorder"));
        Integer verifCustomer=miUtil.parseInt(request.getParameter("hdverification"));
        Integer qQuestion=miUtil.parseInt(request.getParameter("hdquestionario"));
        String authorizer=request.getParameter("hdauthorizer");
        String action=constante.action_CN;
        String strMessage="";

        out.println("parent.opener.parent.mainFrame.redirectOrder("+idOrder+")");
        out.println("parent.close()");

    }


    /*Method : OpenNoBiometrico
    Purpose: Obtiene los datos del servicio no biometric y los muestra en la verificacion no biometrica
    Developer       Fecha       Comentario
    =============   ==========  ======================================================================
    Ricardo Quispe  28/08/2015  Creacion
    Ivan Ortiz      20/07/2016  Se modifico el origen crm via para no bieometrica
    LROSALES        22/09/2016  Se agrega un parametro useCase para identificar la aplicacion que requiere una verificacion
                                de identidad.
    */

    public void OpenNoBiometrico(PrintWriter out, String origen, String motivo, int idorder, String authorizer, String document, String password,
                                 String sesion, String useCase, String docType, String customerId, String login) {

        System.out.println("[OpenNoBiometrico]:");
        String order=String.valueOf(idorder);
        String strMessage="";
        PersonInfoBean  personInfoBean= new PersonInfoBean();
        String codError="";
        String straccion="";
        String strmotivo="";
        String strMessageError="";
        HashMap hashMap= new HashMap();
        String source = useCase; //IOZ20092016I
        int verifCustomer=0;
        int question=0;

        try{
        
            HashMap  responseRule= new HashMap();
            System.out.println("[OpenNoBiometrico][getVerificateNoBiometrica]:authorizer: " + authorizer + " password: " + password + " document: " + document +
                    " constante.Source_CRM: " + source + " order: " + order + " motivo: " + motivo + " docType: " + docType + " customerId: " + customerId
                    + " login: " + login+ "]");
            //invocacion ws getEvaluatedQuestion
            hashMap = biometricaService.getVerificateNoBiometrica(authorizer, password, document, source, order, motivo); //IOZ20092016F se reemplaza constante.Source_CRM por source
            strMessage=(String)hashMap.get("messageError");
            personInfoBean=(PersonInfoBean) hashMap.get("personInfoBean");
            codError=personInfoBean.getResult();
            System.out.println("[OpenNoBiometrico]:strMessage: " + strMessage + "]");
            System.out.println("[OpenNoBiometrico]:codError: " + codError + "]");
            customerId = customerId == null ? "0" : customerId;


           if (constante.Source_CRMVIA.equals(source)){
                System.out.println("[OpenNoBiometrico]:Guardando verificacion aislada]: " );
               System.out.println("PARAMETROS: useCase: " + useCase + " motivo: " + motivo + " authorizer: " + authorizer + " document: " + document + " docType: " + docType + " personInfoBean.getVerificationid().intValue(): " + personInfoBean.getVerificationid().intValue());
            strMessage = biometricaService.insViaCustomer(constante.Tipo_NOBiometrica, useCase, motivo, authorizer, constante.Source_CRMVIA,
                    1, document, null, docType, Integer.parseInt(customerId), personInfoBean.getVerificationid().intValue());
            }

            if (!codError.equals("-1")) {
                System.out.println("!codError.equals");
                if(personInfoBean.getVerificationid()!=null){
                    System.out.println("personInfoBean.getVerificationid()");
                    if (source.equals(constante.Source_CRMVIA)) {
                        System.out.println("RESPONSE RULE CRMVIA");
                        responseRule = biometricaService.getResponseRuleVIA(document, codError, "", constante.Tipo_NOBiometrica, constante.SOURCE_VIA,
                                0, 0, "",Integer.parseInt(customerId));
                    } else {
                    //consulta al motor de reglas la accion a realizar
                        System.out.println("response rule normal: idorder: " + idorder + " document: " + document + " codError: " + codError + " source: " + source );
                        responseRule = biometricaService.getResponseRule(idorder, document, codError, "", constante.Tipo_NOBiometrica, source, 0, 0, "");
                    }

                    straccion=(String)responseRule.get("straccion");
                    strmotivo=(String)responseRule.get("strmotivo");
                    strMessage=(String)responseRule.get("strMessage");
                    strMessageError=(String)responseRule.get("strMessageError");

                    System.out.println("[OpenNoBiometrico][getVerificateNoBiometrica]straccion:" + straccion + " ,strmotivo:" + strmotivo + " ,strMessage:" +
                            strMessage + " ,strMessageError:" + strMessageError+ " login: " + login);

                        if (source.equals(constante.Source_CRMVIA)) {
                        System.out.println("CRM VIA");
                            //validacion a nivel de api
                            if (!StringUtils.isBlank(strMessageError)) {
                                out.println("alert('" + strMessageError.trim() + "');");
                                return;
                            }

                            if (straccion.equals(constante.action_NB)) {
                                hashMap = biometricaService.getActionNoBiometrica(idorder, verifCustomer, 0, authorizer, straccion, source, login);
                                verifCustomer = personInfoBean.getVerificationid().intValue();
                                System.out.println("LEVANTANDO EL POPUPP:");
                                System.out.println("order: " + order + " authorizer: " + authorizer + " documento: " + document + " origen: " + origen + " motivo: " + motivo + " authorizerPass: " + password + " idsesion: " + sesion);
                                //out.println("alert('levanta');");

                                System.out.println("POPPUP AISLADA:");
                                String datos = order + "_" + authorizer + "_" + motivo + "_" + document + "_" + origen + "_" + password + "_" + sesion
                                        + "_" + verifCustomer + "_" + source+ "_"+login;
                                String av_url = "VerificacionNoBiometrica.jsp?datos=" + datos;
                                System.out.println("av_url: " + av_url);
                                String winUrl = "POPUPBIOMETRICA/PopUpVerifFrame.jsp?idOrderPopup=" + idorder + "&action=new" + "&av_url=" + av_url;
                                System.out.println("winUrl: " + winUrl);
                                out.println("window.open(\"" + winUrl + "\", \"Verificacion\",\"toolbar = no, location = no, directories = no, status = no, menubar = no, scrollbars = yes, resizable = yes, screenX = 60, top = 40, left = 60, screenY = 40, width = 620,height=400\"); ");

                            }else{
                                //validacion a nivel de reglas
                                out.println("alert('" + strMessage.trim() + "');");
                            }
                        } else {
                        System.out.println("CRM NORMAL");
                    if(!StringUtils.isBlank(strMessageError))
                    { out.println("alert('" + strMessageError.trim()+"');"); }
                    else{
                                System.out.println("casuisticas");
                                System.out.println("straccion: "+straccion);

                        if(straccion.equals(constante.action_NB) ){
                                System.out.println("VERIFICACION NO BIOMETRICA, LEVANTA EL POPUP");
                                    hashMap=biometricaService.getActionNoBiometrica(idorder,verifCustomer,0,authorizer,straccion,source,login);
                            verifCustomer= personInfoBean.getVerificationid().intValue();
                                    //System.out.println("LEVANTANDO EL POPUPP:");
                                    System.out.println("order: "+order+" authorizer: "+authorizer+" documento: "+document+" origen: "+origen+" motivo: "+motivo+" authorizerPass: "+password+" idsesion: "+sesion);
                                    //out.println("alert('levanta');");
                                    System.out.println("REPLACE NOMAS:");
                            String url="idorder="+order+"&authorizer="+authorizer+
                                    "&motivo="+motivo+"&documento="+document+"&origen="+origen+"&authorizerPass="+password+"&idsesion="+sesion+
                                    "&verificationId=" + verifCustomer;
                            out.println("parent.mainFrame.location.replace('POPUPBIOMETRICA/VerificacionNoBiometrica.jsp?"+url+"');");
                        }else if (straccion.equals(constante.action_R)){
                            System.out.println("codError:" + codError + "getVerificationid" + personInfoBean.getVerificationid());
                            verifCustomer= personInfoBean.getVerificationid().intValue();
                            System.out.println("Mensaje:Reintento" + strMessage);

                            if(!StringUtils.isBlank(strMessage))
                            { out.println("alert('" + strMessage.trim() +"');");}


                                    hashMap=biometricaService.getActionNoBiometrica(idorder,verifCustomer,0,authorizer,straccion,source,login);
                            System.out.println(hashMap);
                        }else if(straccion.equals(constante.action_A)){
                            System.out.print("codError:"+codError+"getVerificationid"+personInfoBean.getVerificationid());
                            verifCustomer= personInfoBean.getVerificationid().intValue();
                            if(!StringUtils.isBlank(strMessage))
                            { out.println("alert('" + strMessage.trim() + "' );");}

                                    hashMap=biometricaService.getActionNoBiometrica(idorder,verifCustomer,0,authorizer,straccion,source,login);
                            System.out.println(hashMap);

                            if(origen.equals("new")){
                                this.anularOrdenNewBiometrica(idorder,sesion);
                                out.println("parent.opener.parent.mainFrame.redirectOrder("+idorder+")");
                                out.println("parent.close()");
                            }else if(origen.equals("edit")){
                                out.println("parent.opener.parent.mainFrame.editOrderBiometric('ANULAR')");
                                out.println("parent.close()");
                            }

                        }else if(straccion.equals(constante.action_C) || straccion.equals(constante.action_D) ){

                            //action si el usuario no esta registrado en el mantenimiento de usuario
                            verifCustomer= personInfoBean.getVerificationid().intValue();

                            if(!StringUtils.isBlank(strMessage))
                            { out.println("alert('" + strMessage.trim() + "' );");}

                            hashMap=biometricaService.getActionNoBiometrica(idorder,verifCustomer,0,authorizer,straccion,source,login);
                            System.out.println(hashMap);
                            if(origen.equals("new")){
                                out.println("parent.opener.parent.mainFrame.redirectOrder("+idorder+")");
                                out.println("parent.close()");
                            }else if(origen.equals("edit")){
                                out.println("parent.opener.parent.mainFrame.editOrderBiometric('ok')");
                                out.println("parent.close()");
                            }
                        }
                    }

                        }
                }else{
                    out.println("alert(" + "Error con el servicio"+")");
                }

            }else{

                System.out.println("[noBiometrico]:authorizer"+strMessage);
                responseRule=biometricaService.getResponseRule( idorder,document,codError,"",
                        constante.Tipo_NOBiometrica, source, 0, 0, "");
                straccion=(String)responseRule.get("straccion");
                strmotivo=(String)responseRule.get("strmotivo");
                strMessage=(String)responseRule.get("strMessage");
                strMessageError=(String)responseRule.get("strMessageError");
                System.out.println("straccion:" + straccion + " ,strmotivo:" + strmotivo + " ,strMessage:" + strMessage + " ,strMessageError:" + strMessageError);


                if (!StringUtils.isBlank(strMessage)) {
                    out.println("alert('" + strMessage.trim() + "');");
                }

                hashMap = biometricaService.getActionNoBiometrica(idorder, 0, 0, authorizer, constante.action_D, source, login);
                System.out.println(hashMap);

                if(origen.equals("new")){
                    out.println("parent.opener.parent.mainFrame.redirectOrder("+idorder+")");
                    out.println("parent.close()");
                }else if(origen.equals("edit")){
                    out.println("parent.opener.parent.mainFrame.editOrderBiometric('ok')");
                    out.println("parent.close()");
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }




    public  void anularOrdenNewBiometrica(int order,String strSession){
        System.out.println("------anularOrdenNewBiometrica-------");

        HashMap hashData= new HashMap();
        HashMap hashDataOrder= new HashMap();
        OrderBean orbResume = new OrderBean();
        EditOrderService editOrderService= new EditOrderService();
        GeneralService generalService= new GeneralService();
        String strMessage="";

        PortalSessionBean objPortalSesBean = null;
        SpecificationBean objSpecificationBean = new SpecificationBean();
        Integer idEspecification=0;
        try {

            hashDataOrder=editOrderService.getOrder(order);
            orbResume=(OrderBean)hashDataOrder.get("objResume");

            System.out.println("--Specification: " + orbResume.getNpSpecificationId());
            System.out.println("--CustomerId: " + orbResume.getCsbCustomer().getSwCustomerId());
            System.out.println("--DivisionId: " + orbResume.getNpDivisionId());
            System.out.println("--NpStatus: " + orbResume.getNpStatus());
            System.out.println("--order: " + order);

            idEspecification=orbResume.getNpSpecificationId();

            hashData=generalService.getSpecificationDetail(idEspecification);
            System.out.println("getSpecificationDetail/->"+hashData);
            strMessage=(String)hashData.get("strMessage");


            if (strMessage!=null)
                throw new Exception(strMessage);

            objSpecificationBean=(SpecificationBean)hashData.get("objSpecifBean");
            System.out.println("Bpelflowgroup" + objSpecificationBean.getNpBpelflowgroup());

            objPortalSesBean = (PortalSessionBean) SessionService.getUserSession(strSession);

            System.out.println("Code" + objPortalSesBean.getCode());
            System.out.println("Login" + objPortalSesBean.getLogin());


            if( objPortalSesBean == null || objPortalSesBean.getUserid() == 0 )
                throw new Exception("La sesiÛn del usuario ha expirado por favor vuelva a registrarse. Inicie Login");


            hashData.put("objSpecificationBean",objSpecificationBean);
            hashData.put("strOrderId" ,String.valueOf(order) );
            hashData.put("strCustomerId",String.valueOf(orbResume.getCsbCustomer().getSwCustomerId()) );
            hashData.put("strDivisionId",String.valueOf(orbResume.getNpDivisionId())  );
            hashData.put("strNpBpelconversationid",orbResume.getNpBpelconversationid() );
            hashData.put("strActionName",constante.ACTION_INBOX_ANULAR);
            hashData.put("strNextInboxName","");
            hashData.put("strOldInboxName",  orbResume.getNpStatus());
            hashData.put("strCurrentInbox",orbResume.getNpStatus());


            WorkflowService objWorkflowService = new WorkflowService();

            System.out.println("HasOrder:" + hashData);
            hashData= objWorkflowService.doInvokeBPELProcess(hashData, objPortalSesBean);


            String strNextStatus=(String)hashData.get("strNextInbox");
            strMessage=(String)hashData.get("strMessage");

            System.out.println("Siguiente Inbox-->"+strNextStatus);
            System.out.println("Mensaje de Error del BPEL-->"+strMessage);

            if (strMessage!=null && strNextStatus!=null){

                throw new ExceptionBpel(strMessage);
            }else if (strMessage!=null)
                throw new ExceptionBpel(strMessage);


            //Actualiza el timeStap
            //----------------------
            hashData= objOrderService.updTimeStamp(order);
            strMessage=(String)hashData.get("strMessage");

            if (strMessage!=null)
                throw new Exception(strMessage);

        }catch (Exception e){
            e.getStackTrace();
            strMessage=e.getMessage();
            System.out.print("anularOrdenNewBiometrica:"+e.getMessage());
        }

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

    /*Method : registerExonerate
   Purpose: Registrar los datos de una exoneraci√≥n
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Luis Huapaya    05/08/2018  Creaci√≥n
   */
    private void registerExonerate(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        System.out.println("registerExonerate");
        int   idOrder=miUtil.parseInt(request.getParameter("idOrder"));
        String login= request.getParameter("hdnSessionLogin");
        String numDoc = request.getParameter("Rut");
        int valTypeDoc = miUtil.parseInt(request.getParameter("valTypeDoc"));
        String authorizedUser = request.getParameter("hdnAuthorizedUser");
        String strmessage="";
        HashMap map = null;
        DigitalDocumentService objDigitalizationService = new DigitalDocumentService();
        System.out.println("ESTOS SON LOS VALORES PARA LA EXONERACION: " +
        "LOGIN: " + login + " NUMERODO: " + numDoc + " ORDER: " + idOrder + " TIPODOC: " + valTypeDoc + " authorizedUser:  " + authorizedUser
        );

        map=biometricaService.registerExonerate(idOrder,authorizedUser,login,numDoc,valTypeDoc);
        strmessage = (String)((map.get("strMessageError")!=null)?map.get("strMessageError"):((map.get("strMessage")!=null)?map.get("strMessage"):null));
        if(strmessage != null) {
           out.println("alert(\"" + strmessage+"\")");

        }else{
            out.println("alert(\"" + "Se registro auditoria del tipo de documento."+"\")");

            //JVERGARA PRY 0787
            //Obtener el Tipo de transaccion de la Orden
            int trxType = -1;
            HashMap objhashDoc;
            objhashDoc = objDigitalDocumentService.getDocumentGeneration(String.valueOf(idOrder));
            String message = (String)objhashDoc.get(Constante.MESSAGE_OUTPUT);
            if(StringUtils.isBlank(message)){
                trxType = ((DocumentGenerationBean)objhashDoc.get("documentGenerationBean")).getTrxType();
            }
            System.out.println("Tipo de TransacciÛn 1:Todos, 2:Venta, 3:Portabilidad, 4:PostVenta : "+trxType);
            if(Constante.TRX_TYPE_PORTABILIDAD_ID==trxType) {
                out.println("alert(\"" + Constante.MESSAGE_EXONERATE_PORTAB + "\")");
                HashMap objhash;
                objhash = objDigitalizationService.updateGenerationVI(idOrder, login);
                if (StringUtils.isBlank((String) objhash.get(Constante.MESSAGE_OUTPUT))) {
                    System.out.println("[OrderServlet] Actualizando Generacion de documento si existe OrderId: " + ((DocumentGenerationBean) objhash.get("documentGenerationBean")).getOrderId());
                } else {
                    System.out.println("[OrderServlet] Actualizando Generacion de documento si existe " + objhash.get(Constante.MESSAGE_OUTPUT));

                }
            }

           out.println("parent.opener.parent.mainFrame.redirectOrder("+idOrder+")");
           out.println("parent.close()");
        }
    }

    /*Method : validateClientExoneration
   Purpose: Valida si un cliente puede realizar una exoneracion
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Luis Huapaya    05/08/2018  Creaci√≥n
   */
    public boolean validateClientExoneration(String idOrder){
        boolean isExoneration = false;
        int intNumberValidate = 0;

        BiometricaService biometricaService = new BiometricaService();
        intNumberValidate = biometricaService.validateClientExonerate (Integer.parseInt(idOrder));

        if(intNumberValidate != 0) isExoneration = true;

        return isExoneration;
    }








    /*Method : verificacionBiometricaAislada
    Purpose: Valida una veridicacion cuando la orden es editada.
    Developer       Fecha       Comentario
    =============   ==========  ======================================================================
    Ricardo Quispe  28/08/2015  Creacion
    LROSALES        22/09/2016  Se agrega un parametro useCase para obtener identificar la aplicacion que requiere
                                una verificacion de identidad
    LHUAPAYA        28/09/2016  Se agrega condicionales para poder manejar la verificacion de identidad aislada
    */
    private void verificacionBiometricaAislada(HttpServletRequest request, HttpServletResponse response,PrintWriter out) {
        System.out.println("*************** INICIO verificacionBiometricaAislada***************");
        VerificationCustomerBean verificationCustomerBean= new VerificationCustomerBean();
        BiometricBean  biometricBean= new BiometricBean();

        int   idOrder=miUtil.parseInt(request.getParameter("idOrder"));
        String document=request.getParameter("Rut");
        String ercDesc=request.getParameter("ErcDesc");
        String nombres=request.getParameter("Nombres");
        String apPaterno=request.getParameter("ApPaterno");
        String apMaterno=request.getParameter("ApMaterno");
        String vigencia=request.getParameter("Vigencia");
        String nroAudit=request.getParameter("NroAudit");
        String restriccion=request.getParameter("Restriccion");
        String login =request.getParameter("hdnSessionLogin");
        int    flagDni=miUtil.parseInt(request.getParameter("hdflagDni"));
        String codError=request.getParameter("codError");
        String athorizedUser=request.getParameter("hdnAuthorizedUser");
        String athorizedDni=request.getParameter("hdnAuthorizedDni");
        String athorizedPass=request.getParameter("hdnAuthorizedPass");
        String ercAcepta=request.getParameter("hdErcAcepta");
        String errorAcepta=request.getParameter("hdErrorAcepta");
        String docType = request.getParameter("hdnTipodoc");

        String source = request.getParameter("hdSource");
        System.out.println("SOURCE EN verificacionBiometricaAislada: " + source);
        int customerid = 0;
        if (request.getParameter("hdnCustomerId") != null)
            customerid = Integer.parseInt(request.getParameter("hdnCustomerId"));

        HashMap responseRule= new HashMap();

        System.out.println("document:"+document+"codError:"+codError+"restriccion:"+restriccion+" pass:"+athorizedPass+ "ErrorAcepta:"+errorAcepta);

        String strMessage="";
        String strmotivo="";
        String straccion="";
        String strMessageError="";
        String strSource = "";

        try {
            if (constante.SOURCE_VIA.equals(source)) {
                strSource = constante.SOURCE_VIA;
                responseRule = biometricaService.getResponseRuleVIA(document, codError, restriccion, constante.Tipo_Biometrica, constante.SOURCE_VIA, flagDni, 0, ercAcepta,customerid);
            }

            //consultar si es registro nuevo qcountRegistro  qcountRegistro
            straccion=(String)responseRule.get("straccion");
            strmotivo=(String)responseRule.get("strmotivo");
            strMessage=(String)responseRule.get("strMessage");
            strMessageError=(String)responseRule.get("strMessageError");
            System.out.println("[straccion:]"+straccion+" [strmotivo:]"+strmotivo+" [strMessageError:]" + strMessageError);

            verificationCustomerBean.setVerificationType(constante.Tipo_Biometrica);
            verificationCustomerBean.setSource(strSource);
            verificationCustomerBean.setMotive(strmotivo);
            verificationCustomerBean.setAuthorizedUser(athorizedUser);
            verificationCustomerBean.setDocumento(document);
            verificationCustomerBean.setTransaction(idOrder);
            verificationCustomerBean.setTypeTransaction(constante.type_transaction);
            verificationCustomerBean.setDocType(docType);

            biometricBean.setObservacion(restriccion);
            biometricBean.setAuditoria(nroAudit);
            biometricBean.setRenicCode(codError);
            biometricBean.setReniecResponse(ercDesc);
            biometricBean.setValityDni(document);
            biometricBean.setName(nombres);
            biometricBean.setFather(apPaterno);
            biometricBean.setMother(apMaterno);
            biometricBean.setStatus(straccion);

            String strMessageRule=strMessage;
            if (!StringUtils.isBlank(strMessageError)) {
                out.println("alert(\"" + strMessageError+"\");");
                //se muestra mensaje parametrizado en np_rule
            } else {

                //Si es Exitoso no mostrar mensaje
                if (!straccion.equals(constante.action_C)) {
                    out.println("alert(\"" + strMessage.trim() + "\");");
                }

                System.out.println("MessageRule:[" + strMessage + "]");
                if (constante.SOURCE_VIA.equals(source)) {
                    strMessage = biometricaService.getActionBiometricVIA(straccion, verificationCustomerBean, biometricBean, login, customerid);
                }

                System.out.println("MessageAction:[" + strMessage + "]");
                if (StringUtils.isBlank(strMessage)) {

                    String url = "";
                    String datos = "";
                    if (straccion.equals(constante.action_R)) {
                        // MMONTOYA
                        // Se habilita el boton en caso de reintento.
                        out.println("parent.mainFrame.document.getElementById('btnVerificacion').disabled=false;");
                    } else if (straccion.equals(constante.action_A)) {
                        if(constante.SOURCE_VIA.equals(source)){
                            out.println("parent.mainFrame.document.getElementById('btnRefreshForm').click();");
                        }else{
                            out.println("parent.opener.parent.mainFrame.editOrderBiometric('ANULAR')");
                            out.println("parent.close()");
                        }
                    } else if (straccion.equals(constante.action_C)) {

                        System.out.println("Mensaje:" + strMessageRule);
                        datos = "idOrder=" + idOrder + "&strDocument=" + document + "&strName=" + nombres +
                                "&strApePat=" + apPaterno + "&strApemat=" + apMaterno + "&strVigencia=" + vigencia +
                                "&strRestriccion=" + restriccion + "&message=" + strMessageRule;
                        url = "POPUPBIOMETRICA/VerificacionBiometricaEdit.jsp?" + datos;

                        if (constante.SOURCE_VIA.equals(source)) {
                            out.println("parent.mainFrame.document.getElementById('btnRefreshForm').click();");
                            out.println("window.open(\"" + url + "&strSource=" + constante.SOURCE_VIA + "\", \"Verificacion\",\"toolbar = no, location = no, directories = no, status = no, menubar = no, scrollbars = yes, resizable = yes, screenX = 60, top = 40, left = 60, screenY = 40, width = 620,height=400\"); ");
                        } else out.println("parent.mainFrame.location.replace('" + url + "');");

                    } else if (straccion.equals(constante.action_NB) || straccion.equals(constante.action_D)) {
                        this.OpenNoBiometrico(out, "edit", strmotivo, idOrder, athorizedUser, document, athorizedPass, "", source, docType, String.valueOf(customerid), login);

                    }
                }else{
                    out.println("alert(\"" + strMessage.trim() + "\");");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception:verificacionBiometricaAislada"+e.getMessage());
        }

        System.out.println("*************** FIN verificacionBiometricaAislada***************");
    }





}
