<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.*"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
    <script type="text/javascript">

    //parent.mainFrame.document.frmdatos.txtNextelDiagnostico.value='';
    strOldDescription =   parent.mainFrame.document.frmdatos.txtNextelDiagnostico.value;
               
    <%    
		HashMap hshDataMap = (HashMap) request.getAttribute(Constante.DATA_STRUCT);    
          
    //String strDiagnosisDescription = (String) hshDataMap.get("strDiagnosisDescription");    
    String strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
        
    String strNewDescription = (String) hshDataMap.get("strDiagnosisDescription");    
        
		if(StringUtils.isBlank(strMessage)) {%>
      parent.mainFrame.document.frmdatos.txtNextelDiagnostico.value = strOldDescription + "\n" + '<%=strNewDescription%>';<%
    }%>    
    
    
    
    </script>
  </head>
  <body bgcolor="#AABBCC">
  </body>
</html>