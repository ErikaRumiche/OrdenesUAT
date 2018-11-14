package pe.com.nextel.ejb;

import pe.com.entel.integration.evaluationquestion.proxy.types.AuthenticationCustomerResponse;
import pe.com.nextel.bean.BiometricBean;
import pe.com.nextel.bean.VerificationCustomerBean;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.HashMap;

public interface SEJBBiometricaRemote extends EJBObject {

    HashMap  validOrderPendientes(int idOrder) throws Exception ,SQLException, RemoteException;
    HashMap  getValidActivation(int orderId, int specificationId, String login, String useCase) throws Exception ,SQLException, RemoteException;
    String   getDocumento(int order)throws Exception ,SQLException, RemoteException;
    HashMap  getListAction(int orderId)throws Exception ,SQLException, RemoteException;
    HashMap  getListReason(Integer idAccion, String solution, int nbPend) throws Exception ,SQLException, RemoteException;
    HashMap  getResponseRule(Integer transactionID,String docnumber,String codeError,String restriction,
                             String type,String source,Integer flagdni,Integer attempt,String ercAcepta)throws Exception ,SQLException, RemoteException;
    HashMap  getCountVerifCustomer(int transactionID, String verificationType, String source,
                                   String docnumber)throws Exception ,SQLException, RemoteException;

    String   getAnularOrden(VerificationCustomerBean verificationCustomerBean,int action,int motive,String login)throws Exception ,SQLException, RemoteException;
    String  getActionBiometric(String action,VerificationCustomerBean verificationCustomerBean,BiometricBean biometricBean,
                               String login)throws Exception ,SQLException, RemoteException;

    HashMap getVerifPendiente(Integer orderid,String document, String cmbaction)throws Exception ,SQLException, RemoteException;

    String  getAnularVerificacion(int order,String login,String athorizedUser,String source)throws Exception ,SQLException, RemoteException;

    HashMap getActionNoBiometrica(int order,int verifCustomer,int question,String av_authorizeduser,String av_action,String source, String login)throws Exception ,SQLException, RemoteException;

    HashMap  getVerificateNoBiometrica(String user,String password ,String documentNumber,String application, String orderId ,String motive)throws Exception ,SQLException, RemoteException;

    HashMap  getInsertUserNotConfig(int order, String typeVerificacion,String login)throws Exception ,SQLException, RemoteException;

    HashMap getListSolution()throws Exception ,SQLException, RemoteException;

    HashMap getValidarCategoria(String categoria)throws Exception ,SQLException, RemoteException;

    HashMap getValidaCategoriaSolucion(int order) throws Exception ,SQLException, RemoteException;

    HashMap getVerificationCustomer(long verificationId) throws Exception ,SQLException, RemoteException;

    HashMap getTypesDocumentsExoneration()throws SQLException,RemoteException,Exception;

    int validateClientExonerate(int idOrder)throws SQLException,RemoteException,Exception;

    HashMap registerExonerate(int orderid, String authorizedUser, String login, String numDoc, int valTypeDoc)throws SQLException,RemoteException,Exception;

    HashMap getLastLegalRepresentative(int orderId)throws SQLException,RemoteException,Exception;

    HashMap  getListReasonAislada(String action) throws Exception ,SQLException, RemoteException; //IOZ12092016I

    HashMap  getListAccion(String av_type_document) throws Exception ,SQLException, RemoteException; //IOZ12092016I

    String  insertRegistrar(String login, String tipoDoc, String numDoc, String accion, String customerId, String sourcev)throws Exception ,SQLException, RemoteException; //IOZ15092016I

    String  insViaCustomer(String verificationType, String useCase, String motivo, String authorizer, String transactionType, Integer statusVerification, String docNumber, String phoneNumber, String docType, Integer customerId,
                           Integer verificationId)throws Exception; //PORTEGA

    HashMap listarVerificaciones(String tipoDocumento, String  numeroDocumento, String fechaInicio, String fechaFin) throws RemoteException, SQLException, Exception;

    HashMap listarTiposDocumentoVerificaciones() throws RemoteException, SQLException, Exception;

    HashMap obtenerDetalleVia(Long identificador) throws RemoteException, SQLException, Exception;

    HashMap  getResponseRuleVIA(String docnumber,String codeError,String restriction,
                                String type,String source,Integer flagdni,Integer attempt,String ercAcepta, int customerId)throws Exception ,SQLException, RemoteException;

    String  getActionBiometricVIA(String action,VerificationCustomerBean verificationCustomerBean,BiometricBean biometricBean,
                                  String login,int customerId)throws Exception ,SQLException, RemoteException;

    HashMap getViaConfigTypeDocList() throws Exception;
}
