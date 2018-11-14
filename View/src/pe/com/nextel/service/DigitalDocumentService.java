package pe.com.nextel.service;


import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import pe.com.entel.esb.data.document.documento.v1.ListaDocumentoType;
import pe.com.entel.esb.data.generico.entelgenericheader.v2.HeaderRequestType;
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
import pe.com.entel.integracion.document.schema.ListaDocumentoItemType;
import pe.com.entel.integracion.document.schema.ListaDocumentosType;
import pe.com.nextel.bean.*;
import pe.com.nextel.ejb.SEJBDigitalDocumentRemote;
import pe.com.nextel.ejb.SEJBDigitalDocumentRemoteHome;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;
import pe.com.nextel.util.RequestHashMap;

import javax.naming.Context;
import javax.rmi.PortableRemoteObject;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.*;


/**
 * Created by JCASTILLO on 04/04/2017.
 * [PRY-0787]
 */
public class DigitalDocumentService extends GenericService {

    HashMap <Integer, String> messages = new HashMap<Integer, String>();
    {
        messages.put(1, Constante.MESSAGE_DIGIT_OK);
        messages.put(2, Constante.MESSAGE_MANUAL_OK);
        messages.put(40, Constante.MESSAGE_REASON_40);
        messages.put(50, Constante.MESSAGE_REASON_50);
        messages.put(60, Constante.MESSAGE_REASON_60);
        messages.put(80,Constante.MESSAGE_DIGIT_ERROR);

    }
    private String getMessageGeneration(DocumentGenerationBean documentGenerationBean){
        String message;
        if(documentGenerationBean.getGenerationStatus()==1){
        if(documentGenerationBean.getTrxType()==Constante.TRX_TYPE_VENTA_ID){
            if(documentGenerationBean.getGenerationStatus()==1){
            if(documentGenerationBean.getSignatureType()==Constante.SIGNATURE_TYPE_DIGITAL){
                message=messages.get(1);
            }else{
                message=messages.get(documentGenerationBean.getSignatureReason());
                if(message==null)
                message=messages.get(2);
            }
            }else{
                message=messages.get(80);
            }
        }else {
            if (documentGenerationBean.getSignatureType() == Constante.SIGNATURE_TYPE_DIGITAL) {
                message = messages.get(1);
            } else {
                message = messages.get(2);
            }
        }
        }else{
            message=messages.get(80);
        }
        return message;

    }
    private static final Logger logger = Logger.getLogger(DigitalDocumentService.class);
    public static SEJBDigitalDocumentRemote getSEJBDigitalDocumentRemote() {
        SEJBDigitalDocumentRemote sejbDigitalDocumentRemote=null;
        try{
            final Context context = MiUtil.getInitialContext();
            final SEJBDigitalDocumentRemoteHome sejbDigitalDocumentRemoteHome =
                    (SEJBDigitalDocumentRemoteHome) PortableRemoteObject.narrow( context.lookup( "SEJBDigitalDocument" ), SEJBDigitalDocumentRemoteHome.class );
            sejbDigitalDocumentRemote = sejbDigitalDocumentRemoteHome.create();

            return sejbDigitalDocumentRemote;
        }catch(Exception ex) {
            logger.error("Exception",ex);

        }
        return sejbDigitalDocumentRemote;
    }

    public HashMap getEmailByCustomer(String customerId, String sitedId){
        logger.info("customerId:"+customerId+",sitedId:"+sitedId);
        HashMap hashMap= new HashMap();
        try {
            Long lCustomerId = new Long(customerId);
            Long lsiteId = MiUtil.parseLong(sitedId);
            hashMap= getSEJBDigitalDocumentRemote().getEmail(lCustomerId,lsiteId);
        }catch(Throwable t){
            manageCatch(hashMap, t);
        }
        logger.info(hashMap);
        return hashMap;
    }

    public HashMap validateVIDD(String buildingId, String typeId, String divisionId, String specificationId,  String channelId,String customerId){
        logger.info("typeId:"+typeId+",specificationId:"+specificationId+",buildingId:"+buildingId+",channelId:"+channelId+",customerId:"+customerId);
        HashMap hashMap= new HashMap();
        int validateVIDD=0;
        try {
            Long lBuildingId = MiUtil.parseLong(buildingId);
            Long lspecificationId= MiUtil.parseLong(specificationId);
            Long ldivisionId= MiUtil.parseLong(divisionId);
            Long lcustomerId= MiUtil.parseLong(customerId);
            Integer iType=MiUtil.parseInt(typeId);
            Integer iChannel=MiUtil.parseInt(channelId);
            hashMap=getSEJBDigitalDocumentRemote().validateVIDD(lBuildingId,iType,ldivisionId,lspecificationId,iChannel,lcustomerId);
        }catch(Throwable t){
            manageCatch(hashMap, t);
        }
        logger.info(hashMap);
        return hashMap;
    }
    public HashMap validateVIDD(String buildingId, String typeId, String divisionId, String specificationId,  String channelId,String customerId,String generatorType){
        logger.info("typeId:"+typeId+",specificationId:"+specificationId+",buildingId:"+buildingId+",channelId:"+channelId+",customerId:"+customerId+",generatorType:"+generatorType);
        HashMap hashMap= new HashMap();
        int validateVIDD=0;
        try {
            Long lBuildingId = MiUtil.parseLong(buildingId);
            Long lspecificationId= MiUtil.parseLong(specificationId);
            Long ldivisionId= MiUtil.parseLong(divisionId);
            Long lcustomerId= MiUtil.parseLong(customerId);
            Integer iType=MiUtil.parseInt(typeId);
            Integer iChannel=MiUtil.parseInt(channelId);
            hashMap=getSEJBDigitalDocumentRemote().validateVIDD(lBuildingId,iType,ldivisionId,lspecificationId,iChannel,lcustomerId,generatorType);
        }catch(Throwable t){
            manageCatch(hashMap, t);
        }
        logger.info(hashMap);
        return hashMap;
    }

    public HashMap saveDocumentGeneration(DocumentGenerationBean documentGenerationBean){
        logger.info("saveDocumentGeneration: Inicio:"+documentGenerationBean);
        logger.info("saveDocumentGeneration: getAssigneeId:"+documentGenerationBean.getAssigneeId());
        HashMap hashMap= new HashMap();
        try {
            hashMap=getSEJBDigitalDocumentRemote().saveDocumentGeneration(documentGenerationBean);
        }catch(Throwable t){
            manageCatch(hashMap, t);
        }
        logger.info("saveDocumentGeneration: Fin:"+hashMap);
        return hashMap;
    }

    public HashMap updateDocumentGeneration(DocumentGenerationBean documentGenerationBean){
        logger.info("updateDocumentGeneration: Inicio:"+documentGenerationBean);
        HashMap hashMap= new HashMap();
        try {
            hashMap=getSEJBDigitalDocumentRemote().updateDocumentGeneration(documentGenerationBean);
        }catch(Throwable t){
            manageCatch(hashMap, t);
        }
        logger.info("updateDocumentGeneration: Fin:"+hashMap);
        return hashMap;
    }

    public HashMap saveDocAssignee(DocAssigneeBean assigneeBean){
        logger.info("saveDocAssignee: Inicio:"+assigneeBean);
        HashMap hashMap= new HashMap();
        try {
            hashMap=getSEJBDigitalDocumentRemote().saveDocAssignee(assigneeBean);
        }catch(Throwable t){
            manageCatch(hashMap, t);
        }
        logger.info("saveDocAssignee: Fin:"+hashMap);
        return hashMap;
    }

    public HashMap updateDocAssignee(DocAssigneeBean assigneeBean){
        logger.info("updateDocAssignee: Inicio:"+assigneeBean);
        HashMap hashMap= new HashMap();
        try {
            hashMap=getSEJBDigitalDocumentRemote().updateDocAssignee(assigneeBean);
        }catch(Throwable t){
            manageCatch(hashMap, t);
        }
        logger.info("updateDocAssignee: Fin:"+hashMap);
        return hashMap;
    }

    public HashMap getDocumentGeneration(String orderId){
        logger.info("getDocumentGeneration:"+orderId);
        HashMap hashMap= new HashMap();
        try {
          hashMap=getSEJBDigitalDocumentRemote().getDocumentGeneration(MiUtil.parseLong(orderId));
        }catch(Throwable t){
            manageCatch(hashMap, t);
        }
        logger.info(hashMap);
        return hashMap;
    }


    private HeaderRequestType getHeader(String usuario,String trxId, int source){
        String application= (source == Constante.SOURCE_ORDERS_ID)?Constante.APPLICATION_ORDERS:Constante.APPLICATION_INCIDENT;
        String channel= (source == Constante.SOURCE_ORDERS_ID)?Constante.CHANNEL_ORDERS:Constante.CHANNEL_INCIDENT;
        HeaderRequestType header = new HeaderRequestType();
        header.setCanal(channel);
        header.setIdAplicacion(application);
        header.setUsuario(usuario);
        header.setIdTransaccionNegocio(trxId);
        header.setNodoAdicional(null);

        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date());
        XMLGregorianCalendar date;
        try {
            date = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
            header.setFechaInicio(date);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }

        return header;
    }
    private pe.com.entel.integracion.document.schema.HeaderRequestType getHeaderAttachment(String usuario,String trxId,int source){
        pe.com.entel.integracion.document.schema.HeaderRequestType header = new pe.com.entel.integracion.document.schema.HeaderRequestType();
        String application= (source == Constante.SOURCE_ORDERS_ID)?Constante.APPLICATION_ORDERS:Constante.APPLICATION_INCIDENT;
        String channel= (source == Constante.SOURCE_ORDERS_ID)?Constante.CHANNEL_ORDERS:Constante.CHANNEL_INCIDENT;
        header.setCanal(channel);
        header.setIdAplicacion(application);
        header.setUsuario(usuario);
        header.setIdTransaccionNegocio(trxId);


        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date());
        XMLGregorianCalendar date;
        try {
            date = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
            header.setFechaInicio(date);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }

        return header;
    }

    public DocumentGenerationBean getDocumentGenerationBean(OrderBean orderBean,HashMap hashMap){
        logger.info("getDocumentGenerationBean 1: Inicio:" + hashMap);
        DocumentGenerationBean documentGenerationBean=new DocumentGenerationBean();
        documentGenerationBean.setSpecificationId((long) orderBean.getNpSpecificationId());
        documentGenerationBean.setCustomerId(orderBean.getNpCustomerId());
        documentGenerationBean.setOrderId(orderBean.getNpOrderId());
        documentGenerationBean.setBuildingId(orderBean.getNpBuildingId());
        documentGenerationBean.setSignatureType(MiUtil.getInteger(hashMap.get("cmbSignature")));
        documentGenerationBean.setSignatureReason(MiUtil.getInteger(hashMap.get("cmbReason")));
        documentGenerationBean.setRequestNumber(MiUtil.getString(hashMap.get("txtNumSolicitud")));
        documentGenerationBean.setEmail(MiUtil.getString(hashMap.get("txtEmailDG")));
        documentGenerationBean.setEmailNullF(MiUtil.getInteger(hashMap.get("hdnEmailNullF")));
        if("S".equals(MiUtil.getString(hashMap.get("hdnFlagMigration")))){
            documentGenerationBean.setGenerationStatus(Constante.GENERATION_STATUS_ERROR);
        }else {
        documentGenerationBean.setGenerationStatus(Constante.GENERATION_STATUS_INITIAL);
        }
        documentGenerationBean.setCreatedBy(orderBean.getNpCreatedBy());

    return documentGenerationBean;
    }
    public DocumentGenerationBean getDocumentGenerationBean(RequestHashMap request,PortalSessionBean objPortalSesBean){
        logger.info("getDocumentGenerationBean 2: Inicio");
        DocumentGenerationBean documentGenerationBean=new DocumentGenerationBean();
        documentGenerationBean.setSpecificationId(MiUtil.parseLong(request.getParameter("hdnSubCategoria")));
        documentGenerationBean.setCustomerId(MiUtil.parseLong(request.getParameter("txtCompanyId")));
        documentGenerationBean.setOrderId(MiUtil.parseLong(request.getParameter("hdnOrderId")));
        documentGenerationBean.setBuildingId(MiUtil.parseLong(request.getParameter("hdnTiendaId")));
        Integer signatureType= MiUtil.getInteger(request.getParameter("cmbSignature"));
        Integer signatureReason=MiUtil.getInteger(request.getParameter("cmbReason"));
        documentGenerationBean.setSignatureType(signatureType);
        documentGenerationBean.setSignatureReason(signatureReason);
        documentGenerationBean.setEmail(MiUtil.getString(request.getParameter("txtEmailDG")));
        documentGenerationBean.setEmailNullF(MiUtil.getInteger(request.getParameter("hdnEmailNullF")));
        documentGenerationBean.setTrxType(4);
        documentGenerationBean.setGenerationStatus(0);
        documentGenerationBean.setEmailStatus(0);
        //documentGenerationBean.setCreatedBy(objPortalSesBean.getLogin());
        return documentGenerationBean;
    }
    public DocumentGenerationBean getDocumentGenerationBean(HttpServletRequest request, PortalSessionBean objPortalSesBean){
        logger.info("getDocumentGenerationBean 2: Inicio");
        DocumentGenerationBean documentGenerationBean=new DocumentGenerationBean();
        documentGenerationBean.setSpecificationId(MiUtil.parseLong(request.getParameter("hdnSubCategoria")));
        documentGenerationBean.setCustomerId(MiUtil.parseLong(request.getParameter("txtCompanyId")));
        documentGenerationBean.setOrderId(MiUtil.parseLong(request.getParameter("hdnOrderId")));
        documentGenerationBean.setBuildingId(MiUtil.parseLong(request.getParameter("hdnTiendaId")));
        Integer signatureType= MiUtil.getInteger(request.getParameter("cmbSignature"));
        Integer signatureReason=MiUtil.getInteger(request.getParameter("cmbReason"));
        documentGenerationBean.setSignatureType(signatureType);
        documentGenerationBean.setSignatureReason(signatureReason);
        documentGenerationBean.setEmail(MiUtil.getString(request.getParameter("txtEmailDG")));
        documentGenerationBean.setEmailNullF(MiUtil.getInteger(request.getParameter("hdnEmailNullF")));
        documentGenerationBean.setTrxType(4);
        documentGenerationBean.setGenerationStatus(0);
        documentGenerationBean.setEmailStatus(0);
        //documentGenerationBean.setCreatedBy(objPortalSesBean.getLogin());
        return documentGenerationBean;
    }

    public DocAssigneeBean getDocAssigneeBean(OrderBean orderBean, HashMap hashMap){
        logger.info("getDocAssigneeBean 1: Inicio:" + hashMap);
        DocAssigneeBean docAssigneeBean =new DocAssigneeBean();
        docAssigneeBean.setOrderId(orderBean.getNpOrderId());
        docAssigneeBean.setFirstName(MiUtil.getString(hashMap.get("txtFirstNameAssignee")));
        docAssigneeBean.setLastName(MiUtil.getString(hashMap.get("txtLastNameAssignee")));
        docAssigneeBean.setFamilyName(MiUtil.getString(hashMap.get("txtFamilyNameAssignee")));
        docAssigneeBean.setTypeDoc(MiUtil.getString(hashMap.get("hdnCmbDocTypeAssigneeText")));
        docAssigneeBean.setNumDoc(MiUtil.getString(hashMap.get("txtDocNumAssignee")));
        docAssigneeBean.setCreatedBy(orderBean.getNpCreatedBy());
        return docAssigneeBean;
    }

    public DocAssigneeBean getDocAssigneeBean(HttpServletRequest request,PortalSessionBean objPortalSesBean) {
        logger.info("getDocAssigneeBean 2: Inicio:" + request);
        DocAssigneeBean docAssigneeBean = new DocAssigneeBean();
        docAssigneeBean.setOrderId(MiUtil.getLong(request.getParameter("hdnOrderId")));
        docAssigneeBean.setFirstName(MiUtil.getString(request.getParameter("txtFirstNameAssignee")));
        docAssigneeBean.setLastName(MiUtil.getString(request.getParameter("txtLastNameAssignee")));
        docAssigneeBean.setFamilyName(MiUtil.getString(request.getParameter("txtFamilyNameAssignee")));
        docAssigneeBean.setTypeDoc(MiUtil.getString(request.getParameter("hdnCmbDocTypeAssigneeText")));
        docAssigneeBean.setNumDoc(MiUtil.getString(request.getParameter("txtDocNumAssignee")));
        docAssigneeBean.setCreatedBy(objPortalSesBean.getLogin());
        return docAssigneeBean;
    }

    public HashMap getDocAssignee(String orderId){
        logger.info("orderId:"+orderId);
        HashMap hashMap= new HashMap();
        try {
            hashMap=getSEJBDigitalDocumentRemote().getDocAssignee(MiUtil.parseLong(orderId));
        }catch(Throwable t){
            manageCatch(hashMap, t);
        }
        logger.info(hashMap);
        return hashMap;
    }

    public HashMap getDigitalDocumentsToGenerate(String source,String trxId,String specificationId,String customerId,String divisionId,String tipoEjec,String tipoTrans,String userLogin) throws Exception{
        HashMap mapResult = new HashMap();
        String traza= "[DigitalDocumentService][getDigitalDocumentsToGenerate] ";
        Long ltrxId=MiUtil.parseLong(trxId);
        Long lcustomerId=MiUtil.parseLong(customerId);
        Integer isource=MiUtil.parseInt(source);
        Long lspecificationId=MiUtil.parseLong(specificationId);
        String messageError;
        try{
            logger.info(traza + "INICIO");
            if(StringUtils.isBlank(trxId)){
                logger.info(traza + "transaccion vacia");
                messageError="TRANSACCION VACÍA";
            }else{
                logger.info(traza + "INICIO");
                GetRulesRequestType request = new GetRulesRequestType();
                request.setIdCliente(lcustomerId);
                if(Constante.SOURCE_ORDERS_ID==isource) {
                    request.setEspecificacionOrden(lspecificationId);
                }else{
                    request.setEspecificacionIncidente(lspecificationId);
                }
                request.setDivision(MiUtil.parseInt(divisionId));
                request.setTipoEjec(tipoEjec);
                request.setTipoTrans(tipoTrans);
                HeaderRequestType header = getHeader(userLogin,trxId,isource);
                HashMap obDocDig = getSEJBDigitalDocumentRemote().getDigitalDocumentsToGenerate(isource,ltrxId,request, header);
                messageError = (String)obDocDig.get(Constante.MESSAGE_OUTPUT);

                if(StringUtils.isBlank(messageError)){
                    GetRulesResponseType response = (GetRulesResponseType) obDocDig.get("result");
                    if(response != null){
                        String codResp = response.getResponseStatus().getCodigoRespuesta();
                        logger.info(traza + "CodResp: "+codResp);

                        if(Constante.CODIGO_RESP_OSB_OK.equals(codResp)){
                            List<ListaDocumentoType> listaDocumentoTypeList = response.getResponseData().getListaDocumentosPorGenerar().getListaDocumentoPorGenerarItem();
                            listaDocumentoTypeList=listaDocumentoTypeList!=null?listaDocumentoTypeList:new ArrayList<ListaDocumentoType>();
                            logger.info(traza + "Nro de documentos digitales: "+listaDocumentoTypeList.size());
                                List<String> list = new ArrayList<String>();
                                for(ListaDocumentoType item : listaDocumentoTypeList){
                                    list.add(item.getNombreDocumento());
                                }

                                mapResult.put("documentList", list);
                        }
                    }
                }
            }

            mapResult.put(Constante.MESSAGE_OUTPUT,messageError);
        }catch(Exception e){
            manageCatch(mapResult,e);
        }finally {
            logger.info(traza + "FIN");
        }
        logger.info(mapResult);
        return mapResult;
    }
    public HashMap generateDocumentsAndSendEmail(String idOrden,String divisionId,String channelClient,String userLogin) throws Exception{
        HashMap hashMapResult = new HashMap();
        Integer statusGeneration=0;
        Integer statusEmail;
        int lines=0;
        Long lorderId=Long.parseLong(idOrden);
        int iclientType=MiUtil.parseInt(channelClient);
        int idivisionId=MiUtil.parseInt(divisionId);
        HashMap hashMapDocumentGeneration= getDocumentGeneration(idOrden);
        String executionType,trxType;
        int pvtType=0;
        DocumentGenerationBean documentGenerationBean=(DocumentGenerationBean)hashMapDocumentGeneration.get("documentGenerationBean");
        documentGenerationBean.setGenerationStatus(Constante.GENERATION_STATUS_IN_PROCESS);
        documentGenerationBean.setModificationBy(userLogin);
        updateDocumentGeneration(documentGenerationBean);
        Long customerId=documentGenerationBean.getCustomerId();
        HashMap hashMapCustomer=getCustomerData(customerId);
        CustomerBean customerBean = (CustomerBean)hashMapCustomer.get("objCustomerBean");
        String documentNumber=MiUtil.getString(customerBean.getSwRuc());
        String documentType=customerBean.getNpTipoDoc();
        int itrxType=documentGenerationBean.getTrxType();
        int signatureType=documentGenerationBean.getSignatureType();
        if(signatureType==Constante.SIGNATURE_TYPE_DIGITAL){
            executionType=Constante.EXECUTION_TYPE_ASYNC;
        }else {
            executionType=Constante.EXECUTION_TYPE_SYNC;
        }

        String traza= "[DigitalDocumentService][generateDocumentsAndSendEmail]";
        try{
            logger.info(traza + "INICIO");
            if(StringUtils.isBlank(idOrden)){
                logger.info(traza + "orden vacia");
            }else {
                traza = traza + "[idOrden=" + idOrden + "] ";
                logger.info(traza + "INICIO");
                CreateDocumentByRuleRequestType request = new CreateDocumentByRuleRequestType();
                request.setIdOrden(lorderId);
                request.setTipoEjec(executionType);
                request.setTipoDocumento(documentType);
                request.setNumeroDocumento(documentNumber);
                HeaderRequestType header = getHeader(userLogin, idOrden, Constante.SOURCE_ORDERS_ID);
                if(itrxType==Constante.TRX_TYPE_POSTVENTA_ID){
                    pvtType=getPvtType(documentGenerationBean.getSpecificationId());
                    if(pvtType==Constante.POSTVENTA_TYPE_CP){
                        GetNumberActiveSuspendedLinesRequestType linesRequestType=new GetNumberActiveSuspendedLinesRequestType();
                        linesRequestType.setTipoDocumento(documentType);
                        linesRequestType.setNumeroDocumento(documentNumber);
                        lines=getNumberLinesPostpago(lorderId,linesRequestType,header);
                        if(lines==0) {
                            trxType = Constante.TRX_TYPE_CHANGE_PLAN;
                        }else {
                            trxType=Constante.TRX_TYPE_ALL_DOCUMENTS_BY_RULE;
                        }
                    }else {
                        trxType=Constante.TRX_TYPE_ALL_DOCUMENTS_BY_RULE;
                    }
                }else{
                    trxType=Constante.TRX_TYPE_ALL_DOCUMENTS_BY_RULE;
                }
                request.setTipoTrans(trxType);
                statusGeneration=generateDigitalDocuments(request,header);
                if(statusGeneration!=1){
                    documentGenerationBean.setSignatureReason(Constante.SIGNATURE_REASON_ERROR_GENERATION);
                    documentGenerationBean.setSignatureType(Constante.SIGNATURE_TYPE_MANUAL);
                }else
                if (documentGenerationBean.getSignatureType() == Constante.SIGNATURE_TYPE_DIGITAL && documentGenerationBean.getTrxType()==Constante.TRX_TYPE_POSTVENTA_ID) {
                    String templateCode;
                    SendEmailRequestType emailRequestType = new SendEmailRequestType();
                    emailRequestType.setTipoEjec(executionType);
                    emailRequestType.setTipoTrans(trxType);
                    emailRequestType.setIdOrden(lorderId);
                    HashMap hashMapGetInfoEmail = getInfoEmail(userLogin, itrxType,pvtType,trxType,iclientType, lorderId, customerId, idivisionId,documentGenerationBean.getSpecificationId(), lines);
                    String xmlVariable = (String) hashMapGetInfoEmail.get("xmlVariable");
                    if(xmlVariable==null){
                        hashMapGetInfoEmail=getInfoEmailPvt(itrxType,pvtType,iclientType, lorderId, customerId, idivisionId,documentGenerationBean.getSpecificationId(), lines);
                        xmlVariable = (String) hashMapGetInfoEmail.get("xmlVariable");
                    }
                    templateCode = (String) hashMapGetInfoEmail.get("templateCode");
                    emailRequestType.setCodPlantillaCorreo(templateCode);
                    xmlVariable = Base64.encode(MiUtil.getString(xmlVariable).getBytes());
                    emailRequestType.setXmlVariable(xmlVariable);
                    statusEmail=sendEmail(emailRequestType,header);
                    documentGenerationBean.setEmailStatus(statusEmail);
                }
                documentGenerationBean.setModificationBy(userLogin);
                documentGenerationBean.setGenerationStatus(statusGeneration);
                updateDocumentGeneration(documentGenerationBean);
                if(statusGeneration!=1){
                    if(documentGenerationBean.getTrxType()==Constante.TRX_TYPE_POSTVENTA_ID) {
                        updateOrderCode("P"+documentGenerationBean.getOrderId(), documentGenerationBean.getOrderId());
                    }else{
                        updateOrderCode("", documentGenerationBean.getOrderId());
                    }
                }
            }
            hashMapResult.put("message",getMessageGeneration(documentGenerationBean));
            hashMapResult.put("status",statusGeneration);


        }catch(Exception e){
            manageCatch(hashMapResult,e);
        }finally {
            logger.info(traza + "FIN");
        }

        return hashMapResult;
    }



    public HashMap generateDigitalDocuments(String idOrden, String tipoEjec, String tipoTrans, String tipoDoc, String numDoc, String userLogin) throws Exception{
        HashMap mapResult = new HashMap();
        String traza= "[DigitalDocumentService][generateDigitalDocuments]";
        try{
            logger.info(traza + "INICIO");
            if(StringUtils.isBlank(idOrden)){
                logger.info(traza + "orden vacia");
            }else{
                traza= traza+"[idOrden="+idOrden+"] ";
                logger.info(traza + "INICIO");
                CreateDocumentByRuleRequestType request = new CreateDocumentByRuleRequestType();

                request.setIdOrden(MiUtil.getLong(idOrden));
                request.setTipoEjec(tipoEjec);
                request.setTipoTrans(tipoTrans);
                request.setTipoDocumento(tipoDoc);
                request.setNumeroDocumento(numDoc);
                HeaderRequestType header = getHeader(userLogin, idOrden, Constante.SOURCE_ORDERS_ID);

                int statusGeneration=generateDigitalDocuments(request,header);

                mapResult.put("statusGeneration",statusGeneration);
                /*HashMap obDocDig = getSEJBDigitalDocumentRemote().generateDigitalDocuments(request, header);
                    CreateDocumentByRuleResponseType response = (CreateDocumentByRuleResponseType)obDocDig.get("result");

                if(response != null){
                    mapResult.put("responseStatus",response.getResponseStatus());
                }else{
                    mapResult.put("responseStatus",null);
                }
                 */

            }

        }catch(Exception e){
            manageCatch(mapResult,e);
        }finally {
            logger.info(traza + "FIN");
        }

        return mapResult;
    }

    public HashMap getValidaCategoriaSolucionDigitalization(int order) {
        logger.info("order:"+order);
        HashMap objHashMap = new HashMap();
        String strMessage = "";

        try{
            objHashMap=getSEJBDigitalDocumentRemote().getValidaCategoriaSolucionDigitalization(order);

        }
        catch(SQLException e){
            strMessage  = "[SQLException][getValidaCategoriaSolucionDigitalization][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessageError",strMessage);
            logger.info("strMessageError:"+strMessage);
        }
        catch(RemoteException e){
            strMessage  = "[RemoteException][getValidaCategoriaSolucionDigitalization][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessageError",strMessage);
            logger.info("strMessageError:"+strMessage);
        }
        catch(Exception e){
            strMessage  = "[Exception][getValidaCategoriaSolucionDigitalization][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessageError",strMessage);
            logger.info("strMessageError:"+strMessage);
        }
        logger.info(objHashMap);
        return  objHashMap;

    }

    /**
     * Motivo: Obtiene la lista de configuración dependiendo del dominio, origen, tipo de transacción, canal y sección
     * <br>Realizado por: <a href="mailto:johana.rios@soapros.pe">Johana Rios</a>
     * <br>Fecha: 05/04/2017
     * @param     int iCustomerid
     * @return	  String
     */
    public HashMap getValueNpDigitalConfig(String domain,int origen,int typetrx, int channel, int section){
        logger.info("domain:"+domain+",origen:"+origen+",typetrx:"+typetrx+",channel:"+channel+",section:"+section);
        HashMap hshDataMap = new HashMap();
        try {
            hshDataMap = getSEJBDigitalDocumentRemote().getValueNpDigitalConfig(domain,origen,typetrx, channel,section);

        }catch(Throwable t){
            manageCatch(hshDataMap, t);
        }
        logger.info(hshDataMap);
        return hshDataMap;
    }

    /**
     * Motivo: Obtiene el si el cliente es Empresa (E) o Persona (P)
     * <br>Realizado por: <a href="mailto:johana.rios@soapros.pe">Johana Rios</a>
     * <br>Fecha: 31/03/2017
     * @param     int iCustomerid
     * @return	  String
     */
    public HashMap getClientTypeFromCustomerId(int iCustomerid){
        logger.info("customerId"+iCustomerid);
        HashMap hshDataMap = new HashMap();
        try {
            String strClientType=getSEJBDigitalDocumentRemote().getClientTypeFromCustomerId(iCustomerid);
            hshDataMap.put("strClientType",strClientType);
        }catch(Throwable t){
            manageCatch(hshDataMap, t);
        }
        logger.info(hshDataMap);
        return hshDataMap;
    }







    /**
     * Motivo: Obtiene el si el cliente es Empresa (E) o Persona (P)
     * <br>Realizado por: <a href="mailto:johana.rios@soapros.pe">Johana Rios</a>
     * <br>Fecha: 31/03/2017
     */
    public HashMap getSolNumber(String source,String specificationId,String channel, String division){
        logger.info("source"+source+",specificationId:"+specificationId+",channel:"+channel);
        HashMap hshDataMap = new HashMap();
        try {
            hshDataMap=getSEJBDigitalDocumentRemote().getSolNumber(MiUtil.getInt(source),MiUtil.getLong(specificationId),MiUtil.getInt(channel),MiUtil.getInt(division));
        }catch(Throwable t){
            manageCatch(hshDataMap, t);
        }
        logger.info(hshDataMap);
        return hshDataMap;
    }

    /**
     * Motivo: Obtiene flag de caso especial
     * <br>Realizado por: <a href="mailto:johana.rios@soapros.pe">Johana Rios</a>
     * <br>Fecha: 31/03/2017
     * @param     int iOrderid
     * @return	  String
     */
    public HashMap getFlagSpecialCase(String sOrderid){
        logger.info("[DigitalDocumentService][getFlagSpecialCase] - OrderId:" + sOrderid);
        String strFlagSpecialCase="";
        HashMap hshDataMap = new HashMap();
        try {
            Long lOrderId = MiUtil.parseLong(sOrderid);
            hshDataMap=getSEJBDigitalDocumentRemote().getFlagSpecialCase(lOrderId);

        }catch(Throwable t){
            manageCatch(hshDataMap, t);
        }
        logger.info(hshDataMap);
        return hshDataMap;
    }


    /**
     * Motivo: Enviar documentos adjuntos a servidor de ACEPTA mediante servicios de SOAINT
     * <br>Realizado por: <a href="mailto:jefferson.vergara@soapros.pe">Jefferson Vergara</a>
     * <br>Fecha: 25/04/2017
     */
    public HashMap sendAttachedDocuments(List<FileItem> items,String source, String trxId, String userLogin,String customerDoctType, String customerNumDoc){
        HashMap hshDataMapResult = new HashMap();
        String traza= "[DigitalDocumentService][sendAttachedDocuments] ";
        Long ltrxId=MiUtil.parseLong(trxId);
        Integer isource=MiUtil.parseInt(source);
        HashMap hshValues = new HashMap();

        List<NpTableBean> npTableBeans = new ArrayList<NpTableBean>();
        try{
            hshValues = getSEJBDigitalDocumentRemote().getValueNpDigitalConfig(Constante.ATTACH_DOC_TYPE_WS, Constante.SOURCE_ALL_ID, Constante.TRX_TYPE_ALL, Constante.CHANNEL_ALL_ID, Constante.SECTION_ALL_ID);
            ArrayList arrayValues = (ArrayList)hshValues.get("objArrayList");
            for(Object obj:arrayValues){
                NpTableBean npTableBean = new NpTableBean();
                npTableBean.setNpvalue((String)((HashMap)obj).get("npvalue"));
                npTableBean.setNpvaluedesc((String) ((HashMap) obj).get("npvaluedesc"));
                npTableBeans.add(npTableBean);
            }

            logger.info(traza + "INICIO");


            if(StringUtils.isBlank(trxId)){
                logger.info(traza + "transaccion vacia");
            }else{
                logger.info(traza + "transaccion no vacia");
                pe.com.entel.integracion.document.schema.HeaderRequestType header = getHeaderAttachment(userLogin,trxId,isource);
                ListaDocumentoItemType listaDocumentoItemType;
                ListaDocumentosType listaDocumentosType = new ListaDocumentosType();
                logger.info(traza + "Antes de agregar documentos a la lista input size: " + items.size());
                for(FileItem fileItem:items){
                    listaDocumentoItemType = new ListaDocumentoItemType();
                    for(NpTableBean aux:npTableBeans){
                        if(aux.getNpvalue().equals(fileItem.getFieldName())){
                            listaDocumentoItemType.setNombre(aux.getNpvaluedesc());
                        }
                    }
                    listaDocumentoItemType.setNombreDocumento(FilenameUtils.getName(fileItem.getName()));
                    listaDocumentoItemType.setTipoArchivo(fileItem.getContentType());
                    listaDocumentoItemType.setContenido(getB64code(fileItem));
                    listaDocumentosType.getListaDocumentoItem().add(listaDocumentoItemType);
                }
                logger.info(traza + "Luego de agregar documentos a la lista tamano "+listaDocumentosType.getListaDocumentoItem().size());

                CreateDocumentRequestType createDocumentRequestType = new CreateDocumentRequestType();

                if(Constante.SOURCE_ORDERS_ID==isource) {
                    createDocumentRequestType.setIdOrden(ltrxId);
                }else{
                    createDocumentRequestType.setIdIncidente(ltrxId);
                }
                createDocumentRequestType.setTipoDocumento(customerDoctType);
                createDocumentRequestType.setNumeroDocumento(customerNumDoc);
                createDocumentRequestType.setListaDocumentos(listaDocumentosType);

                 HashMap obDocAtt = getSEJBDigitalDocumentRemote().sendAttachedDocuments(isource, ltrxId, createDocumentRequestType, header);
                String messageError = (String)obDocAtt.get(Constante.MESSAGE_OUTPUT);
                if(StringUtils.isBlank(messageError)){
                    CreateDocumentResponseType response = (CreateDocumentResponseType)obDocAtt.get(Constante.RESULT);
                    if(response != null){
                        String codResp = response.getResponseStatus().getCodigoRespuesta();
                        logger.info(traza + "CodResp: "+codResp);
                        if(Constante.CODIGO_RESP_OSB_OK.equals(codResp)){
                            hshDataMapResult.put(Constante.MESSAGE_OUTPUT,codResp);
                        }else{
                            hshDataMapResult.put(Constante.MESSAGE_OUTPUT,"Error en el servicio de adjuntar documentos");
                        }
                    }else{
                        hshDataMapResult.put(Constante.MESSAGE_OUTPUT,"No hay respuesta del servicio de adjuntar documentos");
                    }

                }else{
                    hshDataMapResult.put(Constante.MESSAGE_OUTPUT,"Error en el servicio de adjuntar documentos");
                }

            }

        }catch(Exception e){
            manageCatch(hshDataMapResult,e);
        }finally {
            logger.info(traza + "FIN");
        }

        logger.info(hshDataMapResult);

        return hshDataMapResult;
    }



    /**
     * Motivo: Codifica en formato Base64Binary un arreglo de bytes
     * <br>Realizado por: <a href="mailto:jefferson.vergara@soapros.pe">Jefferson Vergara</a>
     * <br>Fecha: 25/04/2017
     */

    private byte[] getB64code(FileItem item){
        logger.info("Codificando ...");
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int sizeInBytes = (int)item.getSize();
        byte[] bytes = new byte[sizeInBytes];
        try {
            item.getInputStream().read(bytes);
            logger.info("tamano bytes " + bytes.length);
            byte[] bytes2 = IOUtils.toByteArray(item.getInputStream());

            logger.info("tamano bytes2 "+ bytes2.length);

        } catch (IOException e) {
            e.printStackTrace();
        }

        String encoded = DatatypeConverter.printBase64Binary(bytes);
        byte[] decoded = DatatypeConverter.parseBase64Binary(encoded);


 /*
        int nRead;
        byte[] data = new byte[16384];

        try {
            while ((nRead = item.getInputStream().read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);

            }
            buffer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String encoded = DatatypeConverter.printBase64Binary(buffer.toByteArray());
        byte[] decoded = DatatypeConverter.parseBase64Binary(encoded);
        */
        return decoded;
    }

    public HashMap getListDigitalDocuments(String idOrden,String idIncidente, String numeroDocumento, String estadoDocumento,String userLogin,String tipoDocumento) throws Exception{
        HashMap mapResult = new HashMap();
        String traza = "[DigitalDocumentService][getListDigitalDocuments]";
        String statusOrdenAdj = "0";
        String statusOrdenGen = "0";
        String statusOrdenTodos = "0";
        int sourceTrx = 0;
        String idTrx = "";
        try{
            logger.info(traza + "INICIO " + tipoDocumento);
            logger.info(traza + "INICIO " + "[idOrden=" + idOrden + " idIncidente=" + idIncidente +"] ");
                if((idOrden == null || idOrden.equals("0")) && (idIncidente == null || idIncidente.equals("0"))){
                    logger.info(traza + "orden o incidente vacio");
                    mapResult.put("statusOrden", Constante.COD_ERROR_DOC_LIST);
                } else {

                    GetDocumentListRequestType request = new GetDocumentListRequestType();

                    if(StringUtils.isBlank(idIncidente) || idIncidente.equals("0")) {
                        sourceTrx = Constante.SOURCE_ORDERS_ID;
                        idTrx = idOrden;
                        request.setIdOrden(Long.parseLong(idOrden));
                    } else{
                        sourceTrx = Constante.SOURCE_INCIDENT_ID;
                        idTrx = idIncidente;
                        request.setIdIncidente(Long.parseLong(idIncidente));
                    }

                    logger.info(traza + "[sourceTrx=" + sourceTrx + " idTrx=" + idTrx +"] ");

                    request.setNumeroDocumento(numeroDocumento);
                    request.setEstadoDocumento(estadoDocumento);

                    logger.info(traza + "[numeroDocumento=" + numeroDocumento + " estadoDocumento=" + estadoDocumento +"] ");

                    HeaderRequestType header = getHeader(userLogin, idTrx, sourceTrx);

                    HashMap obDocDig = getSEJBDigitalDocumentRemote().getListDigitalDocuments(request, header);

                    GetDocumentListResponseType response = (GetDocumentListResponseType) obDocDig.get("result");
                    String messageError = (String)obDocDig.get(Constante.MESSAGE_OUTPUT);
                    logger.info(traza + "Response: " + response);
                    logger.info(traza + "messageError: " + messageError);
                    if (response != null) {
                        String codResp = response.getResponseStatus().getCodigoRespuesta();
                        logger.info(traza + "CodResp: " + codResp);
                        if (Constante.CODIGO_RESP_OSB_OK.equals(codResp)) {
                            List<pe.com.entel.esb.message.documentmanage.getdocumentlist.v1.ListaDocumentoItemType>
                                    listaDocumentoTypeList = response.getResponseData().getListaDocumentos().getListaDocumentoItem();

                            listaDocumentoTypeList = listaDocumentoTypeList != null ?
                                    listaDocumentoTypeList : new ArrayList<pe.com.entel.esb.message.documentmanage.getdocumentlist.v1.ListaDocumentoItemType>();

                            logger.info(traza + "Nro de documentos digitales: " + listaDocumentoTypeList.size());

                            List<pe.com.entel.esb.message.documentmanage.getdocumentlist.v1.ListaDocumentoItemType> listGenerados = new ArrayList<pe.com.entel.esb.message.documentmanage.getdocumentlist.v1.ListaDocumentoItemType>();
                            List<pe.com.entel.esb.message.documentmanage.getdocumentlist.v1.ListaDocumentoItemType> listAdjuntos = new ArrayList<pe.com.entel.esb.message.documentmanage.getdocumentlist.v1.ListaDocumentoItemType>();
                            List<pe.com.entel.esb.message.documentmanage.getdocumentlist.v1.ListaDocumentoItemType> listTodos = new ArrayList<pe.com.entel.esb.message.documentmanage.getdocumentlist.v1.ListaDocumentoItemType>();
                            if (listaDocumentoTypeList.size() == 0) {
                                statusOrdenTodos = Constante.COD_CLEAR_DOC_LIST;
                                statusOrdenGen = Constante.COD_CLEAR_DOC_LIST;
                                statusOrdenAdj = Constante.COD_CLEAR_DOC_LIST;
                            } else {
                                for (pe.com.entel.esb.message.documentmanage.getdocumentlist.v1.ListaDocumentoItemType item : listaDocumentoTypeList) {
                                    if (item.getTipoCarga().equals(Constante.DIGITAL_DOCUMENT_GENERATED)) {
                                        listGenerados.add(item);
                                        if (!item.getEstadoFinal().equals(Constante.STATUS_DIGITAL_DOCUMENTS)) {
                                            statusOrdenGen = Constante.COD_OK_DOC_LIST;
                                            logger.info(traza + "statusOrdenGen: " + statusOrdenGen);
                                        } else if(tipoDocumento == Constante.DIGITAL_DOCUMENT_GENERATED){
                                            statusOrdenGen = Constante.COD_WAIT_DOC_LIST;
                                            logger.info(traza + "statusOrdenGen: " + statusOrdenGen);
                                            break;
                                        }
                                    }
                                    if (item.getTipoCarga().equals(Constante.ATTACHE_DIGITAL_DOCUMENTS)) {
                                        if (!item.getEstadoFinal().equals(Constante.STATUS_DIGITAL_DOCUMENTS)) {
                                            listAdjuntos.add(item);
                                            statusOrdenAdj = Constante.COD_OK_DOC_LIST;
                                            logger.info(traza + "statusOrdenAdj: " + statusOrdenAdj);
                                        } else if(statusOrdenAdj != Constante.COD_OK_DOC_LIST){
                                            statusOrdenAdj = Constante.COD_WAIT_DOC_LIST;
                                        }
                                    }
                                    statusOrdenTodos = Constante.COD_OK_DOC_LIST;
                                    listTodos.add(item);
                                }
                                logger.info(traza + "statusOrdenTodos: " + statusOrdenTodos);
                            }
                            if (tipoDocumento == Constante.DIGITAL_DOCUMENT_GENERATED) {
                                logger.info(traza + "Nro de documentos digitales Generados: " + listGenerados.size());
                                mapResult.put("documentList", listGenerados);
                                mapResult.put("statusOrden", statusOrdenGen);
                            } else {
                                if (tipoDocumento == Constante.ATTACHE_DIGITAL_DOCUMENTS) {
                                    logger.info(traza + "Nro de documentos digitales Adjuntos: " + listAdjuntos.size());
                                    mapResult.put("documentList", listAdjuntos);
                                    mapResult.put("statusOrden", statusOrdenAdj);
                                } else {
                                    if (tipoDocumento == Constante.DIGITAL_DOCUMENT_ALL) {
                                        logger.info(traza + "Nro de documentos digitales listar todos: " + listTodos.size());
                                        mapResult.put("documentList", listTodos);
                                        mapResult.put("statusOrden", statusOrdenTodos);
                                    }
                                }
                            }
                        } else{
                            mapResult.put("statusOrden", Constante.COD_ERROR_DOC_LIST);
                            mapResult.put(Constante.MESSAGE_OUTPUT, messageError);
                        }
                    } else{
                        mapResult.put("statusOrden", Constante.COD_ERROR_DOC_LIST);
                        mapResult.put(Constante.MESSAGE_OUTPUT, messageError);
                    }
                }
            }catch(Exception e){
                manageCatch(mapResult,e);
                e.printStackTrace();
                logger.info(traza + " ERROR " + e.getMessage());
                manageCatch(mapResult,e);
            }finally {
                logger.info(traza + "FIN");
            }
            return mapResult;
        }

    public Map<String, Object> verDocumentoDigital(String idAceptaDoc, String userLogin, String trxId, int source) throws Exception{
        Map<String, Object> mapResult = new HashMap<String, Object>();
        String traza= "[DigitalDocumentService][verDocumentoDigital] ";
        try{
            if(StringUtils.isBlank(idAceptaDoc)){
                logger.info(traza + "INICIO");
                logger.info(traza + "idAceptaDocumento es vacio");
            }else{
                traza= traza+"[idAceptaDoc="+idAceptaDoc+"] ";
                logger.info(traza + "INICIO");

                GetDocumentRequestType request = new GetDocumentRequestType();
                request.setIdDocumento(idAceptaDoc);

                HeaderRequestType header = getHeader(userLogin, trxId, source);

                logger.info(traza + " header:" + MiUtil.transfromarAnyObjectToXmlText(header));

                Map<String, Object> obDocDig = getSEJBDigitalDocumentRemote().verDocumentoDigital(request, header);

                String messageError = (String)obDocDig.get(Constante.MESSAGE_OUTPUT);

                if(StringUtils.isBlank(messageError)){
                    GetDocumentResponseType response = (GetDocumentResponseType)obDocDig.get("result");

                    if(response != null){
                        String codResp = response.getResponseStatus().getCodigoRespuesta();
                        logger.info(traza + "CodResp: "+codResp);

                        if(Constante.CODIGO_RESP_OSB_OK.equals(codResp)){
                            byte[] contenidoFile = response.getResponseData().getDocumento().getContenido();
                            String mimeType = response.getResponseData().getDocumento().getMimeType();
                            String fileName = response.getResponseData().getDocumento().getNombreDocumento();
                            mapResult.put("contenidoFile", contenidoFile);
                            mapResult.put("mimeType", mimeType);
                            mapResult.put("fileName", fileName);
                        }
                    }
                }
            }
        }catch(RemoteException e){
            logger.info(traza + "RemoteException: "+e.getMessage());
        }catch(Exception e) {
            logger.info(traza + "Exception: " + e.getMessage());
        }finally {
            logger.info(traza + "FIN");
        }

        return mapResult;
    }


    /**
     * Motivo: Valida los archivos adjuntados por tipo y peso
     * <br>Realizado por: <a href="mailto:jefferson.vergara@soapros.pe">Jefferson Vergara</a>
     * <br>Fecha: 08/05/2017
     */
    public HashMap getvalidateUploadedFiles(String strFileType, int iFileSize, String strContentType){
        logger.info("strFileType: "+strFileType+",iFileSize: "+iFileSize);
        HashMap hshDataMap = new HashMap();
        int iFileTypeId = Integer.valueOf(strFileType);
        try {
            hshDataMap=getSEJBDigitalDocumentRemote().getvalidateUploadedFiles(iFileTypeId, iFileSize,strContentType);
        }catch(Throwable t){
            manageCatch(hshDataMap, t);
        }
        logger.info(hshDataMap);
        return hshDataMap;
    }

    /**
     * Motivo: Valida si se debe mostrar la seccion de adjuntar documentos
     * <br>Realizado por: <a href="mailto:jefferson.vergara@soapros.pe">Jefferson Vergara</a>
     * <br>Fecha: 09/05/2017
     */
    public HashMap getAttachSectionStatus(int iorigen, int itypetrx, int ibuidingid) {
        logger.info("iorigen: "+iorigen+",itypetrx: "+itypetrx);
        HashMap hshDataMap = new HashMap();
        try {
            hshDataMap=getSEJBDigitalDocumentRemote().getAttachSectionStatus(iorigen,itypetrx,ibuidingid);
        }catch(Throwable t){
            manageCatch(hshDataMap, t);
        }
        logger.info(hshDataMap);
        return hshDataMap;
    }

    public HashMap getCustomerData(long lCustomerid){

        HashMap hshDataMap = new HashMap();
        try {
            hshDataMap=getSEJBDigitalDocumentRemote().getCustomerData(lCustomerid);
        }catch(Throwable t){
            manageCatch(hshDataMap, t);
        }

        return hshDataMap;
    }

    public void saveDocumentGenerationAndAssigneST(HttpServletRequest request, PortalSessionBean objPortalSesBean,
                                                   DocAssigneeBean docAssigneeBean, DocumentGenerationBean documentGenerationBean) {

        logger.info("[DigitalDocumentService][saveDocumentGenerationAndAssigneST] - hdnGenStatus: "+request.getParameter("hdnGenStatus") +" || orderid: "+documentGenerationBean.getOrderId());
        HashMap hashMapDigi = new HashMap();


        if (Constante.FLAG_UPD_DOC_GENERATION_NO == MiUtil.getInt(request.getParameter("hdnGenStatus"))) {
            if(Constante.FLAG_SECTION_ST_ACTIVE == MiUtil.getInt(request.getParameter("hdnFlgSDD"))) {
                if (MiUtil.getLong(request.getParameter("hdnDocGenId")) == 0) {
                if(Constante.FLAG_SECTION_ST_ACTIVE == MiUtil.getInt(request.getParameter("hdnFlgSAC")) && "1".equals(MiUtil.getString(request.getParameter("hdnChkAssignee")))){
                        if (MiUtil.getLong(request.getParameter("hdnDocAssigneeId")) == 0) {
                            hashMapDigi = saveDocAssignee(docAssigneeBean);
                            documentGenerationBean.setAssigneeId(MiUtil.getLong(hashMapDigi.get("idAssignee")));
                        } else {
                            docAssigneeBean.setDocAssigneeId(MiUtil.getLong(request.getParameter("hdnDocAssigneeId")));
                            updateDocAssignee(docAssigneeBean);
                            documentGenerationBean.setAssigneeId(MiUtil.getLong(request.getParameter("hdnDocAssigneeId")));
                        }
                    }
                    documentGenerationBean.setCreatedBy(objPortalSesBean.getLogin());
                    saveDocumentGeneration(documentGenerationBean);
                } else {
                    documentGenerationBean.setId(MiUtil.getLong(request.getParameter("hdnDocGenId")));
                    documentGenerationBean.setModificationBy(objPortalSesBean.getLogin());

                if(Constante.FLAG_SECTION_ST_ACTIVE == MiUtil.getInt(request.getParameter("hdnFlgSAC")) && "1".equals(MiUtil.getString(request.getParameter("hdnChkAssignee")))){
                        if (MiUtil.getLong(request.getParameter("hdnDocAssigneeId")) == 0) {
                            hashMapDigi = saveDocAssignee(docAssigneeBean);
                            documentGenerationBean.setAssigneeId(MiUtil.getLong(hashMapDigi.get("idAssignee")));
                        } else {
                            docAssigneeBean.setDocAssigneeId(MiUtil.getLong(request.getParameter("hdnDocAssigneeId")));
                            updateDocAssignee(docAssigneeBean);
                            documentGenerationBean.setAssigneeId(MiUtil.getLong(request.getParameter("hdnDocAssigneeId")));
                        }
                    }else{
                        docAssigneeBean.setOrderId(null);
                        updateDocAssignee(docAssigneeBean);
                        documentGenerationBean.setAssigneeId(null);
                    }

                    updateDocumentGeneration(documentGenerationBean);
                }
            } else {
                if (Constante.FLAG_SECTION_ST_ACTIVE == MiUtil.getInt(request.getParameter("hdnFlgSAC")) && "1".equals(MiUtil.getString(request.getParameter("hdnChkAssignee")))) {
                    if (MiUtil.getLong(request.getParameter("hdnDocAssigneeId")) == 0) {
                        hashMapDigi = saveDocAssignee(docAssigneeBean);
                    } else {
                        docAssigneeBean.setDocAssigneeId(MiUtil.getLong(request.getParameter("hdnDocAssigneeId")));
                        updateDocAssignee(docAssigneeBean);
                    }
                }
            }
    }
    }

    /**
     * Motivo: Valida si debe generar los documentos con firma manual o digital segun VIA
     * <br>Realizado por: <a href="mailto:jefferson.vergara@soapros.pe">Jefferson Vergara</a>
     * <br>Fecha: 11/05/2017
     */

    public HashMap validateVIATechnicalService(String orderId, int customerId, String strLogin, int iChkAssignee, String strDocNumAssignee, String cmbDocTypeAssignee) {


        logger.info("orderId: "+orderId+", customerId: "+customerId+", strLogin: "+strLogin+", iChkAssignee: "+iChkAssignee+", strDocNumAssignee: "+strDocNumAssignee+", cmbDocTypeAssignee: "+cmbDocTypeAssignee);
        HashMap hshDataMap = new HashMap();
        try {
            hshDataMap=getSEJBDigitalDocumentRemote().validateVIATechnicalService(orderId,customerId,strLogin,iChkAssignee,strDocNumAssignee,cmbDocTypeAssignee);
        }catch(Throwable t){
            manageCatch(hshDataMap, t);
    }
        logger.info(hshDataMap);
        return hshDataMap;
    }

    public HashMap updateOrderCode(String sOrdercode, long iOrderid){
        logger.info("updateOrderCode: Inicio:"+sOrdercode);
        HashMap hashMap= new HashMap();
        try {
            hashMap = getSEJBDigitalDocumentRemote().updateOrderCode(sOrdercode, iOrderid);
        }catch(Throwable t){
            manageCatch(hashMap, t);
        }
        logger.info("updateOrderCode: Fin:"+hashMap);
        return hashMap;
    }

    /**
     * Motivo: Valida si se debe mostrar la seccion de adjuntar documentos
     * <br>Realizado por: <a href="mailto:jefferson.vergara@soapros.pe">Jefferson Vergara</a>
     * <br>Fecha: 09/05/2017
     */
    public HashMap getSignatureReason(int iorigen, int itypetrx) {
        logger.info("[DigitalDocumentService][getSignedReason] iorigen: "+iorigen+",itypetrx: "+itypetrx);
        HashMap hshDataMap = new HashMap();
        try {
            hshDataMap=getSEJBDigitalDocumentRemote().getSignatureReason(iorigen, itypetrx);
        }catch(Throwable t){
            manageCatch(hshDataMap, t);
        }
        logger.info(hshDataMap);
        return hshDataMap;
    }

    public HashMap getDocumentGenerationInc(String incidentId){
        logger.info("[DigitalDocumentService][getSignedReason] incidentId:"+incidentId);
        HashMap hashMap= new HashMap();
        try {
            hashMap = getSEJBDigitalDocumentRemote().getDocumentGenerationInc(MiUtil.parseLong(incidentId));
        }catch(Throwable t){
            manageCatch(hashMap, t);
        }
        logger.info(hashMap);
        return hashMap;
    }
    public HashMap getInfoEmail(String user, int trxType,int pvtType,String transType,int channel,long orderId,long customerId,int divisionId,long specificationId,int lines)  throws Exception{
        logger.info("[DigitalDocumentService][getInfoEmail] orderId:"+orderId);
        HashMap hashMap= new HashMap();
        try {
            hashMap = getSEJBDigitalDocumentRemote().getInfoEmail(user,trxType,pvtType,transType,channel,orderId,customerId,divisionId,specificationId,lines);
        }catch(Throwable t){
            manageCatch(hashMap, t);
        }
        logger.info(hashMap);
        return hashMap;
    }
    public HashMap getInfoEmailPvt(int trxType,int pvtType,int channel,long orderId,long customerId,int divisionId,long specificationId,int lines)  throws Exception{
        logger.info("[DigitalDocumentService][getInfoEmailPvt] orderId:"+orderId);
        HashMap hashMap= new HashMap();
        try {
            hashMap = getSEJBDigitalDocumentRemote().getInfoEmailPvt(trxType,pvtType,channel,orderId,customerId,divisionId,specificationId,lines);
        }catch(Throwable t){
            manageCatch(hashMap, t);
        }
        logger.info(hashMap);
        return hashMap;
    }

    public int sendEmail(SendEmailRequestType request,HeaderRequestType header)  throws Exception{
        logger.info("[DigitalDocumentService][getInfoEmail] orderId:"+request.getIdOrden());
        int statusEmail=Constante.RESPONSE_CODE_WS_EXCEPTION;
        HashMap hashMap= new HashMap();
        try {
            hashMap = getSEJBDigitalDocumentRemote().sendEmail(request,header);
            if(StringUtils.isBlank((String) hashMap.get(Constante.MESSAGE_OUTPUT))){
                SendEmailResponseType response = (SendEmailResponseType) hashMap.get("result");
                String responseCode = response.getResponseStatus().getCodigoRespuesta();
                if(Constante.CODIGO_RESP_SEND_EMAIL_OK.equals(responseCode)){
                    statusEmail=Constante.RESPONSE_CODE_WS_SUCCESS;
                }else{
                    statusEmail=Constante.RESPONSE_CODE_WS_FAIL;
                }
            }else{
                statusEmail=Constante.RESPONSE_CODE_WS_EXCEPTION;
            }

        }catch(Exception t){
            logger.error("Error in sendEmail",t);
        }
        logger.info(hashMap);
        return statusEmail;
    }
    public int generateDigitalDocuments(CreateDocumentByRuleRequestType request,HeaderRequestType header)  throws Exception{
        logger.info("[DigitalDocumentService][generateDigitalDocuments] orderId:"+request.getIdOrden());
        int statusGeneration=Constante.RESPONSE_CODE_WS_EXCEPTION;
        HashMap hashMap= new HashMap();
        try {
            hashMap = getSEJBDigitalDocumentRemote().generateDigitalDocuments(request,header);
            if(StringUtils.isBlank((String) hashMap.get(Constante.MESSAGE_OUTPUT))){
                CreateDocumentByRuleResponseType response = (CreateDocumentByRuleResponseType) hashMap.get("result");
                String responseCode = response.getResponseStatus().getCodigoRespuesta();
                if(Constante.CODIGO_RESP_OSB_OK.equals(responseCode)){
                    statusGeneration=Constante.RESPONSE_CODE_WS_SUCCESS;
                }else {
                    statusGeneration=Constante.RESPONSE_CODE_WS_FAIL;
                }

            }else {
                statusGeneration=Constante.RESPONSE_CODE_WS_EXCEPTION;
            }

        }catch(Exception t){
            logger.error("Error in generateDigitalDocuments",t);
        }
        logger.info(hashMap);
        return statusGeneration;
    }

    /**
     * Motivo: Actualiza el tipo de Firma a Manual según su VI en la generacion de documentos.
     * <br>Realizado por: <a href="mailto:jefferson.vergara@soapros.pe">Jefferson Vergara</a>
     * <br>Fecha: 18/05/2017
     */
    public HashMap updateGenerationVI(int iorderid, String strLoginid) {
        logger.info("[DigitalDocumentService][updateGenerationVI] iorderid: "+iorderid+",strLoginid: "+strLoginid);
        HashMap hshDataMap = new HashMap();
        try {
            hshDataMap=getSEJBDigitalDocumentRemote().updateGenerationVI(iorderid,strLoginid);
        }catch(Throwable t){
            manageCatch(hshDataMap, t);
        }
        logger.info(hshDataMap);
        return hshDataMap;
    }

    /**
     * Motivo: Actualiza el tipo de Firma a Manual en la generacion de documentos que no tenga VI.
     * <br>Realizado por: <a href="mailto:jefferson.vergara@soapros.pe">Jefferson Vergara</a>
     * <br>Fecha: 19/05/2017
     */
    public HashMap updateGenerationSIGN(int iorderid,int ireason, String strLoginid) {
        logger.info("[DigitalDocumentService][updateGenerationSIGN] iorderid: "+iorderid+", rasonId: "+ireason+" ,strLoginid: "+strLoginid);
        HashMap hshDataMap = new HashMap();
        try {
            hshDataMap=getSEJBDigitalDocumentRemote().updateGenerationSIGN(iorderid,ireason,strLoginid);
        }catch(Throwable t){
            manageCatch(hshDataMap, t);
        }
        logger.info(hshDataMap);
        return hshDataMap;
    }

    public HashMap sendEmailST(String orderId,String userLogin, String subCategoria, String divisionId, String transType){
        logger.info("sendEmailST orderId:"+orderId);
        HashMap hashMap=new HashMap();
        HashMap hashMapDocument=getDocumentGeneration(orderId);
        DocumentGenerationBean documentGenerationBean= (DocumentGenerationBean) hashMapDocument.get("documentGenerationBean");
        if(documentGenerationBean==null||documentGenerationBean.getGenerationStatus()==Constante.GENERATION_STATUS_INITIAL){
        hashMap.put("messageValidation",Constante.MESSAGE_ST_VALIDATE);
        return hashMap;
        }else {
            try {
                if(documentGenerationBean.getGenerationStatus()==1&&documentGenerationBean.getSignatureType()==Constante.SIGNATURE_TYPE_DIGITAL){
                Long lorderId = MiUtil.getLong(orderId);
                Long lsubCategoria = MiUtil.getLong(subCategoria);
                int idivision=MiUtil.getInt(divisionId);
                Long customerId = documentGenerationBean.getCustomerId();
                HashMap hashMapCustomer = getCustomerData(documentGenerationBean.getCustomerId());
                CustomerBean customerBean = (CustomerBean) hashMapCustomer.get("objCustomerBean");
                String documentNumber = customerBean.getSwRuc();
                HashMap hashMapChannel = getClientTypeFromCustomerId(customerId.intValue());
                int iclientType = MiUtil.getInt(hashMapChannel.get("strClientType"));
                HeaderRequestType header = getHeader(userLogin, orderId, Constante.SOURCE_ORDERS_ID);
                SendEmailRequestType emailRequestType = new SendEmailRequestType();
                emailRequestType.setTipoEjec(Constante.EXECUTION_TYPE_ASYNC);
                emailRequestType.setTipoTrans(Constante.TRX_TYPE_ALL_DOCUMENTS_BY_RULE);
                emailRequestType.setIdOrden(lorderId);
                HashMap hashMapGetInfoEmail = getInfoEmail(userLogin, documentGenerationBean.getTrxType(),Constante.POSTVENTA_TYPE_ST,transType, iclientType, lorderId, customerId,idivision,lsubCategoria,0);
                emailRequestType.setTipoTrans(Constante.TRX_TYPE_ALL_DOCUMENTS_BY_RULE);
                String xmlVariable = (String) hashMapGetInfoEmail.get("xmlVariable");
                if(xmlVariable==null){
                    hashMapGetInfoEmail = getInfoEmailPvt(documentGenerationBean.getTrxType(),Constante.POSTVENTA_TYPE_ST, iclientType, lorderId, customerId,idivision,lsubCategoria,0);
                    xmlVariable = (String) hashMapGetInfoEmail.get("xmlVariable");

                }
                String templateCode = (String) hashMapGetInfoEmail.get("templateCode");
                emailRequestType.setCodPlantillaCorreo(templateCode);
                xmlVariable = Base64.encode(MiUtil.getString(xmlVariable).getBytes());
                emailRequestType.setXmlVariable(xmlVariable);
                int statusEmail=sendEmail(emailRequestType, header);
                documentGenerationBean.setEmailStatus(statusEmail);
                documentGenerationBean.setModificationBy(userLogin);
                updateDocumentGeneration(documentGenerationBean);
                }
            } catch (Throwable t) {
                manageCatch(hashMap, t);
            }
        }
        logger.info(hashMap);
        logger.info("sendEmailST fin:"+orderId);
        return hashMap;
    }

    public int getNumberLinesPostpago(Long orderId, GetNumberActiveSuspendedLinesRequestType request, HeaderRequestType header)  throws Exception{
        logger.info("[DigitalDocumentService][getNumberLinesPostpago] orderId:"+orderId);
        int linespospago;
        HashMap hashMap= new HashMap();
        try {
            hashMap = getSEJBDigitalDocumentRemote().getNumberLines(orderId,request,header);
            GetNumberActiveSuspendedLinesResponseType getNumberLinesResponse = (GetNumberActiveSuspendedLinesResponseType) hashMap.get("result");
            linespospago = getNumberLinesResponse.getResponseData().getPostpago();
        }catch(Exception t){
            logger.error("Error in getNumberLinesPostpago",t);
            linespospago=0;
        }
        logger.info(hashMap);
        return linespospago;
    }

    public int getPvtType(long specificationId)  throws Exception{
        logger.info("[DigitalDocumentService][getPvtType] specificationId:"+specificationId);
        int type;
        HashMap hashMap= new HashMap();
        try {
            hashMap = getSEJBDigitalDocumentRemote().getPvtType(specificationId);
            type=MiUtil.getInt(hashMap.get("type"));

        }catch(Exception t){
            logger.error("Error in getPvtType",t);
            type=-1;
        }
        logger.info(hashMap);
        return type;
    }
    /**
     * Motivo: Obtener el tipo de Documento de un Cliente para compararlo con VIA
     * <br>Realizado por: <a href="mailto:jefferson.vergara@soapros.pe">Jefferson Vergara</a>
     * <br>Fecha: 20/06/2017
     */
    public HashMap get_ident_info_customer(long lcustomerid) {
        logger.info("[DigitalDocumentService][get_ident_info_customer] lcustomerid: "+lcustomerid);
        HashMap hshDataMap = new HashMap();
        try {
            hshDataMap=getSEJBDigitalDocumentRemote().get_ident_info_customer(lcustomerid);
        }catch(Throwable t){
            manageCatch(hshDataMap, t);
        }
        logger.info(hshDataMap);
        return hshDataMap;
    }

    /**
     * Motivo: Valida si se debe mostrar la seccion de la lista de documentos adjuntos
     * <br>Realizado por: Andre Calderon</a>
     * <br>Fecha: 22/08/2017
     */
    public HashMap getAttDocListFlag(int orderid, int incidentid) {
        logger.info("[DigitalDocumentService][getAttDocListFlag] orderid: "+orderid+", incidentid: "+incidentid);
        HashMap hshDataMap = new HashMap();
        try {
            hshDataMap=getSEJBDigitalDocumentRemote().getAttDocListFlag(orderid, incidentid);
        }catch(Throwable t){
            manageCatch(hshDataMap, t);
        }
        logger.info(hshDataMap);
        return hshDataMap;
    }
    /**
     * Motivo: Valida que se haya activado los equipos para ordenes de reparacion
     * <br>Realizado por: Johel Castillo</a>
     * <br>Fecha: 15/09/2017
     */
    public HashMap validateImeiLoan(String orderId){
        logger.info("validateImeiLoan:"+orderId);
        HashMap hashMap= new HashMap();
        try {
            hashMap=getSEJBDigitalDocumentRemote().validateImeiLoan(MiUtil.parseLong(orderId));
        }catch(Throwable t){
            manageCatch(hashMap, t);
        }
        logger.info(hashMap);
        return hashMap;
    }

}
