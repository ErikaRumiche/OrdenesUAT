package pe.com.nextel.section.sectionMassiveItems;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.StringTokenizer;
import java.util.Vector;

import pe.com.nextel.bean.CoAssignmentBean;
import pe.com.nextel.bean.ItemBean;
import pe.com.nextel.bean.ItemServiceBean;
import pe.com.nextel.dao.BillingAccountDAO;
import pe.com.nextel.dao.ItemDAO;
import pe.com.nextel.dao.MassiveOrderDAO;
import pe.com.nextel.dao.OrderDAO;
import pe.com.nextel.util.MiUtil;
import pe.com.nextel.util.RequestHashMap;


public class SectionMassiveItemsEvents{

  MassiveOrderDAO objMassiveOrderDAO = new MassiveOrderDAO();
  BillingAccountDAO objBillingAccountDAO = new BillingAccountDAO();
  ItemDAO objItemDAO = new ItemDAO();
  
   
 /**
   * Motivo: 
   * <br>Realizado por: <a href="mailto:henry.rengifo@nextel.com.pe">Henry Rengifo</a>
   * <br>Fecha: 16/07/2009
   * @param     RequestHashMap request  
   * @param     Connection conn       
   * @return    String resultInsert
  */
  public String saveSectionChangeStructure(RequestHashMap request,Connection conn)  throws Exception, SQLException{
    System.out.println("=========== Inicio - Registro del ITEM ===========");
    String resultInsert =   "";    
    CoAssignmentBean objCoAssignmentBean = new CoAssignmentBean();
    
    /*System.out.println("request ===> ["+request);    */
    
    System.out.println("hdnUserId         --->  "+request.getParameter("hdnIUserId"));
    System.out.println("hdnSessionLogin   --->  "+request.getParameter("hdnSessionLogin"));
    System.out.println("hdnSpecification  --->  "+request.getParameter("hdnSpecification"));    
    System.out.println("txtCompanyId      --->  "+request.getParameter("txtCompanyId"));
 
    String   pn_customer_id                 = request.getParameter("txtCompanyId");
    String   pn_specification               = request.getParameter("hdnSpecification");    
    String   pn_order_id                    = request.getParameter("hdnNumeroOrder");    
    String   pn_user_id                     = request.getParameter("hdnIUserId");
    String   pv_session_login               = request.getParameter("hdnSessionLogin");
    String[] strTxtPhoneNumber              = request.getParameterValues("txtPhoneNumber");    
    String[] strChkPhoneNumber              = request.getParameterValues("hdnChkPhoneNumber");
    String[] strOrigResponsablePago         = request.getParameterValues("txtOrigResponsablePago");    
    //String[] cmbNewResponsablePago          = request.getParameterValues("hdnCmbNewResponsablePago");    
    String[] cmbNewResponsablePago          = request.getParameterValues("hdnTxtNewResponsablePago");
    String[] hdnNpcontractnumber            = request.getParameterValues("hdnNpcontractnumber");
    String[] hdnBscspaymntrespcustomeridId  = request.getParameterValues("hdnBscspaymntrespcustomeridId");
    
    objCoAssignmentBean.setNporderid(Long.parseLong(pn_order_id)); 
    
    System.out.println("strTxtPhoneNumber.length ==>"+strTxtPhoneNumber.length);
    for (int i = 0; i<strTxtPhoneNumber.length; i++){
        if (MiUtil.getStringNull(MiUtil.getStringObject(cmbNewResponsablePago,i))!= null){    
            System.out.println(i+" - txtPhoneNumber                ====> ["+MiUtil.getStringNull(MiUtil.getStringObject(strTxtPhoneNumber,i)));            
            System.out.println(i+" - cmbNewResponsablePago         ====> ["+MiUtil.getStringNull(MiUtil.getStringObject(cmbNewResponsablePago,i)));
            System.out.println(i+" - hdnNpcontractnumber           ====> ["+MiUtil.getStringNull(MiUtil.getStringObject(hdnNpcontractnumber,i)));
            System.out.println(i+" - hdnBscspaymntrespcustomeridId ====> ["+MiUtil.getStringNull(MiUtil.getStringObject(hdnBscspaymntrespcustomeridId,i)));
            System.out.println(i+" - strOrigResponsablePago        ====> ["+MiUtil.getStringNull(MiUtil.getStringObject(strOrigResponsablePago,i)));
                                        
             objCoAssignmentBean.setNpphone(MiUtil.getStringNull(MiUtil.getStringObject(strTxtPhoneNumber,i)));
             objCoAssignmentBean.setNpbscscontractId(MiUtil.getStringNull(MiUtil.getStringObject(hdnNpcontractnumber,i)));
             objCoAssignmentBean.setNpbscspaymntrespcustomeridId(MiUtil.getStringNull(MiUtil.getStringObject(hdnBscspaymntrespcustomeridId,i)));                    
             objCoAssignmentBean.setNpnewsiteid(MiUtil.getStringNull(MiUtil.getStringObject(cmbNewResponsablePago,i)));
                    
              resultInsert = objBillingAccountDAO.insAssignmentBillAccount(objCoAssignmentBean,conn);   
              System.out.println("resultInsert  == ["+resultInsert+"]");
              
        }        
    }
    System.out.println("=========== Fin - Registro del ITEM ===========");
    return resultInsert;
  }
  
  
   /**
    * Motivo: Evento de grabado de Items de la orden
    * <br>Realizado por: <a href="mailto:evelyn.ocampo@nextel.com.pe">Evelyn Ocampo</a>
    * <br>Fecha: 10/07/2009
    * @param     RequestHashMap request  
    * @param     Connection conn       
    * @return    String resultInsertItems 
    */
  public String saveSectionSSAA(RequestHashMap request,Connection conn)  throws Exception, SQLException{
    
    String   pn_customer_id                 = request.getParameter("txtCompanyId");
    String   pn_specification               = request.getParameter("hdnSpecification");    
    String   pn_order_id                    = request.getParameter("hdnNumeroOrder");
    String   pn_user_id                     = request.getParameter("hdnIUserId");
    String   pv_session_login               = request.getParameter("hdnSessionLogin");
    
    System.out.println("=========== Inicio - Registro del ITEM ===========");
    String resultInsertItems          =   "";
      
    resultInsertItems           = grabarItemsMassiveSSAA(request,conn);
    
    System.out.println("=========== Fin - Registro del ITEM ===========");
                                            
    return resultInsertItems;
  }
  
  /**
  * Motivo: Registra los Items, Servicios Adicionales, Imeis , 
  * <br>Realizado por: <a href="mailto:evelyn.ocampo@nextel.com.pe">Evelyn Ocampo</a>
  * <br>Fecha: 10/07/2009
  * @param     RequestHashMap request
  * @param     Connection conn
  * @return    String resultInsertItems
  */
  public String grabarItemsMassiveSSAA(RequestHashMap request, Connection conn) throws Exception, SQLException{
    System.out.println("=========== ENTRA A GRABAR-> -> ->");
    String    resultTransaction             = "";
    String[]  pv_item_cantidad              = request.getParameterValues("hdnIndice");
    String[]  pv_item_messajeSSAA           = request.getParameterValues("hdnItemMsjResSSAA");
    String    resultValidateItems           ="";
    //DATOS PARA ITEMS
    ItemBean  itemBean    =   new ItemBean();
    String    strMessageImei  =   null;
    System.out.println("CANTIDAD==============="+pv_item_cantidad);
    if( pv_item_cantidad != null ){
      int cantItems  = Integer.parseInt(MiUtil.getStringObject(pv_item_cantidad,0));
      
      OrderDAO objOrderDAO = new OrderDAO();
      
      /*resultValidateItems = validateItems(request,conn,cantItems);
      if (resultValidateItems != null)
        return resultValidateItems;*/
      
      for(int i=0; i<cantItems; i++){      
        System.out.println("=========== Inicio - Registro del ITEM #Orden-> "+cantItems+" Usuario->");
        System.out.println("i======"+i);
        System.out.println("primerrooo======"+MiUtil.getStringObject(pv_item_messajeSSAA,i));
        if (MiUtil.getStringNull(MiUtil.getStringObject(pv_item_messajeSSAA,i)).equals("SUCCESS")){  
          //Insertar ITEMS
          resultTransaction  = grabarItemMassive(request,conn,itemBean,i,"INSERT");
          
          if( resultTransaction!= null)
              return resultTransaction;
          
          String itemIdSave = ""+itemBean.getNpitemid();
          
          /*resultTransaction  = grabarItemsBillingAccount(request,conn,itemIdSave,i); 
          
          if( resultTransaction!= null)
              return resultTransaction;*/
          
          resultTransaction  = grabarItemsMassiveServices(request,conn,itemIdSave,i); 
          
          if( resultTransaction!= null)
              return resultTransaction;
                              
          /*
          resultTransaction = grabarItemsImeis(request,conn,itemIdSave,i);
                
          if( resultTransaction!= null)
              strMessageImei =MiUtil.getString(strMessageImei) + " "+ resultTransaction;
          */       
        }//Fin del IF 
     }//Fin del For
     /*if ( !MiUtil.getString(strMessageImei).equalsIgnoreCase("") )
      return strMessageImei;*/
        
   }//Fin del If
          
   return null;
   
  }
  
  
  /**
    * Motivo: Evento de grabado de Items de la orden Plan Tarifario
    * <br>Realizado por: <a href="mailto:magally.mora@nextel.com.pe">Magally Mora</a>
    * <br>Fecha: 10/07/2009
    * @param     RequestHashMap request  
    * @param     Connection conn       
    * @return    String resultInsertItems 
    */
  public String saveSectionPlan(RequestHashMap request,Connection conn)  throws Exception, SQLException{
    
    System.out.println("request Plan ===> ["+request);
    
    System.out.println("hdnUserId        --->  "+request.getParameter("hdnIUserId"));
    System.out.println("hdnSessionLogin  --->  "+request.getParameter("hdnSessionLogin"));
    System.out.println("hdnSpecification  --->  "+request.getParameter("hdnSpecification"));    
    System.out.println("txtCompanyId  --->  "+request.getParameter("txtCompanyId"));
    
    String   pn_customer_id                 = request.getParameter("txtCompanyId");
    String   pn_specification               = request.getParameter("hdnSpecification");    
    String   pn_order_id                    = request.getParameter("hdnNumeroOrder");
   //String   pn_user_id                     = request.getParameter("hdnUserId");
    String   pn_user_id                     = request.getParameter("hdnIUserId");
    String   pv_session_login               = request.getParameter("hdnSessionLogin");
    
    System.out.println("=========== Inicio - Registro del ITEM ===========");
    String resultInsertItems          =   "";
    
    resultInsertItems           = grabarItemsMassivePlan(request,conn);
    System.out.println("=========== Fin - Registro del ITEM ===========");
  
   return resultInsertItems;
  }
  
  
     /**
  * Motivo: Registra los Items, Plan Tarifario 
  * <br>Realizado por: <a href="mailto:magally.mora@nextel.com.pe">Magally Mora</a>
  * <br>Fecha: 10/07/2009
  * @param     RequestHashMap request  
  * @param     Connection conn       
  * @return    String resultInsertItems 
  */
  public String grabarItemsMassivePlan(RequestHashMap request, Connection conn) throws Exception, SQLException{
    System.out.println("=========== ENTRA A GRABAR PLAN-> -> ->");
    String    resultTransaction             = "";
    String[]  pv_item_cantidad              = request.getParameterValues("hdnIndice");
    String[]  pv_item_messajeSSAA           = request.getParameterValues("txtItemMsjResSSAA");
    String[] strPlan                        = request.getParameterValues("hdnItemNewPlanId");
    String[] strChkPhone                    = request.getParameterValues("hdnchkPhone");
    String   resultValidateItems           ="";
    //DATOS PARA ITEMS
    ItemBean  itemBean    =   new ItemBean();
    String    strMessageImei  =   null;
    System.out.println("CANTIDAD==============="+pv_item_cantidad);
    //if( pv_item_cantidad != null ){
      //int cantItems  = Integer.parseInt(MiUtil.getStringObject(pv_item_cantidad,0));
  
      OrderDAO objOrderDAO = new OrderDAO();
       //for(int i=0; i<cantItems; i++){
       for (int i = 0; i<strPlan.length; i++){
       // System.out.println("=========== Inicio - Registro del ITEM #Orden-> "+cantItems+" Usuario->");
        
        System.out.println("Plan==============="+strPlan);
       
       if (!MiUtil.getStringNull(MiUtil.getStringObject(strPlan,i)).equals("0")){ 
        //if (MiUtil.getStringNull(MiUtil.getStringObject(strChkPhone,i)).equals("on")){  
      
          //Insertar ITEMS
          resultTransaction  = grabarItemMassive(request,conn,itemBean,i,"INSERT");
          
          if( resultTransaction!= null)
              return resultTransaction;
         // if (MiUtil.getStringNull(MiUtil.getStringObject(pv_item_messajeSSAA,i)).equals("SUCESS")){     
          String itemIdSave = ""+itemBean.getNpitemid();
          resultTransaction  = grabarItemsMassiveServices(request,conn,itemIdSave,i); 
          
          if( resultTransaction!= null)
              return resultTransaction;
                              
         //}//Fin del IF  
        }//Fin del IF 
     }//Fin del For
     
 //}//Fin del If
          
   return null;
   
  }
  
  
  public String grabarItemMassive(RequestHashMap request, Connection conn, ItemBean itemBean, int i, String operatorTransaction)  throws Exception, SQLException{
      
    String    pn_order_id                   = request.getParameter("hdnNumeroOrder");
    String    pv_session_login              = request.getParameter("hdnSessionLogin");
     
    if( operatorTransaction.equals("INSERT") ){
      System.out.println("=========== Inicio - Registro del ITEM #Orden Masiva-> "+pn_order_id+" Usuario-> "+pv_session_login+"===========");
    }
    

    String[]  pv_item_modality_val            = request.getParameterValues("hdnItemModality");    
     
    String[]  pv_item_solution                = request.getParameterValues("txtItemSolution");
    String[]  pv_item_solution_Val            = request.getParameterValues("hdnItemSolutionId");


    String[]  pv_item_model_val               = request.getParameterValues("txtItemModel");

     
    String[]  pv_item_Phone_val               = request.getParameterValues("txtItemPhoneNumber");

    String[]  pv_item_Sim_Val                 = request.getParameterValues("hdnItemSimNumber");
      
    String[]  pv_item_Imei_Val                = request.getParameterValues("hdnItemImeiNumber");
      
    //String[]  pv_item_Product_Val             = request.getParameterValues("hdnItemNewProductId");

    //String[]  pv_item_Product_Line_Val        = request.getParameterValues("txtItemProductLineId");    
    String[]  pv_item_Product_Val             = request.getParameterValues("hdnItemNewProductId");

    String[]  pv_item_Product_Line_Val        = request.getParameterValues("hdnItemNewProductLineId");  
      
    String[]  pv_item_PlanTarifario_Val       = request.getParameterValues("hdnItemOrigPlanId");
    
    String[]  pv_item_PlanTarifarioName_Val   = request.getParameterValues("txtItemOrigPlan"); 

    String[]  pv_item_Equipment_Val           = request.getParameterValues("hdnItemEquipment");
             

    String[]  pv_item_Currency_Val            = request.getParameterValues("hdnItemCurrency");
      
    String[]  pv_item_Quantity_Val            = request.getParameterValues("hdnItemQuantity");
      
    String[]  pv_item_services_Val            = request.getParameterValues("hdnItemServicesNew");
    
    
    String[]  pv_item_original_productid_Val  = request.getParameterValues("hdnItemOrigProductId");
    
    String[]  pv_item_flag_save               = request.getParameterValues("hdnFlagSave");
    
    String[]  pv_item_PriceCtaInscrip_Val     = request.getParameterValues("txtItemPrice");
    
    String[]  pv_item_PriceException_Val      = request.getParameterValues("txtItemPriceExc");
    
    String[]  pv_item_original_price           = request.getParameterValues("hdnOriginalPrice");
    String[]  pv_item_price_type               = request.getParameterValues("hdnPriceType");
    String[]  pv_item_price_typeId             = request.getParameterValues("hdnPriceTypeId");
    String[]  pv_item_price_type_itemId        = request.getParameterValues("hdnItemPriceTypeId");
    
    String[]  pv_item_NewPlanTarifarioId_Val   = request.getParameterValues("hdnItemNewPlanId");
    String[]  pv_item_NewPlanTarifarioName_Val = request.getParameterValues("hdnItemNewPlanNameId");
    
    //String[]  pv_item_NewPlanTarifarioId       = request.getParameterValues("hdncmbNewPlanTarifario");
  
    

    //Numero Orden
    itemBean.setNporderid(MiUtil.parseLong(pn_order_id));
    //Modalidad Salida
    itemBean.setNpmodalitysell(MiUtil.getStringObject(pv_item_modality_val,i));
    //Solución
    itemBean.setNpsolutionid(MiUtil.parseLong(MiUtil.getStringObject(pv_item_solution_Val,i)));
    //Numero del Teléfono
    itemBean.setNpphone(MiUtil.getStringObject(pv_item_Phone_val,i));
    //Numero del Nuevo Teléfono
    //itemBean.setNpnewphone(MiUtil.getStringObject(pv_item_NewPhone_Val,i));
    itemBean.setNpnewphone("");
    //SIM Generico
    //itemBean.setNpownimeinumber(MiUtil.getStringObject(pv_item_generic_Val,i));
    itemBean.setNpownimeinumber("");
    //Numero Serial
    //itemBean.setNpserialnumber(MiUtil.getStringObject(pv_item_Sim_Val,i));
    itemBean.setNpserialnumber("");
    //Numero Imei
    itemBean.setNpimeinumber(MiUtil.getStringObject(pv_item_Imei_Val,i));
    //Numero Sim
    itemBean.setNpsiminumber(MiUtil.getStringObject(pv_item_Sim_Val,i));
    //Modelo
    itemBean.setNpmodel(MiUtil.getStringObject(pv_item_model_val,i));
    
    //if ((!itemBean.getNpexceptionrevenue().equals("")))
    //Plan Tarifario Nuevo
    itemBean.setNpplanid(MiUtil.parseLong(MiUtil.getStringObject(pv_item_NewPlanTarifarioId_Val,i)));
    //Nombre Plan Tarifario Nuevo
    itemBean.setNpplanname(MiUtil.getStringObject(pv_item_NewPlanTarifarioName_Val,i));
    //Linea Producto
    itemBean.setNpproductlineid(MiUtil.parseLong(MiUtil.getStringObject(pv_item_Product_Line_Val,i)));
    //Product ID
    itemBean.setNpproductid(MiUtil.parseLong(MiUtil.getStringObject(pv_item_Product_Val,i)));
    //Cantidad
    itemBean.setNpquantity(MiUtil.parseInt(MiUtil.getStringObject(pv_item_Quantity_Val,i).equals("")?"1":MiUtil.getStringObject(pv_item_Quantity_Val,i)));
    //Producto Original
    itemBean.setNporiginalproductid(MiUtil.parseLong(MiUtil.getStringObject(pv_item_original_productid_Val,i)));
    //itemBean.setNpprice("");
    itemBean.setNpprice(MiUtil.getString(MiUtil.getStringObject(pv_item_PriceCtaInscrip_Val,i)));
    //Precio Excepción
    itemBean.setNppriceexception(MiUtil.getString(MiUtil.getStringObject(pv_item_PriceException_Val,i)));
    //Precio Renta
    itemBean.setNprent("");
    //Precio Descuento
    itemBean.setNpdiscount("");
    //Tipo Moneda
    itemBean.setNpcurrency(MiUtil.getStringObject(pv_item_Currency_Val,i));
    //Responsable Area
    /*itemBean.setNparearespdev(MiUtil.parseLong(MiUtil.getStringObject(pv_item_ResponsibleArea_Val,i)));
    //Responsable Devolución
    itemBean.setNpprovidergrpiddev(MiUtil.parseLong(MiUtil.getStringObject(pv_item_ResponsibleDev_Val,i)));*/
    itemBean.setNparearespdev(0);
    //Responsable Devolución
    itemBean.setNpprovidergrpiddev(0);
    
    itemBean.setNpwarrant("");
    itemBean.setNpoccurrence(0);
    itemBean.setNppromotionid(0);
    itemBean.setNpaddendumid(0);
    itemBean.setNpinventorycode("");
    itemBean.setNpconceptid(0);
   
    itemBean.setNporiginalplanid(MiUtil.parseLong(MiUtil.getStringObject(pv_item_PlanTarifario_Val,i)));
    itemBean.setNporiginalplanname(MiUtil.getStringObject(pv_item_PlanTarifarioName_Val,i));     
    
    //Producto Original
    itemBean.setNporiginalproductid(MiUtil.parseLong(MiUtil.getStringObject(pv_item_original_productid_Val,i)));
    itemBean.setNpequipment(MiUtil.getStringObject(pv_item_Equipment_Val,i));
         
    itemBean.setNpequipmentreturn("");
    itemBean.setNpequipmentnotyetgiveback("");
    itemBean.setNpequipmentreturndate(MiUtil.toFecha("","dd/MM/yyyy"));         
    itemBean.setNpexception("");         
    itemBean.setNpexceptionrevenue("");
    itemBean.setNpexceptionrevenuediscount(0);
    itemBean.setNpexceptionrent("");
    itemBean.setNpexceptionrentdiscount(0);
    itemBean.setNpexceptionminadidispatch("");
    itemBean.setNpexceptionminaditelephony("");
    itemBean.setNpinstalationexception("");
         
             
    itemBean.setNpmodificationdate(MiUtil.getDateBD("dd/MM/yyyy"));
    itemBean.setNpmodificationby(pv_session_login);
    itemBean.setNpcreateddate(MiUtil.getDateBD("dd/MM/yyyy"));
    itemBean.setNpcreatedby(pv_session_login);
    
    /**Banda Ancha**/
         
    /**(Inicio) Acceso de Datos/Enlace de Datos**/
    itemBean.setNpsharedinstal("");
    itemBean.setNpcontractnumber(0);
    itemBean.setNpfirstcontract("");
    //Instalación Compartida
    itemBean.setNpsharedinstalationid(0);
    //Dirección de Instalación
    itemBean.setNpinstallationaddressid(0);
         
    itemBean.setNpinstalationprice("");
    itemBean.setNpinstalationexception("");
         
    itemBean.setNplinktype("");
    itemBean.setNpnetworkhosttype(0);
      
    itemBean.setNpfeasibilityprogdate(MiUtil.toFechaHora("","dd/MM/yyyy HH:mm")); 
    itemBean.setNpfeasibility("");
    itemBean.setNpinstalation("");
         
    itemBean.setNpinstalationprogdate(MiUtil.toFechaHora("","dd/MM/yyyy HH:mm"));
         
         
    /**(Fin) Acceso de Datos/Enlace de Datos**/
         
    /**(Inicio) Traslado**/
    itemBean.setNpnewaddress(0);
    itemBean.setNpcontactname("");
    itemBean.setNpphonenumber1("");
    itemBean.setNpphonenumber2("");
    itemBean.setNpaditionalcost("");
    itemBean.setNpdescription("");
    /**(Fin) Traslado**/
         
         
    /**(Inicio) Cambio de Plan Tarifario**/
    itemBean.setNporigmainservice(0);
    itemBean.setNpnewmainservice(0);
    /**(Fin) Cambio de Plan Tarifario**/
         
    /**Banda Ancha**/
         
    //Bolsa 
    itemBean.setNpminutesrate("");
         
         
    /**Inicio - Servicio de locución**/ 
    itemBean.setNpendservicedate(MiUtil.toFecha("","dd/MM/yyyy"));
    itemBean.setNpreferencephone("");
    /**Fin    - Servicio de locución**/
         

    /**Inicio Acuerdos Comerciales**/         

    //Precio Original
    //itemBean.setNporiginalprice("");
    //Tipo Precio 
    //itemBean.setNppricetype("");
    //Código Tipo Precio 
    //itemBean.setNppricetypeid(0);
    //Código del Item del Tipo Precio 
    //itemBean.setNppricetypeitemid(0);
    
    //Precio Original
    itemBean.setNporiginalprice(MiUtil.getString(MiUtil.getStringObject(pv_item_original_price,i)));
    //Tipo Precio 
    itemBean.setNppricetype(MiUtil.getString(MiUtil.getStringObject(pv_item_price_type,i)));
    //Código Tipo Precio 
    itemBean.setNppricetypeid(MiUtil.parseLong(MiUtil.getStringObject(pv_item_price_typeId,i)));
    //Código del Item del Tipo Precio 
    itemBean.setNppricetypeitemid(MiUtil.parseLong(MiUtil.getStringObject(pv_item_price_type_itemId,i)));   
       
    /**Fin Acuerdos Comerciales**/

    itemBean.setNpfixedphone("");
    itemBean.setNplocution("");  
       
    /*Inicio Responsable Pago*/
    itemBean.setNpsiteid(0);         
    /*Fin Responsable Pago*/
         
    /*Inicio Contratos de Referencia*/
    itemBean.setNpinternetrefcontract(0);
    itemBean.setNptfrefphonenumber("");
    itemBean.setNpdatosrefcontract(0);
    /*Fin Contratos de Referencia*/
         
    itemBean.setNptypeip(0);
    itemBean.setNpestadoitemDesc("");
    itemBean.setNpestadoitemId("");                
    itemBean.setNpestadoproceso("");         
    /**Fin Suspención definitiva**/
         
      
    String strMessage = null;
    if( operatorTransaction.equals("INSERT") ){
       strMessage = objMassiveOrderDAO.doSaveItemMassive(itemBean,conn);              
    }
      
    if( operatorTransaction.equals("INSERT") ){
      System.out.println("=========== Fin - Registro del ITEM #Orden-> "+pn_order_id+" Usuario-> "+pv_session_login+"=========== Mensaje -> " +strMessage);
    }
       
    return strMessage;
  }
  
  public String grabarItemsMassiveServices(RequestHashMap request, Connection conn, String itemID, int index)  throws Exception, SQLException{
      
    String    resultTransaction          = "";
    String[]  pv_item_services           = request.getParameterValues("hdnItemServicesNew");
    
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
          itemServiceBean.setNpservicefree("");
          itemServiceBean.setNpserviceprice(0);
                  
          resultTransaction = objMassiveOrderDAO.getItemMassiveServiceInsertar(itemServiceBean,conn);
          
          if( resultTransaction!=null )
            return resultTransaction;
      }
    }
    return null;
  }
  
  /**
   * Motivo: Evento de grabado de Items de la orden para la sección dinámica (categoría : Transferencia de Equipos)
   * <br>Realizado por: <a href="mailto:evelyn.ocampo@nextel.com.pe">Evelyn Ocampo</a>
   * <br>Fecha: 03/08/2009
   * @param     RequestHashMap request  
   * @param     Connection conn       
   * @return    String resultInsertItems 
   */
  public String saveSectionTransfer(RequestHashMap request,Connection conn)  throws Exception, SQLException{
    
    String   pn_customer_id                 = request.getParameter("txtCompanyId");
    String   pn_specification               = request.getParameter("hdnSpecification");    
    String   pn_order_id                    = request.getParameter("hdnNumeroOrder");
    String   pn_user_id                     = request.getParameter("hdnIUserId");
    String   pv_session_login               = request.getParameter("hdnSessionLogin");
    
    System.out.println("=========== Inicio - Registro del ITEM - Tranferencia ===========");
    String resultInsertItems          =   "";
      
    resultInsertItems           = grabarItemsMassiveTransfer(request,conn);
    
    System.out.println("=========== Fin - Registro del ITEM - Tranferencia ===========");
                                            
    return resultInsertItems;
  }
  
  /**
  * Motivo: Registra los Items, Servicios Adicionales, Imeis , 
  * <br>Realizado por: <a href="mailto:evelyn.ocampo@nextel.com.pe">Evelyn Ocampo</a>
  * <br>Fecha: 03/08/2009
  * @param     RequestHashMap request
  * @param     Connection conn
  * @return    String resultInsertItems
  */
  public String grabarItemsMassiveTransfer(RequestHashMap request, Connection conn) throws Exception, SQLException{
    System.out.println("=========== ENTRA A GRABAR-> -> ->");
    String    resultTransaction             = "";
    String[]  pv_item_cantidad              = request.getParameterValues("hdnIndice");
    String[]  pv_item_itemLine           = request.getParameterValues("hdnItemLine");
    String    resultValidateItems           ="";
    //DATOS PARA ITEMS
    ItemBean  itemBean    =   new ItemBean();
    String    strMessageImei  =   null;
    System.out.println("CANTIDAD==============="+pv_item_cantidad);
    
    if( pv_item_cantidad != null ){
      int cantItems  = Integer.parseInt(MiUtil.getStringObject(pv_item_cantidad,0));
      
      OrderDAO objOrderDAO = new OrderDAO();
      
    
      for(int i=0; i<cantItems; i++){      
        System.out.println("=========== Inicio - Registro del ITEM #Orden-> "+cantItems+" Usuario->");
        System.out.println("i======"+i);
        System.out.println("primerrooo======"+MiUtil.getStringObject(pv_item_itemLine,i));
        if (MiUtil.getStringNull(MiUtil.getStringObject(pv_item_itemLine,i)).equals("Transferencia")){  
          //Insertar ITEMS
          resultTransaction  = grabarItemMassive(request,conn,itemBean,i,"INSERT");
          
          if( resultTransaction!= null)
              return resultTransaction;
          
          String itemIdSave = ""+itemBean.getNpitemid();
                    
          resultTransaction  = grabarItemsMassiveServices(request,conn,itemIdSave,i); 
          
          if( resultTransaction!= null)
              return resultTransaction;                              
     
        }//Fin del IF 
     }//Fin del For
        
   }//Fin del If
          
   return null;
   
  }
   
}