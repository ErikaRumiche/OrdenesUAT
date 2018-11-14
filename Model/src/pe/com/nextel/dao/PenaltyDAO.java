package pe.com.nextel.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

import org.apache.commons.lang.StringUtils;

import pe.com.nextel.bean.ConfigurationBean;
import pe.com.nextel.bean.ItemBean;
import pe.com.nextel.bean.ListBoxBean;
import pe.com.nextel.bean.PenaltyBean;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;


/**
 * Motivo: Clase DAO que contiene acceso a las tablas de Addedum.
 * <br>Realizado por: <a href="mailto:jorge.gabriel@teamsoft.com.pe">Jorge Gabriel</a>
 * <br>Fecha: 19/05/2016
 * @see pe.com.nextel.dao.GenericDAO
 */
public class PenaltyDAO extends GenericDAO {

    /**
     * Motivo: Obtiene la Penalidad Simulada para un numero de telefono de un Cliente
     * <br>Realizado por: <a href="mailto:daniel.erazo@hpe.com">Daniel Erazo</a>
     * <br>Fecha: 10/05/2016
     *
     * @return		HashMap
     */
    public HashMap getPenaltySimulator(String strTelefono, String strCustomerId) throws SQLException, Exception {
        HashMap objHashMap = new HashMap();
        HashMap hshDatosPenalidad = new HashMap();
        String strMessage;
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String sqlStr = "BEGIN ADDENDUM.NP_ADDENDUM_CONTRACT_PKG.SP_GET_PENALTY_SIMULATOR(?, ?, ?, ?); END;";
        try{
            long customerId = MiUtil.parseLong(strCustomerId);
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setLong(1, customerId);
            cstmt.setString(2, strTelefono);
            cstmt.registerOutParameter(3, OracleTypes.STRUCT, "ADDENDUM.TO_PENALTY_SIMULATOR");
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            strMessage = cstmt.getString(4);

            if(strMessage == null){
                STRUCT struct = (STRUCT)cstmt.getObject(3);
                Object[] penaltyTO = struct.getAttributes();

                for(int i=0; i<penaltyTO.length;i++) System.out.println("penaltyTO["+i+"]"+penaltyTO[i]);

                hshDatosPenalidad.put("num_adenda", penaltyTO[1].toString());
                hshDatosPenalidad.put("plazo", penaltyTO[2].toString());
                hshDatosPenalidad.put("nom_equipo", penaltyTO[3].toString());
                hshDatosPenalidad.put("fecha_inicio", MiUtil.getDate((Date)penaltyTO[4],"dd/MM/yyyy"));
                hshDatosPenalidad.put("fecha_fin", MiUtil.getDate((Date)penaltyTO[5],"dd/MM/yyyy"));
                hshDatosPenalidad.put("total_dias", penaltyTO[6].toString());
                hshDatosPenalidad.put("dias_efectivos", penaltyTO[7].toString());
                hshDatosPenalidad.put("penalidad_simulada", penaltyTO[8].toString());
                hshDatosPenalidad.put("monto_beneficio",penaltyTO[15].toString());
            }

            System.out.println("[PenaltyDAO.getPenaltySimulator] - ErrorMsg: "+strMessage);
        }
        catch(Exception e){
            throw new Exception(e);
        }
        finally{
            closeObjectsDatabase(conn, cstmt, rs);
        }
        objHashMap.put("datosPenalidad", hshDatosPenalidad);
        objHashMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return objHashMap;
    }

    /**
     * Motivo: Obtiene las penalidades para un conjunto de telefonos de un Cliente
     * <br>Realizado por: <a href="mailto:daniel.erazo@hpe.com">Daniel Erazo</a>
     * <br>Fecha: 10/05/2016
     * @Param String strObjectType : Afecta a strObject 'CAD_NUMBER' : Cadena de numeros, 'ORDER': Orden
     * @Param String strObject     : Cadena de numeros separado por | o Id de Orden
     * @return		HashMap
     */
    public HashMap getPenaltyListByPhone(String strObjectType,String strObject, String strCustomerId) throws SQLException, Exception {
        HashMap objHashMap = new HashMap();
        ArrayList<PenaltyBean> penaltyList = new ArrayList<PenaltyBean>();
        String strMessage;
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        long fastOrderId = 0;
        String sqlStr = "BEGIN ADDENDUM.NP_ADDENDUM_CONTRACT_PKG.SP_GET_PENALTY_LST(?, ?, ?, ?, ?); END;";
        try{
            long customerId = MiUtil.parseLong(strCustomerId);
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setLong(1, customerId);
            cstmt.setString(2, strObjectType);
            cstmt.setString(3, strObject);
            cstmt.registerOutParameter(4, OracleTypes.ARRAY, "ADDENDUM.TT_PENALTY_SIMULATOR");
            cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            strMessage = cstmt.getString(5);

            if(strMessage == null){
                ARRAY array = (ARRAY)cstmt.getObject(4);
                if(array!=null){
                    for (int i = 0; i < array.getOracleArray().length; i++) {
                        STRUCT struct = (STRUCT) array.getOracleArray()[i];
                        Object[] penaltyTO = struct.getAttributes();
                        PenaltyBean penaltyBean = new PenaltyBean();

                        penaltyBean.setTelefono(penaltyTO[0].toString());
                        penaltyBean.setNumAdenda(penaltyTO[1].toString());
                        penaltyBean.setPlazo(Integer.valueOf(penaltyTO[2].toString()));
                        penaltyBean.setNomEquipo(penaltyTO[3].toString());
                        penaltyBean.setFechaInicio(MiUtil.getDate((Date) penaltyTO[4], "dd/MM/yyyy"));
                        penaltyBean.setFechaFin(MiUtil.getDate((Date) penaltyTO[5], "dd/MM/yyyy"));
                        penaltyBean.setDiasTotales(Integer.valueOf(penaltyTO[6].toString()));
                        penaltyBean.setDiasEfectivos(Integer.valueOf(penaltyTO[7].toString()));
                        penaltyBean.setPenalidad(Double.valueOf(penaltyTO[8].toString()));
                        penaltyBean.setMontoFinal(Double.valueOf(penaltyTO[9].toString()));
                        penaltyBean.setHabilitado(Integer.parseInt(penaltyTO[10].toString()));
                        String motivo = (penaltyTO[11]!=null)? penaltyTO[11].toString():"0";
                        penaltyBean.setMotivo(Integer.parseInt(motivo));
                        long fOrderId = MiUtil.parseLong(penaltyTO[12].toString());
                        penaltyBean.setFastOrderId(fOrderId);
                        if(MiUtil.parseLong(penaltyTO[12].toString()) > 0){
                            fastOrderId = fOrderId;
                        }
                        penaltyBean.setNumItemPenaltyId(penaltyTO[13].toString());
                        penaltyBean.setNumImeiId(penaltyTO[14].toString());
                        penaltyBean.setMontoBeneficio(Double.valueOf(penaltyTO[15].toString()));
                        penaltyList.add(penaltyBean);
                    }
                }
            }

            System.out.println("[PenaltyDAO.getPenaltyListByPhone] - ErrorMsg: "+strMessage);
        }
        catch(Exception e){
            throw new Exception(e);
        }
        finally{
            closeObjectsDatabase(conn, cstmt, rs);
        }
        objHashMap.put("penalidadList", penaltyList);
        objHashMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        objHashMap.put("fastOrderId",fastOrderId);

        return objHashMap;
    }

    /**
     * Motivo: Obtiene un flag general para mostrar o no la funcionalidad de pago de penalidades
     * <br>Realizado por: <a href="mailto:daniel.erazo@hpe.com">Daniel Erazo</a>
     * <br>Fecha: 10/05/2016
     *
     * @return		HashMap
     */
    public HashMap getFlagShowPenaltyFunctionality(String strOrderId, String strSpecificationId, String strStatus, String strUser) throws SQLException, Exception {
        HashMap objHashMap = new HashMap();
        int flag = 0;
        String strMessage;
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        ArrayList<ListBoxBean> listBoxList = new ArrayList<ListBoxBean>();

        String sqlStr = "BEGIN ADDENDUM.NP_ADDENDUM_CONFIGURATION_PKG.SP_SHOW_PENALTY_FUNCTIONALITY(?, ?, ?, ?, ?, ?, ?); END;";
        try{
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

            long orderId = MiUtil.parseLong(strOrderId);
            cstmt.setLong(1, orderId);
            cstmt.setString(2, strSpecificationId);
            cstmt.setString(3, strStatus);
            cstmt.setString(4, strUser);
            cstmt.registerOutParameter(5, Types.NUMERIC);
            cstmt.registerOutParameter(6, OracleTypes.CURSOR);
            cstmt.registerOutParameter(7, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            strMessage = cstmt.getString(7);
            flag = cstmt.getInt(5);

            System.out.println("[PenaltyDAO.getFlagShowPenaltyFunctionality] - flag: "+flag);

            if(strMessage == null){
                rs = (ResultSet)cstmt.getObject(6);
                if(rs != null){
                    while(rs.next()){
                        int i = 0;
                        ListBoxBean listBoxBean = new ListBoxBean();
                        listBoxBean.setIdListBox(rs.getInt(++i));
                        listBoxBean.setDescListBox(StringUtils.defaultString(rs.getString(++i)));
                        listBoxList.add(listBoxBean);
                    }
                }
            }

            System.out.println("[PenaltyDAO.getFlagShowPenaltyFunctionality] - Tamanio list: "+listBoxList.size());
            System.out.println("[PenaltyDAO.getFlagShowPenaltyFunctionality] - ErrorMsg: "+strMessage);
        }
        catch(Exception e){
            throw new Exception(e);
        }
        finally{
            closeObjectsDatabase(conn, cstmt, null);
        }
        objHashMap.put("flag", flag);
        objHashMap.put("msgList", listBoxList);
        objHashMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return objHashMap;
    }

    /**
     * Motivo:
     * 1. Obtiene un flag para saber si se va a mostrar o no el link de pagar penalidad
     * 2. Permite saber si se puede habilitar el boton de generar orden de pago o anular
     * <br>Realizado por: <a href="mailto:daniel.erazo@hpe.com">Daniel Erazo</a>
     * <br>Fecha: 10/05/2016
     * @Param String option Opciones de OPTION_SHOW_PAY_PENALTY o OPTION_EDIT_ORDER
     * @Param String strOrderId : Id de la orden
     * @return		HashMap
     */
    public HashMap verifAddendumPenalty(String option, String strOrderId, String strUser) throws SQLException, Exception {
        HashMap objHashMap = new HashMap();
        int flag_avanzar    = 0; //Avanzar por defecto no avanza
        int flag_anular     = 0; //Boton anular por defecto deshabilitado
        int flag_order_pago = 0; //Boton generar orden de pago por defecto deshabilitado
        String strMessage;
        Connection conn = null;
        OracleCallableStatement cstmt = null;

        String sqlStr = "BEGIN ADDENDUM.NP_ADDENDUM_PKG.SP_VERIF_ADDENDUM_PENALTY(?, ?, ?, ?, ?, ?, ?); END;";
        try{
            long orderId = MiUtil.parseLong(strOrderId);
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

            cstmt.setString(1, option);
            cstmt.setLong(2, orderId);
            cstmt.setString(3, strUser);
            cstmt.registerOutParameter(4, Types.NUMERIC);
            cstmt.registerOutParameter(5, Types.NUMERIC);
            cstmt.registerOutParameter(6, Types.NUMERIC);
            cstmt.registerOutParameter(7, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            strMessage = cstmt.getString(7);
            flag_avanzar = cstmt.getInt(4);
            flag_anular = cstmt.getInt(5);
            flag_order_pago = cstmt.getInt(6);
            System.out.println("[PenaltyDAO.verifAddendumPenalty] - flag_avanzar: "      + flag_avanzar);
            System.out.println("[PenaltyDAO.verifAddendumPenalty] - flag_anular: "       + flag_anular);
            System.out.println("[PenaltyDAO.verifAddendumPenalty] - flag_order_pago: "   + flag_order_pago);
            System.out.println("[PenaltyDAO.verifAddendumPenalty] - ErrorMsg: "+ strMessage);
        }
        catch(Exception e){
            throw new Exception(e);
        }
        finally{
            closeObjectsDatabase(conn, cstmt, null);
        }
        objHashMap.put("FLAG_AVANZAR", flag_avanzar);
        objHashMap.put("FLAG_ANULAR", flag_anular);
        objHashMap.put("FLAG_GENERAR_ORDEN_PAGO", flag_order_pago);
        objHashMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return objHashMap;
    }

    /**
     * Motivo: Valida si la especificacion mostrara la penalidad simulada al editar producto
     * <br>Realizado por: <a href="mailto:daniel.erazo@hpe.com">Daniel Erazo</a>
     * <br>Fecha: 10/05/2016
     *
     * @return		HashMap
     */
    public HashMap getFlagShowPenaltySimulatorEdit(String strCustomerId, String strPhone) throws SQLException, Exception {
        HashMap objHashMap = new HashMap();
        int flag = 0;
        String strMessage;
        Connection conn = null;
        OracleCallableStatement cstmt = null;

        String sqlStr = "BEGIN ADDENDUM.NP_ADDENDUM_CONFIGURATION_PKG.SP_SHOW_PENALTY_SIM_BY_PHONE(?, ?, ?, ?); END;";
        try{
            long customerId = Integer.parseInt(strCustomerId);

            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

            cstmt.setLong(1, customerId);
            cstmt.setString(2, strPhone);
            cstmt.registerOutParameter(3, OracleTypes.NUMERIC);
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            strMessage = cstmt.getString(4);
            flag = cstmt.getInt(3);

            System.out.println("[PenaltyDAO.getFlagShowPenaltySimulatorEdit] - flag: "+flag);
            System.out.println("[PenaltyDAO.getFlagShowPenaltySimulatorEdit] - ErrorMsg: "+strMessage);
        }
        catch(Exception e){
            throw new Exception(e);
        }
        finally{
            closeObjectsDatabase(conn, cstmt, null);
        }
        objHashMap.put("flag", flag);
        objHashMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return objHashMap;
    }

    /**
     * Motivo: Permite obtener un listado de valores para una configuracion
     * <br>Realizado por: <a href="mailto:daniel.erazo@hpe.com">Daniel Erazo</a>
     * <br>Fecha: 10/05/2016
     *
     * @return		HashMap
     */
    public HashMap getConfigurationList(String strConfiguration) throws SQLException, Exception {
        HashMap objHashMap = new HashMap();
        String strMessage;
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        ArrayList<ListBoxBean> listBoxList = new ArrayList<ListBoxBean>();

        String sqlStr = "BEGIN ADDENDUM.NP_ADDENDUM_CONFIGURATION_PKG.SP_GET_CONFIGURATION_LST(?, ?, ?); END;";
        try{
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

            cstmt.setString(1, strConfiguration);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            strMessage = cstmt.getString(3);

            if(strMessage == null){
                rs = (ResultSet)cstmt.getObject(2);
                if(rs != null){
                    while(rs.next()){
                        int i = 0;
                        ListBoxBean listBoxBean = new ListBoxBean();
                        listBoxBean.setIdListBox(rs.getInt(++i));
                        listBoxBean.setDescListBox(StringUtils.defaultString(rs.getString(++i)));
                        listBoxList.add(listBoxBean);
                    }
                }
            }

            System.out.println("[PenaltyDAO.getConfigurationList] - Tamanio list: "+listBoxList.size());
            System.out.println("[PenaltyDAO.getConfigurationList] - ErrorMsg: "+strMessage);
        }
        catch(Exception e){
            throw new Exception(e);
        }
        finally{
            closeObjectsDatabase(conn, cstmt, rs);
        }
        objHashMap.put("listBoxList", listBoxList);
        objHashMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return objHashMap;
    }

    /**
     * Motivo: Permite insertar/editar/eliminar un item en la tabla
     * <br>Realizado por: <a href="mailto:daniel.erazo@hpe.com">Daniel Erazo</a>
     * <br>Fecha: 10/05/2016
     *
     * @return		HashMap
     */
    public String doUpdateItemPenalty(Connection conn, ItemBean itemBean, long hasPenalty, String user, String operatorTransaction) throws SQLException, Exception {
        String strMessage;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;

        System.out.println("[PenaltyDAO.doUpdateItemPenalty]- OPERATOR: "+operatorTransaction);

        long orderId = itemBean.getNporderid();
        long itemId = itemBean.getNpitemid();
        String phone = itemBean.getNpphone();

        String sqlStr = "BEGIN ADDENDUM.NP_ADDENDUM_CONTRACT_PKG.SP_UPDATE_ADDEM_ITEM_PENALTY(?, ?, ?, ?, ?, ?, ?); END;";
        try{
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

            cstmt.setString(1, operatorTransaction);
            cstmt.setLong(2, orderId);
            cstmt.setLong(3, itemId);
            cstmt.setString(4, phone);
            cstmt.setLong(5, hasPenalty);
            cstmt.setString(6, user);
            cstmt.registerOutParameter(7, OracleTypes.VARCHAR);
            cstmt.executeUpdate();
            strMessage = cstmt.getString(7);

            System.out.println("[PenaltyDAO.doUpdateItemPenalty]- ErrorMsg: "+strMessage);

            if(strMessage!=null) return strMessage;
        }
        catch(Exception e){
            logger.error(formatException(e));
            strMessage = e.getClass() + " - " + e.getMessage();
        }
        finally{
            try{
                closeObjectsDatabase(null,cstmt,rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        return strMessage;
    }


    /**
     Method : getOrderPenaltyByParentOrderId
     *
     * Motivo: Obtiene la orden de penalidad hijo a partir de la orden padre.
     * <br>Realizado por: <a href="mailto:eddy.flores@hpe.com">Eddy Flores</a>
     * <br>Fecha: 19/05/2016
     * @return HashMap que contiene el id del hijo y un mensaje de error en caso no exista la orden.
     */
    public HashMap getOrderPenaltyByParentOrderId(long npparentorderid) throws SQLException, Exception{
        HashMap    hshDataMap         = new HashMap();
        Connection conn               = null;
        OracleCallableStatement cstmt = null;
        try{
            String sqlStr = "BEGIN ADDENDUM.SPI_GET_PENALTY_ORDER_ID(?, ?, ?); END;";
            conn =  Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setLong(1, npparentorderid);
            cstmt.registerOutParameter(2, OracleTypes.NUMBER);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            hshDataMap.put("nporderid", cstmt.getLong(2));
            hshDataMap.put(Constante.MESSAGE_OUTPUT, cstmt.getString(3));
        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        return hshDataMap;
    }

    /**
     Method : updateFastOrderIdPenalty
     *
     * Motivo: Actualiza el id de la orden de pago generada para la penalidad Addendum.
     * <br>Realizado por: <a href="mailto:jorge.gabriel@teamsoft.com.pe">Jorge Gabriel</a>
     * <br>Fecha: 30/05/2016
     * @return String
     */
    public String updateFastOrderIdPenalty(long lOrderId, long lFastOrderId, String strLogin) throws SQLException, Exception {
        String    strMessage          = null;
        Connection conn               = null;
        OracleCallableStatement cstmt = null;
        try{
            String sqlStr = "BEGIN ADDENDUM.NP_ADDENDUM_CONTRACT_PKG.SP_UPD_FASTORDERID_ITEM_PENAL(?, ?, ?, ?); END;";
            conn =  Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setLong(1, lOrderId);
            if (lFastOrderId == 0){
                cstmt.setNull(2,OracleTypes.NUMBER);
            }else{
                cstmt.setLong(2, lFastOrderId);
            }
            cstmt.setString(3, strLogin);
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            strMessage = cstmt.getString(4);

            System.out.println("[PenaltyDAO.updateFastOrderIdPenalty] - ErrorMsg: "+strMessage);

        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        return strMessage;
    }

    /**
     Method : saveOrderPenalty
     *
     * Motivo: Crea la orden de pago de penalidad
     * <br>Realizado por: <a href="mailto:jorge.gabriel@teamsoft.com.pe">Jorge Gabriel</a>
     * <br>Fecha: 31/05/2016
     * @return HashMap que el id del incidente creado.
     */
    public HashMap saveOrderPenalty(long lOrderId, String strPaymentterms, String strLogin, List<PenaltyBean> penaltyList) throws SQLException, Exception {
        CategoryDAO    objCategoryDAO  = new CategoryDAO();;
        HashMap        hshDataMap      = new HashMap();
        HashMap        hshData         = new HashMap();
        Connection     conn            = null;
        OracleCallableStatement cstmt  = null;

        long longSpecificationId;
        int flagAmountZero = 0;
        String strMessage = null;
        try{
            conn =  Proveedor.getConnection();

            StructDescriptor structDescriptor = StructDescriptor.createDescriptor("ADDENDUM.TO_PENALTY_SIMULATOR", conn);
            List<STRUCT> structs = new ArrayList<STRUCT>();
            for (PenaltyBean penaltyBean : penaltyList) {
                Object[] penalty =  {
                        null,                         //wv_npphone
                        null,                         //wn_npaddendumid
                        null,                         //wn_npaddendumterm
                        null,                         //wv_equipmentname
                        null,                         //wd_npusedateinit
                        null,                         //wd_npusedateend
                        null,                         //wn_nptotaldays
                        null,                         //wn_npeffectivedays
                        null,                         //wn_penalty
                        penaltyBean.getMontoFinal(),  //wn_montofinal
                        null,                         //wn_enabled
                        penaltyBean.getMotivo(),      //wn_motiveid
                        null,                         //wn_npfastorderid
                        penaltyBean.getNumItemPenaltyId(), //wn_npitempenaltyid     ,
                        null,                          //wn_npitemid
                        null                           //wn_npbenefitamount
                };
                STRUCT itemPenaltyStruct = new STRUCT(structDescriptor, conn, penalty);
                structs.add(itemPenaltyStruct);
            }
            ArrayDescriptor arrayDescriptor = ArrayDescriptor.createDescriptor("ADDENDUM.TT_PENALTY_SIMULATOR", conn);
            ARRAY penaltyArray = new ARRAY(arrayDescriptor, conn, structs.toArray());

            String sqlStr = "BEGIN ORDERS.SPI_CREATE_PENALTY_ORDER(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); END;";

            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setLong(1, lOrderId);
            cstmt.setString(2, strPaymentterms);
            cstmt.setString(3, strLogin);
            cstmt.setARRAY(4, penaltyArray);
            cstmt.registerOutParameter(5, OracleTypes.NUMBER);
            cstmt.registerOutParameter(6, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(7, OracleTypes.NUMBER);
            cstmt.registerOutParameter(8, OracleTypes.NUMBER);
            cstmt.registerOutParameter(9, OracleTypes.NUMBER);
            cstmt.registerOutParameter(10, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(11, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            hshDataMap.put("lOrderPenaltyId",cstmt.getLong(5));
            System.out.println("lOrderPenaltyId : "+cstmt.getLong(5));
            hshDataMap.put("strOrderPenaltyStatus", cstmt.getString(6));
            System.out.println("strOrderPenaltyStatus : "+cstmt.getString(6));
            hshDataMap.put("strDivisionId", cstmt.getLong(7));
            System.out.println("strDivisionId : "+cstmt.getLong(7));
            longSpecificationId = cstmt.getLong(8);
            System.out.println("longSpecificationId : "+longSpecificationId);
            flagAmountZero = cstmt.getInt(9);
            System.out.println("flagAmountZero : "+flagAmountZero);
            hshDataMap.put("flagAmountZero", flagAmountZero);
            if(flagAmountZero == 1){
                hshDataMap.put("msgFlagAmountZero", cstmt.getString(10));
            }

            strMessage = cstmt.getString(11);
            if( strMessage!=null){
                if (conn != null) conn.rollback();
                hshDataMap.put(Constante.MESSAGE_OUTPUT,strMessage);
                return hshDataMap;
            }

            if(flagAmountZero == 0){//Solo si es una orden con monto diferente de 0
                //Inicio Mod CGC  - Datos para BPEL
                hshData=objCategoryDAO.getSpecificationDetail(longSpecificationId,conn);
                strMessage=(String)hshData.get("strMessage");
                if( strMessage!=null){
                    if (conn != null) conn.rollback();
                    hshDataMap.put(Constante.MESSAGE_OUTPUT,strMessage);
                    return hshDataMap;
                }

                hshDataMap.put("objSpecificationBean",hshData.get("objSpecifBean"));
                hshDataMap.put(Constante.MESSAGE_OUTPUT,strMessage);
            }
        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        return hshDataMap;
    }

    /**
     Method : getOrderPenaltyByParentOrderId
     *
     * Motivo: Obtiene la orden de penalidad hijo a partir de la orden padre.
     * <br>Realizado por: <a href="mailto:eddy.flores@hpe.com">Eddy Flores</a>
     * <br>Fecha: 19/05/2016
     * @return HashMap que contiene el string de la pagina detalle a usarse en un popup.
     */
    public HashMap getPageOrderById(int userId,int appId,long nporderid) throws SQLException, Exception{
        HashMap    hshDataMap         = new HashMap();
        Connection conn               = null;
        OracleCallableStatement cstmt = null;
        try{
            String url = "";
            String sqlStr = "BEGIN ORDERS.SPI_GET_PAGE_ORDER_BY_ID(?,?,?,?,?); END;";
            conn =  Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setInt(1, userId);
            cstmt.setInt(2, appId);
            cstmt.setLong(3, nporderid);
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            if(cstmt.getString(4) != null){
                url = cstmt.getString(4);
            }
            hshDataMap.put("av_url", url);
            hshDataMap.put(Constante.MESSAGE_OUTPUT, cstmt.getString(5));
        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        return hshDataMap;
    }
    
    /**
     * Motivo: Permite obtener un listado de valores de configuracion
     * <br>Realizado por: <a href="mailto:carlos.diazoliva@tcs.com">Carlos Diaz</a>
     * <br>Fecha: 10/04/2017
     *
     * @return		HashMap
     */
    public HashMap getConfigurationValues(String strConfiguration, String strValue, String strValue2) throws SQLException, Exception {
        HashMap objHashMap = new HashMap();
        String strMessage;
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        ArrayList<ConfigurationBean> listConfiguration = new ArrayList<ConfigurationBean>();
        ConfigurationBean configurationBean = null;

        String sqlStr = "BEGIN ADDENDUM.NP_ADDENDUM_CONFIGURATION_PKG.SP_GET_CONFIGURATION(?, ?, ?, ?, ?); END;";
        try{
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

            cstmt.setString(1, strConfiguration);
            cstmt.setString(2, strValue);
            cstmt.setString(3, strValue2);
            cstmt.registerOutParameter(4, OracleTypes.CURSOR);
            cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            strMessage = cstmt.getString(5);

            if(strMessage == null){
                rs = (ResultSet)cstmt.getObject(4);
                if(rs != null){
                    while(rs.next()){
                        configurationBean = new ConfigurationBean();
                        configurationBean.setNpValue(rs.getString("npvalue1"));
                        configurationBean.setNpTag1(rs.getString("npvalue2"));
                        configurationBean.setNpTag2(rs.getString("npvalue3"));
                        configurationBean.setNpTag3(rs.getString("npvalue4"));
                        listConfiguration.add(configurationBean);
                    }
                }
            }

            System.out.println("[PenaltyDAO.getConfigurationValues] - Tamanio list: "+listConfiguration.size());
            System.out.println("[PenaltyDAO.getConfigurationValues] - ErrorMsg: "+strMessage);
        }
        catch(Exception e){
            throw new Exception(e);
        }
        finally{
            closeObjectsDatabase(conn, cstmt, rs);
        }
        objHashMap.put("objArrayList", listConfiguration);
        objHashMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return objHashMap;
    }
    
    /**
     * Motivo: Obtiene el Plan Tarifario Original del Telefono de un Cliente
     * <br>Realizado por: <a href="mailto:carlos.diazoliva@tcs.com">Carlos Diaz</a>
     * <br>Fecha: 17/04/2017
     *
     * @return		HashMap
     */
    public HashMap getPlanTarifarioPenalty(String strCustomerId, String strPhone) throws SQLException, Exception {
        HashMap objHashMap = new HashMap();
        Long planTarifarioId = null;
        String strMessage;
        Connection conn = null;
        OracleCallableStatement cstmt = null;

        String sqlStr = "BEGIN ADDENDUM.NP_ADDENDUM_CONTRACT_PKG.SP_GET_PLAN_TARIFARIO_PENALTY(?, ?, ?, ?); END;";
        try{
            long customerId = Long.parseLong(strCustomerId);

            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

            cstmt.setLong(1, customerId);
            cstmt.setString(2, strPhone);
            cstmt.registerOutParameter(3, OracleTypes.NUMERIC);
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            strMessage = cstmt.getString(4);
			if (cstmt.getObject(3) != null) {
            	planTarifarioId = cstmt.getLong(3);
            }
            
            System.out.println("[PenaltyDAO.getPlanTarifarioPenalty] - planTarifarioId: "+planTarifarioId);
            System.out.println("[PenaltyDAO.getPlanTarifarioPenalty] - ErrorMsg: "+strMessage);
        }
        catch(Exception e){
            throw new Exception(e);
        }
        finally{
            closeObjectsDatabase(conn, cstmt, null);
        }
        objHashMap.put("planTarifarioId", planTarifarioId);
        objHashMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return objHashMap;
    }
    
    /**
     * Motivo: Permite obtener un flag general para usar o no funcionalidad de Penalidades
     * <br>Realizado por: <a href="mailto:carlos.diazoliva@tcs.com">Carlos Diaz</a>
     * <br>Fecha: 28/04/2017
     *
     * @return		HashMap
     */
    public HashMap getFlagShowPenaltyPayFunctionality(long lOrderId) throws SQLException, Exception {
        HashMap objHashMap = new HashMap();
        int flag = 0;
        String strMessage;
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;

        String sqlStr = "BEGIN ADDENDUM.NP_ADDENDUM_CONFIGURATION_PKG.SP_SHOW_PENALTY_BY_ORDERID(?, ?, ?); END;";
        try{
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

            cstmt.setLong(1, lOrderId);
            cstmt.registerOutParameter(2, OracleTypes.NUMERIC);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            flag = cstmt.getInt(2);
            strMessage = cstmt.getString(3);
            
            System.out.println("[PenaltyDAO.getFlagShowPenaltyPayFunctionality] - flag: "+flag);
            System.out.println("[PenaltyDAO.getFlagShowPenaltyPayFunctionality] - ErrorMsg: "+strMessage);
        }
        catch(Exception e){
            throw new Exception(e);
        }
        finally{
            closeObjectsDatabase(conn, cstmt, rs);
        }
        objHashMap.put("flag", flag);
        objHashMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return objHashMap;
    }
    
    /**
     * Motivo: Permite obtener un listado de valores para una configuracion por rol
     * <br>Realizado por: <a href="mailto:jorge.curi@tcs.com">Jorge Curi</a>
     * <br>Fecha: 06/04/2017
     *
     * @return		HashMap
     */    
    public HashMap getConfigurationRolList(long lUserId, int iAppId, String strRuc) throws SQLException, Exception {
    	HashMap objHashMap = new HashMap();
		Connection conn = null;
		OracleCallableStatement cstmt = null;
		ResultSet rs = null;
		String strMessage = null;
		ArrayList<ListBoxBean> listBoxList = new ArrayList<ListBoxBean>();
		try {
			conn = Proveedor.getConnection();
			String sqlStr = "BEGIN ADDENDUM.NP_INTERFACE_PKG.SP_GET_WAY_TO_PAY_LST(?, ?, ?, ?, ?); END;";
			cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
			
			cstmt.setLong(1, lUserId);
			cstmt.setInt(2, iAppId);
			cstmt.setString(3, strRuc);

			cstmt.registerOutParameter(4, OracleTypes.CURSOR);
			cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
			cstmt.execute();
			strMessage = cstmt.getString(5);
			 if(strMessage == null){
	                rs = (ResultSet)cstmt.getObject(4);
	                if(rs != null){
	                    while(rs.next()){
	                        int i = 0;
	                        ListBoxBean listBoxBean = new ListBoxBean();
	                        listBoxBean.setIdListBox(rs.getInt(++i));
	                        listBoxBean.setDescListBox(StringUtils.defaultString(rs.getString(++i)));
	                        listBoxList.add(listBoxBean);
	                    }
	                }
	            }
	            System.out.println("[PenaltyDAO.getConfigurationList] - Tamanio list: "+listBoxList.size());
	            System.out.println("[PenaltyDAO.getConfigurationList] - ErrorMsg: "+strMessage);
		 } catch (SQLException e) {
	         e.printStackTrace();
	         System.out.println("[PenaltyDAO][getConfigurationRolList] - SQLException = " + e.getMessage());
	         throw new SQLException(e);
	      } catch (Exception e) {
	         e.printStackTrace();
	         System.out.println("[PenaltyDAO][getConfigurationRolList] - Exception = " + e.getMessage());
	         throw new Exception(e);
	      }finally{
	        	closeObjectsDatabase(conn, cstmt, rs);
	    }
	    objHashMap.put("listBoxList", listBoxList);
	    objHashMap.put(Constante.MESSAGE_OUTPUT, strMessage);
	    return objHashMap;
	}
}