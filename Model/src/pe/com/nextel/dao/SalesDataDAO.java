package pe.com.nextel.dao;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.ArrayList;
import java.util.HashMap;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import pe.com.nextel.bean.ItemBean;


public class SalesDataDAO extends GenericDAO{
  public SalesDataDAO()
  {
  }
  
  /***********************************************************************************************************************************
  * Motivo: Obtiene los registros de las aplicaciones disponibles para el cliente.
  * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
  * <br>Fecha: 06/05/2010
  ************************************************************************************************************************************/ 
  public HashMap getAplicationDataList(long lnOrderId, long lngDivisionId,long lngCustomerId,long lngSpecificationId) throws SQLException, Exception{             
        OracleCallableStatement cstmt = null; 
        ResultSet rs = null;
        String strMessage=null;
        Connection conn=null;
        HashMap objHashMap = new HashMap();
        ArrayList list = new ArrayList();
        ItemBean objItemBean =  null;
        String strSolutionType= null;
        try{
          String strSql = " BEGIN ORDERS.NP_ORDERS29_PKG.SP_GET_APLICATION_DATA_LIST(?,?,?,?,?,?); END;";        
          conn = Proveedor.getConnection();   
          cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
          cstmt.setLong(1, lnOrderId);
          cstmt.setLong(2, lngDivisionId);
          cstmt.setLong(3, lngCustomerId);
          cstmt.setLong(4, lngSpecificationId);
          cstmt.registerOutParameter(5, OracleTypes.CURSOR);
          cstmt.registerOutParameter(6, OracleTypes.VARCHAR);  
          cstmt.executeUpdate();  
          strMessage = cstmt.getString(6);  
           if( strMessage == null ){
              rs = cstmt.getCursor(5);
              while (rs.next()) {
                  objItemBean = new ItemBean();
                  objItemBean.setNpsolutionid(rs.getLong("npsolutionid"));
                  objItemBean.setNpproductlineid(rs.getLong("npproductlineid"));
                  objItemBean.setNpproductlinename(rs.getString("npproductlinename"));
                  objItemBean.setNpproductid(rs.getLong("npproductid"));
                  objItemBean.setNpproductname(rs.getString("npproductname"));
                  list.add(objItemBean);
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
  
  
  /***********************************************************************************************************************************
  * Motivo: Inserta una Aplicación de Ventas Data
  * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
  * <br>Fecha: 07/05/2010
  * @param     objSiteBean  
  * @param     lOrderId
  * @param     strLogin   
  * @param     iAppId    
  * @return    HashMap 
  *************************************************************************************************************************************/
  public HashMap insSalesData(ItemBean objItemBean,long lOrderId,long lCustomerId,long lDivisionId,String strStatus,String strLogin,Connection conn) throws Exception,SQLException {
    HashMap hshData=new HashMap();
    OracleCallableStatement cstmt = null;
    try{
    
      long lSalesDataId = 0;
      String strMessage=null; 
      String sqlStr = "BEGIN ORDERS.NP_ORDERS29_PKG.SP_INS_BUSINESS_SOLUTION_APP(?, ?, ?, ?,?, " + 
                                                                                "?,?, ?, ?, ?); END;";

      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setLong(1, lOrderId); 
      cstmt.setLong(2, lCustomerId); 
      cstmt.setLong(3, lDivisionId); 
      cstmt.setLong(4, objItemBean.getNpsolutionid());
      cstmt.setLong(5, objItemBean.getNpproductlineid());
      cstmt.setLong(6, objItemBean.getNpproductid());
      cstmt.setString(7, strStatus);
      cstmt.setString(8, strLogin);
      cstmt.registerOutParameter(9, OracleTypes.NUMERIC);
      cstmt.registerOutParameter(10, Types.VARCHAR);        
      cstmt.executeUpdate(); 
    
      strMessage = cstmt.getString(10);
      lSalesDataId = cstmt.getLong(9); 
      
      System.out.println("Mensaj de Error -->"+strMessage);    
      hshData.put("strMessage",strMessage);
      hshData.put("lSalesDataId",lSalesDataId+"");
  
    }catch(Exception ex){
      throw new Exception(ex);
    }finally{
      closeObjectsDatabase(null,cstmt,null);
    }
  
    return hshData;
  }
  
  
  /***********************************************************************************************************************************
  * Motivo: Obtiene los registros de las aplicaciones de un cliente.
  * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
  * <br>Fecha: 06/05/2010
  ************************************************************************************************************************************/ 
  public HashMap getAplicationCustomer(long lngOrderId, long lngDivisionId,long lngCustomerId, String strStatus) throws SQLException, Exception{             
        OracleCallableStatement cstmt = null; 
        ResultSet rs = null;
        String strMessage=null;
        Connection conn=null;
        HashMap objHashMap = new HashMap();
        ArrayList list = new ArrayList();
        ItemBean objItemBean =  null;
        String strSolutionType= null;
        try{
          String strSql = " BEGIN ORDERS.NP_ORDERS29_PKG.SP_GET_APLICATION_CUSTOMER(?,?,?,?,?,?); END;";        
          conn = Proveedor.getConnection();   
          cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
          cstmt.setLong(1, lngOrderId);
          cstmt.setLong(2, lngDivisionId);
          cstmt.setLong(3, lngCustomerId);
          cstmt.setString(4, strStatus);
          cstmt.registerOutParameter(5, OracleTypes.CURSOR);
          cstmt.registerOutParameter(6, OracleTypes.VARCHAR);  
          cstmt.executeUpdate();  
          strMessage = cstmt.getString(6);  
           if( strMessage == null ){
              rs = cstmt.getCursor(5);
              while (rs.next()) {
                  objItemBean = new ItemBean();
                  objItemBean.setNpbusinesssolutionid(rs.getLong("NPBUSINESSSOLUTIONID"));
                  objItemBean.setNpsolutionid(rs.getLong("NPSOLUTIONID"));
                  objItemBean.setNpproductid(rs.getLong("NPPRODUCTID"));
                  objItemBean.setNpproductname(rs.getString("NPPRODUCTNAME"));
                  objItemBean.setNpproductlineid(rs.getLong("NPPRODUCTLINEID"));
                  objItemBean.setNpproductlinename(rs.getString("NPPRODUCTLINENAME"));
                  objItemBean.setNpstatusaplication(rs.getString("NPSTATUS"));
                  objItemBean.setNpcreatedby(rs.getString("NPCREATEDBY"));
                  list.add(objItemBean);
              }
          }
        
          objHashMap.put("strMessage",strMessage);
          objHashMap.put("objArrayListCustomer",list); 
          
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
  
   
  /***********************************************************************************************************************************
  * Motivo: Obtiene el detalle de la aplicación.
  * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
  * <br>Fecha: 06/05/2010
  ************************************************************************************************************************************/ 
  public  HashMap getAplicationDetail(long lnpbusinesssolutionid) throws SQLException {

   ItemBean objItemBean = new ItemBean();        
   String strMessage=null;
   HashMap hshData=new HashMap();
   OracleCallableStatement cstmt = null;
   ResultSet rs = null;
   Connection conn=null;
   long lnpproductlineid=0;
   long lnpproductid=0;
   String strnpproductname = null;
   String strproductlinename = null;
   
   try{
      String sqlStr =  "BEGIN ORDERS.NP_ORDERS29_PKG.SP_GET_APLICATION_DETAIL(?, ?, ?, ?,?,?); END;";
      conn = Proveedor.getConnection();
     
       cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
       cstmt.setLong(1, lnpbusinesssolutionid);
       cstmt.registerOutParameter(2, OracleTypes.NUMERIC);
       cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
       cstmt.registerOutParameter(4, OracleTypes.NUMERIC);
       cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
       cstmt.registerOutParameter(6, Types.VARCHAR);
       cstmt.execute();          	
       strMessage = cstmt.getString(6);
       if( strMessage == null ){
          lnpproductlineid = cstmt.getLong(2);
          strproductlinename = cstmt.getString(3);
          lnpproductid     = cstmt.getLong(4);
          strnpproductname = cstmt.getString(5);
          objItemBean.setNpproductlineid(lnpproductlineid);
          objItemBean.setNpproductlinename(strproductlinename);
          objItemBean.setNpproductid(lnpproductid);
          objItemBean.setNpproductname(strnpproductname);
       }         
       hshData.put("objItemBean",objItemBean);
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
 
 
  
  /***********************************************************************************************************************************
  * Motivo: Actualiza el detalle de la aplicación.
  * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
  * <br>Fecha: 06/05/2010
  ************************************************************************************************************************************/ 
  public HashMap doUpdateSalesData(ItemBean objItemBean, String strLogin,Connection conn) throws Exception, SQLException { 
        
        OracleCallableStatement cstmt = null;
        String strMessage = null; 
        HashMap hshData=new HashMap();
        long lSalesDataId=0;
        
         try{     
               String strSql = "BEGIN ORDERS.NP_ORDERS29_PKG.SP_UPD_SALES_DATA( ?, ?, ?, ?, ?); END;"; 
               cstmt = (OracleCallableStatement)conn.prepareCall(strSql); 
               cstmt.setLong(1,objItemBean.getNpbusinesssolutionid() );
               cstmt.setLong(2,objItemBean.getNpproductlineid());
               cstmt.setLong(3, objItemBean.getNpproductid());
               cstmt.setString(4, strLogin);
               cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
               cstmt.executeUpdate();
               strMessage = cstmt.getString(5);
               lSalesDataId = objItemBean.getNpbusinesssolutionid();
    
               System.out.println("Mensaj de Error -->"+strMessage);    
               hshData.put("strMessage",strMessage);
               hshData.put("lSalesDataId",lSalesDataId+"");

        }catch (Exception e) {
                logger.error(formatException(e));
                strMessage = e.getClass() + " - " + e.getMessage();
        }finally{
            try{
                closeObjectsDatabase(null,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
      }
    
    return hshData;
  }
  
  
  
  /***********************************************************************************************************************************
  * Motivo: Actualiza el estado de la aplicación.
  * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
  * <br>Fecha: 06/05/2010
  ************************************************************************************************************************************/ 
  public String doUpdateSalesDataStatus(long lOrderId, long lCustomerId,long lDivisionId, long lSolutionId,
                                        String strStatus, String strLogin, Connection conn) throws Exception, SQLException { 
        
        OracleCallableStatement cstmt = null;
        String strMessage = null; 
        HashMap hshData=new HashMap();
        long lSalesDataId=0;
        
         try{     
  
               String strSql = "BEGIN ORDERS.NP_ORDERS29_PKG.SP_UPD_BUSINESS_SOLUTION_APP( ?, ?, ?, ?, ?, ?, ?); END;"; 
               cstmt = (OracleCallableStatement)conn.prepareCall(strSql); 
               cstmt.setLong(1,lOrderId);
               cstmt.setLong(2,lCustomerId);
               cstmt.setLong(3,lDivisionId);
               cstmt.setLong(4,lSolutionId);
               cstmt.setString(5,strStatus);
               cstmt.setString(6,strLogin);
               cstmt.registerOutParameter(7, OracleTypes.VARCHAR);
               cstmt.executeUpdate();
               strMessage = cstmt.getString(7);

        }catch (Exception e) {
                logger.error(formatException(e));
                strMessage = e.getClass() + " - " + e.getMessage();
        }finally{
            try{
                closeObjectsDatabase(null,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
      }
    
    return strMessage;
  }
  
  
  
  /***********************************************************************************************************************************
  * Motivo:Validación al grabar una orden con aplicaciones de venta data.
  * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
  * <br>Fecha: 06/05/2010
  ************************************************************************************************************************************/ 
  public String getValidateServiceApp(long lCustomerId,long lOrderId,long lDivisionId,Connection conn) throws Exception, SQLException { 
        
        OracleCallableStatement cstmt = null;
        String strMessage = null; 
        HashMap hshData=new HashMap();
        long lSalesDataId=0;
        
         try{     
               String strSql = "BEGIN ORDERS.NP_ORDERS29_PKG.SP_VALIDATE_SERVICE_APP( ?, ?, ?, ?); END;"; 
               cstmt = (OracleCallableStatement)conn.prepareCall(strSql); 
               cstmt.setLong(1,lCustomerId );
               cstmt.setLong(2,lOrderId );
               cstmt.setLong(3,lDivisionId );
               cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
               cstmt.execute();
               strMessage = cstmt.getString(4);

        }catch (Exception e) {
                logger.error(formatException(e));
                strMessage = e.getClass() + " - " + e.getMessage();
        }finally{
            try{
                closeObjectsDatabase(null,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
      }
    
    return strMessage;
  }
  
  
  /***********************************************************************************************************************************
  * Motivo:Eliminar un registro de la  aplicacion de venta data.
  * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
  * <br>Fecha: 06/05/2010
  ************************************************************************************************************************************/ 
  public HashMap delSalesData(long lUnkwDataId,Connection conn) throws Exception,SQLException {

   String  strMessage=null;
   HashMap hshData =new HashMap();
   OracleCallableStatement cstmt = null;
   int i = 0;
   try{
   
      String sqlStr ="BEGIN ORDERS.NP_ORDERS29_PKG.SP_DELETE_SALES_DATA( ?, ?); END;"; 
   
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setLong(1, lUnkwDataId);                   
      cstmt.registerOutParameter(2, Types.VARCHAR);               
      i  = cstmt.executeUpdate();
      strMessage = cstmt.getString(2);               
    
   }catch(Exception ex){
     throw new Exception(ex);
   }finally{
     closeObjectsDatabase(null,cstmt,null);
   }
   
   hshData.put("strMessage",strMessage);
   hshData.put("iReturn",i+"");
   
   return hshData;
   
  }      
  
  
    
  /***********************************************************************************************************************************
  * Motivo:Graba las aplicaciones activas asociadas a la Orden
  * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
  * <br>Fecha: 06/05/2010
  ************************************************************************************************************************************/ 
  public String insSalesDataActive(long lOrderId,long lCustomerId,long lDivisionId,long lSolutionId, String strLogin,Connection conn) throws Exception, SQLException { 
        
        OracleCallableStatement cstmt = null;
        String strMessage = null; 
        HashMap hshData=new HashMap();
        long lSalesDataId=0;
        
         try{     
               String strSql = "BEGIN ORDERS.NP_ORDERS29_PKG.SP_INS_APLICATION_ACTIVE( ?, ?, ?, ?, ?, ?); END;"; 
               cstmt = (OracleCallableStatement)conn.prepareCall(strSql); 
               cstmt.setLong(1,lOrderId );
               cstmt.setLong(2,lCustomerId );
               cstmt.setLong(3,lDivisionId );
               cstmt.setLong(4,lSolutionId );
               cstmt.setString(5,strLogin);
               cstmt.registerOutParameter(6, OracleTypes.VARCHAR);
               cstmt.execute();
               strMessage = cstmt.getString(6);

        }catch (Exception e) {
                logger.error(formatException(e));
                strMessage = e.getClass() + " - " + e.getMessage();
        }finally{
            try{
                closeObjectsDatabase(null,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
      }
    
    return strMessage;
  }
 

}