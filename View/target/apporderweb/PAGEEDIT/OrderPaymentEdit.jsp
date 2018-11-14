<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.*" %>
<%@ page import="pe.com.nextel.bean.*" %>
<%@ page import="pe.com.nextel.util.*" %>
<%@ page import="java.util.*" %>

<% 
try{
   //Parametros de la página
    String strRuc=(request.getParameter("hdnRuc")==null?"":request.getParameter("hdnRuc"));
    //long lImporteFactura=(request.getParameter("txtImporteFactura")==null?0:MiUtil.parseLong(request.getParameter("txtImporteFactura")));
    double lImporteFactura=(request.getParameter("txtImporteFactura")==null?0:Double.valueOf(request.getParameter("txtImporteFactura")).doubleValue());
   // long lTotalPagoOriginal=(request.getParameter("hdnTotalPaymentOrig")==null?0:MiUtil.parseLong(request.getParameter("hdnTotalPaymentOrig")));       
    double lTotalPagoOriginal=(request.getParameter("hdnTotalPaymentOrig")==null?0:Double.valueOf(request.getParameter("hdnTotalPaymentOrig")).doubleValue());
    String strPagoNroVoucher=""; //deben venir de orderedit pero alli  no setea ningun valor, el codigo anterior esta asi
    long lPagoImporte=0; //deben venir de orderedit pero alli  no setea ningun valor, el codigo anterior esta asi    
    String strPagoFecha=""; //deben venir de orderedit pero alli  no setea ningun valor, el codigo anterior esta asi
    long lPagoDiponible=0; //deben venir de orderedit pero alli  no setea ningun valor, el codigo anterior esta asi
    
    String strRutaContexto=request.getContextPath();
    GeneralService objGeneralService= new GeneralService();   
    String strMessage=null;
    HashMap hshData=null;
    ArrayList arrLista=null;
    
    
%>
 <% int i=0;
   /* PROCEDURE PL_ORDER_PAYMENT_EDIT (*/
 %>
         <HTML>
            <head><title>Registar/Editar Pago</title>
            </head>
            <link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css">
            <script language="JavaScript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/date-picker.js"></script>                        
            <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
            <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>         
            <script language="JavaScript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/DateTimeBasicOperations.js"></script>            
          
            <script>
               var gNroRuc="<%=strRuc%>";
               var gImporteFactura="<%=lImporteFactura%>";
               var gtotalPagoOriginal="<%=lTotalPagoOriginal%>";
               
               function round(number,X) {
                  X = (!X ? 2 : X);
                  return Math.round(number*Math.pow(10,X))/Math.pow(10,X);
               }  
               function CheckDate(obj) {
                  Form = document.formdatos;
                  var datestr = obj.value
                  if ( datestr != "" ){
                     if ( !isValidDate(datestr)){
                        obj.focus();
                        obj.select()
                        return false;
                     }
                  }
                  if (trim(Form.txtNroVoucher.value).length>0 && obj.value!="")
                     fxValidVoucher();
                  return true;
               }
                     
               function fxValidVoucher(){
                  Form = document.formdatos;
                  var idBank     = Form.cmbBank.options[Form.cmbBank.selectedIndex].value;
                  Form.txtNroVoucher.value=trim(Form.txtNroVoucher.value);
                  var FechaVoucher=trim(Form.txtFecha.value);                  
                  if (FechaVoucher.length==0){
                     alert("Ingrese la fecha emisión del Voucher")
                     Form.txtFecha.focus();
                     Form.txtFecha.select();
                     return false;
                  }
                  
                  if (!ContentOnlyNumberDec(Form.txtNroVoucher.value)){
                     alert('Voucher no válido');
                     Form.txtNroVoucher.focus();Form.txtNroVoucher.select();
                     return false;
                  }                        
                  var nroVoucher = Form.txtNroVoucher.value;                                    
                  if (  nroVoucher.length>0 && (Form.cmbBank.value>0) ){                    
                     //var url = "/portal/pls/portal/!WEBCCARE.NPAC_ORDER_EDIT02_PL_PKG.PL_VOUCHER_VALIDATE?idBank="+idBank+"&nroVoucher="+nroVoucher+"&nroRuc="+gNroRuc+"&FechaPago="+Form.txtFecha.value;
                     var url = "<%=strRutaContexto%>/editordersevlet?myaction=ValidateVaucher"+"&idBank="+idBank+"&nroVoucher="+nroVoucher+"&nroRuc="+gNroRuc+"&FechaPago="+Form.txtFecha.value;
                     parent.bottomFrame.location.replace(url);
                  }   
               }
/*function fxSendValues(){
   if (){
      fxSendValues();
   }
}*/

               //al hacer click en aceptar
               function fxSendValues(){
                  var pform = parent.opener.document.frmdatos;
                  var Form = document.formdatos;
                  if (parseFloat(gtotalPagoOriginal)> 0 && parseFloat(gtotalPagoOriginal) >= parseFloat(gImporteFactura)){
                     alert("No puede agregar pagos, el importe pagado actual es mayor o igual al importe de la orden")                                    
                     parent.close();

                  }else{   
                     var TotalPayment  =(Math.min(parseFloat((gImporteFactura==""?"0":gImporteFactura)),parseFloat((gtotalPagoOriginal=="")?"0":gtotalPagoOriginal) + parseFloat((Form.txtDisponible.value=="")?"0":Form.txtDisponible.value))).toString();
                     var Saldo         =(parseFloat((pform.txtImporteFactura.value==""?"0":pform.txtImporteFactura.value)) - TotalPayment).toString();
                     Saldo             =(Saldo!=0 && Saldo!="")?Math.round(Saldo*100)/100:"0"; 
                     pform.hdnPagoBanco.value         =  Form.cmbBank.options[Form.cmbBank.selectedIndex].value;//
                     pform.hdnPagoNroVoucher.value    =  Form.txtNroVoucher.value;//
                     pform.hdnPagoImporte.value       =  Form.txtImporte.value;
                     pform.hdnPagoFecha.value         =  Form.txtFecha.value; //
                     pform.hdnPagoDisponible.value    =  Form.txtDisponible.value;
                     pform.txtnpTotalPayment.value    =  CompletarNumeroDec(""+(TotalPayment==""?"0":TotalPayment),2);
                     pform.txtSaldo.value             =  CompletarNumeroDec(""+Saldo,2);
                     pform.imgPago.src                =  "/websales/images/Editar.gif";
                     
                     /*alert("hdnPagoBanco-->"+pform.hdnPagoBanco.value);
                     alert("hdnPagoNroVoucher-->"+pform.hdnPagoNroVoucher.value);
                     alert("hdnPagoImporte-->"+pform.hdnPagoImporte.value);
                     alert("hdnPagoFecha-->"+pform.hdnPagoFecha.value);
                     alert("hdnPagoDisponible-->"+pform.hdnPagoDisponible.value);
                     alert("txtnpTotalPayment-->"+pform.txtnpTotalPayment.value);
                     alert("txtSaldo-->"+pform.txtSaldo.value);*/
                     parent.close();
                  }   
               }
               //al hacer click en cancelar
               function fxCancel(){
                  parent.close()
               }               
               //al hacer click en borrar
               function fxClearFields(){
                  var pform = parent.opener.document.frmdatos;
                  var Form = document.formdatos;
                  var Saldo=(parseFloat((pform.txtImporteFactura.value==""?"0":pform.txtImporteFactura.value)) - parseFloat((pform.hdnTotalPaymentOrig.value==""?"0":pform.hdnTotalPaymentOrig.value))).toString();
                  pform.hdnPagoBanco.value         =  "";
                  pform.hdnPagoNroVoucher.value    =  "";
                  pform.hdnPagoImporte.value       =  "0";
                  pform.hdnPagoFecha.value         =  "";
                  pform.hdnPagoDisponible.value    =  "0";
                  pform.txtnpTotalPayment.value    =  CompletarNumeroDec(""+pform.hdnTotalPaymentOrig.value,2); 
                  pform.txtSaldo.value             =  ( Saldo != 0 )?CompletarNumeroDec(""+round(eval(Saldo),2)):"0"; 
                  pform.imgPago.src = "<%=Constante.PATH_APPORDER_SERVER%>/images/crear.gif";
                  //parent.close();
               }        
               
               function fxOnchangeVoucher(valor){
                  var val = trim(valor);                  
                  if (!ContentOnlyNumberDec(val))
                  {  alert('Valor no válido'); 
                     this.focus();
                     //this.select(); 
                     return false;
                  }else
                  { 
                     fxValidVoucher();
                     return true;
                  }                           
               }
               
               
            </script>
            <BODY>
               <form method="post" name="formdatos">
               <table border="0" class="PortletHeaderColor" cellspacing="0" cellpadding="0" width="100%">
               <tr class="PortletHeaderColor">
                  <td class="LeftCurve" valign="top" align="left" width="10" >&nbsp;&nbsp;</td>
                  <td class="PortletHeaderColor"align="LEFT" valign="top"> <font class="PortletHeaderText">Registrar/Editar Pago</font></td>
                  <td align="right" class="RightCurve" width="10" >&nbsp;&nbsp;</td>
               </tr>
               </table>               
               
               <table border="1" width="100%" cellpadding="0" cellspacing="0" align="center" class="RegionBorder">
               <tr valign="top">
                  <td class="RegionHeaderColor" height="97">
                  <table border="0" cellspacing="2" cellpadding="0" width="99%" align="center" class="BannerSecondaryLink">
                        <tr id="idBank">
                           <td class="CellLabel" align="left">&nbsp;<font color="red">*</font>&nbsp;Banco&nbsp;</td>
                           <td class="CellContent">&nbsp;
                            <select name="cmbBank" style="width: 60%;" onChange="javascript:if(trim(this[this.selectedIndex].text).length>0){fxValidVoucher();}">
                             <%     
                               hshData = objGeneralService.getTableList("PAYMENT_BANK","1");
                               strMessage=(String)hshData.get("strMessage");
                               if (strMessage!=null)
                                  throw new Exception(strMessage);                               
                               arrLista=(ArrayList)hshData.get("arrTableList");                                  
                             %>
                             <%=MiUtil.buildCombo(arrLista,"wv_npValue","wv_npValueDesc")%> 
                             <script>
                             document.formdatos.cmbBank.value="7";
                             </script>                        
                           </select>
                           <input type="hidden" name="hdnStatus" value="" >                            
                           </td>   
                        </tr>   
                        <tr id="idFecha">
                           <td class="CellLabel" align="left">&nbsp;<font color="red">*</font>&nbsp;Fecha&nbsp;</td>
                           <td class="CellContent">&nbsp;<input type="text" name="txtFecha" value="<%=strPagoFecha%>" size="10" maxlength="10" onBlur="CheckDate(this)">&nbsp;
                             <a href="javascript:show_calendar('formdatos.txtFecha',null,null,'DD/MM/YYYY')" onmouseover="window.status='Fecha Voucher';return true;" onmouseout="window.status='';return true;">
                             <img src="<%=Constante.PATH_APPORDER_SERVER%>/images/show-calendar.gif" width=24 height=22 border=0></a>&nbsp;<i>DD/MM/YYYYY&nbsp;&nbsp;</font>
                           </td>
                        </tr>   
                        <tr id="idVoucher">
                           <td class="CellLabel" align="left">&nbsp;<font color="red">*</font>&nbsp;N°&nbsp;Voucher</td>
                           <td class="CellContent">&nbsp;<input type="text" name="txtNroVoucher" maxlength="8" value="<%=strPagoNroVoucher%>"
                           onChange="javascript: fxOnchangeVoucher(this.value);">
                           </td>
                        </tr>   
                        <tr id="idImporte">
                           <td class="CellLabel" align="left">&nbsp;&nbsp;&nbsp;Importe&nbsp;</td>
                           <td class="CellContent">&nbsp;<input type="text" name="txtImporte" value="<%=MiUtil.getString(lPagoImporte)%>" onFocus="javascript:blur();" >
                           </td>
                        </tr>   
                        <tr id="idDisponible">
                           <td class="CellLabel" align="left">&nbsp;&nbsp;&nbsp;Disponible&nbsp;</td>
                           <td class="CellContent">&nbsp;<input type="text" name="txtDisponible" value="<%=MiUtil.getString(lPagoDiponible)%>" onFocus="javascript:blur();" >
                           </td>
                        </tr>   
                  </table>
                  </td>
                  </tr>    
               </table>  
               <!--Linea de separación -->
               <table><tr align="center"><td></td></tr></table>
               <table border="0" cellspacing="0" cellpadding="0" align="center">               
                  <tr><td align="center"><input type="button" name="btnAceptar" value=" Aceptar " disabled=true onclick="javascript:fxSendValues()">
                      </td>   
                      <td align="center">&nbsp;&nbsp;<input type="button" name="btnCancelar" value=" Cancelar " onclick="javascript:fxCancel()">                     
                      </td>     
                      <td align="center">&nbsp;&nbsp;<input type="button" name="btnBorrar" value=" Borrar " onclick="javascript:fxClearFields()">
                      </td>  
                  </tr>
               </table>
               </form>   
            </BODY>
         </HTML>
<% 
}catch(Exception ex){
  System.out.println("Error try catch-->"+MiUtil.getMessageClean(ex.getMessage())); 
%>
<script>
  alert("<%=MiUtil.getMessageClean(ex.getMessage())%>");
</script>
<%
}
%>  