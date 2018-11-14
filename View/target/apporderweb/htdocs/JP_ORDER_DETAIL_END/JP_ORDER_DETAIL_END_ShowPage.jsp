<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@page import="oracle.portal.provider.v2.ProviderUser" %>
<%@page import="oracle.portal.provider.v2.render.PortletRendererUtil" %>
<%@page import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil" %>
<%@page import="pe.com.nextel.service.*" %>
<%@page import="pe.com.nextel.bean.*" %>
<%@page import="pe.com.nextel.util.*" %>
<%@page import="java.util.*,java.text.SimpleDateFormat"%>
<%@page import="pe.com.nextel.exception.SessionException" %>
<%
try{
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
   
//strSessionId1 = "98102396";
System.out.println("Sesión capturada después del resuest: " + strSessionId1);
PortalSessionBean portalSessionBean = (PortalSessionBean) SessionService.getUserSession(strSessionId1);
if (portalSessionBean == null) {
    System.out.println("No se encontró la sesión de Java ->" + strSessionId1);
    throw new SessionException("La sesión finalizó");
}
   String strBuildingId =null;
   String strLogin=null;
   int iLevel=0;  
   String strCode=null;
   String strUser=null; 
   int iUserId=0;//psbSesion.getUserid();
   int iAppId=0;//psbSesion.getAppId();   
 
 
   strBuildingId=portalSessionBean.getBuildingid()+"";
   strLogin=portalSessionBean.getLogin();
   iLevel=portalSessionBean.getLevel();
   strCode=portalSessionBean.getCode();
   strUser=portalSessionBean.getNom_user();   
   iUserId= portalSessionBean.getUserid();
   iAppId= portalSessionBean.getAppId();      
   
   System.out.println("--------------------INICIO JP_ORDER_DETAIL_END-----------------------");

   //Parametros    
   String strOrderId0=(request.getParameter("an_nporderid")==null?"0":request.getParameter("an_nporderid"));
   System.out.println("strOrderId-->"+strOrderId0);
   long lOrderId0=Long.parseLong(strOrderId0);  
  
    HashMap hshScreenOptions=new HashMap();
    GeneralService objGeneralService= new GeneralService();
 
    //Validacion SreenOptions
    System.out.println("OrderId:"+lOrderId0+"/iUserId:"+iUserId+"/iAppId:"+iAppId);
    hshScreenOptions=objGeneralService.getPermissionDetail(lOrderId0, iUserId, iAppId);  
    String strMessage=(String)hshScreenOptions.get("strMessage");
    if (strMessage!=null)
          throw new Exception(strMessage);  
    
    int iFlagCarrier=MiUtil.parseInt((String)hshScreenOptions.get("iRetorno"));  
    System.out.println("iFlagCarrier:"+iFlagCarrier);

%>

 <input type="hidden" name="flgSave" value="0">
 
<% if(iFlagCarrier==1) { %>
<table border="0" cellspacing="1" cellpadding="2" width="100%" >
   <tr align="center"> 
     <td>
      <input type="hidden" name="v_saveOption" value="0"> 
      <input type=button name="btnUpdOrder" value="Grabar" disabled="disabled" onClick="this.form.v_saveOption.value=1;fxDoSubmitDetail('updateOrdenDetail')">
      
     </td>
   </tr> 
</table>
<%}%>

<script>
   function fxLoadGrabar(){
      if (document.frmdatos.btnUpdOrder != null)
         document.frmdatos.btnUpdOrder.disabled = false;
   }
   fxLoadGrabar();
</script>

</form>

<%}catch(SessionException se) {
     //se.printStackTrace();
     out.println("<script>alert('Su sesión ha finalizado. Ingrese nuevamente');</script>");
     String strUrl = "/portal/pls/portal/WEBSALES.NP_USER_AVAILABILITY_PL_PKG.PL_LOGOUT";
     //out.print("<script>parent.mainFrame.location.replace('"+strUrl+"');</script>");
   }catch(Exception e) {
     String strMessageExceptionGeneralStart = "";
     strMessageExceptionGeneralStart = MiUtil.getMessageClean(e.getMessage());
     out.println("<script>alert('"+strMessageExceptionGeneralStart+"');</script>");   
   }
%>