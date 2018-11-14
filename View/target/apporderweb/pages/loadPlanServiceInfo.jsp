<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Hashtable" %>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="pe.com.nextel.util.*" %>
<%@page import="pe.com.nextel.bean.ServiciosBean" %>
<%@page import="pe.com.nextel.bean.PlanTarifarioBean" %>
<%@page import="pe.com.nextel.bean.ProductLineBean" %>
<%@page import="java.text.DecimalFormat" %>
<%@ page import="pe.com.nextel.service.NewOrderService" %>
<%@ page import="pe.com.nextel.bean.ProductBean" %>

<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Solucion</title>
  <link type="text/css" rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"/>  
   <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
   <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
   <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsItemsIXEdit.js"></script>
</head>
<body style="padding: 0px; margin: 0px" bgcolor="#AABBCC">
<%
  try{
  
  //Valores Principales
  //--------------------
  String strSessionId       = (String)request.getAttribute("strSessionId");
  String strSpecificationId = (String)request.getAttribute("strSpecificationId");
  String strSolutionId      = (String)request.getAttribute("strSolutionId");
  String strMessage         =  (String)request.getAttribute("strMessage");
  
  if( strMessage != null )
    throw new Exception(MiUtil.getMessageClean(strMessage));  
  //Listado de Responsables de Pago
  HashMap hRespPagoList      = (HashMap)request.getAttribute("hRespPagoList"); 
  ArrayList objRespPagoList = new ArrayList();
  if (hRespPagoList!=null){
   objRespPagoList =(ArrayList)hRespPagoList.get("objRespPagoList");  
  }
    
  //Listado de Planes
  //------------------
  HashMap hPlanList         = (HashMap)request.getAttribute("hPlanList");
  ArrayList objArrayListPlanes = new ArrayList();
  if (hPlanList!=null){
    objArrayListPlanes = (ArrayList)hPlanList.get("objPlanList");
  }
  
  //Listado de Servicios
  //--------------------
  HashMap hServiceList      = (HashMap)request.getAttribute("hServiceList"); 
  ArrayList objArrayList = new ArrayList();
  if (hServiceList!=null){
   objArrayList =(ArrayList)hServiceList.get("objServiceList");  
  }
  
  //Arreglo de Servicios por Defecto
  //---------------------------------
  ArrayList objArrayListDefault   = (ArrayList)request.getAttribute("objArrayListDefault");
  
  //Listado de Línea
  //------------------
  HashMap hLineaList         = (HashMap)request.getAttribute("hLineaList");
  ArrayList objArrayListLinea = new ArrayList();
  if (hLineaList!=null){
    objArrayListLinea = (ArrayList)hLineaList.get("objArrayList");
  }

  //Obtener producto bolsa a partir del customerID, siteId y solutionID
  String strProductName = "";
  String strProductId   = "";
  String strMinuteRate  = "";
  String strPriceByMinute  = "";
  String message="";
  String strLevel = "";
  String strPlantarifario = "";
  String strDescripcionPlanTarifario = "";
  String strCustomerId = "";
  String strnpSite = "";
  
  if(strSpecificationId.equals("2022") || strSpecificationId.equals("2023")) { // strSpecificationId= Bolsa cambio    /  Desactivacion                  
   
    strCustomerId  = (String)request.getParameter("strCustomerId");
    strnpSite      = (String)request.getParameter("strnpSite");     
                 
    NewOrderService objNewOrderService = new NewOrderService();      
    HashMap objHashMap = objNewOrderService.getProductBolsa(MiUtil.parseLong(strCustomerId),MiUtil.parseLong(strnpSite),MiUtil.parseLong(strSolutionId));
   
    if( (String)objHashMap.get("strMessage")!= null )         
      message = MiUtil.getMessageClean((String)objHashMap.get("strMessage"));
    else {  
      ProductBean objProductBean = (ProductBean)objHashMap.get("objProductBean");
      strProductName = MiUtil.getString(objProductBean.getNpproductname());
      strProductId   = MiUtil.getString(objProductBean.getNpproductid());
      strMinuteRate  = MiUtil.getString(objProductBean.getNpminute());
      strPriceByMinute  = ""+objProductBean.getNpminuteprice();
      strSolutionId  = MiUtil.getString(objProductBean.getNpsolutionid());
      strLevel = MiUtil.getString(objProductBean.getNpLevel());
      
      PlanTarifarioBean planTarifarioBean = new PlanTarifarioBean();
      planTarifarioBean.setNpplantarifarioid(objProductBean.getNpplanid());   
      HashMap objOriginalPlan = objNewOrderService.getOriginalPlan(planTarifarioBean);
      planTarifarioBean = (PlanTarifarioBean)objOriginalPlan.get("objPlanTarifarioBean");
      if( (String)objHashMap.get("strMessage")!= null ) {       
        message = MiUtil.getMessageClean((String)objHashMap.get("strMessage"));
      }else{
        strPlantarifario = MiUtil.getString(planTarifarioBean.getNpplantarifarioid());
        strDescripcionPlanTarifario = MiUtil.getString(planTarifarioBean.getNpdescripcion());
      }
    }
  }  

%>

<script language="javascript">
    
    function fxLoadRespPago(){      
      formCurrent = parent.mainFrame.frmdatos;
      parent.mainFrame.DeleteSelectOptions(formCurrent.cmb_ItemRespPago);      
      <%  
        if ( objRespPagoList != null && objRespPagoList.size() >0){            
            HashMap hsMapLista = new HashMap(); 
            String strSiteId  =  "";
            String strSiteName  = ""; 
            for (int i=0; i<objRespPagoList.size(); i++){
               hsMapLista = (HashMap)objRespPagoList.get(i);
               strSiteName = (String)hsMapLista.get("wv_swsitename");
               strSiteId = (String)hsMapLista.get("wn_swsiteid");        
               System.out.println("===agrega "+strSiteId+" - "+strSiteName);
              %>              
              parent.mainFrame.AddNewOption(formCurrent.cmb_ItemRespPago, "<%=strSiteId%>", "<%=strSiteName%>");              
              <%
            }
      }
      %>
    }
  function fxLoadPlanesTarifarios(){
    formCurrent = parent.mainFrame.frmdatos;
    parent.mainFrame.DeleteSelectOptions(formCurrent.cmb_ItemPlanTarifario);
    
  <% if ( objArrayListPlanes != null && objArrayListPlanes.size() > 0 ){
        
        PlanTarifarioBean objPlanBean = null;
        for( int i=0; i<objArrayListPlanes.size();i++ ){
                objPlanBean   = new PlanTarifarioBean();
                objPlanBean = (PlanTarifarioBean)objArrayListPlanes.get(i);
                DecimalFormat decFormat = new DecimalFormat("###");
    %>
                parent.mainFrame.vctPlan.addElement(new parent.mainFrame.fxMakePlan('<%=decFormat.format(objPlanBean.getNpplancode())%>','<%=objPlanBean.getNptipo2()%>'));
                parent.mainFrame.AddNewOption(formCurrent.cmb_ItemPlanTarifario,"<%=decFormat.format(objPlanBean.getNpplancode())%>","<%=MiUtil.getMessageClean(objPlanBean.getNpdescripcion())%>");
      <% }
     } %>
  }
  
 
  
   function fxLoadServiceAditiionalSolution(){   
  
    parent.mainFrame.vServicio.removeElementAll();
    parent.mainFrame.vServicioPorDefecto.removeElementAll();
    
   <%if ( objArrayList != null && objArrayList.size() > 0 ){
       ServiciosBean serviciosBean = null;
       for( int i = 0; i < objArrayList.size(); i++ ){
          serviciosBean = (ServiciosBean)objArrayList.get(i);%>
          parent.mainFrame.vServicio.addElement(new Servicio("<%=serviciosBean.getNpservicioid()%>","<%=serviciosBean.getNpnomserv()%>","<%=serviciosBean.getNpnomcorserv()%>","<%=MiUtil.getString(serviciosBean.getNpexcludingind())%>"));
      
     <%}
     }
    %>
    
    parent.mainFrame.fxLongMaxServices("<%=objArrayList%>");
    
   <%if( objArrayListDefault != null && objArrayListDefault.size() > 0 ) {
       ServiciosBean serviciosBeanDefault = null;
       for( int j = 0; j < objArrayListDefault.size(); j++ ){
          serviciosBeanDefault = (ServiciosBean)objArrayListDefault.get(j);%>
          parent.mainFrame.vServicioPorDefecto.addElement(new Servicio("<%=serviciosBeanDefault.getNpservicioid()%>","<%=serviciosBeanDefault.getNpnomserv()%>","<%=serviciosBeanDefault.getNpnomcorserv()%>","<%=MiUtil.getString(serviciosBeanDefault.getNpexcludingind())%>"));
       <%}
     }%>
   }
   
   function fxLoadLineaProducto(){
    formCurrent = parent.mainFrame.frmdatos;
    parent.mainFrame.DeleteSelectOptions(formCurrent.cmb_ItemProductLine);
      <% if ( objArrayListLinea != null && objArrayListLinea.size() > 0 ){
          ProductLineBean productLineBean = null;
          for( int i=0; i<objArrayListLinea.size();i++ ){ 
             productLineBean = new ProductLineBean();
             productLineBean = (ProductLineBean)objArrayListLinea.get(i);
       %>
             parent.mainFrame.AddNewOption(formCurrent.cmb_ItemProductLine,"<%=productLineBean.getNpProductLineId()%>","<%=productLineBean.getNpName()%>");
        <%
          }
      }%>
   }
  

</script>
   
<script "language=javascript">
  <%if ( objRespPagoList != null && objRespPagoList.size() > 0 ){%>       
     fxLoadRespPago();       
  <%}%> 
  <%if ( objArrayListPlanes != null && objArrayListPlanes.size() > 0 ){%>
   fxLoadPlanesTarifarios();
   <%}%>
   
  <%if ( objArrayListLinea != null && objArrayListLinea.size() > 0 ){%>
   fxLoadLineaProducto();
   <%}%>
  
   <%if ( objArrayList != null && objArrayList.size() > 0 ){%>
     fxLoadServiceAditiionalSolution();
     serviceAditional = GetSelectedServices();
     parent.mainFrame.fxCargaServiciosItem(serviceAditional);
     parent.mainFrame.fxCargaServiciosPorDefecto();
   <%}%>
   
   <%
   if(strSpecificationId.equals("2023")) { // strSpecificationId=  Desactivacion Bolsa
     if (message==null || message.trim().equals("")) {
     %>
        var formCurrent = parent.mainFrame.frmdatos;  
        formCurrent.cmb_ItemProductBolsa.value = "<%=strProductName%>";
        formCurrent.cmb_ItemProductBolsaId.value = "<%=strProductId%>";
        formCurrent.txt_ItemMntsRatesProdAct.value = "<%=strMinuteRate%>";
        formCurrent.txt_ItemPriceByMntsAux.value = "<%=strPriceByMinute%>";
        formCurrent.cmb_ItemSolution.value = "<%=strSolutionId%>";
        SetCmbDefaultValue(parent.mainFrame.frmdatos.cmb_ItemNivel,"<%=strLevel%>");

      <%} else { %>
        var formCurrent = parent.mainFrame.frmdatos;  
        formCurrent.cmb_ItemProductBolsa.value = "";
        formCurrent.cmb_ItemProductBolsaId.value = "";
        formCurrent.txt_ItemMntsRatesProdAct.value = "";
        formCurrent.txt_ItemPriceByMntsAux.value = "";
        formCurrent.cmb_ItemSolution.value = "";
        alert("<%=message%>");
      <%}%>
    <%}%>


   <%
   if(strSpecificationId.equals("2022")) { // strSpecificationId= Bolsa cambio - solo para bolsa cambio y Desactivacion
     if (message==null || message.trim().equals("")) {
     %>
        var formCurrent = parent.mainFrame.frmdatos;  
        formCurrent.cmb_ItemProductBolsa.value = "<%=strProductName%>";
        formCurrent.cmb_ItemProductBolsaId.value = "<%=strProductId%>";
        formCurrent.txt_ItemMntsRatesProdAct.value = "<%=strMinuteRate%>";
        formCurrent.cmb_ItemSolution.value = "<%=strSolutionId%>";
        formCurrent.cmb_ItemNewPlantarifario.value = "<%=strDescripcionPlanTarifario%>";
        formCurrent.txt_ItemNewPlantarifarioId.value = "<%=strPlantarifario%>";
        SetCmbDefaultValue(parent.mainFrame.frmdatos.cmb_ItemNivel,"<%=strLevel%>");
      <%} else { %>
        var formCurrent = parent.mainFrame.frmdatos;  
        formCurrent.cmb_ItemProductBolsa.value = "";
        formCurrent.cmb_ItemProductBolsaId.value = "";
        formCurrent.txt_ItemMntsRatesProdAct.value = "";
        formCurrent.txt_ItemPriceByMntsAux.value = "";
        formCurrent.cmb_ItemSolution.value = "";
        alert("<%=message%>");
      <%}%>
    <%}%>

 </script>

<%}catch(Exception ex){%>
  <script DEFER>
    alert("<%=MiUtil.getMessageClean(ex.getMessage())%>")
  </script>
<%}%>

</body>
</html>