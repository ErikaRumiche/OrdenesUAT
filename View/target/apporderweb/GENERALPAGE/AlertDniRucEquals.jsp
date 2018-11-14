<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.*"%>
<%@ page import="java.util.*"%>
<%@ page import="pe.com.nextel.util.*"%>
<%@ page import="pe.com.nextel.bean.*"%>
<%@ page import="pe.com.nextel.service.CustomerService"%>
<%   
try{   
   String strNumDoc  = request.getParameter("strNumDoc");
   String strTipoDoc = request.getParameter("strTipoDoc");
   String strMessage  = null;
   HashMap hshData=null;
   HashMap hshDoc=null;
   ArrayList arrDocuments=null;
   
   CustomerService objCustomerService=new CustomerService(); 
   hshData=objCustomerService.getValidateDocument(strNumDoc,strTipoDoc);        
   strMessage=(String)hshData.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);   
         
   arrDocuments=(ArrayList)hshData.get("arrCustomer");
   
%>

<html>
   <head>
      <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
      <title>Alerta de Compañía</title>
   </head>
<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css">
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script>   
    
    /*function fxSelectedArea(npcity, npareacode){
      if (parent.opener!=null){
       parent.opener.document.formdatos.txtAreaCode.value=npareacode;
       parent.opener.spanAreaNom1.innerHTML ="("+npcity+")";     
       parent.opener.document.formdatos.txtNumTelefono.focus();
      }     
       parent.close();
    }*/ 

 </script>
 <body>
    <!-- Inicio de Marco -->
<TABLE  border="0" cellpadding="0" cellspacing="0" align="center" width="99%">
   <TR valign="top">
      <TD class="RegionHeaderColor">      
      <!-- Titulo Seccion -->
         <table border="0" class="PortletHeaderColor" cellspacing="0" cellpadding="0" width="99%" align="center">
            <tr class="PortletHeaderColor">
               <td class="LeftCurve" valign="top" align="left" width="10" >&nbsp;&nbsp;</td>
               <td class="PortletHeaderColor"align="LEFT" valign="top"> <font class="PortletHeaderText">Cliente Similar</font></td>
               <td align="right" class="RightCurve" width="10" >&nbsp;&nbsp;</td>
            </tr>
         </table> 
       <table border="0" cellspacing="1" cellpadding="1" align="center" width="99%" class="RegionBorder">
            <tr>
               <td class="ListRow0">&nbsp;<b>Número</b></td>
               <td class="ListRow0" align="center">&nbsp;<b>Razón Social</b></td>
            </tr>
            <%  for(int i=0; i<arrDocuments.size();i++) {                                  
                  hshDoc = (HashMap)arrDocuments.get(i);             
            %>
            <tr>
              <td class="CellContent" valign="top" ><%=MiUtil.getString((String)hshDoc.get("swruc"))%></td>
              <td class="CellContent" valign="top" ><%=MiUtil.getString((String)hshDoc.get("swname"))%></td>
            </tr>             
            <%}                      
             %>
        </table>            
      </TD>
   </TR>
</table>
  <!--Inicio Linea de separación -->
     <table border="0" cellspacing="0" cellpadding="0" width="99%" align="center" height="5">
        <tr align="center"><td></td></tr>
     </table>
  <!--Fin de Linea de separación -->
    <center><INPUT TYPE="BUTTON" VALUE="Cerrar" onclick="window.close();"></center>
 </body>
 </html>
<%}catch(Exception ex){
  System.out.println("Error try catch-->"+MiUtil.getMessageClean(ex.getMessage())); 
%>
<script>
  alert("<%=MiUtil.getMessageClean(ex.getMessage())%>");
</script>
<%
}%> 