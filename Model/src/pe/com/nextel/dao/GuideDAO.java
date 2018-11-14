package pe.com.nextel.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import org.apache.commons.lang.StringUtils;

import pe.com.nextel.bean.DeliveryOrderBean;
import pe.com.nextel.bean.EmailBean;
import pe.com.nextel.bean.FileDeliveryBean;
import pe.com.nextel.util.Constante;


public class GuideDAO extends GenericDAO {

	public HashMap insertFileDelivery(FileDeliveryBean fileDeliveryBean) throws SQLException, Exception {
		if (logger.isDebugEnabled()) logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
		Connection conn = null;
		OracleCallableStatement cstmt = null;
		String strMessage = null;
		try {
			int i = 1;
			String sqlStr = generateCallForPackage("ORDERS.NP_WEBSERVICES_PKG.SP_INS_FILE_DELIVERY", 13);
			conn = Proveedor.getConnection();
			cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
			cstmt.setString(i++, fileDeliveryBean.getFileDeliveryId());
			cstmt.setString(i++, fileDeliveryBean.getTransNumber());
			cstmt.setString(i++, fileDeliveryBean.getOrderId());
			cstmt.setString(i++, fileDeliveryBean.getIp());
			cstmt.setString(i++, fileDeliveryBean.getReceptionDate());
			cstmt.setString(i++, fileDeliveryBean.getStatus());
			cstmt.setString(i++, fileDeliveryBean.getErrorMessage());
			cstmt.setString(i++, fileDeliveryBean.getCreatedBy());
			cstmt.setString(i++, fileDeliveryBean.getCreatedDate());
			cstmt.setString(i++, fileDeliveryBean.getNotificado());
			cstmt.setString(i++, fileDeliveryBean.getOperationId());
      cstmt.setString(i++, fileDeliveryBean.getClientName());
			cstmt.registerOutParameter(i++, OracleTypes.VARCHAR);
			cstmt.executeUpdate();
      if (logger.isDebugEnabled()) logger.debug("Después de executeUpdate()");
			strMessage = cstmt.getString(13);
			hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
		} finally {
			closeObjectsDatabase(conn, cstmt, null);
		}
		return hshDataMap;
	}

	public HashMap validateGuide(FileDeliveryBean fileDeliveryBean) throws SQLException, Exception {
		if (logger.isDebugEnabled()) logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
		Connection conn = null;
		OracleCallableStatement cstmt = null;
		String strMessage = null;
		try {
			String sqlStr = generateCallForPackage("ORDERS.NP_WEBSERVICES_PKG.SP_VALIDATE_GUIDE_TO_ADVANCE", 3);
			conn = Proveedor.getConnection();
			cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
			cstmt.setString(1, fileDeliveryBean.getTransNumber());
      cstmt.setString(2, fileDeliveryBean.getCreatedBy());
			cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
			cstmt.execute();
      if (logger.isDebugEnabled()) logger.debug("Después de execute()");
			strMessage = cstmt.getString(3);
			hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
		} finally {
			closeObjectsDatabase(conn, cstmt, null);
		}
		return hshDataMap;
	}
  
  public HashMap getFileDeliveryList(FileDeliveryBean fileDeliveryParam) throws SQLException, Exception {
		if (logger.isDebugEnabled()) logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
		Connection conn = null;
		OracleCallableStatement cstmt = null;
		String strMessage = null;
		ResultSet rs = null;
		ArrayList arrFileDeliveryList = new ArrayList();
		try {
			int i = 1;
			String sqlStr = generateCallForPackage("ORDERS.NP_WEBSERVICES_PKG.SP_GET_FILE_DELIVERY", 4);
			conn = Proveedor.getConnection();
			cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
			cstmt.setString(i++, fileDeliveryParam.getStatus());
			cstmt.setString(i++, fileDeliveryParam.getNotificado());
			cstmt.registerOutParameter(i++, OracleTypes.CURSOR);
			cstmt.registerOutParameter(i++, OracleTypes.VARCHAR);
			cstmt.execute();
      if (logger.isDebugEnabled()) logger.debug("Después de execute()");
			strMessage = cstmt.getString(4);
      if(StringUtils.isBlank(strMessage)) {
        rs = cstmt.getCursor(3);
        while (rs.next()) {
          i = 1;
          FileDeliveryBean fileDeliveryBean = new FileDeliveryBean();
          fileDeliveryBean.setFileDeliveryId(rs.getString(i++));
          fileDeliveryBean.setTransNumber(rs.getString(i++));
          fileDeliveryBean.setOrderId(rs.getString(i++));
          fileDeliveryBean.setIp(rs.getString(i++));
          fileDeliveryBean.setReceptionDate(rs.getString(i++));
          fileDeliveryBean.setStatus(rs.getString(i++));
          fileDeliveryBean.setErrorMessage(rs.getString(i++));
          fileDeliveryBean.setCreatedBy(rs.getString(i++));
          fileDeliveryBean.setCreatedDate(rs.getString(i++));
          fileDeliveryBean.setNotificado(rs.getString(i++));
          fileDeliveryBean.setOperationId(rs.getString(i++));
          fileDeliveryBean.setClientName(rs.getString(i++));
          arrFileDeliveryList.add(fileDeliveryBean);
        }
        hshDataMap.put("arrFileDeliveryList", arrFileDeliveryList);
      } else {
        logger.error(strMessage);
      }
			hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
		} finally {
			closeObjectsDatabase(conn, cstmt, rs);
		}
		return hshDataMap;
	}
  
  public HashMap getDeliveryOrderList(DeliveryOrderBean deliveryOrderParam, int iFlag) throws SQLException, Exception {
		if (logger.isDebugEnabled()) logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
		Connection conn = null;
		OracleCallableStatement cstmt = null;
		String strMessage = null;
		ResultSet rs = null;
		ArrayList arrDeliveryOrderList = new ArrayList();
		try {
			int i = 1;
			String sqlStr = generateCallForPackage("ORDERS.NP_WEBSERVICES_PKG.SP_GET_DELIVERY_ORDER", 5);
			conn = Proveedor.getConnection();
			cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
			cstmt.setString(i++, deliveryOrderParam.getStatus());
			cstmt.setString(i++, deliveryOrderParam.getNotificado());
			cstmt.setInt(i++, iFlag);
			cstmt.registerOutParameter(i++, OracleTypes.CURSOR);
			cstmt.registerOutParameter(i++, OracleTypes.VARCHAR);
			cstmt.execute();
      if (logger.isDebugEnabled()) logger.debug("Después de execute()");
			strMessage = cstmt.getString(5);
      if(StringUtils.isBlank(strMessage)) {
        rs = cstmt.getCursor(4);
        while (rs.next()) {
          i = 1;
          DeliveryOrderBean deliveryOrderBean = new DeliveryOrderBean();
          deliveryOrderBean.setDeliveryOrderId(rs.getString(i++));
          deliveryOrderBean.setOrderId(rs.getString(i++));
          deliveryOrderBean.setQuantityDocument(rs.getString(i++));
          deliveryOrderBean.setQuantityReceipt(rs.getString(i++));
          deliveryOrderBean.setStatus(rs.getString(i++));
          deliveryOrderBean.setError(rs.getString(i++));
          deliveryOrderBean.setErrorMessage(rs.getString(i++));
          deliveryOrderBean.setCreatedDate(rs.getString(i++));
          deliveryOrderBean.setCreatedBy(rs.getString(i++));
          deliveryOrderBean.setModifiedBy(rs.getString(i++));
          deliveryOrderBean.setModifiedDate(rs.getString(i++));
          deliveryOrderBean.setNotificado(rs.getString(i++));
          deliveryOrderBean.setOperationId(rs.getString(i++));
          arrDeliveryOrderList.add(deliveryOrderBean);
        }
        hshDataMap.put("arrDeliveryOrderList", arrDeliveryOrderList);
      } else {
        logger.error(strMessage);
      }
			hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
		} finally {
			closeObjectsDatabase(conn, cstmt, rs);
		}
		return hshDataMap;
	}
  
  public HashMap updateFileDelivery(FileDeliveryBean fileDeliveryBean) throws SQLException, Exception {
		if (logger.isDebugEnabled())
			logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
		Connection conn = null;
		OracleCallableStatement cstmt = null;
		String strMessage = null;
		try {
			int i = 1;
			String sqlStr = generateCallForPackage("ORDERS.NP_WEBSERVICES_PKG.SP_UPD_FILE_DELIVERY", 15);
			conn = Proveedor.getConnection();
			cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
			cstmt.setString(i++, fileDeliveryBean.getFileDeliveryId());
      cstmt.setString(i++, fileDeliveryBean.getTransNumber());
			cstmt.setString(i++, fileDeliveryBean.getOrderId());
			cstmt.setString(i++, fileDeliveryBean.getIp());
			cstmt.setString(i++, fileDeliveryBean.getReceptionDate());
			cstmt.setString(i++, fileDeliveryBean.getStatus());
			cstmt.setString(i++, fileDeliveryBean.getErrorMessage());
			cstmt.setString(i++, fileDeliveryBean.getCreatedBy());
			cstmt.setString(i++, fileDeliveryBean.getCreatedDate());
			cstmt.setString(i++, fileDeliveryBean.getModifiedBy());
			cstmt.setString(i++, fileDeliveryBean.getModifiedDate());
			cstmt.setString(i++, fileDeliveryBean.getNotificado());
			cstmt.setString(i++, fileDeliveryBean.getOperationId());
      cstmt.setString(i++, fileDeliveryBean.getClientName());
			cstmt.registerOutParameter(i++, OracleTypes.VARCHAR);
			cstmt.executeUpdate();
			if (logger.isDebugEnabled())
				logger.debug("Después de executeUpdate()");
			strMessage = cstmt.getString(15);
			hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
		} finally {
			closeObjectsDatabase(conn, cstmt, null);
		}
		return hshDataMap;
	}

  public HashMap updateDeliveryOrder(DeliveryOrderBean deliveryOrderBean) throws SQLException, Exception {
		if (logger.isDebugEnabled())
			logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
		Connection conn = null;
		OracleCallableStatement cstmt = null;
		String strMessage = null;
		try {
			int i = 1;
			String sqlStr = generateCallForPackage("ORDERS.NP_WEBSERVICES_PKG.SP_UPD_DELIVERY_ORDER", 14);
			conn = Proveedor.getConnection();
			cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
			cstmt.setString(i++, deliveryOrderBean.getDeliveryOrderId());
			cstmt.setString(i++, deliveryOrderBean.getOrderId());
			cstmt.setString(i++, deliveryOrderBean.getQuantityDocument());
			cstmt.setString(i++, deliveryOrderBean.getQuantityReceipt());
			cstmt.setString(i++, deliveryOrderBean.getStatus());
			cstmt.setString(i++, deliveryOrderBean.getError());
			cstmt.setString(i++, deliveryOrderBean.getErrorMessage());
			cstmt.setString(i++, deliveryOrderBean.getCreatedBy());
			cstmt.setString(i++, deliveryOrderBean.getCreatedDate());
			cstmt.setString(i++, deliveryOrderBean.getModifiedBy());
			cstmt.setString(i++, deliveryOrderBean.getModifiedDate());
			cstmt.setString(i++, deliveryOrderBean.getNotificado());
			cstmt.setString(i++, deliveryOrderBean.getOperationId());
			cstmt.registerOutParameter(i++, OracleTypes.VARCHAR);
			cstmt.executeUpdate();
			if (logger.isDebugEnabled())
				logger.debug("Después de executeUpdate()");
			strMessage = cstmt.getString(14);
			hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
		} finally {
			closeObjectsDatabase(conn, cstmt, null);
		}
		return hshDataMap;
	}
	
	public HashMap insertWorkflowOrder(long lOrderId, String strAccion, String strInboxNew, String strFlagMigration, String strLogin) throws SQLException, Exception {
		if (logger.isDebugEnabled())
			logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
		Connection conn = null;
		OracleCallableStatement cstmt = null;
		String strMessage = null;
		String strNewInbox = null;
		String strOldInbox = null;
		try {
			int i = 1;
			String sqlStr = generateCallForPackage("BPEL_WORKFLOW.SPI_INS_WORKFLOW_ORDER", 8);
			conn = Proveedor.getConnection();
			cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
			cstmt.setLong(i++, lOrderId);
			cstmt.setString(i++, strAccion);
			cstmt.setString(i++, strInboxNew);
			cstmt.setString(i++, strFlagMigration);
			cstmt.setString(i++, strLogin);
			cstmt.registerOutParameter(i++, OracleTypes.VARCHAR);
			cstmt.registerOutParameter(i++, OracleTypes.VARCHAR);
			cstmt.registerOutParameter(i++, OracleTypes.VARCHAR);
			cstmt.executeUpdate();
			if (logger.isDebugEnabled())
				logger.debug("Después de executeUpdate()");
			strMessage = cstmt.getString(8);
			strOldInbox = cstmt.getString(7);
			strNewInbox = cstmt.getString(6);
			hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
			hshDataMap.put("strOldInbox", strOldInbox);
			hshDataMap.put("strNewInbox", strNewInbox);
		} finally {
			closeObjectsDatabase(conn, cstmt, null);
		}
		return hshDataMap;
	}
  
  public HashMap validateInvokationIP(String strIP, String strTransportista) throws SQLException, Exception {
		if (logger.isDebugEnabled()) logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
		Connection conn = null;
		OracleCallableStatement cstmt = null;
		String strMessage = null;
		try {
			String sqlStr = generateCallForPackage("ORDERS.NP_WEBSERVICES_PKG.SP_INVOKE_WS_VALIDATE_IP", 4);
			conn = Proveedor.getConnection();
			cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
			cstmt.setString(1, strIP);
      cstmt.setString(2, strTransportista);
      cstmt.registerOutParameter(3, OracleTypes.NUMBER);
			cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
			cstmt.execute();
      if (logger.isDebugEnabled()) logger.debug("Después de execute()");
      strMessage = cstmt.getString(4);
      if(StringUtils.isBlank(strMessage)) {
        int iFlag = cstmt.getInt(3);        
        hshDataMap.put("iFlag", new Integer(iFlag));
      }
      hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
		} finally {
			closeObjectsDatabase(conn, cstmt, null);
		}
		return hshDataMap;
	}
  
  public HashMap getIntervalInvokationJob() throws SQLException, Exception {
		if (logger.isDebugEnabled()) logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
		Connection conn = null;
		OracleCallableStatement cstmt = null;
		String strMessage = null;
		try {
			String sqlStr = generateCallForPackage("ORDERS.NP_WEBSERVICES_PKG.SP_GET_INVOKE_WS_INTERVAL_JOB", 2);
			conn = Proveedor.getConnection();
			cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
      cstmt.registerOutParameter(1, OracleTypes.NUMBER);
			cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
			cstmt.execute();
      if (logger.isDebugEnabled()) logger.debug("Después de execute()");
      strMessage = cstmt.getString(2);
      if(StringUtils.isBlank(strMessage)) {
        int iInterval = cstmt.getInt(1);        
        hshDataMap.put("iInterval", new Integer(iInterval));
      }
      hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
		} finally {
			closeObjectsDatabase(conn, cstmt, null);
		}
		return hshDataMap;
	}
  
  public HashMap sendEmail(EmailBean emailBean) throws SQLException, Exception {
		if (logger.isDebugEnabled()) logger.debug("--Inicio--");
		HashMap hshDataMap = new HashMap();
		Connection conn = null;
		OracleCallableStatement cstmt = null;
		String strMessage = null;
		try {
			String sqlStr = generateCallForPackage("WEBSALES.SPI_SEND_EMAIL", 8);
			conn = Proveedor.getConnection();
			cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
			int i = 1;
			cstmt.setLong(i++, emailBean.getLSendMailId());
			cstmt.setString(i++, emailBean.getStrSendTo());
			cstmt.setString(i++, emailBean.getStrCopyTo());
			cstmt.setString(i++, emailBean.getStrBody());
			cstmt.setString(i++, emailBean.getStrSubject());
			cstmt.setString(i++, emailBean.getStrCreatedBy());
			cstmt.setTimestamp(i++, (Timestamp) emailBean.getTsCreatedDate());
			cstmt.registerOutParameter(i++, OracleTypes.VARCHAR);
			cstmt.execute();
			if (logger.isDebugEnabled())
				logger.debug("Después de execute()");
			strMessage = cstmt.getString(8);
			hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
		} finally {
			closeObjectsDatabase(conn, cstmt, null);
		}
		return hshDataMap;
	}

}
