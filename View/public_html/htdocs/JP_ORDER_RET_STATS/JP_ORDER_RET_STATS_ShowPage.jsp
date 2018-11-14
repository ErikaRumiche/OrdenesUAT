<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest"%>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants"%>
<%@ page import="oracle.portal.provider.v2.ProviderUser"%>
<%@ page import="pe.com.nextel.bean.DominioBean"%>
<%@ page import="pe.com.nextel.bean.PortalSessionBean"%>
<%@ page import="pe.com.nextel.service.GeneralService"%>
<%@ page import="pe.com.nextel.service.RepairService"%>
<%@ page import="pe.com.nextel.service.SessionService"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="pe.com.nextel.util.GenericObject"%>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<%@ page import="pe.com.nextel.exception.SessionException"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.*"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Calendar" %>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="org.apache.log4j.Logger"%>
<%@ page import="org.displaytag.tags.*"%>
<%@ page import="org.displaytag.util.*"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net/"%>
<%! 
protected static Logger logger = Logger.getLogger(HttpServlet.class);

protected String formatException(Throwable e) {
	return GenericObject.formatException(e);
}
%>
<% 
try {
  logger.debug("--Inicio--");
  String strSessionId = "";
	/*Cuando se pruebe localmente comentar*/
  
  try {
      PortletRenderRequest pReq = (PortletRenderRequest) request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
      ProviderUser providerUser = pReq.getUser();
      strSessionId = providerUser.getPortalSessionId();
      logger.debug("Sesión capturada  : " + providerUser.getName() + " - " + strSessionId );
  } catch(Exception e) {
      logger.error("Portlet Not Found : " + e.getClass() + " - " + e.getMessage() );
      out.println("Portlet JP_ORDER_RET_STATS_ShowPage Not Found");
      return;
  }
  
  //strSessionId = "998102396";
  logger.debug("Sesión capturada después del request: " + strSessionId);
  
  //Calendar calendar = Calendar.getInstance();
	//calendar.add(Calendar.MONTH, -1);
  
   
  PortalSessionBean portalSessionBean = (PortalSessionBean) SessionService.getUserSession(strSessionId);
  if (portalSessionBean == null) {
    logger.error("No se encontró la sesión de Java ->" + strSessionId);
    throw new SessionException("La sesión finalizó");
  }
  
%>
<iframe name="searchFrame" src ="<%=request.getContextPath()%>/reports/suspRetStatsForm.jsp?hdnLogin=<%=portalSessionBean.getLogin()%>" width="100%" height="800" frameborder="0" marginheight="3" marginwidth="3">
<%--<iframe name="searchFrame" src ="<%=request.getContextPath()%>/reports/suspRetStatsForm.jsp?hdnLogin=JHERRERA" width="100%" height="800" frameborder="0" marginheight="3" marginwidth="3">--%>
</iframe>
<%      
} catch(SessionException se) {
  se.printStackTrace();
  logger.error("[JP_ORDER_RET_STATS_ShowPage.jsp][Finalizó la sesión]");
  out.println("<script>alert('Su sesión ha finalizado. Ingrese nuevamente');</script>");
  String strUrl = "/portal/pls/portal/WEBSALES.NP_USER_AVAILABILITY_PL_PKG.PL_LOGOUT";
} catch(Exception e) {
  e.printStackTrace();
  String strMessageExceptionGeneralStart = "";
  strMessageExceptionGeneralStart = MiUtil.getMessageClean(e.getMessage());
  out.println("<script>alert('"+strMessageExceptionGeneralStart+"');</script>");   
}
%>