<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@page import="oracle.portal.provider.v2.ProviderUser" %>
<%@page import="oracle.portal.provider.v2.ProviderSession" %>
<%@page import="oracle.portal.provider.v2.render.PortletRendererUtil" %>
<%@page import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil" %>
<%@page import="oracle.portal.provider.v2.url.UrlUtils" %>
<%@page import="pe.com.nextel.bean.PortalSessionBean"%>
<%@page import="pe.com.nextel.service.SessionService"%>
<%@page import="pe.com.nextel.util.MiUtil" %>
<%@page import="java.util.HashMap" %>

<%
  try{
    System.out.println("====================================[JP_SESSION][Inicio]======================================================");
    String               sessionID  = null;
    ProviderUser         user       = null;
    try{
      PortletRenderRequest pRequest   = (PortletRenderRequest)request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
      ProviderSession      pSession   = (ProviderSession)pRequest.getSession();
      user       = pRequest.getUser();
      sessionID  = user.getPortalSessionId();
    }catch(Exception ex){
      System.out.println("Portlet JP_SESSION_ShowPage Not Found : " + ex.getClass() + " - " + ex.getMessage());
      System.out.println("Variable del sessionId : " + sessionID);
      out.println("Portlet JP_SESSION_ShowPage Not Found");
      return;
    }
    
    //La variable a registrar la sesión
    System.out.println("SessionId enviado desde portal : " + sessionID);
    if( sessionID!= null && !sessionID.equals("") && !sessionID.equals("0")){
    
		String  strRolId      = MiUtil.getString(request.getParameter("an_rolid"));
    System.out.println("Portlet JP_SESSION_ShowPage - Nextel : " + user.getName()+ " strRolId : " + strRolId);
    
    PortalSessionBean    objPortalSessBean  = new PortalSessionBean();
    SessionService       objSessionService  = new SessionService();
    
	 objPortalSessBean = objSessionService.getSessionData(user.getName(), MiUtil.parseInt(strRolId) );
    
    public PortalSessionBean getSessionData(String v_phone, int n_rolid, int intSalesstructid) {
    if( objPortalSessBean.getMessage()!=null){
      throw new Exception((String)objPortalSessBean.getMessage());
    }
		objPortalSessBean.setSessionID(sessionID);

		HashMap objHashSession  = objSessionService.addUserSessionIni(objPortalSessBean);
    if( objHashSession.get("strMessage")!= null )
      throw new Exception((String)objHashSession.get("strMessage"));
      
    }else{
      System.out.println("Error al obtener el SessionId enviado desde portal : " + sessionID);
      throw new Exception("Error al obtener el SessionId enviado desde portal : " + sessionID + " . El módulo de órdenes no tiene sesión del usuario.");
    }
    
    System.out.println("====================[JP_SESSION][Fin]===================== -> Nextel : " + user.getName());
    }catch(Exception e) {
      System.out.println("[Exception][JP_SESSION][Errores al registrar la sesión en BD de Java - Órdenes]"+e.getClass() + " - " +e.getMessage());
%>
   <script DEFER>
     alert("La sesión del usuario para la aplicación de órdenes no fue registrada correctamente. Causa del error : <%=MiUtil.getMessageClean(e.getMessage())%>");
   </script>
<%  }%>