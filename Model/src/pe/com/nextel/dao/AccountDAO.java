package pe.com.nextel.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import org.apache.commons.lang.StringUtils;

import pe.com.nextel.util.Constante;


//import oracle.jdbc.OracleCallableStatement;
//import oracle.jdbc.OraclePreparedStatement;
//import pe.com.nextel.util.DbmsOutput;

/**
 * Motivo:  Clase DAO que contendrá la invocación a los PL/SQL Packages de Creación de Clientes
 * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>,
 * <br>Fecha: 01/10/2007
 * @see GenericDAO
 */
public class AccountDAO extends GenericDAO {
	
	/**
   * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
   * <br>Fecha: 03/10/2007
	 * @return 
	 * @param strCsIdHight
	 * @param lOrderId
	 */
  public ArrayList getCreateCustomer(long lOrderId, String strCsIdHight)  throws SQLException, Exception {
    Connection conn = null;
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		String error;
		//DbmsOutput dbmsOutput;
    
    try {
      String sqlStr = "BEGIN ORDERS.NP_ORDERS11_PKG.SP_GET_CREATE_CUSTOMER(?,?,?,?); END;";
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
		//dbmsOutput = new DbmsOutput(conn);
		//dbmsOutput.enable(1000000);
      cstmt.setLong(1, lOrderId);
		cstmt.setString(2, strCsIdHight);
		cstmt.registerOutParameter(3, OracleTypes.CURSOR);
		cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
      cstmt.executeQuery();
		  //dbmsOutput.show();			
      error = cstmt.getString(4);
      
      if (StringUtils.isNotEmpty(error)) {
        throw new SQLException(error);
      }
      
      rs = cstmt.getCursor(3);
      while (rs.next()) {
        HashMap objHashMap = new HashMap();
        int i = 1;
				objHashMap.put("customerId", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("csId", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("tablaCliente", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("tipoCliente", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("nivel", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("responsablePago", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("grupoCliente", StringUtils.defaultString(rs.getString(i++),""));
				objHashMap.put("transaccion", StringUtils.defaultString(rs.getString(i++),""));
				arrList.add(objHashMap);
      }
      
    }catch (Exception e) {
		logger.error(formatException(e));
		throw new Exception(e);      
    } finally {
		closeObjectsDatabase(conn, cstmt, rs);
    }
    return arrList;
	}
	
	/**
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 03/10/2007
	 * @return 
	 * @param strCsIdHight
	 * @param lOrderId
	 */
	public ArrayList getCreateBillingAccount(long lOrderId, long lCustomerId, String strTablaCliente) throws SQLException, Exception {
		if(logger.isDebugEnabled()) {
			logger.debug("lOrderId: "+lOrderId);
			logger.debug("lCustomerId: "+lCustomerId);
			logger.debug("strTablaCliente: "+strTablaCliente);
		}
		Connection conn = null;
      OracleCallableStatement cstmt = null;
      ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		String error;
      try {
        String sqlStr = "BEGIN ORDERS.NP_ORDERS11_PKG.SP_GET_BILLING_ACCOUNT (?,?,?,?,?); END;";
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
        cstmt.setLong(1, lOrderId);
        cstmt.setLong(2, lCustomerId);
        cstmt.setString(3, strTablaCliente);
        cstmt.registerOutParameter(4, OracleTypes.CURSOR);
        cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
        cstmt.executeQuery();
        error = cstmt.getString(5);
            
        if (StringUtils.isNotEmpty(error)) {
            throw new SQLException(error);
        }
        
        rs = (ResultSet)cstmt.getObject(4);
        
        while (rs.next()) {
          HashMap objHashMap = new HashMap();
          int i = 1;
          objHashMap.put("billingAccountName", StringUtils.defaultString(rs.getString(i++),""));
          objHashMap.put("billingAccountNewId", StringUtils.defaultString(rs.getString(i++),""));
          objHashMap.put("fName", StringUtils.defaultString(rs.getString(i++),""));
          objHashMap.put("bscsCustomerId", StringUtils.defaultString(rs.getString(i++),""));
          objHashMap.put("bscsSeq", StringUtils.defaultString(rs.getString(i++),""));
          arrList.add(objHashMap);
        }
      } catch (Exception e) {
			logger.error(formatException(e));
			throw new Exception(e);      
		}finally {
			closeObjectsDatabase(conn, cstmt, rs);
      }
      return arrList;
	}
	
	public String getCustCodeByCsId(String strCsId) throws SQLException, Exception {
		Connection conn = null;
      OracleCallableStatement cstmt = null;
      ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		String strCustCode = "";
    try {
      String sqlStr = "SELECT custcode FROM customer_all@pbscs_lm WHERE customer_id = ?";
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setString(1, strCsId);
      rs = cstmt.executeQuery();
      while (rs.next()) {
          strCustCode = rs.getString("custcode");
      }
    }catch (Exception e) {
		logger.error(formatException(e));
		throw new Exception(e);      
    }finally {
			closeObjectsDatabase(conn, cstmt, rs);
    }
    return strCustCode; 
	}
	
	public String synchronizeCustomerBSCS(HashMap hshCustomerMap) throws SQLException, Exception {
		if(logger.isDebugEnabled())
			logger.debug("hshCustomerMap: "+hshCustomerMap);
		String custCode = (String) hshCustomerMap.get("custCode");
		long lCsId = ((Long) hshCustomerMap.get("csId")).longValue();
		String strTipo = (String) hshCustomerMap.get("tipo");
		long lCustomerId = Long.parseLong((String) hshCustomerMap.get("customerId"));
		long lSiteId = Long.parseLong((String) hshCustomerMap.get("customerId"));
		long lOrderId = ((Long) hshCustomerMap.get("orderId")).longValue();
		Connection conn = null;
        OracleCallableStatement cstmt = null;
		String strMessage;
		String sqlStr = "BEGIN ORDERS.SPI_UPD_SINCHRONIZE_COUNTS(?,?,?,?,?,?,?); END;";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
         cstmt.setString(1, custCode);
         cstmt.setLong(2, lCsId);
         cstmt.setString(3, strTipo);
         cstmt.setLong(4, lCustomerId);
         cstmt.setLong(5, lSiteId);
         cstmt.setLong(6, lOrderId);
         cstmt.registerOutParameter(7, OracleTypes.VARCHAR);
         cstmt.executeUpdate();
         strMessage = cstmt.getString(7);
      }
      catch (Exception e) {
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn, cstmt, null);
      }
		return strMessage; 
	}
	
	public String synchronizeBillingAccountBSCS(HashMap hshBillingAccountMap) throws SQLException, Exception {
		if(logger.isDebugEnabled())
			logger.debug("hshBillingAccountMap: "+hshBillingAccountMap);
		String strBillingAccountId = (String) hshBillingAccountMap.get("billingAccountId");
		String strBillingAccountNewId = (String) hshBillingAccountMap.get("billingAccountNewId");
		Connection conn = null;
      OracleCallableStatement cstmt = null;
		String strMessage;
		String sqlStr = "BEGIN ORDERS.NP_ORDERS15_PKG.SP_UPD_SINCHRONIZE_BA(?,?,?); END;";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
         cstmt.setString(1, strBillingAccountId);
         cstmt.setString(2, strBillingAccountNewId);
         cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
         cstmt.executeUpdate();
         strMessage = cstmt.getString(3);
      }
      catch (Exception e) {
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn, cstmt, null);
      }
		return strMessage; 
	}	
	
	public HashMap getCreateContract(HashMap hshParametrosMap) throws SQLException, Exception {
		
		HashMap hshDataMap = new HashMap();
		Connection conn = null;
      OracleCallableStatement cstmt = null;
      ResultSet rs = null;
		//DbmsOutput dbmsOutput;
		String strMessage;
		String sqlStr = "BEGIN ORDERS.NP_ORDERS11_PKG.SP_GET_BSCS_CREATE_CONTRACT(?,?,?,?,?,?); END;";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
         //dbmsOutput = new DbmsOutput(conn);
         //dbmsOutput.enable(1000000);
         cstmt.setString(1, (String) hshParametrosMap.get("strOrderId"));
         //cstmt.setString(2, (String) hshParametrosMap.get("strCsIdBSCS"));
         cstmt.setString(2, (String) hshParametrosMap.get("strCustomerIdBSCS"));
         cstmt.setString(3, (String) hshParametrosMap.get("strPlanId"));
         cstmt.setString(4, (String) hshParametrosMap.get("strImei"));
         cstmt.registerOutParameter(5, OracleTypes.CURSOR);
         cstmt.registerOutParameter(6, OracleTypes.VARCHAR);
         cstmt.executeQuery();
         //dbmsOutput.show();
         //dbmsOutput.close();
         rs = (ResultSet)cstmt.getObject(5);
         strMessage = cstmt.getString(6);
         if(StringUtils.isBlank(strMessage)) {
            if(rs.next()) {
               int i = 1;
               hshDataMap.put("strCodApp", StringUtils.defaultString(rs.getString(i++)));
               hshDataMap.put("coIdTemplate", StringUtils.defaultString(rs.getString(i++)));
               hshDataMap.put("csId", StringUtils.defaultString(rs.getString(i++)));
               hshDataMap.put("eqNum", StringUtils.defaultString(rs.getString(i++)));
               hshDataMap.put("dnType", StringUtils.defaultString(rs.getString(i++)));
            }
         }
      }
      catch (Exception e) {
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn, cstmt, rs);
      }
		hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
		return hshDataMap;
	}	
}