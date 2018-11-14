package pe.com.portability.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

import org.apache.commons.lang.StringUtils;

import pe.com.nextel.bean.DominioBean;
import pe.com.nextel.dao.GenericDAO;
import pe.com.nextel.dao.Proveedor;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;
import pe.com.portability.bean.PortabilityContactBean;
import pe.com.portability.bean.PortabilityItemBean;
import pe.com.portability.bean.PortabilityOrderBean;
import pe.com.portability.bean.PortabilityParticipantBean;


// ALTA


public class PortabilityOrderDAO extends GenericDAO
{
    public PortabilityOrderDAO()
    {
    }

    //----------------------------------BAJA

    /**
     * Motivo: Muestra la lista de las ordenes la categoria de Portabilidad de Baja 
     * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomás Mogrovejo Acosta</a>
     * <br>Fecha: 03/08/2009
     *
     * @return		ArrayList de DominioBean      
     */
    public HashMap getPortabilityLowByOrder(long lOrderId) throws SQLException, Exception {
        HashMap objHashMap = new HashMap();
        ArrayList arrPortabilityLowList = new ArrayList();
        String strMessage;
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strSql = "BEGIN ORDERS.NP_PORTABILITY_PROCESS_PKG.SP_GET_PORTABILITY_LOW_LST(?, ?, ?); END;";
        try{
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(strSql);
            cstmt.setLong(1, lOrderId);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            rs = (ResultSet)cstmt.getObject(2);
            strMessage = cstmt.getString(3);
            while(rs.next()) {

                HashMap hshPortabilityLowMap = new HashMap();

                hshPortabilityLowMap.put("mot_susp", StringUtils.defaultString(rs.getString("mot_susp"),""));
                hshPortabilityLowMap.put("est_contract", StringUtils.defaultString(rs.getString("est_contract"),""));
                hshPortabilityLowMap.put("num_tel", StringUtils.defaultString(rs.getString("num_tel"),""));
                hshPortabilityLowMap.put("ult_est_proc_port", StringUtils.defaultString(rs.getString("ult_est_proc_port"),""));
                hshPortabilityLowMap.put("error_no_integridad", StringUtils.defaultString(rs.getString("error_no_integridad"),""));
                hshPortabilityLowMap.put("eval_sol_baja", StringUtils.defaultString(rs.getString("eval_sol_baja"),""));
                hshPortabilityLowMap.put("motivos", StringUtils.defaultString(rs.getString("motivos"),""));
                hshPortabilityLowMap.put("doc_atatchment", StringUtils.defaultString(rs.getString("doc_atatchment"),""));
                hshPortabilityLowMap.put("ruta", StringUtils.defaultString(rs.getString("ruta"),""));
                hshPortabilityLowMap.put("messages_status", StringUtils.defaultString(rs.getString("messages_status"),""));
                hshPortabilityLowMap.put("status_portability", StringUtils.defaultString(rs.getString("status_portability"),""));
                hshPortabilityLowMap.put("desc_eval_sol_baja", StringUtils.defaultString(rs.getString("desc_eval_sol_baja"),""));
                hshPortabilityLowMap.put("desc_motivos", StringUtils.defaultString(rs.getString("desc_motivos"),""));
                hshPortabilityLowMap.put("desc_doc_atatchment", StringUtils.defaultString(rs.getString("desc_doc_atatchment"),""));
                hshPortabilityLowMap.put("npportabitemid", StringUtils.defaultString(rs.getString("npportabitemid"),""));
                hshPortabilityLowMap.put("fec_lim_ejec", StringUtils.defaultString(rs.getString("fec_lim_ejec"),""));
                hshPortabilityLowMap.put("fec_ejec", StringUtils.defaultString(rs.getString("fec_ejec"),""));
                hshPortabilityLowMap.put("npapplicationid", StringUtils.defaultString(rs.getString("npapplicationid"),""));
                hshPortabilityLowMap.put("dias_transc", StringUtils.defaultString(rs.getString("dias_transc"),""));
                hshPortabilityLowMap.put("np_delaybscs", StringUtils.defaultString(rs.getString("np_delaybscs"),""));
                hshPortabilityLowMap.put("num_tel_wn", StringUtils.defaultString(rs.getString("num_tel_wn"),"")); // se agrego campo de número telefonico con wolrd number

                hshPortabilityLowMap.put("npamountdue", rs.getDouble("npamountdue") );
                hshPortabilityLowMap.put("npcurrencytype", rs.getString("npcurrencytype") );
                hshPortabilityLowMap.put("npcurrencytypedesc", rs.getString("npcurrencytypedesc") );
                hshPortabilityLowMap.put("npexpirationdatereceipt", rs.getDate("npexpirationdatereceipt") );
                hshPortabilityLowMap.put("npreleasedate", rs.getDate("npreleasedate") );

                arrPortabilityLowList.add(hshPortabilityLowMap);

            }
        }
        catch (Exception e) {
            throw new Exception(e);
        }
        finally{
            closeObjectsDatabase(conn,cstmt,rs);
        }
        objHashMap.put("arrPortabilityLowList", arrPortabilityLowList);
        objHashMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return objHashMap;
    }

    /**
     * Motivo: Muestra la cabecera de la orden de la categoria de Portabilidad de Baja 
     * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomás Mogrovejo Acosta</a>
     * <br>Fecha: 03/08/2009
     *
     * @return		ArrayList de DominioBean      
     */
    public HashMap getPortabilityLowHeader(long lOrderId) throws SQLException, Exception {
        HashMap objHashMap = new HashMap();
        ArrayList arrPortabilityLowList = new ArrayList();
        String strMessage;
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strSql = "BEGIN ORDERS.NP_PORTABILITY_PROCESS_PKG.SP_GET_PORTABILITY_LOW_HEADER(?, ?, ?); END;";
        try{
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(strSql);
            cstmt.setLong(1, lOrderId);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            rs = (ResultSet)cstmt.getObject(2);
            strMessage = cstmt.getString(3);
            while(rs.next()) {

                HashMap hshPortabilityLowMap = new HashMap();
                hshPortabilityLowMap.put("nom_receptor", StringUtils.defaultString(rs.getString("nom_receptor"),""));
                hshPortabilityLowMap.put("nom_contact", StringUtils.defaultString(rs.getString("nom_contact"),""));
                hshPortabilityLowMap.put("email_contact", StringUtils.defaultString(rs.getString("email_contact"),""));
                hshPortabilityLowMap.put("tel_contact", StringUtils.defaultString(rs.getString("tel_contact"),""));
                hshPortabilityLowMap.put("fax_contacto", StringUtils.defaultString(rs.getString("fax_contacto"),""));
                arrPortabilityLowList.add(hshPortabilityLowMap);

            }
        }
        catch (Exception e) {
            throw new Exception(e);
        }
        finally{
            closeObjectsDatabase(conn,cstmt,rs);
        }
        objHashMap.put("arrPortabilityLowList", arrPortabilityLowList);
        objHashMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return objHashMap;
    }



    /**
     * Motivo: Obtiene la Lista de los Motivos de Objeción
     * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomás Mogrovejo</a>
     * <br>Fecha: 06/08/2009
     * @return		ArrayList de DominioBean      
     */

    public HashMap getMotivos(String modContract) throws SQLException, Exception {
        HashMap objHashMap = new HashMap();
        ArrayList arrMotivos = new ArrayList();
        String strMessage;
        Connection conn = null;
        HashMap hshData=new HashMap();
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;

        String sqlStr = "BEGIN ORDERS.NP_PORTABILITY_PROCESS_PKG.SP_GET_MOT_OBJ2(?, ?, ?); END;";
        try{
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setString(1, modContract);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            rs = (ResultSet)cstmt.getObject(2);
            strMessage = cstmt.getString(3);
            while (rs.next()) {
                DominioBean dominio = new DominioBean();
                dominio.setValor((String)rs.getString(1));
                dominio.setDescripcion((String)rs.getString(2));
                arrMotivos.add(dominio);
            }
        }
        catch(Exception e){
            throw new Exception(e);
        }
        finally{
            closeObjectsDatabase(conn, cstmt, rs);
        }
        objHashMap.put("arrMotivos", arrMotivos);
        objHashMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return objHashMap;
    }


    /**
     * Motivo: Obtiene la Lista de los Motivos de Objeción por el tipo de objecion
     * <br>Realizado por: <a href="mailto:carlos.serrano@teamsoft.com.pe">Carlos Serrano</a>
     * <br>Fecha: 07/08/2014
     * @return		ArrayList de DominioBean
     */

    public HashMap getMotivosByTypeObjection(String objectionTypeId) throws SQLException, Exception {
        HashMap objHashMap = new HashMap();
        ArrayList arrMotivos = new ArrayList();
        String strMessage;
        Connection conn = null;
        HashMap hshData=new HashMap();
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;

        String sqlStr = "BEGIN ORDERS.NP_PORTABILITY_PROCESS_PKG.SP_GET_REASON_OBJECTION(?, ?, ?); END;";
        try{
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setString(1, objectionTypeId);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            rs = (ResultSet)cstmt.getObject(2);
            strMessage = cstmt.getString(3);
            while (rs.next()) {
                DominioBean dominio = new DominioBean();
                dominio.setValor((String)rs.getString(1));
                dominio.setDescripcion((String)rs.getString(2));
                arrMotivos.add(dominio);
            }
        }
        catch(Exception e){
            throw new Exception(e);
        }
        finally{
            closeObjectsDatabase(conn, cstmt, rs);
        }
        objHashMap.put("arrMotivos", arrMotivos);
        objHashMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return objHashMap;
    }


    /**
     * Motivo: Obtiene los tipos de documentos que pueden ser adjuntados
     * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomás Mogrovejo</a>
     * <br>Fecha: 06/08/2009
     * @return		ArrayList de DominioBean      
     */

    public HashMap getDocAtatchment() throws SQLException, Exception {
        HashMap objHashMap = new HashMap();
        ArrayList arrDocAtatchment = new ArrayList();
        String strMessage;
        Connection conn = null;
        HashMap hshData=new HashMap();
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;

        String sqlStr = "BEGIN ORDERS.NP_PORTABILITY_PROCESS_PKG.SP_GET_DOC_ATATCHMENT(?,?); END;";

        try{
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.registerOutParameter(1, OracleTypes.CURSOR);
            cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            rs = (ResultSet)cstmt.getObject(1);
            strMessage = cstmt.getString(2);
            while (rs.next()) {
                DominioBean dominio = new DominioBean();
                dominio.setValor((String)rs.getString(1));
                dominio.setDescripcion((String)rs.getString(2));
                arrDocAtatchment.add(dominio);
            }
        }
        catch(Exception e){
            throw new Exception(e);
        }
        finally{
            closeObjectsDatabase(conn, cstmt, rs);
        }
        objHashMap.put("arrDocAtatchment", arrDocAtatchment);
        objHashMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return objHashMap;
    }


    public String updatePortabilityLow(PortabilityOrderBean objPortabilityOrderBean,String strLogin,java.sql.Connection conn) throws Exception, SQLException{

        OracleCallableStatement cstmt = null;
        String strMessage=null;
        String sqlStr = "BEGIN ORDERS.NP_PORTABILITY_PROCESS_PKG.SP_UPD_PORTABILITY_LOW(?, ?); END;";
        try{
            Object[] objOrderPortability             = {
                    objPortabilityOrderBean.getNpcmbEvalSolBaja(),
                    objPortabilityOrderBean.getNpcmbMotivos(),
                    objPortabilityOrderBean.getNpcmbDocAtatchment(),
                    objPortabilityOrderBean.getNpav_ruta(),
                    String.valueOf(objPortabilityOrderBean.getNporderid()),
                    objPortabilityOrderBean.getNpmodificate(),
                    objPortabilityOrderBean.getNportabitemid()
            };

            StructDescriptor sdOrderReparation   = StructDescriptor.createDescriptor("ORDERS.TO_PORTABILITY_LOW", conn);
            STRUCT stcOrderReparation            = new STRUCT(sdOrderReparation, conn, objOrderPortability);

            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setSTRUCT(1,stcOrderReparation);

            cstmt.registerOutParameter(2, Types.CHAR);
            cstmt.executeUpdate();

            strMessage = cstmt.getString(2);
        }
        catch (Exception e) {
            throw new Exception(e);
        }
        finally{
            closeObjectsDatabase(null,cstmt,null);
        }
        return strMessage;
    }


    /**
     * Motivo: Obtiene la Lista de los documentos a objetar dado un motivo de objeción
     * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomás Mogrovejo</a>
     * <br>Fecha: 17/08/2009
     * @return		ArrayList de DominioBean      
     */

    public HashMap getAtatchment_by_mo(String strAccesoryType) throws SQLException, Exception {
        HashMap objHashMap = new HashMap();
        //ArrayList arrAccesoryModels = new ArrayList();
        ArrayList arrAtatchment_by_mo = new ArrayList();
        String strMessage;
        Connection conn = null;
        HashMap hshData=new HashMap();
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;

        String sqlStr = "BEGIN ORDERS.NP_PORTABILITY_PROCESS_PKG.SP_GET_ATATCHMENT_BY_MO(?, ?, ?); END;";

        try{
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setString(1, strAccesoryType);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            rs = (ResultSet)cstmt.getObject(2);
            strMessage = cstmt.getString(3);
            while (rs.next()) {
                DominioBean dominio = new DominioBean();
                dominio.setValor((String)rs.getString(1));
                dominio.setDescripcion((String)rs.getString(2));
                arrAtatchment_by_mo.add(dominio);
            }
        }
        catch(Exception e){
            throw new Exception(e);
        }
        finally{
            closeObjectsDatabase(conn, cstmt, rs);
        }
        objHashMap.put("arrAtatchment_by_mo", arrAtatchment_by_mo);
        objHashMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return objHashMap;
    }

    public HashMap getConfigFile(String configTabla) throws SQLException, Exception {

        HashMap objHashMap = new HashMap();
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        Connection conn=null;
        String strMessage = null;
        PortabilityOrderBean objPOBean = null;

        try{

            String sqlStr =  "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_GET_NPCONFIGUTATION_LIST(?, ?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setString(1, configTabla);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.execute();

            strMessage = cstmt.getString(3);
            if (!StringUtils.isNotBlank(strMessage)) {
                rs = (ResultSet) cstmt.getObject(2);
                if(rs.next()) {
                    objPOBean = new PortabilityOrderBean();
                    objPOBean.setNpconfigFile(rs.getString(1));
                    objHashMap.put("objPOBean",objPOBean);
                }
            }
            objHashMap.put("strMessage",strMessage);

        }catch(Exception e){
            objHashMap.put("strMessage",e.getMessage());
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        return objHashMap;
    }

    //------------------------------------ALTA

    /**
     Method : getPortabItemDevList1
     Purpose: Obtiene lista de numeros portados.
     Developer                Fecha       Comentario
     =====================    ==========  ======================================================================
     Lazo de la Vega David    04/08/2009  Creación
     Frank Picoy              02/11/2010  Se agregó em la lista el numero UFMI - Reserva de Numeros Golden.
     */

    public HashMap getPortabItemDevList1(long npItemId)throws Exception,SQLException {  //2

        HashMap objHashMap = new HashMap();
        ArrayList list = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        Connection conn=null;
        String strMessage = null;
        PortabilityOrderBean objPOBean = null;
        int i = 1;

        try{

            String sqlStr =  "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_GET_PROCESS_HIGH(?, ?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            list = new ArrayList();
            cstmt.setLong(1, npItemId);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.execute();

            strMessage = cstmt.getString(3);
            if (!StringUtils.isNotBlank(strMessage)) {
                rs = (ResultSet) cstmt.getObject(2);

                while (rs.next()) {
                    objPOBean = new PortabilityOrderBean();
                    objPOBean.setNpPortabOrderId(rs.getLong(1));
                    objPOBean.setNpPhoneNumber(rs.getString(2));
                    objPOBean.setNpModalityContract(rs.getString(3));
                    objPOBean.setNpShippingDateMessage((String)MiUtil.getDate(rs.getTimestamp(4), "dd/MM/yyyy HH:mm"));
                    objPOBean.setNpLastStateProcess(rs.getString(5));
                    objPOBean.setNpLastStateDesc(rs.getString(6));
                    objPOBean.setNpErrorIntegrity(rs.getString(7));
                    objPOBean.setNpErrorIntegrityDesc(rs.getString(8));
                    objPOBean.setNpReasonRejection(rs.getString(9));
                    objPOBean.setNpReasonRejectionDesc(rs.getString(10));
                    objPOBean.setNpScheduleDeadline((String)MiUtil.getDate(rs.getTimestamp(11), "dd/MM/yyyy HH:mm"));
                    objPOBean.setNpExecutionDeadline((String)MiUtil.getDate(rs.getTimestamp(12), "dd/MM/yyyy HH:mm"));
                    objPOBean.setNpExecutionDate((String)MiUtil.getDate(rs.getTimestamp(13), "dd/MM/yyyy HH:mm"));
                    objPOBean.setNpCorrected(rs.getString(14));
                    objPOBean.setNpState(rs.getString(15));
                    objPOBean.setNpItemid(rs.getLong(16));
                    objPOBean.setNpItemDeviceId(rs.getLong(17));
                    objPOBean.setNpContract(rs.getString(18));
                    objPOBean.setNpStateDesc(rs.getString(19));
                    objPOBean.setNpApplicationId(rs.getString(20));
                    objPOBean.setNpModalitySell(rs.getString(21));
                    objPOBean.setNpProductLineId(rs.getLong(22));
                    objPOBean.setNpScheduleDBscs(rs.getString(23));
                    objPOBean.setNpPhoneNumberWN(rs.getString(24)); /*Teléfono con formato World number*/
                    objPOBean.setNpUfmi(rs.getString(25));/*UFMI - Reserva de Numeros*/
                    objPOBean.setNpSolutionid(rs.getLong(26));/*Solutionid - Reserva de Numeros*/
                    //P1D - Lee Rosales
                    objPOBean.setNpAmountDue(rs.getDouble(27));
                    objPOBean.setNpCurrencyType(rs.getString(28));
                    objPOBean.setNpCurrencyDesc(rs.getString(29));
                    objPOBean.setNpExpirationDateReceipt(rs.getDate(30));
                    objPOBean.setNpReleaseDate(rs.getDate(31));
                    objPOBean.setNpPortabItemId(rs.getLong(32)); //Subsanacion Deuda PRY-1531
                    objPOBean.setNpAssignor(rs.getString(33)); //Subsanacion Deuda PRY-1531
                    list.add(objPOBean);
                }
            }

            objHashMap.put("strMessage",strMessage);
            objHashMap.put("objArrayList",list);

        }catch(Exception e){
            objHashMap.put("strMessage",e.getMessage());
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return objHashMap;
    }


    /**
     * Motivo:  Obtiene una lista de Dominios según el "nptable" de la tabla SWBAPPS.NP_TABLE
     * <br>Realizado por: <a href="_mailto:tomas.mogrovejo@nextel.com.pe">Tomás Mogrovejo Acosta</a>
     * <br>Fecha: 04/08/2009
     *
     * @param      dominioTabla Ej: MODALITY_TYPES
     * @return     ArrayList de PortabilityOrderBean
     */

    public HashMap getDominioList(String dominioTabla) throws SQLException, Exception {

        if(logger.isDebugEnabled())
            logger.debug("--Inicio--");

        HashMap hshDataMap = new HashMap();
        ArrayList arrDominioList = new ArrayList();
        Connection conn = null;
        ResultSet rs = null;
        OracleCallableStatement cstmt = null;
        String strMessage = null;
        String sqlStr = "BEGIN WEBSALES.NPSL_GENERAL_PKG.SP_GET_NPTABLE_LIST(?,?,?); END;";
        try{
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);

            cstmt.setString(1, dominioTabla);
            cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(3, OracleTypes.CURSOR);
            cstmt.executeQuery();
            rs = (ResultSet)cstmt.getObject(3);
            strMessage = cstmt.getString(2);
            if(StringUtils.isBlank(strMessage)) {
                while (rs.next()) {
                    DominioBean dominio = new DominioBean();
                    int i = 1;
                    dominio.setValor(StringUtils.defaultString(rs.getString(i++)));
                    dominio.setDescripcion(StringUtils.defaultString(rs.getString(i++)));
                    arrDominioList.add(dominio);

                }
            }
        }
        catch(Exception e){
            hshDataMap.put("strMessage",e.getMessage());
        }
        finally{
            closeObjectsDatabase(conn, cstmt, rs);
        }
        hshDataMap.put("arrDominioList", arrDominioList);
        hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return hshDataMap;
    }

    /**
     Method : getParentCheckOrder
     Purpose: Obtiene lista de numeros portados.
     Developer                Fecha       Comentario
     =====================    ==========  ======================================================================
     Lazo de la Vega David    06/08/2009  Creación
     */

    public HashMap getParentCheckOrder(long npPortabOrderId)throws Exception,SQLException {

        HashMap objHashMap = new HashMap();
        ArrayList list = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        Connection conn=null;
        String strMessage = null;
        PortabilityOrderBean objPOBean = null;
        //long portaborderid = MiUtil.parseInt((String)hshParameters.get("lPortabOrderId").toString());
        int i = 1;

        try{

            String sqlStr =  "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_PARENT_CHECK_ORDER(?, ?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            list = new ArrayList();
            cstmt.setLong(1, npPortabOrderId);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.execute();

            strMessage = cstmt.getString(3);
            if (!StringUtils.isNotBlank(strMessage)) {
                rs = (ResultSet) cstmt.getObject(2);

                while (rs.next()) {
                    objPOBean = new PortabilityOrderBean();
                    objPOBean.setNpPortabOrderId(rs.getLong(1));
                    objPOBean.setNpOrderParentId(rs.getString(2));
                    list.add(objPOBean);
                    i=1;
                }
            }

            objHashMap.put("strMessage",strMessage);
            objHashMap.put("objParentCheck",list);

        }catch(Exception e){
            objHashMap.put("strMessage",e.getMessage());
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return objHashMap;
    }


    /**
     Method : getParticipantList
     Purpose: Obtiene lista de operarios cedentes o receptores.
     Developer                Fecha       Comentario
     =====================    ==========  ======================================================================
     Lazo de la Vega David    07/08/2009  Creación
     MMONTOYA                 20/10/2015  Se adicionan los parámetros
     */

    public HashMap getParticipantList(int specificationId, int divisionId)throws Exception,SQLException {
        HashMap objHashMap = new HashMap();
        ArrayList list = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        Connection conn=null;
        String strMessage = null;
        PortabilityParticipantBean objPPBean = null;
        int i = 1;

        try{

            String sqlStr =  "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_GET_PARTICIPANT_LST(?, ?, ?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            list = new ArrayList();

            cstmt.setInt(1, specificationId);
            cstmt.setInt(2, divisionId);
            cstmt.registerOutParameter(3, OracleTypes.CURSOR);
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);

            cstmt.execute();

            strMessage = cstmt.getString(4);
            if (!StringUtils.isNotBlank(strMessage)) {
                rs = (ResultSet) cstmt.getObject(3);

                while (rs.next()) {
                    objPPBean = new PortabilityParticipantBean();
                    objPPBean.setNpParticipantId(rs.getString(1));
                    objPPBean.setNpDescripcion(rs.getString(2));
                    objPPBean.setNpTag1(rs.getString(3));
                    objPPBean.setNpStatus(rs.getString(4));
                    list.add(objPPBean);
                    i=1;
                }
            }

            objHashMap.put("strMessage",strMessage);
            objHashMap.put("objParticipantList",list);

        }catch(Exception e){
            objHashMap.put("strMessage",e.getMessage());
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return objHashMap;
    }


    /**
     Method : getItemsPortabList
     Purpose: Obtiene lista de item a ser portados.
     Developer                Fecha       Comentario
     =====================    ==========  ======================================================================
     Lazo de la Vega David    13/08/2009  Creación
     */

    public HashMap getItemsPortabList(long npOrderId)throws Exception,SQLException { //1

        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        HashMap objHashMap = new HashMap();
        ArrayList list = null;
        Connection conn=null;
        String strMessage = null;
        PortabilityOrderBean objPOBean = null;
        int i = 1;

        try{
            String sqlStr =  "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_GET_ITEMS_PORT_LST(?,?,?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            list = new ArrayList();
            cstmt.setLong(1, npOrderId);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.execute();

            strMessage = cstmt.getString(3);
            if (!StringUtils.isNotBlank(strMessage)) {
                rs = (ResultSet) cstmt.getObject(2);

                while (rs.next()) {
                    objPOBean = new PortabilityOrderBean();
                    objPOBean.setNpOrderid(rs.getLong(1));
                    objPOBean.setNpItemid(rs.getLong(2));
                    objPOBean.setNpOrderParentId(rs.getString(3));
                    objPOBean.setNpModalitySell(rs.getString(4));
                    objPOBean.setNpProductLineId(rs.getLong(5));
                    list.add(objPOBean);
                    i=1;
                }
            }

            objHashMap.put("strMessage",strMessage);
            objHashMap.put("objItemList",list);

        }catch(Exception e){
            throw new Exception(e);
        }finally{
            try{
                conn.commit();
                closeObjectsDatabase(conn,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return objHashMap;

    }


    /**
     Method : insertSectionPortabItemHigh
     Purpose: Realiza inserción o actualización de números a ser portados.
     Developer                Fecha       Comentario
     =====================    ==========  ======================================================================
     Lazo de la Vega David    14/08/2009  Creación
     Frank Picoy              12/10/2010  Se agregó en el insert los dos nuevos campos creados(NpUfmi,NpRqstdUfmi)
     */

    public String insertSectionPortabItemHigh(PortabilityOrderBean objPortabilityOrderBean,long npPortabOrderId,java.sql.Connection conn)throws Exception,SQLException {

        OracleCallableStatement cstmt = null;
        //conn = Proveedor.getConnection();
        String strMessage = null;
        String sqlStrIns = "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_INS_PORT_ITEM_HIGH(?,?,?); END;";
        String portabType = "";

        try{

            Object[] objOrderPortability       = {
                    String.valueOf(objPortabilityOrderBean.getNpItemid()),
                    String.valueOf(objPortabilityOrderBean.getNpItemDeviceId()),
                    objPortabilityOrderBean.getNpPhoneNumber(),
                    objPortabilityOrderBean.getNpModalityContract(),
                    objPortabilityOrderBean.getNpAssignor(),
                    String.valueOf(objPortabilityOrderBean.getNpOrderid()),
                    String.valueOf(objPortabilityOrderBean.getNpCustomerId()),
                    objPortabilityOrderBean.getNpCreateBy(),
                    objPortabilityOrderBean.getNpPortabType(),
                    objPortabilityOrderBean.getNpDocument(),
                    objPortabilityOrderBean.getNpTypeDocument(),
                    objPortabilityOrderBean.getNpUfmi(),
                    objPortabilityOrderBean.getNpRqstdUfmi()
            };

            StructDescriptor sdOrderPortability  = StructDescriptor.createDescriptor("ORDERS.TO_PORTABILITY_INSERT_HIGH", conn);
            STRUCT stcOrderPortability            = new STRUCT(sdOrderPortability, conn, objOrderPortability);

            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStrIns);

            cstmt.setSTRUCT(1,stcOrderPortability);
            cstmt.setLong(2,npPortabOrderId);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeUpdate();

            strMessage = cstmt.getString(3);


        }catch (Exception e) {
            throw new Exception(e);

        }finally{
            closeObjectsDatabase(null,cstmt,null);
        }

        return strMessage;
    }


    /**
     Method : searchPortabilityItem
     Purpose: Verifica si existe el numero a portar.
     Developer                Fecha       Comentario
     =====================    ==========  ======================================================================
     Lazo de la Vega David    14/08/2009  Creación
     JVILLANUEVA              14/12/2015  Se agrega parámmetro npPortabOrderId para optimizar query
     */

    public boolean searchPortabilityItem(long npPortabOrderId, long npItemIdSearch, long npItemDevIdSearch,java.sql.Connection conn)throws Exception,SQLException {

        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        //Connection conn=null;
        String strMessage = null;
        boolean busqueda = false;

        try{

            String sqlStr =  "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_SEARCH_ITEMSPORT(?,?,?,?,?); END;";
            //conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, npPortabOrderId);
            cstmt.setLong(2, npItemIdSearch);
            cstmt.setLong(3, npItemDevIdSearch);
            cstmt.registerOutParameter(4, OracleTypes.CURSOR);
            cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
            cstmt.execute();

            strMessage = cstmt.getString(5);
            if (!StringUtils.isNotBlank(strMessage)) {
                rs = (ResultSet) cstmt.getObject(4);

                if(rs.next()) {
                    busqueda = true;
                }else{
                    busqueda = false;
                }
            }

        }catch(Exception e){
            throw new Exception(e);
        }finally{
            try{
                closeObjectsDatabase(null,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return busqueda;
    }

    /**
     Method : searchItem
     Purpose: Verifica si existe el numero a portar en la np_Item.
     Developer                Fecha       Comentario
     =====================    ==========  ======================================================================
     Lazo de la Vega David    03/09/2009  Creación
     */

    public boolean searchItem(long npItemIdSearch,java.sql.Connection conn)throws Exception,SQLException {

        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strMessage = null;
        boolean busqueda = false;

        try{

            String sqlStr =  "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_SEARCH_ITEM(?,?,?); END;";
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, npItemIdSearch);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.execute();

            strMessage = cstmt.getString(3);
            if (!StringUtils.isNotBlank(strMessage)) {
                rs = (ResultSet) cstmt.getObject(2);

                if(rs.next()) {
                    busqueda = true;
                }else{
                    busqueda = false;
                }
            }

        }catch(Exception e){
            throw new Exception(e);
        }finally{
            try{
                closeObjectsDatabase(null,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return busqueda;
    }


    /**
     Method : searchPortabilityItem1
     Purpose: Verifica si existe el numero a portar en la np_Portability_Item.
     Developer                Fecha       Comentario
     =====================    ==========  ======================================================================
     Lazo de la Vega David    03/09/2009  Creación
     JVILLANUEVA              14/12/2015  Se agrega parámmetro npPortabOrderId para optimizar query
     */

    public boolean searchPortabilityItem1(long npPortabOrderId, long npItemIdSearch,java.sql.Connection conn)throws Exception,SQLException {

        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strMessage = null;
        boolean busqueda = false;

        try{

            String sqlStr =  "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_SEARCH_PORTABITEM(?,?,?,?); END;";
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, npPortabOrderId);
            cstmt.setLong(2, npItemIdSearch);
            cstmt.registerOutParameter(3, OracleTypes.CURSOR);
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
            cstmt.execute();

            strMessage = cstmt.getString(4);
            if (!StringUtils.isNotBlank(strMessage)) {
                rs = (ResultSet) cstmt.getObject(3);

                if(rs.next()) {
                    busqueda = true;
                }else{
                    busqueda = false;
                }
            }

        }catch(Exception e){
            throw new Exception(e);
        }finally{
            try{
                closeObjectsDatabase(null,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return busqueda;
    }


    /**
     Method : insertSectionPortabOrderHigh
     Purpose: Proceso para insertar una orden de portabilidad.
     Developer                Fecha       Comentario
     =====================    ==========  ======================================================================
     Lazo de la Vega David    16/08/2009  Creación
     Frank Picoy              12/10/2010  Se agregó en el insert los dos nuevos campos creados(NpUfmi,NpRqstdUfmi).
     */

    public HashMap insertSectionPortabOrderHigh(PortabilityOrderBean objPortabOrderBean, java.sql.Connection conn) throws Exception,SQLException {

        OracleCallableStatement cstmt = null;
        //conn = Proveedor.getConnection();
        String strMessage = null;
        HashMap objHashMap = new HashMap();
        String npPortabOrderId = null;
        String sqlStrIns = "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_INS_PORT_ORDER_HIGH(?,?,?); END;";

        try{

            Object[] objOrderPortability   = { "","","","",
                    objPortabOrderBean.getNpAssignor(),
                    String.valueOf(objPortabOrderBean.getNpOrderid()),
                    String.valueOf(objPortabOrderBean.getNpCustomerId()),
                    objPortabOrderBean.getNpCreateBy(),
                    "",
                    objPortabOrderBean.getNpDocument(),
                    objPortabOrderBean.getNpTypeDocument(),"",null
            };

            StructDescriptor sdOrderPortability  = StructDescriptor.createDescriptor("ORDERS.TO_PORTABILITY_INSERT_HIGH", conn);
            STRUCT stcOrderPortability           = new STRUCT(sdOrderPortability, conn, objOrderPortability);

            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStrIns);

            cstmt.setSTRUCT(1,stcOrderPortability);
            cstmt.registerOutParameter(2, OracleTypes.NUMBER);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeUpdate();

            strMessage = cstmt.getString(3);
            if (!StringUtils.isNotBlank(strMessage)) {
                npPortabOrderId = String.valueOf(cstmt.getObject(2));
            }

            objHashMap.put("npPortabOrderId",npPortabOrderId);
            objHashMap.put("strMessage",strMessage);
            logger.debug("Fin metodo dao insertar");
        }catch (Exception e) {
            throw new Exception(e);

        }finally{
            closeObjectsDatabase(null,cstmt,null);
        }

        return objHashMap;

    }


    /**
     Method : searchPortabilityOrder
     Purpose: Proceso para verificar si existe la order en la np_portability_order.
     Developer                Fecha       Comentario
     =====================    ==========  ======================================================================
     Lazo de la Vega David    16/08/2009  Creación
     */

    public boolean searchPortabilityOrder(long npOrderId, java.sql.Connection conn) throws Exception,SQLException {

        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strMessage = null;
        boolean busqueda = false;

        try{

            String sqlStr =  "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_SEARCH_ORDERPORT(?,?,?); END;";
            //conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, npOrderId);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            strMessage = cstmt.getString(3);
            if (!StringUtils.isNotBlank(strMessage)) {
                rs = (ResultSet) cstmt.getObject(2);

                if(rs.next()) {
                    busqueda = true;
                }else{
                    busqueda = false;
                }
            }

        }catch(Exception e){
            throw new Exception(e);
        }finally{
            try{
                closeObjectsDatabase(null,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return busqueda;

    }


    /**
     Method : getPortabilityOrderId
     Purpose: Proceso para recuperar la npportaborderid de la order en la np_portability_order.
     Developer                Fecha       Comentario
     =====================    ==========  ======================================================================
     Lazo de la Vega David    16/08/2009  Creación
     */

    public long getPortabilityOrderId(long npOrderId,java.sql.Connection conn) throws Exception,SQLException {  //1

        OracleCallableStatement cstmt = null;
        //Connection conn = null;
        ResultSet rs = null;
        String strMessage = null;
        PortabilityOrderBean objPortabBean = null;
        long npPortabOrderId = 0;

        try{

            String sqlStr =  "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_GET_PORTAB_ORDERID(?,?,?); END;";
            //conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, npOrderId);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.execute();

            strMessage = cstmt.getString(3);
            if (!StringUtils.isNotBlank(strMessage)) {
                rs = (ResultSet) cstmt.getObject(2);
                if(rs.next()){
                    npPortabOrderId = (long)rs.getLong(1);
                }
            }

        }catch(Exception e){
            throw new Exception(e);
        }finally{
            try{
                closeObjectsDatabase(null,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return npPortabOrderId;

    }


    /**
     Method : updateSectionPortabItemHigh
     Purpose: Realiza inserción o actualización de números a ser portados.
     Developer                Fecha       Comentario
     =====================    ==========  ======================================================================
     Lazo de la Vega David    14/08/2009  Creación
     Frank Picoy              12/10/2010  Se agregó en el arreglo de objetos los dos nuevos campos creados(NpUfmi,NpRqstdUfmi)
     JVILLANUEVA              14/12/2015  Se agrega parámmetro npPortabOrderId para optimizar query
     */

    public String updateSectionPortabItemHigh(long npPortabOrderId, PortabilityOrderBean objPortabilityOrderBean,java.sql.Connection conn) throws Exception,SQLException {

        OracleCallableStatement cstmt = null;
        //Connection conn = null;
        String strMessage = null;
        String sqlStrIns = "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_UPD_PORT_ITEM_HIGH(?,?,?); END;";
        //conn = Proveedor.getConnection();
        String portabType = "";
        try{

            Object[] objOrderPortability       = {
                    String.valueOf(objPortabilityOrderBean.getNpItemid()),
                    String.valueOf(objPortabilityOrderBean.getNpItemDeviceId()),
                    objPortabilityOrderBean.getNpPhoneNumber(),
                    objPortabilityOrderBean.getNpModalityContract(),
                    objPortabilityOrderBean.getNpAssignor(),
                    String.valueOf(objPortabilityOrderBean.getNpOrderid()),
                    String.valueOf(objPortabilityOrderBean.getNpCustomerId()),
                    objPortabilityOrderBean.getNpCreateBy(),
                    objPortabilityOrderBean.getNpPortabType(),
                    "","",
                    objPortabilityOrderBean.getNpUfmi(),
                    objPortabilityOrderBean.getNpRqstdUfmi()
            };

            StructDescriptor sdOrderPortability  = StructDescriptor.createDescriptor("ORDERS.TO_PORTABILITY_INSERT_HIGH", conn);
            STRUCT stcOrderPortability            = new STRUCT(sdOrderPortability, conn, objOrderPortability);

            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStrIns);
            cstmt.setLong(1, npPortabOrderId);
            cstmt.setSTRUCT(2, stcOrderPortability);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeUpdate();

            strMessage = cstmt.getString(3);


        }catch (Exception e) {
            throw new Exception(e);

        }finally{
            closeObjectsDatabase(null,cstmt,null);
        }

        return strMessage;

    }

    /**
     Method : updateSectionHeaderHigh
     Purpose: Realiza actualización del cedente de la orden.
     Developer                Fecha       Comentario
     =====================    ==========  ======================================================================
     Lazo de la Vega David    29/10/2009  Creación
     */

    public String updateSectionHeaderHigh(long npPortabOrderId,String strCedente,String strDocument, String strTypeDocument,java.sql.Connection conn) throws Exception,SQLException {

        OracleCallableStatement cstmt = null;
        String strMessage = null;

        try{
            String sqlStrIns = "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_UPD_HEADER_HIGH(?,?,?,?,?); END;";
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStrIns);

            cstmt.setLong(1,npPortabOrderId);
            cstmt.setString(2,strCedente);
            cstmt.setString(3,strDocument);
            cstmt.setString(4,strTypeDocument);
            cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
            cstmt.executeUpdate();

            strMessage = cstmt.getString(5);

        }catch (Exception e) {
            throw new Exception(e);

        }finally{
            closeObjectsDatabase(null,cstmt,null);
        }

        return strMessage;
    }

    /**
     Method : getPortabilityOrderId
     Purpose: Proceso para recuperar la npportaborderid de la order en la np_portability_order.
     Developer                Fecha       Comentario
     =====================    ==========  ======================================================================
     Lazo de la Vega David    16/08/2009  Creación
     */

    public long getPortabOrderId(long npOrderId) throws Exception,SQLException {

        OracleCallableStatement cstmt = null;
        Connection conn=null;
        ResultSet rs = null;
        String strMessage = null;
        long npPortabOrderId = 0;

        try{

            String sqlStr =  "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_GET_PORT_ORDERID(?,?,?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, npOrderId);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.execute();

            strMessage = cstmt.getString(3);
            if (!StringUtils.isNotBlank(strMessage)) {
                rs = (ResultSet) cstmt.getObject(2);

                if(rs.next()) {
                    npPortabOrderId = (long)rs.getLong(1);
                }
            }

        }catch(Exception e){
            throw new Exception(e);
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return npPortabOrderId;

    }


    /**
     Method : getPortabItemDevList
     Purpose: Proceso para recuperar lista de ItemDevice dado una Orden.
     Developer                Fecha       Comentario
     =====================    ==========  ======================================================================
     Lazo de la Vega David    20/08/2009  Creación
     */

    public HashMap getPortabItemDevList(long npOrderId,java.sql.Connection conn) throws Exception,SQLException {

        HashMap objHashMap = new HashMap();
        ArrayList list = null;
        PortabilityOrderBean objPOBean = null;
        OracleCallableStatement cstmt = null;
        //Connection conn=null;
        ResultSet rs = null;
        String strMessage = null;

        try{

            String sqlStr =  "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_GET_ITEM_DEVICES(?,?,?); END;";
            //conn = Proveedor.getConnection();
            list = new ArrayList();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, npOrderId);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            strMessage = cstmt.getString(3);
            if (!StringUtils.isNotBlank(strMessage)) {
                rs = (ResultSet) cstmt.getObject(2);

                while (rs.next()) {
                    objPOBean = new PortabilityOrderBean();
                    objPOBean.setNpItemDeviceId(rs.getLong(1));
                    objPOBean.setNpItemid(rs.getLong(2));
                    list.add(objPOBean);
                }
            }

            objHashMap.put("strMessage",strMessage);
            objHashMap.put("objArrayList",list);

        }catch(Exception e){
            throw new Exception(e);
        }finally{
            try{
                closeObjectsDatabase(null,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return objHashMap;

    }

    /**
     Method : deleteSectionPortabItemHigh
     Purpose: Proceso elimina ItemDevice de la Portability Item.
     Developer                Fecha       Comentario
     =====================    ==========  ======================================================================
     Lazo de la Vega David    20/08/2009  Creación
     */
    public String deleteSectionPortabItemHigh(long npPortabOrderId, PortabilityOrderBean objPortabilityOrderBean, java.sql.Connection conn) throws Exception, SQLException {
        //Connection conn = null;
        OracleCallableStatement cstmt = null;
        //conn = Proveedor.getConnection();
        String strMessage = null;
        String sqlStr = "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_DEL_PORTA_ITEM_BY_ITEM(?,?,?,?) ; END;";
        try{
            //conn  = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, npPortabOrderId);
            cstmt.setLong(2, objPortabilityOrderBean.getNpItemid());
            cstmt.setLong(3, objPortabilityOrderBean.getNpItemDeviceId());
            cstmt.registerOutParameter(4, Types.VARCHAR);

            cstmt.executeUpdate();
            strMessage   = cstmt.getString(4);
        }
        catch (Exception e) {
            throw new Exception(e);
        }
        finally{
            closeObjectsDatabase(null,cstmt,null);
        }
        return strMessage;
    }

    /**
     Method : getItemDeviceList
     Purpose: Proceso para recuperar lista de ItemDevice dado un Item.
     Developer                Fecha       Comentario
     =====================    ==========  ======================================================================
     Lazo de la Vega David    20/08/2009  Creación
     */

    public HashMap getItemDeviceList(long npItemid,java.sql.Connection conn) throws Exception,SQLException {

        HashMap objHashMap = new HashMap();
        ArrayList list = null;
        PortabilityOrderBean objPOBean = null;
        OracleCallableStatement cstmt = null;
        //Connection conn=null;
        ResultSet rs = null;
        String strMessage = null;

        try{

            String sqlStr =  "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_GET_ITEMDEVICE_LST(?,?,?); END;";
            //conn = Proveedor.getConnection();
            list = new ArrayList();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, npItemid);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            strMessage = cstmt.getString(3);
            if (!StringUtils.isNotBlank(strMessage)) {
                rs = (ResultSet) cstmt.getObject(2);

                while (rs.next()) {
                    objPOBean = new PortabilityOrderBean();
                    objPOBean.setNpItemDeviceId(rs.getLong(1));
                    objPOBean.setNpItemid(rs.getLong(2));
                    list.add(objPOBean);
                }
            }

            objHashMap.put("strMessage",strMessage);
            objHashMap.put("objArrayList",list);

        }catch(Exception e){
            throw new Exception(e);
        }finally{
            try{
                closeObjectsDatabase(null,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return objHashMap;

    }


    /**
     Method : getPortabItemDeviceList
     Purpose: Proceso para recuperar lista de ItemDevice de la portability Item.
     Developer                Fecha       Comentario
     =====================    ==========  ======================================================================
     Lazo de la Vega David    21/08/2009  Creación
     */

    public HashMap getPortabItemDeviceList(long npItemid,java.sql.Connection conn) throws Exception,SQLException {

        HashMap objHashMap = new HashMap();
        ArrayList list = null;
        PortabilityOrderBean objPOBean = null;
        OracleCallableStatement cstmt = null;
        //Connection conn=null;
        ResultSet rs = null;
        String strMessage = null;

        try{

            String sqlStr =  "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_GET_PORTABITEM_LST(?,?,?); END;";
            //conn = Proveedor.getConnection();
            list = new ArrayList();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, npItemid);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            strMessage = cstmt.getString(3);
            if (!StringUtils.isNotBlank(strMessage)) {
                rs = (ResultSet) cstmt.getObject(2);

                while (rs.next()) {
                    objPOBean = new PortabilityOrderBean();
                    objPOBean.setNpPortabItemId(rs.getLong(1));
                    objPOBean.setNpItemid(rs.getLong(2));
                    objPOBean.setNpItemDeviceId(rs.getLong(3));
                    list.add(objPOBean);
                }
            }

            objHashMap.put("strMessage",strMessage);
            objHashMap.put("objArrayList",list);

        }catch(Exception e){
            throw new Exception(e);
        }finally{
            try{
                closeObjectsDatabase(null,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return objHashMap;

    }


    /**
     Method : updateItemDevice
     Purpose: Realiza actualización de ItemId de los items de la portability item.
     Developer                Fecha       Comentario
     =====================    ==========  ======================================================================
     Lazo de la Vega David    21/08/2009  Creación
     */

    public String updateItemDevice(long npItemDeviceId,long npPortabItemId,java.sql.Connection conn) throws Exception,SQLException {

        OracleCallableStatement cstmt = null;
        String strMessage = null;
        String sqlStrIns = "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_UPD_PORTITEM_DEVICE(?,?,?); END;";
        //conn = Proveedor.getConnection();

        try{

            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStrIns);

            cstmt.setLong(1,npItemDeviceId);
            cstmt.setLong(2,npPortabItemId);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeUpdate();

            strMessage = cstmt.getString(3);


        }catch (Exception e) {
            throw new Exception(e);

        }finally{
            closeObjectsDatabase(null,cstmt,null);
        }

        return strMessage;

    }

    /**
     Method : getItemsPortabList
     Purpose: Obtiene lista de item a ser portados.
     Developer                Fecha       Comentario, conserva la conexion
     =====================    ==========  ======================================================================
     Lazo de la Vega David    21/08/2009  Creación
     */

    public HashMap getItemsPortabLst(long npOrderId,java.sql.Connection conn)throws Exception,SQLException { //1

        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        HashMap objHashMap = new HashMap();
        ArrayList list = null;
        //Connection conn=null;
        String strMessage = null;
        PortabilityOrderBean objPOBean = null;
        int i = 1;

        try{
            String sqlStr =  "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_GET_ITEMS_PORT_LST(?,?,?); END;";
            //conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            list = new ArrayList();
            cstmt.setLong(1, npOrderId);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.execute();

            strMessage = cstmt.getString(3);
            if (!StringUtils.isNotBlank(strMessage)) {
                rs = (ResultSet) cstmt.getObject(2);

                while (rs.next()) {
                    objPOBean = new PortabilityOrderBean();
                    objPOBean.setNpOrderid(rs.getLong(1));
                    objPOBean.setNpItemid(rs.getLong(2));
                    list.add(objPOBean);
                    i=1;
                }
            }

            objHashMap.put("strMessage",strMessage);
            objHashMap.put("objItemList",list);

        }catch(Exception e){
            throw new Exception(e);
        }finally{
            try{
                closeObjectsDatabase(null,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return objHashMap;

    }

    /**
     Method : getItemsPortabList
     Purpose: Obtiene la orden de la portability_order.
     Developer                Fecha       Comentario, conserva la conexion
     =====================    ==========  ======================================================================
     Lazo de la Vega David    24/08/2009  Creación
     Lizbeth Valencia         15/10/2014  Se agregan los parametros customertype y scheduledays
     */

    public HashMap getPortabOrderList(long npOrderId)throws Exception,SQLException { //1

        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        Connection conn=null;
        HashMap objHashMap = new HashMap();
        ArrayList list = null;
        String strMessage = null;
        PortabilityOrderBean objPOBean = null;

        try{
            String sqlStr =  "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_GET_PORTABORDER_ASSIGNOR(?,?,?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            list = new ArrayList();
            cstmt.setLong(1, npOrderId);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            strMessage = cstmt.getString(3);
            if (!StringUtils.isNotBlank(strMessage)) {
                rs = (ResultSet) cstmt.getObject(2);

                if(rs.next()) {
                    objPOBean = new PortabilityOrderBean();
                    objPOBean.setNpPortabOrderId(rs.getLong(1));
                    objPOBean.setNpAssignor(rs.getString(2));
                    objPOBean.setNpOrderid(rs.getLong(3));
                    objPOBean.setNpAssignorDesc(rs.getString(4));
                    objPOBean.setNpTypeDocument(rs.getString(5));
                    objPOBean.setNpDocument(rs.getString(6));
                    objPOBean.setNpCustomerType(rs.getString(7));
                    objPOBean.setNpScheduleDays(rs.getString(8));
                    list.add(objPOBean);
                }
            }

            objHashMap.put("strMessage",strMessage);
            objHashMap.put("objPortabOrderList",list);

        }catch(Exception e){
            throw new Exception(e);
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return objHashMap;

    }



    /**
     * Motivo: Consigue los números portables
     * @throws java.lang.Exception
     * @throws java.sql.SQLException
     * @return
     * @param strType
     * @param lngOrderId
     */

    public HashMap getPortableNumbers(long lngOrderId, String strType)throws SQLException, Exception {

        HashMap objHashMap = new HashMap();
        ArrayList list = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        Connection conn=null;
        String strMessage = null;
        PortabilityItemBean objPOItem = null;


        try{
            String sqlStr =  "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_GET_PORTABLE_NUMBERS_HIGH(?, ?, ?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            list = new ArrayList();
            cstmt.setLong(1, lngOrderId);
            cstmt.setString(2, strType);
            cstmt.registerOutParameter(3, OracleTypes.CURSOR);
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
            cstmt.execute();

            strMessage = cstmt.getString(4);
            if (!StringUtils.isNotBlank(strMessage)) {
                rs = (ResultSet) cstmt.getObject(3);

                while (rs.next()) {
                    objPOItem = new PortabilityItemBean();
                    objPOItem.setNpportaborderid(rs.getString("npportaborderid"));
                    objPOItem.setNpportabitemid(rs.getString("npportabitemid"));
                    objPOItem.setNpphonenumber(rs.getString("npphonenumber"));
                    list.add(objPOItem);
                }
            }

            objHashMap.put("strMessage",strMessage);
            objHashMap.put("numbers",list);

        }catch(Exception e){
            objHashMap.put("strMessage",e.getMessage());
            e.printStackTrace();
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return objHashMap;

    }

    /**
     * Motivo: Verifica si el usuario tiene permiso para una determinada sección
     * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomás Mogrovejo</a>
     * <br>Fecha: 27/08/2009
     * @param     String
     * @return    String
     */
    public long getValOrdenHija(long lOrderId) throws SQLException, Exception{
        String strFechaEmision=null;

        long lordereturn;
        Connection conn=null;
        OracleCallableStatement cstmt = null;

        String sqlStr = " { ? = call ORDERS.NP_PORTABILITY_PROCESS_PKG.FX_VALIDATE_CHILD_ORDER( ? ) } ";
        try{
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

            cstmt.registerOutParameter(1, Types.NUMERIC);
            cstmt.setLong(2,lOrderId);
            cstmt.execute();
            lordereturn = cstmt.getLong(1);
        }
        catch (Exception e) {
            throw new Exception(e);
        }
        finally{
            closeObjectsDatabase(conn,cstmt,null);
        }
        return lordereturn;
    }


    /**
     Method : updateStatusPortability
     Purpose: Realiza actualizacion del estado de item portado.
     Developer                Fecha       Comentario
     =====================    ==========  ======================================================================
     Lazo de la Vega David    28/08/2009  Creación
     */

    public HashMap updateStatusPortability(long npitemid,long npitemdeviceid,String npstatus,String npModality) throws Exception,SQLException {

        HashMap objHashMap = new HashMap();
        OracleCallableStatement cstmt = null;
        Connection conn=null;
        String strMessage = null;
        String strPhoneNumber = null;
        String sqlStrIns = "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_UPD_STATUS_PORTAB(?,?,?,?,?); END;";
        conn = Proveedor.getConnection();
        try{
            Date fechaInicio = new Date();
            System.out.println("[PORTABILITYORDERDAO][updateStatusPortability][PM0010354] : INICIO");
            System.out.println("[PORTABILITYORDERDAO][updateStatusPortability][PM0010354] : ItemId -->" +npitemid);
            System.out.println("[PORTABILITYORDERDAO][updateStatusPortability][PM0010354] : Fecha -->" + fechaInicio);
            System.out.println("[PORTABILITYORDERDAO][updateStatusPortability][PM0010354] : Estado -->" + npstatus);
        }catch(Exception e){
            logger.error(formatException(e));
            strMessage = e.getMessage();
            System.out.println("[PORTABILITYORDERDAO][updateStatusPortability][PM0010354] : ERROR AL PINTAR LOG INICIO "+strMessage);
        }


        try{

            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStrIns);
            cstmt.setLong(1,npitemdeviceid);
            cstmt.setLong(2,npitemid);
            cstmt.setString(3,npstatus);
            cstmt.setString(4,npModality);
            cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
            cstmt.executeUpdate();

            strMessage = cstmt.getString(5);

            objHashMap.put("strMessage",strMessage);

        }catch (Exception e) {
            throw new Exception(e);

        }finally{
            closeObjectsDatabase(conn,cstmt,null);
        }

        try{
        Date fechaFin = new Date();

            System.out.println("[PORTABILITYORDERDAO][updateStatusPortability][PM0010354] : ItemId -->" +npitemid);
            System.out.println("[PORTABILITYORDERDAO][updateStatusPortability][PM0010354] : Fecha -->" + fechaFin);
            System.out.println("[PORTABILITYORDERDAO][updateStatusPortability][PM0010354] : Estado -->" + npstatus);
            System.out.println("[PORTABILITYORDERDAO][updateStatusPortability][PM0010354] : Mensaje -->" + strMessage);
            System.out.println("[PORTABILITYORDERDAO][updateStatusPortability][PM0010354] : FIN");
        }catch(Exception e){
            logger.error(formatException(e));
            strMessage = e.getMessage();
            System.out.println("[PORTABILITYORDERDAO][updateStatusPortability][PM0010354] : ERROR AL PINTAR LOG FIN "+strMessage);
        }

        return objHashMap;

    }

    /**
     Method : getPhoneNumberItem
     Purpose: Proceso que obtendra el telefono de cada numero portado.
     Developer                Fecha       Comentario
     =====================    ==========  ======================================================================
     Lazo de la Vega David    28/08/2009  Creación
     */

    public HashMap getPhoneNumberItem(long npitemid,long npitemdeviceid) throws Exception,SQLException {

        HashMap objHashMap = new HashMap();
        ArrayList list = null;
        PortabilityOrderBean objPOBean = null;
        OracleCallableStatement cstmt = null;
        Connection conn=null;
        ResultSet rs = null;
        String strMessage = null;
        String strPhoneNumber = null;

        try{

            String sqlStr =  "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_GET_PHONE_NUMBER(?,?,?,?); END;";
            conn = Proveedor.getConnection();
            list = new ArrayList();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, npitemdeviceid);
            cstmt.setLong(2,npitemid);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            strPhoneNumber = cstmt.getString(3);
            strMessage = cstmt.getString(4);

            objHashMap.put("strMessage",strMessage);
            objHashMap.put("strPhoneNumber",strPhoneNumber);

        }catch(Exception e){
            throw new Exception(e);
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return objHashMap;

    }

    /**
     Method : getPortabItemList1
     Purpose: Obtiene lista de numeros portados de la np_item y de la np_portability_item.
     Developer                Fecha       Comentario
     =====================    ==========  ======================================================================
     Lazo de la Vega David    02/09/2009  Creación
     */

    public HashMap getPortabItemList1(long npItemId)throws Exception,SQLException {  //2

        HashMap objHashMap = new HashMap();
        ArrayList list = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        Connection conn=null;
        String strMessage = null;
        PortabilityOrderBean objPOBean = null;
        int i = 1;

        try{

            String sqlStr =  "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_GET_PROCESS_HIGH_ITEM(?, ?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            list = new ArrayList();
            cstmt.setLong(1, npItemId);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.execute();

            strMessage = cstmt.getString(3);
            if (!StringUtils.isNotBlank(strMessage)) {
                rs = (ResultSet) cstmt.getObject(2);

                while (rs.next()) {
                    objPOBean = new PortabilityOrderBean();
                    objPOBean.setNpPortabOrderId(rs.getLong(1));
                    objPOBean.setNpPhoneNumber(rs.getString(2));
                    objPOBean.setNpModalityContract(rs.getString(3));
                    objPOBean.setNpShippingDateMessage((String)MiUtil.getDate(rs.getTimestamp(4), "dd/MM/yyyy HH:mm"));
                    objPOBean.setNpLastStateProcess(rs.getString(5));
                    objPOBean.setNpLastStateDesc(rs.getString(6));
                    objPOBean.setNpErrorIntegrity(rs.getString(7));
                    objPOBean.setNpErrorIntegrityDesc(rs.getString(8));
                    objPOBean.setNpReasonRejection(rs.getString(9));
                    objPOBean.setNpReasonRejectionDesc(rs.getString(10));
                    objPOBean.setNpScheduleDeadline((String)MiUtil.getDate(rs.getTimestamp(11), "dd/MM/yyyy HH:mm"));
                    objPOBean.setNpExecutionDeadline((String)MiUtil.getDate(rs.getTimestamp(12), "dd/MM/yyyy HH:mm"));
                    objPOBean.setNpExecutionDate((String)MiUtil.getDate(rs.getTimestamp(13), "dd/MM/yyyy HH:mm"));
                    objPOBean.setNpCorrected(rs.getString(14));
                    objPOBean.setNpState(rs.getString(15));
                    objPOBean.setNpItemid(rs.getLong(16));
                    objPOBean.setNpContract(rs.getString(17));
                    objPOBean.setNpStateDesc(rs.getString(18));
                    objPOBean.setNpApplicationId(rs.getString(19));
                    objPOBean.setNpModalitySell(rs.getString(20));
                    objPOBean.setNpProductLineId(rs.getLong(21));
                    objPOBean.setNpScheduleDBscs(rs.getString(22));
                    objPOBean.setNpPhoneNumberWN(rs.getString(23)); /*Teléfono con formato World number*/
                    objPOBean.setNpPortabItemId(rs.getLong(24)); // Subsanacion Deuda PRY-1531
                    objPOBean.setNpAssignor(rs.getString(25)); // Subsanacion Deuda PRY-1531
                    list.add(objPOBean);
                }
            }

            objHashMap.put("strMessage",strMessage);
            objHashMap.put("objArrayList",list);

        }catch(Exception e){
            objHashMap.put("strMessage",e.getMessage());
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return objHashMap;
    }

    /**************************************************************************************
     Method : getOrderPortabilityReturn
     Purpose: Obteniene el detalle de la Orden de Portabilidad de Retorno
     Developer       Fecha       Comentario
     =============   ==========  ==========================================================
     Karen Salvador  25/08/2009  Creación
     ***************************************************************************************/
    public HashMap getOrderPortabilityReturn(long lNpOrderid) throws Exception, SQLException {
        HashMap hshRetorno=new HashMap();
        String strMessage=null;
        OracleCallableStatement cstmt = null;
        Connection conn=null;
        ResultSet rs = null;
        PortabilityOrderBean objOrderPortabilityReturn = null;
        conn = Proveedor.getConnection();

        try{
            String strSql = "BEGIN NP_PORTABILITY_PROCESS_PKG.SP_GET_ORD_PORTABILITY_RETURN( ? , ? , ? ); end;";
            cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
            cstmt.setLong(1, lNpOrderid);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, Types.CHAR);
            cstmt.execute();
            strMessage = cstmt.getString(3);

            if (strMessage == null){
                rs = (ResultSet)cstmt.getObject(2);
                if (rs.next()) {
                    objOrderPortabilityReturn = new  PortabilityOrderBean();
                    objOrderPortabilityReturn.setNpParticipantDescription(rs.getString("nppartipantdescription"));
                }
                hshRetorno.put("objOrderPortabilityReturn",objOrderPortabilityReturn);
                hshRetorno.put("strMessage",strMessage);
            }

        }catch(Exception e){
            hshRetorno.put("strMessage",e.getMessage());
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        return hshRetorno;
    }


    /**************************************************************************************************
     Method : getOrderPortabilityReturnHome
     Purpose: Obteniene el detalle de la Orden de Portabilidad de Retorno cuando Nextel es cedente
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     Karen Salvador  03/09/2009  Creación
     **************************************************************************************************/
    public HashMap getOrderPortabilityReturnHome(long lNpOrderid) throws Exception, SQLException {
        HashMap hshRetorno=new HashMap();
        PortabilityContactBean objPortabilityContactBean = null;
        String strMessage=null;
        OracleCallableStatement cstmt = null;
        Connection conn=null;
        ResultSet rs = null;
        conn = Proveedor.getConnection();

        try{
            String strSql = "BEGIN ORDERS.NP_PORTABILITY_PROCESS_PKG.SP_GET_PORTABILITY_LOW_HEADER( ? , ? , ? ); end;";
            cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
            cstmt.setLong(1, lNpOrderid);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, Types.CHAR);
            cstmt.execute();
            strMessage = cstmt.getString(3);

            if (strMessage == null){
                rs = (ResultSet)cstmt.getObject(2);
                if (rs.next()) {
                    objPortabilityContactBean = new  PortabilityContactBean();
                    objPortabilityContactBean.setNpreceptorname(rs.getString("nom_receptor"));
                    objPortabilityContactBean.setNpconctactname(rs.getString("nom_contact"));
                    objPortabilityContactBean.setNpcontactemail(rs.getString("email_contact"));
                    objPortabilityContactBean.setNpcontactphone(rs.getString("tel_contact"));
                    objPortabilityContactBean.setNpcontactfax(rs.getString("fax_contacto"));
                }
                hshRetorno.put("objOrderPortabilityReturnHome",objPortabilityContactBean);
                hshRetorno.put("strMessage",strMessage);
            }

        }catch(Exception e){
            hshRetorno.put("strMessage",e.getMessage());
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        return hshRetorno;
    }



    /**
     Method : getPortabItemDeviceList
     Purpose: Proceso para recuperar el estado de la orden (en que inbox se encuentra).
     Developer                Fecha       Comentario
     =====================    ==========  ======================================================================
     Lazo de la Vega David    15/09/2009  Creación
     */

    public HashMap getStatusOrder(long npOrderId) throws Exception,SQLException {

        HashMap objHashMap = new HashMap();
        ArrayList list = null;
        OracleCallableStatement cstmt = null;
        Connection conn=null;
        ResultSet rs = null;
        String strMessage = null;
        String strStatus = null;

        try{

            String sqlStr =  "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_GET_STATUS_ORDER(?,?,?); END;";
            conn = Proveedor.getConnection();
            list = new ArrayList();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, npOrderId);
            cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            strMessage = cstmt.getString(3);
            if (!StringUtils.isNotBlank(strMessage)) {
                strStatus = cstmt.getString(2);

            }

            objHashMap.put("strMessage",strMessage);
            objHashMap.put("strStatus",strStatus);

        }catch(Exception e){
            throw new Exception(e);
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return objHashMap;

    }

    /**
     Method : getItemsPortLst
     Purpose: Obtiene lista de item de la portability item a ser portados.
     Developer                Fecha       Comentario
     =====================    ==========  ======================================================================
     Lazo de la Vega David    15/09/2009  Creación
     */

    public HashMap getItemsPortLst(long npOrderId)throws Exception,SQLException { //1

        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        HashMap objHashMap = new HashMap();
        ArrayList list = null;
        Connection conn=null;
        String strMessage = null;
        PortabilityOrderBean objPOBean = null;

        try{
            String sqlStr =  "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_GET_ITEMS_PORT_LST2(?,?,?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            list = new ArrayList();
            cstmt.setLong(1, npOrderId);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.execute();

            strMessage = cstmt.getString(3);
            if (!StringUtils.isNotBlank(strMessage)) {
                rs = (ResultSet) cstmt.getObject(2);

                while (rs.next()) {
                    objPOBean = new PortabilityOrderBean();
                    objPOBean.setNpOrderid(rs.getLong(1));
                    objPOBean.setNpItemid(rs.getLong(2));
                    objPOBean.setNpOrderParentId(rs.getString(3));
                    list.add(objPOBean);
                }
            }

            objHashMap.put("strMessage",strMessage);
            objHashMap.put("objItemList",list);

        }catch(Exception e){
            throw new Exception(e);
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return objHashMap;

    }


    /**
     Method : getPortabItemPortabList
     Purpose: Obtiene lista de numeros portados de la np_portability_item.
     Developer                Fecha       Comentario
     =====================    ==========  ======================================================================
     Lazo de la Vega David    15/09/2009  Creación
     */

    public HashMap getPortabItemPortabList(long npItemId)throws Exception,SQLException {  //2

        HashMap objHashMap = new HashMap();
        ArrayList list = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        Connection conn=null;
        String strMessage = null;
        PortabilityOrderBean objPOBean = null;
        int i = 1;

        try{

            String sqlStr =  "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_GET_PROCESS_HIGH_PORTAB(?, ?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            list = new ArrayList();
            cstmt.setLong(1, npItemId);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.execute();

            strMessage = cstmt.getString(3);
            if (!StringUtils.isNotBlank(strMessage)) {
                rs = (ResultSet) cstmt.getObject(2);

                while (rs.next()) {
                    objPOBean = new PortabilityOrderBean();
                    objPOBean.setNpPortabOrderId(rs.getLong(1));
                    objPOBean.setNpPhoneNumber(rs.getString(2));
                    objPOBean.setNpModalityContract(rs.getString(3));
                    objPOBean.setNpShippingDateMessage((String)MiUtil.getDate(rs.getTimestamp(4), "dd/MM/yyyy HH:mm"));
                    objPOBean.setNpLastStateProcess(rs.getString(5));
                    objPOBean.setNpLastStateDesc(rs.getString(6));
                    objPOBean.setNpErrorIntegrity(rs.getString(7));
                    objPOBean.setNpErrorIntegrityDesc(rs.getString(8));
                    objPOBean.setNpReasonRejection(rs.getString(9));
                    objPOBean.setNpReasonRejectionDesc(rs.getString(10));
                    objPOBean.setNpScheduleDeadline((String)MiUtil.getDate(rs.getTimestamp(11), "dd/MM/yyyy HH:mm"));
                    objPOBean.setNpExecutionDeadline((String)MiUtil.getDate(rs.getTimestamp(12), "dd/MM/yyyy HH:mm"));
                    objPOBean.setNpExecutionDate((String)MiUtil.getDate(rs.getTimestamp(13), "dd/MM/yyyy HH:mm"));
                    objPOBean.setNpCorrected(rs.getString(14));
                    objPOBean.setNpState(rs.getString(15));
                    objPOBean.setNpItemid(rs.getLong(16));
                    objPOBean.setNpItemDeviceId(rs.getLong(17));
                    objPOBean.setNpContract(rs.getString(18));
                    objPOBean.setNpStateDesc(rs.getString(19));
                    objPOBean.setNpApplicationId(rs.getString(20));
                    objPOBean.setNpScheduleDBscs(rs.getString(21));
                    objPOBean.setNpPhoneNumberWN(rs.getString(22)); /*Teléfono con formato World number*/
                    //P1D - Lee Rosales
                    objPOBean.setNpAmountDue(rs.getDouble(23));
                    objPOBean.setNpCurrencyType(rs.getString(24));
                    objPOBean.setNpCurrencyDesc(rs.getString(25));
                    objPOBean.setNpExpirationDateReceipt(rs.getDate(26));
                    objPOBean.setNpReleaseDate(rs.getDate(27));
                    objPOBean.setNpPortabItemId(rs.getLong(28)); //SubsanacionDeuda PRY-1531
                    objPOBean.setNpAssignor(rs.getString(29)); //SubsanacionDeuda PRY-1531
                    list.add(objPOBean);
                }
            }

            objHashMap.put("strMessage",strMessage);
            objHashMap.put("objArrayList",list);

        }catch(Exception e){
            objHashMap.put("strMessage",e.getMessage());
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return objHashMap;
    }


    /**
     Method : deleteUpdateItemByPortabItem
     Purpose: Proceso que elimina ItemDevice y Items de la Portability Item.
     Developer                Fecha       Comentario
     =====================    ==========  ======================================================================
     Lazo de la Vega David    20/08/2009  Creación
     */
    public String deleteUpdateItemByPortabItem(long npOrderId, java.sql.Connection conn) throws Exception, SQLException {

        OracleCallableStatement cstmt = null;
        String strMessage = null;
        String sqlStr = "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_DEL_ITEM_BY_PORTAB_ITEM(?,?) ; END;";
        try{

            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, npOrderId);
            cstmt.registerOutParameter(2, Types.VARCHAR);
            cstmt.executeQuery();
            strMessage   = cstmt.getString(2);
        }
        catch (Exception e) {
            throw new Exception(e);
        }
        finally{
            closeObjectsDatabase(null,cstmt,null);
        }
        return strMessage;
    }


    /**
     Method : getParentOrder
     Purpose: Obtiene la orden padre de una orden.
     Developer                Fecha       Comentario
     =====================    ==========  ======================================================================
     Lazo de la Vega David    22/10/2009  Creación
     */

    public HashMap getParentOrder(long npOrderId)throws Exception,SQLException {

        HashMap objHashMap = new HashMap();
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        Connection conn=null;
        String strMessage = null;
        PortabilityOrderBean objPOBean = null;

        try{

            String sqlStr =  "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_GET_PARENT_ORDER(?, ?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, npOrderId);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.execute();

            strMessage = cstmt.getString(3);
            if (!StringUtils.isNotBlank(strMessage)) {
                rs = (ResultSet) cstmt.getObject(2);

                if(rs.next()) {
                    objPOBean = new PortabilityOrderBean();
                    objPOBean.setNpParentOrderId(rs.getLong(1));
                    objPOBean.setNpOrderid(rs.getLong(2));
                }
            }

            objHashMap.put("strMessage",strMessage);
            objHashMap.put("objPOBean",objPOBean);

        }catch(Exception e){
            objHashMap.put("strMessage",e.getMessage());
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return objHashMap;
    }

    /**
     Method : getChildOrder
     Purpose: Obtiene la orden hija de una orden.
     Developer                Fecha       Comentario
     =====================    ==========  ======================================================================
     Lazo de la Vega David    22/10/2009  Creación
     */

    public HashMap getChildOrder(long npOrderId)throws Exception,SQLException {

        HashMap objHashMap = new HashMap();
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        Connection conn=null;
        String strMessage = null;
        PortabilityOrderBean objPOBean = null;

        try{

            String sqlStr =  "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_GET_CHILD_ORDER(?, ?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, npOrderId);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.execute();

            strMessage = cstmt.getString(3);
            if (!StringUtils.isNotBlank(strMessage)) {
                rs = (ResultSet) cstmt.getObject(2);

                if(rs.next()) {
                    objPOBean = new PortabilityOrderBean();
                    objPOBean.setNpOrderid(rs.getLong(1));
                    objPOBean.setNpParentOrderId(rs.getLong(2));
                }
            }

            objHashMap.put("strMessage",strMessage);
            objHashMap.put("objPOBean2",objPOBean);

        }catch(Exception e){
            objHashMap.put("strMessage",e.getMessage());
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return objHashMap;
    }

    public HashMap calculateBalanceTotal(long orderParentId,long orderChildId)throws Exception,SQLException {

        HashMap objHashMap = new HashMap();
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        Connection conn=null;
        String strMessage = null;
        PortabilityOrderBean objPOBean = null;

        try{

            String sqlStr =  "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_CALCULATE_BALANCE(?, ?, ?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, orderParentId);
            cstmt.setLong(2, orderChildId);
            cstmt.registerOutParameter(3, OracleTypes.CURSOR);
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
            cstmt.execute();

            strMessage = cstmt.getString(4);
            if (!StringUtils.isNotBlank(strMessage)) {
                rs = (ResultSet) cstmt.getObject(3);

                if(rs.next()) {
                    objPOBean = new PortabilityOrderBean();
                    objPOBean.setNpPaymentParent(rs.getLong(1));
                    objPOBean.setNpPaymentChild(rs.getLong(2));
                    objPOBean.setNpPaymentAmount(rs.getLong(3));
                    objPOBean.setNpBalance(rs.getLong(4));
                }
            }

            objHashMap.put("strMessage",strMessage);
            objHashMap.put("objPOBean3",objPOBean);

        }catch(Exception e){
            objHashMap.put("strMessage",e.getMessage());
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return objHashMap;
    }

    /****************************************************************************************************************
     Method : getParentChildOrder
     Purpose: Obtiene el título de la Orden (Padre ó Hija) y su Nro de Orden respectivo.
     Developer       Fecha       Comentario
     =============   ==========  ====================================================================================
     Karen Salvador  23/10/2009  Creación
     ****************************************************************************************************************/
    public HashMap getParentChildOrder(long npOrderid) throws SQLException, Exception {

        Connection conn = null;
        ResultSet rs=null;
        OracleCallableStatement cstm = null;
        ArrayList list  = null;
        PortabilityOrderBean portabilityOrderBean =  new PortabilityOrderBean();
        String strMessage = null;
        HashMap objHashMapResultado = new HashMap();


        try{
            String strSql = " BEGIN ORDERS.NP_PORTABILITY_PROCESS_PKG.SP_GET_PARENT_CHILD_ORDER(?,?,?,?); END;";
            conn = Proveedor.getConnection();
            cstm = (OracleCallableStatement) conn.prepareCall(strSql);
            cstm.setLong(1,npOrderid);
            cstm.registerOutParameter(2, OracleTypes.VARCHAR);
            cstm.registerOutParameter(3, OracleTypes.NUMBER);
            cstm.registerOutParameter(4, OracleTypes.VARCHAR);

            cstm.executeUpdate();

            strMessage  = cstm.getString(4);

            if( strMessage == null){
                portabilityOrderBean.setNpTitleOrdeParentChild(cstm.getString(2));
                portabilityOrderBean.setNpOrderidParentChild(cstm.getLong(3));
            }

        }catch (Exception e) {
            logger.error(formatException(e));
            objHashMapResultado.put("strMessage",e.getMessage());
        } finally{
            try{
                closeObjectsDatabase(conn,cstm,rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        objHashMapResultado.put("strMessage",strMessage);
        objHashMapResultado.put("objParentChildList",portabilityOrderBean);

        return objHashMapResultado;

    }

    /**
     Method : getInboxOrder
     Purpose: Obtiene el inbox de la orden.
     Developer               Fecha       Comentario
     =============           ==========  ======================================================================
     David Lazo de la Vega    29/10/2009  Creación
     **/
    public HashMap getInboxOrder(long strOrderId) throws SQLException, Exception {

        HashMap hshDataMap = new HashMap();
        HashMap hshtList = null;
        String strInboxStatus = "";
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strMensaje = null;
        try {
            String sqlStr = "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_GET_STATUS_ORDER(?,?,?); END;";
            System.out.println(sqlStr);
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, strOrderId);
            cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            strMensaje = cstmt.getString(3);
            if (!StringUtils.isNotBlank(strMensaje)) {
                strInboxStatus = cstmt.getString(2);
            }

            hshDataMap.put(Constante.MESSAGE_OUTPUT,strMensaje);
            hshDataMap.put("strInboxStatus",strInboxStatus);

        } catch (Exception e) {
            logger.error(formatException(e));
            throw new Exception(e);
        } finally {
            try {
                closeObjectsDatabase(conn, cstmt, rs);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return hshDataMap;
    }

    /**
     Method : checkNumberPorted
     Purpose: Verifica si el número ya se encuentra portado.
     Developer               Fecha       Comentario
     =============           ==========  ======================================================================
     David Lazo de la Vega    29/10/2009  Creación
     **/
    public HashMap checkNumberPorted(String npphoneNumber, long lOrderId) throws SQLException, Exception {

        HashMap hshDataMap = new HashMap();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strMensaje = null;
        try {
            String sqlStr = "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_CHECK_NUMBER_PORTED(?,?,?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setString(1, npphoneNumber);
            cstmt.setLong(2,lOrderId);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            strMensaje = cstmt.getString(3);
            hshDataMap.put(Constante.MESSAGE_OUTPUT,strMensaje);

        } catch (Exception e) {
            logger.error(formatException(e));
            throw new Exception(e);
        } finally {
            try {
                closeObjectsDatabase(conn, cstmt, rs);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return hshDataMap;
    }

    /**
     * Motivo: Obteniene los estados de los controles en la seccion de Portabilidad
     * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
     * <br>Fecha: 16/11/2009
     * @param     lOrderId       Es el Id de la Orden
     * @param     strMsgError    Contiene el mensaje de error, si existiera, al ejecutar el procedimiento almacenado
     * @return    HashMap
     **/
    public HashMap getOrderScreenFieldPorta(long lOrderId,String strPageName) throws SQLException, Exception {
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        Connection conn=null;
        String strMessage=null;
        HashMap h = new HashMap();
        HashMap hshRetorno = new HashMap();
        try{
            String sqlStr = "BEGIN NP_PORTABILITY01_PKG.SP_GET_ORDER_SCREEN_FIELD(?, ?, ?, ? ); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, lOrderId);
            cstmt.setString(2, strPageName);
            cstmt.registerOutParameter(3, OracleTypes.CURSOR);
            cstmt.registerOutParameter(4, Types.CHAR);
            cstmt.execute();
            strMessage = cstmt.getString(4);
            if( strMessage == null ){
                rs = (ResultSet)cstmt.getObject(3);
                if (rs.next()) {
                    h.put("sendsp",rs.getString("wv_sendsp"));
                    h.put("sendpp",rs.getString("wv_sendpp"));
                    h.put("sendsvp",rs.getString("wv_sendsvp"));
                    h.put("createsubsanacion",rs.getString("wv_createsubsanacion"));
                }
            }
            hshRetorno.put("hshData",h);
            hshRetorno.put("strMessage",strMessage);
        }catch(Exception e){
            hshRetorno.put("strMessage",e.getMessage());
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        return hshRetorno;
    }

    /**
     * Motivo: Verifica si el usuario pertene a determinado Inbox
     * <br>Realizado por: <a href="mailto:david.lazo@nextel.com.pe">David Lazo de la Vega</a>
     * <br>Fecha: 30/11/2009
     * @return    integer
     */
    public int getValidInboxContent(int lUserId, String strInboxName, int lInboxType) throws SQLException, Exception{

        int lValidation = 0;
        String strMessage = null;
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;

        String sqlStr = " { ? = call WEBSALES.NPSL_ACCESS_PKG.FX_VALIDATE_USER_INBOX(?,?,?) }";

        try {
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.registerOutParameter(1, OracleTypes.NUMBER);
            cstmt.setInt(2, lUserId);
            cstmt.setString(3, strInboxName);
            cstmt.setInt(4, lInboxType);
            cstmt.executeQuery();

            lValidation = cstmt.getInt(1);

        } catch (Exception e) {
            logger.error(formatException(e));
            throw new Exception(e);
        }
        finally{
            try{
                closeObjectsDatabase(conn,cstmt,rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return lValidation;
    }

    /**
     * Motivo: Metodo que retorna un valor dependiendo si el usuario tiene acceso a los Inbox de Edición de Ordenes de Portabilidad (TIENDA01,ADM_VENTAS,VENTAS)
     * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomás Mogrovejo Acosta</a>
     * <br>Fecha: 28/05/2009
     * @return    integer
     */

    public int getValInboxEditableUser(String strInbox, String strLogin) throws SQLException, Exception{

        int intValidationInboxUser = 0;
        String strMessage = null;
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;

        String sqlStr = "BEGIN ORDERS.NP_PORTABILITY_PROCESS_PKG.SP_VALIDATE_PORTA_INBOXUSER(?, ? , ? , ?); END;";

        try {
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setString(1, strInbox);
            cstmt.setString(2, strLogin);
            cstmt.registerOutParameter(3, Types.INTEGER);
            cstmt.registerOutParameter(4, Types.CHAR);
            cstmt.execute();
            strMessage = cstmt.getString(4);
            if (!StringUtils.isNotBlank(strMessage)) {
                intValidationInboxUser = cstmt.getInt(3);
            }
        } catch (Exception e) {
            logger.error(formatException(e));
            throw new Exception(e);
        }
        finally{
            closeObjectsDatabase(conn,cstmt,rs);
        }
        return intValidationInboxUser;
    }


    /**
     * Motivo: Metodo que retorna un valor si es que el Item es rechazado y listo para una posible subsanación
     * <br>Realizado por: <a href="mailto:tomas.mogrovejo@nextel.com.pe">Tomás Mogrovejo Acosta</a>
     * <br>Fecha: 18/01/2010
     * @return    integer
     */

    public int getValMsgSub(long lOrderId, long npItemId) throws SQLException, Exception{

        int intValMsgSub = 0;
        String strMessage = null;
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;

        String sqlStr = "BEGIN ORDERS.NP_PORTABILITY_PROCESS_PKG.SP_VALIDATE_MSG_SUB(?, ? , ? ,?); END;";

        try {
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setLong(1, lOrderId);
            cstmt.setLong(2, npItemId);
            cstmt.registerOutParameter(3, Types.INTEGER);
            cstmt.registerOutParameter(4, Types.CHAR);
            cstmt.execute();
            strMessage = cstmt.getString(4);
            if (!StringUtils.isNotBlank(strMessage)) {
                intValMsgSub = cstmt.getInt(3);
            }
        } catch (Exception e) {
            logger.error(formatException(e));
            throw new Exception(e);
        }
        finally{
            closeObjectsDatabase(conn,cstmt,rs);
        }
        return intValMsgSub;
    }


    /**************************************************************************************
     * Motivo: Obtiene el listado de Documentos del Cliente
     * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
     * <br>Fecha: 25/01/2010
     * @return    HashMap
     ***************************************************************************************/
    public  HashMap getDocumentList(String strNptable) throws SQLException, Exception{
        HashMap hshData=new HashMap();
        String strMessage=null;
        ArrayList list = new ArrayList();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;

        String sqlStr = "BEGIN ORDERS.NP_PORTA_CONFIGURATION_PKG.SP_GET_VALUE(?, ?, ?); END;";
        try{
            conn = Proveedor.getConnection();

            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

            cstmt.setString(1, strNptable);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, Types.VARCHAR);

            cstmt.execute();
            strMessage = cstmt.getString(3);
            rs = (ResultSet)cstmt.getObject(2);

            while (rs.next()) {
                HashMap h = new HashMap();
                h.put("swvalue", rs.getString(1));
                h.put("swdescription", rs.getString(2));
                list.add(h);
            }

            hshData.put("arrDocumentList",list);
            hshData.put("strMessage",strMessage);
            return hshData;
        }
        catch(Exception e){
            throw new Exception(e);
        }
        finally{
            try{
                closeObjectsDatabase(conn,cstmt,rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }

    }


    /**************************************************************************************
     Method : getValidateDocument
     Purpose: Verifica si es posible mostrar la sección de Documentos.
     Developer                Fecha       Comentario
     =====================    ==========  ======================================================================
     KSALVADOR                27/01/2010  Creación
     ***************************************************************************************/
    public String getValidateDocument(long lorderid, String strNpdocument, String strNptypedocument, java.sql.Connection conn) throws Exception, SQLException {

        OracleCallableStatement cstmt = null;
        String strMessage = null;
        String sqlStr = "BEGIN ORDERS.NP_PORTABILITY_PROCESS_PKG.SP_VALIDATE_PORTABILITY_ORDER(?,?,?,?) ; END;";
        try{

            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, lorderid);
            cstmt.setString(2, strNpdocument);
            cstmt.setString(3, strNptypedocument);
            cstmt.registerOutParameter(4, Types.VARCHAR);
            cstmt.executeQuery();

            strMessage = cstmt.getString(4);

        }
        catch (Exception e) {
            throw new Exception(e);
        }
        finally{
            closeObjectsDatabase(null,cstmt,null);
        }

        return strMessage;
    }


    /**************************************************************************************
     Method : updatePortabilityOrderDetail
     Purpose: Actualiza los datos de los Documentos cuando la orden esta en modo de consulta.
     Developer                Fecha       Comentario
     =====================    ==========  ======================================================================
     KSALVADOR                27/01/2010  Creación
     ***************************************************************************************/
    public String updatePortabilityOrderDetail(long lorderid, String strNpdocument, String strNptypedocument) throws Exception, SQLException {

        OracleCallableStatement cstmt = null;
        String strMessage = null;
        Connection conn = null;
        String sqlStr = "BEGIN ORDERS.NP_PORTABILITY_PROCESS_PKG.SP_UPD_PORTABILIY_ORDER_DETAIL(?,?,?,?) ; END;";
        try{

            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, lorderid);
            cstmt.setString(2, strNpdocument);
            cstmt.setString(3, strNptypedocument);
            cstmt.registerOutParameter(4, Types.VARCHAR);
            cstmt.executeUpdate();

            strMessage = cstmt.getString(4);

        }
        catch (Exception e) {
            throw new Exception(e);
        }
        finally{
            closeObjectsDatabase(conn,cstmt,null);
        }
        return strMessage;
    }

    /**
     * Motivo: Obtiene el resultado de éxito o error del envío del SP
     * <br>Realizado por: <a href="mailto:lee.rosales@hp.com">Lee Rosales</a>
     * <br>Fecha: 29/05/2014
     * @param     orderId
     * @return    HashMap
     */
    public int getFlagSendSP(long orderId) throws SQLException, Exception {

        OracleCallableStatement cstmt = null;
        Connection conn=null;
        int result = 0;

        try{

            String sqlStr =  "{ ? = call PORTABILITY.NP_PORTABILITY_PKG.FX_GET_FLAG_SEND_SP(?) }";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

            cstmt.registerOutParameter(1, Types.NUMERIC);
            cstmt.setLong(2, orderId);
            cstmt.execute();

            result = cstmt.getInt(1);

        }catch(Exception e){
            return -3;
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        return result;
    }

    /**
     * Motivo: Obtiene el resultado de éxito o error del envío del PP
     * <br>Realizado por: <a href="mailto:lee.rosales@hp.com">Lee Rosales</a>
     * <br>Fecha: 29/05/2014
     * @param     orderId
     * @return    HashMap
     */
    public int getFlagSendPP(long orderId) throws SQLException, Exception {

        OracleCallableStatement cstmt = null;
        Connection conn=null;
        int result = 0;

        try{

            String sqlStr =  "{ ? = call PORTABILITY.NP_PORTABILITY_PKG.FX_GET_FLAG_SEND_PP(?) }";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

            cstmt.registerOutParameter(1, Types.NUMERIC);
            cstmt.setLong(2, orderId);
            cstmt.execute();

            result = cstmt.getInt(1);

        }catch(Exception e){
            return -3;
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        return result;
    }

    /**
     Method : updateCusTypeScheduleDays
     Purpose: Realiza actualización del tipo de cliente y los dias para programar.
     Developer                Fecha       Comentario
     =====================    ==========  ======================================================================
     Lizbeth Valencia         14/10/2014  Creación
     */

    public String updateCusTypeScheduleDays(long npPortabOrderId,String strCustomerType,String strScheduleDays,java.sql.Connection conn) throws Exception,SQLException {

        OracleCallableStatement cstmt = null;
        String strMessage = null;

        try{
            String sqlStrIns = "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_UPD_CUSTYPE_SCHEDULEDAYS(?,?,?,?); END;";
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStrIns);

            cstmt.setLong(1,npPortabOrderId);
            cstmt.setString(2,strCustomerType);
            cstmt.setString(3,strScheduleDays);
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
            cstmt.executeUpdate();

            strMessage = cstmt.getString(4);

        }catch (Exception e) {
            throw new Exception(e);

        }finally{
            closeObjectsDatabase(null,cstmt,null);
        }

        return strMessage;
    }

    /**
     Method : updateValidItemDevice
     Purpose: Realiza validaciones de los deviceID, ordenando los items por ID y los device por ID y actualizando los DeviceId de los portaItem.
     Developer                Fecha       Comentario
     =====================    ==========  ======================================================================
     JVILLANUEVA              15/12/2015  Creación
     */

    public String updateValidItemDevice(long npOrderId,java.sql.Connection conn) throws Exception,SQLException {

        OracleCallableStatement cstmt = null;
        String strMessage = null;
        String sqlStrIns = "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_UPD_PORTABILITYHIGH_ITEMID(?,?); END;";
        //conn = Proveedor.getConnection();

        try{

            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStrIns);

            cstmt.setLong(1, npOrderId);
            cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
            cstmt.executeUpdate();

            strMessage = cstmt.getString(2);


        }catch (Exception e) {
            throw new Exception(e);

        }finally{
            closeObjectsDatabase(null,cstmt,null);
        }

        return strMessage;

    }
    /**
     * Motivo: Obtiene tipo de division y expresion regular para validacion de numeros a portar
     * <br>Realizado por: <a href="mailto:anthony.mateo@teamsoft.com.pe">Anthony Mateo</a>
     * <br>Fecha: 19/05/2016
     * @param     orderId
     * @return    HashMap
     */
    public String getExpValidation(long orderId) throws  SQLException, Exception{
        if(logger.isDebugEnabled())
            logger.debug("--Inicio--");

        Connection conn = null;
        OracleCallableStatement cstmt = null;
        String strMessage = null;
        String strResult = null;
        String sqlStr = "BEGIN ORDERS.NP_PORTABILITY01_PKG.SP_GET_EXP_VALIDATE(?,?,?); END;";
        try{
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);

            cstmt.setLong(1, orderId);
            cstmt.registerOutParameter(2,OracleTypes.VARCHAR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            strMessage = cstmt.getString(3);
            if(StringUtils.isBlank(strMessage)) {
                strResult = cstmt.getString(2);
                System.out.println("strResult-Division|ExpReg-->"+strResult);
            }
        }
        catch(Exception e){
            throw new Exception(e);
        }
        finally{
            closeObjectsDatabase(conn, cstmt,null);
        }

        return strResult;
    }

}