<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@ page import="oracle.portal.provider.v2.ProviderUser" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="pe.com.nextel.bean.PortalSessionBean" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.service.OrderTabsService" %>
<%@ page import="pe.com.nextel.service.SessionService" %>
<%@ page import="pe.com.nextel.util.GenericObject" %>
<%@ page import="pe.com.nextel.exception.SessionException" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net/"%>
<%!
protected static Logger logger = Logger.getLogger(HttpServlet.class);

protected String formatException(Throwable e) {
	return GenericObject.formatException(e);
}
%>
<%
try {
  String strSessionId="";
	String strOrderId = StringUtils.defaultString(request.getParameter(Constante.PARAM_ORDER_ID),"0");
	long lOrderId = Long.parseLong(strOrderId);
	System.out.println(Constante.PARAM_ORDER_ID+": "+strOrderId);
	PortletRenderRequest pReq = (PortletRenderRequest) request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
	ProviderUser objetoUsuario1 = pReq.getUser();
  strSessionId=objetoUsuario1.getPortalSessionId();
	PortalSessionBean portalUser = (PortalSessionBean)SessionService.getUserSession(strSessionId);
%>
<link rel="stylesheet" type="text/css" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
<script>
				
    function fxSaveEquipmentNote() {
            if(fxValidateAddNote()) {
                    document.frmdatos.hdnMethod.value="addComment";
                    document.frmdatos.hdnFlagSave.value="S";
                    document.frmdatos.submit();
            }
    }
   
   function fxCancel() {         
      //location.replace('/portal/pls/portal/ORDERS.PLI_ORDER_REDIRECT?an_nporderid=<%=lOrderId%>&av_execframe=MAINFRAME');      
      document.frmdatos.hdnMethod.value="cancel";
      document.frmdatos.submit();
    }
	
	function fxValidateAddNote() {
		var txtSubject = document.frmdatos.txtSubject.value;
		var txtDescription = document.frmdatos.txtDescription.value;
		if(fxTrim(txtSubject)=="") {
			document.frmdatos.txtSubject.select();
			document.frmdatos.txtSubject.focus();
			alert("Ingrese Asunto");
			return false;
		}
		if(txtDescription=="") {
			document.frmdatos.txtDescription.select();
			document.frmdatos.txtDescription.focus();
			alert("Ingrese Descripción");
			return false;
		}
    
    if(txtDescription.length >4000){
      //document.frmdatos.txtDescription.select();
			document.frmdatos.txtDescription.focus();
      alert("La nota tiene longitud mayor a la permitida (4000 caracteres).");
      return false;
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

	function fxMaxlength(obj) {
		obj.value = obj.value.substring(0, 4000);
	}

</script>
<form name="frmdatos" id="frmdatos" action="<%=request.getContextPath()%>/orderTabsServlet" method="POST">

	<input type="hidden" name="hdnOrderId" value="<%=lOrderId%>"/>
	<input type="hidden" name="hdnFlagSave" value="N"/>
	<input type="hidden" name="hdnMethod" />
	<input type="hidden" name="hdnLogin" value="<%=portalUser.getLogin()%>"/>
        <input type="hidden" name="hdnAppId" value="<%=portalUser.getAppId()%>"/>
        <input type="hidden" name="hdnUserId" value="<%=portalUser.getUserid()%>"/>
        
	
	<table border="0" cellspacing="1" cellpadding="1" align="center" class="BannerSecondaryLink" width="100%">
		<tr>
			<td class="CellLabel">
				Asunto
			</td>
			<td class="CellContent">
				<input type="text" name="txtSubject" size="100" maxlength="100" onblur="this.value=fxTrim(this.value);"/>
			</td>
		</tr>
		<tr>
			<td class="CellLabel">
				Notas
			</td>
			<td class="CellContent">
				<textarea name="txtDescription" rows="6" cols="100"></textarea>
			</td>
		</tr>
	</table>
	<br/>
	<table align="center" border="0">
		<tr>
			<td>
				<input type="button" value="Grabar" style="width: 100px" onclick="javascript:fxSaveEquipmentNote()"/>
				<input type="button" value="Cancelar" style="width: 100px" onclick="javascript:fxCancel()"/>
			</td>
		</tr>
	</table>
</form>
<%
} catch(SessionException se) {
	se.printStackTrace();
	out.println("<script>alert('Su sesión ha finalizado. Ingrese nuevamente');</script>");
	String strUrl = "/portal/pls/portal/WEBSALES.NP_USER_AVAILABILITY_PL_PKG.PL_LOGOUT";
	out.print("<script>parent.mainFrame.location.replace('"+strUrl+"');</script>");
} catch(Exception e) {
	logger.error(formatException(e));
}%>