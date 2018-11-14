package pe.com.nextel.ejb;

import java.rmi.RemoteException;

import java.sql.SQLException;

import java.util.HashMap;

import javax.ejb.EJBObject;


public interface SEJBAutomatizacionRemote extends EJBObject 
{
       /**
       * Method : doShowButtonReplaceHandset
       * Purpose: Permite mostrar el Boton ReplaceHandset, si la Orden usa algún equipo de Almacen que no sea de la modalidad Propio,Alquiler en Cliente.
       * Developer       Fecha       Comentario
       * =============   ==========  ======================================================================
       * <br>Realizado por: <a href="mailto:fanny.najarro@nextel.com.pe">Fanny Najarro</a>
       * <br>Fecha: 02/06/2009
       */
      
        HashMap doShowButtonReplaceHandset(long lOrderId,String strLogin)
        throws Exception, SQLException, RemoteException;
        
        /**
         * Method : doErrorOperationImeiSim
         * Purpose: Permite mostrar la ventana de Errores de Operación Imei/Sim.
         * Developer       Fecha       Comentario
         * =============   ==========  ======================================================================
         * <br>Realizado por: <a href="mailto:fanny.najarro@nextel.com.pe">Fanny Najarro</a>
         * <br>Fecha: 04/06/2009
         */
         
         HashMap doErrorOperationImeiSim(long lOrderId, long lItemId, long lItemDeviceId)
         throws Exception, SQLException, RemoteException;
         
         /**
         * Method : getOrderConstantes
         * Purpose: Permite Obtener las constantes de la NP_CONST_PKG.
         * Developer       Fecha       Comentario
         * =============   ==========  ======================================================================
         * <br>Realizado por: <a href="mailto:fanny.najarro@nextel.com.pe">Fanny Najarro</a>
         * <br>Fecha: 08/06/2009
         */          
         HashMap getOrderConstantes()
         throws Exception, SQLException, RemoteException;
         
         
         /**
          * Method : doReplaceHandset
          * Purpose: Permite Obtener el Mensaje de ejecucion Proceso de Reemplazo.
          * Developer       Fecha       Comentario
          * =============   ==========  ======================================================================
          * <br>Realizado por: <a href="mailto:fanny.najarro@nextel.com.pe">Fanny Najarro</a>
          * <br>Fecha: 08/06/2009
          */ 
        
            HashMap doReplaceHandset(long lOrderId, String strLogin, String strCodeAplic)
            throws Exception, SQLException, RemoteException;
         
         /**
          * Method : getOrderSpecificationInbox
          * Purpose: Permite Obtener las categorias e inbox de la orden y de la np_table.
          * Developer       Fecha       Comentario
          * =============   ==========  ======================================================================
          * <br>Realizado por: <a href="mailto:fanny.najarro@nextel.com.pe">Fanny Najarro</a>
          * <br>Fecha: 08/06/2009
          */ 
        
           HashMap getOrderSpecificationInbox(long lOrderId)
           throws Exception, SQLException, RemoteException;
           
           
          /**
          * Method : getStatusNumber
          * Purpose: Permite Obtener el estado de Numero de Telefono.
          * Developer       Fecha       Comentario
          * =============   ==========  ======================================================================
          * <br>Realizado por: <a href="mailto:fanny.najarro@nextel.com.pe">Fanny Najarro</a>
          * <br>Fecha: 09/07/2009
          */ 
           public HashMap getStatusNumber(String strCodApp, String strPhoneNumber)
           throws Exception, SQLException, RemoteException;
           
          /**
          * Method : doUpdatePhoneItemDevice
          * Purpose: Permite actualizar el numero de telefono en la np_item_Device.
          * Developer       Fecha       Comentario
          * =============   ==========  ======================================================================
          * <br>Realizado por: <a href="mailto:fanny.najarro@nextel.com.pe">Fanny Najarro</a>
          * <br>Fecha: 19/07/2009
          */ 
           public String doUpdatePhoneItemDevice(long lOrderId, long lItemId, long lItemDeviceId, String phoneNumber)
           throws Exception, SQLException, RemoteException;
   
     
   
     
}