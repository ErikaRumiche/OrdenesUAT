<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Vector"%>
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
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.text.NumberFormat"%>
<%@ page import="pe.com.nextel.service.*"%>
<%@ page import="pe.com.nextel.bean.*"%>
<%@ page import="pe.com.nextel.bean.PlanTarifarioBean" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>


   <link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css">
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>             
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderIXEdit.js"></script>
    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsItemsEdit.js"></script>
     <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderGeneral.js"></script>

 <%
 
 try{ 
   NewOrderService  objNewOrderService  = new NewOrderService();
   SessionService   objSession          = new SessionService();
   NumberFormat formatter = new DecimalFormat("#0.00");     
   ProductBean objProdBean = new ProductBean();
   OrderBean   objOrderBean = null;  
   
   Hashtable hshtinputNewSection;
   HashMap objHashGeneric    = new HashMap();
    hshtinputNewSection     = (Hashtable)request.getAttribute("hshtInputNewSection");
    String URL_SSAA         = request.getContextPath()+"/DYNAMICSECTION/DynamicSectionItemsMassive/DynamicSectionItemsMassivePage/PopUpServices.jsp";
    String strRutaContext                = request.getContextPath();
    String actionURL_MassiveOrderServlet = strRutaContext+"/massiveorderServlet";
    HashMap hshScreenField=null;
    HashMap hshOrder=null;
	  HashMap hshItemEditable=null;
    ArrayList objItemHEader = new ArrayList();
    ArrayList objArrayListServiceBySolution = new ArrayList();
    ArrayList objArrayGeneric           = new ArrayList();
   String    strCustomerId= "",
             strnpSite ="",
             strCodBSCS = "",
             hdnSpecification = "",
             strDivision ="",
             strOrderId  = "",
             strStatus   = "",
              strPlan  = "",
             strSiteOppId = "",
             strUnknwnSiteId = "";
   String    strGeneratorType= "";
   long      lGeneratorId;
   
            
           
   int   strValiditem=0;

   String	strNumPaymentOrderId=""; //CEM COR0323
   String	strNumGuideNumber=""; 	 //CEM COR0426
   String   strDeleteItem="";
   String   strSessionid=null;
  boolean	bolInboxConExcepcion=false;
  boolean bShowExceptionButton = false,        
             flgSA = false,
             flgIMEIS = false;
   SpecificationBean objSpecificationBean= null;
   
  
      GeneralService objGeneralService= new GeneralService(); 
      MassiveOrderService objMassiveOrderService= new MassiveOrderService(); 
      String strMessage=null;
      String strSolution     = "3";
      HashMap hshData=null;
       ArrayList arrLista=new ArrayList();
       String type = request.getParameter("strTypeCompany");
       //String strTypeCompany= "EMPLOYEE";
       PlanTarifarioBean pBean = new PlanTarifarioBean();
       ArrayList objArrayItemOrder         = new ArrayList();
     
     //Arrays para servicios
      ArrayList objArrayListService  = new ArrayList();
     //ArrayList objArrayListServDefault = new ArrayList();
    
     
     
    // System.out.println("hshtinputNewSection ["+hshtinputNewSection);
     
    /* if(true){
      throw new Exception();
     }
     
     */
     
    if ( hshtinputNewSection != null ) {
      strCustomerId           =   MiUtil.getString((String)hshtinputNewSection.get("strCustomerId"));
      strnpSite               =   MiUtil.getString((String)hshtinputNewSection.get("strSiteId"));
      strCodBSCS              =   MiUtil.getString((String)hshtinputNewSection.get("strCodBSCS"));
      hdnSpecification        =   MiUtil.getString((String)hshtinputNewSection.get("strSpecificationId"));
      strDivision             =   MiUtil.getString((String)hshtinputNewSection.get("strDivisionId"));
      strOrderId              =   MiUtil.getString((String)hshtinputNewSection.get("strOrderId"));
      strSessionid            =   MiUtil.getString((String)hshtinputNewSection.get("strSessionId"));
      strStatus               =   MiUtil.getString((String)hshtinputNewSection.get("strStatus"));  
      strGeneratorType        =   MiUtil.getString((String)hshtinputNewSection.get("strGeneratorType"));
      lGeneratorId            =   MiUtil.parseLong((String)hshtinputNewSection.get("strGeneratorId")); 
      
      strSiteOppId            =   MiUtil.getString((String)hshtinputNewSection.get("strSiteOppId")).equals("null")?"":MiUtil.getString((String)hshtinputNewSection.get("strSiteOppId"));
      strUnknwnSiteId         =   MiUtil.getString((String)hshtinputNewSection.get("strUnknwnSiteId")).equals("null")?"":MiUtil.getString((String)hshtinputNewSection.get("strUnknwnSiteId"));;
      
      objItemHEader           =   objNewOrderService.ItemDAOgetHeaderSpecGrp(MiUtil.parseInt(hdnSpecification));
      request.setAttribute("objItemHEader",objItemHEader);
      request.setAttribute("strDivisionId",strDivision);
      
   }
   
  
   //Se obtiene la información de Cliente ingresado
     HashMap objHashItemOrder = objMassiveOrderService.MassiveOrderDAOgetItemOrder(MiUtil.parseLong(strCustomerId),MiUtil.parseLong(strnpSite),MiUtil.parseLong(hdnSpecification),0); 
     strMessage=(String)objHashItemOrder.get("strMessage");
     //System.out.println("Valores Mensaje items================="+strMessage);
     if (strMessage!=null)
     throw new Exception(strMessage);
     //System.out.println("Items items=>>>>>>"+objArrayItemOrder.size());
     
      /* Se Obtiene el numero el maximos numero de registros que se podra almacenar */
   int numMaxRecord = 0;
   objHashGeneric = objGeneralService.getTableList("MASSIVE_MAX_RECORDS","1"); 
   strMessage=(String)objHashGeneric.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage); 
      
    objArrayGeneric=(ArrayList)objHashGeneric.get("arrTableList");
    if( objArrayGeneric.size() > 0 ){
        HashMap hsMapMaxRecord = new HashMap();               
        for(int i=0; i<objArrayGeneric.size(); i++ ) {  
                hsMapMaxRecord = (HashMap)objArrayGeneric.get(i);                  
                System.out.println("Valor de HM "+hsMapMaxRecord.get("wv_npValue"));                   
                numMaxRecord = Integer.parseInt((String)hsMapMaxRecord.get("wv_npValue"));                   
        }
      }else {
            // Valor por defaul
              numMaxRecord = Constante.MASSIVE_MAX_RECORD_DEFAULT;
      }
  
     
  %> 
  <div id="plan">
 <table  border="0" width="40%" cellpadding="2" cellspacing="1" class="RegionBorder">
<td class="Celllabel" colspan="3">&nbsp; Plan Tarifario &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<select name="cmb_PlanTarifario" style="width:30%;" >                         
</select> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
   <input type="button" name="Aplicar" value="Aplicar" onclick="fxAplicar();" >
   <input type="button" name="Limpiar" value="Limpiar" onclick="fxCleanPlan();" >
   <input type="hidden" name="hndcmb_PlanTarifario">
 </td> 
</table>       
 </div>   
  
 

 
  
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
                             <input type="button" value="Aplicar" name="btnApplyServiceMassive" onclick="javascript:fxApplyServiceMassive();"><p> 
                             <br>
                            <input type="button" value="Limpiar" name="btnCleanServiceMassive" onclick="javascript:fxCleanSSAA();">    
                           </td> 
                        </tr>                       
                     </table>
                  </td>
               </tr>
            </table>
            </div>
         </div>   <!-- de " div Servicios" -->  
         
      <br>
      
      <input type="HIDDEN" name="hdnNpOrderId" value="<%=strOrderId%>"> 
      <input type="HIDDEN" name="hdnSpecificationId" value="<%=hdnSpecification%>">
      <input type=hidden name=hdnNumRegistros / > 
   
 <div style="OVERFLOW: auto; WIDTH: 90%; HEIGHT: 600px"> 
  <table  border="0" width="90%" cellpadding="2" cellspacing="1" class="RegionBorder">
  <tr valign="top">
    <td class="regionheadercolor" width="100%" align="center">
      <table border="0" width="100%" cellspacing="0" cellpadding="0">
        <tr>
          <td width="100%">
            <table border="0" width="100%" cellspacing="0" cellpadding="0">
              <tr>
                <td width="100%">
                  <input type="hidden" name="hdnItemRequeridoFlag" value="0"/>
                  <input type="hidden" name="hdnItemBorrados" value=""/>
                  <table id="items_table" name="items_table" border="0" cellpadding="2" cellspacing="1" width="100%">
                    <tr>
                      <td align="center" class="CellLabel">&nbsp;&nbsp;&nbsp;</td>
                      <td align="center" class="CellLabel">
                      <input name="chkPhoneAll" type="checkbox" onclick="javascript:checkedAll();"/></td>
                      <td align="left" class="CellLabel">&nbsp;<b><font style="color: rgb(190, 0, 0);"> </font></b>Tel&eacute;fono&nbsp; </td>
                      <td align="left"  class="CellLabel">&nbsp; <b><font style="color: rgb(190, 0, 0);"> </font></b>Radio&nbsp;</td>
                      <td align="left"  class="CellLabel">&nbsp;<b><font style="color: rgb(190, 0, 0);"> </font></b>Soluci&oacute;n&nbsp;</td>
                      <td align="left"  class="CellLabel">&nbsp;<b><font style="color: rgb(190, 0, 0);"> </font></b>Modelo&nbsp;</td>
                      <td align="left"  class="CellLabel">&nbsp;<b><font style="color: rgb(190, 0, 0);"> </font></b>Plan Tarifario Orig&nbsp;</td>
                      <td align="left"  class="CellLabel">&nbsp;<b><font style="color: rgb(190, 0, 0);"> </font></b>Plan Tarifario&nbsp;</td>
                      <td align="left"  class="CellLabel">&nbsp; <b><font style="color: rgb(190, 0, 0);"> </font></b>Estado&nbsp;</td>
                      <td align="left"  class="CellLabel">&nbsp;<b><font style="color: rgb(190, 0, 0);"> </font></b>SSAA/Act.&nbsp;</td>
                      <td align="left"  class="CellLabel">&nbsp; <b><font style="color: rgb(190, 0, 0);"> </font></b>SSAA/Desac.&nbsp;</td>
                      <td align="left" class="CellLabel" >&nbsp;Msj. Resultado&nbsp;</td> 
                      <td align="left" class="CellLabel" >&nbsp;Ver SSAA&nbsp;</td> 
                      
                    </tr>
                   </td>           
                </table>
                 </td>
                 <tr>
              </tr>
              </tr>
          </table>
          </td>
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
<script DEFER>
 function fxGetTelefono(){      
      return "<td class=CellLabel><input type=text name=txtItemPhoneNumber size=12 readonly/><input type=hidden name=hdnItemSimNumber /><input type=hidden name=hdnItemImeiNumber /><input type=hidden name=hdnItemCoId /><input type=hidden name=hdnItemModality /><input type=hidden name=hdnItemCurrency /><input type=hidden name=hdnItemId /></td>";		
 }
 </script>
  
 <script DEFER> 
  function fxGetRadio(){
    return "<td><input type=text name=txtRadio size=15 readonly /><input type=hidden name=hdnItemQuantity /></td>";
 }
 </script>
 
<script DEFER> 
  function fxGetSolucion(){
    return "<td><input type=text name=txtItemSolution size=12 readonly /><input type=hidden name=hdnItemSolutionId /></td>";
 }
 </script>
 
 
 <script DEFER> 
  function fxGetPlan(){
    return "<td><input type=text name=txtItemOrigPlan size=25 readonly /></td>";
 }
 </script>
 
<script DEFER> 
  function fxGetModelo(){
    return "<td><input type=text name=txtItemModel size=15 readonly /><input type=hidden name=hdnmodelo /><input type=hidden name=hdnItemProductId /><input type=hidden name=hdnItemProductLineId /><input type=hidden name=hdnItemOrigProductId /><input type=hidden name=hdnItemEquipment /></td>";
 }
  </script>
 

<script DEFER> 
  function fxGetEstado(){
    return "<td><input type=text name=txtestado size=8 readonly /><input type=hidden name=hdnestado /><input type=hidden name=hdnIndice /></td>";
 }
 </script> 
 
 <script DEFER>
 function fxGetCheckbox(){
    return "<td><input type=checkbox name=chkPhoneNumber onClick=checkedItem(this.checked,this.parentNode.parentNode.rowIndex)  /><input type=hidden name=hdnchkPhone /><input type=hidden name=hdnGetServicio /></td>";
   
 }
 </script>
 
<script DEFER> 
 function fxGetActiva(){
     return "<td><input type=text name=txtItemActSSAA size=8 readonly/><input type=hidden name=hdnItemActSSAA /></td>";	
   
 }
 </script> 
 
<script DEFER> 
 function fxGetDesactiva(){
    return "<td><input type=text name=txtItemDesSSAA size=8 readonly/><input type=hidden name=hdnItemDesSSAA /></td>";
   
 }
</script>

 <script DEFER>
 function fxGetSSAA(){
	    return "<td><input type=button name=txtItemSSAA size=30 value=........... onclick='javascript:fxShowSSAA(this,this.parentNode.parentNode.rowIndex);'/><input type=hidden name=hdnItemServices /><input type=hidden name=hdnItemOrigPlanId /><input type=hidden name=hdnItemServicesNew /></td>";		
    }  
 </script>
 
 <script DEFER>
    function fxGetMessageResultSSAA(){
      return "<td><input type=text name=txtItemMsjResSSAA  size=50 readonly/><input type=hidden name=hdnItemMsjResSSAA /><input type=hidden name=hdnItemNewPlanId /><input type=hidden name=hdnItemNewPlanNameId /></td>";
    }  
  </script>
 
 <script DEFER>
    function fxGetNewPlan(){
      return "<td><input type=text name=txtNewPlan  size=25 disabled /></td>";
    }  
  </script>
 
 
<script DEFER> 
    function fxGetNewPlanTarifario(){
        var cadena = "<td>"
        cadena+="<select value=0 name=cmbNewPlanTarifario disabled onchange=changeplan(this.parentNode.parentNode.rowIndex)>";      
        cadena+="<option>";
        cadena+="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
        cadena+="</option>";
        cadena+="</select>";
        cadena+="</td>";    
        return cadena;
      }
  
 </script>
 
 
 <script DEFER>    
      function checkedItem ( objValue, objIndex) {         
         var numRows = items_table.rows.length;
         var form    = document.frmdatos;
         var index   = objIndex - 1;
         if (numRows > 2){                    
             if (objValue == true){
                form.hdnchkPhone[index].value = 'on';                             
                //form.hdnItemOrigPlanId[i].value = form.cmbNewPlanTarifario[i].value;//reemplazar por el otro plan
               // form.hdnItemNewPlanId[index].value = form.cmbNewPlanTarifario[index].value; //se eliminaria
          }else{
                form.hdnchkPhone[index].value = 'off';    
          } 
      }    
        
       if (form.hdnItemNewPlanId[index].value == ''){
           form.hdnItemNewPlanId[index].value='0';
         }  
        
      }
      
    
 </script>
 
 
  <script DEFER>    
      function changeplan (objIndex) {         
        //alert('Entro1');
        
         var numRows = items_table.rows.length;
         var form    = document.frmdatos;
         var index   = objIndex - 1;
         if (numRows > 2){                    
                 //alert ('Entro2');
                 //alert(form.cmbNewPlanTarifario[index].options[form.cmbNewPlanTarifario[index].selectedIndex].text);
                 //alert(form.cmbNewPlanTarifario[index].value);
                 form.hdnItemNewPlanId[index].value = form.cmbNewPlanTarifario[index].value;
                 form.hdnItemNewPlanNameId[index].value=form.cmbNewPlanTarifario[index].options[form.cmbNewPlanTarifario[index].selectedIndex].text;
                 form.cmbNewPlanTarifario[index].selected;
            
              
              //alert(form.hdnItemNewPlanId[index].value);
              //alert(form.hdnItemNewPlanNameId[index].value);
              
            // if (form.hdnItemNewPlanId[index].value == ''){
              //  form.hdnItemNewPlanId[index].value='0';
              //}     
          
             //alert(form.hdnItemNewPlanId[index].value);
           
      } 
      
       if (form.hdnItemNewPlanId[index].value == ''){
                form.hdnItemNewPlanId[index].value='0';
              }  
         
  }
      
    
 </script>
 
 
 <script DEFER>

   function fxMakePlan(planId,plandes){
    this.planId        = planId;
    this.plandes       = plandes;
   
 }
  
  
function fxAplicar () {
  var form = document.frmdatos; 
  var index = items_table.rows.length;
  var count = 0;        

        
    if (index > 1){
      fxMassiveServlet();
    for (var i =0; i < index - 1 ; i++){
  
      if (form.chkPhoneNumber[i].disabled == false && form.chkPhoneNumber[i].checked == true){   
                
                //fxMassiveServlet();

               //form.cmbNewPlanTarifario[i].value = form.cmb_PlanTarifario.value; se comento
               // form.txtNewPlan[i].value= form.cmb_PlanTarifario.value;
               
                form.txtNewPlan[i].value=form.cmb_PlanTarifario.options[form.cmb_PlanTarifario.selectedIndex].text
                form.hdnItemNewPlanId[i].value=form.cmb_PlanTarifario.value;
                form.hdnItemNewPlanNameId[i].value=form.txtNewPlan[i].value;
              
              
               
               // alert(form.cmbNewPlanTarifario[i].value);
              //alert(form.cmb_PlanTarifario.options[form.cmb_PlanTarifario.selectedIndex].text);
              // alert(form.cmbNewPlanTarifario[i].options[form.cmbNewPlanTarifario[i].selectedIndex].text);
             /* form.hdnItemNewPlanId[i].value=form.cmbNewPlanTarifario[i].value;
              form.hdnItemNewPlanNameId[i].value=form.cmbNewPlanTarifario[i].options[form.cmbNewPlanTarifario[i].selectedIndex].text;
              form.cmbNewPlanTarifario[i].selected;*/ //se quito
            
     
      }      
          if (form.hdnItemNewPlanId[i].value == ''){
                form.hdnItemNewPlanId[i].value='0';
              }     
        
     } //for
       
               
     }//if
  
   
}

</script>

 

  <script DEFER>
  
   function fxOnValidateChangePlan(){ 
      var form = document.frmdatos; 
      var index = items_table.rows.length;
      var lastIndex = items_table.rows.length -2;
      var existeRespon = 0;
      var numMaxItems = <%=numMaxRecord%>;
       if (index > 2){          
          if(form.txtNumRecord.value > numMaxItems){
                alert('No se puede enviar mas de ['+numMaxItems+'] Registros');
                return false;
            }
          
             for (var i =0; i < index - 1; i++){  
                  //alert(form.hdnItemNewPlanId[i].value);
                 if (form.hdnItemNewPlanId[i].value != '0' && form.hdnItemNewPlanId[i].value != '') {  
                 
                    existeRespon = 1;
                }
           
                 /*if (form.hdnItemNewPlanId[i].value != '') {  
                  alert('xxx4');
                    existeRespon = 1;
                }*/
                
              /*  if (form.chkPhoneNumber[i].checked == true && (form.hdnItemNewPlanId[i].value == '' || form.hdnItemNewPlanId[i].value == null )){                                                                                         
                      alert('Debe Asignar al Responsable de Pago');
                      return false;
                }*/
             }   
             
              if (existeRespon == 0){
                  alert('Debe completar al menos la Informacion de un Item');
                  return false;
              }  
              
             return true;
          }  else {
                alert('Solo se permite Ordenes para Clientes Mayores');
                return false;
          }
  }
 </script>
 
 
 <script DEFER>
  function fxLoadPlan(){
   
    var index =  items_table.rows.length-1;
    var form =   document.frmdatos;
 
    var objPlan = null;
    var nLength = vplan.size();   
    
     for(i=0; i<vplan.size(); i++){
     objPlan =  vplan.elementAt(i);
     form.opcion=new Option(objPlan.plandes,objPlan.planId) 
     
     
      if (index == 1 ){
               form.cmbNewPlanTarifario.options[i+1]=form.opcion;
               form.hdnItemNewPlanId.value='0';
               
            
             
        }else{
              form.cmbNewPlanTarifario[index-1].options[i+1]=form.opcion; 
              form.hdnItemNewPlanId[index-1].value='0';
        }      
    
     }
    
          
             
 }
</script>
  
<script DEFER>
 function fxCreateRowAssignment(){ 
  var row = items_table.insertRow(-1);
     
     var elemText =  "";
     var cell = "";
     var i = 0;
     
     cell = row.insertCell(i++);
     cell.className = 'CellContent';
     cell.innerHTML = '';
     
     cell = row.insertCell(i++);
     elemText = fxGetCheckbox();
     cell.innerHTML = elemText;
  
     cell = row.insertCell(i++);
     elemText = fxGetTelefono();
     cell.innerHTML = elemText;
     
     cell = row.insertCell(i++);
     elemText = fxGetRadio();
     cell.innerHTML = elemText;
     
     cell = row.insertCell(i++);
     elemText = fxGetSolucion();
     cell.innerHTML = elemText;
     
     cell = row.insertCell(i++);
     elemText = fxGetModelo();
     cell.innerHTML = elemText;
     
     
     cell = row.insertCell(i++);
     elemText = fxGetPlan();
     cell.innerHTML = elemText;
     
    /* cell = row.insertCell(i++);
     elemText = fxGetNewPlanTarifario();
     cell.innerHTML = elemText;*/
     
     cell = row.insertCell(i++);
     elemText = fxGetNewPlan();
     cell.innerHTML = elemText;
     
     cell = row.insertCell(i++);
     elemText = fxGetEstado();
     cell.innerHTML = elemText;
     
     cell = row.insertCell(i++);
     elemText = fxGetActiva();
     cell.innerHTML = elemText;
     
     cell = row.insertCell(i++);
     elemText = fxGetDesactiva();
     cell.innerHTML = elemText;
     
     cell = row.insertCell(i++);
     elemText = fxGetMessageResultSSAA();
     cell.innerHTML = elemText;
     
     cell = row.insertCell(i++);
     elemText = fxGetSSAA();
     cell.innerHTML = elemText;
     
     
 //  fxLoadPlan();
     
   fxRenumeric("items_table",items_table.rows.length-1);
 }

function fxRenumeric(tablename,count){    
   eval("var table = document.all?document.all['"+tablename+"']:document.getElementById('"+tablename+"');");       
   var CellIndex;                         
   for(var i=1;i<=count;i++){              
      CellIndex =  table.rows[i].cells[0];  	  
      CellIndex.innerHTML= i;
   }
   
 }
 
   
function checkedAll () {         
      var form = document.frmdatos; 
      var index = items_table.rows.length;
      if (index > 2){
             for (var i =0; i < index - 1; i++)  { 
           // Validar que el chk se encuentre activo y que el chk principal tambien esta activo
                  if (form.chkPhoneAll.checked == true && form.chkPhoneNumber[i].disabled == false){
                      form.chkPhoneNumber[i].checked = true;
                      form.hdnchkPhone[i].value = 'on';
                 }else{
                      form.chkPhoneNumber[i].checked = false;                      
                      form.hdnchkPhone[i].value = 'off';
                 }         
              }
          }    
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
     fxAddServiceMassive(listdestinyactive,listsource);
     fxAddServiceMassive(listdestinydesactive,listsource);
   }
   
   function fxCleanSSAA(){
   
    var index = items_table.rows.length;
      
      if (index > 1){
       for(var i=0; i < index - 1  ; i++){  
         if (form.chkPhoneNumber[i].disabled == false && form.chkPhoneNumber[i].checked == true){   
           form.txtItemActSSAA[i].value = "";
           form.txtItemMsjResSSAA[i].value = "";
           form.hdnItemMsjResSSAA[i].value = "ERROR";
           form.txtItemDesSSAA[i].value = "";
           form.hdnItemServicesNew[i].value = form.hdnItemServices[i].value;
         }
       }
      }
   
   }
   
    function fxCleanPlan(){
    var index = items_table.rows.length;
    
    if (index > 1){
      for(var i=0; i < index - 1  ; i++){  
        if (form.chkPhoneNumber[i].disabled == false && form.chkPhoneNumber[i].checked == true){   
          //form.cmbNewPlanTarifario[i].value = null; 
          form.txtNewPlan[i].value = '';
          form.hdnItemNewPlanId[i].value = '0'
          form.hdnItemNewPlanNameId.value='';
          form.txtItemActSSAA[i].value = "";
          form.txtItemMsjResSSAA[i].value = "";
          form.hdnItemMsjResSSAA[i].value = "ERROR";
          form.txtItemDesSSAA[i].value = "";
          form.hdnItemServicesNew[i].value = form.hdnItemServices[i].value;
        
        }
      }
    }
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
       alert("Falta Aplicar [PLAN TARIFARIO] a Servicios Adicionales");
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
         strExclude = objServicioPorSolucion.exclude;
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
     var index = items_table.rows.length;
     var bError;
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
           
           if(form.hdnItemNewPlanId[i].value == '0'){
             alert("Falta Aplicar [PLAN TARIFARIO] a Servicios Adicionales");
             return false;
           }
            
           if (form.hdnItemServices[i].value == ""){
             alert("Falta Aplicar [PLAN TARIFARIO] a Servicios Adicionales");
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
                 form.txtItemMsjResSSAA[i].style.color = "red";
                 bError = true;
                 //si hay error al momento de levantar el pop up sólo se vizualiza los ssaa que tiene el equipo
                 form.hdnItemServicesNew[i].value = form.hdnItemServices[i].value;
               }
             }
           }
           
         }else{//Caso cuando no no se selecciona equipos 
           /*form.txtItemActSSAA[i].value = "";
           form.txtItemDesSSAA[i].value = "";
           form.txtItemMsjResSSAA[i].value = "";
           form.hdnItemMsjResSSAA[i].value = "OFF";*/
         }
       }
     }
     
   }
   
   function fxMassiveServlet(){
        document.frmdatos.myaction.value = 'obtenerServiciosPlan';     
        form.action="<%=actionURL_MassiveOrderServlet%>";
        document.frmdatos.submit();              
    }
    
   
    
 </script>





<script DEFER>
 
 //Vector de Servicios
  var vServicio            = new Vector();
  var vServicioPorSolucion  = new Vector();
  var vplan =  new Vector();
  var str_list_act = "";
  var str_list_des = "";
  var str_list_final = "";
  var str_list_services = "";
 
function fxLoadItemsDataPlanTarif(){
    fxLoadComboPlan();
    fxLoadServiceAditiional();
    var form = document.frmdatos;
    var index = items_table.rows.length;
       
       <% objArrayItemOrder = (ArrayList)objHashItemOrder.get("objArrayList");
            if (objArrayItemOrder!= null){   
            ItemBean objItemMassiveBean=null;   
            
            System.out.println("objArrayItemOrder.size() ==> "+objArrayItemOrder.size());
            
         for(int i=0; i<objArrayItemOrder.size(); i++ ) {
              objItemMassiveBean = new ItemBean();
              objItemMassiveBean = (ItemBean)objArrayItemOrder.get(i);
              if (!objItemMassiveBean.getNpcontractstatus().equals("Deactive")){
            //  System.out.println("getNpimeinumber ["+objItemMassiveBean.getNpmodel()+"]["+objItemMassiveBean.getNporiginalplanname());
    %>
    
            
             fxCreateRowAssignment();
              
               if (index == 1){  
                 index = index + 1;                 
                 
                 form.txtItemPhoneNumber.value     = "<%=MiUtil.getString(objItemMassiveBean.getNpphone())%>"; 
                 form.txtRadio.value     = "<%=MiUtil.getString(objItemMassiveBean.getNpradio())%>";
                 form.txtItemSolution.value     = "<%=MiUtil.getString(objItemMassiveBean.getNpsolutionname())%>";
                 form.txtItemModel.value     = "<%=MiUtil.getString(objItemMassiveBean.getNpmodel())%>";
                 form.txtestado.value     = "<%=MiUtil.getString(objItemMassiveBean.getNpcontractstatus())%>";
                 form.hdnItemSimNumber.value ="<%=MiUtil.getString(objItemMassiveBean.getNpsiminumber())%>"; 
                 form.hdnItemImeiNumber.value ="<%=MiUtil.getString(objItemMassiveBean.getNpimeinumber())%>"; 
                 form.hdnItemCoId.value ="<%=MiUtil.getString(objItemMassiveBean.getNpcontractnumber())%>";           
                 form.hdnItemModality.value ="<%=MiUtil.getString(objItemMassiveBean.getNpmodalitysell())%>";    
                 form.hdnItemSolutionId.value = "<%=MiUtil.getString(objItemMassiveBean.getNpsolutionid())%>";
                 form.hdnItemOrigPlanId.value = "<%=MiUtil.getString(objItemMassiveBean.getNporiginalplanid())%>";
                 form.hdnItemProductId.value = "<%=MiUtil.getString(objItemMassiveBean.getNpproductid())%>";
                 form.hdnItemProductLineId.value = "<%=MiUtil.getString(objItemMassiveBean.getNpproductlineid())%>";
                 form.hdnItemCurrency.value = "<%=MiUtil.getString(objItemMassiveBean.getNpcurrency())%>";
                 form.hdnItemQuantity.value = "<%=MiUtil.getString(objItemMassiveBean.getNpquantity())%>";
                 form.txtItemOrigPlan.value = "<%=MiUtil.getString(objItemMassiveBean.getNporiginalplanname())%>";
                form.hdnItemEquipment.value ="<%=MiUtil.getString(objItemMassiveBean.getNpequipment())%>";     
                 //form.hdnItemOrigProductId.value = "<%=MiUtil.getString(objItemMassiveBean.getNpproductid())%>";
                 form.hdnIndice.value= "<%=objArrayItemOrder.size()%>";  
                // form.cmbNewPlanTarifario.value= "<%=MiUtil.getString(objItemMassiveBean.getNporiginalplanid())%>";
                 form.hdnchkPhone.value= "off"; 
                 form.hdnGetServicio.value= "false"; 
                 
                   <%
                  if (MiUtil.getString(objItemMassiveBean.getNpcontractstatus()).equals("Suspended")|| MiUtil.getString(objItemMassiveBean.getNpcontractstatus()).equals("Other")){          
                %>
                      form.chkPhoneNumber.disabled  = true;
                <%}%>
                 
               }else{
               
                 form.txtItemPhoneNumber[index-1].value     = "<%=MiUtil.getString(objItemMassiveBean.getNpphone())%>"; 
                 form.txtRadio[index-1].value     = "<%=MiUtil.getString(objItemMassiveBean.getNpradio())%>";
                 form.txtItemSolution[index-1].value     = "<%=MiUtil.getString(objItemMassiveBean.getNpsolutionname())%>";
                 form.txtItemModel[index-1].value    = "<%=MiUtil.getString(objItemMassiveBean.getNpmodel())%>";
                 form.txtestado[index-1].value   = "<%=MiUtil.getString(objItemMassiveBean.getNpcontractstatus())%>";
                 form.hdnItemSimNumber[index-1].value ="<%=MiUtil.getString(objItemMassiveBean.getNpsiminumber())%>"; 
                 form.hdnItemImeiNumber[index-1].value ="<%=MiUtil.getString(objItemMassiveBean.getNpimeinumber())%>"; 
                 form.hdnItemCoId[index-1].value ="<%=MiUtil.getString(objItemMassiveBean.getNpcontractnumber())%>";           
                 form.hdnItemModality[index-1].value ="<%=MiUtil.getString(objItemMassiveBean.getNpmodalitysell())%>";   
                 form.hdnItemSolutionId[index-1].value = "<%=MiUtil.getString(objItemMassiveBean.getNpsolutionid())%>";
                 form.hdnItemOrigPlanId[index-1].value = "<%=MiUtil.getString(objItemMassiveBean.getNporiginalplanid())%>";
                 form.hdnItemProductId[index-1].value = "<%=MiUtil.getString(objItemMassiveBean.getNpproductid())%>";
                 form.hdnItemProductLineId[index-1].value = "<%=MiUtil.getString(objItemMassiveBean.getNpproductlineid())%>";
                 form.hdnItemCurrency[index-1].value = "<%=MiUtil.getString(objItemMassiveBean.getNpcurrency())%>";
                 form.hdnItemQuantity[index-1].value = "<%=MiUtil.getString(objItemMassiveBean.getNpquantity())%>";
                 form.txtItemOrigPlan[index-1].value = "<%=MiUtil.getString(objItemMassiveBean.getNporiginalplanname())%>";
                 form.hdnItemEquipment[index-1].value ="<%=MiUtil.getString(objItemMassiveBean.getNpequipment())%>";
                 //form.hdnItemOrigProductId[index-1].value = "<%=MiUtil.getString(objItemMassiveBean.getNpproductid())%>";
                 //form.cmbNewPlanTarifario[index-1].value= "<%=MiUtil.getString(objItemMassiveBean.getNporiginalplanid())%>";
                 form.hdnIndice[index-1].value= "<%=objArrayItemOrder.size()%>"; 
                 
                 
                 form.hdnchkPhone[index-1].value= "off";  
                 form.hdnGetServicio[index-1].value= "false";
                  <% 
                  if (MiUtil.getString(objItemMassiveBean.getNpcontractstatus()).equals("Suspended")|| MiUtil.getString(objItemMassiveBean.getNpcontractstatus()).equals("Other")){          
                %>
                      form.chkPhoneNumber[index-1].disabled  = true;
                <%}%>
                
              
                 index = index + 1;
               }
              
     <%}
         }
  }       %>
     
  }
  
  
 function fxAddNewOption(TheCmb, Value, Description) {
        var option = new Option(Description, Value);
        var i = TheCmb.options.length;
        TheCmb.options[i] = option;
    }
  
  
  function fxLoadComboPlan(){ 
  
 

   <%  
                pBean.setNptipo2("0");
                pBean.setNpsolutionid(MiUtil.parseInt(String.valueOf(pBean.getNpsolutionid())));
                pBean.setNpspecificationid(2013);
                
                hshData = (HashMap)(new NewOrderService()).PlanDAOgetPlanList(pBean,type);
                if( hshData.get("strMessage")!= null ) {
                  throw new ServletException((String) hshData.get("strMessage"));
                }
              
                  ArrayList objArrayList = (ArrayList)hshData.get("objPlanList");
                  if ( objArrayList != null && objArrayList.size() > 0){
                  PlanTarifarioBean planTarifarioBean = null;     
               
                  for( int i=0; i<objArrayList.size();i++ ){
                   planTarifarioBean = new PlanTarifarioBean();
                   planTarifarioBean = (PlanTarifarioBean)objArrayList.get(i);
                   DecimalFormat decFormat = new DecimalFormat("###");
                
 %>          
             fxAddNewOption(document.frmdatos.cmb_PlanTarifario,'<%=decFormat.format(planTarifarioBean.getNpplancode())%>','<%=planTarifarioBean.getNpdescripcion()%>');
             vplan.addElement(new fxMakePlan("<%=decFormat.format(planTarifarioBean.getNpplancode())%>","<%=planTarifarioBean.getNpdescripcion()%>")); 
              
         
  <%} 
                }%>
 
  }
  
  
 function fxLoadServiceAditiional(){ 
 
    var vServicioPorDefecto  = new Vector();
   
  <%  
  
     strDivision="1";
     HashMap objHashServiceList  = objMassiveOrderService.getServiceList(MiUtil.parseInt(strDivision), MiUtil.parseInt(strPlan));
     strMessage=(String)objHashServiceList.get("strMessage");
     //System.out.println("Valores Mensaje Servicios================="+strMessage);
     if (strMessage!=null)
     throw new Exception(strMessage);
     //System.out.println("division================="+strDivision);
     //System.out.println("Ruta================="+actionURL_MassiveOrderServlet);
      
     //Obtenemos los SSAA por solución
      HashMap hServiceBySolutionList  = objMassiveOrderService.getServiceListBySolution(MiUtil.parseInt(strDivision));
 
     strMessage = (String)hServiceBySolutionList.get("strMessage");          
      //System.out.println("[objServiceAditional][strMessage]"+strMessage); 
      if (strMessage!=null)
      throw new Exception(strMessage);
     
     
 
      
      objArrayListService =(ArrayList)objHashServiceList.get("objServiceList");
     if ( objArrayListService != null && objArrayListService.size() > 0 ){
      ServiciosBean serviciosBean = null;
      serviciosBean = (ServiciosBean)objArrayListService.get(0);
       int longMaxServices = MiUtil.getString(serviciosBean.getNpnomserv()).length();
       for( int j = 1; j <objArrayListService.size(); j++ ){
         serviciosBean = (ServiciosBean)objArrayListService.get(j);
          if( MiUtil.getString(serviciosBean.getNpnomserv()).length() > longMaxServices )
            longMaxServices =MiUtil.getString(serviciosBean.getNpnomserv()).length();
       }
       
       %>
      longMaxServices = '30<%//=longMaxServices%>';
       <%
       
       for( int i = 0; i < objArrayListService.size(); i++ ){
        
          serviciosBean = (ServiciosBean)objArrayListService.get(i);
          /*System.out.println("[fxLoadServiceAditiional][getNpservicioid]"+serviciosBean.getNpservicioid());
          System.out.println("[fxLoadServiceAditiional][getNpnomserv]"+serviciosBean.getNpnomserv());
          System.out.println("[fxLoadServiceAditiional][getNpnomcorserv]"+serviciosBean.getNpnomcorserv());
          System.out.println("[fxLoadServiceAditiional][getNpexcludingind]"+serviciosBean.getNpexcludingind());*/
    

          %>
          
         fxAddNewOption(document.frmdatos.cmbAvailableServicesMassive,'<%=serviciosBean.getNpservicioid()%>','<%=serviciosBean.getNpnomserv()%>');
         vServicio.addElement(new Servicio("<%=serviciosBean.getNpservicioid()%>","<%=serviciosBean.getNpnomserv()%>","<%=serviciosBean.getNpnomcorserv()%>","<%=MiUtil.getString(serviciosBean.getNpexcludingind())%>"));
       
       
       <% }
       
     }  %>
    
       <% //System.out.println("xxxxxxxxxxxxxxxxxx" + (ArrayList)hServiceBySolutionList.get("objServiceBySolutionList"));
        objArrayListServiceBySolution =(ArrayList)hServiceBySolutionList.get("objServiceBySolutionList");  
        if (objArrayListServiceBySolution!= null && objArrayListServiceBySolution.size() > 0){
        ServiciosBean serviciosBySolution = null;
        for( int i = 1; i < objArrayListServiceBySolution.size(); i++ ){
          serviciosBySolution = (ServiciosBean)objArrayListServiceBySolution.get(i);
          /*System.out.println("[fxLoadServiceAditiional][getNpsolutionid]"+serviciosBySolution.getNpsolutionid());
          System.out.println("[fxLoadServiceAditiional][getNpservicioid]"+serviciosBySolution.getNpservicioid());
          System.out.println("[fxLoadServiceAditiional][getNpexcludingind]"+serviciosBySolution.getNpexcludingind());        */ 
          %>
          vServicioPorSolucion.addElement(new ServicioPorSolucion("<%=serviciosBySolution.getNpsolutionid()%>","<%=serviciosBySolution.getNpservicioid()%>","<%=MiUtil.getString(serviciosBySolution.getNpexcludingind())%>","<%=serviciosBySolution.getNpnomcorserv()%>"));
        <%}
      }%>
 } 


 </script>
 
 
 <script defer>

 function fxValidateMassiveDataSSAA(){
    var form = document.frmdatos; 
    var index = items_table.rows.length - 2;
    var lastIndex = items_table.rows.length - 2;
   
      for (var i =0; i <= index ; i++){ 
       
            if (form.txtItemMsjResSSAA[i].value != "" && form.txtItemMsjResSSAA[i].value == "SUCESS"){    
            
              return true;
            }else{           
            if ( i == lastIndex ){
            alert ("Debe Activar o Desactivar SSAA con [SUCESS]");
            return false;
          }   
            }
      }
  }
</script>


 
<% }
catch(Exception  e){                
   System.out.println("Error try catch-->"+MiUtil.getMessageClean(e.getMessage())); }

%>

