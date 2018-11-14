<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.exception.SessionException" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@ page import="oracle.portal.provider.v2.ProviderSession" %>
<%@ page import="oracle.portal.provider.v2.ProviderUser" %>
<%@ page import="oracle.portal.provider.v2.url.UrlUtils" %>
<%@ page import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil" %>
<%@ page import="pe.com.nextel.dao.*,pe.com.nextel.bean.*" %>
<%@ page import="java.util.*"%>
<%@ page import="pe.com.nextel.util.*"%>
<%@ page import="pe.com.nextel.service.SessionService"%>
<%@ page import="pe.com.nextel.service.GeneralService" %>
<%@ page import="pe.com.nextel.service.SiteService"%>
<%@ page import="pe.com.nextel.service.EditOrderService"%>
<%@ page import="pe.com.nextel.service.CustomerService"%>
<%@ page import="pe.com.nextel.service.NewOrderService"%>
<% 
try{
   String strSession="";
   /* inicio - comentar para probar localmente*/

   try{
    PortletRenderRequest pReq = (PortletRenderRequest) request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
    ProviderUser objetoUsuario1 = pReq.getUser();
    strSession=objetoUsuario1.getPortalSessionId();
    System.out.println("Sesión capturada  JP_ORDER_EDIT_CUSTOMER_ShowPage : " + objetoUsuario1.getName() + " - " + strSession );
    
    System.out.println("Sesión capturada  JP_ORDER_EDIT_CUSTOMER_ShowPage : " );
    
  }catch(Exception e){
    System.out.println("Portler Not Found : " + e.getClass() + " - " + e.getMessage() );
    out.println("Portlet JP_ORDER_EDIT_CUSTOMER_ShowPage Not Found");
    return;
  }

  //strSession = "805809601574955595205364047086969473998102872";
  
  System.out.println("Sesión capturada después del resuest : " + strSession );
	PortalSessionBean objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSession);
  GeneralService objGeneralService   = new GeneralService();
	if(objPortalSesBean==null) {
    System.out.println("No se encontró la sesión de Java ->" + strSession);
		throw new SessionException("La sesión finalizó");
	}
   
   int iUserId=0;
   int iAppId=0;   
   int iRegionId=0;
   String strCreatedBy=null;
   iUserId=objPortalSesBean.getUserid();
   iAppId= objPortalSesBean.getAppId();
   iRegionId=objPortalSesBean.getRegionId();
   strCreatedBy=objPortalSesBean.getCreatedby();
   HashMap hshUserRol = objGeneralService.getRol(Constante.SCRN_OPTTO_FRAUDVALIDATION, iUserId, iAppId);  
    int intFlagUserRol  = MiUtil.parseInt((String)hshUserRol.get("iRetorno"));
    
   //Parametros    
   String strOrderId=(request.getParameter("an_nporderid")==null?"0":request.getParameter("an_nporderid"));
   long lOrderId=Long.parseLong(strOrderId);
   
   HashMap hshData=null;
   HashMap hshOrder=null;  
   HashMap hshValidateFraudCustomer=null;
   String strMessage=null;       
   OrderBean objOrderBean=null;   
   CustomerBean objCustomerBean1=null;
   ArrayList arrContacts=null;
   ArrayList arrAddress=null;
   EditOrderService objOrderService=new EditOrderService();
   SiteService objSiteService= new SiteService();
   CustomerService objCustomerService=new CustomerService();   
   SiteBean objSiteBean=new SiteBean();
   String strNumDoc="",strTipoDoc="";
	 long lLineaCredito=0;
   
   HashMap   objHashMapList = null;
   ArrayList arrListTable   = null;
   
   hshOrder=objOrderService.getOrder(lOrderId);
   strMessage=(String)hshOrder.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);   
   
   objOrderBean=(OrderBean)hshOrder.get("objResume");  
   
   if (objOrderBean==null)
      throw new Exception("No se encontro Datos para la Orden");
         
   hshData=objCustomerService.getCustomerData2(objOrderBean.getCsbCustomer().getSwCustomerId(),strCreatedBy,objOrderBean.getNpRegionId());
   strMessage=(String)hshData.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);    
   
   objCustomerBean1=(CustomerBean)hshData.get("objCustomerBean");   
   
   if(objCustomerBean1==null) objCustomerBean1=new CustomerBean();   
    System.out.println("objOrderBean.getNpSiteId:"+objOrderBean.getNpSiteId()+"");
	
    if (objOrderBean.getNpSiteId()!=0){   
	
	  
      hshOrder = objSiteService.getSiteData(objOrderBean.getNpSiteId());                                      
      strMessage=(String)hshOrder.get("strMessage");   
      if (strMessage!=null)
         throw new Exception(strMessage);  
            
      objSiteBean=(SiteBean)hshOrder.get("objSite");		
      lLineaCredito=objSiteBean.getNpLineaCredito();
      objSiteBean.setSwSiteId(objOrderBean.getNpSiteId());
      hshData=objCustomerService.getCustomerContacts2(objOrderBean.getNpSiteId(),Constante.CUSTOMERTYPE_SITE);      
      strMessage=(String)hshData.get("strMessage");
      if (strMessage!=null)
         throw new Exception(strMessage);    
      
      arrContacts=(ArrayList)hshData.get("arrContactsList");      
      
      hshData=objCustomerService.getCustomerAddress2(objOrderBean.getNpSiteId(),objSiteBean.getSwRegionId(),Constante.CUSTOMERTYPE_SITE);        
      strMessage=(String)hshData.get("strMessage");
      if (strMessage!=null)
         throw new Exception(strMessage);    
      
      arrAddress=(ArrayList)hshData.get("arrAddressList");        
   }else{		
      lLineaCredito=objCustomerBean1.getNpLineaCredito();
      hshData=objCustomerService.getCustomerContacts2(objOrderBean.getCsbCustomer().getSwCustomerId(),Constante.CUSTOMERTYPE_CUSTOMER);      
      strMessage=(String)hshData.get("strMessage");
      if (strMessage!=null)
         throw new Exception(strMessage);    
      
      arrContacts=(ArrayList)hshData.get("arrContactsList");      
      
      hshData=objCustomerService.getCustomerAddress2(objOrderBean.getCsbCustomer().getSwCustomerId(),objCustomerBean1.getSwRegionId(),Constante.CUSTOMERTYPE_CUSTOMER);        
      strMessage=(String)hshData.get("strMessage");
      if (strMessage!=null)
         throw new Exception(strMessage);    
      
      arrAddress=(ArrayList)hshData.get("arrAddressList");           
   }   
   
   strNumDoc = objCustomerBean1.getStrRucValid();
   strTipoDoc = objCustomerBean1.getStrNameValid();
   
   System.out.println("strNumDoc: "  + strNumDoc);
   System.out.println("strTipoDoc: " + strTipoDoc);
   System.out.println("lLineaCredito: "+lLineaCredito);
   
   if (strMessage!=null)
      throw new Exception(strMessage);
  
   objHashMapList = objGeneralService.getTableList("ORDER_INBOX_ENTREGA","1");
   if( objHashMapList != null ){
      if( (String)objHashMapList.get("strMessage")!=null )
        throw new Exception(MiUtil.getMessageClean((String)objHashMapList.get("strMessage")));
   }else
      throw new Exception("Hubieron errores obteniendo datos para ORDERS_INBOX_ENTREGA");
   
   arrListTable = (ArrayList)objHashMapList.get("arrTableList");

   // Cambio Dirección de entrega
   HashMap hashBuildingName = NewOrderService.OrderDAOgetBuildingName(objPortalSesBean.getBuildingid(),objPortalSesBean.getLogin());   
   int intRegionTramiteCodigo = 0;
   hashBuildingName = (HashMap)hashBuildingName.get("hshData");
   System.out.println("hashBuildingName"+hashBuildingName);
   if( hashBuildingName!=null){
     if (hashBuildingName.size() != 0 ) {       
         intRegionTramiteCodigo  = MiUtil.parseInt((String)hashBuildingName.get("wn_regionid"));  
         System.out.println("[intRegionTramiteCodigo]"+intRegionTramiteCodigo);
      }
    }

  // Inicio [N_O000017567] MMONTOYA
  OrderContactBean orderContactBean = objOrderBean.getOrderContactBean();

  //INICIO DERAZO REQ-0940
  String checkNotification = "";
  if(orderContactBean.getCheckNotification() != null && !orderContactBean.getCheckNotification().equals("")){
      System.out.println("[JP_ORDER_EDIT_CUSTOMER_ShowPage.jsp] CheckNotification: "+orderContactBean.getCheckNotification());
      if(orderContactBean.getCheckNotification().equals(Constante.DELIVERY_NOTIFICATION_CHECKED)){
          checkNotification = "checked";
      }
  }
  //FIN DERAZO

    ArrayList arrDocumentType = objGeneralService.getTipoDocumentoList();
/*
  ArrayList arrDocumentType = new ArrayList();

  HashMap map = new HashMap();
  map.put("value", "DNI");
  map.put("description", "DNI");
  arrDocumentType.add(map);

  map = new HashMap();
  map.put("value", "RUC");
  map.put("description", "RUC");
  arrDocumentType.add(map);*/
  // Fin [N_O000017567] MMONTOYA

  //INICIO DERAZO REQ-0940
  HashMap hshConfigTraceability = objGeneralService.getTraceabilityConfigurations();
  int flagTraceabilityFunct = 0;
  int showSectionContacts = 0;
  String propertyShowSectionContacts = "none";
  List<ConfigurationBean> listTracSpecifications = new ArrayList<ConfigurationBean>();
  List<ConfigurationBean> listTracDispatchPlaces = new ArrayList<ConfigurationBean>();
  List<ConfigurationBean> listTracTypeDocuments = new ArrayList<ConfigurationBean>();
  List<ConfigurationBean> listTracTypeCustomersRUC = new ArrayList<ConfigurationBean>();

  if(hshConfigTraceability.get("flagTraceabilityFunct") != null) {
    flagTraceabilityFunct = (Integer) hshConfigTraceability.get("flagTraceabilityFunct");

    if (flagTraceabilityFunct == 1) {
        //Validamos si se va a mostrar o no la seccion de contactos segun los datos de la orden
        HashMap hshValidateShowContacts = objGeneralService.getValidateShowContacts(lOrderId);
        if(hshValidateShowContacts.get("resultValidation") != null){
            showSectionContacts = (Integer)hshValidateShowContacts.get("resultValidation");
        }

        //Obtenemos el listado de configuraciones por si se modifica al editar
        listTracSpecifications = (ArrayList<ConfigurationBean>)hshConfigTraceability.get("listTracSpecifications");
        listTracDispatchPlaces = (ArrayList<ConfigurationBean>)hshConfigTraceability.get("listTracDispatchPlaces");
        listTracTypeDocuments = (ArrayList<ConfigurationBean>)hshConfigTraceability.get("listTracTypeDocuments");
        listTracTypeCustomersRUC = (ArrayList<ConfigurationBean>)hshConfigTraceability.get("listTracTypeCustomersRUC");
    }
  }

  if(showSectionContacts == 1){
      propertyShowSectionContacts = "inline";
  }

  System.out.println("[JP_ORDER_EDIT_CUSTOMER_ShowPage.jsp] flagTraceabilityFunct: "+flagTraceabilityFunct + " showSectionContacts: "+showSectionContacts);
  //FIN DERAZO
  
  //JCURI PRY-1093
  System.out.println("JP_ORDER_EDIT_CUSTOMER_ShowPage.jsp [lugarDespachoDeliveryList] ");
  HashMap mapDispatchPlaceListDelivery = objGeneralService.lugarDespachoDeliveryList();
  ArrayList dispatchPlaceListDelivery = (ArrayList) mapDispatchPlaceListDelivery.get("objArrayList");
  System.out.println("JP_ORDER_EDIT_CUSTOMER_ShowPage.jsp [lugarDespachoDeliveryList.dispatchPlaceListDelivery] " +dispatchPlaceListDelivery);
   	
  //JCURI PRY-1093
  HashMap objHashMapDeliverySpecification = new HashMap();  
  ArrayList arrListDeliverySpecification = null;
  objHashMapDeliverySpecification = objGeneralService.GeneralDAOgetComboList("ORDEN_DELIVERY_REGION_SPECIFICATION");
  if( objHashMapDeliverySpecification.get("strMessage") != null ) { 
  	System.out.println("JP_ORDER_EDIT_CUSTOMER_ShowPage.jsp [ORDEN_DELIVERY_REGION_SPECIFICATION]" + objHashMapDeliverySpecification.get("strMessage"));
  } else {
  	arrListDeliverySpecification = (ArrayList) objHashMapDeliverySpecification.get("objArrayList"); 
    System.out.println("JP_ORDER_EDIT_CUSTOMER_ShowPage.jsp [arrListDeliverySpecification] " + arrListDeliverySpecification);    
  }
   	
%>  
<script defer>
    //INICIO DERAZO REQ-0940
    var flagTraceabilityFunct = <%=flagTraceabilityFunct%>;
    var showSectionContacts = <%=showSectionContacts%>;

    var arrTracSpecifications = [];
    var arrTracDispatchPlaces = [];
    var arrTracTypeDocuments = [];
    var arrTracTypeCustomersRUC = [];

    if(flagTraceabilityFunct == 1){
        <%
          for (int i=0; i<listTracSpecifications.size(); i++) {
            ConfigurationBean configTrac = listTracSpecifications.get(i); %>
            arrTracSpecifications[<%=i%>] = "<%= configTrac.getNpValue()%>";
        <%
          }

          for (int i=0; i<listTracDispatchPlaces.size(); i++) {
            ConfigurationBean configTrac = listTracDispatchPlaces.get(i); %>
            arrTracDispatchPlaces[<%=i%>] = "<%= configTrac.getNpValue()%>";
        <%
          }

          for (int i=0; i<listTracTypeDocuments.size(); i++) {
            ConfigurationBean configTrac = listTracTypeDocuments.get(i); %>
            arrTracTypeDocuments[<%=i%>] = "<%= configTrac.getNpValue()%>";
        <%
          }

          for (int i=0; i<listTracTypeCustomersRUC.size(); i++) {
            ConfigurationBean configTrac = listTracTypeCustomersRUC.get(i); %>
            arrTracTypeCustomersRUC[<%=i%>] = "<%= configTrac.getNpValue()%>";
        <%
          }
        %>
    }
    //FIN DERAZO
    
    //PRY-1093 JCURI
    var arrDispatchPlacesDelivery = [];
    <%
    if(dispatchPlaceListDelivery != null && dispatchPlaceListDelivery.size()>0) {
  	   	HashMap map=null;
  		for (int i = 0; i < dispatchPlaceListDelivery.size(); i++) {
  		map=(HashMap)dispatchPlaceListDelivery.get(i);
  		int buildingidDelivery = MiUtil.getInt((String)map.get("buildingid"));
  	%>    		
  		arrDispatchPlacesDelivery[<%=i%>] = "<%=buildingidDelivery%>";
  	<% 
  		}
     }
	%>
	
	var arrDeliverySpecification = [];
    <%
    if(arrListDeliverySpecification != null && arrListDeliverySpecification.size() > 0) {
  	   	HashMap map=null;
  		for (int i = 0; i < arrListDeliverySpecification.size(); i++) {
  		map= (HashMap) arrListDeliverySpecification.get(i);
  		String npvalueSpecification = MiUtil.getString((String)map.get("wn_npvalue"));
  	%>    		
  		arrDeliverySpecification[<%=i%>] = "<%=npvalueSpecification%>";
  	<% 
  		}
     }
	
	%>
	//FIN PRY-1093 JCURI
	
</script>
  <!--Inicio de JP_ORDER_EDIT_CUSTOMER.jsp -->
<input type="hidden" name="hdnExclusividad" value ="<%=MiUtil.getString(objCustomerBean1.getStrExclusivity())%>">
<input type="hidden" name="hdnSiteRegionId" value ="<%=MiUtil.getString(objSiteBean.getSwRegionId())%>">
<input type="hidden" name="hdnCustomerRegionId" value ="<%=intRegionTramiteCodigo%>">
<input type="hidden" name="hdnTipoDocumento" value ="<%=strTipoDoc%>">
<!--DERAZO REQ-0940-->
<input type="hidden" name="hdnNumDocumento" value ="<%=strNumDoc%>">

<input type="hidden" name="myaction"/>
<!--PRY-1049  | INICIO: AM0001-->
<input type=hidden id="hdnCobertura" name="hdnCobertura" value ="<%=MiUtil.getString(""+objOrderBean.getNpflagcoverage())%>"/>
<input type=hidden id="hdnRegion" name="hdnRegion"  value ="<%=MiUtil.getString(""+objOrderBean.getNpdepartmentuseaddress())%>"/>
<input type=hidden id="hdnProvince" name="hdnProvince"  value ="<%=MiUtil.getString(""+objOrderBean.getNpprovinceuseaddress())%>"/>
<input type=hidden id="hdnDistrict" name="hdnDistrict"  value ="<%=MiUtil.getString(""+objOrderBean.getNpdistrictuseaddress())%>"/>
<input type=hidden id="hdnRegiondId" name="hdnRegiondId"  value ="<%=MiUtil.getString(""+objOrderBean.getNpzonacoberturaid())%>"/>
<input type=hidden id="hdnProvinceId" name="hdnProvinceId"  value ="<%=MiUtil.getString(""+objOrderBean.getNpprovincezoneid())%>"/>
<input type=hidden id="hdnDistrictId" name="hdnDistrictId"  value ="<%=MiUtil.getString(""+objOrderBean.getNpdistrictzoneid())%>"/>
<input type=hidden id="hdnUseFullAddress" name="hdnUseFullAddress"  value ="<%=MiUtil.getString(""+objOrderBean.getNpuseinfulladdress())%>"/>
<!--PRY-1049  | FIN: AM0001-->

<!-- BEGIN: PRY-1049 | DOLANO-0002 -->
<input type=hidden id="hdnHomeServiceZone" name="hdnHomeServiceZone"  value ="<%=MiUtil.getString(""+objOrderBean.getVchHomeServiceZone())%>"/>
<!-- END: PRY-1049 | DOLANO-0002 -->

<table border="0" cellspacing="0" cellpadding="0" width="15%">
   <tr class="PortletHeaderColor"> 
      <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"></td>
      <td class="SubSectionTitle" align="left" valign="top">Compañ&iacute;a</td>
      <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"></td>
   </tr>
</table>	

<table border="0" cellspacing="1" cellpadding="2" width="100%" class="RegionBorder">
<tr >
   <td align="center" class="CellLabel" width="30%">Razón Social</td>
   <td align="center" class="CellLabel" width="30%">Nombre Comercial</td>
   <td align="center" class="CellLabel" width="15%">Ruc/DNI/Otro</td>
    <td align="center" class="CellLabel" width="15%">Tipo de Persona</td>
   <td align="center" class="CellLabel" width="15%" colspan="2">Tipo Compañia</td>
</tr>
<tr>             
   <td align="center" class="CellContent">            
      <table width="100%" border="0" cellspacing="0" cellpadding="0" >
         <tr>                     
            <input type="hidden" name="hdnCustomerName" value="<%=objOrderBean.getCsbCustomer().getSwCustomerId()%>">
            <input type="hidden" name="txtCompanyId" onfocus="blur()" value="<%=objOrderBean.getCsbCustomer().getSwCustomerId()%>"> <!-- necesario-->
            <input type="hidden" name="hdnCustomerIdBscs" value="<%=objOrderBean.getCsbCustomer().getSwCustomerId()%>">                                                                          
            <input type="hidden" name="hdnCustName" value="<%=objCustomerBean1.getSwName()%>"> <!-- portabilidad-->
            <td width="90%" class="CellContent" align="left">&nbsp;<input type="text" name="txtCompany" size="50" maxlength="60" onChange="this.value=trim(this.value.toUpperCase());" style="border :0; background-color:transparent;" value="<%=MiUtil.getString(objCustomerBean1.getSwName())%>" readOnly></td>
            <td width="10%" class="CellContent" align="center"><a href="javascript:getCustDetail('<%=objOrderBean.getCsbCustomer().getSwCustomerId()%>','<%= iUserId %>')"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/viewDetail.gif" alt="Detalle de cliente" width="15" height="15" border="0"></a>
            <%
            if(intFlagUserRol==1)
            {
              hshValidateFraudCustomer=objCustomerService.getValidationFraudCustomer(objCustomerBean1.getSwRuc());
              if(hshValidateFraudCustomer.get("strMessage")!=null){%>
                alert("<%=hshValidateFraudCustomer.get("strMessage")%>");
              <%}
             String strrespuesta=(String)hshValidateFraudCustomer.get("Respuesta");
             if(strrespuesta.equals("S"))
             {
               int intcodTypeList= Integer.parseInt((String)hshValidateFraudCustomer.get("CodTypeList"));
                 switch(intcodTypeList)
                 {
                  case 1 :%>
                      <a href="javascript:alert('<%=(String)hshValidateFraudCustomer.get("DescFraud")%>')">HL</a>
                   <% break;
                  case 2 :%> 
                  <a href="javascript:alert('<%=(String)hshValidateFraudCustomer.get("DescFraud")%>')">BL</a>
                  <%break;
                 }
               }
             }
            %>
            </td>
         </tr>
       </table>
   </td>            
   <td align="center" class="CellContent">&nbsp;<%=MiUtil.getString(objCustomerBean1.getSwNameCom())%></td>            
   <%
   int nNum = 0;
   if ((strTipoDoc.equals("RUC") || strTipoDoc.equals("DNI"))){
       String strMessageDoc  = null;
       HashMap hshDataDoc=null;    
       ArrayList arrDocuments=null;    
       CustomerService objCustomerService1=new CustomerService();    
       hshDataDoc=objCustomerService1.getValidateDocument(strNumDoc,strTipoDoc);        
       strMessageDoc=(String)hshDataDoc.get("strMessage");
       if (strMessageDoc!=null)
          throw new Exception(strMessageDoc);            
       arrDocuments=(ArrayList)hshDataDoc.get("arrCustomer");    
       nNum = arrDocuments.size();
    }
    
    if((Constante.INBOX_ADM_VENTAS.equals(objOrderBean.getNpStatus()) || Constante.INBOX_CREDITO.equals(objOrderBean.getNpStatus()) && (nNum>0) )) {
    %>   
      <td align="center" class="CellContent"><table width="100%"> <tr><td align="center" class="CellContent">&nbsp;<%=MiUtil.getString(objCustomerBean1.getSwRuc())%></td><td align="left" class="CellContent"><a href="javascript:getAlertDocumentEquals('<%=strNumDoc%>','<%=strTipoDoc%>')"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/helpl.gif" alt="Ver Alerta" width="15" height="15" border="0"></a></td></tr></table></td>
   <%}else{%>
   <td align="center" class="CellContent">&nbsp;<%=MiUtil.getString(objCustomerBean1.getSwRuc())%></td>
   <%}%>
   <input type="hidden" name="txtRucDisabled" value="<%=MiUtil.getString(objCustomerBean1.getSwRuc())%>">
    <td align="center" class="CellContent">&nbsp;<%=MiUtil.getString(objCustomerBean1.getSwTipoPerson())%></td>
    <input type="hidden" name="txtSwTipoPerson" value="<%=MiUtil.getString(objCustomerBean1.getSwTipoPerson())%>">
    <td align="center" class="CellContent" >&nbsp;<%=MiUtil.getString(objCustomerBean1.getSwType())%></td>
   <input type="hidden" name="txtSwType" value="<%=MiUtil.getString(objCustomerBean1.getSwType())%>">
</tr>

<tr >
   <td align="center" class="CellLabel" >Tipo Cuenta</td>
   <td align="center" class="CellLabel">
   <%  if (strTipoDoc.equals("RUC")) { %>
   <span style= "display:none">Segmento de Negocio</span>
   <%  }else {%>
   <span style= "display:block">Segmento de Negocio</span>
   <% }%>
   </td>
   <td align="center" class="CellLabel" >Region Cia</td>
   <td align="center" class="CellLabel">Teléfono</td>
   <td align="center" class="CellLabel">Fax</td>
</tr>
<tr>
   <td align="center" class="CellContent">&nbsp;<%=MiUtil.getString(objCustomerBean1.getNpCorporativo())%></td>
    <td align="center" class="CellContent">&nbsp;
    <% if (strTipoDoc.equals("RUC")) { %>
    <span style= "display:none"><%=MiUtil.getString(objCustomerBean1.getNpSegmento())%></span>
    <% }else { %>
    <span style= "display:block"><%=MiUtil.getString(objCustomerBean1.getNpSegmento())%></span>
    <% } %>
    </td>
    <td align="center" class="CellContent"><%=MiUtil.getString(objCustomerBean1.getSwRegionName())%>
   </td>
   <td align="center" class="CellContent">&nbsp;<%=MiUtil.getString(objCustomerBean1.getSwMainPhone())%>
   <td align="center" class="CellContent">&nbsp;<%=MiUtil.getString(objCustomerBean1.getSwMainFax())%></td>
</tr>
 <tr>
      <td align="center" class="CellLabel" >Giro Negocio</td>
      <td align="center" class="CellLabel">Rango de Cuenta</td>
      <td align="center" class="CellLabel" ></td>
      <td align="center" class="CellLabel"></td>
      <td align="center" class="CellLabel"></td>
 </tr>
 <tr>
      <td align="center" class="CellContent">&nbsp;<%=MiUtil.getString(objCustomerBean1.getNpGiroName())%></td>
      <td align="center" class="CellContent">&nbsp;<%=MiUtil.getString(objCustomerBean1.getNpRangoCuenta())%></td>
      <td align="center" class="CellContent"><%=MiUtil.getString(objCustomerBean1.getSwRegionName())%></td>
      <td align="center" class="CellContent">&nbsp;<%=MiUtil.getString(objCustomerBean1.getSwMainPhone())%>
      <td align="center" class="CellContent">&nbsp;<%=MiUtil.getString(objCustomerBean1.getSwMainFax())%></td>
 </tr>
<tr>
        <td align="center" class="CellLabel">Autorización LPDP</td>
        <td align="center" class="CellLabel">Fecha actualización LPDP</td>
        <td align="center" class="CellLabel" ></td>
        <td align="center" class="CellLabel"></td>
        <td align="center" class="CellLabel"></td>
    </tr>
    <tr>
        <td align="center" class="CellContent">&nbsp;<%=MiUtil.getString(objCustomerBean1.getNpAutoLpdp())%></td>
        <td align="center" class="CellContent">&nbsp;<%=MiUtil.getString(objCustomerBean1.getNpFechaLpdp())%></td>
        <td align="center" class="CellContent">&nbsp;</td>
        <td align="center" class="CellContent">&nbsp;</td>
        <td align="center" class="CellContent">&nbsp;</td>
    </tr>
<tr>
   <td colspan="5">
      <table border="0" cellspacing="1" cellpadding="0" width="100%">
         <tr>
            <td align="center" class="CellLabel">Responsable de Pago&nbsp;</td>
             <td align="center" class="CellLabel" width="10%">Carpeta Digital</td> <!-- EFH -->
            <td align="center" class="CellLabel"><a href="javascript:fCopyCodBSCSEdit();">&nbsp;Código BSCS</a></td>
            <td align="center" class="CellLabel">&nbsp;Scoring</td>        
            <td align="center" class="CellLabel">&nbsp;Línea Cred. PEN</td><!--FES Se cambió US$ por PEN -->
            <td align="center" class="CellLabel">&nbsp;Región Site</td>
            <td align="center" class="CellLabel">&nbsp;Tipo Cliente</td>
            <td align="center" class="CellLabel">&nbsp;Cliente Principal</td>
         </tr>
         <tr>
             <td align="center" class="CellContent">&nbsp;
                <%=MiUtil.getString(objSiteBean.getSwSiteName())%> -  <%=MiUtil.getString(objSiteBean.getNpCodBscs())%>
               <input type="hidden" name="txtSiteId" value="<%=objSiteBean.getSwSiteId()%>">               
               <input type="hidden" name="hdnSite" value="<%=objSiteBean.getSwSiteId()%>">
               <input type="hidden" name="hdnClientSitesFlag" value="0">
               <input type="hidden" name="hdnUnkwnSiteId" value="0">
            </td>            
             <td align="center" class="CellContent"><input type="checkbox" onclick="fxChangeCarpetaDigital(this);"  <% if(objOrderBean.getNpCarpetaDigital()!= null && objOrderBean.getNpCarpetaDigital().equals("S")){ %> checked="checked"<%} %>/>
                 <input id="hdnCarpetaDigital" type="hidden" name="hdnCarpetaDigital" <% if(objOrderBean.getNpCarpetaDigital()!= null) { %> value="<%= objOrderBean.getNpCarpetaDigital()%>" <%}else{%> value="N" <%}%>/>
             </td> <!-- EFH -->
            <td align="center" class="CellContent">&nbsp;<%=MiUtil.getString(objCustomerBean1.getSwCodBscs())%>
            <input type="hidden" name="hdnCodBscsDetail" value="<%=MiUtil.getString(objCustomerBean1.getSwCodBscs())%>">
            </td>
            <td align="center" class="CellContent">&nbsp;<%=MiUtil.getString(objCustomerBean1.getSwRating())%><input type="hidden" name="hdnScoring" value=""></td>
            <td align="center" class="CellContent">&nbsp;<%=String.valueOf(lLineaCredito)%></td>  
            <td align="center" class="CellContent">&nbsp;<%=MiUtil.getString(objSiteBean.getSwRegionName())%><input type="HIDDEN" name="txtRegionSite" value="<%=MiUtil.getString(objSiteBean.getSwRegionName())%>"></td>
            <td align="center" class="CellContent">&nbsp;<%=MiUtil.getString(objCustomerBean1.getNpCustomerRelationType())%></td>
            <td align="center" class="CellContent">
            &nbsp;<a href="javascript:getCustomerDetail()"><%=MiUtil.getString(objCustomerBean1.getNpCustomerRelationName())%></a></td>
         </tr>
      </table>
   </td>
</tr>
<tr>
   <td colspan="6" height="28">
      <table id="table_address" name="table_address" border="0" cellspacing="1" cellpadding="0" width="100%">
         <tr>
            <td align="center" class="CellLabel" nowrap >Dirección</td>
            <td align="center" class="CellLabel">&nbsp;Distrito</td>
            <td align="center" class="CellLabel">&nbsp;Provincia</td>        
            <td align="center" class="CellLabel">&nbsp;Departamento</td>
            <td align="center" class="CellLabel">&nbsp;Tipo</td>                     
            </td>
         </tr>
         <!--Inicio:Campos para almacenar la direccion de Entrega modificable-->
         <input type=hidden name='hdnDeliveryAddress'>
         <input type=hidden name='hdnDeliveryCity'>
         <input type=hidden name='hdnDeliveryProvince'>
         <input type=hidden name='hdnDeliveryState'>  
         
         <input type=hidden name='hdnDeliveryCityId'>
         <input type=hidden name='hdnDeliveryProvinceId'>
         <input type=hidden name='hdnDeliveryStateId'>
         
         <!--Fin-->         
         <% HashMap hshMap = new HashMap(); 
           for (int i=0;i<arrAddress.size();i++) {
               hshMap = (HashMap)arrAddress.get(i);
         %>
         <tr>
         
            
             <% if(! (Constante.TYPE_ADDRESS_ENTREGA.equals(MiUtil.getString((String)hshMap.get("wv_swtipodirec")).toUpperCase())) ) {%>                        
               <td align="left" class="CellContent">&nbsp;&nbsp;<%=MiUtil.getString((String)hshMap.get("wv_swaddress1"))%>
                <input type=hidden name='address1' value="<%=MiUtil.getString((String)hshMap.get("wv_swaddress1"))%>" >
                <input type=hidden name='address1CmbId' value="<%=MiUtil.getString((String)hshMap.get("wv_swcityid"))%>" >
               </td>
               <td align="left" class="CellContent">&nbsp;&nbsp;<%=MiUtil.getString((String)hshMap.get("wv_swcity"))%></td>
               <td align="left" class="CellContent">&nbsp;&nbsp;<%=MiUtil.getString((String)hshMap.get("wv_swprovince"))%></td>
               <td align="left" class="CellContent">&nbsp;&nbsp;<%=MiUtil.getString((String)hshMap.get("wv_swstate"))%></td>
               <td align="left" class="CellContent">&nbsp;&nbsp;<%=MiUtil.getString((String)hshMap.get("wv_swtipodirec"))%></td>
            <% }%>                
         </tr> 
        <%}%>         
         <tr>
            <!-- Si el inbox es INBOX_ADM_VENTAS o INBOX_ST_PLATAFORMA. -->
            <%if (Constante.INBOX_ADM_VENTAS.equals(objOrderBean.getNpStatus()) || Constante.INBOX_ST_PLATAFORMA.equals(objOrderBean.getNpStatus())) {
               // Si hay datos de la dirección de entrega, los muestra y los guarda en los campos ocultos.
               if(objOrderBean.getNpShipToAddress1()!=null && !"".equals(objOrderBean.getNpShipToAddress1())){%>
                  <!-- Inicio Muestra los datos de la dirección de entrega -->
                  <td align="left" class="CellContent">&nbsp;
                  <input readonly type=text name=txtDirEntrega value="<%=MiUtil.getString(objOrderBean.getNpShipToAddress1())%>" style='border :0;background-color:transparent;width:98%';>
                  </td>                          
                  <td align="left" class="CellContent">&nbsp;
                  <input readonly type=text name=txtCity value="<%=MiUtil.getString(objOrderBean.getNpShipToCity())%>" style='border :0;background-color:transparent;width:98%';>
                  </td>
                  <td align="left" class="CellContent">&nbsp;
                  <input readonly type=text name=txtProvince value="<%=MiUtil.getString(objOrderBean.getNpShipToProvince())%>" style='border :0;background-color:transparent;width:98%';>
                  </td>
                  <td align="left" class="CellContent">&nbsp;
                  <input readonly type=text name=txtState value="<%=MiUtil.getString(objOrderBean.getNpShipToState())%>" style='border :0;background-color:transparent;width:98%';>
                  </td>
                  <!-- Fin Muestra los datos de la dirección de entrega -->
                  <!-- Inicio guarda los datos de la dirección de entrega en campos ocultos y crea el lapiz -->
                  <td align="left" class="CellContent">&nbsp;
                  <a href=javascript:fxShowAdress();><%=Constante.TYPE_ADDRESS_ENTREGA%></a>&nbsp;&nbsp;
                  <%
                      HashMap objHshMap = new HashMap(); 
                      for (int i=0;i<arrListTable.size();i++) {
                        objHshMap = (HashMap)arrListTable.get(i);
                        
                        //  Si el inbox esta configurado como inbox de entrega. En este caso permite la edición de la dirección de entrega.
                        if(MiUtil.getString((String)objHshMap.get("wv_npValue")).equals(objOrderBean.getNpStatus())){
                  %>
                    <a href=javascript:fxEditShippingAddress();><img src='<%=Constante.PATH_APPORDER_SERVER%>/images/Editar.gif' border=0 align='absbottom'></a>
                    <script>
                      var form = document.frmdatos;
                      form.hdnDeliveryAddress.value = "<%=MiUtil.getString(objOrderBean.getNpShipToAddress1())%>";
                      form.hdnDeliveryCity.value = "<%=MiUtil.getString(objOrderBean.getNpShipToCity())%>";
                      form.hdnDeliveryProvince.value = "<%=MiUtil.getString(objOrderBean.getNpShipToProvince())%>";
                      form.hdnDeliveryState.value = "<%=MiUtil.getString(objOrderBean.getNpShipToState())%>";
                      
                    </script>
                  <%      
                          break;
                        }
                      }
                  %>
                  </td>  
                  <!-- Fin guarda los datos de la dirección de entrega en campos ocultos y crea el lapiz -->
               <!-- Si NO hay datos de la dirección de entrega, los muestra. -->
               <%}else{%>
                  <!-- Inicio Muestra los datos de la dirección de entrega -->
                  <td align="left" class="CellContent">&nbsp;
                  <input readonly type=text name=txtDirEntrega value="<%=MiUtil.getString(objOrderBean.getNpShipToAddress1())%>" style='border :0;background-color:transparent;width:98%';>
                  </td>
                  <td align="left" class="CellContent">&nbsp;
                  <input readonly type=text name=txtCity value="<%=MiUtil.getString(objOrderBean.getNpShipToCity())%>" style='border :0;background-color:transparent;width:98%';>
                  </td>
                  <td align="left" class="CellContent">&nbsp;
                  <input readonly type=text name=txtProvince value="<%=MiUtil.getString(objOrderBean.getNpShipToProvince())%>" style='border :0;background-color:transparent;width:98%';>
                  </td>
                  <td align="left" class="CellContent">&nbsp;
                  <input readonly type=text name=txtState value="<%=MiUtil.getString(objOrderBean.getNpShipToState())%>" style='border :0;background-color:transparent;width:98%';>
                  </td>            
                  <td align="left" class="CellContent">&nbsp;            
                  <a href=javascript:fxShowAdress();><%=Constante.TYPE_ADDRESS_ENTREGA%></a></td>
                  <!-- Fin Muestra los datos de la dirección de entrega -->
               <!-- Si el inbox NO es INBOX_ADM_VENTAS o INBOX_ST_PLATAFORMA. -->
               <%} } else{%>
                  <!-- Inicio Muestra los datos de la dirección de entrega -->
                  <td align="left" class="CellContent">&nbsp;
                  <input readonly type=text name=txtDirEntrega value="<%=MiUtil.getString(objOrderBean.getNpShipToAddress1())%>" style='border :0;background-color:transparent;width:98%';>
                  </td>                          
                  <td align="left" class="CellContent">&nbsp;
                  <input readonly type=text name=txtCity value="<%=MiUtil.getString(objOrderBean.getNpShipToCity())%>" style='border :0;background-color:transparent;width:98%';>
                  </td>
                  <td align="left" class="CellContent">&nbsp;
                  <input readonly type=text name=txtProvince value="<%=MiUtil.getString(objOrderBean.getNpShipToProvince())%>" style='border :0;background-color:transparent;width:98%';>
                  </td>
                  <td align="left" class="CellContent">&nbsp;
                  <input readonly type=text name=txtState value="<%=MiUtil.getString(objOrderBean.getNpShipToState())%>" style='border :0;background-color:transparent;width:98%';>
                  </td>            
                  <!-- Fin Muestra los datos de la dirección de entrega -->
                  <!-- Inicio guarda los datos de la dirección de entrega en campos ocultos y crea el lapiz -->
               <td align="left" class="CellContent">&nbsp;&nbsp;ENTREGA
               <!-- Los bloques de codigo que siguen son iguales. El primero es para cuando el inbox es INBOX_BAGLOCK, en este caso se realizan unas validaciones. El segundo es para otros inboxes. -->
                 <%
                      HashMap objHshMap = new HashMap(); 
                      for (int i=0;i<arrListTable.size();i++) {
                        objHshMap = (HashMap)arrListTable.get(i);

                        //  Si el inbox esta configurado como inbox de entrega. En este caso permite la edición de la dirección de entrega.
                        if(MiUtil.getString((String)objHshMap.get("wv_npValue")).equals(objOrderBean.getNpStatus())){
                          if (Constante.INBOX_BAGLOCK.equals(objOrderBean.getNpStatus())){
                              HashMap hshUserRol1 = objGeneralService.getRol(4752, iUserId, iAppId);
                              int iFlagAdmVenta = MiUtil.parseInt((String)hshUserRol1.get("iRetorno"));
                                if(iFlagAdmVenta==1){
                  %>
                    <a href=javascript:fxEditShippingAddress();><img src='<%=Constante.PATH_APPORDER_SERVER%>/images/Editar.gif' border=0 align='absbottom'></a>
                    <script>
                      var form = document.frmdatos;
                      form.hdnDeliveryAddress.value = "<%=MiUtil.getString(objOrderBean.getNpShipToAddress1())%>";
                      form.hdnDeliveryCity.value = "<%=MiUtil.getString(objOrderBean.getNpShipToCity())%>";
                      form.hdnDeliveryProvince.value = "<%=MiUtil.getString(objOrderBean.getNpShipToProvince())%>";
                      form.hdnDeliveryState.value = "<%=MiUtil.getString(objOrderBean.getNpShipToState())%>";
                      
                    </script>
                  <%      
                                }
                          }
                          else{
                  %>
                    <a href=javascript:fxEditShippingAddress();><img src='<%=Constante.PATH_APPORDER_SERVER%>/images/Editar.gif' border=0 align='absbottom'></a>
                    <script>
                      var form = document.frmdatos;
                      form.hdnDeliveryAddress.value = "<%=MiUtil.getString(objOrderBean.getNpShipToAddress1())%>";
                      form.hdnDeliveryCity.value = "<%=MiUtil.getString(objOrderBean.getNpShipToCity())%>";
                      form.hdnDeliveryProvince.value = "<%=MiUtil.getString(objOrderBean.getNpShipToProvince())%>";
                      form.hdnDeliveryState.value = "<%=MiUtil.getString(objOrderBean.getNpShipToState())%>";
                      
                    </script>
                  <%     
                          }
                          break;
                        }
                      }
                  %>
               </td>
               <!-- Fin guarda los datos de la dirección de entrega en campos ocultos y crea el lapiz -->
            <%}%>                        
         </tr> 

          <!--PRY-1049  | INICIO: AM0001-->
          <%if (objOrderBean.getNpuseinfulladdress()!=null && !"".equals(objOrderBean.getNpuseinfulladdress())) {%>
          <!--PRY-1049  | INICIO: AM0001  In validacion-->
          <tr>
              <td align="left" class="CellContent">&nbsp;&nbsp;<%=objOrderBean.getNpuseinfulladdress()%></td>
              <td align="left" class="CellContent">
                  &nbsp;&nbsp;<%=objOrderBean.getNpdistrictuseaddress()%>
                  <!-- PRY-1140  | INICIO: DOLANO-0003 -->
                  <!-- Solo si el flag de cobertura es que tiene cobertura Bafi 2300, se muestra el HSZ -->
                  <%if (objOrderBean.getNpflagcoverage() == 1 && objOrderBean.getVchHomeServiceZoneDesc() != null &&
                          objOrderBean.getVchHomeServiceZoneDesc().trim().length() > 0) {%>
                    &nbsp;&nbsp; (HSZ =  <%=objOrderBean.getVchHomeServiceZoneDesc()%> )
                  <%}%>
                  <!-- PRY-1140  | FIN: DOLANO-0003 -->
              </td>
              <td align="left" class="CellContent">&nbsp;&nbsp;<%=objOrderBean.getNpprovinceuseaddress()%></td>
              <td align="left" class="CellContent">&nbsp;&nbsp;<%=objOrderBean.getNpdepartmentuseaddress()%></td>
              <td align="left" class="CellContent">&nbsp;&nbsp;USO</td>
          </tr>
          <!--PRY-1049  | FIN: AM0001  In validacion-->
          <%}%>
          <!--PRY-1049  | FIN: AM0001-->

     </table>       
   </td>
</tr>
<tr>
   <td colspan="6">
      <table  id="table_contacts" name="table_contacts"  border="0" cellspacing="1" cellpadding="0" width="100%">
         <tr>
            <td align="center" class="CellLabel">&nbsp;Apellidos y Nombres&nbsp;</td>
            <td align="center" class="CellLabel">&nbsp;Area</td>
            <td align="center" class="CellLabel">&nbsp;Tel&eacute;fono</td>
            <td align="center" class="CellLabel">&nbsp;Area</td>        
            <td align="center" class="CellLabel">&nbsp;Fax</td>
            <td align="center" class="CellLabel">
               <table>
                  <tr>
                     <td align="center" class="CellLabel">USR.</td>
                     <td align="center" class="CellLabel">FAC.</td>
                     <td align="center" class="CellLabel">GG.</td>
                  </tr>
               </table>
            </td>
         </tr>
         <% HashMap h = new HashMap(); 
           for (int i=0;i<arrContacts.size();i++) {
               h = (HashMap)arrContacts.get(i);
         %>
         <tr>
            <td align="left" class="CellContent">&nbsp;<%=MiUtil.getString((String)h.get("wv_swname"))%></td>
            <td align="left" class="CellContent">&nbsp;<%=MiUtil.getString((String)h.get("wv_swphonearea"))%></td>
            <td align="left" class="CellContent">&nbsp;<%=MiUtil.getString((String)h.get("wv_swphone"))%></td>
            <td align="left" class="CellContent">&nbsp;<%=MiUtil.getString((String)h.get("wv_swfaxarea"))%></td>
            <td align="left" class="CellContent">&nbsp;<%=MiUtil.getString((String)h.get("wv_swfax"))%></td>
            <td align="center" class="CellContent">
                <table>
                   <tr>
                      <td align="center" class="CellContent" height="5"><input style="height :13 px;" type=checkbox disabled="disabled" <%=(((String)h.get("wv_swflagusuario")).equals("1")?"checked":"")%> /></td>
                      <td align="center" class="CellContent" height="5"><input style="height :13 px;" type=checkbox disabled="disabled" <%=(((String)h.get("wv_swflagfacturacion")).equals("1")?"checked":"")%> /></td>
                      <td align="center" class="CellContent" height="5"><input style="height :13 px;" type=checkbox disabled="disabled" <%=(((String)h.get("wv_swflaggerenteg")).equals("1")?"checked":"")%>   /></td>
                   </tr>
                </table>
            </td>
         </tr>
         <%}%>
      </table>
   </td>
</tr>
</table>

<!-- Inicio [N_O000017567] MMONTOYA -->
<script type="text/javascript">
    function fxEditShippingAddress() {
        fxEditAdress();
        displayContactsSection(true);
    }

    function displayContactsSection(display) {
        var divContacts = document.getElementById("divContacts");

    	if (display) {
    	    divContacts.style.display = "inline";
    	} else {
    	    divContacts.style.display = "none";
    	}
    }

    //Funcion para setear el tamaño del campo segun la opcion
    function changeValue(dropdown) {

        var option = dropdown.options[dropdown.selectedIndex].value,
                field = document.getElementById('txtContactDocumentNumber');
        if (option == 'DNI') {
            field.maxLength = 8;
            field.value="";
        } else if (option == 'RUC') {
            alert("El tipo de documento RUC no está permitido para datos de contacto.");
            field.maxLength = 11;
            field.value="";
            document.getElementById('cmbContactDocumentType').setAttribute('value','DNI');
        } else if (option == 'CE'){
            field.maxLength = 20;
            field.value="";
        } else if (option == 'PAS'){
            field.maxLength = 20;
            field.value="";
        }
    }

    function lettersNumbers(option,flag){
        if((option=='DNI') || (flag=='1')){
            justNumbers();
        }
    }

    //Función que permite solo Números
    function justNumbers() {
        if ((event.keyCode < 48) || (event.keyCode > 57))
            event.returnValue = false;
    }

    //Función que permite solo letras y espacios en blanco
    function justLetters() {
        if ((event.keyCode != 32) && (event.keyCode < 65) || (event.keyCode > 90) && (event.keyCode < 97) || (event.keyCode > 122))
            event.returnValue = false;
    }
    //EFLORES N_SD000349095
    function fxChangeCarpetaDigital(chkCarpetaDigital){
        if(chkCarpetaDigital.checked){
            document.getElementById("hdnCarpetaDigital").value="S";
        }else{
            document.getElementById("hdnCarpetaDigital").value="N";
        }
    }
    //EFLORES

    //DERAZO REQ-0940 Funcion que permite solo caracteres distintos a espacios en blanco
    function justEmailCharacters(){
        if(event.keyCode == 32) event.returnValue = false;
    }
</script>
<!-- Sección contactos -->
<div id="divContacts" style="display:<%=propertyShowSectionContacts%>"><!--DERAZO REQ-0940-->
<div style="line-height:38%;"><br></div>
<table border="0" cellspacing="0" cellpadding="0" width="15%">
   <tr class="PortletHeaderColor">
      <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"></td>
      <td class="SubSectionTitle" align="left" valign="top">Contacto</td>
      <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"></td>
   </tr>
</table>
<table border="0" cellspacing="1" cellpadding="2" width="100%" class="RegionBorder">
    <tr>
       <!--DERAZO REQ-0940-->
       <td align="center" class="CellLabel" width="15%">Notificaci&oacute;n del Delivery</td>
       <td align="center" class="CellLabel" width="16%">Nombre del Contacto</td>
       <td align="center" class="CellLabel" width="16%">Apellido del Contacto</td>
       <td align="center" class="CellLabel" width="10%">Tipo de Documento</td>
       <td align="center" class="CellLabel" width="10%">Nro. Documento</td>
       <td align="center" class="CellLabel" width="10%">N° Celular</td>
       <!--DERAZO REQ-0940-->
       <td align="center" class="CellLabel" width="23%">E-mail</td>
    </tr>
    <tr>
        <!--DERAZO REQ-0940-->
        <td align="center" class="CellContent"><input type="checkbox" name="chkContactNotification" id="chkContactNotification" style="width:50px" <%=checkNotification%>/><input type="hidden" name="hdnContacNotification"/></td>
        <td align="center" class="CellContent"><input type="text" name="txtContactFirstName" id="txtContactFirstName" style="width:180px" maxlength="40" value="<%=MiUtil.getString(orderContactBean.getFirstName())%>" onkeypress="justLetters()"></td>
        <td align="center" class="CellContent"><input type="text" name="txtContactLastName" id="txtContactLastName" style="width:180px" maxlength="70" value="<%=MiUtil.getString(orderContactBean.getLastName())%>" onkeypress="justLetters()"></td>
        <td align="center" class="CellContent">
            <select name="cmbContactDocumentType" id="cmbContactDocumentType" style="width:75px" onchange="changeValue(this);">
                <%= MiUtil.buildComboSelected(arrDocumentType, "description", "value", MiUtil.getString(orderContactBean.getDocumentType()))%>
            </select></td>
        <td align="center" class="CellContent"><input type="text" name="txtContactDocumentNumber" id="txtContactDocumentNumber" style="width:100px" maxlength="8" value="<%=MiUtil.getString(orderContactBean.getDocumentNumber())%>" onkeypress="lettersNumbers(form.cmbContactDocumentType.value,'0')"></td>
        <td align="center" class="CellContent"><input type="text" name="txtContactPhoneNumber" id="txtContactPhoneNumber" style="width:100px" maxlength="20" value="<%=MiUtil.getString(orderContactBean.getPhoneNumber())%>" onkeypress="lettersNumbers('','1')"></td>
        <!--DERAZO REQ-0940-->
        <td align="center" class="CellContent"><input type="text" name="txtContactEmail" id="txtContactEmail" maxlength="80" value="<%=MiUtil.getString(orderContactBean.getEmail())%>" style="width:250px" onkeypress="justEmailCharacters()"></td>
    </tr>
    <tr>
        <td align="left" class="CellLabel">Referencia</td>
        <td align="left" class="CellContent" colspan="6"><input type="text" name="txtReference" id="txtReference" style="border-bottom:0px;border-left:0px;border-top:0px;border-right:0px;background-color:transparent;width:98%;" readOnly="readonly" value="<%=MiUtil.getString(objOrderBean.getNpShipToReference())%>"></td>
    </tr>
</table>
</div>
<!-- Fin [N_O000017567] MMONTOYA -->
<%}catch(SessionException se) {
    System.out.println("[JP_ORDER_EDIT_CUSTOMER][SessionException] : " + se.getClass() + " - " + se.getMessage());
    se.printStackTrace();
    //out.println("<script>alert('Su sesión ha finalizado. Ingrese nuevamente');</script>");
    //String strUrl = "/portal/pls/portal/WEBSALES.NP_USER_AVAILABILITY_PL_PKG.PL_LOGOUT";
     //out.print("<script>parent.mainFrame.location.replace('"+strUrl+"');</script>");
  }catch(Exception e) {
    String strMessageExceptionGeneralStart = "";
    strMessageExceptionGeneralStart = MiUtil.getMessageClean(e.getMessage());
    System.out.println("[JP_ORDER_EDIT_CUSTOMER][Exception] : " + e.getClass() + " - " + e.getMessage());
    out.println("<script>alert('"+strMessageExceptionGeneralStart+"');</script>");   
  }
%>
   <!--Fin de JP_ORDER_CUSTOMER_EDIT.jsp -->
