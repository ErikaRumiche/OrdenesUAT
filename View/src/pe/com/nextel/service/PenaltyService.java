package pe.com.nextel.service;

import pe.com.nextel.bean.PenaltyBean;
import pe.com.nextel.ejb.SEJBPenaltyRemote;
import pe.com.nextel.ejb.SEJBPenaltyRemoteHome;
import pe.com.nextel.ejb.SEJBPenaltyRemote;
import pe.com.nextel.util.MiUtil;
import pe.com.nextel.util.RequestHashMap;

import javax.naming.Context;
import javax.rmi.PortableRemoteObject;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jorge Gabriel on 19/05/2016.
 */
public class PenaltyService extends  GenericService {

    public static SEJBPenaltyRemote getSEJBPenaltyRemote() {
        try {
            final Context context = MiUtil.getInitialContext();
            final SEJBPenaltyRemoteHome SEJBPenaltyRemoteHome = (SEJBPenaltyRemoteHome) PortableRemoteObject.narrow(context.lookup(
                    "SEJBPenalty"), SEJBPenaltyRemoteHome.class);
            SEJBPenaltyRemote SEJBPenaltyRemote;
            SEJBPenaltyRemote = SEJBPenaltyRemoteHome.create();

            return SEJBPenaltyRemote;
        } catch (Exception e) {
            logger.error(formatException(e));

            return null;
        }
    }

    /**
     * @see pe.com.nextel.dao.PenaltyDAO#getPenaltySimulator()
     * DERAZO Requerimiento REQ-0428 Obtener penalidad simulada
     */
    public HashMap getPenaltySimulator(String strTelefono, String strCustomerId) {
        HashMap hshDataMap = new HashMap();
        try {
            hshDataMap = getSEJBPenaltyRemote().getPenaltySimulator(strTelefono, strCustomerId);
        }
        catch(Throwable t) {
            manageCatch(hshDataMap, t);
            System.out.println("Error [SQLException][PenaltyService][getPenaltySimulator]["+t.getMessage()+"]");
        }
        return hshDataMap;
    }

    /**
     * @see pe.com.nextel.dao.PenaltyDAO#getPenaltyListByPhone()
     * DERAZO Requerimiento REQ-0428 Obtiene las penalidades para un conjunto de telefonos de un Cliente
     */
    public HashMap getPenaltyListByPhone(String strObjectType,String strObject, String strCustomerId) {
        HashMap hshDataMap = new HashMap();
        try {
            hshDataMap = getSEJBPenaltyRemote().getPenaltyListByPhone(strObjectType,strObject, strCustomerId);
        }
        catch(Throwable t) {
            manageCatch(hshDataMap, t);
            System.out.println("Error [SQLException][PenaltyService][getPenaltyListByPhone]["+t.getMessage()+"]");
        }
        return hshDataMap;
    }

    /**
     * @see pe.com.nextel.dao.PenaltyDAO#getFlagShowPenaltySimulatorEdit()
     * DERAZO Requerimiento REQ-0428 Validar si la especificacion mostrara penalidad simulada por telefono al editar
     */
    public HashMap getFlagShowPenaltySimulatorEdit(String strCustomerId, String strPhone) {
        HashMap hshDataMap = new HashMap();
        try {
            hshDataMap = getSEJBPenaltyRemote().getFlagShowPenaltySimulatorEdit(strCustomerId, strPhone);
        }
        catch(Throwable t) {
            manageCatch(hshDataMap, t);
            System.out.println("Error [SQLException][PenaltyService][getFlagShowPenaltySimulatorEdit]["+t.getMessage()+"]");
        }
        return hshDataMap;
    }

    /**
     * @see pe.com.nextel.dao.PenaltyDAO#verifAddendumPenalty()
     * DERAZO Requerimiento REQ-0428 Obtiene un flag para saber si se va a mostrar o no el link de pagar penalidad
     */
    public HashMap verifAddendumPenalty(String strOption, String strOrderId, String strUser) {
        HashMap hshDataMap = new HashMap();
        try {
            hshDataMap = getSEJBPenaltyRemote().verifAddendumPenalty(strOption, strOrderId, strUser);
        }
        catch(Throwable t) {
            manageCatch(hshDataMap, t);
            System.out.println("Error [SQLException][PenaltyService][getShowFlagPayPenalty]["+t.getMessage()+"]");
        }
        return hshDataMap;
    }

    /**
     * @see pe.com.nextel.dao.PenaltyDAO#getFlagShowPenaltyFunctionality()
     * DERAZO Requerimiento REQ-0428 Obtiene un flag general para mostrar o no la funcionalidad de pago de penalidades
     */
    public HashMap getFlagShowPenaltyFunctionality(String strOrderId, String strSpecificationId, String strStatus, String strUser) {
        HashMap hshDataMap = new HashMap();
        try {
            hshDataMap = getSEJBPenaltyRemote().getFlagShowPenaltyFunctionality(strOrderId, strSpecificationId, strStatus, strUser);
        }
        catch(Throwable t) {
            manageCatch(hshDataMap, t);
            System.out.println("Error [SQLException][PenaltyService][getFlagShowPenaltyFunctionality]["+t.getMessage()+"]");
        }
        return hshDataMap;
    }

    /**
     * @see pe.com.nextel.dao.PenaltyDAO#getConfigurationList()
     * DERAZO Requerimiento REQ-0428 Permite obtener un listado de valores para una configuracion
     */
    public HashMap getConfigurationList(String strConfiguration) {
        HashMap hshDataMap = new HashMap();
        try {
            hshDataMap = getSEJBPenaltyRemote().getConfigurationList(strConfiguration);
        }
        catch(Throwable t) {
            manageCatch(hshDataMap, t);
            System.out.println("Error [SQLException][PenaltyService][getConfigurationList]["+t.getMessage()+"]");
        }
        return hshDataMap;
    }

    /**
     * @see pe.com.nextel.dao.PenaltyDAO#getOrderPenaltyByParentOrderId(long)
     */
    public HashMap getOrderPenaltyByParentOrderId(long npparentorderid) {
        HashMap hshDataMap = new HashMap();
        try {
            hshDataMap = getSEJBPenaltyRemote().getOrderPenaltyByParentOrderId(npparentorderid);
        }
        catch(Throwable t) {
            manageCatch(hshDataMap, t);
            System.out.println("Error [SQLException][PenaltyService][getOrderPenaltyByParentOrderId]["+t.getMessage()+"]");
        }
        return hshDataMap;
    }

    /**
     * @see pe.com.nextel.dao.PenaltyDAO#updateFastOrderIdPenalty(long, long, String)
     * Si lFastOrderId es 0 entonces se pasara nulo como parametro
     */
    public String doUpdFastOrderIdPenalty(long lOrderId, long lFastOrderId, String strLogin) {
        if (logger.isDebugEnabled())
            logger.debug("--Inicio--");
        try {
            return getSEJBPenaltyRemote().updateFastOrderIdPenalty(lOrderId, lFastOrderId, strLogin);
        }
        catch(Throwable t) {
            t.printStackTrace();
            System.out.println("Error [SQLException][PenaltyService][getOrderPenaltyByParentOrderId]["+t.getMessage()+"]");
            return "Error [SQLException][PenaltyService][getOrderPenaltyByParentOrderId]["+t.getMessage()+"]";
        }
    }

    /**
     * @see pe.com.nextel.dao.PenaltyDAO#saveOrderPenalty(long, String, String,List)
     */
    public HashMap saveOrderPenalty(long lOrderId, String strPaymentterms, String strLogin, List<PenaltyBean> penaltyList) {
        if (logger.isDebugEnabled())
            logger.debug("--Inicio--");
        HashMap hshDataMap = new HashMap();
        try {
            hshDataMap = getSEJBPenaltyRemote().saveOrderPenalty(lOrderId, strPaymentterms, strLogin, penaltyList);
        }
        catch(Throwable t) {
            manageCatch(hshDataMap, t);
            System.out.println("Error [SQLException][PenaltyService][doSaveOrder]["+t.getMessage()+"]");
        }
        return hshDataMap;
    }

    /**
     * @see pe.com.nextel.dao.PenaltyDAO#getPageOrderById(long)
     */
    public HashMap getPageOrderById(int userId,int appId,long lOrderId) {
        HashMap hshDataMap = new HashMap();
        try {
            hshDataMap = getSEJBPenaltyRemote().getPageOrderById(userId,appId,lOrderId);
        }
        catch(Throwable t) {
            manageCatch(hshDataMap, t);
            System.out.println("Error [SQLException][PenaltyService][getPageOrderById]["+t.getMessage()+"]");
        }
        return hshDataMap;
    }


    /**
     * @see pe.com.nextel.dao.PenaltyDAO#updateStatusFastOrder(long ,long ,String)
     */
    public String updateStatusFastOrder(long lOrderId, long lFastOrderId, String strLogin) {
        HashMap hshDataMap = new HashMap();
        String strMessage= null;
        try {
            strMessage = getSEJBPenaltyRemote().updateStatusFastOrder(lOrderId, lFastOrderId, strLogin);
        }
        catch(Throwable t) {
            manageCatch(hshDataMap, t);
            System.out.println("Error [SQLException][PenaltyService][updateStatusFastOrder]["+t.getMessage()+"]");
            strMessage = "Error [SQLException][PenaltyService][updateStatusFastOrder]["+t.getMessage()+"]";
        }
        return strMessage;
    }
    
    /**
     * @see pe.com.nextel.dao.PenaltyDAO#getFlagShowPenaltyPayFunctionality(long)
     * CDIAZ Requerimiento PRY-0817 Permite obtener el Flag que activa funcionalidad de Penalidad para el Pagar penalidad
     */
    public HashMap getFlagShowPenaltyPayFunctionality(long lOrderId) {
        HashMap hshDataMap = new HashMap();
        try {
            hshDataMap = getSEJBPenaltyRemote().getFlagShowPenaltyPayFunctionality(lOrderId);
        }
        catch(Throwable t) {
            manageCatch(hshDataMap, t);
            System.out.println("Error [SQLException][PenaltyService][getFlagPenaltyPayFunctionality]["+t.getMessage()+"]");
        }
        return hshDataMap;
    }
    
    public HashMap getConfigurationListBoxList(String strNroRuc, int lUserId, int iAppId) {
        HashMap hshDataMap = new HashMap();
        try {        	
            hshDataMap = getSEJBPenaltyRemote().getConfigurationListBoxList(strNroRuc, new Long(lUserId), iAppId);
        }
        catch(Throwable t) {
            manageCatch(hshDataMap, t);
            System.out.println("Error [SQLException][PenaltyService][getConfigurationListBoxList]["+t.getMessage()+"]");
        }
        return hshDataMap;
    }
}
