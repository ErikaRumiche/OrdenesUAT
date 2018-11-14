package pe.com.nextel.ejb;

import java.rmi.RemoteException;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import java.util.ArrayList;
import pe.com.nextel.bean.CommentBean;


public interface SEJBOrderTabsRemote extends EJBObject {
	
	ArrayList getHistoryActionListByOrder(long lOrderId) throws SQLException, RemoteException, Exception;

	ArrayList getHistoryApproveListByOrder(long lOrderId) throws SQLException, RemoteException, Exception;

	ArrayList getCommentByOrderList(long lOrderId) throws SQLException, RemoteException, Exception;

	void addComment(CommentBean objCommentBean) throws SQLException, RemoteException, Exception;

	HashMap getInventoryList(long lOrderId) throws SQLException, RemoteException, Exception;

	HashMap getInvoiceList(long lOrderId) throws SQLException, RemoteException, Exception;
   
   ArrayList getInvoicetoShowList(long lOrderId) throws SQLException, RemoteException, Exception;
   
   HashMap getInventoryOrder(long lOrderId) throws SQLException, RemoteException, Exception;   
   
   HashMap getInventoryPIOrder(long lOrderId) throws SQLException, RemoteException, Exception;   
	
  ArrayList getRequestOLListByOrder(long lOrderId) throws SQLException, RemoteException, Exception;	

}