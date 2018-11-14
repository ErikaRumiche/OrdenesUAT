package pe.com.nextel.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import oracle.sql.ARRAY;
import oracle.sql.STRUCT;

import org.apache.commons.lang.StringUtils;

import pe.com.nextel.bean.DominioBean;
import pe.com.nextel.form.OrderSearchForm;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;


public class TestDAO extends GenericDAO{

    public TestDAO() {
    }
    
    /**
     * Motivo: Obtiene la Lista de Kits
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 12/09/2007
     * @return    ArrayList de DominioBean
     */
    public ArrayList getKitsList(String param1, String param2, String param3, String param4) {
        if (GenericDAO.logger.isDebugEnabled())
            GenericDAO.logger.debug("--Inicio--");
        ArrayList arrList = new ArrayList();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        String strError = null;       
        String sqlStr = "BEGIN WEBSALES.SPI_GET_CUSTOMER_JAVA(?,?,?,?,?,?); END;";
        try {
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
			int a=1;
			cstmt.setString(a++, param1);
			cstmt.setString(a++, param2);
			cstmt.setString(a++, param3);
			cstmt.setString(a++, param4);
            cstmt.registerOutParameter(a++, OracleTypes.ARRAY, "WEBSALES.TT_CUSTOMER_LST");
            cstmt.registerOutParameter(a++, OracleTypes.VARCHAR);
            cstmt.execute();
            strError = cstmt.getString(6);
            ARRAY aryDominioList = cstmt.getARRAY(5);
            if (StringUtils.isNotEmpty(strError)) {
                logger.error(strError);
                return null;
            }
            for (int i = 0; i < aryDominioList.getOracleArray().length; i++) {
                STRUCT stcDominio = (STRUCT) aryDominioList.getOracleArray()[i];
                DominioBean objDominioBean = new DominioBean();
                objDominioBean.setValor(MiUtil.defaultBigDecimal(stcDominio.getAttributes()[0], null).toString());
                objDominioBean.setDescripcion(MiUtil.defaultBigDecimal(stcDominio.getAttributes()[1], null).toString());
                arrList.add(i, objDominioBean);
            }            
        } catch (SQLException e) {
            logger.error(formatException(e));
            strError = e.getMessage();
        } finally {
            if (cstmt != null)
                try {
                    cstmt.close();
                    conn.close();
                } catch (SQLException sqle) {
                    logger.error(formatException(sqle));
                }
        }
        return arrList;
    }
    
    /**
     * Motivo: Obtiene la Lista de Kits
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 12/09/2007
     * @return    ArrayList de DominioBean
     */
    public void getRatePlanType(int coId) {
        if (logger.isDebugEnabled())
            logger.debug("--Inicio--");
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        String sqlStr = "BEGIN APIX.SPI_ALO_GET_RATEPLANTYPE@PNEXTELP(?, ?, ?, ?, ?, ?, ?, ?, ?, ?); END;";
        try {
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            int i=1;
            cstmt.setString(i++, null);
            cstmt.setInt(i++, coId);
            cstmt.setString(i++, null);
            cstmt.setString(i++, null);
            cstmt.setString(i++, null);
            cstmt.registerOutParameter(i++, OracleTypes.NUMBER);
            cstmt.registerOutParameter(i++, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(i++, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(i++, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(i++, OracleTypes.VARCHAR);
            cstmt.execute();
            i = 6;
            logger.debug("an_tmcpde: "+cstmt.getInt(i++));
            logger.debug("av_description: "+cstmt.getString(i++));
            logger.debug("av_short_description: "+cstmt.getString(i++));
            logger.debug("av_err: "+cstmt.getString(i++));
            logger.debug("av_message: "+cstmt.getString(i++));      
        } catch (SQLException e) {
            logger.error(formatException(e));
        } finally {
            if (cstmt != null)
                try {
                    cstmt.close();
                    conn.close();
                } catch (SQLException sqle) {
                    logger.error(formatException(sqle));
                }
        }
    }
	
	public void prueba(String valor) {
        if (logger.isDebugEnabled())
            logger.debug("--Inicio--");
        Connection conn = null;
        OracleCallableStatement cstmt = null;      
        String sqlStr = "BEGIN ORDERS.NP_DEMO_TYPES_PKG.PRUEBA(?); END;";
        try {
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setString(1, valor);
			cstmt.execute();            
        } catch (SQLException e) {
			logger.error(formatException(e));
        } finally {
            if (cstmt != null)
                try {
                    cstmt.close();
                    conn.close();
                } catch (SQLException sqle) {
                    logger.error(formatException(sqle));
                }
        }
    }
	
	/**
     * Motivo:  
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 23/09/2007
     * 
     * @param		strNombreOpcion     Ej: ServiciosReparacion
     * @return		ArrayList de DominioBean      
     */
    public ArrayList getHistoryActionListByOrder(String strOrderId) {
	Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
		ArrayList arrList = new ArrayList();
		String error;
        try {
            String sqlStr = "BEGIN ORDERS.NP_ORDERS09_PKG.SP_GET_ORDER_ACTION_HISTORY(?,?,?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setString(1, strOrderId);
			cstmt.registerOutParameter(2, OracleTypes.CURSOR);
			cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeQuery();
			rs = cstmt.getCursor(2);
            error = cstmt.getString(3);
            if (StringUtils.isNotEmpty(error)) {
                throw new SQLException(error);
            }
            while (rs.next()) {
                HashMap objHashMap = new HashMap();
				int i = 1;
				objHashMap.put("swsender", rs.getString(i++));
				objHashMap.put("swaction", rs.getString(i++));
				objHashMap.put("swreceiver", rs.getString(i++));
				objHashMap.put("swmessagetype", rs.getString(i++));
				objHashMap.put("swdatereceived", rs.getString(i++));
				objHashMap.put("swactiontaken", rs.getString(i++));
				objHashMap.put("swdateactiontaken", rs.getString(i++));
				objHashMap.put("swactiontakenby", rs.getString(i++));
				objHashMap.put("swpriority", rs.getString(i++));
				objHashMap.put("swactionrequired", rs.getString(i++));
				objHashMap.put("swconfirmrequested", rs.getString(i++));
				objHashMap.put("swnote", rs.getString(i++));
				arrList.add(objHashMap);
            }
        } catch (SQLException sqle) {
            logger.error(formatException(sqle));
        } finally {
            try {
                if (conn != null)
                    conn.close();
                if (cstmt != null)
                    cstmt.close();
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return arrList;
    }
	
	/**
     * Motivo: Obtiene la Lista de Órdenes
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 27/12/2007.
     * @param     objOrderSearchForm
     * @return    ArrayList de Ordenes.
     */
    public HashMap getOrdersList(OrderSearchForm objOrderSearchForm) throws SQLException, Exception {
        if(logger.isDebugEnabled()) {
			logger.debug("--Inicio--");
			logger.debug("Flag de Búsqueda: "+objOrderSearchForm.getIFlag());
		}
	HashMap hshDataMap = new HashMap();
        ArrayList arrOrdersList = new ArrayList();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
		  ResultSet rs = null;        
        String strMessage = null;
        String sqlStr = "BEGIN ORDERS.NP_ORDERS01_PKG.SP_GET_ORDER_LST(?, ?, " + 
                                                                      "?, ?, ?, ?, ?, " +
                                                                      "?, ?, ?, ?, ?, " +
                                                                      "?, ?, ?, ?, ?, " +
                                                                      "?, ?, ?, ?, ?, " +
																	  "?, ?, ?, ?, ? " +
                                                                      "); END;";
        try{	
                conn = Proveedor.getConnection();		
		cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
		int i = 1;
		cstmt.setInt(i++, objOrderSearchForm.getIFlag());
		cstmt.setString(i++, objOrderSearchForm.getHdnCustomerId());
		cstmt.setString(i++, objOrderSearchForm.getTxtNroOrden());
		cstmt.setString(i++, objOrderSearchForm.getTxtNroSolicitud());
		cstmt.setString(i++, objOrderSearchForm.getCmbEstadoOrden());
		cstmt.setString(i++, objOrderSearchForm.getCmbDivisionNegocio());
		cstmt.setString(i++, objOrderSearchForm.getCmbSolucionNegocio());
		cstmt.setString(i++, objOrderSearchForm.getCmbCategoria());
		cstmt.setString(i++, objOrderSearchForm.getCmbSubCategoria());
	//	cstmt.setString(i++, objOrderSearchForm.getCmbZona());
	//	cstmt.setString(i++, objOrderSearchForm.getCmbConsultorEjecutivo());
		cstmt.setString(i++, objOrderSearchForm.getCmbRegion());
		cstmt.setString(i++, objOrderSearchForm.getCmbTienda());
		cstmt.setString(i++, objOrderSearchForm.getTxtCreadoPor());
		cstmt.setString(i++, objOrderSearchForm.getTxtCreateDateFrom());
		cstmt.setString(i++, objOrderSearchForm.getTxtCreateDateTill());
		cstmt.setString(i++, objOrderSearchForm.getTxtNroReparacion());
		cstmt.setString(i++, objOrderSearchForm.getTxtNextel());
		cstmt.setString(i++, objOrderSearchForm.getCmbTipoServicio());
		cstmt.setString(i++, objOrderSearchForm.getCmbModelo());
		cstmt.setString(i++, objOrderSearchForm.getCmbTipoFalla());
		cstmt.setString(i++, objOrderSearchForm.getTxtImei());
		cstmt.setString(i++, objOrderSearchForm.getCmbSituacion());
		cstmt.setString(i++, objOrderSearchForm.getCmbTecnicoResponsable());
		cstmt.setString(i++, objOrderSearchForm.getCmbEstadoReparacion());
		cstmt.registerOutParameter(i++, OracleTypes.CURSOR);
		cstmt.registerOutParameter(i++, OracleTypes.VARCHAR);
		cstmt.execute();		
		strMessage = cstmt.getString(27);
		if(StringUtils.isBlank(strMessage)) {
			rs = cstmt.getCursor(26);
			if(objOrderSearchForm.getIFlag() == 3) {
				while(rs.next()) {
					HashMap hshOrdenes = new HashMap();
					hshOrdenes.put("nporderid", StringUtils.defaultString(rs.getString("nporderid")));
					hshOrdenes.put("npordercode", StringUtils.defaultString(rs.getString("npordercode")));
					hshOrdenes.put("swruc", StringUtils.defaultString(rs.getString("swruc")));
					hshOrdenes.put("swname", StringUtils.defaultString(rs.getString("swname")));
					hshOrdenes.put("swsitename", StringUtils.defaultString(rs.getString("swsitename")));
					hshOrdenes.put("npsolutionname", StringUtils.defaultString(rs.getString("npsolutionname")));
					hshOrdenes.put("nptype", StringUtils.defaultString(rs.getString("nptype")));
					hshOrdenes.put("npspecification", StringUtils.defaultString(rs.getString("npspecification")));
					hshOrdenes.put("npstatus", StringUtils.defaultString(rs.getString("npstatus")));
					hshOrdenes.put("npsalesmanname", StringUtils.defaultString(rs.getString("npsalesmanname")));
					hshOrdenes.put("npdealername", StringUtils.defaultString(rs.getString("npdealername")));
					hshOrdenes.put("unitcount", StringUtils.defaultString(rs.getString("unitcount")));
					hshOrdenes.put("npcreatedby", StringUtils.defaultString(rs.getString("npcreatedby")));
					hshOrdenes.put("npcreateddate", MiUtil.getDate(rs.getTimestamp("npcreateddate"), "dd/MM/yyyy"));
					arrOrdersList.add(hshOrdenes);
				}
			}
			else if(objOrderSearchForm.getIFlag() == Constante.FLAG_BUSQUEDA_REPARACION) {
				while(rs.next()) {
					i = 1;				
					HashMap hshOrdenes = new HashMap();
					hshOrdenes.put("nporderid", rs.getString(i++));				//NRO ORDEN
					hshOrdenes.put("swname", rs.getString(i++));				//RAZON SOCIAL
					hshOrdenes.put("npsolutionid", rs.getString(i++));
					hshOrdenes.put("npsolutionname", rs.getString(i++));
					hshOrdenes.put("nptype", rs.getString(i++));				//CATEGORIA
					hshOrdenes.put("npspecification", rs.getString(i++));		//SUBCATEGORIA
					hshOrdenes.put("npspecificationid", rs.getString(i++));
					hshOrdenes.put("npname", rs.getString(i++));				//TIENDA
					hshOrdenes.put("npstatusorder", rs.getString(i++));				//ESTADO ORDEN
					hshOrdenes.put("nprepairid", rs.getString(i++));			//NRO REPARACION
					hshOrdenes.put("npphone", rs.getString(i++));
					hshOrdenes.put("npmodel", rs.getString(i++));
					hshOrdenes.put("npimei", rs.getString(i++));
					hshOrdenes.put("nprepairtype", rs.getString(i++));			//TIPO REPARACION
					hshOrdenes.put("npstatusrepair", rs.getString(i++));				//ESTADO REPARACION
					hshOrdenes.put("nxfalla", rs.getString(i++));				//FALLA
					hshOrdenes.put("npendsituation", rs.getString(i++));		//SITUACION FINAL
					hshOrdenes.put("npusername1", rs.getString(i++));			//TECNICO RESPONSABLE
					hshOrdenes.put("npcreateddate", MiUtil.toFecha(rs.getDate(i++)));
					hshOrdenes.put("npcreatedby", rs.getString(i++));
					arrOrdersList.add(hshOrdenes);
				}
			}
		}
               hshDataMap.put("arrOrdersList", arrOrdersList);
               hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);            
           } catch (SQLException sqle) {
            logger.error(formatException(sqle));
           }
           finally{
               closeObjectsDatabase(conn, cstmt, rs);
           }
		

        return hshDataMap;
    }
    
}
