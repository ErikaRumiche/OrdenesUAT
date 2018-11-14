package pe.com.portability.ejb;

import java.rmi.RemoteException;

import java.sql.SQLException;

import java.util.HashMap;

import javax.ejb.EJBObject;


public interface SEJBPortabilityGeneralRemote extends EJBObject 
{
  HashMap getSectionDocumentValidate(String strNptable, String strNpDescripcion, String strNpvalue) throws  Exception, RemoteException, SQLException;
}