package pe.com.nextel.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;


public interface SEJBGuideRemoteHome extends EJBHome 
{
  SEJBGuideRemote create() throws RemoteException, CreateException;
}