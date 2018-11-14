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
   String strAction=(request.getParameter("sAction")==null?"R":request.getParameter("sAction"));
   String strSessionId=(request.getParameter("hdnSessionId")==null?"0":request.getParameter("hdnSessionId"));   
   String strSpecificationId=(request.getParameter("pSpecificationId")==null?"0":request.getParameter("pSpecificationId")); //CEM COR0354
   
   SiteService objSiteS=new SiteService();
   CustomerService objCustomerS=new CustomerService();
   SiteBean objSiteB=new SiteBean();
   GeneralService objGeneralS=new GeneralService();
   HashMap hshMandatoryAdd=null;
   HashMap hshAddress=null;
   ArrayList arrAddress=null;
   HashMap hshData=null;
   String strMessage=null;
   String strObjectType=null;
   long lObjectId=0;
   int iResultado=0;
   
   /*Cambio de Cuenta de Facturación*/
   String strStatusBA = MiUtil.getString(objGeneralS.getValue(Constante.NPT_SECTION_BA_FROM_SITE,Constante.KN_ORDERS));
   
   
   //String strParam="?nOrderId="+lOrderId+"&nCustomerId="+lCustomerId+"&nSiteId="+lSiteId+"&hdnSessionId="+strSessionId;
   String strParam="?nOrderId="+lOrderId+"&nCustomerId="+lCustomerId+"&nSiteId="+lSiteId+"&hdnSessionId="+strSessionId+"&pSpecificationId="+strSpecificationId; //CEM COR0354
   
   //RUTAS
   String strURLGeneralData="SiteGeneralView.jsp"+strParam;
   String strURLContactos="ContactView.jsp"+strParam;
   String strURLBillAcc="BillingAccountListView.jsp"+strParam;             
   
   System.out.println(" -------------------- INICIO AddressView.jsp---------------------- ");   
   System.out.println("strAction :" + strAction);
   System.out.println("lSiteId :" + lSiteId);
   System.out.println("lCustomerId :" + lCustomerId);  
   System.out.println("lOrderId :" + lOrderId);     
   System.out.println("strSpecificationId :" + strSpecificationId);     
   System.out.println(" --------------------  FIN AddressView.jsp---------------------- ");
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Direcciones</title>
  </head>
<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css">
<DIV ALIGN="LEFT">
<TABLE  BORDER="0" CELLPADDING="0" CELLSPACING="0">
   <TR>
   <TD valign="top">&nbsp;&nbsp;</TD><TD>
      <TABLE  BORDER="0" HEIGHT="100%" WIDTH="100%" CELLPADDING="0" CELLSPACING="0">
       <TR>
          <TD class="LeftTabBgSlant" valign="top" align="left" width="10" height="19" NOWRAP="">&nbsp;</TD>
          <td valign="middle" nowrap class="TabBackgroundColor">&nbsp;&nbsp;<a href="<%=strURLGeneralData%>"><FONT class="TabBackgroundText">Datos Generales</FONT></a>&nbsp;&nbsp;</td>
          <TD align="right" class="RightTabBgCurve" width="8" NOWRAP="">&nbsp;&nbsp;</TD>
       </TR>
      </TABLE>
   </TD>
   <TD>
      <TABLE  BORDER="0" HEIGHT="100%" WIDTH="100%" CELLPADDING="0" CELLSPACING="0">
       <TR>
          <TD class="LeftTabForeSlant" valign="top" align="left" width="10" height="19" NOWRAP="">&nbsp;</TD>    
          <td valign="middle" NOWRAP class="TabForegroundColor">&nbsp;&nbsp;<FONT class="TabForegroundText">Direcciones</FONT>&nbsp;&nbsp;</td>
          <TD align="right" class="RightTabForeCurve" width="8" NOWRAP="">&nbsp;&nbsp;</TD>
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
           <FONT class="PortletHeaderText">Direcciones para Site Nuevo</FONT>
           &nbsp;&nbsp;&nbsp;</TD>
           <TD align="RIGHT" class="PortletHeaderColor" NOWRAP="" valign="top">&nbsp;
           </TD><TD align="right" class="RightCurve" width="10" NOWRAP="">&nbsp;&nbsp;</TD>
        </TR> 
      </TABLE>
      <TABLE  BORDER="1" WIDTH="100%" CELLPADDING="2" CELLSPACING="0" class="RegionBorder">
         <TR><TD class="RegionHeaderColor" WIDTH="100%">
   
<% 
   if (lSiteId != 0){
      strObjectType=Constante.CUSTOMERTYPE_SITE;
      lObjectId=lSiteId;
   }else{
      strObjectType=Constante.CUSTOMERTYPE_CUSTOMER;
      lObjectId= lCustomerId;
   }
   
   hshAddress= objSiteS.getAddressOrContactList(strObjectType,lObjectId,"TIPODIRECCION");
   arrAddress=(ArrayList)hshAddress.get("objArrayList");
   strMessage=(String)hshAddress.get("strMessage");
   iResultado=arrAddress.size();
   
   if (strMessage==null){
      if (iResultado==0)
         strMessage="No existe direcciones";
   }     
      
   if (strMessage!=null){ %>      
         <table width="99%" border="0" cellspacing="0" cellpadding="0">
            <tr>
               <td class="CellContent" valign="top" align="center"><b><font color="#FFOOOO"><%=strMessage%></b></font></td>
            </tr>
         </table>
<% }else{
      //----- Cargamos direcciones no intercambiables.(No se pueden eliminar.)--
      String strSiteType=null;
      String strCustomerType=null; 
      ArrayList arrMandatoryAddrtype=new ArrayList();
      ArrayList arrMandatoryAddrtypeflag=new ArrayList();
         
      strCustomerType= objCustomerS.getCustomerType(lCustomerId);                  
      hshData=objSiteS.getSiteType(lSiteId);
      strMessage=(String)hshData.get("strMessage");
      if (strMessage!=null)
         throw new Exception(strMessage);
       
      strSiteType=(String)hshData.get("strSiteType");
      hshMandatoryAdd=objSiteS.getCheckMandatoryAddress(lCustomerId,lSiteId); 
      strMessage=(String)hshMandatoryAdd.get("strMessage");
      if (strMessage!=null)
         throw new Exception(strMessage);
                            
      arrMandatoryAddrtype=(ArrayList)hshMandatoryAdd.get("objAddrType");
      arrMandatoryAddrtypeflag= (ArrayList)hshMandatoryAdd.get("objAddrTypeFlg");
      
      HashMap hshAddType=null;
      String strAddType=null;
      for (int i=0;i<arrMandatoryAddrtype.size();i++){
         hshAddType=((HashMap)arrMandatoryAddrtype.get(i));
         strAddType=(String)hshAddType.get("addstype");
%>      
         <%=MiUtil.getString(strAddType)%>

<%    } 
      
         String strEtiquetaDirec="";
         String strAddressId=null;
         String strAddress1=null;
         String strAddress2=null;
         String strAddress3=null;
         String strDepartmento=null;
         String strProvincia=null;
         String strDistrito=null;
         String strZip=null;
         int iLegal=0;
         int iFacturacion=0;
         int iEntrega=0;
         int iOtra=0;
         int iComunicacion=0;
         int iInstalacion=0;
         int iCorrespondencia=0;
         long lRegionId=0;
         hshAddress=null;
         for(int i=0;i<arrAddress.size();i++){            
            hshAddress=((HashMap)arrAddress.get(i));
            strAddressId=MiUtil.getString((String)hshAddress.get("swaddressid"));
            strAddress1=((String)hshAddress.get("swaddress1"));
            strAddress2=((String)hshAddress.get("swaddress2"));
            strAddress3=((String)hshAddress.get("swaddress3"));
            strZip=MiUtil.getString((String)hshAddress.get("swzip"));
            iLegal=MiUtil.parseInt((String)hshAddress.get("domicilio"));
            iFacturacion=MiUtil.parseInt((String)hshAddress.get("facturacion"));
            iEntrega=MiUtil.parseInt((String)hshAddress.get("entrega"));
            iComunicacion=MiUtil.parseInt((String)hshAddress.get("comunicacion"));
            iInstalacion=MiUtil.parseInt((String)hshAddress.get("instalacion"));
            iOtra=MiUtil.parseInt((String)hshAddress.get("otra"));
            iCorrespondencia=MiUtil.parseInt((String)hshAddress.get("correspondencia"));
            lRegionId =Long.parseLong((String)hshAddress.get("swregionid"));
            strDepartmento= MiUtil.getString((String)hshAddress.get("departamento"));
            strProvincia= MiUtil.getString((String)hshAddress.get("provincia"));
            strDistrito= MiUtil.getString((String)hshAddress.get("distrito"));
            strEtiquetaDirec="";
            if (iLegal==1){
               if (strEtiquetaDirec.length()>0){
                  strEtiquetaDirec= strEtiquetaDirec + " - ";
               }
               strEtiquetaDirec= strEtiquetaDirec + "DOMICILIO LEGAL";
            }
            
            if (iFacturacion==1){
               if (strEtiquetaDirec.length()>0){
                  strEtiquetaDirec= strEtiquetaDirec + "-";
               }
               strEtiquetaDirec= strEtiquetaDirec + "FACTURACION";
            }
            
            if (iEntrega==1){
               if (strEtiquetaDirec.length()>0){
                  strEtiquetaDirec= strEtiquetaDirec + "-";
               }
               strEtiquetaDirec= strEtiquetaDirec + "ENTREGA";
            }
            
            if (iComunicacion==1){
               if (strEtiquetaDirec.length()>0){
                  strEtiquetaDirec= strEtiquetaDirec + "-";
               }
               strEtiquetaDirec= strEtiquetaDirec + "COMUNICACIONES";
            }
            
            if (iInstalacion==1){
               if (strEtiquetaDirec.length()>0){
                  strEtiquetaDirec= strEtiquetaDirec + "-";
               }
               strEtiquetaDirec= strEtiquetaDirec + "INSTALACION";
            }
            
            if (iOtra==1){
               if (strEtiquetaDirec.length()>0){
                  strEtiquetaDirec= strEtiquetaDirec + "-";
               }
               strEtiquetaDirec= strEtiquetaDirec + "OTRA";
            }    
            
            if (iCorrespondencia==1){
               if (strEtiquetaDirec.length()>0){
                  strEtiquetaDirec= strEtiquetaDirec + "-";
               }
               strEtiquetaDirec= strEtiquetaDirec + "CORRESPONDENCIA";
            }      
            
            iLegal=0;
            iFacturacion=0;
            iEntrega=0;
            iComunicacion=0;
            iInstalacion=0;
            iOtra=0;
            iCorrespondencia=0;
            String strRegionName=null;
            String strDireccion="";
            strRegionName=objGeneralS.getRegionName(lRegionId);
            
            if (strAddress1!=null)
               strDireccion= strDireccion +strAddress1 + " ";
                        
            if (strAddress2!=null)
               strDireccion= strDireccion +strAddress2 + " ";
                        
            if (strAddress3!=null)
               strDireccion= strDireccion +strAddress3 + " ";
            
         %>
         <table border="0" cellspacing="1" cellpadding="0" align="center" width="99%">
            <tr>
               <td width="16%" class="CellLabel">&nbsp;Tipo&nbsp;de&nbsp;Dirección&nbsp;</td>
               <td colspan="5" class="CellContent">&nbsp;<%=strEtiquetaDirec%>&nbsp;</td>
            </tr>
            <tr>
               <td width="16%" class="CellLabel">&nbsp;Dirección&nbsp;</td>
               <td width="24%" class="CellContent">&nbsp;<%=strDireccion%></td>
               <td colspand="3" width="8%" class="CellLabel">&nbsp;Región&nbsp;</td>
               <td class="CellContent" width="18%">&nbsp;<%=strRegionName%>&nbsp;</td>
               <td colspand="3" width="12%" class="CellLabel">&nbsp;Codigo&nbsp;Postal&nbsp;</td>
               <td class="CellContent" width="22%">&nbsp;<%=strZip%>&nbsp;</td>
            </tr>
            <tr>
               <td width="16%" class="CellLabel">&nbsp;Departamento&nbsp;</td>
               <td width="24%" class="CellContent">&nbsp;<%=strDepartmento%>&nbsp;</td>
               <td colspand="3" width="8%" class="CellLabel">&nbsp;Provincia&nbsp;</td>
               <td class="CellContent" width="18%">&nbsp;<%=strProvincia%>&nbsp;</td>
               <td colspand="3" width="12%" class="CellLabel">&nbsp;Distrito&nbsp;</td>
               <td class="CellContent" width="22%">&nbsp;<%=strDistrito%>&nbsp;</td>
            </tr>
         </table>
         <table border="0" cellspacing="1" cellpadding="0" align="center" width="99%">
            <tr>
               <td>&nbsp;&nbsp;</td>               
            </tr>
         </table>
      <%}%>
         <table width="99%">
            <tr>
               <td colspan="4" class="CellContent" align="center">
                  <input type="button" name="btnClose" value="   Cerrar   " onclick="javascript:parent.close();">
               </td>
            </tr>
         </table>
         <form name="formAddress" target="bottomFrame">
            <input type="hidden" name="myaction" >
            <input type="hidden" name="hdnCustomerId" value="<%=lCustomerId%>">         
            <input type="hidden" name="hdnSiteId" value="<%=lSiteId%>" >                          
            <input type="hidden" name="hdnRegionId" >       
            <input type="hidden" name="hdnObjectType" >       
            <input type="hidden" name="hdnAddressId" >       
            <input type="hidden" name="hdnOrderId" value="<%=lOrderId%>">
			<input type="hidden" name="hdnSpecificationId" value="<%=strSpecificationId%>"> <!-- CEM COR0354 -->
         </form>
<%
}
%>
         </TD>
      </TR>
      </TABLE>
   </TD>
</TR>
</TABLE>
</html>
<%}catch(Exception ex){
  System.out.println("Error try catch-->"+MiUtil.getMessageClean(ex.getMessage())); 
%>
<script>
  alert("<%=MiUtil.getMessageClean(ex.getMessage())%>");
</script>
<%
}%>