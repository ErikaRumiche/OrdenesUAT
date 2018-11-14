package pe.com.nextel.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;


public interface SEJBRepairRemoteHome extends EJBHome 
{
  SEJBRepairRemote create() throws RemoteException, CreateException;
}