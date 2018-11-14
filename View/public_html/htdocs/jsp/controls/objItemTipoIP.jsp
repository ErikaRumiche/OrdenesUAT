<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.NewOrderService" %>
<%@ page import="pe.com.nextel.service.GeneralService" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%
  Hashtable hshtinputNewSection = null;
  NewOrderService objNewOrderService = new NewOrderService();
  GeneralService   objGeneralService   = new GeneralService();
  String type_window =  MiUtil.getString((String)request.getAttribute("type_window"));
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
  
  String strMessage = "";
  
  HashMap objHashMap = new HashMap();
  
  if( objHashMap.get("strMessage")!=null ) throw new Exception((String)objHashMap.get("strMessage"));
      ArrayList objArrayList = (ArrayList)objHashMap.get("objArrayList");
	
  
%>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  </head>
  
  <body>
  
      <select name=<%=nameHtmlItem%> >
          <%  objHashMap = objGeneralService.getDominioList(Constante.IP);
              objArrayList = (ArrayList) objHashMap.get("arrDominioList");
              strMessage = (String) objHashMap.get(Constante.MESSAGE_OUTPUT);
              if (StringUtils.isBlank(strMessage)){%>
                  <%=MiUtil.buildComboSelected(objArrayList, Constante.TIPO_IP_GENERICO)%>
              <%}%>
	  </select>

</body>
</html>

