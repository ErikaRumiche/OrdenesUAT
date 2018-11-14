package pe.com.nextel.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import pe.com.nextel.bean.ProposedBean;
import pe.com.nextel.util.Constante;


public class ProposedDAO extends GenericDAO 
{
   /**
   Method : getProposedList
   Purpose: Obtener la lista de las propuestas asociadas 
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Cesar Barzola    21/07/2009  Creación
  **/
  public HashMap getProposedList(long lCustomerId,long lSite,long lSpecificationId,long lSellerId)throws SQLException, Exception {
    ArrayList list = new ArrayList();
    HashMap objHashMap = new HashMap();
    Connection conn = null;
    OracleCallableStatement cstmt = null;
    String strMsgError = null;
    ResultSet rs = null;
    ProposedBean objProposedBean = null;
    try{
      conn = Proveedor.getConnection();
      String sqlStr =  "BEGIN PROPOSED.SPI_GET_PROPOSED_LST(?, ?, ? , ?, ?, ?, ?); END;";
      cstmt=(OracleCallableStatement)conn.prepareCall(sqlStr);
      if(lCustomerId==0)
        cstmt.setNull(1, OracleTypes.NUMBER);
      else
        cstmt.setLong(1,lCustomerId);
      if(lSite==0)
         cstmt.setNull(2, OracleTypes.NUMBER);
      else
        cstmt.setLong(2,lSite);
      if(lSpecificationId==0)
       cstmt.setNull(3, OracleTypes.NUMBER);
      else
        cstmt.setLong(3,lSpecificationId);
       if(lSellerId==0)
        cstmt.setNull(4, OracleTypes.NUMBER);
      else
        cstmt.setLong(4,lSellerId);      
      java.util.Date date = new java.util.Date();
      java.sql.Date sqlDate = new java.sql.Date(date.getTime());      
      cstmt.setDate(5,sqlDate);
      cstmt.registerOutParameter(6, OracleTypes.CURSOR);
      cstmt.registerOutParameter(7, OracleTypes.VARCHAR);
      
      cstmt.execute();
      strMsgError=cstmt.getString(7);
      if(strMsgError==null){
        rs = (ResultSet)cstmt.getObject(6);
        long index=0;
        while (rs.next()) {
          objProposedBean = new ProposedBean();
          index++;
          objProposedBean.setNpindice(index);
          objProposedBean.setNpproposedid(rs.getLong(1));
          objProposedBean.setNpproposedtype(rs.getString(2));
          list.add(objProposedBean); 
        } 
      }
      objHashMap.put(Constante.MESSAGE_OUTPUT,strMsgError);
      objHashMap.put("objArrayList",list);
    }
    catch(Exception e){
     objHashMap.put(Constante.MESSAGE_OUTPUT,"Error:Problemas con la consulta a PROPOSED.SPI_GET_PROPOSED_LST");
      logger.error(formatException(e));
    }
    finally{
      try{
        closeObjectsDatabase(conn,cstmt,rs);
      }
      catch(Exception e){
        logger.error(formatException(e)); 
      }
    }
    return objHashMap;  
  }
  /**
   Method : getProposedList
   Purpose: Obtener la lista de las propuestas asociadas 
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Cesar Barzola    06/08/2009  Creación
  **/
  public HashMap getValidationProposed(long lOrderId, long lProposedId,long lCustomerId,long lSpecification,long lSellerId,String strTrama)throws SQLException, Exception{
     HashMap objHashMap = new HashMap();
     Connection conn = null;
     OracleCallableStatement cstmt = null;
     String strMsgError = null;
    
    try
    {
      conn = Proveedor.getConnection();
      String sqlStr =  "BEGIN ORDERS.NP_PROPOSED01_PKG.SP_VALIDATE_PROPOSED_ORDER(?, ?, ?, ?, ?, ?, ?); END;";
      cstmt=(OracleCallableStatement)conn.prepareCall(sqlStr);
     // cstmt.setNull(1, OracleTypes.NUMBER); //an_orderid
      if(lOrderId==0)
        cstmt.setLong(1,0);
      else
        cstmt.setLong(1,lOrderId);
        
       if(lCustomerId==0) //an_customerid
        cstmt.setNull(2, OracleTypes.NUMBER);
      else
        cstmt.setLong(2,lCustomerId);
        
      if(lSpecification==0) //an_specificationid
        cstmt.setNull(3, OracleTypes.NUMBER);
      else
        cstmt.setLong(3,lSpecification);
        
      if(lSellerId==0)//an_sellerid
        cstmt.setNull(4, OracleTypes.NUMBER);
      else
        cstmt.setLong(4,lSellerId);         
        
      if(lProposedId==0)//an_proposedid
        cstmt.setNull(5, OracleTypes.NUMBER);
      else
        cstmt.setLong(5,lProposedId);
        
      cstmt.setString(6,strTrama);//av_string
      cstmt.registerOutParameter(7, OracleTypes.VARCHAR);   //av_message   
      cstmt.execute();
      strMsgError=cstmt.getString(7);
        objHashMap.put(Constante.MESSAGE_OUTPUT,strMsgError);
     }
     catch(Exception e)
     {
      logger.error(formatException(e));
     }
     finally
     {
        try
        {
          closeObjectsDatabase(conn,cstmt,null);
        }
        catch(Exception e)
        {
          logger.error(formatException(e)); 
        }
     }
    return objHashMap;
    
   }
   public HashMap getValidationCustomer(long lUserId,long lCustomerId)throws SQLException, Exception{
      HashMap objHashMap = new HashMap();
      Connection conn = null;
      OracleCallableStatement cstmt = null;
      String strMsgError = null;
     try{
      conn = Proveedor.getConnection();
      String sqlStr =  "BEGIN ORDERS.NP_PROPOSED01_PKG.SP_VALIDATE_CONDITIONS_BY_USER(?, ?, ?, ?); END;";
      cstmt=(OracleCallableStatement)conn.prepareCall(sqlStr);
      if(lUserId==0) //an_userid
        cstmt.setNull(1, OracleTypes.NUMBER);
      else
        cstmt.setLong(1,lUserId);
        
      if(lCustomerId==0) //an_customerid
        cstmt.setNull(2, OracleTypes.NUMBER);
      else
        cstmt.setLong(2,lCustomerId);
      
      cstmt.registerOutParameter(3, OracleTypes.NUMBER);
      cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
      cstmt.execute();
       strMsgError=cstmt.getString(4);
      if(strMsgError==null){
        String respuesta=Long.toString(cstmt.getInt(3));
        objHashMap.put("respuesta",respuesta);
      }
      objHashMap.put(Constante.MESSAGE_OUTPUT,strMsgError);
     }
     catch(Exception e){
      logger.error(formatException(e));
     }
     finally{
        try{
          closeObjectsDatabase(conn,cstmt,null);
        }
        catch(Exception e){
          logger.error(formatException(e)); 
        }
     }
     return objHashMap;
     
   }
   public HashMap getValidationSalesManProposed(long lUserId,long lSalesManId)throws SQLException, Exception{
      HashMap objHashMap = new HashMap();
      Connection conn = null;
      OracleCallableStatement cstmt = null;
      String strMsgError = null;
      try{
        conn = Proveedor.getConnection();
        String sqlStr =  "BEGIN ORDERS.NP_PROPOSED01_PKG.SP_VALIDATE_SALESMAN_PROPOSED(?, ?, ?, ?); END;";
        cstmt=(OracleCallableStatement)conn.prepareCall(sqlStr);
        if(lUserId==0) //an_userid
          cstmt.setNull(1, OracleTypes.NUMBER);
        else
          cstmt.setLong(1,lUserId);
        if(lSalesManId==0)
          cstmt.setNull(2, OracleTypes.NUMBER); 
        else
          cstmt.setLong(2,lSalesManId);
        
        cstmt.registerOutParameter(3, OracleTypes.NUMBER);
        cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
        cstmt.execute();
        strMsgError=cstmt.getString(4);
        if(strMsgError==null){
           String respuesta=Long.toString(cstmt.getInt(3));
           objHashMap.put("respuesta",respuesta);  
        }
        objHashMap.put(Constante.MESSAGE_OUTPUT,strMsgError);   
      }
      catch(Exception e){
        logger.error(formatException(e));
      }
     finally{
      try{
        closeObjectsDatabase(conn,cstmt,null);
      }
      catch(Exception e){
        logger.error(formatException(e)); 
      }
     }
     return objHashMap;
   }
  
}