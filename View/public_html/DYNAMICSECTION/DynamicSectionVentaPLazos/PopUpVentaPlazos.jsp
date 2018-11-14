<%@ page import="pe.com.nextel.util.Constante" %>


<% String strNumCuotas = request.getParameter("numCuota"); %>
<% String strFlagVep = request.getParameter("flagvep"); %>
<%
    String strCuotaInicial = request.getParameter("cuotaInicial");

    if(strCuotaInicial==null  || strCuotaInicial.equalsIgnoreCase("")){
        strCuotaInicial="0.00";
    }else if(strCuotaInicial.equals(".")){
        strCuotaInicial="0.00";
    }

    String strPaymentTermsIQ = request.getParameter("paymentterms");

%>

<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script type="text/javascript" src="/web_operacion/js/syncscroll.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>

  <table  align="center" width="100%" border="0">
    
    <tr>    
      <td>    
        <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
            <tr class="PortletHeaderColor">
              <td class="LeftCurve" valign="top" align="left" width="10">&nbsp;&nbsp;</td>
              <td class="PortletHeaderColor" align="center" valign="top">
              <font class="PortletHeaderText">Detalle Venta a Plazos</font>
              </td>
              <td align="right" class="RightCurve" width="10%" >&nbsp;&nbsp;</td>
             </tr>
        </table>		
      </td>
    </tr>
    
    <tr>
      <td>&nbsp;&nbsp;</td>
    </tr>
    
    <tr>
      <td align="left">
        <table border="0" cellspacing="0" cellpadding="0" align="left">
          <tr>
            <td class="SectionTitleLeftCurve" width="8">&nbsp;&nbsp;</td>
            <td class="SectionTitle" width="200" align="center">Cuadro Resumen Venta a Plazos </td>
            <td class="SectionTitleRightCurve" width="8">&nbsp;&nbsp;</td>
          </tr>
        </table>
      </td>
	  </tr>
    
    <tr>
      <td align="center">
        <table border="0" id="items_table_vep" cellspacing="1" cellpadding="1" align="center" class="BannerSecondaryLink" width="100%">
          <tr>		
            <td class="CellLabel" align="center" width="5%">Modelo</td>		
            <td class="CellLabel" align="center" width="19%">Plan Tarifario</td>	
            <td class="CellLabel" align="center" width="7%" >Cantidad</td>            
            <td class="CellLabel" align="center" width="10%">Numero de Cuotas</td>
            <!--INICIO: PRY-0864 | AMENDEZ  -->
            <td class="CellLabel" align="center" width="15%">Monto Total &nbsp;</td>
            <!--FIN: PRY-0864 | AMENDEZ  -->
          </tr>
        </table>    
      </td>
    </tr>

      <!--INICIO: PRY-0864 | AMENDEZ  -->
      <tr>
          <td align="center">
              <table border="0" id="items_table_vep_new" cellspacing="1" cellpadding="1" align="center" class="BannerSecondaryLink" width="100%">
                  <tr>
                      <!--INICIO: PRY-0980 | AMENDEZ  -->
                      <td class="CellLabel" align="center" width="10%">Forma de pago Cuota inicial</td>
                      <!--FIN: PRY-0980 | AMENDEZ  -->
                      <td class="CellLabel" align="center" width="7%">Cuota Inicial</td>
                      <td class="CellLabel" align="center" width="19%">Monto a Financiar</td>
                      <td class="CellLabel" align="center" width="7%" >Numero de Cuotas</td>
                      <td class="CellLabel" align="center" width="10%">Monto Cuotas</td>
                      <td class="CellLabel" align="center" width="15%">Monto Ultima Cuota &nbsp;</td>
                  </tr>
              </table>
          </td>
      </tr>
      <!--FIN: PRY-0864 | AMENDEZ  -->

      <tr>
          <td colspan = "6"  align="center" class="BannerSecondaryLink">&nbsp;&nbsp;* El valor calculado del monto de cuota puede variar de acuerdo al redondeo aplicado.</td>
      </tr>

  </table>

  <table  align="center" width="100%" border="0">
    <tr>
      <td>&nbsp;&nbsp;</td>
    </tr>
  
    <tr>
      <td align="left">
        <table border="0" cellspacing="0" cellpadding="0" align="left">
          <tr>
            <td class="SectionTitleLeftCurve" width="8">&nbsp;&nbsp;</td>
            <td class="SectionTitle" width="200" align="center">Cuadro Resumen No Venta a Plazos </td>
            <td class="SectionTitleRightCurve" width="8">&nbsp;&nbsp;</td>
          </tr>
        </table>
      </td>
    </tr>
    
    <tr>
      <td align="left">
        <table border="0" id="items_table_novep" cellspacing="1" cellpadding="1" align="center" class="BannerSecondaryLink" width="100%">
          <tr>            
            <td class="CellLabel" align="center" width="5%">Modelo</td>
            <td class="CellLabel" align="center" width="19%">Plan Tarifario</td>
            <td class="CellLabel" align="center" width="7%" >Cantidad</td>
            <td class="CellLabel" align="center" width="19%">Modalidad de Salida</td>			
            <td class="CellLabel" align="center" width="19%">Monto Total</td>
          </tr>
        </table>
      </td>
    </tr>
    
    <tr>
      <td colspan = "5"  align="center" class="BannerSecondaryLink">&nbsp;&nbsp;* Incluye todos los Items que no son de Venta a Plazos.</td>
    </tr>
    <tr>
      <td>&nbsp;</td>
    </tr>    
	  <tr>
      <td align="left">
        <table border="0" id="items_table_total" cellspacing="1" cellpadding="1" align="center" class="BannerSecondaryLink" width="100%"></table>
      </td>
    </tr>
</table>

<script defer>

fxLoadResumen();

function fxLoadResumen(){
  //Carga detalle Vep
  fxLoadResumenVep();  
}

function fxLoadResumenVep(){
      
   var vctFilaResumenVep = new Vector();
   var vctFilaResumenNoVep = new Vector();
   var cell;
   var elemText = "";  
   var totalMontoVep = 0.00;
   var totalMontoNoVep = 0.00;
   
   var cantColumnasVep = 0;
   var cantColumnasNoVep = 0;
   //INICIO: PRY-0864 | AMENDEZ
   var totalPriceFinal= 0;
   //FIN: PRY-0864 | AMENDEZ

   if (parent.opener.frmdatos.hdnItemValuetxtItemProduct.length == undefined){   
     
     if (('<%=strFlagVep%>' == "1") && (parent.opener.frmdatos.hdnItemValuetxtItemModality.value == "Venta")){
     
          var vctColumnaResumenVep = new Vector();        
          vctColumnaResumenVep.addElement(new fxMakeOrderItemVep(parent.opener.form.txtItemProduct.value));
          vctColumnaResumenVep.addElement(new fxMakeOrderItemVep(getPlanTarifario('A',0)));
          vctColumnaResumenVep.addElement(new fxMakeOrderItemVep(parent.opener.form.txtItemQuantity.value));
          vctColumnaResumenVep.addElement(new fxMakeOrderItemVep('<%=strNumCuotas%>'));

          //INICIO: PRY-0864 | AMENDEZ
          if(parent.opener.form.txtItemPriceException.value == undefined || parent.opener.form.txtItemPriceException.value=="") {
              totalPriceFinal=parent.opener.form.txtItemOriginalPrice.value;
          }else{
              totalPriceFinal=parent.opener.form.txtItemPriceException.value;
          }
          vctColumnaResumenVep.addElement(new fxMakeOrderItemVep(totalPriceFinal * parent.opener.form.txtItemQuantity.value));
          totalMontoVep += totalPriceFinal * parent.opener.form.txtItemQuantity.value;
          //FIN: PRY-0864 | AMENDEZ

          cantColumnasVep = vctColumnaResumenVep.size();
          vctFilaResumenVep.addElement(vctColumnaResumenVep);
     }else{      
          var vctColumnaResumenNoVep = new Vector();
          vctColumnaResumenNoVep.addElement(new fxMakeOrderItemVep(parent.opener.form.txtItemProduct.value));
          vctColumnaResumenVep.addElement(new fxMakeOrderItemVep(getPlanTarifario('A',0)));         
          vctColumnaResumenNoVep.addElement(new fxMakeOrderItemVep(parent.opener.form.txtItemQuantity.value));
          vctColumnaResumenNoVep.addElement(new fxMakeOrderItemVep(parent.opener.form.txtItemModality.value));
          vctColumnaResumenNoVep.addElement(new fxMakeOrderItemVep(parent.opener.form.txtitemTotal.value));
          
          totalMontoNoVep += parent.opener.form.txtitemTotal.value*1;
          
          vctFilaResumenNoVep.addElement(vctColumnaResumenNoVep);

     }
     
   }else{
     for(i=0; i<parent.opener.frmdatos.hdnItemValuetxtItemProduct.length; i++){    
      
        if (('<%=strFlagVep%>' == "1") && (parent.opener.frmdatos.txtItemModality[i].value == "Venta")){
          var vctColumnaResumenVep = new Vector();          
          vctColumnaResumenVep.addElement(new fxMakeOrderItemVep(parent.opener.frmdatos.txtItemProduct[i].value));
          vctColumnaResumenVep.addElement(new fxMakeOrderItemVep(getPlanTarifario('B', i)));
          vctColumnaResumenVep.addElement(new fxMakeOrderItemVep(parent.opener.frmdatos.txtItemQuantity[i].value));
          vctColumnaResumenVep.addElement(new fxMakeOrderItemVep('<%=strNumCuotas%>'));

          //INICIO: PRY-0864 | AMENDEZ
          if(parent.opener.form.txtItemPriceException[i].value == undefined || parent.opener.form.txtItemPriceException[i].value=="") {
             totalPriceFinal=parent.opener.form.txtItemOriginalPrice[i].value;
          }else{
             totalPriceFinal=parent.opener.form.txtItemPriceException[i].value;
          }
          vctColumnaResumenVep.addElement(new fxMakeOrderItemVep(totalPriceFinal * parent.opener.frmdatos.txtItemQuantity[i].value));
          totalMontoVep += totalPriceFinal * parent.opener.frmdatos.txtItemQuantity[i].value;
          //FIN: PRY-0864 | AMENDEZ
          cantColumnasVep = vctColumnaResumenVep.size();
          vctFilaResumenVep.addElement(vctColumnaResumenVep);

        }else{      
          var vctColumnaResumenNoVep = new Vector();
          vctColumnaResumenNoVep.addElement(new fxMakeOrderItemVep(parent.opener.frmdatos.txtItemProduct[i].value));
          vctColumnaResumenNoVep.addElement(new fxMakeOrderItemVep(getPlanTarifario('B', i)));
          vctColumnaResumenNoVep.addElement(new fxMakeOrderItemVep(parent.opener.frmdatos.txtItemQuantity[i].value));
          vctColumnaResumenNoVep.addElement(new fxMakeOrderItemVep(parent.opener.frmdatos.txtItemModality[i].value));
          vctColumnaResumenNoVep.addElement(new fxMakeOrderItemVep(parent.opener.frmdatos.txtitemTotal[i].value));
          
          totalMontoNoVep += parent.opener.frmdatos.txtitemTotal[i].value;
          
          vctFilaResumenNoVep.addElement(vctColumnaResumenNoVep);

        }
     }
   }  
        
     //Resumen VEP
     for (f = 0; f < vctFilaResumenVep.size(); f++){
        var vctColumnas = new Vector();
        vctColumnas = vctFilaResumenVep.elementAt(f);         
        var row   = items_table_vep.insertRow(-1);
        index_arg = 1;
        for (c = 0; c < vctColumnas.size(); c++){
           
          cell = row.insertCell(index_arg - 1);   
          cell.className="CellContent";
          cell.align = "center";
          elemText = vctColumnas.elementAt(c).elementoColumna;    
          cell.innerHTML =  elemText; 
          index_arg++;
          elemText = "";
        }
     }
     
     //Carga total Resumen VEP
     var rowtotal   = items_table_vep.insertRow(-1);
     var elemTextTotal = "";
     var cellTotal;
     var index_arg_total = 1;
           
     for (t=1 ; t<8; t++){
        cellTotal = rowtotal.insertCell(index_arg_total-1); 
        
        cellTotal.align = "center";
        if ( t <4){
          elemTextTotal = "&nbsp;";
        }else if (t == 4){
            cellTotal.className="CellLabel";
            elemTextTotal = "Total";
        }else if (t == 5){
          cellTotal.className="CellLabel";
          elemTextTotal = parseFloat(round_decimals(totalMontoVep,2));
        }
        cellTotal.innerHTML =  elemTextTotal; 
        index_arg_total++;
        elemTextTotal = "";   
     }

    //INICIO: PRY-0864 | AMENDEZ
    //Carga calculo cuotas con cuota inicial
    var rowtotalCI   = items_table_vep_new.insertRow(-1);
    var elemTextTotalCI = "";
    var cellTotalCI;
    var index_arg_totalCI = 1;
    var cuotaInicial='<%=strCuotaInicial%>';
    var numeroCuotas='<%=strNumCuotas%>';
    var paymentterms='<%=strPaymentTermsIQ%>';
    var montoTotalFinanciado=round_decimals(parseFloat(totalMontoVep-cuotaInicial),2);
    var cuotasCalculadas=round_decimals(parseFloat(montoTotalFinanciado/numeroCuotas),1);
    var ultimaCuota=round_decimals(parseFloat(montoTotalFinanciado-(cuotasCalculadas*(numeroCuotas-1))),2);

    //INICIO: PRY-0980 | AMENDEZ
    for (t=1 ; t<7; t++){
    //FIN: PRY-0980 | AMENDEZ
        cellTotalCI = rowtotalCI.insertCell(index_arg_totalCI-1);

        cellTotalCI.align = "center";
        //INICIO: PRY-0980 | AMENDEZ
        if ( t ==1){
            cellTotalCI.className="CellContent";
            if(paymentterms=="1"){
                elemTextTotalCI = 'Cargo en el recibo';
            }else if(paymentterms=="0"){
                elemTextTotalCI = 'Contado';
            }
        }
        //FIN: PRY-0980 | AMENDEZ
        else if ( t ==2){
            cellTotalCI.className="CellContent";
            elemTextTotalCI = CompletarNumeroDec(''+cuotaInicial);
        }else if ( t ==3){
            cellTotalCI.className="CellContent";
            elemTextTotalCI = CompletarNumeroDec(''+montoTotalFinanciado);
        }else if ( t ==4){
            cellTotalCI.className="CellContent";
            elemTextTotalCI = numeroCuotas;
        }else if (t == 5){
            cellTotalCI.className="CellContent";
            elemTextTotalCI = CompletarNumeroDec(''+cuotasCalculadas);
        }else if (t == 6){
            cellTotalCI.className="CellContent";
            elemTextTotalCI = CompletarNumeroDec(''+ultimaCuota);
        }
        cellTotalCI.innerHTML =  elemTextTotalCI;
        index_arg_totalCI++;
        elemTextTotalCI = "";
    }
    //FIN: PRY-0864 | AMENDEZ
     
     //Resumen No VEP
     for (f = 0; f < vctFilaResumenNoVep.size(); f++){
        var vctColumnas = new Vector();
        vctColumnas = vctFilaResumenNoVep.elementAt(f);         
        var row   = items_table_novep.insertRow(-1);
        index_arg = 1;
        for (c = 0; c < vctColumnas.size(); c++){         
          cell = row.insertCell(index_arg - 1);   
          cell.className="CellContent";
          cell.align = "center";
          elemText = vctColumnas.elementAt(c).elementoColumna;    
          cell.innerHTML =  elemText; 
          index_arg++;
          elemText = "";
        }
     }
     
     //Carga total Resumen No VEP
     var rowtotalNoVep   = items_table_novep.insertRow(-1);
     var elemTextTotalNoVep = "";
     var cellTotalNoVep;
     var index_arg_total_noVep = 1;
           
     for (k=1 ; k<6; k++){
        cellTotalNoVep = rowtotalNoVep.insertCell(index_arg_total_noVep-1);
        cellTotalNoVep.align = "center";
        if ( k <4){
          elemTextTotalNoVep = "&nbsp;";
        }else if (k == 4){
          cellTotalNoVep.className="CellLabel";
          elemTextTotalNoVep = "Total";
        }else if (k == 5){
          cellTotalNoVep.className="CellLabel";
          elemTextTotalNoVep = parseFloat(round_decimals(totalMontoNoVep,2));
        }
        cellTotalNoVep.innerHTML =  elemTextTotalNoVep; 
        index_arg_total_noVep++;
        elemTextTotalNoVep = "";   
     }
   
     //Carga monto total recibo
     var rowtotal   = items_table_total.insertRow(-1);
     var cellTotal;
     cellTotal = rowtotal.insertCell(0);
     cellTotal.align = "right";
     cellTotal.className="CellLabel";

     cellTotal.innerHTML =  "El monto total a cargar en el siguiente recibo es S/. " + cuotasCalculadas; //FES Se cambió $ por S/.

}

function getPlanTarifario(tipoBusqueda, indice){
  
  var planTarifario;

  if (tipoBusqueda == 'A'){
    //Condicion para la creaci�n de la orden
    if (parent.opener.form.cmbSubCategoria != undefined) {          
      if (parent.opener.form.cmbSubCategoria.value == '<%=Constante.SPEC_POSTPAGO_VENTA%>'){
         planTarifario = parent.opener.form.txtItemRatePlan.value;
      }else if (parent.opener.form.cmbSubCategoria.value == '<%=Constante.SPEC_CAMBIO_MODELO%>'){
         planTarifario = parent.opener.form.txtItemNewRatePlan.value;
      }else if (parent.opener.form.cmbSubCategoria.value == '<%=Constante.SPEC_CAMBIO_MODELO_ENTRE_TECNOLOGIAS%>'){
         planTarifario = parent.opener.form.txtItemNewRatePlan.value;
      }else if (parent.opener.form.cmbSubCategoria.value == '<%=Constante.SPEC_POSTPAGO_PORTA%>'){
         planTarifario = parent.opener.form.txtItemRatePlan.value;
      }else if (parent.opener.form.cmbSubCategoria.value == '<%=Constante.SPEC_VENTA_MOVILES_RETAIL_POSTPAGO%>'){
          planTarifario = parent.opener.form.txtItemRatePlan.value;
      }                                                       
    //Condicion para la edicion y consulta de la orden
    }else if (parent.opener.form.cmbSubCategoria == undefined){
      if (parent.opener.form.hdnSubCategoria.value == '<%=Constante.SPEC_POSTPAGO_VENTA%>'){
         planTarifario = parent.opener.form.txtItemRatePlan.value;
      }else if (parent.opener.form.hdnSubCategoria.value == '<%=Constante.SPEC_CAMBIO_MODELO%>'){
         planTarifario = parent.opener.form.txtItemNewRatePlan.value;
      }else if (parent.opener.form.hdnSubCategoria.value == '<%=Constante.SPEC_CAMBIO_MODELO_ENTRE_TECNOLOGIAS%>'){
         planTarifario = parent.opener.form.txtItemNewRatePlan.value;
      }else if (parent.opener.form.hdnSubCategoria.value == '<%=Constante.SPEC_POSTPAGO_PORTA%>'){
         planTarifario = parent.opener.form.txtItemRatePlan.value;		 
      }else if (parent.opener.form.hdnSubCategoria.value == '<%=Constante.SPEC_VENTA_MOVILES_RETAIL_POSTPAGO%>'){
          planTarifario = parent.opener.form.txtItemRatePlan.value;
      }          
    }
  }else{
    //Condicion para la creaci�n de la orden  
    if (parent.opener.form.cmbSubCategoria != undefined) {          
      if (parent.opener.form.cmbSubCategoria.value == '<%=Constante.SPEC_POSTPAGO_VENTA%>'){
         planTarifario = parent.opener.frmdatos.txtItemRatePlan[indice].value;
      }else if (parent.opener.form.cmbSubCategoria.value == '<%=Constante.SPEC_CAMBIO_MODELO%>'){
         planTarifario = parent.opener.frmdatos.txtItemNewRatePlan[indice].value;
      }else if (parent.opener.form.cmbSubCategoria.value == '<%=Constante.SPEC_CAMBIO_MODELO_ENTRE_TECNOLOGIAS%>'){
         planTarifario = parent.opener.frmdatos.txtItemNewRatePlan[indice].value;
      }else if (parent.opener.form.cmbSubCategoria.value == '<%=Constante.SPEC_POSTPAGO_PORTA%>'){
         planTarifario = parent.opener.frmdatos.txtItemRatePlan[indice].value;		 
      }else if (parent.opener.form.cmbSubCategoria.value == '<%=Constante.SPEC_VENTA_MOVILES_RETAIL_POSTPAGO%>'){
          planTarifario = parent.opener.frmdatos.txtItemRatePlan[indice].value;
      }                                                       
    //Condicion para la edicion y consulta de la orden
    }else if (parent.opener.form.cmbSubCategoria == undefined){
      if (parent.opener.form.hdnSubCategoria.value == '<%=Constante.SPEC_POSTPAGO_VENTA%>'){
         planTarifario = parent.opener.frmdatos.txtItemRatePlan[indice].value;
      }else if (parent.opener.form.hdnSubCategoria.value == '<%=Constante.SPEC_CAMBIO_MODELO%>'){
         planTarifario = parent.opener.frmdatos.txtItemNewRatePlan[indice].value;
      }else if (parent.opener.form.hdnSubCategoria.value == '<%=Constante.SPEC_CAMBIO_MODELO_ENTRE_TECNOLOGIAS%>'){
         planTarifario = parent.opener.frmdatos.txtItemNewRatePlan[indice].value;
      }else if (parent.opener.form.hdnSubCategoria.value == '<%=Constante.SPEC_POSTPAGO_PORTA%>'){
         planTarifario = parent.opener.frmdatos.txtItemRatePlan[indice].value;		 
      }else if (parent.opener.form.hdnSubCategoria.value == '<%=Constante.SPEC_VENTA_MOVILES_RETAIL_POSTPAGO%>'){
          planTarifario = parent.opener.frmdatos.txtItemRatePlan[indice].value;
      }          
    }  
  }
  
  return planTarifario;
}

function fxMakeOrderItemVep(elementoColumna){
         this.elementoColumna     =   elementoColumna;        
}

//INICIO: PRY-0864 | AMENDEZ
function truncateNumber(decimalNumber){
    num=Math.floor(decimalNumber);
    return num
}
//FIN: PRY-0864 | AMENDEZ

</script>
	
