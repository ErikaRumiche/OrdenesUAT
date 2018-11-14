<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.*"%>
<%@ page import="java.util.*"%>
<%@ page import="pe.com.nextel.util.*"%>
<%@ page import="pe.com.nextel.bean.*"%>
<% 
try{   

   int iIndex=(request.getParameter("nIndex")==null?-1:Integer.parseInt(request.getParameter("nIndex")));    
   long lNewBillAccId=(request.getParameter("nNewBillAccId")==null?-1:Integer.parseInt(request.getParameter("nNewBillAccId")));   
   long lCustomerId=(request.getParameter("nCustomerId")==null?0:Long.parseLong(request.getParameter("nCustomerId")));
   long lSiteId=(request.getParameter("nSiteId")==null?0:Long.parseLong(request.getParameter("nSiteId")));
   long lOrderId=(request.getParameter("nOrderId")==null?0:Long.parseLong(request.getParameter("nOrderId")));
   String strSessionId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));
   String strSpecificationId=(request.getParameter("pSpecificationId")==null?"0":request.getParameter("pSpecificationId")); //CEM COR0354
   
   String strMessage=null;
   ArrayList arrLista=new ArrayList();   

   GeneralService objGeneralService=new GeneralService();   
   BillingAccountService objBillAccService=new BillingAccountService();
   BillingContactBean objContactBean = null;
   BillingAccountBean objAccountBean = null;
   BillingAccountBean objNewAccountBean = null;
   BillingContactBean objNewContactBean = null;
   ArrayList arrContact=null;   
   ArrayList arrNewContact=null;   
   HashMap hshData=null;
   hshData=objBillAccService.getContactBillCreateList(lCustomerId,lSiteId);   
   strMessage=(String)hshData.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);    
   arrContact=(ArrayList)hshData.get("arrListado");   
   if (arrContact==null)
      arrContact=new ArrayList();
   hshData=objBillAccService.getNewContactBilling(lNewBillAccId);   
   strMessage=(String)hshData.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);    
   objNewAccountBean=(BillingAccountBean)hshData.get("objAccount");      
   
   objNewContactBean=objNewAccountBean.getObjBillingContactB();
   
   //String strParam="?nOrderId="+lOrderId+"&nCustomerId="+lCustomerId+"&nSiteId="+lSiteId+"&hdnSessionId="+strSessionId;   
   String strParam="?nOrderId="+lOrderId+"&nCustomerId="+lCustomerId+"&nSiteId="+lSiteId+"&hdnSessionId="+strSessionId+"&pSpecificationId="+strSpecificationId; //CEM COR0354
   //rutas
   String strURLDirecciones="AddressView.jsp"+strParam;
   String strURLGeneralData="SiteGeneralView.jsp"+strParam;
   String strURLContactos="ContactView.jsp"+strParam;  

   System.out.println(" -------------------- INICIO BillingAccountView.jsp---------------------- ");
   System.out.println("strSessionId :" + strSessionId);
   System.out.println("lSiteId :" + lSiteId);
   System.out.println("lCustomerId :" + lCustomerId);  
   System.out.println("lOrderId :" + lOrderId);
   System.out.println("strSpecificationId :" + strSpecificationId);
   System.out.println(" --------------------  FIN BillingAccountView.jsp---------------------- ");   
%>
<html>
   <head>
      <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
      <title>Contact</title>
   </head>

<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css">
<script language="JavaScript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/date-picker.js"></script>                        
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>             
<script language="JavaScript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/DateTimeBasicOperations.js"></script>            
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderIXEdit.js"></script>
<table   BORDER="0" CELLPADDING="0" CELLSPACING="0">
<tr>
   <td valign="top">&nbsp;&nbsp;</td>
   <td>
      <table  BORDER="0" HEIGHT="100%" WIDTH="100%" CELLPADDING="0" CELLSPACING="0">
         <tr>
            <td class="LeftTabBgSlant" valign="top" align="left" width="10" height="19" NOWRAP="">&nbsp;</td>
            <td valign="middle" NOWRAP class="TabBackgroundColor">&nbsp;&nbsp;<a href="<%=strURLGeneralData%>"><FONT class="TabBackgroundText">Datos Generales</FONT></a>&nbsp;&nbsp;</td>
            <td align="right" class="RightTabBgCurve" width="8" NOWRAP="">&nbsp;&nbsp;</td>
         </tr>
      </table>
   </td>
   <td>
      <table  BORDER="0" HEIGHT="100%" WIDTH="100%" CELLPADDING="0" CELLSPACING="0">
         <tr>
            <td class="LeftTabBgSlant" valign="top" align="left" width="10" height="19" NOWRAP="">&nbsp;</td>
            <td valign="middle" nowrap class="TabBackgroundColor">&nbsp;&nbsp;<A HREF="<%=strURLDirecciones%>"><FONT class="TabBackgroundText">Direcciones</FONT></A>&nbsp;&nbsp;</td>
            <td align="right" class="RightTabBgCurve" width="8" NOWRAP="">&nbsp;&nbsp;</TD>
         </tr>
      </table>
   </td>
   <td>
      <table  BORDER="0" HEIGHT="100%" WIDTH="100%" CELLPADDING="0" CELLSPACING="0">
         <tr>
            <td class="LeftTabBgSlant" valign="top" align="left" width="10" height="19" NOWRAP="">&nbsp;</td>
            <td valign="middle" nowrap class="TabBackgroundColor">&nbsp;&nbsp;<A HREF="<%=strURLContactos%>"><FONT class="TabBackgroundText">Contactos</FONT></A>&nbsp;&nbsp;</td>
            <td align="right" class="RightTabBgCurve" width="8" NOWRAP="">&nbsp;&nbsp;</td>
         </tr>
      </table>
   </td>
   <td valign="top">&nbsp;&nbsp;</TD><TD>
      <table  BORDER="0" HEIGHT="100%" WIDTH="100%" CELLPADDING="0" CELLSPACING="0">
         <tr>
            <td class="LeftTabForeSlant" valign="top" align="left" width="10" height="19" NOWRAP="">&nbsp;</td>
            <td valign="middle" NOWRAP class="TabForegroundColor">&nbsp;&nbsp;<FONT class="TabForegroundText">Billing Account</FONT>&nbsp;&nbsp;</td>
            <td align="right" class="RightTabForeCurve" width="8" NOWRAP="">&nbsp;&nbsp;</td>
         </tr>
      </table>
   </td>   
</tr>
</table>
<table  WIDTH="100%" BORDER="0" CELLPADDING="0" CELLSPACING="0" class="TabForegroundColor">
<tr>
   <td  class="TabForegroundColor"><IMG SRC="<%=Constante.PATH_APPORDER_SERVER%>/images/pobtrans.gif" BORDER="0" HEIGHT="3"></TD>
</tr  >
</table>  
</DIV>
<table  BORDER="0" WIDTH="85%" CELLPADDING="4" CELLSPACING="0" class="RegionNoBorder">
<tr>
   <td WIDTH="100%">
      <table BORDER="0" class="PortletHeaderColor" CELLSPACING="0" CELLPADDING="0" WIDTH="100%">
         <tr class="PortletHeaderColor">
            <td class="LeftCurve" valign="top" align=LEFT width="10" height="10" NOWRAP="">&nbsp;&nbsp;</td>
            <td class="PortletHeaderColor" NOWRAP="" align=LEFT valign="top" width="100%">
            <font class="PortletHeaderText">Cuenta Facturación</font>
            &nbsp;&nbsp;&nbsp;</TD>
            <td align="RIGHT" class="PortletHeaderColor" NOWRAP="" valign="top">&nbsp;
            </td><td align="right" class="RightCurve" width="10" NOWRAP="">&nbsp;&nbsp;</td>
         </tr> 
      </table>
      <table  BORDER="1" WIDTH="100%" CELLPADDING="2" CELLSPACING="0" class="RegionBorder">
         <TR>
            <TD class="RegionHeaderColor" WIDTH="100%"> 
               <script>
                  var vpAreaCode = new Vector();
                  var flag1=0;
                  var flag2=0;
               </script>  
   
               <form method="post" name="formdatos" target="bottomFrame">      
                  <input type="hidden" name="myaction"/>        
                  <input type="hidden" name="hdnNewBillAcc" value="<%=lNewBillAccId%>"/>
                  <input type="hidden" name="hdnSizeArrayContact" value="<%=arrContact.size()%>"/>    
                  <input type="hidden" name="hdnStatus" />        
                  <input type="hidden" name="hdnCustomerId" value="<%=lCustomerId%>">
                  <input type="hidden" name="hdnOrderId" value="<%=lOrderId%>">
                  <input type="hidden" name="hdnSiteId"     value="<%=lSiteId%>">    
				  <input type="hidden" name="hdnSpecificationId"   value="<%=strSpecificationId%>"> <!-- CEM COR0354 -->
                  <input type="hidden" name="hndBscsCustId">
                  <input type="hidden" name="hndBscsSeq">          
                  <table  border="0" width="100%" cellpadding="2" cellspacing="1">              
                  <tr>             
                     <td class="CellLabel" align="center" colspan="2">Cuenta Facturaci&oacute;n</td>
                     <td class="CellContent" align="left" colspan="3">
                     <%=MiUtil.getString(objNewAccountBean.getNpBillacCName())%>
                     </td>
                  </tr>
                  <tr>
                     <td align="center" colspan="3"></td>
                  </tr>
                  <tr>
                     <td class="CellLabel" align="center" width="5%">&nbsp;</td>
                     <td class="CellLabel" align="center" width="15%">Nombre Contacto</td>
                     <td class="CellLabel" align="center" colspan="2" width="20%">Direcci&oacute;n Contacto</td>
                     <td class="CellLabel" align="center" width="10%">Tipo</td>                  
                  </tr> 
                  <% for (int i=0;i< arrContact.size();i++){
                        objAccountBean=(BillingAccountBean)arrContact.get(i);
                        objContactBean = objAccountBean.getObjBillingContactB();                
                  %>
                  <tr>
                     <td class="CellContent" align="left">
                        <input type="radio" name="rdbBaccount" value="<%=MiUtil.getIfNotEmpty(objAccountBean.getNpBscsCustomerId())%>"  >
                     </td>
                     <td class="CellContent" align="left"><%=MiUtil.getString(objContactBean.getNpfname())%></td>
                     <td class="CellContent" align="left" colspan="2"><%=MiUtil.getString(objContactBean.getNpaddress1())%></td>
                     <td class="CellContent" align="left"><%=MiUtil.getString(objContactBean.getNpTypeContact())%></td>
                  </tr>
                  <%}%>
                  <tr>
                     <td class="CellContent" align="left">
                     <input type="radio" name="rdbBaccount" value="-1" checked="checked">
                  </td>
                     <td class="CellContent" align="left" colspan="4"><em>&lt; Nuevo &gt;</em></td>                 
                  </tr>
                  <tr>
                     <td align="center" colspan="3"></td>
                  </tr>             
                  <tr>
                     <td colspan="5">        
                        <table width="100%">
                           <tr>                  
                              <td class="CellLabel" align="left" colspan="2" width="20%">Titulo</td>
                              <td class="CellContent" colspan="3" width="80%">&nbsp;<%=MiUtil.getString(objNewContactBean.getNpTitle())%>                     
                              </td>
                           </tr>
                           <tr>                  
                              <td class="CellLabel" align="left" colspan="2"><font class="Required">*</font>&nbsp;Nombres</td>
                              <td class="CellContent" colspan="3">&nbsp;
                              <%=MiUtil.getString(objNewContactBean.getNpfname())%>                 
                           </tr>
                           <tr>                  
                              <td class="CellLabel" align="left" colspan="2"><font class="Required">*</font>&nbsp;Apellidos</td>
                              <td class="CellContent" colspan="3">&nbsp;
                              <%=MiUtil.getString(objNewContactBean.getNplname())%>                
                           </tr>
                           <tr>                  
                              <td class="CellLabel" align="left" colspan="2"><font class="Required">*</font>&nbsp;Cargos</td>
                              <td class="CellContent" colspan="3">&nbsp;<%=MiUtil.getString(objNewContactBean.getNpjobtitle())%>                     
                              </td>                  
                           </tr>            
                           <tr>                  
                              <td class="CellLabel" align="left" colspan="2">
                              Área/Teléfono</td>
                              <td class="CellContent" colspan="3" >&nbsp;                  
                                 <%=MiUtil.getString(objNewContactBean.getNpphonearea())%>&nbsp;
                                 <%=MiUtil.getString(objNewContactBean.getNpphone())%>
                                 <small><span id="spanAreaNom1"></span></small>
                              </td>                  
                           </tr>
                           <tr>                           
                              <td class="CellLabel" align="left" colspan="2">Dirección</td>
                              <td class="CellContent" colspan="3">
                              &nbsp;<%=MiUtil.getString(objNewContactBean.getNpaddress1())%>
                              &nbsp;<%=MiUtil.getString(objNewContactBean.getNpaddress2())%>
                              </td>                  
                           </tr>
                           <tr>
                              <td class="CellLabel" align="left" colspan="2" width="20%">Departamento</td>
                              <td class="CellContent" width="30%">&nbsp;
                                 <%=MiUtil.getString(objNewContactBean.getNpdepartment())%>
                              </td>     
                              <td class="CellLabel" align="left" width="20%">Provincia</td>
                              <td class="CellContent" width="30%">&nbsp;<%=MiUtil.getString(objNewContactBean.getNpstate())%>                   
                              </td>   
                           </tr>
                           <tr>                  
                              <td class="CellLabel" align="left" colspan="2" width="20%">Distrito</td>
                              <td class="CellContent" width="30%">&nbsp;<%=MiUtil.getString(objNewContactBean.getNpcity())%></td>     
                              <td class="CellLabel" align="left">Cod. Postal</td>
                              <td class="CellContent">&nbsp;<%=MiUtil.getString(objNewContactBean.getNpzipcode())%>
                              </td>   
                           </tr>  
                        </table>
                     </td> 
                  </tr>
                  <tr>
                     <td align="center" colspan="6">
                        <table>
                           <tr>         
                              
                              <td><input type="button" value="Cancelar" onClick="javascript:fxCerrar();"></td>                     
                           </tr>
                        </table>
                     </td>  
                  </tr>        
                  </table>    
               </form>
            </td>
         </tr>
      </table>
   </td>
</tr>
</html>  
<script type="text/javascript">
   function fxAreaCode(n1,n2){
      this.name = n1;
      this.codearea = n2;
   }
   
   function fxCerrar(){
      param="?nCustomerId=<%=lCustomerId%>&nSiteId=<%=lSiteId%>&nOrderId=<%=lOrderId%>&pSpecificationId=<%=strSpecificationId%>";//&nRegionId=<%-- =lRegionId --%>";  //CEM - COR0354          
      url="SiteBillingAccountConsulta.jsp"+param;
      parent.mainFrame.location.replace(url);      
   }
   
   function fxLoadBody(){
      var bol=false;
      var ind=0;       
      frm = document.formdatos;  
      var  bscsCustId = "<%=MiUtil.getString(objNewAccountBean.getNpBscsCustomerId())%>";      
      
      //Si solo se creo el radio button "Nuevo"
      if (frm.hdnSizeArrayContact.value==0){      
           //frm.rdbBaccount.checked=true;           
           <% if (!"".equals(MiUtil.getString(objNewContactBean.getNpfname()))){%>//no es un contacto nuevo
                  //        
              frm.rdbBaccount.checked=true;               
           <%}%>         
      }else{//Se añadireon a la lista contactos de Bscs
         //De los radio button de los contactos existentes selecciona el indicado
         for (i=0;i<frm.rdbBaccount.length;i++){
            if (frm.rdbBaccount[i].value=='-1'){
               ind=i;
            }else{         
               var valorRadio=frm.rdbBaccount[i].value;
               //alert("valorRadio-->"+valorRadio);
               var codigos = valorRadio.split("|");           
               /*alert("codigos[0]-->"+codigos[0]);
               alert("codigos[1]-->"+codigos[1]);*/
               if ( codigos[0]==bscsCustId && codigos[1]==bscsSeq){
                    frm.rdbBaccount[i].checked=true;
                    bol=true;
                    break;  
               }                  
            }             
         }    
         if (bol==false && frm.txtContactName.value!=''){
            frm.rdbBaccount[ind].checked=true;;            
         }               
      }
      return;

   }   
      
</script>

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
         vpAreaCode.addElement(new fxAreaCode('<%=MiUtil.getString((String)hshMapData.get("city"))%>','<%=MiUtil.getString((String)hshMapData.get("areaCode"))%>'));
      </script>
<%  }
%>       
    
<script>
   onload=fxLoadBody;  
</script>

<%}catch(Exception ex){
  System.out.println("Error try catch-->"+MiUtil.getMessageClean(ex.getMessage())); 
%>
<script>
  alert("<%=MiUtil.getMessageClean(ex.getMessage())%>");
  history.back(-1);
</script>
<%
}%>