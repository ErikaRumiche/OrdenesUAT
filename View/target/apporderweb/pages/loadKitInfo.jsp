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
        fxCleanKitInfo();
    <%  GeneralService objGeneralService = new GeneralService();
		HashMap hshKitDetailMap = (HashMap) request.getAttribute("hshKitDetailMap");
		String index = (String) request.getAttribute("index");
		HashMap hshKitInfo = objGeneralService.getKitInfo(hshKitDetailMap);
        if(StringUtils.isNotBlank(index)) {
    %>    
			parent.mainFrame.document.getElementById("txtEquipo<%=index%>").value = '<%=hshKitInfo.get("valorEquipo")%>';
			parent.mainFrame.document.getElementById("txtServicio<%=index%>").value = '<%=hshKitInfo.get("valorServicio")%>';
			parent.mainFrame.document.getElementById("hdnPlanTarifario<%=index%>").value = '<%=hshKitInfo.get("planTarifarioId")%>';
			parent.mainFrame.document.getElementById("txtPlanTarifario<%=index%>").value = '<%=hshKitInfo.get("planTarifarioNombre")%>';
			/*Inicio - CEM*/
			if (parent.mainFrame.document.getElementById("txtPlanTarifario<%=index%>").value != "" &&
				parent.mainFrame.document.getElementById("txtEquipo<%=index%>").value != ""		 &&
				parent.mainFrame.document.getElementById("txtServicio<%=index%>").value != ""
			)							
				parent.mainFrame.document.frmdatos.btnProcesar.disabled = false;					
			else{							
				parent.mainFrame.document.frmdatos.btnProcesar.disabled = true;
				parent.mainFrame.document.getElementById("cmbKit<%=index%>").focus();
				if (parent.mainFrame.document.getElementById("txtPlanTarifario<%=index%>").value=="")
					alert("El kit seleccionado no cuenta con información del plan tarifario");					
				else if (parent.mainFrame.document.getElementById("txtEquipo<%=index%>").value=="")
					alert("El kit seleccionado no cuenta con información del servicio");					
				else if (parent.mainFrame.document.getElementById("txtServicio<%=index%>").value=="")
					alert("El kit seleccionado no cuenta con información del equipo");						
			}
			/*Fin - CEM*/		
    <%
        }
    %>
    
		function fxCleanKitInfo() {
			parent.mainFrame.document.getElementById("txtEquipo<%=index%>").value = "";
			parent.mainFrame.document.getElementById("txtServicio<%=index%>").value = "";
			parent.mainFrame.document.getElementById("hdnPlanTarifario<%=index%>").value = "";
			parent.mainFrame.document.getElementById("txtPlanTarifario<%=index%>").value = "";
		}
    
    </script>
  </head>
  <body bgcolor="#AABBCC">
  </body>
</html>