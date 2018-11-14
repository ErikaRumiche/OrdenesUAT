<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.bean.*"%>
<%@ page import="pe.com.nextel.service.*"%>
<%@ page import="pe.com.nextel.util.*"%>
<%@ page import="java.util.*"%>
<%
try{
   long lCustomerId=(request.getParameter("nCustomerId")==null?0:Long.parseLong(request.getParameter("nCustomerId")));
   long lSiteId=(request.getParameter("nSiteId")==null?0:Long.parseLong(request.getParameter("nSiteId")));
   long lOrderId=(request.getParameter("nOrderId")==null?0:Long.parseLong(request.getParameter("nOrderId")));
   String strSessionId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));
   String strSpecificationId=(request.getParameter("pSpecificationId")==null?"0":request.getParameter("pSpecificationId")); //CEM COR0354
   
   ArrayList arrLista=null;
   BillingAccountBean objBillingABean=null;
   BillingContactBean objBillContBean=null;
   BillingAccountService objBAService=new BillingAccountService();   
   String strCustomerType=null;
   
   if (lSiteId!=0)
      strCustomerType=Constante.CUSTOMERTYPE_SITE;
   else
      strCustomerType=Constante.CUSTOMERTYPE_CUSTOMER;
	  
   System.out.println("strCustomerType"+strCustomerType);
   System.out.println("lSiteId"+lSiteId+"");
   System.out.println("lOrderId"+lOrderId+"");
   System.out.println("Llamando a objBAService.getAccountList");
   HashMap hshBillAcc=objBAService.getAccountList(strCustomerType,lSiteId,lOrderId); 
   arrLista=(ArrayList)hshBillAcc.get("objCuentas");
   //String strParam="?nOrderId="+lOrderId+"&nCustomerId="+lCustomerId+"&nSiteId="+lSiteId+"&hdnSessionId="+strSessionId;   
   String strParam="?nOrderId="+lOrderId+"&nCustomerId="+lCustomerId+"&nSiteId="+lSiteId+"&hdnSessionId="+strSessionId+"&pSpecificationId="+strSpecificationId; //CEM COR0354
   
   //RUTAS
   String strURLBillAccEdit = "BillingAccountEdit.jsp"; 
   String strURLBillAccNew = "BillingAccountNew.jsp";    
   String strURLDirecciones="AddressList.jsp"+strParam;
   String strURLGeneralData="SiteGeneralNew.jsp"+strParam;
   String strURLContactos="ContactList.jsp"+strParam;   
   String strURLOrderServlet =request.getContextPath()+"/editordersevlet";    
   
   System.out.println(" ----------- INICIO BillingAccountList.jsp---------------- ");
   System.out.println("lSiteId-->"+lSiteId+"");
   System.out.println("strCustomerType-->"+strCustomerType);
   System.out.println("nCustomerId-->"+lCustomerId);
   System.out.println("nOrderId-->"+lOrderId);
   System.out.println("nSiteId-->"+lSiteId);   
   System.out.println("strSessionId-->"+strSessionId);      
   System.out.println("strSpecificationId-->"+strSpecificationId);
   System.out.println("Tamaño del arreglo de BillingAccountList-->"+arrLista.size());
   System.out.println(" ------------  FIN BillingAccountList.jsp----------------- ");    
   
%>
<html>
<head>
   <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
   <title>Direcciones</title>
</head>

<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css">
<script language="javaScript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
<script language="JavaScript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderIXEdit.js"></script>

<DIV ALIGN="LEFT">
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
</td>
</table>  
</DIV>
<table  BORDER="0" WIDTH="100%" CELLPADDING="4" CELLSPACING="0" class="RegionNoBorder">
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
      <TR><TD class="RegionHeaderColor" WIDTH="100%">  
      
      <form method="post" name="formdatos" target="bottomFrame">
       <input type="hidden" name="myaction"/>
       <input type="hidden" name="hdnNewBillAccId"/>
       <input type="hidden" name="hdnCustomerId" value="<%=lCustomerId%>">
       <input type="hidden" name="hdnOrderId" value="<%=lOrderId%>">
       <input type="hidden" name="hdnSiteId"     value="<%=lSiteId%>">
	    <input type="hidden" name="hdnSpecificationId" value="<%=strSpecificationId%>"> <!--CEM COR0354 -->
       <input type="hidden" name="hdnSessionId">
		 <input type="hidden" name="hdnFlag" value="0">
       
       
         <table  width="99%" border="0" cellspacing="0" cellpadding="0">
         <tr>
            <td class="CellContent">
               <table border="0" cellspacing="0" cellpadding="0">
               <tr>
                  <td align="right">    
                     <a href="javascript:fxAddNewBillAcc();" > 
                     <img name="item_img0" Alt="Agregar Billing Account" src="<%=Constante.PATH_APPORDER_SERVER%>/images/crear.gif" border="no">
                     </a>
                  </td>              
               </tr>
               </table>
            </td>
         </tr>
         </table>          
         <tr>
            <td class="RegionHeaderColor" WIDTH="100%">
               <script>             
             
                  function fxSectionNameOnload(){   
                     //alert("seccion dinamica");
                  }            
     
                  function fxSectionNameValidate(){           
                  // Pasando los valores del vectos a hiddens                          
                     return true;
                  }
                 
                  function fxSectionNameFinalStatus(){
                     //alert("final status seccion 1");   
                     return true;  
                  }
                  
                  function fxShowDetailBA(baId,customerId,siteId,arrInd){
                     var v_parametros = "?nNewBillAccId=" + baId +"&nOrderId=<%=lOrderId%>&nCustomerId="+customerId+"&nSiteId="+siteId+"&nIndex="+arrInd+"&hdnSessionId=<%=strSessionId%>"+"&pSpecificationId=<%=strSpecificationId%>";   //1:Agregar  2:Editar   //CEM - COR0354                    
                     var v_Url=   "<%=strURLBillAccEdit%>" +v_parametros;                              
                     location.href=v_Url;                     
                  }
                  
                  function fxAddNewBillAcc(){       
                     var v_parametros = "<%=strParam%>";
                     var v_Url=   "<%=strURLBillAccNew%>" +v_parametros;       
                     location.href=v_Url;                     
                  }
               
                  function fxDelete(id){                  
                     form = document.formdatos;
                     form.myaction.value="DeleteBillAcc";                
                     form.hdnNewBillAccId.value=id;
							//Inicio CEM COR0354
							form.hdnCustomerId.value="<%=lCustomerId%>";
							form.hdnOrderId.value="<%=lOrderId%>";
							form.hdnSpecificationId.value="<%=strSpecificationId%>";
							form.hdnSiteId.value="<%=lSiteId%>";
							//Fin CEM COR0354		
                     form.hdnSessionId.value="<%=strSessionId%>";
                     form.action="<%=strURLOrderServlet%>";
                     form.submit();                 
                  }
               
                </script>    
               <table  border="0" width="60%" cellpadding="2" cellspacing="1" class="RegionNoBorder">
               <tr>
                  <td>
                     <table id="tableBillAcc" name="tableBillAcc" border="0" width="100%" cellpadding="2" cellspacing="1">                     
                     <tr id="cabecera">
                        <td class="CellLabel" align="center">#</td>
                        <td class="CellLabel" align="center">Cuenta Facturaci&oacute;n</td>
                        <td class="CellLabel" align="right" >&nbsp;  
                        </td>                                 
                     </tr>
                     <% for (int i=0;i< arrLista.size();i++)
                     {   objBillingABean=(BillingAccountBean)arrLista.get(i);
                        objBillContBean=objBillingABean.getObjBillingContactB();                     
                     
                     %>
                     <tr id="<%=i+1%>">
                        <td class="CellContent" align="left"><%=i+1%></td>
                        <td class="CellContent" align="left">
                          <a href="javascript:fxShowDetailBA(<%=objBillingABean.getNpBillaccountNewId()%>,<%=lCustomerId%>,<%=lSiteId%>,<%=i+1%>);"><%=MiUtil.getString(objBillingABean.getNpBillacCName())%></a>
                        </td>
                        <td class="CellContent" align="right">&nbsp;
                        <a href="javascript:fxDelete(<%=objBillingABean.getNpBillaccountNewId()%>);"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/Eliminar.gif" alt="Eliminar" border="0" hspace=2></a>
                        </td>      
                     </tr>                     
                     <%}%>              
                     </table>
                  </td>
               </tr>
               <tr>
                  <td>
                     <table id="hdnBillAcc" name="hdnBillAcc"> </table>
                  </td>
               </tr> 
               </table> 
            </td>
         </tr>
      </td>
      </tr>
      </table>
     </form> 
   </td>
</tr>
</table>
</html> 
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