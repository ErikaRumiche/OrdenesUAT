package pe.com.nextel.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;


public interface SEJBOrderEditRemoteHome extends EJBHome {
    SEJBOrderEditRemote create() throws RemoteException, CreateException;
}
