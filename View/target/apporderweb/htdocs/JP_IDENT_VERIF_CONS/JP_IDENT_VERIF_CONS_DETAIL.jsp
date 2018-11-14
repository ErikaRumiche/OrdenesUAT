<%System.out.println("[JP_ISO_IDENT_VERIF_LIST][Inicio]");%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@ page import="oracle.portal.provider.v2.ProviderUser" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="pe.com.nextel.bean.PortalSessionBean" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.exception.SessionException" %>
<%@ page import="pe.com.nextel.service.SessionService" %>
<%@ page import="pe.com.nextel.bean.VerificationBean" %>
<%@ page import="java.util.List" %>
<%@ page import="pe.com.nextel.service.BiometricaService" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%
    try {
        System.out.println("RO001 Entrando detalle");
        HttpSession sessionActual = request.getSession(false);
        if (sessionActual != null){
            System.out.println(sessionActual.getAttribute("identifier"));
        }
        System.out.println(request.getParameter("identificador"));
        System.out.println("RO001 Fin detalle");
%>
<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/date-picker.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/jQuery-min.js"></script>

<form name="formdetail">
    <table border="0" cellspacing="7" cellpadding="0" width="99%" align="center">
        <tr>
            <td>
                <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
                    <tr>
                        <td style="text-align: right;font-family: Arial, Helvetica; font-size: 8pt; color: #000000; font-weight: normal;">
                            <a style="color:inherit;" href="javascript:window.history.back()">Regresar</a>
                        </td>
                    </tr>
                </table>
                <br/>
                <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
                    <tr>
                        <td class="SectionTitleLeftCurve" width="10">&nbsp;&nbsp;</td>
                        <td class="SectionTitle">&nbsp;&nbsp;Información del cliente</td>
                        <td class="SectionTitleRightCurve" width="8">&nbsp;&nbsp;</td>
                    </tr>
                </table>
                <table border="0" cellspacing="1" cellpadding="1" align="center" class="RegionBorder" width="100%">
                    <tr class="ListRow0">
                        <td class="CellLabel" width="15%">Nombres:</td>
                        <td id="lblNombres" class="CellContent" width="30%"></td>
                        <td class="CellLabel" width="15%">Apellidos:</td>
                        <td id="lblApellidos" class="CellContent" width="30%"></td>
                    </tr>
                    <tr class="ListRow0">
                        <td class="CellLabel" width="15%">Tipo de documento:</td>
                        <td id="lblTipoDocumento" class="CellContent" width="30%"></td>
                        <td class="CellLabel" width="15%">Número de documento:</td>
                        <td id ="lblNroDocumento" class="CellContent" width="30%"></td>
                    </tr>
                    <tr class="ListRow0">
                        <td class="CellLabel" width="15%">Tipo de verificación exitosa:</td>
                        <td id="lblTipoVerif" class="CellContent" width="30%"></td>
                        <td class="CellLabel" width="15%">Registrado por:</td>
                        <td id="lblRegistrado" class="CellContent" width="30%"></td>
                    </tr>
                    <tr class="ListRow0">
                        <td class="CellLabel" width="15%">Modificado por:</td>
                        <td id="lblModificado" class="CellContent" width="30%"></td>
                    </tr>
                </table>
                <br/>
                <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
                    <tr>
                        <td class="SectionTitleLeftCurve" width="10">&nbsp;&nbsp;</td>
                        <td style="text-align: center;" class="SectionTitle">&nbsp;&nbsp;Historial de validación Biométrica</td>
                        <td class="SectionTitleRightCurve" width="8">&nbsp;&nbsp;</td>
                    </tr>
                </table>
                <table id="verifBiometrica" border="0" cellspacing="1" cellpadding="1" align="center" class="RegionBorder" width="100%">
                    
                </table>
                <br/>
                <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
                    <tr>
                        <td class="SectionTitleLeftCurve" width="10">&nbsp;&nbsp;</td>
                        <td style="text-align: center;" class="SectionTitle">&nbsp;&nbsp;Historial de validación No Biométrica</td>
                        <td class="SectionTitleRightCurve" width="8">&nbsp;&nbsp;</td>
                    </tr>
                </table>
                <table id="verifNoBiometrica" border="0" cellspacing="1" cellpadding="1" align="center" class="RegionBorder" width="100%">
                    
                </table>
            </td>
        </tr>
    </table>
    <br/>
</form>
<script type="text/javascript">
    function fxOnLoad(){
        var server="<%=request.getContextPath()%>/biometricaDetailServlet";
        var params = 'myaction=obtenerDetalleVia&identificador=<%=request.getParameter("identificador")%>';
        $.ajax({
            url:server,
            data: params,
            dataType:'json',
            type: "GET",
            cache: false,
            success:function(data){
                if (data.verificationDetailBean != null) {
                    $("#lblNombres").html(data.verificationDetailBean.nombres);
                    $("#lblApellidos").html(data.verificationDetailBean.apellidos);
                    $("#lblTipoDocumento").html(data.verificationDetailBean.tipoDocumento);
                    $("#lblNroDocumento").html(data.verificationDetailBean.numeroDocumento);
                    $("#lblTipoVerif").html(data.verificationDetailBean.tipoVerificacion);
                    $("#lblRegistrado").html(data.verificationDetailBean.registradoPor);
                    $("#lblModificado").html(data.verificationDetailBean.modificadoPor);
                    
                    var arrHistoricoBiometrica = data.verificationDetailBean.historicoBiometrica;
                    var tblStr="<tr class='ListRow0'><td>Nro</td><td>Resultado Verificación</td><td>Motivo Verificación</td><td>Fecha de Creación</td></tr>";
                    if (arrHistoricoBiometrica != null && arrHistoricoBiometrica.length){
                        for (var i = 0; i < arrHistoricoBiometrica.length; i++) {
                            tblStr+="<tr class='ListRow1'>"
                            tblStr+="<td>"+ (i+1) +"</td>"
                            tblStr+="<td>"+ (arrHistoricoBiometrica[i].estado||'') +"</td>"
                            tblStr+="<td>"+ (arrHistoricoBiometrica[i].motivo||'') +"</td>"
                            tblStr+="<td>"+ (arrHistoricoBiometrica[i].fechaCreacion||'') +"</td>"
                            tblStr+="</tr>"
                        }
                    }else{
                        tblStr+="<tr class='ListRow1'><td>No se encontraron registros</td></tr>"
                    }
                    $("#verifBiometrica").html(tblStr);

                    var arrHistoricoNoBiometrica = data.verificationDetailBean.historicoNoBiometrica;
                    tblStr="<tr class='ListRow0'><td>Nro</td><td>Resultado Verificación</td><td>Motivo Verificación</td><td>Fecha de Creación</td><td>Nro de Pregunta Acertada</td></tr>";
                    if (arrHistoricoNoBiometrica != null && arrHistoricoNoBiometrica.length){
                        for (var i = 0; i < arrHistoricoNoBiometrica.length; i++) {
                            tblStr+="<tr class='ListRow1'>"
                            tblStr+="<td>"+ (i+1) +"</td>"
                            tblStr+="<td>"+ (arrHistoricoNoBiometrica[i].estado||'') +"</td>"
                            tblStr+="<td>"+ (arrHistoricoNoBiometrica[i].motivo||'') +"</td>"
                            tblStr+="<td>"+ (arrHistoricoNoBiometrica[i].fechaCreacion||'') +"</td>"
                            tblStr+="<td>"+ (arrHistoricoNoBiometrica[i].numPreguntaAcertada||'') +"</td>"
                            tblStr+="</tr>"
                        }
                    }else{
                        tblStr+="<tr class='ListRow1'><td>No se encontraron registros</td></tr>"
                    }
                    
                    $("#verifNoBiometrica").html(tblStr);
                }
            }
        });
    }
    
    onload=fxOnLoad;
</script>
<%
    }catch(SessionException se) {
        se.printStackTrace();
        System.out.println("[JP_IDENT_VERIF_CONS_DETAIL][Finalizó la carga]");
    }catch(Exception e) {
        out.println("<script>alert('"+MiUtil.getMessageClean(e.getClass() + " - " + e.getMessage())+"');</script>");
    }
%>
