<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="pe.com.nextel.util.MiUtil"%>
<%@page import="java.util.*"%>
<%@page import="pe.com.nextel.util.Constante" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="pe.com.nextel.service.BiometricaService" %>
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/jQuery-min.js"></script>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    System.out.println("INICIO SESSION - Verificacion");

    String strRutaContext=request.getContextPath();
    String strURLBiometricaServlet =strRutaContext+"/biometricaservlet";
    String strURLBiometricaEditServlet =strRutaContext+"/biometricaeditservlet";

    String datos=request.getParameter("datos");
    String[] ArrayDatos =datos.split("_");
    int idOrder=Integer.parseInt(ArrayDatos[0]);

    //String useCase=request.getParameter("hdSource");
    String useCase=Constante.Source_CRM;

    String strlogin= ArrayDatos[2];
    String strauthorizedUser= ArrayDatos[1];
    String strauthorizerDni=MiUtil.getString(ArrayDatos[3]);
    String strauthorizerPass=MiUtil.getString(ArrayDatos[4]);

    String strdocument="";
    if(ArrayDatos.length>=6){
        strdocument=ArrayDatos[5];
        if(strdocument.equals("null")){
            strdocument = "";
        }
    }

    System.out.println("VerificacionEdit [Order]: "+ArrayDatos[0]+" [Datos]: "+datos);
    System.out.println("[strauthorizedUser:]"+ArrayDatos[1]+"[strlogin:]"+ArrayDatos[2]+"order:"+ArrayDatos[0]);
    System.out.println("login:"+request.getParameter("hdnSessionLogin") +"[tamaño:]"+ArrayDatos.length+"Pass:"+strauthorizerPass);

    int orderId = 0;
    String solution = null;
    int nbPend = 0;

    try {
        BiometricaService biometricaService= new BiometricaService();
        Constante constante=new Constante();
        HashMap hshData=null;
        String strMessage="";
        ArrayList comboAction = new ArrayList();

        orderId = Integer.parseInt(ArrayDatos[0]);
%>

<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Verificaci&oacute;n  Biom&eacute;trica / No Biom&eacute;trica</title>
    <link type="text/css" rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"/>
    <OBJECT classid='CLSID:592B9D7E-51C9-401F-A03C-4DE61FF7008D' name="Autentia" id='Autentia'></OBJECT>
    <script language="javascript"  src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
    <script type="text/javascript">

        function cmdTerminar()
        {
            document.frmBiometrica.NroAudit.value   =  "";
            document.frmBiometrica.ErcDesc.value   =  "";
            document.frmBiometrica.Nombres.value   =  "";
            document.frmBiometrica.ApPaterno.value =  "";
            document.frmBiometrica.ApMaterno.value =  "";
            document.frmBiometrica.Vigencia.value  =  "";
            document.frmBiometrica.Restriccion.value  =  "";
            document.frmBiometrica.codError.value  =  "";
        }

        function TParams()
        {
            this.Rut = '';
            this.DV  = '';
            this.Lugar = '';
        }

        function Verificar(){
            var vform=document.frmBiometrica;
            Iniciar();
            var codError=vform.codError.value;
            if(codError!='<%=constante.aceptaCancel%>' ) {
                verifBiometricaEdit();
            } else {
                document.getElementById('btnVerificacion').disabled=false;
            }
        }

        /*
         LROSALES    23/09/2016      Se modificó el campo AppOrigen para que reciba como parámetro el de la aplicación
         que está solicitando la verificación de identidad.
         */
        function Iniciar()
        {

            cmdTerminar();
            var Rut		= document.frmBiometrica.Rut.value;
            WsUser		= '<%=strauthorizerDni%>';
            NroIntentoIni = "1";
            //AppOrigen = "CRM";
            AppOrigen = '<%=useCase%>';
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

            if (document.frmBiometrica.Rut.value == "")
            {
                document.frmBiometrica.Rut.focus ();
                return false;
            }

            if (TieneAutentia())
            {
                var Params   = new TParams;
                Params.Rut  		= Rut ;
                Params.WsUser		= '<%=strauthorizerDni%>';
                Params.NroIntentoIni = "1";
                //Params.AppOrigen     = "CRM";
                Params.AppOrigen     = '<%=useCase%>';
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

                document.frmBiometrica.ErcDesc.value     =  Params.ErcDesc;
                document.frmBiometrica.Nombres.value     =  Params.Nombres;
                document.frmBiometrica.ApPaterno.value   =  Params.ApPaterno;
                document.frmBiometrica.ApMaterno.value   =  Params.ApMaterno;
                document.frmBiometrica.Vigencia.value    =  Params.Vigencia;
                document.frmBiometrica.NroAudit.value    =  Params.NroAudit;
                document.frmBiometrica.Restriccion.value =  Params.Restriccion;
                document.frmBiometrica.codError.value    =  Params.ErcCod;
                document.frmBiometrica.hdErcAcepta.value =  erc;


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

                            document.frmBiometrica.ErcDesc.value     =  objAutentia.ParamsGet(6);
                            document.frmBiometrica.Nombres.value     =  objAutentia.ParamsGet(8);
                            document.frmBiometrica.ApPaterno.value   =  objAutentia.ParamsGet(9);
                            document.frmBiometrica.ApMaterno.value   =  objAutentia.ParamsGet(10);
                            document.frmBiometrica.Vigencia.value    =  objAutentia.ParamsGet(11);
                            document.frmBiometrica.NroAudit.value    =  objAutentia.ParamsGet(12);
                            document.frmBiometrica.Restriccion.value =  objAutentia.ParamsGet(13);
                            document.frmBiometrica.codError.value    =  objAutentia.ParamsGet(7);

                        });
                    }else
                    { // variable 'response' con respuesta distinta a proceso correcto (01000009010000010)
                        if(response.substr(0, 2) == 'E:')
                        {
                            document.frmBiometrica.hdErrorAcepta.value    =  response;
                            return false;
                        }
                    }
                });
            } catch (err)
            {
                document.frmBiometrica.hdErrorAcepta.value    =  err.message;
                return false;
            }
        }


        function closePopUpAnular(){
            parent.opener.parent.mainFrame.redirectOrder('<%=idOrder%>');
            parent.close();
        }


        function verifBiometricaEdit(){
            var vForm = document.frmBiometrica;
            var descripcion=vForm.ErcDesc.value;
            var restriccion=vForm.Restriccion.value;
            vForm.ErcDesc.value=unicode(descripcion);
            vForm.Restriccion.value=unicode(restriccion);

            vForm.target = "bottomFrame";
            vForm.action="<%=strRutaContext%>/biometricaservlet?myaction=verificacionBiometricaEdit"+"&idOrder="+'<%=idOrder%>';
            vForm.submit();
        }

        function verificacionNOBiometrica(){
            var vForm = document.frmBiometrica;
            var accion=vForm.cmbAccion.value;
            var motivo=vForm.cmbMotivo.value;
            var cmbNoBiometric='<%=constante.action_Domain_NB%>';

            if(accion==cmbNoBiometric && motivo>0 ) {
                var DescripMotivo = $("#cmbMotivo option:selected").html();
                vForm.target = "bottomFrame";
                vForm.action="<%=strRutaContext%>/biometricaservlet?myaction=verificacionNobiometrica&motivo="+DescripMotivo;
                vForm.submit();
            }else{
                alert("-Debe seleccionar un motivo para realizar la verificación no biométrica");
                document.getElementById("btnVerificacion").disabled=false;
            }
        }


    </script>
</head>
<body>
<EMBED id="plgAutentia" TYPE="application/x-proxyautentiasocket-plugin" ALIGN=CENTER WIDTH=0 HEIGHT=0>
    <form name="frmBiometrica" method="post" >
        <table  align="center" width="100%" border="0">
            <tr>
                <td>
                    <table border="0" class="PortletHeaderColor" cellspacing="0" cellpadding="0" width="100%">
                        <tr class="PortletHeaderColor">
                            <td class="LeftCurve" valign="top" align="left" width="10">&nbsp;&nbsp;</td>
                            <td class="PortletHeaderColor" align="left" valign="top">
                                <font class="PortletHeaderText">
                                    Verificaci&oacute;n Biom&eacute;trica/No Biom&eacute;trica
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
                            <td class="CellLabel" align="left" valign="top">DNI:</td>
                            <% if(StringUtils.isBlank(strdocument)){ %>
                            <td><input type="text" name="Rut" id="Rut"  align="left" class="CellContent" onkeypress="return NumCheck(event, this)" />
                                <input type="hidden"  id="hdflagDni"    name= "hdflagDni" value="0"  />
                            </td>
                            <% }else{ %>
                            <td><input type="text" name="Rut" id="Rut" value="<%=strdocument%>" align="left" class="CellContent"  onkeypress="return NumCheck(event, this)"  />
                                <input type="hidden"  id="hdflagDni"    name= "hdflagDni" value="1" />
                            </td>
                            <%}%>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr>

                        <tr>
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
                                            } }
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
                            <td><input type="button" id="btnAnular" onclick="AnularOrder()" value="Anular Verificaci&oacute;n y Orden" disabled></td>
                        </tr>
                        <tr><th height="5px"></th></tr>
                        <tr>
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
                                <input type="hidden"  id="hdnSessionLogin"    name= "hdnSessionLogin"       value="<%=strlogin%>" />
                                <input type="hidden"  id="hdnAuthorizedUser"  name= "hdnAuthorizedUser"     value="<%=strauthorizedUser%>" />
                                <input type="hidden"  id="hdnAuthorizedDni"   name= "hdnAuthorizedDni"      value="<%=strauthorizerDni%>" />
                                <input type="hidden"  id="hdnAuthorizedPass"  name= "hdnAuthorizedPass"     value="<%=strauthorizerPass%>" />
                                <input type="hidden"  id="origen"             name= "origen"                value="edit"   />
                                <input type='hidden'  id="hdSource"           name="hdSource"               value="<%=useCase%>" />
                                <input type="hidden"  id="hdVentana"          name="hdVentana"              value='0' />
                                <input type="hidden"  id="hdErcAcepta"        name= "hdErcAcepta"                       />
                                <input type="hidden"  id="hdErrorAcepta"      name= "hdErrorAcepta"                     />
                                <input type="hidden"  id="useCase"            name= "useCase"               value="<%=useCase%>" />
                            </td>
                        </tr>

                    </table>
                </td>
            </tr>
        </table>
    </form>
</body>
<script type="text/javascript">
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

    function verifPendiente(){

        if(!validarDocumento()){ alert("Ingresar documento");return false;}

        var vform=document.frmBiometrica;
        var selectAccion=vform.cmbAccion.value;
        var order=vform.idOrder.value;
        var documento=vform.Rut.value;
        var useCase=vform.useCase.value;
        var accionNoBiometrica='<%=constante.action_Domain_NB%>';

        var server='<%=strURLBiometricaEditServlet%>';
        var params = 'myaction=verifPendiente&order='+order+'&selectAccion='+selectAccion+'&documento='+documento+'&useCase='+useCase;
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


    function AnularOrder(){
        var vform=document.frmBiometrica;
        var accion=vform.cmbAccion.value;
        var motivo=vform.cmbMotivo.value;
        var documento=vform.Rut.value;

        var cmbAnular='<%=constante.action_Domain_A%>';

        if(accion==cmbAnular && motivo>0 ){
            if(confirm("¿ Se procedera a cancelar la verificacion de identidad y se anulara la Orden. Desea Continuar ?")) {

                document.getElementById("btnAnular").disabled=true;
                var vForm = document.frmBiometrica;
                vForm.target = "bottomFrame";
                vForm.myaction.value='anularOrden';
                vForm.action='<%=strURLBiometricaServlet%>';
                vForm.submit();
            }
        }else{
            alert("Debe seleccionar un motivo para realizar la anulacion de la verificacion");
            document.getElementById("btnVerificacion").disabled=false;
        }
    }



    function validarDocumento(){
        var resultado=false;

        var vform=document.frmBiometrica;
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


</html>

<% }catch(Exception ex){
    ex.printStackTrace();
%>

<script>alert('<%=MiUtil.getMessageClean(ex.getMessage())%>');</script>
<%}%>

