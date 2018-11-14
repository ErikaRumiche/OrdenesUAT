<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="org.apache.commons.collections.MapUtils"%>
<%@page import="pe.com.nextel.util.MiUtil"%>
<%@page import="pe.com.nextel.bean.LoadMassiveItemBean"%>
<%@page import="pe.com.nextel.util.Constante"%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Tabla Imei - Sim</title>
  <link type="text/css" rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"/>
  <script language="JavaScript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
</head>
<body style="padding: 0px; margin: 0px" bgcolor="#FDFDF5">
<%
	HashMap hshDataMap = (HashMap) request.getAttribute(Constante.DATA_STRUCT);
	String strMessage = MapUtils.getString(hshDataMap, Constante.MESSAGE_OUTPUT);
	if (!MiUtil.isNotNull(strMessage)) {
		LoadMassiveItemBean loadMassiveItemBean = (LoadMassiveItemBean) MapUtils.getObject(hshDataMap, "loadMassiveItemBean");
    if(loadMassiveItemBean != null) {
%>
<script language="JavaScript" defer="defer">
<%    if(loadMassiveItemBean.getLCantErroneos().longValue() > 0) {
%>      parent.document.frmdatos.btnAceptar.disabled = true;
        alert("Hay <%=loadMassiveItemBean.getLCantErroneos()%> entrada(s) que ha(n) fallado la validación. Revisar en la columna Mensaje.");
<%    } else {
%>      parent.document.frmdatos.btnAceptar.disabled = false;
<%    }
%>
</script>
<table style="border: 1px solid rgb(40,40,40)" align="center" width="100%">
	<tr>
    <th class="CellLabel" width="3%">#</th>
		<th class="CellLabel" width="13%">IMEI</th>
		<th class="CellLabel" width="13%">SIM</th>
		<th class="CellLabel" width="71%">Mensaje</th>
	</tr>
<%		for (int i = 0; i < loadMassiveItemBean.getImei().size(); i++) {
%>
	<tr>
    <td class="CellContent" align="right"><%=(i+1)%></td>
		<td class="CellContent" align="center"><%=loadMassiveItemBean.getImei(i)%></td>
		<td class="CellContent" align="center"><%=loadMassiveItemBean.getSim(i)%></td>
		<td class="CellContent"><%=MiUtil.defaultString(loadMassiveItemBean.getMessage(i), "Aún falta validar en Base de Datos")%></td>
	</tr>
<%
      }
%>
</table>
<%  }
	} else {
%>
<div style="padding: 10px;">
<font face="Verdana"><%="<b>ERROR:</b> " + strMessage%></font>
</div>
<%
	}
%>
</body>
</html>