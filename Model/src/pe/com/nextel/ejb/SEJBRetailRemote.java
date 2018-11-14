package pe.com.nextel.ejb;

import java.rmi.RemoteException;

import java.sql.SQLException;

import java.util.HashMap;

import javax.ejb.EJBObject;

import pe.com.nextel.form.RetailForm;


public interface SEJBRetailRemote extends EJBObject {

	HashMap newOrderRetail(RetailForm retailForm) throws SQLException, RemoteException, Exception;
   String updPhoneItem(long lItemId,String strPhone) throws SQLException, RemoteException;
   String updContractItemDev(long lItemDevId,long iCoId) throws SQLException, RemoteException;
   int getSiteidByCodbscs(String strcodBscs) throws SQLException , RemoteException, Exception;
}