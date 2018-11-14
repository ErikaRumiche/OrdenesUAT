<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.bean.*"%>
<%@ page import="pe.com.nextel.service.*"%>
<%@ page import="pe.com.nextel.util.*"%>
<%@ page import="java.util.*"%>
<%
try{
   //long lSiteId=(request.getParameter("nSiteId")==null?0:Long.parseLong(request.getParameter("nSiteId")));
   long lOrderId=(request.getParameter("nOrderId")==null?0:Long.parseLong(request.getParameter("nOrderId")));
   long lCustomerId=(request.getParameter("nCustomerId")==null?0:Long.parseLong(request.getParameter("nCustomerId")));
   String strCustomerName=(request.getParameter("sCustomerName")==null?"":MiUtil.unescape2(request.getParameter("sCustomerName")));
   String strSessionId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));
   String strSpecificationId=(request.getParameter("pSpecificationId")==null?"0":request.getParameter("pSpecificationId")); //CEM - COR0354
    
   String strMessage=null;
   HashMap hshData=null;
   GeneralService objGeneralService=new GeneralService();
   SiteService objSiteService=new SiteService();
   hshData=objGeneralService.getRegionList();
   SolutionGroupBean objSolutionGroup = null;
   
   strMessage=(String)hshData.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);                               
   ArrayList arrRegion=(ArrayList)hshData.get("arrRegionList");       
   
   //Grupos Solución de la categoría
   hshData = objSiteService.getSpecSolutionGroup(MiUtil.parseLong(strSpecificationId));
   if (strMessage!=null)
      throw new Exception(strMessage);
   ArrayList arrSolGroup= new ArrayList();
   arrSolGroup= (ArrayList)hshData.get("objArrayList");     
   
   //Ruta
   String strURLAreaCode=request.getContextPath()+"/GENERALPAGE/AreaCodeList.jsp";
   String strURLOrderServlet =request.getContextPath()+"/editordersevlet";      

   System.out.println(" --------------------  INCIO SITENEW.jsp---------------------- ");   
   System.out.println("strSessionId-->"+strSessionId);   
   System.out.println("lCustomerId :" + lCustomerId);
   System.out.println("strCustomerName :" + strCustomerName);
   System.out.println("lOrderId :" + lOrderId);
   System.out.println("strSpecificationId :" + strSpecificationId);
   System.out.println(" --------------------  FIN SITENEW.jsp---------------------- ");   
   
%>

<html>
    <head>
    </head>
    <link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css">
    <script language="javaScript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
    <body>
    <!--body onbeforeunload="javascript:checkOnClose1()"-->
    <script language="JavaScript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>

    <!-- Region de nueva direccion -->
    <script LANGUAGE="JavaScript">
   var vAreaCode = new Vector();
   
   function fxAreaCode(n1,n2){
      this.name = n1;
      this.codearea = n2;
   }

   function fxCodeArea(nAreaCode, nAreaNom, ntelf ){
       var param="?sAreaCode="+nAreaCode+"&sSpamArea="+nAreaNom+"&sTelfName="+ntelf+"&sFormName=formdatos";
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
   function fxValidate(){
      Form = document.formdatos;
      if (Form.flg_enabled.value == 0) return false;

      // VALIDACION DE SITE
      if (Form.txtSiteName.value==""){
          alert("Ingrese el nombre del site.");
          Form.txtSiteName.focus();
          return false;
      }
      if (Form.cmbRegion.selectedIndex==0){
         alert("Seleccione la region del Site");
         Form.cmbRegion.focus();
          return false;
      }      
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

      if (Form.txtAreaCodeFax.value!="" || Form.txtFax.value!=""){
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

   /*function checkOnClose1(){
      event.returnValue = "Si desea cerrar esta ventana, haga click ...";
   }*/

   function fxCheckOnClose(){
      Form = document.formdatos;
      if (Form.flg_enabled.value == 0) return false;
      parent.window.close();
      return true;
   }
   
   function fxMaxLong(strLongMax){
   
      //Validación del caracter "'" en el campo
      //---------------------------------------
      if (validar(document.formdatos.txtSiteName.value) == false){
        alert("El cáracter ' no esta permitido, vuelva a insertar el nombre...");
        document.formdatos.txtSiteName.value = "";
        document.formdatos.txtSiteName.focus(); return true;
      }
      
      //Validación de la longitud máxima en el campo
      //--------------------------------------------
      if(document.formdatos.txtSiteName.value.length>strLongMax){
        alert("El dato excede el tamaño permitido, por favor abrévielo…");
        document.formdatos.txtSiteName.value = "";
        document.formdatos.txtSiteName.focus(); 
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
  
  function fxSubmit(){
    form = document.formdatos;
      if (fxValidate()==false) 
        return false; 
      
      form.myaction.value="InsertSite";     
      form.hdnSessionId.value="<%=strSessionId%>";
      form.hdnSolutionGroup.value = form.cmbGrupo[form.cmbGrupo.selectedIndex].value;
      form.hdnSpecificationId.value="<%=strSpecificationId%>"; //CEM COR0354	  
      form.action="<%=strURLOrderServlet%>";           
      form.submit();    
  }
   
</script>
<%
//Encontramos la Maxima Longitud para el nombre del site
//-------------------------------------------------------
 
  hshData=objSiteService.getLongMaxSite();
  strMessage=(String)hshData.get("strMessage");
  String strLongMax=null;
  if (strMessage!=null)
      throw new Exception(strMessage);       
     strLongMax=(String)hshData.get("strLongSite");
     
%>
  <FORM method="get" name="formdatos" target="bottomFrame"><!-- onSubmit="return fxValidate()"-->
     <input type="hidden" name="hdnOrderId" value="<%=lOrderId%>">
     <input type="hidden" name="hdnCustomerId" value="<%=lCustomerId%>">
     <input type="hidden" name="flg_enabled" value="1">     
     <input type="hidden" name="hdnRegionId" value=""> <!--'||an_regionid||'-->
     <input type="hidden" name="hdnSessionId"> 
     <input type="hidden" name="myaction" value=""> 
     <input type="hidden" name="hdnSpecificationId" value="">  <!--CEM COR0354-->
     <input type="hidden" name="hdnSolutionGroup" value=""> 
	 
     <table border="0" cellspacing="0" cellpadding="0" width="100%" class="RegionBorder">
        <tr>
           <td width="100%" class="CellContent" align="center"><font class="TabForegroundText">Ingreso de Datos para Site Nuevo</font></td>
        </tr>
     </table>
     <table border="0" cellspacing="0" cellpadding="0"><tr><td width="100%"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/pobtrans.gif" border="0" height="5"></td></tr></table>

     <table border="0" width="99%" cellpadding="0" cellspacing="0" align="center">
        <tr>
           <td>
              <table border="0" cellspacing="0" cellpadding="0">
                 <tr class="PortletHeaderColor">
                    <td class="SubSectionTitleLeftCurve" valign="top" align="left" width="16"></td>
                    <td class="SubSectionTitle" align="left" valign="top">Datos Generales</td>
                    <td class="SubSectionTitleRightCurve" valign="top" align="right" width="12"></td>
                 </tr>
              </table>
           </td>
        </tr>
     </table>
     <table border="0" width="99%" cellpadding="2" cellspacing="0" class="RegionBorder" align="center">
        <tr>
           <td class="RegionHeaderColor" width="100%">
              <center>
              <table width="100%" border="0" cellspacing="1" cellpadding="0">
                 <tr>
                    <td class="CellLabel">&nbsp;Compañía</TD>          
                    <td colspan="3" class="CellContent"><%=strCustomerName%></td>
                 </tr>
                 <tr>
                    <td class="CellLabel"><font class="Required">*</font>Nombre Site</TD>
                    <td class="CellContent"><input type="text" name="txtSiteName" SIZE="40" onchange="this.value=trim(this.value.toUpperCase());return fxMaxLong(<%=strLongMax%>)"></TD>
                    <td class="CellLabel"><font class="Required">*</font>Región</TD>
                    <td class="CellContent">
                       <select name="cmbRegion" onChange="javascript:document.formdatos.hdnRegionName.value=this[this.selectedIndex].text" >                        
                         <%=MiUtil.buildCombo(arrRegion,"swregionid","swname")%>                                      
                       </select>     
                       <input type="hidden" name="hdnRegionName" >
                    </td>
                 </tr>
                 <!-- Telefono -->
                 <tr>
                    <td class="CellLabel"><a href="javascript:fxCodeArea('txtAreaCode','spanAreaNom1','txtPhone')">Área</a>/Tel&eacute;fono&nbsp;</td>
                    <td class="CellContent">
                       <input type="text" name="txtAreaCode" size="1" maxlength="2" onchange="this.value=trim(this.value);return fxSearchAreaCode(this.value, 'txtAreaCode','spanAreaNom1','txtPhone')">
                       <input type="text" name="txtPhone" size="7" maxlength="12">
                       <small><span id="spanAreaNom1"></span></small>
                       
                    </td>
                    <td class="CellLabel" >&nbsp;<a href="javascript:fxCodeArea('txtAreaCodeFax','spanAreaNom2','txtFax')">Área</a>/Fax&nbsp;</td>
                    <td class="CellContent">
                       <input type="text" name="txtAreaCodeFax" size="1" maxlength="2" onchange="this.value=trim(this.value);return fxSearchAreaCode(this.value, 'txtAreaCodeFax','spanAreaNom2','txtFax')">
                       <input type="text" name="txtFax" size="7" maxlength="8">
                       <small><span id="spanAreaNom2"></span></small>
                    </td>
                 </tr>
                 <!--Fin Fila Teléfono-->
                 <!--Combo Grupo Solución -->
                 <tr>
                   <td class="CellLabel">Grupo de Solución</td>
                   <td colspan="3" class="CellContent">
                      <select name="cmbGrupo">
                      <%                      
                      for(int i = 0; i < arrSolGroup.size(); i++){
                         objSolutionGroup = (SolutionGroupBean)arrSolGroup.get(i);                         
                         %><option value="<%=objSolutionGroup.getNpsolutiongroupid()%>"><%=objSolutionGroup.getNpname()%></option><%
                      }                      
                      %>
                        
                      </select>         							
                    </td>                      	
                  </tr>    
              </table>
              </center>
           </td>
        </tr>
     </table>

     <table border="0" cellspacing="0" cellpadding="0"><tr><td width="100%"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/pobtrans.gif" border="0" height="5"></td></tr></table>

     <table width="99%" align="center">
        <tr>
           <td align="center">
              <input type="button" value="   Guardar   " name="btnSubmit" onclick="javascript:fxSubmit()">
              <input type="button" value="   Cancelar   " name="btnCancel" onclick="javascript:fxCheckOnClose()">
           </td>
        </tr>
     </table>
     <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr>
           <td width="100%"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/pobtrans.gif" border="0" height="6"></td>
        </tr>
     </table>
  </FORM>
  </body>
  </html>
    
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
%>      

<% 
}catch(Exception ex){
  System.out.println("Error try catch-->"+MiUtil.getMessageClean(ex.getMessage())); 
%>
<script>
  alert("<%=MiUtil.getMessageClean(ex.getMessage())%>");
</script>
<%
}
%>  