package pe.com.nextel.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

/**
 * Código: TDECONV003-8
 * <br>Realizado por: PZACARIAS
 * <br>Fecha: 18/06/2018
 */
public interface SEJBCustomerAccountFSRemoteHome extends EJBHome {
    SEJBCustomerAccountFSRemote create() throws RemoteException, CreateException;
}
