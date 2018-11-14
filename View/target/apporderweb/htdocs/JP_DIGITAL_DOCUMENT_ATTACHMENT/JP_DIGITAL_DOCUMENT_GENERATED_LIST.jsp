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
        String actionURL_DocAttServlet = strRutaContext+"/documentAttachmentServlet";
        String statusId=request.getParameter("an_statusId");
        String orderId=request.getParameter("an_orderId");
        String incidentId=request.getParameter("an_incidentId");
        String specificationId=request.getParameter("an_specificationId");
        String trxId;


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
            System.out.println("[JP_DIGITAL_DOCUMENTS_ATTACHMENT] Session : " + objetoUsuario.getName() + " - " + strSessionId );
        } catch(Exception e) {
            System.out.println("[JP_DIGITAL_DOCUMENTS_ATTACHMENT] Portlet Not Found: " + e.getClass() + " - " + e.getMessage() );
            out.println("Portlet JP_DIGITAL_DOCUMENTS_ATTACHMENT Not Found");
            return;
        }

        PortalSessionBean portalSessionBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
        if(portalSessionBean==null) {
            System.out.println("[JP_DIGITAL_DOCUMENTS_TO_GENERATE] No se encontró la sesión de Java -> " + strSessionId);
            throw new SessionException("La sesión finalizó");
        }
        userLogin=portalSessionBean.getLogin();

%>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/jQuery-min.js"></script>


<!-- Seccion Lista de Documentos digitalizados Inicio -->
<div id="dvDigitalDocumentListSection">
    <table class="PortletHeaderColor" border="0" cellspacing="0" cellpadding="0" width="50%">
        <tr class="PortletHeaderColor">
            <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"></td>
            <td class="SubSectionTitle" align="left" valign="top">Documentos Digitales</td>
            <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"></td>
        </tr>
    </table>
    <table  class="RegionBorder" border="0" width="50%" cellpadding="2" cellspacing="1">

        <tr>
            <td class="RegionHeaderColor" width="100%">
                <table id="tableDigitalizedDocuments" border="0" cellspacing="1" cellpadding="0" width="100%" class="RegionBorder">
                    <tbody>LISTA DE DOCUMENTOS DIGITALIZADOS</tbody>
                    <tfoot></tfoot>
                </table>
            </td>
        </tr>
    </table>
    <br>
</div>

<!-- Seccion Lista de Documentos digitalizados Fin -->





                    <%
    }catch(SessionException se) {
        se.printStackTrace();
        System.out.println("[JP_DIGITAL_DOCUMENTS_ATTACHMENT.jsp][Finalizó la sesión]");
    }catch(Exception e) {
        out.println("<script>alert('" + MiUtil.getMessageClean(e.getClass() + " - " + e.getMessage()) + "');</script>");
    }
%>
