<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@ page import="oracle.portal.provider.v2.ProviderUser" %>
<%@page import="oracle.portal.provider.v2.ProviderSession" %>
<%@ page import="java.lang.reflect.Field"%>
<%@page import="java.util.HashMap" %>
<%@page import="java.util.ArrayList" %>
<%@page import="org.apache.log4j.Logger" %>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="pe.com.nextel.service.OrderSearchService" %>
<%@page import="pe.com.nextel.form.OrderSearchForm" %>
<%@page import="pe.com.nextel.util.GenericObject" %>
<%@page import="pe.com.nextel.util.Constante" %>
<%@page import="pe.com.nextel.util.MiUtil"%>
<%@ page import="pe.com.nextel.exception.SessionException" %>
<%@ page import="pe.com.nextel.bean.PortalSessionBean" %>
<%@page import="pe.com.nextel.service.SessionService"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net/"%>
<%!
protected static Logger logger = Logger.getLogger(HttpServlet.class);

protected String formatException(Throwable e) {
	return GenericObject.formatException(e);
}
private static String newLine = "<br>";
private static String tabSpace = "&nbsp;&nbsp;&nbsp;&nbsp;";
%>
<%
logger.debug("Al inicio de todo");
try {
  logger.debug("[JP_ORDER_LIST][Inicio]");
  String strSessionId1 = "";

  try {
      PortletRenderRequest pReq = (PortletRenderRequest) request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
      ProviderUser objetoUsuario1 = pReq.getUser();
      strSessionId1=objetoUsuario1.getPortalSessionId();
      System.out.println("Sesión capturada  : " + objetoUsuario1.getName() + " - " + strSessionId1 );
  } catch(Exception e) {
      System.out.println("Portler Not Found : " + e.getClass() + " - " + e.getMessage() );
      out.println("Portlet JP_ORDER_SEARCHShowPage Not Found");
      return;
  }

    //EFLORES PM0010274
    System.out.println("Sesión capturada después del resuest : " + strSessionId1);
    PortalSessionBean portalSessionBean1 = (PortalSessionBean)SessionService.getUserSession(strSessionId1);
    if(portalSessionBean1==null) {
        System.out.println("No se encontró la sesión de Java ->" + strSessionId1);
        throw new SessionException("La sesión finalizó");
    }
    int iUserId1  = portalSessionBean1.getUserid();
  //strSessionId1 = "998102396";
   
    String strRutaContext=request.getContextPath();
    String actionURL_GeneralServlet = strRutaContext+"/generalServlet";

	OrderSearchService objOrderSearchService = new OrderSearchService();
	OrderSearchForm objOrderSearchForm = new OrderSearchForm();
  objOrderSearchForm = (OrderSearchForm) objOrderSearchForm.populateForm(request);
  objOrderSearchForm.setHdnNumPagina(StringUtils.defaultString(request.getParameter("hdnNumPagina"),"1"));
  if(logger.isDebugEnabled())
    logger.debug("objOrderSearchForm:::"+objOrderSearchForm);
   System.out.println("[JP_ORDER_LIST][objOrderSearchForm]"+objOrderSearchForm);
   
   
System.out.println("****Antes");
	System.out.println("Listado SalesStruct Jerarquia="+objOrderSearchForm.getHdnSalesstructid());
	System.out.println("Listado Providergrpid Jerarquia="+objOrderSearchForm.getHdnProviderid());
	System.out.println("****");


  if(objOrderSearchForm.getHdnSalesstructid()!=null){
		session.putValue("salesBusquedaId", objOrderSearchForm.getHdnSalesstructid());
    		session.putValue("strNombreUnidad", objOrderSearchForm.getStrUnidadJerarquica());
	}else{
		objOrderSearchForm.setHdnSalesstructid(session.getValue("salesBusquedaId")!=null?session.getValue("salesBusquedaId")+"":null);
    		objOrderSearchForm.setStrUnidadJerarquica(session.getValue("strNombreUnidad")+"");
	}
  	
  	if(objOrderSearchForm.getHdnProviderid()!=null){
		session.putValue("providergrpId", objOrderSearchForm.getHdnProviderid());
    		session.putValue("strNombreRep", objOrderSearchForm.getStrRepresentante());
	}else{
		objOrderSearchForm.setHdnProviderid(session.getValue("providergrpId")!=null?session.getValue("providergrpId")+"":null);
    		objOrderSearchForm.setStrRepresentante(session.getValue("strNombreRep")+"");
	}


	System.out.println("Listado SalesStruct Jerarquia="+objOrderSearchForm.getHdnSalesstructid());
	System.out.println("Listado Providergrpid Jerarquia="+objOrderSearchForm.getHdnProviderid());

    System.out.println("cmbSegmentoCompania = "+objOrderSearchForm.getCmbSegmentoCompania());

    String strOptionsSelected = (String) objOrderSearchService.buildOptionsSelected(objOrderSearchForm);
        
         strOptionsSelected = new String(strOptionsSelected.toString().getBytes("ISO-8859-1"),"UTF8");
        
   System.out.println("[JP_ORDER_LIST][Fin de obtener objOrderSearchForm]");
   
	String strHdnNumPagina = StringUtils.defaultString(request.getParameter("hdnNumPagina"),"1");   
	long lPageSelected = Long.parseLong(strHdnNumPagina);
	String strHdnNumRegistros = StringUtils.defaultString(request.getParameter("hdnNumRegistros"),"15");
	int iRowsByPage = Integer.parseInt(strHdnNumRegistros);
	objOrderSearchForm.setHdnNumPagina(strHdnNumPagina);
   
	HashMap hshDataMap = objOrderSearchService.getOrdersList(objOrderSearchForm);
	if(logger.isDebugEnabled())
		logger.debug("Obtuvo la lista relativa a la busqueda");
	ArrayList arrOrderList = (ArrayList) hshDataMap.get("arrOrderList");
	String strNumTotalRegistros = (String) hshDataMap.get("lNumTotalRegistros");
	long lNumTotalRegistros = Long.parseLong(strNumTotalRegistros);
   //System.out.println("Tamaño del ArrayList: " + arrOrderList.size());
	if(arrOrderList != null) {
		request.setAttribute("arrOrderList", arrOrderList);
	}
	int iFlag = objOrderSearchForm.getIFlag();	
%>
<link rel="stylesheet" type="text/css" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
<link rel="stylesheet" type="text/css" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/displaytag.css"></link>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js" charset="ISO-8859-1"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js" charset="ISO-8859-1"></script>
<script type="text/javascript" src="/websales/Resource/jQuery-min.js"></script>
<form name="frmdatos" action="<%=request.getContextPath()%>/orderSearchServlet" method="POST">
	
  <%--	
	<input type="hidden" name="hdnMethod" value=""/>
	<input type="hidden" name="hdnUserId" value="<%=strSessionId1%>"/>
	<input type="hidden" name="hdnNumPagina" value="1"/>
  --%>

    <% 
        HashMap hshMapForm = new HashMap();
        StringBuffer sbConcatenado = new StringBuffer("");
        Field[] fields = objOrderSearchForm.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
          
          /*String strFieldName = fields[i].getName();
          if(strFieldName.equals("strUnidadJerarquica") || strFieldName.equals("strRepresentante")){
            continue;
          }*/
          
          fields[i].setAccessible(true);
    %>    <input type="hidden" name="<%=fields[i].getName()%>" value="<%=fields[i].get(objOrderSearchForm)%>">
    <%      String strValue="";
            if(fields[i].get(objOrderSearchForm)!=null)
              strValue = fields[i].get(objOrderSearchForm).toString();
            if(!strValue.equals(""))
              strValue = strValue.trim();          
          /*if(strValue==null){
              strValue="";
          }else{
              strValue = strValue.trim();
          }*/
          

        //  strValue = new String(strValue.toString().getBytes("UTF8"),"ISO-8859-1");
          strValue = StringUtils.replace(strValue, " ", "%20");
          strValue = StringUtils.replace(strValue, "&", "%26");
          strValue = StringUtils.replace(strValue, " ", "%20");
          strValue = StringUtils.replace(strValue, "&", "%26");
          strValue = StringUtils.replace(strValue, "Ñ", "%D1");
          strValue = StringUtils.replace(strValue, "ñ", "%F1");
          strValue = StringUtils.replace(strValue, "á", "%E1");
          strValue = StringUtils.replace(strValue, "é", "%E9");
          strValue = StringUtils.replace(strValue, "í", "%ED");
          strValue = StringUtils.replace(strValue, "ó", "%F3");
          strValue = StringUtils.replace(strValue, "ú", "%FA");
          strValue = StringUtils.replace(strValue, "Á", "%C1");
          strValue = StringUtils.replace(strValue, "É", "%C9");
          strValue = StringUtils.replace(strValue, "Í", "%CD");
          strValue = StringUtils.replace(strValue, "Ó", "%D3");
          strValue = StringUtils.replace(strValue, "Ú", "%DA");
          strValue = strValue.trim();
          logger.debug("------Inicio strValue-----");
          System.out.println("Valor : "+strValue);
          logger.debug("------Fin strValue-----");
          sbConcatenado.append(fields[i].getName()).append("=").append(strValue).append("&");
          fields[i].setAccessible(false);
        }
    %>
	
    <table border="0" cellspacing="7" cellpadding="0" width="99%" align="center"><tr><td class="CellContent">
	
		<table border="0" cellspacing="0" cellpadding="0" align="center" width="99%">
            <tr bgcolor="#F5F5EB">
                <td valign="top" class="CellContent">
                <%	if(StringUtils.isNotBlank(strOptionsSelected)) {
                %>		<%=strOptionsSelected%>
				<%	} else {
				%>		<font class="CriteriaLabel"> Todos: </font><font class="CellContent">*</font>&nbsp;
				<%	}
				%>
                </td>
                <td align="right" width="18%" valign="top">
                    <a href="javascript:fxGoToOtherSearch()"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/binocular.gif" border="0" width="88" height="24" alt="Otra Búsqueda"></a>
                </td>
            </tr>
        </table>
	
	</td></tr><tr><td>

	<%if (iFlag==Constante.FLAG_BUSQUEDA_ORDEN) {%>
	<display:table id="orders" name="arrOrderList" sort="external" partialList="true" size="<%=new Integer(strNumTotalRegistros)%>" requestURI="" class="orders" pagesize="<%=Integer.parseInt(strHdnNumRegistros)%>" style="width: 100%">
    <display:setProperty name="paging.banner.some_items_found"><span class="spanPaging"> {0} {1} encontrados, mostrando <%=(((lPageSelected-1)*iRowsByPage)+1)+" de "+((lPageSelected*iRowsByPage)>lNumTotalRegistros?lNumTotalRegistros:(lPageSelected*iRowsByPage))%>. </span></display:setProperty>
		<display:column title="Nro" style="white-space: nowrap;" media="html">
			<a href='javascript:fxOrderDetail("<%=((HashMap) orders).get("nporderid")%>")'>
				<img src="<%=Constante.PATH_APPORDER_SERVER%>/images/viewDetail.gif" width="15" height="15" border="0" alt="Detalle de la Orden"></a>
			<%=((HashMap) orders).get("rownum")%>
		</display:column>
		<display:column property="nporderid" title="Nro. Orden"/>
		<display:column property="npordercode" title="Nro. Solicitud"/>
		<display:column property="swruc" title="RUC"/>
		<display:column property="swname" title="Razon Social"/>
		<display:column property="swsitename" title="Site"/>
		<display:column property="nptype" title="Categoria"/>
		<display:column property="npspecification" title="Sub Categoria"/>
		<display:column property="npstatus" title="Estado Orden"/>      
		<display:column property="npsalesmanname" title="Vendedor"/>
		<display:column property="npdealername" title="Dealer"/>
		<display:column property="unitcount" title="Numero de Unidades"/>
		<display:column property="npcreatedby" title="Creado Por"/>
		<display:column property="npcreateddate" title="Fecha Creacion"/>
		<display:column property="vchwebpayment" title="Pago Web"/> <!-- PRY-1239 -->
	</display:table>
	
	<%} else if (iFlag==Constante.FLAG_BUSQUEDA_REPARACION) {%>
	<display:table id="orders" name="arrOrderList" sort="external" partialList="true" size="<%=new Integer(strNumTotalRegistros)%>" requestURI="" class="orders" pagesize="<%=Integer.parseInt(strHdnNumRegistros)%>" style="width: 100%">
    <display:setProperty name="paging.banner.some_items_found"><span class="spanPaging"> {0} {1} encontrados, mostrando <%=(((lPageSelected-1)*iRowsByPage)+1)+" de "+((lPageSelected*iRowsByPage)>lNumTotalRegistros?lNumTotalRegistros:(lPageSelected*iRowsByPage))%>. </span></display:setProperty>
		<display:column title="Nro" style="white-space: nowrap;" media="html">
			<a href='javascript:fxOrderDetail("<%=((HashMap) orders).get("nporderid")%>")'>
				<img src="<%=Constante.PATH_APPORDER_SERVER%>/images/viewDetail.gif" width="15" height="15" border="0" alt="Detalle de la Orden"></a>
			<%=((HashMap) orders).get("rownum")%>
		</display:column>
		<display:column property="nporderid" title="Nro. Orden"/>
		<display:column property="swname" title="Razon Social"/>
		<display:column property="npname" title="Tienda"/>
		<display:column property="npstatusorder" title="Estado Orden"/>
		<display:column property="npcreateddate" title="Fecha Pedido"/>
		<display:column property="nptype" title="Categoria"/>
		<display:column property="npspecification" title="Sub Categoria"/>
		<display:column property="nprepairid" title="Nro. Reparación"/>
		<display:column property="npphone" title="Teléfono"/>
		<display:column property="npmodel" title="Modelo"/>
        <!-- INICIO - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 1/10/2010 --> 
        <display:column property="nptipoequipo" title="Tipo de Equipo"/>
        <!-- INICIO - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 1/10/2010 --> 
		<display:column property="npimei" title="IMEI"/>
		<display:column property="nprepairtype" title="Tipo Reparación"/>
		<display:column property="npstatusrepair" title="Estado Reparación"/>
		<display:column property="nxfalla" title="Falla"/>
		<display:column property="npendsituation" title="Situación"/>
		<display:column property="npusername1" title="Técnico"/>
		<display:column property="npcreatedby" title="Creado Por"/>
		<display:column property="npcreateddate" title="Fecha Creacion"/>      
	</display:table>
	
	<%}%>
	
    </td></tr></table>
    
</form>
<script type="text/javascript">

    function fxReturnConcatenado() {
      //alert('JP_ORDER_LISTShowPage - fxReturnConcatenado');
      strReturnConcatenado = "hdnCustomerId="+document.frmdatos.hdnCustomerId.value+"&"+
            "hdnCustomerName="+document.frmdatos.hdnCustomerName.value+"&"+
            "hdnRuc="+document.frmdatos.hdnRuc.value+"&"+
            "hdnCustomerIdbscs="+document.frmdatos.hdnCustomerIdbscs.value+"&"+
            "hdnStatusCollection="+document.frmdatos.hdnStatusCollection.value+"&"+
            "hdnTypecia="+document.frmdatos.hdnTypecia.value+"&"+
            "hdnNumber="+document.frmdatos.hdnNumber.value+"&"+
            "hdnCodbscs="+document.frmdatos.hdnCodbscs.value+"&"+
            "cmbCriterion="+document.frmdatos.cmbCriterion.value+"&"+
            "txtNumber="+document.frmdatos.txtNumber.value+"&"+
			"chkNumberSearch="+document.frmdatos.chkNumberSearch.value+"&"+
            "txtCodBscs="+document.frmdatos.txtCodBscs.value+"&"+
            "txtRuc="+document.frmdatos.txtRuc.value+"&"+
            "txtCustomerName="+escape(document.frmdatos.txtCustomerName.value)+"&"+
            "txtNroOrden="+document.frmdatos.txtNroOrden.value+"&"+
            "txtNroSolicitud="+document.frmdatos.txtNroSolicitud.value+"&"+
            "cmbEstadoOrden="+document.frmdatos.cmbEstadoOrden.value+"&"+
            "hdnEstadoOrden="+document.frmdatos.hdnEstadoOrden.value+"&"+
            "cmbDivisionNegocio="+document.frmdatos.cmbDivisionNegocio.value+"&"+
            "hdnDivisionNegocio="+escape(document.frmdatos.hdnDivisionNegocio.value)+"&"+
            "cmbSolucionNegocio="+document.frmdatos.cmbSolucionNegocio.value+"&"+
            "hdnSolucionNegocio="+escape(document.frmdatos.hdnSolucionNegocio.value)+"&"+
            "cmbCategoria="+document.frmdatos.cmbCategoria.value+"&"+
            "hdnCategoria="+escape(document.frmdatos.hdnCategoria.value)+"&"+
            "cmbSubCategoria="+escape(document.frmdatos.cmbSubCategoria.value)+"&"+
            "hdnSubCategoria="+escape(document.frmdatos.hdnSubCategoria.value)+"&"+
            //"cmbZona="+document.frmdatos.cmbZona.value+"&"+
            //"hdnZona="+document.frmdatos.hdnZona.value+"&"+
            //"cmbCoordinador="+document.frmdatos.cmbCoordinador.value+"&"+
            //"hdnCoordinador="+document.frmdatos.hdnCoordinador.value+"&"+
            //"cmbSupervisor="+document.frmdatos.cmbSupervisor.value+"&"+
            //"hdnSupervisor="+document.frmdatos.hdnSupervisor.value+"&"+
            //"cmbConsultorEjecutivo="+document.frmdatos.cmbConsultorEjecutivo.value+"&"+
            //"hdnConsultorEjecutivo="+document.frmdatos.hdnConsultorEjecutivo.value+"&"+
            "cmbRegion="+document.frmdatos.cmbRegion.value+"&"+
            "hdnRegion="+document.frmdatos.hdnRegion.value+"&"+
            "cmbTienda="+document.frmdatos.cmbTienda.value+"&"+
            "hdnTienda="+document.frmdatos.hdnTienda.value+"&"+
            "txtCreateDateFrom="+document.frmdatos.txtCreateDateFrom.value+"&"+
            "txtCreateDateTill="+document.frmdatos.txtCreateDateTill.value+"&"+
            "txtCreadoPor="+escape(document.frmdatos.txtCreadoPor.value)+"&"+
            "txtNroReparacion="+document.frmdatos.txtNroReparacion.value+"&"+
            "txtNextel="+document.frmdatos.txtNextel.value+"&"+
            "cmbTipoServicio="+document.frmdatos.cmbTipoServicio.value+"&"+
            "hdnTipoServicio="+document.frmdatos.hdnTipoServicio.value+"&"+
            "cmbModelo="+document.frmdatos.cmbModelo.value+"&"+
            "hdnModelo="+document.frmdatos.hdnModelo.value+"&"+
            "cmbTipoFalla="+document.frmdatos.cmbTipoFalla.value+"&"+
            "hdnTipoFalla="+document.frmdatos.hdnTipoFalla.value+"&"+
            "txtImei="+document.frmdatos.txtImei.value+"&"+
            "cmbSituacion="+document.frmdatos.cmbSituacion.value+"&"+
            "hdnSituacion="+document.frmdatos.hdnSituacion.value+"&"+
            "cmbTecnicoResponsable="+document.frmdatos.cmbTecnicoResponsable.value+"&"+
            "hdnTecnicoResponsable="+document.frmdatos.hdnTecnicoResponsable.value+"&"+
            "cmbEstadoReparacion="+document.frmdatos.cmbEstadoReparacion.value+"&"+
            "hdnEstadoReparacion="+document.frmdatos.hdnEstadoReparacion.value+"&"+
            "cmbTipoProceso="+document.frmdatos.cmbTipoProceso.value+"&"+
            "hdnTipoProceso="+document.frmdatos.hdnTipoProceso.value+"&"+
            "txtImeiCambio="+document.frmdatos.txtImeiCambio.value+"&"+
            "txtImeiPrestamo="+document.frmdatos.txtImeiPrestamo.value+"&"+
            "chkReparacion="+document.frmdatos.chkReparacion.value+"&"+
            "hdnLogin="+document.frmdatos.hdnLogin.value+"&"+
            "hdnUserId="+document.frmdatos.hdnUserId.value+"&"+
            "hdnNumRegistros="+document.frmdatos.hdnNumRegistros.value+"&"+
            "hdnNumPagina="+document.frmdatos.hdnNumPagina.value+"&"+
            "iFlag="+document.frmdatos.iFlag.value+"&"+
            "txtNroGuia="+document.frmdatos.txtNroGuia.value+"&"+
            "hdnSalesstructid="+document.frmdatos.hdnSalesstructid.value+"&"+
            "hdnProviderid="+document.frmdatos.hdnProviderid.value+"&"+
            "strUnidadJerarquica="+document.frmdatos.strUnidadJerarquica.value+"&"+
            "strRepresentante="+document.frmdatos.strRepresentante.value+"&"+
            "txtProposedId="+document.frmdatos.txtProposedId.value+"&"+
            "cmbWebPayment="+document.frmdatos.cmbWebPayment.value; //PRY-1239 19/09/2018 PCACERES
        return strReturnConcatenado;
    }
  function paginar(param) {
    try {
      parent.bottomFrame.document.frmsearch.hdnNumPagina.value = param;
      cadena = "/portal/page/portal/orders/ORDER_LIST?"+parent.bottomFrame.fxReturnConcatenado()+"&d-447055-p="+param;
	  } catch(exception) {
	    document.frmdatos.hdnNumPagina.value = param;
      cadena = "/portal/page/portal/orders/ORDER_LIST?"+fxReturnConcatenado()+"&d-447055-p="+param;
	  }
	  document.location.href = cadena;
  }
  
  function fxPrimeraPagina(param) {
    try {
      parent.bottomFrame.document.frmsearch.hdnNumPagina.value = param;
      cadena = "/portal/page/portal/orders/ORDER_LIST?"+parent.bottomFrame.fxReturnConcatenado()+"&d-447055-p="+param;
    } catch(exception) {
	    document.frmdatos.hdnNumPagina.value = param;
      cadena = "/portal/page/portal/orders/ORDER_LIST?"+fxReturnConcatenado()+"&d-447055-p="+param;
	  }
		document.location.href = cadena;
  }
  
  function fxAnteriorPagina(param) {
    try {
      parent.bottomFrame.document.frmsearch.hdnNumPagina.value = param - 1;
      cadena = "/portal/page/portal/orders/ORDER_LIST?"+parent.bottomFrame.fxReturnConcatenado()+"&d-447055-p="+(param - 1);
    } catch(exception) {
	    document.frmdatos.hdnNumPagina.value = param - 1;
      cadena = "/portal/page/portal/orders/ORDER_LIST?"+fxReturnConcatenado()+"&d-447055-p="+(param - 1);
	  }
		document.location.href = cadena;
  }
  
  function fxSiguientePagina(param) {
    try {
      parent.bottomFrame.document.frmsearch.hdnNumPagina.value = param + 1;
      cadena = "/portal/page/portal/orders/ORDER_LIST?"+parent.bottomFrame.fxReturnConcatenado()+"&d-447055-p="+(param + 1);
    } catch(exception) {
	    document.frmdatos.hdnNumPagina.value = param + 1;
      cadena = "/portal/page/portal/orders/ORDER_LIST?"+fxReturnConcatenado()+"&d-447055-p="+(param + 1);
	  }
		document.location.href = cadena;
  }
  
  function fxUltimaPagina(param) {
    try {
      parent.bottomFrame.document.frmsearch.hdnNumPagina.value = param;
      cadena = "/portal/page/portal/orders/ORDER_LIST?"+parent.bottomFrame.fxReturnConcatenado()+"&d-447055-p="+param;
    } catch(exception) {
	    document.frmdatos.hdnNumPagina.value = param;
      cadena = "/portal/page/portal/orders/ORDER_LIST?"+fxReturnConcatenado()+"&d-447055-p="+param;
	  }    
		document.location.href = cadena;
  }

	function fxObtenerParametrosDisplayTag(url) {
		var indice = url.indexOf('d-');
		if (indice != -1) {
			url = url.substring(indice);
			indice2 = url.indexOf('&');
			if(indice2 > 0) {
				paramDisplayTag = url.substr(0,indice2);
			}
			else {
				paramDisplayTag = url;
			}
		}
		return paramDisplayTag;
	}

	function fxGetPageNumber(url) {
		var indice = url.indexOf("d-");
		if (indice != -1) {
			url = url.substring(indice);
			indice2 = url.indexOf("&");
			if(indice2 > 0) {
				paramDisplayTag = url.substr(0,indice2);
			} else {
				paramDisplayTag = url;
			}
			indice3 = url.indexOf("=");
			if(indice3 > 0) {
				pageNumber = paramDisplayTag.substr(indice3+1);
			} else {
				pageNumber = 0;
			}
		}
		return "&hdnNumPagina="+pageNumber;
	}
	
	function fxOrderDetail(strOrderId){
          //URL DE POPUP
          var url="/portal/pls/portal/ORDERS.PLI_ORDER_REDIRECT?an_nporderid="+strOrderId+"&av_execframe=BOTTOMFRAME";
          parent.bottomFrame.location.replace(url);
	} 
	
	function fxGoToOtherSearch() {
    location.replace("/portal/page/portal/orders/ORDER_SEARCH");
	}
	
</script>
<%
} catch(SessionException se) {
	//se.printStackTrace();
	out.println("<script>alert('Su sesión ha finalizado. Ingrese nuevamente');</script>");
	String strUrl = "/portal/pls/portal/WEBSALES.NP_USER_AVAILABILITY_PL_PKG.PL_LOGOUT";
	out.print("<script>parent.mainFrame.location.replace('"+strUrl+"');</script>");
} catch(Exception e) {
  String strMessageExceptionGeneralStart = "";
  strMessageExceptionGeneralStart = MiUtil.getMessageClean(e.getMessage());
  logger.error(formatException(e));
  out.println("<script>alert('"+strMessageExceptionGeneralStart+"');</script>");   
}%>