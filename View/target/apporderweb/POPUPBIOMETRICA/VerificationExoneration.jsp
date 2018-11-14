<%@page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="pe.com.nextel.util.MiUtil"%>
<%@page import="java.util.*"%>
<%@page import="pe.com.nextel.util.Constante" %>
<%@page import="org.apache.commons.lang.StringUtils" %>
<%@page import="pe.com.nextel.service.BiometricaService" %>
<%@ page import="pe.com.nextel.bean.LegalRepresentativeBean" %>
<%@ page import="pe.com.nextel.service.DigitalDocumentService" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    System.out.println("INICIO SESSION - VerificationExoneration");

    String strRutaContext=request.getContextPath();
    String strURLBiometricaServlet =strRutaContext+"/biometricaservlet";
    String strURLBiometricaEditServlet =strRutaContext+"/biometricaeditservlet";
    String strdocument="";
    //JVERGARA [PRY-0787] inicio
    String strTypeDocument = "";
    String strTypeDocumentValue = "";
    //JVERGARA [PRY-0787] fin

    String datos=request.getParameter("datos");
    String[] ArrayDatos =datos.split("_");
    int idOrder=Integer.parseInt(ArrayDatos[0]);

    String strlogin= ArrayDatos[2];
    String strauthorizedUser= ArrayDatos[1];
    String strauthorizerDni=MiUtil.getString(ArrayDatos[3]);
    String strauthorizerPass=MiUtil.getString(ArrayDatos[4]);
    String strUserID=MiUtil.getString(ArrayDatos[5]);


    if(ArrayDatos.length>=7){strdocument=MiUtil.getString(ArrayDatos[6]);}

    //JVERGARA [PRY-0787] inicio
    if(ArrayDatos.length>=8){
        strTypeDocument=MiUtil.getString(ArrayDatos[7]);
    }
    if(ArrayDatos.length>=9){
        strTypeDocumentValue=MiUtil.getString(ArrayDatos[8]);
    }
    //JVERGARA [PRY-0787] fin

    System.out.println("New--[strauthorizedUser:]"+ArrayDatos[1]+"[strlogin:] "+ArrayDatos[2]+"[order:]"+ArrayDatos[0]);
    System.out.println(" -authorizerDni: "+strauthorizerDni+" -Tamano:"+ArrayDatos.length+" -pass: "+strauthorizerPass+" -UserID: "+strUserID);

    System.out.println(" -documento: "+strdocument+" -Tipo_documento:"+strTypeDocument+" -valorDoc: "+strTypeDocumentValue);

    int orderId = 0;
    String solution = null;
    int nbPend = 0;

    try {
        BiometricaService biometricaService= new BiometricaService();
        Constante constante=new Constante();
        HashMap hshData=null;
        String strMessage="";
        ArrayList comboAction = new ArrayList();
        ArrayList comboTypeDoc = new ArrayList();
        HashMap legalRepresentativeBeanMap = null;

        orderId = Integer.parseInt(ArrayDatos[0]);


%>

<html>
<head>

    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Verificaci&oacute;n  Biom&eacute;trica / No Biom&eacute;trica / Exoneraci&oacute;n</title>
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
    <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/jQuery-min.js"></script>
    <link type="text/css" rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"/>

    <OBJECT classid='CLSID:592B9D7E-51C9-401F-A03C-4DE61FF7008D' name="Autentia" id='Autentia'></OBJECT>

    <script type="text/javascript">

        function cmdTerminar()
        {
            document.frmExonerate.NroAudit.value   =  "";
            document.frmExonerate.ErcDesc.value   =  "";
            document.frmExonerate.Nombres.value   =  "";
            document.frmExonerate.ApPaterno.value =  "";
            document.frmExonerate.ApMaterno.value =  "";
            document.frmExonerate.Vigencia.value  =  "";
            document.frmExonerate.Restriccion.value  =  "";
            document.frmExonerate.codError.value  =  "";
        }

        function TParams()
        {
            this.Rut = '';
            this.DV  = '';
            this.Lugar = '';
        }

        function Verificar(){
            var vform=document.frmExonerate;
            Iniciar();
            var codError=vform.codError.value;
            if(codError!='<%=constante.aceptaCancel%>' ) {
                verifBiometricaNew();
            } else {
                document.getElementById('btnVerificacion').disabled=false;
            }
        }

        function Iniciar()
        {

            cmdTerminar();
            var Rut		= document.frmExonerate.Rut.value;
            WsUser		= '<%=strauthorizerDni%>';
            NroIntentoIni = "1";
            AppOrigen = "CRM";
            VentanaD   = "0";
            ErcDesc		= "";
            ErcCod		= "";
            Nombres		= "";
            ApPaterno	= "";
            ApMaterno	= "";
            Vigencia	= "";
            NroAudit	= "";
            NroIntento	= "";
            Restriccion   = "";
            if (document.frmExonerate.Rut.value == "")
            {
                document.frmExonerate.Rut.focus ();
                return false;
            }

            if (TieneAutentia())
            {
                var Params   = new TParams;
                Params.Rut  		= Rut ;
                Params.WsUser		= '<%=strauthorizerDni%>';
                Params.NroIntentoIni = "1";
                Params.AppOrigen     = "CRM";
                Params.VentanaD      = "0";
                Params.ErcDesc		= "";
                Params.ErcCod		= "";
                Params.Nombres		= "";
                Params.ApPaterno	= "";
                Params.ApMaterno	= "";
                Params.Vigencia		= "";
                Params.NroAudit		= "";
                Params.NroIntento	= "";
                Params.Restriccion   = "";
                erc					= 200;
                erc = Autentia.Transaccion ("../ENTELPERU/verifica_integra",Params);

                document.frmExonerate.ErcDesc.value     =  Params.ErcDesc;
                document.frmExonerate.Nombres.value     =  Params.Nombres;
                document.frmExonerate.ApPaterno.value   =  Params.ApPaterno;
                document.frmExonerate.ApMaterno.value   =  Params.ApMaterno;
                document.frmExonerate.Vigencia.value    =  Params.Vigencia;
                document.frmExonerate.NroAudit.value    =  Params.NroAudit;
                document.frmExonerate.Restriccion.value =  Params.Restriccion;
                document.frmExonerate.codError.value    =  Params.ErcCod;
                document.frmExonerate.hdErcAcepta.value =  erc;


            }else if(xstNPAPIautentia())
            {
                var plgAutentia = document.getElementById('plgAutentia');
                IngresoSocketInterface(plgAutentia,Rut,WsUser,NroIntentoIni,AppOrigen,VentanaD,ErcDesc,ErcCod,Nombres,ApPaterno,ApMaterno,Vigencia,NroAudit,Restriccion);
            }else if(IsWin64())
            {
                var Autentia64 = new ActiveXObject("AxAutentia64.clsAutentia");
                IngresoSocketInterface(Autentia64,Rut,WsUser,NroIntentoIni,AppOrigen,VentanaD,ErcDesc,ErcCod,Nombres,ApPaterno,ApMaterno,Vigencia,NroAudit,Restriccion);
            }
        }

        function TieneAutentia()
        {
            try
            {
                var vAutentia = Autentia.Version;
                if(vAutentia == null){
                    return false;
                }

                else{
                    return true;
                }

            } catch(err)
            {
                return false;
            }
        }

        function IsWin64()
        {
            return (window.navigator.platform == 'Win64');
        }

        function xstNPAPIautentia()
        {
            var mimetype = navigator.mimeTypes["application/x-proxyautentiasocket-plugin"];

            if (mimetype)
            {
                var plugin = mimetype.enabledPlugin;
                if (plugin)
                {
                    return true; //Plugin NPAPI habilitado en Browser.
                } else
                {
                    return false; //Plugin NPAPI no esta habilitado en Browser.
                }
            } else
            {
                return false; //Mime Type de carga del plugin no esta agregado en los validos para el browser.
            }
        }

        function IngresoSocketInterface(objAutentia,Rut,WsUser,ErcDesc,ErcCod,Nombres,ApPaterno,ApMaterno,Vigencia,NroAudit,Restriccion){
            try
            {
                objAutentia.IniciarSesion("", function(response)
                {
                    //'response' devuelve la respuesta del socket si no presento ning?n problema al consultar la libreria Autentia.
                    //Por lo general el error producido es debido a que FPSensor no esta activo, con el c?digo de Error '01000009010000013'
                    if (response.localeCompare("01000009010000010")==0)
                    { //response con proceso sin problemas...
                        //objAutentia.ParamsInit("dato, tipoDato, empresa, paterno, materno, nombre, sexo, fecha, Erc,ErcDesc,NroAudit");
                        //objAutentia.ParamsSet(1, dato, 2, tipoDato, 3, empresa, 4, paterno, 5, materno, 6, nombre, 7, sexo, 8, fecha,Erc,ErcDesc,NroAudit);
                        objAutentia.ParamsInit("Rut,WsUser,NroIntentoIni,AppOrigen,VentanaD,ErcDesc,ErcCod,Nombres,ApPaterno,ApMaterno,Vigencia,NroAudit,Restriccion");
                        objAutentia.ParamsSet(1, Rut);
                        objAutentia.ParamsSet(2, WsUser);
                        objAutentia.ParamsSet(3, NroIntentoIni);
                        objAutentia.ParamsSet(4, AppOrigen);
                        objAutentia.ParamsSet(5, VentanaD);
                        objAutentia.ParamsSet(6, ErcDesc);
                        objAutentia.ParamsSet(7, ErcCod);
                        objAutentia.ParamsSet(8, Nombres);
                        objAutentia.ParamsSet(9, ApPaterno);
                        objAutentia.ParamsSet(10, ApMaterno);
                        objAutentia.ParamsSet(11, Vigencia);
                        objAutentia.ParamsSet(12, NroAudit);
                        objAutentia.ParamsSet(13, Restriccion);
                        objAutentia.Transaccion2('../ENTELPERU/verifica_integra', function(response)
                        {

                            document.frmExonerate.ErcDesc.value     =  objAutentia.ParamsGet(6);
                            document.frmExonerate.Nombres.value     =  objAutentia.ParamsGet(8);
                            document.frmExonerate.ApPaterno.value   =  objAutentia.ParamsGet(9);
                            document.frmExonerate.ApMaterno.value   =  objAutentia.ParamsGet(10);
                            document.frmExonerate.Vigencia.value    =  objAutentia.ParamsGet(11);
                            document.frmExonerate.NroAudit.value    =  objAutentia.ParamsGet(12);
                            document.frmExonerate.Restriccion.value =  objAutentia.ParamsGet(13);
                            document.frmExonerate.codError.value    =  objAutentia.ParamsGet(7);

                        });
                    }else
                    { // variable 'response' con respuesta distinta a proceso correcto (01000009010000010)
                        if(response.substr(0, 2) == 'E:')
                        {
                            document.frmExonerate.hdErrorAcepta.value    =  response;
                            return false;
                        }
                    }
                });
            } catch (err)
            {
                document.frmExonerate.hdErrorAcepta.value    =  err.message;
                return false;
            }
        }




        function closePopUpAnular(){
            parent.opener.parent.mainFrame.redirectOrder('<%=idOrder%>');
            parent.close();
        }

        function verifBiometricaNew(){
            var vForm = document.frmExonerate;
            var descripcion=vForm.ErcDesc.value;
            var restriccion=vForm.Restriccion.value;
            vForm.ErcDesc.value=unicode(descripcion);
            vForm.Restriccion.value=unicode(restriccion);

            vForm.target = "bottomFrame";
            vForm.action="<%=strRutaContext%>/biometricaservlet?myaction=verificacionBiometricaNew"+"&idOrder="+'<%=idOrder%>';

            vForm.submit();
        }

        function verificacionNOBiometrica(){
            var vForm = document.frmExonerate;
            var accion=vForm.cmbAccion.value;
            var motivo=vForm.cmbMotivo.value;
            var cmbNoBiometric='<%=constante.action_Domain_NB%>';
            var idsesion=vForm.hdUserId.value;

            if(accion==cmbNoBiometric && motivo>0 ) {
                var DescripMotivo = $("#cmbMotivo option:selected").html();
                vForm.target = "bottomFrame";
                vForm.action = "<%=strRutaContext%>/biometricaservlet?myaction=verificacionNobiometrica&motivo="+DescripMotivo+"&idsesion="+idsesion;

                vForm.submit();
            }else{
                alert("Debe seleccionar un motivo para realizar la verificación no biométrica");
            }
        }

        function unicode(str){
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
</head>
<body>
<EMBED id="plgAutentia" TYPE="application/x-proxyautentiasocket-plugin" ALIGN=CENTER WIDTH=0 HEIGHT=0>
    <form name="frmExonerate" method="post" >

        <!-- tabla para lo de exoneracion -->
        <table  align="center" width="100%" border="0">
            <tr>
                <td>
                    <table border="0" class="PortletHeaderColor" cellspacing="0" cellpadding="0" width="100%">
                        <tr class="PortletHeaderColor">
                            <td class="LeftCurve" valign="top" align="left" width="10">&nbsp;&nbsp;</td>
                            <td class="PortletHeaderColor" align="left" valign="top">
                                <font class="PortletHeaderText">
                                    Verificaci&oacute;n  Biom&eacute;trica / No Biom&eacute;trica / Exoneraci&oacute;n
                                </font></td>
                            <td align="right" class="RightCurve" width="10%" >&nbsp;&nbsp;</td>
                        </tr>
                    </table>
                </td>
            </tr>

            <tr>
                <td>
                    <table align="center" width="100%" border="0">
                        <tr>
                            <td><input type="hidden" name="myaction" >
                                <input type="hidden" name="idOrder" value="<%=idOrder%>">
                            </td>
                        </tr>
                        <tr>
                            <td class="CellLabel" align="left" valign="top">Tipo Documento:</td>
                            <% if(StringUtils.isBlank(strTypeDocument)){ %>
                            <td>
                                <select name="cmbTypeDocument" id="cmbTypeDocument" onchange="javascript:changeTypeDoc(this);">
                                    <option value="0"></option>
                                    <%hshData= biometricaService.getTypesDocumentsExoneration();
                                        comboTypeDoc=(ArrayList)hshData.get("objArrayList");
                                        strMessage=(String)hshData.get("strMessage");
                                        if(StringUtils.isBlank(strMessage)){
                                            for(int i=0;i<comboTypeDoc.size();i++){
                                                HashMap objeto=(HashMap)comboTypeDoc.get(i);
                                                String value=(String)objeto.get("wv_valueTypeDoc");
                                                String descripcion=(String)objeto.get("wv_descTypeDoc");
                                    %>
                                    <option value='<%=value%>'><%=descripcion%></option>
                                    <%
                                        } }else{%>
                                    <script>
                                        alert("<%= strMessage  %>");
                                    </script>
                                    <%}
                                    %>
                                </select>
                            </td>
                            <% }else{ %>
                            <td><select name="cmbTypeDocument" id="cmbTypeDocument" onchange="javascript:changeTypeDocDig(this);">
                                <option value="0"></option>
                                <option value='<%=strTypeDocument%>'><%=strTypeDocumentValue%></option>
                                </select>
                            </td>
                            <%}%>
                            <td></td>
                            <td class="CellLabel" align="left" valign="top">Num. Doc.</td>
                            <% if( StringUtils.isBlank(strdocument)){ %>
                            <td><input type="text" name="Rut" id="txtNumDoc" onkeypress="return NumCheck(event, this)"  align="left" class="CellContent" />
                                <input type="hidden"  id="hdflagDni"    name= "hdflagDni" value="0"   />
                            </td>
                            <% }else{ %>
                            <td><input type="text" name="Rut" id="txtNumDoc" value="<%=strdocument%>" align="left" class="CellContent" readonly />
                                <input type="hidden"  id="hdflagDni"    name= "hdflagDni" value="1"  />
                            </td>
                            <%}%>
                            <td></td>
                            <td>
                                <input type="button" id="btnRegistrarDatos" onclick="registerExonerate()" value="Registrar Datos" align="left" class="CellContent"></td>
                            <td/>
                        </tr>
                        <tr id="idActionEval">
                            <td class="CellLabel" align="left" valign="top">Acci&oacute;n</td>
                            <td>
                                <select name="cmbAccion" id="cmbAccion" onchange="cargarMotivo(this.value)">
                                    <option value="0"></option>
                                    <%hshData= biometricaService.getListAction(orderId);
                                        comboAction=(ArrayList)hshData.get("objArrayList");
                                        solution = (String)hshData.get("strSolution");
                                        nbPend = (Integer) hshData.get("nbPend");
                                        strMessage=(String)hshData.get("strMessage");
                                        if(StringUtils.isBlank(strMessage)){
                                            for(int i=0;i<comboAction.size();i++){
                                                HashMap objeto=(HashMap)comboAction.get(i);
                                                String dominio=(String)objeto.get("wn_npdomainid");
                                                String descripcion=(String)objeto.get("wv_npdescription");
                                    %>
                                    <option value='<%=dominio%>'><%=descripcion%></option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </td>
                            <td></td>
                            <td class="CellLabel" align="left" valign="top">Motivo</td>
                            <td><select name="cmbMotivo" id="cmbMotivo" >
                                <option value="0"></option>
                            </select>
                            </td>
                            <td></td>
                            <td><input type="button" id="btnAnular" onclick="AnularOrder()" value="Anular Verificaci&oacute;n y Orden" align="left" class="CellContent" disabled></td>
                        </tr>
                        <tr><th height="5px"></th></tr>
                        <tr id="idVerifIdent">
                            <td colspan="5" style="text-align: center;">
                                <input type="button" id="btnVerificacion" name="btnVerificacion" value="Verificar Identidad" onclick="verifPendiente()" align="left" class="CellContent" />
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <input type="hidden"  id="NroAudit"           name= "NroAudit"    />
                                <input type="hidden"  id="ErcDesc"            name= "ErcDesc"     />
                                <input type="hidden"  id="Nombres"            name= "Nombres"     />
                                <input type="hidden"  id="ApPaterno"          name= "ApPaterno"   />
                                <input type="hidden"  id="ApMaterno"          name= "ApMaterno"   />
                                <input type="hidden"  id="Vigencia"           name= "Vigencia"    />
                                <input type="hidden"  id="Restriccion"        name= "Restriccion" />
                                <input type="hidden"  id="codError"           name= "codError" />
                                <input type="hidden"  id="hdnSessionLogin"    name= "hdnSessionLogin"  value="<%=strlogin%>" />
                                <input type="hidden"  id="hdnAuthorizedUser"  name= "hdnAuthorizedUser"  value="<%=strauthorizedUser%>" />
                                <input type="hidden"  id="hdnAuthorizedDni"   name= "hdnAuthorizedDni"  value="<%=strauthorizerDni%>" />
                                <input type="hidden"  id="hdnAuthorizedPass"  name= "hdnAuthorizedPass"  value="<%=strauthorizerPass%>" />
                                <input type="hidden"  id="origen"             name= "origen"  value="new"   />
                                <input type='hidden'  id="hdSource"           name= "hdSource"        value="<%=Constante.Source_CRM%>" />
                                <input type="hidden"  id="hdVentana"          name= "hdVentana"       value='0' />
                                <input type="hidden"  id="hdErcAcepta"        name= "hdErcAcepta"      />
                                <input type="hidden"  id="hdErrorAcepta"      name= "hdErrorAcepta"      />
                                <input type="hidden"  id="hdUserId"           name= "hdUserId"    value="<%=strUserID%>"   />

                            </td>
                        </tr>

                    </table>
                </td>
            </tr>
        </table>
        <!-- fin tabla para lo de exoneracion -->




    </form>
</body>

<script type="text/javascript">

    <% // cargamos al ultimo representante legal de un cliente si lo tuviera
        legalRepresentativeBeanMap = biometricaService.getLastLegalRepresentative(idOrder);
        System.out.println("esto pinta en el jsp: strMessage" + legalRepresentativeBeanMap.get("strMessage") + " -  strMessageError" + legalRepresentativeBeanMap.get("strMessageError"));
        if(StringUtils.isBlank((String)legalRepresentativeBeanMap.get("strMessage")) && StringUtils.isBlank((String)legalRepresentativeBeanMap.get("strMessageError"))
            && legalRepresentativeBeanMap.get("legalRepresentativeBean") != null ){
            System.out.println("ESTOY PINTANDO LOS DATOS DEL REPRESENTANTE LEGAL");
            LegalRepresentativeBean lstLegalRepresentative = (LegalRepresentativeBean)legalRepresentativeBeanMap.get("legalRepresentativeBean");
    %>
    var vform = document.frmExonerate;
    vform.cmbTypeDocument.value = <%=lstLegalRepresentative.getValTypeDoc()%>
            vform.Rut.value = <%=lstLegalRepresentative.getNumberDocument()%>

    if(vform.cmbTypeDocument.value == 3){
        document.getElementById("idActionEval").style.display = "table-row";
        document.getElementById("idVerifIdent").style.display = "table-row";
        document.getElementById("btnRegistrarDatos").style.display = "none";
    }else{
        document.getElementById("idActionEval").style.display = "none";
        document.getElementById("idVerifIdent").style.display = "none";
        document.getElementById("btnRegistrarDatos").style.display = "block";
    }
    <%}else{%>
    document.getElementById("idActionEval").style.display = "none";

    document.getElementById("idVerifIdent").style.display = "none";
    document.getElementById("btnRegistrarDatos").style.display = "none";
    <%}%>

    function changeTypeDoc(typeDoc){
        document.getElementById("txtNumDoc").value="";
        if(typeDoc.value==3){
            document.getElementById("idActionEval").style.display = "table-row";
            document.getElementById("idVerifIdent").style.display = "table-row";
            document.getElementById("btnRegistrarDatos").style.display = "none";
        }else{
            document.getElementById("idActionEval").style.display = "none";
            document.getElementById("idVerifIdent").style.display = "none";
            if(typeDoc.value == 0) document.getElementById("btnRegistrarDatos").style.display = "none";
            else document.getElementById("btnRegistrarDatos").style.display = "block";
        }
    }
    /*
    Cambio de Tipo de Documento para Digitalizacion PRY 0787 - JVERGARA
    */
    function changeTypeDocDig(typeDoc){
            document.getElementById("idActionEval").style.display = "none";
            document.getElementById("idVerifIdent").style.display = "none";
            if(typeDoc.value == 0) document.getElementById("btnRegistrarDatos").style.display = "none";
            else document.getElementById("btnRegistrarDatos").style.display = "block";
    }

    function cargarMotivo(dominio){
        var cmbAnular='<%=constante.action_Domain_A%>';
        var solution = '<%=solution%>';
        var nbPend = '<%=nbPend%>';
        $("#cmbMotivo").empty();
        if(cmbAnular==dominio)
        { document.getElementById("btnAnular").disabled=false;
            document.getElementById("btnVerificacion").disabled=true;
        }
        else{ document.getElementById("btnAnular").disabled=true;
            document.getElementById("btnVerificacion").disabled=false;
        }

        var server='<%=strURLBiometricaEditServlet%>';
        var params = 'myaction=listMotivo&domain='+dominio+'&solution='+solution+'&nbPend='+nbPend;
        $.ajax({
            url: server,
            data: params,
            type: "POST",
            success:function(data){
                $("#cmbMotivo").append(data);
            }
        });
    }

    function AnularOrder() {

        var vform = document.frmExonerate;
        var accion = vform.cmbAccion.value;
        var motivo = vform.cmbMotivo.value;
        var authorizedUser = vform.hdnAuthorizedUser.value;
        var login = vform.hdnSessionLogin.value;
        var documento = vform.Rut.value;

        var cmbAnular = '<%=constante.action_Domain_A%>';

        if (accion == cmbAnular && motivo > 0) {
            if (confirm("¿ Se procedera a cancelar la verificacion de identidad y se anulara la Orden. Desea Continuar ?")) {

                document.getElementById("btnAnular").disabled=true;
                vform.target = "bottomFrame";
                vform.myaction.value = 'anularOrden';
                vform.action = '<%=strURLBiometricaServlet%>';
                vform.submit();
            } else {
                alert("Debe seleccionar un motivo para realizar la anulacion de la verificacion");
            }
        }
    }


    function verifPendiente(){

        if(validarDocumento()){

            var vform=document.frmExonerate;

            var selectAccion=document.getElementById("cmbAccion").value;
            var order=vform.idOrder.value;
            var documento=vform.Rut.value;
            var accionNoBiometrica='<%=constante.action_Domain_NB%>';
            var server='<%=strURLBiometricaEditServlet%>';
            var params = 'myaction=verifPendiente&order='+order+'&selectAccion='+selectAccion+'&documento='+documento;
            $.ajax({
                url: server,
                data: params,
                type: "POST",
                beforeSend: function(){
                    document.getElementById("btnVerificacion").disabled=true;
                },
                error: function(){
                    document.getElementById("btnVerificacion").disabled=false;
                },
                success:function(data){
                    if(data=='1'){

                        if(selectAccion!=accionNoBiometrica){
                            Verificar();
                        }else{
                            verificacionNOBiometrica();
                        }
                    }else{
                        alert(data);
                    }
                }
            });

        }
    }

    function validarDocumento(){
        var resultado=false;

        var vform=document.frmExonerate;
        var documento=vform.Rut.value;

        if(documento.length=0){ alert("Debe ingresar el número de Identidad");}
        else if(documento.length<8){alert("El DNI debe tener 8 caracteres");}
        else if(documento.length=8){resultado=true;}

        return resultado;
    }

    function NumCheck(e, field) {
        key = e.keyCode ? e.keyCode : e.which
        // backspace
        if (key == 8) return true
        // 0-9
        if (key > 47 && key < 58) {
            if (field.value == "") return true
            regexp = /.[0-9]{7}$/
            return !(regexp.test(field.value))
        }
        // .
        if (key == 46) {
            if (field.value == "") return false
            regexp = /^[0-9]+$/
            return regexp.test(field.value)
        }
        // other key
        return false
    }

    function registerExonerate(){
        var vform = document.frmExonerate;
        var typeDoc = vform.cmbTypeDocument.value;
        var numDoc = vform.Rut.value;

        if(numDoc == ""){
            alert("Debe ingresar el numero de documento.");
            return;
        }

        if (confirm("¿ Desea Registrar los datos. ?")) {
            document.getElementById("btnRegistrarDatos").disabled=true;
            vform.target = "bottomFrame";
            vform.myaction.value = 'registerExonerate';
            vform.action = "<%=strURLBiometricaServlet%>?valTypeDoc="+typeDoc;
            vform.submit();
        }

    }

</script>


</html>

<% }catch(Exception ex){
    ex.printStackTrace();
%>

<script>alert('<%=MiUtil.getMessageClean(ex.getMessage())%>');</script>
<%}%>
