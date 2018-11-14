<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest"%>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants"%>
<%@ page import="oracle.portal.provider.v2.ProviderUser"%>
<%@ page import="pe.com.nextel.bean.DominioBean"%>
<%@ page import="pe.com.nextel.bean.PortalSessionBean"%>
<%@ page import="pe.com.nextel.form.RetentionForm"%>
<%@ page import="pe.com.nextel.service.GeneralService"%>
<%@ page import="pe.com.nextel.service.OrderSearchService"%>
<%@ page import="pe.com.nextel.service.SessionService"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="pe.com.nextel.util.GenericObject"%>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<%@ page import="pe.com.nextel.exception.SessionException"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="org.apache.log4j.Logger"%>
<%@ page import="org.displaytag.tags.TableTagParameters"%>
<%@ page import="org.displaytag.util.ParamEncoder"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net/"%>
<%! 
protected static Logger logger = Logger.getLogger(HttpServlet.class);

protected String formatException(Throwable e) {
	return GenericObject.formatException(e);
}
%>
<% 
try {
  logger.debug("--Inicio--");
  String strHdnNumPagina = StringUtils.defaultString(request.getParameter("hdnNumPagina"),"1");   
  strHdnNumPagina = strHdnNumPagina.replaceAll(",","");
  long lPageSelected = Long.parseLong(strHdnNumPagina);
  String strHdnNumRegistros = StringUtils.defaultString(request.getParameter("hdnNumRegistros"),"15");
  int iRowsByPage = Integer.parseInt(strHdnNumRegistros);
  String strNumTotalRegistros = "0";
  String strBuildOptionsSelected = "";
  long lNumTotalRegistros = 0;
  RetentionForm retentionForm = (RetentionForm) request.getAttribute("retentionForm");
  
  if (retentionForm!=null) {    
    retentionForm.setHdnNumPagina(strHdnNumPagina);
    OrderSearchService orderSearchService = new OrderSearchService();
    HashMap hshRepairListMap = orderSearchService.getRetentionList(retentionForm);
    //strBuildOptionsSelected = orderSearchService.buildOptionsSelected(retentionForm);
    
    if(logger.isDebugEnabled())
      logger.debug("Obtuvo la lista relativa a la busqueda");
      
    ArrayList arrListado = (ArrayList) hshRepairListMap.get("arrListado");
    //strNumTotalRegistros = (String) hshRepairListMap.get("numTotalRegistros");
    strNumTotalRegistros = "37";
    lNumTotalRegistros = Long.parseLong(strNumTotalRegistros);
    
    if(arrListado != null) {
      request.setAttribute("arrListado", arrListado);
    }
  } else {
    retentionForm = new RetentionForm();
  }
  request.getSession(true).setAttribute("retentionForm", retentionForm);
  String strLogin = StringUtils.defaultIfEmpty(request.getParameter("hdnLogin"),"");
  

 
%>
<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/displaytag.css"></link>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>

<form name="frmdatos" action="<%=request.getContextPath()%>/repairServlet" method="POST">

  <input type="hidden" name="hdnNumRegistros" id="hdnNumRegistros" value="<%=Constante.NUM_REGISTROS_X_PAGINAS%>">
  <input type="hidden" name="hdnNumPagina" id="hdnNumPagina" value="1">
  <input type="hidden" name="hdnMethod" value=""/>
  
  <table border="0" cellspacing="0" cellpadding="0" align="center" width="99%">
    <tr bgcolor="#F5F5EB">      
      <td align="right" width="18%" valign="top">
        <a href="javascript:fxGoToOtherSearch()"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/binocular.gif" border="0" width="88" height="24" alt="Otra Búsqueda"></a>
      </td>
    </tr>
  </table>
  
  <table border="0" cellspacing="7" cellpadding="0" width="99%" align="center"><tr><td class="CellContent">
    <%String parameterPage = new ParamEncoder("repair").encodeParameterName(TableTagParameters.PARAMETER_PAGE);%>
    <input type="hidden" name="<%=parameterPage%>" value="<%=strHdnNumPagina%>"/>
    <input type="hidden" name="hdnLogin" value="<%=strLogin%>"/>
    
    <display:table id="repair" name="arrListado" class="orders" style="width: 100%">
    
        <display:column property="MOTIVO" title="Motivo" style="text-align:left;" group="1"/>
        <display:column property="AREA" title="Area" style="text-align:left;" group="2"/>
        <display:column property="ASESOR" title="Asesor" style="text-align:center;" group="3"/>
        <display:column property="PROGRAMADAS" title="Programadas" style="text-align:center;"/>
        <display:column property="RETENIDAS" title="Retenidas" style="text-align:center;"/>
        <display:column property="EFECTIVIDAD" title="% Efectividad" style="text-align:center;"/>
      
    </display:table>
  </td></tr></table>
</form>
<script type="text/javascript">

  window.onbeforeunload = function () {
    fxGoToOtherSearch();
  }

  //window.onbeforeunload = fxGoToOtherSearch;
  
	function fxGoToOtherSearch() {
    vForm = document.frmdatos;
    vForm.hdnMethod.value = "searchFormRetention";
    vForm.action = "<%=request.getContextPath()%>/orderSearchServlet";
    vForm.submit();
	}
  //JHERRERA: Fin

</script>
<%      
} catch(Exception e) {
  e.printStackTrace();
  String strMessageExceptionGeneralStart = "";
  strMessageExceptionGeneralStart = MiUtil.getMessageClean(e.getMessage());
  out.println("<script>alert('"+strMessageExceptionGeneralStart+"');</script>");   
}
%>