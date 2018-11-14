<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.HashMap"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="pe.com.nextel.service.GeneralService"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<html>
  <head>
    <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
    <script type="text/javascript">
		
    <%  HashMap hshDataMap = (HashMap) request.getAttribute(Constante.DATA_STRUCT);
        String strType         = (String) hshDataMap.get("strType");
        String strSubCategoria = (String) hshDataMap.get("strSubCategoria");
		String strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
		
	System.out.println("En el validateItemDevice.jsp");
    if ( strType.compareTo("IMEI")  == 0) {    
         if(StringUtils.isNotBlank(strMessage)) {
          %>		alert("Imei Inválido: <%=MiUtil.getMessageClean(strMessage)%>");		
      <% } else {
          %>
              fxCargaImeiOrders(parent.mainFrame.document.frmdatos.txtImeis);
          <%	
          }    
    }else if ( strType.compareTo("SIM") == 0 ){
      if(StringUtils.isNotBlank(strMessage)) {
         %>		alert("SIM Inválido: <%=MiUtil.getMessageClean(strMessage)%>");
   <% } else {
        %>
              parent.mainFrame.fxCargaSimOrders(parent.mainFrame.document.frmdatos.txtSims);
          <%
      }
    }
    %> 
		function fxValidaImeiRepetido(objNuevoImei) {
		vForm = parent.mainFrame.document.frmdatos;
		try {
			cantImeis = vForm.item_imei_imei.length;
			if(cantImeis > 1) {
				for(a = 0; a < cantImeis; a++) {
					oldImei = vForm.item_imei_imei[a].value;
					if(oldImei != "" && oldImei == objNuevoImei.value) {
						alert("IMEI ya está registrado, verifique");
						objNuevoImei.focus();
						return false;
					}
				}
			}
		} catch(exception) {
			alert("[objItemImeiService.jsp:fxValidaImeiRepetido]= "+exception.description);
		}

		return true;
	}

	function fxCargaImeiOrders(objThis) {
		vForm = parent.mainFrame.document.frmdatos;
		var table = parent.mainFrame.document.all ? parent.mainFrame.document.all["table_imeis"] : parent.mainFrame.document.getElementById("table_imeis");
		try {
			if(table.rows.length == 2) { 
				if(vForm.item_imei_radio.checked) {
          /*Cambio para validar que el SIM de la fila esté relacionado al IMEI en BSCS*/
			    if ( vForm.item_imei_sim.value != "" ){
			       var url = "<%=request.getContextPath()%>/itemServlet?hdnMethod=doValidateImeiSimMatch&imei="+vForm.txtImeis.value+"&sim="+vForm.item_imei_sim.value+"&specificationID=<%=strSubCategoria%>&position=-1&strType=IMEI";
			       parent.bottomFrame.location.replace(url);
			    }else{
            vForm.item_imei_imei.value = vForm.txtImeis.value;				
            vForm.txtImeis.value="";
            vForm.hdnImeiChange.value="S";
          }
				}
			} else {			    
				for(i=0; i<vForm.item_imei_radio.length; i++) {				   
					if(vForm.item_imei_radio[i].checked) {
						if(!fxValidaImeiRepetido(objThis)) {
							vForm.txtImeis.select();
							return;
						}
            /*Cambio para validar que el SIM de la fila esté relacionado al IMEI en BSCS*/
            if ( vForm.item_imei_sim[i].value != "" ){
              var url = "<%=request.getContextPath()%>/itemServlet?hdnMethod=doValidateImeiSimMatch&imei="+vForm.txtImeis.value+"&sim="+vForm.item_imei_sim[i].value+"&specificationID=<%=strSubCategoria%>&position="+i+"&strType=IMEI";
              parent.bottomFrame.location.replace(url);
            }else{
              vForm.item_imei_imei[i].value = vForm.txtImeis.value;						
              vForm.txtImeis.value="";
              vForm.hdnImeiChange[i].value="S";
              if (vForm.txtSims.readOnly){
                for (m=0; m<(table.rows.length - 1); m++){
                  if (vForm.item_imei_imei[m].value==""){
                    vForm.item_imei_radio[m].checked = true;
                    vForm.txtImeis.focus();      
                    vForm.hdn_item_imei_selecc.value=m;
                    break;
                  }
                }
              }else
                vForm.txtSims.focus();
              break;
            }
					}
				}
			}
      try{
        vForm.hdnChangedOrder.value="S";
      }catch(e){;}
		} catch(exception) {
			alert("[validateItemDevice.jsp:fxCargaImeiOrders]= "+exception.description);
		}
		return true;
	}
			
</script>
</head>
<body bgcolor="#AABBCC">
</body>
</html>