package pe.com.nextel.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;


public interface SEJBRetailRemoteHome extends EJBHome {
	
	SEJBRetailRemote create() throws RemoteException, CreateException;
	
}