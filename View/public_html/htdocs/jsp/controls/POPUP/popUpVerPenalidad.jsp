<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.service.GeneralService" %>
<%@ page import="pe.com.nextel.service.PenaltyService" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net/"%>
<%
try {
    System.out.println("----------------popUpVerPenalidad-------------------");
	String strTelefono = StringUtils.defaultString(request.getParameter("txt_ItemPhone"),"");
	System.out.println("strTelefono: "+strTelefono);
    String strCustomerId = StringUtils.defaultString(request.getParameter("paramCustomer"),"");
    System.out.println("strCustomerId: "+strCustomerId);
	PenaltyService penaltyService = new PenaltyService();
	HashMap objHashMap = penaltyService.getPenaltySimulator(strTelefono, strCustomerId);
    HashMap datosPenalidad = (HashMap)objHashMap.get("datosPenalidad");
	String strMessage = (String) objHashMap.get(Constante.MESSAGE_OUTPUT);
	if(strMessage!=null) {
		throw new Exception(strMessage);
	}
%>
<link rel="stylesheet" type="text/css" href="/websales/Resource/salesweb.css"></link>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/date-picker.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/ua.js"></script>
<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
<table  align="center" width="100%" border="0">
    <tr><td>
        <table border="0" class="PortletHeaderColor" cellspacing="0" cellpadding="0" width="100%">
            <tr class="PortletHeaderColor">
                <td class="LeftCurve" valign="top" align="left" width="10">&nbsp;&nbsp;</td>
                <td class="PortletHeaderColor" align="left" valign="top">
                    <font class="PortletHeaderText">
                        Datos Generales
                    </font>
                </td>
                <td align="right" class="RightCurve" width="10%" >&nbsp;&nbsp;</td>
            </tr>
        </table>
    </td></tr>
    <!--Detalle de la penalidad-->
    <table align="center" width="100%" border="0">
        <tr>
            <td class="CellLabel" align="left" valign="top">
                Nro. Adenda
            </td>
            <td align="left" class="CellContent">
                <%=datosPenalidad.get("num_adenda")%>
            </td>
        </tr>
        <tr>
            <td class="CellLabel" align="left" valign="top">
                Plazo
            </td>
            <td align="left" class="CellContent">
                <%=datosPenalidad.get("plazo")%>
            </td>
        </tr>
        <tr>
            <td class="CellLabel" align="left" valign="top">
                Equipo
            </td>
            <td align="left" class="CellContent">
                <%=datosPenalidad.get("nom_equipo")%>
            </td>
        </tr>
        <tr>
            <td class="CellLabel" align="left" valign="top">
                Beneficio
            </td>
            <td align="left" class="CellContent">
                <%=datosPenalidad.get("monto_beneficio")%>
            </td>
        </tr>
        <tr>
            <td class="CellLabel" align="left" valign="top">
                F. Inicio
            </td>
            <td align="left" class="CellContent">
                <%=datosPenalidad.get("fecha_inicio")%>
            </td>
        </tr>
        <tr>
            <td class="CellLabel" align="left" valign="top">
                F. Venc. Real
            </td>
            <td align="left" class="CellContent">
                <%=datosPenalidad.get("fecha_fin")%>
            </td>
        </tr>
        <tr>
            <td class="CellLabel" align="left" valign="top">
                Total de Dias
            </td>
            <td align="left" class="CellContent">
                <%=datosPenalidad.get("total_dias")%>
            </td>
        </tr>
        <tr>
            <td class="CellLabel" align="left" valign="top">
                Dias Efectivos
            </td>
            <td align="left" class="CellContent">
                <%=datosPenalidad.get("dias_efectivos")%>
            </td>
        </tr>
        <tr>
            <td class="CellLabel" align="left" valign="top">
                <font color="red">Penalidad</font>
            </td>
            <td align="left" class="CellContent">
                <font color="red"><%=datosPenalidad.get("penalidad_simulada")%></font>
            </td>
        </tr>
    </table>
</table>
<%} catch(Exception e) {
	e.printStackTrace();
    out.println("<script>alert('Ocurrio un error');</script>");
%>
<div style="display: none;">
    <%=e.getMessage()%>
</div>
<%}%>