package pe.com.nextel.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

/**
 * Created by Jorge Gabriel on 19/05/2016.
 */
public interface SEJBPenaltyRemoteHome extends EJBHome {
    SEJBPenaltyRemote create() throws RemoteException, CreateException;
}
