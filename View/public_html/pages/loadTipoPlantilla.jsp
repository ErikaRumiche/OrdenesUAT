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
    <%
    String strFlagTipoAdend = (String)request.getAttribute("strFlagTipoAdend");
    String txtToSet = (String)request.getAttribute("txtToSet");
    %>
    var strFlag = <%=strFlagTipoAdend%>;
    if(strFlag == "0"){
      parent.mainFrame.document.getElementById("<%=txtToSet%>").innerText = '<%=Constante.TIPO_PLANTILLA_NUEVA%>';
      parent.mainFrame.document.getElementById("<%=txtToSet%>").className = 'TemplateLabelNew';
    }else{
      parent.mainFrame.document.getElementById("<%=txtToSet%>").innerText = '<%=Constante.TIPO_PLANTILLA_RENOVACION%>';
      parent.mainFrame.document.getElementById("<%=txtToSet%>").className = 'TemplateLabelRenov';
    }    
    location.replace("<%=Constante.PATH_APPORDER_SERVER%>/Bottom.html");
	</script>
	</head>
</html>