<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@ page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@ page import="oracle.portal.provider.v2.ProviderSession" %>
<%@ page import="oracle.portal.provider.v2.url.UrlUtils" %>
<%@ page import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil" %>
<%@ page import="pe.com.nextel.bean.*"%>
<%@ page import="pe.com.nextel.service.*"%>
<%@ page import="pe.com.nextel.util.*"%>
<%@ page import="java.util.*"%>
<!--Source DynamicSectionEdit/Site/ContactEdit.jsp -->
<% 
 try{
   long lCustomerId=(request.getParameter("nCustomerId")==null?0:Long.parseLong(request.getParameter("nCustomerId")));
   long lSiteId=(request.getParameter("nSiteId")==null?0:Long.parseLong(request.getParameter("nSiteId")));
   long lRegionId=(request.getParameter("nRegionId")==null?0:Long.parseLong(request.getParameter("nRegionId")));
   long lOrderId=(request.getParameter("nOrderId")==null?0:Long.parseLong(request.getParameter("nOrderId")));    
   String strSessionId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));
   String strSpecificationId=(request.getParameter("pSpecificationId")==null?"0":request.getParameter("pSpecificationId")); //CEM COR0354
   
   GeneralService objGeneralS=new GeneralService();
   SiteService objSiteS=new SiteService();
   CustomerService objCustomerS=new CustomerService();
   CustomerBean objCustomerBean =new CustomerBean();
   String strMessage=null;
   HashMap hshData=null;
   String strTypePerson=null;
   String strType=null;
   String strCadena1=null;
   String strCadena2=null;
   HashMap hshUnqContacts=null;
   HashMap hshMndtCntcTypAux=null;
   //HashMap hshUnqCntctsAux=null;  
   HashMap hshCheckUnqContacts=null;    
   HashMap hshTypesContact=null;      
   HashMap hshCheckNoChangeContacts=null;    
   HashMap hshMandatContactType=null;
   //HashMap hshAux1=null;
   //HashMap hshAux2=null;
   ArrayList arrUnqContacts=null;  
   ArrayList arrCheckUnqContactsType=null;  
   ArrayList arrMandatFlgContactType=null;
   ArrayList arrCheckUnqFlgContactsType=null;
   ArrayList arrTypesContact=null;
   ArrayList arrMandatContactType=null;
   ArrayList arrCheckNoChangeContact=null;
   ArrayList arrCheckNoChangeContactFlag=null;
   ArrayList arrLista=null;
   ArrayList arrContacts=null;
   String strObjectType=null;
   HashMap hshResultado=new HashMap();   
   String strNochangeConttypeSelected=null;
   long lObjectId=0;
   int iResultado=0;  
   int iCountEditable=0;
   int iCountNoEditable=0;
   int iEdicion=0;   
   int iNumContAvailable=0;
   String strPedido = "Pedido";
   
   hshData=objCustomerS.getCustomerData(lCustomerId);
   strMessage=(String)hshData.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);
   objCustomerBean=(CustomerBean)hshData.get("objCustomerBean");
   
   if (objCustomerBean==null) objCustomerBean=new CustomerBean();
   
   strTypePerson=objCustomerBean.getSwTipoPerson().toUpperCase();
   
   /*Cambio de Cuenta de Facturación*/
   String strStatusBA = MiUtil.getString(objGeneralS.getValue(Constante.NPT_SECTION_BA_FROM_SITE,Constante.KN_ORDERS));
   
   
   //String strParam="?nOrderId="+lOrderId+"&nCustomerId="+lCustomerId+"&nSiteId="+lSiteId+"&hdnSessionId="+strSessionId;
   String strParam="?nOrderId="+lOrderId+"&nCustomerId="+lCustomerId+"&nSiteId="+lSiteId+"&hdnSessionId="+strSessionId+"&pSpecificationId="+strSpecificationId; //CEM COR0354
   
   //RUTAS
   String strURLGeneralData="SiteGeneralNew.jsp"+strParam;
   String strURLDireccion="AddressList.jsp"+strParam;
   String strURLBillAcc="BillingAccountList.jsp"+strParam;        
   String strURLContact="ContactList.jsp"+strParam;  
   String strURLOrderServlet =request.getContextPath()+"/editordersevlet";    
   String strURLAreaCode=request.getContextPath()+"/GENERALPAGE/AreaCodeList.jsp";
   
   System.out.println(" -------------------- INICIO CONTACTEDIT.jsp---------------------- ");  
   System.out.println("lSiteId :" + lSiteId);
   System.out.println("lCustomerId :" + lCustomerId);  
   System.out.println("lOrderId :" + lOrderId);     
   System.out.println("strSessionId :" + strSessionId);     
   System.out.println("strTypePerson :" + strTypePerson);
   System.out.println("strSpecificationId :" + strSpecificationId);
   System.out.println(" --------------------  FIN CONTACTEDIT.jsp---------------------- ");
   
   if (lSiteId != 0){
      strObjectType=Constante.CUSTOMERTYPE_SITE; 
      lObjectId=lSiteId;
   }else{
      strObjectType=Constante.CUSTOMERTYPE_CUSTOMER;
      lObjectId= lCustomerId;
   }
%> 
<html>
<head>
   <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
   <title>Contacto</title>
</head>
<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css">
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>         
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderIXEdit.js"></script>
<DIV ALIGN="LEFT">
<TABLE  BORDER="0" CELLPADDING="0" CELLSPACING="0">
<TR>
   <TD valign="top">&nbsp;&nbsp;</TD><TD>
      <TABLE  BORDER="0" HEIGHT="100%" WIDTH="100%" CELLPADDING="0" CELLSPACING="0">
         <TR>
            <TD class="LeftTabBgSlant" valign="top" align="left" width="10" height="19" NOWRAP="">&nbsp;</TD>
            <td valign="middle" NOWRAP class="TabBackgroundColor">&nbsp;&nbsp;<a href="<%=strURLGeneralData%>"><FONT class="TabBackgroundText">Datos Generales</FONT></a>&nbsp;&nbsp;</td>
            <TD align="right" class="RightTabBgCurve" width="8" NOWRAP="">&nbsp;&nbsp;</TD>
         </TR>
      </TABLE>
   </TD>
   <TD>
      <TABLE  BORDER="0" HEIGHT="100%" WIDTH="100%" CELLPADDING="0" CELLSPACING="0">
         <TR>
            <TD class="LeftTabBgSlant" valign="top" align="left" width="10" height="19" NOWRAP="">&nbsp;</TD>
            <td valign="middle" nowrap class="TabBackgroundColor">&nbsp;&nbsp;<A HREF="<%=strURLDireccion%>"><FONT class="TabBackgroundText">Direcciones</FONT></A>&nbsp;&nbsp;</td>
            <TD align="right" class="RightTabBgCurve" width="8" NOWRAP="">&nbsp;&nbsp;</TD>
         </TR>
      </TABLE>
   </TD>
   <TD>
      <TABLE  BORDER="0" HEIGHT="100%" WIDTH="100%" CELLPADDING="0" CELLSPACING="0">
         <TR>
            <TD class="LeftTabForeSlant" valign="top" align="left" width="10" height="19" NOWRAP="">&nbsp;</TD>
            <td valign="middle" nowrap class="TabForegroundColor">&nbsp;&nbsp;<FONT class="TabForegroundText">Contactos</FONT>&nbsp;&nbsp;</td>
            <TD align="right" class="RightTabForeCurve" width="8" NOWRAP="">&nbsp;&nbsp;</TD>
         </TR>
      </TABLE>
   </TD>
   
   <!--Deshabilitar esta opción. Valor en la NP_TABLE-->
   <%if( strStatusBA.equals(Constante.IND_STATUS_ACTIVE) ){%>
   <TD>
       <TABLE  BORDER="0" HEIGHT="100%" WIDTH="100%" CELLPADDING="0" CELLSPACING="0">
       <TR>
          <TD class="LeftTabBgSlant" valign="top" align="left" width="10" height="19" NOWRAP="">&nbsp;</TD>
          <td valign="middle" nowrap class="TabBackgroundColor">&nbsp;&nbsp;<A HREF="<%=strURLBillAcc%>"><FONT class="TabBackgroundText">Billing Account</FONT></A>&nbsp;&nbsp;</td>
          <TD align="right" class="RightTabBgCurve" width="8" NOWRAP="">&nbsp;&nbsp;</TD>
       </TR>
       </TABLE>
   </TD>   
   <%}%>
   <!--Deshabilitar esta opción. Valor en la NP_TABLE-->
   
</TR>
</TABLE>
<TABLE  WIDTH="100%" BORDER="0" CELLPADDING="0" CELLSPACING="0" class="TabForegroundColor">
<TR>
   <TD  class="TabForegroundColor"><IMG SRC="<%=Constante.PATH_APPORDER_SERVER%>/images/pobtrans.gif" BORDER="0" HEIGHT="3"></TD></TR>
</TABLE>  
  
</DIV>
<TABLE  BORDER="0" WIDTH="100%" CELLPADDING="4" CELLSPACING="0" class="RegionNoBorder">
   <TR><TD WIDTH="100%">

      <TABLE BORDER="0" class="PortletHeaderColor" CELLSPACING="0" CELLPADDING="0" WIDTH="100%">
         <TR class="PortletHeaderColor">
         <TD class="LeftCurve" valign="top" align=LEFT width="10" height="10" NOWRAP="">&nbsp;&nbsp;</TD>
         <TD class="PortletHeaderColor" NOWRAP="" align=LEFT valign="top" width="100%">
         <FONT class="PortletHeaderText"><FONT class="PortletHeaderText">Contactos para Site Nuevo</FONT></FONT>
         &nbsp;&nbsp;&nbsp;</TD>
         <TD align="RIGHT" class="PortletHeaderColor" NOWRAP="" valign="top">&nbsp;
         </TD><TD align="right" class="RightCurve" width="10" NOWRAP="">&nbsp;&nbsp;</TD></TR> 
      </TABLE>
      <TABLE  BORDER="1" WIDTH="100%" CELLPADDING="2" CELLSPACING="0" class="RegionBorder">
         <TR><TD class="RegionHeaderColor" WIDTH="100%">
   <%  
   //Obtenemos cursor con los datos de cada contacto.
   hshResultado= objSiteS.getAddressOrContactList(strObjectType,lObjectId, Constante.OBJECT_TYPE_CONTACT);
   arrContacts = (ArrayList)hshResultado.get("objArrayList");
   strMessage = (String)hshResultado.get("strMessage");
   iResultado=arrContacts.size();  
  
  if (strMessage!=null)
   throw new Exception(strMessage);
%>      
<script LANGUAGE="JavaScript">
   var ArrayNoChangeContact = new Array();
   var ArrayNoChangeContactFlag = new Array();
   var ArrayUniqueContact = new Array();
   var ArrayMandatoryContact = new Array();
   
   /*function fxDisabledNoChangeContact(){
      form=document.formdatos;      
      for(var j = 0; j < ArrayNoChangeContact.length ; j++){         
         if(ArrayNoChangeContactFlag[j]=='1'){            
            for(var i = 0; i < form.chkContactType.length; i++){               
               if(form.chkContactType[i].value == ArrayNoChangeContact[j]){                 
                 // if ("'||wn_unconditional_update||'" != "1" ) {                 
                    // form.chkContactType[i].disabled = true;
                  //}
               }
            }
         }
      }          
   }*/
               
   function fxCheckUniqueContacts(chkContact){
      form=document.formdatos;
      if(chkContact.checked){         
         for(var j = 0; j < ArrayUniqueContact.length ; j++){            
            if(chkContact.value == ArrayUniqueContact[j]){ //es un contacto de tipo unico
            //deshabilitar los otros contactos del mismo tipo
               for(var i = 0; i < form.chkContactType.length; i++){                 
                  if(chkContact.value == form.chkContactType[i].value){
                     form.chkContactType[i].checked = false;
                  }
               }
               chkContact.checked = true;
               break;
            }
         }
      }
   }
   
   function fxCheckMandatoryContacts(){
      form=document.formdatos;
      for(var j = 0; j < ArrayMandatoryContact.length ; j++){
         cont = 0;
         for(var i = 0; i < form.chkContactType.length; i++){
            if((ArrayMandatoryContact[j] == form.chkContactType[i].value) && (form.chkContactType[i].checked)){
               cont++;
            }
         }
         if(cont==0){
            alert("Debe haber por lo menos un contacto de tipo "+ArrayMandatoryContact[j]);
            return false;
         }
      }
      return true;
   }
      
   function fxUpdateNumContactsSelected(){
      form=document.formdatos;
      var v_cad_contact_type = "";
      if( form.hdnCounter.value > 1){
        for ( i=0; i < form.hdnCounter.value ; i++ ) {
           v_cad_contact_type = ""; 
           v_count = 0;
           num = eval("form.hdnNumContactsAvailable["+i+"].value");
           //avanzamos hasta los checks que corresponden al contacto
           posIni = 0;
           posFin = 0;
           for(var j = 0; j < i; j++){
              posIni = eval(parseInt(posIni) + parseInt(eval("form.hdnNumContactsAvailable["+j+"].value")));
           }
           posFin = eval(parseInt(posIni) + eval(parseInt(num) - 1));
           for (var j = posIni; j <= posFin; j++) {
              if ( form.chkContactType[j].checked ){
                 v_cad_contact_type = v_cad_contact_type + form.chkContactType[j].value + ",";
                 v_count++;
              }
           }
           form.hdnNumContactsSelected[i].value = v_count;
           form.hdnCadContactsSelected[i].value = v_cad_contact_type;
        }
      }else{
           v_count = 0;
           num = form.hdnNumContactsAvailable.value;
           //avanzamos hasta los checks que corresponden al contacto
           posIni = 0;
           posFin = 0;
           posFin = eval(parseInt(num) - 1);
           for (var j = posIni; j <= posFin; j++) {
              if ( form.chkContactType[j].checked ){
                 v_cad_contact_type = v_cad_contact_type + form.chkContactType[j].value + ",";
                 v_count++;
              }
           }
           form.hdnNumContactsSelected.value = v_count;
           form.hdnCadContactsSelected.value = v_cad_contact_type;
      }
   }
                  
   function fxCheckOthers(contador){
     var totalEditables = 0;
     var jobTitleDsc, ljobTitleDsc;
     var cont = contador - 1;
     
     form = document.formdatos;
     if(form.hdnCounter.value > 1){
       for(i=0; i < form.hdnCounter.value; i++){
          if ( eval("form.hdnEdicion["+i+"].value")=='1' ){
             totalEditables = eval(parseInt(totalEditables) + 1);
          }
       }
     }
     if(totalEditables > 1){
       jobTitleDsc = form.txtJobTitleDesc[cont].value;
       ljobTitleDsc=trim(jobTitleDsc.toLowerCase())
       if (form.cmbJobTitle[cont].value == 19) {
          if ( ljobTitleDsc == "" ) {
             alert("Si cargo es OTROS debe llenar descripción");
             eval("form.txtJobTitleDesc["+cont+"].focus()");
             return false;
          }
       }else {
          if ( ljobTitleDsc != "" ) {
             alert("No debe llenar descripción de cargo");
             eval("form.txtJobTitleDesc["+cont+"].select()");
             eval("form.txtJobTitleDesc["+cont+"].focus()");
             return false;
          }
       }
     }else{
       jobTitleDsc = form.txtJobTitleDesc.value;
       ljobTitleDsc = trim(jobTitleDsc.toLowerCase())
       if (form.cmbJobTitle.value == 19) {
          if ( ljobTitleDsc == "" ) {
             alert("Si cargo es OTROS debe llenar descripción");
             form.txtJobTitleDesc.focus();
             return false;
          }
       }else {
          if ( ljobTitleDsc != "" ) {
             alert("No debe llenar descripción de cargo");
             form.txtJobTitleDesc.select();
             form.txtJobTitleDesc.focus();
             return false;
          }
       }
     }
     return true;
   }

   function fxValidate(){
     var totalEditables=0;
     var num;
     var numNoEditables = 0;
     var numEditables = 0;
     var aux = "";
     var cont = 0;
     form = document.formdatos;
     if (form.flg_enabled.value == 0) return false;
     if(form.hdnCounter.value > 1){
       for(i=0; i < form.hdnCounter.value; i++){
          if ( eval("form.hdnEdicion["+i+"].value")=='1' ){
             totalEditables = eval(parseInt(totalEditables) + 1);
          }
       }
       for ( i = 0; i < form.hdnCounter.value; i++ ) {
          numEditables = eval(parseInt(i) - parseInt(numNoEditables));
          //Los datos son editables???
          if ( eval("form.hdnEdicion["+i+"].value")=='0' ){
              numNoEditables = eval(parseInt(numNoEditables) + 1);
              continue;
          }
          // validacion de Anexo
          if(totalEditables>1){
             if (eval("form.txtAnexoPhone["+numEditables+"].value != ''") && eval("form.txtNumTelefono["+numEditables+"].value == ''") ) {
               alert("Debe ingresar el Número de Teléfono para el Anexo.");
               eval("form.txtNumTelefono["+numEditables+"].focus()");
               return false;
             }
          }
          else {
             if (form.txtAnexoPhone.value != '' && form.txtNumTelefono.value == ''){
               alert("Debe ingresar el Número de Teléfono para el Anexo.");
               form.txtNumTelefono.focus();
               return false;
             }
          }
   
          //Seleccionó por lo menos un tipo de direccion???
          //avanzamos hasta los checks que corresponden a la dirección
          posIni = 0;
          posFin = 0;
          for(var k = 0; k < i; k++){
             posIni = eval(parseInt(posIni) + parseInt(eval("form.hdnNumContactsAvailable["+k+"].value")));
          }
          //numero de direcciones permitidas a seleccionar
          num = eval("form.hdnNumContactsAvailable["+i+"].value");
          posFin = eval(parseInt(posIni) + eval(parseInt(num) - 1));
          exp = "";
          if (posFin>posIni){
             for (var k = posIni; k <= posFin; k++) {
                exp = exp + " !form.chkContactType["+k+"].checked ";
                if (k<posFin){
                   exp = exp + "&&";
                }
             }
          }else{
             if(posFin>0){
                exp = "!form.chkContactType.checked ";
             }
          }
          if(exp != ""){
            bool_exp = eval(exp);
            if (bool_exp){
                alert("Seleccione por lo menos un tipo de contacto.");
                if(totalEditables>1){
                   eval("form.cmbTitle["+numEditables+"].focus()");
                }else{
                   form.cmbTitle.focus();
                }
                return false;
            }
          }          
          
          <% if ("JURIDICA".equals(strTypePerson)){%>             
              if(totalEditables>1){
                if ( eval("form.txtFirstName["+numEditables+"].value == ''") ) {
                   alert("Debe ingresar el nombre del Contacto");
                   eval("form.txtFirstName["+numEditables+"].focus()");
                   return false;
                }
                if ( eval("form.txtLastName["+numEditables+"].value == ''") ) {
                   alert("Debe ingresar los apellidos del Contacto");
                   eval("form.txtLastName["+numEditables+"].focus()");
                   return false;
                }
                if ( eval("form.cmbJobTitle["+numEditables+"].selectedIndex == 0") ) {
                   alert("Debe seleccionar el cargo del Contacto");
                   eval("form.cmbJobTitle["+numEditables+"].focus()");
                   return false;
                }
                if ( eval("form.txtEmail["+numEditables+"].value != ''") ) {
                   if( !eval("isEmailAddress( form.txtEmail["+numEditables+"] )") ) {
                      return false;
                   }
                }
              }else{
                if ( form.txtFirstName.value == '') {
                    alert("Debe ingresar el nombre del Contacto");
                   form.txtFirstName.focus();
                   return false;
                }
                if ( form.txtLastName.value == '' ) {
                   alert("Debe ingresar los apellidos del Contacto");
                   form.txtLastName.focus();
                   return false;
                }
                if ( form.cmbJobTitle.selectedIndex == 0 ) {
                   alert("Debe seleccionar el cargo del Contacto");
                   form.cmbJobTitle.focus();
                   return false;
                }
                if ( form.txtEmail.value != '' ) {
                   if( !isEmailAddress(form.txtEmail) ) {
                      return false;
                   }
                }
              }               
          <%}else{%>                 
              if(totalEditables>1){
                  if ( eval("form.txtFirstName["+numEditables+"].value == ''") ) {
                     alert("Debe ingresar el nombre de la Persona");
                     eval("form.txtFirstName["+numEditables+"].focus()");
                     return false;
                  }
                  if ( eval("form.txtLastName["+numEditables+"].value == ''") ) {
                     alert("Debe ingresar los apellidos de la Persona");
                     eval("form.txtLastName["+numEditables+"].focus()");
                     return false;
                  }
                  if ( eval("form.cmbJobTitle["+numEditables+"].selectedIndex == 0") ) {
                     alert("Debe seleccionar el cargo de la Persona");
                     eval("form.cmbJobTitle["+numEditables+"].focus()");
                     return false;
                  }
                  if ( eval("form.txtEmail["+numEditables+"].value != ''") ) {
                     if( !eval("isEmailAddress( form.txtEmail["+numEditables+"] )") ) {
                        return false;
                     }
                  }
              }else{
                  if ( form.txtFirstName.value == '' ) {
                     alert("Debe ingresar el nombre de la Persona");
                     form.txtFirstName.focus();
                     return false;
                  }
                  if ( form.txtLastName.value == '' ) {
                     alert("Debe ingresar los apellidos de la Persona");
                     form.txtLastName.focus();
                     return false;
                  }
                  if ( form.cmbJobTitle.selectedIndex == 0 ) {
                     alert("Debe seleccionar el cargo de la Persona");
                     form.cmbJobTitle.focus();
                     return false;
                  }
                  if ( form.txtEmail.value != '' ) {
                     if( !isEmailAddress( form.txtEmail) ) {
                        return false;
                     }
                  }
              }               
          <%}%>
          
          //Verifica otros.
          if(totalEditables>1){
            if ( eval("form.cmbJobTitle["+numEditables+"].value == '19'") ) {
               if ( eval("form.txtJobTitleDesc["+numEditables+"].value == ''") ) {
                  alert("Si cargo es OTROS debe llenar descripción");
                  eval("form.txtJobTitleDesc["+numEditables+"].focus()");
                  return false;
               }
            }else {
               if ( eval("form.txtJobTitleDesc["+numEditables+"].value != ''") ) {
                  alert("No debe llenar descripción de cargo");
                  eval("form.txtJobTitleDesc["+numEditables+"].select()");
                  eval("form.txtJobTitleDesc["+numEditables+"].focus()");
                  return false;
               }
            }
          }else{
            if ( form.cmbJobTitle.value == '19' ) {
               if ( form.txtJobTitleDesc.value == '' ) {
                  alert("Si cargo es OTROS debe llenar descripción");
                  form.txtJobTitleDesc.focus();
                  return false;
               }
            }else {
               if ( form.txtJobTitleDesc.value != '' ) {
                  alert("No debe llenar descripción de cargo");
                  form.txtJobTitleDesc.select();
                  form.txtJobTitleDesc.focus();
                  return false;
               }
            }
          }
       } //fin del for        
     }else{
        if(form.hdnEdicion.value=='1'){
          // Validar Tipo de contactos
          //numero de contactos permitidos a seleccionar
          num = form.hdnNumContactsAvailable.value;
          exp = "";
          if (num>1){
             for (var i = 0; i < num; i++) {
                exp = exp + " !form.chkContactType["+i+"].checked ";
                if (i<num-1){
                   exp = exp + "&&";
                }
             }
          }else{
             exp = "!form.chkContactType.checked ";
          }
          bool_exp = eval(exp);
          if (bool_exp){
              alert("Seleccione por lo menos un tipo de contacto.");
              form.cmbTitle.focus();
              return false;
          }
       
          <% if("JURIDICA".equals(strTypePerson)){%>
             if ( form.txtFirstName.value == '') {
                alert("Debe ingresar el nombre del Contacto");
                form.txtFirstName.focus();
                return false;
             }
             if ( form.txtLastName.value == '' ) {
                alert("Debe ingresar los apellidos del Contacto");
                form.txtLastName.focus();
                return false;
             }
             if ( form.cmbJobTitle.selectedIndex == 0 ) {
                alert("Debe seleccionar el cargo del Contacto");
                form.cmbJobTitle.focus();
                return false;
             }
             if ( form.txtEmail.value != '' ) {
                if( !isEmailAddress(form.txtEmail) ) {
                   return false;
                }
             }                
          <%}else{%>                
             if ( form.txtFirstName.value == '' ) {
                alert("Debe ingresar el nombre de la Persona");
                form.txtFirstName.focus();
                return false;
             }
             if ( form.txtLastName.value == '' ) {
                alert("Debe ingresar los apellidos de la Persona");
                form.txtLastName.focus();
                return false;
             }
             if ( form.cmbJobTitle.selectedIndex == 0 ) {
                alert("Debe seleccionar el cargo de la Persona");
                form.cmbJobTitle.focus();
                return false;
             }
             if ( form.txtEmail.value != '' ) {
                if( !isEmailAddress( form.txtEmail) ) {
                   return false;
                }
             }
           <%}%>
        }//hdnEdicion==1
     }
     if( !(fxCheckMandatoryContacts()) ){
        return false;
     }
     fxUpdateNumContactsSelected();
     form.flg_enabled.value = 0;
     return true;
   }

   function fxContact(firstName,lastName,middleName,descrOther,jobtitleid) {
      this.firstName    = firstName;
      this.lastName     = lastName;
      this.middleName   = middleName;
      this.jobtitleid   = jobtitleid;
      this.descrOther   = descrOther;
   }
   
   var arrContact = new Vector();
   
   function fxCancelAction(){       
       var url="<%=strURLContact%>";
       parent.mainFrame.location.replace(url);
   }


</script>
<%
   /*strType=objCustomerS.getCustomerType(lCustomerId);
   if (lSiteId!=0){*/
      strType=Constante.TYPE_CRM_CUSTOMER;
   //}
%>      
   <form method="post" name="formdatos" target="bottomFrame">
      <input type="hidden" name="hdnCustomerId" value="<%=lCustomerId%>">
      <input type="hidden" name="hdnSiteId" value="<%=lSiteId%>">
      <input type="hidden" name="hdnRegionId" value="<%=lRegionId%>">      
      <input type="hidden" name="hdnSessionId" value="<%=strSessionId%>">
      <input type="hidden" name="flg_enabled" value="1">
      <input type="hidden" name="myaction" value="1">
      <input type="hidden" name="hdnSessionId">      
      <input type="hidden" name="hdnOrderId" value="<%=lOrderId%>">
      <input type="hidden" name="hdnSpecificationId"   value="<%=strSpecificationId%>"> <!-- CEM COR0354 -->
	  
      <table border="0" cellspacing="1" cellpadding="0" width="85%" class="RegionBorder">
      <tr>
         <td class="CellLabelContent" COLSPAN="4" VALIGN="TOP" nowrap>
            <font class="Required">* : </font>Datos Obligatorios<br>
            <font class="Required">Observaci&oacute;n : </font>              
            <% //Contactos Obligatorios
               hshMandatContactType=objSiteS.getCheckMandatoryContacts(lCustomerId,lSiteId);
               arrMandatContactType=(ArrayList)hshMandatContactType.get("objContactType");
               arrMandatFlgContactType=(ArrayList)hshMandatContactType.get("objContactTypeFlg");
               strMessage=(String)hshMandatContactType.get("strMessage");              
               if (strMessage!=null){
                 throw new Exception(strMessage);            
               }
               if (arrMandatContactType.size()>0) {                                                  
            %>             
                 <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Contactos obligatorios - 
         
            <%    for (int i=0; i<arrMandatContactType.size();i++) {                       
                     hshMndtCntcTypAux=(HashMap)arrMandatContactType.get(i);                         
                     hshMndtCntcTypAux=(HashMap)arrMandatContactType.get(i);                                                                                   
            %>                            
                    <script>
                        ArrayMandatoryContact[ArrayMandatoryContact.length] = '<%=MiUtil.initCap((String)hshMndtCntcTypAux.get("contacttype"))%>';
                    </script>              
                       <b><%=MiUtil.initCap((String)hshMndtCntcTypAux.get("contacttype"))%></b>
            <%       if (i > 0 && (i!= arrMandatContactType.size())){                     
            %>          , 
            <%       }
                  }
               }
           //Contactos Unicos                                
               //hshUnqContacts = objSiteS.getUniqueContacts(lCustomerId,lSiteId) ; 
               hshUnqContacts=objSiteS.getCheckUniqueContacts(lCustomerId,lSiteId);
         
               if (strMessage!=null)
                  throw new Exception(strMessage);      
            
               arrUnqContacts= (ArrayList)hshUnqContacts.get("objContactType");
            %>
               <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Contactos únicos - 
            <%
               for (int j=0;j<arrUnqContacts.size();j++){ 
                  strCadena1=MiUtil.getValue(arrUnqContacts,j,"contacttype");
                  //hshUnqCntctsAux=(HashMap)arrUnqContacts.get(j);                                     
            %>  
                <script>
                   ArrayUniqueContact[ArrayUniqueContact.length] = '<%=MiUtil.initCap(strCadena1)%>';
                </script>
                <b><%=MiUtil.initCap(strCadena1)%></b>
         <%     if ((j+1)<arrUnqContacts.size()){                    
         %>        , 
         <%     }
               }
         %>
              <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              Un mismo contacto puede tener todos los tipos de contacto a la vez, o tener un contacto por cada tipo, o una combinaci&oacute;n de ellos.
         </td>
      </tr>
      </table>
      <!--Fin de Notas-->     
      <% //Obtenemos todos los tipos de direcciones         
      hshTypesContact=objSiteS.getTypeContacts(lCustomerId,lSiteId);
      arrTypesContact= (ArrayList)hshTypesContact.get("objContacttype");         
         
      //Obtenemos los tipos de direcciones que no pueden cambiar
      hshCheckNoChangeContacts=objSiteS.getCheckNoChangeContacts(lCustomerId,lSiteId);
      arrCheckNoChangeContact=(ArrayList)hshCheckNoChangeContacts.get("objContactType");
      arrCheckNoChangeContactFlag=(ArrayList)hshCheckNoChangeContacts.get("objContactTypeFlg");
      strMessage=(String)hshCheckNoChangeContacts.get("strMessage");         
      if (strMessage!=null)
         throw new Exception(strMessage);
      for (int wn=0;wn<arrCheckNoChangeContact.size();wn++ ){         
         //hshAux1=(HashMap)arrCheckNoChangeContact.get(wn);
         //hshAux2=(HashMap)arrCheckNoChangeContactFlag.get(wn);
         strCadena1=MiUtil.getValue(arrCheckNoChangeContact,wn,"contacttype");
         strCadena2=MiUtil.getValue(arrCheckNoChangeContactFlag,wn,"contacttypeflg");
         //strCadena1=MiUtil.getString((String)hshAux1.get("contacttype"));
         //strCadena2 =MiUtil.getString((String)hshAux2.get("contacttypeflg"));
%>
      <script>
         ArrayNoChangeContact[ArrayNoChangeContact.length] = '<%=MiUtil.initCap(strCadena1)%>';
         ArrayNoChangeContactFlag[ArrayNoChangeContactFlag.length] = '<%=MiUtil.initCap(strCadena2)%>';
      </script>           
         
<%    }  %>    
<script language="javascript">
   function fxAreaCode(n1,n2){
      this.name = n1;
      this.codearea = n2;
   }

   var vAreaCode = new Vector();

   var ncount = <%=iResultado%>;         
   function fxCalcularPhoneNumber(objThis, i){
      var nindex = i;
      if (ncount == 1) {
         eval("document.formdatos.hdnNumTelefono.value = document.formdatos.txtNumTelefono.value");
      }
      else if (ncount > 1){
         i=i-1;
         nindex=nindex-1;
         eval("document.formdatos.hdnNumTelefono["+i+"].value = document.formdatos.txtNumTelefono["+nindex+"].value");
      }
   }
         
   function fxCalcularPhoneArea(objThis, i){
      var nindex = i;
      if (ncount == 1) {
         eval("document.formdatos.hdnAreaCode.value = document.formdatos.txtAreaCode.value");
      }
      else if (ncount > 1){
         eval("document.formdatos.hdnAreaCode["+i+"].value = document.formdatos.txtAreaCode["+nindex+"].value");
      }
   }
   
   function fxCalcularFaxNumber(objThis, i){
      var nindex = i ;
      if (ncount == 1) {
         eval("document.formdatos.hdnFax.value = document.formdatos.txtFax.value");
      }
      else if (ncount > 1){
         eval("document.formdatos.hdnFax["+i+"].value = document.formdatos.txtFax["+nindex+"].value");
      }
   }

   function fxCalcularFaxArea(objThis, i){
      var nindex = i;
      if (ncount == 1) {
         eval("document.formdatos.hdnAreaCodeFax.value = document.formdatos.txtAreaCodeFax.value");
      }
      else if (ncount > 1){
         eval("document.formdatos.hdnAreaCodeFax["+i+"].value = document.formdatos.txtAreaCodeFax["+nindex+"].value");
      }
   }      

   function fxSearchAreaCode(code, nf_AreaCode ,nf_AreaNom, nf_telf,nf_hAreaCode,i){
      form = document.formdatos;
      var ncount = form.hdnCounter.value;
      var ni = i;
      if (code==""){
         if (ncount == 1) {
            eval("document.formdatos."+nf_AreaCode+".value = ''");
            eval(nf_AreaNom+ ".innerHTML = ''");
            eval("document.formdatos."+nf_telf+".focus();");
            return;
         }
         else if (ncount > 1){
            eval("document.formdatos."+nf_AreaCode+"["+ni+"].value = ''");
            eval(nf_AreaNom+ "["+i+"].innerHTML = ''");
            eval("document.formdatos."+nf_telf+"["+ni+"].focus();");
            return;
         }
      }
      cadena = "";
      bEntro = false;
      for(j=0;j<vAreaCode.size();j++){
         objAreaCode = vAreaCode.elementAt(j);
         if (objAreaCode.codearea==code){
            if (cadena!=""){ cadena = cadena+"/"; }
            cadena = cadena + objAreaCode.name;
            bEntro = true;
         }
      }
      if ( bEntro == true){
         if (ncount == 1) {
            eval("document.formdatos."+nf_AreaCode+".value = "+code);
            eval(nf_AreaNom + ".innerHTML = '("+cadena+")'");
            eval("document.formdatos."+nf_hAreaCode+".value = "+code);
            eval("document.formdatos."+nf_telf+".focus();");
         } else if (ncount > 1){
            eval("document.formdatos."+nf_AreaCode+"["+ni+"].value = "+code);
            eval(nf_AreaNom + "["+i+"].innerHTML = '("+cadena+")'");
            eval("document.formdatos."+nf_hAreaCode+"["+i+"].value = "+code);
            eval("document.formdatos."+nf_telf+"["+ni+"].focus();");
         }
      }else{

         fxCodeArea(nf_AreaCode, nf_AreaNom, nf_telf, nf_hAreaCode, i);
         if (ncount == 1) {
            eval("document.formdatos."+nf_AreaCode+".value = ''");
            eval("document.formdatos."+nf_hAreaCode+".value = ''");

         } else if (ncount > 1){
            eval("document.formdatos."+nf_AreaCode+"["+ni+"].value = ''");
            eval("document.formdatos."+nf_hAreaCode+"["+ni+"].value = ''");
         }
      }
      return bEntro;
   }        
        
   function fxCodeArea(nAreaCode, nAreaNom, ntelf, sAreaCode,i){
      form = document.formdatos;
      var ncount = form.hdnCounter.value;
      var nindex = i ;
      var param="?sAreaCode="+nAreaCode+"&sSpamArea="+nAreaNom+"&sTelfName="+ntelf+"&sFormName=formdatos&nCount="+ncount+"&nIndex="+nindex+"&hAreaCode="+sAreaCode;
      url="<%=strURLAreaCode%>"+param;
      window.open(url ,"WinCustomer","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,screenX=60,top=40,left=60,screenY=40,width=330,height=400");
   }
     
    function fxValidatePhone(obj,str){
       if (obj.value != ""){
          if (!ContentOnlyNumber(obj.value)){
             alert("El campo "+ str +" debe ser númerico");
             obj.value="";
             obj.focus();
             return false;
          }
          return true;
       }
    }

   function fxSubmit(){
       form = document.formdatos;
       if (fxValidate()==false) 
           return; 
       form.myaction.value="UpdateContact";    
       form.hdnSessionId.value="<%=strSessionId%>";
	   form.hdnSpecificationId.value="<%=strSpecificationId%>"; //CEM COR0354
       form.action='<%=strURLOrderServlet%>';          
       form.submit();                             
   }         
</script>
<%   
      int wn_counter = 0;                  
      long lPersonId=0;
      String strLastName=null;
      String strFistName=null;
      String strMidName=null;
      String strTitle=null;
      String strEmail=null;
      String strMainPhoneArea=null;
      String strMainPhone=null;
      String strMainFaxArea=null;
      String strFax=null;
      String strAnexo=null;
      long lJobTitleId=0;
      String strDescripcion=null;          
      int iGerente=0;
      int iUsuario=0;
      int iFacturacion=0;          
      int iVentas=0;
      int iSistemas=0;
      int iComunicac=0;
      int iData=0;
      int iCobranza=0;
      HashMap hshContact=null;
      String strMainPhoneDep=null;
      String strMainFaxDep=null;           

      for (int h=0;h<arrContacts.size();h++){
         hshContact=(HashMap)arrContacts.get(h);
         lPersonId = Long.parseLong((String)hshContact.get("swpersonid"));
         strLastName   = MiUtil.getString((String)hshContact.get("swlastname"));
         strFistName   = MiUtil.getString((String)hshContact.get("swfirstname"));
         strMidName    = MiUtil.getString((String)hshContact.get("swmiddlename"));
         strTitle    = MiUtil.getString((String)hshContact.get("swtitle"));
         strEmail    = MiUtil.getString((String)hshContact.get("mail"));
         strMainPhoneArea  = MiUtil.getString((String)hshContact.get("phonearea"));
         strMainPhone   = MiUtil.getString((String)hshContact.get("phone"));
         strMainFaxArea = MiUtil.getString((String)hshContact.get("faxarea"));
         strFax  = MiUtil.getString((String)hshContact.get("fax"));
         strAnexo  = MiUtil.getString((String)hshContact.get("anexo"));
         lJobTitleId  = MiUtil.parseLong((String)hshContact.get("swjobtitleid"));
         strDescripcion  = MiUtil.getString((String)hshContact.get("swdescription"));              
         iGerente    = Integer.parseInt((String)hshContact.get("GerentG"));
         iUsuario    = Integer.parseInt((String)hshContact.get("Usuario"));
         iFacturacion    = Integer.parseInt((String)hshContact.get("Facturacion"));
         iVentas    = Integer.parseInt((String)hshContact.get("Ventas"));
         iSistemas    = Integer.parseInt((String)hshContact.get("Sistemas"));
         iComunicac    = Integer.parseInt((String)hshContact.get("Comunicaciones"));
         iData    = Integer.parseInt((String)hshContact.get("Data"));
         iCobranza    = Integer.parseInt((String)hshContact.get("Cobranza"));
         strMainPhoneDep=objGeneralS.getDepartmentName(strMainPhoneArea);
         strMainFaxDep=objGeneralS.getDepartmentName(strMainFaxArea);                
         wn_counter = wn_counter + 1;
         strNochangeConttypeSelected = "";
         iCountEditable = wn_counter - iCountNoEditable;
         
         /*if (Constante.TYPE_CRM_CUSTOMER.equals(strType)){            
            iEdicion =1;  //Cuando es prospecto
         }else if (iUsuario==1 || iFacturacion==1 || iGerente==1){
            //Cuando es Cliente y y el contacto es uno de ellos. ???NO INTERCAMBIABLE.************
            iEdicion = 0;
            iCountNoEditable = iCountNoEditable + 1;
         }else{
            iEdicion =1;
         }*/
         iEdicion =1;
    %>
      <input type="hidden" name="hdnPersonId" value="<%=lPersonId%>">
      <input type="hidden" name="hdnEdicion" value="<%=iEdicion%>">
      <table border="0" cellspacing="0" cellpadding="0" width="80%">
      <tr>
         <td width="100%"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/pobtrans.gif" border="0" height="7"></td>
      </tr>
      </table>
      <table border="0" cellspacing="0" cellpadding="0">
      <tr class="PortletHeaderColor">
         <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="16"></td>
         <td class="SubSectionTitle" align="LEFT" valign="top">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Contacto&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
         <td class="SubSectionTitleRightCurve" valign="top" align="right" width="12"></td>
      </tr>
      </table>          
     <table width="100%" border="0" cellspacing="1" cellpadding="0" class="RegionBorder">
        <tr><!--Inicio fila de tipos de contactos-->
           <td class="CellLabel" valign="top" width="120" nowrap><font class="Required">*</font>Tipo Contacto&nbsp;</td>
           <td class="CellContent" colspan="3">
               <table width="375" cellpadding="0" cellspacing="0" align="left">
               <%    
               int wn = 0;
               int wk = 0;
               int iFlagCheck = 0;
               iNumContAvailable = 0;
               for (int wj=0;wj<arrTypesContact.size();wj++){                   
                  if ((wn%4)==0){                     
                     if (wn>0){  %>
                     </tr>
               <%    }
                     wn = 0;                           
               %>
                     <tr>                          
               <% }
                  //hshAux1=(HashMap)arrTypesContact.get(wj);//Contiene el listado de todas los tipos de contactos
                  strCadena1=MiUtil.getValue(arrTypesContact,wj,"contact");
                  //strCadena1=(String)hshAux1.get("contact");
                  
                  //El tipo de contacto se encuentra seleccionado??
                  if ("Usuario".equals(strCadena1)){                        
                     if (iUsuario == 1){
                        iFlagCheck = 1;
                     }
                  }else if ("Facturacion".equals(strCadena1)){
                     if (iFacturacion == 1) {
                        iFlagCheck = 1;
                     }
                  }else if ("Gerente General".equals(strCadena1)){
                     if ( iGerente ==1){
                        iFlagCheck = 1;
                     }
                  }else if ("Ventas".equals(strCadena1)) {
                     if ( iVentas == 1){
                        iFlagCheck = 1;
                     }
                  }else if ("Sistemas".equals(strCadena1)) {
                     if ( iSistemas == 1){
                        iFlagCheck = 1;
                     }
                  }else if( "Comunicaciones".equals(strCadena1)) {
                     if ( iComunicac == 1){
                      iFlagCheck = 1;
                     }
                  } else if( "Data".equals(strCadena1)) {
                     if ( iData == 1){
                       iFlagCheck = 1;
                     }
                  }
                  //Verificar si este contacto no es intercambiable
                  wk = 0;
                  strCadena2=MiUtil.initCap(MiUtil.getValue(arrCheckNoChangeContact,wk,"contacttype"));
                  //hshAux2=(HashMap)arrCheckNoChangeContact.get(wk);                         
                  //strCadena2=(String)hshAux2.get("contact");
                  while (wk <= arrCheckNoChangeContact.size() && (!strCadena1.equals((strCadena2)))){         
                     //hshAux2 =(HashMap)arrCheckNoChangeContact.get(wk+1);      
                     strCadena2 = MiUtil.initCap(MiUtil.getValue(arrCheckNoChangeContact,wk+1,"contacttype"));
                     //strCadena2 = MiUtil.initCap((String)hshAux2.get("contact"));                             
                     wk = wk + 1;
                  }
                  if (wk < arrCheckNoChangeContact.size()){                        
                     if (iFlagCheck == 1){ //direccion no intercambiable en posicion wk                          
                       // if (iEdicion != 1){                              
                        %>                
                        <%-- td class="CellContent"><input type="checkbox" name="chkContactDisabled"  value="<%=MiUtil.initCap(strCadena1)%>" checked disabled><%=MiUtil.getString(strCadena1)%></td --%>
                        <%  
                        /*strNochangeConttypeSelected = strNochangeConttypeSelected + " , " +  MiUtil.getString(strCadena1);
                        }else{*/
                        %>                                
                        <td class="CellContent"><input type="checkbox" name="chkContactType" onclick="fxCheckUniqueContacts(this)" value="<%=MiUtil.getString(strCadena1)%>" checked><%=MiUtil.initCap(strCadena1)%></td>
                        
                        <% iNumContAvailable = iNumContAvailable + 1;
                        //}
                     }else{
                     %>        
                     <td class="CellContent"><input type="checkbox" name="chkContactType" onclick="fxCheckUniqueContacts(this)" value="<%=MiUtil.getString(strCadena1)%>" <% if (strCadena1.equals(strPedido)){%> disabled <%}%> ><%=MiUtil.initCap(strCadena1)%></td>                    
                     <%    iNumContAvailable = iNumContAvailable + 1;
                     }
                  }else{   
                     if (iFlagCheck == 0){                           
                     %>              
                     <td class="CellContent"><input type="checkbox" name="chkContactType" onclick="fxCheckUniqueContacts(this)" value="<%=MiUtil.getString(strCadena1)%>" <% if (strCadena1.equals(strPedido)){%> disabled <%}%> ><%=MiUtil.initCap(strCadena1)%></td>
                     <%}else{                                 
                     %>
                     <td class="CellContent"><input type="checkbox" name="chkContactType" onclick="fxCheckUniqueContacts(this)" value="<%=MiUtil.getString(strCadena1)%>" checked><%=MiUtil.initCap(strCadena1)%></td>
                     <%  
                     }
                     iNumContAvailable = iNumContAvailable + 1;
                  }
                  iFlagCheck = 0;
                  wn = wn + 1;
               } // del for
               if ((4 - wn )>0 ){ //completar la última fila con celdas en blanco.
                  for (int k =0;k< 4 - wn;k++ ){                        
                  %>
                  <td class="CellContent">&nbsp;</td>
                  <%  
                  }%>
               </tr>
               <%} %>
               
               <input type="hidden" name="hdnNumContactsAvailable" value="<%=iNumContAvailable%>">
               <input type="hidden" name="hdnNumContactsSelected" value="">
               <input type="hidden" name="hdnCadContactsSelected" value="">
               </table>
            </td>
         </tr>
         <tr>
            <td class="CellLabel" valign="top">&nbsp;Título&nbsp;</TD>
            <td class="CellContent" valign="top" colspan="3">
            <%
             if (iEdicion == 1){          
            %>               
               <select name="cmbTitle">                         
                  <% hshData = objGeneralS.getTableList("PERSON_TITLE","1"); 
                     strMessage=(String)hshData.get("strMessage");
                     if (strMessage!=null)
                        throw new Exception(strMessage);                               
                     arrLista=(ArrayList)hshData.get("arrTableList");                    
                  %>
                  <%=MiUtil.buildComboSelected(arrLista,"wv_npValueDesc","wv_npValueDesc",strTitle)%>                                      
               </select>  
          <%              
            }else{
          %>
              <%=strTitle%>
          <%}%>            
            </td>
         </tr>
         <tr>
            <td class="CellLabel"><font class="Required">*</font>Nombres&nbsp;</TD>
            <td class="CellContent" colspan="3">           
               <%if (iEdicion == 1){ %>
                  <input type="text" name="txtFirstName" size="40" maxlength="40" onchange="this.value=trim(this.value.toUpperCase())">              
               <%}else{ %>
                  <%=strFistName%>
               <%}%>               
            </td>
         </tr>
         <tr>
            <td class="CellLabel" ><font class="Required">*</font>Ap.Paterno&nbsp;</td>
            <td class="CellContent">           
               <% if (iEdicion == 1){ %>            
                  <input type="text" name="txtLastName" size="40" maxlength="20" onChange="this.value=trim(this.value.toUpperCase())">
               <%}else{ %>
                     <%=strLastName%>
               <%}%>           
            </td>
            <td class="CellLabel" ><font class="Required">*</font>Ap.Materno&nbsp;</td>
            <td class="CellContent">
               <% if (iEdicion == 1){ %>               
                  <input type="text" name="txtMiddleName" size="40" maxlength="20" onChange="this.value=trim(this.value.toUpperCase())">
               <%}else{ %>
                     <%=strMidName%>
               <%}%>  
            </td>
         </tr>       
         <% if (iEdicion == 1){ %>   
            <script>
               arrContact.addElement(new fxContact("<%=strFistName%>","<%=strLastName%>","<%=strMidName%>","<%=strDescripcion%>","<%=lJobTitleId%>"));
            </script>
         <%}%> 
         <tr>         
            <td class="CellLabel"><font class="Required">*</font>Cargo&nbsp;</td>
            <td class="CellContent">
            <% if (iEdicion == 1){ %> 
               <select name="cmbJobTitle"  style="width: 90%;" onchange="return fxCheckOthers('<%=iCountEditable%>')">                         
               <% hshData = objGeneralS.getTitleList(); 
                  strMessage= (String)hshData.get("strMessage");
                  if (strMessage!=null)
                     throw new Exception(strMessage);                              
                  arrLista=(ArrayList)hshData.get("arrTitleList");                   
               %>
               <%=MiUtil.buildComboSelected(arrLista,"jobtitlteId","descripcion",lJobTitleId+"")%>                                      
               </select>                       
            <%}else{ %>
               <%=strDescripcion%>
            <%}%>   
            <td class="CellLabel" nowrap>&nbsp;OTROS descr.&nbsp;</td>
            <td class="CellContent">
            <% if (iEdicion == 1){ %>                
               <input type="text" name="txtJobTitleDesc" size="40" maxlength="40" onChange="this.value=trim(this.value.toUpperCase());return fxCheckOthers('<%=iCountEditable%>')">
            <%}else{%>                 
               <% if (lJobTitleId == 19){ %>
                  <%=strDescripcion%>
               <%}else{%>
                  &nbsp;
               <%}%>
            <%}%>         
            </td>
         </tr>     
         <tr>          
         <% if (iEdicion == 1){ %>              
            <td class="CellLabel"><a href="javascript:fxCodeArea('txtAreaCode','spanAreaNomPhone','txtNumTelefono','hdnAreaCode','<%=iCountEditable-1%>')">Área</a>/Teléfono</td>
            <td class="CellContent">
               <input type="text" name="txtAreaCode" value="<%=strMainPhoneArea%>" size="1" maxlength="2" onchange="this.value=trim(this.value);return fxSearchAreaCode(this.value, 'txtAreaCode','spanAreaNomPhone','txtNumTelefono','hdnAreaCode',<%=iCountEditable-1%>)">
               <input type="text" name="txtNumTelefono" value="<%=strMainPhone%>" size="7" maxlength="30" onchange="javascript:fxCalcularPhoneNumber(this,<%=iCountEditable-1%>);">
               <input type="hidden" name="hdnAreaCode" value="<%=strMainPhoneArea%>">
               <input type="hidden" name="hdnNumTelefono" value="<%=strMainPhone%>">
               <small><span id="spanAreaNomPhone"><%=MiUtil.getString(MiUtil.decode(strMainPhoneDep,null,null,strMainPhoneDep))%></span></small>
            </td>            
         <%}else{%>            
            <td class="CellLabel">Área/Teléfono</td>
            <td class="CellContent"><%=strMainPhoneArea%>&nbsp;<%=strMainPhone%>
               <small><%=MiUtil.getString(MiUtil.decode(strMainPhoneDep,null,null,strMainPhoneDep))%></small>
            </td>         
         <%}%>
         <% if (iEdicion == 1){ %>                          
            <td class="CellLabel">Anexo</td>
            <td class="CellContent">
               <input type="text" name="txtAnexoPhone" value="<%=strAnexo%>" size="3" maxlength="5" onBlur="javascript:fxValidatePhone(this,'<%=strAnexo%>');" >
         <%}else{%>           
            <td class="CellLabel"></td>
            <td class="CellContent"></small>
         <%}%>
            </td>
         </tr>
         <tr>
        <% if (iEdicion == 1){ %>          
            <td class="CellLabel">&nbsp;<a href="javascript:fxCodeArea('txtAreaCodeFax','spanAreaNomFax','txtFax','hdnAreaCodeFax',<%=iCountEditable-1%>)">Área</a>/Fax</td>
            <td class="CellContent">
               <input type="text" name="txtAreaCodeFax" value="<%=strMainFaxArea%>" size="1" maxlength="2" onchange="this.value=trim(this.value);return fxSearchAreaCode(this.value, 'txtAreaCodeFax','spanAreaNomFax','txtFax','hdnAreaCodeFax',<%=iCountEditable-1%>)">
               <input type="text" name="txtFax" value="<%=strFax%>" size="7" maxlength="8" onchange="javascript:fxCalcularFaxNumber(this,<%=iCountEditable-1%>); ">
               <input type="hidden" name="hdnAreaCodeFax" value="<%=strMainFaxArea%>">
               <input type="hidden" name="hdnFax" value="<%=strFax%>">
               <small><span id="spanAreaNomFax"><%=MiUtil.getString(MiUtil.decode(strMainFaxDep,null,null,strMainFaxDep))%></span></small>
        <%}else{%>            
            <td class="CellLabel">Área/Fax</td>
            <td class="CellContent">(<%=strMainFaxArea%>)<%=strFax%>
               <small><%=MiUtil.getString(MiUtil.decode(strMainFaxDep,null,null,strMainFaxDep))%></small>
        <%}%>
            </td>
            <td class="CellLabel">&nbsp;Email&nbsp;</td>
            <td class="CellContent" colspan="2">
          <% if (iEdicion == 1){ %>      
                <input type="text" name="txtEmail" size="40" maxlength="40" value="<%=strEmail%>" onchange="this.value=trim(this.value.toLowerCase())">               
          <%}else{%> 
                <%=strEmail%>
          <%}%>            
            </td>
         </tr>
         </table>
         <table border="0" cellspacing="0" cellpadding="0" width="60%">
         <tr>
            <TD width="100%"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/pobtrans.gif" border="0" height="7"></td>
         </tr>
         </table>
         <input type="hidden" name="hdnNoChangeContTypeSelected" value="<%=strNochangeConttypeSelected%>">
     <%   
      } //del loop
     %>
      <input type="hidden" name="hdnCounter" VALUE="<%=arrContacts.size()%>">
      <br>
      <table width="821" cellpadding="0" cellspacing="1" >
      <tr> 
         <td align="right">
            <input type="button" value="   Guardar   " name="btnsubmit" onclick="javascript:fxSubmit();">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
         </td>                    
         <td align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="button" value="   Cancelar   " name="btnCancelar" onClick="javascript:fxCancelAction();">
         </td>
      </tr>
      </table>     
      <script language="JavaScript">
         var Form = document.formdatos;
         if ( Form.txtFirstName != null ) {
            if (Form.txtFirstName.length > 1) {
               for(idx = 0; idx < arrContact.size(); idx++) {
                  objContact = arrContact.elementAt(idx);
                  Form.txtFirstName[idx].value     = objContact.firstName;
                  Form.txtLastName[idx].value      = objContact.lastName;
                  Form.txtMiddleName[idx].value    = objContact.middleName;
                  Form.txtJobTitleDesc[idx].value  = (objContact.jobtitleid != 19)?"":objContact.descrOther;
               }
            }else {
               objContact = arrContact.elementAt(0);
               Form.txtFirstName.value    = objContact.firstName;
               Form.txtLastName.value     = objContact.lastName;
               Form.txtMiddleName.value   = objContact.middleName;
               Form.txtJobTitleDesc.value = (objContact.jobtitleid != 19)?"":objContact.descrOther;
            }
         }
      
         //fxDisabledNoChangeContact();
      </script>
   </form>  
<% 
   hshData=objGeneralS.getAreaCodeList(null,null);
   strMessage=(String)hshData.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);       
   ArrayList arrAreaCode=(ArrayList)hshData.get("arrAreaCodeList");
   
   HashMap hshMapData=null;
   for (int i=0;i<arrAreaCode.size();i++){
      hshMapData=((HashMap)arrAreaCode.get(i));                  
%>
   <script>                
      vAreaCode.addElement(new fxAreaCode('<%=MiUtil.getString((String)hshMapData.get("city"))%>','<%=MiUtil.getString((String)hshMapData.get("areaCode"))%>'));
   </script>
<%  }
}catch(Exception ex){
   System.out.println("Error try catch-->"+ex.getMessage()); 
%>
<script>
   fxShowMessage("<%=MiUtil.getMessageClean(ex.getMessage())%>");
</script>
<%
}
%>  