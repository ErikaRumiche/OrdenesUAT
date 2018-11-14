package pe.com.nextel.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import org.apache.commons.lang.StringUtils;

import pe.com.nextel.bean.ConfigurationBean;
import pe.com.nextel.util.Constante;


public class ConfigurationDAO extends GenericDAO {

    public HashMap getValueByConfiguration(String configuration) throws Exception, SQLException{
    
      HashMap hshResultado  = new HashMap();    
      Connection conn = null; 
      OracleCallableStatement cstmt = null;
      ResultSet rs = null;
      String strMensaje = "";      
      
      try {
      
        String sqlStr = "BEGIN ORDERS.NP_CONFIGURATION_PKG.SP_GET_VALUEDESC_X_NPCONFIG(?, ?, ?); END;";      
        conn = Proveedor.getConnection();
        
        cstmt = (OracleCallableStatement)conn.prepareCall(sqlStr);
        
        cstmt.setString(1, configuration);
        cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
        cstmt.registerOutParameter(3, OracleTypes.CURSOR);
        
        cstmt.executeUpdate();
        
        strMensaje = cstmt.getString(2);
        
        if( strMensaje == null ) {
        
          rs = (ResultSet)cstmt.getObject(3);
        
          if (rs.next()) {
          
             ConfigurationBean objConfigurationBean = new ConfigurationBean(); 
        
             objConfigurationBean.setNpValue(rs.getString("npvalue"));
             objConfigurationBean.setNpValueDesc(rs.getString("npvaluedesc"));
            
             hshResultado.put("objConfigurationBean",objConfigurationBean);     
            
          }
          
        }               
        
      } catch (Exception e) {
      
        logger.error(formatException(e));      
        hshResultado.put("strMessage", e.getMessage()); 
      
      } finally {
      
        try {
        
          closeObjectsDatabase(conn, cstmt, rs);  
        
        } catch (Exception e) {
        
          logger.error(formatException(e));
        
        }
        
      } 
      
      return hshResultado;
    
  }

  public HashMap getNpConfigurationValues(String strNpConfiguration, String strValue, String strValueDesc, String strNpStatus, String strNpTag1, String strNpTag2, String strNpTag3)  throws Exception, SQLException{
    Connection conn = null;
    OracleCallableStatement cstmt = null;
    String strMessage = null;
    HashMap objHashMapResultado = new HashMap();
    ArrayList arrNpConfiguration = new ArrayList();
    ResultSet rs = null;
    ConfigurationBean npcomfigurationBean = null;
        
    String sqlStr = "BEGIN ORDERS.NP_CONFIGURATION_PKG.SP_GET_NPCONFIGURATION(?,?,?,?,?,?,?,?,?); END;";
    try{
      conn = Proveedor.getConnection();
      cstmt = (OracleCallableStatement) conn.prepareCall(sqlStr);
      cstmt.setString(1, strNpConfiguration);
      cstmt.setString(2, strValue);
      cstmt.setString(3, strValueDesc);
      cstmt.setString(4, strNpStatus);
      cstmt.setString(5, strNpTag1);
      cstmt.setString(6, strNpTag2);
      cstmt.setString(7, strNpTag3);
      cstmt.registerOutParameter(8, OracleTypes.CURSOR);
      cstmt.registerOutParameter(9, OracleTypes.VARCHAR);		
      cstmt.executeQuery();
      strMessage = cstmt.getString(9);
      if (StringUtils.isBlank(strMessage)) {	
        rs = (ResultSet)cstmt.getObject(8);      
        while (rs.next()) {
          npcomfigurationBean = new ConfigurationBean();
          npcomfigurationBean.setNpValue(rs.getString("npvalue"));
          npcomfigurationBean.setNpValueDesc(rs.getString("npvaluedesc"));
          npcomfigurationBean.setNpTag1(rs.getString("nptag1"));
          npcomfigurationBean.setNpTag2(rs.getString("nptag2"));
          npcomfigurationBean.setNpTag3(rs.getString("nptag3"));
          arrNpConfiguration.add(npcomfigurationBean);		                
        }
      }
    }catch(Exception e){
      logger.error(formatException(e));
      throw new Exception(e);
    }finally{
      try{
        closeObjectsDatabase(conn,cstmt,rs);
      }catch (Exception e) {
        logger.error(formatException(e));
      }
    }  
    objHashMapResultado.put("objArrayList",arrNpConfiguration);
    objHashMapResultado.put(Constante.MESSAGE_OUTPUT,strMessage);
    return objHashMapResultado;	
  }
  
    /**
     Method : getTraceabilityConfigurations
     Purpose: Se obtiene las configuraciones para la funcionalidad de Trazabilidad de Pedidos
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     DERAZO          12/10/2017  REQ-0940: Creación
     */
    public HashMap getTraceabilityConfigurations() throws Exception, SQLException{
        HashMap objHashMap = new HashMap();
        Connection conn = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        String strMessage  = null;
        List<ConfigurationBean> listConfiguration = new ArrayList<ConfigurationBean>();

        try{
            String sqlStr = "BEGIN ORDERS.PKG_ORD_TRACEABILITY.SP_ORD_GET_CONFIG_TRACEABILITY(?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = conn.prepareCall(sqlStr);

            cstmt.registerOutParameter(1, OracleTypes.CURSOR);
            cstmt.registerOutParameter(2, OracleTypes.VARCHAR);

            cstmt.executeQuery();

            strMessage = cstmt.getString(2);
  
            if(strMessage == null){
                rs = (ResultSet)cstmt.getObject(1);
                while(rs.next()){
                    ConfigurationBean objConfigurationBean = new ConfigurationBean();
                    objConfigurationBean.setNpConfiguration(rs.getString("npconfiguration"));
                    objConfigurationBean.setNpValue(rs.getString("npvalue"));
                    objConfigurationBean.setNpValueDesc(rs.getString("npvaluedesc"));
                    listConfiguration.add(objConfigurationBean);
                }

                logger.debug("[ConfigurationDAO][getTraceabilityConfigurations] size listConfiguration: " + listConfiguration.size());
                objHashMap.put("listConfiguration", listConfiguration);
            }

            logger.debug("[ConfigurationDAO][getTraceabilityConfigurations] strMessage: "+strMessage);
            objHashMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        }
        catch(Exception e){
            logger.error(formatException(e));
            objHashMap.put(Constante.MESSAGE_OUTPUT, e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }
            catch(Exception e){
                logger.error(formatException(e));
            }
        }

        return objHashMap;
    }

    /**
     Method : getValidateShowContacts
     Purpose: Se valida si la orden va a mostrar o no la seccion de contactos
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     DERAZO          13/10/2017  REQ-0940: Creación
     */
    public HashMap getValidateShowContacts(long orderid) throws Exception, SQLException{
        HashMap objHashMap = new HashMap();
        Connection conn = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        String strMessage  = null;
        int resultValidation = 0;

        try{
            String sqlStr = "BEGIN ORDERS.PKG_ORD_TRACEABILITY.SP_ORD_VALIDATE_SHOW_CONTACTS(?, ?, ?); END;";
            conn = Proveedor.getConnection();
            cstmt = conn.prepareCall(sqlStr);

            cstmt.setLong(1, orderid);
            cstmt.registerOutParameter(2, OracleTypes.NUMERIC);
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR);

            cstmt.executeQuery();

            resultValidation = cstmt.getInt(2);
            logger.debug("[ConfigurationDAO][getValidateShowContacts] resultValidation: "+resultValidation);
            objHashMap.put("resultValidation", resultValidation);

            strMessage = cstmt.getString(3);
            logger.debug("[ConfigurationDAO][getValidateShowContacts] strMessage: "+strMessage);
            objHashMap.put(Constante.MESSAGE_OUTPUT, strMessage);
        }
        catch(Exception e){
            logger.error(formatException(e));
            objHashMap.put(Constante.MESSAGE_OUTPUT, e.getMessage());
        }
        finally{
            try{
                closeObjectsDatabase(conn, cstmt, rs);
            }
            catch(Exception e){
                logger.error(formatException(e));
            }
        }

        return objHashMap;
    }
}