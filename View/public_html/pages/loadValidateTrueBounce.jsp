<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="pe.com.nextel.bean.DominioBean"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
    <script type="text/javascript">
         
    <%
		HashMap hshDataMap = (HashMap) request.getAttribute("hshDataMap");      
      String strDiagnosisDescription = (String) hshDataMap.get("strValor");	
      String strMessage = (String) hshDataMap.get("strMessage");
            
      if(StringUtils.isNotBlank(strMessage)){%>      
         var strMensaje = '<%= strMessage%>';
         alert(strMensaje);      
      <%}
         if(strDiagnosisDescription!=null ) {%>               
            alert("<%=strDiagnosisDescription%>");      
            parent.mainFrame.document.frmdatos.txtGarantiaTrueBounce.value = 'Si';             
            parent.mainFrame.document.frmdatos.hdnGarantiaTrueBounce.value = 'S';      
      <%}%>                             
      
    </script>
  </head>
  <body bgcolor="#AABBCC">
  </body>
</html>