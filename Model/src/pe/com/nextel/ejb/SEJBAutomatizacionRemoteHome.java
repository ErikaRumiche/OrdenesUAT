package pe.com.nextel.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;


public interface SEJBAutomatizacionRemoteHome extends EJBHome 
{
  SEJBAutomatizacionRemote create() throws RemoteException, CreateException;
}