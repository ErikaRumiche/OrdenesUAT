<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@ page import="oracle.portal.provider.v2.ProviderSession" %>
<%@ page import="oracle.portal.provider.v2.ProviderUser" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<%@ page import="pe.com.nextel.bean.PortalSessionBean" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.bean.*" %>
<%@ page import="pe.com.nextel.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="pe.com.nextel.bean.OrdenDocDigitalBean" %>
<%@ page import="pe.com.nextel.service.SessionService"%>
<%@ page import="pe.com.nextel.service.GestionDocumentoDigitalesService" %>
<%@ page import="pe.com.nextel.exception.SessionException" %>
<%@ page import="pe.com.nextel.util.GenericObject" %>

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
    String strSessionId = "";
    try {
        PortletRenderRequest pReq = (PortletRenderRequest) request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
        ProviderUser objetoUsuario = pReq.getUser();
        strSessionId = objetoUsuario.getPortalSessionId();
        System.out.println("[JP_ORDERS_DIGITAL_DOCUMENTS_LISTShowPage] Sesión capturada: " + objetoUsuario.getName() + " - " + strSessionId );
    } catch(Exception e) {
        System.out.println("[JP_ORDERS_DIGITAL_DOCUMENTS_LISTShowPage] Portler Not Found: " + e.getClass() + " - " + e.getMessage() );
        out.println("Portlet JP_ORDERS_DIGITAL_DOCUMENTS_LISTShowPage Not Found");
        return;
    }

    PortalSessionBean portalSessionBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
    if(portalSessionBean==null) {
        System.out.println("[JP_ORDERS_DIGITAL_DOCUMENTS_LISTShowPage] No se encontró la sesión de Java -> " + strSessionId);
        throw new SessionException("La sesión finalizó");
    }

    String userLogin = portalSessionBean.getLogin();
    System.out.println("[JP_ORDERS_DIGITAL_DOCUMENTS_LISTShowPage] userLogin: " + userLogin);

    String strRutaContext=request.getContextPath();
    String actionURL_DocDigServlet = strRutaContext+"/gestionDocumentoDigitalesServlet";

	String customerId = StringUtils.defaultString(request.getParameter("customerId"));
    System.out.println("[JP_ORDERS_DIGITAL_DOCUMENTS_LISTShowPage] customerId: "+customerId);

    GestionDocumentoDigitalesService service = new GestionDocumentoDigitalesService();

    Map<String, Object> mapResult = service.listarOrdenesDocDigitales(customerId);

    List<OrdenDocDigitalBean> listOrdenes = (ArrayList<OrdenDocDigitalBean>)mapResult.get("listOrdenes");
%>

<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script type="text/javascript" src="/web_operacion/js/syncscroll.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/date-picker.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/ua.js"></script>
<script type="text/javascript">
    function fxListarDocumentosDig(generatorId){
        var vform = document.frmdatos;
        var datosDocDig = "<%=Constante.DESC_COMPANIA%>-"+generatorId+"-<%=userLogin%>";
        vform.target = "bottomFrame";
        vform.action = "<%=actionURL_DocDigServlet%>?myaction=loadPopupDocDig&datosDocDig="+datosDocDig;
        vform.submit();
    }
</script>

<form name="frmdatos" method="POST">
    <div style="width: 60%; overflow-y: auto; height:500px; ">
        <table id="orders" class="orders" style="width: 100%">
            <%
                if(listOrdenes != null && listOrdenes.size() > 0){
            %>
            <thead>
                <tr>
                    <th>Pedido id</th>
                    <th>Categoria</th>
                    <th>SubCategoria</th>
                    <th>Tipo de Pedido</th>
                    <th>Usuario Creación</th>
                    <th>Fecha Creación</th>
                </tr>
            </thead>
            <tbody>
            <%
                    for(OrdenDocDigitalBean item : listOrdenes){
            %>
                <tr class="CellContent">
                    <td><a href="javascript:fxListarDocumentosDig('<%=item.getGeneratorId()%>');"><span><%=item.getOrderId()%></span></a></td>
                    <td><span><%=item.getType()%></span></td>
                    <td><span><%=item.getSpecification()%></span></td>
                    <td><span><%=item.getParameterName()%></span></td>
                    <td><span><%=item.getCreateBy()%></span></td>
                    <td><span><%=MiUtil.getDate(item.getOrderDate(), "dd/MM/yyyy HH:mm:ss")%></span></td>
                </tr>
            <%
                    }
            %>
            </tbody>
            <%
                }else{
            %>
            <tr class="CellContent">
                <td><span style="font-size:10pt;">No hay Ordenes con documentos digitales a mostrar<span></td>
            </tr>
            <%
                }
            %>
        </table>
    </div>
</form>
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