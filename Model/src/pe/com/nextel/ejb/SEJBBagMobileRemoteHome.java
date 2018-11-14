package pe.com.nextel.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

/**
 * Created by montoymi on 03/02/2016.
 */
public interface SEJBBagMobileRemoteHome extends EJBHome {
    SEJBBagMobileRemote create() throws RemoteException, CreateException;
}
