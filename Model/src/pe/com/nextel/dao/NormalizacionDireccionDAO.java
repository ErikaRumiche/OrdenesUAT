package pe.com.nextel.dao;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import oracle.sql.CLOB;
import pe.com.nextel.bean.*;
import pe.com.nextel.util.MiUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrador on 21/12/2015.
 */
public class NormalizacionDireccionDAO extends GenericDAO {

    /**
     * Method : insertNormalizacionLog
     * Purpose: Se encargar de registrar datos en la tabla log
     * Developer        Fecha       Comentario
     * =============    ==========  ======================================================================
     * LROQUE-GORA      19/10/2015  Creación
     */
    public String insertNormalizacionLog(DireccionNormLogBean bean) throws SQLException, Exception {
        System.out.println("[GeneralDAO][getNPValueXNameAndDesc] - INICIO");
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        String strMessage = null;
        Clob clobReq = null;
        Clob clobResp = null;
        String sqlStr = "BEGIN WEBSALES.SPI_INS_NORMALIZACION_LOG(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); END;";
        try {
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setString(1, bean.getCodigoCliente());
            cstmt.setString(2, bean.getTipoDireccion());
            cstmt.setString(3, bean.getDireccionAntigua());

            clobReq = CLOB.createTemporary(conn, false, CLOB.DURATION_SESSION);
            clobReq.setString(1, bean.getRequest());
            cstmt.setClob(4, clobReq);

            clobResp = CLOB.createTemporary(conn, false, CLOB.DURATION_SESSION);
            clobResp.setString(1, bean.getResponse());
            cstmt.setClob(5, clobResp);

            cstmt.setString(6, bean.getUsuarioApp());
            cstmt.setString(7, bean.getNombreAplicacion());
            cstmt.setDate(8, new Date(bean.getFechaHora().getTime()));
            cstmt.setString(9, bean.getNumeroOcurrencia());
            cstmt.setString(10, bean.getNumeroPedido());
            cstmt.setString(11, bean.getTiempoRespOSB());
            cstmt.setString(12, bean.getTiempoRespWS());
            cstmt.setString(13, bean.getFlagGeocodificacion());
            cstmt.setString(14, bean.getMotivoNoGeocodificacion());
            cstmt.setBigDecimal(15, bean.getIdDireccionAntigua());
            cstmt.setString(16, bean.getDescExcepcion());
            cstmt.registerOutParameter(17, OracleTypes.VARCHAR);

            cstmt.executeUpdate();

            strMessage = cstmt.getString(17);
        } catch (SQLException e) {
            logger.error(formatException(e));
            strMessage = e.getMessage();
        } catch (Exception e) {
            logger.error(formatException(e));
            strMessage = e.getMessage();
        } finally {
            if (clobReq != null) {
                clobReq.free();
            }
            if (clobResp != null) {
                clobResp.free();
            }
            try {
                closeObjectsDatabase(conn, cstmt, null);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        return strMessage;
    }

    /**
     * Method : getDepProvDist
     * Purpose: Obtiene el departamento provincia distrito
     * Developer        Fecha       Comentario
     * =============    ==========  ======================================================================
     * LROQUE-GORA      19/10/2015  Creación
     */
    public Map<String, Object> getDepProvDist(int tipoBusqueda, String codDep, String codProv) throws SQLException, Exception {
        Map<String, Object> hshDataMap = new HashMap<String, Object>();

        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strMessage = null;
        try {
            String sqlStr = "begin WEBSALES.SPI_GET_NEW_UBIGEO_LIST(?,?,?,?,?); end;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setInt(1, tipoBusqueda);
            cstmt.setString(2, codDep);
            cstmt.setString(3, codProv);
            cstmt.registerOutParameter(4, OracleTypes.CURSOR);
            cstmt.registerOutParameter(5, OracleTypes.VARCHAR);

            cstmt.executeQuery();

            strMessage = cstmt.getString(5);
            if (MiUtil.cadenaVacia(strMessage)) {
                List<NewUbigeoBean> list = new ArrayList<NewUbigeoBean>();

                rs = (ResultSet) cstmt.getObject(4);
                while (rs.next()) {
                    NewUbigeoBean bean = new NewUbigeoBean();
                    bean.setNombre(rs.getString(1));
                    bean.setDep(rs.getString(2));
                    bean.setProv(rs.getString(3));
                    bean.setDist(rs.getString(4));
                    bean.setNpubigeoid(rs.getBigDecimal(5));
                    bean.setCodigoinei(rs.getString(6));

                    list.add(bean);
                }

                hshDataMap.put("strLista", list);
            }
        } catch (SQLException e) {
            logger.error(formatException(e));
            hshDataMap.put("strMessage", e.getMessage());
        } catch (Exception e) {
            logger.error(formatException(e));
            hshDataMap.put("strMessage", e.getMessage());
        } finally {
            try {
                closeObjectsDatabase(conn, cstmt, rs);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        hshDataMap.put("strMessage", strMessage);

        return hshDataMap;
    }

    /**
     * Method : getUbigeoIneiXUbigeoReniec
     * Purpose: Se encarga de obtener los datos de inei por ubigeo reniec
     * Developer        Fecha       Comentario
     * =============    ==========  ======================================================================
     * LROQUE-GORA      19/10/2015  Creación
     */
    public Map<String, String> getUbigeoIneiXUbigeoReniec(String ubigeoReniec) throws SQLException, Exception {
        Map<String, String> hshDataMap = new HashMap<String, String>();

        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strMessage = null;
        try {
            String sqlStr = "begin WEBSALES.SPI_GET_UBINEI_X_UBRENIEC(?,?,?); end;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setString(1, ubigeoReniec);
            cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);

            cstmt.executeQuery();

            strMessage = cstmt.getString(3);
            if (MiUtil.cadenaVacia(strMessage)) {
                hshDataMap.put("ubigeoinei", cstmt.getString(2));
            }
        } catch (SQLException e) {
            logger.error(formatException(e));
            hshDataMap.put("strMessage", e.getMessage());
        } catch (Exception e) {
            logger.error(formatException(e));
            hshDataMap.put("strMessage", e.getMessage());
        } finally {
            try {
                closeObjectsDatabase(conn, cstmt, rs);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        hshDataMap.put("strMessage", strMessage);

        return hshDataMap;
    }

    /**
     * Method : getUbigeoIneiXUbigeoId
     * Purpose: Se encarga de obtener los datos de inei por id ubigeo
     * Developer        Fecha       Comentario
     * =============    ==========  ======================================================================
     * LROQUE-GORA      19/10/2015  Creación
     */
    public Map<String, String> getUbigeoIneiXUbigeoId(BigDecimal ubigeoId) throws SQLException, Exception {
        Map<String, String> hshDataMap = new HashMap<String, String>();

        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strMessage = null;
        try {
            String sqlStr = "begin WEBSALES.SPI_GET_UBINEI_X_UBIGEOID(?,?,?); end;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setBigDecimal(1, ubigeoId);
            cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);

            cstmt.executeQuery();

            strMessage = cstmt.getString(3);
            if (MiUtil.cadenaVacia(strMessage)) {
                hshDataMap.put("ubigeoinei", cstmt.getString(2));
            }
        } catch (SQLException e) {
            logger.error(formatException(e));
            hshDataMap.put("strMessage", e.getMessage());
        } catch (Exception e) {
            logger.error(formatException(e));
            hshDataMap.put("strMessage", e.getMessage());
        } finally {
            try {
                closeObjectsDatabase(conn, cstmt, rs);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        hshDataMap.put("strMessage", strMessage);

        return hshDataMap;
    }

    /**
     * Method : getDataReniecXUbigeoInei
     * Purpose: Se encarga de obtener los datos de Reniec por codigo ubigeo inei
     * Developer        Fecha       Comentario
     * =============    ==========  ======================================================================
     * LROQUE-GORA      19/10/2015  Creación
     */
    public Map<String, Object> getDataReniecXUbigeoInei(String ubigeoInei) throws SQLException, Exception {
        Map<String, Object> hshDataMap = new HashMap<String, Object>();

        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strMessage = null;
        try {
            String sqlStr = "begin WEBSALES.SPI_GET_DATOSRENIEC_X_UBINEI(?,?,?,?,?,?); end;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setString(1, ubigeoInei);
            cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(6, OracleTypes.VARCHAR);

            cstmt.executeQuery();

            strMessage = cstmt.getString(6);
            if (MiUtil.cadenaVacia(strMessage)) {
                Map<String, String> result = new HashMap<String, String>();
                result.put("ubigeoreniec", cstmt.getString(2));
                result.put("iddep", cstmt.getString(3));
                result.put("idprov", cstmt.getString(4));
                result.put("iddist", cstmt.getString(5));
                hshDataMap.put("result", result);
            }
        } catch (SQLException e) {
            logger.error(formatException(e));
            hshDataMap.put("strMessage", e.getMessage());
        } catch (Exception e) {
            logger.error(formatException(e));
            hshDataMap.put("strMessage", e.getMessage());
        } finally {
            try {
                closeObjectsDatabase(conn, cstmt, rs);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        hshDataMap.put("strMessage", strMessage);

        return hshDataMap;
    }

    /**
     * Method : getCustomerAddressNorm
     * Purpose: Obtener la lista de direcciones del cliente para la normalizacion. Los tipos de direcciones son:
     * LEGAL (DOMICILIO), FACTURACION, ENTREGA Y COMUNICACION
     * Developer        Fecha       Comentario
     * =============    ==========  ======================================================================
     * LROQUE-GORA      19/10/2015  Creación
     */
    public Map<String, Object> getCustomerAddressNorm(long intObjectId, long longRegionId, String strObjectType,
                                                      String strGeneratortype) throws Exception, SQLException {
        Map<String, Object> hshResultado = new HashMap<String, Object>();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        try {
            String sqlStr = "BEGIN WEBSALES.SPI_GET_CUST_ADDRESS_LIST_NORM(?, ?, ?, ?, ?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setLong(1, intObjectId);
            cstmt.setString(2, strObjectType);
            cstmt.setLong(3, longRegionId);
            cstmt.setString(4, strGeneratortype);
            cstmt.registerOutParameter(5, OracleTypes.CURSOR);
            cstmt.registerOutParameter(6, Types.VARCHAR);

            cstmt.executeQuery();

            String strMessage = cstmt.getString(6);
            List<DireccionClienteBean> list = new ArrayList<DireccionClienteBean>();
            if (strMessage == null) {
                rs = (ResultSet) cstmt.getObject(5);
                while (rs.next()) {
                    DireccionClienteBean bean = new DireccionClienteBean();
                    bean.setIdDireccion(rs.getBigDecimal("swaddressid"));
                    bean.setDireccion(rs.getString("swaddress1"));
                    bean.setProvincia(rs.getString("swprovince"));
                    bean.setDistrito(rs.getString("swcity"));
                    bean.setDepartamento(rs.getString("swstate"));
                    bean.setTipoDireccion(rs.getString("TIPODIRECCION"));
                    bean.setCodTipoDireccion(rs.getString("npvalue"));
                    bean.setDistritoId(rs.getString("swcityid"));
                    bean.setProvinciaId(rs.getString("swprovinceid"));
                    bean.setDepartamentoId(rs.getString("swstateid"));
                    bean.setFlagDireccion(rs.getString("swflagaddress"));
                    bean.setAddressSales(rs.getString("swaddresssales"));
                    bean.setUbigeo(rs.getString("ubigeo"));
                    bean.setEstado(rs.getString("swstatusnormalized"));
                    bean.setReferencia(rs.getString("swnote"));

                    list.add(bean);
                }
            }
            hshResultado.put("objArrayList", list);
            hshResultado.put("strMessage", strMessage);
        } catch (Exception e) {
            logger.error(formatException(e));
            hshResultado.put("strMessage", e.getMessage());
        } finally {
            try {
                closeObjectsDatabase(conn, cstmt, rs);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        return hshResultado;
    }

    /**
     * Method : updateAddressNormalize
     * Purpose: Actualizar una direccion con las direcciones normalizadas
     * Developer        Fecha       Comentario
     * =============    ==========  ======================================================================
     * LROQUE-GORA      19/10/2015  Creación
     */
    public String updateAddressNormalize(BigDecimal idAddress, ComplDireccionUpBean objCompl, DireccionNormWSBean bean)
            throws Exception, SQLException {
        OracleCallableStatement cstmt = null;
        Connection conn = null;
        String strMessage = null;
        try {
            String sqlStr = "BEGIN WEBSALES.SPI_UPDATE_NORMALIZA_ADDRESS(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setBigDecimal(1, idAddress);
            cstmt.setString(2, objCompl.getDireccionNorm());
            cstmt.setString(3, objCompl.getEstado());
            cstmt.setString(4, objCompl.getMotivo());
            cstmt.setString(5, objCompl.getUbigeo());
            cstmt.setString(6, bean.getTipoVia());
            cstmt.setString(7, bean.getNombreVia());
            cstmt.setString(8, bean.getNumeroPuerta());
            cstmt.setString(9, bean.getManzana());
            cstmt.setString(10, bean.getNombreUbanizacion());
            cstmt.setString(11, bean.getNumeroInterior());
            cstmt.setString(12, bean.getLote());
            cstmt.setString(13, bean.getTipoZona());
            cstmt.setString(14, bean.getNombreZona());
            cstmt.setString(15, objCompl.getDescDep());
            cstmt.setString(16, objCompl.getDescProv());
            cstmt.setString(17, objCompl.getDescDist());
            cstmt.setString(18, (bean.getGeolocalizacionX()==null) ? "" : String.valueOf(bean.getGeolocalizacionX()));
	        cstmt.setString(19, (bean.getGeolocalizacionY()==null) ? "" : String.valueOf(bean.getGeolocalizacionY()));
            cstmt.setDate(20, new java.sql.Date((new java.util.Date()).getTime()));
            cstmt.setString(21, bean.getTipoInterior());

            cstmt.setString(22, objCompl.getIdDep());
            cstmt.setString(23, objCompl.getIdProv());
            cstmt.setString(24, objCompl.getIdDist());

            cstmt.setString(25, bean.getReferencia());

            cstmt.setString(26, objCompl.getAddressSales());
            cstmt.setString(27, objCompl.getOperacion());
            cstmt.setString(28, objCompl.getTipoDireccion());

            cstmt.registerOutParameter(29, OracleTypes.VARCHAR);

            cstmt.executeUpdate();

            strMessage = cstmt.getString(29);

        } catch (Exception e) {
            logger.error(formatException(e));
            strMessage = e.getMessage();
        } finally {
            try {
                closeObjectsDatabase(conn, cstmt, null);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        return strMessage;
    }

    /**
     * PROCEDURE getCustomerDataClient
     * Purpose : Obtener el tipo de cliente (CUSTOMER, PROSPECT)
     * Person         Date        Comments
     * -----------   ----------  ----------
     * LROQUE-GORA   19/10/2015  Creación
     */
    public Map<String, String> getCustomerDataClient(long intObjectId) throws Exception, SQLException {
        Map<String, String> hshResultado = new HashMap<String, String>();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        try {
            String sqlStr = "BEGIN WEBSALES.SPI_GET_CUSTOMERDATACLIENTNORM(?, ?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setLong(1, intObjectId);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, Types.VARCHAR);

            cstmt.executeQuery();

            String strMessage = cstmt.getString(3);
            if (strMessage == null) {
                rs = (ResultSet) cstmt.getObject(2);
                while (rs.next()) {
                    hshResultado.put("typeclient", rs.getString("SWTYPE"));
                    break;
                }
            }
            hshResultado.put("strMessage", strMessage);
        } catch (Exception e) {
            logger.error(formatException(e));
            hshResultado.put("strMessage", e.getMessage());
        } finally {
            try {
                closeObjectsDatabase(conn, cstmt, rs);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        return hshResultado;
    }
}
