package pe.com.nextel.dao;

import java.lang.Exception;
import java.lang.IllegalAccessException;
import java.lang.NoSuchMethodException;
import java.lang.Object;
import java.lang.String;
import java.lang.System;
import java.lang.reflect.InvocationTargetException;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.*;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import oracle.sql.ARRAY;
import oracle.sql.DATE;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

import org.apache.commons.lang.StringUtils;

import pe.com.nextel.bean.*;
import pe.com.nextel.form.RetailForm;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;

//import pe.com.nextel.util.DbmsOutput;

public class CustomerDAO extends GenericDAO{

    /***********************************************************************
     *
     *
     * INTEGRACION DE ORDENES Y RETAIL - INICIO
     * REALIZADO POR: Lee Rosales
     * FECHA: 11/09/2007
     *
     *
     ***********************************************************************/

    /**
     * Motivo: Obtiene la lista de direcciones origen.
     * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
     * <br>Fecha: 15/01/2008
     * @return    ArrayList de ContactObjectBean
     */
    public HashMap getCustomerContactsByType(long lngCustomerId,String strType) throws Exception, SQLException{

        ArrayList list = new ArrayList();
        HashMap hshResultado  = new HashMap();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        int intResultOperation = 0;
        ContactObjectBean objContactObjectBean = null;

        try{
            String sqlStr = "BEGIN WEBSALES.SPI_GET_CUST_CONTACTS_BY_TYPES(?, ?, ?, ?); END;";
            conn = Proveedor.getConnection();

            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

            cstmt.setLong(1, lngCustomerId);
            cstmt.setString(2, strType);
            cstmt.registerOutParameter(3, OracleTypes.CURSOR);
            cstmt.registerOutParameter(4, OracleTypes.NUMBER);

            cstmt.executeUpdate();

            intResultOperation = cstmt.getInt(4);

            if( intResultOperation != 0 ){
                rs = (ResultSet)cstmt.getObject(3);
                while (rs.next()) {
                    objContactObjectBean = new ContactObjectBean();
                    objContactObjectBean.setSwpersonid(rs.getLong("swpersonid"));
                    objContactObjectBean.setSwfirstname(rs.getString("nombres"));
                    objContactObjectBean.setSwlastname(rs.getString("apellidos"));
                    list.add(objContactObjectBean);
                }

            }
            hshResultado.put("objContactObjectBean",list);
        }catch (Exception e) {
            logger.error(formatException(e));
            hshResultado.put("strMessage", e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return hshResultado;
    }


    /**
     * Motivo: Obtiene la lista de direcciones origen.
     * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
     * <br>Fecha: 15/01/2008
     * @return    ArrayList de AddressObjectBean
     */
    public HashMap getSourceAddress(long lngCustomerId, long lngItemId) throws Exception, SQLException{

        ArrayList list = new ArrayList();
        HashMap hshResultado  = new HashMap();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strMessage = null;
        AddressObjectBean objAddressObjectBean = null;
        try{
            String sqlStr = "BEGIN WEBSALES.SPI_GET_LISTADDRESS_INST(?, ?, ?, ?); END;";
            conn = Proveedor.getConnection();

            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

            cstmt.setLong(1, lngCustomerId);
            cstmt.setLong(2, lngItemId);
            cstmt.registerOutParameter(3, OracleTypes.CURSOR);
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);

            cstmt.executeUpdate();

            strMessage = cstmt.getString(4);

            if( strMessage == null ){
                rs = cstmt.getCursor(3);

                while (rs.next()) {
                    objAddressObjectBean = new AddressObjectBean();
                    objAddressObjectBean.setSwaddress1(rs.getString("ADDRESS"));
                    objAddressObjectBean.setAddressId(rs.getLong("NPADDRESSID"));
                    list.add(objAddressObjectBean);
                }

            }

            hshResultado.put("objAddressObjectlist",list);
            hshResultado.put("strMessage",strMessage);
        }catch (Exception e) {
            logger.error(formatException(e));
            hshResultado.put("strMessage", e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return hshResultado;
    }

    /**
     * Motivo: Obtiene la lista de direcciones destino.
     * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
     * <br>Fecha: 15/01/2008
     * @return    ArrayList de AddressObjectBean
     */
    public HashMap getDestinyAddress(long lngCustomerId) throws Exception, SQLException{

        ArrayList list = new ArrayList();
        HashMap hshResultado  = new HashMap();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strMessage = null;
        AddressObjectBean objAddressObjectBean = null;
        try{
            String sqlStr = "BEGIN WEBSALES.SPI_GET_LISTADDRESS_BYCUSTOMER(?, ?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, lngCustomerId);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeUpdate();
            strMessage = cstmt.getString(3);
            if( strMessage == null ){
                rs = cstmt.getCursor(2);
                while (rs.next()) {
                    objAddressObjectBean = new AddressObjectBean();
                    objAddressObjectBean.setSwaddress1(rs.getString("ADDRESS"));
                    objAddressObjectBean.setAddressId(rs.getLong("SWADDRESSID"));
                    list.add(objAddressObjectBean);
                }
            }

            hshResultado.put("objAddressObjectlist",list);
            hshResultado.put("strMessage",strMessage);
        }catch (Exception e) {
            logger.error(formatException(e));
            hshResultado.put("strMessage", e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        return hshResultado;
    }

    /**
     * Motivo: Recupera los clientes BSCS y Portal, dando el Contrato, Teléfono, SIM.
     * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
     * <br>Fecha: /09/2007
     * @return    ArrayList de DominioBean
     */
    public HashMap getCustomerJava(long longAppId, String strPhoneNumber, long longContractId, String strIMEI, String strSIM,String strNumeroRadio) throws Exception, SQLException {
        if(logger.isDebugEnabled())
            logger.debug("--Inicio--");
        HashMap hshDataMap = new HashMap();
        ArrayList arrCustomerBSCSList = new ArrayList();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        //DbmsOutput dbmsOutput = null;
        String strMessage = null;
        try{
            String sqlStr = "BEGIN WEBSALES.SPI_GET_CUSTOMER_JAVA(?,?,?,?,?,?,?,?); END;";
            conn = Proveedor.getConnection();
            //dbmsOutput = new DbmsOutput(conn);
            //dbmsOutput.enable(1000000);
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setLong(1, longAppId);
            cstmt.setString(2, strPhoneNumber);
            if(longContractId == 0) {
                cstmt.setNull(3, OracleTypes.NUMBER);
            }else{
                cstmt.setLong(3, longContractId);
            }
            cstmt.setString(4, strSIM);
            cstmt.setString(5, strIMEI);
            cstmt.setString(6, strNumeroRadio);
            cstmt.registerOutParameter(7, OracleTypes.ARRAY, "WEBSALES.TT_CUSTOMER_LST");
            cstmt.registerOutParameter(8, OracleTypes.VARCHAR);
            cstmt.execute();
            strMessage = cstmt.getString(8);
            if(StringUtils.isBlank(strMessage)) {
                ARRAY aryDominioList = (ARRAY)cstmt.getObject(7);
                for (int i = 0; i < aryDominioList.getOracleArray().length; i++) {
                    STRUCT stcDominio = (STRUCT) aryDominioList.getOracleArray()[i];
                    DominioBean objDominioBean = new DominioBean();
                    objDominioBean.setValor(MiUtil.defaultString(stcDominio.getAttributes()[1], ""));
                    objDominioBean.setDescripcion(MiUtil.defaultString(stcDominio.getAttributes()[0], ""));
                    arrCustomerBSCSList.add(i, objDominioBean);
                }
            }
            //closeObjectsDatabase(conn, cstmt, null);
            hshDataMap.put("arrCustomerBSCSList", arrCustomerBSCSList);
            hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        }catch (Exception e) {
            logger.error(formatException(e));
            hshDataMap.put(Constante.MESSAGE_OUTPUT, e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        return hshDataMap;
    }

    /**
     Method : getCustomerData
     Purpose: Obtener el detalle de un cliente
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     Lee Rosales     23/06/2007  Creación
     Ruth Polo       12/07/2009  Se agrego las variables wn_vendedorid2,   wv_vendedorname2, wv_dealer2
     Karen Salvador  28/01/2010  Se agrega la variabale wv_nptipodoc
     para Ordenes SSAA.
     */
    public HashMap getCustomerData(long intCustomerId,String strSwcreatedBy, long intRegionid,int iUserId,int iAppId,String strGeneratorId,String strGeneratorType) throws Exception,SQLException {//Exception,SQLException {

        HashMap h = new HashMap();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strMessage = null;
        System.out.println("TipoGenerator : " + strGeneratorType);
        System.out.println("IdGenerator : " + strGeneratorId);
        try{
            String sqlStr = "BEGIN WEBSALES.SPI_GET_CUST_DATA2(?, ?, ?, ?, ?, ?, ?, ?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, intCustomerId);
            cstmt.setString(2, strSwcreatedBy);
            cstmt.setLong(3, intRegionid);
            cstmt.setInt(4, iUserId);
            cstmt.setInt(5, iAppId);
            cstmt.setString(6,strGeneratorType);
            cstmt.setString(7,strGeneratorId);
            cstmt.registerOutParameter(8, OracleTypes.CURSOR);
            cstmt.registerOutParameter(9, Types.CHAR);
            cstmt.executeUpdate();

            strMessage = cstmt.getString(9);

            h.put("strMessage",strMessage);

            if( strMessage == null ) {
                rs = (ResultSet)cstmt.getObject(8);
        /*cstmt.registerOutParameter(6, OracleTypes.CURSOR);
        cstmt.registerOutParameter(7, Types.CHAR);                  
        cstmt.executeUpdate();
        
        strMessage = cstmt.getString(7);
        
        h.put("strMessage",strMessage);     
        
        if( strMessage == null ) {    
          rs = (ResultSet)cstmt.getObject(6);*/
                if (rs.next()) {
  
             /*Ingresando valores en un HasTable*/
                    h.put("wn_swcustomerid",rs.getInt(1)+"");

                    //h.put("wv_swname",rs.getString(2)==null?"":rs.getString(2));
                    h.put("wv_swname",rs.getString(2)==null?"":rs.getString(2).replaceAll("'","\\\\'"));
                    //h.put("wv_swnamecom",rs.getString(3)==null?"":rs.getString(3));
                    h.put("wv_swnamecom",rs.getString(3)==null?"":rs.getString(3).replaceAll("'","\\\\'"));
                    h.put("wv_swruc",rs.getString(4)==null?"":rs.getString(4));
                    h.put("wv_codbscs",rs.getString(5)==null?"":rs.getString(5));
                    h.put("wv_swtype",rs.getString(6)==null?"":rs.getString(6));
                    h.put("wv_swrating",rs.getString(7)==null?"":rs.getString(7));
                    h.put("wv_nplineacredito",rs.getString(8)==null?"":rs.getString(8));
                    h.put("wv_swmainphone",rs.getString(9)==null?"":rs.getString(9));
                    h.put("wv_swmainfax",rs.getString(10)==null?"":rs.getString(10));
                    h.put("wv_swnameregion",rs.getString(11)==null?"":rs.getString(11));
                    h.put("wv_swnamegiro",rs.getString(12)==null?"":rs.getString(12));
                    h.put("wv_tipocuenta",rs.getString(13)==null?"":rs.getString(13));
                    h.put("wn_vendedorid",rs.getInt(14)+"");
                    h.put("wv_vendedorname",rs.getString(15)==null?"":rs.getString(15));
                    h.put("wv_dealer",rs.getString(16)==null?"":rs.getString(16));
                    //RPOLO
                    h.put("wn_vendedorid2",rs.getInt(24)+"");
                    h.put("wv_vendedorname2",rs.getString(25)==null?"":rs.getString(25));
                    h.put("wv_dealer2",rs.getString(26)==null?"":rs.getString(26));

                    h.put("wv_nptipodoc",rs.getString(27)==null?"":rs.getString(27));

                    String document = rs.getString(27)==null?"":rs.getString(27);

                    h.put("wv_npcustomerrelationtype",rs.getString(17)==null?"":rs.getString(17));
                    h.put("wn_npcustomerrelationid",rs.getInt(18)+"");
                    h.put("wv_swname_customerrelation",rs.getString(19)==null?"":rs.getString(19));
                    h.put("wv_swregionid",rs.getString(20)==null?"":rs.getString(20));
                    h.put("wv_codbscs_paymentresp",rs.getString(21)==null?"":rs.getString(21));
                    h.put("wv_paymentrespdesc",rs.getString(22)==null?"":rs.getString(22).replaceAll("'","\\\\'"));
                    h.put("wn_paymentrespid",rs.getString(23)==null?"":rs.getString(23));
                    h.put("wv_billcycle",rs.getString(28));
                }

            }
        }catch (Exception e) {
            logger.error(formatException(e));
            h.put("strMessage",e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return h;
    }

    /**
     Method : getCustomerAddress
     Purpose: Obtener la lista de direcciones del cliente
     Developer        Fecha       Comentario
     =============    ==========  ======================================================================
     Lee Rosales      03/07/2007  Creación
     Walter Sotomayor 30/12/2009  Se modifica el procedure para que devuelva los id del distrito, provincia y departamento.
     */

    public HashMap getCustomerAddress(long intObjectId, long longRegionId, String strObjectType,String strGeneratortype) throws Exception, SQLException{

        ArrayList list = new ArrayList();
        HashMap hshResultado  = new HashMap();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strMessage = null;
        try{
            String sqlStr = "BEGIN WEBSALES.SPI_GET_CUST_ADDRESS_LIST(?, ?, ?, ?, ?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, intObjectId);
            cstmt.setString(2, strObjectType);
            cstmt.setLong(3, longRegionId);
            cstmt.setString(4, strGeneratortype);
            cstmt.registerOutParameter(5, OracleTypes.CURSOR);
            cstmt.registerOutParameter(6, Types.VARCHAR);

            cstmt.executeUpdate();

            strMessage = cstmt.getString(6);
            if (strMessage == null){
                rs = (ResultSet)cstmt.getObject(5);

                HashMap h = null;

                while (rs.next()) {
                    h = new HashMap();
                    h.put("wn_swaddressid",rs.getInt("swaddressid")+"");
                    h.put("wv_swaddress1",rs.getString("swaddress1")==null?"":rs.getString("swaddress1"));
                    h.put("wv_swcity",rs.getString("swcity")==null?"":rs.getString("swcity"));
                    h.put("wv_swprovince",rs.getString("swprovince")==null?"":rs.getString("swprovince"));
                    h.put("wv_swstate",rs.getString("swstate")==null?"":rs.getString("swstate"));
                    h.put("wv_swtipodirec",rs.getString("TIPODIRECCION")==null?"":rs.getString("TIPODIRECCION"));
                    
                    h.put("wv_swcityid",rs.getString("swcityid")==null?"":rs.getString("swcityid"));
                    h.put("wv_swprovinceid",rs.getString("swprovinceid")==null?"":rs.getString("swprovinceid"));
                    h.put("wv_swstateid",rs.getString("swstateid")==null?"":rs.getString("swstateid"));
                    h.put("wv_swflagaddress",rs.getString("swflagaddress")==null?"":rs.getString("swflagaddress"));
                    h.put("wv_swreference", rs.getString("swnote")==null?"":rs.getString("swnote")); // EPV

                    list.add(h);
                }
            }
            hshResultado.put("objArrayList",list);
            hshResultado.put("strMessage",strMessage);
        }catch (Exception e) {
            logger.error(formatException(e));
            hshResultado.put("strMessage",e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return hshResultado;
    }


    /**
     Method : getCustomerContacts
     Purpose: Obtener la lista de direcciones del cliente
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     Lee Rosales     03/07/2007  Creación
     */

    public HashMap getCustomerContacts(long intObjectId, String strObjectType) throws Exception, SQLException{
        ArrayList list = new ArrayList();
        HashMap hshResultado  = new HashMap();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strMessage = null;
        try{
            String sqlStr = "BEGIN WEBSALES.SPI_GET_CUST_CONTACT_LIST(?, ?, ?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, intObjectId);
            cstmt.setString(2, strObjectType);
            cstmt.registerOutParameter(3, Types.CHAR);
            cstmt.registerOutParameter(4, OracleTypes.CURSOR);
            cstmt.executeUpdate();
            strMessage = cstmt.getString(3);
            if (strMessage == null){
                rs = (ResultSet)cstmt.getObject(4);
                while (rs.next()) {
                    Hashtable h = new Hashtable();
                    h.put("wn_swpersonid",rs.getInt(1)+"");
                    h.put("wv_swname",rs.getString(2)==null?"":rs.getString(2));
                    h.put("wv_swphone",rs.getString(3)==null?"":rs.getString(3));
                    h.put("wv_swphonearea",rs.getString(4)==null?"":rs.getString(4));
                    h.put("wv_swfax",rs.getString(5)==null?"":rs.getString(5));
                    h.put("wv_swfaxarea",rs.getString(6)==null?"":rs.getString(6));
                    h.put("wv_swflagusuario",rs.getString(7)==null?"":rs.getString(7));
                    h.put("wv_swflagfacturacion",rs.getString(8)==null?"":rs.getString(8));
                    h.put("wv_swflaggerenteg",rs.getString(9)==null?"":rs.getString(9));
                    h.put("wv_message",strMessage + "");
                    list.add(h);
                }
            }
            hshResultado.put("objArrayList",list);
            hshResultado.put("strMessage",strMessage);
        }catch (Exception e) {
            logger.error(formatException(e));
            hshResultado.put("strMessage",e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return hshResultado;
    }

    /**
     Method : getCustomerSites
     Purpose: Obtener la lista de Sites del cliente
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     Lee Rosales     02/07/2007  Creación
     */

    public HashMap getCustomerSites(long intCustomerId, String strEstadoSite) throws Exception, SQLException{

        ArrayList list = new ArrayList();
        HashMap   hshResultado  = new HashMap();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strMessage = null;
        try{
            String sqlStr = "BEGIN WEBSALES.SPI_GET_SITE_LIST2(?, ?, ?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, intCustomerId);
            cstmt.setString(2, strEstadoSite);
            cstmt.registerOutParameter(3, Types.CHAR);
            cstmt.registerOutParameter(4, OracleTypes.CURSOR);
            cstmt.executeUpdate();
            strMessage = cstmt.getString(3);
            if( strMessage != null ){
                hshResultado.put("strMessage",strMessage);
                return hshResultado;
            }
            rs = (ResultSet)cstmt.getObject(4);

            while (rs.next()) {

                HashMap h = new HashMap();

                h.put("wn_swsiteid",rs.getInt(1)+"");
                h.put("wv_swsitename",rs.getString(2)==null?"":rs.getString(2));
                h.put("wv_npcodbscs",rs.getString(3)==null?"":rs.getString(3));
                h.put("wv_message",strMessage + "");

                list.add(h);

            }
            hshResultado.put("objArrayList",list);
            hshResultado.put("strMessage",strMessage);
        }catch (Exception e) {
            logger.error(formatException(e));
            hshResultado.put("strMessage",e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return hshResultado;
    }

    /**
     Method : getCustomerSearch
     Purpose: Busqueda de cliente por datos de ingreso
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     Lee Rosales     05/07/2007  Creación
     Alexis Gamarra  16/07/2009  Modificación para Jerarquía de Ventas
     */
    public HashMap getCustomerSearch(          String strTipoDocumento,
                                               String strNumeroDocumento,
                                               String strRazonSocial,
                                               String strNombreCliente,
                                               String strTipoCliente,
                                               long intCustomerId,
                                               long intRegionId,
                                               String strSessionCode,
                                               int intSessionLevel,
                                               int intNumPage,
                                               int iProGrpId,
                                               String strLogin,
                                               int intSalesStructId,
                                               int iUserId1,
                                               int iAppId1
    ) throws Exception, SQLException{

        System.out.println("[CustomerDAO][getCustomerSearch]iProGrpId:"+iProGrpId);
        System.out.println("[CustomerDAO][getCustomerSearch]strLogin:"+strLogin);

        ArrayList list = new ArrayList();
        HashMap hshResultado  = new HashMap();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strMessage = null;

        int intNumeroRegistros = 0;

        String sqlStr = "BEGIN WEBSALES.NPSL_CUSTOMER01_PKG.SP_GET_CUSTOMER_SEARCH(" +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+//10
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+//20
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+//30
                "?, ?, ?, ?); END; ";//34


        try{
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

            cstmt.setString(1, strTipoDocumento);
            cstmt.setString(2, strNumeroDocumento);
            cstmt.setString(3, strRazonSocial);
            cstmt.setString(4, strNombreCliente);
            cstmt.setString(5, strTipoCliente);
            cstmt.setString(6, null);//cstmt.setNull(6,OracleTypes.NULL);
            cstmt.setInt(7, (int)intCustomerId);//cstmt.setInt(7,intCustomerId);
            cstmt.setInt(8, 0);//cstmt.setNull(8, OracleTypes.NULL);//cstmt.setInt(8,OracleTypes.NULL);

            cstmt.setString(9, null);//cstmt.setNull(9,OracleTypes.NULL);
            cstmt.setString(10, null);//cstmt.setNull(10,OracleTypes.NULL);
            cstmt.setString(11, null);//cstmt.setNull(11,OracleTypes.NULL);
            cstmt.setString(12, null);//cstmt.setNull(12,OracleTypes.NULL);
            cstmt.setInt(13, 0);//cstmt.setNull(13, OracleTypes.NULL);
            cstmt.setString(14, null);//cstmt.setNull(14,OracleTypes.NULL);
            cstmt.setString(15, null);//cstmt.setNull(15,OracleTypes.NULL);
            cstmt.setString(16, null);//cstmt.setNull(16,OracleTypes.NULL);
            cstmt.setString(17, null);//cstmt.setNull(17,OracleTypes.NULL);

            cstmt.setInt(18, (int)intRegionId);
            cstmt.setInt(19, 0);//cstmt.setNull(19, OracleTypes.NULL);











            cstmt.setInt(20, intSalesStructId);//OracleTypes.NULL);
            cstmt.setString(21, null);//cstmt.setNull(21,OracleTypes.NULL);            
            cstmt.setInt(22, iUserId1);//setNull(22, OracleTypes.NULL);
            cstmt.setInt(23, 0);//setNull(23, OracleTypes.NULL);

            cstmt.setInt(24, iProGrpId);
            cstmt.setInt(25, 0);//setNull(25, OracleTypes.NULL);
            cstmt.setString(26, strLogin);
            cstmt.setString(27, null);//cstmt.setNull(27, OracleTypes.NULL);//;
            cstmt.setInt(28, 0);//setNull(28, OracleTypes.NULL);
            cstmt.setInt(29, 0);//setNull(29, OracleTypes.NULL);
            cstmt.setString(30,null);//cstmt.setNull(30, OracleTypes.NULL);//
            cstmt.setInt(31, iAppId1);//setNull(31, OracleTypes.NULL);

            cstmt.registerOutParameter(32, OracleTypes.CURSOR);
            cstmt.registerOutParameter(33, OracleTypes.NUMBER);
            cstmt.registerOutParameter(34, OracleTypes.VARCHAR);

            cstmt.execute();//Update();

            //dbmsOutput.show();
            //dbmsOutput.close();
            strMessage = cstmt.getString(34);
            System.out.println("[getCustomerSearch]strMessage: "+strMessage);

            if (strMessage==null || strMessage.startsWith(Constante.SUCCESS_ORA_RESULT)){
                rs = cstmt.getCursor(32);
                intNumeroRegistros = cstmt.getInt(33);

                System.out.println("[getCustomerSearch]intNumeroRegistros: "+ intNumeroRegistros+"");

                int intPos = 1;
                while (rs.next()) {
                    HashMap h = new HashMap();

                    h.put("rownum",intPos+++"");

                    h.put("wv_swruc",rs.getString(1)+"");
                    h.put("wv_swname", MiUtil.getString(rs.getString(2)));
                    h.put("wv_swnamecom", MiUtil.getString(rs.getString(3)));
                    h.put("wv_swtype", MiUtil.getString(rs.getString(4)));
                    h.put("wv_swcreatedby", MiUtil.getString(rs.getString(5)));
                    h.put("wn_swcustomerid", rs.getInt(6)+"");
                    h.put("wv_swcantempl", MiUtil.getString(rs.getString(7)));
                    h.put("wv_swdatecreated", MiUtil.getString(rs.getString(8)));
                    h.put("wv_swganadop", MiUtil.getString(rs.getString(9)));
                    h.put("wv_npnacional", MiUtil.getString(rs.getString(10)));
                    h.put("wv_npcorporativo", MiUtil.getString(rs.getString(11)));
                    h.put("wv_swtipoperson", MiUtil.getString(rs.getString(12)));
                    h.put("wv_swrating", MiUtil.getString(rs.getString(13)));
                    h.put("wv_npratingprospect", MiUtil.getString(rs.getString(14)));
                    h.put("wn_swregionid", rs.getInt(15)+"");
                    h.put("wv_swmainphone", MiUtil.getString(rs.getString(16)));
                    h.put("wn_npgiroid", rs.getInt(17)+"");
                    h.put("wv_giro", MiUtil.getString(rs.getString(18)));
                    h.put("wv_npcustomerrelationtype", MiUtil.getString(rs.getString(19)));
                    h.put("wv_npdivisionid", MiUtil.getString(rs.getString(20)));
                    h.put("wv_npdivisionname", MiUtil.getString(rs.getString(21)));

                    h.put("wv_message", strMessage + "");
                    h.put("wn_numregistros", intNumeroRegistros  + "");
                    list.add(h);
                }
                System.out.println("[FIN CARGA HASHMAP]");
                hshResultado.put("objArrayList",list);
                hshResultado.put("numTotalRegisters",String.valueOf(intNumeroRegistros));
                hshResultado.put("strMessage",strMessage);
            }
        }catch(Exception e){
            e.printStackTrace();
            logger.error(formatException(e));
            hshResultado.put("strMessage",e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return hshResultado;
    }
     
     
     /*
    public HashMap getCustomerSearch(String strTipoDocumento, 
                                               String strNumeroDocumento,
                                               String strRazonSocial,
                                               String strNombreCliente,
                                               String strTipoCliente,
                                               long intCustomerId,
                                               long intRegionId,
                                               String strSessionCode,
                                               int intSessionLevel,
                                               int intNumPage,
                                               int iProGrpId,
                                               String strLogin)  throws Exception, SQLException{
       
       System.out.println("[CustomerDAO][getCustomerSearch]iProGrpId:"+iProGrpId);
       System.out.println("[CustomerDAO][getCustomerSearch]strLogin:"+strLogin);

       ArrayList list = new ArrayList();
       HashMap hshResultado  = new HashMap();
       Connection conn = null;
       OracleCallableStatement cstmt = null;
       ResultSet rs = null;
       String strMessage = null;
       //DbmsOutput dbmsOutput = null;

       int intNumeroRegistros = 0;

       String sqlStr = 
                  "BEGIN WEBSALES.NPSL_CUSTOMER_PKG.SP_GET_CUSTOMER_SEARCH1( " +
                  " ?,    ?,    ?,    ?,    ?, " +
                  "NULL, ?, NULL, NULL, NULL," +
                  "NULL, NULL, NULL, NULL, NULL," +
                  "NULL, NULL, ?, NULL, ?," +//El último es de an_salesstructid
                  "NULL, NULL, NULL,    ?, NULL," +
                  "?, NULL, NULL, NULL, NULL," +
                  "?,    ?,    ?,  ?); END;";//El 1ero es de an_appid
        try{
            conn = Proveedor.getConnection();
            //dbmsOutput = new DbmsOutput(conn);
            //dbmsOutput.enable(1000000);           
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            
            cstmt.setString(1, strTipoDocumento);
            cstmt.setString(2, strNumeroDocumento);
            cstmt.setString(3, strRazonSocial);
            cstmt.setString(4, strNombreCliente);
            cstmt.setString(5, strTipoCliente);
            cstmt.setLong(6, intCustomerId);
            cstmt.setLong(7, intRegionId);
            cstmt.setString(8, strSessionCode);
            cstmt.setInt(9, intSessionLevel);
            //JOyola - 17/09/2008 - Se agregaron 2 parametros para la busqueda cuando es consultor indirecto
            cstmt.setInt(10, iProGrpId);
            cstmt.setString(11, strLogin);
            cstmt.setInt(12, intNumPage);
            cstmt.registerOutParameter(13, OracleTypes.CURSOR);
            cstmt.registerOutParameter(14, Types.NUMERIC);
            cstmt.registerOutParameter(15, Types.VARCHAR);
           
            
           
           
            cstmt.executeUpdate();
            //dbmsOutput.show();
            //dbmsOutput.close();
            strMessage = cstmt.getString(15);
            System.out.println("[getCustomerSearch]strMessage: "+strMessage);
            //OJO el API devuelve como mensaje de exito
            //strMessage=ORA-0000: normal, successful completion
            //if (strMessage == null){
            rs = cstmt.getCursor(13);
            intNumeroRegistros = cstmt.getInt(14);
            System.out.println("[getCustomerSearch]intNumeroRegistros: "+ intNumeroRegistros+"");
            while (rs.next()) {
                HashMap h = new HashMap();                 
                h.put("rownum",rs.getString(1)+"");                           
                h.put("wv_swruc",rs.getString(2)+"");              
                h.put("wv_swname",rs.getString(3)==null?"":rs.getString(3));          
                h.put("wv_swnamecom",rs.getString(4)==null?"":rs.getString(4));   
                h.put("wv_swtype",rs.getString(5)==null?"":rs.getString(5));           
                h.put("wv_swcreatedby",rs.getString(6)==null?"":rs.getString(6));        
                h.put("wn_swcustomerid",rs.getInt(7)+""); 
                h.put("wv_swcantempl",rs.getString(8)==null?"":rs.getString(8)); 
                h.put("wv_swdatecreated",rs.getString(9)==null?"":rs.getString(9)); 
                h.put("wv_swganadop",rs.getString(10)==null?"":rs.getString(10)); 
                h.put("wv_npnacional",rs.getString(11)==null?"":rs.getString(11)); 
                h.put("wv_npcorporativo",rs.getString(12)==null?"":rs.getString(12)); 
                h.put("wv_swtipoperson",rs.getString(13)==null?"":rs.getString(13)); 
                h.put("wv_swrating",rs.getString(14)==null?"":rs.getString(14)); 
                h.put("wv_npratingprospect",rs.getString(15)==null?"":rs.getString(15)); 
                h.put("wn_swregionid",rs.getInt(16)+""); 
                h.put("wv_swmainphone",rs.getString(17)==null?"":rs.getString(17)); 
                h.put("wn_npgiroid",rs.getInt(18)+""); 
                h.put("wv_giro",rs.getString(19)==null?"":rs.getString(19)); 
                h.put("wv_npcustomerrelationtype",rs.getString(20)==null?"":rs.getString(20));
                h.put("wv_message",strMessage + "");  
                h.put("wn_numregistros",intNumeroRegistros  + "");                 
                list.add(h);            
            }
           System.out.println("[FIN CARGA HASHMAP]");
          hshResultado.put("objArrayList",list);
          hshResultado.put("numTotalRegisters",String.valueOf(intNumeroRegistros));
          hshResultado.put("strMessage",strMessage);
        }catch (Exception e) {
          logger.error(formatException(e)); 
          hshResultado.put("strMessage",e.getMessage());    
        }
        finally{
          try{
            closeObjectsDatabase(conn, cstmt, rs);  
          }catch (Exception e) {
            logger.error(formatException(e));
          }
        }         
        return hshResultado;       
     }*/

    /**
     Method : getCustRucQty
     Purpose: Obtener las coincidencias de Ruc
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     Lee Rosales     19/07/2007  Creación
     */

    public Hashtable getCustRucQty(String strRuc) throws Exception,SQLException{

        Hashtable h = new Hashtable();

        Connection conn = null;
        OracleCallableStatement cstmt = null;

        String strMessage = null;
        int intCustomerId = 0;
        int intCustomerQty = 0;
        try{
            String sqlStr = "BEGIN WEBSALES.SPI_GET_CUST_RUC_QTY(?, ?, ?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setString(1, strRuc);
            cstmt.registerOutParameter(2, Types.CHAR);
            cstmt.registerOutParameter(3, Types.NUMERIC);
            cstmt.registerOutParameter(4, Types.NUMERIC);

            cstmt.executeUpdate();

            strMessage = cstmt.getString(2);

            if( strMessage == null ){
                intCustomerId = cstmt.getInt(3);
                intCustomerQty = cstmt.getInt(4);

                h.put("wv_ruc",strRuc);
                h.put("wn_customerid",intCustomerId + "");
                h.put("wn_customer_qty",intCustomerQty + "");
            }

            h.put("strMessage",strMessage + "");
        }catch (Exception e) {
            logger.error(formatException(e));
            h.put("strMessage",e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        return h;
    }

    /**
     Method : getCustPatternQty
     Purpose: Obtener las coincidencias de nombre de compañia
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     Lee Rosales     19/07/2007  Creación

     */

    public Hashtable getCustPatternQty(String strNombreCompania) throws Exception,SQLException{

        Hashtable h = new Hashtable();

        Connection conn = null;
        OracleCallableStatement cstmt = null;

        String strMessage = null;
        int intCustomerId = 0;
        int intCustomerQty = 0;
        try{
            String sqlStr = "BEGIN WEBSALES.SPI_GET_CUST_PATTERN_QTY(?, ?, ?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setString(1, strNombreCompania);
            cstmt.registerOutParameter(2, OracleTypes.NUMERIC);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(4, OracleTypes.NUMERIC);
            cstmt.executeUpdate();

            strMessage = cstmt.getString(3);

            if( strMessage == null ){
                intCustomerId   = cstmt.getInt(2);
                intCustomerQty  = cstmt.getInt(4);
                h.put("wv_name",strNombreCompania);
                h.put("wn_customerid",intCustomerId + "");
                h.put("wn_customer_qty",intCustomerQty + "");

            }
            h.put("strMessage",strMessage + "");
        }catch (Exception e) {
            logger.error(formatException(e));
            h.put("strMessage",e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return h;
    }

    /**
     Method : getCustomerIdCrmByBSCS
     Purpose: Obtener las coincidencias de nombre de compañia
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     Lee Rosales     19/07/2007  Creación

     */

    public Hashtable getCustomerIdCrmByBSCS(long intCodbscs) throws Exception,SQLException{

        Hashtable h = new Hashtable();

        Connection conn = null;
        OracleCallableStatement cstmt = null;

        String strMensaje = null;
        int intCustomerId = 0;
        try{
            String sqlStr = "BEGIN WEBSALES.SPI_GET_CUSTOMERID_CRM_BY_BSCS( ?, ?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

            cstmt.setLong(1, intCodbscs);
            cstmt.registerOutParameter(2, Types.CHAR);
            cstmt.registerOutParameter(3, Types.CHAR);

            cstmt.executeUpdate();
            strMensaje = cstmt.getString(2);
            if (strMensaje == null)
                intCustomerId = cstmt.getInt(3);

            h.put("wn_codbscs",intCodbscs + "");
            h.put("wn_customerid",intCustomerId + "");
            h.put("wv_message",strMensaje + "");
        }catch (Exception e) {
            logger.error(formatException(e));
            h.put("wv_message",e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return h;
    }

    /**
     Method : getRol
     Purpose: Obtiene el Rol para un usuario.
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     Lee Rosales     23/07/2007  Creación
     */

    public static int getRol(long intScreenoptionid, long intUserid, long intAppid)  throws Exception,SQLException{

        int intReturnValue = 0;

        Connection conn = null;
        OracleCallableStatement cstmt = null;
        try{
            String sqlStr = "BEGIN WEBSALES.SPI_GET_ROL( ?, ?, ?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, intScreenoptionid);
            cstmt.setLong(2, intUserid);
            cstmt.setLong(3, intAppid);
            cstmt.registerOutParameter(4, Types.NUMERIC);
            cstmt.executeUpdate();
            intReturnValue = cstmt.getInt(4);
        }catch (Exception e) {
            logger.error(formatException(e));
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return intReturnValue;
    }

    /**
     Method : getSiteData
     Purpose: Obtiene los datos del Site.
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     Lee Rosales     27/07/2007  Creación
     */

    public Hashtable getSiteData(long intSiteid, String strCreatedby, long intUserid, long intAppid, long intRegionid,
                                 String strGeneratortype, String strGeneratorId) throws Exception,SQLException{

        Hashtable h = new Hashtable();
        String strMessage = null;
        ResultSet rs = null;

        Connection conn = null;
        OracleCallableStatement cstmt = null;
        //DbmsOutput dbmsOutput = null;
        try{
            String sqlStr = "BEGIN WEBSALES.SPI_GET_SITE_DATA( ?, ?, ?, ?, ?, ?, ? ,?,?); END;";
            conn = Proveedor.getConnection();
            //dbmsOutput = new DbmsOutput(conn);
            //dbmsOutput.enable(1000000);
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

            cstmt.setLong(1, intSiteid);
            cstmt.setString(2, strCreatedby);
            cstmt.setLong(3, intUserid);
            cstmt.setLong(4, intAppid);
            cstmt.setLong(5, intRegionid);
            cstmt.setString(6, strGeneratortype);
            cstmt.setString(7, strGeneratorId);
            cstmt.registerOutParameter(8, Types.VARCHAR);
            cstmt.registerOutParameter(9, OracleTypes.CURSOR);
            cstmt.executeUpdate();
            //dbmsOutput.show();
            //dbmsOutput.close();
            strMessage = cstmt.getString(8);

            if( strMessage == null ){
                rs = (ResultSet)cstmt.getObject(9);
                if (rs.next()) {
                    h.put("wv_codbscs",rs.getString(1)==null?"":rs.getString(1));
                    h.put("wv_nplineacredito",rs.getString(2)==null?"":rs.getString(2));
                    h.put("wv_swnameregion",rs.getString(3)==null?"":rs.getString(3));
                    h.put("wn_vendedorid",rs.getInt(4)+"");
                    h.put("wv_vendedorname",rs.getString(5)==null?"":rs.getString(5));
                    h.put("wv_dealer",rs.getString(6)==null?"":rs.getString(6));
                    h.put("wn_vendedorid2",rs.getInt(11)+"");
                    h.put("wv_vendedorname2",rs.getString(12)==null?"":rs.getString(12));
                    h.put("wv_dealer2",rs.getString(13)==null?"":rs.getString(13));
                }
            }

            h.put("strMessage",strMessage+"");
        }catch (Exception e) {
            logger.error(formatException(e));
            h.put("strMessage", e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        return h;
    }

    /**
     Method : getCompanyInfo
     Purpose: Obtiene los datos de la compañia para valoracion de DATAMAT.
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     Lee Rosales     30/07/2007  Creación
     */

    public Hashtable getCompanyInfo(long intCustomerId)  throws Exception,SQLException{

        Hashtable h = new Hashtable();
        String strMensaje = null;
        ResultSet rs = null;

        Connection conn = null;
        OracleCallableStatement cstmt = null;
        try{
            String sqlStr = "BEGIN ORDERS.NP_ORDERS02_PKG.SP_GET_COMPANIA_INFO( ?, ?, ? ); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, intCustomerId);
            cstmt.registerOutParameter(2, Types.VARCHAR);
            cstmt.registerOutParameter(3, OracleTypes.CURSOR);
            cstmt.executeUpdate();
            strMensaje = cstmt.getString(2);
            if (strMensaje ==null){
                rs = (ResultSet)cstmt.getObject(3);
                h.put("wv_message",strMensaje + "");
                if (rs.next()) {
                    h.put("wn_codigoCompania",rs.getInt(1)+"");
                    h.put("wv_tipoCompania",rs.getString(2)==null?"":rs.getString(2));
                    h.put("wv_nombreCompania",rs.getString(3)==null?"":rs.getString(3));
                    h.put("wn_montoGarantia",rs.getInt(4)+"");
                    h.put("wn_valorTotalGarantia",rs.getInt(5)+"");
                }
            }
        }catch (Exception e) {
            logger.error(formatException(e));
            h.put("wv_message", e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return h;
    }


    public HashMap getAddressDelivery(long intObjectId, String strObjectType, String strGeneratorType, String strRegionId) throws Exception,SQLException{
        ArrayList list = new ArrayList();
        HashMap hshResultado  = new HashMap();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        AddressObjectBean objAddressObjectBean = null;
        try{
            String sqlStr = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_GET_REGIONADDRESS_LIST(?, ?, ?, ?, ?, ?); END;";

            String strMessage = "";

            conn = Proveedor.getConnection();

            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

            cstmt.setLong(1, intObjectId);
            cstmt.setString(2, strObjectType);
            cstmt.setString(3, strGeneratorType);
            cstmt.setString(4, strRegionId);
            cstmt.registerOutParameter(5, OracleTypes.CURSOR);
            cstmt.registerOutParameter(6, Types.VARCHAR);

            cstmt.executeUpdate();

            strMessage = cstmt.getString(6);
            if (strMessage == null){
                rs = (ResultSet)cstmt.getObject(5);
                while (rs.next()) {
                    objAddressObjectBean = new AddressObjectBean();
                    objAddressObjectBean.setSwaddress1(rs.getString("adress"));
                    objAddressObjectBean.setSwcity(rs.getString("swcity"));
                    objAddressObjectBean.setSwprovince(rs.getString("swprovince"));
                    objAddressObjectBean.setSwstate(rs.getString("swstate"));
                    objAddressObjectBean.setNpdistritoid(rs.getInt("swcityid"));
                    objAddressObjectBean.setNpprovinciaid(rs.getInt("swprovinceid"));
                    objAddressObjectBean.setNpdepartamentoid(rs.getInt("swstateid"));
                    list.add(objAddressObjectBean);
                }
            }
            hshResultado.put("objArrayList",list);
            hshResultado.put("strMessage",strMessage);
        }catch (Exception e) {
            logger.error(formatException(e));
            hshResultado.put("strMessage", e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return hshResultado;
    }

    public static int getUnknowSite(long intObjectId, String strObjectType) throws Exception,SQLException {
        Connection conn = null;
        OracleCallableStatement cstmt = null;


        //NUMBER nmObjectId = new NUMBER(intObjectId);
        int intResult =0;
        try{
            String sqlStr = " {  ? = CALL WEBSALES.NPSL_SITE_PKG.FX_GET_UNKNWN_SITE_ID(?, ?, ?) } ";
            String strMsgError = "";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, intObjectId);
            cstmt.setString(2, strObjectType);
            cstmt.registerOutParameter(3, Types.VARCHAR);
            cstmt.executeUpdate();
            strMsgError = cstmt.getString(3);
            if ( strMsgError == null )
                intResult = cstmt.getInt(1);
        }catch (Exception e) {
            logger.error(formatException(e));
            System.out.println("CustomerDAO.getUnknowSite "+e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return intResult;
    }

    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     * INTEGRACION DE ORDENES Y RETAIL - FIN
     * REALIZADO POR: Lee Rosales
     * FECHA: 11/09/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/

/***********************************************************************
 ***********************************************************************
 ***********************************************************************
 *  INTEGRACION DE ORDENES Y RETAIL - INICIO
 *  REALIZADO POR: Carmen Gremios
 *  FECHA: 13/09/2007
 ***********************************************************************
 ***********************************************************************
 ***********************************************************************/

    /**
     * Motivo: Obteniene el detalle del cliente dado el CustomerId
     * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
     * <br>Fecha: 23/08/2007
     * @param     iNpCustomerId  Ej. 27300
     * @return    HashMap
     */
    public  HashMap getCustomerData(long iNpCustomerId) throws SQLException {

        CustomerBean objCustomerBean = new CustomerBean();
        String strMessage=null;
        HashMap hshData=new HashMap();
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        Connection conn=null;

        try{
            String sqlStr =  "BEGIN WEBSALES.SPI_GET_CUSTOMER_DETAIL2(?, ?, ?); END;";
            conn = Proveedor.getConnection();

            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, iNpCustomerId);
            cstmt.registerOutParameter(2, Types.CHAR);
            cstmt.registerOutParameter(3, OracleTypes.CURSOR);
            cstmt.execute();
            strMessage = cstmt.getString(2);
            System.out.print("[getCustomerData]: " + strMessage);
            if( strMessage == null ){
                rs = cstmt.getCursor(3);
                if (rs.next()) {
                    objCustomerBean.setSwCustomerId(iNpCustomerId);
                    objCustomerBean.setSwName(rs.getString("RazonSocial")); //RazonSocial
                    objCustomerBean.setSwRuc(rs.getString("swruc")); //swruc
                    objCustomerBean.setSwCodBscs(rs.getString("swcodbscs")); //swcodbscs
                    objCustomerBean.setNpCorporativo(rs.getString("tipocuenta")); //tipocuenta
                    objCustomerBean.setNpGiroName(rs.getString("gironegocio")); //gironegocio
                    objCustomerBean.setSwMainPhone(rs.getString("swmainphone")); //swmainphone
                    objCustomerBean.setSwMainPhoneArea(rs.getString("swmainphonearea")); //swmainphone
                    objCustomerBean.setDepMainphonearea(rs.getString("depmainphonearea"));

                    objCustomerBean.setSwPhone2(rs.getString("swphone2")); //swphone2
                    objCustomerBean.setNpPhone2areacode(rs.getString("npphone2areacode")); //npphone2areacode
                    objCustomerBean.setDepPhone2areacode(rs.getString("depphone2area"));

                    objCustomerBean.setSwPhone3(rs.getString("swphone3")); //swphone3
                    objCustomerBean.setNpPhone3areacode(rs.getString("npphone3areacode")); //npphone3areacode
                    objCustomerBean.setDepPhone3areacode(rs.getString("depphone3area"));

                    objCustomerBean.setSwMainFax(rs.getString("swmainfax")); //swmainfax;
                    objCustomerBean.setSwMainFaxArea(rs.getString("swmainfaxarea")); //swmainfaxarea;
                    objCustomerBean.setDepMainFaxArea(rs.getString("depmainfaxarea")); //swmainfaxarea;

                    objCustomerBean.setSwGanadoP(rs.getString("consultor")); //consultor
                    objCustomerBean.setNpPartnerCodBscs(rs.getString("dealer")); //dealer
                    objCustomerBean.setSwRegionName(rs.getString("region")); //region
                    objCustomerBean.setSwNameCom(rs.getString("corporatename")); //corporatename
                    objCustomerBean.setSwCanEmpl(rs.getString("cantEmpl")); //cantEmpl
                    objCustomerBean.setSwType(rs.getString("swtype")); //swtype
                    objCustomerBean.setSwRating(rs.getString("swrating")); //swrating
                    objCustomerBean.setSwTipoPerson(rs.getString("swtipoperson")); //swtipoperson
                    objCustomerBean.setSwRegionId(rs.getLong("swregionid"));
                    objCustomerBean.setNpTipoDoc(rs.getString("nptipodoc"));
                }
            }
            hshData.put("objCustomerBean",objCustomerBean);
            hshData.put("strMessage",strMessage);
        }catch (Exception e) {
            logger.error(formatException(e));
            hshData.put("strMessage",e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        return hshData;
    }

    /**
     * Motivo: Obteniene el detalle de un cliente dado
     * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
     * <br>Fecha: 23/07/2007
     * @param     iCustomerId
     * @param     strSwcreatedBy
     * @param     iRegionid
     * @param     strMessage       Contiene el mensaje de error si existiera
     * @return    CustomerBean
     */
    public HashMap getCustomerData2(long iCustomerId,
                                    String strSwcreatedBy,
                                    long iRegionid )
            throws SQLException, Exception {

        CustomerBean objCustomerBean = null;
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        //DbmsOutput dbmsOutput = null;
        ResultSet rs = null;


        String strMessage=null;
        HashMap hshData=new HashMap();
        String sqlStr = "BEGIN WEBSALES.SPI_GET_CUSTOMER_DATA(?, ?, ?, ?, ?); END;";
        try{
            conn = Proveedor.getConnection();
            //dbmsOutput = new DbmsOutput(conn);
            //dbmsOutput.enable(1000000);
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

            cstmt.setLong(1, iCustomerId);
            cstmt.setString(2, strSwcreatedBy);
            cstmt.setLong(3, iRegionid);
            cstmt.registerOutParameter(4, OracleTypes.CURSOR);
            cstmt.registerOutParameter(5, Types.CHAR);

            cstmt.executeUpdate();
            //dbmsOutput.show();
            //dbmsOutput.close();

            strMessage = cstmt.getString(5);
            System.out.println("[CustomerDAO][getCustomerData2]iCustomerId: "+iCustomerId+"");
            System.out.println("[CustomerDAO][getCustomerData2]strSwcreatedBy: "+strSwcreatedBy);
            System.out.println("[CustomerDAO][getCustomerData2]iRegionid: "+iRegionid+"");
            System.out.println("[CustomerDAO][getCustomerData2]strMessage: "+strMessage);
            if (strMessage==null){
                rs = (ResultSet)cstmt.getCursor(4);
                if (rs.next()) {
                    objCustomerBean = new CustomerBean();
                    objCustomerBean.setSwCustomerId(rs.getInt(1));
                    objCustomerBean.setSwName(rs.getString(2));
                    objCustomerBean.setSwRuc(rs.getString(4));
                    objCustomerBean.setSwType(rs.getString(6));
                    objCustomerBean.setSwCodBscs(rs.getString(5));
                    objCustomerBean.setSwRating(rs.getString(7));
                    objCustomerBean.setNpCustomerRelationType(rs.getString(17));
                    objCustomerBean.setNpLineaCredito(MiUtil.parseInt(rs.getString(8)));
                    objCustomerBean.setNpCustomerRelationid(rs.getInt(18));
                    objCustomerBean.setNpCustomerRelationName(rs.getString(19));
                    objCustomerBean.setNpCorporativo(rs.getString(13)); //tipocuenta
                    objCustomerBean.setNpGiroName(rs.getString(12)); //gironegocio
                    objCustomerBean.setSwMainPhone(rs.getString(9)); //swmainphone
                    objCustomerBean.setSwMainFax(rs.getString(10)); //swmainfax;
                    objCustomerBean.setSwRegionName(rs.getString(11)); //region
                    objCustomerBean.setSwNameCom(rs.getString(3)); //corporatename
                    objCustomerBean.setSwRegionId(rs.getLong(20));
                    objCustomerBean.setNpTipoDoc(rs.getString(21));
                    objCustomerBean.setStrExclusivity(rs.getString(22));
                    objCustomerBean.setStrRucValid(rs.getString(23));
                    objCustomerBean.setStrNameValid(rs.getString(24));
                    objCustomerBean.setNpSegmento(rs.getString(26));
                    objCustomerBean.setSwTipoPerson(rs.getString(28));
                    objCustomerBean.setNpAutoLpdp(rs.getString(29)); //autorizacion LPDP AMATEO
                    objCustomerBean.setNpFechaLpdp(rs.getString(30));//fecha actualizacion LPDP AMATEO
                }
            }
            //System.out.println("[CustomerDAO][getCustomerData2]"+objCustomerBean.getStrExclusivity());
        }
        catch (Exception e) {
            logger.error(formatException(e));
            hshData.put("strMessage",e.getMessage());
            throw new Exception(e);
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        hshData.put("strMessage",strMessage);
        hshData.put("objCustomerBean",objCustomerBean);

        return hshData;
    }


    /**
     * Motivo: Obteniene la lista de direcciones (tipos: Usuario, Facturación y Gerente General )
     * del cliente o del site dado el id (del cliente o del site) y el tipo ('CUSTOMER' o 'SITE')
     * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
     * <br>Fecha: 03/07/2007
     * @param     intObjectId     Ejm.  27300
     * @param     strObjectType   Ejm.  CUSTOMER
     * @return    HashMap
     */
    public   HashMap getCustomerContacts2(long intObjectId, String strObjectType)
            throws SQLException {

        ArrayList list = new ArrayList();
        HashMap hshData =new HashMap();
        String strMessage=null;
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        try{
            String sqlStr = "BEGIN WEBSALES.SPI_GET_CUST_CONTACT_LIST(?, ?, ?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

            cstmt.setLong(1, intObjectId);
            cstmt.setString(2, strObjectType);
            cstmt.registerOutParameter(3, Types.CHAR);
            cstmt.registerOutParameter(4, OracleTypes.CURSOR);

            cstmt.executeUpdate();

            strMessage = cstmt.getString(3);
            if (strMessage == null){
                rs = (ResultSet)cstmt.getObject(4);

                while (rs.next()) {
                    HashMap h = new HashMap();
                    h.put("wn_swpersonid", rs.getInt(1) + "");
                    h.put("wv_swname", rs.getString(2));
                    h.put("wv_swphone", rs.getString(3));
                    h.put("wv_swphonearea",rs.getString(4));
                    h.put("wv_swfax", rs.getString(5));
                    h.put("wv_swfaxarea",rs.getString(6));
                    h.put("wv_swflagusuario", rs.getString(7));
                    h.put("wv_swflagfacturacion",rs.getString(8));
                    h.put("wv_swflaggerenteg", rs.getString(9));

                    list.add(h);
                }
            }
            hshData.put("arrContactsList",list);
            hshData.put("strMessage",strMessage);
        }catch (Exception e) {
            logger.error(formatException(e));
            hshData.put("strMessage",e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        return hshData;
    }

    /**
     * Motivo: Obteniene la lista de direcciones del cliente o del site dado el id (del cliente o del site) y el tipo ('CUSTOMER' o 'SITE')
     * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
     * <br>Fecha: 03/07/2007
     * @param     intObjectId     Ejm.  27300
     * @param     strObjectType   Ejm.  CUSTOMER
     * @param     strMessage    Contiene el mensaje de error si existiera
     * @return    ArrayList de objetos del tipo HashMap
     */
    public   HashMap getCustomerAddress2(long intObjectId, long longRegionId, String strObjectType)
            throws SQLException {
        ArrayList list = new ArrayList();
        String strMessage=null;
        HashMap hshData =new HashMap();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        HashMap h=null;
        try{
            String sqlStr = "BEGIN WEBSALES.SPI_GET_CUST_ADDRESS_LIST(?, ?, ?, ?, ?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, intObjectId);
            cstmt.setString(2, strObjectType);
            cstmt.setLong(3, longRegionId);
            cstmt.setString(4, "");
            cstmt.registerOutParameter(5, OracleTypes.CURSOR);
            cstmt.registerOutParameter(6, Types.VARCHAR);

            cstmt.executeUpdate();

            strMessage = cstmt.getString(6);
            if (strMessage ==null  ){
                rs = (ResultSet)cstmt.getObject(5);
                while (rs.next()) {
                    h = new HashMap();
                    h.put("wn_swaddressid", rs.getInt(1) + "");
                    h.put("wv_swaddress1", rs.getString(2));
                    h.put("wv_swcity", rs.getString(3));
                    h.put("wv_swprovince", rs.getString(4));
                    h.put("wv_swstate", rs.getString(5));
                    h.put("wv_swtipodirec", rs.getString(6));
                    h.put("wv_swcityid",rs.getString("swcityid")==null?"":rs.getString("swcityid"));
             /*h.put("wv_swflagfacturacion", rs.getString(7));
             h.put("wv_swflagentrega", rs.getString(8));       */
                    list.add(h);

                }
            }
            hshData.put("strMessage",strMessage);
            hshData.put("arrAddressList",list);
        }catch (Exception e) {
            logger.error(formatException(e));
            hshData.put("strMessage",e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return hshData;
    }

    /**
     * Motivo: Retorna 'Customer' o 'Prospecto'
     * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
     * <br>Fecha: 02/10/2007
     * @param     lCustomerId
     * @return    String
     */
    public String getCustomerType(long lCustomerId)
            throws SQLException {
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        String strNombre=null;
        try{
            String sqlStr = "BEGIN WEBSALES.SPI_GET_CUSTOMER_TYPE(?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setLong(1, lCustomerId);
            cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            strNombre = cstmt.getString(2);
        }catch (Exception e) {
            logger.error(formatException(e));
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        return strNombre;
    }

    /**
     * Motivo: Obteniene la lista de contactos del cliente
     * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
     * <br>Fecha: 03/07/2007
     * @param     lCustomerId
     * @param     iResultado
     * @return    ArrayList de objetos del tipo HashMap
     */
    public   ArrayList getCustomerContactAll(long lCustomerId, int iResultado)
            throws SQLException {
        ArrayList list = new ArrayList();

        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        HashMap h=null;
        try{
            String sqlStr = "BEGIN WEBSALES.SPI_GET_CUSTOMER_CONTACT_LIST(?, ?, ?); END;";

            conn = Proveedor.getConnection();

            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, lCustomerId);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, Types.NUMERIC);
            cstmt.executeUpdate();

            iResultado = cstmt.getInt(3);
            rs = (ResultSet)cstmt.getObject(2);

            while (rs.next()) {
                h = new HashMap();
                h.put("contactid", rs.getLong(1) + "");
                h.put("description", rs.getString(2));
                list.add(h);
            }
        }catch (Exception e) {
            logger.error(formatException(e));
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        return list;
    }

    /**
     * Motivo: Obteniene la lista de contactos del site
     * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
     * <br>Fecha: 03/07/2007
     * @param     lCustomerId
     * @param     lSiteId
     * @param     iResultado
     * @return    ArrayList de objetos del tipo HashMap
     */
    public   ArrayList getSiteContactAll(long lCustomerId,long lSiteId, int iResultado)
            throws SQLException {
        ArrayList list = new ArrayList();

        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        HashMap h=null;
        try{
            String sqlStr = "BEGIN WEBSALES.SPI_GET_SITE_CONTACT_LIST(?, ?, ?, ?); END;";

            conn = Proveedor.getConnection();

            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

            cstmt.setLong(1, lCustomerId);
            cstmt.setLong(2, lSiteId);
            cstmt.registerOutParameter(3, OracleTypes.CURSOR);
            cstmt.registerOutParameter(4, Types.NUMERIC);
            cstmt.execute();

            iResultado = cstmt.getInt(4);
            rs = (ResultSet)cstmt.getObject(3);

            while (rs.next()) {
                h = new HashMap();
                h.put("contactid", rs.getLong(1) + "");
                h.put("description", rs.getString(2));
                list.add(h);
            }
        }catch (Exception e) {
            logger.error(formatException(e));
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        return list;
    }

    /**
     * Motivo: Retorna la direcion de Envio de un Site o Customer
     * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
     * <br>Fecha: 03/10/2007
     * @param     lCustomerId
     * @return    String
     */
    public HashMap getDeliveryAddress(long lObjectId,String strObjectType)
            throws SQLException {
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        String strAddress=null;
        String strMessage=null;
        HashMap hshData=new HashMap();
        try{
            String sqlStr = "BEGIN WEBSALES.SPI_GET_DELIVERY_ADDRESS(?, ?, ?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setLong(1, lObjectId);
            cstmt.setString(2, strObjectType);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            strAddress = cstmt.getString(3);
            strMessage = cstmt.getString(4);

            hshData.put("strMessage",strMessage);
            hshData.put("strAddress",strAddress);
        }catch (Exception e) {
            logger.error(formatException(e));
            hshData.put("strMessage",e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }


        return hshData;
    }

    /**
     * Motivo: Retorna la direcion de Envio de un Site o Customer
     * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
     * <br>Fecha: 03/10/2007
     * @param     lCustomerId
     * @return    String
     */
    public HashMap getAddressByRegion(long lObjectId,String strObjectType,long lSellerRegId)
            throws SQLException {
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        String strAddress=null;
        HashMap hshData=new HashMap();
        String strMessage=null;
        try{
            String sqlStr = "BEGIN WEBSALES.SPI_GET_ADDRESS_BY_REGION(?, ?, ?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setLong(1, lObjectId);
            cstmt.setString(2, strObjectType);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
            cstmt.executeQuery();
            strAddress = cstmt.getString(3);
            strMessage = cstmt.getString(4);

            hshData.put("strMessage",strMessage);
            hshData.put("strAddress",strAddress);
        }catch (Exception e) {
            logger.error(formatException(e));
            hshData.put("strMessage",e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return hshData;
    }

    /**
     * Motivo: Obteniene el detalle del cliente dado el CustomerId
     * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
     * <br>Fecha: 10/10/2007
     * @param     iNpCustomerId  Ej. 27300
     * @return    HashMap
     */
    public HashMap getCustomerSitesList(long intCustomerId,long lOrderId,long lOportunityId,String strStatus)
            throws SQLException {

        ArrayList list = new ArrayList();
        OracleCallableStatement cstmt = null;
        OracleCallableStatement cstmtsg = null;
        ResultSet rs = null;
        HashMap hshData=new HashMap();
        Connection conn=null;
        String strMessage=null;
        try{
            String sqlStr = "BEGIN WEBSALES.SPI_GET_SITE_LIST3(?, ?, ?, ?, ?, ?); END;";

            conn = Proveedor.getConnection();

            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, intCustomerId);
            cstmt.setLong(2, lOrderId);
            cstmt.setLong(3, lOportunityId);
            cstmt.setString(4, strStatus);
            cstmt.registerOutParameter(5, OracleTypes.CURSOR);
            cstmt.registerOutParameter(6, Types.VARCHAR);

            cstmt.executeUpdate();
            strMessage = cstmt.getString(6);
            if (strMessage ==null){
                rs = (ResultSet)cstmt.getObject(5);
                while (rs.next()) {
                    HashMap h = new HashMap();
                    h.put("swsiteid",  rs.getLong(1) + "");
                    h.put("swsitename",rs.getString(2));
                    h.put("npcodbscs", rs.getString(3));
                    h.put("swregionid", rs.getLong(4)+ "");
                    h.put("regionname", rs.getString(5));
                    h.put("status", rs.getString(6));
                    h.put("createdby", rs.getString(7));
                    h.put("nplineacredito", rs.getString(8));

                    sqlStr = "BEGIN WEBSALES.SPI_GET_SITE_SOLUTION_GROUP(?, ?, ?, ?); END;";
                    cstmtsg = (OracleCallableStatement)conn.prepareCall(sqlStr);
                    cstmtsg.setLong(1, MiUtil.parseLong(h.get("swsiteid").toString()));
                    cstmtsg.registerOutParameter(2, OracleTypes.NUMBER);
                    cstmtsg.registerOutParameter(3, Types.VARCHAR);
                    cstmtsg.registerOutParameter(4, Types.VARCHAR);
                    cstmtsg.executeUpdate();
                    strMessage = cstmtsg.getString(4);
                    if (strMessage ==null){
                        h.put("swsolutiongroup", cstmtsg.getObject(2));
                    }
                    list.add(h);
                }
            }

            hshData.put("strMessage",strMessage);
            hshData.put("arrCustomerSiteList",list);
        }catch (Exception e) {
            logger.error(formatException(e));
            hshData.put("strMessage",e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(null, cstmtsg, null);
                closeObjectsDatabase(conn, cstmt, rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return hshData;
    }

    /**
     * Motivo: Inserta los cambios que se solicitan en los datos del cliente, site y billing account.
     * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
     * <br>Fecha: 05/11/2007
     * @param     lOrderId
     * @param     iObjectType
     * @param     lObjectId
     * @param     strName
     * @param     strNameNew
     * @param     objCutomerBean
     * @param     objCutomerBeanNew
     * @param     objAddressBean
     * @param     objAddressBeanNew
     * @param     objContactBean
     * @param     objContactBeanNew
     * @return    String
     */
    public   String insChangeCustomer(long lOrderId, int iObjectType, long lCustomerId,long lSiteId,
                                      String strName,String strNameNew,
                                      CustomerBean objCutomerBean,CustomerBean objCutomerBeanNew,
                                      AddressObjectBean objAddressBean, AddressObjectBean objAddressBeanNew,
                                      ContactObjectBean objContactBean, ContactObjectBean objContactBeanNew,
                                      java.sql.Connection conn
    )
            throws SQLException {
        String strMessage=null;
        //Connection conn = null;
        OracleCallableStatement cstmt = null;
        try{

            // SAR 0037-167824 INI
    /* String sqlStr = "BEGIN WEBSALES.SPI_INSERT_CHANGE_CUSTOMER(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                                                                 "?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +  
                                                                 "?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +  
                                                                 "?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +  
                                                                 "?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +  
                                                                 "?, ?, ?, ?, ?, ?, ?, ?, ?); END;";
																 
	*/

            String sqlStr = "BEGIN WEBSALES.SPI_INSERT_CHANGE_CUSTOMER(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); END;";

            // SAR 0037-167824 FIN

            //conn = Proveedor.getConnection();

            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

            cstmt.setLong(1, lOrderId);
            cstmt.setInt(2, iObjectType);
            cstmt.setLong(3, lCustomerId);
            cstmt.setLong(4, lSiteId);
            cstmt.setString(5, strName);
            cstmt.setString(6, objCutomerBean.getSwMainPhoneArea());
            cstmt.setString(7, objCutomerBean.getSwMainPhone());
            cstmt.setString(8, objCutomerBean.getNpPhone2areacode());
            cstmt.setString(9, objCutomerBean.getSwPhone2());
            cstmt.setString(10, objCutomerBean.getNpPhone3areacode());
            cstmt.setString(11, objCutomerBean.getSwPhone3());
            cstmt.setString(12, objCutomerBean.getSwMainFaxArea());
            cstmt.setString(13, objCutomerBean.getSwMainFax());
            cstmt.setString(14, strNameNew);
            cstmt.setString(15, objCutomerBeanNew.getSwMainPhoneArea());
            cstmt.setString(16, objCutomerBeanNew.getSwMainPhone());
            cstmt.setString(17, objCutomerBeanNew.getNpPhone2areacode());
            cstmt.setString(18, objCutomerBeanNew.getSwPhone2());
            cstmt.setString(19, objCutomerBeanNew.getNpPhone3areacode());
            cstmt.setString(20, objCutomerBeanNew.getSwPhone3());
            cstmt.setString(21, objCutomerBeanNew.getSwMainFaxArea());
            cstmt.setString(22, objCutomerBeanNew.getSwMainFax());
            //Direccion
            cstmt.setLong(23, objAddressBean.getSwregionid());
            cstmt.setString(24, objAddressBean.getSwaddress1());
            cstmt.setString(25, objAddressBean.getSwaddress2());
            cstmt.setString(26, objAddressBean.getSwaddress3());
            cstmt.setString(27, objAddressBean.getSwaddress4());
            cstmt.setString(28, objAddressBean.getSwstate());
            cstmt.setString(29, objAddressBean.getSwprovince());
            cstmt.setString(30, objAddressBean.getSwcity());
            cstmt.setString(31, objAddressBean.getSwzip());
            cstmt.setInt(32, objAddressBean.getNpdepartamentoid());
            cstmt.setInt(33, objAddressBean.getNpprovinciaid());
            cstmt.setInt(34, objAddressBean.getNpdistritoid());

            //SAR 0037-167824 INI

            cstmt.setString(35, objAddressBean.getSwnote());

            cstmt.setLong(36, objAddressBeanNew.getSwregionid());
            cstmt.setString(37, objAddressBeanNew.getSwaddress1());
            cstmt.setString(38, objAddressBeanNew.getSwaddress2());
            cstmt.setString(39, objAddressBeanNew.getSwaddress3());
            cstmt.setString(40, objAddressBeanNew.getSwaddress4());
            cstmt.setString(41, objAddressBeanNew.getSwstate());
            cstmt.setString(42, objAddressBeanNew.getSwprovince());
            cstmt.setString(43, objAddressBeanNew.getSwcity());
            cstmt.setString(44, objAddressBeanNew.getSwzip());
            cstmt.setInt(45, objAddressBeanNew.getNpdepartamentoid());
            cstmt.setInt(46, objAddressBeanNew.getNpprovinciaid());
            cstmt.setInt(47, objAddressBeanNew.getNpdistritoid());

            cstmt.setString(48, objAddressBeanNew.getSwnote());

            //Contacto
            cstmt.setString(49, objContactBean.getSwtitle());
            cstmt.setString(50, objContactBean.getSwfirstname());
            cstmt.setString(51, objContactBean.getSwlastname());
            cstmt.setString(52, objContactBean.getSwmiddlename());
            cstmt.setString(53, objContactBean.getSwjobtitle());
            cstmt.setString(54, objContactBean.getSwemailaddress());
            cstmt.setString(55, objContactBeanNew.getSwtitle());
            cstmt.setString(56, objContactBeanNew.getSwfirstname());
            cstmt.setString(57, objContactBeanNew.getSwlastname());
            cstmt.setString(58, objContactBeanNew.getSwmiddlename());
            cstmt.setString(59, objContactBeanNew.getSwjobtitle());
            cstmt.setString(60, objContactBeanNew.getSwemailaddress());
            cstmt.registerOutParameter(61, Types.VARCHAR);

            int i= cstmt.executeUpdate();
            strMessage = cstmt.getString(61);    //SAR 0037-167824  FIN
        }catch (Exception e) {
            logger.error(formatException(e));
            strMessage = e.getMessage();
        }
        finally{
            try{
                closeObjectsDatabase(null, cstmt, null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        return strMessage;
    }


    /**
     * Motivo: Inserta los cambios que se solicitan en los datos del cliente, site y billing account.
     * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
     * <br>Fecha: 05/11/2007
     * @param     lOrderId
     * @param     iObjectType
     * @param     lObjectId
     * @param     strNameNew
     * @param     objCutomerBeanNew
     * @param     objAddressBeanNew
     * @param     objContactBeanNew
     * @return    String
     */
    public   String updChangeCustomer(long lOrderId, int iObjectType, long lCustomerId, long lSiteId,
                                      String strNameNew, CustomerBean objCutomerBeanNew,
                                      AddressObjectBean objAddressBeanNew,ContactObjectBean objContactBeanNew,
                                      java.sql.Connection conn
    )
            throws SQLException {
        String strMessage=null;
        //Connection conn = null;
        OracleCallableStatement cstmt = null;
        try{

            // SAR 0037-167824 INI
	 /*String sqlStr = "BEGIN WEBSALES.SPI_UPDATE_CHANGE_CUSTOMER(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                                                                 "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +  
                                                                 "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); END;";
	*/

            String sqlStr = "BEGIN WEBSALES.SPI_UPDATE_CHANGE_CUSTOMER(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); END;";

            // SAR 0037-167824 FIN

            //conn = Proveedor.getConnection();

            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

            cstmt.setLong(1, lOrderId);
            cstmt.setInt(2, iObjectType);
            cstmt.setLong(3, lCustomerId);
            cstmt.setLong(4, lSiteId);
            cstmt.setString(5, strNameNew);
            cstmt.setString(6, objCutomerBeanNew.getSwMainPhoneArea());
            cstmt.setString(7, objCutomerBeanNew.getSwMainPhone());
            cstmt.setString(8, objCutomerBeanNew.getNpPhone2areacode());
            cstmt.setString(9, objCutomerBeanNew.getSwPhone2());
            cstmt.setString(10, objCutomerBeanNew.getNpPhone3areacode());
            cstmt.setString(11, objCutomerBeanNew.getSwPhone3());
            cstmt.setString(12, objCutomerBeanNew.getSwMainFaxArea());
            cstmt.setString(13, objCutomerBeanNew.getSwMainFax());
            //Direccion
            cstmt.setLong(14, objAddressBeanNew.getSwregionid());
            cstmt.setString(15, objAddressBeanNew.getSwaddress1());
            cstmt.setString(16, objAddressBeanNew.getSwaddress2());
            cstmt.setString(17, objAddressBeanNew.getSwaddress3());
            cstmt.setString(18, objAddressBeanNew.getSwaddress4());
            cstmt.setString(19, objAddressBeanNew.getSwstate());
            cstmt.setString(20, objAddressBeanNew.getSwprovince());
            cstmt.setString(21, objAddressBeanNew.getSwcity());
            cstmt.setString(22, objAddressBeanNew.getSwzip());
            cstmt.setInt(23, objAddressBeanNew.getNpdepartamentoid());
            cstmt.setInt(24, objAddressBeanNew.getNpprovinciaid());
            cstmt.setInt(25, objAddressBeanNew.getNpdistritoid());

            // SAR 0037-167824 INI
            cstmt.setString(26, objAddressBeanNew.getSwnote());

            //Contacto

            cstmt.setString(27, objContactBeanNew.getSwtitle());
            cstmt.setString(28, objContactBeanNew.getSwfirstname());
            cstmt.setString(29, objContactBeanNew.getSwlastname());
            cstmt.setString(30, objContactBeanNew.getSwmiddlename());
            cstmt.setString(31, objContactBeanNew.getSwjobtitle());
            cstmt.setString(32, objContactBeanNew.getSwemailaddress());
            cstmt.registerOutParameter(33, Types.VARCHAR);


            int i=cstmt.executeUpdate();
            strMessage = cstmt.getString(33);   // SAR 0037-167824 FIN
        }catch (Exception e) {
            logger.error(formatException(e));
            strMessage = e.getMessage();
        }
        finally{
            try{
                closeObjectsDatabase(null, cstmt, null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        return strMessage;
    }

    /**
     * Motivo: Obteniene el detalle del contacto de un tipo especifico (por ejemplo: 'Facturacion' ) del customer o site
     * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
     * <br>Fecha: 05/11/2007
     * @param     lObjectTypeId
     * @param     strObjectType
     * @param     strType
     * @return    HashMap
     */
    public  HashMap getContact(long lObjectTypeId, String strObjectType,String strType) throws SQLException {

        ContactObjectBean objContactBean=new ContactObjectBean();
        String strMessage=null;
        HashMap hshData=new HashMap();
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        Connection conn=null;
        try{
            String sqlStr =  "BEGIN WEBSALES.SPI_GET_CONTACT_DETAIL2(?, ?, ?, ?, ?); END;";
            conn = Proveedor.getConnection();

            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, lObjectTypeId);
            cstmt.setString(2, strObjectType);
            cstmt.setString(3, strType);
            cstmt.registerOutParameter(4, OracleTypes.CURSOR);
            cstmt.registerOutParameter(5, Types.VARCHAR);

            cstmt.execute();

            strMessage = cstmt.getString(5);
            if ( strMessage == null  ){
                rs = (ResultSet)cstmt.getObject(4);

                if (rs.next()) {
                    objContactBean.setSwtitle(rs.getString("swtitle"));
                    objContactBean.setSwfirstname(rs.getString("swfirstname"));
                    objContactBean.setSwlastname(rs.getString("swlastname"));
                    objContactBean.setSwmiddlename(rs.getString("swmiddlename"));
                    objContactBean.setSwjobtitle(rs.getString("swjobtitle"));
                    objContactBean.setSwjobtitleid(rs.getLong("swjobtitleid"));
                    objContactBean.setSwemailaddress(rs.getString("swemailaddress"));
                }
            }
            hshData.put("objContactBean",objContactBean);
            hshData.put("strMessage",strMessage);

        }catch (Exception e) {
            logger.error(formatException(e));
            hshData.put("strMessage",e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        return hshData;
    }


    /**
     * Motivo: Obteniene el detalle de la direccion de un tipo especifico (por ejemplo: 'Facturacion' ) del customer o site
     * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
     * <br>Fecha: 05/11/2007
     * @param     lObjectTypeId
     * @param     strObjectType
     * @param     strType
     * @return    HashMap
     */
    public  HashMap getAddress(long lObjectTypeId, String strObjectType,String strType) throws SQLException {

        AddressObjectBean objAddressBean=new AddressObjectBean();
        String strMessage=null;
        HashMap hshData=new HashMap();
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        Connection conn=null;
        try{
            String sqlStr =  "BEGIN WEBSALES.SPI_GET_ADDRESS_DETAIL(?, ?, ?, ?, ?); END;";
            conn = Proveedor.getConnection();

            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, lObjectTypeId);
            cstmt.setString(2, strObjectType);
            cstmt.setString(3, strType);
            cstmt.registerOutParameter(4, OracleTypes.CURSOR);
            cstmt.registerOutParameter(5, Types.VARCHAR);

            cstmt.execute();

            strMessage = cstmt.getString(5);
            if (strMessage == null){
                rs = (ResultSet)cstmt.getObject(4);
                if (rs.next()) {
                    objAddressBean.setSwregionid(rs.getLong("swregionid"));
                    objAddressBean.setSwregionname(rs.getString("swname"));
                    objAddressBean.setSwaddress1(rs.getString("swaddress1"));
                    objAddressBean.setSwaddress2(rs.getString("swaddress2"));
                    objAddressBean.setSwaddress3(rs.getString("swaddress3"));
                    objAddressBean.setSwaddress4(rs.getString("swaddress4"));
                    objAddressBean.setSwzip(rs.getString("swzip"));
                    objAddressBean.setNpdepartamentoid(rs.getInt("npdepartamentoid"));
                    objAddressBean.setNpprovinciaid(rs.getInt("npprovinciaid"));
                    objAddressBean.setNpdistritoid(rs.getInt("npdistritoid"));
                    objAddressBean.setSwstate(rs.getString("swstate"));
                    objAddressBean.setSwprovince(rs.getString("swprovince"));
                    objAddressBean.setSwcity(rs.getString("swcity"));

                    // SAR 0037-167824 INI
                    objAddressBean.setSwnote(rs.getString("swnote"));
                    // SAR 0037-167824 FIN

                }

            }
            hshData.put("objAddressBean",objAddressBean);
            hshData.put("strMessage",strMessage);
        }catch (Exception e) {
            logger.error(formatException(e));
            hshData.put("strMessage",e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }


        return hshData;
    }

    /**
     * Motivo: Obteniene un registro de la tabla que almacena datos que se piden modificar de una dirección del customer, site o billing account
     * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
     * <br>Fecha: 05/11/2007
     * @param     lOrderId
     * @param     iTypeObject     Ejm: 1=CUSTOMER 2=SITE
     * @param     lObjectId
     * @return    HashMap
     */
    public  HashMap getAddressChange(long lOrderId, int iTypeObject,long lObjectId) throws SQLException {

        AddressObjectBean objAddressBean=new AddressObjectBean();
        AddressObjectBean objAddressBeanNew=new AddressObjectBean();
        String strMessage=null;
        HashMap hshData=new HashMap();
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        Connection conn=null;
        try{
            String sqlStr =  "BEGIN WEBSALES.SPI_GET_ADDRESS_CHANGE(?, ?, ?, ?, ?); END;";
            conn = Proveedor.getConnection();

            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, lOrderId);
            cstmt.setInt(2, iTypeObject);
            cstmt.setLong(3, lObjectId);
            cstmt.registerOutParameter(4, OracleTypes.CURSOR);
            cstmt.registerOutParameter(5, Types.VARCHAR);

            cstmt.execute();

            strMessage = cstmt.getString(5);
            if ( strMessage == null  ){
                rs = (ResultSet)cstmt.getObject(4);
                if (rs.next()) {
                    //objAddressBean.setSwregionid(rs.getLong("swregionid"));
                    //objAddressBean.setSwregionname(rs.getString("swname"));

                    //SAR 0037-167824 INI

                    objAddressBeanNew.setSwnote(rs.getString("npswnotenew"));
                    objAddressBean.setSwnote(rs.getString("npswnote"));

                    //SAR 0037-167824 FIN

                    objAddressBean.setSwregionid(rs.getLong("npregionid"));
                    objAddressBean.setSwaddress1(rs.getString("npaddress1"));
                    objAddressBean.setSwaddress2(rs.getString("npaddress2"));
                    objAddressBean.setSwaddress3(rs.getString("npaddress3"));
                    objAddressBean.setSwaddress4(rs.getString("npaddress4"));
                    objAddressBean.setSwzip(rs.getString("npzip"));
                    objAddressBean.setNpdepartamentoid(rs.getInt("npstateid"));
                    objAddressBean.setNpprovinciaid(rs.getInt("npprovinceid"));
                    objAddressBean.setNpdistritoid(rs.getInt("npcityid"));
                    objAddressBean.setSwstate(rs.getString("npstate"));
                    objAddressBean.setSwprovince(rs.getString("npprovince"));
                    objAddressBean.setSwcity(rs.getString("npcity"));

                    objAddressBeanNew.setSwregionid(rs.getLong("npregionidnew"));
                    objAddressBeanNew.setSwaddress1(rs.getString("npaddress1new"));
                    objAddressBeanNew.setSwaddress2(rs.getString("npaddress2new"));
                    objAddressBeanNew.setSwaddress3(rs.getString("npaddress3new"));
                    objAddressBeanNew.setSwaddress4(rs.getString("npaddress4new"));
                    objAddressBeanNew.setSwzip(rs.getString("npzipnew"));
                    objAddressBeanNew.setNpdepartamentoid(rs.getInt("npstateidnew"));
                    objAddressBeanNew.setNpprovinciaid(rs.getInt("npprovinceidnew"));
                    objAddressBeanNew.setNpdistritoid(rs.getInt("npcityidnew"));
                    objAddressBeanNew.setSwstate(rs.getString("npstatenew"));
                    objAddressBeanNew.setSwprovince(rs.getString("npprovincenew"));
                    objAddressBeanNew.setSwcity(rs.getString("npcitynew"));
                }
            }

            hshData.put("objAddressBean",objAddressBean);
            hshData.put("objAddressBeanNew",objAddressBeanNew);
            hshData.put("strMessage",strMessage);
        }catch (Exception e) {
            logger.error(formatException(e));
            hshData.put("strMessage",e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return hshData;
    }

    /**
     * Motivo: Obteniene un registro de la tabla que almacena datos que se piden modificar de un contacto del customer, site o billing account
     * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
     * <br>Fecha: 05/11/2007
     * @param     lOrderId
     * @param     iTypeObject     Ejm: 1=CUSTOMER 2=SITE
     * @param     lObjectId
     * @return    HashMap
     */
    public  HashMap getContactChange(long lOrderId, int iTypeObject,long lObjectId) throws SQLException {

        ContactObjectBean objContactBean=new ContactObjectBean();
        ContactObjectBean objContactBeanNew=new ContactObjectBean();
        String strMessage=null;
        HashMap hshData=new HashMap();
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        Connection conn=null;

        try{
            String sqlStr =  "BEGIN WEBSALES.SPI_GET_CONTACT_CHANGE(?, ?, ?, ?, ?); END;";
            conn = Proveedor.getConnection();

            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, lOrderId);
            cstmt.setInt(2, iTypeObject);
            cstmt.setLong(3, lObjectId);
            cstmt.registerOutParameter(4, OracleTypes.CURSOR);
            cstmt.registerOutParameter(5, Types.VARCHAR);

            cstmt.execute();
            strMessage = cstmt.getString(5);
            if ( strMessage==null ){
                rs = (ResultSet)cstmt.getObject(4);

                if (rs.next()) {
                    objContactBean.setSwtitle(rs.getString("nptitle"));
                    objContactBean.setSwfirstname(rs.getString("npfirstname"));
                    objContactBean.setSwlastname(rs.getString("nplastname"));
                    objContactBean.setSwmiddlename(rs.getString("npmiddlename"));
                    objContactBean.setSwjobtitle(rs.getString("npjobtitle"));
                    objContactBean.setSwemailaddress(rs.getString("npemail"));

                    objContactBeanNew.setSwtitle(rs.getString("nptitlenew"));
                    objContactBeanNew.setSwfirstname(rs.getString("npfirstnamenew"));
                    objContactBeanNew.setSwlastname(rs.getString("nplastnamenew"));
                    objContactBeanNew.setSwmiddlename(rs.getString("npmiddlenamenew"));
                    objContactBeanNew.setSwjobtitle(rs.getString("npjobtitlenew"));
                    objContactBeanNew.setSwemailaddress(rs.getString("npemailnew"));
                }
            }


            hshData.put("objContactBean",objContactBean);
            hshData.put("objContactBeanNew",objContactBeanNew);
            hshData.put("strMessage",strMessage);
        }catch (Exception e) {
            logger.error(formatException(e));
            hshData.put("strMessage",e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }


        return hshData;
    }

    /**
     * Motivo: Obteniene un registro de la tabla que almacena datos que se piden modificar del customer, site o billing account
     * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
     * <br>Fecha: 05/11/2007
     * @param     lOrderId
     * @param     iTypeObject     Ejm: 1=CUSTOMER 2=SITE
     * @param     lObjectId
     * @return    HashMap
     */
    public  HashMap getHeaderChange(long lOrderId, int iTypeObject,long lObjectId) throws SQLException {

        CustomerBean objCustomerBean=new CustomerBean();
        CustomerBean objCustomerBeanNew=new CustomerBean();
        String strMessage=null;
        String strName=null;
        String strNameNew=null;
        int iType=0;
        HashMap hshData=new HashMap();
        HashMap hshHeader=null;
        ArrayList arrHeader=new ArrayList();
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        Connection conn=null;

        try{
            String sqlStr =  "BEGIN WEBSALES.SPI_GET_HEADER_CHANGE(?, ?, ?, ?, ?); END;";
            conn = Proveedor.getConnection();

            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, lOrderId);
            cstmt.setInt(2, iTypeObject);
            cstmt.setLong(3, lObjectId);
            cstmt.registerOutParameter(4, OracleTypes.CURSOR);
            cstmt.registerOutParameter(5, Types.VARCHAR);

            cstmt.execute();

            strMessage = cstmt.getString(5);

            if ( strMessage == null){
                rs = (ResultSet)cstmt.getObject(4);
                while(rs.next()) {
                    hshHeader=new HashMap();
                    objCustomerBean=new CustomerBean();
                    objCustomerBeanNew=new CustomerBean();

                    strName=rs.getString("npname");
                    iType=rs.getInt("npobjecttype");
                    objCustomerBean.setSwCustomerId(rs.getLong("npobjectid"));
                    objCustomerBean.setSwMainPhoneArea(rs.getString("area1"));
                    objCustomerBean.setSwMainPhone(rs.getString("npphone1"));
                    objCustomerBean.setNpPhone2areacode(rs.getString("area2"));
                    objCustomerBean.setSwPhone2(rs.getString("npphone2"));
                    objCustomerBean.setNpPhone3areacode(rs.getString("area3"));
                    objCustomerBean.setSwPhone3(rs.getString("npphone3"));
                    objCustomerBean.setSwMainFaxArea(rs.getString("npfaxarea"));
                    objCustomerBean.setSwMainFax(rs.getString("npfaxnumber"));
                    objCustomerBean.setDepMainphonearea(rs.getString("depphone1"));
                    objCustomerBean.setDepPhone2areacode(rs.getString("depphone2"));
                    objCustomerBean.setDepPhone3areacode(rs.getString("depphone3"));
                    objCustomerBean.setDepMainFaxArea(rs.getString("depfax"));

                    strNameNew=rs.getString("npnamenew");
                    objCustomerBeanNew.setSwMainPhoneArea(rs.getString("area1new"));
                    objCustomerBeanNew.setSwMainPhone(rs.getString("npphone1new"));
                    objCustomerBeanNew.setNpPhone2areacode(rs.getString("area2new"));
                    objCustomerBeanNew.setSwPhone2(rs.getString("npphone2new"));
                    objCustomerBeanNew.setNpPhone3areacode(rs.getString("area3new"));
                    objCustomerBeanNew.setSwPhone3(rs.getString("npphone3new"));
                    objCustomerBeanNew.setSwMainFaxArea(rs.getString("npfaxareanew"));
                    objCustomerBeanNew.setSwMainFax(rs.getString("npfaxnumbernew"));
                    objCustomerBeanNew.setDepMainphonearea(rs.getString("depphone1new"));
                    objCustomerBeanNew.setDepPhone2areacode(rs.getString("depphone2new"));
                    objCustomerBeanNew.setDepPhone3areacode(rs.getString("depphone3new"));
                    objCustomerBeanNew.setDepMainFaxArea(rs.getString("depfaxnew"));

                    hshHeader.put("strType",iType+"");
                    hshHeader.put("objCustomerBean",objCustomerBean);
                    hshHeader.put("strName",strName);
                    hshHeader.put("objCustomerBeanNew",objCustomerBeanNew);
                    hshHeader.put("strNameNew",strNameNew);
                    arrHeader.add(hshHeader);
                }
            }

            hshData.put("arrHeader",arrHeader);
            hshData.put("strMessage",strMessage);
        }catch (Exception e) {
            logger.error(formatException(e));
            hshData.put("strMessage",e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        return hshData;
    }

/***********************************************************************
 ***********************************************************************
 ***********************************************************************
 *  INTEGRACION DE ORDENES Y RETAIL - FIN
 *  REALIZADO POR: Carmen Gremios
 *  FECHA: 13/09/2007
 ***********************************************************************
 ***********************************************************************
 ***********************************************************************/

    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - INICIO
     *  REALIZADO POR: Richard De los Reyes
     *  FECHA: 28/08/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/

    /**
     * Motivo: Valida al cliente y/o determina si es Customer o Prospect.
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 19/08/2007
     *
     * @param      tipoDocumento   Ej: RUC, DNI, L.E.
     * @param      nroDocumento    Ej: 20421814041
     * @return     CustomerBean que servirá para validar si el Cliente existe o no 
     */
    public CustomerBean getValidateCustomer(String tipoDocumento, String nroDocumento) {
        if (logger.isDebugEnabled())
            logger.debug("--Inicio--");
        CustomerBean customer = new CustomerBean();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        String strError = null;
        String sqlStr = "BEGIN WEBSALES.NP_CUSTOMER_DATA_PKG.SP_VALIDATE_CUSTOMER(?, ?, ?, ?, ?, ?, ?); END;";
        try {
            int i = 1;
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setString(i++, tipoDocumento);
            cstmt.setString(i++, nroDocumento);
            cstmt.registerOutParameter(i++, Types.VARCHAR);
            cstmt.registerOutParameter(i++, Types.VARCHAR);
            cstmt.registerOutParameter(i++, Types.NUMERIC);
            cstmt.registerOutParameter(i++, Types.NUMERIC);
            cstmt.registerOutParameter(i++, Types.VARCHAR);
            cstmt.executeUpdate();
            i = 3;
            customer.setSwTipoPerson(StringUtils.defaultString(cstmt.getString(i++)));
            customer.setSwType(StringUtils.defaultString(cstmt.getString(i++)));
            customer.setSwCustomerId(cstmt.getInt(i++));
            customer.setStatus(cstmt.getInt(i++));
            customer.setStrMessage(StringUtils.defaultString(cstmt.getString(i++)));
            if (logger.isDebugEnabled())
                logger.debug(customer);
        } catch (SQLException e) {
            logger.error(formatException(e));
            strError = formatException(e);
        } finally {
            try {
                closeObjectsDatabase(conn, cstmt, null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
            /*if (cstmt != null)
                try {
                     closeObjectsDatabase(conn, cstmt, null);  
                } catch (SQLException sqle) {
                    logger.error(formatException(sqle));
                }
				*/
        }
        return customer;
    }

    /**
     * Motivo: Obtiene la Lista de Cuentas por Cliente.
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 19/08/2007
     *
     * @param     customerId  Id del Cliente
     * @return    ArrayList de SiteBean (Cuentas)
     */
    public ArrayList getSitesByCustomer(long customerId) {
        if (logger.isDebugEnabled())
            logger.debug("--Inicio--");
        ArrayList list = new ArrayList();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strError = null;
        String sqlStr = "BEGIN WEBSALES.NP_CUSTOMER_DATA_PKG.SP_GET_CUSTOMER_SITE_LIST(?, ?, ?); END;";
        try {
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setLong(1, customerId);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, Types.VARCHAR);
            cstmt.executeUpdate();

            strError = cstmt.getString(3);
            if (StringUtils.isNotEmpty(strError))
                throw new SQLException();
            rs = (ResultSet)cstmt.getObject(2);
            while (rs.next()) {
                SiteBean site = new SiteBean();
                int i = 1;
                site.setSwSiteName(StringUtils.defaultString(rs.getString(i++), ""));
                site.setNpCodBscs(StringUtils.defaultString(rs.getString(i++), ""));
                list.add(site);
            }

        } catch (SQLException e) {
            logger.error(formatException(e));
            strError = e.getMessage();
        } finally {
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }
            catch (Exception e) {
                logger.error(formatException(e));
            }
            /*if (cstmt != null)
                try {
                    rs.close();
                    cstmt.close();
                    conn.close();
                } catch (SQLException sqle) {
                    logger.error(formatException(sqle));
                }*/
        }
        return list;
    }

    /**
     * Motivo: Obtiene la Información del Cliente.
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 26/08/2007
     * @param     customerId  Id del Cliente
     * @return    CustomerBean con la Información del Cliente
     */
    public CustomerBean getCustomerInfo(long customerId) throws Exception,SQLException{
        if (logger.isDebugEnabled())
            logger.debug("--Inicio--");
        CustomerBean customer = new CustomerBean();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strError = null;
        System.out.println("-------------------------------------------------customerId"+customerId);
        try{
            String sqlStr = "BEGIN WEBSALES.NP_CUSTOMER_DATA_PKG.SP_CUSTOMER_DET(?, ?, ?); END;";

            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setLong(1, customerId);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, Types.VARCHAR);
            cstmt.executeUpdate();
            strError = cstmt.getString(3);
            if (StringUtils.isNotEmpty(strError)){
                System.out.println("[getCustomerInfo][error:]"+strError);
                customer.setStrMessage(strError);
            }
            else {
                rs = (ResultSet)cstmt.getObject(2);
                if (rs.next()) {
                    int i = 1;
                    customer.setTituloPersona(StringUtils.defaultString(rs.getString(i++), ""));
                    customer.setNpFirstName(StringUtils.defaultString(rs.getString(i++), ""));
                    customer.setNpLastName(StringUtils.defaultString(rs.getString(i++), ""));
                    customer.setSwCustomerId(rs.getLong(i++));
                    customer.setNpTipoDoc(StringUtils.defaultString(rs.getString(i++), ""));
                    customer.setSwRuc(StringUtils.defaultString(rs.getString(i++), ""));
                    customer.setSwCodBscs(StringUtils.defaultString(rs.getString(i++), ""));
                    customer.setSwName(StringUtils.defaultString(rs.getString(i++), ""));
                    customer.setSwMainPhone(StringUtils.defaultString(rs.getString(i++), ""));
                    customer.setNpGiroId(rs.getLong(i++));
                    customer.setSwIndustryId(rs.getLong(i++));
                    customer.setSwTipoPerson(StringUtils.defaultString(rs.getString(i++), ""));
                    customer.setSwType(StringUtils.defaultString(rs.getString(i++), ""));
                }
            }
        }catch (Exception e) {
            logger.error(formatException(e));
            customer.setStrMessage(e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return customer;
    }

    /**
     * Motivo: Validar si existe una orden en proceso para Prospect
     * <br>Realizado por: <a href="mailto:juan.oyola@nextel.com.pe">Juan Oyola</a>
     * <br>Fecha: 23/07/2008
     * @param     customerId  Id del Cliente
     * @return    String
     */
    public HashMap getExisteOrdenProspect(long customerId) throws Exception,SQLException{
        HashMap objHashMap = new HashMap();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        String strError = null;
        try{
            String sqlStr = "BEGIN WEBSALES.SPI_EXIST_ORDER_FOR_PROSPECT(?, ?); END;";

            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setLong(1, customerId);
            cstmt.registerOutParameter(2, Types.VARCHAR);
            cstmt.executeUpdate();
            strError = cstmt.getString(2);
            objHashMap.put("strMessage",strError);
        }catch (Exception e) {
            logger.error(formatException(e));
            objHashMap.put("strMessage",e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return objHashMap;
    }

    /**
     * Motivo: Obtiene las Direcciones de un determinado Cliente.
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 26/08/2007
     * @param     customerId  Id del Cliente
     * @return    ArrayList de String (Dirección)
     */
    public HashMap getAddressesByCustomer(long customerId) throws Exception,SQLException{
        if (logger.isDebugEnabled())
            logger.debug("--Inicio--");
        HashMap objHashMap = new HashMap();
        ArrayList list = new ArrayList();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        AddressObjectBean objAddress;
        ResultSet rs = null;
        String strError = null;
        try{
            String sqlStr = "BEGIN WEBSALES.NP_CUSTOMER_DATA_PKG.SP_ADDRESSES_CUSTOMER_LST(?, ?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstmt.setLong(1, customerId);
            cstmt.registerOutParameter(2, -10);
            cstmt.registerOutParameter(3, 12);
            cstmt.executeUpdate();
            strError = cstmt.getString(3);
            if( strError==null){
                rs = (ResultSet)cstmt.getObject(2);
                while (rs.next()) {
                    objAddress = new AddressObjectBean();
                    objAddress.setSwaddress1(rs.getString("address"));
                    objAddress.setSwstate(rs.getString("depart"));
                    objAddress.setSwprovince(rs.getString("provincia"));
                    objAddress.setSwcountry(rs.getString("distrito"));
                    objAddress.setSwdepid(rs.getString("dep"));
                    objAddress.setNpprovinciaid(rs.getInt("prov"));
                    objAddress.setNpdistritoid(rs.getInt("dist"));
                    list.add(objAddress);
                }
            }
            objHashMap.put("strMessage",strError);
            objHashMap.put("objArrayList",list);
        }catch (Exception e) {
            logger.error(formatException(e));
            objHashMap.put("strMessage",e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        return objHashMap;
    }

    /**
     * Motivo: Obtiene las Direcciones de un determinado Cliente.
     * <br>Realizado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
     * <br>Fecha: 26/08/2007
     * @return    ArrayList de String (Dirección)
     */
    public ArrayList getManageTypesOfRecords(RetailForm retailForm) throws IllegalAccessException,
            InvocationTargetException,
            NoSuchMethodException {
        if (logger.isDebugEnabled())
            logger.debug("--Inicio--");
        ArrayList list = new ArrayList();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        String strError = null;
        //DbmsOutput dbmsOutput = null;
        String sqlStr = "BEGIN ORDERS.NP_RETAIL01_PKG.PL_APPLICATION_RESULT2(?, ?, ?); END;";
        try {
            conn = Proveedor.getConnection();
            //dbmsOutput = new DbmsOutput(conn);
            //dbmsOutput.enable(10000000);
            cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
            List orderDetailList = new ArrayList();
            StructDescriptor sdOrderDetail = StructDescriptor.createDescriptor("ORDERS.TR_ORDER_DETAIL", conn);
            ArrayDescriptor adOrderDetailList = ArrayDescriptor.createDescriptor("ORDERS.TT_ORDER_DETAIL_LST", conn);
            StructDescriptor sdOrder = StructDescriptor.createDescriptor("ORDERS.TR_ORDER", conn);
            for (int i = 0; i < retailForm.getTxtIMEI().length; i++) {
                Object objOrderRetail[] = { retailForm.getTxtIMEI()[i],
                        retailForm.getTxtEquipo()[i],
                        retailForm.getTxtServicio()[i],
                        retailForm.getTxtPlanTarifario()[i],
                        retailForm.getCmbKit()[i],
                        retailForm.getHdnKit()[i],
                        retailForm.getTxtContrato()[i],
                        retailForm.getTxtNextel()[i],
                        retailForm.getTxtModelo()[i],
                        retailForm.getTxtSIM()[i],
                        retailForm.getTxtNroVoucher()[i] };
                STRUCT sOrderRetail = new STRUCT(sdOrderDetail, conn, objOrderRetail);
                orderDetailList.add(sOrderRetail);
            }

            ARRAY aOrderRetail = new ARRAY(adOrderDetailList, conn, ((Object) (orderDetailList.toArray())));
            Object objOrder[] = { retailForm.getCmbTipoDocumento(),
                    retailForm.getTxtNroDocumento(),
                    aOrderRetail };
            STRUCT sOrder = new STRUCT(sdOrder, conn, objOrder);
            cstmt.setSTRUCT(1, sOrder);
            //cstmt.setObject(1, (STRUCT)sOrder);
            cstmt.registerOutParameter(2, 2003, "ORDERS.TT_ORDER_DETAIL_LST");
            cstmt.execute();
            //dbmsOutput.show();
            //dbmsOutput.close();
        } catch (SQLException e) {
            logger.error(formatException(e));
            strError = e.getMessage();
        } finally {
            try {
                closeObjectsDatabase(conn, cstmt, null);
            } catch (SQLException sqle) {
                logger.error(formatException(sqle));
            }
        }
        return list;
    }



    /***********************************************************************
     ***********************************************************************
     ***********************************************************************
     *  INTEGRACION DE ORDENES Y RETAIL - FIN
     *  REALIZADO POR: Richard De los Reyes
     *  FECHA: 28/08/2007
     ***********************************************************************
     ***********************************************************************
     ***********************************************************************/




    /**
     * Motivo: Obtiene el CustomerID del BSCS en base al CustomerID de CRM
     * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
     * <br>Fecha: 05/11/2007
     * @return    int (CSID)
     */
    public static long getCustomerIdBSCS(long an_objectid, String av_objecttype)   {


        Connection conn = null;
        OracleCallableStatement cstmt = null;
        long intResult= 0;

        //NUMBER nmbjectid = new NUMBER(an_objectid);
        String strSql = " { ? = call WEBSALES.FXI_GET_OBJECTID_BSCS_BY_CRM( ?, ?,?) } ";
        System.out.println("an_objectid: "+an_objectid+"");
        System.out.println("av_objecttype: "+av_objecttype);
        try {
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
            cstmt.registerOutParameter(1,OracleTypes.NUMBER);
            cstmt.setLong(2, an_objectid);
            cstmt.setString(3, av_objecttype);
            cstmt.registerOutParameter(4,OracleTypes.NUMBER);

            cstmt.executeUpdate();
            intResult = cstmt.getLong(1);

        } catch (SQLException e) {
            System.out.println("getUserRol nError Nro.: "+ e.getClass() + " " + e.getErrorCode() + " nMensaje:   -- >" + e.getMessage()+"\n");
            System.out.println("e.getErrorCode()  : "+e.getErrorCode() );

        }finally {
            try {
                closeObjectsDatabase(conn, cstmt, null);
            } catch (SQLException sqle) {
                logger.error(formatException(sqle));
                return 0;
            }
        }

        return intResult;
    }


    /**
     * Motivo: Inserta los cambios que se solicitan en los datos del cliente, site y billing account en EDICION
     * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">KAREN SALVADOR</a>
     * <br>Fecha: 16/01/2008
     * @param     lOrderId
     * @param     iObjectType
     * @param     lObjectId
     * @param     strNameNew
     * @param     objCutomerBeanNew
     * @param     objAddressBeanNew
     * @param     objContactBeanNew
     * @return    String
     */
    public   String updChangeCustomerEdit(long lOrderId, int iObjectType, long lCustomerId, long lSiteId,
                                          String strSiteNameNew, CustomerBean objCustomerBeanNewAct,
                                          AddressObjectBean objAddressBeanNewAct,
                                          ContactObjectBean objContactBeanNewAct, String strLogin,
                                          java.sql.Connection conn
    )
            throws SQLException {
        String strMessage=null;
        //Connection conn = null;
        OracleCallableStatement cstmt = null;
        try{
            String sqlStr = "BEGIN WEBSALES.SPI_UPDATE_CUSTOMER_EDIT (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?); END;";                      //SAR 0037-167824

            //conn = Proveedor.getConnection();

            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

            cstmt.setLong(1, lOrderId);
            cstmt.setInt(2, iObjectType);
            cstmt.setLong(3, lCustomerId);
            cstmt.setLong(4, lSiteId);
            cstmt.setString(5, strLogin);

            cstmt.setString(6, strSiteNameNew);
            cstmt.setString(7, objCustomerBeanNewAct.getSwMainPhoneArea());
            cstmt.setString(8, objCustomerBeanNewAct.getSwMainPhone());
            cstmt.setString(9, objCustomerBeanNewAct.getNpPhone2areacode());
            cstmt.setString(10, objCustomerBeanNewAct.getSwPhone2());
            cstmt.setString(11, objCustomerBeanNewAct.getNpPhone3areacode());
            cstmt.setString(12, objCustomerBeanNewAct.getSwPhone3());
            cstmt.setString(13, objCustomerBeanNewAct.getSwMainFaxArea());
            cstmt.setString(14, objCustomerBeanNewAct.getSwMainFax());
            //Direccion
            cstmt.setLong(15, objAddressBeanNewAct.getSwregionid());
            cstmt.setString(16, objAddressBeanNewAct.getSwaddress1());
            cstmt.setString(17, objAddressBeanNewAct.getSwaddress2());
            cstmt.setString(18, objAddressBeanNewAct.getSwaddress3());
            cstmt.setString(19, objAddressBeanNewAct.getSwaddress4());
            cstmt.setString(20, objAddressBeanNewAct.getSwstate());
            cstmt.setString(21, objAddressBeanNewAct.getSwprovince());
            cstmt.setString(22, objAddressBeanNewAct.getSwcity());
            cstmt.setString(23, objAddressBeanNewAct.getSwzip());
            cstmt.setInt(24, objAddressBeanNewAct.getNpdepartamentoid());
            cstmt.setInt(25, objAddressBeanNewAct.getNpprovinciaid());
            cstmt.setInt(26, objAddressBeanNewAct.getNpdistritoid());


            // SAR 0037-167824 INI
            cstmt.setString(27, objAddressBeanNewAct.getSwnote());
            //Contacto
            cstmt.setString(28, objContactBeanNewAct.getSwtitle());
            cstmt.setString(29, objContactBeanNewAct.getSwfirstname());
            cstmt.setString(30, objContactBeanNewAct.getSwlastname());
            cstmt.setString(31, objContactBeanNewAct.getSwmiddlename());
            cstmt.setString(32, objContactBeanNewAct.getSwjobtitle());
            cstmt.setLong(33, objContactBeanNewAct.getSwjobtitleid());
            cstmt.setString(34, objContactBeanNewAct.getSwemailaddress());
            cstmt.registerOutParameter(35, Types.VARCHAR);

            int i=cstmt.executeUpdate();
            strMessage = cstmt.getString(35);

            // SAR 0037-167824 FIN

        }catch (Exception e) {
            logger.error(formatException(e));
            strMessage = e.getMessage();
        }
        finally{
            try{
                closeObjectsDatabase(null, cstmt, null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        return strMessage;
    }


    /**
     Method : getInfoCustomer
     Purpose: Obtener información del cliente según el numero de la orden
     Developer       		Fecha       Comentario
     =============   		==========  ======================================================================
     Cristian Espinoza     	05/02/2008  Creación
     */
    public HashMap getInfoCustomer(long lOrderId) throws Exception,SQLException {

        HashMap objHashMap = new HashMap();

        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strMessage = null;
        try{
            String sqlStr = "BEGIN WEBSALES.SPI_GET_CUSTOMER_INFO(?, ?, ?); END;";

            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, lOrderId);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.executeUpdate();
            strMessage = cstmt.getString(3);
            objHashMap.put("strMessage",strMessage);

            if( strMessage == null ) {

                rs = (ResultSet)cstmt.getObject(2);

                if (rs.next()) {
                    objHashMap.put("strCustomerId",rs.getLong(1)+""); //wn_swcustomerid
                    objHashMap.put("strCustCodeBSCS",rs.getString(2)==null?"":rs.getString(2)); //wv_codbscs
                    objHashMap.put("strCustomerIdBSCS",rs.getLong(3)+""); //npbscscustomerid
                }
            }

        }catch (Exception e){
            logger.error(formatException(e));
            objHashMap.put("strMessage",e.getMessage());
        }finally{
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        return objHashMap;
    }

    /**
     * Motivo: Resuelve la validación de semejanza de un DNI con un Ruc o viceversa
     * <br>Realizado por: <a href="mailto:oliver.dubock@nextel.com.pe">Odubock</a>
     * <br>Fecha: 14/05/2008
     * @param     long    strNumDoc
     * @param     long    strTipoDoc
     * @return    HashMap objHashMap
     */
    public HashMap getValidateDniRucEquals(String strNumDoc,String strTipoDoc) throws Exception,SQLException{
        HashMap  objHashMap = new HashMap();
        Connection conn = null;
        OracleCallableStatement cstm = null;
        String strMessage,strNumDocResponse,strTipoDocResponse = null;
        try{
            String sqlStr = "BEGIN WEBSALES.NPSL_CUSTOMER_PKG.SP_VALIDATE_DOC_CUSTOMER(?,?,?,?,?); END;";
            conn = Proveedor.getConnection();
            cstm = (OracleCallableStatement) conn.prepareCall(sqlStr);
            cstm.setString(1,strTipoDoc);
            cstm.setString(2,strNumDoc);
            cstm.registerOutParameter(3,OracleTypes.VARCHAR);
            cstm.registerOutParameter(4,OracleTypes.VARCHAR);
            cstm.registerOutParameter(5,OracleTypes.VARCHAR);
            cstm.executeUpdate();

            strNumDocResponse  = cstm.getString(3);
            strTipoDocResponse = cstm.getString(4);
            strMessage         = cstm.getString(5);

            objHashMap.put("strNumDoc",strNumDocResponse);
            objHashMap.put("strTipoDoc",strTipoDocResponse);
            objHashMap.put(Constante.MESSAGE_OUTPUT,strMessage);
        }catch (Exception e){
            logger.error(formatException(e));
            objHashMap.put(Constante.MESSAGE_OUTPUT,e.getMessage());
        }finally{
            try{
                closeObjectsDatabase(conn, cstm, null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }


        return objHashMap;
    }

    /**
     * Motivo: Obteniene el detalle de un cliente dado
     * <br>Realizado por: <a href="mailto:cristian.espinoza@nextel.com.pe">Cristian Espinoza</a>
     * <br>Fecha: 23/07/2007
     * @param     iCustomerId
     * @param     strSwcreatedBy
     * @param     iRegionid
     * @param     lOrderId
     * @param     strMessage       Contiene el mensaje de error si existiera
     * @return    CustomerBean
     */
    public HashMap getCustomerDataDetail(long iCustomerId,String strSwcreatedBy, long iRegionid, long lOrderId )
            throws SQLException, Exception {

        CustomerBean objCustomerBean = null;
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        //DbmsOutput dbmsOutput = null;
        ResultSet rs = null;


        String strMessage=null;
        HashMap hshData=new HashMap();
        String sqlStr = "BEGIN WEBSALES.SPI_GET_CUSTOMER_DETAIL_DATA(?, ?, ?, ?, ?, ?); END;";
        try{
            conn = Proveedor.getConnection();
            //dbmsOutput = new DbmsOutput(conn);
            //dbmsOutput.enable(1000000);
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

            cstmt.setLong(1, iCustomerId);
            cstmt.setString(2, strSwcreatedBy);
            cstmt.setLong(3, iRegionid);
            cstmt.setLong(4, lOrderId);
            cstmt.registerOutParameter(5, OracleTypes.CURSOR);
            cstmt.registerOutParameter(6, Types.CHAR);

            cstmt.executeUpdate();
            //dbmsOutput.show();
            //dbmsOutput.close();
            strMessage = cstmt.getString(6);
            System.out.println("[CustomerDAO][getCustomerDataDetail]iCustomerId: "+iCustomerId+"");
            System.out.println("[CustomerDAO][getCustomerDataDetail]strSwcreatedBy: "+strSwcreatedBy);
            System.out.println("[CustomerDAO][getCustomerDataDetail]iRegionid: "+iRegionid+"");
            System.out.println("[CustomerDAO][getCustomerDataDetail]lOrderId: "+lOrderId+"");
            System.out.println("[CustomerDAO][getCustomerDataDetail]strMessage: "+strMessage);

            if (strMessage==null){
                rs = (ResultSet)cstmt.getCursor(5);
                if (rs.next()) {
                    objCustomerBean = new CustomerBean();
                    objCustomerBean.setSwCustomerId(rs.getInt(1));
                    objCustomerBean.setSwName(rs.getString(2));
                    objCustomerBean.setSwRuc(rs.getString(4));
                    objCustomerBean.setSwType(rs.getString(6));
                    objCustomerBean.setSwCodBscs(rs.getString(5));
                    objCustomerBean.setSwRating(rs.getString(7));
                    objCustomerBean.setNpCustomerRelationType(rs.getString(17));
                    objCustomerBean.setNpLineaCredito(MiUtil.parseInt(rs.getString(8)));
                    objCustomerBean.setNpCustomerRelationid(rs.getInt(18));
                    objCustomerBean.setNpCustomerRelationName(rs.getString(19));
                    objCustomerBean.setNpCorporativo(rs.getString(13)); //tipocuenta
                    objCustomerBean.setNpGiroName(rs.getString(12)); //gironegocio
                    objCustomerBean.setSwMainPhone(rs.getString(9)); //swmainphone
                    objCustomerBean.setSwMainFax(rs.getString(10)); //swmainfax;
                    objCustomerBean.setSwRegionName(rs.getString(11)); //region
                    objCustomerBean.setSwNameCom(rs.getString(3)); //corporatename
                    objCustomerBean.setSwRegionId(rs.getLong(20));
                    objCustomerBean.setNpTipoDoc(rs.getString(21));
                    objCustomerBean.setStrExclusivity(rs.getString(22));
                    objCustomerBean.setStrRucValid(rs.getString(23));
                    objCustomerBean.setStrNameValid(rs.getString(24));
                    objCustomerBean.setNpSegmento(rs.getString(25));// Segmento de negocio jsalazar
                    objCustomerBean.setNpRangoCuenta(rs.getString(26));// Rango de Cuenta --Ocruces
                    objCustomerBean.setSwTipoPerson(rs.getString(27));//  Tipo Person --HHS001
                    objCustomerBean.setNpFechaLpdp(rs.getString(28));// Fecha LPDP
                    objCustomerBean.setNpAutoLpdp(rs.getString(29));// Autorizacion LPDP
                }
            }
        }
        catch (Exception e) {
            logger.error(formatException(e));
            hshData.put("strMessage",e.getMessage());
            throw new Exception(e);
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        hshData.put("strMessage",strMessage);
        hshData.put("objCustomerBean",objCustomerBean);

        return hshData;
    }

    public HashMap getNumAddressId (String strTipoObjeto, long lCodigo, String strTipoDireccion, long lRegionId) throws Exception,SQLException{

        HashMap h = new HashMap();

        Connection conn = null;
        OracleCallableStatement cstmt = null;

        String strMessage = null;
        long  lnumAddressId = 0;

        try{
            String sqlStr = "BEGIN ORDERS.NP_ORDERS03_PKG.SP_GET_NUM_ADDRESSID_BY_REGION (?, ?, ?, ?, ?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setString(1, strTipoObjeto);
            cstmt.setLong(2, lCodigo);
            cstmt.setString(3, strTipoDireccion);
            cstmt.setLong(4, lRegionId);
            cstmt.registerOutParameter(5, Types.NUMERIC);
            cstmt.registerOutParameter(6, Types.CHAR);
            cstmt.executeUpdate();

            strMessage = cstmt.getString(6);

            if( strMessage == null ){
                lnumAddressId = cstmt.getInt(5);
                h.put("wn_numaddressid",lnumAddressId+"");
            }
            h.put("strMessage",strMessage);
        }catch (Exception e) {
            logger.error(formatException(e));
            h.put("strMessage",e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        if(logger.isDebugEnabled()) {
            logger.debug("strTipoObjeto: "+strTipoObjeto);
            logger.debug("lCodigo: "+lCodigo);
            logger.debug("strTipoDireccion: "+strTipoDireccion);
            logger.debug("lnumAddressId: "+lnumAddressId);
            logger.debug("strMessage: "+strMessage);
        }
        return h;
    }


    public byte getIsSiteBA(long lngCustomerId) throws Exception,SQLException{

        Connection conn = null;
        OracleCallableStatement cstmt = null;
        byte bytResult = 0;

        try{
            String sqlStr = "{ ? = call SWBAPPS.FXI_IS_SITE_BA(?) }";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.registerOutParameter(1, OracleTypes.NUMBER);
            cstmt.setLong(2, lngCustomerId);
            cstmt.execute();

            bytResult = cstmt.getByte(1);

        }catch (Exception e) {
            logger.error(formatException(e));
            bytResult = -1;
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        return bytResult;
    }

    /**
     Method : getInfoCustomer
     Purpose: Obtener información del cliente según el numero de la orden
     Developer       		Fecha       Comentario
     =============   		==========  ======================================================================
     Hugo Moreno     	  13/10/2008  Creación
     */
    public HashMap getValidateDocument(String strNumDoc,String strTipoDoc) throws Exception,SQLException{

        HashMap objHashMap = new HashMap();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strMessage = null;
        ArrayList list = new ArrayList();
        try{
            String sqlStr = "BEGIN WEBSALES.NPSL_CUSTOMER_PKG.SP_VALIDATE_DOC_CUSTOMER(?,?,?,?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setString(1, strTipoDoc);
            cstmt.setString(2, strNumDoc);
            cstmt.registerOutParameter(3, OracleTypes.CURSOR);
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
            cstmt.executeUpdate();
            strMessage = cstmt.getString(4);
            if( strMessage == null ) {
                rs = cstmt.getCursor(3);
                while(rs.next()) {
                    HashMap h = new HashMap();
                    h.put("swruc",rs.getString(1));
                    h.put("swname",rs.getString(2));
                    list.add(h);
                }
            }
            objHashMap.put("strMessage",strMessage);
            objHashMap.put("arrCustomer",list);
        }
        catch (Exception e){
            logger.error(formatException(e));
            objHashMap.put("strMessage",e.getMessage());
        }finally{
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        return objHashMap;
    }    

    /*Inicio Responsable de Pago*/
    /**
     Method : getCustomerSitesBySolution
     Purpose: Obtener la lista de Sites del cliente en base a la solución
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     Jorge Pérez     21/05/2009  Creación
     Lizbeth Valencia 17/03/2012 Se agrega la especeficiación como parámetro
     */

    public HashMap getCustomerSitesBySolution(long intCustomerId, int iSolution, int iSpecification) throws Exception, SQLException{

        ArrayList list = new ArrayList();
        HashMap   hshResultado  = new HashMap();
        Connection conn = null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strMessage = null;
        try{
            String sqlStr = "BEGIN WEBSALES.SPI_GET_SITE_LIST_BY_SOLUTION(?, ?, ?, ?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, intCustomerId);
            cstmt.setInt(2,iSolution);
            cstmt.setInt(3,iSpecification);
            cstmt.registerOutParameter(4, OracleTypes.CURSOR);
            cstmt.registerOutParameter(5, Types.CHAR);
            cstmt.executeUpdate();
            strMessage = cstmt.getString(5);
            if( strMessage != null ){
                hshResultado.put("strMessage",strMessage);
                return hshResultado;
            }
            rs = (ResultSet)cstmt.getObject(4);

            while (rs.next()) {

                HashMap h = new HashMap();

                h.put("wn_swsiteid",rs.getInt(1)+"");
                h.put("wv_swsitename",rs.getString(2)==null?"":rs.getString(2));
                h.put("wv_npcodbscs",rs.getString(3)==null?"":rs.getString(3));
                h.put("wv_status",rs.getString(4)==null?"":rs.getString(4));
                h.put("wv_message",strMessage + "");

                list.add(h);

            }
            hshResultado.put("objRespPagoList",list);
            hshResultado.put("strMessage",strMessage);
        }catch (Exception e) {
            logger.error(formatException(e));
            hshResultado.put("strMessage",e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return hshResultado;
    }         
    /*Fin Responsable de Pago*/

    /**
     Method : getContactList
     Purpose: Obtener el detalle del contacto de un tipo determinado del customer o site
     Developer       		Fecha       Comentario
     =============   		==========  ======================================================================
     Henry Rengifo     	16/06/2009  Creación
     */
    public  HashMap getContactList(long lObjectTypeId, String strObjectType,String strType, long lSiteId) throws SQLException {
        HashMap hshData=new HashMap();
        Connection conn=null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strMessage=null;
        ArrayList list = new ArrayList();
        ContactObjectBean objContactBean=null; //new ContactObjectBean();

        try{
            System.out.println("============== Inicio - GetContactList ====================");
            System.out.println("lObjectTypeId ["+lObjectTypeId+"]");
            System.out.println("strObjectType ["+strObjectType+"]");
            System.out.println("strType       ["+strType+"]");
            System.out.println("lSiteId       ["+lSiteId+"]");

            conn = Proveedor.getConnection();
            String sqlStr =  "BEGIN WEBSALES.SPI_GET_CONTACT_DETAIL3(?, ?, ?, ?, ?, ?); END;";

            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, lObjectTypeId);
            cstmt.setString(2, strObjectType);
            cstmt.setString(3, strType);
            cstmt.setLong(4, lSiteId);
            cstmt.registerOutParameter(5, OracleTypes.CURSOR);
            cstmt.registerOutParameter(6, Types.VARCHAR);

            cstmt.execute();
            strMessage = cstmt.getString(6);

            if ( strMessage == null  ){
                rs = (ResultSet)cstmt.getObject(5);

                while (rs.next()) {
                    objContactBean=new ContactObjectBean();
                    objContactBean.setSwtitle(rs.getString("swtitle")==null?"":rs.getString("swtitle"));
                    objContactBean.setSwfirstname(rs.getString("swfirstname")==null?"":rs.getString("swfirstname"));
                    objContactBean.setSwlastname(rs.getString("swlastname")==null?"":rs.getString("swlastname"));
                    objContactBean.setSwmiddlename(rs.getString("swmiddlename")==null?"":rs.getString("swmiddlename"));
                    objContactBean.setSwjobtitle(rs.getString("swjobtitle")==null?"":rs.getString("swjobtitle"));
                    objContactBean.setSwjobtitleid(rs.getLong("swjobtitleid"));
                    objContactBean.setSwemailaddress(rs.getString("swemailaddress")==null?"":rs.getString("swemailaddress"));
                    objContactBean.setSwpersonid(rs.getLong("swpersonid"));
                    list.add(objContactBean);
                }
            }

            hshData.put("strMessage",strMessage);
            hshData.put("arrContactList",list);
        }catch (Exception e) {
            logger.error(formatException(e));
            hshData.put("strMessage",e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        System.out.println("============== Fin - GetContactList ====================");
        return hshData;
    }

    /**
     Method : insChangeContact
     Purpose: Inserta los cambios que se solicitan en los datos del contacto tipo pedido de un cliente.
     Developer       		Fecha       Comentario
     =============   		==========  ======================================================================
     Henry Rengifo     	16/06/2009  Creación
     */
    public String insChangeContact(long lOrderId, long lObjectType,long lCustomerId,
                                   ContactObjectBean objContactBean, java.sql.Connection conn){
        String strMessage=null;
        OracleCallableStatement cstmt = null;
        try{
            String sqlStr = "BEGIN WEBSALES.SPI_INSERT_CHANGE_CONTACT( ?, ?, ?, ?, " +
                    "?, ?, ?, ?, " +
                    "?, ?, ?, ?, " +
                    "?, ?, ?, ?, " +
                    "?, ?, ?, ?, " +
                    "?, ? ); END;";

            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

            cstmt.setLong(1, lOrderId);
            cstmt.setLong(2, lObjectType);
            cstmt.setLong(3, lCustomerId);
            cstmt.setLong(4, objContactBean.getSwsiteid());
            cstmt.setString(5, objContactBean.getSwtitle());
            cstmt.setString(6, objContactBean.getSwfirstname());
            cstmt.setString(7, objContactBean.getSwlastname());
            cstmt.setString(8, objContactBean.getSwmiddlename());
            cstmt.setString(9, objContactBean.getSwjobtitle());
            cstmt.setString(10, objContactBean.getSwemailaddress());
            cstmt.setString(11, objContactBean.getNptitlenew());
            cstmt.setString(12, objContactBean.getNpfirstnamenew());
            cstmt.setString(13, objContactBean.getNplastnamenew());
            cstmt.setString(14, objContactBean.getNpmiddlenamenew());
            cstmt.setString(15, objContactBean.getNpjobtitlenew());
            cstmt.setString(16, objContactBean.getNpemailnew());
            cstmt.setString(17, objContactBean.getNpcontacttype());
            cstmt.setString(18, objContactBean.getNpaction());

            if( objContactBean.getSwpersonid() == 0 )
                cstmt.setNull(19, OracleTypes.NUMBER);
            else
                cstmt.setLong(19, objContactBean.getSwpersonid());

            cstmt.setString(20, objContactBean.getNpcreatedby());
            cstmt.setInt(21, objContactBean.getNpitemid());
            cstmt.registerOutParameter(22, Types.VARCHAR);

            cstmt.executeUpdate();

            strMessage = cstmt.getString(22);
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(formatException(e));
            strMessage = e.getMessage();
        }
        finally{
            try{
                closeObjectsDatabase(null, cstmt, null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return strMessage;
    }

    /**
     Method : updChangeContact
     Purpose: Actualiza los cambios que se solicitan en los datos del contacto tipo pedido de un cliente.
     Developer       		Fecha       Comentario
     =============   		==========  ======================================================================
     Henry Rengifo     	16/06/2009  Creación
     */
    public String updChangeContact(long lOrderId, String strObjectType,long lCustomerId,
                                   ContactObjectBean objContactBean,
                                   java.sql.Connection conn) { //throws Exception,SQLException {

        OracleCallableStatement cstmt = null;
        String strMessage = null;

        String strSql = "BEGIN WEBSALES.SPI_UPDATE_CHANGE_CONTACT( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ); END;";

        System.out.println("-----------------------");
        System.out.println("lOrderId ["+lOrderId+"]");
        System.out.println("strObjectType ["+strObjectType+"]");
        System.out.println("lCustomerId ["+lCustomerId+"]");
        System.out.println("-----------------------");

        try{
            cstmt = (OracleCallableStatement)conn.prepareCall(strSql);

            cstmt.setLong(1, lOrderId);
            //cstmt.setLong(2, 1);
            cstmt.setLong(2, Long.parseLong(strObjectType));
            cstmt.setLong(3, lCustomerId);
            //Contacto
            cstmt.setString(4, objContactBean.getNptitlenew());
            cstmt.setString(5, objContactBean.getNpfirstnamenew());
            cstmt.setString(6, objContactBean.getNplastnamenew());
            cstmt.setString(7, objContactBean.getNpmiddlenamenew());
            cstmt.setString(8, objContactBean.getNpjobtitlenew());
            cstmt.setString(9, objContactBean.getNpemailnew());
            cstmt.setString(10, objContactBean.getNpaction());
            cstmt.setLong(11,  objContactBean.getNpitemid());
            cstmt.registerOutParameter(12, Types.VARCHAR);

            cstmt.executeUpdate();
            strMessage = cstmt.getString(12);
        }catch (Exception e) {
            logger.error(formatException(e));
            strMessage = e.getMessage();
        }
        finally{
            try{
                closeObjectsDatabase(null, cstmt, null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return strMessage;
    }

    /**
     * Motivo: Inserta los cambios que se solicitan en los datos del contacto tipo pedido de un cliente determinado
     * <br>Realizado por: <a href="mailto:evelyn.ocampo@nextel.com.pe">EVELYN OCAMPO</a>
     * <br>Fecha: 16/06/2009
     * @param     lOrderId
     * @param     iObjectType
     * @param     lObjectId
     * @param     strNameNew
     * @param     objCutomerBeanNew
     * @param     objAddressBeanNew
     * @param     objContactBeanNew
     * @return    String
     */
   /* public String updChangeContactEdit(long lOrderId, long lCustomerId,                                      
                                       ContactObjectBean objContactBean, java.sql.Connection conn)                                       
    
    {   
       String strMessage=null;          
       OracleCallableStatement cstmt = null;
       try{
          String sqlStr = "BEGIN WEBSALES.SPI_UPDATE_CONTACT_EDIT (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); END;";
          
          System.out.println("-------------------");                                                      
          System.out.println("lOrderId ["+lOrderId+"]");
          System.out.println("lOrderId ["+lCustomerId+"]");          
          System.out.println("-------------------"); 
      
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
           
         cstmt.setLong(1, lOrderId);    
         cstmt.setLong(2, 1); 
         //cstmt.setLong(2, Long.parseLong(strObjectType));  
         cstmt.setLong(3, lCustomerId);
         //Contacto
         cstmt.setString(4, objContactBean.getNptitlenew());      
         cstmt.setString(5, objContactBean.getNpfirstnamenew());
         cstmt.setString(6, objContactBean.getNplastnamenew());
         cstmt.setString(7, objContactBean.getNpmiddlenamenew());
         cstmt.setString(8, objContactBean.getNpjobtitlenew());
         cstmt.setLong(9, objContactBean.getSwjobtitleid());         
         cstmt.setString(10, objContactBean.getNpemailnew());            
         cstmt.setLong(11, objContactBean.getNpitemid());     
         cstmt.registerOutParameter(12, Types.VARCHAR);   
         
         int i=cstmt.executeUpdate();   
         strMessage = cstmt.getString(12);  
       }catch (Exception e) {       
         logger.error(formatException(e));
         strMessage = e.getMessage();
       }
       finally{
         try{
           closeObjectsDatabase(null, cstmt, null);  
        }catch (Exception e) {
           logger.error(formatException(e));
        }
      }   
    
       return strMessage;
    }     */

    /**
     Method : getContactChangeList
     Purpose: Obtener el detalle del contacto de un tipo determinado del customer o site
     Developer       		Fecha       Comentario
     =============   		==========  ======================================================================
     Henry Rengifo     	16/06/2009  Creación
     */
    public  HashMap getContactChangeList(long lOrderId,int iItemid,int iObjectType,long lObjectId, String strContactType) throws SQLException {
        HashMap hshData=new HashMap();
        Connection conn=null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strMessage=null;
        ArrayList list = new ArrayList();
        ContactObjectBean objContactBean=null; //new ContactObjectBean();

        try{

            System.out.println("============Inicio - getContactChangeList==================");
            System.out.println("lOrderId "+lOrderId);
            System.out.println("iItemid "+iItemid);
            System.out.println("iObjectType "+iObjectType);
            System.out.println("lObjectId "+lObjectId);
            System.out.println("strContactType "+strContactType);


            conn = Proveedor.getConnection();
            String sqlStr =  "BEGIN WEBSALES.SPI_GET_CONTACT_CHANGE2(?, ?, ?, ?, ?, ?, ?); END;";

            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, lOrderId);
            cstmt.setInt(2, iItemid);
            cstmt.setInt(3, iObjectType);
            cstmt.setLong(4, lObjectId);
            cstmt.setString(5, strContactType);
            cstmt.registerOutParameter(6, OracleTypes.CURSOR);
            cstmt.registerOutParameter(7, Types.VARCHAR);

            cstmt.execute();
            strMessage = cstmt.getString(7);

            if ( strMessage == null  ){
                rs = (ResultSet)cstmt.getObject(6);

                while (rs.next()) {
                    objContactBean=new ContactObjectBean();

                    objContactBean.setSwtitle(rs.getString("nptitle")==null?"":rs.getString("nptitle"));
                    objContactBean.setSwfirstname(rs.getString("npfirstname")==null?"":rs.getString("npfirstname"));
                    objContactBean.setSwlastname(rs.getString("nplastname")==null?"":rs.getString("nplastname"));
                    objContactBean.setSwmiddlename(rs.getString("npmiddlename")==null?"":rs.getString("npmiddlename"));
                    objContactBean.setSwjobtitle(rs.getString("npjobtitle")==null?"":rs.getString("npjobtitle"));
                    objContactBean.setSwemailaddress(rs.getString("npemail")==null?"":rs.getString("npemail"));
                    objContactBean.setNpaction(rs.getString("npaction")==null?"":rs.getString("npaction"));
                    objContactBean.setNptitlenew(rs.getString("nptitlenew")==null?"":rs.getString("nptitlenew"));
                    objContactBean.setNpfirstnamenew(rs.getString("npfirstnamenew")==null?"":rs.getString("npfirstnamenew"));
                    objContactBean.setNplastnamenew(rs.getString("nplastnamenew")==null?"":rs.getString("nplastnamenew"));
                    objContactBean.setNpmiddlenamenew(rs.getString("npmiddlenamenew")==null?"":rs.getString("npmiddlenamenew"));
                    objContactBean.setNpjobtitlenew(rs.getString("npjobtitlenew")==null?"":rs.getString("npjobtitlenew"));
                    objContactBean.setNpemailnew(rs.getString("npemailnew")==null?"":rs.getString("npemailnew"));
                    objContactBean.setSwpersonid(rs.getLong("nppersonid"));
                    objContactBean.setSwjobtitleid(rs.getLong("swjobtitleid"));
                    objContactBean.setNpitemid(rs.getInt("npitemid"));

                    list.add(objContactBean);
                }
            }
            hshData.put("strMessage",strMessage);
            hshData.put("arrContactList",list);
        }catch (Exception e) {
            logger.error(formatException(e));
            hshData.put("strMessage",e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        System.out.println("============Fin - getContactChangeList==================");
        return hshData;
    }


    /**
     Method : updChangeContact
     Purpose: Actualiza el estado de los datos del contacto.
     Developer       		Fecha       Comentario
     =============   		==========  ======================================================================
     Henry Rengifo     	01/07/2009  Creación
     */
    public String updChangeContactAction(long lOrderId, String strObjectType,long lCustomerId,
                                         String strNpaction, java.sql.Connection conn) { //throws Exception,SQLException {

        OracleCallableStatement cstmt = null;
        String strMessage = null;

        String strSql = "BEGIN WEBSALES.SPI_UPDATE_CONTACT_ACTION( ?, ?, ?, ?, ? ); END;";

        System.out.println("-----------------------");
        System.out.println("lOrderId ["+lOrderId+"]");
        System.out.println("strObjectType ["+strObjectType+"]");
        System.out.println("lCustomerId ["+lCustomerId+"]");
        System.out.println("-----------------------");

        try{
            cstmt = (OracleCallableStatement)conn.prepareCall(strSql);

            cstmt.setLong(1, lOrderId);
            cstmt.setString(2, strObjectType);
            cstmt.setLong(3, lCustomerId);
            cstmt.setString(4, strNpaction);
            cstmt.registerOutParameter(5, Types.VARCHAR);

            cstmt.executeUpdate();
            strMessage = cstmt.getString(5);
        }catch (Exception e) {
            logger.error(formatException(e));
            strMessage = e.getMessage();
        }
        finally{
            try{
                closeObjectsDatabase(null, cstmt, null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return strMessage;
    }

    /**
     Method : validateUbigeo
     Purpose:
     Developer       		Fecha       Comentario
     =============   		==========  ======================================================================
     DGUTIERREZ     	   21/07/2010  Creación
     */

    public String validateUbigeo(String state, String province, String city) {

        String strMessage = "";
        Connection conn = Proveedor.getConnection();
        OracleCallableStatement cstmt = null;
        
      /*SPI_GET_UBIGEO (
        an_swstate             IN   VARCHAR2,
        an_swprovince          IN   VARCHAR2,
        an_swcity              IN   VARCHAR2,
        av_ubigeo             OUT   VARCHAR2*/

        String strSql = "BEGIN ORDERS.SPI_GET_UBIGEO( ?, ?, ?, ?); END;";

        try {

            cstmt = (OracleCallableStatement)conn.prepareCall(strSql);

            cstmt.setString(1, state);
            cstmt.setString(2, province);
            cstmt.setString(3, city);

            cstmt.registerOutParameter(4, Types.VARCHAR);

            cstmt.execute();

            strMessage = cstmt.getString(4);

        } catch (Exception e) {

            logger.error(formatException(e));
            strMessage = "";

        } finally{

            try{

                closeObjectsDatabase(conn, cstmt, null);


            } catch (Exception e) {

                logger.error(formatException(e));
                strMessage = "";

            }

        }

        return strMessage;

    }
    /**
    Method : validateUbigeo
    Purpose:
    Developer                 Fecha       Comentario
    =============             ==========  ======================================================================
    FBERNALES                22/12/2015  Creación
    */  
    public Map<Double,Boolean> getValidateAddress(String sAddress, String sUbigeo, 
                                      String sAplicacion){
        Connection conn=null;
        String storedProcedure;
        OracleCallableStatement cstmt = null;
        Boolean bReturn = false;
        Map<Double,Boolean> mapReturn = new HashMap<Double,Boolean>();
        ResultSet rs = null;
        int lResultValidateAddress = 0;
        String av_message = null;
        int an_status = 0;
        storedProcedure = "begin WEBSALES.spi_get_validate_address(?,?,?,?,?,?); end;";
     
        
        try {
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(storedProcedure);

            cstmt.registerOutParameter(1, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(2, OracleTypes.NUMBER);
            cstmt.registerOutParameter(3, OracleTypes.NUMBER);
            cstmt.setString(4, sAddress);
            cstmt.setString(5, sUbigeo);
            cstmt.setString(6, sAplicacion);
            //Se ejecuta el statement
            cstmt.execute();
            //Se obtiene la respuesta error/exito del stored

            String strMessage = (String)cstmt.getObject(1);
            if (strMessage != null) {
                System.out.println("Error en la base de datos: " + strMessage);
                logger.error("CustomerDAO.getValidateAddress(...  ).SQLException:" + 
                             av_message);
            } else {
                lResultValidateAddress = ((BigDecimal)cstmt.getObject(2)).intValue();
                Double lResultCorrelacionAddress = ((BigDecimal)cstmt.getObject(3)).doubleValue();
                if (lResultValidateAddress > 0) {
                    bReturn = true;
                } else {
                    bReturn = false;
                }
                mapReturn.put(lResultCorrelacionAddress,bReturn);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } finally {
            try{

                closeObjectsDatabase(conn, cstmt, null);


            } catch (Exception e) {

                logger.error(formatException(e));
            }
        }
        return (mapReturn);
    }
    
    public void insLogValidateAddress(String sIdApp,
                                      Double dCorrelacion, 
                                      //String sIp,
                                      String sCreatedBy,
                                      //String sPhoneNumber,
                                      String sDireccion,
                                      Integer lIdCliente,
                                      String sNumDoc,
                                      Integer lNumOrder ){
      Connection conn=null;
      String storedProcedure;
        OracleCallableStatement cstmt = null;
      String av_message; 
        
        Boolean bReturn = false;
        ResultSet rs = null;
        int lResultValidateAddress = 0;
        av_message = null;
        int an_status = 0;
        oracle.sql.DATE wv_createddate = null;
        storedProcedure = 
                "begin WEBSALES.SPI_INS_LOG_VALIDATE_ADDRESS(?,?,?,?,?,?,?,?,?); end;";
        try {
        /*
            av_message         OUT VARCHAR2,
            av_npidapp          IN VARCHAR2,
            an_npcorrelacion    IN NUMBER,
            av_npip             IN VARCHAR2,
            ad_npcreatedby      IN VARCHAR2,
            av_npphonenumber    IN VARCHAR2,
            ad_npcreateddate    IN DATE,
            av_npdireccion      IN VARCHAR2
         */
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(storedProcedure);

            cstmt.registerOutParameter(1, OracleTypes.VARCHAR);
            cstmt.setString(2, sIdApp);
            cstmt.setDouble(3, dCorrelacion);
            //cstmt.setString(4, sIp);
            cstmt.setString(4, sCreatedBy);
            //cstmt.setString(6, sPhoneNumber);
            cstmt.setDATE(5, wv_createddate);
            cstmt.setString(6, sDireccion);
            cstmt.setInt(7, lIdCliente);
            cstmt.setString(8, sNumDoc);
            cstmt.setInt(9, lNumOrder);
            
            //Se ejecuta el statement
            cstmt.execute();

             String strMessage = (String)cstmt.getObject(1);
             if (strMessage != null && !strMessage.equalsIgnoreCase("NO ERRORS")) {
                System.out.println("Error en la base de datos: " + strMessage);
                logger.error("CustomerDAO.insLogValidateAddress(...  ).SQLException:" + 
                             strMessage);            
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } finally {
            //Se limpian las variables de conexión
            try{
                closeObjectsDatabase(conn, cstmt, null);

            } catch (Exception e) {

                logger.error(formatException(e));
            } 
        }        
    }

    /**
     Method : getOrdenesDocDigitalesList
     Purpose: Obtener las ordenes con documentos digitales
     Developer       		 Fecha          Comentario
     =================		 ==========     ================================================
     [PM0011173]LROQUE       28/10/2016     Creación*/
    public Map<String, Object> getOrdenesDocDigitalesList(Long idCustomer) throws Exception {
        Map<String, Object> hshData = new HashMap<String, Object>();
        Connection conn=null;
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;

        String messageError = null;

        String traza = "[CustomerDAO][getOrdenesDocDigitalesList][idCustomer="+idCustomer+"] ";
        System.out.println(traza + "INICIO");
        long timeInicio = System.currentTimeMillis();
        try{
            System.out.println(traza + "SP: ORDERS.SPI_ORDENES_DOC_DIGITALES");
            System.out.println(traza + "Param IN: an_customer_id="+idCustomer);

            conn = Proveedor.getConnection();
            String sqlStr =  "BEGIN ORDERS.SPI_ORDENES_DOC_DIGITALES(?, ?, ?); END;";

            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, idCustomer);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.registerOutParameter(3, Types.VARCHAR);

            cstmt.execute();

            messageError = cstmt.getString(3);
            if (StringUtils.isBlank(messageError)){
                List<OrdenDocDigitalBean> list = new ArrayList<OrdenDocDigitalBean>();

                rs = (ResultSet)cstmt.getObject(2);
                while (rs.next()) {
                    OrdenDocDigitalBean bean = new OrdenDocDigitalBean();
                    bean.setOrderId(rs.getLong(("nporderid")));
                    bean.setGeneratorId(rs.getLong(("npgeneratorid")));
                    bean.setType(rs.getString("nptype"));
                    bean.setSpecification(rs.getString("npspecification"));
                    bean.setParameterName(rs.getString("npparametername"));
                    bean.setCreateBy(rs.getString("npcreatedby"));

                    java.sql.Timestamp dateO = rs.getTimestamp("nporderdate");
                    bean.setOrderDate(dateO == null ? null : (new Date(dateO.getTime())));

                    list.add(bean);
                }

                hshData.put("result", list);
            }
        }catch (Exception e) {
            logger.error(formatException(e));
            messageError = (e.getCause()==null) ? e.getMessage() : e.getCause().toString();
        }finally{
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
            System.out.println(traza + "Tiempo de procesamiento: "+(System.currentTimeMillis() - timeInicio));
            System.out.println(traza + "FIN");
        }

        hshData.put("messageError", messageError);

        return hshData;
    }

}