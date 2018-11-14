package pe.com.nextel.section.sectionItem;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import pe.com.nextel.bean.ApportionmentBean;
import pe.com.nextel.bean.BaAssignmentBean;
import pe.com.nextel.bean.ConfigurationBean;
import pe.com.nextel.bean.ItemBean;
import pe.com.nextel.bean.ItemDeviceBean;
import pe.com.nextel.bean.ItemServiceBean;
import pe.com.nextel.bean.LoadMassiveItemBean;
import pe.com.nextel.dao.*;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.GenericObject;
import pe.com.nextel.util.MiUtil;
import pe.com.nextel.util.RequestHashMap;
import pe.com.nextel.util.GenericObject;


/**
 * Motivo: Clase de Secciones de Dinámicas que ejecuta eventos para los Items (Save,Update,Delete).
 * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
 * <br>Fecha: 28/08/2007
 * @see SectionItemEvents
 */
 

public class SectionItemEvents extends GenericObject {


    ItemDAO objItemDAO = new ItemDAO();
    AutomatizacionDAO objAutomatizacionDAO = new AutomatizacionDAO();
    
    //DERAZO REQ-0428 DAO
    PenaltyDAO penaltyDAO = new PenaltyDAO();
    
    //CDIAZ [PRY-0817]
    ExceptionDAO objExceptionDAO =   new ExceptionDAO();
    
    /**
    * Motivo: Evento de grabado de Items de la orden
    * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
    * <br>Fecha: 30/10/2007
    * @param     RequestHashMap request  
    * @param     Connection conn       
    * @return    String resultInsertItems 
    */
  public String saveSection1(RequestHashMap request,Connection conn)  throws Exception, SQLException{
    System.out.println("=========== Inicio - Registro del ITEM ===========");
    String resultInsertItems          =   "";
    
    resultInsertItems           = grabarItems(request,conn, null);
    System.out.println("=========== Fin - Registro del ITEM ===========");
    return resultInsertItems;
  }
     
    /**
    * Motivo: Evento de actualización de Items de la orden
    * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
    * <br>Fecha: 30/10/2007
    * @param     RequestHashMap request  
    * @param     Connection conn       
    * @return    String resultInsertItems 
    */ 
    public String updateSection1(RequestHashMap request,Connection conn) throws Exception, SQLException{
        //System.out.println("Update de Items");
        logger.info("[Update_section_Items]");
	
        String resultInsertItems          =   "";
        try{ 
          resultInsertItems = actualizarItems(request,conn, null);
          return resultInsertItems;
        }catch(Exception ex){
          ex.printStackTrace();
          return MiUtil.getMessageClean("[Exception][updateSection1]["+ex.getClass() + " " + ex.getMessage()+ " - Caused by " + ex.getCause() + "]");
        }
    }  
    
     
  /**
  * Motivo: Registra los Items, Servicios Adicionales, Imeis , Excepciones y Adendas de una orden
  * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
  * <br>Fecha: 30/10/2007
  * @param     RequestHashMap request
  * @param     Connection conn
  * @return    String resultInsertItems
  * Modificado por: Ruth Polo   13/01/2009
  * Se valida para que no se cree una orden solciitando el mismo Servicio para un equipo.
  */
  public String grabarItems(RequestHashMap request, Connection conn, List<ItemBean> listItemSave) throws Exception, SQLException{
    
    String    resultTransaction             = "";
    String[]  pv_item_cantidad              = request.getParameterValues("hdnIndice");
    String    resultValidateItems           ="";

    //DERAZO REQ-0428
    int pn_flag_penalty_func = Integer.valueOf(request.getParameter("hdnFlagPenaltyFunct"));

    //DATOS PARA ITEMS
    ItemBean  itemBean    =   new ItemBean();
    String    strMessageImei  =   null;
    
    if( pv_item_cantidad != null ){
      int cantItems = pv_item_cantidad.length;
      OrderDAO objOrderDAO = new OrderDAO();
      logger.info("[Cantidad_Items]: " + cantItems);
      //RPOLO validacion de items
      logger.info("[validateItems]: ACTIVAR_DESACTIVAR_SERVICIOS");
      resultValidateItems = validateItems(request,conn,cantItems);
      if (resultValidateItems != null)
        return resultValidateItems;
      
      for(int i=0; i<cantItems; i++){      
        
        //Insertar ITEMS
        logger.info("[Graba en la tabla np_item /*Inserta un Item a la orden Orden*/]");
        resultTransaction  = grabarItem(request,conn,itemBean,i,"INSERT");
        
        if( resultTransaction!= null)
            return resultTransaction;
        
        String itemIdSave = ""+itemBean.getNpitemid();
        logger.info("[i] " + i + "[itemIdSave]: " + itemIdSave);
        logger.info("[Graba en la tabla orders.np_ba_assignment]");
        System.out.println("SectionItemEvents/grabarItems -> itemIdSave : " + itemIdSave);
        
        //PRY-0890 JCURI
        if(listItemSave != null) {
        	listItemSave.add(getDataItemForApportionment(itemBean));
        }
        
        resultTransaction  = grabarItemsBillingAccount(request,conn,itemIdSave,i); 
        
        if( resultTransaction!= null)
            return resultTransaction;
        
        logger.info("[graba en la tabla: SP_INS_ORDER_ITEM_SERVICE]");
        resultTransaction  = grabarItemsServices(request,conn,itemIdSave,i); 
        
        if( resultTransaction!= null)
            return resultTransaction;
                            
        String[]  pv_list_adenda     = request.getParameterValues("txtListAdenda");     
        
        if(pv_list_adenda != null){
          logger.info("[graba en la table np_order_template]");				
          resultTransaction  = insertTemplateOrder(request,conn,itemIdSave,i); 
		  	
          if( resultTransaction!= null)
               return resultTransaction;                            
            //return resultTransaction;        
          //System.out.println("[SectionItemEvents.Grabar] insertTemplateOrder = "+itemIdSave);       
        }
        //JPEREZ: Cuando falle la validación de un IMEI/SIM, se debe continuar validando el resto y 
        // mostrar el mensaje global al final 
        logger.info("[graba en la tabla np_item_device]"); 
        resultTransaction = grabarItemsImeis(request,conn,itemIdSave,i);
              
        if( resultTransaction!= null)
            strMessageImei =MiUtil.getString(strMessageImei) + " "+ resultTransaction;
               
        //DERAZO REQ-0428 Insertar Items con adenda
        logger.info("[Insertar item al crear]: FlagPenaltyFunct: "+pn_flag_penalty_func);

        if(pn_flag_penalty_func == 1) { //Validamos flag universal
            String[]  pv_item_penalidad = request.getParameterValues("hdnItemValuetxtPagoPenalidad");
            String[]  pv_item_PriceException_Val = request.getParameterValues("hdnItemValuetxtItemPriceException");
            String pagoPenalidad = MiUtil.getStringObject(pv_item_penalidad,i);
            String tipoPrecio = itemBean.getNppricetype();
            String precioExcepcion = MiUtil.getStringObject(pv_item_PriceException_Val, i);

            logger.info("[Insertar item al crear]: OrderId: "+itemBean.getNporderid()+" ItemId: "+itemBean.getNpitemid());
            logger.info("[Insertar item al crear]: PriceType: "+tipoPrecio+" PagoPenalidad: "+pagoPenalidad+" precioExcepcion: "+precioExcepcion);

            if(pagoPenalidad != null && !pagoPenalidad.equals("")){
                long  hasPenalty = 2; //No tiene adenda

                if(pagoPenalidad.equals("S")) {
                    if(precioExcepcion != null && !precioExcepcion.equals("")){
                        //Tiene penalidad porque tiene precio de excepcion
                        hasPenalty = 1;
                    }
                    else{
                        //Tiene adenda, validamos si tendra penalidad o no
                        if(tipoPrecio.equals("ORIGINAL")){
                            //No tiene penalidad porque esta eligiendo precio de lista
                            hasPenalty = 0;
                        }
                        else{
                            //Tiene penalidad porque esta eligiendo una promocion
                            hasPenalty = 1;
                            if(!validarPenalidadCambioDePlan(request, itemBean)){
                        		hasPenalty = 0;
                        }
                    }
                }
                }

                logger.info("[Insertar item al crear]: Entra al metodo grabarItemPenalidad");
                resultTransaction = grabarItemPenalidad(request, conn, itemBean, hasPenalty, "INSERT");

                if(resultTransaction != null)
                    return resultTransaction;
            }
        }
        //FIN DERAZO

     }//Fin del For
     if ( !MiUtil.getString(strMessageImei).equalsIgnoreCase("") )
      return strMessageImei;
        
   }//Fin del If
          
   return null;
   
  }
  
  
  
    /**
    * Motivo: Actualiza el Item, Servicios Adicionales, Imeis , Excepciones y Adendas de una orden
    * <br>Realizado por: <a href="mailto:lee.rosales@nextel.com.pe">Lee Rosales</a>
    * <br>Fecha: 30/10/2007
    * @param     RequestHashMap request  
    * @param     Connection conn       
    * @return    String resultInsertItems 
    */  
    public String actualizarItems(RequestHashMap request,Connection conn, List<ItemBean> listItemSave)  throws Exception, SQLException{ 

        String    resultTransaction             = "";
        String    strChangedOrder               = MiUtil.getString(request.getParameter("hdnChangedOrder"));
        //RDELOSREYES - Carga Masiva - 29/10/2008 - INICIO
        HashMap hshDataMap = (HashMap) request.get(Constante.DATA_STRUCT);
        if(hshDataMap!=null) {
          LoadMassiveItemBean loadMassiveItemBean = (LoadMassiveItemBean) MapUtils.getObject(hshDataMap, "loadMassiveItemBean");
          if(loadMassiveItemBean!=null) {
            strChangedOrder = "S";
          }
        }
        //RDELOSREYES - Carga Masiva - 29/10/2008 - FIN
        String    pn_order_id                   = request.getParameter("hdnNumeroOrder");
        String    pv_session_login              = request.getParameter("hdnSessionLogin");
        String    pv_items_borrados             = request.getParameter("hdnItemBorrados");
        String[]  pv_item_cantidad              = request.getParameterValues("hdnIndice");
        String[]  pv_item_flag_save             = request.getParameterValues("hdnFlagSave");
        String[]  pv_list_adenda                = request.getParameterValues("txtListAdenda");
        
        //DERAZO REQ-0428
        int pn_flag_penalty_func = Integer.valueOf(request.getParameter("hdnFlagPenaltyFunct"));
        
        //DATOS PARA ITEMS
        ItemBean  itemBean  =  new ItemBean();
        String    av_message  = "";
        String    strImeiMessage = null;
		
        logger.info("[Actualizacion de items]");
        
        if( pv_item_cantidad != null ){
         int cantItems = pv_item_cantidad.length;   
         logger.info("[Antes_For Cantidad_Items]: " + cantItems);
         for(int i=0; i<cantItems; i++){            
         /*Lógica para Ingresar/Actualizar/Eliminar un ITEM*/   
              logger.info("[En_el_For_siempre_en_cuando_variable_i_menor_cantItems]");	
              if( MiUtil.getStringObject(pv_item_flag_save,i).equals("N") ){               
                 //resultTransaction  = objItemDAO.getItemInsertar(itemBean,conn);
                 //BUSCAR ERROR
                 System.out.println("Entra al Metodo: grabarItem(request,conn,itemBean,i,INSERT)");
                 resultTransaction  = grabarItem(request,conn,itemBean,i,"INSERT");
            
                 if( resultTransaction!= null)
                    return resultTransaction;

                 String itemIdSave = ""+itemBean.getNpitemid();
               
                 //PRY-0890 JCURI
                 if(listItemSave != null) {
                 	listItemSave.add(getDataItemForApportionment(itemBean));
                 }
               
                 /*Insertar los Assignment Billing Account*/ 
				 //BUSCAR ERROR
                 System.out.println("Entra al Metodo: grabarItemsBillingAccount(request,conn,itemIdSave,i)");
                 resultTransaction  = grabarItemsBillingAccount(request,conn,itemIdSave,i); 
                 if( resultTransaction!= null)
                    return resultTransaction;
               
                 /*Ingresar los Servicios Adicionales*/  
				 //BUSCAR ERROR
                 System.out.println("Entra al Metodo: grabarItemsServices(request,conn,itemIdSave,i)");
                 resultTransaction  = grabarItemsServices(request,conn,itemIdSave,i); 
                 if( resultTransaction!= null)
                    return resultTransaction; 
                              
                 /* Ingresa las adendas*/                               
                  if(pv_list_adenda != null){
                     resultTransaction  = insertTemplateOrder(request,conn,itemIdSave,i); 
              
                     if( resultTransaction!= null)
                        return resultTransaction;   
                  }
                 /*Ingresar los Imeis*/  
                 /*JPEREZ: Cuando falle la validación de un IMEI/SIM, se debe continuar validando el resto y 
                  * mostrar el mensaje global al final */
				 //BUSCAR ERROR
                 System.out.println("Entra al Metodo: grabarItemsImeis(request,conn,itemIdSave,i)"); 
                 resultTransaction = grabarItemsImeis(request,conn,itemIdSave,i);
                 if( resultTransaction!= null)
                    strImeiMessage = MiUtil.getString(strImeiMessage) + resultTransaction;                                                    
                     
                 //DERAZO REQ-0428 Insertar Items con adenda
                 logger.info("[Insertar item al editar]: FlagPenaltyFunct: "+pn_flag_penalty_func);

                 if(pn_flag_penalty_func == 1) { //Validamos flag universal
                    String[]  pv_item_penalidad = request.getParameterValues("hdnItemValuetxtPagoPenalidad");
                    String[]  pv_item_PriceException_Val = request.getParameterValues("hdnItemValuetxtItemPriceException");
                    String pagoPenalidad = MiUtil.getStringObject(pv_item_penalidad,i);
                    String tipoPrecio = itemBean.getNppricetype();
                    String precioExcepcion = MiUtil.getStringObject(pv_item_PriceException_Val, i);

                    logger.info("[Insertar item al editar]: OrderId: "+itemBean.getNporderid()+" ItemId: "+itemBean.getNpitemid());
                    logger.info("[Insertar item al editar]: PriceType: "+tipoPrecio+" PagoPenalidad: "+pagoPenalidad+" precioExcepcion: "+precioExcepcion);

                    if(pagoPenalidad != null && !pagoPenalidad.equals("")){
                        long  hasPenalty = 2; //No tiene adenda

                        if(pagoPenalidad.equals("S")) {
                            if(precioExcepcion != null && !precioExcepcion.equals("")){
                                //Tiene penalidad porque tiene precio de excepcion
                                hasPenalty = 1;
                            }
                            else{
                                //Tiene adenda, validamos si tendra penalidad o no
                                if(tipoPrecio.equals("ORIGINAL")){
                                    //No tiene penalidad porque esta eligiendo precio de lista
                                    hasPenalty = 0;
                                }
                                else{
                                    //Tiene penalidad porque esta eligiendo una promocion
                                    hasPenalty = 1;
                                }
                            }
                        }

                        logger.info("[Insertar item al crear]: Entra al metodo grabarItemPenalidad");
                        resultTransaction = grabarItemPenalidad(request, conn, itemBean, hasPenalty, "INSERT");

                        if(resultTransaction != null)
                          return resultTransaction;
                    }
                 }
                 //FIN DERAZO
              }
              else if( MiUtil.getStringObject(pv_item_flag_save,i).equals("A") && strChangedOrder.equals("S") ){
                 //if( strChangedOrder.equals("S")){ //S (Se modificó) / N (No se modificó)                 

                  //inicio  [PRY-0710]
                  System.out.println("Inicio sectionItemEvents.actualizarItems : parameters: " + request.getParameter("hdnFlagEditOrdenPort") + " - " + request.getParameter("hdnFlagEditOrdenPort"));
                  if(request.getParameter("hdnFlagEditOrdenPort") != null && request.getParameter("hdnFlagEditOrdenPort").equals("Enabled") ){
                      System.out.println("Inicio sectionItemEvents.actualizarItems - [PRY-0710] - parametro: " + request.getParameter("edititems") + " - orden: " + request.getParameter("hdnNumeroOrder"));
                      System.out.println("sectionItemEvents.actualizarItems - [PRY-0710] - guardamos los cambios");
                      saveChangeItemPortability(request,conn,itemBean,i);
                      System.out.println("FIN sectionItemEvents.actualizarItems - [PRY-0710] - parametro: " + request.getParameter("edititems") + " - orden: " + request.getParameter("hdnNumeroOrder"));
                  }

                   //BUSCAR ERROR
                   System.out.println("Entra al Metodo: grabarItem(request,conn,itemBean,i,UPDATE)");
				   resultTransaction  = grabarItem(request,conn,itemBean,i,"UPDATE");
                   if( resultTransaction != null)
                      return resultTransaction;
                 //}else{
                   //String[]  pv_item_pk = request.getParameterValues("hdnItemId");
                   //itemBean.setNpitemid();
                 //}
                   
                 //PRY-0890 JCURI
                 if(listItemSave != null) {
                	 listItemSave.add(getDataItemForApportionment(itemBean));
                 }
                   
                 //BUSCAR ERROR
                    System.out.println("Entra al Métdo: deleteTemplateOrder(request,conn,String.valueOf(itemBean.getNpitemid()),i)");   

				 resultTransaction  = deleteTemplateOrder(request,conn,String.valueOf(itemBean.getNpitemid()),i);                 
                 if( resultTransaction!= null)
                   return resultTransaction;                
                 String itemIdSave = ""+itemBean.getNpitemid();                
                 /*Actualizar los Assignment Billing Account*/ 
				 //BUSCAR ERROR
				 System.out.println("Entra al Metodo: updateItemsBillingAccount(request,conn,itemIdSave,i)");
                 resultTransaction  = updateItemsBillingAccount(request,conn,itemIdSave,i); 
                 if( resultTransaction!= null)
                    return resultTransaction;
                 
                 /*Actualizar los Servicios Adicionales*/
				 //BUSCAR ERROR
                 System.out.println("Entra al Metodo: updateItemsServiceAditional(request,conn,itemIdSave,i)");
                 resultTransaction  = updateItemsServiceAditional(request,conn,itemIdSave,i); 
                 if( resultTransaction!= null)
                    return resultTransaction;
                                          
                 /*Actualizar Adendas*/
                 if(pv_list_adenda != null){ 
					//BUSCAR ERROR
                    System.out.println("Entra al Metodo: insertTemplateOrder(request,conn,itemIdSave,i)");
                    resultTransaction  = insertTemplateOrder(request,conn,itemIdSave,i);                   
                    if(resultTransaction!= null)
                       return resultTransaction;                  
                 }
                 /*Actualizar los Imeis*/
                 /*JPEREZ: Cuando falle la validación de un IMEI/SIM, se debe continuar validando el resto y 
                  * mostrar el mensaje global al final */
				 //BUSCAR ERROR
                 System.out.println("Entra al Metodo: updateItemsImeis(request,conn,itemIdSave,i)"); 
                 resultTransaction  = updateItemsImeis(request,conn,itemIdSave,i);                  
                 if( resultTransaction!= null)
                    //return resultTransaction;
                    strImeiMessage = MiUtil.getString(strImeiMessage) + " " + resultTransaction;                                     
                 
                 //DERAZO REQ-0428 Insertar Items con adenda
                 logger.info("[Actualizar item al editar]: FlagPenaltyFunct: "+pn_flag_penalty_func);

                 if(pn_flag_penalty_func == 1) { //Validamos flag universal
                    String[]  pv_item_penalidad = request.getParameterValues("hdnItemValuetxtPagoPenalidad");
                    String[]  pv_item_PriceException_Val = request.getParameterValues("hdnItemValuetxtItemPriceException");
                    String pagoPenalidad = MiUtil.getStringObject(pv_item_penalidad,i);
                    String tipoPrecio = itemBean.getNppricetype();
                    String precioExcepcion = MiUtil.getStringObject(pv_item_PriceException_Val, i);

                    logger.info("[Actualizar item al editar]: OrderId: "+itemBean.getNporderid()+" ItemId: "+itemBean.getNpitemid());
                    logger.info("[Actualizar item al editar]: PriceType: "+tipoPrecio+" PagoPenalidad: "+pagoPenalidad+" precioExcepcion: "+precioExcepcion);

                    if(pagoPenalidad != null && !pagoPenalidad.equals("")){
                         long  hasPenalty = 2; //No tiene adenda

                        if(pagoPenalidad.equals("S")) {
                            if(precioExcepcion != null && !precioExcepcion.equals("")){
                                //Tiene penalidad porque tiene precio de excepcion
                                hasPenalty = 1;
                            }
                            else{
                                //Tiene adenda, validamos si tendra penalidad o no
                                if(tipoPrecio.equals("ORIGINAL")){
                                    //No tiene penalidad porque esta eligiendo precio de lista
                                    hasPenalty = 0;
                                }
                                else{
                                    //Tiene penalidad porque esta eligiendo una promocion
                                    hasPenalty = 1;
                                }
                            }
                        }

                         logger.info("[Actualizar item al editar]: Entra al metodo grabarItemPenalidad");
                         resultTransaction = grabarItemPenalidad(request, conn, itemBean, hasPenalty, "UPDATE");

                         if(resultTransaction != null)
                             return resultTransaction;
                    }
                 }
                 //FIN DERAZO
              }
         }//Fin del For   
         if ( !MiUtil.getString(strImeiMessage).equalsIgnoreCase("")){
            logger.info("[Evaluando_strImeiMessage_Diferente_Vacio][strImeiMessage]: " +strImeiMessage);
            return strImeiMessage;
         }   
          
        } // fin del if --> pv_item_cantidad
      
        //Eliminar Items
        if( pv_items_borrados!= null){
		  //BUSCAR ERROR
          logger.info("[Numero_Items_a_Eliminarse]: " + pv_items_borrados);
          System.out.println("Entra al Metodo: deleteItems(pv_items_borrados,pn_order_id,conn)");          
          resultTransaction   =   deleteItems(pv_items_borrados,pn_order_id,conn);
         if( resultTransaction!= null){
           return resultTransaction;
         }    

          //DERAZO REQ-0428 Elimina
          resultTransaction = deleteItemsPenalty(pv_items_borrados,pn_order_id,conn);
          if( resultTransaction!= null){
            return resultTransaction;
          }
        }      
        return null;
    }
    
    public String deleteItems(String pv_items_borrados, String pn_order_id, Connection conn) throws Exception, SQLException{
       StringTokenizer strTokenizer =  new StringTokenizer(pv_items_borrados,"|");
       Vector vctDeleted = new Vector();
                  
       while(strTokenizer.hasMoreElements()){
             String str = (String)strTokenizer.nextElement();
             vctDeleted.addElement(""+str);
       }
            
       //El vector está lleno
       String resultDeleteItem = "";
       //**Eliminar
       
       for( int x=0; x<vctDeleted.size(); x++ ){
            ItemBean itemBean = new ItemBean();
            itemBean.setNporderid(MiUtil.parseLong(pn_order_id));
            itemBean.setNpitemid(MiUtil.parseLong((String)vctDeleted.elementAt(x)));
            //BUSCAR ERROR
            logger.info("[/*Elimina los Items de la orden e item ingresados*/]");
            System.out.println("Entra al Metodo: objItemDAO.getItemOrderDelete(itemBean,conn)");
            resultDeleteItem  = objItemDAO.getItemOrderDelete(itemBean,conn);
              
            if( resultDeleteItem != null ) return resultDeleteItem;  
        }       
      return null;
    }
    
    public String updateItemsBillingAccount(RequestHashMap request, Connection conn, String itemID, int index) throws Exception,SQLException{
          String    strOrderId                    = request.getParameter("hdnNumeroOrder");
          
          ItemBean objItemBean = new ItemBean();
          objItemBean.setNporderid(MiUtil.parseLong(strOrderId));
          objItemBean.setNpitemid(MiUtil.parseLong(itemID));
          logger.info("[Elimino todos los IMEIS asociados al ITEM table: np_ba_assignment]");
          logger.info("[itemBean][OrderId]: " + objItemBean.getNporderid());
          logger.info("[itemBean][ItemId]:  " + objItemBean.getNpitemid());

          String result = objItemDAO.getItemImeiAssignementBADelete(objItemBean,conn);
          if( result!= null ) 
              return result;
          
           return grabarItemsBillingAccount(request,conn,itemID,index);
    }
    
  public String updateItemsServiceAditional(RequestHashMap request, Connection conn, String itemID, int index) throws Exception,SQLException{
    String    strOrderId                    = request.getParameter("hdnNumeroOrder");
    
    ItemBean objItemBean = new ItemBean();
    objItemBean.setNporderid(MiUtil.parseLong(strOrderId));
    objItemBean.setNpitemid(MiUtil.parseLong(itemID));
    logger.info("[Elimina los Servicios Adicionales asociados a un Item]");
    logger.info("[itemBean][OrderId]: " + objItemBean.getNporderid());
    logger.info("[itemBean][itemId]:  " + objItemBean.getNpitemid());
    String result = objItemDAO.delItemServiceAditional(objItemBean,conn);
    if( result!= null ) 
        return result;
    
    return grabarItemsServices(request,conn,itemID,index);
  }
  
  public String updateItemsImeis(RequestHashMap request, Connection conn, String itemID, int index) throws Exception,SQLException{
    String    strOrderId                    = request.getParameter("hdnNumeroOrder");
    System.out.println("=========== Inicio - Actualización ITEM_DEVICE #Orden-> "+strOrderId+" ======= ItemId->"+itemID+" ====");
    ItemBean objItemBean = new ItemBean();
    objItemBean.setNporderid(MiUtil.parseLong(strOrderId));
    objItemBean.setNpitemid(MiUtil.parseLong(itemID));
    String inboxActual = request.getParameter("txtEstadoOrden");
    String validateInbox = objItemDAO.validateInbox(inboxActual);
    String  result = null;
      System.out.println("====== VALIDATE INBOX ======="+inboxActual+"======: "+validateInbox);
      System.out.println("====== Tamño del result: "+validateInbox.length());
    
    if ( validateInbox.length()>1){
        logger.info("[Caso_Prueba_Eliminacion_ItemDevices_Validate_Inbox_Error]"+validateInbox);
        return validateInbox;
    }
    
    if (validateInbox.equals("0")){
    ArrayList itemDeviceListOld = objAutomatizacionDAO.getItemDeviceOrder(conn, objItemBean);
    
    System.out.println("=========== Inicio - Actualización ITEM_DEVICE #Orden-> "+strOrderId+" ====== Se eliminan todos los registros de ITEM_DEVICE  =====");
        result = objItemDAO.delItemImeiDelete(objItemBean,conn);
	
	 if (result != null){
           logger.info("[Caso_Prueba_Eliminacion_ItemDevices_Diferente_Null]"+result);
           if(!result.equals("")){
              logger.info("[Caso_Prueba_Eliminacion_ItemDevices_Diferente_Vacio]");
              return result;
           }else
           logger.info("[Caso_Prueba_Eliminacion_ItemDevices_Cadena Vacio Objeto No nulo]");
     }
    
    System.out.println("=========== Inicio - Actualización ITEM_DEVICE #Orden-> "+strOrderId+" ====== Se vuelven a registrar todos los registros de ITEM_DEVICE  =====");
    result = grabarItemsImeis(request,conn,itemID,index);
    
    ArrayList itemDeviceListNew = objAutomatizacionDAO.getItemDeviceOrder(conn, objItemBean);
    
    //Las listas solo tendrán datos si son de la categorías 2001 o 2002
    sincronizarItemDeviceList(conn, itemDeviceListOld, itemDeviceListNew);
    }
    if(validateInbox.equals("1")){
        System.out.println("=========== Inicio - Actualización ITEM_DEVICE #Orden-> "+strOrderId+" ====== Se vuelven a registrar todos los registros de ITEM_DEVICE  =====");
        result = updateItemsDevice(request,conn,itemID,index);
        if (result != null){
            logger.info("[Caso_Prueba_Actualización_ItemDevices_Error]"+result);
        }
      }
    
    System.out.println("=========== Fin - Actualización ITEM_DEVICE #Orden-> "+strOrderId+" ======= ItemId->"+itemID+" ====");
    return result;
    //return null;
  
  }
  
  /**
   * Method : sincronizarItemDeviceList
   * Purpose: Permite actualizar el campo npitemdeviceid del np_item_error
   * Developer       Fecha       Comentario
   * =============   ==========  ======================================================================
   * <br>Realizado por: <a href="mailto:cesar.lozza@nextel.com.pe">Cesar Lozza</a>
   * <br>Fecha: 12/10/2009    
   * @throws java.sql.SQLException
   * @throws java.lang.Exception
   * @param itemDeviceListNew
   * @param itemDeviceListOld
   * @param conn
   */
  public void sincronizarItemDeviceList(Connection conn, ArrayList itemDeviceListOld, ArrayList itemDeviceListNew)  throws Exception{
     
     logger.info("Sincronizando lista de item device");
     logger.info("itemDeviceListOld.size() " + itemDeviceListOld.size());
     logger.info("itemDeviceListNew.size() " + itemDeviceListNew.size());
     ItemDeviceBean itemDeviceBeanOld = null;
     ItemDeviceBean itemDeviceBeanNew = null;
     String strMessage = null;
  
     for(int i=0; i<itemDeviceListOld.size(); i++){
        itemDeviceBeanOld = (ItemDeviceBean)itemDeviceListOld.get(i);
        
        if(itemDeviceBeanOld.getNpimeinumber() != null && itemDeviceBeanOld.getNpsimnumber() != null
           && !"".equals(itemDeviceBeanOld.getNpimeinumber()) && !"".equals(itemDeviceBeanOld.getNpsimnumber()) ){
          
           for(int j=0; j<itemDeviceListNew.size(); j++){
              itemDeviceBeanNew = (ItemDeviceBean)itemDeviceListNew.get(j);           
              
              if(itemDeviceBeanNew.getNpimeinumber() != null && itemDeviceBeanNew.getNpsimnumber() != null
                 && !"".equals(itemDeviceBeanNew.getNpimeinumber()) && !"".equals(itemDeviceBeanNew.getNpsimnumber()) ){
              
                 if(itemDeviceBeanOld.getNpimeinumber().equals(itemDeviceBeanNew.getNpimeinumber())
                    && itemDeviceBeanOld.getNpsimnumber().equals(itemDeviceBeanNew.getNpsimnumber())){
                    strMessage = objAutomatizacionDAO.updateItemError(conn, itemDeviceBeanOld.getNporderid(), itemDeviceBeanOld.getNpitemid(), itemDeviceBeanOld.getNpitemdeviceid(), itemDeviceBeanNew.getNpitemdeviceid());
                    if(strMessage != null){
                       logger.info("sincronizarItemDeviceList" + strMessage);
                    }
                    continue;
                 }              
              }
           }        
        }
        

     }  
  }
  
    public String grabarItem(RequestHashMap request, Connection conn, ItemBean itemBean, int i, String operatorTransaction)  throws Exception, SQLException{
      
      String    pn_order_id                   = request.getParameter("hdnNumeroOrder");
      String    pv_session_login              = request.getParameter("hdnSessionLogin");
      long      lSpecificationId              = MiUtil.parseLong(request.getParameter("hdnSpecification"));
      
      if( operatorTransaction.equals("INSERT") ){
        System.out.println("=========== Inicio - Registro del ITEM #Orden-> "+pn_order_id+" Usuario-> "+pv_session_login+"===========");
      }else{
        System.out.println("=========== Inicio - Actualización del ITEM #Orden-> "+pn_order_id+" Usuario-> "+pv_session_login+"===========");
      }
      String[]  pv_item_pk                      = request.getParameterValues("hdnItemId");
      String[]  pv_item_modality                = request.getParameterValues("txtItemModality");
      String[]  pv_item_modality_Val            = request.getParameterValues("hdnItemValuetxtItemModality");
      
      String[]  pv_item_solution                = request.getParameterValues("txtItemSolution");
      String[]  pv_item_solution_Val            = request.getParameterValues("hdnItemValuetxtItemSolution");
      
      //CBARZOLA:CAMBIO DE MODELO DISTINTAS TECNOLOGIAS 
      String[]  pv_item_originalsolution        = request.getParameterValues("txtItemOriginalSolution");
      String[]  pv_item_originalsolution_Val    = request.getParameterValues("hdnItemValuetxtItemOriginalSolution");
      
      String[]  pv_item_originalsolutionid        = request.getParameterValues("txtItemOriginalSolutionId");
      String[]  pv_item_originalsolutionid_Val    = request.getParameterValues("hdnItemValuetxtItemOriginalSolutionId");

      String[]  pv_item_generic                 = request.getParameterValues("txtItemGenericSIMIMEI");
      String[]  pv_item_generic_Val             = request.getParameterValues("hdnItemValuetxtItemGenericSIMIMEI");
      
      String[]  pv_item_model                   = request.getParameterValues("txtItemModel");
      String[]  pv_item_model_Val               = request.getParameterValues("hdnItemValuetxtItemModel");
      
      String[]  pv_item_total                   = request.getParameterValues("txtitemTotal");
      String[]  pv_item_total_Val               = request.getParameterValues("hdnItemValuetxtitemTotal");
      
      String[]  pv_item_Phone                   = request.getParameterValues("txtItemPhone");
      String[]  pv_item_Phone_Val               = request.getParameterValues("hdnItemValuetxtItemPhone");
      
      String[]  pv_item_NewPhone                = request.getParameterValues("txtItemNewNumber");
      String[]  pv_item_NewPhone_Val            = request.getParameterValues("hdnItemValuetxtItemNewNumber");
      
      String[]  pv_item_Sim                     = request.getParameterValues("txtItemSIM");
      String[]  pv_item_Sim_Val                 = request.getParameterValues("hdnItemValuetxtItemSIM");
      
      String[]  pv_item_Imei                    = request.getParameterValues("txtItemIMEI");
      String[]  pv_item_Imei_Val                = request.getParameterValues("hdnItemValuetxtItemIMEI");
      
      String[]  pv_item_Linea                   = request.getParameterValues("txtItemLine");
      String[]  pv_item_Linea_Val               = request.getParameterValues("hdnItemValuetxtItemLine");
      
      String[]  pv_item_Product                 = request.getParameterValues("txtItemProduct");
      String[]  pv_item_Product_Val             = request.getParameterValues("hdnItemValuetxtItemProduct");
      
      String[]  pv_item_Product_Line            = request.getParameterValues("txtItemProductLine");
      String[]  pv_item_Product_Line_Val        = request.getParameterValues("hdnItemValuetxtItemProductLine");    
      
      String[]  pv_item_PlanTarifario           = request.getParameterValues("txtItemRatePlan");
      String[]  pv_item_PlanTarifario_Val       = request.getParameterValues("hdnItemValuetxtItemRatePlan");
      
      String[]  pv_item_Equipment               = request.getParameterValues("txtItemEquipment");
      String[]  pv_item_Equipment_Val           = request.getParameterValues("hdnItemValuetxtItemEquipment");
      
      String[]  pv_item_Devolution              = request.getParameterValues("txtItemDevolucionFlag");
      String[]  pv_item_Devolution_Val          = request.getParameterValues("hdnItemValuetxtItemDevolucionFlag");
      
      String[]  pv_item_PendDevolution          = request.getParameterValues("txtItemPendDevolutionFlag");
      String[]  pv_item_PendDevolution_Val      = request.getParameterValues("hdnItemValuetxtItemPendDevolutionFlag");
      
      String[]  pv_item_FecDevolution           = request.getParameterValues("txtItemPendDevolutionDate");
      String[]  pv_item_FecDevolution_Val       = request.getParameterValues("hdnItemValuetxtItemPendDevolutionDate");
      
      String[]  pv_item_NewProduct              = request.getParameterValues("txtItemNewProduct");
      String[]  pv_item_NewProduct_Val          = request.getParameterValues("hdnItemValuetxtItemNewProduct");
      
      String[]  pv_item_NewPlanTarifarioId      = request.getParameterValues("txtItemNewRatePlanId");
      String[]  pv_item_NewPlanTarifarioId_Val  = request.getParameterValues("hdnItemValuetxtItemNewRatePlanId");
      
      String[]  pv_item_NewPlanTarifario        = request.getParameterValues("txtItemNewRatePlan");
      String[]  pv_item_NewPlanTarifario_Val    = request.getParameterValues("hdnItemValuetxtItemNewRatePlan");
      
      String[]  pv_item_NewPhoneNumber          = request.getParameterValues("txtItemNewPhoneNumber");
      String[]  pv_item_NewPhoneNumber_Val      = request.getParameterValues("hdnItemValuetxtItemNewPhoneNumber");
      
      String[]  pv_item_PriceCtaInscrip         = request.getParameterValues("txtItemPriceCtaInscrip");
      String[]  pv_item_PriceCtaInscrip_Val     = request.getParameterValues("hdnItemValuetxtItemPriceCtaInscrip");
      
      String[]  pv_item_PriceException          = request.getParameterValues("txtItemPriceException");
      String[]  pv_item_PriceException_Val      = request.getParameterValues("hdnItemValuetxtItemPriceException");
      
      String[]  pv_item_EquipmentRent           = request.getParameterValues("txtItemRentEquipment");
      String[]  pv_item_EquipmentRent_Val       = request.getParameterValues("hdnItemValuetxtItemRentEquipment");
      
      String[]  pv_item_Currency                = request.getParameterValues("txtItemMoneda");
      String[]  pv_item_Currency_Val            = request.getParameterValues("hdnItemValuetxtItemMoneda");
      
      String[]  pv_item_Warrant                 = request.getParameterValues("txtItemFlgProductGN");
      String[]  pv_item_Warrant_Val             = request.getParameterValues("hdnItemValuetxtItemFlgProductGN");
      
      String[]  pv_item_Ocurrence               = request.getParameterValues("txtItemNroOcurrence");
      String[]  pv_item_Ocurrence_Val           = request.getParameterValues("hdnItemValuetxtItemNroOcurrence");

      String[]  pv_item_Quantity                = request.getParameterValues("txtItemQuantity");
      String[]  pv_item_Quantity_Val            = request.getParameterValues("hdnItemValuetxtItemQuantity");
      
      String[]  pv_item_Promocion               = request.getParameterValues("txtItemPromotioId");
      String[]  pv_item_Promocion_Val           = request.getParameterValues("hdnItemValuetxtItemPromotioId");
      
      String[]  pv_item_Adenda                  = request.getParameterValues("txtItemAdenda");
      String[]  pv_item_Adenda_Val              = request.getParameterValues("hdnItemValuetxtItemAdenda");
      
      String[]  pv_item_services                = request.getParameterValues("item_services");
      String[]  pv_item_services_Val            = request.getParameterValues("hdnItemValueitem_services");
      
      String[]  pv_item_ResponsibleArea         = request.getParameterValues("txtItemResponsibleArea");
      String[]  pv_item_ResponsibleArea_Val     = request.getParameterValues("hdnItemValuetxtItemResponsibleArea");
      
      String[]  pv_item_ResponsibleDev          = request.getParameterValues("txtItemResponsibleDev");
      String[]  pv_item_ResponsibleDev_Val      = request.getParameterValues("hdnItemValuetxtItemResponsibleDev");
      
      
      /**Acceso Internet - Enlace de Datos**/   
      
      //Contrato a asociar
      String[]  pv_item_sharedinstal              = request.getParameterValues("txtItemContractServ");
      String[]  pv_item_sharedinstal_Val          = request.getParameterValues("hdnItemValuetxtItemContractServ");

      //Nro de Contrato
      String[]  pv_item_contractnumber            = request.getParameterValues("txtItemContractNumber");
      String[]  pv_item_contractnumber_Val        = request.getParameterValues("hdnItemValuetxtItemContractNumber");

      //Nro instalación compartida
      String[]  pv_item_firstcontract             = request.getParameterValues("txtItemSharedInstallNumber");
      String[]  pv_item_firstcontract_Val         = request.getParameterValues("hdnItemValuetxtItemSharedInstallNumber");

      //Instalación compartida
      String[]  pv_item_SharedInstall             = request.getParameterValues("txtItemSharedInstall");
      String[]  pv_item_SharedInstall_Val         = request.getParameterValues("hdnItemValuetxtItemSharedInstall");
      
      //Direción de Instalación 
      String[]  pv_item_AddressInstall            = request.getParameterValues("txtItemAddressInstall");
      String[]  pv_item_AddressInstall_Val        = request.getParameterValues("hdnItemValuetxtItemAddressInstall");

      //Cargo de Instalación/Traslado 
      String[]  pv_item_instalationprice          = request.getParameterValues("txtItemInstallCargo");
      String[]  pv_item_instalationprice_Val      = request.getParameterValues("hdnItemValuetxtItemInstallCargo");
      
      //Cargo de Instalación/Traslado de Excepción
      String[]  pv_item_instalationexception      = request.getParameterValues("txtItemExcepInstallCargo");
      String[]  pv_item_instalationexception_Val  = request.getParameterValues("hdnItemValuetxtItemExcepInstallCargo");

      //Tipo de Enlace
      String[]  pv_item_linktype                  = request.getParameterValues("txtItemLinkType");
      String[]  pv_item_linktype_Val              = request.getParameterValues("hdnItemValuetxtItemLinkType");
      
      //Tipo Nodo
      String[]  pv_item_networkhosttype           = request.getParameterValues("txtItemNodeType");
      String[]  pv_item_networkhosttype_Val       = request.getParameterValues("hdnItemValuetxtItemNodeType");
      
      //Fecha de Factibilidad
      String[]  pv_item_feasibilityprogdate       = request.getParameterValues("txtItemFactbDate");
      String[]  pv_item_feasibilityprogdate_Val   = request.getParameterValues("hdnItemValuetxtItemFactbDate");
      
      //Factibilidad
      String[]  pv_item_feasibility               = request.getParameterValues("txtItemFactb");
      String[]  pv_item_feasibility_Val           = request.getParameterValues("hdnItemValuetxtItemFactb");

      //Instalación
      String[]  pv_item_instalation               = request.getParameterValues("txtItemInstall");
      String[]  pv_item_instalation_Val           = request.getParameterValues("hdnItemValuetxtItemInstall");

      //Fecha de Instalación
      String[]  pv_item_instalationprogdate       = request.getParameterValues("txtItemInstallDate");
      String[]  pv_item_instalationprogdate_Val   = request.getParameterValues("hdnItemValuetxtItemInstallDate");
      
      /**Acceso Internet - Enlace de Datos**/   
      
      /**Para Traslado**/
      
      //Nueva Dirección
      String[]  pv_item_destinyaddress            = request.getParameterValues("txtItemDestinyAddress");
      String[]  pv_item_destinyaddress_Val        = request.getParameterValues("hdnItemValuetxtItemDestinyAddress");
      
      //Nombre del Contacto
      String[]  pv_item_contactname               = request.getParameterValues("txtItemContact");
      String[]  pv_item_contactname_Val           = request.getParameterValues("hdnItemValuetxtItemContact");
      
      //Teléfono Principal
      String[]  pv_item_MainPhone                 = request.getParameterValues("txtItemMainPhone");
      String[]  pv_item_MainPhone_Val             = request.getParameterValues("hdnItemValuetxtItemMainPhone");
      
      //Teléfono Auxiliar
      String[]  pv_item_AuxPhone                  = request.getParameterValues("txtItemAuxPhone");
      String[]  pv_item_AuxPhone_Val              = request.getParameterValues("hdnItemValuetxtItemAuxPhone");
      
      //Costo Adicional
      String[]  pv_item_AdditionalCost            = request.getParameterValues("txtItemAdditionalCost");
      String[]  pv_item_AdditionalCost_Val        = request.getParameterValues("hdnItemValuetxtItemAdditionalCost");
      
      //Descripción
      String[]  pv_item_Description               = request.getParameterValues("txtItemDescription");
      String[]  pv_item_Description_Val           = request.getParameterValues("hdnItemValuetxtItemDescription");
      
      /**Fin Traslado**/
      
      /**Inicio - Cambio de Plan Tarifario**/   
      
      //Original Principal Servicio 
      String[]  pv_item_OrigMainService           = request.getParameterValues("txtItemOrigMainServiceId");
      String[]  pv_item_OrigMainService_Val       = request.getParameterValues("hdnItemValuetxtItemOrigMainServiceId");
      
      //Nuevo Principal Servicio
      String[]  pv_item_NewMainService           = request.getParameterValues("txtItemNewMainService");
      String[]  pv_item_NewMainService_Val       = request.getParameterValues("hdnItemValuetxtItemNewMainService");
      
      /**Fin - Cambio de Plan Tarifario**/   
      
      String[]  pv_item_original_productid_Val    = request.getParameterValues("txtItemProductBolsaId");
      
      
      String[]  pv_item_MntsRates                 = request.getParameterValues("txtItemMntsRates");
      String[]  pv_item_MntsRates_Val             = request.getParameterValues("hdnItemValuetxtItemMntsRates");
      
      /**Inicio - Servicio de locución**/ 
      String[]  pv_item_EndDateServices           = request.getParameterValues("txtItemEndDateService");
      String[]  pv_item_EndDateServices_Val       = request.getParameterValues("hdnItemValuetxtItemEndDateService");
      
      String[]  pv_item_ReferencePhone            = request.getParameterValues("txtItemPhoneReference");
      String[]  pv_item_ReferencePhone_Val        = request.getParameterValues("hdnItemValuetxtItemPhoneReference");
      /**Fin    - Servicio de locución**/ 
      
      String[]  pv_item_flag_save                 = request.getParameterValues("hdnFlagSave");
      
      /* JPEREZ: EXCEPCIONES - INICIO */
      String[] pv_item_exception                  = request.getParameterValues("txtItemException");
      String[] pv_item_exception_revenue          = request.getParameterValues("txtItemExceptionRevenue");
      String[] pv_item_exception_revenue_disc     = request.getParameterValues("txtItemExceptionRevenueDisc");
      String[] pv_item_exception_rent             = request.getParameterValues("txtItemExceptionRent");
      String[] pv_item_exception_rent_disc        = request.getParameterValues("txtItemExceptionRentDisc");
      String[] pv_item_exception_min_ad_cd        = request.getParameterValues("txtItemExceptionMinAdiCD");
      String[] pv_item_exception_min_ad_it        = request.getParameterValues("txtItemExceptionMinAdiIT");
      String[] pv_item_exception_move             = request.getParameterValues("txtItemExceptionMove");
      /* JPEREZ: EXCEPCIONES - FIN */
      
      /*Inicio Acuerdos Comerciales*/
      String[] pv_item_original_price              = request.getParameterValues("txtItemOriginalPrice");
      String[] pv_item_price_type                  = request.getParameterValues("txtItemPriceType");
      String[] pv_item_price_typeId                = request.getParameterValues("txtItemPriceTypeId");
      String[] pv_item_price_type_itemId           = request.getParameterValues("txtItemPriceTypeItemId");
      /*Fin Acuerdos Comerciales*/
      
      String[] pv_item_fixed_phone                 = request.getParameterValues("txtItemFixedPhone");
      String[] pv_item_fixed_phone_Val             = request.getParameterValues("hdnItemValuetxtItemFixedPhone");
      
      String[] pv_item_locution                 = request.getParameterValues("txtItemLocution");
      String[] pv_item_locution_Val             = request.getParameterValues("hdnItemValuetxtItemLocution");

      /*Inicio Responsable de Pago*/
      String[]  pv_item_resp_pago                = request.getParameterValues("txtItemRespPago");
      String[]  pv_item_resp_pago_Val            = request.getParameterValues("hdnItemValuetxtItemRespPago");
      /*Fin Responsable de Pago*/
      
      /*Inicio Contratos de Referencia*/
      String[]  pv_item_num_existente_tf         = request.getParameterValues("txtItemNumeroExistenteTF");
      String[]  pv_item_contr_existente_int      = request.getParameterValues("txtItemContratoInternet");
      String[]  pv_item_contr_existente_datos    = request.getParameterValues("txtItemContratoDatos");
      String[]  pv_item_nodo_principal           = request.getParameterValues("txtItemNodoPrincipal");
      /*Fin Contratos de Referencia*/
      
      /*Suspención Definitiva*/
      String[]  pv_estado_item                     = request.getParameterValues("txtItemEstado");
      String[]  pv_estado_item_Val                 = request.getParameterValues("hdnItemValuetxtItemEstado");
      String[] pv_estado_proceso                   = request.getParameterValues("txtEstadoOperacion");
      /*Fin Suspención definitiva*/
      
      /*Suspención Temporal*/
      String[] pv_cobro                            = request.getParameterValues("txtItemCobro");      
      /*Fin Suspención Temporal*/
      
      //Descripción
      String[]  pv_item_ip                        = request.getParameterValues("txtTipoIP");
      String[]  pv_item_ip_Val                    = request.getParameterValues("hdnItemValuetxtTipoIP");

      // CPUENTE CAP & CAL - INICIO
      String[] pv_item_equipment_time_int     = request.getParameterValues("txtItemTiempoMeses");
      String[] pv_item_own_equipment_int     = request.getParameterValues("txtItemEquipoPropio");
      String[] pv_item_reason_change         = request.getParameterValues("hdnItemValuetxtItemMotivoReemplazo");
      // CPUENTE CAP & CAL - FIN 
      
      //PHIDALGO
      String[] pv_item_level         = request.getParameterValues("hdnItemValuetxtItemNivel");
      
      String[]  pv_item_IMEI_cliente               = request.getParameterValues("txtItemIMEICliente");
      String[]  pv_item_IMEI_cliente_Val           = request.getParameterValues("hdnItemValuetxtItemIMEICliente");

      String[]  pv_item_imei_sim               = request.getParameterValues("txtItemImeiSim");
      String[]  pv_item_imei_sim_Val           = request.getParameterValues("hdnItemValuetxtItemImeiSim");
     
     //DLAZO - SUSCRIPCIONES - INICIO
      String[] pv_item_activation_date            = request.getParameterValues("txtItemActivationDate");
      String[] pv_item_activation_date_Val        = request.getParameterValues("hdnItemValuetxtItemActivationDate");
      String[] pv_item_deactivacion_date          = request.getParameterValues("txtItemDeactivationDate");
      String[] pv_item_deactivation_date_Val      = request.getParameterValues("hdnItemValuetxtItemDeactivationDate");
      //DLAZO - SUSCRIPCIONES - FIN
      
      String[] pv_item_price_venta_vep            = request.getParameterValues("txtItemPriceVentaVEP");
      String[] pv_item_price_venta_vep_Val        = request.getParameterValues("hdnItemValuetxtItemPriceVentaVEP");
      String[] pv_item_venta_vep                  = request.getParameterValues("txtItemVentaVEP");
      String[] pv_item_venta_vep_Val              = request.getParameterValues("hdnItemValuetxtItemVentaVEP");
      
      // MMONTOYA - Despacho en tienda
      String[]  pv_item_ProductStatus             = request.getParameterValues("txtItemProductStatus");
      String[]  pv_item_ProductStatus_Val         = request.getParameterValues("hdnItemValuetxtItemProductStatus");
      String[]  pv_item_ItemChanged               = request.getParameterValues("txtItemChanged");
      String[]  pv_item_ItemChanged_Val           = request.getParameterValues("hdnItemValuetxtItemChanged");                  
      String[]  pv_item_ItemFlagAccessory         = request.getParameterValues("txtItemFlagAccessory");
      String[]  pv_item_ItemFlagAccessory_Val     = request.getParameterValues("hdnItemValuetxtItemFlagAccessory");
      
      // MMONTOYA [ADT-RCT-092 Roaming con corte]
      String[]  pv_item_ServPlanType                = request.getParameterValues("txtItemServPlanType");
      String[]  pv_item_ServPlanType_Val            = request.getParameterValues("hdnItemValuetxtItemServPlanType");
      String[]  pv_item_ServBagCode                 = request.getParameterValues("txtItemServBagCode");
      String[]  pv_item_ServBagCode_Val             = request.getParameterValues("hdnItemValuetxtItemServBagCode");
      String[]  pv_item_ServBagType                 = request.getParameterValues("txtItemServBagType");
      String[]  pv_item_ServBagType_Val             = request.getParameterValues("hdnItemValuetxtItemServBagType");
      String[]  pv_item_ServValidActivationDate     = request.getParameterValues("txtItemServValidActivationDate");
      String[]  pv_item_ServValidActivationDate_Val = request.getParameterValues("hdnItemValuetxtItemServValidActivationDate");
      String[]  pv_item_ServValidity                = request.getParameterValues("txtItemServValidity");
      String[]  pv_item_ServValidity_Val            = request.getParameterValues("hdnItemValuetxtItemServValidity");

      //PRY-0762 JQUISPE Se obtiene la cantidad Renta Adelantada
      String[]  pv_item_CantidadRentaAdelantada      = request.getParameterValues("txtCantidadRA");
      String[]  pv_item_CantidadRentaAdelantada_Val  = request.getParameterValues("hdnItemValuetxtCantidadRA");
      
      //PRY-0762 JQUISPE Se obtiene el total Renta Adelantada
      String[]  pv_item_TotalRentaAdelantada      = request.getParameterValues("txtTotalRA");
      String[]  pv_item_TotalRentaAdelantada_Val  = request.getParameterValues("hdnItemValuetxtTotalRA");

      //CDM+CDP EFLORES SE obtiene el campo Mantener SIM PRY-0817
      String[]  pv_item_KeepSIM = request.getParameterValues(Constante.NPOBJHTMLNAME_KEEP_SIM);
        String[]  pv_item_KeepSIM_Val = request.getParameterValues(Constante.NPOBJHTMLNAME_KEEP_SIM_VAL);
      //PRY-0721 DERAZO Se obtiene la Region del Item
      String[]  pv_item_region                      = request.getParameterValues("txtItemRegion");
      String[]  pv_item_region_Val                  = request.getParameterValues("hdnItemValuetxtItemRegion");
      
      //EFLORES BAFI2
      String[] pv_item_NpProvinceZoneId     =  request.getParameterValues("txtNpProvinceZone");
      String[] pv_item_NpProvinceZoneId_Val =  request.getParameterValues("hdnItemValuetxtNpProvinceZone");
      String[] pv_item_NpDistrictZoneId     =  request.getParameterValues("txtNpDistrictZone");
      String[] pv_item_NpDistrictZoneId_Val =  request.getParameterValues("hdnItemValuetxtNpDistrictZone");    

      
      //PRY-0890 JBALCAZAR
      String[]  pv_item_NpProrrateoPrice      = request.getParameterValues("txtMontoProrrateo");
      String[]  pv_item_NpProrrateoPrice_Val  = request.getParameterValues("hdnItemValuetxtMontoProrrateo");
      String[]  pv_item_IndiceItemId  = request.getParameterValues("hdnIndice");
      
      //TDECONV003-2 DERAZO Se obtiene el IMEI FullStack
      String[]  pv_item_ownImeiFS             = request.getParameterValues("txtItemImeiFS");
      String[]  pv_item_ownImeiFS_Val         = request.getParameterValues("hdnItemValuetxtItemImeiFS");

         //Numero Ordenc
         itemBean.setNporderid(MiUtil.parseLong(pn_order_id));
         //Modalidad Salida
         itemBean.setNpmodalitysell(MiUtil.getStringObject(pv_item_modality_Val,i));
         //Solución
         itemBean.setNpsolutionid(MiUtil.parseLong(MiUtil.getStringObject(pv_item_solution_Val,i)));
        //CBARZOLA:Solución Original
          itemBean.setNporiginalsolutionid(MiUtil.parseLong(MiUtil.getStringObject(pv_item_originalsolutionid_Val,i))); 
          itemBean.setNporiginalsolutionname(MiUtil.getStringObject(pv_item_originalsolution_Val,i));
         //Numero del Teléfono
         itemBean.setNpphone(MiUtil.getStringObject(pv_item_Phone_Val,i));
         //Numero del Nuevo Teléfono
         itemBean.setNpnewphone(MiUtil.getStringObject(pv_item_NewPhone_Val,i));
         //SIM Generico
         itemBean.setNpownimeinumber(MiUtil.getStringObject(pv_item_generic_Val,i));
         //Numero Serial
         itemBean.setNpserialnumber(MiUtil.getStringObject(pv_item_Sim_Val,i));
         //Numero Imei
         itemBean.setNpimeinumber(MiUtil.getStringObject(pv_item_Imei_Val,i));
         //Numero Sim
         itemBean.setNpsiminumber(MiUtil.getStringObject(pv_item_Sim_Val,i));
         //Modelo
         itemBean.setNpmodel(MiUtil.getStringObject(pv_item_model_Val,i));
         //Plan Tarifario Nuevo
         itemBean.setNpplanid(MiUtil.parseLong(MiUtil.getStringObject(pv_item_PlanTarifario_Val,i)));
         //Nombre Plan Tarifario Nuevo        
         
         itemBean.setNpplanname(MiUtil.getStringObject(pv_item_PlanTarifario,i).equals("null")?"":MiUtil.getStringObject(pv_item_PlanTarifario,i));
         
         
         //Linea Producto
         itemBean.setNpproductlineid(MiUtil.parseLong(MiUtil.getStringObject(pv_item_Product_Line_Val,i)));
         //Product ID
         itemBean.setNpproductid(MiUtil.parseLong(MiUtil.getStringObject(pv_item_Product_Val,i)));
         //Cantidad
         itemBean.setNpquantity(MiUtil.parseInt(MiUtil.getStringObject(pv_item_Quantity_Val,i).equals("")?"1":MiUtil.getStringObject(pv_item_Quantity_Val,i)));
         //Precio de Cta Inscripción
         itemBean.setNpprice(MiUtil.getString(MiUtil.getStringObject(pv_item_PriceCtaInscrip_Val,i)));
         //Precio Excepción
         itemBean.setNppriceexception(MiUtil.getString(MiUtil.getStringObject(pv_item_PriceException_Val,i)));
         //Precio Renta
         itemBean.setNprent(MiUtil.getString(MiUtil.getStringObject(pv_item_EquipmentRent_Val,i)));
         //Precio Descuento
         itemBean.setNpdiscount(MiUtil.getString(MiUtil.getStringObject(pv_item_EquipmentRent_Val,i)));
         //Tipo Moneda
         itemBean.setNpcurrency(MiUtil.getStringObject(pv_item_Currency_Val,i));
         //Responsable Area
         itemBean.setNparearespdev(MiUtil.parseLong(MiUtil.getStringObject(pv_item_ResponsibleArea_Val,i)));
         //Responsable Devolución
         itemBean.setNpprovidergrpiddev(MiUtil.parseLong(MiUtil.getStringObject(pv_item_ResponsibleDev_Val,i)));
         //Garantia
         itemBean.setNpwarrant(MiUtil.getStringObject(pv_item_Warrant_Val,i));
         itemBean.setNpoccurrence(MiUtil.parseInt(MiUtil.getStringObject(pv_item_Ocurrence_Val,i)));
         itemBean.setNppromotionid(MiUtil.parseLong(MiUtil.getStringObject(pv_item_Promocion_Val,i)));
         itemBean.setNpaddendumid(MiUtil.parseLong(MiUtil.getStringObject(pv_item_Adenda_Val,i)));
         //itemBean.setNpinventorycode(MiUtil.getStringObject(pv_item_Phone_Val,i));
         itemBean.setNpequipment(MiUtil.getStringObject(pv_item_Equipment,i));
         
         //Plan Tarifario Original
         itemBean.setNporiginalplanid(MiUtil.parseInt(MiUtil.getStringObject(pv_item_NewPlanTarifarioId_Val,i)));
         itemBean.setNporiginalplanname(MiUtil.getStringObject(pv_item_NewPlanTarifario,i));
         
         //Producto Original
         itemBean.setNporiginalproductid(MiUtil.parseLong(MiUtil.getStringObject(pv_item_original_productid_Val,i)));
         
         itemBean.setNpequipmentreturn(MiUtil.getStringObject(pv_item_Devolution_Val,i));
         itemBean.setNpequipmentnotyetgiveback(MiUtil.getStringObject(pv_item_PendDevolution_Val,i));
         itemBean.setNpequipmentreturndate(MiUtil.toFecha(MiUtil.getStringObject(pv_item_FecDevolution,i),"dd/MM/yyyy"));         
         itemBean.setNpexception(MiUtil.getStringObject(pv_item_exception,i));         
         itemBean.setNpexceptionrevenue(MiUtil.getStringObject(pv_item_exception_revenue,i));
         itemBean.setNpexceptionrevenuediscount(MiUtil.parseDouble(MiUtil.getStringObject(pv_item_exception_revenue_disc,i)));
         itemBean.setNpexceptionrent(MiUtil.getStringObject(pv_item_exception_rent,i));
         itemBean.setNpexceptionrentdiscount(MiUtil.parseDouble(MiUtil.getStringObject(pv_item_exception_rent_disc,i)));
         
         if (MiUtil.getStringObject(pv_item_exception_min_ad_cd,i).compareTo("Y")==0)
          itemBean.setNpexceptionminadidispatch("S");
         else 
          itemBean.setNpexceptionminadidispatch("");
         //itemBean.setNpexceptionminadidispatch(MiUtil.getStringObject(pv_item_exception_min_ad_cd,i));                                  
         if (MiUtil.getStringObject(pv_item_exception_min_ad_it,i).compareTo("Y")==0)
          itemBean.setNpexceptionminaditelephony("S");
         else 
          itemBean.setNpexceptionminaditelephony("");
                   
        itemBean.setNpinstalationexception(MiUtil.getStringObject(pv_item_instalationexception_Val,i));  
         
         if ( (!itemBean.getNpexceptionrevenue().equals("")) || (!itemBean.getNpexceptionrent().equals("")) || 
              (!itemBean.getNpexceptionminadidispatch().equals("")) || (!itemBean.getNpexceptionminaditelephony().equals(""))
              ) 
                itemBean.setNpexception("S");
         else 
                itemBean.setNpexception("");
                
         itemBean.setNpmodificationdate(MiUtil.getDateBD("dd/MM/yyyy"));
         itemBean.setNpmodificationby(pv_session_login);
         itemBean.setNpcreateddate(MiUtil.getDateBD("dd/MM/yyyy"));
         itemBean.setNpcreatedby(pv_session_login);
         
         /**Banda Ancha**/
         
         /**(Inicio) Acceso de Datos/Enlace de Datos**/
         itemBean.setNpsharedinstal(MiUtil.getStringObject(pv_item_sharedinstal_Val,i));
         itemBean.setNpcontractnumber(MiUtil.parseInt(MiUtil.getStringObject(pv_item_contractnumber_Val,i)));
         itemBean.setNpfirstcontract(MiUtil.getStringObject(pv_item_firstcontract_Val,i));
         //Instalación Compartida
         itemBean.setNpsharedinstalationid(MiUtil.parseLong(MiUtil.getStringObject(pv_item_SharedInstall_Val,i)));
         //Dirección de Instalación
         itemBean.setNpinstallationaddressid(MiUtil.parseLong(MiUtil.getStringObject(pv_item_AddressInstall_Val,i)));
         
         itemBean.setNpinstalationprice(MiUtil.getString(MiUtil.getStringObject(pv_item_instalationprice_Val,i)));
         itemBean.setNpinstalationexception(MiUtil.getString(MiUtil.getStringObject(pv_item_instalationexception_Val,i)));
         
         itemBean.setNplinktype(MiUtil.getStringObject(pv_item_linktype_Val,i));
         itemBean.setNpnetworkhosttype(MiUtil.parseInt(MiUtil.getStringObject(pv_item_networkhosttype_Val,i)));
         if(MiUtil.getStringObject(pv_item_feasibilityprogdate_Val,i)!=null){
            if(!MiUtil.getStringObject(pv_item_feasibilityprogdate_Val,i).equals("")){
               itemBean.setNpfeasibilityprogdate(MiUtil.toFechaHora(MiUtil.getStringObject(pv_item_feasibilityprogdate_Val,i),"dd/MM/yyyy HH:mm"));                 
            }
         }           
         itemBean.setNpfeasibility(MiUtil.getStringObject(pv_item_feasibility_Val,i));
         itemBean.setNpinstalation(MiUtil.getStringObject(pv_item_instalation_Val,i));
         
         if(MiUtil.getStringObject(pv_item_instalationprogdate_Val,i)!=null){
            if(!MiUtil.getStringObject(pv_item_instalationprogdate_Val,i).equals("")){
               itemBean.setNpinstalationprogdate(MiUtil.toFechaHora(MiUtil.getStringObject(pv_item_instalationprogdate_Val,i),"dd/MM/yyyy HH:mm"));                 
            }
         }   
         
         
         /**(Fin) Acceso de Datos/Enlace de Datos**/
         
         /**(Inicio) Traslado**/
         itemBean.setNpnewaddress(MiUtil.parseLong(MiUtil.getStringObject(pv_item_destinyaddress_Val,i)));
         itemBean.setNpcontactname(MiUtil.getStringObject(pv_item_contactname_Val,i));
         itemBean.setNpphonenumber1(MiUtil.getStringObject(pv_item_MainPhone_Val,i));
         itemBean.setNpphonenumber2(MiUtil.getStringObject(pv_item_AuxPhone_Val,i));
         itemBean.setNpaditionalcost(MiUtil.getString(MiUtil.getStringObject(pv_item_AdditionalCost_Val,i)));
         itemBean.setNpdescription(MiUtil.getStringObject(pv_item_Description_Val,i));
         /**(Fin) Traslado**/
         
         
         /**(Inicio) Cambio de Plan Tarifario**/
         itemBean.setNporigmainservice(MiUtil.parseLong(MiUtil.getStringObject(pv_item_OrigMainService_Val,i)));
         itemBean.setNpnewmainservice(MiUtil.parseLong(MiUtil.getStringObject(pv_item_NewMainService_Val,i)));
         /**(Fin) Cambio de Plan Tarifario**/
         
         /**Banda Ancha**/
         
         //Bolsa 
         itemBean.setNpminutesrate(MiUtil.getStringObject(pv_item_MntsRates_Val,i));
         
         
         /**Inicio - Servicio de locución**/ 
         itemBean.setNpendservicedate(MiUtil.toFecha(MiUtil.getStringObject(pv_item_EndDateServices_Val,i),"dd/MM/yyyy"));
         itemBean.setNpreferencephone(MiUtil.getStringObject(pv_item_ReferencePhone_Val,i));
         /**Fin    - Servicio de locución**/
         
         /**Inicio Acuerdos Comerciales**/ 
         
         /**Suspención Definitiva**/
        // Se valida porque se da el caso que para el estado del proceso los items del arreglo son menores que los de las filas
        // porque recien cuando se graba aparece el estado del proceso.
         if (pv_estado_item != null && i < pv_estado_item.length) {
             itemBean.setNpestadoitemDesc(MiUtil.getString(MiUtil.getStringObject(pv_estado_item, i)));
             itemBean.setNpestadoitemId(MiUtil.getString(MiUtil.getStringObject(pv_estado_item_Val, i)));                
             itemBean.setNpestadoproceso(MiUtil.getString(MiUtil.getStringObject(pv_estado_proceso, i)));         
         }
         /**Fin Suspención definitiva**/
         
         /**Suspención Temporal**/
         itemBean.setNpcobro(MiUtil.getString(MiUtil.getStringObject(pv_cobro, i)));       // REVISAR
         /**Fin Suspención temporal**/                
         //Precio Original
         itemBean.setNporiginalprice(MiUtil.getString(MiUtil.getStringObject(pv_item_original_price,i)));
         //Tipo Precio 
         itemBean.setNppricetype(MiUtil.getString(MiUtil.getStringObject(pv_item_price_type,i)));
         //Código Tipo Precio 
         itemBean.setNppricetypeid(MiUtil.parseLong(MiUtil.getStringObject(pv_item_price_typeId,i)));
         //Código del Item del Tipo Precio 
         itemBean.setNppricetypeitemid(MiUtil.parseLong(MiUtil.getStringObject(pv_item_price_type_itemId,i)));       
         /**Fin Acuerdos Comerciales**/
         itemBean.setNpfixedphone(MiUtil.getStringObject(pv_item_fixed_phone_Val,i));
         itemBean.setNplocution(MiUtil.getStringObject(pv_item_locution_Val,i));  
       
         /*Inicio Responsable Pago*/
         itemBean.setNpsiteid(MiUtil.parseLong(MiUtil.getStringObject(pv_item_resp_pago_Val,i)));         
         /*Fin Responsable Pago*/
         
         /*Inicio Contratos de Referencia*/
         itemBean.setNpinternetrefcontract(MiUtil.parseLong(MiUtil.getStringObject(pv_item_contr_existente_int,i)));
         itemBean.setNptfrefphonenumber(MiUtil.getStringObject(pv_item_num_existente_tf,i));
         itemBean.setNpdatosrefcontract(MiUtil.parseLong(MiUtil.getStringObject(pv_item_contr_existente_datos,i)));
         itemBean.setNpcontractref(MiUtil.parseLong(MiUtil.getStringObject(pv_item_nodo_principal,i)));
         /*Fin Contratos de Referencia*/
         
         itemBean.setNptypeip(MiUtil.parseInt(MiUtil.getStringObject(pv_item_ip_Val,i)));
         
         // CPUENTE CAP & CAL - INICIO
        itemBean.setNpequipmenttime(MiUtil.parseInt(MiUtil.getStringObject(pv_item_equipment_time_int,i)));
        itemBean.setNpownequipment(MiUtil.parseInt(MiUtil.getStringObject(pv_item_own_equipment_int,i))); 
        itemBean.setNpreasonchange(MiUtil.getStringObject(pv_item_reason_change,i));
        // CPUENTE CAP & CAL - FIN
        itemBean.setNpLevel(MiUtil.parseInt(MiUtil.getStringObject(pv_item_level,i)));

        itemBean.setNpimeicustomer(MiUtil.getStringObject(pv_item_IMEI_cliente_Val,i));
        itemBean.setNpownimeisim(MiUtil.getStringObject(pv_item_imei_sim_Val,i));
        
        //DLAZO - SUSCRIPCIONES
        if(lSpecificationId != Constante.SPEC_SSAA_PROMOTIONS){
          itemBean.setNpactivationdate(MiUtil.toFecha(MiUtil.getStringObject(pv_item_activation_date_Val,i),"dd/MM/yyyy"));
        }else{
          itemBean.setNpactivationdate(MiUtil.getDateBD("dd/MM/yyyy"));
        }
        itemBean.setNpdeactivationdate(MiUtil.toFecha(MiUtil.getStringObject(pv_item_deactivation_date_Val,i),"dd/MM/yyyy"));
        
		  itemBean.setNpVepItem(MiUtil.parseInt(MiUtil.getStringObject(pv_item_venta_vep_Val,i)));
	    itemBean.setNpVepTotalPrice(MiUtil.parseDouble(MiUtil.getStringObject(pv_item_price_venta_vep_Val,i)));
	  
        // MMONTOYA - Despacho en tienda
        itemBean.setNpproductstatus(MiUtil.getStringObject(pv_item_ProductStatus_Val,i));
        itemBean.setNpchanged(MiUtil.getStringObject(pv_item_ItemChanged_Val,i));
        itemBean.setNpflagaccessory(MiUtil.getStringObject(pv_item_ItemFlagAccessory_Val,i));
	  
        // MMONTOYA [ADT-RCT-092 Roaming con corte]
        itemBean.setNpservplantype(MiUtil.parseInt(MiUtil.getStringObject(pv_item_ServPlanType_Val,i)));
        itemBean.setNpservbagcode(MiUtil.getStringObject(pv_item_ServBagCode_Val,i));
        itemBean.setNpservbagtype(MiUtil.getStringObject(pv_item_ServBagType_Val,i));
        itemBean.setNpservvalidactivationdate(MiUtil.toFecha(MiUtil.getStringObject(pv_item_ServValidActivationDate_Val,i),"dd/MM/yyyy"));
        itemBean.setNpservvalidity(MiUtil.parseInt(MiUtil.getStringObject(pv_item_ServValidity_Val,i)));

        //PRY-0762 JQUISPE Se agrega la cantidad de Renta Adelantada
        itemBean.setNpcantidadRentaAdelantada("".equals(MiUtil.getStringObject(pv_item_CantidadRentaAdelantada_Val,i))?null:Integer.valueOf(MiUtil.getStringObject(pv_item_CantidadRentaAdelantada_Val,i)));
        
        //PRY-0762 JQUISPE Se agrega el total de Renta Adelantada
        itemBean.setNptotalRentaAdelantada(MiUtil.getString(MiUtil.getStringObject(pv_item_TotalRentaAdelantada_Val,i)));
        
        //CDM+CDP EFLORES Se agrega el valor del PRY-0817
        if(pv_item_KeepSIM!=null && pv_item_KeepSIM_Val!=null){
            System.out.println("[PRY-0817][SectionItemEvents]["+pn_order_id+"] Longitud de arreglo Checkbox"+pv_item_KeepSIM.length+" Informacion "+pv_item_KeepSIM.toString());
            System.out.println("[PRY-0817][SectionItemEvents]["+pn_order_id+"] Valor de "+Constante.SPEC_CHECK_RESP_PAGO+"= "+MiUtil.getStringObject(pv_item_KeepSIM,i)+ " "+MiUtil.getStringObject(pv_item_KeepSIM_Val,i));
            itemBean.setNpkeepSIM(MiUtil.parseInt(MiUtil.getStringObject(pv_item_KeepSIM_Val,i)));
        }

        //PRY-0721 DERAZO Se agrega la Region del Item
        itemBean.setNpzonacoberturaid(MiUtil.parseInt(MiUtil.getStringObject(pv_item_region_Val,i)));
	  
        //TDECONV003-2 DERAZO Se agrega el IMEI FullStack del Item
        if(pv_item_ownImeiFS_Val != null){
          itemBean.setNpOwnImeiFS(MiUtil.getStringObject(pv_item_ownImeiFS_Val,i));
          System.out.println("itemBean.setNpOwnImeiFS:"+MiUtil.getStringObject(pv_item_ownImeiFS_Val,i));
        } 
  
	  
        //EFLORES BAFI2
        if(pv_item_NpProvinceZoneId!=null && pv_item_NpProvinceZoneId_Val!=null){
            itemBean.setNpProvinceZoneId(MiUtil.parseInt(MiUtil.getStringObject(pv_item_NpProvinceZoneId_Val,i)));
        }
        if(pv_item_NpDistrictZoneId!=null && pv_item_NpDistrictZoneId_Val!=null){
            itemBean.setNpDistrictZoneId(MiUtil.parseInt(MiUtil.getStringObject(pv_item_NpDistrictZoneId_Val,i)));
        }
	  
        //PRY-0890 JBALCAZAR
        if(pv_item_NpProrrateoPrice!=null && pv_item_NpProrrateoPrice_Val!=null){
          itemBean.setNpprorrateoPrice(MiUtil.getStringObject(pv_item_NpProrrateoPrice_Val,i));
        }

        //PRY-0890 JBALCAZAR
        if(pv_item_IndiceItemId!=null){
          itemBean.setNphndIndice(MiUtil.getStringObject(pv_item_IndiceItemId,i));
          System.out.println("itemBean.setNphndIndice ::"+MiUtil.getStringObject(pv_item_IndiceItemId,i));
        }        
	  
       String strMessage = null;
       if( operatorTransaction.equals("INSERT") ){
          logger.info("[Graba Item]");	
          strMessage = objItemDAO.doSaveItem(itemBean,conn);              
       }else{
          itemBean.setNpitemid(MiUtil.parseLong(pv_item_pk[i]));
          logger.info("[Actualiza Item]");
          strMessage = objItemDAO.doUpdateItem(itemBean,conn);   
       }
       
       if( operatorTransaction.equals("INSERT") ){
         System.out.println("=========== Fin - Registro del ITEM #Orden-> "+pn_order_id+" Usuario-> "+pv_session_login+"=========== Mensaje -> " +strMessage + " itemBeanId-> " + itemBean.getNporderid());
       }else{
         System.out.println("=========== Fin - Actualización del ITEM #Orden-> "+pn_order_id+" Usuario-> "+pv_session_login+"=========== Mensaje -> " +strMessage + " itemBeanId-> " + itemBean.getNporderid());
       }
       
       return strMessage;
    }

  public String grabarItemsBillingAccount(RequestHashMap request, Connection conn, String itemID, int index)  throws Exception, SQLException{
    /**Por ahora**/
    try{
    String    resultTransaction             = "";
    String    strOrderId                    = request.getParameter("hdnNumeroOrder");
    String    strCreatedBy                  = request.getParameter("hdnSessionLogin");
    String[]  pv_item_billing_account_Val   = request.getParameterValues("item_billingAccount");
    BaAssignmentBean baAssignmentBean       = null;
    
    Vector    la_services                   = new Vector();
    int       ln_indexid;
    String    lv_freeservice;
    String    wv_npflagservicio_ant         = "";
    String    wv_npflagservicio_act         = "";
    String    resultFlafservice             = "";
    boolean   flgLevelSave                  = false;
    
    String    flgEquipment                  = "";
    String    idService                     = "";
    String    chargeTypId                   = "";
    String    billingE                      = "";
    String    billingS                      = "";
    
    BillingAccountDAO objBillingAccountDAO  = new BillingAccountDAO();
    
    // INSERTAMOS LOS SERVICIOS DE ITEMS
    // Inicio: Se inserta los servicios de los ITEMs
    
    if ( pv_item_billing_account_Val != null ){
        String strCadena = MiUtil.getStringObject(pv_item_billing_account_Val,index);
        //Si hay elementos a ingresar
        if( !strCadena.equals("") ){
          StringTokenizer tokens  = new StringTokenizer(strCadena,"|");
        
        String strTipeSite      = "";
        String strNewSiteId     = "";
        String strTipeBilling   = "";
        String strNewBillingId  = "";
        
        if(tokens.hasMoreTokens()){
          String aux = "";
          aux   =   (String)tokens.nextToken();
          strTipeSite     = aux;
          aux   =   (String)tokens.nextToken();
          strNewSiteId    = aux;
          aux   =   (String)tokens.nextToken();
          strTipeBilling  = aux;  
          aux   =   (String)tokens.nextToken();
          strNewBillingId = MiUtil.getString(aux);
          
          if( strNewBillingId.equals("-2") || strNewBillingId.equals("") )
            strNewBillingId = null;
          
          baAssignmentBean    = new BaAssignmentBean();
          baAssignmentBean.setNporderid(MiUtil.parseLong(strOrderId));
          //baAssignmentBean.setBillingAccountId(billingE);
          //baAssignmentBean.setNpbillaccnewid(billingS);
          if( MiUtil.getString(strTipeBilling).equals("E") )
            baAssignmentBean.setBillingAccountId(strNewBillingId);
          if( MiUtil.getString(strTipeBilling).equals("S") )
            baAssignmentBean.setNpbillaccnewid(strNewBillingId);
          //baAssignmentBean.setNpserviceid(MiUtil.parseLong(idService));
          
          if( MiUtil.getString(strTipeSite).equals("E") )
            baAssignmentBean.setNpactivesiteid(strNewSiteId);
          if( MiUtil.getString(strTipeSite).equals("S") )
            baAssignmentBean.setNpunknowsiteid(strNewSiteId);
          
          baAssignmentBean.setNpdeviceid(MiUtil.parseLong(itemID)); 
          baAssignmentBean.setNpcreateddate(MiUtil.getDateBD("dd/MM/yyyy"));
          baAssignmentBean.setNpcreatedby(strCreatedBy);
          logger.info("[Inicio Inserta valores en tabla: orders.np_ba_assignment]");
          resultTransaction = objBillingAccountDAO.insertarAssignementAccount(baAssignmentBean,conn);
          //if( resultTransaction!= null)
            //return resultTransaction;
        }
        
          
        }
        
        /*
        for( int n = 0,j = 1;  j <= (la_services.size()/16); n=n+16,j++ ) {
              
          //Solo la primera vez
          if( n == 0 ){
          
              //Verificamos si el registro es a modo equipo
              flgEquipment  = (String)la_services.elementAt(n+2);
          
              //Si ha sido seleccionado. Ingresamos un solo registro
              if( !flgEquipment.equals("-2") ){
              
              //Obtenemos el Tipo de Cargo para Único
              billingE            = null;
              billingS            = null;
              baAssignmentBean    = new BaAssignmentBean();
                if( ((String)la_services.elementAt(n+1)).equals("E") )
                  billingE = (String)la_services.elementAt(n+2);
                else if( ((String)la_services.elementAt(n+1)).equals("S") )
                  billingS = (String)la_services.elementAt(n+2);
              
              baAssignmentBean.setNporderid(MiUtil.parseLong(strOrderId));
              baAssignmentBean.setBillingAccountId(billingE);
              baAssignmentBean.setNpbillaccnewid(billingS);
              //baAssignmentBean.setNpserviceid(MiUtil.parseLong(idService));
              baAssignmentBean.setNpdeviceid(MiUtil.parseLong(itemID)); 
              //baAssignmentBean.setNpchargetype("Unico");
              baAssignmentBean.setNpcreateddate(MiUtil.getDateBD("dd/MM/yyyy"));
              baAssignmentBean.setNpcreatedby(strCreatedBy);
              
              //resultTransaction = objBillingAccountService.BillingAccountDAOinsertarAssignementAccount(baAssignmentBean);
              resultTransaction = objBillingAccountDAO.insertarAssignementAccount(baAssignmentBean,conn);
              
                if( resultTransaction!= null)
                  return resultTransaction;
                
              }
          
          }
              
          //Verificamos si el registro es a nivel Servicio Adicional
          flgEquipment  = (String)la_services.elementAt(n+5);
          
          //Si el SA fue seleccionado. Ingresamos un solo registro para este SA
          if( !flgEquipment.equals("-2") ){
          
              //Obtenemos el SA
              idService    = (String)la_services.elementAt(n+6);
              
              //Obtenemos el Tipo de Cargo para Único
              billingE            = null;
              billingS            = null;
              baAssignmentBean    = new BaAssignmentBean();
                if( ((String)la_services.elementAt(n+4)).equals("E") )
                  billingE = (String)la_services.elementAt(n+5);
                else if( ((String)la_services.elementAt(n+4)).equals("S") )
                  billingS = (String)la_services.elementAt(n+5); 
              
              baAssignmentBean.setNporderid(MiUtil.parseLong(strOrderId));
              baAssignmentBean.setBillingAccountId(billingE);
              baAssignmentBean.setNpbillaccnewid(billingS);
              baAssignmentBean.setNpserviceid(idService);
              baAssignmentBean.setNpdeviceid(MiUtil.parseLong(itemID)); 
              //baAssignmentBean.setNpchargetype("Unico");
              baAssignmentBean.setNpcreateddate(MiUtil.getDateBD("dd/MM/yyyy"));
              baAssignmentBean.setNpcreatedby(strCreatedBy);
              
              resultTransaction = objBillingAccountDAO.insertarAssignementAccount(baAssignmentBean,conn);
          
              
              if( resultTransaction!= null)
                return resultTransaction;
              
          }else{
                  
            //Obtenemos el SA
            idService    = (String)la_services.elementAt(n+6);
            
            //Verificamos si el registro es a nivel Servicio Adicional
            flgEquipment  = (String)la_services.elementAt(n+9);
            
            if( !flgEquipment.equals("-2") ){
            
            //Obtenemos el Tipo de Cargo para Único
            billingE            = null;
            billingS            = null;
            baAssignmentBean    = new BaAssignmentBean();
              if( ((String)la_services.elementAt(n+8)).equals("E") )
                billingE = (String)la_services.elementAt(n+9);
              else if( ((String)la_services.elementAt(n+8)).equals("S") )
                billingS = (String)la_services.elementAt(n+9); 
            
            baAssignmentBean.setNporderid(MiUtil.parseLong(strOrderId));
            baAssignmentBean.setBillingAccountId(billingE);
            baAssignmentBean.setNpbillaccnewid(billingS);
            baAssignmentBean.setNpserviceid(idService);
            baAssignmentBean.setNpdeviceid(MiUtil.parseLong(itemID)); 
            baAssignmentBean.setNpchargetype("Unico");
            baAssignmentBean.setNpcreateddate(MiUtil.getDateBD("dd/MM/yyyy"));
            baAssignmentBean.setNpcreatedby(strCreatedBy);
            
            resultTransaction = objBillingAccountDAO.insertarAssignementAccount(baAssignmentBean,conn);
        
            
            if( resultTransaction!= null)
              return resultTransaction;
              
            }
            
            //Verificamos si el registro es a nivel Servicio Adicional
            flgEquipment  = (String)la_services.elementAt(n+12);
            
            if( !flgEquipment.equals("-2") ){
            
            //Obtenemos el Tipo de Cargo para Recurrente
            billingE            = null;
            billingS            = null;
            baAssignmentBean    = new BaAssignmentBean();
              if( ((String)la_services.elementAt(n+11)).equals("E") )
                billingE = (String)la_services.elementAt(n+12);
              else if( ((String)la_services.elementAt(n+11)).equals("S") )
                billingS = (String)la_services.elementAt(n+12); 
            
            baAssignmentBean.setNporderid(MiUtil.parseLong(strOrderId));
            baAssignmentBean.setBillingAccountId(billingE);
            baAssignmentBean.setNpbillaccnewid(billingS);
            baAssignmentBean.setNpserviceid(idService);
            baAssignmentBean.setNpdeviceid(MiUtil.parseLong(itemID)); 
            baAssignmentBean.setNpchargetype("Recurrente");
            baAssignmentBean.setNpcreateddate(MiUtil.getDateBD("dd/MM/yyyy"));
            baAssignmentBean.setNpcreatedby(strCreatedBy);
            
            resultTransaction = objBillingAccountDAO.insertarAssignementAccount(baAssignmentBean,conn);
        
            
            if( resultTransaction!= null)
              return resultTransaction;
              
            }
            
            //Verificamos si el registro es a nivel Servicio Adicional
            flgEquipment  = (String)la_services.elementAt(n+15);
            
            if( !flgEquipment.equals("-2") ){
              
            //Obtenemos el Tipo de Cargo para Excesos
            billingE            = null;
            billingS            = null;
            baAssignmentBean    = new BaAssignmentBean();
              if( ((String)la_services.elementAt(n+14)).equals("E") )
                billingE = (String)la_services.elementAt(n+15);
              else if( ((String)la_services.elementAt(n+14)).equals("S") )
                billingS = (String)la_services.elementAt(n+15); 
            
            baAssignmentBean.setNporderid(MiUtil.parseLong(strOrderId));
            baAssignmentBean.setBillingAccountId(billingE);
            baAssignmentBean.setNpbillaccnewid(billingS);
            baAssignmentBean.setNpserviceid(idService);
            baAssignmentBean.setNpdeviceid(MiUtil.parseLong(itemID)); 
            baAssignmentBean.setNpchargetype("Excesos");
            baAssignmentBean.setNpcreateddate(MiUtil.getDateBD("dd/MM/yyyy"));
            baAssignmentBean.setNpcreatedby(strCreatedBy);
            
            resultTransaction = objBillingAccountDAO.insertarAssignementAccount(baAssignmentBean,conn);
            
            if( resultTransaction!= null)
              return resultTransaction;
            
            }
          
          }
        
        }*/
      
    }
            
      return null;
    }catch(Exception e){
      System.out.println("Hubieron errores en asignación de Billing Account");
      return null;
    }
  }
  
  public String grabarItemsServices(RequestHashMap request, Connection conn, String itemID, int index)  throws Exception, SQLException{
      
    String    resultTransaction          = "";
    String[]  pv_item_services           = request.getParameterValues("item_services");
    
    /*JPEREZ: EXCEPCIONES - INICIO*/
    String[]  pv_item_exc_services       = request.getParameterValues("txtItemExceptionServiceId");
    String[]  pv_item_exc_services_disc  = request.getParameterValues("txtItemExceptionServiceDisc");
    /*JPEREZ: EXCEPCIONES - FIN*/
    ItemServiceBean itemServiceBean      = null;
    
    Vector    la_services                = new Vector();
    int       ln_indexid;
    String    lv_freeservice;
    String    wv_npflagservicio_ant      = "";
    String    wv_npflagservicio_act      = "";
    String    resultFlafservice          = "";
          
 
    // INSERTAMOS LOS SERVICIOS DE ITEMS
    // Inicio: Se inserta los servicios de los ITEMs
    
    if ( pv_item_services != null ){
          
      String strCadena = MiUtil.getStringObject(pv_item_services,index);
      StringTokenizer tokens  = new StringTokenizer(strCadena,"|");
      
      while(tokens.hasMoreTokens()){
        String aux = tokens.nextToken();
        la_services.addElement((String)aux);
      }
      
      
      for( int n = 0,j = 1;  j <= (la_services.size()/3); n=n+3,j++ ) {
      
          if (  ((String)la_services.elementAt(n+1)).equals("S") ) {
             wv_npflagservicio_ant = "X";
          }
          else {
             wv_npflagservicio_ant = ""; 
          }
          
          if (  ((String)la_services.elementAt(n+2)).equals("S") ) {
             wv_npflagservicio_act = "X";
          }
          else {
             wv_npflagservicio_act = ""; 
          }
          
          if ( wv_npflagservicio_ant.equals("X") && wv_npflagservicio_act.equals("X") ){
              resultFlafservice = "Contratado";
          }else if( wv_npflagservicio_ant.equals("X") && wv_npflagservicio_act.equals("") ){
              resultFlafservice = "Remover";
          }else if( wv_npflagservicio_ant.equals("") && wv_npflagservicio_act.equals("X") ){
              resultFlafservice = "Solicitado";
          }else{
              resultFlafservice = "";
          }
      
          itemServiceBean = new ItemServiceBean();
          itemServiceBean.setNpitemid(MiUtil.parseLong(itemID));
          itemServiceBean.setNpserviceid(MiUtil.parseLong((String)la_services.elementAt(n)));
          itemServiceBean.setNpservicetype("A");
          itemServiceBean.setNpaction(resultFlafservice);
          
          //Si el servicio tiene excepción
          if ( pv_item_exc_services != null ){
            if (pv_item_exc_services[index] != null){                        
               StringTokenizer tkItemExcSrvId   = new StringTokenizer(pv_item_exc_services[index],"|");                        
               StringTokenizer tkItemExcSrvDisc = new StringTokenizer(pv_item_exc_services_disc[index],"|"); 
               try{
               while ( tkItemExcSrvId.hasMoreTokens() ){
                  long lExcServId   = Long.parseLong(tkItemExcSrvId.nextToken());                             
                  if ( lExcServId == itemServiceBean.getNpserviceid()  ){
                     itemServiceBean.setNpservicefree("S");
                     try{
                        double servicePrice = Double.parseDouble(tkItemExcSrvDisc.nextToken());
                        itemServiceBean.setNpserviceprice(servicePrice);
                     }catch(Exception e){
                        e.printStackTrace();
                     }
                  }
               }
               }catch(Exception e){}
             }
          }
          logger.info("[Inserta servicios del item]");
          resultTransaction = objItemDAO.getItemServiceInsertar(itemServiceBean,conn);
          
          if( resultTransaction!=null )
            return resultTransaction;
      }
    }
    return null;
  }
    
  public String grabarItemsImeis(RequestHashMap request, Connection conn, String itemID, int index)  throws Exception, SQLException{
    
    String strActionName                    = (request.getParameter("cmbAction")==null?"":request.getParameter("cmbAction"));
    String[]  pv_item_indice                = request.getParameterValues("hdnIndice");
    String[]  pv_item_indice_ime            = request.getParameterValues("hdnIndice_imei");
    String[]  pv_item_indice_imei           = null;
    String[]  pv_item_indice_sim            = null;
    
    logger.info("[pv_item_indice_ime]: " +pv_item_indice_ime);
    logger.info("[index]: " + index);
    if(pv_item_indice_ime!=null){
      logger.info("[pv_item_indice_ime][Cantidad de Equipos]: " +pv_item_indice_ime.length);    
    }
        
    //RDELOSREYES - Carga Masiva - 29/10/2008 - INICIO
    HashMap hshDataMap = (HashMap) request.get(Constante.DATA_STRUCT);
    if(hshDataMap==null) {
      pv_item_indice_imei           = request.getParameterValues("item_imei_imei");
      pv_item_indice_sim            = request.getParameterValues("item_imei_sim");
    } else {
      LoadMassiveItemBean loadMassiveItemBean = (LoadMassiveItemBean) MapUtils.getObject(hshDataMap, "loadMassiveItemBean");
      pv_item_indice_imei = loadMassiveItemBean.getArrayImei();
      pv_item_indice_sim = loadMassiveItemBean.getArraySim();
    }
    //RDELOSREYES - Carga Masiva - 29/10/2008 - FIN
    String[]  pv_item_indice_bad            = request.getParameterValues("item_imei_bad");
    String[]  pv_item_indice_product        = request.getParameterValues("item_imei_producto");
    String[]  pv_item_imei_producto_id      = request.getParameterValues("item_imei_producto_id");
    String[]  pv_item_hdnImeiChange         = request.getParameterValues("hdnImeiChange");
    String[]  pv_item_hdnSimChange          = request.getParameterValues("hdnSimChange");
    
    String[]  pv_item_indice_plan           = request.getParameterValues("item_imei_plan");
    String[]  pv_item_indice_check          = request.getParameterValues("item_imei_check");
    
    String[]  pv_item_imei_contract_number      = request.getParameterValues("item_imei_contract_number");
    String[]  pv_item_imei_request_id           = request.getParameterValues("item_imei_request_id");
    
    String    strOrderId                    = request.getParameter("hdnNumeroOrder");
    String    strCreatedBy                  = request.getParameter("hdnSessionLogin");
    ////////////////////////////////////////////////////////////////////
    String    strLugarAtencion               = request.getParameter("cmbLugarAtencion"); //LUGAR DE DESPACHO
    String[]  pv_item_Product_Val           = request.getParameterValues("hdnItemValuetxtItemProduct"); //ID DE PRODUCTO
    String[]  pv_item_modality_Val          = request.getParameterValues("hdnItemValuetxtItemModality"); //MODALIDAD
    String    strSubCategoria                 = request.getParameter("cmbSubCategoria"); //SUBCATEGORIA
    String[]  pv_item_Warrant_Val           = request.getParameterValues("hdnItemValuetxtItemFlgProductGN");
    String[]  pv_item_Num_Tel               = request.getParameterValues("hdnNumTel");//TELEFONO
    
    ////////////////////////////////////////////////////////////////////
    String    resultTransaction             = null;
    
    ItemDeviceBean itemDeviceBean           = null;
    HashMap hshItemDeviceMap           = new HashMap();
    
    System.out.println("=========== Inicio - Registro del ITEM_DEVICE #Orden-> "+strOrderId+" #ItemId-> " + itemID +" Usuario-> "+strCreatedBy+"===========");
    
    String    itemEvaluate                  = "0";
    String    strMessage                    = null;
    
    int k = 0;
    
    if( pv_item_indice_ime != null ) {
        
        itemEvaluate  = pv_item_indice[index];
        logger.info("[itemEvaluate]: " + itemEvaluate);
        
        for( int i = 0; i < pv_item_indice_ime.length; i++ ){
            
            logger.info("Evaluacion del Item en el For : "+pv_item_indice_ime[i]+" == "+itemEvaluate+" iteracion numero: "+i);
            
            if( pv_item_indice_ime[i].equals(itemEvaluate) ) {
              k++;
              itemDeviceBean = new ItemDeviceBean();
              itemDeviceBean.setNpitemid(MiUtil.parseLong(itemID));
              itemDeviceBean.setNporderid(MiUtil.parseLong(strOrderId));
              itemDeviceBean.setNpproductid(MiUtil.parseLong(MiUtil.getStringObject(pv_item_imei_producto_id,i)));
              itemDeviceBean.setNpcreatedby(strCreatedBy);
              itemDeviceBean.setNpserialnumber(MiUtil.getStringObject(pv_item_indice_imei,i));
              itemDeviceBean.setNpsimnumber(MiUtil.getStringObject(pv_item_indice_sim,i));
              itemDeviceBean.setNpbadimei(MiUtil.getStringObject(pv_item_indice_bad,i));
              itemDeviceBean.setNpimeinumber(MiUtil.getStringObject(pv_item_indice_imei,i));
              itemDeviceBean.setNpcheckimei(MiUtil.getStringObject(pv_item_indice_check,i));
              itemDeviceBean.setNpcreateddate(MiUtil.getDateBD("dd/MM/yyyy"));
              itemDeviceBean.setStrImeiChange(MiUtil.getStringObject(pv_item_hdnImeiChange, i));
              itemDeviceBean.setStrSimChange(MiUtil.getStringObject(pv_item_hdnSimChange, i));
              itemDeviceBean.setNpphone(MiUtil.getStringObject(pv_item_Num_Tel, i));
              itemDeviceBean.setNprequestid(MiUtil.parseLong(MiUtil.getStringObject(pv_item_imei_request_id, i)));
              itemDeviceBean.setNpcontractnumber(MiUtil.parseDouble(MiUtil.getStringObject(pv_item_imei_contract_number, i)));
              
              logger.info("[Inserta en el item_device][getItemInsertDevices][strActionName]: " +strActionName);
              resultTransaction = objItemDAO.getItemInsertDevices(itemDeviceBean,strActionName,conn);
              
              System.out.println("=========== Fin - Registro del ITEM_DEVICE #Orden-> "+strOrderId+" #ItemId-> " + itemID +" Usuario-> "+strCreatedBy+"=========== Message->"+resultTransaction);
              
              if (resultTransaction!=null ){                              
                strMessage = MiUtil.getString(strMessage) +"\n"+ itemEvaluate+"-"+MiUtil.getString(k)+" "+ resultTransaction;                
              }  
              //return resultTransaction;
              
              /*
              hshItemDeviceMap.put("imei", MiUtil.getStringObject(pv_item_indice_imei,i));
              hshItemDeviceMap.put("lugarDespacho", strLugarAtencion); //FULLFILMENT = 41
              hshItemDeviceMap.put("producto", MiUtil.getStringObject(pv_item_Product_Val, index));
              hshItemDeviceMap.put("modalidad", MiUtil.getStringObject(pv_item_modality_Val, index));
              hshItemDeviceMap.put("subCategoria", strSubCategoria);
              hshItemDeviceMap.put("garantia", MiUtil.getStringObject(pv_item_Warrant_Val,index));
              resultTransaction = objItemDAO.doValidateIMEI(hshItemDeviceMap, conn);
                          if (StringUtils.isNotBlank(resultTransaction))
                return resultTransaction;
              */  
            }else{
              k = 0;
            } 
        }
          
    }
    
    return strMessage;
  }
      
       public String insertTemplateOrder(RequestHashMap request, Connection conn, String itemID, int ind)  throws Exception, SQLException{
          String[]  pv_list_adenda       = request.getParameterValues("txtListAdenda");
          String[]  pv_item_Imei         = request.getParameterValues("txtItemIMEI");
          String[]  pv_priceCtaInscrip   = request.getParameterValues("txtItemPriceCtaInscrip");
          String[]  pv_priceExcp         = request.getParameterValues("txtItemPriceException");                    
          String[]  pv_item_Product_Val  = request.getParameterValues("hdnItemValuetxtItemProduct");
          String[]  pv_item_modality_Val = request.getParameterValues("hdnItemValuetxtItemModality");
          String    pv_Specification     = request.getParameter("hdnSpecification");          
          String[]  pv_item_Ocurrence_Val = request.getParameterValues("hdnItemValuetxtItemNroOcurrence");
          String    pn_order_id           = request.getParameter("hdnNumeroOrder");
          String    pv_session_login      = request.getParameter("hdnSessionLogin");                      
          String    pv_salesstruct_original = request.getParameter("hdnSalesStructOrigenId");
                    
          int num_order = MiUtil.parseInt(pn_order_id); 
          int item_id = MiUtil.parseInt(itemID); 
          
          String template_ids = null; 
          template_ids = pv_list_adenda[ind];
          String imei = "";
          if(pv_item_Imei != null)
             imei = pv_item_Imei[ind];
          
          GeneralDAO objGeneralDAO = new GeneralDAO();   
          double descount      = 0;
          String strOcurrence  = null;
          String strProduct    = MiUtil.getStringObject(pv_item_Product_Val,ind);
          String strModality   = MiUtil.getStringObject(pv_item_modality_Val,ind);          
          String strModalityId = objGeneralDAO.getValue("MODALITY_TYPES", strModality);
          String strSpecification = pv_Specification;
          int iSalesStructOriginalId = MiUtil.parseInt(pv_salesstruct_original);
          logger.info("[strModalityId][objGeneralDAO.getValue]: " +strModalityId);
          
          if (StringUtils.isNotEmpty(MiUtil.getStringObject(pv_item_Ocurrence_Val,ind)))
            strOcurrence = MiUtil.getStringObject(pv_item_Ocurrence_Val,ind);
          
          //System.out.println("strProduct = " + strProduct);
          //System.out.println("strSpecification = " + strSpecification);        
          //System.out.println("strModalityId = " + strModalityId);        
          //System.out.println("strOcurrence = " + strOcurrence);          
          //System.out.println("txtItemPriceCtaInscrip = " + MiUtil.getStringObject(pv_priceCtaInscrip,ind));
          //System.out.println("txtItemPriceException = " + MiUtil.getStringObject(pv_priceExcp,ind));

          OrderDAO objOrderDAO = new OrderDAO();
          //(int an_npproductid, String av_npmodality, int an_npoccurrence, int an_npcategoryid)
          HashMap hshProdutPriceMap  = objOrderDAO.getProductPriceList(strProduct, strModalityId,strOcurrence, strSpecification, iSalesStructOriginalId);          
          String strProductPriceList = (String)hshProdutPriceMap.get("dblProductPriceList");  
          logger.info("[strProductPriceList][objOrderDAO.getProductPriceList]:" +strProductPriceList);		  
          double dblProductPriceList = Double.parseDouble(strProductPriceList); 
          
          if (StringUtils.isNotEmpty(strModalityId)){
            // Si es Alquiler (10) o Venta (20)
            if ((strModalityId.equals("10"))||(strModalityId.equals("20"))){

              if(StringUtils.isNotEmpty(MiUtil.getStringObject(pv_priceExcp,ind)))
                descount = dblProductPriceList - MiUtil.parseDouble(MiUtil.getStringObject(pv_priceExcp,ind));
              else
                if(StringUtils.isNotEmpty(MiUtil.getStringObject(pv_priceCtaInscrip,ind)))
                  descount = dblProductPriceList -MiUtil.parseDouble( MiUtil.getStringObject(pv_priceCtaInscrip,ind));
            }                    
          }            
                               
          String user = pv_session_login;
         
          String resultTransaction = null;
          OrderDAO orderDao = new OrderDAO();
          if(template_ids != null && StringUtils.isNotEmpty(template_ids) ){
          logger.info("[Registra las adendas y/o plantillas de la orden]");		
              resultTransaction = orderDao.insertTemplateOrder(num_order, 
                                                               item_id, 
                                                               template_ids,// idAdendas - plazos 
                                                               imei,
                                                               descount,
                                                               user,
                                                               conn);
                                                               
              if( resultTransaction!=null ){
                 return resultTransaction;
              }
          }
          return null;
       }
       
       public String deleteTemplateOrder(RequestHashMap request, Connection conn, String itemID, int ind)  throws Exception, SQLException{
          String pn_order_id = request.getParameter("hdnNumeroOrder");          
          int num_order = MiUtil.parseInt(pn_order_id); 
          int item_id = MiUtil.parseInt(itemID); 
          
          String resultTransaction = null;
          OrderDAO orderDao = new OrderDAO();
          logger.info("[Eliminar las plantillas de adendas de un item asociadas a una orden]");
          resultTransaction = orderDao.deleteTemplateOrder(num_order,item_id,conn);                                                              
          if( resultTransaction!=null ){
            return resultTransaction;
          }
          
          return null;
       }
 
  /**
   * Motivo: Registra/Actualiza los Items con penalidad
   * <br>Realizado por: <a href="mailto:daniel.erazo@hpe.com">Daniel Erazo</a>
   * <br>Fecha: 26/05/2016
   * @param     RequestHashMap request
   * @param     Connection conn
   * @param     IteamBean itemBean
   * @return    String result
   */
  public String grabarItemPenalidad(RequestHashMap request, Connection conn, ItemBean itemBean, long hasPenalty, String operatorTransaction)  throws Exception, SQLException{
      String  pv_session_login  = request.getParameter("hdnSessionLogin");
      String strMessage = null;

      System.out.println("[grabarItemPenalidad] "+operatorTransaction+" del ITEM PENALTY # Orden: "+itemBean.getNporderid()+" Usuario: "+pv_session_login);
      System.out.println("[grabarItemPenalidad] Entra al Metodo: penaltyDAO.doUpdateItemPenalty");
      strMessage = penaltyDAO.doUpdateItemPenalty(conn, itemBean, hasPenalty, pv_session_login, operatorTransaction);

      return strMessage;
  }

  /**
   * Motivo: Elimina los Items con penalidad
   * <br>Realizado por: <a href="mailto:daniel.erazo@hpe.com">Daniel Erazo</a>
   * <br>Fecha: 26/05/2016
   * @param     String pv_items_borrados
   * @param     Connection conn
   * @param     String pn_order_id
   * @return    String result
   */
  public String deleteItemsPenalty(String pv_items_borrados, String pn_order_id, Connection conn) throws Exception, SQLException{
    StringTokenizer strTokenizer =  new StringTokenizer(pv_items_borrados,"|");
    Vector vctDeleted = new Vector();

    System.out.println("[deleteItemsPenalty] DELETE el ITEM PENALTY # Orden: "+pn_order_id);

    while(strTokenizer.hasMoreElements()){
        String str = (String)strTokenizer.nextElement();
        vctDeleted.addElement(""+str);
    }

    String resultDeleteItem = "";

    for(int x=0; x<vctDeleted.size(); x++){
        ItemBean itemBean = new ItemBean();
        itemBean.setNporderid(MiUtil.parseLong(pn_order_id));
        itemBean.setNpitemid(MiUtil.parseLong((String) vctDeleted.elementAt(x)));
        itemBean.setNpphone("");
        System.out.println("[deleteItemsPenalty] Entra al Metodo: penaltyDAO.doUpdateItemPenalty");
        resultDeleteItem  = penaltyDAO.doUpdateItemPenalty(conn, itemBean, 0, "", "DELETE");

        if(resultDeleteItem != null) return resultDeleteItem;
    }

    return null;
  }
 
  /**
  * Motivo: Registra los Items, Servicios Adicionales, Imeis , Excepciones y Adendas de una orden
  * <br>Realizado por: <a href="mailto:ruth.polo@nextel.com.pe">Ruth Polo</a>
  * <br>Fecha: 30/10/2007
  * @param     RequestHashMap request
  * @param     Connection conn
  * @param     int cant
  * @return    String resultInsertItems
  */
  public String validateItems(RequestHashMap request, Connection conn, int cant) throws Exception, SQLException{
    String    strPhone =   null;
    long      IntimeiID;
    Vector    la_services                   = new Vector();
    String    wv_npflagservicio_ant         = "";
    String    wv_npflagservicio_act         = "";
    int       IntServicioid=0;
    long      lSpecificationId     = MiUtil.parseLong(request.getParameter("hdnSpecification"));
    HashMap   hshData=null;
    String    strMessage= null;
    int       intCount=0;
    String StrMessage;
 
    OrderDAO objOrderDAO = new OrderDAO();
      
    for(int i=0; i<cant; i++){       
    //validar si existe una orden con la misma solicitud de Servicio
      String[]  pv_item_Phone              = request.getParameterValues("txtItemPhone");
      String[]  pv_item_Phone_Val          = request.getParameterValues("hdnItemValuetxtItemPhone");
      String[]  pv_item_pk                 = request.getParameterValues("hdnItemId");
      String[]  pv_item_services           = request.getParameterValues("item_services");
      
      System.out.println("pv_item_Phone_Val="+pv_item_Phone_Val);
      System.out.println("valor="+MiUtil.getStringObject(pv_item_Phone_Val,i));
      
      strPhone    = MiUtil.getStringObject(pv_item_Phone_Val,i);
      IntimeiID   = MiUtil.parseLong(pv_item_pk[i]);

      if (lSpecificationId == Constante.SPEC_ACTIVAR_DESACTIVAR_SERVICIOS) {
        if ( pv_item_services != null ){
          String strCadena = MiUtil.getStringObject(pv_item_services,i);
          StringTokenizer tokens  = new StringTokenizer(strCadena,"|");
                
          while(tokens.hasMoreTokens()){
            String aux = tokens.nextToken();
            la_services.addElement((String)aux);
          }
          System.out.println("la_services.size()="+la_services.size());
          for( int n = 0,j = 1;  j <= (la_services.size()/3); n=n+3,j++ ) {
            if (  ((String)la_services.elementAt(n+1)).equals("S") ) {
               wv_npflagservicio_ant = "X";
            }
            else {
               wv_npflagservicio_ant = ""; 
            }
            
            if (  ((String)la_services.elementAt(n+2)).equals("S") ) {
               wv_npflagservicio_act = "X";
            }
            else {
               wv_npflagservicio_act = ""; 
            }
            //Solicitado
            if( wv_npflagservicio_ant.equals("") && wv_npflagservicio_act.equals("X") ){
              IntServicioid=(int)MiUtil.parseLong((String)la_services.elementAt(n));
            }
            
            System.out.println("strPhone="+strPhone+"IntServicioid="+IntServicioid+"lSpecificationId="+lSpecificationId);                    
            if (strPhone!="" && IntServicioid!=0 ){
              hshData = objOrderDAO.getValidateSSAA(lSpecificationId, strPhone, IntServicioid);
              strMessage=(String)hshData.get("strMessage");
              System.out.println("strMessage="+strMessage);
              if (strMessage!=null)
                throw new Exception(strMessage);   
              intCount=MiUtil.parseInt((String)hshData.get("strCount"));
              System.out.println("intCount="+intCount);
              if ( intCount >0) {
                if (conn != null) conn.rollback();
                StrMessage="Existe una orden en proceso para el equipo "+strPhone+ " en donde se solicita el mismo servicio adicional, favor verifique!!!";
                hshData.put("strMessage",StrMessage);
                return StrMessage;
              }//if
            }//if
          }//for
        }//if
      }//if
    }//for
    return null;
  }


    public String updateItemsDevice(RequestHashMap request, Connection conn, String itemID, int index)  throws Exception, SQLException{

        String strActionName                    = (request.getParameter("cmbAction")==null?"":request.getParameter("cmbAction"));
        String[]  pv_item_indice                = request.getParameterValues("hdnIndice");
        String[]  pv_item_indice_ime            = request.getParameterValues("hdnIndice_imei");


        logger.info("[pv_item_indice_ime]: " +pv_item_indice_ime);
        logger.info("[index]: " + index);
        if(pv_item_indice_ime!=null){
            logger.info("[pv_item_indice_ime][Cantidad de Equipos]: " +pv_item_indice_ime.length);
        }

        String[]  pv_item_indice_check          = request.getParameterValues("item_imei_check");
        String    strOrderId                    = request.getParameter("hdnNumeroOrder");
        String    strCreatedBy                  = request.getParameter("hdnSessionLogin");
        String    resultTransaction             = null;
        ItemDeviceBean itemDeviceBean           = null;


        System.out.println("=========== Inicio - Update del ITEM_DEVICE #Orden-> "+strOrderId+" #ItemId-> " + itemID +" Usuario-> "+strCreatedBy+"===========");


        String    itemEvaluate                  = "0";
        String    strMessage                    = null;

        int k = 0;

        if( pv_item_indice_ime != null ) {

            itemEvaluate  = pv_item_indice[index];
            logger.info("[itemEvaluate]: " + itemEvaluate);

            for( int i = 0; i < pv_item_indice_ime.length; i++ ){

                logger.info("Evaluacion del Item en el For : "+pv_item_indice_ime[i]+" == "+itemEvaluate+" iteracion numero: "+i);

                if( pv_item_indice_ime[i].equals(itemEvaluate) ) {
                    k++;
                    itemDeviceBean = new ItemDeviceBean();
                    itemDeviceBean.setNpitemid(MiUtil.parseLong(itemID));
                    itemDeviceBean.setNporderid(MiUtil.parseLong(strOrderId));
                    itemDeviceBean.setNpcheckimei(MiUtil.getStringObject(pv_item_indice_check, i));

                    logger.info("[Actualiza en el item_device][updateItemsDevice][strActionName]: " +strActionName);
                    //resultTransaction = objItemDAO.getItemInsertDevices(itemDeviceBean, strActionName, conn);
                    resultTransaction = objItemDAO.updItemDevice(itemDeviceBean, conn);
                    System.out.println("=========== Fin - Update del ITEM_DEVICE #Orden-> "+strOrderId+" #ItemId-> " + itemID +" Usuario-> "+strCreatedBy+"=========== Message->"+resultTransaction);

                    if (resultTransaction!=null ){
                        strMessage = MiUtil.getString(strMessage) +"\n"+ itemEvaluate+"-"+MiUtil.getString(k)+" "+ resultTransaction;
                    }
                    //return resultTransaction;

                }else{
                    k = 0;
                }
            }

        }

        return strMessage;
    }

    /**
     * Motivo: Validar Penalidad Para el Cambio de Plan
     * <br>Realizado por: <a href="mailto:carlos.diazoliva@tcs.com">Carlos Diaz</a>
    * <br>Fecha: 17/04/2017
     * @param     RequestHashMap request  
     * @param     ItemBean itemBean       
     * @return    boolean hasPenalty 
     */
   	public boolean validarPenalidadCambioDePlan(RequestHashMap request, ItemBean itemBean) throws SQLException, Exception {
   		boolean hasPenalty = true;

   		String strSpecificationId = (request.get("hdnSpecification") == null ? "" : (String) request.get("hdnSpecification"));
   		String strCustomerId = (String)request.getParameter("txtCompanyId");
   		String strPhone = itemBean.getNpphone();
   		
   		long planId = itemBean.getNpplanid();
   		int piAppId = MiUtil.getInt(request.get("hdnSessionAppid"));

   		System.out.println("[validarPenalidadCambioDePlan] customerId::" + strCustomerId + "||phone::" + strPhone + "||appId::" + piAppId + "||specificationId::" + strSpecificationId);
   		
   		ConfigurationBean configurationBean = obtenerConfiguracionMontoPenalidad(strSpecificationId);
   		if (configurationBean != null) {
   			Long planOriginalId = getPlanTarifarioOriginal(strCustomerId, strPhone);
   			if (planOriginalId !=null) {
   				Float penaltyAmount =  Float.valueOf(configurationBean.getNpValue());
   				Float pricePlan = obtenerPrecioPlan(piAppId, planId);
   				Float originalPricePlan = obtenerPrecioPlan(piAppId, planOriginalId.longValue());

   				System.out.println("[validarPenalidadCambioDePlan] penalty::" + penaltyAmount + "||pricePlan::" + pricePlan + "||originalPricePlan:" + originalPricePlan);
   				if (pricePlan != null && originalPricePlan != null) {
   					if(originalPricePlan - pricePlan <= penaltyAmount){
   						hasPenalty = false;
   					}
   				}
   			}
   		}

   		return hasPenalty;
   	} 
   	
   	 /**
     * Motivo: Obtener Plan Tarifario de adenda activa
     * <br>Realizado por: <a href="mailto:carlos.diazoliva@tcs.com">Carlos Diaz</a>
     * <br>Fecha: 17/04/2017
     * @param     String strCustomerId  
     * @param     String strPhone       
     * @return    Long planId 
     */
   	public Long getPlanTarifarioOriginal(String strCustomerId, String strPhone) throws SQLException, Exception {
   		Long planId = null;
   		HashMap objHashMap = penaltyDAO.getPlanTarifarioPenalty(strCustomerId, strPhone);
   		String strMessage = (String) objHashMap.get(Constante.MESSAGE_OUTPUT);
   		if (strMessage == null) {
   			planId = (Long) objHashMap.get("planTarifarioId");
   		}
   		return planId;
   	}
   	
  	/**
    * Motivo: Obtener Variable de Configuracion del Monto de Penalidad de la Orden Cambio de Plan
    * <br>Realizado por: <a href="mailto:carlos.diazoliva@tcs.com">Carlos Diaz</a>
    * <br>Fecha: 17/04/2017
    * @param     String strSpecificationId  
    * @return    ConfigurationBean configurationBean 
    */
   	private ConfigurationBean obtenerConfiguracionMontoPenalidad(String strSpecificationId) throws SQLException, Exception{
   		ConfigurationBean configurationBean = null;
   		HashMap objHashMap = penaltyDAO.getConfigurationValues("MINIMUM_AMOUNT_PENALTY", null, strSpecificationId);
   		String strMessage = (String) objHashMap.get(Constante.MESSAGE_OUTPUT);
   		if (strMessage == null) {
   			 List<ConfigurationBean> listConfiguration = (List<ConfigurationBean>)objHashMap.get("objArrayList");
   			 if (!listConfiguration.isEmpty()) {
   				 configurationBean = listConfiguration.get(0);
   			 }
   		}
   		return configurationBean;
   	}
   	
  	/**
    * Motivo: Obtener Precio del Plan Tarifario
    * <br>Realizado por: <a href="mailto:carlos.diazoliva@tcs.com">Carlos Diaz</a>
    * <br>Fecha: 17/04/2017
    * @param     int piAppId  
    * @param     long planId       
    * @return    Float pricePlan 
    */
   	private Float obtenerPrecioPlan(int piAppId, long planId) throws SQLException, Exception {
   		Float pricePlan = null;
   		String[] pastrItemPlanIds = { String.valueOf(planId) };
   		ArrayList astrServicesTemp = new ArrayList();
   		astrServicesTemp.add(null);

   		HashMap objHashMap = objExceptionDAO.getAccessFee(piAppId, pastrItemPlanIds, astrServicesTemp);

   		String strMessage = (String) objHashMap.get(Constante.MESSAGE_OUTPUT);
   		if (strMessage == null) {
   			float[] arrRentFee = (float[]) objHashMap.get("afAccessFee");
   			if (arrRentFee != null && arrRentFee.length > 0) {
   				pricePlan = Float.valueOf(arrRentFee[0]);
   			}
   		}
   		return pricePlan;
   	}
    public void saveChangeItemPortability(RequestHashMap request, Connection conn, ItemBean itemBean, int i)  throws Exception, SQLException{

        System.out.println("INICIO [SectionItemEvents.saveChangeItemPortability] + orden: " + request.getParameter("hdnNumeroOrder"));

        String strMessage = null;

        String    pn_order_id                   = request.getParameter("hdnNumeroOrder"); /*********************************/
        String    pv_session_login              = request.getParameter("hdnSessionLogin");  /******************************/
        String[]  pv_item_pk                      = request.getParameterValues("hdnItemId");      /*******************************/
        String[]  pv_item_Product_Val             = request.getParameterValues("hdnItemValuetxtItemProduct");  /**********************/
        String[]  pv_item_PriceException_Val      = request.getParameterValues("hdnItemValuetxtItemPriceException");  /****************************/
        String[] pv_item_price_type                  = request.getParameterValues("txtItemPriceType");
        String[]  pv_item_PriceCtaInscrip_Val     = request.getParameterValues("hdnItemValuetxtItemPriceCtaInscrip");
        String[]  pv_item_PlanTarifario           = request.getParameterValues("txtItemRatePlan");


        //Numero Ordenc
        itemBean.setNporderid(MiUtil.parseLong(pn_order_id));    /**************************/

        //Product ID
        itemBean.setNpproductid(MiUtil.parseLong(MiUtil.getStringObject(pv_item_Product_Val,i)));         /***************************/

        //Precio Excepción
        itemBean.setNppriceexception(MiUtil.getString(MiUtil.getStringObject(pv_item_PriceException_Val,i)));       /************************************/

        itemBean.setNpcreateddate(MiUtil.getDateBD("dd/MM/yyyy"));   /*******************/
        itemBean.setNpcreatedby(pv_session_login);         /***********************/


        //Tipo Precio
        itemBean.setNppricetype(MiUtil.getString(MiUtil.getStringObject(pv_item_price_type,i)));          //*********************************//

        //Plan Tarifario
        itemBean.setNpplanname(MiUtil.getString(MiUtil.getStringObject(pv_item_PlanTarifario,i)));

        //precio
        itemBean.setNpprice(MiUtil.getString(MiUtil.getStringObject(pv_item_PriceCtaInscrip_Val,i)));

        itemBean.setNpitemid(MiUtil.parseLong(pv_item_pk[i]));         /*****************************/
        logger.info("[graba cambio en el Item]");
        strMessage = objItemDAO.doInsChangeItemPort(itemBean, conn);

        System.out.println("FIN [SectionItemEvents.saveChangeItemPortability] orden: " + request.getParameter("hdnNumeroOrder"));

    }

    /***********************************************************************************************************************************
     * <br>Realizado por: <a href="mailto:jorge.curi@tcs.com">JCURI</a>
     * <br>Fecha: 08/08/2017
     ************************************************************************************************************************************/
    public List<ApportionmentBean> getDataItemApportionment(RequestHashMap request, List<ItemBean> listItem)  throws Exception{ 
       	System.out.println("[INI] SectionItemEvents/getDataItemApportionment");   
       	List<ApportionmentBean> list = new ArrayList<ApportionmentBean>();
       	
           try {
           	String[]  pv_itemProrrateoTrxId= request.getParameterValues("itemProrrateoTrxId");
           	String[]  pv_itemProrrateoIndice= request.getParameterValues("itemProrrateoIndice");
               String[]  itemProrrateoPrice= request.getParameterValues("itemProrrateoPrice");
               String[]  itemProrrateoPriceIgv= request.getParameterValues("itemProrrateoPriceIgv");
               String[]  itemProrrateoRoundedPrice= request.getParameterValues("itemProrrateoRoundedPrice");
               String[]  itemProrrateoIgv= request.getParameterValues("itemProrrateoIgv");            
               String[]  itemProrrateoTemplateid= request.getParameterValues("itemProrrateoTemplateid");
               String[]  itemProrrateoCicloO= request.getParameterValues("itemProrrateoCicloO");
               String[]  itemProrrateoCicloD= request.getParameterValues("itemProrrateoCicloD");
               String[]  itemProrrateoQuantity= request.getParameterValues("itemProrrateoQuantity");
               
               if( pv_itemProrrateoIndice != null ){
                   int cantItems = pv_itemProrrateoIndice.length;
                   System.out.println("SectionItemEvents/getDataItemApportionment [cantItems] " + cantItems);
                   for(int i=0; i<cantItems; i++){
                   	String index = pv_itemProrrateoIndice[i];                	
                   	System.out.println("SEJBOrdenNewBean/ItemCalculoProrrateo -> [itemProrrateoIndice] " + index + " [itemProrrateoPrice] " + itemProrrateoPrice[i] +" [itemProrrateoTemplateid] "+ itemProrrateoTemplateid[i] + " [itemProrrateoCicloO] " +itemProrrateoCicloO[i] + "[itemProrrateoCicloD] " + itemProrrateoCicloD[i]+ " [itemProrrateoQuantity] " + itemProrrateoQuantity[i]);
                   	for(ItemBean item : listItem) {
                   		System.out.println("[indexApportionment] " + index + "[indexItem]" + item.getNphndIndice());
                   		if(index.equals(item.getNphndIndice())) {
                   			ApportionmentBean apportionment = new ApportionmentBean();
                   			apportionment.setTrxId(pv_itemProrrateoTrxId[i]);
                   			apportionment.setItemId(item.getNpitemid());
                   			apportionment.setTemplateId(itemProrrateoTemplateid[i]);
                   			apportionment.setCicloOrigen(itemProrrateoCicloO[i]);
                   			apportionment.setCicloDestino(itemProrrateoCicloD[i]);
                   			apportionment.setPrice(itemProrrateoPrice[i]);                			
                   			apportionment.setPriceIgv(itemProrrateoPriceIgv[i]);
                   			apportionment.setRoundedPrice(itemProrrateoRoundedPrice[i]);
                   			apportionment.setIgv(Double.parseDouble(itemProrrateoIgv[i]));
                   			apportionment.setQuantity(Integer.parseInt(itemProrrateoQuantity[i]));
                   			list.add(apportionment);
                   		}
                   	}
                   }                
               }
               
    		} catch (Exception e) {
    	         throw new Exception(e.getMessage());			
    		}
       	System.out.println("[END] SectionItemEvents/getDataItemApportionment [list] " + list.size());   
       	return list;
    }
    
    private ItemBean getDataItemForApportionment(ItemBean item) {
    	System.out.println("getDataItemForApportionment || [NphndIndice]" + item.getNphndIndice() + "[Npitemid()]" + item.getNpitemid());
    	ItemBean itemSave = new ItemBean();
    	itemSave.setNphndIndice(item.getNphndIndice());
    	itemSave.setNpitemid(item.getNpitemid());
    	return itemSave;
    }

}