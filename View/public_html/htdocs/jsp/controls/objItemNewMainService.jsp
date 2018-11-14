<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.NewOrderService" %>
<%@ page import="pe.com.nextel.bean.ServiciosBean" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.ArrayList" %>
<%
  String    type_window =  MiUtil.getString((String)request.getAttribute("type_window"));
  NewOrderService objNewOrderService = new NewOrderService();
  /*
  Hashtable hshtinputNewSection = null;
  String    strCodigoCliente= "",
            strnpSite ="",
            strCodBSCS = "",
            hdnSpecification = "",
            strSolution = "";
  
  hshtinputNewSection     = (Hashtable)request.getAttribute("hshtInputNewSection");
  
  if ( hshtinputNewSection != null ) {
    strCodigoCliente        =   (String)hshtinputNewSection.get("strCodigoCliente");
    strnpSite               =   (String)hshtinputNewSection.get("strnpSite");
    strCodBSCS              =   (String)hshtinputNewSection.get("strCodBSCS");
    hdnSpecification        =   (String)hshtinputNewSection.get("hdnSpecification");
    strSolution             =   (String)hshtinputNewSection.get("strSolution");
    request.setAttribute("hshtInputNewSection",hshtinputNewSection);
  }
  */
  String nameHtmlItem = (String)request.getParameter("nameObjectHtml");
  if ( nameHtmlItem==null ) nameHtmlItem = "";
  
  String strMessage = "";
  
  //HashMap objArrayList = objNewOrderService.getServiceRentList(MiUtil.parseLong(""));
  
%>
 
<select name="<%=nameHtmlItem%>"> 
   <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
   
   

<%
if( !type_window.equals("NEW") ){
  String strPlanId    = "";
 
  String[] paramNpobjitemvalue      = request.getParameterValues("b");
  String[] paramNpobjitemheaderid   = request.getParameterValues("a");
  
  for(int i=0;i<paramNpobjitemheaderid.length; i++){
    //Plan Tarifario Id
    if( paramNpobjitemheaderid[i].equals("10") )
        strPlanId = paramNpobjitemvalue[i];
  }
%>
    
  <script>
    function fxLoadEditNewMainService(){
    formCurrent = parent.mainFrame.frmdatos;
    parent.mainFrame.DeleteSelectOptions(formCurrent.cmb_ItemNewMainService);
    <%
    HashMap hshServiceRentList  = objNewOrderService.getServiceRentList(MiUtil.parseLong(strPlanId));
    
    ArrayList objServiceRent = (ArrayList)hshServiceRentList.get("objServiciosBean");
   
     for(int i=0;i<objServiceRent.size();i++){
        ServiciosBean  objServiciosBean =(ServiciosBean)objServiceRent.get(i);
    %>
      parent.mainFrame.AddNewOption(parent.mainFrame.frmdatos.cmb_ItemNewMainService,'<%=objServiciosBean.getNpservicioid()%>','<%=objServiciosBean.getNpnomserv()%>');
    <%}%>
    }
   </script>
 <%}%>
