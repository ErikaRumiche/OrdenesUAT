<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@ page import="oracle.portal.provider.v2.ProviderUser" %>
<%@ page import="pe.com.nextel.service.SessionService"%>
<%@ page import="pe.com.nextel.service.GeneralService" %>
<%@ page import="java.net.URLDecoder" %>
<%@page import="java.util.HashMap" %>
<%@page import="java.util.ArrayList" %>
<%@page import="org.apache.log4j.Logger" %>
<%@page import="pe.com.nextel.util.GenericObject" %>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="pe.com.nextel.service.OrderSearchService" %>
<%@page import="pe.com.nextel.util.MiUtil"%>
<%@page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.exception.SessionException" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/"%>

<%!
	/**
	 * Motivo: JSP para mostrar el listado de Órdenes Internas
	 * Realizado por: Luis Silva
	 * Fecha: 22/10/2010
	 */
	protected static Logger logger = Logger.getLogger(HttpServlet.class);
	private String strMessage;
	protected String formatException(Throwable e) {
		return GenericObject.formatException(e);
	}
%>
<%
try{
	logger.debug("[JP_ORDER_EDIT_INTERNAL_ORDERS][Inicio]");
	String strSession="";
	String strPortletPagePathContext="";
	
	/*Comentar para probar localmente*/
	try{
		PortletRenderRequest pReq = (PortletRenderRequest) request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
	 	strPortletPagePathContext = "/" + StringUtils.substringAfter(
				StringUtils.substringAfter(pReq.getParameter("_page_url"),pReq.getServerName()),"/");
		ProviderUser objetoUsuario1 = pReq.getUser();
		strSession=objetoUsuario1.getPortalSessionId();
		System.out.println("Sesión capturada  JP_ORDER_EDIT_INTERNAL_ORDERS_ShowPage : " + objetoUsuario1.getName() + " - " + strSession );
		System.out.println("Sesión capturada  JP_ORDER_EDIT_INTERNAL_ORDERS_ShowPage : " );
	}catch(Exception e){
		System.out.println("Portler Not Found : " + e.getClass() + " - " + e.getMessage() );
		out.println("Portlet JP_ORDER_EDIT_INTERNAL_ORDERS_ShowPage Not Found");
		return;
	}

	//Descomentar para probar localmente.
	//strSession = "998102396";
	
	/*Parametros*/
	//Indica si la Orden padre (an_parentorderid) es una Orden de activacion. 1 = Sí, 0 = No.
	String strActiveParent = StringUtils.defaultString(request.getParameter("av_isactiveparent"),"0");	
	
	String strParentOrderId = StringUtils.defaultString(request.getParameter("an_nporderid"),"0");
	long lParentOrderId = Long.parseLong(strParentOrderId);
	
	//Paginación.
	String strHdnNumPagina = StringUtils.defaultString(request.getParameter("hdnNumPagina"),"1");
	long lPageSelected = Long.parseLong(strHdnNumPagina);
	String strHdnNumRegistros = StringUtils.defaultString(request.getParameter("hdnNumRegistros"),"15");
	int iRowsByPage = Integer.parseInt(strHdnNumRegistros);	
	
	/*Obtengo la lista de Ordenes Internas*/
	OrderSearchService objOrderSearchService = new OrderSearchService();
	HashMap hshDataMap = objOrderSearchService.getInternalOrderList(lParentOrderId,iRowsByPage,lPageSelected);
	strMessage = (String) hshDataMap.get("strMessage");
	if (strMessage != null){
		throw new Exception(strMessage);
	}

	/*Obtengo la lista de Ordenes Padres*/
	HashMap hshOrdenesPadre = objOrderSearchService.getParentOrderList(lParentOrderId);
	strMessage = (String) hshOrdenesPadre.get("strMessage");
	if (strMessage != null){
		throw new Exception(strMessage);
	}
	
	ArrayList arrParentOrderList = (ArrayList) hshOrdenesPadre.get("arrParentOrderList");
	if(arrParentOrderList == null) {
		System.out.println("arrParentOrderList Es nulo en la prsentación");
	}
	ArrayList arrInternalOrderList = (ArrayList) hshDataMap.get("arrInternalOrderList");
	if(arrInternalOrderList != null) {
		request.setAttribute("arrInternalOrderList", arrInternalOrderList);
	}
	String strNumTotalRegistros = (String) hshDataMap.get("lNumTotalRegistros");
	long lNumTotalRegistros = Long.parseLong(strNumTotalRegistros);
	String strTipoOrden = (String) hshDataMap.get("strTipoOrden");
	
	/*Construyo la Jerarquia de la Orden*/
	HashMap hshOrdenPadreElement = new HashMap();
	String strJerarquiaOrden = "";
	int iParentIndex = 0;
	int iParentOrderListSize = arrParentOrderList.size();
	while(iParentIndex <  iParentOrderListSize){
		hshOrdenPadreElement = (HashMap)arrParentOrderList.get(iParentIndex);
		if (hshOrdenPadreElement.get("nporderid").equals(strParentOrderId)){
			if (StringUtils.isEmpty(strJerarquiaOrden)){
				strJerarquiaOrden = (String)hshOrdenPadreElement.get("nporderid");
			}else{
				strJerarquiaOrden = strJerarquiaOrden + " > " + hshOrdenPadreElement.get("nporderid");	
			}
		}else{
			if (StringUtils.isEmpty(strJerarquiaOrden)){
				strJerarquiaOrden = "<a href='javascript:fxOrderDetail(\"" + hshOrdenPadreElement.get("nporderid") + "\")'>" + hshOrdenPadreElement.get("nporderid") + "</a>";				
			}else{
				strJerarquiaOrden = strJerarquiaOrden + " > <a href='javascript:fxOrderDetail(\"" + hshOrdenPadreElement.get("nporderid") + "\")'>" + hshOrdenPadreElement.get("nporderid") + "</a>";	
			}
		}
		iParentIndex = iParentIndex + 1;
	}
	
%>
<link rel="stylesheet" type="text/css" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
<link rel="stylesheet" type="text/css" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/displaytag.css"></link>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/jQuery-min.js"></script>

<form name="frmdatos" method="POST">

<input type="hidden" name="hdnParentOrderId" value="<%=strParentOrderId%>"/>
<input type="hidden" name="hdnActivateParentOrderId" value=""/>

<table border="0" cellspacing="7" cellpadding="0" width="99%" align="center">
	<tr>
		<td><p class="CellNbg">Jerarquía de la orden: <%=strJerarquiaOrden%></p>
		</td>
	</tr>
	<tr>
		<td>
			<% if (strTipoOrden != null){
        if (strTipoOrden.equals(Constante.TIPO_ORDEN_INTERNA_ACTIVATE) || strTipoOrden.equals(Constante.TIPO_ORDEN_INITIAL)){ %>
				<display:table id="orders" name="arrInternalOrderList" sort="external" partialList="true" size="<%=new Integer(strNumTotalRegistros)%>" requestURI="" class="orders" pagesize="<%=Integer.parseInt(strHdnNumRegistros)%>" style="width: 100%">
					<display:caption>Órdenes de Activación de la orden <%= strParentOrderId%></display:caption>
					<display:setProperty name="basic.empty.showtable" value="false"/>
					<display:setProperty name="paging.banner.some_items_found">
						<span class="spanPaging"> {0} {1} encontrados, mostrando <%=(((lPageSelected-1)*iRowsByPage)+1)+" al "+((lPageSelected*iRowsByPage)>lNumTotalRegistros?lNumTotalRegistros:(lPageSelected*iRowsByPage))%>.</span>
					</display:setProperty>
					<display:column title="Nro" style="white-space: nowrap;" media="html">
						<a href='javascript:fxOrderDetail("<%=((HashMap) orders).get("nporderid")%>")'>
							<img src="<%=Constante.PATH_APPORDER_SERVER%>/images/viewDetail.gif" width="15" height="15" border="0" alt="Detalle de la Orden"></a>
						<%=((HashMap) orders).get("rownum")%>
					</display:column>
					<display:column title="Des." style="white-space: nowrap;" media="html">
						<a href='javascript:fxListDeactivateOrder("<%=((HashMap) orders).get("nporderid")%>")'>
							<img src="<%=Constante.PATH_APPORDER_SERVER%>/images/mas.gif" width="15" height="15" border="0" alt="Ver Órdenes de desactivación">
						</a>
					</display:column>
					<display:column property="nporderid" title="Nro. Orden"/>
					<display:column property="npordercode" title="Nro. Solicitud"/>
					<display:column property="npparentorderid" title="Nro. Orden Padre"/>
          <%if (strTipoOrden.equals(Constante.TIPO_ORDEN_INTERNA_ACTIVATE)){%>
					<display:column property="npactivationdate" title="Fecha de Activación"/>
          <%}%>
					<display:column property="estadoEjecucion" title="Estado De Ejecución"/>
				</display:table>
				<br/>
				<br/>
				<div id="imgProgressCircle" style="text-align: center;width: 100%;"><img alt="" src="<%=Constante.PATH_APPORDER_SERVER%>/images/progresscircle.gif"></div>
				<div id="deactivateOrdersContainer"></div>
			<%}else if (strTipoOrden.equals(Constante.TIPO_ORDEN_INTERNA_DEACTIVATE) || strTipoOrden.equals(Constante.TIPO_ORDEN_FINAL)){ %>
				<div id="deactivateOrders">
					<display:table id="orders" name="arrInternalOrderList" sort="external" partialList="true" size="<%=new Integer(strNumTotalRegistros)%>" requestURI="" class="orders" pagesize="<%=Integer.parseInt(strHdnNumRegistros)%>" style="width: 100%">
						<display:caption>Órdenes de Desactivación de la orden <%= strParentOrderId%></display:caption>
						<display:setProperty name="basic.empty.showtable" value="false"/>
						<display:setProperty name="paging.banner.some_items_found">
							<span class="spanPaging"> {0} {1} encontrados, mostrando <%=(((lPageSelected-1)*iRowsByPage)+1)+" al "+((lPageSelected*iRowsByPage)>lNumTotalRegistros?lNumTotalRegistros:(lPageSelected*iRowsByPage))%>.</span>
						</display:setProperty>
						<display:setProperty name="paging.banner.page.link">
							<a href="javascript:paginarDes({0})" title="Ir a la página {0}">{0}</a>
						</display:setProperty>
						<display:setProperty name="paging.banner.full">
							<span class="pagelinks"> [<a href="javascript:fxPrimeraPaginaDes(1)">Primera</a>/ <a href=javascript:fxAnteriorPaginaDes({5})>Ant.</a>] {0} [ <a href="javascript:fxSiguientePaginaDes({5})">Sig.</a>/ <a href="javascript:fxUltimaPaginaDes({6})">Ultima </a>]</span>
						</display:setProperty>						
						<display:setProperty name="paging.banner.first">
							<span class="pagelinks"> [Primera/Ant.] {0} [ <a href="javascript:fxSiguientePaginaDes({5})">Sig.</a>/ <a href="javascript:fxUltimaPaginaDes({6})">Ultima</a>] </span>						
						</display:setProperty>
						<display:setProperty name="paging.banner.last">
							<span class="pagelinks">[ <a href="javascript:fxPrimeraPaginaDes(1)">Primera</a>/ <a href="javascript:fxAnteriorPaginaDes({5})">Ant.</a>] {0} [Sig./Ultima] </span>
						</display:setProperty>
						<display:column title="Nro" style="white-space: nowrap;" media="html">
							<a href='javascript:fxOrderDetail("<%=((HashMap) orders).get("nporderid")%>")'>
								<img src="<%=Constante.PATH_APPORDER_SERVER%>/images/viewDetail.gif" width="15" height="15" border="0" alt="Detalle de la Orden"></a>
							<%=((HashMap) orders).get("rownum")%>
						</display:column>
						<display:column property="nporderid" title="Nro. Orden"/>
						<display:column property="npordercode" title="Nro. Solicitud"/>
						<display:column property="npparentorderid" title="Nro. Orden Padre"/>
            <% if (strTipoOrden.equals(Constante.TIPO_ORDEN_INTERNA_DEACTIVATE)){%>
						<display:column property="npphone" title="Teléfono"/>
						<display:column property="npnomserv" title="Servicio"/>
						<display:column property="npactivationdate" title="Fecha de Activación"/>
						<display:column property="npdeactivationdate" title="Fecha de Desactivación"/>
            <%}%>
						<display:column property="estadoEjecucion" title="Estado De Ejecución"/>
					</display:table>
				</div>
			<%} 
      } %>		
		</td>
	</tr>
</table>
</form>
<script type="text/javascript">

	$('#imgProgressCircle').hide();

	function fxPreAjaxListDeactivateOrder(){
		$('#deactivateOrdersContainer').hide();
		$('#imgProgressCircle').show();
	}

	function fxPostAjaxListDeactivateOrder(){
		$('#imgProgressCircle').hide();
		$('#deactivateOrdersContainer').show();
	}
	
	//Muestra el detalle de una Orden.
	function fxOrderDetail(strOrderId){
		var url="/portal/pls/portal/ORDERS.PLI_ORDER_REDIRECT?an_nporderid="+strOrderId+"&av_execframe=BOTTOMFRAME";
		parent.bottomFrame.location.replace(url);
	} 
	
	//Muestra las Órdenes de desactivación de una Orden de Activación.
	function fxListDeactivateOrder(strOrderId){
		fxPreAjaxListDeactivateOrder();
		document.frmdatos.hdnActivateParentOrderId.value = strOrderId; 
		var url="<%=strPortletPagePathContext%>&an_nporderid=" + strOrderId + "&av_isactiveparent=1";
		$('#deactivateOrdersContainer').load(url + ' #deactivateOrders', function(){
				fxPostAjaxListDeactivateOrder();
			});
	}
	
	//FUNCIONES PARA LA PAGINACIÓN DE LA PRIMERA TABLA
	//Retorna los parámetros para la paginación
	function fxReturnConcatenado(pagina) {
		strReturnConcatenado = "an_nporderid=" + document.frmdatos.hdnParentOrderId.value + "&" + 
			"hdnNumPagina=" + pagina + "&d-447055-p=" + pagina;
		return strReturnConcatenado;
	}	
	//Ir a una página determinada.
	function paginar(pagina) {
		var url="<%=strPortletPagePathContext%>&" + fxReturnConcatenado(pagina);
		document.location.href = url;
	}
	//Ir a la primera página
	function fxPrimeraPagina(pagina) {
		var url="<%=strPortletPagePathContext%>&" + fxReturnConcatenado(pagina);
		document.location.href = url;
	}		
	//Ir a la anterior página
	function fxAnteriorPagina(pagina) {
		pagina = pagina - 1;
		var url="<%=strPortletPagePathContext%>&" + fxReturnConcatenado(pagina);
		document.location.href = url;
	}		
	//Ir a la siguiente página
	function fxSiguientePagina(pagina) {
		pagina = pagina + 1;
		var url="<%=strPortletPagePathContext%>&" + fxReturnConcatenado(pagina);
		document.location.href = url;
	}		
	//Ir a la última página
	function fxUltimaPagina(pagina) {
		var url="<%=strPortletPagePathContext%>&" + fxReturnConcatenado(pagina);
		document.location.href = url;
	}
	//FUNCIONES PARA LA PAGINACIÓN DE LA SEGUNDA TABLA
	//Retorna los parámetros para la paginación
	function fxReturnConcatenadoDes(pagina) {
		strReturnConcatenado = "an_nporderid=" + document.frmdatos.hdnActivateParentOrderId.value + "&" + 
			"hdnNumPagina=" + pagina + "&d-447055-p=" + pagina;
		return strReturnConcatenado;
	}	
			
	//Ir a una página determinada.
	function paginarDes(pagina) {
		fxPreAjaxListDeactivateOrder();
		var url="<%=strPortletPagePathContext%>&" + fxReturnConcatenadoDes(pagina);
		$('#deactivateOrdersContainer').load(url + ' #deactivateOrders', function(){
				fxPostAjaxListDeactivateOrder();
			});
	}
	//Ir a la primera página
	function fxPrimeraPaginaDes(pagina) {
		fxPreAjaxListDeactivateOrder();
		var url="<%=strPortletPagePathContext%>&" + fxReturnConcatenadoDes(pagina);
		$('#deactivateOrdersContainer').load(url + ' #deactivateOrders', function(){
			fxPostAjaxListDeactivateOrder();
		});
	}		
	//Ir a la anterior página
	function fxAnteriorPaginaDes(pagina) {
		fxPreAjaxListDeactivateOrder();
		pagina = pagina - 1;
		var url="<%=strPortletPagePathContext%>&" + fxReturnConcatenadoDes(pagina);
		$('#deactivateOrdersContainer').load(url + ' #deactivateOrders', function(){
			fxPostAjaxListDeactivateOrder();
		});
	}		
	//Ir a la siguiente página
	function fxSiguientePaginaDes(pagina) {
		fxPreAjaxListDeactivateOrder();
		pagina = pagina + 1;
		var url="<%=strPortletPagePathContext%>&" + fxReturnConcatenadoDes(pagina);
		$('#deactivateOrdersContainer').load(url + ' #deactivateOrders', function(){
			fxPostAjaxListDeactivateOrder();
		});
	}		
	//Ir a la última página
	function fxUltimaPaginaDes(pagina) {
		fxPreAjaxListDeactivateOrder();
		var url="<%=strPortletPagePathContext%>&" + fxReturnConcatenadoDes(pagina);
		$('#deactivateOrdersContainer').load(url + ' #deactivateOrders', function(){
			fxPostAjaxListDeactivateOrder();
		});
	}	
</script>

<%
} catch(SessionException se) {
	out.println("<script>alert('Su sesión ha finalizado. Ingrese nuevamente');</script>");
	String strUrl = "/portal/pls/portal/WEBSALES.NP_USER_AVAILABILITY_PL_PKG.PL_LOGOUT";
	out.print("<script>parent.mainFrame.location.replace('"+strUrl+"');</script>");
} catch(Exception e) {
	String strMessageExceptionGeneralStart = "";
	strMessageExceptionGeneralStart = MiUtil.getMessageClean(e.getMessage());
	logger.error(formatException(e));
	out.println("<script>alert('"+strMessageExceptionGeneralStart+"');</script>");   
}%>
