package pe.com.nextel.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.HashMap;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import org.apache.commons.lang.StringUtils;

import pe.com.nextel.bean.SubRegCustomerInfoBean;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;


public class SubRegCustomerInfoDAO extends GenericDAO
{
  public SubRegCustomerInfoDAO()
  {
  }
  
  public  String insertCustomerInfo(SubRegCustomerInfoBean customerInfoBean,java.sql.Connection conn) throws SQLException {
    String strMessage=null;
    OracleCallableStatement cstmt = null;
    try{
      String sqlStr =  "BEGIN ORDERS.NP_ORDERS31_PKG.SP_INSERT_SUBREG_ORDER(?, ?, ?, ?," +
                                                                           "?, ?); END;";
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      
      cstmt.setLong(1, customerInfoBean.getCustomerId());
      cstmt.setLong(2, customerInfoBean.getOrderId());
      cstmt.setString(3, customerInfoBean.getStrRazonSoc());
      cstmt.setString(4, customerInfoBean.getStrPhone());
      cstmt.setString(5, customerInfoBean.getStrLogin());
      
      cstmt.registerOutParameter(6, OracleTypes.VARCHAR);
      cstmt.execute();
      
      strMessage = cstmt.getString(6);
      
    } catch (Exception e) {   
      strMessage = e.getMessage();
    } finally{
      try{
        closeObjectsDatabase(null, cstmt, null);  
      }catch (Exception e) {
        logger.error(formatException(e));
      }
    }
    return strMessage;
  }
  
  public  String updatetCustomerInfo(SubRegCustomerInfoBean customerInfoBean,java.sql.Connection conn) throws SQLException {
    String strMessage=null;
    OracleCallableStatement cstmt = null;
    try{
      String sqlStr =  "BEGIN ORDERS.NP_ORDERS31_PKG.SP_UPDATE_SUBREG_ORDER(?, ?, ?, ?," +
                                                                           "?, ?, ?); END;";
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      
      cstmt.setLong(1, customerInfoBean.getCustomerId());
      cstmt.setLong(2, customerInfoBean.getOrderId());
      cstmt.setString(3, customerInfoBean.getStrRazonSoc());
      cstmt.setString(4, customerInfoBean.getStatusOrder());
      cstmt.setString(5, customerInfoBean.getStrPhone());
      cstmt.setString(6, customerInfoBean.getStrLogin());
      
      cstmt.registerOutParameter(7, OracleTypes.VARCHAR);
      cstmt.execute();
      
      strMessage = cstmt.getString(7);
      
    } catch (Exception e) {   
      strMessage = e.getMessage();
    } finally{
      try{
        closeObjectsDatabase(null, cstmt, null);  
      }catch (Exception e) {
        logger.error(formatException(e));
      }
    }
    return strMessage;
  }
  
  public  HashMap isExistOrderSubReg(long lCustomerId, long lIncidentId,java.sql.Connection conn) throws SQLException {
    HashMap hshDataMap = new HashMap();
    String strMessage=null;
    OracleCallableStatement cstmt = null;
    int exist = 0;
    try{
      String sqlStr =  "BEGIN ORDERS.NP_ORDERS31_PKG.SP_GET_FLG_SUBREG_ORDER(?, ?, ?, ?); END;";
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      
      cstmt.setLong(1, lCustomerId);
      cstmt.setLong(2, lIncidentId);
      cstmt.registerOutParameter(3, OracleTypes.NUMBER);
      cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
      cstmt.execute();
      
      strMessage = cstmt.getString(4);
      if(strMessage==null){
          exist = cstmt.getInt(3);
          hshDataMap.put("an_exist",exist+"");
      }
      
    } catch (Exception e) {   
      strMessage = e.getMessage();
    } finally{
      try{
        closeObjectsDatabase(null, cstmt, null);  
      }catch (Exception e) {
        logger.error(formatException(e));
      }
    }
    hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
    
    return hshDataMap;
  }
  
  public HashMap getOrderSubReg(long lOrderId)  throws SQLException, Exception {
    HashMap hshDataMap = new HashMap();
    Connection conn = null;
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
		String strMessage=null;
		SubRegCustomerInfoBean customerInfoBean = new SubRegCustomerInfoBean();
    try {
      String sqlStr = "BEGIN ORDERS.NP_ORDERS31_PKG.SP_GET_SUBREG_ORDER(?,?,?); END;";
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
		
      cstmt.setLong(1, lOrderId);
      cstmt.registerOutParameter(2, OracleTypes.CURSOR);
      cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
      cstmt.executeQuery();
		  
      strMessage = cstmt.getString(3);
      
      if (!StringUtils.isNotEmpty(strMessage)) {
        rs = cstmt.getCursor(2);
        if (rs.next()) {
          int i = 1;
          customerInfoBean.setStrRazonSoc(StringUtils.defaultString(rs.getString(i++),""));
          customerInfoBean.setCustomerId(MiUtil.parseLong(rs.getString(i++)));
          customerInfoBean.setStrPhone(StringUtils.defaultString(rs.getString(i++),""));
        }
        hshDataMap.put("customerInfoBean",customerInfoBean);
      }
    }catch (Exception e) {
        logger.error(formatException(e));
        strMessage = e.getMessage();
    }finally {
        closeObjectsDatabase(conn, cstmt, rs);
    }
    
    hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
    
    return hshDataMap;
	}
  
  public HashMap getPhoneSubReg(long lCustomerId,long lIncidentId)  throws SQLException, Exception {
    HashMap hshDataMap = new HashMap();
    Connection conn = null;
    OracleCallableStatement cstmt = null;
		String strMessage=null;
    String strPhone=null;
		
    try {
      String sqlStr = "BEGIN ORDERS.NP_ORDERS31_PKG.SP_GET_NUM_SUBREG(?,?,?); END;";
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
		
      cstmt.setLong(1, lIncidentId);
      cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
      cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
      cstmt.executeQuery();
		  
      strMessage = cstmt.getString(3);
      
      if (!StringUtils.isNotEmpty(strMessage)) {
        strPhone = cstmt.getString(2);
      }
      hshDataMap.put("strPhone",strPhone);
    }catch (Exception e) {
        logger.error(formatException(e));
        strMessage = e.getMessage();
    }finally {
        closeObjectsDatabase(conn, cstmt, null);
    }
    
    hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
    
    return hshDataMap;
	}
  
  
  
}