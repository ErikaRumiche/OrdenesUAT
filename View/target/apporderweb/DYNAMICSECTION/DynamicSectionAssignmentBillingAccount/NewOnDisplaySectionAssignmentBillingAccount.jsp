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
  
  if ( hshtinputNewSection != null ) {
  
    strCustomerId        =   (String)hshtinputNewSection.get("strCustomerId");
    strnpSite               =   (String)hshtinputNewSection.get("strSiteId");
    strCodBSCS              =   (String)hshtinputNewSection.get("strCodBSCS");
    hdnSpecification        =   (String)hshtinputNewSection.get("strSpecificationId");
    //strSolution             =   (String)hshtinputNewSection.get("strSolution");
    strTypeCompany          =   (String)hshtinputNewSection.get("strTypeCompany");

    strOrderId              =   (String)hshtinputNewSection.get("strOrderId");

	 strGeneratorId      		= 	 (String)hshtinputNewSection.get("strGeneratorId");
	 strGeneratorType  		=	 (String)hshtinputNewSection.get("strGeneratorType");	
		
	 System.out.println("[NewOnDisplaySectionAssignmentBillingAccount.jsp]strGeneratorId: "+strGeneratorId);
	 System.out.println("[NewOnDisplaySectionAssignmentBillingAccount.jsp]strGeneratorType: "+strGeneratorType);
    
    //Validación si el cliente no tiene un site
    if(strnpSite.equals("")) strnpSite="0";
    
    System.out.println("SiteId NewOnDisplaySectionAssignmentBilliangAccount: "+strnpSite+"*****");
    System.out.println("CustomerId NewOnDisplaySectionAssignmentBilliangAccount: "+strCustomerId+"*****");

      //Inicio CEM - COR0802
	  if(strnpSite.equals("0")) {
		strCustomerType=Constante.CUSTOMERTYPE_CUSTOMER; // Si el valor es 0 no ingreso Site en la pantalla principal
		strClienteId=strCustomerId;
	  }
	  else{
		strCustomerType=Constante.CUSTOMERTYPE_SITE;
		strClienteId=strnpSite;
	  }	  
	  System.out.println("strCustomerType: "+strCustomerType);
	  System.out.println("strCustomerId: "+strCustomerId);	
      //Fin CEM - COR0802    
  }
  
  
 
%>

<%
  HashMap objHashMap = new HashMap();
  ArrayList objArraySiteExistsList    = new ArrayList();
  ArrayList objArraySiteSolicitedList = new ArrayList();
  
  ArrayList objArrayBillingAccountExitstList    = new ArrayList();
  ArrayList objArrayBillingAccountSolicitedList = new ArrayList();
  
  NewOrderService       objNewOrderService       = new NewOrderService();
  BillingAccountService objBillingAccountService = new BillingAccountService();
  
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

%>

  <div>
     <table id="item_crear" name="item_crear" width="8%" border="0" cellspacing="2" cellpadding="0">
     <tr align="center">
        <td id="CellAddItem" name="CellAddItem" align="left" valign="middle" colspan="4" >&nbsp; 
           <a href="javascript:fxCreateRowAssignment();" > <!-- onmouseover=window.status='Agregar Item'; onmouseout=window.status=';>-->
           <img name="item_img_crear" ALT="Cambiar Estructura de Cuenta" src="<%=Constante.PATH_APPORDER_SERVER%>/images/crear.gif" border="no"></a>
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
  <input type=hidden name=txtPhoneNumber />
  <input type=hidden name=txtOrigResponsablePago />
  
  <input type=hidden name=txtOrigFactura / >
  <input type=hidden name=hdnOrigResP / >
  <input type=hidden name=hdnOrigFac / >
  
  <input type=hidden name=hdnFlagES / >
  
  <input type=hidden name=hdnContractId / >
  <input type=hidden name=hdnSnCodeId / >

  <input type=hidden name=cmbNewResponsablePago / >
  <input type=hidden name=cmbNewFactura / >
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
                  <td class="CellLabel" align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
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
 var cont = 1;
 
 function fxCreateRowAssignment(){
 
 //Validar para un cliente LARGE si selecciono Responsable de Pago
	if (fxValidateSelectionRPag('newdetallefact')){
		return;
	}	
		
 //Inserto una nueva fila
 var row      = table_assignment.insertRow(-1);
 
 var elemText =  "";
 var cell = "";
 var i = 0;
 
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
 cell = row.insertCell(2);
 elemText = fxGetOrigFactura();
 cell.innerHTML = elemText;
 */
 cell = row.insertCell(i++);
 elemText = fxGetNewResponsablePago();
 cell.innerHTML = elemText;
 
 cell = row.insertCell(i++);
 elemText = fxGetNewFactura();
 cell.innerHTML = elemText;
 
 cell = row.insertCell(i++);
 elemText = fxGetGUIDelete();
 cell.innerHTML = elemText;
 
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
    //alert(url);
      //var iPermitir = fxRequestXML("generalServlet","requestRespPago","nothing");
		var iPermitir = fxRequestXML("generalServlet","requestRespPago",url);
      if (iPermitir ==null )
         iPermitir=-1;
      return iPermitir;		
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
      for(i=0; i <vctBillingAccount.size(); i++){
        objBillingAccount =  vctBillingAccount.elementAt(i);
        opcion=new Option( objBillingAccount.baStatus + " - " + objBillingAccount.baName , objBillingAccount.baId ) 
        form.cmbNewFactura[index-1].options[i+1]=opcion;
      }
  }
 
 </script>
 

 <script DEFER>
 function fxGetTelefono(){
	return "<input type=hidden name=hdnNewFactura><input type=hidden name=hdnNewResponsablePago><input type=hidden name=hdnOrigFactura><input type=hidden name=hdnFlagES><input type=hidden name=hdnSnCodeId><input type=hidden name=hdnContractId><input type=hidden name=indItems><td><input type=text name=txtPhoneNumber size=12 onblur='this.value=trim(this.value); javascript:fxGetDataPhone2(this);' onkeydown='this.value=trim(this.value); javascript:fxGetDataPhone(this);'></td>";		
 }
 
 function fxGetResponsablePago(){
    return "<td><input type=text name=txtOrigResponsablePago size=33 readonly /><input type=hidden name=hdnOrigResP /></td>";
 }
 
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
 
 function fxGetNewResponsablePago(){
    var cadena = "<td>"
    cadena+="<select value=0 name=cmbNewResponsablePago onchange='fxLoadBASite(this.value,parentNode.parentNode.rowIndex);'>";
    cadena+="<option>";
    cadena+="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
    cadena+="</option>";
    cadena+="</select>";
    cadena+="</td>";
    
    return cadena;
  }
  
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
    var index =  Number(index);//+1;     
    var pageBA = siteId<0?'New':'Old';
    siteId = siteId<0?(siteId*-1):siteId;
    var url = "<%=request.getContextPath()%>/customerservlet?myaction=loadBillingAccountBySiteId&strSiteId="+siteId+"&strCustomerId=<%=strCustomerId%>&strOrderId=<%=strOrderId%>&strPage="+pageBA+"&strIndex="+index+"&typeCustomer="+typeCustomer;
    parent.bottomFrame.location.replace(url);
  }
 </script>
 
 <script DEFER>
 var vctSite            = new Vector();
 var vctBillingAccount  = new Vector();
 function fxOnLoadAssignementBillingAccount(){
    fxLoadResponsablePagos();
    fxLoadBillingAccount();
 }
 
 function fxLoadResponsablePagos(){
 <% if( objArraySiteExistsList != null ){
      SiteBean objSiteBean =  null;
      System.out.println("Site objArraySiteExistsList : " + objArraySiteExistsList.size() );
      for(int i=0; i<objArraySiteExistsList.size(); i++ ) {  
          objSiteBean = new SiteBean();
          objSiteBean = (SiteBean)objArraySiteExistsList.get(i);
  %>
      vctSite.addElement(new fxMakeSite("<%=objSiteBean.getSwSiteId()%>","<%=objSiteBean.getSwSiteName()%>","<%=objSiteBean.getSwRegionName()%>","<%=objSiteBean.getNpStatus()%>"));
 
 <% }}%>
 
 //alert(vctSite.size());
 
 <% if( objArraySiteSolicitedList != null ){
      SiteBean objSiteBean =  null;
      System.out.println("Site objArraySiteSolicitedList : " + objArraySiteSolicitedList.size() );
      for(int i=0; i<objArraySiteSolicitedList.size(); i++ ) {  
          objSiteBean = new SiteBean();
          objSiteBean = (SiteBean)objArraySiteSolicitedList.get(i);
  %>
      vctSite.addElement(new fxMakeSite("<%=(objSiteBean.getSwSiteId()*-1)%>","<%=objSiteBean.getSwSiteName()%>","<%=objSiteBean.getSwRegionName()%>","<%=objSiteBean.getNpStatus()%>"));
 
 <% }}%>
 
 //alert(vctSite.size());
 }
 
 function fxLoadBillingAccount(){
 
 <% if( objArrayBillingAccountSolicitedList != null ){
     BillingAccountBean objBillingAccountBean =  null;
      System.out.println("Site objArrayBillingAccountSolicitedList : " + objArrayBillingAccountSolicitedList.size() );
      for(int i=0; i<objArrayBillingAccountSolicitedList.size(); i++ ) {  
          objBillingAccountBean = new BillingAccountBean();
          objBillingAccountBean = (BillingAccountBean)objArrayBillingAccountSolicitedList.get(i);
  %>
     vctBillingAccount.addElement(new fxMakeBilling("<%=objBillingAccountBean.getNpBillaccountNewId()%>","<%=objBillingAccountBean.getNpBillacCName()%>","Solicitado")) ;
 <% }}%>
 
  <% if( objArrayBillingAccountExitstList != null ){
     BillingAccountBean objBillingAccountBean =  null;
      System.out.println("Site objArrayBillingAccountSolicitedList : " + objArrayBillingAccountExitstList.size() );
      for(int i=0; i<objArrayBillingAccountExitstList.size(); i++ ) {  
          objBillingAccountBean = new BillingAccountBean();
          objBillingAccountBean = (BillingAccountBean)objArrayBillingAccountExitstList.get(i);
  %>
     vctBillingAccount.addElement(new fxMakeBilling("<%=objBillingAccountBean.getNpBillaccountNewId()%>","<%=objBillingAccountBean.getNpBillacCName()%>","Existente")) ;
 <% }}%>
 
 }
  function fxInputTabEnter() {
	  return (window.event.keyCode == 13);
  }
 
  function fxGetDataPhone(objPhone){
   if(fxInputTabEnter()) { 
    form = document.frmdatos;  
    phoneNumber  = objPhone.value;
    tablelength  = table_assignment.rows.length;
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
    tablelength  = table_assignment.rows.length;
    //Inicio CEM COR0459
    form.txtPhoneNumber[currentIndex].value=trim(form.txtPhoneNumber[currentIndex].value);
    if (form.txtPhoneNumber[currentIndex].value!=""){
      for(var i=0;i<tablelength;i++){   
        if(currentIndex!=i){
          if(form.txtPhoneNumber[currentIndex].value==trim(form.txtPhoneNumber[i].value)){
            alert("El número de teléfono "+form.txtPhoneNumber[currentIndex].value+" ya fue seleccionado");
            form.txtPhoneNumber[currentIndex].value = '';
            form.txtPhoneNumber[currentIndex].focus();
            return;
          }
        }
      }
      var url = "<%=request.getContextPath()%>/serviceservlet?myaction=loadDataPhone&indexActual="+currentIndex+"&pCustomerId="+"<%=strCustomerId%>"+"&pSiteId="+<%=strnpSite%>+"&hdnSpecification="+<%=hdnSpecification%>+"&strOrderId="+<%=strOrderId%>;
      form.action = url;  
      form.submit();
    }  
 }
 
 function fxMakeSite(siteId,siteName,siteRegionName,siteStatus){
    this.siteId         = siteId;
    this.siteName       = siteName;
    this.siteRegionName = siteRegionName;
    this.siteStatus     = siteStatus;
 }
 
 function fxMakeBilling(baId,baName,baStatus){
    this.baId         = baId;
    this.baName       = baName;
    this.baStatus     = baStatus;
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
    
    try{
    //Validamos que el RP asociado al teléfono se haya obtenido
    if( form.hdnContractId != undefined && form.hdnContractId != null )
    for(i=1; i < form.hdnContractId.length; i++){
        if( form.hdnContractId[i].value == "" ) {
           alert("El teléfono ingresado no ha sido validado. Usted debe presionar TAB o ENTER sobre el campo teléfono en la sección [Cuenta Facturación Detalle] para validar");
            return false;
        }
    }
    }catch(e){}
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
      var ind_NewBA  = form.cmbNewFactura[i].selectedIndex;
      var desc_NewBA = form.cmbNewFactura[i].options[ind_NewBA].text;
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
    //alert(objThisRow);
    if( confirm("¿Está usted seguro que desea eliminar este registro?") ) {
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

function fxMakeObjBA(baId,baName,baStatus){		
	this.baId     		=   baId;
    this.baName       	=   baName;
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
 </script>
 
 <%}catch(Exception e){%>
  <script>alert('<%=MiUtil.getString(e.getMessage())%>');</script>
 <%}%>