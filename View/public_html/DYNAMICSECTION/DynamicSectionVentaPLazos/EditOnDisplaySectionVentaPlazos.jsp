<%@ page contentType="text/html; charset=windows-1252"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="pe.com.nextel.service.GeneralService"%>
<%@ page import="pe.com.nextel.service.OrderSearchService"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="pe.com.nextel.bean.DominioBean"%>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="java.util.Hashtable"%>
<%@page import="pe.com.nextel.bean.PortalSessionBean"%>
<%@page import="pe.com.nextel.service.SessionService"%>

<%

try{
    String strRutaContext=request.getContextPath();
    String strURLOrderServlet =strRutaContext+"/editordersevlet";
   GeneralService generalService = new GeneralService();
   OrderSearchService  objOrderSearchService  = new OrderSearchService();
   
   HashMap objHashConfiguration = generalService.getConfigurationInstallment("VALID_NUM_QUOTA");
   Hashtable hshtinputNewSection = null;  
   hshtinputNewSection     = (Hashtable)request.getAttribute("hshtInputEditSection");
   
   String strFlagVep = null;
   String strNumCuotas = null;
   String strAmountVep = null;
   String strValor = null;
   String strOrderId = null;
   String strSalesStuctOrigenId = "";

   //INICIO: PRY-0864 | AMENDEZ
   String strInitialQuota="";
   //FIN: PRY-0864 | AMENDEZ

    //INICIO: PRY-0980 | AMENDEZ
    String strNpPaymentTermsIQ=null;
    String strCodigoCliente   =null;
    String strSessionId       =null;
    int    iUserId               = 0;
    //FIN: PRY-0980 | AMENDEZ

   if ( hshtinputNewSection != null ) {
      strFlagVep            =   MiUtil.getString((String)hshtinputNewSection.get("strFlagVep"));
      strNumCuotas          =   MiUtil.getString((String)hshtinputNewSection.get("strNumCuotas"));
      strAmountVep          =   MiUtil.getString((String)hshtinputNewSection.get("strAmountVep")); 
      strOrderId            =   MiUtil.getString((String)hshtinputNewSection.get("strOrderId"));
      strSalesStuctOrigenId =   MiUtil.getString((String)hshtinputNewSection.get("strSalesStuctOrigenId"));
      //INICIO: PRY-0864 | AMENDEZ
      strInitialQuota      =   MiUtil.getString((String)hshtinputNewSection.get("strInitialQuota"));
      //FIN: PRY-0864 | AMENDEZ

       //INICIO: PRY-0980 | AMENDEZ
       strNpPaymentTermsIQ  =   MiUtil.getString((String)hshtinputNewSection.get("strNpPaymentTermsIQ"));
       strCodigoCliente        =   (String)hshtinputNewSection.get("strCustomerId");
       strSessionId            =   (String)hshtinputNewSection.get("strSessionId");
       //FIN: PRY-0980 | AMENDEZ
   }
    PortalSessionBean objSessionBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
    if( objSessionBean != null ){
        iUserId  = objSessionBean.getUserid();
    }
   
%>
<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script type="text/javascript" src="/web_operacion/js/syncscroll.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>

<BR>

<table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
	<tr>
		<td align="left">
			<table border="0" cellspacing="0" cellpadding="0" align="left">
				<tr>
					<td class="SectionTitleLeftCurve" width="8">&nbsp;&nbsp;</td>
					<td class="SectionTitle" width="100" align="center">Venta a Plazos </td>
					<td class="SectionTitleRightCurve" width="8">&nbsp;&nbsp;</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td align="center">
			<table border="0" cellspacing="1" cellpadding="1" align="center" class="BannerSecondaryLink" width="100%">
				<tr>
					<td class="CellLabel" align="center">Venta a Plazos</td>
                    <!-- INICIO: PRY-0864 | AMENDEZ -->
                    <td class="CellLabel" align="center">Cuota Inicial</td>
                    <!-- FIN: PRY-0864 | AMENDEZ -->
					<td class="CellLabel" align="center">N&uacute;mero cuotas</td>		
                    <td class="CellLabel" align="center">&nbsp;</td>
				</tr>
				<tr>
					<td class="CellContent" align="center">
						<input style="height :13 px;" type="checkbox" id="chkVepFlag" name="chkVepFlag" onclick="fxSetValue(this)"/>
					</td>
                    <!-- INICIO: PRY-0864 | AMENDEZ -->
                    <td class="CellContent" align="center">

                        <!-- INICIO: PRY-0980 | AMENDEZ -->
                        <table  border="0" cellspacing="1" cellpadding="1" align="center" class="BannerSecondaryLink" width="100%">
                            <tr>
                                <td class="CellLabel" align="center">Importe</td>
                                <td class="CellLabel" align="center">Cargo en el recibo</td>
                            </tr>
                            <tr>
                                <td class="CellContent" align="center">
                                    <input type="input" id="txtCuotaInicial" value="<%=strInitialQuota %>" name="txtCuotaInicial" onblur="return validateKeyUp(this,event);" onkeypress="return validateFloatKeyPress(this,event);"  />
                                </td>
                                <td class="CellContent" align="center">
                                    <input style="height :13 px;" type="checkbox" id="chkPaymentTermsIQ" name="chkPaymentTermsIQ" onclick="fxSetValuePaymentTermsIQ(this)"/>
                                    <input type="hidden" name="txtPaymentTermsIQ" id="txtPaymentTermsIQ">
                                </td>
                            </tr>
                        </table>
                        <!-- FIN: PRY-0980 | AMENDEZ -->
                    </td>
                    <!-- FIN: PRY-0864 | AMENDEZ -->
          
					<td class="CellContent">
						<select id="cmbVepNumCuotas"  name="cmbVepNumCuotas" style="width: 75%" onclick="" onchange="fxValidateNumeroCuotas(this, 0)">
            <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
						<% 
            if(objHashConfiguration != null) {
              if(objHashConfiguration.get("strMessage") == null) {
                ArrayList objListConfiguration = (ArrayList)objHashConfiguration.get("objArrayList"); 
                for(int i=0;i<objListConfiguration.size();i++) {
                  DominioBean objDominio = (DominioBean)objListConfiguration.get(i);                  
            %>	
            <option value="<%=objDominio.getDescripcion()%>"><%=objDominio.getDescripcion()%></option>              
						      <%
                  if ((strNumCuotas!= null) && (objDominio.getDescripcion().equalsIgnoreCase(strNumCuotas))){
                     strValor = objDominio.getDescripcion();
                  } 
                }
              }
            }%>
            </select>
            <script>            
               document.frmdatos.cmbVepNumCuotas.value="<%=strValor%>";
            </script>
					</td>
				
        	<td class="CellContent">
          <a href="javascript:fxShowPopUpVep()">Resumen Venta a Plazos</a>
          </td>
			
				</tr>
				<tr>
			</table>
		</td>
	</tr>
  <tr>
     <td>&nbsp;&nbsp;</td>
  </tr>
</table>
	
<script DEFER>

var indexComboCuota;
function fxVentaPLazosOnload(){
  //Carga valores del flag VEP
  indexComboCuota = document.frmdatos.cmbVepNumCuotas.selectedIndex;
  var chkVepFlag = document.frmdatos.chkVepFlag;
  var x=document.getElementById("cmbVepNumCuotas");
  
  if (document.frmdatos.cmbFormaPago.value == '<%=Constante.PAYFORM_CARGO_EN_RECIBO%>'){  
     fxSetValueInicial(chkVepFlag)
  }else{   
     chkVepFlag.checked = false;
     chkVepFlag.disabled = true;     
     x.disabled = true;
     //INICIO: PRY-0864 | AMENDEZ
      document.frmdatos.txtCuotaInicial.disabled = true;
     //FIN: PRY-0864 | AMENDEZ

      //INICIO: PRY-0980 | AMENDEZ
      document.frmdatos.chkPaymentTermsIQ.checked = false;
      document.frmdatos.chkPaymentTermsIQ.disabled = true;
      //FIN: PRY-0980 | AMENDEZ
  }
    //INICIO: PRY-0980 | AMENDEZ
    var validatePaymentTerms=validatePaymentTermsCI();
    if(validatePaymentTerms == "1"){
        document.frmdatos.chkPaymentTermsIQ.disabled = false;
    }else{
        document.frmdatos.chkPaymentTermsIQ.disabled = true;
    }
    //FIN: PRY-0980 | AMENDEZ
}

//INICIO: PRY-0980 | AMENDEZ
function fxSetValuePaymentTermsIQ(chkPaymentTermsIQ){
    var cuotainicial=document.frmdatos.txtCuotaInicial.value;
    if (chkPaymentTermsIQ.checked ==true){
        if(cuotainicial > 0){
        document.frmdatos.chkPaymentTermsIQ.value = "1";
            document.frmdatos.txtPaymentTermsIQ.value = "1";
    }else {
        document.frmdatos.chkPaymentTermsIQ.value = "0";
            document.frmdatos.txtPaymentTermsIQ.value = "0";
            document.frmdatos.chkPaymentTermsIQ.checked = false;
            alert('La cuota inicial debe ser mayor a cero para seleccionar cargo en el recibo');
        }
    }else {
        document.frmdatos.chkPaymentTermsIQ.value = "0";
        document.frmdatos.txtPaymentTermsIQ.value = "0";

    }
}

function validatePaymentTermsCI(){
    var varValidatePaymentTermsCI=0;
    try{
        var url_server    = '<%=strURLOrderServlet%>';
        var npcustomerId  = "<%=strCodigoCliente%>";
        var npuserid      = "<%=iUserId%>";
        var npvep         = document.frmdatos.chkVepFlag.value;

        var params = 'myaction=validatePaymentTermsCI&npcustomerId='+npcustomerId+'&npuserid='+npuserid+'&npvep='+npvep;

        $.ajax({
            url: url_server,
            data: params,
            async: false,
            type: "POST",
            success:function(data){
                varValidatePaymentTermsCI=data;
            }
        });

        return varValidatePaymentTermsCI;
    }catch (e){
        alert("Hubo un error en la validacion de orden de venta a plazos");
        return varValidatePaymentTermsCI;
    }
}
//FIN: PRY-0980 | AMENDEZ

function fxSetValueInicial(chkVepFlag){
  <%if (strFlagVep.equalsIgnoreCase("1")){%>     
     chkVepFlag.checked = true;     
     chkVepFlag.value = "1";
     fxDeshabilitarComboCuota(chkVepFlag.checked);
    //INICIO: PRY-0864 | AMENDEZ
    document.frmdatos.txtCuotaInicial.disabled = false;
    //FIN: PRY-0864 | AMENDEZ

    //INICIO: PRY-0980 | AMENDEZ
    document.frmdatos.chkPaymentTermsIQ.disabled = false;
    <%if (strNpPaymentTermsIQ.equalsIgnoreCase("1")){%>
        document.frmdatos.chkPaymentTermsIQ.checked = true;
        document.frmdatos.chkPaymentTermsIQ.value = "1";
        document.frmdatos.txtPaymentTermsIQ.value = "1";
    <%}else {%>
        document.frmdatos.chkPaymentTermsIQ.checked = false;
        document.frmdatos.chkPaymentTermsIQ.value = "0";
        document.frmdatos.txtPaymentTermsIQ.value = "0";
    <%}%>
    //FIN: PRY-0980 | AMENDEZ
  <%}else {%>
     chkVepFlag.checked = false;
     chkVepFlag.value = "0";
     fxDeshabilitarComboCuota(chkVepFlag.checked);
    //INICIO: PRY-0864 | AMENDEZ
    document.frmdatos.txtCuotaInicial.disabled = true;
    //FIN: PRY-0864 | AMENDEZ

    //INICIO: PRY-0980 | AMENDEZ
    document.frmdatos.chkPaymentTermsIQ.disabled = true;
    document.frmdatos.chkPaymentTermsIQ.checked = false;
    document.frmdatos.chkPaymentTermsIQ.value = "0";
    document.frmdatos.txtPaymentTermsIQ.value = "0";
    //FIN: PRY-0980 | AMENDEZ
  <%}%>
}

function fxSetValue(chkVepFlag){
   if (closepopupOrderItem()){//Valida que la ventana del popUp del Item de la orden este cerrado
      if (chkVepFlag.checked == true){ 
         if (fxValidateExistItemsVEP(chkVepFlag,0)){ //Valida que no existan Items con Modalidad venta
            chkVepFlag.value = "1";
            fxDeshabilitarComboCuota(chkVepFlag.checked);
             //INICIO: PRY-0864 | AMENDEZ
             document.frmdatos.txtCuotaInicial.disabled = false;
             //FIN: PRY-0864 | AMENDEZ

             //INICIO: PRY-0980 | AMENDEZ
             document.frmdatos.chkPaymentTermsIQ.disabled = false;
             //FIN: PRY-0980 | AMENDEZ
         }
       }else{
         if (fxValidateExistItemsVEP(chkVepFlag,0)){ //Valida que no existan Items con Modalidad venta
            chkVepFlag.value = "0";
            fxDeshabilitarComboCuota(chkVepFlag.checked);
             //INICIO: PRY-0864 | AMENDEZ
             document.frmdatos.txtCuotaInicial.disabled = true;
             document.frmdatos.txtCuotaInicial.value='0.00';
             //FIN: PRY-0864 | AMENDEZ

             //INICIO: PRY-0980 | AMENDEZ
             document.frmdatos.chkPaymentTermsIQ.checked = false;
             document.frmdatos.chkPaymentTermsIQ.disabled = true;
             //FIN: PRY-0980 | AMENDEZ
         }
      }
   }else{
      if (chkVepFlag.checked == true){
         chkVepFlag.checked = false;
      }else{
         chkVepFlag.checked = true;
      }
   }

    //INICIO: PRY-0980 | AMENDEZ
    var validatePaymentTerms=validatePaymentTermsCI();
    if(validatePaymentTerms == "1"){
        document.frmdatos.chkPaymentTermsIQ.disabled = false;
    }else{
        document.frmdatos.chkPaymentTermsIQ.disabled = true;
    }
    //FIN: PRY-0980 | AMENDEZ
}

function fxDeshabilitarComboCuota(flagChecked){
  var x=document.getElementById("cmbVepNumCuotas");
  if (flagChecked){
     x.disabled = false;
  }else{
     x.selectedIndex = 0;
     x.disabled = true;     
  }
}


function fxValidateNumeroCuotas(objComboCuota, val){

   if (closepopupOrderItem()){
       //INICIO: PRY-1200 | AMENDEZ
       fxValidateExistItemsVEP(objComboCuota, val);
       //FIN: PRY-1200 | AMENDEZ
   }else{
      document.getElementById(objComboCuota.id).selectedIndex  = indexComboCuota;
   }
}

function fxValidateExistItemsVEP(obj, val){
   var items_vep = 0;
   if (form.txtItemModality != undefined){
      if (form.txtItemModality.length == undefined) {
         if (form.txtItemModality.value == "Venta"){
            items_vep++;
         }
      }else{
         for(i = 0; i < form.txtItemModality.length; i++){
            if (form.txtItemModality[i].value == "Venta"){
               items_vep++;
            }
         }
      }
   }
   
   if (val == "0"){
     if (items_vep > 0){
         alert("No puede modificar las condiciones de la venta mientras existan ítems en el detalle de la orden con la Modalidad Venta");
         if (obj.id == "cmbVepNumCuotas"){
            document.getElementById(obj.id).selectedIndex  = indexComboCuota;
     
         }else if(obj.id == "chkVepFlag"){
            if (obj.checked == true){
               obj.checked = false;
            }else{
               obj.checked = true;
            }
         }
         return false;
     }
     return true;
   }else{
   
      return items_vep;
   }
}


function fxSaveIndexComboCuota(obj){
   indexComboCuota = obj.selectedIndex;
   return indexComboCuota;
}

   
function fxShowPopUpVep(){
   
   var flagvep = document.frmdatos.chkVepFlag.value;   
   var numCuota = document.frmdatos.cmbVepNumCuotas.value;
    //INICIO: PRY-0864 | AMENDEZ
    var vtxtCuotaInicial = document.frmdatos.txtCuotaInicial.value;
    //FIN: PRY-0864 | AMENDEZ

    //INICIO: PRY-0980 | AMENDEZ
    var vchkPaymentTermsIQ=  document.frmdatos.txtPaymentTermsIQ.value;
    //FIN: PRY-0980 | AMENDEZ
   if (fxValidateExistItemsVEP(document.frmdatos.cmbVepNumCuotas, 1) > 0 && flagvep=="1"){
       //INICIO: PRY-0864 | AMENDEZ
       try{
           var priceItemTotal=parseFloat(form.txtTotalSalesPrice.value);
           if(priceItemTotal < vtxtCuotaInicial){
               alert("El monto de la cuota inicial debe ser menor al monto total de la orden");
           }else{
               var winUrl = appContext+"/DYNAMICSECTION/DynamicSectionVentaPLazos/PopUpVentaPlazos.jsp?numCuota=" + numCuota + "&flagvep=" +flagvep+ "&cuotaInicial=" +vtxtCuotaInicial+ "&paymentterms=" +vchkPaymentTermsIQ;
               var popupWin = window.open(winUrl, "Resumen_Vep","status=yes, location=0, width=620, height=600, left=300, top=30, screenX=50, screenY=100");
           }
       }catch(e){}
       //FIN: PRY-0864 | AMENDEZ


   }else{
      alert("No existen Items de Venta a Plazos para la orden");
   }
}

//INICIO: PRY-0864 | AMENDEZ
function validateFloatKeyPress(el, evt) {
    var charCode = (evt.which) ? evt.which : event.keyCode;
    var number = el.value.split('.');
    if (charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57)) {
        return false;
    }

    if(number.length>1 && charCode == 46){
        return false;
    }

    var caratPos = getSelectionStart(el);
    var dotPos = el.value.indexOf(".");
    if( caratPos > dotPos && dotPos>-1 && (number[1].length > 1)){
        return false;
    }

    //INICIO: PRY-0980 | AMENDEZ
    var cuotainicial=document.frmdatos.txtCuotaInicial.value;
    if(cuotainicial=="."){
        document.frmdatos.txtCuotaInicial.value="";
        return false;
    }
    //FIN: PRY-0980 | AMENDEZ

    return true;
}

function getSelectionStart(o) {
    if (o.createTextRange) {
        var r = document.selection.createRange().duplicate()
        r.moveEnd('character', o.value.length)
        if (r.text == '') return o.value.length
        return o.value.lastIndexOf(r.text)
    } else return o.selectionStart
}
//FIN: PRY-0864 | AMENDEZ

//INICIO: PRY-0980 | AMENDEZ
function validateKeyUp(el, evt) {
    var charCode = (evt.which) ? evt.which : event.keyCode;
    var number = el.value.split('.');
    if (charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57)) {
        return false;
    }

    if(number.length>1 && charCode == 46){
        return false;
    }

    var caratPos = getSelectionStart(el);
    var dotPos = el.value.indexOf(".");
    if( caratPos > dotPos && dotPos>-1 && (number[1].length > 1)){
        return false;
    }

    var cuotainicial=document.frmdatos.txtCuotaInicial.value;
    if(cuotainicial == 0  || cuotainicial=="" ||cuotainicial=="."){
        document.frmdatos.chkPaymentTermsIQ.value = "0";
        document.frmdatos.txtPaymentTermsIQ.value = "0";
        document.frmdatos.chkPaymentTermsIQ.checked = false;
        alert('La cuota inicial debe ser mayor a cero para seleccionar cargo en el recibo');
        return false;
    }

    return true;
}
//FIN: PRY-0980 | AMENDEZ

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
//FIN: PRY-1200 | AMENDEZ
</SCRIPT>

<% 

}catch(Exception  ex){                
   System.out.println("Error try catch-->"+MiUtil.getMessageClean(ex.getMessage())); 
%>
<script>
   alert("<%=MiUtil.getMessageClean(ex.getMessage())%>");
</script>
<%
}
%> 