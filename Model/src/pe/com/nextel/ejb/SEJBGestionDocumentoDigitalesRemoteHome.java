package pe.com.nextel.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

/**
 * Created by Administrador on 25/10/2016.
 */
public interface SEJBGestionDocumentoDigitalesRemoteHome extends EJBHome {
    SEJBGestionDocumentoDigitalesRemote create() throws RemoteException, CreateException;
}