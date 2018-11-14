package pe.com.nextel.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;


public interface SEJBSalesDataRemoteHome extends EJBHome 
{
  SEJBSalesDataRemote create() throws RemoteException, CreateException;
}