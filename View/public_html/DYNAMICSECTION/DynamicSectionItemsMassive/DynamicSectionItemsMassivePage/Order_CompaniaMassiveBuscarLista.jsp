<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="java.util.*" %>
<%@page import="pe.com.nextel.dao.*"%>
<%@page import="pe.com.nextel.bean.*"%>
<%@page import="pe.com.nextel.util.MiUtil"%>
<%@page import="pe.com.nextel.util.RequestHashMap"%>
<%@page import="pe.com.nextel.service.*"%>
<%@page import="pe.com.nextel.util.Constante" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/"%>
<%

    String strSessionCode = null;
    int intSessionLevel = 0;
    String hdnSessionLogin=(request.getParameter("hdnSessionLogin1")==null?"":request.getParameter("hdnSessionLogin1"));
    strSessionCode =(request.getParameter("hdnSessionCode1")==null?"":request.getParameter("hdnSessionCode1"));
    String hdnSessionLevel =(request.getParameter("hdnSessionLevel1")==null?"":request.getParameter("hdnSessionLevel1"));
    String hdnProGrpId =(request.getParameter("hdnProGrpId1")==null?"":request.getParameter("hdnProGrpId1"));
    int iUserId = MiUtil.parseInt((request.getParameter("hdnUserId")==null?"":request.getParameter("hdnUserId")));
    int iAppId = MiUtil.parseInt((request.getParameter("hdnAppId")==null?"":request.getParameter("hdnAppId")));

    int intProGrpId = MiUtil.parseInt(hdnProGrpId);
    intSessionLevel = MiUtil.parseInt(hdnSessionLevel);

    System.out.println("[Inicio - Order_CompaniaMassiveBuscarLista.jsp]");
    System.out.println("hdnSessionLevel: "+intSessionLevel);
    System.out.println("intProGrpId: "+intProGrpId);
    System.out.println("hdnSessionCode: "+strSessionCode);
    System.out.println("hdnSessionLogin: "+hdnSessionLogin);


    String strHdnNumPagina    = request.getParameter("hdnNumPagina")==null?"1":request.getParameter("hdnNumPagina");
    int lPageSelected        = Integer.parseInt(strHdnNumPagina);
    String strHdnNumRegistros = request.getParameter("hdnNumRegistros")==null?"15":request.getParameter("hdnNumRegistros");
    int iRowsByPage           = Integer.parseInt(strHdnNumRegistros);
    int lNumTotalRegistros    = 0;
    System.out.println("strHdnNumPagina: "+strHdnNumPagina);
    System.out.println("lPageSelected: "+lPageSelected);
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>ORDENES > BUSCAR COMPAÑIA</title>
    <link rel="stylesheet" type="text/css" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"></link>
    <link rel="stylesheet" type="text/css" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/displaytag.css"></link>
    <link rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css">
    <script language="JavaScript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
    <script language="javascript" DEFER>
        window.focus();

        function goToOtherSearch() {
            var strParam="";
            var strURL="";
            strParam="?hdnSessionLevel1="+"<%=intSessionLevel%>"+"&hdnSessionCode1="+"<%=strSessionCode%>"+"&hdnSessionLogin1="+"<%=hdnSessionLogin%>"+"&hdnProGrpId1="+"<%=intProGrpId%>";
            strURL="Order_CompaniaMassiveBuscar.jsp"+strParam
            window.location=strURL;
        }

        function fxSelectCompanyId(idCompany){
            //alert("idCompany==================="+idCompany);
            var url = "<%=request.getContextPath()%>/massiveorderServlet?myaction=buscarCliente&detallemyaction=id&hdnSessionLogin1="+'<%=hdnSessionLogin%>'+"&hdnCustId1="+idCompany;
            vDocForm  = parent.opener.parent.mainFrame.frmdatos;
            vDocForm.action = '<%=request.getContextPath()%>/massiveorderServlet';
            vDocForm.hdnCustId1.value  = idCompany;
            vDocForm.myaction.value = 'buscarCliente';
            vDocForm.detallemyaction.value = 'id';
            vDocForm.submit();
            parent.close();
        }

        function fxCloseWindow(){
            parent.close();
        }
    </script>

</head>
<body>
<%
    // Lista de compañias
    //--------------------

    //1.- Lectura de parametros

    String strTipoDocumento   = null;
    String strNumeroDocumento = null;
    String strRazonSocial     = null;
    String strNombreCliente   = null;
    String strTipoCliente     = null;
    int intCustomerId  = 0;
    int intRegionId    = 0;
    String strCriterio = "";

    int intNumeroMinimo = 0;
    int intNumeroMaximo = 0;
    int intTotalPaginas = 0;

    // Recupera los criterios propios de la busqueda
    if(request.getParameter("v_tip_documento") != null)  strTipoDocumento = request.getParameter("v_tip_documento");
    if(request.getParameter("v_num_documento") != null)  strNumeroDocumento = request.getParameter("v_num_documento");
    if(request.getParameter("v_raz_social") != null) strRazonSocial = request.getParameter("v_raz_social");
    if(request.getParameter("v_nom_customer") != null) strNombreCliente = request.getParameter("v_nom_customer");
    if(request.getParameter("v_tipo_person") != null) strTipoCliente = request.getParameter("v_tipo_person");
    if(request.getParameter("v_company_id") != null && ! request.getParameter("v_company_id").equals("") ) intCustomerId =  Integer.parseInt(request.getParameter("v_company_id"));
    if(request.getParameter("cmbRegion") != null && ! request.getParameter("cmbRegion").equals("") ) intRegionId = Integer.parseInt(request.getParameter("cmbRegion"));

    //2.- Visualizando el criterio de busqueda
    if(strTipoDocumento != null   && ! strTipoDocumento.equals("") )  strCriterio = strCriterio + "<font class='CriteriaLabel'>&nbsp;Tip.Doc.: </font><font class='CellContent'>" + strTipoDocumento + "</font>";
    if(strNumeroDocumento != null && ! strNumeroDocumento.equals("")) strCriterio = strCriterio + "<font class='CriteriaLabel'>&nbsp;Num.Doc.: </font><font class='CellContent'>" + strNumeroDocumento + "</font>";
    if(strRazonSocial != null     && ! strRazonSocial.equals(""))     strCriterio = strCriterio + "<font class='CriteriaLabel'>&nbsp;Raz&oacute;n Social: </font><font class='CellContent'>" + strRazonSocial + "</font>";
    if(strNombreCliente != null   && ! strNombreCliente.equals(""))   strCriterio = strCriterio + "<font class='CriteriaLabel'>&nbsp;Nombre Comercial: </font><font class='CellContent'>" + strNombreCliente + "</font>";
    if(strTipoCliente != null     && ! strTipoCliente.equals(""))    strCriterio = strCriterio + "<font class='CriteriaLabel'>&nbsp;Tipo de Persona: </font><font class='CellContent'>" + strTipoCliente + "</font>";

    if(intCustomerId != 0 )   strCriterio = strCriterio + "<font class='CriteriaLabel'>&nbsp;Id Compañía: </font><font class='CellContent'>" + intCustomerId + "</font>";
    if(intRegionId != 0 )     strCriterio = strCriterio + "<font class='CriteriaLabel'>&nbsp;Regi&oacute;n: </font><font class='CellContent'>" + intRegionId + "</font>";
    if( strCriterio.equals("") ) strCriterio = "<font class='CriteriaLabel'>&nbsp;Todos: </font><font class='CellContent'>*</font>";

    System.out.println("Criterio de Busqueda : " + strCriterio);


%>
<table  border="1" cellpadding="0" cellspacing="0" align="center" class="RegionBorder" width="100%">
    <form name="frmdatoslist" id="frmdatoslist" method="post" target="mainFrame">
        <tr>
            <table border="0" class="PortletHeaderColor" cellspacing="0" cellpadding="0" width="100%">
                <tr class="PortletHeaderColor">
                    <td class="LeftCurve" valign="top" align="left" width="10">&nbsp;&nbsp;</td>
                    <td class="PortletHeaderColor" align="left" valign="top">
                        <font class="PortletHeaderText">Listado de Compañías</font></td>
                    <td align="right" class="RightCurve" width="10%" >&nbsp;&nbsp;</td>
                </tr>
            </table>
        </tr>
        <tr valign="top">
            <table border="1" width="100%" cellpadding="2" cellspacing="0" class="RegionBorder" align="center" >
                <tr>
                    <td class="RegionHeaderColor">
                        <!--Inicio Linea de separación -->
                        <table border="0" cellspacing="2" cellpadding="0" width="80%" align="center" height="5">
                            <tr align="center"><td></td></tr>
                        </table>
                        <!--Fin de Linea de separación -->

                        <!-- Titulo Seccion -->
                        <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
                            <tr>
                                <td class="SectionTitleLeftCurve" width="10">&nbsp;&nbsp;</td>
                                <td class="SectionTitle">&nbsp;&nbsp;Criterios de B&uacute;squeda</td>
                                <td class="SectionTitleRightCurve" width="8">&nbsp;</td>
                            </tr>
                        </table>

                        <!--Inicio Linea de separación -->
                        <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center" height="5">
                            <tr align="center"><td></td></tr>
                        </table>
                        <!--Fin de Linea de separación -->

                        <!-- Criterios de Búsqueda -->
                        <table border="0" cellspacing="0" cellpadding="0" align="center" width="100%">
                            <tr bgcolor="#F5F5EB">
                                <td valign="top">
                                    <font class="CriteriaLabel">&nbsp;Nombres criterios : </font><font class="CellContent"><%=strCriterio%></font>
                                </td>
                                <td align="right" width="18%" valign="top">
                                    <a href="javascript:goToOtherSearch()"><img src="<%=Constante.PATH_APPORDER_SERVER%>/images/binocular.gif" border="0" width="88" height="24" alt="Otra B&uacute;squeda"></a>
                                </td>
                            </tr>
                        </table>

                        <!--Inicio Linea de separación -->
                        <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center" height="5">
                            <tr align="center"><td></td></tr>
                        </table>
                        <!--Fin de Linea de separación -->

                        <%
                            CustomerService objCustomer         = new CustomerService();
                            ArrayList      arrLista2            = new ArrayList();
                            HashMap        objHashMap1          = new HashMap();
                            String         strMessage2          = null;

                            System.out.println("*********************************");
                            System.out.println("npsalesstructid = " + request.getSession().getValue("npsalesstructid")+"");
                            System.out.println("swprovidergrpid = " + request.getSession().getValue("swprovidergrpid")+"");
                            System.out.println("*********************************");

                            //Provgrpid
                            String strProvgrpid = request.getSession().getValue("swprovidergrpid")==null?"":request.getSession().getValue("swprovidergrpid")+"";
                            strProvgrpid = strProvgrpid.trim();
                            intProGrpId = 0;
                            if(strProvgrpid.equals("")){
                                strProvgrpid = "";
                            }else{
                                intProGrpId = Integer.parseInt(strProvgrpid);
                            }

                            //Salesstructid
                            String strSalesStructId = request.getSession().getValue("npsalesstructid")==null?"":request.getSession().getValue("npsalesstructid")+"";
                            strSalesStructId = strSalesStructId.trim();
                            int intSalesStructId = 0;
                            if(strSalesStructId.equals("")){
                                strSalesStructId = "";
                            }else{
                                intSalesStructId = Integer.parseInt(strSalesStructId);
                            }

                            objHashMap1 = objCustomer.getCustomerSearch(strTipoDocumento,
                                    strNumeroDocumento,
                                    strRazonSocial,
                                    strNombreCliente,
                                    strTipoCliente,
                                    intCustomerId,
                                    intRegionId,
                                    strSessionCode,
                                    intSessionLevel,
                                    lPageSelected,
                                    intProGrpId,
                                    hdnSessionLogin,
                                    intSalesStructId,
                                    iUserId,
                                    iAppId
                            );

                            lNumTotalRegistros  = MiUtil.parseInt((String)objHashMap1.get("numTotalRegisters"));
                            arrLista2           = (ArrayList)objHashMap1.get("objArrayList");
                            request.setAttribute("arrCustomerLista2",arrLista2);
                            strMessage2   = (String)objHashMap1.get("strMessage");
                        %>

                        <display:table id="orders" name="arrCustomerLista2" class="orders" sort="external" partialList="true" size="<%=new Integer(lNumTotalRegistros)%>" requestURI="" pagesize="<%=Integer.parseInt(strHdnNumRegistros)%>" style="width: 100%">
                            <display:setProperty name="paging.banner.some_items_found"><span class="spanPaging"> {0} {1} encontrados, mostrando <%=(((lPageSelected-1)*iRowsByPage)+1)+" de "+((lPageSelected*iRowsByPage)>lNumTotalRegistros?lNumTotalRegistros:(lPageSelected*iRowsByPage))%>. </span></display:setProperty>
                            <display:column title="Nro" style="white-space: nowrap;" media="html" >
                                <a href='javascript:fxSelectCompanyId("<%=((HashMap) orders).get("wn_swcustomerid")%>")'>
                                    <img src="<%=Constante.PATH_APPORDER_SERVER%>/images/viewDetail.gif" width="15" height="15" border="0">
                                </a>
                                <%=((HashMap) orders).get("rownum")%>
                            </display:column>
                            <display:column property="wv_swruc" title="Ruc"/>
                            <display:column property="wv_swnamecom" title="Razón Social"/>
                            <display:column property="wv_swtype" title="Tipo Compa&ntilde;&iacute;a"/>
                            <display:column property="wv_swcreatedby" title="Creado por"/>
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
    function paginar(numPagina) {
        parametros ="";
        parametros += "&v_tip_documento=<%=strTipoDocumento%>";
        parametros += "&v_num_documento=<%=strNumeroDocumento%>";
        parametros += "&v_raz_social=<%=strRazonSocial%>";
        parametros += "&v_nom_customer=<%=strNombreCliente%>";
        parametros += "&v_tipo_person=<%=strTipoCliente%>";
        parametros += "&v_company_id=<%=intCustomerId%>";
        parametros += "&cmbRegion=<%=intRegionId%>";
        parametros += "&hdnProGrpId=<%=hdnProGrpId%>";
        parametros += "&hdnSessionLogin=<%=hdnSessionLogin%>";
        parametros += "&hdnSessionCode=<%=strSessionCode%>";
        parametros += "&hdnSessionLevel=<%=hdnSessionLevel%>";
        parametros += "&hdnUserId=<%=iUserId%>";
        parametros += "&hdnAppId=<%=iAppId%>";

        cadena = "<%=request.getContextPath()%>/htdocs/jsp/Order_CompaniaMassiveBuscarLista.jsp?"+"&d-447055-p="+numPagina;
        cadena += parametros;

        document.location.href = cadena;
    }

    function fxPrimeraPagina(param) {
        paginar(param);
    }

    function fxAnteriorPagina(param) {
        paginar((param - 1));
    }

    function fxSiguientePagina(param) {
        paginar((param + 1));
    }

    function fxUltimaPagina(param) {
        paginar(param);
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
</script>