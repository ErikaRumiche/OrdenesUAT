package pe.com.nextel.ejb;

import java.rmi.RemoteException;

import java.sql.SQLException;

import java.util.HashMap;

import javax.ejb.EJBObject;


public interface SEJBMassiveOrderRemote extends EJBObject 
{
 
 HashMap MassiveOrderDAOgetItemOrder(long npcustomerid, long npsiteid, long npspecification, long npcustomeridAcept)throws RemoteException, Exception, SQLException ;

  HashMap getServiceList(int iDivisionId, int iPlanId) throws  Exception, RemoteException, SQLException;

  HashMap getServiceListBySolution(int iDivisionId) throws  SQLException, Exception, RemoteException;

  HashMap getServiceItemListBySolution(int iSolutionId) throws  Exception,  RemoteException, RemoteException, SQLException;

  HashMap getPlanList() throws  Exception, RemoteException;

  HashMap getCommercialService(long numContract) throws   RemoteException,  SQLException, Exception;

  HashMap getValidateByPhone(long npphone, long intSpecification, long nOrderid) throws  SQLException, Exception, RemoteException;

  
}

