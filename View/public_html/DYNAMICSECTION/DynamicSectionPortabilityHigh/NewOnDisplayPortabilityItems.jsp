<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.portability.service.PortabilityOrderService"%>
<%@ page import="pe.com.portability.bean.PortabilityOrderBean"%>
<%@ page import="pe.com.nextel.bean.DominioBean"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="pe.com.nextel.service.SessionService"%>
<%@ page import="pe.com.nextel.bean.PortalSessionBean"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.text.DecimalFormat"%>

<%
  try{
  
    System.out.println("****************************************************************");
    System.out.println("NewOnDisplayPortabilityItems.jsp");
    System.out.println("****************************************************************");
    Hashtable hshParam=(Hashtable)request.getAttribute("hshtInputEditSection");  
    if (hshParam==null) hshParam=new Hashtable();
    long lCustomerId=MiUtil.parseLong((String)hshParam.get("strCustomerId"));
    long lSiteId=MiUtil.parseLong((String)hshParam.get("strSiteId"));       
    long lOrderId=MiUtil.parseLong((String)hshParam.get("strOrderId"));         
    String strSessionId=MiUtil.getString((String) hshParam.get("strSessionId"));
    long lSpecificationId=MiUtil.parseLong((String) hshParam.get("strSpecificationId"));
         
    PortalSessionBean objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
    String strLogin=objPortalSesBean.getLogin();
    int strAppId=objPortalSesBean.getAppId(); //PM0010311 - OTrillo 24/09/2015
    
    String strMessage=null;
    HashMap hshItemPortab = new HashMap();
    HashMap hshModalidadCont = new HashMap();
    HashMap hshPortabItem = new HashMap();
    ArrayList arrItemPortabList = null;
    ArrayList arrmodalityContList = null;
    ArrayList arrPortabItemList = null;
    ArrayList arrSubsanacionList = null;

    PortabilityOrderService objPOSItemPort = new PortabilityOrderService();
    PortabilityOrderService objPOSPortabItem = new PortabilityOrderService();
    PortabilityOrderService objPOSModality = new PortabilityOrderService();
    PortabilityOrderService objPSInbox = new PortabilityOrderService();
    PortabilityOrderService objPOSValidation = new PortabilityOrderService();
    PortabilityOrderBean objPOBean = null;
    PortabilityOrderBean objPOPBean = null;
    PortabilityOrderBean objPODevBean = null;
    DominioBean objDominioBean = null;
    
    String npPosition = "";
    String npPortabOrderId = "";
    String npItemId = "";
    String npItemDeviceId = "";
    String npAplicationId = "";
    String npPhoneNumber = "";
    String npModalityCont = "";
    String npShippingDate = "";
    String npLastState = "";
    String npErrorInteg = "";
    String npReasonReject = "";
    String npScheduleDead = "";
    String npExecDeadLine = "";
    String npExecutionDate = "";
    String npCorrected = "";
    String npState = "";
    String npOrderParent = "";
    String npContract = "";
    String npModalitySell = "";
    String npProductLineId = "";
    String npScheduleDBscs = "";        
    String npPhoneNumberWN = "";
    String npPortabItemId = "";
    String npAmountDue = "";
    String npCurrencyType = "";
    String npCurrencyDesc = "";
    String npAssignorId = "";
    String npUfmi = "";//Por Reserva de Numeros - 02/11/2010 - FPICOY
    long npSolutionId = 0;
    int npcant = 0;
    int resultSP = objPOSPortabItem.getFlagSendSP(lOrderId);
    int resultPP = objPOSPortabItem.getFlagSendPP(lOrderId);
    String resultVal = objPOSValidation.getExpValidation(lOrderId);
    String division = "";
    String expresionRegular = "";
    String lengthNumberPhone = "";
    String messageAlertPort = "";
    if(resultVal!=null){
       String[] rs = resultVal.split("#");
       division = rs[0];
       expresionRegular = rs[1];
       lengthNumberPhone = rs[2];
       messageAlertPort = rs[3];
    }
%>

<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/date-picker.js"></script>
     <%
        HashMap hshStatus = (HashMap)objPOSItemPort.getStatusOrder(lOrderId);
        String npStatus = ((String)hshStatus.get("strStatus")).trim();
        
        //Ini TMOGROVEJO 23/11/2009
        int intValidationInboxUser = objPOSPortabItem.getValInboxEditableUser(npStatus,strLogin);                                       
        //Fin TMOGROVEJO 23/11/2009
        
        //if(!npStatus.equals("PORTABILIDAD_NUMERICA") && !npStatus.equals("BACKLOG_PORTABILIDAD")){
        if(npStatus.equals("TIENDA01") || npStatus.equals("ADM_VENTAS") || npStatus.equals("VENTAS")){
          //Obtengo los Items de la np_Item
          hshItemPortab = (HashMap)objPOSItemPort.getItemsPortabList(lOrderId); //1
          arrItemPortabList = new ArrayList();
          arrItemPortabList = (ArrayList)hshItemPortab.get("objItemList");
        }else{
          //Obtengo los Items de la np_portability_item
          hshItemPortab = (HashMap)objPOSItemPort.getItemsPortLst(lOrderId);
          arrItemPortabList = new ArrayList();
          arrItemPortabList = (ArrayList)hshItemPortab.get("objItemList");
        }
        
        objPOPBean = new PortabilityOrderBean();
        if(arrItemPortabList.size()>0){
          objPOPBean = (PortabilityOrderBean)arrItemPortabList.get(0);
          npOrderParent = (objPOPBean.getNpOrderParentId()!=null?objPOPBean.getNpOrderParentId():"");
        }
        
        HashMap hshInboxDoc = objPSInbox.getInboxOrder(lOrderId);
        String strInbox = (String)hshInboxDoc.get("strInboxStatus");
        System.out.println(strInbox);
    %>

<script type="text/javascript" defer>

    function fxDeleteItemPortability(){
      var row, cell, rownum=0;
      var table = document.all ? document.all["tableItemsPortab"]:document.getElementById("tableItemsPortab");
      rownum = tableItemsPortab.rows.length;
      if(rownum>1){
      	row = tableItemsPortab.deleteRow(rownum -1);
      }
    }
    
    function fxVerificarPhoneNumber(phoneNumber,ubicacion){
      var vForm = document.frmdatos;
      var cant = 0;
      var index = parseInt(ubicacion)-1;
      var numFilas = eval(tableItemsPortab.rows.length);
      //alert(index);
      if (numFilas > 2 && phoneNumber != ''){
        for(var i=0; i<numFilas-1; i++){
          if(vForm.txtphonenumber[i].value == phoneNumber){
             cant = cant + 1;
          }
        }
        if(cant == 2){
          alert("El número telefónico ya fue ingresado");
          vForm.txtphonenumber[index].focus();
          vForm.txtphonenumber[index].style.backgroundColor = "#F3F781";
          return false;
        }
        vForm.txtphonenumber[index].style.backgroundColor = "#FFFFFF";
      }
      return true;
    }

    function fxSubsanarDeuda(phoneNumber, applicationId, portabItemId, amountDue, currencyType, currencyDesc, npAssignorId){
        var url = "<%=request.getContextPath()%>/DYNAMICSECTION/DynamicSectionPortabilityHigh/SubSanacionPortability.jsp?npPhoneNumber=" + phoneNumber +
                "&npAplicationId=" + applicationId + "&npPortabItemId=" + portabItemId + "&npAmountDue=" + amountDue + "&npCurrencyType=" + currencyType +
                "&npCurrencyDesc=" + currencyDesc + "&npAssignorId=" + npAssignorId;
        window.open(url,"WinListPagos","toolbar=no,location=0,directories=no,status=yes,menubar=0,scrollbars=no,resizable=yes,screenX=100,top=80,left=150,screenY=80,width=700,height=500,modal=yes");
    };

    function fxSectionPortabilityHighValidate(){
      var vForm = document.frmdatos; 
      if(tableItemsPortab != undefined){
      var numFilas = eval(tableItemsPortab.rows.length);

      if (numFilas == 2){     
         if(vForm.txtphonenumber.value != ""){
            var longitud = vForm.txtphonenumber.value;
            
            if( vForm.cmbmodalidadcont.value == ""){
               alert("Debe ingresar modalidad de contrato ");
               vForm.cmbmodalidadcont.focus();
               return false;  
            }            
              if((vForm.cmbmodalidadcont.value=='01' || vForm.cmbmodalidadcont.value=='02') &&
                      (longitud.length != 9 && longitud.length != 8)){
                  alert("El número telefónico debe tener 9 dígitos en caso de telefonía móvil o 8 dígitos en " +
                  "caso de telefonía fija móvil");
                  vForm.txtphonenumber.focus();
                  return false;
              }/*else if((vForm.cmbmodalidadcont.value=='03' || vForm.cmbmodalidadcont.value=='04') &&
               longitud.length != 7) {
               alert("El número telefónico debe tener 7 dígitos");
               vForm.txtphonenumber.focus();
               return false;
               }*/
          }else{
            if( vForm.cmbmodalidadcont.value != ""){
               alert("Debe ingresar el teléfono ");
               vForm.cmbmodalidadcont.focus();
               return false;  
            }
          }
      }else if(numFilas > 2){            
         for(var i=0; i<numFilas-1; i++){      
            if(vForm.txtphonenumber[i].value != ""){             
               var longitud = vForm.txtphonenumber[i].value;                              
                if(longitud.length != 9 && longitud.length != 8){
                    alert("El número telefónico debe tener 9 dígitos en caso de telefonía móvil o 8 dígitos en " +
                    "caso de telefonía fija móvil");
                  vForm.txtphonenumber[i].focus();
                  return false;               
               }               
               if( vForm.cmbmodalidadcont[i].value == ""){
                  alert("Debe ingresar la modalidad de contrato ");
                  vForm.cmbmodalidadcont[i].focus();
                  return false;
               }                                 
            }else{
              if( vForm.cmbmodalidadcont[i].value != ""){
                 alert("Debe ingresar el teléfono ");
                 vForm.cmbmodalidadcont[i].focus();
                 return false;  
              }
            }
         }
                         
      }else{
        alert("No existen números para ser portados");
        return false;
      }
      }
      return true;
    }
    
     loadSection();       
     
     function loadSection(){
       var vForm = document.frmdatos;        
       vForm.hdntblListaPortabilityHigh.value = tableItemsPortab.rows.length; 
       var numFilas = eval(tableItemsPortab.rows.length);
       var npestado = vForm.txtEstadoOrden.value;
       if(npestado != 'TIENDA01' && npestado != 'ADM_VENTAS' && npestado != 'VENTAS'){
        if (numFilas == 2){
          vForm.txtphonenumber.readOnly = true;
          if(vForm.hdnOrdenParentId.value == ''){
            dvByTxtModalityCont.style.display="";
            dvByCmbModalityCont.style.display="none";
          }else{
            dvByTxtModalityCont.style.display="none";
            dvByCmbModalityCont.style.display="";
          }
        }else if(numFilas > 2){
          for(var i=0; i<numFilas-1; i++){
             vForm.txtphonenumber[i].readOnly = true;
             if(vForm.hdnOrdenParentId.value == ''){
              dvByTxtModalityCont[i].style.display="";
              dvByCmbModalityCont[i].style.display="none";
             }else{
              dvByTxtModalityCont[i].style.display="none";
              dvByCmbModalityCont[i].style.display="";
             }
          }
        }
       }else{
        if (numFilas == 2){
          dvByTxtModalityCont.style.display="none";
          dvByCmbModalityCont.style.display="";
        }else if(numFilas > 2){
          for(var i=0; i<numFilas-1; i++){
             dvByTxtModalityCont[i].style.display="none";
             dvByCmbModalityCont[i].style.display="";
          }
        }
       }
     }
    
    function esInteger(e) {
      var charCode;
      charCode = e.keyCode;
      status = charCode;
      if (charCode > 31 && (charCode < 48 || charCode > 57)) {
      return false;
      }
      return true;
    }
    function fxSendMessage(messageType){
      form = document.frmdatos;
      var url = "<%=request.getContextPath()%>/portabilityOrderServlet?strOrderId=<%=lOrderId%>&strLogin=<%=strLogin%>&strCustomerId=<%=lCustomerId%>&messageType="+messageType+"&strPortabilityType=ALTA&hdnMethod=sendPortabilityMessages";
      parent.bottomFrame.location.replace(url);
    } 
    
    fxShowButtomWS();
   
   function fxShowButtomWS(){
      form  = document.frmdatos;
      var StrInbox = form.txtEstadoOrden.value;
      var flgSendSP = "<%=resultSP%>";
      var flgSendPP = "<%=resultPP%>";
      if (StrInbox == "<%=Constante.INBOX_PORTABILIDAD_NUMERICA%>" && flgSendSP != "1" ){
         dvBySpecButtomSP.style.display="inline";

      }else{         
         dvBySpecButtomSP.style.display="none";
         dvByPortabStatus.style.display="none";
      }

      if (StrInbox == "<%=Constante.INBOX_PORTABILIDAD_NUMERICA%>" && flgSendPP != "1" ){
           dvBySpecButtomPP.style.display="inline";
        }else{
           dvBySpecButtomPP.style.display="none";
           dvByPortabStatus.style.display="none";
        }

   }
   
   function fxSavePortabilityStatus(){
      var vForm = document.frmdatos; 
      var numFilas = eval(tableItemsPortab.rows.length);
      
      if (numFilas == 2){   
        var strItemId = vForm.hddItemId.value;
        var strItemDeviceId = vForm.hdnItemDeviceId.value;
        var strModalidadCont = vForm.cmbmodalidadcont.value;
        var strStatusPortab = vForm.cmbCorrected.value;              
        var numberFilas = 1;
        var indice = 1;                
        var cadena = strItemDeviceId+"-"+strStatusPortab+"-"+numberFilas+"-"+indice+"-"+strModalidadCont+"-"+strItemId;        
        vForm.arrStatus.value=cadena;
        vForm.target="bottomFrame";
        vForm.action="<%=request.getContextPath()%>/portabilityOrderServlet?hdnMethod=updateStatusPortabilityItem&numFilas="+numberFilas;
        vForm.submit();
      }else if(numFilas > 2){
        for(var i=0; i<numFilas-1; i++){
          var strItemId = vForm.hddItemId[i].value;
          var strItemDeviceId = vForm.hdnItemDeviceId[i].value;
          var strModalidadCont = vForm.cmbmodalidadcont[i].value;
          var strStatusPortab = vForm.cmbCorrected[i].value;
          var indice = i + 1;                    
          var numberFilas = numFilas - 1;                  
          var cadena = strItemDeviceId+"-"+strStatusPortab+"-"+numberFilas+"-"+indice+"-"+strModalidadCont+"-"+strItemId;          
          vForm.arrStatus[i].value=cadena;
        }
        vForm.target="bottomFrame";
        vForm.action="<%=request.getContextPath()%>/portabilityOrderServlet?hdnMethod=updateStatusPortabilityItem";
        vForm.submit();
      }
      
    }
    
    function fxValidarNumeroPortado(phone,ubicacion){
      var vForm = document.frmdatos;
      var numFilas = eval(tableItemsPortab.rows.length);
      var phoneNumberList = '';
      var index = parseInt(ubicacion)-1;
      var expresionRegular = "<%=expresionRegular%>";
      var numDigitos = "<%=lengthNumberPhone%>";
      var division = "<%=division%>";
      var message = "<%=messageAlertPort%>";

        //Ini PM0010311 - OTrillo 24/09/2015
        document.frmdatos.btnUpdOrder.disabled = true;
        document.frmdatos.btnUpdOrderInbox.disabled = true;

        var hdnFechaCreacionOrden = vForm.hdnFechaCreacionOrden.value;
        var cmbAssignor = vForm.cmbAssignor.value;
        var applicationId = "<%=strAppId%>";
        //Fin PM0010311 - OTrillo 24/09/2015

      if(phone != '') {
        if(numFilas > 2) {
            for (var i = 0; i < numFilas - 1; i++) {
                if (vForm.txtphonenumber[i].value != '') {
                    if (phoneNumberList == '') {
                        phoneNumberList = phoneNumberList + trim(vForm.txtphonenumber[i].value);
                    } else {
                        phoneNumberList = phoneNumberList + '|' + trim(vForm.txtphonenumber[i].value);
                    }
                }
            }
            phoneNumberList = trim(phoneNumberList);
        }
        if(division == 1) {//1 = Telefonia movil
            if (new RegExp(expresionRegular).test(phone)) {
                if (phone.length == numDigitos) {
                    vForm.target = "bottomFrame";
                    vForm.action = "<%=request.getContextPath()%>/portabilityOrderServlet?hdnMethod=checkNumberPorted&strOrderId=<%=lOrderId%>&phoneNumber=" + phone + "&index=" + ubicacion + "&numFilas=" + numFilas + "&fechaCreacionOrden=" + hdnFechaCreacionOrden + "&cmbAssignorId=" + cmbAssignor + "&applicationId=" + applicationId + "&phoneNumberList=" + phoneNumberList + "&lengthNumberPhone=" + numDigitos; //PM0010311 - OTrillo 24/09/2015
                    vForm.submit();
                }
            } else {
                alert(message);
                if(numFilas > 2) {
                    vForm.txtphonenumber[index].value = '';
                }else{
                    vForm.txtphonenumber.value = '';
                }
            }
        }
        if(division == 8) {//8 = Telefonia fijo movil
            if (new RegExp(expresionRegular).test(phone)) {
                if (phone.length == numDigitos) {
        			vForm.target="bottomFrame";
                    vForm.action = "<%=request.getContextPath()%>/portabilityOrderServlet?hdnMethod=checkNumberPorted&strOrderId=<%=lOrderId%>&phoneNumber=" + phone + "&index=" + ubicacion + "&numFilas=" + numFilas + "&fechaCreacionOrden=" + hdnFechaCreacionOrden + "&cmbAssignorId=" + cmbAssignor + "&applicationId=" + applicationId + "&phoneNumberList=" + phoneNumberList + "&lengthNumberPhone=" + numDigitos; //PM0010311 - OTrillo 24/09/2015
        			vForm.submit();
      			}
            } else {
                alert(message);
                if(numFilas > 2) {
                    vForm.txtphonenumber[index].value = '';
                }else{
                    vForm.txtphonenumber.value = '';
                }
            }
        }
      }
    }
  
  //Reserva de Numeros Golden - RHUACANI-ASISTP - 24/10/2010
  function fxLoadPopUpRadio(numFila,posicion){
    document.getElementById("hdnFilaActual").value=numFila;
    var form = document.frmdatos;
    var count_items     = getNumRows("items_table");
    var arrAuxPosition=posicion.split('-')
    var indexOrderItem=arrAuxPosition[0];
    var strRatePlanId=0;
    var strSolutionId=0;

    if(count_items < 2){
        if(form.hdnItemValuetxtItemRatePlan == undefined){
            if(form.hdnItemValuetxtItemNewRatePlan == undefined){
              strRatePlanId = 0;
              strSolutionId = 0;
            }else{
              strRatePlanId = form.hdnItemValuetxtItemNewRatePlan.value;
              strSolutionId = form.hdnItemValuetxtItemSolution.value;
            }
        }else{
            strRatePlanId = form.hdnItemValuetxtItemRatePlan.value;
            strSolutionId = form.hdnItemValuetxtItemSolution.value;
        }
     }else{
        if(form.hdnItemValuetxtItemRatePlan == undefined){
            if(form.hdnItemValuetxtItemNewRatePlan == undefined){
              strRatePlanId = 0;
              strSolutionId = 0;
            }else{
              strRatePlanId = form.hdnItemValuetxtItemNewRatePlan[indexOrderItem-1].value;
              strSolutionId = form.hdnItemValuetxtItemSolution[indexOrderItem-1].value;
            }
        }else{
            strRatePlanId = form.hdnItemValuetxtItemRatePlan[indexOrderItem-1].value;
            strSolutionId = form.hdnItemValuetxtItemSolution[indexOrderItem-1].value;
        }
      }
    //BEGIN PBASUALDO 20101206
    if(strRatePlanId=="")
    {
      alert("El elemento seleccionado debe tener asociado un plan tarifario");
      return;
    }//END PBASUALDO 20101206
    //Developer: PBasualdo - 12/11/2010
    var v=window.open("<%=request.getContextPath()%>/htdocs/jsp/controls/POPUP/PopUpNumberReserve.jsp?htx_patron=-1&htx_popUpType=2&htx_planid="+strRatePlanId+"&htx_solutionid="+strSolutionId,"PopUpNumberReserve","width=480,height=310,scrollbars=no,menubar=no,url=0,toolbar=no,top=100,left=200"); 
    v.focus();
  }
  //Reserva de Numeros Golden - RHUACANI-ASISTP - 24/10/2010
  function fxRowUpdate(datos){
    var vForm = document.frmdatos;    
    var idfila= document.getElementById("hdnFilaActual").value;
    var tablax=document.getElementById("tableItemsPortab");
    var tbodyx=tablax.tBodies[0];
    var filas =tbodyx.rows;
    var cont = filas.length;
    var fila =filas[idfila];
    var celda=fila.cells[3];
    celda.innerHTML=datos.text;
    if (cont>2) {
      vForm.hdnUfmi[idfila-1].value=datos.text;
      vForm.hdnUfmiId[idfila-1].value=datos.value.split(',')[1];//PBASUALDO 20110118
    } else {
      document.getElementById("hdnUfmi").value=datos.text;
      document.getElementById("hdnUfmiId").value=datos.value.split(',')[1];//PBASUALDO 20110118
    }
  }
      
</script>

  <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
    <input type="hidden" name="hdntblListaPortabilityHigh">
    <input type="hidden" name="hdnFilaActual"><!-- Reserva de Numeros Golden - RHUACANI-ASISTP - 24/10/2010 -->
    <tr align="center">
      <td>
        <table border="0" cellspacing="0" cellpadding="0" align="left">
          <tr>
            <td>
               <div id="dvBySpecButtomSP" style=display:'none'> 
                  <input type="button" name="btnSendMessagesSP" value="Envio SP" onclick="fxSendMessage('SP')"/>
               </div>
            </td>
            <td>
               <div id="dvBySpecButtomPP" style=display:'none'> 
                  <input type="button" name="btnSendMessagesPP" value="Envio PP" onclick="fxSendMessage('PP')"/>
               </div>   
            </td>
            <!--td>
               <div id="dvBySpecButtomSVP" style=display:'none'> 
                  <input type="button" name="btnSendMessagesSVP" value="Envio SVP" onclick="fxSendMessage('SVP')"/>
               </div>
            </td-->
          </tr>
        </table>
        <table border="0" cellspacing="0" cellpadding="0" align="right">
          <tr>
            <td>
              <div id="dvByPortabStatus" style=display:'none'> 
                <input type="button" name="btnSavePortabStatus" value="Grabar Subsanación" onclick="fxSavePortabilityStatus()"/>
              </div>   
            </td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td align="left">
        <table border="0" cellspacing="0" cellpadding="0" align="left">
          <tr>
            <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"></td>
            <td class="SubSectionTitle" align="left" valign="top">Portabilidad Numérica - Alta - Detalle</td>
            <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"> &nbsp;</td>
             <input type="hidden" name="hdnLogin" value="<%=strLogin%>">
             <input type="hidden" name="hdnCustomerId" value="<%=lCustomerId%>">
             <input type="hidden" name="hdnSpecificationId" value="<%=lSpecificationId%>">
             <input type="hidden" name="hdnOrdenParentId" value="<%=npOrderParent%>">
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td align="center">
        <table id="tableItemsPortab" border="0" cellspacing="1" cellpadding="2" width="100%" class="RegionBorder">
          <tr>
            <td class="CellLabel" align="center" width="5%">&nbsp;N°</td>               
            <td class="CellLabel" align="center" width="12%">&nbsp;Teléfono</td>
            <td class="CellLabel" align="center" width="12%">&nbsp;Modalidad Origen</td>
            <td class="CellLabel" align="center" width="25%" colspan="2">&nbsp;&nbsp;&nbsp;Nro.Radio&nbsp;&nbsp;</td><!-- Reserva de Numeros Golden - RHUACANI-ASISTP - 24/10/2010 -->               
            <td class="CellLabel" align="center" width="6%">&nbsp;Id. Solicitud</td>
            <td class="CellLabel" align="center" width="20%">&nbsp;Est. Portabilidad</td>               
            <td class="CellLabel" align="center" width="20%">&nbsp;Error Integridad</td>               
            <td class="CellLabel" align="center" width="20%">&nbsp;Motivo Rechazo</td>
            <!--LROSALES-P1D-->
            <td class="CellLabel" align="center" width="20%">&nbsp;Monto Adeudado</td>
            <td class="CellLabel" align="center" width="20%">&nbsp;Tipo Moneda</td>
            <td class="CellLabel" align="center" width="20%">&nbsp;Fec. Venc. Ult. Recibo</td>
            <td class="CellLabel" align="center" width="20%">&nbsp;Fecha de Ejecución</td>
            <td class="CellLabel" align="center" width="20%">&nbsp;Subsanacion Deuda</td>
            <!--LROSALES-P1D-->
            <!--
            <td class="CellLabel" align="center" width="5%">&nbsp;Fec. Lim. Prog.</td>					
            <!--<td class="CellLabel" align="center" width="5%">&nbsp;Fec. Lim. Ejec.</td>-->
            <!--<td class="CellLabel" align="center" width="5%">&nbsp;Fecha Ejecución</td>
            <td class="CellLabel" align="center" width="5%">&nbsp;Programación BSCS</td>            
            <!-- Se agrego campo de telefono en formato world number -->             
            <!--<td class="CellLabel" align="center" width="5%">Num. a Programar</td>                                                         
            <%
               if(!npOrderParent.equals("") ){
            %>
                  <td class="CellLabel" align="center" width="6%">Subsanación</td>                  
            <%
               }
            %>
            -->                          
          </tr>
          <%
            for(int i=0; i<arrItemPortabList.size(); i++){
              objPOBean = new PortabilityOrderBean();
              objPOBean = (PortabilityOrderBean)arrItemPortabList.get(i);
              
              //////////
              if(npStatus.equals("TIENDA01") || npStatus.equals("ADM_VENTAS") || npStatus.equals("VENTAS")){              
                if((!((String)objPOBean.getNpModalitySell()).equals("Propio") && !((String)objPOBean.getNpModalitySell()).equals("Alquiler en Cliente")) && objPOBean.getNpProductLineId() != 11){ //Servicio de Telecomunicaciones = 11
                  //Obtengo los Items Device de la np_Portability_Item
                  hshPortabItem = (HashMap)objPOSPortabItem.getPortabItemDevList1(objPOBean.getNpItemid()); //2
                  arrPortabItemList = new ArrayList();
                  arrPortabItemList = (ArrayList)hshPortabItem.get("objArrayList");                  
                }else{
                  //Obtengo los Items Device de la np_Portability_Item
                  hshPortabItem = (HashMap)objPOSPortabItem.getPortabItemList1(objPOBean.getNpItemid()); //2
                  arrPortabItemList = new ArrayList();
                  arrPortabItemList = (ArrayList)hshPortabItem.get("objArrayList");
                }              
              }else{                
                hshPortabItem = (HashMap)objPOSPortabItem.getPortabItemPortabList(objPOBean.getNpItemid()); //2
                arrPortabItemList = new ArrayList();
                arrPortabItemList = (ArrayList)hshPortabItem.get("objArrayList");                
              }
              ///////////
              
              if(arrPortabItemList.size() > 0){
              
                for(int j=0; j<arrPortabItemList.size(); j++){
                  objPODevBean = new PortabilityOrderBean();
                  objPODevBean = (PortabilityOrderBean)arrPortabItemList.get(j);
                  
                  npPosition = (i+1) + "-" + (j+1);
                  npPortabOrderId = String.valueOf(objPODevBean.getNpPortabOrderId());//(objPODevBean.getNpPortabOrderId()!=null?String.valueOf(objPODevBean.getNpPortabOrderId()):"");
                  npItemId = String.valueOf(objPODevBean.getNpItemid());

                  if(npStatus.equals("TIENDA01") || npStatus.equals("ADM_VENTAS") || npStatus.equals("VENTAS")){
                    if((!((String)objPOBean.getNpModalitySell()).equals("Propio") && !((String)objPOBean.getNpModalitySell()).equals("Alquiler en Cliente")) && objPOBean.getNpProductLineId() != 11){
                      npItemDeviceId = String.valueOf(objPODevBean.getNpItemDeviceId());
                    }else{
                      npItemDeviceId = "";
                    }
                    npModalitySell = (objPODevBean.getNpModalitySell()!=null?objPODevBean.getNpModalitySell():"");
                    npProductLineId = String.valueOf(objPODevBean.getNpProductLineId());
                  }else{
                    npItemDeviceId = String.valueOf(objPODevBean.getNpItemDeviceId());
                    npModalitySell = "";
                    npProductLineId = "";
                  }
                  npPhoneNumber = (objPODevBean.getNpPhoneNumber()!=null?objPODevBean.getNpPhoneNumber():"");
                  npModalityCont = (objPODevBean.getNpModalityContract()!=null?objPODevBean.getNpModalityContract():"");
                  npAplicationId = (objPODevBean.getNpApplicationId()!=null?objPODevBean.getNpApplicationId():"");
                  npShippingDate =  (objPODevBean.getNpShippingDateMessage()!=null?objPODevBean.getNpShippingDateMessage():"");
                  npLastState = (objPODevBean.getNpLastStateDesc()!=null?objPODevBean.getNpLastStateDesc():"");
                  npErrorInteg = (objPODevBean.getNpErrorIntegrityDesc()!=null?objPODevBean.getNpErrorIntegrityDesc():"");
                  npReasonReject = (objPODevBean.getNpReasonRejectionDesc()!=null?objPODevBean.getNpReasonRejectionDesc():"");
                  npScheduleDead = (objPODevBean.getNpScheduleDeadline()!=null?objPODevBean.getNpScheduleDeadline():"");
                  npExecDeadLine = (objPODevBean.getNpExecutionDeadline()!=null?objPODevBean.getNpExecutionDeadline():"");
                  npExecutionDate = (objPODevBean.getNpExecutionDate()!=null?objPODevBean.getNpExecutionDate():"");
                  npState = (objPODevBean.getNpState()!=null?objPODevBean.getNpState():"");
                  npContract = (objPODevBean.getNpContract()!=null?objPODevBean.getNpContract():"");
                  npScheduleDBscs = objPODevBean.getNpScheduleDBscs();                  
                  npPhoneNumberWN = (objPODevBean.getNpPhoneNumber()!=null?objPODevBean.getNpPhoneNumberWN():"");                  
                  int intValMsgSub = objPOSPortabItem.getValMsgSub(lOrderId,objPODevBean.getNpItemid()); /*Agregado TM 19012010*/                                    
                  npcant = npcant + 1;
                  npUfmi = (objPODevBean.getNpUfmi()!=null?objPODevBean.getNpUfmi():"");
                  npSolutionId = objPODevBean.getNpSolutionid();
                  npPortabItemId = String.valueOf(objPODevBean.getNpPortabItemId());
                  npAmountDue = String.valueOf(objPODevBean.getNpAmountDue());
                  npCurrencyType = String.valueOf(objPODevBean.getNpCurrencyType());
                  npCurrencyDesc = String.valueOf(objPODevBean.getNpCurrencyDesc());
                  npAssignorId = String.valueOf(objPODevBean.getNpAssignor());
          %>
          <tr>
            <!--Campo Posición-->
            <td class="CellContent" align="center">
              <input type="hidden" name="arrStatus">
              <input type="hidden" name="hdnNumeracion" value="<%=npcant%>">
              <input type="hidden" name="hddPortabOrderid" value="<%=npPortabOrderId%>">
              <input type="hidden" name="hddItemId" value="<%=npItemId%>">
              <input type="hidden" name="hdnItemDeviceId" value="<%=npItemDeviceId%>">
              <input type="hidden" name="hdnModalitySell" value="<%=npModalitySell%>">
              <input type="hidden" name="hdnProductLineId" value="<%=npProductLineId%>">
              <input type="hidden" name="hdnUfmi" value="<%=npUfmi%>"><!-- Se agrega este hidden para recibir el numero de radio UFMI reservado - FPICOY - 28/10/2010-->
              <input type="hidden" name="hdnUfmiId" value="" > <!-- PBASUALDO 20110118 -->
              <input type="text" name="txtPosition" size="3" style="text-align:center;" value="<%=npPosition%>">
            </td>
            <!--Campo Teléfono-->
            <%
              if((npStatus.equals("TIENDA01") || npStatus.equals("ADM_VENTAS") || npStatus.equals("VENTAS")) ){
            %>
            <td class="CellContent" align="center">
              <input class="txtphonenumber" type="text" name="txtphonenumber" maxlength="<%=lengthNumberPhone%>" size="12" value="<%=npPhoneNumber%>" style="text-align:left" onkeypress="javascript:return esInteger(event);" onkeyup="javascript:fxVerificarPhoneNumber(this.value,<%=npcant%>), fxValidarNumeroPortado(this.value,<%=npcant%>);"><!--EFLORES REQ-0204 07/01/2016--> <!--AMATEO PM0010868 -->
            </td>
            <%
              }else{
            %>
            <td class="CellContent" align="center">
              <input type="text" name="txtphonenumber" maxlength="<%=lengthNumberPhone%>" size="12" value="<%=npPhoneNumber%>" style="text-align:center;border-bottom:none;border-right:none;border-top:none;border-left:none;background-color:transparent" readonly><!--AMATEO PM0010868 -->
            </td>
            <%
              }
            %>
            <!--Campo Modalidad Origen-->
              <%
              if (intValidationInboxUser==1){                 
                
                hshModalidadCont = (HashMap)objPOSModality.getDominioList("PORTABILITY_CONTRACT_MODALITY");
                arrmodalityContList = (ArrayList)hshModalidadCont.get("arrDominioList");
                strMessage = (String) hshModalidadCont.get(Constante.MESSAGE_OUTPUT);
              %>            
                <td class="CellContent" align="center">                
                  <div id="dvByCmbModalityCont" style=display:'none'>
                  	<!--JBALCAZAR PRY-1055 -->
                    <select name="cmbmodalidadcont" class="cmbmodalidadcont">
                    <%=MiUtil.buildComboSelected(arrmodalityContList, MiUtil.getString((String) npModalityCont))%>
                    </select>
                  </div>                   
                  <div id="dvByTxtModalityCont" style=display:'none'>
                    <input type="text" name="txtmodalidadcont" size="15" value="<%=npContract%>" style="text-align:center;border-bottom:none;border-right:none;border-top:none;border-left:none;background-color:transparent" readonly>
                  </div> 
                  <input type="hidden" name="hdnmodalidadcont" value="<%=npModalityCont%>">                                    
                </td>                                 
              <%
              }else{
              %>                                    
                <td class="CellContent" align="center">
                  <div id="dvByCmbModalityCont" style=display:'none'>
                     <input type="hidden" name="cmbmodalidadcont" value="<%=objPODevBean.getNpModalityContract()%>">
                        <%=(String)(objPODevBean.getNpContract()!=null?objPODevBean.getNpContract():"")%>
                  </div>                 
                  <div id="dvByTxtModalityCont" style=display:'none'>
                     <input type="text" name="txtmodalidadcont" size="15" value="<%=npContract%>" style="text-align:center;border-bottom:none;border-right:none;border-top:none;border-left:none;background-color:transparent" readonly>
                  </div> 
                </td>                                
              <%
              }
              %>
            <!--Campo Número Radio-->
            <%if (Constante.SOLUCION_2G_PRE==npSolutionId || Constante.SOLUCION_2G_POST==npSolutionId || Constante.SOLUTION_3G_HPPTT_PRE==npSolutionId || Constante.SOLUTION_3G_HPPTT_POST==npSolutionId) {%>
               <td class="CellContent" align="center" width="8%"><%=npUfmi%></td><!-- Reserva de Numeros Golden - RHUACANI-ASISTP - 24/10/2010 -->
               <td class="CellContent" align="center" width="19%">  
               <%if ("".equals(npUfmi)) {%>
                 <a href="javascript:fxLoadPopUpRadio(<%=npcant%>,'<%=npPosition%>');">Reservar radio</a>   
               <%} else {%>
                 &nbsp;Reservar radio&nbsp;
               <%}%>
               </td><!-- Reserva de Numeros Golden - RHUACANI-ASISTP - 24/10/2010 -->
            <%} else {%>
               <td class="CellContent" align="center" width="30%" colspan="2" color="red">No aplica para 3G non-PTT</td><!-- Reserva de Numeros Golden - RHUACANI-ASISTP - 24/10/2010 -->
            <%}%>
            <!--Campo Id. Solicitud-->
            <td class="CellContent" align="center">
              <input type="text" name="txtAplicationId" value="<%=npAplicationId%>" style="text-align:center;border-bottom:none;border-right:none;border-top:none;border-left:none;background-color:transparent" readonly>
            </td>
            <!--Campo Estado Portabilidad-->
            <td class="CellContent" align="center"><%=npLastState%></td>
            <!--Campo Error Integridad-->
            <td class="CellContent" align="center"><%=npErrorInteg%></td>
            <!--Campo Motivo Rechazo-->
            <td class="CellContent" align="center"><%=npReasonReject%></td>
            <!--Campo Monto Adeudado-->
            <td class="CellContent" align="center"><%= new DecimalFormat("#.00").format(objPODevBean.getNpAmountDue())%></td>
            <!--Campo Tipo Moneda-->
            <td class="CellContent" align="center"><%=MiUtil.getString(objPODevBean.getNpCurrencyDesc())%></td>
            <!--Campo Fec. Venc. Ult. Recibo-->
            <td class="CellContent" align="center"><%=MiUtil.getDate(objPODevBean.getNpExpirationDateReceipt(),"dd/MM/yyyy")%></td>
            <!--Campo Fecha Ejecución-->
            <td class="CellContent" align="center"><%=objPODevBean.getNpExecutionDate()%></td>
            <!--Campo Subsanacion Deuda-->
            <%
              if(npStatus.equals("PORTABILIDAD_NUMERICA") && Constante.MOTIVO_RECHAZO_DEUDA.equals(objPODevBean.getNpReasonRejection())){%>
            <td class="CellContent" align="center">&nbsp;<a href="javascript:fxSubsanarDeuda('<%=npPhoneNumber%>','<%=npAplicationId%>','<%=npPortabItemId%>','<%=npAmountDue%>','<%=npCurrencyType%>','<%=npCurrencyDesc%>','<%=npAssignorId%>');" >Subsanar Deuda</a></td>
            <%} else{%>
            <td class="CellContent" align="center"></td>
            <%}
            %>
            <!--Campos a Comentar INI - LROSALES-P1D-->
            <!--Campo Fec. Lim. Prog.-->
            <!--<td class="CellContent" align="center">
              <input type="text" name="txtScheduleDead" value="<%=npScheduleDead%>" style="text-align:center;border-bottom:none;border-right:none;border-top:none;border-left:none;background-color:transparent" readonly>
            </td>					
            <!--<td class="CellContent" align="center">
              <input type="text" name="txtExecDeadLine" value="<%=npExecDeadLine%>" style="text-align:center;border-bottom:none;border-right:none;border-top:none;border-left:none;background-color:transparent" readonly>
            </td>-->
            <!--Campo Fecha Ejecución-->
            <!--<td class="CellContent" align="center">
              <input type="text" name="txtExecutionDate" value="<%=npExecutionDate%>" style="text-align:center;border-bottom:none;border-right:none;border-top:none;border-left:none;background-color:transparent" readonly>
            </td>
            <!--Campo Programación BSCS-->
            <!--<td class="CellContent" align="center"><%=npScheduleDBscs%></td>
            <!--Campo Nro Programar-->
            <!-- se agrego campo de telefono en formato world number -->            
            <!--<td class="CellContent" align="center"><%=npPhoneNumberWN%></td>
            <!--Campo Subsanación-->
            <!--<%
               if(!npOrderParent.equals("") ){
               
               hshModalidadCont = (HashMap)objPOSModality.getDominioList("PORTAB_CORRECTED");
               arrSubsanacionList = (ArrayList)hshModalidadCont.get("arrDominioList");
               strMessage = (String) hshModalidadCont.get(Constante.MESSAGE_OUTPUT);                        
               
                if( npStatus.equals("PORTABILIDAD_NUMERICA") && (intValMsgSub == 1)  ) {
            %>
                                                          
            <td class="CellContent" align="center">
              <select name="cmbCorrected">
              <%=MiUtil.buildComboSelected(arrSubsanacionList, MiUtil.getString((String) npState))%>
              </select>
              <input type="hidden" name="hdnCorrected" value="<%=MiUtil.getString((String) npState)%>">
            </td>
            
            <%
               }else{
            %>
            
            <td class="CellContent" align="center"><!--<%=(objPODevBean.getNpStateDesc()!=null?objPODevBean.getNpStateDesc():"")%>               
               <div id="dvByCmbCorrected" style=display:"none">               
                  <select name="cmbCorrected">
                  <%=MiUtil.buildComboSelected(arrSubsanacionList, MiUtil.getString((String) objPODevBean.getNpState()))%>
                  </select>
                     <input type="hidden" name="hdnCorrected" value="<%=objPODevBean.getNpState()%>">               
               </div>

               <div id="dvByTxtCorrected" style=display:"">
                  <%=(npState!=null?npState:"")%>
               </div> 
            </td> 
            
            <%}}%>
            <!--Campos a Comentar FIN - LROSALES-P1D-->
          </tr>
          <%
              }
              }
            }
          %>
        </table>
      </td>
    </tr>
  </table>
  
  <%
  } catch (Exception e) {
    e.printStackTrace();
    System.out.println("Mensaje::::" + e.getMessage() + "<br>");
    System.out.println("Causa::::" + e.getCause() + "<br>");
    for (int i = 0; i < e.getStackTrace().length; i++) {
      System.out.println("    " + e.getStackTrace()[i] + "<br>");
	  }
  }%>

