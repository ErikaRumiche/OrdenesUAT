package pe.com.nextel.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

import pe.com.nextel.bean.OrderBean;


public class AgreementDAO extends GenericDAO {

  public HashMap validateAgreement(ArrayList objArray, OrderBean objOrder, Connection conn) throws Exception,SQLException{
      
    ResultSet rs=null;
    OracleCallableStatement cstmt = null;
    String strMessage = null;
    HashMap   objHashMapResultado = new HashMap();   
    ArrayList arrItemAgreement    = new ArrayList();
    String strSql = "BEGIN ORDERS.NP_AGREEMENT_PKG.SP_VALIDATE_AGREEMENT(?,?,?,?,?); END;";
    try{
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement) conn.prepareCall(strSql);
      StructDescriptor sdItemAgreement = StructDescriptor.createDescriptor("ORDERS.TO_ITEM_AGREEMENT", conn);
      ArrayDescriptor  adItemAgreement = ArrayDescriptor.createDescriptor("ORDERS.TT_ITEM_AGREEMENT_LST", conn);
       
      for(int i=0; i<objArray.size(); i++){ 
        HashMap objHashMap = (HashMap)objArray.get(i);
        Object[] objItemAgreement = {	objHashMap.get("item_type_id"), objHashMap.get("item_type_count")};
        STRUCT stcItemAgreement = new STRUCT(sdItemAgreement, conn, objItemAgreement);
        arrItemAgreement.add(stcItemAgreement); 
      }
        
      ARRAY aryItemAgreement = new ARRAY(adItemAgreement, conn, arrItemAgreement.toArray());
        
      cstmt.setARRAY(1, aryItemAgreement);
      cstmt.setLong(2,objOrder.getNpCustomerId());
      cstmt.setLong(3,objOrder.getNpSiteId());
      cstmt.setLong(4,objOrder.getNpOrderId());
      cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
      
      cstmt.executeUpdate();
        
      strMessage  = cstmt.getString(5);
        
      objHashMapResultado.put("strMessage",strMessage);
    }
    catch(Exception e){
      logger.error(formatException(e));              
      objHashMapResultado.put("strMessage", e.getMessage());
   }finally{
      try{
        closeObjectsDatabase(conn,cstmt,rs); 
      }catch (Exception e) {
        logger.error(formatException(e));
      }
    } 
    return objHashMapResultado;                                                                
  }
}