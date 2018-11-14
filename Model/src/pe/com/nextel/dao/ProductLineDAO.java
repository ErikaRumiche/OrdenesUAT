package pe.com.nextel.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.ArrayList;
import java.util.HashMap;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import pe.com.nextel.bean.ProductLineBean;


//import oracle.jdbc.OracleCallableStatement;

public class ProductLineDAO extends GenericDAO{
  
  /**
   Method : getProductLineDetail
   Purpose: Obtener el detalle de la Linea de Productos
   
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Lee Rosales     22/09/2007  Creación
   */
     
  public HashMap getProductLineDetail(long lngProductLineId) throws SQLException,Exception{
    
   ProductLineBean objProductLineBean = null;
   Connection conn = null; 
   OracleCallableStatement cstm = null;
   String strMessage = null;
        
   String strSql = "BEGIN PRODUCT.SPI_GET_PRODUCT_LINE_DET3(?,?,?,?); END;";
   try{
      conn = Proveedor.getConnection();
      cstm = (OracleCallableStatement) conn.prepareCall(strSql);               
      cstm.setLong(1,lngProductLineId);
      cstm.registerOutParameter(2, OracleTypes.VARCHAR);
      cstm.registerOutParameter(3, OracleTypes.NUMBER);
      cstm.registerOutParameter(4, OracleTypes.VARCHAR);      
      cstm.execute();   
      strMessage  = cstm.getString(4);      
      if( strMessage == null ){
         objProductLineBean = new ProductLineBean();
         objProductLineBean.setNpName(cstm.getString(2));
         objProductLineBean.setNptype(cstm.getInt(3));
      }
   }         
   catch (Exception e) {
      throw new Exception(e);
   }
   finally{
      closeObjectsDatabase(conn,cstm,null);
   }  
   HashMap objHashMap = new HashMap();
   objHashMap.put("objProductLineBean",objProductLineBean);
   objHashMap.put("strMessage",strMessage);
   return objHashMap;
 }

   /**
   Method : getProductLineSpecList
   Purpose: Obtener los valores de LINEA PRODUCTOS
   Developer         Fecha       Comentario
   ===============   ==========  ======================================================================
   Lee Rosales       22/09/2007  Creación
   Walter Sotomayor  07/06/2011  Se agrega el parametro longProductLineId.
   */
     
  public HashMap getProductLineSpecList(long longSolutionId, long longSpecificationId, String strObjectType, long longProductLineId) throws SQLException,Exception{
    
   ArrayList list = new ArrayList();
   ProductLineBean objProductLineBean = null;
   Connection conn = null; 
   ResultSet rs=null;
   OracleCallableStatement cstm = null;
   String strMessage = null;
   
   String strSql = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_GET_PRODUCT_LINE_SPEC(?,?,?,?,?,?); END;";     
   try{
      conn = Proveedor.getConnection();
      cstm = (OracleCallableStatement) conn.prepareCall(strSql);
      
      cstm.setLong(1,longSolutionId);         
      cstm.setLong(2,longSpecificationId);
      cstm.setString(3,strObjectType);
      cstm.setLong(4,longProductLineId);      
      cstm.registerOutParameter(5, OracleTypes.CURSOR);
      cstm.registerOutParameter(6, OracleTypes.VARCHAR);
      cstm.execute();
      
      strMessage  = cstm.getString(6);
      
      if( strMessage == null ){
      //rs = cstm.getCursor(3);
         rs = (ResultSet)cstm.getObject(5);
         while (rs.next()) {
            objProductLineBean = new ProductLineBean();
            objProductLineBean.setNpProductLineId(rs.getInt("npobjectid"));
            objProductLineBean.setNpName(rs.getString("npname"));
            list.add(objProductLineBean);
         }
      }
   }
   catch (Exception e) {
      throw new Exception(e);
   }
   finally{
      closeObjectsDatabase(conn,cstm,rs);
   }
   HashMap objHashMap = new HashMap();
   objHashMap.put("objArrayList",list);
   objHashMap.put("strMessage",strMessage);
     
   return objHashMap;
 }
    
  /**
   Method : getProductLineValueList
   Purpose: Obtener los valores de LINEA PRODUCTOS
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Lee Rosales     22/09/2007  Creación
   */
     
  public static ArrayList getProductLineValueList(int iProductLineId, String strMessage) throws SQLException,Exception{
     ArrayList list = null;
     ProductLineBean tb = null;
     Connection conn = null; 
     ResultSet rs=null;
     OracleCallableStatement cstm = null;
        
     String strSql = "BEGIN WEBSALES.NPSL_GENERAL_PKG.SP_GET_PRODUCTLINE_LIST(?,?,?); END;";
     try{
        conn = Proveedor.getConnection();
        cstm = (OracleCallableStatement) conn.prepareCall(strSql);
                 
        cstm.setInt(1,iProductLineId);
        cstm.registerOutParameter(2, Types.CHAR);
        cstm.registerOutParameter(3, OracleTypes.CURSOR);     
        cstm.execute();
   
        strMessage  = cstm.getString(2);
        
        if( strMessage == null ){
         //rs = cstm.getCursor(3);
         rs = (ResultSet)cstm.getObject(3);
         int npProductLineId;
         while (rs.next()) {
           list = new ArrayList();
           tb = new ProductLineBean();
           npProductLineId = Integer.parseInt(rs.getString("npProductLineId"));
           tb.setNpProductLineId(npProductLineId);
           tb.setNpName(rs.getString("npName")==null?"":rs.getString("npName"));
   
           list.add(tb);
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
    
}
