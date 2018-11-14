<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-2" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@ page import="oracle.portal.provider.v2.ProviderSession" %>
<%@ page import="oracle.portal.provider.v2.ProviderUser" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<%@ page import="pe.com.nextel.bean.PortalSessionBean" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="java.util.*" %>
<%@ page import="pe.com.nextel.bean.DocumentoDigitalLinkBean" %>
<%@ page import="pe.com.nextel.service.SessionService"%>
<%@ page import="pe.com.nextel.exception.SessionException" %>
<%@ page import="pe.com.nextel.service.DigitalDocumentService" %>
<%@ page import="org.apache.commons.collections.OrderedIterator" %>
<%@ page import="pe.com.nextel.service.CustomerService" %>
<%@ page import="pe.com.nextel.bean.CustomerBean" %>
<%@ page import="pe.com.nextel.bean.OrderBean" %>
<%@ page import="pe.com.nextel.service.EditOrderService" %>



<%
    System.out.println("---------------------- INICIO JP_DIGITAL_DOCUMENT_ATTACHMENT.jsp-------------------------------------");
    System.out.println("-- Objective: Attach required documents and send them through SOAINT service to Amazon repository  --");
    System.out.println("-- Owner    : Jefferson Vergara Perez Mail-to: jefferson.vergara@soapros.pe -------------------------");
    System.out.println("-- Project  : [PRY - 0787] VIDD - \"Verificación de Identidad y Digitalización de Documentos \" -----");
    System.out.println("-----------------------------------------------------------------------------------------------------");

    try {

        System.out.println("Obtener Portlet Request para la creacion del Portlet 0805");

        String userLogin = "";
        String strSessionId = "";

        try {
            PortletRenderRequest pReq = (PortletRenderRequest) request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
            ProviderUser objetoUsuario = pReq.getUser();
            strSessionId = objetoUsuario.getPortalSessionId();
            System.out.println("[JP_DIGITAL_DOCUMENTS_ATTACHMENT] Session : " + objetoUsuario.getName() + " - " + strSessionId );
        } catch(Exception e) {
            System.out.println("[JP_DIGITAL_DOCUMENTS_ATTACHMENT] Portlet Not Found: " + e.getClass() + " - " + e.getMessage() );
            out.println("Portlet JP_DIGITAL_DOCUMENTS_ATTACHMENT Not Found");
            return;
        }

        System.out.println("Obtener Session del Usuario");

        PortalSessionBean portalSessionBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
        if(portalSessionBean==null) {
            System.out.println("[JP_DIGITAL_DOCUMENTS_ATTACHMENT] No se encontró la sesión de Java -> " + strSessionId);
            throw new SessionException("La sesión finalizó");
        }

        userLogin=portalSessionBean.getLogin();

        System.out.println("Obtener datos de request "+request.getContextPath()+" "+request.getRequestURI());
        String strRutaContext=request.getContextPath();


        //Obteniendo los datos de la Orden
        HashMap hshOrder=null;
        String strMessage=null;
        OrderBean objOrderBean=null;

        CustomerBean objCustomerBean;
        //Declarando servicio para obtener datos de Customer
        CustomerService customerService = new CustomerService();
        //Declarando servicio para obtener datos de Ordenes
        EditOrderService objOrderService=new EditOrderService();

        //Parametros Ordenes o Incidentes
        String strOrderId=(request.getParameter("an_nporderid")==null?"0":request.getParameter("an_nporderid"));
        long lOrderId0=Long.parseLong(strOrderId);

        String incidentId=(request.getParameter("incidentId")==null?"0":request.getParameter("incidentId"));
        long lincidentId=Long.parseLong(incidentId);

        System.out.println("Obtener datos de transaccion Orden: "+strOrderId+" Incidente: "+incidentId);

        //Variables comunes
        int specificationId= 0;
        String trxId;
        String customerId="";
        String strCustomerDocType= "";
        String strCustomerNumDoc = "";
        int source;
        String trxStatus="";

        //Si el portlet funciona en Incidente, sino Ordenes
        if(incidentId!="0"){
            trxId=incidentId;
            source=Constante.SOURCE_INCIDENT_ID;
            customerId=(request.getParameter("customerId")==null?"0":request.getParameter("customerId"));




        }else{
            trxId= strOrderId;
            source=Constante.SOURCE_ORDERS_ID;
            hshOrder=objOrderService.getOrder(lOrderId0);
            strMessage=(String)hshOrder.get("strMessage");

            if (strMessage!=null)
                throw new Exception(strMessage);

            //Obtener el bean de Ordenes
            objOrderBean=(OrderBean)hshOrder.get("objResume");

            trxStatus=objOrderBean.getNpStatus();

            //Obtener datos del bean de ordenes y de customer
            customerId=String.valueOf(objOrderBean.getCsbCustomer().getSwCustomerId());
            strCustomerDocType = objOrderBean.getCsbCustomer().getNpTipoDoc();
            strCustomerNumDoc = objOrderBean.getCsbCustomer().getSwRuc();
        }

        if(!MiUtil.getString(customerId).equals("")){
            HashMap hashCustomer;
            hashCustomer=customerService.getCustomerData(Long.parseLong(customerId));
            strMessage = (String)hashCustomer.get("strMessage");
            if ( strMessage != null  )
                throw new Exception(strMessage);
            objCustomerBean = (CustomerBean)hashCustomer.get("objCustomerBean");
            strCustomerDocType = objCustomerBean.getNpTipoDoc();
            strCustomerNumDoc = objCustomerBean.getSwRuc();
        }

        int ibuidingid =portalSessionBean.getBuildingid();

       String data = "?myaction=setDocumentAttachment" +
                     "&trxId="+trxId+
                     "&source="+source+
                     "&customerId="+customerId+
                     "&userLogin="+userLogin+
                     "&strCustomerDocType="+strCustomerDocType+
                     "&strCustomerNumDoc="+strCustomerNumDoc;

       String  actionURL_DocAttServlet = strRutaContext+"/digitalDocumentServlet"+data;

       String actionURL_DigitalDocumentServlet = strRutaContext+"/digitalDocumentServlet";

%>
<script type="text/javascript" src="<%=request.getContextPath()%>/websales/Resource/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/websales/Resource/js/jquery.form.js"></script>


    <!-- Seccion Adjuntar Documentos  Inicio -->
    <div id="dvAttachDocumentSection">
        <!-- Seccion Seleecionar tipo de Documento  Inicio -->
        <div id="dvSelectDocumentTypeSection">
            <table class="PortletHeaderColor" border="0" cellspacing="0" cellpadding="0" width="1100">
                <tr class="PortletHeaderColor">
                    <td class="LeftCurve" valign="top" align="left" width="18"></td>
                    <td class="PortletHeaderText" align="left" valign="top">Adjuntar Documentos</td>
                    <td class="RightCurve" valign="top" align="right" width="11"></td>
                </tr>
            </table>
            <table  class="RegionBorder" border="0" width="1100" cellpadding="2" cellspacing="1">
                <tr>
                    <td class="RegionHeaderColor" width="100%">
                        <table border="0" cellpadding="0" cellspacing="1" width="100%" style="text-align: left;">
                            <tr>
                                <td class="CellLabel" width="30%">Tipo de Elemento</td>
                                <td class="CellContent" width="70%"><select name="cmbElement"  id="cmbElement"/></td>
                            </tr>
                        </table>

                    </td>
                </tr>
            </table>
            <table id="tbButtonAddFiles"  border="0" width="1100" cellpadding="2" cellspacing="1">
                <tr>
                    <td class="RegionHeaderColor" width="100%">
                        <table  border="0" cellpadding="0" cellspacing="1" width="100%" style="text-align: left;">
                            <tr>
                                <td width="30%" align="left"><input type="button" onclick="addInputFile()" value="Agregar Archivo"/></td>
                                <td width="60%" align="center">&nbsp;</td>
                                <td width="10%" align="center">&nbsp;</td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>

            <br>
        </div>
            <!-- Seccion Seleecionar tipo de Documento  Fin -->
            <!-- Seccion Listar Filas de documentos a Adjuntar  Inicio -->
        <div id="dvLstAttachedDocumentSection">
            <form method="post" id="uploadForm" action="<%=actionURL_DocAttServlet%>" enctype="multipart/form-data;ISO-8859-1">
            <table border="0" cellspacing="0" cellpadding="0" width="1100">
                <tr class="PortletHeaderColor">
                    <td class="LeftCurve" valign="top" align="left" width="18"></td>
                    <td class="PortletHeaderText" align="left" valign="top">Listado de Documentos a Adjuntar</td>
                    <td class="RightCurve" valign="top" align="right" width="11"></td>
                 </tr>
            </table>
                <table  class="RegionBorder" border="0" width="1100" cellpadding="2" cellspacing="1">
                    <tr>
                        <td class="RegionHeaderColor" width="100%">
                            <table id="uploadTable" border="0" cellpadding="0" cellspacing="1" width="100%" style="text-align: left;">
                                <tr>
                                    <td class="CellLabel" width="30%" align="center">Elemento</td>
                                    <td class="CellLabel" width="60%" align="center">Archivo a Seleccionar</td>
                                    <td class="CellLabel" width="10%" align="center">Eliminar</td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
                <table id="tbButtonSendFiles"   border="0" width="1100" cellpadding="2" cellspacing="1">
                    <tr>
                        <td class="RegionHeaderColor" width="100%">
                            <table  border="0" cellpadding="0" cellspacing="1" width="100%" style="text-align: left;">
                                <tr>
                                    <td width="30%" align="center">&nbsp;</td>
                                    <td width="60%" align="center"><input class="btn btn-primary" type="submit" id="send" value="Enviar Archivos"/></td>
                                    <td width="10%" align="center">&nbsp;</td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </form>
             <br>
         </div>
          <!-- Seccion Listar Filas de documentos a Adjuntar  Inicio -->
    </div>
    <!-- Seccion Adjuntar Documentos  Fin -->
 <script type="text/javascript">

     var totalcountOfUploadFiles = 0;
     var uploadFilesLimit = 0;

     getComboDocTypeAttach();
     getLimitDocTypeAttach();
     getAttachSectionStatus();

     function getAttachSectionStatus(){

         $.ajax({
             url:"<%= actionURL_DigitalDocumentServlet%>",
             type:"GET",
             async:false,
             cache:false,
             data:{
                 "myaction":"getAttachSectionStatus",
                 "origen" : "<%=source%>",
                 "typetrx" : "<%=trxId%>",
                 "ibuidingid":"<%=ibuidingid%>"
             },
             dataType:"json",
             success:function(data){
                 var message=data.strMessage||'';
                 var resultado = data.strResult;
                 if(message==''){
                     if(resultado==0){
                         $('#tbButtonSendFiles').show();
                         $('#tbButtonAddFiles').show();
                     }else{
                         $('#tbButtonSendFiles').hide();
                         $('#tbButtonAddFiles').hide();
                     }
                 }else{
                    alert(message);
                 }
             },
             error: function(xhr, status, error) {
                 alert(xhr.responseText);
             }
         });
     }

     function getComboDocTypeAttach(){

         var cmbDocTypeAttach=$('#cmbElement');

         $.ajax({
             url:"<%= actionURL_DigitalDocumentServlet%>",
             type:"GET",
             async:false,
             cache:false,
             data:{
                 "myaction":"getCombo",
                 "domain" : "<%= Constante.ATTACH_DOC_TYPE %>",
                 "origen" : "<%= Constante.SOURCE_ALL_ID %>",
                 "typetrx" : "<%= Constante.TYPE_TRX_ALL_ID %>",
                 "channel" : "<%=Constante.CHANNEL_ALL_ID%>",
                 "section" : "<%= Constante.SECTION_ALL_ID%>"
             },
             dataType:"json",
             success:function(data){
                 $.each(data.objArrayList, function (i, item) {
                     cmbDocTypeAttach.append($("<option />").val(item.npvalue).text(item.npvaluedesc));
                 });
             },

             error: function(xhr, status, error) {
                 alert(xhr.responseText);
             }
         });
     }

     function getLimitDocTypeAttach(){

         $.ajax({
             url:"<%= actionURL_DigitalDocumentServlet%>",
             type:"GET",
             async:false,
             cache:false,
             data:{
                 "myaction":"getCombo",
                 "domain" : "<%= Constante.ATTACH_DOC_LIMIT_COUNT %>",
                 "origen" : "<%= Constante.SOURCE_ALL_ID %>",
                 "typetrx" : "<%= Constante.TYPE_TRX_ALL_ID %>",
                 "channel" : "<%=Constante.CHANNEL_ALL_ID%>",
                 "section" : "<%= Constante.SECTION_ALL_ID%>"
             },
             dataType:"json",
             success:function(data){
                 $.each(data.objArrayList, function (i, item) {
                     uploadFilesLimit = item.npvalue;
                 });
             },

             error: function(xhr, status, error) {
                 alert(xhr.responseText);
             }
         });
     }

     var options = {
         dataType: "text",
         type:"POST",
         async:false,
         beforeSubmit: validate,
         cache:false,
         success:       showResponse,// post-submit callback
         clearForm: false,        // clear all form fields after successful submit
         error:   showerror
     };

     function showResponse(responseText, statusText, xhr, $form)  {
         $('#send').attr("disabled", false);
         var respuesta = $.parseJSON(responseText);
         if(respuesta.strMessage=='<%=Constante.CODIGO_RESP_OSB_OK%>'){
             alert("<%=Constante.MESSAGE_ALERT_ATT_DOC_OK%>");
             fxcleanrows();
         }else{
             alert(respuesta.strMessage);
         }
     }

     function showerror(xhr,status,error){
         $('#send').disabled = false;
         alert(xhr.responseText +"/"+status+"/"+error);
     }

     function fxcleanrows(){
         var trs=$('#uploadTable tr').length;
         while(trs>1)
         {
             // Eliminamos la ultima columna
             trs--;
             $("#uploadTable tr:last").remove();
         }
         totalcountOfUploadFiles = 0;

     }

     // bind form using 'ajaxForm'
     $('#uploadForm').ajaxForm(options);

     function validate() {
         $('#send').attr("disabled", true); // deshabilitar boton
         var continueInvoke=true;
         var trs=$('#uploadTable tr').length;
         if(trs==1){
             alert("No hay archivos para enviar ");
             $('#send').attr("disabled", false);
             continueInvoke = false;
             return false;
         }
         $("#uploadForm input[required]").each(function(){

             /* If the element has no value. */
             if($(this).val() == ""){
                 alert("Debe elegir un archivo ");
                 $('#send').attr("disabled", false);
                 continueInvoke = false;     /* Set the variable to false, to indicate that the form should not be submited. */
                 return false;
             }

         });
         return continueInvoke;
     }

     function addInputFile() {
         getLimitDocTypeAttach();
         if(totalcountOfUploadFiles<uploadFilesLimit) {

             var tipoElemento=$('#cmbElement option:selected').text();
             var tipoElementoVal = $('#cmbElement option:selected').val();
             $('#uploadTable').append('<tr class="CellContent"><td>'+tipoElemento+'</td>'+
             '<td><input type="file" class="input-file" style="width: 400px" name=\"'+tipoElementoVal+'\" required="required"/></td>' +
             '<td align="center"><a onclick="javascript:eliminar(this);" shape=""><img width="15" height="20" alt="Eliminar" src="/websales/images/Eliminar.gif" border="no"></a></td></tr>');

             totalcountOfUploadFiles++;
         }else{
             alert("El límite de archivos a enviar es "+ uploadFilesLimit+" por transaccion");
         }
     }

     function addRowFiles(input) {
         var tr='<tr><td>'+input.name+'</td><td><a href="javascript:deleteFile(this,'+i+')">Eliminar</a></td></tr>';
         return tr;
     }

     function eliminar(input) {
         totalcountOfUploadFiles--;
         $(input).parent().parent().remove();
         //delete files[element];
     }

     //load combo criterio
     function addResultOption(element,value, description) {
         element.options[element.options.length] = new Option(description, value);
     }

 </script>
<%
    }catch(SessionException se) {
        se.printStackTrace();
        System.out.println("[JP_DIGITAL_DOCUMENTS_ATTACHMENT.jsp][Finalizó la sesión]");
    }catch(Exception e) {
        out.println("<script>alert('" + MiUtil.getMessageClean(e.getClass() + " - " + e.getMessage()) + "');</script>");
    }
%>
