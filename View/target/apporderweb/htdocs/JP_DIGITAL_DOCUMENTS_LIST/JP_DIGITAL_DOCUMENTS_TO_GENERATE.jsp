<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@ page import="oracle.portal.provider.v2.ProviderSession" %>
<%@ page import="oracle.portal.provider.v2.ProviderUser" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<%@ page import="pe.com.nextel.bean.PortalSessionBean" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="java.util.*" %>
<%@ page import="pe.com.nextel.bean.DocumentoDigitalLinkBean" %>
<%@ page import="pe.com.nextel.service.SessionService"%>
<%@ page import="pe.com.nextel.exception.SessionException" %>
<%@ page import="pe.com.nextel.service.DigitalDocumentService" %>
<%@ page import="org.apache.commons.collections.OrderedIterator" %>
<link rel="stylesheet" type="text/css" href="/websales/Resource/salesweb.css"></link>

<%
try {
    String strRutaContext=request.getContextPath();
    String actionURL_DocDigServlet = strRutaContext+"/digitalDocumentServlet";
    String customerId=request.getParameter("an_customerId");
    String orderId=request.getParameter("an_orderId");
    String incidentId=request.getParameter("an_incidentId");
    String specificationId=request.getParameter("an_specificationId");
    String divisionId=request.getParameter("an_divisionId");
    String tipoEjec=request.getParameter("av_tipoEjec");
    String tipoTrans=request.getParameter("av_tipoTrans");
    String trxId;

    if (orderId==null&&incidentId==null){
        throw new IllegalArgumentException("Error en el envio de parametros");
    }
    int source;
    if(incidentId!=null){
        trxId=incidentId;
        source=Constante.SOURCE_INCIDENT_ID;
    }else{
        trxId= orderId;
        source=Constante.SOURCE_ORDERS_ID;
    }


    String userLogin = "";
    String strSessionId = "";

    try {
        PortletRenderRequest pReq = (PortletRenderRequest) request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
        ProviderUser objetoUsuario = pReq.getUser();
        strSessionId = objetoUsuario.getPortalSessionId();
        System.out.println("[JP_DIGITAL_DOCUMENTS_TO_GENERATE] Sesión capturada: " + objetoUsuario.getName() + " - " + strSessionId );
    } catch(Exception e) {
        System.out.println("[JP_DIGITAL_DOCUMENTS_TO_GENERATE] Portler Not Found: " + e.getClass() + " - " + e.getMessage() );
        out.println("Portlet JP_DIGITAL_DOCUMENTS_TO_GENERATE Not Found");
        return;
    }

    PortalSessionBean portalSessionBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
    if(portalSessionBean==null) {
        System.out.println("[JP_DIGITAL_DOCUMENTS_TO_GENERATE] No se encontró la sesión de Java -> " + strSessionId);
        throw new SessionException("La sesión finalizó");
    }
    userLogin=portalSessionBean.getLogin();

%>
<script type="text/javascript" src="<%=request.getContextPath()%>/websales/Resource/js/jquery-1.10.2.js"></script>

<table class="PortletHeaderColor" cellspacing="0" cellpadding="0" width="100%">
    <tr class="PortletHeaderColor">
        <td class="LeftCurve" valign="top" align="left" width="10" >&nbsp;&nbsp;</td>
        <td class="PortletHeaderColor" align="left" valign="top"> <span class="PortletHeaderText">Documentos a generar</span></td>
        <td align="right" class="RightCurve" width="10" >&nbsp;&nbsp;</td>
    </tr>
</table>
<table id="tableDocuments"border="0" cellspacing="1" cellpadding="0" width="100%" class="RegionBorder">
    <tbody></tbody>
    <tfoot></tfoot>
</table>

<script type="text/javascript">

    $.ajax({
        url:"<%= actionURL_DocDigServlet %>",
        type:"GET",
        cache:false,
        data:{
            "myaction":"getDigitalDocumentsToGenerate",
            "trxId":"<%= trxId%>",
            "source":"<%= source%>",
            "specificationId":"<%= specificationId%>",
            "customerId": "<%= customerId %>",
            "divisionId":"<%= divisionId%>",
            "tipoEjec":"<%= tipoEjec%>",
            "tipoTrans":"<%= tipoTrans%>",
            "userLogin":"<%=userLogin%>"
        },
        dataType:"json",
        success:function(data){
                if(!$.isEmptyObject(data.documentList)){
                $.each(data.documentList, function (index, value) {
                    $('#tableDocuments > tbody').append('<tr><td class="CellContent" >'+value+'</td></tr>');
                });
                }else{
                    $('#tableDocuments > tfoot').append('<tr><td class="CellContent">No hay documentos a mostrar</td></tr>');
                }
        },
        error:function(xhr, status, error) {
            alert(xhr.responseText);
        }
    });




</script>


<%
    }catch(SessionException se) {
        se.printStackTrace();
        System.out.println("[JP_DIGITAL_DOCUMENTS_TO_GENERATE.jsp][Finalizó la sesión]");
    }catch(Exception e) {
        out.println("<script>alert('" + MiUtil.getMessageClean(e.getClass() + " - " + e.getMessage()) + "');</script>");
    }
%>
