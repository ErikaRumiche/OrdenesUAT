package pe.com.nextel.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;


public interface SEJBCreditEvaluationRemoteHome extends EJBHome 
{
  SEJBCreditEvaluationRemote create() throws RemoteException, CreateException;
}