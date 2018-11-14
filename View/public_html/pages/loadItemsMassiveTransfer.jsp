<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="pe.com.nextel.bean.ItemBean"%>
<%@page import="pe.com.nextel.bean.ProductLineBean"%>
<%@page import="pe.com.nextel.bean.PlanTarifarioBean"%>
<%@page import="pe.com.nextel.service.NewOrderService"%>
<%@page import="pe.com.nextel.util.MiUtil"%>
<%@page import="pe.com.nextel.util.Constante"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.math.BigDecimal"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.text.NumberFormat"%>
<%@ page import="pe.com.nextel.bean.ServiciosBean" %>
          <%
            ArrayList objArrayItemOrder   = (ArrayList)request.getAttribute("objArrayItemOrder");   
            
            ArrayList objArrayPlanList   = (ArrayList)request.getAttribute("objArrayPlanList"); 
            
            ArrayList objArrayServiceList   = (ArrayList)request.getAttribute("objArrayServiceList"); 
            
            ArrayList objArrayServiceBySolutionList   = (ArrayList)request.getAttribute("objArrayServiceBySolutionList"); 
            
            String objWv_swname  = (String)request.getAttribute("wv_swname");
            
            int objWv_customerid = MiUtil.parseInt((String)request.getAttribute("wv_customer"));
            
          %>                  
<script>
    var form = parent.mainFrame.document.frmdatos;
    
    if (form.txtCompanya.value == "" || "<%=objWv_swname%>" != "null"){    
      form.txtCompanya.value="<%=objWv_swname%>";
      form.hdnCompanyaName.value ="<%=objWv_swname%>";
    }
    
    if (form.hdnCustId1.value == "" || form.hdnCustId1.value == 0 || "<%=objWv_customerid%>" != "null")
      form.hdnCustId1.value = "<%=objWv_customerid%>";
    
</script>
<!--<form name="frmdatos"> -->
  <textarea name="txtItemsMassiveTransfer">
    <table  border="0" width="100%" cellpadding="2" cellspacing="1" class="RegionBorder">
      <tr>
        <td class="RegionHeaderColor" width="100%">
            <table border="0" cellspacing="0" cellpadding="0" id="tbCabeceraServiciosAdicionales">
              <tr class="PortletHeaderColor">
                 <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="16"></td>
                 <td class="SubSectionTitle" align="left" valign="top">Transferencia</td>
                 <td class="SubSectionTitleRightCurve" valign="top" align="right" width="12"></td>
              </tr>
            </table>
            
            <table  border="0" width="70%" cellpadding="2" cellspacing="1" class="RegionBorder">
              <td class="CellLabel" align="center" >
                <input type="button" name="btnAplicarCesion" value="Aplicar Transferencia" onclick="fxAplicarCesion();" > 
              </td>             
              <td class="CellLabel" align="center" >
                <input type="button" name="btnLimpiarCesion" value="Limpiar Transferencia" onclick="fxLimpiarCesion();" > 
              </td>             
            </table>  
          
          <br>
          <div id="plan">
            <table border="0" cellspacing="0" cellpadding="0" id="tbCabeceraServiciosAdicionales">
              <tr class="PortletHeaderColor">
                 <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="16"></td>
                 <td class="SubSectionTitle" align="left" valign="top">Plan Tarifario</td>
                 <td class="SubSectionTitleRightCurve" valign="top" align="right" width="12"></td>
              </tr>
            </table>
            
            <table  border="0" width="70%" cellpadding="2" cellspacing="1" class="RegionBorder">
              <td class="CellLabel" >&nbsp; Plan Tarifario: &nbsp;
                <select name="cmb_PlanTarifario" > 
                  <option>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                  </option>
                </select>                     
               <input type="hidden" name="hndcmb_PlanTarifario">
             </td> 
             <td class="CellLabel" align="center" >&nbsp; Precio Excepción: &nbsp;
               <input type="text" name="txtPriceExcep" value="" >
             </td>
             <td class="CellLabel" align="center" >
               <input type="button" name="Aplicar" value="Aplicar" onclick="fxAplicarPlan();" > 
             </td>
             <td class="CellLabel" align="center" >
               <input type="button" value="Limpiar" name="btnLimpiarPlan" onclick="javascript:fxCleanPlan();">
             </td>             
           </table>       
        </div>   
    
        <br>
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
            <table border="1" width="70%" cellpadding="2" cellspacing="0" class="RegionBorder">
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
                              <select name="cmbAvailableServicesMassive" size="10" multiple>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
                             <!--<input type="button" value="Validar Servicio" name="Servicio" onclick="javascript:fxMassiveServlet();"><p> 
                             <br>-->
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
         <br>
                 
          <div style="OVERFLOW: auto; WIDTH: 100%; HEIGHT: 600px">      
          <table  border="0" width="100%" cellpadding="2" cellspacing="1" class="RegionBorder">
            <tr>
              <td>
                <table id="table_assignment" name="table_assignment" border="0" width="100%" cellpadding="2" cellspacing="1">                     
                  <tr id="cabecera">
                    <td class="CellLabel" align="center">&nbsp;</td>
                    <td class="CellLabel" align="center">&nbsp;<input type=checkbox name=chkPhoneNumberAll onclick="javascript:checkedAll();" >&nbsp;</td>
                    <td class="CellLabel" align="center">&nbsp;Teléfono&nbsp;</td>
                    <td align="center" class="CellLabel" >&nbsp;Radio&nbsp;</td>
                    <td align="center" class="CellLabel" >&nbsp;Solución&nbsp;</td>
                    <!--<td align="center" class="CellLabel" >&nbsp;Modelo&nbsp;</td>-->
                    <td align="center" class="CellLabel" >&nbsp;Plan Tarifario Orig&nbsp;</td>
                    <td align="center" class="CellLabel" >&nbsp;Plan Tarifario&nbsp;</td>
                    <td align="center" class="CellLabel" >&nbsp;Línea&nbsp;</td>
                    <td align="center" class="CellLabel" >&nbsp;Precio&nbsp;</td>
                    <td align="center" class="CellLabel" >&nbsp;Precio Exc.&nbsp;</td>
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
          

        </td>
      </tr>
    </table> 
    </table>
  </textarea>
<!--</form> -->


  <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
  <link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
  <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
  <script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderIXEdit.js"></script>
   
  <script DEFER>
    
    function fxLoadServiceAditional(){
      // Vector de Servicios por Defecto
      var vServicioPorDefecto  = new Vector();    
      var form = parent.mainFrame.document.frmdatos;
    
      parent.mainFrame.deleteAllValues(form.cmbAvailableServicesMassive);
      parent.mainFrame.deleteAllValues(form.cmbDesactiveServicesMassive);
      parent.mainFrame.vServicio.removeElementAll();
      parent.mainFrame.vServicioPorSolucion.removeElementAll();

      <%  

      if ( objArrayServiceList != null && objArrayServiceList.size() > 0 ){
        ServiciosBean serviciosBean = null;
        serviciosBean = (ServiciosBean)objArrayServiceList.get(0);
        int longMaxServices = MiUtil.getString(serviciosBean.getNpnomserv()).length();
        for( int j = 1; j < objArrayServiceList.size(); j++ ){
          serviciosBean = (ServiciosBean)objArrayServiceList.get(j);
          if( MiUtil.getString(serviciosBean.getNpnomserv()).length() > longMaxServices )
            longMaxServices = MiUtil.getString(serviciosBean.getNpnomserv()).length();
        }
       
      %>
        longMaxServices = '30<%//=longMaxServices%>';
      <%       
        for( int i = 0; i < objArrayServiceList.size(); i++ ){
          serviciosBean = (ServiciosBean)objArrayServiceList.get(i);
          /*System.out.println("[fxLoadServiceAditiional][getNpservicioid]"+serviciosBean.getNpservicioid());
          System.out.println("[fxLoadServiceAditiional][getNpnomserv]"+serviciosBean.getNpnomserv());
          System.out.println("[fxLoadServiceAditiional][getNpnomcorserv]"+serviciosBean.getNpnomcorserv());
          System.out.println("[fxLoadServiceAditiional][getNpexcludingind]"+serviciosBean.getNpexcludingind());*/
          //System.out.println("[fxLoadServiceAditiional][getNpsolutionid]"+serviciosBean.getNpsolutionid());

       %>
       fxAddNewOption(parent.mainFrame.document.frmdatos.cmbAvailableServicesMassive,'<%=serviciosBean.getNpservicioid()%>','<%=serviciosBean.getNpnomserv()%>');
       parent.mainFrame.vServicio.addElement(new Servicio("<%=serviciosBean.getNpservicioid()%>","<%=serviciosBean.getNpnomserv()%>","<%=serviciosBean.getNpnomcorserv()%>","<%=MiUtil.getString(serviciosBean.getNpexcludingind())%>"));
       
       <%}
      }     
     %>
     
     <%if (objArrayServiceBySolutionList!= null && objArrayServiceBySolutionList.size() > 0){
        ServiciosBean serviciosBySolution = null;
        for( int i = 1; i < objArrayServiceBySolutionList.size(); i++ ){
          serviciosBySolution = (ServiciosBean)objArrayServiceBySolutionList.get(i);
          /*System.out.println("[fxLoadServiceAditiional][getNpsolutionid]"+serviciosBySolution.getNpsolutionid());
          System.out.println("[fxLoadServiceAditiional][getNpservicioid]"+serviciosBySolution.getNpservicioid());
          System.out.println("[fxLoadServiceAditiional][getNpexcludingind]"+serviciosBySolution.getNpexcludingind()); */         
          %>
          parent.mainFrame.vServicioPorSolucion.addElement(new ServicioPorSolucion("<%=serviciosBySolution.getNpsolutionid()%>","<%=serviciosBySolution.getNpservicioid()%>","<%=MiUtil.getString(serviciosBySolution.getNpexcludingind())%>","<%=serviciosBySolution.getNpnomcorserv()%>"));
        <%}
      }%>

    }
    
    /*function fxLoadPlanTransfer(){
      var index = parent.mainFrame.table_assignment.rows.length-1;
      var form  = parent.mainFrame.document.frmdatos;
 
      var objPlan = null;
    
      for(i=0; i < parent.mainFrame.vplan.size(); i++){
        objPlan =  parent.mainFrame.vplan.elementAt(i);

        form.opcion=new Option(objPlan.plandes,objPlan.planId) 
          
        if (index == 1 ){
          form.cmbNewPlanTarifario.options[i+1]=form.opcion;          
        }else{
          form.cmbNewPlanTarifario[index-1].options[i+1]=form.opcion;          
        }      
      }
    }*/
    
    function fxRenumeric(tablename,count){    
      eval("var table = parent.mainFrame.document.all?parent.mainFrame.document.all['"+tablename+"']:parent.mainFrame.document.getElementById('"+tablename+"');");       
      var CellIndex;                         
      for(var i=1;i<=count;i++){              
        CellIndex =  table.rows[i].cells[0];  	  
        CellIndex.innerHTML= i;
      }
    }  
  </script>
  
  <script DEFER>
    function fxGetCheck(){
       return "<td>&nbsp;<input type=checkbox name=chkPhoneNumber disabled onClick='javascript:fxCheckedItem(this.checked,this.parentNode.parentNode.rowIndex)' /> <input type=hidden name=hdnchkPhone /><input type=hidden name=hdnGetServicio /> </td>";	
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
	    return "<td><input type=text name=txtItemSolution size=15 readonly/><input type=hidden name=hdnItemSolutionId /><input type=hidden name=hdnItemEquipment /><input type=hidden name=txtItemModel /></td>";		
    }
  </script>  
  
  <script DEFER>
    function fxGetPlan(){
	    return "<td><input type=text name=txtItemOrigPlan size=20 readonly/><input type=hidden name=hdnItemOrigPlanId /><input type=hidden name=hdnItemOrigProductId /><input type=hidden name=hdnItemOrigProductLineId /></td>";		
    }
  </script>
  
  <script DEFER>
    function fxGetPlanNew(){
	    return "<td><input type=text name=cmbNewPlanTarifario size=20 readonly/><input type=hidden name=hdncmbNewPlanTarifario /></td>";		
    }
  </script>  
  
  <script DEFER>
    function fxGetLinea(){
	    return "<td><input type=text name=txtItemLine size=10 readonly/><input type=hidden name=hdnItemLine /><input type=hidden name=hdnItemNewProductId /><input type=hidden name=hdnItemNewProductLineId /><input type=hidden name=hdnItemNewPlanId /><input type=hidden name=hdnItemNewPlanNameId /></td>";		
    }
  </script>
  
  <script DEFER>
    function fxGetPrecio(){
	    return "<td><input type=text name=txtItemPrice size=4 readonly/><input type=hidden name=hdnItemPrice /><input type=hidden name=hdnOriginalPrice /><input type=hidden name=hdnPriceType /><input type=hidden name=hdnPriceTypeId /><input type=hidden name=hdnItemPriceTypeId /></td>";		
    }
  </script>
  
  <script DEFER>
    function fxGetPrecioExc(){
	    return "<td><input type=text name=txtItemPriceExc size=5 readonly/></td>";		
    }
  </script>
  
  <script DEFER>
    function fxGetEstado(){
	    return "<td><input type=text name=txtItemStatus size=8 readonly/><input type=hidden name=hdnIndice /></td>";		
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

  
<script defer>
  
  function fxMakePlan(planId,plandes){
    this.planId        = planId;
    this.plandes       = plandes;
   
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
   
  function fxCreateRow(){        
     //Inserto una nueva fila
    var row      = parent.mainFrame.table_assignment.insertRow(-1);
     
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
     
    /*cell = row.insertCell(i++);
    cell.className = 'CellContent';
    elemText = fxGetModel();
    cell.innerHTML = elemText;*/
     
    cell = row.insertCell(i++);
    cell.className = 'CellContent';
    elemText = fxGetPlan();
    cell.innerHTML = elemText;
    
    cell = row.insertCell(i++);
    cell.className = 'CellContent';
    elemText = fxGetPlanNew();
    cell.innerHTML = elemText;
     
    cell = row.insertCell(i++);
    cell.className = 'CellContent';
    elemText = fxGetLinea();
    cell.innerHTML = elemText;
    
    cell = row.insertCell(i++);
    cell.className = 'CellContent';
    elemText = fxGetPrecio();
    cell.innerHTML = elemText;
    
    cell = row.insertCell(i++);
    cell.className = 'CellContent';
    elemText = fxGetPrecioExc();
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
    
    fxRenumeric("table_assignment",parent.mainFrame.table_assignment.rows.length-1);  
    //fxLoadPlanTransfer();

  }
  
  function fxLoadItemsMassiveTransfer(){
    parent.mainFrame.idItemsMassiveTransfer.innerHTML = "";
    parent.mainFrame.idItemsMassiveTransfer.innerHTML = txtItemsMassiveTransfer.value;
     
    var form = parent.mainFrame.document.frmdatos;
    var index = parent.mainFrame.table_assignment.rows.length;
      
    
    <%
      System.out.println("en la listaaaa====");
      if ( objArrayPlanList != null && objArrayPlanList.size() > 0){
      System.out.println("dentro del ifff");
        PlanTarifarioBean planTarifarioBean = null;           
        for( int i=0; i<objArrayPlanList.size();i++ ){
          planTarifarioBean = new PlanTarifarioBean();
          planTarifarioBean = (PlanTarifarioBean)objArrayPlanList.get(i);
          DecimalFormat decFormat = new DecimalFormat("###");
    %>
          
          fxAddNewOption(parent.mainFrame.document.frmdatos.cmb_PlanTarifario,'<%=decFormat.format(planTarifarioBean.getNpplancode())%>','<%=planTarifarioBean.getNpdescripcion()%>'); 
          //parent.mainFrame.vplan.addElement(new fxMakePlan("<%=decFormat.format(planTarifarioBean.getNpplancode())%>","<%=planTarifarioBean.getNpdescripcion()%>"));
          
    <%
        }
    }
    %>
    
    fxLoadServiceAditional();
    
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
          form.hdnItemLine.value = "ERROR"; //"<%=MiUtil.getString(objMassiveItemBean.getNpproductlinename())%>"; 
          form.hdnItemMsjResSSAA.value = "ERROR";
          form.hdnItemPrice.value = "<%=MiUtil.getString(objMassiveItemBean.getNpprice())%>";
          form.hdnOriginalPrice.value = "<%=MiUtil.getString(objMassiveItemBean.getNporiginalprice())%>";
          form.hdnPriceType.value = "<%=MiUtil.getString(objMassiveItemBean.getNppricetype())%>";
          form.hdnPriceTypeId.value = "<%=MiUtil.getString(objMassiveItemBean.getNppricetypeid())%>";
          form.hdnItemPriceTypeId.value = "<%=MiUtil.getString(objMassiveItemBean.getNppricetypeitemid())%>";     
          form.hdnItemNewProductId.value = "<%=MiUtil.getString(objMassiveItemBean.getNpproductid())%>";
          form.hdnItemNewProductLineId.value = "<%=MiUtil.getString(objMassiveItemBean.getNpproductlineid())%>";
          form.hdnItemNewPlanId.value = "<%=MiUtil.getString(objMassiveItemBean.getNporiginalplanid())%>";
          form.hdnItemNewPlanNameId.value = "<%=MiUtil.getString(objMassiveItemBean.getNporiginalplanname())%>";
          
          form.hdnIndice.value= "<%=objArrayItemOrder.size()%>";  
          form.hdnGetServicio.value= "false"; 
   
          
                <%
                  if (!MiUtil.getString(objMassiveItemBean.getNpcontractstatus()).equals("Suspended") ||
                      (MiUtil.getString(objMassiveItemBean.getNpcontractstatus()).equals("Suspended") && 
                       MiUtil.getString(objMassiveItemBean.getNpcountreasonId()).equals("1"))
                      ){          
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
          form.hdnItemLine[index-1].value = "ERROR"; //"<%=MiUtil.getString(objMassiveItemBean.getNpproductlinename())%>"; 
          form.hdnItemMsjResSSAA[index-1].value = "ERROR";
          form.hdnItemPrice[index-1].value = "<%=MiUtil.getString(objMassiveItemBean.getNpprice())%>";
          form.hdnOriginalPrice[index-1].value = "<%=MiUtil.getString(objMassiveItemBean.getNporiginalprice())%>";
          form.hdnPriceType[index-1].value = "<%=MiUtil.getString(objMassiveItemBean.getNppricetype())%>";
          form.hdnPriceTypeId[index-1].value = "<%=MiUtil.getString(objMassiveItemBean.getNppricetypeid())%>";
          form.hdnItemPriceTypeId[index-1].value = "<%=MiUtil.getString(objMassiveItemBean.getNppricetypeitemid())%>";
          form.hdnItemNewProductId[index-1].value = "<%=MiUtil.getString(objMassiveItemBean.getNpproductid())%>";
          form.hdnItemNewProductLineId[index-1].value = "<%=MiUtil.getString(objMassiveItemBean.getNpproductlineid())%>";
          form.hdnItemNewPlanId[index-1].value = "<%=MiUtil.getString(objMassiveItemBean.getNporiginalplanid())%>";
          form.hdnItemNewPlanNameId[index-1].value = "<%=MiUtil.getString(objMassiveItemBean.getNporiginalplanname())%>";
          
          form.hdnIndice[index-1].value= "<%=objArrayItemOrder.size()%>"; 
          form.hdnGetServicio[index-1].value= "false";
          
                <% /*System.out.println("ssaa=================="+objMassiveItemBean.getNpssaacontratado());
                   System.out.println("SOLUCION=============="+objMassiveItemBean.getNpsolutionid());
                   System.out.println("PRECIO=============="+objMassiveItemBean.getNpprice());
                   System.out.println("PRECIO ORIGINAL=============="+objMassiveItemBean.getNporiginalprice());
                   System.out.println("TIPO PRECIO=============="+objMassiveItemBean.getNppricetype());
                   System.out.println("PRODUCT ID =============="+objMassiveItemBean.getNpproductid());
                   System.out.println("PRODUCTLINE ID =============="+objMassiveItemBean.getNpproductlineid());*/
                   /*System.out.println("PRODUCTLINE ID =============="+objMassiveItemBean.getNpcontractstatus());
                   System.out.println("PRODUCTLINE ID =============="+objMassiveItemBean.getNpcountreasonId());*/
                  if (!MiUtil.getString(objMassiveItemBean.getNpcontractstatus()).equals("Suspended") ||
                      (MiUtil.getString(objMassiveItemBean.getNpcontractstatus()).equals("Suspended") &&
                       MiUtil.getString(objMassiveItemBean.getNpcountreasonId()).equals("1"))
                     ){          
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
    fxFinishItemsLoad();
  }
  
  function fxAddNewOption(TheCmb, Value, Description) {
    var option = new Option(Description, Value);
    var i = TheCmb.options.length;
    TheCmb.options[i] = option;
  }
   
  function fxFinishItemsLoad(){
    location.replace("<%=Constante.PATH_APPORDER_SERVER%>/Bottom.html");
  }  
  
  onload = fxLoadItemsMassiveTransfer;    
</script>

