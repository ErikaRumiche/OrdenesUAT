package pe.com.nextel.ejb;

import java.rmi.RemoteException;

import java.sql.SQLException;

import java.util.HashMap;

import javax.ejb.EJBObject;

import pe.com.nextel.bean.DeliveryOrderBean;
import pe.com.nextel.bean.EmailBean;
import pe.com.nextel.bean.FileDeliveryBean;


public interface SEJBGuideRemote extends EJBObject {

  public HashMap insertFileDelivery(FileDeliveryBean fileDeliveryBean) throws SQLException, RemoteException, Exception;

  public HashMap validateGuide(FileDeliveryBean fileDeliveryBean) throws SQLException, RemoteException, Exception;
  
  public HashMap getFileDeliveryList(FileDeliveryBean fileDeliveryParam) throws SQLException, RemoteException, Exception;
  
  public HashMap getDeliveryOrderList(DeliveryOrderBean deliveryOrderParam, int iFlag) throws SQLException, RemoteException, Exception;
  
  //public HashMap doInvokeBPELProcess(HashMap hashData, String strLogin) throws SQLException, RemoteException, Exception;
  
  public HashMap updateFileDelivery(FileDeliveryBean fileDeliveryBean) throws SQLException, RemoteException, Exception;
  
  public HashMap updateDeliveryOrder(DeliveryOrderBean deliveryOrderBean) throws SQLException, RemoteException, Exception;
  
  public HashMap insertWorkflowOrder(long lOrderId, String strAccion, String strInboxNew, String strFlagMigration, String strLogin) throws SQLException, RemoteException, Exception;
  
  public HashMap validateInvokationIP(String strIP, String strTransportista) throws SQLException, RemoteException, Exception;
  
  public HashMap getIntervalInvokationJob() throws SQLException, RemoteException, Exception;
  
  public HashMap sendEmail(EmailBean emailBean) throws SQLException, RemoteException, Exception;

}