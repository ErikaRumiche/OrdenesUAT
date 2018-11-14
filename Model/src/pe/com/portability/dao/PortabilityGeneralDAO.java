package pe.com.portability.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.HashMap;

import oracle.jdbc.OracleCallableStatement;

import pe.com.nextel.dao.GenericDAO;
import pe.com.nextel.dao.Proveedor;


public class PortabilityGeneralDAO   extends GenericDAO{
  public PortabilityGeneralDAO()
  {
  }
  
  
   /**************************************************************************************************
   Method : getSectionDocumentValidate
   Purpose: Metodo que obtiene los valores de una tabla en base a su descripcion: PORTABILITY.NP_CONFIGURATION
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Karen Salvador  28/01/2010  Creación
   ***************************************************************************************************/
	public HashMap getSectionDocumentValidate(String strNptable, String strNpDescripcion, String strNpvalue) throws SQLException, Exception {
    
    Connection conn = null; 
    ResultSet rs=null;
    OracleCallableStatement cstm = null;
    String strMessage = null;
    String strValue = null;
    HashMap  objHashMap = new HashMap();
      
    try{
         String strSql = "BEGIN ORDERS.NP_PORTA_CONFIGURATION_PKG.SP_GET_VALUEXDESC(?,?,?,?); END;";      
         conn = Proveedor.getConnection();
         cstm = (OracleCallableStatement) conn.prepareCall(strSql);
         cstm.setString(1,strNptable);
         cstm.setString(2,strNpDescripcion);
         cstm.registerOutParameter(3, Types.VARCHAR);
         cstm.registerOutParameter(4, Types.VARCHAR);
         cstm.execute();
         strValue    = cstm.getString(3);
         strMessage  = cstm.getString(4);
         
         objHashMap.put("strValue",strValue);
         objHashMap.put("strMessage",strMessage);
          
      }catch (Exception e) {
        logger.error(formatException(e));
        objHashMap.put("strMessage",e.getMessage());
      } finally{
          try{
            closeObjectsDatabase(conn,cstm,rs); 
          }catch (Exception e) {
              logger.error(formatException(e));
         }     
      }
      return objHashMap;
      
  }
}