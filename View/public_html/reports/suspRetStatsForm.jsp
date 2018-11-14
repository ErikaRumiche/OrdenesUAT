<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest"%>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants"%>
<%@ page import="oracle.portal.provider.v2.ProviderUser"%>
<%@ page import="pe.com.nextel.bean.DominioBean"%>
<%@ page import="pe.com.nextel.bean.PortalSessionBean"%>
<%@ page import="pe.com.nextel.service.GeneralService"%>
<%@ page import="pe.com.nextel.service.RepairService"%>
<%@ page import="pe.com.nextel.service.SessionService"%>
<%@ page import="pe.com.nextel.service.OrderSearchService"%>
<%@ page import="pe.com.nextel.form.RetentionForm"%>
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
  RepairService repairService = new RepairService();
  ArrayList valoresCombo = new ArrayList();
  HashMap hshDataMap = new HashMap();
  String strMessage = null;
  String strSessionId = "";
  String strLogin = StringUtils.defaultIfEmpty(request.getParameter("hdnLogin"),"");
  
  RetentionForm retentionForm = (RetentionForm) request.getAttribute("retentionForm");
  if (retentionForm == null) {
    retentionForm = new RetentionForm();
  }
  request.getSession(true).removeAttribute("retentionForm");
  	
  Calendar calendar = Calendar.getInstance();  
	calendar.add(Calendar.MONTH,0);
  
  Calendar calendar_low = Calendar.getInstance();
  calendar_low.add(Calendar.MONTH, -6);
  
  
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
  <input type="hidden" name="hdnRecordsRetrieve" value="0"/>
  <input type="hidden" name="hdnAreaCal" value=""/>
  <input type="hidden" name="hdnSuspensionReason" value=""/>
  <input type="hidden" name="hdnAsesor" value=""/>
  <input type="hidden" name="hdnTipoCliente" value=""/>
  <input type="hidden" name="hdnHerramientaRetencion" value=""/>  
  <input type="hidden" name="hdnLogin" value="<%=strLogin%>"/>
  <table border="0" cellspacing="1" cellpadding="1" align="center" width="100%">
    <tr>
      <td align="left">
        <table border="0" cellspacing="0" cellpadding="0" align="left">
          <tr>
            <td class="SectionTitleLeftCurve" width="8">&nbsp;&nbsp;</td>
            <td class="SectionTitle" width="100" align="center">Criterios de &nbsp;Selecci&oacute;n</td>
            <td class="SectionTitleRightCurve" width="8">&nbsp;&nbsp;</td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
  <table border="0" cellspacing="1" cellpadding="1" align="center" width="100%" class="RegionBorder">
    <tr>
      <td width="100%" colspan="2" align="center">
        <table border="0" cellspacing="1" cellpadding="1" align="center" width="100%">
          <tr>
            <td class="CellLabel" align="Left" width="40%">Fecha de Registro</td>
            <td class="CellContent" align="Center" width="20%">              
              <input type="text" name="txtFechaDesde" id="txtFechaDesde" size="10" maxlength="10" value="<%=MiUtil.getDate(calendar_low.getTime(), "dd/MM/yyyy")%>" onblur="this.value=fxTrim(this.value);"/>
              <a href="javascript:show_calendar('frmdatos.txtFechaDesde',null,null,'DD/MM/YYYY');" onmouseover="window.status='Fecha Desde';return true;" onmouseout="window.status='';return true;">
                <img src="<%=Constante.PATH_APPORDER_SERVER%>/images/show-calendar.gif" width="24" height="22" border="0"/>
              </a>
            </td>
            <td class="CellLabel" align="Center" width="10%">al</td>
            <td class="CellContent" align="Center" width="20%">              
              <input type="text" name="txtFechaHasta" id="txtFechaHasta" size="10" maxlength="10" value="<%=MiUtil.getDate(calendar.getTime(), "dd/MM/yyyy")%>" onblur="this.value=fxTrim(this.value);"/>
              <a href="javascript:show_calendar('frmdatos.txtFechaHasta',null,null,'DD/MM/YYYY');" onmouseover="window.status='Fecha Hasta';return true;" onmouseout="window.status='';return true;">
                <img src="<%=Constante.PATH_APPORDER_SERVER%>/images/show-calendar.gif" width="24" height="22" border="0"/>
              </a>
            </td>
          </tr>
        </table>
      </td>
      <td class="CellLabel" align="Left" width="10%">
        <P>Asesor</P>
      </td>
      <td class="CellContent" align="Left" width="13%">
        <input type="text" name="txtAsesor" id="txtEnvio" size="15" maxlength="15"/>
      </td>
      <td class="CellLabel" align="Left" width="8%">&nbsp;</td>
      <td class="CellContent" align="Left" width="13%">&nbsp;
      </td>
    </tr>
    <tr>
      <td class="CellLabel" align="Left" width="10%">Area de CAL</td>      
      <td class="CellContent" align="Left" width="13%" height="1">
        <select name="cmbCalArea" style="width: 100%">
          <% 	hshDataMap =  orderSearchService.getCalArea();
            valoresCombo = (ArrayList) hshDataMap.get("arrAreas");
            strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
            if (StringUtils.isBlank(strMessage)){%>
          <%= MiUtil.buildComboSelected(valoresCombo, "")%>
          <% }%>
        </select>
      </td>
      <td class="CellLabel" align="Left" width="10%" height="1">Tipo de Cliente</td>
      <td class="CellContent" align="Left" width="13%" height="1">
        <select name="cmbClient" style="width: 100%">
          <% 	hshDataMap =  orderSearchService.getClientType();
            valoresCombo = (ArrayList) hshDataMap.get("arrClientTypes");
            strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
            if (StringUtils.isBlank(strMessage)){%>
          <%= MiUtil.buildComboSelected(valoresCombo, "")%>
          <% }%>
        </select>
      </td>
      <td class="CellLabel" align="Left" width="8%" height="1">&nbsp;</td>
      <td class="CellContent" align="Left" width="13%" height="1">&nbsp;</td>
    </tr>
    <tr>
      
      <td class="CellLabel" align="Left" width="10%">Motivo Suspensi&oacute;n</td>      
      <td class="CellContent" align="Left" width="13%" height="1">
        <select name="cmbSuspensionReason" style="width: 100%">
          <% 	hshDataMap =  orderSearchService.getSuspensionReason();
            valoresCombo = (ArrayList) hshDataMap.get("arrReasons");
            strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
            if (StringUtils.isBlank(strMessage)){%>
          <%= MiUtil.buildComboSelected(valoresCombo, "")%>
          <% }%>
        </select>
      </td>
      <td class="CellLabel" align="Left" width="10%">Herramienta de Retenci&oacute;n</td>
      <td class="CellContent" align="Left" width="13%">
        <select name="cmbRetentionTool" style="width: 100%">
          <% 	hshDataMap =  orderSearchService.getRetentionTool();
            valoresCombo = (ArrayList) hshDataMap.get("arrTools");
            strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
            if (StringUtils.isBlank(strMessage)){%>
          <%= MiUtil.buildComboSelected(valoresCombo, "")%>
          <% }%>
        </select>
      </td>
      <td class="CellLabel" align="Left" width="8%">&nbsp;</td>
      <td class="CellContent" align="Left" width="13%">&nbsp;</td>
    </tr>
    <tr>
    </tr>
  </table>
  <table width="100%" border="0">
    <tr>
      <td align="Left">
        <input type="button" name="btnGeneral" value="Cuadro General" style="width: 100px;" onclick="fxCuadroGeneral();"/>
        <input type="button" name="BtnDetallado" value="Detallado" style="width: 100px;" onclick="fxDetallado();"/>
      </td>
    </tr>
  </table>
  
  <p/>
  <div id="idSearchProcessing" name="idSearchProcessing" style="overflow:auto; height:0px;">
  </div>
  <p>

</form>
<script type="text/javascript">

  function fxCuadroGeneral() {
    vForm = document.frmdatos;   
    
    fxLimpiarDatosHdn();		
    fxFillHiddenByComboBox();
    /* 
    // para cuando el reporte tenia que salir en excel al inicio
    var url = "<%=request.getContextPath()%>/reports/reportGeneralSuspension.jsp?"                 
               +"&as_fechaInicial="+vForm.txtFechaDesde.value
               +"&as_fechaFinal="+vForm.txtFechaHasta.value
               +"&as_areaCal="+vForm.hdnAreaCal.value
               +"&as_suspensionReason="+vForm.hdnSuspensionReason.value
               +"&hdnLogin="+vForm.hdnLogin.value;
      
    var popupWin = window.open(url, "reportGeneralSuspensionWindow","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,screenX=60,top=40,left=60,screenY=40,width=850,height=600");
    */
    
    //vForm.hdnMethod.value = "searchGeneral";      
    //vForm.action = "<%=request.getContextPath()%>/orderSearchServlet";            
    //vForm.target = "bottomFrame";
    //vForm.submit();
    
    if  (fxValidateDatesBetweenA(vForm.txtFechaDesde,vForm.txtFechaHasta)){    
      vForm.hdnMethod.value = "searchRetention";      
      vForm.action = "<%=request.getContextPath()%>/orderSearchServlet";                   
      vForm.submit();
    }
    
  }
  
    function fxDetallado() {
    vForm = document.frmdatos;   
    
    fxLimpiarDatosHdn();		
    fxFillHiddenByComboBox();
    
    if  (fxValidateDatesBetweenA(vForm.txtFechaDesde,vForm.txtFechaHasta)){
     
      var url = "<%=request.getContextPath()%>/reports/reportDetalladoSuspension.jsp?"                 
                 +"&as_fechaInicial="+vForm.txtFechaDesde.value
                 +"&as_fechaFinal="+vForm.txtFechaHasta.value
                 +"&as_areaCal="+vForm.hdnAreaCal.value
                 +"&as_suspensionReason="+vForm.hdnSuspensionReason.value               
                 +"&as_asesor="+vForm.hdnAsesor.value
                 +"&as_tipoCliente="+vForm.hdnTipoCliente.value
                 +"&as_retentionTool="+vForm.hdnHerramientaRetencion.value
                 +"&hdnLogin="+vForm.hdnLogin.value;
      
      var popupWin = window.open(url, "reportDetalladoSuspensionWindow","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,screenX=60,top=40,left=60,screenY=40,width=850,height=600");
    }
    
  }
  
  function fxLimpiarDatosHdn(){
    var vForm = document.frmdatos; 
    
    vForm.hdnAreaCal.value = "";
    vForm.hdnSuspensionReason.value = "";
    vForm.hdnAsesor.value = "";
    vForm.hdnTipoCliente.value = "";
    vForm.hdnHerramientaRetencion.value = "";
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
  
  function fxFillHiddenByComboBox() {
    //fxLimpiarDatos();
		var vForm = document.frmdatos;
    
    try {
      vForm.hdnAreaCal.value = vForm.cmbCalArea.options[vForm.cmbCalArea.selectedIndex].text;
    } catch(exception) {}
    try {
      vForm.hdnSuspensionReason.value = vForm.cmbSuspensionReason.options[vForm.cmbSuspensionReason.selectedIndex].value;
    } catch(exception) {} 
    
    try {
      vForm.hdnAsesor.value = vForm.txtAsesor.value;
    } catch(exception) {}
    try {
      vForm.hdnTipoCliente.value = vForm.cmbClient.options[vForm.cmbClient.selectedIndex].text;
    } catch(exception) {} 
    try {
      vForm.hdnHerramientaRetencion.value = vForm.cmbRetentionTool.options[vForm.cmbRetentionTool.selectedIndex].value;
    } catch(exception) {}    
    
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

</script>
<%      
} catch(SessionException se) {
  se.printStackTrace();
  logger.error("[suspRetStatsFormShowPage.jsp][Finalizó la sesión]");
  out.println("<script>alert('Su sesión ha finalizado. Ingrese nuevamente');</script>");
  String strUrl = "/portal/pls/portal/WEBSALES.NP_USER_AVAILABILITY_PL_PKG.PL_LOGOUT";
} catch(Exception e) {
  e.printStackTrace();
  String strMessageExceptionGeneralStart = "";
  strMessageExceptionGeneralStart = MiUtil.getMessageClean(e.getMessage());
  out.println("<script>alert('"+strMessageExceptionGeneralStart+"');</script>");   
}
%>
