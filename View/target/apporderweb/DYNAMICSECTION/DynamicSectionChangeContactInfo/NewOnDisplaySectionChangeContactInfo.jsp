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
      Hashtable hshtinputNewSection = null;
      CustomerService  objCustomerService  = new CustomerService();	 
  
      String  strCustomerId= "";
      String  strnpSite ="";
      String  strCodBSCS = "";
      String  hdnSpecification = "";
      String  strTypeCompany = "";
      String  strOrderId  = "";
      String  strCustomerStruct = "";
      String  strGeneratorId  = "";
      String  strGeneratorType = "";      
      String  strCustomerType=""; 
      String  strClienteId="";   
      
      hshtinputNewSection     = (Hashtable)request.getAttribute("hshtInputNewSection");
  
      if ( hshtinputNewSection != null ) {
      
        strCustomerId           =   (String)hshtinputNewSection.get("strCustomerId");
        strnpSite               =   (String)hshtinputNewSection.get("strSiteId");
        strCodBSCS              =   (String)hshtinputNewSection.get("strCodBSCS");
        hdnSpecification        =   (String)hshtinputNewSection.get("strSpecificationId");
        //strSolution             =   (String)hshtinputNewSection.get("strSolution");
        strTypeCompany          =   (String)hshtinputNewSection.get("strTypeCompany");    
        strOrderId              =   (String)hshtinputNewSection.get("strOrderId");    
        strGeneratorId      		= 	(String)hshtinputNewSection.get("strGeneratorId");
        strGeneratorType  	  	=	  (String)hshtinputNewSection.get("strGeneratorType");	        
      }
       
%>
<%

    System.out.println("strTypeCompany ======== hrm ==="+strTypeCompany);
    
    if (strnpSite.length() == 0) {strnpSite = "0"; }
    
    System.out.println("strnpSite ==> "+strnpSite);
   
    HashMap objHashMap = new HashMap();
    ArrayList objArrayTitleList    = new ArrayList();
    ArrayList objArrayStatusList   = new ArrayList();
    ArrayList objArrayCargoList    = new ArrayList();
    ArrayList objArrayContactList  = new ArrayList();
  
    GeneralService  objGeneralService  = new GeneralService();  
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
  
    lObjectId=MiUtil.parseLong(strCustomerId);
    strObjectType=Constante.CUSTOMERTYPE_CUSTOMER; 
    System.out.println("CUSTOMER: "+lObjectId);
  
  //Constante.TYPE_CONTACT_PEDIDO
  
    objHashMap=objCustomerService.getContactList(lObjectId,strObjectType,"90",Long.parseLong(strnpSite));
    strMessage=(String)objHashMap.get("strMessage");
    if (strMessage!=null){     
      throw new Exception(strMessage);       
    }
  
    objArrayContactList=(ArrayList)objHashMap.get("arrContactList");
  
%>
  <br>
  <div>
     <table id="item_crear" name="item_crear" width="8%" border="0" cellspacing="2" cellpadding="0">
     <tr align="center">
        <td id="CellAddItem" name="CellAddItem" align="left" valign="middle" colspan="4" >&nbsp; 
           <a href="javascript:fxCreateRowContact();" > <!-- onmouseover=window.status='Agregar Item'; onmouseout=window.status=';>-->
           <img name="item_img_crear" ALT="Cambiar Datos Contacto" src="<%=Constante.PATH_APPORDER_SERVER%>/images/crear.gif" border="no"></a>
        </td>
     </tr>
     </table>
  </div>
  
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

  <input type=hidden name=hdnFlagSave />
  <input type=hidden name=indItems /> 
  <input type=hidden name=hdnCustomerId value="<%=strCustomerId%>" />
    
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
              
       cell = row.insertCell(i++);
       elemText = fxGetGUIDelete();
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
           if(index == 1 && form.cmbTitulo.options!= undefined) {
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
            form.hdmCmbEstatus.value = "Nuevo";
           } else {
            form.cmbEstatus[index-1].options[i+1] = opcion;
            form.cmbEstatus[index-1].value = "Nuevo";
            form.hdmCmbEstatus[index-1].value = "Nuevo";
           }
         }  
     }
     
   /* function fxLoadCargo(){
       var form = parent.mainFrame.document.frmdatos;       
       var objCargo = null;
       var index = form.cmbCargo.length;
       
         for(i=0; i <vctCargo.size(); i++){
           objCargo =  vctCargo.elementAt(i);           
           opcion=new Option( objCargo.cargoName , objCargo.cargoId );                      
           if(index < 3 && form.cmbCargo.options!= undefined) {
            alert("IF");
            form.cmbCargo.options[i+1] = opcion;
           } else {
           
            alert("Else");
            form.cmbCargo[index-1].options[i+1] = opcion;
           }
         }
     }     */
     
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
       cadena+="<select value=0 name=cmbTitulo>";
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
        return "<td><input type=text name=txtNombres size=15 onchange=this.value=trim(this.value.toUpperCase()) /> <input type=hidden name=hdnNombresOld /> <input type=hidden name=hdnTituloOld /> <input type=hidden name=hdnCargoNameOld /> <input type=hidden name=hdnPersonId /> <input type=hidden name=hdmCargoDescrip /></td>";    	 
     }
  </script>  
  <script DEFER>
     function fxGetApePaterno(){
       return "<td> <input type=text name=txtApePaterno size=33 onchange=this.value=trim(this.value.toUpperCase()) /><input type=hidden name=hdnApePaternoOld /> </td>";
     }
  </script>   
  <script DEFER>
     function fxGetApeMaterno(){
       return "<td> <input type=text name=txtApeMaterno size=30 onchange=this.value=trim(this.value.toUpperCase()) /> <input type=hidden name=hdnApeMaternoOld /> </td>";
     }
  </script>  
  <script DEFER>
     function fxGetCargo(){
       var cadena = "<td>"
       cadena+="<select value=0 name=cmbCargo>";
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
       return "<td><input type=text name=txtEmail size=25 onchange=this.value=trim(this.value.toLowerCase()) /><input type=hidden name=hdnEmailOld /></td>";
     }
   </script>  
   
   <script DEFER>
     function fxGetEstatus(){
       var cadena = "<td>  <input type=hidden name=hdmCmbEstatus /> "
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
       cadena+="<a href='javascript:fVoid()' onclick='javascript:fxDeleteRegister(this,this.parentNode.parentNode.rowIndex);'  ><img 	src='<%=Constante.PATH_APPORDER_SERVER%>/images/Eliminar.gif' border='no' ALT='Borrar Item'></a>";    
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
         %>
             vctCargo.addElement(new fxMakeCargo("<%=hsMapCargoList.get("jobtitlteId")%>","<%=hsMapCargoList.get("descripcion").toString()%>"));
        
        <% }}%>      
         
     }
    
     function fxLoadValores(){       
       var form = document.frmdatos; 
       var index = table_assignment.rows.length;
       <% if (objArrayContactList!= null){
            ContactObjectBean objContactBean=null;
            for(int i=0; i<objArrayContactList.size(); i++ ) {
              objContactBean = new ContactObjectBean();
              objContactBean = (ContactObjectBean)objArrayContactList.get(i);
              System.out.println("i1 ="+ MiUtil.getString(objContactBean.getSwfirstname()));       
              %>
              fxCreateRowContact();
              <%
              System.out.println("i2 ="+MiUtil.getString(objContactBean.getSwtitle()));               
              %>        
               if (index == 1){
                   
                   index = index + 1;
                                  
                 /* VALORES ACTUALES EN EL FORMULARIO DE LA SECCION */
                 form.cmbTitulo.value     = "<%=MiUtil.getString(objContactBean.getSwtitle())%>";
                 form.txtNombres.value    = "<%=MiUtil.getString(objContactBean.getSwfirstname())%>";                               
                 form.txtApePaterno.value = "<%=MiUtil.getString(objContactBean.getSwlastname())%>";
                 form.txtApeMaterno.value = "<%=MiUtil.getString(objContactBean.getSwmiddlename())%>";
                 form.cmbCargo.value      = "<%=MiUtil.getString(objContactBean.getSwjobtitleid())%>";
                 form.txtEmail.value      = "<%=MiUtil.getString(objContactBean.getSwemailaddress())%>"; 
                 
                 /* VALORES ANTIGUOS EN EL FORMULARIO DE LA SECCION */
                 form.hdnTituloOld.value     = "<%=MiUtil.getString(objContactBean.getSwtitle())%>"; 
                 form.hdnNombresOld.value    = "<%=MiUtil.getString(objContactBean.getSwfirstname())%>";   
                 form.hdnApePaternoOld.value = "<%=MiUtil.getString(objContactBean.getSwlastname())%>";
                 form.hdnApeMaternoOld.value = "<%=MiUtil.getString(objContactBean.getSwmiddlename())%>";                       
                 form.hdnEmailOld.value      = "<%=MiUtil.getString(objContactBean.getSwemailaddress())%>"; 
                 form.hdnPersonId.value      = "<%=MiUtil.getString(objContactBean.getSwpersonid())%>";  
                // form.hdnCargoNameOld.value  = "<%=MiUtil.getString(objContactBean.getSwjobtitleid())%>"; 
                 form.hdnCargoNameOld.value  = form.cmbCargo.options[form.cmbCargo.selectedIndex].text;
                 
                // form.hdnFlagSave.value = "U";
                 
                 <% if (MiUtil.getString(objContactBean.getSwpersonid()).length() > 0) {%>
                    form.cmbEstatus.value    = "Existe"; 
                    form.cmbEstatus.disabled = false;
                 <%}%>
                 
                 form.hdmCmbEstatus.value = form.cmbEstatus.value;
 
               }
               else{
                
                 /* VALORES ACTUALES EN EL FORMULARIO DE LA SECCION */                               
                 form.cmbTitulo[index-1].value     = "<%=MiUtil.getString(objContactBean.getSwtitle())%>";
                 form.txtNombres[index-1].value    = "<%=MiUtil.getString(objContactBean.getSwfirstname())%>"; 
                 form.txtApePaterno[index-1].value = "<%=MiUtil.getString(objContactBean.getSwlastname())%>";
                 form.txtApeMaterno[index-1].value = "<%=MiUtil.getString(objContactBean.getSwmiddlename())%>";
                 form.cmbCargo[index-1].value      = "<%=MiUtil.getString(objContactBean.getSwjobtitleid())%>";
                 form.txtEmail[index-1].value      = "<%=MiUtil.getString(objContactBean.getSwemailaddress())%>";   
                  
                /* VALORES ANTIGUOS EN EL FORMULARIO DE LA SECCION */
                 form.hdnTituloOld[index-1].value     = "<%=MiUtil.getString(objContactBean.getSwtitle())%>"; 
                 form.hdnNombresOld[index-1].value    = "<%=MiUtil.getString(objContactBean.getSwfirstname())%>";                     
                 form.hdnApePaternoOld[index-1].value = "<%=MiUtil.getString(objContactBean.getSwlastname())%>";
                 form.hdnApeMaternoOld[index-1].value = "<%=MiUtil.getString(objContactBean.getSwmiddlename())%>";  
                 form.hdnEmailOld[index-1].value      = "<%=MiUtil.getString(objContactBean.getSwemailaddress())%>";
                 form.hdnPersonId[index-1].value      = "<%=MiUtil.getString(objContactBean.getSwpersonid())%>"; 
                 //form.hdnCargoNameOld[index-1].value  = "<%=MiUtil.getString(objContactBean.getSwjobtitleid())%>"; 
                 
                 form.hdnCargoNameOld[index-1].value  = form.cmbCargo[index-1].options[form.cmbCargo[index-1].selectedIndex].text; 
                 
                // form.hdnFlagSave[index-1].value = "U";
                 
                  <% if (MiUtil.getString(objContactBean.getSwpersonid()).length() > 0) {%>
                    form.cmbEstatus[index-1].value    = "Existe"; 
                    form.cmbEstatus[index-1].disabled = false;
                  <%}%>
                
                 form.hdmCmbEstatus[index-1].value = form.cmbEstatus[index-1].value;
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
       if (numRows > 1){    
          
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
             if( !isEmailAddress( form.txtEmail[i] ) ) {              
               return false;
             }
             
             if(form.hdnPersonId[i].value != "" && form.cmbEstatus[i].value == "Nuevo"){
                alert("No puede cambiar a Nuevo porque el contacto Existe");
                return false;
             }             
             // Se asigan la descripcion del cargo al hidden
             form.hdmCargoDescrip[i].value = form.cmbCargo[i].options[form.cmbCargo[i].selectedIndex].text             
           }
      } else {
            if (form.cmbTitulo!= undefined){   
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
                    if( !isEmailAddress( form.txtEmail ) ) {              
                        return false;
                    }
                    if(form.hdnPersonId.value != "" && form.cmbEstatus.value == "Nuevo"){
                        alert("No puede cambiar a Nuevo porque el contacto Existe");
                        return false;
                   }             
                   // Se asigan la descripcion del cargo al hidden
                   form.hdmCargoDescrip.value = form.cmbCargo.options[form.cmbCargo.selectedIndex].text
            }else{
                alert("Debe Ingresar al menos un Contacto");
                return false;
            }  
      }       
       form.indItems.value   = numRows;     
       return true; 
     }
     
     function fxEstadoChange(objValue){
       var form = document.frmdatos; 
       var index = table_assignment.rows.length;
      alert(objValue);
     }     
  </script>
  
 <%}catch(Exception e){%>
  <script>alert('<%=MiUtil.getString(e.getMessage())%>');</script>
 <%}%>