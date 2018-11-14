<%System.out.println("[IdentityVerificationDetail.jsp][Inicio]");%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@ page import="oracle.portal.provider.v2.ProviderUser" %>
<%@ page import="pe.com.nextel.service.SessionService"%>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="pe.com.nextel.bean.PortalSessionBean" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.exception.SessionException" %>
<%@ page import="pe.com.nextel.service.IdentificationService"%>
<%@ page import="pe.com.nextel.bean.IdentityVerificationDetailBean" %>
<%@ page import="pe.com.nextel.bean.BiometricValidationBean" %>
<%@ page import="pe.com.nextel.bean.NoBiometricValidationBean" %>
<%@ page import="pe.com.nextel.bean.ExternalValidationBean" %>
<%@ page import="java.util.List" %>
<%
    Constante Constante = new Constante();
    try {
        String strSessionId = "";

        try{
            PortletRenderRequest pReq = (PortletRenderRequest) request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
            ProviderUser objetoUsuario = pReq.getUser();
            strSessionId=objetoUsuario.getPortalSessionId();
            System.out.println("Sesión capturada  : " + objetoUsuario.getName() + " - " + strSessionId );

        }catch(Exception e){
            System.out.println("Portlet Not Found : " + e.getClass() + " - " + e.getMessage() );
            out.println("Portlet JP_IDENT_VERIF_DETAIL Not Found");
            return;
        }

        System.out.println("Sesión capturada después del request : " + strSessionId );
        PortalSessionBean portalSessionBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
        if(portalSessionBean==null) {
            System.out.println("No se encontró la sesión de Java ->" + strSessionId);
            throw new SessionException("La sesión finalizó");
        }

        IdentificationService identificationService = new IdentificationService();

        //Parametros
        String strOrderId=(request.getParameter("an_nporderid")==null?"0":request.getParameter("an_nporderid"));
        long lOrderId=Long.parseLong(strOrderId);

        System.out.println("[Paul]lOrderId = " + lOrderId);

        IdentityVerificationDetailBean identityVerificationDetailBean = null;
        identityVerificationDetailBean = identificationService.getIdentityVerificationDetail(lOrderId);

        List<BiometricValidationBean> listBiometricValidation = identityVerificationDetailBean.getListBiometricValidation();
        List<NoBiometricValidationBean> listNoBiometricValidation = identityVerificationDetailBean.getListNoBiometricValidation();
        List<ExternalValidationBean> listExternalValidation = identityVerificationDetailBean.getListExternalValidation();

        Integer num;

        if(listBiometricValidation != null) {
            num = 0;
            for(BiometricValidationBean biometricValidationBean: listBiometricValidation) {
                biometricValidationBean.setVerificationNumber(++num);
            }
            request.setAttribute("listBiometricValidation", listBiometricValidation);
        }

        if(listNoBiometricValidation != null) {
            num = 0;
            for(NoBiometricValidationBean noBiometricValidationBean: listNoBiometricValidation) {
                noBiometricValidationBean.setVerificationNumber(++num);
            }
            request.setAttribute("listNoBiometricValidation", listNoBiometricValidation);
        }
        
        if(listExternalValidation != null) {
            num = 0;
            for(ExternalValidationBean externalValidationBean: listExternalValidation) {
            	externalValidationBean.setVerificationNumber(++num);
            }
            request.setAttribute("listExternalValidation", listExternalValidation);
        }
%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net/"%>

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
<form name="frmdatos">

    <table border="0" cellpadding="5" width="100%">

        <tr>
            <td colspan="4" style="width: 100%;">
            </td>
        </tr>

        <tr class="ListRow1">
            <td CLASS="CellLabel" style="width: 25%;"><strong>Nombres:</strong></td>
            <td style="width: 25%;"><label><%=MiUtil.getString(identityVerificationDetailBean.getNombres())%></label></td>

            <td CLASS="CellLabel" style="width: 25%;"><strong>Apellidos:</strong></td>
            <td style="width: 25%;"><label><%=MiUtil.getString(identityVerificationDetailBean.getApellidos())%></label></td>
        </tr>

        <tr class="ListRow1">
            <td CLASS="CellLabel" style="width: 25%;"><strong>Tipo de documento:</strong></td>
            <td style="width: 25%;"><label><%=MiUtil.getString(identityVerificationDetailBean.getTipoDocumento())%></label></td>

            <td CLASS="CellLabel" style="width: 25%;"><strong>Nro. de documento:</strong></td>
            <td style="width: 25%;"><label><%=MiUtil.getString(identityVerificationDetailBean.getNroDocumento())%></label></td>
        </tr>

        <tr class="ListRow1">
            <td CLASS="CellLabel" style="width: 25%;"><strong>Tipo de Verificación exitosa:</strong></td>
            <td style="width: 25%;"><label><%=MiUtil.getString(identityVerificationDetailBean.getTipoVerificacionExitosa())%></label></td>

            <td CLASS="CellLabel" style="width: 25%;"><strong>Registrado por:</strong></td>
            <td style="width: 25%;"><label><%=MiUtil.getString(identityVerificationDetailBean.getRegistradoPor())%></label></td>
        </tr>

        <tr class="ListRow1">
            <td CLASS="CellLabel" style="width: 25%;"><strong>Modificado por:</strong></td>
            <td style="width: 25%;"><label><%=MiUtil.getString(identityVerificationDetailBean.getModificadoPor())%></label></td>

            <td colspan="2" style="width: 50%;">
            </td>
        </tr>
    </table>

    <table border="0" cellspacing="7" cellpadding="0" width="100%">
        <tr><td class="SectionTitle" colspan="5" align="center">Historial de validaci&oacute;n biom&eacute;trica</td></tr>
        <tr><td>
        <display:table id="biometricValidation" name="listBiometricValidation" class="orders" export="false" pagesize="10" style="text-align: center; width: 100%;">
            <display:column property="verificationNumber" title="Id" style="text-align:center; width:5%;" />
            <display:column property="verificationResult" title="Resultado de verificaci&oacute;n" style="text-align:center; width:30%;" />
            <display:column property="source" title="Aplicaci&oacute;n" style="text-align:center; width:15%;" />
            <display:column property="verificationDate" title="Fecha de verificaci&oacute;n" format="{0,date,dd/MM/yyyy hh:mm:ss a}" style="text-align:center; width:20%;" />
            <display:column property="verificationMotive" title="Motivos de verificaci&oacute;n" style="text-align:center; width:30%;" />
        </display:table>
    </td></tr></table>

    <table border="0" cellspacing="7" cellpadding="0" width="100%">
        <tr><td class="SectionTitle" colspan="6" align="center">Historial de validaci&oacute;n no biom&eacute;trica</td></tr>
        <tr><td>
        <display:table id="noBiometricValidation" name="listNoBiometricValidation" class="orders" export="false" pagesize="10" style="text-align: center; width: 100%;">
            <display:column property="verificationNumber" title="Id" style="text-align:center; width:5%;" />
            <display:column property="verificationResult" title="Resultado de verificaci&oacute;n" style="text-align:center; width:25%;" />
            <display:column property="source" title="Aplicaci&oacute;n" style="text-align:center; width:10%;" />
            <display:column property="verificationDate" title="Fecha de verificaci&oacute;n" format="{0,date,dd/MM/yyyy hh:mm:ss a}" style="text-align:center; width:15%;" />
            <display:column property="nroPreguntaAcertada" title="Nro. de pregunta acertada" style="text-align:center; width:15%;" />
            <display:column property="verificationMotive" title="Motivos de verificaci&oacute;n" style="text-align:center; width:30%;" />
        </display:table>
    </td></tr></table>
    
    <table border="0" cellspacing="7" cellpadding="0" width="100%">
        <tr><td class="SectionTitle" colspan="6" align="center">Historial de verificación externa</td></tr>
        <tr><td>
        <display:table id="externalValidation" name="listExternalValidation" class="orders" export="false" pagesize="10" style="text-align: center; width: 100%;">
            <display:column property="verificationNumber" title="Id" style="text-align:center; width:5%;" />
            <display:column property="codeVerificationExt" title="Id Verificaci&oacute;n Externa" style="text-align:center; width:15%;" />
            <display:column property="verificationResult" title="Resultado de verificaci&oacute;n" style="text-align:center; width:10%;" />
            <display:column property="source" title="Aplicaci&oacute;n" style="text-align:center; width:10%;" />
            <display:column property="verificationDate" title="Fecha de verificaci&oacute;n" format="{0,date,dd/MM/yyyy hh:mm:ss a}" style="text-align:center; width:15%;" />
            <display:column property="documentNumber" title="Documento" style="text-align:center; width:10%;" />
            <display:column property="fullName" title="Nombre Persona" style="text-align:center; width:23%;" />
            <display:column property="provider" title="Proveedor" style="text-align:center; width:12%;" />
        </display:table>
    </td></tr></table>

</form>
<%
    }catch(SessionException se) {
        se.printStackTrace();
        System.out.println("[IdentityVerificationDetail.jsp][Finalizó la sesión]");
    }catch(Exception e) {
        out.println("<script>alert('" + MiUtil.getMessageClean(e.getClass() + " - " + e.getMessage()) + "');</script>");
    }
%>