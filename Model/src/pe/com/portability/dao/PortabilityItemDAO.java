package pe.com.portability.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.ArrayList;
import java.util.HashMap;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import oracle.sql.ARRAY;
import oracle.sql.STRUCT;

import org.apache.commons.lang.StringUtils;

import pe.com.nextel.bean.ServiciosBean;
import pe.com.nextel.dao.GenericDAO;
import pe.com.nextel.dao.Proveedor;
import pe.com.portability.bean.PortabilityItemBean;


public class PortabilityItemDAO  extends GenericDAO{

  public PortabilityItemDAO()
  {
  }
  
   /**************************************************************************************************
   Method : getItemOrderPortabilityReturn
   Purpose: Obtiene los Items asociados a una órden de Portabilidad de Retorno
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Karen Salvador  25/08/2009  Creación
   ***************************************************************************************************/
	public HashMap getItemOrderPortabilityReturn(long lOrderId) throws SQLException, Exception {
    
    Connection conn = null; 
    ResultSet rs=null;
    OracleCallableStatement cstm = null;
    ArrayList list  = null;
    PortabilityItemBean portabilityItemBean = null;
    String strMessage = null;
    HashMap objHashMapResultado = new HashMap();
    
      
    try{
         String strSql = "BEGIN ORDERS.NP_PORTABILITY_PROCESS_PKG.SP_GET_ITEM_PORTABILITY_RETURN(?,?,?); END;";      
         conn = Proveedor.getConnection();
         cstm = (OracleCallableStatement) conn.prepareCall(strSql);
         cstm.setLong(1,lOrderId);
         cstm.registerOutParameter(2, OracleTypes.CURSOR);
         cstm.registerOutParameter(3, Types.CHAR);
         cstm.execute();
         strMessage  = cstm.getString(3);
         
         if( strMessage == null){
            rs = (ResultSet)cstm.getCursor(2);
            list = new ArrayList();
            
            while(rs.next()){
                portabilityItemBean = new PortabilityItemBean();
                portabilityItemBean.setNpPortabItemId(rs.getLong("np_portaitemid"));
                portabilityItemBean.setNpPortabOrderId(rs.getLong("np_portaorderid"));
                portabilityItemBean.setNpphonenumber(rs.getString("np_phonenumber"));
                portabilityItemBean.setnNpStatusPhoneBSCS(rs.getString("np_statusphonebscs"));
                portabilityItemBean.setMessagetype(rs.getString("np_statusmessage"));
                portabilityItemBean.setNpexecutiondatereturn(rs.getString("np_executedate"));
                portabilityItemBean.setNpSequenceId(rs.getLong("np_sequenceid"));
                portabilityItemBean.setNpScheduleDelayBSCS(rs.getString("np_delaybscs"));
                portabilityItemBean.setNpRejectType(rs.getString("np_rejecttype"));
                portabilityItemBean.setNpLimitTimerDate(rs.getString("np_limittimerdate"));
                portabilityItemBean.setNpComment(rs.getString("np_comment"));
                portabilityItemBean.setNpPhoneNumberWN(rs.getString("np_phonenumber_wn"));

                portabilityItemBean.setNpReasonType(rs.getString("np_reason_return"));
                portabilityItemBean.setNpReasonDesc(rs.getString("np_reason_return_desc"));


                list.add(portabilityItemBean);  
            }
         }
         objHashMapResultado.put("strMessage",strMessage);
         objHashMapResultado.put("objArrayList",list);  
      }catch (Exception e) {
        logger.error(formatException(e));
        objHashMapResultado.put("strMessage",e.getMessage());
      } finally{
          try{
            closeObjectsDatabase(conn,cstm,rs); 
          }catch (Exception e) {
              logger.error(formatException(e));
         }     
      }
      return objHashMapResultado;
      
  }
  
   /**************************************************************************************
    Method : doUpdateItemPortabilityReturn
    Porpouse: Actualiza el item de la Ordend de Portabilidad
    Developer       Fecha       Comentario
   =============   ==========  ============================================================
   Karen Salvador  25/08/2009  Creación
   *******************************************************************************************/
    public String doUpdateItemPortabilityReturn(PortabilityItemBean portabilityItemBean,Connection conn) throws Exception, SQLException { 
        
        OracleCallableStatement cstmt = null;
        String strMessage = null; 
        
         try{     
               String strSql = "BEGIN ORDERS.NP_PORTABILITY_PROCESS_PKG.SP_UPD_ITEM_PORTABILITY_RETURN( ?, ?, ?, ?); END;"; 
               cstmt = (OracleCallableStatement)conn.prepareCall(strSql); 
               cstmt.setLong(1, portabilityItemBean.getNpPortabItemId());
               cstmt.setLong(2, portabilityItemBean.getNpPortabOrderId());
               cstmt.setString(3, portabilityItemBean.getNpComment());
               cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
               cstmt.executeUpdate();
               strMessage = cstmt.getString(4);
      
        }catch (Exception e) {
                logger.error(formatException(e));
                strMessage = e.getClass() + " - " + e.getMessage();
        }finally{
            try{
                closeObjectsDatabase(null,cstmt,null);
            }catch (Exception e) {
                logger.error(formatException(e));
            }
      }
    
    return strMessage;

  }
  
  /*******************************************************************************************************
   Method : getItemOrderPortabilityReturnHome
   Purpose: Obtiene los Items asociados a una órden de Portabilidad de Retorno cuando el Cedente es Nextel
   Developer       Fecha       Comentario
   =============   ==========  ===========================================================================
   Karen Salvador  02/09/2009  Creación
   ********************************************************************************************************/
	public HashMap getItemOrderPortabilityReturnHome(long lOrderId) throws SQLException, Exception {
    
    Connection conn = null; 
    ResultSet rs=null;
    OracleCallableStatement cstm = null;
    ArrayList list  = null;
    PortabilityItemBean portabilityItemBean = null;
    String strMessage = null;
    HashMap objHashMapResultado = new HashMap();
    
      
    try{
         String strSql = "BEGIN ORDERS.NP_PORTABILITY_PROCESS_PKG.SP_GET_ITEM_PORTA_RETURN_HOME(?,?,?); END;";      
         conn = Proveedor.getConnection();
         cstm = (OracleCallableStatement) conn.prepareCall(strSql);
         cstm.setLong(1,lOrderId);
         cstm.registerOutParameter(2, OracleTypes.CURSOR);
         cstm.registerOutParameter(3, Types.CHAR);
         cstm.execute();
         strMessage  = cstm.getString(3);
         
         if( strMessage == null){
            rs = (ResultSet)cstm.getCursor(2);
            list = new ArrayList();
            
            while(rs.next()){
                portabilityItemBean = new PortabilityItemBean();
                portabilityItemBean.setNpPortabItemId(rs.getLong("np_portaitemid"));
                portabilityItemBean.setNpPortabOrderId(rs.getLong("np_portaorderid"));
                portabilityItemBean.setNpphonenumber(rs.getString("np_phonenumber"));
                portabilityItemBean.setNpexecutiondatereturn(rs.getString("np_executiondatereturn"));
                portabilityItemBean.setNpReasonType(rs.getString("np_reasontype"));
                portabilityItemBean.setMessagetype(rs.getString("np_statusmessage"));
                portabilityItemBean.setNpSequenceId(rs.getLong("np_sequenceid"));
                portabilityItemBean.setNpScheduleDelayBSCS(rs.getString("np_delaybscs"));
                portabilityItemBean.setNpComment(rs.getString("np_comment"));
                
                portabilityItemBean.setNpPhoneNumberWN(rs.getString("np_phonenumber_wn"));
                
                list.add(portabilityItemBean);  
            }
         }
         objHashMapResultado.put("strMessage",strMessage);
         objHashMapResultado.put("objArrayList",list);  
      }catch (Exception e) {
        logger.error(formatException(e));
        objHashMapResultado.put("strMessage",e.getMessage());
      } finally{
          try{
            closeObjectsDatabase(conn,cstm,rs); 
          }catch (Exception e) {
              logger.error(formatException(e));
         }     
      }
      return objHashMapResultado;
      
  }
  
  /****************************************************************************************************************
   Method : getDetailItemPortabilityReturnHome
   Purpose: Obtiene el detalle del item asociado a una órden de Portabilidad de Retorno cuando el Cedente es Nextel
   Developer       Fecha       Comentario
   =============   ==========  ====================================================================================
   Karen Salvador  04/09/2009  Creación
   ****************************************************************************************************************/
	public HashMap getDetailItemPortabilityReturnHome(String strPhoneNumber) throws SQLException, Exception {
    
    Connection conn = null; 
    ResultSet rs=null;
    OracleCallableStatement cstm = null;
    ArrayList list  = null;
    
    String strMessage = null;
    String strPlanTarifarioName = null;
    HashMap objHashMapResultado = new HashMap();
    ArrayList arrServiceList = new ArrayList();
    PortabilityItemBean portabilityItemBean =  new PortabilityItemBean();
    
      
    try{
         String strSql = " BEGIN ORDERS.NP_PORTABILITY_PROCESS_PKG.SP_GET_PHONE_PORTA_RETURN_HOME(?,?,?,?); END;";      
         conn = Proveedor.getConnection();
         cstm = (OracleCallableStatement) conn.prepareCall(strSql);
         cstm.setString(1,strPhoneNumber);
         cstm.registerOutParameter(2, OracleTypes.VARCHAR);
         cstm.registerOutParameter(3,OracleTypes.ARRAY, "ORDERS.TT_SERVICE_PORTA_LST");
         cstm.registerOutParameter(4, OracleTypes.VARCHAR);
         cstm.execute();
         
         strMessage  = cstm.getString(4);
         
         ARRAY aryServicioList = (ARRAY)cstm.getObject(3);
         
         if (StringUtils.isBlank(strMessage)) {
         
            strPlanTarifarioName =  cstm.getString(3);    
            
              for (int i = 0; i < aryServicioList.getOracleArray().length; i++) {
                  STRUCT stcServicio = (STRUCT) aryServicioList.getOracleArray()[i];
                  
                  portabilityItemBean.setNpServiceContract(stcServicio.getOracleAttributes()[0].toString());
                  portabilityItemBean.setNpServiceContractStatus(stcServicio.getOracleAttributes()[1].toString());
                  arrServiceList.add(i, portabilityItemBean);
              }
         }
      }catch (Exception e) {
        logger.error(formatException(e));
        objHashMapResultado.put("strMessage",e.getMessage());
      } finally{
          try{
            closeObjectsDatabase(conn,cstm,rs); 
          }catch (Exception e) {
              logger.error(formatException(e));
         }     
      }

      objHashMapResultado.put("arrServiceList", arrServiceList);
      objHashMapResultado.put("strPlanTarifarioName",strPlanTarifarioName);
       objHashMapResultado.put("strMessage",strMessage);
      
      return objHashMapResultado;
      
  }

  
     /**
     Method : getDetailServicePortabilidad
     Purpose: Obtiene el detalle de un servicio en Portabilidad
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     Karen Salvador  04/09/2009  Creación
     */
    public static ServiciosBean getDetailServicePortabilidad(long lngServiceId) throws Exception,SQLException{
       
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
                serviciosBean.setNpstatus(rs.getString("npstatus"));
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
   
  
}