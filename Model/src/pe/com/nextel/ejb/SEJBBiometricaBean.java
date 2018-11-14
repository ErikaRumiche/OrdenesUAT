package pe.com.nextel.ejb;


import org.apache.commons.lang.StringUtils;
import pe.com.entel.integration.evaluationquestion.AuthenticationCustomerPT;
import pe.com.entel.integration.evaluationquestion.AuthenticationServices;
import pe.com.entel.integration.evaluationquestion.proxy.types.AuthenticationCustomerRequest;
import pe.com.entel.integration.evaluationquestion.proxy.types.AuthenticationCustomerResponse;
import pe.com.entel.integration.evaluationquestion.proxy.types.Option;
import pe.com.entel.integration.evaluationquestion.proxy.types.Question;
import pe.com.entel.integration.evaluationquestion.proxy.types.Quiz;

import pe.com.entel.integration.evaluationquestion.proxy.types.*;
import pe.com.nextel.bean.*;
import pe.com.nextel.dao.BiometricaDAO;
import pe.com.nextel.util.GenericObject;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SEJBBiometricaBean extends GenericObject implements SessionBean {
    private SessionContext _context;
    BiometricaDAO objBiometricaDao=null;

    public void ejbCreate() {

        objBiometricaDao= new BiometricaDAO();
    }

    public void setSessionContext(SessionContext context) throws EJBException {
        _context = context;
    }

    public void ejbRemove() throws EJBException {
        System.out.println("[SEJBBiometricaBean][ejbRemove()]");
    }

    public void ejbActivate() throws EJBException {
        System.out.println("[SEJBBiometricaBean][ejbActivate()]");
    }

    public void ejbPassivate() throws EJBException {
        System.out.println("[SEJBBiometricaBean][ejbPassivate()]");
    }



    public HashMap validOrderPendientes(int orderId)throws Exception{
        return  objBiometricaDao.validOrderPendientes(orderId);
    }

    public HashMap getValidActivation(int orderId, int specificationId, String login, String useCase) throws Exception {
        return  objBiometricaDao.getValidActivation(orderId, specificationId, login, useCase);
    }

    public String getDocumento(int order)throws Exception {
        return  objBiometricaDao.getDocumento(order);
    }

    public HashMap getListAction(int orderId)throws Exception{
        return   objBiometricaDao.getListAction(orderId);
    }

    public HashMap getListReason(Integer idAccion, String solution, int nbPend)throws Exception{
        return   objBiometricaDao.getListReason(idAccion, solution, nbPend);
    }

    public HashMap getResponseRule(Integer transactionID,String docnumber,String codeError,String restriction,
                                   String type,String source,Integer flagdni,Integer attempt,String ercAcepta)throws Exception{
        return  objBiometricaDao.getResponseRule(transactionID,docnumber,codeError,restriction,type,source,
                flagdni,attempt, ercAcepta);
    }

    public  HashMap  getCountVerifCustomer(int transactionID,String verificationType,String source,
                                           String docnumber)throws Exception{
        return  objBiometricaDao.getCountVerifCustomer(transactionID,verificationType,source,docnumber);
    }

    public   String  getAnularOrden(VerificationCustomerBean verificationCustomerBean,int action,int motive,String login) throws Exception{
        return  objBiometricaDao.getAnularOrden( verificationCustomerBean, action, motive, login);
    }

    public   String  getActionBiometric(String action,VerificationCustomerBean verificationCustomerBean,BiometricBean biometricBean,
                                        String login)throws Exception {
        return  objBiometricaDao.getActionBiometric(action,verificationCustomerBean,biometricBean,login);
    }

    public   HashMap getVerifPendiente(Integer orderid,String document, String cmbaction)throws Exception{
        return  objBiometricaDao.getVerifPendiente(orderid,document,cmbaction);
    }

    public   String  getAnularVerificacion(int order,String login,String athorizedUser,String source)throws Exception {
        return  objBiometricaDao.getAnularVerificacion(order,login,athorizedUser,source);
    }

    public HashMap getActionNoBiometrica(int order,int verifCustomer,int question,String av_authorizeduser,String av_action,String source, String login)throws Exception{
        return  objBiometricaDao.getActionNoBiometrica(order,verifCustomer,question,av_authorizeduser,av_action,source,login);
    }

    public HashMap getInsertUserNotConfig(int order, String typeVerificacion,String login){
        return  objBiometricaDao.getInsertUserNotConfig(order,typeVerificacion,login);
    }

    public HashMap getListSolution()throws SQLException{
        return objBiometricaDao.getListSolution();

    }

    public  HashMap getValidarCategoria(String categoria){
        return objBiometricaDao.getValidarCategoria(categoria);
    }

    public HashMap getValidaCategoriaSolucion(int order) throws SQLException{
        return objBiometricaDao.getValidaCategoriaSolucion(order);
    }



    public HashMap  getVerificateNoBiometrica(String user,String password ,String documentNumber,String application, String orderId ,String motive)throws SQLException, Exception {
        HashMap hshResultMap = new HashMap();
        System.out.println("[SEJBBiometricaBean.getVerificateNoBiometrica]");
        String resultado="";
        int minAceptado=0;
        AuthenticationServices authenticationServices = new AuthenticationServices();
        AuthenticationCustomerPT authenticationCustomerPT = authenticationServices.getAuthenticationCustomerPort();
        AuthenticationCustomerRequest request = new AuthenticationCustomerRequest();
        AuthenticationCustomerResponse response=null;
        System.out.println("[SEJBBiometricaBean.getVerificateNoBiometrica]");
        request.setUser(user);
        request.setPassword(password);
        request.setDocumentNumber(documentNumber);
        request.setApplication(application);
        request.setOrderId(orderId);
        request.setMotive(motive);
        PersonInfoBean personInfoBean =new PersonInfoBean ();
        String message="";
        String messageError="";
        System.out.println("[SEJBBiometricaBean.getVerificateNoBiometrica]: user:"+user+" passwor:"+password+" documentNumber:"+documentNumber+" application:"+
                application+" orderId:"+orderId+" motive:"+motive);


        try{
            response= authenticationCustomerPT.authenticationCustomer(request);

            System.out.println("[SEJBBiometricaBean.getVerificateNoBiometrica]: getQuestionaries:" + response.getQuestionaries().size());
            resultado=response.getResult();
            personInfoBean.setResult(resultado);
            message=response.getDescriptionResult();
            if(!resultado.equals("-1"))
            { personInfoBean.setVerificationid(response.getVerificationid());}

            System.out.println("[SEJBBiometricaBean.getVerificateNoBiometrica]: response" + response.getPersonInfo());
            System.out.println("[SEJBBiometricaBean.getVerificateNoBiometrica]: getQuestionaries:" + response.getQuestionaries().size());
            if (response.getQuestionaries().size()>0) {
                System.out.println("[SEJBBiometricaBean.getVerificateNoBiometrica]: cuestionario");
                minAceptado = response.getMinAccepted().intValue();

                FotoPersona fotoPersona = response.getPhotoPerson();

                //    personInfoBean.setDocumentNumber(datoPersona.getDocumentNumber());
                personInfoBean.setMinAccepted(response.getMinAccepted());
                personInfoBean.setAttempts(response.getAttempt());

                //    personInfoBean.setLengthPhoto(response.getPhotoPerson().getLengthPhoto());

                ArrayList<Quiz> lstQuestionaries = (ArrayList<Quiz>) response.getQuestionaries();
                List<QuizBean> lstQuizBean = new ArrayList<QuizBean>();

                for (Quiz questionario : lstQuestionaries) {
                    QuizBean quizBean = new QuizBean();
                    quizBean.setIdQuestionary(questionario.getIdQuestionary());
                    ArrayList<Question> lstQuestion = (ArrayList<Question>) questionario.getQuestion();
                    List<QuestionBean> lstQuestionBean = new ArrayList<QuestionBean>();
                    for (Question question : lstQuestion) {
                        QuestionBean questionBean = new QuestionBean();
                        questionBean.setQuestion(question.getQuestion());
                        questionBean.setIdoptionSuccess(question.getIdoptionSuccess());
                        questionBean.setIdquestion(question.getIdquestion());
                        ArrayList<Option> lstOption = (ArrayList<Option>) question.getOption();
                        List<OptionBean> lstOptionBean = new ArrayList<OptionBean>();
                        for (Option options : lstOption) {
                            OptionBean optionBean = new OptionBean();
                            optionBean.setOption(options.getOption());
                            optionBean.setIdoption(options.getIdoption());
                            lstOptionBean.add(optionBean);
                        }
                        questionBean.setLstOption(lstOptionBean);
                        lstQuestionBean.add(questionBean);
                    }
                    quizBean.setLstQuestion(lstQuestionBean);
                    lstQuizBean.add(quizBean);
                }
                personInfoBean.setLstQuizBean(lstQuizBean);
                personInfoBean.setPhoto(fotoPersona.getPhoto());
            } else {
                messageError = message + "ocuestionario vacio";

            }

            System.out.println("[SEJBBiometricaBean.getVerificateNoBiometrica]: resultado:" + resultado + "message:" + message);
        }catch(Exception e){
            messageError= e.getMessage();
            System.out.println("[SEJBBiometricaBean.getVerificateNoBiometrica]: Error la invocar el ServicioWeb=> "+e.getStackTrace());
        }

        System.out.print("[SEJBBiometricaBean.getVerificateNoBiometrica]: 1.messageError"+messageError);
        hshResultMap.put("messageError",messageError);
        hshResultMap.put("personInfoBean",personInfoBean);
        hshResultMap.put("message",message);
        hshResultMap.put("minAcep",minAceptado);
        hshResultMap.put("CustomerResponse",response);

        return  hshResultMap;
    }

    public HashMap getVerificationCustomer(long verificationId) throws SQLException{
        return objBiometricaDao.getVerificationCustomer(verificationId);
    }

/**
     * Motivo: Obtiene la lista de documentos que pueden ser considerados en una exoneración
     * Realizado por: LHUAPAYA
     * Fecha: 09/08/2016
     **/
    public HashMap getTypesDocumentsExoneration()throws SQLException,RemoteException,Exception{
        return objBiometricaDao.getTypesDocumentsExoneration();
    }

    /**
     * Motivo: Valida si el cliente puede pasar por una exoneración
     * Realizado por: LHUAPAYA
     * Fecha: 09/08/2016
     **/
    public int validateClientExonerate(int idOrder)throws SQLException,RemoteException,Exception {
        return objBiometricaDao.validateClientExonerate(idOrder);
    }

        /**
         * Motivo: Registra los datos de una exoneración
         * Realizado por: LHUAPAYA
         * Fecha: 09/08/2016
         **/
        public HashMap registerExonerate(int orderid,String authorizedUser, String login, String numDoc, int valTypeDoc)throws SQLException,RemoteException,Exception{
            return objBiometricaDao.registerExonerate(orderid,authorizedUser,login,numDoc,valTypeDoc);
        }

        /**
         * Motivo: Devuelve el ultimo representante legal de una empresa
         * Realizado por: LHUAPAYA
         * Fecha: 09/08/2016
         **/
        public HashMap getLastLegalRepresentative(int orderId)throws SQLException,RemoteException,Exception {
            return objBiometricaDao.getLastLegalRepresentative(orderId);
        }


    /**
     * Motivo: Obtiene las acciones en base al tipo de documento
     * <br>IOZ01092016I</a>
     * <br>Fecha: 12/09/2016
     * @return    HashMap
     * */
    public HashMap getListAccion(String  av_type_document )throws Exception{
        return   objBiometricaDao.getListAccion(av_type_document);
    }
    /**
     * Motivo: Obtiene los motivos en base a la accion
     * <br>IOZ01092016I</a>
     * <br>Fecha: 12/09/2016
     * @return    HashMap
     * */
    public HashMap getListReasonAislada(String  action )throws Exception{
        return   objBiometricaDao.getListReasonAislada(action);
    }

    /**
     * Motivo: inserta el registrar
     * <br>IOZ15092016I</a>
     * <br>Fecha: 12/09/2016
     * @return    String
     * */
    public   String  insertRegistrar(String login, String tipoDoc, String numDoc, String accion, String customerId, String sourcev)throws Exception {
        return  objBiometricaDao.insertRegistrar(login, tipoDoc, numDoc, accion, customerId, sourcev);
    }

    /**
     * Motivo: inserta el registrar
     * <br>PORTEGA</a>
     * <br>Fecha: 05/10/2016
     * @return    String
     * */
    public   String  insViaCustomer(String verificationType, String useCase, String motivo, String authorizer, String transactionType,
                                    Integer statusVerification, String docNumber, String phoneNumber, String docType, Integer customerId,
                                    Integer verificationId)throws Exception {
        return  objBiometricaDao.insViaCustomer(verificationType, useCase, motivo, authorizer, transactionType,
                statusVerification, docNumber, phoneNumber, docType, customerId, verificationId);
    }

    /**
     * Motivo: Consultar verificaciones aisladas
     * <br>RO
     * <br>Fecha: 20/09/2016
     * @return    HashMap
     * */
    public HashMap listarVerificaciones(String tipoDocumento, String  numeroDocumento, String fechaInicio, String fechaFin){
        return objBiometricaDao.listarVerifaciones(tipoDocumento, numeroDocumento, fechaInicio, fechaFin);
    }

    /**
     * Motivo: Consultar tipos documentos para la consulta de verificaciones
     * <br>RO
     * <br>Fecha: 20/09/2016
     * @return    HashMap
     * */
    public HashMap listarTiposDocumentoVerificaciones(){
        return objBiometricaDao.listarTiposDocumentoVerificaciones();
    }

    /**
     * Motivo: Obtiene el detalle de una verificacion de identidad aislada
     * <br>RO
     * <br>Fecha: 22/09/2016
     * @return    HashMap
     * */
    public HashMap obtenerDetalleVia(Long identificador){
        return objBiometricaDao.obtenerDetalleVia(identificador);
    }

    public HashMap getResponseRuleVIA(String docnumber,String codeError,String restriction,
                                      String type,String source,Integer flagdni,Integer attempt,String ercAcepta,int customerId)throws Exception{
        return  objBiometricaDao.getResponseRuleVIA(docnumber, codeError, restriction, type, source,
                flagdni, attempt, ercAcepta,customerId);
    }

    public   String  getActionBiometricVIA(String action,VerificationCustomerBean verificationCustomerBean,BiometricBean biometricBean,
                                           String login, int customerId)throws Exception {
        return  objBiometricaDao.getActionBiometricVIA(action, verificationCustomerBean, biometricBean, login, customerId);
    }

    /**
     * Motivo: Permite obtener un listado de valores para una configuracion para biometrica fase 2
     * <br>Realizado por: <a href="mailto:paolo.ortega@hpe.com">Paolo Ortega</a>
     * <br>Fecha: 10/10/2016
     *
     * @return		HashMap
     */
    public HashMap getViaConfigTypeDocList() throws Exception {
        return objBiometricaDao.getViaConfigTypeDocList();
    }
}