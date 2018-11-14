<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="oracle.portal.provider.v2.render.PortletRenderRequest"%>
<%@page import="oracle.portal.provider.v2.http.HttpCommonConstants"%>
<%@page import="oracle.portal.provider.v2.ProviderSession"%>
<%@page import="oracle.portal.provider.v2.ProviderUser"%>
<%@page import="oracle.portal.provider.v2.render.PortletRendererUtil"%>
<%@page import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil" %>
<%@page import="pe.com.nextel.service.*"%>
<%@page import="pe.com.nextel.bean.*"%>
<%@page import="pe.com.nextel.exception.SessionException"%>
<%@page import="pe.com.nextel.util.*"%>
<%@page import="pe.com.nextel.service.SessionService"%>
<%@page import="java.util.*,java.text.SimpleDateFormat"%>
<%@page import="pe.com.nextel.service.NewOrderService"%>
<%@page import="pe.com.portability.service.PortabilityOrderService"%>
<%@page import="pe.com.nextel.util.Constante"%>

<%

    try{
        System.out.println("---------------------- INICIO JP_ORDER_EDIT_ORDER------------------");
        String strSessionId="";

  /* inicio - comentar para probar localmente*/

        try{
            PortletRenderRequest pReq = (PortletRenderRequest) request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
            ProviderUser objetoUsuario1 = pReq.getUser();
            strSessionId=objetoUsuario1.getPortalSessionId();
            System.out.println("Sesi?n capturada  JP_ORDER_EDIT_ORDER_ShowPage : " + objetoUsuario1.getName() + " - " + strSessionId );

        }catch(Exception e){
            System.out.println("Portler Not Found : " + e.getClass() + " - " + e.getMessage() );
            out.println("Portlet JP_ORDER_EDIT_ORDER_ShowPage Not Found");
            return;
        }

        //strSessionId = "749126701476259056994469063087083508994001614";
        //strSessionId = "998102396";

        System.out.println("Sesi?n capturada despu?s del request : " + strSessionId );
        PortalSessionBean objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
        if(objPortalSesBean==null) {
            System.out.println("No se encontr? la sesi?n de Java ->" + strSessionId);
            throw new SessionException("La sesi?n finaliz?");
        }

        String strBuildingId =null;
        String strLogin=null;
        int iLevel=0;
        String strCode=null;
        String strUser=null;
        int iUserId=0;
        int iAppId=0;
        String strMessage = null;
        String strParentUrl = "";
        String strParentName = "";
        int iNumInboxBack=0;

        //[Despacho en Tienda] PCASTILLO
        int iCourier = 0;

        String autorizacionDevolucion="";
        strBuildingId=objPortalSesBean.getBuildingid()+"";
        strLogin=objPortalSesBean.getLogin();
        iLevel=objPortalSesBean.getLevel();
        strCode=objPortalSesBean.getCode();
        strUser=objPortalSesBean.getNom_user();
        iUserId= objPortalSesBean.getUserid();
        iAppId= objPortalSesBean.getAppId();

        System.out.println("[JP_ORDER_EDIT_ORDER][strSessionId] --> "+strSessionId);
        System.out.println("[JP_ORDER_EDIT_ORDER][iUserId]  --> "+iUserId);
        System.out.println("[JP_ORDER_EDIT_ORDER][iAppId]   --> "+iAppId);
        System.out.println("[JP_ORDER_EDIT_ORDER][strLogin] --> "+strLogin);
  /*Defino Par?metro utilizar en hidden*/
        String strAppId = MiUtil.getString(iAppId);
  /*Fin*/

        String strTienda = "";
        String strRegionTramite = "";

        //No se debe mostrar la tienda y region tramite del usuario logeado, debe ser de la orden, #6182
  /*strTienda = objPortalSesBean.getTienda();
  strRegionTramite = objPortalSesBean.getRegionTramite();*/

  /* Inicio Data */
        String strDataSalesmanLabel = "<td class='CellLabel' colspan=1 align='center' name='vendedorDataLabel' id='vendedorDataLabel'>Vendedor Data</td>";
        String strDataSalesmanHiddenLabel = "<td class='CellLabel' colspan=1 align='center' name='vendedorDataLabel' id='vendedorDataLabel'>&nbsp;</td>";
        String strDataSalesmanField = "&nbsp;<td class='CellContent' colspan=1 id='vendedorData' name='vendedorData'  align='center'>&nbsp;<select name='cmbVendedorData' style='width: 90%' onchange='fxChangeSalesManData(this);'><option value=''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option></select></td>";
        String strDataSalesmanHiddenField = "<td class='CellContent' colspan=1 id='vendedorData' name='vendedorData'>&nbsp;</td> ";
  /* Fin Data */

        //Parametros
        String strOrderId0=(request.getParameter("an_nporderid")==null?"0":request.getParameter("an_nporderid"));
        long lOrderId0=Long.parseLong(strOrderId0);

        //Variables para rellenar el combo de Tienda
        HashMap hshDataMap = new HashMap();
        ArrayList valoresCombo = new ArrayList();
        strMessage = null;
        //Obteniendo los datos de la Orden
        HashMap hshOrder=null;
        HashMap hshOrderAux=null;
        String strMessage0=null;
        OrderBean objOrderBean=null;
        ArrayList arrListado=null;
        ArrayList arrReject=null;
        //String strExclusivity=null;
        RejectBean rjbRechazos = null;
        EditOrderService objOrderService=new EditOrderService();
        GeneralService objGeneralService= new GeneralService();
        CustomerService objCustomerService = new CustomerService();
        PortabilityOrderService objPortabOrderServ = new PortabilityOrderService();
        PenaltyService objPenaltyService = new PenaltyService(); //EFLORES REQ-0428

        //[Despacho en Tienda] PCASTILLO
        int iShowCourier = objOrderService.getEnabledCourier(lOrderId0); // Mostrar habilitado el check COURIER
        //System.out.println("[JP_ORDER_EDIT_ORDER][iShowCourieriShowCourieriShowCourieriShowCourieriShowCourieriShowCourier] --> "+iShowCourier);
        //Automatizaciones
        AutomatizacionService objAutomatizacionService = new AutomatizacionService();

        NewOrderService objOrdenAux = new NewOrderService();
        hshOrderAux = objOrdenAux.getNoteCount(lOrderId0);
        Integer iNoteCount = (Integer)hshOrderAux.get("iTotal");

        HashMap hshData=null;
        HashMap hshScreenField=null;
        HashMap hshRolMult = new HashMap();
        HashMap hshAutomatizacion = new HashMap();
        int iFlagComboVendedor=0;

        String strTotalAmount = "";
        String strDueAmount = "";

        hshOrder=objOrderService.getOrder(lOrderId0);
        System.out.println("---------------------- AVANCE ------------------");
        autorizacionDevolucion=objOrderService.getAutorizacionDevolucion(lOrderId0,iUserId,iAppId);

        strMessage0=(String)hshOrder.get("strMessage");

        if (strMessage0!=null)
            throw new Exception(strMessage0);

        objOrderBean=(OrderBean)hshOrder.get("objResume");

        //HTENORIO validacion si la orden tiene flujo bpel

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

        //[Despacho en Tienda] PCASTILLO
        iCourier = objOrderBean.getNpFlagCourier();

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

        //Rechazos
        hshOrder=objOrderService.getRejectList(lOrderId0);
        strMessage0=(String)hshOrder.get("strMessage");
        if (strMessage0!=null)
            throw new Exception(strMessage0);

        arrReject = (ArrayList)hshOrder.get("arrRechazos");

        // JCASAS Deposito en Garantia
        hshOrder = objOrderService.getGuarantee(lOrderId0, Constante.PAYMENT_SOURCE, 23);
        strMessage0=(String)hshOrder.get("strMessage");
        if (strMessage0!=null)
            throw new Exception(strMessage0);
        strTotalAmount = (String)hshOrder.get("lTotalAmount");
        strDueAmount = (String)hshOrder.get("lDueAmount");

        //Manejo dinamico de controles
        //PRY-1081
        hshOrder=objOrderService.getEditOrderScreenField(lOrderId0,Constante.PAGE_ORDER_EDIT, objOrderBean.getNpSpecificationId(), objOrderBean.getNpStatus(), iUserId, iAppId);
        strMessage0=(String)hshOrder.get("strMessage");
        if (strMessage0!=null)
            throw new Exception(strMessage0);

        hshScreenField= (HashMap)hshOrder.get("hshData");

        //Manejo(Validacion) del control ReplaceHandset
        hshAutomatizacion = objAutomatizacionService.doShowButtonReplaceHandset(lOrderId0,strLogin);
        strMessage0 = (String)hshAutomatizacion.get("strMessage");
        System.out.println("Mensaje_Replace_Handset -->"+strMessage0);
        if (strMessage0!=null){
            throw new Exception(strMessage0);
        }//Fin

        //Verfifica si el combo del vendedor es modificable
        hshOrder= objOrderService.getFlagModifiySalesName(objOrderBean.getNpSpecificationId(),objOrderBean.getNpProviderGrpId(),iUserId,iAppId);
        strMessage0=(String)hshOrder.get("strMessage");
        if (strMessage0!=null)
            throw new Exception(strMessage0);
        iFlagComboVendedor = MiUtil.parseInt((String)hshOrder.get("iRetorno"));

        System.out.println("---------------------- INICIO JP_ORDER_EDIT_ORDER------------------");
        System.out.println("strOrderId-->"+lOrderId0);
        System.out.println("CustomerId-->"+objOrderBean.getCsbCustomer().getSwCustomerId());
        System.out.println("RegionID-->"+objOrderBean.getNpRegionId());
        System.out.println("lSpecificationId-->"+objOrderBean.getNpSpecificationId());
        System.out.println("strBuildingId--> "+strBuildingId);
        System.out.println("---------------------- FIN JP_ORDER_EDIT_ORDER------------------");

        int iSpecificationId = objOrderBean.getNpSpecificationId();

        // Se concatenan ScreenOption a evaluar
        int iFlagPayForms = -1;
        int iFlagCarrier = -1;
        int iFlagAddDocument = -1;
        String strScreenOption = "";
        strScreenOption = Integer.toString(Constante.SCRN_OPTTO_PAYFORM)+";"+Integer.toString(Constante.SCRN_OPTTO_CARRIER)+";"+Integer.toString(Constante.SCRN_OPTTO_CARRIER)+";"+Integer.toString(Constante.SCRN_OPTTO_ADDDOCUMENT);
        hshRolMult = objGeneralService.getRolMult(strScreenOption, iUserId, iAppId);
        strMessage=(String)hshRolMult.get("strMessage");
        if (strMessage!=null)
            throw new Exception(strMessage);
        String strResulRol = hshRolMult.get("strResult").toString();
        String [] strCampos = strResulRol.split(";");
        for(int i=0; i<strCampos.length;i++){
            if (i%2 == 0){
                if(strCampos[i].equals(Integer.toString(Constante.SCRN_OPTTO_PAYFORM)))
                    iFlagPayForms = Integer.parseInt(strCampos[i+1]);
                if(strCampos[i].equals(Integer.toString(Constante.SCRN_OPTTO_CARRIER)))
                    iFlagCarrier = Integer.parseInt(strCampos[i+1]);
                if(strCampos[i].equals(Integer.toString(Constante.SCRN_OPTTO_ADDDOCUMENT)))
                    iFlagAddDocument = Integer.parseInt(strCampos[i+1]);
            }
        }
        System.out.println("[JP_ORDER_EDIT_ORDER][iFlagPayForms]"+iFlagPayForms);
        System.out.println("[JP_ORDER_EDIT_ORDER][iFlagCarrier]"+iFlagCarrier);
        System.out.println("[JP_ORDER_EDIT_ORDER][iFlagAddDocument]"+iFlagAddDocument);
  
  /* Inicio Data */
        HashMap hshDataValidate = new HashMap();
        String strShowDataFields = "0";
        String strLoadSellerData = "";
        String strDataSalesProvId = "";
        String strFlagexclusivity = "";

        ArrayList arrSalesDataList=null;
        strMessage = null;
        System.out.println("hdnSpecification: "+objOrderBean.getNpSpecificationId());
        System.out.println("strGeneratorType: "+strGeneratortype);
        System.out.println("lGeneratorId: "+MiUtil.parseLong(strGeneratorid));
        System.out.println("strCustomerId: "+objOrderBean.getCsbCustomer().getSwCustomerId());
        System.out.println("strSiteId: "+objOrderBean.getNpSiteId());
        hshDataValidate = objGeneralService.getSalesDataShow( objOrderBean.getNpSpecificationId(),strGeneratortype, MiUtil.parseLong(strGeneratorid), objOrderBean.getCsbCustomer().getSwCustomerId(), objOrderBean.getNpSiteId());
        strMessage = (String)hshDataValidate.get("strMessage");
        if (strMessage!=null)
            throw new Exception(strMessage);

        strShowDataFields = (String)hshDataValidate.get("strShowDataFields");
        strLoadSellerData = (String)hshDataValidate.get("strLoadSellerData");
        strDataSalesProvId = (String)hshDataValidate.get("strDataSalesProvId");
        strFlagexclusivity = (String)hshDataValidate.get("strFlagExclusivity");

        if ( strShowDataFields.equalsIgnoreCase("1") || strLoadSellerData.equalsIgnoreCase("1")  ){
            //Obtiene la lista de Vendedores Data
            HashMap hshVendedorData = new HashMap();

            hshVendedorData= objGeneralService.getSalesDataList( Constante.SALES_RULE_ID_DATA);
            strMessage=(String)hshVendedorData.get("strMessage");
            if (strMessage!=null)
                throw new Exception(MiUtil.getMessageClean(strMessage));

            arrSalesDataList=(ArrayList)hshVendedorData.get("arrSalesDataList");
        }

        //Obtiene el vendedor de data
        HashMap hshOrderSeller = objGeneralService.getOrderSellerByType(objOrderBean.getNpOrderId(), 2    );
        strMessage=(String)hshOrderSeller.get("strMessage");
        if (strMessage!=null)
            throw new Exception(strMessage);
        String strSellerName = (String)hshOrderSeller.get("strSellerName");
        objOrderBean.setNpProviderGrpIdData(MiUtil.parseLong((String)hshOrderSeller.get("strSellerId")));
   
   /* Fin Data*/

        // Modificaciones para suspensiones :
        int plazo = 5;
        int iCantidadDiasCalendario = objOrderService.getAmountCalendarDays(MiUtil.toFecha(objOrderBean.getNpCreatedDate()),plazo);
        String sFechaFinalOriginal = objOrderService.getFechaFinalIntervalo(MiUtil.toFecha(objOrderBean.getNpCreatedDate()),plazo);

        //Trayendo el numero de dias (59 dias) para calcular la fecha final de suspensiones
        HashMap hshMaxNumDias = objGeneralService.getDominioList("MAX_DIAS_SUSPENSION");
        ArrayList arrModalitySellList = (ArrayList) hshMaxNumDias.get("arrDominioList");
        DominioBean diasMaximos = (DominioBean)arrModalitySellList.get(0);

        int iScreenOption_Id = Constante.SCRN_OPTTO_EDITRECONEXDATE;
        System.out.println("[JP_ORDER_EDIT_ORDER - VALIDACION DE FECHAS SUSPENSIONES][iScreenOption_Id]"+iScreenOption_Id);
        System.out.println("[JP_ORDER_EDIT_ORDER - VALIDACION DE FECHAS SUSPENSIONES][iUserId]"+iUserId);
        System.out.println("[JP_ORDER_EDIT_ORDER - VALIDACION DE FECHAS SUSPENSIONES][iAppId]"+iAppId);
        System.out.println("[JP_ORDER_EDIT_ORDER - VALIDACION DE FECHAS SUSPENSIONES][sFechaFinalOriginal]"+sFechaFinalOriginal);

        //iAppId = 20;
        //int iUserId2=250241;
        long iPermisosEditFecha = objCustomerService.CustomerDAOgetRol(iScreenOption_Id,iUserId,iAppId);
        System.out.println("[JP_ORDER_EDIT_ORDER - VALIDACION DE FECHAS SUSPENSIONES][iPermisosEditFecha]"+iPermisosEditFecha);
        //<!--jsalazar - modif hpptt # 1 - 29/09/2010 - inicio-->
        boolean fechProcMen=true;
        boolean fechProcIgual=false; // indica si la fecha del sistema es igual que la fecha de proceso

        Date today=MiUtil.getDateBD("dd/MM/yyyy");
        Date fechPro=objOrderBean.getNpScheduleDate();
        String strFechPro=MiUtil.getDate(fechPro,"dd/MM/yyyy");
        Date miFech=MiUtil.toFecha(strFechPro,"dd/MM/yyyy");
        String strToday=MiUtil.getDate(today,"dd/MM/yyyy");

        if(objOrderBean.getNpSpecificationId()== Constante.SPEC_SERVICIOS_ADICIONALES[0] &&
                objOrderBean.getNpStatus()==Constante.INBOX_PROCESOS_AUTOMATICOS ){
            if ((today.compareTo(miFech))==-1) { //fecha del sistemas es menor que la fecha del de proceso
                fechProcMen=true;
            }else {
                fechProcMen=false; // es mayor o igual  a la fecha del sistema
            }
            if ((today.compareTo(miFech))==0) { //fecha del sistemas es igual a la fecha del de proceso
                fechProcIgual=true;
            }else {
                fechProcIgual=false; // fecha del sistema es diferente a la fecha del proceso
            }


        } else {
            fechProcMen=true;// es mayor o igual  a la fecha del sistema
        }

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

        // INICIO PRY-1239 24/09/2018 PCACERES
		
        String sWebPayment = "";
        sWebPayment = objOrderBean.getVchWebPayment();
		
        // FIN PRY-1239 24/09/2018 PCACERES

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
        System.out.println("PZF_EDIT: strCustomerId = " + strCustomerId);

        IsolatedVerificationService objIsolatedVerificationService = new IsolatedVerificationService();
        List<IsolatedVerifConfigBean> specLst = objIsolatedVerificationService.getViaConfig(strCustomerId);
        
        String strRutaContext=request.getContextPath();
        String actionURL_DigitalDocumentServlet = strRutaContext+"/digitalDocumentServlet";
        String actionURL_PopulateCenterServlet = strRutaContext+"/populateCenterServlet";
        
     	// Inicio [reason Change of model] CDIAZ
        HashMap mapSpecificationsChangeModel = objGeneralService.getValueNpTable("CHANGE_MODEL_SPECIFICATION");
		ArrayList arrSpecificationsChangeModel = (ArrayList)mapSpecificationsChangeModel.get("objArrayList");
		boolean flagShowReasonCdm = false;
        for (int i = 0; i < arrSpecificationsChangeModel.size(); i++) {
        	TableBean specificationChangeModelBean =  (TableBean)arrSpecificationsChangeModel.get(i);
        	
        	if(iSpecificationId == Integer.parseInt(specificationChangeModelBean.getNpValue())){
        		flagShowReasonCdm = true;
        		break;
        	}
        }
     	// Fin [reason Change of model] CDIAZ

     	//PRY-0890 JCURI
     	System.out.println("JP_ORDER_EDIT_ORDER_ShowPage.jsp [PRY-0890 JCURI]");
     	String npProrrateo = objOrderBean.getNpProrrateo();
        String npOrderChildId = objOrderBean.getNpOrderChildId();
        String npOrderParentId =objOrderBean.getNpOrderParentId();
        String hdnOrderProrrateoValue = "";
        
        request.setAttribute("strProrrateo", objOrderBean.getNpProrrateo());
 		request.setAttribute("strPaymentTotalProrrateo", objOrderBean.getNpPaymentTotalProrrateo());
 		System.out.println("JP_ORDER_EDIT_ORDER_ShowPage.jsp [getNpOrderId] : "  + objOrderBean.getNpOrderId());     		
 		System.out.println("JP_ORDER_EDIT_ORDER_ShowPage.jsp [nppaymenttotalprorrateo] : "  + objOrderBean.getNpPaymentTotalProrrateo());
        System.out.println("JP_ORDER_EDIT_ORDER_ShowPage.jsp [strProrrateo] : "  + npProrrateo);
        System.out.println("JP_ORDER_EDIT_ORDER_ShowPage.jsp [nporderchildid] : "  + npOrderChildId);
        System.out.println("JP_ORDER_EDIT_ORDER_ShowPage.jsp [getNpOrderparentId] : "  + npOrderParentId);
        
     		
     	if(npOrderChildId!=null && !"".equals(npOrderChildId)) {
 		hdnOrderProrrateoValue = "SPARENT";
     	} else if(npOrderParentId!=null && !"".equals(npOrderParentId)) {
 		hdnOrderProrrateoValue = "SCHILD";
 	}     			
 	System.out.println("JP_ORDER_EDIT_ORDER_ShowPage.jsp [hdnOrderProrrateoValue] : "  + hdnOrderProrrateoValue);
 		         			  
     	
  
//TDECONV003- Flag funcionalidad - 1 encendido | 0 apagado
        String strTDESwitch = objGeneralService.getValue(Constante.TDE_SWITCH, Constante.TDE_SWITCH_NPVALUEDESC);
        System.out.println("KOR_TDE_SWITCH:  " + strTDESwitch);
   	
        
		//JCURI PRY-1093
        boolean isDeliverySpecification = false;
        HashMap objHashMapDeliverySpecification = new HashMap();        
        objHashMapDeliverySpecification = objGeneralService.GeneralDAOgetComboList("ORDEN_DELIVERY_REGION_SPECIFICATION");
        if( objHashMapDeliverySpecification.get("strMessage") != null ) { 
        	System.out.println("JP_ORDER_EDIT_CUSTOMER_ShowPage.jsp [ORDEN_DELIVERY_REGION_SPECIFICATION]" + objHashMapDeliverySpecification.get("strMessage"));
        } else {
        	ArrayList arrListDeliverySpecification = (ArrayList) objHashMapDeliverySpecification.get("objArrayList"); 
          	System.out.println("JP_ORDER_EDIT_CUSTOMER_ShowPage.jsp [arrListDeliverySpecification] " + arrListDeliverySpecification);          
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
        System.out.println("JP_ORDER_EDIT_CUSTOMER_ShowPage.jsp [isDeliverySpecification] " + isDeliverySpecification);
   	
%>  
<!--START MSOTO: 08-04-2014 SAR N_O000019563 Se deshabilita el combo--> 
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/jQuery-min.js"></script>

<script>
    function comparaFechas(strFechaI,strFechaF){
        //Fecha Proceso

        var day_I    = parseFloat(strFechaI.substring(0,2));
        var month_I  = parseFloat(strFechaI.substring(3,5));
        var year_I   = parseFloat(strFechaI.substring(6,10));

        var day_F  = parseFloat(strFechaF.substring(0,2));
        var month_F  = parseFloat(strFechaF.substring(3,5));
        var year_F   = parseFloat(strFechaF.substring(6,10));
        if (year_F < year_I){
            return false;
        };
        if (month_F < month_I){
            return false;
        };
        if (day_F <= day_I){
            return false;
        };

        return true;
    }

    function fxValidateFechaFin(obj){ //Agregado para el la validacion de fechas en suspensiones
        form = document.frmdatos;
        var wv_dateProceso = form.txtFechaProceso.value;
        var wv_dateFinProg = form.txtFechaReconexion.value;



        var wv_message = "La fecha Fin de Programaci?n debe ser mayor a la fecha de Proceso";




        if ( wv_dateProceso != "" && wv_dateFinProg !=""){
            if ( !isValidDate(wv_dateFinProg)){
                form.txtFechaReconexion.value="";
                form.txtFechaReconexion.select();
                return;
            };

            //Fecha Proceso
            var day_P    = parseFloat(wv_dateProceso.substring(0,2));
            var month_P  = parseFloat(wv_dateProceso.substring(3,5));
            var year_P   = parseFloat(wv_dateProceso.substring(6,10));

            //Fecha Reconexion
            var day_Fin    = parseFloat(wv_dateFinProg.substring(0,2));
            var month_Fin  = parseFloat(wv_dateFinProg.substring(3,5));
            var year_Fin   = parseFloat(wv_dateFinProg.substring(6,10));


            //compararcion con la fecha fin
            if (year_Fin < year_P){
                alert(wv_message);
                form.txtFechaReconexion.value="";
                form.txtFechaReconexion.select();
                return;
            };
            if (year_Fin == year_P){
                if (month_Fin < month_P){
                    alert(wv_message);
                    form.txtFechaReconexion.value="";
                    form.txtFechaReconexion.select();
                    return;
                };
                if (month_Fin == month_P){
                    if (day_Fin <= day_P){
                        alert(wv_message);
                        form.txtFechaReconexion.value="";
                        form.txtFechaReconexion.select();
                        return;
                    };
                }
            }
        }


        if ( wv_dateProceso == "" && wv_dateFinProg !=""){
            alert("Debe ingresar la Fecha de Proceso");
            form.txtFechaReconexion.value="";
            form.txtFechaProceso.select();
            return;
        }


        return true;

    }
    function fxValidateFechaInicio(obj){ //Agregado para el la validacion de fechas en suspensiones

        form = document.frmdatos;
        var wv_dateProceso = form.txtFechaProceso.value;
        var wv_dateFinProg = form.txtFechaReconexion.value;
        var wv_dateToday = "<%=strToday%>";
        var wv_dateFechProBD = "<%=strFechPro%>";
        var wv_message = "La Fecha de Inicio de Programaci?n debe ser mayor o igual a la fecha actual";
        var wv_message1 = "La Fecha de Inicio de Programaci?n debe ser menor a la Fecha de Fin de Programaci?n";
        var validar_fe_actual=false;
        // strFechPro
        if ( wv_dateProceso != ""){
            if ( !isValidDate(wv_dateProceso)){
                form.txtFechaProceso.value="";
                form.txtFechaProceso.select();
                return;
            };

            //Fecha de proceso en la bd
            var day_BD    = parseFloat(wv_dateFechProBD.substring(0,2));
            var month_BD  = parseFloat(wv_dateFechProBD.substring(3,5));
            var year_BD   = parseFloat(wv_dateFechProBD.substring(6,10));

            //Fecha Proceso
            var wv_fProceso= new Date();
            var day_P    = parseFloat(wv_dateProceso.substring(0,2));
            var month_P  = parseFloat(wv_dateProceso.substring(3,5));
            var year_P   = parseFloat(wv_dateProceso.substring(6,10));

            //Fecha actual
            var day_today    = parseFloat(wv_dateToday.substring(0,2));
            var month_today  = parseFloat(wv_dateToday.substring(3,5));
            var year_today   = parseFloat(wv_dateToday.substring(6,10));


            if (year_P != year_BD || month_P != month_BD ||day_P != day_BD  ){
                validar_fe_actual=true;
            };

            if(validar_fe_actual){

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
        }


        if ( wv_dateProceso != "" && wv_dateFinProg !=""){
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

            //Fecha Fin
            var day_Fin    = parseFloat(wv_dateFinProg.substring(0,2));
            var month_Fin  = parseFloat(wv_dateFinProg.substring(3,5));
            var year_Fin   = parseFloat(wv_dateFinProg.substring(6,10));

            if (year_Fin < year_P){
                alert(wv_message);
                form.txtFechaProceso.value="";
                form.txtFechaProceso.select();
                alert(wv_message1);
                return;
            };
            if (year_Fin == year_P){
                if (month_Fin < month_P){
                    alert(wv_message);
                    form.txtFechaProceso.value="";
                    form.txtFechaProceso.select();
                    alert(wv_message1);
                    return;
                };
                if (month_Fin == month_P){
                    if (day_Fin <= day_P){
                        form.txtFechaProceso.value="";
                        form.txtFechaProceso.select();
                        alert(wv_message1);
                        return;
                    };
                }
            }
        }

        return true;

    }

  //TDECONV003 KOTINIANO
  function fxChangeFlagMigration(FlagMigration){
      if(FlagMigration.checked){
          document.getElementById("hdnFlagMigration").value="S";
      }else{
          document.getElementById("hdnFlagMigration").value="N";
      }
  }


	
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
    function fxShowSectionsVIDD(){
        getChannel();
        validateVIDD();
        fxShowSectionDigitDocument();
        fxShowSectionPopulateCenter();
        fxShowSectionAuthContact();
        fxValidateSpecLstDigitalization();
    }
    function fxUpdateResponse() {
        var form= document.frmdatos;
        jQuery("#dvPopulateCenter input[name=cpufresponse]").attr('checked',false);
        form.hdnCpufResponse.value=2;
    }
    function validateVIDD(){
        var form = document.frmdatos;
        var v_specificationId=form.hdnSubCategoria.value;
        var v_channel = form.hdnChannelClient.value;
        var v_buildingId= <%=strBuildingId%>;
        var v_orderId =<%=strOrderId0%>;
        var v_divisionId=<%=objOrderBean.getNpDivisionId()%>;
        var v_customerId= form.txtCompanyId.value;
        var flag=0;
        $.ajax({
            url:"<%= actionURL_DigitalDocumentServlet %>",
            type:"GET",
            async:false,
            cache:false,
            data:{
                "myaction":"validateSpecialCaseVIDD",
                "buildingId" : v_buildingId,
                "type":<%= Constante.TYPE_ORDER_ID%>,
                "specificationId":v_specificationId,
                "channel":v_channel,
                "orderId":v_orderId,
                "divisionId":v_divisionId,
                "customerId":v_customerId
            },
            dataType:"json",
            success:function(data){
                if(data != null){
                    var message=data.strMessage||'';
                    var messageCase=data.strMessageCase||'';
                    var strFlagSpecialCase=data.strFlagSpecialCase||'';
                    form.hdnFlgSAC.value="";
                    form.hdnFlgSDD.value="";
                    form.hdnFlgCPUF.value="";
                    form.hdnFlgVIA.value ="";

                    if(message==''){
                        form.hdnFlgSAC.value=data.assigneesection;
                        form.hdnFlgSDD.value=data.digitalsection;
                        form.hdnFlgCPUF.value=data.cpufsection;
                        form.hdnFlgVIA.value=data.identverifsection;
                        if(messageCase==''){
                            if(strFlagSpecialCase==1){
                                form.hdnFlgSAC.value=<%=Constante.FLAG_SECTION_ST_ACTIVE%>;
                                form.hdnFlgSDD.value=<%=Constante.FLAG_SECTION_ST_ACTIVE%>;
                            }
                        }
                    }
                }
            },
            error:function(xhr,status,error) {
                alert(xhr.responseText);
            }
        });
    }

    function fxShowSectionAuthContact(){
        var td1,td2,td3;
        td1='<td class="CellLabel"><div id="dvAssignee" style="display:none">&nbsp;Apoderado&nbsp;</div></td>';
        td2='<td class="CellContent" align="left"> <div id="dvChkAssignee" style="display:none"></div></td>';
        td3='<td class="CellContent" colspan="7"><div id="dvChkAssigneeNextCell" style="display:none">&nbsp;</div></td>';

        var flag_st = <%=MiUtil.contentInArray(objOrderBean.getNpSpecificationId(),Constante.SPEC_REPAIRS)%>;

        if(form.hdnFlgSAC.value==<%=Constante.FLAG_SECTION_ST_ACTIVE%>) {
            $('#rowDinamicoCST').html(td1+td2+td3);;
            fxEditSectionAuthContact();
        }else {
            if(flag_st){
                $('#rowDinamicoCST').html(td1+td2+td3);
            }else{
                $('#rowDinamicoSST').html(td1+td2+td3);
            }
            fxDetailSectionAuthContact();
        }
    }

    function fxDetailSectionAuthContact(){
        $('#tdLabelDocType').empty().html('Tipo de Documento');
        $('#tdLabelNumDoc').empty().html('N&uacute;mero de documento');
        $('#tdLabelName').empty().html('Nombres');
        $('#tdLabelLastName').empty().html('Apellido Paterno');
        $('#tdLabelFamilyName').empty().html('Apellido Materno');

        $.ajax({
            url: "<%= actionURL_DigitalDocumentServlet %>",
            type: "GET",
            async:false,
            cache:false,
            data: {
                "myaction": "getDocAssignee",
                "orderId": "<%=strOrderId0%>"
            },
            dataType: 'json',
            success: function (data) {
                var message = data.strMessage || '';

                if (message == '') {
                    var docAssignee=data.docAssigneeBean;
                    form.hdnDocAssigneeId.value=docAssignee.id;

                    $('#tdDocTypeAssignee').text(docAssignee.typeDoc);
                    $('#tdDocNumAssignee').text(docAssignee.numDoc);
                    $('#tdFirstNameAssignee').text(docAssignee.firstName);
                    $('#tdLastNameAssignee').text(docAssignee.lastName);
                    $('#tdFamilyNameAssignee').text(docAssignee.familyName);

                    $('#dvChkAssignee').html("<input type='checkbox' name='chkAssignee' id='chkAssignee' checked=true value ='1' disabled='disabled'>");
                    $('#dvChkAssignee').show();
                    $('#dvAssignee').show();
                    $('#dvChkAssigneeNextCell').show();
                    $('#dvAssigneeSection').show();
                }
                else {
                    if (form.hdnDocGenId.value == "") {
                        $('#dvAssigneeSection').hide();
                        $('#dvAssignee').hide();
                        $('#dvChkAssignee').hide();
                        $('#dvChkAssigneeNextCell').hide();
                    } else {
                        $('#dvChkAssignee').html("<input type='checkbox' name='chkAssignee' id='chkAssignee' disabled='disabled'>");
                        $('#dvAssignee').show();
                        $('#dvChkAssignee').show();
                        $('#dvChkAssigneeNextCell').show();
                    }

                }
            },
            error: function (xhr, status, error) {
                alert(xhr.responseText);
            }
        });
    }

    function fxEditSectionAuthContact(){
        var form = document.frmdatos;

        var colDocTypeAssignee = $('#tdDocTypeAssignee');
        var colDocNumAssignee = $('#tdDocNumAssignee');
        var colFirstNameAssignee = $('#tdFirstNameAssignee');
        var colLastNameAssignee = $('#tdLastNameAssignee');
        var colFamilyNameAssignee = $('#tdFamilyNameAssignee');
        var dvChkAssignee = $("#dvChkAssignee");

        colDocTypeAssignee.html('<select id="cmbDocTypeAssignee" name="cmbDocTypeAssignee" onblur="javascript:fxLoadMaxLenghtDocType();"><option value=""></option></select>');
        colDocNumAssignee.html('<input type="text" id="txtDocNumAssignee" name="txtDocNumAssignee" size="25">');
        colFirstNameAssignee.html('<input type="text" id="txtFirstNameAssignee" name="txtFirstNameAssignee" maxLength="100" style="width: 100%" >');
        colLastNameAssignee.html('<input type="text" id="txtLastNameAssignee" name="txtLastNameAssignee" maxLength="100" style="width: 100%" >');
        colFamilyNameAssignee.html('<input type="text" id="txtFamilyNameAssignee" name="txtFamilyNameAssignee" maxLength="100" style="width: 100%" >');

        $('#tdLabelDocType').empty().html('<FONT color=#ff0000><B>*</B></FONT>Tipo de Documento');
        $('#tdLabelNumDoc').empty().html('<FONT color=#ff0000><B>*</B></FONT>N&uacute;mero de documento');
        $('#tdLabelName').empty().html('<FONT color=#ff0000><B>*</B></FONT>Nombres');
        $('#tdLabelLastName').empty().html('<FONT color=#ff0000><B>*</B></FONT>Apellido Paterno');
        $('#tdLabelFamilyName').empty().html('<FONT color=#ff0000><B>*</B></FONT>Apellido Materno');

        $.ajax({
            url: "<%= actionURL_DigitalDocumentServlet %>",
            type: "GET",
            async:false,
            cache:false,
            data: {
                "myaction": "getDocAssignee",
                "orderId": "<%=strOrderId0%>"
            },
            dataType: 'json',
            success: function (data) {
                var message = data.strMessage || '';

                if (message == '') {
                    var docAssignee = data.docAssigneeBean;
                    form.hdnDocAssigneeId.value=docAssignee.docAssigneeId;
                    form.hdnChkAssignee.value=1;
                    form.hdnCmbDocTypeAssigneeText.value = docAssignee.typeDoc;

                    if(form.hdnGenStatus.value == ""){

                        getComboAssignee();

                        $('#cmbDocTypeAssignee option:contains(' + docAssignee.typeDoc + ')').each(function(){
                            if ($(this).text() == docAssignee.typeDoc) {
                                $(this).attr('selected', 'selected');
                                return false;
                            }
                            return true;
                        });

                        $('#txtDocNumAssignee').val(docAssignee.numDoc);
                        $('#txtFirstNameAssignee').val(docAssignee.firstName);
                        $('#txtLastNameAssignee').val(docAssignee.lastName);
                        $('#txtFamilyNameAssignee').val(docAssignee.familyName);

                        dvChkAssignee.html('<input type="checkbox" name="chkAssignee" id="chkAssignee" checked=true value ="1" onclick="fxSetValueAssignee(this)">');
                        dvChkAssignee.show();
                        $('#dvChkAssigneeNextCell').show();

                        $('#dvAssigneeSection').show();
                        $('#dvAssignee').show();
                    }else{

                        colDocTypeAssignee.empty().text(docAssignee.typeDoc);
                        colDocNumAssignee.empty().text(docAssignee.numDoc);
                        colFirstNameAssignee.empty().text(docAssignee.firstName);
                        colLastNameAssignee.empty().text(docAssignee.lastName);
                        colFamilyNameAssignee.empty().text(docAssignee.familyName);

                        dvChkAssignee.html('<input type="checkbox" name="chkAssignee" id="chkAssignee" checked=true value ="1" disabled="disabled">');
                        dvChkAssignee.show();

                        $('#dvAssignee').show();
                        $('#dvChkAssigneeNextCell').show();

                        $('#dvAssigneeSection').show();
                    }
                }else{
                    if(form.hdnGenStatus.value == ""){
                        dvChkAssignee.html('<input type="checkbox" name="chkAssignee" id="chkAssignee" onclick="fxSetValueAssignee(this)">');
                        dvChkAssignee.show();

                        $('#dvAssignee').show();
                        $('#dvChkAssigneeNextCell').show();

                        $('#dvAssigneeSection').hide();
                    }else{
                        dvChkAssignee.html('<input type="checkbox" name="chkAssignee" id="chkAssignee" disabled="disabled">');
                        dvChkAssignee.show();

                        $('#dvAssignee').show();
                        $('#dvChkAssigneeNextCell').show();

                        $('#dvAssigneeSection').hide();
                    }

                }
            },
            error: function (xhr, status, error) {
                alert(xhr.responseText);
            }
        });


    }

    function fxSetValueAssignee(chkAssignee){
        form  = document.frmdatos;
        if (chkAssignee.checked == true){
            chkAssignee.value = "1";
            form.hdnChkAssignee.value = "1";
            getComboAssignee();
            $('#dvAssigneeSection').show();
        }
        else{
            fxCleanSectionAssignee();
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

        $.ajax({
            url:"<%= actionURL_DigitalDocumentServlet %>",
            type:"GET",
            async:false,
            cache:false,
            data:{
                "myaction":"getChannel",
                "customerId" : <%=objOrderBean.getCsbCustomer().getSwCustomerId()%>
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

    function fxShowDocumentsToGenerate(){
        var vform=document.frmdatos;
        var specificationId=vform.hdnSubCategoria.value;
        var customerId    = vform.txtCompanyId.value;
        var orderId="<%=strOrderId0%>";
        var divisionId="<%=objOrderBean.getNpDivisionId()%>";
        var cmbSignature=$('#cmbSignature');
        var tipoEjec;
        var signatureType=cmbSignature.val()||vform.hdnSignatureType.value;
        if(signatureType==<%= Constante.SIGNATURE_TYPE_DIGITAL %>){
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
        var vform=document.frmdatos;
        var display=vform.hdnDisplay.value;
        var orderId="<%=strOrderId0%>";
        var url="/portal/page/portal/orders/POPULATE_CENTER_LIST";
        url = url + "?orderId="+orderId+"&display="+display+"&userLogin="+'<%=strLogin%>';
        var vurl = "/portal/pls/portal/websales.NPSL_GENERAL_PL_PKG.WINDOW_FRAME?av_title="+escape("Centro Poblado de Uso Frecuente")+"&av_url="+escape(url);
        vWinNewCondition = window.open(vurl,"vWinNewCondition","toolbar=no,location=0,directories=no,status=yes,menubar=0,scrollbars=yes,resizable=no,screenX=100,top=80,left=100,screenY=80,width=1060,height=800,modal=yes");
    }
    function fxShowSectionPopulateCenter(){

        var form=document.frmdatos;
        if(form.hdnFlgCPUF.value==<%=Constante.FLAG_SECTION_ACTIVE%>){
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
                    form.hdnCpufResponse.value=cpufresponse;
                    if (cpufresponse==0){
                        $("#cpufResponse0").attr("checked", "checked");
                    }else if(cpufresponse==1){
                        $("#cpufResponse1").attr("checked", "checked");
                    }
                    if(form.hdnGenStatus.value=='0' || form.hdnGenStatus.value==""){
                        form.hdnDisplay.value='<%=Constante.SHOW_TYPE_EDIT%>';
                    }else{
                        $("#dvPopulateCenter input[name=cpufresponse]").attr('disabled','disabled');
                        form.hdnDisplay.value='<%=Constante.SHOW_TYPE_DETAIL%>';
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
    }
    function fxShowSectionDigitDocument(){
        var form=document.frmdatos;
        if(form.hdnFlgSDD.value==<%=Constante.FLAG_SECTION_ST_ACTIVE%>) {
            fxEditSectionDigitDocument();
        }else{
            fxDetailSectionDigitDocument();
        }
    }

    function fxEditSectionDigitDocument(){
        var rowDinamico=$('#rowDinamico');
        var tdsignatureType = $('#signatureType').empty();

        tdsignatureType.html('<select name="cmbSignature"  id="cmbSignature" onchange="updateSection()"/>');
        var cmbSignature = $('#cmbSignature');

        $.ajax({
            url:"<%= actionURL_DigitalDocumentServlet %>",
            type:"GET",
            async:false,
            cache:false,
            data:{
                "myaction":"getDocumentGeneration",
                "orderId" : "<%=strOrderId0%>"
            },
            dataType:'json',
            success:function(data){
                var message=data.strMessage||'';
                if(message==''){
                    var documentGeneration=data.documentGenerationBean;
                    form.hdnDocGenId.value=documentGeneration.id;
                    form.hdnGenStatus.value=documentGeneration.generationStatus||"";
                    form.hdnTrxType.value=documentGeneration.trxType;
                    form.hdnSignatureType.value=documentGeneration.signatureType;
                    if(form.hdnGenStatus.value == ""){
                        form.hdnUpdateDocGen.value = <%=Constante.FLAG_UPD_DOC_GENERATION_YES%>;

                        getComboFirma(cmbSignature);

                        cmbSignature.val(documentGeneration.signatureType);

                        if (documentGeneration.signatureType == "<%=Constante.SIGNATURE_TYPE_DIGITAL%>") {
                            cargaFirmaDigital('<%=Constante.SHOW_TYPE_EDIT_DETAIL%>',rowDinamico,documentGeneration.email);
                        } else if (documentGeneration.signatureType == "<%=Constante.SIGNATURE_TYPE_MANUAL%>") {
                            cargaFirmaManual('<%=Constante.SHOW_TYPE_EDIT_DETAIL%>',rowDinamico,documentGeneration.signatureReasonLabel);
                        }
                        $('#dvDigitalizacionDocumentos').show();
                    }else{
                        form.hdnUpdateDocGen.value = <%=Constante.FLAG_UPD_DOC_GENERATION_NO%>;
                        form.hdnTransType.value = documentGeneration.trxWs;
                        tdsignatureType.empty();
                        tdsignatureType.text(documentGeneration.signatureTypeLabel);
                        if (documentGeneration.signatureType == "<%=Constante.SIGNATURE_TYPE_DIGITAL%>") {
                            cargaFirmaDigital('<%=Constante.SHOW_TYPE_DETAIL%>',rowDinamico,documentGeneration.email);
                        } else if (documentGeneration.signatureType == "<%=Constante.SIGNATURE_TYPE_MANUAL%>") {
                            cargaFirmaManual('<%=Constante.SHOW_TYPE_DETAIL%>',rowDinamico,documentGeneration.signatureReasonLabel);
                        }
                        $('#dvDigitalizacionDocumentos').show();
                    }

                }
                else{
                    form.hdnUpdateDocGen.value = <%=Constante.FLAG_UPD_DOC_GENERATION_YES%>;
                    getComboFirma(cmbSignature);
                    if(cmbSignature.val()==<%= Constante.SIGNATURE_TYPE_DIGITAL %>){
                        cargaFirmaDigital('<%=Constante.SHOW_TYPE_EDIT%>',rowDinamico,null);
                    }else if(cmbSignature.val()==<%= Constante.SIGNATURE_TYPE_MANUAL %>){
                        cargaFirmaManual('<%=Constante.SHOW_TYPE_EDIT%>',rowDinamico,null);
                    }
                    $('#dvDigitalizacionDocumentos').show();
                }
            },
            error: function(xhr, status, error) {
                alert(xhr.responseText);
            }
        });

    }

    function fxDetailSectionDigitDocument(){
        var tdsignatureType = $('#signatureType').empty();
        $.ajax({
            url:"<%= actionURL_DigitalDocumentServlet %>",
            type:"GET",
            async:false,
            cache:false,
            data:{
                "myaction":"getDocumentGeneration",
                "orderId" : "<%=strOrderId0%>"
            },
            dataType:'json',
            success:function(data){
                var message=data.strMessage||'';
                if(message==''){
                    form.hdnFlgDocument.value=1;
                    var documentGeneration=data.documentGenerationBean;
                    form.hdnDocGenId.value=documentGeneration.id;
                    var rowDinamico=$('#rowDinamico');
                    var documentGeneration=data.documentGenerationBean;
                    form.hdnGenStatus.value=documentGeneration.generationStatus;
                    form.hdnSignatureType.value=documentGeneration.signatureType;
                    tdsignatureType.text(documentGeneration.signatureTypeLabel);
                    form.hdnTrxType.value=documentGeneration.trxType;
                    if (documentGeneration.signatureType == "<%=Constante.SIGNATURE_TYPE_DIGITAL%>") {
                        cargaFirmaDigital('<%=Constante.SHOW_TYPE_DETAIL%>',rowDinamico,documentGeneration.email);
                    } else if (documentGeneration.signatureType == "<%=Constante.SIGNATURE_TYPE_MANUAL%>") {
                        cargaFirmaManual('<%=Constante.SHOW_TYPE_DETAIL%>',rowDinamico,documentGeneration.signatureReasonLabel);
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
        var txtSite          = form.txtSiteId.value;

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

    function cargaFirmaManual(showType,rowDinamico,label) {
        var td1,td2,td3;
        if(showType=='<%=Constante.SHOW_TYPE_EDIT%>'){
            td1 = '<td class="CellLabel" width="15%"><font color="red"><b>*</b></font>Motivo</td>';
            td2 = '<td class="CellContent" width="25%"><select id="cmbReason" name="cmbReason" style="width: 100%"/></td>';
            td3 = '<td class="CellContent"/>';
            rowDinamico.html(td1 + td2 + td3);
            getComboReason();
        }

        if(showType=='<%=Constante.SHOW_TYPE_DETAIL%>'){
            td1='<td class="CellLabel" width="15%">Motivo</td>';
            td2='<td class="CellContent" width="25%">'+label+'</td>';
            td3='<td class="CellContent"/>';
            rowDinamico.html(td1 + td2 + td3);
        }

        if(showType=='<%=Constante.SHOW_TYPE_EDIT_DETAIL%>'){
            td1 = '<td class="CellLabel" width="15%"><font color="red"><b>*</b></font>Motivo</td>';
            td2 = '<td class="CellContent" width="25%"><select id="cmbReason" name="cmbReason" style="width: 100%"/></td>';
            td3 = '<td class="CellContent"/>';
            rowDinamico.html(td1 + td2 + td3);
            getComboReason();

            $('#cmbReason option:contains(' + label + ')').each(function(){
                if ($(this).text() == label) {
                    $(this).attr('selected', 'selected');
                    return false;
                }
                return true;
            });

        }


    }
    function cargaFirmaDigital(showType,rowDinamico,label){
        var td1,td2,td3;
        if(showType=='<%=Constante.SHOW_TYPE_EDIT%>'){
            td1='<td class="CellLabel" width="15%"><font color="red"><b>*</b></font>Correo Electr&oacute;nico</td>';
            td2='<td class="CellContent" width="25%"><input type="text" name="txtEmailDG"  maxlength="80" style="width: 100%"  onchange="fxValidateEmail(this)"></td>';
            td3 = '<td class="CellContent"/>';
            rowDinamico.html(td1+td2+td3);
            getEmail();
        }
        if(showType=='<%=Constante.SHOW_TYPE_DETAIL%>'){
            td1='<td class="CellLabel" width="15%">Correo Electr&oacute;nico</td>';
            td2='<td class="CellContent" width="25%">'+label+'</td>';
            td3='<td class="CellContent"/>';
            rowDinamico.html(td1+td2+td3);
        }
        if(showType=='<%=Constante.SHOW_TYPE_EDIT_DETAIL%>'){
            td1='<td class="CellLabel" width="15%"><font color="red"><b>*</b></font>Correo Electr&oacute;nico</td>';
            td2='<td class="CellContent" width="25%"><input type="text" id="txtEmailDG" name="txtEmailDG"  maxlength="80" style="width: 100%"  onchange="fxValidateEmail(this)" value="'+label+'"></td>';
            td3 = '<td class="CellContent"/>';
            rowDinamico.html(td1+td2+td3);
        }

    }
    function updateSection(){
        var form = document.frmdatos;

        var cmbSignature=$('#cmbSignature');
        var rowDinamico=$('#rowDinamico');
        rowDinamico.empty();


        if(cmbSignature.val()==<%= Constante.SIGNATURE_TYPE_DIGITAL %>){
            cargaFirmaDigital('<%=Constante.SHOW_TYPE_EDIT%>',rowDinamico,null);
            //fxValidateSpecLstDigitalization(); //JVERGARA Mostrar seccion de digitalizacion
        }else if(cmbSignature.val()==<%= Constante.SIGNATURE_TYPE_MANUAL %>){
            cargaFirmaManual('<%=Constante.SHOW_TYPE_EDIT%>',rowDinamico,null);
            //fxValidateViaType(<%= Constante.SIGNATURE_TYPE_MANUAL %>); //JVERGARA Ocultar seccion de digitalizacion
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

 	 //JCURI PRY-1093
    function fxValidateDispatchList(dispatchPlaceId) {
    	form  = document.frmdatos;
    	arrDispatchPlacesDelivery = parent.mainFrame.arrDispatchPlacesDelivery;
 	   
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
//FIN VIDD
</script>
<!--jsalazar - modif hpptt # 1 - 29/09/2010 - Fin-->


<script DEFER>
    var wn_nxpedidoid = "<%=lOrderId0%>";
    var gpRejectArr  = new Array();
    var g_orderid    = "<%=lOrderId0%>";
    var gIndexReject =0;
    var gIndexRejectIni =0;
    var wv_npFlagProcesoAutom="<%=objOrderBean.getNpScheduleStatus()%>";
    var wv_swtype="<%=MiUtil.getString(objOrderBean.getNpCompanyType())%>";

    var dPaymentTotal = "<%=objOrderBean.getNpPaymentTotal()%>";
    var strPaymentStatus = "<%=MiUtil.getString(objOrderBean.getNpPaymentStatus())%>";
    window.parent.document.title='<%=lOrderId0%>';
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
        var txtSite          = form.txtSiteId.value;
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
            alert("formato de fecha no v?lido (dd/MM/yyyy)");
            return false;
        }
        return true;
    }

    //INICIO DERAZO REQ-0940
    function fxDynamicContactSection(dispatchPlaceId){
        form  = document.frmdatos;
        var flagTraceability = parent.mainFrame.flagTraceabilityFunct;

        if(flagTraceability != null){
            //Si esta activo el flag, realizamos las validaciones
            if(flagTraceability == 1){
                //Realizamos las validaciones para mostrar o no la seccion de contactos
                var specificationId = form.hdnSubCategoria.value;
                var typeDocument = form.hdnTipoDocumento.value;
                var numDocument = form.hdnNumDocumento.value;
                var chkContactNotification = document.getElementById("chkContactNotification");

                //Obtenemos las iniciales del numero de documento para validar en caso de RUC
                var prefixNumDocument = numDocument.substring(0, 2);

                //Realizamos las validaciones para mostrar o no la seccion de contactos
                var resValidation = fxValidateShowContacsSection(specificationId, dispatchPlaceId, typeDocument, prefixNumDocument);

                //Ocultamos o mostramos la seccion de contactos
                displayContactsSection(resValidation);

                if(resValidation){
                    //Actualizamos el valor de la validacion de la seccion de contactos
                    parent.mainFrame.showSectionContacts = 1;
                    chkContactNotification.checked = true;
                }
                else{
                    parent.mainFrame.showSectionContacts = 0;
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

            if(!validateDispatchPlace){
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
</script>
<!-- Fin Data -->


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
%>
<script DEFER>
    gIndexRejectIni=gIndexReject;
</script>

<table border="0" cellspacing="0" cellpadding="0" width="15%">
    <tr class="PortletHeaderColor">
        <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"></td>
        <td class="SubSectionTitle" align="left" valign="top">Orden&nbsp;<%=strOrderId0%></td>
        <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"></td>
    </tr>
</table>
<!--input type="hidden" name="hdnFlagLoadSolucion" value="0">
<input type="hidden" name="hdnSolucionIdAnt" value=""-->
<!-- Inicio Variable hidden -->
<input type="hidden" id="strAppId" name="strAppId" value="<%=strAppId%>">
<!-- Prueba Fin -->
<input type="hidden" name="hdnOrderId" value="<%=lOrderId0%>">
<input type="hidden" name="hdnUserName" value="<%=strLogin%>">
<input type="hidden" name="hdnNpBpelconversationid" value="<%=MiUtil.getString(objOrderBean.getNpBpelconversationid())%>">
<input type="hidden" name="hdnNpBpelinstanceid" value="<%=objOrderBean.getNpBpelinstanceid()%>">
<input type="hidden" name="hdnTimeStamp" value="<%=objOrderBean.getNpTimeStamp()%>">
<%System.out.println("TimeStamp Origen: "+objOrderBean.getNpTimeStamp());%>
<input type="hidden" name="txtNumeroGuia" value="<%=objOrderBean.getNpguidegenerated()%>">
<input type="hidden" name="hdnCurrencyEdit" value="">
<!--INI:Validacion de Propuestas:CBARZOLA 09/08/2009-->
<input type="hidden" name="hdnSellerId" value="<%=MiUtil.getString(objOrderBean.getNpProviderGrpId())%>">
<input type="hidden" name="hdnProposedId" value="<%=MiUtil.getString(objOrderBean.getNpproposedid())%>">
<input type="hidden" name="hdnTrama" value="">
<!--FIN:Validacion de Propuestas:CBARZOLA 09/08/2009-->

<!--INI:Validacion de Fecha de Proceso en Suspensiones:JHERRERA 02/11/2009-->
<input type="hidden" name="hdnFechaProcAut_old" value="<%=MiUtil.toFecha(objOrderBean.getNpScheduleDate())%>">
<input type="hidden" name="hdnFechaProcAut_new" value="">
<!--  -->
<input type="hidden" name="hdnPermisosEditFecha" value="<%=iPermisosEditFecha%>">
<input type="hidden" name="hdnFechaCreacionOrden" value="<%=MiUtil.toFecha(objOrderBean.getNpCreatedDate())%>">
<input type="hidden" name="hdnCantidadDiasCalendario" value="<%=iCantidadDiasCalendario%>">
<input type="hidden" name="hdnFechaFinalOriginal" value="<%=sFechaFinalOriginal%>">
<input type="hidden" name="hndGeneratortype" value="<%=strGeneratortype%>">
<!--FIN:Validacion de Fecha de Proceso en Suspensiones:JHERRERA 02/11/2009-->
<!--jsalazar - modif hpptt # 1 - 29/09/2010 - inicio-->
<input type="hidden" name="hdnSpecificationIdJ" value="<%=objOrderBean.getNpSpecificationId()%>">
<!--jsalazar - modif hpptt # 1 - 29/09/2010 - fin-->
<!--objOrderBean.getNpSpecificationId()-->
<input type="hidden" name="hdnSalesStructOrigenId" value="<%=objOrderBean.getSalesStructureOriginalId()%>"> <!-- SAR 0037-165858 MCB -->
<input type="hidden" name="hdnChkCourier" value="">
<!--JCASTILLO SECTION POPULATE CENTER-->
<input type="hidden" name="hdnFlgCPUF" value="">
<input type="hidden" name="hdnCpufResponse" value="">
<input type="hidden" name="hdnDisplay" value="">
<!--JCASTILLO SECTION DIGITALIZACION DE DOCUMENTOS-->
<input type="hidden" name="hdnFlgSDD" value="">
<input type="hidden" name="hdnEmailNullF" value="">
<input type="hidden" name="hdnSignatureType" value="">
<input type="hidden" name="hdnSignatureReason" value="">
<input type="hidden" name="hdnGenStatus" value="">
<input type="hidden" name="hdnTrxType" value="">
<input type="hidden" name="hdnFlgDocument" value="">
<input type="hidden" name="hdnImeiLoan" value="">
<!--SECTION AUTH CONTACT-->
<input type="hidden" name="hdnFlgSAC" value="">
<input type="hidden" name="hdnOrderProrrateo" value="<%=hdnOrderProrrateoValue%>"> <!-- PRY-0890 JCURI -->

<!--JVERGARA VERIFICACION DE IDENTIDAD AISLADA  -->
<input type="hidden" name="hdnFlgVIA" value="">

<input type="hidden" name="hdnChkAssignee" value="">
<input type="hidden" name="hdnChannelClient" value="">
<input type="hidden" name="hdnCmbDocTypeAssigneeVal" value="">
<input type="hidden" name="hdnCmbDocTypeAssigneeText" value="">
<input type="hidden" name="hdnUpdateDocGen" value="">
<input type="hidden" name="hdnDocAssigneeId" value="">
<input type="hidden" name="hdnDocGenId" value="">
<input type="hidden" name="hdnTransType" value="">

<!-- INICIO: PRY-1200 -->
<input type="hidden" name="hdnMontoOrdenVEP" value="<%=objOrderBean.getNpPaymentTotal()%>">
<input type="hidden" name="hdnCuotaInicialVEP" value="<%=MiUtil.getString(objOrderBean.getInitialQuota())%>">
<input type="hidden" name="hdnFormaPagoCIVEP" value="<%=MiUtil.getString(objOrderBean.getNpPaymentTermsIQ())%>">
<input type="hidden" name="hdnCantidadCuotasVEP" value="<%=MiUtil.getString(objOrderBean.getNpNumCuotas())%>">
<input type="hidden" name="hdnFlagVEP" value="<%=MiUtil.getString(objOrderBean.getNpFlagVep())%>">
<!-- FIN: PRY-1200 -->

<table  border="0" width="100%" cellpadding="2" cellspacing="1" class="RegionBorder">
    <tr>
        <td>
            <table  border="0" width="100%" cellpadding="2" cellspacing="1">
                <tr>
                    <td class="CellLabel" align="center" width="12%">&nbsp;<b>NRO. SOLICITUD</b></td>
                    <!--<td class="CellLabel" align="center" width="13%">Solución</td>-->
					<td class="CellLabel" align="center" width="13%">División</td> 
					<td class="CellLabel" align="center" colspan="2">Categoría</td>
					<td class="CellLabel" align="center" colspan="2">Sub Categoría</td>
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
                    <td class="CellContent" align="center" width="12%">&nbsp;
                        <%if ("Editable".equals(MiUtil.getString((String)hshScreenField.get("ordercode")))){%>
                        <input type="text" id="txtNumSolicitud" name="txtNumSolicitud" size="15" maxlength="15" value="<%=MiUtil.getString(objOrderBean.getNpOrderCode())%>">
                        <%}else{%>
                        <input type="text" id="txtNumSolicitud" name="txtNumSolicitud" size="15" style="border :0;  background-color:transparent;" readonly maxlength="15" value="<%=MiUtil.getString(objOrderBean.getNpOrderCode())%>">
                        <%}%>
                    </td>


                    <td class="CellContent" align="center" width="13%">&nbsp;<%=MiUtil.getString(objOrderBean.getNpDivisionName())%>
                        <input type="hidden" name="hdnDivisionName" value="<%=MiUtil.getString(objOrderBean.getNpDivisionName())%>">
                    </td>
                    <td class="CellContent" align="center" colspan="2">&nbsp;
                        <input type="text" name="txtCategoria" size="35" align="middle" style="border :0;  background-color:transparent;" readonly value="<%=MiUtil.getString(objOrderBean.getNpType())%>">
                        <input type="hidden" name="hdnFlagLoadCategoria" value="0">
                        <input type="hidden" name="hdnCategoriaIdAnt" value="">
                        <input type="hidden" name="hdnCategoriaId" value="<%=MiUtil.getString(objOrderBean.getNpType())%>">
                    </td>
                    <td class="CellContent" align="center" colspan="2">&nbsp;
                        <%=MiUtil.getString(objOrderBean.getNpSpecification())%> <!-- Nombre de la Subcategoria-->
                        <input type="hidden" name="hdnSubCategoriaIdAnt" value="">
                        <input type="hidden" name="hdnSubCategoria" value="<%=objOrderBean.getNpSpecificationId()%>"> <!-- to editReject -->
                        <input type="hidden" name="hdnProductType">

                        <script>
                            fxValidateSpecLst();

                        </script>
                    </td>

                    <%if(MiUtil.contentInArray(objOrderBean.getNpSpecificationId(),Constante.SPEC_REPAIRS)) { %>
                    <td class="CellContent" colspan=2 align="center">
                        <input type="hidden" name="txtVendedor"  size="30" maxlength="50" value="<%=MiUtil.getString(objOrderBean.getNpSalesmanName())%>">
                        &nbsp;&nbsp;&nbsp;

                        <!--START MSOTO: 08-04-2014 SAR N_O000019563 Se deshabilita el combo-->
                        <select name="cmbVendedor" style="display:none" onchange="javascript: document.frmdatos.txtVendedor.value=this.options[this.selectedIndex].text;fxChangeSalesMan(this,<%=iUserId%>,<%=iAppId%>);">
                        </select>
                        <script>
                            AddNewOption(document.frmdatos.cmbVendedor,"<%=objOrderBean.getNpProviderGrpId()%>","<%=objOrderBean.getNpSalesmanName()%>");
                            document.frmdatos.cmbVendedor.value="<%=objOrderBean.getNpProviderGrpId()%>";
                        </script>

                        <%
                            ////MSOTO: 08-04-2014 SAR N_O000019563 Para evitar que cargue todo el query de la lista cuando se deshabilita
                            if ( MiUtil.getString(strGeneratortype).equalsIgnoreCase("OPP") || MiUtil.getString(strGeneratortype).equalsIgnoreCase("OPP_PORTAB")  )
                                iFlagComboVendedor = 0;
                        %>

                        <%if ( iFlagComboVendedor == 0  || fechProcIgual){ // 0:No es modificable 1:Modificable//fechProcIgual true:No modificable //jsalazar hpptt  %>

                        <!-- MSOTO 03-06-2014 SAR N_O000022816 txtCmbVendedor reemplaza al cmbVendedor -->
                        <input type="text" name="txtCmbVendedor" style="width: 80%;" value="<%=MiUtil.getString(objOrderBean.getNpSalesmanName())%>" disabled="disabled" />
                        <script>
                            $(document).ready(function(){
                                $("#lblVendor").html("");
                                //$("#lblVendor").text("Vendedor");
                                $("#lblVendor").html("<label>Vendedor</label>");
                            });
                        </script>

                        <%}else{%>

                        <!--- MSOTO 03-06-2014 SAR N_O000022816 txtCmbVendedor reemplaza al cmbVendedor-->
                        <input type="text" name="txtCmbVendedor" style="width: 80%;" value="<%=MiUtil.getString(objOrderBean.getNpSalesmanName())%>" disabled="disabled" />
                        <script>
                            $(document).ready(function(){
                                $("#lblVendor").html("");
                                //$("#lblVendor").text("Vendedor");
                                //$("#lnkVendor").attr("href",'href="javascript:fxSearchCmbVendedor(0)"');
                                $("#lblVendor").html("<a href='javascript:fxSearchCmbVendedor(0)'>Vendedor</a>");
                            });
                        </script>

                        <%}%>

                        <input type="hidden" name="hdnVendedorAux">
                        <input type="hidden" name="hdnAlcanceExcluCC" value="wv_AlcanceExcluCC">
                        <input type="hidden" name="hdnAlcanceExcluAC" value="wv_AlcanceExcluAC">
                        <input type="hidden" name="hdnCustAlcanceExclusividad" value="">
                        <input type="hidden" name="hdnOrderCreator" value="<%=objOrderBean.getNpProviderGrpId()%>">
                        <input type="hidden" name="hdnVendedor" value="<%=objOrderBean.getNpProviderGrpId()%>">
                        <input type="hidden" name="hdnAppCode" value="<%=iAppId%>">

                    </td>

                    <%}else{%>
                    <td class="CellContent" colspan=2 align="center">
                        <input type="hidden" name="txtVendedor"  size="30" maxlength="50" value="<%=MiUtil.getString(objOrderBean.getNpSalesmanName())%>">
                        &nbsp;&nbsp;&nbsp;

                        <!--START MSOTO: 08-04-2014 SAR N_O000019563 Se deshabilita el combo-->
                        <select name="cmbVendedor" style="display:none" onchange="javascript: document.frmdatos.txtVendedor.value=this.options[this.selectedIndex].text;fxChangeSalesMan(this,<%=iUserId%>,<%=iAppId%>);">
                        </select>
                        <script>
                            AddNewOption(document.frmdatos.cmbVendedor,"<%=objOrderBean.getNpProviderGrpId()%>","<%=objOrderBean.getNpSalesmanName()%>");
                            document.frmdatos.cmbVendedor.value="<%=objOrderBean.getNpProviderGrpId()%>";
                        </script>

                        <%
                            ////MSOTO: 08-04-2014 SAR N_O000019563 Para evitar que cargue todo el query de la lista cuando se deshabilita
                            if ( MiUtil.getString(strGeneratortype).equalsIgnoreCase("OPP") || MiUtil.getString(strGeneratortype).equalsIgnoreCase("OPP_PORTAB")  )
                                iFlagComboVendedor = 0;
                        %>

                        <%if ( (iFlagComboVendedor == 0) || fechProcIgual ){ // 0:No es modificable 1:Modificable //fechProcIgual es true --> no es modificable //jsalazar hpptt%>

                        <!--- MSOTO 03-06-2014 SAR N_O000022816 txtCmbVendedor reemplaza al cmbVendedor-->
                        <input type="text" name="txtCmbVendedor" style="width: 80%;" value="<%=MiUtil.getString(objOrderBean.getNpSalesmanName())%>" disabled="disabled" />
                        <script>
                            $(document).ready(function(){
                                $("#lblVendor").html("");
                                //$("#lblVendor").text("Vendedor");
                                $("#lblVendor").html("<label>Vendedor</label>");
                            });
                        </script>

                        <%}else{%>
                        <%if ( strLoadSellerData.equalsIgnoreCase("1") && arrSalesDataList!=null ){%>
                        <!--- MSOTO 03-06-2014 SAR N_O000022816 txtCmbVendedor reemplaza al cmbVendedor-->
                        <input type="text" name="txtCmbVendedor" style="width: 80%;" value="<%=MiUtil.getString(objOrderBean.getNpSalesmanName())%>" disabled="disabled" />
                        <script>
                            $(document).ready(function(){
                                $("#lblVendor").html("");
                                //$("#lblVendor").text("Vendedor");
                                //$("#lnkVendor").attr("href",'href="javascript:fxSearchCmbVendedor(1)"');
                                $("#lblVendor").html("<a href='javascript:fxSearchCmbVendedor(1)'>Vendedor</a>");
                            });
                        </script>

                        <%}else{%>
                        <!--- MSOTO 03-06-2014 SAR N_O000022816 txtCmbVendedor reemplaza al cmbVendedor-->
                        <input type="text" name="txtCmbVendedor" style="width: 80%;" value="<%=MiUtil.getString(objOrderBean.getNpSalesmanName())%>" disabled="disabled" />
                        <script>
                            $(document).ready(function(){
                                $("#lblVendor").html("");
                                //$("#lblVendor").text("Vendedor");
                                //$("#lnkVendor").attr("href",'href="javascript:fxSearchCmbVendedor(0)"');
                                $("#lblVendor").html("<a href='javascript:fxSearchCmbVendedor(0)'>Vendedor</a>");
                            });
                        </script>

                        <%}%>

                        <%}%>

                        <input type="hidden" name="hdnVendedorAux">
                        <input type="hidden" name="hdnAlcanceExcluCC" value="wv_AlcanceExcluCC">
                        <input type="hidden" name="hdnAlcanceExcluAC" value="wv_AlcanceExcluAC">
                        <input type="hidden" name="hdnCustAlcanceExclusividad" value="">
                        <input type="hidden" name="hdnOrderCreator" value="<%=objOrderBean.getNpProviderGrpId()%>">
                        <input type="hidden" name="hdnVendedor" value="<%=objOrderBean.getNpProviderGrpId()%>">

                    </td>

                    <%}%>

                    <!-- Inicio Data -->
                    <input type="hidden" name="hdncmbVendedorData" value="">
                    <input type="hidden" name="hdnVendedorDataId" value="">
                    <input type="hidden" name="hdnDataFieldsVisibles" value="">
                    <%=strDataSalesmanField%>

                    <script defer>
                        var form      = parent.mainFrame.frmdatos;
                        if ("<%=strShowDataFields%>" == "1"){
                            fxShowDataFields();
                            <%
                            HashMap hsh=null;
                            String strDatoValue=null;
                            String strDatoText=null;
                            String strSalesTeamId = String.valueOf(objOrderBean.getNpProviderGrpId());
                            String strSalesmanData = String.valueOf(objOrderBean.getNpProviderGrpIdData());

                            if (arrSalesDataList != null){
                             for (int i=0; i< arrSalesDataList.size(); i++){
                                hsh = (HashMap)arrSalesDataList.get(i);
                                strDatoValue= (String)hsh.get("npprovidergrpid");
                                strDatoText= (String)hsh.get("swname");
                                if (strDatoValue!=null && !"".equals(strDatoValue)){
                                  %>AddNewOption(form.cmbVendedorData,"<%=strDatoValue%>","<%=strDatoText%>");<%
                        }
                     }

                     if (!strSalesmanData.equalsIgnoreCase("")){
                        %>
                            SetCmbDefaultValue(form.cmbVendedorData,"<%=strSalesmanData%>");
                            form.hdnVendedorDataId.value = "<%=strSalesmanData%>";
                            <%
                         }
                         if ( (!strFlagexclusivity.equalsIgnoreCase("N"))||fechProcIgual ){// si fechProcIgual es true no se puede modificar //jsalazar hpptt
                            //Tiene exclusividad. Se bloquea el combo
                            %>document.frmdatos.cmbVendedorData.disabled=true;<%
                     }
                    }
                    if ((!"Editable".equals(MiUtil.getString((String)hshScreenField.get("salesmandata"))))|| fechProcIgual ){ //jsalazar hpptt
                      %>document.frmdatos.cmbVendedorData.disabled=true;<%
                    }

                    %>
                        }else{
                            DeleteSelectOptions(form.cmbVendedorData);
                            fxHideDataFields();
                        }
                    </script>


                    <!-- Fin Data -->

                </tr>
                <!-- Segundo grupo -->
                <tr>
                    <td class="CellLabel"   align="left" width="12%">Estado</td>
                    <td class="CellContent" align="left" width="13%">&nbsp;<%=MiUtil.getString(objOrderBean.getNpStatus())%>  <!-- Nombre del estado -->
                        <input type="hidden" id="txtEstadoOrden" name="txtEstadoOrden" value="<%=MiUtil.getString(objOrderBean.getNpStatus())%>"> <!-- Nombre del estado -->
                    </td>


                    <td class="CellLabel"   align="left" width="12%">Tienda</td>
                    <%if (iSpecificationId == Constante.SPEC_SERVCIO_TECNICO_ACCESORIOS || iSpecificationId == Constante.SPEC_SERVCIO_TECNICO_CASORAPIDO || iSpecificationId == Constante.SPEC_SERVCIO_TECNICO_REEMPLAZO || iSpecificationId == Constante.SPEC_SERVCIO_TECNICO_REPARACION ){
                        strBuildingId = MiUtil.getString(objOrderBean.getNpBuildingId());
                    %>
                    <td class="CellContent" align="left" width="8%"><input type="hidden" name="hdnTiendaId"><select name="cmbTienda" />
                        <option value=""></option>
                        <%	hshDataMap = objGeneralService.getBuildingList(Constante.BUILDING_TIPO_FISICA);
                            valoresCombo = (ArrayList) hshDataMap.get("arrBuildingList");
                            strMessage = (String) hshDataMap.get("strMessage");
                            if (strMessage==null){
                                for(int i=0;i<valoresCombo.size();i++){
                                    DominioBean dominio = (DominioBean) valoresCombo.get(i);%>
                        <option value='<%=dominio.getValor()%>'><%=dominio.getDescripcion()%></option>
                            <%}
                    }%></select>
                        <script>
                            document.frmdatos.cmbTienda.value="<%=strBuildingId%>";
                        </script></td><%
                } else{%>
                    <td class="CellContent" align="left" width="4%">&nbsp;<%=strTienda%></td>
                    <input type="hidden" name="hdnTiendaId" value="<%=strBuildingId%>">
                    <%}%>
                    <td class="CellLabel"   align="left" width="7%">Region Tr&aacute;mite</td>
                    <td class="CellContent" align="left" width="6%">&nbsp;<%=strRegionTramite%></td>
                    <td class="CellLabel"   align="left" width="3%">Dealer</td>
                    <td class="CellContent" align="left" >
                        <!--&nbsp;<%=MiUtil.getString(objOrderBean.getNpDealerName())%>-->
                        &nbsp;<input type="text" name="txtDealer" size="12" value="<%=MiUtil.getString(objOrderBean.getNpDealerName())%>" maxlength="20" readonly>
                        <input type="hidden" name="hdnDealer" value="">
                    </td>
                    <!-- Inicio Data -->
                    <td class="CellContent">&nbsp;</td>
                    <!-- Fin Data -->
                </tr>
                <% //inicio de seccion que se muestra si y solo si no una reparaci?n
                    if(!MiUtil.contentInArray(objOrderBean.getNpSpecificationId(),Constante.SPEC_REPAIRS)){

                        //
                %>
                <tr>
                    <td class="CellLabel" align="left" width="12%">
                        <%if ("Editable".equals(MiUtil.getString((String)hshScreenField.get("signdate")))){%>
                        <a href="javascript:fxMuestraHelpFechaHora();">
                            <%}%>
                            Fecha Hora Firma</a></td>
                    <td class="CellContent" width="13%">&nbsp;
                        <input type="hidden" name="hdnFechaHoraFirma" value="<%=MiUtil.toFechaHora(objOrderBean.getNpSignDate())%>">
                        <%if ("Editable".equals(MiUtil.getString((String)hshScreenField.get("signdate")))){%>
                        <input type="text" name="txtFechaHoraFirma" size="18" maxlength="18" value="<%=MiUtil.toFechaHora(objOrderBean.getNpSignDate())%>" onblur="frmdatos.hdnFechaHoraFirma.value=this.value">
                        <%}else{%>
                        <%=MiUtil.toFechaHora(objOrderBean.getNpSignDate())%>
                        <%}%>
                    </td>
                    <% if(flagShowReasonCdm) {%>
                    <td class="CellLabel" align="left" width="12%">Motivo de Cambio de Modelo&nbsp;</td>
                    <td class="CellContent" width="8%">&nbsp;<%=objOrderBean.getNpReasonCdmName()%></td>
                    <%}else{%>                    

				<% if(objOrderBean.getNpSpecificationId()== Constante.SPEC_POSTPAGO_VENTA && Constante.TDE_SWITCH_ON.equals(strTDESwitch )) { %>
                                         <td class="CellContent" align="center" width="20%">
						<!-- TDECONV003 - KOTINIANO || switch tde-->
						<div id="divFlagMigration" style="display: <% if(objOrderBean.getNpSpecificationId()== Constante.SPEC_POSTPAGO_VENTA && Constante.TDE_SWITCH_ON.equals(strTDESwitch)) { %> block <%}else{%>none <%}%>">
                        <span  style="font-weight: bold;color: #000000; padding: 3px 5px; margin-right: 5px;"> Migración Prepago a Postpago</span>
							<input type="checkbox" name="chkFlagMigration" onclick="fxChangeFlagMigration(this);"  <% if(objOrderBean.getNpFlagMigracion()!= null && (objOrderBean.getNpFlagMigracion().equals("S") || objOrderBean.getNpFlagMigracion().equals("C"))){ %> checked="checked" disabled="disabled"<%} %>/>
							<input type="hidden" name="hdnFlagMigration" <% if(objOrderBean.getNpFlagMigracion()!= null) { %> value="<%= objOrderBean.getNpFlagMigracion()%>" <%}else{%> value="N" <%}%>/>
                        </div>
                </td>
                <%}else{%>
					 <td class="CellContent" align="center" width="12%">&nbsp;</td>
                                         <td class="CellContent" width="8%">&nbsp;</td>
				<%}%>
                    <%}%>
                    <td class="CellLabel" align="left" width="4%">Creado Por</td>
                    <td class="CellContent" width="7%">&nbsp;<%=MiUtil.getString(objOrderBean.getNpCreatedBy())%></td>
                    <input type="hidden" name="hdnCreateBy" value="<%=MiUtil.getString(objOrderBean.getNpCreatedBy())%>">
                    <td class="CellLabel" align="left" width="6%">Fecha Creaci&oacute;n</td>
                    <td class="CellContent" width="3%">&nbsp;<%=MiUtil.toFecha(objOrderBean.getNpCreatedDate())%> </td>
                    <!-- Inicio Data -->
                    <td class="CellContent">&nbsp;</td>
                    <!-- Fin Data -->
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
                        <td class="CellContent">&nbsp;</td>
                    </tr>
                    <%}%>
                <!-- Cuarto grupo -->
                <tr >
                    <td class="CellLabel" align="left" colspan="9" >&nbsp;Descripci?n&nbsp;
                        <%if (iNoteCount.intValue() != 0){
                        %><font color="red"><b>(Ver Notas)</b></font><%
                        } else {
                        %>&nbsp;<%
                            }%>
                    </td>
                </tr>
                <tr>
                    <td class="CellContent" colspan="8">&nbsp;
                        <textarea name="txtDetalle" cols="145" rows="4"><%=MiUtil.getString(objOrderBean.getNpDescription())%></textarea></td>
                    <%if( fechProcIgual ){// fechProcIgual: true no se modifica el campo jsalazar hpptt  %>
                    <script>
                        document.frmdatos.txtDetalle.disabled=true;
                    </script>
                    <%}%>
                    <!-- Inicio Data -->
                    <td class="CellContent">&nbsp;</td>
                    <!-- Fin Data -->
                </tr>

                                <tr >
                    <td width="100%" colspan="8" height="10"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/pobtrans.gif" border="0" height="6"></td>
                </tr>
                <tr>
                    <td class="CellLabel" align="left" width="12%">Lugar Despacho</td>
                    <td class="CellContent" width="13%">&nbsp;
                  <!--DERAZO REQ-0940-->
                  <!--JCURI PRY-1093-->
                  <select name="cmbLugarAtencion" style="width: 85%;" onchange="javascript:fxSelectSigEstado(this.value);fxDynamicContactSection(this.value);fxShowCarrier(this);">
                            <option value=""></option>
                            <%
                                //hshOrder = objOrderService.getDispatchPlaceList(2001);
                                hshOrder = objOrderService.getDispatchPlaceList(objOrderBean.getNpSpecificationId());
                                strMessage0=(String)hshOrder.get("strMessage");
                                if (strMessage0!=null)
                                    throw new Exception(strMessage0);

                                arrListado=(ArrayList)hshOrder.get("arrData");

                                HashMap map=null;
                                String strShortNameDP=null, strBuildingIdDP=null, strWorkflowTypeDP=null,strTypeDP=null;
                                for(int i=0;i< arrListado.size();i++){
                                    map=(HashMap)arrListado.get(i);
                                    strShortNameDP=MiUtil.getString((String)map.get("wv_npShortName"));
                                    strBuildingIdDP=MiUtil.getString((String)map.get("wn_npBuildingId"));
                                    strWorkflowTypeDP=MiUtil.getString((String)map.get("npWorkFlowType"));
                                    strTypeDP=MiUtil.getString((String)map.get("npType"));
                            %>
                            <option value="<%=strBuildingIdDP%>"><%=strShortNameDP%></option>
                            <script>
                                workflowArr[<%=i%>]= new fxMakeWorkflow('<%=strBuildingIdDP%>','<%=strWorkflowTypeDP%>','<%=strTypeDP%>');
                            </script>
                            <%}%>
                        </select>
                        <script>
                            document.frmdatos.cmbLugarAtencion.value="<%=objOrderBean.getNpDispatchPlaceId()%>";
                        </script>
                        <%if ( MiUtil.getString(strGeneratortype).equalsIgnoreCase("OPP")){%>
                        <%if((objOrderBean.getNpSpecificationId() == 2068 || objOrderBean.getNpSpecificationId() == 2069 || objOrderBean.getNpSpecificationId() == 2001 || objOrderBean.getNpSpecificationId() == 2002 
                        || objOrderBean.getNpSpecificationId() == Constante.SPEC_PREPAGO_TDE
                        || objOrderBean.getNpSpecificationId() == Constante.SPEC_REPOSICION_PREPAGO_TDE)){%>
                        <script>
                            document.frmdatos.cmbLugarAtencion.disabled=false;
                        </script>
                        <%}%>

                        <%}else{%>
                        <%if ((!"Editable".equals(MiUtil.getString((String)hshScreenField.get("dispatchplace"))))||fechProcIgual){//jsalazar hpptt %>
                        <script>
                            document.frmdatos.cmbLugarAtencion.disabled=true;
                        </script>
                        <%}%>
                        <!--DLAZO-->
                        <%if((objOrderBean.getNpSpecificationId() == 2068 || objOrderBean.getNpSpecificationId() == 2069)){%>
                        <script>
                            document.frmdatos.cmbLugarAtencion.disabled=true;
                        </script>
                        <%}%>

                        <%}%>


                        <!--DLAZO-->
                        <input type="hidden" name="hdnLugarDespachoAux">
                        <input type="hidden" name="hdnLugarDespachoTypeOrig" value="">
                        <input type="hidden" name="hdnRejection" value="">
                        <input type="hidden" name="hdnSign" value="">
                        <input type="hidden" name="hdnLugarDespachoType" value="">
                        <input type="hidden" name="hdnLugarDespacho" value="<%=objOrderBean.getNpDispatchPlaceId()%>">

                        <!--INICIO RPASCACIO N_SD000246338-->
                        <input type="hidden" name="hdnLugarDespacho2" value="<%=objOrderBean.getNpDispatchPlaceId()%>">
                        <!--FIN RPASCACIO N_SD000246338-->


                        <input type="hidden" name="hdnWorkflowType" value="<%=objOrderBean.getNpWorkFlowId()%>"> <!-- to editReject -->

                    </td>
                    <!--[Despacho en Tienda] PCASTILLO MONTOYA -->
               <!--PRY-1093 JCURI (isDeliverySpecification)-->
               <%if (objOrderService.showCourier(objOrderBean.getNpSpecificationId()) || isDeliverySpecification) {%>
                    <td class="CellLabel" align="left" width="12%">Entrega por Courier</td>
                    <% if (iCourier == 1){
                        if (iShowCourier == 1){%>
                         	<%//JCURI PRY-1093 %>
                          <td class="CellContent"><input type="checkbox" name="chkCourier" value ="1" checked onclick="fxSetValueCourier(this);fxChkCourierShowContact(this);fxShowCarrierChk();"></td>
                    <% } else {%>
                    <td class="CellContent"><input type="checkbox" name="chkCourier" value ="1" disabled="disabled" checked ></td>
                    <% }
                    } else {
                        if (iShowCourier == 1){ %>
                          <%//JCURI PRY-1093 %>
                          <td class="CellContent"><input type="checkbox" name="chkCourier" value ="0" onclick="fxSetValueCourier(this);fxChkCourierShowContact(this);fxShowCarrierChk();"></td>
                    <% } else { %>
                    <td class="CellContent"><input type="checkbox" name="chkCourier" value ="0" disabled="disabled"></td>
                    <% }
                    }
                    }%>
                    <td class="CellLabel"   align="left" width="12%">Representante CC</td>
                    <td class="CellContent" align="left" width="8%">
                        <input type="hidden" name="hdnRepresentanteCC" value="">

                        <%
                            //MSOTO: 08-04-2014 SAR N_O000019563 Para evitar que cargue todo el query de la lista cuando se deshabilita
                            int intFlagReppCall = 0;
                            if( MiUtil.getString(objOrderBean.getNpStatus()).equals(Constante.INBOX_CALLCENTER) ){
                                HashMap hshUserRolCallCenterSuperv = null;
                                hshUserRolCallCenterSuperv = objGeneralService.getRol(Constante.SCRN_OPTTO_REP_CALLCENTER, iUserId, iAppId);
                                strMessage=(String)hshUserRolCallCenterSuperv.get("strMessage");
                                if (strMessage!=null)
                                    throw new Exception(strMessage);

                                int intFlagCallSuperv  = MiUtil.parseInt((String)hshUserRolCallCenterSuperv.get("iRetorno"));
                                System.out.println("Tiene Permiso : " + intFlagCallSuperv);

                                if( intFlagCallSuperv == 1 )
                                    intFlagReppCall = 1;

                            }else if ("Editable".equals(MiUtil.getString((String)hshScreenField.get("callcenteruser")))){
                                intFlagReppCall = 1;
                            }
                        %>

                        <%if( (intFlagReppCall == 0)||fechProcIgual ){// fechProcIgual: true no se modifica el campo jsalazar hpptt  %>
                        <select name="cmbRepresentanteCC">
                        </select>

                        <script>
                            AddNewOption(document.frmdatos.cmbRepresentanteCC,"<%=MiUtil.getString(objOrderBean.getNpUserName1())%>","<%=MiUtil.getString(objOrderBean.getNpUserName1())%>");
                        </script>

                        <script>
                            document.frmdatos.cmbRepresentanteCC.value="<%=MiUtil.getString(objOrderBean.getNpUserName1())%>";
                        </script>

                        <script>
                            document.frmdatos.cmbRepresentanteCC.disabled=true;
                        </script>

                        <%}else{%>
                        <select name="cmbRepresentanteCC">
                            <%  hshData = objGeneralService.getRepresentantesCCList();
                                strMessage0 = (String)hshData.get("strMessage");
                                if (strMessage0!=null)
                                    throw new Exception(strMessage0);
                                arrListado=(ArrayList)hshData.get("arrRepreCCList");
                            %>
                            <%=MiUtil.buildCombo(arrListado,"wn_dealer","wn_dealer")%>
                        </select>
                        <script>
                            document.frmdatos.cmbRepresentanteCC.value="<%=MiUtil.getString(objOrderBean.getNpUserName1())%>";
                        </script>

                        <%}%>

                    </td>
                    <%if(MiUtil.contentInArray(objOrderBean.getNpSpecificationId(),Constante.SPEC_BAG_DATE)){%>
                    <td class="CellLabel" align="left" width="14%">Fecha&nbsp;Proc.&nbsp;Aut.</td>
                    <td class="CellContent"  width="14%">&nbsp;
                        <input type="hidden" name="hdnFechaProceso" value="<%=MiUtil.toFecha(objOrderBean.getNpScheduleDate())%>">
                        <!-- HTENORIO COR1108 Hidden para hacer la comparacion con la fecha que se ingresa-->
                        <input type="hidden" name="hdnFechaProcesoAux" value="<%=MiUtil.toFecha(objOrderBean.getNpScheduleDate())%>">
                        <input type="text" name="txtFechaProceso" size="10" readonly value="<%=MiUtil.toFecha(objOrderBean.getNpScheduleDate())%>" maxlength="10" onblur="frmdatos.hdnFechaProceso.value=this.value">
                        &nbsp;&nbsp;&nbsp;
                    </td>
                    <%}else if(objOrderBean.getNpSpecificationId()== Constante.SPEC_SSAA_PROMOTIONS || objOrderBean.getNpSpecificationId() == Constante.SPEC_SSAA_SUSCRIPCIONES){
                        if(MiUtil.getString(objOrderBean.getNpStatus()).equals(Constante.INBOX_CALLCENTER)){%>
                    <td class="CellLabel" align="left" width="14%"><a href="javascript:show_calendar('frmdatos.txtFechaProceso',null,null,'DD/MM/YYYY');">
                        Fecha&nbsp;Proc.&nbsp;Aut.</a></td>
                    <td class="CellContent"  width="14%">&nbsp;
                        <input type="hidden" name="hdnFechaProceso" value="<%=MiUtil.toFecha(objOrderBean.getNpScheduleDate())%>">
                        <!-- HTENORIO COR1108 Hidden para hacer la comparacion con la fecha que se ingresa-->
                        <input type="hidden" name="hdnFechaProcesoAux" value="<%=MiUtil.toFecha(objOrderBean.getNpScheduleDate())%>">
                        <input type="text" name="txtFechaProceso" size="10" value="<%=MiUtil.toFecha(objOrderBean.getNpScheduleDate())%>" maxlength="10" onblur="frmdatos.hdnFechaProceso.value=this.value">
                        &nbsp;&nbsp;&nbsp;
                    </td>
                    <%}else{%>
                    <td class="CellLabel" align="left" width="14%">Fecha&nbsp;Proc.&nbsp;Aut.</td>
                    <td class="CellContent"  width="14%">&nbsp;
                        <input type="hidden" name="hdnFechaProceso" value="<%=MiUtil.toFecha(objOrderBean.getNpScheduleDate())%>">
                        <!-- HTENORIO COR1108 Hidden para hacer la comparacion con la fecha que se ingresa-->
                        <input type="hidden" name="hdnFechaProcesoAux" value="<%=MiUtil.toFecha(objOrderBean.getNpScheduleDate())%>">
                        <input type="text" name="txtFechaProceso" size="10" readonly value="<%=MiUtil.toFecha(objOrderBean.getNpScheduleDate())%>" maxlength="10" onblur="frmdatos.hdnFechaProceso.value=this.value">
                        &nbsp;&nbsp;&nbsp;
                    </td>
                    <%}%>
                    <%}else{
                        if (("Editable".equals(MiUtil.getString((String)hshScreenField.get("scheduledate"))))&& fechProcMen){%>  <!--jsalazar - modif hpptt # 1 - 29/09/2010 -->
                    <td class="CellLabel" align="left" >
                        <a href="javascript:show_calendar('frmdatos.txtFechaProceso',null,null,'DD/MM/YYYY');">
                            Fecha&nbsp;Proc.&nbsp;Aut.</a></td>
                    <%}else{%>
                    <td class="CellLabel" align="left" width="13%">Fecha&nbsp;Proc.&nbsp;Aut.</td>
                    <%}%>
                    <td class="CellContent">&nbsp;
                        <input type="hidden" name="hdnFechaProceso" value="<%=MiUtil.toFecha(objOrderBean.getNpScheduleDate())%>">
                        <%if (("Editable".equals(MiUtil.getString((String)hshScreenField.get("scheduledate")))) && fechProcMen){%> <!--jsalazar - modif hpptt # 1 - 29/09/2010 -->
                        <input type="text" name="txtFechaProceso" size="10" value="<%=MiUtil.toFecha(objOrderBean.getNpScheduleDate())%>" maxlength="10" onblur="frmdatos.hdnFechaProceso.value=this.value; <%if ( objOrderBean.getNpSpecificationId() == (Constante.SPEC_SUSPENSIONES[0]) ){%>calcularFechaReconex();fxValidateFecha(this);<%}%>;  <%if ( objOrderBean.getNpSpecificationId() == (Constante.SPEC_SUSPENSIONES[0]) ){%>fxValidateDiasEdit(<%=strOrderId0%>)<%}%>;
                            <%if ( objOrderBean.getNpSpecificationId() == (Constante.SPEC_SUSPENSIONES[2]) ){%>fxValidateRolDiasEdit()<%}%>
                            <%if ( objOrderBean.getNpSpecificationId() == (Constante.SPEC_SERVICIOS_ADICIONALES[0]) ){%>fxValidateFechaInicio(this);<%}%>";
                        >
                        &nbsp;&nbsp;&nbsp;
                        <%}else{%>
                        <%=MiUtil.toFecha(objOrderBean.getNpScheduleDate())%>
                        <%}%>
                    </td>

                    <%if (objOrderBean.getNpSpecificationId()== Constante.SPEC_SUSPENSIONES[0]){%> <!--jsalazar - modif hpptt # 1 - 29/09/2010 -->
                    <%
                        if ("Editable".equals(MiUtil.getString((String)hshScreenField.get("scheduledate")))){%>
                    <td class="CellLabel" align="center"><DIV id=dvFecReconexEdit style=display:'none'><a href="javascript:show_calendar('frmdatos.txtFechaReconexion',null,null,'DD/MM/YYYY');" name="linkCalendar">Fecha&nbsp;Proc.&nbsp;Reconex.</a></DIV></td>
                    <%}else{%>
                    <td class="CellLabel" align="left" width="3%"><DIV id=dvFecReconexEdit style=display:'none'>Fecha&nbsp;Proc.&nbsp;Reconex.</DIV></td>
                    <%}%>
                    <td class="CellContent"><DIV id="dvFecReconexEditInput" style=display:'none'>&nbsp;
                        <input type="hidden" name="hdnFechaReconeccion" value="<%=MiUtil.toFecha(objOrderBean.getNpScheduleDate2())%>">
                        <%if ("Editable".equals(MiUtil.getString((String)hshScreenField.get("scheduledate")))){%>
                        <input type="text" name="txtFechaReconexion" size="10" value="<%=MiUtil.toFecha(objOrderBean.getNpScheduleDate2())%>" maxlength="10" onblur="frmdatos.hdnFechaReconeccion.value=this.value; <%if ( objOrderBean.getNpSpecificationId() == (Constante.SPEC_SUSPENSIONES[0]) ){%>calcularFechaReconex();fxValidateFecha(this);<%}%> ; <%if ( objOrderBean.getNpSpecificationId() == (Constante.SPEC_SUSPENSIONES[0]) ){%>fxValidateDiasEdit(<%=strOrderId0%>)<%}%>">
                        &nbsp;&nbsp;&nbsp;
                        <%}else{%>
                        <%=MiUtil.toFecha(objOrderBean.getNpScheduleDate2())%>
                        <%}%>
                    </DIV></td>
                    <%}%><!--jsalazar - modif hpptt # 1 - 29/09/2010 -->
                    <!--jsalazar - modif hpptt # 1 - 29/09/2010 - inicio-->
                    <%if (objOrderBean.getNpSpecificationId()== Constante.SPEC_SERVICIOS_ADICIONALES[0]){%>
                    <% if (("Editable".equals(MiUtil.getString((String)hshScreenField.get("scheduledate")))) && fechProcMen ){%> <!--jsalazar modif hpptt # 1 - 29/09/2010-->
                    <td class="CellLabel" align="left"><DIV id="dvFechaFinProgEdit" style=display:'none'><a href="javascript:show_calendar('frmdatos.txtFechaReconexion',null,null,'DD/MM/YYYY');" name="linkCalendar">Fecha&nbsp;Fin&nbsp;Prog.</a></DIV></td>
                    <%}else{%>
                    <td class="CellLabel" align="left" width="3%"><DIV id="dvFechaFinProgEdit" style=display:'none'>Fecha&nbsp;Fin&nbsp;Prog.</DIV></td>
                    <%}%>
                    <td class="CellContent"><DIV id="dvFechaFinProgEditInput" style=display:'none'>&nbsp;
                        <input type="hidden" name="hdnFechaFinProg" value="<%=MiUtil.toFecha(objOrderBean.getNpScheduleDate2())%>">
                        <%if (("Editable".equals(MiUtil.getString((String)hshScreenField.get("scheduledate")))) && fechProcMen){%> <!--jsalazar modif hpptt # 1 - 29/09/2010-->
                        <input type="text" name="txtFechaReconexion" size="10" value="<%=MiUtil.toFecha(objOrderBean.getNpScheduleDate2())%>" maxlength="10" onblur="frmdatos.hdnFechaFinProg.value=this.value; <%if ( objOrderBean.getNpSpecificationId() == (Constante.SPEC_SERVICIOS_ADICIONALES[0]) ){%>fxValidateFechaFin(this);<%}%> ; ">
                        &nbsp;&nbsp;&nbsp;
                        <%}else{%>
                        <%=MiUtil.toFecha(objOrderBean.getNpScheduleDate2())%>
                        <%}%>
                    </DIV></td>
                    <%}%>
                    <!--jsalazar - modif hpptt # 1 - 29/09/2010 - fin-->
                    <%}%>

                    <%if (objOrderBean.getNpSpecificationId()== Constante.SPEC_SUSPENSIONES[0]){ //Se agrego llamada a metodo Javascript para validar el mostrado o no del Div de fecha de reconexion%>
                    <script type="text/javascript">
                        MuestraFechaReconexBySpecification("<%=objOrderBean.getNpSpecificationId()%>");
                    </script>
                    <%}%>

                    <!--jsalazar - modif hpptt # 1 - 29/09/2010 - inicio-->
                    <%if (objOrderBean.getNpSpecificationId()== Constante.SPEC_SERVICIOS_ADICIONALES[0]){%>
                    <script type="text/javascript">
                        MuestraFechaProgFin("<%=objOrderBean.getNpSpecificationId()%>");
                    </script>
                    <%}%>
                    <!--jsalazar - modif hpptt # 1 - 29/09/2010 - fin-->
                    <!-- Inicio Data -->
                    <td class="CellContent">&nbsp;</td>
                    <!-- Fin Data -->
                </tr>
                <%} // fin de seccion que se muestra si y solo si no una reparaci?n
                %>
                <tr>
                    <td class="CellLabel" align="left" width="12%">Forma de Pago</td>
                    <td class="CellContent" width="13%">&nbsp;
                        <select id="cmbFormaPago" name="cmbFormaPago" style="width: 90%;" onclick="fxSaveIndexComboFormaPago(this);"  onchange="javascript:fxShowPayment1();fxValidateFormaPagoVEP(this);"> <%-- onchange="javascript:fxValidarFormaPago(this);" --%>
                            <% //JPEREZ -- 14/04/2008
                                System.out.println("[iFlagPayForms3]"+iFlagPayForms);
                                if (  (iFlagPayForms==1) && ("Editable".equals(MiUtil.getString((String)hshScreenField.get("paymentterms")))) ) {
                                    hshData = objOrderService.getPayFormList(objOrderBean.getNpSpecificationId(),objOrderBean.getCsbCustomer().getSwCustomerId());
                                    strMessage0=(String)hshData.get("strMessage");
                                    if (strMessage0!=null)
                                        throw new Exception(strMessage0);
                                    arrListado=(ArrayList)hshData.get("objListado");
                            %>
                            <%=MiUtil.buildCombo(arrListado,"value","descripcion")%>
                            <%}else{%>
                            <option value="<%=MiUtil.getString(objOrderBean.getNpPaymentTerms())%>">  <!-- Tipo Pago -->
                                <%=MiUtil.getString(objOrderBean.getNpPaymentTerms())%> <!-- Tipo Pago -->
                            </option>
                            <script DEFER>
                                document.frmdatos.cmbFormaPago.disabled = true;
                            </script>
                            <%}%>
                        </select>
                        <script defer>
                            document.frmdatos.cmbFormaPago.value = "<%=objOrderBean.getNpPaymentTerms()%>";
                        </script>
                        <input type="hidden" name="hdnFormaPagoAux">
                        <input type="hidden" name="hdnFormaPagoOrig" value="<%=MiUtil.getString(objOrderBean.getNpPaymentTerms())%>"> <!-- Tipo Pago -->
                        <input type="hidden" name="hdnFormaPago" value="">

                        <!--INICIO RPASCACIO N_SD000246338-->
                        <input type="hidden" name="hdnFormaPago2" value="<%=objOrderBean.getNpPaymentTerms()%>">
                        <!--FIN RPASCACIO N_SD000246338-->


                        <input type="hidden" name="hdnOrdenPagoAnular" value="">
                        <script defer>
                            document.frmdatos.cmbFormaPago.value = "<%=MiUtil.getString(objOrderBean.getNpPaymentTerms())%>";
                        </script>
                    </td>
                    <!--jsalazar hpptt inicio -servicios adicionales -->
                    <%if (fechProcIgual){ //fechProcIgual true:No modificable%>
                    <script>
                        document.frmdatos.cmbFormaPago.disabled=true;
                    </script>
                    <%}%>
                    <!-- jsalazar hpptt fin - servicios adicionales -->
                    <td class="CellLabel" align="left" width="12%">
                        <%if ("Editable".equals(MiUtil.getString((String)hshScreenField.get("paymentfuturedate")))){%>
                        <a href="javascript:show_calendar('frmdatos.txtFechaProbablePago',null,null,'DD/MM/YYYY');">
                            <%}%>
                            Fecha Prob. Pago
                            <%if ("Editable".equals(MiUtil.getString((String)hshScreenField.get("paymentfuturedate")))){%>
                        </a>
                        <%}%>
                    </td>
                    <td class="CellContent" align="left" width="8%">&nbsp;<!--Fecha del proximo pago-->
                        <!--jsalazar hpptt inicio -->
                        <%if(fechProcIgual){%>
                        <input type="text" name="txtFechaProbablePago" style="border :0; background-color: transparent;" readonly size="10" value="<%=MiUtil.toFecha(objOrderBean.getNpPaymentFutureDate())%>" maxlength="10">
                        <%
                        }else{
                            if ("Editable".equals(MiUtil.getString((String)hshScreenField.get("paymentfuturedate")))){%>
                        <input type="text" name="txtFechaProbablePago" size="10" value="<%=MiUtil.toFecha(objOrderBean.getNpPaymentFutureDate())%>" maxlength="10" onBlur="fxCheckDate(this)">
                        <%}else{%>
                        <input type="text" name="txtFechaProbablePago" style="border :0; background-color: transparent;" readonly size="10" value="<%=MiUtil.toFecha(objOrderBean.getNpPaymentFutureDate())%>" maxlength="10">
                        <%}
                        }%>
                        <!--jsalazar hpptt fin -->
                    </td>
                    <td class="CellLabel"   align="left" width="14%">Importe Factura</td>
                    <td class="CellContent" align="left" width="7%">&nbsp;<input type="text" name="txtImporteFacturaTotal" size="12" style='text-align:left' style="border :0; background-color: #F5F5EB;" readonly value="<%=MiUtil.formatDecimal(objOrderBean.getNpPaymentTotal())%>">
                        <input type="hidden" name="txtImporteFactura" value="<%=objOrderBean.getNpPaymentTotal()%>">
                    </td>

                    <td class="CellLabel"   align="left">Estado&nbsp;Proc.&nbsp;Aut.</td>
                    <td class="CellContent" align="left">
                        <input type="text" name="txtEstadoProcAutom" size="12" maxlength="30" style="border :0;  background-color:transparent;" readonly  value="<%=MiUtil.getString(objOrderBean.getNpScheduleStatusName())%>">
                    </td>
                    <!-- Inicio Data -->
                    <td class="CellContent">&nbsp;</td>
                    <!-- Fin Data -->

                </tr>
                <tr>
                    <td class="CellLabel"   align="left" width="12%">Estado de Pago</td>
                    <td class="CellContent" align="left" width="13%">&nbsp;<%=MiUtil.getString(objOrderBean.getNpPaymentStatus())%></td>  <!--Estado del pago-->
                    <td class="CellLabel"   align="left" width="12%"><%=MiUtil.getString(strParentName)%></td>
                    <input type="hidden" name="hdnParentName" value="<%=MiUtil.getString(strParentName)%>">
                    <%
                        if (  !MiUtil.getString(strGeneratorid).equals("") && !MiUtil.getString(strParentUrl).equals("") ){
                    %><td class="CellContent" align="left" width="8%"><a href="javascript:fxShowParent('<%=strGeneratorid%>','<%=strParentUrl%>')"><%=strGeneratorid%></a></td><%
                }else{
                %><td class="CellContent" width="4%">&nbsp;</td><%
                    }
                %>

                    <!-- INICIO PRY-1239 24/09/2018 PCACERES -->
                    <% if (MiUtil.getString(objOrderBean.getNpPaymentStatus()).equals("Cancelado")
                        && MiUtil.getString(sWebPayment).equals("1")) { %>
                        <td class="CellLabel">&nbsp;Importe Pagado</td>
                    <% } else {%>
                    <td class="CellLabel" width="7%">&nbsp;<a href="javascript:fxShowPayment();" >Importe Pagado</a>
                        <script>
                            function fxShowPayment(){
                                var vForm = document.frmdatos;
                                if(("<%=objOrderBean.getNpSpecificationId()%>" == "2068") || ("<%=objOrderBean.getNpSpecificationId()%>" == "2069")){
                                    var orderParentId = vForm.hdnOrdenParentId.value;
                                    if(orderParentId != ''){
                                        var url = "<%=strRutaContext%>/PAGEEDIT/PaymentList.jsp?nOrderId="+orderParentId ;
                                        window.open(url,"WinListPagos","toolbar=no,location=0,directories=no,status=yes,menubar=0,scrollbars=no,resizable=yes,screenX=100,top=80,left=150,screenY=80,width=700,height=500,modal=yes");
                                    }else{
                                        var url = "<%=strRutaContext%>/PAGEEDIT/PaymentList.jsp?nOrderId=<%=lOrderId0%>" ;
                                        window.open(url,"WinListPagos","toolbar=no,location=0,directories=no,status=yes,menubar=0,scrollbars=no,resizable=yes,screenX=100,top=80,left=150,screenY=80,width=700,height=500,modal=yes");
                                    }
                                }else{
                                    var url = "<%=strRutaContext%>/PAGEEDIT/PaymentList.jsp?nOrderId=<%=lOrderId0%>" ;
                                    window.open(url,"WinListPagos","toolbar=no,location=0,directories=no,status=yes,menubar=0,scrollbars=no,resizable=yes,screenX=100,top=80,left=150,screenY=80,width=700,height=500,modal=yes");
                                }
                            };
                        </script>
                    </td>
                    <% } %>
                    <!-- FIN PRY-1239 24/09/2018 PCACERES -->

                    <td class="CellContent">&nbsp;
                        <table>
                            <tr>
                                <td>
                                    <input type="text" name="txtnpTotalPayment" size="8" style='text-align:right' readonly value="<%=MiUtil.formatDecimal(objOrderBean.getNpPaymentAmount())%>">

                                </td>
                                <td>
                                    <div id="divPayment" name="divPayment" style="display:inline" >
                                        <%
                                            if ( ("Enabled".equals(MiUtil.getString((String)hshScreenField.get("bankpayment")))) && !("Cancelado".equals(MiUtil.getString(objOrderBean.getNpPaymentStatus()))) && !(fechProcIgual) ){//jsalazar hpptt fechProcIgual %>
                                        <a href="javascript:fxEditPayment();" onmouseover="window.status='Agregar Pago';" onmouseout="window.status='';">
                                            <img name="imgPago" Alt="Agregar Pago" src="<%=Constante.PATH_APPORDER_SERVER%>/images/crear.gif" border="no"/>
                                        </a>
                                        <%}%>
                                    </div>
                                    <input type="hidden" name="hdnTotalPaymentOrig" value="<%=objOrderBean.getNpPaymentAmount()%>">
                                    <input type="hidden" name="hdnPagoBanco">
                                    <input type="hidden" name="hdnPagoNroVoucher">
                                    <input type="hidden" name="hdnPagoImporte">
                                    <input type="hidden" name="hdnPagoFecha">
                                    <input type="hidden" name="hdnPagoDisponible">
                                    <input type="hidden" name="hdnPagoEstado" value="<%=MiUtil.getString(objOrderBean.getNpPaymentStatus())%>">
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td class="CellLabel"  align="left" width="14%">Imp. Dep. Garant?a</td>
                    <td class="CellContent" width="3%">&nbsp;
                        <input type="text" name="txtGuaranteeTotalAmount" size="12" style='text-align:left' style="border :0; background-color: #F5F5EB;" readonly value="<%=MiUtil.formatDecimal(Double.parseDouble(strTotalAmount))%>">

                    </td>
                    <!-- Inicio Data -->
                    <td class="CellContent">&nbsp;</td>
                    <!-- Fin Data -->
                </tr>
                <!-- Inicio [CHECK APODERADO - PROY 0787] JRIOS -->
                <tr id="rowDinamicoCST">
                </tr>
                <!-- Fin [CHECK APODERADO - PROY 0787] JRIOS -->

                <!--JLIMAYMANTA-->
                <% String strTypeServices="";
                    HashMap hashBuildingTS = new HashMap();
                    hashBuildingTS = NewOrderService.OrderDAOgetBuildingTS(objPortalSesBean.getBuildingid(),objPortalSesBean.getLogin());
                    hashBuildingTS = (HashMap)hashBuildingTS.get("hshDataTE");
                    System.out.println("hashBuildingTS"+hashBuildingTS);
                    if( hashBuildingTS!=null){
                        if (hashBuildingTS.size() != 0 ) {
                            strTypeServices               = MiUtil.getString((String)hashBuildingTS.get("wv_typeservice"));
                            System.out.println("[strTypeService]"+strTypeServices);
                        }
                    }
                %>

                <% System.out.println("[strTypeServices===================================]"+strTypeServices); %>
                <% System.out.println("[MiUtil.getString(objOrderBean.getNpVoucher())=====]"+MiUtil.getString(objOrderBean.getNpVoucher())); %>
                <tr>
                    <td class="CellLabel"   align="left" width="12%">Fecha Pago</td>
                    <td class="CellContent" align="left" width="13%">&nbsp;<%=MiUtil.toFecha(objOrderBean.getNpPaymentDate())%></td>  <!--Fecha del pago -->
                    <td class="CellLabel" align="left" width="12%">Propuesta</td><!--CBARZOLA-->
                    <td class="CellContent" align="left" width="8%">&nbsp;<%=MiUtil.getString(objOrderBean.getNpproposedid())%></td>
                    <input type="hidden" value="<%=strTypeServices%>" name="hdnTypeService" >
                    <input type="hidden" value="<%=objPortalSesBean.getBuildingid()%>" name="hdnTienda" >
                    <td class="CellLabel"   align="left" width="4%">Saldo</td>
                    <!--Calculo del Saldo-DLAZO-->
                    <%if((objOrderBean.getNpSpecificationId() == 2068) || (objOrderBean.getNpSpecificationId() == 2069)){%>
                    <td class="CellContent" align="left" width="7%">&nbsp;<input type="text" name="txtSaldo" size="9" style='text-align:right' style="border :0; background-color: #F5F5EB;" readonly value="<%=objPortabOrderServ.calculateBalance(lOrderId0)%>">
                            <%}else{%>
                    <td class="CellContent" align="left" width="7%">&nbsp;<input type="text" name="txtSaldo" size="9" style='text-align:right' style="border :0; background-color: #F5F5EB;" readonly value="<%=MiUtil.formatDecimal(objOrderBean.getSaldo())%>">
                            <%}%>
                    <td class="CellLabel" align="left" width="3%">Imp. Dep. Gar. Pag.</td>
                    <td class="CellContent" align="left">&nbsp;
                        <input type="text" name="txtGuaranteeDueAmount" size="12" style='text-align:left' style="border :0; background-color: #F5F5EB;" readonly value="<%=MiUtil.formatDecimal(Double.parseDouble(strDueAmount))%>">
                    </td>
                    <!-- Inicio Data -->
                    <td class="CellContent">&nbsp;</td>
                    <!-- Fin Data -->
                </tr>
				
                <!-- INICIO PRY-1239 21/09/2018 PCACERES -->
                <% if (MiUtil.getString(objOrderBean.getNpPaymentStatus()).equals("Cancelado")) { %>
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
					<td class="CellContent"></td>
                </tr>
                <% } %>
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
                <% if(MiUtil.getString(objOrderBean.getNpStatus()).equals(Constante.INBOX_ALMACEN_TIENDA)){%>
                <tr>
                    <td class="CellLabel" align="left" width="3%">Procesado BSCS</td>
                    <td class="CellContent" align="left"><%=MiUtil.getString(objOrderBean.getNpProcesoAutom()).equals("")?"NO":"SI"%></td>
                </tr>
                <%}%>
                <% if (showLinkPenaltyOrder){ //EFLORES REQ-0428%>
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
                    <td width="100%"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/pobtrans.gif" border="0" height="6"></td>
                </tr>
            </table>
        </td>
    </tr>
    <input type="hidden" name="hdnFechaHoraEntrega" value="<%=MiUtil.toFechaHora(objOrderBean.getNpDeliveryDate())%>" >
    <!--Rechazos -->
    <% //inicio de seccion que se muestra si y solo si no una reparaci?n
        if(!MiUtil.contentInArray(objOrderBean.getNpSpecificationId(),Constante.SPEC_REPAIRS)){
            //
    %>
    <tr>
        <td>
            <input type="hidden" name="hdnRejects" value=""><!-- se utiliza -->
            <table id="tableRejects" name="tableRejects" border="0" cellpadding="0" cellspacing="1" align="center" width="100%">
                <tr>
                    <td class="CellLabel"  align="center">&nbsp;&nbsp;&nbsp;#
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
                    <td class="CellLabel" align="center" id="divEditReject" style="display:'none';">&nbsp;
                        <a href="javascript:fxEditReject();"onmouseover="window.status='Registrar Rechazo';" onmouseout="window.status='';">
                            <img name="imgPago" Alt="Registrar Rechazo" src="<%=Constante.PATH_APPORDER_SERVER%>/images/crear.gif" border="no"></a>
                    </td>
                    <td class="CellLabel" align="center" id="divBlankReject">
                    </td>
                </tr>
                <%
                    /*hshOrder = objOrderService.getRejectList(lOrderId0);
                    strMessage0=(String)hshOrder.get("strMessage");

                    if (strMessage0!=null)
                       throw new Exception(strMessage0);

                    arrReject=(ArrayList)hshOrder.get("arrRechazos");
                    rjbRechazos =null;
                    */
                    for (int i=0;i<arrReject.size(); i++){
                        rjbRechazos=(RejectBean)arrReject.get(i);
                %>
                <tr>
                    <td class="CellContent" align="center"><%=i+1%></td>
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
                    <td class="CellLabel" width="20%">&nbsp;
                        <%if ("Enabled".equals(MiUtil.getString((String)hshScreenField.get("mandatorycarry")))){%>
                        <font color="red"><b>*</b></font>
               <%} // JCURI PRY-1093%>
                        Transportista</td>
                    <td class="CellContent" width="20%">&nbsp;
                  <select id="cmbTransportista" name="cmbTransportista" disabled style="width: 70%;">                      
                            <% System.out.println("[iFlagCarrier2]"+iFlagCarrier);
                                if (iFlagCarrier==1){
                                    //if ("Editable".equals(MiUtil.getString((String)hshScreenField.get("carrier")))){
                    //   hshData=objOrderService.getCarrierList("CARRIER","a"); 
                    //   strMessage0=(String)hshData.get("strMessage");
                    //  if (strMessage0!=null)
                    //      throw new Exception(strMessage0);                               
                    //   arrListado=(ArrayList)hshData.get("objListado");                      
                  %> 
                  <% //=MiUtil.buildCombo(arrListado,"carrierid","carriername")%>  
                  <script>                  
                  document.frmdatos.cmbTransportista.value="<%=MiUtil.getString(objOrderBean.getNpCarrierId())%>"; 
                                document.frmdatos.cmbTransportista.disabled=false;
                            </script>
                            <% }else{ %>
                            <option value="<%=MiUtil.getString(objOrderBean.getNpCarrierId())%>">  <!--wn_nxtranspid el valor  se obtiene del detalle de la orden-->
                                <%=MiUtil.getString(objOrderBean.getNpCarrierName())%> <!--wv_nxnombre el valor  se obtiene del detalle de la orden-->
                            </option>
                            <%}%>
                        </select>
                        <input type="hidden" name="hdnTransportistaAux">
                    </td>
                    <!--input type="hidden" name="hdnTransportista" value="'||wn_nxtranspid||'"-->
                    <td class="CellLabel" width="20%">
                        <%if (Constante.INBOX_ALMACEN_DELIVERY.equals(MiUtil.getString(objOrderBean.getNpStatus()))){%>
                        <font color="red"><b>*</b></font>
                        <%}%>
                        Fecha Hora Entrega</td>
                    <td class="CellContent" width="20%">&nbsp;
                        <%if ("Editable".equals(MiUtil.getString((String)hshScreenField.get("deliverydate")))){//jsalazar HPPTT%>
                        <input type="text" name="txtFechaHoraEntrega" size="15" maxlength="20" value="<%=MiUtil.toFechaHora(objOrderBean.getNpDeliveryDate())%>" onblur="frmdatos.hdnFechaHoraEntrega.value=this.value">
                        <%}else{ //Read Only%>
                        <%=MiUtil.toFechaHora(objOrderBean.getNpDeliveryDate())%>
                        <%}%>
                    </td>
                    <td colspan="2" class="CellContent" align="center" width="20%">&nbsp;
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
                    <td class="CellLabel"   align="left" width="20%">Verificaci?n de documentos</td>
                    <td class="CellContent" align="left"><font color="red">&nbsp;<%=objOrderBean.getDocVerificationBean().getResultDesc()%></font></td>
                </tr>
            </table>
        </td>
    </tr>
    <% } %>
    <!-- Fin [PPNN] MMONTOYA -->
    <tr>
        <td>
            <table border="0" cellspacing="0" cellpadding="0" width="99%">
                <tr>
                    <% ArrayList objInboxBack = objOrderService.getInboxList(MiUtil.getString(objOrderBean.getNpBpelbackinboxs()), MiUtil.getString(objOrderBean.getNpSpecificationId())  );
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
                                System.out.println("iNumInboxBack:"+iNumInboxBack);
                                if (iNumInboxBack==0){//eliminar la acci?n retroceder
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
                            // Si dentro de las opciones del combo accion se encuentra la opci?n BAGLOCK
                            //--------------------------------------------------------------------------
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
                        <%if (("Disabled".equals(MiUtil.getString((String)hshScreenField.get("action"))))|| !(fechProcMen)){%>  <!--jsalazar - modif hpptt # 1 - 29/09/2010 -->
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
                                        <!--%=MiUtil.buildCombo(objInboxBack,"datoBPel","datoBPel")%-->
                                        <%=MiUtil.buildInboxCombo(objInboxBack,"datoBPel","datoBPel",objOrderBean.getNpStatus())%>
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
                        <% if ("Enabled".equals(MiUtil.getString((String)hshScreenField.get("createpayorder")))
                                && !Constante.PAYFORM_CARGO_EN_RECIBO.equals(MiUtil.getString(objOrderBean.getNpPaymentTerms()).trim())
                                && objOrderBean.getNpPaymentTotal() >0)
                        {
                        %>
                        <input name="btnGenerarOrdenPago" type="button" value="Generar Orden Pago" onclick="javascript:fxGenerarOrdenPago();">
                        <%}%>
                    </td>
                    <td class="CellContent" width="*.%" >&nbsp;
                    </td>

                </tr>
            </table>
        </td>
    </tr>
    <td>
        <table border="0" cellspacing="0" cellpadding="0" width="99%">
            <tr>
                <td  class="CellContent" width="80%" >&nbsp;
                    <!--BOTON ORDEN DE PAGO-->
                    <%if ("Enabled".equals(MiUtil.getString((String)hshScreenField.get("createpayorder")))
                            && !Constante.PAYFORM_CARGO_EN_RECIBO.equals(MiUtil.getString(objOrderBean.getNpPaymentTerms()).trim())
                            && objOrderBean.getNpPaymentTotal() >0)
                    {
                    %>
                    <input name="btnGenerarOrdenPago" type="button" value="Generar Orden Pago" onclick="javascript:fxGenerarOrdenPago();">
                    <%}%>
                    <!--BOTON GENERAR COMPROBANTES-->
                    <% if ("Enabled".equals(MiUtil.getString((String)hshScreenField.get("createdocument")))){ %>
                    <input name="btnGenerarDocumentos" type="button" value="Generar Documentos" onclick="javascript:fxGenerarComprobantes();">
                    <%}%>
                    <!--BOTON SUSPENCION DE EQUIPOS EXTERNO.JNINO-->
                    <input type="hidden" name="hdnSuspEquipType">
                    <% if ("Enabled".equals(MiUtil.getString((String)hshScreenField.get("createsuspequip")))){ %>
                    <input name="btnSuspendeEquipo" type="button" value="Suspender Equipos" onclick="javascript:fxSuspendeEquipo('DEV');">
                    <%}%>
                    <!--BOTON PARTE DE INGRESO-->
                    <input type="hidden" name="hdnPIType">
                    <% if ("Enabled".equals(MiUtil.getString((String)hshScreenField.get("createparteingreso")))){ %>
                    <input name="btnParteIngreso" type="button" value="Parte de Ingreso" onclick="javascript:fxPartedeIngreso('DEV');">
                    <%}%>
                    <%
                        System.out.println("======>"+MiUtil.getString((String)hshScreenField.get("createparteingresobadimei")));
                    %>
                    <!--BOTON PARTE DE INGRESO BAD IMEI-->
                    <% if ("Enabled".equals(MiUtil.getString((String)hshScreenField.get("createparteingresobadimei")))){ %>
                    <input name="btnParteIngresoBadImei" type="button" value="Parte de Ingreso Bad SIM" onclick="javascript:fxPartedeIngreso('BAD');">
                    <%}%>
                    <!--BOTON AUTORIZACION DE DEVOLUCION-->
                    <!-- <script>alert("<%=autorizacionDevolucion%>")</script>-->
                    <% if (autorizacionDevolucion.equals("0")){ %>
                    <input name="btnFinanzas" type="button" value="Autorizar Extorno" onclick="javascript:fxAutorizacionDevolucion();">
                    <%}%>
                    <!--BOTON REEMPLAZAR EQUIPOS-->
                    <%if ("Enabled".equals(MiUtil.getString((String)hshScreenField.get("replaceHandset")))){%>
                    <%
                        System.out.println("strResult_ReplaceHandset ==>"+(String)hshAutomatizacion.get("strResult"));
                        System.out.println("REEMPLAZAR EQUIPOS==>"+MiUtil.getString((String)hshScreenField.get("replaceHandset")));
                    %>
                    <input name="btnReplaceHandset" type="button" value="Reemplazar Equipos" onclick="javascript:fxDisabledReplacedHandset(<%=iAppId%>);">
                    <%}%>
                    <!--BOTON GENERAR PARTE INGRESO-->
                    <%if  ("Enabled".equals(MiUtil.getString((String)hshScreenField.get("parteingresobadaddress")))){ %>
                    <%if ( MiUtil.getString(objOrderBean.getNpguidegenerated()).equals("S")){ %>
                    <input name="btnGenerarPI" type="button" value="Parte de Ingreso" onclick="javascript:fxPartedeIngreso('BADADDRESS');">
                    <%} else {%>
                    <input name="btnGenerarPI" type="button" value="Parte de Ingreso" onclick="javascript:fxPartedeIngreso('BADADDRESS');" disabled>
                    <%}%>
                    <%}%>
                    <!--BOTON GENERAR GUIA-->
                    <%if  ("Enabled".equals(MiUtil.getString((String)hshScreenField.get("guiaremision")))){ %>
                    <%if ( MiUtil.getString(objOrderBean.getNpguidegenerated()).equals("S")){ %>
                    <input name="btnGenerarGuia" type="button" value="Gu?a Remisi?n" onclick="javascript:fxGenerarGuia ();" disabled>
                    <%} else {%>
                    <input name="btnGenerarGuia" type="button" value="Gu?a Remisi?n" onclick="javascript:fxGenerarGuia ();" >
                    <%}%>
                    <%}%>
                </td>
                <td  class="CellContent" width="20%" >&nbsp; </td>
            </tr>
        </table>
    </td>
    <%}else{%>
    <td>
        <table border="0" cellspacing="0" cellpadding="0" width="99%">
            <tr>
                <td  class="CellContent" width="80%" >&nbsp;
                    <!--BOTON AUTORIZACION DE DEVOLUCION-->
                    <!--<script>alert("<%=autorizacionDevolucion%>")</script>-->
                    <% if ((autorizacionDevolucion.equals("0"))&& (!fechProcIgual)){ //jsalazar hpptt%>
                    <input name="btnFinanzas" type="button" value="Autorizar Extorno" onclick="javascript:fxAutorizacionDevolucion();">
                    <%}%>
                </td>
                <td  class="CellContent" width="20%" >&nbsp; </td>
            </tr>
        </table>
    </td>
    <%} %>
    <!--PHIDALGO-->
    <%if (  (iFlagAddDocument==1) && ("Editable".equals(MiUtil.getString((String)hshScreenField.get("adddocument")))) && !fechProcIgual) { //jsalazar hpptt %>
    <tr>
        <td>
            <table id="tableAddDocument" name="AddDocument" border="0" cellpadding="0" cellspacing="1" align="center" width="100%">
                <tr>
                    <td class="CellContent"><a href="javascript:fxUploadDocumentICM();">Adjuntar Documentos<a/></td>
                    <td class="CellContent">&nbsp;</td>
                    <td class="CellContent">&nbsp;</td>
                    <td class="CellContent">&nbsp;</td>
                    <td class="CellContent">&nbsp;</td>
                    <td class="CellContent">&nbsp;</td>
                    <td class="CellContent">&nbsp;</td>
                    <td class="CellContent">&nbsp;</td>
                </tr>
            </table>
        </td>
    </tr>
    <%}%>
    <!--JPEREZ - INICIO -->
    <!-- Campos para excepciones  -->
    <!--<input type="hidden" name="solution_id" value="<%=objOrderBean.getNpSolutionId()%>">-->
    <input type="hidden" name="hdnDivisionId" value="<%=objOrderBean.getNpDivisionId()%>">
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
    <!-- Orden Modificada  -->
    <input type="hidden" name="hdnChangedOrder" value="N"/>
    <!--JPEREZ - FIN -->

    <!--Inicio CEM-->
    <%
        System.out.println("StructId ACB: "+objOrderBean.getSalesStructureOriginalId());
        System.out.println("SpecificationId: "+objOrderBean.getNpSpecificationId());
        System.out.println("Status: "+MiUtil.getString(objOrderBean.getNpStatus()));
        String strMsgOrderPassForInventory="";
        if (objOrderBean.getNpSpecificationId()==Constante.SPEC_ACCESO_INTERNET &&
                MiUtil.getString(objOrderBean.getNpStatus()).equals(Constante.INBOX_INSTALACION_ING)){
            HashMap objHash = objOrderService.OrderPassForInventory(lOrderId0);
            strMsgOrderPassForInventory = (String) objHash.get("strMessage");
            System.out.println("strMsgOrderPassForInventory: "+strMsgOrderPassForInventory);
        }
    %>
    <INPUT type="hidden" name="hdnOrderPassForInventory" value="<%=strMsgOrderPassForInventory%>">
    <!--Fin CEM-->
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
                        <td class="CellContent" width="15%"><input type="radio" name="cpufresponse" id="cpufResponse0" value="0">No tengo CPUF
                        </td>
                        <td class="CellContent" width="15%"><input type="radio" name="cpufresponse" id="cpufResponse1" value="1">No conozco CPUF
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
                        <td class="CellLabel" width="15%" id="tdLabelDocType"><FONT color=#ff0000><B>*</B></FONT>Tipo de Documento</td>
                        <td class="CellContent" width="25%" id="tdDocTypeAssignee"></td>
                        <td class="CellLabel" width="15%" id="tdLabelNumDoc"><FONT color=#ff0000><B>*</B></FONT>N&uacute;mero de documento</td>
                        <td class="CellContent" width="45%" id="tdDocNumAssignee"></td>
                    </tr>

                    <tr>
                        <td class="CellLabel" width="15%" id="tdLabelName"><FONT color=#ff0000><B>*</B></FONT>Nombres</td>
                        <td class="CellContent" width="25%" id="tdFirstNameAssignee"></td>
                        <td class="CellContent" width="60%" colspan="2">&nbsp;</td>
                    </tr>
                    <tr>
                        <td class="CellLabel" width="15%" id="tdLabelLastName"><FONT color=#ff0000><B>*</B></FONT>Apellido Paterno</td>
                        <td class="CellContent" width="25%" id="tdLastNameAssignee"></td>
                        <td class="CellContent" width="60%" colspan="2">&nbsp;</td>
                    </tr>
                    <tr>
                        <td class="CellLabel" width="15%" id="tdLabelFamilyName"><FONT color=#ff0000><B>*</B></FONT>Apellido Materno</td>
                        <td class="CellContent" width="25%" id="tdFamilyNameAssignee"></td>
                        <td class="CellContent" width="60%" colspan="2">&nbsp;</td>
                    </tr>
                </table>

            </td>
        </tr>
    </table>

</div>
<div id="dvDigitalizacionDocumentos" style="display: none;">
    <br>
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
            <td class="CellContent" width="60%"><a href="javascript:fxShowDocumentsToGenerate()">Visualizar Documentos a digitalizar</a></td>
        </tr>

        <tr id="rowDinamico">
        </tr>

    </table>

</div>
<div id="dvVerificacionIdentidadAislada" style="display: block;">
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
    /* Cambiamos el nombre del t?tulo de la p?gina */
    parent.document.title = "<%=strOrderId0%>";

    <% if (validateAction){ %>
    document.frmdatos.cmbAction.disabled = true;
    <%}%>


    fxCargaFlagCourier();
    function fxCargaFlagCourier(){
        form = document.frmdatos;
        if (form.chkCourier != undefined){
            form.hdnChkCourier.value = "<%=MiUtil.getString(iCourier)%>";
        }
    }


    fxShowFlagCourier();
    function fxShowFlagCourier(){
        form = document.frmdatos;
        if (form.chkCourier != undefined && '<%=iShowCourier%>' == "1"){
            <% if ( String.valueOf(objOrderBean.getNpDispatchPlaceId()).equals(Constante.DISPATCH_PLACE_ID_FULLFILLMENT)) { %>
            form.chkCourier.checked = false;
            form.chkCourier.disabled = true;
            form.hdnChkCourier.value = "0";
            <% }else{%>
            form.chkCourier.disabled = false;
            <% }%>
        }
    }
    fxShowSectionsVIDD();



</script>

<%}catch(SessionException se) {
    System.out.println("[JP_ORDER_EDIT_ORDER][SessionException] : " + se.getClass() + " - " + se.getMessage());
    se.printStackTrace();
    //out.println("<script>alert('Su sesi?n ha finalizado. Ingrese nuevamente');</script>");
    //String strUrl = "/portal/pls/portal/WEBSALES.NP_USER_AVAILABILITY_PL_PKG.PL_LOGOUT";
    //out.print("<script>parent.mainFrame.location.replace('"+strUrl+"');</script>");
}catch(Exception e) {
    e.printStackTrace();
    String strMessageExceptionGeneralStart = "";
    strMessageExceptionGeneralStart = MiUtil.getMessageClean(e.getMessage());
    System.out.println("[JP_ORDER_EDIT_ORDER][Exception] : " + e.getClass() + " - " + e.getMessage());
    out.println("<script>alert('"+strMessageExceptionGeneralStart+"');</script>");
}
%>

<script DEFER>
    fxLoadFromCustomer();
    function fxLoadFromCustomer(){
        document.frmdatos.hdnCustAlcanceExclusividad.value = document.frmdatos.hdnExclusividad.value;
    }
    function fxShowParent(GeneratorId, parentUrl){
        if (parentUrl!=null){
            //location.replace(parentUrl+GeneratorId)
            var strUrl=parentUrl+GeneratorId;
            var frameUrl=strUrl;
            var winUrl = "/portal/pls/portal/WEBSALES.NPSL_NEW_GENERAL_PL_PKG.PL_FRAME?av_url="+escape(frameUrl);
            window.open(winUrl, "Incidencia","status=yes, location=no, width=1040, height=800, left=100, top=100, scrollbars=yes, screenX=50, screenY=100");

        }
    }

    //EFLORES REQ-0428 Mostrar Popup
    function fxShowPopup(parentUrl,title){ //Atributos separados por & ejem : a=3&b=3...
        if(parentUrl != null && parentUrl != ''){
            var winUrl = "/portal/pls/portal/WEBSALES.NPSL_NEW_GENERAL_PL_PKG.PL_FRAME?av_url="+escape(parentUrl);
            window.open(winUrl,'',"status=yes, location=no, width=1040, height=800, left=100, top=100, scrollbars=yes, screenX=50, screenY=100");
        }
    }
    function fxShowPayment1(){
        vform = document.frmdatos;
        vPaymentForm = vform.cmbFormaPago.value;
        if (vPaymentForm == "Contado"){
            divPayment.style.display = "block";
        }
        else{
            divPayment.style.display = "none";
        }
    }
    fxShowPayment1();

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


    function fxValidateRolDiasEdit(){ // solo funciona para casos de suspension definitiva
        //alert("fxValidateDiasEdit");
        form  = document.frmdatos;
        var npScheduleDate = form.txtFechaProceso.value;
        var npScheduleDate2 = form.txtFechaReconexion.value;

        var dFechaCreacion = form.hdnFechaCreacionOrden.value;
        //alert("dFechaCreacion : "+dFechaCreacion);
        var dFechaCreacion2 = ""+dFechaCreacion;
        //alert("dFechaCreacion2 : "+dFechaCreacion2);

        form.hdnFechaProcAut_new.value = npScheduleDate;
        //alert("form.hdnFechaProcAut_new.value : "+form.hdnFechaProcAut_new.value);
        var permisosEdicion = form.hdnPermisosEditFecha.value;

        var iDiasDiferencia = form.hdnCantidadDiasCalendario.value;

        var iResultadoValidacion = fxValidateFechas();

        if (iResultadoValidacion==1){
            //alert("Diferencia menor de 5");

            if (permisosEdicion==1){
                // cuando si tiene permiso
                form.hdnFechaProcAut_old.value = npScheduleDate;
            } else {
                // cuando no tiene permiso
                alert("Su perfil no le permite ingresar fechas menores a la programada inicialmente.");
                form.txtFechaProceso.value = form.hdnFechaProcAut_old.value;
                return false;
            }
        }

    }

    function fxValidateDiasEdit(paramNpOrderId){ //Agregado por rmartinez para manejar la Validacion de los 60 d?as en la Suspensiones
        form  = document.frmdatos;
        var npScheduleDate = form.txtFechaProceso.value;
        var npScheduleDate2 = form.txtFechaReconexion.value;

        form.myaction.value  = "validaDiasSuspensionEdit";
        url = "<%=request.getContextPath()%>/editordersevlet?paramNpOrderId="+ paramNpOrderId +"&myaction=validaDiasSuspensionEdit&npScheduleDate="+npScheduleDate + "&npScheduleDate2="+npScheduleDate2;
        form.action=url;
        form.submit();
    }


    function fxValidateFechas(){
        var result = 0;

        var npScheduleDate_new = form.txtFechaProceso.value;
        var maxOriginalDate = form.hdnFechaFinalOriginal.value;
        //alert("npScheduleDate_new : "+npScheduleDate_new);
        //alert("maxOriginalDate :"+maxOriginalDate);
        var valor = compareDates(npScheduleDate_new,"<",maxOriginalDate)
        //alert("valor de valor_nuevo < valor_antiguo : "+valor);

        if (valor==true){
            result = 1;
        }
        return result;
    }

    function  fxUploadDocumentICM(){
        var v_customerId = form.txtCompanyId.value;
        var v_objecttype = "ORDER";
        var v_url = "/portal/pls/portal/websales.NP_CONTENT_MANAGER_PL_PKG.PL_DOCUMENT_UPLOAD";
        v_url = v_url + "?av_elementID="+ wn_nxpedidoid +"&av_customerId=" + v_customerId +"&av_objectType=" + v_objecttype;
        window.open(v_url,"WinICMr","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,screenX=60,top=40,left=60,screenY=40,width=600,height=150");
    }



    function fxValidateFormaPagoVEP(objComboFormaPago){

       //INICIO: PRY-1200 | AMENDEZ
       var iSpecificationId=form.hdnSpecificationIdJ.value;
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
                    if (fxValidateExistItemsVEP(document.frmdatos.cmbVepNumCuotas,0)){
                        // Valida si existen Items con Modalidad Venta. Si es TRUE no existen
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
                            document.frmdatos.chkVepFlag.disabled = false;
                        }
                    }else{
                        //alert("Existen Items con modalidad Venta");
                        document.getElementById(objComboFormaPago.id).selectedIndex  = indexComboFomaPago;
                    }
                }else{
                    if (objComboFormaPago.value != '<%=Constante.PAYFORM_CARGO_EN_RECIBO%>'){
                        //alert(objComboFormaPago.value);
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

    //[Despacho en Tienda] PCASTILLO
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
    }

    //<!--MSOTO NO_000022816 02/06/2014-->
    function fxSearchCmbVendedor(typelist){
        v_form = document.frmdatos;
        v_txtCmbVendedor = v_form.txtCmbVendedor.value;
        //v_form.hdnVendedor.value = v_txtCmbVendedor;
        url = "/portal/pls/portal/!websales.np_contact_attention_pl_pkg.PL_VENDOR_SEARCH?" + "av_typelist=" + typelist + "&av_searchnom=" + escape(v_txtCmbVendedor);
        url = "/portal/pls/portal/websales.npsl_general_pl_pkg.window_frame?av_title=" + escape("Busqueda de compa?ia") + "&av_url=" + escape(url);
        WinAsist = window.open(url,"WinAsist","toolbar=no,location=0,directories=no,status=yes,menubar=0,scrollbars=no,resizable=no,screenX=100,top=80,left=100,screenY=80,width=700,height=500,modal=yes");
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

  //JCURI PRY-1093
  fxShowCarrierIni();
  fxShowContactIni();
  function fxShowCarrierIni() {
     var carrierId = document.frmdatos.cmbTransportista.value;     
     if(carrierId == null || carrierId=="" || carrierId==0){
    	 var dispatchPlaceId = document.frmdatos.cmbLugarAtencion.value;
       	 fxShowCarrierId(dispatchPlaceId);  
     }	      
  }
  function fxShowCarrier(cmbLugarAtencion) {		
		var idLugarAtencion = cmbLugarAtencion.value;
	  fxShowCarrierId(idLugarAtencion);
  }
  function fxShowCarrierChk(){
	  var idLugarAtencion = document.frmdatos.cmbLugarAtencion.value;
	  fxShowCarrierId(idLugarAtencion);
  }
  function fxShowCarrierId(idLugarAtencion) {
		$('option', '#cmbTransportista').remove();
		
		rs = fxValidateDispatchList(idLugarAtencion);
		if(!rs) {
			return;
		}
		
		$.ajax({
            url: "<%=request.getContextPath()%>/editordersevlet",
            type: "GET",
            async:false,
            cache:false,
            data:{
            	"myaction":"getCarrierPlaceOfficeList",
                "cmbLugarDespacho":idLugarAtencion,
                "strParamName":"CARRIER",
                "strParamStatus":"a"
            },
            dataType:'json',
            success:function(data) {
                if(data != null){               	
                	strMessage = data.strMessage;                	
                	if(strMessage == null) {
                		objListado = data.objListado;
                		if($.isArray(objListado)){
                    		
                			$.each(objListado, function(index, value) {
                				$("#cmbTransportista").append('<option value="' + value.carrierid + '" style="width: 70%;">' + value.carriername + '</option>');
                				fxTransportistaAux(value.carrierid);
                			});                			
                    		
                    	}
                	} else {
                		alert(strMessage);
                	}            		
                }
            },
            error:function(xhr, status, error) {
                alert(error);
            }
        });
		
	}

  	//JCURI PRY-1093
	function fxTransportistaAux(valor) {
		form  = document.frmdatos;
  		form.hdnTransportistaAux.value=valor;        
	}
	
  	//JCURI PRY-1093
	function fxShowContactIni() {
		form  = document.frmdatos;
		//fxDynamicContactSection(form.cmbLugarAtencion.value);   
		resValidation = fxValidateDispatchList(form.cmbLugarAtencion.value);
		
		if(resValidation) {
			displayContactsSection(resValidation);
		}		
	}
	

</script>