<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest"%>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants"%>
<%@ page import="oracle.portal.provider.v2.ProviderUser"%>
<%@ page import="pe.com.nextel.bean.DominioBean"%>
<%@ page import="pe.com.nextel.bean.PortalSessionBean"%>
<%@ page import="pe.com.nextel.service.GeneralService"%>
<%@ page import="pe.com.nextel.service.OrderSearchService"%>
<%@ page import="pe.com.nextel.service.SessionService"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="pe.com.nextel.util.GenericObject"%>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<%@ page import="pe.com.nextel.exception.SessionException"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.*"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Calendar" %>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="org.apache.log4j.Logger"%>
<%@ page import="org.displaytag.tags.*"%>
<%@ page import="org.displaytag.util.*"%>

<%! 
protected static Logger logger = Logger.getLogger(HttpServlet.class);

protected String formatException(Throwable e) {
	return GenericObject.formatException(e);
}
%>
<%

String strSessionId1 = null;

try {
  GeneralService generalService = new GeneralService();
  OrderSearchService orderSearchService = new OrderSearchService();
  ArrayList valoresCombo = new ArrayList();
  HashMap hshDataMap = new HashMap();
  String strMessage = null;
  String strSessionId = "";
  String strLogin = StringUtils.defaultIfEmpty(request.getParameter("hdnLogin"),"");
  
  //PortalSessionBean portalSessionBean = null;
  
  /*
  PortalSessionBean portalSessionBean = (PortalSessionBean) SessionService.getUserSession(strSessionId);
  if (portalSessionBean == null) {
    logger.error("No se encontró la sesión de Java ->" + strSessionId);
    throw new SessionException("La sesión finalizó");
  }*/
  
  Calendar calendar = Calendar.getInstance();  
	calendar.add(Calendar.MONTH,0);
  
  Calendar calendar_low = Calendar.getInstance();
  calendar_low.add(Calendar.DATE, -2);//fecha desde: se restan 2 días a la fecha de hoy
  
  // para la ventana de buscar cliente
  String strRutaContext=request.getContextPath();   
  String URL_Order_CompaniaBuscar = strRutaContext+"/htdocs/jsp/Order_CompaniaBuscar.jsp";      
  
  
%>
<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/displaytag.css"></link>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
<script type="text/javascript" SRC="<%=Constante.PATH_APPORDER_SERVER%>/Resource/date-picker.js"></SCRIPT>
<script type="text/javascript">

	var vValor = window.Event ? true : false;

	function fxOnlyNumber(evt){
		// NOTA: '0' = 48, '9' = 57
		var vKey = vValor ? evt.which : evt.keyCode;
		return (vKey <= 13 || (vKey >= 48 && vKey <= 57));
	}

  function fxValidateIMEI(txtIMEI){
    if (txtIMEI.value.length > 1){       
      RegExpImei = /^\d{15}$/;
      if (!RegExpImei.test(txtIMEI.value)){
        alert("La longuitud del IMEI debe ser de 15 dígitos");
        txtIMEI.select();
        txtIMEI.focus();
        return false;
      }
      return true;
    }
  }

</script>
<form name="frmdatos" action="<%=request.getContextPath()%>/orderSearchServlet" method="POST">
  <input type="hidden" name="hdnNumRegistros" id="hdnNumRegistros" value="<%=Constante.NUM_REGISTROS_X_PAGINAS%>">
  <input type="hidden" name="hdnNumPagina" id="hdnNumPagina" value="1">
  <input type="hidden" name="hdnMethod" value=""/>
  <input type="hidden" name="hdnToken" value=""/>
  <input type="hidden" name="hdnProceso" value=""/>
  <input type="hidden" name="hdnEstado" value=""/>  
  <input type="hidden" name="hdnRecordsRetrieve" value="0"/>
  <input type="hidden" name="hdnLogin" value="<%=strLogin%>"/>
  <table border="0" cellspacing="1" cellpadding="1" align="center" width="100%">
    <tr>
      <td align="left">
        <table border="0" cellspacing="0" cellpadding="0" align="left">
          <tr>
            <td class="SectionTitleLeftCurve" width="8">&nbsp;&nbsp;</td>
            <td class="SectionTitle" width="100" align="center">Criterios de Filtro</td>
            <td class="SectionTitleRightCurve" width="8">&nbsp;&nbsp;</td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
  <table border="0" cellspacing="1" cellpadding="1" align="center" width="100%" class="RegionBorder">
    <tr>
      <td class="CellLabel" align="Left" width="10%">&nbsp;&nbsp;&nbsp;Proceso</td>
      <td class="CellContent" align="Left" width="13%">
        <select name="cmbProceso" style="width: 100%">
          <%  hshDataMap = orderSearchService.getActionID("MASS_PROCESS_LOG");
              valoresCombo = (ArrayList) hshDataMap.get("arrActionID");
              strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
              if (StringUtils.isBlank(strMessage)) {%>
                <%=MiUtil.buildComboSelected(valoresCombo, "")%>
            <%}%>
        </select>
        <input type="hidden" name="hdnTipoReparacion"/>
      </td>
      <td class="CellLabel" align="Left" width="16%">&nbsp;&nbsp;&nbsp;Estado</td>
    	<td class="CellContent" width="16%">
        <select name="cmbEstado" style="width: 100%" >
        <%	hshDataMap =  orderSearchService.getEstadoPMC("MASS_PROCESS_STATUS_LOG");
            valoresCombo = (ArrayList) hshDataMap.get("arrEstadoPMC");
            strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
            if (StringUtils.isBlank(strMessage)){%>
                  <%=MiUtil.buildComboSelected(valoresCombo, "")%>
            <%}%>
        </select>
      </td>
      <td class="CellLabel" align="Left" width="16%">&nbsp;&nbsp;&nbsp;Tel&eacute;fono</td>
      <td class="CellContent" align="Left" width="16%">
        <input type="text" name="txtTelefono" id="txtIMEI" size="15" maxlength="9" style="width: 100%" onKeypress="if ((event.keyCode>57)||(event.keyCode<48)) event.returnValue = false;"/>
        <input type="hidden" name="hdnProveedor"/>
      </td>
    </tr>
    <tr>
      <td width="100%" colspan="2" align="center">
        <table border="0" cellspacing="1" cellpadding="1" align="center" width="100%">
          <tr>
            <td class="CellLabel" align="Left" width="45%">&nbsp;&nbsp;&nbsp;Fecha de Proceso</td>
            <td class="CellContent" align="Center" width="20%">   
            
            
              <input type="text" name="txtFechaDesde" id="txtFechaDesde" size="10" maxlength="10" value="<%=MiUtil.getDate(calendar_low.getTime(), "dd/MM/yyyy")%>" onblur="this.value=fxTrim(this.value);"/>
              <a href="javascript:show_calendar('frmdatos.txtFechaDesde',null,null,'DD/MM/YYYY');" onmouseover="window.status='Fecha Desde';return true;" onmouseout="window.status='';return true;">
                <img src="<%=Constante.PATH_APPORDER_SERVER%>/images/show-calendar.gif" width="24" height="22" border="0"/>
              </a>
              
              
            </td>
            <td class="CellLabel" align="Center" width="15%">al</td>
            <td class="CellContent" align="Center" width="20%">              
              <input type="text" name="txtFechaHasta" id="txtFechaHasta" size="10" maxlength="10" value="<%=MiUtil.getDate(calendar.getTime(), "dd/MM/yyyy")%>" onblur="this.value=fxTrim(this.value);"/>
              <a href="javascript:show_calendar('frmdatos.txtFechaHasta',null,null,'DD/MM/YYYY');" onmouseover="window.status='Fecha Hasta';return true;" onmouseout="window.status='';return true;">
                <img src="<%=Constante.PATH_APPORDER_SERVER%>/images/show-calendar.gif" width="24" height="22" border="0"/>
              </a>
            </td>
          </tr>
        </table>
      </td>      
        <td class="CellLabel" align="Left" width="16%">&nbsp;&nbsp;&nbsp;Razón Social</td>
      </td>
      <td class="CellContent" align="Left" width="13%">
        <input type="text" name="txtRazonSocial" id="txtEnvio" size="50" maxlength="50"/>
      </td>
      <td class="CellLabel" align="Left" width="8%">&nbsp;&nbsp;&nbsp;</td>
      <td class="CellContent" align="Left" width="13%">&nbsp;
      </td>
    </tr>
    <tr>
    </tr>
  </table>
  <table width="100%" border="0">
    <tr>
      <td align="left" class="CellContent">
        <input type="button" name="btnBuscar" value="Buscar" style="width: 100px;" onclick="fxSearchPCM();"/>
        <input type="button" name="cmdExecute" value="Exportar a Excel" style="width:150px;" onClick="javascript:fxPCMExcel();"/>
      </td>
      <td class="CellContent">&nbsp;
      </td>
      <td align="Right" colspan="2" class="CellContent">&nbsp;</td>
    </tr>
  </table>
  <p/>
  <div id="idSearchProcessing" name="idSearchProcessing" style="overflow:auto; height:0px;">
  </div>
  <p>
</form>
<script type="text/javascript">

  function fxSearchPCM() {
    vForm = document.frmdatos;   
    
    fxLimpiarDatosHdn();		
    fxFillHiddenByComboBox();
    
    if  (fxValidateDatesBetweenA(vForm.txtFechaDesde,vForm.txtFechaHasta)) {    
    
      if (vForm.cmbProceso.value != ""){    
         vForm.hdnMethod.value = "searchPCM";      
         vForm.action = "<%=request.getContextPath()%>/orderSearchServlet";                   
         vForm.submit();
      }else{
         alert("Debe seleccionar el tipo de proceso");
         vForm.cmbProceso.focus();
      }
      
    } // fin de la validacion de la fecha fxValidateDatesBetweenA
    
  }
  
  
  function fxPCMExcel() {
    vForm = document.frmdatos;   
        
    fxLimpiarDatosHdn();		
    fxFillHiddenByComboBox();
    if (vForm.cmbProceso.value != "") { 
      vForm = document.frmdatos;
      vForm.action = "/portal/pls/portal/!ORDERS.NP_SUSPENSION_REPORTS_PL_PKG.PL_REPORT_XLS";
      vForm.target = "_self";
      vForm.method = "POST";
      vForm.submit();
      return;      
    } else {
       alert("Debe seleccionar el tipo de proceso");
       vForm.cmbProceso.focus();
    }   
  }

  /* Ini Funcion valida fecha*/
   function fxValidateDatesBetweenA(txtDateStart, txtDateEnd) {
      if (txtDateStart.value!=""  & txtDateEnd.value=="") {
         if (fxAnyOptionFilled()){
            alert("Ingrese la fecha (HASTA)");
               txtDateEnd.focus();
               return false;
         }
      }
      if (txtDateStart.value==""  & txtDateEnd.value!="") {
         if (fxAnyOptionFilled()){
            alert("Ingrese la fecha (DESDE)");
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
                            alert("La diferencia entre las fechas debe ser como máximo de seis meses");
                            txtDateEnd.focus();
                            txtDateEnd.select();
                            return false;
            }
         }
      }
      return true;   
   }
   

   /* Fin Función valida fecha*/
  
  function fxLimpiarDatosHdn(){
    var vForm = document.frmdatos; 
    
    vForm.hdnProceso.value = "";
    vForm.hdnEstado.value = "";
  } 
  
  function fxFillHiddenByComboBox() {
    //fxLimpiarDatos();
		var vForm = document.frmdatos;
    
    try {
      vForm.hdnProceso.value = vForm.cmbProceso.options[vForm.cmbProceso.selectedIndex].value;
    } catch(exception) {}    
    
    try {
      vForm.hdnEstado.value = vForm.cmbEstado.options[vForm.cmbEstado.selectedIndex].value;
    } catch(exception) {}    
  }
  
  function fxMostrarTodos(){
    var vForm = document.frmdatos; 
        
    vForm = document.frmdatos;    
    
      alert("vForm.hdnProceso.value : "+vForm.hdnProceso.value);
      alert("vForm.hdnEstado.value : "+vForm.hdnEstado.value);
      alert("vForm.txtTelefono.value : "+vForm.txtTelefono.value);      
      alert("vForm.txtFechaDesde.value : "+vForm.txtFechaDesde.value);
      alert("vForm.txtFechaHasta.value : "+vForm.txtFechaHasta.value);
      alert("vForm.txtRazonSocial.value : "+vForm.txtRazonSocial.value);
      
  }


  function fxValidateIMEI(txtIMEI){
    if (txtIMEI.value.length > 1){
      RegExpImei = /^\d{15}$/;
      if (!RegExpImei.test(txtIMEI.value)){
        alert("La longuitud del IMEI debe ser de 15 dígitos");
        txtIMEI.select();
        txtIMEI.focus();
        return false;
      }else{
        return true;
      }
    }
  }
  
  function searchConpany(){
    // ventana que llama a buscar usuarios
    // falta ver como se haria para que levante desde el iFrame o pasarle el portalSession
   }

  function fxSearchOnList(txtIMEI){
    vForm = document.frmdatos;
    intFilas = vForm.hdnRecordsRetrieve.value;
    var strImei = vForm.txtIMEI.value;

    /*if (fxValidateIMEI(txtIMEI)){
      if (intFilas != 0){
        if (intFilas > 1){
          for (i= 0; i < vForm.chkRepairId.length;i++){
            if (strImei == vForm.hdnIMEIDefectuoso[i].value){
              vForm.chkRepairId[i].checked = true;
            }
          }
        }else{
          if (strImei == vForm.hdnIMEIDefectuoso.value){
            vForm.chkRepairId.checked = true;
          }
        }
      }else{
        alert("No existen registros");
      }
      vForm.txtIMEI.value="";    
      vForm.txtIMEI.focus();    
    }*/
    
    
    
    /* Modificación TM 08/07/2009 */
    
   var strInicio = 0;       
   //var strFin = vForm.hdnIMEIDefectuoso.length - 1;    
   var strPosicion;           
   
      if (fxValidateIMEI(txtIMEI)){
         if (intFilas != 0){
            if (intFilas > 1){
               
               var strFin = vForm.hdnIMEIDefectuoso.length - 1;    
            
               while (strInicio <= strFin ){      
                  strPosicion = fxEntero((strInicio + strFin) / 2);
                  if (vForm.hdnIMEIDefectuoso[strPosicion].value == strImei){
         
                     vForm.chkRepairId[strPosicion].focus();
                     
                     vForm.txtIMEI.value="";    
                     vForm.txtIMEI.focus();
                     
                     return vForm.chkRepairId[strPosicion].checked = true;                
                     
                  }   
                  if (strImei > vForm.hdnIMEIDefectuoso[strPosicion].value ){
                     strInicio = strPosicion + 1;      
                  }else {
                     strFin = strPosicion - 1;            
                  }          
               }
         
               alert("El equipo: " + strImei + " no se encuentra en la lista" );
            }
         } /* Fin del if que valida que sea diferente de 0 */      
                     
      }/*Fin del if que valida el IMEI*/         
          
  }
  
  
  
  
  /* Ini Funcion valida fecha*/
   
   function fxValidateDatesBetween(txtDateStart, txtDateEnd) {
      if (txtDateStart.value!=""  & txtDateEnd.value=="") {
         if (fxAnyOptionFilled()){
            alert("Ingrese la fecha (HASTA)");
               txtDateEnd.focus();
               return false;
         }
      }
      if (txtDateStart.value==""  & txtDateEnd.value!="") {
         if (fxAnyOptionFilled()){
            alert("Ingrese la fecha (DESDE)");
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
                            alert("La diferencia entre las fechas debe ser máximo de un mes");
                            txtDateEnd.focus();
                            txtDateEnd.select();
                            return false;
            }
         }
      }
      return true;   
   }
   

   /* Fin Función valida fecha*/
  
  
  
  
  
  
   function fxEntero(valor){  
   //intento convertir a entero.  
   //si era un entero no le afecta, si no lo era lo intenta convertir  
      valor = parseInt(valor);  
  
    //comprobamos si es un valor entero  
      if (isNaN(valor)) {  
          //no es entero 0  
          return 0;  
      }else{  
          //es un valor entero  
          return valor;  
      }  
   }  
   

  function fxDesMarcar(){
    vForm = document.frmdatos;
    intFilas = vForm.hdnRecordsRetrieve.value;
    if (intFilas != 0){
      if (intFilas > 1){
        for (i= 0; i < vForm.chkRepairId.length;i++){
          vForm.chkRepairId[i].checked = vForm.chkAll.checked;
        }
      }else{
        vForm.chkRepairId.checked = vForm.chkAll.checked;
      }
    }else{
      alert("No existen registros");
    }
  }
  
  function fxSearchRepair() {
    vForm = document.frmdatos;
    vForm.chkAll.checked = false;
    
    //alert("Desde : "+vForm.txtFechaDesde.value);
    //alert("Hasta : "+vForm.txtFechaHasta.value);
    
    if  (fxValidateDatesBetween(vForm.txtFechaDesde,vForm.txtFechaHasta)){     
      if (vForm.cmbTipoTransaccion.value != ""){
         vForm.hdnMethod.value = "searchRepairForProcessing";      
         vForm.action = "<%=request.getContextPath()%>/repairServlet";            
         vForm.target = "bottomFrame";
         vForm.submit();
      
      }else{
         alert("Debe seleccionar el tipo de transacción");
         vForm.cmbTipoTransaccion.focus();
      }
    }
    
  }
   
  
   function fxMarcados() {                  
      vForm = document.frmdatos;
      var intChecks = 0;       
      intFilas = vForm.hdnRecordsRetrieve.value;
      
      if (intFilas != 0){
         if (intFilas > 1){                                    
            for (i= 0; i < vForm.chkRepairId.length;i++) {                                                         
               if (vForm.chkRepairId[i].checked) {
                  intChecks = intChecks + 1;                  
               }                              
            }         
            vForm.txtMarcados.value = intChecks;                  
         }         
      }         
  } 
   
   
   
	function fxValidateSearchRepair() {
		var vForm = document.frmdatos;
    if(fxAnyOptionFilled()) {
      var bCreateDate = fxValidateDatesBetween(vForm.txtCreateDateFrom, vForm.txtCreateDateTill);
			if(!(bCreateDate)) {
        return false;
			}
		}else{
      alert("Ingrese algún criterio de búsqueda");
			return false;				
    }
		return true;
	}

  function fxAnyOptionFilled() {
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
				monthaux = monthaux + 3;
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
					alert("La diferencia entre las fechas debe ser de tres meses");
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

</script>
<%      
} catch(SessionException se) {
  se.printStackTrace();
  logger.error("[suspPCMForm.jsp][Finalizó la sesión]");
  out.println("<script>alert('Su sesión ha finalizado. Ingrese nuevamente');</script>");
  String strUrl = "/portal/pls/portal/WEBSALES.NP_USER_AVAILABILITY_PL_PKG.PL_LOGOUT";
} catch(Exception e) {
  e.printStackTrace();
  String strMessageExceptionGeneralStart = "";
  strMessageExceptionGeneralStart = MiUtil.getMessageClean(e.getMessage());
  out.println("<script>alert('"+strMessageExceptionGeneralStart+"');</script>");   
}
%>
