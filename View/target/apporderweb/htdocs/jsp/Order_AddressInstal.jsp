<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.*"%>
<%@ page import="pe.com.nextel.util.MiUtil"%>
<%@ page import="pe.com.nextel.util.Constante"%>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<%
  String     strCodigoCliente= "",
             strnpSite ="",
             strCodBSCS = "",
             hdnSpecification = "",
             strTypeCompany = "",
             //strSolution = "",
             strSiteOppId = "",
             strUnknwnSiteId = "";

    strCodigoCliente        =   (String)request.getParameter("strCustomerId");
    strnpSite               =   (String)request.getParameter("strSiteId");
    strCodBSCS              =   (String)request.getParameter("strCodBSCS");
    hdnSpecification        =   (String)request.getParameter("strSpecificationId");
    //strSolution             =   (String)request.getParameter("strSolution");
    strSiteOppId            =   (String)request.getParameter("strSiteOppId");
    strUnknwnSiteId         =   (String)request.getParameter("strUnknwnSiteId");

    //1.- Lectura de parametros
    int     iswObjectId=0;
    String  sTipoDirCustomer="60";
    String  sSwObjectType=null;
    
    // JOYOLA, 05/03/2008 se cambio el orden de los if ya que no evaluaba el siteid
    System.out.println("5. Order_AddressInstal.jsp.strnpSite ===>"+strnpSite);
    System.out.println("5. Order_AddressInstal.jsp.strSiteOppId ===>"+ strSiteOppId);
    System.out.println("5. Order_AddressInstal.jsp.strUnknwnSiteId ===>"+ strUnknwnSiteId);
    System.out.println("5. Order_AddressInstal.jsp.strCodigoCliente ===>"+ strCodigoCliente);
    
    if( strUnknwnSiteId != null  && (!strUnknwnSiteId.equals("0")) && (!strUnknwnSiteId.equals(""))  ){
      iswObjectId =  MiUtil.parseInt(strUnknwnSiteId);
      sSwObjectType="SITE";
    }else if( strSiteOppId != null  && (!strSiteOppId.equals("0")) && (!strSiteOppId.equals("")) ){
    //if( strnpSite != null  && (!strnpSite.equals("")) ){
      //iswObjectId =  MiUtil.parseInt(strnpSite);
      iswObjectId =  MiUtil.parseInt(strSiteOppId);
      sSwObjectType="SITE";
    }else if( strCodigoCliente != null && (!strCodigoCliente.equals("")) ){
      iswObjectId =  MiUtil.parseInt(strCodigoCliente);
      sSwObjectType="CUSTOMER";
    } 
%>
      <html>
      <head>
      <title>Direcciones de Instalación</title>
      <link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css">
      <script language="Javascript">   
         function fxAddressInstalElec(addressId,sharedName,sharedAddress,department,province,district) {
            form = parent.opener.parent.mainFrame.document.frmdatos;
            form.txt_ItemAddressInstall.value = addressId;
            form.txt_ItemAddressInstallView.value = sharedName+" - "+sharedAddress+" " +district+" "+province+" "+department;
            //Invocar al Servlet para cargar la data de Traslado
            //var url = "<%=request.getContextPath()%>/itemServlet?hdnMethod=getServiceList&strAddressId="+addressId+"&strCustomerId=<%=strCodigoCliente%>";
            //parent.bottomFrame.location.replace(url);
            parent.close();
         }
      </script>
      </head>
      <body>
         <!--                                    -->
      <form name="frmdatos" action="<%=request.getContextPath()%>/retailServlet" method="POST">
         <input type="hidden" name="myaction"/>                 
         <br>
         <table border="0" class="PortletHeaderColor" cellspacing="0" cellpadding="0" width="100%">
         <tr class="PortletHeaderColor">
            <td class="LeftCurve" valign="top" align="left" width="10" >&nbsp;&nbsp;</td>
            <td class="PortletHeaderColor"align="LEFT" valign="top"> <font class="PortletHeaderText">Listado de Direcciones</font></td>
            <td align="right" class="RightCurve" width="10" >&nbsp;&nbsp;</td>
         </tr>
         </table>
         <table border="0" cellpadding="0" cellspacing="0"><tr><td height="2"></td></tr></table>
            <table border="0" width="100%" cellpadding="0" cellspacing="1" class="RegionBorder" align="center">
                 <!-- Listado Resumen de Servicios -->
                 <tr>
                    <td align="left" class="CellLabel"><b>&nbsp;&nbsp;</b></td>
                    <!--<td align="left" class="CellLabel"><b>&nbsp;Nombre Instalación</b></td>-->
                    <td align="left" class="CellLabel"><b>&nbsp;Dirección&nbsp;</b></td>
                    <td align="left" class="CellLabel"><b>&nbsp;Departamento&nbsp;</b></td>
                    <td align="left" class="CellLabel"><b>&nbsp;Provincia&nbsp;</b></td>
                    <td align="left" class="CellLabel"><b>&nbsp;Distrito&nbsp;</b></td>
                 </tr>
             <% 
                GeneralService objGeneralS=new GeneralService();   
                ArrayList arrLista=new ArrayList(); 
                HashMap h = new HashMap(); 
                String strMessage = "";  
                h = objGeneralS.getAddress(iswObjectId,sTipoDirCustomer,sSwObjectType,MiUtil.parseLong(hdnSpecification));
                arrLista=(ArrayList)h.get("objAddressList"); 
                strMessage= (String)h.get("strMessage");
                if (strMessage==null){

                   for (int i=0;i<arrLista.size();i++){
                      HashMap hshMap;
                      hshMap=(HashMap)arrLista.get(i);
                         String strddressid= (String)hshMap.get("swaddressid");  
                         String strAddress1= (String)hshMap.get("swaddress1");
                         String strAddress2= (String)hshMap.get("swaddress2");           
                         if (strAddress2==null) strAddress2="";
                         String strDepartamento= (String)hshMap.get("Departamento");  
                         String strProvincia= (String)hshMap.get("Provincia");
                         String strDistrito= (String)hshMap.get("Distrito");
                  %>
                    <tr>
                       <td align="left" class="CellContent">&nbsp;
                       <a href="javascript:fxAddressInstalElec(<%=strddressid%>,'<%=MiUtil.getString(strAddress1)%>','<%=MiUtil.getString(strAddress2)%>','<%=MiUtil.getString(strDepartamento)%>','<%=MiUtil.getString(strProvincia)%>','<%=MiUtil.getString(strDistrito)%>')"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/viewDetail.gif" width="15" height="15" border="0"></a>
                       &nbsp;</td>
                       <!--<td align="left" class="CellContent"><%=strAddress1%></td>-->
                       <td align="left" class="CellContent">&nbsp;<%=strAddress1%></td>
                       <td align="left" class="CellContent">&nbsp;<%=strDepartamento%></td>
                       <td align="left" class="CellContent">&nbsp;<%=strProvincia%></td>
                       <td align="left" class="CellContent">&nbsp;<%=MiUtil.getString(strDistrito)%></td>
                    </tr>
                  <%
                   }
                 }
                  %>
             </table>
         <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
         <tr align="center">
            <td colspan="3">
               <br>
               <input type="button" name="btnSave" value="Cerrar" onclick="javascript:parent.close();">
            </td>
         </tr>
         </table>
         </form>
      </body>
      </html>