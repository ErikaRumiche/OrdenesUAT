<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.bean.PortalSessionBean" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@ page import="oracle.portal.provider.v2.ProviderUser" %>
<%@ page import="pe.com.nextel.bean.DominioBean"%>
<%@ page import="pe.com.nextel.bean.ProcCompMasBean"%>
<%@ page import="pe.com.nextel.service.SessionService" %>
<%@ page import="pe.com.nextel.util.GenericObject"%>
<%@ page import="pe.com.nextel.service.OrderSearchService" %>
<%@ page import="pe.com.nextel.service.GeneralService"%>
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
  OrderSearchService orderSearchService = new OrderSearchService();
  ArrayList list = new ArrayList();
  
  String strMessage = null;
  
  // PARAMETROS
  String strSessionId = "";
  
  String strProceso = "";  
  String strEstado = "";  
  String strTelefono = "";
  String strFechaDesde = "";
  String strFechaHasta = ""; 
  String strRazonSocial = ""; 
   
  // VALORES MOSTRADOS
  
  String strN_SOLICITUD_PROCESOS_MASIVOS = "";
  String strPROCESOS_MASIVOS = "";
  String strFECHA_PROCESO = "";
  String strFECHA_PROGRAMACION = "";
  String strESTADO_PROCESO = "";
  String strMENSAJE_ERROR = "";
  String strRESUMEN_PROCESO = "";
  String strNEXTEL = "";
  String strRAZON_SOCIAL = "";
  String strUSUARIO_CREACION = "";
  String strFECHA_CREACION = "";
  String strUSUARIO_MODIFICACION = "";
  String strFECHA_MODIFICACION = "";
  String strCONTRACT_NUMBER = "";
  
	strMessage = null;

%>

  <table cellspacing="3" cellpadding="2" border="1" width="100%" align="center">
    <tr>      
      <td class="CellLabel" align="Center" width="5%" colspan="15"><b><font face="arial" SIZE=3 >NEXTEL DEL PERU</font></b></td>      
    </tr>  
    <tr>      
      <td class="CellLabel" align="Center" width="5%" colspan="15"><b><font face="arial" SIZE=3 >REPORTE DE PROCESOS COMPUESTOS MASIVOS</font></b></td>
    </tr>
    <tr>      
      <td class="CellLabel" align="Center" width="5%" bgcolor="#FFCC99"><font face="ms sans serif"><b>N&deg;</font></b></td>      
      <td class="CellLabel" align="Center" width="5%" bgcolor="#FFCC99"><b><font face="ms sans serif">N° Solicitud Procesos Masivos</font></b></td>
      <td class="CellLabel" align="Center" width="6%" bgcolor="#FFCC99"><b><font face="ms sans serif">Procesos Masivos</font></b></td>
      <td class="CellLabel" align="Center" width="6%" bgcolor="#FFCC99"><b><font face="ms sans serif">N° Contrato</font></b></td>
      <td class="CellLabel" align="Center" width="6%" bgcolor="#FFCC99"><b><font face="ms sans serif">Fecha de Programación</font></b></td>      
      <td class="CellLabel" align="Center" width="6%" bgcolor="#FFCC99"><b><font face="ms sans serif">Fecha de Atención</font></b></td>      
      <td class="CellLabel" align="Center" width="5%" bgcolor="#FFCC99"><b><font face="ms sans serif">Estado de Proceso</font></b></td>      
      <td class="CellLabel" align="Center" width="7%" bgcolor="#FFCC99"><b><font face="ms sans serif">Mensaje de Error</font></b></td>
      <td class="CellLabel" align="Center" width="7%" bgcolor="#FFCC99"><b><font face="ms sans serif">Resumen Proceso</font></b></td>      
      <td class="CellLabel" align="Center" width="7%" bgcolor="#FFCC99"><b><font face="ms sans serif">Nextel</font></b></td>
      <td class="CellLabel" align="Center" width="7%" bgcolor="#FFCC99"><b><font face="ms sans serif">Razón Social</font></b></td>      
      <td class="CellLabel" align="Center" width="7%" bgcolor="#FFCC99"><b><font face="ms sans serif">Usuario Creador</font></b></td>      
      <td class="CellLabel" align="Center" width="7%" bgcolor="#FFCC99"><b><font face="ms sans serif">Fecha de Creación</font></b></td>      
      <td class="CellLabel" align="Center" width="7%" bgcolor="#FFCC99"><b><font face="ms sans serif">Usuario de Modificación</font></b></td>
      <td class="CellLabel" align="Center" width="7%" bgcolor="#FFCC99"><b><font face="ms sans serif">Fecha de Modificación</font></b></td>      
    </tr>
    
    <%
        
      HashMap hshResultado = new HashMap();
      HashMap hshParameters = new HashMap();
      
      
      strProceso = StringUtils.defaultString(request.getParameter("as_proceso"));
      strEstado = StringUtils.defaultString(request.getParameter("as_estado"));
      strTelefono = StringUtils.defaultString(request.getParameter("as_telefono"));
      strFechaDesde = StringUtils.defaultString(request.getParameter("as_fechaDesde"));
      strFechaHasta = StringUtils.defaultString(request.getParameter("as_fechaHasta")); 
      strRazonSocial = StringUtils.defaultString(request.getParameter("as_razonSocial"));

      hshParameters.put("as_proceso", strProceso);
      hshParameters.put("as_estado", strEstado);
      hshParameters.put("as_telefono", strTelefono);
      hshParameters.put("as_fechaDesde", strFechaDesde);
      hshParameters.put("as_fechaHasta", strFechaHasta);
      hshParameters.put("as_razonSocial", strRazonSocial);
      
      hshResultado = orderSearchService.getProcesosCompuestosMasivosXLS(hshParameters);
      strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);     
      
      if (StringUtils.isBlank(strMessage)){
      
        list = (ArrayList)hshResultado.get("arrListadoPCM");
        Iterator iteratorAux = list.iterator();        
        int intFila = 0;

          //while((iteratorAux.hasNext())& (intFila<=10) ) {
          while((iteratorAux.hasNext()) ) {
            intFila++;
            //HashMap hshListado = new HashMap();
            //ClaimBeam c = new ClaimBeam();
            //hshListado = (HashMap)iteratorAux.next();
            ProcCompMasBean pcm = (ProcCompMasBean)iteratorAux.next();
            
            strN_SOLICITUD_PROCESOS_MASIVOS = pcm.getSolicitudProcesosMasivos();
            strPROCESOS_MASIVOS             = pcm.getProcesosMasivos();
            strFECHA_PROCESO                = pcm.getFechaProceso();
            strFECHA_PROGRAMACION           = pcm.getFechaDiferida();
            strESTADO_PROCESO               = pcm.getEstadoProceso();
            strMENSAJE_ERROR                = pcm.getMensajeError();
            strRESUMEN_PROCESO              = pcm.getResumenProceso();
            strNEXTEL                       = pcm.getNextel();
                strNEXTEL = strNEXTEL.substring(3);            
            strRAZON_SOCIAL                 = pcm.getRazonSocial();
            strUSUARIO_CREACION             = pcm.getUsuarioCreacion();
            strFECHA_CREACION               = pcm.getFechaCreacion();
            strUSUARIO_MODIFICACION         = pcm.getUsuarioModificacion();
            strFECHA_MODIFICACION           = pcm.getFechaModificacion();
            strCONTRACT_NUMBER              = pcm.getContractNumber();

           %>
            <tr>
              <td class="CellContent" align="Center" width="5%" bgcolor="#FFFFCC"><font face="ms sans serif"><%=intFila%></font></td>
              <td class="CellContent" align="Center" width="7%" bgcolor="#FFFFCC"><font face="ms sans serif"><%=strN_SOLICITUD_PROCESOS_MASIVOS%></font></td>              
              <td class="CellContent" align="Center" width="5%" bgcolor="#FFFFCC"><font face="ms sans serif"><%=strPROCESOS_MASIVOS%></font></td>
              <td class="CellContent" align="Center" width="5%" bgcolor="#FFFFCC"><font face="ms sans serif"><%=strCONTRACT_NUMBER%></font></td>
              <td class="CellContent" align="Center" width="6%" bgcolor="#FFFFCC"><font face="ms sans serif"><%=strFECHA_PROGRAMACION%></font></td>                            
              <td class="CellContent" align="Center" width="6%" bgcolor="#FFFFCC"><font face="ms sans serif"><%=strFECHA_PROCESO%></font></td>                            
              <td class="CellContent" align="Center" width="6%" bgcolor="#FFFFCC"><font face="ms sans serif"><%=strESTADO_PROCESO%></font></td>              
              <td class="CellContent" align="Left" width="7%" bgcolor="#FFFFCC"><font face="ms sans serif"><%=strMENSAJE_ERROR%></font></td>              
              <td class="CellContent" align="Left" width="5%" bgcolor="#FFFFCC"><font face="ms sans serif"><%=strRESUMEN_PROCESO%></font></td>              
              <td class="CellContent" align="Left" width="6%" bgcolor="#FFFFCC" style="mso-number-format:\@" ><font face="ms sans serif"><%=strNEXTEL%></font></td>                            
              <td class="CellContent" align="Left" width="6%" bgcolor="#FFFFCC"><font face="ms sans serif"><%=strRAZON_SOCIAL%></font></td>
              <td class="CellContent" align="Center" width="7%" bgcolor="#FFFFCC"><font face="ms sans serif"><%=strUSUARIO_CREACION%></font></td>              
              <td class="CellContent" align="Center" width="5%" bgcolor="#FFFFCC"><font face="ms sans serif"><%=strFECHA_CREACION%></font></td>              
              <td class="CellContent" align="Left" width="6%" bgcolor="#FFFFCC"><font face="ms sans serif"><%=strUSUARIO_MODIFICACION%></font></td>                            
              <td class="CellContent" align="Center" width="6%" bgcolor="#FFFFCC"><font face="ms sans serif"><%=strFECHA_MODIFICACION%></font></td>              
            </tr>
        <%}%>
      <%}%>
  </table>          
  <P>&nbsp;</P>
</form>
<%
} catch(SessionException se) {
  se.printStackTrace();
  System.out.println("[reportProcesosCompuestosMasivos.jsp][Finalizó la sesión]");
  out.println("<script>alert('Su sesión ha finalizado. Ingrese nuevamente');</script>");
  String strUrl = "/portal/pls/portal/WEBSALES.NP_USER_AVAILABILITY_PL_PKG.PL_LOGOUT";
} catch(Exception e) {
  String strMessageExceptionGeneralStart = "";
  strMessageExceptionGeneralStart = MiUtil.getMessageClean(e.getMessage());
  out.println("<script>alert('"+strMessageExceptionGeneralStart+"');</script>");   
}
%>
