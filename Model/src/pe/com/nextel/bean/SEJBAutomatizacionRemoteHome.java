package pe.com.nextel.bean;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;


public interface SEJBAutomatizacionRemoteHome extends EJBHome 
{
  SEJBAutomatizacionRemote create() throws RemoteException, CreateException;
}