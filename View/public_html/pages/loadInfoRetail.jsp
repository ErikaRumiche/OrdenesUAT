<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.bean.DominioBean"%>
<%@ page import="pe.com.nextel.bean.PortalSessionBean" %>
<%@ page import="pe.com.nextel.service.GeneralService"%>
<%@ page import="pe.com.nextel.service.RetailService"%>
<%@ page import="pe.com.nextel.service.SessionService" %>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%	
	HashMap hshDataMap              = (HashMap) request.getAttribute(Constante.DATA_STRUCT);
	String strOrderId               = MiUtil.defaultString(hshDataMap.get("orderId"),"");
  String strStatusExecution       = MiUtil.defaultString(hshDataMap.get("statusProcess"),"");
	String strCustCodeBSCS          = MiUtil.defaultString(hshDataMap.get("strCustCodeBSCS"),"");
	ArrayList arrContractInfoList   = (ArrayList) hshDataMap.get("arrContractInfoList");
	String strMessage               = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
	String strMessageCustCodeBSCS   =	(String) hshDataMap.get("strMessageCustCodeBSCS");	
	String strMessageCreateContract =	(String) hshDataMap.get("strMessageCreateContract");	
  String strMessageEx             =	(String) hshDataMap.get("strMessageEx");
  String strMessInsNotAct         =	(String) hshDataMap.get("strMessageinsNotificationAction");
  String strMessGetInfoCust       =	(String) hshDataMap.get("strMessagegetInfoCustomer");

	System.out.println("Inicio de loadInfoRetail.jsp");
	System.out.println("strOrderId --> " + strOrderId);
	System.out.println("strCustCodeBSCS --> " + strCustCodeBSCS);
	System.out.println("strMessage --> " + strMessage);
	System.out.println("strMessageCustCodeBSCS --> " + strMessageCustCodeBSCS);
  System.out.println("strMessGetInfoCust --> " + strMessGetInfoCust);	  
	System.out.println("strMessageCreateContract --> " + strMessageCreateContract);
  System.out.println("strMessageEx --> " + strMessageEx);
	System.out.println("strMessInsNotAct --> " + strMessInsNotAct);	
%>
<head>
	<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
	<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
	<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
	<script type="text/javascript">
				
		<%
		if(StringUtils.isNotBlank(strMessage)){
		%>
			alert('<%="Hubo un error al crear la orden: "+MiUtil.getMessageClean(strMessage)%>');
			parent.mainFrame.document.frmdatos.btnProcesar.disabled = false;		
	<%}else{%>		
		<%if(StringUtils.isNotBlank(strStatusExecution)){
          if(strStatusExecution.equals(Constante.NEW_ORDER_SOLICITUD_EXISTE)) {%>
		 			  alert("El Nro. de Solicitud que Ud. ha ingresado ya existe. Por favor, corregir");
             parent.mainFrame.document.frmdatos.btnProcesar.disabled = false; /*CEM*/
             parent.mainFrame.document.frmdatos.txtNroSolicitud.select();
             parent.mainFrame.document.frmdatos.txtNroSolicitud.focus();
		      <%}         
          else if(strStatusExecution.equals(Constante.NEW_ORDER_FLUJO_NORMAL)) {
             if(StringUtils.isNotBlank(strOrderId)){
          %>		parent.mainFrame.document.frmdatos.btnProcesar.disabled = true; /*CEM*/
                alert("Se creó la Orden Nº <%=strOrderId%>");
                parent.mainFrame.document.frmdatos.txtCodigoBSCS.value = "<%=strCustCodeBSCS%>";
                parent.mainFrame.document.frmdatos.txtOrden.value = "<%=strOrderId%>";
            <%if(StringUtils.isNotBlank(strMessageCustCodeBSCS)){%>
                    alert('<%=MiUtil.showCleanAlerts(MiUtil.getMessageClean(strMessageCustCodeBSCS))%>');
             <%}else if(StringUtils.isNotBlank(strMessGetInfoCust)){%>
                    alert('<%=MiUtil.showCleanAlerts(MiUtil.getMessageClean(strMessGetInfoCust))%>');
             <%}else if(StringUtils.isNotBlank(strMessageCreateContract)){%>
                    alert('<%=MiUtil.showCleanAlerts(MiUtil.getMessageClean(strMessageCreateContract))%>');
             <%}else if(StringUtils.isNotBlank(strMessageEx)){%>
                     alert('<%=MiUtil.showCleanAlerts(MiUtil.getMessageClean(strMessageEx))%>');
             <%}else if(StringUtils.isNotBlank(strMessInsNotAct)){%>
                    alert('<%=MiUtil.showCleanAlerts(MiUtil.getMessageClean(strMessInsNotAct))%>');
                <%} 
                if(arrContractInfoList!=null) {
                    Iterator itContractInfo = arrContractInfoList.iterator();
                    while(itContractInfo.hasNext()) {
                       HashMap hshContractInfoMap = (HashMap) itContractInfo.next();
            %>	       parent.mainFrame.document.frmdatos.txtContrato[<%=hshContractInfoMap.get("hdnItem")%>].value = '<%=hshContractInfoMap.get("lContratoId")%>';
                       parent.mainFrame.document.frmdatos.txtNextel[<%=hshContractInfoMap.get("hdnItem")%>].value = '<%=hshContractInfoMap.get("strTelefono")%>';                        
            <%		  }
                 }
					   }else{
				%>		  alert('<%="Hubo un error al crear la orden: "+MiUtil.getMessageClean(strMessage)%>');
                parent.mainFrame.document.frmdatos.btnProcesar.disabled = false;
				<%	 }
				  }
			}
		}
		%>
		</script>
	</head>
	<body bgcolor="#AABBCC">
	</body>
</html>