package pe.com.nextel.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;


public interface SEJBCustomerRemoteHome extends EJBHome {
    SEJBCustomerRemote create() throws RemoteException, CreateException;
}
