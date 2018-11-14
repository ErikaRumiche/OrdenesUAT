<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.service.CustomerService" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="pe.com.nextel.bean.ContactObjectBean" %>
<%
  CustomerService objCustomerService = new CustomerService();
  String strCodigoCliente = "";

  strCodigoCliente        =   MiUtil.getString((String)request.getParameter("codCustomer"));
  
  HashMap objHashMap = objCustomerService.getCustomerContactsByType(MiUtil.parseLong(strCodigoCliente),"S");
  
  if( objHashMap.get("strMessage")!=null ) throw new Exception((String)objHashMap.get("strMessage"));
  
  ArrayList objArrayList = (ArrayList)objHashMap.get("objContactObjectBean");
  
  ContactObjectBean objContactObjectBean = null;

%>
      <head>
         <title>Listado de Contactos</title>
      </head>
      <link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css">
      <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
      <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
      <script language="javascript">
         parent.document.title = "Portal Nextel";
         window.focus();

         function fxCloseWindow(){
            parent.close();
         }

         function fxSelectContact(fullName, phone) {
            var form = parent.opener.parent.mainFrame.document.frmdatos;
            form.cmb_ItemContact.value        = fullName;
            //form.txtPhonePrincipal.value = phone;
            parent.close();
         }
      </script>

      <table border="0" width="100%" cellpadding="0" cellspacing="1" class="RegionBorder">
         <form name="frmdatoslist" id="frmdatoslist" method="post" target="mainFrame">
            <tr>
            <table border="0" class="PortletHeaderColor" cellspacing="0" cellpadding="0" width="100%">
               <tr class="PortletHeaderColor">
                  <td class="LeftCurve" valign="top" align="left" width="10">&nbsp;&nbsp;</td>
                  <td class="PortletHeaderColor" align="left" valign="top">
                     <font class="PortletHeaderText">Listado de Contactos</font></td>
                  <td align="right" class="RightCurve" width="10%" >&nbsp;&nbsp;</td>
               </tr>
            </table>
            </tr>
            <tr>
               <table border="1" width="100%" cellpadding="2" cellspacing="0" class="RegionBorder" align="center" >
                  <tr>
                     <td width="100%"class="RegionHeaderColor">
                        

                        <table border="0" width="95%" cellpadding="0" cellspacing="1" class="RegionBorder" align="center">
                           <!-- Listado Resumen de Servicios -->
                          <tr>
                              <td align="center" class="ListRow0"><b>&nbsp;&nbsp;</b></td>
                              <td align="center" class="ListRow0"><b>&nbsp;Nombres&nbsp;</b></td>
                              <td align="center" class="ListRow0"><b>&nbsp;Apellidos&nbsp;</b></td>
                              <td align="center" class="ListRow0"><b>&nbsp;Teléfono&nbsp;</b></td>
                           </tr>
                           
                           <%
                           if ( objArrayList != null && objArrayList.size() > 0 ){
                             for( int i=0; i<objArrayList.size();i++ ){
                                  objContactObjectBean = new ContactObjectBean();
                                  objContactObjectBean = (ContactObjectBean)objArrayList.get(i);
                           %>
                              <tr>
                                 <td align="center" class="ListRow1">
                                 <%
                                 String strFullName = objContactObjectBean.getSwfirstname() + " " + objContactObjectBean.getSwlastname();
                                 %>
                                    <a href="javascript:fxSelectContact('<%=strFullName%>','')">
                                    <img src="<%=Constante.PATH_APPORDER_SERVER%>/images/viewDetail.gif" width="15" height="15" border="0"></a><%=(i+1)%>
                                 </td>
                                 <td align="left" class="ListRow1">&nbsp;<%=objContactObjectBean.getSwfirstname()%>&nbsp;</td>
                                 <td align="left" class="ListRow1">&nbsp;<%=objContactObjectBean.getSwlastname()%>&nbsp;</td>
                                 <td align="left" class="ListRow1">&nbsp;&nbsp;</td>
                              </tr>
                           <%
                              }
                           }
                           %>

                        </table>
                        <br/>
                        <!-- Fin de Listado -->

                        <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
                   	      <tr align="center">
                       	      <td width="45%">&nbsp;</td>                 	
                              <td width="10%">                 	
                                 <input type="button" name="btnCerrar" value="Cerrar" onclick="javascript:fxCloseWindow();">
                              </td>
                       	      <td width="45%">&nbsp;</td>
                           </tr>
                  	   </table>
                     </td>
                  </tr>
            	</table>
            </tr> 	
            </form>
         </table>
