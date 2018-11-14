package pe.com.nextel.dao;

import java.lang.Exception;
import java.lang.Float;
import java.lang.Integer;
import java.lang.Object;
import java.lang.String;
import java.lang.System;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.NUMBER;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

import pe.com.nextel.util.MiUtil;


//import oracle.jdbc.OracleCallableStatement;

public class ExceptionDAO extends GenericDAO{
   /**
    *Method : getCustomerBillCycle
    *
    *Purpose: Obtener el ciclo de facturación de un cliente
    *Developer       Fecha       Comfentario
    *=============   ==========  ======================================================================
    *JPEREZ           24/10/2007  Creación
    *@throws java.sql.SQLException
    *@return HashMap
    *@param piOrderId
    *@param piCustomerId
    */
   public static HashMap getCustomerBillCycle(int piCustomerId, int piOrderId) throws Exception, SQLException{      
      HashMap hashMap = new HashMap();        
      Connection conn = null; 
      OracleCallableStatement cstmt = null;
      String strMensaje = null;
      String  strBillCycle = null;
      String  strBillCycleDsc = null;
      String sqlStr = "BEGIN ORDERS.SPI_GET_CUSTOMER_BYLLCYCLE(?, ?, ?, ?, ?); END;";
      try{
      conn = Proveedor.getConnection();          
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setLong(1, piCustomerId);
      cstmt.setLong(2, piOrderId);
      cstmt.registerOutParameter(3, Types.CHAR);
      cstmt.registerOutParameter(4, Types.CHAR);
      cstmt.registerOutParameter(5, Types.CHAR);     
        
      cstmt.executeUpdate();
        
      strBillCycle    = cstmt.getString(3); 
      strBillCycleDsc = cstmt.getString(4); 
      strMensaje      = cstmt.getString(5); 
       
      hashMap.put("wv_billcycledsc",strBillCycleDsc==null?"":strBillCycleDsc);
      hashMap.put("wv_billcycle",strBillCycle==null?"":strBillCycle);  
      hashMap.put("wv_message",strMensaje);
      }
      catch (Exception e) {
         throw new Exception(e);
      }
      finally{
			closeObjectsDatabase(conn,cstmt,null);			
         /*if (cstmt != null)
            cstmt.close();        
         if (conn != null)
            conn.close();
			*/	
      }                   
      return hashMap;
   }
   /**
    *Method : getAccessFee
    *
    *Purpose: Obtener la renta básica de un arreglo de planes
    *Developer       Fecha       Comentario
    *=============   ==========  ======================================================================
    *JPEREZ           29/10/2007  Creación 
    *
    *@throws java.sql.SQLException
    *@throws java.lang.Exception
    *@return HashMap
    *@param pastrItemPlanIds
    *@param piAppId
    */
   public HashMap getAccessFee(int piAppId, String[] pastrItemPlanIds, ArrayList pastrItemServIds) throws Exception, SQLException {                  
      HashMap hashMap = new HashMap();        
      DecimalFormat decFormat = new DecimalFormat("###"); 
      Connection conn = null; 
      OracleCallableStatement cstmt = null;     
      String strMessage = null;           
      String sqlStr = "BEGIN ORDERS.SPI_GET_ACCESS_FEES(?,?,?) ; END;";
      try{     
         float[] arrAccessFee = new float[pastrItemPlanIds.length];            
         List accessFeeList = new ArrayList();                    
         conn = Proveedor.getConnection();
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
          
         StructDescriptor sdAccessFee     = StructDescriptor.createDescriptor("ORDERS.TO_ACCESS_FEE", conn);        
         ArrayDescriptor  adAccessFeeList = ArrayDescriptor.createDescriptor("ORDERS.TT_ACCESS_FEE_LST", conn);
         for (int i=0; i < pastrItemPlanIds.length; i++){                     
            System.out.println("pastrItemServIds.get(i)==="+(String)pastrItemServIds.get(i));
            Object[] objAccessFee = {Integer.valueOf(pastrItemPlanIds[i]), (String)pastrItemServIds.get(i),null};                      
            STRUCT stcAccessFee = new STRUCT(sdAccessFee, conn, objAccessFee);           
            accessFeeList.add(stcAccessFee);
            System.out.println("===01");
         }                 
         ARRAY aryAccessFeesList = new ARRAY(adAccessFeeList, conn, accessFeeList.toArray());      
         cstmt.setLong(1, piAppId);        
         cstmt.setARRAY(2, aryAccessFeesList);
         //cstmt.setObject(2, (ARRAY)aryAccessFeesList);
         cstmt.registerOutParameter(2, OracleTypes.ARRAY, "ORDERS.TT_ACCESS_FEE_LST");
         cstmt.registerOutParameter(3, Types.CHAR);      
         cstmt.executeUpdate();      
         strMessage = cstmt.getString(3);      
         hashMap.put("strMessage",strMessage);
         if (strMessage!= null){
           System.out.println("strMessage en ExceptionDAO.getAccessFee "+strMessage);
           hashMap.put("afAccessFee", null);
			  try{
				closeObjectsDatabase(conn, cstmt, null);  
				}catch (Exception e) {
					logger.error(formatException(e));
				}
           /*if (cstmt != null)
             cstmt.close();        
           if (conn != null)
             conn.close();
			 */ 
           return hashMap;
         } 
         
         //aryAccessFeesList = cstmt.getARRAY(2);
         aryAccessFeesList = (ARRAY)cstmt.getObject(2);
         
         for (int i=0; i < aryAccessFeesList.getOracleArray().length; i++){           
            STRUCT stcAccessFee = (STRUCT) aryAccessFeesList.getOracleArray()[i];                          
            NUMBER accessFee = ((NUMBER) stcAccessFee.getOracleAttributes()[2]);                           
            if (accessFee!= null)
             arrAccessFee[i] = accessFee.floatValue();
           else
             arrAccessFee[i] = 0;
         }                          
         hashMap.put("afAccessFee", arrAccessFee);     
      }
      catch (Exception e) {
         throw new Exception(e);
      }
      finally{
			closeObjectsDatabase(conn,cstmt,null);
         /*if (cstmt != null)
            cstmt.close();        
         if (conn != null)
            conn.close();*/
      }  
      return hashMap;
   }
   
   /**
    *Method : getRentFee
    *
    *Purpose: Obtener el precio de un arreglo de productos 
    *Developer       Fecha       Comentario
    *=============   ==========  ======================================================================
    *JPEREZ           29/10/2007  Creación 
    *
    *@throws java.sql.SQLException
    *@throws java.lang.Exception
    *@return HashMap
    *@param pastrItemProductIds
    */
   public HashMap getRentFee(String[] pastrItemProductIds, int iSpecId) throws Exception, SQLException {
      System.out.println("ExceptionDAO.getRentFee Inicio");      
      HashMap hashMap = new HashMap();        
      Connection conn = null; 
      OracleCallableStatement cstmt = null;     
      String strMessage = null;           
      String sqlStr = "BEGIN ORDERS.NP_ORDERS03_PKG.SP_GET_RENT_FEES(?,?,?) ; END;";
      try{
         float[] arrRentFee = new float[pastrItemProductIds.length];
         List rentFeeList = new ArrayList();      
         conn  = Proveedor.getConnection();
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
         
         StructDescriptor sdRentFee    = StructDescriptor.createDescriptor("ORDERS.TO_RENT_FEE", conn);        
         ArrayDescriptor  adRentList   = ArrayDescriptor.createDescriptor("ORDERS.TT_RENT_FEE_LST", conn);
         
         for (int i=0; i < pastrItemProductIds.length; i++){
            Object[] objRentFee = {Integer.valueOf(pastrItemProductIds[i]), null};             
            STRUCT stcRentFee = new STRUCT(sdRentFee, conn, objRentFee);      
            rentFeeList.add(stcRentFee);      
         } 
         
         ARRAY aryRentFeesList = new ARRAY(adRentList, conn, rentFeeList.toArray());      
         cstmt.setARRAY(1, aryRentFeesList);
         //cstmt.setObject(1, (ARRAY)aryRentFeesList);
         cstmt.registerOutParameter(1, OracleTypes.ARRAY, "ORDERS.TT_RENT_FEE_LST");
         cstmt.setInt(2, iSpecId);
         cstmt.registerOutParameter(3, Types.CHAR);
         cstmt.executeUpdate();
           
         strMessage = cstmt.getString(3);
         hashMap.put("strMessage",strMessage);
         if (strMessage != null){
           System.out.println("strMessage en ExceptionDAO.getRentFee "+strMessage);
           hashMap.put("afRentFee", null);
				try{
					closeObjectsDatabase(conn, cstmt, null);  
				}catch (Exception e) {
					logger.error(formatException(e));
				}			  
           /*if (cstmt != null)
             cstmt.close();        
           if (conn != null)
             conn.close();*/
           return hashMap;
         }
         //aryRentFeesList = cstmt.getARRAY(1);
         aryRentFeesList = (ARRAY)cstmt.getObject(1);
   
         if ( (aryRentFeesList != null) && (aryRentFeesList.getOracleArray() != null) ){                              
           for (int i=0; i < aryRentFeesList.getOracleArray().length; i++){          
             STRUCT stcRentFee = (STRUCT) aryRentFeesList.getOracleArray()[i];                                       
             NUMBER rentFee    = ((NUMBER) stcRentFee.getOracleAttributes()[1]);                     
             /*if (rentFee == null){
               hashMap.put("afRentFee", arrRentFee); 
               throw new Exception("El producto no tiene registrado un precio de alquiler");
             }*/
             if (rentFee != null)
               arrRentFee[i]     = rentFee.floatValue();
             else
               arrRentFee[i]     = 0;
           }  
         }      
         hashMap.put("afRentFee", arrRentFee);   
      }
      catch (Exception e) {
         throw new Exception(e);
      }
      finally{
			closeObjectsDatabase(conn,cstmt,null);
         /*if (cstmt != null)
            cstmt.close();        
         if (conn != null)
            conn.close();      */
      }
      return hashMap;
   }
   
   /**
    * 
    * Purpose: Obtener la renta básica de un arreglo de planes
    * Developer       Fecha       Comentario
    * =============   ==========  ======================================================================
    * JPEREZ           02/11/2007  Creación 
    * @throws java.sql.SQLException
    * @throws java.lang.Exception
    * @return HashMap
    * @param piServiceId
    * @param piPlanId
    */
   public HashMap getServiceCost(int piPlanId, int piServiceId) throws Exception, SQLException {      
      HashMap hashMap = new HashMap();        
      Float fServiceCost;
      Connection conn = null; 
      OracleCallableStatement cstmt = null;     
      String strMessage = null;           
      String sqlStr = "BEGIN ORDERS.SPI_GET_SERVICE_COST(?,?,?,?) ; END;";
      try{
         conn  = Proveedor.getConnection();
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
         
         cstmt.setLong(1, piPlanId);
         cstmt.setLong(2, piServiceId);
         cstmt.registerOutParameter(3, Types.FLOAT);
         cstmt.registerOutParameter(4, Types.CHAR);       
               
         cstmt.executeUpdate();
         
         fServiceCost = new Float(cstmt.getFloat(3));
         strMessage   = cstmt.getString(4);
                  
         hashMap.put("strMessage",strMessage);
         hashMap.put("fServiceCost", fServiceCost);
      }
      catch (Exception e) {
         throw new Exception(e);
      }
      finally{
			closeObjectsDatabase(conn,cstmt,null);
         /*if (cstmt != null)
            cstmt.close();        
         if (conn != null)
            conn.close();*/
      }      
      return hashMap;            
   }
   
   /**
    * 
    * Purpose: Obtener la renta básica de un arreglo de planes
    * Developer       Fecha       Comentario
    * =============   ==========  ======================================================================
    * JPEREZ           02/11/2007  Creación 
    *     
    * @throws java.sql.SQLException
    * @throws java.lang.Exception
    * @return HashMap
    * @param pstrCreatedBy
    * @param pstrEndPeriod
    * @param pstrBeginPeriod
    * @param plOrderId
    */
   public String insertOrderPeriod(long plOrderId, String pstrBeginPeriod, String pstrEndPeriod, String pstrCreatedBy, Connection conn) throws Exception, SQLException {      
      //Connection conn = null; 
      OracleCallableStatement cstmt = null;     
      String strMessage = null;           
      String sqlStr = "BEGIN ORDERS.NP_ORDERS03_PKG.SP_INSERT_ORDER_PERIOD(?,?,?,?,?) ; END;";
      try{
      //conn  = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      
      cstmt.setLong(1, plOrderId);
      cstmt.setString(2, pstrBeginPeriod);
      cstmt.setString(3, pstrEndPeriod);
      cstmt.setString(4, pstrCreatedBy);
      cstmt.registerOutParameter(5, Types.CHAR);       

      cstmt.executeUpdate();
      strMessage   = cstmt.getString(5);
      }
      catch (Exception e) {
         throw new Exception(e);
      }
      finally{
			closeObjectsDatabase(null,cstmt,null);
      //if (cstmt != null)
      //   cstmt.close();      
      }
      return strMessage;  
   }
   
   public HashMap updateExceptionApprove(int piExceptionid, int piOrderId, String pstrStatus, int piExceptionUnitId, int piExceptionUserId,  String pstrNote, String pstrLogin, long plTimeStamp, Connection conn)
   throws Exception, SQLException {
      HashMap hashMap = new HashMap();                 
      
      OracleCallableStatement cstmt = null;     
      String strMessage = null;     
      String strResultNewInbox = null; // LMM SAR_0037-186400
      String strResultOldInbox = null; // LMM SAR_0037-186400
      int iStatus = -1;
      
      String sqlStr = "BEGIN ORDERS.NP_EXCEPTION_APPROVE_PKG.SP_UPDATE_ORDER_APP_DET_BPEL(?,?,?,?,?,?,?,?,?,?,?,?) ; END;";
      try{     
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
         
         cstmt.setLong(1, piExceptionid);
         cstmt.setLong(2, piOrderId);
         cstmt.setString(3, pstrStatus);
         cstmt.setLong(4, piExceptionUnitId);
         cstmt.setLong(5, piExceptionUserId);
         cstmt.setString(6, pstrNote);
         cstmt.setString(7, pstrLogin);
         cstmt.setLong(8,plTimeStamp);
         cstmt.registerOutParameter(9, Types.CHAR); // LMM SAR_0037-186400
         cstmt.registerOutParameter(10, Types.CHAR); // LMM SAR_0037-186400
         cstmt.registerOutParameter(11, Types.INTEGER); // LMM SAR_0037-186400
         cstmt.registerOutParameter(12, Types.CHAR); // LMM SAR_0037-186400
         
         cstmt.executeUpdate();
         
         strResultNewInbox = cstmt.getString(9); // LMM SAR_0037-186400
         strResultOldInbox = cstmt.getString(10); // LMM SAR_0037-186400
         iStatus    = cstmt.getInt(11); // LMM SAR_0037-184600
         strMessage = cstmt.getString(12); // LMM SAR_0037-184600
         
         hashMap.put("strNextInbox",strResultNewInbox); // LMM SAR_0037-186400
         hashMap.put("strOldInbox",strResultOldInbox); // LMM SAR_0037-186400
         hashMap.put("strMessage",strMessage);
         hashMap.put("status", MiUtil.getString(""+iStatus) );
      }
      catch (Exception e) {
         throw new Exception(e);
      }
      finally{
			closeObjectsDatabase(null,cstmt,null);
         /*if (cstmt != null)
            cstmt.close();*/        
      }
      return hashMap;      
   }
   
   public String deleteOrderPeriod(long plOrderId, Connection conn) throws Exception, SQLException {      
      //Connection conn = null; 
      OracleCallableStatement cstmt = null;     
      String strMessage = null;           
      String sqlStr = "BEGIN ORDERS.NP_ORDERS03_PKG.SP_DELETE_ORDER_PERIOD(?,?) ; END;";
      try{
         //conn  = Proveedor.getConnection();
         cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
         
         cstmt.setLong(1, plOrderId);      
         cstmt.registerOutParameter(2, Types.CHAR);       
   
         cstmt.executeUpdate();
         strMessage   = cstmt.getString(2);
      }
      catch (Exception e) {
         throw new Exception(e);
      }
      finally{
			closeObjectsDatabase(null,cstmt,null);
         /*if (cstmt != null)
            cstmt.close();*/        
      }
      return strMessage;     
   }    
}