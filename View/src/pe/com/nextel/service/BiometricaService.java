package pe.com.nextel.service;

import org.apache.commons.lang.StringUtils;
import pe.com.nextel.bean.BiometricBean;
import pe.com.nextel.bean.PersonInfoBean;
import pe.com.nextel.bean.QuizBean;
import pe.com.nextel.bean.VerificationCustomerBean;
import pe.com.nextel.ejb.SEJBBiometricaRemote;
import pe.com.nextel.ejb.SEJBBiometricaRemoteHome;
import pe.com.nextel.util.MiUtil;
import pe.com.nextel.util.Constante;

import javax.naming.Context;
import javax.rmi.PortableRemoteObject;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;

public class BiometricaService {

    public static SEJBBiometricaRemote getSEJBBiometricaRemote() {
        try{
            final Context context = MiUtil.getInitialContext();
            final SEJBBiometricaRemoteHome sejbBiometricaNewRemoteHome  =
                    (SEJBBiometricaRemoteHome) PortableRemoteObject.narrow(context.lookup("SEJBBiometrica"), SEJBBiometricaRemoteHome.class);
            SEJBBiometricaRemote sejbBiometricaRemote;
            sejbBiometricaRemote = sejbBiometricaNewRemoteHome.create();

            return sejbBiometricaRemote;
        }catch(Exception ex) {
            System.out.println("Exception : [BiometricaService][getSEJBBiometricaRemote]["+ex.getMessage()+"]");
            return null;
        }

    }
    Constante constante= new Constante();

    public HashMap validOrderPendientes(int idOrder){
        HashMap objHashMap = new HashMap();
        String  strMessage = new String();

        try{
            objHashMap=getSEJBBiometricaRemote().validOrderPendientes(idOrder);
        }
        catch(SQLException e){
            strMessage  = "[SQLException][BiometricaService][validOrderPendientes][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println("strMessage:"+strMessage);
            objHashMap.put("strMessage",strMessage);
        }
        catch(RemoteException e){
            strMessage  = "[RemoteException][BiometricaService][validOrderPendientes][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println("strMessage:"+strMessage);
            objHashMap.put("strMessage",strMessage);
        }
        catch(Exception e){
            strMessage  = "[Exception][BiometricaService][validOrderPendientes][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println("strMessage:"+strMessage);
            objHashMap.put("strMessage",strMessage);
        }

        return objHashMap;
    }

    public HashMap getValidActivation(int orderId, int specificationId, String login, String useCase){
        int resultado=0;
        HashMap objHashMap = new HashMap();
        String  strMessage = "";

        try{
            objHashMap=getSEJBBiometricaRemote().getValidActivation(orderId, specificationId, login, useCase);

        }
        catch(SQLException e){
            strMessage  = "[SQLException][BiometricaService][getValidActivation][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println("strMessage:"+strMessage);
        }
        catch(RemoteException e){
            strMessage  = "[RemoteException][BiometricaService][getValidActivation][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println("strMessage:"+strMessage);
        }
        catch(Exception e){
            strMessage  = "[Exception][BiometricaService][getValidActivation][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println("strMessage:"+strMessage);
        }

        System.out.println(strMessage+"objHashMap"+objHashMap);
        return objHashMap;

    }

    public String getDocumento(int dni){
        String xdni="";
        String  strMessage = "";
        String documento="";

        try{
            documento=getSEJBBiometricaRemote().getDocumento(dni);
        }
        catch(SQLException e){
            strMessage  = "[SQLException][BiometricaService][getDocumento][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println("strMessage:"+strMessage);
        }
        catch(RemoteException e){
            strMessage  = "[RemoteException][BiometricaService][getDocumento][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println("strMessage:"+strMessage);
        }
        catch(Exception e){
            strMessage  = "[Exception][BiometricaService][getDocumento][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println("strMessage:"+strMessage);
        }
        System.out.println("[getDocumento]strMessage:"+strMessage+"resultado:"+documento);
        return documento;
    }

    public HashMap getListAction(int orderId){
        HashMap objHashMap= new HashMap();
        String strMessage="";
        try{
            objHashMap=getSEJBBiometricaRemote().getListAction(orderId);
        }
        catch(SQLException e){
            strMessage  = "[SQLException][BiometricaService][getListAction][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            System.out.println("strMessage:"+strMessage);
        }
        catch(RemoteException e){
            strMessage  = "[RemoteException][BiometricaService][getListAction][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            System.out.println("strMessage:"+strMessage);
        }
        catch(Exception e){
            strMessage  = "[Exception][BiometricaService][getListAction][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            System.out.println("[getListAction]strMessage:"+strMessage);
        }
        return objHashMap;
    }

    public HashMap getListReason(Integer idAccion, String solution, int nbPend){
        HashMap objHashMap= new HashMap();
        String strMessage="";
        try{
            objHashMap=getSEJBBiometricaRemote().getListReason(idAccion, solution, nbPend);
        }
        catch(SQLException e){
            strMessage  = "[SQLException][BiometricaService][getListReason][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            System.out.println("strMessage:"+strMessage);
        }
        catch(RemoteException e){
            strMessage  = "[RemoteException][BiometricaService][getListReason][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            System.out.println("strMessage:"+strMessage);
        }
        catch(Exception e){
            strMessage  = "[Exception][BiometricaService][getListReason][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            System.out.println("[getListReason]strMessage:"+strMessage);
        }
        return objHashMap;
    }

    public HashMap getResponseRule(Integer transactionID,String docnumber,String codeError,String restriction,
                                   String type,String source,Integer flagdni,Integer attempt,String ercAcepta){
        HashMap objHashMap= new HashMap();
        String strMessage="";
        try{
            objHashMap=getSEJBBiometricaRemote().getResponseRule(transactionID,docnumber,codeError,restriction,
                    type,source,flagdni,attempt,ercAcepta);
        }
        catch(SQLException e){
            strMessage  = "[SQLException][BiometricaService][getResponseRule][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            System.out.println("strMessage:"+strMessage);
        }
        catch(RemoteException e){
            strMessage  = "[RemoteException][BiometricaService][getResponseRule][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            System.out.println("strMessage:"+strMessage);
        }
        catch(Exception e){
            strMessage  = "[Exception][BiometricaService][getResponseRule][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            System.out.println("[getResponseRule]strMessage:"+strMessage);
        }

        return  objHashMap;
    }


    public  HashMap  getCountVerifCustomer(int transactionID,String verificationType,String source,
                                           String docnumber){
        HashMap objHashMap= new HashMap();
        String strMessage="";
        try{
            objHashMap=getSEJBBiometricaRemote().getCountVerifCustomer(transactionID, verificationType, source, docnumber);
        }
        catch(SQLException e){
            strMessage  = "[SQLException][BiometricaService][getCountVerifCustomer][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            System.out.println("strMessage:"+strMessage);
        }
        catch(RemoteException e){
            strMessage  = "[RemoteException][BiometricaService][getCountVerifCustomer][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            System.out.println("strMessage:"+strMessage);
        }
        catch(Exception e){
            strMessage  = "[Exception][BiometricaService][getCountVerifCustomer][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            System.out.println("strMessage:"+strMessage);
        }

        return  objHashMap;
    }

    public String getAnularOrden(VerificationCustomerBean verificationCustomerBean,int action,int motive,String login){
        String  strMessage = "";
        try{
            strMessage=getSEJBBiometricaRemote().getAnularOrden(verificationCustomerBean, action, motive, login);
        }
        catch(SQLException e){
            strMessage  = "[SQLException][BiometricaService][getAnularOrden][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println("strMessage:"+strMessage);
            return strMessage;
        }
        catch(RemoteException e){
            strMessage  = "[RemoteException][BiometricaService][getAnularOrden][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println("strMessage:"+strMessage);
            return strMessage;
        }
        catch(Exception e){
            strMessage  = "[Exception][BiometricaService][getAnularOrden][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println("strMessage:"+strMessage);
            return strMessage;
        }
        System.out.println("[getAnularOrden]strMessage:"+strMessage);
        return strMessage;
    }

    public   String  getActionBiometric(String action,VerificationCustomerBean verificationCustomerBean,BiometricBean biometricBean,
                                        String login){
        String strMessage="";

        try{
            strMessage=getSEJBBiometricaRemote().getActionBiometric(action, verificationCustomerBean, biometricBean, login);

        }
        catch(SQLException e){
            strMessage  = "[SQLException][BiometricaService][getActionBiometric][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println("strMessage:"+strMessage);
        }
        catch(RemoteException e){
            strMessage  = "[RemoteException][BiometricaService][getActionBiometric][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println("strMessage:"+strMessage);
        }
        catch(Exception e){
            strMessage  = "[Exception][BiometricaService][getActionBiometric][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println("strMessage:"+strMessage);
        }

        return  strMessage;
    }


    public   HashMap getVerifPendiente(Integer orderid,String document, String cmbaction){
        HashMap objHashMap= new HashMap();
        String strMessage="";
        try{
            objHashMap=getSEJBBiometricaRemote().getVerifPendiente(orderid, document, cmbaction);
        }
        catch(SQLException e){
            strMessage  = "[SQLException][BiometricaService][getVerifPendiente][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessageError",strMessage);
            System.out.println("strMessageError:"+strMessage);
        }
        catch(RemoteException e){
            strMessage  = "[RemoteException][BiometricaService][getVerifPendiente][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessageError",strMessage);
            System.out.println("strMessageError:"+strMessage);
        }
        catch(Exception e){
            strMessage  = "[Exception][BiometricaService][getVerifPendiente][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessageError",strMessage);
            System.out.println("strMessageError:"+strMessage);
        }

        return  objHashMap;





    }

    public   String  getAnularVerificacion(int order,String login,String athorizedUser,String source){
        String strMessage="";

        try{
            strMessage=getSEJBBiometricaRemote().getAnularVerificacion(order,login,athorizedUser,source);

        }
        catch(SQLException e){
            strMessage  = "[SQLException][BiometricaService][getAnularVerificacion][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println("strMessage:"+strMessage);
        }
        catch(RemoteException e){
            strMessage  = "[RemoteException][BiometricaService][getAnularVerificacion][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println("strMessage:"+strMessage);
        }
        catch(Exception e){
            strMessage  = "[Exception][BiometricaService][getAnularVerificacion][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println("strMessage:"+strMessage);
        }

        return  strMessage;
    }


    public   HashMap getVerificateNoBiometrica(String user,String password ,String documentNumber,String application, String orderId ,String motive){
        HashMap objHashMap= new HashMap();
        String strMessage="";
        try{
            objHashMap=getSEJBBiometricaRemote().getVerificateNoBiometrica(user, password, documentNumber,application,orderId,motive);
        }
        catch(SQLException e){
            strMessage  = "[SQLException][getVerificateNoBiometrica][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessageError",strMessage);
            System.out.println("strMessageError:"+strMessage);
        }
        catch(RemoteException e){
            strMessage  = "[RemoteException][getVerificateNoBiometrica][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessageError",strMessage);
            System.out.println("strMessageError:"+strMessage);
        }
        catch(Exception e){
            strMessage  = "[Exception][getVerificateNoBiometrica][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessageError",strMessage);
            System.out.println("strMessageError:"+strMessage);
        }

        return  objHashMap;
    }


    public HashMap getActionNoBiometrica(int order,int verifCustomer,int question,String av_authorizeduser,String av_action,String source, String login){
        HashMap objHashMap= new HashMap();
        String strMessage="";
        try{
            objHashMap=getSEJBBiometricaRemote().getActionNoBiometrica(order, verifCustomer, question, av_authorizeduser, av_action,source,login);
        }
        catch(SQLException e){
            strMessage  = "[SQLException][getActionNoBiometrica][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessageError",strMessage);
            System.out.println("strMessageError:"+strMessage);
        }
        catch(RemoteException e){
            strMessage  = "[RemoteException][getActionNoBiometrica][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessageError",strMessage);
            System.out.println("strMessageError:"+strMessage);
        }
        catch(Exception e){
            strMessage  = "[Exception][getActionNoBiometrica][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessageError",strMessage);
            System.out.println("strMessageError:"+strMessage);
        }

        return  objHashMap;

    }

    public HashMap getInsertUserNotConfig(int order,String typeVerificacion,String login){
        HashMap objHashMap= new HashMap();
        String strMessage="";
        try{
            objHashMap=getSEJBBiometricaRemote().getInsertUserNotConfig(order, typeVerificacion,login);
        }
        catch(SQLException e){
            strMessage  = "[SQLException][getInsertUserNotConfig][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessageError",strMessage);
            System.out.println("strMessageError:"+strMessage);
        }
        catch(RemoteException e){
            strMessage  = "[RemoteException][getInsertUserNotConfig][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessageError",strMessage);
            System.out.println("strMessageError:"+strMessage);
        }
        catch(Exception e){
            strMessage  = "[Exception][getInsertUserNotConfig][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessageError",strMessage);
            System.out.println("strMessageError:"+strMessage);
        }

        return  objHashMap;

    }

    public int validarSoluciones(String idsSoluciones){
        HashMap objHashMap= new HashMap();
        int resultado=0;
        String[] ids=idsSoluciones.split(",");
        String solucion="";
        String strMessage="";
        List<String> list= new ArrayList<String>();

        try{
            objHashMap=getSEJBBiometricaRemote().getListSolution();
            System.out.println(objHashMap);

            list=(ArrayList<String>) objHashMap.get("objArrayList");
            strMessage=(String)objHashMap.get("strMessage");

            for (int i=0; i<ids.length;i++){
                solucion=ids[i];
                for(int y=0;y<list.size();y++) {

                    if (solucion.equals(list.get(y))) {
                        resultado++;
                    }
                }
            }
        }
        catch(SQLException e){
            strMessage  = "[SQLException][validarSoluciones][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessageError",strMessage);
            System.out.println("strMessageError:"+strMessage);
        }
        catch(RemoteException e){
            strMessage  = "[RemoteException][validarSoluciones][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessageError",strMessage);
            System.out.println("strMessageError:"+strMessage);
        }
        catch(Exception e){
            strMessage  = "[Exception][validarSoluciones][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessageError",strMessage);
            System.out.println("strMessageError:"+strMessage);
        }

        return    resultado;
    }



    public HashMap  getValidarCategoria(String categoria){
        HashMap objHashMap = new HashMap();
        String strMessage = "";

        try{
            objHashMap=getSEJBBiometricaRemote().getValidarCategoria(categoria);
            System.out.println(objHashMap);
        }
        catch(SQLException e){
            strMessage  = "[SQLException][validarSoluciones][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessageError",strMessage);
            System.out.println("strMessageError:"+strMessage);
        }
        catch(RemoteException e){
            strMessage  = "[RemoteException][validarSoluciones][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessageError",strMessage);
            System.out.println("strMessageError:"+strMessage);
        }
        catch(Exception e){
            strMessage  = "[Exception][validarSoluciones][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessageError",strMessage);
            System.out.println("strMessageError:"+strMessage);
        }

        return  objHashMap;
    }


    public HashMap  getValidaCategoriaSolucion(int order){
        HashMap objHashMap = new HashMap();
        String strMessage = "";

        try{
            objHashMap=getSEJBBiometricaRemote().getValidaCategoriaSolucion(order);

        }
        catch(SQLException e){
            strMessage  = "[SQLException][getValidaCategoriaSolucion][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessageError",strMessage);
            System.out.println("strMessageError:"+strMessage);
        }
        catch(RemoteException e){
            strMessage  = "[RemoteException][getValidaCategoriaSolucion][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessageError",strMessage);
            System.out.println("strMessageError:"+strMessage);
        }
        catch(Exception e){
            strMessage  = "[Exception][getValidaCategoriaSolucion][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessageError",strMessage);
            System.out.println("strMessageError:"+strMessage);
        }

        return  objHashMap;
    }


    public HashMap getVerificationCustomer(long verificationId) {
        HashMap objHashMap = new HashMap();
        String strMessage = "";

        try {
            objHashMap = getSEJBBiometricaRemote().getVerificationCustomer(verificationId);

        } catch (Exception e) {
            strMessage = "[Exception][getVerificationCustomer][" + e.getClass() + " " + e.getMessage() + "]";
            objHashMap.put("strMessageError", strMessage);
            System.out.println("strMessageError:" + strMessage);
        }

        return objHashMap;
    }

    public HashMap getTypesDocumentsExoneration(){
        HashMap objHashMap = new HashMap();
        String strMessage = "";
        try{
            objHashMap = getSEJBBiometricaRemote().getTypesDocumentsExoneration();
        }catch (SQLException s) {
            strMessage = "[Exception BD][getTypesDocumentsExoneration][" + s.getClass() + " " + s.getMessage() + "]";
            objHashMap.put("strMessageError",strMessage);
            System.out.println("SQLException: " + strMessage);
        }catch (RemoteException r){
            strMessage = "[Exception EJB][getTypesDocumentsExoneration][" + r.getClass() + " " + r.getMessage() + "]";
            objHashMap.put("strMessageError", strMessage);
            System.out.println("RemoteException: " + strMessage);
        }catch (Exception e){
            strMessage = "[Exception OTHERS][getTypesDocumentsExoneration][" + e.getClass() + " " + e.getMessage() + "]";
            objHashMap.put("strMessageError", strMessage);
            System.out.println("Exception: " + strMessage);
        }

        return objHashMap;
    }

    public int validateClientExonerate(int idOrder){
        HashMap objHashMap = new HashMap();
        String strMessage = "";
        int intNumberValidate = 0;
        try{
            intNumberValidate = getSEJBBiometricaRemote().validateClientExonerate(idOrder);
        }catch (SQLException s) {
            strMessage = "[Exception BD][validateClientExonerate][" + s.getClass() + " " + s.getMessage() + "]";
            objHashMap.put("strMessageError",strMessage);
            System.out.println("SQLException: " + strMessage);
        }catch (RemoteException r){
            strMessage = "[Exception EJB][validateClientExonerate][" + r.getClass() + " " + r.getMessage() + "]";
            objHashMap.put("strMessageError", strMessage);
            System.out.println("RemoteException: " + strMessage);
        }catch (Exception e){
            strMessage = "[Exception OTHERS][validateClientExonerate][" + e.getClass() + " " + e.getMessage() + "]";
            objHashMap.put("strMessageError", strMessage);
            System.out.println("Exception: " + strMessage);
        }

        return intNumberValidate;
    }

    public HashMap registerExonerate(int orderid,String authorizedUser, String login, String numDoc, int valTypeDoc){
        HashMap objHashMap = new HashMap();
        String strMessage = "";
        try{
            objHashMap = getSEJBBiometricaRemote().registerExonerate(orderid, authorizedUser, login, numDoc, valTypeDoc);
        }catch (SQLException s) {
            strMessage = "[Exception BD][registerExonerate][" + s.getClass() + " " + s.getMessage() + "]";
            objHashMap.put("strMessageError",strMessage);
            System.out.println("SQLException: " + strMessage);
        }catch (RemoteException r){
            strMessage = "[Exception EJB][registerExonerate][" + r.getClass() + " " + r.getMessage() + "]";
            objHashMap.put("strMessageError", strMessage);
            System.out.println("RemoteException: " + strMessage);
        }catch (Exception e){
            strMessage = "[Exception OTHERS][registerExonerate][" + e.getClass() + " " + e.getMessage() + "]";
            objHashMap.put("strMessageError", strMessage);
            System.out.println("Exception: " + strMessage);
        }

        return objHashMap;
    }


    public HashMap getLastLegalRepresentative(int orderId){
        HashMap objHashMap = new HashMap();
        String strMessage = "";
        try{
            objHashMap = getSEJBBiometricaRemote().getLastLegalRepresentative(orderId);
        }catch (SQLException s) {
            strMessage = "[Exception BD][getLastLegalRepresentative][" + s.getClass() + " " + s.getMessage() + "]";
            objHashMap.put("strMessageError",strMessage);
            System.out.println("SQLException: " + strMessage);
        }catch (RemoteException r){
            strMessage = "[Exception EJB][getLastLegalRepresentative][" + r.getClass() + " " + r.getMessage() + "]";
            objHashMap.put("strMessageError", strMessage);
            System.out.println("RemoteException: " + strMessage);
        }catch (Exception e){
            strMessage = "[Exception OTHERS][getLastLegalRepresentative][" + e.getClass() + " " + e.getMessage() + "]";
            objHashMap.put("strMessageError", strMessage);
            System.out.println("Exception: " + strMessage);
        }

        return objHashMap;
    }

    /**
     * Motivo: Obtiene los motivos en base a la accion
     * <br>IOZ01092016I</a>
     * <br>Fecha: 12/09/2016
     * @return    HashMap
     * */
    public HashMap getListReasonAislada(String action){
        HashMap objHashMap= new HashMap();
        String strMessage="";
        try{
            objHashMap=getSEJBBiometricaRemote().getListReasonAislada(action);
        }
        catch(SQLException e){
            strMessage  = "[SQLException][BiometricaService][getListReasonAislada][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            System.out.println("strMessage:"+strMessage);
        }
        catch(RemoteException e){
            strMessage  = "[RemoteException][BiometricaService][getListReasonAislada][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            System.out.println("strMessage:"+strMessage);
        }
        catch(Exception e){
            strMessage  = "[Exception][BiometricaService][getListReasonAislada][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            System.out.println("[getListReasonAislada]strMessage:"+strMessage);
        }
        return objHashMap;
    }

    /**
     * Motivo: Obtiene las acciones en base al tipo de documento
     * <br>IOZ01092016I</a>
     * <br>Fecha: 12/09/2016
     * @return    HashMap
     * */
    public HashMap getListAccion(String av_type_document){
        HashMap objHashMap= new HashMap();
        String strMessage="";
        try{
            objHashMap=getSEJBBiometricaRemote().getListAccion(av_type_document);
        }
        catch(SQLException e){
            strMessage  = "[SQLException][BiometricaService][getListAccion][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            System.out.println("strMessage:"+strMessage);
        }
        catch(RemoteException e){
            strMessage  = "[RemoteException][BiometricaService][getListAccion][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            System.out.println("strMessage:"+strMessage);
        }
        catch(Exception e){
            strMessage  = "[Exception][BiometricaService][getListAccion][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            System.out.println("[getListAccion]strMessage:"+strMessage);
        }
        return objHashMap;
    }

    /**
     * Motivo: inserta el registrar
     * <br>IOZ15092016I</a>
     * <br>Fecha: 12/09/2016
     * @return    String
     * */
    public   String  insertRegistrar(String login, String tipoDoc, String numDoc, String accion, String customerId, String sourcev){
        String strMessage="";

        try{
            strMessage=getSEJBBiometricaRemote().insertRegistrar(login, tipoDoc, numDoc, accion, customerId, sourcev);

        }
        catch(SQLException e){
            strMessage  = "[SQLException][BiometricaService][getAnularVerificacion][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println("strMessage:"+strMessage);
        }
        catch(RemoteException e){
            strMessage  = "[RemoteException][BiometricaService][getAnularVerificacion][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println("strMessage:"+strMessage);
        }
        catch(Exception e){
            strMessage  = "[Exception][BiometricaService][getAnularVerificacion][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println("strMessage:"+strMessage);
        }

        return  strMessage;
    }

    /**
     * Motivo: inserta verificacion aislada
     * <br>PORTEGA</a>
     * <br>Fecha: 05/10/2016
     * @return    String
     * */
    public String insViaCustomer(String verificationType, String useCase, String motivo, String authorizer, String transactionType,
                                 Integer statusVerification, String docNumber, String phoneNumber, String docType, Integer customerId,
                                 Integer verificationId){
        String strMessage="";

        try{
            strMessage=getSEJBBiometricaRemote().insViaCustomer(verificationType, useCase, motivo, authorizer, transactionType,
                    statusVerification, docNumber, phoneNumber, docType, customerId, verificationId);

        }
        catch(SQLException e){
            strMessage  = "[SQLException][BiometricaService][insViaCustomer][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println("strMessage:"+strMessage);
        }
        catch(RemoteException e){
            strMessage  = "[RemoteException][BiometricaService][insViaCustomer][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println("strMessage:"+strMessage);
        }
        catch(Exception e){
            strMessage  = "[Exception][BiometricaService][insViaCustomer][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println("strMessage:"+strMessage);
        }

        return  strMessage;
    }

    /**
     * Motivo: Consultar verificaciones aisladas
     * <br>RO
     * <br>Fecha: 20/09/2016
     * @return  HashMap
     * */
    public HashMap listarVerificaciones(String tipoDocumento, String  numeroDocumento, String fechaInicio, String fechaFin){
        String strMessage;
        HashMap objHashMap = new HashMap();

        System.out.println("RO001: listarVerificaciones BiometricaService.java");
        try{
            objHashMap=getSEJBBiometricaRemote().listarVerificaciones(tipoDocumento, numeroDocumento, fechaInicio, fechaFin);
        }
        catch(SQLException e){
            strMessage  = "[SQLException][BiometricaService][getAnularVerificacion][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            System.out.println("strMessage:"+strMessage);
        }
        catch(RemoteException e){
            strMessage  = "[RemoteException][BiometricaService][getAnularVerificacion][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            System.out.println("strMessage:"+strMessage);
        }
        catch(Exception e){
            strMessage  = "[Exception][BiometricaService][getAnularVerificacion][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            System.out.println("strMessage:"+strMessage);
        }

        System.out.println("RO001: listarVerificaciones BiometricaService.java Fin");
        return  objHashMap;
    }

    /**
     * Motivo: Listar tipos de documentos permitidos en la verificacion
     * <br>RO
     * <br>Fecha: 20/09/2016
     * @return HashMap
     * */
    public HashMap listarTiposDocumentoVerificaciones(){
        String strMessage;
        HashMap objHashMap = new HashMap();
        System.out.println("RO001: listarTiposDocumentoVerificaciones BiometricaService.java");
        try{
            objHashMap=getSEJBBiometricaRemote().listarTiposDocumentoVerificaciones();
        }
        catch(SQLException e){
            strMessage  = "[SQLException][BiometricaService][getAnularVerificacion][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            System.out.println("strMessage:"+strMessage);
        }
        catch(RemoteException e){
            strMessage  = "[RemoteException][BiometricaService][getAnularVerificacion][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            System.out.println("strMessage:"+strMessage);
        }
        catch(Exception e){
            strMessage  = "[Exception][BiometricaService][getAnularVerificacion][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            System.out.println("strMessage:"+strMessage);
        }
        System.out.println("RO001: listarTiposDocumentoVerificaciones BiometricaService.java Fin");
        return  objHashMap;
    }

    /**
     * Motivo: Consultar verificaciones aisladas
     * <br>RO
     * <br>Fecha: 20/09/2016
     * @return    String
     * */
    public HashMap obtenerDetalleVia(Long identificador){
        String strMessage;
        HashMap objHashMap = new HashMap();
        System.out.println("RO001: obtenerDetalleVia BiometricaService.java");
        try{
            objHashMap=getSEJBBiometricaRemote().obtenerDetalleVia(identificador);
        }
        catch(SQLException e){
            strMessage  = "[SQLException][BiometricaService][obtenerDetalleVia][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            System.out.println("strMessage:"+strMessage);
        }
        catch(RemoteException e){
            strMessage  = "[RemoteException][BiometricaService][obtenerDetalleVia][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            System.out.println("strMessage:"+strMessage);
        }
        catch(Exception e){
            strMessage  = "[Exception][BiometricaService][obtenerDetalleVia][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            System.out.println("strMessage:"+strMessage);
        }
        System.out.println("RO001: obtenerDetalleVia BiometricaService.java Fin");
        return  objHashMap;
    }

    public HashMap getResponseRuleVIA(String docnumber,String codeError,String restriction,
                                      String type,String source,Integer flagdni,Integer attempt,String ercAcepta,int customerId){
        HashMap objHashMap= new HashMap();
        String strMessage="";
        try{
            objHashMap=getSEJBBiometricaRemote().getResponseRuleVIA(docnumber, codeError, restriction,
                    type, source, flagdni, attempt, ercAcepta,customerId);
        }
        catch(SQLException e){
            strMessage  = "[SQLException][BiometricaService][getResponseRuleVIA][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            System.out.println("strMessage:"+strMessage);
        }
        catch(RemoteException e){
            strMessage  = "[RemoteException][BiometricaService][getResponseRuleVIA][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            System.out.println("strMessage:"+strMessage);
        }
        catch(Exception e){
            strMessage  = "[Exception][BiometricaService][getResponseRuleVIA][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessage",strMessage);
            System.out.println("[getResponseRuleVIA]strMessage:"+strMessage);
        }

        return  objHashMap;
    }

    public   String  getActionBiometricVIA(String action,VerificationCustomerBean verificationCustomerBean,BiometricBean biometricBean,
                                           String login, int customerId){
        String strMessage="";

        try{
            strMessage=getSEJBBiometricaRemote().getActionBiometricVIA(action, verificationCustomerBean, biometricBean, login, customerId);

        }
        catch(SQLException e){
            strMessage  = "[SQLException][BiometricaService][getActionBiometricVIA][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println("strMessage:"+strMessage);
        }
        catch(RemoteException e){
            strMessage  = "[RemoteException][BiometricaService][getActionBiometricVIA][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println("strMessage:"+strMessage);
        }
        catch(Exception e){
            strMessage  = "[Exception][BiometricaService][getActionBiometricVIA][" + e.getClass() + " " + e.getMessage()+"]";
            System.out.println("strMessage:"+strMessage);
        }

        return  strMessage;
    }


    public   HashMap getViaConfigTypeDocList(){
        HashMap objHashMap= new HashMap();
        String strMessage="";
        try{
            objHashMap=getSEJBBiometricaRemote().getViaConfigTypeDocList();
        }
        catch(SQLException e){
            strMessage  = "[SQLException][getViaConfigurationList][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessageError",strMessage);
            System.out.println("strMessageError:"+strMessage);
        }
        catch(RemoteException e){
            strMessage  = "[RemoteException][getViaConfigurationList][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessageError",strMessage);
            System.out.println("strMessageError:"+strMessage);
        }
        catch(Exception e){
            strMessage  = "[Exception][getViaConfigurationList][" + e.getClass() + " " + e.getMessage()+"]";
            objHashMap.put("strMessageError",strMessage);
            System.out.println("strMessageError:"+strMessage);
        }

        return  objHashMap;
    }
}
