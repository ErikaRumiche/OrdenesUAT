package pe.com.portability.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;


public interface SEJBPortabilityOrderRemoteHome extends EJBHome 
{
  SEJBPortabilityOrderRemote create() throws RemoteException, CreateException;
}