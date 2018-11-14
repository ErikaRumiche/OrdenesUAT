<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.bean.PortalSessionBean" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@ page import="oracle.portal.provider.v2.ProviderUser" %>
<%@ page import="pe.com.nextel.bean.OrderBean"%>
<%@ page import="pe.com.nextel.bean.FinalSuspensionBean"%>
<%@ page import="pe.com.nextel.service.SessionService" %>
<%@ page import="pe.com.nextel.util.GenericObject"%>
<%@ page import="pe.com.nextel.service.OrderSearchService"%>
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
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Date" %>

<%!    
protected static Logger logger = Logger.getLogger(HttpServlet.class);

protected String formatException(Throwable e) {
	return GenericObject.formatException(e);
}
%>
<%
try{

   HashMap hshDataMap = new HashMap();
  ArrayList list = new ArrayList();
  
  String strMessage = null;
  String strSessionId = "";
  String strFechaDesde = "";
  String strFechaHasta = "";  
  
  long strCUSTOMER_ID = 0;
  String strBSCS_ID = "";
  String strNAME = "";
  String strCYCLE = "";
  String strCREATE_SUS_DATE = "";
  String strUSER_CREATE = "";
  String strSUSPENSION_DATE = "";
  String strSCORING_CUST = "";
  String strCUST_TYPE = "";
  String strCUST_STATE = "";
  String strSUSPENSION_PHONE = "";
  String strSUSPENSION_STATE = "";
  String strGIRO = "";
  String strREGION = "";
  String strIN_WARRANTY_DOC = "";
  String strTOTAL_ITEMS = "";
  String strST_SUSPENSION = "";
  String strNPCLOSEDDATE = "";


   OrderSearchService objOrderSearch = new OrderSearchService();
   strMessage = null;

%>

  <table cellspacing="3" cellpadding="2" border="1" width="100%" align="center">
    <tr>      
      <td class="CellLabel" align="Left" width="5%" ><font face="arial">CUSTCODE</font></td>
      <td class="CellLabel" align="Left" width="5%" ><font face="arial">CUSTOMER_ID</font></td>
      <td class="CellLabel" align="Left" width="6%" ><font face="arial">CCNAME</font></td>
      <td class="CellLabel" align="Left" width="6%" ><font face="arial">CICLO</font></td>
      <td class="CellLabel" align="Left" width="5%" ><font face="arial">CREATED_DATE</font></td>
      <td class="CellLabel" align="Left" width="7%" ><font face="arial">CREATED_USER</font></td>
      <td class="CellLabel" align="Left" width="6%" ><font face="arial">SUSPENDED_DATE</font></td>
      <td class="CellLabel" align="Left" width="7%" ><font face="arial">SCORING</font></td>
      <td class="CellLabel" align="Left" width="7%" ><font face="arial">TYPE_CUSTCODE</font></td>
      <td class="CellLabel" align="Left" width="8%" ><font face="arial">STATUS</font></td>
      <td class="CellLabel" align="Left" width="7%" ><font face="arial">STATUS_DES</font></td>
      <td class="CellLabel" align="Left" width="6%" ><font face="arial">GIRO</font></td>
      <td class="CellLabel" align="Left" width="6%" ><font face="arial">SUSPENDEDCUID</font></td>
      <td class="CellLabel" align="Left" width="6%" ><font face="arial">EQUIPOS</font></td>
    </tr>
    
    <%
             
      HashMap hshResultado = new HashMap();
      HashMap hshParameters = new HashMap();
            
      strFechaDesde = StringUtils.defaultString(request.getParameter("ad_start_date"));
      strFechaHasta = StringUtils.defaultString(request.getParameter("ad_end_date"));      

      hshParameters.put("ad_start_date", strFechaDesde);
      hshParameters.put("ad_end_date", strFechaHasta);      
      
      hshResultado = objOrderSearch.getfinalSuspensionList(hshParameters);
      strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT); 
      
      if (StringUtils.isBlank(strMessage)){
      
        list = (ArrayList)hshResultado.get("objArrayList");
        Iterator iteratorList = list.iterator();        
        int intFila = 0;

          while((iteratorList.hasNext()) ) {
            intFila++;
            //Falta recorrer el iterator....
            FinalSuspensionBean order = (FinalSuspensionBean)iteratorList.next();
            
            strCUSTOMER_ID = order.getNpCustomerId();
            strBSCS_ID = order.getNpBscsId();
            if(order.getNpName() != null){
               strNAME = order.getNpName();
            }else{
               strNAME = "";
            }
            
            if(order.getNpCycle() != null){
               strCYCLE = order.getNpCycle();
            }else{
               strCYCLE = "";
            }
            
            if(order.getNpCreateSupDate() != null){
               strCREATE_SUS_DATE = order.getNpCreateSupDate();
            }else{
               strCREATE_SUS_DATE = "";
            }
            
            if(order.getNpCreatedBy() != null){
               strUSER_CREATE = order.getNpCreatedBy();
            }else{
               strUSER_CREATE = "";
            }
            
            if(order.getNpSuspensionDate() != null){
               strSUSPENSION_DATE = order.getNpSuspensionDate();
            }else{
               strSUSPENSION_DATE = "";
            }
            
            if(order.getNpScoringCust() != null){
               strSCORING_CUST = order.getNpScoringCust();
            }else{
               strSCORING_CUST = "";
            }
            
            if(order.getNpCustomerType() != null){
               strCUST_TYPE = order.getNpCustomerType();
            }else{
               strCUST_TYPE = "";
            }
            
            if(order.getNpCustomerState() != null){
               strCUST_STATE = order.getNpCustomerState();
            }else{
               strCUST_STATE = "";
            }
            
            if(order.getNpSuspensionPhone() != null){
               strSUSPENSION_PHONE = order.getNpSuspensionPhone();
            }else{
               strSUSPENSION_PHONE = "";
            }
            
            if(order.getNpSuspensionState() != null){
               strSUSPENSION_STATE = order.getNpSuspensionState();
            }else{
               strSUSPENSION_STATE = "";
            }
            
            if(order.getNpGiro() != null){
               strGIRO = order.getNpGiro();
            }else{
               strGIRO = "";
            } 
            
            if(order.getTOTAL_ITEMS() != null){
               strTOTAL_ITEMS = order.getTOTAL_ITEMS();
            }else{
               strTOTAL_ITEMS = "";
            }
            
            if(order.getST_SUSPENSION() != null){
               strST_SUSPENSION = order.getST_SUSPENSION();
            }else{
               strST_SUSPENSION = "";
            }
            
            if(order.getTOTAL_ITEMS() != null){
               strTOTAL_ITEMS = order.getTOTAL_ITEMS();
            }else{
               strTOTAL_ITEMS = "";
            }
            
            if(order.getNPCLOSEDDATE()!= null){
               strNPCLOSEDDATE = order.getNPCLOSEDDATE();
            }else{
               strNPCLOSEDDATE = "";
            }                        

           %>
            <tr>
              <td class="CellContent" align="Left" width="5%" ><font face="arial"><%=strBSCS_ID%></font></td>
              <td class="CellContent" align="Right" width="7%" ><font face="arial"><%=strCUSTOMER_ID%></font></td>                                          
              <td class="CellContent" align="Left" width="6%" nowrap ><font face="arial"><%=strNAME%></font></td>
              <td class="CellContent" align="Left" width="6%" style="mso-number-format:\@"><font face="arial" ><%=strCYCLE%></font></td>
              <td class="CellContent" align="Left" width="7%" style="mso-number-format:\@"><font face="arial"><%=strCREATE_SUS_DATE%></font></td>
              <td class="CellContent" align="Left" width="7%" ><font face="arial"><%=strUSER_CREATE%></font></td>              
              <td class="CellContent" align="Left" width="7%" style="mso-number-format:\@"><font face="arial"><%=strNPCLOSEDDATE%></font></td>                            
              <td class="CellContent" align="Left" width="6%" ><font face="arial"><%=strSCORING_CUST%></font></td>
              <td class="CellContent" align="Left" width="7%" ><font face="arial"><%=strCUST_TYPE%></font></td>
              <td></td>
              <td class="CellContent" align="Left" width="8%" ><font face="arial"><%=strST_SUSPENSION%></font></td>
              <td class="CellContent" align="Left" width="7%" nowrap ><font face="arial"><%=strGIRO%></font></td>              
              <td></td>
              <td class="CellContent" align="Left" width="6%" ><font face="arial"><%=strTOTAL_ITEMS%></font></td>
            </tr>
        <%}%>
      <%}%>
  </table>          
  <P>&nbsp;</P>
</form>
<%
} catch(SessionException se) {
  se.printStackTrace();
  System.out.println("[reportClaims.jsp][Finalizó la sesión]");
  out.println("<script>alert('Su sesión ha finalizado. Ingrese nuevamente');</script>");
  String strUrl = "/portal/pls/portal/WEBSALES.NP_USER_AVAILABILITY_PL_PKG.PL_LOGOUT";
} catch(Exception e) {
  String strMessageExceptionGeneralStart = "";
  strMessageExceptionGeneralStart = MiUtil.getMessageClean(e.getMessage());
  out.println("<script>alert('"+strMessageExceptionGeneralStart+"');</script>");   
}
%>
