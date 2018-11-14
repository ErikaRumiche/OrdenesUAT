<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="pe.com.nextel.service.GeneralService" %>
<%@page import="pe.com.nextel.service.NewOrderService" %>
<%@page import="pe.com.nextel.service.BillingAccountService" %>
<%@page import="pe.com.nextel.service.CustomerService" %>
<%@page import="pe.com.nextel.util.MiUtil" %>
<%@page import="pe.com.nextel.util.Constante" %>
<%@page import="pe.com.nextel.bean.BillingAccountBean" %>
<%@page import="pe.com.nextel.bean.SiteBean" %> 
<%@page import="pe.com.nextel.bean.ContactObjectBean"%>

<% 
 try{
  Hashtable hshtInputDetailSection = null;
  String    strCustomerId= "",
            strnpSite ="",
            strCodBSCS = "",
            hdnSpecification = "",            
            strTypeCompany = "",
            strOrderId  = "",
            strCustomerStruct = "",
            strGeneratorId  = "",
            strGeneratorType = "";
           
  String strCustomerType=""; 
  String strClienteId="";     
  CustomerService  objCustomerService  = new CustomerService();	 
 
  hshtInputDetailSection     = (Hashtable)request.getAttribute("hshtInputDetailSection");
  
  if ( hshtInputDetailSection != null ) {
  
    strCustomerId           =   (String)hshtInputDetailSection.get("strCustomerId");
    strnpSite               =   (String)hshtInputDetailSection.get("strSiteId");
    strCodBSCS              =   (String)hshtInputDetailSection.get("strCodBSCS");
    hdnSpecification        =   (String)hshtInputDetailSection.get("strSpecificationId");   
    strTypeCompany          =   (String)hshtInputDetailSection.get("strTypeCompany");
    strOrderId              =   (String)hshtInputDetailSection.get("strOrderId");
	  strGeneratorId      		= 	(String)hshtInputDetailSection.get("strGeneratorId");
	  strGeneratorType  	  	=	  (String)hshtInputDetailSection.get("strGeneratorType");	   
    
  }       
%>
<%
  HashMap objHashMap = new HashMap();
  ArrayList objArrayTitleList       = new ArrayList();
  ArrayList objArrayStatusList       = new ArrayList();
  ArrayList objArrayCargoList       = new ArrayList();
  ArrayList objArrayContactList     = new ArrayList();
  
  GeneralService       objGeneralService         = new GeneralService();
  String strMessage = null;
  
  /**Obtenenmos el Título del Contacto*/
    objHashMap = objGeneralService.getTableList("PERSON_TITLE","1"); 
    strMessage=(String)objHashMap.get("strMessage");
    if (strMessage!=null)
      throw new Exception(strMessage); 
  
  objArrayTitleList=(ArrayList)objHashMap.get("arrTableList");
  
  /**Obtenenmos Los Estados del Contact Change*/
    objHashMap = objGeneralService.getTableList("CONTACT_CHANGE_STATUS","1"); 
    strMessage=(String)objHashMap.get("strMessage");
    if (strMessage!=null)
      throw new Exception(strMessage); 
    
    objArrayStatusList=(ArrayList)objHashMap.get("arrTableList");
  
  /**Obtenemos le Cargo del Contacto*/
  objHashMap = objGeneralService.getTitleList();
  strMessage= (String)objHashMap.get("strMessage");
  if (strMessage!=null)
    throw new Exception(strMessage);                              
  
  objArrayCargoList=(ArrayList)objHashMap.get("arrTitleList");
  
  /**Obtenemos los contactos tipo pedido*/
  String strObjectType=null;
  long lObjectId=0;
  String strTitle=null;
  String strNombres=null;
  String strLastName=null;
  String strMiddleName=null;
  String strJobTitle=null;
  String strEmail=null;
  String strTitleId=null;
  String strJobTitleId=null;
  long lOrderId=0;
  int iItemid=0;
  int iObjectType=0;
  String strContactType="90";
  
  lObjectId=MiUtil.parseLong(strCustomerId);
  strObjectType=Constante.CUSTOMERTYPE_CUSTOMER; 
  lOrderId = MiUtil.parseLong(strOrderId);
  //iObjectType=1;
  
  System.out.println("HRM SITE ===================>"+strnpSite);
  
  if (strnpSite.trim().equals("0")){
    iObjectType=1;
    lObjectId=MiUtil.parseLong(strCustomerId);
  }else{
    iObjectType=2;
    lObjectId=MiUtil.parseLong(strnpSite);
  }
  
  System.out.println("CUSTOMER: "+lObjectId);

      objHashMap=objCustomerService.getContactChangeList(lOrderId,iItemid,iObjectType,lObjectId, strContactType);
  
  strMessage=(String)objHashMap.get("strMessage");
  if (strMessage!=null){     
    throw new Exception(strMessage);       
  }
  
  objArrayContactList=(ArrayList)objHashMap.get("arrContactList");
  System.out.println("Tamaño Array Edit: "+objArrayContactList.size());  
   
%>
  <br>
  <table border="0" cellspacing="0" cellpadding="0" width="40%">
     <tr> 
        <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="18"></td>
        <td class="SubSectionTitle" align="left" valign="top">Datos Contacto Pedido</td>
        <td class="SubSectionTitleRightCurve" valign="top" align="right" width="11"> &nbsp;</td>
        <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
        <td align="right">    
        </td>
     </tr>     
  </table>
  
  <input type=hidden name=indItems />  
  <input type=hidden name=hdnFlagSave />
  <input type=hidden name=hdnCustomerId value="<%=strCustomerId%>" />
  <input type=hidden name=hdnNumeroOrder value="<%=strOrderId%>" />
    
  <table  border="0" width="40%" cellpadding="2" cellspacing="1" class="RegionBorder">
     <tr>
       <td>
       <table id="table_assignment" name="table_assignment" border="0" width="100%" cellpadding="2" cellspacing="1">                     
              <tr id="cabecera">
                  <td class="CellLabel" align="center">&nbsp;#&nbsp;</td>
                  <td class="CellLabel" align="center">&nbsp;T&iacute;tulo&nbsp;</td>
                  <td class="CellLabel" align="center">&nbsp;Nombres&nbsp;</td>
                  <td class="CellLabel" align="center">&nbsp;Ap.Paterno&nbsp;</td>
                  <td class="CellLabel" align="center">&nbsp;Ap.Materno&nbsp;</td>
                  <td class="CellLabel" align="center">&nbsp;Cargo&nbsp;</td>
                  <td class="CellLabel" align="center">&nbsp;Email&nbsp;</td>
                  <td class="CellLabel" align="center">&nbsp;Estado&nbsp;</td>                  
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
     
     function fxCreateRowContact(){
       //Inserto una nueva fila
       var row      = table_assignment.insertRow(-1);
       var elemText =  "";
       var cell = "";
       var i = 0;
        
       cell = row.insertCell(i++);
       cell.className = 'CellContent';
       cell.innerHTML = '';
       
       cell = row.insertCell(i++);
       elemText = fxGetTitulo();
       cell.innerHTML = elemText;
       
       cell = row.insertCell(i++);
       elemText = fxGetNombres();
       cell.innerHTML = elemText;
       
       cell = row.insertCell(i++);
       elemText = fxGetApePaterno();
       cell.innerHTML = elemText;
       
       cell = row.insertCell(i++);
       elemText = fxGetApeMaterno();
       cell.innerHTML = elemText;
       
       cell = row.insertCell(i++);
       elemText = fxGetCargo();
       cell.innerHTML = elemText;
       
       cell = row.insertCell(i++);
       elemText = fxGetEmail();
       cell.innerHTML = elemText;
       
       cell = row.insertCell(i++);
       elemText = fxGetEstatus();             
       cell.innerHTML = elemText;
       
       fxLoadTitulo();
       fxLoadStatu();
       fxLoadCargo();       
       
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
     
     function fxLoadTitulo(){       
       var form = parent.mainFrame.document.frmdatos;       
       var objTitulo = null;
       var index = form.cmbTitulo.length;
        
         for(i=0; i <vctTitulo.size(); i++){
           objTitulo =  vctTitulo.elementAt(i);
           opcion=new Option( objTitulo.titleName , objTitulo.titleId );
          if(index == 1 && form.cmbTitulo.options!= undefined ) {
            form.cmbTitulo.options[i+1] = opcion;                        
           } else {
            form.cmbTitulo[index-1].options[i+1] = opcion;
           }
         }      
     }
     
     function fxLoadStatu(){       
       var form = parent.mainFrame.document.frmdatos;       
       var objStatus = null;
       var index = form.cmbEstatus.length;       
       
         for(i=0; i <vctStatus.size(); i++){
           objStatus =  vctStatus.elementAt(i);
           opcion=new Option( objStatus.statusName , objStatus.statusId );
           if(index == 1 && form.cmbEstatus.options!= undefined) {           
            form.cmbEstatus.options[i+1] = opcion;
            form.cmbEstatus.value = "Nuevo";
           } else {           
            form.cmbEstatus[index-1].options[i+1] = opcion;
            form.cmbEstatus[index-1].value = "Nuevo";
           }
         }      
     }
     
    function fxLoadCargo(){
       var form = parent.mainFrame.document.frmdatos;       
       var objCargo = null;
       var index = form.cmbCargo.length;
       
         for(i=0; i <vctCargo.size(); i++){
           objCargo =  vctCargo.elementAt(i);
           opcion=new Option( objCargo.cargoName , objCargo.cargoId );
           
           if(index == 1 && form.cmbCargo.options!= undefined) {
            form.cmbCargo.options[i+1] = opcion;            
           } else {
            form.cmbCargo[index-1].options[i+1] = opcion;
           }
         }      
     }     
  </script>  
  <script DEFER>     
     function fxGetTitulo(){
       var cadena = "<td>"
       cadena+="<select value=0 name=cmbTitulo disabled>";
       cadena+="<option>";
       cadena+="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
       cadena+="</option>";
       cadena+="</select>";       
       cadena+="</td>";      
       return cadena;
     }          
  </script> 
  <script DEFER>
     function fxGetNombres(){
        return "<td><input type=text name=txtNombres size=15 disabled /> <input type=hidden name=hdnNombresOld /> <input type=hidden name=hdnTituloOld /> <input type=hidden name=hdnCargoNameOld /> <input type=hidden name=hdnPersonId /> <input type=hidden name=hdmCargoDescrip /> <input type=hidden name=hdnNpitemid /> <input type=hidden name=hdnNpaction /> </td>";    	
     }
  </script>  
  <script DEFER>
     function fxGetApePaterno(){
       return "<td> <input type=text name=txtApePaterno size=33 disabled /><input type=hidden name=hdnApePaternoOld /> </td>";      
     }
  </script>   
  <script DEFER>
     function fxGetApeMaterno(){
       return "<td> <input type=text name=txtApeMaterno size=30 disabled /> <input type=hidden name=hdnApeMaternoOld /> </td>";       
     }
  </script>  
  <script DEFER>
     function fxGetCargo(){
       var cadena = "<td>"
       cadena+="<select value=0 name=cmbCargo disabled >";
       cadena+="<option>";
       cadena+="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
       cadena+="</option>";
       cadena+="</select>";       
       cadena+="</td>";        
       return cadena;        
     }
   </script>  
   <script DEFER>
     function fxGetEmail(){
       return "<td><input type=text name=txtEmail size=25 disabled /><input type=hidden name=hdnEmailOld /> </td>";   
     }
   </script>     
   <script DEFER>
     function fxGetEstatus(){
       var cadena = "<td>"
       cadena+="<select value=0 name=cmbEstatus disabled>";
       cadena+="<option>";
       cadena+="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
       cadena+="</option>";
       cadena+="</select>";       
       cadena+="</td>";        
       return cadena;        
     }
   </script>      
   <script DEFER>
     function fxGetGUIDelete(){
       var cadena = "<td>"       
       cadena+="<img 	src='<%=Constante.PATH_APPORDER_SERVER%>/images/Eliminar.gif' border='no' ALT='Borrar Item'>";    
       cadena+="</td>";      
       return cadena;    
    }
  </script>   
    
   <script DEFER>
     var vctTitulo    = new Vector();
     var vctStatus    = new Vector();
     var vctCargo     = new Vector();
     
     function fxOnLoadChangeContactInfo(){
        fxLoadTitulos();
        fxLoadStatus();
        fxLoadCargos();
        fxLoadValores();       
     }
     
     function fxLoadTitulos(){
        <% if( objArrayTitleList != null ){
             HashMap hsMapTitleList = new HashMap();             
             System.out.println("Titulo objArrayTitleList : " + objArrayTitleList.size() );
             for(int i=0; i<objArrayTitleList.size(); i++ ) {                  
                  hsMapTitleList = (HashMap)objArrayTitleList.get(i);
                  System.out.println("i ="+ hsMapTitleList.get("wv_npValueDesc"));                  
         %>            
             vctTitulo.addElement(new fxMakeTitle("<%=hsMapTitleList.get("wv_npValueDesc")%>","<%=hsMapTitleList.get("wv_npValueDesc").toString()%>"));
        
        <% }}%>
        
     }
     
     function fxLoadStatus(){
        <% if( objArrayStatusList != null ){
             HashMap hsMapStatusList = new HashMap();             
             System.out.println("Estatus objArrayStatusList : " + objArrayStatusList.size() );
             for(int i=0; i<objArrayStatusList.size(); i++ ) {                  
                  hsMapStatusList = (HashMap)objArrayStatusList.get(i);
                  System.out.println("Estatus = "+ hsMapStatusList.get("wv_npValueDesc"));                  
         %>             
             vctStatus.addElement(new fxMakeStatus("<%=hsMapStatusList.get("wv_npValueDesc")%>","<%=hsMapStatusList.get("wv_npValueDesc").toString()%>"));
        <% }}%>        
     }
     
     function fxLoadCargos(){
        <% if( objArrayCargoList != null ){
             HashMap hsMapCargoList = new HashMap();
             System.out.println("Cargos objArrayCargoList : " + objArrayCargoList.size() );
             for(int i=0; i<objArrayCargoList.size(); i++ ) {  
                 hsMapCargoList = (HashMap)objArrayCargoList.get(i);
                 System.out.println("Cargos ["+hsMapCargoList+"]");
        %>            
             vctCargo.addElement(new fxMakeCargo("<%=hsMapCargoList.get("jobtitlteId")%>","<%=hsMapCargoList.get("descripcion").toString()%>"));                    
        <% }}%>      
         
     }
    
     function fxLoadValores(){       
       var form = document.frmdatos; 
       var index = table_assignment.rows.length;
       
       <% if (objArrayContactList!= null){
            ContactObjectBean objContactBean=null;   
            
            System.out.println("objArrayContactList.size() ==> "+objArrayContactList.size());
            
            for(int i=0; i<objArrayContactList.size(); i++ ) {
              objContactBean = new ContactObjectBean();
              objContactBean = (ContactObjectBean)objArrayContactList.get(i);
              System.out.println("JobId ["+objContactBean.getSwjobtitleid()+"] JobName ["+objContactBean.getSwjobtitle()+"] objContactBean.getNpitemid ["+objContactBean.getNpitemid()+"]");
              %>
              fxCreateRowContact();
                            
               if (index == 1){  
                 index = index + 1;
                  
                 /* VALORES ACTUALES EN EL FORMULARIO DE LA SECCION */                
                 form.cmbTitulo.value     = "<%=MiUtil.getString(objContactBean.getNptitlenew())%>";
                 form.txtNombres.value    = "<%=MiUtil.getString(objContactBean.getNpfirstnamenew())%>";                               
                 form.txtApePaterno.value = "<%=MiUtil.getString(objContactBean.getNplastnamenew())%>";
                 form.txtApeMaterno.value = "<%=MiUtil.getString(objContactBean.getNpmiddlenamenew())%>";            
                 form.cmbCargo.value      = "<%=MiUtil.getString(objContactBean.getSwjobtitleid())%>";
                 form.txtEmail.value      = "<%=MiUtil.getString(objContactBean.getNpemailnew())%>"; 
                 
                 //form.txtEstado.value     = "<%=MiUtil.getString(objContactBean.getNpaction())%>";                  
                 //alert(form.cmbCargo.options[form.cmbCargo.selectedIndex].text);
                 
                 /* VALORES ANTIGUOS EN EL FORMULARIO DE LA SECCION */
                 form.hdnTituloOld.value     = "<%=MiUtil.getString(objContactBean.getSwtitle())%>"; 
                 form.hdnNombresOld.value    = "<%=MiUtil.getString(objContactBean.getSwfirstname())%>";   
                 form.hdnApePaternoOld.value = "<%=MiUtil.getString(objContactBean.getSwlastname())%>";
                 form.hdnApeMaternoOld.value = "<%=MiUtil.getString(objContactBean.getSwmiddlename())%>";                       
                 form.hdnEmailOld.value      = "<%=MiUtil.getString(objContactBean.getSwemailaddress())%>"; 
                 form.hdnPersonId.value      = "<%=MiUtil.getString(objContactBean.getSwpersonid())%>";  
                 form.hdnCargoNameOld.value  = "<%=MiUtil.getString(objContactBean.getSwjobtitleid())%>"; 
                 form.hdnNpitemid.value      = "<%=objContactBean.getNpitemid()%>"; 
                 form.hdmCargoDescrip.value  = "";
                 form.hdnNpaction.value      = "<%=MiUtil.getString(objContactBean.getNpaction())%>"; 
                
                 form.hdnFlagSave.value = "U";
                 form.cmbEstatus.value       = "<%=MiUtil.getString(objContactBean.getNpaction())%>"; 
                
                
               }else{
                
                  /* VALORES ACTUALES EN EL FORMULARIO DE LA SECCION */       
                 form.cmbTitulo[index-1].value     = "<%=MiUtil.getString(objContactBean.getNptitlenew())%>";
                 form.txtNombres[index-1].value    = "<%=MiUtil.getString(objContactBean.getNpfirstnamenew())%>"; 
                 form.txtApePaterno[index-1].value = "<%=MiUtil.getString(objContactBean.getNplastnamenew())%>";
                 form.txtApeMaterno[index-1].value = "<%=MiUtil.getString(objContactBean.getNpmiddlenamenew())%>";               
                 form.cmbCargo[index-1].value      = "<%=MiUtil.getString(objContactBean.getSwjobtitleid())%>";
                 form.txtEmail[index-1].value      = "<%=MiUtil.getString(objContactBean.getNpemailnew())%>";   
                  
                /* VALORES ANTIGUOS EN EL FORMULARIO DE LA SECCION */
                 form.hdnTituloOld[index-1].value     = "<%=MiUtil.getString(objContactBean.getSwtitle())%>"; 
                 form.hdnNombresOld[index-1].value    = "<%=MiUtil.getString(objContactBean.getSwfirstname())%>";                     
                 form.hdnApePaternoOld[index-1].value = "<%=MiUtil.getString(objContactBean.getSwlastname())%>";
                 form.hdnApeMaternoOld[index-1].value = "<%=MiUtil.getString(objContactBean.getSwmiddlename())%>";  
                 form.hdnEmailOld[index-1].value      = "<%=MiUtil.getString(objContactBean.getSwemailaddress())%>";
                 form.hdnPersonId[index-1].value      = "<%=MiUtil.getString(objContactBean.getSwpersonid())%>"; 
                 form.hdnCargoNameOld[index-1].value  = "<%=MiUtil.getString(objContactBean.getSwjobtitleid())%>"; 
                 
                 form.hdnNpitemid[index-1].value      = "<%=objContactBean.getNpitemid()%>"; 
                 form.hdmCargoDescrip[index-1].value  = "";
                 form.hdnNpaction[index-1].value      = "<%=MiUtil.getString(objContactBean.getNpaction())%>"; 
                 
                 form.hdnFlagSave.value = "U";   
                 
                 form.cmbEstatus[index-1].value       = "<%=MiUtil.getString(objContactBean.getNpaction())%>"; 
                
                 index = index + 1;
               }               
              <%
            }            
          }
       %>
     }
     
     function fxMakeTitle(titleId,titleName){
       this.titleId         = titleId;
       this.titleName       = titleName;
     }
     
      function fxMakeStatus(statusId,statusName){
       this.statusId        = statusId;
       this.statusName      = statusName;
     }
     
     function fxMakeCargo(cargoId,cargoName){
       this.cargoId         = cargoId;
       this.cargoName       = cargoName;       
     }
     
     function fVoid(){}
 
     function fxDeleteRegister(objThis, objThisRow){ 
     
      if( confirm("¿Está usted seguro que desea eliminar este registro?") ) {
         table_assignment.deleteRow(objThisRow);
         fxRenumeric("table_assignment",table_assignment.rows.length-1);
       }
     
     }
    
     function fxOnValidateChangeContactInfo(){
       form = document.frmdatos;
       var numRows = table_assignment.rows.length - 1 ;
       var numContactList = <%=objArrayContactList.size()%>;
       
       if (numRows == 1 && numContactList ==1){    
            if( form.cmbTitulo.value == "" ) {
               alert("El campo [Titulo] no puede estar vacío");
               return false;
              }
              if (form.txtNombres.value == ""){
                  alert("El campo [Nombres] no puede estar vacío");
                  return false;
              }
              if( form.txtApePaterno.value == "" ) {
                  alert("El campo [Ap. Paterno] no puede estar vacío");
                  return false;
             }
              if( form.cmbCargo.value == "" ) {
               alert("El campo [Cargo] no puede estar vacío");
               return false;
             }             
             if( form.txtEmail.value == "" ) {
               alert("El campo [Email] no puede estar vacío");
               return false;
             }
             if( form.cmbEstatus.value == "" ) {
               alert("El campo [Estado] no puede estar vacío");
               return false;
             }
             // Se asigan la descripcion del cargo al hidden
             form.hdmCargoDescrip.value = form.cmbCargo.options[form.cmbCargo.selectedIndex].text
                
      } else {
          for(i=0; i < numRows; i++){   
              if( form.cmbTitulo[i].value == "" ) {
               alert("Los campos [Titulo] no pueden estar vacíos");
               return false;
              }
              if (form.txtNombres[i].value == ""){
                  alert("Los campos [Nombres] no pueden estar vacíos");
                  return false;
              }
              if( form.txtApePaterno[i].value == "" ) {
                  alert("Los campos [Ap. Paterno] no pueden estar vacíos");
                  return false;
             }
              if( form.cmbCargo[i].value == "" ) {
               alert("Los campos [Cargo] no pueden estar vacíos");
               return false;
             }
             
             if( form.txtEmail[i].value == "" ) {
               alert("Los campos [Email] no pueden estar vacíos");
               return false;
             }                  
             if( form.cmbEstatus[i].value == "" ) {
               alert("El campo [Estado] no puede estar vacío");
               return false;
             }             
             // Se asigan la descripcion del cargo al hidden
             form.hdmCargoDescrip[i].value = form.cmbCargo[i].options[form.cmbCargo[i].selectedIndex].text
          } 
      }       
       form.indItems.value   = numRows;
     
       return true; 
     }
     
  </script>
  
 <%}catch(Exception e){%>
  <script>alert('<%=MiUtil.getString(e.getMessage())%>');</script>
 <%}%>