<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@ page import="oracle.portal.provider.v2.ProviderUser" %>
<%@ page import="pe.com.nextel.bean.DominioBean"%>
<%@ page import="pe.com.nextel.bean.PortalSessionBean" %>
<%@ page import="pe.com.nextel.service.GeneralService"%>
<%@ page import="pe.com.nextel.service.RetailService"%>
<%@ page import="pe.com.nextel.service.SessionService" %>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<%@ page import="pe.com.nextel.exception.SessionException"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>

<%
try {
  String strInfDetail = "0";
	String strInfDoc = "0";
  GeneralService objGeneralService = new GeneralService();
  RetailService objRetailService = new RetailService();
  ArrayList valoresCombo = new ArrayList();
	HashMap hshDataMap = new HashMap();
	hshDataMap = objGeneralService.getDescriptionByValue("LINEAS EN DETALLE","RETAIL_CONFIGURACION");
  strInfDetail = (String) hshDataMap.get("strDescription");
	hshDataMap = objGeneralService.getDescriptionByValue("LINEAS EN DOCUMENTOS DE PAGO","RETAIL_CONFIGURACION");
	strInfDoc = (String) hshDataMap.get("strDescription");
  String strMessage = null;
  String strSessionId1 = "";
	/*Cuando se pruebe localmente comentar*/
  
try {
    PortletRenderRequest pReq = (PortletRenderRequest) request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
    ProviderUser objetoUsuario1 = pReq.getUser();
    strSessionId1=objetoUsuario1.getPortalSessionId();
    System.out.println("Sesión capturada  : " + objetoUsuario1.getName() + " - " + strSessionId1 );
} catch(Exception e) {
    System.out.println("Portler Not Found : " + e.getClass() + " - " + e.getMessage() );
    out.println("Portlet JP_ORDER_SEARCHShowPage Not Found");
    return;
}

//strSessionId1 = "404158502045871144517788277133083662994001614";
System.out.println("Sesión capturada después del resuest: " + strSessionId1);
PortalSessionBean portalSessionBean = (PortalSessionBean) SessionService.getUserSession(strSessionId1);
if (portalSessionBean == null) {
    System.out.println("No se encontró la sesión de Java ->" + strSessionId1);
    throw new SessionException("La sesión finalizó");
}
	
%>
<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
<script type="text/javascript">
	
    
	// Inicio-CEM
	var vValor = window.Event ? true : false;
	var vContador=0;

	function fxOnlyNumber(evt){ 
		// NOTA: '0' = 48, '9' = 57
		var vKey = vValor ? evt.which : evt.keyCode; 
		return (vKey <= 13 || (vKey >= 48 && vKey <= 57));
	} 

  //odubock
  function validaNumerosConCaracteresEspeciales(checkString){ 
      newString = ""; 
      count = 0;
      for (i = 0; i < checkString.length; i++) { 
          ch = checkString.substring(i, i+1); 
          if ((ch >= "0" && ch <= "9") || (ch == ".") || (ch == ",") || (ch == ";") || (ch == "_") || (ch == "-")) {
              newString += ch;
          }
      }
      if (checkString != newString) {
        return false;
      }else{
        return true;
      }
  }

	function fxValidateLengthNumDocumento(){ 		

		var vResultado = true;				
		switch(document.frmdatos.cmbTipoDocumento.value) { 
			case "<%=Constante.TIPO_DOC_DNI%>": 	
				if (document.frmdatos.txtNroDocumento.value.length!=8){
					alert("El número de " + "<%=Constante.TIPO_DOC_DNI%>" + " debe ser de 8 dígitos");
					document.frmdatos.txtNroDocumento.focus();
					vResultado = false;
				}
				break;
			case "<%=Constante.TIPO_DOC_LE%>": 	
				if (document.frmdatos.txtNroDocumento.value.length!=8){
					alert("El número de " + "<%=Constante.TIPO_DOC_LE%>" + " debe ser de 8 dígitos");
					document.frmdatos.txtNroDocumento.focus();
					vResultado = false;					
				}
				break; 
			
			case "<%=Constante.TIPO_DOC_RUC%>": 
				if (document.frmdatos.txtNroDocumento.value.length!=11){
					alert("El número de " + "<%=Constante.TIPO_DOC_RUC%>" + " debe ser de 11 dígitos");					
					document.frmdatos.txtNroDocumento.focus();
					vResultado = false;					
				}
				break; 
			default: 				
				break; 
		} 
		return vResultado;
	}
		
	//Fin-CEM
    function fxFillCustomerInfo() {
        vForm = document.frmdatos;
        tipoDocumento = vForm.cmbTipoDocumento.value;
        nroDocumento = vForm.txtNroDocumento.value;
        if(nroDocumento!="" && tipoDocumento!="") {
            var url = "<%=request.getContextPath()%>/retailServlet?hdnMethod=loadCustomerInfo&cmbTipoDocumento="+tipoDocumento+"&txtNroDocumento="+nroDocumento;
            //var url = "<%=request.getContextPath()%>/retailServlet?hdnMethod=showConfirm&cmbTipoDocumento="+tipoDocumento+"&txtNroDocumento="+nroDocumento;
            parent.bottomFrame.location.replace(url);
        }
    }
         
    function fxFillProvincias() {
        vForm = document.frmdatos;
        departamento = vForm.cmbDepartamento.value;
        if(departamento!="") {
            distrito = '00';
            var url = "<%=request.getContextPath()%>/retailServlet?hdnMethod=loadUbigeo&cmbDepartamento="+departamento+"&cmbDistrito="+distrito+"&jerarquia=<%=Constante.JERARQUIA_PROVINCIA%>";
            parent.bottomFrame.location.replace(url);
        }
        else {
            DeleteSelectOptions(document.frmdatos.cmbProvincia);
			DeleteSelectOptions(document.frmdatos.cmbDistrito);
        }
    }
    
    function fxFillDistritos() {
        vForm = document.frmdatos;
        departamento = vForm.cmbDepartamento.value;
        provincia = vForm.cmbProvincia.value;
        if(provincia!="") {
            var url = "<%=request.getContextPath()%>/retailServlet?hdnMethod=loadUbigeo&cmbDepartamento="+departamento+"&cmbProvincia="+provincia+"&jerarquia=<%=Constante.JERARQUIA_DISTRITO%>";
            parent.bottomFrame.location.replace(url);
        } else {
            DeleteSelectOptions(document.frmdatos.cmbDistrito);
        }
    }
    
    function fxFillSubGiros() {
        vForm = document.frmdatos;
        giro = vForm.cmbGiro.value;
        if(giro!="") {
            var url = "<%=request.getContextPath()%>/retailServlet?hdnMethod=loadSubGiro&cmbGiro="+giro;
            parent.bottomFrame.location.replace(url);
        } else {
            DeleteSelectOptions(document.frmdatos.cmbSubGiro);
        }
    }
    
    function fxFillKitInfo(kitId, index) {
        if(kitId!="") {
            var vForm = document.frmdatos;
            var vTienda = vForm.cmbTienda.value;
            var url = "<%=request.getContextPath()%>/retailServlet?hdnMethod=loadKitInfo&cmbKit="+kitId+"&index="+index+"&strRetailId="+vTienda;
            parent.bottomFrame.location.replace(url);
        }
		if(document.frmdatos.btnProcesar.isDisabled==false) //CEM
			document.frmdatos.btnProcesar.focus();
    }
         
    function fxShowFieldByTipDoc(tipoDocumento) {
        if (tipoDocumento == "RUC") {
            fxShowBillingAccountSection(true);
        }
        else {
            fxShowBillingAccountSection(false);
        }
        fxCleanFields();
    }
    
    function fxShowBillingAccountSection(flag) {
        if(flag) {
          trGiro.style.display="block";
          trCuenta.style.display="block";
          trRazonSocial.style.display="block";
        } else {
          trGiro.style.display="none";
          trCuenta.style.display="none";
          trRazonSocial.style.display="none";		
        }
    }
    
    /*JOYOLAR - SE INVOCA DESDE EL LOADCUSTOMERINFO.JSP*/
    function fxCleanCmbCuenta(){
      DeleteSelectOptions(frmdatos.cmbCuenta);
    }
    
    function fxCleanFields(){
        frmdatos.txtCodigoBSCS.value = "";
        frmdatos.txtTipoCliente.value = "";
        DeleteSelectOptions(frmdatos.cmbCuenta);
        frmdatos.txtRazonSocial.value = "";
        frmdatos.cmbTituloCliente.options[0].selected = true;
        frmdatos.txtNombres.value = "";
        frmdatos.txtApellidos.value = "";
        frmdatos.txtTelefono.value = "";
        for(a=0;a<frmdatos.txtDireccion.length;a++) {
            frmdatos.txtDireccion[a].value = "";
        }
        frmdatos.cmbDepartamento.selectedIndex = 0;
        DeleteSelectOptions(frmdatos.cmbProvincia);
        DeleteSelectOptions(document.frmdatos.cmbDistrito);
        frmdatos.cmbGiro.selectedIndex = 0;
        DeleteSelectOptions(frmdatos.cmbSubGiro);  
    }

    //JNINO 09/30/2010  Se comento la validación de IMEI para que acepte alfanumericos
    function fxValidateIMEI(txtIMEI, ind) {
	if (txtIMEI.value.length > 1) {	                   
             imei = txtIMEI.value;
             for (i=1; i<= <%=strInfDetail%>; i++){
                 if ((document.getElementById("txtIMEI"+i).value.length>0) && (ind != i)){
                     if (imei == document.getElementById("txtIMEI"+i).value){
                         alert("El IMEI que ha seleccionado ya ha sido escogido en la línea "+i+".");
                         txtIMEI.select();
		         txtIMEI.focus();
                         return false;
                     }
                 }
             }
                
             fxLoadImeiInfo(txtIMEI.value, ind);
            
        } else {
            fxResetRowDetail(txtIMEI, ind);
        }
    }
	
	function fxLoadImeiInfo(imei, index) {
		if(imei!="") {
            var url = "<%=request.getContextPath()%>/retailServlet?hdnMethod=loadImeiInfo&txtIMEI="+imei+"&index="+index;
            parent.bottomFrame.location.replace(url);
        }
	}
    
	function fxDelete(ind){
		
		imei=document.getElementById("txtIMEI"+ind).value;		
		if (imei!=""){		
			mensaje="Desea eliminar los datos ingresados para el siguiente imei: " +imei
		}	
		else{
			if ( //document.getElementById("txtIMEI"+ind).value == "" &&
				 document.getElementById("txtEquipo"+ind).value == "" &&
				 document.getElementById("txtServicio"+ind).value == "" &&
				 document.getElementById("txtPlanTarifario"+ind).value == "" &&
				 document.getElementById("txtContrato"+ind).value == "" &&
				 document.getElementById("txtNextel"+ind).value == "" &&
				 document.getElementById("txtModelo"+ind).value == "" &&
				 document.getElementById("txtSIM"+ind).value == "" &&
				 document.getElementById("cmbKit"+ind).value == ""){
				 
				 alert("No hay información por eliminar para esta fila");
				 return;
			}		
			mensaje="Desea eliminar los datos ingresados?"
		}		
		if (!confirm(mensaje)){			
			return false;
		}	
		document.getElementById("txtIMEI"+ind).value = "";
        document.getElementById("hdnItem"+ind).value = "";
        document.getElementById("txtEquipo"+ind).value = "";
        document.getElementById("txtServicio"+ind).value = "";
        document.getElementById("txtPlanTarifario"+ind).value = "";
        document.getElementById("cmbKit"+ind).selectedIndex = 0;
        document.getElementById("cmbKit"+ind).disabled = true;
        document.getElementById("hdnKit"+ind).value = "";
        document.getElementById("txtContrato"+ind).value = "";
        document.getElementById("txtNextel"+ind).value = "";
        document.getElementById("txtModelo"+ind).value = "";
        document.getElementById("txtSIM"+ind).value = "";
	}
	
    function fxResetRowDetail(txtIMEI, ind) {
		if(txtIMEI!=null) {
			txtIMEI.value = "";
		}
        document.getElementById("hdnItem"+ind).value = "";
        document.getElementById("txtEquipo"+ind).value = "";
        document.getElementById("txtServicio"+ind).value = "";
        document.getElementById("txtPlanTarifario"+ind).value = "";
        document.getElementById("cmbKit"+ind).selectedIndex = 0;
        document.getElementById("cmbKit"+ind).disabled = true;
        document.getElementById("hdnKit"+ind).value = "";
        document.getElementById("txtContrato"+ind).value = "";
        document.getElementById("txtNextel"+ind).value = "";
        document.getElementById("txtModelo"+ind).value = "";
        document.getElementById("txtSIM"+ind).value = "";
    }
    
</script>
<form name="frmdatos" action="<%=request.getContextPath()%>/retailServlet" method="POST">
	<input type="hidden" name="hdnMethod" value=""/>	
   <%//Cuando se pruebe localmente comentar portalUser.getLogin()%>
   <input type="hidden" name="hdnLogin" value="<%=portalSessionBean.getLogin()%>"/>
	 <!--<input type="hidden" name="hdnLogin" value="CESPINOZA"/>-->
    
   <input type="hidden" name="hdnDep" value=""/>	
   <input type="hidden" name="hdnDepId" value=""/>	
   <input type="hidden" name="hdnProv" value=""/>	
   <input type="hidden" name="hdnProvId" value=""/>	
   <input type="hidden" name="hdnDistId" value=""/>	
   <input type="hidden" name="hdnDist" value=""/>	
   <input type="hidden" name="hdnTitle" value=""/>	
   
	<table border="0" cellspacing="1" cellpadding="1" align="center" class="RegionBorder" width="100%">
    <tr>
		  <td class="CellLabel" align="Left">&nbsp;&nbsp;&nbsp;&nbsp;N° Orden</td>
		  <td class="CellContent" colspan = "4">
			  <input type="text" id="txtOrden" name="txtOrden" size="25" maxlength="25" readonly="readonly"/>
		  </td>  
    </tr>  
    <tr>
		<td class="CellLabel" align="Left" width="12%"><font color="red">&nbsp;*&nbsp;</font>Tipo Documento</td>
		<td class="CellContent" align="Left" width="36%">
			<select name="cmbTipoDocumento" onchange="fxShowFieldByTipDoc(this.value);fxFillCustomerInfo();">
			<%	hshDataMap = objGeneralService.getDominioList("DOCUMENT_TYPE");
				valoresCombo = (ArrayList) hshDataMap.get("arrDominioList");
                strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
                if (StringUtils.isBlank(strMessage)) {
			%>		<%=MiUtil.buildComboSelected(valoresCombo, Constante.TIPO_DOC_DNI)%>
			<%	}
            %>
			</select>
		</td>
		<td class="CellLabel" align="Left" width="12%"><font color="red">&nbsp;*&nbsp;</font>N&deg; Documento</td>
		<td class="CellContent" width="40%">
			<input type="text" id="txtNroDocumento" name="txtNroDocumento" onchange="fxFillCustomerInfo();javascript:this.value=this.value.toUpperCase();" size="25" maxlength="11"/> <!--CEM-->
		</td>
	</tr>
    <tr>
		<td class="CellLabel" align="Left">&nbsp;&nbsp;&nbsp;&nbsp;C&oacute;digo BSCS</td>
		<td class="CellContent">
			<input type="text" id="txtCodigoBSCS" name="txtCodigoBSCS" size="25" maxlength="25" readonly="readonly"/>
		</td>
		<td class="CellLabel" align="Left">&nbsp;&nbsp;&nbsp;Tipo de Cliente</td>
		<td class="CellContent">
	        <input type="text" id="txtTipoCliente" name="txtTipoCliente" size="25" maxlength="25" readonly="readonly"/>
		</td>
	</tr>
    <tr id="trCuenta" style="display: none;">
		<td class="CellLabel" align="Left">&nbsp;&nbsp;&nbsp;&nbsp;Cuenta</td>
		<td class="CellContent" align="Left" colspan="3">
			<select name="cmbCuenta" style="width: 40%">
				<option value=""></option>
			</select>
		</td>
	</tr>
    <tr id="trRazonSocial" style="display: none;">
		<td class="CellLabel" align="Left"><font color="red">&nbsp;*&nbsp;</font>Raz&oacute;n Social</td>
		<td class="CellContent" colspan="3">
			<input type="text" id="txtRazonSocial" name="txtRazonSocial" size="70" maxlength="70" onChange="javascript:this.value=this.value.toUpperCase();" onBlur="javascript:this.value=trim(this.value);"/>
		</td>
    </tr>
    <tr>
		<td class="CellLabel" align="Left"><font color="red">&nbsp;*&nbsp;</font>Nombres</td>
		<td class="CellContent" align="Left">
			<select name="cmbTituloCliente">
			<%	hshDataMap = objGeneralService.getDominioList("PERSON_TITLE");
				valoresCombo = (ArrayList) hshDataMap.get("arrDominioList");
                strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
                if (StringUtils.isBlank(strMessage)) {
			%>		<%=MiUtil.buildComboSelected1(valoresCombo, "")%>
			<%	}
            %>
			</select>
			<input type="text" id="txtNombres" name="txtNombres" size="25" maxlength="36" onChange="javascript:this.value=this.value.toUpperCase();" onBlur="javascript:this.value=trim(this.value);"/>
		</td>
		<td class="CellLabel" align="Left"><font color="red">&nbsp;*&nbsp;</font>Apellidos</td>
		<td class="CellContent">
			<input type="text" id="txtApellidos" name="txtApellidos" size="25" maxlength="36" onChange="javascript:this.value=this.value.toUpperCase();" onBlur="javascript:this.value=trim(this.value);"/>
		</td>
	</tr>
    <tr>
		<td class="CellLabel" align="Left" rowspan="2"><font color="red">&nbsp;*&nbsp;</font>Direcci&oacute;n</td>
		<td class="CellContent" colspan="3">
			<input type="text" id="txtDireccion1" name="txtDireccion" size="50" maxlength="40" onChange="javascript:this.value=this.value.toUpperCase();" onBlur="javascript:this.value=trim(this.value);"/>
		</td>
    </tr>
    <tr>
		<td class="CellContent" colspan="3">
			<input type="text" id="txtDireccion2" name="txtDireccion" size="50" maxlength="40" onChange="javascript:this.value=this.value.toUpperCase();" onBlur="javascript:this.value=trim(this.value);"/>
		</td>
    </tr>
    <tr>
		<td class="CellLabel" align="Left"><font color="red">&nbsp;*&nbsp;</font>Departamento</td>
		<td class="CellContent" align="Left">
			<select name="cmbDepartamento" onchange="fxFillProvincias()">
			<%	hshDataMap = objGeneralService.getDominioList("RETAIL_DEPARTAMENTOS");
				valoresCombo = (ArrayList) hshDataMap.get("arrDominioList");
                strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
                if (StringUtils.isBlank(strMessage)) {
			%> 		<%=MiUtil.buildComboSelected(valoresCombo, "")%>
			<%	}
            %>                       
			</select>
		</td>
		<td class="CellLabel" align="Left"><font color="red">&nbsp;*&nbsp;</font>Provincia</td>
		<td class="CellContent" align="Left">
			<select name="cmbProvincia" style="width: 75%" onchange="fxFillDistritos()">
				<option value=""></option>            
			</select>
		</td>
	</tr>
    <tr>
		<td class="CellLabel" align="Left"><font color="red">&nbsp;*&nbsp;</font>Distrito</td>
		<td class="CellContent" align="Left">
			<select name="cmbDistrito" style="width: 75%">
				<option value=""></option>
			</select>
		</td>
		<td class="CellLabel" align="Left"><font color="red">&nbsp;*&nbsp;</font>Tel&eacute;fono</td>
		<td class="CellContent">
			<input type="text" id="txtTelefono" name="txtTelefono" size="25" maxlength="25" onKeyPress="return fxOnlyNumber(event);" />
		</td>
	</tr>
    <tr id="trGiro" style="display: none;">
		<td class="CellLabel" align="Left"><font color="red">&nbsp;*&nbsp;</font>Giro</td>
		<td class="CellContent" align="Left">
			<select name="cmbGiro" onchange="fxFillSubGiros()" style="width: 100%">
			<%	hshDataMap = objGeneralService.getGiroList();
				valoresCombo = (ArrayList) hshDataMap.get("arrGiroList"); //CEM				
                strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
                if (StringUtils.isBlank(strMessage)) {
			%>		<%=MiUtil.buildComboSelected(valoresCombo, "")%>
			<%	}
            %>
			</select>
      </td>
      <td class="CellLabel" align="Left"><font color="red">&nbsp;*&nbsp;</font>Sub Giro</td>
      <td class="CellContent" align="Left">
			<select name="cmbSubGiro" style="width: 100%">
				<option value=""></option>
			</select>
		</td>
	</tr>
</table>
<br/>
<table border="0" cellspacing="1" cellpadding="0" align="center" class="RegionBorder" width="100%">
	<tr>
		<td class="CellLabel" align="Left" width="12%"><font color="red">&nbsp;*&nbsp;</font>N&deg; Solicitud</td>
		<td class="CellContent" width="36%">		
			<input type="text" id="txtNroSolicitud" name="txtNroSolicitud" size="24" maxlength="10"/>&nbsp;(7 caracteres)
		</td>
		<td class="CellLabel" align="Left" width="12%"><font color="red">&nbsp;*&nbsp;</font>Tienda</td>
		<td class="CellContent" align="Left" width="40%">
		<select name="cmbTienda" onblur="fxShowFieldPlace(this.value);">
			<%	hshDataMap = objGeneralService.getRetailList();
				valoresCombo = (ArrayList) hshDataMap.get("arrRetailList");
                strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
                if (StringUtils.isBlank(strMessage)) {
			%>		<%=MiUtil.buildComboSelected(valoresCombo, "")%>
			<%	} else {
			%>		<script>alert("Error al cargar el combo: <%=MiUtil.getMessageClean(strMessage)%>");</script>
			<%	}
            %>
			</select>
			</td>
		</tr>
		<tr>
			<td class="CellLabel" align="Left">&nbsp;&nbsp;&nbsp;&nbsp;Observaciones</td>
			<td class="CellContent" colspan="3">
				<textarea name="txtObservaciones" cols="100" rows="3" onChange="javascript:this.value=this.value.toUpperCase();" onBlur="javascript:this.value=trim(this.value);" maxlength="400" onkeyup="return ismaxlength(this)"></textarea>
			</td>
		</tr>
	</table>
	<br/>
	<table border="0" cellspacing="1" cellpadding="0" align="center" class="RegionBorder" width="100%">
		<tr>
			<td class="CellLabel" align="Center">IMEI</td>
			<td class="CellLabel" align="Center">Kit</td>
			<td class="CellLabel" align="Center">Equipo(S/.)</td><!--FES Se cambió $ por S -->
			<td class="CellLabel" align="Center">Servicio(S/.)</td><!--FES Se cambió $ por S -->
			<td class="CellLabel" align="Center">Plan Tarifario</td>			
			<td class="CellLabel" align="Center">Contrato</td>
			<td class="CellLabel" align="Center">Nextel</td>
			<td class="CellLabel" align="Center">Modelo</td>
			<td class="CellLabel" align="Center">SIM</td>
			<td class="CellLabel" align="Center">Eliminar</td>
		</tr>
	<%	//hshDataMap = objGeneralService.getKitsList();
		//ArrayList arrKitsList = (ArrayList) hshDataMap.get("arrKitsList");
        for(int i=1; i<=Integer.parseInt(strInfDetail); i++) {
	%>
		<tr>
			<td class="cellContent" align="center">
				<input type="hidden" id="hdnItem<%=i%>" name="hdnItem"/>
				<input type="text" id="txtIMEI<%=i%>" name="txtIMEI" size="14" onblur="fxValidateIMEI(this, '<%=i%>')"  maxlength="15"/>
			</td>
			<td class="cellContent" align="center">
				<select id="cmbKit<%=i%>" name="cmbKit" onchange="fxFillKitInfo(this.value, '<%=i%>')" disabled="disabled">
					<option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
				</select>
				<input type="hidden" id="hdnKit<%=i%>" name="hdnKit"/>
			</td>			
			<td class="cellContent" align="center">
				<input type="text" id="txtEquipo<%=i%>" name="txtEquipo" size="4" align="right" readonly="readonly"/>
			</td>
			<td class="cellContent" align="center">
				<input type="text" id="txtServicio<%=i%>" name="txtServicio" size="4" align="right" readonly="readonly"/>
			</td>
			<td class="cellContent" align="center">
				<input type="text" id="txtPlanTarifario<%=i%>" name="txtPlanTarifario" size="16" align="center" readonly="readonly"/>
				<input type="hidden" id="hdnPlanTarifario<%=i%>" name="hdnPlanTarifario"/>
			</td>
			<td class="cellContent" align="center">
				<input type="text" id="txtContrato<%=i%>" name="txtContrato" size="5" readonly="readonly"/>
			</td>
			<td class="cellContent" align="center">
				<input type="text" id="txtNextel<%=i%>" name="txtNextel" size="11" readonly="readonly"/>
			</td>
			<td class="cellContent" align="center">
				<input type="text" id="txtModelo<%=i%>" name="txtModelo" size="6" readonly="readonly"/>
			</td>
			<td class="cellContent" align="center">
				<input type="text" id="txtSIM<%=i%>" name="txtSIM" size="20" readonly="readonly"/>
			</td>
			<td class="cellContent" align="center">&nbsp;
                <a href="javascript:fxDelete(<%=i%>);"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/Eliminar.gif" alt="Eliminar" border="0" hspace=2></a>
			</td>			
		</tr>
	<%	}
    %>
	</table>
	<br/>
	<table cellpadding="0" cellspacing="1" align="center" border="0" class="RegionBorder" width="50%">
		<tr>
			<td class="CellLabel" align="center">N&deg; Voucher</td>
		</tr>
    <%	for(int i = 1; i <= Integer.parseInt(strInfDoc); i++) {
        %>
		<tr>
			<td class="cellContent" align="center">
				<input type="text" name="txtNroVoucher" size="26" maxlength="24" onBlur="javascript:this.value=trim(this.value);"/>
			</td>
		</tr>
    <%	}
    %>
	</table>
	<br/>
	<table align="center">
		<tr>
			<td align="center">
				<input type="button" name="btnProcesar" value="Procesar" onclick="fxProcessOrder();"/>
			</td>
			<td align="center">
				<input type="button" name="btnNuevo" value="Nuevo" onclick="fxRefreshPage();"/>
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">

    fxShowFieldByTipDoc("<%=Constante.TIPO_DOC_DNI%>");
   
    function fxProcessOrder() {
        var vForm = document.frmdatos;
        if(validateRetailNewOrder()) {
            vForm.hdnMethod.value = "newOrderRetail";
			vForm.target = "bottomFrame";
            vForm.submit();
			vForm.btnProcesar.disabled = "true";
        }
    }
     
     function fxShowFieldPlace(objPlace){
      var vForm = document.frmdatos;
      var vItem=0;
  
      <%	for(int i = 1; i <= Integer.parseInt(strInfDetail); i++) {
        %> 
            if( document.getElementById("cmbKit"+<%=i%>).length > 1) {
                if ( document.getElementById("txtIMEI"+<%=i%>).value != "" ){
                    vItem = <%=i%>;
                }
            }
     <% }%>
          
     if (vItem >=1){
     
         mensaje="Desea eliminar los datos ingresados?"
         
          if (!confirm(mensaje)){			
             return false;
          }
          
            <%	for(int i = 1; i <= Integer.parseInt(strInfDetail); i++) {
        %> 
          
                    document.getElementById("txtIMEI"+<%=i%>).value = "";
                    document.getElementById("hdnItem"+<%=i%>).value = "";
                    document.getElementById("txtEquipo"+<%=i%>).value = "";
                    document.getElementById("txtServicio"+<%=i%>).value = "";
                    document.getElementById("txtPlanTarifario"+<%=i%>).value = "";
                    document.getElementById("hdnKit"+<%=i%>).value = "";
                    document.getElementById("txtContrato"+<%=i%>).value = "";
                    document.getElementById("txtNextel"+<%=i%>).value = "";
                    document.getElementById("txtModelo"+<%=i%>).value = "";
                    document.getElementById("txtSIM"+<%=i%>).value = "";
                    DeleteSelectOptions(document.getElementById("cmbKit"+<%=i%>));
            <% }%>
     }
     
  
      <%	for(int i = 1; i <= Integer.parseInt(strInfDetail); i++) {
        %> 
            DeleteSelectOptions(document.getElementById("cmbKit"+<%=i%>));
     <% }%>     
   
     // var url = "<%=request.getContextPath()%>/retailServlet?hdnMethod=loadKitInfoRetail&strTienda="+objPlace+"&index="+index;
      var url = "<%=request.getContextPath()%>/retailServlet?hdnMethod=loadKitInfoRetail&strTiendaRetail="+objPlace+"&strInfDetail=<%=strInfDetail%>";
      parent.bottomFrame.location.replace(url);
    }
     
    function validateRetailNewOrder() {
    
        var vForm = document.frmdatos;
        
        var txtCodigoBSCS = vForm.txtCodigoBSCS.value;
        
        var cmbTipoDocumento = vForm.cmbTipoDocumento.value;
        var txtNroDocumento = vForm.txtNroDocumento.value;
        
        var txtTipoCliente = vForm.txtTipoCliente.value;
        
        var txtRazonSocial = vForm.txtRazonSocial.value;
        
        var cmbTituloCliente = vForm.cmbTituloCliente.value;
        var txtNombres = vForm.txtNombres.value;
        var txtApellidos = vForm.txtApellidos.value;
        
        var txtDireccion1 = vForm.txtDireccion[0].value;
        var txtDireccion2 = vForm.txtDireccion[1].value;
        
        var cmbDepartamento = vForm.cmbDepartamento.value;
        var cmbProvincia = vForm.cmbProvincia.value;
        var cmbDistrito = vForm.cmbDistrito.value;
        
        var txtTelefono = vForm.txtTelefono.value;
        
        var cmbGiro = vForm.cmbGiro.value;
        var cmbSubGiro = vForm.cmbSubGiro.value;
        var cmbCuenta = vForm.cmbCuenta.value;        
        ////////////////////////////////////////////////////////
        var txtNroSolicitud = vForm.txtNroSolicitud.value;
        var cmbTienda = vForm.cmbTienda.value;
        ///*
        ////////////////////////////////////////////////////////
        ////                VALIDACION GENERICA             ////
		
        if(cmbTipoDocumento=="") {
            vForm.cmbTipoDocumento.focus();
            alert("Seleccione Tipo de Documento");
            return false;
        }
        if(fxTrim(txtNroDocumento)=="") {
            alert("Ingrese Nro. de Documento");
            vForm.txtNroDocumento.select();
            vForm.txtNroDocumento.focus();
            return false;
        }
        if (!fxValidateLengthNumDocumento()) {			
			return false;
         }	
        ////////////////////////////////////////////////////////
        ////    VALIDACION REFERENTE A UN NUEVO CLIENTE     ////
        if (txtTipoCliente == "Prospect" ||txtTipoCliente =="") {
            if(cmbTipoDocumento == "RUC") {
                if(fxTrim(txtRazonSocial)=="") {
                    alert("Ingrese Razón Social");
                    vForm.txtRazonSocial.select();
                    vForm.txtRazonSocial.focus();                    
                    return false;
                }
            }
			//titulo del cliente
            if(cmbTituloCliente=="") {
				if (vForm.cmbTituloCliente.disabled==true){
					alert("No se puede procesar la orden, falta registrar el titulo del cliente (Sr/Sra...)");
					return false;
				}
				else{
					vForm.cmbTituloCliente.focus();
					alert("Seleccione Título Cliente");
					return false;
				}			
            }
			
            if(fxTrim(txtNombres)=="") {
                alert("Ingrese Nombres del Cliente");
                vForm.txtNombres.select();
                vForm.txtNombres.focus();
                return false;
            }
            if(fxTrim(txtApellidos)=="") {
                alert("Ingrese Apellidos del Cliente");
                vForm.txtApellidos.select();
                vForm.txtApellidos.focus();
                return false;
            }
            if(fxTrim(txtDireccion1)=="") {
                alert("Ingrese Dirección del Cliente");
                vForm.txtDireccion[0].select();
                vForm.txtDireccion[0].focus();
                return false;
            }
            if(cmbDepartamento=="") {
				if (vForm.cmbDepartamento.disabled==true){
					alert("No se puede procesar la orden, falta registrar el departamento.");
					return false;
				}
				else{
					vForm.cmbDepartamento.focus();
					alert("Seleccione Departamento");
					return false;
				}	
            }
            if(cmbProvincia=="" || cmbProvincia=="0") {
				if (vForm.cmbProvincia.disabled==true){
					alert("No se puede procesar la orden, falta registrar la provincia.");
					return false;
				}
				else{
					vForm.cmbProvincia.focus();
					alert("Seleccione Provincia");
					return false;
				}
            }
            if(cmbDistrito=="" || cmbDistrito=="0") {
				if (vForm.cmbDistrito.disabled==true){
					alert("No se puede procesar la orden, falta registrar el distrito.");
					return false;
				}
				else{
					vForm.cmbDistrito.focus();
					alert("Seleccione Distrito");
					return false;
				}
            }
            if(fxTrim(txtTelefono)=="") {
				if (vForm.txtTelefono.disabled==true){
					alert("No se puede procesar la orden, falta registrar el teléfono del cliente.");
					return false;
				}
				else{
					alert("Ingrese Teléfono del Cliente");
					vForm.txtTelefono.select();
					vForm.txtTelefono.focus();
					return false;
				}			
            }
            if(cmbTipoDocumento == "RUC") {
                if(cmbGiro=="") {
					if (vForm.cmbGiro.disabled==true){
						alert("No se puede procesar la orden, falta registrar el giro.");
						return false;
					}
					else{				
						vForm.cmbGiro.focus();
						alert("Seleccione Giro");
						return false;
					}
                }
                if(cmbSubGiro=="") {
					if (vForm.cmbSubGiro.disabled==true){
						alert("No se puede procesar la orden, falta registrar el subgiro.");
						return false;
					}
					else{				
						vForm.cmbSubGiro.focus();
						alert("Seleccione SubGiro");
						return false;
					}								
                }
            }
        }
		
        else if (txtTipoCliente == "Customer") {
          /*JOYOLAR Inc 6686 - Si el cliente es LARGE debe validar la CTA*/
          if(txtCodigoBSCS != "" && txtCodigoBSCS.substr(0,2) != "1.") {
            if (vForm.cmbCuenta.length > 1){
              if (cmbCuenta == ""){
                alert("Seleccione una Cuenta");
                vForm.cmbCuenta.focus();
                return false;
              }
            }
          }
        }

        if(fxTrim(txtNroSolicitud)=="") {
            alert("Ingrese Nro. de Solicitud");
            vForm.txtNroSolicitud.select();
            vForm.txtNroSolicitud.focus();
            return false;
        }

        if(!validaNumerosConCaracteresEspeciales(txtNroSolicitud)){
          alert("Ingrese números y/o caracteres especiales para el Nro. de Solicitud");
          vForm.txtNroSolicitud.select();
          vForm.txtNroSolicitud.focus();          
          return false;
        }
        if(cmbTienda=="") {
            vForm.cmbTienda.focus();
            alert("Seleccione Tienda");
            return false;
        }		
        ////////////////////////////////////////////////////////
        ////        VALIDACION REFERENTE A ITEMS            ////
        var cont = 0;
		var numFilasUtilizadas=0;

        for (i=1; i<= <%=strInfDetail%>; i++){
            if (document.frmdatos.txtIMEI[i-1].value!=""){
                if(document.getElementById("cmbKit"+i).value=="") {
					try{
						alert("Si a ingresado un IMEI, tiene que seleccionar un Kit");
						document.getElementById("cmbKit"+i).focus();					
						return false;
					}catch(e){}
                }
				numFilasUtilizadas=numFilasUtilizadas+1;
            }
            else {
                cont++;
            }
        }
		
        if (cont == <%=strInfDetail%>) {
            alert("Tiene que ingresar algun item");
            document.getElementById("txtIMEI1").focus();
            return false;
        }   
		//*/
		//Inicio CEM COR0443
		//Validar que se ingrese por lo menos un # de voucher
		for (i=0; i< <%=strInfDoc%>; i++){
			if (frmdatos.txtNroVoucher[i].value!=""){
				break;
			}
		}			
		if (i==<%=strInfDoc%>){
			alert("Debe ingresar el número de voucher");
			return false;
		}
		//Fin CEM COR0443
        return true;
    }

    function ismaxlength(obj){
        var mlength=obj.getAttribute? parseInt(obj.getAttribute("maxlength")) : "";
        if (obj.getAttribute && obj.value.length>mlength){
            obj.value=obj.value.substring(0,mlength);
        }
    }
    
    function fxTrim(s) {
        while (s.length>0 && (s.charAt(0)==" "||s.charCodeAt(0)==10||s.charCodeAt(0)==13)) {
            s=s.substring(1, s.length);
        }
        while (s.length>0 && (s.charAt(s.length-1)==" "||s.charCodeAt(s.length-1)==10||s.charCodeAt(s.length-1)==13)) {
            s=s.substring(0, s.length-1);
        }
        return s; 
    }
    
    function fxRefreshPage() {
		//Probar localmente
      location.replace('<%="/portal/page/portal/orders/RETAIL_NEW"%>');
      //location.replace("JP_ORDER_RETAIL_ShowPage.jsp");
    }
    
    document.frmdatos.txtNroDocumento.focus();
    
</script>
<%
} catch(SessionException se) {
  se.printStackTrace();
  System.out.println("[JP_ORDER_SEARCH][Finalizó la sesión]");
  out.println("<script>alert('Su sesión ha finalizado. Ingrese nuevamente');</script>");
  String strUrl = "/portal/pls/portal/WEBSALES.NP_USER_AVAILABILITY_PL_PKG.PL_LOGOUT";
} catch(Exception e) {
  String strMessageExceptionGeneralStart = "";
  strMessageExceptionGeneralStart = MiUtil.getMessageClean(e.getMessage());
  out.println("<script>alert('"+strMessageExceptionGeneralStart+"');</script>");   
}
%>
