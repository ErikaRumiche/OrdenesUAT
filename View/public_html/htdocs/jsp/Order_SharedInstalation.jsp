<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="pe.com.nextel.service.*" %>
<%@page import="pe.com.nextel.util.*" %>
<%@page import="java.util.*" %>
<%@page import="oracle.portal.provider.v2.render.PortletRenderRequest" %>
<%@page import="oracle.portal.provider.v2.http.HttpCommonConstants" %>
<%@page import="oracle.portal.provider.v2.ProviderSession" %>
<%@page import="oracle.portal.provider.v2.url.UrlUtils" %>
<%@page import="oracle.portal.provider.v2.render.http.HttpPortletRendererUtil" %>
<%@page import="pe.com.nextel.dao.*,pe.com.nextel.bean.*" %>
<%@page import="pe.com.nextel.util.Constante" %>

<%
/*
Recuperando Parametros de Input
*/
  
  Hashtable hshtinputNewSection = null;
  GeneralService  objGeneralService = new GeneralService();
  String    strCodigoCliente= "",
            strnpSite ="",
            strCodBSCS = "",
            hdnSpecification = "";
            //strSolution = "";
            
  hshtinputNewSection     = (Hashtable)request.getAttribute("hshtInputNewSection");
  
  if ( hshtinputNewSection != null ) {
    strCodigoCliente        =   (String)hshtinputNewSection.get("strCodigoCliente");
    strnpSite               =   (String)hshtinputNewSection.get("strnpSite");
    strCodBSCS              =   (String)hshtinputNewSection.get("strCodBSCS");
    hdnSpecification        =   (String)hshtinputNewSection.get("hdnSpecification");
    //strSolution             =   (String)hshtinputNewSection.get("strSolution");
    request.setAttribute("hshtInputNewSection",hshtinputNewSection);
  }
  
  String nameHtmlItem = (String)request.getParameter("nameObjectHtml");  
  String strMessage = "";  
  String URL_Order_SharedInstalation     = "htdocs/jsp/Order_SharedInstalation.jsp";
  ArrayList arrLista=new ArrayList();
  GeneralService objGeneralS=new GeneralService();   
  String strURLOrderServlet =request.getContextPath()+"/editordersevlet";    
  ArrayList valoresCombo = new ArrayList();
	HashMap hshDataMap = new HashMap();

%>
      <link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"/>      
      <script language="JavaScript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/date-picker.js"></script>                        
      <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
      <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>         
      <script language="JavaScript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/DateTimeBasicOperations.js"></script>            
      <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/FunctionsOrderIXEdit.js"></script>   
<script>
  function fxLoadPlace(depId,provId,tipo) {                 
        Form = document.formdatos;                 
        if (tipo==1 && depId==""){ //  0: Carga Departamentos 1: Carga Provincia  2: Carga Distrito                                                 
             //deleteOptionIE(document.formdatos.cmbDist); 
             //deleteOptionIE(document.formdatos.cmbProv);             
             Form.txtZip.value="";
        }else if (tipo==2 && (provId=="" || provId=="0")){
             //deleteOptionIE(document.formdatos.cmbDist); 
             Form.txtZip.value="";
        }else{                      
            Form.myaction.value="LoadUbigeo";
            //deleteOptionIE(document.formdatos.cmbDist); 
            var url = "<%=strURLOrderServlet%>"+"?sDepId="+depId+"&sProvId="+provId+"&nTipo="+tipo+"&myaction=LoadUbigeo"; 
            parent.bottomFrame.location.replace(url);            
         }
      }
      
    function fxFillProvincias() {
        vForm = document.frmdatos;
        departamento = vForm.cmbDepartamento.value;
        if(departamento!="") {
            distrito = '00';
            var url = "<%=request.getContextPath()%>/retailServlet?hdnMethod=loadUbigeo&cmbDepartamento="+departamento+"&cmbDistrito="+distrito+"&jerarquia=<%=Constante.JERARQUIA_PROVINCIA%>";
            parent.bottomFrame.location.replace(url);
        }
        else {
            DeleteSelectOptions(document.frmdatos.cmbProvincia);
        }
    }
      
    function fxFillDistritos() {
        vForm = document.frmdatos;
        departamento = vForm.cmbDepartamento.value;
        provincia = vForm.cmbProvincia.value;
        if(provincia!="") {
            var url = "<%=request.getContextPath()%>/retailServlet?hdnMethod=loadUbigeo&cmbDepartamento="+departamento+"&cmbProvincia="+provincia+"&jerarquia=<%=Constante.JERARQUIA_DISTRITO%>";
            parent.bottomFrame.location.replace(url);
        }
        else {
            DeleteSelectOptions(document.frmdatos.cmbDistrito);
        }
    }
      function fxValidate(){
         vForm = document.frmdatos;
         vForm.hdnDepartamento.value= vForm.cmbDepartamento.options[vForm.cmbDepartamento.options.selectedIndex].text;
         vForm.hdnProvincia.value   = vForm.cmbProvincia.options[vForm.cmbProvincia.options.selectedIndex].text
         vForm.hdnDistrito.value    = vForm.cmbDistrito.options[vForm.cmbDistrito.options.selectedIndex].text
         vForm.submit(); 
      }
</script>
<!--Cabecera de la pagina: inicio-->
      <html>
      <head>
      <title>Búsqueda de Instalaciones Compartidas</title>
      </head>
      <body class="CellContent">
         <!--Contenido CustomerNew : inicio-->
      <form name="frmdatos" action="Order_SharedInstalationLista.jsp" method="POST">
        <input type="hidden" name="hdnDepartamento" value="">
        <input type="hidden" name="hdnProvincia" value="">
        <input type="hidden" name="hdnDistrito" value="">
        
         <input type="hidden" name="myaction"/>                 
         <br>
         <table border="0" class="PortletHeaderColor" cellspacing="0" cellpadding="0" width="100%">
         <tr class="PortletHeaderColor">
            <td class="LeftCurve" valign="top" align="left" width="10" >&nbsp;&nbsp;</td>
            <td class="PortletHeaderColor"align="LEFT" valign="top"> <font class="PortletHeaderText">Instalaciones Compartidas &gt; Buscar</font></td>
            <td align="right" class="RightCurve" width="10" >&nbsp;&nbsp;</td>
         </tr>
         </table>
         <table border="0" cellpadding="0" cellspacing="0"><tr><td height="2"></td></tr></table>

         <table border="1" width="100%" cellpadding="0" cellspacing="0" align="center" class="RegionBorder">
            <tr valign="top">
               <td class="RegionHeaderColor">
                  <table border="0" cellpadding="0" cellspacing="0"><tr><td height="5"></td></tr></table>
                  <table border="0" cellspacing="0" cellpadding="0" width="99%" align="center">
                  <tr>
                     <td class="SectionTitleLeftCurve" width="10">&nbsp;&nbsp;</td>
                     <td class="SectionTitle">&nbsp;&nbsp;Criterios de b&uacute;squeda</td>
                     <td class="SectionTitleRightCurve" width="8">&nbsp;&nbsp;</td>
                  </tr>
                  </table>
                  <table border="0" cellspacing="2" cellpadding="0" width="99%" align="center" class="BannerSecondaryLink">
                  <tr>
                     <td class="CellLabel" align="left" width="45%">&nbsp;ID&nbsp;Instalación Compartida&nbsp;</td>
                     <td class="CellContent" colspan="3">&nbsp;<input type="text" name="txtSharedInstalationId" size="10" value=""></td>
                  </tr>
                  <tr>
                     <td class="CellLabel" align="left">&nbsp;Nombre de Instalación Compartida</td>
                     <td class="CellContent" colspan="3">&nbsp;<input type="text" name="txtSharedInstalationName" size="45" value=""></td>
                  </tr>
                  <tr>
                     <td class="CellLabel" align="left">&nbsp;Dirección de Instalación Compartida</td>
                     <td class="CellContent" colspan="3">&nbsp;<input type="text" name="txtSharedInstalationAddress" size="45" value=""></td>
                  </tr>
                  <tr>
                     <td class="CellLabel" align="left">&nbsp;Departamento</td>
                     <td class="CellContent" width="30%">&nbsp;
                        <select name="cmbDepartamento" onchange="fxFillProvincias()">
                          <option value=""></option>
                          <%    
                                hshDataMap = objGeneralService.getDominioList("RETAIL_DEPARTAMENTOS");
								valoresCombo = (ArrayList) hshDataMap.get("arrDominioList");
                                strMessage = (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
                                if (strMessage==null) {
                                    for(int i=0;i<valoresCombo.size();i++) {
                                        DominioBean dominio = (DominioBean) valoresCombo.get(i);
                            %>
                          <option value='<%=dominio.getValor()%>'>
                            <%=dominio.getDescripcion()%>
                          </option>
                          <%        }
                                }
								else{
							%>
								<script>alert("[cmbDepartamento]: <%=strMessage%>");</script>
							<%
								}
                           %>
                      </select>

                     </td>     
                  </tr>
                  <tr>
                     <td class="CellLabel" align="left">&nbsp;Provincia</td>
                     <td class="CellContent" colspan="3">&nbsp;
                      <select name="cmbProvincia" onchange="fxFillDistritos()">
                        <option value=""></option>
                      </select>
                     </td>
                  </tr>
                  <tr>
                     <td class="CellLabel" align="left">&nbsp;Distrito</td>
                     <td class="CellContent" colspan="3">&nbsp;
                      <select name="cmbDistrito">
                        <option value=""></option>
                      </select>
                     </td>
                  </tr>
                  </table>
               </td>
            </tr>
         </table>
         <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
         <tr align="center">
            <td colspan="3">
               <br>
               <input type="button" name="btnSave" value="Buscar" onclick="javascript:fxValidate();">
               <input type="button" name="btnCancel" value="Cancelar" onclick="javascript:parent.close();">
            </td>
         </tr>
         </table>
         </form>
      </body>
      </html>