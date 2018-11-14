<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="pe.com.nextel.bean.PortalSessionBean" %>
<%@ page import="pe.com.nextel.bean.NpObjHeaderSpecgrp" %>
<%@ page import="pe.com.nextel.bean.ItemDeviceBean" %>
<%@ page import="pe.com.nextel.bean.ItemBean" %>
<%@ page import="pe.com.nextel.bean.ProductBean" %>
<%@ page import="pe.com.nextel.bean.OrderBean" %>
<%@ page import="pe.com.nextel.bean.SpecificationBean" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.service.NewOrderService" %>
<%@ page import="pe.com.nextel.service.SessionService" %>
<%@ page import="pe.com.nextel.service.GeneralService" %>
<%@ page import="pe.com.nextel.service.MassiveOrderService" %>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.text.NumberFormat"%>
<%@ page import="pe.com.nextel.bean.ServiciosBean" %>
<%
try{
   NewOrderService      objNewOrderService          = new NewOrderService();
   SessionService       objSession                  = new SessionService();
   GeneralService       objGeneralService           = new GeneralService();
   MassiveOrderService  objItemMassiveOrderService  = new MassiveOrderService();
   System.out.println("valores de entrada00================================");
   NumberFormat formatter = new DecimalFormat("#0.00");     
   ProductBean objProdBean = new ProductBean();
   OrderBean   objOrderBean = null;  
   
   Hashtable hshtinputNewSection = null;   
   HashMap   hshData=null;
   HashMap   hshScreenField=null;
   HashMap   hshOrder=null;
	 HashMap   hshItemEditable=null;   
   ArrayList objArrayListServiceBySolution = new ArrayList();
   HashMap   objHashItemOrder   = new HashMap();
   HashMap   objHashGeneric     = new HashMap();
   
   ArrayList objArrayItemOrder  = new ArrayList();
   ArrayList objArrayGeneric    = new ArrayList();
   
   String    strCustomerId= "",
             strnpSite ="",
             strCodBSCS = "",
             strSpecificationId = "",
             strDivision ="",
             strOrderId  = "",
             strStatus   = "",
             strMessage  = "",
             strSiteOppId = "",
             strUnknwnSiteId = "",
             strGeneratorType= "";
            
   String    strSessionId=null;
   int       strValiditem=0;
   String    strNumPaymentOrderId=""; 
   String	   strNumGuideNumber=""; 	 
   String    strDeleteItem="";

   long      lGeneratorId;
   String    strCustomerType="";
            
   SpecificationBean objSpecificationBean= null;
   
   String strPlan = "";
   MassiveOrderService objMassiveOrderService = new MassiveOrderService();
   ArrayList objArrayList        = new ArrayList();
   
   hshtinputNewSection     = (Hashtable)request.getAttribute("hshtInputNewSection");
   String URL_SSAA         = request.getContextPath()+"/DYNAMICSECTION/DynamicSectionItemsMassive/DynamicSectionItemsMassivePage/PopUpServices.jsp";
   String strRutaContext                = request.getContextPath();
   String actionURL_MassiveOrderServlet = strRutaContext+"/massiveorderServlet";
   
   
   if ( hshtinputNewSection != null ) {      
      strCustomerId           =   MiUtil.getString((String)hshtinputNewSection.get("strCustomerId"));
      strnpSite               =   MiUtil.getString((String)hshtinputNewSection.get("strSiteId"));
      strCodBSCS              =   MiUtil.getString((String)hshtinputNewSection.get("strCodBSCS"));
      strSpecificationId      =   MiUtil.getString((String)hshtinputNewSection.get("strSpecificationId"));
      strDivision             =   MiUtil.getString((String)hshtinputNewSection.get("strDivisionId"));
      strOrderId              =   MiUtil.getString((String)hshtinputNewSection.get("strOrderId"));
      strSessionId            =   MiUtil.getString((String)hshtinputNewSection.get("strSessionId"));
      strStatus               =   MiUtil.getString((String)hshtinputNewSection.get("strStatus")); 
      strGeneratorType        =   MiUtil.getString((String)hshtinputNewSection.get("strGeneratorType"));
      lGeneratorId            =   MiUtil.parseLong((String)hshtinputNewSection.get("strGeneratorId")); 
      
      System.out.println("hshtinputNewSection=============="+strCustomerId);
        System.out.println("strOrderId=============="+strOrderId);
      if(strnpSite.equals("")) strnpSite="0";
      
      if(strnpSite.equals("0")) {
		    strCustomerType=Constante.CUSTOMERTYPE_CUSTOMER; // Si el valor es 0 no ingreso Site en la pantalla principal		    
	    }
	    else{
		    strCustomerType=Constante.CUSTOMERTYPE_SITE;		    
	    }	           
      
   }
   //Se obtiene la información de Cliente ingresado   
   objHashItemOrder     = objItemMassiveOrderService.MassiveOrderDAOgetItemOrder(MiUtil.parseLong(strCustomerId),MiUtil.parseLong(strnpSite),MiUtil.parseLong(strSpecificationId),0);
  // System.out.println("objHashItemOrder==============>"+objHashItemOrder);
   strMessage=(String)objHashItemOrder.get("strMessage");
   System.out.println("Valores Mensaje================="+strMessage);
   if (strMessage!=null)
     throw new Exception(strMessage);
     
   objArrayItemOrder = (ArrayList)objHashItemOrder.get("objArrayList");   
   System.out.println("Tamaño============"+objArrayItemOrder.size());
   
   //Obtenemos los SSAA
   HashMap hServiceList  = objMassiveOrderService.getServiceList(MiUtil.parseInt(strDivision),MiUtil.parseInt(strPlan));
   strMessage = (String)hServiceList.get("strMessage");          
   System.out.println("[objServiceAditional][strMessage]"+strMessage); 
   if (strMessage!=null)
     throw new Exception(strMessage);
   /*objArrayList =(ArrayList)hServiceList.get("objServiceList");  
   System.out.println("[objArrayList.size***********]"+objArrayList.size()); */ 
   
   //Obtenemos los SSAA por solución
   HashMap hServiceBySolutionList  = objMassiveOrderService.getServiceListBySolution(MiUtil.parseInt(strDivision)); //.getServiceListBySolution(MiUtil.parseInt(strDivision));   
   strMessage = (String)hServiceBySolutionList.get("strMessage");          
   System.out.println("[objServiceAditional][strMessage]"+strMessage); 
   if (strMessage!=null)
     throw new Exception(strMessage);
     
   objArrayListServiceBySolution =(ArrayList)hServiceBySolutionList.get("objServiceBySolutionList");  
   
   
   /* Se Obtiene el numero el maximos numero de registros que se podra almacenar */
   int numMaxRecord = 0;
   objHashGeneric = objGeneralService.getTableList("MASSIVE_MAX_RECORDS","1"); 
   strMessage=(String)objHashGeneric.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage); 
      
    objArrayGeneric=(ArrayList)objHashGeneric.get("arrTableList");
    //if( objArrayGeneric != null ){
      if( objArrayGeneric.size() > 0 ){
        HashMap hsMapMaxRecord = new HashMap();               
        for(int i=0; i<objArrayGeneric.size(); i++ ) {  
                hsMapMaxRecord = (HashMap)objArrayGeneric.get(i);                  
                System.out.println("Valor de HM "+hsMapMaxRecord.get("wv_npValue"));                   
                numMaxRecord = Integer.parseInt((String)hsMapMaxRecord.get("wv_npValue"));                   
        }
      }else {
            // Valor por default
              numMaxRecord = Constante.MASSIVE_MAX_RECORD_DEFAULT;
      }
   %>
  
  <link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
  <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
  <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
  <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderIXEdit.js"></script>

          <br>
          <div id="services">
            
            <!-- Servicios Disponibles y Seleccionados -->
            <div id="tableServiciosAdicionales">
            <table border="0" cellspacing="0" cellpadding="0" id="tbCabeceraServiciosAdicionales">
              <tr class="PortletHeaderColor">
                 <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="16"></td>
                 <td class="SubSectionTitle" align="left" valign="top">Servicios Adicionales</td>
                 <td class="SubSectionTitleRightCurve" valign="top" align="right" width="12"></td>
              </tr>
            </table>
            <table border="1" width="90%" cellpadding="2" cellspacing="0" class="RegionBorder">
               <tr align="center">
                  <td class="RegionHeaderColor" valign="top">
                     <table border="0" cellspacing="1" cellpadding="0" width="100%">
                        <tr >
                           <td class="CellLabel" align="center">Disponibles</td>
                           <td class="CellLabel"></td>                           
                           <td class="CellLabel" align="center">Activar</td>
                           <td class="CellLabel" align="center">Desactivar</td>
                           <td class="CellLabel"></td>
                        </tr>
                        <tr>
                           <td class="CellLabel" width="25%">&nbsp;</td>
                           <td class="CellLabel">&nbsp;</td>
                           <td class="CellLabel" width="25%" >&nbsp;</td>
                           <td class="CellLabel">&nbsp;</td>
                           <td class="CellLabel">&nbsp;</td>
                        </tr>
                        <tr>
                           <td class="CellContent" align="center">
                              <select name="cmbAvailableServicesMassive" size="10" multiple>
                              </select>                              
                              <input type="hidden" value="" name="item_services">
                           </td>
                           
                           <td class="CellContent" align="center">
                              <input type="button" value="Activar" name="btnAddService" onclick="javascript:fxAddServiceMassive('cmbAvailableServicesMassive','cmbActiveServicesMassive');"><p>                           
                              <input type="hidden" name="hdnFlagServicio" value="0"> <!-- Almacena el Flag de modificacion de los Servicios de Items -->
                              <br>
                               <input type="button" value="Desactivar" name="btnDesService" onclick="javascript:fxAddServiceMassive('cmbAvailableServicesMassive', 'cmbDesactiveServicesMassive');">    
                           </td> 
 
                           <td class="CellContent" align="center" >
                              <select name="cmbActiveServicesMassive" ondblclick="javascript:fxAddServiceMassive('cmbActiveServicesMassive','cmbAvailableServicesMassive');"  multiple style="width:200px; height:140px">
                              </select>
                           </td>
                           <td class="CellContent" align="center">
                              <select name="cmbDesactiveServicesMassive" ondblclick="javascript:fxAddServiceMassive('cmbDesactiveServicesMassive','cmbAvailableServicesMassive');"  multiple style="width:200px; height:140px">
                              </select>
                           </td>
                           
                           <td class="CellContent" align="center">                                                                                   
                             <input type="button" value="Validar Servicios" name="Servicio" onclick="javascript:fxMassiveServlet();"><p> 
                             <br>
                             <input type="button" value="Aplicar" name="btnApplyServiceMassive" onclick="javascript:fxApplyServiceMassive();"><p>                                                        
                             <br>
                             <input type="button" value="Limpiar" name="btnCleanServiceMassive" onclick="javascript:fxCleanServiceMassive('cmbAvailableServicesMassive','cmbActiveServicesMassive','cmbDesactiveServicesMassive');">                          
                           </td> 
                        </tr>                       
                     </table>
                  </td>
               </tr>
            </table>
            </div>
         </div>   <!-- de " div Servicios" -->  
         
      <br>
      <!--<input type="hidden" name="hdnNumeroOrder" value="<%=strOrderId%>">-->
     
      <INPUT type="hidden" name="hdnObjetType" value="ORDER"> 
      <input type="HIDDEN" name="hdnNpOrderId" value="<%=strOrderId%>"> 
      <input type="HIDDEN" name="hdnSpecificationId" value="<%=strSpecificationId%>">
      <!--<input type="hidden" name="hdnIndice" value="<%=objArrayItemOrder.size()%>">-->
      
      <div style="OVERFLOW: auto; WIDTH: 100%; HEIGHT: 600px">      
      <table  border="0" width="90%" cellpadding="2" cellspacing="1" class="RegionBorder">
        <tr>
          <td>
            <table id="table_assignment" name="table_assignment" border="0" width="100%" cellpadding="2" cellspacing="1">                     
              <tr id="cabecera">
                <td class="CellLabel" align="center">&nbsp;</td>
                <td class="CellLabel" align="center">&nbsp;<input type=checkbox name=chkPhoneNumberAll onclick="checkedAll();">&nbsp;</td>
                <td class="CellLabel" align="center">&nbsp;Teléfono&nbsp;</td>
                <td align="center" class="CellLabel" >&nbsp;Radio&nbsp;</td>
                <td align="center" class="CellLabel" >&nbsp;Solución&nbsp;</td>
                <td align="center" class="CellLabel" >&nbsp;Modelo&nbsp;</td>
                <td align="center" class="CellLabel" >&nbsp;Plan Tarifario&nbsp;</td>
                <td align="center" class="CellLabel" >&nbsp;Estado&nbsp;</td>  
                <td align="center" class="CellLabel" >&nbsp;SSAA Act.&nbsp;</td> 
                <td align="center" class="CellLabel" >&nbsp;SSAA Desac.&nbsp;</td> 
                <td align="center" class="CellLabel" >&nbsp;Msj. Resultado&nbsp;</td> 
                <td align="center" class="CellLabel" >&nbsp;Ver SSAA&nbsp;</td> 
              </tr>
            </table>
          </td>
        </tr>
        
        <tr>
          <td height="20" class="CellLabel">
          &nbsp;Items a Enviar:&nbsp;&nbsp;<input type=text name=txtNumRecord size=8 readonly /> 
          </td>
        </tr>
        
      </table>  
      </div>

      
 
  <script language="javascript" DEFER>  

  function fxCreateRow(){        
     //Inserto una nueva fila
    var row      = table_assignment.insertRow(-1);
     
    var elemText =  "";
    var cell = "";
    var i = 0;
     
    cell = row.insertCell(i++);
    cell.className = 'CellContent';
    cell.innerHTML = '';
    
    cell = row.insertCell(i++);
    cell.className = 'CellContent';
    elemText = fxGetCheck();
    cell.innerHTML = elemText;
    
    cell = row.insertCell(i++);
    cell.className = 'CellContent';
    elemText = fxGetTelefono();
    cell.innerHTML = elemText;
     
    cell = row.insertCell(i++);
    cell.className = 'CellContent';
    elemText = fxGetRadio();
    cell.innerHTML = elemText;

    cell = row.insertCell(i++);
    cell.className = 'CellContent';
    elemText = fxGetSolution();
    cell.innerHTML = elemText;
     
    cell = row.insertCell(i++);
    cell.className = 'CellContent';
    elemText = fxGetModel();
    cell.innerHTML = elemText;
     
    cell = row.insertCell(i++);
    cell.className = 'CellContent';
    elemText = fxGetPlan();
    cell.innerHTML = elemText;
     
    cell = row.insertCell(i++);
    cell.className = 'CellContent';
    elemText = fxGetEstado();
    cell.innerHTML = elemText;
    
    cell = row.insertCell(i++);
    cell.className = 'CellContent';
    elemText = fxGetActSSAA();
    cell.innerHTML = elemText;
    
    cell = row.insertCell(i++);
    cell.className = 'CellContent';
    elemText = fxGetDesSSAA();
    cell.innerHTML = elemText;
    
    cell = row.insertCell(i++);
    cell.className = 'CellContent';
    elemText = fxGetMessageResultSSAA();
    cell.innerHTML = elemText;
    
    cell = row.insertCell(i++);
    cell.className = 'CellContent';
    elemText = fxGetSSAA();
    cell.innerHTML = elemText;
    
    fxRenumeric("table_assignment",table_assignment.rows.length-1);  
  }

  </script>
  
  <script DEFER>
  function fxRenumeric(tablename,count){    
    eval("var table = document.all?document.all['"+tablename+"']:document.getElementById('"+tablename+"');");       
    var CellIndex;                         
    for(var i=1;i<=count;i++){              
      CellIndex =  table.rows[i].cells[0];  	  
      CellIndex.innerHTML= i;
    }
  }  
  </script>
  
  <script DEFER>
    function fxGetCheck(){
       return "<td>&nbsp;<input type=checkbox name=chkPhoneNumber disabled onClick=fxCheckedItem(this.checked,this.parentNode.parentNode.rowIndex) /><input type=hidden name=hdnchkPhone /><input type=hidden name=hdnGetServicio /></td>";	
    }
  </script>
  
  <script DEFER>
    function fxGetTelefono(){
	    return "<td class=CellLabel><input type=text name=txtItemPhoneNumber size=12 readonly/><input type=hidden name=hdnItemSimNumber /><input type=hidden name=hdnItemImeiNumber /><input type=hidden name=hdnItemCoId /><input type=hidden name=hdnItemModality /><input type=hidden name=hdnItemCurrency /><input type=hidden name=hdnItemId /></td>";		
    }  
  </script>
  
  <script DEFER>
    function fxGetRadio(){
	    return "<td><input type=text name=txtItemPhoneRadio size=8 readonly/><input type=hidden name=hdnItemQuantity /></td>";		
    } 
  </script>
  
  <script DEFER>
    function fxGetSolution(){
	    return "<td><input type=text name=txtItemSolution size=20 readonly/><input type=hidden name=hdnItemSolutionId /></td>";		
    }
  </script>
  
  <script DEFER>
    function fxGetModel(){
	    return "<td><input type=text name=txtItemModel size=10 readonly/><input type=hidden name=hdnItemEquipment /></td>";		
    }
  </script>
  
  <script DEFER>
    function fxGetPlan(){
	    return "<td><input type=text name=txtItemOrigPlan size=30 readonly/><input type=hidden name=hdnItemOrigPlanId /><input type=hidden name=hdnItemOrigProductId /><input type=hidden name=hdnItemOrigProductLineId /></td>";		
    }
  </script>
  
  <script DEFER>
    function fxGetEstado(){
	    return "<td><input type=text name=txtItemStatus size=10 readonly/><input type=hidden name=hdnIndice /></td>";		
    }  
  </script>
  
  <script DEFER> 
    function fxGetActSSAA(){	    
      return "<td><input type=text name=txtItemActSSAA size=8 readonly/><input type=hidden name=hdnItemActSSAA /></td>";		
  }
  </script>
  
  <script DEFER>
    function fxGetDesSSAA(){
      return "<td><input type=text name=txtItemDesSSAA size=8 readonly/><input type=hidden name=hdnItemDesSSAA /></td>";
    }  
  </script>
  
  <script DEFER>
    function fxGetMessageResultSSAA(){
      return "<td><input type=text name=txtItemMsjResSSAA size=50 readonly/><input type=hidden name=hdnItemMsjResSSAA value=ERROR /></td>";
    }  
  </script>
  
  <script DEFER>
    function fxGetSSAA(){
	    return "<td><input type=button name=txtItemSSAA size=50 value=........... onclick='javascript:fxShowSSAA(this,this.parentNode.parentNode.rowIndex);'/><input type=hidden name=hdnItemServices /><input type=hidden name=hdnItemServicesNew /></td>";		
    }  
  </script>
  
  <script DEFER>    
    function checkedAll () {         
      var form = document.frmdatos; 
      var index = table_assignment.rows.length;
      
      if (index > 1){
          if (form.chkPhoneNumberAll.checked == true){          
                  for (var i =0; i < index - 1; i++){
                      if (form.chkPhoneNumber[i].disabled == false){
                        form.hdnchkPhone[i].value = "on";
                        form.chkPhoneNumber[i].checked = true;                               
                       }
                  }
          }else{
                   for (var i =0; i < index - 1; i++){  
                       form.hdnchkPhone[i].value = "off"; 
                       form.chkPhoneNumber[i].checked = false;  
                   }    
          }
      }
    }

 </script>
  
 <script DEFER>    
  //Vector de Servicios
  var vServicio            = new Vector();
  var vServicioPorSolucion  = new Vector();
  var str_list_act = "";
  var str_list_des = "";
  var str_list_final = "";
  var str_list_services = "";
  
  function fxLoadMassiveItemsDataSSAA(){    
    var form = document.frmdatos;
    var index = table_assignment.rows.length;
    <%
     if( objArrayItemOrder != null ){
     System.out.println("objArrayItemOrder==="+objArrayItemOrder.size());
     
      ItemBean objMassiveItemBean =  null;      
      for(int i=0; i<objArrayItemOrder.size(); i++ ) {
        objMassiveItemBean = new ItemBean();
        objMassiveItemBean = (ItemBean)objArrayItemOrder.get(i);
        if (!objMassiveItemBean.getNpcontractstatus().equals("Deactive") || !objMassiveItemBean.getNpcontractstatus().equals("Other")){
        %>
        fxCreateRow();        
        
        if (index == 1){
          index = index + 1;
          form.txtItemPhoneNumber.value = "<%=MiUtil.getString(objMassiveItemBean.getNpphone())%>";
          form.txtItemPhoneRadio.value = "<%=MiUtil.getString(objMassiveItemBean.getNpradio())%>";
          form.txtItemSolution.value = "<%=MiUtil.getString(objMassiveItemBean.getNpsolutionname())%>";
          form.txtItemModel.value = "<%=MiUtil.getString(objMassiveItemBean.getNpmodel())%>";
          form.txtItemOrigPlan.value = "<%=MiUtil.getString(objMassiveItemBean.getNporiginalplanname())%>";
          form.txtItemStatus.value = "<%=MiUtil.getString(objMassiveItemBean.getNpcontractstatus())%>"; 
          form.hdnItemSimNumber.value ="<%=MiUtil.getString(objMassiveItemBean.getNpsiminumber())%>"; 
          form.hdnItemImeiNumber.value ="<%=MiUtil.getString(objMassiveItemBean.getNpimeinumber())%>"; 
          form.hdnItemCoId.value ="<%=MiUtil.getString(objMassiveItemBean.getNpcontractnumber())%>";           
          form.hdnItemModality.value ="<%=MiUtil.getString(objMassiveItemBean.getNpmodalitysell())%>";    
          form.hdnItemSolutionId.value = "<%=MiUtil.getString(objMassiveItemBean.getNpsolutionid())%>";
          form.hdnItemOrigPlanId.value = "<%=MiUtil.getString(objMassiveItemBean.getNporiginalplanid())%>";
          form.hdnItemOrigProductId.value = "<%=MiUtil.getString(objMassiveItemBean.getNporiginalproductid())%>";
          form.hdnItemOrigProductLineId.value = "<%=MiUtil.getString(objMassiveItemBean.getNporiginalproductlineid())%>";
          form.hdnItemCurrency.value = "<%=MiUtil.getString(objMassiveItemBean.getNpcurrency())%>";
          form.hdnItemQuantity.value = "<%=MiUtil.getString(objMassiveItemBean.getNpquantity())%>";
          form.hdnItemServices.value= "<%=MiUtil.getString(objMassiveItemBean.getNpssaacontratado())%>";  
          form.hdnItemServicesNew.value= "<%=MiUtil.getString(objMassiveItemBean.getNpssaacontratado())%>";
          form.hdnItemEquipment.value ="<%=MiUtil.getString(objMassiveItemBean.getNpequipment())%>";         
          form.hdnItemMsjResSSAA.value = "ERROR";
          
          form.hdnchkPhone.value= "off"; 
          
          form.hdnIndice.value= "<%=objArrayItemOrder.size()%>";  
          form.hdnGetServicio.value= "false"; 
          
                <%
                  if (!MiUtil.getString(objMassiveItemBean.getNpcontractstatus()).equals("Suspended")){          
                %>
                      form.chkPhoneNumber.disabled  = false;
                <%}%>
        }else{
          form.txtItemPhoneNumber[index-1].value = "<%=MiUtil.getString(objMassiveItemBean.getNpphone())%>";
          form.txtItemPhoneRadio[index-1].value = "<%=MiUtil.getString(objMassiveItemBean.getNpradio())%>";
          form.txtItemSolution[index-1].value = "<%=MiUtil.getString(objMassiveItemBean.getNpsolutionname())%>";
          form.txtItemModel[index-1].value = "<%=MiUtil.getString(objMassiveItemBean.getNpmodel())%>";
          form.txtItemOrigPlan[index-1].value = "<%=MiUtil.getString(objMassiveItemBean.getNporiginalplanname())%>";
          form.txtItemStatus[index-1].value = "<%=MiUtil.getString(objMassiveItemBean.getNpcontractstatus())%>"; 
          form.hdnItemSimNumber[index-1].value ="<%=MiUtil.getString(objMassiveItemBean.getNpsiminumber())%>"; 
          form.hdnItemImeiNumber[index-1].value ="<%=MiUtil.getString(objMassiveItemBean.getNpimeinumber())%>"; 
          form.hdnItemCoId[index-1].value ="<%=MiUtil.getString(objMassiveItemBean.getNpcontractnumber())%>";           
          form.hdnItemModality[index-1].value ="<%=MiUtil.getString(objMassiveItemBean.getNpmodalitysell())%>";   
          form.hdnItemSolutionId[index-1].value = "<%=MiUtil.getString(objMassiveItemBean.getNpsolutionid())%>"; 
          form.hdnItemOrigPlanId[index-1].value = "<%=MiUtil.getString(objMassiveItemBean.getNporiginalplanid())%>";
          form.hdnItemOrigProductId[index-1].value = "<%=MiUtil.getString(objMassiveItemBean.getNporiginalproductid())%>";
          form.hdnItemOrigProductLineId[index-1].value = "<%=MiUtil.getString(objMassiveItemBean.getNporiginalproductlineid())%>";
          form.hdnItemCurrency[index-1].value = "<%=MiUtil.getString(objMassiveItemBean.getNpcurrency())%>";
          form.hdnItemQuantity[index-1].value = "<%=MiUtil.getString(objMassiveItemBean.getNpquantity())%>";
          form.hdnItemServices[index-1].value= "<%=MiUtil.getString(objMassiveItemBean.getNpssaacontratado())%>"; 
          form.hdnItemServicesNew[index-1].value= "<%=MiUtil.getString(objMassiveItemBean.getNpssaacontratado())%>";
          form.hdnItemEquipment[index-1].value ="<%=MiUtil.getString(objMassiveItemBean.getNpequipment())%>";
          form.hdnItemMsjResSSAA[index-1].value = "ERROR";
          
          form.hdnIndice[index-1].value= "<%=objArrayItemOrder.size()%>"; 
          
          form.hdnchkPhone[index-1].value= "off"; 
          form.hdnGetServicio[index-1].value= "false";
          
                <% System.out.println("ssaa=================="+objMassiveItemBean.getNpssaacontratado());
                   System.out.println("SOLUCION=============="+objMassiveItemBean.getNpsolutionid());
                  if (!MiUtil.getString(objMassiveItemBean.getNpcontractstatus()).equals("Suspended")){          
                %>
                      form.chkPhoneNumber[index-1].disabled  = false;
                <%}%>
          
          index = index + 1;
        }
      <%
      }
      }
    }
    %>
    fxLoadMassiveServiceAditionalSSAA();
  }
   
  function fxLoadMassiveServiceAditionalSSAA(){ 
    // Vector de Servicios por Defecto
    var vServicioPorDefecto  = new Vector();    
    var form = document.frmdatos;
    
    deleteAllValues(form.cmbAvailableServicesMassive);
    deleteAllValues(form.cmbDesactiveServicesMassive);
    vServicio.removeElementAll();
    vServicioPorSolucion.removeElementAll();

   <% objArrayList =(ArrayList)hServiceList.get("objServiceList");  

    if ( objArrayList != null && objArrayList.size() > 0 ){
      ServiciosBean serviciosBean = null;
      serviciosBean = (ServiciosBean)objArrayList.get(0);
       int longMaxServices = MiUtil.getString(serviciosBean.getNpnomserv()).length();
       for( int j = 1; j < objArrayList.size(); j++ ){
         serviciosBean = (ServiciosBean)objArrayList.get(j);
          if( MiUtil.getString(serviciosBean.getNpnomserv()).length() > longMaxServices )
            longMaxServices = MiUtil.getString(serviciosBean.getNpnomserv()).length();
       }
       
       %>
      longMaxServices = '30<%//=longMaxServices%>';
       <%       
       for( int i = 0; i < objArrayList.size(); i++ ){
          serviciosBean = (ServiciosBean)objArrayList.get(i);
          /*System.out.println("[fxLoadServiceAditiional][getNpservicioid]"+serviciosBean.getNpservicioid());
          System.out.println("[fxLoadServiceAditiional][getNpnomserv]"+serviciosBean.getNpnomserv());
          System.out.println("[fxLoadServiceAditiional][getNpnomcorserv]"+serviciosBean.getNpnomcorserv());
          System.out.println("[fxLoadServiceAditiional][getNpexcludingind]"+serviciosBean.getNpexcludingind());*/
          //System.out.println("[fxLoadServiceAditiional][getNpsolutionid]"+serviciosBean.getNpsolutionid());

       %>
       AddNewOption(document.frmdatos.cmbAvailableServicesMassive,'<%=serviciosBean.getNpservicioid()%>','<%=serviciosBean.getNpnomserv()%>');
       vServicio.addElement(new Servicio("<%=serviciosBean.getNpservicioid()%>","<%=serviciosBean.getNpnomserv()%>","<%=serviciosBean.getNpnomcorserv()%>","<%=MiUtil.getString(serviciosBean.getNpexcludingind())%>"));
       
       <%}
     }     
    %>
    <%if (objArrayListServiceBySolution!= null && objArrayListServiceBySolution.size() > 0){
        ServiciosBean serviciosBySolution = null;
        for( int i = 1; i < objArrayListServiceBySolution.size(); i++ ){
          serviciosBySolution = (ServiciosBean)objArrayListServiceBySolution.get(i);
          /*System.out.println("[fxLoadServiceAditiional][getNpsolutionid]"+serviciosBySolution.getNpsolutionid());
          System.out.println("[fxLoadServiceAditiional][getNpservicioid]"+serviciosBySolution.getNpservicioid());
          System.out.println("[fxLoadServiceAditiional][getNpexcludingind]"+serviciosBySolution.getNpexcludingind());
          System.out.println("[fxLoadServiceAditiional][getNpnomcorserv]"+serviciosBySolution.getNpnomcorserv());*/
          %>
          vServicioPorSolucion.addElement(new ServicioPorSolucion("<%=serviciosBySolution.getNpsolutionid()%>","<%=serviciosBySolution.getNpservicioid()%>","<%=MiUtil.getString(serviciosBySolution.getNpexcludingind())%>","<%=serviciosBySolution.getNpnomcorserv()%>"));
        <%}
      }%>

   }

   function AddNewOption(TheCmb, Value, Description) {
     var option = new Option(Description, Value);
     var i = TheCmb.options.length;
     TheCmb.options[i] = option;
   }
   
   function Servicio(id, name ,nameShort ,exclude) {        
     this.id = id;
     this.name = name;
     this.nameDisplay = (exclude=="")?name:name+" - "+exclude;
     this.nameShort = nameShort;
     this.nameShortDisplay = (exclude=="")?nameShort:nameShort+" - "+exclude;
     this.exclude = exclude;
     this.active_new = "N";
     this.modify_new = "N";        
   }
   
   function ServicioPorSolucion(idSol, idServ ,exclude,nameShort) {        
     this.idSol = idSol;
     this.idServ = idServ;
     this.exclude = exclude;
     this.nameShort = nameShort;
   }
 
   function fxAddServiceMassive(listsource,listdestiny){
     for (i=eval("document.frmdatos."+listsource+".options.length")-1;i>=0;i--){
       if (eval("document.frmdatos." + listsource + ".options[i].selected")){
         marca = eval("document.frmdatos." + listsource + ".options[i].text");
         codigo = eval("document.frmdatos." + listsource + ".options[i].value");
         control = eval("document.frmdatos." + listdestiny);
         var option1 = new Option(marca,codigo,control.length);
         control.options.add(option1);
         expression = "document.frmdatos." + listsource + ".options.remove(" + i + ")";
         eval(expression);
       }
     }
   }
 
   function fxCleanServiceMassive(listsource,listdestinyactive,listdestinydesactive){
      var index = table_assignment.rows.length;
      var count = 0;
      
      if (index > 1){
       for(var i=0; i < index - 1  ; i++){  
         if (form.chkPhoneNumber[i].disabled == false && form.chkPhoneNumber[i].checked == true){   
           form.txtItemActSSAA[i].value = "";
           form.txtItemMsjResSSAA[i].value = "";
           form.hdnItemMsjResSSAA[i].value = "ERROR";
           form.txtItemDesSSAA[i].value = "";
           form.hdnItemServicesNew[i].value = form.hdnItemServices[i].value;
         }
         
         if (form.hdnItemMsjResSSAA[i].value == "SUCCESS"){                   
           count++;                        
         }
         
       }
      }
      
      form.txtNumRecord.value = '  '+count;
   }
  
   function fxSetListItemService(listActive){     
     var arrActive;
     var str="";
    
     if (listActive!= "" ){     
       arrActive = listActive.split("|");
     
       for(i=0; i<arrActive.length; i++){       
         if(arrActive[i]!="S")
           str = str + "|"+ arrActive[i];        
       }
      str = str.substring(1);
     }

     return str;
   }
   
   function fxShowSSAA(obj,rowIndex){     
     var list_act    = form.hdnItemServicesNew[rowIndex-1].value;
     var solutionId  = form.hdnItemSolutionId[rowIndex-1].value;
     var ItemActSSAA = form.txtItemActSSAA[rowIndex-1].value;
     var ItemDesSSAA = form.txtItemDesSSAA[rowIndex-1].value;
     
     if (list_act == ""){
       alert("Falta Validar [SERVICIOS]");
       return false;
     }
     
     url= "<%=URL_SSAA%>?strListAct="+list_act+"&strIndex="+rowIndex+"&strSolutionId="+solutionId+"&strItemActSSAA="+ItemActSSAA+"&strItemDesSSAA="+ItemDesSSAA;     
     var popupWin = window.open(url, "WinAsist2001","status=yes, location=0, width=550, scrollbars=yes, height=400, left=300, top=30, screenX=50, screenY=100");
   }
   
   //Verifica que el ssaa petenezca a la solución del item seleccionado
   function fxVerifyActiveSSAA(iSolId,listActivados){
     var nLength = vServicioPorSolucion.size();
     var arr_strActivados;
     var strEstado;
     var strMessageSol = "";
         
     arr_strActivados = listActivados.split("|");
     //strEstado = "NO";     
     for (m=0;m<arr_strActivados.length;m++){               
       strEstado = "NO";
       for(r=0; r<nLength ;r++){         
         objServicioPorSolucion = vServicioPorSolucion.elementAt(r);

         if(objServicioPorSolucion.idSol == iSolId){
           if(arr_strActivados[m] == objServicioPorSolucion.idServ){
             strEstado = "SI";                
             break;
           }
         }       
         
       }   

       /*if(strEstado == "NO")
         break;*/
       if (strEstado != "SI"){       
         //alert("nombre====="+ objServicioPorSolucion.nameShort);
         strMessageSol = strMessageSol + "," + fxGetNameSSAA(arr_strActivados[m]); //objServicioPorSolucion.nameShort;
       }
       
     }     
     //alert("strMessageSol=="+strMessageSol);
     return strMessageSol.substring(1);
     /*if(strEstado == "NO")
       return false;    
     else
       return true; */  
   }
   
   //Verifica que los ssaa a activar no sean excluyentes
   function fxVerifyExcludeSSAA(iSolId,listActivados,listActivos){
     var arr_strActivados; //ssaa que se van a activar
     var arr_strActivos; //ssaa con los que cuenta el equipo
     var arr_ServiciosTipo; // arreglo de servicios con el campo exluding
     var intTotal;
     var strEstado;
     var strMessage = "";
             
     arr_strActivados = listActivados.split("|");
     arr_strActivos = listActivos.split("|");
     //alert("arr_strActivados=========>"+arr_strActivados[0]);
     intTotal = arr_strActivados.length + arr_strActivos.length;
     arr_ServiciosTipo = new Array(intTotal);
     
     var j = 0;
     for(i=0; i<arr_strActivados.length; i++){ 
       arr_ServiciosTipo[j] = new Array(2);
       arr_ServiciosTipo[j][0] = arr_strActivados[i];
       arr_ServiciosTipo[j][1] = '';
       j++;
     }
     for(i=0; i<arr_strActivos.length; i++){ 
       arr_ServiciosTipo[j] = new Array(2);              
       arr_ServiciosTipo[j][0] = arr_strActivos[i];
       arr_ServiciosTipo[j][1] = '';
       j++;
     }
     
     //obtenemos el excluding del servicio
     for(i=0; i<arr_ServiciosTipo.length; i++)     
       arr_ServiciosTipo[i][1] = fxGetServiceRol(iSolId, arr_ServiciosTipo[i][0]);        
     
     //strEstado = "";    
     for(i=0;i< arr_ServiciosTipo.length - 1; i++){ 
       strEstado = "NO";
       for(j=i+1; j<arr_ServiciosTipo.length; j++){         
         if(arr_ServiciosTipo[i][1] == arr_ServiciosTipo[j][1] ){
           //strEstado = "Ya existe un Servicio Excluyente para " + arr_ServiciosTipo[i][1];
           //break;
           //strEstado =  strEstado + "," + arr_ServiciosTipo[i][1];
           strEstado = "SI";
           break;
         }
       }
       /*if(strEstado !=  "SI")
         break;    */   
         if (strEstado ==  "SI" && arr_ServiciosTipo[i][1] == ""){
            if ( arr_ServiciosTipo[i][0] == arr_ServiciosTipo[j][0])
               strMessage =  strMessage + "," + fxGetNameSSAA(arr_ServiciosTipo[i][0]);
         }
         if (strEstado ==  "SI" && arr_ServiciosTipo[i][1] != ""){
            strMessage =  strMessage + "," + arr_ServiciosTipo[i][1];
         }
         
     }

     //return strEstado.substring(1);
     return strMessage.substring(1);
   }
   
   //Funcion que devuelve el campo excluding de un servicio
   function fxGetServiceRol(iSolId, iServicioId){
     var nLength = vServicioPorSolucion.size();     
     var strExclude = "NADA";
     
     for(k=0; k<nLength; k++){ 
       objServicioPorSolucion = vServicioPorSolucion.elementAt(k);
       if(objServicioPorSolucion.idSol == iSolId && objServicioPorSolucion.idServ == iServicioId)
         strExclude = objServicioPorSolucion.exclude;  //objServicioPorSolucion.nameShort;
     }
     
     return strExclude;    
   }
   
   function fxVerifyDesactiveSSAA(listDesactivados,listActivos){
     var arrDesactivados;
     var arrActivos;
     var strEstado;
     var strMessage="";

     arrDesactivados = listDesactivados.split("|");
     arrActivos = listActivos.split("|");
     //strEstado = "NO";
     for (i=0; i< arrDesactivados.length ; i++){  
       strEstado = "NO";
       for (j=0; j< arrActivos.length; j++){
         if(arrDesactivados[i] == arrActivos[j]){
            strEstado = "SI";            
            break;
         }
       }
       //if ( strEstado == "NO"){
       if ( strEstado != "SI"){
         //strMessage = "Servicio: "+fxGetNameSSAA(arrDesactivados[i])+ " no se encuentra activo";
         //break;
         strMessage = strMessage + "," + fxGetNameSSAA(arrDesactivados[i]);
       }
     }
     
     return strMessage.substring(1);
   }
   
   //Función que devuelve el nombre del servicio adicional 
   function fxGetNameSSAA(idServ){
     var nCant = vServicio.size();
     var strName="";
     
     for (n=0; n<nCant; n++){                    
       objServicio = vServicio.elementAt(n);
       
       if(objServicio.id == idServ){
         strName = objServicio.nameShort;
         break;
       }       
     }
     
     return strName;
   }
   
   //Actualiza la lista de ssaa a activar que se vizualizará en el pop up
   function fxUpdateSSAAActive(listActivos,listActivar){//ssaa actuales, ssaa a activar
     var arrActivar;
     
     arrActivar = listActivar.split("|");

     for (i=0; i<arrActivar.length; i++){
       listActivos = listActivos + "|"+arrActivar[i] + "|"+"N"+"|"+"S";
     }
     str_list_final = listActivos;
     
   }
   
   //Actualiza la lista de ssaa a desactivar que se vizualizará en el pop up
   function fxUpdateSSAADesactive(listFinal,listDesactivar){
     var arrActivados;
     var arrDesactivados;
     var listActive="";
   
     arrActivados    = listFinal.split("|");
     arrDesactivados = listDesactivar.split("|");
   
     for (i=0; i<arrDesactivados.length; i++){    
       for (j=0; j<arrActivados.length; j++){
         if(arrDesactivados[i] == arrActivados[j]){
           arrActivados[j+2] = "N";
         }
       }
     }
     
     for(i=0; i<arrActivados.length; i++){
       listActive = listActive + "|" + arrActivados[i];
     }     
     
     str_list_final = listActive.substring(1);
     
   }
   
   function fxApplyServiceMassive(){
     var form = document.frmdatos; 
     var index = table_assignment.rows.length;
     var bError;
     var count = 0;
     str_list_act = "";
     str_list_des = "";
     str_list_services;
     
     if (document.frmdatos.cmbActiveServicesMassive.length < 1 &&
       document.frmdatos.cmbDesactiveServicesMassive.length < 1){
       alert("Debe seleccionar por lo menos un Servicio Disponible a [ACTIVAR] ó [DESACTIVAR]");
       return false;
     }
     

     //Obtenemos los SSAA a Activar Masivamente
     if (document.frmdatos.cmbActiveServicesMassive.length > 0){
       for(i=0; i< document.frmdatos.cmbActiveServicesMassive.length; i++)
         str_list_act = str_list_act + "|" + form.cmbActiveServicesMassive.options[i].value;
       str_list_act = str_list_act.substring(1);
       //alert("SSAA Active========================="+str_list_act);
     }
        
     //Obtenemos los SSAA a Desactivar Masivamente
     if (document.frmdatos.cmbDesactiveServicesMassive.length > 0){
       for(i=0; i< document.frmdatos.cmbDesactiveServicesMassive.length; i++)
         str_list_des = str_list_des + "|" + form.cmbDesactiveServicesMassive.options[i].value;
       str_list_des = str_list_des.substring(1);
       //alert("SSAA Desactive================="+str_list_des)
     }
     
     if (index > 1){
       for(var i=0; i < index - 1  ; i++){   
         bError = false;
         str="";
         str_list_final="";
         str_list_services="";
         if (form.chkPhoneNumber[i].disabled == false && form.chkPhoneNumber[i].checked == true){ 
           form.txtItemActSSAA[i].value = "";
           form.txtItemDesSSAA[i].value = "";
           form.txtItemMsjResSSAA[i].value = "";
           form.hdnItemMsjResSSAA[i].value = "ERROR";
           form.hdnItemServicesNew[i].value = form.hdnItemServices[i].value;
           
           if (form.hdnItemServices[i].value == ""){
             alert("Falta Validar [SERVICIOS]");
             return false;
           }
           //str_list_services = form.hdnItemServices[i].value;           
           str = fxSetListItemService(form.hdnItemServices[i].value);//se obtiene lista con formato: 180|370
           //alert("str==============================>"+str);
           if (document.frmdatos.cmbActiveServicesMassive.length > 0){       
             //Verificamos que los ssaa a activar pertenezcan a la solucion del item
             strMessage = fxVerifyActiveSSAA(form.hdnItemSolutionId[i].value,str_list_act);
             if (strMessage == ""){
             //if (fxVerifyActiveSSAA(form.hdnItemSolutionId[i].value,str_list_act)){
               form.txtItemActSSAA[i].value = "       SI";
               form.txtItemActSSAA[i].style.color = "blue";
               //if (form.txtItemDesSSAA[i].value == "       NO")
               //  form.txtItemDesSSAA[i].value = "";
               //alert("entraa SUCESS SOLUTION==");
               //Luego verificamos que no sean Excluyentes  
               strMessage = fxVerifyExcludeSSAA(form.hdnItemSolutionId[i].value,str_list_act,str);//form.hdnItemServices[i].value
              // if( strMessage == "SI"){
               if( strMessage == ""){
                 //Informativo
                 form.txtItemActSSAA[i].value = "       SI";
                 form.txtItemActSSAA[i].style.color = "blue";
                 //if (form.txtItemDesSSAA[i].value == "       NO")
                 //form.txtItemDesSSAA[i].value = "";
                
                 form.txtItemMsjResSSAA[i].value = "SUCCESS";
                 form.hdnItemMsjResSSAA[i].value = "SUCCESS";
                 form.txtItemMsjResSSAA[i].style.color = "blue";
                 //alert("strMessage================>"+strMessage);
                 //si todo ha salido ok se actualiza los ssaa a mostrar en el pop up
                 fxUpdateSSAAActive(form.hdnItemServices[i].value,str_list_act);//lista de ssaa que tiene el equipo,lista de ssaa a acivar
                 form.hdnItemServicesNew[i].value = str_list_final;
                 //alert("final=================="+form.hdnItemServicesNew[i].value);
               }else{
                 form.txtItemActSSAA[i].value = "       NO";
                 form.txtItemActSSAA[i].style.color = "red";
                                  
                 //alert("entraa ERROR EXCLUDE==");
                 form.txtItemMsjResSSAA[i].value = "ERROR : "+ strMessage; 
                 form.hdnItemMsjResSSAA[i].value = "ERROR";
                 form.txtItemMsjResSSAA[i].style.color = "red";
                 bError = true;
                 //si hay error al momento de levantar el pop up sólo se vizualiza los ssaa que tiene el equipo
                 form.hdnItemServicesNew[i].value = form.hdnItemServices[i].value;
               }
             }
             else{
               //alert("entraa ERROR SOLUTION==");
               form.txtItemActSSAA[i].value = "       NO";
               form.txtItemActSSAA[i].style.color = "red";
               
               form.txtItemMsjResSSAA[i].value = "ERROR : "+ strMessage; //"ERROR: Servicio Adicional no petenerce a la Solución del Equipo";            
               form.hdnItemMsjResSSAA[i].value = "ERROR";
               form.txtItemMsjResSSAA[i].style.color = "red";
               bError = true;
               //si hay error al momento de levantar el pop up sólo se vizualiza los ssaa que tiene el equipo
               form.hdnItemServicesNew[i].value = form.hdnItemServices[i].value;
             }
             
           }
           
           if (!bError){           
             if (document.frmdatos.cmbDesactiveServicesMassive.length > 0){
               str = fxSetListItemService(form.hdnItemServices[i].value);//se envia la lista original de ssaa que tiene el equipo
                                                                        //se obtiene lista con formato: 180|370
               
               //Verificamos que los ssaa a desactivar pertenezcan al equipo seleccionado   
               var strMessageDes = fxVerifyDesactiveSSAA(str_list_des,str);//form.hdnItemServices[i].value
               if( strMessageDes == ""){
                 form.txtItemDesSSAA[i].value = "       SI";
                 form.txtItemDesSSAA[i].style.color = "blue"; 
                 if (form.txtItemActSSAA[i].value == "       NO")
                 form.txtItemDesSSAA[i].value = "";
                 
                 form.txtItemMsjResSSAA[i].value = "SUCCESS";
                 form.hdnItemMsjResSSAA[i].value = "SUCCESS";
                 form.txtItemMsjResSSAA[i].style.color = "blue"; 
                 
                 //si todo ha salido ok se actualiza los ssaa a mostrar en el pop up
                 fxUpdateSSAADesactive(form.hdnItemServicesNew[i].value,str_list_des);
                 form.hdnItemServicesNew[i].value = str_list_final;
                 //alert("al final de desactivar========"+form.hdnItemServicesNew[i].value);
               }else{
                 form.txtItemDesSSAA[i].value = "       NO";
                 form.txtItemDesSSAA[i].style.color = "red";   
                 if (form.txtItemActSSAA[i].value == "       SI")
                 form.txtItemActSSAA[i].value = "";
                 //form.txtItemActSSAA[i].value = "";
                 
                 form.txtItemMsjResSSAA[i].value = "ERROR: "+strMessageDes;                 
                 form.hdnItemMsjResSSAA[i].value = "ERROR";
                 form.txtItemMsjResSSAA[i].style.color = "red";
                 bError = true;
                 //si hay error al momento de levantar el pop up sólo se vizualiza los ssaa que tiene el equipo
                 form.hdnItemServicesNew[i].value = form.hdnItemServices[i].value;
               }
             }
           }
           
         }
         
         if(form.hdnItemMsjResSSAA[i].value == "SUCCESS"){
            count++;  
         }
         
       }
     }
     
     form.txtNumRecord.value = '  '+count;
     
   }
   
   function fxMassiveServlet(){
     var index = table_assignment.rows.length;
      /*if (index > 1){
       for(var i=0; i < index - 1  ; i++){  
         if (form.chkPhoneNumber[i].disabled == false && form.chkPhoneNumber[i].checked == true){   
           form.txtItemActSSAA[i].value = "";
           form.txtItemMsjResSSAA[i].value = "";
           form.hdnItemMsjResSSAA[i].value = "ERROR";
           form.txtItemDesSSAA[i].value = "";
         }
       }
      }*/
     
     document.frmdatos.myaction.value = 'obtenerServicios';       
     form.action="<%=actionURL_MassiveOrderServlet%>";
     document.frmdatos.submit();              
   }
    
    function fxCheckedItem( objValue, objIndex ){        
        var form = document.frmdatos;
        var index = objIndex - 1;
          
          if (objValue == true){
              form.hdnchkPhone[index].value = "on";
          }else{
              form.hdnchkPhone[index].value = "off";
          }        
    }        
</script>
<script defer>

  function fxValidateMassiveDataSSAA(){
    var form = document.frmdatos; 
    var index = table_assignment.rows.length - 2;
    var lastIndex = table_assignment.rows.length - 2;
    var rowIndex = table_assignment.rows.length;
    var numMaxItems = <%=numMaxRecord%>;
    
    if (rowIndex > 2){
      
      if(form.txtNumRecord.value > numMaxItems){
        alert('No se puede enviar mas de ['+numMaxItems+'] Registros');
        return false;
      }
      
      for (var i =0; i <= index ; i++){         
          if (form.txtItemMsjResSSAA[i].value != "" && form.txtItemMsjResSSAA[i].value == "SUCCESS"){    
            return true;
          }else{                       
            if ( i == lastIndex ){
              alert ("Debe Activar o Desactivar SSAA con [SUCCESS]");
              return false;
            }   
          }
      }
    }else{
       alert("Solo se permite Ordenes para Clientes Mayores");
       return false;
    }
    
  }
  
</script>

<%      
}catch(Exception  ex){                
   System.out.println("Error try catch -->"+MiUtil.getMessageClean(ex.getMessage())); 
   ex.printStackTrace();
%>
<script>
   alert("<%=MiUtil.getMessageClean(ex.getMessage())%>");
</script>
<%
}
%>  