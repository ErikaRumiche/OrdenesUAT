package pe.com.nextel.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

/**
 * Created by montoymi on 12/08/2015.
 * [ADT-RCT-092 Roaming con corte]
 */
public interface SEJBRoamingRemoteHome extends EJBHome {
    SEJBRoamingRemote create() throws RemoteException, CreateException;
}
