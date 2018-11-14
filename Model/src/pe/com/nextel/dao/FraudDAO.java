package pe.com.nextel.dao;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.HashMap;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;


public class FraudDAO extends GenericDAO 
{
/**
   Method : getVerificationFraudCustomer
   Purpose: Obtener la verificacion si un cliente es fraudulento 
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Cesar Barzola    10/05/2009  Creación
  **/

  public HashMap getVerificationFraudCustomer(String strDocCustomer)throws Exception,SQLException
  {
    HashMap objHashMap = new HashMap();
    Connection conn = null;
    OracleCallableStatement cstmt = null;
    String strMsgError = null;
    
    try
    {
      conn = Proveedor.getConnection();
      String sqlStr =  "BEGIN FRAUD.SPI_VALIDATE_FRAUD_CUSTOMER(?, ?, ? , ?, ?, ?, ?, ?); END;";
      cstmt=(OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setString(1,strDocCustomer);
      cstmt.setString(2,"COMPANY");
      cstmt.setString(3,"");
      cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
      cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
      cstmt.registerOutParameter(6, OracleTypes.VARCHAR);
      cstmt.registerOutParameter(7, OracleTypes.VARCHAR);
      cstmt.registerOutParameter(8, OracleTypes.VARCHAR);
      
      cstmt.execute();
      strMsgError=cstmt.getString(8);
        if(strMsgError==null)
        {
           String codTypeList=cstmt.getString(4);
           System.out.println("El codigo es: "+codTypeList);
           objHashMap.put("CodTypeList",codTypeList);
           objHashMap.put("DescTypeList",cstmt.getString(5));
           objHashMap.put("DescFraud",cstmt.getString(6));
           objHashMap.put("MessageSpeach",cstmt.getString(7));
           if(codTypeList !=null)objHashMap.put("Respuesta","S");
            else objHashMap.put("Respuesta","N"); 
        }
        objHashMap.put("strMessage",strMsgError);
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
/**
   Method : getVerificationFraudOrder
   Purpose: Obtener la verificacion si un cliente es fraudulento cuando se realiza una nueva orden
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Cesar Barzola      10/05/2009  Creación
   Cesar Barzola      15/07/2009  Modificacion
  **/
  public HashMap getVerificationFraudOrder(long intCodClient,int intSpecificationId,String strCategory,String strSubcategory,String strLogin,Connection conn) throws Exception,SQLException
  {
    HashMap objHashMap = new HashMap();
    OracleCallableStatement cstmt = null;
    String strMsgError = null;
    
    try
    {
      String sqlStr =  "BEGIN FRAUD.SPI_VALIDATE_CUSTOMER_ORDERS(?, ?, ? , ?, ?, ?, ?); END;";
      cstmt=(OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setLong(1,intCodClient);
      cstmt.setInt(2,intSpecificationId);
      cstmt.setString(3,strCategory+","+strSubcategory);
      cstmt.setString(4,strLogin);
      cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
      cstmt.registerOutParameter(6, OracleTypes.VARCHAR);
      cstmt.registerOutParameter(7, OracleTypes.VARCHAR);
      
      cstmt.execute();
      strMsgError=cstmt.getString(7);
        if(strMsgError==null)
        {
          objHashMap.put("flagFraud",cstmt.getString(5));
          objHashMap.put("strMessageFraud",cstmt.getString(6));
        }
        objHashMap.put("strMessage",strMsgError);
     }
     catch(Exception e)
     {
      logger.error(formatException(e));
     }
     finally
     {
        try
        {
          closeObjectsDatabase(null,cstmt,null);
        }
        catch(Exception e)
        {
          logger.error(formatException(e));
        }
     }
    return objHashMap;
  }
}