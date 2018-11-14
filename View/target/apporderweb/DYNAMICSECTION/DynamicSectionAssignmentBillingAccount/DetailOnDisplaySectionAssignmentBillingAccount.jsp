<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="pe.com.nextel.service.NewOrderService" %>
<%@page import="pe.com.nextel.service.BillingAccountService" %>
<%@page import="pe.com.nextel.service.CustomerService" %>
<%@page import="pe.com.nextel.util.MiUtil" %>
<%@page import="pe.com.nextel.util.Constante" %>
<%@page import="pe.com.nextel.bean.BillingAccountBean" %>
<%@page import="pe.com.nextel.bean.SiteBean" %>
<%@page import="pe.com.nextel.bean.CoAssignmentBean" %>
<%
  try{
  Hashtable hshtinputNewSection = null;
  String    strCodigoCliente= "",
            strnpSite ="",
            strCodBSCS = "",
            hdnSpecification = "",
            //strSolution = "",
            strOrderId  = "",
            strCustomerStruct = "";
  
  String strCustomerType="";  //CEM COR0802
  String strClienteId="";     //CEM COR0802
  CustomerService  objCustomerService  = new CustomerService();	 //CEM COR0802
  
  hshtinputNewSection     = (Hashtable)request.getAttribute("hshtInputDetailSection");
  
  if ( hshtinputNewSection != null ) {
  
  strCodigoCliente        =   (String)hshtinputNewSection.get("strCustomerId");
  strnpSite               =   (String)hshtinputNewSection.get("strSiteId");
  strCodBSCS              =   (String)hshtinputNewSection.get("strCodBSCS");
  hdnSpecification        =   (String)hshtinputNewSection.get("strSpecificationId");
  //strSolution             =   (String)hshtinputNewSection.get("strSolutionId");
  strOrderId              =   (String)hshtinputNewSection.get("strOrderId");
  
  }
  
  //Validación si el cliente no tiene un site
  if(strnpSite.equals("")) strnpSite="0";
  System.out.println("Site EditOnDisplaySectionAssignmentBilliangAccount:"+strnpSite+"*****");
  System.out.println("CustomerId EditOnDisplaySectionAssignmentBilliangAccount: "+strCodigoCliente+"*****");
  
  //Inicio CEM - COR0802
  if(strnpSite.equals("0")) {
  strCustomerType=Constante.CUSTOMERTYPE_CUSTOMER; // Si el valor es 0 no ingreso Site en la pantalla principal
  strClienteId=strCodigoCliente;
  }
  else{
  strCustomerType=Constante.CUSTOMERTYPE_SITE;
  strClienteId=strnpSite;
  } 
  
  System.out.println("strCustomerType: "+strCustomerType);
  System.out.println("strCodigoCliente: "+strCodigoCliente);	
  //Fin CEM - COR0802
  
%>

<%
  HashMap   objHashMap = new HashMap();
  ArrayList objArraySiteExistsList    = new ArrayList();
  ArrayList objArraySiteSolicitedList = new ArrayList();
  
  ArrayList objArrayBillingAccountSolicitedList = new ArrayList();
  ArrayList objArrayBillingAccountExitstList    = new ArrayList();
  
  ArrayList objArrayCoAssignment          = new ArrayList();
  HashMap   objHashMapService             = new HashMap();
  CoAssignmentBean objAuxCoAssignmentBean = null;
  
  /**Obtenemos los Sites existentes**/
  objHashMap = (new NewOrderService()).SiteDAOgetSiteExistsList(MiUtil.parseLong(strCodigoCliente),strCustomerType); 
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
  
  /**Para el caso de que el cliente sea un FLAT**/
  if( MiUtil.getString(strCustomerStruct).equals("FLAT") ){
  //Obtener los billing account existentes del customer
  objHashMap = (new BillingAccountService()).BillingAccountDAOgetBillingAccountList(lCustomerIdBSCS);
  if( objHashMap.get("strMessage") != null )
      throw new Exception((String)objHashMap.get("strMessage"));
  
  objArrayBillingAccountExitstList = (ArrayList)objHashMap.get("objArrayList");
  
  //Obtener los billing account solicitados del customer
  objHashMap = (new BillingAccountService()).BillingAccountDAOgetAccountList(MiUtil.parseLong(strnpSite),MiUtil.parseLong(strCodigoCliente),MiUtil.parseLong(strOrderId));
  if( objHashMap.get("strMessage") != null )
      throw new Exception((String)objHashMap.get("strMessage"));
  
  objArrayBillingAccountSolicitedList = (ArrayList)objHashMap.get("objArrayList");
  }
  
  /**Obtengo las asociaciones de cuenta para esta orden**/
  objHashMap = (new BillingAccountService()).BillingAccountDAOgetCoAssignmentList(MiUtil.parseLong(strOrderId));
  if( objHashMap.get("strMessage") != null )
      throw new Exception((String)objHashMap.get("strMessage"));
  objArrayCoAssignment = (ArrayList)objHashMap.get("objArrayList");
  /*
  HashMap          objHashMapGet = null;
  
  //Si el array tiene data. Llenamos un HashMap con toda la data
  for(int i=0; i<objArrayCoAssignment.size(); i++){
    objAuxCoAssignmentBean  = new CoAssignmentBean();
    objAuxCoAssignmentBean = (CoAssignmentBean)objArrayCoAssignment.get(i);
    objHashMapGet  = new HashMap();
    objHashMapGet  = ( new BillingAccountService()).BillingAccountDAOgetCoAssignmentBillingByContract(MiUtil.parseLong(objAuxCoAssignmentBean.getNpbscscontractId()));
    objHashMapService.put(""+i,objHashMapGet.get("objArrayList"));
  }
  */
%>
      <div>
         <table id="item_crear" name="item_crear" width="8%" border="0" cellspacing="2" cellpadding="0">
         <tr align="center">
            <td id="CellAddItem" name="CellAddItem" align="left" valign="middle" colspan="4" >&nbsp; 
            </td>
         </tr>
         </table>
      </div>
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
  <input type=hidden name=hdnCoAssignmentID />
  <input type=hidden name=txtPhoneNumber />
  <input type=hidden name=txtOrigResponsablePago />
  <input type=hidden name=txtOrigFactura value="No Vale" / >
  <input type=hidden name=hdnOrigResP / >
  <input type=hidden name=hdnOrigFac / >
    
  <input type=hidden name=cmbNewResponsablePago / >
  <input type=hidden name=cmbNewFactura / >
  
  <input type=hidden name=hdnFlagSave />
  
  <input type=hidden name=hdnDeleteRegister />
  
  <input type=hidden name=hdnFlagES / >
  
  <input type=hidden name=hdnContractId / >
  <input type=hidden name=hdnSnCodeId / >  
  <input type=hidden name=hdnOrigFactura / > 
  <input type=hidden name=hdnNewResponsablePago / > 
  <input type=hidden name=hdnNewFactura / >
  
  
 <table  border="0" width="40%" cellpadding="2" cellspacing="1" class="RegionBorder">
     <tr>
       <td>
       <table id="table_assignment" name="table_assignment" border="0" width="100%" cellpadding="2" cellspacing="1">                     
              <tr id="cabecera">
                  <td class="CellLabel" align="center">&nbsp;#&nbsp;</td>
                  <td class="CellLabel" align="center">&nbsp;Teléfono&nbsp;</td>
                  <td class="CellLabel" align="center">&nbsp;Responsable de Pago&nbsp;</td>
                  <!--<td class="CellLabel" align="center">&nbsp;Servicio/Factura&nbsp;</td>-->
                  <td class="CellLabel" align="center">&nbsp;Nuevo Responsable de Pago&nbsp;</td>
                  <td class="CellLabel" align="center">&nbsp;Nueva Factura&nbsp;</td>
                  <!--<td class="CellLabel" align="center">&nbsp;&nbsp;</td>-->
              </tr>
         </table>
       </td>
      </tr>
      <tr>
        <td>
         
        </td>
      </tr> 
 </table>   
 <br>
 <script defer>
 function fxCreateRowAssignment(){ 
 //Inserto una nueva fila
 var row      = table_assignment.insertRow(-1);
 
 var elemText =  "";
 var cell = "";
 var i = 0;
 /*Agrego la primera línea*/
 cell = row.insertCell(i++);
 cell.className = 'CellContent';
 cell.innerHTML = '';
 
 
 cell = row.insertCell(i++);
 elemText = fxGetTelefono();
 cell.innerHTML = elemText;
 
 cell = row.insertCell(i++);
 elemText = fxGetResponsablePago();
 cell.innerHTML = elemText;
 /*
 cell = row.insertCell(i++);
 elemText = fxGetOrigFactura();
 cell.innerHTML = elemText;
 */
 cell = row.insertCell(i++);
 elemText = fxGetNewResponsablePago();
 cell.innerHTML = elemText;
 
 cell = row.insertCell(i++);
 elemText = fxGetNewFactura();
 cell.innerHTML = elemText;
 
 /*
 cell = row.insertCell(i++);
 elemText = fxGetGUIDelete();
 cell.innerHTML = elemText;
 */
 fxLoadSiteData();
 fxLoadBillingAccountData(); 
 
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

 function fxUpdateTableDetail(){   
   var form = parent.mainFrame.document.frmdatos;
   var length = table_assignment.rows.length-1;         
   for (j=0; j <length; j++){      
      //alert(form.cmbNewResponsablePago[j+1].selectedIndex);
      if (form.cmbNewResponsablePago[j+1].selectedIndex == 1){         
         fxLoadBA("1", j+1);
      }
      else{         
         fxLoadBA("", j+1);
      }
   }
 }
 
 function fxLoadSiteData(){
  var index =  table_assignment.rows.length;
  var form = parent.mainFrame.document.frmdatos;
 
  var objSite = null;
     for(i=0; i <vctSite.size(); i++){
        objSite =  vctSite.elementAt(i);
        
        opcion=new Option( objSite.siteStatus + " - " + objSite.siteName , objSite.siteId ) 
       
        form.cmbNewResponsablePago[index-1].options[i+1]=opcion;
        
     }
 
 }
 
 /**Permite cargar todos los nuevos reponsables de pago
    asignados en la sección de responsables de pago   
 **/
  function fxReloadedSite(){
    var cantRowsTable = table_assignment.rows.length;
    var form          = parent.mainFrame.document.frmdatos;
 
    var objSite = null;
    for(x=1; x<cantRowsTable; x++){
      var indexDefault = form.cmbNewResponsablePago[x].value;
      DeleteSelectOptions(form.cmbNewResponsablePago[x]);
      for(i=0; i <vctSite.size(); i++){
        objSite =  vctSite.elementAt(i);
        opcion=new Option( objSite.siteStatus + " - " + objSite.siteName , objSite.siteId )
        form.cmbNewResponsablePago[x].options[i+1]=opcion;
      }
      form.cmbNewResponsablePago[x].value = indexDefault;
    }
  }
 
 function fxLoadBillingAccountData(){
  var index =  table_assignment.rows.length;
  var form = parent.mainFrame.document.frmdatos;
 
  var objBillingAccount = null;  
  //alert(form.cmbNewResponsablePago[index-1].selectedIndex);
  //alert(form.cmbNewResponsablePago[index-1].options[form.cmbNewResponsablePago[index-1].selectedIndex].value);  
   
     for(i=0; i <vctBillingAccount.size(); i++){
        objBillingAccount =  vctBillingAccount.elementAt(i);        
        opcion=new Option( objBillingAccount.baStatus + " - " + objBillingAccount.baName , objBillingAccount.baId )             
        //alert(objBillingAccount.baSite);
        form.cmbNewFactura[index-1].options[i+1]=opcion;        
     }
 }
 
 </script>
 <script defer="defer">
  function fxLoadBASite(siteId, index){
    form = parent.mainFrame.frmdatos;
    var typeCustomer = '';
    if( form.hdnSiteId.value == 0 || form.hdnSiteId.value == "0" || form.hdnSiteId.value == "" )
      typeCustomer = 'FLAT';
    else
      typeCustomer = 'LARGE';
    var index =  Number(index);//+1;     
    var pageBA = siteId<0?'New':'Old';
    siteId = siteId<0?(siteId*-1):siteId;
    var url = "<%=request.getContextPath()%>/customerservlet?myaction=loadBillingAccountBySiteId&strSiteId="+siteId+"&strCustomerId=<%=strCodigoCliente%>&strOrderId=<%=strOrderId%>&strPage="+pageBA+"&strIndex="+index+"&typeCustomer="+typeCustomer+"&strSiteHidden="+form.hdnSiteId.value;
	  //alert(url)
    parent.bottomFrame.location.replace(url);
    //}
  }
 </script>
 

 <script DEFER>
 function fxGetTelefono(){
	//CEM COR0459
    return "<input type=hidden name=hdnNewFactura><input type=hidden name=hdnNewResponsablePago><input type=hidden name=hdnOrigFactura><input type=hidden name=hdnFlagES><input type=hidden name=hdnSnCodeId><input type=hidden name=hdnContractId><input type=hidden name=hdnCoAssignmentID ><input type=hidden name=hdnFlagSave value=I ><input type=hidden name=indItems><td><input type=text name=txtPhoneNumber size=12 onkeydown='this.value=trim(this.value); javascript:fxGetDataPhone(this);' ></td>";
 }
 </script>
 <script DEFER>
 function fxGetResponsablePago(){
    return "<td><input type=text name=txtOrigResponsablePago size=33 readonly /><input type=hidden name=hdnOrigResP /></td>";
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
    var cadena = "<td>"
    cadena+="<select name=cmbNewResponsablePago onchange='javascript:fxLoadBASite(this.value, parentNode.parentNode.rowIndex);'>";
    cadena+="<option>";
    cadena+="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
    cadena+="</option>";
    cadena+="</select>";
    cadena+="</td>";
    
    return cadena;
  }
 </script>
 <script DEFER>
  function fxGetNewFactura(){
    var cadena = "<td>"
    cadena+="<select value=0 name=cmbNewFactura  onChange=fxChangeValue(this)>";
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
    cadena+="<a href='javascript:fVoid()' onclick='javascript:fxDeleteRegister(this,this.parentNode.parentNode.rowIndex);'  ><img src='<%=Constante.PATH_APPORDER_SERVER%>/images/Eliminar.gif' border='no' ALT='Borrar Item'></a>";
    cadena+="</td>";
    return cadena;  
  }
  </script>
  <script DEFER>
  function fxDeleteCmbRespPago(){
     var form = parent.mainFrame.document.frmdatos;
     var length1 = table_assignment.rows.length-1;      
     for (k=0; k<length1 ; k++){
        DeleteSelectOptions(form.cmbNewFactura[k]);
     }
  }
  </script>
  <script DEFER>
  function fxLoadBA(value, index){
     var index =  Number(index)+1;
     var form = parent.mainFrame.document.frmdatos;
     DeleteSelectOptions(form.cmbNewFactura[index-1]);
     var objBillingAccount = null;
     var cont = 0;
     if (value == ""){
       for(i=0 ; i < parent.mainFrame.vctBillingAccount.size() ; i++){
         objBillingAccount =  vctBillingAccount.elementAt(i);
         if (objBillingAccount.baSite == "-1" || objBillingAccount.baSite == "0"){
            cont = cont + 1;
            opcion=new Option( objBillingAccount.baStatus + " - " + objBillingAccount.baName , objBillingAccount.baId );
            form.cmbNewFactura[index-1].options[cont]=opcion;
         }
       }
     }
     else{
       for(m=0 ; m < parent.mainFrame.vctBillingAccount.size() ; m++){
         objBillingAccount =  vctBillingAccount.elementAt(m);
         if (objBillingAccount.baSite == "-1" || objBillingAccount.baSite != "0"){
            cont = cont + 1;
            opcion=new Option( objBillingAccount.baStatus + " - " + objBillingAccount.baName , objBillingAccount.baId );
            form.cmbNewFactura[index-1].options[cont]=opcion;
         }
       }
     }
  }
 
 </script>
 
 <script DEFER>
 var vctSite            = new Vector();
 var vctBillingAccount  = new Vector();
 function fxOnLoadAssignementBillingAccount(){ 
    fxLoadResponsablePagos();
    fxLoadSetData();    
    /**Permite deshabilitar los campos**/
    fxDisabledAllControls();
 }
 
 /**Pemite deshabilitar todos los controles**/
 function fxDisabledAllControls(){
  form = document.frmdatos;
  
  var cantRowsTable = table_assignment.rows.length;
  
  if( cantRowsTable > 1 ){
    for(i=1; i<cantRowsTable; i++){
      form.txtPhoneNumber[i].disabled = true;
      form.txtOrigResponsablePago[i].disabled = true;
      //form.txtOrigFactura[i].disabled = true;
      form.cmbNewResponsablePago[i].disabled = true;
      form.cmbNewFactura[i].disabled = true;
    }
  }
 
 }
 
 function fxLoadResponsablePagos(){
 <% if( objArraySiteExistsList != null ){
      SiteBean objSiteBean =  null;
      //System.out.println("Site objArraySiteExistsList : " + objArraySiteExistsList.size() );
      for(int i=0; i<objArraySiteExistsList.size(); i++ ) {  
          objSiteBean = new SiteBean();
          objSiteBean = (SiteBean)objArraySiteExistsList.get(i);
  %>
      vctSite.addElement(new fxMakeSite("<%=objSiteBean.getSwSiteId()%>","<%=objSiteBean.getSwSiteName()%>","<%=objSiteBean.getSwRegionName()%>","<%=objSiteBean.getNpStatus()%>"));
 
 <% }}%>
 
 <% if( objArraySiteSolicitedList != null ){
      SiteBean objSiteBean =  null;
      //System.out.println("Site objArraySiteSolicitedList : " + objArraySiteSolicitedList.size() );
      for(int i=0; i<objArraySiteSolicitedList.size(); i++ ) {  
          objSiteBean = new SiteBean();
          objSiteBean = (SiteBean)objArraySiteSolicitedList.get(i);
  %>
      vctSite.addElement(new fxMakeSite("<%=objSiteBean.getSwSiteId()%>","<%=objSiteBean.getSwSiteName()%>","<%=objSiteBean.getSwRegionName()%>","<%=objSiteBean.getNpStatus()%>"));
 
 <% }}%>
 
 }
 
 function fxLoadBillingAccount(){
 
 <% if( objArrayBillingAccountSolicitedList != null ){
     BillingAccountBean objBillingAccountBean =  null;
      //System.out.println("Site objArrayBillingAccountSolicitedList : " + objArrayBillingAccountSolicitedList.size() );
      for(int i=0; i<objArrayBillingAccountSolicitedList.size(); i++ ) {  
          objBillingAccountBean = new BillingAccountBean();
          objBillingAccountBean = (BillingAccountBean)objArrayBillingAccountSolicitedList.get(i);
  %>
     vctBillingAccount.addElement(new fxMakeBilling("<%=objBillingAccountBean.getNpBillaccountNewId()%>","<%=objBillingAccountBean.getNpBillacCName()%>","Solicitado","<%=objBillingAccountBean.getNpSiteId()%>")) ;
 <% }}%>
 
  <% if( objArrayBillingAccountExitstList != null ){
     BillingAccountBean objBillingAccountBean =  null;
      //System.out.println("Site objArrayBillingAccountSolicitedList : " + objArrayBillingAccountExitstList.size() );
      for(int i=0; i<objArrayBillingAccountExitstList.size(); i++ ) {  
          objBillingAccountBean = new BillingAccountBean();
          objBillingAccountBean = (BillingAccountBean)objArrayBillingAccountExitstList.get(i);
  %>
     vctBillingAccount.addElement(new fxMakeBilling("<%=objBillingAccountBean.getNpBillaccountNewId()%>","<%=objBillingAccountBean.getNpBillacCName()%>","Existente",'-1')) ;
 <% }}%>
 
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
      //var url = "serviceservlet?myaction=loadDataPhone&indexActual="+currentIndex+"&pCustomerId="+"<%=strCodigoCliente%>";//CEM COR0459 ;
      var url = "<%=request.getContextPath()%>/serviceservlet?myaction=loadDataPhone&indexActual="+currentIndex+"&pCustomerId="+"<%=strCodigoCliente%>"+"&pSiteId="+"<%=strnpSite%>"+"&hdnSpecification="+<%=hdnSpecification%>+"&strOrderId="+<%=strOrderId%>;
      form.action = url;  
      form.submit();
    }  
	}
 }
 
 function fxChangeValue(objChange){
 
 /*currentIndex = objChange.parentNode.parentNode.rowIndex;
 if( form.hdnFlagSave[currentIndex].value == "I" ){
     form.hdnFlagSave[currentIndex].value = "U";
 }*/
 }

 function fxLoadSetData(){
 var form = document.frmdatos;
 var typeSite = "";
 
 <%
 if( objArrayCoAssignment != null ){
  CoAssignmentBean objCoAssignmentBean =  null;
  ArrayList objArrayListAux = null;
  for(int i=0; i<objArrayCoAssignment.size(); i++ ) {
    objCoAssignmentBean = new CoAssignmentBean();
    objCoAssignmentBean = (CoAssignmentBean)objArrayCoAssignment.get(i);
    ArrayList objBillingBySiteExists    = new ArrayList();
    ArrayList objBillingBySiteSolicited = new ArrayList();
    /**Si es Activo cargamos los existentes**/
    if( MiUtil.getString(objCoAssignmentBean.getNptypesite()).equals(Constante.SITE_STATUS_ACTIVO) ){
      long lngAuxCustIdBSCS   = (new CustomerService()).getCustomerIdBSCS(MiUtil.parseLong(objCoAssignmentBean.getNpnewsiteid()),Constante.CUSTOMERTYPE_SITE);
      HashMap objBilling = (new BillingAccountService()).BillingAccountDAOgetBillingAccountList(lngAuxCustIdBSCS);
      objBillingBySiteExists = (ArrayList)objBilling.get("objArrayList");
      
      ArrayList objAuxBillingBySite = new ArrayList();  
      objBilling = (new BillingAccountService()).BillingAccountDAOgetAccountList(MiUtil.parseLong(objCoAssignmentBean.getNpnewsiteid()),MiUtil.parseLong(strCodigoCliente),MiUtil.parseLong(strOrderId));
      objBillingBySiteSolicited = (ArrayList)objBilling.get("objArrayList");
      
      /*if( !objAuxBillingBySite.isEmpty() ){
        for(int t=0; t<objAuxBillingBySite.size(); t++ ) {
          objBillingBySite.add(objAuxBillingBySite.get(t));
        }
      }*/
      
    /**Si es Unknow cargamos los solicitados**/
    }else if( MiUtil.getString(objCoAssignmentBean.getNptypesite()).equals(Constante.SITE_STATUS_UNKNOWN) ){
      HashMap objBilling = (new BillingAccountService()).BillingAccountDAOgetAccountList(MiUtil.parseLong(objCoAssignmentBean.getNpnewsiteid()),MiUtil.parseLong(strCodigoCliente),MiUtil.parseLong(strOrderId));
      objBillingBySiteSolicited = (ArrayList)objBilling.get("objArrayList");
    /**Si no se ha seleccionado algún site**/
    }else{
      /**Si es un FLAT**/
      //if( strCustomerStruct.equals("FLAT")){
        long lngAuxCustIdBSCS   = (new CustomerService()).getCustomerIdBSCS(MiUtil.parseLong(strCodigoCliente),Constante.CUSTOMERTYPE_CUSTOMER);	
        //long lngAuxCustIdBSCS   = (new CustomerService()).getCustomerIdBSCS(MiUtil.parseLong(objCoAssignmentBean.getNpnewsiteid()),Constante.CUSTOMERTYPE_SITE);		 
        HashMap objBilling = (new BillingAccountService()).BillingAccountDAOgetBillingAccountList(lngAuxCustIdBSCS);
        objBillingBySiteExists = (ArrayList)objBilling.get("objArrayList");
        
        ArrayList objAuxBillingBySite = new ArrayList();  
        objBilling = (new BillingAccountService()).BillingAccountDAOgetAccountList(MiUtil.parseLong(objCoAssignmentBean.getNpnewsiteid()),MiUtil.parseLong(strCodigoCliente),MiUtil.parseLong(strOrderId));
        objBillingBySiteSolicited = (ArrayList)objBilling.get("objArrayList");

      /**Si es un LARGE**/
      //}else if(strCustomerStruct.equals("LARGE")){
        
      //}
    }
 %>
 
 fxCreateRowAssignment();
 
 /*Teléfono*/
 form.txtPhoneNumber[<%=i+1%>].value = "<%=objCoAssignmentBean.getNpphone()%>";
 /*Id de la asignación*/
 form.hdnCoAssignmentID[<%=i+1%>].value = "<%=objCoAssignmentBean.getNpcoassignmentid()%>";
 /*Origen del Responsable de Pago*/
 form.txtOrigResponsablePago[<%=i+1%>].value = "<%=objCoAssignmentBean.getNpbscspaymntrespcustname()%>";
 form.hdnOrigResP[<%=i+1%>].value = "<%=objCoAssignmentBean.getNpbscspaymntrespcustomeridId()%>";
 /*Código del Contrato*/
 form.hdnContractId[<%=i+1%>].value = "<%=objCoAssignmentBean.getNpbscscontractId()%>";
 /*Responsable de Pago elegido*/
 form.cmbNewResponsablePago[<%=i+1%>].value = "<%=objCoAssignmentBean.getNpnewsiteid()%>";
 /*Tipo de Site (Active/Unknow)*/
 typeSite = "<%=objCoAssignmentBean.getNptypesite()%>";
 
 /**Carga las facturas existentes**/
 <%
 if(objBillingBySiteExists!=null){
  BillingAccountBean objBillAccBean = null;
    for(int j=0; j<objBillingBySiteExists.size(); j++ ){
      objBillAccBean = new BillingAccountBean();
      objBillAccBean = (BillingAccountBean)objBillingBySiteExists.get(j);
 %>
  parent.mainFrame.AddNewOption(form.cmbNewFactura[<%=i+1%>],"<%=objBillAccBean.getNpBillaccountNewId()%>","Existente - <%=MiUtil.getString(objBillAccBean.getNpBillacCName())%>");
 <% }
 }%>
 
 <%
 if(objBillingBySiteSolicited!=null){
  BillingAccountBean objBillAccBean = null;
    for(int j=0; j<objBillingBySiteSolicited.size(); j++ ){
      objBillAccBean = new BillingAccountBean();
      objBillAccBean = (BillingAccountBean)objBillingBySiteSolicited.get(j);
 %>
  parent.mainFrame.AddNewOption(form.cmbNewFactura[<%=i+1%>],"<%=objBillAccBean.getNpBillaccountNewId()%>","Solicitado - <%=MiUtil.getString(objBillAccBean.getNpBillacCName())%>");
 <% }
 }%>
 
 
 <%if(!MiUtil.getString(objCoAssignmentBean.getNpbillaccnewid()).equals("")){%>
  form.cmbNewFactura[<%=i+1%>].value = "<%=objCoAssignmentBean.getNpbillaccnewid()%>";
 <%}else if(!MiUtil.getString(objCoAssignmentBean.getNpbscsbillingAccountId()).equals("")){%>
  form.cmbNewFactura[<%=i+1%>].value = "<%=objCoAssignmentBean.getNpbscsbillingAccountId()%>";
 <%}%>
 
 /**Carga los Servicios/Factura del teléfono**/
 <%
    objArrayListAux     =   new ArrayList();
    //objArrayListAux     =   (ArrayList)objHashMapService.get(""+i);
     for(int x=0; ((objArrayListAux!=null)&&(x<objArrayListAux.size())); x++){
      objAuxCoAssignmentBean  = new CoAssignmentBean();
      objAuxCoAssignmentBean = (CoAssignmentBean)objArrayListAux.get(x);
 %>
    opcion=new Option( '<%=objAuxCoAssignmentBean.getNporigsitedesc()%>' , '<%=objAuxCoAssignmentBean.getNpbscssncode()%>' );
    //form.txtOrigFactura[<%=(i+1)%>].options[<%=x+1%>]=opcion;
 <% }%>
 
 /*Servicio Factura*/
 //form.txtOrigFactura[<%=i+1%>].value = "<%=objCoAssignmentBean.getNpbscssncode()%>";

 form.hdnFlagSave[<%=i+1%>].value = "U";
 
 <%}}%>
 
 }

 
 function fxMakeSite(siteId,siteName,siteRegionName,siteStatus){
    this.siteId         = siteId;
    this.siteName       = siteName;
    this.siteRegionName = siteRegionName;
    this.siteStatus     = siteStatus;
 }
 
 function fxMakeBilling(baId,baName,baStatus,baSite){
    this.baId         = baId;
    this.baName       = baName;
    this.baStatus     = baStatus;
    this.baSite       = baSite;
 }
 
 function fxOnValidateAssignmentBillingAccount(){
    form = document.frmdatos;
    
    if( table_assignment.rows.length <= 1 ){
       alert("Debe de haber mas de un registro en la sección [ASIGNACIÓN DE CUENTAS DE FACTURACIÓN] ");
       return false;
       
    }
    
    for(i=1; i < form.txtPhoneNumber.length; i++){
        if( form.txtPhoneNumber[i].value == "" ) {
           alert("Los campos [Teléfono] no pueden estar vacíos");
            return false;
        }
    }
    
    /**Se emplea para entregar los valores del servicio/facura
       al control oculto que será leído en la actualización**/
    /*for(i=1; i < form.txtOrigFactura.length; i++){
      var valorFactura = form.txtOrigFactura[i].value;
      valorFactura = trim(valorFactura);
      form.hdnOrigFactura[i].value = valorFactura;
    }*/
    
    /**Se emplea para entregar los valores del nuevo responsable de pago
       al control oculto que será leído en la actualización**/
    for(i=1; i < form.cmbNewResponsablePago.length; i++){
      var valorSite = form.cmbNewResponsablePago[i].value;
      valorSite = trim(valorSite);
      form.hdnNewResponsablePago[i].value = valorSite;
    }
    
    /**Se emplea para entregar los valores de la nueva cuenta de facturación
       al control oculto que será leído en la actualización**/
    for(i=1; i < form.cmbNewFactura.length; i++){
      var valorNewFactura = form.cmbNewFactura[i].value;
      valorNewFactura = trim(valorNewFactura);
      form.hdnNewFactura[i].value = valorNewFactura;
    }
    
    
    
    for(i=1; i < form.cmbNewFactura.length; i++){
	
	  //var ind_NewBA  = form.cmbNewFactura[i].selectedIndex;	  
	  //var desc_NewBA = form.cmbNewFactura[i].options[ind_NewBA].text;	
	  //INICIO CEM COR354
      try{
       var ind_NewBA  = form.cmbNewFactura[i].selectedIndex;	  
       var desc_NewBA = form.cmbNewFactura[i].options[ind_NewBA].text;
      }catch (e){
       var desc_NewBA="";		 
      }
	  //FIN CEM COR354
      if( desc_NewBA.indexOf("Solicitado") != -1 ){
        form.hdnFlagES[i].value = "S";
      }else if( desc_NewBA.indexOf("Existente") != -1 ){
        form.hdnFlagES[i].value = "E";
      }else{
        form.hdnFlagES[i].value = "";
      }
     
    }
    
    return true;
 }
 
 function fVoid(){}
 
 function fxDeleteRegister(objThis, objThisRow){
    form  = document.frmdatos;    
    if( confirm("¿Está usted seguro que desea eliminar este registro?") ) {    
    value = form.hdnCoAssignmentID[objThisRow].value
    value = parseInt(value);            
    if( !isNaN(value) )
      form.hdnDeleteRegister.value += "|" + value;      
      table_assignment.deleteRow(objThisRow);
      fxRenumeric("table_assignment",table_assignment.rows.length-1);
      disabledSaveButton(false);
    }   
 }

  function disabledSaveButton(disabled) {
  	if (form.btnSaveOrder) {
  		form.btnSaveOrder.disabled = disabled;
  	} else {
  		form.btnUpdOrder.disabled = disabled;
  		form.btnUpdOrderInbox.disabled = disabled;
  	}
  }
 

//INICIO CEM - COR354
function fxMakeObjNewRespPago(siteId,  siteName,  siteRegionName,  siteStatus){		
         this.siteId     		=   siteId;
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

function fxMakeObjBA(baId,baName,baStatus,baSite){		
    this.baId     	=   baId;
    this.baName     =   baName;
    this.baStatus 	=   baStatus;
    this.baSite 		=   baSite;
}  

//BA: BillingAccount
function fxTransferBAVector(vctBAMain){     
	var vctBAAux = new Vector();            
   for( x = 0; x < vctBAMain.size(); x++ ){
       var objBAMake = new fxMakeObjBA(
       vctBAMain.elementAt(x).baId,
       vctBAMain.elementAt(x).baName,
       vctBAMain.elementAt(x).baStatus,
       vctBAMain.elementAt(x).baSite);	
       vctBAAux.addElement(objBAMake);        
	} 
	parent.mainFrame.vctBillingAccount=vctBAAux;   
}

//FIN CEM - COR354

 </script>
 <%}catch(Exception e){%>
  <script>alert('<%=MiUtil.getString(e.getMessage())%>');</script>
 <%}%>