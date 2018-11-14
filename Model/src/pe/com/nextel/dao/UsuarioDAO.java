package pe.com.nextel.dao;

import java.math.BigInteger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.HashMap;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;


public class UsuarioDAO  extends GenericDAO{

    /**
     Method : getUserRol
     Purpose: Permite Mostrar una lista de todas las  opciones.
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     Lee Rosales     06/09/2007  Creación
     */
    
  public static int getUserRol(int idScreenOption, int idApp, String idCode) throws Exception, SQLException {
      
    Connection conn = null; 
    OracleCallableStatement cstmt = null;
    int intResult= 0;
      
    BigInteger bigDecimal = new BigInteger(""+idScreenOption);      
    String strSql = " { ? = call WEBSALES.NPSL_ACCESS_PKG.FX_GET_ROL( ?, ?, ? ) } ";               
      
    try {      
      conn = Proveedor.getConnection();               
      cstmt = (OracleCallableStatement)conn.prepareCall(strSql);           
      cstmt.registerOutParameter(1,OracleTypes.NUMBER);
      cstmt.setInt(2, idScreenOption);
      cstmt.setInt(3, idApp);
      cstmt.setString(4, idCode);
      cstmt.executeUpdate();          
      intResult = cstmt.getInt(1);
    }catch (SQLException e) {
      System.out.println("getUserRol nError Nro.: "+ e.getClass() + " " + e.getErrorCode() + " nMensaje:   -- >" + e.getMessage()+"\n");
      System.out.println("e.getErrorCode()  : "+e.getErrorCode() );
    }finally {
      closeObjectsDatabase(conn,cstmt,null);
    }
    return intResult;
  }

    
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
   * Motivo: Verifica si el usuario tiene permiso para una determinada sección
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 24/07/2007
   * @param     iScreenoptionid  
   * @param     iUserid        
   * @param     iAppid    
   * @return    int 
   */
   
  public int getRol(int iScreenoptionid, long iUserid, int iAppid) throws SQLException, Exception{
    int iReturnValue = 0;
    Connection conn=null;
    OracleCallableStatement cstmt = null;
     
    String sqlStr = "BEGIN WEBSALES.SPI_GET_ROL( ?, ?, ?, ?); END;";
    
    try{
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
         
      cstmt.setInt(1, iScreenoptionid);
      cstmt.setLong(2, iUserid);
      
      if (iAppid == -1) 
        cstmt.setNull(3, Types.INTEGER);
      else 
        cstmt.setInt(3, iAppid);
         
      cstmt.registerOutParameter(4, Types.NUMERIC);          
      cstmt.execute();
         
      iReturnValue = cstmt.getInt(4); 
      
    }catch(Exception e){
      e.printStackTrace();
    }finally{
      closeObjectsDatabase(conn,cstmt,null);
    }
    
    return iReturnValue;
  }  
    
    
   /**
   * Motivo: Verifica si el usuario tiene permiso para una determinada sección
   * <br>Realizado por: <a href="mailto:carmen.gremios@nextel.com.pe">Carmen Gremios</a>
   * <br>Fecha: 24/07/2007
   * @param     iScreenoptionid  
   * @param     iLevel        
   * @param     strCode    
   * @return    int 
   */
   public int getRol(int iScreenoptionid, int iLevel, String strCode) 
   throws SQLException, Exception
   {
      int iReturnValue = 0;
      
      OracleCallableStatement cstmt = null;
      Connection conn=null; 
      String sqlStr = "BEGIN WEBSALES.SPI_GET_ROL2( ?, ?, ?, ?); END;";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
         
         cstmt.setInt(1, iScreenoptionid);
         cstmt.setInt(2, iLevel); 
         cstmt.setString(3,strCode);              
         cstmt.registerOutParameter(4, Types.NUMERIC);          
         cstmt.execute();
         iReturnValue = cstmt.getInt(4); 
      }
      catch(Exception e){
         throw new Exception(e);
      }
      
      finally{
        closeObjectsDatabase(conn,cstmt,null);
      }      
      return iReturnValue;
   }  
   
   
   /**
   * Motivo: Verifica si el usuario tiene permiso para una determinada sección
   * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales ;-)</a>
   * <br>Fecha: 29/04/2008
   * @return    HashMap 
   */
   public HashMap getSessionId() throws Exception,SQLException{
   
      OracleCallableStatement cstmt = null;
      Connection conn = null; 
      String strSessionId = null, strMessage = null;
      HashMap objHashMap = new HashMap();
      String sqlStr = "BEGIN ORDERS.NP_ORDERS01_PL_PKG.SP_GET_SESSIONID( ?, ?); END;";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);         
         cstmt.registerOutParameter(1, OracleTypes.VARCHAR);
         cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
         cstmt.execute();                           
         strMessage   = cstmt.getString(2); 
         
         if( strMessage == null ) strSessionId = cstmt.getString(1); 
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
        closeObjectsDatabase(conn,cstmt,null);
      }      
      objHashMap.put("strMessage",strMessage);
      objHashMap.put("strSessionId",strSessionId);
      
      return objHashMap;
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

   /**
   * Motivo: Obtiene resultados de permisos para multiples Screen Option
   * <br>Realizado por: <a href="mailto:hugo.moreno@nextel.com.pe">Lee Rosales ;-)</a>
   * <br>Fecha: 12/06/2008
   * @return    HashMap 
   */
   public HashMap getRolMult(String strScreenOption, long iUserid, int iAppid) throws Exception,SQLException{
   
      OracleCallableStatement cstmt = null;      
      Connection conn = null; 
      String strResult = null;
      String strMessage = null;
      HashMap objHashMap = new HashMap();
      System.out.println("[UsuarioDAO][strScreenOption]"+strScreenOption);
      System.out.println("[UsuarioDAO][iUserid]"+iUserid);
      System.out.println("[UsuarioDAO][iAppid]"+iAppid);
      String sqlStr = "BEGIN WEBSALES.SPI_GET_ROL_MULT( ?, ?, ?, ?,?); END;";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
         
         cstmt.setString(1, strScreenOption);
         cstmt.setLong(2, iUserid);
         cstmt.setInt(3, iAppid);
         cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
         cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
         cstmt.execute();                  
         strResult = cstmt.getString(4); 
         strMessage = cstmt.getString(5);  
         System.out.println("[UsuarioDAO][getRolMult][strMessage]"+strMessage);
         System.out.println("[UsuarioDAO][getRolMult][strResult]"+strResult);
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn,cstmt,null);
      }      
      objHashMap.put("strMessage",strMessage);      
      objHashMap.put("strResult",strResult);            
      return objHashMap;
   }  
   
   
    /**
   * Motivo: Verifica si el usuario tiene permiso para una determinada sección en la Orden a modo de consulta
   * <br>Realizado por: <a href="mailto:karen.salvador@nextel.com.pe">Karen Salvador</a>
   * <br>Fecha: 05/07/2008
   * @param     lOrderid
   * @param     iUserid        
   * @param     iAppid    
   * @return    int 
   */
   
   public int getPermissionDetail(long lOrderid, long iUserid, int iAppid) 
   throws SQLException, Exception
   {
      int iReturnValue = 0;
      Connection conn=null;
      OracleCallableStatement cstmt = null;
      
      String sqlStr = "BEGIN ORDERS.SPI_GET_PERMISSION_DETAIL( ?, ?, ?, ?); END;";
      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);      
         cstmt.setLong(1, lOrderid);
         cstmt.setLong(2, iUserid);
         if (iAppid == -1) 
            cstmt.setNull(3, Types.INTEGER);
         else 
            cstmt.setInt(3, iAppid);
         
         cstmt.registerOutParameter(4, Types.NUMERIC);          
         cstmt.execute();
         
         iReturnValue = cstmt.getInt(4); 
      }
      catch(Exception e){
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn,cstmt,null);
      }            
      return iReturnValue;
   }  
    
    public int getPermissionEdit(Connection conn,int userId,int orderId) throws SQLException, Exception{
      OracleCallableStatement cstmt = null;
      String sql = "BEGIN ORDERS.NP_ORDERS02_PKG.SP_GET_ORDER_PERMISSION_EDIT(?, ?, ?, ?); END;";
      int returnValue = 0;
      String msgError=null;
      
      try{
        cstmt = (OracleCallableStatement)conn.prepareCall(sql);
        cstmt.setInt(1, userId);
        cstmt.setInt(2, orderId);
        cstmt.registerOutParameter(3, OracleTypes.INTEGER);
        cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
        cstmt.execute();
          
        returnValue = cstmt.getInt(3);
        msgError    = cstmt.getString(4);
        if (msgError!=null){
            throw new Exception(msgError);
        }
       }catch(Exception e){
           throw new Exception(e);
       }finally 
       {
         close(cstmt);
       }
      return returnValue;
    }
    
    
    public String getPermissionViewOrder(Connection conn,int userId,int orderId,int appId) throws SQLException, Exception{
      OracleCallableStatement cstmt = null;
      String sql = "BEGIN ORDERS.NP_REPORT03_PKG.SP_ORDERS_VIEW_FORM(?,?,?,?,?); END;";            
      ResultSet rs = null;
      String returnValue =null;
      String msgError=null;
      
      try{
        cstmt = (OracleCallableStatement)conn.prepareCall(sql);
        cstmt.setInt(1, userId);
        cstmt.setInt(2, orderId);
        cstmt.setInt(3, appId);
        cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
        cstmt.execute();
          
        returnValue = cstmt.getString(4);
        msgError    = cstmt.getString(5);
        if (msgError!=null){
            throw new Exception(msgError);
        }
       }catch(Exception e){
           throw new Exception(e);     
        
       }finally 
       {
         close(cstmt);
       }
      return returnValue;
    }  

    public int getRol(Connection conn,int screenoptionid, long userid, int appid) throws SQLException, Exception{
      int iReturnValue = 0;
      
      OracleCallableStatement cstmt = null;
       
      String sqlStr = "BEGIN WEBSALES.SPI_GET_ROL( ?, ?, ?, ?); END;";
      
      try{
        //conn = Proveedor.getConnection();
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
           
        cstmt.setInt(1, screenoptionid);
        cstmt.setLong(2, userid);
        
        if (appid == -1) 
          cstmt.setNull(3, Types.INTEGER);
        else 
          cstmt.setInt(3, appid);
           
        cstmt.registerOutParameter(4, Types.NUMERIC);          
        cstmt.execute();
           
        iReturnValue = cstmt.getInt(4); 
        
      }catch(Exception e){
        throw new Exception(e);
      }finally{
        close(cstmt);       
      }
      
      return iReturnValue;
    }      
    

}
