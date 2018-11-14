<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<%@ page import="pe.com.nextel.bean.TableBean" %>

<%


  try{
  String strSessionIdOrderEnd = "";
  
    try{
    PortletRenderRequest pReq = (PortletRenderRequest) request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
    ProviderUser objetoUsuario1 = pReq.getUser();
    strSessionIdOrderEnd=objetoUsuario1.getPortalSessionId();
    System.out.println("Sesi�n capturada  JP_ORDER_NEW_END_ShowPage : " + objetoUsuario1.getName() + " - " + strSessionIdOrderEnd );
  }catch(Exception e){
    System.out.println("Portlet Not Found : " + e.getClass() + " - " + e.getMessage() );
    out.println("Portlet JP_ORDER_NEW_END_ShowPage Not Found");
    return;
  }

 String strRutaContext=request.getContextPath(); 
 String strURLOrderServlet =strRutaContext+"/editordersevlet";
 //PBI000000042016
 String strOrderServlet =strRutaContext+"/orderservlet";
  
  System.out.println("Sesi�n capturada despu�s del resuest : " + strSessionIdOrderEnd );
	PortalSessionBean portalSessionBean4 = (PortalSessionBean)SessionService.getUserSession(strSessionIdOrderEnd);
	if(portalSessionBean4==null) {
    System.out.println("No se encontr� la sesi�n de Java ->" + strSessionIdOrderEnd);
		throw new SessionException("La sesi�n finaliz�");
	}
  
  HashMap hshInputOParamsOrders = new HashMap();
  System.out.println("LOGIN : " + portalSessionBean4.getLogin());
  
  //Inicio [reason Change of model] CDIAZ
  GeneralService objGeneralService = new GeneralService();
  HashMap mapSpecificationsChangeModel = objGeneralService.getValueNpTable("CHANGE_MODEL_SPECIFICATION");
  ArrayList arrSpecificationsChangeModel = (ArrayList)mapSpecificationsChangeModel.get("objArrayList");
  //Fin [reason Change of model] CDIAZ
        //EFLORES TDECONV003-3
        HashMap mapValidMessage = objGeneralService.getDescriptionByValue("MSG_ALERT_MAX_ITEMS_PROPIOS", "MIGRATION_PRE_POST" );
        String msg_chk_migration_error = mapValidMessage.get("strDescription")==null?"": mapValidMessage.get("strDescription").toString();
        String plan_reg_exp_validation = objGeneralService.getValue("MIGRATION_PRE_POST","PLAN_REGEXP");
        System.out.println("[TDECONV003-3] msg_chk_migration_error = "+msg_chk_migration_error);
        System.out.println("[TDECONV003-3] plan_reg_exp_validation = "+plan_reg_exp_validation);

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

<script type="text/javascript" src="<%=request.getContextPath()%>/websales/Resource/js/Roaming.js"></script>

<script>
// INICIO Constantes
   var SPEC_RECONEXION    ="2008";  // RECONEXION

    // MMONTOYA [ADT-RCT-092 Roaming con corte]
    // Inicio constantes de Roaming.js
    var SPEC_ACTIVAR_PAQUETES_ROAMING = <%=Constante.SPEC_ACTIVAR_PAQUETES_ROAMING%>;
    var TIPO_BOLSA_RECURRENTE = "<%=Constante.TIPO_BOLSA_RECURRENTE%>";
    var CONTEXT_PATH = "<%=request.getContextPath()%>";
    // Fin constantes de Roaming.js

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
    if(fxValidateNumSolicitud()){
        alert('El numero de Solicitud ya se encuentra registrado en \n una orden del aplicativo Retail');
        return false;
    }
       if(!fxValidateBiometricVerif()){
           return false;
       }

    if(document.frmdatos.cmbSubCategoria.value != ""){
      fxIndiceItemDevice();
    }


        //INICIO: PRY-1037 | KPEREZ
       if (v_form.cmbSubCategoria.value == '<%=Constante.SPEC_POSTPAGO_VENTA%>'){
           if(!validateSimManager()){
               return;
           }
       }
        //FIN: PRY-1037 | KPEREZ




    <!-- EFH [N_SD000349095] -->
    <!--Obteniendo el valor de chkCarpetaDigital-->
    var chkCarpetaDigital = v_form.chkCarpetaDigital.checked;
    if(chkCarpetaDigital === true){
       v_form.hdnCarpetaDigital.value="S";
    }else{
        v_form.hdnCarpetaDigital.value="N";
    }
    <!-- EFH [N_SD000349095]-->

       <!-- [TDECONV003] KOTINIANO -->
       <!--valor de chkFlagMigration-->
        //EFLORES TDECONV003-3
        try {
       var chkFlagMigration = v_form.chkFlagMigration.checked;
       if(chkFlagMigration === true){
           v_form.hdnFlagMigration.value="S";
                if (v_form.cmbSubCategoria.value == <%= Constante.SPEC_POSTPAGO_VENTA %>) {
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
                    } else if(txtItemModality[0] != "<%= Constante.MODALITY_PROPIO %>"){
                        alert("<%= msg_chk_migration_error %>");
                        return false;
                    } else {
                        if (txtItemModality[0] == "<%= Constante.MODALITY_PROPIO %>") {
                            var rx = <%= plan_reg_exp_validation %>;
                            if (rx.test(txtItemRatePlan[0])) {
                                v_form.hdnFlagMigration.value = "C";
                            }
                        }
                    }
                }
       }else{
           v_form.hdnFlagMigration.value="N";
       }
        }catch(e){
            v_form.hdnFlagMigration.value = "N";
        }
        //FIN EFLORES TDECONV003-3
       <!-- [TDECONV003] KOTINIANO-->

     if(!fxValidateOrderGeneral())
       return false;
     if(!fxValidateOrderVolume())
       return false;
     if(!fxValidateSectionsforSaving())
       return false;
     if(!fxValidateItemVolume())
       return false;

       <!-- JCASTILLO  CPUF-->
       if(!fxValidateCpuf()){
           return false;
       }
       <!-- JCASTILLO  -->
       if(form.hdnFlgSDD.value==<%=Constante.FLAG_SECTION_ACTIVE%>) {
           form.hdnSignatureType.value = v_form.cmbSignature.value;
           if(form.hdnSignatureType.value=='<%=Constante.SIGNATURE_TYPE_DIGITAL%>'){
               if(!fxValidateEmail(v_form.txtEmailDG)){
                return false;
               }
           }
       }

       if(form.hdnFlgSAC.value==<%=Constante.FLAG_SECTION_ACTIVE%>){
           if(form.hdnChkAssignee.value==1){
               if(!fxValidateAssignee()){
                   return false;
               }
           }
       }

   if (document.frmdatos.chkVepFlag != undefined){
            //INICIO: PRY-1200 | AMENDEZ
            var iSpecificationId=form.cmbSubCategoria.value;
            if (validateSpecificationVep(iSpecificationId)){
            //INICIO: PRY-1200 | AMENDEZ
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

    // MMONTOYA [ADT-RCT-092 Roaming con corte]
    // Validacion del servicio roaming recurrente.
    if (!validateAllRecurrentRoamingService(form.cmbSubCategoria.value, 0)) {
        return;
    }

    // FBERNALES [PM0011240 Portabilidad]
    if (form.cmbSubCategoria.value=="<%=Constante.SPEC_PREPAGO_PORTA%>" || form.cmbSubCategoria.value=="<%=Constante.SPEC_POSTPAGO_PORTA%>"){
        if (!fxValidateDocument(trim(form.txtDocumento.value),form.txtDocumento.value,form.cmbDocumento.value)) {
          return false;
        }
    }


    try{
      var wn_decrip_long = form.txtDetalle.value.length;
    if ( wn_decrip_long > 4000) {
      alert("El campo 'Descripcion' tiene " + wn_decrip_long +" letras y es mayor que el maximo permitido (4000). Disminuya la"+" cantidad de letras.");
      form.txtDetalle.select();
      return false;
		}
	  }catch(e){}
       //v_form.btnSaveOrder.disabled = true;

        //PBI000000042016
        var codBscs = $("input[name=txtCodBSCS]").val();
        var subCategoria = $("select[name=cmbSubCategoria]").val();
        var ordenId = $("input[name=hdnNumeroOrder]").val();
        var esflat = codBscs.indexOf("1.");

        alert("codBscs " + codBscs);
        alert("subCategoria " + subCategoria);
        alert("ordenId " + ordenId);
        alert("esflat " + esflat);

        //Si devuelve -1 el cliente es LARGE
        if(esflat == -1) {
            if(validarEspecResPago(subCategoria)){
                var resPagoOpts = $("select[name=cmbResponsablePago] option");
                var respPagoSelected = $("select[name=cmbResponsablePago]").val();
                alert("resPagoOpts " + resPagoOpts);
                alert("respPagoSelected " + respPagoSelected);

                if(!validarNuevoResPago(ordenId)){
                    if ((resPagoOpts.length - 1) > 0 && respPagoSelected == "") {
                        alert("Debe seleccionar responsable de pago");
                        return false;
                    }
                }
            }
        }

       doSubmit('grabarOrden');
   }
   
    function validarEspecResPago(especificacionId){
        var url_server = '<%=strURLOrderServlet%>';
        var dataString = "especificacionId="+ especificacionId;
        var params = 'myaction=validarEspecResPago&'+dataString;
        $.ajax({
            url: url_server,
            async: false,
            type: 'POST',
            dataType: 'text/xml',
            data: params,
            success: function(data){
                alert("requestValidarEspecResPago "+ data);
                if(data != "0"){
                    return true;
                }
            },
            error:function(xhr, ajaxOptions, thrownError){
                alert('Error inesperado: ' + xhr.responseText);
                return false;
            }
        });
        return false;
    }
    function validarNuevoResPago(ordenId){
        var url_server = '<%=strURLOrderServlet%>';
        var dataString = "ordenId="+ ordenId;
        var params = 'myaction=validarNuevoRespPago&'+dataString;
        $.ajax({
            url: url_server,
            async: false,
            type: 'POST',
            dataType: 'text/xml',
            data: params,
            success: function(data){
                alert("requestValidarNuevoRespPago "+ data);
                if(data != "0"){
                    return true;
                }
            },
            error:function(xhr, ajaxOptions, thrownError){
                alert('Error inesperado: ' + xhr.responseText);
                return false;
            }
        });
        return false;
    }

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



function validatePreEvaluationCMD(){
    var url_server = '<%=strURLOrderServlet%>';
    var wn_customerId = '<%=strCustomerId%>';
    var result = false;

    if (wn_customerId==""){
       var txtCompanyId=document.frmdatos.txtCompanyId.value;
       wn_customerId=txtCompanyId;
    }
	
	var params = 'myaction=validatePreevaluationCDM&customerId='+wn_customerId;
	
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
   
function fxValidateCpuf() {
    if (form.hdnFlgCPUF.value ==<%=Constante.FLAG_SECTION_ACTIVE%>) {

        if($("#cpufResponse0").is(":checked")){
            form.hdnCpufResponse.value=0;
        }
        if($("#cpufResponse1").is(":checked")){
            form.hdnCpufResponse.value=1;
        }
        var response=form.hdnCpufResponse.value;
        if (response=='') {
            alert('Debe seleccionar una opcion de CPUF');
            $('#cpufResponse0').focus();
            return false;
        }
    }
    return true;
}
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
              alert("El Carnet de Extranjer�a del Apoderado debe tener 12 digitos");
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

  function fxValidateOrderGeneral(){
    var form  = document.frmdatos;

      //EPV
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

      //portegar PRY-0817
      //se valida cammpo de fecha de programacion automatica
      if(!validateFechaProg())return false;
      //fin portegar

         if (form.cmbFormaPago.value == ""){
            alert("Debe seleccionar forma de pago");
            form.cmbFormaPago.focus();
            return false;
         }

          //INICIO DERAZO REQ-0940
        if(form.txtContactEmail != null && form.txtContactPhoneNumber != null && form.txtContactFirstName != null && form.txtContactLastName != null &&
                form.cmbContactDocumentType != null && form.txtContactDocumentNumber != null) {

            //Validaciones Genericas
            if (form.cmbContactDocumentType.value == 'DNI' && (form.txtContactDocumentNumber.value.length != 8 || !ContentOnlyNumber(form.txtContactDocumentNumber.value))) {
                alert("El DNI del contacto debe tener 8 digitos");
                form.txtContactDocumentNumber.focus();
                return false;
            }

            if (form.cmbContactDocumentType.value == 'RUC') {
                alert("El tipo de documento RUC no est� permitido para datos de contacto.");
                form.cmbContactDocumentType.focus();
                return false;
            }

            //Validaciones para el email
            if (form.txtContactEmail.value != "") {
                //Validaciones para el email
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
                //Validaciones para el numero celular
                var regExpNotAllowWhiteSpaces = /^\S*$/;
                if (!regExpNotAllowWhiteSpaces.test(form.txtContactPhoneNumber.value)) {
                    alert("El numero celular no puede contener espacios en blanco");
                    form.txtContactPhoneNumber.focus();
                    return false;
                }

                var regExpOnlyNumber = /^[0-9]+$/;
                if (!regExpOnlyNumber.test(form.txtContactPhoneNumber.value)) {
                    alert("El numero celular del Contacto solo debe contener n�meros");
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
            var traceabilityValidation = form.hdnTracebilityValidation.value;

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
                if (form.txtContactFirstName.value != "" || form.txtContactLastName.value != "" || form.cmbContactDocumentType.value != "" || form.txtContactDocumentNumber.value != "") {
                    if (!(form.txtContactFirstName.value != "" && form.txtContactLastName.value != "" && form.cmbContactDocumentType.value != "" && form.txtContactDocumentNumber.value != "")) {
                        alert("Completar los dem�s campos del Contacto, el numero celular y el email son opcionales"); //DERAZO REQ-0940
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

                if(form.cmbFormaPago.value=="<%=Constante.PAYFORM_CARGO_EN_RECIBO%>" && flagvep== 0){
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
          alert("Debe seleccionar una Direcci�n de Entrega");
          return false;
        }
      }
      
      //FBERNALES SP3103
      if(!fxSearchAddressBlackList()){
            return;
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
            alert("Debe ingresar la fecha de suspensi�n");
            form.txtFechaProceso.focus();
            return false;
            }
            if (form.txtFechaReconexion.value == "") {
            alert("Debe ingresar la fecha de reconexi�n");
            form.txtFechaReconexion.focus();
            return false;
            }            
         }
			
      }catch(e){}
      
      
      if( trim(form.txtNumSolicitud.value).length == 0 ){
         alert("Debe ingresar el N�mero de Solicitud");
         return false; 
      }
      
      if( form.cmbSubCategoria.options[0].selected ){
         alert("Debe seleccionar una Sub Categor�a");
         return false;
      }
      
      <%
   	  // Inicio [reason Change of model] CDIAZ
      if(arrSpecificationsChangeModel != null && arrSpecificationsChangeModel.size()>0) {
      	for (int i = 0; i < arrSpecificationsChangeModel.size(); i++) {
      		TableBean specificationChangeModelBean = (TableBean)arrSpecificationsChangeModel.get(i);
      %>
      if (form.cmbSubCategoria.value == '<%=specificationChangeModelBean.getNpValue()%>' && form.cmbReasonCdm.value == '' ){
    	  alert("Debe seleccionar un Motivo de Cambio de Modelo");
    	  return false;
      }
      <%
      	}
      }
      // Fin [reason Change of model] CDIAZ
      %>
      
      //PRY - 0890 JBALCAZAR
      var flagIsProrrateo = $("input[name='hdnIsApportionment']").val();      
      var flagitemSA =false;
      var boton_show_apportioment = $("input[name='hdnApportionment']").val();
      var flag_btnPA = $("#v_savePagoAnticipado").val();
      try{      	
    	  
    	if(flagIsProrrateo==true || flagIsProrrateo=='true'){    		    
    	  if(boton_show_apportioment=="" || boton_show_apportioment=="undefined" || boton_show_apportioment==undefined){
    		  	 alert("Debe elegir si aplicar� o no el pago anticipado de prorrateo.");	
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
              alert("Solo se puede registrar un Item asociado a esta categor�a")
              return false;
            }            
         }
			
      }catch(e){}    
  /* Fin Data */
      return true;
      
   }

function validateFechaProg(){
    if(!document.getElementById("txtFechaProgAuto")) return true;
    var txt_fecha=document.getElementById("txtFechaProgAuto");
    //alert('es menor?: '+isSmallerCurrentDate(txt_fecha.value));

    if(txt_fecha.value !='') {
        //se verifica si el formato y la fecha son validas
        if (!isValidDate(txt_fecha.value))return false;
        //se verifica si la fecha ingresada es menor a la actual
        if(isSmallerCurrentDate(txt_fecha.value)){
            alert("La fecha de programaci�n autom�tica no debe ser menor a hoy.");
            txt_fecha.focus();
            return false;
        }
    }
    return true;
}

function isSmallerCurrentDate(date1){
    var q = new Date();
    var m = q.getMonth();
    var d = q.getDate();
    var y = q.getFullYear();

    var date = new Date(y,m,d);

    date1=date1.replace(/(\d+)\/(\d+)\/(\d+)/, "$3/$2/$1");
    var mydate=new Date(date1);

    mydate.setHours(0, 0, 0, 0);
    date.setHours(0, 0, 0, 0);

//alert('mydate: '+mydate+', date: '+date);

    if(mydate<date)
    {
        return true;
    }
    else
    {
        return false;
    }
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
   
/*Developer : Eduardo Pe�a EPV
 Objetivo  : Validar la cantidad de items registrados en la tabla
 */

function fxValidateItemVolume(){

    var cantidad=sumaCantidadesItemsTabla();
    var subcategoriaModel=form.cmbSubCategoria.value;
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
                        'Cantidad m�xima de modelos: '+value+' \n'+
                        'Cantidad Actual de Modelos ingresados: '+cantidad+' \n'+
                        'La orden no puede ser grabada.');

                    }else{

                        retorno=false;
                    }

                }else{
                    retorno =true;

                }
            }else{
                retorno =true;
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
/*FBERNALES Requerimiento PM0010503 Validacion del numero de solicitud en Retail  */
function fxValidateNumSolicitud(){

      var numSolicitud=form.txtNumSolicitud.value;     
      var url_server = '<%=strURLOrderServlet%>';
      var params = 'myaction=GetValidateNumSolicitud&numSolicitud='+numSolicitud+'&specificationid='+form.cmbSubCategoria.value;      
      var retorno=false;
      var subcatResult;
      var message, flag, value;
      
      $.ajax({
          url: url_server,
          data: params,
          async: false,
          type: "POST",
          success:function(data){
              subcatResult=data.split(',');
              flag=subcatResult[0];
              value=subcatResult[1];
              if(value!='NULL'){
                    if(value=='true'){
                        retorno =true;                           
                    }else{
                        retorno=false;
                    }
              }else{
                  retorno=false;
              }              
          },
          error:function(data){
              subcatResult=data.split(',');
              flag=subcatResult[0];
              value=subcatResult[1];
              retorno=false;
              alert('ERROR :'+message);

          }
      });
      return retorno;
  }
/*JVERGARA PRY 0787
* */
function fxValidateBiometricVerif(){
    form = document.frmdatos;

    if(form.hdnFlgViaType.value !=0 && (form.hdnVerifId.value != null || form.hdnVerifId.value != '')){

        var verifType =$(".chkVerifId:checked").attr("veriftype");

        if(verifType=='NOT BIOMETRIC'){
            if(!confirm("La verificaci�n de identidad seleccionada es No Biom�trica. Los documentos Digitales se generar�n sin Firma Digital. " +
                    "�Desea continuar?")){
                return false;
            }
        }else if(verifType=='EXONERATE'){
            if(!confirm("La verificaci�n de identidad seleccionada es Exonerada. Los documentos Digitales se generar�n sin Firma Digital" +
                    "�Desea Continuar?")){
                return false;
            }
        }
        return true;
    }

    return true;
}
/*Developer : Eduardo Pe�a EPV
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
            var nppaymenttermsiq=document.frmdatos.txtPaymentTermsIQ.value;

            if (wn_customerId==""){
                var txtCompanyId=document.frmdatos.txtCompanyId.value;
                wn_customerId=txtCompanyId;
            }

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
</script>
<!-- SP3103 -  FBERNALES VALIDACION DE DIRECCIONES DE RIESGO -->
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/jquery-1.10.2.js"></script>   
<script> 
    var jQuery_1_10_2 = $.noConflict(true);
    //SP3103 -  FBERNALES VALIDACION DE DIRECCIONES DE RIESGO
     function fxSearchAddressBlackList(){
        var vform = document.frmdatos;
        var hdnOrderId=vform.hdnNumeroOrder.value;
        var txtCompanyId=vform.txtCompanyId.value;
        var hdnSessionId=vform.hdnSessionLogin.value;    
        var txtAddress1s = document.getElementsByName("address1");
        var address1CmbIds = document.getElementsByName("address1CmbId");
      // Si ha sido modificado una direcci?n de entrega
      var txtAddress2s = document.getElementsByName("hdnDeliveryAddress");  
      //var address1CmbId2s = document.getElementsByName("hdnDeliveryCityId");
      var url_server = '<%=strURLOrderServlet%>';    
      var retorno = false;
   
      var allAddress= [];
      var evalAddress= [];
        var allDistri= [];
      // Direcciones anteriores
      for (var i=0; i<txtAddress1s.length; i++){  
        var txtAddress= txtAddress1s[i].value; 
        var address1CmbId= address1CmbIds[i].value; 
        //txtAddress=txtAddress.replace(/\s/g, "");
        var arr_direcciones = [txtAddress];
        var arr_distri = [address1CmbId];
        allAddress[i]=arr_direcciones;
        allDistri[i]=arr_distri;
      }
      //Direcciones editadas    
      for (var j=0; j<txtAddress2s.length; j++){    
        var txtAddress= txtAddress2s[j].value; 
         var address1CmbId2= vform.hdnDeliveryCityId.value; 
        //txtAddress=txtAddress.replace(/\s/g, "");
        var arr_direcciones = [txtAddress];
        var arr_distri = [address1CmbId2];
        allAddress[i+j]=arr_direcciones;  
        allDistri[i+j]=arr_distri;
        
      }
        var countVal = 0;
      for (var i=0; i< allAddress.length ; i++){
        var txtAddress= allAddress[i][0];
        var txtAddress2=txtAddress;
        txtAddress=txtAddress.replace(/\s/gi, "");
        
        var cmbDist = allDistri[i][0];
        var flag=false;
        
        if(i==0){
          var arr_direcciones = [txtAddress];
          evalAddress[countVal]=arr_direcciones; 
          retorno=fxValidateAddressBlackList(txtAddress2,hdnSessionId,hdnOrderId,txtCompanyId,cmbDist,url_server);
          countVal++;
        }else{
          for(var j=0; j< evalAddress.length ; j++){
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
            retorno=fxValidateAddressBlackList(txtAddress2,hdnSessionId,hdnOrderId,txtCompanyId,cmbDist,url_server);
            countVal++;
          }
          
        }
        if(!retorno){
            return false;
        }
      }

      return retorno;
    }

    function fxValidateAddressBlackList(txtAddress,hdnSessionId,hdnOrderId,txtCompanyId,cmbDist,url_server){
      var txtAddress2 = txtAddress.replace(/\s/gi, "");
      var vform = document.frmdatos;
      var retorno=false;
      var params = 'myaction=GetValidarDireccionRiesgo&txtAddress='+txtAddress2+'&cmbDist='+cmbDist+'&specificationid='+vform.cmbSubCategoria.value;          
        jQuery_1_10_2.ajax({
            url: url_server, 
            data: params,
            async: false,
            type: "POST",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success:function(data){
                if(data.flag){
                    var confirma = confirm("La direccion ingresada  " +txtAddress+" esta registrada en la base datos de fraude.\n ?Desea continuar?");
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
                alert("Se produjo un error al intentar validar la direcci?n");   
            }
        });
        return retorno;
    }
    // FBERNALES - 17/12/2015 INSERTA LOGS VALIDACION
function insLogValidateAddress(correlacion,direccion,usuario,trxId,companyId){
      var url_server = '<%=strURLOrderServlet%>';
      var params = 'myaction=GetInsertLogValidateAddress&direccion='+direccion+'&correlacion='+correlacion+'&createdBy='+usuario+'&npnumorder='+trxId+'&npidcliente='+companyId;
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
        alert("Se produjo un error al intentar validar la direcci?n");  
        }
      });   
      return retorno;
    }
    // FIN FBERNALES
   
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