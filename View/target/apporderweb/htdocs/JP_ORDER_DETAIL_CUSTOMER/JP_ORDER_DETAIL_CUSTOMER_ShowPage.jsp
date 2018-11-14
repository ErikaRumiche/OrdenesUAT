<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="pe.com.nextel.exception.SessionException" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@ page import="oracle.portal.provider.v2.ProviderUser" %>
<%@ page import="oracle.portal.provider.v2.url.UrlUtils" %>
<%@ page import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil" %>
<%@ page import="pe.com.nextel.dao.*,pe.com.nextel.bean.*" %>
<%@ page import="java.util.*"%>
<%@ page import="pe.com.nextel.util.*"%>
<%@ page import="pe.com.nextel.service.SessionService"%>
<%@ page import="pe.com.nextel.service.GeneralService" %>
<%@ page import="pe.com.nextel.service.CustomerService" %>
<%@ page import="pe.com.nextel.service.EditOrderService" %>
<%@ page import="pe.com.nextel.service.SiteService" %>
<!--Inicio de JP_ORDER_CUSTOMER_DETAIL.jsp -->    
<% 
try{
   String strSessionId1  = "";
 
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
  
 //strSessionId1 = "998102396";
  System.out.println("Sesión capturada después del resuest: " + strSessionId1);
  PortalSessionBean portalSessionBean = (PortalSessionBean) SessionService.getUserSession(strSessionId1);
  GeneralService objGeneralService   = new GeneralService();
  if (portalSessionBean == null) {
      System.out.println("No se encontró la sesión de Java ->" + strSessionId1);
      throw new SessionException("La sesión finalizó");
  }
   int iUserId;
   int iAppId;   
   int iRegionId;
   String strCreatedBy=null;
   
   iUserId=portalSessionBean.getUserid();
   iAppId= portalSessionBean.getAppId();
   iRegionId=portalSessionBean.getRegionId();
   strCreatedBy=portalSessionBean.getCreatedby();
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
   //CustomerBean objCustomerBean=null;
   CustomerBean objCustomerBean1=null;
   ArrayList arrContacts=null;
   ArrayList arrAddress=null;   
   EditOrderService objOrderService=new EditOrderService();
   SiteService objSiteService= new SiteService();
   CustomerService objCustomerService=new CustomerService();
   SiteBean objSiteBean=new SiteBean();
	long lLineaCredito=0;
	String strTipoDoc="";
	
   
   hshOrder=objOrderService.getOrder(lOrderId);
   strMessage=(String)hshOrder.get("strMessage");
   
   if (strMessage!=null)
      throw new Exception(strMessage);   
   
   objOrderBean=(OrderBean)hshOrder.get("objResume");  
   
   if (objOrderBean==null)
      throw new Exception("No se encontro Datos para la Orden");
      
   hshData =objCustomerService.getCustomerData(objOrderBean.getCsbCustomer().getSwCustomerId());//objOrderBean.getCsbCustomer().getSwCustomerId()   
   strMessage=(String)hshData.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage); 
      
   //objCustomerBean=(CustomerBean)hshData.get("objCustomerBean");
   
   //hshData=objCustomerService.getCustomerData2(objOrderBean.getCsbCustomer().getSwCustomerId(),strCreatedBy,objOrderBean.getNpRegionId());
   hshData=objCustomerService.getCustomerDataDetail(objOrderBean.getCsbCustomer().getSwCustomerId(),strCreatedBy,objOrderBean.getNpRegionId(),lOrderId);
   strMessage=(String)hshData.get("strMessage");
  
   if (strMessage!=null)
      throw new Exception(strMessage);    
   
   objCustomerBean1=(CustomerBean)hshData.get("objCustomerBean");
   System.out.println("objOrderBean.getNpSiteId:"+objOrderBean.getNpSiteId()+"");
   if (objOrderBean.getNpSiteId()!=0){    
      hshOrder= objSiteService.getSiteData(objOrderBean.getNpSiteId());                                      
      strMessage=(String)hshOrder.get("strMessage");  
      if (strMessage!=null)
         throw new Exception(strMessage);   
      
      objSiteBean=(SiteBean)hshOrder.get("objSite");
		lLineaCredito=objSiteBean.getNpLineaCredito();
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
      hshData=objCustomerService.getCustomerContacts2(objOrderBean.getCsbCustomer().getSwCustomerId(),Constante.CUSTOMERTYPE_CUSTOMER);		
		lLineaCredito=objCustomerBean1.getNpLineaCredito();
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

   strTipoDoc = objCustomerBean1.getStrNameValid();
   
   System.out.println("strTipoDocumento: "  + strTipoDoc);

   if (strMessage!=null)
      throw new Exception(strMessage);   
      
   // [N_O000017567] MMONTOYA
   OrderContactBean orderContactBean = objOrderBean.getOrderContactBean();
      
   String strRutaContext=request.getContextPath();      
   
   System.out.println("---------------------- INICIO JP_ORDER_DETAIL_CUSTOMER------------------");
   System.out.println("strOrderId-->"+strOrderId);   
   System.out.println("CustomerId-->"+objOrderBean.getCsbCustomer().getSwCustomerId());
   System.out.println("strCreatedBy-->"+strCreatedBy);
   System.out.println("RegionID-->"+objOrderBean.getNpRegionId());
	System.out.println("lLineaCredito: "+lLineaCredito);
	System.out.println("Specification: "+objOrderBean.getNpSpecificationId());
	System.out.println("Npstatustmp: "+objOrderBean.getNpstatustmp());
	System.out.println("getNpShipToAddress1: "+MiUtil.getString(objOrderBean.getNpShipToAddress1()));
	System.out.println("getNpShipToAddress2: "+MiUtil.getString(objOrderBean.getNpShipToAddress2()));
	
   System.out.println("---------------------- FIN JP_ORDER_DETAIL_CUSTOMER------------------");   
   
   //INICIO DERAZO REQ-0940
   String checkNotification = "";
   if(orderContactBean.getCheckNotification() != null && !orderContactBean.getCheckNotification().equals("")){
     System.out.println("[JP_ORDER_EDIT_CUSTOMER_ShowPage.jsp] CheckNotification: "+orderContactBean.getCheckNotification());
     if(orderContactBean.getCheckNotification().equals(Constante.DELIVERY_NOTIFICATION_CHECKED)){
        checkNotification = "checked";
     }
   }
   //FIN DERAZO
%>  
  <!--Inicio de JP_ORDER_EDIT_CUSTOMER.jsp -->

<input type="hidden" name="myaction"/>
<table border="0" cellspacing="0" cellpadding="0" width="15%">
   <tr class="PortletHeaderColor"> 
      <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"></td>
      <td class="SubSectionTitle" align="left" valign="top">Compañia</td>
      <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"></td>
   </tr>
</table>	

<table border="0" cellspacing="1" cellpadding="2" width="99%" class="RegionBorder">
   <tr >      
      <td align="center" class="CellLabel" width="30%">Razón Social</td>
      <td align="center" class="CellLabel" width="30%">Nombre Comercial</td>
      <td align="center" class="CellLabel" width="15%">Ruc/DNI/Otro</td>
       <td align="center" class="CellLabel" width="15%">Tipo de Persona</td>
       <td align="center" class="CellLabel" width="15%" colspan="2">Tipo Compañ&iacute;a</td>
   </tr>
   <tr>          
      <td align="center" class="CellContent">            
         <table width="100%" border="0" cellspacing="0" cellpadding="0" >
            <tr>                     
               <input type="hidden" name="hdnCustomerName" value="<%=objOrderBean.getCsbCustomer().getSwCustomerId()%>">
               <input type="hidden" name="txtCompanyId" onfocus="blur()" value="<%=objOrderBean.getCsbCustomer().getSwCustomerId()%>"> <!-- necesario-->
               <input type="hidden" name="hdnCustomerIdBscs" value="<%=objOrderBean.getCsbCustomer().getSwCustomerId()%>">         
               <input type="hidden" name="hdnCustName" value="<%=objCustomerBean1.getSwName()%>"> <!-- portabilidad-->
                <!--PRY-1049 | INICIO: AM0001-->
                <input type=hidden id="hdnCobertura" name="hdnCobertura" value ="<%=MiUtil.getString(""+objOrderBean.getNpflagcoverage())%>"/>
                <input type=hidden id="hdnRegion" name="hdnRegion"  value ="<%=MiUtil.getString(""+objOrderBean.getNpdepartmentuseaddress())%>"/>
                <input type=hidden id="hdnProvince" name="hdnProvince"  value ="<%=MiUtil.getString(""+objOrderBean.getNpprovinceuseaddress())%>"/>
                <input type=hidden id="hdnDistrict" name="hdnDistrict"  value ="<%=MiUtil.getString(""+objOrderBean.getNpdistrictuseaddress())%>"/>
                <!--PRY-1140 LGONZALES BEGIN-->
                <input type=hidden id="hdnRegiondId" name="hdnRegiondId"  value ="<%=MiUtil.getString(""+objOrderBean.getNpzonacoberturaid())%>"/>
                <input type=hidden id="hdnProvinceId" name="hdnProvinceId"  value ="<%=MiUtil.getString(""+objOrderBean.getNpprovincezoneid())%>"/>
                <input type=hidden id="hdnDistrictId" name="hdnDistrictId"  value ="<%=MiUtil.getString(""+objOrderBean.getNpdistrictzoneid())%>"/>
                <!--PRY-1140 LGONZALES END-->
                <input type=hidden id="hdnUseFullAddress" name="hdnUseFullAddress"  value ="<%=MiUtil.getString(""+objOrderBean.getNpuseinfulladdress())%>"/>
                <!--PRY-1049  | FIN: AM0001-->

                    <!-- BEGIN: PRY-1049 | DOLANO-0002 -->
                    <input type=hidden id="hdnHomeServiceZone" name="hdnHomeServiceZone"  value ="<%=MiUtil.getString(""+objOrderBean.getVchHomeServiceZone())%>"/>
                    <!-- END: PRY-1049 | DOLANO-0002 -->

               <td width="90%" class="CellContent" align="left">&nbsp;<input type="text" name="txtCompany" size="40" maxlength="60" onChange="this.value=trim(this.value.toUpperCase());" style="border :0; background-color:transparent;" value="<%=MiUtil.getString(objCustomerBean1.getSwName())%>" readOnly></td>
               <td width="10%" class="CellContent" align="center"><a href="javascript:getCustDetail('<%=objOrderBean.getCsbCustomer().getSwCustomerId()%>','<%= iUserId %>')"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/viewDetail.gif" alt="Detalle de cliente" width="15" height="15" border="0"></a>
               <%
                if(intFlagUserRol==1)
                {
                  hshValidateFraudCustomer=objCustomerService.getValidationFraudCustomer(objCustomerBean1.getSwRuc());
                  if(hshValidateFraudCustomer.get("strMessage")!=null){
                    %>
                      alert("<%=hshValidateFraudCustomer.get("strMessage")%>");
                    <%
                  }
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
      <td align="center" class="CellContent">&nbsp;<%=MiUtil.getString(objCustomerBean1.getSwRuc())%></td>
      <input type="hidden" name="txtRucDisabled" value="<%=MiUtil.getString(objCustomerBean1.getSwRuc())%>">
      <td align="center" class="CellContent">&nbsp;<%=MiUtil.getString(objCustomerBean1.getSwTipoPerson())%></td>
      <input type="hidden" name="txtSwTipoPerson" value="<%=MiUtil.getString(objCustomerBean1.getSwTipoPerson())%>">
      <td align="center" class="CellContent" colspan="2">&nbsp;<%=MiUtil.getString(objCustomerBean1.getSwType())%></td>
       <input type="hidden" name="txtSwType" value="<%=MiUtil.getString(objCustomerBean1.getSwType())%>">
   </tr>
   <tr>
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
                <td align="center" class="CellLabel">Carpeta Digital</td> <!-- EFH -->
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
               <input type="hidden" name="hdnUnkwnSiteId" value="0">
               </td>
                <td align="center" class="CellContent"><input type="checkbox" disabled="disabled" <% if(objOrderBean.getNpCarpetaDigital()!= null && objOrderBean.getNpCarpetaDigital().equals("S")){ %> checked="checked" <%} %>/></td> <!-- EFH -->
               <td align="center" class="CellContent">&nbsp;<%=MiUtil.getString(objCustomerBean1.getSwCodBscs())%>
               <input type="hidden" name="hdnCodBscsDetail" value="<%=MiUtil.getString(objCustomerBean1.getSwCodBscs())%>">
               </td>
               <td align="center" class="CellContent">&nbsp;<%=MiUtil.getString(objCustomerBean1.getSwRating())%>
               <input type="hidden" name="hdnScoring" value="">
               </td>
               <td align="center" class="CellContent">&nbsp;<%=String.valueOf(lLineaCredito)%>
               </td>
               <td align="center" class="CellContent">&nbsp;<%=MiUtil.getString(objSiteBean.getSwRegionName())%>               
               </td>
               <td align="center" class="CellContent">&nbsp;<%=MiUtil.getString(objCustomerBean1.getNpCustomerRelationType())%></td>
               <td align="center" class="CellContent">
               &nbsp;<a href="javascript:getCustomerDetail()"><%=MiUtil.getString(objCustomerBean1.getNpCustomerRelationName())%></a></td>
            </tr>
         </table>
      </td>
   </tr>   
   <tr>
      <td colspan="6">
         <table id="table_address" name="table_address" border="0" cellspacing="1" cellpadding="0" width="100%">
         <tr>
            <td align="center" class="CellLabel" nowrap >Dirección</td>
            <td align="center" class="CellLabel">&nbsp;Distrito</td>
            <td align="center" class="CellLabel">&nbsp;Provincia</td>        
            <td align="center" class="CellLabel">&nbsp;Departamento</td>
            <td align="center" class="CellLabel">&nbsp;Tipo</td>                     
            </td>      
         </tr>
         <% HashMap hshMap = new HashMap(); 
         for (int i=0;i<arrAddress.size();i++) {
         hshMap = (HashMap)arrAddress.get(i);
         %>
         <tr>          
            <%if( !(Constante.TYPE_ADDRESS_ENTREGA.equals(MiUtil.getString((String)hshMap.get("wv_swtipodirec")).toUpperCase())) ){%>
               <td align="left" class="CellContent">&nbsp;<%=MiUtil.getString((String)hshMap.get("wv_swaddress1"))%>
               <input type=hidden name='address1' value="<%=MiUtil.getString((String)hshMap.get("wv_swaddress1"))%>" >
               <input type=hidden name='address1CmbId' value="<%=MiUtil.getString((String)hshMap.get("wv_swcityid"))%>" >
               </td>                               
               <td align="left" class="CellContent">&nbsp;<%=MiUtil.getString((String)hshMap.get("wv_swcity"))%></td>
               <td align="left" class="CellContent">&nbsp;<%=MiUtil.getString((String)hshMap.get("wv_swprovince"))%></td>
               <td align="left" class="CellContent">&nbsp;<%=MiUtil.getString((String)hshMap.get("wv_swstate"))%></td>
               <td align="left" class="CellContent">&nbsp;<%=MiUtil.getString((String)hshMap.get("wv_swtipodirec"))%></td>
            <%}%>   
         </tr>
         <%}%>                     
         <tr>
					<%
					  //Constante.SPEC_ARTICULOS_VARIOS:2033 es la categoria de orden rapida
					  //No coloca nada en la direccion entrega en los siguientes casos:
					  //1) Es una orden rapida que solicito que se ingrese direccion (campo marcado con "S") o
					  //2) Es una orden rapida de un cliente generico (en esos guarda DNI/RUC)
					  if 	((objOrderBean.getNpSpecificationId()==Constante.SPEC_ARTICULOS_VARIOS && MiUtil.getString(objOrderBean.getNpstatustmp()).equals("S")) ||
							(objOrderBean.getNpSpecificationId()==Constante.SPEC_ARTICULOS_VARIOS && objOrderBean.getCsbCustomer().getSwCustomerId()==9999)){						  
							
					%> 
						<td align="left" class="CellContent">&nbsp;</td>						
						<td align="left" class="CellContent">&nbsp;</td>
						<td align="left" class="CellContent">&nbsp;</td>
						<td align="left" class="CellContent">&nbsp;</td>
						<td align="left" class="CellContent">&nbsp;ENTREGA</td> 
					<%}else{%>
						<td align="left" class="CellContent">&nbsp;<%=MiUtil.getString(objOrderBean.getNpShipToAddress1())%></td>
						<td align="left" class="CellContent">&nbsp;<%=MiUtil.getString(objOrderBean.getNpShipToCity())%></td>
						<td align="left" class="CellContent">&nbsp;<%=MiUtil.getString(objOrderBean.getNpShipToProvince())%></td>
						<td align="left" class="CellContent">&nbsp;<%=MiUtil.getString(objOrderBean.getNpShipToState())%></td>
						<td align="left" class="CellContent">&nbsp;ENTREGA</td> 					
					<%}%>
         </tr>              

         <!-- PRY-1140 LGONZALES Added the section (PRY-1049  | INICIO: AM0001)-->
         <!-- PRY-1049  | INICIO: AM0001-->
         <%if (objOrderBean.getNpuseinfulladdress()!=null && !"".equals(objOrderBean.getNpuseinfulladdress())) {%>
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
<script>
    function displayContactsSection(display) {
        var divContacts = document.getElementById("divContacts");

    	if (display) {
    	    divContacts.style.display = "inline";
    	} else {
    	    divContacts.style.display = "none";
    	}
    }

</script>
<!-- Sección contactos -->
<div id="divContacts" style="display:inline">
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
        <td align="center" class="CellContent"><input type="checkbox" name="chkContactNotification" id="chkContactNotification" style="border-bottom:0px;border-left:0px;border-top:0px;border-right:0px;background-color:transparent;width:50px;" disabled="disabled" <%=checkNotification%>/></td> <!-- EFH -->
        <td align="center" class="CellContent"><input type="text" name="txtContactFirstName" id="txtContactFirstName" style="border-bottom:0px;border-left:0px;border-top:0px;border-right:0px;background-color:transparent;width:180px;" readOnly="readonly" value="<%=MiUtil.getString(orderContactBean.getFirstName())%>"></td>
        <td align="center" class="CellContent"><input type="text" name="txtContactLastName" id="txtContactLastName" style="border-bottom:0px;border-left:0px;border-top:0px;border-right:0px;background-color:transparent;width:180px;" readOnly="readonly" value="<%=MiUtil.getString(orderContactBean.getLastName())%>"></td>
        <td align="center" class="CellContent"><input type="text" name="txtContactDocumentType" id="txtContactDocumentType" style="text-align:center;border-bottom:0px;border-left:0px;border-top:0px;border-right:0px;background-color:transparent;width:75px;" readOnly="readonly" value="<%=MiUtil.getString(orderContactBean.getDocumentType())%>"></td>
        <td align="center" class="CellContent"><input type="text" name="txtContactDocumentNumber" id="txtContactDocumentNumber" style="text-align:center;border-bottom:0px;border-left:0px;border-top:0px;border-right:0px;background-color:transparent;width:100px;" readOnly="readonly" value="<%=MiUtil.getString(orderContactBean.getDocumentNumber())%>"></td>
        <td align="center" class="CellContent"><input type="text" name="txtContactPhoneNumber" id="txtContactPhoneNumber" style="text-align:center;border-bottom:0px;border-left:0px;border-top:0px;border-right:0px;background-color:transparent;width:100px;" readOnly="readonly" value="<%=MiUtil.getString(orderContactBean.getPhoneNumber())%>"></td>
        <!--DERAZO REQ-0940-->
        <td align="center" class="CellContent"><input type="text" name="txtContactEmail" id="txtContactEmail" style="border-bottom:0px;border-left:0px;border-top:0px;border-right:0px;background-color:transparent;width:250px;" readOnly="readonly" value="<%=MiUtil.getString(orderContactBean.getEmail())%>"></td>
    </tr>
    <tr>
        <td align="left" class="CellLabel">Referencia</td>
        <td align="left" class="CellContent" colspan="6"><input type="text" name="txtReference" id="txtReference" style="border-bottom:0px;border-left:0px;border-top:0px;border-right:0px;background-color:transparent;width:98%;" readOnly="readonly" value="<%=MiUtil.getString(objOrderBean.getNpShipToReference())%>"></td>
    </tr>
</table>
</div>
<!-- Fin [N_O000017567] MMONTOYA -->
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
   <!--Fin de JP_ORDER_CUSTOMER_DETAIL.jsp -->