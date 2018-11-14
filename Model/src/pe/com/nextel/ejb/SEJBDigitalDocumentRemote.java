package pe.com.nextel.ejb;

import pe.com.entel.esb.data.generico.entelgenericheader.v2.HeaderRequestType;
import pe.com.entel.esb.message.contractmanage.getnumberactivesuspendedlines.v1.GetNumberActiveSuspendedLinesRequestType;
import pe.com.entel.esb.message.documentmanage.createdocumentbyrule.v1.CreateDocumentByRuleRequestType;
import pe.com.entel.esb.message.documentmanage.getdocument.v1.GetDocumentRequestType;
import pe.com.entel.esb.message.documentmanage.getdocumentlist.v1.GetDocumentListRequestType;
import pe.com.entel.esb.message.documentmanage.getrules.v1.GetRulesRequestType;
import pe.com.entel.esb.message.documentmanage.sendemail.v1.SendEmailRequestType;
import pe.com.entel.integracion.document.schema.CreateDocumentRequestType;
import pe.com.nextel.bean.DocAssigneeBean;
import pe.com.nextel.bean.DocumentGenerationBean;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * Created by JCASTILLO on 04/04/2017.
 * [PRY-0787]
 */
public interface SEJBDigitalDocumentRemote extends EJBObject {

    public HashMap saveDocumentGeneration(DocumentGenerationBean documentGenerationBean) throws Exception;
    public HashMap updateDocumentGeneration(DocumentGenerationBean documentGenerationBean) throws Exception;
    public HashMap getDocumentGeneration(Long orderId) throws Exception;
    public HashMap getEmail(Long customerId, Long siteId) throws Exception;
    public HashMap validateVIDD(Long buildingId, int typeId, Long divisionId, Long specificationId,  int channelId,Long customerId) throws Exception;
    public HashMap validateVIDD(Long buildingId, int typeId, Long divisionId, Long specificationId,  int channelId,Long customerId,String generatorType) throws Exception;
    public HashMap getDigitalDocumentsToGenerate(Integer source, Long trxId, GetRulesRequestType request, HeaderRequestType header) throws Exception;
    public HashMap generateDigitalDocuments(CreateDocumentByRuleRequestType request, HeaderRequestType header) throws Exception;
    public HashMap sendEmail(SendEmailRequestType request, HeaderRequestType header) throws Exception;
    public HashMap getValidaCategoriaSolucionDigitalization(int order) throws Exception;
    public HashMap getValueNpDigitalConfig(String domain,int origen,int typetrx, int channel, int section) throws Exception;
    public String getClientTypeFromCustomerId(int iCustomerid)throws RemoteException, Exception;
    public HashMap getSolNumber(int source,Long specificationId, int channel, int division)  throws Exception;
    public HashMap saveDocAssignee(DocAssigneeBean assigneeBean) throws Exception;
    public HashMap updateDocAssignee(DocAssigneeBean assigneeBean) throws Exception;
    public HashMap getDocAssignee(Long orderId) throws Exception;
    public HashMap getFlagSpecialCase(Long iOrderid)  throws Exception;
    public HashMap sendAttachedDocuments(Integer source, Long trxId, CreateDocumentRequestType createDocumentRequestType, pe.com.entel.integracion.document.schema.HeaderRequestType header) throws Exception;
    public HashMap getListDigitalDocuments(GetDocumentListRequestType request, HeaderRequestType header) throws Exception;
    public HashMap verDocumentoDigital(GetDocumentRequestType request, HeaderRequestType header) throws RemoteException, Exception;
    public HashMap getvalidateUploadedFiles(int iFileTypeId, int iFileSize, String strContentType) throws  Exception;
    public HashMap getAttachSectionStatus(int iorigen, int itypetrx, int ibuidingid)throws Exception;
    public HashMap validateVIATechnicalService(String orderId, int customerId, String strLogin, int iChkAssignee, String strDocNumAssignee, String cmbDocTypeAssignee)throws Exception;
    public HashMap updateGenerationVI(int iorderid, String strLoginid) throws Exception;
    public HashMap updateGenerationSIGN(int iorderid,int ireason, String strLoginid)throws Exception;
    public HashMap getNumberLines(Long trxId, GetNumberActiveSuspendedLinesRequestType request, HeaderRequestType header) throws Exception;
    public HashMap getCustomerData(long lCustomerid)throws Exception;
    public HashMap updateOrderCode(String sOrdercode, long iOrderid) throws Exception;
    public HashMap getSignatureReason(int iorigen, int itypetrx)throws Exception;
    public HashMap getDocumentGenerationInc(Long incidentId)throws Exception;
    public HashMap getInfoEmail(String user, int trxType,int pvtType,String transType,int channel,long orderId,long customerId,int divisionId,long specificationId,int lines)  throws Exception;
    public HashMap getInfoEmailPvt(int trxType,int pvtType,int channel,long orderId,long customerId,int divisionId,long specificationId,int lines)  throws Exception;
    public HashMap getPvtType(long specificationId)  throws Exception;
    public HashMap get_ident_info_customer(long lcustomerId)  throws Exception;
    public HashMap getAttDocListFlag(int orderid, int incidentid)throws Exception;
    public HashMap validateImeiLoan(long orderId)throws Exception;
}
