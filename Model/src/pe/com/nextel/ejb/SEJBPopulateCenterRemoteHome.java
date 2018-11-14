package pe.com.nextel.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

/**
 * Created by JCASTILLO on 04/04/2017.
 * [PRY-0787]
 */
public interface SEJBPopulateCenterRemoteHome extends EJBHome {
    SEJBPopulateCenterRemote create() throws RemoteException, CreateException;
}
