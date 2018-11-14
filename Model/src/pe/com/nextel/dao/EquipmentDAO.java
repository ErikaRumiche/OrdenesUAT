package pe.com.nextel.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.ArrayList;
import java.util.HashMap;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import pe.com.nextel.bean.EquipmentBean;
import pe.com.nextel.util.MiUtil;


public class EquipmentDAO extends GenericDAO{

 /**
  Method : getProductList
  Purpose: Obtener los valores de LINEA PRODUCTOS
  Developer       Fecha       Comentario
  =============   ==========  ======================================================================
  Lee Rosales     22/09/2007  Creación
 */
 
  public HashMap doValidateMassiveSim(long lngCustomerId, 
                                      long lngSpecificationId, 
                                      String strModality,
                                      String[] strSim
                                    ) throws SQLException, Exception{

   String[] strMessages = null;
   String strMessage = null;
   Connection conn = null; 
   ResultSet rs=null;
   OracleCallableStatement cstm = null;
   HashMap objResult = new HashMap();
   String strSql = "BEGIN ORDERS.NP_ORDERS08_PKG.SP_VALIDATE_MASSIVE_SIM(?,?,?,?,?,?); END;";

   try{
      conn = Proveedor.getConnection();
      cstm = (OracleCallableStatement) conn.prepareCall(strSql);
      //String arrayElements[] = { "Test3", "Test4" };
      ArrayDescriptor desc = ArrayDescriptor.createDescriptor("TT_STRING_LST", conn);
      ARRAY arrSim = new ARRAY(desc, conn, strSim);
      //strSim
      cstm.setLong(1,lngCustomerId);
      cstm.setLong(2,lngSpecificationId);
      cstm.setString(3,strModality);
      cstm.setARRAY(4,arrSim);
      cstm.registerOutParameter(5, OracleTypes.ARRAY, "TT_STRING_LST");
      cstm.registerOutParameter(6, OracleTypes.VARCHAR);
      
      cstm.execute();
      
      strMessage  = (String)cstm.getString(6);
      
      System.out.println("Mensaje de Error  : " + strMessage);
      
      if( strMessage == null){
      
        arrSim = cstm.getARRAY(5);
        
        if( arrSim != null && arrSim.length() > 0 )
          strMessages = new String[arrSim.length()];
        
        for (int i = 0; i < arrSim.length(); i++) {
          String strSimValidated = null;
          strSimValidated = arrSim.getOracleArray()[i]==null?"SIM es válido":arrSim.getOracleArray()[i].toString();
          logger.debug("Mostrando el indice "+i+": "+strSimValidated);
          strMessages[i] = strSimValidated;
        }
      
      }
      
      objResult.put("strMessages",strMessages);
      objResult.put("strMessage",strMessage);
      
   }finally{
      closeObjectsDatabase(conn,cstm,rs);
   }
   
   return objResult;                                                                
  }
  
  public static void main(String args[]) {
    try{
      System.out.println("Hola");
      String[] valores = {"Hola","Lee","Rosales"};
      (new EquipmentDAO()).doValidateMassiveSim(MiUtil.parseLong("123"),MiUtil.parseLong("2002"),"Propio",valores);
    
    }catch(Exception ex){
      ex.printStackTrace();
    }
  }
 
  
 /**
  Method : getProductList
  Purpose: Obtener los valores de LINEA PRODUCTOS
  Developer       Fecha       Comentario
  =============   ==========  ======================================================================
  Lee Rosales     22/09/2007  Creación
 */
 
  public static ArrayList getProductList(String ownhandset, String consignmen, String strMessage) throws SQLException, Exception{
    
   ArrayList list = new ArrayList();
   EquipmentBean eBean = null;
   Connection conn = null; 
   ResultSet rs=null;
   OracleCallableStatement cstm = null;
      
   String strSql = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_GET_NPOWNHANDSET_LIST(?,?,?,?); END;";
   try{
      conn = Proveedor.getConnection();
      cstm = (OracleCallableStatement) conn.prepareCall(strSql);
               
      cstm.setString(1,ownhandset);
      cstm.setString(2,consignmen);
      
      cstm.registerOutParameter(4, Types.VARCHAR);
      cstm.registerOutParameter(3, OracleTypes.CURSOR);
      
      cstm.execute();
      
      strMessage  = cstm.getString(4);
      
      if( strMessage == null ){
       rs = (ResultSet)cstm.getObject(3);
       while (rs.next()) {
         eBean = new EquipmentBean();    
         eBean.setNpequipmentid(rs.getString("npvalue"));  
         eBean.setNpequipmentdesc(rs.getString("npvaluedesc"));
         list.add(eBean);
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