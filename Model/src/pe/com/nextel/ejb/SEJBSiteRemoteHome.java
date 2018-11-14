package pe.com.nextel.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;


public interface SEJBSiteRemoteHome extends EJBHome 
{
  SEJBSiteRemote create() throws RemoteException, CreateException;
}