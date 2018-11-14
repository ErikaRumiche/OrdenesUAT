<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="java.util.Hashtable" %>
<%
  Hashtable hshtinputNewSection = null;
  String    strCodigoCliente= "";
  String strObjectReadOnly = "",strObjectHeaderId ="";
  
  hshtinputNewSection     = (Hashtable)request.getAttribute("hshtInputNewSection");
  strObjectReadOnly = (String)request.getParameter("strObjectReadOnly");
  strObjectHeaderId = (String)request.getParameter("strObjectHeaderId");
  if ( hshtinputNewSection != null )
    strCodigoCliente        =   (String)hshtinputNewSection.get("strCodigoCliente");
  
%>
<tr>
  <td class="CellLabel" align="left" colspan="1">&nbsp;<font color="red">*</font>&nbsp;&nbsp;Contacto&nbsp;</td>
  <td class="CellContent">&nbsp;
  <input type="text" name="cmb_ItemContact" value="" size="40">
  <%if(strObjectReadOnly.equals("N")){%>
    <a href="javascript:fxShowContact();" id="idImgVD<%=strObjectHeaderId%>" onmouseover="window.status='Ver Detalle'; return true" onmouseout="window.status='';" >
    <img src="<%=Constante.PATH_APPORDER_SERVER%>/images/viewDetail.gif" width="15" height="15" border="0"></a>
  <%}%>    
  </td>
</tr>

<script>
 function fxShowContact(){
     var url = "<%=request.getContextPath()%>/htdocs/jsp/Order_ContactList.jsp?codCustomer=<%=strCodigoCliente%>";
     WinAsist = window.open(url,"WinAsist1","toolbar=no,location=no,directories=no,status=yes,menubar=0,scrollbars=yes,resizable=yes,screenX=100,top=80,left=100,screenY=80,width=450,height=300,modal=yes");
 }
</script>