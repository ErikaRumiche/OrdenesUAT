package pe.com.nextel.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

/**
 * Created by Administrador on 21/12/2015.
 */
public interface SEJBNormalizarDireccionRemoteHome extends EJBHome {
    SEJBNormalizarDireccionRemote create() throws RemoteException, CreateException;
}