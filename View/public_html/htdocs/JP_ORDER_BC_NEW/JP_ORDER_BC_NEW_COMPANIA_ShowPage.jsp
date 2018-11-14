<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@ page import="oracle.portal.provider.v2.ProviderSession" %>
<%@ page import="oracle.portal.provider.v2.ProviderUser" %>
<%@ page import="oracle.portal.provider.v2.url.UrlUtils" %>
<%@ page import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil" %>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>

<%@page import="pe.com.nextel.exception.SessionException" %>
<%@page import="pe.com.nextel.util.MiUtil"%>

<%@ page import="pe.com.nextel.bean.PortalSessionBean" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.service.SessionService"%>
<%@ page import="pe.com.nextel.service.GeneralService"%>

    
<%
try{
  
  System.out.println("INICIO SESSION - ORDER NEW COMPANIA * ");
  String strCompanySessionId = "";

 try{
    PortletRenderRequest pReq = (PortletRenderRequest) request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
    ProviderUser objetoUsuario1 = pReq.getUser();
    strCompanySessionId=objetoUsuario1.getPortalSessionId();
    System.out.println("Sesión capturada  JP_ORDER_NEW_COMPANIA_ShowPage : " + objetoUsuario1.getName() + " - " + strCompanySessionId );
  }catch(Exception e){
    System.out.println("Portler Not Found : " + e.getClass() + " - " + e.getMessage() );
    out.println("Portlet JP_ORDER_NEW_COMPANIA_ShowPage Not Found");
    return;
  }

  //strCompanySessionId = "123462176630182334072688032086835887998102398";
 
  System.out.println("Sesión capturada después del resuest : " + strCompanySessionId );
	PortalSessionBean portalSessionBean2 = (PortalSessionBean)SessionService.getUserSession(strCompanySessionId);
	if(portalSessionBean2==null) {
    System.out.println("No se encontró la sesión de Java ->" + strCompanySessionId);
		throw new SessionException("La sesión finalizó");
	}
  
  System.out.println("FIN SESSION - ORDER NEW COMPANIA");
  GeneralService objGeneralService = new GeneralService();
  
   int iUserId=0;
   int iAppId=0;   

   iUserId  = portalSessionBean2.getUserid();
   iAppId   = portalSessionBean2.getAppId();  
  
  int iPermission=0;
  HashMap hshData = new HashMap();
  String strMessage=null;
  hshData  = objGeneralService.getRol(Constante.SCRN_OPTTO_NEW_PROSPECT, iUserId, iAppId);
  strMessage = (String)hshData.get("strMessage");
  if ( strMessage != null ) throw new Exception(strMessage);
  iPermission  = MiUtil.parseInt((String)hshData.get("iRetorno")); 

%>
  <p>
    <input type="hidden" name="myaction"/>
    <%//=UrlUtils.htmlFormHiddenFields(pReq, UrlUtils.PAGE_LINK, formName)%>
    <!-- Recuperando las variables de session -->
    <!--<input type="hidden" name="portaluser" value="<%//=strSessionId%>">-->
    <input type="hidden" name="portaluser" value="<%=portalSessionBean2.getNom_user()%>">
	
    <input type="hidden" name="detallemyaction">
    <!-- La dirección de entrega-->
    <input type=hidden name='hdnDeliveryAddress'>
    <input type=hidden name='hdnDeliveryCity'>
    <input type=hidden name='hdnDeliveryProvince'>
    <input type=hidden name='hdnDeliveryState'>
    <input type=hidden name="hdnNumberOfRespPago"> 
    
    <input type=hidden name='hdnDeliveryCityId'>
    <input type=hidden name='hdnDeliveryProvinceId'>
    <input type=hidden name='hdnDeliveryStateId'>
    <input type=hidden name='hdnTypeDocument'>
    <input type=hidden name='hdnDocument'>
    <input type="hidden" name="hndBillcycle">
    
  </p>
  <p>
  <table border="0" cellspacing="2" cellpadding="0" width="80%" align="center" height="5">
       <tr align="center"><td></td></tr>
  </table>
  <table border="0" cellspacing="0" cellpadding="0" width="15%">
       <tr class="PortletHeaderColor"> 
          <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"></td>
          <td class="SubSectionTitle" align="left" valign="top">Compañía</td>
          <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"></td>
       </tr>
  </table>	
  
  <table border="0" cellspacing="1" cellpadding="2" width="100%" class="RegionBorder">
       <tr>
           <td>
               <table width="100%" border="0" cellspacing="0" cellpadding="0" >
                    <td width="50%" align="center" class="CellLabel" >Busqueda por</td>
                    <td width="50%" align="center" class="CellLabel" ><font color="red"><b>*</b></font>
                    &nbsp;Dato</td>
                    <!--  Muy importante a ser implementado el invocar a servlets por Links--> 
                    
               </table>
           </td>
           
           <td class="CellLabel" colspan="2">
               <table width="100%" border="0" cellspacing="0" cellpadding="0" >
                  <tr >
                     <td    width="80%" class="CellLabel" align="left"><font color="red"><b>*</b></font>
                     <%if(request.getParameter("customerId")!=null){%>
                     <a>&nbsp;Razón Social</a>
                     <%}else{%>
                     <a href="javascript:searchConpany();">&nbsp;Razón Social</a>
                     <%}%>
                     </td>
                     
                       <!--
                        <td width="20%" class="CellLabel" align="center">
                        <img src="websales/images/CustValue/CustValue.gif" border="0" align="absbottom"></a></td>-->
                       <!--   WEBSALES.PLI_CUST_VALUE_IMG(NULL); -->
                     
                     <!--Source Programme: WEBSALES.NPSL_CUSTOMER_EXT_PL_PKG.PL_CUST_VALUE_IMG, Start: 20/07/2007 09:11:49-->

                     <script language="languaje">
                     function fxValidateRol(v_customerid){
                        var v_flag = 0;
                        if (v_flag == 1){
                           url = "/portal/pls/portal/WEBSALES.NPSL_CUSTOMER_EXT_PL_PKG.PL_CUST_VALUE_DETAIL?an_customerid=" + v_customerid;
                           url = "/portal/pls/portal/WEBSALES.NPSL_GENERAL_PL_PKG.WINDOW_FRAME?av_title="+escape("ValorizaciÃ³n > SegmentaciÃ³n Detalle")+"&av_url="+escape2(url);
                           WinAsist = window.open(url,"WinAsist","toolbar=no,location=0,directories=no,status=yes,menubar=0,scrollbars=no,resizable=no,screenX=100,top=80,left=100,screenY=80,width=750,height=350,modal=yes");
                        }
                        else{
                           return;
                        }
                     }
                     function fxCleanImgCustValue() {
                        parent.mainFrame.divCustValue.innerHTML = "<img src='<%=Constante.PATH_APPORDER_SERVER%>/images/CustValue/CustValue.gif' border=0 align='texttop'>" ;
                     }
                     </script>
                  
            
                        <td align="right" valign="top" width="56" class="CellContent">
                           <div id="divCustValue" name="divCustValue" style="display:inline">
                              <img id="imgcustvalue" name="imgcustvalue" src="<%=Constante.PATH_APPORDER_SERVER%>/images/CustValue/CustValue.gif" border="0" align="texttop"> 
                           </div>
                        </td>
                    
                     <!--Source Programme: WEBSALES.NPSL_CUSTOMER_EXT_PL_PKG.PL_CUST_VALUE_IMG, Finish: 20/07/2007 09:11:49-->
                     
                  </tr>
                </table>
            </td>
            
            <td align="center" class="CellLabel" colspan="2">Nombre Comercial</td>
            <td align="center" class="CellLabel">Ruc/DNI/Otro</td>
         </tr>
         
         <tr>
            <td align="center" class="CellContent">
               <table border="0" cellspacing="1" cellpadding="0" width="99%">
                  <tr>
                     <td width="50%" align="center" class="CellContent">
                      
                        <select name="cmbTipoBusqueda" class="TextBlack">
                        <% 
                           HashMap objHashMapSearchCustomer = new HashMap();
                           
                           objHashMapSearchCustomer = objGeneralService.GeneralDAOgetComboList("ORDER_TYPE_SEARCH_CUSTOMER");
                           if( objHashMapSearchCustomer.get("strMessage") != null ){ %>
                           <script>alert("<%=objHashMapSearchCustomer.get("strMessage")%>")</script>
                           <%}else{
                           ArrayList l = (ArrayList)objHashMapSearchCustomer.get("objArrayList");
                           if ( l != null ){
                           
                               for ( int i=0; i<l.size(); i++ ){
                                    HashMap h = (HashMap)l.get(i); %>
                                    
                                    <option value='<%=h.get("wn_npvalue")%>'>
                                    <%=h.get("wv_npvaluedesc")%>
                                    </option> <%
                              }
                           }
                           }
                         %>
                         </select>
                      
                     </td>
                     <td width="50%" align="center" class="CellContent" >
                        &nbsp;<input type="text" name="txtDato" size="12" onKeyDown="javascript:fValidatePhone(this); checkKey_BuscarCliente('txtDato',event,11);">
                     </td>
                    
                  </tr>
                  <script>
                     document.frmdatos.txtDato.focus();
                  </script>
               </table>
            </td>
            
            <td align="center" colspan="2" class="CellContent">
            
               <table width="100%" border="0" cellspacing="0" cellpadding="0" >
                  <tr >
                     <td width="90%" class="CellContent" align="left">&nbsp;<input type="text" value="" style="text-transform:uppercase" name="txtCompany" size="40" maxlength="60" onKeyDown="javascript:checkKey_BuscarCliente('txtCompany',event,11);" > 
                     <%if (iPermission==1){%><a href="javascript:fxShowNewProspect();">Nuevo Prospecto</a><%}%></td>
                     <td width="10%" class="CellContent" align="center"><a href="javascript:getCustomerDetail2()"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/viewDetail.gif" alt="Detalle de cliente" width="15" height="15" border="0"></a></td>
                     <!--   WEBSALES.PLI_CUST_VALUE_IMG(NULL); -->
                  
                  </tr>
                </table>
                
               
            
            </td>
            <td align="center" colspan="2" class="CellContent">&nbsp;<input type="text" name="txtCompanyNameCom" size="35"  maxlength="50" value="" readOnly></td>
            <td align="center" class="CellContent">&nbsp;
            <%if(request.getParameter("customerId")!=null){%>
              <input type="text" name="txtCampoOtro" disabled size="10" style="text-align:center" maxlength="15" value="" onKeyPress="javascript:checkKey_BuscarCliente('txtCampoOtro',event,11);"></td>
            <%}else{%>
              <input type="text" name="txtCampoOtro" size="10" style="text-align:center" maxlength="15" value="" onKeyPress="javascript:checkKey_BuscarCliente('txtCampoOtro',event,11);"></td>
            <%}%>
         </tr>
         
         <tr >
            <td>
               <table width="100%" border="0" cellspacing="1" cellpadding="0" >
                  <tr >
                    <td width="50%" align="center" height="17" class="CellLabel" >Tipo Compañía</td>
                    <td width="50%" align="center" height="17" class="CellLabel" >Tipo Cuenta</td>
                  </tr>
                </table>
            </td>
            <td align="center" height="17" class="CellLabel" colspan="2">Industria</td>
            <td align="center" class="CellLabel" >Region Cia</td>
            <td align="center" class="CellLabel">Teléfono</td>
            <td align="center" class="CellLabel">Fax</td>
         </tr>
         <tr>
            <td class="CellContent" width="260">
                <table width="100%" border="0" cellspacing="0" cellpadding="0" >
                  <tr >
                    <td align="center" height="17" class="CellContent" colspan="1">&nbsp;<input type="text" name="txtTipoCompania" size="12" style="text-align:center" maxlength="20" value=""  readOnly></td>
                    <td align="center" height="17" class="CellContent" colspan="1">&nbsp;<input type="text" name="txtTipoCuenta" size="12" style="text-align:center" maxlength="20" value="" readOnly></td>
                  </tr>
                </table>
               
              <!--
               Bypass a Consultores
              
               IF WEBSALES.NPSL_ACCESS_PKG.FX_GET_ROL(2050, 0, 'XX') <> 1 THEN  
                  IF WEBSALES.NPSL_ACCESS_PKG.FX_GET_ROL(2051, 0, 'XX') <> 1 THEN
                     htp.p('                        
                           <a href="javascript:getCustomerDetail2()"><img src="/websales/images/viewDetail.gif" width="15" height="15" border="0"></a>
                           ');
                  END IF;
               END IF;
              
              Fin Bypass a Consultores -->
              
              <input type="hidden" name="hdnCustomerName">
              <input type="hidden" name="txtCompanyId" size="10" onfocus="blur()" value="">
              <input type="hidden" name="hdnCustomerIdBscs" value="">
            </td>
            <td align="left" colspan="2" class="CellContent">&nbsp;<input type="text" name="txtIndustria" style="text-align:center" size="40" maxlength="50" value="" readOnly></td>
            <td align="center" class="CellContent">
               <input type="text" name="txtRegion" size="10" style="text-align:center" maxlength="15" value="" readOnly>

            </td>
            <td align="center" class="CellContent">&nbsp;<input type="text" name="txtTelefono" size="10" style="text-align:center" maxlength="20"  readOnly></td>
            <td align="center" class="CellContent">&nbsp;<input type="text" name="txtFax" size="10" style="text-align:center" maxlength="20"  readOnly></td>
         </tr>
         <tr>
            <td colspan="6">
               <table border="0" cellspacing="1" cellpadding="0" width="100%">
                  <tr>
                     <td align="center" class="CellLabel" nowrap colspan=2 >Responsable de Pago&nbsp;
                        <!--
                        IF WEBSALES.NPSL_ACCESS_PKG.FX_GET_ROL(2451,wns_userid,wns_appid) = 1 THEN
                           DECLARE
                              wn_obj_related    NUMBER := 1;
                           BEGIN
                              htp.p('-->
                              <div id="unkwnSiteInput">
                              <div id="unkwnSiteArea"><input type="CheckBox" name="chkUnkwnSite" value="0" onClick="fxSetUnknwnSiteChkBox()"></div>
                              <a href="javascript:fxInputProspectSiteData();" onmouseover="window.status='Ingresar datos de Site prospecto'; return true" onmouseout="window.status='';"><font class="Text07">Datos de Site Nuevo</font></a>
                              </div>
                              <input type="hidden" name="hdnUnkwnSiteId"      value="0">
                              <input type="hidden" name="hdnUnkwnSiteFlg"     value="0">
                              <input type="hidden" name="hdnPrevSiteId"       value="">
                              <input type="hidden" name="hdnSellerRegionId"   value="0">
                              <input type="hidden" name="hdnUnknwsiteadress"   value="">
                              <script>

                                 function fxSetUnknwnSiteChkBox() {
                                    var Form = document.frmdatos;
                                    if (Form.chkUnkwnSite.checked) {
                                       fxCheckRootAcc();
                                    }else {
                                       Form.chkUnkwnSite.checked = false;
                                       Form.hdnUnkwnSiteFlg.value = 0;
                                       Form.txtDirEntrega.value =  form.hdnUnknwsiteadress.value;
                                    }
                                 }

                                 function fxShowUnkwnSiteChkBox() {
                                    unkwnSiteArea.className="show";
                                 }
                                 function fxHideUnkwnSiteChkBox() {
                                    unkwnSiteArea.className="hidden";
                                 }
                                 function fxShowUnkwnSiteInput() {
                                    unkwnSiteInput.className="show";
                                 }
                                 function fxHideUnkwnSiteInput() {
                                    var Form = document.frmdatos;
                                    Form.chkUnkwnSite.checked = false;
                                    Form.hdnUnkwnSiteFlg.value = 0;
                                    unkwnSiteInput.className="hidden";
                                 }

                                 function fxCheckRootAcc() {
                                    var Form = document.frmdatos;
                                    var customerid = Form.txtCompanyId.value;
                                    var url = "/portal/pls/portal/!WEBSALES.NPSL_ORDER_NEW_PL_PKG.PL_VALIDATE_UNKNWN_SITE?an_customerid="+customerid+"&an_siteid=&an_unknwnsiteid="+Form.hdnUnkwnSiteId.value+"&an_regionid="+Form.hdnSellerRegionId.value;
                                    parent.bottomFrame.location.replace(url);
                                 }

                                 fxHideUnkwnSiteInput();
                                 parent.mainFrame.document.frmdatos.chkUnkwnSite.checked = false;
                                 parent.mainFrame.document.frmdatos.hdnUnkwnSiteFlg.value = 0;
                                 
                                 function fx_cmbResp() {
                                 var Form = document.frmdatos;
                                 if (form.cmbResponsablePago.value != ""){                                
                                  form.txtCodBSCS.value = form.hdnCodBSCS.value;
                                  } 
           
                                 if (Form.siteCheck == null) {                                 
                                 }
                                 else if (Form.siteCheck.checked) {
                                       Form.siteCheck.checked = false
                                    }
                                 }
                                 
                                 function fxInputProspectSiteData() {
                                    var Form = document.frmdatos;
                                    var customerid = Form.txtCompanyId.value;
                                    if (customerid != "") {
                                       if (Form.hdnUnkwnSiteId.value == 0 ) { // Site nuevo no fue ingresado
                                          Form.cmbSite.selectedIndex = 0;  // Si Site estÃ¡ seleccionado de Combo, se quita
                                          var page_params = escape("an_customerId=" + customerid+"&an_regionid="+Form.hdnSellerRegionId.value);
                                          var page_url = "/portal/pls/portal/WEBSALES.NPSL_SITE_PL_PKG.PL_NEW_UNKNOWN_SITE";
                                          var url = "/portal/pls/portal/WEBSALES.NPSL_NEW_GENERAL_PL_PKG.PL_FRAME?av_page_url="+page_url+"&av_page_params="+page_params;
                                          WinAsist = window.open(url,"WinAsist","toolbar=no,location=no,directories=no,status=no,menubar=0,scrollbars=no,resizable=no,screenX=100,top=80,left=100,screenY=80,width=700,height=500,modal=yes");
                                       }else { // Datos de Site nuevo ya fueron ingresados, se debe editar
                                          var page_params = escape("customerid=" + customerid+"&siteid="+Form.hdnUnkwnSiteId.value+"&action=R"+"&an_regionid="+Form.hdnSellerRegionId.value);
                                          var page_url = "/portal/page/portal/nextel/PROSPECT_SITE";
                                          var url = "/portal/pls/portal/WEBSALES.NPSL_NEW_GENERAL_PL_PKG.PL_FRAME?av_page_url="+page_url+"&av_page_params="+page_params;
                                          WinAsist = window.open(url,"WinAsist","toolbar=no,location=no,directories=no,status=no,menubar=0,scrollbars=no,resizable=no,screenX=100,top=80,left=100,screenY=80,width=700,height=500,modal=yes");
                                       }
                                    }else {
                                       alert("Ingrese N° Nextel o Compañía")
                                    }
                                 }
                              </script>
                              <!--');
                           END;
                        END IF;-->
                       
                     </td>
                     <td align="center" class="CellLabel">&nbsp;<a href="javascript:fCopyCodBSCS();">Código BSCS</a></td>
                     <td align="center" class="CellLabel">&nbsp;Scoring</td>        
                     <td align="center" class="CellLabel">&nbsp;Línea Cred. PEN</td><!--FES Se cambió US$ por PEN -->
                     <td align="center" class="CellLabel">&nbsp;Región Site</td>
                     <td align="center" class="CellLabel">&nbsp;Tipo Cliente</td>
                     <td align="center" class="CellLabel">&nbsp;Cliente Principal</td>
                  </tr>
                  <tr>
                     <td align="center" class="CellContent" colspan=2>&nbsp;
                        <select name="cmbResponsablePago" onchange="SelectSiteData(this); javascript:fx_cmbResp()">
                           <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
                        </select>   
                        <input type="hidden" name="hdnSite" value="">
                        <input type="hidden" name="hdnClientSitesFlag" value="0">
                     </td>
                     
                     <td align="center" class="CellContent">&nbsp;<input type="text" name="txtCodBSCS" size="15" style="text-align:center" maxlength="50" value="" readOnly></td>
                     <input type="hidden" name="hdnCodBSCS" value=""/>
                     <td align="center" class="CellContent">&nbsp;<input type="text" name="txtScoring" size="12" style="text-align:center" maxlength="20" onfocus="blur()" value=""></td>
                     <td align="center" class="CellContent">&nbsp;<input type="text" name="txtLineaCredito" size="12" maxlength="20" style="text-align:right" value="" readOnly></td>
                     <td align="center" class="CellContent">&nbsp;<input type="text" name="txtRegionSite" size="10" style="text-align:center" maxlength="20" value="" readOnly></td>
                     <td align="center" class="CellContent">&nbsp;<input type="text" name="txtTipoCliente" size="12" style="text-align:center" maxlength="20" value="" readOnly></td>
                     <td align="center" class="CellContent">&nbsp;<input type="text" name="txtClientePrincipal" size="10" style="text-align:center" maxlength="20" value="" readOnly></td>
                  </tr>
               </table>
            </td>
         </tr>
         
         <tr>
            <td colspan="6">
               <table id="table_address" name="table_address" border="0" cellspacing="1" cellpadding="0" width="100%">
                  <tr>
                     <td align="center" class="CellLabel" nowrap >Direcci&oacute;n</td>
                     <td align="center" class="CellLabel">&nbsp;Distrito</td>
                     <td align="center" class="CellLabel">&nbsp;Provincia</td>        
                     <td align="center" class="CellLabel">&nbsp;Departamento</td>
                     <td align="center" class="CellLabel">
                     <table>
                        <tr>
                        <td align="center" class="CellLabel" colspan="3">TIPO DIRECCION</td>
                        </tr>
                     </table>
                     </td>
                     
                  </tr>
                  <tr>
                     <td align="center" class="CellContent">&nbsp;</td>
                     <td align="center" class="CellContent">&nbsp;</td>
                     <td align="center" class="CellContent">&nbsp;</td>
                     <td align="center" class="CellContent">&nbsp;</td>
                     <td align="center" class="CellContent">&nbsp;</td>
                 </tr>
              </table>       
           </td>
         </tr>
         <tr>
            <td colspan="6">
               <table  id="table_contacts" name="table_contacts"  border="0" cellspacing="1" cellpadding="0" width="100%">
                  <tr>
                     <td align="center" class="CellLabel">&nbsp;Apellidos y Nombres&nbsp;</td>
                     <td align="center" class="CellLabel">&nbsp;Area</td>
                     <td align="center" class="CellLabel">&nbsp;Telefono</td>
                     <td align="center" class="CellLabel">&nbsp;Area</td>        
                     <td align="center" class="CellLabel">&nbsp;Fax</td>
                     <td align="center" class="CellLabel">
                     <table>
                        <tr>
                        <td align="center" class="CellLabel">USR.</td>
                        <td align="center" class="CellLabel">FAC.</td>
                        <td align="center" class="CellLabel">GC.</td>
                        </tr>
                     </table>
                     </td>
                  </tr>
                  
                  <tr>
                     <td align="center" class="CellContent">&nbsp;</td>
                     <td align="center" class="CellContent">&nbsp;</td>
                     <td align="center" class="CellContent">&nbsp;</td>
                     <td align="center" class="CellContent">&nbsp;</td>
                     <td align="center" class="CellContent">&nbsp;</td>
                     <td align="center" class="CellContent">&nbsp;</td>
                  </tr>

               </table>
            </td>
         </tr>
         
        		  
      </table>
      <!-- DATOS COMERCIALES - FIN -->
   
<%}catch(SessionException se) {
    System.out.println("[JP_ORDER_NEW_COMPANIA][SessionException] : " + se.getClass() + " - " + se.getMessage());
    se.printStackTrace();
    //out.println("<script>alert('Su sesión ha finalizado. Ingrese nuevamente');</script>");
    //String strUrl = "/portal/pls/portal/WEBSALES.NP_USER_AVAILABILITY_PL_PKG.PL_LOGOUT";
     //out.print("<script>parent.mainFrame.location.replace('"+strUrl+"');</script>");
  }catch(Exception e) {
    String strMessageExceptionGeneralStart = "";
    strMessageExceptionGeneralStart = MiUtil.getMessageClean(e.getMessage());
    System.out.println("[JP_ORDER_NEW_COMPANIA][Exception] : " + e.getClass() + " - " + e.getMessage());
    out.println("<script>alert('"+strMessageExceptionGeneralStart+"');</script>");   
  }
%>
