package pe.com.nextel.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.ArrayList;
import java.util.HashMap;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import oracle.sql.ARRAY;
import oracle.sql.STRUCT;

import pe.com.nextel.bean.NpTableBean;
import pe.com.nextel.bean.ServiciosBean;
import pe.com.nextel.util.MiUtil;


//import oracle.jdbc.OracleCallableStatement;

public class ServiceDAO extends GenericDAO{

    /**
     Method : getServiceRentList
     Purpose: Obtiene los servicios de un determinado plan
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     Lee Rosales     22/01/2008  Creación
     */
     
    public HashMap getServiceRentList(long lngPlanId) throws Exception,SQLException{
       
       ArrayList list = null;
       Connection conn = null;  
       ResultSet rs=null;
       OracleCallableStatement cstm = null;
       ServiciosBean  serviciosBean = null;
       String strMessage = null;       
       String strSql = "BEGIN ORDERS.NP_ORDERS06_PKG.SPI_GET_PLAN_SRV_RENT_LST(?,?,?); END;";
       try{
          conn = Proveedor.getConnection();
          cstm = (OracleCallableStatement) conn.prepareCall(strSql);       
          cstm.setLong(1,lngPlanId);
          cstm.registerOutParameter(2, OracleTypes.CURSOR);
          cstm.registerOutParameter(3, OracleTypes.VARCHAR);       
          cstm.executeUpdate();       
          strMessage  = cstm.getString(3);       
          if( strMessage == null ){       
             rs = (ResultSet)cstm.getObject(2);
             list = new ArrayList();          
             while (rs.next()) {
                serviciosBean = new ServiciosBean();            
                serviciosBean.setNpservicioid(rs.getLong("npservicioid"));
                serviciosBean.setNpnomserv(rs.getString("npservicioname"));
                serviciosBean.setNpsncode(rs.getLong("sncode"));
                serviciosBean.setNpexcludingind(rs.getString("npexcludingind"));
                list.add(serviciosBean);
             }        
         }          
      }
      catch (Exception e) {
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn,cstm,rs);        
      }
      HashMap objHashMap = new HashMap();
      objHashMap.put("strMessage",strMessage);
      objHashMap.put("objServiciosBean",list);              
      return objHashMap;
   }

     /**
     Method : getServiceDefaultList
     Purpose: Obtiene los servicios adicionales por defecto de acuerdo a la 
              especificacionId ingresado
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     Lee Rosales     22/09/2007  Creación
     */
     
    public ArrayList getServiceDefaultList(String strObjectType, int intSpecificationId, String strMessage) throws SQLException, Exception{       
       ArrayList list = null;
       Connection conn = null; 
       ResultSet rs=null;
       OracleCallableStatement cstm = null;
       ServiciosBean  serviciosBean = null;              
       String strSql = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_GET_SERVICE_DEFAULT_LIST(?,?,?,?); END;";
       try{
          conn = Proveedor.getConnection();
          cstm = (OracleCallableStatement) conn.prepareCall(strSql);       
          cstm.setString(1,strObjectType);       
          cstm.setInt(2,intSpecificationId);
          cstm.registerOutParameter(3, OracleTypes.CURSOR);
          cstm.registerOutParameter(4, Types.CHAR);       
          cstm.execute();
          strMessage  = cstm.getString(4);       
          if( strMessage == null ){                
            rs = (ResultSet)cstm.getObject(3);
            list = new ArrayList();       
             while (rs.next()) {
                 serviciosBean = new ServiciosBean();
                 serviciosBean.setNpservicioid(rs.getLong("npservicioid"));
                 serviciosBean.setNpnomserv(rs.getString("npnomserv"));
                 serviciosBean.setNpnomcorserv(rs.getString("npnomcorserv"));
                 serviciosBean.setNpexcludingind(rs.getString("npexcludingind"));
                 list.add(serviciosBean);
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
     Method : getServiceAllList
     Purpose: Obtiene todos los servicios adicionales de acuerdo a la solución 
              de negocio entregada
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     Lee Rosales     22/09/2007  Creación
     */
     
    public static ArrayList getServiceAllList(int intSolutionId, String strMessage) throws SQLException, Exception{
      
       ArrayList list = null;
       Connection conn = null; 
       ResultSet rs=null;
       OracleCallableStatement cstm = null;
       ServiciosBean  serviciosBean = null;              
       String strSql = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_GET_SERVICE_ALL_LIST(?,?,?); END;";
       try{
          conn = Proveedor.getConnection();
          cstm = (OracleCallableStatement) conn.prepareCall(strSql);
          cstm.setInt(1,intSolutionId);
          cstm.registerOutParameter(2, OracleTypes.CURSOR);
          cstm.registerOutParameter(3, Types.VARCHAR);
          cstm.execute();       
          strMessage  = cstm.getString(3);       
          if( strMessage == null ){        
             rs = (ResultSet)cstm.getObject(2);
             list = new ArrayList();        
             while (rs.next()) {
                serviciosBean = new ServiciosBean();
                serviciosBean.setNpservicioid(rs.getLong("npservicioid"));
                serviciosBean.setNpnomserv(rs.getString("npnomserv"));
                serviciosBean.setNpnomcorserv(rs.getString("npnomcorserv"));
                serviciosBean.setNpexcludingind(rs.getString("npexcludingind"));
                list.add(serviciosBean);
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
   
   public static ArrayList getServiceItemList(long intItemid) throws SQLException, Exception{
      
       ArrayList list = null;
       Connection conn = null; 
       String     strMessage = null;
       ResultSet rs=null;
       OracleCallableStatement cstm = null;
       ServiciosBean  serviciosBean = null;       
       String strSql = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_GET_SERVICE_ITEM(?,?,?); END;";
       try{
          conn = Proveedor.getConnection();
          cstm = (OracleCallableStatement) conn.prepareCall(strSql);                
          cstm.setLong(1,intItemid);
          cstm.registerOutParameter(2, OracleTypes.CURSOR);
          cstm.registerOutParameter(3, Types.VARCHAR);
          cstm.execute();       
          strMessage  = cstm.getString(3);       
          if( strMessage == null ){        
             rs = (ResultSet)cstm.getObject(2);
             list = new ArrayList();
             while (rs.next()) {
                serviciosBean = new ServiciosBean();
                serviciosBean.setNpservicioid(rs.getLong("npserviceid"));
                list.add(serviciosBean);
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
     Method : getServiceAllList
     Purpose: Obtener los valores de LINEA PRODUCTOS
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     Lee Rosales     22/09/2007  Creación
     */
     
    public static ServiciosBean getServiceDescription(int intServicioId, String strMessage) throws SQLException, Exception{
      
       Connection conn = null; 
       ResultSet rs=null;
       OracleCallableStatement cstm = null;
       ServiciosBean  serviciosBean = null;       
       String strSql = "BEGIN ORDERS.NP_ORDERS06_PKG.SP_GET_SERVICE_DESCRIPTION(?,?,?); END;";
       try{
          conn = Proveedor.getConnection();
          cstm = (OracleCallableStatement) conn.prepareCall(strSql);                     
          cstm.setInt(1,intServicioId);
          cstm.registerOutParameter(2, OracleTypes.CURSOR);
          cstm.registerOutParameter(3, Types.VARCHAR);
          cstm.execute();          
          strMessage  = cstm.getString(3);          
          if( strMessage == null){         
             rs = (ResultSet)cstm.getObject(2);
             if (rs.next()) {
                serviciosBean = new ServiciosBean();
                serviciosBean.setNpservicioid(rs.getLong("npservicioid"));
                serviciosBean.setNpnomserv(rs.getString("npnomserv"));
                serviciosBean.setNpnomcorserv(rs.getString("npnomcorserv"));
                serviciosBean.setNpexcludingind(rs.getString("npexcludingind"));
             }
          }
       }
       catch (Exception e) {
         throw new Exception(e);
       }
       finally{
         closeObjectsDatabase(conn,cstm,rs);
       }
       return serviciosBean;                                                                
   }
   
   
     /**
     Method : getServiceAllList
     Purpose: Obtener los valores de los servicios por defecto en base a un producto seleccionado
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     Karen Salvador     01/07/2009  Creación
     */
     
    public static HashMap getProductServiceDefaultList(long lspecificationid,int iProductId,int iplanId,int iPermission_alq, int iPermission_msj) throws SQLException, Exception{
  
       Connection conn = null; 
       ResultSet rs=null;
       OracleCallableStatement cstm = null;
       ServiciosBean  serviciosBean = null;  
       HashMap hshResult=new HashMap();
       ArrayList list = null;
       String strMessage = null;
       String strSql = "BEGIN PRODUCT.SPI_GET_SERVICE_DEFAULT_LIST(?,?,?,?,?,?,?); END;";
       try{
          System.out.println("SPI_GET_SERVICE_DEFAULT_LIST");
          System.out.println("lspecificationid:"+lspecificationid);
          System.out.println("iProductId:"+iProductId);
          System.out.println("iplanId:"+iplanId);
          System.out.println("iPermission_alq:"+iPermission_alq);
          System.out.println("iPermission_msj:"+iPermission_msj);
          conn = Proveedor.getConnection();
          cstm = (OracleCallableStatement) conn.prepareCall(strSql);
          cstm.setLong(1,lspecificationid);
          cstm.setInt(2,iProductId);
          cstm.setInt(3,iplanId);
          cstm.setInt(4,iPermission_alq);
          cstm.setInt(5,iPermission_msj);
          cstm.registerOutParameter(6, OracleTypes.CURSOR);
          cstm.registerOutParameter(7, Types.VARCHAR);
          cstm.execute();          
          strMessage  = cstm.getString(7);   
          System.out.println("strMessage:"+strMessage);
          if( strMessage == null){   
             rs = (ResultSet)cstm.getCursor(6); 
             list = new ArrayList();  
             while(rs.next()) {
                serviciosBean = new ServiciosBean();
                System.out.println(rs.getLong("npservicioid"));
                serviciosBean.setNpservicioid(rs.getLong("npservicioid"));
                System.out.println(rs.getString("npnomserv"));
                serviciosBean.setNpnomserv(rs.getString("npnomserv"));
                System.out.println(rs.getString("npnomcorserv"));
                serviciosBean.setNpnomcorserv(rs.getString("npnomcorserv"));
                System.out.println(rs.getString("npexcludingind"));
                serviciosBean.setNpexcludingind(rs.getString("npexcludingind"));
                list.add(serviciosBean);  
                
             }
          }
       }
       catch (Exception e) {
         throw new Exception(e);
       }
       finally{
         closeObjectsDatabase(conn,cstm,rs);
       }
     hshResult.put("objServiceDefaultList",list);
     hshResult.put("strMessage",strMessage);     
     System.out.println("termina");
     return hshResult;                                                              
   }
   
   
     /**
     Method : getServiceRentList
     Purpose: Obtiene el detalle de un servicio
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     Karen Salvador  09/07/2009  Creación
     */
    public static ServiciosBean getDetailService(long lngServiceId) throws Exception,SQLException{
       
       ArrayList list = null;
       Connection conn = null;  
       ResultSet rs=null;
       OracleCallableStatement cstm = null;
       ServiciosBean  serviciosBean = null;
       String strMessage = null;       
       String strSql = "BEGIN swbapps.SPI_GET_DETAIL_SERVICIO(?,?,?); END;";
       try{
          conn = Proveedor.getConnection();
          cstm = (OracleCallableStatement) conn.prepareCall(strSql);       
          cstm.setLong(1,lngServiceId);
          cstm.registerOutParameter(2, OracleTypes.CURSOR);
          cstm.registerOutParameter(3, OracleTypes.VARCHAR);       
          cstm.executeUpdate();       
          strMessage  = cstm.getString(3);       
          if( strMessage == null ){       
             rs = (ResultSet)cstm.getObject(2); 
             while (rs.next()) {
                serviciosBean = new ServiciosBean();            
                serviciosBean.setNpservicioid(rs.getLong("npservicioid"));
                serviciosBean.setNpnomserv(rs.getString("npnomserv"));
                serviciosBean.setNpnomcorserv(rs.getString("npnomcorserv"));
                serviciosBean.setNpexcludingind(rs.getString("npexcludingind"));
               
             }        
         }          
      }
      catch (Exception e) {
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn,cstm,rs);        
      }
      return serviciosBean; 
   }
   
   
    /**
     Method : getCoreService_by_Plan
     Purpose: Obtiene los servicios core de un determinado plan
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     CBARZOLA     05/07/2009  Creación
     */
     public HashMap getCoreService_by_Plan(long lngPlanId)throws Exception,SQLException{
      ArrayList list = null;
       Connection conn = null;  
       ResultSet rs=null;
       OracleCallableStatement cstm = null;
       ServiciosBean  serviciosBean = null;
       String strMessage = null; 
       int iNumReg=0;
       String strSql = "BEGIN ORDERS.NP_ORDERS25_PKG.SP_GET_CORE_SERVICES_BY_PLAN(?,?,?,?); END;";
       try{
          conn = Proveedor.getConnection();
          cstm = (OracleCallableStatement) conn.prepareCall(strSql);       
          cstm.setLong(1,lngPlanId);
          cstm.registerOutParameter(2, OracleTypes.CURSOR);
          cstm.registerOutParameter(3, OracleTypes.NUMBER);  
          cstm.registerOutParameter(4, OracleTypes.VARCHAR);       
          cstm.execute();       
          strMessage  = cstm.getString(4);       
          if( strMessage == null ){   
           iNumReg  = (int)cstm.getInt(3);
           list = new ArrayList();
            if(iNumReg>0)
            { rs = (ResultSet)cstm.getObject(2);          
             while (rs.next()) {
                serviciosBean = new ServiciosBean();
                serviciosBean.setNpservicioid(rs.getLong("npservicioid"));
                serviciosBean.setNpnomserv(rs.getString("npnomserv"));
                serviciosBean.setNpnomcorserv(rs.getString("npnomcorserv"));
                serviciosBean.setNpexcludingind(rs.getString("npexcludingind"));
                list.add(serviciosBean);
             } 
            }
         }          
      }
      catch (Exception e) {
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn,cstm,rs);        
      }
      HashMap objHashMap = new HashMap();
      objHashMap.put("strMessage",strMessage);
      objHashMap.put("objServiciosBean",list);              
      return objHashMap;
     }  
   //johncmb inicio
      /**
     Method : getServiceGroupLst
     Purpose: Obtener los valores de GRUPO DE SERVICIOS
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     John Salazar     20/10/2010  Creación
     */
     
    public static HashMap getServiceGroupLst() throws SQLException, Exception{
      //SP_SERVICE_GROUP_LST
       Connection conn = null; 
       ResultSet rs=null;
       ArrayList list = null;
       String strMessage=null;
       System.out.println("[ServiceDAO][getServiceGroupLst]");
       OracleCallableStatement cstm = null;
       ServiciosBean  serviciosBean = null;   
       NpTableBean nptableBean = null;
       String strSql = "BEGIN SWBAPPS.SPI_SERVICE_GROUP_LST(?,?); END;";

       try{
          conn = Proveedor.getConnection();
          cstm = (OracleCallableStatement) conn.prepareCall(strSql);                     
          cstm.registerOutParameter(1, OracleTypes.CURSOR);
          cstm.registerOutParameter(2, Types.VARCHAR);
          cstm.execute();          
        
          strMessage  = cstm.getString(2);          
          if( strMessage == null){         
             rs = (ResultSet)cstm.getObject(1);
              list = new ArrayList(); 
              while(rs.next()) {
                nptableBean = new NpTableBean();
                nptableBean.setNpvalue(rs.getString("npvalue"));
                nptableBean.setNpvaluedesc(rs.getString("npvaluedesc"));
                nptableBean.setNptag1(rs.getString("nptag1"));
                 list.add(nptableBean);  
             }
             System.out.println(" ServiceDAO list SIZE:---->"+list.size());
             
         }
          
          
          
       }
       catch (Exception e) {
         throw new Exception(e);
       }
       finally{
         closeObjectsDatabase(conn,cstm,rs);
       }
      HashMap hshResult = new HashMap();
      hshResult.put("objServiceGroupList",list);
      hshResult.put("strMessage",strMessage);     
      System.out.println("termina");
      return hshResult;             
   }
  //johncmb fin
 
   
   /**
     Method : getValidateServListByPlan
     Purpose: Obtiene los servicios que no son compatibles con el Plan seleccionado
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     Frank Picoy     09/10/2010  Creación
     */
     public HashMap getValidateServListByPlan(String strServices, String strPlanId)throws Exception,SQLException{
      ArrayList list = null;
       Connection conn = null;  
       ResultSet rs=null;
       OracleCallableStatement cstm = null;
       ServiciosBean  serviciosBean = null;
       String strMessage = null; 
       int iNumReg=0;
       String strSql = "BEGIN ORDERS.NP_ORDERS12_PKG.SP_GET_VALIDATE_SERV_FOR_PLAN(?,?,?,?); END;";
       System.out.println("strServices---------->" + strServices);
       System.out.println("strPlanId---------->" + strPlanId);
       try{
          conn = Proveedor.getConnection();
          cstm = (OracleCallableStatement) conn.prepareCall(strSql);       
          cstm.setString(1,strServices);
          cstm.setLong(2,MiUtil.parseLong(strPlanId));
          cstm.registerOutParameter(3, OracleTypes.ARRAY, "ORDERS.TT_NP_SERVICE_LST");
          cstm.registerOutParameter(4, OracleTypes.VARCHAR);       
          cstm.execute();      
          list = new ArrayList();
          strMessage  = cstm.getString(4);
          if (strMessage == null){
          ARRAY aryServicesList = (ARRAY)cstm.getObject(3);
          OracleResultSet adrs = (OracleResultSet) aryServicesList.getResultSet();
          while(adrs.next()) {
            STRUCT stcService = adrs.getSTRUCT(2);
            System.out.println("El valor obtenido en Plan es ----->" + stcService.getAttributes()[0]);
            list.add(MiUtil.defaultString(stcService.getAttributes()[0], ""));
          }
         }else{
          throw new Exception(strMessage);
         }
      }
      catch (Exception e) {
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn,cstm,rs);        
      }
      System.out.println("La lista Servicios tiene " + list.size());
      HashMap objHashMap = new HashMap();
      objHashMap.put("strMessage",strMessage);
      objHashMap.put("objServiciosBeanList",list);              
      return objHashMap;
  }
  
  /**
     Method : getValidateServListByProduct
     Purpose: Obtiene los servicios que no son compatibles con el Producto seleccionado
     Developer        Fecha       Comentario
     =============    ==========  ======================================================================
     Frank Picoy      09/10/2010  Creación
     Enrique Zubiaurr 28/02/2011  Modificación
     Enrique Zubiaurr 24/03/2011  Se agrega condición para cuando mensaje del API es "API-0004-INTERFACE"
     */
     public HashMap getValidateServListByProduct(String strServices, String strServicesDesc, String strPlanId, String strProduct)throws Exception,SQLException{
      ArrayList list = null;
       Connection conn = null;  
       ResultSet rs=null;
       OracleCallableStatement cstm = null;
       ServiciosBean  serviciosBean = null;
       String strMessage = null; 
       int iNumReg=0;
       
       String strSql = "BEGIN ORDERS.NP_ORDERS12_PKG.SP_GET_VALIDATE_SERV_FOR_PROD(?,?,?,?,?,?,?); END;";
   
    
       try{
         conn = Proveedor.getConnection();
         
          cstm = (OracleCallableStatement) conn.prepareCall(strSql);
          cstm.setString(1,"27");
          
          cstm.setString(2,strProduct);
          System.out.println("strProduct = "+strProduct);
          cstm.setLong(3,Integer.parseInt(strPlanId));
          System.out.println("strPlanId = "+strPlanId);
          cstm.setString(4,strServices);
          System.out.println("strServices = "+strServices);
          cstm.registerOutParameter(5, OracleTypes.ARRAY, "ORDERS.TT_NP_SERVICE_LST");
          cstm.registerOutParameter(6, OracleTypes.VARCHAR);
          cstm.registerOutParameter(7, OracleTypes.VARCHAR);
          cstm.execute();
          
          list = new ArrayList();
          
          strMessage  = cstm.getString(6);
          System.out.println("El strMessage es------------------##>"+strMessage);
          
          if (strMessage == null || strMessage.equals("API-0004-INTERFACE") || strMessage.equals("100")){

          ARRAY aryServicesList = (ARRAY)cstm.getObject(5);
          
          OracleResultSet adrs = (OracleResultSet) aryServicesList.getResultSet();
          
          while(adrs.next()) {
            STRUCT stcService = adrs.getSTRUCT(2);
            
            list.add(MiUtil.defaultString(stcService.getAttributes()[0], ""));
          }
         }else{
          throw new Exception(strMessage);
         }
         
      }
      catch (Exception e) {
         
         throw new Exception(e);
      }
      finally{
         closeObjectsDatabase(conn,cstm,rs);        
      }
      System.out.println("La lista Servicios tiene " + list.size());
      HashMap objHashMap = new HashMap();
      objHashMap.put("strMessage",strMessage);
      objHashMap.put("objServiciosBeanList",list);              
      return objHashMap;
  }
  
  /**
     Method : getServiceDefaultListByPlan
     Purpose: Obtiene los servicios adicionales por defecto de acuerdo al PlanId ingresado
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     Frank Picoy     23/11/2011  Creación
     */
     
    public HashMap getServiceDefaultListByPlan(long lPlanId) throws SQLException, Exception{       
       HashMap objHashMap = new HashMap();
       ArrayList list = null;
       Connection conn = null; 
       ResultSet rs=null;
       OracleCallableStatement cstm = null;
       ServiciosBean  serviciosBean = null; 
       String strMessage  = "";
       String strSql = "BEGIN ORDERS.NP_ORDERS25_PKG.SP_GET_LIST_PLAN_SERV_DEFAULT(?,?,?); END;";
       try{
          conn = Proveedor.getConnection();
          cstm = (OracleCallableStatement) conn.prepareCall(strSql);       
          cstm.setLong(1,lPlanId);
          cstm.registerOutParameter(2, OracleTypes.CURSOR);
          cstm.registerOutParameter(3, Types.CHAR);       
          cstm.execute();
          strMessage  = cstm.getString(3);       
          if( strMessage == null ){                
            rs = (ResultSet)cstm.getObject(2);
            list = new ArrayList();       
             while (rs.next()) {
                 serviciosBean = new ServiciosBean();
                 serviciosBean.setNpservicioid(rs.getLong("npservicioid"));
                 serviciosBean.setNpnomserv(rs.getString("npnomserv"));
                 serviciosBean.setNpnomcorserv(rs.getString("npnomcorserv"));
                 serviciosBean.setNpexcludingind(rs.getString("npexcludingind"));
                 list.add(serviciosBean);
             }        
          }       
       }
       catch (Exception e) {
         throw new Exception(e);
       }
       finally{
          closeObjectsDatabase(conn,cstm,rs);
       }
       objHashMap.put("objArrayList",list);
       objHashMap.put("strMessage",strMessage);
       return objHashMap;                                                                
   }
  
   

}