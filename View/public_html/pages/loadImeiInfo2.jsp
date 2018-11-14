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
        fxCleanImeiInfo();
		
    <%  HashMap hshImeiDetailMap = (HashMap) request.getAttribute("hshImeiDetailMap");
		String index = (String) request.getAttribute("index");
        if(StringUtils.isNotEmpty(index)) {
			String strTxtSerieLista = (String) hshImeiDetailMap.get("wv_serie");
			if(strTxtSerieLista!=null) {
    %>    
			try {
				parent.mainFrame.document.frmdatos.txtSerieLista[<%=index%>].value = "<%=strTxtSerieLista.toString()%>";
			}
			catch(exception) {
				parent.mainFrame.document.frmdatos.txtSerieLista.value = "<%=strTxtSerieLista.toString()%>";
			}
    <%
			} else {
	%>
			alert("El IMEI proporcionado no es válido");
	<%
			}
		}
    %>
    
		function fxCleanImeiInfo() {
			try {
				parent.mainFrame.document.frmdatos.txtSerieLista[<%=index%>].value = "";
			}
			catch(exception) {
				parent.mainFrame.document.frmdatos.txtSerieLista.value = "";
			}
		}
    
    </script>
  </head>
  <body bgcolor="#AABBCC">
  </body>
</html>