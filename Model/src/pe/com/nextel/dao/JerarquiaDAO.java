package pe.com.nextel.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.ArrayList;
import java.util.HashMap;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import pe.com.nextel.util.Constante;


/**
 * Motivo: Clase DAO que contiene acceso a las tablas de Órdenes.
 * <br>Realizado por: <a href="mailto:alexis.gamarra@nextel.com.pe">Alexis Gamarra Cano</a>
 * <br>Fecha: 24/08/2009
 * @see GenericDAO
 */
public class JerarquiaDAO extends GenericDAO {
  /**
   Method : checkStructRule
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Alexis Gamarra  24/08/2009  Creación
   */
	public String checkStructRule(int ruleid, String strNpsalesstructid) throws SQLException, Exception{    
      String strCheckResult = "N";
      if(strNpsalesstructid!=null && strNpsalesstructid.trim().length()>0){
        Connection conn=null;
        OracleCallableStatement cstmt = null;
        try{
            String sqlStr =  " { ? = call websales.FXI_CHECK_STRUCT_RULE_2( ?, ? ) } ";
            
            conn = Proveedor.getConnection();
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.registerOutParameter(1, Types.VARCHAR);
            cstmt.setInt(2, ruleid);
            cstmt.setInt(3, Integer.parseInt(strNpsalesstructid));
            
            cstmt.execute();
            strCheckResult = cstmt.getString(1);
            
            if(strCheckResult.equalsIgnoreCase("S")){
              strCheckResult = "S";
            }else{
              strCheckResult = "N";
            }
        }catch(Exception e){
           logger.error(formatException(e));
        }finally{
          try{
            closeObjectsDatabase(conn,cstmt,null); 
          }catch (Exception e) {
            logger.error(formatException(e));
          }
        }
      }
      return(strCheckResult);
   }
   public int getParentForAssist(int an_salesstructdefaultid) throws SQLException, Exception {
      int wn_salesstructid = 0;
      OracleCallableStatement cstmt = null;
      String strMessage = null;
      Connection conn=null;
      String strSql = "BEGIN WEBSALES.NP_SALES_STRUCTURE_PKG.SP_GET_PARENT_FOR_ASSIST(?, ?, ?); END;";

      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(strSql);
         cstmt.setInt(1, an_salesstructdefaultid);
         cstmt.registerOutParameter(2, OracleTypes.NUMBER);
         cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
         cstmt.execute();
         strMessage = cstmt.getString(3);
         if(strMessage==null || strMessage.startsWith(Constante.SUCCESS_ORA_RESULT)){
            wn_salesstructid = cstmt.getInt(2);
         }
      }   
      catch (Exception e) {
         e.printStackTrace();
         throw new Exception(e);
      }
      finally{
         try{
          closeObjectsDatabase(conn,cstmt,null); 
         }catch (Exception e) {
            logger.error(formatException(e));
         }         
       }
        return(wn_salesstructid);
    }
    
    public int getPrvdStructAssist(int an_salesstructdefaultid) throws SQLException, Exception {
      int wn_swprovidergrpid = 0;
      OracleCallableStatement cstmt = null;
      String strMessage = null;
      Connection conn=null;
      String strSql = "BEGIN WEBSALES.NP_SALES_STRUCTURE_PKG.SP_GET_PRVDRGRP_SSTRUCT_ASSIST(?, ?, ?); END;";

      try{
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement) conn.prepareCall(strSql);
         cstmt.setInt(1, an_salesstructdefaultid);
         cstmt.registerOutParameter(2, OracleTypes.NUMBER);
         cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
         cstmt.execute();
         strMessage = cstmt.getString(3);
         if(strMessage==null || strMessage.startsWith(Constante.SUCCESS_ORA_RESULT)){
            wn_swprovidergrpid = cstmt.getInt(2);
         }
      }   
      catch (Exception e) {
         e.printStackTrace();
         throw new Exception(e);
      }
      finally{
         try{
          closeObjectsDatabase(conn,cstmt,null); 
         }catch (Exception e) {
            logger.error(formatException(e));
         }         
       }
        return(wn_swprovidergrpid);
    }
    
    public String getLastPosition(int an_salesstructdefaultid) throws SQLException, Exception{    
      String strCheckResult = null;
      Connection conn=null;
      OracleCallableStatement cstmt = null;
      try{
          String sqlStr =  " { ? = call WEBSALES.NP_SALES_STRUCTURE01_PKG.FX_CHECK_LAST_POSITION2(?) } ";
          
          conn = Proveedor.getConnection();
          cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
          cstmt.registerOutParameter(1, Types.VARCHAR);
          cstmt.setInt(2, an_salesstructdefaultid);
          
          cstmt.execute();
          strCheckResult = cstmt.getString(1);
      }catch(Exception e){
         e.printStackTrace();
         logger.error(formatException(e));
      }finally{
        try{
          closeObjectsDatabase(conn,cstmt,null); 
        }catch (Exception e) {
          logger.error(formatException(e));
        }        
      }
       return(strCheckResult);
   }
   /**
   Method : getDataField
   Purpose: Obtener informacion del asistente   
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Cesar Barzola    30/09/2009  Creación
  **/
  public HashMap getDataField(int an_ruleid, String av_retriverepresentative,int an_salesstructid, int an_providergrpid)  throws Exception,SQLException
  {
    Connection conn = null;
    HashMap contAux = new HashMap();
    OracleCallableStatement cstmt = null;
    ResultSet rs = null;
    String strMsgError = null;
    try {
      conn = Proveedor.getConnection();
      
      String sqlStr =  "BEGIN WEBSALES.SPI_GET_DATA_FIELD(?, ?, ?, ?, ?, "+//5
                                                         "?, ?, ?, ?, ?,?); END;";//10
      
      cstmt=(OracleCallableStatement)conn.prepareCall(sqlStr);
      
      cstmt.setInt(1, an_ruleid);
      cstmt.setString(2, av_retriverepresentative);
      cstmt.setInt(3, an_salesstructid);
      cstmt.setInt(4, an_providergrpid);
      cstmt.setNull(5, OracleTypes.NULL);
      cstmt.setNull(6, OracleTypes.NULL);
      cstmt.registerOutParameter(7, OracleTypes.VARCHAR);
      cstmt.registerOutParameter(8, OracleTypes.CURSOR);
      cstmt.registerOutParameter(9, OracleTypes.NUMBER);
      cstmt.registerOutParameter(10, OracleTypes.NUMBER);
      cstmt.registerOutParameter(11, OracleTypes.VARCHAR);
      
      cstmt.execute();
      
      strMsgError = cstmt.getString(11);
      if(strMsgError!=null){
        contAux.put("wv_message", strMsgError);
        System.out.println("wv_message="+strMsgError);
      }
      
      if(strMsgError==null || strMsgError.equals("") || strMsgError.startsWith(Constante.SUCCESS_ORA_RESULT)){
        contAux = new HashMap();
        String strName = cstmt.getString(7);
        if(strName!=null){
          System.out.println("wv_name="+strName);
          contAux.put("wv_name", strName);
        }
        int parentsalesstructid=cstmt.getInt(10);
        contAux.put("wv_parentsalesstructid",String.valueOf(parentsalesstructid));
        
        int intCount = cstmt.getInt(9);
        contAux.put("wn_count", new Integer(intCount));
        System.out.println("wn_count="+intCount);
        
        ArrayList arrProvidergrpid = new ArrayList();
        ArrayList arrSelected = new ArrayList();
        ArrayList arrSwname = new ArrayList();

        try{
          rs = cstmt.getCursor(8);
          if (rs!=null){
            while(rs.next()){
              arrProvidergrpid.add(new Integer(rs.getInt(2)));
              arrSwname.add(rs.getString(3));
              arrSelected.add(rs.getString(4));
            }
          }
        }catch(Exception e1){}
        
        contAux.put("arrProvidergrpid", arrProvidergrpid);
        contAux.put("arrSelected", arrSelected);
        contAux.put("arrSwname", arrSwname);
      }
    }catch(Exception e2){
      logger.error(formatException(e2));
    }finally{
      closeObjectsDatabase(conn, cstmt, rs);
    }
    return contAux;
  }
}