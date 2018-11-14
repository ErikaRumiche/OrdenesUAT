package pe.com.nextel.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import java.sql.Types;
import pe.com.nextel.bean.DominioBean;
import pe.com.nextel.util.Constante;


public class InstallmentSalesDAO extends GenericDAO
{
  public InstallmentSalesDAO()
  {
  }
  
  public HashMap getConfigurationInstallment(String valuedesc) throws SQLException, Exception{             
        OracleCallableStatement cstmt = null; 
        ResultSet rs = null;
        String strMessage=null;
        Connection conn=null;
        HashMap objHashMap = new HashMap();
        ArrayList list = new ArrayList();
        DominioBean objDominioBean =  null;
        String strSolutionType= null;
        try{
          String strSql = " BEGIN INVOICE.SPI_GET_CONF_INSTALLMENT_LIST(?,?,?); END;";        
          conn = Proveedor.getConnection();   
          cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
          cstmt.setString(1, valuedesc);
          cstmt.registerOutParameter(2, OracleTypes.CURSOR);
          cstmt.registerOutParameter(3, OracleTypes.VARCHAR);  
          cstmt.executeUpdate();  
          strMessage = cstmt.getString(3);  
           if( strMessage == null ){
              rs = cstmt.getCursor(2);
              while (rs.next()) {
                  objDominioBean = new DominioBean();
                  
                  objDominioBean.setValor(rs.getString("npvaluedesc"));
                  objDominioBean.setDescripcion(rs.getString("npvalue"));
                  objDominioBean.setDescripcion_aux(rs.getString("npdescription"));
                  objDominioBean.setParam1(rs.getString("npparam1"));
                  objDominioBean.setParam2(rs.getString("npparam2"));
                  objDominioBean.setParam3(rs.getString("npparam3"));
                  objDominioBean.setStatus(rs.getString("npstatus"));
                  
                  list.add(objDominioBean);
              }
          }
        
          objHashMap.put("strMessage",strMessage);
          objHashMap.put("objArrayList",list); 
          
        }catch(Exception e){
            logger.error(formatException(e));
            objHashMap.put("strMessage",e.getMessage());
          }
          finally{
            try{
               closeObjectsDatabase(conn,cstmt,rs); 
            }catch (Exception e) {
               logger.error(formatException(e));
            }
          }  
     return objHashMap;                
  }

    /**
     * @autor EFLORES PM0011359
     * Metodo replica de doCreateInstallmentSales con el parametro connection como entrada.
     * @param lOrderId
     * @param lCustomerId
     * @param lSiteId
     * @param lCustomerbscsId
     * @param strCodbscs
     * @param lquotaNumber
     * @param dAmountTotal
     * @param strOrigenStatus
     * @param strLogin
     * @param conn
     * @return
     */
    public HashMap doCreateInstallmentSales(long lOrderId,
                                            long lCustomerId,
                                            long lSiteId,
                                            long lCustomerbscsId,
                                            String strCodbscs,
                                            long lquotaNumber,
                                            double dAmountTotal,
                                            String  strOrigenStatus,
                                            String strLogin,
                                            double dinitialQuota,
                                            //INICIO: PRY-0980 | AMENDEZ
                                            int npPaymentTerms,
                                            //FIN: PRY-0980 | AMENDEZ
                                            Connection conn)
    {
        logger.info("************************** INICIO InstallmentSalesDAO > doCreateInstallmentSales**************************");

        logger.info("[INPUT][lOrderId]          : "+lOrderId);
        logger.info("[INPUT][lCustomerId]       : "+lCustomerId);
        logger.info("[INPUT][lSiteId]           : "+lSiteId);
        logger.info("[INPUT][lCustomerbscsId]   : "+lCustomerbscsId);
        logger.info("[INPUT][strCodbscs]        : "+strCodbscs);
        logger.info("[INPUT][lquotaNumber]      : "+lquotaNumber);
        logger.info("[INPUT][dAmountTotal]      : "+dAmountTotal);
        logger.info("[INPUT][strOrigenStatus]   : "+strOrigenStatus);
        logger.info("[INPUT][strLogin]          : "+strLogin);
        logger.info("[INPUT][dinitialQuota]     : "+dinitialQuota);
        //INICIO: PRY-0980 | AMENDEZ
        logger.info("[INPUT][npPaymentTerms]    : "+npPaymentTerms);
        //FIN: PRY-0980 | AMENDEZ
        logger.info("[INPUT][conn]              : "+conn);

        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strMessage=null;
        HashMap objHashMap = new HashMap();
        ArrayList list = new ArrayList();
        try{
            String strSql = " BEGIN INVOICE.SPI_CREATE_INSTALLMENT_SALES(?,?,?,?,?,?,?,?,?,?,?,?); END;";
            cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
            cstmt.setLong(1,lOrderId);
            cstmt.setLong(2,lCustomerId);
            cstmt.setLong(3,lSiteId);
            cstmt.setNull(4,0);//lCustomerbscsId
            cstmt.setString(5,strCodbscs);
            cstmt.setLong(6,lquotaNumber);
            cstmt.setDouble(7,dAmountTotal);
            cstmt.setString(8,strOrigenStatus);
            cstmt.setString(9,strLogin);
            //INICIO: PRY-0864 | AMENDEZ
            cstmt.setDouble(10,dinitialQuota);
            //FIN: PRY-0864 | AMENDEZ

            //INICIO: PRY-0980 | AMENDEZ
            cstmt.setInt(11,npPaymentTerms);
            //FIN: PRY-0980 | AMENDEZ

            cstmt.registerOutParameter(12, OracleTypes.VARCHAR);
            cstmt.executeUpdate();
            strMessage = cstmt.getString(12);
            objHashMap.put("strMessage",strMessage);

        }catch(Exception e){
            logger.error(formatException(e));
            objHashMap.put("strMessage",e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(null,cstmt,rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        logger.info("************************** FIN InstallmentSalesDAO > doCreateInstallmentSales**************************");
        return objHashMap;
    }

    public HashMap doCreateInstallmentSales(long lOrderId,
                                         long lCustomerId, 
                                         long lSiteId,   
                                         long lCustomerbscsId, 
                                         String strCodbscs,
                                         long lquotaNumber,
                                         double dAmountTotal,
                                         String  strOrigenStatus,
                                         String strLogin,
                                         double dinitialQuota,
                                         //INICIO: PRY-0980 | AMENDEZ
                                         int npPaymentTerms
                                         //FIN: PRY-0980 | AMENDEZ
                                         )
  {
      logger.info("************************** INICIO InstallmentSalesDAO > doCreateInstallmentSales**************************");

      logger.info("[INPUT][lOrderId]          : "+lOrderId);
      logger.info("[INPUT][lCustomerId]       : "+lCustomerId);
      logger.info("[INPUT][lSiteId]           : "+lSiteId);
      logger.info("[INPUT][lCustomerbscsId]   : "+lCustomerbscsId);
      logger.info("[INPUT][strCodbscs]        : "+strCodbscs);
      logger.info("[INPUT][lquotaNumber]      : "+lquotaNumber);
      logger.info("[INPUT][dAmountTotal]      : "+dAmountTotal);
      logger.info("[INPUT][strOrigenStatus]   : "+strOrigenStatus);
      logger.info("[INPUT][strLogin]          : "+strLogin);
      logger.info("[INPUT][dinitialQuota]     : "+dinitialQuota);
      //INICIO: PRY-0980 | AMENDEZ
      logger.info("[INPUT][npPaymentTerms]    : "+npPaymentTerms);
      //FIN: PRY-0980 | AMENDEZ

        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strMessage=null;
        Connection conn=null;
        HashMap objHashMap = new HashMap();
        ArrayList list = new ArrayList();
        try{
            String strSql = " BEGIN INVOICE.SPI_CREATE_INSTALLMENT_SALES(?,?,?,?,?,?,?,?,?,?,?,?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
            cstmt.setLong(1,lOrderId);
            cstmt.setLong(2,lCustomerId);
            cstmt.setLong(3,lSiteId);
            cstmt.setNull(4,0);//lCustomerbscsId
            cstmt.setString(5,strCodbscs);
            cstmt.setLong(6,lquotaNumber);
            cstmt.setDouble(7,dAmountTotal);
            cstmt.setString(8,strOrigenStatus);
            cstmt.setString(9,strLogin);
            //INICIO: PRY-0864 | AMENDEZ
            cstmt.setDouble(10,dinitialQuota);
            //FIN: PRY-0864 | AMENDEZ

            //INICIO: PRY-0980 | AMENDEZ
            cstmt.setInt(11,npPaymentTerms);
            //FIN: PRY-0980 | AMENDEZ
            cstmt.registerOutParameter(12, OracleTypes.VARCHAR);
            cstmt.executeUpdate();
            strMessage = cstmt.getString(12);
            objHashMap.put("strMessage",strMessage);

        }catch(Exception e){
            logger.error(formatException(e));
            objHashMap.put("strMessage",e.getMessage());
        }
        finally{
            try{
               closeObjectsDatabase(conn,cstmt,rs); 
            }catch (Exception e) {
               logger.error(formatException(e));
            }
        }
      logger.info("************************** FIN InstallmentSalesDAO > doCreateInstallmentSales**************************");
     return objHashMap;    
  }

    /**
     * @author EFLORES PM0011359
     * Metodo borrar VEP replica de doDeleteInstallmentSales
     * @param lOrderId
     * @param conn
     * @return
     */
    public HashMap doDeleteInstallmentSales(long lOrderId,Connection conn){
        logger.info("************************** INICIO InstallmentSalesDAO > doDeleteInstallmentSales**************************");
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strMessage=null;
        HashMap objHashMap = new HashMap();
        try{
            logger.info("*lOrderId :    "+lOrderId);
            logger.info("*conn     :    "+conn);

            String strSql = " BEGIN INVOICE.SPI_DEL_INSTALLMENSALES_ORDER(?,?); END;";
            cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
            cstmt.setLong(1,lOrderId);
            cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
            cstmt.executeUpdate();
            strMessage = cstmt.getString(2);
            objHashMap.put("strMessage",strMessage);

        }catch(Exception e){
            logger.error(formatException(e));
            objHashMap.put("strMessage",e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(null,cstmt,rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        logger.info("************************** FIN InstallmentSalesDAO > doDeleteInstallmentSales**************************");
        return objHashMap;
    }
     public HashMap doDeleteInstallmentSales(long lOrderId){
        logger.info("************************** INICIO InstallmentSalesDAO > doDeleteInstallmentSales**************************");
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strMessage=null;
        Connection conn=null;
        HashMap objHashMap = new HashMap();
        try{
            logger.info("*lOrderId :    "+lOrderId);
            String strSql = " BEGIN INVOICE.SPI_DEL_INSTALLMENSALES_ORDER(?,?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
            cstmt.setLong(1,lOrderId);
            cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
            cstmt.executeUpdate();
            strMessage = cstmt.getString(2);
            objHashMap.put("strMessage",strMessage);
          
        }catch(Exception e){
            logger.error(formatException(e));
            objHashMap.put("strMessage",e.getMessage());
          }
          finally{
            try{
               closeObjectsDatabase(conn,cstmt,rs); 
            }catch (Exception e) {
               logger.error(formatException(e));
            }
          }
         logger.info("************************** FIN InstallmentSalesDAO > doDeleteInstallmentSales**************************");
     return objHashMap;    
  }
 
      public HashMap doValidateExistInstallmentSales(long lOrderId){
          logger.info("************************** INICIO InstallmentSalesDAO > doValidateExistInstallmentSales**************************");
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        String strMessage=null;
        String strIsVep=null;
        Connection conn=null;
        HashMap objHashMap = new HashMap();
        try{
              logger.info("lOrderId: "+lOrderId);
          String strSql = " BEGIN INVOICE.SPI_IS_ORDER_VEP(?,?, ?); END;";
          conn = Proveedor.getConnection();   
          cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
          cstmt.setLong(1,lOrderId);          
          cstmt.registerOutParameter(2, OracleTypes.VARCHAR);  
          cstmt.registerOutParameter(3, OracleTypes.VARCHAR); 
          cstmt.executeUpdate();  
          strIsVep = cstmt.getString(2); 
          strMessage = cstmt.getString(3);           
              logger.info("strIsVep: "+strIsVep);
              logger.info("strMessage: "+strMessage);
          objHashMap.put("strMessage",strMessage);
          objHashMap.put("strIsVep",strIsVep);
          
        }catch(Exception e){
            logger.error(formatException(e));
            objHashMap.put("strMessage",e.getMessage());
          }
          finally{
            try{
               closeObjectsDatabase(conn,cstmt,rs); 
            }catch (Exception e) {
               logger.error(formatException(e));
            }
          }  

          logger.info("************************** FIN InstallmentSalesDAO > doValidateExistInstallmentSales**************************");
     return objHashMap;    
  }


    /**
     * @author AMENDEZ
     * @project PRY-0864
     * Metodo   Evalua mostrar campo precio excepcion en popupitem
     *          flag vep activo y cliente es ruc20(Juridico): 1 --> Se muestra campo de precio excepcion
     *          flag vep inactivo y cliente no es ruc20(Natural): 0 --> No se muestra campo de precio excepcion
     *          errores generales -1
     * @return
     */
    public int evaluateExceptionPriceVep(int npvep,long sw_customerid) throws SQLException, Exception{
        logger.info("************************** INICIO InstallmentSalesDAO > evaluateExceptionPriceVep**************************");
        int iReturnValue = -1;
        Connection conn=null;
        OracleCallableStatement cstmt = null;
        String avch_origin= Constante.VEP_ORIGIN_APP;
        try{
            logger.info("npvep         : "+npvep);
            logger.info("sw_customerid : "+sw_customerid);
            logger.info("avch_origin   : "+avch_origin);

            String sqlStr =  " { ? = call INVOICE.FNC_GET_EXCEPTIONPRICEVEP_EVAL( ?, ?, ? ) } ";

            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.registerOutParameter(1, Types.NUMERIC);
            cstmt.setInt(2, npvep);
            cstmt.setLong(3, sw_customerid);
            cstmt.setString(4,avch_origin);

            cstmt.executeQuery();
            iReturnValue = cstmt.getInt(1);
        }catch(Exception e){
            logger.error(formatException(e));
            iReturnValue = -1;
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        logger.info("************************** FIN InstallmentSalesDAO > evaluateExceptionPriceVep**************************");
        return iReturnValue;
    }

    /**
     * @author AMENDEZ
     * @project PRY-1200
     * Metodo   Evalua el monto inicial en casos de ordenes vep
     * @return
     */
    public String validateOrderVepCI(long nporderid,int npvepquantityquota,double npinitialquota,int npspecificationid,long swcustomerid,double totalsalesprice,int npvep,String nptype,int nppaymenttermsiq) throws SQLException, Exception{
        logger.info("************************** INICIO InstallmentSalesDAO > validateOrderVepCI**************************");
        String sReturnValue = null;
        Connection conn=null;
        OracleCallableStatement cstmt = null;
        String avch_origin= Constante.VEP_ORIGIN_APP;
        try{
            logger.info("nporderid            : "+nporderid);
            logger.info("npvepquantityquota   : "+npvepquantityquota);
            logger.info("npinitialquota       : "+npinitialquota);
            logger.info("npspecificationid    : "+npspecificationid);
            logger.info("swcustomerid         : "+swcustomerid);
            logger.info("totalsalesprice      : "+totalsalesprice);
            logger.info("npvep                : "+npvep);
            logger.info("nptype               : "+nptype);
            logger.info("nppaymenttermsiq     : "+nppaymenttermsiq);
            logger.info("avch_origin          : "+avch_origin);

            String sqlStr =  " { ? = call INVOICE.FNC_GET_VALID_QUOTA_VEP( ?, ?, ? ,?, ?, ?, ?, ?, ?, ?) } ";

            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.registerOutParameter(1, Types.VARCHAR);

            cstmt.setLong(2, nporderid);
            cstmt.setInt(3, npvepquantityquota);
            cstmt.setDouble(4,npinitialquota);
            cstmt.setInt(5, npspecificationid);
            cstmt.setLong(6, swcustomerid);
            cstmt.setDouble(7,totalsalesprice);
            cstmt.setInt(8,npvep);
            cstmt.setString(9,nptype);
            cstmt.setInt(10,nppaymenttermsiq);
            cstmt.setString(11,avch_origin);


            cstmt.executeQuery();
            sReturnValue = cstmt.getString(1);
        }catch(Exception e){
            logger.error(formatException(e));
            sReturnValue = e.getMessage();
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        logger.info("************************** FIN InstallmentSalesDAO > validateOrderVepCI**************************");
        return sReturnValue;
    }

    /**
     * @author AMENDEZ
     * @project PRY-1200
     * Metodo   Retorna valor para colocar el check por defecto de forma de pago cuota inicial
     * @return
     */
    public int validatePaymentTermsCI(long swcustomerid,long userid,int npvep) throws SQLException, Exception{
        logger.info("************************** INICIO InstallmentSalesDAO > validatePaymentTermsCI**************************");
        int sReturnValue = 0;
        Connection conn=null;
        OracleCallableStatement cstmt = null;
        String avch_origin= Constante.VEP_ORIGIN_APP;
        try{
            logger.info("swcustomerid         : "+swcustomerid);
            logger.info("userid               : "+userid);
            logger.info("npvep                : "+npvep);
            logger.info("avch_origin          : "+avch_origin);

            String sqlStr =  " { ? = call INVOICE.FNC_GET_PAYMENTTERMSIQ_EVAL( ?, ?, ?, ? ) } ";

            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.registerOutParameter(1, Types.INTEGER);

            cstmt.setLong(2, swcustomerid);
            cstmt.setLong(3, userid);
            cstmt.setInt(4,npvep);
            cstmt.setString(5, avch_origin);

            cstmt.executeQuery();
            sReturnValue = cstmt.getInt(1);
        }catch(Exception e){
            logger.error(formatException(e));
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        logger.info("************************** FIN InstallmentSalesDAO > validatePaymentTermsCI**************************");
        return sReturnValue;
    }

    /**
     * @author AMENDEZ
     * @project PRY-1200
     * Metodo   Valida si la especificacion esta configurada para VEP
     *          flag 1, Aplica
     *          flag 0, No aplica
     *          flag -1, Errores
     * @return
     */
    public String validateSpecificationVep(int anum_npspecificationid) throws SQLException, Exception{
        logger.info("************************** INICIO InstallmentSalesDAO > validateSpecificationVep(String avch_origen,int anum_npspecificationid)**************************");
        String sReturnValue = null;
        Connection conn=null;
        OracleCallableStatement cstmt = null;
        String avch_origin= Constante.VEP_ORIGIN_APP;
        try{
            logger.info("avch_origen              : "+avch_origin);
            logger.info("anum_npspecificationid   : "+anum_npspecificationid);

            String sqlStr =  " { ? = call INVOICE.FNC_GET_SPECIFICATION_VEP( ?, ?) } ";

            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.registerOutParameter(1, Types.VARCHAR);

            cstmt.setString(2, avch_origin);
            cstmt.setInt(3, anum_npspecificationid);


            cstmt.executeQuery();
            sReturnValue = cstmt.getString(1);
            logger.info("sReturnValue   : "+sReturnValue);
        }catch(Exception e){
            logger.error(formatException(e));
            sReturnValue = e.getMessage();
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        logger.info("************************** FIN InstallmentSalesDAO > validateSpecificationVep(String avch_origen,int anum_npspecificationid)**************************");
        return sReturnValue;
    }

    /**
     * @author AMENDEZ
     * @project PRY-1200
     * Metodo   Obtiene valor de configuracion de tablas VEP
     * @return
     */
    public String getConfigValueVEP(String avch_npvaluedesc) throws SQLException, Exception{
        logger.info("************************** INICIO InstallmentSalesDAO > getConfigValueVEP(String avch_npvaluedesc)**************************");
        String sReturnValue = null;
        Connection conn=null;
        OracleCallableStatement cstmt = null;
        String avch_origin= Constante.VEP_ORIGIN_APP;
        try{
            logger.info("avch_origen              : "+avch_origin);

            String sqlStr =  " { ? = call INVOICE.FNC_GET_CONFIG_VALUE_VEP( ?, ?) } ";

            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.registerOutParameter(1, Types.VARCHAR);

            cstmt.setString(2, avch_npvaluedesc);
            cstmt.setString(3, avch_origin);

            cstmt.executeQuery();
            sReturnValue = cstmt.getString(1);
            logger.info("sReturnValue   : "+sReturnValue);
        }catch(Exception e){
            logger.error(formatException(e));
            sReturnValue = e.getMessage();
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        logger.info("************************** FIN InstallmentSalesDAO > getConfigValueVEP(String avch_npvaluedesc)**************************");
        return sReturnValue;
    }
}