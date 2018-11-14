package pe.com.nextel.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import pe.com.nextel.bean.ApportionmentBean;
import pe.com.nextel.util.MiUtil;

import java.util.HashMap;

/**
 * Motivo: Clase DAO que contiene acceso a las tablas de orders. <br>
 * Realizado por: <a href="mailto:jorge.curi@tcs.com">Jorge Curi</a> <br>
 * Fecha: 11/07/2017
 * 
 * @see pe.com.nextel.dao.GenericDAO
 */
public class ApportionmentDAO extends GenericDAO {

	/**
     * Motivo: Insert registros del Api de Prorrateo
     * <br>Realizado por: <a href="mailto:jorge.curi@tcs.com">Jorge Curi</a> <br>
     * <br>Fecha: 11/07/2017
     *
     * @return		String
     */
	public String saveItemApi(ApportionmentBean bean, String av_login, Connection conn) throws Exception, SQLException {
		System.out.println("ApportionmentDAO/saveItemApi [TrxId] " +bean.getTrxId() + " [ItemId] " + bean.getItemId() + " [CicloOrigen] " + bean.getCicloOrigen() + " [CicloDestino] " + bean.getCicloDestino() + "[TmCode] " + bean.getTmCode() + " [TemplateId]" + bean.getTemplateId() + " [IGV]" + bean.getIgv() + " [PriceIgv]" + bean.getPriceIgv() + " [PriceApi]" + bean.getPrice() + " [RoundedPrice]" + bean.getRoundedPrice() + " [Quantity]" +bean.getQuantity() );
		String strMessage = null;
		OracleCallableStatement cstmt = null;

		try {
			String sqlStr = "BEGIN ORDERS.SPI_INS_ITEM_API_PRORRA(?,?,?,?,?,?,?,?,?,?,?,?,?); END;";			
			cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
			cstmt.setString(1, bean.getTrxId());
			cstmt.setLong(2, bean.getItemId());
			cstmt.setString(3, bean.getCicloOrigen());
			cstmt.setString(4, bean.getCicloDestino());
			cstmt.setString(5, bean.getTmCode());
			cstmt.setString(6, bean.getTemplateId());
			cstmt.setDouble(7, bean.getIgv());
			cstmt.setString(8, bean.getPriceIgv());
			cstmt.setBigDecimal(9, new BigDecimal(bean.getPrice()));
			cstmt.setString(10, bean.getRoundedPrice());
			cstmt.setInt(11, bean.getQuantity());
			cstmt.setString(12, av_login);
			cstmt.registerOutParameter(13, OracleTypes.VARCHAR);
			cstmt.executeUpdate();
			strMessage = cstmt.getString(13);
		} catch (Exception e) {
			logger.error(formatException(e));
			throw new Exception(e);
		} finally {
			try {
				closeObjectsDatabase(null,cstmt,null);
			} catch (Exception e) {
				logger.error(formatException(e));
			}
		}
		return strMessage;
	}
	
	/**
     * Motivo: Insert registros del Api de Prorrateo
     * <br>Realizado por: <a href="mailto:jorge.curi@tcs.com">Jorge Curi</a> <br>
     * <br>Fecha: 11/07/2017
     *
     * @return		String
     */
	public String saveItemApiLog(String trxId,String orderCode,String templateId, String status, String description, String accion, String request, String response, String createBy) throws Exception, SQLException {
		System.out.println("ApportionmentDAO.saveItemApiLog [trxId]" +trxId +" [orderCode] " + orderCode + " [templateId] " + templateId + " [status] " + status + " [description] " + description + " [accion] " + accion + " [createBy] " + createBy);
		System.out.println("[request] " + request);
		System.out.println("[response] " + response);
		String strMessage = null;
		Connection conn = null;
		OracleCallableStatement cstmt = null;
		ResultSet rs = null;

		try {
			String sqlStr = "BEGIN ORDERS.SP_INSERT_LOG_API_PRORRA(?,?,?,?,?,?,?,?,?,?); END;";
			conn = Proveedor.getConnection();
			cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
			cstmt.setString(1, trxId);
			cstmt.setString(2, orderCode);
			cstmt.setString(3, templateId);
			cstmt.setString(4, status);
			cstmt.setString(5, description);
			cstmt.setString(6, accion);
			cstmt.setString(7, request);
			cstmt.setString(8, response);
			cstmt.setString(9, createBy);
			cstmt.registerOutParameter(10, OracleTypes.VARCHAR);
			cstmt.executeUpdate();
			strMessage = cstmt.getString(10);
		} catch (Exception e) {
			logger.error(formatException(e));
			throw new Exception(e);
		} finally {
			try {
				conn.commit();
				closeObjectsDatabase(conn, cstmt, rs);
			} catch (Exception e) {
				logger.error(formatException(e));
			}
		}
		return strMessage;
	}
	
	/**
     * Motivo: Obtiene IGV
     * <br>Realizado por: <a href="mailto:jorge.curi@tcs.com">Jorge Curi</a> <br>
     * <br>Fecha: 09/08/2017
     *
     * @return		String
     */
	public HashMap getIgv() throws Exception, SQLException {
		System.out.println("[INI]ApportionmentDAO/getIgv");
		HashMap objHashMap = new HashMap();
		Connection conn = null;
		OracleCallableStatement cstmt = null;
		ResultSet rs = null;
		

		try {
			String sqlStr = "BEGIN ORDERS.NP_ORDER_PRORRATEO_PKG.SP_GET_IGV(?,?,?); END;";
			conn = Proveedor.getConnection();
			cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
			cstmt.registerOutParameter(1, OracleTypes.NUMBER);
			cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
			cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
			cstmt.executeUpdate();
			
			double dlIgv = cstmt.getDouble(1);
			String strcode = cstmt.getString(2);
			String strMessage = cstmt.getString(3);
			
			objHashMap.put("dlIgv",dlIgv);
		    objHashMap.put("strcode",strcode);
		    objHashMap.put("strMessage",strMessage); 
		      
		} catch (Exception e) {
			logger.error(formatException(e));
			throw new Exception(e);
		} finally {
			try {
				conn.commit();
				closeObjectsDatabase(conn, cstmt, rs);
			} catch (Exception e) {
				logger.error(formatException(e));
			}
		}
		return objHashMap;
	}
	
	public HashMap getTemplateId(String planId) throws Exception, SQLException {
		System.out.println("[INI]ApportionmentDAO/getTemplateId :::"+planId);
		HashMap objHashMap = new HashMap();		
		Connection conn = null;
		OracleCallableStatement cstmt = null;
		ResultSet rs = null;

		try {
			String sqlStr = "BEGIN ORDERS.NP_ORDER_PRORRATEO_PKG.SP_GET_TEMPLATEID(?,?,?); END;";
			conn = Proveedor.getConnection();
			cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
			
			cstmt.setInt(1, MiUtil.parseInt(planId));
			cstmt.registerOutParameter(2, OracleTypes.INTEGER);
			cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
			cstmt.executeUpdate();
			
			int templateid = cstmt.getInt(2);
			String strMessage = cstmt.getString(3);
			
			objHashMap.put("templateid",templateid);
			System.out.println("[INI]ApportionmentDAO/templateid :::"+templateid);			
		    objHashMap.put("strMessage",strMessage); 			
			System.out.println("[INI]ApportionmentDAO/strMessage :::"+strMessage);		    
		} catch (Exception e) {
			logger.error(formatException(e));
			throw new Exception(e);
		} finally {
			try {
				conn.commit();
				closeObjectsDatabase(conn, cstmt, rs);
			} catch (Exception e) {
				logger.error(formatException(e));
			}
		}
		System.out.println("[FIN]ApportionmentDAO/getTemplateId");		
		return objHashMap;
	}
	public HashMap getCustomerBscsId(String customerId ,String siteId) throws Exception, SQLException {
		System.out.println("[INI]ApportionmentDAO/getCustomerBscsId");
		HashMap objHashMap = new HashMap();		
		Connection conn = null;
		OracleCallableStatement cstmt = null;
		ResultSet rs = null;

		try {
			String sqlStr = "BEGIN ORDERS.NP_ORDER_PRORRATEO_PKG.SP_GET_CUSTOMERIDBSCS(?,?,?,?); END;";
			conn = Proveedor.getConnection();
			cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
			
			///JBALCAZAR PRY-1002
			cstmt.setInt(1, MiUtil.parseInt(customerId));
			cstmt.setInt(2, MiUtil.parseInt(siteId));			
			cstmt.registerOutParameter(3, OracleTypes.INTEGER);
			cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
			cstmt.executeUpdate();
			
			int customerBscsId = cstmt.getInt(3);
			String strMessage = cstmt.getString(4);
			///FIN JBALCAZAR PRY-1002
			
			objHashMap.put("customerBscsId",customerBscsId);
			System.out.println("[INI]ApportionmentDAO/getCustomerBscsId/customerBscsId :::"+customerBscsId);			
		    objHashMap.put("strMessage",strMessage); 			
			System.out.println("[INI]ApportionmentDAO/getCustomerBscsId/strMessage :::"+strMessage);		    
		} catch (Exception e) {
			logger.error(formatException(e));
			throw new Exception(e);
		} finally {
			try {
				conn.commit();
				closeObjectsDatabase(conn, cstmt, rs);
			} catch (Exception e) {
				logger.error(formatException(e));
			}
		}
		System.out.println("[FIN]ApportionmentDAO/getCustomerBscsId");		
		return objHashMap;
	}	
    
	public HashMap getCanalProrrateo(int hdnSpecification,String strGeneratorType, String strUser) throws Exception, SQLException {
		System.out.println("[INI]ApportionmentDAO/getCanalProrrateo");
		HashMap objHashMap = new HashMap();		
		Connection conn = null;
		OracleCallableStatement cstmt = null;
		ResultSet rs = null;

		try {
			String sqlStr = "BEGIN ORDERS.NP_ORDER_PRORRATEO_PKG.SP_EM_AC_CONFIG_PRORRATEO(?,?,?,?,?); END;";
			conn = Proveedor.getConnection();
			cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
			
			///JBALCAZAR    PRY-1002
			cstmt.setString(1, MiUtil.getString(strGeneratorType));
			cstmt.setString(2, MiUtil.getString(strUser));			
			cstmt.setInt(3, hdnSpecification);			
			cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
			cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
			cstmt.executeUpdate();
			
			String defaultImbox = cstmt.getString(4);
			String strMessage = cstmt.getString(5);
			///FIN JBALCAZAR PRY-1002
			
			objHashMap.put("defaultImbox",defaultImbox);
			System.out.println("[INI]ApportionmentDAO/getCanalProrrateo/defaultImbox :::"+defaultImbox);			
		    objHashMap.put("strMessage",strMessage); 			
			System.out.println("[INI]ApportionmentDAO/getCanalProrrateo/strMessage :::"+strMessage);		    
		} catch (Exception e) {
			logger.error(formatException(e));
			throw new Exception(e);
		} finally {
			try {
				conn.commit();
				closeObjectsDatabase(conn, cstmt, rs);
			} catch (Exception e) {
				logger.error(formatException(e));
			}
		}
		System.out.println("[FIN]ApportionmentDAO/getCanalProrrateo");		
		return objHashMap;
	}	
	
}
