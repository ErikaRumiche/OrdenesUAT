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
<%@ page import="pe.com.nextel.service.CustomerService" %>
<%@ page import="pe.com.nextel.bean.CustomerBean" %>
<%@ page import="pe.com.nextel.bean.OrderBean" %>
<%@ page import="pe.com.nextel.service.EditOrderService" %>

<%@ page import="pe.com.entel.esb.message.documentmanage.getdocumentlist.v1.ListaDocumentoItemType" %>
<%@ page import="pe.com.nextel.service.DigitalDocumentService" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="pe.com.nextel.util.GenericObject" %>
<link rel="stylesheet" type="text/css" href="/websales/Resource/salesweb.css"></link>

<%!
    protected static Logger logger = Logger.getLogger(HttpServlet.class);

    protected String formatException(Throwable e) {
        return GenericObject.formatException(e);
    }
%>
<%


    System.out.println("---------------------- INICIO JP_DIGITAL_DOCUMENT_VIEW.jsp-------------------------------------");
    System.out.println("-- Owner    : Alex Ramos Mendoza Mail-to: alex.ramos@Teamsoft.com.pe -------------------------");
    System.out.println("-- Project  : [PRY - 0787] VIDD - \"Verificación de Identidad y Digitalización de Documentos \" -----");
    System.out.println("-----------------------------------------------------------------------------------------------------");



    try {

        System.out.println("Obtener Portlet Request para la creacion del Portlet 1");

        String userLogin = "";
        String strSessionId = "";
        try {
            PortletRenderRequest pReq = (PortletRenderRequest) request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
            ProviderUser objetoUsuario = pReq.getUser();
            strSessionId = objetoUsuario.getPortalSessionId();
            System.out.println("[JP_DIGITAL_DOCUMENTS_VIEW] Session : " + objetoUsuario.getName() + " - " + strSessionId );
        } catch(Exception e) {
            System.out.println("[JP_DIGITAL_DOCUMENTS_VIEW] Portlet Not Found: " + e.getClass() + " - " + e.getMessage() );
            out.println("Portlet JP_DIGITAL_DOCUMENTS_VIEW Not Found");
            return;
        }

        System.out.println("Obtener Session del Usuario");

        PortalSessionBean portalSessionBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
        if(portalSessionBean==null) {
            System.out.println("[JP_DIGITAL_DOCUMENTS_VIEW] No se encontró la sesión de Java -> " + strSessionId);
            throw new SessionException("La sesión finalizó");
        }

        userLogin=portalSessionBean.getLogin();

        System.out.println("Obtener datos de request "+request.getContextPath()+" "+request.getRequestURI());
        String strRutaContext=request.getContextPath();
        String actionURL_DocDigServlet_Digital = strRutaContext+"/viewDigitalDocumentServlet";
        String actionURL_DocDigServlet = strRutaContext+"/digitalDocumentServlet";


        //Obteniendo los datos de la Orden
        HashMap hshOrder=null;
        String strMessage=null;
        OrderBean objOrderBean=null;
        CustomerBean objCustomerBean;

        //Declarando servicio para obtener datos de Customer
        CustomerService customerService = new CustomerService();
        //Declarando servicio para obtener datos de Ordenes
        EditOrderService objOrderService=new EditOrderService();

        //Parametros Ordenes o Incidentes
        String strOrderId=(request.getParameter("an_nporderid")==null?"0":request.getParameter("an_nporderid"));
        long lOrderId0=Long.parseLong(strOrderId);

        String strIncidentId=(request.getParameter("incidentId")==null?"0":request.getParameter("incidentId"));
        if(strIncidentId.equals("0")) {
            strIncidentId=(request.getParameter("npincidentid")==null?"0":request.getParameter("npincidentid"));
        }
        long lincidentId=Long.parseLong(strIncidentId);

        System.out.println("[DIGITAL_DOCUMENT_VIEW.jsp]Obtener datos de request strOrderId="+strOrderId+" strIncidentId="+strIncidentId);

        String trxId;
        String customerId="";
        String strCustomerDocType= "";
        String strCustomerNumDoc = "";
        String sourceName;
        int source;
        String actionURL_DocumentGeneratedListServlet = strRutaContext+"/viewDigitalDocumentServlet";

        if(!strIncidentId.equals("0")) {
            sourceName = Constante.SOURCE_ORIGIN_NAME_INC;
            trxId = strIncidentId;
            strOrderId = "0";
            source = Constante.SOURCE_INCIDENT_ID;
            customerId = (request.getParameter("customerId") == null ? "0" : request.getParameter("customerId"));
        } else {
            sourceName = Constante.SOURCE_ORIGIN_NAME_ORD;
            trxId = strOrderId;
            strIncidentId = "0";
            source = Constante.SOURCE_ORDERS_ID;
            hshOrder=objOrderService.getOrder(lOrderId0);
            strMessage=(String)hshOrder.get("strMessage");

            if (strMessage!=null)
                throw new Exception(strMessage);

            //Obtener el bean de Ordenes
            objOrderBean=(OrderBean)hshOrder.get("objResume");

            //Obtener datos del bean de ordenes y de customer
            customerId=String.valueOf(objOrderBean.getCsbCustomer().getSwCustomerId());
            strCustomerDocType = objOrderBean.getCsbCustomer().getNpTipoDoc();
            strCustomerNumDoc = objOrderBean.getCsbCustomer().getSwRuc();
        }

        if(!MiUtil.getString(customerId).equals("")){
            HashMap hashCustomer;
            hashCustomer=customerService.getCustomerData(Long.parseLong(customerId));
            strMessage = (String)hashCustomer.get("strMessage");
            if ( strMessage != null  )
                throw new Exception(strMessage);
            objCustomerBean = (CustomerBean)hashCustomer.get("objCustomerBean");
            strCustomerDocType = objCustomerBean.getNpTipoDoc();
            strCustomerNumDoc = objCustomerBean.getSwRuc();
        }


%>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/jQuery-min.js"></script>
<script src="https://code.jquery.com/jquery-1.9.0.min.js" integrity="sha256-f6DVw/U4x2+HjgEqw5BZf67Kq/5vudRZuRkljnbF344=" crossorigin="anonymous"></script>
<script src="http://malsup.github.com/jquery.form.js"></script>



<!-- Seccion Adjuntar Documentos  Inicio -->
<div id="dvAttachDocumentSection">
        <table class="PortletHeaderColor" border="0" cellspacing="0" cellpadding="0" width="1100">
            <tr class="PortletHeaderColor">
                <td class="LeftCurve" valign="top" align="left" width="18"></td>
                <td class="PortletHeaderText" align="left" valign="top" width="97%"><%=sourceName%> - Documentos Digitales Adjuntos</td>
                <td rowSpan="1" colSpan="1"><a href="javascript:getVIDDDocumentAttList()">
                    <img width=13 height=13 id=refresh_LstDocumentAttSection style="BORDER-TOP: medium none; BORDER-RIGHT: medium none; BORDER-BOTTOM: medium none; BORDER-LEFT: medium none; DISPLAY: block" alt=Refresh src="/images/prefresh.gif">
                </a></td>
                <td class="RightCurve" valign="top" align="right" width="11"></td>
            </tr>
        </table>
        <table  class="RegionBorder" border="0" width="1100" cellpadding="2" cellspacing="1">
            <tr>
                <td class="RegionHeaderColor" width="100%">
                    <table border="0" id="listDocumentAdjuntos" cellpadding="0" cellspacing="1" width="100%" style="text-align: left;">

                    </table>
                </td>
            </tr>
        </table>
        <br>

</div>


<!-- Seccion Adjuntar Documentos  Fin -->
<script type="text/javascript">

    function descargarDocumentoDigitales(idAceptaDoc,tipoCarga){
        window.open('<%=actionURL_DocDigServlet_Digital%>?myaction=verDocDig&idAceptaDoc='+idAceptaDoc+'&tipoCarga='+tipoCarga+'&userLogin=<%=userLogin%>&trxId=<%=trxId%>&source=<%=source%>','popupDetalle','width=500,height=500,scrollbars=yes,resizable=yes,modal=yes');
    }

</script>

<script type="text/javascript">

    getFlagAttDocList();
    getVIDDDocumentAttList();

    function getFlagAttDocList(){
        strFlagAtt = 0;
        $.ajax({
            url:"<%= actionURL_DocumentGeneratedListServlet%>",
            type:"GET",
            async:false,
            cache:false,
            data:{
                "myaction":"getAttDocListFlag",
                "nporderid" : "<%=strOrderId%>",
                "npincidentid" : "<%=strIncidentId%>"
            },
            dataType:"json",
            success:function(data){
                var message=data.strMessage||'';
                var resultado = data.strResult;
                if(message==''){
                    //Por error de generacion cambia a 2 el flag
                    if(resultado == <%= Constante.FLAG_ATT_DOC_LIST_OK %>){
                        strFlagAtt = 1;
                    }else{
                        strFlagAtt = 0;
                    }
                }else{
                    strFlagAtt = 0;
                }
            },
            error: function(xhr, status, error) {
                alert(xhr.responseText);
            }
        });
    }

    function getVIDDDocumentAttList(){

        var tableDocumentAdjuntos = $('#listDocumentAdjuntos');

        tableDocumentAdjuntos.empty();

        if (strFlagAtt == 1) {
            $.ajax({
                url: "<%= actionURL_DocumentGeneratedListServlet%>",
                type: "GET",
                async: false,
                cache: false,
                data: {
                    "myaction": "getDocChrList",
                    "npOrderId": "<%= strOrderId %>",
                    "npIncidentId": "<%= strIncidentId %>",
                    "userLogin": "<%= userLogin %>",
                    "strCustomerDocType": "<%=strCustomerDocType%>",
                    "strCustomerNumDoc": "<%=strCustomerNumDoc%>"
                },
                dataType: "json",
                success: function (data) {
                    if (data.statusOrden == <%= Constante.COD_OK_DOC_LIST %>) {
                        $.each(data.documentList, function (i, item) {
                            tableDocumentAdjuntos.append(
                                    "<tr class=\"CellContent\">" +
                                    "<td style=\"width:1%\">&nbsp;&nbsp;</td>" +
                                    "<td>" +
                                    "<a href=\"javascript:descargarDocumentoDigitales('" + item.idDocumentoAcepta + "','" + item.tipoCarga + "');\">" +
                                    "<span style=\"font-size:10pt;\">" + item.nombreDocumento + "<span>" +
                                    "</a>" +
                                    "</td>" +
                                    "</tr>");
                        });
                    } else if (data.statusOrden == <%= Constante.COD_WAIT_DOC_LIST%>) {
                        tableDocumentAdjuntos.append("<tr class=\"CellContent\"><td><span style=\"font-size:10pt;\">Los datos Adjuntos estan en proceso ..<span></td></tr>");
                    } else {
                        tableDocumentAdjuntos.append("<tr class=\"CellContent\"><td><span style=\"font-size:10pt;\">No hay documentos adjuntos a mostrar<span></td></tr>");
                    }
                },

                error: function (xhr, status, error) {
                    alert(xhr.responseText);
                }
            });
        } else{
            tableDocumentAdjuntos.append("<tr class=\"CellContent\"><td><span style=\"font-size:10pt;\">No hay documentos adjuntos a mostrar<span></td></tr>");
        }
    }
</script>

<%
    }catch(SessionException se) {
        se.printStackTrace();
        System.out.println("[JP_DIGITAL_DOCUMENTS_VIEW.jsp][Finalizó la sesión]");
    }catch(Exception e) {
        out.println("<script>alert('" + MiUtil.getMessageClean(e.getClass() + " - " + e.getMessage()) + "');</script>");
    }
%>
