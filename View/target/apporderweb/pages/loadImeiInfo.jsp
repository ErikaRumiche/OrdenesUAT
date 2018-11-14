<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.HashMap"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="pe.com.nextel.service.GeneralService"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
    <script type="text/javascript">
        
		fxCleanImeiInfo();
		
    <%  HashMap hshDataMap = (HashMap) request.getAttribute("hshDataMap");
		String strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
		String strIndex = (String) request.getAttribute("index");
		System.out.println("En el loadImeiInfo.jsp");		 
		System.out.println("strIndex->"+strIndex);
        if(StringUtils.isNotBlank(strIndex)) {
			String strSerialNumber = MiUtil.defaultString(hshDataMap.get("serialNumber"), "");
			String strEquipmentType = MiUtil.defaultString(hshDataMap.get("equipmentType"), "");
			System.out.println("hshDataMap::: "+hshDataMap);
			
			if(StringUtils.isBlank(strMessage)) {
			
    %>    		
				if (parent.mainFrame.document.getElementById("txtIMEI"+"<%=strIndex%>").value==""){
					parent.mainFrame.document.getElementById("cmbKit"+"<%=strIndex%>").disabled = true;	
				}
				else{
					try {
						parent.mainFrame.document.getElementById("txtSIM<%=strIndex%>").value = "<%=strSerialNumber%>";
						parent.mainFrame.document.getElementById("txtModelo<%=strIndex%>").value = "<%=strEquipmentType%>";
					} catch(exception) {
						//alert(exception.description);
						//parent.mainFrame.document.frmdatos.txtSIM.value = "<%=strSerialNumber%>";
						//parent.mainFrame.document.frmdatos.txtModelo.value = "<%=strEquipmentType%>";
						parent.mainFrame.document.getElementById("txtSIM").value = "<%=strSerialNumber%>";
						parent.mainFrame.document.getElementById("txtModelo").value ="<%=strEquipmentType%>";
					}
					parent.mainFrame.document.getElementById("hdnItem<%=strIndex%>").value = <%=Integer.parseInt(strIndex)-1%>;
					parent.mainFrame.document.getElementById("cmbKit<%=strIndex%>").disabled = false;
					parent.mainFrame.document.getElementById("cmbKit<%=strIndex%>").focus();
				}
    <%		} else {	
	%>			
				if (parent.mainFrame.document.getElementById("txtIMEI"+"<%=strIndex%>").value==""){
					parent.mainFrame.document.getElementById("cmbKit"+"<%=strIndex%>").disabled = true;	
				}
				else{	
					alert("El IMEI proporcionado no es válido");
					parent.mainFrame.fxResetRowDetail(null, <%=strIndex%>);
					parent.mainFrame.document.getElementById("txtIMEI<%=strIndex%>").select();
					parent.mainFrame.document.getElementById("txtIMEI<%=strIndex%>").focus();	
				}
	<%		}
		}
    %>
    
		function fxCleanImeiInfo() {
			try {
				parent.mainFrame.document.getElementById("txtSIM<%=strIndex%>").value="";
				parent.mainFrame.document.getElementById("txtModelo<%=strIndex%>").value="";
				//parent.mainFrame.document.frmdatos.txtSIM[<%=strIndex%>].value = "";
				//parent.mainFrame.document.frmdatos.txtModelo[<%=strIndex%>].value = "";
			}
			catch(exception) {
				parent.mainFrame.document.getElementById("txtSIM").value="";
				parent.mainFrame.document.getElementById("txtModelo").value="";			
				//parent.mainFrame.document.frmdatos.txtSIM.value = "";
				//parent.mainFrame.document.frmdatos.txtModelo.value = "";
			}
		}
    </script>
  </head>
  <body bgcolor="#AABBCC">
  </body>
</html>