<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<html>
  <head>
    <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
  </head>
<body bgcolor="#AABBCC">
<%
String strMessage=(String)request.getAttribute(Constante.MESSAGE_OUTPUT);
%>
<script language="javascript" defer>
        fxShowValidationPropuesta('<%=strMessage%>');
</script>
<script language="javascript">    
    function fxShowValidationPropuesta(display){
       var form      = parent.mainFrame.frmdatos; 
       parent.mainFrame.fxValidaciondePropuestas(display);
    }
  </script>
</body>
</html>
