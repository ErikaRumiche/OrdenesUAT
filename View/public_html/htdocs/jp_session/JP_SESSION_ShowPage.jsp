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
<%@ page import="pe.com.nextel.service.GeneralService"%>

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
    
		String  strRolId          = MiUtil.getString(request.getParameter("an_rolid"));
    int     intApplicationId  = -1;
    /**Se adiciona el parámetro secureId**/
    String  strSecureId     = MiUtil.getString(request.getParameter("secureId"));
    
    System.out.println("Portlet JP_SESSION_ShowPage - Nextel : " + user.getName()+ "| strRolId : " + strRolId + "| strSecureId : " + strSecureId + "|");
    
    
    PortalSessionBean    objPortalSessBean  = new PortalSessionBean();
    SessionService       objSessionService  = new SessionService();
    
    if( !strSecureId.equals("") ){
      intApplicationId = objSessionService.getSecureRol(MiUtil.parseInt(strSecureId));
      System.out.println("Se ingresó mediante un token : Usuario Nextel : " +user.getName() + " SecureId : " + strSecureId + " Application : " + intApplicationId);
    }else{
      System.out.println("No se ingresó mediante un token : Usuario Nextel : " +user.getName() + " SecureId : " + strSecureId + " RolId : " + strRolId);
      intApplicationId  = MiUtil.parseInt(strRolId);
    }
    
    //AGAMARRA
    //Obtengo las variables de diversas tablas, se requiere wn_salesstructid   
    //Nuevo parámetro para Jerarquía de Ventas
    String strSalesstructid  =  MiUtil.getString(request.getParameter("npsalesstructid"));
    if(strSalesstructid==null){
      strSalesstructid  =  MiUtil.getString(request.getParameter("NPSALESSTRUCTID"));
    }
    
    System.out.println("strSalesstructid jerarquia = "+strSalesstructid);
        
    int intSalesstructid = 0;
    if(strSalesstructid!=null){
      intSalesstructid = strSalesstructid.length()==0?0:Integer.parseInt(strSalesstructid);
    }
    
    System.out.println("intSalesstructid jerarquia = "+intSalesstructid);
        
    /*Obtener el id canal de venta*/
    GeneralService objGeneralService= new GeneralService();    
    HashMap hshData=null;
    
    String strMessage = "";    
    int intTipoCanal = 0; // 0 Canal, 1 SubCanal
        
    hshData = objGeneralService.get_AttChannel_Struct(intSalesstructid);
    strMessage=(String)hshData.get("strMessage");
    if (strMessage!=null)
       throw new Exception(strMessage); 
    int idCanalAtencion=MiUtil.parseInt((String)hshData.get("iRetorno"));    
        
    
    //Se agrega el parámetro "wn_salesstructid"
    //objPortalSessBean = objSessionService.getSessionData(user.getName(), intApplicationId);
    objPortalSessBean = objSessionService.getSessionData(user.getName(), intApplicationId, intSalesstructid);
    
    if( objPortalSessBean.getMessage()!=null){
      throw new Exception((String)objPortalSessBean.getMessage());
    }
	
    //objPortalSessBean.setSessionID(sessionID);
    objPortalSessBean.setSalesStructId(intSalesstructid);
      
    //Se guardan las variables obtenidas en sesión:
    session.putValue("swprovidergrpid", objPortalSessBean.getProviderGrpId()+"");
    session.putValue("npsalesstructid", strSalesstructid);
    
    objPortalSessBean.setSessionID(sessionID);
    
    //Inserto las variables obtenidas en sesión mediante SP_INS_PERSISTENCE_BY_PORTALID
    
    System.out.println("Antes de agregar la sesión");
	 HashMap objHashSession  = objSessionService.addUserSessionIni(objPortalSessBean);
    System.out.println("Despues de agregar la sesión");
    
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