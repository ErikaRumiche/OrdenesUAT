<%System.out.println("[JP_ORDER_SEARCHFinalSuspension][Inicio]");%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@ page import="oracle.portal.provider.v2.ProviderUser" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="pe.com.nextel.bean.PortalSessionBean" %>
<%@ page import="pe.com.nextel.bean.DominioBean" %>
<%@ page import="pe.com.nextel.service.SessionService" %>
<%@ page import="pe.com.nextel.service.GeneralService" %>
<%@ page import="pe.com.nextel.util.GenericObject"%>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.exception.SessionException" %>
<%!    
protected static Logger logger = Logger.getLogger(HttpServlet.class);

protected String formatException(Throwable e) {
	return GenericObject.formatException(e);
}
%>

<%
try {
  String strSessionId1 = "";
  
  try{
    PortletRenderRequest pReq = (PortletRenderRequest) request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
    ProviderUser objetoUsuario1 = pReq.getUser();
    strSessionId1=objetoUsuario1.getPortalSessionId();
    System.out.println("Sesión capturada JP_ORDER_SEARCHFinalSuspension.jsp : " + objetoUsuario1.getName() + " - " + strSessionId1 );
    
  }catch(Exception e){
    System.out.println("Portler Not Found : " + e.getClass() + " - " + e.getMessage() );
    out.println("Portlet JP_ORDER_SEARCHFinalSuspension.jsp Not Found");
    return;
  }
  //strSessionId1 = "998102396";

  logger.debug("Sesión capturada después del request: " + strSessionId1);
  PortalSessionBean portalSessionBean = (PortalSessionBean) SessionService.getUserSession(strSessionId1);
  if (portalSessionBean == null) {
    logger.error("No se encontró la sesión de Java ->" + strSessionId1);
    throw new SessionException("La sesión finalizó");
  }
  
  GeneralService objGeneralService = new GeneralService();
	String strMessage = null;
  Calendar calendar = Calendar.getInstance();
	calendar.add(Calendar.MONTH, -6);
   
  Calendar calendar2 = Calendar.getInstance();
  calendar2.add(Calendar.MONTH, 0);
%> 

<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css" type="text/css"></link>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
<script type="text/javascript" SRC="<%=Constante.PATH_APPORDER_SERVER%>/Resource/date-picker.js"></SCRIPT>
 
<form name="frmdatos" action="<%=request.getContextPath()%>/suspensionServlet" method="POST">
 
  <input type="hidden" name="hdnLogin" value="<%=portalSessionBean.getLogin()%>"/>
  <input type="hidden" name="hdnMethod" value=""/>	
	<input type="hidden" name="hdnUserId" value="<%=strSessionId1%>"/>
  
  <table cellspacing="2" cellpadding="3" border="1" width="73%" align="left">
    <tr>      
      <td class="SectionTitle" colspan="2">Criterios</td>      
    </tr>
    <tr>
      <td class="CellLabel" align=center>Desde</td>
      <td class="CellContent" align="left">
        <input type="text" name="txtStartDateSuspension" size="10" maxlength="10" value="<%=MiUtil.getDate(calendar.getTime(), "dd/MM/yyyy")%>" onblur="this.value=fxTrim(this.value);" onKeypress="if (event.keyCode < 44 || event.keyCode > 57 || event.keyCode==45) event.returnValue = false;" />
						<a href="javascript:show_calendar('frmdatos.txtStartDateSuspension',null,null,'DD/MM/YYYY');" onmouseover="window.status='Fecha Desde';return true;" onmouseout="window.status='';return true;">
						<img src="<%=Constante.PATH_APPORDER_SERVER%>/images/show-calendar.gif" width="24" height="22" border="0"></a>&nbsp;<i>DD/MM/YYYY</i>
      </td>
    </tr>
    <tr>
      <td class="CellLabel" align="center">Hasta</td>
      <td class="CellContent" align="left">
        
          <input type="text" name="txtEndDateSuspension" size="10" maxlength="10" value="<%=MiUtil.getDate(calendar2.getTime(), "dd/MM/yyyy")%>" onblur="this.value=fxTrim(this.value);" onKeypress="if (event.keyCode < 44 || event.keyCode > 57 || event.keyCode==45) event.returnValue = false;" />
						<a href="javascript:show_calendar('frmdatos.txtEndDateSuspension',null,null,'DD/MM/YYYY');" onmouseover="window.status='Fecha Hasta';return true;" onmouseout="window.status='';return true;">
						<img src="<%=Constante.PATH_APPORDER_SERVER%>/images/show-calendar.gif" width="24" height="22" border="0"></a>&nbsp;<i>DD/MM/YYYY</i>
        
      </td>
    </tr>
    <tr>
      <td class="CellContent" width="5%" colspan="2" align="center">
        <input type="button" name="cmdExecute" value="Generar Reporte" style="width:150px;" onClick="javascript:fxSearchSuspension();" />
      </td>
    </tr>
  </table>  
  <P>&nbsp;</P>
  <P>&nbsp;</P>
  <P>&nbsp;</P>  
  <P>&nbsp;</P>
  <P>&nbsp;</P>
  <P>&nbsp;</P>
  <P>&nbsp;</P>  
</form>

<script type="text/javascript">

  function fxSearchSuspension() {
      var vForm = document.frmdatos;        
      var strhdnLogin = vForm.hdnLogin.value;      
      
      var bCreateDate = fxValidateDatesBetween(vForm.txtStartDateSuspension, vForm.txtEndDateSuspension);
      
      if (bCreateDate){
             
        var url = "<%=request.getContextPath()%>/reports/reportFinalSuspension.jsp?"      
                   +"&ad_start_date="+vForm.txtStartDateSuspension.value
                   +"&ad_end_date="+vForm.txtEndDateSuspension.value
                   +"&hdnLogin="+strhdnLogin;                      
        //parent.bottomFrame.location.replace(url);
        var popupWin = window.open(url, "reportSuspensionWindow","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,screenX=60,top=40,left=60,screenY=40,width=850,height=600");
       
      }
      return true;
  }
  
  function fxTrim(s) {
		while (s.length>0 && (s.charAt(0)==" "||s.charCodeAt(0)==10||s.charCodeAt(0)==13)) {
			s=s.substring(1, s.length);
		}
		while (s.length>0 && (s.charAt(s.length-1)==" "||s.charCodeAt(s.length-1)==10||s.charCodeAt(s.length-1)==13)) {
			s=s.substring(0, s.length-1);
		}
		return s; 
	}
  
  function fxAnyOptionFilled() {
		return true;
	}
  
  function fxValidateDatesBetween(txtStartDateSuspension, txtEndDateSuspension) {
    
    var bResult = true;
    
		if (txtStartDateSuspension.value==""  & txtEndDateSuspension.value=="") {
			alert("Ingrese las fechas de Búsqueda del Reporte");      
			txtStartDateSuspension.focus();
			//return false;
      bResult=false;
    }
    if (txtStartDateSuspension.value!=""  & txtEndDateSuspension.value=="") {
			if (fxAnyOptionFilled()){
				alert("Ingrese la fecha final del reporte");
				txtEndDateSuspension.focus();
        //return false;
        bResult=false;
			}
		}
		if (txtStartDateSuspension.value==""  & txtEndDateSuspension.value!="") {
			if (fxAnyOptionFilled()){
				alert("Ingrese la fecha inicial del reporte");
				txtStartDateSuspension.focus();
				//return false;
        bResult=false;
			}
		}    
    if (txtStartDateSuspension.value!=""  & txtEndDateSuspension.value!="") {
      if (trxTime(txtEndDateSuspension.value) < trxTime(txtStartDateSuspension.value)) {
				alert("La fecha desde debe ser menor que la fecha hasta.");
				txtStartDateSuspension.focus();
				txtEndDateSuspension.select();
				//return false;
        bResult=false;
      }
      if (!isValidDate(txtStartDateSuspension.value)){
				txtStartDateSuspension.focus();
				txtStartDateSuspension.select();
        alert("Debe ingresar un valor de fecha válido.");
				bResult=false;
			}
      if (!isValidDate(txtEndDateSuspension.value)){
				txtEndDateSuspension.focus();
				txtEndDateSuspension.select();
        alert("Debe ingresar un valor de fecha válido.");
				bResult=false;
			}
		}
    
    
    
    return bResult;
    /* final de validaciones */
  }
  
</script> 

<%
}catch(SessionException se) {
	se.printStackTrace();
  System.out.println("[JP_ORDER_SEARCHFinalSuspension][Finalizó la sesión]");
	/*out.println("<script>alert('Su sesión ha finalizado. Ingrese nuevamente');</script>");
	String strUrl = "/portal/pls/portal/WEBSALES.NP_USER_AVAILABILITY_PL_PKG.PL_LOGOUT";*/
	//out.print("<script>parent.mainFrame.location.replace('"+strUrl+"');</script>");
}catch(Exception e) {
  out.println("<script>alert('"+MiUtil.getMessageClean(e.getClass() + " - " + e.getMessage())+"');</script>");
	logger.error(formatException(e));
}%>