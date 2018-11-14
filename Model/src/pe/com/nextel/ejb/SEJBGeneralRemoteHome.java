package pe.com.nextel.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;


public interface SEJBGeneralRemoteHome extends EJBHome 
{
  SEJBGeneralRemote create() throws RemoteException, CreateException;
}