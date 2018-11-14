package pe.com.nextel.ejb;

import java.rmi.RemoteException;

import java.sql.SQLException;

import java.util.HashMap;

import javax.ejb.EJBObject;

import pe.com.nextel.bean.PortalSessionBean;
import pe.com.nextel.util.RequestHashMap;


public interface SEJBSalesDataRemote extends EJBObject {

  HashMap setSalesData(RequestHashMap request,PortalSessionBean objPortalSesBean)throws RemoteException , Exception;
  
  /***********************************************************************************************************************************
  * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
  * <br>Fecha: 06/05/2010
  * @see pe.com.nextel.dao.SalesDataDAO#getApliactionDataList(long,long,long,long)    
  ************************************************************************************************************************************/ 
  HashMap getAplicationDataList (long lnOrderId,long lngDivisionId,long lngCustomerId,long lngSpecificationId)  throws  Exception, RemoteException,SQLException;
  
  /***********************************************************************************************************************************
  * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
  * <br>Fecha: 06/05/2010
  * @see pe.com.nextel.dao.SalesDataDAO#getAplicationCustomer(long,long,long)    
  ************************************************************************************************************************************/ 
  HashMap getAplicationCustomer (long lngOrderId,long lngDivisionId,long lngCustomerId,String strStatus)  throws  Exception, RemoteException,SQLException;
  
  /***********************************************************************************************************************************
  * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
  * <br>Fecha: 06/05/2010
  * @see pe.com.nextel.dao.SalesDataDAO#getAplicationDetail(long)    
  ************************************************************************************************************************************/ 
  HashMap getAplicationDetail (long lnpbusinesssolutionid)  throws  Exception, RemoteException,SQLException;
  
  /***********************************************************************************************************************************
  * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
  * <br>Fecha: 06/05/2010
  * @see pe.com.nextel.dao.SalesDataDAO#getAplicationDetail(long)    
  ************************************************************************************************************************************/ 
  HashMap delSalesData (RequestHashMap request)  throws  Exception, RemoteException,SQLException;
}