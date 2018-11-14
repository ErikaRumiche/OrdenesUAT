package pe.com.nextel.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

public interface SEJBIdentificationRemoteHome extends EJBHome {
    SEJBIdentificationRemote create() throws RemoteException, CreateException;
}
