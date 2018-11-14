<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@page import="oracle.portal.provider.v2.ProviderSession" %>
<%@page import="oracle.portal.provider.v2.ProviderUser" %>
<%@page import="oracle.portal.provider.v2.render.PortletRendererUtil" %>
<%@page import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil" %>
<%@page import="pe.com.nextel.bean.PortalSessionBean" %>
<%@page import="pe.com.nextel.exception.SessionException" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@page import="pe.com.nextel.util.MiUtil"%>
<%@page import="java.util.*"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="pe.com.nextel.service.NewOrderService"%>
<%@page import="pe.com.nextel.service.SessionService"%>
<%@page import="pe.com.nextel.service.GeneralService"%>
<%@page import="pe.com.nextel.bean.*"%>
<%@page import="pe.com.nextel.service.*" %>

<%
try{

  String strSessionIdOrder = "";

  try{
    PortletRenderRequest pReq = (PortletRenderRequest) request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
    ProviderUser objetoUsuario1 = pReq.getUser();
    strSessionIdOrder=objetoUsuario1.getPortalSessionId();
    System.out.println("Sesi?n capturada  JP_ORDER_NEW_ORDER_ShowPage : " + objetoUsuario1.getName() + " - " + strSessionIdOrder );
  }catch(Exception e){
    System.out.println("Portler Not Found : " + e.getClass() + " - " + e.getMessage() );
    out.println("Portlet JP_ORDER_NEW_ORDER_ShowPage Not Found");
    return;
  }

  //strSessionIdOrder = "749126701476259056994469063087083508994001614";
  //strSessionIdOrder = "998102396";
  
  System.out.println("Sesi?n capturada despu?s del resuest : " + strSessionIdOrder );
	PortalSessionBean portalSessionBean3 = (PortalSessionBean)SessionService.getUserSession(strSessionIdOrder);
	if(portalSessionBean3==null) {
    System.out.println("No se encontr? la sesi?n de Java ->" + strSessionIdOrder);
		throw new SessionException("La sesi?n finaliz?");
	}
  
   //<!--jsalazar - modif hpptt # 1 - 29/09/2010 -Inicio-->
   Date today=MiUtil.getDateBD("dd/MM/yyyy");
   String strToday=MiUtil.getDate(today,"dd/MM/yyyy");
    //<!--jsalazar - modif hpptt # 1 - 29/09/2010 -Fin-->
   int iUserId=0;
   int iAppId=0;   
   int iSalesStructId=0;

   String strValidType = ""; // ocruces N_O000046759 Control de Tipo de Operaci?n vs Tipo de Orden
   String strMensajeTypeOpera = ""; // ocruces N_O000046759 Control de Tipo de Operaci?n vs Tipo de Orden

   iUserId  = portalSessionBean3.getUserid();
   iAppId   = portalSessionBean3.getAppId();  
   iSalesStructId = portalSessionBean3.getSalesStructId();
   

   String strOrderId = null;
   String strMessage = null; 
   String strParentUrl = "";
   String strParentName = "";
   Hashtable hOrder = NewOrderService.OrderDAOgetOrderIdNew();
   
   /* Inicio Data */
   String strDataSalesmanLabel       = "<td class='CellLabel' colspan=1 align='center' name='vendedorDataLabel' id='vendedorDataLabel'>Vendedor Data</td>";
   String strDataSalesmanHiddenLabel = "<td class='CellLabel' colspan=1 align='center' name='vendedorDataLabel' id='vendedorDataLabel'>&nbsp;</td>";
   String strDataSalesmanField       = "<td class='CellContent' colspan=1 id='vendedorData' name='vendedorData'>&nbsp;<select name='cmbVendedorData' onchange='fxChangeSalesManData(this);'><option value=''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option></select></td>";
   String strDataSalesmanHiddenField = "<td class='CellContent' colspan=1 id='vendedorData' name='vendedorData'>&nbsp;</td> ";   
   /* Fin Data */
    
   if ( hOrder!= null)
        strOrderId = (String)hOrder.get("wn_orderid");

   //CGC COR0407
   String strSalesId = request.getParameter("salesTeamId")==null?"":request.getParameter("salesTeamId");        
   String strRegionId = request.getParameter("regionId")==null?"":request.getParameter("regionId");
   System.out.println("[strRegionId]"+strRegionId);
   
   String strGeneratorid       = request.getParameter("generatorid")==null?"":request.getParameter("generatorid");
   String strGeneratortype     = request.getParameter("generatortype")==null?"":request.getParameter("generatortype");
   
   //Trayendo el numero de dias (59 dias) para calcular la fecha final de suspensiones
   GeneralService objService = new GeneralService();
   HashMap hshMaxNumDias = objService.getDominioList("MAX_DIAS_SUSPENSION");
   ArrayList arrModalitySellList = (ArrayList) hshMaxNumDias.get("arrDominioList");
   DominioBean diasMaximos = (DominioBean)arrModalitySellList.get(0);
   GeneralService objGeneralService = new GeneralService();
  
   if ( !MiUtil.getString(strGeneratorid).equals("") && !MiUtil.getString(strGeneratortype).equals("") ){    
    HashMap hshResult = new HashMap();
    hshResult = objGeneralService.getObjectTypeUrl(strGeneratortype);
    strMessage = (String)hshResult.get("strMessage");
    if ( strMessage != null  )
      throw new Exception(strMessage);
    strParentUrl = (String)hshResult.get("strUrl");
    strParentName = (String)hshResult.get("strName");    
   }
   
   //Validaciones del valor de la estructura de ventas del usuario generador de la Orden
   //-----------------------------------------------------------------------------------
   int iSalesStructOrigenId=0;
   HashMap hshResultStruct = new HashMap();
   hshResultStruct = objGeneralService.getSalesStructOrigen(iSalesStructId,Constante.SALES_STRUCT_PORTAL_DEFAULT) ;
   strMessage = (String)hshResultStruct.get("strMessage");
   if ( strMessage != null )
       throw new Exception(strMessage);
    iSalesStructOrigenId = MiUtil.parseInt((String)hshResultStruct.get("iSalesStructOrigen"));
  
   System.out.println("<<<<<<Datos de SalesStructId>>>>>>");
   System.out.println("iSalesStructId:"+iSalesStructId);
   System.out.println("iSalesStructOrigenId:"+iSalesStructOrigenId);
   
   
   //Inicio [Despacho en Tienda] RMARTINEZ
   NewOrderService objOrderService=new NewOrderService();   
   HashMap hSpecificationsList = objOrderService.showCourierSpecificationsId();
   ArrayList arrDominioList = (ArrayList) hSpecificationsList.get("arrDominioList");
   int iShowCourier = 0;
   //Fin [Despacho en Tienda] RMARTINEZ   
   
  //ocruces N_O000046759 Control de Tipo de Operaci?n vs Tipo de Orden
   System.out.println("<<<<<<N_O000046759>>>>>>");
   HashMap hshValidateTypOpe=new HashMap();
   hshValidateTypOpe = objGeneralService.getValidateTypOpe("VALIDATE_CREDIT_EVALUATION");
   strValidType = (String)hshValidateTypOpe.get("strValidTypOpe");
   System.out.println("strValidType:"+strValidType);
   String strCustomerId = request.getParameter("customerId")==null?"":request.getParameter("customerId");
   System.out.println("strCustomerId : "+strCustomerId);

   NewOrderService objOrderTypeOpeService=new NewOrderService();
   HashMap hTypeOpeSpecificationsList = objOrderTypeOpeService.showTypeOpeSpecificationsId(strCustomerId);
   ArrayList arrTypeOpeSpecifications = (ArrayList) hTypeOpeSpecificationsList.get("arrTypeOpeSpecifications");
   //Fin ocruces N_O000046759 Control de Tipo de Operaci?n vs Tipo de Orden

%>
<!--START MSOTO: 08-04-2014 SAR N_O000019563 Se deshabilita el combo--> 
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/jQuery-min.js"></script>

  <!-- DATOS DEL PEDIDO - INICIO -->
  
<script>   
   function redirigeLocal(){
       //alert("hola mundo 2");
       //var url = "/portal/pls/portal/INCIDENT_WEB.NP_ORDER_FROM_INCIDENT_PL_PKG.PL_INCIDENT_VALIDATE?an_npincidentid=38745904";
       //var url = "https://lmaoast16.nextelperu.net/portal/page/portal/orders/ORDER_NEW_JBALDEON?customerId=1229&generatortype=INC&generatorid=38745904&siteId=&salesTeamId=&chnlPartnerId=&regionId=&opportunityTypeId=&unknwnSiteId=&an_nptype=&an_npspecificationid=&av_flagGenerator=N&divisionid=1&customerscoreid=";
       //parent.bottomFrame.location.replace(url);
              
       location.replace("/websales/Bottom.html");
       //var url = "/portal/page/portal/orders/ORDER_NEW?customerId=1229&generatortype=INC&generatorid=38745904&siteId=&salesTeamId=&chnlPartnerId=&regionId=&opportunityTypeId=&unknwnSiteId=&an_nptype=&an_npspecificationid=&av_flagGenerator=N&divisionid=1&customerscoreid=";
       //var url = "https://lmaoast16.nextelperu.net/portal/page/portal/orders/ORDER_NEW?customerId=1229&generatortype=INC&generatorid=38745904&siteId=&salesTeamId=&chnlPartnerId=&regionId=&opportunityTypeId=&unknwnSiteId=&an_nptype=&an_npspecificationid=&av_flagGenerator=N&divisionid=1&customerscoreid=";
       var url = "https://lmaoast16.nextelperu.net/portal/page/portal/orders/ORDER_NEW_JBALDEON?customerId=1229&generatortype=INC&generatorid=38745904&siteId=&salesTeamId=&chnlPartnerId=&regionId=&opportunityTypeId=&unknwnSiteId=&an_nptype=&an_npspecificationid=&av_flagGenerator=N&divisionid=1&customerscoreid=";
       parent.mainFrame.location.href = url;
   }
   
   function redirigeServidor(){
       //alert("hola mundo 2");
       //var url = "/portal/pls/portal/INCIDENT_WEB.NP_ORDER_FROM_INCIDENT_PL_PKG.PL_INCIDENT_VALIDATE?an_npincidentid=38745904";
       //var url = "https://lmaoast16.nextelperu.net/portal/page/portal/orders/ORDER_NEW_JBALDEON?customerId=1229&generatortype=INC&generatorid=38745904&siteId=&salesTeamId=&chnlPartnerId=&regionId=&opportunityTypeId=&unknwnSiteId=&an_nptype=&an_npspecificationid=&av_flagGenerator=N&divisionid=1&customerscoreid=";
       //parent.bottomFrame.location.replace(url);
              
       location.replace("/websales/Bottom.html");
       //var url = "/portal/page/portal/orders/ORDER_NEW?customerId=1229&generatortype=INC&generatorid=38745904&siteId=&salesTeamId=&chnlPartnerId=&regionId=&opportunityTypeId=&unknwnSiteId=&an_nptype=&an_npspecificationid=&av_flagGenerator=N&divisionid=1&customerscoreid=";
       var url = "https://lmaoast16.nextelperu.net/portal/page/portal/orders/ORDER_NEW?customerId=1229&generatortype=INC&generatorid=38745904&siteId=&salesTeamId=&chnlPartnerId=&regionId=&opportunityTypeId=&unknwnSiteId=&an_nptype=&an_npspecificationid=&av_flagGenerator=N&divisionid=1&customerscoreid=";
       //var url = "https://lmaoast16.nextelperu.net/portal/page/portal/orders/ORDER_NEW_JBALDEON?customerId=1229&generatortype=INC&generatorid=38745904&siteId=&salesTeamId=&chnlPartnerId=&regionId=&opportunityTypeId=&unknwnSiteId=&an_nptype=&an_npspecificationid=&av_flagGenerator=N&divisionid=1&customerscoreid=";
       parent.mainFrame.location.href = url;
   }
       
  </script>
  <!-- Fin Data -->

  <br>
   <h1>INICIO</h1>
   <input type="button" onclick="redirigeLocal();" value="redirigeLocal" />
   <input type="button" onclick="redirigeServidor();" value="redirigeServidor" />
   <!-- Inicio Data -->
   <script defer>
      //fxHideDataFields();
   </script>
   <!-- Fin Data -->
   
  <!--  Hidden que indica el # de grupos de item_pedido ingresados -->
  <!-- DATOS DEL PEDIDO - FIN -->
<%}catch(SessionException se) {
    System.out.println("[JP_ORDER_NEW_ORDER][SessionException] : " + se.getClass() + " - " + se.getMessage());
    se.printStackTrace();
    //out.println("<script>alert('Su sesi?n ha finalizado. Ingrese nuevamente');</script>");
    //String strUrl = "/portal/pls/portal/WEBSALES.NP_USER_AVAILABILITY_PL_PKG.PL_LOGOUT";
     //out.print("<script>parent.mainFrame.location.replace('"+strUrl+"');</script>");
  }catch(Exception e) {
    String strMessageExceptionGeneralStart = "";
    strMessageExceptionGeneralStart = MiUtil.getMessageClean(e.getMessage());
    System.out.println("[JP_ORDER_NEW_ORDER][Exception] : " + e.getClass() + " - " + e.getMessage());
    out.println("<script>alert('"+strMessageExceptionGeneralStart+"');</script>");   
  }
%>
