package pe.com.nextel.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

public interface SEJBBiometricaRemoteHome extends EJBHome {
    SEJBBiometricaRemote create() throws RemoteException, CreateException;
}
