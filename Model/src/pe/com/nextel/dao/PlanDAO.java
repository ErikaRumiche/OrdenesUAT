package pe.com.nextel.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.ArrayList;
import java.util.HashMap;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import pe.com.nextel.bean.PlanBean;
import pe.com.nextel.bean.PlanTarifarioBean;
import pe.com.nextel.util.MiUtil;
import pe.com.nextel.util.RequestHashMap;


//import pe.com.nextel.util.DbmsOutput;
//import oracle.jdbc.OracleCallableStatement;


public class PlanDAO extends GenericDAO{

  /**
   Method : getPlanList
   Purpose: Obtener la lista de planes tarifarios
   Developer        Fecha       Comentario
   =============    ==========  ======================================================================
   Lee Rosales      22/09/2007  Creación
   Enrique Zubiaurr 14/11/2010  Se modifica el procedure llamado para compatibilidad M-P-S
   Enrique Zubiaurr 14/03/2011  Se agrega un parámetro al procedure para que se envíe el Product Line Id
   */
  public HashMap getPlanList(PlanTarifarioBean planTarifarioBean, String type) throws SQLException, Exception {

     ArrayList list = new ArrayList();
     HashMap objHashMap = new HashMap();
     PlanTarifarioBean pTarifarioBean  = null;
     Connection conn = null; 
     OracleCallableStatement cstmt = null;
     ResultSet rs = null;
	   //DbmsOutput dbmsOutput = null;
	  String strMessage = null;
     conn = Proveedor.getConnection();
     String sqlStr = "BEGIN ORDERS.NP_ORDERS01_PKG.SP_GET_PLANS_LIST_COMP(?, ?, ?, ?, ?, ?, ?, ?); END;";//EZUBIAURR 14/03/11
     try{        
        //dbmsOutput = new DbmsOutput(conn);
        //dbmsOutput.enable(1000000);	 
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
       
        cstmt.setString(1,"0");
        cstmt.setString(2,type);
        cstmt.setLong(3,planTarifarioBean.getNpsolutionid());
        cstmt.setInt(4,planTarifarioBean.getNpspecificationid());
        cstmt.setLong(5,planTarifarioBean.getNprequestapprove());
        cstmt.setLong(6,planTarifarioBean.getNpprodlineval());//EZUBIAURR 14/03/11
        cstmt.registerOutParameter(7, OracleTypes.CURSOR );
        cstmt.registerOutParameter(8, Types.VARCHAR);
        
        System.out.println("En PlanDAO getProductPlanList---------->"+planTarifarioBean.getNprequestapprove());
     
      
      System.out.println("getNpsubtype---------->"+type);
      
      System.out.println("getNpsolutionid---------->"+planTarifarioBean.getNpsolutionid());
      
      System.out.println("getNpcategoryid---------->"+planTarifarioBean.getNpspecificationid());
      
      System.out.println("getNpproductid---------->"+planTarifarioBean.getNprequestapprove());
       
        cstmt.executeUpdate();
        //dbmsOutput.show();
		    //dbmsOutput.close();
        strMessage = cstmt.getString(8);
        
         System.out.println("En ProductDAO strMessage---------->"+strMessage);
       
        if( strMessage == null ){
           rs = (ResultSet)cstmt.getCursor(7);        
           while (rs.next()) {
              pTarifarioBean = new PlanTarifarioBean(); 
              pTarifarioBean.setNpdescripcion(rs.getString("des"));
              pTarifarioBean.setNpplancode(rs.getDouble("tmcode"));
              pTarifarioBean.setNptipo2(rs.getString("nivelplan"));
              list.add(pTarifarioBean);
           }
        }            
        objHashMap.put("strMessage",strMessage);
        objHashMap.put("objPlanList",list);
     }
     catch(Exception e){
        throw new Exception(e);
     }
     finally{
        closeObjectsDatabase(conn,cstmt,rs);
     }
     return objHashMap;
  }

    
  /**
   Method : getPlanValueList
   Purpose: Obtener los valores de PLANES
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Israel Rondón   01/08/2007  Creación
  **/
   
  public static ArrayList getPlanValueList(int iProductId) throws Exception,SQLException{
    
     ArrayList list= new ArrayList();
     PlanBean tb = null;
     Connection conn = null; 
     ResultSet rs=null;
     OracleCallableStatement cstm = null;
     String strMensaje = null;                 
     String strSql = "BEGIN WEBSALES.NPSL_GENERAL_PKG.SP_GET_PLANITEM_LIST(?,?,?); END;";
     try{
        conn = Proveedor.getConnection();
        cstm = (OracleCallableStatement) conn.prepareCall(strSql);
               
        cstm.setInt(1,iProductId);
        cstm.registerOutParameter(2, Types.CHAR);
        cstm.registerOutParameter(3, OracleTypes.CURSOR);
      
        cstm.execute();
        strMensaje  = cstm.getString(2);
      
        if( strMensaje == null ){     
           rs = (ResultSet)cstm.getObject(3);        
           int npPlanId;     
           while (rs.next()) {
              tb = new PlanBean();
              npPlanId = Integer.parseInt(rs.getString("npPlanId"));
              tb.setNpPlanId(npPlanId);
              tb.setNpPlanDesc(rs.getString("npPlanDesc")==null?"":rs.getString("npPlanDesc"));   
              list.add(tb);
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
  
   /**
   Method : getOriginalPlan
   Purpose: Obtener el plan tarifario original
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   PHIDALGO     15/04/2010  Creación
   */
  public HashMap getOriginalPlan(PlanTarifarioBean planTarifarioBean) throws SQLException, Exception {

     HashMap objHashMap = new HashMap();
     Connection conn = null; 
     OracleCallableStatement cstmt = null;
     ResultSet rs = null;
	   //DbmsOutput dbmsOutput = null;
	   String strMessage = null;
     conn = Proveedor.getConnection();
     String sqlStr = "BEGIN ORDERS.NP_ORDERS01_PKG.SP_GET_ORIGINAL_PLAN(?,?,?); END;";
     try{        
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);

        cstmt.setDouble(1,planTarifarioBean.getNpplantarifarioid());
        cstmt.registerOutParameter(2, Types.VARCHAR);
        cstmt.registerOutParameter(3, OracleTypes.CURSOR);
       
        cstmt.executeUpdate();
        strMessage = cstmt.getString(2); 
       
        if( strMessage == null ){
           rs = (ResultSet)cstmt.getCursor(3);        
           while (rs.next()) { 
              planTarifarioBean.setNpdescripcion(rs.getString("des"));
           }
        }            
        objHashMap.put("strMessage",strMessage);
        objHashMap.put("objPlanTarifarioBean",planTarifarioBean);
     }
     catch(Exception e){
        throw new Exception(e);
     }
     finally{
        closeObjectsDatabase(conn,cstmt,rs);
     }
     return objHashMap;
  }
  
  /**
     Method : getServiceDefaultListByPlan
     Purpose: Obtiene el tipo de plan
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     Frank Picoy     05/12/2011  Creación
   */
    public String getTypePlan(long lPlanId) throws SQLException, Exception{       
       HashMap objHashMap = new HashMap();
       String strTypePlan = "";
       ArrayList list = null;
       Connection conn = null; 
       ResultSet rs=null;
       OracleCallableStatement cstmt = null;
       String strMessage  = "";
       try{
        String sqlStr = "BEGIN ORDERS.SPI_3GP_ALO_TYPE_RATEPLAN(?, ?, ?, ?, ?); END;";
      
        conn = Proveedor.getConnection();
        
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
        cstmt.setNull(1,OracleTypes.NUMBER);                 
        cstmt.setLong(2, lPlanId);                 
        cstmt.registerOutParameter(3, Types.CHAR);
        cstmt.registerOutParameter(4, Types.CHAR);
        cstmt.registerOutParameter(5, Types.CHAR);
        cstmt.execute();     
        strTypePlan = cstmt.getString(3);        
      }catch(Exception e){
        logger.error(formatException(e));
        strMessage = e.getMessage(); 
      }finally{
        try{
          closeObjectsDatabase(conn,cstmt,null); 
        }catch (Exception e) {
          logger.error(formatException(e));
        }
     }
     return strTypePlan;                                                                
   }
  
  /**
    Method : doValidateChangePlanToEmployee
    Purpose: Validar si el plan al que desea cambiarse un empleado es Comercial o no.
    Developer       Fecha       Comentario
    =============   ==========  ======================================================================
    Frank Picoy     20/11/2013  Creación
  */
  public HashMap doValidateChangePlanToEmployee(RequestHashMap objHashMap) throws SQLException, Exception{       
    HashMap objResultHashMap = new HashMap();
    Connection conn = null; 
    OracleCallableStatement cstmt = null;
    String strMessage  = "";
    long lresult=0;
    try {
      String strPhone    = (String)objHashMap.getParameter("strPhone");
      long lPlanId = MiUtil.parseLong(((String)objHashMap.get("strPlanId")));                           
      String sqlStr = "BEGIN ORDERS.SPI_VALID_CHANGE_PLAN_TO_PHONE (?, ?, ?, ?); END;";
            
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
      cstmt.setString(1,strPhone);                 
      cstmt.setLong(2, lPlanId);                 
      cstmt.registerOutParameter(3, Types.NUMERIC);
      cstmt.registerOutParameter(4, Types.VARCHAR);
      cstmt.executeUpdate();
      strMessage = cstmt.getString(4); 
                
      if( strMessage == null ){
         lresult = cstmt.getLong(3);        
      }            
      objResultHashMap.put("strMessage",strMessage);
      objResultHashMap.put("lresult",lresult);        
    } catch(Exception e){
      logger.error(formatException(e));
      strMessage = e.getMessage(); 
    } finally{
        try{
          closeObjectsDatabase(conn,cstmt,null); 
        } catch (Exception e) {
          logger.error(formatException(e));
        }
    }
    return objResultHashMap;                                                                
  }
}