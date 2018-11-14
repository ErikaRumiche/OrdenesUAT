<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="pe.com.portability.service.PortabilityOrderService" %>
<%@ page import="pe.com.portability.bean.PortabilityItemBean" %>
<%@ page import="pe.com.nextel.bean.ServiciosBean" %>
<%@ page import="java.util.*"%>
<%
 try{
   String    strphoneNumber  = request.getParameter("objPhoneNumber")==null?"":(String)request.getParameter("objPhoneNumber");
   String    item_index  = request.getParameter("item_index")==null?"0":(String)request.getParameter("item_index");
   String    strItemId   = request.getParameter("strItemId"); 
   
   System.out.println("strphoneNumber:"+strphoneNumber);
   System.out.println("item_index:"+item_index);
   System.out.println("strItemId:"+strItemId);
   
   HashMap hshDetailphone   = new HashMap();
   PortabilityOrderService objPortabilityOrderService = new PortabilityOrderService();
   PortabilityItemBean objPortabilityItemBean =  null;
   String strMessage = null;
   ArrayList arrServiceList = new ArrayList();
  
   if (strphoneNumber != null){   
      hshDetailphone = objPortabilityOrderService.getDetailItemPortabilityReturnHome(strphoneNumber);
      strMessage = (String)hshDetailphone.get("strMessage"); 
      if (strMessage!= null){                   
          throw new Exception(strMessage);
      } 
      arrServiceList = (ArrayList) hshDetailphone.get("arrServiceList");
  }
%>
<html>
  
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Portabilidad Retorno: Detalle</title>
    <link type="text/css" rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"/>
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
  </head>
  
  <script defer>
     
     //Funcion  que permite cerrar el Popup del Item de la Orden
     //----------------------------------------------------------
      function fxCancelItemEditWindow() {
        parent.close();
      }
  
  </script>
  
  <body>
    <form method="post" name="frmdatos">
      <table  align="center" width="100%" border="0">
        <tr>
            <td>  
                  <table border="0" class="PortletHeaderColor" cellspacing="0" cellpadding="0" width="100%">
                      <tr class="PortletHeaderColor">
                          <td class="LeftCurve" valign="top" align="left" width="10">&nbsp;&nbsp;</td>
                          <td class="PortletHeaderColor" align="left" valign="top">
                              <font class="PortletHeaderText">Item de la Orden Portabilidad Retorno</font></td>
                          <td align="right" class="RightCurve" width="10%" >&nbsp;&nbsp;</td>
                      </tr>
                  </table>
            </td>
        </tr>
        <tr>
            <td> 
              <table align="center" width="100%" border="0" cellspacing="2" cellpadding="2" >
                <tr>
                  <td class="CellLabel" align="left" valign="top"  height="20" width="50%">&nbsp;&nbsp;Número de Télefono:</td>
                  <td class="CellContent" align="left" valign="top" height="20" width="50%">&nbsp;<%=strphoneNumber%></td>
                 </tr>
                 <tr>
                  <td class="CellLabel" align="left" valign="top"  height="20" width="50%">&nbsp;&nbsp;Plan Tarifario:</td>
                  <td class="CellContent" align="left" valign="top" height="20" width="50%">&nbsp;&nbsp;<%=(String)hshDetailphone.get("strPlanTarifarioName")%></td>
                 </tr>
                 <tr>
                  <td class="CellLabel" align="center" valign="top" colspan=2 height="20">Servicios Contratados:</td>
                 </tr>
                 <tr>
                   <td class="CellLabel" align="center" valign="top"  height="20" width="50%">&nbsp;&nbsp;Nombre del Servicio</td>
                   <td class="CellLabel" align="center" valign="top" height="20" width="50%">&nbsp;Estado</td>
                 </tr>
                 <%//Encontramos el nombre y estado del servicio asociados al Teléfono
                   //-----------------------------------------------------------------
                   HashMap h=null;
                   objPortabilityItemBean = new PortabilityItemBean();
                   String strServiceName = null;
                   String strServiceStatus = null;
                   if (arrServiceList!=null){
                      for(int i = 0; i < arrServiceList.size(); i++){
                           h = (HashMap)arrServiceList.get(i);
                           strServiceName= (String)h.get(objPortabilityItemBean.getNpServiceContract());  
                           strServiceStatus= (String)h.get(objPortabilityItemBean.getNpServiceContractStatus());  
                 %>
                 <tr>
                   <td class="CellContent" align="center" valign="top"  height="20" width="50%">&nbsp;<%=strServiceName%></td>
                   <td class="CellContent" align="center" valign="top" height="20" width="50%">&nbsp;<%=strServiceStatus%></td>
                 </tr>
                 <%}
                 }%>
              </table>
            </td>
        </tr>
        <tr>
          <td>
            <table><tr align="center"><td></td></tr></table>
          </td>
        </tr>
        <tr>
          <td>
            <table align="center" width="100%">
                <tr>
                    <td align="center"><input type="reset"  name="btnCerrar"   value=" Cerrar "  onclick="javascript:fxCancelItemEditWindow();"></td>
                 </tr>
            </table>
          </td>
        </tr>
        
      </table>
    </form>
  </body>
</html>

<%}catch(Exception ex){
   ex.printStackTrace();
%>
  <script>alert('<%=MiUtil.getMessageClean(ex.getMessage())%>');</script>
<%}%>
