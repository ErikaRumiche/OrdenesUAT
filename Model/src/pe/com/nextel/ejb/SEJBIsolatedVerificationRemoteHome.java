package pe.com.nextel.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

public interface SEJBIsolatedVerificationRemoteHome extends EJBHome {
    SEJBIsolatedVerificationRemote create() throws RemoteException, CreateException;
}
