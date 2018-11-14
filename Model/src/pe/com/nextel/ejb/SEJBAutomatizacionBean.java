package pe.com.nextel.ejb;

import java.sql.SQLException;

import java.util.HashMap;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import pe.com.nextel.dao.AutomatizacionDAO;


public class SEJBAutomatizacionBean implements SessionBean {

 private SessionContext _context;
 private AutomatizacionDAO objAutomarizacionDAO = null;
 
  public void ejbCreate(){
      objAutomarizacionDAO = new AutomatizacionDAO();
  }

  public void setSessionContext(SessionContext context)
        throws EJBException {
        _context = context;
  }
  
    public void ejbRemove() throws EJBException {
        System.out.println("[SEJBAutomatizacionBean][ejbRemove()]");
    }

    public void ejbActivate() throws EJBException {
        System.out.println("[SEJBAutomatizacionBean][ejbActivate()]");
    }

    public void ejbPassivate() throws EJBException {
        System.out.println("[SEJBAutomatizacionBean][ejbPassivate()]");
    }
   
   
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
        return objAutomarizacionDAO.doShowButtonReplaceHandset(lOrderId,strLogin);
   }
   
   /**
   * Method : doErrorOperationImeiSim
   * Purpose: Permite mostrar la ventana de Errores de Operación Imei/Sim.
   * Developer       Fecha       Comentario
   * =============   ==========  ======================================================================
   * <br>Realizado por: <a href="mailto:fanny.najarro@nextel.com.pe">Fanny Najarro</a>
   * <br>Fecha: 04/06/2009
   * @param  long lOrderId   -- Identificador de la Orden.
   * @param  long lItemId    -- Identificador del Item.     
   * @param  long lItemDeviceId    -- Identificador del ItemDevice. 
   * @return hshRetorno HashMap 
   * @throws java.lang.Exception
   * @throws SQLException
   */
   
   public HashMap doErrorOperationImeiSim(long lOrderId, long lItemId, long lItemDeviceId)
        throws Exception, SQLException {
        return objAutomarizacionDAO.doErrorOperationImeiSim(lOrderId,lItemId,lItemDeviceId);
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
      return objAutomarizacionDAO.getOrderConstantes();
   }
   
   /**
   * Method : getOrderSpecificationInbox
   * Purpose: Permite Obtener las categorias e inbox de la orden y de la np_table.
   * Developer       Fecha       Comentario
   * =============   ==========  ======================================================================
   * <br>Realizado por: <a href="mailto:fanny.najarro@nextel.com.pe">Fanny Najarro</a>
   * <br>Fecha: 08/06/2009
   * @param  long lOrderId   -- Identificador de la Orden.
   * @return hshRetorno HashMap 
   * @throws java.lang.Exception
   * @throws SQLException
   */ 

   public HashMap getOrderSpecificationInbox(long lOrderId)throws Exception, SQLException {
      return objAutomarizacionDAO.getOrderSpecificationInbox(lOrderId);
   }
   
    /**
   * Method : doReplaceHandset
   * Purpose: Realiza ejecutar el Proceso de Reemplazo.
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
      return objAutomarizacionDAO.doReplaceHandset(lOrderId,strLogin,strCodeAplic);
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
   public HashMap getStatusNumber(String strCodApp, String strPhoneNumber)
   throws Exception, SQLException {
      return objAutomarizacionDAO.getStatusNumber(strCodApp,strPhoneNumber);
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
   * @return String
   * @throws SQLException
   */ 
   
   public String doUpdatePhoneItemDevice(long lOrderId, long lItemId, long lItemDeviceId, String phoneNumber)
   throws Exception, SQLException {
       return objAutomarizacionDAO.doUpdatePhoneItemDevice(lOrderId, lItemId,lItemDeviceId, phoneNumber);
   }

  
}