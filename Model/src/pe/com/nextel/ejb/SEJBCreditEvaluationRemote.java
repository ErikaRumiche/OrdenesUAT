package pe.com.nextel.ejb;

import java.rmi.RemoteException;

import java.sql.SQLException;

import java.util.HashMap;

import javax.ejb.EJBObject;

import pe.com.nextel.util.RequestHashMap;


public interface SEJBCreditEvaluationRemote extends EJBObject 
{

  HashMap getCreditEvaluationData(long lSourceId, String strSource) throws RemoteException, Exception;    
  HashMap getRuleResult(long lCreditEvaluationId,String strSource)  throws SQLException, RemoteException, Exception;
  HashMap doEvaluateOrder(RequestHashMap objHashMap) throws SQLException, RemoteException, Exception;
  HashMap doEvaluateOrderCreate(RequestHashMap objHashMap) throws SQLException, RemoteException, Exception; //EFLORES PM0011359
  HashMap doReasonReject(long lSourceId, String strSource, int iflag, String strCreatedby) throws SQLException, RemoteException, Exception;  
  HashMap doValidateCredit(long lSourceId,String strSource) throws Exception, SQLException, RemoteException;
  HashMap getLastCustomerScore(long lnorderid) throws Exception, SQLException, RemoteException;
}