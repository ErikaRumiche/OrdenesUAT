<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest"%>
<%@ page import="oracle.portal.provider.v2.ProviderSession"%>
<%@page import="oracle.portal.provider.v2.ProviderUser" %>
<%@page import="oracle.portal.provider.v2.http.HttpCommonConstants"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="pe.com.nextel.bean.PortalSessionBean"%>
<%@page import="pe.com.nextel.bean.SpecificationBean"%>
<%@page import="pe.com.nextel.util.MiUtil"%>
<%@page import="pe.com.nextel.service.SessionService"%>
<%@page import="pe.com.nextel.service.NewOrderService"%>
<%@page import="pe.com.nextel.service.GeneralService"%>
<%@page import="pe.com.nextel.exception.SessionException" %>
<%@page import="pe.com.nextel.util.Constante"%>
<%@ page import="pe.com.nextel.service.CustomerService"%> 
<%@ page import="java.util.Hashtable"%>

<%
  try{
  String strSessionIdOrderEnd = "";
  
    try{
    PortletRenderRequest pReq = (PortletRenderRequest) request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
    ProviderUser objetoUsuario1 = pReq.getUser();
    strSessionIdOrderEnd=objetoUsuario1.getPortalSessionId();
    System.out.println("Sesión capturada  JP_ORDER_NEW_END_ShowPage : " + objetoUsuario1.getName() + " - " + strSessionIdOrderEnd );
  }catch(Exception e){
    System.out.println("Portlet Not Found : " + e.getClass() + " - " + e.getMessage() );
    out.println("Portlet JP_ORDER_NEW_END_ShowPage Not Found");
    return;
  }
 String strRutaContext=request.getContextPath(); 
 String strURLOrderServlet =strRutaContext+"/editordersevlet"; 

  System.out.println("Sesión capturada después del resuest : " + strSessionIdOrderEnd );
	PortalSessionBean portalSessionBean4 = (PortalSessionBean)SessionService.getUserSession(strSessionIdOrderEnd);
	if(portalSessionBean4==null) {
    System.out.println("No se encontró la sesión de Java ->" + strSessionIdOrderEnd);
		throw new SessionException("La sesión finalizó");
	}
  
  HashMap hshInputOParamsOrders = new HashMap();
  System.out.println("LOGIN : " + portalSessionBean4.getLogin());


%>
<table border="0" cellspacing="1" cellpadding="2" width="100%">
  <tr align="center">
    <td>
      <input type="hidden" name="flgSave" value="0"/>
      <input type="hidden" name="hdnSaveOption" value="0"/>
      <input type="button" name="btnSaveOrder" value="Grabar" onclick="document.frmdatos.hdnSaveOption.value=1;fxValidateforSaving()"/>
    </td>
  </tr>
</table>
<% 

   //Parametros que vienen desde INCIDENTES u OPORTUNIDADES
  //String strCustomerId        = request.getParameter("customerId")==null?"1229":request.getParameter("customerId");
  String strCustomerId        = request.getParameter("customerId")==null?"":request.getParameter("customerId");
  System.out.println("strCustomerId       : "+strCustomerId);
  
  //String strSiteId            = request.getParameter("siteId")==null?"":request.getParameter("siteId");
  String strSiteId            = request.getParameter("siteId")==null?"":request.getParameter("siteId");
  System.out.println("strSiteId           : "+strSiteId);
  
  //String strSalesTeamId       = request.getParameter("salesTeamId")==null?"":request.getParameter("salesTeamId");
  String strSalesTeamId       = request.getParameter("salesTeamId")==null?"":request.getParameter("salesTeamId");
  System.out.println("strSalesTeamId      : "+strSalesTeamId);
  
  //String strGeneratorid       = request.getParameter("generatorid")==null?"14280003":request.getParameter("generatorid");
  String strGeneratorid       = request.getParameter("generatorid")==null?"":request.getParameter("generatorid");
  System.out.println("strGeneratorid      : "+strGeneratorid);
  
  //String strGeneratortype     = request.getParameter("generatortype")==null?"INC":request.getParameter("generatortype");
  String strGeneratortype     = request.getParameter("generatortype")==null?"":request.getParameter("generatortype");
  System.out.println("strGeneratortype    : "+strGeneratortype);
  
  //String strSpecificationId   = request.getParameter("an_npspecificationid")==null?"2015":request.getParameter("an_npspecificationid");
  String strSpecificationId   = request.getParameter("an_npspecificationid")==null?"":request.getParameter("an_npspecificationid");
  System.out.println("strSpecificationId  : "+strSpecificationId);
  
  //String strDivisionId        = request.getParameter("divisionid")==null?"2":request.getParameter("divisionid");
  String strDivisionId        = request.getParameter("divisionid")==null?"":request.getParameter("divisionid");
  System.out.println("strDivisionId       : "+strDivisionId);
  
  String strFlagGenerator     = request.getParameter("av_flagGenerator");
  System.out.println("strFlagGenerator    : "+strFlagGenerator);
  
  //String strOpportunityTypeId = request.getParameter("opportunityTypeId")==null?"":request.getParameter("opportunityTypeId");
  String strOpportunityTypeId = request.getParameter("opportunityTypeId")==null?"":request.getParameter("opportunityTypeId");
  System.out.println("strOpportunityTypeId    : "+strOpportunityTypeId);
  
  /**JOYOLAR 06/03/2008, se agregaron las variables strunknwnsite y strSiteOppId**/
  //String strunknwnSiteId        = request.getParameter("unknwnSiteId")==null?"":request.getParameter("unknwnSiteId");
  String strunknwnSiteId        = request.getParameter("unknwnSiteId")==null?"":request.getParameter("unknwnSiteId");
  System.out.println("strunknwnSiteId       : "+strunknwnSiteId);

  String strSiteOppId        = request.getParameter("hdnSiteOppId")==null?"":request.getParameter("hdnSiteOppId");
  System.out.println("strSiteOppId       : "+strSiteOppId);   
     //NBRAVO

          ArrayList arrNewSite=null; 
          HashMap hshData=null;      
          int cont=0;
          long lCustomerId =MiUtil.parseLong(strCustomerId);
          System.out.println("lCustomerId -->> "+lCustomerId);

          CustomerService objCustomerS=new CustomerService(); 
          hshData=objCustomerS.getCustomerSites(lCustomerId,Constante.SITE_STATUS_UNKNOWN);
          
          if (hshData.get("strMessage") != null && hshData.get("strMessage").toString() != null) {
              throw new Exception(hshData.get("strMessage").toString());
          }
                    
          arrNewSite=(ArrayList)hshData.get("objArrayList");
          
          if (arrNewSite != null) {
              for(int i=0; i<arrNewSite.size();i++) { 
                  cont++;
              }
          } else {
              System.out.println("arrNewSite == null");
          }

  //


%>

<script>
// INICIO Constantes
   var SPEC_RECONEXION    ="2008";  // RECONEXION
// FIN Constantes
   var wv_flagGenerator       = "<%=strFlagGenerator%>";
   var wn_specificationid     = "<%=strSpecificationId%>";
   var wn_customerId          = "<%=strCustomerId%>";
   var wv_generatortype       = "<%=strGeneratortype%>";
   var wn_generatorId         = "<%=strGeneratorid%>";
   var wn_siteId              = "<%=strSiteId%>";
   var wn_divisionId          = "<%=strDivisionId%>";
   var wn_opportunityTypeId   = "<%=strOpportunityTypeId%>";
   var wn_salesid             = "<%=MiUtil.getString(strSalesTeamId)%>";
   var wv_url                 = "";
   
   /**JOYOLAR, 06/03/2008, SE AGREGARON LAS VARIABLES UNKWNSITE Y SITEOPPID**/
   var wn_unknwnSiteId        = "<%=strunknwnSiteId%>";
   var wv_siteOppId           = "<%=strSiteOppId%>";
   
   function fxValidateforSaving(){
     var v_form              = document.frmdatos;
    if(document.frmdatos.cmbSubCategoria.value != ""){
      fxIndiceItemDevice();
    }
     if(!fxValidateOrderGeneral())
       return false;
     if(!fxValidateOrderVolume())
       return false;
     if(!fxValidateSectionsforSaving())
       return false;
    
   if (document.frmdatos.chkVepFlag != undefined){
            var iSpecificationId=form.cmbSubCategoria.value;
            if (validateSpecificationVep(iSpecificationId)){
        if(fxValidateExistItemsVEP(document.frmdatos.cmbVepNumCuotas, 1) == 0 && document.frmdatos.chkVepFlag.checked == true){
           alert("No es posible grabar una orden de venta a plazos si no se cuenta con items de tipo Venta a Plazos");
           return false;
        }

         //INICIO: PRY-0864 | AMENDEZ
         if(!validateOrderVepCI()){
             return false;
         }
         //FIN: PRY-0864 | AMENDEZ

                //INICIO: PRY-1200 | AMENDEZ
                if(!validateInfoVEP()){
                    return false;
                }
                //FIN: PRY-1200 | AMENDEZ

     }
   }
     
       // INI PRY-1002 AMATEOM
	   	if (form.cmbFormaPago.value == "<%=Constante.PAYFORM_CARGO_EN_RECIBO%>"){
			if (form.cmbSubCategoria.value == '<%=Constante.SPEC_CAMBIO_MODELO%>' || form.cmbSubCategoria.value == '<%=Constante.SPEC_CAMBIO_MODELO_ENTRE_TECNOLOGIAS%>') {
				if (!validatePreEvaluationCMD()) {
					return false;
				}
			}
		}	
       // FIN PRY-1002 AMATEOM
    try{
      var wn_decrip_long = form.txtDetalle.value.length;
    if ( wn_decrip_long > 4000) {
      alert("El campo 'Descripción' tiene " + wn_decrip_long +" letras y es mayor que el maximo permitido (4000). Disminuya la"+" cantidad de letras.");
      form.txtDetalle.select();
      return false;
		}
	  }catch(e){}
       //v_form.btnSaveOrder.disabled = true;
       doSubmit('grabarOrden');
   }
   
function validatePreEvaluationCMD(){
    var url_server = '<%=strURLOrderServlet%>';
    var wn_customerId = '<%=strCustomerId%>';
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
   
  function fxValidateOrderGeneral(){
    var form  = document.frmdatos;
         
         
                  //NBRAVO
         if(<%=cont%> == 0 && form.cmbCategoria.value == <%=Constante.KN_VTA_TELEFONIA_FIJA_BA%>){
         alert("No puede crear una orden de BANDA ANCHA con estructura FLAT");
         return false;
          }    

          if(form.cmbCategoria.value == <%=Constante.KN_VTA_TELEFONIA_FIJA_BA%> && "<%=strSiteId%>" == ""){
              if(form.siteCheck.checked==false && form.cmbResponsablePago.value==""){
              alert("No ha seleccionado Responsable de Pago");
              return false;
              }
          }    
          
                          
    
         if( form.cmbLugarAtencion.value == "" ){
            alert("Debe seleccionar Lugar de Despacho");
            form.cmbLugarAtencion.focus();
            return false;
         }         
         

         if (form.cmbFormaPago.value == ""){
            alert("Debe seleccionar forma de pago");
            form.cmbFormaPago.focus();
            return false;
         } 
         

          //INICIO VALIDACION TIENDA EXPRESS JRAMIREZ        
          var typeService = form.hdnTypeService.value;  
          var tienda = form.hdnTienda.value;   
          var subcategoriaid=form.cmbSubCategoria.value;		  
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
             

                    if(form.cmbLugarAtencion.value!=tienda){                
                            alert("Lugar de Despacho debe ser la misma Tienda Express");
                            form.cmbLugarAtencion.focus();
                            return false;
					 
                    }
					
		     //INICIO: PRY-0864_2 | AMENDEZ - Se adiciona check vep a validacion
                     if(subcatContado==1){            
                                if(form.cmbFormaPago.value=="<%=Constante.PAYFORM_CARGO_EN_RECIBO%>" && form.chkVepFlag.checked == false){             
                                    alert("El pago debe ser al contado");
                                    form.cmbLugarAtencion.focus();
                                    return false;             
                                }
                        }
                    //FIN: PRY-0864_2 | AMENDEZ - Se adiciona check vep a validacion             
                
                        //Validacion a nivel de orden                        
                        intTiendaForeign =0;
                        if (window.form.cmbLugarAtencion){ 
                            intTiendaForeign = form.cmbLugarAtencion.value;
                        }
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
                                        alert("Lugar de Despacho no puede ser una Tienda Express");
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
                                                
                                                           if(iIsTiendaExpress_frgn !=0 ){ 
                                                            alert("Lugar de Despacho no puede ser una Tienda Express");
                                                            return;
                                                         } 
                                            
                                        }
                                           
                             }                    
                }
           
            
        }
        
    //FIN VALIDACION TIENDA EXPRESS


    try{
      if (form.txtDirEntrega != null){
        if (form.txtDirEntrega.value == "") {
          alert("Debe seleccionar una Dirección de Entrega");
          return false;
        }
      }
    }catch(e){}  
    
      try{
			//SIEMPRE DEBE VALIDAR LA FORMA DE PAGO
         if( form.cmbFormaPago.options[0].selected ){
            alert("Debe seleccionar Forma de Pago");
            return false;
         }
         
         if (<%=Constante.SPEC_SUSPENSIONES[0]%> == form.cmbSubCategoria.value) {
            if (form.txtFechaProceso.value == "") {
            alert("Debe ingresar la fecha de suspensión");
            form.txtFechaProceso.focus();
            return false;
            }
            if (form.txtFechaReconexion.value == "") {
            alert("Debe ingresar la fecha de reconexión");
            form.txtFechaReconexion.focus();
            return false;
            }            
         }
			
      }catch(e){}
      
      
      if( trim(form.txtNumSolicitud.value).length == 0 ){
         alert("Debe ingresar el Número de Solicitud");
         return false; 
      }
      
      if( form.cmbSubCategoria.options[0].selected ){
         alert("Debe seleccionar una Sub Categoría");
         return false;
      }
      
      try{
         if( form.cmbResponsablePago.options[0].selected ){
            return fxSectionNameValidateSelectSite();
         }
      }catch(e){}
      
      /* Inicio Data */
   
    try{    
      if (form.hdnDataFieldsVisibles.value == "S"){
        form.hdncmbVendedorData.value    = form.cmbVendedorData.options[form.cmbVendedorData.selectedIndex].text;
        form.hdnVendedorDataId.value    = form.cmbVendedorData.options[form.cmbVendedorData.selectedIndex].value;        
        if (form.hdnVendedorDataId.value == ""){
          alert("Debe seleccionar un Vendedor Data");
          form.cmbVendedorData.focus();
          return false;
        }
      }
    }catch(e){}

    try{
         
         if (2074 == form.cmbSubCategoria.value) {
            wn_items = (items_table.rows.length -1); // numero de items 
            if (wn_items>1){
              alert("Solo se puede registrar un Item asociado a esta categoría")
              return false;
            }            
         }
			
      }catch(e){}    
  /* Fin Data */
      
      return true;
      
   }
      
  function fxValidateOrderVolume(){
    form = document.frmdatos;
    
    if(form.cmbSubCategoria.value == <%=Constante.SPEC_POSTPAGO_VENTA%> || form.cmbSubCategoria.value == <%=Constante.SPEC_PORTABILIDAD_POSTPAGO%>){
      
      if(aplicoVO == "2"){
        alert("Volver a Evaluar Promocion por Volumen de Orden");
        return false;
      }
      return true;      
    }
    else{
      return true;
    }   
  }
   
   function fxLoadCustomer(){
     form = document.frmdatos;
     
     form.hdnGeneratorId.value     = wn_generatorId;
     
     //Seteo los valores del Generador
     if( wv_generatortype != "" ){
       form.hdnGeneratorType.value = wv_generatortype;
       form.hdnOrigenType.value    = wv_generatortype;
     }else{
       form.hdnGeneratorType.value = "";
       form.hdnOrigenType.value    = "FFPEDIDOS";
     }
     var generatorTypeRepCalCap = form.hdnOrigenType.value;
     var generatorIdRepCalCap = form.hdnGeneratorId.value;
     //Si el flag es "S" se debe generar la orden obligatoriamente
     if( wn_customerId != "" ){
       form.myaction.value           = "buscarCliente";
       form.detallemyaction.value    = "id";
       form.hdnSalesTeamOppId.value  = wn_salesid;
       form.hdnunknwnSiteId.value    = wn_unknwnSiteId;
       form.hdnSiteOppId.value       = wn_siteId;
       
       //wv_url  = "<%=request.getContextPath()%>/customerservlet?strGeneratorType="+wv_generatortype+"&strSpecFlag=S"; 
       wv_url  = "<%=request.getContextPath()%>/customerservlet?strGeneratorTypeRepCalCap="+generatorTypeRepCalCap+"&strGeneratorId="+generatorIdRepCalCap+"&strGeneratorType="+wv_generatortype+"&strSpecFlag=S";
       wv_url = wv_url + "&strLogin=<%=portalSessionBean4.getLogin()%>";
       
       if( wn_opportunityTypeId != '' )
       wv_url = wv_url + "&strOpportunityTypeId="+wn_opportunityTypeId;
       
       if( wv_flagGenerator != '' )
       wv_url = wv_url + "&strFlagGenerator="+wv_flagGenerator;

       wv_url = wv_url + "&strCustomerId="+wn_customerId;
       wv_url = wv_url + "&strSpecificationId=<%=strSpecificationId%>";
       wv_url = wv_url + "&strDivisionId=<%=strDivisionId%>";
      
       form.action = wv_url;
       form.submit();
      }
   }
   
   function fxLoadOrderDetail(){
     form = document.frmdatos;
     form.cmbLugarAtencion.value  = '<%=portalSessionBean4.getBuildingid()%>';
     form.cmbFormaPago.value      = 'Contado';
   }
   
   function fxOrderNewOnLoad(){
    fxLoadCustomer();
   }

//INICIO: PRY-0864 | AMENDEZ
function validateOrderVepCI(){
    try{
        var url_server = '<%=strURLOrderServlet%>';
        var varValidateCustomerOrderVEPCI=0;
        var nporderid=0;
        var npinitialquota=document.frmdatos.txtCuotaInicial.value;
        var totalsalesprice=parseFloat(document.frmdatos.txtTotalSalesPrice.value);
        var npvepquantityquota = document.frmdatos.cmbVepNumCuotas.value;
        var npspecificationid  = form.cmbSubCategoria.value;
        var npvep = document.frmdatos.chkVepFlag.value;
        var nptype = 'ORDER_NEW';
        var wn_customerId          = "<%=strCustomerId%>";
        var flagValidate=false;
        var nppaymenttermsiq=document.frmdatos.txtPaymentTermsIQ.value;

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
                var montototal     =parseFloat(document.frmdatos.txtTotalSalesPrice.value);
                var cuotainicial   =parseFloat(document.frmdatos.txtCuotaInicial.value);
                var formapagoci    = document.frmdatos.txtPaymentTermsIQ.value;
                var strformapagoci="";
                if(formapagoci=="1"){
                    strformapagoci = 'Cargo en el recibo';
                }else if(formapagoci=="0"){
                    strformapagoci = 'Contado';
                }else {
                    strformapagoci = 'Contado';
                }

                if (cuotainicial == ""){
                    cuotainicial=0;
                }

                var montofinanciar = round_decimals(parseFloat(montototal-cuotainicial),2);
                var cantidadcuotas =    document.frmdatos.cmbVepNumCuotas.value;

                var cuotasmensual=round_decimals(parseFloat(montofinanciar/cantidadcuotas),1);
                var ultimaCuota=round_decimals(parseFloat(montofinanciar-(cuotasmensual*(cantidadcuotas-1))),2);


                var messageConfirm ='Resumen de Orden VEP \n\n' +
                    'Monto total\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0: '+montototal+' \n' +
                    'Cuota inicial\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0: '+cuotainicial+' \n' +
                    'Forma de pago CI\u00A0\u00A0\u00A0: '+strformapagoci+' \n\n' +
                    'Monto a financiar\u00A0\u00A0: '+montofinanciar+' \n' +
                    'Cantidad de cuotas\u00A0: '+cantidadcuotas+' \n\n' +
                    'Cuota mensual\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0: '+cuotasmensual+' \n' +
                    'Ultima cuota\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0: '+ultimaCuota+' ';

                if(document.frmdatos.chkVepFlag.checked == true){
                    if (confirm(messageConfirm)) {
                        return true;
                    }
                    return false;
                }

                return true;


            }catch(e) {
                alert("Hubo un error en el alert de orden de venta a plazos");
                return false;
            }
        }
        //FIN: PRY-1200 | AMENDEZ
   
   onload=fxOrderNewOnLoad;
</script>


<%}catch(SessionException se) {
    System.out.println("[JP_ORDER_NEW_END][SessionException] : " + se.getClass() + " - " + se.getMessage());
    se.printStackTrace();
  }catch(Exception e) {
    String strMessageExceptionGeneralStart = "";
    strMessageExceptionGeneralStart = MiUtil.getMessageClean(e.getMessage());
    System.out.println("[JP_ORDER_NEW_END][Exception] : " + e.getClass() + " - " + e.getMessage());
    e.printStackTrace();
    out.println("<script>alert('"+strMessageExceptionGeneralStart+"');</script>");   
  }
%>