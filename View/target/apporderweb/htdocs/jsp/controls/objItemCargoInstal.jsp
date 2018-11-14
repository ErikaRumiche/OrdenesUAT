<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.*" %>
<%@ page import="pe.com.nextel.util.*" %>
<%@ page import="pe.com.nextel.bean.*" %>
<%@ page import="java.util.*" %>
<%@page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@page import="oracle.portal.provider.v2.ProviderSession" %>
<%@page import="oracle.portal.provider.v2.url.UrlUtils" %>
<%@page import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil" %>
<%@page import="pe.com.nextel.dao.*,pe.com.nextel.bean.*" %>
<%@ page import="pe.com.nextel.util.Constante" %>

<%
/*
Recuperando Parametros de Input
*/
  
  Hashtable hshtinputNewSection = null;
  GeneralService  objGeneralService = new GeneralService();
  String    strCodigoCliente= "",
            strnpSite ="",
            strCodBSCS = "",
            hdnSpecification = "",
            strDivisionId = "";
            
  hshtinputNewSection     = (Hashtable)request.getAttribute("hshtInputNewSection");
  
  if ( hshtinputNewSection != null ) {
    strCodigoCliente        =   (String)hshtinputNewSection.get("strCodigoCliente");
    strnpSite               =   (String)hshtinputNewSection.get("strnpSite");
    strCodBSCS              =   (String)hshtinputNewSection.get("strCodBSCS");
    hdnSpecification        =   (String)hshtinputNewSection.get("hdnSpecification");
    strDivisionId           =   (String)hshtinputNewSection.get("strDivision"); 
        
    request.setAttribute("hshtInputNewSection",hshtinputNewSection);
  }
  
  String nameHtmlItem = (String)request.getParameter("nameObjectHtml");  
  String strMessage = "";  
  String URL_Order_SharedInstalation     = "htdocs/jsp/Order_SharedInstalation.jsp";
%>


<script>
    function fxShowSharedInstalation(){   
       <%System.out.println("*");%>
       url= "<%=URL_Order_SharedInstalation%>";   
       WinAsist = window.open(url,"WinAsist","toolbar=no,location=0,directories=no,status=yes,menubar=0,scrollbars=no,resizable=no,screenX=100,top=80,left=100,screenY=80,width=700,height=500,modal=yes");       
    }
</script>
<tr>
<td class="CellLabel"   align="left">Cargo de Instalación</td>
<td class="CellContent" align="left">   
   <textarea readonly name="txt_ItemSharedInstall" value="" rows="2" cols="30"></textarea>
</td> 
<td class="CellLabel" align="center">&nbsp;     
   <a href="javascript:fxShowSharedInstalation();" onmouseover="window.status='Ver Detalle'; return true" onmouseout="window.status='';" >
   <img src="/websales/images/crear.gif" width="15" height="15" border="0"></a>      
</td>     
</tr>