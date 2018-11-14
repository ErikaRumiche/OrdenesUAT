<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="java.util.Calendar" %>

<%
    try{
        String npPhoneNumber=(request.getParameter("npPhoneNumber")==null?"":request.getParameter("npPhoneNumber").toString());
        String npPortabItemId=(request.getParameter("npPortabItemId")==null?"":request.getParameter("npPortabItemId").toString());
        String npAplicationId=(request.getParameter("npAplicationId")==null?"":request.getParameter("npAplicationId").toString());
        String npAmountDue=(request.getParameter("npAmountDue")==null?"":request.getParameter("npAmountDue").toString());
        String npCurrencyType=(request.getParameter("npCurrencyType")==null?"":request.getParameter("npCurrencyType").toString());
        String npCurrencyDesc=(request.getParameter("npCurrencyDesc")==null?"":request.getParameter("npCurrencyDesc").toString());
        String npAssignorId=(request.getParameter("npAssignorId")==null?"":request.getParameter("npAssignorId").toString());

        Calendar calendar = Calendar.getInstance();

        System.out.println(" ----------- INICIO SubSanacionPortability.jsp---------------- ");
        System.out.println("npPhoneNumber-->"+npPhoneNumber);
        System.out.println("npPortabItemId-->"+npPortabItemId);
        System.out.println("npAplicationId-->"+npAplicationId);
        System.out.println("npAmountDue-->"+npAmountDue);
        System.out.println("npCurrencyType-->"+npCurrencyType);
        System.out.println("npCurrencyDesc-->"+npCurrencyDesc);
        System.out.println("npAssignorId-->"+npAssignorId);
        System.out.println(" ------------  FIN SubSanacionPortability.jsp----------------- ");
%>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/date-picker.js"></script>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Subsanación Portabilidad</title>
</head>
<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css">

<body>
<form name="formdatos" enctype="multipart/form-data" method="post">

    <table border="0" class="PortletHeaderColor" cellspacing="0" cellpadding="0" width="100%">
        <tr class="PortletHeaderColor">
            <td class="LeftCurve" valign="top" align="left" width="10" >&nbsp;&nbsp;</td>
            <td class="PortletHeaderText" align="LEFT" valign="top">Subsanación Portabilidad&nbsp;<%=npPhoneNumber%></td>
            <td align="right" class="RightCurve" width="10" >&nbsp;&nbsp;</td>
        </tr>
    </table>

    <input type="hidden" name="hdnAplicationId" id="hdnAplicationId" value="<%=npAplicationId%>">
    <input type="hidden" name="hdnPortabItemId" id="hdnPortabItemId" value="<%=npPortabItemId%>">
    <input type="hidden" name="hdnPhoneNumber" id="hdnPhoneNumber" value="<%=npPhoneNumber%>">
    <input type="hidden" name="hdnAmountDue" id="hdnAmountDue" value="<%=npAmountDue%>">
    <input type="hidden" name="hdnCurrencyType" id="hdnCurrencyType" value="<%=npCurrencyType%>">
    <input type="hidden" name="hdnCurrencyDesc" id="hdnCurrencyDesc" value="<%=npCurrencyDesc%>">
    <input type="hidden" name="hdnAssignorId" id="hdnAssignorId" value="<%=npAssignorId%>">

    <table border="0" cellspacing="1" cellpadding="1" align="center" class="RegionBorder" width="100%">
        <tr>
            <td class="CellLabel" width="15%">Entidad</td>
            <td class="CellContent" width="35%">
                <input type="text" id="txtEntidad" name="txtEntidad" size="20"
                       min="1" maxlength="25" onblur="this.value=fxTrim(this.value);"/>
            </td>
        </tr>
        <tr>
            <td class="CellLabel" width="15%">Monto</td>
            <td class="CellContent" width="35%">
                <input type="text" id="txtMonto" name="txtMonto" size="7" value="<%=npAmountDue%>"
                       disabled="disabled" min="1" maxlength="10" onblur="this.value=fxTrim(this.value);"/>
            </td>
        </tr>
        <tr>
            <td class="CellLabel" width="15%">Moneda</td>
            <td class="CellContent" width="35%">
                <input type="text" id="txtMoneda" name="txtMonto" size="7" value="<%=npCurrencyDesc%>"
                       disabled="disabled" min="1" maxlength="10" onblur="this.value=fxTrim(this.value);"/>
            </td>
        </tr>
        <tr>
            <td class="CellLabel" width="15%">Nro. Transacción</td>
            <td class="CellContent" width="35%">
                <input type="text" id="txtNroTransaccion" name="txtNroTransaccion" size="15"
                       maxlength="12" onblur="this.value=fxTrim(this.value);"/>
            </td>
        </tr>
        <tr>
            <td class="CellLabel" width="15%">Fecha</td>
            <td class="CellContent" valign="middle" colspan="3">
                <input type="text" name="txtFecha" size="10" maxlength="10" value="<%=MiUtil.getDate(new java.util.Date(), "dd/MM/yyyy")%>" onblur="this.value=fxTrim(this.value);">
                <a href="javascript:show_calendar('formdatos.txtFecha',null,null,'DD/MM/YYYY');" onmouseover="window.status='Fecha';return true;" onmouseout="window.status='';return true;">
                    <img src="<%=Constante.PATH_APPORDER_SERVER%>/images/show-calendar.gif" width="24" height="22" border="0"></a>&nbsp;<i>DD/MM/YYYY</i>
            </td>
        </tr>
        <tr>
            <td class="CellLabel" width="15%">Adjunto</td>
            <td class="CellContent" width="35%">
                <input type="file" id="txtfichero" name="txtfichero">
            </td>
        </tr>
        <tr>
            <table width="99%">
                <tr>
                    <td colspan="4" align="center">
                        <input value="Subsanar"  type="button" name="btnSubSanar" id="btnSubSanar" style="width: 100px;" onclick="fxSubsanarPortabilidad()"/>
                    </td>
                </tr>
            </table>
        </tr>
    </table>
</form>
</body>
</html>

<script type="text/javascript">

    function fxTrim(s) {
        while (s.length>0 && (s.charAt(0)==" "||s.charCodeAt(0)==10||s.charCodeAt(0)==13)) {
            s=s.substring(1, s.length);
        }
        while (s.length>0 && (s.charAt(s.length-1)==" "||s.charCodeAt(s.length-1)==10||s.charCodeAt(s.length-1)==13)) {
            s=s.substring(0, s.length-1);
        }
        return s;
    }

    function fxSubsanarPortabilidad() {

        var vForm = document.formdatos;
        var hdnEntidad = vForm.txtEntidad.value;
        var hdnMonto =  vForm.hdnAmountDue.value;
        var hdnMoneda =  vForm.hdnCurrencyType.value;
        var hdnNroTransaccion =  vForm.txtNroTransaccion.value;
        var hdnFecha =  vForm.txtFecha.value;
        var hdnAplicationId = vForm.hdnAplicationId.value;
        var hdnPortabItemId = vForm.hdnPortabItemId.value;
        var hdnPhoneNumber = vForm.hdnPhoneNumber.value;
        var hdnAdjunto = vForm.txtfichero.value;
        var hdnAssignorId = vForm.hdnAssignorId.value;

        if (hdnEntidad == "") {
            alert("Debe ingresar la entidad de pago")
        } else if (hdnMonto == "") {
            alert("Debe ingresar el monto de deuda")
        } else if (hdnNroTransaccion == "") {
            alert("Debe ingresar el número de transacción")
        } else if (hdnFecha == "") {
            alert("Debe ingresar la fecha de transacción")
        } else if (hdnAdjunto == "") {
            alert("Debe seleccionar el documento adjunto")
        } else {
            document.formdatos.action = "<%=request.getContextPath()%>/portabilityOrderServlet?hdnEntidad=" + hdnEntidad + "&hdnMoneda=" + hdnMoneda +
            "&hdnMonto=" + hdnMonto + "&hdnNroTransaccion=" + hdnNroTransaccion + "&hdnFecha=" + hdnFecha + "&hdnAplicationId=" + hdnAplicationId +
            "&hdnPortabItemId=" + hdnPortabItemId +"&hdnPhoneNumber=" + hdnPhoneNumber + "&hdnAssignorId=" + hdnAssignorId + "&hdnMethod=procesarSubsanacionDeuda";
            document.formdatos.submit();
        }

    }

</script>

<%
}catch(Exception ex) {
    System.out.println("Error try catch-->" + ex.getMessage());
%>

<script>
    alert("<%=MiUtil.getMessageClean(ex.getMessage())%>");
</script>
<%
}
%>