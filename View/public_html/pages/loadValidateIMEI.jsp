<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript">
      <%
        String strMessage = (String)request.getAttribute(Constante.MESSAGE_OUTPUT);
        
        if(!StringUtils.isBlank(strMessage)){
      %>
          var strMensaje = '<%=strMessage%>';
          alert(strMensaje);
          parent.mainFrame.document.getElementById("txt_ItemIMEI_Cliente").focus()
      <%}%>
      
    </script>
  </head>
  <body bgcolor="#AABBCC">
  </body>
</html>
