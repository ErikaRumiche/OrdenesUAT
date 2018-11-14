package pe.com.nextel.servlet;

import com.google.gson.Gson;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import pe.com.nextel.bean.*;
import pe.com.nextel.service.DigitalDocumentService;
import pe.com.nextel.service.IsolatedVerificationService;
import pe.com.nextel.service.SessionService;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;
import pe.com.nextel.util.RequestHashMap;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by JCASTILLO on 03/04/2017.
 * [PRY-0787]
 */
public class DigitalDocumentServlet extends HttpServlet{
    private static final long serialVersionUID = 1L;
    private static final Logger logger= Logger.getLogger(DigitalDocumentServlet.class);
    private static final Gson gson=new Gson();
    private static final String CONTENT_TYPE_TEXT = "text/html; charset=UTF-8";
    private static final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";
    DigitalDocumentService digitalDocumentService = new DigitalDocumentService();

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE_TEXT);
        doGet(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE_JSON);
        PrintWriter out = response.getWriter();
        //System.out.println("[DigitalDocumentServlet][doGet], INICIO");
        try{
            String myaction=request.getParameter("myaction");
            //System.out.println("[DigitalDocumentServlet][doGet], action: " + myaction);
            if ("getEmail".equals(myaction)) {
                getEmail(request,response);
            }else if ("getCombo".equals(myaction))
                getCombo(request,response);
            else if ("validateVIDD".equals(myaction))
                validateVIDD(request,response);
            else if ("validateVIDD2".equals(myaction))
                validateVIDD2(request,response);
            else if("getDigitalDocumentsToGenerate".equals(myaction))
                getDigitalDocumentsToGenerate(request,response);
            else if("getDocumentGeneration".equals(myaction)){
                getDocumentGeneration(request,response);
            }else if ("getChannel".equals(myaction)) {
                getChannel(request, response);
            }else if ("getDocAssignee".equals(myaction)) {
                    getDocAssignee(request, response);
            }else if ("getSolNumber".equals(myaction)) {
                getSolNumber(request, response);
            }else if("setDocumentAttachment".equals(myaction)){
                setDocumentAttachment(request, response);
            }else if("validateSpecialCaseVIDD".equals(myaction)){
                validateSpecialCaseVIDD(request, response);
            }else if("validateVIATechnicalService".equals(myaction)){
                validateVIATechnicalService(request, response);
            }else if("associateVIA".equals(myaction)){
                associateVIA(request,response);
            }else if("generateDigitalDocumentsST".equals(myaction)){
                generateDigitalDocumentsST(request, response);
            }else if("getAttachSectionStatus".equals(myaction)){
                getAttachSectionStatus(request,response);
            }else if("getSignatureReason".equals(myaction)){
                getSignatureReason(request, response);
            }else if("getDocumentGenerationInc".equals(myaction)) {
                getDocumentGenerationInc(request, response);
            }else if("getIdentInfoCustomer".equals(myaction)){
                get_ident_info_customer(request,response);
            }else if("validateImeiLoan".equals(myaction)) {
                validateImeiLoan(request, response);
            }

        }catch(Exception e){
            response.sendError(response.SC_INTERNAL_SERVER_ERROR,e.getMessage());
        }
        finally {
            out.close();
        }
    }




    public void getEmail(HttpServletRequest request,
                         HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        String customerId = MiUtil.getString(request.getParameter("customerId"));
        String siteId = MiUtil.getString(request.getParameter("siteId"));
        HashMap hashMap = digitalDocumentService.getEmailByCustomer(customerId, siteId);
        out.write(gson.toJson(hashMap));
    }

    private void getDigitalDocumentsToGenerate(HttpServletRequest request, HttpServletResponse response)throws Exception{
        PrintWriter out = response.getWriter();

        String trxId = request.getParameter("trxId");
        String source = request.getParameter("source");
        String userLogin = request.getParameter("userLogin");
        String specificationId = request.getParameter("specificationId");
        String customerId= request.getParameter("customerId");
        String divisionId = request.getParameter("divisionId");
        String tipoEjec = request.getParameter("tipoEjec");
        String tipoTrans = request.getParameter("tipoTrans");
        HashMap hashMap= digitalDocumentService.getDigitalDocumentsToGenerate(source,trxId,specificationId,customerId,divisionId,tipoEjec,tipoTrans,userLogin);
        out.write(gson.toJson(hashMap));

    }

    public void validateVIDD(HttpServletRequest request,
                                       HttpServletResponse response) throws Exception {
        logger.info("[DigitalDocumentServlet][validateDigitalizacion], INICIO");
        PrintWriter out = response.getWriter();
        String type = MiUtil.getString(request.getParameter("type"));
        String divisionId = MiUtil.getString(request.getParameter("divisionId"));
        String specificationId = MiUtil.getString(request.getParameter("specificationId"));
        String buildingId = MiUtil.getString(request.getParameter("buildingId"));
        String channel = MiUtil.getString(request.getParameter("channel"));
        String customerId = MiUtil.getString(request.getParameter("customerId"));
        HashMap hashMap= digitalDocumentService.validateVIDD(buildingId,type,divisionId,specificationId,channel,customerId);
        out.write(gson.toJson(hashMap));
    }

    public void validateVIDD2(HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
        logger.info("[DigitalDocumentServlet][validateDigitalizacion2], INICIO");
        PrintWriter out = response.getWriter();
        String type = MiUtil.getString(request.getParameter("type"));
        String divisionId = MiUtil.getString(request.getParameter("divisionId"));
        String specificationId = MiUtil.getString(request.getParameter("specificationId"));
        String buildingId = MiUtil.getString(request.getParameter("buildingId"));
        String channel = MiUtil.getString(request.getParameter("channel"));
        String customerId = MiUtil.getString(request.getParameter("customerId"));
        String generatorType = MiUtil.getString(request.getParameter("generatorType"));
        HashMap hashMap= digitalDocumentService.validateVIDD(buildingId,type,divisionId,specificationId,channel,customerId,generatorType);
        out.write(gson.toJson(hashMap));
    }


    public void validateSpecialCaseVIDD(HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
        logger.info("[DigitalDocumentServlet][validateSpecialCaseVIDD], INICIO");
        PrintWriter out = response.getWriter();
        String type = MiUtil.getString(request.getParameter("type"));
        String divisionId = MiUtil.getString(request.getParameter("divisionId"));
        String specificationId = MiUtil.getString(request.getParameter("specificationId"));
        String buildingId = MiUtil.getString(request.getParameter("buildingId"));
        String channel = MiUtil.getString(request.getParameter("channel"));
        String orderId = MiUtil.getString(request.getParameter("orderId"));
        String customerId = MiUtil.getString(request.getParameter("customerId"));

        HashMap hashMap= digitalDocumentService.validateVIDD(buildingId,type,divisionId,specificationId,channel,customerId);
        //System.out.println("[DigitalDocumentServlet][validateSpecialCaseVIDD], STRMESSAGE: "+MiUtil.getString(hashMap.get(Constante.MESSAGE_OUTPUT)));
        logger.info("[DigitalDocumentServlet][validateSpecialCaseVIDD], STRMESSAGE: "+MiUtil.getString(hashMap.get(Constante.MESSAGE_OUTPUT)));

        if(StringUtils.isBlank(MiUtil.getString(hashMap.get(Constante.MESSAGE_OUTPUT)))){
            HashMap hashMapCase = digitalDocumentService.getFlagSpecialCase(orderId);
            logger.info("[DigitalDocumentServlet][validateSpecialCaseVIDD], STRMESSAGE: "+MiUtil.getString(hashMapCase.get(Constante.MESSAGE_OUTPUT)));
            //System.out.println("[DigitalDocumentServlet][validateSpecialCaseVIDD], STRMESSAGE CASE: "+MiUtil.getString(hashMapCase.get(Constante.MESSAGE_OUTPUT)));
            if(StringUtils.isBlank(MiUtil.getString(hashMapCase.get(Constante.MESSAGE_OUTPUT)))){
                logger.info("strFlagSpecialCase: "+hashMapCase.get("strFlagSpecialCase"));
                hashMap.put("strFlagSpecialCase", hashMapCase.get("strFlagSpecialCase"));
                hashMap.put("strMessageCase","");
            }else{
                hashMap.put("strMessageCase",hashMapCase.get(Constante.MESSAGE_OUTPUT));
            }
        }

        out.write(gson.toJson(hashMap));
    }

    public void getCombo(HttpServletRequest request,
                         HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        String domain;
        int channel, section, origen, typetrx;
        domain = request.getParameter("domain");
        origen = Integer.parseInt(request.getParameter("origen"));
        typetrx = Integer.parseInt(request.getParameter("typetrx"));
        channel = Integer.parseInt(request.getParameter("channel"));
        section = Integer.parseInt(request.getParameter("section"));
        HashMap hashMap = digitalDocumentService.getValueNpDigitalConfig(domain,origen,typetrx, channel,section);
        out.write(gson.toJson(hashMap));
    }


    public void getDocumentGeneration(HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {

        PrintWriter out = response.getWriter();
        String orderId = request.getParameter("orderId");
        HashMap hashMap = digitalDocumentService.getDocumentGeneration(orderId);
        out.write(gson.toJson(hashMap));

    }

    public void getChannel(HttpServletRequest request,
                         HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        int customerId = MiUtil.getInt(request.getParameter("customerId"));
        HashMap hashMap = digitalDocumentService.getClientTypeFromCustomerId(customerId);
        out.write(gson.toJson(hashMap));
    }

    public void getSolNumber(HttpServletRequest request,
                           HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        String source= request.getParameter("source");
        String specificationId=request.getParameter("specificationId");
        String channel = request.getParameter("channel");
        String division = request.getParameter("division");
        HashMap hashMap = digitalDocumentService.getSolNumber(source,specificationId,channel,division);
        out.write(gson.toJson(hashMap));
    }

    public void getDocAssignee(HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {

        PrintWriter out = response.getWriter();
        String orderId = request.getParameter("orderId");
        HashMap hashMap = new HashMap();
        hashMap = digitalDocumentService.getDocAssignee(orderId);

        out.write(gson.toJson(hashMap));

    }

    //JVERGARA
    public void setDocumentAttachment(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("[DigitalDocumentServlet][setDocumentAttachment], INICIO");
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        int sizetemp = 0;
        boolean correct_file=true;
        String message_alert="";
        String message_alert_temp="";
        HashMap hashMapDoc;

        try {
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            System.out.println("isMultipart "+isMultipart);
            DiskFileItemFactory factory = new DiskFileItemFactory();
            // ServletContext servletContext = this.getServletConfig().getServletContext();
            ServletFileUpload upload = new ServletFileUpload(factory);
            // Create a new file upload handler
            upload.setHeaderEncoding("ISO-8859-1"); //Debe ser la misma codificacion del form y de la p�gina que lo contiene
            logger.info("Encoding "+ upload.getHeaderEncoding());
            // Parse the request
            logger.info("[DigitalDocumentServlet][setDocumentAttachment], Antes de leer parseRequest");
            List<FileItem> items =  new ArrayList<FileItem>();
            logger.info("[DigitalDocumentServlet][setDocumentAttachment], Despues de leer parseRequest");
            List<FileItem> items_bulk = upload.parseRequest(request);
            logger.info("[DigitalDocumentServlet][setDocumentAttachment], Antes de recorrer lista");
            for(FileItem fileItem:items_bulk){
                if (!fileItem.getContentType().equals("")){
                    items.add(fileItem);
                }else {
                    logger.info(" NO FILES Nombre Campo: " + fileItem.getFieldName() + " Nombre: " + fileItem.getName() + " ContentType " +
                            fileItem.getContentType() + " inMemory: " + fileItem.isInMemory() + " Size: " + fileItem.getSize() + " BYTES: ");
                }
            }
            boolean isRepited=false;
            incio:for(int i=0;i<items.size();i++){
                    for(int j=0;j<items.size()-1;j++){
                        if(i!=j){
                            if(FilenameUtils.getName(items.get(i).getName()).equals(FilenameUtils.getName(items.get(j).getName()))){
                                isRepited=true;
                                break incio;
                            }
                        }
                    }
            }
            if(isRepited){
                logger.info("elementos repetidos");
                message_alert=Constante.MESSAGE_FILES_REPEATED;
            }else {

                for (FileItem fileItem : items) {
                    logger.info("[DigitalDocumentServlet][setDocumentAttachment], Antes de mostrar contenido de lista");
                    logger.info("[DigitalDocumentServlet][setDocumentAttachment], Nombre de archivo "+ fileItem.getName());
                    logger.info("[DigitalDocumentServlet][setDocumentAttachment], Nombre de archivo relativo  "+ FilenameUtils.getName(fileItem.getName()));
                        logger.info("FILES Nombre Campo: " + fileItem.getFieldName() + " Nombre1: " + fileItem.getName() + " ContentType " +
                            fileItem.getContentType() + " inMemory: " + fileItem.isInMemory() + " Size: " + fileItem.getSize() + " BYTES: " );
                    sizetemp = (int)(fileItem.getSize()/1024); //Conviertiendo a KB
                    message_alert_temp=(String)((digitalDocumentService.getvalidateUploadedFiles(fileItem.getFieldName(),sizetemp,fileItem.getContentType())).get(Constante.MESSAGE_OUTPUT));

                    if((!StringUtils.isBlank(message_alert_temp))){
                        message_alert=message_alert_temp;
                        correct_file = false;
                            break;
                    }


                }
            }

            if(StringUtils.isBlank(message_alert)){
            logger.info("[DigitalDocumentServlet][setDocumentAttachment], Luego de recorrer Lista de archivos");
            String myaction = MiUtil.getString(request.getParameter("myaction"));
            String trxId = MiUtil.getString(request.getParameter("trxId"));
            String source = MiUtil.getString(request.getParameter("source"));
            String userLogin = MiUtil.getString(request.getParameter("userLogin"));
            String customerDoctype = MiUtil.getString(request.getParameter("strCustomerDocType"));
            String customerNumDoc = MiUtil.getString(request.getParameter("strCustomerNumDoc"));

            logger.info("Obtener datos para el servicio Servlet : " +
                    "\"myaction\":\"" + myaction +"\",\n"+
                    " \"trxId\":\"" + trxId + "\",\n" +
                    " \"source\":\"" + source + "\",\n" +
                    " \"userLogin\":\"" + userLogin + "\",\n" +
                    " \"strCustomerDocType\":\"" + customerDoctype + "\",\n" +
                    " \"strCustomerNumDoc\":\"" + customerNumDoc + "\"");
                hashMapDoc = digitalDocumentService.sendAttachedDocuments(items, source, trxId,userLogin,customerDoctype,customerNumDoc);
            }else{
                hashMapDoc = new HashMap();
                hashMapDoc.put(Constante.MESSAGE_OUTPUT,message_alert);
            }

              out.write(gson.toJson(hashMapDoc));
        }catch (FileUploadException e) {
            e.printStackTrace();
        }
        logger.info("[DigitalDocumentServlet][setDocumentAttachment], Fin");
    }

    //JVERGARA PRY 0787
    private void validateVIATechnicalService(HttpServletRequest request, HttpServletResponse response)throws Exception {
        logger.info("[DigitalDocumentServlet][validateVIATechnicalService], INICIO");
        logger.info("[DigitalDocumentServlet][validateVIATechnicalService], Declarar variables");
        String orderId, strDocNumAssignee, strLogin;
        int customerId, iChkAssignee, hdnFlgVIA;
        long lcustomerId;
        String verification_type;
        HashMap hashMap = new HashMap();
        PrintWriter out = response.getWriter();
        String resp = "";
        int result = 0;
        String strMessage ="";
        String strAssigneeDocType ="";
        String customerType = "";
        String clientDocType = "";
        String clientDocNum = "";
        int reasonId=0;
        IsolatedVerificationService service = new IsolatedVerificationService();
        IsolatedVerificationBean verificationBean= new IsolatedVerificationBean();
        List<IsolatedVerificationBean> listIsolatedVerification = null;

        logger.info("[DigitalDocumentServlet][validateVIATechnicalService], Obtener datos de request "+ request.toString() +" /"+request.getParameterNames().toString());
        orderId = MiUtil.emptyValObjTrim(request.getParameter("strOrderId"));
        customerId = MiUtil.getInt(request.getParameter("lCustomerId"));
        lcustomerId = MiUtil.getLong(request.getParameter("lCustomerId"));
        strLogin=  MiUtil.emptyValObjTrim(request.getParameter("strLogin"));
        iChkAssignee = MiUtil.getInt(request.getParameter("hdnChkAssignee"));
        strDocNumAssignee= MiUtil.emptyValObjTrim(request.getParameter("strAssigneeDocNum"));
        strAssigneeDocType= MiUtil.emptyValObjTrim(request.getParameter("strAssigneeDocType"));
        hdnFlgVIA = MiUtil.getInt(request.getParameter("hdnFlgVIA"));

        logger.info("[DigitalDocumentServlet][validateVIATechnicalService], Valores de request" +
                " orderId " + orderId+ "\n"+
                " customerId "+customerId+"\n"+
                " strLogin "+strLogin+"\n"+
                " iChkAssignee "+iChkAssignee+"\n"+
                " strDocNumAssignee "+strDocNumAssignee+"\n"+
                " strAssigneeDocType "+strAssigneeDocType);

        customerType = (String)(digitalDocumentService.getClientTypeFromCustomerId(customerId).get("strClientType"));

        logger.info("[DigitalDocumentServlet][validateVIATechnicalService] "+customerType);

        clientDocNum = (String)(digitalDocumentService.get_ident_info_customer(lcustomerId).get("wv_docnumcustomer"));
        clientDocType = (String)(digitalDocumentService.get_ident_info_customer(lcustomerId).get("wv_doctypecustomer"));

        logger.info("[DigitalDocumentServlet][validateVIATechnicalService] clientDocNum: "+clientDocNum+" clientDocType:"+clientDocType);

        try{

            logger.info("[DigitalDocumentServlet][validateVIATechnicalService], Obtener via relacionadas al customer CustomerId: "+customerId);
            listIsolatedVerification = service.getViaCustomer(customerId, null, null, 0);
            if(listIsolatedVerification.size()>0){
                verificationBean=listIsolatedVerification.get(0); //Obtener solo el primer valor
            }

            logger.info("[DigitalDocumentServlet][validateVIATechnicalService], Lista de verificaciones asociadas: "+listIsolatedVerification.toString());
            verification_type = MiUtil.emptyValObjTrim(verificationBean.getNpverificationtype());
            logger.info("[DigitalDocumentServlet][validateVIATechnicalService], VIA registrada: "+verification_type);
            if(hdnFlgVIA==2){
                if(verification_type.equals(Constante.VERIF_TYPE_BIOMETRICA)){//Tiene Verif. Biometrica
                    if(iChkAssignee==Constante.ASSIGNEE_SECTION_ACTIVE){//Tiene Apoderado
                        if((verificationBean.getNpnrodocument().equals(strDocNumAssignee))&&(verificationBean.getNptipodocument().equals(strAssigneeDocType))){//Compara Datos de VIA con Apoderado
                            updateVia(verificationBean.getNpverificationid(), Constante.type_transaction, Integer.parseInt(orderId), strLogin);
                            resp="";
                            result=1;//Proceder con Firma Digital
                            reasonId=1;
                        }else{// VIA y Apoderado no Coinciden
                            resp=Constante.MESSAGE_ERROR_VIA_ASSIGNEE;
                            result=0; //No proceder VIA y Apoderado no coinciden
                            reasonId=0;
                        }
                    }else{
                        if((verificationBean.getNpnrodocument().equals(clientDocNum))&&(verificationBean.getNptipodocument().equals(clientDocType))){ // Compara Datos de VIA con Cliente
                            updateVia(verificationBean.getNpverificationid(), Constante.type_transaction, Integer.parseInt(orderId), strLogin);
                            resp="";
                            result=1;//Proceder con Firma Digital
                            reasonId=1;
                        }else{// VIA y Cliente no Coinciden
                            resp=Constante.MESSAGE_ERROR_VIA_CLIENT;
                            result=0; //No proceder VIA y Cliente no coinciden
                            reasonId=0;
                        }

                    }
                }else if(verification_type.equals(Constante.VERIF_TYPE_NO_BIOMETRIC)){
                    resp=Constante.MESSAGE_CONFIRM_VIA_BIOMETRIC;
                    result=2; //Proceder con Firma Manual luego de aceptacion del usuario
                    reasonId=50;

                }else if(verification_type.equals(Constante.VERIF_TYPE_EXONERATE)){
                    if(iChkAssignee==Constante.ASSIGNEE_SECTION_ACTIVE){//Tiene Apoderado
                        if(verificationBean.getNpnrodocument().equals(strDocNumAssignee)&&verificationBean.getNptipodocument().equals(strAssigneeDocType)){//Compara Datos de VIA con Apoderado
                            updateVia(verificationBean.getNpverificationid(), Constante.type_transaction, Integer.parseInt(orderId), strLogin);
                            resp=Constante.MESSAGE_ALERT_VIA_ASSIGNEE_EXO;
                            result=3;//Proceder Firma Mnaual sin consultar al usuario
                        }else{// VIA y Apoderado no Coinciden
                            resp=Constante.MESSAGE_ERROR_VIA_ASSIGNEE_EXO;
                            result=0; //No proceder VIA y Apoderado no coinciden
                            reasonId=0;
                        }
                    }else{
                        if((verificationBean.getNpnrodocument().equals(clientDocNum))&&(verificationBean.getNptipodocument().equals(clientDocType))){ // Compara Datos de VIA con Cliente
                            updateVia(verificationBean.getNpverificationid(), Constante.type_transaction, Integer.parseInt(orderId), strLogin);
                            resp=Constante.MESSAGE_ALERT_VIA_ASSIGNEE_CLIE;
                            result=3; //Proceder Firma Manual sin consultar al usuario
                            reasonId=40;
                        }else{
                            resp=Constante.MESSAGE_ERROR_VIA_CLIENT;
                            result=0; //No proceder VIA y Cliente no coinciden
                            reasonId=0;
                        }
                    }
                }else{
                   //VIA Habilitada
                   resp=Constante.MESSAGE_ERROR_NO_VIA;
                   result=0;//No proceder VIA no disponible
                   reasonId=0;
                }
            }else{
                resp=Constante.MESSAGE_ALERT_DISABLED_VIA;
                result=4; //Proceder Firma Manual sin consultar al usuario
                reasonId=70;
            }


            hashMap.put("strMessage",strMessage);
            hashMap.put("messagealert",resp);
            hashMap.put("resultado",result);
            hashMap.put("reasonId",reasonId);
            logger.info("[DigitalDocumentServlet][validateVIATechnicalService], responder resultado "+hashMap);
            out.write(gson.toJson(hashMap));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            out.close();
        }

    }
    //JVERGARA PRY 0787
    public void updateVia(Integer npverificationid, String nptypetransaction, Integer nptransaction, String npmodifiedby){
        IsolatedVerificationService service = new IsolatedVerificationService();
        service.updViaCustomer(npverificationid, "ORDEN", nptransaction, npmodifiedby);
    }
    //JVERGARA PRY 0787
    public void getAttachSectionStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        int origen, typetrx, ibuidingid;
        origen = Integer.parseInt(request.getParameter("origen"));
        typetrx = Integer.parseInt(request.getParameter("typetrx"));
        ibuidingid = Integer.parseInt(request.getParameter("ibuidingid"));
        HashMap hashMap = digitalDocumentService.getAttachSectionStatus(origen,typetrx,ibuidingid);
        out.write(gson.toJson(hashMap));
    }

    public void generateDigitalDocumentsST(HttpServletRequest request,
                               HttpServletResponse response) throws Exception {

        logger.info("[DigitalDocumentServlet][generateDigitalDocumentsST]");
        PortalSessionBean objPortalSesBean = null;
        String strUserId=(request.getParameter("strSessionId")==null?"0":request.getParameter("strSessionId"));
        objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strUserId);

        PrintWriter out = response.getWriter();

        RequestHashMap objHashMap = new RequestHashMap();
        objHashMap = getParameterNames(request);

        HashMap hashMap= new HashMap();
        HashMap hashMapCustomer = digitalDocumentService.getCustomerData(MiUtil.getLong(objHashMap.getParameter("strCustomerId")));
        CustomerBean objCustomerBean = (CustomerBean)hashMapCustomer.get("objCustomerBean");


        DocumentGenerationBean documentGenerationBean = digitalDocumentService.getDocumentGenerationBean(objHashMap, objPortalSesBean);
        DocAssigneeBean docAssigneeBean = digitalDocumentService.getDocAssigneeBean(request, objPortalSesBean);
        String ejecType="";
        String transType="";
        String mensaje="";

        String v_reason= "" +objHashMap.getParameter("hdnSignatureReason");
        int ireason = MiUtil.getInt(v_reason);

        if(ireason>1){
            documentGenerationBean.setSignatureType(Constante.SIGNATURE_TYPE_MANUAL);
            documentGenerationBean.setSignatureReason(MiUtil.getInt(v_reason));
        }

        if(documentGenerationBean.getSignatureType()==Constante.SIGNATURE_TYPE_DIGITAL){
            ejecType = Constante.EJEC_TYPE_ASYNC;
            mensaje=Constante.MESSAGE_DIGIT_OK;
        }else if(documentGenerationBean.getSignatureType()==Constante.SIGNATURE_TYPE_MANUAL){
            ejecType = Constante.EJEC_TYPE_SYNC;
            mensaje=Constante.MESSAGE_MANUAL_OK ;
        }

        String v_transType=request.getParameter("buttonId");
        transType = v_transType.equals("btnImprimirFormato")?Constante.TRX_TYPE_WITH_LENDING:v_transType.equals("btnImprimirFormato2")?Constante.TRX_TYPE_ALL_DOCUMENTS_BY_RULE:"";

        documentGenerationBean.setTrxWs(transType);

        logger.info("[DigitalDocumentServlet][generateDigitalDocumentsST] - PRE SAVED DOC_GENERATION - ORDERID: "+documentGenerationBean.getOrderId());

        digitalDocumentService.saveDocumentGenerationAndAssigneST(request, objPortalSesBean, docAssigneeBean, documentGenerationBean);

        HashMap hashMapDocDig = digitalDocumentService.getDocumentGeneration(documentGenerationBean.getOrderId()+"");
        documentGenerationBean = (DocumentGenerationBean)hashMapDocDig.get("documentGenerationBean");

        logger.info("[DigitalDocumentServlet][generateDigitalDocumentsST] - documentGenerationBean: "+documentGenerationBean.getOrderId());
        documentGenerationBean.setGenerationStatus(Constante.GENERATION_STATUS_IN_PROCESS);
        String login;
        if(objPortalSesBean!=null){
            login=objPortalSesBean.getLogin();
        }else{
            login=MiUtil.getString(request.getParameter("hdnLogin"));
        }
        documentGenerationBean.setModificationBy(login);
        digitalDocumentService.updateDocumentGeneration(documentGenerationBean);
        HashMap hashMapDigitalDocument= digitalDocumentService.generateDigitalDocuments(
                documentGenerationBean.getOrderId()+"",ejecType,transType,
                objCustomerBean.getNpTipoDoc(),objCustomerBean.getSwRuc(),login);

        logger.info("[DigitalDocumentServlet][generateDigitalDocumentsST] - statusGeneration: "+hashMapDigitalDocument.get("statusGeneration"));

        String statusGeneration=hashMapDigitalDocument.get("statusGeneration")+"";
        int istatusGen = MiUtil.getInt(statusGeneration);

        if(istatusGen!=1){
            documentGenerationBean.setSignatureType(2);
            documentGenerationBean.setSignatureReason(80);
            documentGenerationBean.setRequestNumber("P"+documentGenerationBean.getOrderId());
            documentGenerationBean.setGenerationStatus(istatusGen);
            mensaje = Constante.MESSAGE_DIGIT_ERROR;

        }else{
            documentGenerationBean.setGenerationStatus(istatusGen);
            HashMap hashMapSolNumber = digitalDocumentService.getSolNumber(MiUtil.getString(Constante.SOURCE_ORDERS_ID), request.getParameter("hdnSubCategoria"), request.getParameter("hdnChannelClient"),request.getParameter("hdnDivisionId"));
            String ordercode = MiUtil.getString(hashMapSolNumber.get("solNumber"));
            digitalDocumentService.updateOrderCode(ordercode, documentGenerationBean.getOrderId());
            documentGenerationBean.setRequestNumber(ordercode);

        }
        digitalDocumentService.updateDocumentGeneration(documentGenerationBean);
        logger.info("[DigitalDocumentServlet][generateDigitalDocumentsST] - UPDATED DOC_GENERATION");

        hashMap.put("documentGenerationBean", documentGenerationBean);
        hashMap.put("responseMessage", mensaje);

        out.write(gson.toJson(hashMap));

    }

    public RequestHashMap getParameterNames(HttpServletRequest request) {
        RequestHashMap objHashMap =  new RequestHashMap();
        Enumeration paramNames = request.getParameterNames();
        //Mientras hayan parametros
        while(paramNames.hasMoreElements()) {
            //Recogemos el nombre del parametro
            String paramName = (String)paramNames.nextElement();

            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                objHashMap.put(paramName,paramValue);

            } else {
                //Es un arreglo de String
                String[] paramValue = new String[paramValues.length];
                for(int i=0; i<paramValues.length; i++) {
                    paramValue[i] = paramValues[i];
                }
                objHashMap.put(paramName,paramValue);
            }
        }


        return objHashMap;
    }

    //JCALDERON PRY 0787
    public void getSignatureReason(HttpServletRequest request, HttpServletResponse response) throws Exception {

        logger.info("[DigitalDocumentServlet][getSignatureReason], INICIO");
        PrintWriter out = response.getWriter();
        int origen, typetrx;
        origen = Integer.parseInt(request.getParameter("origen"));
        typetrx = Integer.parseInt(request.getParameter("typetrx"));
        logger.info("[DigitalDocumentServlet][getSignatureReason], origen: " + origen + " typetrx: " + typetrx);
        HashMap hashMap = digitalDocumentService.getSignatureReason(origen, typetrx);
        out.write(gson.toJson(hashMap));
    }

    //JCALDERON PRY 0787
    public void getDocumentGenerationInc(HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {

        logger.info("[DigitalDocumentServlet][getDocumentGenerationInc], INICIO");
        PrintWriter out = response.getWriter();
        String incidentId = request.getParameter("incidentId");
        logger.info("[DigitalDocumentServlet][getDocumentGenerationInc], incidentId: " + incidentId);
        HashMap hashMap = digitalDocumentService.getDocumentGenerationInc(incidentId);
        out.write(gson.toJson(hashMap));
    }

    //JVERGARA PRY 0787
    public void associateVIA(HttpServletRequest request,
                             HttpServletResponse response)throws Exception{
        logger.info("[DigitalDocumentServlet][associateVIA], INICIO");
        logger.info("[DigitalDocumentServlet][associateVIA], Asociar VIA con la Orden");
        String orderId, strDocNumAssignee, strLogin;
        int customerId, iChkAssignee;
        long lcustomerId;
        String verification_type;
        HashMap hashMap = new HashMap();
        PrintWriter out = response.getWriter();
        String resp = "";
        int result = 0;
        String strMessage ="";
        String strAssigneeDocType ="";
        int reasonId=0;
        String clientDocNum = "";
        String clientDocType = "";
        IsolatedVerificationService service = new IsolatedVerificationService();
        IsolatedVerificationBean verificationBean= new IsolatedVerificationBean();
        List<IsolatedVerificationBean> listIsolatedVerification = null;

        logger.info("[DigitalDocumentServlet][associateVIA], Obtener datos de request "+ request.toString() +" /"+request.getParameterNames().toString());
        orderId = MiUtil.emptyValObjTrim(request.getParameter("strOrderId"));
        customerId = MiUtil.getInt(request.getParameter("lCustomerId"));
        lcustomerId = MiUtil.getLong(request.getParameter("lCustomerId"));
        strLogin=  MiUtil.emptyValObjTrim(request.getParameter("strLogin"));
        iChkAssignee = MiUtil.getInt(request.getParameter("hdnChkAssignee"));
        strDocNumAssignee= MiUtil.emptyValObjTrim(request.getParameter("strAssigneeDocNum"));
        strAssigneeDocType= MiUtil.emptyValObjTrim(request.getParameter("strAssigneeDocType"));

        logger.info("[DigitalDocumentServlet][associateVIA], Valores de request" +
                " orderId " + orderId+ "\n"+
                " customerId "+customerId+"\n"+
                " strLogin "+strLogin+"\n"+
                " iChkAssignee "+iChkAssignee+"\n"+
                " strDocNumAssignee "+strDocNumAssignee+"\n"+
                " strAssigneeDocType "+strAssigneeDocType);

        clientDocNum = (String)(digitalDocumentService.get_ident_info_customer(lcustomerId).get("wv_doctypecustomer"));
        clientDocType = (String)(digitalDocumentService.get_ident_info_customer(lcustomerId).get("wv_docnumcustomer"));
        try{

            logger.info("[DigitalDocumentServlet][associateVIA], Obtener via relacionadas al customer CustomerId: "+customerId);
            listIsolatedVerification = service.getViaCustomer(customerId, null, null, 0);
            if(listIsolatedVerification.size()>0){
                verificationBean=listIsolatedVerification.get(0); //Obtener solo el primer valor
            }

            logger.info("[DigitalDocumentServlet][associateVIA], Lista de verificaciones asociadas: "+listIsolatedVerification.toString());
            verification_type = MiUtil.emptyValObjTrim(verificationBean.getNpverificationtype());
            logger.info("[DigitalDocumentServlet][associateVIA], VIA registrada: "+verification_type);

            //Metodo llamado para asociar verificacion no biometrica
            if(iChkAssignee==Constante.ASSIGNEE_SECTION_ACTIVE){//Tiene Apoderado
                if((verificationBean.getNpnrodocument().equals(strDocNumAssignee))&&(verificationBean.getNptipodocument().equals(strAssigneeDocType))){//Compara Datos de VIA con Apoderado
                    updateVia(verificationBean.getNpverificationid(), Constante.type_transaction, Integer.parseInt(orderId), strLogin);
                    resp="";
                    result=2;//Proceder con Firma Manual
                    reasonId=50; //Verificacion No Biometrica
                }else{// VIA y Apoderado no Coinciden
                    resp=Constante.MESSAGE_ERROR_NO_BIO_VIA_ASSIGNEE;
                    result=0; //No proceder VIA y Apoderado no coinciden
                    reasonId=0;
                }
            }else{
                if((verificationBean.getNpnrodocument().equals(clientDocNum))&&(verificationBean.getNptipodocument().equals(clientDocType))) { // Compara Datos de VIA con Cliente
                    updateVia(verificationBean.getNpverificationid(), Constante.type_transaction, Integer.parseInt(orderId), strLogin);
                    resp="";
                    result=2;//Proceder con Firma Manual
                    reasonId=50; //Verificacion No Biometrica
                }else{
                    resp=Constante.MESSAGE_ERROR_VIA_CLIENT;
                    result=0; //No proceder VIA y Apoderado no coinciden
                    reasonId=0;
                }

            }

            hashMap.put("strMessage",strMessage);
            hashMap.put("messagealert",resp);
            hashMap.put("resultado",result);
            hashMap.put("reasonId",reasonId);
            logger.info("[DigitalDocumentServlet][associateVIA], responder resultado "+hashMap);
            out.write(gson.toJson(hashMap));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            out.close();
        }
    }
    //JVERGARA PRY 0787
    public void get_ident_info_customer(HttpServletRequest request, HttpServletResponse response) throws Exception {

        logger.info("[DigitalDocumentServlet][get_ident_info_customer], INICIO");
        PrintWriter out = response.getWriter();
        long lcustomerId;
        lcustomerId = MiUtil.getLong(request.getParameter("lcustomerId"));

        logger.info("[DigitalDocumentServlet][get_ident_info_customer], customerId: " + lcustomerId);
        HashMap hashMap = digitalDocumentService.get_ident_info_customer(lcustomerId);
        out.write(gson.toJson(hashMap));
    }

    //JCASTILLO PRY 0787
    public void validateImeiLoan(HttpServletRequest request, HttpServletResponse response) throws Exception {

        logger.info("[DigitalDocumentServlet][validateImeiLoan], INICIO");
        PrintWriter out = response.getWriter();
        String orderId = request.getParameter("orderId");
        HashMap hashMap = new HashMap();
        logger.info("[DigitalDocumentServlet][validateImeiLoan], orderId: " + orderId);
        HashMap hshValidate =digitalDocumentService.validateImeiLoan(orderId);
        if(MiUtil.getInt(hshValidate.get(Constante.STR_RESULT))==0){
            hashMap.put(Constante.MESSAGE_OUTPUT,Constante.MESSAGE_VALIDATE_IMEILOAN);
        }
        out.write(gson.toJson(hashMap));
    }

}
