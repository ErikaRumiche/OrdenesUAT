package pe.com.nextel.dao;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;
import org.apache.commons.lang.StringUtils;
import pe.com.nextel.bean.*;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 *  Created by Practia on 08/07/2015.
 */
public class BiometricaDAO extends GenericDAO {

    public HashMap validOrderPendientes(int idOrder){
        HashMap hashMap= new HashMap();
        String strMessage="";
        hashMap.put("strMessage",strMessage);
        hashMap.put("resultado",1);

        return  hashMap;
    }

    /**
     * Motivo: Valida si se permite abrir la venta validacion biometrica/No biometrica
 * <br>Realizado por: <a href="mailto:rquispe@practiaconsulting.com">Ricardo Quispe</a>
 * <br>Fecha: 16/07/2015
     * MMONTOYA   04/08/2016 Se modifica por el proyecto Verificaci髇 Biom閠rica - Fase 2
     * @param orderId
     * @param specificationId
     * @param login
     * @param useCase
     * @return
     * @throws SQLException
     */
    public HashMap getValidActivation(int orderId, int specificationId, String login, String useCase) throws SQLException {
        System.out.println("[BiometricaDAO][getValidActivation] orderId" + orderId + " specificationId: " + specificationId + " login: " + login + " useCase: " + useCase);

        HashMap hashMap= new HashMap();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        String strMessage="";
        String authorizedUser ="";
        String authorizedDni ="";
        String authorizedpass="";
        int resultado=0;
        String sqlStr = "BEGIN CUSTOMER_AUTHENTICATION.NP_BIOMETRICA_PKG.SP_VALIDATE_BIOMETRIC_ACCESS(?,?,?,?,?,?,?,?,?); END;";

        try {
            conn = Proveedor.getConnection();
            cstmt=(OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setInt(1, orderId);
            cstmt.setInt(2, specificationId);
            cstmt.setString(3,login);
            cstmt.setString(4, useCase);
            cstmt.registerOutParameter(5, Types.VARCHAR);
            cstmt.registerOutParameter(6, OracleTypes.NUMBER);
            cstmt.registerOutParameter(7, Types.VARCHAR);
            cstmt.registerOutParameter(8, Types.VARCHAR);
            cstmt.registerOutParameter(9, Types.VARCHAR);
            cstmt.execute();

            strMessage = cstmt.getString(5);
            resultado=cstmt.getInt(6);
            if(StringUtils.isBlank(strMessage)){
                authorizedUser=cstmt.getString(7);
                authorizedDni=cstmt.getString(8);
                authorizedpass=cstmt.getString(9);
                System.out.println("resultado:"+resultado);
            }

        } catch (SQLException e) {
            System.out.println("[BiometricaDao]strMessage:"+e.getMessage());
            e.printStackTrace();
        }finally {
            try{
                closeObjectsDatabase(conn, cstmt, null);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        System.out.println("strMessage:"+strMessage+"resultado:"+resultado+"user:"+authorizedUser);

        hashMap.put("strMessage",strMessage);
        hashMap.put("resultado",resultado);
        hashMap.put("authorizedUser",authorizedUser);
        hashMap.put("authorizedDni",authorizedDni);
        hashMap.put("authorizedPass",authorizedpass);
        return  hashMap;
    }

    /**
     * Motivo: Obtiene el documento de la orden
     * <br>Realizado por: <a href="mailto:rquispe@practiaconsulting.com">Ricardo Quispe</a>
     * <br>Fecha: 23/07/2015
     * @param     order  Ej. 12702795
     * @return    String
     * */
    public String getDocumento(int order)throws SQLException{
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        String strMessage="";
        String resultado="";

        String sqlStr = "BEGIN CUSTOMER_AUTHENTICATION.NP_BIOMETRICA_PKG.SP_GET_DOCUMENT(?,?,?); END;";

        try {
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setInt(1,order);
            cstmt.registerOutParameter(2, Types.VARCHAR);
            cstmt.registerOutParameter(3, Types.VARCHAR);

            cstmt.execute();
            strMessage = cstmt.getString(2);
            if(StringUtils.isBlank(strMessage)){
                resultado= cstmt.getString(3);
            }

        }catch (SQLException e) {
            System.out.println("[BiometricaDao][strMessage]:"+e.getMessage());
            e.printStackTrace();
        }finally {
            try{
                closeObjectsDatabase(conn, cstmt, null);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }


        return  resultado;
    }

    /**
     * Motivo: Obtiene el listado de las acciones a tomar ejm: no biometrica y anular
     * <br>Realizado por: <a href="mailto:rquispe@practiaconsulting.com">Ricardo Quispe</a>
     * <br>Fecha: 23/07/2015
     * @return    HashMap
     * */
    public HashMap getListAction(int orderId)throws SQLException{
        System.out.println("[getListAction]");
        HashMap objHashMap= new HashMap();
        ArrayList list = new ArrayList();
        Connection conn = null;
        ResultSet rs=null;
        OracleCallableStatement cstmt = null;
        String strMessage="";
        String strSolution="";
        int nbPend = 0;

        String sqlStr = "BEGIN CUSTOMER_AUTHENTICATION.NP_BIOMETRICA_PKG.SP_GET_LIST_ACTION(?,?,?,?,?); END;";

        try {
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setInt(1, orderId);
            cstmt.registerOutParameter(2, Types.VARCHAR);
            cstmt.registerOutParameter(3, OracleTypes.NUMBER);
            cstmt.registerOutParameter(4, Types.VARCHAR);
            cstmt.registerOutParameter(5, OracleTypes.CURSOR);
            cstmt.execute();

            rs = (ResultSet)cstmt.getObject(5);
            strSolution  = cstmt.getString(2);
            nbPend = cstmt.getInt(3);
            strMessage = cstmt.getString(4);
            if(StringUtils.isBlank(strMessage)){
                while (rs.next()) {
                    HashMap h = new HashMap();
                    h.put("wn_npdomainid",rs.getString(1)+"");
                    h.put("wv_npdescription",rs.getString(2)==null?"":rs.getString(2));
                    list.add(h);
                }
            }
        } catch (SQLException e) {
            System.out.println("[BiometricaDao][strMessage]:"+e.getMessage());
            e.printStackTrace();
        }finally {
            try{
                closeObjectsDatabase(conn, cstmt, null);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }


        objHashMap.put("objArrayList",list);
        objHashMap.put("strSolution",strSolution);
        objHashMap.put("nbPend",nbPend);
        objHashMap.put("strMessage",strMessage);
        return  objHashMap;
    }

    /**
     * Motivo: Obtiene el listado de los motivos segun la accion tomada
     * <br>Realizado por: <a href="mailto:rquispe@practiaconsulting.com">Ricardo Quispe</a>
     * <br>Fecha: 23/07/2015
     * @return    HashMap
     * */
    public HashMap getListReason(Integer idAccion, String solution, int nbPend)throws SQLException{
        System.out.println("[getListReason]");
        HashMap objHashMap= new HashMap();
        ArrayList list = new ArrayList();
        Connection conn = null;
        ResultSet rs=null;
        OracleCallableStatement cstmt = null;
        String strMessage="";

        String sqlStr = "BEGIN CUSTOMER_AUTHENTICATION.NP_BIOMETRICA_PKG.SP_GET_LIST_REASON(?,?,?,?,?); END;";

        try {
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setInt(1,idAccion);
            cstmt.setString(2, solution);
            cstmt.setInt(3, nbPend);
            cstmt.registerOutParameter(4, Types.VARCHAR);
            cstmt.registerOutParameter(5, OracleTypes.CURSOR);
            cstmt.execute();

            rs = (ResultSet)cstmt.getObject(5);
            strMessage = cstmt.getString(4);

            if(StringUtils.isBlank(strMessage)){
                while (rs.next()) {
                    HashMap h = new HashMap();
                    h.put("wn_parameterid",rs.getString(1)+"");
                    h.put("wv_npdescription",rs.getString(2)==null?"":rs.getString(2));
                    list.add(h);
                }
            }

        }catch (SQLException e) {
            System.out.println("[BiometricaDao][strMessage]:"+e.getMessage());
            e.printStackTrace();
        }finally {
            try{
                closeObjectsDatabase(conn, cstmt, null);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        objHashMap.put("objArrayList",list);
        objHashMap.put("strMessage",strMessage);
        return objHashMap;
    }

    /**
     * Motivo: Obtiene el listado de los motivos segun la accion tomada
     * <br>Realizado por: <a href="mailto:rquispe@practiaconsulting.com">Ricardo Quispe</a>
     * <br>Fecha: 23/07/2015
     * @return    HashMap
     * */
    public HashMap getResponseRule(Integer transactionID,String docnumber,String codeError,String restriction,
                                   String type,String source,Integer flagdni,Integer attempt,String ercAcepta){

        HashMap objHashMap= new HashMap();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        String strMessage="";
        String strmotivo="";
        String straccion="";
        String strMessageError="";

        String sqlStr = "BEGIN CUSTOMER_AUTHENTICATION.NP_BIOMETRICA_PKG.SP_GET_RESPONSE_RULE(?,?,?,?,?,?,?,?,?,?,?,?,?); END;";


        try {
            conn = Proveedor.getConnection();
            cstmt=(OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setInt(1,transactionID);
            cstmt.setString(2, docnumber);
            cstmt.setString(3,codeError);
            cstmt.setString(4,restriction);
            cstmt.setString(5,type);
            cstmt.setString(6,source);
            cstmt.setInt(7,flagdni);
            cstmt.setInt(8,attempt);
            cstmt.setString(9,ercAcepta);
            cstmt.registerOutParameter(10, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(11, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(12, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(13, OracleTypes.VARCHAR);

            cstmt.execute();

            strMessageError = cstmt.getString(13);
            if(StringUtils.isBlank(strMessageError)){
                strMessage = cstmt.getString(10);
                strmotivo = cstmt.getString(11);
                straccion=cstmt.getString(12);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try{
                closeObjectsDatabase(conn, cstmt, null);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        objHashMap.put("strMessage",strMessage);
        objHashMap.put("strmotivo",strmotivo);
        objHashMap.put("straccion",straccion);
        objHashMap.put("strMessageError",strMessageError);
        return objHashMap;
    }



    /**
     * Motivo: Obtiene la cantidad de registros en la tabla VerifCustomer
     * <br>Realizado por: <a href="mailto:rquispe@practiaconsulting.com">Ricardo Quispe</a>
     * <br>Fecha: 23/07/2015
     * @return    HashMap
     * */
    public  HashMap  getCountVerifCustomer(int transactionID,String verificationType,String source,
                                           String docnumber){

        HashMap objHashMap= new HashMap();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        String strMessage="";
        int strQRegistro=0;

        String sqlStr = "BEGIN CUSTOMER_AUTHENTICATION.NP_BIOMETRICA_PKG.SP_GETCOUNT_VERIFCUSTOMER(?,?,?,?,?,?); END;";

        conn = Proveedor.getConnection();
        try {
            cstmt=(OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setInt(1,transactionID);
            cstmt.setString(2, verificationType);
            cstmt.setString(3,source);
            cstmt.setString(4,docnumber);
            cstmt.registerOutParameter(5, OracleTypes.NUMBER);
            cstmt.registerOutParameter(6, Types.VARCHAR);

            cstmt.execute();

            strMessage=cstmt.getString(6);
            if(StringUtils.isBlank(strMessage)){
                strQRegistro=cstmt.getInt(5);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try{
                closeObjectsDatabase(conn, cstmt, null);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        objHashMap.put("strQRegistro",strQRegistro);
        objHashMap.put("strMessage",strMessage);


        return  objHashMap;

    }


    /**
     * Motivo: Anula la orden
     * <br>Realizado por: <a href="mailto:rquispe@practiaconsulting.com">Ricardo Quispe</a>
     * <br>Fecha: 23/07/2015
     * @return    String
     * */
    public   String  getAnularOrden(VerificationCustomerBean verificationCustomerBean,int action,int motive,String login){
        System.out.println("[AnularOrden][Orderid]:"+verificationCustomerBean.getTransaction());
        String strMessage= "";
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        String sqlStr = "BEGIN CUSTOMER_AUTHENTICATION.NP_BIOMETRIC_SEARCH_PKG.SP_CANCELORDER(?,?,?,?,?,?,?,?,?,?); END;";

        try {
            conn = Proveedor.getConnection();
            cstmt=(OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setInt(1,verificationCustomerBean.getTransaction());
            cstmt.setString(2, verificationCustomerBean.getSource());
            cstmt.setString(3,verificationCustomerBean.getAuthorizedUser());
            cstmt.setString(4,verificationCustomerBean.getDocumento());
            cstmt.setString(5,verificationCustomerBean.getTypeTransaction());
            cstmt.setInt(6,verificationCustomerBean.getStatus());
            cstmt.setInt(7, action);
            cstmt.setInt(8, motive);
            cstmt.setString(9,login);
            cstmt.registerOutParameter(10, Types.VARCHAR);
            cstmt.execute();

            strMessage=cstmt.getString(10);


        } catch (SQLException e) {
            e.printStackTrace();
            strMessage=e.getMessage();
        }finally {
            try{
                closeObjectsDatabase(conn, cstmt, null);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        System.out.println("[getAnularOrden]:"+strMessage);
        return strMessage;
    }



    public   String  getActionBiometric(String action,VerificationCustomerBean verificationCustomerBean,BiometricBean biometricBean,
                                        String login){
        System.out.println("[getActionBiometric][Orderid]:"+verificationCustomerBean.getTransaction());
        String strMessage="";

        Connection conn = null;
        OracleCallableStatement cstmt = null;
        String sqlStr = "BEGIN CUSTOMER_AUTHENTICATION.NP_BIOMETRICA_PKG.SP_ACTIONBIOMETRIC(?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?); END;";

        try {
            conn = Proveedor.getConnection();
            cstmt=(OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setString(1, action);
            cstmt.setInt(2, verificationCustomerBean.getTransaction());
            cstmt.setString(3, verificationCustomerBean.getVerificationType());
            cstmt.setString(4, verificationCustomerBean.getSource());
            cstmt.setString(5, verificationCustomerBean.getMotive());
            cstmt.setString(6, verificationCustomerBean.getAuthorizedUser());
            cstmt.setString(7, verificationCustomerBean.getTypeTransaction());
            cstmt.setString(8, verificationCustomerBean.getDocumento());
            cstmt.setString(9,biometricBean.getStatus());
            cstmt.setString(10,login);
            cstmt.setString(11,biometricBean.getObservacion());
            cstmt.setString(12,biometricBean.getAuditoria());
            cstmt.setString(13,biometricBean.getRenicCode() );
            cstmt.setString(14,biometricBean.getReniecResponse());
            cstmt.setString(15,biometricBean.getValityDni());
            cstmt.setString(16,biometricBean.getName());
            cstmt.setString(17,biometricBean.getFather());
            cstmt.setString(18,biometricBean.getMother());
            cstmt.registerOutParameter(19, Types.VARCHAR);
            cstmt.execute();

            strMessage=cstmt.getString(19);

        } catch (SQLException e) {
            e.printStackTrace();
            strMessage=e.getMessage();

        }finally {
            try{
                closeObjectsDatabase(conn, cstmt, null);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        return  strMessage;
    }


    /**
     * Motivo: verifica si tiene verificaciones pendientes
     * <br>Realizado por: <a href="mailto:rquispe@practiaconsulting.com">Ricardo Quispe</a>
     * <br>Fecha: 23/07/2015
     * @return    HashMap
     * */
    public   HashMap getVerifPendiente(Integer orderid,String document, String cmbaction){
        HashMap objHashMap= new HashMap();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        String strMessageError="";
        String strmessage="";

        String sqlStr = "BEGIN CUSTOMER_AUTHENTICATION.NP_BIOMETRIC_SEARCH_PKG.SP_GET_VERIFPENDIENTE(?,?,?,?,?); END;";

        try {
            conn = Proveedor.getConnection();
            cstmt=(OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setInt(1, orderid);
            cstmt.setString(2, document);
            cstmt.setString(3, cmbaction);
            cstmt.registerOutParameter(4, Types.VARCHAR);
            cstmt.registerOutParameter(5, Types.VARCHAR);
            cstmt.execute();

            strMessageError= cstmt.getString(5);

            if(StringUtils.isBlank(strMessageError)){
                strmessage=cstmt.getString(4);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            strMessageError="DAO[getVerifPendiente]"+e.getMessage();
        }finally {
            try{
                closeObjectsDatabase(conn, cstmt, null);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        objHashMap.put("strMessageError",strMessageError);
        objHashMap.put("strmessage",strmessage);

        return  objHashMap;
    }



    /**
     * Motivo: Anula la orden
     * <br>Realizado por: <a href="mailto:rquispe@practiaconsulting.com">Ricardo Quispe</a>
     * <br>Fecha: 23/07/2015
     * @return    String
     * */
    public   String  getAnularVerificacion(int order,String login,String athorizedUser,String source) {
        System.out.println("[getAnularVerificacion:]"+order);
        String strMessage= "";
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        String sqlStr = "BEGIN CUSTOMER_AUTHENTICATION.NP_BIOMETRIC_SEARCH_PKG.SP_GET_ANULARVERIFICACION(?,?,?,?,?); END;";

        try {
            conn = Proveedor.getConnection();

            cstmt=(OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setInt(1,order);
            cstmt.setString(2, login);
            cstmt.setString(3, athorizedUser);
            cstmt.setString(4, source);
            cstmt.registerOutParameter(5, Types.VARCHAR);
            cstmt.execute();
            strMessage=cstmt.getString(5);

        } catch (SQLException e) {
            e.printStackTrace();
            strMessage=e.getMessage();
        }finally {
            try{
                closeObjectsDatabase(conn, cstmt, null);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        System.out.println("[getAnularVerificacion:Message=]"+strMessage);
        return strMessage;
    }

    public HashMap getActionNoBiometrica(int order,int verifCustomer,int question,String av_authorizeduser,String av_action,String source, String login){
        HashMap objHashMap= new HashMap();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        String strMessageError="";
        String strmessage="";

        System.out.println("[getActionNoBiometrica]order: "+order+", verifCustomer: "+verifCustomer
                +", question: "+question+", av_authorizeduser: "+av_authorizeduser+", av_action: "+av_action
                +", source: "+source+", login: "+login);

        String sqlStr = "BEGIN CUSTOMER_AUTHENTICATION.NP_BIOMETRIC_SEARCH_PKG.GET_ACTIONNOBIOMETRICA(?,?,?,?,?,?,?,?); END;";

        try {
            conn = Proveedor.getConnection();
            cstmt=(OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setInt(1, order);
            cstmt.setInt(2, verifCustomer);
            cstmt.setInt(3, question);
            cstmt.setString(4, av_authorizeduser);
            cstmt.setString(5, av_action);
            cstmt.setString(5, av_action);
            cstmt.setString(6, source);
            cstmt.setString(7, login);
            cstmt.registerOutParameter(8, Types.VARCHAR);

            cstmt.execute();

            strmessage= cstmt.getString(8);


        } catch (SQLException e) {
            e.printStackTrace();
            strmessage="DAO[getVerifPendiente]"+e.getMessage();
        }finally {
            try{
                closeObjectsDatabase(conn, cstmt, null);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        objHashMap.put("strmessage:",strmessage);



        return  objHashMap;
    }

    /**
     * Motivo: Permite obtener un listado de valores para una configuracion para biometrica fase 2
     * <br>Realizado por: <a href="mailto:paolo.ortega@hpe.com">Paolo Ortega</a>
     * <br>Fecha: 10/10/2016
     *
     * @return		HashMap
     */
    public HashMap getViaConfigTypeDocList() throws Exception {
        HashMap objHashMap = new HashMap();
        String strMessage,valueField2=null;
        String[] value;
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        ArrayList<String[]> listConfig = new ArrayList<String[]>();

        System.out.println("[getViaConfigurationList]");

        String sqlStr = "BEGIN customer_authentication.SPI_GET_VIA_TYPE_DOC(?, ?); END;";
        try{
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

            cstmt.registerOutParameter(1, OracleTypes.CURSOR);
            cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            strMessage = cstmt.getString(2);

            if(strMessage == null){
                    rs = (ResultSet)cstmt.getObject(1);
                    if(rs != null){
                        while(rs.next()){
                            value=new String[3];
                            value[0]=rs.getString(1)==null?"":rs.getString(1);
                            value[1]=rs.getString(2)==null?"":rs.getString(2);
                            value[2]=rs.getString(3)==null?"":rs.getString(3);
                            listConfig.add(value);
                        }
                    }
            }

            System.out.println("[BiometricaDAO.getConfigurationList] - Tamanio list: "+listConfig.size());
            System.out.println("[BiometricaDAO.getConfigurationList] - valueField2: "+valueField2);
            System.out.println("[BiometricaDAO.getConfigurationList] - ErrorMsg: "+strMessage);
        }
        catch(Exception e){
            throw new Exception(e);
        }
        finally{
            closeObjectsDatabase(conn, cstmt, rs);
        }
        objHashMap.put("listConfig", listConfig);
        objHashMap.put("valueField2", valueField2);
        objHashMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        return objHashMap;
    }


    /**
     * Motivo: Registro de usuario no registrado en mantenimiento  de usuarios
     * <br>Realizado por: <a href="mailto:rquispe@practiaconsulting.com">Ricardo Quispe</a>
     * <br>Fecha: 23/07/2015
     * @return    String
     **/
    public HashMap getInsertUserNotConfig(int order, String typeVerificacion,String login){
        HashMap objHashMap= new HashMap();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        String strMessageError="";
        String strmessage="";

        String sqlStr = "BEGIN CUSTOMER_AUTHENTICATION.NP_BIOMETRIC_SEARCH_PKG.SP_INSERT_USERNOTCONFIG(?,?,?,?); END;";

        try {
            conn = Proveedor.getConnection();
            cstmt=(OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setInt(1, order);
            cstmt.setString(2, typeVerificacion);
            cstmt.setString(3, login);
            cstmt.registerOutParameter(4, Types.VARCHAR);

            cstmt.execute();
            strmessage= cstmt.getString(4);


        } catch (SQLException e) {
            e.printStackTrace();
            strmessage="DAO[getInsertUserNotConfig]"+e.getMessage();
        }finally {
            try{
                closeObjectsDatabase(conn, cstmt, null);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        objHashMap.put("strmessage:",strmessage);

        return  objHashMap;
    }

    public HashMap getListSolution()throws SQLException{
        System.out.println("[getListSolution]");
        HashMap objHashMap= new HashMap();
        ArrayList<String> list = new ArrayList<String>();
        Connection conn = null;
        ResultSet rs=null;
        OracleCallableStatement cstmt = null;
        String strMessage="";


        String sqlStr = "BEGIN CUSTOMER_AUTHENTICATION.NP_BIOMETRICA_PKG.SP_GET_LIST_SOLUTION(?,?); END;";

        try {
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.registerOutParameter(1, Types.VARCHAR);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.execute();

            rs = (ResultSet)cstmt.getObject(2);
            strMessage = cstmt.getString(1);
            if(StringUtils.isBlank(strMessage)){
                while (rs.next()) {
                    String idssolucion="";
                    idssolucion=rs.getString(1);
                    list.add(idssolucion);
                }
            }
        } catch (SQLException e) {
            System.out.println("[BiometricaDao][strMessage]:"+e.getMessage());
            e.printStackTrace();
        }finally {
            try{
                closeObjectsDatabase(conn, cstmt, null);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        System.out.println("Listada: " + list.size() + " messsage: " + strMessage);

        objHashMap.put("objArrayList",list);
        objHashMap.put("strMessage",strMessage);
        return  objHashMap;
    }

    public  HashMap getValidarCategoria(String categoria){
        HashMap objHashMap = new HashMap();
        Connection conn = null;
        String strMessage=null;
        OracleCallableStatement cstmt = null;
        String resultado="";

        String sqlStr = "BEGIN CUSTOMER_AUTHENTICATION.NP_BIOMETRICA_PKG.SP_GET_VALID_CATEGORIA(?,?,?); END;";

        try {
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setString(1,categoria);
            cstmt.registerOutParameter(2, Types.VARCHAR);
            cstmt.registerOutParameter(3, Types.VARCHAR);
            cstmt.execute();

            strMessage = cstmt.getString(3);
            if(StringUtils.isBlank(strMessage)){
                resultado= cstmt.getString(2);
            }
        } catch (SQLException e) {
            System.out.println("[ValidCategoria][strMessage]:"+e.getMessage());
            e.printStackTrace();
        }finally {
            try{
                closeObjectsDatabase(conn, cstmt, null);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        objHashMap.put("strResultado",resultado);
        objHashMap.put("strMessage",strMessage);
        return  objHashMap;

    }


    /**
     * Motivo: Lista las categorias y solucion  configuradas
     * <br>Realizado por: <a href="mailto:rquispe@practiaconsulting.com">Ricardo Quispe</a>
     * <br>Fecha: 23/07/2015
     * @return    String
     **/
    public HashMap getValidaCategoriaSolucion(int order)throws SQLException{
        System.out.println("[getValidaCategoriaSolucion]");
        HashMap objHashMap= new HashMap();
        Connection conn = null;
        ResultSet rs=null;
        OracleCallableStatement cstmt = null;
        String strMessage="";
        String resultado="";

        String sqlStr = "BEGIN CUSTOMER_AUTHENTICATION.NP_BIOMETRICA_PKG.SP_GET_VALID_CATEGORIASOLUTION(?,?,?); END;";

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
        } catch (SQLException e) {
            System.out.println("[BiometricaDao][strMessage]:"+e.getMessage());
            e.printStackTrace();
        }finally {
            try{
                closeObjectsDatabase(conn, cstmt, null);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        System.out.println("Resultado: : " + resultado + " messsage: " + strMessage);

        objHashMap.put("strResultado",resultado);
        objHashMap.put("strMessage",strMessage);
        return  objHashMap;
    }

    /**
     * Motivo: Devuelve verificaci贸n del cliente.
     * Realizado por: MMONTOYA
     * Fecha: 15/04/2016
     **/
    public HashMap getVerificationCustomer(long verificationId) {
        System.out.println("Inicio BiometricaDAO.getVerificationCustomer - verificationId: " + verificationId);

        PersonInfoBean personInfoBean = null;
        String strMessage = null;

        OracleCallableStatement cstmt = null;
        String sqlStr = "BEGIN CUSTOMER_AUTHENTICATION.NP_AUTHENTICATION_PKG.SP_GET_VERIFICATION_CUSTOMER(?, ?, ?, ?, ?); END;";

        Connection conn = null;
        String fechaEmision="";

        try {
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);

            cstmt.setLong(1, verificationId);
            cstmt.registerOutParameter(2, OracleTypes.ARRAY, "CUSTOMER_AUTHENTICATION.TT_VERIFICATION_LST");
            cstmt.registerOutParameter(3, OracleTypes.BLOB);
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(5, OracleTypes.VARCHAR);

            cstmt.execute();

            strMessage = cstmt.getString(5);
            if (StringUtils.isBlank(strMessage)) {
                StructDescriptor verificationDescriptor = StructDescriptor.createDescriptor("CUSTOMER_AUTHENTICATION.TO_VERIFICATION_LST", conn);
                StructDescriptor quizDescriptor = StructDescriptor.createDescriptor("CUSTOMER_AUTHENTICATION.TO_GET_QUIZ_LST", conn);
                StructDescriptor questionDescriptor = StructDescriptor.createDescriptor("CUSTOMER_AUTHENTICATION.TO_GET_QUESTION_LST", conn);
                StructDescriptor optionDescriptor = StructDescriptor.createDescriptor("CUSTOMER_AUTHENTICATION.TO_GET_OPTION_LST", conn);

                // Obtiene la verificaci贸n.
                ARRAY verificationArray = (ARRAY) cstmt.getObject(2);
                personInfoBean = new PersonInfoBean();

                for (int i = 0; i < verificationArray.getOracleArray().length; i++) {
                    STRUCT verificationStruct = (STRUCT) verificationArray.getOracleArray()[i];
                    Map<String, Object> verificationMap = getAttributeMap(verificationStruct, verificationDescriptor);

                    personInfoBean.setVerificationid(MiUtil.getBigInteger(verificationMap.get("wn_verificationid")));
                    personInfoBean.setAttempts(MiUtil.getBigInteger(verificationMap.get("wn_attemp")));
                    personInfoBean.setDocumentNumber(MiUtil.getString(verificationMap.get("wv_npnrodocument")));
                    personInfoBean.setMinAccepted(MiUtil.getBigInteger(verificationMap.get("wn_minattempt")));

                    //obtener la fecha de emision
                    fechaEmision=this.getFechaEmision(verificationId);
                    personInfoBean.setFechaEmision(fechaEmision);

                    // Obtiene los cuestionarios.
                    ARRAY quizArray = (ARRAY) verificationMap.get("wt_quiz");
                    List<QuizBean> quizBeans = new ArrayList<QuizBean>();

                    for (int j = 0; j < quizArray.getOracleArray().length; j++) {
                        STRUCT quizStruct = (STRUCT) quizArray.getOracleArray()[j];
                        Map<String, Object> quizMap = getAttributeMap(quizStruct, quizDescriptor);

                        QuizBean quizBean = new QuizBean();
                        quizBean.setIdQuestionary(MiUtil.getBigInteger(quizMap.get("wn_quizid")));

                        // Obtiene las preguntas.
                        ARRAY questionArray = (ARRAY) quizMap.get("wt_question");
                        List<QuestionBean> questionBeans = new ArrayList<QuestionBean>();

                        for (int k = 0; k < questionArray.getOracleArray().length; k++) {
                            STRUCT questionStruct = (STRUCT) questionArray.getOracleArray()[k];
                            Map<String, Object> questionMap = getAttributeMap(questionStruct, questionDescriptor);

                            QuestionBean questionBean = new QuestionBean();
                            questionBean.setIdquestion(MiUtil.getBigInteger(questionMap.get("wn_questionid")));
                            questionBean.setQuestion(MiUtil.getString(questionMap.get("wv_question")));

                            // Obtiene las opciones.
                            ARRAY optionArray = (ARRAY) questionMap.get("wt_options");
                            List<OptionBean> optionBeans = new ArrayList<OptionBean>();

                            for (int l = 0; l < optionArray.getOracleArray().length; l++) {
                                STRUCT optionStruct = (STRUCT) optionArray.getOracleArray()[l];
                                Map<String, Object> optionMap = getAttributeMap(optionStruct, optionDescriptor);

                                OptionBean optionBean = new OptionBean();
                                optionBean.setIdoption(MiUtil.getBigInteger(optionMap.get("wn_optionid")));
                                optionBean.setOption(MiUtil.getString(optionMap.get("wv_answer")));

                                String answerCorrect = MiUtil.getString(optionMap.get("wv_answercorrect"));
                                if (answerCorrect.equals("1")) {
                                    questionBean.setIdoptionSuccess(optionBean.getIdoption());
                                }

                                optionBeans.add(optionBean);
                            }

                            questionBean.setLstOption(optionBeans);
                            questionBeans.add(questionBean);
                        }

                        quizBean.setLstQuestion(questionBeans);
                        quizBeans.add(quizBean);
                    }

                    personInfoBean.setLstQuizBean(quizBeans);

                    // Inicio. Obtiene informaci贸n de la foto.
                    String lenghtPhoto = cstmt.getString(4);
                    if (!lenghtPhoto.equals("0")) {
                        Blob photo = cstmt.getBlob(3);
                        personInfoBean.setPhoto(photo.getBytes(1,  (int) photo.length()));
                    }

                    personInfoBean.setLengthPhoto(lenghtPhoto);
                    // Fin. Obtiene informaci贸n de la foto.
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                closeObjectsDatabase(conn, cstmt, null);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        HashMap hshDataMap = new HashMap();
        hshDataMap.put("personInfoBean", personInfoBean);
        hshDataMap.put("strMessage", strMessage);
        return hshDataMap;
    }

    private Map<String, Object> getAttributeMap(STRUCT struct, StructDescriptor structDescriptor) {
        Map<String, Object> attributeMap = new HashMap<String, Object>();

        try {
            ResultSetMetaData metaData = structDescriptor.getMetaData();
            Object[] attributes = struct.getAttributes();

            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                Object value = attributes[i - 1];
                attributeMap.put(metaData.getColumnName(i).toLowerCase(), value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return attributeMap;
    }

/**
     * Motivo: Obtiene la lista de documentos que pueden ser considerados en una exoneraci髇
     * Realizado por: LHUAPAYA
     * Fecha: 09/08/2016
     **/
    public HashMap getTypesDocumentsExoneration()throws SQLException,Exception {
        System.out.println("[getListTypesDoc]");
        HashMap objHashMap= new HashMap();
        ArrayList list = new ArrayList();
        Connection conn = null;
        ResultSet rs=null;
        OracleCallableStatement cstmt = null;
        String strMessage="";

        String sqlStr = "BEGIN CUSTOMER_AUTHENTICATION.NP_EXONERATE_PKG.SP_GET_LIST_TYPESDOC(?,?); END;";

        try {
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.registerOutParameter(1, OracleTypes.CURSOR);
            cstmt.registerOutParameter(2, Types.VARCHAR);
            cstmt.execute();

            rs = (ResultSet)cstmt.getObject(1);
            strMessage = cstmt.getString(2);
            if(StringUtils.isBlank(strMessage)){
                while (rs.next()) {
                    HashMap h = new HashMap();
                    h.put("wv_valueTypeDoc",rs.getString(1)+"");
                    h.put("wv_descTypeDoc",rs.getString(2)==null?"":rs.getString(2));
                    list.add(h);
                }
            }
        } catch (SQLException e) {
            System.out.println("[BiometricaDao][strMessage]:"+e.getMessage());
            e.printStackTrace();
        }finally {
            try{
                closeObjectsDatabase(conn, cstmt, null);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }


        objHashMap.put("objArrayList",list);
        objHashMap.put("strMessage",strMessage);
        return  objHashMap;
    }

    /**
     * Motivo: Valida si el cliente puede pasar por una exoneraci髇
     * Realizado por: LHUAPAYA
     * Fecha: 09/08/2016
     **/
    public int validateClientExonerate(int idOrder)throws SQLException,Exception {
        System.out.println("[validateClientExonerate]");
        HashMap objHashMap= new HashMap();
        ArrayList list = new ArrayList();
        Connection conn = null;
        ResultSet rs=null;
        OracleCallableStatement cstmt = null;
        String strMessage="";
        int intNumberValidate = 0;

        String sqlStr = "BEGIN ? := CUSTOMER_AUTHENTICATION.NP_EXONERATE_PKG.FN_VALIDATE_CUSTOMER_EXONERATE(?,?); END;";

        try {
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.registerOutParameter(1,Types.INTEGER);
            cstmt.setString(2,Constante.Source_CRM);
            cstmt.setInt(3, idOrder);
            cstmt.execute();

            intNumberValidate = cstmt.getInt(1);

        } catch (SQLException e) {
            System.out.println("[BiometricaDao][strMessage]:"+e.getMessage());
            e.printStackTrace();
            throw new SQLException(e);
        }finally {
            try{
                closeObjectsDatabase(conn, cstmt, null);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        return  intNumberValidate;
    }

    /**
     * Motivo: Registra los datos de una exoneraci髇
     * Realizado por: LHUAPAYA
     * Fecha: 09/08/2016
     **/
    public HashMap registerExonerate(int orderid, String authorizedUser, String login, String numDoc, int valTypeDoc)throws SQLException,Exception {
        System.out.println("[DAO - registerExonerate]");
        HashMap objHashMap= new HashMap();
        ArrayList list = new ArrayList();
        Connection conn = null;
        ResultSet rs=null;
        OracleCallableStatement cstmt = null;
        String strMessage="";

        String sqlStr = "BEGIN CUSTOMER_AUTHENTICATION.NP_EXONERATE_PKG.SP_INS_EXONERATE(?,?,?,?,?,?,?); END;";

        try {
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setInt(1, orderid);
            cstmt.setString(2,authorizedUser);
            cstmt.setString(3, login);
            cstmt.setString(4, numDoc);
            cstmt.setInt(5, valTypeDoc);
            cstmt.setString(6, Constante.Source_CRM);
            cstmt.registerOutParameter(7, Types.VARCHAR);
            cstmt.execute();

            strMessage = cstmt.getString(7);
            if(!StringUtils.isBlank(strMessage)){
                objHashMap.put("strMessage", strMessage);
                return objHashMap;
            }
        } catch (SQLException s) {
            System.out.println(s);
            throw new SQLException(s);
        }catch (Exception e){
            System.out.println(e);
            throw new Exception(e);
        }finally {
            try{
                closeObjectsDatabase(conn, cstmt, null);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }


        objHashMap.put("objArrayList", list);
        objHashMap.put("strMessage",strMessage);
        return  objHashMap;
    }

    /**
     * Motivo: Devuelve el ultimo representante legal de una empresa
     * Realizado por: LHUAPAYA
     * Fecha: 09/08/2016
     **/
    public HashMap getLastLegalRepresentative(int orderId)throws SQLException,Exception {
        System.out.println("Inicio BiometricaDAO.getLastLegalRepresentative - orderId: " + orderId);

        LegalRepresentativeBean legalRepresentativeBean = new LegalRepresentativeBean();

        String strMessage = null;



        OracleCallableStatement cstmt = null;
        String sqlStr = "BEGIN CUSTOMER_AUTHENTICATION.NP_EXONERATE_PKG.SP_GET_LAST_LEGAL_REP(?, ?, ?, ?); END;";

        Connection conn = null;

        try {
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);

            cstmt.setLong(1, orderId);
            cstmt.setString(2,Constante.Source_CRM);
            cstmt.registerOutParameter(3, OracleTypes.ARRAY, "CUSTOMER_AUTHENTICATION.TT_LEGAL_REPRESENTATION_LST");
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);

            cstmt.execute();

            strMessage = cstmt.getString(4);
            System.out.println("BiometricaDAO.getLastLegalRepresentative error: " + strMessage);
            if (StringUtils.isBlank(strMessage)) {

                StructDescriptor legalRepresentative = StructDescriptor.createDescriptor("CUSTOMER_AUTHENTICATION.TO_LEGAL_REPRESENTATION", conn);

                ARRAY legalRepresentativeArray = (ARRAY) cstmt.getObject(3);
                System.out.println("BiometricaDAO.getLastLegalRepresentative - TAMA袿 " +legalRepresentativeArray.getOracleArray().length );
                if(legalRepresentativeArray.getOracleArray() != null && legalRepresentativeArray.getOracleArray().length != 0){
                    for (int i = 0; i < legalRepresentativeArray.getOracleArray().length; i++) {
                        STRUCT legalRepresentativeStruct = (STRUCT) legalRepresentativeArray.getOracleArray()[i];
                        Map<String, Object> legalRepresentativeMap = getAttributeMap(legalRepresentativeStruct, legalRepresentative);
                        legalRepresentativeBean.setNomLegalRep(MiUtil.getString(legalRepresentativeMap.get("wv_name")));
                        legalRepresentativeBean.setTypeDocument(MiUtil.getString(legalRepresentativeMap.get("wv_typedoc")));
                        legalRepresentativeBean.setValTypeDoc(MiUtil.getInt(legalRepresentativeMap.get("wn_valtypedoc")));
                        legalRepresentativeBean.setNumberDocument(MiUtil.getString(legalRepresentativeMap.get("wv_numberdoc")));

                    }
                }else{
                    strMessage = "[BiometricaDAO.getLastLegalRepresentative] - No se encuentran representantes legales";
                    System.out.println(strMessage);
                }


            }

        } catch (SQLException s) {
            System.out.println(s);
            throw new SQLException(s);
        }catch (Exception e){
            System.out.println(e);
            throw new Exception(e);
        }finally {
            try{
                closeObjectsDatabase(conn, cstmt, null);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        HashMap hshDataMap = new HashMap();
        hshDataMap.put("legalRepresentativeBean", legalRepresentativeBean);
        hshDataMap.put("strMessage", strMessage);
        return hshDataMap;


    }
    /**
     * Motivo: Obtiene los motivos en base a la accion
     * <br>IOZ01092016I</a>
     * <br>Fecha: 12/09/2016
     * @return    HashMap
     * */
    public HashMap getListReasonAislada(String  action)throws SQLException{
        System.out.println("[getListReasonAislada]");
        HashMap objHashMap= new HashMap();
        ArrayList list = new ArrayList();
        Connection conn = null;
        ResultSet rs=null;
        OracleCallableStatement cstmt = null;
        String strMessage="";

        String sqlStr = "BEGIN CUSTOMER_AUTHENTICATION.NP_CONFIGURATION_PKG.SP_GET_REASON(?,?,?); END;";


        try {
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setString(1,action);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, Types.VARCHAR);
            cstmt.execute();

            rs = (ResultSet)cstmt.getObject(2);
            strMessage = cstmt.getString(3);

            if(StringUtils.isBlank(strMessage)){
                while (rs.next()) {
                    HashMap h = new HashMap();
                    h.put("wn_parameterid",rs.getString(1)+"");
                    h.put("wv_npdescription",rs.getString(2)==null?"":rs.getString(2));
                    list.add(h);
                }
            }

        }catch (SQLException e) {
            System.out.println("[BiometricaDao][strMessage]:" + e.getMessage());
            e.printStackTrace();
        }finally {
            try{
                closeObjectsDatabase(conn, cstmt, null);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        objHashMap.put("objArrayList",list);
        objHashMap.put("strMessage", strMessage);
        return objHashMap;
    }

    /**
     * Motivo: Obtiene las acciones en base al tipo de documento
     * <br>IOZ01092016I</a>
     * <br>Fecha: 12/09/2016
     * @return    HashMap
     * */
    public HashMap getListAccion(String  av_type_document )throws SQLException{
        System.out.println("[getListAccion]");
        HashMap objHashMap= new HashMap();
        ArrayList list = new ArrayList();
        Connection conn = null;
        ResultSet rs=null;
        OracleCallableStatement cstmt = null;
        String strMessage="";

        String sqlStr = "BEGIN CUSTOMER_AUTHENTICATION.NP_CONFIGURATION_PKG.SP_GET_ACTION(?,?,?); END;";

        try {
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setString(1, av_type_document);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, Types.VARCHAR);
            cstmt.execute();

            rs = (ResultSet)cstmt.getObject(2);
            strMessage = cstmt.getString(3);

            if(StringUtils.isBlank(strMessage)){
                while (rs.next()) {
                    HashMap h = new HashMap();
                    h.put("wn_parameterid",rs.getString(1)+"");
                    h.put("wv_npdescription",rs.getString(2)==null?"":rs.getString(2));
                    list.add(h);
                }
            }
            HashMap h = new HashMap();
        }catch (SQLException e) {
            System.out.println("[BiometricaDao][strMessage]:" + e.getMessage());
            e.printStackTrace();
            HashMap h = new HashMap();
        }finally {
            try{
                closeObjectsDatabase(conn, cstmt, null);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        objHashMap.put("objArrayList",list);
        objHashMap.put("strMessage", strMessage);
        return objHashMap;
    }

    /**
     * Motivo: inserta el registrar
     * <br>IOZ15092016I</a>
     * <br>Fecha: 12/09/2016
     * @return    String
     * */
    public   String  insertRegistrar(String login, String tipoDoc, String numDoc, String accion, String customerId, String sourcev) {
        System.out.println("[BiometricDAO.insertRegistrar:]");
        String strMessage= "";
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        String sqlStr = "BEGIN CUSTOMER_AUTHENTICATION.NP_AUTHENTICATION_VIA_PKG.SP_INS_VIA_CUSTOMER(?,?,?,?,?,?,?,?,?,?,?,?,?); END;";

        try {
            conn = Proveedor.getConnection();
            cstmt=(OracleCallableStatement)conn.prepareCall(sqlStr);
            //cstmt.setNull(1,java.sql.Types.NULL);
            cstmt.setString(1, accion);
            cstmt.setString(2, sourcev);
            cstmt.setString(3, "");
            cstmt.setString(4, "");
            cstmt.setString(5, "");
            cstmt.setInt(6, 10); // estado de la verificacion
            cstmt.setString(7, numDoc);
            cstmt.setString(8, "");
            cstmt.setString(9, login);
            cstmt.setString(10, tipoDoc);
            cstmt.setInt(11, Integer.parseInt(customerId));
            cstmt.registerOutParameter(12, Types.INTEGER);
            cstmt.setInt(12, 0);
            cstmt.registerOutParameter(13, Types.VARCHAR);
            cstmt.execute();
            strMessage=cstmt.getString(13);
        } catch (SQLException e) {
            e.printStackTrace();
            strMessage="[BiometricDAO.insertRegistrar:]"+e.getMessage()+"idxOcs: ";
        }finally {
            try{
                closeObjectsDatabase(conn, cstmt, null);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        System.out.println("[BiometricDAO.insertRegistrar:Message=]"+strMessage);
        return strMessage;
    }

    /**
     * Motivo: inserta verificacion aislada
     * <br>PORTEGA</a>
     * <br>Fecha: 05/10/2016
     * @return    String
     * */
    public   String  insViaCustomer(String verificationType, String useCase, String motivo, String authorizer, String transactionType,
                                    Integer statusVerification, String docNumber, String phoneNumber, String docType, Integer customerId,
                                    Integer verificationId) {
        System.out.println("[BiometricDAO.insViaCustomer:]verificationType: "+verificationType+", useCase: "+useCase+", motivo: "+motivo
                +", authorizer: "+authorizer+", transactionType: "+transactionType+", statusVerification: "+statusVerification
                +", docNumber: "+docNumber+", phoneNumber: "+phoneNumber+", docType: "+docType+", customerId: "+customerId
                +", verificationId: "+verificationId);

        String strMessage= "";
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        String sqlStr = "BEGIN CUSTOMER_AUTHENTICATION.NP_AUTHENTICATION_VIA_PKG.SP_INS_VIA_CUSTOMER(?,?,?,?,?,?,?,?,?,?,?,?,?); END;";

        try {
            conn = Proveedor.getConnection();
            cstmt=(OracleCallableStatement)conn.prepareCall(sqlStr);
            //cstmt.setNull(1,java.sql.Types.NULL);
            cstmt.setString(1, verificationType);
            cstmt.setString(2, useCase);
            cstmt.setString(3, motivo);
            cstmt.setString(4, authorizer);
            cstmt.setString(5, transactionType);
            cstmt.setInt(6, statusVerification); // estado de la verificacion
            cstmt.setString(7, docNumber);
            cstmt.setString(8, phoneNumber);
            cstmt.setString(9, authorizer);
            cstmt.setString(10, docType);
            cstmt.setInt(11, customerId);
            cstmt.registerOutParameter(12, Types.INTEGER);
            cstmt.setInt(12, verificationId);
            cstmt.registerOutParameter(13, Types.VARCHAR);
            cstmt.execute();
            strMessage=cstmt.getString(13);
        } catch (SQLException e) {
            e.printStackTrace();
            strMessage="[BiometricDAO.insertRegistrar:]"+e.getMessage()+"idxOcs: ";
        }finally {
            try{
                closeObjectsDatabase(conn, cstmt, null);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        System.out.println("[BiometricDAO.insertRegistrar:Message=]"+strMessage);
        return strMessage;
    }

    /**
     * Lista verificaciones de identidad aislada segun los parametros especificados
     * @param tipoDocumento
     * @param numeroDocumento
     * @param fechaInicio
     * @param fechaFin
     * @return HashMap
     */
    public HashMap listarVerifaciones(String tipoDocumento, String  numeroDocumento, String fechaInicio, String fechaFin){
        System.out.println("[BiometricDAO.listarVerificaciones]");
        HashMap objHashMap= new HashMap();
        ArrayList<VerificationBean> list = new ArrayList<VerificationBean>();
        Connection conn = null;
        ResultSet rs=null;
        OracleCallableStatement cstmt = null;
        String strMessage="";
        VerificationBean verificationBean = null;
        Timestamp timestamp;
        try {
            //strMessage = "BEGIN CUSTOMER_AUTHENTICATION.SPI_GET_VERIFICATION_LIST(?, ?, ?, ?, ?, ?); END;";
            strMessage = "BEGIN CUSTOMER_AUTHENTICATION.NP_AUTHENTICATION_VIA_PKG.SP_GET_VERIFICATION_LIST(?, ?, ?, ?, ?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(strMessage);

            cstmt.setString(1, tipoDocumento);
            cstmt.setString(2, numeroDocumento);
            cstmt.setString(3, fechaInicio);
            cstmt.setString(4, fechaFin);
            cstmt.registerOutParameter(5, OracleTypes.CURSOR);
            cstmt.registerOutParameter(6, OracleTypes.VARCHAR);

            cstmt.execute();
            strMessage = cstmt.getString(6);
            if(strMessage == null || strMessage.isEmpty()){
                rs = (ResultSet)cstmt.getObject(5);

                while (rs.next()) {
                    verificationBean = new VerificationBean();
                    verificationBean.setTipoVerificacion(rs.getString("npdescription"));
                    verificationBean.setOrigen(rs.getString("nptypetransaction"));
                    verificationBean.setNumero(rs.getString("nptransaction"));
                    timestamp = rs.getTimestamp("np_date_start_validity");
                    verificationBean.setFechaInicioVigencia(timestamp ==null?null:(new Date(timestamp.getTime())));
                    timestamp = rs.getTimestamp("np_date_end_validity");
                    verificationBean.setFechaFinVIgencia(timestamp ==null?null:(new Date(timestamp.getTime())));
                    timestamp = rs.getTimestamp("np_date_of_use");
                    verificationBean.setFechaUtilizacion(timestamp ==null?null:(new Date(timestamp.getTime())));
                    verificationBean.setVerificationId(rs.getLong("npverificationid"));
                    verificationBean.setTipoDocumento(rs.getString("nptipodocument"));
                    verificationBean.setNumeroDocumento(rs.getString("npnrodocument"));
                    list.add(verificationBean);
                }
            }

        } catch (Exception e) {
            strMessage="[BiometricaDao.listarVerifaciones][strMessage]:" + e.getMessage();
            System.out.println(strMessage);
            e.printStackTrace();
        }finally {
            try{
                closeObjectsDatabase(conn, cstmt, null);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        objHashMap.put("objArrayList",list);
        objHashMap.put("strMessage", strMessage);
        return  objHashMap;
    }

    /**
     * Motivo: Obtiene el detalle de una verificacion de identidad aislada
     * RO0001
     */
    public HashMap listarTiposDocumentoVerificaciones() {
        System.out.println("[BiometricaDao.listarTiposDocumentoVerificaciones]");
        HashMap objHashMap= new HashMap();
        ArrayList<DominioBean> list = new ArrayList<DominioBean>();
        Connection conn = null;
        ResultSet rs=null;
        OracleCallableStatement cstmt = null;
        String strMessage="";
        DominioBean tableBean = null;
        try {
            strMessage = "BEGIN CUSTOMER_AUTHENTICATION.NP_AUTHENTICATION_VIA_PKG.SP_GET_DOCUMENT_TYPE(?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(strMessage);

            cstmt.registerOutParameter(1, OracleTypes.CURSOR);
            cstmt.registerOutParameter(2, OracleTypes.VARCHAR);

            cstmt.execute();
            strMessage = cstmt.getString(2);
            if( strMessage==null){
                rs = (ResultSet)cstmt.getObject(1);

                while (rs.next()) {
                    tableBean = new DominioBean();
                    tableBean.setValor(rs.getString("npparamv1"));
                    tableBean.setDescripcion(rs.getString("npparamv2"));
                    tableBean.setParam1(rs.getString("npparamn1"));
                    list.add(tableBean);
                }
            }
        } catch (Exception e) {
            strMessage="[BiometricaDao.listarTiposDocumentoVerificaciones][strMessage]:" + e.getMessage();
            System.out.println(strMessage);
            e.printStackTrace();
        }finally {
            try{
                closeObjectsDatabase(conn, cstmt, null);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        objHashMap.put("objArrayList",list);
        objHashMap.put("strMessage", strMessage);
        return objHashMap;
    }

    public HashMap obtenerDetalleVia(Long identificador) {
        System.out.println("[BiometricDAO.obtenerDetalleVia]");
        HashMap objHashMap= new HashMap();
        Connection conn = null;
        ResultSet rs;
        OracleCallableStatement cstmt = null;
        String strMessage ="";
        VerificationBean verificationBean = new VerificationBean();
        VerificationBean historico;
        List<VerificationBean> historicoBiometrica = new ArrayList<VerificationBean>();
        List<VerificationBean> historicoNoBiometrica = new ArrayList<VerificationBean>();
        Timestamp timestamp;
        try {
            strMessage = "BEGIN CUSTOMER_AUTHENTICATION.NP_AUTHENTICATION_VIA_PKG.SP_GET_IDENT_VERIF_DETAIL(?, ?, ?, ?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(strMessage);

            cstmt.setLong(1, identificador);
            cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(3, OracleTypes.CURSOR);
            cstmt.registerOutParameter(4, OracleTypes.CURSOR);
            cstmt.registerOutParameter(5, OracleTypes.CURSOR);

            cstmt.execute();
            strMessage = cstmt.getString(3);
            if( strMessage==null){
                rs = (ResultSet)cstmt.getObject(3);
                if (rs != null && rs.next()) {
                    verificationBean.setVerificationId(rs.getLong("npverificationid"));
                    verificationBean.setTipoDocumento(rs.getString("nptipodocument"));
                    verificationBean.setNumeroDocumento(rs.getString("npnrodocument"));
                    verificationBean.setTipoVerificacion(rs.getString("tipoverificacionexitosa"));
                    verificationBean.setRegistradoPor(rs.getString("npcreatedby"));
                    verificationBean.setModificadoPor(rs.getString("npmodifiedby"));
                    verificationBean.setNombres(rs.getString("npname"));
                    verificationBean.setApellidos(rs.getString("fulllastname"));
                }

                rs = (ResultSet)cstmt.getObject(4);
                if (rs != null) {
                    while (rs.next()){
                        historico = new VerificationBean();
                        historico.setBiometricId(rs.getInt("npbiometricid"));
                        historico.setEstado(rs.getString("estadoverificacion"));
                        historico.setMotivo(rs.getString("motivoverificacion"));
                        historico.setOrigen(rs.getString("origen"));
                        historico.setNumero(rs.getString("nptransaction"));
                        timestamp = rs.getTimestamp("npcreateddate");
                        historico.setFechaCreacion(timestamp == null ? null : (new Date(timestamp.getTime())));
                        historicoBiometrica.add(historico);
                    }
                }
                verificationBean.setHistoricoBiometrica(historicoBiometrica);

                rs = (ResultSet)cstmt.getObject(5);
                if (rs != null) {
                    while (rs.next()){
                        historico = new VerificationBean();
                        historico.setBiometricId(rs.getInt("npbiometricid"));
                        historico.setEstado(rs.getString("estadoverificacion"));
                        historico.setMotivo(rs.getString("motivoverificacion"));
                        historico.setOrigen(rs.getString("origen"));
                        historico.setNumero(rs.getString("nptransaction"));
                        timestamp = rs.getTimestamp("npcreateddate");
                        historico.setFechaCreacion(timestamp == null ? null : (new Date(timestamp.getTime())));
                        historico.setNumPreguntaAcertada(rs.getInt("nropreguntaacertada"));
                        historicoNoBiometrica.add(historico);
                    }
                }
                verificationBean.setHistoricoNoBiometrica(historicoNoBiometrica);
            }
        } catch (Exception e) {
            strMessage="[BiometricaDao.obtenerDetalleVia][strMessage]:" + e.getMessage();
            System.out.println(strMessage);
            e.printStackTrace();
        }finally {
            try{
                closeObjectsDatabase(conn, cstmt, null);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        objHashMap.put("objBean",verificationBean);
        objHashMap.put("strMessage", strMessage);
        return objHashMap;
    }


    public HashMap getResponseRuleVIA(String docnumber,String codeError,String restriction,
                String type,String source,Integer flagdni,Integer attempt,String ercAcepta, int customerId){
            System.out.println("[BiometricaDao.getResponseRuleVIA]docnumber: "+docnumber+", codeError: "+codeError
                    +", restriction: "+restriction+", type: "+type+", source: "+source+", flagdni: "+flagdni
                +", attempt: "+attempt+", ercAcepta: "+ercAcepta);

        HashMap objHashMap= new HashMap();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        String strMessage="";
        String strmotivo="";
        String straccion="";
        String strMessageError="";

        String sqlStr = "BEGIN CUSTOMER_AUTHENTICATION.NP_ISOLATED_VERIF02_PKG.SP_GET_RESPONSE_RULE_VIA(?,?,?,?,?,?,?,?,?,?,?,?,?); END;";


        try {
            conn = Proveedor.getConnection();
            cstmt=(OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setString(1, docnumber);
            cstmt.setString(2,codeError);
            cstmt.setString(3,restriction);
            cstmt.setString(4,type);
            cstmt.setString(5,source);
            cstmt.setInt(6, flagdni);
            cstmt.setInt(7,attempt);
            cstmt.setString(8, ercAcepta);
            cstmt.setInt(9,customerId);
            cstmt.registerOutParameter(10, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(11, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(12, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(13, OracleTypes.VARCHAR);

            cstmt.execute();

            strMessageError = cstmt.getString(13);
            if(StringUtils.isBlank(strMessageError)){
                strMessage = cstmt.getString(10);
                strmotivo = cstmt.getString(11);
                straccion=cstmt.getString(12);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try{
                closeObjectsDatabase(conn, cstmt, null);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        objHashMap.put("strMessage",strMessage);
        objHashMap.put("strmotivo",strmotivo);
        objHashMap.put("straccion",straccion);
        objHashMap.put("strMessageError", strMessageError);
        return objHashMap;
    }

    public   String  getActionBiometricVIA(String action,VerificationCustomerBean verificationCustomerBean,BiometricBean biometricBean,
                                           String login,int cusrtomerId){
        System.out.println("[BiometricaDao.getActionBiometricVIA]action: "+action+", verificationCustomerBean: "+verificationCustomerBean
                +", biometricBean: "+biometricBean+", login: "+login+", cusrtomerId: "+cusrtomerId);
        String strMessage="";

        Connection conn = null;
        OracleCallableStatement cstmt = null;
        String sqlStr = "BEGIN CUSTOMER_AUTHENTICATION.NP_ISOLATED_VERIF02_PKG.SP_ACTIONBIOMETRIC_VIA(?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?); END;";

        try {
            conn = Proveedor.getConnection();
            cstmt=(OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setString(1, action);
            cstmt.setString(2, verificationCustomerBean.getVerificationType());
            cstmt.setString(3, verificationCustomerBean.getSource());
            cstmt.setString(4, verificationCustomerBean.getMotive());
            cstmt.setString(5, verificationCustomerBean.getAuthorizedUser());
            cstmt.setString(6, verificationCustomerBean.getTypeTransaction());
            cstmt.setString(7, verificationCustomerBean.getDocumento());
            cstmt.setString(8,biometricBean.getStatus());
            cstmt.setString(9,login);
            cstmt.setString(10,biometricBean.getObservacion());
            cstmt.setString(11,biometricBean.getAuditoria());
            cstmt.setString(12,biometricBean.getRenicCode() );
            cstmt.setString(13,biometricBean.getReniecResponse());
            cstmt.setString(14,biometricBean.getValityDni());
            cstmt.setString(15,biometricBean.getName());
            cstmt.setString(16,biometricBean.getFather());
            cstmt.setString(17,biometricBean.getMother());
            cstmt.setInt(18,cusrtomerId);
            cstmt.setString(19,verificationCustomerBean.getDocType());
            cstmt.registerOutParameter(20, Types.VARCHAR);
            cstmt.execute();

            strMessage=cstmt.getString(20);

        } catch (SQLException e) {
            e.printStackTrace();
            strMessage=e.getMessage();

        }finally {
            try{
                closeObjectsDatabase(conn, cstmt, null);
            } catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        return  strMessage;
    }


   //ricardo.quispe PRY-0438
    /**
     * Motivo: Obtencion fecha de emision
     * <br>Realizado por: <a href="mailto:rquispe@mdp.com.pe">Ricardo Quispe</a>
     * <br>Fecha: 18/05/2017
     * @param     verificationId  Ej. 12702795
     * @return    String
     * */
   public String getFechaEmision(long verificationId){
       System.out.println("idverificacion:"+verificationId);
       String fechaEmision="";
       String strMessage="";
       Connection conn = null;
       OracleCallableStatement cstmt = null;
       String sqlStr = "BEGIN CUSTOMER_AUTHENTICATION.NP_AUTHENTICATION_PKG.SP_GET_FECHAEMISION(?,?,?); END;";

       try {
           conn = Proveedor.getConnection();
           cstmt=(OracleCallableStatement)conn.prepareCall(sqlStr);
           cstmt.setLong(1, verificationId);
           cstmt.registerOutParameter(2, Types.VARCHAR);
           cstmt.registerOutParameter(3, Types.VARCHAR);
           cstmt.execute();

           strMessage=cstmt.getString(2);
           fechaEmision=cstmt.getString(3);

       } catch (SQLException e) {
           e.printStackTrace();
           strMessage=e.getMessage();
           fechaEmision="";
       }finally {
           try{
               closeObjectsDatabase(conn, cstmt, null);
           } catch (Exception e) {
               logger.error(formatException(e));
           }
       }

       return  fechaEmision;

   }


}
