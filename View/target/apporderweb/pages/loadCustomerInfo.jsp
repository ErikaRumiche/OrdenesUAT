<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.io.IOException"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="pe.com.nextel.bean.SiteBean"%>
<%@ page import="pe.com.nextel.bean.CustomerBean"%>
<%@ page import="pe.com.nextel.bean.AddressObjectBean"%>
<%@ page import="pe.com.nextel.bean.DominioBean"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	</head>
	<body bgcolor="#AABBCC">
		<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
		<script type="text/javascript">
			//fxCleanCustomerInfo();/*CEM*/      
			DeleteSelectOptions(parent.mainFrame.document.frmdatos.cmbSubGiro);         
		<%  CustomerBean customer = (CustomerBean) request.getAttribute("customer");         
			HashMap hshDataMap = (HashMap) request.getAttribute(Constante.DATA_STRUCT);
			String strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
         System.out.println("loadCustomerInfo:"+strMessage);
			if(StringUtils.isBlank(strMessage)) {
				ArrayList cmbSubGiroList = (ArrayList) hshDataMap.get("arrSubGiroList");
				System.out.println("cmbSubGiroList: "+cmbSubGiroList);
				Iterator subGiros = cmbSubGiroList.iterator();
				while(subGiros.hasNext()) {
					DominioBean dominio = (DominioBean) subGiros.next();
		%>			fxAddNewOption(parent.mainFrame.document.frmdatos.cmbSubGiro,'<%=dominio.getValor()%>','<%=dominio.getDescripcion()%>');
		<%  	}
			} else {
		%>
				alert("Error al cargar el combo: <%=strMessage%>");
		<%	}
		%>      
			parent.mainFrame.document.frmdatos.txtCodigoBSCS.value = "<%=customer.getSwCodBscs()%>";
      
      if(fxIsLargeAccount("<%=customer.getSwCodBscs()%>")) {
        parent.mainFrame.fxShowBillingAccountSection(true);
      }
      
			parent.mainFrame.document.frmdatos.txtTipoCliente.value = "<%=customer.getSwType()%>";
      //alert(parent.mainFrame.document.frmdatos.txtTipoCliente.value);
      if (parent.mainFrame.document.frmdatos.txtTipoCliente.value == "Employee"){
         alert("No se pueden generar Órdenes Retail para Empleados");         
         parent.mainFrame.document.frmdatos.txtNroDocumento.value = "";  
         parent.mainFrame.document.frmdatos.txtTelefono.value = "";           
         parent.mainFrame.document.frmdatos.txtNroDocumento.focus();           
         fxCleanCustomerInfo();
      }
      else{         
      <%if (!customer.getSwName().equals("")){%>                  
         parent.mainFrame.document.frmdatos.hdnTitle.value = "<%=customer.getTituloPersona()%>";           
         parent.mainFrame.document.frmdatos.cmbTituloCliente.value = "<%=customer.getTituloPersona()%>";  
         //parent.mainFrame.document.frmdatos.txtRazonSocial.value = "<%=customer.getSwName()%>";  
         parent.mainFrame.document.frmdatos.txtRazonSocial.value = "<%=MiUtil.getMessageClean(customer.getSwName())%>"; 
         parent.mainFrame.document.frmdatos.txtNombres.value = "<%=MiUtil.getMessageClean(customer.getNpFirstName())%>";
         parent.mainFrame.document.frmdatos.txtApellidos.value = "<%=MiUtil.getMessageClean(customer.getNpLastName())%>";
         parent.mainFrame.document.frmdatos.txtDireccion1.value = "<%=MiUtil.getMessageClean(((AddressObjectBean)(customer.getDireccionList().get(0))).getSwaddress1())%>";                           
         parent.mainFrame.document.frmdatos.cmbDepartamento.value = "<%=MiUtil.getMessageClean(((AddressObjectBean)(customer.getDireccionList().get(0))).getSwdepid())%>";                                                                                                
         parent.mainFrame.document.frmdatos.hdnProvId.value = "<%=((AddressObjectBean)(customer.getDireccionList().get(0))).getNpprovinciaid()%>";                     
         parent.mainFrame.document.frmdatos.hdnProv.value = "<%=((AddressObjectBean)(customer.getDireccionList().get(0))).getSwprovince()%>";                           
         parent.mainFrame.document.frmdatos.hdnDistId.value = "<%=((AddressObjectBean)(customer.getDireccionList().get(0))).getNpdistritoid()%>";                           
         parent.mainFrame.document.frmdatos.hdnDist.value = "<%=((AddressObjectBean)(customer.getDireccionList().get(0))).getSwcountry()%>";                                             
         parent.mainFrame.document.frmdatos.cmbDistrito.value = "<%=((AddressObjectBean)(customer.getDireccionList().get(0))).getNpdistritoid()%>";                           
      <%}else{%>
         fxCleanCustomerInfo();
         parent.mainFrame.document.frmdatos.txtTipoCliente.value = "Prospect";
         fxClearCmb();
         <%}%>
			parent.mainFrame.document.frmdatos.txtTelefono.value = "<%=customer.getSwMainPhone()%>";  //CEM

			try {
				for(i = 0; i < parent.mainFrame.document.frmdatos.cmbGiro.options.length; i++) {
					if(parent.mainFrame.document.frmdatos.cmbGiro.options[i].value == "<%=customer.getNpGiroId()%>") {
						parent.mainFrame.document.frmdatos.cmbGiro.options[i].selected = true;
						break;
					}
				}
				for(i=0;i<parent.mainFrame.document.frmdatos.cmbSubGiro.options.length;i++) {
					if(parent.mainFrame.document.frmdatos.cmbSubGiro.options[i].value == "<%=customer.getSwIndustryId()%>") {
						parent.mainFrame.document.frmdatos.cmbSubGiro.options[i].selected = true;
						break;
					}
				}
			} catch(exception) {
				alert(exception.message);
			}		
    
		<%	Iterator cuentas = customer.getCuentaList().iterator();
			while(cuentas.hasNext()) {
				SiteBean site = (SiteBean) cuentas.next();
		%>		fxAddNewOption(parent.mainFrame.document.frmdatos.cmbCuenta,'<%=site.getNpCodBscs()%>','<%=site.getSwSiteName()+" - "+site.getNpCodBscs()%>');
		<%	}
			if(customer.isValidCustomer()) {
		%>		fxSetOnlyReadableCustomerInfo(true);
				parent.mainFrame.document.frmdatos.txtNroSolicitud.focus();
		<%	} else {
		%>		fxSetOnlyReadableCustomerInfo(false);
		<%	}
		%>
    }
    function fxAddNewOption(TheCmb, Value, Description) {
        var option = new Option(Description, Value);
        var i = TheCmb.options.length;
        TheCmb.options[i] = option;
    }
    
    function fxIsLargeAccount(codBscs){

      var codigo;
      codigo = codBscs;

      if (codigo !="" && codigo.substr(0,2) != "1."){
        return true;
      }
      return false;
    }
    
    function fxCleanCustomerInfo() {          
        parent.mainFrame.document.frmdatos.txtCodigoBSCS.value = "";
        parent.mainFrame.document.frmdatos.txtTipoCliente.value = "";
        DeleteSelectOptions(parent.mainFrame.document.frmdatos.cmbCuenta);
        parent.mainFrame.document.frmdatos.txtRazonSocial.value = "";
        parent.mainFrame.document.frmdatos.cmbTituloCliente.options[0].selected = true;
        parent.mainFrame.document.frmdatos.txtNombres.value = "";
        parent.mainFrame.document.frmdatos.txtApellidos.value = "";
        for(a=0;a<parent.mainFrame.document.frmdatos.txtDireccion.length;a++) {
            parent.mainFrame.document.frmdatos.txtDireccion[a].value = "";
        }
        parent.mainFrame.document.frmdatos.cmbDepartamento.selectedIndex = 0;         
        parent.mainFrame.document.frmdatos.cmbProvincia.selectedIndex = 0; 
        parent.mainFrame.document.frmdatos.cmbDistrito.selectedIndex = 0; 
        DeleteSelectOptions(parent.mainFrame.document.frmdatos.cmbProvincia);
        DeleteSelectOptions(parent.mainFrame.document.frmdatos.cmbDistrito);        
        parent.mainFrame.document.frmdatos.cmbGiro.selectedIndex = 0;
        DeleteSelectOptions(parent.mainFrame.document.frmdatos.cmbSubGiro);
        fxClearCmb();
    }
    
    function fxSetOnlyReadableCustomerInfo(flag) {       
        parent.mainFrame.document.frmdatos.txtRazonSocial.readOnly = flag;
        parent.mainFrame.document.frmdatos.cmbTituloCliente.disabled = flag;
        parent.mainFrame.document.frmdatos.txtNombres.readOnly = flag;
        parent.mainFrame.document.frmdatos.txtApellidos.readOnly = flag;        
        for(a=0;a<parent.mainFrame.document.frmdatos.txtDireccion.length;a++) {
            parent.mainFrame.document.frmdatos.txtDireccion[a].readOnly = flag;
        }
        parent.mainFrame.document.frmdatos.cmbDepartamento.disabled = flag;
        parent.mainFrame.document.frmdatos.cmbProvincia.disabled = flag;
        parent.mainFrame.document.frmdatos.cmbDistrito.disabled = flag;
        parent.mainFrame.document.frmdatos.txtTelefono.disabled = flag;
        parent.mainFrame.document.frmdatos.cmbGiro.disabled = flag;
        parent.mainFrame.document.frmdatos.cmbSubGiro.disabled = flag;
    }
    
    function fxUpdateCmb(){                  
      if (parent.mainFrame.document.frmdatos.cmbDepartamento.value != ""){
      DeleteSelectOptions(parent.mainFrame.document.frmdatos.cmbProvincia);
      DeleteSelectOptions(parent.mainFrame.document.frmdatos.cmbDistrito);          
      var vprovid = parent.mainFrame.document.frmdatos.hdnProvId.value;
      var vprov = parent.mainFrame.document.frmdatos.hdnProv.value;      
      var optionProv = new Option(vprov, vprovid);      
      parent.mainFrame.document.frmdatos.cmbProvincia.options[0] = optionProv;
      
      var vdepid = parent.mainFrame.document.frmdatos.hdnDistId.value;
      var vdep = parent.mainFrame.document.frmdatos.hdnDist.value;
      var optionDist = new Option(vdep, vdepid);      
      parent.mainFrame.document.frmdatos.cmbDistrito.options[0] = optionDist;            
      }
    }
    
    function fxClearCmb(){                  
      var optionProv = new Option("", 0);      
      parent.mainFrame.document.frmdatos.cmbProvincia.options[0] = optionProv;         
      var optionDist = new Option("", 0);      
      parent.mainFrame.document.frmdatos.cmbDistrito.options[0] = optionDist;
    }
    
    fxUpdateCmb();      
    
    </script>
  </body>
</html>