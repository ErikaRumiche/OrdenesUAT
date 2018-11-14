package pe.com.nextel.dao;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import oracle.sql.CLOB;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import pe.com.nextel.bean.DocAssigneeBean;
import pe.com.nextel.bean.DocumentGenerationBean;
import pe.com.nextel.bean.LogWSBean;
import pe.com.nextel.bean.NpTableBean;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by JCASTILLO on 04/04/2017.
 * [PRY-0787]
 */
public class DigitalDocumentDAO extends GenericDAO {

    private static final Logger logger = Logger.getLogger(DigitalDocumentDAO.class);
    public HashMap getEmail(Long customerId,Long siteId) throws Exception{
        HashMap  objHashMap = new HashMap();
        Connection conn=null;
        ResultSet rs = null;
        OracleCallableStatement stmt = null;
        String storedProcedure = "begin SWBAPPS.SPI_GET_LAST_CONTACT_EMAIL(?,?,?,?); end;";
        String strMessage;
        String email;

        try{
            conn = Proveedor.getConnection();
            stmt = (OracleCallableStatement) conn.prepareCall(storedProcedure);
            stmt.setLong(1, customerId);
            stmt.setLong(2, siteId);
            stmt.registerOutParameter(3, OracleTypes.VARCHAR);
            stmt.registerOutParameter(4, OracleTypes.VARCHAR);
            stmt.execute();
            email=stmt.getString(3);
            strMessage=stmt.getString(4);
            objHashMap.put("strMessage",strMessage);
            objHashMap.put("email",email);
        }catch (Exception e){
            logger.error("Exception",e);
            throw new Exception(e);
        }finally{
            //Se limpian las variables de conexión
            closeObjectsDatabase(conn, stmt, rs);
        }
        return objHashMap;
    }

    public HashMap saveDocumentGeneration(DocumentGenerationBean documentGenerationBean) throws Exception {
        HashMap  objHashMap = new HashMap();
        Connection conn         = null;
        OracleCallableStatement cstmt = null;
        String strMessage = null;
        try {
            String sqlStr = "BEGIN ORDERS.SPI_INS_DOCUMENT_GENERATION(?, ?); END;";

            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            StructDescriptor sdDocumentGeneration = StructDescriptor.createDescriptor("ORDERS.TO_DOCUMENT_GENERATION", conn);
            Object[] objDocument = { null,
                    documentGenerationBean.getOrderId(),
                    documentGenerationBean.getCustomerId(),
                    documentGenerationBean.getSpecificationId(),
                    documentGenerationBean.getTrxType(),
                    documentGenerationBean.getBuildingId(),
                    documentGenerationBean.getAssigneeId(),
                    documentGenerationBean.getRequestNumber(),
                    documentGenerationBean.getEmail(),
                    documentGenerationBean.getEmailNullF(),
                    documentGenerationBean.getSignatureType(),
                    null,
                    documentGenerationBean.getSignatureReason(),
                    null,
                    documentGenerationBean.getGenerationStatus(),
                    documentGenerationBean.getEmailStatus(),
                    documentGenerationBean.getGeneratedFiles(),
                    documentGenerationBean.getCreatedBy(),
                    null,
                    null,
                    null,
                    documentGenerationBean.getTrxWs()
            };

            STRUCT stcDocument = new STRUCT(sdDocumentGeneration, conn, objDocument);
            cstmt.setSTRUCT(1,stcDocument);
            cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            strMessage = cstmt.getString(2);
        }catch(Exception e){
            logger.error("Exception",e);
            strMessage = e.getMessage();
        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
        return objHashMap;
    }

    public HashMap updateDocumentGeneration(DocumentGenerationBean documentGenerationBean) throws Exception {
        HashMap  objHashMap = new HashMap();
        Connection conn         = null;
        OracleCallableStatement cstmt = null;
        String strMessage = null;
        try {
            String sqlStr = "BEGIN ORDERS.SPI_UPD_DOCUMENT_GENERATION(?, ?); END;";

            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            StructDescriptor sdDocumentGeneration = StructDescriptor.createDescriptor("ORDERS.TO_DOCUMENT_GENERATION", conn);
            Object[] objDocument = { documentGenerationBean.getId(),
                    documentGenerationBean.getOrderId(),
                    documentGenerationBean.getCustomerId(),
                    documentGenerationBean.getSpecificationId(),
                    documentGenerationBean.getTrxType(),
                    documentGenerationBean.getBuildingId(),
                    documentGenerationBean.getAssigneeId(),
                    documentGenerationBean.getRequestNumber(),
                    documentGenerationBean.getEmail(),
                    documentGenerationBean.getEmailNullF(),
                    documentGenerationBean.getSignatureType(),
                    null,
                    documentGenerationBean.getSignatureReason(),
                    null,
                    documentGenerationBean.getGenerationStatus(),
                    documentGenerationBean.getEmailStatus(),
                    documentGenerationBean.getGeneratedFiles(),
                    documentGenerationBean.getCreatedBy(),
                    documentGenerationBean.getCreatedDate(),
                    documentGenerationBean.getModificationBy(),
                    null,
                    documentGenerationBean.getTrxWs()
            };

            STRUCT stcDocument = new STRUCT(sdDocumentGeneration, conn, objDocument);
            cstmt.setSTRUCT(1,stcDocument);
            cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            strMessage = cstmt.getString(2);
        }catch(Exception e){
            logger.error("Exception",e);
            strMessage = e.getMessage();
        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
        return objHashMap;
    }


    public HashMap getDocumentGeneration(Long orderId) throws Exception {
        HashMap  objHashMap = new HashMap();
        Connection conn         = null;
        OracleCallableStatement cstmt = null;
        String strMessage;
        DocumentGenerationBean documentGenerationBean = new DocumentGenerationBean();
        try {
            String sqlStr = "BEGIN ORDERS.SPI_GET_DOCUMENT_GENERATION(?, ?, ?); END;";

            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setLong(1,orderId);
            cstmt.registerOutParameter(2, OracleTypes.STRUCT, "ORDERS.TO_DOCUMENT_GENERATION");
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            strMessage = cstmt.getString(3);
            if(strMessage == null){
                STRUCT struct = (STRUCT)cstmt.getObject(2);
                Object[] documentGenerationTO = struct.getAttributes();
                documentGenerationBean.setId(MiUtil.getLong(documentGenerationTO[0]));
                documentGenerationBean.setOrderId(MiUtil.getLong(documentGenerationTO[1]));
                documentGenerationBean.setCustomerId(MiUtil.getLong(documentGenerationTO[2]));
                documentGenerationBean.setSpecificationId(MiUtil.getLong(documentGenerationTO[3]));
                documentGenerationBean.setTrxType(MiUtil.getInt(documentGenerationTO[4]));
                documentGenerationBean.setBuildingId(MiUtil.getLong(documentGenerationTO[5]));
                documentGenerationBean.setAssigneeId(MiUtil.getLong(documentGenerationTO[6]));
                documentGenerationBean.setRequestNumber(MiUtil.getString(documentGenerationTO[7]));
                documentGenerationBean.setEmail(MiUtil.getString(documentGenerationTO[8]));
                documentGenerationBean.setEmailNullF(MiUtil.getInt(documentGenerationTO[9]));
                documentGenerationBean.setSignatureType(MiUtil.getInt(documentGenerationTO[10]));
                documentGenerationBean.setSignatureTypeLabel(MiUtil.getString(documentGenerationTO[11]));
                documentGenerationBean.setSignatureReason(MiUtil.getInt(documentGenerationTO[12]));
                documentGenerationBean.setSignatureReasonLabel(MiUtil.getString(documentGenerationTO[13]));
                documentGenerationBean.setGenerationStatus(MiUtil.getInt(documentGenerationTO[14]));
                documentGenerationBean.setEmailStatus(MiUtil.getInt(documentGenerationTO[15]));
                documentGenerationBean.setCreatedBy(MiUtil.getString(documentGenerationTO[17]));
                documentGenerationBean.setTrxWs(MiUtil.getString(documentGenerationTO[21]));
                objHashMap.put("documentGenerationBean",documentGenerationBean);

            }
        }catch(Exception e){
            strMessage = e.getMessage();
            logger.error("Exception",e);
        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
        return objHashMap;
    }

    public HashMap validateVIDD(Long buildingId, int typeId, Long divisionId, Long specificationId,  int channelId,Long customerId)  throws Exception {
        HashMap  hashMap = new HashMap();
        Connection conn         = null;
        OracleCallableStatement cstmt = null;
        String strMessage;
        try {
            String sqlStr = "BEGIN SWBAPPS.SPI_STATUS_GENERAL_DIGITAL(?,?,?,?,?,?,?,?,?,?,?); END;";

            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setLong(1,buildingId);
            cstmt.setInt(2, typeId);
            cstmt.setLong(3, divisionId);
            cstmt.setLong(4, specificationId);
            cstmt.setInt(5,channelId);
            cstmt.setLong(6, customerId);
            cstmt.registerOutParameter(7, OracleTypes.INTEGER);
            cstmt.registerOutParameter(8, OracleTypes.INTEGER);
            cstmt.registerOutParameter(9, OracleTypes.INTEGER);
            cstmt.registerOutParameter(10, OracleTypes.INTEGER);
            cstmt.registerOutParameter(11, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            hashMap.put("identverifsection",cstmt.getInt(7));
            hashMap.put("assigneesection",cstmt.getInt(8));
            hashMap.put("digitalsection",cstmt.getInt(9));
            hashMap.put("cpufsection",cstmt.getInt(10));

            strMessage=cstmt.getString(11);
        }catch(Exception e){
            strMessage = e.getMessage();
            logger.error("Error en validateVIDD",e);
        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        hashMap.put(Constante.MESSAGE_OUTPUT,strMessage);

        return hashMap;
    }

    public HashMap validateVIDD(Long buildingId, int typeId, Long divisionId, Long specificationId,  int channelId,Long customerId,String generatorType)  throws Exception {
        HashMap  hashMap = new HashMap();
        Connection conn         = null;
        OracleCallableStatement cstmt = null;
        String strMessage;
        try {
            String sqlStr = "BEGIN SWBAPPS.SPI_STATUS_GENERAL_DIGITAL2(?,?,?,?,?,?,?,?,?,?,?,?); END;";

            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setLong(1,buildingId);
            cstmt.setInt(2, typeId);
            cstmt.setLong(3, divisionId);
            cstmt.setLong(4, specificationId);
            cstmt.setInt(5,channelId);
            cstmt.setLong(6, customerId);
            cstmt.setString(7, generatorType);
            cstmt.registerOutParameter(8, OracleTypes.INTEGER);
            cstmt.registerOutParameter(9, OracleTypes.INTEGER);
            cstmt.registerOutParameter(10, OracleTypes.INTEGER);
            cstmt.registerOutParameter(11, OracleTypes.INTEGER);
            cstmt.registerOutParameter(12, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            hashMap.put("identverifsection",cstmt.getInt(8));
            hashMap.put("assigneesection",cstmt.getInt(9));
            hashMap.put("digitalsection",cstmt.getInt(10));
            hashMap.put("cpufsection",cstmt.getInt(11));

            strMessage=cstmt.getString(12);
        }catch(Exception e){
            strMessage = e.getMessage();
            logger.error("Error en validateVIDD2",e);
        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        hashMap.put(Constante.MESSAGE_OUTPUT,strMessage);

        return hashMap;
    }

    public HashMap getValidaCategoriaSolucion(int order) throws SQLException{
        System.out.println("[getValidaCategoriaSolucion]");
        HashMap objHashMap= new HashMap();
        Connection conn = null;
        ResultSet rs=null;
        OracleCallableStatement cstmt = null;
        String strMessage="";
        String resultado="";

        String sqlStr = "BEGIN SWBAPPS.NP_DOC_DIGITAL_PKG.SP_GET_VALID_CATEGORYSOLUTION(?,?,?); END;";

        try {
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setInt(1, order);
            cstmt.registerOutParameter(2, Types.VARCHAR);
            cstmt.registerOutParameter(3, Types.VARCHAR);

            cstmt.execute();

            strMessage = cstmt.getString(2);
            if(StringUtils.isBlank(strMessage)){
                resultado=cstmt.getString(3);
            }
        } catch (Exception e) {
            logger.error("Exception",e);
        }finally {
            closeObjectsDatabase(conn, cstmt, null);

        }

        objHashMap.put("strResultado",resultado);
        objHashMap.put("strMessage",strMessage);
        return  objHashMap;
    }



    /**
     * Motivo: Obtiene el si el cliente es Empresa (E) o Persona (P)
     * <br>Realizado por: <a href="mailto:johana.rios@soapros.pe">Johana Rios</a>
     * <br>Fecha: 31/03/2017
     * @param     int iCustomerid
     * @return	  String
     */

    public HashMap getValueNpDigitalConfig(String domain,int origen,int typetrx, int channel, int section)
            throws Exception {
        HashMap hshDataMap = new HashMap();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strMessage = null;
        NpTableBean nptableBean;
        ArrayList arrNpTable = new ArrayList();
        try {
            String sqlStr = "BEGIN SWBAPPS.SPI_GET_LIST_CONFIG(?,?,?,?,?,?,?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setString(1, domain);
            cstmt.setInt(2, origen);
            cstmt.setInt(3, typetrx);
            cstmt.setInt(4, channel);
            cstmt.setInt(5, section);
            cstmt.registerOutParameter(6, OracleTypes.CURSOR);
            cstmt.registerOutParameter(7, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            strMessage = cstmt.getString(7);

            if (StringUtils.isBlank(strMessage)) {
                rs = (ResultSet) cstmt.getObject(6);
                while (rs.next()) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("npvalue",(rs.getString("npvalue")));
                    hashMap.put("npvaluedesc",(rs.getString("npvaluedesc")));
                    arrNpTable.add(hashMap);
                }
            }
        }catch (Exception e) {
            logger.error("Exception",e);
            throw new Exception(e);
        }finally{
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        hshDataMap.put("objArrayList", arrNpTable);
        hshDataMap.put("strMessage", strMessage);
        return hshDataMap;
    }

    /**
     * Motivo: Obtiene el si el cliente es Empresa (E) o Persona (P)
     * <br>Realizado por: <a href="mailto:johana.rios@soapros.pe">Johana Rios</a>
     * <br>Fecha: 31/03/2017
     * @param     int iCustomerid
     * @return	  String
     */
    public String getClientTypeFromCustomerId(int iCustomerid) throws Exception,SQLException{

        Connection conn = null;
        OracleCallableStatement cstm = null;
        String strClientType = "";
        String sqlStr = "{ ? = call SWBAPPS.FXI_GET_CHANNEL(?)}";
        try{
            conn = Proveedor.getConnection();
            cstm = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstm.registerOutParameter(1,Types.VARCHAR);
            cstm.setInt(2,iCustomerid);
            cstm.executeUpdate();
            strClientType = cstm.getString(1);
        }
        catch(Exception e){
            logger.error("Exception",e);
            throw new Exception(e);
        }
        finally{
            closeObjectsDatabase(conn, cstm, null);
        }
        return strClientType;
    }

    public HashMap getSolNumber(int source,Long specificationId, int channel, int division)  throws Exception {
        HashMap  hashMap = new HashMap();
        Connection conn         = null;
        OracleCallableStatement cstmt = null;
        String strMessage;
        String solNumber=null;
        try {
            String sqlStr = "BEGIN SWBAPPS.SPI_GET_SOLNUMBER(?,?,?,?,?,?,?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setInt(1,source);
            cstmt.setLong(2,specificationId);
            cstmt.setNull(3,OracleTypes.NUMBER);
            cstmt.setInt(4,channel);
            cstmt.setInt(5,division);
            cstmt.registerOutParameter(6, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(7, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            strMessage=cstmt.getString(7);
            if(StringUtils.isBlank(strMessage)){
                solNumber=cstmt.getString(6);
            }
        }catch(Exception e){
            strMessage = e.getMessage();
            logger.error("Error en getSolNumber",e);
        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        hashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
        hashMap.put("solNumber",solNumber);

        return hashMap;
    }

    public HashMap saveLog(LogWSBean logWSBean) throws Exception {
        HashMap  objHashMap = new HashMap();
        Connection conn         = null;
        OracleCallableStatement cstmt = null;
        String strMessage = null;
        Clob clobReq = null;
        Clob clobResp = null;
        try {
            String sqlStr = "BEGIN SWBAPPS.SPI_INS_WS_LOG(?,?,?,?,?,?,?,?,?,?,?,?,?,?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setInt(1,logWSBean.getSource());
            cstmt.setLong(2,logWSBean.getTrxId());
            cstmt.setString(3,logWSBean.getWsName());
            cstmt.setString(4,logWSBean.getWsOperation());
            clobReq = CLOB.createTemporary(conn, false, CLOB.DURATION_SESSION);
            clobReq.setString(1, logWSBean.getInput());
            cstmt.setClob(5, clobReq);
            clobResp = CLOB.createTemporary(conn, false, CLOB.DURATION_SESSION);
            clobResp.setString(1,logWSBean.getOutput());
            cstmt.setClob(6,clobResp);
            cstmt.setTimestamp(7,new Timestamp(logWSBean.getStartDate().getTime()));
            cstmt.setTimestamp(8,new Timestamp(logWSBean.getEndDate().getTime()));
            cstmt.setDouble(9,logWSBean.getTimeOut());
            cstmt.setInt(10,logWSBean.getStatus());
            cstmt.setInt(11,MiUtil.getInt(logWSBean.getResponseCode()));
            cstmt.setString(12,logWSBean.getResponseMessage());
            cstmt.setString(13,logWSBean.getCreatedBy());
            cstmt.registerOutParameter(14, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            strMessage = cstmt.getString(14);
        }catch(Exception e){
            logger.error("Exception",e);
            strMessage = e.getMessage();
        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
        return objHashMap;
    }

    public HashMap saveDocAssignee(DocAssigneeBean assigneeBean) throws Exception {
        HashMap  objHashMap = new HashMap();
        Connection conn         = null;
        OracleCallableStatement cstmt = null;
        String strMessage = null;
        int idAssignee = 0;
        try {
            String sqlStr = "BEGIN ORDERS.SPI_INS_DOC_ASSIGNEE(?, ?, ?); END;";

            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            StructDescriptor sdDocAssignee = StructDescriptor.createDescriptor("ORDERS.TO_DOC_ASSIGNEE", conn);
            Object[] objSolution = { null,
                    assigneeBean.getOrderId(),
                    assigneeBean.getFirstName(),
                    assigneeBean.getLastName(),
                    assigneeBean.getFamilyName(),
                    assigneeBean.getTypeDoc(),
                    assigneeBean.getNumDoc(),
                    assigneeBean.getCreatedBy(),
                    null,
                    null,
                    null
            };

            STRUCT stcComment = new STRUCT(sdDocAssignee, conn, objSolution);
            cstmt.setSTRUCT(1,stcComment);
            cstmt.registerOutParameter(2, OracleTypes.NUMBER);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            idAssignee = cstmt.getInt(2);
            strMessage = cstmt.getString(3);

        }catch(Exception e){
            logger.error("Exception",e);
            strMessage = e.getMessage();
        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        objHashMap.put("idAssignee",idAssignee);
        objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);

        return objHashMap;
    }

    public HashMap updateDocAssignee(DocAssigneeBean assigneeBean) throws Exception {
        HashMap  objHashMap = new HashMap();
        Connection conn         = null;
        OracleCallableStatement cstmt = null;
        String strMessage = null;
        try {
            String sqlStr = "BEGIN ORDERS.SPI_UPD_DOC_ASSIGNEE(?, ?); END;";

            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            StructDescriptor sdDocAssignee = StructDescriptor.createDescriptor("ORDERS.TO_DOC_ASSIGNEE", conn);
            Object[] objSolution = { assigneeBean.getDocAssigneeId(),
                    assigneeBean.getOrderId(),
                    assigneeBean.getFirstName(),
                    assigneeBean.getLastName(),
                    assigneeBean.getFamilyName(),
                    assigneeBean.getTypeDoc(),
                    assigneeBean.getNumDoc(),
                    assigneeBean.getCreatedBy(),
                    assigneeBean.getCreatedDate(),
                    assigneeBean.getModificationBy(),
                    null
            };

            STRUCT stcComment = new STRUCT(sdDocAssignee, conn, objSolution);
            cstmt.setSTRUCT(1, stcComment);
            cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            strMessage = cstmt.getString(2);

        }catch(Exception e){
            logger.error("Exception",e);
            strMessage = e.getMessage();
        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);

        return objHashMap;
    }

    public HashMap getDocAssignee(Long orderId) throws Exception {
        HashMap  objHashMap = new HashMap();
        Connection conn         = null;
        OracleCallableStatement cstmt = null;
        String strMessage;
        DocAssigneeBean docAssigneeBean = new DocAssigneeBean();
        try {
            String sqlStr = "BEGIN ORDERS.SPI_GET_DOC_ASSIGNEE(?, ?, ?); END;";

            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setLong(1,orderId);
            cstmt.registerOutParameter(2, OracleTypes.STRUCT, "ORDERS.TO_DOC_ASSIGNEE");
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            strMessage = cstmt.getString(3);
            if(strMessage == null){
                STRUCT struct = (STRUCT)cstmt.getObject(2);
                Object[] docAssigneeTO = struct.getAttributes();
                docAssigneeBean.setDocAssigneeId(MiUtil.getLong(docAssigneeTO[0]));
                docAssigneeBean.setOrderId(MiUtil.getLong(docAssigneeTO[1]));
                docAssigneeBean.setFirstName(MiUtil.getString(docAssigneeTO[2]));
                docAssigneeBean.setLastName(MiUtil.getString(docAssigneeTO[3]));
                docAssigneeBean.setFamilyName(MiUtil.getString(docAssigneeTO[4]));
                docAssigneeBean.setTypeDoc(MiUtil.getString(docAssigneeTO[5]));
                docAssigneeBean.setNumDoc(MiUtil.getString(docAssigneeTO[6]));
                objHashMap.put("docAssigneeBean", docAssigneeBean);

            }else{
                objHashMap.put("docAssigneeBean",null);
            }
        }catch(Exception e){
            strMessage = e.getMessage();
            logger.error("Exception",e);
        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
        return objHashMap;
    }

    /**
     * Motivo: Obtiene el flag de caso especial
     * <br>Realizado por: <a href="mailto:johana.rios@soapros.pe">Johana Rios</a>
     * <br>Fecha: 25/04/2017
     * @param     int iOrderId
     * @return	  String
     */

    public HashMap getFlagSpecialCase(Long iOrderid)  throws Exception {
        HashMap  hashMap = new HashMap();
        Connection conn         = null;
        OracleCallableStatement cstmt = null;
        String strMessage;
        try {
            String sqlStr = "BEGIN ORDERS.SPI_GET_FLAG_SPECIAL_CASE(?,?,?); END;";

            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setLong(1, iOrderid);
            cstmt.registerOutParameter(2, OracleTypes.INTEGER);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            hashMap.put("strFlagSpecialCase",cstmt.getInt(2));

            strMessage=cstmt.getString(3);
        }catch(Exception e){
            strMessage = e.getMessage();
            logger.error("Error en validateVIDD Special Case",e);
        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        hashMap.put(Constante.MESSAGE_OUTPUT,strMessage);

        return hashMap;
    }

    /**
     * Motivo: Valida los archivos adjuntos
     * <br>Realizado por: <a href="mailto:jefferson.vergara@soapros.pe">Jefferson Vergara</a>
     * <br>Fecha: 08/05/2017
     * @param     int iFileSize, String strFileType
     * @return	  String
     */

    public HashMap getvalidateUploadedFiles(int iFileTypeId, int iFileSize, String strContentType)  throws Exception {
        HashMap  hashMap = new HashMap();
        Connection conn         = null;
        OracleCallableStatement cstmt = null;
        String strMessage;
        try {
            String sqlStr = "BEGIN SWBAPPS.SPI_VALIDATE_UPLOADED_FILES(?,?,?,?,?); END;";

            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setInt(1, iFileTypeId);
            cstmt.setInt(2, iFileSize);
            cstmt.setString(3,strContentType);
            cstmt.registerOutParameter(4, OracleTypes.INTEGER);
            cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            hashMap.put("strResult",cstmt.getInt(4));

            strMessage=cstmt.getString(5);
        }catch(Exception e){
            strMessage = e.getMessage();
            logger.error("Error en validate uploaded files ",e);
        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        hashMap.put(Constante.MESSAGE_OUTPUT,strMessage);

        return hashMap;
    }

    /**
     * Motivo: Valida si se debe mostrar la seccion de adjuntar documentos
     * <br>Realizado por: <a href="mailto:jefferson.vergara@soapros.pe">Jefferson Vergara</a>
     * <br>Fecha: 09/05/2017
     * @param     int iorigen, int itypetrx
     * @return	  String
     */
    public HashMap getAttachSectionStatus(int iorigen, int itypetrx, int ibuildingid)  throws Exception {
        HashMap  hashMap = new HashMap();
        Connection conn         = null;
        OracleCallableStatement cstmt = null;
        String strMessage;
        try {
            String sqlStr = "BEGIN SWBAPPS.SPI_VALIDATE_ATTACH_SECTION(?,?,?,?,?); END;";

            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setInt(1,iorigen);
            cstmt.setInt(2, itypetrx);
            cstmt.setInt(3, ibuildingid);
            cstmt.registerOutParameter(4, OracleTypes.INTEGER);
            cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            hashMap.put("strResult",cstmt.getInt(4));

            strMessage=cstmt.getString(5);
        }catch(Exception e){
            strMessage = e.getMessage();
            logger.error("Error in validate attach documents section ",e);
        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        hashMap.put(Constante.MESSAGE_OUTPUT,strMessage);

        return hashMap;
    }




    public HashMap updateOrderCode(String sOrdercode, long iOrderid) throws Exception {
        HashMap  objHashMap = new HashMap();
        Connection conn         = null;
        OracleCallableStatement cstmt = null;
        String strMessage = null;
        try {
            String sqlStr = "BEGIN ORDERS.SPI_UPD_ORDER_CODE(?, ?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);

            cstmt.setString(1, sOrdercode);
            cstmt.setLong(2, iOrderid);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            strMessage = cstmt.getString(3);

        }catch(Exception e){
            logger.error("Exception",e);
            strMessage = e.getMessage();
        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);

        return objHashMap;
    }

    /**
     * Motivo: Obtiene la razon de la generaci?n de documentos con firma manual
     * <br>Realizado por: <a href="mailto:juan.andre.teofilo-calderon.magui?a@dxc.com">Andre Calderon</a>
     * <br>Fecha: 11/05/2017
     * @param     int iorigen, int itypetrx
     * @return	  String
     */
    public HashMap getSignatureReason(int iorigen, int itypetrx)  throws Exception {
        HashMap  hashMap = new HashMap();
        Connection conn         = null;
        OracleCallableStatement cstmt = null;
        String strMessage;
        try {
            String sqlStr = "BEGIN SWBAPPS.SPI_GET_SIGNATURE_REASON(?,?,?,?); END;";



            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setInt(1, itypetrx);
            cstmt.setInt(2, iorigen);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            hashMap.put("strResult",cstmt.getString(3));

            strMessage=cstmt.getString(4);
        }catch(Exception e){
            strMessage = e.getMessage();
            logger.error("Error in get Signed Reason ",e);
        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        hashMap.put(Constante.MESSAGE_OUTPUT,strMessage);

        return hashMap;
    }

    /**
     * Motivo: Obtiene la informaci?n de la digitalizaicon de documentos en Incidentes
     * <br>Realizado por: <a href="mailto:juan.andre.teofilo-calderon.magui?a@dxc.com">Andre Calderon</a>
     * <br>Fecha: 11/05/2017
     * @param     Long incidentId
     * @return	  String
     */
    public HashMap getDocumentGenerationInc(Long incidentId) throws Exception {
        HashMap  objHashMap = new HashMap();
        Connection conn         = null;
        OracleCallableStatement cstmt = null;
        String strMessage;
        DocumentGenerationBean documentGenerationBean = new DocumentGenerationBean();
        try {
            String sqlStr = "BEGIN INCIDENT_WEB.SPI_GET_DOCUMENT_GENERATION(?, ?, ?); END;";

            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setLong(1,incidentId);
            cstmt.registerOutParameter(2, OracleTypes.STRUCT, "INCIDENT_WEB.TO_DOCUMENT_GENERATION");
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            strMessage = cstmt.getString(3);
            if(strMessage == null){
                STRUCT struct = (STRUCT)cstmt.getObject(2);
                Object[] documentGenerationTO = struct.getAttributes();
                documentGenerationBean.setId(MiUtil.getLong(documentGenerationTO[0]));
                documentGenerationBean.setOrderId(MiUtil.getLong(documentGenerationTO[1]));
                documentGenerationBean.setCustomerId(MiUtil.getLong(documentGenerationTO[2]));
                documentGenerationBean.setSpecificationId(MiUtil.getLong(documentGenerationTO[3]));
                documentGenerationBean.setResolutionId(MiUtil.getLong(documentGenerationTO[4]));
                documentGenerationBean.setTrxType(MiUtil.getInt(documentGenerationTO[5]));
                documentGenerationBean.setBuildingId(MiUtil.getLong(documentGenerationTO[6]));
                documentGenerationBean.setAssigneeId(MiUtil.getLong(documentGenerationTO[7]));
                documentGenerationBean.setRequestNumber(MiUtil.getString(documentGenerationTO[8]));
                documentGenerationBean.setEmail(MiUtil.getString(documentGenerationTO[10]));
                documentGenerationBean.setEmailNullF(MiUtil.getInt(documentGenerationTO[11]));
                documentGenerationBean.setSignatureType(MiUtil.getInt(documentGenerationTO[12]));
                documentGenerationBean.setSignatureTypeLabel(MiUtil.getString(documentGenerationTO[13]));
                documentGenerationBean.setSignatureReason(MiUtil.getInt(documentGenerationTO[14]));
                documentGenerationBean.setSignatureReasonLabel(MiUtil.getString(documentGenerationTO[15]));
                objHashMap.put("documentGenerationBean",documentGenerationBean);

            }
        }catch(Exception e){
            strMessage = e.getMessage();
            logger.error("Exception",e);
        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
        return objHashMap;
    }


    /**
     * Motivo: Valida si debe firmar de forma digital o manual segun la VIA
     * <br>Realizado por: <a href="mailto:jefferson.vergara@soapros.pe">Jefferson Vergara</a>
     * <br>Fecha: 09/05/2017
     * @param     String orderId, int customerId, String strLogin, int iChkAssignee, String strDocNumAssignee, int iDocTypeAssignee
     * @return	  String
     */
    public HashMap validateVIATechnicalService(String orderId, int customerId, String strLogin, int iChkAssignee, String strDocNumAssignee, String iDocTypeAssignee) throws SQLException {
        HashMap  hashMap = new HashMap();
        Connection conn         = null;
        OracleCallableStatement cstmt = null;
        String strMessage;
        try {
            String sqlStr = "BEGIN SWBAPPS.SPI_VALIDATE_VIA_TECHNICAL_SERVICE(?,?,?,?,?,?,?,?,?); END;";

            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setString(1, orderId);
            cstmt.setInt(2, customerId);
            cstmt.setString(3, strLogin);
            cstmt.setInt(4, iChkAssignee);
            cstmt.setString(5, strDocNumAssignee);
            cstmt.setString(6,iDocTypeAssignee);

            cstmt.registerOutParameter(7, OracleTypes.INTEGER);
            cstmt.registerOutParameter(8, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(9, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            hashMap.put("resultado",cstmt.getInt(7));
            hashMap.put("messagealert",cstmt.getString(8));

            strMessage=cstmt.getString(9);
        }catch(Exception e){
            strMessage = e.getMessage();
            logger.error("Error en validar VIA en Servicio Tecnico ",e);
        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        hashMap.put(Constante.MESSAGE_OUTPUT,strMessage);

        return hashMap;
    }

    public HashMap validateVerification(long order)  throws Exception {
        HashMap  hashMap = new HashMap();
        Connection conn         = null;
        OracleCallableStatement cstmt = null;
        String strMessage;
        try {
            String sqlStr = "BEGIN ORDERS.SPI_VALIDATE_VERIFICATION(?,?,?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setLong(1,order);
            cstmt.registerOutParameter(2, OracleTypes.INTEGER);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            hashMap.put(Constante.STR_RESULT,cstmt.getInt(2));
            strMessage=cstmt.getString(4);
        }catch(Exception e){
            strMessage = e.getMessage();
            logger.error("Error in validate attach documents section ",e);
        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        hashMap.put(Constante.MESSAGE_OUTPUT,strMessage);

        return hashMap;
    }
    public HashMap getOneValueNpDigitalConfig(String domain,Integer value,String valuedesc) throws Exception {
        HashMap hashMap = new HashMap();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strMessage = null;
        NpTableBean nptableBean;
        ArrayList arrNpTable = new ArrayList();
        try {
            String sqlStr = "BEGIN SWBAPPS.SPI_GET_CONFIGURATION(?,?,?,?,?,?,?,?,?,?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setString(1, domain);
            cstmt.registerOutParameter(2, OracleTypes.INTEGER);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.setInt(2, value);
            cstmt.setString(3, valuedesc);
            cstmt.registerOutParameter(4, OracleTypes.INTEGER);
            cstmt.registerOutParameter(5, OracleTypes.INTEGER);
            cstmt.registerOutParameter(6, OracleTypes.INTEGER);
            cstmt.registerOutParameter(7, OracleTypes.INTEGER);
            cstmt.registerOutParameter(8, OracleTypes.INTEGER);
            cstmt.registerOutParameter(9, OracleTypes.INTEGER);
            cstmt.registerOutParameter(10, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            strMessage = cstmt.getString(10);
            hashMap.put(Constante.STR_RESULT,cstmt.getString(3));

        }catch (Exception e) {
            logger.error("Exception",e);
            throw new Exception(e);
        }finally{
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        hashMap.put("strMessage", strMessage);
        return hashMap;
    }

    public HashMap getInfoEmail(String user, int trxType, int pvtType,String transType,int channel,long orderId,long customerId,int divisionId,long specificationId,int lines)  throws Exception {
        HashMap  hashMap = new HashMap();
        Connection conn         = null;
        OracleCallableStatement cstmt = null;
        String strMessage;
        try {
            String sqlStr = "BEGIN SWBAPPS.SPI_GET_INFO_EMAIL(?,?,?,?,?,?,?,?,?,?,?,?,?); END;";

            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setString(1, user);
            cstmt.setInt(2, trxType);
            cstmt.setInt(3, pvtType);
            cstmt.setString(4, transType);
            cstmt.setInt(5, channel);
            cstmt.setLong(6, orderId);
            cstmt.setLong(7, customerId);
            cstmt.setInt(8, divisionId);
            cstmt.setLong(9, specificationId);
            cstmt.setInt(10, lines);
            cstmt.registerOutParameter(11, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(12, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(13, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            hashMap.put("xmlVariable",cstmt.getString(11));
            hashMap.put("templateCode",cstmt.getString(12));
            strMessage=cstmt.getString(13);
        }catch(Exception e){
            strMessage = e.getMessage();
            logger.error("Error in getInfoEmail",e);
        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        hashMap.put(Constante.MESSAGE_OUTPUT,strMessage);

        return hashMap;
    }

    public HashMap getInfoEmailPvt(int trxType, int pvtType,int channel,long orderId,long customerId,int divisionId,long specificationId,int lines)  throws Exception {
        HashMap  hashMap = new HashMap();
        Connection conn         = null;
        OracleCallableStatement cstmt = null;
        String strMessage;
        try {
            String sqlStr = "BEGIN SWBAPPS.SPI_GET_INFO_EMAIL_PVT2(?,?,?,?,?,?,?,?,?,?,?); END;";

            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setInt(1, trxType);
            cstmt.setInt(2, pvtType);
            cstmt.setInt(3, channel);
            cstmt.setLong(4, orderId);
            cstmt.setLong(5, customerId);
            cstmt.setInt(6, divisionId);
            cstmt.setLong(7, specificationId);
            cstmt.setInt(8, lines);
            cstmt.registerOutParameter(9, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(10, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(11, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            hashMap.put("xmlVariable",cstmt.getString(9));
            hashMap.put("templateCode",cstmt.getString(10));
            strMessage=cstmt.getString(11);
        }catch(Exception e){
            strMessage = e.getMessage();
            logger.error("Error in getInfoEmail",e);
        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        hashMap.put(Constante.MESSAGE_OUTPUT,strMessage);

        return hashMap;
    }



    /**
     * Motivo: Actualiza el tipo de firma si existe VI
     * <br>Realizado por: <a href="mailto:jefferson.vergara@soapros.pe">Jefferson Vergara</a>
     * <br>Fecha: 08/05/2017
     * @param     int iorderid, String strLoginid
     * @return	  String
     */

    public HashMap updateGenerationVI(int iorderid, String strLoginid)  throws Exception {
        HashMap  hashMap = new HashMap();
        Connection conn         = null;
        OracleCallableStatement cstmt = null;
        String strMessage;
        DocumentGenerationBean documentGenerationBean = new DocumentGenerationBean();
        try {
            String sqlStr = "BEGIN ORDERS.SPI_UPD_DOCUMENT_GEN_VI(?,?,?,?); END;";

            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);

            cstmt.setInt(1, iorderid);
            cstmt.setString(2, strLoginid);
            cstmt.registerOutParameter(3, OracleTypes.STRUCT ,"ORDERS.TO_DOCUMENT_GENERATION");
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            strMessage=cstmt.getString(4);

            if(strMessage == null){
                STRUCT struct = (STRUCT)cstmt.getObject(3);
                Object[] documentGenerationTO = struct.getAttributes();

                documentGenerationBean.setId(MiUtil.getLong(documentGenerationTO[0]));
                documentGenerationBean.setOrderId(MiUtil.getLong(documentGenerationTO[1]));
                documentGenerationBean.setCustomerId(MiUtil.getLong(documentGenerationTO[2]));
                documentGenerationBean.setSpecificationId(MiUtil.getLong(documentGenerationTO[3]));
                documentGenerationBean.setTrxType(MiUtil.getInt(documentGenerationTO[4]));
                documentGenerationBean.setBuildingId(MiUtil.getLong(documentGenerationTO[5]));
                documentGenerationBean.setAssigneeId(MiUtil.getLong(documentGenerationTO[6]));
                documentGenerationBean.setRequestNumber(MiUtil.getString(documentGenerationTO[7]));
                documentGenerationBean.setEmail(MiUtil.getString(documentGenerationTO[8]));
                documentGenerationBean.setEmailNullF(MiUtil.getInt(documentGenerationTO[9]));
                documentGenerationBean.setSignatureType(MiUtil.getInt(documentGenerationTO[10]));
                documentGenerationBean.setSignatureTypeLabel(MiUtil.getString(documentGenerationTO[11]));
                documentGenerationBean.setSignatureReason(MiUtil.getInt(documentGenerationTO[12]));
                documentGenerationBean.setSignatureReasonLabel(MiUtil.getString(documentGenerationTO[13]));
                documentGenerationBean.setGenerationStatus(MiUtil.getInt(documentGenerationTO[14]));
                hashMap.put("documentGenerationBean",documentGenerationBean);

            }

        }catch(Exception e){
            strMessage = e.getMessage();
            logger.error("Error en actualizar tipo de firma ",e);
        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        hashMap.put(Constante.MESSAGE_OUTPUT,strMessage);

        return hashMap;
    }

    /**
     * Motivo: Actualiza el tipo de firma sin VI
     * <br>Realizado por: <a href="mailto:jefferson.vergara@soapros.pe">Jefferson Vergara</a>
     * <br>Fecha: 08/05/2017
     * @param     int iorderid, String strLoginid
     * @return	  String
     */

    public HashMap updateGenerationSIGN(int iorderid,int ireason, String strLoginid)  throws Exception {
        HashMap  hashMap = new HashMap();
        Connection conn         = null;
        OracleCallableStatement cstmt = null;
        String strMessage;
        DocumentGenerationBean documentGenerationBean = new DocumentGenerationBean();
        try {
            String sqlStr = "BEGIN ORDERS.SPI_UPD_DOCUMENT_GEN_SIGN(?,?,?,?,?); END;";

            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);

            cstmt.setInt(1, iorderid);
            cstmt.setInt(2, ireason);
            cstmt.setString(3, strLoginid);
            cstmt.registerOutParameter(4, OracleTypes.STRUCT, "ORDERS.TO_DOCUMENT_GENERATION");
            cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            strMessage=cstmt.getString(5);
            if(strMessage == null){
                STRUCT struct = (STRUCT)cstmt.getObject(4);
                Object[] documentGenerationTO = struct.getAttributes();
                documentGenerationBean.setId(MiUtil.getLong(documentGenerationTO[0]));
                documentGenerationBean.setOrderId(MiUtil.getLong(documentGenerationTO[1]));
                documentGenerationBean.setCustomerId(MiUtil.getLong(documentGenerationTO[2]));
                documentGenerationBean.setSpecificationId(MiUtil.getLong(documentGenerationTO[3]));
                documentGenerationBean.setTrxType(MiUtil.getInt(documentGenerationTO[4]));
                documentGenerationBean.setBuildingId(MiUtil.getLong(documentGenerationTO[5]));
                documentGenerationBean.setAssigneeId(MiUtil.getLong(documentGenerationTO[6]));
                documentGenerationBean.setRequestNumber(MiUtil.getString(documentGenerationTO[7]));
                documentGenerationBean.setEmail(MiUtil.getString(documentGenerationTO[8]));
                documentGenerationBean.setEmailNullF(MiUtil.getInt(documentGenerationTO[9]));
                documentGenerationBean.setSignatureType(MiUtil.getInt(documentGenerationTO[10]));
                documentGenerationBean.setSignatureTypeLabel(MiUtil.getString(documentGenerationTO[11]));
                documentGenerationBean.setSignatureReason(MiUtil.getInt(documentGenerationTO[12]));
                documentGenerationBean.setSignatureReasonLabel(MiUtil.getString(documentGenerationTO[13]));
                documentGenerationBean.setGenerationStatus(MiUtil.getInt(documentGenerationTO[14]));
                hashMap.put("documentGenerationBean",documentGenerationBean);
            }

        }catch(Exception e){
            strMessage = e.getMessage();
            logger.error("Error en actualizar tipo de firma ",e);
        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        hashMap.put(Constante.MESSAGE_OUTPUT,strMessage);

        return hashMap;
    }

    public HashMap getPvtType(long specificationId)  throws Exception {
        HashMap  hashMap = new HashMap();
        Connection conn         = null;
        OracleCallableStatement cstmt = null;
        String strMessage;
        try {
            String sqlStr = "BEGIN ORDERS.SPI_GET_PVT_TYPE(?,?,?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setLong(1, specificationId);
            cstmt.registerOutParameter(2, OracleTypes.INTEGER);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            hashMap.put("type",cstmt.getInt(2));
            strMessage=cstmt.getString(3);
        }catch(Exception e){
            strMessage = e.getMessage();
            logger.error("Error in getPvtType",e);
        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        hashMap.put(Constante.MESSAGE_OUTPUT,strMessage);

        return hashMap;
    }

    /**
     * Motivo: Obtiene el tipo de Documento del Cliente desde el Client Id
     * <br>Realizado por: <a href="mailto:jefferson.vergara@soapros.pe">Jefferson Vergara</a>
     * <br>Fecha: 20/06/2017
     * @param     int iorderid
     * @return	  String
     */

    public HashMap get_ident_info_customer(long lcustomerId)  throws Exception {
        HashMap  hashMap = new HashMap();
        Connection conn         = null;
        OracleCallableStatement cstmt = null;
        String strMessage;
        try {
            String sqlStr = "BEGIN SWBAPPS.SPI_GET_IDENT_INFO_CUSTOMER(?,?,?,?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setLong(1,lcustomerId);
            cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            hashMap.put("wv_doctypecustomer",cstmt.getString(2));
            hashMap.put("wv_docnumcustomer",cstmt.getString(3));
            strMessage=cstmt.getString(4);
        }catch(Exception e){
            strMessage = e.getMessage();
            logger.error("Error in getting info customer ",e);
        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        hashMap.put(Constante.MESSAGE_OUTPUT,strMessage);

        return hashMap;
    }

    /**
     * Motivo: Obtiene el flag para listar documentos adjuntos
     * <br>Realizado por: <a href="mailto:juan.andre.teofilo-calderon.magui?a@dxc.com">Andre Calderon</a>
     * <br>Fecha: 22/08/2017
     * @param     int orderid, int incidentid
     * @return	  int
     */
    public HashMap getAttDocListFlag(int orderid, int incidentid)  throws Exception {
        HashMap  hashMap = new HashMap();
        Connection conn         = null;
        OracleCallableStatement cstmt = null;
        String strMessage;
        try {
            String sqlStr = "BEGIN SWBAPPS.SPI_GET_FLAG_ATT_DOC_LIST(?,?,?,?); END;";

            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setInt(1, orderid);
            cstmt.setInt(2, incidentid);
            cstmt.registerOutParameter(3, OracleTypes.NUMBER);
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
            cstmt.executeQuery();

            hashMap.put("strResult",cstmt.getInt(3));

            strMessage=cstmt.getString(4);
        }catch(Exception e){
            strMessage = e.getMessage();
            logger.error("Error in get Att_Doc_List_Dlag ",e);
        } finally {
            closeObjectsDatabase(conn,cstmt,null);
        }
        hashMap.put(Constante.MESSAGE_OUTPUT,strMessage);

        return hashMap;
    }

    public HashMap validateImeiLoan(long orderId) throws SQLException{
        logger.info("[validateImeiLoan]orderId:"+orderId);
        HashMap hashMap= new HashMap();
        Connection conn = null;
        ResultSet rs=null;
        OracleCallableStatement cstmt = null;
        String strMessage="";

        String sqlStr = "BEGIN ORDERS.NP_DOC_DIGITAL_PKG.SP_GET_VALIDATE_IMEILOAN(?,?,?); END;";

        try {
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setLong(1, orderId);
            cstmt.registerOutParameter(2, Types.INTEGER);
            cstmt.registerOutParameter(3, Types.VARCHAR);

            cstmt.execute();

            strMessage = cstmt.getString(3);
            if(StringUtils.isBlank(strMessage)){
                hashMap.put(Constante.STR_RESULT,cstmt.getInt(2));
            }
        } catch (Exception e) {
            logger.error("Exception",e);
        }finally {
            closeObjectsDatabase(conn, cstmt, null);

        }
        hashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
        return  hashMap;
    }
}