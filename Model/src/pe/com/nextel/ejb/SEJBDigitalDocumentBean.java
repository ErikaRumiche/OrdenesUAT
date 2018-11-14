package pe.com.nextel.ejb;

import org.apache.log4j.Logger;
import pe.com.entel.esb.contract.contractmanage.v1.ContractManagePort;
import pe.com.entel.esb.contract.contractmanage.v1.MED_ContractManage_v1;
import pe.com.entel.esb.data.generico.entelgenericheader.v2.HeaderRequestType;
import pe.com.entel.esb.data.generico.entelgenericheader.v2.HeaderResponseType;
import pe.com.entel.esb.document.documentmanage.v1.DocumentManagePort;
import pe.com.entel.esb.document.documentmanage.v1.EntelFault;
import pe.com.entel.esb.document.documentmanage.v1.MED_DocumentManage_v1;
import pe.com.entel.esb.message.contractmanage.getnumberactivesuspendedlines.v1.GetNumberActiveSuspendedLinesRequestType;
import pe.com.entel.esb.message.contractmanage.getnumberactivesuspendedlines.v1.GetNumberActiveSuspendedLinesResponseType;
import pe.com.entel.esb.message.documentmanage.createdocumentbyrule.v1.CreateDocumentByRuleRequestType;
import pe.com.entel.esb.message.documentmanage.createdocumentbyrule.v1.CreateDocumentByRuleResponseType;
import pe.com.entel.esb.message.documentmanage.getdocument.v1.GetDocumentRequestType;
import pe.com.entel.esb.message.documentmanage.getdocument.v1.GetDocumentResponseType;
import pe.com.entel.esb.message.documentmanage.getdocumentlist.v1.GetDocumentListRequestType;
import pe.com.entel.esb.message.documentmanage.getdocumentlist.v1.GetDocumentListResponseType;
import pe.com.entel.esb.message.documentmanage.getrules.v1.GetRulesRequestType;
import pe.com.entel.esb.message.documentmanage.getrules.v1.GetRulesResponseType;
import pe.com.entel.esb.message.documentmanage.sendemail.v1.SendEmailRequestType;
import pe.com.entel.esb.message.documentmanage.sendemail.v1.SendEmailResponseType;
import pe.com.entel.integracion.document.schema.CreateDocumentRequestType;
import pe.com.entel.integracion.document.schema.CreateDocumentResponseType;
import pe.com.entel.integracion.document.ws.Document;
import pe.com.entel.integracion.document.ws.DocumentPT;
import pe.com.nextel.bean.DocAssigneeBean;
import pe.com.nextel.bean.DocumentGenerationBean;
import pe.com.nextel.bean.LogWSBean;
import pe.com.nextel.dao.CustomerDAO;
import pe.com.nextel.dao.DigitalDocumentDAO;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.xml.ws.Holder;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by JCASTILLO on 04/04/2017.
 * [PRY-0787]
 */
public class SEJBDigitalDocumentBean implements SessionBean {

    private SessionContext _context;
    private DigitalDocumentDAO digitalDocumentDAO = null;
    private CustomerDAO customerDAO = null;
    private static final Logger logger = Logger.getLogger(SEJBDigitalDocumentBean.class);

    public void ejbCreate() {
        digitalDocumentDAO = new DigitalDocumentDAO();
        customerDAO= new CustomerDAO();
    }

    public void setSessionContext(SessionContext ctx) throws EJBException, RemoteException {
        _context = ctx;
    }

    public void ejbRemove() throws EJBException, RemoteException {
        logger.info("[SEJBDigitalDocumentBean][ejbRemove()]");
    }

    public void ejbActivate() throws EJBException, RemoteException {
        logger.info("[SEJBDigitalDocumentBean][ejbActivate()]");
    }

    public void ejbPassivate() throws EJBException, RemoteException {
        logger.info("[SEJBGestionDocumentoDigitalesBean][ejbPassivate()]");
    }


    public HashMap validateVIDD(Long buildingId, int typeId, Long divisionId, Long specificationId,  int channelId,Long customerId) throws Exception {
        return digitalDocumentDAO.validateVIDD(buildingId, typeId, divisionId,specificationId, channelId,customerId);
    }

    public HashMap validateVIDD(Long buildingId, int typeId, Long divisionId, Long specificationId,  int channelId,Long customerId,String generatorType) throws Exception {
        return digitalDocumentDAO.validateVIDD(buildingId, typeId, divisionId,specificationId, channelId,customerId,generatorType);
    }

    ///[PRY-0787] JCASTILLO Obtiene el ultimo correo electronico registrado o modficado del cliente
    public HashMap getEmail(Long customerId,Long siteId) throws Exception {
        return digitalDocumentDAO.getEmail(customerId, siteId);
    }

    public HashMap saveDocumentGeneration(DocumentGenerationBean documentGenerationBean) throws Exception {
        return digitalDocumentDAO.saveDocumentGeneration(documentGenerationBean);
    }

    public HashMap updateDocumentGeneration(DocumentGenerationBean documentGenerationBean) throws Exception {
        return digitalDocumentDAO.updateDocumentGeneration(documentGenerationBean);
    }

    public HashMap getDocumentGeneration(Long orderId) throws Exception{
        return digitalDocumentDAO.getDocumentGeneration(orderId);
    }

    public HashMap getDigitalDocumentsToGenerate(Integer source,Long trxId,GetRulesRequestType request, HeaderRequestType header) throws RemoteException, Exception{
        HashMap hshResultMap = new HashMap<String, Object>();
        String traza = "[SEJBGestionDocumentoDigitalesBean][getDigitalDocumentsToGenerate][trxId="+trxId+"][source="+source+"]";
        logger.info(traza + "INICIO");
        GetRulesResponseType response=null;
        String input=MiUtil.transfromarAnyObjectToXmlText(header)+Constante.newLine+MiUtil.transfromarAnyObjectToXmlText(request);
        String output,responseMessage = null,responseCode=null;
        int status = 1;
        java.sql.Date startDate,endDate;
        logger.info(traza + "XML [REQUEST]: " + input);
        startDate=new java.sql.Date(System.currentTimeMillis());
        Holder<HeaderResponseType> holder = new Holder<HeaderResponseType>();;
        try{
            MED_DocumentManage_v1  service= new MED_DocumentManage_v1();
            DocumentManagePort port= service.getMED_DocumentManage_v1_SOAP11();
            response = port.getRules(request, header, holder);
            hshResultMap.put("result", response);
        }catch(Exception e){
            status=0;
            responseMessage = e.getMessage();
            responseCode="-1000";
            logger.info(traza + "EntelFault. OSB DocumentManage: ["+responseMessage+"]");
            hshResultMap.put(Constante.MESSAGE_OUTPUT, responseMessage);
        }
        endDate=new java.sql.Date(System.currentTimeMillis());
        output=MiUtil.transfromarAnyObjectToXmlText(holder.value)+Constante.newLine+MiUtil.transfromarAnyObjectToXmlText(response);
        if(response!=null&&response.getResponseStatus()!=null){
            responseCode=response.getResponseStatus().getCodigoRespuesta();
            status=(Constante.CODIGO_RESP_OSB_OK.equals(responseCode))?1:0;
            responseMessage=response.getResponseStatus().getDescripcionRespuesta();
        }
        Double timeOut=(endDate.getTime()-startDate.getTime())/1000.0;
        LogWSBean logWSBean=new LogWSBean(source,trxId,Constante.WS_DOCUMENT_MANAGE,Constante.OPERATION_GET_RULES,input,output,startDate,endDate,timeOut,status,responseCode,responseMessage,header.getUsuario());
        digitalDocumentDAO.saveLog(logWSBean);
        logger.info(traza + "XML [RESPONSE]: "+ output);
        logger.info(traza + "Tiempo de procesamiento: "+logWSBean.getTimeOut());
        logger.info(traza + "FIN");

        return hshResultMap;
    }
    public HashMap generateDigitalDocuments(CreateDocumentByRuleRequestType request, HeaderRequestType header) throws RemoteException, Exception{
        HashMap hshResultMap = new HashMap<String, Object>();
        String traza = "[SEJBDigitalDocumentBean][generateDigitalDocuments][idOrden="+request.getIdOrden()+"] ";
        logger.info(traza + "INICIO");
        CreateDocumentByRuleResponseType response=null;
        String input=MiUtil.transfromarAnyObjectToXmlText(header)+Constante.newLine+MiUtil.transfromarAnyObjectToXmlText(request);
        String output,responseMessage = null,responseCode=null;
        int status = 1;
        java.sql.Date startDate,endDate;
        logger.info(traza + "XML [REQUEST]: " + input);
        startDate=new java.sql.Date(System.currentTimeMillis());
        Holder<HeaderResponseType> holder = new Holder<HeaderResponseType>();
        try{
            MED_DocumentManage_v1 service= new MED_DocumentManage_v1();
            DocumentManagePort port= service.getMED_DocumentManage_v1_SOAP11();
            response = port.createDocumentByRule(request, header, holder);
        }catch(Exception e){
            status=0;
            responseMessage = e.getMessage();
            responseCode=Constante.RESPONSE_CODE_EXCEPTION;
            logger.info(traza + "EntelFault. OSB DocumentManage: ["+responseMessage+"]");
            hshResultMap.put(Constante.MESSAGE_OUTPUT, responseMessage);
        }
        endDate=new java.sql.Date(System.currentTimeMillis());
        output=MiUtil.transfromarAnyObjectToXmlText(holder.value)+Constante.newLine+MiUtil.transfromarAnyObjectToXmlText(response);
        if(response!=null&&response.getResponseStatus()!=null){
            responseCode=response.getResponseStatus().getCodigoRespuesta();
            status=(Constante.CODIGO_RESP_OSB_OK.equals(responseCode))?1:0;
            responseMessage=response.getResponseStatus().getDescripcionRespuesta();
        }
        Double timeOut=(endDate.getTime()-startDate.getTime())/1000.0;
        LogWSBean logWSBean=new LogWSBean(Constante.SOURCE_ORDERS_ID,
                request.getIdOrden(),
                Constante.WS_DOCUMENT_MANAGE,
                Constante.OPERATION_GENERATE_DOCUMENTS,
                input,
                output,
                startDate,
                endDate,
                timeOut,
                status,
                responseCode,
                responseMessage,
                header.getUsuario());
        HashMap log=digitalDocumentDAO.saveLog(logWSBean);
        logger.info(log + "log: "+log);
        hshResultMap.put("result", response);
        logger.info(traza + "XML [RESPONSE]: "+ output);
        logger.info(traza + "Tiempo de procesamiento: "+logWSBean.getTimeOut());
        logger.info(traza + "FIN");
        return hshResultMap;
    }


    public HashMap getListDigitalDocuments(GetDocumentListRequestType request, HeaderRequestType header) throws Exception{
        LogWSBean logWSBean;
        HashMap hshResultMap = new HashMap<String, Object>();
        String traza = "[SEJBDigitalDocumentBean][getListDigitalDocuments][idOrden=" + request.getIdOrden() + " idIncidente=" + request.getIdIncidente() + "] ";
        logger.info(traza + "INICIO");
        GetDocumentListResponseType response=null;
        String input=MiUtil.transfromarAnyObjectToXmlText(header)+Constante.newLine+MiUtil.transfromarAnyObjectToXmlText(request);
        String output,responseMessage = null,responseCode=null;
        int status = 1;
        Long idTrx = Long.parseLong(header.getIdTransaccionNegocio());
        java.sql.Date startDate,endDate;
        logger.info(traza + "XML [REQUEST]: " + input);
        startDate=new java.sql.Date(new Date().getTime());
        Holder<HeaderResponseType> holder = new Holder<HeaderResponseType>();;
        try{
            MED_DocumentManage_v1 service= new MED_DocumentManage_v1();
            DocumentManagePort port= service.getMED_DocumentManage_v1_SOAP11();
            response = port.getDocumentList(request, header, holder);
            hshResultMap.put("result", response);
        }catch(Exception e){
            status=0;
            responseMessage = e.getMessage();
            responseCode=Constante.RESPONSE_CODE_EXCEPTION;
            logger.info(traza + "EntelFault. OSB DocumentManage: ["+responseMessage+"]");
            hshResultMap.put(Constante.MESSAGE_OUTPUT, responseMessage);
        }
        endDate=new java.sql.Date(new Date().getTime());
        output=MiUtil.transfromarAnyObjectToXmlText(holder.value)+Constante.newLine+MiUtil.transfromarAnyObjectToXmlText(response);
        if(response!=null&&response.getResponseStatus()!=null){
            responseCode=response.getResponseStatus().getCodigoRespuesta();
            status=(Constante.CODIGO_RESP_OSB_OK.equals(responseCode))?1:0;
            responseMessage=response.getResponseStatus().getDescripcionRespuesta();
        }
        Double timeOut=(endDate.getTime()-startDate.getTime())/1000.0;

        if(header.getCanal() == Constante.CHANNEL_ORDERS) {
            logWSBean = new LogWSBean(Constante.SOURCE_ORDERS_ID, idTrx, Constante.WS_DOCUMENT_MANAGE, Constante.OPERATION_DOCUMENT_LIST, input, output, startDate, endDate, timeOut, status, responseCode, responseMessage, header.getUsuario());
        }else {
            logWSBean = new LogWSBean(Constante.SOURCE_INCIDENT_ID, idTrx, Constante.WS_DOCUMENT_MANAGE, Constante.OPERATION_DOCUMENT_LIST, input, output, startDate, endDate, timeOut, status, responseCode, responseMessage, header.getUsuario());
        }

        digitalDocumentDAO.saveLog(logWSBean);
        logger.info(traza + "XML [RESPONSE]: "+ output);
        logger.info(traza + "Tiempo de procesamiento: "+logWSBean.getTimeOut());
        logger.info(traza + "FIN");

        return hshResultMap;
    }


    public HashMap sendEmail(SendEmailRequestType request, HeaderRequestType header) throws Exception{
        HashMap hshResultMap = new HashMap<String, Object>();
        String traza = "[SEJBDigitalDocumentBean][enviarCorreo][idOrden="+request.getIdOrden()+"] ";
        logger.info(traza + "INICIO");
        SendEmailResponseType response=null;
        String input=MiUtil.transfromarAnyObjectToXmlText(header)+Constante.newLine+MiUtil.transfromarAnyObjectToXmlText(request);
        String output,responseMessage = null,responseCode=null;
        int status = 1;
        java.sql.Date startDate,endDate;
        logger.info(traza + "XML [REQUEST]: " + input);
        startDate=new java.sql.Date(new Date().getTime());
        Holder<HeaderResponseType> holder = new Holder<HeaderResponseType>();;
        try{
            MED_DocumentManage_v1 service= new MED_DocumentManage_v1();
            DocumentManagePort port= service.getMED_DocumentManage_v1_SOAP11();
            response = port.sendEmail(request, header, holder);
        }catch(EntelFault e){
            status=0;
            responseMessage = e.getMessage();
            responseCode=Constante.RESPONSE_CODE_EXCEPTION;
            logger.info(traza + "EntelFault. OSB DocumentManage: ["+responseMessage+"]");
            hshResultMap.put(Constante.MESSAGE_OUTPUT, responseMessage);
        }
        endDate=new java.sql.Date(new Date().getTime());
        output=MiUtil.transfromarAnyObjectToXmlText(holder.value)+Constante.newLine+MiUtil.transfromarAnyObjectToXmlText(response);
        if(response!=null&&response.getResponseStatus()!=null){
            responseCode=response.getResponseStatus().getCodigoRespuesta();
            status=(Constante.CODIGO_RESP_SEND_EMAIL_OK.equals(responseCode))?1:0;
            responseMessage=response.getResponseStatus().getDescripcionRespuesta();
        }
        Double timeOut=(endDate.getTime()-startDate.getTime())/1000.0;
        LogWSBean logWSBean=new LogWSBean(Constante.SOURCE_ORDERS_ID,request.getIdOrden(),Constante.WS_DOCUMENT_MANAGE,Constante.OPERATION_ENVIAR_CORREO,input,output,startDate,endDate,timeOut,status,responseCode,responseMessage,header.getUsuario());
        digitalDocumentDAO.saveLog(logWSBean);
        hshResultMap.put("result", response);
        logger.info(traza + "Tiempo de procesamiento: "+logWSBean.getTimeOut());
        logger.info(traza + "FIN");

        return hshResultMap;
    }


    public HashMap getValidaCategoriaSolucionDigitalization(int order) throws SQLException {
        return digitalDocumentDAO.getValidaCategoriaSolucion(order);
    }

    //JRIOS PRY-0787
    public HashMap getValueNpDigitalConfig(String domain,int origen,int typetrx, int channel, int section)throws Exception{
        return digitalDocumentDAO.getValueNpDigitalConfig(domain, origen, typetrx, channel, section);
    }

    //JRIOS PRY-0787
    public String getClientTypeFromCustomerId(int iCustomerid)throws Exception{
        return digitalDocumentDAO.getClientTypeFromCustomerId(iCustomerid);
    }
    //JCASTILLO obtiene el numero de solicitud
    public HashMap getSolNumber(int source,Long specificationId, int channel, int division)  throws Exception {
        return digitalDocumentDAO.getSolNumber(source, specificationId, channel, division);
    }

    public HashMap saveDocAssignee(DocAssigneeBean assigneeBean) throws Exception {
        return digitalDocumentDAO.saveDocAssignee(assigneeBean);
    }

    public HashMap updateDocAssignee(DocAssigneeBean assigneeBean) throws Exception {
        return digitalDocumentDAO.updateDocAssignee(assigneeBean);
    }

    public HashMap getDocAssignee(Long orderId) throws Exception{
        return digitalDocumentDAO.getDocAssignee(orderId);
    }

    public HashMap getFlagSpecialCase(Long iOrderid)  throws Exception{
        return digitalDocumentDAO.getFlagSpecialCase(iOrderid);
    }

    public HashMap sendAttachedDocuments(Integer source, Long trxId, CreateDocumentRequestType createDocumentRequestType, pe.com.entel.integracion.document.schema.HeaderRequestType header)throws RemoteException,Exception{
        HashMap hshResultMap = new HashMap();
        String traza = "[SEJBGestionDocumentoDigitalesBean][sendAttachedDocuments][trxId="+trxId+"][source="+source+"]";
        logger.info(traza + "INICIO");
        CreateDocumentResponseType createDocumentResponseType = null;
        String input=MiUtil.transfromarAnyObjectToXmlText(header)+Constante.newLine+MiUtil.transfromarAnyObjectToXmlText(createDocumentRequestType);
        String output,responseMessage = null,responseCode=null;
        int status = 1;
        java.sql.Date startDate,endDate;
        logger.info(traza + "XML [REQUEST]: " + input);
        startDate=new java.sql.Date(new Date().getTime());
        LogWSBean logWSBean;
        Holder<pe.com.entel.integracion.document.schema.HeaderResponseType> holder = new Holder<pe.com.entel.integracion.document.schema.HeaderResponseType>();;
        try{
            Document service = new Document();
            DocumentPT port = service.getDocumentPort();
            createDocumentResponseType = port.createDocument(createDocumentRequestType,header,holder);
        }catch (Exception e){
            responseMessage = e.getMessage();
            logger.info(traza +" EntelFault. OSB DocumentAttachment: ["+responseMessage+"]");
            hshResultMap.put(Constante.MESSAGE_OUTPUT,responseMessage);
        }
        endDate=new java.sql.Date(new Date().getTime());
        output=MiUtil.transfromarAnyObjectToXmlText(createDocumentResponseType);
        if(createDocumentResponseType!=null&&createDocumentResponseType.getResponseStatus()!=null){
            responseCode=createDocumentResponseType.getResponseStatus().getCodigoRespuesta();
            status=(Constante.CODIGO_RESP_OSB_OK.equals(responseCode))?1:0;
            responseMessage=createDocumentResponseType.getResponseStatus().getDescripcionRespuesta();
        }

        Double timeOut=(endDate.getTime()-startDate.getTime())/1000.0;

        if(Constante.SOURCE_ORDERS_ID==source) {
            logger.info(traza + "Se crea LogWS bean: " + Constante.SOURCE_ORDERS_ID +"_"+createDocumentRequestType.getIdOrden()+"_"+Constante.WS_DOCUMENT+"_"+Constante.OPERATION_CREATE_DOCUMENT+"_"+input+"_"+output+"_"+startDate+"_"+endDate+"_"+timeOut+"_"+status+"_"+responseCode+"_"+responseMessage+"_"+header.getUsuario());
            logWSBean=new LogWSBean(Constante.SOURCE_ORDERS_ID,createDocumentRequestType.getIdOrden(),Constante.WS_DOCUMENT,Constante.OPERATION_CREATE_DOCUMENT,input,output,startDate,endDate,timeOut,status,responseCode,responseMessage,header.getUsuario());

        }else{
            logger.info(traza + "Se crea LogWS bean: " + Constante.SOURCE_INCIDENT_ID +"_"+createDocumentRequestType.getIdIncidente()+"_"+Constante.WS_DOCUMENT+"_"+Constante.OPERATION_CREATE_DOCUMENT+"_"+input+"_"+output+"_"+startDate+"_"+endDate+"_"+timeOut+"_"+status+"_"+responseCode+"_"+responseMessage+"_"+header.getUsuario());
            logWSBean=new LogWSBean(Constante.SOURCE_INCIDENT_ID,createDocumentRequestType.getIdIncidente(),Constante.WS_DOCUMENT,Constante.OPERATION_CREATE_DOCUMENT,input,output,startDate,endDate,timeOut,status,responseCode,responseMessage,header.getUsuario());
        }
          digitalDocumentDAO.saveLog(logWSBean);

        hshResultMap.put(Constante.RESULT,createDocumentResponseType);
        logger.info(traza + "XML [RESPONSE]: "+ output);
        logger.info(traza + "Tiempo de procesamiento: "+logWSBean.getTimeOut());
        logger.info(traza + "FIN");

        return hshResultMap;
    }

    public HashMap verDocumentoDigital(GetDocumentRequestType request, HeaderRequestType header) throws RemoteException, Exception{
        HashMap hshResultMap = new HashMap<String, Object>();
        String traza = "[SEJBGestionDocumentoDigitalesBean][verDocumentoDigital][idDocumento="+request.getIdDocumento()+"] ";
        logger.info(traza + "INICIO");
        String input;
        int status = 1;
        String messageError = null;
        String output,responseMessage = null,responseCode=null;
        GetDocumentResponseType response = null;
        LogWSBean logWSBean;
        Long idTrx = Long.parseLong(header.getIdTransaccionNegocio());
        java.sql.Date startDate,endDate;
        input=MiUtil.transfromarAnyObjectToXmlText(header)+Constante.newLine+MiUtil.transfromarAnyObjectToXmlText(request);
        logger.info(traza + "XML [REQUEST]: " + input);
        startDate = new java.sql.Date(new Date().getTime());
        Holder<HeaderResponseType> holder = new Holder<HeaderResponseType>();
        try {

            MED_DocumentManage_v1 service = new MED_DocumentManage_v1();
            DocumentManagePort port = service.getMED_DocumentManage_v1_SOAP11();
            response=port.getDocument(request,header,holder);
            hshResultMap.put("result", response);

        }catch(Exception e) {
            status=0;
            responseMessage = e.getMessage();
            responseCode=Constante.RESPONSE_CODE_EXCEPTION;
            hshResultMap.put(Constante.MESSAGE_OUTPUT,responseMessage);
        }
        endDate=new java.sql.Date(new Date().getTime());
        output=MiUtil.transfromarAnyObjectToXmlText(holder.value)+Constante.newLine+MiUtil.transfromarAnyObjectToXmlText(response);
        if(response!=null&&response.getResponseStatus()!=null){
            responseCode=response.getResponseStatus().getCodigoRespuesta();
            status=(Constante.CODIGO_RESP_OSB_OK.equals(responseCode))?1:0;
            responseMessage=response.getResponseStatus().getDescripcionRespuesta();
        }
        Double timeOut=(endDate.getTime()-startDate.getTime())/1000.0;
        if(Constante.CHANNEL_ORDERS.equals(header.getCanal())) {
            logWSBean = new LogWSBean(Constante.SOURCE_ORDERS_ID, idTrx, Constante.WS_DOCUMENT_MANAGE, Constante.OPERATION_GET_DOCUMENT, input, output, startDate, endDate, timeOut, status, responseCode, responseMessage, header.getUsuario());
        }else{
            logWSBean = new LogWSBean(Constante.SOURCE_INCIDENT_ID, idTrx, Constante.WS_DOCUMENT_MANAGE, Constante.OPERATION_GET_DOCUMENT, input, output, startDate, endDate, timeOut, status, responseCode, responseMessage, header.getUsuario());
        }
        digitalDocumentDAO.saveLog(logWSBean);
        //logger.info(traza + "XML [RESPONSE]: "+ output);
        logger.info(traza + "Tiempo de procesamiento: "+logWSBean.getTimeOut());
        logger.info(traza + "FIN");


        return hshResultMap;
    }

    public HashMap getNumberLines(Long trxId, GetNumberActiveSuspendedLinesRequestType request, HeaderRequestType header) throws Exception {
        HashMap hshResultMap = new HashMap<String, Object>();
        String traza = "[SEJBDigitalDocumentBean][getNumberLines][idTrx="+header.getIdTransaccionNegocio()+"]";
        String input=MiUtil.transfromarAnyObjectToXmlText(header)+Constante.newLine+MiUtil.transfromarAnyObjectToXmlText(request);
        String output,responseMessage = null,responseCode=null;
        int status = 1;
        java.sql.Date startDate,endDate;
        logger.info(traza + "XML [REQUEST]: " + input);
        GetNumberActiveSuspendedLinesResponseType response=null;
        startDate=new java.sql.Date(new Date().getTime());
        Holder<HeaderResponseType> holder = new Holder<HeaderResponseType>();
        try {
            MED_ContractManage_v1 service = new MED_ContractManage_v1();
            ContractManagePort port = service.getMED_ContractManage_v1_SOAP11();
            response = port.getNumberActiveSuspendedLines(request, header, holder);
            hshResultMap.put("result", response);
        }catch (Exception e){
            status=0;
            responseMessage = e.getMessage();
            responseCode=Constante.RESPONSE_CODE_EXCEPTION;
            hshResultMap.put(Constante.MESSAGE_OUTPUT,responseMessage);
        }
        endDate=new java.sql.Date(new Date().getTime());
        output=MiUtil.transfromarAnyObjectToXmlText(holder.value)+Constante.newLine+MiUtil.transfromarAnyObjectToXmlText(response);
        if(response!=null&&response.getResponseStatus()!=null){
            responseCode=response.getResponseStatus().getCodigoRespuesta();
            status=(Constante.CODIGO_RESP_OSB_OK.equals(responseCode))?1:0;
            responseMessage=response.getResponseStatus().getDescripcionRespuesta();
        }
        Double timeOut=(endDate.getTime()-startDate.getTime())/1000.0;
        LogWSBean logWSBean=new LogWSBean(Constante.SOURCE_ORDERS_ID,trxId,Constante.WS_CONTRACT_MANAGE,Constante.OPERATION_GET_NUMBER_LINES,input,output,startDate,endDate,timeOut,status,responseCode,responseMessage,header.getUsuario());
        digitalDocumentDAO.saveLog(logWSBean);
        hshResultMap.put("result", response);
        logger.info(traza + "Tiempo de procesamiento: "+logWSBean.getTimeOut());
        logger.info(traza + "FIN");
        return hshResultMap;


    }

    //JVERGARA PRY-0787
    public HashMap getvalidateUploadedFiles(int iFileTypeId, int iFileSize,String strContentType)throws Exception{
        return digitalDocumentDAO.getvalidateUploadedFiles(iFileTypeId, iFileSize, strContentType);
    }

    //JVERGARA PRY-0787
    public HashMap getAttachSectionStatus(int iorigen, int itypetrx, int ibuidingid)throws Exception{
        return digitalDocumentDAO.getAttachSectionStatus(iorigen, itypetrx, ibuidingid);
    }

    //JVERGARA PRY-0787
    public HashMap validateVIATechnicalService(String orderId, int customerId, String strLogin, int iChkAssignee, String strDocNumAssignee, String iDocTypeAssignee) throws Exception {
        return digitalDocumentDAO.validateVIATechnicalService(orderId, customerId, strLogin, iChkAssignee, strDocNumAssignee, iDocTypeAssignee);
    }

    //JVERGARA PRY-0787
    public HashMap updateGenerationVI(int iorderid, String strLoginid) throws Exception {
        return digitalDocumentDAO.updateGenerationVI(iorderid,strLoginid);
    }

    //JVERGARA PRY-0787
    public HashMap updateGenerationSIGN(int iorderid,int ireason, String strLoginid) throws Exception {
        return digitalDocumentDAO.updateGenerationSIGN(iorderid,ireason,strLoginid);
    }

    public HashMap getCustomerData(long lCustomerid)throws Exception{
        return customerDAO.getCustomerData(lCustomerid);
    }


    public HashMap updateOrderCode(String sOrdercode, long iOrderid) throws Exception{
        return digitalDocumentDAO.updateOrderCode(sOrdercode,iOrderid);
    }

    //JCALDERON PRY-0787
    public HashMap getSignatureReason(int iorigen, int itypetrx)throws Exception{
        return digitalDocumentDAO.getSignatureReason(iorigen, itypetrx);
    }

    //JCALDERON PRY-0787
    public HashMap getDocumentGenerationInc(Long incidentId) throws Exception{
        return digitalDocumentDAO.getDocumentGenerationInc(incidentId);
    }

    //JCASTILLO PRY-0787
    public HashMap getInfoEmail(String user, int trxType,int pvtType,String transType,int channel,long orderId,long customerId,int divisionId,long specificationId,int lines)  throws Exception{
        return digitalDocumentDAO.getInfoEmail(user,trxType,pvtType,transType,channel,orderId,customerId,divisionId,specificationId,lines);
    }

    //JCASTILLO PRY-0787
    public HashMap getInfoEmailPvt(int trxType,int pvtType,int channel,long orderId,long customerId,int divisionId,long specificationId,int lines)  throws Exception{
        return digitalDocumentDAO.getInfoEmailPvt(trxType,pvtType,channel,orderId,customerId,divisionId,specificationId,lines);
    }
    //JCASTILLO PRY-0787
    public HashMap getPvtType(long specificationId)  throws Exception{
        return digitalDocumentDAO.getPvtType(specificationId);
    }
    //JVERGARA PRY 0787
    public HashMap get_ident_info_customer(long lCustomerid)throws Exception{
        return digitalDocumentDAO.get_ident_info_customer(lCustomerid);
    }

    //JCALDERON PRY-0787
    public HashMap getAttDocListFlag(int orderid, int incidentid)throws Exception{
        return digitalDocumentDAO.getAttDocListFlag(orderid, incidentid);
    }

    //JCASTILLO PRY-0787
    public HashMap validateImeiLoan(long orderId)throws Exception{
        return digitalDocumentDAO.validateImeiLoan(orderId);
    }

}
