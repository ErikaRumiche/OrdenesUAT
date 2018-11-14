<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@ page import="oracle.portal.provider.v2.ProviderSession" %>
<%@ page import="oracle.portal.provider.v2.ProviderUser" %>
<%@ page import="oracle.portal.provider.v2.url.UrlUtils" %>
<%@ page import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil" %>
<%@page import="pe.com.nextel.bean.*" %>
<%@page import="pe.com.nextel.util.*" %>
<%@page import="pe.com.nextel.service.*"%>
<%@page import="pe.com.nextel.exception.SessionException" %>
<%@page import="java.util.*"%>

<%

    try{
        System.out.println("---------------------- INICIO JP_ORDER_EDIT_STAR------------------");
        String strSessionId = "";
     /* inicio - comentar para probar localmente*/

        PortletRenderRequest pReq = (PortletRenderRequest)
                request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);

        ProviderUser objetoUsuario1 = pReq.getUser();
        strSessionId=objetoUsuario1.getPortalSessionId();

    /*fin - comentar para probar localmente */

        System.out.println("Sesión capturada después del request : " + strSessionId );
        PortalSessionBean objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
        if(objPortalSesBean==null) {
            System.out.println("No se encontró la sesión de Java ->" + strSessionId);
            throw new SessionException("La sesión finalizó");
        }

        //Inicio TIENDA EXPRESS - Se requiere identificar la tienda del usuario logeado , asi como si es una TE
        //02/06/2014 JRAMIREZ
        int iBuildingIdUser_=0;
        int iIsTiendaExpress=0;


        try{
            iBuildingIdUser_=objPortalSesBean.getBuildingid();
        }catch(Exception e){
            iBuildingIdUser_=0;
        }

        if(iBuildingIdUser_ !=0){
            SessionService objSessionService  = new SessionService();
            iIsTiendaExpress=objSessionService.getExistTiendaExpress(iBuildingIdUser_);
        }
        //Fin TIENDA EXPRESS
        //EFLORES REQ-0204_2 Creacion de generalService para poder obtener las variables de configuracion
        GeneralService objGeneralService= new GeneralService();

        //Obtener las configuraciones para la validacion de ultima preevaluacion
        HashMap hashInboxPermit = objGeneralService.getDataNpTable("PRE_EVAL_NUM_PORTA","INBOX_PERMIT");
        HashMap hashActionPermit = objGeneralService.getDataNpTable("PRE_EVAL_NUM_PORTA","ACTION_PERMIT");
        String msgErrorInboxPermit = (String) hashInboxPermit.get("strMessage");
        String msgErrorActionPermit = (String) hashActionPermit.get("strMessage");
        ArrayList inbox_permit = new ArrayList();ArrayList action_permit = new ArrayList();
        if(msgErrorInboxPermit != null){
            System.out.println("Error INBOX_PERMIT [validacionUltimaPreevaluacion][JP_ORDER_EDIT_START_ShowPage.jsp] "+msgErrorInboxPermit);
        }else if(msgErrorActionPermit != null){
            System.out.println("Error ACTION_PERMIT [validacionUltimaPreevaluacion][JP_ORDER_EDIT_START_ShowPage.jsp] "+msgErrorActionPermit);
        }else{
            inbox_permit = (ArrayList)hashInboxPermit.get("objArrayList");
            action_permit = (ArrayList)hashActionPermit.get("objArrayList");
        }

        int iUserId = objPortalSesBean.getUserid();
        String strRutaContext=request.getContextPath();
        String actionURL_CustomerServlet = strRutaContext+"/customerservlet";
        String strURLOrderServlet =strRutaContext+"/editordersevlet";
        String strURLReject = strRutaContext+"/PAGEEDIT/OrderRejectEdit.jsp";
        String strURLPayment = strRutaContext+"/PAGEEDIT/OrderPaymentEdit.jsp";
        String strURLSDServlet =strRutaContext+"/editdsservlet";
        String strURLBiometricEdit=strRutaContext+"/biometricaeditservlet";
        String strURLBiometric=strRutaContext+"/biometricaservlet";
        String actionURL_GeneralServlet = strRutaContext+"/generalServlet"; //EFLORES PM0010274

        String actionURL_NormalizarServlet    = strRutaContext+"/normalizarDireccionServlet";
        //EFLORES TDECONV003-3
        HashMap mapValidMessage = objGeneralService.getDescriptionByValue("MSG_ALERT_MAX_ITEMS_PROPIOS", "MIGRATION_PRE_POST" );
        String msg_chk_migration_error = mapValidMessage.get("strDescription")==null?"": mapValidMessage.get("strDescription").toString();
        String plan_reg_exp_validation = objGeneralService.getValue("MIGRATION_PRE_POST","PLAN_REGEXP");
        System.out.println("[TDECONV003-3] msg_chk_migration_error = "+msg_chk_migration_error);
        System.out.println("[TDECONV003-3] plan_reg_exp_validation = "+plan_reg_exp_validation);

        System.out.println("---------------------- FIN JP_ORDER_EDIT_STAR------------------");
%>
<script>
    var arrInboxPermit = [];var arrActionPermit = [];
    <% for (int i=0; i<inbox_permit.size(); i++) {
    NpTableBean tb = (NpTableBean)inbox_permit.get(i); %>
    arrInboxPermit[<%= i %>] = "<%= tb.getNpvalue() %>";
    <% } %>
    <% for (int i=0; i<action_permit.size(); i++) {
    NpTableBean tb = (NpTableBean)action_permit.get(i); %>
    arrActionPermit[<%= i %>] = "<%= tb.getNpvalue() %>";
    <% } %>
</script>
<script>var appContext = "<%=request.getContextPath()%>";</script>
<script LANGUAGE="JavaScript" SRC="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></SCRIPT>
<script LANGUAGE="JavaScript" SRC="<%=Constante.PATH_APPORDER_SERVER%>/Resource/date-picker.js"></SCRIPT>
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderEdit.js"></script>
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderException.js"></script>
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/DateTimeBasicOperations.js"></script>
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderIXNew.js"></script>
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderIXEdit.js"></script>
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsItemsIXEdit.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/websales/Resource/js/Roaming.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/websales/Resource/js/Portability.js"></script>
<script LANGUAGE="JavaScript" SRC="<%=Constante.PATH_APPORDER_SERVER%>/Resource/jQuery-min.js"></SCRIPT> <!-- EFLORES PM0010274 -->

<script>

/*  Inicio Constantes */
var TYPE_CERRADO="CERRADO";
var SPEC_REPAIRS = new Array();
SPEC_REPAIRS[0] = "2028";
SPEC_REPAIRS[1] = "2029";
SPEC_REPAIRS[2] = "2030";
SPEC_REPAIRS[3] = "2031";

var SPEC_PORT_DIR_ENTREGA = new Array();
SPEC_PORT_DIR_ENTREGA[0] = "2070";  //JLINARES
SPEC_PORT_DIR_ENTREGA[1] = "2071";  //JLINARES

    // MMONTOYA [ADT-RCT-092 Roaming con corte]
    // Inicio constantes de Roaming.js
    var SPEC_ACTIVAR_PAQUETES_ROAMING = <%=Constante.SPEC_ACTIVAR_PAQUETES_ROAMING%>;
    var TIPO_BOLSA_RECURRENTE = "<%=Constante.TIPO_BOLSA_RECURRENTE%>";
    var CONTEXT_PATH = "<%=request.getContextPath()%>";
    // Fin constantes de Roaming.js

	// MMONTOYA [PM0010658 Portabilidad]
    // Inicio constantes de Portability.js
    var SPEC_PREPAGO_PORTA = <%=Constante.SPEC_PREPAGO_PORTA%>;
    var SPEC_POSTPAGO_PORTA = <%=Constante.SPEC_POSTPAGO_PORTA%>;
    // Fin constantes de Portability.js

/*  Fin Constantes */

var flagDigitalMessage=true;

function fxObjectConvert(objObject,objValue){
    form = document.frmdatos;
    try{
        cadena = "form."+objObject+".value = '"+objValue+"'";
        eval(cadena);
    }catch(e){

    }
}

function fxContentInArray(value){
    for(var i=0;i<SPEC_REPAIRS.length;i++){
        if ( value==SPEC_REPAIRS[i]){
            return true;
        }
    }
    return false;
}

function compare_dates(fecha, fecha2) {
    var xMonth=fecha.substring(3, 5);
    var xDay=fecha.substring(0, 2);
    var xYear=fecha.substring(6,10);
    var yMonth=fecha2.substring(3, 5);
    var yDay=fecha2.substring(0, 2);
    var yYear=fecha2.substring(6,10);
    if (xYear> yYear)  {
        return(true)
    } else {
        if (xYear == yYear) {
            if (xMonth> yMonth) {
                return(true)
            }  else {
                if (xMonth == yMonth)  {
                    if (xDay> yDay)
                        return(true);
                    else
                        return(false);
                } else {
                    return(false);
                }
            }
        }   else
            return(false);
    }
}

function fxValidateOrderBucket(){
    form = parent.mainFrame.document.frmdatos;
    if (form.hdnSpecificationIdJ.value==2026 || form.hdnSpecificationIdJ.value==2081) {
        if (form.txtEstadoOrden.value=='TIENDA01' && form.cmbAction.value=='AVANZAR' && form.hdnPagoEstado.value!='Cancelado') {
            alert("No se completó la transacción: El pago de la orden no esta cancelado.");
            return false;
        }
        var fecha = new Date();
        var day = "";
        var month = "";
        if (fecha.getDate() <10) {
            day = "0" + fecha.getDate();
        } else {
            day = "" + fecha.getDate();
        }
        if ((fecha.getMonth() +1) <10) {
            month = "0" + (fecha.getMonth() +1);
        } else {
            month = "" + (fecha.getMonth() +1);
        }
        var fechaHoy=day + "/" + month + "/" + fecha.getFullYear();
        if (form.txtEstadoOrden.value=='TIENDA01' && form.cmbAction.value=='AVANZAR' && compare_dates(fechaHoy, form.hdnFechaProcAut_old.value)){
            alert("La fecha de Proceso no puede ser menor a la fecha de hoy");
            return false;
        }
    }

    //INICIO VALIDACION TIENDA EXPRESS JRAMIREZ 
    var typeService = form.hdnTypeService.value;
    var tienda = form.hdnTienda.value;
    var subcategoriaid=form.hdnSubCategoria.value;
    var i_nporderid;

    if (window.form.hdnNumeroOrder){
        i_nporderid= form.hdnNumeroOrder.value;
    }else{
        i_nporderid=form.hdnOrderId.value;
    }

    if(typeService=="<%=Constante.TYPE_SERVICE_TE%>" ){

        subcatContado=0;
        var url_server = '<%=strURLOrderServlet%>';
        var params = 'myaction=ValidateSubcategoria&subcategoriaid='+subcategoriaid;
        $.ajax({
            url: url_server,
            data: params,
            async: false,
            type: "POST",
            success:function(data){
                subcatContado=data;
            },
            error:function(data){
                alert("Categoria no indentificada :"+subcategoriaid);
            }
        });

        //Se valida si el objeto no es undefined
        if (window.form.cmbLugarAtencion){
            if(form.cmbLugarAtencion.value!=tienda){
                alert("Lugar de Despacho debe ser la misma Tienda Express");
                form.cmbLugarAtencion.focus();
                return false;
            }
        }

        //INICIO: PRY-0864_2 | AMENDEZ - Se adiciona check vep a validacion
        if(subcatContado==1){

            //INICIO: PRY-1200
            var flagvep=-1;
            try {
                if(form.chkVepFlag == undefined){
                    flagvep=0;
                }else{
                    if(form.chkVepFlag.checked == true){
                        flagvep=1;
                    }else if(form.chkVepFlag.checked == false){
                        flagvep=0;
                    }else {
                        flagvep=0;
                    }
                }
            }catch (e){
                flagvep=0;
            }
            //FIN: PRY-1200

            if(form.cmbFormaPago.value=="<%=Constante.PAYFORM_CARGO_EN_RECIBO%>" && flagvep==0){
                alert("El pago debe ser al contado");
                form.cmbLugarAtencion.focus();
                return false;
            }
        }
	//FIN: PRY-0864_2 | AMENDEZ - Se adiciona check vep a validacion

        //Validacion a nivel de orden
        //Se valida si el objeto no es undefined

        intTiendaForeign =0;
        if (window.form.cmbLugarAtencion){
            intTiendaForeign = form.cmbLugarAtencion.value;
        }

        inpbuildingorder=0;

        //Se verifica si la orden existe
        params = 'myaction=ValidateOrderExist&iorderId='+i_nporderid;
        $.ajax({
            url: url_server,
            data: params,
            async: false,
            type: "POST",
            success:function(data){
                existOrder=data;
            }
        });

        //Si la orden  existe se realiza la validacion a nivel de usuario
        if(existOrder==1){

            params = 'myaction=GetBuildingByOrder&iorderId='+i_nporderid;
            $.ajax({
                url: url_server,
                data: params,
                async: false,
                type: "POST",
                success:function(data){
                    inpbuildingorder=data;
                }
            });

            //Se verifica si el buildingid de la orden es una TE
            iIsTiendaExpress_buildingorder=0;
            params = 'myaction=ValidateIsTiendaExpress&ibuildingId='+inpbuildingorder;
            $.ajax({
                url: url_server,
                data: params,
                async: false,
                type: "POST",
                success:function(data){
                    iIsTiendaExpress_buildingorder=data;
                },
                error:function(data){
                    alert("Tienda no identificada :"+intTiendaForeign);
                }
            });

            if (iIsTiendaExpress_buildingorder==0){

                //Se verifica si la tienda destino es tienda express
                iIsTiendaExpress_frgn=0;
                params = 'myaction=ValidateIsTiendaExpress&ibuildingId='+intTiendaForeign;
                $.ajax({
                    url: url_server,
                    data: params,
                    async: false,
                    type: "POST",
                    success:function(data){
                        iIsTiendaExpress_frgn=data;
                    },
                    error:function(data){
                        alert("Tienda no identificada :"+intTiendaForeign);
                    }
                });


                if(iIsTiendaExpress_frgn==1){
                    alert("Lugar de Despacho no puede ser una Tienda Express");
                    return;
                }

            }
        }
    }else{


        //Se valida si el objeto no es undefined
        if (window.form.cmbLugarAtencion){

            var url_server = '<%=strURLOrderServlet%>';
            intTiendaForeign = form.cmbLugarAtencion.value;
            existOrder=0;
            inpbuildingorder=0;

            //Se verifica si la orden existe
            var params = 'myaction=ValidateOrderExist&iorderId='+i_nporderid;
            $.ajax({
                url: url_server,
                data: params,
                async: false,
                type: "POST",
                success:function(data){
                    existOrder=data;
                }
            });


            //Si la orden no existe se realiza la validacion a nivel de usuario
            if(existOrder==0){

                //Se verifica si la tienda destino es una TE
                iIsTiendaExpress_frgn=0;
                params = 'myaction=ValidateIsTiendaExpress&ibuildingId='+intTiendaForeign;
                $.ajax({
                    url: url_server,
                    data: params,
                    async: false,
                    type: "POST",
                    success:function(data){
                        iIsTiendaExpress_frgn=data;
                    },
                    error:function(data){
                        alert("Tienda no identificada :"+intTiendaForeign);
                    }
                });

                if(iIsTiendaExpress_frgn !=0 ){
                    alert("La Tienda de Recojo no puede ser una Tienda Express");
                    return;
                }

            }else{


                //Se valida a nivel de orden
                //Se obtiene el building de la orden
                params = 'myaction=GetBuildingByOrder&iorderId='+i_nporderid;
                $.ajax({
                    url: url_server,
                    data: params,
                    async: false,
                    type: "POST",
                    success:function(data){
                        inpbuildingorder=data;
                    }
                });

                //Se verifica si el buildingid de la orden es una TE
                iIsTiendaExpress_buildingorder=0;
                params = 'myaction=ValidateIsTiendaExpress&ibuildingId='+inpbuildingorder;
                $.ajax({
                    url: url_server,
                    data: params,
                    async: false,
                    type: "POST",
                    success:function(data){
                        iIsTiendaExpress_buildingorder=data;
                    },
                    error:function(data){
                        alert("Tienda no identificada :"+intTiendaForeign);
                    }
                });

                if(iIsTiendaExpress_buildingorder==1){
                    if(inpbuildingorder !=intTiendaForeign){
                        alert("Lugar de Despacho debe ser la misma Tienda Express de Origen");
                        return;
                    }
                }else{
                    iIsTiendaExpress_frgn=0;
                    params = 'myaction=ValidateIsTiendaExpress&ibuildingId='+intTiendaForeign;
                    $.ajax({
                        url: url_server,
                        data: params,
                        async: false,
                        type: "POST",
                        success:function(data){
                            iIsTiendaExpress_frgn=data;
                        },
                        error:function(data){
                            alert("Tienda no identificada :"+intTiendaForeign);
                        }
                    });

                    if(iIsTiendaExpress_frgn !=0 ){
                        alert("Lugar de Despacho no puede ser una Tienda Express");
                        return;
                    }

                }


            }

        }
    }
    //FIN VALIDACION TIENDA EXPRESS


    return true;
}

function validateEmail(){
    if (document.getElementById('txtCorreoElectronico') &&
            document.getElementById('txtCorreoElectronico').value != ""){

        expr = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
        if ( !expr.test(document.getElementById('txtCorreoElectronico').value) ){
            alert("El email es incorrecto por favor validar.");
            document.getElementById('txtCorreoElectronico').focus();
            return false;
        }else{
            return true;
        }

    }else{
        return true;
    }
}

function validateFecProcAutSSAA(especification){
    formDat = parent.mainFrame.document.frmdatos;
    var f = new Date();
    var dateCurrent =  f.getDate() + "/" + (f.getMonth() +1) + "/" + f.getFullYear();
    if(especification == '<%=Constante.SPEC_ACTIVAR_DESACTIVAR_SERVICIOS%>' && formDat.cmbAction.value == '<%=Constante.ACTION_INBOX_SALTAR%>'){
        if(formDat.cmbInboxNext){
            if(formDat.cmbInboxNext.value == '<%=Constante.INBOX_ACTIVACION_DIFERIDA%>'){
                if(formDat.txtFechaProceso.value == null || formDat.txtFechaProceso.value == ''){
                    alert('Por favor, ingrese la Fecha Proc. Aut.');
                    return true;
                }else{
                    if(formDat.txtFechaProceso.value == dateCurrent || formDat.txtFechaProceso.value < dateCurrent){
                        alert('Por favor, ingrese una Fecha Proc. Aut. mayor a la Fecha Actual');
                        return true;
                    }
                }
            }
        }
    }

    return false;
}

function fxDoSubmit(myAction){
    form = parent.mainFrame.document.frmdatos;


    //Inicio mensaje de confirmacion para hacer anular la orden - JGABRIEL - SAR's PM0010271
    if (window.form.cmbEstadoReparacion){
        if (form.cmbEstadoReparacion.value == "ANULADO" ) {
            var n_orderid = 0;
            if (window.form.hdnNumeroOrder){
                n_orderid = form.hdnNumeroOrder.value;
            }else{
                n_orderid = form.hdnOrderId.value;
            }
            var msg = '"Esta seguro qué desea anular la Orden de Reparación '+n_orderid+'?"\n';
            msg = msg + ' "< No olvidar anular OE , la OST en ANOVO y generar PI >"';
            if (!confirm(msg))
                return;
        }
    }
    //Fin  mensaje de confirmacion para hacer anular la orden - JGABRIEL - SAR's PM0010271


    // INICIO TIENDA EXPRESS JRAMIREZ

    if (window.form.cmbTienda){

        intTiendaSource = form.cmbTienda.value;
        intTiendaForeign = form.cmbTiendaRecojo.value;
        intTiendaUser =<%=iBuildingIdUser_%>;
        iIsTiendaExpress =<%=iIsTiendaExpress%>;

        if(iIsTiendaExpress !=0){

            if(intTiendaSource != intTiendaUser){
                alert("La Tienda Express de origen no corresponde al Usuario");
                return;
            }

            if(intTiendaSource != intTiendaForeign){
                alert("La Tienda Express de origen es distinta a la Tienda Express de recojo");
                return;
            }
        }else{
            iIsTiendaExpress_frgn=0;
            var url_server = '<%=strURLOrderServlet%>';
            var params = 'myaction=ValidateIsTiendaExpress&ibuildingId='+intTiendaForeign;
            $.ajax({
                url: url_server,
                data: params,
                async: false,
                type: "POST",
                success:function(data){
                    iIsTiendaExpress_frgn=data;
                },
                error:function(data){
                    alert("Tienda no identificada :"+intTiendaForeign);
                }
            });

            if(iIsTiendaExpress_frgn !=0 ){
                alert("La Tienda de Recojo no puede ser una Tienda Express");
                return;
            }
        }
    }
    // FIN TIENDA EXPRESS JRAMIREZ

    var flagRepair = fxContentInArray(form.hdnSubCategoria.value);
    fxIndiceItemDevice();

    if(myAction=='updateOrden'){

    	//PRY-1093 JCURI
    	if(!validateCourierDelivery()){
    		return;
    	}

        //Portabilidad Fase 3 - LVALENCIA
        if (form.hdnSpecificationIdJ.value == '<%=Constante.SPEC_POSTPAGO_PORTA%>'){
            var table = document.all ? document.all["items_table"]:document.getElementById("items_table");
            var customertype = document.getElementById("customer_type");
            var nroItems = document.getElementById("nroItems");
         //alert("nroItems: "+nroItems.value);
         if ((table.rows.length > 11) || (nroItems.value > 10)){
                if (!confirm('Los valores seleccionados para: \nTipo de Cliente es: '+customertype.value/*form.cmbCustomerType.value*/+'\nDías a programar es: '+form.cmbScheduleDays.value+'\n¿Desea continuar?'))
                    return;
            }
        }

        //Validar lista de numeros telefonicos EFLORES REQ-0204 07/01/2016
        if(!fxValidateUltimaPreevaluacion()){
            return;
        }
        //Renovacion de Bucket - FPICOY
        if(!fxValidateOrderBucket()){
            return;
        }
        //Volumen de Orden
        if(!fxValidateOrderVolume()){
            return;
        }

        //PORTEGA validacion de email y reporte cliente clozza
        if(!validateEmail()){
            return;
        }
        //console.log("FIN VALIDACION");

        //PORTEGA validacion de email y reporte cliente clozza
        if(!validateEmail()){
            return;
        }
        
        if(!fxSearchAddressBlackList()){
            return;
        }
        //console.log("FIN VALIDACION");

        // llamada al metodo que valida la cantida de los items de una orden de Portabilidad
        if(!fxValidateItemVolume()) {
            return;
        }

       //LHUAPAYA validación de la fecha de procesos autómaticos para la activación/descativación de servicios
        if(validateFecProcAutSSAA(form.hdnSubCategoria.value)){
            return;
        }

        //INICIO: PRY-1037 | KPEREZ
        if (form.hdnSubCategoria.value == '<%=Constante.SPEC_POSTPAGO_VENTA%>' ){
        if(!validateSimManager()){
            return;
        }
        }
        //FIN: PRY-1037 | KPEREZ


       //JCASTILLO
        fxSetCpuf();

        //CPUENTE6
        if (form.txtEstadoOrden.value == '<%=Constante.INBOX_ALMACEN_TIENDA%>' && form.hdnSubCategoria.value == '<%=Constante.SPEC_REPOSICION%>')
        {
            var url="hdnOrderId="+ g_orderid;

            var parametros = fxRequestXML2("editordersevlet","ValidationEquipReplacement","myaction",url);

            if (parametros!= null && parametros!= "null")
            {
                if (!confirm(parametros+'. Desea Continuar?'))
                    return;
            }
        }
        //CPUENTE6

        //Validacion Orden Vep RMARTINEZ


            if (document.frmdatos.chkVepFlag != undefined){
            //INICIO: PRY-1200 | AMENDEZ
            var iSpecificationId=form.hdnSpecificationIdJ.value;
            if (validateSpecificationVep(iSpecificationId)){
            //FIN: PRY-1200 | AMENDEZ

                if(fxValidateExistItemsVEP(document.frmdatos.cmbVepNumCuotas, 1) == 0 && document.frmdatos.chkVepFlag.checked == true){
                    alert("No es posible grabar una orden de venta a plazos si no se cuenta con ítems de tipo Venta a Plazos");
                    return false;
                }

                //INICIO: PRY-0864 | AMENDEZ
                if(!validateOrderVepCI()){
                    return false;
                }
                //FIN: PRY-0864 | AMENDEZ

                //INICIO: PRY-1200 | AMENDEZ
                var evaluateAction=0;
                if ( form.cmbAction.value == '<%=Constante.ACTION_INBOX_ANULAR%>' ) {
                    evaluateAction=1;
                }

                if ( form.cmbAction.value == '<%=Constante.ACTION_INBOX_BACK%>') {
                    evaluateAction=1;
                }

                if ( evaluateAction != 1) {
                if(!validateInfoVEP()){
                    return false;
                }
            }
                //FIN: PRY-1200 | AMENDEZ
            }

        }



        // INI PRY-1062 AMATEOM
		if ((form.txtEstadoOrden.value == "<%=Constante.INBOX_TIENDA01%>" || form.txtEstadoOrden.value == "<%=Constante.INBOX_CALLCENTER%>" || form.txtEstadoOrden.value == "<%=Constante.INBOX_ADM_VENTAS%>") && (form.cmbAction.value == '' || form.cmbAction.value == "<%=Constante.ACTION_INBOX_AVANZAR%>")){
			if (form.cmbFormaPago.value=="<%=Constante.PAYFORM_CARGO_EN_RECIBO%>") {
				if (form.hdnSpecificationIdJ.value == '<%=Constante.SPEC_CAMBIO_MODELO%>' || form.hdnSpecificationIdJ.value == '<%=Constante.SPEC_CAMBIO_MODELO_ENTRE_TECNOLOGIAS%>') {
					if (!validatePreEvaluationCMD()) {
						return false;
					}
				}
			}
		}
        // FIN PRY-1062 AMATEOM



        //PHIDALGO
        try{
            if(  (form.hndGeneratortype.value != '<%=Constante.NAME_ORIGEN_MASSIVE%>' &&
                    form.cmbAction.value != '<%=Constante.ACTION_INBOX_ANULAR%>' &&
                    form.cmbAction.value != '' &&
                    form.txtEstadoOrden.value == '<%=Constante.INBOX_TIENDA01%>') ||
                    (form.hndGeneratortype.value != '<%=Constante.NAME_ORIGEN_MASSIVE%>' &&
                            form.cmbAction.value != '<%=Constante.ACTION_INBOX_ANULAR%>' &&
                            form.cmbAction.value != '' &&
                            form.txtEstadoOrden.value == '<%=Constante.INBOX_ADM_VENTAS%>') ||
                    (form.hndGeneratortype.value != '<%=Constante.NAME_ORIGEN_MASSIVE%>' &&
                            form.cmbAction.value != '<%=Constante.ACTION_INBOX_ANULAR%>' &&
                            form.cmbAction.value != '' &&
                            form.txtEstadoOrden.value == '<%=Constante.INBOX_RECURSOS_HUMANOS%>') ||
                    (form.hndGeneratortype.value != '<%=Constante.NAME_ORIGEN_MASSIVE%>' &&
                            form.cmbAction.value != '<%=Constante.ACTION_INBOX_ANULAR%>' &&
                            form.cmbAction.value != '' &&
                            form.txtEstadoOrden.value == '<%=Constante.INBOX_CALLCENTER%>')
                    ){
                var url="hdnOrderId="+g_orderid+"&hdnSessionId="+form.hdnSessionId.value+"&txtEstadoOrden="+form.txtEstadoOrden.value+
                        "&txtCompanyId="+form.txtCompanyId.value+"&an_swsiteid="+form.txtSiteId.value+
                        "&hdnSubCategoria="+form.hdnSubCategoria.value+"&hdnCreateBy="+form.hdnCreateBy.value+
                        "&hdnFechaCreacionOrden="+form.hdnFechaCreacionOrden.value+"&hdnLugarDespacho="+form.hdnLugarDespacho.value;
                var parametros = fxRequestXML2("editordersevlet","ValidateBudget","myaction",url);
                if(parametros == 'N'){
                    if (!confirm('Orden no cuenta con Presupuesto de Upgrades. ¿Desea mandar al Inbox de Aprobación?')){
                        return false;
                    }
                }else if(parametros != 'S'){
                    alert(parametros);
                    return false;
                }
            }
            if(!fxValidateReasonRejection()){
                return false;
            }
        }catch(e){
        }
        //Se inicializa el  valor de la anulacion de Orden de Pago con N.
        form.hdnOrdenPagoAnular.value="N";
        //Confirmacion antes de anular la orden
        //SI EXISTEN ORDENES DE PAGO PENDIENTES
        if (form.cmbFormaPago.value=="<%=Constante.PAYFORM_CARGO_EN_RECIBO%>"){

            var intFlagPend;
            var intFlagCancel;
            var url="hdnOrderId="+ g_orderid;
            var parametros = fxRequestXML("generalServlet","requestDoSetOrderPayPend",url);

            if (parametros ==null ){
                alert("Error al realizar la operación");
                return;
            }
            else{
                var iPermitir;
                var token=parametros.indexOf("*");
                intFlagPend=parametros.substring(0,token);
                var sMensaje=parametros.substring(token+1,parametros.length);
                intFlagPend=parseInt(intFlagPend);

                if (sMensaje==null || sMensaje=="null" ){
                    if (intFlagPend>0){
                        var url="hdnOrderId="+ g_orderid;
                        var parametrosCancel = fxRequestXML("generalServlet","requestDoSetOrderPayCancel",url);
                        if (parametrosCancel>0){
                            alert("La orden tiene órdenes de pago canceladas, no se puede modificar la forma de pago.");
                            return;
                        }
                        else{ if (confirm("¿Desea cambiar la Forma de Pago?")){
                            form.hdnOrdenPagoAnular.value="S";
                        }else {
                            form.hdnOrdenPagoAnular.value="N";
                            return false;
                        }
                        }
                    }
                }else{
                    alert("Se ha producido un error: "+sMensaje);
                    return;
                }
            }
        }

        if (flagRepair==false){
            if (form.cmbAction.value=="<%=Constante.ACTION_INBOX_ANULAR%>") {
                if (
                        form.hdnSpecificationIdJ.value == '<%=Constante.SPEC_PORTABILIDAD_REC_NO_FUND%>' ||
                        form.hdnSpecificationIdJ.value == '<%=Constante.SPEC_PORTABILIDAD_RET_NUM%>' ){

                if (!confirm("Está seguro de anular la orden? Tener presente que la orden no podra ser reabierta")) {
                    return false;
                }
            }

                else {
                    if (form.hdnBiometricAnular.value != "<%=Constante.ACTION_INBOX_ANULAR%>") {

                        if (!confirm("Está seguro de anular la orden? Tener presente que la orden no podra ser reabierta")) {
                            return false;
        }
                    }
                }
            }
        }

        if (fxValidateAll()==false)
            return false;

        if (!verificacionBiometricaEdit()){
            verificarNormalizacionDir();
        }

     // MMONTOYA [ADT-RCT-092 Roaming con corte]
     // Validación del servicio roaming recurrente.
     if (!validateAllRecurrentRoamingService(form.hdnSpecificationIdJ.value, g_orderid)) {
         return;
    }
	
	 // MMONTOYA [PM0010658 Portabilidad]
     // Solo se validan las categorías: Portabilidad Solicitud de Postpago y Portabilidad Solicitud de Prepago.
        if (form.hdnSpecificationIdJ.value=="<%=Constante.SPEC_PREPAGO_PORTA%>" || form.hdnSpecificationIdJ.value=="<%=Constante.SPEC_POSTPAGO_PORTA%>"){
            if(form.cmbAction.value=="<%=Constante.ACTION_INBOX_AVANZAR%>"){
                if (!validateAddedItemQuantity(form.hdnParentName.value, form.txtEstadoOrden.value)) {
                    return;
   }
            }
        }

        // FBERNALES [PM0011240 Portabilidad]
        if (form.hdnSpecificationIdJ.value=="<%=Constante.SPEC_PREPAGO_PORTA%>" || form.hdnSpecificationIdJ.value=="<%=Constante.SPEC_POSTPAGO_PORTA%>"){            
            if (!fxValidateDocument(trim(form.txtDocumento.value),form.txtDocumento.value,form.cmbDocumento.value)) {
              return false;
            }
        }
        //JCASTILLO CPUF
        function fxSetCpuf() {
            if (form.hdnFlgCPUF.value ==<%=Constante.FLAG_SECTION_ACTIVE%>) {
   
                if($("#cpufResponse0").is(":checked")){
                    form.hdnCpufResponse.value=0;
   }
                if($("#cpufResponse1").is(":checked")){
                    form.hdnCpufResponse.value=1;
                }

            }
        }



        //JRIOS VIDD
        if(form.hdnFlgSDD.value==<%=Constante.FLAG_SECTION_ST_ACTIVE%> && form.hdnGenStatus.value==""){
            form.btnImprimirFormato.disabled = true;
            form.btnImprimirFormato2.disabled = true;
        }

        //JRIOS VIDD
        if(form.hdnFlgSDD.value==<%=Constante.FLAG_SECTION_ST_ACTIVE%>) {
            if(form.cmbSignature) {
            form.hdnSignatureType.value = form.cmbSignature.value;
            if(form.hdnSignatureType.value=='<%=Constante.SIGNATURE_TYPE_DIGITAL%>'){
                if(!fxValidateEmail(form.txtEmailDG)){
                    return false;
                }
            }
        }
        }

        if(form.hdnFlgSAC.value==<%=Constante.FLAG_SECTION_ST_ACTIVE%>){
            if(form.cmbSignature) {
            if(form.hdnChkAssignee.value==1){
                if(!fxValidateAssignee()){
                    return false;
                }
            }
        }
        }
        if(flagDigitalMessage==true){
        if(flagRepair==false&&form.hdnFlgDocument.value==1) {
            if((form.cmbAction.value=='<%=Constante.ACTION_INBOX_AVANZAR%>'||form.cmbAction.value=='<%=Constante.ACTION_INBOX_SALTAR%>')&&form.txtEstadoOrden.value=='<%=Constante.INBOX_TIENDA01%>'){
                if(form.hdnGenStatus.value=='<%=Constante.GENERATION_STATUS_INITIAL%>' && form.hdnTrxType.value!='<%=Constante.TRX_TYPE_PORTABILIDAD_ID%>')
                    var rpta=confirm("Asegurar el ingreso en la orden de toda la información requerida por el documento. Una vez generado el documento no se podrá volver a generar.\n ¿Desea generar los documentos digitales?");
                if(rpta==false){
                    flagDigitalMessage=true;
                    return false;
                }else{
                    flagDigitalMessage=false;
                }
            }
        }
        }
        
        //PRY - 0890 JBALCAZAR 
        var flagitemSA =false;
        var flagIsProrrateo = $("input[name='hdnIsApportionment']").val();        
        var boton_show_apportioment = $("input[name='hdnApportionment']").val();
        var flag_btnPA = $("#v_savePagoAnticipado").val();
        try{      	
          if(flagIsProrrateo==true || flagIsProrrateo=='true'){
      		if(boton_show_apportioment=="" || boton_show_apportioment=="undefined" || boton_show_apportioment==undefined){
      		  	 alert("Debe elegir si aplicará o no el pago anticipado de prorrateo.");	
      		  	 return false;
      		}else{
      	    	  if(boton_show_apportioment=="S"){
      	        	  $("#items_table tr td div input[name='txtMontoProrrateo']").each(function (index) {
      	          		if($(this).val()=='undefined' || $(this).val()==undefined || $(this).val()=="" || $(this).val()=='0.00' || $(this).val()==0.00 ){
      	          			flagitemSA=true;
      	          			return false;
      	          		}            		
      	          	});    		  
      	    	  }   			    		  	 
      		}
          	  
            if(flagitemSA && flag_btnPA == '0'){
      			alert("Debe calcular el pago Anticipado de Prorrateo para todos los Item antes de Grabar");        	       
            		return false;
            }else if(flagitemSA && flag_btnPA == '1'){
          	  	alert("Se realizaron modificaciones en los items calcular nuevamente los pagos anticipados de prorrateo");        	       
            		return false;    	  
            	}
          
      	}      	      	  
        }catch(e){}
        
   }

    if (flagRepair==false){
      <%System.out.println("en el if de JP_ORDER_EDIT_STAR_ShowPage.jsp cuando flagRepair = false");%>
        form.hdnLugarDespachoAux.value=form.cmbLugarAtencion.value;
        form.hdnVendedorAux.value=form.cmbVendedor.value;
        form.hdnTransportistaAux.value=form.cmbTransportista.value;
    }else{
        <%System.out.println("en el else de JP_ORDER_EDIT_START_ShowPage.jsp cuando flagRepair = true");%>
        form.hdnTiendaId.value=form.cmbTienda.value;
        form.hdnVendedorAux.value=form.cmbVendedor.value;
    }
    if( form.hdnRepresentanteCC != undefined )
        form.hdnRepresentanteCC.value = form.cmbRepresentanteCC.value;
    form.hdnFormaPagoAux.value=form.cmbFormaPago.value;
    form.myaction.value=myAction;
    try {
        form.hdnFlagMassive.value="N";
    } catch(exception){}

    /* Inicio Data */
    try{
        form.hdncmbVendedorData.value    = form.cmbVendedorData.options[form.cmbVendedorData.selectedIndex].text;
        form.hdnVendedorDataId.value    = form.cmbVendedorData.options[form.cmbVendedorData.selectedIndex].value;
    }catch(e){}
    /* Fin Data */

    if((form.hdnProposedId.value != null)&&(form.hdnProposedId.value!='')){
        if(form.cmbAction.value!='ANULAR'){ //siempre existe
            bconditionInbox=((form.txtEstadoOrden.value=='<%=Constante.INBOX_ADM_VENTAS%>')|| (form.txtEstadoOrden.value=='<%=Constante.INBOX_TIENDA01%>') || (form.txtEstadoOrden.value=='<%=Constante.INBOX_VENTAS%>'));
            if(bconditionInbox){
                vector =fxItemValidatePropuestas(vctItemsMainFrameOrder,form.hdnSubCategoria.value);
                vctItemPropAcum=fxGetResumItems(vector,form.hdnSubCategoria.value);
                cadena=fxGenerateTrama(vctItemPropAcum,form.hdnSubCategoria.value);
                //alert(cadena);
                form.hdnTrama.value=cadena;
                form.myaction.value="ValidationProposedOrder";
                form.action="<%=strURLOrderServlet%>";
                form.submit();
                return;
            }//end if bconditionInbox
        } //end if cmbAction
    }// end if hdnProposedId

    //EFLORES TDECONV003-3
	if (form.hdnSpecificationIdJ.value == <%= Constante.SPEC_POSTPAGO_VENTA %>) {
		
	     if(form.hdnFlagMigration != undefined){
			try {
				var chkFlagMigration = form.chkFlagMigration.checked;
				if (chkFlagMigration === true) {
					form.hdnFlagMigration.value = "S";
						
						var txtItemRatePlan = [];
						var txtItemModality = [];
						$("#items_table tr td div input[name='txtItemRatePlan']").each(function () {
							txtItemRatePlan.push($(this).val());
						});
						$("#items_table tr td div input[name='txtItemModality']").each(function () {
							txtItemModality.push($(this).val());
						});
						if (txtItemModality.length != 1) {
							alert("<%= msg_chk_migration_error %>");
							return false;
						}else if(txtItemModality[0] != "<%= Constante.MODALITY_PROPIO %>"){
							alert("<%= msg_chk_migration_error %>");
							return false;
						} else {
							if (txtItemModality[0] == "<%= Constante.MODALITY_PROPIO %>") {
								var rx = <%= plan_reg_exp_validation %>;
								if (rx.test(txtItemRatePlan[0])) {
									form.hdnFlagMigration.value = "C";
								}
							}
						}
						
				} else {
					form.hdnFlagMigration.value = "N";
				}
			}catch(e){
				form.hdnFlagMigration.value = "N";
			}
	     }
	}
    //FIN EFLORES TDECONV003-3
	
    if(fxInvocarCreditEvaluation()){

        //Se invoca antes de grabar la orden
        fxValidateApplyOrderVolume();
        form.btnUpdOrder.disabled = true;
        form.btnUpdOrderInbox.disabled = true;
        form.action="<%=strURLOrderServlet%>";
        form.submit();
    }

}

//AMATEOM PRY-1062
function validatePreEvaluationCMD(){
    var url_server = '<%=strURLOrderServlet%>';
    var wn_customerId = form.txtCompanyId.value;
    var params = 'myaction=validatePreevaluationCDM&customerId='+wn_customerId;
    var result = false;
    $.ajax({
        url: url_server,
        async: false,
        type: 'POST',
        dataType: 'json',
        data: params,
        success: function(data){
            if(data.strMessage != "" && data.strMessage != null){
                alert(data.strMessage);
            }else{
                result = true;
            }
        },
        error:function(xhr, ajaxOptions, thrownError){
            if (xhr.status === 0) {
                alert('Not connect: Verify Network.');
            } else if (xhr.status == 404) {
                alert('Requested page not found [404]');
            } else if (xhr.status == 500) {
                alert('Internal Server Error [500].');
            } else if (xhr.status === 'parsererror') {
                alert('Requested JSON parse failed.');
            } else if (xhr.status === 'timeout') {
                alert('Time out error.');
            } else if (xhr.status === 'abort') {
                alert('Ajax request aborted.');
            } else {
                alert('Uncaught Error: ' + xhr.responseText);
            }
        }
    });

    return result;
}

//JRIOS - VIDD
function fxValidateAssignee(){
    var form  = document.frmdatos;
    var v_cmbDocTypeAssignee = document.getElementById("cmbDocTypeAssignee");
    var flagDocType = false;
    var flagComplete = false;

    if(form.cmbDocTypeAssignee != null && form.txtDocNumAssignee != null ){
        if(form.cmbDocTypeAssignee.value != "" && form.txtDocNumAssignee.value != ""){
            form.hdnCmbDocTypeAssigneeText.value = v_cmbDocTypeAssignee.options[v_cmbDocTypeAssignee.selectedIndex].text;
            form.hdnCmbDocTypeAssigneeVal.value = v_cmbDocTypeAssignee.options[v_cmbDocTypeAssignee.selectedIndex].value;
            flagDocType = true;
        }
    }

    if( form.txtFirstNameAssignee != null && form.txtLastNameAssignee != null && form.txtFamilyNameAssignee != null){
        if( form.txtFirstNameAssignee.value != "" && form.txtLastNameAssignee.value != ""
                && form.txtFamilyNameAssignee != ""  && flagDocType){
            flagComplete = true;
        }
    }

    if(flagComplete){
        if( form.hdnCmbDocTypeAssigneeText.value == 'DNI' && (form.txtDocNumAssignee.value.length!=8 || !ContentOnlyNumber(form.txtDocNumAssignee.value)) ){
            alert("El DNI del Apoderado debe tener 8 digitos");
            return false;
        }

        if( form.hdnCmbDocTypeAssigneeText.value == 'CE' && form.txtDocNumAssignee.value.length!=12){
            alert("El Carnet de Extranjería del Apoderado debe tener 12 digitos");
            return false;
        }

        if( form.hdnCmbDocTypeAssigneeText.value == 'PAS' && form.txtDocNumAssignee.value.length!=12){
            alert("El Pasaporte del Apoderado debe tener 12 digitos");
            return false;
        }
    }else{
        alert("Debe Completar todos los campos de Apoderado");
        return false;
    }

    return true;
}

//EFLORES REQ-0204 07/01/2016 Validacion Preevaluacion
//JBALCAZAR PRY-1055  Se agrega modalidad de origen
function fxValidateUltimaPreevaluacion(){
    //EFLORES 29/12/2015 REQ- 0204 Validacion para ultima preevaluacion retail Postpago
    var form = document.frmdatos;
    var v1 = true;
    if (($.inArray(form.txtEstadoOrden.value,arrInboxPermit) != -1 ) && ($.inArray(form.cmbAction.value,arrActionPermit) != -1||form.cmbAction.value==''))
    {
        var url_server = '<%=strURLOrderServlet%>';
        var strNumeros = "";
        if($(".txtphonenumber").length) {
            strNumeros = $(".txtphonenumber").map(function () {
                var temp = "0";
                if (this.value != "") {
                    temp = this.value;
                }
                return temp;
            }).get().join();
        }
        //JBALCAZAR PRY-1055
        var strModalidad = "";       
        if($(".cmbmodalidadcont").length) {
            strModalidad = $(".cmbmodalidadcont").map(function () {
                var temp = "0";
                if (this.value != "") {
                    temp = this.value;
                }
                return temp;
            }).get().join();
        }        
                var params = "myaction=ValidateUltimaPreevaluacion&customerId="+form.txtCompanyId.value+"&cadenaNumeros="+strNumeros+"&cadenaModalidad="+strModalidad+"&userLogin="+form.hdnUserName.value+"&categoryId="+form.hdnSpecificationIdJ.value;
                $.ajax({
                    url: url_server,
                    data: params,
                    async: false,
                    type: "POST",
                    success:function(data){
                        var temporal = data.split("|");
                        if(temporal[0] == "0"){
                            alert(temporal[1]);
                            v1 = false;
                        }
                    },
                    error:function(data){
                        alert("Se ha producido un error "+data);
                    }
                });

    }
    return v1;
    //FIN EFLORES
}

/*Developer : Eduardo Peña EPV
 Objetivo  : Validar la cantidad de items registrados en la tabla
 */

function fxValidateItemVolume(){

    var cantidad=sumaCantidadesItemsTabla();
    var subcategoriaModel=form.hdnSubCategoria.value;
    var subcatResult;
    var url_server = '<%=strURLOrderServlet%>';
    var params = 'myaction=GetValueLimitModel&subcategoriaModel='+subcategoriaModel;
    var message, flag, value;
    var retorno=false;

    $.ajax({
        url: url_server,
        data: params,
        async: false,
        type: "POST",
        success:function(data){
            subcatResult=data.split(',');
            flag=subcatResult[0];
            value=subcatResult[1];
            message=subcatResult[2];

            if(value!='NULL'){
                if(cantidad>value){
                    if(flag=='1'){
                        retorno =false;
                        alert('Por favor revise la cantidad de modelos en la orden\n' +
                        'Cantidad máxima de modelos: '+value+' \n'+
                        'Cantidad Actual de Modelos ingresados: '+cantidad+' \n'+
                        'La orden no puede ser grabada.');

                    }else{

                        retorno=false;
                    }

                }else{
                    retorno=true;

                }
            }else{
                retorno=true;

            }
        },
        error:function(data){
            subcatResult=data.split(',');
            flag=subcatResult[0];
            value=subcatResult[1];
            message=subcatResult[2];
            retorno=false;
            alert('ERROR :'+message);

        }
    });
    return retorno;
}

/*Developer : Eduardo Peña EPV
 Objetivo  : Contar la cantidad de modelos registrados en la tabla
 */
function sumaCantidadesItemsTabla() {

    var product = [];
    var precioCta = [];
    var precioExc = [];
    var modalidad = [];

    $("#items_table tr td div input[name='txtItemProduct']").each(function () {
        product.push($(this).val());
    });

    $("#items_table tr td div input[name='txtItemPriceCtaInscrip']").each(function () {
        precioCta.push($(this).val());
    });

    $("#items_table tr td div input[name='txtItemPriceException']").each(function () {
        precioExc.push($(this).val());
    });

    $("#items_table tr td div input[name='txtItemModality']").each(function () {
        modalidad.push($(this).val());
    });


    var quantityCount = product.length;

    for (var i = 0; i < product.length; i++) {

        if(modalidad[i]=="Propio"){

            quantityCount--;

        }else {

            for (var j = i + 1; j < product.length; j++) {
                if (product[i] == product[j] && precioCta[i] == precioCta[j] && precioExc[i] == precioExc[j]) {
                    quantityCount--;
                    break;
                }
            }
        }
    }
    return quantityCount;
}

//INICIO PRY-0864 | AMENDEZ --Se adiciono la especificacion 2009
function fxInvocarCreditEvaluation(){
    var form = document.frmdatos;
    //EFLORES TDECONV003-3
    try{
        var campVar = form.hdnFlagMigration.value;
    }catch(e){
        campVar = "N";
    }
    if ((form.txtEstadoOrden.value == '<%=Constante.INBOX_TIENDA01%>'   || form.txtEstadoOrden.value == '<%=Constante.INBOX_CALLCENTER%>' || form.txtEstadoOrden.value == '<%=Constante.INBOX_VENTAS%>') && ((form.hdnSubCategoria.value == '2001' && campVar != "C") || form.hdnSubCategoria.value == '2068' || form.hdnSubCategoria.value == '2013' || form.hdnSubCategoria.value == '2017' || form.hdnSubCategoria.value == '2065' || form.hdnSubCategoria.value == '<%=Constante.SPEC_SSAA_SUSCRIPCIONES%>' || form.hdnSubCategoria.value == '<%=Constante.SPEC_SSAA_PROMOTIONS%>' || form.hdnSubCategoria.value == '2024'  ) && (form.cmbAction.value=='<%=Constante.ACTION_INBOX_AVANZAR%>'||form.cmbAction.value=='')) {
        form.myaction.value="evaluarOrden";
        form.action="<%=strURLOrderServlet%>";
        form.submit();
        return true;
    }
    return true;
}
//FIN PRY-0864 | AMENDEZ --Se adiciono la especificacion 2009

/*  function fxValidateCreditEvaluation(){
 var flagGrabar = false;
 form = document.frmdatos;
 form.myaction.value="updateOrden";
 if (form.hdnValidateCredit.value == '1') {
 if(Section_EvaluacionAutomatica.style.display == 'none'){
 //alert("No existe deposito en garantia");
 flagGrabar = true;
 }else{
 if(form.flagActionCredit.value=='' && form.cmbAction.value=='<%=Constante.ACTION_INBOX_AVANZAR%>'){
 form.flagActionCredit.value = 'ACEPTAR';
 }

 if (form.flagActionCredit.value==''){
 parent.bottomFrame.location.replace("<%=Constante.PATH_APPORDER_SERVER%>/Bottom.html");
 return;
 } else {
 if(form.flagActionCredit.value=='ACEPTAR'){
 if (form.txtGuarantee.value > 0){
 if (confirm("¿Acepta el Deposito en Garantía de: "+form.txtGuarantee.value+"?")){
 flagGrabar = true;
 } else {
 parent.bottomFrame.location.replace("<%=Constante.PATH_APPORDER_SERVER%>/Bottom.html");
 return;
 }
 } else {
 flagGrabar = true;
 }
 }else{
 if (form.txtGuarantee.value > 0 || rule_result.rows.length > 1){
 flagGrabar = true;
 }else{
 alert("No se puede Rechazar una Orden sin Deposito de Garantia y sin Restricciones");
 parent.bottomFrame.location.replace("<%=Constante.PATH_APPORDER_SERVER%>/Bottom.html");
 return;
 }

 }
 }
 }
 } else {
 flagGrabar = true;
 }
 if(flagGrabar){

 //Se invoca antes de grabar la orden
 fxValidateApplyOrderVolume();

 form.btnUpdOrder.disabled = true;
 form.action="<%=strURLOrderServlet%>";
 form.submit();
 }
 }*/


function fxValidateCreditEvaluation(){
    var flagGrabar = false;
    form = document.frmdatos;
    form.myaction.value="updateOrden";
    if (form.hdnValidateCredit.value == '1') {
        if(Section_EvaluacionAutomatica.style.display == 'none'){
            //alert("No existe deposito en garantia");
            flagGrabar = true;
        }else{
            flagGrabar = true;
        }
    } else {
        flagGrabar = true;
    }

    if(flagGrabar){
        //Se invoca antes de grabar la orden
        fxValidateApplyOrderVolume();

        form.btnUpdOrder.disabled = true;
        form.action="<%=strURLOrderServlet%>";
        form.submit();
    }
}

function fxValidateOrderVolume(){
    form = document.frmdatos;

    if(form.hdnSubCategoria.value == <%=Constante.SPEC_POSTPAGO_VENTA%> || form.hdnSubCategoria.value == <%=Constante.SPEC_PORTABILIDAD_POSTPAGO%>){
        if(aplicoVO == "2" || aplicoVO == "4"){
            alert("Volver a Evaluar Promocion por Volumen de Orden");
            return false;
        }
        return true;
    }
    else{
        return true;
    }
}

function fxValidateApplyOrderVolume(){
    form = document.frmdatos;

    if(form.hdnSubCategoria.value == <%=Constante.SPEC_POSTPAGO_VENTA%> || form.hdnSubCategoria.value == <%=Constante.SPEC_PORTABILIDAD_POSTPAGO%>){

        if(aplicoVO == "1" && fxIsOrderVolumeChecked()){
            if(confirm("¿Está seguro que desea aplicar promoción por volumen de orden?")){
                fxApplyOrderVolume();
            }
        }
    }
}

function fxDoSubmitMassive(myAction){
    form = document.frmdatos;
    var flagRepair = fxContentInArray(form.hdnSubCategoria.value);

    if(myAction=='updateOrden'){
        //Confirmacion antes de anular la orden
        if (flagRepair==false){
            if (form.cmbAction.value=="<%=Constante.ACTION_INBOX_ANULAR%>"){
                if (!confirm("Está seguro de anular la orden? Tener presente que la orden no podra ser reabierta")) {
                    return false;
                }
            }
        }

        if (fxValidateAll()==false)
            return false;
    }

    if (flagRepair==false){
        <%System.out.println("Cuando es Masivo en el if de JP_ORDER_EDIT_START_ShowPage.jsp cuando flagRepair = false");%>
        form.hdnLugarDespachoAux.value=form.cmbLugarAtencion.value;
        form.hdnVendedorAux.value=form.cmbVendedor.value;
        form.hdnTransportistaAux.value=form.cmbTransportista.value;
    }else{
        <%System.out.println("Cuando es Masivo en el else de JP_ORDER_EDIT_START_ShowPage.jsp cuando flagRepair = true");%>
        form.hdnTiendaId.value=form.cmbTienda.value;
    }
    if( form.hdnRepresentanteCC != undefined )
        form.hdnRepresentanteCC.value = form.cmbRepresentanteCC.value;
    form.hdnFormaPagoAux.value=form.cmbFormaPago.value;
    form.myaction.value=myAction;
    try {
        form.hdnFlagMassive.value="S";
    } catch(exception){}
    form.btnUpdOrder.disabled = true;
    form.btnUpdOrderInbox.disabled = true;
    form.action="<%=strURLOrderServlet%>";
    alert("myAction:"+myAction);
    form.submit();
}

function fxValidateAll(){
    form = document.frmdatos;

    //INICIO DERAZO REQ-0940
    if(form.txtContactEmail != null && form.txtContactPhoneNumber != null && form.txtContactFirstName != null && form.txtContactLastName != null &&
            form.cmbContactDocumentType != null && form.txtContactDocumentNumber != null) {

        //Validaciones Genericas
    if( form.cmbContactDocumentType.value == 'DNI' && (form.txtContactDocumentNumber.value.length!=8 || !ContentOnlyNumber(form.txtContactDocumentNumber.value)) ){
        alert("El DNI del contacto debe tener 8 digitos");
        form.txtContactDocumentNumber.focus();
        return false;
    }

    if( form.cmbContactDocumentType.value == 'RUC'){
        alert("El tipo de documento RUC no está permitido para datos de contacto.");
        form.cmbContactDocumentType.focus();
                            return false;
                        }

        //Validaciones para el email
        if (form.txtContactEmail.value != "") {

            var regExpNotAllowWhiteSpaces = /^\S*$/;
            if (!regExpNotAllowWhiteSpaces.test(form.txtContactEmail.value)) {
                alert("El numero celular no puede contener espacios en blanco");
                form.txtContactEmail.focus();
                return false;
            }

            var regExpEmail = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
            if (!regExpEmail.test(form.txtContactEmail.value)) {
                alert("El formato del e-mail del Contacto debe ser: nombre_usuario@dominio.identificador");
                form.txtContactEmail.focus();
                return false;
            }
        }

        //Validaciones para el numero celular
        if (form.txtContactPhoneNumber.value != "") {
            var regExpNotAllowWhiteSpaces = /^\S*$/;
            if (!regExpNotAllowWhiteSpaces.test(form.txtContactPhoneNumber.value)) {
                alert("El numero celular no puede contener espacios en blanco");
                form.txtContactPhoneNumber.focus();
                return false;
            }

            var regExpOnlyNumber = /^[0-9]+$/;
            if (!regExpOnlyNumber.test(form.txtContactPhoneNumber.value)) {
                alert("El numero celular del Contacto solo debe contener números");
                form.txtContactPhoneNumber.focus();
                return false;
            }

            if (form.txtContactPhoneNumber.value.length != 9) {
                alert("El numero celular del Contacto debe contener 9 digitos");
                form.txtContactPhoneNumber.focus();
                return false;
            }
        }

        //Validaciones Trazabilidad
        var flagTraceability = parent.mainFrame.flagTraceabilityFunct;
        var traceabilityValidation = parent.mainFrame.showSectionContacts;

        //Si esta activo el flag, realizamos las validaciones y si la orden cumplio con las validaciones de Trazabilidad
        if (flagTraceability != null && flagTraceability == 1 &&
                traceabilityValidation != null && traceabilityValidation == 1) {

            //Validamos que haya ingresado todos los datos del contacto
            if(!(form.txtContactFirstName.value != "" && form.txtContactLastName.value != "" && form.cmbContactDocumentType.value != "" && form.txtContactDocumentNumber.value != "")){
                alert("Debe completar todos los campos del Contacto, para el numero celular y el e-mail debe ingresar uno de ellos");
                return false;
            }

            //Validamos que haya ingresado por lo menos el e-mail o numero celular
            if (form.txtContactEmail.value == "" && form.txtContactPhoneNumber.value == "") {
                alert("Debe ingresar el e-mail o el numero celular del Contacto");
                form.txtContactPhoneNumber.focus();
                return false;
            }
        }
        else{
            //Realizamos las validaciones anteriores
            if( form.txtContactFirstName.value != "" || form.txtContactLastName.value != "" || form.cmbContactDocumentType.value != "" || form.txtContactDocumentNumber.value != ""){
                if( !(form.txtContactFirstName.value != "" && form.txtContactLastName.value != "" && form.cmbContactDocumentType.value != "" && form.txtContactDocumentNumber.value != "")){
                    alert("Completar los demás campos del Contacto, el numero celular y el email son opcionales"); //DERAZO REQ-0940
                    return false;
                }
            }
        }
    }

    var chkContactNotification = form.chkContactNotification.checked;
    if(chkContactNotification === true) {
        form.hdnContacNotification.value = "1";
    }
    else {
        form.hdnContacNotification.value = "0";
    }
    //FIN DERAZO

    try{
        if (form.txtDirEntrega != null){
            if (form.txtDirEntrega.value == "") {
                if ( !fxInDirEntregaArr(form.hdnSpecificationIdJ.value) ) {   //JLINARES: Direccion en Portabilidad
                    alert("Debe seleccionar una Dirección de Entrega");
                    return false;
                }
            }
        }
    }catch(e){}

    try{
        if( form.cmbFormaPago.value == "" ){
            alert("Debe seleccionar Forma de Pago");
            form.cmbFormaPago.focus();
            return false;
        }
    }catch(e){}

    fxExistsFieldsBA();
    var flagRepair = fxContentInArray(form.hdnSubCategoria.value);
    //alert("flagRepair-->"+flagRepair);
    if (flagRepair==false){
        if (fxValidateSave()==false)
            return false;

        /** HTENORIO COR1108**/
        /** Comments: Aca va la validacion de fecha de programacion automatica**/
        //alert("form.txtFechaProceso.value = "+form.txtFechaProceso.value + ", form.hdnFechaProcesoAux.value = "+form.hdnFechaProcesoAux.value);
        try{
            if (form.txtFechaProceso.value!=""){

                if (form.txtFechaProceso.value != form.hdnFechaProcesoAux.value) {
                    var fechaServ = new Date(
                            "<%=(GregorianCalendar.getInstance()).get(Calendar.YEAR)%>",
                            "<%=(GregorianCalendar.getInstance()).get(Calendar.MONTH)%>",
                            "<%=(GregorianCalendar.getInstance()).get(Calendar.DAY_OF_MONTH)%>");
                    //alert("fechaServ = "+fechaServ);
                    if (fxCheckServerDate(form.txtFechaProceso,fechaServ)!=true)
                        return false;
                };
            };
        }catch(e){}
    }

    if(fxValidateSectionsforSaving()==false) //secciones dinamicas
        return false;

    if (flagRepair==false){
        if (form.txtEstadoOrden.value==TYPE_CERRADO){
            if(fxValidateSectFinalStatByObjType()==false)
                return false;
        }

        if (form.cmbAction.value=="<%=Constante.ACTION_INBOX_BACK%>" && form.cmbInboxBack.value==""){
            alert("Seleccione el inbox a retroceder");
            return false;
        }

        if (form.hdnSubCategoria.value=="<%=Constante.SPEC_ACCESO_INTERNET%>" && form.txtEstadoOrden.value=="<%=Constante.INBOX_INSTALACION_ING%>"  &&
                form.cmbAction.value=="<%=Constante.ACTION_INBOX_AVANZAR%>" && form.hdnOrderPassForInventory.value != "null" ){

            alert(form.hdnOrderPassForInventory.value);
            return false;
        }
    }
    return true;
}

//JLINARES: Direccion en Portabilidad 
function fxInDirEntregaArr(value){
    for(var i=0; i < SPEC_PORT_DIR_ENTREGA.length ;i++){
        if( value == SPEC_PORT_DIR_ENTREGA[i]){
            return true;
        }
    }
    return false;
}


//Rechazos
function fxEditReject(){
    var Form = document.frmdatos;
    if (Form.cmbLugarAtencion.options[Form.cmbLugarAtencion.selectedIndex].text!="" && Form.cmbLugarAtencion.selectedIndex !=-1){
        var v_parametros = "?hdnOrderId="+   g_orderid
                +"&txtEstadoOrden="+  Form.txtEstadoOrden.value
                +"&WorkFlowType="+   Form.hdnWorkflowType.value
                +"&specialtyid="+     Form.hdnSubCategoria.value
                +"&hdnRejectPermit=E"
                +"&hdnSessionId=<%=strSessionId%>";
        window.open("<%=strURLReject%>"+v_parametros, "Orden_Rechazos","status=yes, location=0, scrollbars=yes, width=800, height=500, left=50, top=100, screenX=50, screenY=100");

    }else{
        alert("Seleccione el Lugar de Atención");
        Form.cmbLugarAtencion.focus();
        return;
    }
}

function fxDetailReject(){
    var Form = document.frmdatos;

    var v_parametros = "?hdnOrderId="+   g_orderid
            +"&txtEstadoOrden="+  Form.txtEstadoOrden.value
            +"&WorkFlowType="+   Form.hdnWorkflowType.value
            +"&specialtyid="+     Form.hdnSubCategoria.value
            +"&hdnRejectPermit=D"
            +"&hdnSessionId=<%=strSessionId%>";
    window.open("<%=strURLReject%>"+v_parametros, "Orden_Rechazos","status=yes, location=0, scrollbars=yes, width=800, height=500, left=50, top=100, screenX=50, screenY=100");

}


//Fin Rechazos

//Inicio Pago
function editPayment10(){
    var form = document.frmdatos;
    var v_parametros = "?hdnPagoBanco="     + escape(form.hdnPagoBanco.value)
            +"&hdnPagoNroVoucher="     + escape(form.hdnPagoNroVoucher.value)
            +"&hdnPagoImporte="        + escape(form.hdnPagoImporte.value)
            +"&hdnPagoFecha="          + escape(form.hdnPagoFecha.value)
            +"&hdnPagoDisponible="     + escape(form.hdnPagoDisponible.value)
            +"&hdnRuc="                + escape(form.txtRucDisabled.value)
            +"&txtImporteFactura="     + escape(form.txtImporteFactura.value)
            +"&hdnTotalPaymentOrig="   + escape(form.hdnTotalPaymentOrig.value);
    var popupWin = window.open("<%=strURLPayment%>", "Orden_Pagos","status=yes, location=0, width=450, height=500, left=50, top=100, screenX=50, screenY=100");

}

function fxEditPayment(){
    var form = document.frmdatos;
    var v_parametros = "?sUrl=<%=strURLPayment%>"
            +"¿hdnPagoBanco="     + escape(form.hdnPagoBanco.value)
            +"|hdnPagoNroVoucher="     + escape(form.hdnPagoNroVoucher.value)
            +"|hdnPagoImporte="        + escape(form.hdnPagoImporte.value)
            +"|hdnPagoFecha="          + escape(form.hdnPagoFecha.value)
            +"|hdnPagoDisponible="     + escape(form.hdnPagoDisponible.value)
            +"|hdnRuc="                + escape(form.txtRucDisabled.value)
            +"|txtImporteFactura="     + escape(form.txtImporteFactura.value)
            +"|hdnTotalPaymentOrig="   + escape(form.hdnTotalPaymentOrig.value);
    var v_Url=   "<%=strRutaContext%>/GENERALPAGE/GeneralFrame.jsp" +v_parametros;
    var popupWin = window.open(v_Url, "Orden_Pagos","status=yes, location=0, width=450, height=500, left=50, top=100, screenX=50, screenY=100");

}
//Fin Pago

function fxSelectSigEstado(objThis){
    form = document.frmdatos;
    var subjObj = null;
    var txt_workflow;
    var txt_vtype;
    //    var table = document.all ? document.all["table_imeis"]:document.getElementById("table_imeis");


    /* if (table.rows.length > 2){
     for(i=0 ; i< (table.rows.length - 1) ; i++){
     if (form.item_imei_imei[i].value != ""){
     alert("No puede cambiar el lugar de despacho si ya escaneo algun SIM/IMEI");
     form.cmbLugarAtencion.value = form.hdnLugarDespacho.value;
     return; };
     };
     };
     else{
     if (table.rows.length == 2){
     if (form.item_imei_imei.value != ""){
     alert("No puede cambiar el lugar de despacho si ya escaneo algun SIM/IMEI");
     form.cmbLugarAtencion.value = form.hdnLugarDespacho.value;
     return; };
     }
     };*/

    for (i=1; i < workflowArr.length ; i++) {
        subjObj = workflowArr[i];
        if (subjObj.atentionid == objThis.value) {
            txt_workflow = subjObj.workflow;
            txt_vtype    = subjObj.vtype;
        };
    };


    if (form.hdnTotalPaymentOrig.value > 0 && form.hdnLugarDespachoTypeOrig.value == "Fisica" && txt_vtype == "Fisica"){
        alert("Ya existe un pago asociado al lugar de despacho actual y por lo tanto no puede ser cambiado por otra tienda");
        form.cmbLugarAtencion.value = form.hdnLugarDespacho.value;
        return;
    };


    if (form.hdnTotalPaymentOrig.value > 0 && form.hdnLugarDespachoTypeOrig.value == "Fisica" && txt_vtype == "Fulfillment"){
        alert("Ya existe un pago asociado al lugar de despacho actual y por lo tanto al grabar la orden dicho pago se marcará para ser transferido");
        form.cmbLugarAtencion.value = form.hdnLugarDespacho.value;
        return;
    };

    if (form.hdnTotalPaymentOrig.value > 0 && form.hdnLugarDespachoTypeOrig.value == "Fulfillment" && txt_vtype == "Fisica"){
        alert("Ya existe un pago asociado al lugar de despacho actual y por lo tanto no puede ser cambiado");
        form.cmbLugarAtencion.value = form.hdnLugarDespacho.value;
        return;
    };

    if (form.cmbLugarAtencion.value == 41){
        form.chkCourier.checked = false;
        form.chkCourier.disabled = true;
        form.hdnChkCourier.value = "0";
    }else{
        form.chkCourier.disabled = false;
    };

    if (txt_vtype =="Fisica" && form.cmbFormaPago.value == "Diferido"){
        form.cmbFormaPago.value = "";
        form.cmbLugarAtencion.value = form.hdnLugarDespacho.value;
        return;
    };

    form.hdnLugarDespachoType.value = txt_vtype;
    form.hdnWorkflowType.value= txt_workflow;

    form.hdnLugarDespacho.value = form.cmbLugarAtencion.value;
};

function fxChangeSalesMan(objThis,iUserId,iAppId){
    form = document.frmdatos;
    var txtCustomerId   = form.txtCompanyId.value;
    var txtSite         = form.txtSiteId.value;
    var txtSessionId    = form.hdnSessionId.value;
    var txtUnknwnSiteId  = "";
    try {
        txtUnknwnSiteId  = form.hdnUnkwnSiteId.value;
    }catch (e) {
        txtUnknwnSiteId  = 0;
    }
    var txtVendedorId   = objThis.options[objThis.selectedIndex].value;
    var txtVendedor       = objThis.options[objThis.selectedIndex].text;
    var txtIdspecialty  = form.hdnSubCategoria.value;
    var txtCustAlcanceExclusividad  = form.hdnCustAlcanceExclusividad.value;//JGALINDO
    if (form.hdnOrderCreator.value == txtVendedorId) {
        return;
    }
    if (txtVendedor != ""){
        var myaction ='ValidateSalesMan';
        //url = "/portal/pls/portal/!WEBCCARE.NPAC_ORDER_PL_PKG.PL_CHANGE_SALESMAN?an_swcustomerid="+txtCustomerId+"&an_swsiteid="+txtSite+"&an_vendedorid="+txtVendedorId+"&av_vendedorname="+txtVendedor+"&an_swspecialtyid="+txtIdspecialty+"&an_alcExclusivity="+txtCustAlcanceExclusividad+"&an_unknwnSiteId="+txtUnknwnSiteId;
        url = "<%=strURLOrderServlet%>"+"?an_swcustomerid="+txtCustomerId+"&an_swsiteid="+txtSite+"&an_vendedorid="+txtVendedorId+"&av_vendedorname="+txtVendedor+"&an_swspecialtyid="+txtIdspecialty+"&an_alcExclusivity="+txtCustAlcanceExclusividad+"&an_unknwnSiteId="+txtUnknwnSiteId+"&myaction="+myaction+"&hdnSessionId="+txtSessionId+"&iUserId="+iUserId+"&iAppId="+iAppId;
        parent.bottomFrame.location.replace(url);
    }
}

function fxShowAdress() {
    var form = document.frmdatos;
    var customerid = form.txtCompanyId.value;
    var param;

    if (customerid != "") {
        if (form.txtSiteId.value != "" && form.txtSiteId.value != "0")
            param="?strObjectType=<%=Constante.CUSTOMERTYPE_SITE%>&intObjectId="+form.txtSiteId.value+"&generatorType=INC";
        else
            param="?strObjectType=<%=Constante.CUSTOMERTYPE_CUSTOMER%>&intObjectId="+customerid+"&generatorType=INC";
        //}
        var url="<%=strRutaContext%>/htdocs/jsp/CustomerAddressDelivery.jsp"+param;
        WinAsist = window.open(url,"WinDireccion","toolbar=no,location=no,directories=no,status=no,menubar=0,scrollbars=no,resizable=yes,screenX=100,top=80,left=100,screenY=80,width=520,height=300,modal=yes");
    } else
        alert("Debe seleccionar una compañía");
}

function fxGenerarComprobantes(){
    form = document.frmdatos;
    if (form.hdnChangedOrder.value=="S"){
        //if( confirm("La orden ha sido modificada y no ha sido grabada. Desea generar los comprobantes?") ) {
        alert("La orden ha sido modificada, primero deberá grabarla y luego generar los comprobantes");
        try{
            form.cmbAction.value="";
        }catch(e) {}
        return;
    }else{
        form.myaction.value='generateDocument';
        form.action="<%=strURLOrderServlet%>";
        form.btnGenerarDocumentos.disabled=true;
        form.submit();
    }
}

function fxGenerarOrdenPago(){
    form = document.frmdatos;
    if (form.hdnChangedOrder.value=="S"){
        if( confirm("La orden ha sido modificada y no ha sido grabada. ¿Desea generar la Orden de Pago?") ) {
            form.myaction.value='generatePayOrder';
            form.action="<%=strURLOrderServlet%>";
            form.submit();
        }
    }else{
        form.myaction.value='generatePayOrder';
        form.action="<%=strURLOrderServlet%>";
        form.submit();
    }
}

function fxAutorizacionDevolucion(){
    form = document.frmdatos;
    if (form.hdnChangedOrder.value=="S"){
        if( confirm("La orden ha sido modificada y no ha sido grabada. ¿Desea generar la Devolución?") ) {
            form.myaction.value='autorizacionDevolucion';
            form.action="<%=strURLOrderServlet%>";
            form.submit();
        }
    }
    else{
        form.myaction.value='autorizacionDevolucion';
        form.action="<%=strURLOrderServlet%>";
        form.submit();
    }
}

var strType;

function fxPartedeIngreso(strType){
    form = document.frmdatos;
    /********09/02/2011****INICIO EXTERNO.JNINO********************/
    /********30/12/2010****INICIO EXTERNO.JNINO********************/
    //Validacion trasladada al Servlet de ParteIngreso
    /********30/12/2010****FIN EXTERNO.JNINO**********************/
    /********09/02/2011****FIN EXTERNO.JNINO********************/
    if (form.hdnChangedOrder.value=="S"){
        alert("La orden ha sido modificada, primero deberá grabarla y luego generar el Parte de Ingreso");
        try{
            form.cmbAction.value="";
        }catch(e) {}
        return;
    }else{
        form.hdnPIType.value = strType;
        form.myaction.value='parteIngreso';
        form.action="<%=strURLOrderServlet%>";
        if (strType=='DEV'){
            form.btnParteIngreso.disabled=true;
        }
        else{
            if (strType=='BAD'){
                form.btnParteIngresoBadImei.disabled=true;
            }else{
                if (strType=='BADADDRESS'){
                    form.btnGenerarPI.disabled=true;
                }
            }
        }
        form.submit();
    }
}

function fxCambiarInbox(valor,specId){
    //alert(" valor-->"+valor+" specId-->"+specId);
    var Form = document.frmdatos;
    if (valor=='RETROCEDER'){
        idRegionInboxBack.style.display='';
        idRegionInboxNext.style.display='none';
        Form.cmbInboxBack.value="";
    }else if (valor=='SALTAR'){
        idRegionInboxBack.style.display='none';
        idRegionInboxNext.style.display='';
        DeleteSelectOptions(Form.cmbInboxNext);
        Form.myaction.value="loadInboxSaltar";
        var url = "<%=strURLOrderServlet%>"+"?strComboName=cmbInboxNext&strFormName=frmdatos&strValorName=inbox&strDescripcionName=inbox";
        Form.action=url;
        Form.submit();
    }else{
        DeleteSelectOptions(Form.cmbInboxNext);
        idRegionInboxBack.style.display='none';
        idRegionInboxNext.style.display='none';
    }
    try{
        fxhideReasonRejection();
    }catch(e){}
}

function fxExistsFieldsBA(){
    var form = document.frmdatos;
    var cadena="";
    if (parseInt(form.hdnSubCategoria.value)=="<%=Constante.SPEC_CAMBIAR_ESTRUCT_CUENTA%>"){
        //alert("Es cambio de estructura de cuenta");
        for (i=0;i<form.txtOrigFactura.length;i++){
            if (i==0)
                cadena=cadena+"-"+",";
            else
            if (form.txtOrigFactura[i].value=="" || form.txtOrigFactura[i].value==null || form.txtOrigFactura[i].value=="null" )
                cadena=cadena+"-"+",";
            else
                cadena=cadena+form.txtOrigFactura[i].value+",";
        }
        cadena=cadena.substring(0,cadena.length-1);
        form.hdnOrigFactura.value=cadena;

        //NewResponsable de Pago
        cadena="";
        for (i=0;i<form.cmbNewResponsablePago.length;i++){
            if (i==0)
                cadena=cadena+"-"+",";
            else
            if (form.cmbNewResponsablePago[i].value=="" || form.cmbNewResponsablePago[i].value==null || form.cmbNewResponsablePago[i].value=="null" )
                cadena=cadena+"-"+",";
            else
                cadena=cadena+form.cmbNewResponsablePago[i].value+",";
        }
        cadena=cadena.substring(0,cadena.length-1);
        form.hdnNewResponsablePago.value=cadena;

        //NewFactura
        cadena="";
        for (i=0;i<form.cmbNewFactura.length;i++){
            if (i==0)
                cadena=cadena+"-"+",";
            else
            if (form.cmbNewFactura[i].value=="" || form.cmbNewFactura[i].value==null || form.cmbNewFactura[i].value=="null" )
                cadena=cadena+"-"+",";
            else
                cadena=cadena+form.cmbNewFactura[i].value+",";
        }
        cadena=cadena.substring(0,cadena.length-1);
        form.hdnNewFactura.value=cadena;
        //alert(form.hdnOrigFactura.value);
    }
}


//CPUENTE6
function fxRequestXML2(servlet,funcion, callMethod, params){

    var url = "<%=request.getContextPath()%>/"+servlet+"?"+ callMethod + "=" + funcion + "&"+params;

    var msxml = new ActiveXObject("msxml2.XMLHTTP");

    msxml.Open("GET", url, false);
    msxml.Send("");
    var ret = msxml.responseText;

    return ret;
}

function fxRequestXML(servlet,funcion, params){

    var url = "<%=request.getContextPath()%>/"+servlet+"?metodo=" + funcion + "&"+params;

    var msxml = new ActiveXObject("msxml2.XMLHTTP");

    msxml.Open("GET", url, false);
    msxml.Send("");
    var ret = msxml.responseText;
    if(ret.indexOf("OK")!=0){
        return null;
    }
    return ret.substring(2,ret.length);
}

//Agrego function de validacion evento click deshabilite button
function fxDisabledReplacedHandset(appCode){
    var form = document.frmdatos;
    var codAplic = appCode;
    var strOrden = form.hdnOrderId.value;
    var strUser  = form.hdnUserName.value;
    var strSpecificationId = form.hdnSubCategoria.value;//CPUENTE6

    form.btnReplaceHandset.disabled="true";
    form.myaction.value='replaceHandset';
    var url = "<%=strURLOrderServlet%>"+"?strOrderId="+strOrden+"&strUserName="+strUser+"&appCode="+codAplic+"&strSpecificationId="+strSpecificationId;
    form.action = url;
    form.submit();

}

function MuestraFechaReconexBySpecification(specificationId){//Agregado por rmartinez 19-06-2009
    form = document.frmdatos;

    //Se agrego para suspensiones temporales rmartinez
    if(specificationId == <%=Constante.SPEC_SUSPENSIONES[0]%>){
        dvFecReconexEdit.style.display="";
        dvFecReconexEditInput.style.display="";
    }else{
        dvFecReconexEdit.style.display="none";
        dvFecReconexEditInput.style.display="none";
    }
}
// INICIO - VALIDACION DE PROPUESTAS  CBARZOLA: 07/08/2009
function fxValidaciondePropuestas(display){
    if(display!='' && display!=null && display!='null'){
        alert(display);
        return;
    }
    else{
        if(fxInvocarCreditEvaluation()){

            //Se invoca antes de grabar la orden
            fxValidateApplyOrderVolume();

            form.myaction.value='updateOrden';
            form.btnUpdOrder.disabled = true;
            form.action="<%=strURLOrderServlet%>";
            form.submit();
        }
    }
}
function fxGenerateTrama(vctItemPropAcum,spefification)
{
    cadena='';
    for(i=0;i<vctItemPropAcum.size();i++){
        switch(spefification)
        {
            case '<%=Constante.SPEC_POSTPAGO_VENTA%>'://VENTAS MOVILES
                if(i==0)
                { cadena=cadena+vctItemPropAcum.elementAt(i).flag +
                        ','+vctItemPropAcum.elementAt(i).objtypenew+
                        ','+vctItemPropAcum.elementAt(i).objidnew+
                        ','+vctItemPropAcum.elementAt(i).cantidad + ';' ;
                }
                else
                {
                    cadena=cadena+vctItemPropAcum.elementAt(i).flag+
                            ','+vctItemPropAcum.elementAt(i).objtypenew+
                            ','+vctItemPropAcum.elementAt(i).objidnew+
                            ','+vctItemPropAcum.elementAt(i).cantidad + ';' ;
                }
                break;
            case '<%=Constante.SPEC_CAMBIAR_PLAN_TARIFARIO%>': //SA-CAMBIO DE PLAN T.
                if(i==0)
                {cadena=cadena+vctItemPropAcum.elementAt(i).flag+
                        ','+vctItemPropAcum.elementAt(i).objtypenew+
                        ','+vctItemPropAcum.elementAt(i).objidold+
                        ','+vctItemPropAcum.elementAt(i).objidnew+
                        ','+vctItemPropAcum.elementAt(i).cantidad+ ';' ;
                }
                else
                {
                    cadena=cadena+vctItemPropAcum.elementAt(i).flag+
                            ','+vctItemPropAcum.elementAt(i).objtypenew+
                            ','+vctItemPropAcum.elementAt(i).objidold+
                            ','+vctItemPropAcum.elementAt(i).objidnew+
                            ','+vctItemPropAcum.elementAt(i).cantidad+';' ;
                }
                break;
            case '<%=Constante.SPEC_BOLSA_CREACION%>': //BOLSA -CREACION
                if(i==0)
                {cadena=cadena+vctItemPropAcum.elementAt(i).flag+
                        ','+vctItemPropAcum.elementAt(i).objtypenew+
                        ','+vctItemPropAcum.elementAt(i).objidnew+
                        ','+vctItemPropAcum.elementAt(i).cantidad+';' ;
                }
                else
                {
                    cadena=cadena+vctItemPropAcum.elementAt(i).flag+
                            ','+vctItemPropAcum.elementAt(i).objtypenew+
                            ','+vctItemPropAcum.elementAt(i).objidnew+
                            ','+vctItemPropAcum.elementAt(i).cantidad+';' ;
                }
                break;
            case '<%=Constante.SPEC_BOLSA_CAMBIO%>': //BOLSA - CAMBIO
                if(i==0)
                {cadena=cadena+vctItemPropAcum.elementAt(i).flag+
                        ','+vctItemPropAcum.elementAt(i).objtypenew+
                        ','+vctItemPropAcum.elementAt(i).objidold+
                        ','+vctItemPropAcum.elementAt(i).objidnew+
                        ','+vctItemPropAcum.elementAt(i).cantidad+';' ;}
                else
                {
                    cadena=cadena+vctItemPropAcum.elementAt(i).flag+
                            ','+vctItemPropAcum.elementAt(i).objtypenew+
                            ','+vctItemPropAcum.elementAt(i).objidold+
                            ','+vctItemPropAcum.elementAt(i).objidnew+
                            ','+vctItemPropAcum.elementAt(i).cantidad+';' ;
                }
                break;
            case '<%=Constante.SPEC_ACTIVAR_DESACTIVAR_SERVICIOS%>': //SA- ACTIV/DES
                if(i==0){
                    cadena=cadena+vctItemPropAcum.elementAt(i).flag+
                            ','+vctItemPropAcum.elementAt(i).objtypenew+
                            ','+vctItemPropAcum.elementAt(i).objidnew+
                            ','+vctItemPropAcum.elementAt(i).cantidad+';' ;
                }
                else{
                    cadena=cadena+vctItemPropAcum.elementAt(i).flag+
                            ','+vctItemPropAcum.elementAt(i).objtypenew+
                            ','+vctItemPropAcum.elementAt(i).objidnew+
                            ','+vctItemPropAcum.elementAt(i).cantidad+';' ;
                }
                break;
        }
    }
    return cadena;
}
function fxGetResumItems(vctItemProposed,specification)
{
    vctItemProposedTemp=vctItemProposed;
    vctItemPropAcum= new Vector();
    for(i=0;i<vctItemProposed.size();i++)
    {
        objprop=vctItemProposed.elementAt(i);
        cant=0;
        if(!objprop.read){
            for(j=0; j<vctItemProposedTemp.size();j++){
                objproptemp=vctItemProposedTemp.elementAt(j);
                bCondRule=false;
                switch(specification)
                {
                    case '<%=Constante.SPEC_POSTPAGO_VENTA%>'://VENTAS MOVILES
                        bCondRuleByVM=(!objproptemp.read)&&(objprop.objidnew == objproptemp.objidnew);
                        bCondRule=bCondRuleByVM;
                        break;
                    case '<%=Constante.SPEC_CAMBIAR_PLAN_TARIFARIO%>': //SA-CAMBIO DE PLAN T.
                        bCondRuleByCP=(!objproptemp.read)&&(objprop.objidold == objproptemp.objidold)&&(objprop.objidnew == objproptemp.objidnew);
                        bCondRule=bCondRuleByCP;
                        break;
                    case '<%=Constante.SPEC_BOLSA_CREACION%>': //BOLSA -CREACION
                        bCondRuleByBCR=(!objproptemp.read)&&(objprop.objidnew == objproptemp.objidnew);
                        bCondRule=bCondRuleByBCR;
                        break;
                    case '<%=Constante.SPEC_BOLSA_CAMBIO%>': //BOLSA - CAMBIO
                        bCondRuleByBCA=(!objproptemp.read)&&(objprop.objidold == objproptemp.objidold)&&(objprop.objidnew == objproptemp.objidnew);
                        bCondRule=bCondRuleByBCA;
                        break;
                    case '<%=Constante.SPEC_ACTIVAR_DESACTIVAR_SERVICIOS%>': //SA- ACTIV/DES
                        bCondRuleBySA=(!objproptemp.read)&&(objprop.objidnew == objproptemp.objidnew);
                        bCondRule=bCondRuleBySA;
                        break;
                }

                if(bCondRule){
                    // alert(cant + '+' + objproptemp.cantidad);
                    cant=cant + Number(objproptemp.cantidad);
                    // alert(cant);
                    objproptemp.read=true;
                    vctItemProposed.elementAt(j).read=true;
                }
            }
            //alert('objprop.cantidad'+cant);
            objprop.cantidad=cant;
            vctItemPropAcum.addElement(objprop);
        }
    }
    return vctItemPropAcum;
}
function fxMakeItenProposed(flag,objtypeold,objidold,objtypenew,objidnew,cantidad)
{
    this.flag=flag;
    this.objtypeold=objtypeold;
    this.objidold=objidold;
    this.objtypenew=objtypenew;
    this.objidnew=objidnew;
    this.cantidad=cantidad;
    this.read=false;
}
function fxItemValidatePropuestas(vctItemsMainOrder,specification){
    var numitems=vctItemsMainOrder.size();
    var vctItemProposed = new Vector();
    var vctServicios = new Vector();
    var newItemPlanTarifario='';
    var oldItemPlantarifarioId='';
    var newItemProductBolsa='';
    var oldItemProductBolsaId='';
    var quantity=0;
    for(i=0;i<numitems;i++){
        objItem=vctItemsMainOrder.elementAt(i);
        numbAtributos=objItem.size();
        for(j=0;j<numbAtributos;j++){
            if(objItem.elementAt(j).npobjitemheaderid == "'10'"){//cmb_ItemPlanTarifario
                vNewItemPlanTarifario=objItem.elementAt(j).npobjitemvalue.split("'");
                newItemPlanTarifario=vNewItemPlanTarifario[1];
                //alert('newItemPlanTarifario:'+newItemPlanTarifario);
            }
            if(objItem.elementAt(j).npobjitemheaderid == "'57'"){//txt_ItemNewPlantarifarioId//ORIGINAL
                vOldItemPlantarifarioId= objItem.elementAt(j).npobjitemvalue.split("'");
                oldItemPlantarifarioId=vOldItemPlantarifarioId[1];
                //alert('oldItemPlantarifarioId:'+oldItemPlantarifarioId);
            }//txtItemProduct //cmb_ItemProductBolsa
            if(objItem.elementAt(j).npobjitemheaderid == "'9'"){//cmb_ItemProducto
                vItemProductBolsa= objItem.elementAt(j).npobjitemvalue.split("'");
                newItemProductBolsa=vItemProductBolsa[1];
                // alert('newItemProductBolsa:'+newItemProductBolsa);
            }
            if(objItem.elementAt(j).npobjitemheaderid == "'74'"){//cmb_ItemProductBolsaId
                vItemProductBolsaId= objItem.elementAt(j).npobjitemvalue.split("'");
                oldItemProductBolsaId=vItemProductBolsaId[1];
                //alert('oldItemProductBolsa:'+oldItemProductBolsaId);
            }
            if(objItem.elementAt(j).npobjitemheaderid == "'25'"){//item_services
                vItemServices= objItem.elementAt(j).npobjitemvalue.split("'");
                itemServices=vItemServices[1];
                //alert('ItemServices'+ itemServices);
            }
            if(objItem.elementAt(j).npobjitemheaderid == "'21'"){ //txt_ItemQuantity
                vquantity= objItem.elementAt(j).npobjitemvalue.split("'");
                quantity=vquantity[1];
            }
        }

        switch(specification){
            case '<%=Constante.SPEC_POSTPAGO_VENTA%>'://VENTAS MOVILES -SA
                objitemproposed= new fxMakeItenProposed(0,'','','VM',newItemPlanTarifario,quantity);
                vctItemProposed.addElement(objitemproposed);
                vctServicios.addElement(new fxMakeAdditionalService(itemServices,1,specification));//quantity));
                break;

            case '<%=Constante.SPEC_CAMBIAR_PLAN_TARIFARIO%>': //SA-CAMBIO DE PLAN T. - SA
                objitemproposed= new fxMakeItenProposed(0,'CP',oldItemPlantarifarioId,'CP',newItemPlanTarifario,quantity);
                vctItemProposed.addElement(objitemproposed);
                vctServicios.addElement(new fxMakeAdditionalService(itemServices,1,specification));//quantity));
                break;
            case '<%=Constante.SPEC_BOLSA_CREACION%>': //BOLSA -CREACION
                objitemproposed= new fxMakeItenProposed(0,'','','BCR',newItemProductBolsa,1);
                vctItemProposed.addElement(objitemproposed);
                break;
            case '<%=Constante.SPEC_BOLSA_CAMBIO%>': //BOLSA - CAMBIO
                objitemproposed= new fxMakeItenProposed(0,'BCA',oldItemProductBolsaId,'BCA',newItemProductBolsa,1);
                vctItemProposed.addElement(objitemproposed);
                break;
            case '<%=Constante.SPEC_ACTIVAR_DESACTIVAR_SERVICIOS%>': //SA-ACT/DES
                vctServicios.addElement(new fxMakeAdditionalService(itemServices,1,specification));//quantity));
                break;
        }

    }
    /*servicios adicionales SA*/
    if(vctServicios.size()>0){
        for(k=0;k<vctServicios.size();k++){
            objAddServ= vctServicios.elementAt(k);
            vctSelectServId=fxGetSelectedServicesIds(objAddServ.itemServices,objAddServ.specification);
            for(i=0;i<vctSelectServId.size();i++){
                objitemproposed= new fxMakeItenProposed(0,'',0,'SA',vctSelectServId.elementAt(i),objAddServ.quantity);
                vctItemProposed.addElement(objitemproposed);
            }
        }
    }  //end if
    return vctItemProposed;
}
function fxMakeAdditionalService(itemServices,quantity,specification)
{
    this.itemServices=itemServices; //cadena
    this.quantity=quantity; //cantidad
    this.specification=specification; //subcategoria
}
function fxGetSelectedServicesIds(cadenaSeleccionados,specification) //RETURN ARRAY
{
    var vctItemSelectedServices = new Vector();
    var cadena = cadenaSeleccionados.split("|");
    var grupos=Math.floor(cadena.length/3);
    for(i=0;i<grupos;i++){
        servId=cadena[i*3+1];
        estado_activo=cadena[i*3+2];
        estado_modificado=cadena[i*3+3];
        if((estado_activo=='N')&&(estado_modificado=='S')){ //nuevos agregados
            vctItemSelectedServices.addElement(servId);
        }
    }
    return vctItemSelectedServices;
}
// FIN - VALIDACION DE PROPUESTAS

//Pop up para editar la direccion de entrega
function fxEditAdress() {
    var form = document.frmdatos;
    var customerid = form.txtCompanyId.value;
    var regionId = "";
    var userId = '<%=iUserId%>';

    if (customerid != "") {
        if (form.txtSiteId.value != "" && form.txtSiteId.value != "0")
            regionId = form.hdnSiteRegionId.value;
        else
            regionId = form.hdnCustomerRegionId.value;

        var param = "?strDirEntrega="+form.hdnDeliveryAddress.value+"&strDpto="+form.hdnDeliveryState.value+"&strProv="+form.hdnDeliveryProvince.value+
                "&strDist="+form.hdnDeliveryCity.value+"&strDptoId="+form.hdnDeliveryStateId.value+"&strProvId="+form.hdnDeliveryProvinceId.value+
                "&strDistId="+form.hdnDeliveryCityId.value+"&regionAddressId="+regionId+"&userId="+userId+"";
        var param = param+"&strSpecification="+form.hdnSubCategoria.value+"&strUserName="+form.hdnUserName.value; //[SP3103 VALIDACION DIRECCIONES RIESGO] DIANA LLANOS
        var param = param+"&strOrderId="+vform.hdnOrderId.value+"&strCompanyId="+vform.txtCompanyId.value; //[SP3103 VALIDACION DIRECCIONES RIESGO] DIANA LLANOS
        
        var frameUrl = "<%=strRutaContext%>/htdocs/jsp/Order_EditAddress.jsp"+param;
        var url = appContext+"/DYNAMICSECTION/DynamicSectionItems/DynamicSectionItemsPage/PopUpOrderFrame.jsp?av_url="+escape(frameUrl);
        WinAsist = window.open(url,"WinAsist","toolbar=no,location=no,directories=no,status=no,menubar=0,scrollbars=no,resizable=yes,screenX=100,top=80,left=100,screenY=80,width=650,height=300,modal=yes");
    }else
        alert("Debe seleccionar una compañía");
}





function fxGenerarGuia (){
    form = document.frmdatos;
    if (form.hdnChangedOrder.value=="S"){
        alert("La orden ha sido modificada, primero deberá grabarla y luego generar los comprobantes");
        try{
            form.cmbAction.value="";
        }catch(e) {}
        return;
    }else{
        form.myaction.value='generaGuia';
        form.action="<%=strURLOrderServlet%>";
        form.btnGenerarGuia.disabled=true;
        form.submit();
    }
}

/********30/12/210****INICIO EXTERNO.JNINO********************/
function fxSuspendeEquipo(strType){
    form = document.frmdatos;

    if (form.hdnChangedOrder.value=="S"){
        alert("La orden ha sido modificada, primero deberá grabarla y luego generar la suspensión de equipos");
        try{
            form.cmbAction.value="";
        }catch(e) {}
        return;
    }else{
        form.hdnSuspEquipType.value = strType;
        form.myaction.value='suspEquipos';
        form.action="<%=strURLOrderServlet%>";

        form.submit();
    }
}
/********30/12/210****FIN EXTERNO.JNINO**********************/

/*jsalazar - modif hpptt # 1 - 29/09/2010 - inicio*/
function MuestraFechaProgFin(specificationId){
    form = document.frmdatos;
    if(specificationId == <%=Constante.SPEC_SERVICIOS_ADICIONALES[0]%>){
        dvFechaFinProgEdit.style.display="";
        dvFechaFinProgEditInput.style.display="";
    }else{
        dvFechaFinProgEdit.style.display="none";
        dvFechaFinProgEditInput.style.display="none";
    }
}
/*jsalazar - modif hpptt # 1 - 29/09/2010 - fin*/

/*lroque--normalizarEdit*/
function verificarNormalizacionDir(){
    try {
        var vform = document.frmdatos;
        var orderId = vform.hdnNumeroOrder.value;

        var server='<%=actionURL_NormalizarServlet%>';
        var params = 'myaction=validarCargaNorm&orderID='+orderId;
        jQuery.ajax({
            url: server,
            data: params,
            type: "POST",
            dataType: 'json',
            success:function(data){
                if(data.valida == 1){
                    var datosNorm = data.datosNorm;
                    var vform = document.frmdatos;
                    vform.target = "bottomFrame";
                    vform.action="<%=actionURL_NormalizarServlet%>?myaction=cargarpopupEdit&datosNorm="+datosNorm;
                    vform.submit();
                }else{
                    return false;
                }
            },
            error:function(){
                return false;
            }
        });
    }catch(err) {}
}

/*rquispe--biometricaEdit*/
function verificacionBiometricaEdit(){

    var vform = document.frmdatos;
    var subCategoria = vform.hdnSubCategoria.value;

    var server = '<%=strURLBiometricEdit%>';
    var params = 'myaction=verifCategoria&strCategoria=' + subCategoria ;
    $.ajax({
        url: server,
        data: params,
        type: "POST",
        success: function (data) {
            if (data == "1") {
                return validActivation();
            }else{  return false; }
        }
    });
}


function validActivation(){
    var vform = document.frmdatos;
    try {
        var subCategoria = vform.hdnSubCategoria.value;
        var soluciones=obtenerSoluciones();
        var login = vform.hdnSessionLogin.value;
        var orderId = vform.hdnNumeroOrder.value;

        var server = '<%=strURLBiometricEdit%>';
        var params = 'myaction=getValidActivation&subCategoria=' + subCategoria + "&solution=" + soluciones + "&login=" + login + "&orderId=" + orderId;
        $.ajax({
            url: server,
            data: params,
            type: "POST",
            success: function (data) {
                if (data == "1")
                {  openBiometricPopUp(); }
                else
                {  return false;         }
            }
        });
    }
    catch (e)
    {   return false;  }

}


function obtenerSoluciones(){
    var itemSoluciones="";

    if (form.hdnItemValuetxtItemSolution != undefined){
        if (form.hdnItemValuetxtItemSolution.length == undefined) {
            itemSoluciones=  form.hdnItemValuetxtItemSolution.value;
        }else{
            for(var  xi = 0; xi < form.hdnItemValuetxtItemSolution.length; xi++){
                if(xi==form.hdnItemValuetxtItemSolution.length-1){
                    itemSoluciones +=form.hdnItemValuetxtItemSolution[xi].value;
                }else{
                    itemSoluciones +=form.hdnItemValuetxtItemSolution[xi].value+",";
                }

            }
        }
    }

    document.frmdatos.hdnItemSoluciones.value=itemSoluciones;
    return itemSoluciones;
}

//open popup biometric edit order
function openBiometricPopUp(){
    var vform = document.frmdatos;
    var orderId=vform.hdnNumeroOrder.value;
    vform.target = "bottomFrame";
    vform.action="<%=strURLBiometric%>?myaction=openBiometricOrderEdit"+"&idOrder="+orderId;
    vform.submit();
}

function closePopUpBiometricEdit(){
    alert("Necesario ralizar verificacion");
}

// EFLORES  PM0010274
function getCustDetail(customerId,userId){
    $.ajax({
        url:"<%= actionURL_GeneralServlet %>",
        type:"GET",
        async:false,
        data:{
            "metodo":"requestVerifyUserCanSeeCustomer",
            "strUserId" : userId,
            "strObjectId":customerId,
            "strObjectType":"CUSTOMER",
            "strTypeMessage":"ORDEN_LISTAR"
        },
        success:function(data){
            var val = data.split("|");
            var num = val[0].substring(3,2);
            if(num == "0"){
                alert(val[1]);
            }else{
                getCustomerDetail();
            }
        },
        error:function(){
            alert("Hay un error");
        }
    });
}
// FIN EFLORES
//INICIO: PRY-0864 | AMENDEZ - PRY-0980 AMENDEZ 09122017
function validateOrderVepCI(){
    try{
        var url_server = '<%=strURLOrderServlet%>';
        var varValidateCustomerOrderVEPCI=0;
        var nporderid=document.frmdatos.hdnOrderId.value;
        var npinitialquota=document.frmdatos.txtCuotaInicial.value;
        var totalsalesprice=parseFloat(document.frmdatos.txtTotalSalesPrice.value);
        var npvepquantityquota = document.frmdatos.cmbVepNumCuotas.value;
        var npspecificationid  = document.frmdatos.hdnSpecificationIdJ.value;
        var npvep = document.frmdatos.chkVepFlag.value;
        var nptype = 'ORDER_EDIT';
		//INICIO: PRY-0980 AMENDEZ 09122017 - Adicion de parametro forma de pago y correccion de valor customer
        var wn_customerId=document.frmdatos.txtCompanyId.value;
        var nppaymenttermsiq=document.frmdatos.txtPaymentTermsIQ.value;
		//FIN: PRY-0980 AMENDEZ 09122017
        var params = 'myaction=validateOrderVepCI&nporderid='+nporderid+'&npvepquantityquota='+npvepquantityquota+'&npinitialquota='+npinitialquota+'&npspecificationid='+npspecificationid+'&swcustomerid='+wn_customerId+'&totalsalesprice='+totalsalesprice+'&npvep='+npvep+'&nptype='+nptype+'&nppaymenttermsiq='+nppaymenttermsiq;

        $.ajax({
            url: url_server,
            data: params,
            async: false,
            type: "POST",
            success:function(data){
                varValidateCustomerOrderVEPCI=data;
            }
        });

        if(varValidateCustomerOrderVEPCI != "1"){
            alert(varValidateCustomerOrderVEPCI);
            return false;
        }
        return true;
    }catch (e){
        alert("Hubo un error en la validacion de orden de venta a plazos");
        return false;
    }
}
//FIN: PRY-0864 | AMENDEZ

//INICIO: PRY-1200 | AMENDEZ
function validateSpecificationVep(npspecificationid){
    try{
        var url_server = '<%=strURLOrderServlet%>';
        var varValidateSpecificationVep=0;

        var params = 'myaction=validateSpecificationVep&npspecificationid='+npspecificationid;

        $.ajax({
            url: url_server,
            data: params,
            async: false,
            type: "POST",
            success:function(data){
                varValidateSpecificationVep=data;
            }
        });

        if(varValidateSpecificationVep == "1"){
            return true;
        }
        return false;
    }catch (e){
        alert("Hubo un error en la validacion de orden de venta a plazos");
        return false;
    }
}

function validateInfoVEP(){

    try{
        //Inicio Valores de la orden de pantalla----------------------------------------------------
        var montototal     =parseFloat(document.frmdatos.txtTotalSalesPrice.value);
        var cuotainicial   =parseFloat(document.frmdatos.txtCuotaInicial.value);
        var formapagoci    = document.frmdatos.txtPaymentTermsIQ.value;
        var strformapagoci="";
        if(formapagoci=="1"){
            strformapagoci = 'Cargo en el recibo';
        }else if(formapagoci=="0"){
            strformapagoci = 'Contado';
        }else{
            strformapagoci = 'Contado';
        }

        if (cuotainicial == ""){
            cuotainicial=0;
        }

        var montofinanciar = 0;
        var cantidadcuotas = 0;
        var cuotasmensual  = 0;
        var ultimaCuota    = 0;

        if (document.frmdatos.chkVepFlag.checked == false){
            montofinanciar=0;
            cantidadcuotas=0;
            cuotasmensual=0;
            ultimaCuota='';
            strformapagoci='';
        }else{
            montofinanciar = CompletarNumeroDec(''+round_decimals(parseFloat(montototal-cuotainicial),2));
            cantidadcuotas = document.frmdatos.cmbVepNumCuotas.value;
            cuotasmensual  = CompletarNumeroDec(''+round_decimals(parseFloat(montofinanciar/cantidadcuotas),1));
            ultimaCuota    = CompletarNumeroDec(''+round_decimals(parseFloat(montofinanciar-(cuotasmensual*(cantidadcuotas-1))),2));
        }
        //Fin Valores de la orden de pantalla----------------------------------------------------

        //Inicio Valores de la orden de BD----------------------------------------------------
        var hdnFlagVEP=document.frmdatos.hdnFlagVEP.value;
        if( hdnFlagVEP == "" ){
            hdnFlagVEP='0';
        }

        var hdnMontoOrdenVEP   = parseFloat(document.frmdatos.hdnMontoOrdenVEP.value);
        var hdnCuotaInicialVEP    = parseFloat(document.frmdatos.hdnCuotaInicialVEP.value);
        var hdnFormaPagoCIVEP     = document.frmdatos.hdnFormaPagoCIVEP.value;
        var hdnStrFormaPagoCI="";
        if(hdnFormaPagoCIVEP=="1"){
            hdnStrFormaPagoCI = 'Cargo en el recibo';
        }else if(hdnFormaPagoCIVEP=="0"){
            hdnStrFormaPagoCI = 'Contado';
        }else{
            hdnStrFormaPagoCI = 'Contado';
        }

        var hdnCantidadCuotasVEP  = 0;
        var hdnMontoFinanciarVEP  = 0;
        var hdnCuotaMensualVEP    = 0;
        var hdnUltimaCuotaVEP     = 0;

        if (hdnFlagVEP == "0"){
            hdnMontoFinanciarVEP=0;
            hdnCantidadCuotasVEP=0;
            hdnCuotaMensualVEP=0;
            hdnUltimaCuotaVEP=0;
            hdnStrFormaPagoCI="";
        }else{
            hdnCantidadCuotasVEP  = document.frmdatos.hdnCantidadCuotasVEP.value;
            hdnMontoFinanciarVEP  = CompletarNumeroDec(''+round_decimals(parseFloat(hdnMontoOrdenVEP-hdnCuotaInicialVEP),2));
            hdnCuotaMensualVEP    = CompletarNumeroDec(''+round_decimals(parseFloat(hdnMontoFinanciarVEP/hdnCantidadCuotasVEP),1));
            hdnUltimaCuotaVEP     = CompletarNumeroDec(''+round_decimals(parseFloat(hdnMontoFinanciarVEP-(hdnCuotaMensualVEP*(hdnCantidadCuotasVEP-1))),2));
        }
        //Fin Valores de la orden de BD----------------------------------------------------

        var requeridoMontoTotalOrdenVEP=' ';
        if (montototal != hdnMontoOrdenVEP){
            requeridoMontoTotalOrdenVEP='*';
        }

        var requeridoCuotaInicialVEP=' ';
        if (cuotainicial != hdnCuotaInicialVEP){
            requeridoCuotaInicialVEP='*';
        }

        var requeridoFormaPagociVEP=' ';
        if (strformapagoci != hdnStrFormaPagoCI){
            requeridoFormaPagociVEP='*';
        }

        var requeridoMontoFinanciarVEP=' ';
        if (montofinanciar != hdnMontoFinanciarVEP){
            requeridoMontoFinanciarVEP='*';
        }

        var requeridoCantidadCuotasVEP=' ';
        if (cantidadcuotas != hdnCantidadCuotasVEP){
            requeridoCantidadCuotasVEP='*';
        }

        var requeridoCuotaMensualVEP=' ';
        if (cuotasmensual != hdnCuotaMensualVEP){
            requeridoCuotaMensualVEP='*';
        }

        var requeridoUltimaCuotaVEP=' ';
        if (ultimaCuota != hdnUltimaCuotaVEP){
            requeridoUltimaCuotaVEP='*';
        }

        if ( requeridoMontoTotalOrdenVEP == '*' || requeridoCuotaInicialVEP == '*' || requeridoFormaPagociVEP == '*' || requeridoMontoFinanciarVEP == '*' || requeridoCantidadCuotasVEP == '*' || requeridoCuotaMensualVEP == '*' || requeridoUltimaCuotaVEP == '*'  ){

        var messageConfirm ='Resumen de Orden VEP \n\n ' +
                            'ANTES \n'+
                            '-----------------------------------------  \n'+
                            'Monto total\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0'+requeridoMontoTotalOrdenVEP+': '+hdnMontoOrdenVEP    +'       \n'+
                            'Cuota inicial\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0'+requeridoCuotaInicialVEP+': '+hdnCuotaInicialVEP  +'       \n'+
                            'Forma de pago CI\u00A0\u00A0\u00A0'+requeridoFormaPagociVEP+': '+hdnStrFormaPagoCI   +'       \n\n'+
                            'Monto a financiar\u00A0\u00A0'+requeridoMontoFinanciarVEP+': '+hdnMontoFinanciarVEP+'       \n'+
                            'Cantidad de cuotas\u00A0'+requeridoCantidadCuotasVEP+': '+hdnCantidadCuotasVEP+'       \n\n'+
                            'Cuota mensual\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0'+requeridoCuotaMensualVEP+': '+hdnCuotaMensualVEP  +'       \n'+
                            'Ultima cuota\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0'+requeridoUltimaCuotaVEP+': '+hdnUltimaCuotaVEP   +'       \n\n'+

                            'AHORA \n'+
                            '-----------------------------------------  \n'+
                            'Monto total\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0'+requeridoMontoTotalOrdenVEP+': '+montototal+' \n'+
                            'Cuota inicial\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0'+requeridoCuotaInicialVEP+': '+cuotainicial+' \n'+
                            'Forma de pago CI\u00A0\u00A0\u00A0'+requeridoFormaPagociVEP+': '+strformapagoci+' \n\n'+
                            'Monto a financiar\u00A0\u00A0'+requeridoMontoFinanciarVEP+': '+montofinanciar+' \n'+
                            'Cantidad de cuotas\u00A0'+requeridoCantidadCuotasVEP+': '+cantidadcuotas+' \n\n'+
                            'Cuota mensual\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0'+requeridoCuotaMensualVEP+': '+cuotasmensual+' \n'+
                            'Ultima cuota\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0'+requeridoUltimaCuotaVEP+': '+ultimaCuota;

        }else{
            var messageConfirm ='Resumen de Orden VEP \n\n' +
                'Monto total\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0: '+montototal+' \n' +
                'Cuota inicial\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0: '+cuotainicial+' \n' +
                'Forma de pago CI\u00A0\u00A0\u00A0: '+strformapagoci+' \n\n' +
                'Monto a financiar\u00A0\u00A0: '+montofinanciar+' \n' +
                'Cantidad de cuotas\u00A0: '+cantidadcuotas+' \n\n' +
                'Cuota mensual\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0: '+cuotasmensual+' \n' +
                'Ultima cuota\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0: '+ultimaCuota+' ';

        }


        if( (hdnFlagVEP == "0" && document.frmdatos.chkVepFlag.checked == true )|| (hdnFlagVEP == "1" && document.frmdatos.chkVepFlag.checked == true) || (hdnFlagVEP == "1" && document.frmdatos.chkVepFlag.checked == false) ){
            if (confirm(messageConfirm)) {
                return true;
            }
            return false;
        }

        return true;


    }catch(e) {
        alert("Hubo un error en la alerta de orden de venta a plazos");
        return false;
    }
}

//FIN: PRY-1200 | AMENDEZ

</script>

<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/jquery-1.10.2.js"></script>
<script>
var jQuery_1_10_2 = $.noConflict(true);
//FBERNALES VALIDA DIRECCIONES
function fxSearchAddressBlackList(){
    var vform = document.frmdatos;
    var hdnSessionId=vform.hdnUserName.value;  //vform.hdnSessionId.value;
    var hdnOrderId=vform.hdnOrderId.value;
    var txtCompanyId=vform.txtCompanyId.value;
    var txtAddress1s = document.getElementsByName("address1");
    var address1CmbIds = document.getElementsByName("address1CmbId");
    
    // Si ha sido modificado una dirección de entrega
    var txtAddress2s = document.getElementsByName("hdnDeliveryAddress");
   // var address1CmbId2s = document.getElementsByName("hdnDeliveryCityId");
   
    var allAddress= [];
    var evalAddress= [];
    var allDistri = [];

    var url_server = '<%=strURLOrderServlet%>';
   
    // Direcciones anteriores
      for (var i=0; i<txtAddress1s.length; i++){  
        var txtAddress= txtAddress1s[i].value; 
        var address1CmbId= address1CmbIds[i].value; 
        
        var arr_direcciones = [txtAddress];
        var arr_distri = [address1CmbId];
        allAddress[i]=arr_direcciones;
        allDistri[i]=arr_distri;
      }
      //Direcciones editadas    
      for (var j=0; j<txtAddress2s.length; j++){    
        var txtAddress= txtAddress2s[j].value; 
        var address1CmbId2= vform.hdnDeliveryCityId.value;      
        
        var arr_direcciones = [txtAddress];
        var arr_distri = [address1CmbId2];
        allAddress[i+j]=arr_direcciones;  
        allDistri[i+j]=arr_distri;
      }
    var countVal = 0;
    for ( var i=0; i< allAddress.length ; i++){
        var txtAddress= allAddress[i][0];
        var txtAddress2 =txtAddress;
        txtAddress=txtAddress.replace(/\s/gi, "");
        var cmbDist = allDistri[i][0];
        if(cmbDist==null || cmbDist==""){
            var strDpto= vform.hdnDeliveryState.value;
            var strProv= vform.hdnDeliveryProvince.value;
            var strDist= vform.hdnDeliveryCity.value;
        }else{
            var strDpto=null;
            var strProv=null;
            var strDist=null;
        }
        var flag=false;
        
        if(i==0){
            var arr_direcciones = [txtAddress];
            evalAddress[countVal]=arr_direcciones; 
            retorno=fxValidateAddressBlackList(txtAddress2,hdnSessionId,hdnOrderId,txtCompanyId,cmbDist,url_server,strDpto,strProv,strDist);
        
            countVal++;
        }else{
            for(j=0; j< evalAddress.length ; j++){
                var compareAddress1=(evalAddress[j][0]).replace(/-|\s/g, "");
                var compareAddress2=txtAddress.replace(/-|\s/g, "");
                //if((evalAddress[j][0]).replace(/\s/g, "")!=txtAddress.replace(/-\s/g, "")){
                if(compareAddress1!=compareAddress2){                 
                    flag=true;
                }
            }
            if(flag){
                var arr_direcciones = [txtAddress];
                evalAddress[countVal]=arr_direcciones;
                retorno=fxValidateAddressBlackList(txtAddress2,hdnSessionId,hdnOrderId,txtCompanyId,cmbDist,url_server,strDpto,strProv,strDist);
                
                countVal++;
            }
          
        }
         if(!retorno){
            return false;
        }
    }

    return retorno;
}
          
    function fxValidateAddressBlackList(txtAddress,hdnSessionId,hdnOrderId,txtCompanyId,cmbDist,url_server,strDpto,strProv,strDist){
        var vform = document.frmdatos;
        var txtAddress2 = txtAddress.replace(/\s/gi, "");
        if(cmbDist!=null && cmbDist!=""){
             var params = 'myaction=GetValidarDireccionRiesgo&txtAddress='+txtAddress2+'&cmbDist='+cmbDist+'&specificationid='+vform.hdnSubCategoria.value;
        }else{
             var params = 'myaction=GetValidarDireccionRiesgo&txtAddress='+txtAddress2+'&strDpto='+strDpto+'&strProv='+strProv+'&strDist='+strDist+'&specificationid='+vform.hdnSubCategoria.value;
        }
       
        var retorno = false;
        jQuery_1_10_2.ajax({
            url: url_server, 
            data: params,
            async: false,
            type: "POST",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success:function(data){

                 if(data.flag){
                    var confirma = confirm("La direccion ingresada " +txtAddress+" esta registrada en la base datos de fraude.\n ¿Desea continuar?");
                    insLogValidateAddress(data.correlacion,txtAddress2,hdnSessionId,hdnOrderId,txtCompanyId);
                    if(confirma){    
                        retorno= true;
                    }else{
                        retorno= false;
                    }                    
                }else{
                    retorno= true;
                }
            },
            error: function(){
                alert("Se produjo un error al intentar validar la dirección");   
            }
        });
        return retorno;  
    }
          // FBERNALES - 17/12/2015 INSERTA LOGS VALIDACION
function insLogValidateAddress(correlacion,direccion,usuario,orderId,companyId){
    var url_server = '<%=strURLOrderServlet%>';
    var params = 'myaction=GetInsertLogValidateAddress&direccion='+direccion+'&correlacion='+correlacion+'&createdBy='+usuario+'&npnumorder='+orderId+'&npidcliente='+companyId;
   
    var retorno = true;
    $.ajax({
        url: url_server, 
        data: params,
        async: false,
        type: "POST",
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success:function(data){                  
                retorno= true;
        },
        error: function(){
            alert("Se produjo un error al intentar validar la dirección");  
        }
    });   
    return retorno;
}
// FIN FBERNALES

//INICIO: PRY-1037 | KPEREZ

function validateSimManager(){
    var url_server = '<%=strURLOrderServlet%>';
    var dataString = $('#frmdatos').serialize();
    var params = 'myaction=validateSimManager&'+dataString;
    var retorno = false;
    $.ajax({
        url: url_server,
        async: false,
        type: 'POST',
        dataType: 'json',
        data: params,
        success: function(data){
            if(data.strMessage != "" && data.strMessage != null){
                alert(data.strMessage);
                retorno= false;
            }else{
                retorno= true;
            }
        },
        error:function(xhr, ajaxOptions, thrownError){
            if (xhr.status === 0) {
                alert('Not connect: Verify Network.');
            } else if (xhr.status == 404) {
                alert('Requested page not found [404]');
            } else if (xhr.status == 500) {
                alert('Internal Server Error [500].');
            } else if (xhr.status === 'parsererror') {
                alert('Requested JSON parse failed.');
            } else if (xhr.status === 'timeout') {
                alert('Time out error.');
            } else if (xhr.status === 'abort') {
                alert('Ajax request aborted.');
            } else {
                alert('Uncaught Error: ' + xhr.responseText);
            }
        }
    });

    return retorno;
}

//FIN: PRY-1037 | KPEREZ
//PRY-1093 JCURI
function validateCourierDelivery(){
	form = parent.mainFrame.document.frmdatos;
	if(form.cmbLugarAtencion ==undefined || form.chkCourier==undefined || (form.chkCourier!=undefined && form.chkCourier.disabled)){
		return true;
	}	
	 
	if(form.cmbAction.value !== "<%=Constante.ACTION_INBOX_AVANZAR%>"){
		return true;
	}

	specificationDely = form.hdnSubCategoria.value;
	valSpecification = true;
	
	if(arrDeliverySpecification.length > 0) {
		for(var i=0; i<arrDeliverySpecification.length; i++){
 			if (arrDeliverySpecification[i] == specificationDely) {	 		        	
 				valSpecification = false;
 				break;
       		}
		}
 	}
	
	if(valSpecification) {
		return true;
	}

	chkCourier = form.hdnChkCourier.value;
	cmbLugarAtencion = form.cmbLugarAtencion.value;
	arrDispatchPlacesDelivery = parent.mainFrame.arrDispatchPlacesDelivery;
	message = "";
	res = false;
	
	if(arrDispatchPlacesDelivery.length > 0) {
		for(var i=0; i<arrDispatchPlacesDelivery.length; i++){
	        if (arrDispatchPlacesDelivery[i] == cmbLugarAtencion ){
	        	if(chkCourier == '1') {
	        		message = "La atención de la orden será por delivery. Esta opción no podrá ser modificada ¿Desea continuar?";
	        		//cmbLugarAtencion = form.cmbLugarAtencion.value;
	        		
	        		if(document.getElementById("cmbTransportista")) {
	        			cmbTransportistaLength= document.getElementById("cmbTransportista").length;			    		
			    		if(cmbTransportistaLength==0) {
			    			message = "La tienda no cuenta con un operador logístico asociado en el sistema. Por favor cambiar el tipo de entrega";
			    		}
			    	}
	        	} else {
	        		message = "La atención de la orden será en tienda. Esta opción no podrá ser modificada ¿Desea continuar?";	        		
	        	}
	        	res = true;
	        	break;
	        }
	    }
	}	
	if(res) {
		if(confirm(message)) {
			return true;
		}else{
			return false;
		}	
	}
	return true;
}


</script>
<%-- =pSession.getId() --%>
<form name="frmdatos" id="frmdatos" action="" method="post" target="bottomFrame" >
    <input type="hidden" name="hdnSessionId" value="<%=strSessionId%>">
    <input type="hidden" name="hdnValidateCredit" value ="0">

<%}catch(SessionException se) {
    se.printStackTrace();
    out.println("<script>alert('Su sesión ha finalizado. Ingrese nuevamente');</script>");
    String strUrl = "/portal/pls/portal/WEBSALES.NP_USER_AVAILABILITY_PL_PKG.PL_LOGOUT";
    out.print("<script>parent.mainFrame.location.replace('"+strUrl+"');</script>");
}catch(Exception e) {
    String strMessageExceptionGeneralStart = "";
    strMessageExceptionGeneralStart = MiUtil.getMessageClean(e.getMessage());
    out.println("<script>alert('"+strMessageExceptionGeneralStart+"');</script>");
}
%>