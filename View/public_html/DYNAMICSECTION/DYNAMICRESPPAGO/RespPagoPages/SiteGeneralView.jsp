<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.bean.*"%>
<%@ page import="pe.com.nextel.service.*"%>
<%@ page import="pe.com.nextel.util.*"%>
<%@ page import="java.util.*"%>
<%    
try{ 
   long lOrderId=(request.getParameter("nOrderId")==null?0:Long.parseLong(request.getParameter("nOrderId")));
   long lCustomerId=(request.getParameter("nCustomerId")==null?0:Long.parseLong(request.getParameter("nCustomerId")));
   long lSiteId=(request.getParameter("nSiteId")==null?0:Long.parseLong(request.getParameter("nSiteId")));   
   //String strAction=(request.getParameter("sAction")==null?"R":request.getParameter("sAction"));
   String strSessionId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));
   String strSpecificationId=(request.getParameter("pSpecificationId")==null?"0":request.getParameter("pSpecificationId")); //CEM COR0354
   
   SiteService objSiteS=new SiteService();  
   GeneralService objGeneralS=new GeneralService();
   SiteBean objSiteBean =new SiteBean();
   String strMsgError=null;   

   HashMap hshData=objSiteS.getSiteDetail(lCustomerId,lSiteId);   
   strMsgError =(String)hshData.get("strMessage");
   HashMap hshSite =(HashMap)hshData.get("hshData");
   long lRegionId=Long.parseLong(MiUtil.getIfNotEmpty((String)hshSite.get("swregionid")));
   
   String strPhoneArea=MiUtil.getString((String)hshSite.get("swofficephonearea"));
   String strFaxArea=(String)hshSite.get("swfaxarea");   
   
   String strOffPhoneDep =objGeneralS.getDepartmentName(strPhoneArea);
   String strFaxDep =objGeneralS.getDepartmentName(strFaxArea);
   
   /*Cambio de Cuenta de Facturación*/
   String strStatusBA = MiUtil.getString(objGeneralS.getValue(Constante.NPT_SECTION_BA_FROM_SITE,Constante.KN_ORDERS));
   
   //String strParam="?nOrderId="+lOrderId+"&nCustomerId="+lCustomerId+"&nSiteId="+lSiteId+"&nRegionId="+lRegionId+"&hdnSessionId="+strSessionId;
   String strParam="?nOrderId="+lOrderId+"&nCustomerId="+lCustomerId+"&nSiteId="+lSiteId+"&nRegionId="+lRegionId+"&hdnSessionId="+strSessionId+"&pSpecificationId="+strSpecificationId; //CEM COR0354
   
   //Ruta   
   String strURLDirecciones="AddressView.jsp"+strParam;
   String strURLContactos="ContactView.jsp"+strParam;
   String strURLBillAcc="BillingAccountListView.jsp"+strParam;   
   
   System.out.println(" --------------------  INCIO SITEGENERAL VIEW.jsp---------------------- ");   
   //System.out.println("strAction :" + strAction);
   System.out.println("lSiteId :" + lSiteId);
   System.out.println("lCustomerId :" + lCustomerId);
   System.out.println("lOrderId :" + lOrderId);
   System.out.println("strSessionId :" + strSessionId);  
   System.out.println("strSpecificationId :" + strSpecificationId);  
    
   System.out.println(" --------------------  FIN SITEGENERAL VIEW.jsp---------------------- ");
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
      <TD  class="TabForegroundColor"><IMG SRC="<%=Constante.PATH_APPORDER_SERVER%>/images/pobtrans.gif" BORDER="0" HEIGHT="3"></TD>
   </TR>
</TABLE>  
</DIV>
<TABLE  BORDER="0" WIDTH="100%" CELLPADDING="4" CELLSPACING="0" class="RegionNoBorder">
   <TR>
      <TD WIDTH="100%">
         <TABLE BORDER="0" class="PortletHeaderColor" CELLSPACING="0" CELLPADDING="0" WIDTH="100%">
            <TR class="PortletHeaderColor">
               <TD class="LeftCurve" valign="top" align=LEFT width="10" height="10" NOWRAP="">&nbsp;&nbsp;</TD>
               <TD class="PortletHeaderColor" NOWRAP="" align=LEFT valign="top" width="100%">
               <FONT class="PortletHeaderText">Datos para Site Nuevo</FONT>
               &nbsp;&nbsp;&nbsp;</TD>
               <TD align="RIGHT" class="PortletHeaderColor" NOWRAP="" valign="top">&nbsp;
               </TD><TD align="right" class="RightCurve" width="10" NOWRAP="">&nbsp;&nbsp;</TD>
            </TR> 
         </TABLE>
         <TABLE  BORDER="1" WIDTH="100%" CELLPADDING="2" CELLSPACING="0" class="RegionBorder">
            <TR>
               <TD class="RegionHeaderColor" WIDTH="100%">  
                 <table width="99%" border="0" cellspacing="1" cellpadding="0" align="center">
                    <tr>
                       <td class="CellLabel"   width="20%">&nbsp;Nombre Site</td>
                       <td class="CellContent" width="30%">&nbsp;<%=MiUtil.getString((String)hshSite.get("swsitename"))%></td>
                       <td class="CellLabel"   width="20%">&nbsp;Region</td>
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
                    <form name="formCustomer">
                           <input type="hidden" name="flg_enabled" value="1">
                    </form>
                    <tr>
                       <td colspan="4" class="CellContent" align="center">
                          <input type="button" name="btnClose" value="   Cerrar   " onclick="javascript:parent.close();">
                       </td>
                    </tr>
                 </table>

               </TD>
            </TR>
         </TABLE>
      </TD>
   </TR>
</TABLE>
</html>
<%      
}catch(Exception  ex){                
   System.out.println("Error try catch-->"+MiUtil.getMessageClean(ex.getMessage())); 
%>
<script>
   alert("<%=MiUtil.getMessageClean(ex.getMessage())%>");
</script>
<%
}
%>