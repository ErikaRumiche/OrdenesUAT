package pe.com.nextel.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.ArrayList;
import java.util.HashMap;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import pe.com.nextel.bean.ItemBean;
import pe.com.nextel.bean.ItemDeviceBean;
import pe.com.nextel.bean.ItemErrorBean;
import pe.com.nextel.util.MiUtil;


public class AutomatizacionDAO extends GenericDAO {

   /**
   * Method : doShowButtonReplaceHandset
   * Purpose: Permite mostrar el Boton ReplaceHandset, si la Orden usa algún equipo de Almacen que no sea de la modalidad Propio,Alquiler en Cliente.
   * Developer       Fecha       Comentario
   * =============   ==========  ======================================================================
   * <br>Realizado por: <a href="mailto:fanny.najarro@nextel.com.pe">Fanny Najarro</a>
   * <br>Fecha: 02/06/2009
   * @param  long lOrderId   -- Identificador de la Orden.
   * @param  String strLogin -- User Id del usuario.     
   * @return hshRetorno HashMap 
   * @throws java.lang.Exception
   * @throws SQLException
   */ 

   public HashMap doShowButtonReplaceHandset(long lOrderId,String strLogin)
        throws Exception, SQLException {
        
        if(logger.isDebugEnabled())
          logger.debug("<--Iniciando doShowButtonReplaceHandset-->");
          
        logger.info("DAO-> lOrderId -->"+lOrderId);
        logger.info("DAO-> strLogin -->"+strLogin);
        
        OracleCallableStatement cstmt = null;
        Connection conn=null;
        String strResult = null;
        String strMessage=null;
        HashMap hshRetorno = new HashMap();
        
        try{
            String sqlStr = "BEGIN BPEL_WORKFLOW.SPI_GET_ORDER_EXIT_WAREHOUSE(?, ?, ?, ? ); END;";    
            conn = Proveedor.getConnection();    
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, lOrderId);        
            cstmt.setString(2, strLogin);        
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);  
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
            cstmt.execute();
            
            if(logger.isDebugEnabled())
              logger.debug("--Execute:BEGIN BPEL_WORKFLOW.SPI_GET_ORDER_EXIT_WAREHOUSE-->");
              
            strMessage = cstmt.getString(4);
            if(strMessage == null)
              strResult = cstmt.getString(3);
            
            logger.info("DAO-> strResult -->"+strResult);
            logger.info("DAO-> strMessage -->"+strMessage);
            
            hshRetorno.put("strResult",strResult);
            hshRetorno.put("strMessage",strMessage);
           
        }catch(Exception e){
            logger.error(formatException(e));         
            hshRetorno.put("strMessage",e.getMessage());        
        }
        finally{
          try{
            closeObjectsDatabase(conn,cstmt,null); 
          }catch(Exception e){
             logger.error(formatException(e));
          }
        }
        logger.info("<--Finalizando doShowButtonReplaceHandset-->");
        return hshRetorno;
   }
   
   /**
   * Method : doReplaceHandset
   * Purpose: Si el siguiente inbox es Activacion/Programacion o Almacen/Tienda y si se obtener Proceso o la Salida es de Almacen entonces se debe existir Reemplazo_SIM_IMEI.
   * Developer       Fecha       Comentario
   * =============   ==========  ======================================================================
   * <br>Realizado por: <a href="mailto:fanny.najarro@nextel.com.pe">Fanny Najarro</a>
   * <br>Fecha: 08/06/2009
   * @param  long lOrderId       -- Identificador de la Orden.
   * @param  String strLogin     -- User Id del usuario.     
   * @param  String strCodeAplic -- Codigo de Aplicacion.     
   * @return hshRetorno HashMap 
   * @throws java.lang.Exception
   * @throws SQLException
   */ 

   public HashMap doReplaceHandset(long lOrderId, String strLogin, String strCodeAplic)
        throws Exception, SQLException {
        
         if(logger.isDebugEnabled())
          logger.debug("<--Iniciando doReplaceHandset-->");
          
          logger.info("DAO-> lOrderId -->"+lOrderId);
          logger.info("DAO-> strLogin -->"+strLogin);
          logger.info("DAO-> strCodeAplic -->"+strCodeAplic);
          
          OracleCallableStatement cstmt = null;
          Connection conn=null;
          String strMessage=null;
          HashMap hshRetorno = new HashMap();
          
          try{
            String sqlStr = "BEGIN BPEL_WORKFLOW.SPI_GEN_REPLACE_SIMIMEI_ORDER(?, ?, ?, ? ); END;"; 
            
            conn = Proveedor.getConnection();    
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, lOrderId);        
            cstmt.setString(2, strLogin);        
            cstmt.setString(3, strCodeAplic);  
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
            cstmt.execute();
            
            if(logger.isDebugEnabled())
              logger.debug("--Execute:BEGIN BPEL_WORKFLOW.SPI_GEN_REPLACE_SIMIMEI_ORDER-->");
              
            strMessage = cstmt.getString(4);
            logger.info("DAO(doReplaceHandset): strMessage -->"+strMessage);
           
                   
            hshRetorno.put("strMessage",strMessage);
            
           
          }catch(Exception e){
              logger.error(formatException(e));         
              hshRetorno.put("strMessage",e.getMessage());        
          }
          finally{
            try{
              closeObjectsDatabase(conn,cstmt,null); 
            }catch(Exception e){
               logger.error(formatException(e));
            }
          }
          logger.info("<--Finalizando doReplaceHandset-->");
          return hshRetorno;
   }
   
   /**
   * Method : doErrorOperationImeiSim
   * Purpose: Llama a la API , muestra detalle del Error de Operacion ImieSim.
   * Developer         Fecha         Comentario
   * =============     ==========    ======================================================================
   * Fanny Najarro     04/06/2009    Creación
   * @param  long lOrderId        -- Identificador de la Orden.
   * @param  long lItemId         -- Identificador del Item.     
   * @param  long lItemDeviceId   -- Identificador del ItemDevice.    
   * @return hshRetorno HashMap 
   * @throws java.lang.Exception
   * @throws SQLException
   */
  

   public HashMap doErrorOperationImeiSim(long lOrderId, long lItemId, long lItemDeviceId)
        throws Exception, SQLException {
        
        if(logger.isDebugEnabled())
          logger.debug("<-- Iniciando doErrorOperationImeiSim -->");
          
        logger.info("DAO-> lOrderId -->"+lOrderId);
        logger.info("DAO-> lItemId -->"+lItemId);
        logger.info("DAO-> lItemDeviceId -->"+lItemDeviceId);
        
        OracleCallableStatement cstmt = null;
        ResultSet rs = null;
        Connection conn=null;
        String strMessage=null;
        HashMap h = new HashMap();
        HashMap hshRetorno = new HashMap();
        ItemErrorBean objItemErrorBean = null;
        
        try{        
            String sqlStr = "BEGIN ORDERS.SPI_GET_NP_ITEM_ERROR(?, ?, ?, ?, ? ); END;";    
            conn = Proveedor.getConnection();    
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, lOrderId);        
            cstmt.setLong(2, lItemId);        
            cstmt.setLong(3, lItemDeviceId);
            cstmt.registerOutParameter(4, OracleTypes.CURSOR);  
            cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
            cstmt.execute();
            
            if(logger.isDebugEnabled())
              logger.debug("--Execute:BEGIN ORDERS.SPI_GET_NP_ITEM_ERROR-->");
              
            strMessage = cstmt.getString(5);
            logger.info("strMessage DAO errorOperation ====>"+strMessage);
            if(strMessage == null){
              rs = (ResultSet)cstmt.getObject(4);
              
              while (rs.next()) {
                h.put("npphone",rs.getLong("npphone")+""); //GGRANADOS
                h.put("npaction",rs.getString("npaction"));
                h.put("npcreateddate",rs.getTimestamp("npcreateddate"));
                h.put("npdescription",rs.getString("npdescription"));
              }
            }
            logger.info("DAO-npphone ==>"+MiUtil.parseInt((String)h.get("npphone")));
            logger.info("DAO-npaction ==>"+(String)h.get("npaction"));
            logger.info("DAO-npdescription ==>"+(String)h.get("npdescription"));
            
            hshRetorno.put("hshItemError",h);
            hshRetorno.put("strMessage",strMessage);  
        
        }catch(Exception e){
            hshRetorno.put("strMessage",e.getMessage()); 
      }finally{
        try{
            closeObjectsDatabase(conn,cstmt,rs); 
          }catch (Exception e) {
            logger.error(formatException(e));
          }
      }
      logger.info(" <==== DAO:doErrorOperationImeiSim ===> "+(String)hshRetorno.get("hshItemError").toString());
      return hshRetorno;
   }
   
   /**
   * Method : getOrderConstantes
   * Purpose: Permite Obtener las constantes de la NP_CONST_PKG.
   * Developer       Fecha       Comentario
   * =============   ==========  ======================================================================
   * <br>Realizado por: <a href="mailto:fanny.najarro@nextel.com.pe">Fanny Najarro</a>
   * <br>Fecha: 08/06/2009
   * @return hshRetorno HashMap 
   * @throws java.lang.Exception
   * @throws SQLException
   */ 

   public HashMap getOrderConstantes()throws Exception, SQLException {
        
        if(logger.isDebugEnabled())
          logger.debug("<--Iniciando getOrderConstantes-->");
        
        OracleCallableStatement cstmt = null;
        Connection conn=null;
        ResultSet rs = null;
        String strMessage=null;
        HashMap h = new HashMap();
        HashMap hshRetorno = new HashMap();
        
        try{
            String sqlStr = "BEGIN ORDERS.SPI_GET_CONST_LIST(?, ? ); END;";    
            conn = Proveedor.getConnection();    
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.registerOutParameter(1, OracleTypes.CURSOR);  
            cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
            cstmt.execute();
            
            if(logger.isDebugEnabled())
              logger.debug("--Execute:BEGIN ORDERS.SPI_GET_CONST_LIST-->");
            
            strMessage = cstmt.getString(2);
            logger.info("DAO-> strMessage -->"+strMessage);
            if( strMessage == null ){
              rs = (ResultSet)cstmt.getObject(1);
              while (rs.next()) {
                h.put("wv_inbox_almacenTienda",rs.getString("wv_inbox_almacenTienda"));
                h.put("wv_inbox_almacenActivProg",rs.getString("wv_inbox_almacenActivProg"));
                h.put("wn_specif_cambioModelo",rs.getInt("wn_specif_cambioModelo")+"");
                h.put("wn_specif_telefMovilPostp",rs.getInt("wn_specif_telefMovilPostp")+"");
                h.put("wn_specif_telefMovilPrep",rs.getInt("wn_specif_telefMovilPrep")+"");
              }
            }
            hshRetorno.put("hshConstantes",h);
            hshRetorno.put("strMessage",strMessage);
           
        }catch(Exception e){
            logger.error(formatException(e));         
            hshRetorno.put("strMessage",e.getMessage());        
        }
        finally{
          try{
            closeObjectsDatabase(conn,cstmt,null); 
          }catch(Exception e){
             logger.error(formatException(e));
          }
        }        
        logger.info(" <==== DAO:getOrderConstantes ===> "+(String)hshRetorno.get("hshConstantes").toString());
        logger.info("<--Finalizando getOrderConstantes-->");
        return hshRetorno;
   }
   
   
   
   
   /**
   * Method : getOrderSpecificationInbox
   * Purpose: Permite Obtener las categorias e inbox de la orden y de la np_table.
   * Developer       Fecha       Comentario
   * =============   ==========  ======================================================================
   * <br>Realizado por: <a href="mailto:fanny.najarro@nextel.com.pe">Fanny Najarro</a>
   * <br>Fecha: 08/06/2009
   * @param  lOrderId   long
   * @return hshRetorno HashMap 
   * @throws java.lang.Exception
   * @throws SQLException
   */ 

   public HashMap getOrderSpecificationInbox(long lOrderId)throws Exception, SQLException {
        
        if(logger.isDebugEnabled())
          logger.debug("<--Iniciando getOrderSpecificationInbox-->");
        
        OracleCallableStatement cstmt = null;
        Connection conn=null;
        String strMessage=null;
        String strResult=null;
        String strCategory=null;
        HashMap hshRetorno = new HashMap();
        
        try{
            String sqlStr = "BEGIN ORDERS.SPI_GET_SPECIFICATION_INBOX(?, ?, ?, ? ); END;";    
            conn = Proveedor.getConnection();    
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, lOrderId);
            cstmt.registerOutParameter(2, OracleTypes.VARCHAR);  
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
            
            cstmt.execute();
            
            if(logger.isDebugEnabled())
              logger.debug("<--Execute:BEGIN ORDERS.SPI_GET_SPECIFICATION_INBOX-->");
            
            strMessage = cstmt.getString(3);
            if(strMessage == null)
              strResult = cstmt.getString(2);
              strCategory = cstmt.getString(4);
            
           
            logger.info("DAO-> strResult -->"+strResult);
            logger.info("DAO-> strCategory -->"+strCategory);
            logger.info("DAO-> strMessage -->"+strMessage);
            
            hshRetorno.put("strResult",strResult);
            hshRetorno.put("strResultCategory",strCategory);
            hshRetorno.put("strMessage",strMessage);
           
        }catch(Exception e){
            logger.error(formatException(e));         
            hshRetorno.put("strMessage",e.getMessage());        
        }
        finally{
          try{
            closeObjectsDatabase(conn,cstmt,null); 
          }catch(Exception e){
             logger.error(formatException(e));
          }
        }        
        logger.info("<--Finalizando getOrderSpecificationInbox-->");
        return hshRetorno;
   }
   
   /**
   * Method : getStatusNumber
   * Purpose: Permite Obtener el estado del numero de telefóno en base al número.
   * Developer       Fecha       Comentario
   * =============   ==========  ======================================================================
   * <br>Realizado por: <a href="mailto:fanny.najarro@nextel.com.pe">Fanny Najarro</a>
   * <br>Fecha: 09/07/2009
   * @param  codApp     String   --Codigo de Aplicacion
   * @param  number     String   --Numero de Telefono
   * @return hshRetorno HashMap 
   * @throws java.lang.Exception
   * @throws SQLException
   */ 

   public HashMap getStatusNumber(String strCodApp, String strPhoneNumber)throws Exception, SQLException {
        
        if(logger.isDebugEnabled())
          logger.debug("<--Iniciando getStatusNumber-->");
        
        OracleCallableStatement cstmt = null;
        Connection conn=null;
        String strMessage=null;
        String strStatusNumber="";
        String strBlockedNumber="";
        HashMap hshRetorno = new HashMap();
        
        try{
            String sqlStr = "BEGIN ORDERS.SPI_GET_STATUS_NUMBER(?, ?, ?, ?, ? ); END;";    
            conn = Proveedor.getConnection();    
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setString(1, strCodApp);
            cstmt.setString(2, strPhoneNumber); 
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);  
            cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
            cstmt.registerOutParameter(5, OracleTypes.VARCHAR);
            
            cstmt.execute();
            
            if(logger.isDebugEnabled())
              logger.debug("<--Execute:BEGIN ORDERS.SPI_GET_STATUS_NUMBER-->");
            
            strMessage = cstmt.getString(5);
            if(strMessage == null){
              strStatusNumber = cstmt.getString(3);
              strBlockedNumber = cstmt.getString(4);
            }
           
            logger.info(" strStatusNumber  --> "+strStatusNumber);
            logger.info(" strBlockedNumber --> "+strBlockedNumber);
            logger.info(" strMessage --> "+strMessage);
            
            hshRetorno.put("strStatusNumber",strStatusNumber);
            hshRetorno.put("strBlockedNumber",strBlockedNumber);
            hshRetorno.put("strMessage",strMessage);
           
        }catch(Exception e){
            logger.error(formatException(e));         
            hshRetorno.put("strMessage",e.getMessage());        
        }
        finally{
          try{
            closeObjectsDatabase(conn,cstmt,null); 
          }catch(Exception e){
             logger.error(formatException(e));
          }
        }        
        logger.info("<--Finalizando getStatusNumber-->");
        return hshRetorno;
   }
   
   /**
   * Method : doUpdatePhoneItemDevice
   * Purpose: Permite actualizar el numero de telefono en la np_item_Device.
   * Developer       Fecha       Comentario
   * =============   ==========  ======================================================================
   * <br>Realizado por: <a href="mailto:fanny.najarro@nextel.com.pe">Fanny Najarro</a>
   * <br>Fecha: 19/07/2009
   * @param  long lOrderId        -- Identificador de la Orden.
   * @param  long lItemId         -- Identificador del Item.     
   * @param  long lItemDeviceId   -- Identificador del ItemDevice.
   * @param  String phoneNumber   -- Número de Teléfono.
   * @return hshRetorno HashMap 
   * @throws java.lang.String
   * @throws SQLException
   */ 

   public String doUpdatePhoneItemDevice(long lOrderId, long lItemId, long lItemDeviceId, String phoneNumber)throws Exception, SQLException {
   
   if(logger.isDebugEnabled())
          logger.debug("<--Iniciando doUpdatePhoneItemDevice-->");
        
        OracleCallableStatement cstmt = null;
        Connection conn=null;
        String strMessage=null;
        String strStatusNumber="";
        String strBlockedNumber="";
        HashMap hshRetorno = new HashMap();
        
        try{
            String sqlStr = "BEGIN ORDERS.SPI_UPD_ORDER_NUMBER_PHONE(?, ?, ?, ?, ? ); END;";    
            conn = Proveedor.getConnection();    
            cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
            cstmt.setLong(1, lOrderId);
            cstmt.setLong(2, lItemId); 
            cstmt.setLong(3, lItemDeviceId); 
            cstmt.setString(4, phoneNumber);
            cstmt.registerOutParameter(5, OracleTypes.VARCHAR);  
           
            cstmt.executeUpdate();
            
            if(logger.isDebugEnabled())
              logger.debug("<--Execute:BEGIN ORDERS.SPI_UPD_ORDER_NUMBER_PHONE-->");
            
            strMessage = cstmt.getString(5);
                       
        }catch(Exception e){
            logger.error(formatException(e));         
            strMessage = e.getClass()+ " - "+e.getMessage();             
        }
        finally{
          try{
            closeObjectsDatabase(conn,cstmt,null); 
          }catch(Exception e){
             logger.error(formatException(e));
          }
        }
        logger.info("<--Finalizando doUpdatePhoneItemDevice-->");
        return strMessage;
    }
   
   
  /**
   * Method : getItemDeviceOrder
   * Purpose: Permite obtener la lista de itemdevice solo para las categorías 2001 y 2002
   * Developer       Fecha       Comentario
   * =============   ==========  ======================================================================
   * <br>Realizado por: <a href="mailto:cesar.lozza@nextel.com.pe">Cesar Lozza</a>
   * <br>Fecha: 12/10/2009   
   * @throws java.sql.SQLException
   * @throws java.lang.Exception
   * @return 
   * @param objItemBean
   * @param conn
   */
   public ArrayList getItemDeviceOrder(Connection conn, ItemBean objItemBean)throws Exception, SQLException {
   
      if(logger.isDebugEnabled())
         logger.debug("<--Iniciando getItemDeviceOrder-->");
   
      ResultSet rs=null;
      OracleCallableStatement cstm = null;
      ArrayList list  = null;
      ItemDeviceBean itemDeviceBean = null;
      String strMensaje = null;
      list = new ArrayList();
      try{
         String strSql = "BEGIN ORDERS.NP_ORDER_AUTOMATIZACION_PKG.SP_GET_ITEM_DEVICE_ORDER(?,?,?,?); END;";
         cstm = (OracleCallableStatement) conn.prepareCall(strSql);

         cstm.setLong(1, objItemBean.getNporderid());
         cstm.setLong(2, objItemBean.getNpitemid());
         cstm.registerOutParameter(3, OracleTypes.CURSOR);
         cstm.registerOutParameter(4, Types.CHAR);

         cstm.execute();
         strMensaje  = cstm.getString(4);
         if  ( strMensaje == null ){
            rs = (ResultSet)cstm.getObject(3);
            while(rs.next()){
               itemDeviceBean = new ItemDeviceBean();
               itemDeviceBean.setNporderid(rs.getInt("nporderid"));
               itemDeviceBean.setNpitemid(rs.getInt("npitemid"));
               itemDeviceBean.setNpitemdeviceid(rs.getLong("npitemdeviceid"));
               itemDeviceBean.setNpimeinumber(rs.getString("npimeinumber"));
               itemDeviceBean.setNpsimnumber(rs.getString("npsimnumber"));
               list.add(itemDeviceBean);
            }
         }

      }catch (Exception e) {
          logger.error(formatException(e));

      }finally{
         try{
            closeObjectsDatabase(null,cstm,rs);
         }catch (Exception e) {
            logger.error(formatException(e));
         }
      }
      logger.info("<--Finalizando getItemDeviceOrder-->");
      return list;
   }   
 
  /**
   * Method : updateItemError
   * Purpose: Permite actualizar el np_item_error y np_item_device
   * Developer       Fecha       Comentario
   * =============   ==========  ======================================================================
   * <br>Realizado por: <a href="mailto:cesar.lozza@nextel.com.pe">Cesar Lozza</a>
   * <br>Fecha: 12/10/2009 
   * @throws java.sql.SQLException
   * @throws java.lang.Exception
   * @return 
   * @param lItemDeviceIdNew
   * @param lItemDeviceIdOld
   * @param lItemId
   * @param lOrderId
   * @param conn
   */
   public String updateItemError(Connection conn, long lOrderId, long lItemId, long lItemDeviceIdOld, long lItemDeviceIdNew)throws Exception, SQLException {
   
      if(logger.isDebugEnabled())
         logger.debug("<--Iniciando updateItemError-->");
      
      OracleCallableStatement cstm = null;
      String strMensaje = null;
      try{
         String strSql = "BEGIN ORDERS.NP_ORDER_AUTOMATIZACION_PKG.SP_UPDATE_ITEM_ERROR(?,?,?,?,?); END;";
         cstm = (OracleCallableStatement) conn.prepareCall(strSql);

         System.out.println("lOrderId " + lOrderId);
         System.out.println("lItemId " + lItemId);
         System.out.println("lItemDeviceIdOld " + lItemDeviceIdOld);
         System.out.println("lItemDeviceIdNew " + lItemDeviceIdNew);
         cstm.setLong(1, lOrderId);
         cstm.setLong(2, lItemId);
         cstm.setLong(3, lItemDeviceIdOld);
         cstm.setLong(4, lItemDeviceIdNew);
         cstm.registerOutParameter(5, Types.CHAR);

         cstm.executeUpdate();
         strMensaje  = cstm.getString(5);
         

      }catch(Exception e){
         logger.error(formatException(e));         
         strMensaje = e.getClass()+ " - "+e.getMessage();             
      }
      finally{
         try{
            closeObjectsDatabase(null,cstm,null); 
         }catch(Exception e){
            logger.error(formatException(e));
         }
      }
      logger.info("<--Finalizando updateItemError-->");
      return strMensaje;
   }   
}