<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@ page import="oracle.portal.provider.v2.ProviderSession" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="pe.com.nextel.bean.PortalSessionBean" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.service.OrderTabsService" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net/"%>
<%
try {
	String strOrderId = StringUtils.defaultString(request.getParameter(Constante.PARAM_ORDER_ID),"0");  
	System.out.println(Constante.PARAM_ORDER_ID+": "+strOrderId);
	long lOrderId = Long.parseLong(strOrderId);
	OrderTabsService objOrderTabsService = new OrderTabsService();
	ArrayList arrHistoryActionList = objOrderTabsService.getHistoryActionListByOrder(lOrderId);
  ArrayList arrRequestOLList = objOrderTabsService.getRequestOLListByOrder(lOrderId);
    System.out.println("Tamaño del ArrayList arrHistoryActionList: " + arrHistoryActionList.size());
    System.out.println("Tamaño del ArrayList arrRequestOLList: " + arrRequestOLList.size());
	if(arrHistoryActionList!=null) {
		request.setAttribute("arrHistoryActionList", arrHistoryActionList);
	}
  if(arrRequestOLList!=null) {
		request.setAttribute("arrRequestOLList", arrRequestOLList);
	}
%>
<link rel="stylesheet" type="text/css" href="/websales/Resource/salesweb.css"></link>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script type="text/javascript" src="/web_operacion/js/syncscroll.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/date-picker.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/ua.js"></script>
<style>
	table.orders {
		border: 0px;
		width: 100%;
		margin: 1px 0 1px 0 !important;
		font-weight: normal;
		font-size: 8pt;
		color: #000000;
		font-family: Arial, Helvetica; 
	}
	th {
		padding: 2px 4px 2px 4px !important;
		text-align: left;
		vertical-align: top;
		font-weight: bold;
	}
	thead tr {
		background-color: #E8E4B7
	}
	tr.odd {
		background-color: #F5F5EB
	}
	tr.even {
		background-color: #E8E4B7
	}
	.spanPaging {
		font-weight: normal;
		font-size: 8pt;
		font-family: Arial, Helvetica;
		color: #000000;
	}
	span.excel {
		background-image: url(../../websales/img/ico_file_excel.png);
	}
	span.csv {
		background-image: url(../../websales/img/ico_file_csv.png);
	}
	span.xml {
		background-image: url(../../websales/img/ico_file_xml.png);
	}
	span.pdf {
		background-image: url(../../websales/img/ico_file_pdf.png);
	}
	span.rtf {
		background-image: url(../../websales/img/ico_file_rtf.png);
	}
	span.pagebanner {
		background-color: #eee;
		border: 1px dotted #999;
		padding: 2px 4px 2px 4px;
		width: 79%;
		margin-top: 10px;
		display: block;
		border-bottom: none;
		font-weight: normal;
		font-size: 8pt;
		font-family: Arial, Helvetica;
	}
	div.exportlinks {
		background-color: #eee;
		border: 1px dotted #999;
		padding: 2px 4px 2px 4px;
		margin: 2px 0 10px 0;
		width: 79%;
		font-weight: normal;
		font-size: 8pt;
		font-family: Arial, Helvetica;
	}
	span.export {
		padding: 0 4px 1px 20px;
		display: inline;
		display: inline-block;
		cursor: pointer;
		font-weight: normal;
		font-size: 8pt;
		font-family: Arial, Helvetica;
	}
</style>
<form name="frmdatos" action="<%=request.getContextPath()%>/orderSearchServlet" method="POST">
	
    <table border="0" cellspacing="7" cellpadding="0" width="99%" align="center"><tr><td>
	
	<display:table name="arrHistoryActionList" class="orders" export="true" pagesize="100" style="width: 100%">
		<display:column property="swsender" title="Enviado por"/>
		<display:column property="swaction" title="Acción Req."/>
		<display:column property="swreceiver" title="Receptor"/>
		<display:column property="swmessagetype" title="Tipo"/>
		<display:column property="swdatereceived" title="Fecha recepción"/>
		<display:column property="swactiontaken" title="Acción tomada"/>
		<display:column property="swdateactiontaken" title="Acción tomada el"/>
		<display:column property="swactiontakenby" title="Acción tomada por"/>
		<display:column property="swpriority" title="Prioridad"/>
		<display:column property="swnote" title="Nota"/>
	</display:table>
	
    </td></tr></table>
    
        <table border="0" cellspacing="7" cellpadding="0" width="99%" align="center">
    <tr><td class="SectionTitle" colspan="9" align="center">Listado de Solicitudes a Almacén</td></tr>
  <tr><td>
  <display:table name="arrRequestOLList" class="orders" export="false" pagesize="100" style="width: 100%">
		<display:column property="nprequestolid" title="Id"/>
		<display:column property="nprequestnumber" title="Solicitud a almacén"/>
		<display:column property="npcreateddate" title="Fecha Generación"/>
		<display:column property="npcreatedby" title="Creado por"/>
		<display:column property="npmodifydate" title="Fecha Transacción"/>
		<display:column property="npmodifyby" title="Atendido por"/>
		<display:column property="tipo_doc" title="Tipo Documento"/>
		<display:column property="tipo_sol" title="Tipo Solicitud"/>
		<display:column property="estado" title="Estado del Documento"/>
	</display:table>
    
    </td></tr></table>
    
</form>
<script type="text/javascript">
    
</script>
<%} catch(Exception e) {
	e.printStackTrace();
    out.println("Mensaje::::" + e.getMessage() + "<br>");
    out.println("Causa::::" + e.getCause() + "<br>");
    for (int i = 0; i < e.getStackTrace().length; i++)
        out.println("    " + e.getStackTrace()[i] + "<br>");
}%>