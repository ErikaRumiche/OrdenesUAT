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
<%
    try {
        String strSessionId1 = "";
        try{
            PortletRenderRequest pReq = (PortletRenderRequest) request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
            ProviderUser objetoUsuario1 = pReq.getUser();
            strSessionId1=objetoUsuario1.getPortalSessionId();
            System.out.println("Sesión capturada JP_IDENT_VERIF_CONS: " + objetoUsuario1.getName() + " - " + strSessionId1 );
        }catch(Exception e){
            System.out.println("Portler Not Found : " + e.getClass() + " - " + e.getMessage() );
            out.println("Portlet JP_IDENT_VERIF_CONS Not Found");
            return;
        }

        System.out.println("Sesión capturada después del request : " + strSessionId1 );
        PortalSessionBean portalSessionBean = (PortalSessionBean) SessionService.getUserSession(strSessionId1);
        if(portalSessionBean==null) {
            System.out.println("No se encontró la sesión de Java ->" + strSessionId1);
            throw new SessionException("La sesión finalizó");
        }

        Calendar calendarIni = Calendar.getInstance();
        calendarIni.add(Calendar.DATE, -30);
        Calendar calendarFin = Calendar.getInstance();

%>
<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/date-picker.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/jQuery-min.js"></script>

<style>
    .ListRow0{ font-family: Arial, Helvetica; font-size: 8pt; color: #000000; font-weight: normal;  background-color: #E8E4B7;}
    .ListRow1{ font-family: Arial, Helvetica; font-size: 8pt; color: #000000; font-weight: normal;  background-color: #F5F5EB;}
</style>
<form name="formdatos">
    <table border="0" cellspacing="7" cellpadding="0" width="99%" align="center">
        <tr>
            <td>
                <br/>
                <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
                    <tr>
                        <td class="SectionTitleLeftCurve" width="10">&nbsp;&nbsp;</td>
                        <td class="SectionTitle">&nbsp;&nbsp;Ingrese la información del cliente</td>
                        <td class="SectionTitleRightCurve" width="8">&nbsp;&nbsp;</td>
                    </tr>
                </table>
                <table border="0" cellspacing="1" cellpadding="1" align="center" class="RegionBorder" width="100%">
                    <tr>
                        <td class="CellLabel" width="15%"><span style="color:red">*</span>Tipo de Documento:</td>
                        <td class="CellContent" width="35%">
                            <select name="cmbDocType" id ="cmbDocType" onchange="fxActualizarMaxLength()">
                            </select>
                        </td>
                        <td class="CellLabel" width="15%"><span style="color:red">*</span>Número de Documento:</td>
                        <td class="CellContent" width="35%">
                            <input type="text" name="txtDocNumber" id="txtDocNumber" size="20"/><span id="spnMax"><span id="maxLength"></span> caracteres máximo.</span>
                        </td>
                    </tr>
                    <tr>
                        <td class="CellLabel" width="15%"><span style="color:red">*</span>Fecha Desde:</td>
                        <td class="CellContent" width="35%">
                            <input type="text" name="txtFromDate" id="txtFromDate" size="10" maxlength="10" value="<%=MiUtil.getDate(calendarIni.getTime(), "dd/MM/yyyy")%>">
                            <a href="javascript:show_calendar('formdatos.txtFromDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Fecha desde';return true;" onmouseout="window.status='';return true;">
                                <img src="<%=Constante.PATH_APPORDER_SERVER%>/images/show-calendar.gif" width="24" height="22" border="0"></a>&nbsp;<i>DD/MM/YYYY</i>
                        </td>
                        <td class="CellLabel" width="15%"><span style="color:red">*</span>Fecha Hasta:</td>
                        <td class="CellContent" width="35%">
                            <input type="text" name="txtToDate" id="txtToDate" size="10" maxlength="10" value="<%=MiUtil.getDate(calendarFin.getTime(), "dd/MM/yyyy")%>">
                            <a href="javascript:show_calendar('formdatos.txtToDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Fecha desde';return true;" onmouseout="window.status='';return true;">
                                <img src="<%=Constante.PATH_APPORDER_SERVER%>/images/show-calendar.gif" width="24" height="22" border="0"></a>&nbsp;<i>DD/MM/YYYY</i>
                        </td>
                    </tr>
                </table>
                <table align="center">
                    <tr>
                        <td align="center">
                            <input type="button" name="btnBuscar" value="Consultar" onclick="fxSearchVerification();"/>
                        </td>
                    </tr>
                </table>
                <br/>
                <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
                    <tr>
                        <td class="SectionTitleLeftCurve" width="10">&nbsp;&nbsp;</td>
                        <td class="SectionTitle">&nbsp;&nbsp;Historial de Verificación de Identidad Aislada</td>
                        <td class="SectionTitleRightCurve" width="8">&nbsp;&nbsp;</td>
                    </tr>
                </table>
                <table id ="tblResults" border="0" cellspacing="1" cellpadding="1" align="center" class="RegionBorder" width="100%">

                </table>
            </td>
        </tr>
    </table>
    <br/>
</form>
<script type="text/javascript">
    function fxSearchVerification(){
        var tipodoc=$("#cmbDocType").val();
        var numdoc=$("#txtDocNumber").val();
        var fecini=$("#txtFromDate").val();
        var fecfin=$("#txtToDate").val();
        var server="<%=request.getContextPath()%>/biometricaDetailServlet";
        var params = 'myaction=listarVerificaciones&tipoDoc='+tipodoc+'&numDoc='+numdoc+'&fecIni='+fecini+'&fecFin='+fecfin;

        if (!tipodoc.length) {
            alert("Seleccione un Tipo de Documento");
            return;
        }
        
        if (!numdoc.length) {
            alert("El campo Número de Documento es obligatorio");
            return;
        }else{
            var maxlength = $("#cmbDocType option:selected").attr("length");
            if (maxlength.length && numdoc.length > maxlength){
                alert("El campo excedió en número de caracteres permitido");
                return; 
            }
        }
        
        if (!fecini.length) {
            alert("El campo Fecha Desde es obligatorio");
            return;
        }else if( !fxValidateDate(fecini)){
            alert("Formato de Fecha Desde incorrecto")
            return;
        }
        
        if (!fecfin.length) {
            alert("El campo Fecha Hasta es obligatorio");
            return;
        }else if ( !fxValidateDate(fecfin)){
            alert("Formato de Fecha Fin incorrecto");
        }

        var dateini= fxGetRealDate(fecini);
        var dateend= fxGetRealDate(fecfin);
        var timediff = Math.ceil((dateend - dateini)/(1000*3600*24));
        
        if ( timediff < 0 ){
            alert("La fecha de inicio no puede ser mayor que la fecha de fin");
            return;
        }else if( timediff > 30){
            alert("El rango de fechas excedió el número de días permitido (30 días).")
            return;
        }
        
        $.ajax({
            url: server,
            data: params,
            type: "GET",
            cache: false,
            success:function(data){
                $("#tblResults").html(data);
            }
        });
    }

    function isNonEmptyNumber (str) {
        if(!str.length) return false;
        var num = +str;
        return isFinite(num) && !isNaN(num);
    }
    
    function fxCargarCombo(){
        var server="<%=request.getContextPath()%>/biometricaDetailServlet";
        var params = 'myaction=construirComboTipoDoc';
        $.ajax({
            url: server,
            data: params,
            type: "GET",
            success:function(data){
                $("#cmbDocType").html(data);
                fxActualizarMaxLength()
            }
        });
    }

    function fxActualizarMaxLength(){
        var maxlength = $("#cmbDocType option:selected").attr("length");
        if ( maxlength!=null && maxlength.length ){
            $("#spnMax").show();
            $("#maxLength").html(maxlength);
        }else{
            $("#spnMax").hide();
        }
    }

    function fxValidateDate(str){
        str = str.split("/");
        if (str.length != 3) return false;
        str = [str[1],str[0],str[2]].join("/");
        var dates = new Date(str);
        if ( isNaN(dates)) return false;

        var mm = (dates.getMonth() + 1).toString()
        var dd = dates.getDate().toString();
        var concat= [
            mm.length===2 ? '' : '0', mm,
            dd.length===2 ? '' : '0', dd,
            dates.getFullYear()].join('');
        str = str.split("/").join("");
        return concat === str;
    }
    
    function fxGetRealDate(str){
        str = str.split("/");
        return new Date([str[1],str[0],str[2]].join("/"));
    }

    $(".detalleLink").live("click",function(event){
        event.preventDefault();
        location.href(event.target);
    });
    
    $("#spnMax").hide();
    onload=fxCargarCombo;
</script>
<%
    }catch(SessionException se) {
        se.printStackTrace();
        System.out.println("[JP_IDENT_VERIF_CONS][Finalizó la sesión]");
    }catch(Exception e) {
        out.println("<script>alert('"+MiUtil.getMessageClean(e.getClass() + " - " + e.getMessage())+"');</script>");
    }
%>
