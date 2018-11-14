package pe.com.nextel.service;

import pe.com.nextel.ejb.SEJBRoamingRemote;
import pe.com.nextel.ejb.SEJBRoamingRemoteHome;
import pe.com.nextel.util.MiUtil;

import javax.naming.Context;
import javax.rmi.PortableRemoteObject;
import java.util.HashMap;

/**
 * Created by montoymi on 12/08/2015.
 */
public class RoamingService extends GenericService {

    public static SEJBRoamingRemote getSEJBRoamingRemote() {
        try {
            final Context context = MiUtil.getInitialContext();
            final SEJBRoamingRemoteHome sEJBSiteRemoteHome =
                    (SEJBRoamingRemoteHome) PortableRemoteObject.narrow(context.lookup("SEJBRoaming"), SEJBRoamingRemoteHome.class);

            return sEJBSiteRemoteHome.create();
        } catch (Exception ex) {
            System.out.println("Exception : [RoamingService][getSEJBRoamingRemote][" + ex.getMessage() + "]");
            return null;
        }
    }

    /**
     * MMONTOYA [ADT-RCT-092 Roaming con corte]
     *
     * @param phone
     * @return
     */
    public HashMap loadRoamingServices(String phone, long planId) {
        HashMap hshDataMap = new HashMap();

        try {
            hshDataMap = getSEJBRoamingRemote().loadRoamingServices(phone, planId);
        } catch (Throwable t) {
            manageCatch(hshDataMap, t);
        }

        return hshDataMap;
    }

    /**
     * MMONTOYA [ADT-RCT-092 Roaming con corte]
     *
     * @param phone
     * @return
     */
    public HashMap validateRecurrentRoamingService(String phone, String activationDate, int validity, long orderId, String bagCode) { // CFERNANDEZ [PRY-0858] - Se agrego el parametro: bagCode
        HashMap hshDataMap = new HashMap();

        try {
            hshDataMap = getSEJBRoamingRemote().validateRecurrentRoamingService(phone, activationDate, validity, orderId, bagCode); // CFERNANDEZ [PRY-0858] - Se agrego el parametro: bagCode
        } catch (Throwable t) {
            manageCatch(hshDataMap, t);
        }

        return hshDataMap;
    }
}
