package pe.com.nextel.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;


public interface SEJBMassiveOrderRemoteHome extends EJBHome 
{
  SEJBMassiveOrderRemote create() throws RemoteException, CreateException;

}