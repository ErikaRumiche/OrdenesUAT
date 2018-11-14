package pe.com.nextel.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import pe.com.nextel.bean.SpecificationBean;


public class SpecificationDAO extends GenericDAO{

   /**
   Method : getSpecificationUserList
   Purpose: Obtiene el listado de las Espcificaciones que tiene asignadas
            un determinado usuario para crear una orden
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Lee Rosales     03/11/2007  Creación
   */
   
   public HashMap getSpecificationUserList(long customerId, String strLogin, String strGeneratorType, String strOpportunityTypeId, String strFlagGenerator, long lngDivisionId, long lngSpecificationId, long lngGeneratorId) throws Exception, SQLException{
      
      //Tipo de Datos para la conexión a la BD
      OracleCallableStatement cstmt = null;
      ResultSet rs = null;
      Connection conn=null;             
      HashMap objHashMap = new HashMap();
      //Tipos de Datos para obtener la data de la BD
      ArrayList list = new ArrayList();
      SpecificationBean objSpecificationBean =  null;
      String strMessage = null;      
      String sqlStr = "BEGIN ORDERS.NP_ORDERS03_PKG.SP_GET_SPECIFICATION_LIST(?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?); END;";
      try{
         int intCountSolutions = 0;
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
         
         if ( customerId == 0 )
           cstmt.setNull(1, OracleTypes.NUMBER);
         else
           cstmt.setLong(1, customerId);
         
         cstmt.setString(2, strLogin);
         
         if( strGeneratorType.equals("") )
           cstmt.setNull(3, OracleTypes.VARCHAR);
         else
           cstmt.setString(3, strGeneratorType);
         
         cstmt.setString(4, strOpportunityTypeId);
         cstmt.setString(5, strFlagGenerator);
         
         if ( lngDivisionId == 0 )
           cstmt.setNull(6, OracleTypes.NUMBER);
         else
           cstmt.setLong(6, lngDivisionId);
         
         if( lngSpecificationId == 0 )
           cstmt.setNull(7, OracleTypes.NUMBER);
         else
           cstmt.setLong(7, lngSpecificationId);
         
         //cirazabal
         if( lngGeneratorId == 0 )
           cstmt.setNull(8, OracleTypes.NUMBER);
         else
           cstmt.setLong(8, lngGeneratorId);    
         
         cstmt.registerOutParameter(9, OracleTypes.NUMBER);
         cstmt.registerOutParameter(10, OracleTypes.CURSOR);
         cstmt.registerOutParameter(11, OracleTypes.VARCHAR);         
         cstmt.executeUpdate();         
         strMessage = cstmt.getString(11);         
         if( strMessage == null ){
           intCountSolutions  = cstmt.getInt(9);
           rs = cstmt.getCursor(10);
           //rs = (ResultSet)cstmt.getObject(9);     
           while (rs.next()) {
              objSpecificationBean = new SpecificationBean();
              objSpecificationBean.setNpDivisionId(rs.getLong("npdivisionid"));
              objSpecificationBean.setNpdivisionname(rs.getString("division"));
              objSpecificationBean.setNpcategorynname(rs.getString("categoria"));
              objSpecificationBean.setNpSpecification(rs.getString("subcategoria"));
              objSpecificationBean.setNpSpecificationId(rs.getLong("npspecificationid"));
              list.add(objSpecificationBean);
           }
         
         }
         
         objHashMap.put("intCntSol",""+intCountSolutions);
         objHashMap.put("strMessage",strMessage);
         objHashMap.put("objArrayList",list);
      }
      catch (Exception e) {
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn,cstmt,rs);
      }
      return objHashMap;                                                       
   }
   
   /**
   Method : getSpecificationUserList
   Purpose: Obtiene el listado de las Espcificaciones que tiene asignadas
            un determinado usuario para crear una orden
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Lee Rosales     03/11/2007  Creación
   */
   
   public HashMap getSpecificationUserList(long userId, long lngDivisionId, long lngSpecificationId, String strObjectType, String strFlagGenerator) throws Exception, SQLException{
      
      //Tipo de Datos para la conexión a la BD
      OracleCallableStatement cstmt = null;
      ResultSet rs = null;
      Connection conn=null;             
      HashMap objHashMap = new HashMap();
      //Tipos de Datos para obtener la data de la BD
      ArrayList list = new ArrayList();
      SpecificationBean objSpecificationBean =  null;
      String strMessage = null;
      String sqlStr = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_GET_USER_SPECIFICATION_LIST(?, ?, ?, ?, ?, ?, ?, ?); END;";
      try{
         int intCountSolutions = 0;
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
         
         cstmt.setLong(1, userId);
         if ( lngDivisionId == 0 )
           cstmt.setNull(2, OracleTypes.NUMBER);
         else
           cstmt.setLong(2, lngDivisionId);
         
         if( lngSpecificationId == 0 )
           cstmt.setNull(3, OracleTypes.NUMBER);
         else
           cstmt.setLong(3, lngSpecificationId);         
         cstmt.setString(4, strObjectType);
         cstmt.setString(5, strFlagGenerator);
         cstmt.registerOutParameter(6, OracleTypes.NUMBER);         
         cstmt.registerOutParameter(7, OracleTypes.CURSOR);
         cstmt.registerOutParameter(8, OracleTypes.VARCHAR);   
         cstmt.executeUpdate();                  
         strMessage = cstmt.getString(8);         
         if( strMessage == null ){
           intCountSolutions  = cstmt.getInt(6);           
           rs = (ResultSet)cstmt.getObject(7);
           while (rs.next()) {
              objSpecificationBean = new SpecificationBean();
              objSpecificationBean.setNpcategorynname(rs.getString("categoria"));
              objSpecificationBean.setNpSpecification(rs.getString("subcategoria"));
              objSpecificationBean.setNpSpecificationId(rs.getLong("npspecificationid"));
              list.add(objSpecificationBean);
           }
         
         }         
         objHashMap.put("intCntSol",""+intCountSolutions);
         objHashMap.put("strMessage",strMessage);
         objHashMap.put("objArrayList",list);
      }
      catch (Exception e) {
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn,cstmt,rs);
      }    
      return objHashMap;                                                       
   }

  /**
   Method : getProductLineValueList
   Purpose: Obtener los valores de LINEA PRODUCTOS
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Lee Rosales     22/09/2007  Creación
   */
   
   public static String getConsigmentValue(int intSpecificationID, String strMessage) throws SQLException, Exception{
      
       String  strnpownhand = "";
       String  strMessage2 = "";
       Connection conn = null; 
       OracleCallableStatement cstm = null;              
       String strSql = "{? = call orders.np_orders06_pkg.fx_get_npownhandset(?)}";
       try{
          conn = Proveedor.getConnection();
          cstm = (OracleCallableStatement) conn.prepareCall(strSql);           
          cstm.registerOutParameter(1,OracleTypes.VARCHAR);       
          cstm.setInt(2,intSpecificationID);           
          cstm.execute();           
          strnpownhand = cstm.getString(1);           
       }
       catch (Exception e) {
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn,cstm,null);        
      }
      return strnpownhand;
   }


/***********************************************************************************************************************************
 * Motivo: Obtiene el listado de las Soluciones que tienen asignadas una determinada categoría al crear la orden.
 * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
 * <br>Fecha: 22/04/2009    
 ************************************************************************************************************************************/ 
  public HashMap getSolutionSpecificationList(long lngSpecificationId, long lngDivisionId, long lngSiteId) throws SQLException, Exception{             
        OracleCallableStatement cstmt = null; 
        ResultSet rs = null;
        String strMessage=null;
        Connection conn=null;
        HashMap objHashMap = new HashMap();
        ArrayList list = new ArrayList();
        SpecificationBean objSpecificationBean =  null;
     		System.out.println("getSolutionSpecificationList");
            System.out.println("lngSpecificationId:"+ lngSpecificationId);
            System.out.println("lngDivisionId:"+ lngDivisionId);
            System.out.println("lngSiteId:"+ lngSiteId);
        try{
          //String strSql = " BEGIN ORDERS.NP_FIXED_PHONE.SP_GET_SOLUTION_SPEC_LIST(?,?,?,?,?); END;";        
          String strSql = " BEGIN ORDERS.NP_ORDERS06_PKG.SP_GET_SOLUTION_SPEC_LIST(?,?,?,?,?); END;";        
          conn = Proveedor.getConnection();   
          cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
          cstmt.setLong(1, lngSpecificationId);
          cstmt.setLong(2, lngDivisionId);
          
          if( lngSiteId == 0 )
            cstmt.setNull(3, OracleTypes.NUMBER);
          else
            cstmt.setLong(3, lngSiteId); 
            
          cstmt.registerOutParameter(4, OracleTypes.CURSOR);
          cstmt.registerOutParameter(5, OracleTypes.VARCHAR);  
          cstmt.executeUpdate();         
          strMessage = cstmt.getString(5);  
          if( strMessage == null ){
              rs = cstmt.getCursor(4);
              while (rs.next()) {
                  objSpecificationBean = new SpecificationBean();
                  objSpecificationBean.setNpSolutionId(rs.getLong("npsolutionid"));
                  objSpecificationBean.setNpsolutionname(rs.getString("npname"));
                  objSpecificationBean.setNpSolutiontype(rs.getString("nptypesolution")); 
                  list.add(objSpecificationBean);
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
 * Motivo: Obtiene los registros de las soluciones configuradas.
 * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
 * <br>Fecha: 22/04/2009    
 ************************************************************************************************************************************/ 
  public HashMap getSolutionType() throws SQLException, Exception{             
        OracleCallableStatement cstmt = null; 
        ResultSet rs = null;
        String strMessage=null;
        Connection conn=null;
        HashMap objHashMap = new HashMap();
        ArrayList list = new ArrayList();
        SpecificationBean objSpecificationBean =  null;
        String strSolutionType= null;
        try{
          String strSql = " BEGIN ORDERS.NP_ORDERS06_PKG.SP_GET_SOLUTION_TYPE(?,?); END;";        
          conn = Proveedor.getConnection();   
          cstmt = (OracleCallableStatement)conn.prepareCall(strSql);
          cstmt.registerOutParameter(1, OracleTypes.CURSOR);
          cstmt.registerOutParameter(2, OracleTypes.VARCHAR);  
          cstmt.executeUpdate();  
          strMessage = cstmt.getString(2);  
           if( strMessage == null ){
              rs = cstmt.getCursor(1);
              while (rs.next()) {
                  objSpecificationBean = new SpecificationBean();
                  objSpecificationBean.setNpSolutionId(rs.getLong("npsolutionid"));
                  objSpecificationBean.setNpDivisionId(rs.getLong("npdivisionid"));
                  objSpecificationBean.setNpsolutionname(rs.getString("npname"));
                  objSpecificationBean.setNpSolutiongroupid(rs.getLong("npsolutiongroupid"));
                  objSpecificationBean.setNpSolutiontype(rs.getString("nptypesolution")); 
                  list.add(objSpecificationBean);
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

}