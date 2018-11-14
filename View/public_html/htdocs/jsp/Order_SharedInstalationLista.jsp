<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="pe.com.nextel.service.GeneralService"%>
<%@ page import="pe.com.nextel.bean.AddressObjectBean"%>
<%@ page import="pe.com.nextel.util.Constante" %>
<%@ page import="pe.com.nextel.util.MiUtil" %>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.HashMap"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net/"%>
<%
  AddressObjectBean objAddressObjB = new AddressObjectBean();
  String strHdnNumPagina    = request.getParameter("hdnNumPagina")==null?"1":request.getParameter("hdnNumPagina");
	long lPageSelected        = Long.parseLong(strHdnNumPagina);
	String strHdnNumRegistros = request.getParameter("hdnNumRegistros")==null?"15":request.getParameter("hdnNumRegistros");
	int iRowsByPage           = Integer.parseInt(strHdnNumRegistros);
  int lNumTotalRegistros    = 0;
  objAddressObjB.setNpnumpage(MiUtil.parseLong(strHdnNumPagina));
  objAddressObjB.setNpnumregisters(MiUtil.parseLong(strHdnNumRegistros));

%>

 <html>
  <head>
   <title>Listado de Direcciones</title>
    <link rel="stylesheet" type="text/css" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
    <link rel="stylesheet" type="text/css" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/displaytag.css"></link>

    <script language="javascript">
       parent.document.title = "Portal Nextel";
       window.focus();

       function fxCloseWindow(){
          parent.close();
       }

       function fxSharedInstalElec(addressId,sharedName,sharedAddress,department,province,district) {
          form = parent.opener.parent.mainFrame.document.frmdatos;
          form.txt_ItemSharedInstall.value = addressId;
          form.txt_ItemSharedInstallView.value = sharedName+" - "+sharedAddress+" " +district+" "+province+" "+department;
          parent.close();
       }
    </script>
 </head>
 
 <body>
  <%
    //1.- Lectura de parametros
    String sDepartamento               = null;
    String sProvincia                  = null;
    String sDistrito                   = null;
    int    intSharedInstalationId      = 0;
    String strSharedInstalationId      = null;
    String strSharedInstalationName    = null;
    String strSharedInstalationAddress = null;

    String hdnDepartamento             = null;
    String hdnProvincia                = null;
    String hdnDistrito                 = null;

    String strCriterio = "";
    
    // Recupera los criterios propios de la busqueda
    
    strSharedInstalationId      = MiUtil.getString((String)request.getParameter("txtSharedInstalationId"));
    strSharedInstalationName    = MiUtil.getString((String)request.getParameter("txtSharedInstalationName"));
    strSharedInstalationAddress = MiUtil.getString((String)request.getParameter("txtSharedInstalationAddress"));
    sDepartamento               = MiUtil.getString((String)request.getParameter("cmbDepartamento"));
    sProvincia                  = MiUtil.getString((String)request.getParameter("cmbProvincia"));
    sDistrito                   = MiUtil.getString((String)request.getParameter("cmbDistrito"));
    hdnDepartamento             = MiUtil.getString((String)request.getParameter("hdnDepartamento"));
    hdnProvincia                = MiUtil.getString((String)request.getParameter("hdnProvincia"));
    hdnDistrito                 = MiUtil.getString((String)request.getParameter("hdnDistrito"));
    
    //2.- Visualizando el criterio de busqueda
    if(sDepartamento  != null) strCriterio = strCriterio + "<font class='CriteriaLabel'>&nbsp;Departamento: </font><font class='CellContent'>"  + hdnDepartamento + "</font>";
    if(sProvincia     != null) strCriterio = strCriterio + "<font class='CriteriaLabel'>&nbsp;Provincia: </font><font class='CellContent'>"     + hdnProvincia + "</font>";
    if(sDistrito      != null) strCriterio = strCriterio + "<font class='CriteriaLabel'>&nbsp;Distrito: </font><font class='CellContent'>"      + hdnDistrito + "</font>";
    
    if(intSharedInstalationId != 0 )   strCriterio = strCriterio + "<br><font class='CriteriaLabel'>&nbsp;ID Instalación Compartida : </font><font class='CellContent'>" + intSharedInstalationId + "</font>";      
    if(strSharedInstalationName != null   && ! strSharedInstalationName.equals("") )  strCriterio = strCriterio + "<br><font class='CriteriaLabel'>&nbsp;Nombre de Instalación Compartida: </font><font class='CellContent'>" + strSharedInstalationName + "</font>";
    if(strSharedInstalationAddress != null   && ! strSharedInstalationAddress.equals("") )  strCriterio = strCriterio + "<br><font class='CriteriaLabel'>&nbsp;Dirección de Instalación Compartida: </font><font class='CellContent'>" + strSharedInstalationAddress + "</font>";
    
    objAddressObjB.setAddressId(MiUtil.parseInt(strSharedInstalationId));
    objAddressObjB.setSwaddress1(strSharedInstalationName);
    objAddressObjB.setSwaddress2(strSharedInstalationAddress);
    objAddressObjB.setNpdepartmentname(sDepartamento);
    objAddressObjB.setNpprovincename(sProvincia);
    objAddressObjB.setNpdistrictname(sDistrito);
      
  %>    
  <table border="0" width="100%" cellpadding="0" cellspacing="1" class="RegionBorder">
     <form name="frmdatoslist" id="frmdatoslist" method="post" target="mainFrame">
        <tr>
        <table border="0" class="PortletHeaderColor" cellspacing="0" cellpadding="0" width="100%">
           <tr class="PortletHeaderColor">
              <td class="LeftCurve" valign="top" align="left" width="10">&nbsp;&nbsp;</td>
              <td class="PortletHeaderColor" align="left" valign="top">
                 <font class="PortletHeaderText">Listado de Direcciones</font></td>
              <td align="right" class="RightCurve" width="10%" >&nbsp;&nbsp;</td>
           </tr>
        </table>
        </tr>
        <tr>
          <table border="1" width="100%" cellpadding="2" cellspacing="0" class="RegionBorder" align="center" >
            <tr>
               <td width="100%"class="RegionHeaderColor">
               <!--Inicio Linea de separación -->
                <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center" height="5">
                  <tr align="center"><td></td></tr>
                </table>
               <!--Fin de Linea de separación -->
                  
              <!-- Criterios de Búsqueda -->
              <table border="0" cellspacing="0" cellpadding="0" align="center" width="100%">
                <tr bgcolor="#F5F5EB">
                    <td valign="top">
                       <font class="CriteriaLabel">&nbsp;Nombres criterios :</font><br>
                       <font class="CellContent"><%=strCriterio%></font>
                     </td>
                     <td align="right" width="18%" valign="top">
                              <a href="javascript:fxBackSharedInstalation();"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/binocular.gif" border="0" width="88" height="24" alt="Otra B&uacute;squeda"></a>
                     </td>
                   </tr>
                 </table>
    
                 <!--Inicio Linea de separación -->
                 <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center" height="5">
                        <tr align="center"><td></td></tr>
                 </table>
                 <!--Fin de Linea de separación -->
                 <% 
                    GeneralService objGeneralS2         = new GeneralService();   
                    ArrayList      arrLista2            = new ArrayList(); 
                    HashMap        objHshSharedInstall  = new HashMap(); 
                    String         strMessage2           = null;  
                    
                    objHshSharedInstall = objGeneralS2.getSharedInstalation(objAddressObjB);
                    lNumTotalRegistros  = MiUtil.parseInt((String)objHshSharedInstall.get("numTotalRegisters")); 
                    arrLista2           = (ArrayList)objHshSharedInstall.get("objSharedInstallList"); 
                    request.setAttribute("arrSharedInstallList",arrLista2);
                    strMessage2   = (String)objHshSharedInstall.get("strMessage");
                 %>
                 <display:table id="orders" name="arrSharedInstallList" class="orders" sort="external" partialList="true" size="<%=new Integer(lNumTotalRegistros)%>" requestURI="" pagesize="<%=Integer.parseInt(strHdnNumRegistros)%>" style="width: 100%">
                   <display:setProperty name="paging.banner.some_items_found"><span class="spanPaging"> {0} {1} encontrados, mostrando <%=(((lPageSelected-1)*iRowsByPage)+1)+" de "+((lPageSelected*iRowsByPage)>lNumTotalRegistros?lNumTotalRegistros:(lPageSelected*iRowsByPage))%>. </span></display:setProperty>
                   
                   <display:column title="Nro" style="white-space: nowrap;" media="html" >
                   <a href="javascript:fxSharedInstalElec(<%=((HashMap) orders).get("SharedInstallId")%>,'<%=((HashMap) orders).get("SharedInstallName")%>','<%=((HashMap) orders).get("Address")%>','<%=((HashMap) orders).get("Departamento")%>','<%=((HashMap) orders).get("Provincia")%>','<%=((HashMap) orders).get("Distrito")%>')">
                    <img src="<%=Constante.PATH_APPORDER_SERVER%>/images/viewDetail.gif" width="15" height="15" border="0">
                   </a>
                   </display:column>
                   
                   <display:column property="SharedInstallName" title="Nombre Instalación"/>
                   <display:column property="Address" title="Dirección"/>
                   <display:column property="Departamento" title="Departamento"/>
                   <display:column property="Provincia" title="Provincia"/>
                   <display:column property="Distrito" title="Distrito"/>
                 </display:table>
                     
                 <br/>

                 <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
                   <tr align="center">
                     <td width="45%">&nbsp;</td>                 	
                     <td width="10%">                 	
                        <input type="button" name="btnCerrar" value="Cerrar" onclick="javascript:fxCloseWindow();">
                     </td>
                     <td width="45%">&nbsp;</td>
                   </tr>
                 </table>
                  </td>
                </tr>
            	</table>
            </tr> 	
         </table>
       </form>
     </body>
   </html>
<script type="text/javascript">
    
	function paginar(url) {
    //alert("Hola")
    parametros ="";
    <%if(!strSharedInstalationId.equals("")){%>
      parametros += "&txtSharedInstalationId=<%=strSharedInstalationId%>";
    <%}%>
    <%if(!strSharedInstalationName.equals("")){%>
      parametros += "&txtSharedInstalationName=<%=strSharedInstalationName%>";
    <%}%>
    <%if(!strSharedInstalationAddress.equals("")){%>
      parametros += "&txtSharedInstalationAddress=<%=strSharedInstalationAddress%>";
    <%}%>
    <%if(!sDepartamento.equals("")){%>
      parametros += "&cmbDepartamento=<%=sDepartamento%>";
    <%}%>
    <%if(!sProvincia.equals("")){%>
      parametros += "&cmbProvincia=<%=sProvincia%>";
    <%}%>
    <%if(!sDistrito.equals("")){%>
      parametros += "&cmbDistrito=<%=sDistrito%>";
    <%}%>
    <%if(!hdnDepartamento.equals("")){%>
      parametros += "&hdnDepartamento=<%=hdnDepartamento%>";
    <%}%>
    <%if(!hdnProvincia.equals("")){%>
      parametros += "&hdnProvincia=<%=hdnProvincia%>";
    <%}%>
    <%if(!hdnDistrito.equals("")){%>
      parametros += "&hdnDistrito=<%=hdnDistrito%>";
    <%}%>
    
		cadena = "<%=request.getContextPath()%>/htdocs/jsp/Order_SharedInstalationLista.jsp?"+fxObtenerParametrosDisplayTag(url)+fxGetPageNumber(url);
		cadena += parametros;
    //alert(cadena);
    document.location.href = cadena;
	}

	function fxObtenerParametrosDisplayTag(url) {
		var indice = url.indexOf('d-');
		if (indice != -1) {
			url = url.substring(indice);
			indice2 = url.indexOf('&');
			if(indice2 > 0) {
				paramDisplayTag = url.substr(0,indice2);
			}
			else {
				paramDisplayTag = url;
			}
		}
		return paramDisplayTag;
	}

	function fxGetPageNumber(url) {
		var indice = url.indexOf("d-");
		if (indice != -1) {
			url = url.substring(indice);
			indice2 = url.indexOf("&");
			if(indice2 > 0) {
				paramDisplayTag = url.substr(0,indice2);
			} else {
				paramDisplayTag = url;
			}
			indice3 = url.indexOf("=");
			if(indice3 > 0) {
				pageNumber = paramDisplayTag.substr(indice3+1);
			} else {
				pageNumber = 0;
			}
		}
		return "&hdnNumPagina="+pageNumber;
	}
	
	function fxGoToOtherSearch() {
    vForm = document.frmdatos;
    vForm.hdnMethod.value = "showSearch";
    vForm.submit();
	}
  
  function fxBackSharedInstalation(){
    window.location.href = '<%=request.getContextPath()%>/htdocs/jsp/Order_SharedInstalation.jsp';
  }
	
</script>