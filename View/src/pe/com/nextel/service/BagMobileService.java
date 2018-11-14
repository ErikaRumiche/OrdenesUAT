package pe.com.nextel.service;

import pe.com.nextel.ejb.SEJBBagMobileRemote;
import pe.com.nextel.ejb.SEJBBagMobileRemoteHome;
import pe.com.nextel.util.MiUtil;

import javax.naming.Context;
import javax.rmi.PortableRemoteObject;
import java.util.HashMap;

/**
 * Created by montoymi on 03/02/2016.
 */
public class BagMobileService extends GenericService {

    public static SEJBBagMobileRemote getSEJBBagMobileRemote() {
        try {
            final Context context = MiUtil.getInitialContext();
            final SEJBBagMobileRemoteHome sEJBSiteRemoteHome =
                    (SEJBBagMobileRemoteHome) PortableRemoteObject.narrow(context.lookup("SEJBBagMobile"), SEJBBagMobileRemoteHome.class);

            return sEJBSiteRemoteHome.create();
        } catch (Exception ex) {
            System.out.println("Exception : [BagMobileService][getSEJBBagMobileRemote][" + ex.getMessage() + "]");
            return null;
        }
    }

    public HashMap validateCommunites(String phoneNumber, long customerBscsId) {
        HashMap hshDataMap = new HashMap();

        try {
            hshDataMap = getSEJBBagMobileRemote().validateCommunites(phoneNumber, customerBscsId);
        } catch (Throwable t) {
            manageCatch(hshDataMap, t);
        }

        return hshDataMap;
    }

    public HashMap removeAllCommunities(String phoneNumber, long customerBscsId) {
        HashMap hshDataMap = new HashMap();

        try {
            hshDataMap = getSEJBBagMobileRemote().removeAllCommunities(phoneNumber, customerBscsId);
        } catch (Throwable t) {
            manageCatch(hshDataMap, t);
        }

        return hshDataMap;
    }
}
