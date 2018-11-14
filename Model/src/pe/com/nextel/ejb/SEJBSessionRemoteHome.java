package pe.com.nextel.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;


public interface SEJBSessionRemoteHome extends EJBHome {
    SEJBSessionRemote create() throws RemoteException, CreateException;
}
