<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="pe.com.nextel.util.MiUtil"%>
<%@page import="java.util.*"%>
<%@page import="pe.com.nextel.util.Constante" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/library.js"></script>
<script language="javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/BasicOperations.js"></script>
<script type="text/javascript" src="<%=Constante.PATH_APPORDER_SERVER%>/Resource/jQuery-min.js"></script>

<%
    System.out.println("INICIO SESSION - Verificacion Biometrica");
    try{
        int idOrder=Integer.parseInt(request.getParameter("idOrder"));
        String strDocument  =request.getParameter("strDocument");
        String strRutaContext=request.getContextPath();
        String strName=request.getParameter("strName");
        String strApePat=request.getParameter("strApePat");
        String strApemat=request.getParameter("strApemat");
        String strVigencia=request.getParameter("strVigencia");
        String strRestriccion=request.getParameter("strRestriccion");
        String strMessgae=request.getParameter("message");

        String strSource = request.getParameter("strSource");

        String actionURL_NormalizarServlet    = strRutaContext+"/normalizarDireccionServlet";
        String strURLNormalizarEdit = strRutaContext+"/POPUPNORMALIZACION/PopUpNormalizarEdit.jsp";
        String strURLNormalizarMain = strRutaContext+"/POPUPNORMALIZACION/PopUpFrameNormalizar.jsp";

%>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="<%=Constante.PATH_APPORDER_SERVER%>/Resource/salesweb.css"/>
    <title>Verificaci&oacute;n  Biom&eacute;trica </title>
    <script type="text/javascript">
        window.onbeforeunload = closeExitoso;
        function closeExitoso(){
            <%if(strSource !=  null && strSource.equals(Constante.SOURCE_VIA)){%>
            parent.opener.parent.mainFrame.fxCleanForm();
            window.close();
            <%}else{%>
            var server='<%=actionURL_NormalizarServlet%>';
            var params = 'myaction=validarCargaNorm&orderID='+'<%=idOrder%>';
            jQuery.ajax({
                url: server,
                data: params,
                type: "POST",
                dataType: 'json',
                success:function(data){
                    if(data.valida == 1){
                        var datosNorm = data.datosNorm;
                        var url = "<%=strURLNormalizarEdit%>?datosNorm="+datosNorm;
                        var winUrl = "<%=strURLNormalizarMain%>?&av_url=" + url;
                        var win = parent.opener.open(winUrl, "Normalizacion","toolbar = no, location = no, directories = no, status = no, menubar = no, scrollbars = yes, resizable = yes, screenX = 60, top = 40, left = 60, screenY = 40, width = 710,height=410");
                    }else{
                        parent.opener.parent.mainFrame.editOrderBiometric("ok");
                    }
                },
                error:function(){
                    parent.opener.parent.mainFrame.editOrderBiometric("ok");
                },
                complete: function(){
                    parent.close();
                }
            });
            <%}%>
        }

    </script>
</head>
<body>
<table  align="center" width="100%" border="0">

    <tr>
        <td>
            <table border="0" class="PortletHeaderColor" cellspacing="0" cellpadding="0" width="100%">
                <tr class="PortletHeaderColor">
                    <td class="LeftCurve" valign="top" align="left" width="10">&nbsp;&nbsp;</td>
                    <td class="PortletHeaderColor" align="left" valign="top">
                        <font class="PortletHeaderText">
                            Verificaci&oacute;n Biom&eacute;trica
                        </font></td>
                    <td align="right" class="RightCurve" width="10%" >&nbsp;&nbsp;</td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <table align="center" width="100%" border="0">
                <tr>
                    <td class="CellLabel" align="left" valign="top">Mensaje:</td>
                    <td align="left" class="CellContent"><label>
                        Validaci&oacute;n Exitosa.
                    </label></td>
                </tr>
                <tr>
                    <td class="CellLabel" align="left" valign="top">DNI:</td>
                    <td align="left" class="CellContent"><label>
                        <% if( !StringUtils.isBlank(strDocument)){ %>
                        <%=strDocument%>
                        <% }else{ %>  -   <% } %>
                    </label></td>
                </tr>
                <tr><td class="CellLabel" align="left" valign="top">Nombre:</td>
                    <td align="left" class="CellContent"><label>
                        <% if( !StringUtils.isBlank(strName)){ %>
                        <%=strName%>
                        <% }else{ %>  -   <% } %>
                    </label></td>
                </tr>
                <tr>
                    <td class="CellLabel" align="left" valign="top">Ap. Paterno:</td>
                    <td align="left" class="CellContent"><label>
                        <% if( !StringUtils.isBlank(strApePat)){ %>
                        <%=strApePat%>
                        <% }else{ %>  -   <% } %>
                    </label></td>
                </tr>
                <tr>
                    <td class="CellLabel" align="left" valign="top">Ap. Materno:</td>
                    <td align="left" class="CellContent"><label>
                        <% if( !StringUtils.isBlank(strApemat)){ %>
                        <%=strApemat%>
                        <% }else{ %>  -   <% } %>
                    </label></td>
                </tr>
                <tr>
                    <td class="CellLabel" align="left" valign="top">Vigencia:</td>
                    <td align="left" class="CellContent"><label>
                        <% if( !StringUtils.isBlank(strVigencia)){ %>
                        <%=strVigencia%>
                        <% }else{ %>  -   <% } %>
                    </label></td>
                </tr>
                <tr><th height="5px"></th></tr>
                <tr>
                    <td colspan="2"><input type="button" value="OK" onclick="closeExitoso()"  style="padding: 3px" /></td>
                </tr>

            </table>


        </td>
    </tr>
</table>


</body>
</html>

<% }catch(Exception ex){
    ex.printStackTrace();
%>
<script>alert('<%=MiUtil.getMessageClean(ex.getMessage())%>');</script>
<%}%>