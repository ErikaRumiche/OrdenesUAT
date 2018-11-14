<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@page import="oracle.portal.provider.v2.ProviderUser" %>
<%@page import="oracle.portal.provider.v2.render.PortletRendererUtil" %>
<%@page import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil" %>
<%@page import="pe.com.nextel.service.*" %>
<%@page import="pe.com.nextel.bean.*" %>
<%@page import="pe.com.nextel.util.*" %>
<%@page import="pe.com.nextel.exception.SessionException" %>
<%@page import="java.util.*,java.text.SimpleDateFormat"%>
<%@page import="pe.com.nextel.service.NewOrderService"%>


<%
    try{
        String strSessionId1 = "";

        try {
            PortletRenderRequest pReq = (PortletRenderRequest) request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
            ProviderUser objetoUsuario1 = pReq.getUser();
            strSessionId1=objetoUsuario1.getPortalSessionId();
            System.out.println("Sesión capturada  : " + objetoUsuario1.getName() + " - " + strSessionId1 );
        } catch(Exception e) {
            System.out.println("Portler Not Found : " + e.getClass() + " - " + e.getMessage() );
            out.println("Portlet JP_ORDER_SEARCHShowPage Not Found");
            return;
        }

//strSessionId1 = "332543643307506896176974040086894340994158298";
//strSessionId1 = "998102396";
        System.out.println("Sesión capturada después del resuest: " + strSessionId1);
        PortalSessionBean portalSessionBean = (PortalSessionBean) SessionService.getUserSession(strSessionId1);
        if (portalSessionBean == null) {
            System.out.println("No se encontró la sesión de Java ->" + strSessionId1);
            throw new SessionException("La sesión finalizó");
        }
        String strBuildingId =null;
        String strLogin=null;
        int iLevel=0;
        String strCode=null;
        String strUser=null;
        int iUserId=0;//psbSesion.getUserid();
        int iAppId=0;//psbSesion.getAppId();
        int iNumInboxBack=0;

        strBuildingId=portalSessionBean.getBuildingid()+"";
        strLogin=portalSessionBean.getLogin();
        iLevel=portalSessionBean.getLevel();
        strCode=portalSessionBean.getCode();
        strUser=portalSessionBean.getNom_user();
        iUserId= portalSessionBean.getUserid();
        iAppId= portalSessionBean.getAppId();
        String strTienda = "";
        String strRegionTramite = "";

        //Parametros
        String strOrderId0=(request.getParameter("an_nporderid")==null?"0":request.getParameter("an_nporderid"));
        long lOrderId0=Long.parseLong(strOrderId0);
   
   /*Defino Parametros para utilizar en el hidden*/
        String strAppId = MiUtil.getString(iAppId);
   /*Fin*/

        //Obteniendo los datos de la Orden
        HashMap hshOrder=null;
        HashMap hshOrderAux = null;
        String strMessage=null;
        OrderBean objOrderBean=null;
        ArrayList arrListado=null;
        ArrayList arrReject=null;
        HashMap hshScreenField=null;
        HashMap hshData=null;
        RejectBean rjbRechazos = null;
        String strMessage0=null;
        HashMap hshMapInbox=null;
        String iFirstInbox=null;
        EditOrderService objOrderService=new EditOrderService();
        GeneralService objGeneralService= new GeneralService();
        PenaltyService objPenaltyService = new PenaltyService(); //EFLORES REQ-0428
        String autorizacionDevolucion="";

        String strTotalAmount = "";
        String strDueAmount = "";

        NewOrderService objOrdenAux = new NewOrderService();

        hshOrderAux = objOrdenAux.getNoteCount(lOrderId0);

        Integer iNoteCount = (Integer)hshOrderAux.get("iTotal");

        String sDescripcionOrdenRapida="";
        String sCadAux1="",sCadAux2="",sCadAux3="";
        hshOrder=objOrderService.getOrder(lOrderId0);
        autorizacionDevolucion=objOrderService.getAutorizacionDevolucion(lOrderId0,iUserId,iAppId);

        int iCourier = 0;

        strMessage=(String)hshOrder.get("strMessage");

        if (strMessage!=null)
            throw new Exception(strMessage);

        objOrderBean=(OrderBean)hshOrder.get("objResume");
        HashMap hshScreenOptions=new HashMap();

        //Incidencia #6182
        HashMap hshNombreTienda=new HashMap();
        hshNombreTienda = objGeneralService.getBuildingName((int)objOrderBean.getNpBuildingId());
        strTienda = (String)hshNombreTienda.get("strBuldingName");
        strRegionTramite = objGeneralService.getRegionName(objOrderBean.getNpRegionId());

        System.out.println("objOrderBean.getNpBuildingId() :" + objOrderBean.getNpBuildingId());
        System.out.println("objOrderBean.getNpCreatedBy() :" + objOrderBean.getNpCreatedBy());
        System.out.println("strTienda :" + strTienda);
        System.out.println("strRegionTramite :" + strRegionTramite);
        //-------------------------------------------------------------

        //RCDLRP - 21/08/2008 - INCIDENCIA #5150 - INICIO
        String strParentUrl = "";
        String strParentName = "";
        String strGeneratorid       = MiUtil.getString(objOrderBean.getNpGeneratorId());
        String strGeneratortype     = MiUtil.getString(objOrderBean.getNpGeneratorType());

        if ( !strGeneratorid.equals("") && !strGeneratortype.equals("") ){
            objGeneralService = new GeneralService();
            HashMap hshResult = new HashMap();
            hshResult = objGeneralService.getObjectTypeUrl(strGeneratortype);
            strMessage = (String)hshResult.get("strMessage");
            if ( strMessage != null  )
                throw new Exception(strMessage);
            strParentUrl = (String)hshResult.get("strUrl");
            strParentName = (String)hshResult.get("strName");
        }
        //RCDLRP - 21/08/2008 - INCIDENCIA #5150 - FIN

        //Rechazos
        hshOrder= objOrderService.getRejectList(lOrderId0);
        strMessage=(String)hshOrder.get("strMessage");
        if (strMessage!=null)
            throw new Exception(strMessage);

        arrReject=(ArrayList)hshOrder.get("arrRechazos");
   
   /*Inicio Data*/
        HashMap hshShowDataFields   = objGeneralService.getSalesDataShow( objOrderBean.getNpSpecificationId(),strGeneratortype, MiUtil.parseLong(strGeneratorid), objOrderBean.getCsbCustomer().getSwCustomerId(), objOrderBean.getNpSiteId());
        strMessage=(String)hshShowDataFields.get("strMessage");
        if (strMessage!=null)
            throw new Exception(strMessage);
        String strShowDataFields = "0";
        strShowDataFields = (String)hshShowDataFields.get("strShowDataFields");
        System.out.println("strShowDataFields===="+strShowDataFields);

        //Obtiene el vendedor de data
        HashMap hshOrderSeller = objGeneralService.getOrderSellerByType(objOrderBean.getNpOrderId(), 2    );
        strMessage=(String)hshOrderSeller.get("strMessage");
        if (strMessage!=null)
            throw new Exception(strMessage);
        String strSellerName = (String)hshOrderSeller.get("strSellerName");
        String strSellerId   = (String)hshOrderSeller.get("strSellerId");
   /*Fin Data*/

        Date fechPro=objOrderBean.getNpScheduleDate();
        String strFechPro=MiUtil.getDate(fechPro,"dd/MM/yyyy");
        Date miFech=MiUtil.toFecha(strFechPro,"dd/MM/yyyy");
        Date today=MiUtil.getDateBD("dd/MM/yyyy");

        //[Despacho en Tienda] PCASTILLO
        iCourier = objOrderBean.getNpFlagCourier();

        boolean validateAction = false;
        if (objOrderBean.getNpSpecificationId()== Constante.SPEC_ACTIVAR_DESACTIVAR_SERVICIOS ||
                objOrderBean.getNpSpecificationId()== Constante.SPEC_SSAA_PROMOTIONS ||
                objOrderBean.getNpSpecificationId()== Constante.SPEC_SSAA_SUSCRIPCIONES) {
            if (Constante.INBOX_PROCESOS_AUTOMATICOS.equals(objOrderBean.getNpStatus()) && today.compareTo(miFech)==0) {
                if(!Constante.NAME_ORIGEN_USSD.equals(objOrderBean.getNpOrigen())){
                    validateAction = true;
                }
            }
        }

		// INICIO PRY-1239 21/09/2018 PCACERES
		
		String sWebPayment = "";
		sWebPayment = objOrderBean.getVchWebPayment();
		
		// FIN PRY-1239 21/09/2018 PCACERES
		
        //Inicio validacion, mostrar orden de penalidad solo para ordenes de cambio de modelo REQ-0428 EFLORES
        System.out.println("REQ-0428 ------------INICIO VALIDACION MOSTRAR ORDEN DE PENALIDAD----------------");
        HashMap hshSpecifications = objPenaltyService.getConfigurationList("SPECIFICATION_INBOX_PENALTY");
        long nppenaltyorderid = 0;
        List<ListBoxBean> lbb = (List<ListBoxBean>)hshSpecifications.get("listBoxList");
        System.out.println("REQ-0428 - TOTAL DE ESPECIFICACIONES : ======> "+lbb.size());
        boolean showLinkPenaltyOrder = false;
        String orderPenaltyURL = "";
        for(ListBoxBean l :lbb){
            System.out.println("REQ-0428 - COMPARANDO  :"+objOrderBean.getNpSpecificationId()+" ======> "+l.getIdListBox());
            if(objOrderBean.getNpSpecificationId() == l.getIdListBox()){ //Verfica que se encuentre en el tipo de orden cambio de modelo
                HashMap m = objPenaltyService.getOrderPenaltyByParentOrderId(objOrderBean.getNpOrderId());
                String msg = (String)m.get(Constante.MESSAGE_OUTPUT);
                if(msg == null){
                    showLinkPenaltyOrder = true;
                    nppenaltyorderid = (Long)m.get("nporderid");
                    HashMap url = objPenaltyService.getPageOrderById(iUserId,iAppId,nppenaltyorderid);
                    msg = (String)url.get(Constante.MESSAGE_OUTPUT);
                    if(msg == null){
                        orderPenaltyURL = (String)url.get("av_url");
                        System.out.println("REQ-0428 - URL_DETALLE_ORDEN_PENALIDAD ===> "+orderPenaltyURL);
                    }else{
                        System.out.println("REQ-0428 - ERROR ===> "+msg);
                    }
                    System.out.println("REQ-0428 - NPORDERPENALTYID ===> "+nppenaltyorderid);
                }else{
                    System.out.println("REQ-0428 - ERROR ===> "+msg);
                }
                break;
            }
        }
        System.out.println("------------REQ-0428 FIN VALIDACION MOSTRAR ORDEN DE PENALIDAD----------------");

        //pzacarias
        String strCustomerId = String.valueOf(objOrderBean.getCsbCustomer().getSwCustomerId());
        System.out.println("PZF_DETAIL: strCustomerId = " + strCustomerId);

        IsolatedVerificationService objIsolatedVerificationService = new IsolatedVerificationService();
        List<IsolatedVerifConfigBean> specLst = objIsolatedVerificationService.getViaConfig(strCustomerId);

        String strRutaContext=request.getContextPath();
        String actionURL_DigitalDocumentServlet = strRutaContext+"/digitalDocumentServlet";
        String actionURL_PopulateCenterServlet = strRutaContext+"/populateCenterServlet";

     	//PRY-0890 JCURI
     	System.out.println("JP_ORDER_DETAIL_ORDER_ShowPage.jsp [PRY-0890 JCURI]");
     	String npProrrateo = objOrderBean.getNpProrrateo();
        String npOrderChildId = objOrderBean.getNpOrderChildId();
        String npOrderParentId =objOrderBean.getNpOrderParentId();
        String hdnOrderProrrateoValue = "";
        
        request.setAttribute("strProrrateo", objOrderBean.getNpProrrateo());
 		request.setAttribute("strPaymentTotalProrrateo", objOrderBean.getNpPaymentTotalProrrateo());
 		System.out.println("JP_ORDER_DETAIL_ORDER_ShowPage.jsp [getNpOrderId] : "  + objOrderBean.getNpOrderId());     		
 		System.out.println("JP_ORDER_DETAIL_ORDER_ShowPage.jsp [nppaymenttotalprorrateo] : "  + objOrderBean.getNpPaymentTotalProrrateo());
        System.out.println("JP_ORDER_DETAIL_ORDER_ShowPage.jsp [strProrrateo] : "  + npProrrateo);
        System.out.println("JP_ORDER_DETAIL_ORDER_ShowPage.jsp [nporderchildid] : "  + npOrderChildId);
        System.out.println("JP_ORDER_DETAIL_ORDER_ShowPage.jsp [getNpOrderparentId] : "  + npOrderParentId);
        
     		
     	if(npOrderChildId!=null && !"".equals(npOrderChildId)) {
 		hdnOrderProrrateoValue = "SPARENT";
     	} else if(npOrderParentId!=null && !"".equals(npOrderParentId)) {
 		hdnOrderProrrateoValue = "SCHILD";
 	}     			
 	System.out.println("JP_ORDER_DETAIL_ORDER_ShowPage.jsp [hdnOrderProrrateoValue] : "  + hdnOrderProrrateoValue);
        
        
//TDECONV003- Flag funcionalidad - 1 encendido | 0 apagado
        String strTDESwitch = objGeneralService.getValue(Constante.TDE_SWITCH, Constante.TDE_SWITCH_NPVALUEDESC);
        System.out.println("KOR_TDE_SWITCH:  " + strTDESwitch);
        
        //JCURI PRY-1093
        boolean isDeliverySpecification = false;
        HashMap objHashMapDeliverySpecification = new HashMap();        
        objHashMapDeliverySpecification = objGeneralService.GeneralDAOgetComboList("ORDEN_DELIVERY_REGION_SPECIFICATION");
        if( objHashMapDeliverySpecification.get("strMessage") != null ) { 
        		System.out.println("JP_ORDER_DETAIL_ORDER.jsp [ORDEN_DELIVERY_REGION_SPECIFICATION]" + objHashMapDeliverySpecification.get("strMessage"));
        } else {
        		ArrayList arrListDeliverySpecification = (ArrayList) objHashMapDeliverySpecification.get("objArrayList"); 
          	System.out.println("JP_ORDER_DETAIL_ORDER.jsp [arrListDeliverySpecification] " + arrListDeliverySpecification);          
          	if (arrListDeliverySpecification != null && arrListDeliverySpecification.size() > 0) {
          		for ( int i=0; i<arrListDeliverySpecification.size(); i++ ) {
          			HashMap mapDeliverySpecification = (HashMap) arrListDeliverySpecification.get(i); 
            			int npvalueSpecification = MiUtil.parseInt((String)mapDeliverySpecification.get("wn_npvalue"));
            			if(objOrderBean.getNpSpecificationId() == npvalueSpecification) {
            				isDeliverySpecification = true;
           					break;
            			}
          		}
          	}    
        }
        System.out.println("JP_ORDER_DETAIL_ORDER.jsp [isDeliverySpecification] " + isDeliverySpecification);
%>

<script>
    function fxValidateSpecLst(){
        form = document.frmdatos;
        var inSpecLst = false;
        <%
              for (int i = 0; i < specLst.size(); i++) {
                  %>
        if (form.hdnSubCategoria.value == '<%=specLst.get(i).getSpec()%>'){
            inSpecLst = true;
        }
        <%}
        %>

        $("#dataIsolatedVerification").html('<tr><td class="CellContent" colspan="6">No se encontraron registros</td></tr>');

        if(inSpecLst){
            fxValidateViaType();
        } else {
            $("#dvVerificacionIdentidadAislada").css('display', 'none');
        }
    }

    function fxValidateSpecLstDigit(){
        form = document.frmdatos;
        var inSpecLst = true;


        $("#dataIsolatedVerification").html('<tr><td class="CellContent" colspan="6">No se encontraron registros</td></tr>');

        if(inSpecLst){
            fxValidateViaType();
        } else {
            $("#dvVerificacionIdentidadAislada").css('display', 'none');
        }
    }

    function fxValidateViaType(){
        form = document.frmdatos;

        $("#dvVerificacionIdentidadAislada").css('display', 'block');

        // Listar las verificaciones aisladas
        $.ajax({
            url: "<%=request.getContextPath()%>/generalServlet?metodo=requestGetViaCustomer&customerId=" + form.txtCompanyId.value + "&orderId=" + "<%=strOrderId0%>" + "&hdncmbSubCategoria=" + form.hdnSubCategoria.value + "&selected=1",
            type: "GET",
            dataType:'json',
            success:function(data) {
                $("#dataIsolatedVerification").html('');
                if(data != null){
                    data = data.listIsolatedVerification;
                    if($.isArray(data)){
                        $.each(data, function(index, value){
                            $("#dataIsolatedVerification").append("<tr><td class=\"CellContent\"><input type=\"checkbox\" name=\"chkVerifId\" value =\"1\" checked disabled=\"true\"></td>" +
                                    "<td class=\"CellContent\">" + value.nptipodocument + "</td><td class=\"CellContent\">" + value.npnrodocument + "</td><td class=\"CellContent\">" + value.customer_name + "</td><td class=\"CellContent\">" + value.date_of_use + "</td><td class=\"CellContent\">" + value.date_end_validity + "</td></tr>");
                        });
                    }
                }
            },
            error:function(xhr, status, error) {
                alert(error);
            }
        });
    }

    //INICIO VIA EN DIGIT
    // JVERGARA - 20/04/2017 CONSULTA EL FLAG DE DIGITALIZACION EN VIA
    function fxValidateSpecLstDigitalization(){

            fxValidateSpecLstDigit();

    }


    //INICIO VIDD
    //INICIO VIDD
    function fxShowSectionsVIDD(){
        fxShowSectionPopulateCenter();
        fxShowSectionDigitDocument();
        fxShowSectionAuthContact();
        fxValidateSpecLstDigitalization();
    }
    function fxShowPopulateCenterDet(){
        var orderId="<%=strOrderId0%>";
        var url="/portal/page/portal/orders/POPULATE_CENTER_LIST";
        url = url + "?orderId="+orderId+"&display=DETAIL";
        var vurl = "/portal/pls/portal/websales.NPSL_GENERAL_PL_PKG.WINDOW_FRAME?av_title="+escape("Centro Poblado de Uso Frecuente")+"&av_url="+escape(url);
        vWinNewCondition = window.open(vurl,"vWinNewCondition","toolbar=no,location=0,directories=no,status=yes,menubar=0,scrollbars=yes,resizable=no,screenX=100,top=80,left=100,screenY=80,width=1060,height=800,modal=yes");
    }

    function fxShowSectionPopulateCenter(){
        var tdsignatureType = $('#signatureType').empty();
        $.ajax({
            url:"<%= actionURL_PopulateCenterServlet %>",
            type:"GET",
            async:false,
            cache:false,
            data:{
                "myaction":"getPopulateCenter",
                "orderId" : "<%=strOrderId0%>"
            },
            dataType:'json',
            success:function(data){
                var message=data.strMessage||'';
                if(message==''){
                    var cpufresponse=data.populateCenterBean.response;
                    if (cpufresponse==0){
                        $("#cpufresponse0").attr('checked', 'checked');
                    }else if(cpufresponse==1){
                        $("#cpufresponse1").attr('checked', 'checked');
                    }
                    $('#dvPopulateCenter').show();
                }else{
                    $('#dvPopulateCenter').hide();
                }
            },
            error: function(xhr, status, error) {
                alert(xhr.responseText);
            }
        });
    }

    function fxShowSectionAuthContact(){
        var td1,td2,td3;
        td1='<td class="CellLabel"><div id="dvAssignee" style="display:none">&nbsp;Apoderado&nbsp;</div></td>';
        td2='<td class="CellContent" align="left"> <div id="dvChkAssignee" style="display:none">&nbsp;<input type="checkbox" name="chkAssignee" id="chkAssignee" disabled="disabled"></div></td>';
        td3='<td class="CellContent" colspan="7"><div id="dvChkAssigneeNextCell" style="display:none">&nbsp;</div></td>';

        var flag_st = <%=MiUtil.contentInArray(objOrderBean.getNpSpecificationId(),Constante.SPEC_REPAIRS)%>;

        if(flag_st) {
            $('#rowDinamicoCST').html(td1+td2+td3);
        }else{
            $('#rowDinamicoSST').html(td1+td2+td3);
        }

        $.ajax({
            url:"<%= actionURL_DigitalDocumentServlet %>",
            type:"GET",
            data:{
                "myaction":"getDocAssignee",
                "orderId" : "<%=strOrderId0%>"
            },
            dataType:'json',
            success:function(data){
                var message=data.strMessage||'';

                if(message==''){
                    var docAssignee = data.docAssigneeBean;
                    $('#docTypeAssignee').text(docAssignee.typeDoc);
                    $('#docNumAssignee').text(docAssignee.numDoc);
                    $('#firstNameAssignee').text(docAssignee.firstName);
                    $('#lastNameAssignee').text(docAssignee.lastName);
                    $('#familyNameAssignee').text(docAssignee.familyName);

                    $('#dvAssigneeSection').show();

                    $('#dvAssignee').show();
                    $('#chkAssignee').attr("checked",true);
                    $('#dvChkAssignee').show();
                    //$('#dvAssigneeNextCell').show();
                    $('#dvChkAssigneeNextCell').show();
                }
                else{
                    if(form.hdnDocGenId.value== ""){
                        $('#dvAssigneeSection').hide();
                        $('#dvAssignee').hide();
                        $('#dvChkAssignee').hide();
                        //$('#dvAssigneeNextCell').hide();
                        $('#dvChkAssigneeNextCell').hide();
                    }else{
                        $('#dvAssignee').show();
                        $('#chkAssignee').attr("checked",false);
                        $('#dvChkAssignee').show();
                        //$('#dvAssigneeNextCell').show();
                        $('#dvChkAssigneeNextCell').show();
                    }

                }
            },
            error: function(xhr, status, error) {
                alert(xhr.responseText);
            }
        });
    }

    function fxShowSectionDigitDocument(){
        $.ajax({
            url:"<%= actionURL_DigitalDocumentServlet %>",
            type:"GET",
            data:{
                "myaction":"getDocumentGeneration",
                "orderId" : "<%=strOrderId0%>"
            },
            dataType:'json',
            success:function(data){
                var message=data.strMessage||'';
                if(message==''){
                    var rowDinamico=$('#rowDinamico');
                    var documentGeneration=data.documentGenerationBean;
                    form.hdnDocGenId.value=documentGeneration.id;

                    $('#signatureType').text(documentGeneration.signatureTypeLabel)
                    if (documentGeneration.signatureType == "<%=Constante.SIGNATURE_TYPE_DIGITAL%>") {
                        cargaFirmaDigital(rowDinamico,documentGeneration.email);
                    } else if (documentGeneration.signatureType == "<%=Constante.SIGNATURE_TYPE_MANUAL%>") {
                        cargaFirmaManual(rowDinamico,documentGeneration.signatureReasonLabel);
                    }
                    $('#dvDigitalizacionDocumentos').show();
                }
                else{
                    $('#dvDigitalizacionDocumentos').hide();
                }
            },
            error: function(xhr, status, error) {
                alert(xhr.responseText);
            }
        });
    }

    function cargaFirmaManual(rowDinamico,label){
        var td1='<td class="CellLabel" width="15%">Motivo</td>';
        var td2='<td class="CellContent" width="25%">'+label+'</td>';
        var td3='<td class="CellContent"/>';
        rowDinamico.html(td1+td2+td3);
    }
    function cargaFirmaDigital(rowDinamico,label){
        var td1='<td class="CellLabel" width="15%">Correo Electr&oacute;nico</td>';
        var td2='<td class="CellContent" width="25%">'+label+'</td>';
        var td3='<td class="CellContent"/>';
        rowDinamico.html(td1+td2+td3);

    }
//FIN VIDD
</script>

<script DEFER>
    var gpRejectArr  = new Array();
    var g_orderid    = "<%=lOrderId0%>";
    var gIndexReject =0;
    var gIndexRejectIni =0;
</script>

<%
    for(int i=0; i <arrReject.size();i++){
        rjbRechazos =  (RejectBean)arrReject.get(i);

%>
<script DEFER>
    gpRejectArr[gIndexReject]  =new fxCreateReject( "<%=rjbRechazos.getNpRejectId()%>",
            "<%=rjbRechazos.getNpOrderId()%>",
            "<%=MiUtil.getString(rjbRechazos.getNpReason())%>",
            "<%=MiUtil.getMessageClean(MiUtil.getString(rjbRechazos.getNpComment()))%>",
            "<%=MiUtil.getString(rjbRechazos.getNpStatus())%>",
            "<%=MiUtil.getString(rjbRechazos.getNpCreatedBy())%>",
            "<%=MiUtil.toFecha(rjbRechazos.getNpCreatedDate())%>",
            "<%=MiUtil.getString(rjbRechazos.getNpModifiedBy())%>",
            "<%=MiUtil.toFecha(rjbRechazos.getNpModifiedDate())%>",
            "<%=MiUtil.getString(rjbRechazos.getNpInbox())%>");
    gIndexReject++;
</script>
<%
    }

    //JCASAS Deposito en Garantia
    hshOrder = objOrderService.getGuarantee(lOrderId0, Constante.PAYMENT_SOURCE, 23);
    strMessage0=(String)hshOrder.get("strMessage");
    if (strMessage0!=null)
        throw new Exception(strMessage0);
    strTotalAmount = (String)hshOrder.get("lTotalAmount");
    strDueAmount = (String)hshOrder.get("lDueAmount");

    hshOrder=objOrderService.getOrderScreenField(lOrderId0,Constante.PAGE_ORDER_DETAIL);
    strMessage=(String)hshOrder.get("strMessage");
    if (strMessage!=null)
        throw new Exception(strMessage);

    hshScreenField= (HashMap)hshOrder.get("hshData");
    //String strRutaContext=request.getContextPath();


    //Validacion SreenOptions
    //-----------------------
    System.out.println("OrderId:"+lOrderId0+"/iUserId:"+iUserId+"/iAppId:"+iAppId);
    hshScreenOptions=objGeneralService.getPermissionDetail(lOrderId0, iUserId, iAppId);
    strMessage=(String)hshScreenOptions.get("strMessage");
    if (strMessage!=null)
        throw new Exception(strMessage);
    int iFlagCarrier=MiUtil.parseInt((String)hshScreenOptions.get("iRetorno"));
    System.out.println("iFlagCarrier:"+iFlagCarrier);

    //Verificamos si nos encontramos en el Primer Inbox
    //-------------------------------------------------
    hshMapInbox = objOrderService.getIsFirstInbox(lOrderId0);
    strMessage=(String)hshMapInbox.get("strMessage");
    if (strMessage!=null)
        throw new Exception(strMessage);

    iFirstInbox =(String)hshMapInbox.get("sFirstInbox");
    System.out.println("sFirstInbox:"+iFirstInbox);


    System.out.println("---------------------- INICIO JP_ORDER_DETAIL_ORDER------------------");
    System.out.println("strOrderId-->"+strOrderId0);
    System.out.println("Constante.PAGE_ORDER_DETAIL-->"+Constante.PAGE_ORDER_DETAIL);
    System.out.println("CustomerId-->"+objOrderBean.getCsbCustomer().getSwCustomerId());
    System.out.println("strUser-->"+strUser);
    System.out.println("RegionID-->"+objOrderBean.getNpRegionId());
    System.out.println("---------------------- FIN JP_ORDER_DETAIL_ORDER------------------");


%>
<script defer>
    window.parent.document.title='<%=strOrderId0%>';
    //EFLORES REQ-0428 Mostrar Popup
    function fxShowPopup(parentUrl,title){ //Atributos separados por & ejem : a=3&b=3...
        if(parentUrl != null && parentUrl != ''){
            var winUrl = "/portal/pls/portal/WEBSALES.NPSL_NEW_GENERAL_PL_PKG.PL_FRAME?av_url="+escape(parentUrl);
            window.open(winUrl,'',"status=yes, location=no, width=1040, height=800, left=100, top=100, scrollbars=yes, screenX=50, screenY=100");
        }
    }
    
    //JQUISPE PRY-0762
    function getShowOrdenRentaAdelantada(strOrderRAId){
    	var strUrl="/portal/page/portal/orders/ORDER_DETAIL?an_nporderid="+strOrderRAId;
        var  url = "/portal/pls/portal/WEBSALES.NPSL_GENERAL_PL_PKG.WINDOW_FRAME?av_title="+escape("PORTAL NEXTEL")+"&av_url="+escape(strUrl);
        window.open(url,"WinCustomer","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,screenX=60,top=40,left=60,screenY=40,width=850,height=600");
        return;
    }
    
    //JCURI
    function getShowOrdenProrrateo(strOrderId){
    	var strUrl="/portal/page/portal/orders/ORDER_DETAIL?an_nporderid="+strOrderId;
        var  url = "/portal/pls/portal/WEBSALES.NPSL_GENERAL_PL_PKG.WINDOW_FRAME?av_title="+escape("PORTAL NEXTEL")+"&av_url="+escape(strUrl);
        window.open(url,"WinCustomer","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,screenX=60,top=40,left=60,screenY=40,width=850,height=600");
        return;
    }    
</script>
<input type="hidden" name="hdnOrderId" value="<%=strOrderId0%>">
<input type="hidden" name="hdnNpBpelconversationid" value="<%=MiUtil.getString(objOrderBean.getNpBpelconversationid())%>">
<input type="hidden" name="hdnNpBpelinstanceid" value="<%=objOrderBean.getNpBpelinstanceid()%>">
<input type="hidden" name="txtNumeroGuia" value="<%=objOrderBean.getNpguidegenerated()%>">
<input type="hidden" name="hdnChannelClient" value="">
<input type="hidden" name="hdnFlgSAC" value="">
<input type="hidden" name="hdnOrderProrrateo" value="<%=hdnOrderProrrateoValue%>"> <!-- PRY-0890 JCURI -->
<input type="hidden" name="hdnFlgSDD" value="">
<input type="hidden" name="hdnDocAssigneeId" value="">
<input type="hidden" name="hdnDocGenId" value="">
<table border="0" cellspacing="0" cellpadding="0" width="15%">
    <tr class="PortletHeaderColor">
        <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"></td>
        <td class="SubSectionTitle" align="left" valign="top">Orden&nbsp;<%=strOrderId0%></td>
        <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"></td>
    </tr>
</table>

<!-- Inicio Variable hidden -->
<input type="hidden" id="strAppId" name="strAppId" value="<%=strAppId%>">
<!-- Fin -->

<table  border="0" width="100%" cellpadding="2" cellspacing="1" class="RegionBorder">
    <tr>
        <td>
            <table  border="0" width="100%" cellpadding="2" cellspacing="1">
                <tr>
                    <td class="CellLabel" align="center">&nbsp;<b>NRO. SOLICITUD</b></td>
                    <td class="CellLabel" align="center">División</td>
                    <td class="CellLabel" align="center" colspan="2">Categoría</td>
                    <td class="CellLabel" align="center" colspan="2">Sub Categoría</td>
                    <td class="CellLabel" colspan=1 align="center">Vendedor</td>
                    <!-- Inicio Data -->
                    <%
                        if ( MiUtil.getString(strShowDataFields).equalsIgnoreCase("1")){
                    %><td class="CellLabel" colspan=1 align="center">Vendedor Data</td><%
                }else{
                %><td class="CellLabel" colspan=1 align="center">&nbsp;</td><%
                    }
                %>
                    <!-- Fin Data -->
                </tr>
                <tr>
                    <td class="CellContent" align="center">&nbsp;
                        <input type="text" name="txtNumSolicitud" size="15" style="border :0; background-color:transparent;" readonly maxlength="8" value="<%=MiUtil.getString(objOrderBean.getNpOrderCode())%>">
                    </td>
                    <td class="CellContent" align="center">&nbsp;<%=MiUtil.getString(objOrderBean.getNpDivisionName())%>
                    </td>
                    <td class="CellContent" align="center" colspan="2">&nbsp;
                        <input type="text" name="txtCategoria" size="35" align="middle" style="border :0;  background-color:transparent;" readonly value="<%=MiUtil.getString(objOrderBean.getNpType())%>">
                    </td>
                    <td class="CellContent" align="center" colspan="2">&nbsp;
                        <%=MiUtil.getString(objOrderBean.getNpSpecification())%>
                        <input type="hidden" name="hdnSubCategoria" value="<%=objOrderBean.getNpSpecificationId()%>">

                    </td>
                    <td class="CellContent" colspan=1 align="center"><%=MiUtil.getString(objOrderBean.getNpSalesmanName())%></td>
                    <%
                        if ( MiUtil.getString(strShowDataFields).equalsIgnoreCase("1")){
                    %><td class="CellContent" colspan=1 align="center"><%=MiUtil.getString(strSellerName)%></td><%
                }else{
                %><td class="CellContent" colspan=1 align="center">&nbsp;</td><%
                    }
                %>
                </tr>

                <!-- Segundo grupo -->
                <tr>
                    <td class="CellLabel"   align="left" >Estado</td>
                    <td class="CellContent" align="left" >&nbsp;<%=MiUtil.getString(objOrderBean.getNpStatus())%>  <!-- Nombre del estado -->
                        <input type="hidden" name="txtEstadoOrden" value="<%=MiUtil.getString(objOrderBean.getNpStatus())%>"/>
                    </td>
                    <td class="CellLabel"   align="left" >Tienda</td>
                    <td class="CellContent" align="left" >&nbsp;<%=strTienda%></td>
                    <td class="CellLabel"   align="left" >Regi&oacute;n Tr&aacute;mite</td>
                    <td class="CellContent" align="left" >&nbsp;<%=strRegionTramite%></td>
                    <td class="CellLabel"   align="left" >Dealer</td>
                    <td class="CellContent" align="left" >&nbsp;<%=MiUtil.getString(objOrderBean.getNpDealerName())%></td>

                </tr>
                <% //inicio de seccion que se muestra si y solo si no una reparación
                    if(!MiUtil.contentInArray(objOrderBean.getNpSpecificationId(),Constante.SPEC_REPAIRS)){
                        //
                %>
                <tr>
                    <td class="CellLabel" align="left">Fecha Hora Firma</td>
                    <td class="CellContent">&nbsp;
                        <input type="text" name="txtFechaHoraFirma" size="18" style="border :0; background-color:transparent;" readonly maxlength="18" value="<%=MiUtil.toFechaHora(objOrderBean.getNpSignDate())%>">
                    </td>
                    <td class="CellContent" align="center">
                      <!--TDECONV003 KOTINIANO  || switch tde-->
                      <div id="divFlagMigration" style="display: <% if(objOrderBean.getNpSpecificationId()== Constante.SPEC_POSTPAGO_VENTA && Constante.TDE_SWITCH_ON.equals(strTDESwitch)) { %> block <%}else{%>none <%}%>">
                           <span  style="font-weight: bold;color: #000000; padding: 3px 5px; margin-right: 5px;"> Migración Prepago a Postpago</span>
                          <input type="checkbox" name="chkFlagMigration" disabled="disabled" <% if(objOrderBean.getNpFlagMigracion()!= null && (objOrderBean.getNpFlagMigracion().equals("S")||objOrderBean.getNpFlagMigracion().equals("C"))){ %> checked="checked" <%} %>/><!-- TDECONV003-3 EFLORES -->
                       </div>
                   </td>
                   <% if(objOrderBean.getNpSpecificationId()== Constante.SPEC_POSTPAGO_VENTA && Constante.TDE_SWITCH_ON.equals(strTDESwitch)) { %>
                   <!--<td class="CellContent">&nbsp;</td>-->
                   <%}else{%>
                    <td class="CellContent">&nbsp;</td>
                   <%}%>

                    <td class="CellLabel" align="left">Creado Por</td>
                    <td class="CellContent">&nbsp;<%=MiUtil.getString(objOrderBean.getNpCreatedBy())%></td>
                    <td class="CellLabel" align="left">Fecha Creaci&oacute;n</td>
                    <td class="CellContent">&nbsp;<%=MiUtil.toFecha(objOrderBean.getNpCreatedDate())%> </td>
                </tr>

                <!-- Inicio [CHECK APODERADO - PROY 0787] JRIOS -->

                <tr id="rowDinamicoSST">
                </tr>

                <!-- Fin  [CHECK APODERADO - PROY 0787] JRIOS -->

                <%//PRY-0762 JQUISPE
                    HashMap hshRentaAdelantada = objOrdenAux.getOrdenRentaAdelantada(lOrderId0);
                    strMessage = (String)hshRentaAdelantada.get("strMessage");
                    if (strMessage!=null){
                  	  throw new Exception(strMessage);
                    }
                    
                    OrderRentaAdelantadaBean objOrderRentaAdelantadaBean = (OrderRentaAdelantadaBean)hshRentaAdelantada.get("objOrderRentaAdelantadaBean");
                    long longOrdenRentaAdelantadaId = objOrderRentaAdelantadaBean.getNpOrderRefRentaAdelantadaId();
                    if(longOrdenRentaAdelantadaId != 0){%>                    
                    <tr>
                       <td class="CellLabel" align="left">N° Orden RA</td>
                       <td class="CellContent">&nbsp;<a href="javascript:getShowOrdenRentaAdelantada('<%=longOrdenRentaAdelantadaId%>')"><%=longOrdenRentaAdelantadaId%></a></td>
                       <td class="CellContent">&nbsp;</td>
                       <td class="CellContent">&nbsp;</td>
                       <td class="CellContent">&nbsp;</td>
                       <td class="CellContent">&nbsp;</td>
                       <td class="CellContent">&nbsp;</td>
                       <td class="CellContent">&nbsp;</td>
                    </tr>
                <%}%>
                <!-- Cuarto grupo -->
                <tr >
                    <td class="CellLabel" align="left" colspan="8" >&nbsp;Descripción&nbsp;
                        <%if (iNoteCount.intValue() != 0){
                        %><font color="red"><b>(Ver Notas)</b></font><%
                        } else {
                        %>&nbsp;<%
                            }%>
                    </td>
                </tr>
                <tr>
                    <%
                        //Constante.SPEC_ARTICULOS_VARIOS:2033 es la categoria de orden rapida
                        //No coloca nada en la direccion entrega en los siguientes casos:
                        //1) Es una orden rapida que solicito que se ingrese direccion (campo marcado con "S") o
                        //2) Es una orden rapida de un cliente generico (en esos guarda DNI/RUC)
                        if      ((objOrderBean.getNpSpecificationId()==Constante.SPEC_ARTICULOS_VARIOS && MiUtil.getString(objOrderBean.getNpstatustmp()).equals("S")) ||
                                (objOrderBean.getNpSpecificationId()==Constante.SPEC_ARTICULOS_VARIOS && objOrderBean.getCsbCustomer().getSwCustomerId()==9999)){
                            if (!MiUtil.getString(objOrderBean.getNpShipToAddress1()).equals("")){
                                sCadAux1="NOMBRE:"+MiUtil.getString(objOrderBean.getNpShipToAddress1())+"\n";
                            }
                            if (!MiUtil.getString(objOrderBean.getNpShipToProvince()).equals("")){
                                sCadAux2="RUC/DNI::"+MiUtil.getString(objOrderBean.getNpShipToProvince())+"\n";
                            }
                            if (!MiUtil.getString(objOrderBean.getNpShipToAddress2()).equals("")){
                                sCadAux3="DIRECCION:"+MiUtil.getString(objOrderBean.getNpShipToAddress2())+"\n";
                            }
                            sDescripcionOrdenRapida=sCadAux1+sCadAux2+sCadAux3;
                            System.out.println("sDescripcionOrdenRapida:"+sDescripcionOrdenRapida);
                    %>
                    <td class="CellContent" colspan="8">&nbsp;
                        <textarea name="txtDetalle" cols="145" rows="4" readonly="readonly"><%=sDescripcionOrdenRapida+MiUtil.getString(objOrderBean.getNpDescription())%></textarea></td>
                    <%}else{%>
                    <td class="CellContent" colspan="8">&nbsp;
                        <textarea name="txtDetalle" cols="145" rows="4" readonly="readonly"><%=MiUtil.getString(objOrderBean.getNpDescription())%></textarea></td>
                    <%}%>
                </tr>

                <tr>
                    <td width="100%" colspan="9" height="10"></td>
                </tr>


                <tr>
                    <td width="100%" colspan="8"><img src="/images/pobtrans.gif" border="0" height="6"></td>
                </tr>

                <tr>
                    <td class="CellLabel" align="left">Lugar Despacho</td>
                    <td class="CellContent">&nbsp;
                        <%
                            hshOrder = objOrderService.getDispatchPlaceList(2001);
                            strMessage=(String)hshOrder.get("strMessage");
                            if (strMessage!=null)
                                throw new Exception(strMessage);

                            arrListado=(ArrayList)hshOrder.get("arrData");
                        %>
                        <%=MiUtil.getDescripcion(arrListado,"wn_npBuildingId", "wv_npShortName",objOrderBean.getNpDispatchPlaceId()+"")%></td>

                    <!--[Despacho en Tienda] Inicio PCASTILLO MMONTOYA 09/04/2013 -->
                  <!--PRY-1093 JCURI (isDeliverySpecification)-->
                  <% if (objOrderService.showCourier(objOrderBean.getNpSpecificationId()) || isDeliverySpecification) {%>
                    <td class="CellLabel" align="left">Entrega por Courier</td>
                    <% if (iCourier == 1){ %>
                    <td class="CellContent"><input type="checkbox" name="chkCourier" disabled="disabled" checked ></td>
                    <% } else { %>
                    <td class="CellContent"><input type="checkbox" name="chkCourier" disabled="disabled" ></td>
                    <% } %>
                    <%}%>
                    <!--[Despacho en Tienda] Fin PCASTILLO MMONTOYA 09/04/2013 -->
                    <td class="CellLabel"   align="left">Representante CC</td>
                    <td class="CellContent" align="left"><%=MiUtil.getString(objOrderBean.getNpUserName1())%></td>
                    <td class="CellLabel" align="left">
                        Fecha&nbsp;Proc.&nbsp;Aut.
                    </td>
                    <td class="CellContent"  >&nbsp;
                        <%=MiUtil.toFecha(objOrderBean.getNpScheduleDate())%>
                    </td>
                    <%if (objOrderBean.getNpSpecificationId()== Constante.SPEC_SUSPENSIONES[0]){%> <!--jsalazar - modif hpptt # 1 - 29/09/2010-->
                    <!-- Se agrego div para validar que la fecha de reconexion solo se muestre en suspensiones temporales rmartinez-->
                    <td class="CellLabel" align="left"><div id="dvFecReconexEdit" style= "display:none">Fecha&nbsp;Proc.&nbsp;Reconex.</div></td>
                    <td class="CellContent"  ><div id="dvFecReconexEditInput" style= "display:none">&nbsp;
                        <%=MiUtil.toFecha(objOrderBean.getNpScheduleDate2())%>
                    </div></td>

                    <%if (objOrderBean.getNpSpecificationId()== Constante.SPEC_SUSPENSIONES[0]){ //Se agrego llamada a metodo Javascript para validar el mostrado o no del Div de fecha de reconexion%>
                    <script type="text/javascript">
                        MuestraFechaReconexBySpecification("<%=objOrderBean.getNpSpecificationId()%>");
                    </script>
                    <%}%>
                    <%}%><!--jsalazar - modif hpptt # 1 - 29/09/2010-->

                    <!--jsalazar - modif hpptt # 1 - 29/09/2010 - inicio-->
                    <%if (objOrderBean.getNpSpecificationId()== Constante.SPEC_SERVICIOS_ADICIONALES[0]){%>
                    <td class="CellLabel" align="left"><div id="dvFechaFinProgEdit" style= "display:none">Fecha&nbsp;Fin&nbsp;Prog.</div></td>
                    <td class="CellContent"  ><div id="dvFechaFinProgEditInput" style= "display:none">&nbsp;
                        <%=MiUtil.toFecha(objOrderBean.getNpScheduleDate2())%>
                    </div></td>
                    <%if (objOrderBean.getNpSpecificationId()== Constante.SPEC_SERVICIOS_ADICIONALES[0]){%>
                    <script type="text/javascript">
                        MuestraFechaProgFin("<%=objOrderBean.getNpSpecificationId()%>");
                    </script>
                    <%}%>
                    <%}%>
                    <!--jsalazar - modif hpptt # 1 - 29/09/2010 - fin-->

                </tr>
                <%}%>
                <tr>
                    <td class="CellLabel" align="left">Forma de Pago</td>
                    <td class="CellContent">&nbsp;<%=MiUtil.getString(objOrderBean.getNpPaymentTerms())%></td>
                    <td class="CellLabel"   align="left">Fecha Prob. Pago</td>
                    <td class="CellContent" align="left" >&nbsp;<!--Fecha del proximo pago-->
                        <input type="text" name="txtFechaProbablePago" style="border :0;  background-color:transparent;" readonly size="10" value="<%=MiUtil.toFecha(objOrderBean.getNpPaymentFutureDate())%>" maxlength="10">
                    </td>
                    <td class="CellLabel"   align="left">Importe Factura</td>
                    <td class="CellContent" align="left">&nbsp;<%=MiUtil.formatDecimal(objOrderBean.getNpPaymentTotal())%></td>
                    <input type="hidden" name="txtImporteFactura" value="<%=objOrderBean.getNpPaymentTotal()%>">
                    </td>
                    <td class="CellLabel"   align="left">Estado&nbsp;Proc.&nbsp;Aut.</td>
                    <td class="CellContent" align="left">
                        <input type="text" name="txtEstadoProcAutom" size="12" maxlength="30" style="border :0;  background-color:transparent;" readonly  value="<%=MiUtil.getString(objOrderBean.getNpScheduleStatusName())%>"></td>

                </tr>
                <tr>
                    <td class="CellLabel"   align="left">Estado de Pago</td>
                    <td class="CellContent" align="left">&nbsp;<%=MiUtil.getString(objOrderBean.getNpPaymentStatus())%></td>  <!--Estado del pago-->
                    <%--RCDLRP - 21/08/2008 - INCIDENCIA #5150 - INICIO --%>
                    <td class="CellLabel"   align="left"><%=MiUtil.getString(strParentName)%></td>
                    <%
                        if (  !MiUtil.getString(strGeneratorid).equals("") && !MiUtil.getString(strParentUrl).equals("") ){
                    %><td class="CellContent" align="left"><%=strGeneratorid%></td><%
                }else{
                %><td class="CellContent">&nbsp;</td><%
                    }
                %>
					
					<!-- INICIO PRY-1239 25/09/2018 PCACERES -->
					<% if (MiUtil.getString(objOrderBean.getNpPaymentStatus()).equals("Cancelado")
                        && MiUtil.getString(sWebPayment).equals("1")) { %>
                        <td class="CellLabel">&nbsp;Importe Pagado</td>
                    <% } else {%>
					
                    <%--RCDLRP - 21/08/2008 - INCIDENCIA #5150 - FIN --%>
                    <td class="CellLabel" align="rigth">&nbsp;<a href="javascript:fxShowPayment();" >Importe Pagado</a>
                        <script>
                            function fxShowPayment(){
                                var url = "<%=strRutaContext%>/PAGEEDIT/PaymentList.jsp?nOrderId=<%=lOrderId0%>" ;
                                window.open(url,"WinListPagos","toolbar=no,location=0,directories=no,status=yes,menubar=0,scrollbars=no,resizable=yes,screenX=100,top=80,left=150,screenY=80,width=700,height=500,modal=yes");
                            };
                        </script>
                    </td>
					
					<% } %>
                    <!-- FIN PRY-1239 24/09/2018 PCACERES -->
					
                    <% if( (iFlagCarrier==1)   && ("Pendiente".equals(MiUtil.getString(objOrderBean.getNpPaymentStatus()))) ){ %>
                    <td class="CellContent">&nbsp;<input type="text" name="txtnpTotalPayment" size="8" style='text-align:right' readonly value="<%=MiUtil.formatDecimal(objOrderBean.getNpPaymentAmount())%>">
                        <a href="javascript:fxEditPayment();"  onmouseover="window.status='Agregar Pago';" onmouseout="window.status='';">
                            <img name="imgPago" Alt="Agregar Pago" src="<%=Constante.PATH_APPORDER_SERVER%>/images/crear.gif" border="no"></a>
                    </td>
                    <%}else if ((iFlagCarrier==-1) || (iFlagCarrier==0) || (iFlagCarrier==1 &&  Constante.INBOX_PROCESOS_AUTOMATICOS.equals(MiUtil.getString(objOrderBean.getNpStatus())) )){ %>
                    <td class="CellContent" size="8">&nbsp;<%=MiUtil.formatDecimal(objOrderBean.getNpPaymentAmount())%></td>
                    <%}%>

                    <input type="hidden" name="hdnTotalPaymentOrig" value="<%=objOrderBean.getNpPaymentAmount()%>">
                    <input type="hidden" name="hdnPagoBanco">
                    <input type="hidden" name="hdnPagoNroVoucher">
                    <input type="hidden" name="hdnPagoImporte">
                    <input type="hidden" name="hdnPagoFecha">
                    <input type="hidden" name="hdnPagoDisponible">
                    </td>
                    <td class="CellLabel" align="left">Imp. Dep. Garantía</td>
                    <td class="CellContent" align="left">&nbsp;<%=MiUtil.formatDecimal(Double.parseDouble(strTotalAmount))%></td>
                </tr>
                <tr>
                    <td class="CellLabel"   align="left">Fecha Pago</td>
                    <td class="CellContent" align="left">&nbsp;<%=MiUtil.toFecha(objOrderBean.getNpPaymentDate())%></td>  <!--Fecha del pago -->
                    <td class="CellLabel" align="left">Propuesta</td><!--CBARZOLA 30/07/2009-->
                    <td class="CellContent" align="left">&nbsp;<%=MiUtil.getString(objOrderBean.getNpproposedid())%></td>
                    <td class="CellLabel"   align="left">Saldo</td>

                    <% if( (iFlagCarrier==1)   && ("Pendiente".equals(MiUtil.getString(objOrderBean.getNpPaymentStatus()))) ){ %>
                    <td class="CellContent" align="left">&nbsp;<input type="text" name="txtSaldo" size="10" style='text-align:right' style="border :0; background-color: #F5F5EB;" readonly value="<%=MiUtil.formatDecimal(objOrderBean.getSaldo())%>">
                            <%}else if( (iFlagCarrier==-1)  || (iFlagCarrier==0) || (iFlagCarrier==1 &&  Constante.INBOX_PROCESOS_AUTOMATICOS.equals(MiUtil.getString(objOrderBean.getNpStatus())) )  ){%>
                    <td class="CellContent">&nbsp;<%=MiUtil.formatDecimal(objOrderBean.getSaldo())%>
                            <%}%>
                    <td class="CellLabel"  align="left">Imp. Dep. Gar. Pag.</td>
                    <td class="CellContent">&nbsp;<%=MiUtil.formatDecimal(Double.parseDouble(strDueAmount))%></td>
                </tr>
             
				<!-- INICIO PRY-1239 21/09/2018 PCACERES -->
				<tr>
					<td class="CellLabel" align="left">Pago Web</td>
                    <% if (MiUtil.getString(sWebPayment).equals("1")){ %>
                    <td class="CellContent"><input type="checkbox" name="chkWebPayment" disabled="disabled" checked ></td>
                    <% } else { %>
                    <td class="CellContent"><input type="checkbox" name="chkWebPayment" disabled="disabled" ></td>
                    <% } %>
					<td class="CellLabel"></td>
					<td class="CellContent"></td>
					<td class="CellLabel"></td>
					<td class="CellContent"></td>
					<td class="CellLabel"></td>
					<td class="CellContent"></td>
				</tr>
				<!-- FIN PRY-1239 21/09/2018 PCACERES -->

                <%//PRY-0890 JCURI
                    if(hdnOrderProrrateoValue != null && !"".equals(hdnOrderProrrateoValue)){%>
	                    <tr>
	                    <% if(hdnOrderProrrateoValue.equals("SPARENT")){%>                    	
	                        <td class="CellLabel" align="left">Orden de pago anticipado</td>
	                        <td class="CellContent">&nbsp;<a href="javascript:getShowOrdenProrrateo('<%=npOrderChildId%>')"><%=npOrderChildId%></a></td>
	                      <%} else {%> 
	                       		<td class="CellLabel" align="left">Orden de Venta/Portabilidad</td>
	                        	<td class="CellContent">&nbsp;<a href="javascript:getShowOrdenProrrateo('<%=npOrderParentId%>')"><%=npOrderParentId%></a></td>
	                      
	                       <%}%>
	                        <td class="CellContent">&nbsp;</td>
	                        <td class="CellContent">&nbsp;</td>
	                        <td class="CellContent">&nbsp;</td>
	                        <td class="CellContent">&nbsp;</td>
	                        <td class="CellContent">&nbsp;</td>
	                        <td class="CellContent">&nbsp;</td>
	                        <td class="CellContent">&nbsp;</td>
	                    </tr>
                    <%}%>
                                    
                <!--JRIOS-->
                <tr id="rowDinamicoCST">
                </tr>
                <!--JLIMAYMANTA-->
                <% String strTypeService="";
                    HashMap hashBuildingTS = new HashMap();
                    hashBuildingTS = NewOrderService.OrderDAOgetBuildingTS(portalSessionBean.getBuildingid(), portalSessionBean.getLogin());
                    hashBuildingTS = (HashMap)hashBuildingTS.get("hshDataTE");
                    System.out.println("hashBuildingTS"+hashBuildingTS);
                    if( hashBuildingTS!=null){
                        if (hashBuildingTS.size() != 0 ) {
                            strTypeService               = MiUtil.getString((String)hashBuildingTS.get("wv_typeservice"));
                            System.out.println("[strTypeService]"+strTypeService);
                        }
                    }
                %>

                <% System.out.println("[strTypeService===================================]"+strTypeService); %>
                <% System.out.println("[MiUtil.getString(objOrderBean.getNpVoucher())===================================]"+MiUtil.getString(objOrderBean.getNpVoucher())); %>
                <%if(!MiUtil.getString(objOrderBean.getNpVoucher()).equals("")){%>
                <tr>
                    <td class="CellLabel"   align="left" width="12%">Voucher:</td>
                    <td class="CellContent" align="left" width="13%">&nbsp;<%=MiUtil.getString(objOrderBean.getNpVoucher())%></td>
                    <td class="CellContent">&nbsp;</td>
                    <td class="CellContent">&nbsp;</td>
                    <td class="CellContent">&nbsp;</td>
                    <td class="CellContent">&nbsp;</td>
                    <td class="CellContent">&nbsp;</td>
                    <td class="CellContent">&nbsp;</td>
                    <td class="CellContent">&nbsp;</td>
                </tr>
                <%}%>
                <!--GGRANADOS-->
                <% if(MiUtil.getString(objOrderBean.getNpStatus()).equals("ALMACEN_TIENDA")){%>
                <tr>
                    <td class="CellLabel" align="left" width="3%">Procesado BSCS</td>
                    <td class="CellContent" align="left"><%=MiUtil.getString(objOrderBean.getNpProcesoAutom())%></td>
                </tr>
                <%}%>
                <% if (showLinkPenaltyOrder){%>
                <tr>
                    <td class="CellLabel"   align="left" width="12%">Orden de Penalidad:</td>
                    <td class="CellContent" align="left" width="13%">&nbsp;<a href="javascript:fxShowPopup('<%= orderPenaltyURL %>','Orden de Penalidad');"><%=MiUtil.getString(nppenaltyorderid)%></a></td>
                    <td class="CellContent">&nbsp;</td>
                    <td class="CellContent">&nbsp;</td>
                    <td class="CellContent">&nbsp;</td>
                    <td class="CellContent">&nbsp;</td>
                    <td class="CellContent">&nbsp;</td>
                    <td class="CellContent">&nbsp;</td>
                    <td class="CellContent">&nbsp;</td>
                </tr>
                <% } %>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <table border="0" cellspacing="0" cellpadding="0" width="99%">
                <tr>
                    <td width="100%"><img src="/images/pobtrans.gif" border="0" height="6">
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <!--Rechazos -->
    <% //inicio de seccion que se muestra si y solo si no una reparación
        if(!MiUtil.contentInArray(objOrderBean.getNpSpecificationId(),Constante.SPEC_REPAIRS)){
            //
    %>
    <tr>
        <td>
            <table id="tableRejects" name="tableRejects" border="0" cellpadding="0" cellspacing="1" align="center" width="100%">
                <tr>
                    <td class="CellLabel" align="center">&nbsp;&nbsp;&nbsp;#
                        <%if (arrReject.size()>0){ %>
                        <a href="javascript:fxDetailReject();"onmouseover="window.status='Detalle de Rechazo';" onmouseout="window.status='';">
                            <img name="viewDetail" Alt="Detalle de Rechazo" src="<%=Constante.PATH_APPORDER_SERVER%>/images/viewDetail.gif" border="no"></a>
                        <%}%>
                    </td>
                    <td class="CellLabel">&nbsp;&nbsp;&nbsp;Motivo&nbsp;Rechazo</td>
                    <td class="CellLabel">&nbsp;&nbsp;&nbsp;Estado</td>
                    <td class="CellLabel">&nbsp;&nbsp;&nbsp;Creado&nbsp;Por</td>
                    <td class="CellLabel">&nbsp;&nbsp;&nbsp;Fecha</td>
                    <td class="CellLabel">&nbsp;&nbsp;&nbsp;Subsanado&nbsp;Por</td>
                    <td class="CellLabel">&nbsp;&nbsp;&nbsp;Fecha</td>
                    <td class="CellLabel" align="center" id="divEditReject" style="display:'none';">&nbsp;</td>
                    <td class="CellLabel" align="center" id="divBlankReject"> </td>
                </tr>
                <%
                    /*hshOrder= objOrderService.getRejectList(lOrderId0);
                    strMessage=(String)hshOrder.get("strMessage");
                    if (strMessage!=null)
                       throw new Exception(strMessage);

                    arrReject=(ArrayList)hshOrder.get("arrRechazos");
                    rjbRechazos =null;
                    */
                    for (int i=0;i<arrReject.size(); i++){
                        rjbRechazos=(RejectBean)arrReject.get(i);
                %>
                <tr>
                    <td class="CellContent"><%=i+1%></td>
                    <td class="CellContent"><%=MiUtil.getString(rjbRechazos.getNpReason())%></td>
                    <td class="CellContent"><%=MiUtil.getString(rjbRechazos.getNpStatusDesc())%></td>
                    <td class="CellContent"><%=MiUtil.getString(rjbRechazos.getNpCreatedBy())%></td>
                    <td class="CellContent"><%=MiUtil.toFecha(rjbRechazos.getNpCreatedDate())%></td>
                    <td class="CellContent"><%=MiUtil.getString(rjbRechazos.getNpModifiedBy())%></td>
                    <td class="CellContent"><%=MiUtil.toFecha(rjbRechazos.getNpModifiedDate())%></td>
                    <td class="CellContent">&nbsp;</td>
                </tr>
                <%}%>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <table border="0" cellspacing="0" cellpadding="0" width="99%">
                <tr>
                    <td width="100%"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/pobtrans.gif" border="0" height="6">
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <table border="0" cellpadding="0" cellspacing="1" align="center" width="100%">
                <tr>
                    <td class="CellLabel" width="10%">&nbsp;&nbsp;Transportista</td>
                    <td class="CellContent" width="15%">&nbsp;<%=MiUtil.getString(objOrderBean.getNpCarrierName())%>
                    </td>
                    <td class="CellLabel" width="13%">&nbsp;&nbsp;Fecha Hora Entrega</td>
                    <td class="CellContent" width="12%">&nbsp;<input type="text" name="txtFechaHoraEntrega" style="border :0;  background-color:transparent;" readonly size="15" maxlength="20" value="<%=MiUtil.toFechaHora(objOrderBean.getNpDeliveryDate())%>" ></td>
                    <td class="CellContent" align="center" width="16%">&nbsp;
                    </td>
                    <td class="CellContent" width="16%" >&nbsp;
                    </td>
                    <td class="CellContent" width="*.%" >&nbsp;
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <!-- Inicio [PPNN] MMONTOYA -->
    <% if (objOrderService.requiresDocumentVerification(lOrderId0)) { %>
    <tr>
        <td>
            <table border="0" cellpadding="0" cellspacing="1" align="center" width="100%">
                <tr>
                    <td class="CellLabel"   align="left" width="20%">Verificación de documentos</td>
                    <td class="CellContent" align="left"><font color="red">&nbsp;<%=objOrderBean.getDocVerificationBean().getResultDesc()%></font></td>
                </tr>
            </table>
        </td>
    </tr>
    <% } %>
    <!-- Fin [PPNN] MMONTOYA -->
    <%if(  (Constante.INBOX_PROCESOS_AUTOMATICOS.equals(MiUtil.getString(objOrderBean.getNpStatus()))) && (iFlagCarrier==1) && (iFirstInbox!="1") ) {%>
    <tr>
        <td>
            <table border="0" cellspacing="0" cellpadding="0" width="99%">
                <tr>
                    <% ArrayList objInboxBack = objOrderService.getInboxList(MiUtil.getString(objOrderBean.getNpBpelbackinboxs()), MiUtil.getString(objOrderBean.getNpSpecificationId() ));
                        if (objInboxBack!=null){
                            iNumInboxBack=objInboxBack.size();
                            System.out.println("iNumInboxBack:"+iNumInboxBack);
                        }
                    %>
                    <td class="CellLabel"  width="20%">&nbsp;&nbsp;Acci&oacute;n </td>
                    <td class="CellContent" width="20%">&nbsp;
                        <select name="cmbAction" style="width: 50%;" onchange="javascript:fxCambiarInbox(this.value,<%=objOrderBean.getNpSpecificationId()%>);document.frmdatos.hdnAction.value =this[this.selectedIndex].text;">
                            <%
                                hshData =
                                        objOrderService.getActionList(objOrderBean.getNpSpecificationId(),objOrderBean.getNpStatus(),MiUtil.getString(objOrderBean.getNpBpelbackinboxs()),lOrderId0);
                                strMessage0=(String)hshData.get("strMessage");
                                if (strMessage0!=null)
                                    throw new Exception(strMessage0);
                                arrListado=(ArrayList)hshData.get("objListado");
                                if (iNumInboxBack==0){//eliminar la acción retroceder
                                    HashMap h=null;
                                    String strDatoV=null;
                                    if (arrListado!=null){
                                        System.out.println("arrListado.size - (I):"+arrListado.size());
                                        for(int i = 0; i < arrListado.size(); i++){
                                            h = (HashMap)arrListado.get(i);
                                            strDatoV= (String)h.get("npactionvalue");
                                            System.out.println("strDatoV:"+strDatoV);
                                            if (strDatoV.equals(Constante.ACTION_INBOX_BACK))
                                                arrListado.remove(i);
                                        }
                                        System.out.println("arrListado.size - (II):"+arrListado.size());
                                    }
                                }
                                // Fiiltra los inbox que deben aparecer dado el specificacionId, el bpel conversation y el estado de la orden
                     /*hshData=objOrderService.getListaAccion(arrListado,objOrderBean.getNpSpecificationId(),objOrderBean.getNpBpelconversationid(),objOrderBean.getNpStatus());
                     strMessage0=(String)hshData.get("strMessage");                                          
                     if (strMessage0!=null)
                        throw new Exception(strMessage0);                               
                     arrListado=(ArrayList)hshData.get("objListado");  */
                            %>
                            <%=MiUtil.buildCombo(arrListado,"npactionvalue","npactiondesc")%>
                        </select>
                        <%
                            // Si dentro de las opciones del combo accion se encuentra la opción BAGLOCK
                            boolean bolEditRejectPermit=MiUtil.contentInArrayList(arrListado,"npactionvalue",Constante.ACTION_INBOX_BAGLOCK);
                            int iEditRejectPermit=(bolEditRejectPermit==true?1:0);
                            //Si el estado de la orden es
                            if ( ! bolEditRejectPermit){
                                if (Constante.ACTION_INBOX_BAGLOCK.equals(objOrderBean.getNpStatus())){
                                    iEditRejectPermit = 1;
                                }
                            }
                        %>
                        <script>
                            var EditRejectPermit=<%=iEditRejectPermit%>;
                            if (EditRejectPermit == 1){
                                divEditReject.style.display="";
                                divBlankReject.style.display="none";
                            }else{
                                divEditReject.style.display="none";
                                divBlankReject.style.display="";
                            }
                        </script>
                        <input type="hidden" name="hdnAction">
                        <%if ("Disabled".equals(MiUtil.getString((String)hshScreenField.get("action")))){%>
                        <script>
                            document.frmdatos.cmbAction.disabled=true;
                        </script>
                        <%}%>
                    </td>


                    <td class="CellContent" width="40%" >
                        <table  width="100%"  border="0" name="idRegionInboxBack" id="idRegionInboxBack" style="display:'none'">
                            <tr>
                                <td class="CellLabel" width="50%">&nbsp;&nbsp;Destino</td>
                                <td class="CellContent" style="width: 50%;" >&nbsp;
                                    <select name="cmbInboxBack" onchange="javascript:document.frmdatos.hdnInboxBack.value =this[this.selectedIndex].text;">
                                        <%=MiUtil.buildCombo(objInboxBack,"datoBPel","datoBPel")%>
                                    </select>
                                </td>
                            </tr>
                        </table>
                        <table width="100%" border="0" name="idRegionInboxNext" id="idRegionInboxNext" style="display:'none'">
                            <tr>
                                <td class="CellLabel" width="50%">&nbsp;&nbsp;Destino</td>
                                <td class="CellContent" style="width: 50%;" >&nbsp;
                                    <select name="cmbInboxNext" onchange="javascript:document.frmdatos.hdnInboxBack.value =this[this.selectedIndex].text;">
                                        <option value=""></option>
                                    </select>
                                </td>
                            </tr>
                        </table>
                        <input type="hidden" name="hdnInboxBack">
                    </td>
                    <td class="CellContent" width="20%" >&nbsp;
                    </td>
                    <td class="CellContent" width="*.%" >&nbsp;
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <%}%>

    <tr>
        <td>
            <table border="0" cellpadding="0" cellspacing="1" align="center" width="100%">
                <tr>
                    <td class="CellContent" width="*.%" >
                        <% if ( ("Enabled".equals(MiUtil.getString((String)hshScreenField.get("createdocument"))))  && (iFlagCarrier==-1) &&  ("Cancelado".equals(MiUtil.getString(objOrderBean.getNpPaymentStatus()))) ){ %>
                        <input name="btnGenerarDocumentos" type="button" value="Generar Comprobantes" onclick="javascript:fxGenerarComprobantes();">
                        <%}%>
                        <!--BOTON AUTORIZACION DE DEVOLUCION-->
                        <!--<script>alert("<%=autorizacionDevolucion%>")</script>-->
                        <% if (autorizacionDevolucion.equals("0")){ %>
                        <input name="btnFinanzas" type="button" value="Autorizar Extorno" onclick="javascript:fxAutorizacionDevolucion();">
                        <%}%>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <%}else{%>
    <td>
        <table border="0" cellspacing="0" cellpadding="0" width="99%">
            <tr>
                <td  class="CellContent" width="80%" >&nbsp;
                    <!--BOTON AUTORIZACION DE DEVOLUCION-->
                    <!-- <script>alert("<%=autorizacionDevolucion%>")</script>-->
                    <% if (autorizacionDevolucion.equals("0")){ %>
                    <input name="btnFinanzas" type="button" value="Autorizar Extorno" onclick="javascript:fxAutorizacionDevolucion();">
                    <%}%>
                </td>
                <td  class="CellContent" width="20%" >&nbsp; </td>
            </tr>
        </table>
    </td>
    <%} %>
    <!--JPEREZ - INICIO -->
    <!--<input type="hidden" name="solution_id" value="<%=objOrderBean.getNpSolutionId()%>">-->
    <input type="hidden" name="hdnDivisionId" value="<%=objOrderBean.getNpDivisionId()%>">
    <!-- Campos para excepciones  -->
    <input type="hidden" name="hdnExceptionInstallation" value=""/>
    <input type="hidden" name="hdnExceptionPrice" value="<%=objOrderBean.getNpExceptionPrice()%>"/>
    <input type="hidden" name="hdnExceptionPlan" value="<%=objOrderBean.getNpExceptionPlan()%>"/>
    <input type="hidden" name="hdnExceptionWarrant" value="<%=objOrderBean.getNpExceptionWarrant()%>"/>
    <input type="hidden" name="hdnExceptionRevenue" value="<%=objOrderBean.getNpExceptionRevenue()%>"/>
    <input type="hidden" name="hdnExceptionRevenueAmount" value="<%=objOrderBean.getNpExceptionRevenueAmount()%>"/>
    <input type="hidden" name="hdnExceptionBillCycle" value="<%=objOrderBean.getNpExcepcionBillCycle()%>"/>
    <input type="hidden" name="hdnExceptionTotal" value=""/>
    <INPUT type="hidden" name="hdnPeriodoIni" value="<%=objOrderBean.getNpBeginPeriod()%>">
    <INPUT type="hidden" name="hdnPeriodoFin" value="<%=objOrderBean.getNpEndPeriod()%>">
    <input type="hidden" name="hdnWorkflowType" value="<%=objOrderBean.getNpWorkFlowId()%>"> <!-- to editReject -->
    <input type="hidden" name="hdnSalesStructOrigenId" value="<%=objOrderBean.getSalesStructureOriginalId()%>"/> <!-- SAR 0037-165858 MCB -->
    <input type="hidden" name="hdnOrigen" value="<%=objOrderBean.getNpOrigen()%>"/> <!-- EFLORES PRY-0979 -->
    <tr>
</TABLE>
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
                        <td class="CellContent" width="15%"><input type="radio" name="cpufresponse" id="cpufresponse0" value="0" disabled="disabled">No tengo CPUF
                        </td>
                        <td class="CellContent" width="15%"><input type="radio" name="cpufresponse" id="cpufresponse1" value="1" disabled="disabled">No conozco CPUF
                        </td>
                        <td class="CellContent" width="60%"><input type="button" onclick="fxShowPopulateCenterDet()" value="Ingresar CPUF"/></td>
                    </tr>

                </table>

            </td>
        </tr>
    </table>
</div>

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
                        <td class="CellLabel" width="15%">Tipo de Documento</td>
                        <td class="CellContent" width="25%" id="docTypeAssignee"></td>
                        <td class="CellLabel" width="15%">N&uacute;mero de documento</td>
                        <td class="CellContent" width="45%" id="docNumAssignee"></td>
                    </tr>

                    <tr>
                        <td class="CellLabel" width="15%">Nombres</td>
                        <td class="CellContent" width="25%" id="firstNameAssignee"></td>
                        <td class="CellContent" width="60%" colspan="2">&nbsp;</td>
                    </tr>
                    <tr>
                        <td class="CellLabel" width="15%">Apellido Paterno</td>
                        <td class="CellContent" width="25%" id="lastNameAssignee"></td>
                        <td class="CellContent" width="60%" colspan="2">&nbsp;</td>
                    </tr>
                    <tr>
                        <td class="CellLabel" width="15%">Apellido Materno</td>
                        <td class="CellContent" width="25%" id="familyNameAssignee"></td>
                        <td class="CellContent" width="60%" colspan="2">&nbsp;</td>
                    </tr>
                </table>

            </td>
        </tr>
    </table>
</div>

<div id="dvDigitalizacionDocumentos" style="display: none;">
    <br/>
    <table border="0" cellspacing="0" cellpadding="0" width="25%">
        <tr class="PortletHeaderColor">
            <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"></td>
            <td class="SubSectionTitle" align="left" valign="top"><%=Constante.SECTION_DIGIT_DOCUMENT%></td>
            <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"></td>
        </tr>
    </table>

    <table class="RegionBorder" border="0" cellpadding="0" cellspacing="1" width="100%" style="text-align: left;">
        <tr>
            <td class="CellLabel" width="15%">Tipo de Firma</td>
            <td class="CellContent" id="signatureType" width="25%"></td>
            <td class="CellContent" width="60%"><!--<a href="javascript:fxShowDocumentsToGenerate()">Visualizar Documentos a digitalizar</a>--></td>
        </tr>

        <tr id="rowDinamico">
        </tr>

    </table>

</div>

<div id="dvVerificacionIdentidadAislada" style="display: block;">
    <br/>
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
                        <td class="CellLabel">Fecha de uso</td>
                        <td class="CellLabel">Fecha de vencimiento</td>
                    </tr>
                    </thead>
                    <tbody id="dataIsolatedVerification">
                    <tr>
                        <td class="CellContent" colspan="6">No se encontraron registros</td>
                    </tr>
                    </tbody>
                </table>

            </td>
        </tr>
    </table>

</div>

<script DEFER>
    /* Cambiamos el nombre del título de la página */
    parent.document.title = "<%=strOrderId0%>";

    <% if (validateAction){ %>
    document.frmdatos.cmbAction.disabled = true;
    <%}%>

    fxShowSectionsVIDD();
</script>

<%}catch(SessionException se) {
    //se.printStackTrace();
    out.println("<script>alert('Su sesión ha finalizado. Ingrese nuevamente');</script>");
    String strUrl = "/portal/pls/portal/WEBSALES.NP_USER_AVAILABILITY_PL_PKG.PL_LOGOUT";
    //out.print("<script>parent.mainFrame.location.replace('"+strUrl+"');</script>");
}catch(Exception e) {
    String strMessageExceptionGeneralStart = "";
    strMessageExceptionGeneralStart = MiUtil.getMessageClean(e.getMessage());
    out.println("<script>alert('"+strMessageExceptionGeneralStart+"');</script>");
}
%>
<!-- Fin de JP_ORDER_EDIT_ORDER.jsp -->
