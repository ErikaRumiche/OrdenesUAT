<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.*"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
    <script type="text/javascript">

    <%
		String strMessage = (String) request.getAttribute(Constante.MESSAGE_OUTPUT);
    String strPagina = (String) request.getAttribute("strPagina");

    if(StringUtils.isNotBlank(strMessage)){%>
      var strMensaje = '<%= strMessage%>';
      alert(strMensaje);

      <%if(StringUtils.isNotBlank(strPagina)){%>
          var url = '<%= strPagina%>';
          parent.mainFrame.location.href = url;
          parent.bottomFrame.location.replace(url);
      <%}%>      
    <%}%>
    </script>
  </head>
  <body bgcolor="#AABBCC">
  </body>
</html>