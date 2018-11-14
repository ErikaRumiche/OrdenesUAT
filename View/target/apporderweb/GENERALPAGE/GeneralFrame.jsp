<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.util.*" %>
<%
    String strUrl=MiUtil.getString(request.getParameter("sUrl"));
    String strTitulo=MiUtil.getString(request.getParameter("sTitle"));   
    strUrl=strUrl.replace('¿','?');       
    strUrl=strUrl.replace('|','&');           
%>
<HTML>
<head>
   <title><%=strTitulo%></title>
   <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <script language="JavaScript">
      try {
        if(window.name=='Responsable_Pago') {
          window.onbeforeunload = confirmExit;
          
          function confirmExit() {
            return "¿Verificó que existen las Direcciones de Facturación y Entrega y los Contactos de Usuario y Facturación?";	
          }
        }
        
       /*if(window.name=='Ventas_Data') {
          window.onbeforeunload = confirmExit;
          
          function confirmExit() {
            return "¿Verificó que se ingresaron Aplicaciones de Venta Data?";	
          }
       }*/
      }
      catch(exception) {
      }
    </script>   
</head>
<FRAMESET border=0 frameSpacing=0 rows=99%,* frameBorder=NO>
  <FRAME name=mainFrame src='<%=strUrl%>' noResize>
  <FRAME name=bottomFrame src="<%=Constante.PATH_APPORDER_SERVER%>/Bottom.html" noResize scrolling=no>
</FRAMESET>
</HTML>