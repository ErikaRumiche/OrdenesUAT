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
<!--Source DynamicSectionEdit/Site/ContactNew.jsp -->
<%
try{
   long lCustomerId=(request.getParameter("nCustomerId")==null?0:Long.parseLong(request.getParameter("nCustomerId")));
   long lSiteId=(request.getParameter("nSiteId")==null?0:Long.parseLong(request.getParameter("nSiteId")));
   long lRegionId=(request.getParameter("nRegionId")==null?0:Long.parseLong(request.getParameter("nRegionId")));
   long lOrderId=(request.getParameter("nOrderId")==null?0:Long.parseLong(request.getParameter("nOrderId")));  
   String strSessionId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));
   String strSpecificationId=(request.getParameter("pSpecificationId")==null?"0":request.getParameter("pSpecificationId")); //CEM COR0354
   
   GeneralService objGeneralService=new GeneralService();
   SiteService objSiteService=new SiteService();
   CustomerService objCustomerService=new CustomerService();
   String strMessage=null;
   
   String strPedido = "Pedido";
   String strType=null;
   String strContactType=null;
   String strUnqContact=null;
   String strUnqFlgType=null;
   HashMap hshTypeContacts=null;
   HashMap hshCheckUnqContacts=null;  
   HashMap hshMandatContactType=null;
   HashMap hshMndtCntcTypAux=null;
   HashMap hshMndtFlgCntcTypAux=null;
   //HashMap hshUnqContacts=null;
   HashMap hshUnqCntctsAux=null;
   HashMap hshData=null;
   ArrayList arrTypeContacts=null;
   ArrayList arrCheckUnqContactsType=null;
   ArrayList arrCheckUnqFlgContactsType=null;
   ArrayList arrMandatContactType=null;
   ArrayList arrMandatFlgContactType=null;
   ArrayList arrUnqContacts=null;
   ArrayList arrLista=null;
   
   String strObjectType=null;
   long lObjectId=0;   
   
   /*Cambio de Cuenta de Facturación*/
   String strStatusBA = MiUtil.getString(objGeneralService.getValue(Constante.NPT_SECTION_BA_FROM_SITE,Constante.KN_ORDERS));
   
   
   //String strParam="?nOrderId="+lOrderId+"&nCustomerId="+lCustomerId+"&nSiteId="+lSiteId+"&hdnSessionId="+strSessionId;
   String strParam="?nOrderId="+lOrderId+"&nCustomerId="+lCustomerId+"&nSiteId="+lSiteId+"&hdnSessionId="+strSessionId+"&pSpecificationId="+strSpecificationId; //CEM COR0354
   
   //RUTAS
   String strURLGeneralData="SiteGeneralNew.jsp"+strParam;
   String strURLDireccion="AddressList.jsp"+strParam;
   String strURLBillAcc="BillingAccountList.jsp"+strParam;     
   String strURLContact="ContactList.jsp"+strParam;  
   String strURLAreaCode=request.getContextPath()+"/GENERALPAGE/AreaCodeList.jsp";
   String strURLOrderServlet =request.getContextPath()+"/editordersevlet";     
   
   System.out.println(" -------------------- INICIO CONTACT_NEW.jsp---------------------- ");  
   //System.out.println("strAction :" + strAction);
   System.out.println("lSiteId :" + lSiteId);
   System.out.println("lCustomerId :" + lCustomerId);  
   System.out.println("lOrderId :" + lOrderId);
   System.out.println("strSpecificationId :" + strSpecificationId);
   System.out.println(" --------------------  FIN CONTACT_NEW.jsp---------------------- ");
%>    
    
<% 
    if (lSiteId != 0){
       strObjectType="SITE";
       lObjectId=lSiteId;
    }else{
       strObjectType="CUSTOMER";
       lObjectId= lCustomerId;
    }
%>  
      <script language="javascript">
         function fxCancelAction(){
            url="<%=strURLContact%>";
            parent.mainFrame.location.replace(url);
         }
      </script>
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
          
          <script language="JavaScript">
             var ArrayMandatoryContact = new Array();
             var ArrayMandatoryContactFlag = new Array();             
             function checkOthers(jobTitleCode,jobTitleDsc){
                ljobTitleDsc=trim(jobTitleDsc.value.toLowerCase())
                if (jobTitleCode.value == 19) {
                   if ( ljobTitleDsc == "" ) {
                      alert("Si cargo es OTROS debe llenar descripción");
                      jobTitleDsc.focus();
                      return false;
                   }
                }else {
                   if ( ljobTitleDsc != "" ) {
                      alert("No debe llenar descripción de cargo, si el cargo no es OTROS");
                      jobTitleDsc.select();
                      jobTitleDsc.focus();
                      return false;
                   }
                }
                return true;
             }
             function checkMandatoryContact(){
                var cont;
                v_form=document.formdatos;
                for(var j = 0; j < ArrayMandatoryContact.length ; j++){
                    if(ArrayMandatoryContactFlag[j] == '0' ){
                       cont = 0;
                       for(var i = 0; i < v_form.chkContactType.length; i++){
                          if((ArrayMandatoryContact[j] == v_form.chkContactType[i].value) && !(v_form.chkContactType[i].checked)){
                             alert("No se ha creado el tipo de contacto obligatorio "+ArrayMandatoryContact[j]);
                             return false;
                          }
                       }
                    }
                }
                return true;
             } 
             function fxValidate(){
                v_form  = document.formdatos;
                var exp = "";
                var bool_exp;                        
                var num;
                if (v_form.flg_enabled.value == 0) return false;
                //numero de contactos permitidos a seleccionar
                num = v_form.hdnNumContAvailable.value;
                if(num>1){
                   for (i = 0; i < num; i++) {
                      exp = exp + " !v_form.chkContactType["+i+"].checked ";
                      if (i<num-1){
                         exp = exp + "&&";
                      }
                   }
                }else{
                   exp = " !v_form.chkContactType.checked ";
                }
                bool_exp = eval(exp);
                if (bool_exp){
                    alert("Seleccione por lo menos un tipo de Contacto.");
                    return false;
                }
                if ( v_form.txtFirstName.value == '' ) {
                   alert("Debe ingresar el nombre del Contacto");
                   v_form.txtFirstName.focus();
                   return false;
                }
                if ( v_form.txtLastName1.value == '' ) {
                   alert("Debe ingresar el apellido paterno del Contacto");
                   v_form.txtLastName1.focus();
                   return false;
                }
                if ( v_form.cmbJobTitle.selectedIndex == 0 ) {
                   alert("Debe seleccionar el cargo del Contacto");
                   v_form.cmbJobTitle.focus();
                   return false;
                }else {
                   if( !checkOthers(v_form.cmbJobTitle,v_form.txtJobTitleDesc) ) {
                      return false;
                   }                   
                }
                if (v_form.txtAnexoPhone.value != '' && v_form.txtPhone.value == ''){
                  alert("Debe ingresar el Número de Teléfono para el Anexo.");
                  v_form.txtPhone.focus();
                  return false;
                }
                if ( v_form.txtEmail.value != "" ) {
                      if( !isEmailAddress( v_form.txtEmail ) ) {
                         return false;
                      }
                }
                if( !(checkMandatoryContact()) ){
                   return false;
                }                      
                v_form.flg_enabled.value = 0;
                return true;
             }

               function fxValidatePhone(obj, str){
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
          </script>      
<%          
         strType=objCustomerService.getCustomerType(lCustomerId);         

         //Obtener contactos obligatorios y únicos. CUSTOMER OR SITE        
         hshTypeContacts=objSiteService.getTypeContacts(lCustomerId,lSiteId);
         strMessage=(String)hshTypeContacts.get("strMessage");
         if (strMessage==null){
             arrTypeContacts= (ArrayList)hshTypeContacts.get("objContacttype");
         }else{             
             throw new Exception(strMessage);
         }          
         hshCheckUnqContacts=objSiteService.getCheckUniqueContacts(lCustomerId,lSiteId);
         arrCheckUnqContactsType= (ArrayList)hshCheckUnqContacts.get("objContactType");
         arrCheckUnqFlgContactsType= (ArrayList)hshCheckUnqContacts.get("objContactTypeFlg");
%>
        <form method="post" name="formdatos" target="bottomFrame">           
           <input type="hidden" name="hdnCustomerId" value="<%=lCustomerId%>">
           <input type="hidden" name="hdnSiteId" value="<%=lSiteId%>">
           <input type="hidden" name="hdnRegionId" value="<%=lRegionId%>">   
           <input type="hidden" name="hdnSessionId" value="<%=strSessionId%>">
           <input type="hidden" name="flg_enabled" value="1">
           <input type="hidden" name="myaction"/>   
           <input type="hidden" name="hdnOrderId" value="<%=lOrderId%>">
		   <input type="hidden" name="hdnSpecificationId"   value="<%=strSpecificationId%>"> <!-- CEM COR0354 -->
		   
           <TABLE BORDER="0" cellspacing="0" cellpadding="0" width="100%">
              <TR>
                 <TD width="100%"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/pobtrans.gif" border="0" height="5"></td>
              </TR>
           </TABLE>         
           <table border="0" cellspacing="1" cellpadding="0" width="60%" class="RegionBorder">
               <TR>
                  <TD CLASS="CellLabelContent" COLSPAN="4" VALIGN="TOP" nowrap>
                    <font class="Required">* : </font>Datos Obligatorios<br>
                    <font class="Required">Observaci&oacute;n : </font>                    
                    <!--Contactos Obligatorios-->
                    <%
                         hshMandatContactType=objSiteService.getCheckMandatoryContacts(lCustomerId,lSiteId);                         
                         strMessage=(String)hshMandatContactType.get("strMessage");
                    if (strMessage==null){                       
                       arrMandatContactType=(ArrayList)hshMandatContactType.get("objContactType");
                       arrMandatFlgContactType=(ArrayList)hshMandatContactType.get("objContactTypeFlg");
                    }else{                 
                       throw new Exception(strMessage);
                    }
                   
                    if (arrMandatContactType.size()>0) {                                                 
                  %>
                       <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Contactos obligatorios - 
                  <%   for (int i=0; i<arrMandatContactType.size();i++) {                       
                         hshMndtCntcTypAux=(HashMap)arrMandatContactType.get(i);                         
                         hshMndtFlgCntcTypAux=(HashMap)arrMandatFlgContactType.get(i);                                           
                  %>
                         <script>
                            ArrayMandatoryContact[ArrayMandatoryContact.length] = '<%=MiUtil.getString((String)hshMndtCntcTypAux.get("contacttype"))%>';
                            ArrayMandatoryContactFlag[ArrayMandatoryContactFlag.length] = '<%=MiUtil.getString((String)hshMndtFlgCntcTypAux.get("contacttypeflg"))%>';
                         </script>
                
                         <b><%=MiUtil.initCap((String)hshMndtCntcTypAux.get("contacttype"))%></b>
                  <%     if (i > 0 && (i!= arrMandatContactType.size())){  
                  %>       , 
                  <%      }
                       }
                    }
                    //Contactos Unicos                  
                      //hshUnqContacts = objSiteService.getUniqueContacts(lCustomerId,lSiteId) ; 
                     //hshCheckUnqContacts=objSiteService.getCheckUniqueContacts(lCustomerId,lSiteId);
                     if (strMessage!=null)
                        throw new Exception(strMessage);      
                     
                     arrUnqContacts= (ArrayList)hshCheckUnqContacts.get("objContactType");

                  %>
                     <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Contactos únicos -
                  <% 
                    for (int j=0;j<arrUnqContacts.size();j++){                                        
                       hshUnqCntctsAux=(HashMap)arrUnqContacts.get(j);                       
                  %>    <b><%=MiUtil.initCap((String)hshUnqCntctsAux.get("contacttype"))%></b>
                  <%   if ((j+1)<arrUnqContacts.size()){                          
                  %>        , 
                  <%   }
                    }
                  %>
                    <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    Un mismo contacto puede tener todos los tipos de contacto a la vez, o tener un contacto por cada tipo, o una combinaci&oacute;n de ellos.
                  </td>
               </tr>
           </table>
           <table border="0" cellspacing="0" cellpadding="0" width="60%">
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
            <table width="60%" border="0" cellspacing="1" cellpadding="0" class="RegionBorder">                     
               <tr>
                  <td class="CellLabel" VALIGN="TOP" nowrap><font class="Required">*</font>Tipo Contacto</TD>
                  <TD CLASS="CellContent" COLSPAN="3">
                     <TABLE WIDTH="500" CELLPADDING="0" CELLSPACING="0" ALIGN="LEFT">
                    <%                     
                     int wn=0;
                     int wk=0;
                     int iNumConAvailable = 0;
                     for(int k=0;k<arrTypeContacts.size();k++ ){                
                        if ((wn %4 )==0){
                           if (wn >0){                           
                   %>        
                              </tr>
                   <%      }
                           wn= 0;
                   %>
                           <tr>
                     
                   <%   }
                      hshData= (HashMap)arrTypeContacts.get(k); //Contiene el listado de todas los tipos de contactos
                      strContactType =MiUtil.getString((String)hshData.get("contact"));                   
                      //Verificar si este contacto es único y si ya existe
                      wk=0;
                      //Contiene el listado de contactos unicos                      
                      hshData= (HashMap)arrCheckUnqContactsType.get(wk); 
                      strUnqContact=MiUtil.initCap((String)hshData.get("contacttype"));                      
                      while (wk <= arrCheckUnqContactsType.size() && ( !strContactType.equals(strUnqContact))){
                         try{
                         hshData =(HashMap)arrCheckUnqContactsType.get(wk+1);                            
                         strUnqContact = MiUtil.initCap((String)hshData.get("contacttype"));                             
                         }catch(Exception e){
                         }    
                           wk++;
                      } 
                       if (wk < arrCheckUnqContactsType.size() ){//Contacto único en posicion wk      
                           //Contiene un listado de flag que indican si ya existen esos contactos unicos
                           hshData=(HashMap)arrCheckUnqFlgContactsType.get(wk);
                           strUnqFlgType=MiUtil.getString((String)hshData.get("contacttypeflg"));
                           if ("1".equals(strUnqFlgType)){//ya existe ,entonces mostrar picture ok                            
                        %>      
                              <td class="CellContent"><input type="checkbox" name="chkContactDisabled" value="<%=strContactType%>" disabled><%=MiUtil.initCap(strContactType)%></td>                              
                        <% }else{
                              iNumConAvailable = iNumConAvailable + 1;                             
                        %>                        
                              <td class="CellContent"><input type="checkbox" name="chkContactType" value="<%=strContactType%>" <% if (strContactType.equals(strPedido)){%> disabled <%}%> ><%=MiUtil.initCap(strContactType)%></td>                              
                        <%  
                           }
                        }else{
                        %>    
                            <td class="CellContent"><input type="checkbox" name="chkContactType" value="<%=strContactType%>" <% if (strContactType.equals(strPedido)){%> disabled <%}%> ><%=MiUtil.initCap(strContactType)%></td>
                        <%
                             iNumConAvailable = iNumConAvailable + 1;
                        }                              
                         wn= wn + 1;
                     } // fin del for
                     if ((4 - wn)>0){   //Completar la última fila con celdas en blanco.
                        for (int h=0;h<(4-wn);h++){                        
                   %>        
                           <td class="CellContent">&nbsp;</td>
                           
                    <%  }%>
                         </tr>
                    <% } %>                     
                     </table>
                  </td>
               </tr>
               <tr>
                  <td class="celllabel" valign="top">&nbsp;Título&nbsp;</TD>
                  <td class="cellcontent" valign="top" colspan="3">
                     <select name="cmbTitle">                         
                       <% hshData = objGeneralService.getTableList("PERSON_TITLE","1"); 
                          strMessage=(String)hshData.get("strMessage");
                          if (strMessage!=null)
                             throw new Exception(strMessage);                               
                          arrLista=(ArrayList)hshData.get("arrTableList");                           
                       %>
                       <%=MiUtil.buildCombo(arrLista,"wv_npValueDesc","wv_npValueDesc")%>                                      
                     </select>               
                  </td>
               </tr>
               <tr>
                  <TD CLASS="CellLabel"><font class="Required">*</font>Nombre&nbsp;</TD>
                  <td class="cellcontent" width="124" colspan="3"><input type="text" name="txtFirstName" size="40" maxlength="40" onchange="this.value=trim(this.value.toUpperCase())"></td>
               </tr>
               <tr>
                  <td class="CellLabel" nowrap><font class="Required">*</font>Ap. Paterno&nbsp;</td>
                  <td class="CellContent" ><input type="text" name="txtLastName1" SIZE="40" MAXLENGTH="20" onchange="this.value=trim(this.value.toUpperCase())"></td>
                  <td class="CellLabel" >&nbsp;Ap. Materno&nbsp;</td>
                  <td class="CellContent" ><input type="text" name="txtLastName2" SIZE="40" MAXLENGTH="20" onchange="this.value=trim(this.value.toUpperCase())"></td>
               </tr>
               <tr>
                  <td class="CellLabel" ><font class="Required">*</font>Cargo&nbsp;</td>
                  <td class="CellContent">
                  <select name="cmbJobTitle" style="width: 90%;" onchange="return checkOthers(this,document.formdatos.txtJobTitleDesc)">                         
                   <% hshData = objGeneralService.getTitleList(); 
                      strMessage= (String)hshData.get("strMessage");
                      if (strMessage!=null)
                         throw new Exception(strMessage);                              
                      arrLista=(ArrayList)hshData.get("arrTitleList");                     
                   %>
                   <%=MiUtil.buildCombo(arrLista,"jobtitlteId","descripcion")%>                                      
                  </select>
                  </TD>
                  <TD CLASS="CellLabel" nowrap>&nbsp;OTROS descr.&nbsp;</td>
                  <TD CLASS="CellContent"><input type="text" name="txtJobTitleDesc" SIZE="40" MAXLENGTH="40" onchange="this.value=trim(this.value.toUpperCase());return checkOthers(document.formdatos.cmbJobTitle,this)"></td>
               </TR>
               <script>          
                  function fxAreaCode(n1,n2){
                     this.name = n1;
                     this.codearea = n2;
                  }
                  var vAreaCode = new Vector();                       
                  
                  function fxCodeArea(nAreaCode, nAreaNom, ntelf){   
                     form= document.forms(0);
                     var param="?sAreaCode="+nAreaCode+"&sSpamArea="+nAreaNom+"&sTelfName="+ntelf+"&sFormName="+form.name;
                     url="<%=strURLAreaCode%>"+param;     
                     window.open(url ,"WinCustomer","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,screenX=60,top=40,left=60,screenY=40,width=330,height=400");
                  }                        

                 function fxSearchAreaCode(code, nf_AreaCode ,nf_AreaNom, nf_telf){
                    if (code==""){
                       eval("document.formdatos."+nf_AreaCode+".value = ''");
                       eval(nf_AreaNom + ".innerHTML = ''");
                       eval("document.formdatos."+nf_telf+".focus();");
                       return;
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
                       eval("document.formdatos."+nf_AreaCode+".value = "+code);
                       eval(nf_AreaNom + ".innerHTML = '("+cadena+")'");
                       eval("document.formdatos."+nf_telf+".focus();");
                    }else{
                       fxCodeArea(nf_AreaCode, nf_AreaNom, nf_telf );
                       eval("document.formdatos."+nf_AreaCode+".value = ''");
                    }
                    return bEntro;
                 }             

                function fxSubmit(){
                    form = document.formdatos;
                    if (fxValidate()==false) 
                        return; 
                    form.myaction.value="InsertContact";           
					form.hdnSpecificationId.value="<%=strSpecificationId%>"; //CEM COR0354
                    form.action='<%=strURLOrderServlet%>';          
                    form.submit();                             
                }
                
               </script>

               <tr>
                  <td class="CellLabel"><a href="javascript:fxCodeArea('txtAreaCode','spanAreaNom1','txtPhone')">Área</a>/Tel&eacute;fono&nbsp;</td>
                  <td class="CellContent">
                     <input type="text" name="txtAreaCode" size="1" maxlength="2" onchange="this.value=trim(this.value);return fxSearchAreaCode(this.value, 'txtAreaCode','spanAreaNom1','txtPhone')">
                     <input type="text" name="txtPhone" size="7" maxlength="30">
                     <small><span id="spanAreaNom1"></span></small>
                  </td>
                     <td class="CellLabel">Anexo</td>
                     <td class="CellContent"><input type="text" name="txtAnexoPhone" size="3" maxlength="5" onBlur="javascript:fxValidatePhone(this,'Anexo');" ></td>
               </tr>
               <TR>
                  <td class="CellLabel"><a href="javascript:fxCodeArea('txtAreaCodeFax','spanAreaNom2','txtFax')">Área</a>/Fax&nbsp;</td>
                  <td class="CellContent">
                     <input type="text" name="txtAreaCodeFax" size="1" maxlength="2" onchange="this.value=trim(this.value);return fxSearchAreaCode(this.value, 'txtAreaCodeFax','spanAreaNom2','txtFax')">
                     <input type="text" name="txtFax" size="7" maxlength="8">
                     <small><span id="spanAreaNom2"></span></small>
                  </td>
                  <TD CLASS="CellLabel" >&nbsp;Email&nbsp;</td>
                  <TD CLASS="CellContent"><input type="text" name="txtEmail" SIZE="40" MAXLENGTH="40" onchange="this.value=trim(this.value.toLowerCase())"></td>
               </tr>
               </table>
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
               <input type="hidden" name="hdnNumContAvailable" value="<%=iNumConAvailable%>">
         </form>
         
<% 
   hshData=objGeneralService.getAreaCodeList(null,null);
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
  System.out.println("Error try catch-->"+MiUtil.getMessageClean(ex.getMessage())); 
%>
<script>
  fxShowMessage("<%=MiUtil.getMessageClean(ex.getMessage())%>");
</script>
<%
}
%>        