package pe.com.nextel.ejb;

import java.io.FileInputStream;

import java.sql.SQLException;

import java.util.HashMap;

import java.util.Properties;

import javax.ejb.SessionBean;
import pe.com.nextel.dao.RepairDAO;
import javax.ejb.SessionContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.StringTokenizer;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.sql.DataSource;
import java.util.ArrayList;
import javax.naming.Context;
import javax.naming.InitialContext;
import pe.com.nextel.bean.RepairBean;

import org.apache.log4j.Logger;

import pe.com.nextel.dao.RepairDAO;


import pe.com.nextel.util.Constante;

import wsp.equipmentnew.proxy.EquipmentNewProxySOAPClient;
import wsp.equipmentnew.proxy.be.EquipmentOutBean;
import pe.com.nextel.util.StaticProperties;

public class SEJBRepairBean implements SessionBean 
{
  private SessionContext _context;
    DataSource        ds                    = null;
    RepairDAO     objRepairDAO    = null;    
    
    protected static Logger logger = Logger.getLogger(SEJBRepairBean.class);

   public void ejbCreate() {
      objRepairDAO    =   new RepairDAO();
      //Generar una referencia para el DataSource
       try {
       Context context = new InitialContext();

           StaticProperties singleton = StaticProperties.instance();
           Properties properties = singleton.props;
         ds = (DataSource)context.lookup(properties.getProperty("JNDI.DATASOURCE"));
       }catch(Exception ex){
            ex.printStackTrace();
       }
      
  }

  public void ejbActivate()  {  }

  public void ejbPassivate()  {  }

  public void ejbRemove()  {  }

  public void setSessionContext(SessionContext ctx)  {  }
  
  /*public HashMap getOrderHistory(String strOrderID)throws SQLException,Exception {
    return objRepairDAO.getOrderHistory(strOrderID);
  }*/
  
  /*public  HashMap getImeiModelTab(RepairBean objRepairBean)  throws Exception {
      return objRepairDAO.getImeiModelTab(objRepairBean);
   }*/  
  
  public HashMap generateNacionalizacion(HashMap hshParameters)throws SQLException,Exception
  {
    return objRepairDAO.generateNacionalizacion(hshParameters);
  }
  public HashMap generateRAFile(HashMap hshParameters)throws SQLException,Exception
  {
    return objRepairDAO.generateRAFile(hshParameters);
  }
  
  public HashMap getBscsModelId(String modelName) throws SQLException,Exception
  {
    return objRepairDAO.getBscsModelId(modelName);
  }
  public HashMap existsImei(String imei) throws SQLException,Exception
  {
    return objRepairDAO.existsImei(imei);
  }
  
  public HashMap registerEquipmentNew(String imei, String numSerie, String modelId) {
        HashMap hshData = new HashMap();
        
        EquipmentNewProxySOAPClient clientEquipment = new EquipmentNewProxySOAPClient();
        String codApp = "ORDERS-CREAR_INCIDENTE-NACIONALIZACION";         
        
        try {         
            EquipmentOutBean result = clientEquipment.registerEquipmentNew(codApp, imei, numSerie, Long.valueOf(modelId).longValue());            
            hshData.put("lngEquipmentId", result.getEquipmentId());

            // Obtiene los valores del objeto AuditControl.
            String[] messages = result.getErrorMessage().split("\\|\\|");
            String idMsg = messages[0].split(":")[1].trim();
            String codError = messages[1].split(":")[1].trim();
            String errMsg = messages[2].split(":")[1].trim();
            errMsg = !errMsg.equals("OK") ? errMsg : null;
            
            hshData.put("idMsg", idMsg);
            hshData.put("codError", codError);        
            hshData.put("errMsg", errMsg);
           
            logger.info("Nuevo equipo registrado lngEquipmentId=" + result.getEquipmentId());
            
            System.out.println("idMsg=" + idMsg);
            System.out.println("codError=" + codError);
            System.out.println("errMsg=" + errMsg);
        } catch (Exception e) {
            logger.error(e.getMessage());   
            e.printStackTrace();
            System.out.println("Causita: " + e.getCause());
        }
        
        return hshData;
    }
    
    // MMONTOYA - Despacho en tienda
    public HashMap getModels(long orderId) throws SQLException, Exception {
        return objRepairDAO.getModels(orderId);
    }
    
    // YRUIZ - Generación de Orden de Soporte SSTT
    public String getPlanType(String strPhone, String strImei) 
    throws SQLException, Exception
    {
        return objRepairDAO.getPlanType(strPhone, strImei);
    }
    
     /**
      * Motivo: Obtiene una lista de los centros de Reparaciones
      * <br>Realizado por: <a href="mailto:paolo.ortega@teamsoft.com.pe">Paolo Ortega Ramirez</a>
      * <br>Fecha: 20/05/2014
      * @return    HashMap
      */
    public HashMap getCentroReparacion() throws SQLException, Exception{
        return objRepairDAO.getCentroReparacion();
    }
     
    /**
     * Motivo: Obtiene la cantidad de reparaciones
     * <br>Realizado por: <a href="mailto:carlos.delossantos@teamsoft.com.pe">Carlos De los santos</a>
     * <br>Fecha: 27/05/2014
     * @return    Int
     */
    public int getCantReparaciones( String strIMEI) throws SQLException, Exception{
       return objRepairDAO.getCantReparaciones(strIMEI);
    }
    
    public HashMap getOtherFailureList(int intRepairId, String strRepairTypeId) throws SQLException, Exception {
        return objRepairDAO.getOtherFailureList(intRepairId, strRepairTypeId);
    }
    
    public HashMap getSelectedOtherFailureList(int intRepairId, String strRepairTypeId) throws SQLException, Exception {
        return objRepairDAO.getSelectedOtherFailureList(intRepairId, strRepairTypeId);
    }
}