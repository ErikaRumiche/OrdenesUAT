package pe.com.nextel.dao;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import oracle.sql.DATE;
import oracle.sql.ARRAY;
import oracle.sql.NUMBER;
import oracle.sql.STRUCT;

import org.apache.commons.lang.StringUtils;

import pe.com.nextel.bean.*;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;

//import oracle.jdbc.driver.CallableStatement;
//import oracle.jdbc.OracleCallableStatement;
import pe.com.nextel.bean.PlanTarifarioBean;

public class ProductDAO extends GenericDAO {

  public HashMap getModelList() throws Exception,SQLException{
     
     ProductBean objProductBean = new ProductBean();
     ArrayList objArray = new ArrayList();
     Connection conn = null; 
     ResultSet rs=null;
     OracleCallableStatement cstmt = null;
     String strMessage = null;
     HashMap objHashMapResultado = new HashMap();        
     String strSql = "BEGIN NP_ORDERS06_PKG.SP_GET_MODEL_LST(?,?); END;";
     try{
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement) conn.prepareCall(strSql);        
        cstmt.registerOutParameter(1, OracleTypes.CURSOR);
        cstmt.registerOutParameter(2, OracleTypes.VARCHAR);        
        cstmt.executeUpdate();        
        strMessage  = cstmt.getString(2);        
        if( strMessage == null ){        
           rs = (ResultSet)cstmt.getObject(1);
           while (rs.next()) {
              objProductBean = new ProductBean();   
              objProductBean.setNpproductid(Long.parseLong(rs.getString("npproductid")));
              objProductBean.setNpname(rs.getString("npname"));
              objArray.add(objProductBean);
           }
        }
     }
     catch (Exception e) {
        throw new Exception(e);
     }
     finally{
		closeObjectsDatabase(conn,cstmt,rs);
        /*if (cstmt != null)
            cstmt.close();
        if (conn != null)
            conn.close();   
        if (rs != null)
            rs.close(); */ 
     }         
     objHashMapResultado.put("strMessage",strMessage);
     objHashMapResultado.put("objArrayModel",objArray);     
     return objHashMapResultado;                                                                
   }
  
  public HashMap getModelListByCategory(int specId) throws Exception,SQLException{
     
     ProductBean objProductBean = new ProductBean();
     ArrayList objArray = new ArrayList();
     Connection conn = null; 
     ResultSet rs=null;
     OracleCallableStatement cstmt = null;
     String strMessage = null;
     HashMap objHashMapResultado = new HashMap();        
     String strSql = "BEGIN NP_ORDERS06_PKG.SP_GET_MODEL_LST_BY_SPEC(?,?,?); END;";
     try{
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement) conn.prepareCall(strSql); 
		
		cstmt.setInt(1,specId);
        cstmt.registerOutParameter(2, OracleTypes.CURSOR);
        cstmt.registerOutParameter(3, OracleTypes.VARCHAR);        
        cstmt.executeUpdate();        
        strMessage  = cstmt.getString(3);        
        if( strMessage == null ){        
           rs = (ResultSet)cstmt.getObject(2);
           while (rs.next()) {
              objProductBean = new ProductBean();   
              objProductBean.setNpproductid(Long.parseLong(rs.getString("npproductid")));
              objProductBean.setNpname(rs.getString("npname"));
              objArray.add(objProductBean);
           }
        }
     }
     catch (Exception e) {
        throw new Exception(e);
     }
     finally{
		closeObjectsDatabase(conn,cstmt,rs);
        /*if (cstmt != null)
            cstmt.close();
        if (conn != null)
            conn.close();   
        if (rs != null)
            rs.close(); */ 
     }         
     objHashMapResultado.put("strMessage",strMessage);
     objHashMapResultado.put("objArrayModel",objArray);     
     return objHashMapResultado;                                                                
   }
   
  public HashMap getTableValue(String strNameTable) throws Exception,SQLException{
     
     TableBean objTableBean = new TableBean();
     Connection conn = null; 
     ResultSet rs=null;
     OracleCallableStatement cstmt = null;
     String strMessage = null;
     HashMap objHashMapResultado = new HashMap();   
     
     String strSql = "BEGIN SWBAPPS.SPI_GET_VALUE(?,?,?); END;";
     try{
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement) conn.prepareCall(strSql);
        
        cstmt.setString(1,strNameTable);
        cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
        
        cstmt.executeUpdate();
        
        strMessage  = cstmt.getString(2);
        
        if( strMessage == null ){
        objTableBean.setNpValue(cstmt.getString(3));
        }
     }     
     catch (Exception e) {
        throw new Exception(e);
     }
     finally{
			closeObjectsDatabase(conn,cstmt,null);
        /*if (cstmt != null)
            cstmt.close();
        if (conn != null)
            conn.close();*/   
     }
     objHashMapResultado.put("strMessage",strMessage);
     objHashMapResultado.put("objTableBean",objTableBean);
     
     return objHashMapResultado;                                                                
   }

  public HashMap getProductBolsa(long lngCustomerId,long lngSiteId) throws Exception,SQLException{
     
     ProductBean objProductBean = new ProductBean();
     Connection conn = null; 
     ResultSet rs=null;
     OracleCallableStatement cstmt = null;
     String strMessage = null;
     HashMap objHashMapResultado = new HashMap();   
     
     String strSql = "BEGIN ORDERS.SPI_GET_PRODUCTBOLSA_BY_CUST(?,?,?,?,?,?,?,?,?); END;";
     try{
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement) conn.prepareCall(strSql);          
        if(lngSiteId > 0){
          cstmt.setLong(1,lngSiteId);
          cstmt.setString(2,"S");
        }else{
          cstmt.setLong(1,lngCustomerId);
          cstmt.setString(2,"C");
        }
        cstmt.registerOutParameter(3, OracleTypes.NUMBER);
        cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(5, OracleTypes.NUMBER);
        cstmt.registerOutParameter(6, OracleTypes.NUMBER);
        cstmt.registerOutParameter(7, OracleTypes.NUMBER);
        cstmt.registerOutParameter(8, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(9, OracleTypes.VARCHAR);
        
        cstmt.executeUpdate();
                
		  strMessage  = cstmt.getString(9);
        
        if( strMessage == null ){
           objProductBean.setNpproductid(cstmt.getLong(3));
           objProductBean.setNpproductname(cstmt.getString(4));
           objProductBean.setNpminute(cstmt.getInt(5));
           objProductBean.setNpminuteprice(cstmt.getDouble(6));
		   objProductBean.setNpsolutionid(cstmt.getInt(7));
		   objProductBean.setNpLevel(cstmt.getInt(8));
        }
     }
     catch (Exception e) {
        throw new Exception(e);
     }
     finally{
			closeObjectsDatabase(conn,cstmt,null); 
     }
     objHashMapResultado.put("strMessage",strMessage);
     objHashMapResultado.put("objProductBean",objProductBean);
     
     return objHashMapResultado;                                                                
   }
   
  /**
   * Purpose: Obtiene el producto bolsa segun id de cliente, id site y el id de la solucion
   * Developer       Fecha       Comentario
   * =============   ==========  ======================================================================
   * RARANA          21/07/2009  Creación 
   * @throws java.sql.SQLException
   * @throws java.lang.Exception
   * @return 
   * @param lngCustomerId
   * @param lngSiteId
   * @param lngSolutionId
   */
   public HashMap getProductBolsa(long lngCustomerId,long lngSiteId, long lngSolutionId) throws Exception,SQLException{     
     ProductBean objProductBean = new ProductBean();
     Connection conn = null; 
     ResultSet rs=null;
     OracleCallableStatement cstmt = null;
     String strMessage = null;
     HashMap objHashMapResultado = new HashMap();   
     
     String strSql = "BEGIN ORDERS.NP_ORDERS17_PKG.SP_GET_PRODUCTBOLSA_BY_CUSTSOL(?,?,?,?,?,?,?,?,?,?); END;";
     
     try{
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement) conn.prepareCall(strSql);          
        if(lngSiteId > 0){
          cstmt.setLong(1,lngSiteId);
          cstmt.setString(2,"S");
        }else{
          cstmt.setLong(1,lngCustomerId);
          cstmt.setString(2,"C");
        }     
        cstmt.setLong(3, lngSolutionId);
        cstmt.registerOutParameter(4, OracleTypes.NUMBER);
        cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(6, OracleTypes.NUMBER);
		    cstmt.registerOutParameter(7, OracleTypes.NUMBER);
        cstmt.registerOutParameter(8, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(9, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(10, OracleTypes.VARCHAR);
        
        cstmt.executeUpdate();
                
		    strMessage  = cstmt.getString(10);
        
        if( strMessage == null ){
           objProductBean.setNpproductid(cstmt.getLong(4));
           objProductBean.setNpproductname(cstmt.getString(5));
           objProductBean.setNpminute(cstmt.getInt(6));
           objProductBean.setNpminuteprice(cstmt.getDouble(7));
           objProductBean.setNpLevel(cstmt.getInt(8));
           objProductBean.setNpplanid(cstmt.getLong(9));
          
			     objProductBean.setNpsolutionid(lngSolutionId);
        }
     }
     catch (Exception e) {
        throw new Exception(e);
     }
     finally{
			closeObjectsDatabase(conn,cstmt,null); 
     }
     objHashMapResultado.put("strMessage",strMessage);
     objHashMapResultado.put("objProductBean",objProductBean);
     
     return objHashMapResultado;                                                                
   }

  public HashMap getProductDetailImei(String strImei,long longCustomerId, long lSpecificationId) throws Exception,SQLException{
      
     ProductBean objProductBean = new ProductBean();;
     Connection conn = null; 
     ResultSet rs=null;
     OracleCallableStatement cstmt = null;
     String strMessage = null;
     HashMap objHashMapResultado = new HashMap();   
     
     String strSql = "BEGIN WEBSALES.NP_BSCS_UTIL04_PKG.SP_GET_DETAIL_BY_IMEI(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); END;";
     try{
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement) conn.prepareCall(strSql);
        
        cstmt.setString(1,strImei);
        
        if( longCustomerId==0 )
         cstmt.setNull(2,OracleTypes.NUMBER);
        else
         cstmt.setLong(2,longCustomerId);
         
        cstmt.setLong(3,lSpecificationId); 
         
        cstmt.registerOutParameter(4, OracleTypes.NUMBER);
        cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(6, OracleTypes.NUMBER);
        cstmt.registerOutParameter(7, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(8, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(9, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(10, OracleTypes.NUMBER);
        cstmt.registerOutParameter(11, OracleTypes.NUMBER);
        cstmt.registerOutParameter(12, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(13, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(14, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(15, OracleTypes.NUMBER);
        cstmt.registerOutParameter(16, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(17, OracleTypes.NUMBER);
        cstmt.registerOutParameter(18, OracleTypes.NUMBER);
        cstmt.registerOutParameter(19, OracleTypes.VARCHAR); 
        cstmt.registerOutParameter(20, OracleTypes.VARCHAR);
        cstmt.executeUpdate();
        
        strMessage  = cstmt.getString(20);
        
        objProductBean.setNpcontractid(cstmt.getLong(4));
        objProductBean.setNpcostatus(cstmt.getString(5));
        objProductBean.setNpplanid(cstmt.getInt(6));
        objProductBean.setNpplan(cstmt.getString(7));
        objProductBean.setNpcd_sim(cstmt.getString(8));
        objProductBean.setNpequipmentimei(cstmt.getString(9));
        objProductBean.setNpproductlineid(cstmt.getLong(10));
        objProductBean.setNpproductid(cstmt.getLong(11));     
        objProductBean.setNpproductmodel(cstmt.getString(12)==null?"":cstmt.getString(12));
        objProductBean.setNpequipment(cstmt.getString(13));
        objProductBean.setNpwarranty(cstmt.getString(14));
        objProductBean.setNpoccurrence(cstmt.getInt(15));
        objProductBean.setNpssaa_contratado(cstmt.getString(16));
        objProductBean.setNpcustomerid_subscriber(cstmt.getLong(17));
        objProductBean.setNpcustomerid_paymntresp(cstmt.getLong(18));
        objProductBean.setNpcustcode_paymntresp(cstmt.getString(19));
     }
     catch (Exception e) {
        throw new Exception(e);
     }
     finally{
			closeObjectsDatabase(conn,cstmt,null);
        /*if (cstmt != null)
            cstmt.close();
        if (conn != null)
           conn.close();   */
     }   
     objHashMapResultado.put("strMessage",strMessage);
     objHashMapResultado.put("objProductBean",objProductBean);
     
     return objHashMapResultado;                                                                
   }
   
   public HashMap getProductDetail(long longProductId) throws Exception,SQLException{
      
     ProductBean objProductBean = new ProductBean();;
     Connection conn = null; 
     ResultSet rs=null;
     OracleCallableStatement cstmt = null;
     String strMessage = null;
     HashMap objHashMapResultado = new HashMap();   
     
     String strSql = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_GET_PRODUCT_DET(?,?,?,?); END;";
     try{
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement) conn.prepareCall(strSql);
        
        cstmt.setLong(1,longProductId);
        cstmt.registerOutParameter(2, OracleTypes.NUMBER);
        
        cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
        
        cstmt.executeUpdate();
        
        strMessage  = cstmt.getString(4);
        
        if( strMessage != null )
        throw new Exception(strMessage);
        
        objProductBean.setNpproductlineid(cstmt.getLong(2));
        objProductBean.setNpproductname(cstmt.getString(3));
     }
     catch(Exception e){
        throw new Exception(e);
     }
     finally{
			closeObjectsDatabase(conn,cstmt,null);
        /*if (cstmt != null)
            cstmt.close();
        if (conn != null)
           conn.close();   */
     }
     objHashMapResultado.put("strMessage",strMessage);
     objHashMapResultado.put("objProductBean",objProductBean);
     
     return objHashMapResultado;                                                                
   }
   
   
   /**
   Method : getProductList
   Purpose: Obtener los valores de LINEA PRODUCTOS
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Lee Rosales     22/09/2007  Creación
   */
   
  public static ArrayList getProductList(ProductBean productBean, String strMessage) throws SQLException, Exception{
      
   ArrayList list = null;
   ProductBean pBean = null;
   Connection conn = null; 
   ResultSet rs=null;
   OracleCallableStatement cstm = null;
        
   String strSql = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_GET_PRODUCT_LIST(?,?,?,?,?); END;";
   try{
      conn = Proveedor.getConnection();
      cstm = (OracleCallableStatement) conn.prepareCall(strSql);
               
      cstm.setLong(1,productBean.getNpproductid());
      cstm.setLong(2,productBean.getNpproductlineid());
      cstm.setLong(3,productBean.getNpsolutionid());
      
      cstm.registerOutParameter(4, OracleTypes.CURSOR);
      cstm.registerOutParameter(5, Types.VARCHAR);
      
      cstm.execute();
   
      strMessage  = cstm.getString(5);
      
      if( strMessage == null ){      
         rs = (ResultSet)cstm.getObject(4);
         list = new ArrayList();      
         while (rs.next()) {
            pBean = new ProductBean();    
            pBean.setNpproductid(rs.getInt("npproductid"));  
            pBean.setNpproductlineid(rs.getInt("npproductlineid"));
            pBean.setNpname(rs.getString("npname"));
            pBean.setNpstatus(rs.getString("npstatus"));
            list.add(pBean);
         }
      }
   }   
   catch (Exception e) {
      throw new Exception(e);
   }
   finally{
      closeObjectsDatabase(conn,cstm,rs);
   }    
   return list;                                                                
  }
   
	/**
     * Motivo: Obtener los valores de Línea de Productos
	 * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
	 * [Creación]
     * <br>Modificado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
	 * [Manejo de SQL Types]
     * <br>Fecha: 22/09/2007
     * @return    HashMap que contiene: Lista de Producto, String vacío o que contiene el mensaje de error. 
     */
	public HashMap getProductList(ProductBean objProductParamBean) throws SQLException, Exception {
        logger.info("*************************** INICIO ProductDAO >getProductList(ProductBean objProductParamBean)***************************");
		HashMap hshDataMap = new HashMap();
		ArrayList arrProductList = new ArrayList();
		Connection conn = null; 		
      OracleCallableStatement cstmt = null;
		String strMessage = null;
		String strSql = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_GET_PRODUCT_LST(?,?,?,?,?,?,?,?,?,?,?); END;";
    
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(strSql);		

          logger.info("objProductParamBean.toString() :"+objProductParamBean.toString());

         if(objProductParamBean.getNpproductid() == 0) {
            cstmt.setNull(1, OracleTypes.NUMBER);
         } else {
            cstmt.setLong(1, objProductParamBean.getNpproductid());
         }
         if(objProductParamBean.getNpproductlineid() == 0) {
            cstmt.setNull(2, OracleTypes.NUMBER);
         } else {
            cstmt.setLong(2, objProductParamBean.getNpproductlineid());
         }
         cstmt.setString(3, objProductParamBean.getNpmodality());
         cstmt.setLong(4, objProductParamBean.getNpcategoryid());
         cstmt.setNull(5,OracleTypes.VARCHAR);
         if(objProductParamBean.getNpinventorycode() != null){
            if(objProductParamBean.getNpinventorycode().equals("0")) {
               cstmt.setNull(6, OracleTypes.NUMBER);
            } else {
                 cstmt.setString(6, objProductParamBean.getNpinventorycode());
            }
         }else cstmt.setNull(6, OracleTypes.NUMBER);  
         cstmt.setString(7,objProductParamBean.getNpproductname());
         cstmt.setNull(8,OracleTypes.NUMBER);
         if ( objProductParamBean.getNpsolutionid() == 0){
           cstmt.setNull(9,OracleTypes.NUMBER);
         }else{
            cstmt.setLong(9, objProductParamBean.getNpsolutionid());
         }
         
         cstmt.registerOutParameter(10, OracleTypes.ARRAY, "PRODUCT.TT_PRODUCT_LST");
         cstmt.registerOutParameter(11, Types.VARCHAR);
         cstmt.execute();
         strMessage  = cstmt.getString(11);
         if(StringUtils.isNotBlank(strMessage)){
            throw new Exception(strMessage);
         }		
         ARRAY aryProductList = (ARRAY)cstmt.getObject(10);
         OracleResultSet adrs = (OracleResultSet) aryProductList.getResultSet();
         logger.debug("LENGTH: "+aryProductList.getOracleArray().length);
         while(adrs.next()) {
            STRUCT stcProductPrice = adrs.getSTRUCT(2);
            ProductBean objProductBean = new ProductBean();
            objProductBean = new ProductBean();
         
            objProductBean.setNpproductid(MiUtil.defaultBigDecimal(stcProductPrice.getAttributes()[0], new BigDecimal(0)).longValue());
            objProductBean.setNpproductname(MiUtil.defaultString(stcProductPrice.getAttributes()[2], ""));
            objProductBean.setNpminuteprice(MiUtil.defaultBigDecimal(stcProductPrice.getAttributes()[14], new BigDecimal(0)).doubleValue());
            objProductBean.setNpminute(MiUtil.defaultBigDecimal(stcProductPrice.getAttributes()[15], new BigDecimal(0)).intValue());
         
            arrProductList.add(objProductBean);
         }
      }
      catch(Exception e){
          logger.error(e);
        throw new Exception(e);
     }
     finally{
			closeObjectsDatabase(conn,cstmt,null);

     }
		hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
      hshDataMap.put("objArrayList", arrProductList);
        logger.info("*************************** FIN ProductDAO >getProductList(ProductBean objProductParamBean)***************************");
      return hshDataMap;                                                               
   }
   
    /**
     * Motivo: Obtener los valores de Línea de Precios
	 * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
	 * [Creación]
     * <br>Modificado por: <a href="mailto:richard.delosreyes@nextel.com.pe">Richard De los Reyes</a>
	 * [Manejo de SQL Types]
     * <br>Fecha: 22/09/2007
     * @return    HashMap que contiene: Lista de Precios de Producto, String vacío o que contiene el mensaje de error.
     * ORIGINAL*/
	public HashMap getProductPriceList(ProductBean objProductParamBean) throws SQLException, Exception {
        logger.info("*************************** INICIO ProductDAO > getProductPriceList(ProductBean objProductParamBean)***************************");
		HashMap hshDataMap = new HashMap();
		ArrayList arrProductPriceList = new ArrayList();
		Connection conn = null; 		
    OracleCallableStatement cstmt = null;
		String strMessage = null; 
    
		//PRY-1200 | INICIO: AMENDEZ
        logger.info("objProductParamBean.toString() :"+objProductParamBean.toString());
        //String strSql = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_GET_PRODUCT_PRICE_LST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); END;";
        String strSql = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_EM_AC_PRODUCT_PRICE_LST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); END;";
        //PRY-1200 | FIN: AMENDEZ
    
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(strSql);
         if(objProductParamBean.getNpcustomerid() == 0) {
            cstmt.setNull(1, OracleTypes.NUMBER);
         } else{
            cstmt.setLong(1, objProductParamBean.getNpcustomerid());
         }
         
         if(objProductParamBean.getNpproductid_new() == 0) {
            cstmt.setNull(2, OracleTypes.NUMBER);
         } else{
            cstmt.setLong(2, objProductParamBean.getNpproductid_new());
         }
                     
         if(objProductParamBean.getNpproductid_old() == 0) {
            cstmt.setNull(3, OracleTypes.NUMBER);
         } else{
            cstmt.setLong(3, objProductParamBean.getNpproductid_old());
         }
                          
         cstmt.setString(4, objProductParamBean.getNpmodality_new());
         cstmt.setString(5, objProductParamBean.getNpmodality_old());

         //Devolución
         if(StringUtils.isNotBlank(objProductParamBean.getNpflg_return())) {
            cstmt.setInt(6, MiUtil.parseInt(objProductParamBean.getNpflg_return()));
         } else{
            cstmt.setNull(6, OracleTypes.NUMBER);
         }          
         
          //Garantía
         if(StringUtils.isNotBlank(objProductParamBean.getNpflg_garanty())) {
            cstmt.setInt(7, MiUtil.parseInt(objProductParamBean.getNpflg_garanty()));
         } else{
            cstmt.setNull(7, OracleTypes.NUMBER);
         }

          //Ocurrencia
         if(objProductParamBean.getNpoccurrence() == -1) {
            cstmt.setNull(8, OracleTypes.NUMBER);
         } else{
            cstmt.setLong(8, objProductParamBean.getNpoccurrence());
         }
         
         cstmt.setString(9, String.valueOf(objProductParamBean.getNpsolutionid()));         

        //Plan Tarifario Nuevo
         if(objProductParamBean.getNpplanid() == 0) {
            cstmt.setNull(10,OracleTypes.VARCHAR);
         } else{
            cstmt.setString(10, String.valueOf(objProductParamBean.getNpplanid()));
         }
        
         //Categoría
         if(objProductParamBean.getNpcategoryid() == 0) {
            cstmt.setNull(11,OracleTypes.NUMBER);
         } else{
            cstmt.setLong(11,objProductParamBean.getNpcategoryid());
         }
         cstmt.setString(12, objProductParamBean.getNpquantity());
         cstmt.setString(13, objProductParamBean.getNpflg_option());
        
        //<!--START MSOTO: 15-07-2014 SAR N_O000027196 Portabilidad Prepago--> //Se agrega factura prepago TDECONV029
         if(objProductParamBean.getNpcategoryid() == 2002 || objProductParamBean.getNpcategoryid() == 2069 || objProductParamBean.getNpcategoryid() == Constante.SPEC_PREPAGO_TDE
                 || objProductParamBean.getNpcategoryid() == Constante.SPEC_REPOSICION_PREPAGO_TDE) { //Se agrega reposicion prepago tde - TDECONV034
            cstmt.setNull(14,OracleTypes.NUMBER);
         } else{
            cstmt.setString(14, objProductParamBean.getNpmodel());
         } 
         
         if(objProductParamBean.getNpsiteid() == 0) {
            cstmt.setNull(15, OracleTypes.NUMBER);
         } else{
            cstmt.setLong(15, objProductParamBean.getNpsiteid());
         }
         
         cstmt.setString(16, objProductParamBean.getNpphonenumber());
         cstmt.setLong(17, objProductParamBean.getSalesStructureOriginalId());
         
         if(objProductParamBean.getNpflagvep()!=-1) {
            cstmt.setInt(18,objProductParamBean.getNpflagvep());
         } else {
            cstmt.setInt(18, OracleTypes.NULL);
         }
         
         if(objProductParamBean.getNpnumcuotas()!=-1) {
            cstmt.setInt(19,objProductParamBean.getNpnumcuotas());
         } else {
            cstmt.setInt(19, OracleTypes.NULL);
         }
         
          //PRY-1200 | INICIO: AMENDEZ
          if(objProductParamBean.getOrderId()==null) {
              cstmt.setInt(20, OracleTypes.NULL);
          } else {
              cstmt.setInt(20,MiUtil.parseInt(objProductParamBean.getOrderId()));
          }
          if(objProductParamBean.getNpclassid()==null) {
              cstmt.setInt(21, OracleTypes.NULL);
          } else {
              cstmt.setInt(21,objProductParamBean.getNpclassid());
          }
          //PRY-1200 | FIN: AMENDEZ

         cstmt.registerOutParameter(22, OracleTypes.ARRAY, "ORDERS.TT_PRODUCT_PRICE_LST");
         cstmt.registerOutParameter(23, Types.VARCHAR);
         
         cstmt.executeUpdate();
        
         strMessage  = cstmt.getString(23);
         if(StringUtils.isNotBlank(strMessage)) {
            throw new Exception(strMessage);
         }
        
         ARRAY aryProductPriceList = (ARRAY)cstmt.getObject(22);
         OracleResultSet adrs = (OracleResultSet) aryProductPriceList.getResultSet();
         while(adrs.next()) {
            int i = 0;
            STRUCT stcProductPrice = adrs.getSTRUCT(2);
             if ("".equals(MiUtil.defaultString(stcProductPrice.getAttributes()[i+1], "")))  
            {
              arrProductPriceList = new ArrayList();
            }else{
                                             
                     
            ProductPriceBean objProductPriceBean = new ProductPriceBean();
                logger.info(i +"-"+stcProductPrice.getAttributes()[i]);
            objProductPriceBean.setNpproductid(MiUtil.defaultBigDecimal(stcProductPrice.getAttributes()[i++], new BigDecimal(0)).longValue());
                logger.info(i +"-"+stcProductPrice.getAttributes()[i]);
            objProductPriceBean.setNpcurrency(MiUtil.defaultString(stcProductPrice.getAttributes()[i++], ""));
                logger.info(i +"-"+stcProductPrice.getAttributes()[i]);
            objProductPriceBean.setNporiginalprice(MiUtil.defaultBigDecimal(stcProductPrice.getAttributes()[i++], new BigDecimal(0)).doubleValue());
                logger.info(i +"-"+stcProductPrice.getAttributes()[i]);
            objProductPriceBean.setNppriceonetime(MiUtil.defaultBigDecimal(stcProductPrice.getAttributes()[i++], new BigDecimal(0)).doubleValue());
                logger.info(i +"-"+stcProductPrice.getAttributes()[i]);
            objProductPriceBean.setNppricerecurring(MiUtil.defaultBigDecimal(stcProductPrice.getAttributes()[i++], new BigDecimal(0)).doubleValue());
                logger.info(i +"-"+stcProductPrice.getAttributes()[i]);
            objProductPriceBean.setNpobjectid(MiUtil.defaultBigDecimal(stcProductPrice.getAttributes()[i++], new BigDecimal(0)).longValue()); 
                logger.info(i +"-"+stcProductPrice.getAttributes()[i]);
            objProductPriceBean.setNpobjecttype(MiUtil.defaultString(stcProductPrice.getAttributes()[i++], ""));     
                logger.info(i +"-"+stcProductPrice.getAttributes()[i]);
            objProductPriceBean.setNpobjectitemid(MiUtil.defaultBigDecimal(stcProductPrice.getAttributes()[i++], new BigDecimal(0)).longValue()); 
           
           arrProductPriceList.add(objProductPriceBean);  
            
         }
     }
     }
     catch(Exception e){        
         logger.error(e);
        throw new Exception(e);
     }
     finally{
			closeObjectsDatabase(conn,cstmt,null);

     }
		 
	   hshDataMap.put(Constante.MESSAGE_OUTPUT, strMessage);
     hshDataMap.put("objArrayList", arrProductPriceList);
        logger.info("tamaño del arreglo obtenido"+arrProductPriceList.size());

        logger.info("*************************** FIM ProductDAO > getProductPriceList(ProductBean objProductParamBean)***************************");
     return hshDataMap;                                                                
   }
   
   
     
   /**
   Method : getDetailByPhone
   Purpose: Obtener los valores de LINEA PRODUCTOS
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Lee Rosales     22/09/2007  Creación
   Karen Salvador  23/01/2008  Modificación en la salida de parámetros (aumenta 1)
   Karen Salvador  19/03/2008  Aumenta un parametro de entrada long longSiteId
   Karen Salvador  15/05/20098 Aumenta un parametro de salida SolutionId
   */
   public HashMap getDetailByPhone(String strPhoneNumber,long longCustomerId, long longSiteId, long lSepecificationId) throws Exception,SQLException{
       
       Connection conn = null; 
       ResultSet rs=null;
       OracleCallableStatement cstmt = null;
       String strMessage = null;
       ProductBean objProductBean = new ProductBean();
       HashMap objHashMapResultado = new HashMap();   
       
       String strSql = "{call WEBSALES.NP_BSCS_UTIL04_PKG.SP_GET_DETAIL_BY_PHONE(?,?,?,?,?,?,"
                                                                              + "?,?,?,?,?,"
                                                                              + "?,?,?,?,?,"
                                                                              + "?,?,?,?,?,?,?)}";
       try{                                                                  
          conn = Proveedor.getConnection();
          cstmt = (OracleCallableStatement) conn.prepareCall(strSql);
          
          cstmt.setLong(1,MiUtil.parseLong(strPhoneNumber));
          cstmt.setLong(2,longCustomerId);
          if( longSiteId == 0 )
           cstmt.setNull(3,OracleTypes.NUMBER);
          else
              cstmt.setLong(3,longSiteId);
              
          cstmt.setLong(4,lSepecificationId);                 
          cstmt.registerOutParameter(5, OracleTypes.NUMBER);
          cstmt.registerOutParameter(6, OracleTypes.VARCHAR);
          cstmt.registerOutParameter(7, OracleTypes.NUMBER);
          cstmt.registerOutParameter(8, OracleTypes.VARCHAR);
          cstmt.registerOutParameter(9, OracleTypes.VARCHAR);
          cstmt.registerOutParameter(10, OracleTypes.VARCHAR);
          cstmt.registerOutParameter(11, OracleTypes.NUMBER);
          cstmt.registerOutParameter(12, OracleTypes.NUMBER);
          cstmt.registerOutParameter(13, OracleTypes.VARCHAR);
          cstmt.registerOutParameter(14, OracleTypes.VARCHAR);
          cstmt.registerOutParameter(15, OracleTypes.VARCHAR);
          cstmt.registerOutParameter(16, OracleTypes.NUMBER);
          cstmt.registerOutParameter(17, OracleTypes.VARCHAR);
          cstmt.registerOutParameter(18, OracleTypes.NUMBER);
          cstmt.registerOutParameter(19, OracleTypes.NUMBER);
          cstmt.registerOutParameter(20, OracleTypes.VARCHAR);
          cstmt.registerOutParameter(21, OracleTypes.DATE); 
          cstmt.registerOutParameter(22, OracleTypes.NUMBER); 
          cstmt.registerOutParameter(23, OracleTypes.VARCHAR);          
          cstmt.executeUpdate();          
          strMessage  = cstmt.getString(23);          
          if( strMessage == null ){          
             objProductBean.setNpcontractid(cstmt.getLong(5));
             objProductBean.setNpcostatus(cstmt.getString(6));
             objProductBean.setNpplanid(cstmt.getInt(7));
             objProductBean.setNpplan(cstmt.getString(8));
             objProductBean.setNpcd_sim(cstmt.getString(9));
             objProductBean.setNpequipmentimei(cstmt.getString(10));
             objProductBean.setNpproductlineid(cstmt.getLong(11));
             objProductBean.setNpproductid(cstmt.getLong(12));
             objProductBean.setNpproductmodel(cstmt.getString(13));
             objProductBean.setNpequipment(cstmt.getString(14));
             objProductBean.setNpwarranty(cstmt.getString(15));
             objProductBean.setNpoccurrence(cstmt.getInt(16));
             objProductBean.setNpssaa_contratado(cstmt.getString(17));
             objProductBean.setNpcustomerid_subscriber(cstmt.getLong(18));
             objProductBean.setNpcustomerid_paymntresp(cstmt.getLong(19));
             objProductBean.setNpcustcode_paymntresp(cstmt.getString(20));
             objProductBean.setNpstatusdate(cstmt.getDate(21));   
             objProductBean.setNpsolutionid(cstmt.getInt(22));
          }
       }
       catch (Exception e) {
         throw new Exception(e);
       }
       finally{
			closeObjectsDatabase(conn,cstmt,null);
          /*if (cstmt != null)
             cstmt.close();
          if (conn != null)
             conn.close();   */
       }
       objHashMapResultado.put("strMessage",strMessage);
       objHashMapResultado.put("objProductBean",objProductBean);
       return objHashMapResultado;                                                                
   }
  /**
	Method : getDetailByPhoneBySpecification
	Purpose: Obtener los datos del equipo, por teléfono, si el estado es valido según la especificación
	Developer       		Fecha       Comentario
	=============   		==========  ======================================================================
	Cristian Espinoza   	28/01/2008  Creación
	Karen Salvador  		  19/03/2008  Aumenta un parametro de entrada long longSiteId
	Cristian Espinoza   	11/08/2008  Se agrego el parametro OrderId
        Karen Salvador        15/05/2009  Se agrego el parametro SolutionId
        Carlos Puente           13/08/2009  Se agregan parametros
	*/
	public HashMap getDetailByPhoneBySpecification(String strPhoneNumber,long longCustomerId,long longSiteId,long lSpecificationId, long lOrderId) throws Exception,SQLException{
       
	   Connection conn = null; 
		ResultSet rs=null;
		OracleCallableStatement cstmt = null;
		String strMessage = null;
		ProductBean objProductBean = new ProductBean();
		HashMap objHashMapResultado = new HashMap();        
      System.out.println("[ProductDAO][getDetailByPhoneBySpecification][Inicio]");
      System.out.println("[ProductDAO][getDetailByPhoneBySpecification][Inicio]:strPhoneNumber:"+strPhoneNumber);
      System.out.println("[ProductDAO][getDetailByPhoneBySpecification][Inicio]:longCustomerId:"+longCustomerId);
      System.out.println("[ProductDAO][getDetailByPhoneBySpecification][Inicio]:longSiteId:"+longSiteId);
      System.out.println("[ProductDAO][getDetailByPhoneBySpecification][Inicio]:lSpecificationId:"+lSpecificationId);
      System.out.println("[ProductDAO][getDetailByPhoneBySpecification][Inicio]:lOrderId:"+lOrderId);
		if(logger.isDebugEnabled()){		
			logger.debug("Inicio - [ProductDAO][getDetailByPhoneBySpecification]");
			logger.debug("strPhoneNumber: "+strPhoneNumber);
			logger.debug("longCustomerId: "+longCustomerId+"");
			logger.debug("longSiteId: "+longSiteId+"");
			logger.debug("lOrderId: "+lOrderId+"");			
		}	 	  
       //cpuente  
		String strSql = "{call ORDERS.NP_ORDERS07_PKG.SP_GET_DETAIL_BYPHONE_BYSPECIF(?,?,?,?,?,?,?,?,?,"
                                                                            + "?,?,?,?,?,?,?,"
                                                                            + "?,?,?,?,?,?,"
                                                                            + "?,?,?,?,?,?,?,?,?,?,?,?)}";//FPICOY Se agregó un campo adicional
                          
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(strSql);
          
         cstmt.setString(1,strPhoneNumber);
         cstmt.setLong(2,longCustomerId);
         if( longSiteId == 0 )
            cstmt.setNull(3,OracleTypes.NUMBER);
         else
            cstmt.setLong(3,longSiteId);
         cstmt.setLong(4,lSpecificationId);
         cstmt.setLong(5,lOrderId); 
			
         cstmt.registerOutParameter(6, OracleTypes.NUMBER);
         cstmt.registerOutParameter(7, OracleTypes.VARCHAR);
         cstmt.registerOutParameter(8, OracleTypes.NUMBER);
         cstmt.registerOutParameter(9, OracleTypes.VARCHAR);
         cstmt.registerOutParameter(10, OracleTypes.VARCHAR);
         cstmt.registerOutParameter(11, OracleTypes.VARCHAR);
         cstmt.registerOutParameter(12, OracleTypes.NUMBER);
         cstmt.registerOutParameter(13, OracleTypes.NUMBER);
         cstmt.registerOutParameter(14, OracleTypes.VARCHAR);
         cstmt.registerOutParameter(15, OracleTypes.VARCHAR);
         cstmt.registerOutParameter(16, OracleTypes.VARCHAR);
         cstmt.registerOutParameter(17, OracleTypes.NUMBER);
         cstmt.registerOutParameter(18, OracleTypes.VARCHAR);
         cstmt.registerOutParameter(19, OracleTypes.NUMBER);
         cstmt.registerOutParameter(20, OracleTypes.NUMBER);
         cstmt.registerOutParameter(21, OracleTypes.VARCHAR);
         cstmt.registerOutParameter(22, OracleTypes.DATE);     
         cstmt.registerOutParameter(23, OracleTypes.NUMBER); 
         cstmt.registerOutParameter(24, OracleTypes.NUMBER);    //cpuente    
         cstmt.registerOutParameter(25, OracleTypes.VARCHAR);   //cpuente  
         cstmt.registerOutParameter(26, OracleTypes.VARCHAR);   //cpuente 
         cstmt.registerOutParameter(27, OracleTypes.VARCHAR);   //cpuente 
         cstmt.registerOutParameter(28, OracleTypes.VARCHAR);
         cstmt.registerOutParameter(29, OracleTypes.NUMBER);
         cstmt.registerOutParameter(30, OracleTypes.VARCHAR);
         cstmt.registerOutParameter(31, OracleTypes.VARCHAR);
         cstmt.registerOutParameter(32, OracleTypes.NUMBER);
         cstmt.registerOutParameter(33, OracleTypes.VARCHAR);//FPICOY Se agregó el campo para la garantia extendida.
         cstmt.registerOutParameter(34, OracleTypes.VARCHAR);   
         
          
         cstmt.executeUpdate();
         //dbmsOutput.show();
         //dbmsOutput.close();
         strMessage  = cstmt.getString(34);
   
         if( strMessage == null ){
          
            objProductBean.setNpcontractid(cstmt.getLong(6));
            objProductBean.setNpcostatus(cstmt.getString(7));
            objProductBean.setNpplanid(cstmt.getInt(8));
            objProductBean.setNpplan(cstmt.getString(9));
            objProductBean.setNpcd_sim(cstmt.getString(10));
            objProductBean.setNpequipmentimei(cstmt.getString(11));
            objProductBean.setNpproductlineid(cstmt.getLong(12));
            objProductBean.setNpproductid(cstmt.getLong(13));
            objProductBean.setNpproductmodel(cstmt.getString(14));
            objProductBean.setNpequipment(cstmt.getString(15));
            objProductBean.setNpwarranty(cstmt.getString(16));
            objProductBean.setNpoccurrence(cstmt.getInt(17));
            objProductBean.setNpssaa_contratado(cstmt.getString(18));
            objProductBean.setNpcustomerid_subscriber(cstmt.getLong(19));
            objProductBean.setNpcustomerid_paymntresp(cstmt.getLong(20));
            objProductBean.setNpcustcode_paymntresp(cstmt.getString(21));
            objProductBean.setNpstatusdate(cstmt.getDate(22));  
            objProductBean.setNpsolutionid(cstmt.getInt(23)); 
            objProductBean.setNptiempoequipo(cstmt.getInt(24));  //cpuente   
            objProductBean.setNpestadoContrato(cstmt.getString(25));  //cpuente 
            objProductBean.setNpmotivoEstado(cstmt.getString(26));  //cpuente  
            objProductBean.setNpfechaCambioEstado(cstmt.getString(27));  //cpuente 
            objProductBean.setStrRealModel(cstmt.getString(28));
            objProductBean.setLRealModelId(cstmt.getLong(29));
            objProductBean.setStrRealImei(cstmt.getString(30));
            objProductBean.setStrRealSim(cstmt.getString(31));
            objProductBean.setLRealProductLineId(cstmt.getLong(32));
            objProductBean.setNpguaranteeExtFact(cstmt.getString(33));//FPICOY Se agregó el campo para la garantia extendida.
            
         }
      }
      catch(Exception e){
         System.out.println("[Exception][ProductDAO][getDetailByPhoneBySpecification]"+e.getMessage());
         throw new Exception(e);
      }
		finally{
         System.out.println("[Finally][ProductDAO][getDetailByPhoneBySpecification][Inicio]");
			closeObjectsDatabase(conn,cstmt,rs);
         /*if (rs != null)
            rs.close();
         if (cstmt != null)
            cstmt.close();
         if (conn != null)
            conn.close();*/   
         System.out.println("[Finally][ProductDAO][getDetailByPhoneBySpecification][Fin]");
      }           
      objHashMapResultado.put("strMessage",strMessage);
      objHashMapResultado.put("objProductBean",objProductBean);
      System.out.println("[ProductDAO][getDetailByPhoneBySpecification][Fin]");
      return objHashMapResultado;                                                                
   }
	/**
	Method : getDetailByImeiBySpecification
	Purpose: Obtener los datos del equipo, por IMEI, si el estado es valido según la especificación
	Developer       	Fecha       Comentario
	=============   	==========  ======================================================================
	Cristian Espinoza   29/01/2008  Creación
  Karen Salvador     08/05/2008 Se incrementa la modalidad del equipo como parámetro de salida  (Para validar en caso que la modalida sea Alquiler pRecojo)
  Karen Salvador     15/05/2009 Se incrementa un parametro más de salida a la llamada del procedimiento SP_GET_DETAIL_BYIMEI_BYSPECIF.
     EFLORES         04/09/2017 [TDECONV003-1] Añade nuevo parametro flag de migracion
	*/
	public HashMap getDetailByImeiBySpecification(String strImei,long longCustomerId,long lSpecificationId,String strModalitySell, String strFlagMigration) throws Exception,SQLException{
       
     ProductBean objProductBean = new ProductBean();;
     Connection conn = null; 
     ResultSet rs=null;
     OracleCallableStatement cstmt = null;
     String strMessage = null;
     HashMap objHashMapResultado = new HashMap();      
	  System.out.println("[ProductDAO][getDetailByImeiBySpecification][Inicio]");
	 if(logger.isDebugEnabled()){		
		logger.debug("Inicio - [ProductDAO][getDetailByImeiBySpecification]");
		logger.debug("strImei: "+strImei);
		logger.debug("longCustomerId: "+longCustomerId+"");
		logger.debug("lSpecificationId: "+lSpecificationId+"");
		logger.debug("strModalitySell: "+strModalitySell+"");		
	 }	 //CPUENTE
	 String strSql = "BEGIN ORDERS.NP_ORDERS07_PKG.SP_GET_DETAIL_BYIMEI_BYSPECIF(?,?,?," 
                                                                            + " ?,?,?,?,?,?,?,?,?,?,"
                                                                            + " ?,?,?,?,?,?,?,?,?,?,?); END;";//[TDECONV003-1] EFLORES 04/09/2017
	  try{
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement) conn.prepareCall(strSql);
        
        cstmt.setString(1,strImei);     
        if( longCustomerId==0 )
         cstmt.setNull(2,OracleTypes.NUMBER);
        else
         cstmt.setLong(2,longCustomerId);
       
        cstmt.setLong(3,lSpecificationId);
        cstmt.setString(4,strModalitySell);
        cstmt.registerOutParameter(5, OracleTypes.NUMBER);
        cstmt.registerOutParameter(6, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(7, OracleTypes.NUMBER);
        cstmt.registerOutParameter(8, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(9, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(10, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(11, OracleTypes.NUMBER);
        cstmt.registerOutParameter(12, OracleTypes.NUMBER);
        cstmt.registerOutParameter(13, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(14, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(15, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(16, OracleTypes.NUMBER);
        cstmt.registerOutParameter(17, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(18, OracleTypes.NUMBER);
        cstmt.registerOutParameter(19, OracleTypes.NUMBER);
        cstmt.registerOutParameter(20, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(21, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(22, OracleTypes.NUMBER);
        cstmt.registerOutParameter(23, OracleTypes.VARCHAR);
        cstmt.setString(24,strFlagMigration); //EFLORES [TDECONV003-1]
        cstmt.executeUpdate();
        //dbmsOutput.show();
        //dbmsOutput.close();
        strMessage  = cstmt.getString(23);
        
        System.out.println("strMessage"+strMessage);
        
       if( strMessage == null ){
         objProductBean.setNpcontractid(cstmt.getLong(5));
         objProductBean.setNpcostatus(cstmt.getString(6));
         objProductBean.setNpplanid(cstmt.getInt(7));
         objProductBean.setNpplan(cstmt.getString(8));
         objProductBean.setNpcd_sim(cstmt.getString(9));
         objProductBean.setNpequipmentimei(cstmt.getString(10));
         objProductBean.setNpproductlineid(cstmt.getLong(11));
         objProductBean.setNpproductid(cstmt.getLong(12));		
         objProductBean.setNpproductmodel(cstmt.getString(13)==null?"":cstmt.getString(13)); 		
         objProductBean.setNpequipment(cstmt.getString(14));
         objProductBean.setNpwarranty(cstmt.getString(15));
         objProductBean.setNpoccurrence(cstmt.getInt(16));
         objProductBean.setNpssaa_contratado(cstmt.getString(17));
         objProductBean.setNpcustomerid_subscriber(cstmt.getLong(18));
         objProductBean.setNpcustomerid_paymntresp(cstmt.getLong(19));
         objProductBean.setNpcustcode_paymntresp(cstmt.getString(20)); 
         objProductBean.setNpsolutionid(cstmt.getInt(22));
       } 
       objProductBean.setNpequipmentimeistatus(cstmt.getString(21));
    }
    catch (Exception e) {
      System.out.println("[Exception][ProductDAO][getDetailByImeiBySpecification]"+e.getMessage());
       throw new Exception(e);
    }
    finally{
       System.out.println("[Finally][ProductDAO][getDetailByImeiBySpecification][Inicio]");
		 closeObjectsDatabase(conn,cstmt,null);
       /*if (cstmt != null)
          cstmt.close();
       if (conn != null)
          conn.close();*/   
       System.out.println("[Finally][ProductDAO][getDetailByImeiBySpecification][Fin]");
    }
     objHashMapResultado.put("strStatusImei",objProductBean.getNpequipmentimeistatus());
     objHashMapResultado.put("strMessage",strMessage);
     objHashMapResultado.put("objProductBean",objProductBean);

	 if(logger.isDebugEnabled()){				
		logger.debug("strStatusImei: "+objProductBean.getNpequipmentimeistatus());
		logger.debug("strMessage: "+strMessage+"");
		logger.debug("objProductBean: "+objProductBean+"");
		logger.debug("Fin - [ProductDAO][getDetailByImeiBySpecification]");
	 }	     
    System.out.println("[ProductDAO][getDetailByImeiBySpecification][Fin]");
     return objHashMapResultado;                                                                
   }
      
   /* VALIDACION DE STOCK - INICIO
    * JPEREZ
    */
  /**
   * Purpose: Obtiene un flag que indica si es que debe validarse el stock para la especificación
   * Developer       Fecha       Comentario
   * =============   ==========  ======================================================================
   * JPEREZ          03/03/2008  Creación 
   * @throws java.sql.SQLException
   * @throws java.lang.Exception
   * @return 
   * @param idSpecification
   */
    public HashMap getValidateStock(int idSpecification, int iDispatchPlace) throws Exception, SQLException{
      HashMap hashMap = new HashMap();        
      Connection conn = null; 
      OracleCallableStatement cstmt = null;
      String strMensaje = null;
      String strFlag    = null;
      String sqlStr = "BEGIN ORDERS.NP_ORDERS16_PKG.SP_VALID_STOCK(?, ?, ?, ?); END;";
      try{
         conn = Proveedor.getConnection();          
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
         
         cstmt.setInt(1,idSpecification);
         cstmt.setInt(2,iDispatchPlace);
         cstmt.registerOutParameter(3, Types.CHAR);
         cstmt.registerOutParameter(4, Types.CHAR);
         cstmt.executeUpdate();
         
         strFlag         = cstmt.getString(3);
         strMensaje      = cstmt.getString(4);
         
         hashMap.put("wv_flag",strFlag);
         hashMap.put("wv_message",strMensaje);
      }
      catch (Exception e) {
         throw new Exception(e);
      }
      finally{
			closeObjectsDatabase(conn,cstmt,null);
         /*if (cstmt != null)
            cstmt.close();        
         if (conn != null)
            conn.close();*/
      }      
      return hashMap;      
    }
    
  /**
   * Purpose: Obtiene un mensaje de stock para determinado producto
   * Developer       Fecha       Comentario
   * =============   ==========  ======================================================================
   * JPEREZ          03/03/2008  Creación 
   * @throws java.sql.SQLException
   * @throws java.lang.Exception
   * @return 
   * @param strSaleModality
   * @param iBuildingid
   * @param lProductId
   * @param idSpecification
   * @param strTipo
   */
    public HashMap getStockMessage(int idSpecification, long lProductId, int iBuildingid, String strSaleModality, long lSalesStructOrigenId, String strTipo) 
    throws Exception, SQLException{
      HashMap hashMap = new HashMap();        
      Connection conn = null; 
      OracleCallableStatement cstmt = null;
      String strMensajeStock   = null;
      String strFlagStock      = null;
      String strMensaje        = null;    
      
      System.out.println("DATOS DE GETSTOCKMESSAGE:");
      System.out.println("idSpecification:"+idSpecification);
      System.out.println("lProductId:"+lProductId);
      System.out.println("iBuildingid:"+iBuildingid);
      System.out.println("strSaleModality:"+strSaleModality);
      System.out.println("strTipo:"+strTipo);
      String sqlStr = "BEGIN ORDERS.NP_ORDERS16_PKG.SP_GET_STOCK_MESSAGE(?,?,?,?,?,?,?,?,?,?); END;";
      try{
         conn = Proveedor.getConnection();          
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
         
         cstmt.setInt(1, idSpecification);
         cstmt.setLong(2, lProductId);
         cstmt.setInt(3, iBuildingid);
         cstmt.setString(4, strSaleModality);
         cstmt.setInt(5,0); //Origen de la llamada al SP (0 es cuando es llamado desde órdenes)
         cstmt.setLong(6, lSalesStructOrigenId);
         cstmt.setString(7, strTipo);
         cstmt.registerOutParameter(8, Types.CHAR);
         cstmt.registerOutParameter(9, Types.CHAR);
         cstmt.registerOutParameter(10, Types.CHAR);
         cstmt.executeUpdate();
         
         strMensajeStock         = cstmt.getString(8);
         strFlagStock            = cstmt.getString(9);
         strMensaje              = cstmt.getString(10);
         
         hashMap.put("wv_message_stock",strMensajeStock);
         hashMap.put("wv_flag_stock",strFlagStock);
         hashMap.put("wv_message",strMensaje);
      }
      catch (Exception e) {
         throw new Exception(e);
      }
      finally{
			closeObjectsDatabase(conn,cstmt,null);
         /*if (cstmt != null)
            cstmt.close();        
         if (conn != null)
            conn.close();*/
      }      
      return hashMap;      
    }  
	/**
     * Motivo: Obtener los valores de Línea de Productos
	 * <br>Realizado por: <a href="mailto:edgar.jara@nextel.com.pe">Edgar Jara</a>
	 * [Creación]
     * <br>Fecha: 22/09/2007
     * @return    HashMap que contiene: Lista de Producto, String vacío o que contiene el mensaje de error. 
     */
	public float getKitEquipmentPrice(long npProduct,String npModality, long npSalesStructOrigenId) throws SQLException, Exception {
		HashMap hshDataMap = new HashMap();
    HashMap hshDataMap1 = new HashMap();
		ArrayList arrProductList = new ArrayList();
		Connection conn = null; 		
    OracleCallableStatement cstmt = null;
		float productPrice=0;
    String strMessage = null;
		String strSql = "BEGIN ORDERS.SPI_GET_KIT_EQUIPMENT_PRICE(?,?,?,?,?); END;";
        
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(strSql);		
         
         cstmt.setLong(1,npProduct);
         cstmt.setString(2, npModality);
         cstmt.setLong(3,npSalesStructOrigenId);
         cstmt.registerOutParameter(4,Types.FLOAT);//OracleTypes.ARRAY,"PRODUCT.TR_PRODUCT_KIT_DET");acleTypes.ARRAY, "PRODUCT.TT_PRODUCT_KIT_PRODUCT_DET_LST");
         cstmt.registerOutParameter(5, Types.VARCHAR);
         cstmt.execute();
         strMessage  = cstmt.getString(5);
         if(StringUtils.isNotBlank(strMessage)){
            throw new Exception(strMessage);
         }		
         productPrice = (float)cstmt.getFloat(4);
                 
      }
      catch(Exception e){
        throw new Exception(e);
     }
     finally{
			closeObjectsDatabase(conn,cstmt,null);
	     /*if(cstmt != null)
           cstmt.close();
        if(conn != null)
           conn.close(); */
     }
      return productPrice;                                                               
   }    
      
/**
   * Purpose: Obtiene el valor de S o N si el servicio tiene comission
   * Developer       Fecha       Comentario
   * =============   ==========  ======================================================================
   * RPOLO          13/01/2009  Creación 
   * @throws java.sql.SQLException
   * @throws java.lang.Exception
   * @return 
   * @param intServicioId
   */
    public HashMap getComissionMessage(int intServicioId) throws Exception, SQLException{
      HashMap hashMap = new HashMap();        
      Connection conn = null; 
      OracleCallableStatement cstmt = null;
      String strMensajeStock   = null;
      String strFlagStock      = null;
      String strMensaje        = null;    
      
      String sqlStr = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_VERIFICATE_COMMISSION(?,?,?); END;";
      try{
         conn = Proveedor.getConnection();          
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
         
         cstmt.setInt(1, intServicioId);
         cstmt.registerOutParameter(2, Types.CHAR);
         cstmt.registerOutParameter(3, Types.CHAR);
         cstmt.executeUpdate();
         
         hashMap.put("wv_npcommission",cstmt.getString(3));
         hashMap.put("wv_message",cstmt.getString(2));
      }
      catch (Exception e) {
         throw new Exception(e);
      }
      finally{
			closeObjectsDatabase(conn,cstmt,null);
      }      
      return hashMap;      
    }
    
 /**
	Method : validaDiasSuspension
	Purpose: Valida los que un contrato no este suspendido mas de 60 días.
	Developer       		Fecha       Comentario
	=============   		==========  ======================================================================
	Rensso Martinez   	24/06/2009  Creación
	*/
	public HashMap validaDiasSuspension(String strPhoneNumber, long lSpecificationId, String strNpScheduleDate, String strNpScheduleDate2) throws Exception,SQLException{
       
	  Connection conn = null; 
		ResultSet rs=null;
		OracleCallableStatement cstmt = null;
		String strMessage = null;
		ProductBean objProductBean = new ProductBean();
		HashMap objHashMapResultado = new HashMap();        
      System.out.println("[ProductDAO][validaDiasSuspension][Inicio]");      
      System.out.println("[ProductDAO][validaDiasSuspension][Inicio]:strPhoneNumber:"+strPhoneNumber);
      System.out.println("[ProductDAO][validaDiasSuspension][Inicio]:strPhoneNumber:"+lSpecificationId);
      System.out.println("[ProductDAO][validaDiasSuspension][Inicio]:longCustomerId:"+strNpScheduleDate);
      System.out.println("[ProductDAO][validaDiasSuspension][Inicio]:longCustomerId:"+strNpScheduleDate2);
		if(logger.isDebugEnabled()){		
			logger.debug("Inicio - [ProductDAO][getDetailByPhoneBySpecification]");
			logger.debug("strPhoneNumber: "+strPhoneNumber);
      logger.debug("strPhoneNumber: "+lSpecificationId);
			logger.debug("longCustomerId: "+strNpScheduleDate+"");
			logger.debug("longSiteId: "+strNpScheduleDate2+"");
		}	 	  
    
      
		String strSql = "{call ORDERS.NP_ORDERS23_PKG.SP_VALIDATE_DIAS_SUSP(?,?,?,?,?)}";
                          
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(strSql);
          
         cstmt.setString(1,strPhoneNumber);
         cstmt.setLong(2,lSpecificationId);
         cstmt.setString(3,strNpScheduleDate);
         cstmt.setString(4,strNpScheduleDate2);          			        
         cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
          
         cstmt.executeUpdate();
         strMessage  = cstmt.getString(5);
           
      }
      catch(Exception e){
         System.out.println("[Exception][ProductDAO][validaDiasSuspension]"+e.getMessage());
         throw new Exception(e);
      }
		finally{
         System.out.println("[Finally][ProductDAO][validaDiasSuspension][Inicio]");
			closeObjectsDatabase(conn,cstmt,rs);
         /*if (rs != null)
            rs.close();
         if (cstmt != null)
            cstmt.close();
         if (conn != null)
            conn.close();*/   
         System.out.println("[Finally][ProductDAO][validaDiasSuspension][Fin]");
      }           
      objHashMapResultado.put("strMessage",strMessage);
      System.out.println("[ProductDAO][validaDiasSuspension][Fin]");
      return objHashMapResultado;                                                                
   }
   
   /**
   Method : getProductModelList
   Purpose: Obtener la lista de modelos de equipos
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Frank Picoy     28/09/2010  Creación
   */
  public HashMap getProductModelList(ProductBean objProductBean) throws Exception,SQLException {
    ArrayList list = new ArrayList();
    HashMap objHashMap = new HashMap();
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    Connection conn=null; 
    ProductBean productBean = null;
    String strMessage = null;
    try{
      String sqlStr =  "BEGIN ORDERS.NP_ORDERS06_PKG.SP_GET_MODEL_LST_BY_PROD_LINE(?, ?, ?); END;";
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setLong(1, objProductBean.getNpproductlineid());
      cstmt.registerOutParameter(2, OracleTypes.CURSOR);
      cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
      cstmt.executeUpdate();
      strMessage = cstmt.getString(3);
      if( strMessage==null){      
        rs = (ResultSet)cstmt.getObject(2);
        while (rs.next()) {
          productBean = new ProductBean();
          productBean.setNpproductid(Long.parseLong(rs.getString("npproductid")));
          productBean.setNpproductname(rs.getString("npname"));
          list.add(productBean);
        }
      }
      objHashMap.put("strMessage",strMessage);
      objHashMap.put("objArrayList",list);  
    }catch(Exception e){
      objHashMap.put("strMessage",e.getMessage()); 
    }finally{
       try{
        closeObjectsDatabase(conn,cstmt,rs); 
       }catch (Exception e) {
          logger.error(formatException(e));
       }
     }  
    return objHashMap;                                                      
  }
    
  /**
   Method : getProductListByModelId
   Purpose: Obtener la lista de modelos de equipos
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Frank Picoy     28/09/2010  Creación
   */
  public HashMap getProductListByModelId(ProductBean objProductParamBean) throws SQLException, Exception {
    ArrayList list = new ArrayList();
    HashMap objHashMap = new HashMap();
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    Connection conn=null; 
    ProductBean productBean = null;
    String strMessage = null;
    try{
      String sqlStr =  "BEGIN ORDERS.NP_ORDERS06_PKG.SP_GET_PRODUCT_LST_BY_MODEL(?, ?, ?, ?); END;";
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setLong(1, objProductParamBean.getNpproductlineid());
      cstmt.setLong(2, objProductParamBean.getNpproductid());
      cstmt.registerOutParameter(3, OracleTypes.CURSOR);
      cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
      cstmt.executeUpdate();
      strMessage = cstmt.getString(4);
      if( strMessage==null){      
        rs = (ResultSet)cstmt.getObject(3);
        while (rs.next()) {
          productBean = new ProductBean();
          productBean.setNpproductid(Long.parseLong(rs.getString("npproductid")));
          productBean.setNpproductname(rs.getString("npname"));
          productBean.setNpminuteprice(0);
          productBean.setNpminute(0);
          list.add(productBean);
        }
      }
      objHashMap.put("strMessage",strMessage);
      objHashMap.put("objArrayList",list);  
    }catch(Exception e){
      objHashMap.put("strMessage",e.getMessage()); 
    }finally{
       try{
        closeObjectsDatabase(conn,cstmt,rs); 
       }catch (Exception e) {
          logger.error(formatException(e));
       }
     }  
    return objHashMap;                                                      
  }
  
  /**
   Method : getProductPlanList
   Purpose: Obtener la lista Planes a partir de un producto seleccionado
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Frank Picoy     08/10/2010  Creación
   */
  public HashMap getProductPlanList(ProductBean objProductBean) throws Exception,SQLException {
    ArrayList list = new ArrayList();
    HashMap objHashMap = new HashMap();
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    Connection conn=null; 
    PlanTarifarioBean planTarifBean = null;
    String strMessage = null;
    conn = Proveedor.getConnection();
    String sqlStr = "BEGIN ORDERS.NP_ORDERS01_PKG.SP_GET_PLANS_LIST_COMP(?, ?, ?, ?, ?, ?, ?, ?); END;";//EZUBIAURR
    
    try{
      //String sqlStr =  "BEGIN PRODUCT.SPI_GET_PLAN_LST_BY_PRODUCT(?, ?, ?); END;";

      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      
      cstmt.setString(1,"0");
      //cstmt.setLong(1, objProductBean.getNpproductid());
      cstmt.setString(2,objProductBean.getNpsubtype());
      System.out.println("getNpsubtype ="+objProductBean.getNpsubtype());
      
      //cstmt.registerOutParameter(2, OracleTypes.CURSOR);
      cstmt.setLong(3,objProductBean.getNpsolutionid());
      System.out.println("getNpsolutionid ="+objProductBean.getNpsolutionid());
      //cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
      cstmt.setLong(4,objProductBean.getNpcategoryid());
      System.out.println("getNpcategoryid ="+objProductBean.getNpcategoryid());
      cstmt.setLong(5,objProductBean.getNpproductid());
      System.out.println("getNpproductid ="+objProductBean.getNpproductid());
      cstmt.setLong(6,objProductBean.getNpproductlineid());//EZUBIAURR 14/03/11
      System.out.println("getNpproductlineid ="+objProductBean.getNpproductlineid());
      cstmt.registerOutParameter(7, OracleTypes.CURSOR );
      cstmt.registerOutParameter(8, Types.VARCHAR);
      
      cstmt.execute();
      
      strMessage = cstmt.getString(8);
      
      System.out.println("En ProductDAO strMessage---------->"+strMessage);
      if( strMessage==null){      
        rs = (ResultSet)cstmt.getCursor(7);
        while (rs.next()) {
          planTarifBean = new PlanTarifarioBean();
          planTarifBean.setNpdescripcion(rs.getString("des"));
          planTarifBean.setNpplantarifarioid(MiUtil.parseLong(rs.getString("tmcode")));
          //planTarifBean.setNpdescripcion(rs.getString("des"));
          
          list.add(planTarifBean);
        }
      }
      objHashMap.put("strMessage",strMessage);
      objHashMap.put("objArrayList",list);  
    }catch(Exception e){
      objHashMap.put("strMessage",e.getMessage()); 
    }finally{
       try{
        closeObjectsDatabase(conn,cstmt,rs); 
       }catch (Exception e) {
          logger.error(formatException(e));
       }
     }  
    return objHashMap;                                                      
  }
  
     /**
   Method : getProductListByPlanAndProdLine
   Purpose: Obtener la lista de productos a partir del plan original y linea de producto seleccionado.
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Frank Picoy     15/10/2010  Creación
   */
  public HashMap getProductListByPlanAndProdLine(ProductBean objProductBean) throws Exception,SQLException {
    ArrayList list = new ArrayList();
    HashMap objHashMap = new HashMap();
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    Connection conn=null; 
    ProductBean productBean = null;
    String strMessage = null;
    try{
      String sqlStr =  "BEGIN PRODUCT.SPI_GET_PRODUCT_LST_BY_PLAN(?, ?, ?, ?); END;";
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setLong(1, objProductBean.getNpproductlineid());
      cstmt.setLong(2, objProductBean.getNpplanid());
      cstmt.registerOutParameter(3, OracleTypes.CURSOR);
      cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
      cstmt.executeUpdate();
      strMessage = cstmt.getString(4);
      if( strMessage==null){      
        rs = (ResultSet)cstmt.getObject(3);
        while (rs.next()) {
          productBean = new ProductBean();
          productBean.setNpproductid(Long.parseLong(rs.getString("npproductid")));
          productBean.setNpproductname(rs.getString("npname"));
          list.add(productBean);
        }
      }
      objHashMap.put("strMessage",strMessage);
      objHashMap.put("objArrayList",list);  
    }catch(Exception e){
      objHashMap.put("strMessage",e.getMessage()); 
    }finally{
       try{
        closeObjectsDatabase(conn,cstmt,rs); 
       }catch (Exception e) {
          logger.error(formatException(e));
       }
     }  
    return objHashMap;                                                      
  }

  public HashMap getValidateSimImei (String strNumber) throws Exception, SQLException{
    Connection conn = null;
    OracleCallableStatement cstmt = null;
    String strMessage = null;
    HashMap objHashMapResultado = new HashMap();

		if(logger.isDebugEnabled()){		
			logger.debug("strNumber: "+strNumber);
		}	 	    
    
    String strSql = "{call WEBSALES.SPI_VALIDATE_SIM_IMEI(?, ?, ?)}";
    
    try{
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement) conn.prepareCall(strSql);
      cstmt.setString(1,strNumber);
      cstmt.registerOutParameter(2,OracleTypes.VARCHAR);
      cstmt.registerOutParameter(3,OracleTypes.VARCHAR);
      cstmt.executeUpdate();
      
      strMessage = cstmt.getString(3);
      
      if(strMessage==null){
        objHashMapResultado.put("result",cstmt.getString(2));
        
        if(logger.isDebugEnabled()){		
          logger.debug("result: "+cstmt.getString(2));
        }	 	    
      }
      
    }catch(Exception e){
      logger.error(formatException(e));
      throw new Exception(e);
      
    }finally{
      try{
        closeObjectsDatabase(conn,cstmt,null);
      }catch(Exception e){
        logger.error(formatException(e));
      }
      
    }
    objHashMapResultado.put(Constante.MESSAGE_OUTPUT,strMessage);
    return objHashMapResultado;
  }
  
   /**
   Method : getBolsaCreacionN2
   Purpose: Obtener información para la creación de un producto bolsa de nivel 2 con 0 minutos contratados
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Nya Bravo       16/08/2011  Creación
   */
  public HashMap getBolsaCreacionN2(long lngCustomerId,long lngSiteId) throws Exception,SQLException{
     
     ProductBean objProductBean = new ProductBean();
     Connection conn = null; 
     ResultSet  rs   = null;
     OracleCallableStatement cstmt = null;
     String strMessage = null;
     String strLevel2  = null;
     String strMinutesrate = null;
     String strDescrBolsa  = null;
     Float strPrecioxmin;
     HashMap objHashMapResultado = new HashMap();   
     
     String strSql = "BEGIN ORDERS.NP_ORDERS17_PKG.SP_GET_BOLSA_CREACION_INFO(?,?,?,?,?,?,?); END;";
     try{
        conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement) conn.prepareCall(strSql);         
        cstmt.setLong(1,lngCustomerId);
        cstmt.setString(2,"C");

        cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(5, Types.FLOAT);
        cstmt.registerOutParameter(6, OracleTypes.VARCHAR);        
        cstmt.registerOutParameter(7, OracleTypes.VARCHAR);

        cstmt.executeUpdate();
        strLevel2       = cstmt.getString(3);          
        strMinutesrate  = cstmt.getString(4);     
        strPrecioxmin   = new Float(cstmt.getFloat(5));   
        strDescrBolsa   = cstmt.getString(6);
		    strMessage      = cstmt.getString(7);       
     }
     catch (Exception e) {
        throw new Exception(e);
     }
     finally{
			closeObjectsDatabase(conn,cstmt,null); 
     }
     
     objHashMapResultado.put("strBolsaN2",strMessage);
     objHashMapResultado.put("strLevel2",strLevel2);
     objHashMapResultado.put("strMinutesrate",strMinutesrate);
     objHashMapResultado.put("strPrecioxmin",strPrecioxmin+ "");
     objHashMapResultado.put("strDescrBolsa",strDescrBolsa);    
     
     System.out.println("strLevel2 "+strLevel2); 
     objHashMapResultado.put(Constante.MESSAGE_OUTPUT,strMessage);     
     return objHashMapResultado;                              
   }   

    /**
     Method : getBolsaCelulares
     Purpose: Obtener el producto bolsa de celulares registrado por cliente
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     LHUAPAYA     30/07/2015  Creación:ADT-BCL-083
     */
    public HashMap getBolsaCelulares(long lngSiteId, long customerId,long lngProductLineId)throws Exception,SQLException{
        ProductBean objProductBean = new ProductBean();
        HashMap objHashMapResultado = new HashMap();
        Connection conn = null;
        ResultSet  rs   = null;
        OracleCallableStatement cstmt = null;
        String nameProduct = null;
        String message = null;
        String strSql = "BEGIN CELL_BAG.SPI_GET_PRODUCT_BOLSA_BY_SITE(?,?,?,?,?,?,?); END;";
        try{
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement) conn.prepareCall(strSql);
            cstmt.setLong(1, lngSiteId);
            cstmt.setLong(2, customerId);
            cstmt.setLong(3, lngProductLineId);
            cstmt.registerOutParameter(4,OracleTypes.VARCHAR);
            cstmt.registerOutParameter(5,OracleTypes.NUMBER);
            cstmt.registerOutParameter(6,OracleTypes.VARCHAR);
            cstmt.registerOutParameter(7,OracleTypes.VARCHAR);
            cstmt.executeUpdate();
            message = cstmt.getString(7);

            if(message == null){
                objProductBean.setNpproductid(cstmt.getLong(5));
                objProductBean.setNpproductname(cstmt.getString(4));
                objProductBean.setNpcost(Double.parseDouble(cstmt.getString(6)));
            }
        }
        catch(Exception e){
            throw new Exception(e);
        }finally{
            closeObjectsDatabase(conn,cstmt,null);
        }
        objHashMapResultado.put("objProductBean", objProductBean);
        objHashMapResultado.put("message",message);
        return objHashMapResultado;
    }

    /**
     Method : getProductBCL
     Purpose: Obtiene todos los productos bolsa de celulares por linea de producto y tipo
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     LHUAPAYA     30/07/2015  Creación:ADT-BCL-083
     */
    public HashMap getProductBCL(ProductBean objProductParamBean)throws Exception,SQLException{
        ArrayList list = new ArrayList();
        HashMap objHashMap = new HashMap();
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        Connection conn=null;
        ProductBean productBean = null;
        String strMessage = null;
        try{
            String sqlStr =  "BEGIN CELL_BAG.NP_MOBILE_BAG05_PKG.SP_GET_PRODUCT_BOLCEL_LST(?, ?, ?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, objProductParamBean.getNpproductlineid());
            cstmt.setLong(2, objProductParamBean.getNpproduct_type());
            cstmt.registerOutParameter(3, OracleTypes.CURSOR);
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
            cstmt.executeUpdate();
            strMessage = cstmt.getString(4);
            if( strMessage==null){
                rs = (ResultSet)cstmt.getObject(3);
                while (rs.next()) {
                    productBean = new ProductBean();
                    productBean.setNpproductid(Long.parseLong(rs.getString("npproductid")));
                    productBean.setNpproductname(rs.getString("npname"));
                    productBean.setNpminuteprice(0);
                    productBean.setNpminute(0);
                    list.add(productBean);
                }
            }
            objHashMap.put("strMessage",strMessage);
            objHashMap.put("objArrayList",list);
        }catch(Exception e){
            objHashMap.put("strMessage",e.getMessage());
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return objHashMap;
    }

    /**
     Method : getAllProductBCL
     Purpose: Obtener todos los  productos bolsa de celulares registrado por cliente
     Developer       Fecha        Comentario
     =============   ==========  ======================================================================
     LHUAPAYA     30/07/2015  Creación:ADT-BCL-083
     */
    public HashMap getAllProductBCL(long lngSiteId, long lngCutomerId)throws Exception,SQLException{
        ArrayList list = new ArrayList();
        HashMap objHashMap = new HashMap();
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        Connection conn=null;
        ProductBean productBean = null;
        String strMessage = null;
        try{
            String sqlStr =  "BEGIN CELL_BAG.NP_MOBILE_BAG05_PKG.SP_GET_ALL_PRODUCT_BOLSA_SITE(?,?,?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, lngSiteId);
            cstmt.setLong(2, lngCutomerId);
            cstmt.registerOutParameter(3, OracleTypes.CURSOR);
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
            cstmt.executeUpdate();
            strMessage = cstmt.getString(4);
            if( strMessage==null){
                rs = (ResultSet)cstmt.getObject(3);
                while (rs.next()) {
                    HashMap objHasmapTemp = new HashMap();
                    objHasmapTemp.put("strProductLineId",rs.getLong("NPPRODUCTLINEID"));
                    objHasmapTemp.put("strProductLineDesc", rs.getString("NPNAME"));
                    objHasmapTemp.put("strProductId", rs.getLong("NPPRODUCTID"));
                    objHasmapTemp.put("strProductDesc", rs.getString("PRODESC"));
                    objHasmapTemp.put("strProductCost", rs.getDouble("NPCOST"));
                    list.add(objHasmapTemp);
                }
            }
            objHashMap.put("strMessage",strMessage);
            objHashMap.put("objArrayList",list);
            System.out.println("Si pinto bien");
        }catch(Exception e){
            objHashMap.put("strMessage",e.getMessage());
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return objHashMap;
    }

    /**
    Method : getCantidadRentaAdelantada
    Purpose: Obtiene la cantidad de Renta Adelantada
    Developer       Fecha       Comentario
    =============   ==========  ======================================================================
    JQUISPE         15/02/2017  PRY-0762: Creación
    */
   public HashMap getCantidadRentaAdelantada(int codigoProducto, int codigoPlan)  throws Exception, SQLException {
	   
	    Connection conn = null;
	    ResultSet rs=null;
	    OracleCallableStatement cstm = null;
	    String strMessage = null;
	    HashMap objHashMapResultado = new HashMap();
	    
	    try{
	      String strSql = "BEGIN PRODUCT.SPI_GET_CANTIDAD_RA(?,?,?,?); END;";
	      conn = Proveedor.getConnection();
	      cstm = (OracleCallableStatement) conn.prepareCall(strSql);

	      cstm.setInt(1,codigoProducto);
	      cstm.setInt(2,codigoPlan);
	      cstm.registerOutParameter(3, OracleTypes.INTEGER);
	      cstm.registerOutParameter(4, OracleTypes.VARCHAR);

	      cstm.execute();
	      strMessage  = cstm.getString(4);

	      if( strMessage == null){
	    	  objHashMapResultado.put("intCantidadRA", cstm.getInt(3));
	      }
	      
	      objHashMapResultado.put("strMessage",strMessage);
	     
	    }catch (Exception e) {
	      logger.error(formatException(e));
	      objHashMapResultado.put("strMessage",e.getMessage());
	    }finally{
	      try{	        
	        closeObjectsDatabase(conn,cstm,rs);
	      }catch (Exception e) {
	        logger.error(formatException(e));
	      }
	    }
	    return objHashMapResultado;
   }

    /**
     Method : getEnabledRegions
     Purpose: Obtener la lista de regiones habilitadas por producto
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     DERAZO          16/02/2017  PRY-0721: Creación
     */
    public HashMap getEnabledRegions(String strProductId) throws Exception,SQLException {
        List<RegionBean> listaRegiones = new ArrayList<RegionBean>();
        HashMap objHashMap = new HashMap();
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        Connection conn=null;
        RegionBean regionBean = null;
        String strMessage = null;
        int result = 0;
        System.out.println("[ProductDAO][getEnabledRegions] strProductId: "+strProductId);

        try{
            String sqlStr =  "BEGIN PRODUCT.NP_ZONA_COBERTURA_PKG.SP_GET_ENABLED_REGIONS(?, ?, ?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            long productId = MiUtil.parseLong(strProductId);
            cstmt.setLong(1, productId);
            cstmt.registerOutParameter(2, OracleTypes.NUMBER);
            cstmt.registerOutParameter(3, OracleTypes.CURSOR);
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
            cstmt.executeUpdate();
            strMessage = cstmt.getString(4);
            System.out.println("[ProductDAO][getEnabledRegions] strMessage: "+strMessage);

            if(strMessage==null){
                result = cstmt.getInt(2);
                System.out.println("[ProductDAO][getEnabledRegions] result: "+result);

                if(result == 1){
                    rs = (ResultSet)cstmt.getObject(3);
                    while (rs.next()) {
                        regionBean = new RegionBean();
                        regionBean.setRegionId(Integer.parseInt(rs.getString("npzonacoberturaid")));
                        regionBean.setRegionName(rs.getString("npnombrezona"));
                        listaRegiones.add(regionBean);
                    }
                }
            }

            objHashMap.put("strMessage",strMessage);
            objHashMap.put("result",result);
            objHashMap.put("listaRegiones",listaRegiones);
        }catch(Exception e){
            objHashMap.put("strMessage",e.getMessage());
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        return objHashMap;
    }

/**
     * Method : getListProvince
     * Purpose: Obtiene la lista de provincias segun una region
     * Developer      Fecha       Comentario
     * ==================================
     * EFLORES        06/06/2017  Creacion
     */
    public HashMap getListProvinceBAFI(String strProductId,String strZonaCoberturaId) throws Exception,SQLException{
        List<RegionBean> lstRegiones = new ArrayList<RegionBean>();
        HashMap objHashMap = new HashMap();
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        Connection conn=null;
        RegionBean regionBean = null;
        String strMessage = null;
        int result = 0;
        System.out.println("[ProductDAO][getListProvinceBAFI] strProductId: "+strProductId);
        System.out.println("[ProductDAO][getListProvinceBAFI] strZonaCoberturaId: "+strZonaCoberturaId);

        try{
            String sqlStr =  "BEGIN PRODUCT.NP_ZONA_COBERTURA_PKG.SP_GET_LIST_PROVINCES_BAFI(?,?,?,?,?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            long productId = MiUtil.parseLong(strProductId);
            long npzonacoberturaId = MiUtil.parseLong(strZonaCoberturaId);
            cstmt.setLong(1, productId);
            cstmt.setLong(2, npzonacoberturaId);
            cstmt.registerOutParameter(3, OracleTypes.CURSOR);
            cstmt.registerOutParameter(4, OracleTypes.NUMBER);
            cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
            cstmt.executeUpdate();
            strMessage = cstmt.getString(5);
            System.out.println("[ProductDAO][getListProvinceBAFI] strMessage: "+strMessage);

            if(strMessage==null){
                result = cstmt.getInt(4);
                System.out.println("[ProductDAO][getListProvinceBAFI] result: "+result);

                if(result == 1){
                    rs = (ResultSet)cstmt.getObject(3);
                    while (rs.next()) {
                        regionBean = new RegionBean();
                        regionBean.setRegionId(Integer.parseInt(rs.getString("npzonaprovid")));
                        regionBean.setRegionName(rs.getString("npnombrezona"));
                        lstRegiones.add(regionBean);
                    }
                }
            }

            objHashMap.put("strMessage",strMessage);
            objHashMap.put("result",result);
            objHashMap.put("listaRegiones",lstRegiones);
        }catch(Exception e){
            objHashMap.put("strMessage",e.getMessage());
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        return objHashMap;
    }

    /**
     * Method : getListDistrict
     * Purpose: Obtiene la lista de distritos segun una region
     * Developer      Fecha       Comentario
     * ==================================
     * EFLORES        06/06/2017  Creacion
     */
    public HashMap getListDistrictBAFI(String strProductId,String strProvinceZoneId) throws Exception,SQLException{
        List<RegionBean> lstRegiones = new ArrayList<RegionBean>();
        HashMap objHashMap = new HashMap();
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        Connection conn=null;
        RegionBean regionBean = null;
        String strMessage = null;
        int result = 0;
        System.out.println("[ProductDAO][getListDistrictBAFI] strProductId: "+strProductId);
        System.out.println("[ProductDAO][getListDistrictBAFI] strProvinceZoneId: "+strProvinceZoneId);

        try{
            String sqlStr =  "BEGIN PRODUCT.NP_ZONA_COBERTURA_PKG.SP_GET_LIST_DISTRICTS_BAFI(?,?,?,?,?); END;";
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            long productId = MiUtil.parseLong(strProductId);
            long npprovinceId = MiUtil.parseLong(strProvinceZoneId);
            cstmt.setLong(1, productId);
            cstmt.setLong(2, npprovinceId);
            cstmt.registerOutParameter(3, OracleTypes.CURSOR);
            cstmt.registerOutParameter(4, OracleTypes.NUMBER);
            cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
            cstmt.executeUpdate();
            strMessage = cstmt.getString(5);
            System.out.println("[ProductDAO][getListDistrictBAFI] strMessage: "+strMessage);

            if(strMessage==null){
                result = cstmt.getInt(4);
                System.out.println("[ProductDAO][getListDistrictBAFI] result: "+result);

                if(result == 1){
                    rs = (ResultSet)cstmt.getObject(3);
                    while (rs.next()) {
                        regionBean = new RegionBean();
                        regionBean.setRegionId(Integer.parseInt(rs.getString("npzonadistid")));
                        regionBean.setRegionName(rs.getString("npnombrezona"));
                        lstRegiones.add(regionBean);
                    }
                }
            }

            objHashMap.put("strMessage",strMessage);
            objHashMap.put("result",result);
            objHashMap.put("listaRegiones",lstRegiones);
        }catch(Exception e){
            objHashMap.put("strMessage",e.getMessage());
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }

        return objHashMap;
    }


    /**
     Method : getProductPlanList
     Purpose: Obtener la lista Planes a partir de un producto seleccionado
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     AMENDEZ         26/03/2018  [EST-1098]Creación
     */
    public HashMap getProductPlanListBafi(ProductBean objProductBean) throws Exception,SQLException {
        logger.info("*************************** INICIO ProductDAO > getProductPlanListBafi***************************");
        ArrayList list = new ArrayList();
        HashMap objHashMap = new HashMap();
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        Connection conn=null;
        PlanTarifarioBean planTarifBean = null;
        String strMessage = null;
        conn = Proveedor.getConnection();
        String sqlStr = "BEGIN ORDERS.NP_ORDERS01_PKG.SP_PR_CA_GETPLANLISTBAFI(?, ?, ?, ?, ?, ?, ?, ?, ?, ?); END;";

        try{
            logger.info("objProductBean.toString() : "+objProductBean.toString());
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

            cstmt.setInt(1,objProductBean.getFlagCoverage());
            cstmt.setString(2,objProductBean.getNpmodality());
            cstmt.setString(3,"0");
            cstmt.setString(4,objProductBean.getNpsubtype());
            cstmt.setLong(5,objProductBean.getNpsolutionid());
            cstmt.setLong(6,objProductBean.getNpcategoryid());
            cstmt.setLong(7,objProductBean.getNpproductid());
            cstmt.setLong(8,objProductBean.getNpproductlineid());
            cstmt.registerOutParameter(9, OracleTypes.CURSOR );
            cstmt.registerOutParameter(10, Types.VARCHAR);

            cstmt.execute();

            strMessage = cstmt.getString(10);

            logger.info("En ProductDAO strMessage---------->"+strMessage);
            if( strMessage==null){
                rs = (ResultSet)cstmt.getCursor(9);
                while (rs.next()) {
                    planTarifBean = new PlanTarifarioBean();
                    planTarifBean.setNpdescripcion(rs.getString("des"));
                    planTarifBean.setNpplantarifarioid(MiUtil.parseLong(rs.getString("tmcode")));
                    planTarifBean.setNpplancode(rs.getDouble("tmcode"));
                    planTarifBean.setNptipo2(rs.getString("nivelplan"));

                    list.add(planTarifBean);
                }
            }
            objHashMap.put("strMessage",strMessage);
            objHashMap.put("objArrayList",list);
            objHashMap.put("objPlanList",list);
        }catch(Exception e){
            objHashMap.put("strMessage",e.getMessage());
        }finally{
            try{
                closeObjectsDatabase(conn,cstmt,rs);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
        }
        logger.info("*************************** FIN ProductDAO > getProductPlanListBafi***************************+");
        return objHashMap;
    }
}