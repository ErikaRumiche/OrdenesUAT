package pe.com.nextel.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pe.com.nextel.bean.ContractServiceBscsBean;
import pe.com.nextel.bean.PlanTarifarioBean;
import pe.com.nextel.service.CustomerService;
import pe.com.nextel.service.MassiveOrderService;
import pe.com.nextel.service.NewOrderService;
import pe.com.nextel.util.Constante;
import pe.com.nextel.util.MiUtil;


public class MassiveOrderServlet extends GenericServlet{

  private static final String CONTENT_TYPE       = "text/html; charset=UTF-8";
  
  CustomerService objCustomerService = new CustomerService();
  
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }
  
  /**
  Method : doGet
  Developer       Fecha       Comentario
  =============   ==========  ======================================================================
  Evelyn Ocampo   30/07/2009  Creación
  */
  public void doGet(HttpServletRequest request, 
                    HttpServletResponse response) throws ServletException, IOException {
                      
    response.setContentType(CONTENT_TYPE);
    PrintWriter out = response.getWriter();
    HashMap h = new HashMap();
    // Variables para estandar.              
    String  strMyaction         = null;
    String  strDetalleMyaction  = null;
    String  strSiteId           = "";
    /*String  strCreatedby        = request.getParameter("hdnSessionCreatedby");
    long    intUserid           = MiUtil.parseLong((String)request.getParameter("hdnSessionUserid"));
    long    intRegionid         = MiUtil.parseLong((String)request.getParameter("hdnSessionRegionId"));
    int     intAppid            = MiUtil.parseInt((String)request.getParameter("hdnSessionRolId"));
    int    hdnIUserId           = MiUtil.parseInt((String)request.getParameter("hdnIUserId"));
    int    hdnIAppId            = MiUtil.parseInt((String)request.getParameter("hdnIAppId"));

    System.out.println("hdnIUserId : " + hdnIUserId);
    System.out.println("hdnIAppId : " + hdnIAppId); */
      
    try{
      
    out.println("<html>");
    out.println("<head><title>MassiveOrderServlet</title></head>");
       
    out.println("<script language='JavaScript' src='"+Constante.PATH_APPORDER_SERVER+"/Resource/BasicOperations.js'></script>");
    out.println("<script language='JavaScript'>");
      
    //Captura la acción a realizar.
    strMyaction         = request.getParameter("myaction");
    strDetalleMyaction  = request.getParameter("detallemyaction");
    
    System.out.println("strMyaction================>"+strMyaction);
    // Bucle estandar de seleccion de acciones.
      if ( strMyaction != null ) {          
        //Acción: buscarCliente 
        if(strMyaction.equals("buscarCliente")) {          
          //Buscar por descripción de la compañía
          if(strDetalleMyaction.equals("compania")) {           
            doSearchCustomerByDescription(request,out,response); 
          }
          if(strDetalleMyaction.equals("id")){
            doSearchCustomerById(request,out,response);
          } 
          if(strDetalleMyaction.equals("siteId")) {           
            doSearchCustomerBySiteId(request,out,response); 
          }
          if(strDetalleMyaction.equals("companiaName")) {           
            doSearchCustomerByName(request,out,response); 
          }//Obtiene SSAA         
        }else if(strMyaction.equals("obtenerServicios")){
           doGetServiceContract(request,out);           
           request.getRequestDispatcher("pages/loadItemsMassiveContractService.jsp").forward(request, response);
        }else if(strMyaction.equals("obtenerServiciosPlan")){
           doGetServiceContractPlan(request,out);           
           request.getRequestDispatcher("pages/loadItemsMassiveContractPlan.jsp").forward(request, response);   
        }else if(strMyaction.equals("limpiarCliente")) {
            clearMassiveCustomer(request, response);
        }
                        
      }
      
    }catch(Exception ex){
      //ex.printStackTrace();
      System.out.println("Massive Servlet : " + ex.getClass() + " - "+ ex.getMessage());
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
  Evelyn Ocampo   30/07/2009  Creación
  */
  public void doSearchCustomerByDescription(HttpServletRequest request, PrintWriter out, HttpServletResponse response) throws Exception{
    
    HashMap   hshCutomer = new HashMap();
    String    strLogin;
    int       intRegionId;
    String    strMessage = null;
    String    wv_customer = "";
    
    HashMap   HashItemOrder = new HashMap();
    HashMap   HashItemPlan = new HashMap();
    HashMap   HashServiceList = new HashMap();
    HashMap   HashServiceBySolutionList = new HashMap();
    HashMap   objSiteHashMap = new HashMap();
    HashMap   HashCutomer = new HashMap();
    
    ArrayList objArrayItemOrder  = new ArrayList();
    ArrayList objArrayPlanList   = new ArrayList();
    ArrayList objArrayServiceList = new ArrayList(); 
    ArrayList objArrayServiceBySolutionList = new ArrayList(); 
    
    
    MassiveOrderService  objItemMassiveOrderService  = new MassiveOrderService();
    NewOrderService      objNewOrderService = new NewOrderService();
    PlanTarifarioBean    pBean = new PlanTarifarioBean();
    MassiveOrderService  objMassiveOrderService  = new MassiveOrderService();
    
    
    String    strCustomerDescription;   
    String    strSpecificationId;
    String    strDivisionId;
    String    strnpSite=null;
    String    strNpSiteId=null;
    String    strSolutionId = null;
    String    strCompanyType;
    String    strPlan = "";
    String    strGeneratorType,strGeneratorTypeRepCalCap;
    int       wn_swcustomerid = 0;
    String    wv_swname = "";
    String    strCantSite = null;
    
    intRegionId               = MiUtil.parseInt(request.getParameter("hdnRegionId"));
    strLogin                  = MiUtil.getString(request.getParameter("hdnSessionLogin"));
    int iUserId               = MiUtil.parseInt((String)request.getParameter("hdnIUserId"));
    int iAppId                = MiUtil.parseInt((String)request.getParameter("hdnIAppId"));
    strGeneratorType          = MiUtil.getString(request.getParameter("strGeneratorType"));
    strGeneratorTypeRepCalCap = (String)request.getParameter("hdnOrigenType");
    String strGeneratorId     = (request.getParameter("strGeneratorId")==null?"":request.getParameter("strGeneratorId")); 
    
    strCustomerDescription    = (String)request.getParameter("hdnCompanyaName");
    strSpecificationId        = (String)request.getParameter("hdnSpecificationId");
    strDivisionId             = (String)request.getParameter("hdnDivisionId");    
    strCompanyType            = Constante.CUSTOMERTYPE_CUSTOMER; //(String)request.getParameter("hdnCompanyType");
    String strCodigoClienteAcept     = request.getParameter("txtCompanyId");
    strNpSiteId               = (String)request.getParameter("hdnNpSiteId")==null?"0":(String)request.getParameter("hdnNpSiteId"); 
    strCantSite               = (String)request.getParameter("hdnCantSiteAcep");
    
    
    
    System.out.println("[MassiveOrderServlet][strCodigoClienteAcept]---------->"+strCodigoClienteAcept);
    System.out.println("[MassiveOrderServlet][strCustomerDescription]--------->"+strCustomerDescription);
    System.out.println("strCompanyType----------------------->"+strCompanyType);
    System.out.println("strCantSite----------------------->"+strCantSite);
    System.out.println("strNpSiteId----------------------->"+strNpSiteId);
    Hashtable hshCustPattern = objCustomerService.CustomerDAOgetCustPatternQty(strCustomerDescription);
    
    strMessage = (String)hshCustPattern.get("strMessage");
    
    strMessage = strMessage.equals("null")?null:strMessage;
    
    if( strMessage!=null ){
        throw new Exception(strMessage);
    }
    
    
    System.out.println("strCustomerDescription========"+strCustomerDescription+"-"+(String)hshCustPattern.get("wn_customerid"));
    
    //Si solo hubo un encuentro 
    if ( MiUtil.parseInt((String)hshCustPattern.get("wn_customer_qty")) == 1  ) {   
    System.out.println("En Customerid = 1");
      if ( MiUtil.parseInt((String)hshCustPattern.get("wn_customerid")) != 0 ){
      System.out.println("En Customerid != 0");
        if (MiUtil.parseInt(strCantSite) != 0 && MiUtil.parseInt(strNpSiteId) == 0){
           out.println("alert('Debe Seleccionar un Responsable de Pago.');");
        }else{
            if (hshCustPattern.get("wn_customerid").equals(strCodigoClienteAcept)){
              out.println("alert('Cliente Cedente debe ser diferente a Cliente Aceptante.');");    
            }else{         
                                
                HashCutomer = objCustomerService.CustomerDAOgetCustomerData( MiUtil.parseLong((String)hshCustPattern.get("wn_customerid")), strLogin, MiUtil.parseLong(""+intRegionId),iUserId,iAppId,strGeneratorId,strGeneratorTypeRepCalCap);
                
                if( HashCutomer.get("strMessage")!=null )
                    throw new Exception((String)HashCutomer.get("strMessage"));
                       
                if( (String)HashCutomer.get("strMessage") == null) {
                    
                    if (HashCutomer.size()!=0) {
                      wn_swcustomerid     = MiUtil.parseInt((String)HashCutomer.get("wn_swcustomerid"));
                      if (wn_swcustomerid==0)
                        throw new Exception("No se encontró Compañía");
                        
                      wv_swname           = (String)HashCutomer.get("wv_swname"); 
                      
                      System.out.println("Nombre de Compania========================"+wv_swname);
                      
                      if (MiUtil.getString(wv_swname)=="")
                        throw new Exception("No se encontró Compañía");
                               
                      request.setAttribute("wv_swname",wv_swname);
                      
                      // Lista de Sites
                      
                      objSiteHashMap =  objCustomerService.getCustomerSites(MiUtil.parseLong((String)hshCustPattern.get("wn_customerid")),"Activo");
              
                      if( objSiteHashMap.get("strMessage") != null )
                         throw new Exception((String)objSiteHashMap.get("strMessage"));
                    
                      ArrayList lista_sites = (ArrayList)objSiteHashMap.get("objArrayList");
                      
                      request.setAttribute("lista_sites",lista_sites);
                      
                      wv_customer = (String)hshCustPattern.get("wn_customerid");
                      request.setAttribute("wv_customer",wv_customer);
                      
                      request.getRequestDispatcher("pages/loadBillingAcountMassiveTransfer.jsp").forward(request, response); 
                      
                    }
                    
          
                }
                
                /*request.setAttribute("lista_sites",lista_sites);
                
                request.getRequestDispatcher("pages/loadItemsMassiveTransfer.jsp").forward(request, response);*/
          
                /*HashItemOrder     = objItemMassiveOrderService.MassiveOrderDAOgetItemOrder(MiUtil.parseLong((String)hshCustPattern.get("wn_customerid")),MiUtil.parseLong(strnpSite),MiUtil.parseLong(strSpecificationId));
                objArrayItemOrder = (ArrayList)HashItemOrder.get("objArrayList"); 
                request.setAttribute("objArrayItemOrder",objArrayItemOrder);
                
                      
                pBean.setNptipo2("0");
                pBean.setNpsolutionid(MiUtil.parseInt(strSolutionId));
                pBean.setNpspecificationid(MiUtil.parseInt(strSpecificationId));
                          
                HashItemPlan = objNewOrderService.PlanDAOgetPlanList(pBean,strCompanyType);
                objArrayPlanList  = (ArrayList)HashItemPlan.get("objPlanList"); 
                request.setAttribute("objArrayPlanList",objArrayPlanList);
                
                //Obtenemos los ssaa
                HashServiceList  = objMassiveOrderService.getServiceList(MiUtil.parseInt(strDivisionId),MiUtil.parseInt(strPlan));
                objArrayServiceList  = (ArrayList)HashServiceList.get("objServiceList"); 
                request.setAttribute("objArrayServiceList",objArrayServiceList);
                
                //Obtenemos los SSAA por solución
                HashServiceBySolutionList  = objMassiveOrderService.getServiceListBySolution(MiUtil.parseInt(strDivisionId));
                objArrayServiceBySolutionList  = (ArrayList)HashServiceBySolutionList.get("objServiceBySolutionList");
                request.setAttribute("objArrayServiceBySolutionList",objArrayServiceBySolutionList);  
                
               
                System.out.println("XXXXXXXXXXXXXXX==="+MiUtil.parseLong((String)hshCustPattern.get("wn_customerid")));   
                wv_customer = (String)hshCustPattern.get("wn_customerid");
                request.setAttribute("wv_customer",wv_customer);
                request.getRequestDispatcher("pages/loadItemsMassiveTransfer.jsp").forward(request, response);*/
            }
        }
      }else{
         System.out.println("En customerid = doSearchCustomerByDescription");
         out.println("alert('Especifique mejor su búsqueda.');");
         out.println("parent.mainFrame.searchCustomerMassive()");
        
      }
    }
    //Si hay mas de dos encuentros
    else {
      System.out.println("en no entraa doSearchCustomerByDescription===================");
      out.println("alert('Especifique mejor su búsqueda.');");
      out.println("parent.mainFrame.searchCustomerMassive()");
    }
    
  }
  
  /**
  Method : doGetServiceContract
  Developer       Fecha       Comentario
  =============   ==========  ======================================================================
  Henry Rengifo   05/08/2009  Creación
  */
  public void doGetServiceContract(HttpServletRequest request, PrintWriter out) throws Exception{
    MassiveOrderService  objItemMassiveOrderService  = new MassiveOrderService();
    ContractServiceBscsBean objContractServiceBscsBean;
    HashMap hResult = new HashMap();
    HashMap hItemServices = new HashMap();
    //HashMap hResultMessage = new HashMap();
    ArrayList list = new ArrayList();    
    ArrayList listServ = new ArrayList();    
    
    String[] codContrato = request.getParameterValues("hdnItemCoId");
    String[] chkPhone    = request.getParameterValues("hdnchkPhone");
    String[] ItemSolutionId  = request.getParameterValues("hdnItemSolutionId");
    String[] hdnGetServicio = request.getParameterValues("hdnGetServicio");  
    //String[] hdnItemPhoneNumber = request.getParameterValues("txtItemPhoneNumber");
    //String  strSpecificationId    = (String)request.getParameter("hdnSpecificationId");
    //String  strOrderId   = (String)request.getParameter("hdnNpOrderId");
    
    for(int i=0;i<chkPhone.length;i++){
    
    System.out.println(i+" - Contrato -"+codContrato[i]+" <-->"+" check "+chkPhone[i]+"   solucion  - "+ItemSolutionId[i]);
    
        if (chkPhone[i].equals("on") && codContrato[i]!= null && hdnGetServicio[i].equals("false")){
          objContractServiceBscsBean = new ContractServiceBscsBean();
          hResult = objItemMassiveOrderService.getCommercialService(Long.parseLong(codContrato[i]));  
          //hResultMessage = objItemMassiveOrderService.getValidateByPhone(Long.parseLong(hdnItemPhoneNumber[i].substring(3,12)),Long.parseLong(strSpecificationId),Long.parseLong(strOrderId));
          
          objContractServiceBscsBean.setHiddenId(i);
          objContractServiceBscsBean.setServiceCode((String)hResult.get("objCommercialService"));
          //objContractServiceBscsBean.setStrMessage((String)hResultMessage.get("strMessage")==null?"":(String)hResultMessage.get("strMessage"));
          //objContractServiceBscsBean.setStrTypeError((String)hResultMessage.get("strTypeError"));
          list.add(objContractServiceBscsBean);   
          
          hItemServices = objItemMassiveOrderService.getServiceItemListBySolution(MiUtil.parseInt(ItemSolutionId[i]));
          listServ.add(hItemServices);
        }
    }       
    request.setAttribute("setAttService", list);
    request.setAttribute("setAttServiceBySol", listServ);
    
  }
  
    /**
   Method : doSearchCustomerById
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Evelyn Ocampo   10/08/2009  Creación
   */
   public void doSearchCustomerById(HttpServletRequest request, PrintWriter out,HttpServletResponse response) throws Exception{
   
    Enumeration paramNames = request.getParameterNames();
    while(paramNames.hasMoreElements()) {
      String paramName = (String)paramNames.nextElement();            
      String[] paramValues = request.getParameterValues(paramName);
      String paramValue = paramValues[0];
      System.out.println("paramName ["+paramName+"] -- Valor ["+paramValue+"]");
    }
   
   
   
    String    strCodigoCliente="0";    
    String    strLogin,strGeneratorType,strGeneratorTypeRepCalCap;
    int       intRegionId;
    String    strSolutionId = null;
    String    strCompanyType;
    String    strPlan = "";
    String    strSpecificationId;
    String    strDivisionId;
    String    strnpSite=null;
    int       wn_swcustomerid = 0;
    String    wv_swname = "";
    String    strNpSiteId = null;
    String    wv_customer = "";
    String    strCantSite = null;
        
    HashMap   HashItemOrder = new HashMap();
    HashMap   HashItemPlan = new HashMap();
    HashMap   HashServiceList = new HashMap();
    HashMap   HashServiceBySolutionList = new HashMap();
    HashMap   HashCutomer = new HashMap();
    HashMap   objSiteHashMap = new HashMap();
    
    ArrayList objArrayItemOrder  = new ArrayList();
    ArrayList objArrayPlanList   = new ArrayList();
    ArrayList objArrayServiceList = new ArrayList(); 
    ArrayList objArrayServiceBySolutionList = new ArrayList(); 
    
    
    MassiveOrderService  objItemMassiveOrderService  = new MassiveOrderService();
    NewOrderService      objNewOrderService = new NewOrderService();
    PlanTarifarioBean    pBean = new PlanTarifarioBean();
    MassiveOrderService  objMassiveOrderService  = new MassiveOrderService();
  
    intRegionId               = MiUtil.parseInt(request.getParameter("hdnRegionId"));
    strLogin                  = MiUtil.getString(request.getParameter("hdnSessionLogin"));
    int iUserId               = MiUtil.parseInt((String)request.getParameter("hdnIUserId"));
    int iAppId                = MiUtil.parseInt((String)request.getParameter("hdnIAppId"));
    strGeneratorType          = MiUtil.getString(request.getParameter("strGeneratorType"));
    strGeneratorTypeRepCalCap = (String)request.getParameter("hdnOrigenType");
    String strGeneratorId     = (request.getParameter("strGeneratorId")==null?"":request.getParameter("strGeneratorId")); 
    strSpecificationId        = (String)request.getParameter("hdnSpecificationId");
    strDivisionId             = (String)request.getParameter("hdnDivisionId");    
    strCompanyType            = Constante.CUSTOMERTYPE_CUSTOMER;

    strCodigoCliente          = request.getParameter("hdnCustId1");
    String strCodigoClienteAcept     = request.getParameter("txtCompanyId");
    strNpSiteId               = (String)request.getParameter("hdnNpSiteId")==null?"0":(String)request.getParameter("hdnNpSiteId"); 
    strCantSite               = (String)request.getParameter("hdnCantSiteAcep");    
    
	  System.out.println("[MassiveOrderServlet][doSearchCustomerById]strCodigoClienteAcept----------------------: "+strCodigoClienteAcept+"");
    System.out.println("[MassiveOrderServlet][doSearchCustomerById]strCodigoCliente---------------------------: "+strCodigoCliente+"");
	  System.out.println("[MassiveOrderServlet][doSearchCustomerById]strLogin: "+strLogin);
	  System.out.println("[MassiveOrderServlet][doSearchCustomerById]strGeneratorType: "+strGeneratorType);
	  System.out.println("[MassiveOrderServlet][doSearchCustomerById]intRegionId: "+iUserId+"");
	  System.out.println("[MassiveOrderServlet][doSearchCustomerById]strSpecificationId: "+strSpecificationId);	
    System.out.println("[MassiveOrderServlet][doSearchCustomerById]strDivisionId: "+strDivisionId);
    System.out.println("[MassiveOrderServlet][doSearchCustomerById]strNpSiteId: "+strNpSiteId);
    System.out.println("[MassiveOrderServlet][doSearchCustomerById]strCantSite: "+strCantSite);

    if ( MiUtil.parseInt(strCantSite) != 0 && MiUtil.parseInt(strNpSiteId) == 0){
    //if ( MiUtil.parseInt(strCantSite) != 0 ){
       out.println("alert('Debe Seleccionar un Responsable de Pago.');");
    }else{
        if ( strCodigoCliente.equals(strCodigoClienteAcept) ){
          out.println("alert('Cliente Cedente debe ser diferente a Cliente Aceptante.');");    
        }else{
        
          if ( ! strCodigoCliente.equals("0") ){
            HashCutomer = objCustomerService.CustomerDAOgetCustomerData( MiUtil.parseLong(strCodigoCliente), strLogin, MiUtil.parseLong(""+intRegionId),iUserId,iAppId,strGeneratorId,strGeneratorTypeRepCalCap);
            
            if( HashCutomer.get("strMessage")!=null )
               throw new Exception((String)HashCutomer.get("strMessage"));
            
            if( HashCutomer != null) {
                
              if (HashCutomer.size()!=0) {
                wn_swcustomerid     = MiUtil.parseInt((String)HashCutomer.get("wn_swcustomerid"));
                if (wn_swcustomerid==0)
                  throw new Exception("No se encontró Compañía");
                  
                wv_swname           = (String)HashCutomer.get("wv_swname"); 
                
                System.out.println("Nombre de Compania========================"+wv_swname);
                
                if (MiUtil.getString(wv_swname)=="")
                  throw new Exception("No se encontró Compañía");
                  
                request.setAttribute("wv_swname",wv_swname);
                
                // Lista de Sites
                objSiteHashMap =  objCustomerService.getCustomerSites(MiUtil.parseLong(strCodigoCliente),"Activo");
          
                if( objSiteHashMap.get("strMessage") != null )
                   throw new Exception((String)objSiteHashMap.get("strMessage"));
                
                ArrayList lista_sites = (ArrayList)objSiteHashMap.get("objArrayList");                
                      
                request.setAttribute("lista_sites",lista_sites);
                
                wv_customer = (String)strCodigoCliente;
                request.setAttribute("wv_customer",wv_customer);
                      
                request.getRequestDispatcher("pages/loadBillingAcountMassiveTransfer.jsp").forward(request, response); 
             
              }
              
            }            
            
          
            /*HashItemOrder     = objItemMassiveOrderService.MassiveOrderDAOgetItemOrder(MiUtil.parseLong((String)strCodigoCliente),MiUtil.parseLong(strnpSite),MiUtil.parseLong(strSpecificationId),0);
            objArrayItemOrder = (ArrayList)HashItemOrder.get("objArrayList"); 
            request.setAttribute("objArrayItemOrder",objArrayItemOrder);
            
            pBean.setNptipo2("0");
            pBean.setNpsolutionid(MiUtil.parseInt(strSolutionId));
            pBean.setNpspecificationid(MiUtil.parseInt(strSpecificationId));
                      
            HashItemPlan = objNewOrderService.PlanDAOgetPlanList(pBean,strCompanyType);
            objArrayPlanList  = (ArrayList)HashItemPlan.get("objPlanList"); 
            request.setAttribute("objArrayPlanList",objArrayPlanList);
            
            //Obtenemos los ssaa
            HashServiceList  = objMassiveOrderService.getServiceList(MiUtil.parseInt(strDivisionId),MiUtil.parseInt(strPlan));
            objArrayServiceList  = (ArrayList)HashServiceList.get("objServiceList"); 
            request.setAttribute("objArrayServiceList",objArrayServiceList);
            
            //Obtenemos los SSAA por solución
            HashServiceBySolutionList  = objMassiveOrderService.getServiceListBySolution(MiUtil.parseInt(strDivisionId));
            objArrayServiceBySolutionList  = (ArrayList)HashServiceBySolutionList.get("objServiceBySolutionList");
            request.setAttribute("objArrayServiceBySolutionList",objArrayServiceBySolutionList);
            
            request.getRequestDispatcher("pages/loadItemsMassiveTransfer.jsp").forward(request, response);*/
          }
        }
    }
  }
  
  
  /**
  Method : doGetServiceContractPlan
  Developer       Fecha       Comentario
  =============   ==========  ======================================================================
  Magally Mora    21/08/2009  Creación
  */
  public void doGetServiceContractPlan(HttpServletRequest request, PrintWriter out) throws Exception{
    MassiveOrderService  objItemMassiveOrderService  = new MassiveOrderService();
    ContractServiceBscsBean objContractServiceBscsBean;
    HashMap hResult = new HashMap();
    HashMap hItemServices = new HashMap();
    HashMap hResultMessage = new HashMap();
    ArrayList list = new ArrayList();    
    ArrayList listServ = new ArrayList();    
    
    String[] codContrato = request.getParameterValues("hdnItemCoId");
    String[] chkPhone    = request.getParameterValues("hdnchkPhone");
    String[] ItemSolutionId  = request.getParameterValues("hdnItemSolutionId");
    String[] hdnGetServicio = request.getParameterValues("hdnGetServicio");  
    String[] hdnItemPhoneNumber = request.getParameterValues("txtItemPhoneNumber");
    String  strSpecificationId    = (String)request.getParameter("hdnSpecificationId");
    String  strOrderId   = (String)request.getParameter("hdnNpOrderId");
    
    for(int i=0;i<chkPhone.length;i++){
    
    System.out.println(i+" - Contrato -"+codContrato[i]+" <-->"+" check "+chkPhone[i]+"   solucion  - "+ItemSolutionId[i]);
    
        if (chkPhone[i].equals("on") && codContrato[i]!= null && hdnGetServicio[i].equals("false")){
          objContractServiceBscsBean = new ContractServiceBscsBean();
          hResult = objItemMassiveOrderService.getCommercialService(Long.parseLong(codContrato[i]));  
          hResultMessage = objItemMassiveOrderService.getValidateByPhone(Long.parseLong(hdnItemPhoneNumber[i]),Long.parseLong(strSpecificationId),Long.parseLong(strOrderId));
          
          objContractServiceBscsBean.setHiddenId(i);
          objContractServiceBscsBean.setServiceCode((String)hResult.get("objCommercialService"));
          objContractServiceBscsBean.setStrMessage((String)hResultMessage.get("strMessage")==null?"":(String)hResultMessage.get("strMessage"));
          objContractServiceBscsBean.setStrTypeError((String)hResultMessage.get("strTypeError"));
          list.add(objContractServiceBscsBean);   
          
          hItemServices = objItemMassiveOrderService.getServiceItemListBySolution(MiUtil.parseInt(ItemSolutionId[i]));
          listServ.add(hItemServices);
        }
    }       
    request.setAttribute("setAttService", list);
    request.setAttribute("setAttServiceBySol", listServ);
    
  }
  
   /**
   Method : doSearchCustomerBySiteId
   Developer       Fecha       Comentario
   =============   ==========  ======================================================================
   Evelyn Ocampo   10/08/2009  Creación
   */
   public void doSearchCustomerBySiteId(HttpServletRequest request, PrintWriter out,HttpServletResponse response) throws Exception{
   
    Enumeration paramNames = request.getParameterNames();
    while(paramNames.hasMoreElements()) {
      String paramName = (String)paramNames.nextElement();            
      String[] paramValues = request.getParameterValues(paramName);
      String paramValue = paramValues[0];
      System.out.println("paramName ["+paramName+"] -- Valor ["+paramValue+"]");
    }
   
   
   
    String    strCodigoCliente="0";    
    String    strLogin,strGeneratorType,strGeneratorTypeRepCalCap;
    int       intRegionId;
    String    strSolutionId = null;
    String    strCompanyType;
    String    strPlan = "";
    String    strSpecificationId;
    String    strDivisionId;
    String    strnpSite=null;
    int       wn_swcustomerid = 0;
    String    wv_swname = "";
    String    strMessage = null;
    String    strCustomerDescription;   
    //String    strNpSiteId = null;
    //String   strNpMassiveSiteId = null;
    String    wv_customer = "";
        
    HashMap   HashItemOrder = new HashMap();
    HashMap   HashItemPlan = new HashMap();
    HashMap   HashServiceList = new HashMap();
    HashMap   HashServiceBySolutionList = new HashMap();
    HashMap   HashCutomer = new HashMap();
    
    ArrayList objArrayItemOrder  = new ArrayList();
    ArrayList objArrayPlanList   = new ArrayList();
    ArrayList objArrayServiceList = new ArrayList(); 
    ArrayList objArrayServiceBySolutionList = new ArrayList(); 
    
    
    MassiveOrderService  objItemMassiveOrderService  = new MassiveOrderService();
    NewOrderService      objNewOrderService = new NewOrderService();
    PlanTarifarioBean    pBean = new PlanTarifarioBean();
    MassiveOrderService  objMassiveOrderService  = new MassiveOrderService();
  
    intRegionId               = MiUtil.parseInt(request.getParameter("hdnRegionId"));
    strLogin                  = MiUtil.getString(request.getParameter("hdnSessionLogin"));
    int iUserId               = MiUtil.parseInt((String)request.getParameter("hdnIUserId"));
    int iAppId                = MiUtil.parseInt((String)request.getParameter("hdnIAppId"));
    strGeneratorType          = MiUtil.getString(request.getParameter("strGeneratorType"));
    strGeneratorTypeRepCalCap = (String)request.getParameter("hdnOrigenType");
    String strGeneratorId     = (request.getParameter("strGeneratorId")==null?"":request.getParameter("strGeneratorId")); 
    strSpecificationId        = (String)request.getParameter("hdnSpecificationId");
    strDivisionId             = (String)request.getParameter("hdnDivisionId");    
    strCompanyType            = Constante.CUSTOMERTYPE_CUSTOMER;

    strCodigoCliente          = request.getParameter("hdnCustId1");
    String strCodigoClienteAcept     = request.getParameter("txtCompanyId");
    //strNpSiteId               = (String)request.getParameter("hdnNpSiteId"); 
    strnpSite                 = (String)request.getParameter("hdnResponsablePagoMasivos"); 
    strCustomerDescription    = (String)request.getParameter("hdnCompanyaName");
    
    
    
	  System.out.println("[MassiveOrderServlet][doSearchCustomerById]strCodigoClienteAcept----------------------: "+strCodigoClienteAcept+"");
    System.out.println("[MassiveOrderServlet][doSearchCustomerById]strCodigoCliente---------------------------: "+strCodigoCliente+"");
	  System.out.println("[MassiveOrderServlet][doSearchCustomerById]strLogin: "+strLogin);
	  System.out.println("[MassiveOrderServlet][doSearchCustomerById]strGeneratorType: "+strGeneratorType);
	  System.out.println("[MassiveOrderServlet][doSearchCustomerById]intRegionId: "+iUserId+"");
	  System.out.println("[MassiveOrderServlet][doSearchCustomerById]strSpecificationId: "+strSpecificationId);	
    System.out.println("[MassiveOrderServlet][doSearchCustomerById]strDivisionId: "+strDivisionId);
    System.out.println("[MassiveOrderServlet][doSearchCustomerById]strNpSiteId: "+strnpSite);
    
    Hashtable hshCustPattern = objCustomerService.CustomerDAOgetCustPatternQty(strCustomerDescription);
    
    strMessage = (String)hshCustPattern.get("strMessage");
    
    strMessage = strMessage.equals("null")?null:strMessage;
    
    if( strMessage!=null ){
        throw new Exception(strMessage);
    }
    
    /*if ( MiUtil.parseInt(strNpSiteId) == 0 ){
       out.println("alert('Debe Seleccionar un Responsable de Pago.');");
    }else{*/
        /*if ( strCodigoCliente.equals(strCodigoClienteAcept) ){
          out.println("alert('Cliente Cedente debe ser diferente a Cliente Aceptante.');");    
        }else{*/
        
          /*if ( ! strCodigoCliente.equals("0") ){
            HashCutomer = objCustomerService.CustomerDAOgetCustomerData( MiUtil.parseLong(strCodigoCliente), strLogin, MiUtil.parseLong(""+intRegionId),iUserId,iAppId,strGeneratorId,strGeneratorTypeRepCalCap);
            
            if( HashCutomer != null) {
                
              if (HashCutomer.size()!=0) {
                wn_swcustomerid     = MiUtil.parseInt((String)HashCutomer.get("wn_swcustomerid"));
                if (wn_swcustomerid==0)
                  throw new Exception("No se encontró Compañía");
                  
                wv_swname           = (String)HashCutomer.get("wv_swname"); 
                
                System.out.println("Nombre de Compania========================"+wv_swname);
                
                if (MiUtil.getString(wv_swname)=="")
                  throw new Exception("No se encontró Compañía");
                         
                request.setAttribute("wv_swname",wv_swname);
             
              }
              
            }
              
            if( HashCutomer.get("strMessage")!=null )
            throw new Exception((String)HashCutomer.get("strMessage"));*/
            
            //if (! strnpSite.equals("0")){
           
            HashItemOrder     = objItemMassiveOrderService.MassiveOrderDAOgetItemOrder(MiUtil.parseLong((String)hshCustPattern.get("wn_customerid")),MiUtil.parseLong(strnpSite),MiUtil.parseLong(strSpecificationId),MiUtil.parseLong(strCodigoClienteAcept));
            objArrayItemOrder = (ArrayList)HashItemOrder.get("objArrayList"); 
            request.setAttribute("objArrayItemOrder",objArrayItemOrder);
            
            pBean.setNptipo2("0");
            pBean.setNpsolutionid(MiUtil.parseInt(strSolutionId));
            pBean.setNpspecificationid(MiUtil.parseInt(strSpecificationId));
                      
            HashItemPlan = objNewOrderService.PlanDAOgetPlanList(pBean,strCompanyType);
            objArrayPlanList  = (ArrayList)HashItemPlan.get("objPlanList"); 
            request.setAttribute("objArrayPlanList",objArrayPlanList);
            
            //Obtenemos los ssaa
            HashServiceList  = objMassiveOrderService.getServiceList(MiUtil.parseInt(strDivisionId),MiUtil.parseInt(strPlan));
            objArrayServiceList  = (ArrayList)HashServiceList.get("objServiceList"); 
            request.setAttribute("objArrayServiceList",objArrayServiceList);
            
            //Obtenemos los SSAA por solución
            HashServiceBySolutionList  = objMassiveOrderService.getServiceListBySolution(MiUtil.parseInt(strDivisionId));
            objArrayServiceBySolutionList  = (ArrayList)HashServiceBySolutionList.get("objServiceBySolutionList");
            request.setAttribute("objArrayServiceBySolutionList",objArrayServiceBySolutionList);
            
            wv_customer = (String)hshCustPattern.get("wn_customerid");            
            request.setAttribute("wv_customer",wv_customer);
            
            request.getRequestDispatcher("pages/loadItemsMassiveTransfer.jsp").forward(request, response);
          //}
        //}
    //}
  }
  
  /**
  Method : doSearchCustomerByName
  Developer       Fecha       Comentario
  =============   ==========  ======================================================================
  Evelyn Ocampo   24/08/2009  Creación
  */
  public void doSearchCustomerByName(HttpServletRequest request, PrintWriter out, HttpServletResponse response) throws Exception{
    
    HashMap   hshCutomer = new HashMap();
    String    strLogin;
    int       intRegionId;
    String    strMessage = null;
    String    wv_customer = "";
    
    HashMap   HashItemOrder = new HashMap();
    HashMap   HashItemPlan = new HashMap();
    HashMap   HashServiceList = new HashMap();
    HashMap   HashServiceBySolutionList = new HashMap();
    HashMap   objSiteHashMap = new HashMap();
    
    ArrayList objArrayItemOrder  = new ArrayList();
    ArrayList objArrayPlanList   = new ArrayList();
    ArrayList objArrayServiceList = new ArrayList(); 
    ArrayList objArrayServiceBySolutionList = new ArrayList(); 
    
    
    MassiveOrderService  objItemMassiveOrderService  = new MassiveOrderService();
    NewOrderService      objNewOrderService = new NewOrderService();
    PlanTarifarioBean    pBean = new PlanTarifarioBean();
    MassiveOrderService  objMassiveOrderService  = new MassiveOrderService();
    
    
    String    strCustomerDescription;   
    String    strSpecificationId;
    String    strDivisionId;
    String    strnpSite=null;
    String    strNpSiteId=null;
    String    strSolutionId = null;
    String    strCompanyType;
    String    strPlan = "";
    strCustomerDescription    = (String)request.getParameter("hdnCompanyaName");
    strSpecificationId        = (String)request.getParameter("hdnSpecificationId");
    strDivisionId             = (String)request.getParameter("hdnDivisionId");    
    strCompanyType            = Constante.CUSTOMERTYPE_CUSTOMER; //(String)request.getParameter("hdnCompanyType");
    String strCodigoClienteAcept     = request.getParameter("txtCompanyId");
    strNpSiteId               = (String)request.getParameter("hdnNpSiteId"); 
    
    
    
    System.out.println("[MassiveOrderServlet][strCodigoClienteAcept]---------->"+strCodigoClienteAcept);
    System.out.println("[MassiveOrderServlet][strCustomerDescription]--------->"+strCustomerDescription);
    System.out.println("strCompanyType----------------------->"+strCompanyType);
    Hashtable hshCustPattern = objCustomerService.CustomerDAOgetCustPatternQty(strCustomerDescription);
    
    strMessage = (String)hshCustPattern.get("strMessage");
    
    strMessage = strMessage.equals("null")?null:strMessage;
    
    if( strMessage!=null ){
        throw new Exception(strMessage);
    }
    
    
    System.out.println("strCustomerDescription========"+strCustomerDescription+"-"+(String)hshCustPattern.get("wn_customerid"));
    
    //Si solo hubo un encuentro 
    if ( MiUtil.parseInt((String)hshCustPattern.get("wn_customer_qty")) == 1  ) {   
    System.out.println("En Customerid = 1");
      if ( MiUtil.parseInt((String)hshCustPattern.get("wn_customerid")) != 0 ){
      System.out.println("En Customerid != 0");
        /*if (MiUtil.parseInt(strNpSiteId) == 0){
           out.println("alert('Debe Seleccionar un Responsable de Pago.');");
        }else{
            if (hshCustPattern.get("wn_customerid").equals(strCodigoClienteAcept)){
              out.println("alert('Cliente Cedente debe ser diferente a Cliente Aceptante.');");    
            }else{         
               
                // Lista de Sites
                // ----------------------------
                objSiteHashMap =  objCustomerService.getCustomerSites(MiUtil.parseLong((String)hshCustPattern.get("wn_customerid")),"Activo");
      
                if( objSiteHashMap.get("strMessage") != null )
                   throw new Exception((String)objSiteHashMap.get("strMessage"));
            
                ArrayList lista_sites = (ArrayList)objSiteHashMap.get("objArrayList");
                
                out.println("var form = parent.mainFrame.document.frmdatos;");
                
                out.println("if (form.cmbResponsablePagoMasivos != null){");
                
                out.println("form.cmbResponsablePagoMasivos.length=0;");
                out.println("AddNewOption(form.cmbResponsablePagoMasivos, '', '           '); }");
          
                HashMap hsMapLista = new HashMap();                   
                for( int i=0; i<lista_sites.size();i++ ){
                    hsMapLista = (HashMap)lista_sites.get(i);
                    out.println("if (form.cmbResponsablePagoMasivos != null)");
                    out.println("AddNewOption(form.cmbResponsablePagoMasivos, '" + hsMapLista.get("wn_swsiteid")  + "', \"" + hsMapLista.get("wv_swsitename").toString()  + "  " + hsMapLista.get("wv_npcodbscs") + "\" );");
                }
          
                
                /*request.setAttribute("lista_sites",lista_sites);
                
                request.getRequestDispatcher("pages/loadItemsMassiveTransfer.jsp").forward(request, response);*/
          
                HashItemOrder     = objItemMassiveOrderService.MassiveOrderDAOgetItemOrder(MiUtil.parseLong((String)hshCustPattern.get("wn_customerid")),MiUtil.parseLong(strnpSite),MiUtil.parseLong(strSpecificationId),MiUtil.parseLong(strCodigoClienteAcept));
                objArrayItemOrder = (ArrayList)HashItemOrder.get("objArrayList"); 
                request.setAttribute("objArrayItemOrder",objArrayItemOrder);
                
                      
                pBean.setNptipo2("0");
                pBean.setNpsolutionid(MiUtil.parseInt(strSolutionId));
                pBean.setNpspecificationid(MiUtil.parseInt(strSpecificationId));
                          
                HashItemPlan = objNewOrderService.PlanDAOgetPlanList(pBean,strCompanyType);
                objArrayPlanList  = (ArrayList)HashItemPlan.get("objPlanList"); 
                request.setAttribute("objArrayPlanList",objArrayPlanList);
                
                //Obtenemos los ssaa
                HashServiceList  = objMassiveOrderService.getServiceList(MiUtil.parseInt(strDivisionId),MiUtil.parseInt(strPlan));
                objArrayServiceList  = (ArrayList)HashServiceList.get("objServiceList"); 
                request.setAttribute("objArrayServiceList",objArrayServiceList);
                
                //Obtenemos los SSAA por solución
                HashServiceBySolutionList  = objMassiveOrderService.getServiceListBySolution(MiUtil.parseInt(strDivisionId));
                objArrayServiceBySolutionList  = (ArrayList)HashServiceBySolutionList.get("objServiceBySolutionList");
                request.setAttribute("objArrayServiceBySolutionList",objArrayServiceBySolutionList);  
                
               
                System.out.println("XXXXXXXXXXXXXXX==="+MiUtil.parseLong((String)hshCustPattern.get("wn_customerid")));   
                wv_customer = (String)hshCustPattern.get("wn_customerid");
                request.setAttribute("wv_customer",wv_customer);
                request.getRequestDispatcher("pages/loadItemsMassiveTransfer.jsp").forward(request, response);
           // }
       // }
      /*}else{
         System.out.println("En customerid = doSearchCustomerByDescription");
         out.println("alert('Especifique mejor su búsqueda.');");
         out.println("parent.mainFrame.searchCustomerMassive()");*/
        
      }
    }
    //Si hay mas de dos encuentros
    /*else {
      System.out.println("en no entraa doSearchCustomerByDescription===================");
      out.println("alert('Especifique mejor su búsqueda.');");
      out.println("parent.mainFrame.searchCustomerMassive()");
    }*/
    
  }
  
    /**
  Method : clearMassiveCustomer
  Developer       Fecha       Comentario
  =============   ==========  ======================================================================
  Evelyn Ocampo   24/08/2009  Creación
  */
  public void clearMassiveCustomer(HttpServletRequest request, HttpServletResponse response) throws Exception{
     
     request.getRequestDispatcher("pages/loadItemsClearMassiveTransfer.jsp").forward(request, response);
  
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