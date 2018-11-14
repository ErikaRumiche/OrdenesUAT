<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@page import="oracle.portal.provider.v2.ProviderUser" %>
<%@page import="oracle.portal.provider.v2.ProviderSession" %>
<%@page import="oracle.portal.provider.v2.url.UrlUtils" %>
<%@page import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil" %>
<%@page import="pe.com.nextel.dao.*,pe.com.nextel.bean.*" %>
<%@page import="pe.com.nextel.service.SessionService"%>
<%@page import="pe.com.nextel.exception.SessionException" %>
<%@page import="pe.com.nextel.util.MiUtil"%>
<%@page import="java.util.*"%>
<%@page import="pe.com.nextel.util.Constante" %>

<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>

<%
  try{
   /* Inicio - comentar para probar localmente*/
   System.out.println("INICIO SESSION - ORDER NEW STAR");

   String strSessionId1  = "";

  try{
    PortletRenderRequest pReq = (PortletRenderRequest) request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
    ProviderUser objetoUsuario1 = pReq.getUser();
    strSessionId1=objetoUsuario1.getPortalSessionId();
    System.out.println("Sesión capturada  JP_ORDER_NEW_START_ShowPage : " + objetoUsuario1.getName() + " - " + strSessionId1 );
  }catch(Exception e){
    System.out.println("Portler Not Found : " + e.getClass() + " - " + e.getMessage() );
    out.println("Portlet JP_ORDER_NEW_START_ShowPage Not Found");
    return;
  }

  //strSessionId1 = "655718738877632405544903018086691539998102659";
  //strSessionId1 = "805809601574955595205364047086969473998102872";//Consultor
  
  System.out.println("Sesión capturada después del resuest : " + strSessionId1 );
	PortalSessionBean portalSessionBean1 = (PortalSessionBean)SessionService.getUserSession(strSessionId1);
	if(portalSessionBean1==null) {
    System.out.println("No se encontró la sesión de Java ->" + strSessionId1);
		throw new SessionException("La sesión finalizó");
	}
  
   String strGeneratortype     = request.getParameter("generatortype")==null?"":request.getParameter("generatortype");      
   System.out.println("[JP_ORDER_NEW_STAR_ShowPage][strGeneratortype]"+strGeneratortype);
   String strGeneratorId     =  request.getParameter("opportunityTypeId")==null?"":request.getParameter("opportunityTypeId");      
   System.out.println("[JP_ORDER_NEW_STAR_ShowPage][strGeneratorId]"+strGeneratorId);

	if (strGeneratorId==null || strGeneratorId.equals("")){
		strGeneratorId     =  request.getParameter("generatorid")==null?"":request.getParameter("generatorid");
		System.out.println("[JP_ORDER_NEW_STAR_ShowPage][strGeneratorId2]"+strGeneratorId);	
	}      
   
   int iUserId1  = portalSessionBean1.getUserid();	
   int iAppId1   = portalSessionBean1.getAppId();

   int iProGrpId = portalSessionBean1.getProviderGrpId();




   System.out.println("FIN SESSION - ORDER NEW STAR");

    String strRutaContext=request.getContextPath();
   
   String actionURL_CustomerServlet      = strRutaContext+"/customerservlet";
   String actionURL_OrderServlet         = strRutaContext+"/orderservlet";
   String actionURL_CustomerServlet_byId = strRutaContext+"/customerservlet";
   
   String URL_Order_CompaniaBuscar       = strRutaContext+"/htdocs/jsp/Order_CompaniaBuscar.jsp";
   String URL_Order_NextelListado        = strRutaContext+"/htdocs/jsp/Order_NextelListado.jsp";
   String URL_Order_CompaniaListar       = strRutaContext+"/htdocs/jsp/Order_CompaniaLista.jsp";
  %>
</p>

<script>var appContext = "<%=request.getContextPath()%>";</script>
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderNew.js"></script>
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderIXNew.js"></script>
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderGeneral.js"></script>
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderException.js"></script>
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderIXEdit.js"></script>
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/DateTimeBasicOperations.js"></script>
<script language="javaScript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/date-picker.js"></script>
 

<script>
   function fxInputTabEnter() {
		return ((window.event.keyCode == 9) || (window.event.keyCode == 13));
	 }

   function fxCargaSpecifications(){
   }
   
   function SelectLoadSpecification(objSpecification){
     form = document.frmdatos;
     
     /*INI - Conf. Defecto*/
     
     dvFechaFinProg.style.display="none";
     dvFechaReconexion.style.display="none";
     
     dvFechaFinProgInput.style.display="none";
     dvFechaReconexionInput.style.display="none";

     dvFechaProcAutN.style.display="";
     dvFechaProcAutP.style.display="none";
          
     /*FIN - Conf. Defecto*/
     
     if( objSpecification.value !=null && objSpecification.value !="" ){
     //Se debe de poner la Sección en blanco
     form.txtPropuesta.value='';//CBARZOLA 30/07/2009
     if(objSpecification.value == "2022" || objSpecification.value == "2023"){
        parent.mainFrame.linkCalendar1.href = "javascript:;";
     }else{
        parent.mainFrame.linkCalendar1.href = "javascript:show_calendar('frmdatos.txtFechaProceso',null,null,'DD/MM/YYYY');";
     }
     
     //Se agrego para suspensiones temporales rmartinez
     if(objSpecification.value == <%=Constante.SPEC_SUSPENSIONES[0]%>){
       dvFechaReconexion.style.display="";
       dvFechaReconexionInput.style.display="";
     }
          /*mlopezl - modif hpptt # 1 - 30/06/2010 - inicio*/
     if(objSpecification.value == <%=Constante.SPEC_SERVICIOS_ADICIONALES[0]%>){
       dvFechaFinProg.style.display="";
       dvFechaFinProgInput.style.display="";
     }
     /*mlopezl - modif hpptt # 1 - 30/06/2010 - fin*/
     
     /*dlazo - modif hpptt # - 14/12/2010 - inicio*/
     if(objSpecification.value == <%=Constante.SPEC_SSAA_PROMOTIONS%>){
       dvFechaProcAutN.style.display="none";
       dvFechaProcAutP.style.display="";
     }
     /*dlazo - modif hpptt # - 14/12/2010 - fin*/
     try{
      parent.mainFrame.IdSpecificationSections.innerHTML = "";
     }catch(e){}
       form.hdnSite.value   = form.cmbResponsablePago.value;
       try{
       form.hdncmbVendedorVoz.value = form.cmbVendedor[form.cmbVendedor.selectedIndex].value;
       }catch(e){}
       form.myaction.value  = "loadSpecificationsSections";       
       form.detallemyaction.value="loadSpecificationsSections"
       url = "<%=actionURL_OrderServlet%>?idSpecification="+objSpecification.value;
       form.action=url;
       form.submit();
        
     }else{
        try{
          fxHideDataFields();
        }catch(e){}
    }
   }

   function fxOrderOnload(){
      fxCargaSpecifications();
   }
         
   function doSubmit(myAction){
     form = document.frmdatos;
   
     if(myAction=='buscarCliente') {
       
      form.myaction.value="buscarCliente";
      form.detallemyaction.value="bpel";
      form.action="<%=actionURL_CustomerServlet%>";

        if (form.txtDato.value !="") {
            form.submit();
        }else {
            doSubmit("limpiarCliente");
        }  
        
      }else if(myAction=='limpiarCliente'){
         form.myaction.value="limpiarCliente";
         form.action="<%=actionURL_CustomerServlet%>";
         form.submit();
      }else if(myAction=='grabarOrden'){
               
         form.myaction.value="grabarOrden";
         form.hdnSpecification.value = form.cmbSubCategoria.value;
         
         //Traer los campos de la Categoría y SubCategoría y el Vendedor
         form.hdncmbVendedor.value    = form.cmbVendedor.options[form.cmbVendedor.selectedIndex].text;
         form.hdnVendedorId.value    = form.cmbVendedor.options[form.cmbVendedor.selectedIndex].value;
         //alert("hdncmbVendedor : " + form.hdncmbVendedor.value)
         form.hdncmbCategoria.value    = form.cmbCategoria.options[form.cmbCategoria.selectedIndex].text;
         //alert("hdncmbCategoria : " + form.hdncmbCategoria.value)
         form.hdncmbSubCategoria.value    = form.cmbSubCategoria.options[form.cmbSubCategoria.selectedIndex].text;
         //alert("hdncmbSubCategoria : " + form.hdncmbSubCategoria.value)
         
         form.hdnSpecification.value = form.cmbSubCategoria.value;
         //form.hdnSite.value = form.cmbResponsablePago.value;
         fxSetSites();
         
      //INICIO - VALIDACION DE PROPUESTAS:CBARZOLA 08/08/2009
         if(form.txtPropuesta.value!=''){
           spefification=form.cmbSubCategoria.value;
           vector =fxItemValidatePropuestas(vctItemsMainFrameOrder,spefification);
           vctItemPropAcum=fxGetResumItems(vector,spefification);
           cadena=fxGenerateTrama(vctItemPropAcum,spefification);
           //alert(cadena);
           form.hdnTrama.value=cadena;
           form.myaction.value="ValidationProposedOrder";
           form.action="<%=actionURL_OrderServlet%>";
           form.submit();
           return;
         }
         //FIN - VALIDACION DE PROPUESTAS
         
       if(fxInvocarCreditEvaluation(form.hdnSpecification.value, form.hdnGeneratorType.value)){
            
            //Se invoca antes de grabar la orden
            fxValidateApplyOrderVolume();
            
            form.btnSaveOrder.disabled = true;  
             form.action="<%=actionURL_OrderServlet%>";
             form.submit(); 
         }
         
       /*  if ((form.hdnSpecification.value == '2001' || form.hdnSpecification.value == '2013' || form.hdnSpecification.value == '2017') && (form.hdnGeneratorType.value!= 'MASSIVE')) {
          form.myaction.value="evaluarOrden";
          form.action="<%=actionURL_OrderServlet%>";          
          form.submit();
          return;
         }*/ // CBARZOLA:08/08/2009 COMENTADO PARA  UTILIZAR LA FUNCION fxInvocarCreditEvaluation
         //Deshabilitamos el botón de grabar. Si se encuentran errores se debe habilitar
        /* form.btnSaveOrder.disabled = true;
         form.action="<%=actionURL_OrderServlet%>";
         form.submit(); */ //CBARZOLA:08/08/2009 comentado para que funcione el grabado
                           
       }else if(myAction=='limpiarOrden'){
          form.myaction.value="limpiarOrden";
          form.action="<%=actionURL_OrderServlet%>";
          form.submit();
       }
    };

 //INICIO PRY-0864 | AMENDEZ --Se adiciono la especificacion 2009
 function fxInvocarCreditEvaluation(npspecificationid, npgeneratortype){
      var form = document.frmdatos;
      //if (npspecificationid == '2001' || npspecificationid == '2013' || npspecificationid == '2017') {
      if ((npspecificationid == '2001' || npspecificationid == '2068' || npspecificationid == '2013' || npspecificationid == '2017' || npspecificationid == '2065' || npspecificationid == '<%=Constante.SPEC_SSAA_SUSCRIPCIONES%>' || npspecificationid == '<%=Constante.SPEC_SSAA_PROMOTIONS%>' || npspecificationid == '2024') && (npgeneratortype!= 'MASSIVE')) {
        form.myaction.value="evaluarOrden"; 
        form.action="<%=actionURL_OrderServlet%>";          
        form.submit();
        return false;
      }
      return true;
  }
 //FIN PRY-0864 | AMENDEZ --Se adiciono la especificacion 2009

 /* function fxValidateCreditEvaluation(){
    var flagGrabar = false;
    form = document.frmdatos;
    form.myaction.value="grabarOrden";
    if (form.hdnValidateCredit.value == '1') {
      if(Section_EvaluacionAutomatica.style.display == 'none'){
          alert("No existe deposito en garantía");
          flagGrabar = true;
      }else{
          if (form.flagActionCredit.value==''){
            parent.bottomFrame.location.replace("<%=Constante.PATH_APPORDER_SERVER%>/Bottom.html");
            return;
          } else {
                if(form.flagActionCredit.value=='ACEPTAR'){
                    if (form.txtGuarantee.value > 0){
                      if (confirm("¿Acepta el Déposito en Garantía de: "+form.txtGuarantee.value+"?")){
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
      
      form.btnSaveOrder.disabled = true;
      form.action="<%=actionURL_OrderServlet%>";
      form.submit();
    }
  }
  */
  
  
   function fxValidateCreditEvaluation(){
    var flagGrabar = false;
    form = document.frmdatos;
    form.myaction.value="grabarOrden";
    
    if (form.hdnValidateCredit.value == '1') {
      if(Section_EvaluacionAutomatica.style.display == 'none'){
          //alert("No existe deposito en garantía");
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
    
      form.btnSaveOrder.disabled = true;
      form.action="<%=actionURL_OrderServlet%>";
      form.submit();
    }
  }
  
  function fxValidateApplyOrderVolume(){
    form = document.frmdatos;
    
    if(form.cmbSubCategoria.value == <%=Constante.SPEC_POSTPAGO_VENTA%> || form.cmbSubCategoria.value == <%=Constante.SPEC_PORTABILIDAD_POSTPAGO%>){
      
      if(fxIsOrderVolumeChecked()){
        if(confirm("¿Está seguro que desea aplicar promoción por volumen de orden?")){
          fxApplyOrderVolume();
        }
      }                                                                                                                             
    }
  }
   
	function searchConpany(){
		form = document.frmdatos;
		v_companhia = form.txtCompany.value;		


    var v_parametros = "?sUrl=<%=URL_Order_CompaniaBuscar%>"+"¿hdnIUserId="+<%=iUserId1%>+"|hdnIAppId="+<%=iAppId1%>+"|hdnSessionLevel=" + "<%=portalSessionBean1.getLevel()%>"+"|hdnSessionCode="+"<%=portalSessionBean1.getCode()%>"+"|hdnSessionLogin="+"<%=portalSessionBean1.getLogin()%>"+"|hdnProGrpId="+"<%=portalSessionBean1.getProviderGrpId()%>";


		url= "<%=request.getContextPath()%>/htdocs/jsp/PopUpCompanyFrame.jsp"+ v_parametros;



		WinAsist_Inc = window.open(url,"WinAsistCompanySearch","toolbar=no,location=0,directories=no,status=yes,menubar=0,scrollbars=no,resizable=no,screenX=100,top=80,left=100,screenY=80,width=700,height=500,modal=yes");
   };

	function searchCustomer(){
		form = document.frmdatos;
		v_companhia = form.txtCompany.value;
		//var v_parametros = "?sUrl=<%=URL_Order_CompaniaBuscar%>"+"¿hdnSessionLevel=" + "<%=portalSessionBean1.getLevel()%>"+"|hdnSessionCode="+"<%=portalSessionBean1.getCode()%>"+"|txtCompany="+v_companhia;     		
    var v_parametros = "?sUrl=<%=URL_Order_CompaniaBuscar%>"+"¿hdnIUserId="+<%=iUserId1%>+"|hdnIAppId="+<%=iAppId1%>+"|hdnSessionLevel=" + "<%=portalSessionBean1.getLevel()%>"+"|hdnSessionCode="+"<%=portalSessionBean1.getCode()%>"+"|hdnSessionLogin="+"<%=portalSessionBean1.getLogin()%>"+"|txtCompany="+v_companhia+"|hdnProGrpId="+"<%=portalSessionBean1.getProviderGrpId()%>";


		url= "<%=request.getContextPath()%>/htdocs/jsp/PopUpCompanyFrame.jsp"+ v_parametros;	
		form.txtCompany.value=""; //(similar a Dia a Dia - limpiar el campo)



		WinAsist = window.open(url,"WinSearchCustomer","toolbar=no,location=0,directories=no,status=yes,menubar=0,scrollbars=no,resizable=no,screenX=100,top=80,left=100,screenY=80,width=700,height=500,modal=yes");
   };

  function searchRuc(){
     form = document.frmdatos;
     v_companhia = form.txtCompany.value;
	  //v_parametros = "?hdnSessionLevel=" + "<%=portalSessionBean1.getLevel()%>" + "&hdnSessionCode=" + "<%=portalSessionBean1.getCode()%>" +  "&txtCampoOtro=" + form.txtCampoOtro.value + "";
     v_parametros = "?hdnSessionLevel=" + "<%=portalSessionBean1.getLevel()%>" + "&hdnSessionCode=" + "<%=portalSessionBean1.getCode()%>" + "&hdnSessionLogin=" + "<%=portalSessionBean1.getLogin()%>"+  "&txtCampoOtro=" + form.txtCampoOtro.value + ""+"|hdnProGrpId="+"<%=portalSessionBean1.getProviderGrpId()%>";
     url= "<%=URL_Order_CompaniaBuscar%>" + v_parametros;
     
     WinAsist = window.open(url,"WinAsist","toolbar=no,location=0,directories=no,status=yes,menubar=0,scrollbars=no,resizable=no,screenX=100,top=80,left=100,screenY=80,width=700,height=500,modal=yes");
  };
  
  function searchNextel(){
     form = document.frmdatos;
     v_nextel = form.txtDato.value;
     //v_parametros = "?hdnSessionLevel=" + form.hdnSessionLevel.value + "&hdnSessionCode=" + form.hdnSessionCode.value + "&txtDato=" + form.txtDato.value + "";
     v_parametros = "?txtDato=" + form.txtDato.value + "&portaluser=" + "'<%=portalSessionBean1.getNom_user()%>'";
     
     url= "<%=URL_Order_NextelListado%>" + v_parametros;
     
     WinAsist = window.open(url,"WinAsist","toolbar=no,location=0,directories=no,status=yes,menubar=0,scrollbars=no,resizable=no,screenX=100,top=80,left=100,screenY=80,width=700,height=500,modal=yes");
  };
  
  //JPEREZ
  function fxListCompania(){
    form = document.frmdatos;
    url= "<%=URL_Order_CompaniaListar%>";
    WinAsist = window.open(url,"WinAsist","toolbar=no,location=0,directories=no,status=yes,menubar=0,scrollbars=no,resizable=no,screenX=100,top=80,left=100,screenY=80,width=700,height=500,modal=yes");
  }
   
<!-- Por implementar -->
  function invocarServletBusqueda(){
     form = document.frmdatos;
     
     var v_myaction = form.myaction.value;
     var v_dato = form.txtDato.value;
     var v_tipobusqueda = form.cmbTipoBusqueda.value;
     var v_SessionLogin = form.hdnSessionLogin.value;
     var v_SessionRegionId = form.hdnSessionRegionId.value;
     
     var v_parametros = "?myaction=" + v_myaction + "&txtDato=" + v_dato + "&cmbTipoBusqueda=" + v_tipobusqueda + "&hdnSessionLogin=" + "<%=portalSessionBean1.getLogin()%>" + "&hdnSessionRegionId=" + <%=portalSessionBean1.getRegionId()%> + "";
     
     return v_parametros;
     
  };
  
  function checkTabKey(e){
     var CR= 9;
     var keycode = e.keyCode;
     return (keycode == CR);
  }
  
  function checkKey_BuscarCliente(tipo,e,num1){
  
    if(fxInputTabEnter()) {
     form = document.frmdatos;
     form.txtCompany.value      = form.txtCompany.value.toUpperCase();
     form.hdnCompanyName.value  = form.txtCompany.value.toUpperCase();
     form.txtPropuesta.value=''; //CBARZOLA 30/07/2009
     


     try{
     parent.mainFrame.IdSpecificationSections.innerHTML = "";
     }catch(e){}
  
       if (num1==10 || num1==11) {
          if(tipo == "txtDato") {
              doSubmit("buscarCliente"); 
          }
          
          if(tipo == "txtCompany") {
              


            if( document.frmdatos.txtCompany.value != "" ){
              form.txtPropuesta.value='';//CBARZOLA 30/07/2009
              form.myaction.value = 'buscarCliente';
              form.detallemyaction.value = 'compania';
              form.hdnCustId.value = '';
              form.action="<%=actionURL_CustomerServlet%>";
              form.submit();
            }else{
              doSubmit("limpiarCliente");
            }
            
          }
          
          if(tipo == "txtCampoOtro") {
          
              document.frmdatos.myaction.value = 'buscarCliente';
              document.frmdatos.detallemyaction.value = 'ruc';
              form.action="<%=actionURL_CustomerServlet%>";
              form.txtPropuesta.value='';//CBARZOLA 30/07/2009
              
              document.frmdatos.submit();        
  
       }
          
       //};
       if (num1==12)  {
       
          if(tipo == "txtDato") { 
              doSubmit("buscarCliente"); 
          }
          if(tipo == "txtCompany") {
              alert('Especifique mejor su busqueda');
              searchCustomer(); 
          }
          if(tipo == "txtCampoOtro") {
              searchCustomer(); 
          }
          
       };
     }
    }
  }
  function getCustomerDetail2(){
     form = document.frmdatos;
     customerId = form.txtCompanyId.value;
     
     if (customerId=="" || customerId=="0" ){
        alert("Antes de ver el detalle debe de haber elegido un cliente");
        return;
     }
     url = "/portal/page/portal/nextel/CUST_DETAIL?customerId=" + customerId;
     url = "/portal/pls/portal/WEBSALES.NPSL_GENERAL_PL_PKG.WINDOW_FRAME?av_title="+escape("PORTAL NEXTEL")+"&av_url="+escape(url);
     window.open(url,"WinCustomer","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,screenX=60,top=40,left=60,screenY=40,width=850,height=600");
     return;
  }
  
  function fxShowNewProspect(){
  v_razonSocial = "";
  v_typeDocument = "";
  v_nroDocument = "";
  v_custName = "";
  v_custLastName = "";
  v_commercialName = "";
  v_region =0;

  url = "/portal/pls/portal/WEBSALES.NP_CONTACT_ATTENTION_PL_PKG.PL_PROSPECT_NEW?"
               +"av_razonSocial="+v_razonSocial
               +"&av_typeDocument="+v_typeDocument
               +"&av_nroDocument="+v_nroDocument
               +"&av_custName="+v_custName
               +"&av_custLastName="+v_custLastName
               +"&av_commercialName="+v_commercialName
               +"&an_region="+v_region;
  url = "/portal/pls/portal/WEBSALES.NPSL_GENERAL_PL_PKG.WINDOW_FRAME?av_title=" + escape(" Nuevo Prospecto ") + "&av_url=" + escape(url);
  window.open(url,"WinNewProspectOrder","toolbar=no,location=0,directories=no,status=yes,menubar=0,scrollbars=no,resizable=no,screenX=100,top=80,left=100,screenY=80,width=600,height=350,modal=yes");

  return;
  
  }
  
  function SelectCombo(strNombreCombo){
     form = document.frmdatos;     
     if (form.cmbDivision!=null){
        form.myaction.value=strNombreCombo;
        form.action="<%=actionURL_OrderServlet%>";
        form.submit(); 
     }
  }
  
  function SelectSiteData(objThis){
     form = document.frmdatos;
     v_generetor = "<%=strGeneratortype%>";               
     try{
        parent.mainFrame.IdSpecificationSections.innerHTML = "";
        // Si viene desde un incidente y se setean las categorías x defecto no se limpia
        if (v_generetor == "INC" && form.cmbSubCategoria.length == 2 && form.cmbCategoria.length == 2 && form.cmbDivision.length == 2){           
           form.cmbSubCategoria.focus();           
        }
        else{           
           form.cmbSubCategoria.value = "";
           form.cmbCategoria.value = "";
           //form.cmbSolucion.value = ""; 
			  form.cmbDivision.value = "";
           
           /*Solo aparece para bolsa cambio/desactivacion
           Para ocultar el label de responsable de pago de la bolsa actual del cliente*/
           form.txtPaymentRespDesc.style.display = "none";
           parent.mainFrame.document.getElementById("txtLblPaymentResp").style.display = "none";
           /*------------------------------------------------------------------------------*/
        }    
     }catch(e){}     
     if (objThis.value != ""){
        form.myaction.value="mostrarDireccionesSite";
        form.action="<%=actionURL_CustomerServlet%>";
        form.submit(); 
     }else {        
        fxCustomerDetail();
     }        
  }
  
  function fxCustomerDetail(){
    form = document.frmdatos;
    form.myaction.value = 'buscarCliente';
    form.detallemyaction.value = 'detail';
    form.hdnCustId.value  = form.txtCompanyId.value;
    form.hdnCompanyName.value = form.txtCompany.value;
    form.action="<%=actionURL_CustomerServlet%>";
    form.submit();
  }
  
  function fxSearchCustomerId(){
    form = document.frmdatos;
    form.myaction.value = 'buscarCliente';
    form.detallemyaction.value = 'id';
    
    form.hdnCustId.value  = form.txtCompanyId.value;
    form.hdnCompanyName.value = form.txtCompany.value;
    form.action="<%=actionURL_CustomerServlet%>";
    form.submit();
  }
  
  function BuscarClientePorDescripcion(){
    form = document.frmdatos;
    
    form.myaction.value = 'buscarCliente';
    form.detallemyaction.value = 'compania';
    form.hdnCompanyName.value = form.txtCompany.value;
    form.action="<%=actionURL_CustomerServlet%>";
    //alert("Antes del Submit");
    form.submit();
  }

  function fValidatePhone(objThis){
     form = document.frmdatos;
     v_cmbBusqueda = form.cmbTipoBusqueda.value;
     
     if (!ContentOnlyNumber(objThis.value)&&(v_cmbBusqueda!="5")){
        alert("Número de Teléfono no válido");
        form.txtDato.select();
        return false;
     };
     
     form.txtCompany.value = "";
     if (objThis.value == ""){
        return false;
     };
  
  }

   function fxShowAdress() {
      var form = document.frmdatos;
      var customerid = form.txtCompanyId.value;
      var regionid = form.hdnRegionIdAddress.value;
      var v_generetor = "<%=strGeneratortype%>";
      var param;
      
      if (customerid != "") {
         try {
            var action = (form.chkUnkwnSite.checked)?1:0;
         }catch(e) {
            var action = 0;
         }
         if (action == 1) 
            param="?strObjectType=<%=Constante.CUSTOMERTYPE_SITE%>&intObjectId="+form.hdnUnkwnSiteId.value+"&regionAddressId="+regionid+"&generatorType="+v_generetor;      
         else 
            if (form.cmbResponsablePago.value != "")
               param="?strObjectType=<%=Constante.CUSTOMERTYPE_SITE%>&intObjectId="+form.cmbResponsablePago.value+"&regionAddressId="+regionid+"&generatorType="+v_generetor;
            else
               param="?strObjectType=<%=Constante.CUSTOMERTYPE_CUSTOMER%>&intObjectId="+customerid+"&regionAddressId="+regionid+"&generatorType="+v_generetor;
              
         var url="<%=strRutaContext%>/htdocs/jsp/CustomerAddressDelivery.jsp"+param;
         WinAsist = window.open(url,"WinAsist","toolbar=no,location=no,directories=no,status=no,menubar=0,scrollbars=no,resizable=yes,screenX=100,top=80,left=100,screenY=80,width=650,height=300,modal=yes");
      }else
         alert("Debe seleccionar una compañía");
   }

   
   //Si es cliente LARGE validar que se haya seleccionado
   //responsable de pago
   function fxValidateSelectionRP (){
   
      var form = document.frmdatos;
	  var numRespPago=0;

      respPagoSelected = form.cmbResponsablePago.value;
      numRespPago      = form.cmbResponsablePago.length - 1; // Se le resta 1 porque el primer valor del combo es vacío
	  if (numRespPago > 0 && respPagoSelected==''){
         alert("Debe seleccionar un Responsable de Pago");
         form.cmbResponsablePago.focus();
         return false;
      }
	  return true;
   }

   
  function fxValidateSelectionRPag (opcion){
    var form = document.frmdatos;
	  var numRespPago=0;
    
    respPagoSelected = form.cmbResponsablePago.value;
    numRespPago      = form.cmbResponsablePago.length - 1; // Se le resta 1 porque el primer valor del combo es vacío
    siteSecRespPago  = form.hdnSiteSecRespPago.value;
    //cirazabal: valida Responsable de pago desde Seccion de Resp. Pago
		if (numRespPago > 0 && respPagoSelected=='' && siteSecRespPago==''){ //Es una cuenta LARGE
			//var sPermitir=fxSelectResposablePago('newitems');
			var sPermitir=fxSelectResposablePago(opcion);
			var iPermitir = parseInt (sPermitir);
			if (iPermitir<0 ){
				alert("Se produjo un error al realizar la validacion de Responsable de Pago");
				return true;
			}else{
				if (iPermitir==0){
					alert("Debe seleccionar un Responsable de Pago");
					form.cmbResponsablePago.focus();
					return true;
				}else					
					return false
			}//Fin del if iPermitir
		}else{ // Es una cuenta FLAT     
			return false
    }						
	  //return true;
  }
  
  function fxRequestXML(servlet,funcion, params){
      //var url = "<%=request.getContextPath()%>/generalServlet?metodo=" + funcion + "&"+params;
		//alert(params);
    var url = "<%=request.getContextPath()%>/"+servlet+"?metodo=" + funcion + "&"+params;
		//alert(url);
    var msxml = new ActiveXObject("msxml2.XMLHTTP");
    msxml.Open("GET", url, false);
    msxml.Send("");
    var ret = msxml.responseText;
		//alert(ret);
    if(ret.indexOf("OK")!=0){      
      return null;
    }
    
    return ret.substr(2);
  }
  
  //cirazabal validar el Site para la orden, segun sites existente o generados desde oportunidad
  function fxSetSites(){
    if(form.cmbResponsablePago.value != ''){
      form.hdnSite.value = form.cmbResponsablePago.value;
    }else{
      form.hdnSite.value = form.hdnSiteSecRespPago.value;
    }    
  }
  // INICIO - VALIDACION DE PROPUESTAS  CBARZOLA: 07/08/2009
  function fxValidaciondePropuestas(display){    
    if(display!='' && display!=null && display!='null'){      
      alert(display);
      return;
    }
    else{
      if(fxInvocarCreditEvaluation(form.hdnSpecification.value, form.hdnGeneratorType.value)){         
         
         //Se invoca antes de grabar la orden
         fxValidateApplyOrderVolume();
         
         form.myaction.value="grabarOrden";
         form.btnSaveOrder.disabled = true;         
         form.action="<%=actionURL_OrderServlet%>";
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
         // alert('newItemPlanTarifario'+newItemPlanTarifario);
        }
        if(objItem.elementAt(j).npobjitemheaderid == "'57'"){//ORIGINAL txt_ItemNewPlantarifarioId
          vOldItemPlantarifarioId= objItem.elementAt(j).npobjitemvalue.split("'");
          oldItemPlantarifarioId=vOldItemPlantarifarioId[1];
          //alert('oldItemPlantarifarioId'+oldItemPlantarifarioId);
        }//txtItemProduct //cmb_ItemProductBolsa
        if(objItem.elementAt(j).npobjitemheaderid == "'9'"){//cmb_ItemProducto
          vItemProductBolsa= objItem.elementAt(j).npobjitemvalue.split("'");
          newItemProductBolsa=vItemProductBolsa[1];
         // alert('newItemProductBolsa'+newItemProductBolsa);
        }
        if(objItem.elementAt(j).npobjitemheaderid == "'74'"){//cmb_ItemProductBolsaId
          vItemProductBolsaId= objItem.elementAt(j).npobjitemvalue.split("'");
          oldItemProductBolsaId=vItemProductBolsaId[1];
        //  alert('oldItemProductBolsa'+itemProductBolsaId);
        }
        if(objItem.elementAt(j).npobjitemheaderid == "'25'"){//item_services
          vItemServices= objItem.elementAt(j).npobjitemvalue.split("'");
          itemServices=vItemServices[1];
         // alert('ItemServices'+ itemServices);
        }
        if(objItem.elementAt(j).npobjitemheaderid == "'21'"){//txt_ItemQuantity
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
      }   //end if       
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
  
  //Pop up para editar la dirección de entrega
  function fxEditAdress() {
      var form = document.frmdatos;
      var customerid = form.txtCompanyId.value;
      var regionid = form.hdnRegionIdAddress.value;
      var userId = '<%=iUserId1%>';
      if (customerid != "") {
        var param = "?strDirEntrega="+form.hdnDeliveryAddress.value+"&strDpto="+form.hdnDeliveryState.value+"&strProv="+form.hdnDeliveryProvince.value+
                    "&strDist="+form.hdnDeliveryCity.value+"&strDptoId="+form.hdnDeliveryStateId.value+"&strProvId="+form.hdnDeliveryProvinceId.value+
                    "&strDistId="+form.hdnDeliveryCityId.value+"&regionAddressId="+regionid+"&userId="+userId+"";
        var frameUrl = "<%=strRutaContext%>/htdocs/jsp/Order_EditAddress.jsp"+param;
        var url = appContext+"/DYNAMICSECTION/DynamicSectionItems/DynamicSectionItemsPage/PopUpOrderFrame.jsp?av_url="+escape(frameUrl);
        WinAsist = window.open(url,"WinAsist","toolbar=no,location=no,directories=no,status=no,menubar=0,scrollbars=no,resizable=yes,screenX=100,top=80,left=100,screenY=80,width=650,height=300,modal=yes");
      }else
         alert("Debe seleccionar una compañía");
   }
  
    function fxValidateSectionRegularize(){
      var form = document.frmdatos;
      
      if(form.txtNumeroTelefono.value==""){
        alert("Debe ingresar un numero de teléfono");
        return false;
      }else{
        if(!ContentOnlyNumber(form.txtNumeroTelefono.value)){
          alert("Número de Nextel no válido");
          form.txtNumeroTelefono.select();
          return false;
        }
      }
      
      if(form.txtRazonsocial.value==""){
        alert("Debe ingresar la razon social");
        return false;
      }
      
      return true;
    }
    
    function fxSubRegOnLoad(){
      document.frmdatos.txtRazonsocial.value = document.frmdatos.txtCompany.value;
    }
</script>


<script language = "javascript">
    /* flags de ejecucicon de funciones */
     wn_flag_lugar_Atencion  = 1;  /* Para ejecutar una sola vez la funcion "SelectLugarAtencion" */
     var wb_existItem        = false;
                 
     var wvs_swcreatedby     = "LROSALES";
     var wvs_swpersonid      = "139261";
     var wns_appid           = "26";
     var wv_npprocessgroup   = "0";                  
     var wns_npbuildingid    = "21";

</script>

<form name="frmdatos" id="frmdatos" action="" method="post" target="bottomFrame" > 

<input type="hidden" name="hdnUserId" value="<%=strSessionId1%>">
<input type="hidden" name="hdnSessionId" value="<%=strSessionId1%>">
<input type="hidden" name="hdnSessionLogin" value="<%=portalSessionBean1.getLogin()%>">
<input type="hidden" name="hdnSessionCreatedby" value="<%=portalSessionBean1.getNom_user()%>">
<input type="hidden" name="hdnBuildingId" value="<%=portalSessionBean1.getBuildingid()%>">
<input type="hidden" name="hdnGeneratorType" value="">
<input type="hidden" name="hdnOrigenType" value=""> 
<input type="hidden" name="hdnGeneratorId" value="">
<input type="hidden" name="hdnSiteOppId" value="">
<input type="hidden" name="hdnunknwnSiteId" value="">
<input type="hidden" name="hdnSalesTeamOppId" value="">
<input type="HIDDEN" name="hdnCompanyName">
<input type="hidden" name="hdnIUserId" value="<%=iUserId1%>">
<input type="hidden" name="hdnIAppId" value="<%=iAppId1%>">
<input type="HIDDEN" name="hdnCustId">
<input type="HIDDEN" name="hdngeneratortype" value="<%=strGeneratortype%>">
<input type="HIDDEN" name="hdngeneratorId" value="<%=strGeneratorId%>">
<input type="HIDDEN" name="hdnValidateCredit" value ="0">
<!--cirazabal: Agregado para controlar la eleccion de Site desde Responsables de Pago-->
<input type="HIDDEN" name="hdnSiteSecRespPago" value="">
<input type="hidden" name="hdnTrama" value=""><!--CBARZOLA-->
<input type="hidden" name="hdnOrderId" value="0"><!--CBARZOLA-->
<input type="hidden" name="hdnRegionIdAddress" value="0">
<%}catch(SessionException se) {
    System.out.println("[JP_ORDER_NEW_START][SessionException] : " + se.getClass() + " - " + se.getMessage());
    se.printStackTrace();
    //out.println("<script>alert('Su sesión ha finalizado. Ingrese nuevamente');</script>");
    //String strUrl = "/portal/pls/portal/WEBSALES.NP_USER_AVAILABILITY_PL_PKG.PL_LOGOUT";
     //out.print("<script>parent.mainFrame.location.replace('"+strUrl+"');</script>");
  }catch(Exception e) {
    String strMessageExceptionGeneralStart = "";
    strMessageExceptionGeneralStart = MiUtil.getMessageClean(e.getMessage());
    System.out.println("[JP_ORDER_NEW_START][Exception] : " + e.getClass() + " - " + e.getMessage());
    out.println("<script>alert('"+strMessageExceptionGeneralStart+"');</script>");   
  }
%>