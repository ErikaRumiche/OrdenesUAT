<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.form.PCMForm"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net/"%>
<% 
try {
  int strHdnNumPagina = Integer.parseInt((StringUtils.defaultString(request.getParameter("hdnNumPagina"),"1")).replaceAll(",",""));   

  HashMap hshPCMListMap=null;
  ArrayList arrListado=null;
  PCMForm pcmForm = (PCMForm) request.getAttribute("pcmForm");
  if (pcmForm!=null) {    
    hshPCMListMap = (HashMap)request.getAttribute("hshPCMListMap");
    
    arrListado = (ArrayList) hshPCMListMap.get("arrListado");
    if(arrListado != null) {
      request.setAttribute("arrListado", arrListado);
    }
  }  else {
    pcmForm = new PCMForm();
  }
  request.getSession(true).setAttribute("pcmForm", pcmForm); %>

<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/displaytag.css"></link>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>

<script type="text/javascript">            
  function goToOtherPage(pageNumber) {
      document.frmdatos.hdnNumPagina.value = pageNumber;
      document.frmdatos.hdnMethod.value = "searchPCM";
      document.frmdatos.submit();    
  }
  
  function fxGoToOtherSearch() {
      document.frmdatos.hdnMethod.value = "searchFormPCM";
      document.frmdatos.submit(); 
  }
</script>

<form name="frmdatos" action="<%=request.getContextPath()%>/orderSearchServlet" method="POST">

  <input type="hidden" name="hdnNumRegistros" id="hdnNumRegistros" value="<%=Constante.NUM_REGISTROS_X_PAGINAS%>"/>
  <input type="hidden" name="hdnNumPagina" id="hdnNumPagina" value="<%=pcmForm!=null?pcmForm.getHdnNumPagina():"" %>"/>
  
  <input type="hidden" name="hdnMethod" value="searchPCM"/>
  
  <input type="hidden" name="hdnProceso" value="<%=pcmForm!=null?pcmForm.getHdnProceso():"" %>"/>
  <input type="hidden" name="hdnEstado" value="<%=pcmForm!=null?pcmForm.getHdnEstado():"" %>"/>  
  <input type="hidden" name="txtTelefono" value="<%=pcmForm!=null?pcmForm.getTxtTelefono():"" %>"/>  
  <input type="hidden" name="txtFechaDesde" value="<%=pcmForm!=null?pcmForm.getTxtFechaDesde():"" %>"/>
  <input type="hidden" name="txtFechaHasta" value="<%=pcmForm!=null?pcmForm.getTxtFechaHasta():"" %>"/>  
  <input type="hidden" name="txtRazonSocial" value="<%=pcmForm!=null?pcmForm.getTxtRazonSocial():"" %>"/>
  
  <table border="0" cellspacing="0" cellpadding="0" align="center" width="99%">
    <tr bgcolor="#F5F5EB">      
      <td align="right" width="18%" valign="top">
        <a href="javascript:fxGoToOtherSearch()"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/binocular.gif" border="0" width="88" height="24" alt="Otra Búsqueda"></a>
      </td>
    </tr>
  </table>
  
  <table border="0" cellspacing="7" cellpadding="0" width="99%" align="center"><tr><td class="CellContent">
    <display:table id="repair" name="arrListado" class="orders" style="width: 100%">
        <display:column property="SOLICITUD" title="N° Solicitud Procesos Masivos" style="text-align:center;"/>
        <display:column property="PROCESO" title="Procesos Masivos" style="text-align:center;"/>
        <display:column property="CONTRATO" title="N° Contrato" style="text-align:center;"/>          
        <display:column property="FECHAPROGRAMACION" title="Fecha de Programación" style="text-align:center;"/>
        <display:column property="FECHAATENCION" title="Fecha de Atención" style="text-align:center;"/>
        <display:column property="ESTADO" title="Estado de Proceso" style="text-align:center;"/>
        <display:column property="MENSAJE" title="Mensaje de Error" style="text-align:left;"/>
        <display:column property="RESUMEN" title="Resumen Proceso" style="text-align:left;"/>
        <display:column property="NEXTEL" title="Nextel" style="text-align:center;"/>
        <display:column property="RAZONSOCIAL" title="Razón Social" style="text-align:left;"/>
        <display:column property="USUARIOCREACION" title="Usuario de Creación" style="text-align:left;"/>
        <display:column property="FECHACREACION" title="Fecha de Creación" style="text-align:center;"/>
        <display:column property="USUARIOMODIFICACION" title="Usuario de Modificación" style="text-align:left;"/>
        <display:column property="FECHAMODIFICACION" title="Fecha de Modificación" style="text-align:center;"/>
    </display:table>
  </table>
</form>
<% 
} catch(Exception e) {
  e.printStackTrace();
  out.println("<script>alert('" + MiUtil.getMessageClean(e.getMessage()) + "');</script>");   
}
%>