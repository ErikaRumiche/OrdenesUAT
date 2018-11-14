<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.util.*" %>
<%@ page import="java.util.*,java.text.SimpleDateFormat"%>
<%@ page import="pe.com.nextel.service.*" %>
<%@ page import="pe.com.nextel.bean.*" %>
<%@ page import="pe.com.nextel.service.GeneralService" %>
<%
   long lOrderId=(request.getParameter("an_nporderid")==null?0:MiUtil.parseLong(request.getParameter("an_nporderid")));
   String strSessionId=(request.getParameter("hdnSession")==null?"":request.getParameter("hdnSession"));
   
   String strLogin="DTEODOSIO";
   String strUser="DAVID TEODOSIO"; 
   int iAppId=17;   
   
   PortalSessionBean objPortalSesBean = (PortalSessionBean)SessionService.getUserSession(strSessionId);
   strLogin=objPortalSesBean.getLogin();
   strUser=objPortalSesBean.getNom_user();
   iAppId=objPortalSesBean.getAppId();
   
   EditOrderService objOrderService=new EditOrderService();      
   OrderBean objOrderBean=null;   
   String strMessage=null;
   HashMap hshOrder=null;
   
   hshOrder=(HashMap)objOrderService.getOrder(lOrderId);
   strMessage=(String)hshOrder.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);   
   
   objOrderBean=(OrderBean)hshOrder.get("objResume"); 
   
   /*FNAJARRO : Obtiene ruta del servidor para los formatos relacionados con la categoria de la orden */
   GeneralService objGenServ = new GeneralService();
   HashMap objHashMap=objGenServ.getNameServerReport(Constante.FORM,Constante.URL_SERVER_SERVLET);
   strMessage=(String)objHashMap.get("strMessage");
   if (strMessage!=null)
      throw new Exception(strMessage);
   String strNameServerReport=(String)objHashMap.get("strNameServerReport");
   
   
   System.out.println("---------------------- INICIO ValidatePrePaId.jsp------------------");
   System.out.println("lOrderId-->"+lOrderId);   
   System.out.println("MiUtil.getString(objOrderBean.getNpType())-->"+MiUtil.getString(objOrderBean.getNpType()));   
   System.out.println("MiUtil.getString(objOrderBean.getNpSpecification())-->"+MiUtil.getString(objOrderBean.getNpSpecification()));   
   System.out.println("getNpSpecificationId-->"+objOrderBean.getNpSpecificationId());
   System.out.println("iAppId-->"+iAppId);     
   System.out.println("strSessionId-->"+strSessionId);  
   System.out.println("Nombre del Servidor: "+strNameServerReport); 
   System.out.println("---------------------- FIN ValidatePrePaId.jsp------------------");      
%>
<html>
<head>
<title>FORMATOS</title>
</head>
<link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css">
<script language="javaScript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsIncidentEdit.js"></script>
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsIncidentException.js"></script>
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>

<script language="Javascript">
   function fxVerExcel(pedidoId,category,subCategory){
      var rdoOpcion = document.formdatos.rdoTipoPlan;
      var wv_formulario = (rdoOpcion[0].checked?rdoOpcion[0].value:rdoOpcion[1].value);      
      var url = "<%=strNameServerReport%>an_orderid="+pedidoId+"&av_type="+escape2(category)+"&an_npspecificationid=<%=objOrderBean.getNpSpecificationId()%>&av_specification="+escape2(subCategory)+"&av_login=<%=strLogin%>"+"&av_formulario="+wv_formulario+"&av_nom_user="+escape2('<%=strUser%>')+"&an_appid=<%=iAppId%>";
      var winXls = window.open(url, winXls,"toolbar=yes,location=0,directories=no,status=yes,menubar=yes,scrollbars=no,resizable=yes,screenX=100,top=80,left=100,screenY=80,width=800,height=550,modal=yes");
      self.close();
   }
</script>

<body class="CellContent">
   <form method="get" name="formdatos">
   <br>
      <table border="0" class="PortletHeaderColor" cellspacing="0" cellpadding="0" width="100%">
         <tr class="PortletHeaderColor">
            <td class="LeftCurve" valign="top" align="left" width="10" >&nbsp;&nbsp;</td>
            <td class="PortletHeaderColor"align="left" valign="top"> <font class="PortletHeaderText">Orden <%=lOrderId%> </font></td>
            <td align="right" class="RightCurve" width="10" >&nbsp;&nbsp;</td>
         </tr>
      </table>
      
      <table border="1" width="100%" cellpadding="0" cellspacing="0" class="regionborder">
         <tr valign="top">
            <td class="RegionHeaderColor" height="97">
               <br>
               <table border="0" cellspacing="0" cellpadding="0" width="97%" align="center">
                  <tr>
                     <td class="SectionTitleLeftCurve" width="10">&nbsp;&nbsp;</td>
                     <td class="SectionTitle">&nbsp;&nbsp;Seleccionar&nbsp;Tipo&nbsp;de&nbsp;Plan&nbsp;Tarifario</td>
                     <td class="SectionTitleRightCurve" width="8">&nbsp;&nbsp;</td>
                  </tr>
               </table>
            
               <table border="0" cellspacing="2" cellpadding="0" width="98%" align="center" class="BannerSecondaryLink">
                  <tr><td align="LEFT" class="CellContent">&nbsp;<input type="radio" name="rdoTipoPlan" value="PostPago" checked> PostPago</td></tr>
                  <tr><td align="LEFT" class="CellContent">&nbsp;<input type="radio" name="rdoTipoPlan" value="PrePago"> PrePago</td></tr>
               </table>
               <br>
            </td>
         </tr>
      </table>
      <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
         <tr align="center">
            <td colspan="3">
            <br>
            <input type="button" name="cmdSave" value="Aceptar" onclick="fxVerExcel('<%=lOrderId%>','<%=MiUtil.getString(objOrderBean.getNpType())%>','<%=MiUtil.getString(objOrderBean.getNpSpecification())%>')">&nbsp;
            </td>
         </tr>
      </table>
   </form>
</body>
</html>