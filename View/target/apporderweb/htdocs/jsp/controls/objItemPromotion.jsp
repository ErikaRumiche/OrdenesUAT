<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="pe.com.nextel.util.*" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.bean.*"%>
<%@ page import="pe.com.nextel.bean.NpObjHeaderSpecgrp" %>
<%@ page import="pe.com.nextel.service.ItemService"%>
<%@ page import="pe.com.nextel.service.*"%>
<%
try{  
   
   ArrayList objItemHeaderAux = new ArrayList();   
   HashMap objScreenField=null;
   boolean   blnSA          = false;
   boolean   blnIMEI        = false;
   String    strSA          = "";
   String    strIMEI        = "";
   
   String    strMessage         = "",
             strResult          = "",
             strResultCategory  = "";
   
   String    strDivisionId  = (String)request.getAttribute("strDivisionId");
   
   objItemHeaderAux       = (ArrayList)request.getAttribute("objItemHEader");
   objScreenField         = (HashMap)request.getAttribute("objScreenField");
   String strPageSource   = (String)request.getAttribute("pageSource");   
   String strCategoryId   = (String)request.getAttribute("CategoriaId");
   
   String strOrderId =(request.getParameter("an_nporderid")==null?"0":request.getParameter("an_nporderid"));
  
   //Llamada al service
   AutomatizacionService objAutomatService = new AutomatizacionService();
   HashMap hshAutomat = new HashMap();
    
   //LLamada al metodo validacion de inbox.
   if(!strOrderId.equals("0")){
     
       try{
         hshAutomat        = objAutomatService.getOrderSpecificationInbox(Long.parseLong(strOrderId));
         strMessage        = (String)hshAutomat.get("strMessage");
         strResult         = (String)hshAutomat.get("strResult");
         strResultCategory = (String)hshAutomat.get("strResultCategory");
         
       }catch(Exception e) {
        throw new Exception("[objItemImeiService.jsp][objAutomatService.getOrderSpecificationInbox]= "+e.getMessage());
       }
       
       if (strMessage!=null){
        throw new Exception("[objItemImeiService.jsp][objAutomatService.getOrderSpecificationInbox]= "+strMessage);    
       }
   }
   
   System.out.println("<----------[objItemImeiService.jsp]------------->");
   System.out.println("OrdenId ===> "+strOrderId);
   System.out.println("strCategoryId ===> "+strCategoryId);
   System.out.println("Mens_getOrderSpecificationInbox      ===> "+strMessage);
   System.out.println("strResult_getOrderSpecificationInbox ===> "+strResult);
   System.out.println("strResultCategory_getOrderSpecificationInbox ===> "+strResultCategory);
   System.out.println("<----------[objItemImeiService.jsp]------------->");
   
   
   //Fin del metodo Validacion de Inbox
   request.removeAttribute("objItemHEader");
   request.removeAttribute("objScreenField");
   
 
   if( objItemHeaderAux != null && objItemHeaderAux.size() > 0 ){
      NpObjHeaderSpecgrp objnpObjHeaderSpecgrp = null;
      for ( int i=0 ; i<objItemHeaderAux.size(); i++ ){
         String valueVisible = "";
         objnpObjHeaderSpecgrp = new NpObjHeaderSpecgrp();
         objnpObjHeaderSpecgrp = (NpObjHeaderSpecgrp)objItemHeaderAux.get(i);
         
         if( objnpObjHeaderSpecgrp.getNpobjitemheaderid()==25 )
            blnSA   = true;
         if( objnpObjHeaderSpecgrp.getNpobjitemheaderid()==72 )
            blnIMEI = false;
      }
   }
  
%>
<!-- Inicio IMEIS -->
<script defer>
  function fxInputTabEnter() {
		return ((window.event.keyCode == 9) || (window.event.keyCode == 13));
	 }

	function fxValidateItemDevice(objImei) {
    vForm = document.frmdatos;
		if(fxInputTabEnter()) {
      if(eval(vForm.txtImeis.readOnly == true)== false){
        fxValidateImeiBD(objImei);
      }else{
        alert("No se puede ingresar IMEI");
        return;      
      }
		}
	}

  
  function fxValidateSimBD(objSim) {
   try{
     vForm = document.frmdatos;
     if (vForm.hdnIndice_imei != null){
        fxCargaSimOrders(objSim);
     }
     else{
        alert("No puede agregar SIM");
        return false;
     }
   }catch(exception) {
			alert("[objItemImeiService.jsp:fxValidateSimBD]= "+exception.description);
		}  
  }
  	
    
  function fxGetSelectIndexTableIMEIS(){
    var vForm = document.frmdatos;
    var cantRowsIMEIS =  table_imeis.rows.length;
    //alert("cantRowsIMEIS : " + cantRowsIMEIS)
      if( cantRowsIMEIS == 2 ){
          return 1;
      }else{
      //alert("Entramos al ELSE : " + vForm.hdnIndice_imei.length )
        for(i=0; i<vForm.hdnIndice_imei.length; i++){
        //alert("Dentro IF : " + (i+1) + " - > " + vForm.item_imei_radio[i].checked )
          if( vForm.item_imei_radio[i].checked )
            return i+1;
        }
      }
  }

	
	function fxValidaSimRepetido(objNuevoSim) {
		vForm = document.frmdatos;
		try {
			cantSims = vForm.item_imei_sim.length;
			if(cantSims > 1) {
				for(a = 0; a < cantSims; a++) {
					oldSim = vForm.item_imei_sim[a].value
					if(oldSim != "" && oldSim == objNuevoSim.value) {
						alert("SIM ya está registrado, verifique");
						objNuevoSim.focus();
						return false;
					}
				}
			}
		} catch(exception) {
			alert("[objItemImeiService.jsp:fxValidaSimRepetido]= "+exception.description);
		}
		return true;
	}
  
   //Function que direcciona a una ventana popUpShowError
   function fxShowErrorImeiSim(error, itid, itdev){
      var errorEstOp   = error;
      var itemId       = itid;
      var itemDeviceId = itdev;
      
    	   var winUrl = "<%=request.getContextPath()%>/DYNAMICSECTION/DynamicSectionItems/DynamicSectionItemsPage/PopUpErrorOperationImeiSim.jsp?"+
         "strOrderId="+<%=strOrderId%>+"&strItemId="+itemId+"&strItemDeviceId="+itemDeviceId;
         window.open(winUrl,"Error_Operación_IMEI_SIM","toolbar=no,location=0,modal=yes,titlebar=0,directories=no,status=yes,menubar=0,scrollbars=no,screenX=100,top=350,left=500,screenY=80,width=250,height=250");
        
   }
   
   function fxContaDigitos(valor,i,j,itid,itdev){
      
      var v_form = document.frmdatos;
      var strAppId = v_form.strAppId.value;
      var strPhoneNumber = valor;
      if(valor == ""){
        return;
      }      
      form.myaction.value='validatePhoneNumber'; 
      var url = "<%=request.getContextPath()%>/editordersevlet?phoneNumber="+strPhoneNumber+
      "&strAppId="+strAppId+"&strOrderId=<%=strOrderId%>"+"&strval_i="+i+"&strval_j="+j+"&strItemId="+itid+
      "&strItemDeviceId="+itdev;
      form.action = url;             
      form.submit();
      
   }

</script>



<table width="93%">
  <tr>      
    <td width="50%"> 
      <div id="divServicios" >
      <%if(blnSA){%>
         <table class="RegionBorder">
               <tr>
                  <td class="CellLabel" align="center">
                     &nbsp;&nbsp;Promociones por Item&nbsp;&nbsp;
                  </td>
               </tr>
               <tr>
                  <td class="CellLabel" align="center">
                  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Servicio&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;Act.&nbsp;|&nbsp;Modf.&nbsp;|&nbsp;&nbsp;Días&nbsp;&nbsp;|&nbsp;Orden Desact.&nbsp;
                  </td>
               </tr>
               <tr>
                  <td class="CellContent" align="center">
                     <select name="cmbSelectedPromotion" size="15" multiple style="font-family: Courier New; font-size: 9 pt;" >
                        <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
                     </select>
                     <input type="hidden" name="hdnItemSelectPromotion">
                  </td>
               </tr>
         </table>
      </div>   
      <%}%>
    </td>      
    <td width="50%"> 
    </td>
   </tr>
</table>

<br>
   <script DEFER>
                     
      var wn_items_imeis = 0;
      var wb_existItem_imeis = false;
      var glastKey=0;
      form = document.frmdatos;

      function fxAddRowTableImeis(tableID,auxVctItemsImei,indiceImei,indInsertImei) {
        
        var index_arg;
        var aux_estado;
        var table = document.all ? document.all[tableID]:document.getElementById(tableID);
        var elemText;
        if (arguments.length > 1) {/* los argumentos pasados.. a partir del segundo. */
            //var row = table.insertRow(-1);
            var row = table.insertRow(indInsertImei);
            row.id  = auxVctItemsImei.objImeiId;
            //alert(""+row.id);
            
            if (document.all) {
               for (index_arg = 1; index_arg<=indiceImei; index_arg++) {
               var cell = row.insertCell(index_arg - 1);
               elemText="";
               
               switch (index_arg){
                  case 1:     /*celda 0*/
                     elemText =  "<input type='radio' name='item_imei_radio' onclick='javascript:SeteaItemIndex(this)'>"+
                                 "<input type='text'  name='item_imei_num' value='"+ auxVctItemsImei.objImeiNum +"' size='2' readonly>"+                                 
                                 "<input type='hidden' name='hdnIndice_imei' value='"+ auxVctItemsImei.objImeiId +"' size='2'>"+
                                 "<input type='hidden' name='hdnImeiChange' value='N'>"+
                                 "<input type='hidden' name='hdnSimChange'  value='N'>"+
                                 "<input type='hidden' name='item_imei_contract_number' value='"+ auxVctItemsImei.objContractNumber +"'>"+
                                 "<input type='hidden' name='item_imei_request_id' value='"+ auxVctItemsImei.objRequestId +"'>";
                               										
                     break;
                  case 2:     /*celda 1*/
                     elemText =  "<div id='divImeis'>"+
                                 "<input type='text' name='item_imei_imei' value='"+ auxVctItemsImei.objImei +"' style='text-align:center' size='15' readonly>"+
                                 "<div id='producto'>";
                     break;
                  case 3:     /*celda 3*/
                     elemText =  "<div id='divImeis'>"+
                                 "<input type='text' name='item_imei_sim' value='"+ auxVctItemsImei.objSim +"' style='text-align:center' size='20' readonly>"+
                                 "<div id='producto'>";
                     break;
                  case 4:     /*celda 4*/
                  
                     elemText = "<input type='text' name='item_imei_bad' value='"+ auxVctItemsImei.objBad +"' style='text-align:center' size='15' readonly>"+
                                "<input type='hidden' name='item_imei_modality' value='"+ auxVctItemsImei.objModality +"' >";
                     break;
                  case 5:     /*celda 5*/
                     elemText =  "<input type='text' name='item_imei_producto' value='"+ auxVctItemsImei.objProduct +"' size='20' readonly>"+
                                 "<input type='hidden' name='item_imei_producto_id' value='"+ auxVctItemsImei.objProductId +"' >"+
											"<input type='hidden' name='item_imei_producto_warrant' value='"+ auxVctItemsImei.objWarrant +"' >";
                     break;
                  case 6:     /*celda 6*/
                     elemText = "<input type='text' name='item_imei_plan' value='"+ auxVctItemsImei.objPlan +"' size='15' readonly>";
                     break;
                  case 7:     /*celda 7*/
                     elemText = "<input type='text' name='item_imei_check' value='" + auxVctItemsImei.objCheck + "'  style='text-align:center' size='1' readonly>";
                     break;         
                  default:
               }  /* end_switch */
               
               if (auxVctItemsImei.objErrorEstOp == 'S')
                 aux_estado = "<%=Constante.OPERATION_STATUS_ERROR%>";
               else if (auxVctItemsImei.objErrorEstOp == 'N') 
                 aux_estado = "<%=Constante.OPERATION_STATUS_OK%>";
               else if(auxVctItemsImei.objErrorEstOp == 'I') 
               aux_estado = "<%=Constante.OPERATION_STATUS_INCOMPLETO%>";
               else if(auxVctItemsImei.objErrorEstOp == '' || auxVctItemsImei.objErrorEstOp == null )
                 aux_estado = "";
                 
               var valorini = auxVctItemsImei.objImeiNum.indexOf('-');
               var i = eval(auxVctItemsImei.objImeiId); //eval(auxVctItemsImei.objImeiNum.substring(0,1));  
               var j = eval(auxVctItemsImei.objImeiNum.substring(valorini+1,auxVctItemsImei.objImeiNum.length));//eval(auxVctItemsImei.objImeiNum.substring(2,3));    
              
               //Valido la Categoria 2001,2002 -> Ventas Móviles. y prepago tde. y reposicion prepago tde TDECONV034
               <% if (  "2001".equals(strResultCategory) ||
                        "2002".equals(strResultCategory) ||
                        (String.valueOf(Constante.SPEC_PREPAGO_TDE)).equals(strResultCategory)||
                        (String.valueOf(Constante.SPEC_REPOSICION_PREPAGO_TDE)).equals(strResultCategory))  
                { %> 
                 
                   //input muestra el item_imei_numTel(Número Teléfono)                 
                   if(index_arg == 8 ){
                      var numTelefono = auxVctItemsImei.objNumTel==undefined?"":auxVctItemsImei.objNumTel;
                      var itid = eval(auxVctItemsImei.objImeiItem==undefined?"":auxVctItemsImei.objImeiItem);
                      var itdev = eval(auxVctItemsImei.objItemDeviceId==undefined?"":auxVctItemsImei.objItemDeviceId);
                      var readonly = "";

                      if (itid==undefined || itdev==undefined){
                        readonly = "readonly";
                      }                      
                      elemText = "<input type='text' id='item_imei_numTel_"+i+"_"+j+"' name='item_imei_numTel_"+i+"_"+j+"' value='" + numTelefono + "'  style='text-align:center' size='15' maxlength='9' onKeyPress='return fxOnlyNumber(event);' onChange='fxContaDigitos(this.value,"+i+","+j+","+itid+","+itdev+");' "+readonly+">"
                                  +"<input type='hidden' name='hdnNumTel'  value='" + numTelefono + "'>";
                    }


               <%}%>
               
               //input muestra el error_Imei_Sim(Estado Operacion)  
               
               <% if (( "2001".equals(strResultCategory) ||
                         "2002".equals(strResultCategory) ||
                         (String.valueOf(Constante.SPEC_PREPAGO_TDE)).equals(strResultCategory) ||
                         (String.valueOf(Constante.SPEC_REPOSICION_PREPAGO_TDE)).equals(strResultCategory))&&
                         "s".equals(strResult) )
                  { %>   
                  
                    if(index_arg == 9 ){
                      
                        if(aux_estado==""){                        
                        elemText = "<input type='text' name='item_imei_errorEstOp' value='" + aux_estado + "' style='text-align:center' size='15' readonly>";
                        }else if(aux_estado == "<%=Constante.OPERATION_STATUS_ERROR%>"){
                        elemText = "<input type='text' name='item_imei_errorEstOp' value='" + aux_estado + "' style='color:red' style='text-decoration:underline' style='text-align:center'"+
                        "size='15' onclick='fxShowErrorImeiSim(this.value,"+auxVctItemsImei.objImeiItem+","+auxVctItemsImei.objItemDeviceId+");' readonly>";
                        }else if(aux_estado == "<%=Constante.OPERATION_STATUS_INCOMPLETO%>"){
                        elemText = "<input type='text' name='item_imei_errorEstOp' value='" + aux_estado + "' style='color:red' style='text-decoration:underline' style='text-align:center'"+
                        "size='15' onclick='fxShowErrorImeiSim(this.value,"+auxVctItemsImei.objImeiItem+","+auxVctItemsImei.objItemDeviceId+");' readonly>";
                        }else if(aux_estado == "<%=Constante.OPERATION_STATUS_OK%>"){
                        elemText = "<input type='text' name='item_imei_errorEstOp' value='" + aux_estado + "' style='color:green' style='text-align:center' size='15' readonly>";
                        }
                    }
                 
               <%}%>
              
               //Valido la Categoria 2009 -> Cambio de Modelo.
               
               <% if ( "2009".equals(strResultCategory) &&
                       "s".equals(strResult) )
                  { %> 
                    
                    //input muestra el error_Imei_Sim(Estado Operacion)
                    if(index_arg == 8  ){
                        
                        if(aux_estado==""){                        
                        elemText = "<input type='text' name='item_imei_errorEstOp' value='" + aux_estado + "' style='text-align:center' size='15' readonly>";
                        }else if(aux_estado == "<%=Constante.OPERATION_STATUS_ERROR%>"){
                        elemText = "<input type='text' name='item_imei_errorEstOp' value='" + aux_estado + "' style='color:red' style='text-decoration:underline' style='text-align:center'"+
                        "size='15' onclick='fxShowErrorImeiSim(this.value,"+auxVctItemsImei.objImeiItem+","+auxVctItemsImei.objItemDeviceId+");' readonly>";
                        }else if(aux_estado == "<%=Constante.OPERATION_STATUS_INCOMPLETO%>"){
                        elemText = "<input type='text' name='item_imei_errorEstOp' value='" + aux_estado + "' style='color:red' style='text-decoration:underline' style='text-align:center'"+
                        "size='15' onclick='fxShowErrorImeiSim(this.value,"+auxVctItemsImei.objImeiItem+","+auxVctItemsImei.objItemDeviceId+");' readonly>";
                        }else if(aux_estado == "<%=Constante.OPERATION_STATUS_OK%>"){
                        elemText = "<input type='text' name='item_imei_errorEstOp' value='" + aux_estado + "' style='color:green' style='text-align:center' size='15' readonly>";
                        }
                    }
                    
               <%}%>
               
               
               //alert("elemText  : " + elemText);
               cell.innerHTML = elemText;
             } /* end_for */
            }  /* end_if */
            wn_items_imeis =(table.rows.length -1); /* numero de items  */
            if (wn_items_imeis>1){
               wb_existItem_imeis =true; 
            }
            //alert("Entramos 2")
            /*  Ubicamos el foco en el primer item vacio de la tabla  */
            if ((table.rows.length - 1) == 1){
               form.item_imei_radio.checked = true;
               form.hdn_item_imei_selecc.value = 0;  
            }
            else{
               for (m=0; m<(table.rows.length - 1); m++){
                  if (form.item_imei_imei[m].value == ""){
                     form.item_imei_radio[m].checked = true;
                     form.hdn_item_imei_selecc.value = m;
                     break; 
                  }
               }
            }/*fin del if foco item vacio en tabla*/
         }/*fin del if de argumetos pasados*/
        
        
      }/*Fin Function-->fxAddRowTableImeis*/

      var item_index_imei=0;
      /*  Determina cual es el item del IMEI con el que se va a trabajar  */     
     
  
      
  function fxDeleteImeiTable(itemId){

    //try{
      /*Eliminar los IMEIS*/     
     countImeis = table_imeis.rows.length;

     for( x = 0,u = 0; x <countImeis; u++){
        var rowTable = table_imeis.rows[x];
        if( itemId == rowTable.id ){
            table_imeis.deleteRow(x);
            countImeis = table_imeis.rows.length;
            continue;
        }else{
          x++;
        }
     }
    //}catch(e){}
     
  }
  
  
  
</script>

   </script>
  <!--  Generamos una tabla oculta para almacenar los imeis y para su posterior copia de la tabla  -->
  <!--  Sirve para la funcion: ImeiListaImies  -->
  <div id="divDataImeis">
    <table id="tableImeisAux" width="99%">
    </table>
  </div>
  <!-- Se oculta la tabla de Imeis Auxiliar -->
<script defer>
    divDataImeis.className = "hidden";
	//Esto solamente es para manejar el readonly del IMEI / SIM
	if(document.frmdatos.txtImeis!=null && document.frmdatos.txtSims!=null) {
		fxSetImeiSimReadOnly(true);
		<%	String pageSource = MiUtil.defaultString(request.getAttribute("pageSource"),"");
			if(pageSource.equals(Constante.PAGE_ORDER_EDIT)) {
				ItemService objItemService = new ItemService();
				HashMap hshDataMap = objItemService.getInboxGenerateGuide();
				strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
				if(StringUtils.isNotBlank(strMessage)) {
		%>			alert("<%=MiUtil.getMessageClean(strMessage)%>");
		<%		} else {
					ArrayList arrInboxList = (ArrayList) hshDataMap.get("arrInboxList");
					Iterator itInboxList = arrInboxList.iterator();
					while(itInboxList.hasNext()) {
						String strInboxValidate = (String) itInboxList.next();
		%>				try {
							if("<%=strInboxValidate%>" == document.frmdatos.txtEstadoOrden.value) {
								if(document.frmdatos.txtNumeroGuia.value!="S") {
									fxSetImeiSimReadOnly(false);
								}
							}
						} catch(exception) {
							alert(exception.description);
						}
		<%			}
				}
			}
		%>
	}
  
  
  var strDivisionId = '<%=MiUtil.getString((String)strDivisionId)%>';
  
  function fxOnLoadImeiService(){
    if( strDivisionId == 2 )
      document.frmdatos.txtSims.readOnly = true;
      
    if(table_imeis.rows.length < 2){
      document.frmdatos.txtImeis.readOnly = true;
      document.frmdatos.txtSims.readOnly = true;
    }
  }
  
	function fxSetImeiSimReadOnly(flag) {
    document.frmdatos.txtImeis.readOnly = flag;
    document.frmdatos.txtSims.readOnly = flag;      
    if( strDivisionId == 2 )
      document.frmdatos.txtSims.readOnly = true;      
	}
	//CAMBIOS DE RDELOSREYES - FIN - 29/01/2008
  
	function fxCargaImeiOrders(objThis) {
		vForm = parent.mainFrame.document.frmdatos;
		var table = parent.mainFrame.document.all ? parent.mainFrame.document.all["table_imeis"] : parent.mainFrame.document.getElementById("table_imeis");
		if ( trim(vForm.txtImeis.value) == ""){
			alert("El Imei está en blanco");
			vForm.txtImeis.focus();
			return false;
		}  
		try {
			if(table.rows.length == 2) { 
				if(vForm.item_imei_radio.checked) {            
					vForm.item_imei_imei.value = vForm.txtImeis.value;				
					vForm.txtImeis.value="";
					vForm.hdnImeiChange.value="S";
					vForm.txtSims.value = fxGetSIMSugerido(vForm.item_imei_num.value);      
				}
			} 
			else {// -- (3)		    
				for(i=0; i<vForm.item_imei_radio.length; i++) {	 // (2)			   
					if(vForm.item_imei_radio[i].checked) { // (1)
						if(!fxValidaImeiRepetido(objThis)) {
							vForm.txtImeis.select();
							return;
						}
						vForm.item_imei_imei[i].value = vForm.txtImeis.value;						
						vForm.txtImeis.value="";
						vForm.hdnImeiChange[i].value="S";          
						vForm.txtSims.value = fxGetSIMSugerido(vForm.item_imei_num[i].value);
						if (vForm.txtSims.readOnly){
							for (m=0; m<(table.rows.length - 1); m++){
								if (vForm.item_imei_imei[m].value==""){
									vForm.item_imei_radio[m].checked = true;                    
									vForm.txtImeis.focus();      
									vForm.hdn_item_imei_selecc.value=m;
									break;
								}
							} //fin del FOR
						}
						else{
							if ( !fxIsTab() )
							vForm.txtSims.focus();                
						}
						break;            
					} //  -- (1)
				} //  -- (2) FOR
			} // -- (3)		
			try{
				vForm.hdnChangedOrder.value="S";
			}catch(e){}
		} 
		catch(exception) {
			alert(" "+exception.description);
		}
		return true;
	}
  
  function fxIsTab(){        
    return (window.event.keyCode == 9)
  }
  function fxIsEnter(){        
    return (window.event.keyCode == 13)
  }
  
  function fxGetSIMSugerido(fila){ 
    var vSIM ="";
    
    vForm = parent.mainFrame.document.frmdatos;
    lugarDespacho = vForm.cmbLugarAtencion.value;    
    // Aplica solo para CAMBIO DE MODELO y lugar de despacho FULFILLMENT
    //lugarDespacho == "<%=Constante.DISPATCH_PLACE_ID_FULLFILLMENT%>"  &&
    if ("<%=strCategoryId%>" == "<%=Constante.SPEC_CAMBIO_MODELO%>"){
      var indice= -1;
      indice=fxGetIndiceItemImeiService(fila);      
      if (indice != -1){        
        if (items_table.rows.length == 2){ //tiene una sola fila
          if (vForm.txtItemDevolucionFlag.value == "SI" || vForm.txtItemDevolucionFlag.value == "S" ){
            vSIM =  vForm.txtItemSIM.value;  
          }          
        }
        else{
			 //tiene mas de una fila
          if (vForm.txtItemDevolucionFlag[indice-1].value == "SI" || vForm.txtItemDevolucionFlag.value == "S"){
            vSIM =  vForm.txtItemSIM[indice-1].value;  
          }
        }  
      }
    }
    return vSIM
  }
  
  function fxGetIndiceItemImeiService(fila){    
    try{
      if (fila !="" ) {        
        indice=fila.substring(0,fila.indexOf('-',0))        
      }
      else{
        alert("Error al tratar de obtener el indice de la sección de escaneo de IMEI/SIM");
        indice = -1;
      }
    }  
    catch(e){
      alert("Error al tratar de obtener el indice de la sección de escaneo de IMEI/SIM");
      indice = -1;      
    } 
    return indice;    
  }
  
  function fxValidaImeiRepetido(objNuevoImei) {
		vForm = parent.mainFrame.document.frmdatos;
		try {
			cantImeis = vForm.item_imei_imei.length;
			if(cantImeis > 1) {
				for(a = 0; a < cantImeis; a++) {
					oldImei = vForm.item_imei_imei[a].value;
					if(oldImei != "" && oldImei == objNuevoImei.value) {
						alert("IMEI ya está registrado, verifique");
						objNuevoImei.focus();
						return false;
					}
				}
			}
		} catch(exception) {
			alert("[objItemImeiService.jsp:fxValidaImeiRepetido]= "+exception.description);
		}

		return true;
	}
  
</script>
<!-- Fin IMEIS -->
<!-- Catch -->
<% 
}catch(Exception e){
   e.getCause();
   e.printStackTrace();
   System.out.println("[objItemImeiService.jsp] Error try catch -->"+e.getMessage()); 
%>
<script>
   alert("<%=MiUtil.getMessageClean(e.getMessage())%>");
   
   
</script>
<%
}
%>
<!-- Fin Catch -->