<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="java.util.*"%>
<%@ page import="pe.com.nextel.service.*" %>
<%@ page import="pe.com.nextel.bean.*" %>
<%
    System.out.println("*********************************************PopUpOrder.jsp*********************************************");
    try{
        String    objectType  = request.getParameter("objectType")==null?"":(String)request.getParameter("objectType");
        String    item_index  = request.getParameter("item_index")==null?"0":(String)request.getParameter("item_index");
        String    type_window = MiUtil.getString(request.getParameter("type_window"));
        String    objTypeEvent= MiUtil.getString(request.getParameter("objTypeEvent"));
        String    strFieldReadOnly = request.getParameter("strFieldReadOnly")==null?"":(String)request.getParameter("strFieldReadOnly"); //CEM COR0323
        String    strItemId   = request.getParameter("strItemId");
        String  strnpnumcuotas = request.getParameter("strnpnumcuotas");
        String  strflagvep = request.getParameter("strflagvep");

        //INICIO: PRY-0864 | AMENDEZ
        String  strInitialQuota      =   MiUtil.getString(request.getParameter("strInitialQuota"));
        //FIN: PRY-0864 | AMENDEZ

        //INICIO: PRY-0980 | AMENDEZ
        String  strNpPaymentTermsIQ      =   MiUtil.getString(request.getParameter("strNpPaymentTermsIQ"));
        //FIN: PRY-0980 | AMENDEZ

        //INICIO: AMENDEZ | PRY-1049
        String  strHdnCobertura =  MiUtil.getString(request.getParameter("strHdnCobertura"));
        //FIN: AMENDEZ | PRY-1049

        request.setAttribute("type_window",type_window);
        request.setAttribute("objTypeEvent",objTypeEvent);
        request.setAttribute("strItemId",strItemId);
        NewOrderService objNewOrderServicePopUp = new NewOrderService();
        GeneralService  objGeneralService = new GeneralService();
        //[TDECONV003-1] EFLORES 01/09/2017
        HashMap tdeSwitchValidation = objGeneralService.getStatusByTable("SWITCH_FUNCIONALIDAD","TDE_VAL_NUMTEL_EQUIP_PROPIO");
        String tdeValNumTelEquiProp = (String)tdeSwitchValidation.get("strStatus");
        HashMap tdeSwitchValidation2 = objGeneralService.getStatusByTable("SWITCH_FUNCIONALIDAD","TDE_REMOVE_VAL_EQUIP_ALQUILER");
        String tdeValEquipAlquiler = (String)tdeSwitchValidation2.get("strStatus");
        //[TDECONV003-1] FIN

        //INICIO DERAZO TDECONV003-2
        HashMap tdeHashMapValidImeiFS = objGeneralService.getStatusByTable("TDECONV003_IMEI_FS","TDE_IMEI_FULLSTACK_PROPIO");
        String tdeValImeiFS = (String)tdeHashMapValidImeiFS.get("strStatus");

        String tdeLengthImeiFS = Constante.LENGTH_IMEI_FS;
        HashMap tdeHashMapLengthImeiFS = objGeneralService.getValueTag1("TDE_LENGTH_IMEI_FULLSTACK_PROPIO","TDECONV003_LENGTH_IMEI_FS");
        if(tdeHashMapLengthImeiFS.get("strMessage") == null){
            tdeLengthImeiFS = (String)tdeHashMapLengthImeiFS.get("strTag1");
        }
        else{
            System.out.println("[TDECONV003-2] Error al obtener tamaño de IMEI FS: "+(String)tdeHashMapLengthImeiFS.get("strMessage"));
        }
        //FIN DERAZO

        //[TDECONV003-6] INI PCACERES
        HashMap tdeSwitchValidation3 = objGeneralService.getStatusByTable("TDECONV003_6_IMEI_FS","TDE_VAL_IMEI_FS_FLAG");
        String flagValImeiFS = (String)tdeSwitchValidation3.get("strStatus");
        //[TDECONV003-6] FIN

        //[TDECONV003-8] INI PZACARIAS
        HashMap tdeSwitchValidation4 = objGeneralService.getStatusByTable("TDECONV003-8","TDE_VAL_NUMTEL_SIM");
        String tdeValNumTelSIM = (String)tdeSwitchValidation4.get("strStatus");
        HashMap tdeSwitchValidation5 = objGeneralService.getStatusByTable("TDECONV003-8","TDE_VAL_NUMTEL_MSISDN");
        String tdeValNumTelMSISDN = (String)tdeSwitchValidation5.get("strStatus");
        HashMap tdeSwitchValidation6 = objGeneralService.getStatusByTable("TDECONV003-8","TDE_VAL_NUMTEL_TIP_DOC");
        String tdeValNumTelTipDoc = (String)tdeSwitchValidation6.get("strStatus");

        String strRutaContext=request.getContextPath();
        String strURLItemServlet =strRutaContext+"/itemServlet";
        //[TDECONV003-8 FIN

        //[PRY-0710] Se añade el servicio para obtener el detalle de la orden
        EditOrderService objEditOrderServicePopup = new EditOrderService();
        HashMap hshOrderDetail = null;
        //[PRY-0710]
        Hashtable hshtinputNewSection = new Hashtable();
        HashMap hshValidateStock   = new HashMap();
        HashMap hshValidateSimImei = new HashMap();
        String    strCodigoCliente= "",
                strnpSite ="",
                strCodBSCS = "",
                hdnSpecification = "",
                strTypeCompany = "",
                strDivision = "",
                strStockFlag = "",
                strMessage   = "",
                strStatus = "",
                //JOYOLA, 06/03/2008, se agrego variable strSiteOppId
                strSiteOppId = "",
                strUnknwnSiteId = "",
                strGeneratorType = "",
                strSessionId = "",
                strGeneratorId = "",
             /*JPEREZ: Se agrega strDispatchPlace*/
                strDispatchPlace = "",
                strOrderId = "",
                strSimImei = "",
                strMessageSimImei = "",
                strSalesStuctOrigenId="",
            //[PRY-0710] Agrega rol modificar producto
                strModProd = "";


        String strTypeDocument = ""; // PRY-0762 JQUISPE
        String strDocument = ""; //PRY-0762 JQUISPE

        boolean isRentaAdelantada = false; //PRY-0762 JQUISPE
        String strPrecioPlan = ""; //PRY-0762 JQUISPE

        strCodigoCliente = (String)request.getParameter("strCustomerId");
        hdnSpecification = (String)request.getParameter("strSpecificationId");
        strnpSite        = (String)request.getParameter("strSiteId");
        strCodBSCS       = (String)request.getParameter("strCodBSCS");
        strDivision      = (String)request.getParameter("strDivisionId");
        strTypeCompany   = (String)request.getParameter("strTypeCompany");
        strStatus        = (String)request.getParameter("strStatus");
        strSiteOppId     = MiUtil.getString(request.getParameter("strSiteOppId"));
        strUnknwnSiteId  = MiUtil.getString(request.getParameter("strUnknwnSiteId"));
        strGeneratorType = (String)request.getParameter("strGeneratorType");
        strSessionId     = (String)request.getParameter("strSessionId");
        strGeneratorId   = (String)request.getParameter("strGeneratorId");
        strOrderId       = (String)request.getParameter("strOrderId");
        strSalesStuctOrigenId=(String)request.getParameter("strSalesStuctOrigenId");
        strDispatchPlace = (String)request.getParameter("strDispatchPlace");
        strModProd = (String)request.getParameter("strModProd");

     	// PRY-0762 JQUISPE
        strTypeDocument = (String)request.getParameter("strTypeDocument");
        strDocument = (String)request.getParameter("strDocument");
        
        System.out.println("[PopUpOrder][strGeneratorType]"+strGeneratorType);
        System.out.println("[PopUpOrder][strGeneratorId]"+strGeneratorId);
        System.out.println("[PopUpOrder][strOrderId]"+strOrderId);
        System.out.println("[PopUpOrder][hdnSpecification]"+hdnSpecification);
        System.out.println("[PopUpOrder][strDivision]"+strDivision);
        System.out.println("[PopUpOrder][type_window]"+type_window);
        System.out.println("[PopUpOrder][strCodigoCliente]"+strCodigoCliente);
        System.out.println("[PopUpOrder][strnpSite]"+strnpSite);
        System.out.println("[PopUpOrder][strnpnumcuotas]"+strnpnumcuotas);
        System.out.println("[PopUpOrder][strflagvep]"+strflagvep);
        System.out.println("[PopUpOrder][strDispatchPlace]"+strDispatchPlace);

        //INICIO: PRY-0864 | AMENDEZ
        System.out.println("[PopUpOrder][strInitialQuota]"+strInitialQuota);
        System.out.println("[PopUpOrder][strTypeCompany]"+strTypeCompany);
        //FIN: PRY-0864 | AMENDEZ

        //INICIO: PRY-0980 | AMENDEZ
        System.out.println("[PopUpOrder][strNpPaymentTermsIQ]"+strNpPaymentTermsIQ);
        //FIN: PRY-0980 | AMENDEZ

        // PRY-0762 JQUISPE
        System.out.println("[PopUpOrder][strTypeDocument]"+strTypeDocument);
        System.out.println("[PopUpOrder][strDocument]"+strDocument);

        //-------INICIO: AMENDEZ | PRY-1049
        System.out.println("[PopUpOrder][strHdnCobertura]"+strHdnCobertura);
        //-------FIN: AMENDEZ | PRY-1049


        //[PRY-0710] Obtiene Detalle de la Orden
        int intModProd = MiUtil.parseInt(strModProd);
        String strPaymentTermsModProd ="";
        System.out.println("[PRY-0710]["+strOrderId+"][PopUpOrder][intModProd]"+intModProd);
        if(intModProd == 1){ // Tiene Permiso
            hshOrderDetail = objEditOrderServicePopup.getOrder(MiUtil.parseLong(strOrderId));
            String msg = (String)hshOrderDetail.get("strMessage");
            if(msg == null || msg.equals("")){
                OrderBean orderBeanModProd = (OrderBean)hshOrderDetail.get("objResume");
                strPaymentTermsModProd = orderBeanModProd.getNpPaymentTerms();
                System.out.println("[PRY-0710][PopUpOrder]["+strOrderId+"][strPaymentTermsModProd]"+strPaymentTermsModProd);
            }else{
                System.out.println("[PRY-0710][PopUpOrder]["+strOrderId+"][messageError]"+msg);
            }
        }
        //[PRY-0710]

        //[TDECONV003-1] EFLORES
        System.out.println("[TDECONV003-1] tdeValNumTelEquiProp "+tdeValNumTelEquiProp);
        System.out.println("[TDECONV003-1] tdeValEquipAlquiler "+tdeValEquipAlquiler);
        //[TDECONV003-1]

        //DERAZO TDECONV003-2
        System.out.println("[TDECONV003-2] tdeValImeiFS "+tdeValImeiFS);
        System.out.println("[TDECONV003-2] tdeLengthImeiFS "+tdeLengthImeiFS);

        if( strSiteOppId.equals("0") || strSiteOppId.equals("null")    ) strSiteOppId = "";
        if( strUnknwnSiteId.equals("0")  || strUnknwnSiteId.equals("null") ) strUnknwnSiteId = "";


        String  flgClosePopUp = objGeneralService.getValue("CLOSE_POPUP_BY_ESPECIFICATION",hdnSpecification);

        if( !MiUtil.getString(strGeneratorType).equals("") ){
            if( strUnknwnSiteId!=null && !strUnknwnSiteId.equals("") ){
                strnpSite = strUnknwnSiteId;
            }else if( strSiteOppId!=null && !strSiteOppId.equals("")){
                strnpSite = strSiteOppId;
            }
        }

        ArrayList objItemHEader = objNewOrderServicePopUp.ItemDAOgetItemHeaderSpecGrp(MiUtil.parseInt(hdnSpecification));
   
      //JQUISPE PRY-0762 validacion Renta Adelantada
        System.out.println("--- PopUpOrder.jsp----objTypeEvent>>"+objTypeEvent);
        if("NEW".equals(objTypeEvent)){
        	HashMap hshValidaRentaAdelantada = objNewOrderServicePopUp.validarRentaAdelantada(hdnSpecification, MiUtil.parseLong(strCodigoCliente), strTypeDocument, strDocument, null);
        strMessage = (String)hshValidaRentaAdelantada.get("strMessage");
            System.out.println("--- PopUpOrder.jsp----strMessage>>"+strMessage);
        if (strMessage!=null){
      	  throw new Exception(strMessage);
        }        
        isRentaAdelantada = ((Boolean)hshValidaRentaAdelantada.get("resultado")).booleanValue();
        }else{
        	HashMap hshRentaAdelantada = objNewOrderServicePopUp.getOrdenRentaAdelantada(MiUtil.parseLong(strOrderId));
            strMessage = (String)hshRentaAdelantada.get("strMessage");
            System.out.println("--- PopUpOrder.jsp----strMessage>>"+strMessage);
            if (strMessage!=null){
          	  throw new Exception(strMessage);
            }
        
            OrderRentaAdelantadaBean objOrderRentaAdelantadaBean = (OrderRentaAdelantadaBean)hshRentaAdelantada.get("objOrderRentaAdelantadaBean");
            System.out.println("--- PopUpOrder.jsp----objOrderRentaAdelantadaBean.getNpOrderRefRentaAdelantadaId()>>"+objOrderRentaAdelantadaBean.getNpOrderRefRentaAdelantadaId());
            if(objOrderRentaAdelantadaBean.getNpOrderRefRentaAdelantadaId() != 0){
          	  isRentaAdelantada = true;
            }
        }
        
        //JQUISPE PRY-0762
        if(isRentaAdelantada &&  !type_window.equals("NEW")){
       		String[] paramNpobjitemheaderid   = request.getParameterValues("a");
       		String[] paramNpobjitemvalue      = request.getParameterValues("b");
       		String strPlanId = "";
       		
       		System.out.println("PopUpOrder.jsp : paramNpobjitemheaderid" + paramNpobjitemheaderid);
       		
       		if(paramNpobjitemheaderid != null && paramNpobjitemheaderid.length > 0){
           		for(int i=0;i<paramNpobjitemheaderid.length; i++){
           			if( paramNpobjitemheaderid[i].equals("10") ){
           				strPlanId  = paramNpobjitemvalue[i];
           				break;
           			}        		        
           		}
           		
           		if(strPlanId != null && strPlanId.trim().length() > 0){
           			SessionService objSession  = new SessionService();
           			PortalSessionBean objSessionUser  = objSession.getUserSession(strSessionId);        			
           			HashMap objHashMapTotalRA = objGeneralService.getPrecioPlan(objSessionUser.getAppId(), strPlanId);
           			float[] arrRentFee = (float[])objHashMapTotalRA.get("afAccessFee");
           			strPrecioPlan = String.valueOf(arrRentFee[0]);
           		}
       		}       	
        }


   /*hshtinputNewSection.put("strCustomerId",""+strCodigoCliente);
   hshtinputNewSection.put("strSiteId",""+strnpSite);
   hshtinputNewSection.put("strCodBSCS",""+strCodBSCS);
   hshtinputNewSection.put("strSpecificationId",""+hdnSpecification);
   hshtinputNewSection.put("strSolution",""+strSolution);
   hshtinputNewSection.put("strTypeCompany",""+strTypeCompany);*/

        hshtinputNewSection.put("strCodigoCliente",""+strCodigoCliente);
        hshtinputNewSection.put("strnpSite",""+strnpSite);
        hshtinputNewSection.put("strCodBSCS",""+strCodBSCS);
        hshtinputNewSection.put("hdnSpecification",""+hdnSpecification);
        hshtinputNewSection.put("strDivision",""+strDivision);
        hshtinputNewSection.put("strTypeCompany",""+strTypeCompany);
        hshtinputNewSection.put("strStatus",""+strStatus);
        hshtinputNewSection.put("strSiteOppId",""+strSiteOppId);
        hshtinputNewSection.put("strUnknwnSiteId",""+strUnknwnSiteId);
        hshtinputNewSection.put("strGeneratorType",""+strGeneratorType);
        hshtinputNewSection.put("strSessionId",""+strSessionId);
        hshtinputNewSection.put("strOrderId",""+strOrderId); //se agrego la orden
        hshtinputNewSection.put("strSalesStuctOrigenId",""+strSalesStuctOrigenId);
        hshtinputNewSection.put("strnpnumcuotas",""+strnpnumcuotas);
        hshtinputNewSection.put("strflagvep",""+strflagvep);

        //INICIO: PRY-0864 | AMENDEZ
        hshtinputNewSection.put("strInitialQuota",""+strInitialQuota);
        //FIN: PRY-0864 | AMENDEZ

        //INICIO: PRY-0980 | AMENDEZ
        hshtinputNewSection.put("strNpPaymentTermsIQ",""+strNpPaymentTermsIQ);
        //FIN: PRY-0980 | AMENDEZ

        //INICIO: AMENDEZ | PRY-1049
        hshtinputNewSection.put("strHdnCobertura",""+strHdnCobertura);
        //FIN: AMENDEZ | PRY-1049


        request.setAttribute("hshtInputNewSection",hshtinputNewSection);
        request.setAttribute("strSessionId",strSessionId);

        strStockFlag = "N";
        if (hdnSpecification != null){
            hshValidateStock = objNewOrderServicePopUp.getValidateStock(MiUtil.parseInt(hdnSpecification), MiUtil.parseInt(strDispatchPlace)   );
            if (hshValidateStock!=null && hshValidateStock.size() > 0){
                strStockFlag = (String)hshValidateStock.get("wv_flag");
                strMessage = (String)hshValidateStock.get("wv_message");
                if (strMessage!= null){
                    throw new Exception(strMessage);
                }
            }
        }

        HashMap hpCategoryCambioModeloId = objGeneralService.getValueByConfiguration("SPECIFICATION_SERVICE_MSJ");

        String strCategoryCambioModeloId = "2009";

        if(hpCategoryCambioModeloId != null) {

            if(hpCategoryCambioModeloId.get("objConfigurationBean") != null) {

                ConfigurationBean configurationBean = (ConfigurationBean)hpCategoryCambioModeloId.get("objConfigurationBean");

                strCategoryCambioModeloId = configurationBean.getNpValueDesc();

            }

        }

        HashMap hNpTableList  = objGeneralService.getValueNpTable("FLAG_ACTIV_MSJ_VALID_COMP");
        ArrayList objNpTableList =(ArrayList)hNpTableList.get("objArrayList");
        String  strflagValidCompMPS= "I";
        TableBean nptableBean = null;
        if (objNpTableList.size()>0) {
            nptableBean = (TableBean)objNpTableList.get(0);
            strflagValidCompMPS = nptableBean.getNpValue().trim();
            System.out.println("La strflagValidCompMPS tiene---->" + strflagValidCompMPS);
        }


        HashMap hshConfiguration = new HashMap();
        ArrayList arrConfiguration = new ArrayList();
        hshConfiguration = objGeneralService.GeneralDAOgetNpConfValues("SPECIFICATION_CDI",null,null,"1",null,null,null);
        arrConfiguration = (ArrayList)hshConfiguration.get("objArrayList");

        //INICIO DERAZO REQ-0428
        String actionURL_PenaltyServlet = strRutaContext+"/penaltyservlet";
        
     	// Inicio [validate price exception] CDIAZ
        HashMap mapSpecificationsPriceException = objGeneralService.getValueNpTable("EXCEPTION_PRICE_SPECIFICATION");
        ArrayList arrSpecificationsPriceException = (ArrayList)mapSpecificationsPriceException.get("objArrayList");

        boolean flagValidationPriceException = false;
        for (int i = 0; i < arrSpecificationsPriceException.size(); i++) {
        	TableBean priceExceptionBean =  (TableBean)arrSpecificationsPriceException.get(i);
        	
        	if(hdnSpecification.equals(priceExceptionBean.getNpValue())){
        		flagValidationPriceException = true;
        		break;
        	}
        }
     	// Fin [validate price exception] CDIAZ
%>
<html>
<head>
    <title>Test Companiaaaaa</title>
    <link type="text/css" rel="stylesheet"
          href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"/>
    <style type="CDATA">
        .show   { display:inline}
        .hidden { display:none }
    </style>

    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js" charset="ISO-8859-1"> </script>
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js" charset="ISO-8859-1"></script>
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsItemsIXEdit.js" charset="ISO-8859-1"></script>
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderIXNew.js"charset="ISO-8859-1"></script>
    <script language="javaScript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/date-picker.js" charset="ISO-8859-1"></script>
    <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/jQuery-min.js" charset="ISO-8859-1"></script>

    <script type="text/javascript" src="<%=request.getContextPath()%>/websales/Resource/js/Roaming.js"></script>

    <% if(intModProd == 1){ //[PRY-0710]%>
    <script type="text/javascript" src="<%=request.getContextPath()%>/websales/Resource/js/ModProd.js"></script>
    <% } %>

</head>
<script defer>
    // MMONTOYA [ADT-RCT-092 Roaming con corte]
    // Inicio constantes de Roaming.js
    var SPEC_ACTIVAR_PAQUETES_ROAMING = <%=Constante.SPEC_ACTIVAR_PAQUETES_ROAMING%>;
    var TIPO_BOLSA_RECURRENTE = "<%=Constante.TIPO_BOLSA_RECURRENTE%>";
    var CONTEXT_PATH = "<%=request.getContextPath()%>";

    //EFLORES CDM+CDP PRY-0817 Se añade string adicional para guardar estado de comboSSAA y SSAA Seleccionados
    <% if(MiUtil.getInt(hdnSpecification) == Constante.SPEC_CAMBIO_MODELO_ENTRE_TECNOLOGIAS){ %>
        var varSSAASelectedTemp="";
    <%}%>

    // Fin constantes de Roaming.js

    var varTemporal = "";
    var indexEdit = <%=item_index%>;

    //[TDECONV003-8] INI PZACARIAS
    function fxValidateSIM_MSISDN(){
        var sim=frmdatos.txt_ItemSIM_Eagle.value;
        var newNumber=frmdatos.txt_ItemNewNumber.value;
        var tdeValNumTelSIM=<%=tdeValNumTelSIM%>;
        var tdeValNumTelMSISDN=<%=tdeValNumTelMSISDN%>;
        var subcatResult;
        var url_server = '<%=strURLItemServlet%>';
        var params = 'hdnMethod=validateSIM_MSISDN&sim='+sim+'&newNumber='+newNumber+'&tdeValNumTelSIM='+tdeValNumTelSIM+'&tdeValNumTelMSISDN='+tdeValNumTelMSISDN;
        var retorno = false;

        $.ajax({
            url: url_server,
            data: params,
            async: false,
            type: "POST",
            success:function(data){
                subcatResult=data.split('|');
                var flagValid=subcatResult[0];
                if (flagValid == "0"){
                    alert(subcatResult[1]);
                    retorno = false;
                } else {
                    retorno = true;
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
                retorno = false;
            }
        });
        return retorno;
    }

    function fxValidateTipDoc(){
        var tipDoc="<%=strTypeDocument%>";
        var newNumber=frmdatos.txt_ItemNewNumber.value;
        var subcatResult;
        var url_server = '<%=strURLItemServlet%>';
        var params = 'hdnMethod=validateTipDoc&tipDoc='+tipDoc+'&newNumber='+newNumber;
        var retorno = false;

        $.ajax({
            url: url_server,
            data: params,
            async: false,
            type: "POST",
            success:function(data){
                subcatResult=data.split('|');
                var flagValid=subcatResult[0];
                if (flagValid == "0"){
                    alert(subcatResult[1]);
                    retorno = false;
                } else {
                    retorno = true;
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
                retorno = false;
            }
        });
        return retorno;
    }
    //[TDECONV003-8] FIN

    function fxIsValidate(){
        var nameHtmlControl      = "";
        var mandatoryControl     = "";
        var datatypeControl      = "";
        var valueControl         = "";
        var bolsacreacion = 2021;
        var bolsacambio = 2022;
        var planTarifario = 2013;
        var solutionPospago = 3;
        var hdnSpecification = <%=hdnSpecification%>;
        var strOK = "";
        var c=0;
        var newNumberValid = false;

        //CBARZOLA:valida si los servicios estan dentro de los servicios core
        try{
            if (form.item_services.value!=''){
                if(fxValidaServiciosComerciales(form.item_services.value)){
                    return false;
                }
            }
        }catch(e){}

        //Verificar si tiene campos mandatorios
        for( x = 0; x < vctItemOrder.size(); x++ ){
            nameHtmlControl   = vctItemOrder.elementAt(x).namehtmlitem;
            mandatoryControl  = vctItemOrder.elementAt(x).npvalidateflg;
            datatypeControl   = vctItemOrder.elementAt(x).npdatatype;
            objitemheaderid   = vctItemOrder.elementAt(x).npobjitemheaderid;

            //alert("Datos : " + nameHtmlControl + " mandatoryControl : " + mandatoryControl  + " -> " + mandatoryControl.length)

            if( mandatoryControl == "S" ){
                //alert("Entramos a mandatoryControl ");
                valueControl  = eval("form." + nameHtmlControl + ".value");

                if( vctItemOrder.elementAt(x).npcontroltype == "TEXT" ){
                    if( trim(valueControl).length == 0 ){
                        if( eval("idDisplay"+objitemheaderid+".style.display") != 'none' ){
                            alert("El campo '" + vctItemOrder.elementAt(x).npobjitemname + "' es obligatorio");
                            eval("form." + nameHtmlControl + ".focus()");
                            return false;
                        }
                    }

                }else if( vctItemOrder.elementAt(x).npcontroltype == "SELECT" ){
                    if( valueControl == "" ){
                        if( eval("idDisplay"+objitemheaderid+".style.display") != 'none' ){
                            alert("La opción '" + vctItemOrder.elementAt(x).npobjitemname + "' es obligatorio");
                            eval("form." + nameHtmlControl + ".focus()");
                            return false;
                        }
                    }
                }else if( vctItemOrder.elementAt(x).npcontroltype == "OTRO" ){
                    if( valueControl == "" ){
                        if( eval("idDisplay"+objitemheaderid+".style.display") != 'none' ){
                            alert("La opción '" + vctItemOrder.elementAt(x).npobjitemname + "' es obligatoria");
                            //eval("form." + nameHtmlControl + ".focus()");
                            return false;
                        }
                    }
                }

            }//Fin del if mandatory
        }//Fin del for


        if ((hdnSpecification == bolsacreacion) || (hdnSpecification == bolsacambio))
        {
            strOK = form.cmb_ItemProducto.options[form.cmb_ItemProducto.selectedIndex].text.substring(6,8);
            c=0;
            if (strOK == "0K"){
                c=1;
            }
        }
        //Verificar si los valores ingresados son del Tipo de DATO que se requiere
        for( x = 0; x < vctItemOrder.size(); x++ ){
            nameHtmlControl   = vctItemOrder.elementAt(x).namehtmlitem;
            mandatoryControl  = vctItemOrder.elementAt(x).npvalidateflg;
            readOnlyControl   = vctItemOrder.elementAt(x).npobjreadonly;
            datatypeControl   = vctItemOrder.elementAt(x).npdatatype;
            objitemheaderid   = vctItemOrder.elementAt(x).npobjitemheaderid;
            //alert("Datos : " + nameHtmlControl + " mandatoryControl : " + mandatoryControl  + " -> " + mandatoryControl.length)

            //alert("Entramos a mandatoryControl ");
            valueControl  = eval("form." + nameHtmlControl + ".value");
            //Solo es válido para los campos TEXT
            if( vctItemOrder.elementAt(x).npcontroltype == "TEXT" ){
                if( datatypeControl == "<%=Constante.VALIDATE_INTEGER%>" ){
                    //if( (eval("idDisplay"+objitemheaderid+".style.display") != 'none') && (readOnlyControl != "S" ) && (mandatoryControl != "S") ){
                    if( (eval("idDisplay"+objitemheaderid+".style.display") != 'none') && (readOnlyControl != "S" ) ){
                        if( !ContentOnlyNumber(valueControl) ){
                            alert("El campo '" + vctItemOrder.elementAt(x).npobjitemname + "' solo permite digitos ENTEROS");
                            eval("form." + nameHtmlControl + ".select()");
                            eval("form." + nameHtmlControl + ".focus()");
                            return false;
                        }
                    }
                }else if( datatypeControl == "<%=Constante.VALIDATE_INT_POSITIVE%>" ){
                    //if( (eval("idDisplay"+objitemheaderid+".style.display") != 'none') && (readOnlyControl != "S" ) && (mandatoryControl != "S") ){
                    /*     if( (eval("idDisplay"+objitemheaderid+".style.display") != 'none') && (readOnlyControl != "S" ) ){
                     if( !ContentOnlyNumber(valueControl) || parseInt(valueControl) == 0 ){
                     alert("El campo '" + vctItemOrder.elementAt(x).npobjitemname + "' solo permite digitos ENTEROS positivos");
                     eval("form." + nameHtmlControl + ".select()");
                     eval("form." + nameHtmlControl + ".focus()");
                     return false;
                     }
                     }*/
                    if( (eval("idDisplay"+objitemheaderid+".style.display") != 'none') && (readOnlyControl != "S" ) ){
                        /*-nbravo-*/
                        if (nameHtmlControl == "txt_ItemMntsRates" && c==1 ){ // Modificación para que permita crear un producto bolsa con Minutos 0 -- filtrar por especificadion : hdnSpecification
                            // alert("hdnSpecification: "+hdnSpecification);
                            if(!ContentOnlyNumber(valueControl)){
                                alert("El campo '" + vctItemOrder.elementAt(x).npobjitemname + "' solo permite digitos ENTEROS positivos");
                                eval("form." + nameHtmlControl + ".select()");
                                eval("form." + nameHtmlControl + ".focus()");
                                return false;
                            }
                        }
                        else if( !ContentOnlyNumber(valueControl) || parseInt(valueControl) == 0 ){
                            alert("El campo '" + vctItemOrder.elementAt(x).npobjitemname + "' solo permite digitos ENTEROS positivos");
                            eval("form." + nameHtmlControl + ".select()");
                            eval("form." + nameHtmlControl + ".focus()");
                            return false;
                        }
                        /*-nbravo-*/
                    }
                }else if( datatypeControl == "<%=Constante.VALIDATE_FLOAT%>" ){
                    //if( (eval("idDisplay"+objitemheaderid+".style.display") != 'none') && (readOnlyControl != "S" ) && (mandatoryControl != "S") ){
                    if( (eval("idDisplay"+objitemheaderid+".style.display") != 'none') && (readOnlyControl != "S" ) ){
                        if( !ContentOnlyNumberDec(valueControl) ){
                            alert("El campo '" + vctItemOrder.elementAt(x).npobjitemname + "' solo permite digitos DECIMALES");
                            eval("form." + nameHtmlControl + ".select()");
                            eval("form." + nameHtmlControl + ".focus()");
                            return false;
                        }
                    }
                }else if( datatypeControl == "<%=Constante.VALIDATE_DATE%>" ){
                    //if( (eval("idDisplay"+objitemheaderid+".style.display") != 'none') && (readOnlyControl != "S" ) && (mandatoryControl != "S") ){
                    if( (eval("idDisplay"+objitemheaderid+".style.display") != 'none') && (readOnlyControl != "S" ) ){
                        if (eval("form." + nameHtmlControl + ".disabled == false") == true){
                            if( !isValidDate(valueControl) ){
                                eval("form." + nameHtmlControl + ".select()");
                                eval("form." + nameHtmlControl + ".focus()");
                                return false;
                            }
                        }
                    }
                }else if( datatypeControl == "<%=Constante.VALIDATE_DATE_PLUS%>" ){
                    //if( (eval("idDisplay"+objitemheaderid+".style.display") != 'none') && (readOnlyControl != "S" ) && (mandatoryControl != "S") ){
                    if( (eval("idDisplay"+objitemheaderid+".style.display") != 'none') && (readOnlyControl != "S" ) ){
                        if (eval("form." + nameHtmlControl + ".disabled == false") == true){
                            if( !fxCheckDateCurrent(valueControl) ){
                                eval("form." + nameHtmlControl + ".select()");
                                eval("form." + nameHtmlControl + ".focus()");
                                return false;
                            }
                        }
                    }
                }else if( datatypeControl == "<%=Constante.VALIDATE_DATE_TIME%>" ){
                    //if( (eval("idDisplay"+objitemheaderid+".style.display") != 'none') && (readOnlyControl != "S" ) && (mandatoryControl != "S") ){
                    if( (eval("idDisplay"+objitemheaderid+".style.display") != 'none') && (readOnlyControl != "S" ) ){
                        if (eval("form." + nameHtmlControl + ".disabled == false") == true){
                            if( !fxValidateDateTime(valueControl) ){
                                eval("form." + nameHtmlControl + ".select()");
                                eval("form." + nameHtmlControl + ".focus()");
                                return false;
                            }
                        }
                    }
                }else if( datatypeControl == "<%=Constante.VALIDATE_DATE_TIME_PLUS%>" ){
                    //if( (eval("idDisplay"+objitemheaderid+".style.display") != 'none') && (readOnlyControl != "S" ) && (mandatoryControl != "S") ){
                    if( (eval("idDisplay"+objitemheaderid+".style.display") != 'none') && (readOnlyControl != "S" ) ){
                        if (eval("form." + nameHtmlControl + ".disabled == false") == true){
                            if( !fxValidateDateTimePlus(valueControl) ){
                                eval("form." + nameHtmlControl + ".select()");
                                eval("form." + nameHtmlControl + ".focus()");
                                return false;
                            }
                        }
                    }
                }
            }

        }//Fin del for
        try{
            if(frmdatos.cmb_ItemPlanTarifario.value == frmdatos.txt_ItemNewPlantarifarioId.value &&
                    hdnSpecification == planTarifario && frmdatos.cmb_ItemSolution.value == solutionPospago){
                alert("El Plan Tarifario debe ser diferente al Plan Tarifario Original.");
                parent.mainFrame.frmdatos.cmb_ItemPlanTarifario.focus();
                return false;
            }
        }catch(e){}

        <% //[TDECONV003-1] EFLORES 01/09/2017
         if(tdeValNumTelEquiProp.equals("1") && hdnSpecification.equals(Constante.SPEC_POSTPAGO_VENTA+"")){ %>
            try{
                if(parent.opener.frmdatos.chkFlagMigration != null && parent.opener.frmdatos.chkFlagMigration.checked) {
                    if (frmdatos.cmb_ItemModality.value == "Propio" && frmdatos.txt_ItemNewNumber != null) {
                        if (frmdatos.txt_ItemNewNumber.value == "") {
                            alert("El campo nuevo número debe ser obligatorio");
                            parent.mainFrame.frmdatos.txt_ItemNewNumber.focus();
                            return false;
                        }else if(!ContentOnlyNumber(frmdatos.txt_ItemNewNumber.value)){
                            alert("Nuevo número Nextel no válido: No todos los caracteres son dígitos");
                            parent.mainFrame.frmdatos.txt_ItemNewNumber.focus();
                            return false;
                        }else if(frmdatos.txt_ItemNewNumber.value.length==11 && frmdatos.txt_ItemNewNumber.value.substring(0,2)!="<%= Constante.PREFIJO_TELEFONO %>"){
                            alert("Nuevo número Nextel no válido: El prefijo no es <%= Constante.PREFIJO_TELEFONO %>");
                            parent.mainFrame.frmdatos.txt_ItemNewNumber.focus();
                            return false;
                        }else if(frmdatos.txt_ItemNewNumber.value.length!=9){
                            if(frmdatos.txt_ItemNewNumber.value.length<9){
                                alert("Nuevo número Nextel no válido: La longitud del campo número de telefono no es la correcta");
                                parent.mainFrame.frmdatos.txt_ItemNewNumber.focus();
                                return false;
                            }else{
                                if(frmdatos.txt_ItemNewNumber.value.length==11 && frmdatos.txt_ItemNewNumber.value.substring(0,2)=="<%= Constante.PREFIJO_TELEFONO %>"){
                                    frmdatos.txt_ItemNewNumber.value = frmdatos.txt_ItemNewNumber.value;
                                    newNumberValid = true;
                                }else{
                                   alert("Nuevo número Nextel no válido: La longitud del campo número de telefono no es la correcta");
                            parent.mainFrame.frmdatos.txt_ItemNewNumber.focus();
                            return false;
                                }
                            }
                           
                        }else if(frmdatos.txt_ItemNewNumber.value.length == 9){
                            var t = frmdatos.txt_ItemNewNumber.value;
                            frmdatos.txt_ItemNewNumber.value = "<%= Constante.PREFIJO_TELEFONO %>"+t;
                            newNumberValid = true;
                        }
                    }
                }
            }catch(e){}
        <%} //[TDECONV003-1]%>

        //[TDECONV003-8] INI PZACARIAS
        if(newNumberValid) {
            //Validacion SIM y MSISDN
            <% if(tdeValNumTelSIM.equals("1") || tdeValNumTelMSISDN.equals("1")){ %>
            try {
                if (frmdatos.txt_ItemSIM_Eagle != null && frmdatos.txt_ItemSIM_Eagle.value != "") {
                    if(!fxValidateSIM_MSISDN()){
                        parent.mainFrame.frmdatos.txt_ItemNewNumber.focus();
                        return false;
                    }
                }
            }catch(e){}
            <%}%>

            //Validacion Tipo Documento
            <% if(tdeValNumTelTipDoc.equals("1")){ %>
            try {
                var tipDoc = "<%= strTypeDocument %>";
                if (tipDoc != null && tipDoc != "") {
                    if(!fxValidateTipDoc()){
                        parent.mainFrame.frmdatos.txt_ItemNewNumber.focus();
                        return false;
                    }
                }
            }catch(e){}
            <%}%>
        }
        //[TDECONV003-8] FIN

        return true;
    }//Fin de Function

    function fxValidateGeneral(){
        <%if(!objectType.equals("INC")){%>
        if( !fxValidateSA() ) return false;
        <%}%>
        <%if(!objectType.equals("INC")){%>
        if( !fxValidateMatch() ) return false;
        <%}%>
        <%if(!objectType.equals("INC")){%>
        if( !fxValidateChangeBag() ) return false;
        <%}%>
        return true;
    }

    /**MVERAE
     Objetivo : Permite validar la edicion del item seleccionado.
     **/
    function fxValidateGeneralEdit(){
        if( !fxValidateSA() ) return false;
        if( !fxValidateMatchEdit() ) return false;
        if( !fxValidateChangeBag() ) return false;
        return true;
    }

    /**Odubock
     Objetivo : Permite validar que el ingreso del nuevo producto Bolsa sea diferente al que ya tenía.
     **/
    function fxValidateChangeBag(){
        // Se anula la validacion Bolsa Fase III. Si se puede seleccionar el mismo producto bolsa
        /*
         var form = parent.mainFrame.frmdatos;
         if ("<%=hdnSpecification%>" == 2022){
         if(form.cmb_ItemProductBolsaId.value == form.cmb_ItemProducto.value){
         alert("Para realizar la transacción necesita cambiar el tipo de Bolsa de Minutos actual; transacción de CAMBIO de Bolsa no permitida.");
         form.cmb_ItemProducto.focus();
         return false;
         }else
         return true;
         }else
         */
        return true;
    }

    /*Developer : Víctor Cedeño
     Objetivo  : Obtiene la suma de las cantidades de los items generados
     */

    function sumaCantidadesItems() {
        var quantities = [];
        $("#items_table tr td div input[name='txtItemQuantity']", parent.opener.document).each(function () {
            quantities.push($(this).val());
        });
        var quantityCount = 0;
        for (var i = 0; i < quantities.length; i++) {
            quantityCount += Number(quantities[i]);
        }
        <%if(type_window.equals("EDIT")){%>
        quantityCount -= Number(quantities[indexEdit]);
        <%}%>
        return quantityCount;
    }

    /*Developer : Lizbeth Valencia
     Objetivo  : Validar que las ordenes de portabilidad tengan 100 items como máximo
     */

    function fxValidatePortabilityItems(){
        var quantityCount = sumaCantidadesItems();
        var currentQuantity = $("input[name='txt_ItemQuantity']").val();

        if(Number(quantityCount)+Number(currentQuantity)>100){
            alert("Sólo se pueden ingresar 100 equipos como máximo");
            return false;
        }
        return true;
    }

    /*Developer : Lee Rosales
     Objetivo  : Validar que un item ingresado no se repita. Por ahora
     se considera como PK el Teléfono o el IMEI o el contrato
     */
    function fxValidateMatch(){
        var cntTablesItems = parent.opener.items_table.rows.length;
        var openerForm     = parent.opener.frmdatos;

        //JPEREZCU SAR N_O000031059 20142608
        var npobjitemnamehtml = "";
        if( cntTablesItems == 1 ) return true;

        if( !fxIsNecessaryValidate() ) return true;
        //alert("Entraremos a buscar : " + vctItemOrder.size())
        //Recorro cada fila del ITEM

        //JPEREZCU SAR N_O000031059 20142608 inicio: se obtienen los valores del item a agegar antes de hacer el bucle de la grilla principal
        for(j = 0; j < vctItemOrder.size(); j ++ ){
            npobjitemheaderid = vctItemOrder.elementAt(j).npobjitemheaderid;
            npobjitemvalue    = vctItemOrder.elementAt(j).npobjitemvalue;

            if( fxGetObjectByHeaderId(npobjitemheaderid) != null ){
                //Si es un SIM (IMEI para Eagle)/Teléfono/IMEI/Nro Contrato
                if( npobjitemheaderid == 2 || npobjitemheaderid == 3 || npobjitemheaderid == 4 || npobjitemheaderid == 27 ){
                    //Si es un contrato
                    if( npobjitemheaderid == 27 && (
                            ( eval("idDisplay"+objitemheaderid+".style.display") == 'none' ) ||
                            ( eval("document.frmdatos.txt_ItemContractNumber.value == '' ") )
                            )
                    ){
                        continue;
                    }
                    //Si es un SIM (IMEI para Eagle)
                    if( npobjitemheaderid == 2 && (
                            ( eval("idDisplay"+objitemheaderid+".style.display") == 'none' ) ||
                            ( eval("document.frmdatos.txt_ItemSIM_Eagle.value == '' ") )
                            )
                    ){
                        continue;
                    }

                    npobjitemnamehtml = fxGetObjectByHeaderId(npobjitemheaderid).nphtmlname;
                    if( cntTablesItems == 2 ){
                        npitemobjectvalue = eval("parent.opener.frmdatos.hdnItemValue"+npobjitemnamehtml.substring(1,npobjitemnamehtml.length)+".value");
                        //Comparamos el valor actual y el valor de la cabecera de la fila
                        if( npitemobjectvalue == npobjitemvalue && npitemobjectvalue!="" && npobjitemvalue!=""){
                            alert("Este registro ya fue ingresado. Ingrese otros datos");
                            return false;
                        }
                    }else{
                        //recorre la grilla de la página principal
                        for( i = 0; i < (cntTablesItems-1); i ++ ){
                            npitemobjectvalue = eval("parent.opener.frmdatos.hdnItemValue"+npobjitemnamehtml.substring(1,npobjitemnamehtml.length)+"[i].value");
                            //Comparamos el valor actual y el valor de la cabecera de la fila
                            if( npitemobjectvalue == npobjitemvalue && npitemobjectvalue!="" && npobjitemvalue!=""){
                                alert("Este registro ya fue ingresado. Ingrese otros datos");
                                return false;
                            }
                        }
                    }
                }
            }
        }

        //JPEREZCU SAR N_O000031059 20142608 fin
        /*
         for( i = 0; i < (cntTablesItems-1); i ++ ){

         //Recorro los datos a pasar
         for(j = 0; j < vctItemOrder.size(); j ++ ){
         npobjitemheaderid = vctItemOrder.elementAt(j).npobjitemheaderid;
         npobjitemvalue    = vctItemOrder.elementAt(j).npobjitemvalue;

         if( fxGetObjectByHeaderId(npobjitemheaderid) != null ){
         if( npobjitemheaderid != 22 && npobjitemheaderid != 25 && npobjitemheaderid != 72 ){
         //Si es un SIM (IMEI para Eagle)/Teléfono/IMEI/Nro Contrato
         if( npobjitemheaderid == 2 || npobjitemheaderid == 3 || npobjitemheaderid == 4 || npobjitemheaderid == 27 ){
         //alert("Entramos : " + objitemheaderid )
         //Si es un contrato
         if( npobjitemheaderid == 27 && (
         ( eval("idDisplay"+objitemheaderid+".style.display") == 'none' ) ||
         ( eval("document.frmdatos.txt_ItemContractNumber.value == '' ") )
         )
         ){
         continue;
         }

         //Si es un SIM (IMEI para Eagle)
         if( npobjitemheaderid == 2 && (
         ( eval("idDisplay"+objitemheaderid+".style.display") == 'none' ) ||
         ( eval("document.frmdatos.txt_ItemSIM_Eagle.value == '' ") )
         )
         ){
         continue;
         }

         npobjitemnamehtml = fxGetObjectByHeaderId(npobjitemheaderid).nphtmlname;
         if( cntTablesItems == 2 )
         npitemobjectvalue = eval("parent.opener.frmdatos.hdnItemValue"+npobjitemnamehtml.substring(1,npobjitemnamehtml.length)+".value");
         else
         npitemobjectvalue = eval("parent.opener.frmdatos.hdnItemValue"+npobjitemnamehtml.substring(1,npobjitemnamehtml.length)+"[i].value");
         //Comparamos el valor actual y el valor de la cabecera de la fila
         //alert("Valor Actual : " + npobjitemvalue + " -> Valor Cabecera : " + npitemobjectvalue );
         if( npitemobjectvalue == npobjitemvalue && npitemobjectvalue!="" && npobjitemvalue!=""){
         alert("Este registro ya fue ingresado. Ingrese otros datos");
         return false;
         }
         }
         }//Fin del If de las excepciones de controles
         }//Fin del If de distinto de null
         }//Fin del for del vector del PopUp
         }//Fin del for de ITEM's
         */


        return true;
    }

    /*Developer : MVERAE
     Objetivo  : Validar que un item editado no se repita. Por ahora
     se considera como PK el Teléfono o el IMEI o el contrato
     */
    function fxValidateMatchEdit(){
        var cntTablesItems = parent.opener.items_table.rows.length;
        var openerForm     = parent.opener.frmdatos;

        //JPEREZCU SAR N_O000031059 20142608
        var npobjitemnamehtml = "";

        if( cntTablesItems == 1 ) return true;

        if( !fxIsNecessaryValidate() ) return true;

        //JPEREZCU SAR N_O000031059 20142608 inicio: se obtienen los valores del item a agegar antes de hacer el bucle de la grilla principal
        for(j = 0; j < vctItemOrder.size(); j ++ ){
            npobjitemheaderid = vctItemOrder.elementAt(j).npobjitemheaderid;
            npobjitemvalue    = vctItemOrder.elementAt(j).npobjitemvalue;
            if( fxGetObjectByHeaderId(npobjitemheaderid) != null ){
                //Teléfono/IMEI/Nro Contrato
                if( npobjitemheaderid == 3 || npobjitemheaderid == 4 || npobjitemheaderid == 27 ){
                    //Si es un contrato
                    if( npobjitemheaderid == 27 && (
                            ( eval("idDisplay"+objitemheaderid+".style.display") == 'none' ) ||
                            ( eval("document.frmdatos.txt_ItemContractNumber.value == '' ") )
                            )
                    ){
                        continue;
                    }

                    //Si es un SIM (IMEI para Eagle)
                    if( npobjitemheaderid == 2 && (
                            ( eval("idDisplay"+objitemheaderid+".style.display") == 'none' ) ||
                            ( eval("document.frmdatos.txt_ItemSIM_Eagle.value == '' ") )
                            )
                    ){
                        continue;
                    }

                    npobjitemnamehtml = fxGetObjectByHeaderId(npobjitemheaderid).nphtmlname;

                    if( cntTablesItems == 2 ){
                        npitemobjectvalue = eval("parent.opener.frmdatos.hdnItemValue"+npobjitemnamehtml.substring(1,npobjitemnamehtml.length)+".value");
                    }else{
                        //recorre la grilla de la página principal
                        for( i = 0; i < (cntTablesItems-1); i ++ ){
                            npitemobjectvalue = eval("parent.opener.frmdatos.hdnItemValue"+npobjitemnamehtml.substring(1,npobjitemnamehtml.length)+"[i].value");
                            //Comparamos el valor actual y el valor de la cabecera de la fila
                            if(i!= <%=item_index%>){//el indice tiene que ser distinto al indice del item editado
                                if( npitemobjectvalue == npobjitemvalue ){
                                    alert("Este registro ya fue ingresado. Ingrese otros datos");
                                    return false;
                                }
                            }
                        }
                    }

                }
            }
        }

        //alert("Entraremos a buscar : " + vctItemOrder.size())
        //Recorro cada fila del ITEM
        /*
         for( i = 0; i < (cntTablesItems-1); i ++ ){
         //Recorro los datos a pasar
         for(j = 0; j < vctItemOrder.size(); j ++ ){
         npobjitemheaderid = vctItemOrder.elementAt(j).npobjitemheaderid;
         npobjitemvalue    = vctItemOrder.elementAt(j).npobjitemvalue;
         if( fxGetObjectByHeaderId(npobjitemheaderid) != null ){
         if( npobjitemheaderid != 22 && npobjitemheaderid != 25 && npobjitemheaderid != 72 ){
         if( npobjitemheaderid == 3 || npobjitemheaderid == 4 || npobjitemheaderid == 27 ){

         //Si es un contrato
         if( npobjitemheaderid == 27 && (
         ( eval("idDisplay"+objitemheaderid+".style.display") == 'none' ) ||
         ( eval("document.frmdatos.txt_ItemContractNumber.value == '' ") )
         )
         ){
         continue;
         }

         //Si es un SIM (IMEI para Eagle)
         if( npobjitemheaderid == 2 && (
         ( eval("idDisplay"+objitemheaderid+".style.display") == 'none' ) ||
         ( eval("document.frmdatos.txt_ItemSIM_Eagle.value == '' ") )
         )
         ){
         continue;
         }

         npobjitemnamehtml = fxGetObjectByHeaderId(npobjitemheaderid).nphtmlname;
         if( cntTablesItems == 2 )
         npitemobjectvalue = eval("parent.opener.frmdatos.hdnItemValue"+npobjitemnamehtml.substring(1,npobjitemnamehtml.length)+".value");
         else
         npitemobjectvalue = eval("parent.opener.frmdatos.hdnItemValue"+npobjitemnamehtml.substring(1,npobjitemnamehtml.length)+"[i].value");
         //Comparamos el valor actual y el valor de la cabecera de la fila
         //alert("Valor Actual : " + npobjitemvalue + " -> Valor Cabecera : " + npitemobjectvalue );
         if(i!= <%=item_index%>){//el indice tiene que ser distinto al indice del item editado
         if( npitemobjectvalue == npobjitemvalue ){
         alert("Este registro ya fue ingresado. Ingrese otros datos");
         return false;
         }
         }
         }
         }//Fin del If de las excepciones de controles
         }//Fin del If de distinto de null
         }//Fin del for del vector del PopUp
         }//Fin del for de ITEM's

         */

        return true;
    }

    function fxIsNecessaryValidate(){
        for(j = 0; j < vctItemOrder.size(); j ++ ){
            npobjitemheaderid = vctItemOrder.elementAt(j).npobjitemheaderid;
            //Por ahora solo se validan los teléfonos, IMEIS, Contratos
            if( npobjitemheaderid == 2 || npobjitemheaderid == 3 || npobjitemheaderid == 4 || npobjitemheaderid == 27 )
                return true;
        }
        return false;
    }

    function fxGetObjectByHeaderId(objHeaderId){
        for( x = 0; x <parent.opener.vctItemHeaderOrder.size(); x++ ){
            objitemheaderid   = parent.opener.vctItemHeaderOrder.elementAt(x).npobjitemheaderid;
            if( objHeaderId == objitemheaderid )
                return parent.opener.vctItemHeaderOrder.elementAt(x);
        }
    }

    function fxValidateSA(){

        var form = document.frmdatos;
        var modalidad =  "";
        var productLine = "";
        try{
            modalidad = form.cmb_ItemModality[form.cmb_ItemModality.selectedIndex].text;
        }catch(e){}

        try{
            productLine = form.cmb_ItemProductLine[form.cmb_ItemProductLine.selectedIndex].value;
        }catch(e){}

        var alquilerServices = (parent.mainFrame.alquilerServicesSelected == 0) && (parent.mainFrame.alquilerServicesSelected1 == 0) && (parent.mainFrame.alquilerServicesSelected2 == 0) && (parent.mainFrame.alquilerServicesSelected3 == 0);

        // Se valida la activación o desactivación de por lo menos un servicio adicional
        if ("<%=hdnSpecification%>" == "<%=Constante.SPEC_SSAA_SUSCRIPCIONES%>" || "<%=hdnSpecification%>" == "<%=Constante.SPEC_SSAA_PROMOTIONS%>" || "<%=hdnSpecification%>" == "<%=Constante.SPEC_ACTIVAR_DESACTIVAR_SERVICIOS%>"){
            var strActDesServ = 0
            var strServicio = form.item_services.value.split("|");
            for(i = 1; i < strServicio.length; i=i+3){
                if (strServicio[i+1]=='N' || strServicio[i+2]=='N'){
                    strActDesServ = strActDesServ + 1;
                }
            }
            if(strActDesServ == 0){
                alert("Se debe activar o desactivar por lo menos un servicio adicional.\nDe no encontrar el servicio requerido contactarse con Marketing");
                return false;
            }
        }

        for( x = 0; x < vctItemOrder.size(); x++ ){
            objheaderid = vctItemOrder.elementAt(x).npobjitemid;
            //Si existen servicios adicionales

            if( objheaderid == 23 ){
                //Si la categoría de Venta de Internet / VPN
                if ("<%=hdnSpecification%>" == "<%=Constante.KN_VTA_INTERNET_ACC_INTERNET%>" || "<%=hdnSpecification%>" == "<%=Constante.KN_VTA_INTERNET_ENLACE_DATOS%>"){

                    if ( productLine ==  "<%=Constante.PRODUCT_LINE_SERV_INST_INTERNET%>" || productLine == "<%=Constante.PRODUCT_KIT_INTERNET%>" ){
                        if(form.cmb_ItemSolution.value == "<%=Constante.KN_ACCESO_INTERNET%>" &&  parent.mainFrame.rentServicesSelected == 0 ){
                            alert("Debe seleccionar al menos un servicio garantizado");
                            return false;
                        }
                        if(form.cmb_ItemSolution.value == "<%=Constante.KN_ENLACE_DATOS%>" && parent.mainFrame.rentServicesSelectedEnd == 0 ) {
                            alert("Debe seleccionar al menos un servicio garantizado");
                            return false;
                        }
                    }
                }
                /*
                 Para Venta de Internet (2004) y VPN (2005): Valida que se seleccione un servicio de alquiler
                 Para Venta de Telefonía Fija - BA (2006): Si la solución es de Internet: Valida que se seleccione un servicio de alquiler
                 Si la solución es de TF: Valida que se seleccione un servicio de alquiler sólo si la modalidad es Alquiler
                 Para Cambio de Plan BA (2049) y ACT/DES de Servicios BA (2048): Valida que se seleccione un servicio de alquiler sólo si la solución es Telefonía Fija
                 */

                if ("<%=hdnSpecification%>" == "<%=Constante.KN_VTA_INTERNET_ACC_INTERNET%>" || "<%=hdnSpecification%>" == "<%=Constante.KN_VTA_INTERNET_ENLACE_DATOS%>" || "<%=hdnSpecification%>" == "<%=Constante.KN_VTA_TELEFONIA_FIJA_BA%>" ){
                    if (  modalidad=="<%=Constante.TIPO_ALQUILER%>" ){
                        if ( form.cmb_ItemSolution.value == "<%=Constante.KN_TELEFONIA_FIJA%>" && alquilerServices ){
                            alert("Debe seleccionar al menos un servicio de alquiler");
                            return false;
                        }else if ( (form.cmb_ItemSolution.value == "<%=Constante.KN_ACCESO_INTERNET%>" || form.cmb_ItemSolution.value == "<%=Constante.KN_ENLACE_DATOS%>") && alquilerServices ){
                            alert("Debe seleccionar al menos un servicio de alquiler");
                            return false;
                        }
                    }
                }else if ("<%=hdnSpecification%>" == "<%=Constante.KN_ACT_CAMB_PLAN_BA%>" ||"<%=hdnSpecification%>" == "<%=Constante.KN_ACT_DES_SERVICIOS_BA%>"){
                    if ( form.cmb_ItemSolution.value == "<%=Constante.KN_TELEFONIA_FIJA%>" && alquilerServices ){
                        alert("Debe seleccionar al menos un servicio de alquiler");
                        return false;
                    }
                }

                var count_service = 0;
                for(j=0; j<vServicioArren.size(); j++){
                    objServicioArr = vServicioArren.elementAt(j);
                    if(objServicioArr.exclude == 'ALQ' && objServicioArr.modify_new == 'S'){
                        count_service = count_service + 1;
                    }
                    if(count_service > 1){
                        alert('No se puede tener mas de un servicio de alquiler Solicitado');
                        return false;
                    }
                }

                //Inicio MSOTO 26/11/2013
                // CAMBIO DE MODELO ? Servicios de Arrendamiento
                if ("<%=hdnSpecification%>" == "<%=Constante.SPEC_CAMBIO_MODELO%>"){
                    // SAR:0037-202106  -  Si es ?Alquiler?, que en el ítem tenga un servicio de arrendamiento
                    if (  form.cmb_ItemModality[form.cmb_ItemModality.selectedIndex].text=="Alquiler" ){
                        if(count_service == 0){
                            alert('Verificar que se tenga servicio de arrendamiento');
                            return false;
                        }
                        // SAR:0037-202108  -  Si es ?Venta?, que en el ítem no tenga un servicio de arrendamiento
                    }else if (  form.cmb_ItemModality[form.cmb_ItemModality.selectedIndex].text=="Venta" ){
                        if(count_service > 0){
                            alert('Verificar que no se tenga servicio de arrendamiento');
                            return false;
                        }
                    }
                }
                //Fin MSOTO 26/11/2013

                // Validación de servicios CDI
                <%
                    for( int j = 0; j < arrConfiguration.size(); j++ ){
                      ConfigurationBean objConfiguration = null;
                      String strSpecification = null;
                      String strSolution =null;
                      String strPlan= null;
                      objConfiguration = new ConfigurationBean();
                      objConfiguration = (ConfigurationBean)arrConfiguration.get(j);
                      strSpecification = objConfiguration.getNpTag1();
                      strSolution = objConfiguration.getNpTag2();
                      strPlan=objConfiguration.getNpTag3();
                      if(hdnSpecification.equals(strSpecification)){
                %>
                //Se valida si el plan fue configurado en la tabla NP_CONFIGURATION, con npconfiguration='SPECIFICATION_CDI'

                try{
                    if(form.cmb_ItemSolution.value=="<%=strSolution%>" && frmdatos.cmb_ItemPlanTarifario.value=="<%=strPlan%>" ){

                        var count_cdi = 0;
                        var wn_cantServ = vServicio.size();

                        for (i=0;i<form.cmbSelectedServices.options.length;i++){
                            cadena = form.cmbSelectedServices.options[i].value
                            for(k=0; k<wn_cantServ; k++){
                                objServicio = vServicio.elementAt(k);
                                if (objServicio.nameShort == cadena && objServicio.modify_new == "S" && objServicio.exclude == "CDI"){ //SAR 0037-193623
                                    count_cdi = count_cdi + 1;
                                }
                            }
                        }

                        if(count_cdi==0){
                            alert("Debe seleccionar al menos un servicio de CDI");
                            return false;
                        }
                    }
                }catch(e){
                    //Entra a esta excepcion en caso de no existir el combo cmb_ItemPlanTarifario
                }

                <%
                      }
                    }
                %>

                return true;
            }

            if( objheaderid == 120 ){
                // Validación de servicios CDI
                <%

                    for( int j = 0; j < arrConfiguration.size(); j++ ){
                      ConfigurationBean objConfiguration = null;
                      String strSpecification = null;
                      String strSolution = null;
                      String strPlan= null;
                      objConfiguration = new ConfigurationBean();
                      objConfiguration = (ConfigurationBean)arrConfiguration.get(j);
                      strSpecification = objConfiguration.getNpTag1();
                      strSolution = objConfiguration.getNpTag2();
                      strPlan=objConfiguration.getNpTag3();
                      if(hdnSpecification.equals(strSpecification)){
                %>
                //Se valida si el plan fue configurado en la tabla NP_CONFIGURATION, con npconfiguration='SPECIFICATION_CDI'

                try{
                    if(form.cmb_ItemSolution.value=="<%=strSolution%>" && frmdatos.cmb_ItemPlanTarifario.value=="<%=strPlan%>" ){
                        var count_cdi = 0;
                        var wn_cantServ = vServicio.size();
                        for (i=0;i<form.cmbSelectedServices.options.length;i++){
                            cadena = form.cmbSelectedServices.options[i].value
                            for(k=0; k<wn_cantServ; k++){
                                objServicio = vServicio.elementAt(k);
                                if (objServicio.nameShort == cadena &&
                                        objServicio.modify_new == "S" && objServicio.exclude == "CDI"){ //SAR 0037-193623
                                    count_cdi = count_cdi + 1;
                                }
                            }
                        }

                        if(count_cdi==0){
                            alert("Debe seleccionar al menos un servicio de CDI");
                            return false;
                        }
                    }
                }catch(e){
                    //Entra a esta excepcion en caso de no existir el combo cmb_ItemPlanTarifario
                }
                <%
                     }
                   }
               %>

                return true;
            }

        }
        return true;
    }

    /*Developer: Hugo Moreno
     Objetivo : Valida que todos los ITEMS sean de un mismo tipo de moneda
     */
    function fxCurrencyValidate(v_moneda, v_flag){
        var form = document.frmdatos;

        //Cuando es llamado desde un Incidente
        if (parent.opener.items_table==null){
            return true; // indica que no hay problema con la moneda
        }

        v_numRows = parent.opener.items_table.rows.length;

        // NEW ORDEN
        if (parent.opener.frmdatos.hdnCurrency != null){
            //Cuando se agrega el primer ITEM, su moneda será la GUÍA
            if (v_numRows == 1){
                parent.opener.frmdatos.hdnCurrency.value = v_moneda;
            }
            else{
                //Cuando ya exista un ITEM, se debe validar que sea del mismo tipo de moneda que la GUÍA
                if (v_numRows == 2){
                    if (v_flag == 0)
                        v_flagCurrency = parent.opener.frmdatos.hdnCurrency.value;
                    else{
                        v_flagCurrency = v_moneda;
                        parent.opener.frmdatos.hdnCurrency.value = v_moneda;
                    }
                }
                if (v_numRows > 2)
                    v_flagCurrency = parent.opener.frmdatos.hdnCurrency.value;
                if (v_moneda != v_flagCurrency){
                    alert("Ingresar Productos con el mismo tipo de moneda");
                    return false;
                }
            }
        }
        // EDIT ORDEN
        else{
            // Si hay más de 1 ROW, se toma la primera fila como GUÍA
            if (v_numRows > 2)
                parent.opener.frmdatos.hdnCurrencyEdit.value = parent.opener.frmdatos.txtItemMoneda[1].value;
            else{
                // Si viene de NUEVO ITEM
                if (v_flag == 0){
                    if (v_numRows == 1)
                        parent.opener.frmdatos.hdnCurrencyEdit.value = v_moneda;
                    else
                        parent.opener.frmdatos.hdnCurrencyEdit.value = parent.opener.frmdatos.txtItemMoneda.value;
                }
                // Si viene de EDIT ITEM
                else
                    parent.opener.frmdatos.hdnCurrencyEdit.value = v_moneda;
            }
            if (v_numRows == 1)
            // Si viene de NUEVO ITEM
                if (v_flag == 0)
                    parent.opener.frmdatos.hdnCurrencyEdit.value = v_moneda;
                //Si viene de EDIT ITEM
                else
                    parent.opener.frmdatos.hdnCurrencyEdit.value = parent.opener.frmdatos.txtItemMoneda[1].value;
            else{
                if (v_numRows > 1) {
                    v_flagCurrency = parent.opener.frmdatos.hdnCurrencyEdit.value;
                    if (v_moneda != v_flagCurrency){
                        alert("Ingresar Productos con el mismo tipo de moneda");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    function getSelectedIdServices() {
        var str = "";
        var cont=0;
        var wn_cantServ = vServicio.size();
        for(j=0; j<wn_cantServ; j++){
            objServicio = vServicio.elementAt(j);

            if (objServicio.modify_new == "S" && objServicio.active_new == "N") {
                str =  str +  objServicio.id + ",";
                ++cont;
            }
        }
        if (cont>0) {
            str =  str.substr(0,str.length-1);
        }
        return str;
    }

    function getSelectedDescServices() {
        var strName = "";
        var cont=0;
        var wn_cantServ = vServicio.size();
        for(j=0; j<wn_cantServ; j++){
            objServicio = vServicio.elementAt(j);

            if (objServicio.modify_new == "S" && objServicio.active_new == "N") {
                strName =  strName +  objServicio.name + ",";
                ++cont;
            }
        }
        if (cont>0) {
            strName =  strName.substr(0,strName.length-1);
        }
        return strName;
    }

    /*Developer: Frank Picoy
     Objetivo : Valida el tipo de plan
     */
    function fxValidateTypePlan(objPlanId){
        var form = document.frmdatos;
        var url = "<%=request.getContextPath()%>/itemServlet?&strMode='NEW'&strPlanId="+objPlanId+"&hdnMethod=getValidateTypePlan";
        parent.bottomFrame.location.replace(url);
    }

    function fxSetHiddenTypePlan(flag) {
        var form = document.frmdatos;
        form.hdnflagPlanType.value=flag;
    }

    //Inicio SAR N_O000005779 - FPICOY - 20/11/2013
    /*Developer: Frank Picoy
     Objetivo : Validar si el plan al que desea cambiarse un empleado es Comercial o no.
     */
    function fxSetHiddenChangePlan(flag) {
        var form = document.frmdatos;
        form.hdnflagChangePlan.value=flag;
    }
    //Fin SAR N_O000005779 - FPICOY - 20/11/2013

    /*Developer: Lee Rosales
     Objetivo : Crea un nuevo item
     */
    function fxSendItemValuesOrder(){

        var form = document.frmdatos;
        var hdnSpecification=<%=hdnSpecification%>;//Reserva de Numeros Golden - RHUACANI-ASISTP - 24/10/2010
        var spec_PostV=<%=Constante.SPEC_POSTPAGO_VENTA%>;//Reserva de Numeros Golden - RHUACANI-ASISTP - 24/10/2010
        var spec_PreV=<%=Constante.SPEC_PREPAGO_NUEVA%>;//Reserva de Numeros Golden - RHUACANI-ASISTP - 24/10/2010
        var spec_PreV_TDE=<%=Constante.SPEC_PREPAGO_TDE%>; //Agregar nueva especificacion para Prepago TDE TDECONV029 08/08/2017
        var spec_PreR_TDE=<%=Constante.SPEC_REPOSICION_PREPAGO_TDE%>; //Agregar nueva especificacion para reposicion prepago TDE 25/01/2018 TDECONV034
        var kitGolden=<%=Constante.PRODUCT_LINE_KIT_GOLDEN%>;//Reserva de Numeros Golden - RHUACANI-ASISTP - 24/10/2010
        var itemidRN=<%=Constante.ITEM_ID_RESERVA_NUMEROS%>;//Reserva de Numeros Golden - RHUACANI-ASISTP - 24/10/2010
        var nameHTMLInput      = "";
        var valueDescription   = "";
        var spec_RenBucketCCP=<%=Constante.SPEC_SSAA_RENOV_BCKT_CCP%>;
        var spec_ChangePlan=<%=Constante.SPEC_CAMBIAR_PLAN_TARIFARIO%>;// SAR N_O000005779 - FPICOY - 20/11/2013
        var spec_ChangeModelTech=<%=Constante.SPEC_CAMBIO_MODELO_ENTRE_TECNOLOGIAS%>;// SAR N_O000012485 - FPICOY - 09/01/2014
        var spec_PortabilidadAlta=<%=Constante.SPEC_POSTPAGO_PORTA%>;// Portabilidad Fase 3 - LVALENCIA - 22/10/2014

        /*INICIO ADT-BCL-083 --LHUAPAYA*/
        <% if( hdnSpecification.equals(String.valueOf(Constante.SPEC_BOLSA_CREAR)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_BOLSA_UPGRADE)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_BOLSA_DOWNGRADE)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_BOLSA_DESACTIVAR))){ %>
        var url = "<%=request.getContextPath()%>/itemServlet?strSiteId=<%=strnpSite%>&strCustomerId=<%=strCodigoCliente%>&hdnMethod=getValidatePhoneBlacklist";
        parent.bottomFrame.location.replace(url);
        <%}%>
        /*PURPOSE: SALTAR LAS VALIDACIONES DE CAMPOS OBLIGATORIOS */
        <%if(hdnSpecification.equals(String.valueOf(Constante.SPEC_BOLSA_DESACTIVAR))){%>
        if(document.frmdatos.chk_todos.checked){
            var solucionCodigo;
            var solucionDesc;
            var modalidadCodigo;
            var modalidadDesc;
            var nivelCodigo;
            var nivelDesc;
            <%
                long site=0;
                long customer = 0;
               if(!"".equals(strnpSite)) site = MiUtil.parseInt(strnpSite);
               else customer = MiUtil.parseInt(strCodigoCliente);
               HashMap hsmAllProduct = objNewOrderServicePopUp.getAllProductBCL(site,customer);
               String mensaje = (String)hsmAllProduct.get("strMessage");
                System.out.println("este es el mensaje: " + mensaje);

               //ArrayList objArrayProduct = objNewOrderServicePopUp.getAllProductBCL(MiUtil.parseInt(strnpSite));

               if(mensaje != null){
            %>  alert('<%=mensaje%>');
            return;
            <%}else{
                ArrayList objArrayProduct = (ArrayList)hsmAllProduct.get("objArrayList");
               System.out.println("este es el tamaño de la lista: " + objArrayProduct.size());
               int tamanio = objArrayProduct.size();
            %>
            for( x = 0; x < vctItemOrder.size(); x++ ){
                nameHTMLInput = vctItemOrder.elementAt(x).namehtmlitem;
                if(nameHTMLInput=='cmb_ItemSolution'){
                    solucionCodigo   =   eval("form." + nameHTMLInput + ".value");
                    if ( vctItemOrder.elementAt(x).npcontroltype == "SELECT" )
                        valueDescription  = eval("form." + nameHTMLInput + ".options[form."+nameHTMLInput+".selectedIndex].text");
                    else{
                        valueDescription  = eval("form." + nameHTMLInput + ".value");
                    }
                    solucionDesc = fxCleanString(valueDescription);
                }
                if(nameHTMLInput=='cmb_ItemModality'){
                    modalidadCodigo   =   eval("form." + nameHTMLInput + ".value");
                    if ( vctItemOrder.elementAt(x).npcontroltype == "SELECT" )
                        valueDescription  = eval("form." + nameHTMLInput + ".options[form."+nameHTMLInput+".selectedIndex].text");
                    else{
                        valueDescription  = eval("form." + nameHTMLInput + ".value");
                    }
                    modalidadDesc = fxCleanString(valueDescription);
                }
                if(nameHTMLInput=='cmb_ItemNivel'){
                    nivelCodigo   =   eval("form." + nameHTMLInput + ".value");
                    if ( vctItemOrder.elementAt(x).npcontroltype == "SELECT" )
                        valueDescription  = eval("form." + nameHTMLInput + ".options[form."+nameHTMLInput+".selectedIndex].text");
                    else{
                        valueDescription  = eval("form." + nameHTMLInput + ".value");
                    }
                    nivelDesc = fxCleanString(valueDescription);
                }
            }
            <%
          for(int n = 0; n<tamanio; n++){

               HashMap hsmProduct = new HashMap();
               hsmProduct = (HashMap)objArrayProduct.get(n);
               long codLine =  ((Long)hsmProduct.get("strProductLineId")).longValue();
               String descLine = (String)hsmProduct.get("strProductLineDesc");
               long codProd = ((Long)hsmProduct.get("strProductId")).longValue();
               String descProd = (String)hsmProduct.get("strProductDesc");
               double codPrec =(Double)hsmProduct.get("strProductCost");
               double descPrec = (Double)hsmProduct.get("strProductCost");
               %>
            for(var y = 0; y < vctItemOrder.size(); y++ ){
                nameHTMLInput = vctItemOrder.elementAt(y).namehtmlitem;
                if(nameHTMLInput=='cmb_ItemSolution'){
                    vctItemOrder.elementAt(y).npobjitemvalue    =  solucionCodigo;
                    vctItemOrder.elementAt(y).npobjitemvaluedesc = solucionDesc;
                    vctItemOrder.elementAt(y).npobjitemflagsave = 'N';
                    continue;
                }
                if(nameHTMLInput=='cmb_ItemModality'){
                    vctItemOrder.elementAt(y).npobjitemvalue    =  modalidadCodigo;
                    vctItemOrder.elementAt(y).npobjitemvaluedesc = modalidadDesc;
                    vctItemOrder.elementAt(y).npobjitemflagsave = 'N';
                    continue;
                }
                if(nameHTMLInput=='cmb_ItemNivel'){
                    vctItemOrder.elementAt(y).npobjitemvalue    =  nivelCodigo;
                    vctItemOrder.elementAt(y).npobjitemvaluedesc = nivelDesc;
                    vctItemOrder.elementAt(y).npobjitemflagsave = 'N';
                    continue;
                }
                if(nameHTMLInput=='cmb_ItemProductLine'){
                    vctItemOrder.elementAt(y).npobjitemvalue    =  "<%=codLine%>";
                    vctItemOrder.elementAt(y).npobjitemvaluedesc = "<%=descLine%>";
                    vctItemOrder.elementAt(y).npobjitemflagsave = 'N';
                    continue;
                }
                if(nameHTMLInput=='cmb_ItemProductoOrigen'){
                    vctItemOrder.elementAt(y).npobjitemvalue    =  "<%=codProd%>";
                    vctItemOrder.elementAt(y).npobjitemvaluedesc = "<%=descProd%>";
                    vctItemOrder.elementAt(y).npobjitemflagsave = 'N';
                    continue;
                }
                if(nameHTMLInput=='txt_ItemPriceCtaInscrip'){
                    vctItemOrder.elementAt(y).npobjitemvalue    = "<%=codPrec%>";
                    vctItemOrder.elementAt(y).npobjitemvaluedesc = "<%=descPrec%>";
                    vctItemOrder.elementAt(y).npobjitemflagsave = 'N';
                    continue;
                }
            }
            parent.opener.parent.mainFrame.fxAddRowOrderItemsGlobal(vctItemOrder);
            <%}%>
            formResetGeneral();
            return;
            <%}%>
        }
        <%}%>
        /*FIN ADT-BCL-083 --LHUAPAYA*/

    // MMONTOYA [ADT-RCT-092 Roaming con corte]
    // Validación del servicio roaming recurrente.
    if (!validateSelectedRecurrentRoamingService(hdnSpecification, <%=strOrderId%>)) {
        return;
    }
        /*INICIO ADT-BCL-083 --LHUAPAYA*/
        <% if( hdnSpecification.equals(String.valueOf(Constante.SPEC_BOLSA_CREAR)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_BOLSA_UPGRADE)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_BOLSA_DOWNGRADE)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_BOLSA_DESACTIVAR))){ %>
        var url = "<%=request.getContextPath()%>/itemServlet?strSiteId=<%=strnpSite%>&strCustomerId=<%=strCodigoCliente%>&hdnMethod=getValidatePhoneBlacklist";
        parent.bottomFrame.location.replace(url);
        <%}%>

        //Validad cantidad de items para las ordenes de portabilidad alta
        if (hdnSpecification == spec_PortabilidadAlta){
            if (!fxValidatePortabilityItems()){
                if(sumaCantidadesItems()<100){
                    $("input[name='txt_ItemQuantity']").focus();
                    $("input[name='txt_ItemQuantity']").select();
                }else{
                    parent.close();
                }
                return false;
            }
        }

        //Inicio Validaciòn VEP 10-01-13 RMARTINEZ
        //alert('strflagvep: '+'<%=strflagvep%>');
        if ('<%=strflagvep%>' == "1"){
            var solution = form.cmb_ItemSolution[form.cmb_ItemSolution.selectedIndex].value;

            if (solution != "<%=Constante.SOLUCION_2G_POST%>" &&
                    solution != "<%=Constante.SOLUTION_3G_HPPTT_POST%>" &&
                    solution != "<%=Constante.SOLUTION_DATA%>" &&
                    solution != "<%=Constante.SOLUTION_INTERNET_MOVIL_POST%>" &&
                    solution != "<%=Constante.SOLUTION_3G_POST%>"){
                alert("No es posible agregar ítems a la orden para la soluciòn seleccionada");
                return false;
            }

            //alert('solution: '+solution);
            //alert('hdnSpecification: '+hdnSpecification);
        }
        //Fin Validaciòn VEP 10-01-13 RMARTINEZ
        try{
            varTemporal = parent.mainFrame.frmdatos.hdnIdCM.value +"|"+parent.mainFrame.frmdatos.hdnNameCM.value+"|"+parent.mainFrame.frmdatos.hdnNameShortCM.value+"|"+parent.mainFrame.frmdatos.hdnOriginalProductIdCM.value;
        }catch(e){}

        //Reserva de Numeros Golden - Inicio RHUACANI-ASISTP - 24/10/2010
        if (hdnSpecification==spec_PostV || hdnSpecification==spec_PreV || hdnSpecification == spec_PreV_TDE ||  hdnSpecification == spec_PreR_TDE) {
            if (form.cmb_ItemProductLine.value==kitGolden && !fxValidateQuantityValues()) {
                return false;
            }
        }
        //Fin RHUACANI-ASISTP - 24/10/2010

        //Se agregan los servicios de arrendamiento al vector final de servicios
        //----------------------------------------------------------------------
        try{
            AddServicesDefault();
        }catch(e){}


        try{
            form.item_services.value = GetSelectedServices();
        }catch(e){}

        var bResult;
        if( fxIsValidate() ){
            if(form.txt_ItemMoneda != null){
                bResult = fxCurrencyValidate(form.txt_ItemMoneda.value, 0);
            }
            else{
                bResult = true;
            }

            //INI EZM Validación compatibilidad Modelo - Plan - Servicio
            if (bResult){
                //Valida que el Plan Destino no sea igual al plan origen.
                if (hdnSpecification == spec_RenBucketCCP){
                    if (form.hdnOriginalPlanId.value == form.cmb_ItemPlanTarifario.value) {
                        alert("El Plan Destino no puede ser igual al Plan Origen");
                        return false;
                    }
                }

                //Valida el Plan antes de agregar el item
                if ((hdnSpecification == spec_RenBucketCCP) && form.hdnflagPlanType.value=="1"){
                    fxValidateTypePlan(form.cmb_ItemPlanTarifario.value);
                    return false;
                }

                //Inicio SAR N_O000005779 - FPICOY - 20/11/2013
                //Antes de agregar el item valida para la categoria Cambio de Plan, si el plan al que desea cambiarse un empleado es Comercial o no.
                if ((hdnSpecification == spec_ChangePlan || hdnSpecification == spec_ChangeModelTech) && form.hdnflagChangePlan.value!=""){ //SAR N_O000012485 - FPICOY - 09/01/2014
                    alert(form.hdnflagChangePlan.value);//SAR N_O000012485 - FPICOY - 09/01/2014
                    return false;
                }
                //Fin SAR N_O000005779 - FPICOY - 20/11/2013

                <%if ( hdnSpecification.equals(String.valueOf(Constante.SPEC_POSTPAGO_VENTA)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_ACTIVAR_DESACTIVAR_SERVICIOS))
                || hdnSpecification.equals(String.valueOf(Constante.SPEC_CAMBIO_MODELO_ENTRE_TECNOLOGIAS)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_CAMBIO_MODELO))
                || hdnSpecification.equals(String.valueOf(Constante.SPEC_EMPLEADO_ASIGNACION)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_EMPLEADO_FAMILIAR))
                || hdnSpecification.equals(String.valueOf(Constante.SPEC_EMPLEADO_AMIGO)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_PORTABILIDAD_POSTPAGO))
                || hdnSpecification.equals(String.valueOf(Constante.SPEC_PRESTAMO_CLIENTE_POR_DEMO)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_PRESTAMO_TEST))
                || hdnSpecification.equals(String.valueOf(Constante.SPEC_CAMBIAR_PLAN_TARIFARIO)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_ACT_DES_SERVICIOS_BA))
                || hdnSpecification.equals(String.valueOf(Constante.SPEC_TRANSFERENCIA_SUB_REG))
                || hdnSpecification.equals(String.valueOf(Constante.SPEC_TRANSFERENCIA)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_ACCESO_INTERNET))
                || hdnSpecification.equals(String.valueOf(Constante.SPEC_VENTA_MOVILES_RETAIL_POSTPAGO)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_VENTA_TELFIJA_NUEVA_ADICION))
                || hdnSpecification.equals(String.valueOf(Constante.SPEC_SSAA_PROMOTIONS))
                ){%>

                form.item_id_services.value = getSelectedIdServices();
                form.item_desc_services.value = getSelectedDescServices();
                if ("<%=strStockFlag%>"!="Y" && "<%=strflagValidCompMPS%>"=="A"){
                    fxValidServicesSelectedList(form.item_id_services.value,form.item_desc_services.value);
                }
                <% } %>
                //FIN EZM Validación compatibilidad Modelo - Plan - Servicio

                for( x = 0; x < vctItemOrder.size(); x++ ){
                    nameHTMLInput = vctItemOrder.elementAt(x).namehtmlitem;
                    //alert("nameHTMLInput:" + nameHTMLInput);
                    /*Obtenemos el Value*/
                    vctItemOrder.elementAt(x).npobjitemvalue    =   eval("form." + nameHTMLInput + ".value");

                    /**SOLO SI ES REPOSICIONES O CAMBIO DE MODELO (ARTIFICIO TEMPORAL) **/
                    if( vctItemOrder.elementAt(x).npobjheaderid == 52 || vctItemOrder.elementAt(x).npobjheaderid == 12 ){
                        if( eval("form." + nameHTMLInput + ".value") == "SI" ) vctItemOrder.elementAt(x).npobjitemvalue = "S";
                        else if( eval("form." + nameHTMLInput + ".value") == "NO" ) vctItemOrder.elementAt(x).npobjitemvalue = "N";
                        else  vctItemOrder.elementAt(x).npobjitemvalue = eval("form." + nameHTMLInput + ".value");
                    }
                    /**SOLO SI ES REPOSICIONES O CAMBIO DE MODELO (ARTIFICIO TEMPORAL) **/

                    /*Obtenemos la Description*/
                    if ( vctItemOrder.elementAt(x).npcontroltype == "SELECT" )
                        valueDescription  = eval("form." + nameHTMLInput + ".options[form."+nameHTMLInput+".selectedIndex].text");
                    else{
                        valueDescription  = eval("form." + nameHTMLInput + ".value");
                    }

                    //Reserva de Numeros Golden - Inicio RHUACANI-ASISTP - 24/10/2010
                    if (vctItemOrder.elementAt(x).npobjitemid == itemidRN){
                        valueDescription=fxGetStringElementsByMultiSelectControl(nameHTMLInput);
                        vctItemOrder.elementAt(x).npobjitemvalue=valueDescription;
                    }
                    //Fin RHUACANI-ASISTP - 24/10/2010

                    valueDescription = fxCleanString(valueDescription);

                    vctItemOrder.elementAt(x).npobjitemvaluedesc = valueDescription;
                    // alert("npobjitemvaluedesc:" + vctItemOrder.elementAt(x).npobjitemvaluedesc);
                    vctItemOrder.elementAt(x).npobjitemflagsave = 'N';

                    //DERAZO REQ-0428
                    //EFLORES REQ-0428_2 Se cambia logica
                    if(parent.opener.flagPenaltyFunct == 1){
                        if(nameHTMLInput == "txt_PagoPenalidad"){
                            var numAddenActive = parent.mainFrame.document.getElementById("h_penalidad").value;
                            if(numAddenActive == "0"){
                                vctItemOrder.elementAt(x).npobjitemvalue = "N";
                            }
                            else{
                                <%  System.out.println("[PopUpOrder][strOrderId] Orden "+strOrderId+" ItemId "+strItemId+" tiene adenda activa"); %>
                                vctItemOrder.elementAt(x).npobjitemvalue = "S";
                            }
                        }
                    }
                    //EFLORES CDM+CDP PRY-0817 Se ingresa logica para mantener valor del check
                    if(nameHTMLInput == "chkMantenerSIM"){
                        var chkStatusKeepSim = parent.mainFrame.document.frmdatos.chkMantenerSIM.checked;
                        if(chkStatusKeepSim){
                            vctItemOrder.elementAt(x).npobjitemvalue = "1";
                        }else{
                            vctItemOrder.elementAt(x).npobjitemvalue = "";
                }
                    }
                }

                /*INICIO ADT-BCL-083 --LHUAPAYA*/
                <% if(hdnSpecification.equals(String.valueOf(Constante.SPEC_BOLSA_CREAR)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_BOLSA_DESACTIVAR)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_BOLSA_DOWNGRADE)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_BOLSA_UPGRADE))){ %>
                var strProductLine = form.cmb_ItemProductLine[form.cmb_ItemProductLine.selectedIndex].value;
                <% if(hdnSpecification.equals(String.valueOf(Constante.SPEC_BOLSA_CREAR))){ %>
                var strTypeProduct = form.cmb_itemType[form.cmb_itemType.selectedIndex].value;
                <%}else{%>
                var strTypeProduct = 1;
                <%}%>
                if(strTypeProduct==1){
                    if(!fxValidateCellBag(strProductLine)){
                        alert('No puede existir mas de un producto con la misma linea');
                        return false;
                    }
                }
                <%}%>
                /*FIN ADT-BCL-083 --LHUAPAYA*/

                /*Si el item se ha modificado, se setea un flag*/
                <%if(!objectType.equals("INC")){%>
                fxValidateItemChange();
                <%}%>

                // Validación para Kits Prepago
                /*      if( form.txt_ItemPriceException!=null ){
                 var productLine="";

                 try{
                 productLine = form.cmb_ItemProductLine.value;
                 }catch(e){}
                 if (productLine=="12" && form.txt_ItemPriceException.value!="" && form.cmb_ItemModality.value =="Venta" ){
                 //alert("Es un Kit Prepago: " + form.cmb_ItemModality.value + " productid:" +  form.cmb_ItemProducto.value);
                 //objThis.value=trim(objThis.value);
                 var url = "<%=request.getContextPath()%>/serviceservlet?myaction=doValidateKitPrice&strModality=20"+"&strProductId=" + form.cmb_ItemProducto.value+"&strExceptionPrice="+form.txt_ItemPriceException.value+"&strPrice="+form.txt_ItemPriceCtaInscrip.value;
                 alert(url);
                 parent.bottomFrame.location.replace(url);
                 //return;
                 }
                 }
                 */
                //RPOLO Verifica comisiion de Servicios SSAA
                /*
                <%if ( hdnSpecification.equals(String.valueOf(Constante.SPEC_ACTIVAR_DESACTIVAR_SERVICIOS))){%>
                 var url = "<%=request.getContextPath()%>/itemServlet?&strServiceId="+form.item_services.value+"&hdnMethod=getComissionMessage";
                 parent.bottomFrame.location.replace(url);
                <%}%>
                 */

                //Ahora hacemos una validación General
                if( !fxValidateGeneral() ) return false;

                <%
              	// Inicio [validate price exception] CDIAZ
  				if(flagValidationPriceException) {
  				%>
  				if( !fxValidatePriceException() ) return false;
  				<%
  				}
              	// Fin [validate price exception] CDIAZ
  				%>

                fxCalculateTotalItem();
                //JPEREZ: Evalúa la modalidad de salida para el ítem
                try{
                    var strModality = form.cmb_ItemModality[form.cmb_ItemModality.selectedIndex].text;
                    if ( !fxValidateItemModality("<%=hdnSpecification%>", strModality, -1) ){
                        alert("La Modalidad de Salida debe ser igual para todos los ítems de la orden");
                        return false;
                    }
                }catch(e){}



                //KSALVADOR: Se agrega nuevo parametro Tipo:(Nuevo/Usados) para validar en el stock a que subalmacen pertnecen
                //Actualmente este valor sólo se encuentra configurado para Reposiciones
                try{
                    var strTipo = "";
                    strTipo = form.cmbItemProductStatus.value;
                }catch(e){
                }
                //Validación de Stock  - JPEREZ
                if ("<%=strStockFlag%>"=="Y" ){

                    //EZM 08/12/10 Compatibilidad Modelo-Plan-Servicio
                    var form = document.frmdatos;
                    objValueId = form.item_id_services.value;
                    objValueDesc = form.item_desc_services.value;

                    var hdnSpecification=<%=hdnSpecification%>;
                    <%if ( hdnSpecification.equals(String.valueOf(Constante.SPEC_ACTIVAR_DESACTIVAR_SERVICIOS)) ||  hdnSpecification.equals(String.valueOf(Constante.SPEC_CAMBIAR_PLAN_TARIFARIO))
                       || hdnSpecification.equals(String.valueOf(Constante.SPEC_TRANSFERENCIA)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_TRANSFERENCIA_SUB_REG))
                       || hdnSpecification.equals(String.valueOf(Constante.SPEC_CAMBIO_MODELO))
                     ){%>
                    var ind = form.txt_ItemModel.selectedIndex;
                    var productDesc = form.txt_ItemModel.options[ind].text;

                    var form = document.frmdatos;
                    var url = "<%=request.getContextPath()%>/itemServlet?&strMode='NEW'&strServices="+objValueId+"&strServicesDesc="+objValueDesc+"&strProduct="+productDesc+"&strPlanId="+form.txt_ItemNewPlantarifarioId.value+"&strSpecificationId=<%=hdnSpecification%>&strProductId="+form.cmb_ItemProducto.value+"&strDispatchPlace=<%=strDispatchPlace%>&strSaleModality="+form.cmb_ItemModality.value+"&strTipo="+strTipo+"&strSalesStuctOrigenId=<%=strSalesStuctOrigenId%>&strflagValidCompMPS=<%=strflagValidCompMPS%>&hdnMethod=getStockMessage2";
                    parent.bottomFrame.location.replace(url);
                    <% }

                     else if (hdnSpecification.equals(String.valueOf(Constante.SPEC_CAMBIO_MODELO_ENTRE_TECNOLOGIAS))
                           || hdnSpecification.equals(String.valueOf(Constante.SPEC_EMPLEADO_ASIGNACION)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_EMPLEADO_FAMILIAR))
                           || hdnSpecification.equals(String.valueOf(Constante.SPEC_EMPLEADO_AMIGO)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_PORTABILIDAD_POSTPAGO))
                           || hdnSpecification.equals(String.valueOf(Constante.SPEC_PRESTAMO_CLIENTE_POR_DEMO)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_PRESTAMO_TEST))
                           //|| hdnSpecification.equals(String.valueOf(Constante.SPEC_ACT_DES_SERVICIOS_BA))
                           || hdnSpecification.equals(String.valueOf(Constante.SPEC_ACT_CAMB_PLAN_BA))
                           || hdnSpecification.equals(String.valueOf(Constante.SPEC_ACCESO_INTERNET)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_VENTA_INTERNET_ENLACE_DATOS))
                           || hdnSpecification.equals(String.valueOf(Constante.SPEC_POSTPAGO_VENTA)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_VENTA_MOVILES_RETAIL_POSTPAGO))
                           || hdnSpecification.equals(String.valueOf(Constante.SPEC_VENTA_TELFIJA_NUEVA_ADICION))
                      ) {%>
                    var ind = form.cmb_ItemProducto.selectedIndex;
                    var productDesc = form.cmb_ItemProducto.options[ind].text;
                    var form = document.frmdatos;
                    var url = "<%=request.getContextPath()%>/itemServlet?&strMode='NEW'&strServices="+objValueId+"&strServicesDesc="+objValueDesc+"&strProduct="+productDesc+"&strPlanId="+form.cmb_ItemPlanTarifario.value+"&strSpecificationId=<%=hdnSpecification%>&strProductId="+form.cmb_ItemProducto.value+"&strDispatchPlace=<%=strDispatchPlace%>&strSaleModality="+form.cmb_ItemModality.value+"&strTipo="+strTipo+"&strSalesStuctOrigenId=<%=strSalesStuctOrigenId%>&strflagValidCompMPS=<%=strflagValidCompMPS%>&hdnMethod=getStockMessage2";
                    parent.bottomFrame.location.replace(url);
                    <% } else {%>

                    var url = "<%=request.getContextPath()%>/itemServlet?&strMode='NEW'&strSpecificationId=<%=hdnSpecification%>&strProductId="+form.cmb_ItemProducto.value+"&strDispatchPlace=<%=strDispatchPlace%>&strSaleModality="+form.cmb_ItemModality.value+"&strTipo="+strTipo+"&strSalesStuctOrigenId=<%=strSalesStuctOrigenId%>&hdnMethod=getStockMessage";
                    parent.bottomFrame.location.replace(url);
                    <% } %>
                    //EZM 08/12/10 Compatibilidad Modelo-Plan-Servicio
                    //var url = "<%=request.getContextPath()%>/itemServlet?&strMode='NEW'&strSpecificationId=<%=hdnSpecification%>&strProductId="+form.cmb_ItemProducto.value+"&strDispatchPlace=<%=strDispatchPlace%>&strSaleModality="+form.cmb_ItemModality.value+"&hdnMethod=getStockMessage";
                    //parent.bottomFrame.location.replace(url);
                }else{
                    try{
                        parent.opener.vIdModelos.addElement(varTemporal);
                    }catch(e){}


                    <%if( objTypeEvent.equals("EDIT") ){%>
                    <%if(objectType.equals("INC")){%>
                    parent.opener.parent.mainFrame.fxAddRowOrderItemsGlobalEdit(vctItemOrder);
                    <%}else{%>
                    parent.opener.fxAddRowOrderItemsGlobalEdit(vctItemOrder);
                    <%}%>
                    <%}else if( objTypeEvent.equals("NEW") ){ %>
                    <%if(objectType.equals("INC")){%>
                    parent.opener.parent.mainFrame.fxAddRowOrderItemsGlobal(vctItemOrder);
                    <%}else{%>

                    parent.opener.fxAddRowOrderItemsGlobal(vctItemOrder);
                    //Inicio Reserva de Numeros - FPICOY 17/01/2011
                    <%if (hdnSpecification.equals(String.valueOf(Constante.SPEC_CAMBIO_NUMERO))) { %>
                    v_numRows = parent.opener.items_table.rows.length;
                    v_numRows = v_numRows - 1;
                    if (v_numRows>1) {
                        parent.opener.document.frmdatos.item_ptnId[v_numRows-1].value=document.frmdatos.hdnPtnId.value;
                    } else {
                        parent.opener.document.frmdatos.item_ptnId.value=document.frmdatos.hdnPtnId.value;
                    }
                    <%}%>
                    //Fin Reserva de Numeros - FPICOY 17/01/2011
                    <%}%>
                    <%}%>
                    parent.opener.deleteItemEnabled       =	true;

                    <%if(flgClosePopUp!=null && (!flgClosePopUp.equals(Constante.NPERROR))){%>
                    parent.close();
                    <%}%>
                }

                //CPUENTE2 CAP & CAL CAMBIO DE MODELO CON/SIN DEVOLUCION DE EQUIPO - INICIO
                if ("<%=hdnSpecification%>"== "<%=Constante.SPEC_CAMBIO_MODELO%>" || "<%=hdnSpecification%>"== "<%=Constante.SPEC_CAMBIO_MODELO_ENTRE_TECNOLOGIAS%>")
                {
                    //alert(""+form.txt_ItemEquipment.value);
                    //alert(""+form.cmb_ItemDevolution.value);
                    if (form.txt_ItemEquipment.value=="Cliente" && form.cmb_ItemDevolution.value== "S")
                        alert("No olvidar pedir factura de transferencia");
                }
                //CPUENTE2 CAP & CAL CAMBIO DE MODELO CON/SIN DEVOLUCION DE EQUIPO - FIN

            }//bResult
            else{
                try{
                    DeleteArrServices();
                }catch(e){}
            }
            //Reiniciar todo y se puede seleccionar nuevamente
            formResetGeneral();

        }//validate
        else{
            try{
                DeleteArrServices();
            }catch(e){}
        }
    }
    //INI EZM Validación de compatibilidad Modelo-Plan-Servicio
    function fxValidServicesSelectedList(objValueId,objValueDesc){
        var form = document.frmdatos;
        var hdnSpecification=<%=hdnSpecification%>;
        <%if ( hdnSpecification.equals(String.valueOf(Constante.SPEC_ACTIVAR_DESACTIVAR_SERVICIOS)) ||  hdnSpecification.equals(String.valueOf(Constante.SPEC_CAMBIAR_PLAN_TARIFARIO))
             || hdnSpecification.equals(String.valueOf(Constante.SPEC_TRANSFERENCIA)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_TRANSFERENCIA_SUB_REG))
             || hdnSpecification.equals(String.valueOf(Constante.SPEC_CAMBIO_MODELO)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_SSAA_PROMOTIONS))
            ){%>
        var ind = form.txt_ItemModel.selectedIndex;
        var productDesc = form.txt_ItemModel.options[ind].text;
        var form = document.frmdatos;
        <%if ( hdnSpecification.equals(String.valueOf(Constante.SPEC_CAMBIAR_PLAN_TARIFARIO))
         ){%>
        var url = "<%=request.getContextPath()%>/itemServlet?&strMode='NEW'&strServices="+objValueId+"&strServicesDesc="+objValueDesc+"&strProduct="+productDesc+"&strPlanId="+form.cmb_ItemPlanTarifario.value+"&hdnMethod=getValidServSelectedList";
        parent.bottomFrame.location.replace(url);
        <% }
        else {%>
        var url = "<%=request.getContextPath()%>/itemServlet?&strMode='NEW'&strServices="+objValueId+"&strServicesDesc="+objValueDesc+"&strProduct="+productDesc+"&strPlanId="+form.txt_ItemNewPlantarifarioId.value+"&hdnMethod=getValidServSelectedList";
        parent.bottomFrame.location.replace(url);
        <% } %>
        <% }
          else if (hdnSpecification.equals(String.valueOf(Constante.SPEC_CAMBIO_MODELO_ENTRE_TECNOLOGIAS))
                     || hdnSpecification.equals(String.valueOf(Constante.SPEC_EMPLEADO_ASIGNACION)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_EMPLEADO_FAMILIAR))
                     || hdnSpecification.equals(String.valueOf(Constante.SPEC_EMPLEADO_AMIGO)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_PORTABILIDAD_POSTPAGO))
                     || hdnSpecification.equals(String.valueOf(Constante.SPEC_PRESTAMO_CLIENTE_POR_DEMO)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_PRESTAMO_TEST))
                     //|| hdnSpecification.equals(String.valueOf(Constante.SPEC_ACT_DES_SERVICIOS_BA))
                     || hdnSpecification.equals(String.valueOf(Constante.SPEC_ACT_CAMB_PLAN_BA))
                     || hdnSpecification.equals(String.valueOf(Constante.SPEC_ACCESO_INTERNET)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_VENTA_INTERNET_ENLACE_DATOS))
                     || hdnSpecification.equals(String.valueOf(Constante.SPEC_POSTPAGO_VENTA)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_VENTA_MOVILES_RETAIL_POSTPAGO))
                     || hdnSpecification.equals(String.valueOf(Constante.SPEC_VENTA_TELFIJA_NUEVA_ADICION))
                )  {%>
        var ind = form.cmb_ItemProducto.selectedIndex;
        var productDesc = form.cmb_ItemProducto.options[ind].text;
        var form = document.frmdatos;
        var url = "<%=request.getContextPath()%>/itemServlet?&strMode='NEW'&strServices="+objValueId+"&strServicesDesc="+objValueDesc+"&strProduct="+productDesc+"&strPlanId="+form.cmb_ItemPlanTarifario.value+"&hdnMethod=getValidServSelectedList";

        parent.bottomFrame.location.replace(url);
        <% } %>

    }
    //FIN EZM Validación de compatibilidad Modelo-Plan-Servicio

    //INI EZM Validación de compatibilidad Modelo-Plan-Servicio
    function fxValidServicesSelectedListEdit(objValueId,objValueDesc){

        var form = document.frmdatos;
        var hdnSpecification=<%=hdnSpecification%>;
        <%if ( hdnSpecification.equals(String.valueOf(Constante.SPEC_ACTIVAR_DESACTIVAR_SERVICIOS)) ||  hdnSpecification.equals(String.valueOf(Constante.SPEC_CAMBIAR_PLAN_TARIFARIO))
        || hdnSpecification.equals(String.valueOf(Constante.SPEC_CAMBIO_MODELO))|| hdnSpecification.equals(String.valueOf(Constante.SPEC_TRANSFERENCIA))
        || hdnSpecification.equals(String.valueOf(Constante.SPEC_TRANSFERENCIA_SUB_REG)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_SSAA_PROMOTIONS))
        ){%>
        var ind = form.txt_ItemModel.selectedIndex;
        var productDesc = form.txt_ItemModel.options[ind].text;
        var form = document.frmdatos;
        <%if ( hdnSpecification.equals(String.valueOf(Constante.SPEC_CAMBIAR_PLAN_TARIFARIO))
        ){%>
        var url = "<%=request.getContextPath()%>/itemServlet?&strMode=EDIT&strServices="+objValueId+"&strServicesDesc="+objValueDesc+"&strProduct="+productDesc+"&strPlanId="+form.cmb_ItemPlanTarifario.value+"&hdnMethod=getValidServSelectedList";
        parent.bottomFrame.location.replace(url);
        <% }
        else {%>
        var url = "<%=request.getContextPath()%>/itemServlet?&strMode=EDIT&strServices="+objValueId+"&strServicesDesc="+objValueDesc+"&strProduct="+productDesc+"&strPlanId="+form.txt_ItemNewPlantarifarioId.value+"&hdnMethod=getValidServSelectedList";
        parent.bottomFrame.location.replace(url);
        <% } %>
        <% }
        else {%>
        var ind = form.cmb_ItemProducto.selectedIndex;
        var productDesc = form.cmb_ItemProducto.options[ind].text;
        var form = document.frmdatos;
        var url = "<%=request.getContextPath()%>/itemServlet?&strMode=EDIT&strServices="+objValueId+"&strServicesDesc="+objValueDesc+"&strProduct="+productDesc+"&strPlanId="+form.cmb_ItemPlanTarifario.value+"&hdnMethod=getValidServSelectedList";
        parent.bottomFrame.location.replace(url);
        <% } %>

    }
    //FIN EZM Validación de compatibilidad Modelo-Plan-Servicio

    /*Developer: Lee Rosales
     Objetivo : Actualiza un item
     */
    function fxSendItemValuesEditOrder(){
        var spec_ChangePlan=<%=Constante.SPEC_CAMBIAR_PLAN_TARIFARIO%>;// SAR N_O000005779 - FPICOY - 20/11/2013
        var hdnSpecification=<%=hdnSpecification%>;// SAR N_O000005779 - FPICOY - 20/11/2013
        var spec_ChangeModelTech=<%=Constante.SPEC_CAMBIO_MODELO_ENTRE_TECNOLOGIAS%>;// SAR N_O000012485 - FPICOY - 09/01/2014
        var spec_PortabilidadAlta=<%=Constante.SPEC_POSTPAGO_PORTA%>;// Ajuste de Portabilidad - VCEDENOS - 25/03/2015

        //[REQ-0710] Modificacion de Producto
        //Metodo para validar  que el cambio de producto sea del mismo precio
     <% if(intModProd == 1){ //[REQ-0710] %>

        var modProdNpPlanTarifario = document.frmdatos.cmb_ItemPlanTarifario.value;
        if(!validateIfSamePlanTarifario("<%= strPaymentTermsModProd %>","<%= Constante.PAYFORM_CONTADO %>",vctItemOrderOriginal,modProdNpPlanTarifario,document.frmdatos.cmb_ItemPlanTarifario)){
            return;
        }

          var modProdNpPrice = document.frmdatos.txt_ItemPriceCtaInscrip.value;
          var modProdNpPriceException = document.frmdatos.txt_ItemPriceException.value;
          if(!validateIfSamePrice("<%= strPaymentTermsModProd %>","<%= Constante.PAYFORM_CONTADO %>",vctItemOrderOriginal,modProdNpPrice,modProdNpPriceException)) {
              alert("El precio original del item no coincide con el nuevo");
              return;
          }

          var modProdNpPriceType = document.frmdatos.txt_ItemPriceType.value;
          if(!validateCheckAddendumIfPriceExcepAndListPriceT(document.frmdatos.checkSelec,modProdNpPriceType,modProdNpPriceException)){
            alert("Se debe marcar al menos una plantilla de adenda");
              return;
          }
    <% }%>
    // MMONTOYA [ADT-RCT-092 Roaming con corte]
    // Validación del servicio roaming recurrente.
    if (!validateSelectedRecurrentRoamingService(hdnSpecification, <%=strOrderId%>)) {
        return;
    }

        /****************INICIO DE RECOGER ACTUALIZAR EL VECTOR*********************/
        form = document.frmdatos;
        try{
            varTemporal = parent.mainFrame.frmdatos.hdnIdCM.value +"|"+parent.mainFrame.frmdatos.hdnNameCM.value+"|"+parent.mainFrame.frmdatos.hdnNameShortCM.value+"|"+parent.mainFrame.frmdatos.hdnOriginalProductIdCM.value;
        }catch(e){}


	//PRY-0890 JBALCAZAR
	//enviamos el monto de prorrateo vacio para enviarlo al header.
    	var objTotalProrrateo = parent.mainFrame.frmdatos.txt_MontoProrrateo;	
        try{
        	if(objTotalProrrateo){
        		$("input[name='txt_MontoProrrateo']").val("");       		        	
        		}        	
           }catch(e){}
	
	
        //Validad cantidad de items para las ordenes de portabilidad alta
        if (hdnSpecification == spec_PortabilidadAlta){
            if (!fxValidatePortabilityItems()){
                if(sumaCantidadesItems()<100){
                    $("input[name='txt_ItemQuantity']").focus();
                    $("input[name='txt_ItemQuantity']").select();
                }else{
                    parent.close();
                }
                return false;
            }
        }

        //Se agregan los servicios de arrendamiento al vector final de servicios
        //----------------------------------------------------------------------
        try{
            AddServicesDefault();
        }catch(e){}

        try{
            form.item_services.value = GetSelectedServices();
        }catch(e){}


        try{
            form.item_services.value = GetSelectedServices();
        }catch(e){}
        if(fxIsValidate()){
            var bResult;
            if(form.txt_ItemMoneda != null){
                bResult = fxCurrencyValidate(form.txt_ItemMoneda.value, 1);
            }
            else{
                bResult = true;
            }

            //INI EZM Validación compatibilidad Modelo - Plan - Servicio
            if (bResult){
                //Inicio SAR N_O000005779 - FPICOY - 20/11/2013
                //Antes de agregar el item valida para la categoria Cambio de Plan, si el plan al que desea cambiarse un empleado es Comercial o no.
                if ((hdnSpecification == spec_ChangePlan || hdnSpecification == spec_ChangeModelTech) && form.hdnflagChangePlan.value!=""){ //SAR N_O000012485 - FPICOY - 09/01/2014
                    alert(form.hdnflagChangePlan.value);//SAR N_O000012485 - FPICOY - 09/01/2014
                    return false;
                }
                //Fin SAR N_O000005779 - FPICOY - 20/11/2013
                for( x = 0; x < vctItemOrder.size(); x++ ){
                    var nameHTMLInput = vctItemOrder.elementAt(x).namehtmlitem;
                    /*Obtenemos el Value*/

                    try{
                        vctItemOrder.elementAt(x).npobjitemvalue = eval("form." + nameHTMLInput + ".value");
                    }catch(e){}


                    /**SOLO SI ES REPOSICIONES O CAMBIO DE MODELO (ARTIFICIO TEMPORAL) **/

                    if( vctItemOrder.elementAt(x).npobjheaderid == 52 || vctItemOrder.elementAt(x).npobjheaderid == 12 ){

                        if( eval("form." + nameHTMLInput + ".value") == "SI" ) vctItemOrder.elementAt(x).npobjitemvalue = "S";

                        else if( eval("form." + nameHTMLInput + ".value") == "NO" ) vctItemOrder.elementAt(x).npobjitemvalue = "N";

                        else  vctItemOrder.elementAt(x).npobjitemvalue = eval("form." + nameHTMLInput + ".value");

                    }

                    /**SOLO SI ES REPOSICIONES O CAMBIO DE MODELO (ARTIFICIO TEMPORAL) **/


                    /*Obtenemos la Description*/
                    if ( vctItemOrder.elementAt(x).npcontroltype == "SELECT" ){
                        try{
                            var valie = eval("form." + nameHTMLInput + ".options[form."+nameHTMLInput+".selectedIndex].text");
                            vctItemOrder.elementAt(x).npobjitemvaluedesc = valie;
                        }catch(e){}
                    }else{
                        vctItemOrder.elementAt(x).npobjitemvaluedesc = eval("form." + nameHTMLInput + ".value");
                    }
                    //Reserva de Numeros Golden - Inicio RHUACANI-ASISTP - 24/10/2010
                    if (vctItemOrder.elementAt(x).npobjitemid == <%=Constante.ITEM_ID_RESERVA_NUMEROS%>){
                        valueDescription=fxGetStringElementsByMultiSelectControl(nameHTMLInput);
                        vctItemOrder.elementAt(x).npobjitemvalue=valueDescription;
                    }
                    //Fin RHUACANI-ASISTP - 24/10/2010
                    vctItemOrder.elementAt(x).npobjitemflagsave = 'A';

                    //DERAZO REQ-0428
                    //EFLORES REQ-0428_2 Se cambia logica
                    if(parent.opener.flagPenaltyFunct == 1){
                        if(nameHTMLInput == "txt_PagoPenalidad"){
                            var numAddenActive = parent.mainFrame.document.getElementById("h_penalidad").value;
                            if(numAddenActive == "0"){
                                vctItemOrder.elementAt(x).npobjitemvalue = "N";
                            }
                            else{
                                <%  System.out.println("[PopUpOrder][strOrderId] Orden "+strOrderId+" ItemId "+strItemId+" tiene adenda activa"); %>
                                vctItemOrder.elementAt(x).npobjitemvalue = "S";
                            }
                        }
                    }
                    //EFLORES CDM+CDP PRY-0817 Se ingresa logica para mantener valor del check
                    if(nameHTMLInput == "chkMantenerSIM"){
                        var chkStatusKeepSim = parent.mainFrame.document.frmdatos.chkMantenerSIM.checked;
                        if(chkStatusKeepSim){
                            vctItemOrder.elementAt(x).npobjitemvalue = "1";
                        }else{
                            vctItemOrder.elementAt(x).npobjitemvalue = "";
                }
                    }
                }
                var lista_servicios = GetSelectedServices();

                /*Si el item se ha modificado, se setea un flag*/
                <%if(!objectType.equals("INC")){%>
                fxValidateItemChange();
                <%}%>

                <%if(!objectType.equals("INC")){%>
                //fxValidateItemChangeWithGuide();

                var isChangeGuide     = 0;
                var isChangePlanRate  = 0;
                var isChangePrice     = 0;
                var isChangePriceExc  = 0;
                for(j = 0; j < vctItemOrder.size(); j ++ ){
                    //Preguntar si hubo un cambio en los campos Precio Cta Inscripción, Precio de Excepción o Plan Tarifario
                    if( vctItemOrder.elementAt(j).npobjitemid != 9 &&     //Plan Tarifario
                            vctItemOrder.elementAt(j).npobjitemid != 17 &&    //Precio
                            vctItemOrder.elementAt(j).npobjitemid != 18 &&    //Precio Excepción
                            vctItemOrder.elementAt(j).npobjitemid != 21 &&    //Precio Promoción
                            vctItemOrder.elementAt(j).npobjitemid != 23 &&    //Servicios Adicionales
                            vctItemOrder.elementAt(j).npobjitemid != 38 &&    //Plantilla Adenda
                            vctItemOrder.elementAt(j).npobjitemid != 43 ){    //Promoción

                        if (vctItemOrder.elementAt(j).npobjitemvalue != vctItemOrderOriginal.elementAt(j).npobjitemvalue){
                            isChangeGuide = 1;
                            //alert("" + vctItemOrder.elementAt(j).npobjitemname + " -> Valor anterior : " + vctItemOrderOriginal.elementAt(j).npobjitemvalue + " -> Valor Actual : " + vctItemOrder.elementAt(j).npobjitemvalue )
                            //break;
                        }
                    }else{
                        //Plan Tarifario
                        if( vctItemOrder.elementAt(j).npobjitemid == 9 && (vctItemOrder.elementAt(j).npobjitemvalue != vctItemOrderOriginal.elementAt(j).npobjitemvalue) ){
                            isChangePlanRate = 1;
                        }
                        //Precio
                        if( vctItemOrder.elementAt(j).npobjitemid == 17 && (vctItemOrder.elementAt(j).npobjitemvalue != vctItemOrderOriginal.elementAt(j).npobjitemvalue) ){
                            isChangePrice = 1;
                        }
                        //Precio de Excepción
                        if( vctItemOrder.elementAt(j).npobjitemid == 18 && (vctItemOrder.elementAt(j).npobjitemvalue != vctItemOrderOriginal.elementAt(j).npobjitemvalue) ){
                            isChangePriceExc = 1;
                        }

                    }
                }

                <%}%>

                <%if(!objectType.equals("INC")){%>
                //Ahora hacemos una validación General

                if( !fxValidateGeneralEdit() ) return false;
                <%}%>
                
                <%
             	// Inicio [validate price exception] CDIAZ
 				if(flagValidationPriceException) {
 				%>
 				if( !fxValidatePriceException() ) return false;
 				<%
 				}
             	// Fin [validate price exception] CDIAZ
 				%>
             	
                fxCalculateTotalItem();

                //JPEREZ: Evalúa la modalidad de salida para el ítem
                try{
                    var strModality = form.cmb_ItemModality[form.cmb_ItemModality.selectedIndex].text;
                    if ( !fxValidateItemModality("<%=hdnSpecification%>", strModality, item_index) ){
                        alert("La Modalidad de Salida debe ser igual para todos los ítems de la orden");
                        return false;
                    }
                }catch(e){}

                //KSALVADOR: Se agrega nuevo parametro Tipo:(Nuevo/Usados) para validar en el stock a que subalmacen pertnecen
                //Actualmente este valor sólo se encuentra configurado para Reposiciones
                try{
                    var strTipo = "";
                    strTipo = form.cmbItemProductStatus.value;
                }catch(e){
                }

                //Validación de Stock  - JPEREZ
                if ("<%=strStockFlag%>"=="Y" ){
                    //Debe validarse stock para la categoría
                    var url = "<%=request.getContextPath()%>/itemServlet?&strMode='EDIT'&strSpecificationId=<%=hdnSpecification%>&strProductId="+form.cmb_ItemProducto.value+"&strDispatchPlace=<%=strDispatchPlace%>&strSaleModality="+form.cmb_ItemModality.value+"&strTipo="+strTipo+"&strSalesStuctOrigenId=<%=strSalesStuctOrigenId%>&hdnMethod=getStockMessage";
                    parent.bottomFrame.location.replace(url);
                }else{
                    try{
                        parent.opener.vIdModelos.objects[parent.opener.frmdatos.hdnItemSelected.value] = varTemporal;
                    }catch(e){}

                    <%if(objectType.equals("INC")){%>
                    parent.opener.parent.mainFrame.fxEditRowOrderItemsGlobal(vctItemOrder,<%=item_index%>);
                    <%}else{%>
                    parent.opener.fxEditRowOrderItemsGlobal(vctItemOrder,<%=item_index%>,isChangeGuide,isChangePlanRate);
                    //Inicio Reserva de Numeros - FPICOY 17/01/2011
                    <%if (hdnSpecification.equals(String.valueOf(Constante.SPEC_CAMBIO_NUMERO))) { %>
                    v_numRows = parent.opener.items_table.rows.length;
                    v_numRows = v_numRows - 1;
                    if (v_numRows>1) {
                        parent.opener.document.frmdatos.item_ptnId[v_numRows-1].value=document.frmdatos.hdnPtnId.value;
                    } else {
                        parent.opener.document.frmdatos.item_ptnId.value=document.frmdatos.hdnPtnId.value;
                    }
                    <%}%>
                    //Fin Reserva de Numeros - FPICOY 17/01/2011
                    <%}%>
                    /****************FIN DE RECOGER ACTUALIZAR EL VECTOR*********************/
                    <%if(!objectType.equals("INC")){%>
                    parent.opener.fxTransferVectorItems(vctItemOrder,<%=item_index%>);
                    <%}%>
                    //Cerrar la ventana

                    //EZUBIAURR 05/01/2011
                    /*******************VALIDACION COMPATIBILIDAD MODELO-PLAN-SERVICIO*****************/
                    <%if ( "A".equals(strflagValidCompMPS)  && ( hdnSpecification.equals(String.valueOf(Constante.SPEC_POSTPAGO_VENTA)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_ACTIVAR_DESACTIVAR_SERVICIOS))
                 || hdnSpecification.equals(String.valueOf(Constante.SPEC_CAMBIO_MODELO_ENTRE_TECNOLOGIAS)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_CAMBIO_MODELO))
                 || hdnSpecification.equals(String.valueOf(Constante.SPEC_EMPLEADO_ASIGNACION)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_EMPLEADO_FAMILIAR))
                 || hdnSpecification.equals(String.valueOf(Constante.SPEC_EMPLEADO_AMIGO)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_PORTABILIDAD_POSTPAGO))
                 || hdnSpecification.equals(String.valueOf(Constante.SPEC_PRESTAMO_CLIENTE_POR_DEMO)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_PRESTAMO_TEST))
                 || hdnSpecification.equals(String.valueOf(Constante.SPEC_CAMBIAR_PLAN_TARIFARIO))
                 || hdnSpecification.equals(String.valueOf(Constante.SPEC_ACT_CAMB_PLAN_BA)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_TRANSFERENCIA_SUB_REG))
                 || hdnSpecification.equals(String.valueOf(Constante.SPEC_TRANSFERENCIA)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_ACCESO_INTERNET))
                 || hdnSpecification.equals(String.valueOf(Constante.SPEC_VENTA_MOVILES_RETAIL_POSTPAGO)) || hdnSpecification.equals(String.valueOf(Constante.SPEC_VENTA_TELFIJA_NUEVA_ADICION))
                 || hdnSpecification.equals(String.valueOf(Constante.SPEC_SSAA_PROMOTIONS))
                 )){%>

                    form.item_id_services.value = getSelectedIdServices();

                    form.item_desc_services.value = getSelectedDescServices();

                    fxValidServicesSelectedListEdit(form.item_id_services.value,form.item_desc_services.value);
                    <% }

                   else {%>

                    fxCancelItemEditWindow();
                    <% } %>


                }// if result
            }//if validate
            else{
                try{
                    DeleteArrServices();
                }catch(e){}
            }
        }
        else{
            try{
                DeleteArrServices();
            }catch(e){}
        }
    }

    /*
     Objetivo:Obtiene una cadena string con los elementos seguido del patron |
     Developer:RHUACANI-ASISTP
     Fecha:24/10/2010
     */
    function fxGetStringElementsByMultiSelectControl(nameHtmlControl) {
        form = document.frmdatos; // PBASUALDO
        strElements = "";
        cantElements=eval("form."+nameHtmlControl+".length");
        if (cantElements==1) {
            cadena = eval("form."+nameHtmlControl+".options[0].text");
            if (trim(cadena).length==0) {
                return;
            }
        }
        for(i=0; i<cantElements;i++){
            if(i==cantElements-1)strElements+=eval("form."+nameHtmlControl+".options["+i+"].text")+","+eval("form."+nameHtmlControl+".options["+i+"].value");
            else strElements += eval("form."+nameHtmlControl+".options["+i+"].text")+","+ eval("form."+nameHtmlControl+".options["+i+"].value") + ",";
        }
        return strElements;
    }

    /**
     Objetivo :  Permite calcular o recalcular el precio del monto total del ITEM
     Developer:  Lee Rosales
     Fecha    :  08/09/2007
     **/
    function fxCalculateTotalItem(){
        var precioCta = "0";
        var precioExc = "";
        var cantidad  = "1";

        //En caso la categoría contenga el campo TOTAL
        if( getTotal() != -1 ){

            for( x = 0; x < vctItemOrder.size(); x++ ){
                objitemid = vctItemOrder.elementAt(x).npobjitemid;
                if( objitemid == 17 || objitemid == 104){ //Agregado por rmartinez para suspensiones temporales cuando el objectId sea 90.
                    nameHTMLInput = vctItemOrder.elementAt(x).namehtmlitem;
                    precioCta  = eval("form." + nameHTMLInput + ".value");
                }if( objitemid == 18 ){
                    nameHTMLInput = vctItemOrder.elementAt(x).namehtmlitem;
                    precioExc  = eval("form." + nameHTMLInput + ".value");
                }if( objitemid == 20 ){
                    nameHTMLInput = vctItemOrder.elementAt(x).namehtmlitem;
                    cantidad   = eval("form." + nameHTMLInput + ".value");
                }
            }/*Fin del for*/

            if( precioExc != "" ) precioCta = precioExc;
            vctItemOrder.elementAt(getTotal()).npobjitemvalue       =   parseFloat(precioCta) * parseInt(cantidad);
            vctItemOrder.elementAt(getTotal()).npobjitemvaluedesc   =   parseFloat(precioCta) * parseInt(cantidad);
        }/*Fin del if(getTotal())*/
    }

    /**
     Objetivo :  Indica si para la categoría actual existe el campo TOTAL
     Developer:  Lee Rosales
     Fecha    :  08/09/2007
     **/
    function getTotal(){
        for( x = 0; x < vctItemOrder.size(); x++ ){
            objitemid = vctItemOrder.elementAt(x).npobjitemid;
            if( objitemid == 22 ) return x;
        }
        return -1;
    }

    function fxValidateDateTime(objectValue){
        var wv_fecha_firma         = objectValue.substring(0,10);
        var wv_hora_firma          = objectValue.substring(11,16);

        if (!isValidDate(wv_fecha_firma) || !isValidHour(wv_hora_firma) ){
            return false;
        }

        return true;
    }

    function fxValidateDateTimePlus(objectValue){
        var wv_fecha_firma         = objectValue.substring(0,10);
        var wv_hora_firma          = objectValue.substring(11,16);

        if( !fxCheckDateCurrent(wv_fecha_firma) )
            return false;
        if (!isValidDate(wv_fecha_firma) || !isValidHour(wv_hora_firma) ){
            return false;
        }

        return true;
    }

    function fxCheckDateCurrent(obj) {
        var wv_datestr = obj;
        var wv_hoy_str = new Date();
        var day_act    = parseFloat(wv_hoy_str.getDate());
        var month_act  = parseFloat(wv_hoy_str.getMonth()+1);
        var year_act   = parseFloat(wv_hoy_str.getYear());

        var wv_message = "La fecha debe ser mayor o igual a la fecha actual";

        if ( wv_datestr != "" ){
            if ( !isValidDate(wv_datestr)){
                //obj.select();
                return false;
            };

            var day_new    = parseFloat(wv_datestr.substring(0,2));
            var month_new  = parseFloat(wv_datestr.substring(3,5));
            var year_new   = parseFloat(wv_datestr.substring(6,10));
            if (year_new < year_act){
                alert(wv_message);
                //obj.select();
                return false;
            };
            if (year_new == year_act){
                if (month_new < month_act){
                    alert(wv_message);
                    //obj.select();
                    return false;
                };
                if (month_new == month_act){
                    if (day_new < day_act){
                        alert(wv_message);
                        //obj.select();
                        return false;
                    };
                }
            }
        }
        return true;
    };

    /**
     Objetivo :  Permite cerrar la ventana del ITEM
     Developer:  Lee Rosales
     Fecha    :  08/09/2007
     **/
    function fxCancelItemEditWindow() {
        <%if(objectType.equals("INC")){%>
        parent.opener.parent.mainFrame.document.frmdatos.flgSave.value="0";
        parent.opener.parent.mainFrame.deleteItemEnabled       =	true;
        parent.close();
        <%}else{%>
        parent.opener.document.frmdatos.flgSave.value="0";
        parent.opener.deleteItemEnabled       =	true;
        parent.close();
        <%}%>
        //Reiniciar todo y se puede seleccionar nuevamente
        formResetGeneral();
    }

    /**
     Objetivo :  Permite obtener
     Developer:  Lee Rosales
     Fecha    :  08/09/2007
     **/
    function fxValidateItemChangeWithGuide(){
        var isChangeGuide     = 0;
        var isChangePlanRate  = 0;
        var isChangePrice     = 0;
        var isChangePriceExc  = 0;
        for(j = 0; j < vctItemOrder.size(); j ++ ){
            //Preguntar si hubo un cambio en los campos Precio Cta Inscripción, Precio de Excepción o Plan Tarifario
            if( vctItemOrder.elementAt(j).npobjitemid != 9 &&     //Plan Tarifario
                    vctItemOrder.elementAt(j).npobjitemid != 17 &&    //Precio
                    vctItemOrder.elementAt(j).npobjitemid != 18 &&    //Precio Excepción
                    vctItemOrder.elementAt(j).npobjitemid != 21 &&    //Precio Promoción
                    vctItemOrder.elementAt(j).npobjitemid != 23 &&    //Servicios Adicionales
                    vctItemOrder.elementAt(j).npobjitemid != 38 &&    //Plantilla Adenda
                    vctItemOrder.elementAt(j).npobjitemid != 43 ){    //Promoción

                if (vctItemOrder.elementAt(j).npobjitemvalue != vctItemOrderOriginal.elementAt(j).npobjitemvalue){
                    isChangeGuide = 1;
                    //alert("" + vctItemOrder.elementAt(j).npobjitemname + " -> Valor anterior : " + vctItemOrderOriginal.elementAt(j).npobjitemvalue + " -> Valor Actual : " + vctItemOrder.elementAt(j).npobjitemvalue )
                    //break;
                }
            }else{
                //Plan Tarifario
                if( vctItemOrder.elementAt(j).npobjitemid == 9 && (vctItemOrder.elementAt(j).npobjitemvalue != vctItemOrderOriginal.elementAt(j).npobjitemvalue) ){
                    isChangePlanRate = 1;
                }
                //Precio
                if( vctItemOrder.elementAt(j).npobjitemid == 17 && (vctItemOrder.elementAt(j).npobjitemvalue != vctItemOrderOriginal.elementAt(j).npobjitemvalue) ){
                    isChangePrice = 1;
                }
                //Precio de Excepción
                if( vctItemOrder.elementAt(j).npobjitemid == 18 && (vctItemOrder.elementAt(j).npobjitemvalue != vctItemOrderOriginal.elementAt(j).npobjitemvalue) ){
                    isChangePriceExc = 1;
                }

            }
        }
        if( isChangeGuide == 1 ){
            alert("hubieron cambios que afectan a la guía");
        }else if( isChangePlanRate == 1 || isChangePrice == 1 || isChangePriceExc == 1 ){
            alert("hubieron cambios que NO afectan a la guía");
            if( isChangePlanRate == 1 ){
                alert("hubieron cambios en Cambio de Plan");
            }
            if( isChangePrice == 1 ){
                alert("hubieron cambios en Precio");
            }
            if( isChangePriceExc == 1 ){
                alert("hubieron cambios en Precio Excepción");
            }
        }
    }

    /*JPEREZ
     Valida si es que un ítem ha sido editado
     */
    function fxValidateItemChange(){
        //vctItemOrderOriginal  = vctItemOrder ;
        for(j = 0; j < vctItemOrder.size(); j ++ ){
            if (vctItemOrder.elementAt(j).npobjitemvalue != vctItemOrderOriginal.elementAt(j).npobjitemvalue){
                parent.opener.document.frmdatos.hdnChangedOrder.value="S";
                break;
            }
        }
    }

    //JPEREZ: Valida que los ítemes de la orden sean de la misma modalidad de salida
    function fxValidateItemModality(specificationId, strItemModality, itemIndex){
        //itemIndex == -1 => ítem nuevo
        //Por ahora sólo para el caso de Venta Prepago (2002)
        var cntTablesItems = parent.opener.items_table.rows.length;
        var npitemobjectvalue;
        var openerForm     = parent.opener.frmdatos;
        if( cntTablesItems == 1 ) return true;
        if (specificationId == <%=Constante.SPEC_PREPAGO_NUEVA%> 
                 || specificationId == <%=Constante.SPEC_PREPAGO_TDE%> ||  specificationId == <%=Constante.SPEC_REPOSICION_PREPAGO_TDE%> ){
            for( i = 0; i < (cntTablesItems-1); i ++ ){
                //Recorro los datos a pasar
                if ( (cntTablesItems == 2) && (itemIndex!=i+1) ){
                    npitemobjectvalue = npitemobjectvalue = parent.opener.frmdatos.txtItemModality.value;
                    if (strItemModality != npitemobjectvalue) return false;
                }
                else if (itemIndex!=i+1){
                    npitemobjectvalue = npitemobjectvalue = parent.opener.frmdatos.txtItemModality[i].value;
                    if (strItemModality != npitemobjectvalue) return false;
                }
            }
            return true;
        }
        else
            return true;
    }

    /*INICIO ADT-BCL-083 --LHUAPAYA*/
    function fxValidateCellBag(strProductLine){
        /*
         var npitemobjectvalue;
         var cntTablesItems = parent.opener.items_table.rows.length;
         //alert('tamaño de la lista: ' + cntTablesItems);
         if(cntTablesItems == 1) return true;
         for(var i=0;i<(cntTablesItems-1);i++){
         if(cntTablesItems==2){
         npitemobjectvalue = parent.opener.frmdatos.hdnItemValuetxtItemProductLine.value;
         if(npitemobjectvalue == strProductLine) return false;
         else return true;
         }else{
         npitemobjectvalue = parent.opener.frmdatos.hdnItemValuetxtItemProductLine[i+1].value;
         if(npitemobjectvalue == strProductLine) return false;
         else return true;
         }
         }
         */
        var table = parent.opener.items_table;
        for(var i = 1;i<table.rows.length;i++){
            for(var j=0;j<table.rows[i].cells.length;j++){
                var nom_div = table.getElementsByTagName('tr')[i].getElementsByTagName('td')[j].getElementsByTagName('div')[0].id;
                var linea = nom_div.replace(/\s+/g, '');
                if("txtItemProductLine" == linea){
                    var valor = table.getElementsByTagName('tr')[i].getElementsByTagName('td')[j].getElementsByTagName('div')[0].getElementsByTagName('input')[1].value;
                    if(strProductLine==valor){
                        return false;
                    }
                }
            }
        }
        return true;
    }
    /*FIN ADT-BCL-083 --LHUAPAYA*/

    /*
     JPEREZ
     Copia un vector
     */
    function fxCopyVector(vctOriginal, vctCopia){
        for( x = 0; x < vctOriginal.size(); x++ ){
            var objMake = new fxMakeOrderItem(
                    vctOriginal.elementAt(x).npobjitemheaderid,
                    vctOriginal.elementAt(x).npobjspecgrpid,
                    vctOriginal.elementAt(x).npobjitemid,
                    vctOriginal.elementAt(x).npobjitemname,
                    vctOriginal.elementAt(x).namehtmlheader,
                    vctOriginal.elementAt(x).namehtmlitem,
                    vctOriginal.elementAt(x).npcontroltype,
                    vctOriginal.elementAt(x).npdefaultvalue,
                    vctOriginal.elementAt(x).npsourceprogram,
                    vctOriginal.elementAt(x).npspecificationgrpid,
                    vctOriginal.elementAt(x).npdisplay,
                    vctOriginal.elementAt(x).npobjreadonly,
                    vctOriginal.elementAt(x).npobjitemvalue,
                    vctOriginal.elementAt(x).npobjitemvaluedesc
            );
            vctCopia.addElement(objMake);
        }

    }

    /*
     JPEREZ:    Maneja la respuesta de stock
     03/03/2009: Dependiendo de strStockFlag se usa un confirm o un alert
     strStockFlag "0" : No se muestra mensaje de stock
     strStockFlag "1" : Se muestra un confirm
     strStockFlag "2" : Se muestra un alert (no continúa)
     */
    function fxStockResponse(strStockFlag, strStockMessage, strMode){
        if ( (strStockFlag == "1") ) {
            if(confirm(strStockMessage)){
                if (strMode == "NEW"){
                    fxAddItem();
                    try{
                        parent.opener.vIdModelos.addElement(varTemporal);
                    }catch(e){}
                }
                else{
                    fxUpdateItem();
                    try{
                        parent.opener.vIdModelos.objects[parent.opener.frmdatos.hdnItemSelected.value] = varTemporal;
                    }catch(e){}
                }
            }
            else{
                try{
                    DeleteArrServices();
                }catch(e){}
            }
        }else if ( strStockFlag == "2" ){
            alert(strStockMessage);
            try{
                DeleteArrServices();
            }catch(e){}
            return false;
        }else if ( strStockFlag == "0" ){
            if (strMode == "NEW"){
                fxAddItem();
                try{
                    parent.opener.vIdModelos.addElement(varTemporal);
                }catch(e){}
            }
            else{
                fxUpdateItem();
                try{
                    parent.opener.vIdModelos.objects[parent.opener.frmdatos.hdnItemSelected.value] = varTemporal;
                }catch(e){}
            }
        }
    }

    //Eliminar los servicios de arrendamiento que se colocaron en la lista de servicios disponibles
    function DeleteArrServices() {
        var wn_cantServ = vServicio.size();

        if ("<%=hdnSpecification%>" == "<%=Constante.SPEC_CAMBIO_MODELO%>"){
            for(j=0; j<wn_cantServ; j++){
                objServicio1 = vServicio.elementAt(j);

                if(document.frmdatos.hdnServArrCM.value == objServicio1.nameShort){
                    vServicio.removeElementAt(j + 1);
                    wn_cantServ = vServicio.size();
                    j = j-1;
                }
            }
        }
    }

    function valueExistsInArrayArren(aArreglo,valor){
        var i=0;
        bReturn = false;
        if (aArreglo!=null){
            if (aArreglo.size()>0){
                for (i=0;i<aArreglo.size();i++){
                    if (aArreglo.elementAt(i).nameShort == valor){
                        bReturn = true;
                        break;
                    }
                }
            }else{
                bReturn = false;
            }
        }
        return bReturn;
    }

    /*
     JPEREZ
     Agrega un ítem a la orden.
     */
    function fxAddItem(){
        <%if( objTypeEvent.equals("EDIT") ){%>
        <%if(objectType.equals("INC")){%>
        parent.opener.parent.mainFrame.fxAddRowOrderItemsGlobalEdit(vctItemOrder);
        <%}else{%>
        parent.opener.fxAddRowOrderItemsGlobalEdit(vctItemOrder);
        <%}%>
        <%}else if( objTypeEvent.equals("NEW") ){ %>
        <%if(objectType.equals("INC")){%>
        parent.opener.parent.mainFrame.fxAddRowOrderItemsGlobal(vctItemOrder);
        <%}else{%>
        parent.opener.fxAddRowOrderItemsGlobal(vctItemOrder);
        <%}%>
        <%}%>
        parent.opener.deleteItemEnabled       =	true;
        //parent.close();
        //Reiniciar todo y se puede seleccionar nuevamente
        formResetGeneral();

    }

    function fxUpdateItem(){
        <%if(objectType.equals("INC")){%>
        parent.opener.parent.mainFrame.fxEditRowOrderItemsGlobal(vctItemOrder,<%=item_index%>);
        <%}else{%>
        var isChangeGuide     = 0;
        var isChangePlanRate  = 0;
        var isChangePrice     = 0;
        var isChangePriceExc  = 0;
        for(j = 0; j < vctItemOrder.size(); j ++ ){
            //Preguntar si hubo un cambio en los campos Precio Cta Inscripción, Precio de Excepción o Plan Tarifario
            if( vctItemOrder.elementAt(j).npobjitemid != 9 &&     //Plan Tarifario
                    vctItemOrder.elementAt(j).npobjitemid != 17 &&    //Precio
                    vctItemOrder.elementAt(j).npobjitemid != 18 &&    //Precio Excepción
                    vctItemOrder.elementAt(j).npobjitemid != 21 &&    //Precio Promoción
                    vctItemOrder.elementAt(j).npobjitemid != 23 &&    //Servicios Adicionales
                    vctItemOrder.elementAt(j).npobjitemid != 38 &&    //Plantilla Adenda
                    vctItemOrder.elementAt(j).npobjitemid != 43 ){    //Promoción

                if (vctItemOrder.elementAt(j).npobjitemvalue != vctItemOrderOriginal.elementAt(j).npobjitemvalue){
                    isChangeGuide = 1;
                    //alert("" + vctItemOrder.elementAt(j).npobjitemname + " -> Valor anterior : " + vctItemOrderOriginal.elementAt(j).npobjitemvalue + " -> Valor Actual : " + vctItemOrder.elementAt(j).npobjitemvalue )
                    //break;
                }
            }else{
                //Plan Tarifario
                if( vctItemOrder.elementAt(j).npobjitemid == 9 && (vctItemOrder.elementAt(j).npobjitemvalue != vctItemOrderOriginal.elementAt(j).npobjitemvalue) ){
                    isChangePlanRate = 1;
                }
                //Precio
                if( vctItemOrder.elementAt(j).npobjitemid == 17 && (vctItemOrder.elementAt(j).npobjitemvalue != vctItemOrderOriginal.elementAt(j).npobjitemvalue) ){
                    isChangePrice = 1;
                }
                //Precio de Excepción
                if( vctItemOrder.elementAt(j).npobjitemid == 18 && (vctItemOrder.elementAt(j).npobjitemvalue != vctItemOrderOriginal.elementAt(j).npobjitemvalue) ){
                    isChangePriceExc = 1;
                }

            }
        }
        parent.opener.fxEditRowOrderItemsGlobal(vctItemOrder,<%=item_index%>,isChangeGuide,isChangePlanRate);
        <%}%>

        /****************FIN DE RECOGER ACTUALIZAR EL VECTOR*********************/

        <%if(!objectType.equals("INC")){%>
        parent.opener.fxTransferVectorItems(vctItemOrder,<%=item_index%>);
        <%}%>

        //Cerrar la ventana
        fxCancelItemEditWindow();
    }
    /*Odubock; valida que para el campo fecha disbaled, no tenga acción sobre el calendario */
    function preShowCalendar(obj,valor1,valor2,strFormato){
        if(eval(obj+".disabled == false") == true){
            show_calendar(obj,valor1,valor2,strFormato);
        }
    }

    <%
 	// Inicio [validate price exception] CDIAZ
	if(flagValidationPriceException) {
	%>
	function fxValidatePriceException() {
		var txt_Exception = $("input[name='txt_ItemPriceException']")[0];
		var isValid = true;
		if (txt_Exception && txt_Exception.value != "") {
			var checks = $("input[name='checkSelec']");
			isValid = false;
			for (var i = 0; i < checks.length; i++) {
				if ((checks[i].checked == true)) {
					isValid = true;
					break;
				}
			}

			if (isValid == false) {
				alert("Debe tener una adenda seleccionada");
			} 

		} 
		return isValid;
	}
	<%
	}
 	// Fin [validate price exception] CDIAZ
	%>

</script>

<body>
<!--  PRY-0762 JQUISPE-->
<input type="hidden" name="hdnFlagBuscarRA" value="<%=isRentaAdelantada%>">
<input type="hidden" name="hdnPrecioPlanRA" value="<%=strPrecioPlan%>">
<form method="post" name="frmdatos">
    <!-- <input type="hidden" name="hdnCurrency" value="">-->
    <table  align="center" width="100%" border="0">

        <tr><td>

            <table border="0" class="PortletHeaderColor" cellspacing="0" cellpadding="0" width="100%">
                <tr class="PortletHeaderColor">
                    <td class="LeftCurve" valign="top" align="left" width="10">&nbsp;&nbsp;</td>
                    <td class="PortletHeaderColor" align="left" valign="top">
                        <font class="PortletHeaderText">
                            Item de Orden
                        </font></td>
                    <td align="right" class="RightCurve" width="10%" >&nbsp;&nbsp;</td>
                </tr>
            </table>
        </td></tr>
        <tr><td>

            <!--Sección de objetos de ITEMS -->
            <table align="center" width="100%" border="0">
                <input type="hidden"  value="<%=MiUtil.getDate("dd/MM/yyyy")%>"  name="txtFechaServidor" >

                <input type="hidden" name="hdnServArrCM" value="">
                <input type="hidden" name="hdnServArrNom" value="">
                <input type="hidden" name="hdnOriginalProductIdCM" value="">
                <input type="hidden" name="hdnHtmlItemCM" value="">
                <input type="hidden" name="hdnIdCM" value="">
                <input type="hidden" name="hdnNameCM" value="">
                <input type="hidden" name="hdnNameShortCM" value="">
                <input type="hidden" name="hdnServDefIdMsj" value="">

                <!-- Para Equipo SIM -->
                <input type="hidden" name="hdnRealModel" value="">
                <input type="hidden" name="hdnRealModelId" value="">
                <input type="hidden" name="hdnRealSim" value="">
                <input type="hidden" name="hdnRealImei" value="">
                <input type="hidden" name="hdnRealProductLine" value="">

                <!-- Para Compatibilidad de Servicios -->
                <input type="hidden" name="item_id_services" value="">
                <input type="hidden" name="item_desc_services" value="">
                <input type="hidden" name="hdnOriginalPlanId" value="">

                <!-- Para Reserva de Numeros - PBASUALDO-->
                <input type="hidden" name="hdnPtnId" value="">

                <input type="hidden" name="hdnPlanServ" value="">
                <input type="hidden" name="hdnflagPlanType" value="1">
                <!-- Inicio SAR N_O000005779/ - FPICOY - 20/11/2013-->
                <input type="hidden" name="hdnflagChangePlan" value="">  <!-- SAR N_O000012485 - FPICOY - 09/01/2014-->
                <!-- Fin SAR N_O000005779 - FPICOY - 20/11/2013-->

                <!-- Inicio SAR [N_O000048087] Oferta Diferenciada - JVILLANUEVA - 11/05/2015-->
                <input type="hidden" name="hdnOffertDif" value="">
                <!-- Fin SAR [N_O000048087] Oferta Diferenciada  - JVILLANUEVA - 11/05/2015-->
                <%
                    try{
                        //Si existe una configuración para el grupo
                        if( objItemHEader != null && objItemHEader.size() > 0 ){

                            NpObjItemSpecgrp objnpObjHeaderSpecgrp = null;
                            String  strExecMaxLength = "";
                            String   valueVisible = "";
                            String  strExecSize = ""; // LHUAPAYA  [ADT-BCL-083]

                            //INICIO - DLAZO - Obtener el ProcessType de la Orden (ACTIVATE o DEACTIVATE).
                            HashMap hshProcessType = objGeneralService.getProcessTypeByOrderId(MiUtil.parseLong((String)strOrderId));
                            String strProcessType = (String)(hshProcessType.get("strProcessType")==null?"":hshProcessType.get("strProcessType"));
                            //FIN - DLAZO

                            for ( int i=0 ; i<objItemHEader.size(); i++ ){
                                objnpObjHeaderSpecgrp = new NpObjItemSpecgrp();
                                objnpObjHeaderSpecgrp =  (NpObjItemSpecgrp)objItemHEader.get(i);

                                //Si no es un tipo de objeto CONTROL
                                if( !Constante.CONTROL_OTRO.equals(objnpObjHeaderSpecgrp.getNpobjitemcontroltype() ) ){
                                    if(objnpObjHeaderSpecgrp.getNpnamehtmlitem().equals("txt_ItemDeactivationDate")){
                                        if(type_window.equals("NEW")){
                                            objnpObjHeaderSpecgrp.setNpdisplay("N");
                                        }else{ //EDIT && DETAIL
                                            strStatus = strStatus==null?"TIENDA01":strStatus;
                                            //INICIO - DLAZO - Visualizar fecha de Desactivación solo en ordenes de desactivación.
                                            if(strProcessType.equals(Constante.TIPO_ORDEN_INTERNA_DEACTIVATE)){
                                                if(!strStatus.equals(Constante.INBOX_PROCESOS_AUTOMATICOS) && !strStatus.equals(Constante.INBOX_ACTIVACION_PROGRAMACION) && !strStatus.equals(Constante.INBOX_CALLCENTER)){
                                                    objnpObjHeaderSpecgrp.setNpdisplay("N");
                                                }else{
                                                    objnpObjHeaderSpecgrp.setNpobjreadonly("S");
                                                }
                                            }else{
                                                objnpObjHeaderSpecgrp.setNpdisplay("N");
                                            }
                                        }//FIN - DLAZO
                                    }
                                    
                                  //JQUISPE PRY-0762
                                    if(!isRentaAdelantada){
                                        if("txt_CantidadRA".equals(objnpObjHeaderSpecgrp.getNpnamehtmlitem()) ||
                                            "txt_TotalRA".equals(objnpObjHeaderSpecgrp.getNpnamehtmlitem())){
                                            	objnpObjHeaderSpecgrp.setNpdisplay("N");
                                        }
                                    }
                %>
                
                <tr id="idDisplay<%=objnpObjHeaderSpecgrp.getNpobjitemheaderid()%>"  style=display:<%=objnpObjHeaderSpecgrp.getNpdisplay().equals("N")?"none":""%> >
                    <td class="CellLabel" align="left" valign="top">
                        <div id="idDisplayValidate<%=objnpObjHeaderSpecgrp.getNpobjitemheaderid()%>">
                            <%
                                //Si el campo es obligatorio
                                if(MiUtil.getString(objnpObjHeaderSpecgrp.getNpvalidateflg()).equals("S") ){ %>
                            <font color="red">&nbsp;*&nbsp;</font>
                            <%}else{%>
                            &nbsp;&nbsp;&nbsp;
                            <%}%>

                            <%=objnpObjHeaderSpecgrp.getNpobjitemname()%>&nbsp;
                        </div>
                    <td align="left" class="CellContent">&nbsp;

                        <%//Si es un TEXT. Elegimos que mostrar
                            if( Constante.CONTROL_TEXT.equals(objnpObjHeaderSpecgrp.getNpobjitemcontroltype() ) ){

                                /*INICIO LHUAPAYA  [ADT-BCL-083]*/
                                if( objnpObjHeaderSpecgrp.getNpnamehtmlitem().equals("cmb_ItemProductoOrigen")  && (MiUtil.parseInt(hdnSpecification) == Constante.SPEC_BOLSA_DESACTIVAR || MiUtil.parseInt(hdnSpecification) == Constante.SPEC_BOLSA_UPGRADE || MiUtil.parseInt(hdnSpecification) == Constante.SPEC_BOLSA_DOWNGRADE)){
                                    strExecSize = "size=35";
                                }else{
                                    strExecSize = "";
                                }
                                /*FIN LHUAPAYA  [ADT-BCL-083]*/

                                if( objnpObjHeaderSpecgrp.getNplength() > 0 )
                                    strExecMaxLength = "maxlength='"+objnpObjHeaderSpecgrp.getNplength()+"'";
                        %>

                        <input type="TEXT" <%=strExecMaxLength%>  <%=strExecSize%>  value="<%=MiUtil.getString(objnpObjHeaderSpecgrp.getNpdefaultvalue())%>" <%=objnpObjHeaderSpecgrp.getNpobjreadonly().equals("S")?"readonly":""%> name="<%=objnpObjHeaderSpecgrp.getNpnamehtmlitem()%>" onBlur="fxActionObjectText('<%=objnpObjHeaderSpecgrp.getNpnamehtmlitem()%>',this)" >
                        <%
                            if (hdnSpecification.equals("2055")){
                                if (objnpObjHeaderSpecgrp.getNpnamehtmlitem().equals("txt_ItemIMEI")){
                                    if (type_window.equals("NEW")||type_window.equals("EDIT")  ){
                        %>
                        <a href="javascript:fShowEquipPendRecojo();">Equipos Pend. Recojo y Robo</a>
                        <%
                                    }
                                }
                            }
                            if (objnpObjHeaderSpecgrp.getNpnamehtmlitem().equals("txt_ItemSIM_Eagle")){
                        %>
                        <label id="idValidateImeSim"/>
                        <%
                            }
                        %>
                        <!--
                            Se define la etiqueta del objeto item.
                            En caso sea un tipo de dato fecha se debe de validar agregar un
                            link para ingresar la fecha
                        -->
                        <%
                            if( MiUtil.getString(objnpObjHeaderSpecgrp.getNpdatatype()).equals(Constante.VALIDATE_DATE) ||
                                    MiUtil.getString(objnpObjHeaderSpecgrp.getNpdatatype()).equals(Constante.VALIDATE_DATE_PLUS) ||
                                    MiUtil.getString(objnpObjHeaderSpecgrp.getNpdatatype()).equals(Constante.VALIDATE_DATE_TIME) ||
                                    MiUtil.getString(objnpObjHeaderSpecgrp.getNpdatatype()).equals(Constante.VALIDATE_DATE_TIME_PLUS)
                                    ){
                        %>
                        <%if(MiUtil.getString(objnpObjHeaderSpecgrp.getNpobjreadonly()).equals("S")){%>
                        <a>
                            <img src="<%=Constante.PATH_APPORDER_SERVER%>/images/show-calendar.gif" width="24" height="22" border="0">
                        </a>
                        <%}else{%>
                        <!--a href="javascript:show_calendar('frmdatos.<%=objnpObjHeaderSpecgrp.getNpnamehtmlitem()%>',null,null,'DD/MM/YYYY');"-->
                        <a href="javascript:preShowCalendar('frmdatos.<%=objnpObjHeaderSpecgrp.getNpnamehtmlitem()%>',null,null,'DD/MM/YYYY');">
                            <img src="<%=Constante.PATH_APPORDER_SERVER%>/images/show-calendar.gif" width="24" height="22" border="0">
                        </a>
                        <%}%>
                        <%
                            if( MiUtil.getString(objnpObjHeaderSpecgrp.getNpdatatype()).equals(Constante.VALIDATE_DATE) ||
                                    MiUtil.getString(objnpObjHeaderSpecgrp.getNpdatatype()).equals(Constante.VALIDATE_DATE_PLUS)
                                    ){
                        %>
                        &nbsp;DD/MM/YYYY
                        <%}%>

                        <%
                            if( MiUtil.getString(objnpObjHeaderSpecgrp.getNpdatatype()).equals(Constante.VALIDATE_DATE_TIME) ||
                                    MiUtil.getString(objnpObjHeaderSpecgrp.getNpdatatype()).equals(Constante.VALIDATE_DATE_TIME_PLUS)
                                    ){
                        %>
                        &nbsp; DD/MM/YYYY hh:mm
                        <%}%>

                        <%
                            }
                        %>
                        <%//Si es un SELECT
                        }else if( Constante.CONTROL_SELECT.equals(objnpObjHeaderSpecgrp.getNpobjitemcontroltype() ) ) {%>
                        <%
                            String path =  "/htdocs/jsp/controls/"+objnpObjHeaderSpecgrp.getNpsourceprogram()+"&nameObjectHtml="+objnpObjHeaderSpecgrp.getNpnamehtmlitem()+"&strObjectReadOnly="+objnpObjHeaderSpecgrp.getNpobjreadonly()+"&strObjectValidate="+objnpObjHeaderSpecgrp.getNpvalidateflg()+"&strObjectHeaderId="+objnpObjHeaderSpecgrp.getNpobjitemheaderid();
                        %>
                        <jsp:include page='<%=path%>' flush="true" />
                        <%/*INICIO ADT-BCL-083 --LHUAPAYA*/
                        }else if( Constante.CONTROL_CHECK.equals(objnpObjHeaderSpecgrp.getNpobjitemcontroltype())) {%>
                        <input type="checkbox" name="<%=objnpObjHeaderSpecgrp.getNpnamehtmlitem()%>" onclick="javascript:fxHidenFields(this)">
                        <!-- FIN ADT-BCL-083 --LHUAPAYA -->
                        <%}%>

                    </td>

                </tr>
                <%
                    //Si es un CONTROL
                }else{
                    String path2 =  "/htdocs/jsp/controls/"+objnpObjHeaderSpecgrp.getNpsourceprogram()+"&nameObjectHtml="+objnpObjHeaderSpecgrp.getNpnamehtmlitem()+"&strObjectReadOnly="+objnpObjHeaderSpecgrp.getNpobjreadonly()+"&strObjectValidate="+objnpObjHeaderSpecgrp.getNpvalidateflg()+"&strObjectHeaderId="+objnpObjHeaderSpecgrp.getNpobjitemheaderid();
                %>
                <tr id="idDisplay<%=objnpObjHeaderSpecgrp.getNpobjitemheaderid()%>"  style=display:<%=objnpObjHeaderSpecgrp.getNpdisplay().equals("N")?"none":""%> >

                    <jsp:include page='<%=path2%>' flush="true" />


                </tr>
                <%}%>

                <%} //Fin del for para los Text y Select
                    //For para los campos de tipo OTROS
                }else{
                %>
                <tr>
                    <td class="CellLabel" align="left" valign="top">No se ha configurado ningún item para esta sub-categoría. Consulte con Sistemas</td>
                </tr>
                <%
                    }

                }catch(Exception ex){
                    ex.printStackTrace();
                %>
                <script>alert("<%=MiUtil.getMessageClean(ex.getMessage())%>")</script>
                <%
                    }
                %>
            </table>
            <!--Fin de Sección de objetos de ITEMS -->

            <table><tr align="center"><td></td></tr></table>
            <table border="0" cellspacing="0" cellpadding="0" align="center">
                <tr>
                    <% if( type_window.equals("EDIT") ) {%>
                    <td align="center"><input type="button" name="btnAceptar"  value=" Aceptar " onclick="javascript:fxSendItemValuesEditOrder();">&nbsp;&nbsp;&nbsp;</td>

                    <%}else if( type_window.equals("NEW") ){%>
                    <td align="center"><input type="button" name="btnAceptar"  value=" Agregar " onclick="javascript:fxSendItemValuesOrder();">&nbsp;&nbsp;&nbsp;</td>
                    <%}else if( type_window.equals("DETAIL") ){%>
                    <td align="center"><input type="button" name="btnAceptar"  value=" Aceptar " disabled>&nbsp;&nbsp;&nbsp;</td>
                    <%}%>
                    <td align="center"><input type="reset"  name="btnCerrar"   value=" Cerrar "  onclick="javascript:fxCancelItemEditWindow();"></td><!--javascript:CancelItemEditWindow();-->
                </tr>
            </table>

        </td></tr>
    </table>
</form>
</body>
</html>

<script language="javascript" >
    /**
     Developer : Lee Rosales
     */
    var vctItemOrder = new Vector();
    var vctItemOrderOriginal = new Vector();
    document.getElementById('idDisplay151').style.display='none';
    //Reserva de Numeros Golden - RHUACANI-ASISTP - 24/10/2010
    function fxValidateQuantityValues(){
        var tbox = document.getElementById('txt_ItemQuantity');
        var select = document.getElementById('cmb_ItemAssignedNumber');

        if (parseInt(tbox.value) == select.length) {
            return true;
        } else {
            // Si no se cumple la condicion...
            alert('[ERROR] La cantidad ingresada es diferente a la cantidad de los numeros a reservar');
            return false;
        }
        return false;
    }

    function loadItems(){
        if ( !(vctItemOrder.size() > 0) ) {
            <%
            if( objItemHEader != null && objItemHEader.size() > 0 ){
                NpObjItemSpecgrp objnpObjHeaderSpecgrpItem = null;
                for ( int j=0 ; j<objItemHEader.size(); j++ ){
                String  strCadena = "'";
                    objnpObjHeaderSpecgrpItem = new NpObjItemSpecgrp();
                    objnpObjHeaderSpecgrpItem =  (NpObjItemSpecgrp)objItemHEader.get(j);

               strCadena += objnpObjHeaderSpecgrpItem.getNpobjitemheaderid() + "' , '";
               strCadena += objnpObjHeaderSpecgrpItem.getNpobjspecgrpid() + "' , '";
               strCadena += objnpObjHeaderSpecgrpItem.getNpobjitemid() + "' , '";
               strCadena += objnpObjHeaderSpecgrpItem.getNpobjitemname() + "' , '";
               strCadena += objnpObjHeaderSpecgrpItem.getNpnamehtmlheader() + "' , '";
               strCadena += objnpObjHeaderSpecgrpItem.getNpnamehtmlitem() + "' , '";
               strCadena += objnpObjHeaderSpecgrpItem.getNpobjitemcontroltype() + "' , '";
               strCadena += objnpObjHeaderSpecgrpItem.getNpdefaultvalue() + "' , '";
               strCadena += objnpObjHeaderSpecgrpItem.getNpsourceprogram() + "' , '";
               strCadena += objnpObjHeaderSpecgrpItem.getNpspecificationgrpid() + "' , '";
               strCadena += objnpObjHeaderSpecgrpItem.getNpdisplay() + "' , '";
               strCadena += objnpObjHeaderSpecgrpItem.getNpobjreadonly() + "' , '";
               strCadena += "' , '";//Value
               strCadena += "' , '";//Desc
               strCadena += "' , '";//Flag Save
               strCadena += "' , '";//Item Id
               strCadena += objnpObjHeaderSpecgrpItem.getNpdatatype() + "' , '";
               strCadena += objnpObjHeaderSpecgrpItem.getNpvalidateflg() + "' , '";
               strCadena += objnpObjHeaderSpecgrpItem.getNplength() + "'";
            %>
            vctItemOrder.addElement(new fxMakeOrderItem(<%=strCadena%>));
            <%
                }//Fin del For
            }//Fin del If
            %>
        }
    }

    function fxLoadValues(){
        var form = document.frmdatos;
        <%
          if( type_window.equals("EDIT") || type_window.equals("DETAIL") ){
            String[] paramNpobjitemheaderid = request.getParameterValues("a");
            String[] paramNpobjitemvalue    = request.getParameterValues("b");
              for(int i=0; i<paramNpobjitemheaderid.length; i++){
                if("122".equals(paramNpobjitemheaderid[i])){ // Campo txtItemImeiSim relacionado al SIM (IMEI para Eagle).
                  strSimImei = MiUtil.escape2(paramNpobjitemvalue[i]);
                }
          %>
        for( x = 0; x < vctItemOrder.size(); x++ ){

            if ( vctItemOrder.elementAt(x).npobjitemheaderid == <%=paramNpobjitemheaderid[i]%> ){
                try{
                    if( vctItemOrder.elementAt(x).namehtmlitem != "hdn_ListAdenda" ){
                        if( vctItemOrder.elementAt(x).namehtmlitem != "item_services" ){
                            if("<%=paramNpobjitemheaderid[i]%>"!="73"){
			        //EFLORES CDM+CDP REQ-0817
                                if(vctItemOrder.elementAt(x).npcontroltype != "CHECK"){
                                var strParamDesc  = "form." + vctItemOrder.elementAt(x).namehtmlitem + ".value = '<%=MiUtil.escape2(paramNpobjitemvalue[i])%>'";
                                vctItemOrder.elementAt(x).npobjitemvalue = "<%=MiUtil.escape2(paramNpobjitemvalue[i])%>";
                                eval(strParamDesc);
                                }else{

                                    var strModality = form.cmb_ItemModality[form.cmb_ItemModality.selectedIndex].text;
                                    if (( form.txt_ItemOriginalSolution.value == "Smart Postpago" || form.txt_ItemOriginalSolution.value =="Smart Prepago" ) && (form.cmb_ItemSolution.value=="15" || form.cmb_ItemSolution.value=="16")
                                        &&(strModality=="Alquiler" ||strModality=="Venta")
                                        ) //vasp
                                    {
                                        document.getElementById('idDisplay151').style.display='';
                            }

                                    <% if(MiUtil.escape2(paramNpobjitemvalue[i]).equals("1")){ %>
                                     document.getElementById('idDisplay151').style.display='';
                                     var strParamDesc  = "form." + vctItemOrder.elementAt(x).namehtmlitem + ".setAttribute('checked','checked')";
                                    eval(strParamDesc);
                                    <%}%>
                                    
                                    

                                }

                            }
                        }else{
                            fxCargaServiciosItem("<%=MiUtil.escape2(paramNpobjitemvalue[i])%>");
                            fxCargaServiciosAdicionlesItem("<%=MiUtil.escape2(paramNpobjitemvalue[i])%>");
                        }
                    }
                }catch(e){}
                break;
            }
        }
        <%}//Fin de for
       }//Fin del if%>

        try{
            if('<%=hdnSpecification%>' == '<%=Constante.SPEC_POSTPAGO_VENTA%>' || '<%=hdnSpecification%>' == '<%=Constante.SPEC_PORTABILIDAD_POSTPAGO%>'){
                fxUpdatePriceVO();
            }
        }catch(e){
            //alert("Error fxUpdatePriceVO");
        }
    }

    function fxSetDetail(){
        <%if(type_window.equals("DETAIL") ){%>
        var x= document.getElementById("frmdatos");
        for (var i=0;i<x.length;i++){
            try{
                if(x.elements[i].name == "cmb_ItemContact")fxObjectConvertImgByParam("","HIDE",61);
                x.elements[i].disabled = true;
            }catch(e){}
        }
        x.btnCerrar.disabled = false;
        <%}%>
    }

    function fxShowHideControls(){
        /*   if (document.frmdatos.cmb_ItemContractServ == undefined)
         return false;
         */
        /*var selection = document.frmdatos.cmb_ItemContractServ.options[document.frmdatos.cmb_ItemContractServ.selectedIndex].value;
         document.frmdatos.txt_ItemContractNumber.value="";

         if (selection=="S"){                       // Compartida
         fxOcultarTr("yes",idDisplay27);
         fxOcultarTr("none",idDisplay28);
         //alert("entro jsp");
         fxOcultarTr("none",idDisplay30);
         fxOcultarTr("none",idDisplay18);
         }else{                                     // Anterior
         fxOcultarTr("none",idDisplay27);
         fxOcultarTr("yes",idDisplay28);
         }*/
    }

    function fxViewAddendumAct(){
        <%
        String numAddenAct = (String)request.getAttribute("numAddenAct");
        if(numAddenAct != null){
            if (!(("0").equals(numAddenAct))){
            %>
        alert("El contrato tiene adendas vigentes, le recomendamos verificarlas.");
        <%
        }
    }
    %>
    }

    /*DERAZO Muestra el link ver penalidad simulada al editar producto*/
    function fxShowPenaltySimulatorEdit(specificationId, customerId, phone){
        $.ajax({
            url:"<%= actionURL_PenaltyServlet%>",
            type:"POST",
            dataType:'json',
            data:{
                "method":"showPenaltySimulatorEdit",
                "specificationId" : specificationId,
                "customerId":customerId,
                "phone":phone
            },
            success:function(data){
                if(!(typeof data.strMessage === 'undefined')) {
                    alert("Error:"+data.strMessage);
                }
                else{
                    var flag = data.flagShowPenaltySim;

                    if(flag == 1){
                        parent.mainFrame.document.getElementById("h_penalidad").value = "1";
                        parent.mainFrame.document.getElementById("v_penalidad").style.display = "inline-block";
                    }
                }
            },
            error:function(xhr, ajaxOptions, thrownError){
                console.log("Ocurrio un error al mostrar el link de penalidad simulada");
                if (data.status === 0) {
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
    }

    /*Inicio JPEREZ*/
    function fxVoipFields(){
        var form = parent.mainFrame.frmdatos;
        var specificationId = <%=hdnSpecification%>;
        var solution = form.cmb_ItemSolution[form.cmb_ItemSolution.selectedIndex].value;
        var modality = "";
        var productLine = "";
        var product = "";

        try{
            modality = form.cmb_ItemModality.value;
            productLine = form.cmb_ItemProductLine.value;
            product = form.cmb_ItemProducto.value;
        }catch(e){
            modality = "";
            productLine = "";
            product = "";
        }
        if (specificationId == "<%=Constante.KN_VTA_TELEFONIA_FIJA_BA%>"){
            if (solution  == "<%=Constante.KN_TELEFONIA_FIJA%>"){
                try{
                    fxOcultarTr("none",idDisplay100); //Contrato existente internet
                    fxOcultarTr("none",idDisplay101); //Contrato existente datos
                    fxOcultarTr("none",idDisplay99); //Num existente de telefonía   //nbravo
                    fxOcultarTr("yes",idDisplay111); //Nodo principal
                    fxOcultarTr("yes",idDisplay33); //Tipo de Enlace
                    fxOcultarTr("yes",idDisplay26); //Contrato a asociar
                    fxOcultarTr("yes",idDisplay34); //Tipo de Nodo
                }catch(e){}
            }else if (solution  == "<%=Constante.KN_ACCESO_INTERNET%>"){
                try{
                    fxOcultarTr("none",idDisplay99);  //Num existente de telefonía
                    fxOcultarTr("none",idDisplay101); //Contrato existente datos
                    fxOcultarTr("yes",idDisplay100); //Contrato existente internet
                    fxOcultarTr("yes",idDisplay111);  //Nodo principal
                    fxOcultarTr("yes",idDisplay33); //Tipo de Enlace
                    fxOcultarTr("none",idDisplay26); //Contrato a asociar
                    fxOcultarTr("yes",idDisplay34); //Tipo de Nodo
                }catch(e){}
            }else if (solution  == "<%=Constante.KN_ENLACE_DATOS%>"){
                try{
                    fxOcultarTr("none",idDisplay99);  //Num existente de telefonía
                    fxOcultarTr("none",idDisplay100); //Contrato existente internet
                    fxOcultarTr("none",idDisplay111);  //Nodo principal
                    fxOcultarTr("yes",idDisplay101); //Contrato existente datos
                    fxOcultarTr("none",idDisplay33); //Tipo de Enlace
                    fxOcultarTr("yes",idDisplay26); //Contrato a asociar
                    fxOcultarTr("none",idDisplay34); //Tipo de Nodo
                }catch(e){}
            }
        }else if (specificationId == "<%=Constante.KN_VTA_INTERNET_ACC_INTERNET%>"){
            try{
                fxOcultarTr("none",idDisplay99);  //Num existente de telefonía
                fxOcultarTr("none",idDisplay101); //Contrato existente datos
                fxOcultarTr("yes",idDisplay100); //Contrato existente internet
                fxOcultarTr("yes",idDisplay111);  //Nodo principal
            }catch(e){}
        }else if (specificationId == "<%=Constante.KN_VTA_INTERNET_ENLACE_DATOS%>"){
            try{
                fxOcultarTr("none",idDisplay99);  //Num existente de telefonía
                fxOcultarTr("none",idDisplay100); //Contrato existente internet
                fxOcultarTr("none",idDisplay111);  //Nodo principal
                fxOcultarTr("yes",idDisplay101); //Contrato existente datos
            }catch(e){}
        } else if(modality == '<%=hshtinputNewSection.get("strModality")%>' && productLine == '<%=hshtinputNewSection.get("strProductLine")%>' && product == '<%=hshtinputNewSection.get("strProduct")%>'){
            fxOcultarTr("none",idDisplay121);  //ocultar el campo IMEI (Cliente)
        }

    }

    function fxContractFieldsByProductLine(){
        var form = parent.mainFrame.frmdatos;
        var specificationId = <%=hdnSpecification%>;
        var solution = form.cmb_ItemSolution[form.cmb_ItemSolution.selectedIndex].value;
        var productLine = "";
        try{
            productLine = form.cmb_ItemProductLine[form.cmb_ItemProductLine.selectedIndex].value;
        }catch(e){}
        <%if(!type_window.equals("NEW") ){%>
        if (specificationId == <%=Constante.KN_VTA_TELEFONIA_FIJA_BA%>){
            if ( (solution  == "<%=Constante.KN_TELEFONIA_FIJA%>") && (productLine=="<%=Constante.PRODUCT_LINE_SERV_INST_TF%>") ){
                try{
                    fxOcultarTr("none",idDisplay10);
                    fxOcultarTr("none",idDisplay100);
                    fxOcultarTr("none",idDisplay101);
                    fxOcultarTr("none",idDisplay99);    //nbravo
                    //form.txt_ItemNumeroExistenteTF.value="";  //nbravo
                }catch(e){}
            }
            if ( (solution  == "<%=Constante.KN_TELEFONIA_FIJA%>") && (productLine!="<%=Constante.PRODUCT_LINE_SERV_INST_TF%>") ){
                try{
                    fxOcultarTr("yes",idDisplay100);
                    fxOcultarTr("yes",idDisplay101);
                    fxOcultarTr("yes",idDisplay99);
                }catch(e){}
            }
        }
        //Modificacion HPPTT+ - Garantia Extendida - FPICOY
        if (specificationId == <%=Constante.SPEC_POSTPAGO_VENTA%> ){
            if (productLine==<%=Constante.PRODUCT_LINE_KIT_GAR_EXT%>){
                try{
                    fxOcultarTr("none",idDisplay124);
                }catch(e){}
            }
            if (productLine==<%=Constante.PRODUCT_LINE_KIT_GOLDEN%>){
                try{
                    fxOcultarTr("none",idDisplay123);
                    fxOcultarTr("none",idDisplay124);
                }catch(e){}
            }
        }
        <%}%>
    }

    /*Fin JPEREZ*/

    /*INICIO ADT-BCL-083 --LHUAPAYA*/
    //EFLORES CDM+CDP PRY-0817 Se restringe la funcionalidad segun el nombre del elemento HTML para mantener el metodo.
    function fxHidenFields(object){
        var form = document.frmdatos;
        if(object.name == "<%= Constante.NPOBJHTMLNAME_KEEP_SIM %>"){
            //Logica en caso se necesite usar este evento
            var abc="";
        }else{
        if(object.checked){
            document.getElementById("cmb_ItemProductLine").disabled = true;
            document.getElementById("cmb_ItemProductoOrigen").disabled = true;
            document.getElementById("txt_ItemPriceCtaInscrip").disabled = true;
            form.cmb_ItemProductLine.value =null;
            form.cmb_ItemProductoOrigen.value = '';
            form.txt_ItemPriceCtaInscrip.value = '';
        }else{
            document.getElementById("cmb_ItemProductLine").disabled = false;
            document.getElementById("cmb_ItemProductoOrigen").disabled = false;
            document.getElementById("txt_ItemPriceCtaInscrip").disabled = false;
        }
    }
    }
    /*FIN ADT-BCL-083 --LHUAPAYA*/

    //PRY-0762 JQUISPE Limpia campo Renta Adelantada
    function fxResetFieldRentaAdelantada(){
    	var objCantidadRA = parent.mainFrame.frmdatos.txt_CantidadRA;
    	var objTotalRA = parent.mainFrame.frmdatos.txt_TotalRA;
    	
    	if(objCantidadRA){
    		objCantidadRA.value = "";
    	}
    	
    	if(objTotalRA){
    		objTotalRA.value = "";
    	}
    	
    	parent.mainFrame.hdnPrecioPlanRA.value = "";
    }
    
    //PRY-0762 JQUISPE agrega valor Total Renta Adelantada
    function fxAddValueTotalRentaAdelantada(){
 	   var objCantidadRA = parent.mainFrame.form.txt_CantidadRA;
 	   var objTotalRA = parent.mainFrame.form.txt_TotalRA;
 	   
 	   if(objCantidadRA){
 		   var vPrecioPlanRA = parent.mainFrame.hdnPrecioPlanRA.value;
 		   var vCantidadItem = parent.mainFrame.form.txt_ItemQuantity.value;
 		   
 		   if(objTotalRA){
 	 		   fxObjectConvert('txt_TotalRA',"");
 	 		   if(objCantidadRA.value != "" && vPrecioPlanRA != "" && vCantidadItem != ""){
 	 			   var vTotalRA = objCantidadRA.value*vCantidadItem*(parseFloat(vPrecioPlanRA).toFixed(2));
 	 			   fxObjectConvert('txt_TotalRA',vTotalRA.toFixed(2));
 	 		   }
 		   }
 	   }
    }

</script>



<script DEFER>

    function fxloadItemsEdit(){
        loadItems();
        fxShowHideControls();

        <%if( type_window.equals("EDIT") || type_window.equals("DETAIL") ){%>
        try{
            fxOnLoadListAddendum();
        }catch(e){}
        <%}%>
        //alert("0");
        try{
            fxViewAddendumAct();
        }catch(e){}
        //alert("1");
        try{//CBARZOLA:carga los servicios core de un plan
            fxLoadServiceComercialCore();
        }catch(e){}
        try{
            fxLoadServiceAditiional();
        }catch(e){}

        try{
            fxCargaServiciosItem("");

        }catch(e){}
        try{
            fxCargaServiciosPorDefecto();
        }catch(e){}
        //Eventos OnLoad que pueden o no cargarse
        /*try{
         //Para la carga de los Productos
         fxLoadProduct();
         }catch(e){}*/

        try{
            fxLoadEditProduct();
        }catch(e){}

        try{
            fxLoadEditProductDestino();
        }catch(e){}

        try{
            fxLoadEditProductBCType();
        }catch(e){}

        try{
            fxLoadEditProductLine();
        }catch(e){}

        try{
            fxLoadEditPlan();
        }catch(e){}


        try{
            fxLoadEditModalidad();
        }catch(e){}

        try{
            fxLoadEditPromotion();
        }catch(e){}

        try{
            fxLoadEditRatePlanId();
        }catch(e){}

        /*Modificacion HPPTT+ Garantia Extendida - FPICOY*/
        try{
            fxLoadEditModelProduct();
        }catch(e){}

        /*Nuevo Servicio Principal*/
        try{
            fxLoadEditNewMainService();
        }catch(e){}

        //alert("2");

        /*Inicio CESPINOZA*/
        /*Source Address*/
        try{
            fxLoadItemDestinyAddress();
        }catch(e){}
        /*Fin CESPINOZA*/

        //Para Contrato a Asociar
        try{
            fxLoadNpTable();
        }catch(e){}

        try{
            fxLoadNpTable2();
        }catch(e){}
        try{
            fxLoadModalityByPhone();
        }catch(e){}

        try{
            fxLoadSolution();
        }catch(e){}

        fxLoadValues();
        try{
            if ( form.cmb_ItemSolution.value == "<%=Constante.KN_TELEFONIA_FIJA%>" )  //nbravo
            {
                idDisplay20.style.display = 'none';
            }
        }catch(e){}

        try{
            fxContractFieldsByProductLine();
        }catch(e){}

        try{
            fxVoipFields();
        }catch(e){}

        try{
            <%if (hdnSpecification!=null ) {%> // strSpecificationId <> Bolsa cambio MCB001 Se quito la restricción de bolsa Cambio
            fxLoadProductBolsa();
            <%}%>
        }catch(e){}

        try{
            fxLoadFixedPhone();
        }catch(e){}

        // para el producto origen de bolsa de celulares
        try{
            fxLoadProductBolsaOrigen();
        }catch(e){}

        //PRY-0721 DERAZO Cargamos el combo de Region al Editar
        try{
            fxLoadEditRegion();
        }catch(e){}

        //BAFI2 EFLORES Cargamos el combo de provincias
        try{
            fxLoadEditProvince();
        }catch(e){}

        try{
            fxLoadEditDistrict();
        }catch(e){}

        //DERAZO TDECONV003-2 Cargamos el Imei FS del Item
        try{
            fxLoadImeiFS();
        }catch(e){}

        fxSetDetail();
        fxGeneralRules();

        fxLoadSimImei();

        /*Saca una copia del vector*/
        fxCopyVector(vctItemOrder, vctItemOrderOriginal);

        // MMONTOYA [ADT-RCT-092 Roaming con corte]
        // Inicio Carga el combo de servicios roaming y el vector de servicios.
        if (npspecificationId == SPEC_ACTIVAR_PAQUETES_ROAMING && <%=type_window.equals("EDIT")%>) {
            var form = parent.mainFrame.frmdatos;
            var phone = form.txt_ItemPhone.value;
            var planId = form.txt_ItemNewPlantarifarioId.value;
            var url = "<%=request.getContextPath()%>/serviceservlet?paramPhoneNumber=" + phone + "&strPlanId=" + planId + "&myaction=loadRoamingServices";
            parent.bottomFrame.location.replace(url);
    }
        // Fin Carga el combo de servicios roaming y el vector de servicios.

        //DERAZO REQ-0428 Mostramos el link ver penalidad simulada en el editar
        if (<%=type_window.equals("EDIT")%> && parent.opener.flagPenaltyFunct == 1){
            var phoneEdit = parent.mainFrame.frmdatos.txt_ItemPhone.value;
            fxShowPenaltySimulatorEdit(<%=hdnSpecification%>, <%=strCodigoCliente%>, phoneEdit);
        }
        <% if(intModProd == 1){ //[REQ-0710] %>
            parent.mainFrame.document.frmdatos.txt_ItemQuantity.disabled = true;
            parent.mainFrame.document.frmdatos.cmb_ItemTipoIP.disabled = true;
            parent.mainFrame.document.frmdatos.cmb_ItemSolution.disabled = true;
            parent.mainFrame.document.frmdatos.cmb_ItemModality.disabled = true;
            parent.mainFrame.document.frmdatos.cmb_ItemProductLine.disabled = true;
        <% }%>
    }

    onload = fxloadItemsEdit;
  
</script>

<script DEFER>
    /*Developer : Lee Rosales
     Purpose   : Establecer una lógica para las categorías
     */
    var npspecificationId = <%=hdnSpecification%>;
    var npstatus          = '<%=MiUtil.getString(strStatus)%>';
    function fxGeneralRules(){
        //alert("Tamaño : "+vctItemOrder.size());
        //npstatus = 'TIENDA01';
        /**
         CATEGORÍA - TRASLADO
         //Traslado
         **/
        <%if(!type_window.equals("DETAIL")){%>
        if( npspecificationId == 2050 ){
            //Verificamos el INBOX
            if( npstatus == 'FACTIBILIDAD' ) {
                //alert("Entramos a Factibilidad");
                //Se deshabilitan los campos
                //Solucion - 93
                // fxObjectConvertByParam("","DISABLED",93);
                //Dirección de Origen - 59
                fxObjectConvertByParam("","DISABLED",59);
                //Dirección destino - 60
                fxObjectConvertByParam("","DISABLED",60);
                //Contacto - 61
                fxObjectConvertByParam("","DISABLED",61);
                fxObjectConvertImgByParam("","HIDE",61);
                //Telefono Principal - 62
                fxObjectConvertByParam("","DISABLED",62);
                //Teléfono Auxiliar
                fxObjectConvertByParam("","DISABLED",63);
                //Precio Traslado (US$)
                fxObjectConvertByParam("","DISABLED",31);
                //Precio Traslado Exc. (US$)
                fxObjectConvertByParam("","DISABLED",32);
                //Costo Adicional
                fxObjectConvertByParam("","ENABLED",66);
                //Se habilitan y se muestran los campos
                //Fecha Factibilidad - 35
                fxObjectConvertByParam("","ENABLED",35);
                fxObjectConvertByParam("","SHOW",35);
                //Factibilidad - 36
                fxObjectConvertByParam("","ENABLED",36);
                fxObjectConvertByParam("","SHOW",36);
                //Descripción - 80
                fxObjectConvertByParam("","ENABLED",80);
                fxObjectConvertByParam("","SHOW",80);

            }else if( npstatus == 'INSTALACION_ING' ){

                //Se deshabilitan los campos
                //Solucion - 93
                fxObjectConvertByParam("","DISABLED",93);
                //Dirección de Origen - 59
                fxObjectConvertByParam("","DISABLED",59);
                //Dirección destino - 60
                fxObjectConvertByParam("","DISABLED",60);
                //Contacto - 61
                fxObjectConvertByParam("","DISABLED",61);
                fxObjectConvertImgByParam("","HIDE",61);
                //Telefono Principal - 62
                fxObjectConvertByParam("","DISABLED",62);
                //Teléfono Auxiliar
                fxObjectConvertByParam("","DISABLED",63);
                //Precio Traslado (US$)
                fxObjectConvertByParam("","DISABLED",31);
                //Precio Traslado Exc. (US$)
                fxObjectConvertByParam("","DISABLED",32);
                //Costo Adicional
                fxObjectConvertByParam("","DISABLED",66);

                //Se habilitan y se muestran los campos
                //Fecha Factibilidad - 35
                fxObjectConvertByParam("","DISABLED",35);
                fxObjectConvertByParam("","SHOW",35);
                //Factibilidad - 36
                fxObjectConvertByParam("","DISABLED",36);
                fxObjectConvertByParam("","SHOW",36);
                //Descripción - 80
                fxObjectConvertByParam("","ENABLED",80);
                fxObjectConvertByParam("","SHOW",80);
                //Fecha de Instalación - 38
                fxObjectConvertByParam("","ENABLED",38);
                fxObjectConvertByParam("","SHOW",38);

            }else if( npstatus == 'CALLCENTER FF' || npstatus == 'TIENDA01'  ){
                //Deshabilito todo
                //Solucion - 93
                fxObjectConvertByParam("","DISABLED",93);
                //Dirección de Origen - 59
                fxObjectConvertByParam("","ENABLED",59);
                //Dirección destino - 60
                fxObjectConvertByParam("","ENABLED",60);
                //Contacto - 61
                fxObjectConvertByParam("","ENABLED",61);
                //fxObjectConvertImgByParam("","HIDE",61);
                //Telefono Principal - 62
                fxObjectConvertByParam("","ENABLED",62);
                //Teléfono Auxiliar
                fxObjectConvertByParam("","ENABLED",63);
                //Precio Traslado (US$)
                fxObjectConvertByParam("","ENABLED",31);
                //Precio Traslado Exc. (US$)
                fxObjectConvertByParam("","ENABLED",32);
                //Costo Adicional
                //fxObjectConvertByParam("","ENABLED",66);
                fxObjectConvertByParam("","DISABLED",66);
            }else if( npstatus != ''  ){
                //alert('Entramos IF Final');
                //Deshabilito todo
                //Solucion - 93
                fxObjectConvertByParam("","DISABLED",93);
                //Dirección de Origen - 30
                fxObjectConvertByParam("","DISABLED",59);
                //Dirección destino - 60
                fxObjectConvertByParam("","DISABLED",60);
                //Contacto - 61
                fxObjectConvertByParam("","DISABLED",61);
                fxObjectConvertImgByParam("","HIDE",61);
                //Telefono Principal - 62
                fxObjectConvertByParam("","DISABLED",62);
                //Teléfono Auxiliar
                fxObjectConvertByParam("","DISABLED",63);
                //Precio Traslado (US$)
                fxObjectConvertByParam("","DISABLED",31);
                //Precio Traslado Exc. (US$)
                fxObjectConvertByParam("","DISABLED",32);
                //Costo Adicional
                fxObjectConvertByParam("","DISABLED",66);
            }//Inicio CESPINOZA
            else if( npstatus == ''  ){
                //alert('Entramos IF Final');
                //Deshabilito todo
                //Solucion - 93
                //fxObjectConvertByParam("","DISABLED",93);
                //Dirección de Origen - 30
                fxObjectConvertByParam("","ENABLED",59);
                //Dirección destino - 60
                fxObjectConvertByParam("","ENABLED",60);
                //Contacto - 61
                fxObjectConvertByParam("","ENABLED",61);
                //Telefono Principal - 62
                fxObjectConvertByParam("","ENABLED",62);
                //Teléfono Auxiliar
                fxObjectConvertByParam("","ENABLED",63);
                //Precio Traslado (US$)
                fxObjectConvertByParam("","ENABLED",31);
                //Precio Traslado Exc. (US$)
                fxObjectConvertByParam("","ENABLED",32);
                //Costo Adicional
                fxObjectConvertByParam("","DISABLED",66);
            }//Fin CESPINOZA
            /**
             CATEGORÍA - ACTIVAR Y DESACTIVAR
             //Servicios Adicionales - Activar y Desactivar
             **/
        }else if( npspecificationId == 2048 ){
            //Verificamos el INBOX
            if( npstatus == 'FACTIBILIDAD' ) {
                //alert("Entramos a Factibilidad");

                //Se deshabilitan los campos
                //Contrato de Referencia - 27
                fxObjectConvertByParam("","DISABLED",27);
                //Plan Tarifario Original - 16
                fxObjectConvertByParam("","DISABLED",16);
                //Servicio Principal Original - 82
                fxObjectConvertByParam("","DISABLED",82);
                //Servicios Adicionales - 28
                fxObjectSSAA("DISABLED");

                //Se habilitan y se muestran los campos
                //Factibilidad - 36
                fxObjectConvertByParam("","ENABLED",36);
                fxObjectConvertByParam("","SHOW",36);
                //Descripción - 80
                fxObjectConvertByParam("","ENABLED",80);
                fxObjectConvertByParam("","SHOW",80);

            }else if( npstatus == 'INSTALACION_ING' ){

                //Se deshabilitan los campos
                //Contrato de Referencia - 27
                fxObjectConvertByParam("","DISABLED",27);
                //Plan Tarifario Original - 16
                fxObjectConvertByParam("","DISABLED",16);
                //Servicio Principal Original - 82
                fxObjectConvertByParam("","DISABLED",82);
                //Servicios Adicionales - 28
                fxObjectSSAA("DISABLED");

                //Se habilitan y se muestran los campos
                //Factibilidad - 36
                fxObjectConvertByParam("","DISABLED",36);
                fxObjectConvertByParam("","SHOW",36);
                //Descripción - 80
                fxObjectConvertByParam("","ENABLED",80);
                fxObjectConvertByParam("","SHOW",80);

            }else if( npstatus != ''  ){
                //Se deshabilitan los campos
                //Contrato de Referencia - 27
                fxObjectConvertByParam("","DISABLED",27);
                //Plan Tarifario Original - 16
                fxObjectConvertByParam("","DISABLED",16);
                //Servicio Principal Original - 82
                fxObjectConvertByParam("","DISABLED",82);
                //Servicios Adicionales - 28
                fxObjectSSAA("DISABLED");
            }
            /**
             CATEGORÍA - CAMBIO DE MODELO
             **/
        }else if( (npspecificationId == <%=strCategoryCambioModeloId%>)||(npspecificationId == <%=Constante.SPEC_CAMBIO_MODELO_ENTRE_TECNOLOGIAS%>) ){
            var modalidad = document.frmdatos.txt_ItemEquipment.value;
            if (modalidad=="<%=Constante.TIPO_ALQUILER%>") {
                document.frmdatos.cmb_ItemDevolution.value="S";
                document.frmdatos.cmb_ItemDevolution.disabled="true";
            }
            /**

             /**
             CATEGORÍA - REPOSICION
             **/

        }else if( npspecificationId == <%=Constante.SPEC_REPOSICION%> ){
            <% if (strDispatchPlace.equals(Constante.DISPATCH_PLACE_ID_FULLFILLMENT)){ %>
            fxObjectConvertByParam("","HIDE",135);
            fxObjectConvertByParam("","HIDE",137);

            <%}%>
            /**
             CATEGORÍA - CAMBIO DE PLAN TARIFARIO
             //Servicios Adicionales - Cambio de Plan Tarifario
             **/
        }else if( npspecificationId == 2049 ){
            //Verificamos el INBOX
            if( npstatus == 'FACTIBILIDAD' ) {
                //alert("Entramos a Factibilidad");

                //Se deshabilitan los campos
                //Solucion - 93
                fxObjectConvertByParam("","DISABLED",93);
                //Contrato de Referencia - 27
                fxObjectConvertByParam("","DISABLED",27);
                //Plan Tarifario Original - 16
                fxObjectConvertByParam("","DISABLED",16);
                //Servicio Principal Original - 82
                fxObjectConvertByParam("","DISABLED",82);
                //Plan Tarifario Nuevo - 10
                fxObjectConvertByParam("","DISABLED",10);
                //Servicio Principal Nuevo - 83
                fxObjectConvertByParam("","DISABLED",83);
                //Servicios Adicionales - 28
                fxObjectSSAA("DISABLED");

                //Se habilitan y se muestran los campos
                //Factibilidad - 36
                fxObjectConvertByParam("","ENABLED",36);
                fxObjectConvertByParam("","SHOW",36);
                //Descripción - 80
                fxObjectConvertByParam("","ENABLED",80);
                fxObjectConvertByParam("","SHOW",80);
                //Telefonia Fija
                fxObjectConvertByParam("","DISABLED",94);
            }else if( npstatus == 'INSTALACION_ING' ){

                //Se deshabilitan los campos
                //Solucion - 93
                fxObjectConvertByParam("","DISABLED",93);
                //Contrato de Referencia - 27
                fxObjectConvertByParam("","DISABLED",27);
                //Plan Tarifario Original - 16
                fxObjectConvertByParam("","DISABLED",16);
                //Servicio Principal Original - 82
                fxObjectConvertByParam("","DISABLED",82);
                //Plan Tarifario Nuevo - 10
                fxObjectConvertByParam("","DISABLED",10);
                //Servicio Principal Nuevo - 83
                fxObjectConvertByParam("","DISABLED",83);
                //Servicios Adicionales - 28
                fxObjectSSAA("DISABLED");

                //Se habilitan y se muestran los campos
                //Factibilidad - 36
                fxObjectConvertByParam("","DISABLED",36);
                fxObjectConvertByParam("","SHOW",36);
                //Descripción - 80
                fxObjectConvertByParam("","ENABLED",80);
                fxObjectConvertByParam("","SHOW",80);
                //Telefonia Fija
                fxObjectConvertByParam("","DISABLED",94);
            }else if( npstatus != ''  ){
                //Se deshabilitan los campos
                //Solucion - 93
                fxObjectConvertByParam("","DISABLED",93);
                //Contrato de Referencia - 27
                fxObjectConvertByParam("","DISABLED",27);
                //Plan Tarifario Original - 16
                fxObjectConvertByParam("","DISABLED",16);
                //Servicio Principal Original - 82
                fxObjectConvertByParam("","DISABLED",82);
                //Plan Tarifario Nuevo - 10
                fxObjectConvertByParam("","DISABLED",10);
                //Servicio Principal Nuevo - 83
                fxObjectConvertByParam("","DISABLED",83);
                //Servicios Adicionales - 28
                fxObjectSSAA("DISABLED");
                //Telefonia Fija
                fxObjectConvertByParam("","DISABLED",94);
            }
        }
        else if( npspecificationId == 2074 ){
            //Plan Tarifario Nuevo - 10
            fxObjectConvertByParam("","HIDE",10);
            fxObjectConvertByParam("","DISABLED",18);
    }
        <%} else {%> // type_window.equals("DETAIL")
            // MMONTOYA [ADT-RCT-092 Roaming con corte]
            // En modo DETAIL no debe verse el combo Servicios Disponibles.
            if (npspecificationId == SPEC_ACTIVAR_PAQUETES_ROAMING) {
                //Servicios Disponibles - 140
                fxObjectConvertByParam("","HIDE",140);
            }
        <%}%>

        // MMONTOYA [ADT-RCT-092 Roaming con corte]
        // En modo NEW no debe verse el campo Servicio Seleccionado.
        <% if (type_window.equals("NEW")) {%>
            if (npspecificationId == SPEC_ACTIVAR_PAQUETES_ROAMING) {
                //Servicio Seleccionado - 139
                fxObjectConvertByParam("","HIDE",139);
            }
        <%}%>

        // MMONTOYA [ADT-RCT-092 Roaming con corte]
        if (npspecificationId == SPEC_ACTIVAR_PAQUETES_ROAMING) {
            document.frmdatos.txtItemSelectedServiceROA.style.width = "75%";
            document.frmdatos.cmbItemServiceROA.style.width = "75%";
        }

        try{
            parent.mainFrame.frmdatos.txt_ItemModel.disabled = true;
        }catch(e){}

    }

    function fxObjectSSAA(objObjectAction){
        if( objObjectAction == "DISABLED" ){
            eval("form.cmbAvailableServices.disabled = true;");
            eval("form.cmbSelectedServices.disabled = true;");
            eval("form.baddService.disabled = true;");
        }else if( objObjectAction == "ENABLED" ){
            eval("form.cmbAvailableServices.disabled = false;");
            eval("form.cmbSelectedServices.disabled = false;");
            eval("form.baddService.disabled = false;");
        }
    }

    function fxObjectConvertByParam(objObject,objObjectAction,objHeaderId){
        var form = document.frmdatos;
        var objObjectName = "";
        var objObjectValue = "";
        objObjectName  = objObject;

        if( objHeaderId != "" ){
            objObjectValue  = fxGetObjectProperties(objHeaderId);
            objObjectName   = objObjectValue.namehtmlitem;
        }

        if( objObjectAction == "SHOW" ){
            try{
                eval("idDisplay"+objHeaderId+".style.display = ''");
            }catch(e){}
        }else if( objObjectAction == "HIDE" ){
            try{
                eval("idDisplay"+objHeaderId+".style.display = 'none'");
            }catch(e){}
        }else if( objObjectAction == "DISABLED" ){
            try{
                eval("form." + objObjectName + ".disabled = true;");
            }catch(e){}
        }else if( objObjectAction == "ENABLED" ){
            try{
                eval("form." + objObjectName + ".disabled = false;");
            }catch(e){}
        }else if( objObjectAction == "READONLY" ){
            try{
                eval("form." + objObjectName + ".readOnly = true;");
            }catch(e){}
        }else if( objObjectAction == "READ" ){
            try{
                eval("form." + objObjectName + ".readOnly = false;");
            }catch(e){}
        }

    }

    function fxObjectConvertImgByParam(objObject,objObjectAction,objHeaderId){
        var form = document.frmdatos;
        var objObjectName = "";
        var objObjectValue = "";
        objObjectName  = objObject;
        if( objObjectAction == "SHOW" ){
            eval("idImgVD"+objHeaderId+".style.display = ''");
        }else if( objObjectAction == "HIDE" ){
            eval("idImgVD"+objHeaderId+".style.display = 'none'");
        }
    }

    function fxGetObjectProperties(objHeaderId){

        //Verificar si tiene campos mandatorios
        for( x = 0; x < vctItemOrder.size(); x++ ){
            objitemheaderid   = vctItemOrder.elementAt(x).npobjitemheaderid;

            if( objHeaderId == objitemheaderid )
                return vctItemOrder.elementAt(x);
        }

        return -1;

    }

    function fxObjectConvert(objObject,objValue){
        form = document.frmdatos;
        try{
            cadena = "form."+objObject+".value = '"+objValue+"'";
            eval(cadena);
        }catch(e){

        }
    }

    function fxObjectTry(objObject,objValue){
        form = document.frmdatos;
        try{
            cadena = "form."+objObject+".value = '"+objValue+"'";
            eval(cadena);
        }catch(e){

        }
    }


    function fxCleanServicesVector(vServicio){
        for(i=0; i<vServicio.size(); i++){
            vServicio.elementAt(i).modify_new = "N";
        }
    }

    /*Developer: Karen Salvador
     Objetivo : Carga los servicios adicionales al vector vServicio que no se hayan cargado inicialmente
     */
    function fxCargaServiciosAdicionlesItem(servicios_item){

        var url = "<%=request.getContextPath()%>/itemServlet?hdnMethod=getServiceAdicionalList&strSessionId=<%=strSessionId%>&strSpecificationId=<%=hdnSpecification%>";
        wn_cantServ = vServicio.size();
        wv_Serv_bscs = servicios_item;

        var aService    = new Array();
        var idxAServ = 0;
        var n=1;

        var val1= wv_Serv_bscs.indexOf('|')
        var val2= wv_Serv_bscs.indexOf('|',val1+1);
        var servadicid = '';

        for (j=0;j<vServicio.size();j++){
            objServicio = vServicio.elementAt(j);
            aService[idxAServ] = objServicio.id;
            idxAServ++;
        }
        for(i=0; i<n ; i++){
            val1=wv_Serv_bscs.indexOf('|',val1);
            val2=wv_Serv_bscs.indexOf('|',val1+1);
            servid=wv_Serv_bscs.substring(val1+1,val2);
            if(servid==''){
                n=i;
            }else{
                n=n+1;
                // if( !valueExistsInArray(aService,servid) ){
                wv_act=wv_Serv_bscs.substring(val2+1,val2+2);
                wv_mod=wv_Serv_bscs.substring(val2+3,val2+4);
                servadicid = servid;
                url += "&strServiceId="+servadicid+"&strServiceMod="+wv_mod+"&strServiceAct="+wv_act;
                //}
            }
            val1=val2+3;
        }
        //var url = "<%=request.getContextPath()%>/itemServlet?hdnMethod=getServiceAdicionalList&strSessionId=<%=strSessionId%>&strSpecificationId=<%=hdnSpecification%>"+"&strServiceId="+servadicid+"&strServiceMod="+wv_mod+"&strServiceAct="+wv_act;
        parent.bottomFrame.location.replace(url);
    }

    /*Developer: Lee Rosales
     Objetivo : Carga los servicios adicionales a los seleccionados y resta
     los servicios disponibles.
     */
    function fxCargaServiciosItem(servicios_item){



        v_doc  = parent.mainFrame;
        form   = document.frmdatos;

        if (form.cmbAvailableServices!=null){
            deleteAllValues(form.cmbAvailableServices);
            deleteAllValues(form.cmbSelectedServices);
            /*Se deben dejar en "N" los flags del vector*/
            fxCleanServicesVector(vServicio);
            wn_cantServ = vServicio.size();
            wv_Serv_item = servicios_item;
            /* variable para separacion de las "x" en el combo "cmbSelectedServices" */
            var txt_separacion="  ";
            /* tomamos como maximo 10 caracteres (blancos)para almacenar los servicios seleccionados */
            var txt_vacios_2="";
            wv_Serv_bscs = servicios_item;

            //Este for carga la lista de servicios disponibles (lado izquierdo)
            for(i=0; i<wn_cantServ; i++){
                objServicio = vServicio.elementAt(i);

                difServices = 0;
                if( objServicio.name.length < longMaxServices )
                    difServices = longMaxServices - objServicio.name.length;
                for( k = 0; k < difServices; k++ )
                    txt_vacios_2+=" ";

                wv_servicio = objServicio.id + "|";

                /*Antes obtenía el nombre corto*/
                //var txt_servicio = objServicio.nameShortDisplay + txt_vacios_2;
                /*Ahora obtengo el nombre largo*/
                var txt_servicio = objServicio.name + txt_vacios_2;

                var txt_servicio_2 = txt_servicio.substr(0,longMaxServices);
                /* El teléfono tiene este servicio */

                if ( wv_Serv_bscs.indexOf(wv_servicio)>-1 ){
                    var wv_indice = wv_Serv_bscs.indexOf(wv_servicio);
                    var wv_long   = wv_servicio.length;
                    var wv_servicio_act = wv_Serv_item.substring(wv_indice + wv_long ,wv_indice + wv_long + 1);
                    var wv_servicio_new = wv_Serv_item.substring(wv_indice + wv_long + 2 ,wv_indice + wv_long + 3);

                    if ( wv_servicio_act == "S" && wv_servicio_new == "S" ){
                        AddNewOption(form.cmbSelectedServices, objServicio.nameShort, txt_servicio_2 + txt_separacion + "x" + txt_separacion +"x");

                        if(objServicio.exclude == "MSJ") {

                            document.frmdatos.hdnServDefIdMsj.value=objServicio.id;

                        }

                        if( objServicio.exclude == "ALQ" )
                            parent.mainFrame.alquilerServicesSelected++;

                        // Telefonia fija - Post venta
                        try{
                            if( objServicio.exclude == "ALQ1" )
                                parent.mainFrame.alquilerServicesSelected1++;
                            if( objServicio.exclude == "ALQ2" )
                                parent.mainFrame.alquilerServicesSelected2++;
                            if( objServicio.exclude == "ALQ3" )
                                parent.mainFrame.alquilerServicesSelected3++;
                        }catch(e){}

                        if( objServicio.exclude == "INT" )
                            parent.mainFrame.rentServicesSelected++;
                        if( objServicio.exclude == "END" )
                            parent.mainFrame.rentServicesSelectedEnd++;

                    }
                    else if (wv_servicio_act == "S" && wv_servicio_new == "N"){
                        //AddNewOption(form.cmbSelectedServices, objServicio.nameShort, txt_servicio_2 + txt_separacion + " " + txt_separacion +"x");
                        AddNewOption(form.cmbSelectedServices, objServicio.nameShort, txt_servicio_2 + txt_separacion + "x" + txt_separacion +" ");

                        if( objServicio.exclude == "ALQ" )
                            parent.mainFrame.alquilerServicesSelected++;

                        // Telefonia fija - Post venta
                        try{
                            if( objServicio.exclude == "ALQ1" )
                                parent.mainFrame.alquilerServicesSelected1++;
                            if( objServicio.exclude == "ALQ2" )
                                parent.mainFrame.alquilerServicesSelected2++;
                            if( objServicio.exclude == "ALQ3" )
                                parent.mainFrame.alquilerServicesSelected3++;
                        }catch(e){}

                        if( objServicio.exclude == "INT" )
                            parent.mainFrame.rentServicesSelected++;
                        if( objServicio.exclude == "END" )
                            parent.mainFrame.rentServicesSelectedEnd++;
                    }
                    else if (wv_servicio_act == "N" && wv_servicio_new == "S"){

                        AddNewOption(form.cmbSelectedServices, objServicio.nameShort, txt_servicio_2 + txt_separacion + " " + txt_separacion +"x");

                        //alert("Entramos " + objServicio.nameShort)
                        if( objServicio.exclude == "ALQ" )
                            parent.mainFrame.alquilerServicesSelected++;

                        // Telefonia fija - Post venta
                        try{
                            if( objServicio.exclude == "ALQ1" )
                                parent.mainFrame.alquilerServicesSelected1++;
                            if( objServicio.exclude == "ALQ2" )
                                parent.mainFrame.alquilerServicesSelected2++;
                            if( objServicio.exclude == "ALQ3" )
                                parent.mainFrame.alquilerServicesSelected3++;
                        }catch(e){}

                        if( objServicio.exclude == "INT" )
                            parent.mainFrame.rentServicesSelected++;
                        if( objServicio.exclude == "END" )
                            parent.mainFrame.rentServicesSelectedEnd++;
                    }
                    objServicio.active_new=wv_servicio_act;
                    objServicio.modify_new=wv_servicio_new;
                }
                else{
                    /* El teléfono no tiene este servicio */
                    /*Nombre Corto*/
                    //v_doc.AddNewOption(form.cmbAvailableServices, objServicio.nameShort , objServicio.nameShortDisplay);
                    /*Nombre Largo*/
                    //alert("objServicio.group:" + objServicio.group);
                    if(npspecificationId == <%=Constante.SPEC_SSAA_PROMOTIONS%>){
                        if(objServicio.group == "3"){ //Grupo de Promociones
                            v_doc.AddNewOption(form.cmbAvailableServices, objServicio.nameShort , objServicio.name);
                        }
                    }else if(npspecificationId == <%=Constante.SPEC_SSAA_SUSCRIPCIONES%>){
                        if(objServicio.group == "2"){ //Grupo de Suscripciones
                            v_doc.AddNewOption(form.cmbAvailableServices, objServicio.nameShort , objServicio.name);
                        }
                    }else{
                        v_doc.AddNewOption(form.cmbAvailableServices, objServicio.nameShort , objServicio.name);
                    }
                }

                //alert("alquilerServicesSelected : " + parent.mainFrame.alquilerServicesSelected)
                //alert("rentServicesSelected : " + parent.mainFrame.rentServicesSelected)
            } // end_for
            /* Si el ComboBox de Serv. Seleccionados no tiene elementos entonces añadimos un
             elemento "vacio" para una mejor visualizacion del combo */
            if (form.cmbSelectedServices.length < 1) {
                txt_servicio_2 = "            ";
                AddNewOption(form.cmbSelectedServices, "", txt_servicio_2 + txt_separacion + " " + txt_separacion +" ");
            }
        }  /* end_if */

        if( npspecificationId == <%=strCategoryCambioModeloId%> ){
            fxCargaServicioCambioModelo(document.frmdatos.hdnNameCM.value, document.frmdatos.hdnNameShortCM.value);
        }

    } /* end_function */

    function fxCargaServicioCambioModelo(name, nameShort){
        form   = document.frmdatos;

        if(name == "" || nameShort == ""){
            return;
        }

        var txt_separacion="  ";
        var txt_vacios_2="";
        difServices = 0;
        if( name.length < longMaxServices )
            difServices = longMaxServices - name.length;

        for( k = 0; k < difServices; k++ )
            txt_vacios_2+=" ";

        var txt_servicio = name + txt_vacios_2;
        var txt_servicio_2 = txt_servicio.substr(0,longMaxServices);

        AddNewOption(form.cmbSelectedServices, nameShort, txt_servicio_2 + txt_separacion + "x" + txt_separacion +" ");
    }

    //Carga servicios por defecto en el lado derecho
    function fxCargaServiciosPorDefecto(){
        var form = document.frmdatos;
        var wn_size = vServicioPorDefecto.size();
        for(j=0; j<wn_size ; j++){
            objServicio = vServicioPorDefecto.elementAt(j);
            for (i=0;i<form.cmbAvailableServices.options.length;i++){
                if (objServicio.nameShort == form.cmbAvailableServices.options[i].value) {
                    form.cmbAvailableServices.options[i].selected = true;
                }
            }
        }
        fxAddService();
        var serviceAditional2 = GetSelectedServices();

    }

    //Carga servicios por defecto en el lado derecho. En caso existan ssaa contratados del mismo grupo,
    //estos se colocaran para ser desactivados.
    function fxCargaServiciosPorDefectoNuevo(){
        var form = document.frmdatos;
        var wn_size = vServicioPorDefecto.size();
        for(j=0; j<wn_size ; j++){
            objServicio = vServicioPorDefecto.elementAt(j);
            for (i=0;i<form.cmbAvailableServices.options.length;i++){
                if (objServicio.nameShort == form.cmbAvailableServices.options[i].value) {
                    form.cmbAvailableServices.options[i].selected = true;
                }
            }
        }
        fxAddServiceToDefault();
        var serviceAditional2 = GetSelectedServices();

    }


    function fxActionObjectText(nameText,objThis){
        var form = document.frmdatos;
        var objValueObject = objThis.value;
        var objRefObject   = objThis;
        var strSolution    = "";
        try{
            strSolution = form.cmb_ItemSolution[form.cmb_ItemSolution.selectedIndex].value;
        }catch(e){
            strSolution = "";
        }

        var strInstallAddressId;
        if (trim(objThis.value)!=""){
            /*Inicio contratos de referencia*/
            if (nameText=="txt_ItemNumeroExistenteTF"){
                strInstallAddressId = form.txt_ItemAddressInstall.value;
                if (strInstallAddressId==""){
                    alert("Debe seleccionar una dirección de instalación");
                    return false;
                }
                var url = "<%=request.getContextPath()%>/itemServlet?&strCustomerId=<%=strCodigoCliente%>&strSpecificationId=<%=hdnSpecification%>&strPhoneNumber="+trim(objThis.value)+"&hdnMethod=getValidatePhoneVoIp&nameText="+nameText+"&strInstallAddressId="+strInstallAddressId;
                parent.bottomFrame.location.replace(url);
            }
            if (nameText=="txt_ItemContratoInternet"){
                if (strSolution==""){
                    alert("Debe seleccionar la solución");
                    form.cmb_ItemSolution.focus();
                    return false;
                }
                strInstallAddressId = form.txt_ItemAddressInstall.value;
                if (strInstallAddressId==""){
                    alert("Debe seleccionar una dirección de instalación");
                    return false;
                }
                var url = "<%=request.getContextPath()%>/itemServlet?&strCustomerId=<%=strCodigoCliente%>&strSpecificationId=<%=hdnSpecification%>&strContractNumber="+trim(objThis.value)+"&hdnMethod=getValidateContract&nameText="+nameText+"&strSolution="+strSolution+"&strInstallAddressId="+strInstallAddressId;
                parent.bottomFrame.location.replace(url);
            }
            if (nameText=="txt_ItemContratoDatos"){
                if (strSolution==""){
                    alert("Debe seleccionar la solución");
                    form.cmb_ItemSolution.focus();
                    return false;
                }
                strInstallAddressId = form.txt_ItemAddressInstall.value;
                if (strInstallAddressId==""){
                    alert("Debe seleccionar una dirección de instalación");
                    return false;
                }
                var url = "<%=request.getContextPath()%>/itemServlet?&strCustomerId=<%=strCodigoCliente%>&strSpecificationId=<%=hdnSpecification%>&strContractNumber="+trim(objThis.value)+"&hdnMethod=getValidateContract&nameText="+nameText+"&strSolution="+strSolution+"&strInstallAddressId="+strInstallAddressId;
                parent.bottomFrame.location.replace(url);
            }
            /*Fin contratos de referencia*/
            /*Inicio Nodo Principal*/
            if (nameText=="txt_ItemNodoPrincipal"){
                var url = "<%=request.getContextPath()%>/itemServlet?&strCustomerId=<%=strCodigoCliente%>&strSpecificationId=<%=hdnSpecification%>&strContractNumber="+trim(objThis.value)+"&hdnMethod=getValidateContract&nameText="+nameText+"&strSolution="+strSolution;
                parent.bottomFrame.location.replace(url);
            }
            /*Fin Nodo Principal*/
            if (nameText=="txt_ItemContractNumber"){
                var url = "<%=request.getContextPath()%>/itemServlet?&strCustomerId=<%=strCodigoCliente%>&strSpecificationId=<%=hdnSpecification%>&strContractNumber="+trim(objThis.value)+"&hdnMethod=getValidateContract&nameText="+nameText+"&strSolution="+strSolution;
                parent.bottomFrame.location.replace(url);
            }

            if (nameText=="txt_ItemNewNumber"){
                if( '<%=hdnSpecification%>' == '2012' ){
                    var url = "<%=request.getContextPath()%>/serviceservlet?paramPhoneNumber="+objValueObject+"&paramCustomerId=<%=strCodigoCliente%>&paramSiteId=<%=strnpSite%>&paramSpecificationId=<%=hdnSpecification%>&myaction=doValidateNewPhoneToChange";
                    parent.bottomFrame.location.replace(url);
                }else if( '<%=hdnSpecification%>' == '2001' || '<%=hdnSpecification%>' == '2002' 
                       || '<%=hdnSpecification%>'== '<%=Constante.SPEC_PREPAGO_TDE%>' || '<%=hdnSpecification%>'== '<%=Constante.SPEC_REPOSICION_PREPAGO_TDE%>'){
                    objValueObject = trim(objValueObject);
                    var v_form = document.frmdatos;
                    var strAppId;
                    var i;
                    var j;
                    var itid;
                    var itdev;

                    var url = "<%=request.getContextPath()%>/editordersevlet?phoneNumber="+objValueObject+
                            "&strAppId="+strAppId+"&strOrderId=<%=strOrderId%>"+"&strval_i="+i+"&strval_j="+j+"&strItemId="+itid+
                            "&strItemDeviceId="+itdev+"&myaction=validatePhoneNumber";
                    parent.bottomFrame.location.replace(url);
                }
            }

            if (nameText=="txt_ItemIMEI" && (!eval("parent.mainFrame.frmdatos.txt_ItemIMEI.readOnly"))){
                objThis.value=trim(objThis.value); //CEM COR0429
                var url = "<%=request.getContextPath()%>/itemServlet?hdnMethod=getDetailImei&strParamInput="+objThis.value+"&strCustomerId=<%=strCodigoCliente%>&strSpecificationId=<%=hdnSpecification%>"; //COR0596
                parent.bottomFrame.location.replace(url);
            }

            if (nameText=="txt_ItemMntsRates"){
                if(!isNaN(objValueObject)){
                    if(objValueObject > parseInt(form.txt_ItemCantMntsBolsa.value)){
                        alert("Los minutos contratados deben ser menores a los minutos de la bolsa seleccionada");
                        objRefObject.value = '';
                        objRefObject.focus();
                    }
                }else{
                    alert("Los minutos contratados no son los correctos");
                    objRefObject.value = '';
                    objRefObject.focus();
                }

            }

            if (nameText=="txt_ItemPriceByMnts"){
                //var objValue = objThis.value;
                var objValue = "";

                objValue = parseFloat(form.txt_ItemPriceBolsa.value) / parseInt(form.txt_ItemCantMntsBolsa.value);

                if( !isNaN(parseFloat(objValue)) ){
                    if( objValue != objValueObject ){
                        alert("El precio por minuto ingresado del Producto Bolsa no es el correcto");
                        objRefObject.value = '';
                        objRefObject.focus();
                    }
                }else{
                    alert("La cantidad de minutos es cero, seleccione otro producto " );
                    objRefObject.value = '';
                    objRefObject.focus();
                }
            }

            // Inicio CEM COR0429 - CGC
            if( nameText=="txt_ItemSIM_Eagle" ){
                var modality="";
                var strSolutionId="";
                try{
                    modality = form.cmb_ItemModality.value;
                    strSolutionId = form.cmb_ItemSolution.value;
                }catch(e){}
                objThis.value=trim(objThis.value);
 //[TDECONV003-1] EFLORES 04/09/2017 Se añade flag de migracion
                var strFlagMigration = "0";
                <% if(tdeValEquipAlquiler.equals("1")){%>
                if(parent.opener.frmdatos.chkFlagMigration != null && parent.opener.frmdatos.chkFlagMigration.checked){
                    strFlagMigration = "1";
                }else{
                    strFlagMigration = "0";
                }
                <% } %>
                //[TDECONV003-1] Fin
                var url = "<%=request.getContextPath()%>/itemServlet?hdnMethod=getDetailImei&strFrom=SIMEagle&strCustomerId=<%=strCodigoCliente%>&strSpecificationId=<%=hdnSpecification%>&strParamInput="+objThis.value+"&strItemModality="+modality+"&strSolutionId="+strSolutionId+"&strFlagMigration="+strFlagMigration; //[TDECONV003-1]EFLORES
                parent.bottomFrame.location.replace(url);
            }

            //INICIO DERAZO TDECONV003-2
            if(nameText=="txt_ItemImeiFS"){
                objThis.value = trim(objThis.value);
                var modalitySelected = "";
                // [TDECONV003-6] se añade validacion
                var strSolutionId="";
                try{
                    modalitySelected = form.cmb_ItemModality.value;
                    // [TDECONV003-6] se añade validacion
                    strSolutionId = form.cmb_ItemSolution.value;
                }catch(e){}

                //Si se ha seleccionado la modalidad, realizamos validaciones
                if(modalitySelected != ""){
                    if(objThis.value != ""){
                        //Validamos el tipo de dato
                        if(!ContentOnlyNumber(objThis.value)) {
                            alert("El IMEI FS debe contener solo números");
                            objThis.value = "";
                            objThis.focus();
                        }
                        else{
                            //Validamos la cantidad de digitos
                            if(objThis.value.length != <%=tdeLengthImeiFS%>){
                                alert("El IMEI FS debe tener <%=tdeLengthImeiFS%> digitos");
                                objThis.value = "";
                                objThis.focus();
                            }
                        }
                    }

                    // [TDECONV003-6] INI se añade validacion
                    <% if(flagValImeiFS.equals("1")){%>
                        var strFlagMigration = "0";
                        <% if(tdeValEquipAlquiler.equals("1")){%>
							if(parent.opener.frmdatos.chkFlagMigration != null && parent.opener.frmdatos.chkFlagMigration.checked){
								strFlagMigration = "1";
							}else{
								strFlagMigration = "0";
							}
							objThis.value=trim(objThis.value);
							var url = "<%=request.getContextPath()%>/itemServlet?hdnMethod=getDetailImei&strFrom=IMEIFS&strCustomerId=<%=strCodigoCliente%>&strSpecificationId=<%=hdnSpecification%>&strParamInput="+objThis.value+"&strItemModality="+modalitySelected+"&strSolutionId="+strSolutionId+"&strFlagMigration="+strFlagMigration;
							parent.bottomFrame.location.replace(url);
						<% } %>
                    <% } %>
                    // [TDECONV003-6] FIN
                }
            }
            //FIN DERAZO

            if( nameText=="txt_ItemPriceException" ){
                var productLine="";
                try{
                    productLine = form.cmb_ItemProductLine.value;
                }catch(e){}
                if ((productLine=="12"|| productLine=="188") && form.txt_ItemPriceException.value!="" && form.cmb_ItemModality.value =="Venta" && ('<%=hdnSpecification%>' == '2002' || '<%=hdnSpecification%>' == '<%=Constante.SPEC_PREPAGO_TDE%>' || '<%=hdnSpecification%>' == '<%=Constante.SPEC_REPOSICION_PREPAGO_TDE%>') ){
                    //alert("Es un Kit Prepago: " + form.cmb_ItemModality.value + " productid:" +  form.cmb_ItemProducto.value);
                    //objThis.value=trim(objThis.value);
                    var url = "<%=request.getContextPath()%>/serviceservlet?myaction=doValidateKitPrice&strModality=20"+"&strProductId=" + form.cmb_ItemProducto.value+"&strExceptionPrice="+form.txt_ItemPriceException.value+"&strPrice="+form.txt_ItemPriceCtaInscrip.value+"&strSalesStuctOrigenId=<%=strSalesStuctOrigenId%>";
                    //alert(url);
                    parent.bottomFrame.location.replace(url);
                }
            }

            // Fin CEM COR0429 - CGC

            //INICIO Telefonia fija - Post Venta
            if (nameText=="txt_ItemFixedPhone"){
                objValueObject = trim(objValueObject);
                var url = "<%=request.getContextPath()%>/serviceservlet?paramPhoneNumber="+objValueObject+"&paramCustomer=<%=strCodigoCliente%>&paramSite=<%=strnpSite%>&paramSpecification=<%=hdnSpecification%>&myaction=loadDetailFixedPhone&strSessionId=<%=strSessionId%>&strOrderId=<%=strOrderId%>&strTypeCompany=<%=strTypeCompany%>";
                parent.bottomFrame.location.replace(url);
            }
            //FIN Telefonia Fija  - Post Venta

            if (nameText=="txt_ItemIMEI_Cliente"){
                objValueObject = trim(objValueObject);
                var url = "<%=request.getContextPath()%>/itemServlet?imei="+objValueObject+"&hdnMethod=doValidateIMEICustomer";
                parent.bottomFrame.location.replace(url);
            }

            //PRY-0762 JQUISPE
            if(nameText=="txt_ItemQuantity"){
            	fxAddValueTotalRentaAdelantada();
            }

        }else{
            //alert("Distinto de 'hola'")
        }


    }

    function fxInputTabEnter() {
        return ((window.event.keyCode == 9) || (window.event.keyCode == 13));
    }

    /*Developer: Lee Rosales
     Objetivo : Después de agregarse un registro deben de
     regresar los controles a su estado inicial
     */
    function formResetGeneral(){
        form   = document.frmdatos;
        document.getElementById("frmdatos").reset();
        var itemsTemplate = document.getElementById("itemsTemplate");

        //RHUARCANI 20101117 INICIO
        try{
            formCurrent = parent.mainFrame.frmdatos;
            //parent.mainFrame.DeleteSelectOptions(formCurrent.cmb_ItemAssignedNumber);
            parent.mainFrame.DeleteAllSelectOptions(formCurrent.cmb_ItemAssignedNumber);
            idValidateImeSim.innerHTML = "";
        }catch(e){}
        //RHUARCANI 20101117 FIN

        var itemsTemplate = document.getElementById("itemsTemplate");


        if (itemsTemplate != null){
            for (i=itemsTemplate.rows.length; i>1; i--){
                itemsTemplate.deleteRow(i-1);
            }

            <%
            ItemService objItemServiceTransaction = new ItemService();
            int idProm = 0; int idPlan = 0; int idSpecification = 0;
            int idKit  = 0;
            HashMap objHashMap = objItemServiceTransaction.OrderDAOgetAddendasList( idProm, idPlan, idSpecification, idKit);
            strMessage = "";
            strMessage = (String) objHashMap.get("strMessage");
            ArrayList list = (ArrayList)objHashMap.get("objArrayList");
            int flagAdendas =1;
            request.setAttribute("tblListAdendum", list);
            %>
            parent.mainFrame.fxOnLoadListAddendum();

        }
        try{
            //Reiniciamos los vectores
            // Vector de Servicios por Defecto
            parent.mainFrame.vServicio.removeElementAll();
            // Vector de Servicios por Defecto
            parent.mainFrame.vServicioPorDefecto.removeElementAll();

            parent.mainFrame.fxLoadServiceAditiional();
        }catch(e){}

        //Reiniciar los SSAA seleccionados y por defecto
        try{
            fxCargaServiciosItem("");
        }catch(e){}

        try{
            fxCargaServiciosPorDefecto();
        }catch(e){}

        try{
            //Se deben reiniciar los contadores globales
            parent.mainFrame.alquilerServicesSelected = 0;
            parent.mainFrame.rentServicesSelected     = 0;
            parent.mainFrame.rentServicesSelectedEnd  = 0;
            parent.mainFrame.alquilerServicesSelected1 = 0;
            parent.mainFrame.alquilerServicesSelected2 = 0;
            parent.mainFrame.alquilerServicesSelected3 = 0;

            //Se deben de inicializar en blanco los SSAA
            form.item_services.value = '';
        }catch(e){}

        try{
            parent.mainFrame.valorOldProductNew  = '';
            parent.mainFrame.valorOldModalityNew = '';
            parent.mainFrame.valorOldPlanNew     = '';
            parent.mainFrame.valorOldQuantity    = '';
        }catch(e){}

        <%if(MiUtil.parseInt(hdnSpecification) == Constante.SPEC_BOLSA_DESACTIVAR ){%>
        document.getElementById("cmb_ItemProductLine").disabled = false;
        document.getElementById("cmb_ItemProductoOrigen").disabled = false;
        document.getElementById("txt_ItemPriceCtaInscrip").disabled = false;
        <%}%>
    }

    function fShowEquipPendRecojo() {
        var form = document.frmdatos;
        var url = "<%=request.getContextPath()%>/htdocs/jsp/controls/POPUP/popUpEquipPendRecojo.jsp?customerId="+"<%=strCodigoCliente%>"+"&siteId="+"<%=strnpSite%>";
        WinAsist = window.open(url,"EquipPendRecojo","toolbar=no,location=0,directories=no,status=yes,menubar=0,scrollbars=yes,resizable=yes,screenX=300,top=165,left=600,screenY=200,width=500,height=400,modal=yes");

    }

    /**********************************************************************************************/
    /* Motivo:Función que obtiene el tipo de solución en caso exista un registro para el item y envia
     /* el valor obtenido a la funcion fxOnLoadListSolution del objeto Solución para cargar los valores
     /* en base al tipo de Solución.
     /* Realizado por:  Karen Salvador
     /* Fecha: 07/05/2009 */
    /**********************************************************************************************/
    function  fxLoadSolution(){

        var form = document.frmdatos;
        var flagSolucion = '';
        var flagSolutionType = '';

        //Cuando es llamado desde un Incidente
        //------------------------------------
        <%if(!objectType.equals("INC")){%>

        <%
         //Verificamos si la categoría debe ser validada para el caso de Prepago o Postpago en el item
         //-------------------------------------------------------------------------------------------
         objHashMap = objGeneralService.getValueTag1(hdnSpecification,"VALIDATE_SPEC_TYPE_SOLUTION");
         if( objHashMap.get("strMessage")!= null )
            throw new Exception((String)objHashMap.get("strMessage"));
         String strValidateSolution =( String)objHashMap.get("strTag1");

         if ( !strValidateSolution.equals("0") ){
         %>

        v_numRows = parent.opener.items_table.rows.length;

        //Hay mas de un item en la orden - Se toma la primera fila como guia
        //-------------------------------------------------------------------
        if (v_numRows > 2){
            flagSolucion = parent.opener.frmdatos.txtItemSolution[1].value;
            flagSolutionType = '';
        }else {
            //Hay mas de un item en la orden - Se toma la primera fila como guia
            //-------------------------------------------------------------------
            if (v_numRows > 2){
                flagSolucion = parent.opener.frmdatos.txtItemSolution[1].value;
                flagSolutionType = '';
            }else{

                if (v_numRows == 2){ // Cuando tiene un item.
                    flagSolucion = parent.opener.frmdatos.txtItemSolution.value;
                    flagSolutionType = '';
                }
                else{ // Cuando no tiene items.
                    flagSolucion = '';
                    flagSolutionType = '';
                }
            }

        }

        //Sólo si la solución es diferente vacío se encuentra el tipo de Solución para pasar este parametro, a la funcion que llenara la
        //data de la solución, en caso contrario se toma en cuenta todas los tipos de soluciones.
        if (flagSolucion!=''){
            <%
              SpecificationBean objSpecificationBean = new SpecificationBean();
              NewOrderService   objNewOrderService    = new NewOrderService();
              objHashMap =objNewOrderService.getSolutionType();
              if( objHashMap.get("strMessage")!= null )
                throw new Exception((String)objHashMap.get("strMessage"));

                 ArrayList objArrayList = (ArrayList)objHashMap.get("objArrayList");
                     if (objArrayList != null && objArrayList.size()>0){
                         for (int i=0; i<objArrayList.size(); i++){
                           objSpecificationBean = new SpecificationBean();
                           objSpecificationBean = (SpecificationBean)objArrayList.get(i);
              %>
            if (flagSolucion == '<%=objSpecificationBean.getNpsolutionname()%>'){
                flagSolutionType = '<%=objSpecificationBean.getNpSolutiontype()%>';
            }
            <%
              }
            }
            %>
        }

        <%}else{%>
        flagSolucion = '';
        flagSolutionType = '';
        <%}%>

        <%}%>

        //Invoca a la función del objeto Solución donde llenará los valores del combo en base al tipo de solución: Prepago, Postpago ó null
        parent.mainFrame.fxOnLoadListSolution(flagSolutionType);

    }

    //Limpia la cadena de caracter no definido #160
    function fxCleanString(cadena){
        var i = 0;
        var salida = cadena;
        while ( i < cadena.length){
            if (cadena.toString().charCodeAt(i) == 160){
                salida = '';
                return salida;
            }
            i++;
        }
        return salida;
    }
    //document.frmdatos.hdnOriginalProductIdCM.value = parent.opener.vIdModelos.elementAt(parent.opener.frmdatos.hdnItemSelected.value);
    try{

        varTemporal = parent.opener.vIdModelos.elementAt(parent.opener.frmdatos.hdnItemSelected.value);
        var values = varTemporal.toString().split('|');

        for(var i=0; i<values.length;i++){
            if(i==0){
                parent.mainFrame.frmdatos.hdnIdCM.value = values[i];
            }
            else if(i==1){
                parent.mainFrame.frmdatos.hdnNameCM.value = values[i];
            }
            else if(i==2){
                parent.mainFrame.frmdatos.hdnNameShortCM.value = values[i];
            }
            else if(i==3){
                parent.mainFrame.frmdatos.hdnOriginalProductIdCM.value = values[i];
            }
        }

    }catch(e){
    }

    function fxLoadSimImei(){
        vDoc = parent.mainFrame;
        <%
          if("".equals(strSimImei)){
            strMessageSimImei = "";
          }else{
            hshValidateSimImei = objGeneralService.ProductDAOgetValidateSimImei(strSimImei);
            strMessageSimImei  = " * "+(String)hshValidateSimImei.get("result")+" : "+strSimImei;
          }
        %>
        try{
            vDoc.idValidateImeSim.innerHTML = '<%=strMessageSimImei%>';
        }catch(e){}
    }
</script>
<%}catch(Exception ex){
    ex.printStackTrace();
%>
<script>alert('<%=MiUtil.getMessageClean(ex.getMessage())%>');</script>
<%}%>