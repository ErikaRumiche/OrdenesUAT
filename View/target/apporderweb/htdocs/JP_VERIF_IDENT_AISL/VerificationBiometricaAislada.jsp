<%@page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="pe.com.nextel.util.MiUtil" %>
<%@page import="java.util.*" %>
<%@page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.service.BiometricaService" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="pe.com.nextel.bean.PortalSessionBean" %>
<%@ page import="pe.com.nextel.service.SessionService" %>
<%@ page import="pe.com.nextel.exception.SessionException" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@ page import="oracle.portal.provider.v2.ProviderSession" %>
<%@ page import="oracle.portal.provider.v2.ProviderUser" %>
<%@ page import="oracle.portal.provider.v2.url.UrlUtils" %>
<%@ page import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil" %>
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/jQuery-min.js"></script>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    System.out.println("INICIO SESSION - VerificationBiometricaAislada");

    String strSessionId = "";

    PortletRenderRequest pReq = (PortletRenderRequest)
            request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);

    ProviderUser objetoUsuario1 = pReq.getUser();
    strSessionId = objetoUsuario1.getPortalSessionId();

    /*fin - comentar para probar localmente */
    //strSessionId="805809601574955595205364047086969473998102872";

    System.out.println("Sesión capturada después del request : " + strSessionId);
    PortalSessionBean objPortalSesBean = (PortalSessionBean) SessionService.getUserSession(strSessionId);

    if (objPortalSesBean == null) {
        System.out.println("No se encontró la sesión de Java ->" + strSessionId);
        throw new SessionException("La sesión finalizó");
    }

    System.out.println("login del usuario: " + objPortalSesBean.getLogin());
    String strlogin = objPortalSesBean.getLogin();


    String strRutaContext = request.getContextPath();
    String strURLBiometricaEditServlet = strRutaContext + "/biometricaeditservlet";
    String strURLBiometric = strRutaContext + "/biometricaservlet";

    BiometricaService biometricaService = new BiometricaService();
    //Cliente
    HashMap hshData = null;
    String strMessageClie = "";
    ArrayList comboTypeDocClie = new ArrayList();
    //RRLL o apoderado
    //HashMap hshDataRRLL=null;
    String strMessageRRLL = "";
    ArrayList comboTypeDocRRLL = new ArrayList();

    String strMessageAccion = "";
    ArrayList comboAction = new ArrayList();

    Constante constante = new Constante();

    /*PARA VALIDAR EL USUARIO*/

    HashMap hashMap = biometricaService.getValidActivation(0, 0, strlogin, Constante.SOURCE_VIA);

    String strauthorizedUser = (String) hashMap.get("authorizedUser");
    String strauthorizerDni = (String) hashMap.get("authorizedDni");
    String strauthorizerPass = (String) hashMap.get("authorizedPass");
    int idOrder = -3;

    /*portega, Lista validacion tipo doc dinamica*/

    HashMap docTypeHashMap = biometricaService.getViaConfigTypeDocList();
    List<String[]> configDocTypes = (List<String[]>) docTypeHashMap.get("listConfig");

    for (String[] value :configDocTypes) {
        System.out.println("value[0]: "+value[0]+", value[1]: "+value[1]+", value[2]: "+value[2]);
    }

%>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Verificaci&oacute;n de Identidad Aislada</title>
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
    <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/jQuery-min.js"></script>
    <link type="text/css" rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"/>
    <OBJECT classid='CLSID:592B9D7E-51C9-401F-A03C-4DE61FF7008D' name="Autentia" id='Autentia' style='visibility: hidden;'></OBJECT>
    <script type="text/javascript">

        function fxCleanGeneralObjects() {

        }

        function fxGetConfigDocTypes() {
            var listConfig = {
                <%for (int i = 0; i < configDocTypes.size(); i++) {
                String[] value=configDocTypes.get(i);
                %>
                '<%=value[0]%>':
                {'longitud':'<%=value[1]%>','alfaNum':'<%=value[2]%>'}
                <%
                if(i<configDocTypes.size()-1){ %>,<% }
                }%>
            }
            return listConfig;
        }

        function fxCleanForm() {
            var v_form = document.frmdatos;
            var cmbTypeDocument = v_form.cmbTypeDocument;
            var cmbAccion = v_form.cmbAccion;
            var cmbMotivo = v_form.cmbMotivo;
            //form buscar
            $("#txtNumber").val("");
            $("#txtCodBscs").val("");
            $("#txtRuc").val("");
            $("#txtCustomerName").val("");
            //form buscar
            $("#zona_verif_ident").hide();
            $("#cmbTypeDocument").removeAttr('disabled');
            cmbTypeDocument.options[0].selected = true;
            $("#cmbAccion").removeAttr('disabled');
            cmbAccion.options[0].selected = true;
            $("#cmbMotivo").removeAttr('disabled');
            if(cmbMotivo.options[0])cmbMotivo.options[0].selected = true;
            $("#Rut").val("");
            $("#Rut").removeAttr('disabled');
            $("#btnVerificacion").removeAttr('disabled');
            $("#btnRegistrar").removeAttr('disabled');
        }

        function fxValidateNumber() {
            var v_form = document.frmdatos;
            var vhdnNumber = v_form.hdnNumber.value;
            var vNumber = v_form.txtNumber.value;
            var vCriterio = v_form.cmbCriterion.value;
            if (v_form.cmbCriterion.selectedIndex >= 0) {
                if (vNumber != "" && (vhdnNumber != vNumber || vhdnNumber == "")) {
                    if (!ContentOnlyNumber(v_form.txtNumber.value)) {
                        alert("El número ingresado no es válido.");
                        v_form.txtNumber.value = "";
                        v_form.txtNumber.focus();
                        v_form.txtNumber.select();
                        return;
                    }

                    v_form.hdnCustomerName.value = "";
                    v_form.txtCustomerName.value = "";
                    v_form.txtRuc.value = "";
                    v_form.txtCodBscs.value = "";
                    v_form.hdnCustomerId.value = "";
                    fxCleanGeneralObjects();
                    v_form.hdnNumber.value = vNumber;

                    var url = "/portal/pls/portal/WEBSALES.NPSL_CUSTOMER02_PL_PKG.PL_VALIDATE_NUMBER_BIO"
                            + "?av_number=" + vNumber
                            + "&av_criterio=" + v_form.cmbCriterion.options[v_form.cmbCriterion.selectedIndex].value;
                    parent.bottomFrame.location.replace(url);
                }
            }
            else {
                alert("Seleccione el criterio.");
            }
        }

        function fxValidateCustomer(v_origen, v_type) {
            var v_form = document.frmdatos;
            var v_txtCustomerName = v_form.txtCustomerName.value;
            var v_hacer_busqueda = false;
            var v_hdnCustomerName = v_form.hdnCustomerName.value;
            var v_hdnCustomerId = v_form.hdnCustomerId.value;
            var v_txtRuc = v_form.txtRuc.value;
            var v_txtCodBscs = v_form.txtCodBscs.value;
            $("#cmbTypeDocument").val("");

            v_form.cmbTypeDocument.disabled = false; //cada vez que se haca una busqueda se habilita de nuevo el tipo documento
            v_form.Rut.disabled = false;
            v_form.txtNumber.value = "";
            if (v_origen == "CUST_NAME") {
                if (v_txtCustomerName != v_hdnCustomerName && v_txtCustomerName != "") {
                    v_form.txtRuc.value = "";
                    v_form.hdnRuc.value = "";
                    v_form.txtCodBscs.value = "";
                    v_form.hdnCustomerId.value = "";
                    v_form.hdnCustomerId.value = "";
                    v_form.hdnCustomerIdbscs.value = "";
                    fxCleanGeneralObjects();
                    if (v_txtCustomerName != "" || v_hdnCustomerId != "" || v_txtRuc != "" || v_txtCodBscs != "") {
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
                if ((v_form.txtRuc.value != v_form.hdnRuc.value || v_form.hdnRuc.value == "") && v_txtRuc != "") {
                    v_form.hdnCustomerName.value = "";
                    v_form.txtCustomerName.value = "";
                    v_form.txtCodBscs.value = "";
                    v_form.hdnCustomerId.value = "";
                    v_form.hdnCustomerIdbscs.value = "";
                    fxCleanGeneralObjects();
                    v_hacer_busqueda = true;
                    if (v_type == "1") {
                        v_form.hdnSearchType.value = "1";
                    }
                }
                else if (v_txtRuc == "") {
                    fxCleanObjects();
                }
            }
            if (v_origen == "CUST_CODBSCS") {
                if ((v_form.txtCodBscs.value != v_form.hdnCodbscs.value || v_form.txtCodBscs.value == "") && v_txtCodBscs != "") {
                    v_form.hdnCustomerName.value = "";
                    v_form.txtCustomerName.value = "";
                    v_form.txtRuc.value = "";
                    v_form.hdnRuc.value = "";
                    v_form.hdnCustomerId.value = "";
                    v_form.hdnCustomerIdbscs.value = "";

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
                if (v_form.hdnCustomerId.value != "") {
                    v_hacer_busqueda = true;
                }
            }
            if (v_hacer_busqueda == true) {
                v_form.action = "/portal/pls/portal/!WEBSALES.NPSL_CUSTOMER02_PL_PKG.PL_VALIDATE_CUSTOMER_BIO";
                v_form.target = "bottomFrame";
                v_form.submit();
            }
            return;
        }

        function fxCleanObjects() {
            var v_form = document.frmdatos;
            v_form.hdnCustomerName.value = "";
            v_form.hdnCustomerId.value = "";
            v_form.txtRuc.value = "";
            v_form.txtCodBscs.value = "";
            v_form.txtCustomerName.value = "";
            v_form.hdnStatusCollection.value = "";
            v_form.hdnTypecia.value = "";
            fxCleanGeneralObjects();
        }

        function fxSearchCustomer() {
            var formdatos = document.frmdatos;
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
            WinAsist = window.open(url, "WinAsist", "toolbar=no,location=0,directories=no,status=yes,menubar=0,scrollbars=no,resizable=no,screenX=100,top=80,left=100,screenY=80,width=700,height=500,modal=yes");
        }

    </script>
</head>
<body>
<EMBED id="plgAutentia" TYPE="application/x-proxyautentiasocket-plugin" ALIGN=CENTER WIDTH=0 HEIGHT=0>
    <form name="frmdatos" method="post">
        <table cellSpacing=7 cellPadding=0 width="99%" align=center border=0>
            <tbody>
            <tr>
                <td rowSpan="1" colSpan="1">
                    <div id="divCust">
                        <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
                            <tr>
                                <td class="SectionTitleLeftCurve" width="10">&nbsp;&nbsp;</td>
                                <td class="SectionTitle">&nbsp;&nbsp;Ingrese Criterios de B&uacute;squeda - Datos de la
                                    Compañía
                                </td>
                                <td class="SectionTitleRightCurve" width="8">&nbsp;&nbsp;</td>
                            </tr>
                        </table>
                        <input type="hidden" name="hdnNumber" id="hdnNumber" value="">
                        <input type="hidden" name="hdnCustomerId" id="hdnCustomerId" value="">
                        <input type="hidden" name="hdnCustomerName" id="hdnCustomerName" value="">
                        <input type="hidden" name="hdnRuc" id="hdnRuc" value="">
                        <input type="hidden" name="hdnTipodoc" id="hdnTipodoc" value="">
                        <input type="hidden" name="hdnCodbscs" id="hdnCodbscs" value="">
                        <input type="hidden" name="hdnCustomerIdbscs" id="hdnCustomerIdbscs" value="">
                        <input type="hidden" name="hdnSearchType" id="hdnSearchType" value="0">
                        <input type="hidden" name="hdnStatusCollection" id="hdnStatusCollection" value="">
                        <input type="hidden" name="hdnTypecia" id="hdnTypecia" value="">
                        <input type="hidden" id="hdUserId" name="hdUserId" value="45536161"/>

                        <input type="hidden" id="NroAudit" name="NroAudit"/>
                        <input type="hidden" id="ErcDesc" name="ErcDesc"/>
                        <input type="hidden" id="Nombres" name="Nombres"/>
                        <input type="hidden" id="ApPaterno" name="ApPaterno"/>
                        <input type="hidden" id="ApMaterno" name="ApMaterno"/>
                        <input type="hidden" id="Vigencia" name="Vigencia"/>
                        <input type="hidden" id="Restriccion" name="Restriccion"/>
                        <input type="hidden" id="codError" name="codError"/>
                        <input type="hidden" id="hdnSessionLogin" name="hdnSessionLogin" value="<%=strlogin%>"/>
                        <input type="hidden" id="hdnAuthorizedUser" name="hdnAuthorizedUser"
                               value="<%=strauthorizedUser%>"/>
                        <input type="hidden" id="hdnAuthorizedDni" name="hdnAuthorizedDni"
                               value="<%=strauthorizerDni%>"/>
                        <input type="hidden" id="hdnAuthorizedPass" name="hdnAuthorizedPass"
                               value="<%=strauthorizerPass%>"/>
                        <input type="hidden" id="origen" name="origen" value="new"/>
                        <input type='hidden' id="hdSource" name="hdSource" value="<%=Constante.Source_CRMVIA%>"/>
                        <input type="hidden" id="hdVentana" name="hdVentana" value='0'/>
                        <input type="hidden" id="hdErcAcepta" name="hdErcAcepta"/>
                        <input type="hidden" id="hdErrorAcepta" name="hdErrorAcepta"/>
                        <input type="hidden" id="idOrder" name="idOrder" value="<%=idOrder%>"/>

                        <table border="0" cellpadding="2" cellspacing="0" class="RegionBorder" width="100%"
                               id="table_customer">
                            <tr>
                                <td class="CellLabel" align="left" valign="middle">&nbsp;&nbsp;Criterio</td>
                                <td class="CellLabel" align="left" valign="middle">&nbsp;&nbsp;Número</td>
                                <td class="CellLabel" align="left">&nbsp;Cod. BSCS</TD>
                                <td class="CellLabel">&nbsp;<font color="#FF0000">*&nbsp;</font>RUC/DNI/Otro</td>
                                <td class="CellLabel" align="left">&nbsp;<font color="#FF0000">*&nbsp;</font><a
                                        href="javascript:fxSearchCustomer()">Raz&oacute;n&nbsp;Social</a></TD>
                            </tr>
                            <tr>
                                <td align="center" class="cellcontent">
                                    <select name="cmbCriterion">
                                        <option value="10">N° Teléfono</option>
                                    </select>
                                </td>
                                <td class="CellContent">
                                    <input type="text" name="txtNumber" id="txtNumber" value="" size="30" maxlength="50"
                                           onKeyDown="if (window.event.keyCode== 9) {this.value=trim(this.value.toUpperCase());fxValidateNumber();}"
                                           onKeyPress="if (window.event.keyCode== 13) {this.value=trim(this.value.toUpperCase());fxValidateNumber();}">
                                </td>
                                <td class="CellContent">
                                    <input type="text" name="txtCodBscs" id="txtCodBscs" value="" size="15"
                                           maxlength="50"
                                           onKeyDown="if (window.event.keyCode== 9) {this.value=trim(this.value.toUpperCase());fxValidateCustomer('CUST_CODBSCS','0');}"
                                           onKeyPress="if (window.event.keyCode== 13) {this.value=trim(this.value.toUpperCase());fxValidateCustomer('CUST_CODBSCS','1');}">
                                </td>
                                <td class="CellContent">
                                    <input type="text" name="txtRuc" id="txtRuc" value="" size="15" maxlength="11"
                                           onKeyDown="if (window.event.keyCode== 9) {this.value=trim(this.value.toUpperCase());fxValidateCustomer('CUST_RUC','0');}"
                                           onKeyPress="if (window.event.keyCode== 13) {this.value=trim(this.value.toUpperCase());fxValidateCustomer('CUST_RUC','1');}">
                                </td>
                                <td class="CellContent">
                                    <input type="text" id="txtCustomerName" name="txtCustomerName" size="35"
                                           maxlength="75"
                                           onKeyDown="if (window.event.keyCode== 9) {this.value=trim(this.value.toUpperCase());fxValidateCustomer('CUST_NAME','0');}"
                                           onKeyPress="if (window.event.keyCode== 13) {this.value=trim(this.value.toUpperCase());fxValidateCustomer('CUST_NAME','1');}">
                                </td>
                            </tr>
                        </table>
                    </div>
                    <br>
                    <!-- inicio de tabla para verifiacion de identidad aislada -->
                    <div id="zona_verif_ident">
                        <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
                            <tbody>
                            <tr>
                                <td class="SectionTitleLeftCurve" width="10" rowspan="1" colspan="1">&nbsp;&nbsp;</td>
                                <td class="SectionTitle" rowspan="1" colspan="1">&nbsp;&nbsp;Persona que realiza la
                                    verificaci&oacute;n de identidad aislada
                                </td>
                                <td class="SectionTitleRightCurve" width="8" rowspan="1" colspan="1">&nbsp;&nbsp;</td>
                            </tr>
                            </tbody>
                        </table>
                        <table align="center" width="100%" class="RegionBorder" border="0" cellspacing="1"
                               cellpadding="1">
                            <tr id="zona_tip_doc">
                                <td class="CellContent">
                                    <table align="center" width="100%" border="0">
                                        <tr>
                                            <td class="CellLabel" width="15%" align="left" valign="top">Tipo de
                                                Documento:
                                            </td>
                                            <td class="CellContent" width="25%" align="left" valign="top">
                                                <select name="cmbTypeDocument" id="cmbTypeDocument"
                                                        onchange="changeCmbTypeDocument(this.value)"></select>
                                            </td>
                                            <td class="CellLabel" width="15%" align="left" valign="top">Nro de
                                                documento:
                                            </td>
                                            <td class="CellContent" width="25%" align="left" valign="top">
                                                <input type="text" name="Rut" id="Rut" value="" size="30"
                                                       maxlength="50">
                                                <input type="hidden" id="hdflagDni" name="hdflagDni" value="0"/>
                                            </td>
                                            <td class="CellContent" width="20%" align="left" valign="top">
                                                <input type="button" id="btnRegistrar" value="Registrar" align="left"
                                                       onclick="clickRegistrar(this)"/>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr id="zona_acciones">
                                <td class="CellContent">
                                    <table align="center" width="100%" border="0">
                                        <tr>
                                            <td class="CellLabel" width="15%" align="left" valign="top">Acci&oacute;n:
                                            </td>
                                            <td class="CellContent" width="25%" align="left" valign="top">
                                                <select name="cmbAccion" id="cmbAccion"
                                                        onchange="changeCmbAction(this.value)"></select>
                                            </td>
                                            <td class="CellLabel" width="15%" align="left" valign="top"><span
                                                    id="textMotivo">Motivo</span></td>
                                            <td class="CellContent" width="25%" align="left" valign="top">
                                                <select name="cmbMotivo" id="cmbMotivo">
                                                    <option value="0"></option>
                                                </select>
                                            </td>
                                            <td class="CellContent" width="20%" align="left" valign="top">
                                                <input type="button" id="btnVerificacion" name="btnVerificacion"
                                                       value="Verificar Identidad" onclick="verifPendiente()"
                                                       align="left"/>
                                            </td>
                                        </tr>

                                    </table>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <!-- fin de tabla para verifiacion de identidad aislada -->
                </td>
            </tr>
            </tbody>
        </table>
        <!-- boton oculto-->
        <input type="button" id="btnRefreshForm" name="btnRefreshForm" style="display:none;" onclick="fxCleanForm();" align="left" />
    </form>
    <br>
    <form method="post" name="formdatos">
        <input type="hidden" name="cmbDocumentType" value="">
        <input type="hidden" name="txtDocumentNumber" value="">
        <input type="hidden" name="txtCommercialName" value="">
        <input type="hidden" name="txtRazonSocial" value="SAGA">
        <input type="hidden" name="txtCustCode" value="">
        <input type="hidden" name="cmbRegion" value="">
        <input type="hidden" name="cmbTypePerson" value="">

        <input type="hidden" name="NPGIROID" value="0">
        <input type="hidden" name="txt_giro" value="">
        <input type="hidden" name="cmbSubgiro" value="0">
        <input type="hidden" name="txt_subgiro" value="">
        <input type="hidden" name="cmbSegmento" value="">
        <input type="hidden" name="txt_segmento" value="">
        <input type="hidden" name="cmbValorcliente" value="">
        <input type="hidden" name="txt_valorcliente" value="">

        <input type="hidden" name="v_page_number" value="1">
        <input type="hidden" name="v_visits" value="1">
        <input type="hidden" name="cmbRangoCuenta" value="">
        <input type="hidden" name="txt_rangocuenta" value="">
    </form>
</body>
<script type="text/javascript">
    var listaConfigDocType;
    function fxLoadIni() {
        var v_form = document.frmdatos;
        document.getElementById("divCust").style.display = "block";
        document.getElementById("zona_verif_ident").style.display = "none";
        document.getElementById("zona_tip_doc").style.display = "none";
        document.getElementById("zona_acciones").style.display = "none";
        document.getElementById("btnVerificacion").style.display = "block";
        document.getElementById("btnRegistrar").style.display = "none";
        v_form.cmbMotivo.disabled = true;
        $("#cmbMotivo").show();
        $("#textMotivo").show();
        listaConfigDocType = fxGetConfigDocTypes();
    }

    fxLoadIni()

    function changeCmbTypeDocument(tipodoc) {
        $("#cmbAccion").empty();
        var v_form = document.frmdatos;
        v_form.hdnTipodoc.value = tipodoc;
        v_form.cmbAccion.disabled = false;
        var server = '<%=strURLBiometricaEditServlet%>';
        var params = 'myaction=listAccion&av_type_document=' + tipodoc;
        //alert("valor params:"+params);
        $.ajax({
            url: server,
            data: params,
            type: "POST",
            success: function (data) {
                $("#cmbAccion").append(data);
            }
        });

        if (tipodoc == "DNI") {
            var v_form = document.frmdatos;
            document.getElementById("zona_acciones").style.display = "block";
            document.getElementById("btnVerificacion").style.display = "block";
            document.getElementById("btnRegistrar").style.display = "none";
            v_form.cmbMotivo.disabled = true;
        } else {
            document.getElementById("zona_acciones").style.display = "none";
            document.getElementById("btnVerificacion").style.display = "none";
            document.getElementById("btnRegistrar").style.display = "block";
        }
    }

    function changeCmbAction(action) {
        $("#cmbMotivo").empty();
        var v_form = document.frmdatos;
        v_form.cmbMotivo.disabled = false;
        var server = '<%=strURLBiometricaEditServlet%>';
        var params = 'myaction=listMotivoAislada&action=' + action;
        //alert("valor params:"+params);
        $.ajax({
            url: server,
            data: params,
            type: "POST",
            success: function (data) {
                if (data == "<option value='0'></option>") {
                    $("#cmbMotivo").hide();
                    $("#textMotivo").hide();
                } else {
                    $("#cmbMotivo").show();
                    $("#textMotivo").show();
                    $("#cmbMotivo").append(data);
                }
            }
        });
    }

    function clickRegistrar(button) {
        //button.disabled = true;
        var tipoDoc = $("#cmbTypeDocument").val();
        var numDoc = $("#Rut").val();
        var accion = $("#cmbAccion").val();
        var customerId = $("#hdnCustomerId").val();
        //INICIO AMENDEZ - N_SD000915843
        var login = $("#hdnSessionLogin").val();
        //FIN AMENDEZ - N_SD000915843
        var server = '<%=strURLBiometricaEditServlet%>';
        var params = 'myaction=insertRegistrar&tipoDoc=' + tipoDoc + '&numDoc=' + numDoc + '&accion=' + accion + '&customerId=' + customerId + '&login=' + login;

        if (validarTipoDocNroDoc()) {
            //alert("params: "+params);
            $.ajax({
                url: server,
                data: params,
                type: "POST",
                success: function (data) {
                    //alert("data: "+data);
                    if (data == "null") {
                        alert("Se registró correctamente");
                        fxCleanForm();
                    } else {
                        alert("Error: " + data);
                    }
                }
            });
        }
        //else{button.disabled = false;}
    }

    function validarTipoDocNroDoc() {
        var tipoDoc = $("#cmbTypeDocument").val();
        var resultado = false;

        if (tipoDoc.length < 1) {
            alert("Seleccione el tipo de documento");
            return false;
        }
        return validarDocumento();
    }

    function validarDocumento() {
        var tipoDoc = $("#cmbTypeDocument").val();
        var cantMax = listaConfigDocType[tipoDoc].longitud;

        //alert('cantMax: ' + cantMax);

        var vform = document.frmdatos;
        var documento = vform.Rut.value;

        if(!validarCaracteres(tipoDoc, documento)) return false;

        if (cantMax == null || cantMax == '')
            return true;

        if (documento.length < 1) {
            alert("Debe ingresar el número de Documento");
            return false;
        }
        else if (documento.length != cantMax) {
            alert("El " + tipoDoc + " debe tener " + cantMax + " caracteres");
            return false;
        }
        return true;
    }

    function validarCaracteres(tipoDoc, documento) {
        var alfaNum = listaConfigDocType[tipoDoc].alfaNum;

        //alert('alfaNum: ' + alfaNum);

        if (alfaNum == null || alfaNum == '' || alfaNum == '0')
            return true;

        if (alfaNum== '1') {
            if(!validarSiNumero(documento)){
                alert("Tipo de documento solo permite números.");
                return false;
            }
        }
        return true;
    }

    function validarSiNumero(texto){
        if (!/^([0-9])*$/.test(texto))
            return false;
        else
            return true;
    }

</script>
<script>
    function verifPendiente() {
        if (validarTipoDocNroDoc()) {
            var vform = document.frmdatos;
            var selectAccion = vform.cmbAccion.value;
            //var order=vform.idOrder.value;
            var documento = vform.Rut.value;
            var accionNoBiometrica = '<%=constante.action_Domain_NB%>';

            if (selectAccion == "B") {
                Verificar();
            } else {
                verificacionNOBiometrica();
            }
        }
    }

    function Verificar() {
        var vform = document.frmdatos;
        Iniciar();
        var codError = vform.codError.value;
        if (codError != '<%=constante.aceptaCancel%>') {
            verifBiometricaEdit();
        }
    }

    function Iniciar() {

        cmdTerminar();
        var Rut = document.frmdatos.Rut.value;
        WsUser = '<%=strauthorizerDni%>';
        NroIntentoIni = "1";
        AppOrigen = "CRM";
        VentanaD = "0";
        ErcDesc = "";
        ErcCod = "";
        Nombres = "";
        ApPaterno = "";
        ApMaterno = "";
        Vigencia = "";
        NroAudit = "";
        NroIntento = "";
        Restriccion = "";

        if (document.frmdatos.Rut.value == "") {
            document.frmdatos.Rut.focus();
            return false;
        }

        if (TieneAutentia()) {
            var Params = new TParams;
            Params.Rut = Rut;

            Params.NroIntentoIni = "1";
            Params.AppOrigen = "CRM";
            Params.VentanaD = "0";
            Params.ErcDesc = "";
            Params.ErcCod = "";
            Params.Nombres = "";
            Params.ApPaterno = "";
            Params.ApMaterno = "";
            Params.Vigencia = "";
            Params.NroAudit = "";
            Params.NroIntento = "";
            Params.Restriccion = "";
            erc = 200;
            erc = Autentia.Transaccion("../ENTELPERU/verifica_integra", Params);

            document.frmdatos.ErcDesc.value = Params.ErcDesc;
            document.frmdatos.Nombres.value = Params.Nombres;
            document.frmdatos.ApPaterno.value = Params.ApPaterno;
            document.frmdatos.ApMaterno.value = Params.ApMaterno;
            document.frmdatos.Vigencia.value = Params.Vigencia;
            document.frmdatos.NroAudit.value = Params.NroAudit;
            document.frmdatos.Restriccion.value = Params.Restriccion;
            document.frmdatos.codError.value = Params.ErcCod;
            document.frmdatos.hdErcAcepta.value = erc;


        } else if (xstNPAPIautentia()) {
            var plgAutentia = document.getElementById('plgAutentia');
            IngresoSocketInterface(plgAutentia, Rut, WsUser, NroIntentoIni, AppOrigen, VentanaD, ErcDesc, ErcCod, Nombres, ApPaterno, ApMaterno, Vigencia, NroAudit, Restriccion);
        } else if (IsWin64()) {
            var Autentia64 = new ActiveXObject("AxAutentia64.clsAutentia");
            IngresoSocketInterface(Autentia64, Rut, WsUser, NroIntentoIni, AppOrigen, VentanaD, ErcDesc, ErcCod, Nombres, ApPaterno, ApMaterno, Vigencia, NroAudit, Restriccion);
        }
    }

    function TParams() {
        this.Rut = '';
        this.DV = '';
        this.Lugar = '';
    }

    function cmdTerminar() {
        document.frmdatos.NroAudit.value = "";
        document.frmdatos.ErcDesc.value = "";
        document.frmdatos.Nombres.value = "";
        document.frmdatos.ApPaterno.value = "";
        document.frmdatos.ApMaterno.value = "";
        document.frmdatos.Vigencia.value = "";
        document.frmdatos.Restriccion.value = "";
        document.frmdatos.codError.value = "";
    }

    function TieneAutentia() {
        try {
            var vAutentia = Autentia.Version;
            if (vAutentia == null) {
                return false;
            } else {
                return true;
            }
        } catch (err) {
            return false;
        }
    }

    function xstNPAPIautentia() {
        var mimetype = navigator.mimeTypes["application/x-proxyautentiasocket-plugin"];
        if (mimetype) {
            var plugin = mimetype.enabledPlugin;
            if (plugin) {
                return true; //Plugin NPAPI habilitado en Browser.
            } else {
                return false; //Plugin NPAPI no esta habilitado en Browser.
            }
        } else {
            return false; //Mime Type de carga del plugin no esta agregado en los validos para el browser.
        }
    }

    function IsWin64() {
        return (window.navigator.platform == 'Win64');
    }

    function verifBiometricaEdit() {
        var vForm = document.frmdatos;
        var descripcion = vForm.ErcDesc.value;
        var restriccion = vForm.Restriccion.value;
        vForm.ErcDesc.value = unicode(descripcion);
        vForm.Restriccion.value = unicode(restriccion);

        vForm.target = "bottomFrame";
        vForm.action = "<%=strRutaContext%>/biometricaservlet?myaction=verificacionBiometricaAislada" + "&idOrder=" + '<%=idOrder%>';
        vForm.submit();
    }

    function verificacionNOBiometrica() {
        var vForm = document.frmdatos;
        var accion = vForm.cmbAccion.value;
        var motivo = vForm.cmbMotivo.value;

        var cmbNoBiometric = '<%=constante.action_NB%>';
        var idsesion = vForm.hdUserId.value;

        if (validarDocVerificIdentidad()) {
            var DescripMotivo = $("#cmbMotivo option:selected").html();
            vForm.target = "bottomFrame";
            vForm.action = "<%=strRutaContext%>/biometricaservlet?myaction=verificacionNobiometrica&motivo=" + DescripMotivo + "&idsesion=" + idsesion;
            vForm.submit();
        }
    }

    function validarDocVerificIdentidad() {

        var tipoDoc = $("#cmbTypeDocument").val();
        var numDoc = $("#Rut").val();
        var accion = $("#cmbAccion").val();
        var motivo = $("#cmbMotivo").val();
        if(motivo == null) motivo = "Error en componente de Acepta";
        var cmbNoBiometric = '<%=constante.action_NB%>';

        var resultado = false;

        if (tipoDoc.length > 0) {
            if (accion == cmbNoBiometric && accion.length > 0) {
                if (validarTipoDocNroDoc()) {
                    if (motivo != '0' && motivo.length > 0) {
                        resultado = true;
                    } else {
                        alert("Debe seleccionar un motivo para realizar la verificación no biométrica");
                        resultado = false;
                    }
                }
            } else {
                alert("Debe seleccionar la accion a realizar");
                resultado = false;
            }
        } else {
            alert("Seleccione el tipo de documento");
            resultado = false;
        }
        return resultado;
    }


    function unicode(str) {
        str = str.replaceAll('á', 'a');
        str = str.replaceAll('é', 'e');
        str = str.replaceAll('í', 'i');
        str = str.replaceAll('ó', 'o');
        str = str.replaceAll('ú', 'u');

        str = str.replaceAll('Á', 'A');
        str = str.replaceAll('É', 'E');
        str = str.replaceAll('Í', 'I');
        str = str.replaceAll('Ó', 'O');
        str = str.replaceAll('Ú', 'U');

        str = str.replaceAll('ñ', 'n');
        str = str.replaceAll('Ñ', 'N');

        str = str.replaceAll('¿', '\u00bf');

        return str;
    }

    String.prototype.replaceAll = function (sfind, sreplace) {
        return this.split(sfind).join(sreplace);
    };

</script>
</html>