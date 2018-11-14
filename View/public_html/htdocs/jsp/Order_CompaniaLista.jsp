<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.util.*"%>
<%@ page import="pe.com.nextel.service.CustomerService"%>
<%@ page import="pe.com.nextel.bean.DominioBean"%>
<%@ page import="java.util.*"%>
<%@page import="javax.servlet.http.HttpSession"%>
<%
    System.out.println("--------- ORDER_COMPANIALISTA.JSP ---------");
    System.out.println("Session: " + request.isRequestedSessionIdValid());
    HttpSession sessionActual = request.getSession(false);
    if (request.isRequestedSessionIdValid()){

      HashMap   hshCustomer = new HashMap();
      CustomerService objCustomerService = new CustomerService();
      ArrayList objArrayCustomer = (ArrayList)session.getAttribute("objArrayCustomerJava");
      String strLogin = (String)session.getAttribute("strLogin");
      String strRegionId  = (String)session.getAttribute("strRegionId");
      String strAppId  = (String)session.getAttribute("strAppId");
      String strUserId  = (String)session.getAttribute("strUserId");
      String strGeneratorTypeRepCalcap = (String)session.getAttribute("hdnOrigenType");
      String strGeneratorId     = (String)session.getAttribute("strGeneratorId");

      session.removeAttribute("objArrayCustomerJava");
      session.removeAttribute("strLogin");
      session.removeAttribute("strRegionId");
      session.removeAttribute("strAppId");
      session.removeAttribute("strUserId");

      String strCustomerId = "";
      String strCustomerName = "";
      String strCustomerRuc = "";
      String strCustomerRelationType = "";
      String strCustomerType  = "";

%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>SELECCION DE CLIENTE</title>
    <link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css">
    <script language="JavaScript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
    <script language="javascript">
       window.focus();
       function fxSelectCompanyId(idCompany){
        vDocForm  = opener.parent.mainFrame.parent.mainFrame.frmdatos;
        vDocForm.hdnCustId.value  = idCompany;
        vDocForm.myaction.value = 'buscarCliente';
        vDocForm.detallemyaction.value = 'id';
        vDocForm.submit();
        parent.close();
       }
    </script>
  </head>
  <body>
    <!-- Inicio de Marco -->
    <TABLE  border="1" cellpadding="0" cellspacing="0" align="center" class="RegionBorder" width="99%">
      <TR valign="top">
        <TD class="RegionHeaderColor">
          <!--Inicio Linea de separación -->
          <table border="0" cellspacing="2" cellpadding="0" width="80%" align="center" height="5">
            <tr align="center"><td></td></tr>
          </table>
          <!--Fin de Linea de separación -->
                  
          <!-- Titulo Seccion -->
          <table border="0" cellspacing="0" cellpadding="0" width="99%" align="center">
            <tr>
              <td class="SectionTitleLeftCurve" width="10">&nbsp;&nbsp;</td>
              <td class="SectionTitle">&nbsp;&nbsp;</td>
              <td class="SectionTitleRightCurve" width="8">&nbsp;</td>
            </tr>
          </table>
                  
          <!--Inicio Linea de separación -->
          <table border="0" cellspacing="0" cellpadding="0" width="99%" align="center" height="5">
            <tr align="center"><td></td></tr>
          </table>
          <!--Fin de Linea de separación -->                      

          <table border="0" cellspacing="1" cellpadding="1" align="center" width="99%">
            <tr>
              <td class="ListRow0">&nbsp;</td>
              <td class="ListRow0">&nbsp;<b>Ruc</b></td>
              <td class="ListRow0">&nbsp;<b>Razón Social</b></td>
              <td class="ListRow0">&nbsp;<b>Tipo Compa&ntilde;&iacute;a</b></td>
              <td class="ListRow0">&nbsp;<b>Tipo Cliente</b></td>
            </tr>
            <!--Filas del listado -->
<%
    for (int i= 1 ; i < objArrayCustomer.size(); i++){
      String idCustomer = ((DominioBean)objArrayCustomer.get(i)).getValor();
      hshCustomer = objCustomerService.CustomerDAOgetCustomerData( MiUtil.parseLong(idCustomer), strLogin, MiUtil.parseLong(""+strRegionId),MiUtil.parseInt(strUserId) , MiUtil.parseInt(strAppId),strGeneratorId,strGeneratorTypeRepCalcap);
      if ( hshCustomer!=null && hshCustomer.size() > 0){
         strCustomerId     = (String)hshCustomer.get("wn_swcustomerid");
         strCustomerName   = (String)hshCustomer.get("wv_swname");
         strCustomerRuc    = (String)hshCustomer.get("wv_swruc");
         strCustomerRelationType = (String)hshCustomer.get("wv_npcustomerrelationtype");
         strCustomerType           = (String)hshCustomer.get("wv_swtype"); 
      %>
         <tr>
            <td class="ListRow0" valign="top">
              <a href="javascript:fxSelectCompanyId(<%=strCustomerId%>)"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/viewDetail.gif" width="15" height="15" border="0"></a>
            </td>
            <td class="ListRow1" valign="top"><%=strCustomerRuc%></td>
            <td class="ListRow1" valign="top"><%=strCustomerName%></td>
            <td class="ListRow1" valign="top"><%=strCustomerType%></td>
            <td class="ListRow1" valign="top"><%=strCustomerRelationType%></td>
         </tr>
      <%
      }else ; //Lanzar excepción
      
    }
%>
          </table>
        </tr>
      </td>
    </table>
  </body>
</html>
<%
    }
    else{
        Date fecha = new Date();
        String ipAddress =  request.getRemoteAddr();
        System.out.println("INGRESO NO AUTORIZADO A ORDER_COMPANIALISTA.JSP");
        System.out.println("IP Address: "+ipAddress);
        System.out.println("Fecha: "+fecha);
        System.out.println("URL: "+request.getRequestURL()+(request.getQueryString() != null ? "?" + request.getQueryString() : ""));
        sessionActual.invalidate();
        System.out.println("REDIRECCIONANDO...");
        response.sendRedirect("/portal/pls/portal/NPR_PORTAL.wwsec_app_priv.login?p_requested_url=NPR_PORTAL.home");
        return;
    }

%>