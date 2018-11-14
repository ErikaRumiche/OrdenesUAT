package pe.com.nextel.service;

import pe.com.nextel.ejb.SEJBOrderPrintLabelRemote;
import pe.com.nextel.ejb.SEJBOrderPrintLabelRemoteHome;
import pe.com.nextel.util.MiUtil;

import javax.naming.Context;
import javax.rmi.PortableRemoteObject;
import java.util.HashMap;

/**
 * Created by Jorge Gabriel on 07/08/2015.
 */
public class OrderPrintLabelService  extends  GenericService {

    public static SEJBOrderPrintLabelRemote getSEJBOrderPrintLabelRemote() {
        try {
            final Context context = MiUtil.getInitialContext();
            final SEJBOrderPrintLabelRemoteHome SEJBOrderPrintLabelRemoteHome = (SEJBOrderPrintLabelRemoteHome) PortableRemoteObject.narrow(context.lookup(
                    "SEJBOrderPrintLabel"), SEJBOrderPrintLabelRemoteHome.class);
            SEJBOrderPrintLabelRemote SEJBOrderPrintLabelRemote;
            SEJBOrderPrintLabelRemote = SEJBOrderPrintLabelRemoteHome.create();

            return SEJBOrderPrintLabelRemote;
        } catch (Exception e) {
            logger.error(formatException(e));

            return null;
        }
    }

    /**
     * @see pe.com.nextel.dao.OrderDAO#valOrderPrintLabel(long)
     */
    public HashMap valOrderPrintLabel(long lOrderId) {
        if (logger.isDebugEnabled())
            logger.debug("--Inicio--");
        HashMap hshDataMap = new HashMap();
        try {
            hshDataMap = getSEJBOrderPrintLabelRemote().valOrderPrintLabel(lOrderId);
        }
        catch(Throwable t) {
            manageCatch(hshDataMap, t);
        }
        return hshDataMap;
    }

    /**
     * @see pe.com.nextel.dao.OrderDAO#sendPrintLabels(long, long, String, long, String, String, long)
     */
    public String sendPrintLabels(long lOrderId, long lSpecificationId, String lItemsIds, long lCustomerId, String lCustomerName,String wsLogin,long buildingId) {
        String strMessage = "";
        if (logger.isDebugEnabled())
            logger.debug("--Inicio--");
        try {
            return getSEJBOrderPrintLabelRemote().sendPrintLabels(lOrderId, lSpecificationId, lItemsIds, lCustomerId, lCustomerName, wsLogin, buildingId);
        }
        catch(Throwable t) {
            //manageCatch(strMessage, t);
            strMessage = t.getMessage();
        }
        return strMessage;
    }

}
