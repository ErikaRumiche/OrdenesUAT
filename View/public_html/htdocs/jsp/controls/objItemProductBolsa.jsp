<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.NewOrderService" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="pe.com.nextel.bean.ProductBean" %>
<%@ page import="pe.com.nextel.bean.PlanTarifarioBean" %>
<%
  Hashtable hshtinputNewSection = null;
  NewOrderService objNewOrderService = new NewOrderService();
  String type_window  =  MiUtil.getString((String)request.getAttribute("type_window"));
  String objTypeEvent =  MiUtil.getString((String)request.getAttribute("objTypeEvent"));
  String    strCodigoCliente= "",
            strnpSite ="",
            strCodBSCS = "",
            hdnSpecification = "",
            strDivision = "";
  
  hshtinputNewSection     = (Hashtable)request.getAttribute("hshtInputNewSection");
  
  if ( hshtinputNewSection != null ) {
    strCodigoCliente        =   (String)hshtinputNewSection.get("strCodigoCliente");
    strnpSite               =   (String)hshtinputNewSection.get("strnpSite");
    strCodBSCS              =   (String)hshtinputNewSection.get("strCodBSCS");
    hdnSpecification        =   (String)hshtinputNewSection.get("hdnSpecification");
    strDivision             =   (String)hshtinputNewSection.get("strDivision");
    request.setAttribute("hshtInputNewSection",hshtinputNewSection);
  }
  
  String nameHtmlItem = (String)request.getParameter("nameObjectHtml");
  if ( nameHtmlItem==null ) nameHtmlItem = "";
  
  String strProductName = "";
  String strProductId   = "";
  String strMinuteRate  = "";
  String strPriceByMinute  = "";
  String strSolutionId = "";
  String strLevel = "";
  String message="";
  String strPlantarifario = "";
  String strDescripcionPlanTarifario = "";  
  
  if( !type_window.equals("NEW") ){
    String[] paramNpobjitemheaderid = request.getParameterValues("a");
    String[] paramNpobjitemvalue    = request.getParameterValues("b");

    for(int i=0;i<paramNpobjitemheaderid.length; i++){
      if( paramNpobjitemheaderid[i].equals("93") ) {
        strSolutionId = MiUtil.getString(paramNpobjitemvalue[i]);
      }
    }
  }
  
  HashMap objHashMap = objNewOrderService.getProductBolsa(MiUtil.parseLong(strCodigoCliente),MiUtil.parseLong(strnpSite),MiUtil.parseLong(strSolutionId));
  if( objHashMap.get("strMessage")!=null ) throw new Exception((String)objHashMap.get("strMessage"));
  
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

  if( type_window.equals("NEW") ){
    strProductName = "";	
  }

%>
   
   <tr>
   <td class="CellLabel" align="left" valign="top"><font color="red">&nbsp;*&nbsp;</font>Producto Bolsa</td>
   <td align="left" class="CellContent" >
      &nbsp;&nbsp;<input type="text" name="<%=nameHtmlItem%>" maxlength="30" value="<%=strProductName%>" readonly >
   </td>
   </tr>

<script>
  function fxLoadProductBolsa(){

  
  <%if (!hdnSpecification.trim().equals("2023")) { %>

    formCurrent = parent.mainFrame.frmdatos;
    formCurrent.cmb_ItemProductBolsaId.value = '<%=strProductId%>'; 
    SetCmbDefaultValue(parent.mainFrame.frmdatos.cmb_ItemNivel,"<%=strLevel%>");
    try{
    formCurrent.txt_ItemMntsRatesProdAct.value = '<%=strMinuteRate%>';
    }catch(e){}
    
    try{
    formCurrent.txt_ItemPriceByMntsAux.value = '<%=strPriceByMinute%>';
    }catch(e){}

    try{
    formCurrent.cmb_ItemSolution.value = '<%=strSolutionId%>';
    
    <%if (!hdnSpecification.trim().equals("2022")) { 
       
       if (hdnSpecification.trim().equals("2023"))
       
        {%> //Bolsa Cambio
    
          formCurrent.cmb_ItemSolution.disabled = false;

       
        <%} else { %>   
          
      formCurrent.cmb_ItemSolution.disabled=true;
    
    <%}%>
       
    
    <%}%>


    }catch(e){}
    
    <%if( type_window.equals("EDIT") && !hdnSpecification.trim().equals("2022")){%>
    try{
    formCurrent.txt_ItemPriceByMnts.value = '<%=strPriceByMinute%>';
    }catch(e){}
    <%}%>
    
    <%if( type_window.equals("DETAIL")  || type_window.equals("EDIT") ){%>
    try{
    formCurrent.cmb_ItemNewPlantarifario.value = '<%=strDescripcionPlanTarifario%>';
    }catch(e){}
    <%}%>
    
    <%if( !objTypeEvent.equals("NEW") ){
    String[] paramNpobjitemvalue      = request.getParameterValues("b");
    String[] paramNpobjitemheaderid   = request.getParameterValues("a");
    String   strProductBolsa = "";
    
    //if(paramNpobjitemheaderid!=null){ //Este seria la solucion al problema PHIDALGO lo encontre sin esta validación 
    for(int i=0;i<paramNpobjitemheaderid.length; i++)
    //Product Bolsa
    if( paramNpobjitemheaderid[i].equals("73") )
        strProductBolsa = paramNpobjitemvalue[i];
        
    objHashMap = objNewOrderService.getProductDetail(MiUtil.parseLong(strProductBolsa));
    if( objHashMap.get("strMessage")!=null ) throw new Exception((String)objHashMap.get("strMessage"));
    
    objProductBean  = (ProductBean)objHashMap.get("objProductBean");
    strProductBolsa = MiUtil.getString(objProductBean.getNpproductname());
    //}
    %>
    formCurrent.<%=nameHtmlItem%>.value = "<%=strProductBolsa%>";
    <%}%>


   <%} else { %>
    
          formCurrent.cmb_ItemProductBolsa.disabled = true;

          formCurrent.txt_ItemMntsRatesProdAct.disabled = true;
        
          formCurrent.txt_ItemPriceByMntsAux.disabled = true;

          formCurrent.cmb_ItemNivel.disabled = true;



   <%}%>

  }
</script>