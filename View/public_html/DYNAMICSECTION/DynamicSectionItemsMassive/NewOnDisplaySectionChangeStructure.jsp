<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="pe.com.nextel.service.NewOrderService" %>
<%@page import="pe.com.nextel.service.BillingAccountService" %>
<%@page import="pe.com.nextel.service.CustomerService" %>
<%@page import="pe.com.nextel.service.MassiveOrderService" %>
<%@page import="pe.com.nextel.service.GeneralService" %>
<%@page import="pe.com.nextel.util.MiUtil" %>
<%@page import="pe.com.nextel.util.Constante" %>
<%@page import="pe.com.nextel.bean.BillingAccountBean" %>
<%@page import="pe.com.nextel.bean.SiteBean" %>
<%@page import="pe.com.nextel.bean.ItemBean" %>
<%
  try{
  Hashtable hshtinputNewSection = null;
  String    strCustomerId= "",
            strnpSite ="",
            strCodBSCS = "",
            hdnSpecification = "",
            //strSolution = "",
            strTypeCompany = "",
            strOrderId  = "",
            strCustomerStruct = "",
            strGeneratorId  = "",
            strGeneratorType = "";				
	
  String strCustomerType="";  //CEM COR0802
  String strClienteId="";     //CEM COR0802
  CustomerService  objCustomerService  = new CustomerService();	 //CEM COR0802
  
  hshtinputNewSection     = (Hashtable)request.getAttribute("hshtInputNewSection");
  
  System.out.println("Cambio Estructura - hshtinputNewSection ===> "+hshtinputNewSection);
  
  if ( hshtinputNewSection != null ) {
  
    strCustomerId        =   (String)hshtinputNewSection.get("strCustomerId");
    strnpSite            =   (String)hshtinputNewSection.get("strSiteId");
    strCodBSCS           =   (String)hshtinputNewSection.get("strCodBSCS");
    hdnSpecification     =   (String)hshtinputNewSection.get("strSpecificationId");    
    strTypeCompany       =   (String)hshtinputNewSection.get("strTypeCompany");
    strOrderId           =   (String)hshtinputNewSection.get("strOrderId");
	  strGeneratorId       = 	 (String)hshtinputNewSection.get("strGeneratorId");
	  strGeneratorType  	 =	 (String)hshtinputNewSection.get("strGeneratorType");	
   //strSolution         =   (String)hshtinputNewSection.get("strSolution");
	 
    //Validación si el cliente no tiene un site
    if(strnpSite.equals("")) strnpSite="0";
   
      //Inicio CEM - COR0802
	  if(strnpSite.equals("0")) {
		strCustomerType=Constante.CUSTOMERTYPE_CUSTOMER; // Si el valor es 0 no ingreso Site en la pantalla principal
		strClienteId=strCustomerId;
	  }
	  else{
		strCustomerType=Constante.CUSTOMERTYPE_SITE;
		strClienteId=strnpSite;
	  }	  	
      //Fin CEM - COR0802    
  }
  
  
 
%>

<%
  HashMap objHashMap        = new HashMap();
  HashMap objHashItemOrder  = new HashMap();
  HashMap objHashGeneric    = new HashMap();
  
  
  ArrayList objArraySiteExistsList    = new ArrayList();
  ArrayList objArraySiteSolicitedList = new ArrayList();
  ArrayList objArrayItemOrder         = new ArrayList();
  ArrayList objArrayGeneric           = new ArrayList();
  
  ArrayList objArrayBillingAccountExitstList    = new ArrayList();
  ArrayList objArrayBillingAccountSolicitedList = new ArrayList();
  
  NewOrderService       objNewOrderService       = new NewOrderService();
  BillingAccountService objBillingAccountService = new BillingAccountService();
  MassiveOrderService   objItemMassiveOrderService  = new MassiveOrderService();
  GeneralService        objGeneralService       = new GeneralService();
 
  
  String strMessage = null;
  
  /**Obtenemos los Sites existentes**/
  objHashMap = (new NewOrderService()).SiteDAOgetSiteExistsList(MiUtil.parseLong(strCustomerId),strCustomerType);
  if( objHashMap.get("strMessage") != null )
      throw new Exception((String)objHashMap.get("strMessage"));
  objArraySiteExistsList = (ArrayList)objHashMap.get("objArrayList");
  
  if( objArraySiteExistsList!=null ) strCustomerStruct = objArraySiteExistsList.size()==0?"FLAT":"LARGE"; 
  
  /**Obtenemos los Sites solicitados**/
  objHashMap = (new NewOrderService()).SiteDAOgetSiteSolicitedList(MiUtil.parseLong(strOrderId));
  if( objHashMap.get("strMessage") != null )
      throw new Exception((String)objHashMap.get("strMessage"));
  objArraySiteSolicitedList = (ArrayList)objHashMap.get("objArrayList");
  
  /**Sección de Cuentas de Facturación**/
  long lCustomerIdBSCS   = (new CustomerService()).getCustomerIdBSCS(MiUtil.parseLong(strClienteId),strCustomerType);		 
  System.out.println("lCustomerIdBSCS: "+lCustomerIdBSCS+"");
  
  if( MiUtil.getString(strCustomerStruct).equals("FLAT") ){
  //Obtener los billing account existentes del customer
  objHashMap = (new BillingAccountService()).BillingAccountDAOgetBillingAccountList(lCustomerIdBSCS);
  if( objHashMap.get("strMessage") != null )
      throw new Exception((String)objHashMap.get("strMessage"));
  
  objArrayBillingAccountExitstList = (ArrayList)objHashMap.get("objArrayList");
  
  //Obtener los billing account solicitados del customer
  objHashMap = (new BillingAccountService()).BillingAccountDAOgetAccountList(MiUtil.parseLong(strnpSite),MiUtil.parseLong(strCustomerId),MiUtil.parseLong(strOrderId));
  if( objHashMap.get("strMessage") != null )
      throw new Exception((String)objHashMap.get("strMessage"));
  
  objArrayBillingAccountSolicitedList = (ArrayList)objHashMap.get("objArrayList");
  }
  
   //Se obtiene la información de Cliente ingresado
   
   objHashItemOrder     = objItemMassiveOrderService.MassiveOrderDAOgetItemOrder(MiUtil.parseLong(strCustomerId), MiUtil.parseLong(strnpSite), MiUtil.parseLong(hdnSpecification),0);
   //System.out.println("objHashItemOrder ==> ["+objHashItemOrder+"]");
   strMessage=(String)objHashItemOrder.get("strMessage");
   System.out.println("Valores Mensaje================="+strMessage);
   if (strMessage!=null)
     throw new Exception(strMessage);
     
   objArrayItemOrder = (ArrayList)objHashItemOrder.get("objArrayList");
   
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
            // Valor por defaul
              numMaxRecord = Constante.MASSIVE_MAX_RECORD_DEFAULT;
      }
  
      System.out.println("numMaxRecord "+numMaxRecord);
     
%>

  <table border="0" cellspacing="0" cellpadding="0" width="40%">
     <tr> 
        <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"></td>
        <td class="SubSectionTitle" align="left" valign="top">Cuenta Facturaci&oacute;n en Detalle</td>
        <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"> &nbsp;</td>
        <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
        <td align="right">    
        </td>
     </tr>     
  </table>
  
  <input type=hidden name=indItems />
  <input type=hidden name=txtOrigFactura / >
  <!--<input type=hidden name=hdnOrigResP / >-->
  <input type=hidden name=hdnOrigFac / >
  <input type=hidden name=hdnFlagES / >
  <input type=hidden name=hdnContractId / >
  <input type=hidden name=hdnSnCodeId / >
  <input type=hidden name=cmbNewFactura / >
  <input type=hidden name=hdnOrigFactura / > 
  <input type=hidden name=hdnNewResponsablePago / > 
  <input type=hidden name=hdnNewFactura / > 		
  <input type=hidden name=hdnNumRegistros / > 
  
  <!--<div style="OVERFLOW: auto; WIDTH: 540px; HEIGHT: 114px">-->
  <div style="OVERFLOW: auto; WIDTH: 80%; HEIGHT: 300px">
      <table  border="0" width="40%" cellpadding="2" cellspacing="1" class="RegionBorder">
         <tr>
           <td>   
              <table border="0" width="100%" cellpadding="2" cellspacing="1">                     
                <tr>
                   <td class="CellContent" align="center" colspan="2">&nbsp;Nuevo Responsable de Pago&nbsp;</td>                                   
                   <td class="CellContent" align="center" colspan="2">&nbsp;<select name=cmbNewResponsablePagoAplicar >
                                                                                <option>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
                                                                            </select>
                                                                            <input type="button" name="Aplicar" value="Aplicar" onclick="fxAplicar();" >
                   </td>  
                </tr>            
              </table>
           </td>
          </tr>  
         <tr>
           <td>
           <table id="table_assignment" name="table_assignment" border="0" width="100%" cellpadding="2" cellspacing="1">                     
                  <tr id="cabecera">
                      <td class="CellLabel" align="center">&nbsp;#&nbsp;</td>
                      <td class="CellLabel" align="center">&nbsp;<input type=checkbox name=chkPhoneNumberAll onclick="checkedAll();">&nbsp;</td>
                      <td class="CellLabel" align="center">&nbsp;Teléfono&nbsp;</td>
                      <td class="CellLabel" align="center">&nbsp;Responsable de Pago&nbsp;</td>                  
                      <td class="CellLabel" align="center">&nbsp;Nuevo Responsable de Pago&nbsp;</td>                
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
  
  <br>

 <script defer>
 
 function fxCreateRowAssignment(){ 
   
     var row      = table_assignment.insertRow(-1);
     var elemText =  "";
     var cell = "";
     var i = 0;
     
     cell = row.insertCell(i++);
     cell.className = 'CellContent';
     cell.innerHTML = '';
     
     cell = row.insertCell(i++);
     cell.className = 'CellContent';
     elemText = fxGetCheckPhone();
     cell.innerHTML = elemText;
     
     cell = row.insertCell(i++);
     cell.className = 'CellContent';
     elemText = fxGetTelefono();
     cell.innerHTML = elemText;
     
     cell = row.insertCell(i++);
     cell.className = 'CellContent';
     elemText = fxGetResponsablePago();
     cell.innerHTML = elemText;
   
     cell = row.insertCell(i++);
     cell.className = 'CellContent';
     elemText = fxGetNewResponsablePago();
     cell.innerHTML = elemText;
     
     fxLoadSiteData();     
     //fxLoadBillingAccountData();
     
     fxRenumeric("table_assignment",table_assignment.rows.length-1);
 }

function fxRenumeric(tablename,count){    
   eval("var table = document.all?document.all['"+tablename+"']:document.getElementById('"+tablename+"');");       
   var CellIndex;                         
   for(var i=1;i<=count;i++){              
      CellIndex =  table.rows[i].cells[0];  	  
      CellIndex.innerHTML= i;
   }
}  
 
	function fxSelectResposablePago(opcion){    
   //Pendiente optimizar - se repite en NewOnDisplaySectionAssignmentBillingAccount
		var form = document.frmdatos;
		<%	String strOrigenOrden="";
			String strCampo1="";
			if (strGeneratorType.equals(Constante.GENERATOR_TYPE_OPP)){
		%>	
				var sOrigenOrden  = "<%=Constante.GENERATOR_TYPE_OPP%>";
				var sCampo1			= "<%=strGeneratorId%>";
		<%	}
			else{
				if (strGeneratorType.equals(Constante.GENERATOR_TYPE_INC)){
		%>	
					var sOrigenOrden  = "<%=Constante.GENERATOR_TYPE_INC%>";
		<%		}
				else{
		%>		
					var sOrigenOrden  = "ORD"; //opcion Ordenes CREAR
		<%		}
		%>		
				var sCampo1			= form.hdnNumeroOrder.value;				
		<% }		
		%>
		
		var url="category="+"<%=hdnSpecification%>"+"&origen="+sOrigenOrden+"&campo1="+sCampo1+"&opcion="+opcion+"&strCustomerId=<%=strCustomerId%>";
		url = url + "&strTypeCompany=<%=strTypeCompany%>";
    var iPermitir = fxRequestXML("generalServlet","requestRespPago",url);
      if (iPermitir ==null )
         iPermitir=-1;
      return iPermitir;		
	} 
 
  function fxLoadSiteData(){    
    var form = parent.mainFrame.document.frmdatos;
    var objSite = null;
    
    for(i=0; i <vctSite.size(); i++){
        objSite =  vctSite.elementAt(i);
        opcion=new Option( objSite.siteStatus + " - " + objSite.siteName , objSite.siteId )         
        form.cmbNewResponsablePagoAplicar.options[i+1]=opcion;
     }     
  }
  
  /***********************************************/
  /*function fxLoadSiteData01(){  
    var cantRowsTable = table_assignment.rows.length;
    var form          = parent.mainFrame.document.frmdatos; 
    var objSite = null;   
    
    for(x=1; x<cantRowsTable; x++){
      var indexDefault = form.cmbNewResponsablePagoAplicar.value;
      DeleteSelectOptions(form.cmbNewResponsablePagoAplicar);
      for(i=0; i <vctSite.size(); i++){
        objSite =  vctSite.elementAt(i);
        opcion=new Option( objSite.siteStatus + " - " + objSite.siteName , objSite.siteId )
        form.cmbNewResponsablePagoAplicar.options[i+1]=opcion;
      }
        form.cmbNewResponsablePagoAplicar.value = indexDefault;
    }
  }*/
  
  /***********************************************/
 /**Permite cargar todos los nuevos reponsables de pago
    asignados en la sección de responsables de pago   
 **/
 function fxReloadedSite(){    
    var form          = parent.mainFrame.document.frmdatos;   
    var objSite = null;
    
    var indexDefault = form.cmbNewResponsablePagoAplicar.value;
    DeleteSelectOptions(form.cmbNewResponsablePagoAplicar);
          
     for(i=0; i <vctSite.size(); i++){
          objSite =  vctSite.elementAt(i);
          opcion=new Option( objSite.siteStatus + " - " + objSite.siteName , objSite.siteId )    
          form.cmbNewResponsablePagoAplicar.options[i+1]=opcion;
     } 
  }
 </script>
 <script DEFER>  
    function checkedAll () {         
          var form = document.frmdatos; 
          var index = table_assignment.rows.length;
         
          if (index > 2){
            for (var i =0; i < index - 1; i++){
                // Validar que el chk se encuentre activo y que el chk principal tambien esta activo
                 if (form.chkPhoneNumberAll.checked == true && form.chkPhoneNumber[i].disabled == false){
                      form.chkPhoneNumber[i].checked = true;
                      form.hdnChkPhoneNumber[i].value = 'on';
                 }else{
                      form.chkPhoneNumber[i].checked = false;                      
                      form.hdnChkPhoneNumber[i].value = 'off';
                 }
            }
          }    
      }
  </script>
  <script DEFER>   
    function fxCheckedItem( objValue, objIndex ){   
        var numRows = table_assignment.rows.length;
        var form    = document.frmdatos;
        var index   = objIndex - 1;

      if (numRows > 2){
        if (objValue == true){
              form.hdnChkPhoneNumber[index].value = "on";
              //form.hdnCmbNewResponsablePago[index].value = form.cmbNewResponsablePago[index].value;
          }else{
              form.hdnChkPhoneNumber[index].value = "off";
          } 
      }
    }  
 </script>
 
 <script DEFER>
     function fxAplicar () {
           var form = document.frmdatos; 
           var index = table_assignment.rows.length;
           var count = 0;
           
           if (index > 2){              
               for (var i =0; i < index - 1 ; i++){
                   if (form.chkPhoneNumber[i].disabled == false && form.chkPhoneNumber[i].checked == true){                   
                      
                    //  alert('form.cmbNewResponsablePagoAplicar.value['+form.cmbNewResponsablePagoAplicar.value+']');                      
                    //  alert(form.cmbNewResponsablePagoAplicar.options[form.cmbNewResponsablePagoAplicar.selectedIndex].text);
                      
                      form.txtNewResponsablePago[i].value    = form.cmbNewResponsablePagoAplicar.options[form.cmbNewResponsablePagoAplicar.selectedIndex].text;
                      form.hdnTxtNewResponsablePago[i].value = form.cmbNewResponsablePagoAplicar.value;
                      
                      
                      //form.cmbNewResponsablePago[i].selected;                   
                   }/*else{
                      form.cmbNewResponsablePago[i].value = null;                      
                      form.cmbNewResponsablePago[i].selected;                   
                   }*/
                   
                   if (form.hdnTxtNewResponsablePago[i].value != ''){                   
                        count++;                        
                   }
                   
               }              
          }         
          form.txtNumRecord.value = '  '+count;
      }
 </script>
  <script DEFER>
     /*function fxAsignarHidden() {         
          var form = document.frmdatos; 
          var index = table_assignment.rows.length;
          if (index > 1){
            for (var i =0; i < index - 1; i++){
                // Validar que el chk se encuentre activo y que el chk principal tambien esta activo
                 if (form.chkPhoneNumber[i].disabled == false){
                     form.hdnCmbNewResponsablePago[i].value = form.cmbNewResponsablePago[i].value;                   
                 }else{
                     form.hdnCmbNewResponsablePago[i].value = '';//null;
                 }
            }
          }    
      }*/
 </script>
 
 <script DEFER>
      function fxGetTelefono(){      
            return "<td> <input type=text name=txtPhoneNumber size=12 readonly /> <input type=hidden name=hdnNpcontractnumber /> <input type=hidden name=hdnBscspaymntrespcustomeridId /> </td>";		
            <!--return "<td><input type=hidden name=hdnNewFactura><input type=hidden name=hdnNewResponsablePago><input type=hidden name=hdnOrigFactura><input type=hidden name=hdnFlagES><input type=hidden name=hdnSnCodeId><input type=hidden name=hdnContractId><input type=hidden name=indItems><td><input type=text name=txtPhoneNumber size=12></td>";-->
      }
 </script>  
 <script DEFER> 
     function fxGetCheckPhone(){                  
            return "<td>&nbsp;<input type=checkbox name=chkPhoneNumber disabled onClick=fxCheckedItem(this.checked,this.parentNode.parentNode.rowIndex) /> <input type=hidden name=hdnChkPhoneNumber /> </td>";		
     }
 </script> 
 <script DEFER> 
     function fxGetResponsablePago(){
        return "<td><input type=text name=txtOrigResponsablePago size=60 readonly /><input type=hidden name=hdnOrigResP /></td>";
     }
 </script>
 <script DEFER> 
     function fxGetOrigFactura(){
        var cadena = "<td>"
        cadena+="<select value=0 name=txtOrigFactura>";
        cadena+="<option>";
        cadena+="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
        cadena+="</option>";
        cadena+="</select>";
        cadena+="</td>";    
        return cadena;
     }
 </script> 
 <script DEFER>
      function fxGetNewResponsablePago(){
        return "<td><input type=text name=txtNewResponsablePago size=60 readonly /><input type=hidden name=hdnTxtNewResponsablePago /></td>";
     }
 </script>
 <script DEFER> 
      function fxGetNewFactura(){
        var cadena = "<td>"
        cadena+="<select value=0 name=cmbNewFactura>";
        cadena+="<option>";
        cadena+="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
        cadena+="</option>";
        cadena+="</select>";
        cadena+="</td>";    
        return cadena;        
      }
 </script>
 <script DEFER> 
     function fxGetGUIDelete(){
        var cadena = "<td>"
        cadena+="<a href='javascript:fVoid()' onclick='javascript:fxDeleteRegister(this,this.parentNode.parentNode.rowIndex);'  ><img 	src='<%=Constante.PATH_APPORDER_SERVER%>/images/Eliminar.gif' border='no' ALT='Borrar Item'></a>";	
        cadena+="</td>";    
        return cadena;  
      }  
 </script> 
 <script defer="defer">
      function fxLoadBASite(siteId,index){
        form = parent.mainFrame.frmdatos;
        var typeCustomer = '<%=strCustomerStruct%>';
        var index =  Number(index) + 1;//+1;     
        var pageBA = siteId<0?'New':'Old';
        siteId = siteId<0?(siteId*-1):siteId;
        var url = "<%=request.getContextPath()%>/customerservlet?myaction=loadBillingAccountBySiteId&strSiteId="+siteId+"&strCustomerId=<%=strCustomerId%>&strOrderId=<%=strOrderId%>&strPage="+pageBA+"&strIndex="+index+"&typeCustomer="+typeCustomer;
        parent.bottomFrame.location.replace(url);
      }
 </script>
 
 <script DEFER>
 var vctSite            = new Vector();
 var vctBillingAccount  = new Vector();
 function fxOnLoadSectionChangeStructure(){
    fxLoadResponsablePagos();
    //fxLoadBillingAccount();
    fxLoadValores();
    //fxLoadSiteData01();
 }
 
 function fxLoadResponsablePagos(){
     <% if( objArraySiteExistsList != null ){
          SiteBean objSiteBean =  null;
          System.out.println("Site objArraySiteExistsList : " + objArraySiteExistsList.size() );
          for(int i=0; i<objArraySiteExistsList.size(); i++ ) {  
              objSiteBean = new SiteBean();
              objSiteBean = (SiteBean)objArraySiteExistsList.get(i);
      %>
          vctSite.addElement(new fxMakeSite("<%=objSiteBean.getSwSiteId()*-1%>","<%=objSiteBean.getSwSiteName()%>","<%=objSiteBean.getSwRegionName()%>","<%=objSiteBean.getNpStatus()%>")); 
     <% }}%>
 
     <% if( objArraySiteSolicitedList != null ){
          SiteBean objSiteBean =  null;
          System.out.println("Site objArraySiteSolicitedList : " + objArraySiteSolicitedList.size() );
          for(int i=0; i<objArraySiteSolicitedList.size(); i++ ) {  
              objSiteBean = new SiteBean();
              objSiteBean = (SiteBean)objArraySiteSolicitedList.get(i);
      %>  
           vctSite.addElement(new fxMakeSite("<%=(objSiteBean.getSwSiteId()*-1)%>","<%=objSiteBean.getSwSiteName()%>","<%=objSiteBean.getSwRegionName()%>","<%=objSiteBean.getNpStatus()%>"));           
      <%}}%>
 }
 
  function fxInputTabEnter() {
	  return ((window.event.keyCode == 9) || (window.event.keyCode == 13));
  }
 
  function fxGetDataPhone(objPhone){
   if(fxInputTabEnter()) { 
    form = document.frmdatos;  
    phoneNumber  = objPhone.value;
    currentIndex = objPhone.parentNode.parentNode.rowIndex;	
    //Inicio CEM COR0459
    form.txtPhoneNumber[currentIndex].value=trim(form.txtPhoneNumber[currentIndex].value);
    if (form.txtPhoneNumber[currentIndex].value!=""){
      var url = "<%=request.getContextPath()%>/serviceservlet?myaction=loadDataPhone&indexActual="+currentIndex+"&pCustomerId="+"<%=strCustomerId%>"+"&pSiteId="+<%=strnpSite%>+"&hdnSpecification="+<%=hdnSpecification%>+"&strOrderId="+<%=strOrderId%>;
      form.action = url;  
      form.submit();
    }  
   }
 }
 
 function fxGetDataPhone2(objPhone){
    form = document.frmdatos;  
    phoneNumber  = objPhone.value;
    currentIndex = objPhone.parentNode.parentNode.rowIndex;	
    //Inicio CEM COR0459
    form.txtPhoneNumber[currentIndex].value=trim(form.txtPhoneNumber[currentIndex].value);
    if (form.txtPhoneNumber[currentIndex].value!=""){
      var url = "<%=request.getContextPath()%>/serviceservlet?myaction=loadDataPhone&indexActual="+currentIndex+"&pCustomerId="+"<%=strCustomerId%>"+"&pSiteId="+<%=strnpSite%>+"&hdnSpecification="+<%=hdnSpecification%>+"&strOrderId="+<%=strOrderId%>;
      form.action = url;  
      form.submit();
    }  
 }
 
 function fxMakeSite(siteId,siteName,siteRegionName,siteStatus){
    //this.siteId         = siteId;
    this.siteId         = siteId<0?(siteId*-1):siteId;
    this.siteName       = siteName;
    this.siteRegionName = siteRegionName;
    this.siteStatus     = siteStatus;
 }
 
 function fxMakeBilling(baId,baName,baStatus){
    this.baId         = baId;
    this.baName       = baName;
    this.baStatus     = baStatus;
 }
 
 function fVoid(){}
 
 function fxDeleteRegister(objThis, objThisRow){    
    if( confirm("¿Está usted seguro que desea eliminar este registro?") ) {
    table_assignment.deleteRow(objThisRow);
    fxRenumeric("table_assignment",table_assignment.rows.length-1);
    }
    
 }
 
//INICIO CEM - COR354
function fxMakeObjNewRespPago(siteId,  siteName,  siteRegionName,  siteStatus){		         
         //this.siteId     		    =   siteId;
         this.siteId            = siteId<0?(siteId*-1):siteId;
         this.siteName        	=   siteName;
         this.siteRegionName    =   siteRegionName;
         this.siteStatus        =   siteStatus;
}  


function fxTransferNewRespPagoVector(vctMain){  
        var vctAux = new Vector();          
          for( x = 0; x < vctMain.size(); x++ ){
      
              var objMake = new fxMakeObjNewRespPago(
              vctMain.elementAt(x).siteId,
              vctMain.elementAt(x).siteName,
              vctMain.elementAt(x).siteRegionName,
              vctMain.elementAt(x).siteStatus);		
              vctAux.addElement(objMake);        
          } 
        parent.mainFrame.vctSite=vctAux;   
}

function fxMakeObjBA(baId,baName,baStatus){		
    this.baId     		=   baId;
    this.baName      	=   baName;
    this.baStatus 		=   baStatus;
}  

//BA: BillingAccount
function fxTransferBAVector(vctBAMain){
  
	var vctBAAux = new Vector();      
      
    for( x = 0; x < vctBAMain.size(); x++ ){

        var objBAMake = new fxMakeObjBA(
        vctBAMain.elementAt(x).baId,
        vctBAMain.elementAt(x).baName,
        vctBAMain.elementAt(x).baStatus);	
        vctBAAux.addElement(objBAMake);        
	} 
	parent.mainFrame.vctBillingAccount=vctBAAux;   
}
//FIN CEM - COR354

 function fxLoadValores(){       
       
       var form = document.frmdatos; 
       var index = table_assignment.rows.length; // + 1;
       
       <% if (objArrayItemOrder!= null){
            ItemBean objItemBean=null;   
            
            System.out.println("objArrayContactList.size() ==> "+objArrayItemOrder.size());
            
            for(int i=0; i<objArrayItemOrder.size(); i++ ) {
              objItemBean = new ItemBean();
              objItemBean = (ItemBean)objArrayItemOrder.get(i);     
              
              if (!MiUtil.getString(objItemBean.getNpcontractstatus()).equals("Deactive")){
        %>
        
              fxCreateRowAssignment();
              
               if (index  < 2){  
                     index = index + 1;                                  
                     form.txtPhoneNumber.value                = "<%=MiUtil.getString(objItemBean.getNpphone())%>";
                     form.txtOrigResponsablePago.value        = "<%=objItemBean.getNppayrespname()%> - <%=objItemBean.getNpcustcode()%>";
                     form.hdnNpcontractnumber.value           = "<%=objItemBean.getNpcontractnumber()%>";
                     form.hdnBscspaymntrespcustomeridId.value = "<%=objItemBean.getNpbscspaymntrespcustomeridId()%>";
                     form.hdnOrigResP.value                   = "<%=objItemBean.getNpcustcode()%>";
                     
                     
                     <%
                        if (!MiUtil.getString(objItemBean.getNpcontractstatus()).equals("Suspended")){          
                     %>
                          form.chkPhoneNumber.disabled  = false;
                          //form.cmbNewResponsablePago.disabled  = false;                          
                     <%}%>                        
                          form.hdnChkPhoneNumber.value = "off"; 
                     
               }else{               
                     form.txtPhoneNumber[index-1].value                = "<%=MiUtil.getString(objItemBean.getNpphone())%>"; 
                     form.txtOrigResponsablePago[index-1].value        = "<%=objItemBean.getNppayrespname()%> - <%=objItemBean.getNpcustcode()%>";
                     form.hdnNpcontractnumber[index-1].value           = "<%=objItemBean.getNpcontractnumber()%>";
                     form.hdnBscspaymntrespcustomeridId[index-1].value = "<%=objItemBean.getNpbscspaymntrespcustomeridId()%>";
                     form.hdnOrigResP[index-1].value                   = "<%=objItemBean.getNpsitename()%>";
                     
                     <%
                        if (!MiUtil.getString(objItemBean.getNpcontractstatus()).equals("Suspended")){          
                     %>
                          form.chkPhoneNumber[index-1].disabled  = false;  
                          //form.cmbNewResponsablePago[index-1].disabled  = false;
                     <%}%>                        
                     
                          form.hdnChkPhoneNumber[index-1].value = "off";                          
                     
                      index = index + 1;                     
               }
              
         <%}}}%>
         
         <% System.out.println("objArrayItemOrder.size() ===========>["+objArrayItemOrder.size()); %>
         
  }
  
  
  function fxOnValidateChangeStructure(){ 
      var form = document.frmdatos; 
      var index = table_assignment.rows.length;
      var lastIndex = table_assignment.rows.length -2;
      var existeRespon = 0;
      var numMaxItems = <%=numMaxRecord%>;
      
       if (index > 2){          
            if(form.txtNumRecord.value > numMaxItems){
                alert('No se puede enviar mas de ['+numMaxItems+'] Registros');
                return false;
            }
          
             for (var i =0; i < index - 1; i++){  
                 if (form.hdnTxtNewResponsablePago[i].value != ''){
                 //if (form.hdnCmbNewResponsablePago[i].value != ''){
                    existeRespon = 1;
                }
           
                if (form.chkPhoneNumber[i].checked == true && (form.hdnTxtNewResponsablePago[i].value == '' || form.hdnTxtNewResponsablePago[i].value == null )){                                                                                         
               // if (form.chkPhoneNumber[i].checked == true && (form.hdnCmbNewResponsablePago[i].value == '' || form.hdnCmbNewResponsablePago[i].value == null )){                                                                                         
                      alert('Debe Asignar al Responsable de Pago');
                      return false;
                }
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
 
 <%}catch(Exception e){%>
  <script>alert('<%=MiUtil.getString(e.getMessage())%>');</script>
 <%}%>