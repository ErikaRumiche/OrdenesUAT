package pe.com.nextel.dao;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.STRUCT;
import org.apache.commons.lang.StringUtils;
import pe.com.nextel.bean.RoamingServiceBean;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.ResultSet;

/**
 * Created by montoymi on 12/08/2015.
 */
public class RoamingDAO extends GenericDAO {

    public HashMap selectServicesByPhone(long planId) throws Exception {
        if (logger.isDebugEnabled())
            logger.debug("Inicio RoamingDAO.selectServicesByPhoneAndOrderId");

        List<RoamingServiceBean> serviceBeans = new ArrayList<RoamingServiceBean>();
        String strMessage = null;

        Connection conn = null;
        OracleCallableStatement cstmt = null;
        String sqlStr = "BEGIN ORDERS.NP_ORDER_ROA_PKG.SP_GET_SERVICES_BY_PLANID(?,?,?); END;";

        try {
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);

            cstmt.setLong(1, planId);
            cstmt.registerOutParameter(2, OracleTypes.ARRAY, "ORDERS.TT_SERVICE_ROA_LST");
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);

            cstmt.execute();

            strMessage = cstmt.getString(3);
            if (StringUtils.isBlank(strMessage)) {
                ARRAY serviceArray = (ARRAY) cstmt.getObject(2);

                for (int i = 0; i < serviceArray.getOracleArray().length; i++) {
                    STRUCT serviceStruct = (STRUCT) serviceArray.getOracleArray()[i];

                    RoamingServiceBean serviceBean = new RoamingServiceBean();
                    serviceBean.setNpservicioid(serviceStruct.getOracleAttributes()[0].longValue());
                    serviceBean.setNpnomserv(serviceStruct.getOracleAttributes()[1].toString());
                    serviceBean.setBagCode(serviceStruct.getOracleAttributes()[2].toString());
                    serviceBeans.add(serviceBean);
                }
            }
        } finally {
            closeObjectsDatabase(conn, cstmt, null);
        }

        HashMap hshDataMap = new HashMap();
        hshDataMap.put("serviceBeans", serviceBeans);
        hshDataMap.put("strMessage", strMessage);
        return hshDataMap;
    }

    public HashMap validateRecurrentRoamingService(String phone, String activationDate, int validity, long orderId) throws Exception {
        if (logger.isDebugEnabled())
            logger.debug("Inicio RoamingDAO.validateRecurrentRoamingService");

        String endScheduleDate = null;
        String strMessage = null;

        Connection conn = null;
        OracleCallableStatement cstmt = null;
        String sqlStr = "BEGIN ORDERS.NP_ORDER_ROA_PKG.SP_ROA_VAL_ACT_FOR_RANGE_DAY(?,?,?,?,?,?,?,?); END;";

        try {
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);

            cstmt.setString(1, phone);
            cstmt.setString(2, activationDate);
            cstmt.setInt(3, validity);
            cstmt.setLong(4, orderId);
            cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(6, OracleTypes.VARCHAR); // av_flag -> No se utiliza.
            cstmt.registerOutParameter(7, OracleTypes.VARCHAR); // av_code -> No se utiliza.
            cstmt.registerOutParameter(8, OracleTypes.VARCHAR);

            cstmt.execute();

            endScheduleDate = cstmt.getString(5);

            // El parámetro av_message, además del mensaje de error, también guarda la fecha de ativación
            // en caso de que exista ya una orden de activación roaming con fecha de activación que se
            // traslape con la fecha dada.
            // Se limpia en el caso en que no sea error de Oracle para que no lance una excepción.
            strMessage = cstmt.getString(8);
            if (strMessage != null && strMessage.indexOf("ORA-") == -1) {
                strMessage = null;
            }
        } finally {
            closeObjectsDatabase(conn, cstmt, null);
        }

        HashMap hshDataMap = new HashMap();
        hshDataMap.put("endScheduleDate", endScheduleDate);
        hshDataMap.put("strMessage", strMessage);
        return hshDataMap;
    }

    /**
     * CFERNANDEZ [PRY-0858 - Paquete Ilimitado Whatsapp Roaming]
     * @param phone, orderId
     * @return
     * @throws Exception
     */
    public HashMap obtenerOrderRoamingService(String phone, long orderId) throws Exception {
        if (logger.isDebugEnabled())
            logger.debug("Inicio RoamingDAO.obtenerOrderRoamingService");

        HashMap hshDataMap = new HashMap();
        int strResult = 0;
        String strMessage = null;

        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        ArrayList arrList = new ArrayList();

        String sqlStr = "BEGIN ORDERS.NP_ORDER_ROA_PKG.SP_GET_ROA_ORDER_PROC_ACTIVE(?,?,?,?,?); END;";

        try {
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);

            cstmt.setString(1, phone);
            cstmt.setLong(2, orderId);
            cstmt.registerOutParameter(3, OracleTypes.CURSOR);
            cstmt.registerOutParameter(4, OracleTypes.INTEGER);
            cstmt.registerOutParameter(5, OracleTypes.VARCHAR);

            cstmt.execute();
            rs = cstmt.getCursor(3);
            strResult = cstmt.getInt(4);
            strMessage = cstmt.getString(5);

            System.out.println("objRoamingDAO.obtenerOrderRoamingService - strResult: " + strResult);

            if (strResult != 0 && strMessage == null) {

                while(rs.next()){
                    HashMap objHashMap = new HashMap();
                    int i = 1;

                    objHashMap.put("idOrder", rs.getString(i++));
                    objHashMap.put("bagCode", rs.getString(i++));
                    objHashMap.put("scheduledate", rs.getDate(i++));

                    arrList.add(objHashMap);
                }
            }
            hshDataMap.put("arrOrdersList",arrList);
            hshDataMap.put("strMessage", strMessage);
            hshDataMap.put("strResult", strResult);

        }catch (SQLException sql){
            logger.error(formatException(sql));
            hshDataMap.put("strMessage", sql.getMessage());
        }finally {
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }catch(Exception e){
                logger.error(formatException(e));
            }
        }
        return hshDataMap;
    }
}
