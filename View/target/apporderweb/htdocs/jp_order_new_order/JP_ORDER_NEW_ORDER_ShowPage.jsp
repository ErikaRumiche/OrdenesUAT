<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@page import="oracle.portal.provider.v2.ProviderSession" %>
<%@page import="oracle.portal.provider.v2.ProviderUser" %>
<%@page import="oracle.portal.provider.v2.render.PortletRendererUtil" %>
<%@page import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil" %>
<%@page import="pe.com.nextel.bean.PortalSessionBean" %>
<%@page import="pe.com.nextel.exception.SessionException" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@page import="pe.com.nextel.util.MiUtil"%>
<%@page import="java.util.*"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="pe.com.nextel.service.NewOrderService"%>
<%@page import="pe.com.nextel.service.SessionService"%>
<%@page import="pe.com.nextel.service.GeneralService"%>
<%@page import="pe.com.nextel.bean.*"%>
<%@page import="pe.com.nextel.service.*" %>


<%

    try{

        String strSessionIdOrder = "";

        try{
            PortletRenderRequest pReq = (PortletRenderRequest) request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
            ProviderUser objetoUsuario1 = pReq.getUser();
            strSessionIdOrder=objetoUsuario1.getPortalSessionId();
            System.out.println("Sesión capturada  JP_ORDER_NEW_ORDER_ShowPage : " + objetoUsuario1.getName() + " - " + strSessionIdOrder );
        }catch(Exception e){
            System.out.println("Portler Not Found : " + e.getClass() + " - " + e.getMessage() );
            out.println("Portlet JP_ORDER_NEW_ORDER_ShowPage Not Found");
            return;
        }

        System.out.println("Sesión capturada después del resuest : " + strSessionIdOrder );
        PortalSessionBean portalSessionBean3 = (PortalSessionBean)SessionService.getUserSession(strSessionIdOrder);
        if(portalSessionBean3==null) {
            System.out.println("No se encontró la sesión de Java ->" + strSessionIdOrder);
            throw new SessionException("La sesión finalizó");
        }

        //<!--jsalazar - modif hpptt # 1 - 29/09/2010 -Inicio-->
        Date today=MiUtil.getDateBD("dd/MM/yyyy");
        String strToday=MiUtil.getDate(today,"dd/MM/yyyy");
        //<!--jsalazar - modif hpptt # 1 - 29/09/2010 -Fin-->
        int iUserId=0;
        int iAppId=0;
        int iSalesStructId=0;

        String strValidType = ""; // ocruces N_O000046759 Control de Tipo de Operación vs Tipo de Orden
        String strMensajeTypeOpera = ""; // ocruces N_O000046759 Control de Tipo de Operación vs Tipo de Orden

        iUserId  = portalSessionBean3.getUserid();
        iAppId   = portalSessionBean3.getAppId();
        iSalesStructId = portalSessionBean3.getSalesStructId();


        String strOrderId = null;
        String strMessage = null;
        String strParentUrl = "";
        String strParentName = "";
        Hashtable hOrder = NewOrderService.OrderDAOgetOrderIdNew();
   
   /* Inicio Data */
        String strDataSalesmanLabel       = "<td class='CellLabel' colspan=1 align='center' name='vendedorDataLabel' id='vendedorDataLabel'>Vendedor Data</td>";
        String strDataSalesmanHiddenLabel = "<td class='CellLabel' colspan=1 align='center' name='vendedorDataLabel' id='vendedorDataLabel'>&nbsp;</td>";
        String strDataSalesmanField       = "<td class='CellContent' colspan=1 id='vendedorData' name='vendedorData'>&nbsp;<select name='cmbVendedorData' onchange='fxChangeSalesManData(this);'><option value=''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option></select></td>";
        String strDataSalesmanHiddenField = "<td class='CellContent' colspan=1 id='vendedorData' name='vendedorData'>&nbsp;</td> ";
   /* Fin Data */

        if ( hOrder!= null)
            strOrderId = (String)hOrder.get("wn_orderid");

        //CGC COR0407
        String strSalesId = request.getParameter("salesTeamId")==null?"":request.getParameter("salesTeamId");
        String strRegionId = request.getParameter("regionId")==null?"":request.getParameter("regionId");
        System.out.println("[strRegionId]"+strRegionId);

        String strGeneratorid       = request.getParameter("generatorid")==null?"":request.getParameter("generatorid");
        String strGeneratortype     = request.getParameter("generatortype")==null?"":request.getParameter("generatortype");

        //Trayendo el numero de dias (59 dias) para calcular la fecha final de suspensiones
        GeneralService objService = new GeneralService();
        HashMap hshMaxNumDias = objService.getDominioList("MAX_DIAS_SUSPENSION");
        ArrayList arrModalitySellList = (ArrayList) hshMaxNumDias.get("arrDominioList");
        DominioBean diasMaximos = (DominioBean)arrModalitySellList.get(0);
        GeneralService objGeneralService = new GeneralService();

        if ( !MiUtil.getString(strGeneratorid).equals("") && !MiUtil.getString(strGeneratortype).equals("") ){
            HashMap hshResult = new HashMap();
            hshResult = objGeneralService.getObjectTypeUrl(strGeneratortype);
            strMessage = (String)hshResult.get("strMessage");
            if ( strMessage != null  )
                throw new Exception(strMessage);
            strParentUrl = (String)hshResult.get("strUrl");
            strParentName = (String)hshResult.get("strName");
        }

        //Validaciones del valor de la estructura de ventas del usuario generador de la Orden
        //-----------------------------------------------------------------------------------
        int iSalesStructOrigenId=0;
        HashMap hshResultStruct = new HashMap();
        hshResultStruct = objGeneralService.getSalesStructOrigen(iSalesStructId,Constante.SALES_STRUCT_PORTAL_DEFAULT) ;
        strMessage = (String)hshResultStruct.get("strMessage");
        if ( strMessage != null )
            throw new Exception(strMessage);
        iSalesStructOrigenId = MiUtil.parseInt((String)hshResultStruct.get("iSalesStructOrigen"));

        System.out.println("<<<<<<Datos de SalesStructId>>>>>>");
        System.out.println("iSalesStructId:"+iSalesStructId);
        System.out.println("iSalesStructOrigenId:"+iSalesStructOrigenId);


        //Inicio [Despacho en Tienda] RMARTINEZ
        NewOrderService objOrderService=new NewOrderService();
        HashMap hSpecificationsList = objOrderService.showCourierSpecificationsId();
        ArrayList arrDominioList = (ArrayList) hSpecificationsList.get("arrDominioList");
        int iShowCourier = 0;
        //Fin [Despacho en Tienda] RMARTINEZ


        //ocruces N_O000046759 Control de Tipo de Operación vs Tipo de Orden
        System.out.println("<<<<<<N_O000046759>>>>>>");
        HashMap hshValidateTypOpe=new HashMap();
        hshValidateTypOpe = objGeneralService.getValidateTypOpe("VALIDATE_CREDIT_EVALUATION");
        strValidType = (String)hshValidateTypOpe.get("strValidTypOpe");
        System.out.println("strValidType:"+strValidType);
        String strCustomerId = request.getParameter("customerId")==null?"":request.getParameter("customerId");
        System.out.println("strCustomerId : "+strCustomerId);

        NewOrderService objOrderTypeOpeService=new NewOrderService();
        HashMap hTypeOpeSpecificationsList = objOrderTypeOpeService.showTypeOpeSpecificationsId(strCustomerId);
        ArrayList arrTypeOpeSpecifications = (ArrayList) hTypeOpeSpecificationsList.get("arrTypeOpeSpecifications");
        //Fin ocruces N_O000046759 Control de Tipo de Operación vs Tipo de Orden

        //pzacarias
        IsolatedVerificationService objIsolatedVerificationService = new IsolatedVerificationService();
        List<IsolatedVerifConfigBean> specLst = objIsolatedVerificationService.getViaConfig(strCustomerId);
        //JCASTILLO
        String strRutaContext=request.getContextPath();
        String actionURL_DigitalDocumentServlet = strRutaContext+"/digitalDocumentServlet";
        String actionURL_PopulateCenterServlet = strRutaContext+"/populateCenterServlet";
        String strLogin=portalSessionBean3.getLogin();
        String strURLOrderServlet =strRutaContext+"/orderServlet";

        //PRY-1049| INICIO: AMENDEZ
        String strURLEditOrderServlet =strRutaContext+"/editordersevlet";
        int ibuildingid=portalSessionBean3.getBuildingid();
        //PRY-1049 | INICIO: AMENDEZ

     	// Inicio [reason Change of model] CDIAZ
        HashMap mapSpecificationsChangeModel = objGeneralService.getValueNpTable("CHANGE_MODEL_SPECIFICATION");
        ArrayList arrSpecificationsChangeModel = (ArrayList)mapSpecificationsChangeModel.get("objArrayList");
     	// Fin [reason Change of model] CDIAZ

//TDECONV003- Flag funcionalidad - 1 encendido | 0 apagado
        String strTDESwitch = objService.getValue(Constante.TDE_SWITCH, Constante.TDE_SWITCH_NPVALUEDESC);
        System.out.println("KOR_TDE_SWITCH:  " + strTDESwitch);
        
        //JCURI PRY-1093
        String strChecked = "";
        boolean activeChkCourier = (Boolean) objOrderService.activeChkCourier(iUserId,iAppId);
        System.out.println("JP_ORDER_NEW_ORDER_ShowPage [strLogin] " + strLogin + " [activeChkCourier] " + activeChkCourier);
        if(activeChkCourier) {
        	strChecked="checked";
        }
%>
<!--START MSOTO: 08-04-2014 SAR N_O000019563 Se deshabilita el combo-->
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/jQuery-min.js"></script>

<!-- DATOS DEL PEDIDO - INICIO -->
<script>
    window.parent.document.title='<%=strOrderId%>';
    function fCopiaOdenId(){


        var wn_PedidoId         = "<%=strOrderId%>";

        form = document.frmdatos;
        form.txtNumSolicitud.value = "P" + wn_PedidoId;
        form.txtNumSolicitud.focus();
        return;
    };

    function fxShowParent(GeneratorId, parentUrl){

        if (parentUrl!=null){
            //location.replace(parentUrl+GeneratorId)
            var strUrl=parentUrl+GeneratorId;
            var frameUrl=strUrl;
            var winUrl = "/portal/pls/portal/WEBSALES.NPSL_NEW_GENERAL_PL_PKG.PL_FRAME?av_url="+escape(frameUrl);
            window.open(winUrl, "Incidencia","status=yes, location=no, width=1040, height=800, left=100, top=100, scrollbars=yes, screenX=50, screenY=100");

        }
    }
    function fxValidateFecha(obj){ //Agregado para el la validacion de fechas en suspensiones
        form = document.frmdatos;
        var wv_dateProceso = form.txtFechaProceso.value;
        var wv_dateReconexion = form.txtFechaReconexion.value;
        var wv_message = "La fecha de Reconexion debe ser mayor o igual a la fecha de Proceso";

        if ( wv_dateProceso != "" && wv_dateReconexion !=""){
            if ( !isValidDate(wv_dateReconexion)){
                form.txtFechaReconexion.select();
                return;
            };

            //Fecha Proceso
            var wv_fProceso= new Date();
            var day_P    = parseFloat(wv_dateProceso.substring(0,2));
            var month_P  = parseFloat(wv_dateProceso.substring(3,5));
            var year_P   = parseFloat(wv_dateProceso.substring(6,10));

            //Fecha Reconexion
            var day_Rec    = parseFloat(wv_dateReconexion.substring(0,2));
            var month_Rec  = parseFloat(wv_dateReconexion.substring(3,5));
            var year_Rec   = parseFloat(wv_dateReconexion.substring(6,10));

            if (year_Rec < year_P){
                alert(wv_message);
                form.txtFechaReconexion.select();
                return;
            };
            if (year_Rec == year_P){
                if (month_Rec < month_P){
                    alert(wv_message);
                    form.txtFechaReconexion.select();
                    return;
                };
                if (month_Rec == month_P){
                    if (day_Rec < day_P){
                        alert(wv_message);
                        form.txtFechaReconexion.select();
                        return;
                    };
                }
            }
        }
        return true;

    }

    //<!--jsalazar - modif hpptt # 1 - 27/09/2010 -inicio-->
    function fxValidateFechaFin(obj){ //Agregado para el la validacion de fechas en suspensiones
        form = document.frmdatos;
        var wv_dateProceso = form.txtFechaProceso.value;
        var wv_dateFinProg = form.txtFechaFinProg.value;
        var wv_message = "La fecha Fin de Programación debe ser mayor a la fecha de Proceso";




        if ( wv_dateProceso != "" && wv_dateFinProg !=""){
            if ( !isValidDate(wv_dateFinProg)){
                form.txtFechaFinProg.value="";
                form.txtFechaFinProg.select();
                return;
            };

            //Fecha Proceso
            var wv_fProceso= new Date();
            var day_P    = parseFloat(wv_dateProceso.substring(0,2));
            var month_P  = parseFloat(wv_dateProceso.substring(3,5));
            var year_P   = parseFloat(wv_dateProceso.substring(6,10));

            //Fecha Reconexion
            var day_Fin    = parseFloat(wv_dateFinProg.substring(0,2));
            var month_Fin  = parseFloat(wv_dateFinProg.substring(3,5));
            var year_Fin   = parseFloat(wv_dateFinProg.substring(6,10));

            if (year_Fin < year_P){
                alert(wv_message);
                form.txtFechaFinProg.value="";
                form.txtFechaFinProg.select();
                return;
            };
            if (year_Fin == year_P){
                if (month_Fin < month_P){
                    alert(wv_message);
                    form.txtFechaFinProg.value="";
                    form.txtFechaFinProg.select();
                    return;
                };
                if (month_Fin == month_P){
                    if (day_Fin <= day_P){
                        alert(wv_message);
                        form.txtFechaFinProg.value="";
                        form.txtFechaFinProg.select();
                        return;
                    };
                }
            }
        }
        if ( wv_dateProceso == "" && wv_dateFinProg !=""){
            alert("Debe ingresar la fecha de proceso");
            form.txtFechaFinProg.value="";
            form.txtFechaProceso.select();
            return;
        }


        return true;

    }

    function fxValidateFechaInicio(obj){ //Agregado para el la validacion de fechas en suspensiones

        form = document.frmdatos;
        var wv_dateProceso = form.txtFechaProceso.value;
        var wv_dateToday = "<%=strToday%>";
        var wv_message = "La Fecha de Inicio de Programación debe ser mayor o igual a la fecha actual";
        var validar_fe_actual=false;
        // strFechPro
        if ( wv_dateProceso != ""){
            if ( !isValidDate(wv_dateProceso)){
                form.txtFechaProceso.value="";
                form.txtFechaProceso.select();
                return;
            };


            //Fecha Proceso
            var wv_fProceso= new Date();
            var day_P    = parseFloat(wv_dateProceso.substring(0,2));
            var month_P  = parseFloat(wv_dateProceso.substring(3,5));
            var year_P   = parseFloat(wv_dateProceso.substring(6,10));

            //Fecha actual
            var day_today    = parseFloat(wv_dateToday.substring(0,2));
            var month_today  = parseFloat(wv_dateToday.substring(3,5));
            var year_today   = parseFloat(wv_dateToday.substring(6,10));


            if (year_P < year_today){
                //alert(wv_message);
                form.txtFechaProceso.value="";
                form.txtFechaProceso.select();
                alert(wv_message);
                return;
            };
            if (year_P == year_today){
                if (month_P < month_today){
                    //alert(wv_message);
                    form.txtFechaProceso.value="";
                    form.txtFechaProceso.select();
                    alert(wv_message);
                    return;
                };
                if (month_P == month_today){
                    if (day_P < day_today){
                        form.txtFechaProceso.value="";
                        form.txtFechaProceso.select();
                        alert(wv_message);
                        return;
                    };
                }
            }

        }

        return true;

    }

    //<!--jsalazar - modif hpptt # 1 - 29/09/2010 - Fin-->

    //CBARZOLA:PROPUESTAS
    function openPopUpProposedList(){
        form = document.frmdatos;
        v_siteId=form.cmbResponsablePago.value;
        v_specificationId=form.cmbSubCategoria.value;
        v_customerId=form.txtCompanyId.value;
        v_sallerId=form.cmbVendedor.value;
        v_parametros="?customerId="+v_customerId+"&siteId="+v_siteId+"&specificationId="+v_specificationId+"&sallerId="+v_sallerId
        url="<%=request.getContextPath()%>/htdocs/jsp/PopUpProposedList.jsp"+ v_parametros;
        var screenY, screenX,vleft, vtop;
        if(navigator.appName == "Microsoft Internet Explorer") {
            screenY = document.body.offsetHeight;
            screenX = window.screen.availWidth;
        }
        else {
            screenY = window.outerHeight
            screenX = window.outerWidth
        }
        vleft=screen
        vleft = (screenX - 250) / 2;
        vtop = (screenY - 250) / 2;
        WinAsist=window.open(url,"WinSearchProposed","width=250,height=250,status=no,resizable=no,scrollbars=yes,screenX=" +vleft+ ", top="+ vtop +",left=" + vleft + ",screenY=" + vtop );
    }

    function fxShowCourier(){
        //Inicio [Despacho en Tienda] RMARTINEZ
        var existe = 0;
        form = document.frmdatos;
        <%
         iShowCourier = 0;
         for (int i = 0; i < arrDominioList.size(); i++) {
             DominioBean dominio = (DominioBean) arrDominioList.get(i);
             String strSpecificationId = dominio.getValor();
             System.out.println("strSpecificationId" + strSpecificationId);
             %>
        if (form.cmbSubCategoria.value == '<%=strSpecificationId%>'){
            existe = 1;
            <%iShowCourier = 1;%>
        }
        <%}%>
        //alert("existe: " + existe);
       
       //PRY-1093 JCURI
       if (existe == 0) {
    	   if (validateDeliverySpecification()){
               existe = 1;
               <%iShowCourier = 1;%>
          } 
       }
       //FIN PRY-1093 JCURI
      
        if (existe == 1){
            dvCourier.style.display="";
            dvChkCourier.style.display="";
        }else{
            dvCourier.style.display="none";
            dvChkCourier.style.display="none";
            form.hdnChkCourier.value = "0";
            form.chkCourier.checked = false;
        }
        //Fin [Despacho en Tienda] RMARTINEZ

        return existe;
    }
/*
    function fxValidateDigitalization(){
        var form = document.frmdatos;
        form.hdncmbSubCategoria.value = form.cmbSubCategoria.value;
        form.hdnFlgViaType.value = "";
        form.hdnVerifId.value = "";
        $("#dataIsolatedVerification").html('<tr><td class="CellContent" colspan="5">No se encontraron registros</td></tr>');
        fxValidateViaTypeDigitalizacion();

    }
    */

    function fxValidateTypeOpe(vthis){
        form = document.frmdatos;
        var existe = 0;
      //INICIO: AMENDEZ | PRY-1049
      if(!fxValidatePreEvaluation() ){
          return false;
      }
      //FIN: AMENDEZ | PRY-1049
        if (!<%=MiUtil.getString(strValidType).equals("0")%>){
            <%
             for (int i = 0; i < arrTypeOpeSpecifications.size(); i++) {
                 DominioBean dominioTypeOpe = (DominioBean) arrTypeOpeSpecifications.get(i);
                 String strSpecificationTypeOpe = dominioTypeOpe.getValor();
                 System.out.println("strSpecificationTypeOpe" + strSpecificationTypeOpe);
                 %>
            if (form.cmbSubCategoria.value == '<%=strSpecificationTypeOpe%>'){
                existe = 1;
            }
            <%}
       %>
            if ((existe == 0) && (form.cmbSubCategoria.value != "")){
                alert("Última Evaluación Crediticia NO Coincide con la Categoría de Orden a Crear. Debe Pre-Evaluar Nuevamente con el Tipo de Operación Correcta");
                if (<%=MiUtil.getString(strValidType).equals("2")%>){
                    form.cmbSubCategoria.value="";
                    form.cmbSubCategoria.focus();
                    return false;
                }
            }
        }

        SelectLoadSpecification(vthis);
        fxShowCourier();
        fxShowSectionsVIDD();


    }


    // JVERGARA - 04/04/2017 CONSULTA EL FLAG DE DIGITALIZACION EN VIA
    function fxValidateSpecLstDigitalization(){
        var form = document.frmdatos;
        var flgVIA =form.hdnFlgVIA.value;
        var flgSDD =form.hdnFlgSDD.value;
        if(flgVIA==2){
            fxValidateDigitalization();
        }else{
            fxValidateSpecLst()
        }

    }


    function fxValidateSpecLst(){
        var form = document.frmdatos;
        var inSpecLst = false;
        var isCompany = 0;
        <%
              for (int i = 0; i < specLst.size(); i++) {
                  %>
        form.hdncmbSubCategoria.value = form.cmbSubCategoria.value;
        if (form.cmbSubCategoria.value == '<%=specLst.get(i).getSpec()%>'){
            inSpecLst = true;
            isCompany = '<%=specLst.get(i).getIsCompany()%>';
        }
        <%}
        %>

        form.hdnFlgViaType.value = "";
        form.hdnVerifId.value = "";
        $("#dataIsolatedVerification").html('<tr><td class="CellContent" colspan="5">No se encontraron registros</td></tr>');


        if(inSpecLst){
            fxValidateViaType(isCompany);
        } else {
           // $("#dvVerificacionIdentidadAislada").css('style', 'display:"none"');
            document.getElementById('dvVerificacionIdentidadAislada').style.display = "none";
        }
    }

    function fxValidateDigitalization(){
        form = document.frmdatos;
        form.hdncmbSubCategoria.value = form.cmbSubCategoria.value;
        form.hdnVerifId.value = "";
        $("#dataIsolatedVerification").html('<tr><td class="CellContent" colspan="5">No se encontraron registros</td></tr>');
        form.hdnFlgViaType.value = 1;
            // $("#dvVerificacionIdentidadAislada").css('style', 'display:"block"');
            document.getElementById('dvVerificacionIdentidadAislada').style.display = "block";

            // Listar las verificaciones aisladas
            $.ajax({
                url: "<%=request.getContextPath()%>/generalServlet?metodo=requestGetViaCustomer&customerId=" + form.txtCompanyId.value + "&orderId=" + "<%=strOrderId%>" + "&hdncmbSubCategoria=" + form.cmbSubCategoria.value + "&selected=0",
                type: "GET",
                dataType:'json',
                success:function(data) {
                    $("#dataIsolatedVerification").html('');
                    if(data != null){
                        data = data.listIsolatedVerification;
                        $("#hdnRowCount").val(data.length);
                        if($.isArray(data)){
                            var isEmpty = true;
                            $.each(data, function(index, value){
                                $("#dataIsolatedVerification").append("<tr><td class=\"CellContent\"><input type=\"checkbox\" name=\"chkVerifId\" veriftype=\""+value.npverificationtype+"\" value =\"0\" onclick=\"checkInputOptions(this);fxSetVerifId(this,"+value.npverificationid+",'"+value.npnrodocument+"','"+value.nptipodocument+"');\"></td>" +
                                    "<td class=\"CellContent\">" + value.nptipodocument + "</td><td class=\"CellContent\">" + value.npnrodocument + "</td><td class=\"CellContent\">" + value.customer_name + "</td><td class=\"CellContent\">" + value.date_end_validity + "</td></tr>");
                                isEmpty = false;
                            });
                            if(isEmpty){
                                $("#dataIsolatedVerification").html('<tr><td class="CellContent" colspan="5">No se encontraron registros</td></tr>');
                            }
                        }
                    }
                },
                error:function(xhr, status, error) {
                    alert(error);
                }
            });
    }


    function fxValidateViaType(isCompany){
        form = document.frmdatos;

        if(isCompany == 1){
            form.hdnFlgViaType.value = 1;
           // $("#dvVerificacionIdentidadAislada").css('style', 'display:"block"');
            document.getElementById('dvVerificacionIdentidadAislada').style.display = "block";

            // Listar las verificaciones aisladas
            $.ajax({
                url: "<%=request.getContextPath()%>/generalServlet?metodo=requestGetViaCustomer&customerId=" + form.txtCompanyId.value + "&orderId=" + "<%=strOrderId%>" + "&hdncmbSubCategoria=" + form.cmbSubCategoria.value + "&selected=0",
                type: "GET",
                dataType:'json',
                success:function(data) {
                    $("#dataIsolatedVerification").html('');
                    if(data != null){
                        data = data.listIsolatedVerification;
                        $("#hdnRowCount").val(data.length);
                        if($.isArray(data)){
                            var isEmpty = true;
                            $.each(data, function(index, value){
                                $("#dataIsolatedVerification").append("<tr><td class=\"CellContent\"><input type=\"checkbox\" name=\"chkVerifId\" veriftype=\""+value.npverificationtype+"\" value =\"0\" onclick=\"checkInputOptions(this);fxSetVerifId(this,"+value.npverificationid+",'"+value.npnrodocument+"','"+value.nptipodocument+"');\"></td>" +
                                        "<td class=\"CellContent\">" + value.nptipodocument + "</td><td class=\"CellContent\">" + value.npnrodocument + "</td><td class=\"CellContent\">" + value.customer_name + "</td><td class=\"CellContent\">" + value.date_end_validity + "</td></tr>");
                                isEmpty = false;
                            });
                            if(isEmpty){
                                $("#dataIsolatedVerification").html('<tr><td class="CellContent" colspan="5">No se encontraron registros</td></tr>');
                            }
                        }
                    }
                },
                error:function(xhr, status, error) {
                    alert(error);
                }
            });
        } else {
            form.hdnFlgViaType.value = 0;
            document.getElementById('dvVerificacionIdentidadAislada').style.display = "none";
        }
    }
    //JVERGARA VALIDAR VIA Y ASSIGNEE

    function fxValidateAssigneeVIA(chkVerifId,npverificationnumber,nptipodocument){
        //Obtener el Tipo de Documento del Cliente
        fxGetClientDocType();

        form  = document.frmdatos;
        var DocNumAssignee = $.trim(form.txtDocNumAssignee.value);
        var DocNumClient   = $.trim(form.txtCampoOtro.value);
        var v_cmbDocTypeAssignee = document.getElementById("cmbDocTypeAssignee");
        var DocTypeAssignee =v_cmbDocTypeAssignee.options[v_cmbDocTypeAssignee.selectedIndex].text;
        var ClientDocType = form.hdnClientDocType.value;


        if(form.hdnChkAssignee.value==1){
            if(npverificationnumber != DocNumAssignee){
                blockSelectionVIA(chkVerifId,"El numero de documento del Apoderado no coincide con el de Verificacion de Identidad Aislada");
            }else if(nptipodocument != DocTypeAssignee){
                blockSelectionVIA(chkVerifId,"El tipo de documento del Apoderado no coincide con el de Verificacion de Identidad Aislada");
            }

        }else{
            if(npverificationnumber != DocNumClient){
                blockSelectionVIA(chkVerifId,"Los datos no coinciden con la Verificación de Identidad Aislada, ingrese los datos del apoderado");
            }else if(nptipodocument != ClientDocType){
                blockSelectionVIA(chkVerifId,"Los datos no coinciden con la Verificación de Identidad Aislada, ingrese los datos del apoderado");
            }

        }


    }
    function blockSelectionVIA(chkVerifId,message){
        $('input[name="' + chkVerifId.name + '"]').attr("checked",false);
        chkVerifId.value = "0";
        form.hdnVerifId.value = "";
        alert(message);
    }

    function myTrim(x) {
        return x.replace(/^\s+|\s+$/gm,'');
    }

    //INICIO VIDD
    function fxShowSectionsVIDD(){
        getChannel();
        validateVIDD();
        form.txtNumSolicitud.value = "";
        document.getElementById('solNumber').innerHTML='<a href="javascript:fxGetSolNumber();">NRO. SOLICITUD</a>';
        fxShowSectionPopulateCenter();
        fxShowSectionAssignee();
        fxShowSectionDigitDocument();
        fxValidateSpecLstDigitalization();
    }

    function fxCleanSectionsVIDD(){

        $('#dvAssignee').hide();
        $('#dvChkAssignee').hide();
        $('#dvAssigneeNextCell').hide();
        $('#dvChkAssigneeNextCell').hide();
        $('#dvAssigneeSection').hide();
        document.getElementById('solNumber').innerHTML='NRO. SOLICITUD';
        $('#dvDigitalizacionDocumentos').hide();
        //fxValidateSpecLstDigitalization();
    }
    function fxGetSolNumber(){
        form = document.frmdatos;
        var v_specificationId=form.cmbSubCategoria.value;
        var channel    = form.hdnChannelClient.value;
        var division = form.cmbDivision.value;

        if(form.hdnFlgSDD.value==<%=Constante.FLAG_SECTION_ACTIVE%>) {
            $.ajax({
                url: "<%= actionURL_DigitalDocumentServlet %>",
                type: "GET",
                async: false,
                data: {
                    "myaction": "getSolNumber",
                    "source":<%= Constante.SOURCE_ORDERS_ID%>,
                    "specificationId": v_specificationId,
                    "channel": channel,
                    "division":division,
                    "orderId": <%= strOrderId%>
                },
                dataType: "json",
                success: function (data) {
                    var message = data.strMessage || '';
                    if (message == '') {
                        form.txtNumSolicitud.value = data.solNumber;
                        form.txtNumSolicitud.focus()

                    }


                },
                error: function (xhr, status, error) {
                    alert(xhr.responseText);
                }
            });
        }else {
            fCopiaOdenId();
        }

    }
    function fxShowSectionAssignee(){
        var form = document.frmdatos;
        var flgSAC=form.hdnFlgSAC.value;
        fxCleanSectionAssignee();
        if(flgSAC==<%=Constante.FLAG_SECTION_ACTIVE%>){
            $('#dvAssignee').show();
            $('#dvChkAssignee').show();
            $('#dvAssigneeNextCell').show();
            $('#dvChkAssigneeNextCell').show();
            $('#dvAssigneeSection').hide();
        }else{
            $('#dvAssignee').hide();
            $('#dvChkAssignee').hide();
            $('#dvAssigneeNextCell').hide();
            $('#dvChkAssigneeNextCell').hide();
            $('#dvAssigneeSection').hide();
        }
    }

    function fxCleanSectionAssignee(){
        var form = document.frmdatos;
        form.hdnChkAssignee.value = "0";
        form.chkAssignee.checked = false;
        form.chkAssignee.value = "0";
        form.hdnCmbDocTypeAssigneeText.value="";
        form.hdnCmbDocTypeAssigneeVal.value="";
        form.cmbDocTypeAssignee.value ="";
        form.txtDocNumAssignee.value="";
        form.txtFirstNameAssignee.value="";
        form.txtLastNameAssignee.value="";
        form.txtFamilyNameAssignee.value="";
    }

    function fxShowSectionDigitDocument(){
        var form = document.frmdatos;
        var flgSDD=form.hdnFlgSDD.value;
        if(flgSDD==<%=Constante.FLAG_SECTION_ACTIVE%>){
            loadSectionDigitalizacionDocumentos();
            $('#dvDigitalizacionDocumentos').show();

        }else{
            $('#dvDigitalizacionDocumentos').hide();
        }
    }

    function fxShowSectionPopulateCenter(){
        var form = document.frmdatos;
        var flgCPUF=form.hdnFlgCPUF.value;
        if(flgCPUF==<%=Constante.FLAG_SECTION_ACTIVE%>){
            $('#dvPopulateCenter').show();

        }else{
            $('#dvPopulateCenter').hide();
        }
    }
    function fxUpdateResponse() {
        var form= document.frmdatos;
        $("#dvPopulateCenter input[name=cpufresponse]").attr('checked',false);
        form.hdnCpufResponse.value=2;
    }

    function validateVIDD(){
        var form = document.frmdatos;
        var v_specificationId=form.cmbSubCategoria.value;
        var v_channel = form.hdnChannelClient.value;
        var v_buildingId= <%=portalSessionBean3.getBuildingid()%>;
        var txtCustomerId    = form.txtCompanyId.value;
        var flag=0;
        $.ajax({
            url:"<%= actionURL_DigitalDocumentServlet %>",
            type:"GET",
            async:false,
            cache:false,
            data:{
                "myaction":"validateVIDD2",
                "buildingId" : v_buildingId,
                "type":<%= Constante.TYPE_ORDER_ID%>,
                "specificationId":v_specificationId,
                "channel":v_channel,
                "divisionId":form.cmbDivision.value,
                "customerId":txtCustomerId,
                "generatorType":"<%=strGeneratortype%>"
            },
            dataType:"json",
            success:function(data){
                var message=data.strMessage||'';
                if(message==''){
                    form.hdnFlgSAC.value=data.assigneesection;
                    form.hdnFlgSDD.value=data.digitalsection;
                    form.hdnFlgVIA.value = data.identverifsection;
                    form.hdnFlgCPUF.value= data.cpufsection;
                }else{
                    form.hdnFlgSAC.value="";
                    form.hdnFlgSDD.value="";
                    form.hdnFlgVIA.value = "";
                    form.hdnFlgCPUF.value= "";
                }

            },
            error:function(xhr,status,error) {
                alert(xhr.responseText);
            }
        });
    }
    function fxShowDocumentsToGenerate(){
        var vform=document.frmdatos;
        var specificationId=vform.cmbSubCategoria.value;
        var customerId    = vform.txtCompanyId.value;
        var orderId="<%=strOrderId%>";
        var divisionId=vform.cmbDivision.value;
        var cmbSignature=$('#cmbSignature');
        var tipoEjec;
        if(cmbSignature.val()==<%= Constante.SIGNATURE_TYPE_DIGITAL %>){
            tipoEjec="<%=Constante.EXECUTION_TYPE_ASYNC%>";
        }else{
            tipoEjec="<%=Constante.EXECUTION_TYPE_SYNC%>";
        }
        var tipoTrans="<%=Constante.TRX_TYPE_ALL_DOCUMENTS_BY_RULE%>"
        var url="/portal/page/portal/orders/DIGITAL_DOCUMENTS_TO_GENERATE";
        url = url + "?an_orderId="+orderId+"&an_specificationId="+specificationId+"&an_customerId="+customerId+"&an_divisionId="+divisionId+"&av_tipoEjec="+tipoEjec+"&av_tipoTrans="+tipoTrans;
        var vurl = "/portal/pls/portal/websales.NPSL_GENERAL_PL_PKG.WINDOW_FRAME?av_title="+escape("Lista de Documentos a Generar")+"&av_url="+escape(url);
        vWinNewCondition = window.open(vurl,"vWinNewCondition","toolbar=no,location=0,directories=no,status=yes,menubar=0,scrollbars=yes,resizable=no,screenX=100,top=80,left=100,screenY=80,width=530,height=400,modal=yes");

    }

    function fxShowPopulateCenterDet(){
        var orderId="<%=strOrderId%>";
        var url="/portal/page/portal/orders/POPULATE_CENTER_LIST";
        url = url + "?orderId="+orderId+"&display='EDIT'&userLogin="+'<%=strLogin%>';
        var vurl = "/portal/pls/portal/websales.NPSL_GENERAL_PL_PKG.WINDOW_FRAME?av_title="+escape("Centro Poblado de Uso Frecuente")+"&av_url="+escape(url);
        vWinNewCondition = window.open(vurl,"vWinNewCondition","toolbar=no,location=0,directories=no,status=yes,menubar=0,scrollbars=yes,resizable=no,screenX=100,top=80,left=100,screenY=80,width=1060,height=800,modal=yes");
    }

    function loadSectionDigitalizacionDocumentos(){

        var cmbSignature = $('#cmbSignature');
        var rowDinamico = $('#rowDinamico');
        getComboFirma(cmbSignature);
        if (cmbSignature.val() == "<%= Constante.SIGNATURE_TYPE_DIGITAL %>"){
            cargaFirmaDigital(rowDinamico);
            }
        else if (cmbSignature.val() == "<%= Constante.SIGNATURE_TYPE_MANUAL %>"){
            cargaFirmaManual(rowDinamico);

        }
    }


    function getComboFirma(cmbSignature){
        form = document.frmdatos;
        cmbSignature.empty();
        $.ajax({
            url:"<%= actionURL_DigitalDocumentServlet %>",
            type:"GET",
            async:false,
            data:{
                "myaction":"getCombo",
                "domain" : "<%= Constante.SIGNATURE_TYPE %>",
                "origen" : "<%= Constante.SOURCE_ALL_ID%>",
                "typetrx" : "<%= Constante.TRX_TYPE_ALL%>",
                "channel": "<%= Constante.CHANNEL_ALL_ID %>",
                "section": "<%= Constante.SECTION_DIGIT_DOCUMENT_ID%>"
            },
            dataType:"json",
            success:function(data){
                $.each(data.objArrayList, function (i, item) {
                    cmbSignature.append($("<option />").val(item.npvalue).text(item.npvaluedesc));
                });

            },
            error:function(xhr, status, error) {
                alert(error);
            }
        });
    }

    function getComboReason(){
        var form=document.frmdatos
        var cmbReason=$('#cmbReason');
        $.ajax({
            url:"<%= actionURL_DigitalDocumentServlet %>",
            type:"GET",
            async:false,
            data:{
                "myaction":"getCombo",
                "domain" : "<%= Constante.REASON %>",
                "origen" : "<%= Constante.SOURCE_ORDERS_ID %>",
                "typetrx" : "<%= Constante.TRX_TYPE_ALL%>",
                "channel": "<%= Constante.CHANNEL_ALL_ID %>",
                "section": "<%= Constante.SECTION_DIGIT_DOCUMENT_ID %>"

            },
            dataType:"json",
            success:function(data){
                $.each(data.objArrayList, function (i, item) {
                    cmbReason.append($("<option />").val(item.npvalue).text(item.npvaluedesc));
                });
            },
            error:function(xhr, status, error) {
                alert(error);
            }
        });
    }
    function getEmail(){
        form = document.frmdatos;
        var txtCustomerId    = form.txtCompanyId.value;
        var txtSite          = form.cmbResponsablePago.value;

        $.ajax({
            url:"<%= actionURL_DigitalDocumentServlet %>",
            type:"GET",
            async:false,
            data:{
                "myaction":"getEmail",
                "customerId" : txtCustomerId,
                "siteId":txtSite
            },
            dataType:"json",
            success:function(data){
                var message=data.strMessage||'';
                if(message=='') {
                    var email = data.email || '';
                    form.txtEmailDG.value=email;
                    if (email == '') {
                        form.hdnEmailNullF.value = 1;
                    } else {
                        form.hdnEmailNullF.value = 0;
                    }
                }
            },
            error: function(xhr, status, error) {
                alert(xhr.responseText);
            }
        });
    }

    function cargaFirmaManual(rowDinamico) {
        var td1 = '<td class="CellLabel" width="15%"><font color="red"><b>*</b></font>Motivo</td>';
        var td2 = '<td class="CellContent" width="25%"><select id="cmbReason" name="cmbReason" style="width: 100%"/></td>';
        var td3 = '<td class="CellContent"/>';
        rowDinamico.html(td1 + td2 + td3);
        getComboReason();
    }
    //INICIO TDE-CONV JCASTILLO
    function cargaFirmaManualMigracion(rowDinamico) {

        var td1 = '<td class="CellLabel" width="15%"><font color="red"><b>*</b></font>Motivo</td>';
        var td2 = '<td class="CellContent" width="25%"><select id="cmbReason" name="cmbReason" style="width: 100%">' +
            '<option value="<%=Constante.SIGNATURE_REASON_MIGRATION_PLAN_VALUE%>"><%=Constante.SIGNATURE_REASON_MIGRATION_PLAN_DESC%></option>'+
            '</select></td>';
        var td3 = '<td class="CellContent"/>';
        rowDinamico.html(td1 + td2 + td3);
    }
    //FIN TDE-CONV JCASTILLO
    function cargaFirmaDigital(rowDinamico){
        var td1='<td class="CellLabel" width="15%"><font color="red"><b>*</b></font>Correo Electr&oacute;nico</td>';
        var td2='<td class="CellContent" width="25%"><input type="text" name="txtEmailDG"  maxlength="80" style="width: 100%"  onchange="fxValidateEmail(this)"></td>';
        var td3 = '<td class="CellContent"/>';
        rowDinamico.html(td1+td2+td3);
        getEmail();

    }
    function updateSection(){
        var form = document.frmdatos;

        var cmbSignature=$('#cmbSignature');
        var rowDinamico=$('#rowDinamico');
        rowDinamico.empty();


        if(cmbSignature.val()==<%= Constante.SIGNATURE_TYPE_DIGITAL %>){
            cargaFirmaDigital(rowDinamico);
            fxValidateSpecLstDigitalization(); //JVERGARA Mostrar seccion de digitalizacion
        }else if(cmbSignature.val()==<%= Constante.SIGNATURE_TYPE_MANUAL %>){
            cargaFirmaManual(rowDinamico);
            fxValidateSpecLst();///JCASTILLO  consultar la configuracion actual

        }

    }
    function fxValidateEmail(email){
        if (email &&email.value != ""){

            expr = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
            if ( !expr.test(email.value) ){
                alert("El correo electronico es incorrecto por favor validar.");
                email.focus();
                return false;
            }else{
                return true;
            }

        }else{
            alert("El correo electronico esta vacio.");
            email.focus();
            return false;
        }
    }

    //FIN VIDD

    function checkInputOptions(obj_check) {
        $('input[name="' + obj_check.name + '"]').not(obj_check).attr("checked", false);
    };

    function fxSetVerifId(chkVerifId, npverificationid,npverificationnumber,nptipodocument ) {


        form  = document.frmdatos;

        if (chkVerifId.checked == true){
            chkVerifId.value = "1";
            form.hdnVerifId.value = npverificationid;

                //Solo si es dentro de proyecto, valida que la VIA seleccionada coincida con los datos del cliente o Apoderado
                if(form.hdnFlgSDD.value==<%=Constante.FLAG_SECTION_ACTIVE%>){
                    fxValidateAssigneeVIA(chkVerifId,npverificationnumber,nptipodocument);
        }

            }
        else{
            chkVerifId.value = "0";
            form.hdnVerifId.value = "";
        }


    }

    // Inicio [reason Change of model] CDIAZ
    function fxShowReasonCdm(){
    	var form = document.frmdatos;
   	    var spnReasonCdm = document.getElementById('spnReasonCdm');
   	    var cmbReasonCdm = document.getElementById('cmbReasonCdm');
   	    
   	    spnReasonCdm.style.display="none";
   	 	cmbReasonCdm.style.display="none";
        
        <%
        if(arrSpecificationsChangeModel != null && arrSpecificationsChangeModel.size()>0) {
        	for (int i = 0; i < arrSpecificationsChangeModel.size(); i++) {
        		TableBean specificationChangeModelBean = (TableBean)arrSpecificationsChangeModel.get(i);
        %>
        if (form.cmbSubCategoria.value == '<%=specificationChangeModelBean.getNpValue()%>'){
        	spnReasonCdm.style.display="inline";
        	cmbReasonCdm.style.display="inline";
        }
        <%
        	}
        }
        %>
    }
 	// Fin [reason Change of model] CDIAZ
    function fxGetClientDocType(){
        form = document.frmdatos;
        var txtCustomerId    = form.txtCompanyId.value;
        $.ajax({
            url:"<%= actionURL_DigitalDocumentServlet %>",
            type:"GET",
            async:false,
            cache:false,
            data:{
                "myaction":"getIdentInfoCustomer",
                "lcustomerId" : txtCustomerId
            },
            dataType:"json",
            success:function(data){
                var message=data.strMessage||'';
                if(message==''){
                    form.hdnClientDocType.value=data.wv_doctypecustomer;
                }else{
                    form.hdnClientDocType.value="";
                }
            },

            error: function(xhr, status, error) {
                alert(xhr.responseText);
            }
        });
    }

	//Ini: TDECONV003 KOTINIANO
    function fxShowFlagMigration(){
        form = document.frmdatos;
        //console.log("form.cmbSubCategoria.value "+form.cmbSubCategoria.value);
        //Flag TDE
        if (form.cmbSubCategoria.value == '<%=Constante.SPEC_POSTPAGO_VENTA%>' && '<%=Constante.TDE_SWITCH_ON%>' == '<%=strTDESwitch%>'){
            divFlagMigration.style.display="block";
            // $("#divFlagMigration").css("display", "block");
        }else{
            divFlagMigration.style.display="none";
            //$("#divFlagMigration").css("display", "none");
            form.chkFlagMigration.checked = false;
            form.hdnFlagMigration.value = 'N';
        }

    }
    //Fin: TDECONV003 KOTINIANO
    //Ini: TDECONV003 JCASTILLO
    function changeFlagMigration(chkFlagMigration){
        form = document.frmdatos;
        var cmbSignature = $('#cmbSignature');
        var rowDinamico = $('#rowDinamico');
        if (chkFlagMigration.checked==true){
            fCopiaOdenId();
            cmbSignature.val(<%=Constante.SIGNATURE_TYPE_MANUAL%>);
            $('#cmbSignature option:not(:selected)').attr('disabled',true);
            cargaFirmaManualMigracion(rowDinamico)
            $('#linkDocumentsToGenerate').hide();
        }else{
            fxGetSolNumber();
            cmbSignature.val(<%=Constante.SIGNATURE_TYPE_DIGITAL%>)
            cargaFirmaDigital(rowDinamico);
            $('#cmbSignature option').attr('disabled',false);
            $('#linkDocumentsToGenerate').show();

        }

    }
    //Fin: TDECONV003 JCASTILLO

    // DOLANO - PRY-1049 - BEGIN
    function fxValidatePreEvaluation(){
        var form =document.frmdatos;
        var subCategoriaVal = form.cmbSubCategoria.value;
        var valueRegion=parent.mainFrame.document.getElementById('hdnRegion').value;
        var valueEvaluation=0;
        // Si se selecciona SubCategoria POSTPAGO NUEVA & ADICION
        if (subCategoriaVal == '<%=Constante.SPEC_POSTPAGO_VENTA%>'){

            var url_server    = '<%=strURLEditOrderServlet%>';
            var swcustomerid  = form.txtCompanyId.value;
            var npbuidingid   ='<%=ibuildingid%>';

            var params = 'myaction=loadUseAddressInOrder&customerId='+swcustomerid+'&npbuidingid='+npbuidingid;
            // Cargar direccion de uso, si pasa todas las validaciones
            $.ajax({
                url: url_server,
                dataType:'json',
                data: params,
                type: "POST",
                async: false,
                success:function(data) {

                    /* Comportamientos:
                    * 1 = Flujo actual continua
                    * 2 = Alerta. Flujo continua
                    * 3 = Alerta. Restringir flujo
                    * 4 = Flujo Bafi 2300
                    * 5 = Flujo Bafi AWS*/

                    if(data.addressUse.behavior==1){
                        parent.mainFrame.document.getElementById('hdnCobertura').value = data.addressUse.flagcobertura;
                        valueEvaluation= 1;
                    }else if(data.addressUse.behavior==2){
                        alert(data.addressUse.message);
                        parent.mainFrame.document.getElementById('hdnCobertura').value = data.addressUse.flagcobertura;
                        valueEvaluation= 1;
                    }else if (data.addressUse.behavior==3){
                        alert(data.addressUse.message);
                        parent.mainFrame.document.getElementById('hdnCobertura').value = data.addressUse.flagcobertura;
                        valueEvaluation= 0;
                    }else if (valueRegion == "" && (data.addressUse.behavior == 4 || data.addressUse.behavior == 5)) {
                        var table = parent.mainFrame.document.getElementById('table_address');
                        var row = table.insertRow(-1);
                        col = row.insertCell(0);
                        col.className = 'CellContent';
                        col.align = 'left';
                        col.innerHTML = "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"+data.addressUse.useAddress;

                        col = row.insertCell(1);
                        col.className = 'CellContent';
                        col.align = 'left';

                        <!-- BEGIN: PRY-1140 | DOLANO-0003 -->
                        /* Solo si el comportamiento es de flujo BAFI 2300, es decir que tiene cobertura 2300
                        se muestra el HSZ */
                        if(data.addressUse.behavior == 4 && data.addressUse.homeServiceZoneDesc != null && data.addressUse.homeServiceZoneDesc !== ''){
                            col.innerHTML = data.addressUse.district + " (HSZ = " + data.addressUse.homeServiceZoneDesc + " )" ;
                        } else {
                        col.innerHTML = data.addressUse.district;
                        }
                        <!-- END: PRY-1140 | DOLANO-0003 -->

                        col = row.insertCell(2);
                        col.className = 'CellContent';
                        col.align = 'left';
                        col.innerHTML = data.addressUse.province;

                        col = row.insertCell(3);
                        col.className = 'CellContent';
                        col.align = 'left';
                        col.innerHTML = data.addressUse.department;

                        col = row.insertCell(4);
                        col.className = 'CellContent';
                        col.align = 'center';
                        col.innerHTML = data.addressUse.addresstype;

                        parent.mainFrame.document.getElementById('hdnCobertura').value = data.addressUse.flagcobertura;
                        parent.mainFrame.document.getElementById('hdnRegion').value = data.addressUse.department;
                        parent.mainFrame.document.getElementById('hdnProvince').value = data.addressUse.province;
                        parent.mainFrame.document.getElementById('hdnDistrict').value = data.addressUse.district;
                        parent.mainFrame.document.getElementById('hdnRegiondId').value = data.addressUse.zonacobid;
                        parent.mainFrame.document.getElementById('hdnProvinceId').value = data.addressUse.zonacobprov;
                        parent.mainFrame.document.getElementById('hdnDistrictId').value = data.addressUse.zonacobdist;
                        parent.mainFrame.document.getElementById('hdnUseFullAddress').value = data.addressUse.usefulladdress;

                        <!-- BEGIN: PRY-1049 | DOLANO-0002 -->
                        parent.mainFrame.document.getElementById('hdnHomeServiceZone').value = data.addressUse.homeServiceZone;
                        <!-- END: PRY-1049 | DOLANO-0002 -->

                        valueEvaluation= 1;

                    }else{
                        parent.mainFrame.document.getElementById('hdnCobertura').value=data.addressUse.flagcobertura;
                        valueEvaluation= 1;
                    }
                },
                error: function(xhr, status, error) {
                    alert(xhr.responseText);
                    valueEvaluation= 0;

                }
            });

            if(valueEvaluation == 0){
                return false;
            }
            return true;
        }else if(valueRegion!=""){
            var table = parent.mainFrame.document.getElementById('table_address');
            var table_length = table.rows.length;
            table.deleteRow(table_length-1);
            parent.mainFrame.document.getElementById('hdnRegion').value="";
            parent.mainFrame.document.getElementById('hdnProvince').value="";
            parent.mainFrame.document.getElementById('hdnDistrict').value="";
            parent.mainFrame.document.getElementById('hdnRegiondId').value="";
            parent.mainFrame.document.getElementById('hdnProvinceId').value="";
            parent.mainFrame.document.getElementById('hdnDistrictId').value="";
            parent.mainFrame.document.getElementById('hdnUseFullAddress').value="";
            <!-- BEGIN: PRY-1049 | DOLANO-0002 -->
            parent.mainFrame.document.getElementById('hdnHomeServiceZone').value="";
            <!-- END: PRY-1049 | DOLANO-0002 -->

            return true;
        }
        return true;

    }
    // DOLANO - PRY-1049 - END

    //PRY-1093 JCURI
	function validateDeliverySpecification() {
		form  = document.frmdatos;
		if(arrDeliverySpecification.length > 0) {
			dispatchPlaceId = form.cmbLugarAtencion.value;
			for(var i=0; i<arrDeliverySpecification.length; i++){
	 			if (arrDeliverySpecification[i] == form.cmbSubCategoria.value) {	 		        	
	 				return true;
	       		}
			}
	 	}
		return false;		
    }
	//FIN PRY-1093


</script>

<!-- Inicio Data -->
<script defer>
    function fxHideDataFields(){
        parent.mainFrame.vendedorData.innerHTML = "<%=strDataSalesmanHiddenField%>";
        parent.mainFrame.vendedorDataLabel.innerHTML = "<%=strDataSalesmanHiddenLabel%>";
        document.frmdatos.hdnDataFieldsVisibles.value="N";
    }
    function fxShowDataFields(){
        parent.mainFrame.vendedorData.innerHTML = "<%=strDataSalesmanField%>";
        parent.mainFrame.vendedorDataLabel.innerHTML = "<%=strDataSalesmanLabel%>";
        document.frmdatos.hdnDataFieldsVisibles.value="S";
    }


    function fxChangeSalesManData(newVale){
        form = document.frmdatos;
        var txtCustomerId    = form.txtCompanyId.value;
        var txtSite          = form.cmbResponsablePago.value;
        var txtVendedorId    = newVale.options[newVale.selectedIndex].value;
        var txtVendedor      = newVale.options[newVale.selectedIndex].text;
        if (txtVendedorId != ""){
            url= appContext+"/orderservlet?an_vendedorid="+txtVendedorId+"&myaction=validateSalesExclusivity";
            url = url +"&an_CompanyId="+txtCustomerId+"&an_SiteId="+txtSite+"&av_Vendedor="+txtVendedor;
            parent.bottomFrame.location.replace(url);
        }
    }

    function calcularFechaReconex(){
        var diasMaximos = "<%=diasMaximos.getValor()%>";
        var fechaProceso = form.txtFechaProceso.value;
        var dia = "";
        var mes = "";
        var diferenciaFechas = "";
        var fechaReconex = form.txtFechaReconexion.value;
        if(fechaProceso!="" && isValidDate(fechaProceso) && formatoFecha(fechaProceso)){
            fechaProceso = fechaProceso.split("/")[2]+"/"+fechaProceso.split("/")[1]+"/"+fechaProceso.split("/")[0];
            if(fechaReconex!=""){
                if(formatoFecha(fechaReconex)){
                    fechaReconex = fechaReconex.split("/")[2]+"/"+fechaReconex.split("/")[1]+"/"+fechaReconex.split("/")[0];
                    var diferenciaFechas = DifferenceBetweenDates( fechaProceso, fechaReconex, "D", false )
                    if(diferenciaFechas<=15){
                        alert("La diferencia entre fechas debe ser mayor a 15 dias");
                        return false;
                    }
                }else{
                    form.txtFechaReconexion.value="";
                }
            }else{
                fechaReconex = new Date(fechaProceso);
                fechaReconex.setDate(fechaReconex.getDate()+(parseInt(diasMaximos)-1));
                dia = ""+fechaReconex.getDate();
                if(dia.length==1){
                    dia = "0"+fechaReconex.getDate();
                }
                mes = ""+(fechaReconex.getMonth()+1);
                if(mes.length==1){
                    mes = "0"+mes;
                }
                form.txtFechaReconexion.value=dia+"/"+mes+"/"+fechaReconex.getFullYear();
            }
        }
    }
    function formatoFecha(fecha){
        if (!/^\d{2}\/\d{2}\/\d{4}$/.test(fecha)){
            alert("formato de fecha no válido (dd/MM/yyyy)");
            return false;
        }
        return true;
    }

    function fxValidaDiasSuspension(){
        var cant_items = parent.mainFrame.vctItemsMainFrameOrder.size();
        form  = document.frmdatos;
        var npScheduleDate = form.txtFechaProceso.value;
        var npScheduleDate2 = form.txtFechaReconexion.value;
        var v_specificationId   = form.cmbSubCategoria.value;
        var phonoNumber = "";

        if (form.txtFechaProceso.value != form.hdnFechaProceso.value || form.txtFechaReconexion.value != form.hdnFechaReconexion.value){
            if (items_table.rows.length >= 2){
                if( confirm("¿Está usted seguro que desea modificar la fecha. Se revalidará la cantidad de días de suspension por Item?") ){
                    form.hdnFechaProceso.value = form.txtFechaProceso.value;
                    form.hdnFechaReconexion.value = form.txtFechaReconexion.value;

                    bucle1:
                            for(var i = 0 ; i < cant_items ; i++) {
                                vctItems = vctItemsMainFrameOrder.elementAt(i);
                                bucle2:
                                        for (var j = 0 ; j < vctItems.size() ; j++) {
                                            if (j == 0){
                                                phonoNumber += replace(vctItems.elementAt(j).npobjitemvalue,"'","");
                                                break bucle2;
                                            }
                                        }
                                phonoNumber += "/";
                            }

                    var url = "<%=request.getContextPath()%>/serviceservlet?paramPhoneNumber=" + phonoNumber +"&paramSpecification=" + v_specificationId + "&myaction=validaDiasSuspensionCrear&strNpScheduleDate="+ npScheduleDate + "&strNpScheduleDate2="+npScheduleDate2;
                    form.action=url;
                    form.submit();

                }else{
                    form.txtFechaProceso.value = form.hdnFechaProceso.value;
                    form.txtFechaReconexion.value = form.hdnFechaReconexion.value;
                }
            }
        }
    }

    function fxValidateFormaPagoVEP(objComboFormaPago){
        //INICIO: PRY-1200 | AMENDEZ
        var iSpecificationId=form.cmbSubCategoria.value;
        if(validateSpecificationVep(iSpecificationId)){
        //FIN: PRY-1200 | AMENDEZ

            //INICIO: PRY-0864 | AMENDEZ
            if(objComboFormaPago.value == '<%=Constante.PAYFORM_CARGO_EN_RECIBO%>'){
                alert("REVISE LA ORDEN: Si se trata de VEP, seleccione opción. Luego NO podrá modificarse");
                document.frmdatos.chkVepFlag.disabled = false;
            }
            //FIN: PRY-0864 | AMENDEZ

            if (document.frmdatos.chkVepFlag != undefined){
                if (document.frmdatos.chkVepFlag.checked ==true){
                    if (fxValidateExistItemsVEP(document.frmdatos.cmbVepNumCuotas, 0)){
                        // Valida si existen Items con Modalidad Venta. Si es TRUE no existen
                        if (objComboFormaPago.value != '<%=Constante.PAYFORM_CARGO_EN_RECIBO%>'){
                            document.frmdatos.chkVepFlag.checked = false;
                            document.frmdatos.chkVepFlag.value = 0;
                            document.frmdatos.chkVepFlag.disabled = true;
                            document.frmdatos.cmbVepNumCuotas.selectedIndex = 0;
                            document.frmdatos.cmbVepNumCuotas.disabled = true;
                            //INICIO: PRY-0864 | AMENDEZ
                            document.frmdatos.txtCuotaInicial.value = 0.00;
                            document.frmdatos.txtCuotaInicial.disabled = true;
                            //FIN: PRY-0864 | AMENDEZ

                            //INICIO: PRY-0980 | AMENDEZ
                            document.frmdatos.chkPaymentTermsIQ.checked = false;
                            document.frmdatos.chkPaymentTermsIQ.disabled = true;
                            //FIN: PRY-0980 | AMENDEZ
                        }else{
                            document.frmdatos.chkVepFlag.disabled = false;
                        }
                    }else{
                        //alert("Existen Items con modalidad Venta");
                        document.getElementById(objComboFormaPago.id).selectedIndex  = indexComboFomaPago;
                    }
                }else{
                    if (objComboFormaPago.value != '<%=Constante.PAYFORM_CARGO_EN_RECIBO%>'){

                        document.frmdatos.chkVepFlag.checked = false;
                        document.frmdatos.chkVepFlag.disabled = true;

                        document.frmdatos.cmbVepNumCuotas.selectedIndex = 0;
                        document.frmdatos.cmbVepNumCuotas.disabled = true;
                        //INICIO: PRY-0864 | AMENDEZ
                        document.frmdatos.txtCuotaInicial.value = 0.00;
                        document.frmdatos.txtCuotaInicial.disabled = true;
                        //FIN: PRY-0864 | AMENDEZ

                        //INICIO: PRY-0980 | AMENDEZ
                        document.frmdatos.chkPaymentTermsIQ.checked = false;
                        document.frmdatos.chkPaymentTermsIQ.disabled = true;
                        //FIN: PRY-0980 | AMENDEZ
                    }else{
                        //alert(objComboFormaPago.value);
                        document.frmdatos.chkVepFlag.disabled = false;
                    }
                }
            }
        }
    }

    var indexComboFomaPago;
    function fxSaveIndexComboFormaPago(obj){
        indexComboFomaPago = obj.selectedIndex;
        return indexComboFomaPago;
    }


    //[Despacho en Tienda] RMARTINEZ
    function fxSetValueCourier(chkCourier){
        form  = document.frmdatos;
        if (chkCourier.checked == true){
            chkCourier.value = "1";
            form.hdnChkCourier.value = "1";
        }
        else{
            chkCourier.value = "0";
            form.hdnChkCourier.value = "0";
        }
        //alert(form.hdnChkCourier.value);
    }
    //[CHECK APODERADO - PROY 0787 (VIDD)] JRIOS
    function fxSetValueAssignee(chkAssignee){
        form  = document.frmdatos;
        if (chkAssignee.checked == true){
            chkAssignee.value = "1";
            form.hdnChkAssignee.value = "1";
            getComboAssignee();
            $('#dvAssigneeSection').show();
        }
        else{
            chkAssignee.value = "0";
            form.hdnChkAssignee.value = "0";
            $('#dvAssigneeSection').hide();
        }
    }

    function getComboAssignee(){
        //var companyId = form.txtCompanyId.value;
        //validar por tipo de persona
        var valor;

        DeleteSelectOptions(document.frmdatos.cmbDocTypeAssignee);

        var form=document.frmdatos
        var cmbDocTypeAssignee=$('#cmbDocTypeAssignee');
        var channel    = form.hdnChannelClient.value;

        $.ajax({
            url:"<%= actionURL_DigitalDocumentServlet %>",
            type:"GET",
            async:false,
            cache:false,
            data:{
                "myaction":"getCombo",
                "domain" : "<%= Constante.DOC_TYPE_ASSIGNEE %>",
                "origen" : "<%= Constante.SOURCE_ALL_ID %>",
                "typetrx" : "<%= Constante.TYPE_TRX_ALL_ID %>",
                "channel" : channel,
                "section" : "<%= Constante.SECTION_ASSIGNEE_ID%>"
            },
            dataType:"json",
            success:function(data){
                $.each(data.objArrayList, function (i, item) {
                    cmbDocTypeAssignee.append($("<option />").val(item.npvalue).text(item.npvaluedesc));
                });
            },

            error: function(xhr, status, error) {
                alert(xhr.responseText);
            }
        });

    }

    function getChannel(){
        form = document.frmdatos;
        var txtCustomerId    = form.txtCompanyId.value;

        $.ajax({
            url:"<%= actionURL_DigitalDocumentServlet %>",
            type:"GET",
            async:false,
            cache:false,
            data:{
                "myaction":"getChannel",
                "customerId" : txtCustomerId
            },
            dataType:"json",
            success:function(data){
                var channel=data.strClientType||'';
                if(channel!=''){
                    form.hdnChannelClient.value=channel;
                }else{
                    //alert("Tipo de Documento no disponible");;
                }
            },
            error: function(xhr, status, error) {
                alert(xhr.responseText);
            }
        });
    }

    function fxLoadMaxLenghtDocType(){
        var docTypeSelectedText = $('#cmbDocTypeAssignee').find(":selected").text();
        var lenghtDocType;

        $('#txtDocNumAssignee').val("");

        if(docTypeSelectedText=='DNI'){
            lenghtDocType =<%=Constante.LENGHT_DNI%>;
            $('#txtDocNumAssignee').attr('maxlength', lenghtDocType);
        }
        if(docTypeSelectedText=='CE'){
            lenghtDocType =<%=Constante.LENGHT_CE%>;
            $('#txtDocNumAssignee').attr('maxlength', lenghtDocType);
        }
        if(docTypeSelectedText=='PAS'){
            lenghtDocType =<%=Constante.LENGHT_PAS%>;
            $('#txtDocNumAssignee').attr('maxlength', lenghtDocType);
        }
    }


    function fxShowFlagCourier(lugarDespacho){
        form  = document.frmdatos;
        if (lugarDespacho == '<%=Constante.DISPATCH_PLACE_ID_FULLFILLMENT%>'){
            form.chkCourier.checked = false;
            form.chkCourier.disabled = true;
            form.hdnChkCourier.value = "0";
        }else{
            form.chkCourier.disabled = false;
         fxActiveChkCourier(); //PRY-1093 JCURI
      }
   }
    
   //PRY-1093 JCURI
   function fxActiveChkCourier() {
	   form  = document.frmdatos;
	   
	   var flagActiveChk = <%=activeChkCourier%>;
	   var showCourier='<%=iShowCourier%>';
	   	   
	   if(showCourier == 1) 		   
		   if(flagActiveChk) {			   
			   if(arrDispatchPlacesDelivery.length > 0) {
				   dispatchPlaceId = form.cmbLugarAtencion.value;
		 			for(var i=0; i<arrDispatchPlacesDelivery.length; i++){
		 		        if (arrDispatchPlacesDelivery[i] == dispatchPlaceId ){
		 		        	form.chkCourier.checked = true;
		 			       	form.chkCourier.disabled = false;
		 			       	form.hdnChkCourier.value = "1";
		 			       	return;
		       			}
		 		    }
		 		}
		   }
	}

    //INICIO DERAZO REQ-0940
    function fxDynamicContactSection(dispatchPlaceId){
        form  = document.frmdatos;
        var flagTraceability = parent.mainFrame.flagTraceabilityFunct;

        if(flagTraceability != null){
            //Si esta activo el flag, realizamos las validaciones
            if(flagTraceability == 1){
                var specificationId = form.cmbSubCategoria.value;
                var typeDocument = form.hdnTypeDocument.value;
                var numDocument = form.hdnDocument.value;
                var chkContactNotification = document.getElementById("chkContactNotification");

                //Obtenemos las iniciales del numero de documento para validar en caso de RUC
                var prefixNumDocument = numDocument.substring(0, 2);

                //Realizamos las validaciones para mostrar o no la seccion de contactos
                var resValidation = fxValidateShowContacsSection(specificationId, dispatchPlaceId, typeDocument, prefixNumDocument);

                //Ocultamos o mostramos la seccion de contactos
                displayContactsSection(resValidation);

                if(resValidation){
                    //Actualizamos el valor del hidden para validar o no al grabar la orden
                    form.hdnTracebilityValidation.value = 1;
                    chkContactNotification.checked = true;
                }
                else{
                    form.hdnTracebilityValidation.value = 0;
                    chkContactNotification.checked = false;
                }
            }
        }
    }

    function fxValidateShowContacsSection(specificationId, dispatchPlaceId, typeDocument, numDocument){
        var arrTracSpecifications = parent.mainFrame.arrTracSpecifications;
        var arrTracDispatchPlaces = parent.mainFrame.arrTracDispatchPlaces;
        var arrTracTypeDocuments = parent.mainFrame.arrTracTypeDocuments;
        var arrTracTypeCustomersRUC = parent.mainFrame.arrTracTypeCustomersRUC;
        var validateSpecification = false;
        var validateDispatchPlace = false;
        var validateTypeDocument = false;
        var validateTypeCustomerRUC = false;

        //Realizamos las validaciones si todos los criterios estan configurados
        if(arrTracSpecifications.length > 0 && arrTracDispatchPlaces.length > 0
                && arrTracTypeDocuments.length > 0){

            for(var i=0; i<arrTracSpecifications.length; i++){
                if (arrTracSpecifications[i] == specificationId){
                    validateSpecification = true;
                    break;
                }
            }

            if(!validateSpecification) return false;

            for(var i=0; i<arrTracDispatchPlaces.length; i++){
                if (arrTracDispatchPlaces[i] == dispatchPlaceId ){
                    validateDispatchPlace = true;
                    break;
                }
            }

            if(!validateDispatchPlace) {
            	rspDispatchlist = fxValidateDispatchList(dispatchPlaceId); //JCURI PRY-1093            	
            	return rspDispatchlist;
            }

            for(var i=0; i<arrTracTypeDocuments.length; i++){
                if (arrTracTypeDocuments[i] == typeDocument){
                    validateTypeDocument = true;

                    //Si es RUC, validamos el tipo de cliente
                    typeDocument = typeDocument.toUpperCase();
                    if(typeDocument == 'RUC'){
                        //Si no existe configuracion, no mostramos la seccion de contactos
                        if (arrTracTypeCustomersRUC.length > 0){
                            for(var j=0; j<arrTracTypeCustomersRUC.length; j++){
                                if(arrTracTypeCustomersRUC[j] == numDocument){
                                    validateTypeCustomerRUC = true;
                                    break;
                                }
                            }
                        }
                        else{
                            return false;
                        }
                    }
                    else{
                        validateTypeCustomerRUC = true;
                        break;
                    }
                }
            }

            if(!validateTypeDocument) return false;
            if(!validateTypeCustomerRUC) return false;
        }
        else{
            //Si no existe configuracion para algun criterio, no se muestra la seccion de contactos
            return false;
        }

        return true;
    }
    //FIN DERAZO
    
    //<!--MSOTO NO_000022816 02/06/2014-->
    function fxSearchCmbVendedor(typelist){
        v_form = document.frmdatos;
        v_txtCmbVendedor = v_form.txtCmbVendedor.value;
        //v_form.hdnVendedor.value = v_txtCmbVendedor;
        url = "/portal/pls/portal/!websales.np_contact_attention_pl_pkg.PL_VENDOR_SEARCH?" + "av_typelist=" + typelist + "&av_searchnom=" + escape(v_txtCmbVendedor);
        url = "/portal/pls/portal/websales.npsl_general_pl_pkg.window_frame?av_title=" + escape("Busqueda de compa?ia") + "&av_url=" + escape(url);
        WinAsist = window.open(url,"WinAsist","toolbar=no,location=0,directories=no,status=yes,menubar=0,scrollbars=no,resizable=no,screenX=100,top=80,left=100,screenY=80,width=700,height=500,modal=yes");
    }
       
   //JCURI PRY-1093
   function fxValidateDispatchList(dispatchPlaceId) {
	   form  = document.frmdatos;
	   if (form.chkCourier.checked == false){ 
		   return false;  
	   }
	   if(arrDispatchPlacesDelivery.length > 0) {
 			for(var i=0; i<arrDispatchPlacesDelivery.length; i++){
 		        if (arrDispatchPlacesDelivery[i] == dispatchPlaceId ){
 		        	return true;
 		        }
 		    }
 		}
        return false;
   }
   //JCURI PRY-1093
   function fxChkCourierShowContact(chkCourier){
	   	form  = document.frmdatos;
		fxDynamicContactSection(form.cmbLugarAtencion.value); 	   
   }
 
 
  </script>
  <!-- Fin Data -->

<br>
<table border="0" cellspacing="0" cellpadding="0" width="15%">
    <tr class="PortletHeaderColor">
        <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"></td>
        <td class="SubSectionTitle" align="left" valign="top">Orden&nbsp;<%=strOrderId%></td>
        <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"></td>
    </tr>
</table>

<table  border="0" width="100%" cellpadding="2" cellspacing="1" class="RegionBorder">

    <tr >
        <td class="RegionHeaderColor" width="100%">

            <table border="0" cellpadding="0" cellspacing="1" width="100%" >
                <!-- Primer grupo -->
                <tr>
                    <td class="CellLabel" align="center">&nbsp;<font color="red"><b>*</b></font>&nbsp;<label id="solNumber">NRO. SOLICITUD</label></td>
                    <td class="CellLabel" align="center">Divisi&oacute;n</td>
                    <td class="CellLabel" align="center">Categoría</td>
                    <td class="CellLabel" colspan=2 align="center">Sub Categoría</td>
                    <td class="CellLabel" colspan=2 align="center">
                        <!--MSOTO NO_000022816 02/06/2014-->
                        <div id="lblVendor" >
                            <a id="lnkVendor" href="javascript:fxSearchCmbVendedor(0);">Vendedor</a>
                        </div>
                    </td>
                    <!-- Inicio cambio Data -->
                    <%=strDataSalesmanLabel%>
                    <!-- Fin cambio Data -->
                </tr>
                <tr>
                    <td class="CellContent">&nbsp;<input type="text" name="txtNumSolicitud" size="15" maxlength="15" value="">

                    </td>
                    <td class="CellContent">&nbsp;

                        <select name="cmbDivision" onblur="javascript:fx_fillCategory(this.value);fxShowReasonCdm();javascript:fxCleanSectionsVIDD();fxShowFlagMigration();">
                            <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
                        </select>

                        <input type="hidden" name="hdnNumeroOrder" value="<%=strOrderId%>" >
                        <input type="hidden" name="hdnFlagLoadSolucion" value="0">
                        <input type="hidden" name="hdnSolucionIdAnt" value="">
                        <input type="hidden" name="hdnCurrency" value="">

                    </td>
                    <td class="CellContent">&nbsp;
                        <input type="hidden" name="hdncmbCategoria" value="">
                        <select name="cmbCategoria" onblur="javascript:fx_fillSubCategory(this.value);fxShowReasonCdm();javascript:fxCleanSectionsVIDD();fxShowFlagMigration();" >
                            <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
                        </select>

                        <input type="hidden" name="hdnFlagLoadCategoria" value="0">
                        <input type="hidden" name="hdnCategoriaIdAnt" value="">

                    </td>
                    <td class="CellContent" colspan=2>&nbsp;
                        <input type="hidden" name="hdncmbSubCategoria" value="">
                        <select name="cmbSubCategoria" onblur="javascript: fxValidateTypeOpe(this);fxShowReasonCdm();fxShowFlagMigration();" >
                            <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
                        </select>

                        <input type="hidden" name="hdnSubCategoriaIdAnt" value="">
                        <input type="hidden" name="hdnSubCategoria" value="">
                        <input type="hidden" name="hdnProductType">
                        <input type="hidden" name="hdnSpecification" value="">
                        <input type="hidden" name="hdnSaveOption" value="">

                        <!-- pzacarias -->
                        <input type="hidden" name="hdnFlgViaType" value="">
                        <input type="hidden" name="hdnVerifId" value="">
                        <input type="hidden" id="hdnRowCount" name="hdnRowCount" value="">
                        <!--JCASTILLO SECTION POPULATE CENTER-->
                        <input type="hidden" name="hdnFlgCPUF" value="">
                        <input type="hidden" name="hdnCpufResponse" value="">

                        <!--JCASTILLO SECTION DIGITALIZACION DE DOCUMENTOS-->
                        <input type="hidden" name="hdnFlgSDD" value="">
                        <input type="hidden" name="hdnEmailNullF" value="">
                        <input type="hidden" name="hdnSignatureType" value="">
                        <input type="hidden" name="hdnSignatureReason" value="">
                        <!--SECTION AUTH CONTACT-->
                        <input type="hidden" name="hdnFlgSAC" value="">

                        <!--JVERGARA VERIFICACION DE IDENTIDAD AISLADA  -->
                        <input type="hidden" name="hdnFlgVIA" value="">

                        <input type="hidden" name="hdnStatusVal" value="">

                        <input type="hidden" name="hdnChkAssignee" value="">
                        <input type="hidden" name="hdnChannelClient" value="">
                        <input type="hidden" name="hdnCmbDocTypeAssigneeVal" value="">
                        <input type="hidden" name="hdnCmbDocTypeAssigneeText" value="">

                        <input type="hidden" name="hdnClientDocType" value="">



                    </td>
                    <td class="CellContent" colspan=2>&nbsp;
                        <input type="hidden" name="hdncmbVendedor" value="">
                        <input type="hidden" name="hdnVendedorId" value="<%=strSalesId%>">
                        <input type="hidden" name="hdnOrderCreator" value="">

                        <select name="cmbVendedor" style="display:none" onchange="fxChangeSalesMan(this,<%=iUserId%>,<%=iAppId%>);">
                            <!-- MSOTO 03-06-2014 SAR N_O000022816 txtCmbVendedor reemplaza al cmbVendedor -->
                            <%
                                String strMessage0 = null;
                                String strSalesName = null;
                                if ( "".equals(strSalesId) ){
                                    strSalesName="";
                                }else{
                                    HashMap hshData = new HashMap();
                                    GeneralService objGenServ = new GeneralService();
                                    hshData = objGenServ.getSalesmanName(Long.parseLong(strSalesId));
                                    strMessage0=(String)hshData.get("strMessage");
                                    if (strMessage0!=null)
                                        throw new Exception(strMessage0);
                                    strSalesName=(String)hshData.get("strName");}
                            %>
                        </select>

                        <% if ( strGeneratortype == "OPP" || strGeneratortype == "OPP_PORTAB" ){ %>
                        <!-- MSOTO 03-06-2014 SAR N_O000022816 txtCmbVendedor reemplaza al cmbVendedor -->
                        <input type="text" name="txtCmbVendedor" style="width: 80%;" value="<%=strSalesName%>" disabled="disabled" />
                        <script>
                            $(document).ready(function(){
                                $("#lblVendor").html("");
                                //$("#lblVendor").text("Vendedor");
                                $("#lblVendor").html("<label>Vendedor</label>");
                            });
                        </script>
                        <%}else{%>
                        <!--- MSOTO 03-06-2014 SAR N_O000022816 txtCmbVendedor reemplaza al cmbVendedor-->
                        <input type="text" name="txtCmbVendedor" style="width: 80%;" value="<%=strSalesName%>" disabled="disabled" />
                        <script>
                            $(document).ready(function(){
                                $("#lblVendor").html("");
                                //$("#lblVendor").text("Vendedor");
                                //$("#lnkVendor").attr("href",'href="javascript:fxSearchCmbVendedor(0)"');
                                $("#lblVendor").html("<a href='javascript:fxSearchCmbVendedor(0)'>Vendedor</a>");
                            });
                        </script>
                        <%}%>

                        <%--CGC COR0407--%>
                        <script>
                            var v_salemanid="<%=strSalesId%>";
                            if (v_salemanid != "")
                                AddNewOption(document.frmdatos.cmbVendedor,"<%=strSalesId%>","<%=strSalesName%>");
                            document.frmdatos.cmbVendedor.value="<%=strSalesId%>";
                        </script>

                        <input type="hidden" name="hdnAlcanceExcluCC" value="wv_AlcanceExcluCC">
                        <input type="hidden" name="hdnAlcanceExcluAC" value="wv_AlcanceExcluAC">
                        <input type="hidden" name="hdnCustAlcanceExclusividad" value="">
                        <input type="hidden" name="hdnVendedor" value="">

                    </td>
                    <!-- Inicio Data -->
                    <input type="hidden" name="hdncmbVendedorData" value="">
                    <input type="hidden" name="hdnVendedorDataId" value="">
                    <input type="hidden" name="hdnDataFieldsVisibles" value="">
                    <input type="hidden" name="hdncmbVendedorVoz" value="">
                    <input type="hidden" name="hdnLoadSellerData" value="">
                    <%=strDataSalesmanField%>
                    <!-- Fin Data -->
                </tr>
                <!-- Grupo Auxiliar -->

                <tr>
                    <td class="CellLabel" align="center">&nbsp;</td>
                    <td class="CellLabel">
                        &nbsp;<input type="hidden" name="txtEstadoOrden" size="15" maxlength="20" value="Pendiente" readonly>
                        <span id="txtLblPaymentResp" style="display:none">Resp. de Pago</span>
                    </td>
                    <td class="CellLabel" align="center" colspan="3" >
                        <input type="text" name="txtPaymentRespDesc" size="60" value="" readonly style="display:none">
                        <input type="hidden" name="hdnPaymentRespId">
                    </td>

                    <td class="CellLabel" align="center">Dealer</td>
                    <td class="CellContent">
                        &nbsp;<input type="text" name="txtDealer" size="12" maxlength="20" value="" readOnly>
                        <input type="hidden" name="hdnDealer" size="12" maxlength="20" value="">
                    </td>
                    <!-- Inicio Data -->
                    <td class="CellContent">&nbsp;</td>
                    <!-- Fin Data -->
                </tr>
                <!-- Segundo grupo -->


                <tr>
                    <td class="CellLabel" align="center">&nbsp;<font color="red"><b>*</b></font>&nbsp;Lugar Despacho</td>

                    <td class="CellLabel" align="left">
                        <div id="dvCourier" style=display:'none'>&nbsp;&nbsp;Entrega por Courier </div>
                    </td>


                    <td class="CellLabel" align="center">Region Tramite</td>
                    <td class="CellLabel" align="center">Tienda</td>
                    <td class="CellLabel" align="center">Firma fecha Hora</td>
                    <td class="CellLabel" align="center">Estado&nbsp;Proc.&nbsp;Aut.</td>
                    <td class="CellLabel" align="center">
                        <div id="dvFechaProcAutN" style=display:''><a href="javascript:show_calendar('frmdatos.txtFechaProceso',null,null,'DD/MM/YYYY');" name="linkCalendar1">Fecha&nbsp;Proc.&nbsp;Aut.</a></div><!--dlazo-->
                        <div id="dvFechaProcAutP" style=display:'none'><a name="linkCalendar1">Fecha&nbsp;Proc.&nbsp;Aut.</a></div> <!--dlazo-->
                    </td>
                    <td class="CellLabel" align="center"> <div id="dvFechaFinProg" style=display:'none'><a href="javascript:show_calendar('frmdatos.txtFechaFinProg',null,null,'DD/MM/YYYY');" name="linkCalendar">Fecha&nbsp;Fin&nbsp;Prog.</a></div></td> <!--mlopezl - modif hpptt # 1 - 30/06/2010-->
                    <td class="CellLabel" align="center"> <div id="dvFechaReconexion" style=display:'none'><a href="javascript:show_calendar('frmdatos.txtFechaReconexion',null,null,'DD/MM/YYYY');" name="linkCalendar">Fecha&nbsp;Proc.&nbsp;Reconex.</a></div></td>
                    <!-- Inicio Data >
                    <td class="CellLabel">&nbsp;</td>
                    <Fin Data -->

                </tr>
                <tr>
                    <td class="CellContent">&nbsp;
                      <!--DERAZO REQ-0940-->
                      <select name="cmbLugarAtencion" onchange="javascript:fxShowFlagCourier(this.value);fxDynamicContactSection(this.value);">
                            <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
                        </select>
                        <input type="hidden" name="hdnWorkflowType" size="10" value="">
                        <input type="hidden" name="txtPedidoId" value="wn_PedidoId">
                        <input type="hidden" name="hdnLugarDespachoType" value="">
                        <input type="hidden" name="hdnLugarDespacho" value="">
                    <!--DERAZO REQ-0940-->
                    <input type="hidden" name="hdnTracebilityValidation" value="0">
                    </td>
                    <%
                        String strTienda = "";
                        String strRegionTramite = "";
                        int intRegionTramiteCodigo = 0;
                        //HashMap hashBuildingName = new HashMap();
                        //pe.com.nextel.test.ErrorMessage  objErrorMessage = new pe.com.nextel.test.ErrorMessage(strMessage5);
                        HashMap hashBuildingName = new HashMap();

                        //hashBuildingName = NewOrderService.OrderDAOgetBuildingName( 21,"DTEODOSIO");

                        hashBuildingName = NewOrderService.OrderDAOgetBuildingName(portalSessionBean3.getBuildingid(),portalSessionBean3.getLogin());

                        hashBuildingName = (HashMap)hashBuildingName.get("hshData");
                        System.out.println("hashBuildingName"+hashBuildingName);
                        if( hashBuildingName!=null){
                            if (hashBuildingName.size() != 0 ) {
                                strTienda               = MiUtil.getString((String)hashBuildingName.get("wv_name"));
                                strRegionTramite        = MiUtil.getString((String)hashBuildingName.get("wv_regionname"));
                                //strRegionId = "122";
                                if ( strRegionId.equals("") || strRegionId == null)
                                    intRegionTramiteCodigo  = MiUtil.parseInt((String)hashBuildingName.get("wn_regionid"));
                                else
                                    intRegionTramiteCodigo = MiUtil.parseInt(strRegionId);
                                System.out.println("[intRegionTramiteCodigo]"+intRegionTramiteCodigo);
                            }
                        }

                        //JLIMAYMANTA
                        String strTypeService="";
                        HashMap hashBuildingTS = new HashMap();
                        hashBuildingTS = NewOrderService.OrderDAOgetBuildingTS(portalSessionBean3.getBuildingid(),portalSessionBean3.getLogin());
                        hashBuildingTS = (HashMap)hashBuildingTS.get("hshDataTE");
                        System.out.println("hashBuildingTS"+hashBuildingTS);
                        if( hashBuildingTS!=null){
                            if (hashBuildingTS.size() != 0 ) {
                                strTypeService               = MiUtil.getString((String)hashBuildingTS.get("wv_typeservice"));
                                System.out.println("[strTypeService]"+strTypeService);
                            }
                            if(strTypeService==null){
                                strTypeService="";
                            }
                        }

                    %>
				  <%//JCURI PRY-1093 %>
                  <td class="CellContent"> <div id="dvChkCourier" style=display:'none'> &nbsp;<input type="checkbox" name="chkCourier" value ="0" onclick="fxSetValueCourier(this);fxChkCourierShowContact(this);" <%=strChecked%>></div></td>
                  <input type="hidden" name="hdnChkCourier" value="<%=activeChkCourier%>">
                    <td class="CellContent">&nbsp;<input type="text" name="txtRegionTramite" size="18" maxlength="20" value="<%=strRegionTramite%>" readonly></td>
                    <input type="hidden" value="<%=intRegionTramiteCodigo%>" name="hdnRegionId" >
                    <input type="hidden" value="<%=strTypeService%>" name="hdnTypeService" >
                    <input type="hidden" value="<%=portalSessionBean3.getBuildingid()%>" name="hdnTienda" >

                    <td class="CellContent">&nbsp;<input type="text" name="txtTienda" size="18" maxlength="20" value="<%=strTienda%>" readonly></td>
                    <%
                        java.util.Date f = new java.util.Date();
                        java.util.Date dFechaActual = new java.util.Date(f.getTime());

                        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                    %>
                    <td class="CellContent">&nbsp;<input type="text" name="txtFirmaFechaHora" size="20" maxlength="20" value="<%=formateador.format(dFechaActual)%>" readonly></td>
                    <td class="CellContent">&nbsp;<input type="text" name="txtEstadoProcAutom" value="Pendiente" size="10" maxlength="30"></td>
                    <td class="CellContent" align="center">&nbsp;<input type="text" id="txtFechaProgAuto" name="txtFechaProceso" value="" size="10" maxlength="10" onchange="CheckDate(this);" onBlur="if(form.cmbSubCategoria.value == <%=Constante.SPEC_SUSPENSIONES[0]%>){calcularFechaReconex(); fxValidaDiasSuspension();}"></td>
                    <input type="hidden" value="" name="hdnFechaProceso" >
                    <!--td class="CellContent"><a href="javascript:show_calendar('frmdatos.txtFechaProceso',null,null,'DD/MM/YYYY');"></a></td-->
                    <!-- mlopezl - modif hpptt # 1 - 30/06/2010 - inicio-->
                    <td class="CellContent" align="center"><div id="dvFechaFinProgInput" style=display:'none'> &nbsp;<input type="text" name="txtFechaFinProg" value="" size="10" maxlength="10" onBlur="fxValidateFechaFin(this);"></div></td>
                    <input type="hidden" value="" name="hdnFechaFinProg" >
                    <!-- mlopezl - modif hpptt # 1 - 30/06/2010 - fin-->

                    <td class="CellContent" align="center"><div id="dvFechaReconexionInput" style=display:'none'> &nbsp;<input type="text" name="txtFechaReconexion" value="" size="10" maxlength="10" onBlur="calcularFechaReconex();fxValidateFecha(this); if(form.cmbSubCategoria.value == <%=Constante.SPEC_SUSPENSIONES[0]%>){ fxValidaDiasSuspension();}"></div></td>
                    <input type="hidden" value="" name="hdnFechaReconexion" >
                    <!-- Fin Data -->
                </tr>

                <!-- Tercer grupo -->
                <tr>
                    <td class="CellLabel" align="center">&nbsp;<font color="red"><b>*</b></font>&nbsp;Forma de Pago</td>
                    <td class="CellLabel" align="center">Estado de Pago</td>
                    <td class="CellLabel" align="center"><a href="javascript:show_calendar('frmdatos.txtFechaProbablePago',null,null,'DD/MM/YYYY');">&nbsp;Fecha Pago&nbsp;</a></td>
                    <td class="CellLabel" align="center">Propuesta</td>
                    <td class="CellLabel" align="center"><%=MiUtil.getString(strParentName)%></td>
                    <td class="CellLabel" colspan=4 align="center">&nbsp;<span id="spnReasonCdm" style="display:none">Motivo de Cambio de Modelo</span></td>
                </tr>
                <tr>
                    <td class="CellContent">&nbsp;
                        <select id="cmbFormaPago" name="cmbFormaPago" onclick="fxSaveIndexComboFormaPago(this);" onchange="fxValidateFormaPagoVEP(this);"> <!--onchange="javascript:fValidarFormaPago(this);"-->
                            <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
                        </select>
                        <input type="hidden" name="hdnFormaPagoOrig" value="">
                    </td>
                    <td class="CellContent">&nbsp;<input type="text" name="txtEstadoPago" size="15" maxlength="20" value="Pendiente" readOnly></td>
                    <td class="CellContent">&nbsp;<input type="text" name="txtFechaProbablePago" value="" size="18" maxlength="10" onBlur="CheckDate(this)">&nbsp;</td>
                    <td class="CellContent">
                        <div style="width:100%;">
                            <div style="float:left;width:30%;text-align:center;"><input type="text" name="txtPropuesta" readonly value="" size="18" maxlength="10" onBlur=""></div>
                            <div style="float:right;width:30%;text-align:center;"><a href="javascript:openPopUpProposedList()"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/viewDetail.gif" alt="Propuestas" width="15" height="15" border="0"></a></div>
                        </div>
                    </td>
                    <%
                        if (  !MiUtil.getString(strGeneratorid).equals("") && !MiUtil.getString(strParentUrl).equals("") ){
                    %><td class="CellContent" align="center"><a href="javascript:fxShowParent('<%=strGeneratorid%>','<%=strParentUrl%>')"><%=strGeneratorid%></a></td><%
                }else{
                %><td class="CellContent">&nbsp;</td><%
                    }
                %>

                    <td class="CellContent" colspan=4>&nbsp;
                    	<select id="cmbReasonCdm" name="cmbReasonCdm" style="display:none;"> 
                            <option value="">&nbsp;</option>
                          <% 
                           HashMap objHashMapChangeModelReason = new HashMap();
                           
                           objHashMapChangeModelReason = objGeneralService.GeneralDAOgetComboList("CHANGE_MODEL_REASON");
                           if( objHashMapChangeModelReason.get("strMessage") != null ){ %>
                           <script>alert("<%=objHashMapChangeModelReason.get("strMessage")%>")</script>
                           <%}else{
                           ArrayList arrListChangeModelReason = (ArrayList)objHashMapChangeModelReason.get("objArrayList");
                           if ( arrListChangeModelReason != null ){
                           
                               for ( int i=0; i<arrListChangeModelReason.size(); i++ ){
                                    HashMap h = (HashMap)arrListChangeModelReason.get(i); %>
                                    
                                    <option value='<%=h.get("wn_npvalue")%>'>
                                    <%=h.get("wv_npvaluedesc")%>
                                    </option> <%
                              }
                           }
                           }
                         %>
                        </select>

                      <!-- TDECONV003 - KOTINIANO-->
                      <div id="divFlagMigration" style="display: none">
                          <span  style="font-weight: bold;color: #000000; padding: 3px 5px; margin-right: 5px;"> Migración Prepago a Postpago</span>
                          <input type="checkbox" name="chkFlagMigration" onclick="changeFlagMigration(this)" /><input type="hidden" name="hdnFlagMigration"/>
                      </div>
		  
                 </td>
                </tr>

                <!-- Inicio [CHECK APODERADO - PROY 0787] JRIOS -->


                <tr >
                    <td class="CellLabel"><div id="dvAssignee" style="display:none">&nbsp;Apoderado&nbsp;</div></td>
                    <td class="CellLabel" colspan="7"><div id="dvAssigneeNextCell" style=display:'none'>&nbsp;</div></td>
                </tr>
                <tr>
                    <td class="CellContent" align="left"> <div id="dvChkAssignee" style=display:'none'>&nbsp;<input type="checkbox" name="chkAssignee" value ="0" onclick="fxSetValueAssignee(this)"></div></td>

                    <td class="CellContent" colspan="7"><div id="dvChkAssigneeNextCell" style=display:'none'>&nbsp;</div></td>
                </tr>

                <!-- Fin  [CHECK APODERADO - PROY 0787] JRIOS -->

                <!-- Cuarto grupo -->

                <tr >
                    <td class="CellLabel" align="center">&nbsp;Descripci&oacute;n&nbsp;</td>
                    <td class="CellLabel" colspan="7">&nbsp;</td>
                </tr>
                <tr>
                    <td class="CellContent" colspan="8">&nbsp;<textarea name="txtDetalle" cols="145" rows="7"></textarea></td>
                </tr>



                <!--JPEREZ - INICIO -->
                <!-- Campos para excepciones  -->
                <input type="hidden" name="hdnExceptionInstallation" value="">
                <input type="hidden" name="hdnExceptionPrice" value="">
                <input type="hidden" name="hdnExceptionPlan" value="">
                <input type="hidden" name="hdnExceptionWarrant" value="">
                <input type="hidden" name="hdnExceptionRevenue" value="">
                <input type="hidden" name="hdnExceptionRevenueAmount" value="">
                <input type="hidden" name="hdnExceptionBillCycle" value="">
                <input type="hidden" name="hdnExceptionTotal" value="">
                <!-- Orden Modificada  -->
                <input type="hidden" name="hdnChangedOrder" value="N"/>
                <!--JPEREZ - FIN -->
                <input type="hidden" name="hdnSalesStructOrigenId" value="<%=iSalesStructOrigenId%>"/>

            </table>

            <!-- Cerrar -->
</table>

<!-- INICIO SECCION DE CENTRO POBLADO DE USO FRECUENTE JCASTILLO PRY-0787-->
<div id="dvPopulateCenter" style="display: none">
<br>
    <table border="0" cellspacing="0" cellpadding="0" width="25%">
        <tr class="PortletHeaderColor">
            <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"></td>
            <td class="SubSectionTitle" align="left" valign="top"><%=Constante.SECTION_POPULATE_CENTER%></td>
            <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"></td>
        </tr>
    </table>
    <table  class="RegionBorder" border="0" width="100%" cellpadding="2" cellspacing="1">

        <tr>
            <td class="RegionHeaderColor" width="100%">

                <table border="0" cellpadding="0" cellspacing="1" width="100%" style="text-align: left;">
                    <tr>
                        <td class="CellContent" width="15%"><input type="radio" name="cpufresponse" id="cpufResponse0" value="0" checked="checked">No tengo CPUF
                        </td>
                        <td class="CellContent" width="15%"><input type="radio" name="cpufresponse" value="1" id="cpufResponse1">No conozco CPUF
                        </td>
                        <td class="CellContent" width="60%"><input type="button" onclick="fxShowPopulateCenterDet()" value="Ingresar CPUF"/></td>
                    </tr>

                </table>

            </td>
        </tr>
    </table>
</div>
<!-- Inicio [SECCION APODERADO - PROY 0787] JRIOS -->

<div id="dvAssigneeSection" style="display:'none';">
    <br>
    <table border="0" cellspacing="0" cellpadding="0" width="25%">
        <tr class="PortletHeaderColor">
            <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"></td>
            <td class="SubSectionTitle" align="left" valign="top"><%=Constante.SECTION_ASSIGNEE%></td>
            <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"></td>
        </tr>
    </table>

    <table  border="0" width="100%" cellpadding="2" cellspacing="1" class="RegionBorder">

        <tr >
            <td class="RegionHeaderColor" width="100%">

                <table border="0" cellpadding="0" cellspacing="1" width="100%" style="text-align: left;">

                    <tr>
                        <td class="CellLabel" width="15%"><FONT color=#ff0000><B>*</B></FONT>Tipo de Documento</td>
                        <td class="CellContent" width="25%">

                            <select id="cmbDocTypeAssignee" name="cmbDocTypeAssignee" onblur="javascript:fxLoadMaxLenghtDocType();">
                            <option value=""></option>
                        </select></td>
                        <td class="CellLabel" width="15%"><FONT color=#ff0000><B>*</B></FONT>N&uacute;mero de documento</td>
                        <td class="CellContent" width="45%"><input type="text" id="txtDocNumAssignee" name="txtDocNumAssignee" size="25"></td>
                    </tr>

                    <tr>
                        <td class="CellLabel" width="15%"><FONT color=#ff0000><B>*</B></FONT>Nombres</td>
                        <td class="CellContent" width="25%" ><input type="text" name="txtFirstNameAssignee" maxLength="100" style="width: 100%" ></td>
                        <td class="CellContent" width="60%" colSpan="2">&nbsp;</td>
                    </tr>
                    <tr>
                        <td class="CellLabel" width="15%"><FONT color=#ff0000><B>*</B></FONT>Apellido Paterno</td>
                        <td class="CellContent" width="25%" ><input type="text" name="txtLastNameAssignee" maxLength="100" style="width: 100%" ></td>
                        <td class="CellContent" width="60%" colSpan="2">&nbsp;</td>
                    </tr>
                    <tr>
                        <td class="CellLabel" width="15%"><FONT color=#ff0000><B>*</B></FONT>Apellido Materno</td>
                        <td class="CellContent" width="25%" colSpan="1"><input type="text" name="txtFamilyNameAssignee" maxLength="100" style="width: 100%" ></td>
                        <td class="CellContent" width="60%" colSpan="2">&nbsp;</td>
                    </tr>
                </table>

            </td>
        </tr>
    </table>
</div>

<!-- Fin [SECCION APODERADO - PROY 0787] JRIOS -->

<!-- INICIO SECCION DE DIGITALIZACION DE DOCUMENTOS JCASTILLO PRY-0787-->
<div id="dvDigitalizacionDocumentos" style="display: none">
    <br>
    <table border="0" cellspacing="0" cellpadding="0" width="25%">
        <tr class="PortletHeaderColor">
            <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"></td>
            <td class="SubSectionTitle" align="left" valign="top"><%=Constante.SECTION_DIGIT_DOCUMENT%></td>
            <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"></td>
        </tr>

    </table>

    <table  class="RegionBorder" border="0" width="100%" cellpadding="2" cellspacing="1">

        <tr>
            <td class="RegionHeaderColor" width="100%">

                <table border="0" cellpadding="0" cellspacing="1" width="100%" style="text-align: left;">
                    <tr>
                        <td class="CellLabel" width="15%"><font color="red"><b>*</b></font>Tipo de Firma</td>
                        <td class="CellContent" width="25%"><select name="cmbSignature"  id="cmbSignature" onchange="updateSection()"/>
                        </td>
                        <td class="CellContent" width="60%"><a href="javascript:fxShowDocumentsToGenerate()" id="linkDocumentsToGenerate">Visualizar Documentos a digitalizar</a></td>
                    </tr>

                    <tr id="rowDinamico">
                    </tr>

                </table>

            </td>
        </tr>
    </table>
</div>



<div id="dvVerificacionIdentidadAislada" style="display:'none';" >    <!--  style="display: none" -->
    <br>
    <table border="0" cellspacing="0" cellpadding="0" width="25%">
        <tr class="PortletHeaderColor">
            <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"></td>
            <td class="SubSectionTitle" align="left" valign="top">Verificaci&oacute;n de identidad aislada</td>
            <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"></td>
        </tr>
    </table>

    <table  border="0" width="100%" cellpadding="2" cellspacing="1" class="RegionBorder">

        <tr >
            <td class="RegionHeaderColor" width="100%">

                <table border="0" cellpadding="0" cellspacing="1" width="100%" style="text-align: center;">
                    <thead>
                    <tr>
                        <td class="CellLabel">Seleccione</td>
                        <td class="CellLabel">Tipo de documento</td>
                        <td class="CellLabel">N&uacute;mero de documento</td>
                        <td class="CellLabel">Nombres y Apellidos</td>
                        <td class="CellLabel">Fecha de vencimiento</td>
                    </tr>
                    </thead>
                    <tbody id="dataIsolatedVerification">
                    <tr>
                        <td class="CellContent" colspan="5">No se encontraron registros</td>
                    </tr>
                    </tbody>
                </table>

            </td>
        </tr>
    </table>
    <br>
</div>


<!-- Inicio Data -->
<script defer>
    fxHideDataFields();
</script>
<!-- Fin Data -->

<!--  Hidden que indica el # de grupos de item_pedido ingresados -->
<!-- DATOS DEL PEDIDO - FIN -->
<%}catch(SessionException se) {
    System.out.println("[JP_ORDER_NEW_ORDER][SessionException] : " + se.getClass() + " - " + se.getMessage());
    se.printStackTrace();
    //out.println("<script>alert('Su sesión ha finalizado. Ingrese nuevamente');</script>");
    //String strUrl = "/portal/pls/portal/WEBSALES.NP_USER_AVAILABILITY_PL_PKG.PL_LOGOUT";
    //out.print("<script>parent.mainFrame.location.replace('"+strUrl+"');</script>");
}catch(Exception e) {
    String strMessageExceptionGeneralStart = "";
    strMessageExceptionGeneralStart = MiUtil.getMessageClean(e.getMessage());
    System.out.println("[JP_ORDER_NEW_ORDER][Exception] : " + e.getClass() + " - " + e.getMessage());
    out.println("<script>alert('"+strMessageExceptionGeneralStart+"');</script>");
}
%>
