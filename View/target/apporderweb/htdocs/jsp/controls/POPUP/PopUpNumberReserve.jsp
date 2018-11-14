<!--Developer: RHuacani ASISTP - 24/10/2010-->
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.service.NewOrderService" %>
<%@ page import="pe.com.nextel.service.GeneralService" %>
<%@ page import="java.util.HashMap"%>
<%@page import="org.apache.commons.collections.MapUtils"%>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="java.util.ArrayList"%>
<%
String patron = request.getParameter("htx_patron");
String cantNumeros = request.getParameter("htx_cantNumeros");
String popUpType = request.getParameter("htx_popUpType");
String popUpTypeDesc="";
String codPlanId = request.getParameter("htx_planid");
String productId = request.getParameter("htx_productid"); //PBASUALDO 20101206
String solutionId = request.getParameter("htx_solutionid"); //PBASUALDO 20110621
String selectedNumbers = request.getParameter("htx_selectednumbers")==null?"":request.getParameter("htx_selectednumbers");//PBASUALDO 20101206
String selectedValues = request.getParameter("htx_selectedvalues")==null?"":request.getParameter("htx_selectedvalues");//PBASUALDO 20101206
String alreadyPickupNumbers= request.getParameter("htx_alreadypickupnumbers")==null?"":request.getParameter("htx_alreadypickupnumbers");//PBASUALDO 20110615 
GeneralService generalService = new GeneralService();
ArrayList cantNumerosList = (ArrayList)generalService.getTableList("RESNUM_CMB_VAL","1").get("arrTableList");
String strMessageInf = popUpType.equals("2")&&((Constante.SOLUTION_3G_HPPTT_POST+"").equals(solutionId)||(Constante.SOLUTION_3G_HPPTT_PRE+"").equals(solutionId))?"Debe iniciar con el código 41":"Debe iniciar con el código 51";
System.out.println("popUpType------>" + popUpType + "-" + strMessageInf );
%>

<html>
   
  <head>
    <title>Reserva de Numeros</title>
    
     <!--Imports Validate-->
     <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/np.constants.js"></script>
     <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/np.core.js"></script>
     <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/np.core.constants.js"></script> 
     
<script type="text/javascript">

var popUpType='<%=popUpType%>';

//RHUARCANI 20101117 INICIO
function fxInputTabEnter() {
		return ((window.event.keyCode == 9) || (window.event.keyCode == 13));
	 }
function fxKeySearch() {
  if(fxInputTabEnter())
  {
   fxListarNumberGolden() ; 
  }
}

//RHUARCANI 20101117 FIN
 function fxListarNumberGolden(){
    var patron         = document.getElementById('txt_SearchNumber');
    var cantNumeros    = document.getElementById('cmb_Cantidad');
    var selectedNumbers = document.getElementById("selectedOptions");
    var selectedNumbersText="";
    var selectedValuesText="";
    document.getElementById("btnSave").disabled=true;
    document.getElementById("btnSave").value="Buscando...";
    document.getElementById("btnAgregar").disabled=true;

    if(selectedNumbers.options.length>0)
    {
      for(i=0;i<selectedNumbers.options.length;i++)
      {
        selectedNumbersText+=selectedNumbers.options[i].text+";";
        selectedValuesText+=selectedNumbers.options[i].value+";";
      }
    }
    
    patron = patron.value;
    cantNumeros = cantNumeros.value;
    document.forms[0].htx_patron.value  = patron ;
    document.forms[0].htx_cantNumeros.value  = cantNumeros ;
    document.forms[0].htx_selectednumbers.value = selectedNumbersText;
    document.forms[0].htx_selectedvalues.value = selectedValuesText;
    document.forms[0].submit();
  }
  // BEGIN PBASUALDO 20101206
  function fxLoadNumberExists(){
     if (popUpType=='3') {
        fxListSelectedNumbers();
     } else if (popUpType=='2'){
        document.getElementById("availableOptions").removeAttribute("multiple");
        fxListSelectedUfmi();
     } else {
        document.getElementById("availableOptions").removeAttribute("multiple");
        fxValidateNumberChangeSelectedNumbers();
     }
      // PBASUALDO 20101207  para la validacion de asegurarse tener un solo elemento cuando NO es numeros asignados
      if(popUpType!='3'&&document.getElementById('selectedOptions').options.length > 0){		
          document.getElementById("btonAgregarOpcion").disabled=true;
      }
  }
    
  function fxListSelectedUfmi() {
    var index=0;
    var cmbSelectedOptions = document.getElementById('selectedOptions');
    var vForm = window.opener.document.frmdatos;
    var idfila= window.opener.document.getElementById("hdnFilaActual").value;
    var tablax= window.opener.document.getElementById("tableItemsPortab");
    var tbodyx=tablax.tBodies[0];
    var filas =tbodyx.rows;
    var fila =filas[idfila];
    var celda=fila.cells[3];
    var item = new Option;
    if (filas.length>2) {
      item.text = vForm.hdnUfmi[idfila-1].value;
      item.value = vForm.hdnUfmiId[idfila-1].value;
      var auxAlreadyPickUp='';
      for(i=0;i<filas.length-1;i++){
        if(vForm.hdnUfmi[i].value!='')
          auxAlreadyPickUp+="'"+vForm.hdnUfmi[i].value+"',";
      }
      if(auxAlreadyPickUp!=''){
      document.getElementById("htx_alreadypickupnumbers").value=auxAlreadyPickUp;
      }
    } else {
      item.text = window.opener.document.getElementById("hdnUfmi").value;
      item.value = window.opener.document.getElementById("hdnUfmiId").value;
      if(item.text!=''){
      document.getElementById("htx_alreadypickupnumbers").value="'"+item.text+"',";
      }
    }
    if(item.text!=''){
      cmbSelectedOptions.options.add(item); 
    }
  }

 
  function fxListSelectedNumbers()
  { 
    var index=0;
    var cmbSelectedOptions = document.getElementById('selectedOptions');
     <%
        String[] arrSelectedNumbers=new String[0];
        String[] arrSelectedNumbersValues=new String[0];
        if(!selectedNumbers.equals("")){
        arrSelectedNumbers=selectedNumbers.split(";");
        arrSelectedNumbersValues=selectedValues.split(";");
        
        for (int i = 0; i < arrSelectedNumbers.length; i++) {
          
      %>
        
        var item = new Option;        
        item.text =  '<%=arrSelectedNumbers[i]%>';
        item.value = '<%=arrSelectedNumbersValues[i]%>';
                        
        cmbSelectedOptions.options.add(item);        
      <%
           }
        }
      %>
      
      
     
    
    //INICIO validacion de numeros ya elegidos en otro item 20110615    
    var vctItemsMainAux=window.opener.parent.opener.parent.mainFrame.vctItemsMainFrameOrder;    
    var alreadyPickupNumbers="";
    for( i = 0 ; i < vctItemsMainAux.size();  i ++ ) {    
        var vctAuxRead = vctItemsMainAux.elementAt(i);
        for( j = 0 ; j < vctAuxRead.size();  j ++ ) {
            var objAuxRead = vctAuxRead.elementAt(j);
             
            if(objAuxRead.npobjitemheaderid=="'<%=Constante.ITEM_HEADER_ID_RESERVA_NUMEROS%>'")
            {
                var npobjitem=objAuxRead.npobjitemvalue.replace(/'/g,'');
                var arrayItemValueTemp=npobjitem.split(',');
                var itemValue='';
                
                if(arrayItemValueTemp.length>3) {                  
                   for (k=0; k<(arrayItemValueTemp.length/3); k++) {
                      itemValue=arrayItemValueTemp[k*3];
                      if (itemValue!=''){
                         alreadyPickupNumbers+=""+itemValue+",";
                      }
                   }
                }else{
                     itemValue=arrayItemValueTemp[0];
                     if (itemValue!=''){
                        alreadyPickupNumbers+=""+itemValue+",";
                     }
                }               
                
            }
            
        }
    }
    document.getElementById("htx_alreadypickupnumbers").value=alreadyPickupNumbers
   
    //FIN validacion de numeros ya elegidos en otro item 20110615
  }  
  //END PBASUALDO 20101206
  
  //INICIO construir lista de nuevos numeros ya elegidos
  function fxValidateNumberChangeSelectedNumbers(){
      var vctItemsMainAux=window.opener.parent.opener.parent.mainFrame.vctItemsMainFrameOrder;    
      var alreadyPickupNumbers="";
      for( i = 0 ; i < vctItemsMainAux.size();  i ++ ) {    
          var vctAuxRead = vctItemsMainAux.elementAt(i);
          for( j = 0 ; j < vctAuxRead.size();  j ++ ) {
              var objAuxRead = vctAuxRead.elementAt(j);
              
              if(objAuxRead.npobjitemheaderid=="'<%=Constante.ITEM_ID_NUEVO_NUMERO%>'")
              {                    
                  var npobjitem=objAuxRead.npobjitemvalue.substr(1);
                  npobjitem=npobjitem.substr(0,npobjitem.length-1);
                  alreadyPickupNumbers+=""+npobjitem+",";
              }
              
          }
      }
      
     document.getElementById("htx_alreadypickupnumbers").value=alreadyPickupNumbers;
  }
  
  //FIN construir lista de nuevos numeros ya elegidos

 function fxClose(){
    parent.close();
  }


function fxAttributeDelete(){
	var selectedList = document.getElementById("selectedOptions");
	var availableList = document.getElementById("availableOptions");
    var selIndex = selectedList.selectedIndex;
    if(selIndex < 0)
        return;
		
    ////Inicio RHuacani ASISTP: Permite que la seleccion por bloques y el traspaso de opciones entre listas  funcione bien.
	var arrSelecionado = new Array();
		while (selectedList.selectedIndex != -1) {
				if (selectedList.selectedIndex >= 0)
					arrSelecionado.push(selectedList.options[selectedList.selectedIndex]);
			//Deselecciona la actual opcion marcada, para continuar el bucle.		
			selectedList.options[selectedList.selectedIndex].selected = false; 
		}
		var tamano = arrSelecionado.length -1;
		for(i=tamano; i>=0; i--){
				availableList.appendChild(arrSelecionado[i]);
		}
	////Fin RHuacani ASISTP.
    fxSelectNone(selectedList,availableList);
    fxSetSize(availableList,selectedList);
    
  //Activamos botones si no hay nada seleccionado PBASUALDO		
    if(selectedList.options.length == 0){		
      document.getElementById("btonAgregarOpcion").disabled=false;
	 	}
    
}
function fxAttributeAdd(){
 if(!document.getElementById("btonAgregarOpcion").disabled){//PBASUALDO
	var availableList = document.getElementById("availableOptions");
	var selectedList = document.getElementById("selectedOptions");
    var addIndex = availableList.selectedIndex;
    if(addIndex < 0)
        return;	
	////Inicio RHuacani ASISTP.
	var arrSelecionado = new Array();
		while (availableList.selectedIndex != -1) {
				if (availableList.selectedIndex >= 0)
					arrSelecionado.push(availableList.options[availableList.selectedIndex]);
					
			if(document.getElementById("availableOptions").multiple)
			availableList.options[availableList.selectedIndex].selected = false;
      else
      availableList.selectedIndex=-1;
		}
		var tamano = arrSelecionado.length -1;
		for(i=tamano; i>=0; i--){
				selectedList.appendChild(arrSelecionado[i]);
		}
	////Fin RHuacani ASISTP.

    fxSelectNone(selectedList,availableList);
    fxSetSize(selectedList,availableList);
    	//Desactivamos botones 
    if(popUpType!='3'){
      document.getElementById("btonAgregarOpcion").disabled=true;
    }
   }//PBASUALDO
}
function fxAllDelete(){
    var len = document.getElementById("selectedOptions").length -1;
    for(i=len; i>=0; i--){
        document.getElementById("availableOptions").appendChild(document.getElementById("selectedOptions").item(i));
    }
    fxSelectNone(document.getElementById("selectedOptions"),document.getElementById("availableOptions"));
    fxSetSize(document.getElementById("selectedOptions"),document.getElementById("availableOptions"));
    
}
function fxAllAdd(){
    var len = document.getElementById("availableOptions").length -1;
    for(i=len; i>=0; i--){
        document.getElementById("selectedOptions").appendChild(document.getElementById("availableOptions").item(i));
    }
    fxSelectNone(document.getElementById("selectedOptions"),document.getElementById("availableOptions"));
    fxSetSize(document.getElementById("selectedOptions"),document.getElementById("availableOptions"));
}
function fxSelectNone(list1,list2){
    list1.selectedIndex = -1;
    list2.selectedIndex = -1;
}
function fxSetSize(list1,list2){
    list1.size = fxGetSize(list1);
    list2.size = fxGetSize(list2);
}
function fxGetSize(list){

    var len = list.childNodes.length;
    var nsLen = 0;

    for(i=0; i<len; i++){
        if(list.childNodes.item(i).nodeType==1)
            nsLen++;
    }
    if(nsLen<2)
        return 2;
    else
        return nsLen;
}


function fxSelectedSave(){

    var listaPadre=null;
    if(popUpType=='3'){
    listaPadre = window.opener.document.getElementById("cmb_ItemAssignedNumber");
    }
    
    var optionList = document.getElementById("selectedOptions").options;
    var len = optionList.length;
	
    if(popUpType=='1')
    {
      if(len > 0){
        window.opener.document.getElementById("txt_ItemNewNumber").value= optionList[0].text; 
        var argPtnId = optionList[0].value.split(',');
        window.opener.document.getElementById("hdnPtnId").value= argPtnId[0]; 
      }    
    }else if(popUpType=='2')
    {
      if(len > 0){
        window.opener.fxRowUpdate(optionList[0]); //PBASUALDO 20110118
      }    
    }else
    {
      // Eliminamos las opciones de la lista de la ventana padre.	
      for(i=listaPadre.length-1; i>=0; i--){
         listaPadre.remove(i);		 
      }      
      // Agregamos las opciones elegidas en la lista de la ventana hija.
      for(i=0; i<len; i++){    
        var opcionx 	  = window.opener.document.createElement("option");
        var opcionz		  = optionList.item(i);
        opcionx.value	  = opcionz.value;
        opcionx.innerHTML = "<option value='"+opcionz.value+"' >"+opcionz.text+"</option>";
        // alert('value: ' + opcionz.value + ' text: ' + opcionz.text);
        listaPadre.appendChild(opcionx);
      }    
    }
    
    window.close();
    window.opener.focus();    
}

window.onload=function(){
            NP.ResourceType.Formatting.formatPTN("txt_SearchNumber");
          }

function fxSearch(){
      var ptrn = '<%= patron%>';
      var varItemCiontac = document.getElementById('availableOptions');
      var indice = 0;      
      if(ptrn != "null" && ptrn != ''){
        
        <%
          long ldnType = 0;
          long lnpCode = 0;
          long ltmCode = 0;
          String sPortabilidad=null;
          String sQuantity = cantNumeros;
          try{ltmCode=Long.parseLong(codPlanId);}catch(Exception ex){}
          
          if(popUpType.equals("1"))
          {
            ldnType=Long.parseLong(Constante.GOLDEN_NUMBERS);
            lnpCode=Long.parseLong(Constante.SEARCH_PTN);
            //ltmCode=Long.parseLong(Constante.TT_TMCODE);
            //BEGIN PBASUALDO 20101206
            popUpTypeDesc="Reserva de Numero";
            if(productId.equals(Constante.PRODUCT_NUMBER_CHANGE_RIPLEY))
            {
              ldnType=Long.parseLong(Constante.ELQUELLAMAPAGA_NUMBERS);
            }
            //END PBASUALDO 20101206
          }else if(popUpType.equals("2"))
          {
            if((Constante.SOLUTION_3G_HPPTT_POST+"").equals(solutionId)||(Constante.SOLUTION_3G_HPPTT_PRE+"").equals(solutionId)){
              ldnType=Long.parseLong(Constante.GOLDEN_NUMBERS);
            }else if((Constante.SOLUCION_2G_POST+"").equals(solutionId)||(Constante.SOLUCION_2G_PRE+"").equals(solutionId)){
              ldnType=Long.parseLong(Constante.OTHER_NUMBERS);
            }
            lnpCode=Long.parseLong(Constante.SEARCH_UFMI);
            sPortabilidad = "P";
            //ltmCode=Long.parseLong(Constante.TT_TMCODE);
            popUpTypeDesc="Radios Disponibles";
          }else
          {
            ldnType=Long.parseLong(Constante.GOLDEN_NUMBERS);
            lnpCode=Long.parseLong(Constante.SEARCH_PTN);
            //ltmCode=Long.parseLong(Constante.TT_TMCODE);
            popUpTypeDesc="Números Disponibles";
          }
          
        
           String strMessage ="";
          HashMap objHashMap=null;
          if(!patron.equals("-1")){
            NewOrderService newOrderService = new NewOrderService();
            System.out.println("PopUpNumberReserve.jsp.....: " + alreadyPickupNumbers);
            if (alreadyPickupNumbers!=null && alreadyPickupNumbers.length()>0 && alreadyPickupNumbers.endsWith(",")) {
              alreadyPickupNumbers = alreadyPickupNumbers.substring(0,alreadyPickupNumbers.length()-1);
              System.out.println("PopUpNumberReserve4.jsp.....: " + alreadyPickupNumbers);
            }
            objHashMap = (HashMap) newOrderService.getNumberGolden(null,ldnType,lnpCode,patron,ltmCode, alreadyPickupNumbers, sQuantity, sPortabilidad);
            strMessage = (String)MapUtils.getString(objHashMap, Constante.MESSAGE_OUTPUT);
          }else
          {
            System.out.println("segmento ejecutado solo la primera vez");
            %>
            var cmbSelectedOptions = document.getElementById('selectedOptions');
            var listaPadre=null;
            var index=cmbSelectedOptions.length;
            if(popUpType=='3'){
              listaPadre = window.opener.document.getElementById("cmb_ItemAssignedNumber");
        
              for(i=0;i<listaPadre.length; i++){
                var exists=false;
                for(j=0;j<cmbSelectedOptions.length;j++)
                {
                  if(listaPadre.options[i].text==cmbSelectedOptions.options[j].text)
                    exists=true;
                }
                if(!exists)
                {
                  var item = new Option;        
                  item.text =  listaPadre.options[i].text;
                  item.value = listaPadre.options[i].value;
                  cmbSelectedOptions.options.add(item);
                }
              }
            }
            <%
            
          }
        %>
     var x = '<%= (strMessage!=null)?strMessage.replaceAll("\n",""):"" %>';
      <%
           int size = 0;
           if (!MiUtil.isNotNull(strMessage)){
              ArrayList recoveryList = (ArrayList) MapUtils.getObject(objHashMap, "objArrayList");

              if (recoveryList != null) 
                size = recoveryList.size();
              else size=-1;
      %>
      
      <%
              if (recoveryList != null){ 
              for (int i = 0; i < recoveryList.size(); i++) {
                Object[] objList = (Object[]) recoveryList.get(i);
                String compareNumber=(!popUpType.equals("2"))?objList[3].toString():objList[0].toString();
                compareNumber=compareNumber.substring((!popUpType.equals("2"))?2:3,compareNumber.length());
                boolean exists=false; 
                for (int j = 0; j < arrSelectedNumbers.length; j++)
                  if(compareNumber.equals(arrSelectedNumbers[j]))
                      exists=true;
                    
                if(!exists){
        %>
        
        var item = new Option;
        var temp_item = '<%=(!popUpType.equals("2"))?objList[3]:objList[0]%>';
        var temp_item_value = '<%=objList[4]+","+objList[1]%>'; //PBASUALDO 20110118
        item.text =  temp_item.substring(<%=(!popUpType.equals("2"))?"0":"0"%>,temp_item.length);
        item.value = temp_item_value;

        varItemCiontac.options.add(item);
        document.getElementById('txt_SearchNumber').value = ptrn;
        
        <%    }
          }
           }
           }
        %>
               
      }
      
}

</script>
  </head>
<link type="text/css" rel="stylesheet"
          href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"/>
    <style type="CDATA">
        .show   { display:inline}
        .hidden { display:none }
    </style>
  <body onload="fxSearch();fxLoadNumberExists();"> <!--// PBASUALDO 20101206 -->

<form name="frmOrderNumber" action="PopUpNumberReserve.jsp" onsubmit="return false;" method="GET"> 

<input type="hidden" name="htx_patron" id="htx_patron" />
<input type="hidden" name="htx_cantNumeros" id="htx_cantNumeros" />
<input type="hidden" name="htx_popUpType" id="htx_popUpType" value="<%=popUpType%>" />
<input type="hidden" name="htx_planid" id="htx_planid" value="<%=codPlanId%>" />
<input type="hidden" name="htx_productid" id="htx_productid" value="<%=productId%>" /> <!-- PBASUALDO 20101206 -->
<input type="hidden" name="htx_selectednumbers" id="htx_selectednumbers" value="<%=selectedNumbers%>" /> <!-- PBASUALDO 20101206 -->
<input type="hidden" name="htx_selectedvalues" id="htx_selectedvalues" value="<%=selectedValues%>" /> <!-- PBASUALDO 20101206 -->
<input type="hidden" name="htx_alreadypickupnumbers" id="htx_alreadypickupnumbers" value="<%=alreadyPickupNumbers%>" /> <!-- PBASUALDO 20110615 -->
<input type="hidden" name="htx_solutionid" id="htx_solutionid" value="<%=solutionId%>" /> <!-- PBASUALDO 20110621 -->
<td  class="" colspan="2"  align="left">

         <!--Linea de separación -->
         <table><tr align="center"><td></td></tr></table>
         <div id="services">
            
            <!-- Servicios Disponibles y Seleccionados -->
            <div id="tableServiciosAdicionales">
    
    
  <table border="0" cellspacing="0" cellpadding="0" id="tbCabeceraServiciosAdicionales">        
    <tr class="PortletHeaderColor">
    <td class="CellLabel" align="left" colspan="4">
           Ingrese un patrón para la búsqueda.&nbsp;&nbsp;<%=strMessageInf%>
      </td>
    </tr>
    <tr>
    <td align="left" colspan="4">
           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      </td>
    </tr>
    <tr class="PortletHeaderColor">
      <td class="SubSectionTitle" align="left" >
           &nbsp;&nbsp;&nbsp;Numero&nbsp;&nbsp;&nbsp;
      </td>
      <td class="CellContent">
          &nbsp;<input type="text" name="txt_SearchNumber" value="" size="20" maxlength="11" onKeyPress="return NP.ResourceType.CharFilter.<%=(!popUpType.equals("2"))?"filterPTN":"filterUFMI"%>('txt_SearchNumber',event);" onKeydown="fxKeySearch();" >
          &nbsp;
      </td>
      <td class="SubSectionTitle" align="left" >
           &nbsp;&nbsp;&nbsp;Cantidad a Buscar
      </td>
      <td class="CellContent">
          &nbsp;<select name="cmb_Cantidad">
           <%
          if(cantNumerosList!=null && cantNumerosList.size()>0 ){
            for(int i=0;i<cantNumerosList.size();i++){%>
            <option value='<%=((HashMap)cantNumerosList.get(i)).get("wv_npValue")%>' <%if (((HashMap)cantNumerosList.get(i)).get("wv_npValue").toString().equals(cantNumeros)) {%> selected <%}%>>
                         <%=((HashMap)cantNumerosList.get(i)).get("wv_npValueDesc")%>
           </option>
            
           <% }
          }
        %>
          </select>
          &nbsp;
        <input type="button" name="btnSave" onclick="javascript:fxListarNumberGolden()" value="Buscar" >
             
        
      </td>
    </tr>
    <tr></tr>
    <tr></tr>
    <tr></tr>
    <tr></tr>
    <tr></tr>
    <tr></tr>
    <tr></tr>
    <tr></tr>
    
  </table>
            
            <table border="0" cellspacing="0" cellpadding="0" id="tbCabeceraServiciosAdicionales">
              <tr class="PortletHeaderColor">
                 <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="16"></td>
                 <td class="SubSectionTitle" align="left" valign="top"><%=popUpTypeDesc%></td>
                 <td class="SubSectionTitleRightCurve" valign="top" align="right" width="12"></td>
              </tr>
            </table>
            <table border="0" width="100%" cellpadding="2" cellspacing="0" >
               <tr align="center">
                  <td  valign="top">
                     <table border="0" cellspacing="0" cellpadding="0" width="100%">
                        <tr >
                           <td class="CellLabel" align="center">Disponibles</td>
                           <td class="CellLabel"></td>
                           <td class="CellLabel" align="center"><%=(!popUpType.equals("3"))?"Seleccionados":"Reservados"%></td>
                           <td class="CellLabel"> <div id="dServiceDetail"></div>
                           </td>
                        </tr>
                        
                        </tr>
                        <tr>
                           <td class="CellContent" align="center">
                              <select id="availableOptions" name="availableOptions" style="height:155px" style="width:100px" size="12" multiple ondblclick="javascript:fxAttributeAdd();">
                              
                              </select>
                           <input type="hidden" value="" name="item_services">
                           </td>
                           <td class="CellContent" align="center">
                              <table border ="0">
                              <tr><input type="button" id="btonAgregarOpcion" value=" > " name="EnviaService" onclick="javascript:fxAttributeAdd();"></tr>
                              
                              <tr><input type="button" value=" < " name="RecibeService" onclick="javascript:fxAttributeDelete();"></tr>
                              <input type="hidden" name="hdnFlagServicio" value="0"> <!-- Almacena el Flag de modificacion de los Servicios de Items -->
                              <br>
                              
                              <%                              
                                if(popUpType.equals("3"))
                                {
                              %>
                              
                              <tr><input type="button" value=">>" name="EnviaAllService" onclick="javascript:fxAllAdd();"></tr>                              
                              <tr><input type="button" value="<<" name="RecibeAllService" onclick="javascript:fxAllDelete();"></tr>
                              <br>
                              <%
                                }
                              %> 
                              
                              </table>
                           </td>                                                            
                           <td class="CellContent" align="center" colspan="2">
                              <select id="selectedOptions" name="selectedOptions" style="height:155px" style="width:100px" size="10" ondblclick="javascript:fxAttributeDelete();"  multiple style="font-family: Courier New; font-size: 9 pt;" >
                              
                              </select>
                           </td>
                        </tr>
                     </table>
                  </td>
               </tr>
            </table>
            </div>
         </div>   <!-- de " div Servicios" -->    

</td>

<table><tr align="center"><td></td></tr></table>
<table border="0" cellspacing="0" cellpadding="0" align="center">
  <tr>              
    <td align="center"><input type="button" name="btnAgregar"  value=" Agregar " onclick="javascript:fxSelectedSave();">&nbsp;&nbsp;&nbsp;</td>
    <td align="center"><input type="reset"  name="btnCerrar"   value=" Cerrar "  onclick="javascript:fxClose();"></td>
  </tr>
</table>

</form>
</body>
</html>
