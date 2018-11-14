package pe.com.nextel.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;


public interface SEJBOrderSearchRemoteHome extends EJBHome {

	SEJBOrderSearchRemote create() throws RemoteException, CreateException;
	
}