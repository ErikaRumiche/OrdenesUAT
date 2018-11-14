<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.GeneralService"%>
<%@ page import="pe.com.nextel.bean.DominioBean"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%
try {
	String strReparacionId = StringUtils.defaultString(request.getParameter("an_repairid"),"0");
	long lReparacionId =  Long.parseLong(strReparacionId);
    String strError;
    GeneralService objGeneralService = new GeneralService();
    ArrayList valoresCombo = new ArrayList();
%>
<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script type="text/javascript" src="/web_operacion/js/syncscroll.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
<script type="text/javascript">

	function fxAddSelectedClient(fuente, destino) {
		if(fxVerifyMultipledLines(fuente) > 1) {
			var i=0;
			while (i<fuente.options.length) {
				if(fxVerifyMultipledLines(fuente) > 1) {
					if(fuente.options[i].selected == true) {
						try {
							var indice1 = i;
							var indice2 = destino.length;
							option = new Option(fuente.options[indice1].text, fuente.options[indice1].value);
							destino.options[indice2] = option;
							fuente.options[indice1] = null;
						}
						catch(exception) {
							alert(exception.description);
						}
					}
					else{
						i++;
					}
				}
				else {
					i=fuente.options.length;
				}
			}
		}
		if(fxVerifyMultipledLines(fuente) <= 1) {
			fxManageSwapSelect(fuente, destino);
		}
	}
	
	function fxVerifyMultipledLines(fuente) {
		var cont = 0;
		for(i=0;i<fuente.options.length;i++) {
			if(fuente.options[i].selected == true) {
				cont++;
			}
		}
		return cont;
	}
	
	function fxManageSwapSelect(fuente, destino) {
		try {
			var indice1 = fuente.options.selectedIndex;
			try {
				if(indice1 == -1) {
					fuente.options[0].selected=true;
					indice1 = fuente.options.selectedIndex;
				}
			}
			catch(exception) {
			}
			var indice2 = destino.length;
			option = new Option(fuente.options[indice1].text, fuente.options[indice1].value);
			destino.options[indice2]=option;
			fuente.options[indice1] = null;
			try {
				fuente.options[indice1].selected=true;
			}
			catch(exception) {
				fuente.options[indice1-1].selected=true;
			}
		}
		catch(exception) {
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

<input type="hidden" name="hndReparacionId" value="<%=lReparacionId%>">

<table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
	<tr>
		<td align="left">
			<table border="0" cellspacing="0" cellpadding="0" align="left">
				<tr>
					<td class="SectionTitleLeftCurve" width="8">&nbsp;&nbsp;</td>
					<td class="SectionTitle" width="100" align="center">Equipo</td>
					<td class="SectionTitleRightCurve" width="8">&nbsp;&nbsp;</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td align="center">
			<table border="0" cellspacing="1" cellpadding="1" align="center" class="BannerSecondaryLink" width="100%">
				<tr>
					<td class="CellLabel" align="center">Nextel</td>
					<td class="CellLabel" align="center">IMEI</td>
					<td class="CellLabel" align="center">SIM</td>
					<td class="CellLabel" align="center">Serie</td>
					<td class="CellLabel" align="center">Producto</td>
					<td class="CellLabel" align="center">Plan Tarifario</td>
					<td class="CellLabel"></td>
				</tr>
				<tr>
					<td class="CellContent">
						<input type="text" name="txtNextel" size="10" maxlength="12" readonly="readonly"/>
					</td>
					<td class="CellContent">
						<input type="text" name="txtImei" size="15" maxlength="15" readonly="readonly"/>
					</td>
					<td class="CellContent">
						<input type="text" name="txtSim" size="15" maxlength="15" readonly="readonly"/>
					</td>
					<td class="CellContent">
						<input type="text" name="txtSerie" size="15" maxlength="15" readonly="readonly"/>
					</td>
					<td class="CellContent">
						<input type="text" name="txtProducto" size="15" maxlength="15" readonly="readonly"/>
					</td>
					<td class="CellContent">
						<input type="text" name="txtPlanTarifario" size="15" maxlength="15" readonly="readonly"/>
					</td>
					<td class="CellContent">
					</td>
				</tr>
				<tr>
					<td class="CellLabel" align="center">Garantia</td>
					<td class="CellLabel" align="center">Alquilado</td>
					<td class="CellLabel" align="center">Black List</td>
					<td class="CellLabel" align="center">Fecha Emisión</td>
					<td class="CellLabel" align="center">Tipo Accesorio</td>
					<td class="CellLabel" align="center">Modelo Accesorio</td>
					<td class="CellLabel" align="center">Sem. Fabricación</td>
				</tr>
				<tr>
					<td class="CellContent">
						<input type="text" name="txtGarantia" size="10" maxlength="12" readonly="readonly"/>
					</td>
					<td class="CellContent">
						<input type="text" name="txtAlquilado" size="15" maxlength="15" readonly="readonly"/>
					</td>
					<td class="CellContent">
						<input type="text" name="txtBlackList" size="15" maxlength="15" readonly="readonly"/>
					</td>
					<td class="CellContent">
						<input type="text" name="txtFechaEmision" size="10" maxlength="10" readonly="readonly"/>
					</td>
					<td class="CellContent">
						<select name="cmbTipoAccesorio" style="width: 75%">
							<option value=""></option>
						</select>
					</td>
					<td class="CellContent">
						<select name="cmbModeloAccesorio" style="width: 75%">
							<option value=""></option>
						</select>
					</td>
					<td class="CellContent">
						<input type="text" name="txtSemFabricacion" size="15" maxlength="15" readonly="readonly"/>
					</td>
				</tr>
				<tr>
					<td class="CellLabel" align="center">Casos Usuario</td>
					<td class="CellLabel" align="center">Casos Equipo</td>
					<td class="CellLabel" align="center">Casos Cliente</td>
					<td class="CellLabel" align="center">Recepción</td>
					<td class="CellLabel" align="center">Motorizado</td>
					<td class="CellLabel" align="center">Proceso</td>
					<td class="CellLabel" align="center">Código OSIPTEL</td>
				</tr>
				<tr>
					<td class="CellContent">
						<input type="text" name="txtCasosUsuario" size="10" maxlength="12" readonly="readonly"/>
					</td>
					<td class="CellContent">
						<input type="text" name="txtCasosEquipo" size="15" maxlength="15" readonly="readonly"/>
					</td>
					<td class="CellContent">
						<input type="text" name="txtCasosCliente" size="15" maxlength="15" readonly="readonly"/>
					</td>
					<td class="CellContent">
						<input type="text" name="txtRecepcion" size="15" maxlength="15"/>
					</td>
					<td class="CellContent">
						<input type="text" name="txtMotorizado" size="15" maxlength="15"/>
					</td>
					<td class="CellContent">
						<input type="text" name="txtProceso" size="15" maxlength="15"/>
					</td>
					<td class="CellContent">
						<input type="text" name="txtCodigoOsiptel" size="15" maxlength="15" readonly="readonly"/>
					</td>
				<tr>
			</table>
		</td>
	</tr>
	<tr>
		<td height="15px">
		</td>
	</tr>
	<tr>
		<td align="left">
			<table border="0" cellspacing="0" cellpadding="0" align="left">
				<tr>
					<td class="SectionTitleLeftCurve" width="8">&nbsp;&nbsp;</td>
					<td class="SectionTitle" width="100" align="center">Reparación</td>
					<td class="SectionTitleRightCurve" width="8">&nbsp;&nbsp;</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td align="center">
			<table border="0" cellspacing="1" cellpadding="1" align="center" class="BannerSecondaryLink" width="100%">
				<tr>
					<td class="CellLabel" align="center">Fecha Recepción</td>
					<td class="CellLabel" align="center">Contacto Nombre</td>
					<td class="CellLabel" align="center">Contacto Apellido</td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td class="CellContent">
						<input type="text" name="txtFechaRecepcion" size="10" maxlength="10" value="<%=MiUtil.getDate(Calendar.getInstance().getTime(), "dd/MM/yyyy")%>" onblur="this.value=fxTrim(this.value);">
						<a href="javascript:show_calendar('frmdatos.txtFechaRecepcion',null,null,'DD/MM/YYYY');" onmouseover="window.status='Fecha Recepción';return true;" onmouseout="window.status='';return true;">
						<img src="/websales/images/show-calendar.gif" width="24" height="22" border="0"></a>&nbsp;<i>DD/MM/YYYY</i>
					</td>
					<td class="CellContent">
						<input type="text" name="txtContactoNombre" size="15" maxlength="15"/>
					</td>
					<td class="CellContent">
						<input type="text" name="txtContactoApellido" size="15" maxlength="15"/>
					</td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td class="CellLabel" align="center">Contacto Recojo</td>
					<td class="CellLabel" align="center" colspan="3">Dirección Recojo</td>
					<td class="CellLabel" align="center" colspan="3">Reporte Cliente</td>
				</tr>
				<tr>
					<td class="CellContent">
						<textarea name="txtContactoRecojo" cols="10" rows="3"></textarea>
					</td>
					<td class="CellContent" colspan="3">
						<textarea name="txtDireccionRecojo" cols="60" rows="3"></textarea>
					</td>
					<td class="CellContent" colspan="3">
						<textarea name="txtReporteCliente" cols="60" rows="3"></textarea>
					</td>
				</tr>
				<tr>
					<td class="CellLabel" align="center">Tipo Servicio</td>
					<td class="CellLabel" align="center" colspan="2">Tipo Falla</td>
					<td class="CellLabel" align="center" colspan="2">Código Resolución</td>
					<td class="CellLabel" align="center">Situación</td>
					<td></td>
				</tr>
				<tr>
					<td class="CellContent">
						<select name="cmbTipoServicio" style="width: 75%">
							<option value=""></option>
						</select>
					</td>
					<td class="CellContent" colspan="2">
						<select name="cmbTipoFalla" style="width: 75%">
							<option value=""></option>
						</select>
					</td>
					<td class="CellContent" colspan="2">
						<select name="cmbCodigoResolucion" style="width: 75%">
							<option value=""></option>
						</select>
					</td>
					<td class="CellContent">
						<select name="cmbSituacion" style="width: 75%">
							<option value=""></option>
						</select>
					</td>
					<td></td>
				</tr>
				<tr>
					<td class="CellLabel" align="center">Estado Rep.</td>
					<td class="CellLabel" align="center">Técnico Respons.</td>
					<td class="CellLabel" align="center">Creado Por</td>
					<td class="CellLabel" align="center">Fecha Creación</td>
					<td class="CellLabel" align="center">Fecha Inicio</td>
					<td class="CellLabel" align="center">Fecha Fin</td>
					<td class="CellLabel" align="center">Fecha Entrega</td>
				</tr>
				<tr>
					<td class="CellContent">
						<select name="cmbEstadoReparacion" style="width: 75%">
							<option value=""></option>
						</select>
					</td>
					<td class="CellContent">
						<select name="cmbTecnicoResponsable" style="width: 75%">
							<option value=""></option>
						</select>
					</td>
					<td class="CellContent">
						<input type="text" name="txtCreadoPor" size="15" maxlength="15" readonly="readonly"/>
					</td>
					<td class="CellContent">
						<input type="text" name="txtFechaCreacion" size="15" maxlength="15" readonly="readonly"/>
					</td>
					<td class="CellContent">
						<input type="text" name="txtFechaInicio" size="15" maxlength="15" readonly="readonly"/>
					</td>
					<td class="CellContent">
						<input type="text" name="txtFechaFin" size="15" maxlength="15" readonly="readonly"/>
					</td>
					<td class="CellContent">
						<input type="text" name="txtFechaEntrega" size="15" maxlength="15" readonly="readonly"/>
					</td>
				</tr>
				<tr>
					<td class="CellLabel" align="center">Diagnóstico</td>
					<td class="CellLabel" align="center" colspan="6">Nextel Diagnóstico</td>
				</tr>
				<tr>
					<td class="CellContent">
						<select name="cmbDiagnostico" style="width: 75%">
							<option value=""></option>
						</select>
					</td>
					<td class="CellContent" colspan="6">
						<textarea name="txtNextelDiagnostico" cols="100" rows="3"></textarea>
					</td>
				</tr>
				<tr>
					<td colspan="7">
						<input type="button" name="btnGenerarOrdenInterna" value="Generar Orden Interna" style="width: 175px"/>
						<input type="button" name="btnGenerarOrdenExterna" value="Generar Orden Externa" style="width: 175px"/>
						<input type="button" name="btnImprimirFormato" value="Imprimir Formato" style="width: 175px" script="javascript:fxImprimirFormato();"/>
					</td>
				</tr>
				<tr>
					<td class="CellLabel" align="center" colspan="3">Lista de Préstamos & Cambios</td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td colspan="5">
						<table border="0" cellspacing="0" cellpadding="0" width="100%">
							<tr>
								<td class="CellLabel" align="center">#</td>
								<td class="CellLabel" align="center">IMEI</td>
								<td class="CellLabel" align="center">Serie</td>
								<td class="CellLabel" align="center">TIpo IMEI</td>
								<td class="CellLabel" align="center">Crear Doc.</td>
								<td class="CellLabel" align="center">Tipo Doc.</td>
								<td class="CellLabel" align="center"># Doc.</td>
								<td class="CellLabel" align="center">Fecha</td>
								<td class="CellLabel" align="center">Creado Por</td>
							</tr>
							<tr>
								<td class="CellContent" align="center">&nbsp;</td>
								<td class="CellContent" align="center">&nbsp;</td>
								<td class="CellContent" align="center">&nbsp;</td>
								<td class="CellContent" align="center">&nbsp;</td>
								<td class="CellContent" align="center">&nbsp;</td>
								<td class="CellContent" align="center">&nbsp;</td>
								<td class="CellContent" align="center">&nbsp;</td>
								<td class="CellContent" align="center"></td>
								<td class="CellContent" align="center"></td>
							</tr>
							<tr>
								<td class="CellContent" align="center">&nbsp;</td>
								<td class="CellContent" align="center">&nbsp;</td>
								<td class="CellContent" align="center">&nbsp;</td>
								<td class="CellContent" align="center">&nbsp;</td>
								<td class="CellContent" align="center">&nbsp;</td>
								<td class="CellContent" align="center">&nbsp;</td>
								<td class="CellContent" align="center">&nbsp;</td>
								<td class="CellContent" align="center"></td>
								<td class="CellContent" align="center"></td>
							</tr>
						</table>
					</td>
					<td colspan="2">
						<input type="button" name="btnAñadir" value="Añadir" style="width: 125px"/>
						<br/>
						<input type="button" name="btnGenerarDocumento" value="Generar Documento" style="width: 125px"/>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td height="15px">
		</td>
	</tr>
	<tr>
		<td align="left">
			<table border="0" cellspacing="0" cellpadding="0" align="left">
				<tr>
					<td class="SectionTitleLeftCurve" width="8">&nbsp;&nbsp;</td>
					<td class="SectionTitle" width="100" align="center">Repuestos</td>
					<td class="SectionTitleRightCurve" width="8">&nbsp;&nbsp;</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td align="left">
			<table border="0" cellspacing="0" cellpadding="0" align="left" class="BannerSecondaryLink">
				<tr>
					<td class="CellLabel" align="center">Disponibles</td>
					<td class="CellLabel" align="center"></td>
					<td class="CellLabel" align="center">Seleccionados</td>
				</tr>
				<tr>
					<td class="CellContent" align="center">
						<select name="cmbRepuestoFuente" multiple="" size="10" style="width: 150px;">
							<option value="73">Antena Coil</option>
							<option value="71">Antenna</option>
							<option value="63">Back Housing</option>
							<option value="72">Bissel</option>
							<option value="64">Flip Cover</option>
							<option value="62">Front Housing</option>
							<option value="66">Keypad</option>
							<option value="65">Keypad Board</option>
							<option value="69">LCD Module</option>
							<option value="70">Lens</option>
							<option value="79">Main Board</option>
							<option value="75">Mic assembly</option>
							<option value="76">Microphone</option>
							<option value="61">Nextel Seedstock</option>
							<option value="67">PTT/Volume Keypad</option>
							<option value="68">Power Keypad</option>
							<option value="78">RF & Controller board</option>
							<option value="74">Sticker</option>
							<option value="80">Top Button</option>
							<option value="77">Unit Replaced</option>
						</select>
					</td>
					<td class="CellContent" align="center">
						<input type="button" name="btnRight" value=">>" onclick="fxAddSelectedClient(document.frmdatos.cmbRepuestoFuente, document.frmdatos.cmbRepuestoDestino)" style="width: 25px;"/>
						<p/>
						<input type="button" name="btnLeft" value="<<" onclick="fxAddSelectedClient(document.frmdatos.cmbRepuestoDestino, document.frmdatos.cmbRepuestoFuente)" style="width: 25px;"/>
					</td>
					<td class="CellContent" align="center">
						<select name="cmbRepuestoDestino" multiple="" size="10" style="width: 150px;">
						</select>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
	
<%} catch (Exception e) {
    System.out.println("Mensaje::::" + e.getMessage() + "<br>");
    System.out.println("Causa::::" + e.getCause() + "<br>");
    for (int i = 0; i < e.getStackTrace().length; i++) {
        System.out.println("    " + e.getStackTrace()[i] + "<br>");
	}
}%>