<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.lang.reflect.Field"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="pe.com.nextel.bean.DominioBean"%>
<%@ page import="pe.com.nextel.form.OrderSearchForm" %>
<%@page import="pe.com.nextel.util.GenericObject" %>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%--@ page import="org.apache.commons.lang.ArrayUtils"--%>
<%!
protected static Logger logger = Logger.getLogger(HttpServlet.class);

protected String formatException(Throwable e) {
	return GenericObject.formatException(e);
}
%>
<%
try {
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
    <script type="text/javascript">
    <%
    OrderSearchForm objOrderSearchForm = (OrderSearchForm) request.getAttribute("objOrderSearchForm");
		logger.debug("objOrderSearchForm: "+objOrderSearchForm);
    %>
    </script>
  </head>
  <body bgcolor="#AABBCC">
    <form name="frmsearch" action="<%=request.getContextPath()%>/orderSearchServlet" method="POST">
    <%
        HashMap hshMapForm = new HashMap();
        StringBuffer sbConcatenado = new StringBuffer("");
        Field[] fields = objOrderSearchForm.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
         
         /* String strFieldName = fields[i].getName();
          if(strFieldName.equals("strUnidadJerarquica") || strFieldName.equals("strRepresentante")){
            continue;
          }*/
          
          fields[i].setAccessible(true);
    %>    <input type="hidden" name="<%=fields[i].getName()%>" 
value="<%=fields[i].get(objOrderSearchForm)%>">
    <%    String strValue = "";
          try{
            strValue = fields[i].get(objOrderSearchForm).toString();
          }catch(Exception e){
          }
          
          if(strValue==null){
              strValue="";
          }          
          strValue = strValue.trim();          
          
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
    
          
          sbConcatenado.append(fields[i].getName()).append("=").append(strValue).append("&");
          fields[i].setAccessible(false);
        }
    %>
    </form>
  </body>
  <script type="text/javascript">
    
    function fxReturnConcatenado() {
     // alert('manageForm - fxReturnConcatenado');
      strReturnConcatenado = "hdnCustomerId="+document.frmsearch.hdnCustomerId.value+"&"+
            "hdnCustomerName="+document.frmsearch.hdnCustomerName.value+"&"+
            "hdnRuc="+document.frmsearch.hdnRuc.value+"&"+
            "hdnCustomerIdbscs="+document.frmsearch.hdnCustomerIdbscs.value+"&"+
            "hdnStatusCollection="+document.frmsearch.hdnStatusCollection.value+"&"+
            "hdnTypecia="+document.frmsearch.hdnTypecia.value+"&"+
            "hdnNumber="+document.frmsearch.hdnNumber.value+"&"+
            "hdnCodbscs="+document.frmsearch.hdnCodbscs.value+"&"+
            "cmbCriterion="+document.frmsearch.cmbCriterion.value+"&"+
            "txtNumber="+document.frmsearch.txtNumber.value+"&"+
			"chkNumberSearch="+document.frmdatos.chkNumberSearch.value+"&"+
            "txtCodBscs="+document.frmsearch.txtCodBscs.value+"&"+
            "txtRuc="+document.frmsearch.txtRuc.value+"&"+
            "txtCustomerName="+escape(document.frmsearch.txtCustomerName.value)+"&"+
            "txtNroOrden="+document.frmsearch.txtNroOrden.value+"&"+
            "txtNroSolicitud="+document.frmsearch.txtNroSolicitud.value+"&"+
            "cmbEstadoOrden="+document.frmsearch.cmbEstadoOrden.value+"&"+
            "hdnEstadoOrden="+document.frmsearch.hdnEstadoOrden.value+"&"+
            "cmbDivisionNegocio="+document.frmsearch.cmbDivisionNegocio.value+"&"+
            "hdnDivisionNegocio="+escape(document.frmsearch.hdnDivisionNegocio.value)+"&"+
            "cmbSolucionNegocio="+document.frmsearch.cmbSolucionNegocio.value+"&"+
            "hdnSolucionNegocio="+escape(document.frmsearch.hdnSolucionNegocio.value)+"&"+
            "cmbCategoria="+document.frmsearch.cmbCategoria.value+"&"+
            "hdnCategoria="+escape(document.frmsearch.hdnCategoria.value)+"&"+
            "cmbSubCategoria="+escape(document.frmsearch.cmbSubCategoria.value)+"&"+
            "hdnSubCategoria="+escape(document.frmsearch.hdnSubCategoria.value)+"&"+
            "cmbZona="+document.frmsearch.cmbZona.value+"&"+
            "hdnZona="+document.frmsearch.hdnZona.value+"&"+
            "cmbCoordinador="+document.frmsearch.cmbCoordinador.value+"&"+
            "hdnCoordinador="+document.frmsearch.hdnCoordinador.value+"&"+
            "cmbSupervisor="+document.frmsearch.cmbSupervisor.value+"&"+
            "hdnSupervisor="+document.frmsearch.hdnSupervisor.value+"&"+
            "cmbConsultorEjecutivo="+document.frmsearch.cmbConsultorEjecutivo.value+"&"+
            "hdnConsultorEjecutivo="+document.frmsearch.hdnConsultorEjecutivo.value+"&"+
            "cmbRegion="+document.frmsearch.cmbRegion.value+"&"+
            "hdnRegion="+document.frmsearch.hdnRegion.value+"&"+
            "cmbTienda="+document.frmsearch.cmbTienda.value+"&"+
            "hdnTienda="+document.frmsearch.hdnTienda.value+"&"+
            "txtCreateDateFrom="+document.frmsearch.txtCreateDateFrom.value+"&"+
            "txtCreateDateTill="+document.frmsearch.txtCreateDateTill.value+"&"+
            "txtCreadoPor="+escape(document.frmsearch.txtCreadoPor.value)+"&"+
            "txtNroReparacion="+document.frmsearch.txtNroReparacion.value+"&"+
            "txtNextel="+document.frmsearch.txtNextel.value+"&"+
            "cmbTipoServicio="+document.frmsearch.cmbTipoServicio.value+"&"+
            "hdnTipoServicio="+document.frmsearch.hdnTipoServicio.value+"&"+
            "cmbModelo="+document.frmsearch.cmbModelo.value+"&"+
            "hdnModelo="+document.frmsearch.hdnModelo.value+"&"+
            "cmbTipoFalla="+document.frmsearch.cmbTipoFalla.value+"&"+
            "hdnTipoFalla="+document.frmsearch.hdnTipoFalla.value+"&"+
            "txtImei="+document.frmsearch.txtImei.value+"&"+
            "cmbSituacion="+document.frmsearch.cmbSituacion.value+"&"+
            "hdnSituacion="+document.frmsearch.hdnSituacion.value+"&"+
            "cmbTecnicoResponsable="+document.frmsearch.cmbTecnicoResponsable.value+"&"+
            "hdnTecnicoResponsable="+document.frmsearch.hdnTecnicoResponsable.value+"&"+
            "cmbEstadoReparacion="+document.frmsearch.cmbEstadoReparacion.value+"&"+
            "hdnEstadoReparacion="+document.frmsearch.hdnEstadoReparacion.value+"&"+
            "cmbTipoProceso="+document.frmsearch.cmbTipoProceso.value+"&"+
            "hdnTipoProceso="+document.frmsearch.hdnTipoProceso.value+"&"+
            "txtImeiCambio="+document.frmsearch.txtImeiCambio.value+"&"+
            "txtImeiPrestamo="+document.frmsearch.txtImeiPrestamo.value+"&"+
            "chkReparacion="+document.frmsearch.chkReparacion.value+"&"+
            "hdnLogin="+document.frmsearch.hdnLogin.value+"&"+
            "hdnUserId="+document.frmsearch.hdnUserId.value+"&"+
            "hdnNumRegistros="+document.frmsearch.hdnNumRegistros.value+"&"+
            "hdnNumPagina="+document.frmsearch.hdnNumPagina.value+"&"+
            "iFlag="+document.frmsearch.iFlag.value+"&"+
            "txtNroGuia="+document.frmsearch.txtNroGuia.value+"&"+
            "hdnSalesstructid="+document.frmsearch.hdnSalesstructid.value+"&"+
            "hdnProviderid="+document.frmsearch.hdnProviderid.value+"&"+
            "strUnidadJerarquica="+document.frmsearch.strUnidadJerarquica.value+"&"+
            "strRepresentante="+document.frmsearch.strRepresentante.value+"&"+
            "txtProposedId="+document.frmsearch.txtProposedId.value+"&"+
            "cmbWebPayment="+document.frmsearch.cmbWebPayment.value; //PRY-1239 19/09/2018 PCACERES
        return strReturnConcatenado;
    }
    //alert('/portal/page/portal/orders/ORDER_LIST?<%=sbConcatenado%>');
    parent.mainFrame.location.href="/portal/page/portal/orders/ORDER_LIST?<%=sbConcatenado%>";
   // parent.mainFrame.location.href="<%=request.getContextPath()%>/htdocs/JP_ORDER_LIST/JP_ORDER_LISTShowPage.jsp?<%=sbConcatenado%>";
  </script>
</html>
<%
} catch(Exception e) {
  out.println("<script>alert('"+MiUtil.getMessageClean(e.getClass() + " - " + e.getMessage())+"');</script>");
	logger.error(formatException(e));
}%>