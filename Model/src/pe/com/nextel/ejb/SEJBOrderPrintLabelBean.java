package pe.com.nextel.ejb;

import pe.com.nextel.dao.OrderDAO;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by Jorge Gabriel on 07/08/2015.
 */
public class SEJBOrderPrintLabelBean implements SessionBean {
    private SessionContext _context;
    OrderDAO objOrderDAO = null;

    public void ejbCreate() {
        objOrderDAO = new OrderDAO();
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void ejbRemove() {
    }

    public void setSessionContext(SessionContext ctx) {
        _context = ctx;
    }

    /**
     *
     * @throws java.lang.Exception
     * @throws java.sql.SQLException
     * @return HashMap, con datos para la impresion de etiquetas.
     * @param lOrderId
     */
    public HashMap valOrderPrintLabel(long lOrderId) throws SQLException, Exception {
        return objOrderDAO.valOrderPrintLabel(lOrderId);
    }


    /**
     *
     * @throws java.lang.Exception
     * @throws java.sql.SQLException
     * @return String, mensaje de error.
     * @param lOrderId
     * @param lSpecificationId
     * @param lItemsIds
     * @param lCustomerId
     * @param lCustomerName
     * @param wsLogin
     * @param buildingId
     */
    public String sendPrintLabels(long lOrderId, long lSpecificationId, String lItemsIds, long lCustomerId, String lCustomerName,String wsLogin,long buildingId) throws SQLException, Exception {
        return objOrderDAO.sendPrintLabels(lOrderId, lSpecificationId, lItemsIds, lCustomerId, lCustomerName, wsLogin, buildingId);
    }
}
