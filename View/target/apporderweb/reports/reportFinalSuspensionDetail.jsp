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
  String strTypeSuspension = "";  
  
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
  String strCO_ID = "";
  String strCategoriaOrden = "";
  String strStatusBSCS = "";
  String strMotivoBSCS = "";
  String strEQUIPO = "";
  
   OrderSearchService objOrderSearch = new OrderSearchService();
   strMessage = null;

%>

  <table cellspacing="3" cellpadding="2" border="1" width="100%" align="center">
    <tr>      
      <td class="CellLabel" >CUSTCODE</td>
      <td class="CellLabel" >CUSTOMER_ID</td>
      <td class="CellLabel" >CCNAME</td>
      <td class="CellLabel" >CICLO</td>
      <td class="CellLabel" >CREATED_DATE</td>
      <td class="CellLabel" >CREATED_USER</td>
      <td class="CellLabel" >SUSPENDED_DATE</td>
      <td class="CellLabel" >SCORING</td>
      <td class="CellLabel" >TYPE_CUSTCODE</td>
      <td class="CellLabel" >STATUS</td>
      <td class="CellLabel" >STATUS_DES</td>
      <td class="CellLabel" >GIRO</td>
      <td class="CellLabel" >SUSPENDEDCUID</td>
      <td class="CellLabel" >EQUIPOS</td>
      <td class="CellLabel" >CONTRACT_NUMBER</td>
      <td class="CellLabel" >CATEGORIA_ORDEN</td>
      <td class="CellLabel" >ESTADO_BSCS</td>
      <td class="CellLabel" >MOTIVO_BSCS</td>
    </tr>
    
    <%
             
      HashMap hshResultado = new HashMap();
      HashMap hshParameters = new HashMap();
            
      strFechaDesde = StringUtils.defaultString(request.getParameter("ad_start_date"));
      strFechaHasta = StringUtils.defaultString(request.getParameter("ad_end_date"));      
      strTypeSuspension = StringUtils.defaultString(request.getParameter("strTypeSuspension")); 
      
      hshParameters.put("ad_start_date", strFechaDesde);
      hshParameters.put("ad_end_date", strFechaHasta);      
      hshParameters.put("wv_TypeSuspen", strTypeSuspension);      
      
        hshResultado = objOrderSearch.getfinalSuspDetailList(hshParameters);
      strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT); 
      
      if (StringUtils.isBlank(strMessage)){
      
        list = (ArrayList)hshResultado.get("objArrayList");
        Iterator iteratorList = list.iterator();        
        int intFila = 0;

          while((iteratorList.hasNext()) ) {
            intFila++;
            //Falta recorrer el iterator....
            FinalSuspensionBean order = (FinalSuspensionBean)iteratorList.next();
            
            strCUSTOMER_ID      = order.getNpCustomerId();
            strBSCS_ID          = order.getNpBscsId();
            strNAME             = order.getNpName();
            strCYCLE            = order.getNpCycle();
            strCREATE_SUS_DATE  = order.getNpCreateSupDate();
            strUSER_CREATE      = order.getNpCreatedBy();
            strSUSPENSION_DATE = order.getNpSuspensionDate();
            strSCORING_CUST     = order.getNpScoringCust();
            strCUST_TYPE        = order.getNpCustomerType();
            strCUST_STATE       = order.getNpCustomerState();
            strSUSPENSION_PHONE = order.getNpSuspensionPhone();
            strSUSPENSION_STATE = order.getNpSuspensionState();
            strGIRO             = order.getNpGiro();
            //strTOTAL_ITEMS      = order.getTOTAL_ITEMS(); // # de telefono
            strEQUIPO           = order.getTOTAL_ITEMS(); // # de telefono
            strST_SUSPENSION    = order.getST_SUSPENSION();
            strTOTAL_ITEMS      = order.getTOTAL_ITEMS();
            strNPCLOSEDDATE     = order.getNPCLOSEDDATE();
            strCO_ID            = order.getCO_ID();
            strCategoriaOrden   = order.getNpCategoriaOrden();
            strStatusBSCS       = order.getStatusBSCS();
            strMotivoBSCS       = order.getNpMotivoBSCS();
                  
           %>
            <tr>
              <td class="CellContent" ><%=strBSCS_ID%></td>
              <td class="CellContent" ><%=strCUSTOMER_ID%></td>                                          
              <td class="CellContent" nowrap ><%=strNAME%></td>
              <td class="CellContent" style="mso-number-format:\@"><%=strCYCLE%></td>
              <td class="CellContent" style="mso-number-format:\@"><%=strCREATE_SUS_DATE%></td>
              <td class="CellContent" ><%=strUSER_CREATE%></td>              
              <td class="CellContent" style="mso-number-format:\@"><%=strNPCLOSEDDATE%></td>                            
              <td class="CellContent" ><%=strSCORING_CUST%></td>
              <td class="CellContent" ><%=strCUST_TYPE%></td>
              <td></td>
              <td class="CellContent" ><%=strST_SUSPENSION%></td>
              <td class="CellContent" nowrap ><%=strGIRO%></td>              
              <td></td>
              <td class="CellContent" ><%=strEQUIPO%></td>
              <td class="CellContent" style="mso-number-format:\@"><%=strCO_ID%></td>
              <td class="CellContent" ><%=strCategoriaOrden%></td>
              <td class="CellContent" ><%=strStatusBSCS%></td>
              <td class="CellContent" ><%=strMotivoBSCS%></td>
            </tr>
        <%}%>
      <%}%>
  </table>          
  <P>&nbsp;</P>
</form>
<%
} catch(SessionException se) {
  se.printStackTrace();
  System.out.println("[reportFinalSuspensionDetail.jsp][Finalizó la sesión]");
  out.println("<script>alert('Su sesión ha finalizado. Ingrese nuevamente');</script>");
  String strUrl = "/portal/pls/portal/WEBSALES.NP_USER_AVAILABILITY_PL_PKG.PL_LOGOUT";
} catch(Exception e) {
  String strMessageExceptionGeneralStart = "";
  strMessageExceptionGeneralStart = MiUtil.getMessageClean(e.getMessage());
  out.println("<script>alert('"+strMessageExceptionGeneralStart+"');</script>");   
}
%>
