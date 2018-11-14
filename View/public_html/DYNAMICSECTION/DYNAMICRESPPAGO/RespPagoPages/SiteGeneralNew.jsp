<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.bean.*"%>
<%@ page import="pe.com.nextel.service.*"%>
<%@ page import="pe.com.nextel.util.*"%>
<%@ page import="java.util.*"%>
<%    
try{

   HashMap hshData=new HashMap();
   HashMap hshSolutionGroup =new HashMap();
   long lOrderId=(request.getParameter("nOrderId")==null?0:Long.parseLong(request.getParameter("nOrderId")));
   long lCustomerId=(request.getParameter("nCustomerId")==null?0:Long.parseLong(request.getParameter("nCustomerId")));
   long lSiteId=(request.getParameter("nSiteId")==null?0:Long.parseLong(request.getParameter("nSiteId")));   
   String strAction=(request.getParameter("sAction")==null?"R":request.getParameter("sAction"));   
   String strSessionId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));   
   String strSpecificationId=(request.getParameter("pSpecificationId")==null?"0":request.getParameter("pSpecificationId")); //CEM COR0354
   
   SiteService objSiteService=new SiteService();  
   GeneralService objGeneralService=new GeneralService();
   SiteBean objSiteBean =new SiteBean();
   String strMessage=null;
   
   hshData=objSiteService.getSiteDetail(lCustomerId,lSiteId);   
   strMessage=(String)hshData.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage); 
      
   HashMap hshSite=(HashMap)hshData.get("hshData");
   
   hshSolutionGroup = objSiteService.getSiteSolutionGroup(lSiteId);
   strMessage=(String)hshSolutionGroup.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage); 
      
     String strSolutionGroup = (String)hshSolutionGroup.get("strSolutionGroup");
     
   
   long lRegionId=Long.parseLong(MiUtil.getIfNotEmpty((String)hshSite.get("swregionid")));
   
   String strPhoneArea=MiUtil.getString((String)hshSite.get("swofficephonearea"));
   String strFaxArea=(String)hshSite.get("swfaxarea");   

   String strOffPhoneDep =objGeneralService.getDepartmentName(strPhoneArea);
   String strFaxDep =objGeneralService.getDepartmentName(strFaxArea);
   
   /*Cambio de Cuenta de Facturación*/
   String strStatusBA = MiUtil.getString(objGeneralService.getValue(Constante.NPT_SECTION_BA_FROM_SITE,Constante.KN_ORDERS));
   
   //String strParam="?nOrderId="+lOrderId+"&nCustomerId="+lCustomerId+"&nSiteId="+lSiteId+"&nRegionId="+lRegionId+"&hdnSessionId="+strSessionId;
   String strParam="?nOrderId="+lOrderId+"&nCustomerId="+lCustomerId+"&nSiteId="+lSiteId+"&nRegionId="+lRegionId+"&hdnSessionId="+strSessionId+"&pSpecificationId="+strSpecificationId; //CEM COR0354
   //Ruta   
   String strURLDirecciones="AddressList.jsp"+strParam;
   String strURLContactos="ContactList.jsp"+strParam;
   String strURLBillAcc="BillingAccountList.jsp"+strParam;
   String strURLAreaCode=request.getContextPath()+"/GENERALPAGE/AreaCodeList.jsp";
   String strURLOrderServlet = request.getContextPath()+"/editordersevlet";   
   
   System.out.println(" --------------------  INCIO SITEGENERAL NEW.jsp---------------------- ");   
   System.out.println("strAction :" + strAction);
   System.out.println("lSiteId :" + lSiteId);
   System.out.println("lCustomerId :" + lCustomerId);
   System.out.println("lOrderId :" + lOrderId);
   System.out.println("strSessionId :" + strSessionId);      
   System.out.println("strSpecificationId :" + strSpecificationId);
   System.out.println(" --------------------  FIN SITEGENERAL NEW.jsp---------------------- ");
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Direcciones</title>
  </head>
  <link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css">
  <script language="javaScript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
  <script language="JavaScript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<DIV ALIGN="LEFT">

<%
//Encontramos la Maxima Longitud para el nombre del site
//-------------------------------------------------------
  SiteService objSiteServiceNew=new SiteService();
  hshData=objSiteServiceNew.getLongMaxSite();
  strMessage=(String)hshData.get("strMessage");
  String strLongMax=null;
  if (strMessage!=null)
      throw new Exception(strMessage);       
     strLongMax=(String)hshData.get("strLongSite");   
%>


 <TABLE   BORDER="0" CELLPADDING="0" CELLSPACING="0">
  <TR>
   <TD valign="top">&nbsp;&nbsp;</TD><TD>
    <TABLE  BORDER="0" HEIGHT="100%" WIDTH="100%" CELLPADDING="0" CELLSPACING="0">
    <TR>
    <TD class="LeftTabForeSlant" valign="top" align="left" width="10" height="19" NOWRAP="">&nbsp;</TD>
    <td valign="middle" NOWRAP class="TabForegroundColor">&nbsp;&nbsp;<FONT class="TabForegroundText">Datos Generales</FONT>&nbsp;&nbsp;</td>
    <TD align="right" class="RightTabForeCurve" width="8" NOWRAP="">&nbsp;&nbsp;</TD>
    </TR>
    </TABLE>
   </TD>
   <TD>
    <TABLE  BORDER="0" HEIGHT="100%" WIDTH="100%" CELLPADDING="0" CELLSPACING="0">
    <TR>
    <TD class="LeftTabBgSlant" valign="top" align="left" width="10" height="19" NOWRAP="">&nbsp;</TD>
    <td valign="middle" nowrap class="TabBackgroundColor">&nbsp;&nbsp;<A HREF="<%=strURLDirecciones%>"><FONT class="TabBackgroundText">Direcciones</FONT></A>&nbsp;&nbsp;</td>
    <TD align="right" class="RightTabBgCurve" width="8" NOWRAP="">&nbsp;&nbsp;</TD>
    </TR>
    </TABLE>
   </TD>
   <TD>
    <TABLE  BORDER="0" HEIGHT="100%" WIDTH="100%" CELLPADDING="0" CELLSPACING="0">
    <TR>
    <TD class="LeftTabBgSlant" valign="top" align="left" width="10" height="19" NOWRAP="">&nbsp;</TD>
    <td valign="middle" nowrap class="TabBackgroundColor">&nbsp;&nbsp;<A HREF="<%=strURLContactos%>"><FONT class="TabBackgroundText">Contactos</FONT></A>&nbsp;&nbsp;</td>
    <TD align="right" class="RightTabBgCurve" width="8" NOWRAP="">&nbsp;&nbsp;</TD>
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
  <FONT class="PortletHeaderText">Datos para Site Nuevo</FONT>
  &nbsp;&nbsp;&nbsp;</TD>
  <TD align="RIGHT" class="PortletHeaderColor" NOWRAP="" valign="top">&nbsp;
  </TD><TD align="right" class="RightCurve" width="10" NOWRAP="">&nbsp;&nbsp;</TD></TR> 
  </TABLE>

 <TABLE  BORDER="1" WIDTH="100%" CELLPADDING="2" CELLSPACING="0" class="RegionBorder">
 <TR><TD class="RegionHeaderColor" WIDTH="100%">
  
<% if ("R".equals(strAction) || strAction==null){%>

      <script language="javascript">
         function fxEditar() {            
            var parametros="<%=strParam%>&sAction=E";
            parent.mainFrame.location.href="SiteGeneralNew.jsp"+parametros;  
         }
      </script>  
      
      <table cellspacing="0" cellpadding="0" border="0" width="99%" height="20" align="center">
         <tr>
            <td valign="bottom" class="CellContent">
               <a href="javascript:fxEditar()"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/Editar.gif" border="0" alt="Editar" width="16" height="16"></a>
            </td>
         </tr>
      </table>
     <table width="99%" border="0" cellspacing="1" cellpadding="0" align="center">
        <tr>
           <td class="CellLabel"   width="20%">&nbsp;Nombre Site</td>
           <td class="CellContent" width="30%" >&nbsp;<%=MiUtil.getString((String)hshSite.get("swsitename"))%></td>
           <td class="CellLabel"   width="20%">&nbsp;Regi&oacute;n</td>
           <td class="CellContent" width="30%">&nbsp;<%=MiUtil.getString((String)hshSite.get("region"))%></td>
        </tr>
        <tr>
           <td class="CellLabel"   width="20%">&nbsp;Área/Tel&eacute;fono</td>
           <td class="CellContent" width="30%">&nbsp;
           <%=MiUtil.getString(MiUtil.decode((String)hshSite.get("swofficephone"),null,null,"("+strPhoneArea+")&nbsp;&nbsp;"+MiUtil.getString((String)hshSite.get("swofficephone"))))%>
           <small><%=MiUtil.getString(MiUtil.decode(strOffPhoneDep,null,null,"("+strOffPhoneDep+")"))%></small>
           </td>
           <td class="CellLabel"   width="20%">&nbsp;Área/Fax</td>
           <td class="CellContent" width="30%">&nbsp;
           <%=MiUtil.getString(MiUtil.decode((String)hshSite.get("swfax"),null,null,"("+(String)hshSite.get("swfaxarea")+")&nbsp;&nbsp;"+MiUtil.getString((String)hshSite.get("swfax"))))%>
           <small><%=MiUtil.getString(MiUtil.decode(strFaxDep,null,null,"("+strFaxDep+")"))%></small>
           </td>
        </tr>
        <tr>
          <td class="CellLabel">Grupo de Solución</td>
          <td colspan="3" class="CellContent"><%=MiUtil.decode(strSolutionGroup, null, "",strSolutionGroup)%></td>                                 	
        </tr> 
        <form name="formCustomer">
               <input type="hidden" name="flg_enabled" value="1">
			   <input type=hidden name=hdnIdOrden value="<%=lOrderId%>"/ > <!--COR0354-->
        </form>
        <script>
           function fxCheckOnClose(){
              Form = document.formCustomer;
              if (Form.flg_enabled.value == 0) return false;
              var url = "ValidateUnkwnSite.jsp?nCustomerId=<%=lCustomerId%>&nSiteId=&nUnknwnsiteid=<%=lSiteId%>&nRegionId=<%=lRegionId%>";
              parent.bottomFrame.location.replace(url);
              return true;
           }
        </script>
        <tr>
           <td colspan="4" class="CellContent" align="center">
              <input type="button" name="btnClose" value="   Cerrar   " onclick="javascript:fxCheckOnClose()">
           </td>
        </tr>
     </table>
<%}else if ("E".equals(strAction)){
   hshData =objGeneralService.getRegionList(); 

   strMessage=(String)hshData.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);                               
   ArrayList arrRegion=(ArrayList)hshData.get("arrRegionList");    
%>   
   <script>
      function fxAreaCode(n1,n2){
         this.name = n1;
         this.codearea = n2;
      }
      var vAreaCode = new Vector();  
   </script>
    
<% hshData=objGeneralService.getAreaCodeList(null,null);
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
%>  

   <script language="JavaScript">
   <!--
      function fxSearchAreaCode(code, nf_AreaCode ,nf_AreaNom, nf_telf){
         if (code==""){
            eval("document.formCustomer."+nf_AreaCode+".value = ''");
            eval(nf_AreaNom + ".innerHTML = ''");
            eval("document.formCustomer."+nf_telf+".focus();");
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
            eval("document.formCustomer."+nf_AreaCode+".value = "+code);
            eval(nf_AreaNom + ".innerHTML = '("+cadena+")'");
            eval("document.formCustomer."+nf_telf+".focus();");
         }else{
            fxCodeArea(nf_AreaCode, nf_AreaNom, nf_telf);
            eval("document.formCustomer."+nf_AreaCode+".value = ''");
         }
         return bEntro;
      } 
      
      function fxCodeArea(nAreaCode, nAreaNom, ntelf ){
         var param="?sAreaCode="+nAreaCode+"&sSpamArea="+nAreaNom+"&sTelfName="+ntelf+"&sFormName=formCustomer";
         url="<%=strURLAreaCode%>"+param;
         window.open(url ,"WinCustomer","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,screenX=60,top=40,left=60,screenY=40,width=330,height=400");
     }

      function fxValidate(){
         Form=document.formCustomer;
         if (Form.flg_enabled.value == 0) return false;
         if (!ValidPhoneWithCodeArea(Form.txtAreaCode, Form.txtPhone ,"teléfono del Site",true)){
            return false;
         }else if (Form.txtPhone.value==""){
            alert("Ingrese el Nro. telefónico del site");
            Form.txtPhone.focus();
            return false;
         }else if (!ContentOnlyNumber(Form.txtPhone.value)){
            alert("Ingrese el Nro. telefónico válido");
            Form.txtPhone.focus();
            return false;
         }
         if (Form.txtAreaCodeFax!="" || Form.txtFax.value!=""){
            if (!ValidPhoneWithCodeArea(Form.txtAreaCodeFax, Form.txtFax ,"Fax del Site",false)){
               return false;
            }
            if (!ContentOnlyNumber(Form.txtFax.value)){
               alert("Ingrese el Nro. de fax válido");
               Form.txtFax.focus();
               return false;
            }
         }
         Form.flg_enabled.value = 0;
         return true;
      }
      
      function fxSubmit(){
        form = document.formCustomer;
        if (fxValidate()==false) 
            return; 
        form.myaction.value="UpdateSite";           
        form.hdnSessionId.value="<%=strSessionId%>"; 
		form.hdnSpecificationId.value="<%=strSpecificationId%>"; //CEM COR0354
        form.action="<%=strURLOrderServlet%>";           
        form.submit();    
      }
      
      
      function fxMaxLong(strLongMax){
        
        //Validación del caracter "'" en el campo
        //---------------------------------------
        if (validar(document.formCustomer.txtSiteName.value) == false){
          alert("El cáracter ' no esta permitido, vuelva a insertar el nombre...");
          document.formCustomer.txtSiteName.value = "";
          document.formCustomer.txtSiteName.focus(); 
          return true;
        }
        
       //Validación de la longitud máxima en el campo
      //--------------------------------------------
        if(document.formCustomer.txtSiteName.value.length > strLongMax){
          alert("El dato excede el tamaño permitido, por favor abrévielo…");
          document.formCustomer.txtSiteName.value = "";
          document.formCustomer.txtSiteName.focus(); 
          return true;
        }
      }
      
      
       function validar(txt){
           var ubicacion
           var contador = 0
          
           //devuelve falso si existe algun error 
           //-------------------------------------
            for (var i=0; i < txt.length; i++) {
                ubicacion = txt.substring(i, i + 1)
                if (ubicacion !="'") {
                    contador++
                } else {
                  return false
                }
            }
    
      }
      
      
   
      -->
   </script>

   <!--center-->
   <!--Tabla de Datos Generales: inicio-->

      <FORM NAME="formCustomer" TARGET="bottomFrame"><!--ACTION=" /portal/pls/portal/!WEBSALES.NPSL_SITE_PL_PKG.PL_UNKNOWN_SITE_EDIT_SAVE"  onSubmit="return validate()"-->
      <table width="100%" border="0" cellspacing="1" cellpadding="0" align="center">
      <input type="hidden" name="hdnCustomerId" value="<%=lCustomerId%>">
      <input type="hidden" name="hdnSiteId"     value="<%=lSiteId%>">
      <input type="hidden" name="hdnRegionId"   value="<%=lRegionId%>">
	  <input type="hidden" name="hdnOrderId"    value="<%=lOrderId%>"> <!-- CEM COR0354 -->
      <input type="hidden" name="flg_enabled"	value="1">
      <input type="hidden" name="myaction"		value=""> 
      <input type="hidden" name="hdnSessionId" > 
	  <input type="hidden" name="hdnSpecificationId" value="">  <!--CEM COR0354-->
      
      <tr>
         <td class="CellLabel" width="20%">&nbsp;Nombre Site</TD>
         <td class="CellContent" width="30%">
            <input type="text" name="txtSiteName" size="40"  value='<%=MiUtil.escape((String)hshSite.get("swsitename"))%>' onchange="this.value=trim(this.value.toUpperCase());return fxMaxLong(<%=strLongMax%>)" >
         </td>
         <td class="CellLabel" width="10%">&nbsp;Regi&oacute;n</td>
         <td class="CellContent" width="40%">
            <select name="cmbRegion" onChange="javascript:document.formCustomer.hdnRegionName.value=this[this.selectedIndex].text">
               <%=MiUtil.buildCombo(arrRegion,"swregionid","swname")%>                
            </select>
            <input type="hidden" name="hdnRegionName" >
            <script>
               document.formCustomer.cmbRegion.value=<%=lRegionId%>;
               document.formCustomer.hdnRegionName.value=document.formCustomer.cmbRegion[document.formCustomer.cmbRegion.selectedIndex].text;
            </script>
         </TD>
      </TR>
      <tr>
         <td class="CellLabel" width="20%"><font class="Required">*</font><a href="javascript:fxCodeArea('txtAreaCode','spanAreaNom1','txtPhone')">Área</a>/Tel&eacute;fono</td>
         <td class="CellContent" width="30%">
            <input type="text" name="txtAreaCode" size="1" maxlength="2" value="<%=strPhoneArea%>" onchange="this.value=trim(this.value);return fxSearchAreaCode(this.value, 'txtAreaCode','spanAreaNom1','txtPhone')">
            <input type="text" name="txtPhone" SIZE="10" MAXLENGTH="12" value="<%=MiUtil.getString((String)hshSite.get("swofficephone"))%>" onchange="this.value=trim(this.value)">
            <small><span id="spanAreaNom1"><%=MiUtil.getString(MiUtil.decode(strOffPhoneDep,null,null,"("+strOffPhoneDep+")"))%></span></small>            

         </td>
         <td class="CellLabel" width="10%">&nbsp;<a href="javascript:fxCodeArea('txtAreaCodeFax','spanAreaNom2','txtFax')">Área</a>/Fax</td>
         <td class="CellContent" width="40%">
            <input type="text" name="txtAreaCodeFax" size="1" maxlength="2" value="<%=MiUtil.getString((String)hshSite.get("swfaxarea"))%>" onchange="this.value=trim(this.value);return fxSearchAreaCode(this.value, 'txtAreaCodeFax','spanAreaNom2','txtFax')">
            <input type="text" name="txtFax" SIZE="10" MAXLENGTH="7" value="<%=MiUtil.getString((String)hshSite.get("swfax"))%>" onchange="this.value=trim(this.value)">
            <small><span id="spanAreaNom2"><%=MiUtil.getString(MiUtil.decode(strFaxDep,null,null,"("+strFaxDep+")"))%></span></small>                        
         </td>
      </tr>
       <tr>
          <td class="CellLabel">Grupo de Solución</td>
          <td colspan="3" class="CellContent"><%=MiUtil.decode(strSolutionGroup, null, "",strSolutionGroup)%></td>                                 	
      </tr>
      <tr>
         <td colspan="4" class="CellContent" align="center">
            <input type="button" name="btnsubmit" value="   Guardar   " onclick="javascript:fxSubmit()">
            <input type="button" name="btnCancel" value="   Cancelar   " onclick="javascript:fxOnCancel()">
            <input type="button" name="btnCerrar" value="   Cerrar   "  onClick="parent.close();">
            <script>
               function fxOnCancel() {                  
                  var parametros="<%=strParam%>&sAction=R";
                  var Url="SiteGeneralNew.jsp"+parametros;                     
                  parent.mainFrame.location.replace(Url);
               }
            </script>
          </td>
         </tr>
      </FORM> 
<%}%>
</TD></TR></TABLE></TD></TR></TABLE>
</html>
<%}catch(Exception ex){
  System.out.println("Error try catch-->"+MiUtil.getMessageClean(ex.getMessage())); 
%>
<script>
  alert("<%=MiUtil.getMessageClean(ex.getMessage())%>");
</script>
<%
}%>