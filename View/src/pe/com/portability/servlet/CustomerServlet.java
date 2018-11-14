package pe.com.portability.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import pe.com.nextel.bean.DominioBean;
import pe.com.nextel.bean.SiteBean;
import pe.com.nextel.bean.SpecificationBean;
import pe.com.nextel.ejb.SEJBCustomerRemote;
import pe.com.nextel.service.BillingAccountService;
import pe.com.nextel.service.CustomerService;
import pe.com.nextel.service.GeneralService;
import pe.com.nextel.service.NewOrderService;
import pe.com.nextel.service.SiteService;
import pe.com.nextel.servlet.GenericServlet;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;


public class CustomerServlet extends GenericServlet {
   
   private static final String CONTENT_TYPE       = "text/html; charset=UTF-8";
   private static final String SEARCH_BY_TELEFONO   = "1";
   private static final String SEARCH_BY_CONTRACT = "2";
   private static final String SEARCH_BY_IMEI     = "3";
   private static final String SEARCH_BY_SIM      = "4";
   private static final String SEARCH_BY_RADIO    = "5";
   private static final String CODE_APPLICATION   = "201";
  
   CustomerService objCustomerService = new CustomerService();
    
   public void init(ServletConfig config) throws ServletException {
        super.init(config);
   }
    
   public void doGet(HttpServletRequest request, 
                      HttpServletResponse response) throws ServletException, IOException {
                      
      response.setContentType(CONTENT_TYPE);
      PrintWriter out = response.getWriter();
      HashMap h = new HashMap();
      // Variables para estandar.              
      String  strMyaction         = null;
      String  strDetalleMyaction  = null;
      String  strSiteId           = "";
      String  strCreatedby        = request.getParameter("hdnSessionCreatedby");
      long    intUserid           = MiUtil.parseLong((String)request.getParameter("hdnSessionUserid"));
      long    intRegionid         = MiUtil.parseLong((String)request.getParameter("hdnSessionRegionId"));
      int     intAppid            = MiUtil.parseInt((String)request.getParameter("hdnSessionRolId"));
      int    hdnIUserId           = MiUtil.parseInt((String)request.getParameter("hdnIUserId"));
      int    hdnIAppId            = MiUtil.parseInt((String)request.getParameter("hdnIAppId"));

      System.out.println("hdnIUserId : " + hdnIUserId);
      System.out.println("hdnIAppId : " + hdnIAppId); 
      
      try{
      
      out.println("<html>");
      out.println("<head><title>CustomerServlet</title></head>");
       
      out.println("<script language='JavaScript' src='"+Constante.PATH_APPORDER_SERVER+"/Resource/BasicOperations.js'></script>");
      out.println("<script language='JavaScript'>");
      
      //Captura la acción a realizar.
      strMyaction         = request.getParameter("myaction");
      strDetalleMyaction  = request.getParameter("detallemyaction");
       
      // Bucle estandar de seleccion de acciones.
        if ( strMyaction != null ) {
            
            //Acción: buscarCliente 
            if(strMyaction.equals("buscarCliente")) {
            
              //Buscar por tipo de dato ( Teléfono, Contrato, IMEI )
              if(strDetalleMyaction.equals("bpel")) {
                doSearchCustomerByData(request,out);
              //Buscar por descripción de la compañía
              }else if(strDetalleMyaction.equals("compania")) {
                doSearchCustomerByDescription(request,out);
              //Buscar por el id de la compañía
              }else if( strDetalleMyaction.equals("id") ) {
                doSearchCustomerById(request,out);
              //Buscar por el ruc de la compañía
              }else if( strDetalleMyaction.equals("ruc") ) {
                doSearchCustomerByRuc(request,out);
              }//KSALVADOR/
              else if( strDetalleMyaction.equals("detail") ) {
                doSearchCustomerByDetail(request,out);
              }
            
            }
            //Limpia los datos de la orden
            else if(strMyaction.equals("limpiarCliente")) {
              clearCustomer(request, out);
            }
            //Genera las direcciones por Site 
            else if(strMyaction.equals("mostrarDireccionesSite")) {
              strSiteId = (String)request.getParameter("cmbResponsablePago");
              searchCustomerBySite(strSiteId,request,out);
            }else if(strMyaction.equals("loadBillingAccountBySiteId")) {
              loadBillingAccountBySiteId(request,response);
            }
            
        }
      
      }catch(Exception ex){
        //ex.printStackTrace();
        System.out.println("Customer Servlet : " + ex.getClass() + " - "+ ex.getMessage());
        String variable = ""+ex.getMessage()+"";
        variable  = (MiUtil.getMessageClean(variable)).replaceAll("\\u000a"," ");
        out.println("alert(\""+variable+"\");");
        //Al haberse encontrado un error. Se debería de deshabilitar la búsqueda
        //del cliente si se tratara de una OPP o INC
      }finally{
        out.println("location.replace('"+Constante.PATH_APPORDER_SERVER+"/Bottom.html');");
        out.println("</script>");
        out.close();
      }
      
    }

  public void doPost(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException {
    
    response.setContentType(CONTENT_TYPE);
    doGet(request, response);
  }
    
    
  /**
  Method : doSearchCustomerByDescription
  Purpose: Genera una busqueda del cliente por su descripción y si este coincide con 
  un acierto se procede a mostrar la información indicada. En caso contrario
  se expone una ventana flotante donde debe elegir cual de todos los aciertos
  es el indicado
  Developer       Fecha       Comentario
  =============   ==========  ======================================================================
  Lee Rosales     25/02/2008  Creación
  */
  public void doSearchCustomerByDescription(HttpServletRequest request, PrintWriter out) throws Exception{
  
    HashMap   hshCutomer = new HashMap();
    HashMap   hshValidationCustomer= new HashMap();
    String    strLogin;
    int       intRegionId;
    String    strMessage = null;
    
    String    strCustomerDescription;    
    strCustomerDescription    = (String)request.getParameter("hdnCompanyName");
    strLogin                  = (String)request.getParameter("hdnSessionLogin");
    intRegionId               = MiUtil.parseInt(request.getParameter("hdnRegionId"));
    int    iUserId            = MiUtil.parseInt((String)request.getParameter("hdnIUserId"));
    int    iAppId             = MiUtil.parseInt((String)request.getParameter("hdnIAppId"));
    String strGeneratorTypeRepCalcap = (String)request.getParameter("hdnOrigenType");
    String strGeneratorId     = (request.getParameter("hdnGeneratorId")==null?"":request.getParameter("hdnGeneratorId"));      
    String strGeneratortype  = request.getParameter("hdngeneratortype");
    System.out.println("[CustomerSevlet][doSearchCustomerByDescription][intRegionId]"+intRegionId);
    System.out.println("[CustomerSevlet][doSearchCustomerByDescription][strGeneratortype]"+strGeneratortype);
    clearCustomer(request,out);
    // Regla: 
    // Primero hacer invocacion a procedimiento q muestra el numero de coincidencias.
    // dependiendo de eso envia el popup
    Hashtable hshCustPattern = objCustomerService.CustomerDAOgetCustPatternQty(strCustomerDescription);
    
    strMessage = (String)hshCustPattern.get("strMessage");
    
    strMessage = strMessage.equals("null")?null:strMessage;
    
    if( strMessage!=null ){
        throw new Exception(strMessage);
    }

    hshValidationCustomer=objCustomerService.getValidationCustomer((long)iUserId,MiUtil.parseLong((String)hshCustPattern.get("wn_customerid")));
    strMessage = (String)hshValidationCustomer.get(Constante.MESSAGE_OUTPUT);
    if( strMessage!=null ){
        throw new Exception(strMessage);
    }
        
    System.out.println("CBARZOLA-->Respuesta"+MiUtil.parseInt((String)hshValidationCustomer.get("respuesta")));
     //Si solo hubo un encuentro 
     if ( MiUtil.parseInt((String)hshCustPattern.get("wn_customer_qty")) == 1 && (  (strGeneratortype.equals(Constante.GENERATOR_TYPE_OPP))|| (MiUtil.parseInt((String)hshValidationCustomer.get("respuesta"))== 1 ) ) ) {         
        hshCutomer = objCustomerService.CustomerDAOgetCustomerData( MiUtil.parseLong((String)hshCustPattern.get("wn_customerid")), strLogin, MiUtil.parseLong(""+intRegionId),iUserId,iAppId,strGeneratorId,strGeneratorTypeRepCalcap);
        if( hshCutomer != null ){
          if( (String)hshCutomer.get("strMessage") != null )
            throw new Exception((String)hshCutomer.get("strMessage"));
          else          
            showCustomerDetail(hshCutomer,request,out);
        }
     }
     //Si hay mas de dos encuentros
     else {
        out.println("alert('Especifique mejor su búsqueda.');");
        out.println("parent.mainFrame.searchCustomer()");
     }
  }
    
   /**
   Method : doSearchCustomerByData
   Purpose: Genera una busqueda del cliente tomando como filtro el Teléfono,
   Contrato o IMEI ingresado por el usuario. En caso contrario
   se expone una ventana flotante donde debe elegir cual de todos los aciertos
   es el indicado o especificar su búsqueda
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Lee Rosales     25/02/2008  Creación
   JPEREZ          20/05/2008  Al encontrar más de un resultado se levanta el listado de compañías
   TMOGROVEJO      21/09/2008  Se modificaron las referencias del array de busquedas clientes
   KSALVADOR       17/11/2009  Se modifica y agrega una condicion para los combos de valores Busqueda (SEARCH_BY_RADIO)
   */
   public void doSearchCustomerByData(HttpServletRequest request, PrintWriter out) throws Exception{
  
    String    strTipoBusqueda,strLogin;
    String    strNumeroTelefono = null,strNumeroContrato = null,strNumeroIMEI = null,strNumeroSIM = null,strNumeroRadio=null;
    String    strMessage = null;
    int       intRegionId;
    HashMap   hshCutomer = new HashMap();
    HashMap   hshValidationCustomer = new HashMap();
    strTipoBusqueda = request.getParameter("cmbTipoBusqueda");
    strLogin        = request.getParameter("hdnSessionLogin");
    int iUserId     = MiUtil.parseInt((String)request.getParameter("hdnIUserId"));
    int iAppId      = MiUtil.parseInt((String)request.getParameter("hdnIAppId"));
    intRegionId     = MiUtil.parseInt(request.getParameter("hdnRegionId"));
    String strGeneratorTypeRepCalcap = (String)request.getParameter("hdnOrigenType");
    String strGeneratorId     = (request.getParameter("hdnGeneratorId")==null?"":request.getParameter("hdnGeneratorId")); 
    String strGeneratortype  = request.getParameter("hdngeneratortype");
    System.out.println("[CustomerServlet][doSearchCustomerByData]strLogin: "+strLogin);
     /* La variable strTipoBusqueda, tiene los valores del combo box cmbTipoBusqueda */
     //Busqueda por Nextel     
     if(strTipoBusqueda.equals(SEARCH_BY_TELEFONO)  ) {
        GeneralService    objGeneralService  = new GeneralService();
        strNumeroTelefono = objGeneralService.GeneralDAOgetWorldNumber(request.getParameter("txtDato"),"COUNTRY");
     }
     //Busqueda por Contrato
     else if(strTipoBusqueda.equals(SEARCH_BY_CONTRACT)  ) {
         strNumeroContrato = request.getParameter("txtDato");
     }
     //Busqueda por IMEI
     else if(strTipoBusqueda.equals(SEARCH_BY_IMEI)  ) {
         strNumeroIMEI = request.getParameter("txtDato");
     }
     else if(strTipoBusqueda.equals(SEARCH_BY_SIM)  ) {
         strNumeroSIM = request.getParameter("txtDato");
     }
      else if(strTipoBusqueda.equals(SEARCH_BY_RADIO)){
        strNumeroRadio = request.getParameter("txtDato"); 
     }
     
          
     HashMap objHashCustomerJava = objCustomerService.CustomerDAOgetCustomerJava(MiUtil.parseLong("27"),strNumeroTelefono,MiUtil.parseLong(strNumeroContrato),strNumeroIMEI,strNumeroSIM,strNumeroRadio);
         
       if( objHashCustomerJava.get("strMessage") != null )
          throw new Exception((String)objHashCustomerJava.get("strMessage"));
         
       ArrayList objArrayCustomerJava =  (ArrayList)objHashCustomerJava.get("arrCustomerBSCSList");
       if( objArrayCustomerJava != null && objArrayCustomerJava.size() > 0 ){
          //El primer registro siempre tiene valores 0 
          if( objArrayCustomerJava.size() > 2 ){
             //throw new Exception("Hay mas de un acierto");             
             HttpSession session = request.getSession(true);
             session.setAttribute("objArrayCustomerJava",objArrayCustomerJava);
             session.setAttribute("strLogin",strLogin);
             session.setAttribute("strRegionId", MiUtil.getString(intRegionId) );             
             session.setAttribute("strAppId", MiUtil.getString(iAppId));
             session.setAttribute("strUserId", MiUtil.getString(iUserId));
             session.setAttribute("hdnOrigenType",strGeneratorTypeRepCalcap);
             session.setAttribute("strGeneratorId",strGeneratorId);
             out.println("parent.mainFrame.fxListCompania()");
          }else{
            /*El primer registro tiene datos "0" , por eso se usa el registro de índice 1*/
            //String idCustomer = ((DominioBean)objArrayCustomerJava.get(1)).getDescripcion();
            //En la descripción se trae el customerIdBSCS y en el valor se trae el customerId CRM
            String idCustomer = null;
            if(strTipoBusqueda.equals(SEARCH_BY_TELEFONO) || strTipoBusqueda.equals(SEARCH_BY_RADIO) ) {
              //idCustomer = ((DominioBean)objArrayCustomerJava.get(1)).getValor();            
              idCustomer = ((DominioBean)objArrayCustomerJava.get(1)).getDescripcion();
            }
            else{
              idCustomer = ((DominioBean)objArrayCustomerJava.get(0)).getValor(); 
              //idCustomer = ((DominioBean)objArrayCustomerJava.get(0)).getDescripcion();
            }				 
            hshCutomer = objCustomerService.CustomerDAOgetCustomerData( MiUtil.parseLong(idCustomer), strLogin, MiUtil.parseLong(""+intRegionId),iUserId,iAppId,strGeneratorId,strGeneratorTypeRepCalcap);             
           hshValidationCustomer=objCustomerService.getValidationCustomer((long)iUserId,MiUtil.parseLong(idCustomer));
            strMessage = (String)hshValidationCustomer.get(Constante.MESSAGE_OUTPUT);
          if( strMessage!=null ){
              throw new Exception(strMessage);
          }
           
            
            if ( (hshCutomer!=null && hshCutomer.size() > 0) && (  (strGeneratortype.equals(Constante.GENERATOR_TYPE_OPP))|| (MiUtil.parseInt((String)hshValidationCustomer.get("respuesta"))== 1 ) ) )          
             showCustomerDetail(hshCutomer,request,out);
            else
              throw new Exception("No se encontraron aciertos");             
          }           
       }  
    
   }
    
  /**
   Method : doSearchCustomerById
   Purpose: Genera una busqueda del cliente por su descripción y si este coincide con 
   un acierto se procede a mostrar la información indicada. En caso contrario
   se expone una ventana flotante donde debe elegir cual de todos los aciertos
   es el indicado
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Lee Rosales     25/02/2008  Creación
   */
   public void doSearchCustomerById(HttpServletRequest request, PrintWriter out) throws Exception{
    String    strCodigoCliente,strLogin,strGeneratorType,strGeneratorTypeRepCalCap;
    int       intRegionId;
    HashMap   hshCutomer = new HashMap();
  
    intRegionId       = MiUtil.parseInt(request.getParameter("hdnRegionId"));
    strLogin          = MiUtil.getString(request.getParameter("hdnSessionLogin"));
    int iUserId       = MiUtil.parseInt((String)request.getParameter("hdnIUserId"));
    int iAppId        = MiUtil.parseInt((String)request.getParameter("hdnIAppId"));
    strGeneratorType  = MiUtil.getString(request.getParameter("strGeneratorType"));
    strGeneratorTypeRepCalCap = (String)request.getParameter("hdnOrigenType");
    String strGeneratortype  = request.getParameter("hdngeneratortype");
    String strGeneratorId     = (request.getParameter("strGeneratorId")==null?"":request.getParameter("strGeneratorId"));            
    HashMap   hshValidationCustomer= new HashMap();
    String    strMessage = null;
    
    if( ! strGeneratorType.equals("") )
      strCodigoCliente  = request.getParameter("strCustomerId");
    else
      strCodigoCliente  = request.getParameter("hdnCustId");
    
	 System.out.println("[CustomerServlet][doSearchCustomerById]intRegionId: "+intRegionId+"");
	 System.out.println("[CustomerServlet][doSearchCustomerById]strLogin: "+strLogin);
	 System.out.println("[CustomerServlet][doSearchCustomerById]strGeneratorType: "+strGeneratorType);
	 System.out.println("[CustomerServlet][doSearchCustomerById]intRegionId: "+iUserId+"");
	 System.out.println("[CustomerServlet][doSearchCustomerById]strCodigoCliente: "+strCodigoCliente);	 
   
   // Valida si el consultor puede visualizar el cliente
   hshValidationCustomer=objCustomerService.getValidationCustomer((long)iUserId,MiUtil.parseLong(strCodigoCliente));
   strMessage = (String)hshValidationCustomer.get(Constante.MESSAGE_OUTPUT);
   // strMessage = strMessage.equals("null")?null:strMessage;
    if( strMessage!=null ){
        throw new Exception(strMessage);
    }
    if ( !strGeneratortype.equals(Constante.GENERATOR_TYPE_OPP) && MiUtil.parseInt((String)hshValidationCustomer.get("respuesta")) == 0 ){      
      System.out.println("La compañía se encuentra fuera del ámbito de venta");      
      out.println("parent.mainFrame.alert('La compañía se encuentra fuera del ámbito de venta')");    
      return;
    }
   
   
    hshCutomer = objCustomerService.CustomerDAOgetCustomerData( MiUtil.parseLong(strCodigoCliente), strLogin, MiUtil.parseLong(""+intRegionId),iUserId,iAppId,strGeneratorId,strGeneratorTypeRepCalCap);
    
    if( hshCutomer.get("strMessage")!=null )
     throw new Exception((String)hshCutomer.get("strMessage"));
    
     
    showCustomerDetail(hshCutomer,request,out);

   }
    
    
  /**
   Method : doSearchCustomerByRuc
   Purpose: Genera una busqueda del cliente por su descripción y si este coincide con 
   un acierto se procede a mostrar la información indicada. En caso contrario
   se expone una ventana flotante donde debe elegir cual de todos los aciertos
   es el indicado
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Lee Rosales     25/02/2008  Creación
  */
  public void doSearchCustomerByRuc(HttpServletRequest request, PrintWriter out) throws Exception{
    String strNumeroRuc = request.getParameter("txtCampoOtro");
    String    strCodigoCliente,strLogin,strMessage;
    int       intRegionId;
    HashMap   hshCutomer = new HashMap();
    HashMap   hshValidationCustomer= new HashMap();
    strLogin          = MiUtil.getString(request.getParameter("hdnSessionLogin"));
    intRegionId       = MiUtil.parseInt(request.getParameter("hdnRegionId"));
    int iUserId       = MiUtil.parseInt((String)request.getParameter("hdnIUserId"));
    int iAppId        = MiUtil.parseInt((String)request.getParameter("hdnIAppId"));
    String strGeneratorTypeRepCalcap = (String)request.getParameter("hdnOrigenType");
    String strGeneratorId     = (request.getParameter("hdnGeneratorId")==null?"":request.getParameter("hdnGeneratorId"));
    String strGeneratortype  = request.getParameter("hdngeneratortype");
    
    Hashtable h1      = objCustomerService.CustomerDAOgetCustRucQty(strNumeroRuc);
    
	 System.out.println("[CustomerServlet][doSearchCustomerByRuc]strLogin: "+strLogin);
    if ( h1 != null){ 
      strMessage = (String)h1.get("strMessage");
      strMessage = strMessage.equals("null")?null:strMessage;
      
      if( strMessage!=null )
      throw new Exception((String)h1.get("strMessage"));
    }
    hshValidationCustomer=objCustomerService.getValidationCustomer((long)iUserId,MiUtil.parseLong((String)h1.get("wn_customerid")));
    strMessage = (String)hshValidationCustomer.get(Constante.MESSAGE_OUTPUT);
    if( strMessage!=null ){
        throw new Exception(strMessage);
    }
    
    
    if ( MiUtil.parseInt((String)h1.get("wn_customer_qty")) == 1 && (  (strGeneratortype.equals(Constante.GENERATOR_TYPE_OPP))|| (MiUtil.parseInt((String)hshValidationCustomer.get("respuesta"))== 1 ) ) ) { 
      hshCutomer = objCustomerService.CustomerDAOgetCustomerData(MiUtil.parseLong((String)h1.get("wn_customerid")), strLogin, MiUtil.parseLong(""+intRegionId),iUserId,iAppId,strGeneratorId,strGeneratorTypeRepCalcap);
      showCustomerDetail(hshCutomer,request,out);
    }else{
      out.println("alert('Especifique mejor su búsqueda.');");
      out.println("parent.mainFrame.searchRuc()");
    }
  }
    
 
  
  /**
   Method : searchCustomerBySite
   Purpose: Buscar clientes y devuleve un hashtable
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Lee Rosales     27/11/2007  Creación
   */
  
   public void searchCustomerBySite( String strSiteId,HttpServletRequest request, PrintWriter out )  throws Exception {
  
    //Debo obtener el RegionId del SITE
    SiteService objSiteService = new SiteService();
    HashMap objHashMap = objSiteService.getSiteData(MiUtil.parseLong(strSiteId));
    if( objHashMap.get("strMessage")!=null ) throw new Exception((String)objHashMap.get("strMessage"));
   
    SiteBean objSiteBean = (SiteBean)objHashMap.get("objSite");
    
    String strMessage = objSiteService.doAddrContValidation(objSiteBean.getSwcustomerid(),Long.parseLong(strSiteId));
    
    if( strMessage != null ){
      out.println("parent.mainFrame.document.frmdatos.cmbResponsablePago.value='';");
       throw new Exception(strMessage);       
    }
    // Lista de Direcciones
    // ----------------------------
    getShowAddress(strSiteId,objSiteBean.getSwRegionId(),"SITE",request,out);
   
    // Lista de Contactos
    // ----------------------------
    getShowContacts(strSiteId,"SITE",request,out);

    // Datos del Responsable de pago
    // -----------------------------
    getShowSiteData(strSiteId,request,out);
   }
   
  
  /**
   Method : doSearchCustomerByDetail
   Purpose: Permite traer la data inicial del Customer de la Orden sin tener que recargar los combos de la Solución, Categoría y SubCategoría.
   Developer       Fecha       Comentario
   =============   ==========  ===============================================================================================================
   Karen Salvador  11/06/2008  Creación
   */
  
   public void doSearchCustomerByDetail(HttpServletRequest request, PrintWriter out )  throws Exception {
   
    System.out.println("[CustomerServlet]:doSearchCustomerByDetail");
    HashMap   hshCutomer = new HashMap();
    String    strLogin;
    int       intRegionId;
    String    strMessage = null;
    
    String    strCustomerDescription;    
    strCustomerDescription  = (String)request.getParameter("hdnCompanyName");
    strLogin                = (String)request.getParameter("hdnSessionLogin");
    intRegionId             = MiUtil.parseInt(request.getParameter("hdnRegionId"));
    int    iUserId          = MiUtil.parseInt((String)request.getParameter("hdnIUserId"));
    int    iAppId           = MiUtil.parseInt((String)request.getParameter("hdnIAppId"));
    String strGeneratorTypeRepCalcap = (String)request.getParameter("hdnOrigenType");
    String strGeneratorId   = (request.getParameter("hdnGeneratorId")==null?"":request.getParameter("hdnGeneratorId"));
    System.out.println("[doSearchCustomerByDetail]:strCustomerDescription - " +strCustomerDescription);
    System.out.println("[doSearchCustomerByDetail]:strLogin - "+strLogin);
    System.out.println("[doSearchCustomerByDetail]:intRegionId - "+intRegionId);
    System.out.println("[doSearchCustomerByDetail]:iUserId - "+iUserId);
    System.out.println("[doSearchCustomerByDetail]:iAppId - "+iAppId);
    
    Hashtable hshCustPattern = objCustomerService.CustomerDAOgetCustPatternQty(strCustomerDescription);
    strMessage = (String)hshCustPattern.get("strMessage");
    
    strMessage = strMessage.equals("null")?null:strMessage;
    if( strMessage!=null ){
        throw new Exception(strMessage);
    }
      
    // Lista de Direcciones
    // ----------------------------
    getShowAddress((String)hshCustPattern.get("wn_customerid"),MiUtil.parseLong(String.valueOf(intRegionId)),"CUSTOMER",request,out);

     // Lista de Contactos
    // ----------------------------
    getShowContacts((String)hshCustPattern.get("wn_customerid"),"CUSTOMER",request,out);
   

    // Datos del Customer
    // -----------------------------   
    hshCutomer = objCustomerService.CustomerDAOgetCustomerData(MiUtil.parseLong((String)hshCustPattern.get("wn_customerid")), strLogin, MiUtil.parseLong(""+intRegionId),iUserId,iAppId,strGeneratorId,strGeneratorTypeRepCalcap);
    strMessage = (String)hshCutomer.get("strMessage");
    
  //strMessage = strMessage.equals("null")?null:strMessage;
    System.out.println("strMessage:"+strMessage);
    if( strMessage!=null ){
      clearCustomer(request,out);
      throw new Exception((String)hshCutomer.get("strMessage"));
    }
    
    out.println("var form = parent.mainFrame.document.frmdatos;");  
    out.println("form.txtCodBSCS.value          = '" + hshCutomer.get("wv_codbscs") + "';");
    out.println("form.txtLineaCredito.value     = '" + hshCutomer.get("wv_nplineacredito") + "';");	 
    out.println("form.txtRegionSite.value       = '';");
    out.println("form.cmbVendedor.value         = '" + hshCutomer.get("wn_vendedorid") + "';");
    out.println("form.hdnVendedor.value         = '" + hshCutomer.get("wn_vendedorid") + "';");
    System.out.println("================001=================");
    out.println("form.txtDealer.value           = '" + hshCutomer.get("wv_dealer") + "';");

   }
    
    

  /**
   Method : showCustomerDetail
   Purpose: Muestra los detalles del cliente
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   David Coca      20/07/2007  Creación
   Ruth Polo       09/01/2009  Logica para vendedor para consultores (Ordenes SSAA)
   Rensso Martinez 28/09/2009  Se agrego hidden wv_hdncodbscs para almacenar el codigo de BSCS inicial por default.
   */    
   public void showCustomerDetail(HashMap hshCustomer, HttpServletRequest request, PrintWriter out) throws Exception {
      
     //----------------------------------------------
     //  1.2 Sección de proceso y captura de datos
     //----------------------------------------------
     HashMap objHashMap = null;
     String strCodigoCliente = "0";
     String strNombreCliente = null;
       
     int wn_swcustomerid = 0;   
     String wv_swname    = "";         
     String wv_swnamecom = ""; 
     String wv_swruc = "";          
     String wv_codbscs = "";       
     String wv_swtype = ""; 
     String wv_swrating = "";        
     String wv_nplineacredito = "";  
     String wv_swmainphone = ""; 
     String wv_swmainfax = "";       
     String wv_swnameRegion = "";
     long   wn_regionid = 0;
     String wv_swnameGiro = "";       
     String wv_tipoCuenta = "";      
     int wn_vendedorid = 0;  
     String wv_vendedorname = "";    
     String wv_dealer = ""; 
     String wv_domiciliodir = "";     
     String wv_entregadir = "";      
     String wv_cuName = "";      
     String wv_cuTele = "";          
     String wv_cuFax = ""; 
     String wv_cgName = "";           
     String wv_cgTele = "";          
     String wv_cgFax = "";        
     String wv_facturacionDir = ""; 
     String wv_entregacDir = ""; 
     String wv_comunicacionDir = "";  
     String wv_cfName = "";          
     String wv_cfTele = "";       
     String wv_cfFax = ""; 
     String wv_npcustomerrelationtype = "";     
     String wn_npcustomerrelationid = "";                   
     String wv_swname_customerrelation = "";         
     String wv_codbscs_paymentresp = "";
     String wv_paymentrespdesc = "";
     String wn_paymentrespid = "";
     int wn_vendedorid2 = 0;  
     String wv_vendedorname2 = "";    
     String wv_dealer2 = "";      
     long wn_rol_consultor =0;
     String wv_hdncodbscs="";
     
     long    iUserId         = MiUtil.parseInt((String)request.getParameter("hdnIUserId"));
     long    iAppId          = MiUtil.parseInt((String)request.getParameter("hdnIAppId"));     
     
     if( hshCustomer != null) {
          
        if (hshCustomer.size()!=0) {
              
          // Datos del HastTable
          wn_swcustomerid     = MiUtil.parseInt((String)hshCustomer.get("wn_swcustomerid"));
          if (wn_swcustomerid==0)
            throw new Exception("No se encontraron aciertos");
          wv_swname           = (String)hshCustomer.get("wv_swname"); 
          
          if (MiUtil.getString(wv_swname)=="")
            throw new Exception("No se encontraron aciertos");
          wv_swnamecom        = (String)hshCustomer.get("wv_swnamecom"); 
                
          wv_swruc            = (String)hshCustomer.get("wv_swruc");          
          wv_codbscs          = (String)hshCustomer.get("wv_codbscs");       
          wv_hdncodbscs       = (String)hshCustomer.get("wv_codbscs");
          wv_swtype           = (String)hshCustomer.get("wv_swtype"); 
          wv_swrating         = (String)hshCustomer.get("wv_swrating");        
          wv_nplineacredito   = (String)hshCustomer.get("wv_nplineacredito");  
                
          wv_swmainphone      = (String)hshCustomer.get("wv_swmainphone"); 
          wv_swmainfax        = (String)hshCustomer.get("wv_swmainfax");
              
          wv_swnameRegion     = (String)hshCustomer.get("wv_swnameregion");  
          wv_swnameGiro       = (String)hshCustomer.get("wv_swnamegiro");       
          wv_tipoCuenta       = (String)hshCustomer.get("wv_tipocuenta");      
          
          
          wn_vendedorid       = MiUtil.parseInt((String)hshCustomer.get("wn_vendedorid"));  
          wv_vendedorname     = (String)hshCustomer.get("wv_vendedorname");    
          wv_dealer           = (String)hshCustomer.get("wv_dealer"); 
          
          // RPOLO
          //Para uso cuando es Consultor Directo o Indirecto se debe mostrar el vendedor como se indica
          CustomerService objCustomerService = new CustomerService();
                    
          if (objCustomerService.CustomerDAOgetRol(Constante.ROL_CONSULTORD,iUserId,iAppId) == 1 ||
              objCustomerService.CustomerDAOgetRol(Constante.ROL_CONSULTORI,iUserId,iAppId) == 1 ){
            
            wn_vendedorid       = MiUtil.parseInt((String)hshCustomer.get("wn_vendedorid2"));  
            wv_vendedorname     = (String)hshCustomer.get("wv_vendedorname2");    
            wv_dealer           = (String)hshCustomer.get("wv_dealer2");             
          }
                    
          wv_npcustomerrelationtype   = (String)hshCustomer.get("wv_npcustomerrelationtype");     
          wn_npcustomerrelationid     = (String)hshCustomer.get("wn_npcustomerrelationid");
          wv_swname_customerrelation  = (String)hshCustomer.get("wv_swname_customerrelation");
          
          //Variables para mostrar el responsable de pago de la bolsa
          wv_codbscs_paymentresp  = (String)hshCustomer.get("wv_codbscs_paymentresp");
          wv_paymentrespdesc      = (String)hshCustomer.get("wv_paymentrespdesc");
          wn_paymentrespid        = (String)hshCustomer.get("wn_paymentrespid");
          System.out.println("[CustomerServlet][showCustomerDetail][wv_codbscs_paymentresp]"+wv_codbscs_paymentresp);
          System.out.println("[CustomerServlet][showCustomerDetail][wv_paymentrespdesc]"+wv_paymentrespdesc);
          System.out.println("[CustomerServlet][showCustomerDetail][wn_paymentrespid]"+wn_paymentrespid);
          //-----------------------------------------------------------------------------------
          
          wn_regionid = MiUtil.parseLong((String)hshCustomer.get("wv_swregionid"));
          String strRegionId = request.getParameter("hdnRegionId");                   
          System.out.println("[CustomerServlet][showCustomerDetail][wn_regionid]"+wn_regionid);
          System.out.println("[CustomerServlet][showCustomerDetail][strRegionId]"+strRegionId);
          if(strRegionId != null && !strRegionId.equals("0"))
             wn_regionid = MiUtil.parseLong(strRegionId);
         
          System.out.println("[CustomerServlet][showCustomerDetail][wn_regionid]"+wn_regionid);
          //System.out.println("[CustomerServlet][showCustomerDetail][intRegionId]"+intRegionId);
          
          strCodigoCliente = wn_swcustomerid+"";
          strNombreCliente = wv_swname;                
          // Valoracion de cliente          
            if ( ! strCodigoCliente.equals("0") ) {
                Hashtable hd = new Hashtable();
                //Remoto
                SEJBCustomerRemote sEJBCustomerRemote = CustomerService.getSEJBCustomerRemote();
                //Local
                //SEJBOrderNewLocal sEJBOrderNewLocal = NewOrderService.getSEJBOrderNewLocal();
                 hd    =   sEJBCustomerRemote.CustomerDAOgetCompanyInfo(MiUtil.parseInt(strCodigoCliente));
                if(hd.size()!=0) {
                  if( !(hd.get("wn_valorTotalGarantia")+"").equals("0") ) {
					  if(hd.get("wn_valorTotalGarantia") == null) hd.put("wn_valorTotalGarantia","1");
                      out.println("parent.mainFrame.divCustValue.innerHTML = \"<img src='"+Constante.PATH_APPORDER_SERVER+"/images/CustValue/CustValue" + hd.get("wn_valorTotalGarantia") + ".gif' border=0 align='absbottom'>\" ;");  
                  }else {
                      out.println("parent.mainFrame.divCustValue.innerHTML = \"<img src='"+Constante.PATH_APPORDER_SERVER+"/images/CustValue/CustValue.gif' border=0 align='absbottom'>\" ;");
                  }
                }
           }
       }
                                        
          clearCustomer(request,out);   
          String strDealerName = null;
          String strVendedor = (String)request.getParameter("hdnVendedorId");          
          System.out.println("strVendedor==="+strVendedor);
          if (  !MiUtil.getString(strVendedor).equalsIgnoreCase("")   ){            
            HashMap objHashMapDealer = objGeneralService.getDealerBySalesman(MiUtil.parseLong(strVendedor));              
            if( objHashMapDealer.get("strMessage") != null ){                            
              throw new Exception((String)objHashMapDealer.get("strMessage"));
            }            
            strDealerName = MiUtil.getString((String)objHashMapDealer.get("strDealerName"));            
            if (strDealerName != null)
              strDealerName = strDealerName.equalsIgnoreCase("null")?"":strDealerName;
            
            
            //if ( MiUtil.getString(wv_dealer).equalsIgnoreCase("") || MiUtil.getString(wv_dealer).equalsIgnoreCase("null") ){              
              wv_dealer = strDealerName;
            //}
          }
          
          System.out.println("wv_dealer==="+wv_dealer);
          //-----------------------------------------
          //  1.3 Sección de visualización de datos
          //-----------------------------------------
          out.println("if (form.txtCompany!=null)          form.txtCompany.value          = '" + wv_swname + "';");            
          out.println("if (form.hdnCustomerName!=null)     form.hdnCustomerName.value     = '" + wv_swname + "';");
          out.println("if (form.txtCompanyNameCom!=null)   form.txtCompanyNameCom.value   = '" + wv_swnamecom+ "';");
          out.println("if (form.txtCodBSCS!=null)          form.txtCodBSCS.value          = '" + wv_codbscs+ "';");
          out.println("if (form.txtCodBSCS!=null)          form.hdnCodBSCS.value          = '" + wv_hdncodbscs+ "';");
          out.println("if (form.txtCompanyId!=null)        form.txtCompanyId.value        = '" + wn_swcustomerid+ "';");
          out.println("if (form.txtCampoOtro!=null)        form.txtCampoOtro.value        = '" + wv_swruc+ "';");
          out.println("if (form.txtRegion!=null)           form.txtRegion.value           = '" + wv_swnameRegion + "';");
          out.println("if (form.txtIndustria!=null)        form.txtIndustria.value        = '" + wv_swnameGiro + "';");
          out.println("if (form.txtTipoCuenta!=null)       form.txtTipoCuenta.value       = '" + wv_tipoCuenta + "';");
          out.println("if (form.txtTipoCompania!=null)     form.txtTipoCompania.value     = '" + wv_swtype + "';");
          
          out.println("if (form.cmbVendedor!=null)         form.cmbVendedor.value         = '" + wn_vendedorid + "';");
          out.println("if (form.cmbVendedor!=null)         form.hdnOrderCreator.value     = '" + wn_vendedorid + "';");
          out.println("if (form.hdnVendedor!=null)         form.hdnVendedor.value         = '" + wn_vendedorid + "';");
          out.println("if (form.txtDealer!=null)           form.txtDealer.value           = '" + wv_dealer +  "';");
          out.println("if (form.hdnDealer!=null)           form.hdnDealer.value           = '" + wv_dealer + "';");

          out.println("if (form.txtScoring!=null)          form.txtScoring.value          = '" + wv_swrating + "';");
          out.println("if (form.txtLineaCredito!=null)     form.txtLineaCredito.value     = '" + wv_nplineacredito + "';");
          
          out.println("if (form.txtTelefono!=null)         form.txtTelefono.value         = '" + wv_swmainphone + "';");
          out.println("if (form.txtFax!=null)              form.txtFax.value              = '" + wv_swmainfax + "';");
          
          out.println("if (form.txtTipoCliente!=null)      form.txtTipoCliente.value      = '" + wv_npcustomerrelationtype+ "';");
          out.println("if (form.txtClientePrincipal!=null) form.txtClientePrincipal.value = '" + wv_swname_customerrelation + "';");
          
          out.println("if (form.txtPaymentRespDesc!=null) form.txtPaymentRespDesc.value = '" + wv_paymentrespdesc + "';");
          out.println("if (form.hdnPaymentRespId!=null)   form.hdnPaymentRespId.value = '" + wn_paymentrespid + "';");
                    
             
          // Lista de Direcciones
          // ----------------------------
          getShowAddress(strCodigoCliente,wn_regionid,"CUSTOMER",request,out);
          
          // Lista de Contactos
          // ----------------------------
          getShowContacts(strCodigoCliente,"CUSTOMER",request,out);
          
          // Lista de Sites
          // ----------------------------
          objHashMap =  objCustomerService.getCustomerSites(MiUtil.parseLong(strCodigoCliente),"Activo");
      
          if( objHashMap.get("strMessage") != null )
              throw new Exception((String)objHashMap.get("strMessage"));
            
          ArrayList lista_sites = (ArrayList)objHashMap.get("objArrayList");

          out.println("if (frmdatos.cmbResponsablePago != null){");
          out.println("frmdatos.cmbResponsablePago.length=0;");
          out.println("AddNewOption(frmdatos.cmbResponsablePago, '', '           '); }");
          
          HashMap hsMapLista = new HashMap();                   
          for( int i=0; i<lista_sites.size();i++ ){
              hsMapLista = (HashMap)lista_sites.get(i);
              out.println("if (frmdatos.cmbResponsablePago != null)");
              out.println("AddNewOption(frmdatos.cmbResponsablePago, '" + hsMapLista.get("wn_swsiteid")  + "', \"" + hsMapLista.get("wv_swsitename").toString()  + "  " + hsMapLista.get("wv_npcodbscs") + "\" );");
          }
          
         out.println("form.txtDetalle.focus() "); 
         
         //out.println("form.hdnCustId.value="+strCodigoCliente); 
         setControlsByParamaters(request,out,strCodigoCliente);
         
         
      }  // Fin de  h!= null  
      
   }
    
  
   /**
    Method : clearCustomer
    Purpose: Limpia datos de los clientes
    Developer       Fecha       Comentario
    =============   ==========  ======================================================================
    David Coca      20/07/2007  Creación
    */    
   public void clearCustomer(HttpServletRequest request, PrintWriter out) throws Exception{
       
     out.println("var form = parent.mainFrame.document.frmdatos;");  
     
     //out.println("form.txtDato.value             = '';");
     out.println("form.txtCompany.value = form.txtCompany.value.toUpperCase();");
     out.println("form.hdnCustomerName.value     = '';");
     out.println("form.txtCompanyNameCom.value   = '';");
     out.println("form.txtCodBSCS.value          = '';");
     out.println("form.txtCompanyId.value        = '';");
     out.println("form.txtCampoOtro.value        = '';");
     out.println("form.txtRegion.value           = '';");
     out.println("form.txtIndustria.value        = '';");
     out.println("form.txtTipoCuenta.value       = '';");
     out.println("form.txtTipoCompania.value     = '';");
     out.println("form.cmbVendedor.value         = '';");
     out.println("form.hdnOrderCreator.value     = '';");
     out.println("form.hdnVendedor.value         = '';");
     out.println("form.txtDealer.value           = '';");
     out.println("form.txtScoring.value          = '';");
     out.println("form.txtLineaCredito.value     = '';");     
     out.println("form.txtTelefono.value         = '';");
     out.println("form.txtFax.value              = '';");
     
     out.println("form.txtTipoCliente.value      = '';");
     out.println("form.txtClientePrincipal.value = '';");
     
     out.println("form.txtPaymentRespDesc.value  = '';");
     out.println("form.hdnPaymentRespId.value    = '';");
          
     // Direcciones
     out.println("var frmdatos  = parent.mainFrame.document.frmdatos;");
     out.println("var table = parent.mainFrame.document.getElementById('table_address');");
     out.println("var table_length = table.rows.length;");
     out.println("var col;");
     out.println("var i;");
        
     out.println("if (table_length > 0)");
     out.println("for (i=1; i < table_length; i++){");
     out.println("table.deleteRow(1);");
     out.println("}");         
     
     // Contactos 
     out.println("var tablec = parent.mainFrame.document.getElementById('table_contacts');");
     out.println("var tablec_length = tablec.rows.length;");
     out.println("var col;");
     out.println("var j;");
        
     out.println("if (tablec_length > 0)");
     out.println("for (j=1; j < tablec_length; j++){");
     out.println("tablec.deleteRow(1);");
     out.println("}");   
     
     // Site
     out.println("frmdatos.cmbResponsablePago.length=0;");
     out.println("AddNewOption(frmdatos.cmbResponsablePago, '', '           ');");
     
     //Inclusion de Ordenes.
     //------------------------------------- 
     
     out.println("parent.mainFrame.frmdatos.txtNumSolicitud.value ='';");
     //out.println("parent.mainFrame.frmdatos.cmbSolucion.value ='';");
	  out.println("parent.mainFrame.frmdatos.cmbDivision.value ='';");
     out.println("parent.mainFrame.frmdatos.txtFechaProceso.value ='';");
     out.println("parent.mainFrame.frmdatos.txtDetalle.value ='';");
          
     //out.println("parent.mainFrame.DeleteSelectOptions(form.cmbSolucion);");
	  out.println("parent.mainFrame.DeleteSelectOptions(form.cmbDivision);");
     out.println("parent.mainFrame.DeleteSelectOptions(form.cmbCategoria);");
     out.println("parent.mainFrame.DeleteSelectOptions(form.cmbSubCategoria);");
     
     out.println("parent.mainFrame.VsubjectTypeSpec.removeElementAll();");
     
     out.println("parent.mainFrame.frmdatos.txtDealer.value ='';");
     
     //Valoración del cliente
     //Hacer la invocacion al API y traer el nombre del archivo.
     out.println("parent.mainFrame.divCustValue.innerHTML = \"<img src='"+Constante.PATH_APPORDER_SERVER+"/images/CustValue/CustValue.gif' border=0 align='absbottom'>\" ;");
     //out.println("alert('se ha procedido a finalizar la limpieza ');"); 
     // Repetir similares
     //out.println("location.replace('"+Constante.PATH_APPORDER_LOCAL+"/Bottom.html');");    
  }
  
   /**
    Method : getShowAddress
    Purpose: Obtiene la visualizacion de datos de direcciones ya sea por Customer o Site
    Developer        Fecha       Comentario
    =============    ==========  ==========================================================================
    David Coca       26/07/2007  Creación
    Walter Sotomayor 30/12/2009  Se modifica el metodo para que muestre la dirección de entrega por defecto.
   */    
   public void getShowAddress(String strCodigo, long longRegionId , String strTipoObjeto, HttpServletRequest request, PrintWriter out) throws Exception {
        long lNumAddressId = 0;
        String strFlagDefault = "";
        String strAux = "";
        CustomerService objCustomerService = new CustomerService();
        HashMap objHashMap = null;
      
        objHashMap =  objCustomerService.getNumAddressId(strTipoObjeto,MiUtil.parseLong(strCodigo),"30", longRegionId);    
        if( objHashMap.get("strMessage") != null ) {
            throw new Exception((String)objHashMap.get("strMessage"));
        }      
        lNumAddressId=MiUtil.parseLong((String)objHashMap.get("wn_numaddressid"));
        System.out.println("[CustomerServlet][getShowAddress][lNumAddressId]"+lNumAddressId);
        // Lista de direcciones
        // --------------------
        System.out.println("[CustomerServlet][getShowAddress][strCodigo]"+strCodigo);
        System.out.println("[CustomerServlet][getShowAddress][longRegionId]"+longRegionId);
        System.out.println("[CustomerServlet][getShowAddress][strTipoObjeto]"+strTipoObjeto);
        objHashMap =  objCustomerService.CustomerDAOgetCustomerAddress(MiUtil.parseInt(strCodigo),longRegionId,strTipoObjeto,"");
        
        if( objHashMap.get("strMessage") != null ) {
            throw new Exception((String)objHashMap.get("strMessage"));
        }      
         ArrayList lista_direcciones = (ArrayList)objHashMap.get("objArrayList");
         // Lista de direcciones
         // --------------------
         String direccion = "";
         String dirAux = "";
         
         if ( lista_direcciones != null ) {
          
         out.println("var frmdatos  = parent.mainFrame.document.frmdatos;");
         out.println("frmdatos.hdnRegionIdAddress.value = '"+longRegionId+"'");
         out.println("var table = parent.mainFrame.document.getElementById('table_address');");
         out.println("var table_length = table.rows.length;");
         out.println("var col;");
         out.println("var i;");
            
         out.println("if (table_length > 0)");
         out.println("for (i=1; i < table_length; i++){");
         out.println("table.deleteRow(1);");
         out.println("}");
         
         HashMap h = new HashMap();         
         for( int i=0; i<lista_direcciones.size();i++ ){
          
              h = (HashMap)lista_direcciones.get(i);
              System.out.println("[CustomerSevlet][wv_swaddress1]"+MiUtil.getMessageClean(h.get("wv_swaddress1").toString()));
              direccion = h.get("wv_swtipodirec").toString();
              strFlagDefault = h.get("wv_swflagaddress").toString();     
              out.println("row = table.insertRow(-1);");
         
              out.println("col = row.insertCell(0);");
              out.println("col.className = 'CellContent';");
              out.println("col.align = 'left';");
              
              if( direccion.equals(Constante.DIR_ENTREGA) ) {
              System.out.println("wv_swcity: "+MiUtil.getMessageClean(h.get("wv_swcity").toString()));
              System.out.println("wv_swcity: "+h.get("wv_swcity").toString());
              //Establesco los Hidden de la ShipToAddress
              if ("1".equals(strFlagDefault)&&("".equals(strAux))){
                    out.println(" frmdatos.hdnDeliveryAddress.value = '"+MiUtil.getMessageClean(h.get("wv_swaddress1").toString())+"'");
                    out.println(" frmdatos.hdnDeliveryCity.value = '"+MiUtil.getMessageClean(h.get("wv_swcity").toString())+"'");
                    out.println(" frmdatos.hdnDeliveryProvince.value = '"+MiUtil.getMessageClean(h.get("wv_swprovince").toString())+"'");
                    out.println(" frmdatos.hdnDeliveryState.value = '"+MiUtil.getMessageClean(h.get("wv_swstate").toString())+"'");              
                    
                    out.println(" frmdatos.hdnDeliveryCityId.value = '"+MiUtil.getMessageClean(h.get("wv_swcityid").toString())+"'");
                    out.println(" frmdatos.hdnDeliveryProvinceId.value = '"+MiUtil.getMessageClean(h.get("wv_swprovinceid").toString())+"'");
                    out.println(" frmdatos.hdnDeliveryStateId.value = '"+MiUtil.getMessageClean(h.get("wv_swstateid").toString())+"'");
                  
                    dirAux = " &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input readonly type=text name=txtDirEntrega maxlength=200 value= '"+(MiUtil.getMessageClean(h.get("wv_swaddress1").toString()))+"' onchange='javascript:fVerifModifDirEntrega(this)' style='border-bottom:none;border-right:none;border-top:none;border-left:none;background-color:transparent;width:100%';>";
                    dirAux+= " <div id=dirEntregaDiv style=display:none>";
                    dirAux+= " <input type=text name=txtDirEntregaMensaje style='color:red;border-bottom:none;border-right:none;border-top:none;border-left:none;background-color:transparent;' size=17 value='Dirección modificada!' readOnly>";
                    dirAux+= " </div>";
                  
                    out.println("col.innerHTML = \"" + dirAux + "\";");
                                
                    out.println("col = row.insertCell(1);");
                    out.println("col.className = 'CellContent';");
                    out.println("col.align = 'left';");                     
                    out.println("col.innerHTML = '<input readonly type=text name=txtCity maxlength=200 value= \""+(MiUtil.getMessageClean(h.get("wv_swcity").toString()))+"\"  style=border-bottom:none;border-right:none;border-top:none;border-left:none;background-color:transparent;width:100%;>';");                            
             
                    out.println("col = row.insertCell(2);");
                    out.println("col.className = 'CellContent';");
                    out.println("col.align = 'left';");                       
                    out.println("col.innerHTML = '<input readonly type=text name=txtProvince maxlength=200 value= \""+(MiUtil.getMessageClean(h.get("wv_swprovince").toString()))+"\"  style=border-bottom:none;border-right:none;border-top:none;border-left:none;background-color:transparent;width:100%;>';");              
             
                    out.println("col = row.insertCell(3);");
                    out.println("col.className = 'CellContent';");
                    out.println("col.align = 'left';");                       
                    out.println("col.innerHTML = '<input readonly type=text name=txtState maxlength=200 value= \""+(MiUtil.getMessageClean(h.get("wv_swstate").toString()))+"\"  style=border-bottom:none;border-right:none;border-top:none;border-left:none;background-color:transparent;width:100%;>';");                                            
                    
                    out.println("col = row.insertCell(4);");
                    out.println("col.className = 'CellContent';");
                    out.println("col.align = 'center';");
                    out.println("col.innerHTML = \"<a href=javascript:fxShowAdress();>" + h.get("wv_swtipodirec") + "</a>&nbsp;&nbsp;<a href=javascript:fxEditAdress();><img src='"+Constante.PATH_APPORDER_SERVER+"/images/Editar.gif' border=0 align='absbottom'></a>\";");
                                    
                    strAux = "entro";
                    
                }else if(!("1".equals(strFlagDefault))&&("".equals(strAux))&&(lNumAddressId == 1)){
                    out.println(" frmdatos.hdnDeliveryAddress.value = '"+MiUtil.getMessageClean(h.get("wv_swaddress1").toString())+"'");
                    out.println(" frmdatos.hdnDeliveryCity.value = '"+MiUtil.getMessageClean(h.get("wv_swcity").toString())+"'");
                    out.println(" frmdatos.hdnDeliveryProvince.value = '"+MiUtil.getMessageClean(h.get("wv_swprovince").toString())+"'");
                    out.println(" frmdatos.hdnDeliveryState.value = '"+MiUtil.getMessageClean(h.get("wv_swstate").toString())+"'");              
                    
                    out.println(" frmdatos.hdnDeliveryCityId.value = '"+MiUtil.getMessageClean(h.get("wv_swcityid").toString())+"'");
                    out.println(" frmdatos.hdnDeliveryProvinceId.value = '"+MiUtil.getMessageClean(h.get("wv_swprovinceid").toString())+"'");
                    out.println(" frmdatos.hdnDeliveryStateId.value = '"+MiUtil.getMessageClean(h.get("wv_swstateid").toString())+"'");
                  
                    dirAux = " &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input readonly type=text name=txtDirEntrega maxlength=200 value= '"+(MiUtil.getMessageClean(h.get("wv_swaddress1").toString()))+"' onchange='javascript:fVerifModifDirEntrega(this)' style='border-bottom:none;border-right:none;border-top:none;border-left:none;background-color:transparent;width:100%';>";
                    dirAux+= " <div id=dirEntregaDiv style=display:none>";
                    dirAux+= " <input type=text name=txtDirEntregaMensaje style='color:red;border-bottom:none;border-right:none;border-top:none;border-left:none;background-color:transparent;' size=17 value='Dirección modificada!' readOnly>";
                    dirAux+= " </div>";
                  
                    out.println("col.innerHTML = \"" + dirAux + "\";");
                                
                    out.println("col = row.insertCell(1);");
                    out.println("col.className = 'CellContent';");
                    out.println("col.align = 'left';");                     
                    out.println("col.innerHTML = '<input readonly type=text name=txtCity maxlength=200 value= \""+(MiUtil.getMessageClean(h.get("wv_swcity").toString()))+"\"  style=border-bottom:none;border-right:none;border-top:none;border-left:none;background-color:transparent;width:100%;>';");                            
             
                    out.println("col = row.insertCell(2);");
                    out.println("col.className = 'CellContent';");
                    out.println("col.align = 'left';");                       
                    out.println("col.innerHTML = '<input readonly type=text name=txtProvince maxlength=200 value= \""+(MiUtil.getMessageClean(h.get("wv_swprovince").toString()))+"\"  style=border-bottom:none;border-right:none;border-top:none;border-left:none;background-color:transparent;width:100%;>';");              
             
                    out.println("col = row.insertCell(3);");
                    out.println("col.className = 'CellContent';");
                    out.println("col.align = 'left';");                       
                    out.println("col.innerHTML = '<input readonly type=text name=txtState maxlength=200 value= \""+(MiUtil.getMessageClean(h.get("wv_swstate").toString()))+"\"  style=border-bottom:none;border-right:none;border-top:none;border-left:none;background-color:transparent;width:100%;>';");                                            
                    
                    out.println("col = row.insertCell(4);");
                    out.println("col.className = 'CellContent';");
                    out.println("col.align = 'center';");
                    out.println("col.innerHTML = \"<a href=javascript:fxShowAdress();>" + h.get("wv_swtipodirec") + "</a>&nbsp;&nbsp;<a href=javascript:fxEditAdress();><img src='"+Constante.PATH_APPORDER_SERVER+"/images/Editar.gif' border=0 align='absbottom'></a>\";");
                                    
                    strAux = "entro";                
                
                }else if(!("1".equals(strFlagDefault))&&("".equals(strAux))){
                    out.println(" frmdatos.hdnDeliveryAddress.value = ''");
                    out.println(" frmdatos.hdnDeliveryCity.value = ''");
                    out.println(" frmdatos.hdnDeliveryProvince.value = ''");
                    out.println(" frmdatos.hdnDeliveryState.value = ''");

                    out.println(" frmdatos.hdnDeliveryCityId.value = ''");
                    out.println(" frmdatos.hdnDeliveryProvinceId.value = ''");
                    out.println(" frmdatos.hdnDeliveryStateId.value = ''");
                    
                    dirAux = " &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input readonly type=text name=txtDirEntrega maxlength=200 value= '' onchange='javascript:fVerifModifDirEntrega(this)' style='border-bottom:none;border-right:none;border-top:none;border-left:none;background-color:transparent;width:100%';>";
                    dirAux+= " <div id=dirEntregaDiv style=display:none>";
                    dirAux+= " <input type=text name=txtDirEntregaMensaje style='color:red;border-bottom:none;border-right:none;border-top:none;border-left:none;background-color:transparent;' size=17 value='Dirección modificada!' readOnly>";
                    dirAux+= " </div>";
                  
                    out.println("col.innerHTML = \"" + dirAux + "\";");
                                
                    out.println("col = row.insertCell(1);");
                    out.println("col.className = 'CellContent';");
                    out.println("col.align = 'left';");                     
                    out.println("col.innerHTML = '<input readonly type=text name=txtCity maxlength=200 value= \"\"  style=border-bottom:none;border-right:none;border-top:none;border-left:none;background-color:transparent;width:100%;>';");                            
            
                    out.println("col = row.insertCell(2);");
                    out.println("col.className = 'CellContent';");
                    out.println("col.align = 'left';");                       
                    out.println("col.innerHTML = '<input readonly type=text name=txtProvince maxlength=200 value= \"\"  style=border-bottom:none;border-right:none;border-top:none;border-left:none;background-color:transparent;width:100%;>';");
           
                    out.println("col = row.insertCell(3);");
                    out.println("col.className = 'CellContent';");
                    out.println("col.align = 'left';");
                    out.println("col.innerHTML = '<input readonly type=text name=txtState maxlength=200 value= \"\" style=border-bottom:none;border-right:none;border-top:none;border-left:none;background-color:transparent;width:100%;>';");
              
                    out.println("col = row.insertCell(4);");
                    out.println("col.className = 'CellContent';");
                    out.println("col.align = 'center';");                    
                    out.println("col.innerHTML = \"<a href=javascript:fxShowAdress();>" + h.get("wv_swtipodirec") + "</a>&nbsp;&nbsp;<a href=javascript:fxEditAdress();><img src='"+Constante.PATH_APPORDER_SERVER+"/images/Editar.gif' border=0 align='absbottom'></a>\";");                    
                    strAux = "entro";
                  }
              }else{
              out.println("col.innerHTML = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + MiUtil.getMessageClean(h.get("wv_swaddress1").toString()) + "';");                

              out.println("col = row.insertCell(1);");
              out.println("col.className = 'CellContent';");
              out.println("col.align = 'left';");       
              out.println("col.innerHTML = '" + h.get("wv_swcity") + "';");              
         
              out.println("col = row.insertCell(2);");
              out.println("col.className = 'CellContent';");
              out.println("col.align = 'left';");         
              out.println("col.innerHTML = '" + h.get("wv_swprovince") + "';");              
         
              out.println("col = row.insertCell(3);");
              out.println("col.className = 'CellContent';");
              out.println("col.align = 'left';");         
              out.println("col.innerHTML = '" + h.get("wv_swstate") + "';");   
              
              out.println("col = row.insertCell(4);");
              out.println("col.className = 'CellContent';");
              out.println("col.align = 'center';");
              out.println("col.innerHTML = '" + h.get("wv_swtipodirec") + "';");
             } 
               
         
              //Por cambios en el Store Procedure se ha de invocar de otra manera los Check's
              //out.println("col.innerHTML = '<table><tr><td><input type=checkbox " + (((String)h.get("wv_swflaglegal")).equals("1")?"checked":"")  +  " readOnly /></td><td><input type=checkbox " + (((String)h.get("wv_swflagfacturacion")).equals("1")?"checked":"") + " readOnly /></td><td><input type=checkbox " + (((String)h.get("wv_swflagfacturacion")).equals("1")?"checked":"")  + " readOnly /></td></tr></table>';");
              
              dirAux = "";
              
              out.println("");
         }
         
         }else{
           out.println("alert('No se encontraron direcciones');");
         }
             
    }  
    
    
    /**
     Method : getShowContacts
     Purpose: Obtiene la visualizacion de datos de contactos ya sea por Customer o Site
     Developer       Fecha       Comentario
     =============   ==========  ======================================================================
     David Coca      26/07/2007  Creación
     */    
    public void getShowContacts (String strCodigo, String strTipoObjeto, HttpServletRequest request, PrintWriter out)  throws Exception {
        
          // Lista de contactos
          // --------------------
          
          HashMap lista_contactos2 = objCustomerService.getCustomerContacts2(MiUtil.parseLong(strCodigo),strTipoObjeto);
          ArrayList lista_contactos = (ArrayList)lista_contactos2.get("arrContactsList");
          HashMap h = new HashMap();
        
        
          out.println("var tablec = parent.mainFrame.document.getElementById('table_contacts');");
          out.println("var tablec_length = tablec.rows.length;"); 
          out.println("var col;");
          out.println("var j;");
             
          out.println("if (tablec_length > 0)");
          out.println("for (j=1; j < tablec_length; j++){");
          out.println("tablec.deleteRow(1);");
          out.println("}");                
          
          
          for( int i=0; i<lista_contactos.size();i++ ){
          
              h = (HashMap)lista_contactos.get(i);
              
              out.println("row = tablec.insertRow(-1);");
              
              out.println("col = row.insertCell(0);");
              out.println("col.className = 'CellContent';");
              //out.println("col.innerHTML = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + h.get("wv_swname") + "';");
              out.println("col.innerHTML = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + MiUtil.getMessageClean((String)h.get("wv_swname")) + "';");
              
              out.println("col = row.insertCell(1);");
              out.println("col.className = 'CellContent';");
              out.println("col.align = 'right';");       
              out.println("col.innerHTML = '" + MiUtil.getString((String)h.get("wv_swphonearea")) + "';");
              
              out.println("col = row.insertCell(2);");
              out.println("col.className = 'CellContent';");
              out.println("col.align = 'right';");         
              out.println("col.innerHTML = '" + MiUtil.getString((String)h.get("wv_swphone")) + "';");
              
              out.println("col = row.insertCell(3);");
              out.println("col.className = 'CellContent';");
              out.println("col.align = 'right';");         
              out.println("col.innerHTML = '" + MiUtil.getString((String)h.get("wv_swfaxarea")) + "';");
              
              out.println("col = row.insertCell(4);");
              out.println("col.className = 'CellContent';");
              out.println("col.align = 'right';");         
              out.println("col.innerHTML = '" + MiUtil.getString((String)h.get("wv_swfax")) + "';");
          
              out.println("col = row.insertCell(5);");
              out.println("col.className = 'CellContent';");
              out.println("col.align = 'center';");         
              out.println("col.innerHTML = '<table><tr><td><input type=checkbox " + (((String)h.get("wv_swflagusuario")).equals("1")?"checked":"")  +  " disabled /></td><td><input type=checkbox " + (((String)h.get("wv_swflagfacturacion")).equals("1")?"checked":"") + " disabled /></td><td><input type=checkbox " + (((String)h.get("wv_swflaggerenteg")).equals("1")?"checked":"")  + " disabled /></td></tr></table>';");
              //out.println("col.innerHTML = '<table><tr><td>" + (((String)h.get("wv_swflagusuario")).equals("1")?"<img src=websales/images/Ok1.gif >":"")  +  "</td><td>" + (((String)h.get("wv_swflagfacturacion")).equals("1")?"<img src=websales/images/Ok1.gif >":"") + "</td><td>" + (((String)h.get("wv_swflaggerenteg")).equals("1")?"<img src=websales/images/Ok1.gif >":"")  + "</td></tr></table>';");
                
          }
    }
  
  /**
  Method : getShowSiteData
  Purpose: Obtiene los datos de los Sites
  Developer       Fecha       Comentario
  =============   ==========  ======================================================================
  David Coca      27/07/2007  Creación
  */
  public void getShowSiteData (String strCodigoSite, HttpServletRequest request, PrintWriter out)  throws Exception {
        
    String  strCreatedby = request.getParameter("hdnSessionLogin");
    String  strMessage   = null;    
    long    intRegionid      = MiUtil.parseLong((String)request.getParameter("hdnRegionId"));    
    long    intUserid        = MiUtil.parseLong((String)request.getParameter("hdnIUserId"));
    long    intAppid         = MiUtil.parseLong((String)request.getParameter("hdnIAppId"));
    System.out.println("[CustomerServlet][getShowSiteData]");
    String strGeneratortype  = request.getParameter("hdngeneratortype");
    System.out.println("[CustomerServlet][getShowSiteData][strGeneratortype]"+strGeneratortype);
    String strGeneratorId    = request.getParameter("hdngeneratorId");
    System.out.println("[CustomerServlet][getShowSiteData][strGeneratorId]"+strGeneratorId);
    System.out.println("[CustomerServlet][getShowSiteData][strCodigoSite]"+strCodigoSite);    
    System.out.println("[CustomerServlet][getShowSiteData][intUserid]"+intUserid);	 
	 
    Hashtable hshSiteData = objCustomerService.CustomerDAOgetSiteData(MiUtil.parseLong(strCodigoSite), strCreatedby, intUserid, intAppid, intRegionid,
                                                                      strGeneratortype, strGeneratorId);
    
    strMessage = (String)hshSiteData.get("strMessage");
    
    strMessage = strMessage.equals("null")?null:strMessage;
    
    if( strMessage!=null ){
      clearCustomer(request,out);
      throw new Exception((String)hshSiteData.get("strMessage"));
    }
    
    /*Se obtiene nombre del Dealer*/
    String wv_dealer = "";    
    if (strGeneratortype.equals("OPP")){    
       String strDealerName = null;
       String strVendedor = (String)request.getParameter("hdnVendedorId");         
       System.out.println("strVendedor======"+strVendedor);
       if (  !MiUtil.getString(strVendedor).equalsIgnoreCase("")   ){            
         HashMap objHashMapDealer = objGeneralService.getDealerBySalesman(MiUtil.parseLong(strVendedor));              
         if( objHashMapDealer.get("strMessage") != null ){                            
           throw new Exception((String)objHashMapDealer.get("strMessage"));
         }            
         strDealerName = MiUtil.getString((String)objHashMapDealer.get("strDealerName"));            
         if (strDealerName != null)
           strDealerName = strDealerName.equalsIgnoreCase("null")?"":strDealerName;
         
         
         //if ( MiUtil.getString(wv_dealer).equalsIgnoreCase("") || MiUtil.getString(wv_dealer).equalsIgnoreCase("null") ){              
           wv_dealer = strDealerName;
         //}
       }
    }
    out.println("var form = parent.mainFrame.document.frmdatos;"); 
    
    out.println("form.txtCodBSCS.value          = '" + hshSiteData.get("wv_codbscs") + "';");
    out.println("form.txtLineaCredito.value     = '" + hshSiteData.get("wv_nplineacredito") + "';");
    out.println("form.txtRegionSite.value       = '" + hshSiteData.get("wv_swnameregion") + "';");
    out.println("form.cmbVendedor.value         = '" + hshSiteData.get("wn_vendedorid") + "';");
    out.println("form.hdnVendedor.value         = '" + hshSiteData.get("wn_vendedorid") + "';");
	 	 
	 if (hshSiteData.get("wn_vendedorid") == null || hshSiteData.get("wn_vendedorid").equals("") || hshSiteData.get("wn_vendedorid").equals("0")){		
		out.println("form.cmbVendedor.value         = '" + hshSiteData.get("wn_vendedorid2") + "';");
		out.println("form.hdnVendedor.value         = '" + hshSiteData.get("wn_vendedorid2") + "';");		
	 }
    //out.println("form.txtDealer.value           = '" + hshSiteData.get("wv_dealer") + "';");
    out.println("form.txtDealer.value           = '" + wv_dealer + "';");
    

  }
  
  public void setControlsByParamaters(HttpServletRequest request, PrintWriter out,String strCodigoCliente) throws Exception {    
    String strSpec                = null;
    String strSiteId              = "";
    String strSalesId             = "";
    String strGeneratorType       = "";
    String strCustomerId          = "";
    String strLogin               = "";
    String strOpportunityTypeId   = "";
    String strFlagGenerator       = "";
    String strSpecificationId     = "";
    String strDivisionId          = "";
    //cirazabal
    String strGeneratorId       = "";
    
    strSpec                 = request.getParameter("strSpecFlag");
    
    //Parámetros de OPP
    strSiteId               = MiUtil.getString(request.getParameter("hdnSiteOppId"));
    strSalesId              = MiUtil.getString(request.getParameter("hdnSalesTeamOppId"));    
    strDivisionId           = MiUtil.getString(request.getParameter("strDivisionId"));
    strSpecificationId      = MiUtil.getString(request.getParameter("strSpecificationId"));
    strOpportunityTypeId    = request.getParameter("strOpportunityTypeId");
    strGeneratorType        = MiUtil.getString(request.getParameter("strGeneratorType"));
 
    
    //strFlagGenerator        = "S";
    strFlagGenerator        = request.getParameter("strFlagGenerator");
    strGeneratorId        = request.getParameter("hdnGeneratorId");
    
    System.out.println("[CustomerServlet][setControlsByParamaters][strSiteId]"+strSiteId);
    System.out.println("[CustomerServlet][setControlsByParamaters][strSalesId]"+strSalesId);
    System.out.println("[CustomerServlet][setControlsByParamaters][strGeneratorType]"+strGeneratorType);        
    System.out.println("[CustomerServlet][setControlsByParamaters][strFlagGenerator]"+strFlagGenerator);
    System.out.println("[CustomerServlet][setControlsByParamaters][strDivisionId]"+strDivisionId);
    System.out.println("[CustomerServlet][setControlsByParamaters][strSpecificationId]"+strSpecificationId);
    System.out.println("[CustomerServlet][setControlsByParamaters][strOpportunityTypeId]"+strOpportunityTypeId);
    //Si es una orden desde OPP o INC
    if( !strGeneratorType.equals("") ){
       strCustomerId           = request.getParameter("strCustomerId");
       strLogin                = request.getParameter("strLogin");
    }else{
       strCustomerId           = MiUtil.getString(request.getParameter("hdnCustId"));       
       if( strCustomerId.equals("") )
       strCustomerId   = strCodigoCliente;       
       strLogin                = request.getParameter("hdnSessionLogin");
    }
    
    NewOrderService   objNewOrderService              = new NewOrderService();
    GeneralService    objGeneralServiceSpecification  = new GeneralService();
    HashMap           objHashMapSpecificationUserList = new HashMap();
    ArrayList         objArraySpecificationUserList   = null;
    SpecificationBean objSpecificationBean            = null;
    int               intCntSol                       = -1;
    
    //out.println("parent.mainFrame.DeleteSelectOptions(form.cmbSolucion);");
	 out.println("parent.mainFrame.DeleteSelectOptions(form.cmbDivision);");
    out.println("parent.mainFrame.DeleteSelectOptions(form.cmbCategoria);");
    out.println("parent.mainFrame.DeleteSelectOptions(form.cmbSubCategoria);");
            
    out.println("parent.mainFrame.VsubjectTypeSpec.removeElementAll();");
    out.println("parent.mainFrame.Vdivision.removeElementAll();");
    out.println("parent.mainFrame.Vcategory.removeElementAll();");
    out.println("parent.mainFrame.VcategorySubCategory.removeElementAll();");
        
    if (MiUtil.getString(request.getParameter("hdnGeneratorType")).trim().equals("MASSIVE")) {
      strGeneratorType        = MiUtil.getString(request.getParameter("hdnGeneratorType"));      
    }    
        
    objHashMapSpecificationUserList  =   objNewOrderService.SpecificationDAOgetSpecificationUserList
    (
    MiUtil.parseLong(strCustomerId),
    strLogin,
    strGeneratorType,
    strOpportunityTypeId,
    strFlagGenerator,
    MiUtil.parseLong(strDivisionId),
    MiUtil.parseLong(strSpecificationId),
    MiUtil.parseLong(strGeneratorId)
    );
    
    if( objHashMapSpecificationUserList == null || objHashMapSpecificationUserList.get("strMessage")!=null )
      throw new Exception((String)objHashMapSpecificationUserList.get("strMessage"));
    
    objArraySpecificationUserList = (ArrayList)objHashMapSpecificationUserList.get("objArrayList");
    intCntSol                     =   MiUtil.parseInt((String)objHashMapSpecificationUserList.get("intCntSol"));
              
    //out.println("parent.mainFrame.alert('Hey I: ' + parent.mainFrame.VsubjectTypeSpec.size());");    
    //out.println("parent.mainFrame.alert('Hey Vsolution: ' + parent.mainFrame.Vsolution.size());");
    //out.println("parent.mainFrame.alert('Hey Vcategory: ' + parent.mainFrame.Vcategory.size());");
    //out.println("parent.mainFrame.alert('Hey VcategorySubCategory: ' + parent.mainFrame.VcategorySubCategory.size());");
    
    //Cargo la data de las espcificaciones y categorías
    if(objArraySpecificationUserList!=null) {
      for( int i=0; i<objArraySpecificationUserList.size(); i++ ){    
          objSpecificationBean  = new SpecificationBean();
          objSpecificationBean  = (SpecificationBean)objArraySpecificationUserList.get(i);
          //out.println("parent.mainFrame.VsubjectTypeSpec.addElement(new parent.mainFrame.makeSpecificationCategorySubCategorySolution("+objSpecificationBean.getNpSolutionId()+",'"+objSpecificationBean.getNpsolutionname()+"','"+objSpecificationBean.getNpcategorynname()+"',"+objSpecificationBean.getNpSpecificationId()+",'"+objSpecificationBean.getNpSpecification()+"'))");
			 out.println("parent.mainFrame.VsubjectTypeSpec.addElement(new parent.mainFrame.makeSpecificationCategorySubCategoryDivision("+objSpecificationBean.getNpDivisionId()+",'"+objSpecificationBean.getNpdivisionname()+"','"+objSpecificationBean.getNpcategorynname()+"',"+objSpecificationBean.getNpSpecificationId()+",'"+objSpecificationBean.getNpSpecification()+"'))");
        }
    }    
    out.println("parent.mainFrame.fx_fillElementsVector();");
    //out.println("parent.mainFrame.fx_fillSolution();");
	 out.println("parent.mainFrame.fx_fillDivision();");
	 //out.println("debugger;");
    
    
    if( ! strSiteId.trim().equals("") ) {
      searchCustomerBySite(strSiteId,request,out);
      out.println("form.cmbResponsablePago.value='"+strSiteId+"';");
      out.println("form.cmbResponsablePago.disabled=true;");
      out.println("form.hdnSite.value='"+strSiteId+"';");
    }
    
    if( strSpec != null && intCntSol != -1 ){
      HashMap hshData        = objGeneralServiceSpecification.getSpecificationDetail(MiUtil.parseLong(strSpecificationId));
      String  strCategory    = "";
      if ( (String)hshData.get("strMessage") != null )
          throw new Exception((String)hshData.get("strMessage"));      
      objSpecificationBean = (SpecificationBean)hshData.get("objSpecifBean");  
      strCategory    =  objSpecificationBean.getNpType();      
      //out.println("form.cmbSolucion.value = "+intCntSol+";");
		out.println("form.cmbDivision.value = "+intCntSol+";");
      out.println("parent.mainFrame.fx_fillCategory("+intCntSol+");");      
      out.println("form.cmbCategoria.value = '" + strCategory + "';");
      out.println("parent.mainFrame.fx_fillSubCategory('"+strCategory+"')");      
      out.println("form.cmbSubCategoria.value = "+strSpecificationId+";");
      out.println("form.cmbSubCategoria.focus();");      
    }
        
    //Set para el vendedor
    if( ! strSalesId.equals("") ){
      out.println("form.cmbVendedor.value='"+strSalesId+"';");
      out.println("form.hdncmbVendedor.value='"+strSalesId+"';");
    }
    
    //Inhabilitar las entradas para cambiar el CustomerId
    if( ! strGeneratorType.equals("") ){
      out.println("form.txtCompany.disabled=true;");
      out.println("form.txtDato.disabled=true;");
    }        
    out.println("parent.mainFrame.fxLoadOrderDetail();");    
  }
  
    
  public void loadBillingAccountBySiteId(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException{
    ArrayList objArrayBillingAccountExitstList    = new ArrayList();
    ArrayList objArrayBillingAccountSolicitedList = new ArrayList();
    HashMap   objHashMap = null;
    String strTypeCustomer  =   MiUtil.getString((String)request.getParameter("typeCustomer"));
    String strPage          =   MiUtil.getString((String)request.getParameter("strPage"));
    String strNameObject    =   MiUtil.getString((String)request.getParameter("strNameObject"));
    long   lngSiteId        =   MiUtil.parseLong((String)request.getParameter("strSiteId"));
    long   lngCustId        =   MiUtil.parseLong((String)request.getParameter("strCustomerId"));
    long   lngOrderId       =   MiUtil.parseLong((String)request.getParameter("strOrderId"));
    String strIndex         =   MiUtil.getString((String)request.getParameter("strIndex"));
    long lngCustBSCSId      =   0;
    //long   lngCustId       =   MiUtil.parseLong((String)request.getParameter("strCustId"));
    //long   lngCustBSCSId   =   MiUtil.parseLong((String)request.getParameter("strCustBSCSId"));
    
    //Si es un FLAT y el Site es nulo obtenemos los BA solicitados del Customer
    if( lngSiteId == 0 ){//&& strTypeCustomer.equals("FLAT") ){
      lngCustBSCSId     = (new CustomerService()).getCustomerIdBSCS(lngCustId,Constante.CUSTOMERTYPE_CUSTOMER);	
      objHashMap = (new BillingAccountService()).BillingAccountDAOgetBillingAccountList(lngCustBSCSId);
      objArrayBillingAccountExitstList = (ArrayList)objHashMap.get("objArrayList");
      
      objHashMap = (new BillingAccountService()).BillingAccountDAOgetAccountList(lngSiteId,lngCustId,lngOrderId);
      objArrayBillingAccountSolicitedList = (ArrayList)objHashMap.get("objArrayList");
    }else{
      lngCustBSCSId     = (new CustomerService()).getCustomerIdBSCS(lngSiteId,Constante.CUSTOMERTYPE_SITE);
      objHashMap = (new BillingAccountService()).BillingAccountDAOgetBillingAccountList(lngCustBSCSId);
      objArrayBillingAccountExitstList = (ArrayList)objHashMap.get("objArrayList");
      
      objHashMap = (new BillingAccountService()).BillingAccountDAOgetAccountList(lngSiteId,lngCustId,lngOrderId);
      objArrayBillingAccountSolicitedList = (ArrayList)objHashMap.get("objArrayList");
    }
    
    
    //objHashMap = (new BillingAccountService()).BillingAccountDAOgetBillingAccountList(lngCustBSCSId);
    //objArrayBillingAccountExitstList = (ArrayList)objHashMap.get("objArrayList");
    
    request.removeAttribute("strIndex");
    request.removeAttribute("objArrBillAccExtLst");
    request.removeAttribute("objArrBillAccSolcLst");
    request.removeAttribute("strPage");
    request.removeAttribute("strNameObject");
    
    request.setAttribute("strPage",strPage);
    request.setAttribute("strIndex",strIndex);
    request.setAttribute("strNameObject",strNameObject);
    request.setAttribute("objArrBillAccExtLst",objArrayBillingAccountExitstList);
    request.setAttribute("objArrBillAccSolcLst",objArrayBillingAccountSolicitedList);
    request.getRequestDispatcher("pages/loadBillingAccountBySiteId.jsp").forward(request, response);
    
  }
  
  
  /**
     * @see GenericServlet#executeDefault(HttpServletRequest, HttpServletResponse)
     */
    public void executeDefault(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                                                                                                IOException {
        try {
            logger.debug("Antes de hacer el sendRedirect");
            response.sendRedirect("/portal/page/portal/orders/items");
        } catch (Exception e) {
            logger.error(formatException(e));
        }
    }
    
    
    
    
}
