<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.bean.*"%>
<%@ page import="pe.com.nextel.service.*"%>
<%@ page import="pe.com.nextel.util.*"%>
<%@ page import="java.util.*"%>
<%
try{
   long lCustomerId=(request.getParameter("nCustomerId")==null?0:Long.parseLong(request.getParameter("nCustomerId")));
   long lSiteId=(request.getParameter("nSiteId")==null?0:Long.parseLong(request.getParameter("nSiteId")));
   long lRegionId=(request.getParameter("nRegionId")==null?0:Long.parseLong(request.getParameter("nRegionId"))); 
   long lOrderId=(request.getParameter("nOrderId")==null?0:Long.parseLong(request.getParameter("nOrderId")));
   //String strAction=(request.getParameter("sAction")==null?"R":request.getParameter("sAction"));
   String strSessionId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));
   String strSpecificationId=(request.getParameter("pSpecificationId")==null?"0":request.getParameter("pSpecificationId")); //CEM COR0354
   
   SiteService objSiteService=new SiteService();
   CustomerService objCustomerService=new CustomerService();
   SiteBean objSiteBean=new SiteBean();
   GeneralService objGeneralService=new GeneralService();
   HashMap hshContacts=null;
   ArrayList arrContacts=null;
   ArrayList arrNoChangeContact=null;
   HashMap hshNoChangeContact=null;
   String strMessage=null;
   String strObjectType=null;
   long lObjectId=0;
   int iResultado=0;
   
   /*Cambio de Cuenta de Facturación*/
   String strStatusBA = MiUtil.getString(objGeneralService.getValue(Constante.NPT_SECTION_BA_FROM_SITE,Constante.KN_ORDERS));
   
  
   //String strParam="?nOrderId="+lOrderId+"&nCustomerId="+lCustomerId+"&nSiteId="+lSiteId+"&hdnSessionId="+strSessionId;
   String strParam="?nOrderId="+lOrderId+"&nCustomerId="+lCustomerId+"&nSiteId="+lSiteId+"&hdnSessionId="+strSessionId+"&pSpecificationId="+strSpecificationId; //CEM COR0354
   
   //RUTAS
   String strURLGeneralData="SiteGeneralNew.jsp"+strParam;
   String strURLDireccion="AddressList.jsp"+strParam;
   String strURLBillAcc="BillingAccountList.jsp"+strParam;   
   String strURLOrderServlet =request.getContextPath()+"/editordersevlet";    
   
   System.out.println(" -------------------- INICIO CONTACTList.jsp---------------------- ");
   System.out.println("strSessionId :" + strSessionId);
   System.out.println("lSiteId :" + lSiteId);
   System.out.println("lCustomerId :" + lCustomerId);  
   System.out.println("lOrderId :" + lOrderId);
   System.out.println("strSpecificationId :" + strSpecificationId);
   System.out.println(" --------------------  FIN CONTACTList.jsp---------------------- ");

%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Contactos</title>
  </head>
  <link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css">  
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
  <FONT class="PortletHeaderText">Contactos para Site Nuevo</FONT>
  &nbsp;&nbsp;&nbsp;</TD>
  <TD align="RIGHT" class="PortletHeaderColor" NOWRAP="" valign="top">&nbsp;
  </TD><TD align="right" class="RightCurve" width="10" NOWRAP="">&nbsp;&nbsp;</TD></TR> 
  </TABLE>

 <TABLE  BORDER="1" WIDTH="100%" CELLPADDING="2" CELLSPACING="0" class="RegionBorder">
 <TR><TD class="RegionHeaderColor" WIDTH="100%">  
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
   
   function fxCheckOnClose(){
      Form = document.formdatos;
      var url = "ValidateUnkwnSite.jsp?nCustomerId=<%=lCustomerId%>&nSiteId=&nUnknwnsiteid=<%=lSiteId%>";
      parent.bottomFrame.location.replace(url);
      return true;        
   }
   </script>
<%      
   //Obtenemos cursor con Contactos
   hshContacts= objSiteService.getAddressOrContactList(strObjectType,lObjectId,"PERSON");
   arrContacts=(ArrayList)hshContacts.get("objArrayList");
   strMessage=(String)hshContacts.get("strMessage");
   iResultado=arrContacts.size();            
%>

   <script language="javascript">
   var arrayContactId   = new Array();
   var arrayContactType = new Array();
   var ArrayNoChangeContact = new Array();
   var ArrayMandatoryContact = new Array();      
   
   function putContactOnArray( id, name ) {
      var index;
      index = arrayContactId.length;
      arrayContactId[index] = id;
      arrayContactType[index] = name;
   }
   
   function findMandatoryContact( contactType){
      var pos;
      var cont = 0;
      var aux;
      for (i = 0; i < arrayContactId.length; i++) {
         aux = arrayContactType[i];
         pos = aux.search(contactType);
         if ( pos >= 0 ){ //si se encontró el contacto
            cont++;  
         }
      }
      return cont;
   }
   
   function fxDeleteContact(contactId, contactType){
      var flag;
      var pos;
      for (var i = 0; i< ArrayNoChangeContact.length; i++){
        if(contactType.search(ArrayNoChangeContact[i])>=0){
           alert("No está permitido eliminar este tipo de contacto");     
           return;
        }
      }
      for(var k = 0; k < ArrayMandatoryContact.length; k++){
        pos = contactType.search(ArrayMandatoryContact[k]);
        if (pos >= 0 ){
           flag = findMandatoryContact(ArrayMandatoryContact[k]);
           if (flag == 1){ //es el ultimo contacto obligatorio
              alert("No puede eliminar un Contacto Obligatorio");
              return;
           }
        }
      }
      if( confirm("Está seguro de eliminar el Contacto?") ) {
         form = document.formdatos;
         form.myaction.value="DeleteContact"; 
         form.hdnObjectType.value="<%=strObjectType%>";
         form.hdnContactId.value=contactId;
		 //Inicio CEM COR0354
		 form.hdnCustomerId.value="<%=lCustomerId%>";
		 form.hdnOrderId.value="<%=lOrderId%>";
		 form.hdnSpecificationId.value="<%=strSpecificationId%>";
		 form.hdnSiteId.value="<%=lSiteId%>";
		 //Fin CEM COR0354		 
         form.action="<%=strURLOrderServlet%>";
         form.submit();                       
      }
   }
   
   </script>          
           
   <table  width="99%" border="0" cellspacing="0" cellpadding="0">
   <tr>
      <td class="CellContent">
         <table border="0" cellspacing="0" cellpadding="0">
         <tr>
            <td valign="top">
            <a href="ContactNew.jsp<%=strParam%>">
            <img src="<%=Constante.PATH_APPORDER_SERVER%>/images/crear.gif" border="0" alt="Agregar" lowsrc="<%=Constante.PATH_APPORDER_SERVER%>/images/new_noover.gif"></a>                  
            </td>             
            <% if (iResultado!=0){ %>               
            <td valign="top">
            <a href="ContactEdit.jsp<%=strParam%>"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/Editar.gif" border="0" alt="Editar"></a>
            </td>
            <%}%>               
         </tr>
         </table>
      </td>
   </tr>
   </table>     
     
<%    if (strMessage==null){
         if (iResultado==0)
            strMessage="No existe Contactos";
      }     
      
      if (strMessage!=null){ %>      
       <table width="99%" border="0" cellspacing="0" cellpadding="0">
       <tr>
          <td class="CellContent" valign="top" align="center"><b><font color="#FFOOOO"><%=strMessage%></b></font></td>
       </tr>
       </table>
      <% }else{%>            
<%
      //Contactos no intercambiables
      /*hshNoChangeContact=objSiteService.getNoChangeContacts(lSiteId,lCustomerId);
      strMessage=(String)hshNoChangeContact.get("strMessage");
      if (strMessage==null){
         arrNoChangeContact=(ArrayList)hshNoChangeContact.get("objContacttype");
      }else{
         arrNoChangeContact= new ArrayList();
      }*/
      
      hshNoChangeContact=objSiteService.getCheckNoChangeContacts(lCustomerId,lSiteId);      
      strMessage=(String)hshNoChangeContact.get("strMessage");         
      if (strMessage!=null)
         throw new Exception(strMessage);
      
      arrNoChangeContact=(ArrayList)hshNoChangeContact.get("objContactType");     
      
      String strType=null;
      HashMap hshNochangeContact=null;
      String strNoChangeContact=null;
      for (int i =0;i <arrNoChangeContact.size();i++){  
         hshNochangeContact=(HashMap)arrNoChangeContact.get(i);
         strNoChangeContact=MiUtil.getString((String)hshNochangeContact.get("contacttype"));
%>
      <script>
         ArrayNoChangeContact[ArrayNoChangeContact.length] = "<%=strNoChangeContact%>";
      </script>                      
<%      
      }
      strType = "Prospect";
            
      //contactos obligatorios
      ArrayList arrMandaContact=new ArrayList();
      HashMap hshMandatContactType=null;
      //arrMandaContact=objSiteBean.getMandatoryContact(strType);            
      hshMandatContactType=objSiteService.getCheckMandatoryContacts(lCustomerId,lSiteId);
      strMessage=(String)hshMandatContactType.get("strMessage");                    
      if (strMessage!=null){
        throw new Exception(strMessage);            
      }
      arrMandaContact=(ArrayList)hshMandatContactType.get("objContactType");

      HashMap hshMandaContact=null;
      String strMandaContact=null;
      for (int i=0; i <arrMandaContact.size();i++){
         hshMandaContact=(HashMap)arrMandaContact.get(i);
         strMandaContact=MiUtil.getString((String)hshMandaContact.get("contact"));            
%>
         <script>
            ArrayMandatoryContact[ArrayMandatoryContact.length] = "<%=strMandaContact%>";
         </script>                      
<%
      }    
            
      String strName=null;
      long lPersonId=0;
      String strLastName=null;
      String strFistName=null;
      String strMidName=null;
      String strDescripcion=null;
      long lJobTitleId=0;
      int iUsuario=0;
      int iFacturacion=0;
      int iGerente=0;
      int iVentas=0;
      int iSistemas=0;
      int iComunicac=0;
      int iData=0;
      int iCobranza=0;
      String strOpName=null;
      String strTitle=null;
      String strEmail=null;
      String strMainPhoneArea=null;
      String strMainPhone=null;
      String strMainFaxArea=null;
      String strFax=null;
      String strAnexo=null;
      String strMainPhoneDep=null;
      String strMainFaxDep=null;
      HashMap hshContact=null;
      String strEtiquetaUsu=null;
      for (int i =0;i<arrContacts.size();i++){
         hshContact=((HashMap)arrContacts.get(i));                   
         strMainPhoneArea=MiUtil.getString((String)hshContact.get("phonearea"));
         strMainFaxArea=MiUtil.getString((String)hshContact.get("faxarea"));
         strMainPhoneDep=objGeneralService.getDepartmentName(strMainPhoneArea);
         strMainFaxDep=objGeneralService.getDepartmentName(strMainFaxArea);                  
         strLastName=MiUtil.getString((String)hshContact.get("swlastname"));
         strFistName=MiUtil.getString((String)hshContact.get("swfirstname"));
         strMidName=MiUtil.getString((String)hshContact.get("swmiddlename"));
         strTitle=MiUtil.getString((String)hshContact.get("swtitle"));
         strEmail=MiUtil.getString((String)hshContact.get("mail"));
         strMainPhone=MiUtil.getString((String)hshContact.get("phone"));
         strFax=MiUtil.getString((String)hshContact.get("fax"));
         strAnexo=MiUtil.getString((String)hshContact.get("anexo"));
         lPersonId =Long.parseLong((String)hshContact.get("swpersonid"));     
         lJobTitleId=Long.parseLong((String)hshContact.get("swjobtitleid"));     
         strDescripcion=MiUtil.getString((String)hshContact.get("swdescription"));
         iGerente=Integer.parseInt(MiUtil.getIfNotEmpty((String)hshContact.get("GerentG")));        
         iUsuario=Integer.parseInt(MiUtil.getIfNotEmpty((String)hshContact.get("Usuario")));        
         iFacturacion=Integer.parseInt(MiUtil.getIfNotEmpty((String)hshContact.get("Facturacion")));        
         iVentas=Integer.parseInt(MiUtil.getIfNotEmpty((String)hshContact.get("Ventas")));        
         iSistemas=Integer.parseInt(MiUtil.getIfNotEmpty((String)hshContact.get("Sistemas")));        
         iComunicac=Integer.parseInt(MiUtil.getIfNotEmpty((String)hshContact.get("Comunicaciones")));                
         iData=Integer.parseInt(MiUtil.getIfNotEmpty((String)hshContact.get("Data")));                
         iCobranza=Integer.parseInt(MiUtil.getIfNotEmpty((String)hshContact.get("Cobranza")));                
       
         strEtiquetaUsu = "";
         if ( iUsuario==1 ){
            if ( strEtiquetaUsu.length() >0 ){
               strEtiquetaUsu= strEtiquetaUsu + " - ";
           } 
            strEtiquetaUsu= strEtiquetaUsu + "USUARIO";
         }
         if ( iFacturacion==1 ){
            if ( strEtiquetaUsu.length() >0 ){
               strEtiquetaUsu= strEtiquetaUsu + " - ";
           } 
            strEtiquetaUsu= strEtiquetaUsu + "FACTURACION";
         } 
         if ( iGerente==1 ){
            if ( strEtiquetaUsu.length() >0 ){
               strEtiquetaUsu = strEtiquetaUsu + " - ";
           } 
            strEtiquetaUsu = strEtiquetaUsu + "GERENTE GENERAL";
         } 
         if ( iVentas==1 ){
            if ( strEtiquetaUsu.length() >0 ){
               strEtiquetaUsu = strEtiquetaUsu + " - ";
           } 
            strEtiquetaUsu = strEtiquetaUsu + "VENTAS";
         } 
         if ( iSistemas==1 ){
            if ( strEtiquetaUsu.length() >0 ){
               strEtiquetaUsu = strEtiquetaUsu + " - ";
           } 
            strEtiquetaUsu = strEtiquetaUsu + "SISTEMAS";
         } 
         if ( iComunicac==1 ){
            if ( strEtiquetaUsu.length() >0 ){
               strEtiquetaUsu = strEtiquetaUsu + " - ";
           } 
            strEtiquetaUsu = strEtiquetaUsu + "COMUNICACIONES";
         } 
         if ( iData==1 ){
            if ( strEtiquetaUsu.length() >0 ){
               strEtiquetaUsu = strEtiquetaUsu + " - ";
           } 
            strEtiquetaUsu = strEtiquetaUsu + "DATA";
         }
               
%>           
         <table cellspacing="0" cellpadding="0" border="0" width="100%" height="20" align="center">
         <tr>
            <td valign="BOTTOM">
               <a href="javascript:fxDeleteContact('<%=lPersonId%>','<%=strEtiquetaUsu%>')"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/Eliminar.gif" border="0" alt="Eliminar"></a>
            </td>
         </tr>
         </table>

         <table width="100%" border="0" cellspacing="1" cellpadding="0" >  
         <tr>
            <td class="CellLabel" valign="top" width="20%" nowrap><font class="Required">*</font>Tipo Contacto&nbsp;</td>
            <td class="CellContent" colspan="3" width="80%"><%=strEtiquetaUsu%></td>
         </tr>
         <tr>
            <td class="CellLabel" valign="top" width="20%">&nbsp;Título&nbsp;</TD>
            <td class="CellContent" valign="top" width="30%"><%=strTitle%></td>             
            <td class="CellLabel" width="20%"><font class="Required">*</font>Nombres&nbsp;</TD>
            <td class="CellContent" width="30%"><%=strFistName%></td>
         </TR>
         <tr>
            <td class="CellLabel" width="20%"><font class="Required">*</font>Ap.Paterno&nbsp;</td>
            <td class="CellContent" width="30%"><%=strLastName%></td>
            <td class="CellLabel" width="20%"><font class="Required">*</font>Ap.Materno&nbsp;</td>
            <td class="CellContent" width="30%"><%=strMidName%></td>
         </tr> 
         <tr>            
            <td class="CellLabel" width="20%"><font class="Required">*</font>Cargo&nbsp;</td>
            <td class="CellContent" width="30%"><%=strDescripcion%></td>                      
            <td class="CellLabel" nowrap width="20%">&nbsp;OTROS descr.&nbsp;</td>
            <td class="CellContent" width="30%">          
               <% if (lJobTitleId == 19){ %>
                <%=strDescripcion%>
               <%}else{%>
                &nbsp;
               <%}%>                      
            </td>
         </TR>             
         <tr>               
            <td class="CellLabel" width="20%">Área/Teléfono</td>
            <td class="CellContent" width="30%"><%=strMainPhoneArea%> &nbsp;<%=strMainPhone%>
               <small><%=MiUtil.getString(MiUtil.decode(strMainPhoneDep,null,null,strMainPhoneDep))%></small>
            </td>              
            <td class="CellLabel" width="20%"></td>
            <td class="CellContent" width="30%"></small>            
            </td>
         </tr>
         <tr>                   
            <td class="CellLabel" width="20%">Área/Fax</td>
            <td class="CellContent" width="30%"> <%=strMainFaxArea%>&nbsp;<%=strFax%>
               <small><%=MiUtil.getString(MiUtil.decode(strMainFaxDep,null,null,strMainFaxDep))%></small>         
            </td>
            <td class="CellLabel" width="20%">&nbsp;Email&nbsp;</td>
            <td class="CellContent" width="30%"><%=strEmail%></td>
         </tr>       
         </table>               
         <script>
            putContactOnArray( '<%=lPersonId%>', '<%=strEtiquetaUsu%>' );
         </script>
              
      <%}%>
         
         <table width="100%">
         <tr>
            <td  class="CellContent" align="center">
               <input type="button" name="btnClose" value="   Cerrar   " onclick="javascript:fxCheckOnClose()">
            </td>
         </tr>
         </table>    
             
         <form name="formdatos" target="bottomFrame" method="post">
            <input type="hidden" name="myaction" >
            <input type="hidden" name="hdnCustomerId" value="<%=lCustomerId%>">         
            <input type="hidden" name="hdnSiteId" value="<%=lSiteId%>" >                          
            <input type="hidden" name="hdnRegionId" >       
            <input type="hidden" name="hdnObjectType" >       
            <input type="hidden" name="hdnContactId" >     
            <input type="hidden" name="hdnSessionId" value="<%=strSessionId%>">
            <input type="hidden" name="hdnOrderId" value="<%=lOrderId%>">
			<input type="hidden" name="hdnSpecificationId"   value="<%=strSpecificationId%>"> <!-- CEM COR0354 -->
         </form>
<%}%>
   </TD>
   </TR>
   </TABLE>
   </TD></TR>
   </TABLE>
</html>
<%}catch(Exception ex){
  System.out.println("Error try catch-->"+ex.getMessage()); 
%>
<script>
  alert("<%=MiUtil.getMessageClean(ex.getMessage())%>");
</script>
<%
}%>