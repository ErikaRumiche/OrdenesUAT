package pe.com.nextel.ejb;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * Created by montoymi on 03/02/2016.
 */
public interface SEJBBagMobileRemote extends EJBObject {

    HashMap validateCommunites(String phoneNumber, long customerBscsId) throws RemoteException;

    HashMap removeAllCommunities(String phoneNumber, long customerBscsId) throws RemoteException;

}
