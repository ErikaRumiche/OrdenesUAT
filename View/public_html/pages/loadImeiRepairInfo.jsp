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
		
		<%	
      HashMap hshImeiDetailMap = (HashMap) request.getAttribute("hshImeiDetailMap");
			String index = (String) request.getAttribute("index");
      String strTechnology = (String) request.getAttribute("strTechnology");
      String strTechnologyItem = (String) request.getAttribute("strTechnologyItem");
			String strTxtSerieLista = (String) hshImeiDetailMap.get("strSerie");
      
			if(StringUtils.isNotEmpty(index)) {
				if(strTxtSerieLista!=null) {
            if (strTechnology.compareTo(strTechnologyItem) == 0){
		%>	
                try {
                  parent.mainFrame.document.frmdatos.txtSerieLista[<%=index%>].value = "<%=strTxtSerieLista.toString()%>";
                } 
                catch(exception) {
                  parent.mainFrame.document.frmdatos.txtSerieLista.value = "<%=strTxtSerieLista.toString()%>";
                }
		<%		  } else{%>
                alert("El IMEI proporcionado no es de la misma tecnología.");
                try{
                  parent.mainFrame.document.frmdatos.txtImeiLista[<%=index%>].value = "";
                }catch(exception){
                  parent.mainFrame.document.frmdatos.txtImeiLista.value = "";
                }
                try{
                  parent.mainFrame.document.frmdatos.txtImeiLista[<%=index%>].focus();
                }catch(exception){
                  parent.mainFrame.document.frmdatos.txtImeiLista.focus();
                }
		<%
            }
          
				} else {%>
					alert("El IMEI proporcionado no es válido");
					try{
						parent.mainFrame.document.frmdatos.txtImeiLista[<%=index%>].value = "";
					}catch(exception){
						parent.mainFrame.document.frmdatos.txtImeiLista.value = "";
					}
					try{
						parent.mainFrame.document.frmdatos.txtImeiLista[<%=index%>].focus();
					}catch(exception){
						parent.mainFrame.document.frmdatos.txtImeiLista.focus();
					}
		<%	 	
				} // fin strTxtSerieLista!=null
			}	  // fin index
		%>
		
		function fxCleanImeiInfo() {
			try {
				parent.mainFrame.document.frmdatos.txtSerieLista[<%=index%>].value = "";
			} catch(exception) {
				parent.mainFrame.document.frmdatos.txtSerieLista.value = "";
			}
		}
    
	</script>
	</head>
	<body bgcolor="#AABBCC">
	</body>
</html>