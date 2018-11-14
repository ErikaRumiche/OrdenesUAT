<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.bean.PortalSessionBean" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@ page import="oracle.portal.provider.v2.ProviderUser" %>
<%@ page import="pe.com.nextel.bean.DominioBean"%>
<%@ page import="pe.com.nextel.bean.SuspensionReportsBean"%>
<%@ page import="pe.com.nextel.service.SessionService" %>
<%@ page import="pe.com.nextel.util.GenericObject"%>
<%@ page import="pe.com.nextel.service.GeneralService"%>
<%@ page import="pe.com.nextel.service.OrderSearchService"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<%@ page import="pe.com.nextel.exception.SessionException"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="org.apache.log4j.Logger"%>
<%@ page import="org.displaytag.tags.*"%>
<%@ page import="org.displaytag.util.*"%>
<%!    
protected static Logger logger = Logger.getLogger(HttpServlet.class);

protected String formatException(Throwable e) {
	return GenericObject.formatException(e);
}
%>
<%
try{

	HashMap hshDataMap = new HashMap();
  OrderSearchService objSearchService = new OrderSearchService();
  ArrayList arrListado = new ArrayList();
  
  // parametros de entrada
  String strMessage = null;
  String strSessionId = "";
  String strFechaDesde = "";
  String strFechaHasta = "";  
  String strAreaCal = "";
  String strMotivoSuspension = "";  
  String strAses = "";      
  String strTipoClt = "";
  String strHerramientaRetencion = ""; 
  
  // parametros para poner en el reporte  
  String strAREA = "";
  String strASESOR = "";
  String strTIPO_CLIENTE = "";  
  String strCOD_CLIENTE = "";
  String strCUENTA = "";
  String strNOMBRE_CLIENTE = "";
  String strFECHA_REGISTRO = "";
  String strFECHA_EJECUCION = "";
  String strMOTIVO = "";
  String strHERRAMIENTA = "";
  String strTELEFONO = "";
  String strESTADO = "";
           
  OrderSearchService objOrderSearchService = new OrderSearchService(); //objeto repair service que va a ejecutar la llamada hacia arriba
	strMessage = null;

%>

  <table cellspacing="3" cellpadding="2" border="1" width="100%" align="center">
    <tr>      
      <td class="CellLabel" align="Center" width="5%" bgcolor="#FFCC99"><b><font face="ms sans serif">N&deg;</font></b></td>
      <td class="CellLabel" align="Center" width="5%" bgcolor="#FFCC99"><b><font face="ms sans serif">Area</font></b></td>
      <td class="CellLabel" align="Center" width="5%" bgcolor="#FFCC99"><b><font face="ms sans serif">Asesor</font></b></td>  
      <td class="CellLabel" align="Center" width="6%" bgcolor="#FFCC99"><b><font face="ms sans serif">Tipo de Cliente</font></b></td>
      <td class="CellLabel" align="Center" width="7%" bgcolor="#FFCC99"><b><font face="ms sans serif">Código de Cliente</font></b></td>
      <td class="CellLabel" align="Center" width="5%" bgcolor="#FFCC99"><b><font face="ms sans serif">N° de Cuenta</font></b></td>      
      <td class="CellLabel" align="Center" width="7%" bgcolor="#FFCC99"><b><font face="ms sans serif">Cliente</font></b></td>
      <td class="CellLabel" align="Center" width="5%" bgcolor="#FFCC99"><b><font face="ms sans serif">Fecha de Registro</font></b></td>
      <td class="CellLabel" align="Center" width="5%" bgcolor="#FFCC99"><b><font face="ms sans serif">Fecha de Ejecución</font></b></td>
      <td class="CellLabel" align="Center" width="6%" bgcolor="#FFCC99"><b><font face="ms sans serif">Motivo</font></b></td>
      <td class="CellLabel" align="Center" width="6%" bgcolor="#FFCC99"><b><font face="ms sans serif">Herramienta</font></b></td>
      <td class="CellLabel" align="Center" width="7%" bgcolor="#FFCC99"><b><font face="ms sans serif">Teléfono</font></b></td>
      <td class="CellLabel" align="Center" width="5%" bgcolor="#FFCC99"><b><font face="ms sans serif">Estado</font></b></td>      
    </tr>
    
    <%
        
      HashMap hshResultado = new HashMap();
      HashMap hshParameters = new HashMap();
      
      strFechaDesde = StringUtils.defaultString(request.getParameter("as_fechaInicial"));
      strFechaHasta = StringUtils.defaultString(request.getParameter("as_fechaFinal"));      
      strAreaCal = StringUtils.defaultString(request.getParameter("as_areaCal"));
      strMotivoSuspension = StringUtils.defaultString(request.getParameter("as_suspensionReason"));  
      strAses = StringUtils.defaultString(request.getParameter("as_asesor"));      
      strTipoClt = StringUtils.defaultString(request.getParameter("as_tipoCliente"));
      strHerramientaRetencion = StringUtils.defaultString(request.getParameter("as_retentionTool"));  

      hshParameters.put("as_fechaInicial", strFechaDesde);
      hshParameters.put("as_fechaFinal", strFechaHasta);      
      hshParameters.put("as_areaCal", strAreaCal);
      hshParameters.put("as_suspensionReason", strMotivoSuspension);      
      hshParameters.put("as_asesor", strAses);      
      hshParameters.put("as_tipoCliente", strTipoClt);
      hshParameters.put("as_retentionTool", strHerramientaRetencion);
                  
      hshResultado = objOrderSearchService.getDetailedSuspensionList(hshParameters);
      strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);     
      
      if (StringUtils.isBlank(strMessage)){
      
        arrListado = (ArrayList)hshResultado.get("arrListado");
        Iterator iteratorAux = arrListado.iterator();        
        int intFila = 0;

          //while((iteratorAux.hasNext())& (intFila<=10) ) {
          while((iteratorAux.hasNext()) ) {
            intFila++;
            
            SuspensionReportsBean g = (SuspensionReportsBean)iteratorAux.next();
            
            strAREA = g.getDetalladoArea();
            strASESOR = g.getDetalladoAsesor();
            strTIPO_CLIENTE = g.getDetalladoTipoCliente();            
            strCOD_CLIENTE = g.getDetalladoCodCliente();
            strCUENTA = g.getDetalladoCuenta();
            strNOMBRE_CLIENTE = g.getDetalladoNombreCliente();
            strFECHA_REGISTRO = g.getDetalladoFechaRegistro();
            strFECHA_EJECUCION = g.getDetalladoFechaEjecucion();
            strMOTIVO = g.getDetalladoMotivo();
            strHERRAMIENTA = g.getDetalladoHerramienta();
            strTELEFONO = g.getDetalladoTelefono();
            strESTADO = g.getDetalladoEstado();
            
           %>
            <tr>
              <td class="CellContent" align="Center" width="5%" bgcolor="#FFFFCC"><font face="ms sans serif"><%=intFila%></font></td>
              <td class="CellContent" align="Left" width="7%" bgcolor="#FFFFCC"><font face="ms sans serif"><%=strAREA%></font></td>              
              <td class="CellContent" align="Center" width="5%" bgcolor="#FFFFCC"><font face="ms sans serif"><%=strASESOR%></font></td>                 
              <td class="CellContent" align="Center" width="6%" bgcolor="#FFFFCC"><font face="ms sans serif"><%=strTIPO_CLIENTE%></font></td>                               
              <td class="CellContent" align="Center" width="7%" bgcolor="#FFFFCC"><font face="ms sans serif"><%=strCOD_CLIENTE%></font></td>
              <td class="CellContent" align="Center" width="7%" bgcolor="#FFFFCC"><font face="ms sans serif"><%=strCUENTA%></font></td>
              <td class="CellContent" align="Center" width="7%" bgcolor="#FFFFCC"><font face="ms sans serif"><%=strNOMBRE_CLIENTE%></font></td>              
              <td class="CellContent" align="Center" width="5%" bgcolor="#FFFFCC"><font face="ms sans serif"><%=strFECHA_REGISTRO%></font></td>                 
              <td class="CellContent" align="Center" width="6%" bgcolor="#FFFFCC"><font face="ms sans serif"><%=strFECHA_EJECUCION%></font></td>                 
              <td class="CellContent" align="Left" width="6%" bgcolor="#FFFFCC"><font face="ms sans serif"><%=strMOTIVO%></font></td>
              <td class="CellContent" align="Left" width="7%" bgcolor="#FFFFCC"><font face="ms sans serif"><%=strHERRAMIENTA%></font></td>
              <td class="CellContent" align="Center" width="7%" bgcolor="#FFFFCC"><font face="ms sans serif"><%=strTELEFONO%></font></td>
              <td class="CellContent" align="Center" width="7%" bgcolor="#FFFFCC"><font face="ms sans serif"><%=strESTADO%></font></td>
            </tr>
        <%}%>
      <%}%>
  </table>          
  <P>&nbsp;</P>
</form>
<%
} catch(SessionException se) {
  se.printStackTrace();
  System.out.println("[reportGeneralSuspension.jsp][Finalizó la sesión]");
  out.println("<script>alert('Su sesión ha finalizado. Ingrese nuevamente');</script>");
  String strUrl = "/portal/pls/portal/WEBSALES.NP_USER_AVAILABILITY_PL_PKG.PL_LOGOUT";
} catch(Exception e) {
  String strMessageExceptionGeneralStart = "";
  strMessageExceptionGeneralStart = MiUtil.getMessageClean(e.getMessage());
  out.println("<script>alert('"+strMessageExceptionGeneralStart+"');</script>");   
}
%>
