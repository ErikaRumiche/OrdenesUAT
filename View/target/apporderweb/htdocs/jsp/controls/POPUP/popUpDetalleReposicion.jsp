<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.service.GeneralService" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net/"%>
<%
try {
	String strTelefono = StringUtils.defaultString(request.getParameter("txt_ItemPhone"),"");
	System.out.println("strTelefono: "+strTelefono);
	GeneralService objGeneralService = new GeneralService();
	HashMap objHashMap = objGeneralService.getDetalleReposicionByTelefono(strTelefono);
	ArrayList arrDetalleReposicionList = (ArrayList) objHashMap.get("arrDetalleReposicionList");
	String strMessage = (String) objHashMap.get(Constante.MESSAGE_OUTPUT);
	if(strMessage!=null) {
		throw new Exception(strMessage);
	}
    System.out.println("Tamaño del ArrayList: " + arrDetalleReposicionList.size());
	if(arrDetalleReposicionList != null) {
		request.setAttribute("arrDetalleReposicionList", arrDetalleReposicionList);
	}
%>
<link rel="stylesheet" type="text/css" href="/websales/Resource/salesweb.css"></link>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/date-picker.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/ua.js"></script>
<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
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

<table border="0" cellspacing="7" cellpadding="0" width="99%" align="center">
	<tr>
		<td class="CellLabel" width="7%">Telefono:</td>
		<td class="CellContent" width="93%"><%=strTelefono%></td>
	</tr>
	<tr>
		<td colspan="2">
			<display:table name="arrDetalleReposicionList" class="orders" export="true" pagesize="100" style="width: 100%">
				<display:column property="npcoid" title="Contrato"/>
				<display:column property="fec_act_gn" title="Fecha Act. GN."/>
				<display:column property="fec_reposicion" title="Fecha Repo."/>
				<display:column property="fec_ini_period" title="Fec. Inic. Periodo GN."/>
				<display:column property="fec_fin_period" title="Fec. Fin. Periodo GN."/>
			</display:table>
		</td>
	</tr>
</table>
<%} catch(Exception e) {
	e.printStackTrace();
	out.println("<script>alert('"+e.getMessage()+"');</script>");
}%>