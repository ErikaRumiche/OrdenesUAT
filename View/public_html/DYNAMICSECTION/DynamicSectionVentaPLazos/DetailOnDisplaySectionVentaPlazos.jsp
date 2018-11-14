<%@ page contentType="text/html; charset=windows-1252"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="pe.com.nextel.service.GeneralService"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="pe.com.nextel.bean.DominioBean"%>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="java.util.Hashtable"%>
<%@ page import="pe.com.nextel.service.NewOrderService" %>

<%

try{
   GeneralService generalService = new GeneralService();
   NewOrderService  objNewOrderService  = new NewOrderService();

   //INICIO: PRY-1200 | AMENDEZ
   String strRutaContext=request.getContextPath();
   String strURLOrderServlet =strRutaContext+"/editordersevlet";
   //FIN: PRY-1200 | AMENDEZ

   HashMap objHashConfiguration = generalService.getConfigurationInstallment("VALID_NUM_QUOTA");

   Hashtable hshtInputDetailSection = null;
   hshtInputDetailSection     = (Hashtable)request.getAttribute("hshtInputDetailSection");
   String strFlagVep = null;
   String strNumCuotas = null;
   String strAmountVep = null;
   String strValor = null;
   String strOrderId = null;
   String strInitialQuota=null;
   //INICIO: PRY-0980 | AMENDEZ
   String strNpPaymentTermsIQ=null;
   //FIN: PRY-0980 | AMENDEZ

   if ( hshtInputDetailSection != null ) {
      strFlagVep           =   MiUtil.getString((String)hshtInputDetailSection.get("strFlagVep"));
      strNumCuotas         =   MiUtil.getString((String)hshtInputDetailSection.get("strNumCuotas"));
      strAmountVep         =   MiUtil.getString((String)hshtInputDetailSection.get("strAmountVep"));
      strOrderId           =   MiUtil.getString((String)hshtInputDetailSection.get("strOrderId"));
      strInitialQuota      =   MiUtil.getString((String)hshtInputDetailSection.get("strInitialQuota"));
      //INICIO: PRY-0980 | AMENDEZ
      strNpPaymentTermsIQ  =   MiUtil.getString((String)hshtInputDetailSection.get("strNpPaymentTermsIQ"));
      //FIN: PRY-0980 | AMENDEZ
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
                        <%if (strFlagVep.equals("1")){%>
                           <input style="height :13 px;" type="checkbox" id="chkVepFlag" value="1" checked name="chkVepFlag" disabled>
                        <%}else{%>
                           <input style="height :13 px;"  type="checkbox" id="chkVepFlag" value= "0" name="chkVepFlag" disabled>
                        <%}%>
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
                                    <input type="input" id="txtCuotaInicial" value="<%=strInitialQuota %>" onkeypress="return validateFloatKeyPress(this,event);" name="txtCuotaInicial"/ disabled>
                                </td>
                                <td class="CellContent" align="center">
                                    <%if (strNpPaymentTermsIQ.equals("1")){%>
                                        <input style="height :13 px;" type="checkbox" id="chkPaymentTermsIQ" name="chkPaymentTermsIQ" value= "1" checked disabled />
                                    <%}else{%>
                                        <input style="height :13 px;" type="checkbox" id="chkPaymentTermsIQ" name="chkPaymentTermsIQ" value= "0" disabled />
                                    <%}%>

                                </td>
                            </tr>
                        </table>
                        <!-- FIN: PRY-0980 | AMENDEZ -->
                    </td>
                    <!-- FIN: PRY-0864 | AMENDEZ -->

					<td class="CellContent">
						<select name="cmbVepNumCuotas" style="width: 75%" disabled>
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

function fxVentaPLazosOnload(){}

function fxShowPopUpVep(){

   var flagvep = document.frmdatos.chkVepFlag.value;
   var numCuota = document.frmdatos.cmbVepNumCuotas.value;
    //INICIO: PRY-0864 | AMENDEZ
    var vtxtCuotaInicial = document.frmdatos.txtCuotaInicial.value;
    //FIN: PRY-0864 | AMENDEZ

    //INICIO: PRY-0980 | AMENDEZ
    var vchkPaymentTermsIQ=  document.frmdatos.chkPaymentTermsIQ.value;
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