<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@page import="java.util.*"%>
<%@page import="pe.com.nextel.service.GeneralService"%>
<%@page import="pe.com.nextel.bean.*"%>
<%@page import="pe.com.nextel.util.Constante"%>
<%@page import="pe.com.nextel.util.MiUtil"%>
<%@page import="javax.servlet.http.HttpSession"%>

<%
    System.out.println("--------- ORDER_COMPANIABUSCAR.JSP ---------");
    System.out.println("Session: " + request.isRequestedSessionIdValid());
    HttpSession sessionActual = request.getSession(false);
    if (request.isRequestedSessionIdValid()){


    String hdnProGrpId =(request.getParameter("hdnProGrpId")==null?"":request.getParameter("hdnProGrpId"));
    String hdnSessionLevel=(request.getParameter("hdnSessionLevel")==null?"":request.getParameter("hdnSessionLevel"));
    String hdnSessionCode=(request.getParameter("hdnSessionCode")==null?"":request.getParameter("hdnSessionCode"));
    String hdnSessionLogin=(request.getParameter("hdnSessionLogin")==null?"":request.getParameter("hdnSessionLogin"));
    int iUserId1 = MiUtil.parseInt((request.getParameter("hdnIUserId")==null?"0":request.getParameter("hdnIUserId")));
    int iAppId1 = MiUtil.parseInt((request.getParameter("hdnIAppId")==null?"0":request.getParameter("hdnIAppId")));

    System.out.println("[Inicio - Order_CompaniaBuscar.jsp]");
    System.out.println("hdnSessionLevel: "+hdnSessionLevel);
    System.out.println("hdnSessionCode: "+hdnSessionCode);
    System.out.println("hdnSessionLogin: "+hdnSessionLogin);
    System.out.println("hdnProGrpId --> "+hdnProGrpId);
    System.out.println("[Fin - Order_CompaniaBuscar.jsp]");
%>
<html>
<head>

    <title>ORDENES > BUSCAR COMPAÑIA</title>
    <link type="text/css" rel="stylesheet"
          href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"/>

    <script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>

    <script language="javascript">
        window.focus();
        var wn_rol_2050 = 0;
        var wn_rol_2051 = 0;

        function busquedaCustomer(){
            if (ContentOnlyNumber(document.frmdatos.v_company_id.value)){
                if (ContentOnlyNumber(document.frmdatos.v_num_documento.value)){
                    document.frmdatos.v_tipo_person.disabled = false;
                    /*if(!fxValidateFiltros()){
                     alert("Debe agregar un filtro más a su búsqueda")
                     document.frmdatos.v_raz_social.focus();
                     return false;
                     }*/
                    document.frmdatos.submit();
                    /* Deshabilitamos el comboBox de "Tipo Persona" solo para consultores */
                    if (wn_rol_2050 == 1 || wn_rol_2051 == 1 ){
                        document.frmdatos.v_tipo_person.disabled = true;
                    };
                }
                else {
                    alert("Número de documento debe ser numérico")
                    document.frmdatos.v_num_documento.focus();
                }
            }
            else{
                alert("Compañía ID debe ser numérico")
                document.frmdatos.v_company_id.focus();
            }
        }

        function cancelar(){
            if ("crear" == "crear"){
                if (parent.opener!=null)
                    if (parent.opener.parent.mainFrame.frmdatos.compania!=null){
                        parent.opener.parent.mainFrame.frmdatos.compania.focus();
                        parent.opener.parent.mainFrame.frmdatos.compania.select()
                    }
            };
            parent.close();
        }

        function fx_loadBody(){
            document.frmdatos.v_raz_social.focus();
        }

        function fxValidateObligatoryFields(){
            if ((document.frmdatos.v_tip_documento.value == "" || document.frmdatos.v_num_documento.value == "") && document.frmdatos.v_raz_social.value == "" && document.frmdatos.v_company_id.value == ""){
                alert("Ingrese como mínimo un criterio de búsqueda: Tipo y Número de Documento, Razón Social, Compañía ID o Código BSCS");
                return false;
            }else{
                return true;
            }
        }

        function fxAssignAction(){
            if ((document.frmdatos.v_tip_documento.value == "" || document.frmdatos.v_num_documento.value == "") && document.frmdatos.v_raz_social.value == "" && document.frmdatos.v_company_id.value == ""){
                document.frmdatos.action = "Order_CompaniaBuscar.jsp";
            }else{
                document.frmdatos.action = "Order_CompaniaBuscarLista.jsp";
            }
        }

        // Valida que si el usuario selecciona: Región, Tipo Persona o Tipo Doc, exista por lo menos un filtro mas
        /*function fxValidateFiltros(){
         v_form = document.frmdatos;
         if (v_form.v_num_documento.value != "" || v_form.v_raz_social.value != "" || v_form.v_nom_customer.value != "" || v_form.v_company_id.value != ""){
         return true;
         }
         else{
         return false;
         }
         }*/
    </script>
</head>

<body onload="javascript:fx_loadBody();">
<!--form method="post" name="frmdatos" action="Order_CompaniaBuscarLista.jsp"-->
<form method="post" name="frmdatos" onsubmit="return fxAssignAction();">
    <P>
        <input type="hidden" name="v_uso" value="crear">
    <table border="0" class="PortletHeaderColor" cellspacing="0" cellpadding="0" width="100%">
        <tr class="PortletHeaderColor">
            <td class="LeftCurve" valign="top" align="left" width="10" >&nbsp;&nbsp;</td>
            <td class="PortletHeaderColor"align="left" valign="top"> <font class="PortletHeaderText">Compa&ntilde;&iacute;as &gt; Buscar</font></td>
            <td align="right" class="RightCurve" width="10" >&nbsp;&nbsp;</td>
        </tr>
    </table>
    <table border="1" width="100%" cellpadding="0" cellspacing="0" align="center" class="RegionBorder" height="96">
        <tr valign="top">
            <td class="RegionHeaderColor" height="97">
                <br><br>
                <table border="0" cellspacing="0" cellpadding="0" width="97%" align="center">
                    <tr>
                        <td class="SectionTitleLeftCurve" width="10">&nbsp;&nbsp;</td>
                        <td class="SectionTitle">&nbsp;&nbsp;B&uacute;squeda de Compa&ntilde;&iacute;as por</td>
                        <td class="SectionTitleRightCurve" width="8">&nbsp;&nbsp;</td>
                    </tr>
                </table>
                <table border="0" cellspacing="2" cellpadding="0" width="98%" align="center" class="BannerSecondaryLink">
                    <tr>
                        <td class="CellLabel" align="left" width="242">&nbsp;Tipo Documento (*)</td>
                        <td class="CellContent" width="249">
                            <select name="v_tip_documento">
                                <option value=""></option>
                                <option value="LE">LE</option>
                                <option value="DNI">DNI</option>
                                <option value="RUC">RUC</option>
                                <option value="CE">Carnet Extranjer&iacute;a</option>
                                <option value="CIP">Cod. Ident. Personal</option>
                            </select>
                        </td>
                        <td class="CellLabel" width="249" align="left">&nbsp;N&uacute;mero Documento (*)</td>
                        <td class="CellContent" width="249"><input type="text" name="v_num_documento" size="15" value="<%=request.getParameter("txtCampoOtro")==null?"":request.getParameter("txtCampoOtro")%>"></td>
                    </tr>
                    <tr>
                        <td class="CellLabel" align="left" width="242">&nbsp;Raz&oacute;n Social (*)</td>
                        <td class="CellContent" colspan="3"><input type="text" name="v_raz_social" size="40" onchange="this.value=trim(this.value.toUpperCase())" onKeyPress="if (window.event.keyCode== 13) { this.value=trim(this.value.toUpperCase());  busquedaCustomer();}" value="<%=request.getParameter("txtCompany")==null?"":request.getParameter("txtCompany")%>">

                        </td>

                    </tr>
                    <tr>
                        <td class="CellLabel" align="left" width="242">&nbsp;Nombre de Compa&ntilde;&iacute;a </td>
                        <td class="CellContent" colspan="3"><input type="text" name="v_nom_customer" size="40" onchange="this.value=trim(this.value.toUpperCase())"></td>
                    </tr>

                    <tr>
                        <td class="CellLabel" align="left" width="242">&nbsp;Compañía ID (*)</td>
                        <td class="CellContent" colspan="3"><input type="text" name="v_company_id" size="40"></td>
                    </tr>
                    <tr>
                        <td class="CellLabel" align="left" width="242">&nbsp;Región </td>
                        <td class="CellContent" colspan="3">
                            <select name="cmbRegion" class="TextBlack">
                                <option value="">&nbsp;&nbsp;</option>
                                <%
                                    GeneralService objGeneralService = new GeneralService();
                                    HashMap hshDataMap = objGeneralService.getComboRegionList();
                                    ArrayList arrComboRegionList = (ArrayList) hshDataMap.get("arrComboRegionList");
                                    String strMessage =  (String) hshDataMap.get(Constante.MESSAGE_OUTPUT);
                                    if(strMessage == null || strMessage.substring(0,8).equals(Constante.SUCCESS_ORA_RESULT)) {
                                        for(int i = 0; i < arrComboRegionList.size(); i++ ){
                                            DominioBean objDominioBean = (DominioBean) arrComboRegionList.get(i);
                                %>
                                <option value="<%=objDominioBean.getValor()%>"><%=objDominioBean.getDescripcion()%></option>
                                <%
                                        }
                                    }
                                %>
                            </select>


                        </td>
                    </tr>
                    <tr>
                        <td class="CellLabel" align="left" width="242">&nbsp;Tipo Persona </td>
                        <td class="CellContent" colspan="3">
                            <select name="v_tipo_person">
                                <option value=""></option>
                                <option value="Customer">Customer</option>
                                <option value="Prospect">Prospect</option>
                            </select>

                        </td>
                    </tr>
                </table>
                <br>
            </td>
        </tr>
    </table>
    <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
        <tr align="center">
            <td colspan="3">
                <br>
                <input type="submit" name="v_buscar" value="Buscar" onclick="javascript:busquedaCustomer();fxValidateObligatoryFields();">
                <input type="button" name="v_cancelar" value="Cancelar" onclick="javascript:cancelar();">
            </td>
        </tr>
    </table>
    <!-- Seccion en donde se recuperan las variables de session -->
    <!--  -->
    <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
        <tr align="center">
            <td >
                <input type="hidden" name="numPagina" size="15" value="1">
                <input type="hidden" name="paginado" size="15"  value="0">
                <input type="hidden" name="hdnSessionLevel"     value="<%=hdnSessionLevel%>">
                <input type="hidden" name="hdnSessionCode"      value="<%=hdnSessionCode%>">
                <input type="hidden" name="hdnSessionLogin"     value="<%=hdnSessionLogin%>">
                <input type="hidden" name="hdnProGrpId"         value="<%=hdnProGrpId%>">
                <input type="hidden" name="hdnIUserId"          value="<%=iUserId1%>">
                <input type="hidden" name="hdnIAppId"           value="<%=iAppId1%>">
            </td>
        </tr>
    </table>
</form>
</body>
</html>
<%
    }
    else{
        Date fecha = new Date();
        String ipAddress =  request.getRemoteAddr();
        System.out.println("INGRESO NO AUTORIZADO A ORDER_COMPANIABUSCAR.JSP");
        System.out.println("IP Address: "+ipAddress);
        System.out.println("Fecha: "+fecha);
        System.out.println("URL: "+request.getRequestURL()+(request.getQueryString() != null ? "?" + request.getQueryString() : ""));
        sessionActual.invalidate();
        System.out.println("REDIRECCIONANDO...");
        response.sendRedirect("/portal/pls/portal/NPR_PORTAL.wwsec_app_priv.login?p_requested_url=NPR_PORTAL.home");
        return;
    }

%>