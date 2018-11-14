<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@page import="oracle.portal.provider.v2.ProviderSession" %>
<%@page import="oracle.portal.provider.v2.ProviderUser" %>
<%@page import="oracle.portal.provider.v2.render.PortletRendererUtil" %>
<%@page import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil" %>
<%@page import="pe.com.nextel.bean.PortalSessionBean" %>
<%@page import="pe.com.nextel.exception.SessionException" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@page import="pe.com.nextel.util.MiUtil"%>
<%@page import="java.util.*"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="pe.com.nextel.service.NewOrderService"%>
<%@page import="pe.com.nextel.service.SessionService"%>
<%@page import="pe.com.nextel.service.GeneralService"%>
<%@page import="pe.com.nextel.bean.*"%>
<%@page import="pe.com.nextel.service.*" %>

<%

try{

  String strSessionIdOrder = "";

  try{
    PortletRenderRequest pReq = (PortletRenderRequest) request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
    ProviderUser objetoUsuario1 = pReq.getUser();
    strSessionIdOrder=objetoUsuario1.getPortalSessionId();
    System.out.println("Sesión capturada  JP_ORDER_NEW_ORDER_ShowPage : " + objetoUsuario1.getName() + " - " + strSessionIdOrder );
  }catch(Exception e){
    System.out.println("Portler Not Found : " + e.getClass() + " - " + e.getMessage() );
    out.println("Portlet JP_ORDER_NEW_ORDER_ShowPage Not Found");
    return;
  }
  
  System.out.println("Sesión capturada después del resuest : " + strSessionIdOrder );
	PortalSessionBean portalSessionBean3 = (PortalSessionBean)SessionService.getUserSession(strSessionIdOrder);
	if(portalSessionBean3==null) {
    System.out.println("No se encontró la sesión de Java ->" + strSessionIdOrder);
		throw new SessionException("La sesión finalizó");
	}
  
   //<!--jsalazar - modif hpptt # 1 - 29/09/2010 -Inicio-->
   Date today=MiUtil.getDateBD("dd/MM/yyyy");
   String strToday=MiUtil.getDate(today,"dd/MM/yyyy");
    //<!--jsalazar - modif hpptt # 1 - 29/09/2010 -Fin-->
   int iUserId=0;
   int iAppId=0;   
   int iSalesStructId=0;

   String strValidType = ""; // ocruces N_O000046759 Control de Tipo de Operación vs Tipo de Orden
   String strMensajeTypeOpera = ""; // ocruces N_O000046759 Control de Tipo de Operación vs Tipo de Orden

   iUserId  = portalSessionBean3.getUserid();
   iAppId   = portalSessionBean3.getAppId();  
   iSalesStructId = portalSessionBean3.getSalesStructId();
   

   String strOrderId = null;
   String strMessage = null; 
   String strParentUrl = "";
   String strParentName = "";
   Hashtable hOrder = NewOrderService.OrderDAOgetOrderIdNew();
   
   /* Inicio Data */
   String strDataSalesmanLabel       = "<td class='CellLabel' colspan=1 align='center' name='vendedorDataLabel' id='vendedorDataLabel'>Vendedor Data</td>";
   String strDataSalesmanHiddenLabel = "<td class='CellLabel' colspan=1 align='center' name='vendedorDataLabel' id='vendedorDataLabel'>&nbsp;</td>";
   String strDataSalesmanField       = "<td class='CellContent' colspan=1 id='vendedorData' name='vendedorData'>&nbsp;<select name='cmbVendedorData' onchange='fxChangeSalesManData(this);'><option value=''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option></select></td>";
   String strDataSalesmanHiddenField = "<td class='CellContent' colspan=1 id='vendedorData' name='vendedorData'>&nbsp;</td> ";   
   /* Fin Data */
    
   if ( hOrder!= null)
        strOrderId = (String)hOrder.get("wn_orderid");

   //CGC COR0407
   String strSalesId = request.getParameter("salesTeamId")==null?"":request.getParameter("salesTeamId");        
   String strRegionId = request.getParameter("regionId")==null?"":request.getParameter("regionId");
   System.out.println("[strRegionId]"+strRegionId);
   
   String strGeneratorid       = request.getParameter("generatorid")==null?"":request.getParameter("generatorid");
   String strGeneratortype     = request.getParameter("generatortype")==null?"":request.getParameter("generatortype");
   
   //Trayendo el numero de dias (59 dias) para calcular la fecha final de suspensiones
   GeneralService objService = new GeneralService();
   HashMap hshMaxNumDias = objService.getDominioList("MAX_DIAS_SUSPENSION");
   ArrayList arrModalitySellList = (ArrayList) hshMaxNumDias.get("arrDominioList");
   DominioBean diasMaximos = (DominioBean)arrModalitySellList.get(0);
   GeneralService objGeneralService = new GeneralService();
  
   if ( !MiUtil.getString(strGeneratorid).equals("") && !MiUtil.getString(strGeneratortype).equals("") ){    
    HashMap hshResult = new HashMap();
    hshResult = objGeneralService.getObjectTypeUrl(strGeneratortype);
    strMessage = (String)hshResult.get("strMessage");
    if ( strMessage != null  )
      throw new Exception(strMessage);
    strParentUrl = (String)hshResult.get("strUrl");
    strParentName = (String)hshResult.get("strName");    
   }
   
   //Validaciones del valor de la estructura de ventas del usuario generador de la Orden
   //-----------------------------------------------------------------------------------
   int iSalesStructOrigenId=0;
   HashMap hshResultStruct = new HashMap();
   hshResultStruct = objGeneralService.getSalesStructOrigen(iSalesStructId,Constante.SALES_STRUCT_PORTAL_DEFAULT) ;
   strMessage = (String)hshResultStruct.get("strMessage");
   if ( strMessage != null )
       throw new Exception(strMessage);
    iSalesStructOrigenId = MiUtil.parseInt((String)hshResultStruct.get("iSalesStructOrigen"));
  
   System.out.println("<<<<<<Datos de SalesStructId>>>>>>");
   System.out.println("iSalesStructId:"+iSalesStructId);
   System.out.println("iSalesStructOrigenId:"+iSalesStructOrigenId);
   
   
   //Inicio [Despacho en Tienda] RMARTINEZ
   NewOrderService objOrderService=new NewOrderService();   
   HashMap hSpecificationsList = objOrderService.showCourierSpecificationsId();
   ArrayList arrDominioList = (ArrayList) hSpecificationsList.get("arrDominioList");
   int iShowCourier = 0;
   //Fin [Despacho en Tienda] RMARTINEZ   
   
  //ocruces N_O000046759 Control de Tipo de Operación vs Tipo de Orden
   System.out.println("<<<<<<N_O000046759>>>>>>");
   HashMap hshValidateTypOpe=new HashMap();
   hshValidateTypOpe = objGeneralService.getValidateTypOpe("VALIDATE_CREDIT_EVALUATION");
   strValidType = (String)hshValidateTypOpe.get("strValidTypOpe");
   System.out.println("strValidType:"+strValidType);
   String strCustomerId = request.getParameter("customerId")==null?"":request.getParameter("customerId");
   System.out.println("strCustomerId : "+strCustomerId);

   NewOrderService objOrderTypeOpeService=new NewOrderService();
   HashMap hTypeOpeSpecificationsList = objOrderTypeOpeService.showTypeOpeSpecificationsId(strCustomerId);
   ArrayList arrTypeOpeSpecifications = (ArrayList) hTypeOpeSpecificationsList.get("arrTypeOpeSpecifications");
   //Fin ocruces N_O000046759 Control de Tipo de Operación vs Tipo de Orden

%>
<!--START MSOTO: 08-04-2014 SAR N_O000019563 Se deshabilita el combo--> 
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/jQuery-min.js"></script>

  <!-- DATOS DEL PEDIDO - INICIO -->
  <script>
    window.parent.document.title='<%=strOrderId%>';
    function fCopiaOdenId(){
      
        
      var wn_PedidoId         = "<%=strOrderId%>";
    
      form = document.frmdatos;
      form.txtNumSolicitud.value = "P" + wn_PedidoId;
      form.txtNumSolicitud.focus();
      return;
   };
   
   function fxShowParent(GeneratorId, parentUrl){

      if (parentUrl!=null){
       //location.replace(parentUrl+GeneratorId)
       var strUrl=parentUrl+GeneratorId;
       var frameUrl=strUrl;
       var winUrl = "/portal/pls/portal/WEBSALES.NPSL_NEW_GENERAL_PL_PKG.PL_FRAME?av_url="+escape(frameUrl);
       window.open(winUrl, "Incidencia","status=yes, location=no, width=1040, height=800, left=100, top=100, scrollbars=yes, screenX=50, screenY=100");
      
      }
   }   
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
     
     //<!--jsalazar - modif hpptt # 1 - 27/09/2010 -inicio-->
   function fxValidateFechaFin(obj){ //Agregado para el la validacion de fechas en suspensiones
       form = document.frmdatos;
       var wv_dateProceso = form.txtFechaProceso.value;   
       var wv_dateFinProg = form.txtFechaFinProg.value; 
       var wv_message = "La fecha Fin de Programación debe ser mayor a la fecha de Proceso";
       
       
       
       
       if ( wv_dateProceso != "" && wv_dateFinProg !=""){
       if ( !isValidDate(wv_dateFinProg)){
             form.txtFechaFinProg.value="";
             form.txtFechaFinProg.select();
             return;
       };           
          
        //Fecha Proceso            
        var wv_fProceso= new Date();
        var day_P    = parseFloat(wv_dateProceso.substring(0,2));
        var month_P  = parseFloat(wv_dateProceso.substring(3,5));
        var year_P   = parseFloat(wv_dateProceso.substring(6,10));

        //Fecha Reconexion        
        var day_Fin    = parseFloat(wv_dateFinProg.substring(0,2));
        var month_Fin  = parseFloat(wv_dateFinProg.substring(3,5));
        var year_Fin   = parseFloat(wv_dateFinProg.substring(6,10));
        
          if (year_Fin < year_P){
             alert(wv_message);
             form.txtFechaFinProg.value="";
             form.txtFechaFinProg.select();
             return;
          };
          if (year_Fin == year_P){
             if (month_Fin < month_P){
                alert(wv_message);
                form.txtFechaFinProg.value="";
                form.txtFechaFinProg.select();
                return;
             };
             if (month_Fin == month_P){
                if (day_Fin <= day_P){
                   alert(wv_message);
                   form.txtFechaFinProg.value="";
                   form.txtFechaFinProg.select();
                   return;
                };
             }
          }
       }
       if ( wv_dateProceso == "" && wv_dateFinProg !=""){
       alert("Debe ingresar la fecha de proceso");
       form.txtFechaFinProg.value="";
       form.txtFechaProceso.select();
       return;
       }

       
       return true;
    
    }
 
    function fxValidateFechaInicio(obj){ //Agregado para el la validacion de fechas en suspensiones
       
       form = document.frmdatos;
       var wv_dateProceso = form.txtFechaProceso.value;   
       var wv_dateToday = "<%=strToday%>"; 
       var wv_message = "La Fecha de Inicio de Programación debe ser mayor o igual a la fecha actual";
       var validar_fe_actual=false;
      // strFechPro
       if ( wv_dateProceso != ""){
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

        //Fecha actual        
        var day_today    = parseFloat(wv_dateToday.substring(0,2));
        var month_today  = parseFloat(wv_dateToday.substring(3,5));
        var year_today   = parseFloat(wv_dateToday.substring(6,10));
        
        
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
       
       return true;
    
    }
   
//<!--jsalazar - modif hpptt # 1 - 29/09/2010 - Fin-->

//CBARZOLA:PROPUESTAS
  function openPopUpProposedList(){
    form = document.frmdatos;
    v_siteId=form.cmbResponsablePago.value;
    v_specificationId=form.cmbSubCategoria.value;
    v_customerId=form.txtCompanyId.value;
    v_sallerId=form.cmbVendedor.value;
    v_parametros="?customerId="+v_customerId+"&siteId="+v_siteId+"&specificationId="+v_specificationId+"&sallerId="+v_sallerId
    url="<%=request.getContextPath()%>/htdocs/jsp/PopUpProposedList.jsp"+ v_parametros;
    var screenY, screenX,vleft, vtop;	
    if(navigator.appName == "Microsoft Internet Explorer") {
      screenY = document.body.offsetHeight;
      screenX = window.screen.availWidth;
    }
    else {
      screenY = window.outerHeight
      screenX = window.outerWidth
    }
    vleft=screen    
    vleft = (screenX - 250) / 2;
	vtop = (screenY - 250) / 2;
    WinAsist=window.open(url,"WinSearchProposed","width=250,height=250,status=no,resizable=no,scrollbars=yes,screenX=" +vleft+ ", top="+ vtop +",left=" + vleft + ",screenY=" + vtop );
  }
   
  function fxShowCourier(){
     //Inicio [Despacho en Tienda] RMARTINEZ
       var existe = 0;
       form = document.frmdatos;
       <%
        iShowCourier = 0;
        for (int i = 0; i < arrDominioList.size(); i++) {
            DominioBean dominio = (DominioBean) arrDominioList.get(i);
            String strSpecificationId = dominio.getValor(); 
            System.out.println("strSpecificationId" + strSpecificationId);
            %>
             if (form.cmbSubCategoria.value == '<%=strSpecificationId%>'){
                existe = 1;
                <%iShowCourier = 1;%>            
             }
       <%}%>
       //alert("existe: " + existe);
       if (existe == 1){
          dvCourier.style.display="";
          dvChkCourier.style.display="";          
       }else{
          dvCourier.style.display="none";
          dvChkCourier.style.display="none";     
          form.hdnChkCourier.value = "0";
          form.chkCourier.checked = false;
       }
      //Fin [Despacho en Tienda] RMARTINEZ
      
      return existe;
   }

  function fxValidateTypeOpe(vthis){
      form = document.frmdatos;
      var existe = 0;
      if (!<%=MiUtil.getString(strValidType).equals("0")%>){
           <%
            for (int i = 0; i < arrTypeOpeSpecifications.size(); i++) {
                DominioBean dominioTypeOpe = (DominioBean) arrTypeOpeSpecifications.get(i);
                String strSpecificationTypeOpe = dominioTypeOpe.getValor();
                System.out.println("strSpecificationTypeOpe" + strSpecificationTypeOpe);
                %>
                 if (form.cmbSubCategoria.value == '<%=strSpecificationTypeOpe%>'){
                    existe = 1;
                 }
                <%}
           %>
         if ((existe == 0) && (form.cmbSubCategoria.value != "")){
             alert("Última Evaluación Crediticia NO Coincide con la Categoría de Orden a Crear. Debe Pre-Evaluar Nuevamente con el Tipo de Operación Correcta");
             if (<%=MiUtil.getString(strValidType).equals("2")%>){
                form.cmbSubCategoria.value="";
                form.cmbSubCategoria.focus();
                return false;
             }
         }
      }
      SelectLoadSpecification(vthis);
      fxShowCourier();
  }

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
      var txtSite          = form.cmbResponsablePago.value;
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
            alert("formato de fecha no válido (dd/MM/yyyy)");
            return false;
      }
      return true;
   }
    
    function fxValidaDiasSuspension(){ 
       var cant_items = parent.mainFrame.vctItemsMainFrameOrder.size();
       form  = document.frmdatos;
       var npScheduleDate = form.txtFechaProceso.value;
       var npScheduleDate2 = form.txtFechaReconexion.value;
       var v_specificationId   = form.cmbSubCategoria.value;
       var phonoNumber = "";
       
       if (form.txtFechaProceso.value != form.hdnFechaProceso.value || form.txtFechaReconexion.value != form.hdnFechaReconexion.value){
          if (items_table.rows.length >= 2){  
                if( confirm("¿Está usted seguro que desea modificar la fecha. Se revalidará la cantidad de días de suspension por Item?") ){
                   form.hdnFechaProceso.value = form.txtFechaProceso.value;
                   form.hdnFechaReconexion.value = form.txtFechaReconexion.value;
                   
                   bucle1:
                   for(var i = 0 ; i < cant_items ; i++) {
                      vctItems = vctItemsMainFrameOrder.elementAt(i);
                      bucle2:
                      for (var j = 0 ; j < vctItems.size() ; j++) {
                         if (j == 0){
                            phonoNumber += replace(vctItems.elementAt(j).npobjitemvalue,"'","");                            
                            break bucle2;
                         }                          
                      }                      
                      phonoNumber += "/";
                   }
                                                       
                var url = "<%=request.getContextPath()%>/serviceservlet?paramPhoneNumber=" + phonoNumber +"&paramSpecification=" + v_specificationId + "&myaction=validaDiasSuspensionCrear&strNpScheduleDate="+ npScheduleDate + "&strNpScheduleDate2="+npScheduleDate2;
                form.action=url;
                form.submit();
                  
                }else{
                   form.txtFechaProceso.value = form.hdnFechaProceso.value;
                   form.txtFechaReconexion.value = form.hdnFechaReconexion.value;
                }
          }
       }
    }

  function fxValidateFormaPagoVEP(objComboFormaPago){
        //INICIO: PRY-1200 | AMENDEZ
        var iSpecificationId=form.cmbSubCategoria.value;
        if (validateSpecificationVep(iSpecificationId)){
        //FIN: PRY-1200 | AMENDEZ

          //INICIO: PRY-0864 | AMENDEZ
          if(objComboFormaPago.value == '<%=Constante.PAYFORM_CARGO_EN_RECIBO%>'){
              alert("REVISE LA ORDEN: Si se trata de VEP, seleccione opción. Luego NO podrá modificarse");
              document.frmdatos.chkVepFlag.disabled = false;
          }
          //FIN: PRY-0864 | AMENDEZ
      
          if (document.frmdatos.chkVepFlag != undefined){
              if (document.frmdatos.chkVepFlag.checked ==true){
                   if (fxValidateExistItemsVEP(document.frmdatos.cmbVepNumCuotas, 0)){
                       // Valida si existen Items con Modalidad Venta. Si es TRUE no existen
                      if (objComboFormaPago.value != '<%=Constante.PAYFORM_CARGO_EN_RECIBO%>'){
                         document.frmdatos.chkVepFlag.checked = false;
                         document.frmdatos.chkVepFlag.value = 0;
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
                      document.getElementById(objComboFormaPago.id).selectedIndex  = indexComboFomaPago;
                   }
              }else{
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
              }
          }
      }  
  }
    
    var indexComboFomaPago;
    function fxSaveIndexComboFormaPago(obj){       
       indexComboFomaPago = obj.selectedIndex;
       return indexComboFomaPago;
    }        
    
    
   //[Despacho en Tienda] RMARTINEZ
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
      //alert(form.hdnChkCourier.value);
   }
   
   function fxShowFlagCourier(lugarDespacho){
      form  = document.frmdatos;
      if (lugarDespacho == '<%=Constante.DISPATCH_PLACE_ID_FULLFILLMENT%>'){
         form.chkCourier.checked = false;
         form.chkCourier.disabled = true;
         form.hdnChkCourier.value = "0";
      }else{
         form.chkCourier.disabled = false;
      }
   }
    
   //<!--MSOTO NO_000022816 02/06/2014-->
   function fxSearchCmbVendedor(typelist){
      v_form = document.frmdatos;
      v_txtCmbVendedor = v_form.txtCmbVendedor.value;
      url = "/portal/pls/portal/!websales.np_contact_attention_pl_pkg.PL_VENDOR_SEARCH?" + "av_typelist=" + typelist + "&av_searchnom=" + escape(v_txtCmbVendedor);
      url = "/portal/pls/portal/websales.npsl_general_pl_pkg.window_frame?av_title=" + escape("Busqueda de compa?ia") + "&av_url=" + escape(url);
      WinAsist = window.open(url,"WinAsist","toolbar=no,location=0,directories=no,status=yes,menubar=0,scrollbars=no,resizable=no,screenX=100,top=80,left=100,screenY=80,width=700,height=500,modal=yes");
   }
       
  </script>
  <!-- Fin Data -->

  <br>
  <table border="0" cellspacing="0" cellpadding="0" width="15%">
     <tr class="PortletHeaderColor"> 
        <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"></td>
        <td class="SubSectionTitle" align="left" valign="top">Orden&nbsp;<%=strOrderId%></td>
        <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"></td>
     </tr>
  </table>
  
  <table  border="0" width="100%" cellpadding="2" cellspacing="1" class="RegionBorder">
     
     <tr >
        <td class="RegionHeaderColor" width="100%">   
           
           <table border="0" cellpadding="0" cellspacing="1" width="100%" >
              <!-- Primer grupo -->
              <tr>
                  <td class="CellLabel" align="center">&nbsp;<font color="red"><b>*</b></font>&nbsp;<b><a href="javascript:fCopiaOdenId();">NRO. SOLICITUD</a></b></td>
                  <td class="CellLabel" align="center">Divisi&oacute;n</td>
                  <td class="CellLabel" align="center">Categoría</td>
                  <td class="CellLabel" colspan=2 align="center">Sub Categoría</td>
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
                  <td class="CellContent">&nbsp;<input type="text" name="txtNumSolicitud" size="15" maxlength="8" value="">
                                                
                  </td>
                  <td class="CellContent">&nbsp;
                                        
						  <select name="cmbDivision" onblur="javascript:fx_fillCategory(this.value);">
                       <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
                    </select>
                      
                    <input type="hidden" name="hdnNumeroOrder" value="<%=strOrderId%>" >
                    <input type="hidden" name="hdnFlagLoadSolucion" value="0">                       
                    <input type="hidden" name="hdnSolucionIdAnt" value="">
                    <input type="hidden" name="hdnCurrency" value="">
                    
                  </td>
                  <td class="CellContent">&nbsp;
                    <input type="hidden" name="hdncmbCategoria" value="">                    
                    <select name="cmbCategoria" onblur="javascript:fx_fillSubCategory(this.value);" >
                       <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
                    </select>

                    <input type="hidden" name="hdnFlagLoadCategoria" value="0">                       
                    <input type="hidden" name="hdnCategoriaIdAnt" value="">    
                    
                  </td>
                  <td class="CellContent" colspan=2>&nbsp;
                    <input type="hidden" name="hdncmbSubCategoria" value="">
                    <select name="cmbSubCategoria" onblur="javascript: fxValidateTypeOpe(this);" >
                        <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
                    </select>
                    
                    <input type="hidden" name="hdnSubCategoriaIdAnt" value="">
                    <input type="hidden" name="hdnSubCategoria" value="">
                    <input type="hidden" name="hdnProductType"> 
                    <input type="hidden" name="hdnSpecification" value="">
                    <input type="hidden" name="hdnSaveOption" value="">
                    
                  </td>
                  <td class="CellContent" colspan=2>&nbsp;
                    <input type="hidden" name="hdncmbVendedor" value="">                    
                    <input type="hidden" name="hdnVendedorId" value="<%=strSalesId%>">
                    <input type="hidden" name="hdnOrderCreator" value="">
                    
                    <select name="cmbVendedor" style="display:none" onchange="fxChangeSalesMan(this,<%=iUserId%>,<%=iAppId%>);"> 
                          <!-- MSOTO 03-06-2014 SAR N_O000022816 txtCmbVendedor reemplaza al cmbVendedor -->
                          <%
                          String strMessage0 = null;
                          String strSalesName = null;   
                          if ( "".equals(strSalesId) ){
                            strSalesName="";
                          }else{ 
                            HashMap hshData = new HashMap();
                            GeneralService objGenServ = new GeneralService();
                            hshData = objGenServ.getSalesmanName(Long.parseLong(strSalesId));
                            strMessage0=(String)hshData.get("strMessage");
                            if (strMessage0!=null)
                              throw new Exception(strMessage0); 
                            strSalesName=(String)hshData.get("strName");}
                          %>                    
                    </select>     
                    
                    <% if ( strGeneratortype == "OPP" || strGeneratortype == "OPP_PORTAB" ){ %>  
                         <!-- MSOTO 03-06-2014 SAR N_O000022816 txtCmbVendedor reemplaza al cmbVendedor -->
                         <input type="text" name="txtCmbVendedor" style="width: 80%;" value="<%=strSalesName%>" disabled="disabled" />
                         <script>  
                              $(document).ready(function(){
                              $("#lblVendor").html("");
                              //$("#lblVendor").text("Vendedor");
                              $("#lblVendor").html("<label>Vendedor</label>");
                              });                                  
                         </script>  
                    <%}else{%>
                        <!--- MSOTO 03-06-2014 SAR N_O000022816 txtCmbVendedor reemplaza al cmbVendedor-->
                        <input type="text" name="txtCmbVendedor" style="width: 80%;" value="<%=strSalesName%>" disabled="disabled" />   
                        <script>                        
                              $(document).ready(function(){
                              $("#lblVendor").html("");
                              //$("#lblVendor").text("Vendedor");
                              //$("#lnkVendor").attr("href",'href="javascript:fxSearchCmbVendedor(0)"');
                              $("#lblVendor").html("<a href='javascript:fxSearchCmbVendedor(0)'>Vendedor</a>");
                              });    
                        </script> 
                    <%}%>

                    <%--CGC COR0407--%>
                    <script>                              
                        var v_salemanid="<%=strSalesId%>";                        
                        if (v_salemanid != "")
                          AddNewOption(document.frmdatos.cmbVendedor,"<%=strSalesId%>","<%=strSalesName%>");                                                    
                          document.frmdatos.cmbVendedor.value="<%=strSalesId%>";
                    </script>
                    
                    <input type="hidden" name="hdnAlcanceExcluCC" value="wv_AlcanceExcluCC">
                    <input type="hidden" name="hdnAlcanceExcluAC" value="wv_AlcanceExcluAC">
                    <input type="hidden" name="hdnCustAlcanceExclusividad" value="">
                    <input type="hidden" name="hdnVendedor" value="">
                                                
                  </td>
                  <!-- Inicio Data -->                  
                  <input type="hidden" name="hdncmbVendedorData" value="">                    
                  <input type="hidden" name="hdnVendedorDataId" value="">
                  <input type="hidden" name="hdnDataFieldsVisibles" value="">
                  <input type="hidden" name="hdncmbVendedorVoz" value="">   
                  <input type="hidden" name="hdnLoadSellerData" value=""> 
                  <%=strDataSalesmanField%>                  
                  <!-- Fin Data -->
              </tr>
            <!-- Grupo Auxiliar -->
 
              <tr>
                  <td class="CellLabel" align="center">&nbsp;</td>
                  <td class="CellLabel">
                    &nbsp;<input type="hidden" name="txtEstadoOrden" size="15" maxlength="20" value="Pendiente" readonly>
                    <span id="txtLblPaymentResp" style="display:none">Resp. de Pago</span>
                  </td>
                  <td class="CellLabel" align="center" colspan="3" >
                      <input type="text" name="txtPaymentRespDesc" size="60" value="" readonly style="display:none">
                      <input type="hidden" name="hdnPaymentRespId">
                    </td>
                  
                  <td class="CellLabel" align="center">Dealer</td>
                  <td class="CellContent">
                    &nbsp;<input type="text" name="txtDealer" size="12" maxlength="20" value="" readOnly>
                    <input type="hidden" name="hdnDealer" size="12" maxlength="20" value="">
                  </td>
                  <!-- Inicio Data -->
                  <td class="CellContent">&nbsp;</td> 
                  <!-- Fin Data -->
              </tr>
            <!-- Segundo grupo -->

              <tr>
                  <td class="CellLabel" align="center">&nbsp;<font color="red"><b>*</b></font>&nbsp;Lugar Despacho</td>

                  <td class="CellLabel" align="left">
                     <div id="dvCourier" style=display:'none'>&nbsp;&nbsp;Entrega por Courier </div>
                  </td>
                  
                  <td class="CellLabel" align="center">Region Tramite</td>
                  <td class="CellLabel" align="center">Tienda</td>
                  <td class="CellLabel" align="center">Firma fecha Hora</td>
                  <td class="CellLabel" align="center">Estado&nbsp;Proc.&nbsp;Aut.</td>
                  <td class="CellLabel" align="center">
                    <div id="dvFechaProcAutN" style=display:''><a href="javascript:show_calendar('frmdatos.txtFechaProceso',null,null,'DD/MM/YYYY');" name="linkCalendar1">Fecha&nbsp;Proc.&nbsp;Aut.</a></div><!--dlazo-->
                    <div id="dvFechaProcAutP" style=display:'none'><a name="linkCalendar1">Fecha&nbsp;Proc.&nbsp;Aut.</a></div> <!--dlazo-->
                  </td>
                  <td class="CellLabel" align="center"> <div id="dvFechaFinProg" style=display:'none'><a href="javascript:show_calendar('frmdatos.txtFechaFinProg',null,null,'DD/MM/YYYY');" name="linkCalendar">Fecha&nbsp;Fin&nbsp;Prog.</a></div></td> <!--mlopezl - modif hpptt # 1 - 30/06/2010-->
                  <td class="CellLabel" align="center"> <div id="dvFechaReconexion" style=display:'none'><a href="javascript:show_calendar('frmdatos.txtFechaReconexion',null,null,'DD/MM/YYYY');" name="linkCalendar">Fecha&nbsp;Proc.&nbsp;Reconex.</a></div></td>
                  <!-- Inicio Data >
                  <td class="CellLabel">&nbsp;</td> 
                  <Fin Data -->
                  
              </tr>
              <tr>
                  <td class="CellContent">&nbsp;
                      <select name="cmbLugarAtencion" onchange="javascript:fxShowFlagCourier(this.value);"> 
                      <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
                    </select> 
                    <input type="hidden" name="hdnWorkflowType" size="10" value="">
                    <input type="hidden" name="txtPedidoId" value="wn_PedidoId">
                    <input type="hidden" name="hdnLugarDespachoType" value="">
                    <input type="hidden" name="hdnLugarDespacho" value="">
                  </td>
                  <%
                  String strTienda = "";
                  String strRegionTramite = "";
                  int intRegionTramiteCodigo = 0;
                  //HashMap hashBuildingName = new HashMap();
                  //pe.com.nextel.test.ErrorMessage  objErrorMessage = new pe.com.nextel.test.ErrorMessage(strMessage5);
                  HashMap hashBuildingName = new HashMap();
                  
                  //hashBuildingName = NewOrderService.OrderDAOgetBuildingName( 21,"DTEODOSIO");   
                                    
                  hashBuildingName = NewOrderService.OrderDAOgetBuildingName(portalSessionBean3.getBuildingid(),portalSessionBean3.getLogin());
                     
                  hashBuildingName = (HashMap)hashBuildingName.get("hshData");
                  System.out.println("hashBuildingName"+hashBuildingName);
                  if( hashBuildingName!=null){
                     if (hashBuildingName.size() != 0 ) {       
                        strTienda               = MiUtil.getString((String)hashBuildingName.get("wv_name"));
                        strRegionTramite        = MiUtil.getString((String)hashBuildingName.get("wv_regionname"));                        
                        //strRegionId = "122";
                        if ( strRegionId.equals("") || strRegionId == null)
                           intRegionTramiteCodigo  = MiUtil.parseInt((String)hashBuildingName.get("wn_regionid"));  
                        else
                           intRegionTramiteCodigo = MiUtil.parseInt(strRegionId);
                        System.out.println("[intRegionTramiteCodigo]"+intRegionTramiteCodigo);
                     }
                  }
                  
                  //JLIMAYMANTA
                   String strTypeService="";
                   HashMap hashBuildingTS = new HashMap();
                   hashBuildingTS = NewOrderService.OrderDAOgetBuildingTS(portalSessionBean3.getBuildingid(),portalSessionBean3.getLogin());
                   hashBuildingTS = (HashMap)hashBuildingTS.get("hshDataTE");
                   System.out.println("hashBuildingTS"+hashBuildingTS);
                   if( hashBuildingTS!=null){
                     if (hashBuildingTS.size() != 0 ) {       
                        strTypeService               = MiUtil.getString((String)hashBuildingTS.get("wv_typeservice"));                      
                        System.out.println("[strTypeService]"+strTypeService);
                     }
                      if(strTypeService==null){
                          strTypeService="";
                     }
                  }
                  
                  %>

                  <td class="CellContent"> <div id="dvChkCourier" style=display:'none'> &nbsp;<input type="checkbox" name="chkCourier" value ="0" onclick="fxSetValueCourier(this)"></div></td>
                  <input type="hidden" name="hdnChkCourier" value="">
                  <td class="CellContent">&nbsp;<input type="text" name="txtRegionTramite" size="18" maxlength="20" value="<%=strRegionTramite%>" readonly></td>
                  <input type="hidden" value="<%=intRegionTramiteCodigo%>" name="hdnRegionId" >
                  <input type="hidden" value="<%=strTypeService%>" name="hdnTypeService" >
                  <input type="hidden" value="<%=portalSessionBean3.getBuildingid()%>" name="hdnTienda" >
                  
                  <td class="CellContent">&nbsp;<input type="text" name="txtTienda" size="18" maxlength="20" value="<%=strTienda%>" readonly></td>
                  <%
                    java.util.Date f = new java.util.Date();
                    java.util.Date dFechaActual = new java.util.Date(f.getTime());
                    
                    SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    
                  %>
                  <td class="CellContent">&nbsp;<input type="text" name="txtFirmaFechaHora" size="20" maxlength="20" value="<%=formateador.format(dFechaActual)%>" readonly></td>
                  <td class="CellContent">&nbsp;<input type="text" name="txtEstadoProcAutom" value="Pendiente" size="10" maxlength="30"></td>
                  <td class="CellContent" align="center">&nbsp;<input type="text" name="txtFechaProceso" value="" size="10" maxlength="10" onchange="CheckDate(this);" onBlur="if(form.cmbSubCategoria.value == <%=Constante.SPEC_SUSPENSIONES[0]%>){calcularFechaReconex(); fxValidaDiasSuspension();}"></td>
                  <input type="hidden" value="" name="hdnFechaProceso" >
                  <!--td class="CellContent"><a href="javascript:show_calendar('frmdatos.txtFechaProceso',null,null,'DD/MM/YYYY');"></a></td-->
                  <!-- mlopezl - modif hpptt # 1 - 30/06/2010 - inicio-->
                  <td class="CellContent" align="center"><div id="dvFechaFinProgInput" style=display:'none'> &nbsp;<input type="text" name="txtFechaFinProg" value="" size="10" maxlength="10" onBlur="fxValidateFechaFin(this);"></div></td>
                  <input type="hidden" value="" name="hdnFechaFinProg" > 
                  <!-- mlopezl - modif hpptt # 1 - 30/06/2010 - fin-->
                  
                  <td class="CellContent" align="center"><div id="dvFechaReconexionInput" style=display:'none'> &nbsp;<input type="text" name="txtFechaReconexion" value="" size="10" maxlength="10" onBlur="calcularFechaReconex();fxValidateFecha(this); if(form.cmbSubCategoria.value == <%=Constante.SPEC_SUSPENSIONES[0]%>){ fxValidaDiasSuspension();}"></div></td>
                  <input type="hidden" value="" name="hdnFechaReconexion" > 
                  <!-- Fin Data -->
              </tr>
              
             <!-- Tercer grupo -->
              <tr>
                  <td class="CellLabel" align="center">&nbsp;<font color="red"><b>*</b></font>&nbsp;Forma de Pago</td>
                  <td class="CellLabel" align="center">Estado de Pago</td>
                  <td class="CellLabel" align="center"><a href="javascript:show_calendar('frmdatos.txtFechaProbablePago',null,null,'DD/MM/YYYY');">&nbsp;Fecha Pago&nbsp;</a></td>
                  <td class="CellLabel" align="center">Propuesta</td> 
                  <td class="CellLabel" align="center"><%=MiUtil.getString(strParentName)%></td>                  
                  <td class="CellLabel" colspan=4 align="center">&nbsp;</td>
              </tr>
              <tr>
                  <td class="CellContent">&nbsp;
                      <select id="cmbFormaPago" name="cmbFormaPago" onclick="fxSaveIndexComboFormaPago(this);" onchange="fxValidateFormaPagoVEP(this);"> <!--onchange="javascript:fValidarFormaPago(this);"-->
                      <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
                      </select>
                        <input type="hidden" name="hdnFormaPagoOrig" value="">
                  </td>
                  <td class="CellContent">&nbsp;<input type="text" name="txtEstadoPago" size="15" maxlength="20" value="Pendiente" readOnly></td>
                  <td class="CellContent">&nbsp;<input type="text" name="txtFechaProbablePago" value="" size="18" maxlength="10" onBlur="CheckDate(this)">&nbsp;</td>                  
                  <td class="CellContent">
                    <div style="width:100%;">
                      <div style="float:left;width:30%;text-align:center;"><input type="text" name="txtPropuesta" readonly value="" size="18" maxlength="10" onBlur=""></div>                    
                      <div style="float:right;width:30%;text-align:center;"><a href="javascript:openPopUpProposedList()"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/viewDetail.gif" alt="Propuestas" width="15" height="15" border="0"></a></div>
                    </div>
                  </td>
                  <%
                  if (  !MiUtil.getString(strGeneratorid).equals("") && !MiUtil.getString(strParentUrl).equals("") ){
                    %><td class="CellContent" align="center"><a href="javascript:fxShowParent('<%=strGeneratorid%>','<%=strParentUrl%>')"><%=strGeneratorid%></a></td><%
                  }else{
                    %><td class="CellContent">&nbsp;</td><%
                  }
                  %> 
                  <td class="CellContent" colspan=4>&nbsp;</td>                  
              </tr>
                   
            <!-- Cuarto grupo -->
     
             <tr >
                 <td class="CellLabel" align="center">&nbsp;Descripci&oacute;n&nbsp;</td>
                 <td class="CellLabel" colspan="7">&nbsp;</td>
              </tr> 
             <tr>
                 <td class="CellContent" colspan="8">&nbsp;<textarea name="txtDetalle" cols="145" rows="7"></textarea></td>
              </tr>


            <!--JPEREZ - INICIO -->
               <!-- Campos para excepciones  -->
               <input type="hidden" name="hdnExceptionInstallation" value="">               
               <input type="hidden" name="hdnExceptionPrice" value="">
               <input type="hidden" name="hdnExceptionPlan" value="">
               <input type="hidden" name="hdnExceptionWarrant" value="">
               <input type="hidden" name="hdnExceptionRevenue" value="">
               <input type="hidden" name="hdnExceptionRevenueAmount" value="">
               <input type="hidden" name="hdnExceptionBillCycle" value="">
               <input type="hidden" name="hdnExceptionTotal" value="">
                 <!-- Orden Modificada  -->
               <input type="hidden" name="hdnChangedOrder" value="N"/>
           <!--JPEREZ - FIN -->
               <input type="hidden" name="hdnSalesStructOrigenId" value="<%=iSalesStructOrigenId%>"/>
           </table>                   

        <!-- Cerrar -->
   </table>
   
   <!-- Inicio Data -->
   <script defer>
      fxHideDataFields();
   </script>
   <!-- Fin Data -->
   
  <!--  Hidden que indica el # de grupos de item_pedido ingresados -->
  <!-- DATOS DEL PEDIDO - FIN -->
<%}catch(SessionException se) {
    System.out.println("[JP_ORDER_NEW_ORDER][SessionException] : " + se.getClass() + " - " + se.getMessage());
    se.printStackTrace();
    //out.println("<script>alert('Su sesión ha finalizado. Ingrese nuevamente');</script>");
    //String strUrl = "/portal/pls/portal/WEBSALES.NP_USER_AVAILABILITY_PL_PKG.PL_LOGOUT";
     //out.print("<script>parent.mainFrame.location.replace('"+strUrl+"');</script>");
  }catch(Exception e) {
    String strMessageExceptionGeneralStart = "";
    strMessageExceptionGeneralStart = MiUtil.getMessageClean(e.getMessage());
    System.out.println("[JP_ORDER_NEW_ORDER][Exception] : " + e.getClass() + " - " + e.getMessage());
    out.println("<script>alert('"+strMessageExceptionGeneralStart+"');</script>");   
  }
%>
