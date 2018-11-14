package pe.com.nextel.ejb;

import pe.com.nextel.bean.PenaltyBean;
import pe.com.nextel.util.RequestHashMap;

import javax.ejb.EJBObject;
import java.sql.SQLException;
import java.util.HashMap;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by Jorge Gabriel on 19/05/2016.
 */
public interface SEJBPenaltyRemote extends EJBObject {

    //DERAZO Requerimiento REQ-0428
    public HashMap getPenaltySimulator(String strTelefono, String strCustomerId) throws SQLException, Exception, RemoteException;

    //DERAZO Requerimiento REQ-0428
    public HashMap getPenaltyListByPhone(String strObjectType,String strObject, String strCustomerId) throws SQLException, Exception, RemoteException;

    //DERAZO Requerimiento REQ-0428
    public HashMap getFlagShowPenaltySimulatorEdit(String strCustomerId, String strPhone) throws SQLException, Exception, RemoteException;

    //DERAZO Requerimiento REQ-0428
    public HashMap verifAddendumPenalty(String strOption,String strOrderId, String strUser) throws SQLException, Exception, RemoteException;

    //DERAZO Requerimiento REQ-0428
    public HashMap getFlagShowPenaltyFunctionality(String strOrderId, String strSpecificationId, String strStatus, String strUser) throws SQLException, Exception, RemoteException;

    //DERAZO Requerimiento REQ-0428
    public HashMap getConfigurationList(String strConfiguration) throws SQLException, Exception, RemoteException;

    public HashMap getOrderPenaltyByParentOrderId(long npparentorderid) throws SQLException, Exception;

    /**
     Method : updateFastOrderIdPenalty
     *
     * Motivo: Actualiza el id de la orden de pago generada para la penalidad Addendum.
     * <br>Realizado por: <a href="mailto:jorge.gabriel@teamsoft.com.pe">Jorge Gabriel</a>
     * <br>Fecha: 30/05/2016
     * @return String
     */
    public String updateFastOrderIdPenalty(long lOrderId, long lFastOrderId, String strLogin) throws SQLException, Exception;

    /**
     * @see pe.com.nextel.dao.PenaltyDAO#saveOrderPenalty(long, String, String, List)
    **/
    public HashMap saveOrderPenalty(long lOrderId, String strPaymentterms, String strLogin, List<PenaltyBean> penaltyList) throws SQLException, Exception;

    public HashMap getPageOrderById(int userId,int appId,long lOrderId) throws SQLException, Exception;


    public String updateStatusFastOrder(long lOrderId,long lFastOrderId, String strLogin) throws  Exception;
    
    //CDIAZ Requerimiento PRY-0817
    public HashMap getFlagShowPenaltyPayFunctionality(long lOrderId) throws SQLException, Exception, RemoteException;

    public HashMap getConfigurationListBoxList(String strNroRuc, long lUserId, int iAppId) throws SQLException, Exception;
}
