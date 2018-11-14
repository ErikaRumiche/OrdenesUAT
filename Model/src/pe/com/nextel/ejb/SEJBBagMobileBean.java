package pe.com.nextel.ejb;

import org.apache.log4j.Logger;
import pe.com.entel.integracion.communityassociations.proxy.CommunityAssociations;
import pe.com.entel.integracion.communityassociations.proxy.CommunityAssociationsPT;
import pe.com.entel.integracion.communityassociations.proxy.types.CommunityAssociationsIsChildContractRequest;
import pe.com.entel.integracion.communityassociations.proxy.types.CommunityAssociationsIsChildContractResponse;
import pe.com.entel.integracion.communityassociations.proxy.types.CommunityAssociationsRemoveRequest;
import pe.com.entel.integracion.communityassociations.proxy.types.CommunityAssociationsResponse;
import pe.com.nextel.dao.BagMobileDAO;
import pe.com.nextel.dao.Proveedor;
import pe.com.nextel.util.Constante;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import static pe.com.nextel.util.GenericObject.formatException;

/**
 * Created by montoymi on 03/02/2016.
 */
public class SEJBBagMobileBean implements SessionBean {
    public enum CommunitesValidationResult {
        PARENT_CONTRACT,
        CHILD_CONTRACT,
        NONE
    }

    protected static Logger logger = Logger.getLogger(SEJBBagMobileBean.class);

    private BagMobileDAO objBagMobileDAO = null;

    public void ejbCreate() {
        objBagMobileDAO = new BagMobileDAO();
    }

    @Override
    public void setSessionContext(SessionContext sessionContext) throws EJBException, RemoteException {

    }

    @Override
    public void ejbRemove() throws EJBException, RemoteException {

    }

    @Override
    public void ejbActivate() throws EJBException, RemoteException {

    }

    @Override
    public void ejbPassivate() throws EJBException, RemoteException {

    }

    /**
     * Valida si el contrato es padre o hijo en alguna comunidad.
     * @param phoneNumber
     * @param customerBscsId
     * @return
     * @throws SQLException
     */
    public HashMap validateCommunites(String phoneNumber, long customerBscsId) {
        logger.info("SEJBBagMobileBean.validateCommunites(phoneNumber => " + phoneNumber + ", customerBscsId => " + customerBscsId + ")");

        HashMap hshDataMap = new HashMap();
        String strMessage = null;
        CommunitesValidationResult validationResult = CommunitesValidationResult.NONE;

        Connection conn = null;

        try {
            conn = Proveedor.getConnection();

            if (isParentContract(conn, phoneNumber)) {
                validationResult = CommunitesValidationResult.PARENT_CONTRACT;
                strMessage = "No puede continuar porque el contrato es padre en una bolsa. Primero se debe desactivar la bolsa.";
            } else if (isChildContract(conn, phoneNumber, customerBscsId)) {
                validationResult = CommunitesValidationResult.CHILD_CONTRACT;
                strMessage = "El contrato está asociado a una o más bolsas. Para continuar se debe desasociar el contrato todas las bolsas. ¿Desea continuar?.";
            }

            hshDataMap.put("validationResult", validationResult);
            hshDataMap.put("strMessage", strMessage);
            return hshDataMap;
        } catch (Exception e) {
            logger.error(formatException(e));
            hshDataMap.put("strMessage", e.getMessage());
            return hshDataMap;
        } finally {
            if (conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Remueve el contrato de todas las comunidades asociadas.
     * @param phoneNumber
     * @param customerBscsId
     * @throws SQLException
     */
    public HashMap removeAllCommunities(String phoneNumber, long customerBscsId) {
        logger.info("SEJBBagMobileBean.deassociateCommunites(phoneNumber => " + phoneNumber + ", customerBscsId => " + customerBscsId + ")");

        HashMap hshDataMap = new HashMap();

        CommunityAssociations communityAssociations = new CommunityAssociations();
        CommunityAssociationsPT communityAssociationsPT = communityAssociations.getCommunityAssociationsPort();

        CommunityAssociationsRemoveRequest request = new CommunityAssociationsRemoveRequest();
        CommunityAssociationsResponse response = null;

        request.setServiceType(null);
        request.setPhoneNumber(phoneNumber);
        request.setCustomerBSCSId(customerBscsId);

        response = communityAssociationsPT.removeCommunityAssociationByPhone(request);

        if (!("0".equals(response.getAudit().getErrorCode()))){
            logger.error(Constante.ERROR + "No se pudo remover de las comunidades: " + response.getAudit().getErrorMsg());
            logger.error("Para detalles ver log de: CellPhoneManagement-ejb");
            hshDataMap.put("strMessage", response.getAudit().getErrorMsg());
        }

        return hshDataMap;
    }

    private boolean isParentContract(Connection conn, String phoneNumber) throws Exception {
        HashMap hshDataMap = objBagMobileDAO.selectCommunityByPhone(conn, phoneNumber);
        if (hshDataMap.get("strMessage") != null) {
            String message = hshDataMap.get("strMessage").toString();
            throw new Exception(message);
        }

        String communityName = (String) hshDataMap.get("communityName");
        return communityName != null;
    }

    private boolean isChildContract(Connection conn, String phoneNumber, long customerBscsId) throws Exception {
        HashMap hshDataMap = objBagMobileDAO.selectCommunitesByCustomerBscsId(conn, customerBscsId) ;
        if (hshDataMap.get("strMessage") != null) {
            String message = hshDataMap.get("strMessage").toString();
            throw new Exception(message);
        }

        List<String> communityNames = (List<String>) hshDataMap.get("communityNames");
        if(communityNames.size()<1)return false;

        CommunityAssociations communityAssociations = new CommunityAssociations();
        CommunityAssociationsPT communityAssociationsPT = communityAssociations.getCommunityAssociationsPort();

        CommunityAssociationsIsChildContractRequest request = new CommunityAssociationsIsChildContractRequest();
        CommunityAssociationsIsChildContractResponse response = null;

        request.setPhoneNumber(phoneNumber);
        request.getCommunityNames().addAll(communityNames);
        response = communityAssociationsPT.isChildContract(request);

        if (!("0".equals(response.getAudit().getErrorCode()))){
            logger.error(Constante.ERROR + "Error al verificar si el contrato es padre: " + response.getAudit().getErrorMsg());
            logger.error("Para detalles ver log de: CellPhoneManagement-ejb");
            hshDataMap.put("strMessage", response.getAudit().getErrorMsg());
        }

        return response.isChildContract();
    }
}

