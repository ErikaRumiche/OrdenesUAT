package pe.com.nextel.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

/**
 * Created by Jorge Gabriel on 07/08/2015.
 */
public interface SEJBOrderPrintLabelRemoteHome extends EJBHome {
    SEJBOrderPrintLabelRemote create() throws RemoteException, CreateException;
}
