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
      HashMap hshDataMap = (HashMap) request.getAttribute("objResultado");
		  String strMessage = (String) hshDataMap.get("strMessage");           
      %> 
            
      alert("Se nacionalizó correctamente el equipo");
      
    </script>
  </head>
  <body bgcolor="#AABBCC">
  </body>
</html>