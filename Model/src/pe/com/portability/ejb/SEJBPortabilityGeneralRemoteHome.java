package pe.com.portability.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;


public interface SEJBPortabilityGeneralRemoteHome extends EJBHome 
{
  SEJBPortabilityGeneralRemote create() throws RemoteException, CreateException;
}