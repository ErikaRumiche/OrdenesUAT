<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.HashMap"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="pe.com.nextel.service.GeneralService"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
		<script type="text/javascript">
			
		<%String index = (String) request.getAttribute("index");
      String strMessage = (String) request.getAttribute("strMessage");
			String strStatusSim = (String) request.getAttribute("strStatusSim");
      
			if(StringUtils.isNotEmpty(strMessage)) {
    
    %>
					alert("El SIM proporcionado no es válido");
          try{
						parent.mainFrame.document.frmdatos.txtSimLista[<%=index%>].value = "";
					}catch(exception){
						parent.mainFrame.document.frmdatos.txtSimLista.value = "";
					}
					try{
						parent.mainFrame.document.frmdatos.txtSimLista[<%=index%>].focus();
					}catch(exception){
						parent.mainFrame.document.frmdatos.txtSimLista.focus();
					}
		<%	 	
        
      }	  // fin strMessage
		%>

	</script>
	</head>
	<body bgcolor="#AABBCC">
	</body>
</html>