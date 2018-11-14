<%System.out.println("[JP_ORDER_SEARCH][Inicio]");%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@ page import="oracle.portal.provider.v2.ProviderUser" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="pe.com.nextel.bean.PortalSessionBean" %>
<%@ page import="pe.com.nextel.bean.DominioBean" %>
<%@ page import="pe.com.nextel.service.SessionService" %>
<%@ page import="pe.com.nextel.service.GeneralService" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.exception.SessionException" %>
<%@page import="pe.com.nextel.service.OrderSearchService"%>

<%@ page import="pe.com.nextel.util.GenericObject" %>

<%!
protected static Logger logger = Logger.getLogger(HttpServlet.class);

protected String formatException(Throwable e) {
	return GenericObject.formatException(e);
}
%>

<%
try {
  String strSessionId1 = "";
  
  try{
    PortletRenderRequest pReq = (PortletRenderRequest) request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
    ProviderUser objetoUsuario1 = pReq.getUser();
    strSessionId1=objetoUsuario1.getPortalSessionId();
    System.out.println("Sesión capturada  : " + objetoUsuario1.getName() + " - " + strSessionId1 );
    
  }catch(Exception e){
    System.out.println("Portler Not Found : " + e.getClass() + " - " + e.getMessage() );
    out.println("Portlet JP_ORDER_SEARCHShowPage Not Found");
    return;
  }
  
  //strSessionId1 = "464855881967091855419054279192645894994001614"; // Pruebas en Local
  
  System.out.println("Sesión capturada después del resuest : " + strSessionId1 );
	PortalSessionBean portalSessionBean = (PortalSessionBean)SessionService.getUserSession(strSessionId1);
	if(portalSessionBean==null) {
    System.out.println("No se encontró la sesión de Java ->" + strSessionId1);
		throw new SessionException("La sesión finalizó");
	}

    
   System.out.println("Valor jerarquia"+portalSessionBean.getSalesStructId());	
	

	/**************************************************************************/
	GeneralService objGeneralService = new GeneralService();
	HashMap hshDataMap = new HashMap();
	ArrayList valoresCombo = new ArrayList();
	String strMessage = null;
	Calendar calendar = Calendar.getInstance();
	calendar.add(Calendar.MONTH, -1);
  
  HashMap hshRegionList   = null;
  HashMap hshBuildingList = null;
  HashMap hshStateList    = null;  
  
  //AGAMARRA
  //String npsalesstructid = (String)session.getValue("npsalesstructid");


  String npsalesstructid = portalSessionBean.getSalesStructId()+""; //(String)session.getValue("npsalesstructid");
	
    System.out.println("Valor jerarquia  A"+npsalesstructid );    		

  if(npsalesstructid!=null && (npsalesstructid.equals("0") || npsalesstructid.trim().equals(""))){
    npsalesstructid = "13";
  }else{
    OrderSearchService orderSearchService = new OrderSearchService();
    String bCheckStruct = orderSearchService.checkStructRule(97, npsalesstructid);
    
    if(bCheckStruct.equalsIgnoreCase("S")){
      npsalesstructid = "13";
    }
  }
  
  //String swprovidergrpid = (String)session.getValue("swprovidergrpid");


  String swprovidergrpid = portalSessionBean.getProviderGrpId()+"";//(String)session.getValue("swprovidergrpid");
  
System.out.println("Valor jerarquia  B"+swprovidergrpid  );    		

  
%>
<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/date-picker.js"></script>
<script type="text/javascript">
    function fxCleanGeneralObjects() {
      
    }
        
    function fxLoadGeneral() {
       v_form = document.formdatos;
       if (v_form.hdnSearchType.value == "1")
          fxSearchOrder();
    }
	
	function fxFillSoluciones() {
    vForm = document.formdatos;
    divisionNegocio = vForm.cmbDivisionNegocio.value;
    if(divisionNegocio!=""){
        var url = "<%=request.getContextPath()%>/orderSearchServlet?hdnMethod=loadSolution&cmbDivisionNegocio="+divisionNegocio;
        parent.bottomFrame.location.replace(url);
    }else {
        if(document.formdatos.cmbSolucionNegocio!=null)
          DeleteSelectOptions(document.formdatos.cmbSolucionNegocio);
    }
  }
  
	function fxFillCategorias() {
    vForm = document.formdatos;
    solucionNegocio = vForm.cmbSolucionNegocio.value;
    if(solucionNegocio!="") {
        var url = "<%=request.getContextPath()%>/orderSearchServlet?hdnMethod=loadCategory&cmbSolucionNegocio="+solucionNegocio;
        parent.bottomFrame.location.replace(url);
    }else{
        if(document.formdatos.cmbCategoria!=null)
          DeleteSelectOptions(document.formdatos.cmbCategoria);
    }
  }
	
	function fxFillSubCategorias() {
        vForm = document.formdatos;
        categoria = vForm.cmbCategoria.value;
        if(categoria!="") {            
            var url = "<%=request.getContextPath()%>/orderSearchServlet?hdnMethod=loadSubCategory&cmbCategoria="+categoria+"&cmbSolucionNegocio="+solucionNegocio;
            parent.bottomFrame.location.replace(url);
        }
        else {
            DeleteSelectOptions(document.formdatos.cmbSubCategoria);
        }
    }
    
</script>
<script type="text/javascript">

    function fxValidateCustomer(v_origen, v_type) {
        var v_form            = document.formdatos;
        var v_txtCustomerName = v_form.txtCustomerName.value;
        var v_hacer_busqueda  = false;
        var v_hdnCustomerName = v_form.hdnCustomerName.value;
        var v_hdnCustomerId   = v_form.hdnCustomerId.value;
        var v_txtRuc          = v_form.txtRuc.value;
        var v_txtCodBscs      = v_form.txtCodBscs.value;
            
        v_form.txtNumber.value = "";
        if (v_origen == "CUST_NAME") {
            if (v_txtCustomerName != v_hdnCustomerName && v_txtCustomerName != "") {
                v_form.txtRuc.value ="";
                v_form.hdnRuc.value="";
                v_form.txtCodBscs.value ="";
                v_form.hdnCustomerId.value="";
                v_form.hdnCustomerId.value="";
                v_form.hdnCustomerIdbscs.value="";
                fxCleanGeneralObjects();
                if (v_txtCustomerName != "" || v_hdnCustomerId != ""  || v_txtRuc != "" || v_txtCodBscs != "") {
                    v_hacer_busqueda = true;
                    if (v_type == "1")
                       v_form.hdnSearchType.value = "1";
                }
            }
            else if (v_txtCustomerName == "") {
                fxCleanObjects();
            }
        }
        if (v_origen == "CUST_RUC") {
            if ((v_form.txtRuc.value != v_form.hdnRuc.value || v_form.hdnRuc.value == "") &&  v_txtRuc != "") {
                v_form.hdnCustomerName.value ="";
                v_form.txtCustomerName.value ="";
                v_form.txtCodBscs.value ="";
                v_form.hdnCustomerId.value="";
                v_form.hdnCustomerIdbscs.value="";
                fxCleanGeneralObjects();
                v_hacer_busqueda = true;
                if (v_type == "1") 
                   v_form.hdnSearchType.value = "1";
            }
            else if (v_txtRuc == "") {
                fxCleanObjects();     
            }
        }
        if (v_origen == "CUST_CODBSCS") {
            if ( (v_form.txtCodBscs.value != v_form.hdnCodbscs.value || v_form.txtCodBscs.value == "") && v_txtCodBscs != "") {
                v_form.hdnCustomerName.value ="";
                v_form.txtCustomerName.value ="";
                v_form.txtRuc.value ="";
                v_form.hdnRuc.value="";
                v_form.hdnCustomerId.value="";
                v_form.hdnCustomerIdbscs.value="";
                
                fxCleanGeneralObjects();
                v_hacer_busqueda = true;
                if (v_type == "1")
                   v_form.hdnSearchType.value = "1";
            }  
            else if (v_txtCodBscs == "") {
                fxCleanObjects();
            }
        }
        if (v_origen == "CUST_DOCUMENTS") {
            v_hacer_busqueda = true;
        }
        if (v_origen == "CUST_SELECTED") {
            v_hacer_busqueda = true;
        }
        if (v_origen == "CUSTID_SELECTED") {
            if (v_form.hdnCustomerId.value !="") {
                v_hacer_busqueda = true;
            }
        }
        if (v_hacer_busqueda == true){
            v_form.action="/portal/pls/portal/!WEBSALES.NPSL_CUSTOMER02_PL_PKG.PL_VALIDATE_CUSTOMER";
            v_form.target = "bottomFrame";
            v_form.submit();
        }
        return;
    }

    function fxValidateNumber() {
        var v_form = document.formdatos;
        var vhdnNumber=v_form.hdnNumber.value;
        var vNumber=v_form.txtNumber.value;
        var vCriterio=v_form.cmbCriterion.value;
        if (v_form.cmbCriterion.selectedIndex >= 0) {
            if (vNumber != "" && (vhdnNumber != vNumber || vhdnNumber == "")){
                if (!ContentOnlyNumber(v_form.txtNumber.value)&& vCriterio!="50" ) {
                    alert("El número ingresado no es válido.");
                    v_form.txtNumber.value = "";
                    v_form.txtNumber.focus();
                    v_form.txtNumber.select();                  
                    return;
                }
               
                v_form.hdnCustomerName.value ="";
                v_form.txtCustomerName.value ="";
                v_form.txtRuc.value ="";
                v_form.txtCodBscs.value ="";
                v_form.hdnCustomerId.value="";
                fxCleanGeneralObjects();
                v_form.hdnNumber.value=vNumber;
               
                var url = "/portal/pls/portal/WEBSALES.NPSL_CUSTOMER02_PL_PKG.PL_VALIDATE_NUMBER"
                        + "?av_number=" + vNumber
                        + "&av_criterio=" + v_form.cmbCriterion.options[v_form.cmbCriterion.selectedIndex].value;
                parent.bottomFrame.location.replace(url);
            }
        }
        else {
            alert("Seleccione el criterio.");
        }
    }         
    
    function fxCheckKey(e, num1) {
        var v_form = document.formdatos;
        if (fxCheckEnterKey(e)) {
            if(num1 == 1)
                v_form.txtNumber.blur();
            else if(num1 == 2)
                v_form.txtCustomerName.blur();
            else if(num1 == 3)
                v_form.txtCodBscs.blur();
            else if(num1 == 4)
                v_form.txtRuc.blur();
        }
    }
      
    function fxCheckEnterKey(e) {
        var CR = 13;
        var NS = ((window.Event)? 1: 0);
        var keycode = ((NS)? e.which: e.keyCode);
        return (keycode == CR);
    }   

    function fxCleanObjects() {
        var v_form = document.formdatos;
        v_form.hdnCustomerName.value ="";
        v_form.hdnCustomerId.value="";
        v_form.txtRuc.value ="";
        v_form.txtCodBscs.value ="";
        v_form.txtCustomerName.value ="";
        v_form.hdnStatusCollection.value="";
        v_form.hdnTypecia.value="";            
        fxCleanGeneralObjects();
    }
    
    function fxSearchCustomer() {
        var formdatos        = document.formdatos;
        var txtCustomerName = formdatos.txtCustomerName;
        if (txtCustomerName.value == "") {
            formdatos.txtNumber.value = "";
            fxCleanObjects();
        }
        formdatos.hdnCustomerName.value = txtCustomerName.value;
        url = "/portal/pls/portal/!WEBSALES.NPSL_CUSTOMER01_PL_PKG.PL_CUSTOMER_SEARCH"
            + "?av_customername=" + escape(txtCustomerName.value);
        url = "/portal/pls/portal/WEBSALES.NPSL_GENERAL_PL_PKG.WINDOW_FRAME?av_title="
            + escape("Búsqueda de compañía") + "&av_url=" + escape(url);
        WinAsist = window.open(url,"WinAsist","toolbar=no,location=0,directories=no,status=yes,menubar=0,scrollbars=no,resizable=no,screenX=100,top=80,left=100,screenY=80,width=700,height=500,modal=yes");
    }

    function fxCheckOrden(e) {
        var v_form = document.formdatos;
        var vNumber=v_form.txtNumber.value;
        var vCriterio=v_form.cmbCriterion.value;
        var div = document.getElementById("div_OrderNumberSearch");
        if (vNumber!="" && v_form.hdnCustomerId.value!="" && vCriterio == "10" ) {
          div.style.display="block"; 
          div.style.display="inline"; 
        } else {
          div.style.display="none"; 
          document.getElementById("chkNumberSearch").checked = false;
          fxChangeValueChkReparacion();
        }
    }
    
    function fxChangeValueNumberSearch() {
        if(document.getElementById("chkNumberSearch").checked==true) {
          if(document.formdatos.txtNumber.value=="" || !ContentOnlyNumber(document.formdatos.txtNumber.value)) {
            alert("El número ingresado no es válido.");
            document.formdatos.txtNumber.value = "";
            document.formdatos.txtNumber.focus();
            document.formdatos.txtNumber.select();   
            document.getElementById("chkNumberSearch").checked=false;
            return;            
          }
          document.getElementById("chkNumberSearch").value = 1;
        } else {
          document.getElementById("chkNumberSearch").value = 0;
        }
        fxChangeValueChkReparacion();
    }    
    function fxChangeValueChkReparacion() { 
        if(document.getElementById("chkNumberSearch").checked==true && 
           document.getElementById("chkReparacion").checked == true) { 


           document.formdatos.txtNextel.value=document.formdatos.txtNumber.value;
           document.formdatos.txtNextel.readOnly = true;            
        } else {

           document.formdatos.txtNextel.value="";
           document.formdatos.txtNextel.readOnly = false; 
        }
    }
</script>
<!-- frmdatos -->
<form name="formdatos" action="<%=request.getContextPath()%>/orderSearchServlet" method="POST">
    
  <table border="0" cellspacing="7" cellpadding="0" width="99%" align="center"><tr><td>

	<input type="hidden" name="hdnLogin" value="<%=portalSessionBean.getLogin()%>"/>
  <input type="hidden" name="hdnMethod" value=""/>
	<input type="hidden" name="hdnUserId" value="<%=strSessionId1%>"/>
  
  <input type="hidden" name="strUnidadJerarquica" value=""/>
  <input type="hidden" name="strRepresentante" value=""/>
  
	  
    <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
        <tr>
            <td class="SectionTitleLeftCurve" width="10">&nbsp;&nbsp;</td>
            <td class="SectionTitle">&nbsp;&nbsp;Ingrese Criterios de B&uacute;squeda - Datos de la Compañía</td>
            <td class="SectionTitleRightCurve" width="8">&nbsp;&nbsp;</td>
        </tr>
    </table>
    
    <%--
    INICIO: BUSQUEDA DE COMPAÑIA - RDELOSREYES
    --%>
    <input type="hidden" name="hdnNumRegistros" id="hdnNumRegistros" value="<%=Constante.NUM_REGISTROS_X_PAGINAS%>">
    <input type="hidden" name="hdnNumPagina" id="hdnNumPagina" value="1">
    
    <input type="hidden" name="hdnCustomerId" id="hdnCustomerId" value="">
    <input type="hidden" name="hdnCustomerName" id="hdnCustomerName" value="">
    <input type="hidden" name="hdnRuc" id="hdnRuc" value="">
    <input type="hidden" name="hdnCustomerIdbscs" id="hdnCustomerIdbscs" value="">      
    <input type="hidden" name="hdnStatusCollection" id="hdnStatusCollection" value="">      
    <input type="hidden" name="hdnTypecia" id="hdnTypecia" value="">            
    <input type="hidden" name="hdnNumber" id="hdnNumber" value="">                  
    <input type="hidden" name="hdnCodbscs" id="hdnCodbscs" value="">                        
    <input type="hidden" name="hdnnotclient_allowed" id="hdnnotclient_allowed" value="0">
    <input type="hidden" name="hdnSearchType" id="hdnSearchType" value="0">
      
    <div id="divCust">
        <table border="0" cellpadding="2" cellspacing="0" class="RegionBorder" width="100%" id="table_customer">
            <tr>
                <td class="CellLabel" align="left" valign="middle">&nbsp;&nbsp;Criterio</td>                 
                <td class="CellLabel" align="left" valign="middle">&nbsp;&nbsp;Número</td>                              
                <td class="CellLabel" align="left">&nbsp;Cod. BSCS </TD>         
                <td class="CellLabel">&nbsp;<font color="#FF0000">*&nbsp;</font>RUC/DNI/Otro</td>
                <td class="CellLabel" align="left">&nbsp;<font color="#FF0000">*&nbsp;</font><a href="javascript:fxSearchCustomer()">Raz&oacute;n&nbsp;Social</a></TD>
                <td class="CellLabel" align="left"valign="middle">&nbsp;&nbsp;Rango de Cuenta</td>
            </tr>
            <tr>
                <td align="center" class="cellcontent">
                    <%--WEBSALES.NPSL_GENERAL_PL_PKG.SP_COMBO_FEEDING_ORDER('CRITERION_OPTION',1,1,'','','10',FALSE); -- POr defecto la opción que se muestra es Nextel--%>
                    <select name="cmbCriterion" onchange="return fxCheckOrden(event)" >
                        <option value="10">N° Teléfono</option>
                        <option value="50">N° Radio</option>
                        <option value="20">IMEI</option>
                        <option value="30">Contrato</option>
                        <option value="40">SIM</option>
                    </select>               
                </td>
                 
                <td class="CellContent">
                    <input type="text" name="txtNumber" id="txtNumber" value="" size="30" maxlength="50"
                           onKeyDown="if (window.event.keyCode== 9) {this.value=trim(this.value.toUpperCase());fxValidateNumber();}" 
                           onKeyPress="if (window.event.keyCode== 13) {this.value=trim(this.value.toUpperCase());fxValidateNumber();}" >
                           
                           <div id="div_OrderNumberSearch" style="display:none;">
                           <input type="checkbox" id="chkNumberSearch" name="chkNumberSearch" value="0" onclick="fxChangeValueNumberSearch();" /> <i>Ver &Oacute;rdenes solo de este n&uacute;mero </i></div>
                </td>
                <td class="CellContent">
                    <input type="text" name="txtCodBscs" id="txtCodBscs" value="" size="15" maxlength="50" 
                           onKeyDown="if (window.event.keyCode== 9) {this.value=trim(this.value.toUpperCase());fxValidateCustomer('CUST_CODBSCS','0');}" 
                           onKeyPress="if (window.event.keyCode== 13) {this.value=trim(this.value.toUpperCase());fxValidateCustomer('CUST_CODBSCS','1');}">                           
               </td>                                                  
               <td class="CellContent">
                    <input type="text" name="txtRuc" id="txtRuc" value="" size="15" maxlength="11" 
                           onKeyDown="if (window.event.keyCode== 9) {this.value=trim(this.value.toUpperCase());fxValidateCustomer('CUST_RUC','0');}" 
                           onKeyPress="if (window.event.keyCode== 13) {this.value=trim(this.value.toUpperCase());fxValidateCustomer('CUST_RUC','1');}">
               </td>            
               <td class="CellContent">
                    <input type="text" name="txtCustomerName" size="35"  maxlength="75"                      
                           onKeyDown="if (window.event.keyCode== 9) {this.value=trim(this.value.toUpperCase());fxValidateCustomer('CUST_NAME','0');}" 
                           onKeyPress="if (window.event.keyCode== 13) {this.value=trim(this.value.toUpperCase());fxValidateCustomer('CUST_NAME','1');}">
               </td>
               <td class="cellcontent">
                <select name="cmbRangoCuenta">
                    <%	hshDataMap = objGeneralService.getComboReparacionList("Rango_Cuenta");
                        valoresCombo = (ArrayList) hshDataMap.get("arrComboReparacionList");
                        strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
                        if (strMessage == null) {%>
                           <option value="0">  </option>
                           <%=MiUtil.buildComboSelected(valoresCombo, "", false)%>
                        <%}
                    %>
                </select>               
               </td>
            </tr>
        </table>
    </div>   
    <%--
    FIN: BUSQUEDA DE COMPAÑIA - RDELOSREYES
    --%>
    <br/>
    
    <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
        <tr>
            <td class="SectionTitleLeftCurve" width="10">&nbsp;&nbsp;</td>
            <td class="SectionTitle">&nbsp;&nbsp;Ingrese Criterios de B&uacute;squeda - Datos de la Orden</td>
            <td class="SectionTitleRightCurve" width="8">&nbsp;&nbsp;</td>
        </tr>
    </table>
    <table border="0" cellspacing="1" cellpadding="1" align="center" class="RegionBorder" width="100%">
        <tr>
            <td class="CellLabel" width="15%">N&deg; Orden</td>
            <td class="CellContent" width="35%">
                <input type="text" name="txtNroOrden" size="5"  maxlength="10" onblur="this.value=fxTrim(this.value);" 
                onKeyPress="if (window.event.keyCode== 13) {this.value=trim(this.value.toUpperCase());fxSearchOrder()}"/>
                <input type="checkbox" name="chkFastSearch" value="1" checked/> <i>Ver todo el detalle de la Orden</i>
            </td>
            <td class="CellLabel" width="15%">N&deg; Solicitud</td>
            <td class="CellContent" width="35%">
                <input type="text" id="txtNroSolicitud" name="txtNroSolicitud" size="7"
                       maxlength="15" onblur="this.value=fxTrim(this.value);"/>                
            </td>
        </tr>
        <tr>
			<td class="CellLabel">División de Negocio</td>
            <td class="CellContent">
                <select name="cmbDivisionNegocio" onchange="fxFillSoluciones()">
					<%hshDataMap = objGeneralService.getDivisionList();
						strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
            if( strMessage != null )
              throw new Exception(strMessage);
              
            valoresCombo = (ArrayList) hshDataMap.get("arrDivisionList");
					%>
					<%=MiUtil.buildComboSelected(valoresCombo, "", true)%>
                </select>
                <input type="hidden" name="hdnDivisionNegocio">
            </td>
            <td class="CellLabel">Solución de Negocio</td>
            <td class="CellContent">
                <select name="cmbSolucionNegocio" onchange="fxFillCategorias()" style="width: 60%">
                    <option value=""></option>
                </select>
                <input type="hidden" name="hdnSolucionNegocio">
            </td>
        </tr>
        <tr>
            <td class="CellLabel">Categoría</td>
            <td class="CellContent">
                <select name="cmbCategoria" onchange="fxFillSubCategorias()" style="width: 100%">
                    <option value=""></option>
                </select>
                <input type="hidden" name="hdnCategoria">
            </td>
            <td class="CellLabel">Sub Categoría</td>
            <td class="CellContent">
                <select name="cmbSubCategoria" style="width: 100%">
                    <option value=""></option>
                </select>
                <input type="hidden" name="hdnSubCategoria">
            </td>
        </tr>

        <jsp:include page="loadJerarquias.jsp">
          <jsp:param name="ruleid" value="33"/>
          <jsp:param name="av_retriverepresentative" value="LASTLEVEL"/>
          <jsp:param name="av_madatoryrepresentative" value="NO"/>
          <jsp:param name="strNpsalesstructid" value="<%=npsalesstructid%>"/>
          <jsp:param name="strSwprovidergrpid" value="<%=swprovidergrpid%>"/>
          <jsp:param name="pn_npsalesstructid" value=""/>
          <jsp:param name="pn_swproviderid" value=""/>
        </jsp:include>
        
        <tr>
            <td class="CellLabel">Región</td>
            <td class="CellContent">
                <select name="cmbRegion">
                    <option value=""></option>
					<%hshRegionList = objGeneralService.getRegionList();
				/*		
            hshRegionList =null;
            System.out.println("Antes de entrar a preguntar por el RegionList");
            if( ( hshRegionList = (HashMap)application.getAttribute("regionList") ) ==null){
                System.out.println("Entramos a preguntar por el RegionList");
                hshRegionList = objGeneralService.getRegionList();
                application.setAttribute("regionList",hshRegionList);
            }else{
              System.out.println("No Entramos a preguntar por el RegionList");
            }
            */
            strMessage = (String) hshRegionList.get(Constante.MESSAGE_OUTPUT);
            if( strMessage != null )
              throw new Exception(strMessage);
            
            valoresCombo = (ArrayList) hshRegionList.get("arrRegionList");
						if (strMessage == null) {
							for(int i=0;i<valoresCombo.size();i++ ) {
								HashMap hshRegionMap = (HashMap) valoresCombo.get(i);
					%>
					<option value='<%=hshRegionMap.get("swregionid")%>'>
						<%=hshRegionMap.get("swname")%>
					</option>
					<%		}
						}
					%>
                </select>
                <input type="hidden" name="hdnRegion">
            </td>
            <td class="CellLabel">Tienda</td>
            <td class="CellContent">
                <select name="cmbTienda">
					<option value=""></option>
					<%hshBuildingList = objGeneralService.getBuildingList(Constante.BUILDING_TIPO_FISICA);
            /*hshBuildingList =null;
            
            if( ( hshBuildingList = (HashMap)application.getAttribute("buildingList") ) ==null){
                hshBuildingList = objGeneralService.getBuildingList(Constante.BUILDING_TIPO_FISICA);
                application.setAttribute("buildingList",hshBuildingList);
            }
            */
						strMessage = (String) hshBuildingList.get("strMessage");
            if( strMessage != null )
              throw new Exception(strMessage);
            
            valoresCombo = (ArrayList) hshBuildingList.get("arrBuildingList");
						if (strMessage==null){                           
							for(int i=0;i<valoresCombo.size();i++){
								DominioBean dominio = (DominioBean) valoresCombo.get(i);
					%>
					<option value='<%=dominio.getValor()%>'>
						<%=dominio.getDescripcion()%>
					</option>
					<%		}
						}
					%>
				</select>
                <input type="hidden" name="hdnTienda">
            </td>
        </tr>
		<tr>
			<td class="CellLabel">Estado</td>
            <td class="CellContent">
                <select name="cmbEstadoOrden">
					<option value="" selected="selected"></option>
               <option value="(EN PROCESO)">(EN PROCESO)</option>
					<%hshStateList = objGeneralService.getEstadoOrdenList();
              	  strMessage = (String) hshStateList.get(Constante.MESSAGE_OUTPUT);
            if( !StringUtils.isBlank(strMessage) )
              throw new Exception(strMessage);
              
            valoresCombo = (ArrayList) hshStateList.get("arrEstadoOrdenList");
						if (StringUtils.isBlank(strMessage)) {
					%>
						<%=MiUtil.buildComboSelected(valoresCombo, "", false)%>
					<%
						}
					%>
                </select>
                <input type="hidden" name="hdnEstadoOrden">
            </td>
			<td class="CellLabel" width="15%" valign="top">Creado por:</td>
            <td class="CellContent" width="35%" valign="top">
                <input type="text" name="txtCreadoPor" size="25" maxlength="30" onblur="this.value=fxTrim(this.value);"/>
            </td>
		</tr>
        <tr>
            <td class="CellLabel" width="15%">N&deg; Guía</td>
            <td class="CellContent" width="35%">
              <input type="text" name="txtNroGuia" size="20" maxlength="15" onblur="this.value=fxTrim(this.value);"/>                
            </td>
            <td class="CellLabel" width="15%" valign="top">Servicio Técnico:</td>
            <td class="CellContent" width="35%" valign="top">
                <input type="checkbox" name="chkReparacion" value="1" onclick="javascript:fxChangeValueChkReparacion();" />
            </td>
        </tr>        
         <tr>
            <td class="CellLabel" valign="top"><div style="padding-left: 3px;">N° Propuesta</div></td>
            <td class="CellContent"><input type="text" name="txtProposedId" size="20" maxlength="15" onblur="this.value=fxTrim(this.value);"/></td>
            <td class="CellLabel" valign="top"><div style="padding-left: 3px;">Fecha de Creación</div></td>
            <td class="CellContent" valign="middle" colspan="3">
                &nbsp;desde&nbsp;<input type="text" name="txtCreateDateFrom" size="10" maxlength="10" value="<%=MiUtil.getDate(calendar.getTime(), "dd/MM/yyyy")%>" onblur="this.value=fxTrim(this.value);">
                <a href="javascript:show_calendar('formdatos.txtCreateDateFrom',null,null,'DD/MM/YYYY');" onmouseover="window.status='Fecha desde';return true;" onmouseout="window.status='';return true;">
                <img src="<%=Constante.PATH_APPORDER_SERVER%>/images/show-calendar.gif" width="24" height="22" border="0"></a>&nbsp;<i>DD/MM/YYYY</i>
                <br/>
                &nbsp;hasta&nbsp;&nbsp;<input type="text" name="txtCreateDateTill" size="10" maxlength="10" value="<%=MiUtil.getDate(new java.util.Date(), "dd/MM/yyyy")%>" onblur="this.value=fxTrim(this.value);">
                <a href="javascript:show_calendar('formdatos.txtCreateDateTill',null,null,'DD/MM/YYYY');" onmouseover="window.status='Fecha hasta';return true;" onmouseout="window.status='';return true;">
                <img src="<%=Constante.PATH_APPORDER_SERVER%>/images/show-calendar.gif" width="24" height="22" border="0"></a>&nbsp;<i>DD/MM/YYYY</i>
            </td>
        </tr>
        <tr>
            <td class="CellLabel" valign="top"><div style="padding-left: 3px;">Segmento</div></td>
            <td class="CellContent">
                <select name="cmbSegmentoCompania">
                    <%	hshDataMap = objGeneralService.getComboReparacionList("Segmento_Compania");
                        valoresCombo = (ArrayList) hshDataMap.get("arrComboReparacionList");
                        strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
                        if (strMessage == null) {%>
                    <option value="0">  </option>
                    <%=MiUtil.buildComboSelected(valoresCombo, "", false)%>
                    <%}
                    %>
                </select>
                <input type="hidden" name="hdnSegmentoCompania">
            </td>
            <!-- INICIO PRY-1239 PCACERES -->
            <td class="CellLabel" width="15%" valign="top">Pago Web:</td>
            <td class="CellContent">
                <select name="cmbWebPayment" >
                    <option value="" selected="selected"></option>
                    <option value="1">Si</option>
                    <option value="0">No</option>
                </select>
            </td>
            <!-- FIN PRY-1239 PCACERES -->
        </tr>
    </table>
    
    <br/>
    
    <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
        <tr>
            <td class="SectionTitleLeftCurve" width="10">&nbsp;&nbsp;</td>
            <td class="SectionTitle">&nbsp;&nbsp;Ingrese Criterios de B&uacute;squeda - Datos de Reparación</td>
            <td class="SectionTitleRightCurve" width="8">&nbsp;&nbsp;</td>
        </tr>
    </table>
    <table border="0" cellspacing="1" cellpadding="1" align="center" class="RegionBorder" width="100%">
        <tr>
            <td class="CellLabel" width="15%" height="22">N&deg; Reparación</td>
            <td class="CellContent" width="35%" height="22">
                <input type="text" name="txtNroReparacion" size="5" maxlength="10" onblur="this.value=fxTrim(this.value);"/>
            </td>
            <td class="CellLabel" width="15%" height="22">Nextel</td>
            <td class="CellContent" width="35%" height="22">
                <input type="text" id="txtNextel" name="txtNextel" size="10" onblur="this.value=fxTrim(this.value);"/>
            </td>
        </tr>
        <!-- >INICIO - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 30/09/2010 <-->
        <tr>
            <td class="CellLabel">Tipo Proceso</td>
            <td class="CellContent">
                <select name="cmbTipoProceso">
                    <%	hshDataMap = objGeneralService.getComboReparacionList("REPAIR_PROCESSTYPE");
						valoresCombo = (ArrayList) hshDataMap.get("arrComboReparacionList");
						strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
						if (strMessage == null) {
					%>
						<%=MiUtil.buildComboSelected(valoresCombo, "", true)%>
					<%
						}
					%>
                </select>
                <input type="hidden" name="hdnTipoProceso">
            </td>
            
            <td class="CellLabel">Tipo de Equipo</td>
            <td class="CellContent">
               <select name="cmbTipoEquipo" style="width: 30%" onchange="fxLoadListModel();"/>
                    <%	
                    hshDataMap = objGeneralService.getTipoEquipoList();
                    valoresCombo = (ArrayList) hshDataMap.get("arrTipoDispList");
                    strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
                    if (StringUtils.isBlank(strMessage)) {
                    %>
                      <%=MiUtil.buildComboSelected(valoresCombo, "", true)%>
                    <% } %>
                </select>               
                <input type="hidden" name="hdnTipoEquipo">
            </td>
        </tr>
        <!-- FIN - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 30/09/2010 -->
         
         <tr>
            <td class="CellLabel">Tipo Servicio</td>
            <td class="CellContent">
                <select name="cmbTipoServicio">
                    <%	hshDataMap = objGeneralService.getComboReparacionList("ServiciosReparacion");
						valoresCombo = (ArrayList) hshDataMap.get("arrComboReparacionList");
						strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
						if (strMessage == null) {
					%>
						<%=MiUtil.buildComboSelected(valoresCombo, "", true)%>
					<%
						}
					%>    </select>
                <input type="hidden" name="hdnTipoServicio">
            </td>
            <!-- INICIO - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 30/09/2010 -->
            <td class="CellLabel">Modelo</td>
            <td class="CellContent">
                <select name="cmbModelo" style="width: 30%" id="cmbModelo">
                
                </select>
                <input type="hidden" name="hdnModelo">
                <div id="dvModelo"></div>
                
             </td>
             <!-- FIN - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 30/09/2010 -->  
        </tr>
        <tr>
            <td class="CellLabel">Tipo Falla</td>
            <td class="CellContent">
                <select name="cmbTipoFalla">
                    <%	hshDataMap = objGeneralService.getComboReparacionList("REPAIR_FAIL_TYPE");
						valoresCombo = (ArrayList) hshDataMap.get("arrComboReparacionList");
						strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
						if (strMessage == null) {
					%>
						<%=MiUtil.buildComboSelected(valoresCombo, "", true)%>
					<%
						}
					%>
                </select>
                <input type="hidden" name="hdnTipoFalla">
            </td>
            <!-- INICIO- Team HP Argentina - UFMI -  PROYECTO HP-PTT - 30/09/2010 --> 
            <td class="CellLabel">Imei</td>
            <td class="CellContent">
                <input type="text" name="txtImei" size="15" maxlength="15" onblur="this.value=fxTrim(this.value);"/>
            </td>
        </tr>
        <tr>
            <td class="CellLabel">Imei Cambio</td>
            <td class="CellContent">
                <input type="text" name="txtImeiCambio" size="15" maxlength="15" onblur="this.value=fxTrim(this.value);"/>
            </td>
            <td class="CellLabel">Imei Préstamo</td>
            <td class="CellContent">
                <input type="text" name="txtImeiPrestamo" size="15" maxlength="15" onblur="this.value=fxTrim(this.value);"/>
            </td>
        </tr>
        <tr>
            <td class="CellLabel">Situación</td>
            <td class="CellContent">
                <select name="cmbSituacion">
                    <%	hshDataMap = objGeneralService.getComboReparacionList("REPAIR_SITUACION");
						valoresCombo = (ArrayList) hshDataMap.get("arrComboReparacionList");
						strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
						if (strMessage == null) {
					%>
						<%=MiUtil.buildComboSelected(valoresCombo, "", true)%>
					<%
						}
					%>    </select>
                <input type="hidden" name="hdnSituacion">
            </td>
            
            <td class="CellLabel">Técnico Responsable</td>
            <td class="CellContent">
                <select name="cmbTecnicoResponsable">
                    <%	hshDataMap = objGeneralService.getComboReparacionList("REPAIR_TECHNICAL");
						valoresCombo = (ArrayList) hshDataMap.get("arrComboReparacionList");
						strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
						if (strMessage == null) {
					%>
						<%=MiUtil.buildComboSelected(valoresCombo, "", true)%>
					<%
						}
					%>
                </select>
                <input type="hidden" name="hdnTecnicoResponsable">
            </td>
        </tr>
        <tr>
            <td class="CellLabel">Estado</td>
            <td class="CellContent">
                <select name="cmbEstadoReparacion">
					<%	hshDataMap = objGeneralService.getComboReparacionList("Repair Status");
						valoresCombo = (ArrayList) hshDataMap.get("arrComboReparacionList");
						strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
						if (strMessage == null) {
					%>
						<%=MiUtil.buildComboSelected(valoresCombo, "", true)%>
					<%
						}
					%>
                </select>
                <input type="hidden" name="hdnEstadoReparacion"></td>
            <td class="CellLabel"></td>
            <td class="CellContent" colspan="3">
            </td>				
        </tr>
        <tr>
            <td class="CellLabel">Marca DAP</td>
            <td class="CellContent">
                <select name="cmbMarcaDap">
                      <option value=""></option>
                      <option value="S">Si</option>
                      <option value="N">No</option>
                </select></td>
            <td class="CellLabel">Estado Orden Externa</td>
            <td class="CellContent" colspan="3">
                <select name="cmbEstadoOrdenExterna" style="width: 200px">
                   <OPTION VALUE="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</OPTION> 
                  <%hshDataMap = objGeneralService.getRepairConfiguration("ST_OE_STATES");
                    valoresCombo = (ArrayList) hshDataMap.get("arrDominioList");
                    strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
                    if(valoresCombo != null && strMessage == null){
                       for(int i=0;i<valoresCombo.size();i++){
                          DominioBean dominio = (DominioBean)valoresCombo.get(i);
                          %>
                             <OPTION VALUE="<%=dominio.getDescripcion()%>"> <%=dominio.getDescripcion()%></OPTION>
                       <%}%>
                    <%}%>
                </select>
            </td>				
        </tr>
        <tr>
            <td class="CellLabel">Respuesta de la Cotizaci&oacute;n</td>
            <td class="CellContent">
                <select name="cmbRespuestaCotizacion" style="width: 150px">
                  <%hshDataMap = objGeneralService.getRepairConfiguration("ST_STATUS_QUOTE");
                    valoresCombo = (ArrayList) hshDataMap.get("arrDominioList");
                    strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
                    if (strMessage == null) {
                        %>
                                <%=MiUtil.buildComboSelected(valoresCombo, "", true)%>
                        <%
                                }
                        %>
                </select>
                </td>
            <td class="CellLabel">Situaci&oacute;n Equipo</td>
            <td class="CellContent" colspan="3">
                <select name="cmbSituacionEquipo" style="width: 150px">
                  <%hshDataMap = objGeneralService.getDominioList("REPAIR_ORE_SITUACION_EQUIPOS");
                    valoresCombo = (ArrayList) hshDataMap.get("arrDominioList");
                    strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
                    if (strMessage == null) {
                    %>
                            <%=MiUtil.buildComboSelected(valoresCombo, "", true)%>
                    <%
                            }
                    %>
                </select>
            </td>				
        </tr>
        <tr>
            <td class="CellLabel">Tienda Recojo</td>
            <td class="CellContent">
                <select name="cmbTiendaRecojo">
                        <option value=""></option>
                        <%hshBuildingList = objGeneralService.getBuildingList(Constante.BUILDING_TIPO_FISICA);
                                strMessage = (String) hshBuildingList.get("strMessage");
                        if( strMessage != null )
                          throw new Exception(strMessage);
                        
                        valoresCombo = (ArrayList) hshBuildingList.get("arrBuildingList");
                                if (strMessage==null){                           
                                        for(int i=0;i<valoresCombo.size();i++){
                                                DominioBean dominio = (DominioBean) valoresCombo.get(i);
                        %>
                        <option value='<%=dominio.getValor()%>'>
                                <%=dominio.getDescripcion()%>
                        </option>
                        <%		}
                                }
                        %>
                </select>
            </td>
            <td class="CellLabel"></td>
            <td class="CellContent" colspan="3">
            </td>				
        </tr>
        <!-- FIN - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 30/09/2010 --> 
    </table>
    
    <br/>
    
    <table align="center">
        <tr>
            <td align="center">
                <input type="button" name="btnBuscar" value="Buscar" onclick="fxSearchOrder();"/>
            </td>
        </tr>
    </table>
    
    </td></tr></table>
    <script>
        var vForm = document.formdatos;
        
        vForm.cmbDivisionNegocio.value = "";
        vForm.cmbSolucionNegocio.value = "";
        vForm.cmbCategoria.value = "";
        vForm.cmbSubCategoria.value = "";
       
    </script>
</form>

<script type="text/javascript">

  var countfxSearchOrder=0;
  
  function fxSearchOrder() {
    var vForm = document.formdatos;
    
    //------------------------------
    //AGAMARRA
    var v_provid = vForm.cmbRepresentante.value;
    
    if(v_provid!=null && v_provid!='' && v_provid>0){
      vForm.hdnProviderid.value = v_provid;
      try{
        vForm.strRepresentante.value = vForm.cmbRepresentante.options[vForm.cmbRepresentante.selectedIndex].text;
      }catch(err){
      }
    }else{
      vForm.hdnProviderid.value = '';
      vForm.strRepresentante.value = '';
    }
    
    if(vForm.v_unidadjer.value!=null && vForm.v_unidadjer.value!='' && vForm.v_unidadjer.value>0){
      vForm.hdnSalesstructid.value = vForm.v_unidadjer.value;
      vForm.strUnidadJerarquica.value = vForm.v_unidadjerar.value;
    }
    
    /*vForm.strRepresentante.value = '';
    
    try{
      if(vForm.cmbRepresentante.value != '')
        vForm.strRepresentante.value = vForm.cmbRepresentante.options[vForm.cmbRepresentante.selectedIndex].text;
      else
        vForm.strRepresentante.value = '';
    }catch(err){
    }*/
    
    /*
    alert('vForm.strRepresentante.value='+vForm.strRepresentante.value);
    alert('vForm.strUnidadJerarquica.value='+vForm.strUnidadJerarquica.value);

    alert('vForm.hdnProviderid.value='+vForm.hdnProviderid.value);
    alert('vForm.hdnSalesstructid.value='+vForm.hdnSalesstructid.value);
    */
    //------------------------------
    
    if (countfxSearchOrder>0){
        alert("Ya existe una consulta en proceso");
        return false;
    }			 
   
    
    if(fxValidateSearchOrder()) {
     
     countfxSearchOrder= countfxSearchOrder+1;
     
      vForm.hdnMethod.value = "searchOrder";
      vForm.action = "<%=request.getContextPath()%>/orderSearchServlet";            
      vForm.target = "bottomFrame";      
      vForm.submit();
    }
  }
	
	function fxValidateSearchOrder() {    
		var vForm = document.formdatos;
		if(fxIsSearchByNameOrId()) {
			fxFillHiddenByComboBox();
			return true;
		}
		else {      
			if(fxAnyOptionFilled()) {
				var bCreateDate = fxValidateDatesBetween(vForm.txtCreateDateFrom, vForm.txtCreateDateTill);
				if(!(bCreateDate)) {
					return false;
				}
			}	else {
				alert("Ingrese algún criterio de búsqueda");
				return false;				
			}
		}
		fxFillHiddenByComboBox();
		return true;
	}
	
	function fxFillHiddenByComboBox() {
		var vForm = document.formdatos; 
		vForm.hdnSubCategoria.value = vForm.cmbSubCategoria.options[vForm.cmbSubCategoria.selectedIndex].text;
		vForm.hdnDivisionNegocio.value = vForm.cmbDivisionNegocio.options[vForm.cmbDivisionNegocio.selectedIndex].text;
		/*try {
			vForm.hdnZona.value = vForm.cmbZona.options[vForm.cmbZona.selectedIndex].text;
		}
		catch(exception) {
		}
		try {
			vForm.hdnCoordinador.value = vForm.cmbCoordinador.options[vForm.cmbCoordinador.selectedIndex].text;
		}
		catch(exception) {
		}
		try {
			vForm.hdnSupervisor.value = vForm.cmbSupervisor.options[vForm.cmbSupervisor.selectedIndex].text;
		}
		catch(exception) {
		}
		try {
			vForm.hdnConsultorEjecutivo.value = vForm.cmbConsultorEjecutivo.options[vForm.cmbConsultorEjecutivo.selectedIndex].text;
		}
		catch(exception) {
		}*/
		vForm.hdnRegion.value = vForm.cmbRegion.options[vForm.cmbRegion.selectedIndex].text;
		vForm.hdnTienda.value = vForm.cmbTienda.options[vForm.cmbTienda.selectedIndex].text;
		vForm.hdnTipoFalla.value = vForm.cmbTipoFalla.options[vForm.cmbTipoFalla.selectedIndex].text;
		vForm.hdnSolucionNegocio.value = vForm.cmbSolucionNegocio.options[vForm.cmbSolucionNegocio.selectedIndex].text;

        //pzacarias
        vForm.hdnSegmentoCompania.value = vForm.cmbSegmentoCompania.options[vForm.cmbSegmentoCompania.selectedIndex].text;
	}



  function fxIsSearchByNameOrId() {
      var vForm = document.formdatos;
      txtNroOrden = vForm.txtNroOrden.value;
      hdnCustomerId = vForm.hdnCustomerId.value;
      txtNextelRep = vForm.txtNextel.value;
      txtImeiRep = vForm.txtImei.value;
      txtRangoCuenta = vForm.cmbRangoCuenta.value;
      txtSegmentoCompania = vForm.cmbSegmentoCompania.value;
      if(fxTrim(txtNroOrden)=="" && fxTrim(hdnCustomerId)=="" && fxTrim(txtNextelRep)=="" && fxTrim(txtImeiRep)=="" && fxTrim(txtRangoCuenta)=="" && fxTrim(txtSegmentoCompania)=="") {
          return false;
      }
      return true;
  }
	
	function fxValidateDatesBetween(txtDateStart, txtDateEnd) {
		if (txtDateStart.value==""  & txtDateEnd.value=="") {
			alert("Ingrese las fechas de Creación");
			txtDateStart.focus();
			return false;
		}
		if (txtDateStart.value!=""  & txtDateEnd.value=="") {
			if (fxAnyOptionFilled()){
				alert("Ingrese la fecha (HASTA) del incidente");
				txtDateEnd.focus();
				return false;
			}
		}
		if (txtDateStart.value==""  & txtDateEnd.value!="") {
			if (fxAnyOptionFilled()){
				alert("Ingrese la fecha (DESDE) del incidente");
				txtDateStart.focus();
				return false;
			}
		}
		if (txtDateStart.value!=""  & txtDateEnd.value!="") {
			if (!isValidDate(txtDateStart.value)){
				txtDateStart.focus();
				txtDateStart.select();
				return false;
			}
			if (!isValidDate(txtDateEnd.value)){
				txtDateEnd.focus();
				txtDateEnd.select();
				return false;
			}
			
			/* Diferencia Maxima de 6 meses con Fecha Inicio para no cargar el servidor */
			var dateInitial  =  txtDateStart.value;
			var dateFinal    =  txtDateEnd.value;
			var monthaux      =  0;
			var datePat = /^(\d{1,2})(\/|-)(\d{1,2})\2(\d{4})$/;
			matchArray = dateInitial.match(datePat);
			if (matchArray != null) {
				day = matchArray[1];
				month = matchArray[3];
				year = matchArray[4];
				yearaux  = parseInt(year);
				monthaux = parseFloat(month) ;
				monthaux = monthaux + 6;
				if (monthaux > 12) {
					monthaux = monthaux - 12;
					yearaux = yearaux + 1;
				}
				if (monthaux < 9) {
					month = "0" + monthaux;
				} else{
					month = monthaux + "";
				}
				year = yearaux + "";
				dateaux = day.toString() + "/" + month.toString() + "/" + year.toString();
				
				if (trxTime(dateFinal) < trxTime(dateInitial)) {
					alert("La fecha desde debe ser menor que la fecha hasta.");
					txtDateStart.focus();
					txtDateStart.select();
					return false;
				}
				
				if (trxTime(dateFinal)>trxTime(dateaux)) {
					alert("La diferencia entre las fechas debe ser máximo de seis meses");
					txtDateEnd.focus();
					txtDateEnd.select();
					return false;
				}
			}
		}
		return true;   
	}
	
	function fxValidateTextInput(xinput, tipval) {
		var xkey = event.keyCode;
		if(tipval=="INT") {
			if ((xkey < 48) || (xkey > 57)) {
				event.returnValue = false;
			}
		}
		if(tipval=="DEC") {
			if (((xkey < 46) || (xkey > 57)) && (xkey != 47)) {
				event.returnValue = false;
			}
		}
		if(tipval=="STR") {
			if (((xkey != 32) && (xkey < 65)) || ((xkey > 90) && (xkey < 97))) {
				event.returnValue = false;
			}
		}
		if(tipval=="TLF") {
			if (((xkey != 32) && (xkey < 45)) || (xkey > 57)) {
				event.returnValue = false;
			}
		}
		if (tipval=="AFN") {
			if (((xkey != 32) && (xkey < 45)) || (xkey > 57) || ((xkey < 48) || (xkey > 57))) {
				event.returnValue = false;
			}
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
	
	//Funcion que obligara introducir algun criterio de busqueda.
	function fxAnyOptionFilled() {
		vForm = document.formdatos;
		
		txtNroSolicitud = vForm.txtNroSolicitud.value;
		cmbEstadoOrden = vForm.cmbEstadoOrden.value;
		cmbDivisionNegocio = vForm.cmbDivisionNegocio.value;
      cmbSolucion = vForm.cmbSolucionNegocio.value;
		try {
			cmbCategoria = vForm.cmbCategoria.value;
			cmbSubCategoria = vForm.cmbSubCategoria.value;
			//cmbZona = vForm.cmbZona.value;
			//cmbCoordinador = vForm.cmbCoordinador.value;
			//cmbConsultorEjecutivo = vForm.cmbConsultorEjecutivo.value;
		} catch(exception){
    }
    cmbRegion = vForm.cmbRegion.value;
    cmbTienda = vForm.cmbTienda.value;
    txtCreadoPor = vForm.txtCreadoPor.value;
    
    txtNroReparacion = vForm.txtNroReparacion.value;
    txtNextel = vForm.txtNextel.value;
    cmbTipoServicio = vForm.cmbTipoServicio.value;
    cmbTipoProceso = vForm.cmbTipoProceso.value;
    cmbModelo = vForm.cmbModelo.value;
    cmbTipoFalla = vForm.cmbTipoFalla.value;
    txtImei = vForm.txtImei.value;
    txtImeiCambio = vForm.txtImeiCambio.value;
    txtImeiPrestamo = vForm.txtImeiPrestamo.value;
    cmbSituacion = vForm.cmbSituacion.value;
    cmbTecnicoResponsable = vForm.cmbTecnicoResponsable.value;
    cmbEstadoReparacion = vForm.cmbEstadoReparacion.value;
    txtNroGuia = vForm.txtNroGuia.value;
    txtPropuesta = vForm.txtProposedId.value; //CBARZOLA 02/08/2009
    
    // Inicio - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 18/11/2010 
    cmbTipoEquipo = vForm.cmbTipoEquipo.value;
    // Fin - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 18/11/2010
    
    //De Jerarquía de Ventas:
    hdnProviderid = vForm.hdnProviderid.value;
    hdnSalesstructid = vForm.hdnSalesstructid.value;
    
    //AGAMARRA
    bCamposJerarquiaVacio = (hdnProviderid==null || hdnProviderid=='' || hdnProviderid<=0) && 
                       (hdnSalesstructid!=null || hdnSalesstructid=='' || hdnSalesstructid<=0);
    
    bCamposVacios = (cmbDivisionNegocio != "") || (cmbSolucion != "");
    
    bCamposOrdenesVacios = (fxTrim(txtNroSolicitud) == "") && (cmbEstadoOrden == "")
            && /*(cmbDivisionNegocio == "") &&*/ (cmbCategoria == "")
            && (cmbSubCategoria == "") /*&& (cmbZona == "") */
            /*&& (cmbCoordinador == "") && (cmbConsultorEjecutivo == "")*/
            && (cmbRegion == "") && (cmbTienda == "")
            && (fxTrim(txtCreadoPor) == "")
                   && (txtNroGuia == "") && (txtPropuesta == "");//CBARZOLA 02/08/2009
            
    // Inicio - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 18/11/2010         
    bCamposReparacionesVacios = (fxTrim(txtNroReparacion) == "") && (fxTrim(txtNextel) == "")
               && (cmbTipoServicio == "") && (cmbTipoProceso == "") 
               && (cmbTipoFalla == "") && (fxTrim(txtImei) == "")
               && (fxTrim(txtImeiCambio) == "") && (fxTrim(txtImeiPrestamo) == "")
               && (cmbSituacion == "") && (cmbTecnicoResponsable == "")
               && (cmbEstadoReparacion == "");                              
    
     bCamposReparacionesTipoEquipoVacios = (cmbTipoEquipo == "" && cmbModelo=="") || (cmbTipoEquipo == "" && cmbModelo!="");
    // Fin - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 18/11/2010 
    
    if (bCamposVacios && bCamposOrdenesVacios && bCamposJerarquiaVacio){      
       return false;
    }          
    // Inicio - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 18/11/2010            
  	   return !(bCamposOrdenesVacios && bCamposReparacionesVacios && bCamposJerarquiaVacio && bCamposReparacionesTipoEquipoVacios);
    // Fin - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 18/11/2010         
	}
   
// INICIO - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 30/09/2010 
//Funcion Ajax
function $(e){if(typeof e=='string')e=document.getElementById(e);return e};
function collect(a,f){var n=[];for(var i=0;i<a.length;i++){var v=f(a[i]);if(v!=null)n.push(v)}return n};

ajax={};
ajax.x=function(){try{return new ActiveXObject('Msxml2.XMLHTTP')}catch(e){try{return new ActiveXObject('Microsoft.XMLHTTP')}catch(e){return new XMLHttpRequest()}}};
ajax.serialize=function(f){var g=function(n){return f.getElementsByTagName(n)};var nv=function(e){if(e.name)return encodeURIComponent(e.name)+'='+encodeURIComponent(e.value);else return ''};var i=collect(g('input'),function(i){if((i.type!='radio'&&i.type!='checkbox')||i.checked)return nv(i)});var s=collect(g('select'),nv);var t=collect(g('textarea'),nv);return i.concat(s).concat(t).join('&');};
ajax.send=function(u,f,m,a){var x=ajax.x();x.open(m,u,true);x.onreadystatechange=function(){if(x.readyState==4)f(x.responseText)};if(m=='POST')x.setRequestHeader('Content-type','application/x-www-form-urlencoded');x.send(a)};
ajax.get=function(url,func){ajax.send(url,func,'GET')};
ajax.gets=function(url){var x=ajax.x();x.open('GET',url,false);x.send(null);return x.responseText};
ajax.post=function(url,func,args){ajax.send(url,func,'POST',args)};
ajax.update=function(url,elm){var e=$(elm);var f=function(r){e.innerHTML=r};ajax.get(url,f)};
ajax.submit=function(url,elm,frm){var e=$(elm);var f=function(r){e.innerHTML=r};ajax.post(url,f,ajax.serialize(frm))};

    
      function removeOptionSelected() 
      {
          var selectbox = document.getElementById("cmbModelo");
 
          var i;
          for(i=selectbox.options.length-1;i>=0;i--)
          {
            selectbox.remove(i);
          }
            
       }
       
      function enableField()
      {
        var selectbox = document.getElementById("cmbModelo");
        selectbox.disabled=false;
      }
       
      function disableField()
      {
        var selectbox = document.getElementById("cmbModelo");
        selectbox.disabled=true;
      } 
      
      // INICIO - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 09/11/2010 
      disableField();
       // FIN - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 09/11/2010
        
      function fxLoadListModel (){
          
          enableField();
          removeOptionSelected();
    
          var vForm = document.formdatos;
          tipoequipo = vForm.cmbTipoEquipo.value;
          vForm.hdnMethod.value = "loadModel";
          vForm.action = "<%=request.getContextPath()%>/generalServlet?cmbTipoEquipo="+ tipoequipo;
          vForm.target = "bottomFrame";      
          
          var url = "<%=request.getContextPath()%>/generalServlet?cmbTipoEquipo="+ tipoequipo + "&metodo=requestLoadModel";// + ajax.serialize(vForm);
          ajax.get(url,fxLoadAfterRes);
    }
    
      function fxLoadAfterRes (respuesta){
       // Inicio - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 18/11/2010 
        appendOptionLast("cmbModelo",""," ");
       // Inicio - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 18/11/2010 
      
        var str = respuesta.split("|");
        for(i=1; i < str.length  ; i++){
          op = str[i].split("*");
          appendOptionLast("cmbModelo",op[0],op[1]);
         }
	
     }
     
//Carga los modelos segun el tipo de equipo     
function appendOptionLast(selId, value, text)
{
  var elOptNew = document.createElement('option');
  elOptNew.text = text;
  elOptNew.value = value;
  var elSel = document.getElementById(selId);

  try {
    elSel.add(elOptNew, null); // standards compliant; doesn't work in IE
  }
  catch(ex) {
    elSel.add(elOptNew); // IE only
  }
}

// FIN - Team HP Argentina - UFMI -  PROYECTO HP-PTT - 30/09/2010 
   
    
</script>
<%
}catch(SessionException se) {
	se.printStackTrace();
  System.out.println("[JP_ORDER_SEARCH][Finalizó la sesión]");
	/*out.println("<script>alert('Su sesión ha finalizado. Ingrese nuevamente');</script>");
	String strUrl = "/portal/pls/portal/WEBSALES.NP_USER_AVAILABILITY_PL_PKG.PL_LOGOUT";*/
	//out.print("<script>parent.mainFrame.location.replace('"+strUrl+"');</script>");
}catch(Exception e) {
  out.println("<script>alert('"+MiUtil.getMessageClean(e.getClass() + " - " + e.getMessage())+"');</script>");
	logger.error(formatException(e));
}%>