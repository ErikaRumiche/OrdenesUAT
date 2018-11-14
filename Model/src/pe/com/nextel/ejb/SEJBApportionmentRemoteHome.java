package pe.com.nextel.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * Created by JCURI on 11/07/2017.
 */
public interface SEJBApportionmentRemoteHome extends EJBHome {
    SEJBApportionmentRemote create() throws RemoteException, CreateException;
}
