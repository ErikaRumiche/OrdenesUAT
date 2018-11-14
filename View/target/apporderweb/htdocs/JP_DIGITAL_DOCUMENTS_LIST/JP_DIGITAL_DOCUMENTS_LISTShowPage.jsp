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
<%@ page import="pe.com.nextel.bean.*" %>
<%@ page import="pe.com.nextel.util.*" %>
<%@ page import="pe.com.nextel.util.GenericObject" %>
<%@ page import="pe.com.nextel.service.GestionDocumentoDigitalesService" %>

<%@ taglib prefix="display" uri="http://displaytag.sf.net/"%>
<link rel="stylesheet" type="text/css" href="/websales/Resource/salesweb.css"></link>

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

<%!
    protected static Logger logger = Logger.getLogger(HttpServlet.class);

    protected String formatException(Throwable e) {
        return GenericObject.formatException(e);
    }
%>
<%
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

        String strRutaContext=request.getContextPath();
        String myAction;
        String attributeNameId;
        Integer  statusOrden=0;
        String actionURL_DocumentGeneratedListServlet = strRutaContext+"/viewDigitalDocumentServlet";
        String actionURL_DigitalDocumentServlet = strRutaContext+"/digitalDocumentServlet";

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

        System.out.println("[DIGITAL_DOCUMENTS_LIST.jsp]Obtener datos de request strOrderId="+strOrderId+" strIncidentId="+strIncidentId);

        String trxId;
        String sourceName;
        String customerId="";
        String strCustomerDocType= "";
        String strCustomerNumDoc = "";
        int source;

        if(!strIncidentId.equals("0")) {
            trxId = strIncidentId;
            sourceName = Constante.SOURCE_ORIGIN_NAME_INC;
            source = Constante.SOURCE_INCIDENT_ID;
            myAction = "getDocumentGenerationInc";
            attributeNameId="incidentId";
            strOrderId = "0";
            customerId = (request.getParameter("customerId") == null ? "0" : request.getParameter("customerId"));
        } else {
            trxId = strOrderId;
            sourceName = Constante.SOURCE_ORIGIN_NAME_ORD;
            source = Constante.SOURCE_ORDERS_ID;
            myAction = "getDocumentGeneration";
            attributeNameId = "orderId";
            strIncidentId = "0";
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

        System.out.println("[JP_DIGITAL_DOCUMENTS_LISTShowPage] orderId: " + strOrderId + " incidentId: " + strIncidentId);

        System.out.println("[JP_DIGITAL_DOCUMENTS_LISTShowPage] strCustomerDocType: " + strCustomerDocType + " strCustomerNumDoc: " + strCustomerNumDoc);

        String actionURL_DocDigServlet = strRutaContext+"/gestionDocumentoDigitalesServlet";
        String actionURL_DocDigServlet_Digital = strRutaContext+"/viewDigitalDocumentServlet";

        String varDatos = request.getParameter("datosDocDig");
        boolean esCompania = false;

        String[] arrayDatos = {};
        if(!StringUtils.isBlank(varDatos)){
            arrayDatos = varDatos.split("-");
            if(Constante.DESC_COMPANIA.equals(arrayDatos[0])){
                esCompania = true;
            }
        }

        List<DocumentoDigitalLinkBean> listDocDig = null;

        if(strIncidentId.equals("0")) {
            if (esCompania) {
                userLogin = "";
                if (arrayDatos.length == 3) {
                    String orderRetail = arrayDatos[1];
                    userLogin = arrayDatos[2];
                    System.out.println("[JP_DIGITAL_DOCUMENTS_LISTShowPage] COMPANIA generatorId: " + orderRetail);
                    System.out.println("[JP_DIGITAL_DOCUMENTS_LISTShowPage] COMPANIA userLogin: " + userLogin);

                    GestionDocumentoDigitalesService service = new GestionDocumentoDigitalesService();
                    Map<String, Object> mapResult = service.listarDocumentosGenerados(orderRetail, userLogin);
                    listDocDig = (ArrayList<DocumentoDigitalLinkBean>) mapResult.get("listDocumentos");
                } else {
                    System.out.println("[JP_DIGITAL_DOCUMENTS_LISTShowPage] COMPANIA datos insuficientes");
                }
            } else {
                strSessionId = "";
                try {
                    PortletRenderRequest pReq = (PortletRenderRequest) request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
                    ProviderUser objetoUsuario = pReq.getUser();
                    strSessionId = objetoUsuario.getPortalSessionId();
                    System.out.println("[JP_DIGITAL_DOCUMENTS_LISTShowPage] Sesión capturada: " + objetoUsuario.getName() + " - " + strSessionId);
                } catch (Exception e) {
                    System.out.println("[JP_DIGITAL_DOCUMENTS_LISTShowPage] Portler Not Found: " + e.getClass() + " - " + e.getMessage());
                    out.println("Portlet JP_DIGITAL_DOCUMENTS_LISTShowPage Not Found");
                    return;
                }

                portalSessionBean = (PortalSessionBean) SessionService.getUserSession(strSessionId);
                if (portalSessionBean == null) {
                    System.out.println("[JP_DIGITAL_DOCUMENTS_LISTShowPage] No se encontró la sesión de Java -> " + strSessionId);
                    throw new SessionException("La sesión finalizó");
                }

                strOrderId=(request.getParameter("an_nporderid")==null?"0":request.getParameter("an_nporderid"));
                System.out.println("[JP_DIGITAL_DOCUMENTS_LISTShowPage] orderId: " + strOrderId);

                long lOrderId = Long.parseLong(strOrderId);

                objOrderService = new EditOrderService();
                hshOrder = (HashMap) objOrderService.getOrder(lOrderId);

                strMessage = (String) hshOrder.get("strMessage");
                if (StringUtils.isBlank(strMessage)) {
                    if (hshOrder.get("objResume") != null) {
                        objOrderBean = (OrderBean) hshOrder.get("objResume");

                        System.out.println("[JP_DIGITAL_DOCUMENTS_LISTShowPage][orderId=" + lOrderId + "] GeneratorType: " + objOrderBean.getNpGeneratorType() + "; GeneratorId: " + objOrderBean.getNpGeneratorId());

                        if (Constante.SOURCER_RETAIL.equals(objOrderBean.getNpGeneratorType())) {
                            String orderRetail = String.valueOf(objOrderBean.getNpGeneratorId());
                            userLogin = portalSessionBean.getLogin();
                            System.out.println("[JP_DIGITAL_DOCUMENTS_LISTShowPage] userLogin: " + userLogin);

                            GestionDocumentoDigitalesService service = new GestionDocumentoDigitalesService();
                            Map<String, Object> mapResult = service.listarDocumentosGenerados(orderRetail, userLogin);
                            listDocDig = (ArrayList<DocumentoDigitalLinkBean>) mapResult.get("listDocumentos");
                        }
                    }
                } else {
                    throw new Exception(strMessage);
                }
            }
        }

%>

<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/jQuery-min.js"></script>
<script src="https://code.jquery.com/jquery-1.9.0.min.js" integrity="sha256-f6DVw/U4x2+HjgEqw5BZf67Kq/5vudRZuRkljnbF344=" crossorigin="anonymous"></script>
<script src="http://malsup.github.com/jquery.form.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script type="text/javascript" src="/web_operacion/js/syncscroll.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/date-picker.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/ua.js"></script>
<script type="text/javascript">
    function descargarDocumento(idAceptaDoc){
        window.open('<%=actionURL_DocDigServlet%>?myaction=verDocDig&idAceptaDoc='+idAceptaDoc+'&userLogin=<%=userLogin%>','popupDetalle','width=500,height=500,scrollbars=yes,resizable=yes,modal=yes');
    }

    function descargarDocumentoDigitales(idAceptaDoc,tipoCarga){
        window.open('<%=actionURL_DocDigServlet_Digital%>?myaction=verDocDig&idAceptaDoc='+idAceptaDoc+'&tipoCarga='+tipoCarga+'&userLogin=<%=userLogin%>&trxId=<%=trxId%>&source=<%=source%>','popupDetalle','width=500,height=500,scrollbars=yes,resizable=yes,modal=yes');
    }
</script>


<div id="dvLstDocumentSection">
    <form method="post" enctype="multipart/form-data">
        <table border="0" cellspacing="0" cellpadding="0" width="1100">
            <tr class="PortletHeaderColor">
                <td class="LeftCurve" valign="top" align="left" width="18"></td>
                    <td class="PortletHeaderText" align="left" valign="top" width="97%"><%=sourceName%> - Documentos Digitales</td>
                <td rowSpan="1" colSpan="1"><a href="javascript:getVIDDDocumentList()">
                    <img width=13 height=13 id=refresh_LstDocumentSection style="BORDER-TOP: medium none; BORDER-RIGHT: medium none; BORDER-BOTTOM: medium none; BORDER-LEFT: medium none; DISPLAY: block" alt=Refresh src="/images/prefresh.gif">
                </a></td>
                <td class="RightCurve" valign="top" align="right" width="11"></td>
            </tr>
        </table>
        <table  class="RegionBorder" border="0" width="1100" cellpadding="2" cellspacing="1">
            <tr>
                <td class="RegionHeaderColor" width="100%">
                    <table id="tableDocumentListRet" border="0" cellpadding="0" cellspacing="1" width="100%" style="text-align: left; display: none;">
                        <%  if(listDocDig != null && listDocDig.size() > 0){
                                for(DocumentoDigitalLinkBean item : listDocDig){%>
                                    <tr class="CellContent">
                                        <td style="width:1%">&nbsp;&nbsp;</td>
                                        <td><a href="javascript:descargarDocumento('<%=item.getIdDocAcepta()%>');"><span style="font-size:10pt;"><%=item.getNombreDoc()%><span></a></td>
                                    </tr>
                                <%}
                            }else{%>
                                <tr class="CellContent">
                                    <td><span style="font-size:10pt;">No hay <%=sourceName%> con documentos digitales a mostrar<span></td>
                                </tr>
                            <%}%>
                    </table>
                    <table id="tableDocumentListVIDD" border="0" cellpadding="0" cellspacing="1" width="100%" style="text-align: left; display: none;">
                    </table>
                </td>
            </tr>
        </table>
    </form>
    <br>
</div>

<script type="text/javascript">
    var strGenerationError;

    $(document).ready(function() {
        getSignatureReason();
        fxShowTableDocument();
    });

    function getSignatureReason(){
        strGenerationError = 0;
        $.ajax({
            url:"<%= actionURL_DigitalDocumentServlet%>",
            type:"GET",
            async:false,
            cache:false,
            data:{
                "myaction":"getSignatureReason",
                "typetrx" : "<%=source%>",
                "origen" : "<%=trxId%>"
            },
            dataType:"json",
            success:function(data){
                var message=data.strMessage||'';
                var resultado = data.strResult;
                if(message==''){
                    //Por error de generacion cambia a 2 el flag
                    if(resultado == <%= Constante.SIGNATURE_REASON_ERROR_GEN %> || resultado == <%= Constante.SIGNATURE_REASON_MIGRATION_PLAN_VALUE %>){
                        strGenerationError = 2;
                    }else{
                        strGenerationError = 0;
                    }
                }else{
                    strGenerationError = 1;
                }
            },
            error: function(xhr, status, error) {
                alert(xhr.responseText);
            }
        });
    }

    function fxShowTableDocument(){

        $.ajax({
            url:"<%= actionURL_DigitalDocumentServlet %>",
            type:"GET",
            async:false,
            cache:false,
            data:{
                "myaction":"<%=myAction%>",
                "<%=attributeNameId%>" : "<%=trxId%>"
            },
            dataType:'json',
            success:function(data){
                var message=data.strMessage||'';
                if(message==''){
                    getVIDDDocumentList();
                }
                else{
                    $('#tableDocumentListRet').show();
                }
            },
            error: function(xhr, status, error) {
                alert(xhr.responseText);
            }
        });
    }

    function getVIDDDocumentList(){
        var tableDocumentList = $('#tableDocumentListVIDD');
        tableDocumentList.empty();
        //En caso el flag sea 2 se muestra el error de generacion
        if(strGenerationError == 2) {
            tableDocumentList.append("<tr class=\"CellContent\"><td><span style=\"font-size:10pt;\">La Generación de Documentos Digitales falló. Debe seguir con el procedimiento de contingencia<span></td></tr>");
            $('#tableDocumentListVIDD').show();
            $('#tableDocumentListRet').hide();
        } else {

            $.ajax({
                url: "<%= actionURL_DocumentGeneratedListServlet%>",
                type: "GET",
                async: false,
                cache: false,
                data: {
                    "myaction": "getDocGenList",
                    "npOrderId": "<%= strOrderId %>",
                    "npIncidentId": "<%= strIncidentId %>",
                    "userLogin": "<%= userLogin %>",
                    "strCustomerDocType":"<%=strCustomerDocType%>",
                    "strCustomerNumDoc":"<%=strCustomerNumDoc%>"
                },
                dataType: "json",
                success: function (data) {
                    if (data.statusOrden == <%= Constante.COD_OK_DOC_LIST %>) {
                        $.each(data.documentList, function (i, item) {
                            tableDocumentList.append(
                            "<tr class=\"CellContent\">" +
                                "<td style=\"width:1%\">&nbsp;&nbsp;</td>" +
                                "<td>" +
                                    "<a href=\"javascript:descargarDocumentoDigitales('"+item.idDocumentoAcepta+"','"+item.tipoCarga+"');\">" +
                                        "<span style=\"font-size:10pt;\">"+item.nombreDocumento+"<span>" +
                                    "</a>" +
                                 "</td>" +
                            "</tr>");
                        });
                        $('#tableDocumentListVIDD').show();
                        $('#tableDocumentListRet').hide();
                    } else if (data.statusOrden == <%= Constante.COD_WAIT_DOC_LIST%>) {
                        tableDocumentList.append("<tr class=\"CellContent\"><td><span style=\"font-size:10pt;\">Los Documentos Generados estan en proceso ..<span></td></tr>");
                        $('#tableDocumentListVIDD').show();
                        $('#tableDocumentListRet').hide();
                    } else {
                        tableDocumentList.append("<tr class=\"CellContent\"><td><span style=\"font-size:10pt;\">No hay Documentos Generados a mostrar<span></td></tr>");
                        $('#tableDocumentListVIDD').hide();
                        $('#tableDocumentListRet').show();
                    }
                },

                error: function (xhr, status, error) {
                    alert(xhr.responseText);
                }
            });
        }
    }
</script>

<%
    } catch(SessionException se) {
        out.println("<script>alert('Su sesión ha finalizado. Ingrese nuevamente');</script>");
        String strUrl = "/portal/pls/portal/WEBSALES.NP_USER_AVAILABILITY_PL_PKG.PL_LOGOUT";
        out.print("<script>parent.mainFrame.location.replace('"+strUrl+"');</script>");
    } catch(Exception e) {
        String strMessageExceptionGeneralStart = MiUtil.getMessageClean(e.getMessage());
        logger.error(formatException(e));
        out.println("<script>alert('"+strMessageExceptionGeneralStart+"');</script>");
    }%>